package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.TvSidebar
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.DirectPlayScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.PlayerScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ZoDarkBackground
import com.example.viewmodel.TvNavDestination
import com.example.viewmodel.ZoTubeViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ZoTubeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ZoDarkBackground),
                    color = ZoDarkBackground
                ) {
                    ZoTubeApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun ZoTubeApp(viewModel: ZoTubeViewModel) {
    val activeVideo by viewModel.activeVideo.collectAsStateWithLifecycle()
    val currentDestination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()
    val watchHistory by viewModel.watchHistory.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val byokState by viewModel.byokState.collectAsStateWithLifecycle()
    val adsBlockedCount by viewModel.adsBlockedCount.collectAsStateWithLifecycle()

    // If a video is playing, present the full TV player
    if (activeVideo != null) {
        val currentPlaying = activeVideo!!
        PlayerScreen(
            video = currentPlaying,
            isBookmarked = bookmarks.any { it.videoId == currentPlaying.id },
            onToggleBookmark = { viewModel.toggleBookmark(currentPlaying) },
            onClose = { viewModel.closePlayer() }
        )
    } else {
        // Main TV Navigation and Browse Interface
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            val isExpandedSidebar = maxWidth >= 700.dp

            Row(modifier = Modifier.fillMaxSize()) {
                // TV Leanback Sidebar
                TvSidebar(
                    selectedDestination = currentDestination,
                    onDestinationSelected = { dest -> viewModel.navigateTo(dest) },
                    isExpanded = isExpandedSidebar
                )

                // Main Content Pane
                when (currentDestination) {
                    TvNavDestination.HOME -> {
                        HomeScreen(
                            feedState = feedState,
                            selectedCategory = selectedCategory,
                            onCategorySelected = { cat -> viewModel.selectCategory(cat) },
                            onVideoClicked = { vid -> viewModel.playVideo(vid) },
                            onBookmarkToggled = { vid -> viewModel.toggleBookmark(vid) },
                            isBookmarked = { id -> viewModel.isBookmarked(id) },
                            onRetry = { viewModel.loadCategoryVideos(selectedCategory) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    TvNavDestination.SEARCH -> {
                        SearchScreen(
                            query = searchQuery,
                            searchState = searchState,
                            recentSearches = recentSearches,
                            onQueryChange = { q -> viewModel.onSearchQueryChanged(q) },
                            onSearch = { q -> viewModel.executeSearch(q) },
                            onClearHistory = { viewModel.clearSearchHistory() },
                            onVideoClicked = { vid -> viewModel.playVideo(vid) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    TvNavDestination.CATEGORIES -> {
                        CategoriesScreen(
                            currentCategory = selectedCategory,
                            onCategorySelected = { cat -> viewModel.selectCategory(cat) },
                            categoryVideos = feedState.videos,
                            onVideoClicked = { vid -> viewModel.playVideo(vid) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    TvNavDestination.DIRECT_PLAY -> {
                        DirectPlayScreen(
                            onPlayDirect = { urlOrId -> viewModel.playDirect(urlOrId) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    TvNavDestination.LIBRARY -> {
                        LibraryScreen(
                            watchHistory = watchHistory,
                            bookmarks = bookmarks,
                            onVideoClicked = { vid -> viewModel.playVideo(vid) },
                            onClearHistory = { viewModel.clearHistory() },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    TvNavDestination.SETTINGS -> {
                        SettingsScreen(
                            currentApiKey = byokState.effectiveApiKey,
                            customApiKey = byokState.customApiKey,
                            effectiveApiKey = byokState.effectiveApiKey,
                            hasCustomKey = byokState.hasCustomKey,
                            adsBlockedCount = adsBlockedCount,
                            onSaveApiKey = { newKey -> viewModel.saveCustomApiKey(newKey) },
                            onClearApiKey = { viewModel.clearCustomApiKey() },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
