# PopularMovies
This is a simple app that presents to user a grid of movies.

#### Built with
- API The Movie DB
- RecyclerView
- java.net library (HttpURLConnection)
- org.json library (JSON object)
- AsyncTaskLoader and LoaderManager
- Room, LiveData and ViewModel
- Android Executors
- Picasso
- Parcelable to pass object via intents

#### Before running
To run this code, it's necessary an api key from The Movie Database API. In this project I used git ignore for don't send a file with my api key. Please, create a file on **/res/values* directory named *TMDB_api_Key.xml* and inside this file add a string named *TheMovieDB_ApiKey* with your own api key.
