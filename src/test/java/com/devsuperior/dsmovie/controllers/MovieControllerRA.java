package com.devsuperior.dsmovie.controllers;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MovieControllerRA {

	private String titleMovie;

	@BeforeEach
	public void setUp() throws JSONException {
    baseURI = "http://localhost:8080";

		titleMovie = "vingadores";
  }

	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {

		given()
			.get("/movies")
		.then()
		  .statusCode(200)
			  .body("content.title", hasItems("The Witcher", "Guerra Mundial Z"));
	}
	
	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {

		given()
		  .get("/movies?title={titleMovie}", titleMovie)
			 .then()
			  .statusCode(200)
				.body("content.id[0]", is(13))
				.body("content.title[0]", equalTo("Vingadores: Ultimato"))
				.body("content.score[0]", is(0.0F))
				.body("content.count[0]", is(0))
				.body("content.image[0]", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/7RyHsO4yDXtBv1zUU3mTpHeQ0d5.jpg"));
			}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {	
		
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
		
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {
		
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {

	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
	
	}
}
