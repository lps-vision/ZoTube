package com.example.data.repository

import com.example.data.model.VideoItem

object CuratedCatalog {

    val CATEGORIES = listOf(
        "Trending",
        "Mizo / Hla",
        "Music",
        "Gaming",
        "News",
        "Tech & Science",
        "Movies & Animation",
        "Relaxing / Lofi"
    )

    fun getCategoryVideos(category: String): List<VideoItem> {
        return when (category) {
            "Mizo / Hla" -> MIZO_VIDEOS
            "Music" -> MUSIC_VIDEOS
            "Gaming" -> GAMING_VIDEOS
            "News" -> NEWS_VIDEOS
            "Tech & Science" -> TECH_VIDEOS
            "Movies & Animation" -> ANIMATION_VIDEOS
            "Relaxing / Lofi" -> LOFI_VIDEOS
            else -> TRENDING_VIDEOS
        }
    }

    val TRENDING_VIDEOS = listOf(
        VideoItem(
            id = "Bey4XXJAqS8",
            title = "Costa Rica in 4K 60fps HDR (Ultra HD)",
            channelTitle = "Jacob + Katie Schwarz",
            thumbnailUrl = "https://img.youtube.com/vi/Bey4XXJAqS8/hqdefault.jpg",
            description = "Experience stunning wildlife, lush rainforests, and vibrant colors in native 4K 60fps.",
            publishedAt = "Trending Worldwide",
            duration = "5:14",
            viewCount = "120M views",
            category = "Trending"
        ),
        VideoItem(
            id = "dQw4w9WgXcQ",
            title = "Rick Astley - Never Gonna Give You Up (Official Music Video)",
            channelTitle = "Rick Astley",
            thumbnailUrl = "https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg",
            description = "The official video for Never Gonna Give You Up by Rick Astley. Restored in 4K.",
            publishedAt = "Classic Trending",
            duration = "3:32",
            viewCount = "1.5B views",
            category = "Trending"
        ),
        VideoItem(
            id = "fJ9rUzIMcZQ",
            title = "Queen – Bohemian Rhapsody (Official Video Remastered)",
            channelTitle = "Queen Official",
            thumbnailUrl = "https://img.youtube.com/vi/fJ9rUzIMcZQ/hqdefault.jpg",
            description = "Bohemian Rhapsody taken from A Night At The Opera (1975).",
            publishedAt = "Legendary Hits",
            duration = "6:00",
            viewCount = "1.7B views",
            category = "Trending"
        ),
        VideoItem(
            id = "kJQP7kiw5Fk",
            title = "Luis Fonsi - Despacito ft. Daddy Yankee",
            channelTitle = "Luis Fonsi",
            thumbnailUrl = "https://img.youtube.com/vi/kJQP7kiw5Fk/hqdefault.jpg",
            description = "Despacito official video by Luis Fonsi featuring Daddy Yankee.",
            publishedAt = "Top Music",
            duration = "4:42",
            viewCount = "8.4B views",
            category = "Trending"
        ),
        VideoItem(
            id = "9bZkp7q19f0",
            title = "PSY - GANGNAM STYLE(강남스타일) M/V",
            channelTitle = "officialpsy",
            thumbnailUrl = "https://img.youtube.com/vi/9bZkp7q19f0/hqdefault.jpg",
            description = "PSY - GANGNAM STYLE (강남스타일) official music video.",
            publishedAt = "Global Viral",
            duration = "4:13",
            viewCount = "5.2B views",
            category = "Trending"
        ),
        VideoItem(
            id = "CevxZvSJLk8",
            title = "Katy Perry - Roar (Official)",
            channelTitle = "Katy Perry",
            thumbnailUrl = "https://img.youtube.com/vi/CevxZvSJLk8/hqdefault.jpg",
            description = "Official Music Video for Roar performed by Katy Perry.",
            publishedAt = "Pop Hits",
            duration = "4:30",
            viewCount = "3.9B views",
            category = "Trending"
        )
    )

