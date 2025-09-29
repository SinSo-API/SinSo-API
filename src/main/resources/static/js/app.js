// js/app.js
class DatabaseSearch {
    constructor() {
        this.searchInput = document.getElementById('searchInput');
        this.searchBtn = document.getElementById('searchBtn');
        this.columnSelect = document.getElementById('columnSelect');
        this.results = document.getElementById('results');
        this.resultsCount = document.getElementById('results-count');
        this.loading = document.getElementById('loading');
        this.noResults = document.getElementById('no-results');
        this.pagination = document.getElementById('pagination');
        
        this.currentPage = 0;
        this.pageSize = 20;
        this.lastSearchData = null;
        
        this.initEventListeners();
    }
    
    removeDuplicates(songs) {
        const seen = new Set();
        return songs.filter(song => {
            // Create unique identifier based on multiple fields
            const identifier = `${song.songNameSinhala || ''}|${song.songNameEnglish || ''}|${song.artistNameSinhala || ''}|${song.artistNameEnglish || ''}|${song.composer || ''}`.toLowerCase();
            
            if (seen.has(identifier)) {
                return false;
            }
            seen.add(identifier);
            return true;
        });
    }
    
    highlightSearchTerm(text, searchTerm) {
        if (!text || !searchTerm) return text;
        
        const regex = new RegExp(`(${this.escapeRegex(searchTerm)})`, 'gi');
        return text.replace(regex, '<mark>$1</mark>');
    }
    
    escapeRegex(text) {
        return text.replace(/[.*+?^${}()|[\]\\]/g, '\\    }');
    }
    
    truncateText(text, maxLength = 150) {
        if (!text || text.length <= maxLength) return text;
        return text.substring(0, maxLength) + '...';
    }
    
