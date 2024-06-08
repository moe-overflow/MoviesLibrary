package de.hsb.movieslib;

import de.hsb.movieslib.Controller.MovieController;
import de.hsb.movieslib.Model.Movie;

import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.Collection;

//@SpringBootTest
@WebMvcTest(MovieController.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MoviesLibraryApplicationTests
{

    @Autowired
    private MockMvc mockMvc;

    private Collection<Movie> movieList;

    @Test
    @Order(1)
    void testGetAllMovies() throws Exception
    {
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("300"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Prey"))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].name").value("Prey"));
    }


    @Test
    @Order(2)
    void testGetMovieById_Success() throws Exception
    {
        mockMvc.perform(get("/movies/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("300"));
    }

    @Test
    @Order(3)
    void testGetMovieById_NotFound() throws Exception
    {
        mockMvc.perform(get("/movies/id/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    void testGetMoviesByName_Success() throws Exception
    {
        mockMvc.perform(get("/movies/name/Prey"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Prey"))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].name").value("Prey"));
    }

    @Test
    @Order(4)
    void testGetMoviesByName_NotFound() throws Exception
    {
        mockMvc.perform(get("/movies/name/007"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    void testAddMovie() throws Exception
    {
        String newMovieJson = "{\"name\":\"New Movie\"}";

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMovieJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("New Movie"));
    }



    @Test
    @Order(6)
    void testDeleteMovieById_Success() throws Exception
    {
        mockMvc.perform(delete("/movies/id/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(7)
    void testDeleteMovieById_NotFound() throws Exception
    {
        mockMvc.perform(delete("/movies/id/523"))
                .andExpect(status().isNotFound());
    }



}
