package de.hsb.movieslib.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@lombok.Getter
@lombok.Setter
@Document(collection = "movies")
public class Movie
{

    @Id
    private String id;
    private String name;

    public Movie() {}

    public Movie(String name)
    {
        this.name = name;
    }

}
