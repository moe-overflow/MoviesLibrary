package de.hsb.movieslib.Controller;

import de.hsb.movieslib.Model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/movies")
public class MovieController
{
    private final MongoTemplate mongoTemplate;

    @Autowired
    public MovieController(MongoTemplate mongoTemplate)
    {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping
    public ResponseEntity<Collection<Movie>> getMovies()
    {
        Collection<Movie> currentMovies = mongoTemplate.findAll(Movie.class);
        return new ResponseEntity<>(currentMovies, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Movie> insertMovie(@RequestBody Movie movie)
    {
        Movie movieToSave = mongoTemplate.save(movie);
        return new ResponseEntity<>(movieToSave, HttpStatus.CREATED);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Movie> findById(@PathVariable String id)
    {
        Collection<Movie> currentMovies = mongoTemplate.findAll(Movie.class);
        for ( var movie : currentMovies)
            if(movie.getId().equals(id))
                return new ResponseEntity<>(movie, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    // Note: in case multiple movies with the same name exist, all of them must be returned
    @GetMapping("/name/{name}")
    public ResponseEntity<Collection<Movie>> findByName(@PathVariable String name)
    {
        // Query for finding movie by name (case-sensitive)
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex("^" + name + "$", "i"));
        Collection<Movie> foundMovies = mongoTemplate.find(query, Movie.class);
        return new ResponseEntity<>(foundMovies, HttpStatus.OK);
    }


    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Movie movieToDelete = mongoTemplate.findAndRemove(query, Movie.class);
        return movieToDelete != null ?
        new ResponseEntity<>(HttpStatus.NO_CONTENT) :
        new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearEntireDatabase()
    {
        mongoTemplate.getDb().drop();
        return ResponseEntity.ok("Database cleared!");
    }

}