    val MIZO_VIDEOS = listOf(
        VideoItem(
            id = "XqZsoesa55w",
            title = "Zoramchhani - Ka Lung Di (Mizo Classic Lengzem)",
            channelTitle = "Mizo Music Heritage",
            thumbnailUrl = "https://img.youtube.com/vi/XqZsoesa55w/hqdefault.jpg",
            description = "Mizo hla hlui mawi em em mai, ngaihthlak nuam leh lunglen thlak.",
            publishedAt = "Mizo Traditional",
            duration = "4:15",
            viewCount = "450K views",
            category = "Mizo / Hla"
        ),
        VideoItem(
            id = "L_LUpnjgPso",
            title = "Mizo Gospel Hla Thar - Lalpa Chu Fak Ula",
            channelTitle = "Zo Gospel Melody",
            thumbnailUrl = "https://img.youtube.com/vi/L_LUpnjgPso/hqdefault.jpg",
            description = "Kohhran leh mimal tan Pathian fakna hla mawi tak.",
            publishedAt = "Zo Gospel",
            duration = "5:10",
            viewCount = "280K views",
            category = "Mizo / Hla"
        ),
        VideoItem(
            id = "kXYiU_JCYtU",
            title = "Durtlang & Aizawl City Aerial Tour (Mizoram Beauty)",
            channelTitle = "Explore Mizoram",
            thumbnailUrl = "https://img.youtube.com/vi/kXYiU_JCYtU/hqdefault.jpg",
            description = "Mizoram khawpui Aizawl leh tlâng mawi tak tak thlirna.",
            publishedAt = "Zo Ram",
            duration = "6:45",
            viewCount = "190K views",
            category = "Mizo / Hla"
        ),
        VideoItem(
            id = "2Vv-BfVoq4g",
            title = "Ed Sheeran - Perfect (Mizo Acoustic Rendition)",
            channelTitle = "Zo Acoustic Sessions",
            thumbnailUrl = "https://img.youtube.com/vi/2Vv-BfVoq4g/hqdefault.jpg",
            description = "Guitar rem a hla mawi tak sakna, live acoustic session.",
            publishedAt = "Live Acoustic",
            duration = "4:23",
            viewCount = "320K views",
            category = "Mizo / Hla"
        ),
        VideoItem(
            id = "YQHsXMglC9A",
            title = "Adele - Hello (Zo Music Cover)",
            channelTitle = "Zo Talent Voice",
            thumbnailUrl = "https://img.youtube.com/vi/YQHsXMglC9A/hqdefault.jpg",
            description = "Thiam tak maia Adele hla sak chhuah lehna.",
            publishedAt = "Zo Music",
            duration = "4:55",
            viewCount = "150K views",
            category = "Mizo / Hla"
        )
    )

    val MUSIC_VIDEOS = listOf(
        VideoItem(
            id = "09R8_2nJtjg",
            title = "Maroon 5 - Sugar (Official Music Video)",
            channelTitle = "Maroon 5",
            thumbnailUrl = "https://img.youtube.com/vi/09R8_2nJtjg/hqdefault.jpg",
            description = "Sugar official music video directed by David Dobkin.",
            publishedAt = "Pop Hits",
            duration = "5:01",
            viewCount = "4.0B views",
            category = "Music"
        ),
        VideoItem(
            id = "JGwWNGJdvx8",
            title = "Ed Sheeran - Shape of You (Official Music Video)",
            channelTitle = "Ed Sheeran",
            thumbnailUrl = "https://img.youtube.com/vi/JGwWNGJdvx8/hqdefault.jpg",
            description = "The official music video for Ed Sheeran - Shape of You.",
            publishedAt = "Top Pop",
            duration = "4:24",
            viewCount = "6.2B views",
            category = "Music"
        ),
        VideoItem(
            id = "OPf0YbXqDm0",
            title = "Mark Ronson - Uptown Funk ft. Bruno Mars",
            channelTitle = "Mark Ronson",
            thumbnailUrl = "https://img.youtube.com/vi/OPf0YbXqDm0/hqdefault.jpg",
            description = "Official video for Uptown Funk by Mark Ronson ft. Bruno Mars.",
            publishedAt = "Funk / Pop",
            duration = "4:31",
            viewCount = "5.1B views",
            category = "Music"
        ),
        VideoItem(
            id = "hT_nvWreIhg",
            title = "OneRepublic - Counting Stars",
            channelTitle = "OneRepublic",
            thumbnailUrl = "https://img.youtube.com/vi/hT_nvWreIhg/hqdefault.jpg",
            description = "Official Music Video for Counting Stars performed by OneRepublic.",
            publishedAt = "Alternative Rock",
            duration = "4:43",
            viewCount = "4.0B views",
            category = "Music"
        )
    )

