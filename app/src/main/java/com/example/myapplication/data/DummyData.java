package com.example.myapplication.data;

import com.example.myapplication.models.Movie;
import java.util.ArrayList;
import java.util.List;

public class DummyData {
    
    public static List<Movie> getDummyMovies() {
        List<Movie> movies = new ArrayList<>();
        
        // Movie 1: The Shawshank Redemption
        movies.add(new Movie(
                1,
                "The Shawshank Redemption",
                "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates.",
                "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
                "/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg",
                "1994-09-23",
                8.7,
                25847
        ));
        
        // Movie 2: The Godfather
        movies.add(new Movie(
                2,
                "The Godfather",
                "Spanning the years 1945 to 1955, a chronicle of the fictional Italian-American Corleone crime family. When organized crime family patriarch, Vito Corleone barely survives an attempt on his life, his youngest son, Michael steps in to take care of the would-be killers, launching a campaign of bloody revenge.",
                "/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
                "/tmU7GeKVybMWFButWEGl2M4GeiP.jpg",
                "1972-03-14",
                8.7,
                19845
        ));
        
        // Movie 3: The Dark Knight
        movies.add(new Movie(
                3,
                "The Dark Knight",
                "Batman raises the stakes in his war on crime. With the help of Lt. Jim Gordon and District Attorney Harvey Dent, Batman sets out to dismantle the remaining criminal organizations that plague the streets. The partnership proves to be effective, but they soon find themselves prey to a reign of chaos unleashed by a rising criminal mastermind known to the terrified citizens of Gotham as the Joker.",
                "/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
                "/hkBaDkMWbLaf8B1lsWsKX7Ew3Xq.jpg",
                "2008-07-16",
                8.5,
                32324
        ));
        
        // Movie 4: Pulp Fiction
        movies.add(new Movie(
                4,
                "Pulp Fiction",
                "A burger-loving hit man, his philosophical partner, a drug-addled gangster's moll and a washed-up boxer converge in this sprawling, comedic crime caper. Their adventures unfurl in three stories that ingeniously trip back and forth in time.",
                "/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg",
                "/suaEOtk1N1sgg2MTM7oZd2cfVp3.jpg",
                "1994-09-10",
                8.5,
                27547
        ));
        
        // Movie 5: Forrest Gump
        movies.add(new Movie(
                5,
                "Forrest Gump",
                "A man with a low IQ has accomplished great things in his life and been present during significant historic events—in each case, far exceeding what anyone imagined he could do. But despite all he has achieved, his one true love eludes him.",
                "/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg",
                "/7c9UVPPiTPltouxRVY6N9uEGAIB.jpg",
                "1994-06-23",
                8.5,
                26874
        ));
        
        // Movie 6: Inception
        movies.add(new Movie(
                6,
                "Inception",
                "Cobb, a skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible: inception, the implantation of another person's idea into a target's subconscious.",
                "/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg",
                "/s3TBrRGB1iav7gFOCNx3H31MoES.jpg",
                "2010-07-15",
                8.4,
                35147
        ));
        
        // Movie 7: The Matrix
        movies.add(new Movie(
                7,
                "The Matrix",
                "Set in the 22nd century, The Matrix tells the story of a computer hacker who joins a group of underground insurgents fighting the vast and powerful computers who now rule the earth.",
                "/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg",
                "/fNG7i7RqMErkcqhohV2a6cV1Ehy.jpg",
                "1999-03-30",
                8.2,
                24698
        ));
        
        // Movie 8: Interstellar
        movies.add(new Movie(
                8,
                "Interstellar",
                "The adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.",
                "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
                "/xJHokMbljvjADYdit5fK5VQsXEG.jpg",
                "2014-11-05",
                8.4,
                34562
        ));
        
        // Movie 9: Avengers: Endgame
        movies.add(new Movie(
                9,
                "Avengers: Endgame",
                "After the devastating events of Avengers: Infinity War, the universe is in ruins due to the efforts of the Mad Titan, Thanos. With the help of remaining allies, the Avengers must assemble once more in order to undo Thanos' actions and restore order to the universe once and for all.",
                "/or06FN3Dka5tukK1e9sl16pB3iy.jpg",
                "/7RyHsO4yDXtBv1zUU3mTpHeQ0d5.jpg",
                "2019-04-24",
                8.3,
                24258
        ));
        
        // Movie 10: Spider-Man: No Way Home
        movies.add(new Movie(
                10,
                "Spider-Man: No Way Home",
                "Peter Parker is unmasked and no longer able to separate his normal life from the high-stakes of being a super-hero. When he asks for help from Doctor Strange the stakes become even more dangerous, forcing him to discover what it truly means to be Spider-Man.",
                "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
                "/iQFcwSGbZXMkeyKrxbPnwnRo5fl.jpg",
                "2021-12-15",
                8.1,
                20847
        ));
        
        return movies;
    }
    
    public static List<Movie> searchMovies(String query) {
        List<Movie> allMovies = getDummyMovies();
        List<Movie> results = new ArrayList<>();
        
        String searchQuery = query.toLowerCase();
        
        for (Movie movie : allMovies) {
            if (movie.getTitle().toLowerCase().contains(searchQuery)) {
                results.add(movie);
            }
        }
        
        return results;
    }
}
