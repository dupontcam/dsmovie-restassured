package com.devsuperior.dsmovie.controllers;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsuperior.dsmovie.tests.TokenUtil;

import net.minidev.json.JSONObject;

public class ScoreControllerRA {

	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private String clientToken, adminToken;
	
	private Map<String, Object> postScoreInstance;

	@BeforeEach
  public void setUp() throws Exception {
		baseURI = "http://localhost:8080";

		clientUsername = "alex@gmail.com";
    clientPassword = "123456";
    adminUsername = "maria@gmail.com";
    adminPassword = "123456";

		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);

	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {

		postScoreInstance = new HashMap<>();
		postScoreInstance.put("movieId", 50);
		postScoreInstance.put("score", 2);
		JSONObject newScore = new JSONObject(postScoreInstance);

		given()
		.header("Content-type", "application/json")
    	.header("Authorization", "Bearer " + adminToken)
      	.contentType("application/json")
      		.body(newScore)
    .when()
      .put("/scores")
    .then()
      .statusCode(404)
				.body("error", equalTo("Recurso não encontrado"));	
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {

		postScoreInstance = new HashMap<>();
		postScoreInstance.put("score", 2);
		JSONObject newScore = new JSONObject(postScoreInstance);

		given()
		.header("Content-type", "application/json")
    	.header("Authorization", "Bearer " + clientToken)
      	.contentType("application/json")
      		.body(newScore)
    .when()
      .put("/scores")
    .then()
      .statusCode(422)
				.body("error", equalTo("Dados inválidos"))
				.body("errors.fieldName[0]", equalTo("movieId"))
				.body("errors.message[0]", equalTo("Campo requerido"));
		}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {	
		
		postScoreInstance = new HashMap<>();
		postScoreInstance.put("movieId", 25);
		postScoreInstance.put("score", -10);
		JSONObject newScore = new JSONObject(postScoreInstance);

		given()
		.header("Content-type", "application/json")
    	.header("Authorization", "Bearer " + clientToken)
      	.contentType("application/json")
      		.body(newScore)
    .when()
      .put("/scores")
    .then()
      .statusCode(422)
				.body("error", equalTo("Dados inválidos"))
				.body("errors.fieldName[0]", equalTo("score"))
				.body("errors.message[0]", equalTo("Valor mínimo 0"));
	}
}