    val LOFI_VIDEOS = listOf(
        VideoItem(
            id = "jfKfPfyJRdk",
            title = "lofi hip hop radio 📚 - beats to relax/study to",
            channelTitle = "Lofi Girl",
            thumbnailUrl = "https://img.youtube.com/vi/jfKfPfyJRdk/hqdefault.jpg",
            description = "The most popular lofi stream in the world. Calming beats and peaceful study vibes.",
            publishedAt = "Live 24/7 Stream",
            duration = "Live",
            viewCount = "Live Broadcast",
            category = "Relaxing / Lofi"
        ),
        VideoItem(
            id = "4xDzrJKXOOY",
            title = "synthwave radio 🌌 - chill synth / retro beats",
            channelTitle = "Lofi Girl",
            thumbnailUrl = "https://img.youtube.com/vi/4xDzrJKXOOY/hqdefault.jpg",
            description = "Synthwave retro vibes to drive, code, or unwind.",
            publishedAt = "Synthwave",
            duration = "Live",
            viewCount = "Continuous Stream",
            category = "Relaxing / Lofi"
        ),
        VideoItem(
            id = "5qap5aO4i9A",
            title = "Lofi Hip Hop Radio 🌿 - Chillhop Beats & Music",
            channelTitle = "Chillhop Music",
            thumbnailUrl = "https://img.youtube.com/vi/5qap5aO4i9A/hqdefault.jpg",
            description = "Peaceful background music for relaxation and creativity.",
            publishedAt = "Chillhop",
            duration = "2:30:00",
            viewCount = "45M views",
            category = "Relaxing / Lofi"
        )
    )

    val GAMING_VIDEOS = listOf(
        VideoItem(
            id = "gB9n2gHsHN4",
            title = "Minecraft 15th Anniversary Official Animation",
            channelTitle = "Minecraft",
            thumbnailUrl = "https://img.youtube.com/vi/gB9n2gHsHN4/hqdefault.jpg",
            description = "Celebrate 15 years of creativity, exploration and survival in Minecraft.",
            publishedAt = "Gaming Showcase",
            duration = "4:12",
            viewCount = "38M views",
            category = "Gaming"
        ),
        VideoItem(
            id = "QdBZY2fkU-0",
            title = "Grand Theft Auto VI Trailer 1",
            channelTitle = "Rockstar Games",
            thumbnailUrl = "https://img.youtube.com/vi/QdBZY2fkU-0/hqdefault.jpg",
            description = "Our trailer for Grand Theft Auto VI, heading to Vice City.",
            publishedAt = "Trending Trailer",
            duration = "1:31",
            viewCount = "230M views",
            category = "Gaming"
        ),
        VideoItem(
            id = "d1ZSXZQ4nls",
            title = "Elden Ring Shadow of the Erdtree - Official Launch Trailer",
            channelTitle = "Bandai Namco Entertainment",
            thumbnailUrl = "https://img.youtube.com/vi/d1ZSXZQ4nls/hqdefault.jpg",
            description = "Guided by Empyrean Miquella, walk in the footsteps of the fallen.",
            publishedAt = "RPG Gameplay",
            duration = "3:10",
            viewCount = "15M views",
            category = "Gaming"
        )
    )

