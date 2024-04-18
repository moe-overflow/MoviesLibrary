package com.hsb.movieapi.Controller;

import com.hsb.movieapi.Model.Movie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController
{
    @GetMapping
    public List<Movie> getMovies()
    {
        List<Movie> list = new ArrayList<>();
        list.add(new Movie("300"));
        list.add(new Movie("prey"));
        return list;
    }

    @PostMapping
    public void insertMovie(Movie movie)
    {

    }


}
