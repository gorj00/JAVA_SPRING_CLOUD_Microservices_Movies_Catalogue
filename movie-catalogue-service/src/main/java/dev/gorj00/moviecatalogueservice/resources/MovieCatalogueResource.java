package dev.gorj00.moviecatalogueservice.resources;

import dev.gorj00.moviecatalogueservice.models.CatalogueItem;
import dev.gorj00.moviecatalogueservice.models.Movie;
import dev.gorj00.moviecatalogueservice.models.Rating;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalogue")
public class MovieCatalogueResource {

    @RequestMapping("{/userId}")
    public List<CatalogueItem> getCatalogue(@PathVariable("userId") String userId) {

        RestTemplate restTemplate = new RestTemplate();

        // get all rated movie IDs
        List<Rating> ratings = Arrays.asList(
                new Rating("1234", 4),
                new Rating("5678", 5)
                );
        return ratings.stream().map(rating -> {
                    // for each movie ID, call movie info service and get details
                    // creates object from json response and model
                    Movie movie = restTemplate.getForObject("http://localhost" +
                                    ":8082/movies/" + rating.getMovieId(),
                            Movie.class);
                    // put them all together
                    return new CatalogueItem(movie.getName(),
                            "Test", rating.getRating());
                })
                .collect(Collectors.toList());
    }
}