    val TECH_VIDEOS = listOf(
        VideoItem(
            id = "Bey4XXJAqS8",
            title = "The Colors of Nature in 8K HDR 60fps",
            channelTitle = "Nature Relaxation Films",
            thumbnailUrl = "https://img.youtube.com/vi/Bey4XXJAqS8/hqdefault.jpg",
            description = "Ultra high definition footage showcasing OLED television capabilities.",
            publishedAt = "4K / 8K TV Tech",
            duration = "5:14",
            viewCount = "80M views",
            category = "Tech & Science"
        ),
        VideoItem(
            id = "aqz-KE-bpKQ",
            title = "Big Buck Bunny 4K 60fps (Open Source Animation)",
            channelTitle = "Blender Foundation",
            thumbnailUrl = "https://img.youtube.com/vi/aqz-KE-bpKQ/hqdefault.jpg",
            description = "High framerate 60fps benchmark film created with open source Blender 3D.",
            publishedAt = "Tech Animation",
            duration = "9:56",
            viewCount = "42M views",
            category = "Tech & Science"
        ),
        VideoItem(
            id = "tO01J-M3g0U",
            title = "SpaceX Starship Orbital Flight Test Launch",
            channelTitle = "SpaceX",
            thumbnailUrl = "https://img.youtube.com/vi/tO01J-M3g0U/hqdefault.jpg",
            description = "Liftoff of Starship and Super Heavy rocket from Starbase.",
            publishedAt = "Science / Space",
            duration = "4:50",
            viewCount = "22M views",
            category = "Tech & Science"
        )
    )

    val ANIMATION_VIDEOS = listOf(
        VideoItem(
            id = "YE7VzlLtp-4",
            title = "Big Buck Bunny - HD 1080p Official",
            channelTitle = "Blender Studio",
            thumbnailUrl = "https://img.youtube.com/vi/YE7VzlLtp-4/hqdefault.jpg",
            description = "The classic animated movie created entirely using open-source tools.",
            publishedAt = "Animation Studio",
            duration = "9:56",
            viewCount = "19M views",
            category = "Movies & Animation"
        ),
        VideoItem(
            id = "eYq7WapuDLU",
            title = "Sintel - 4K Open Movie by Blender Animation",
            channelTitle = "Blender Studio",
            thumbnailUrl = "https://img.youtube.com/vi/eYq7WapuDLU/hqdefault.jpg",
            description = "An emotional fantasy journey of Sintel looking for her dragon.",
            publishedAt = "Short Film",
            duration = "14:48",
            viewCount = "12M views",
            category = "Movies & Animation"
        )
    )

    val NEWS_VIDEOS = listOf(
        VideoItem(
            id = "gCNeDWCI0vo",
            title = "World News Live Broadcast & Global Headlines",
            channelTitle = "International News",
            thumbnailUrl = "https://img.youtube.com/vi/gCNeDWCI0vo/hqdefault.jpg",
            description = "Round-the-clock news updates, global reports, and live coverage.",
            publishedAt = "Live News",
            duration = "Live",
            viewCount = "1.2M views",
            category = "News"
        ),
        VideoItem(
            id = "21X5lGlDOfg",
            title = "NASA Live: Earth Views from the International Space Station",
            channelTitle = "NASA",
            thumbnailUrl = "https://img.youtube.com/vi/21X5lGlDOfg/hqdefault.jpg",
            description = "Live views of Earth from NASA's camera aboard the ISS.",
            publishedAt = "NASA Live",
            duration = "Live",
            viewCount = "85M views",
            category = "News"
        )
    )
}
