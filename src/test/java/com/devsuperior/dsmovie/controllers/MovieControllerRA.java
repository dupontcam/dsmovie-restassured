package com.devsuperior.dsmovie.controllers;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsuperior.dsmovie.tests.TokenUtil;

public class MovieControllerRA {

	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private String clientToken, adminToken, invalidToken;
	private Long existingMovieId, nonExistingMovieId;
	private String titleMovie;

	private Map<String, Object> postMovieInstance;

	@BeforeEach
	public void setUp() throws JSONException {
    baseURI = "http://localhost:8080";

		clientUsername = "joaquim@gmail.com";
		clientPassword = "123456";
		adminUsername = "maria@gmail.com";
		adminPassword = "123456";

		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		invalidToken = adminToken + "xpto";

		titleMovie = "vingadores";

		postMovieInstance = new HashMap<>();
    postMovieInstance.put("title", "Filme Novo");
    postMovieInstance.put("score", 0.0F);
		postMovieInstance.put("count", 0);
    postMovieInstance.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");

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
		
		existingMovieId = 1L;
		
    given()
		.get("/movies/{movieId}", existingMovieId)
		.then()
		.statusCode(200)
		.body("id", is(1))
		.body("title", equalTo("The Witcher"))
		.body("score", is(4.5F))
		.body("count", is(2))
		.body("image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
		
		nonExistingMovieId = 100L;
    
    given()
    .get("/movies/{movieId}", nonExistingMovieId)
    .then()
    .statusCode(404)
		.body("error", is("Recurso não encontrado"));
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
