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
        
        this.initEventListeners();
    }
    
    initEventListeners() {
        this.searchBtn.addEventListener('click', () => this.performSearch());
        
        this.searchInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                this.performSearch();
            }
        });
        
        this.searchInput.addEventListener('input', (e) => {
            if (e.target.value.length > 2) {
                this.debounce(() => this.performSearch(), 300)();
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
    
    async performSearch() {
        const query = this.searchInput.value.trim();
        const column = this.columnSelect.value;
        
        if (query.length < 1) {
            this.clearResults();
            return;
        }
        
        this.showLoading();
        
        try {
            let url;
            if (column === 'all') {
                url = `/api/search?query=${encodeURIComponent(query)}`;
            } else {
                url = `/api/search/${column}?query=${encodeURIComponent(query)}`;
            }
            
            const response = await fetch(url);
            
            if (!response.ok) {
                throw new Error('Search failed');
            }
            
            const data = await response.json();
            this.displayResults(data, query);
            
        } catch (error) {
            console.error('Search error:', error);
            this.showError();
        } finally {
            this.hideLoading();
        }
    }
    
    showLoading() {
        this.loading.style.display = 'block';
        this.results.style.display = 'none';
        this.noResults.style.display = 'none';
    }
    
    hideLoading() {
        this.loading.style.display = 'none';
        this.results.style.display = 'block';
    }
    
    displayResults(data, query) {
        if (data.length === 0) {
            this.showNoResults();
            return;
        }
        
        this.resultsCount.textContent = `Found ${data.length} result${data.length === 1 ? '' : 's'} for "${query}"`;
        
        this.results.innerHTML = data.map(item => this.createResultItem(item)).join('');
        this.noResults.style.display = 'none';
    }
    
    createResultItem(item) {
        const fullName = `${item.first_name || ''} ${item.last_name || ''}`.trim();
        
        return `
            <div class="result-item">
                <h3>${this.escapeHtml(fullName || 'No Name')}</h3>
                <p><strong>First Name:</strong> ${this.escapeHtml(item.first_name || 'N/A')}</p>
                <p><strong>Last Name:</strong> ${this.escapeHtml(item.last_name || 'N/A')}</p>
                <p><strong>Email:</strong> ${this.escapeHtml(item.email || 'N/A')}</p>
                <p><strong>ID:</strong> ${this.escapeHtml(item.id || 'N/A')}</p>
            </div>
        `;
    }
    
    formatKey(key) {
        return key.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase());
    }
    
    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
    
    showNoResults() {
        this.noResults.style.display = 'block';
        this.results.innerHTML = '';
        this.resultsCount.textContent = '';
    }
    
    showError() {
        this.results.innerHTML = '<div class="result-item"><p style="color: red;">An error occurred while searching. Please try again.</p></div>';
        this.resultsCount.textContent = '';
    }
    
    clearResults() {
        this.results.innerHTML = '';
        this.resultsCount.textContent = '';
        this.noResults.style.display = 'none';
    }
}

// Initialize the search functionality when the page loads
document.addEventListener('DOMContentLoaded', () => {
    new DatabaseSearch();
});