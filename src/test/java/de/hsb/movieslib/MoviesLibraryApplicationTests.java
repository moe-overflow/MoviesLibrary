package de.hsb.movieslib;

import com.jayway.jsonpath.JsonPath;
import de.hsb.movieslib.Model.Movie;

import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MoviesLibraryApplicationTests
{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp()
    {
        // Before each test method, database should be empty
        this.mongoTemplate.dropCollection(Movie.class);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    void testGetAllMovies() throws Exception
    {
        mongoTemplate.save(new Movie("300"));
        mongoTemplate.save(new Movie("Test movie"));
        mongoTemplate.save(new Movie("A1ph4"));

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("300"))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].name").value("Test movie"))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[2].name").value("A1ph4"))
                .andExpect(jsonPath("$[2].id").exists());
    }

    @Test
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
    void testGetMovieById() throws Exception
    {
        Movie movie = new Movie("testMovie");
        String name = movie.getName();

        // Insert a movie
        ResultActions ra = mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + name + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(name));

        String id = JsonPath.parse(ra.andReturn().getResponse().getContentAsString()).read("$.id");

        mockMvc.perform(get("/movies/id/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    void testGetMoviesByName_Success() throws Exception
    {
        Movie movie = new Movie("testMovie");
        String name = movie.getName();

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        //                .content("{\"name\":\"testMovie\"}"))
                        .content("{\"name\":\"" + name + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(name));

        mockMvc.perform(get("/movies/name/testMovie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(name));
    }

    @Test
    void testGetMoviesByName_NotFound() throws Exception
    {
        // Trying to get a movie with a random name from database => Not Found
        mockMvc.perform(get("/movies/name/007"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMovieById_Success() throws Exception
    {
        Movie movie = new Movie("test");
        String name = movie.getName();
        ResultActions ra = mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + name + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(name));

        String id = JsonPath.parse(ra.andReturn().getResponse().getContentAsString()).read("$.id");

        mockMvc.perform(delete("/movies/id/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteMovieById_NotFound() throws Exception
    {
        // Trying to remove a movie with a random id from empty database
        mockMvc.perform(delete("/movies/id/523"))
                .andExpect(status().isNotFound());
    }

    @AfterEach
    void clearDatabase()
    {
        // After each test method, database should be cleared
        this.mongoTemplate.dropCollection(Movie.class);
    }


}
