// Marketaux API Configuration
var API_KEY = 'eujbMLycKaoQbNXxe3uV1NHWhoybH3NLio9hV8j0';
var API_BASE_URL = 'https://api.marketaux.com/v1/news/all';

// Current category filter
var currentCategory = 'all';

// Fetch news on page load
document.addEventListener('DOMContentLoaded', function() {
    fetchNews();
    setupCategoryFilters();
});

// Setup category filter buttons
function setupCategoryFilters() {
    var filterBtns = document.querySelectorAll('.filter-btn');

    filterBtns.forEach(function(btn) {
        btn.addEventListener('click', function() {
            // Update active state
            filterBtns.forEach(function(b) { b.classList.remove('active'); });
            btn.classList.add('active');

            // Update category and fetch
            currentCategory = btn.getAttribute('data-category');
            fetchNews();
        });
    });
}

// Fetch news from Marketaux API
function fetchNews() {
    var loadingState = document.getElementById('loadingState');
    var errorState = document.getElementById('errorState');
    var newsGrid = document.getElementById('newsGrid');

    // Show loading, hide others
    loadingState.style.display = 'flex';
    errorState.style.display = 'none';
    newsGrid.innerHTML = '';

    // Build API URL
    var url = API_BASE_URL + '?api_token=' + API_KEY + '&language=en&limit=12';

    // Add category filter if not "all"
    if (currentCategory !== 'all') {
        url += '&categories=' + currentCategory;
    }

    // Add some randomness by using different pages
    var randomPage = Math.floor(Math.random() * 5) + 1;
    url += '&page=' + randomPage;

    fetch(url)
        .then(function(response) {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(function(data) {
            loadingState.style.display = 'none';

            if (data.data && data.data.length > 0) {
                displayNews(data.data);
            } else {
                showError('No news articles found.');
            }
        })
        .catch(function(error) {
            console.error('Error fetching news:', error);
            loadingState.style.display = 'none';
            showError('Unable to load news. Please try again later.');
        });
}

// Display news articles in grid
function displayNews(articles) {
    var newsGrid = document.getElementById('newsGrid');
    newsGrid.innerHTML = '';

    articles.forEach(function(article) {
        var card = createNewsCard(article);
        newsGrid.appendChild(card);
    });
}

// Create a news card element
function createNewsCard(article) {
    var card = document.createElement('article');
    card.className = 'news-card';

    // Format date
    var publishedDate = new Date(article.published_at);
    var formattedDate = publishedDate.toLocaleDateString('en-US', {
        month: 'short',
        day: 'numeric',
        year: 'numeric'
    });

    // Get category
    var category = 'General';
    if (article.categories && article.categories.length > 0) {
        category = article.categories[0].charAt(0).toUpperCase() + article.categories[0].slice(1);
    }

    // Get image or use placeholder
    var imageUrl = article.image_url || 'https://via.placeholder.com/400x200/1a1a1c/4a9eff?text=News';

    // Truncate description
    var description = article.description || article.snippet || 'No description available.';
    if (description.length > 150) {
        description = description.substring(0, 150) + '...';
    }

    // Build card HTML
    card.innerHTML =
        '<div class="card-image">' +
        '<img src="' + imageUrl + '" alt="' + escapeHtml(article.title) + '" onerror="this.src=\'https://via.placeholder.com/400x200/1a1a1c/4a9eff?text=News\'">' +
        '<span class="card-category">' + escapeHtml(category) + '</span>' +
        '</div>' +
        '<div class="card-content">' +
        '<div class="card-meta">' +
        '<span class="card-source">' + escapeHtml(article.source || 'Unknown') + '</span>' +
        '<span class="card-date">' + formattedDate + '</span>' +
        '</div>' +
        '<h3 class="card-title">' + escapeHtml(article.title) + '</h3>' +
        '<p class="card-description">' + escapeHtml(description) + '</p>' +
        '<a href="' + article.url + '" target="_blank" rel="noopener noreferrer" class="card-link">' +
        'Read More <span class="arrow">→</span>' +
        '</a>' +
        '</div>';

    return card;
}

// Show error state
function showError(message) {
    var errorState = document.getElementById('errorState');
    errorState.querySelector('p').textContent = message;
    errorState.style.display = 'flex';
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
    if (!text) return '';
    var div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}