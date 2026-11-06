# AniKō

<p align="center">
  <img src="app/src/main/res/drawable/aniko_logo.png" alt="AniKō logo" width="320"/>
</p>

AniKō is an Android anime catalog application developed in Kotlin. It allows users to explore anime, search by title, view detailed information, save favorites locally, and consult saved anime offline.

## Demo Video

[Watch the AniKō demo video](https://drive.google.com/file/d/1dv4BMQOyd_5aWDu7dFkY6RiKE4Op2Gy9/view?usp=sharing)

## Features

- Anime catalog loaded from the Kitsu API
- Real-time search by anime title
- Anime cards with cover image, title and score
- Detailed anime screen built with Jetpack Compose
- Full synopsis, genres, episode count, status and large image
- Add and remove favorites
- Local persistence with SQLite
- Favorites available offline
- Offline access to saved anime details
- Network error handling
- Splash screen with AniKō branding

## Architecture

The project follows the MVVM architecture used throughout the course:

```
View (Activity / Fragment / Compose)
              ↓
          ViewModel
              ↓
          Repository
           ↙      ↘
      Retrofit   SQLite
        API       Local DB
```

The UI observes data exposed by the ViewModel, while the Repository centralizes access to both remote and local data sources.

## Technologies

- Kotlin
- MVVM
- Repository pattern
- Android Views + XML
- Fragments
- RecyclerView
- ListAdapter + DiffUtil
- ViewBinding
- LiveData
- Coroutines
- Retrofit
- Gson
- Kitsu API
- Picasso
- SQLite
- Jetpack Compose
- Material Components

## API

The application consumes the public Kitsu API:

```
https://kitsu.io/api/edge/
```

Main operations used by the app include:

- Anime listing
- Search by text
- Anime detail
- Categories/genres included in detail requests

## Local Persistence

Favorite anime are stored in a local SQLite database.

Saved information includes:

- ID
- Title
- Score
- Synopsis
- Episodes
- Status
- Genres
- Image URLs

This allows the Favorites section and saved anime information to remain available without an Internet connection.

## Main Screens

### Explore Anime
Displays anime obtained from the API in an XML RecyclerView and provides real-time search.

### Favorites
Displays anime persisted locally with SQLite.

### Anime Detail
Built with Jetpack Compose and displays the selected anime's complete information.

## Project Structure

```
com.example.animecatalog
├── data
│   ├── local
│   ├── model
│   ├── network
│   └── repository
├── ui
│   ├── anime
│   ├── favorites
│   └── detail
├── viewmodel
├── MainActivity.kt
└── SplashActivity.kt
```

## Author

Martina Joaquin  
Desarrollo de Aplicaciones Móviles II — 2026
