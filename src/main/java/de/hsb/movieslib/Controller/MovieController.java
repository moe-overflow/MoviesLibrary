package de.hsb.movieslib.Controller;

import de.hsb.movieslib.Model.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/movies")
public class MovieController
{
    private final Collection<Movie> list = new ArrayList<>();
    private static int counter = 0;

    public MovieController()
    {

        Movie movie1 = new Movie("300");
        movie1.setId(++counter);
        list.add(movie1);

        Movie movie2 = new Movie("Prey");
        movie2.setId(++counter);
        list.add(movie2);

        Movie movie3 = new Movie("Prey");
        movie3.setId(++counter);
        list.add(movie3);

    }

    @GetMapping
    public ResponseEntity<Collection<Movie>> getMovies()
    {
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Movie> insertMovie(@RequestBody Movie movie)
    {
        movie.setId(++counter);
        list.add(movie);
        return new ResponseEntity<>(movie, HttpStatus.CREATED);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Movie> findById(@PathVariable int id)
    {
        for ( var movie : list)
            if(movie.getId() == id)
                return new ResponseEntity<>(movie, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    // Note: in case multiple movies with the same name exist, all of them must be returned
    @GetMapping("/name/{name}")
    public ResponseEntity<Collection<Movie>> findByName(@PathVariable String name)
    {
        Collection<Movie> foundMovies = new ArrayList<>();
        for ( var movie : list )
            if(movie.getName().equals(name))
                foundMovies.add(movie);

        if (foundMovies.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(foundMovies, HttpStatus.OK);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id)
    {
        for (var movie : list)
        {
            if(movie.getId() == id)
            {
                list.remove(movie);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
