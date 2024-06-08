package de.hsb.movieslib.View;

import de.hsb.movieslib.MoviesLibraryApplication;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Logger
{
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MoviesLibraryApplication.class);

    public static org.slf4j.Logger logger()
    {
        return log;
    }


}
