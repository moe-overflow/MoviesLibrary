package com.hsb.movieapi.Model;

@lombok.Getter
@lombok.Setter
public class Movie
{
    private String name;
    private String id;


    public Movie(String name)
    {
        this.name = name;
    }

}
