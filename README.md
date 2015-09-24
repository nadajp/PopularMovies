# PopularMovies
Udacity Android Nanodegree Project 1 & 2

This is Project 1 & 2 of Udacity's Android Nanodegree. 

Features:
 - pulls popular movies from movie database and displays movie posters as a grid
 - ability to select movie to view details
 - detail view also shows trailers and allows user to click on a trailer and view in youtube/browser
 - ability to select movie as favourite, which saves it to the local database to allow for offline viewing
 - ability to sort movies by highest rated, most popular, or favourite
 - tablet support

Movie database API key has been removed for privacy. 

To use the app, go to https://www.themoviedb.org and obtain your API key, then insert it in MovieGridFragment.java inside DownloadMoviesTask:

public static final String API_KEY = ""; // Insert your movie db api key here
