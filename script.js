let player;
let currentApiKey = localStorage.getItem('zotube_api_key') || '';

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    initNavigation();
    initSettings();
    initSearch();
    initCategories();
    initDirectPlay();
    loadCategoryVideos('Trending');
    updateApiStatus();
});

// ... (previous functions)

// Direct Play logic
function initDirectPlay() {
    const input = document.getElementById('direct-input');
    const btn = document.getElementById('direct-play-btn');

    btn.addEventListener('click', () => {
        let val = input.value.trim();
        if (!val) return;

        // Try to extract ID from URL
        let videoId = val;
        if (val.includes('youtu.be/')) {
            videoId = val.split('youtu.be/')[1].split('?')[0];
        } else if (val.includes('v=')) {
            videoId = val.split('v=')[1].split('&')[0];
        } else if (val.includes('embed/')) {
            videoId = val.split('embed/')[1].split('?')[0];
        }

        openPlayer(videoId);
    });
}

// Navigation logic
function initNavigation() {
    const tabs = document.querySelectorAll('#sidebar li');
    const sections = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const target = tab.getAttribute('data-tab');
            
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            sections.forEach(s => {
                s.classList.remove('active');
                if (s.id === `${target}-tab`) s.classList.add('active');
            });
            
            // If home is selected, ensure trending is loaded
            if (target === 'home') {
                const activeCategory = document.querySelector('.category-chip.active');
                if (activeCategory) loadCategoryVideos(activeCategory.getAttribute('data-category'));
            }
        });
    });
}

// Categories logic
function initCategories() {
    const chips = document.querySelectorAll('.category-chip');
    chips.forEach(chip => {
        chip.addEventListener('click', () => {
            chips.forEach(c => c.classList.remove('active'));
            chip.classList.add('active');
            
            const category = chip.getAttribute('data-category');
            document.getElementById('category-title').innerText = chip.innerText + ' Videos';
            loadCategoryVideos(category);
        });
    });
}

// Settings logic (BYOK)
function initSettings() {
    const input = document.getElementById('api-key-input');
    const saveBtn = document.getElementById('save-key-btn');
    const clearBtn = document.getElementById('clear-key-btn');

    input.value = currentApiKey;

    saveBtn.addEventListener('click', () => {
        const key = input.value.trim();
        if (key) {
            localStorage.setItem('zotube_api_key', key);
            currentApiKey = key;
            updateApiStatus();
            alert('API Key saved successfully!');
            const activeCategory = document.querySelector('.category-chip.active');
            loadCategoryVideos(activeCategory ? activeCategory.getAttribute('data-category') : 'Trending');
        }
    });

    clearBtn.addEventListener('click', () => {
        localStorage.removeItem('zotube_api_key');
        currentApiKey = '';
        input.value = '';
        updateApiStatus();
        alert('API Key cleared.');
        const activeCategory = document.querySelector('.category-chip.active');
        loadCategoryVideos(activeCategory ? activeCategory.getAttribute('data-category') : 'Trending');
    });
}

function updateApiStatus() {
    const badge = document.getElementById('api-status');
    if (currentApiKey) {
        badge.classList.add('active');
        badge.innerHTML = '<span class="dot"></span> Custom Key Active • 10K Quota';
    } else {
        badge.classList.remove('active');
        badge.innerHTML = '<span class="dot"></span> No Key Active (Limited)';
    }
}

// Search logic
function initSearch() {
    const input = document.getElementById('search-input');
    const btn = document.getElementById('search-button');

    btn.addEventListener('click', () => performSearch(input.value));
    input.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') performSearch(input.value);
    });
}

async function performSearch(query) {
    if (!query.trim()) return;
    
    const resultsGrid = document.getElementById('search-results');
    resultsGrid.innerHTML = '<div class="loading">Searching...</div>';

    try {
        const data = await fetchFromYouTube('search', {
            q: query,
            part: 'snippet',
            type: 'video',
            maxResults: 24
        });
        
        renderVideos(data.items, resultsGrid);
    } catch (error) {
        resultsGrid.innerHTML = `<div class="loading">Error: ${error.message}. Make sure your API key is valid.</div>`;
    }
}

// API Interaction
async function fetchFromYouTube(endpoint, params) {
    // In a real scenario, you might want to proxy this or use a restricted key for trending
    const apiKey = currentApiKey || ''; 
    
    if (!apiKey && endpoint === 'search') {
        throw new Error('API Key is required for searching. Please add it in Settings.');
    }

    const url = new URL(`https://www.googleapis.com/youtube/v3/${endpoint}`);
    
    params.key = apiKey;
    Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));

    const response = await fetch(url);
    if (!response.ok) {
        const err = await response.json();
        throw new Error(err.error.message);
    }
    return response.json();
}

async function loadCategoryVideos(category) {
    const grid = document.getElementById('video-grid');
    grid.innerHTML = '<div class="loading">Loading videos...</div>';

    try {
        let data;
        if (category === 'Trending') {
            data = await fetchFromYouTube('videos', {
                chart: 'mostPopular',
                part: 'snippet,statistics',
                maxResults: 24,
                regionCode: 'IN'
            });
        } else {
            // Map categories to search queries
            const queries = {
                'Mizo': 'Mizo Music Video',
                'Music': 'Popular Music',
                'Gaming': 'Gaming Trending',
                'News': 'World News',
                'Tech': 'Tech Reviews',
                'Movies': 'Official Movie Trailers',
                'Relaxing': 'Lofi Hip Hop Radio'
            };
            
            data = await fetchFromYouTube('search', {
                q: queries[category] || category,
                part: 'snippet',
                type: 'video',
                maxResults: 24
            });
        }
        
        renderVideos(data.items, grid);
    } catch (error) {
        grid.innerHTML = `<div class="loading">Error: ${error.message}. Please check your API Key in Settings.</div>`;
    }
}

function renderVideos(videos, container) {
    container.innerHTML = '';
    
    if (!videos || videos.length === 0) {
        container.innerHTML = '<div class="loading">No videos found.</div>';
        return;
    }

    videos.forEach(item => {
        const videoId = typeof item.id === 'string' ? item.id : item.id.videoId;
        const snippet = item.snippet;
        
        const card = document.createElement('div');
        card.className = 'video-card';
        card.innerHTML = `
            <div class="thumbnail" style="background-image: url('${snippet.thumbnails.medium.url}')"></div>
            <div class="video-info">
                <div class="video-title">${snippet.title}</div>
                <div class="video-meta">${snippet.channelTitle}</div>
            </div>
        `;
        
        card.addEventListener('click', () => openPlayer(videoId));
        container.appendChild(card);
    });
}

// Player logic
function openPlayer(videoId) {
    const overlay = document.getElementById('player-overlay');
    overlay.classList.remove('hidden');

    if (player) {
        player.loadVideoById(videoId);
    } else {
        player = new YT.Player('player-container', {
            height: '100%',
            width: '100%',
            videoId: videoId,
            playerVars: {
                'autoplay': 1,
                'modestbranding': 1,
                'rel': 0
            }
        });
    }

    document.getElementById('close-player').onclick = () => {
        player.stopVideo();
        overlay.classList.add('hidden');
    };
}
