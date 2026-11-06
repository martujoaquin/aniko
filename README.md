# AniKō

AniKō es una aplicación Android desarrollada en Kotlin para explorar, buscar y guardar animes utilizando la API pública Kitsu.

## Funcionalidades

- Mostrar un catálogo de animes.
- Buscar animes por nombre en tiempo real.
- Ver portada, título y puntuación.
- Ver el detalle completo de un anime.
- Mostrar sinopsis, géneros, cantidad de episodios, estado e imagen.
- Agregar y eliminar animes de favoritos.
- Guardar favoritos localmente con SQLite.
- Consultar favoritos y sus datos sin conexión.
- Mostrar mensajes cuando no hay resultados o cuando ocurre un error de red.

## API utilizada

La aplicación consume la API pública:

Kitsu API

Endpoints utilizados:

- `anime` para obtener el listado de animes.
- `anime?filter[text]=...` para realizar búsquedas.
- `anime/{id}` para obtener el detalle completo.
- `include=categories` para obtener los géneros.

## Arquitectura

La aplicación está organizada siguiendo el patrón MVVM:

- `Activity` y `Fragment`: muestran la interfaz y observan los datos.
- `ViewModel`: maneja la lógica de presentación y expone datos con LiveData.
- `Repository`: centraliza el acceso a la API y a SQLite.
- `Retrofit Service`: define las peticiones HTTP a Kitsu.
- `SQLiteOpenHelper`: administra la persistencia local de favoritos.
- `Model`: representa los datos utilizados por la aplicación.

## Pantallas principales

- `SplashActivity`: pantalla inicial con el logo de AniKō.
- `MainActivity`: contenedor principal de fragments.
- `AnimeListFragment`: catálogo y buscador con RecyclerView.
- `FavoritesFragment`: listado de favoritos guardados en SQLite.
- `AnimeDetailActivity`: detalle del anime desarrollado con Jetpack Compose.

## Video demostrativo

Link al video: [https://drive.google.com/file/d/1dv4BMQOyd_5aWDu7dFkY6RiKE4Op2Gy9/view?usp=sharing](https://drive.google.com/file/d/1dv4BMQOyd_5aWDu7dFkY6RiKE4Op2Gy9/view?usp=sharing)