    initEventListeners() {
        this.searchBtn.addEventListener('click', () => this.performSearch());
        
        this.searchInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                this.performSearch();
            }
        });
        
        this.searchInput.addEventListener('input', (e) => {
            const query = e.target.value.trim();
            if (query.length === 0) {
                this.clearResults();
            } else if (query.length > 2) {
                this.debounce(() => this.performSearch(0, true), 800)();
            }
        });
        
        this.columnSelect.addEventListener('change', () => {
            const query = this.searchInput.value.trim();
            if (query.length > 0) {
                this.performSearch(0, true);
            }
        });
    }
    
    debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }
    
    async performSearch(page = 0, resetPage = true) {
        const query = this.searchInput.value.trim();
        const searchType = this.columnSelect.value;
        
        if (query.length < 1) {
            this.clearResults();
            return;
        }
        
        // Reset to first page for new searches
        if (resetPage) {
            page = 0;
        }
        
        this.currentPage = page;
        this.showLoading();
        
        try {
            const params = new URLSearchParams();
            
            // Set search parameter based on selected type
            if (searchType === 'all') {
                params.append('all', query);
            } else {
                params.append(searchType, query);
            }
            
            // Add pagination parameters
            params.append('page', page.toString());
            params.append('size', this.pageSize.toString());
            
            const url = `/v1/search?${params.toString()}`;
            console.log('Making request to:', url); // Debug log
            
            const response = await fetch(url);
            console.log('Response status:', response.status); // Debug log
            
            if (!response.ok) {
                const errorText = await response.text();
                console.error('Error response:', errorText);
                throw new Error(`Search failed: ${response.status} - ${errorText}`);
            }
            
            const data = await response.json();
            console.log('Received data:', data); // Debug log
            
            // Handle different response structures
            let songs = [];
            let total = 0;
            let responseData = {};
            
            if (data.error) {
                // Handle error responses from backend
                throw new Error(data.error);
            }
            
            if (data.songs !== undefined) {
                // Expected structure from your backend
                songs = Array.isArray(data.songs) ? data.songs : [];
                total = data.total || 0;
                responseData = {
                    songs: songs,
                    total: total,
                    page: data.page || page,
                    size: data.size || this.pageSize,
                    totalPages: data.totalPages || Math.ceil(total / this.pageSize),
                    hasNext: data.hasNext || false,
                    hasPrevious: data.hasPrevious || false
                };
            } else if (Array.isArray(data)) {
                // If response is directly an array of songs
                songs = data;
                total = songs.length;
                responseData = {
                    songs: songs,
                    total: total,
                    page: page,
                    size: this.pageSize,
                    totalPages: Math.ceil(total / this.pageSize),
                    hasNext: (page + 1) * this.pageSize < total,
                    hasPrevious: page > 0
                };
            } else {
                // Unexpected response structure
                console.warn('Unexpected response structure:', data);
                throw new Error('Unexpected response format from server');
            }
            
            // Remove duplicates on client side as additional safety
            if (songs.length > 0) {
                songs = this.removeDuplicates(songs);
                responseData.songs = songs;
                responseData.total = songs.length;
            }
            
            this.lastSearchData = responseData;
            this.displayResults(responseData, query);
            
        } catch (error) {
            console.error('Search error:', error);
            this.showError(`Search failed: ${error.message}`);
        } finally {
            this.hideLoading();
        }
    }
    
    showLoading() {
        this.loading.style.display = 'block';
        this.results.style.display = 'none';
        this.noResults.style.display = 'none';
        this.pagination.style.display = 'none';
    }
    
    hideLoading() {
        this.loading.style.display = 'none';
        this.results.style.display = 'block';
    }
    
    displayResults(data, query) {
        if (!data.songs || data.songs.length === 0) {
            this.showNoResults();
            return;
        }
        
        // Sort results by relevance
        const sortedSongs = this.sortByRelevance(data.songs, query);
        
        this.resultsCount.textContent = `Found ${data.total} result${data.total === 1 ? '' : 's'} for "${query}"${data.totalPages > 1 ? ` (Page ${data.page + 1} of ${data.totalPages})` : ''}`;
        
        this.results.innerHTML = sortedSongs.map(song => this.createResultItem(song, query)).join('');
        this.noResults.style.display = 'none';
        
        this.createPagination(data);
    }
    
    sortByRelevance(songs, query) {
        const queryLower = query.toLowerCase();
        
        return songs.sort((a, b) => {
            let scoreA = 0;
            let scoreB = 0;
            
            // Exact matches get highest priority
            if ((a.songNameEnglish || '').toLowerCase() === queryLower || 
                (a.songNameSinhala || '').toLowerCase() === queryLower) scoreA += 100;
            if ((b.songNameEnglish || '').toLowerCase() === queryLower || 
                (b.songNameSinhala || '').toLowerCase() === queryLower) scoreB += 100;
            
            // Title starts with query gets high priority
            if ((a.songNameEnglish || '').toLowerCase().startsWith(queryLower) || 
                (a.songNameSinhala || '').toLowerCase().startsWith(queryLower)) scoreA += 50;
            if ((b.songNameEnglish || '').toLowerCase().startsWith(queryLower) || 
                (b.songNameSinhala || '').toLowerCase().startsWith(queryLower)) scoreB += 50;
            
            // Artist name matches get medium priority
            if ((a.artistNameEnglish || '').toLowerCase().includes(queryLower) || 
                (a.artistNameSinhala || '').toLowerCase().includes(queryLower)) scoreA += 25;
            if ((b.artistNameEnglish || '').toLowerCase().includes(queryLower) || 
                (b.artistNameSinhala || '').toLowerCase().includes(queryLower)) scoreB += 25;
            
            // Title contains query gets medium priority
            if ((a.songNameEnglish || '').toLowerCase().includes(queryLower) || 
                (a.songNameSinhala || '').toLowerCase().includes(queryLower)) scoreA += 20;
            if ((b.songNameEnglish || '').toLowerCase().includes(queryLower) || 
                (b.songNameSinhala || '').toLowerCase().includes(queryLower)) scoreB += 20;
            
            // Composer/Lyricist matches get lower priority
            if ((a.composer || '').toLowerCase().includes(queryLower) || 
                (a.lyricist || '').toLowerCase().includes(queryLower)) scoreA += 10;
            if ((b.composer || '').toLowerCase().includes(queryLower) || 
                (b.lyricist || '').toLowerCase().includes(queryLower)) scoreB += 10;
            
            // Lyrics matches get lowest priority
            if ((a.lyricsPreview || '').toLowerCase().includes(queryLower)) scoreA += 5;
            if ((b.lyricsPreview || '').toLowerCase().includes(queryLower)) scoreB += 5;
            
            return scoreB - scoreA;
        });
    }
    
    createResultItem(song, query) {
        const searchTerm = query || '';
        
        // Safely get song properties with fallbacks
        const titleSinhala = song.songNameSinhala || song.song_name_sinhala || '';
        const titleEnglish = song.songNameEnglish || song.song_name_english || '';
        const artistSinhala = song.artistNameSinhala || song.artist_name_sinhala || '';
        const artistEnglish = song.artistNameEnglish || song.artist_name_english || '';
        const composer = song.composer || '';
        const lyricist = song.lyricist || '';
        const releaseYear = song.releaseYear || song.release_year || '';
        const genre = song.genre || '';
        const lyricsPreview = song.lyricsPreview || song.lyrics_preview || song.lyrics || '';
        
        return `
            <div class="result-item">
                <h3>${this.highlightSearchTerm(this.escapeHtml(titleSinhala || titleEnglish || 'No Title'), searchTerm)}</h3>
                ${titleEnglish && titleSinhala && titleSinhala !== titleEnglish ? 
                    `<p class="subtitle">${this.highlightSearchTerm(this.escapeHtml(titleEnglish), searchTerm)}</p>` : ''}
                
                <div class="song-details">
                    <p><strong>Artist:</strong> ${this.highlightSearchTerm(this.escapeHtml(artistSinhala || artistEnglish || 'N/A'), searchTerm)}</p>
                    ${artistEnglish && artistSinhala && artistSinhala !== artistEnglish ? 
                        `<p><strong>Artist (EN):</strong> ${this.highlightSearchTerm(this.escapeHtml(artistEnglish), searchTerm)}</p>` : ''}
                    
                    ${composer ? `<p><strong>Composer:</strong> ${this.highlightSearchTerm(this.escapeHtml(composer), searchTerm)}</p>` : ''}
                    ${lyricist ? `<p><strong>Lyricist:</strong> ${this.highlightSearchTerm(this.escapeHtml(lyricist), searchTerm)}</p>` : ''}
                    
                    <div class="song-meta">
                        ${releaseYear ? `<span class="meta-item"><strong>Year:</strong> ${this.escapeHtml(releaseYear)}</span>` : ''}
                        ${genre ? `<span class="meta-item"><strong>Genre:</strong> ${this.escapeHtml(genre)}</span>` : ''}
                    </div>
                </div>
                
                ${lyricsPreview ? `
                    <div class="lyrics-preview">
                        <strong>Lyrics Preview:</strong><br>
                        <div class="lyrics-content">
                            ${this.highlightSearchTerm(this.escapeHtml(this.truncateText(lyricsPreview, 200)), searchTerm)}
                        </div>
                    </div>` : ''}
            </div>
        `;
    }
    
    createPagination(data) {
        if (data.totalPages <= 1) {
            this.pagination.style.display = 'none';
            return;
        }
        
        this.pagination.style.display = 'flex';
        let paginationHTML = '';
        
        // Previous button
        if (data.hasPrevious) {
            paginationHTML += `<button class="pagination-btn" onclick="searchInstance.performSearch(${data.page - 1}, false)">Previous</button>`;
        }
        
        // Page numbers
        const startPage = Math.max(0, data.page - 2);
        const endPage = Math.min(data.totalPages - 1, data.page + 2);
        
        if (startPage > 0) {
            paginationHTML += `<button class="pagination-btn" onclick="searchInstance.performSearch(0, false)">1</button>`;
            if (startPage > 1) {
                paginationHTML += `<span class="pagination-dots">...</span>`;
            }
        }
        
        for (let i = startPage; i <= endPage; i++) {
            const isActive = i === data.page ? 'active' : '';
            paginationHTML += `<button class="pagination-btn ${isActive}" onclick="searchInstance.performSearch(${i}, false)">${i + 1}</button>`;
        }
        
        if (endPage < data.totalPages - 1) {
            if (endPage < data.totalPages - 2) {
                paginationHTML += `<span class="pagination-dots">...</span>`;
            }
            paginationHTML += `<button class="pagination-btn" onclick="searchInstance.performSearch(${data.totalPages - 1}, false)">${data.totalPages}</button>`;
        }
        
        // Next button
        if (data.hasNext) {
            paginationHTML += `<button class="pagination-btn" onclick="searchInstance.performSearch(${data.page + 1}, false)">Next</button>`;
        }
        
        this.pagination.innerHTML = paginationHTML;
    }
    
    escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
    
    showNoResults() {
        this.noResults.style.display = 'block';
        this.results.innerHTML = '';
        this.resultsCount.textContent = '';
        this.pagination.style.display = 'none';
    }
    
    showError(message = 'An error occurred while searching. Please try again.') {
        this.results.innerHTML = `<div class="result-item"><p style="color: red;">${message}</p></div>`;
        this.resultsCount.textContent = '';
        this.pagination.style.display = 'none';
    }
    
    clearResults() {
        this.results.innerHTML = '';
        this.resultsCount.textContent = '';
        this.noResults.style.display = 'none';
        this.pagination.style.display = 'none';
    }
}

// Make searchInstance global for pagination callbacks
let searchInstance;

// Initialize the search functionality when the page loads
document.addEventListener('DOMContentLoaded', () => {
    searchInstance = new DatabaseSearch();
});