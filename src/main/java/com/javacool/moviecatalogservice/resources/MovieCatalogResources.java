package com.javacool.moviecatalogservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.javacool.moviecatalogservice.model.CatalogItem;
import com.javacool.moviecatalogservice.model.Movie;
import com.javacool.moviecatalogservice.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResources {
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	//private WebClient.Builder webClientBuilder;
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId")String userId){
		
		
		/*
		 * List<Rating> ratingList=Arrays.asList(new Rating("Test1", 3), new
		 * Rating("Test2", 4), new Rating("Test3", 2));
		 */
		UserRating userRating=restTemplate.getForObject("http://rating-movie-service/ratingsdata/users/"+userId,UserRating.class);
		
		return userRating.getUserRating().stream().map(rating->{
			 Movie movie=restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
			/*
			 * Movie movie=webClientBuilder.build() .get()
			 * .uri("http://localhost:8082/movies/"+rating.getMovieId()) .retrieve()
			 * .bodyToMono(Movie.class) .block();
			 */
			
			
		 return (new CatalogItem(movie.getName(),"Test",rating.getRating()));})
				.collect(Collectors.toList());
		
		
		//return Collections.singletonList(new CatalogItem("Transformer", "Test", 4));
	}
}
