# SinSo API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![SQLite](https://img.shields.io/badge/SQLite-3.50.3-blue.svg)](https://www.sqlite.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**The Largest Open-Source Sinhala Songs Lyrics API** - A comprehensive REST API for accessing Sinhala song lyrics, artist information, and song metadata with support for both English and Sinhala content.

## Project Overview

SinSo API is a Spring Boot-based REST API that provides access to a comprehensive database of Sinhala songs, artists, and lyrics. The API supports bilingual content (English and Sinhala) and offers powerful search capabilities across all data fields.

### Key Features

- **Comprehensive Song Database**: Access to extensive collection of Sinhala songs
- **Artist Information**: Detailed artist profiles with associated songs
- **Lyrics Access**: Full lyrics content in both Sinhala and English
- **Advanced Search**: Multi-field search across songs, artists, and lyrics
- **Bilingual Support**: Full support for both English and Sinhala content
- **Interactive Documentation**: Built-in Swagger/OpenAPI documentation
- **Rate Limiting**: Built-in rate limiting for API protection
- **Web Interface**: User-friendly web interface for database exploration

## Quick Start

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/SinSo-API/SinSo-API.git
   cd SinSo-API
   ```

2. **Build the project**
   ```bash
   ./mvnw clean install
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the API**
   - API Base URL: `http://localhost:8080`
   - Interactive Documentation: `http://localhost:8080/v1/docs/`
   - Web Interface: `http://localhost:8080/`

> **Note**: A future update will allow users to self-host the API by themselves using Docker for easier deployment and management.

## API Documentation

### Base URL
```
http://localhost:8080/v1
```

### Available Endpoints

#### Songs
- `GET /songs` - Get all songs summary
- `GET /songs/{songId}` - Get specific song details

#### Artists
- `GET /artists` - Get all artists summary
- `GET /artists/{artistId}` - Get specific artist with songs

#### Lyrics
- `GET /lyrics/{lyricId}` - Get specific song lyrics

#### Search
- `GET /search?all={query}` - Global search across all fields
- `GET /search?artist={name}` - Search by artist name
- `GET /search?title={title}` - Search by song title
- `GET /search?lyrics={content}` - Search by lyrics content

### Search Parameters
- `all` - Global search query (searches across all fields)
- `artist` - Search by artist name
- `title` - Search by song title
- `lyrics` - Search by lyrics content
- `page` - Page number for pagination (default: 0)
- `size` - Results per page (default: 20, max: 100)

## Usage Examples

### Get All Songs
```bash
curl "http://localhost:8080/v1/songs"
```

### Get Specific Song
```bash
curl "http://localhost:8080/v1/songs/song_123"
```

### Search Songs
```bash
# Global search
curl "http://localhost:8080/v1/search?all=love"

# Search by artist
curl "http://localhost:8080/v1/search?artist=එඩ්වඩ් ජයකොඩි"

# Search by title
curl "http://localhost:8080/v1/search?title=මල්"

# Search by lyrics
curl "http://localhost:8080/v1/search?lyrics=සිහින"
```

### Get Artist Information
```bash
curl "http://localhost:8080/v1/artists/artist_456"
```

### Get Song Lyrics
```bash
curl "http://localhost:8080/v1/lyrics/lyric_789"
```

## Development

### Project Structure
```
src/
├── main/
│   ├── java/com/vishalrashmika/sinso/api/
│   │   ├── Artists/          # Artist-related endpoints
│   │   ├── Songs/            # Song-related endpoints
│   │   ├── Lyrics/           # Lyrics-related endpoints
│   │   ├── Search/           # Search functionality
│   │   ├── Config/           # Configuration classes
│   │   ├── Errors/           # Error handling
│   │   └── Utils/            # Utility classes
│   └── resources/
│       ├── application.properties
│       ├── db/               # SQLite database
│       └── static/           # Web interface
└── test/                     # Test files
```

### Technology Stack
- **Backend**: Spring Boot 3.5.5
- **Database**: SQLite 3.50.3
- **Documentation**: OpenAPI 3 (Swagger)
- **Rate Limiting**: Bucket4j
- **Build Tool**: Maven
- **Java Version**: 21

### Configuration
The application can be configured via `application.properties`:
- Database connection settings
- Rate limiting parameters
- Swagger documentation settings
- Server port configuration

## Docker Support

The project includes a Dockerfile for containerized deployment:

```bash
# Build Docker image
docker build -t sinso-api .

# Run container
docker run -p 8080:8080 sinso-api
```

## Contributing

We welcome contributions to the SinSo API project! Here's how you can help:

### How to Contribute

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Make your changes**
4. **Add tests** for new functionality
5. **Commit your changes**
   ```bash
   git commit -m "Add: your feature description"
   ```
6. **Push to your fork**
   ```bash
   git push origin feature/your-feature-name
   ```
7. **Create a Pull Request**

### Contribution Guidelines

- Follow Java coding standards
- Add appropriate tests for new features
- Update documentation for API changes
- Ensure all tests pass before submitting PR
- Use descriptive commit messages
- Follow the existing code style and structure

### Areas for Contribution

- Bug fixes
- New features
- Documentation improvements
- Test coverage
- UI/UX enhancements
- Additional language support
- Performance optimizations

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

- **Developer**: Vishal Rashmika
- **Email**: vishal@vishalrashmika.com
- **Website**: [vishalrashmika.com](https://vishalrashmika.com)

## Acknowledgments

- Thanks to all contributors who help maintain this project
- Special thanks to the Sinhala music community for their support
- Built with love for the Sinhala music lovers worldwide

---

**Made with love for the Sinhala Music Community**