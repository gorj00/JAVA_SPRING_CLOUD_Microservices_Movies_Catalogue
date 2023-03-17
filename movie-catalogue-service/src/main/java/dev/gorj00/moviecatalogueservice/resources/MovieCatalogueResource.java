package dev.gorj00.moviecatalogueservice.resources;

import dev.gorj00.moviecatalogueservice.models.CatalogueItem;
import dev.gorj00.moviecatalogueservice.models.Movie;
import dev.gorj00.moviecatalogueservice.models.Rating;
import dev.gorj00.moviecatalogueservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalogue")
public class MovieCatalogueResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;
    
    @RequestMapping("{/userId}")
    public List<CatalogueItem> getCatalogue(@PathVariable("userId") String userId) {

        // get all rated movie IDs
        UserRating ratings = restTemplate.getForObject("http://localhost" +
                ":8083/users/" + userId, UserRating.class);
        return ratings.getUserRating().stream().map(rating -> {
                // for each movie ID, call movie info service and get details
                // creates object from json response and model

                // Rest template way (deprecated)
                Movie movie = restTemplate.getForObject
                ("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

                // Webflux (current)
                /*
                Movie movie = webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8082/movies/" + rating.getMovieId())
                        .retrieve()
                        .bodyToMono(Movie.class)
                        .block();
                 */
                // put them all together
                return new CatalogueItem(movie.getName(),
                        "Test", rating.getRating());
            })
            .collect(Collectors.toList());
    }
}
