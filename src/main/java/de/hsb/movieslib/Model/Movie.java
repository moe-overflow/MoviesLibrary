package de.hsb.movieslib.Model;

@lombok.Getter
@lombok.Setter
public class Movie
{
    private String name;
    private int id;

    public Movie() {}

    public Movie(String name)
    {
        this.name = name;
    }

}
