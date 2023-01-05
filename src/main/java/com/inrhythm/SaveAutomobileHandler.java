package com.inrhythm;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inrhythm.dto.AutomobileDTO;
import com.inrhythm.lambda.layers.AbstractLambdaDTOWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This handler should write a SINGLE Automobile to a DynamoDb table
 *
 * Inspired by tutorial at Baeldung.com see: https://www.baeldung.com/aws-lambda-dynamodb-java
 */
public class SaveAutomobileHandler extends AbstractLambdaDTOWrapper<AutomobileDTO, Boolean> {
	private AmazonDynamoDB dynamoDB;

	private LambdaLogger logger;
	private String dynamodbTableName = "Automobiles";

	private Properties appProperties;


	@Override
	public Boolean handleRequest(Object objIn, Context context) {
		this.logger = context.getLogger();
		init();

		// Convert object in to AutomobileDTO
		ObjectMapper objectMapper = new ObjectMapper();
		AutomobileDTO automobile = objectMapper.convertValue(objIn, AutomobileDTO.class);


		if (automobile == null) {
			// No record found! Abort!
			logger.log("Empty/Null automobile record was passed in.");
			return false;
		}

		try {
			logger.log("Attempting to persist an Automobile.");
			persistAutomobile(automobile);
		} catch (ConditionalCheckFailedException e) {
			logger.log("Failed to persist an Automobile");
			return false;
		}

		logger.log("Successfully persisted an Automobile.");
		return true;
	}

	private void init() {
		// Load Application Properties
		this.appProperties = new PropertyLoader(logger).getAppProperties();
		logger.log("AppName: " + appProperties.getProperty("app.name"));

		String regionName = appProperties.getProperty("app.region", "us-east-2");
		logger.log("regionName: " + regionName);

		this.dynamodbTableName = appProperties.getProperty("db.tbl.automobiles", "Automobiles");

		initDynamoDbClient(regionName);
	}

	private void initDynamoDbClient(String regionName) {
		Regions targetRegion = Regions.fromName(regionName);
		this.dynamoDB = AmazonDynamoDBClientBuilder.standard()
				.withRegion(targetRegion)
				.build();
	}

	private void persistAutomobile(AutomobileDTO automobile) throws ConditionalCheckFailedException {
		Map<String, AttributeValue> attributesMap = new HashMap<>();

		attributesMap.put("id", new AttributeValue(String.valueOf(automobile.getId())));
		attributesMap.put("brand", new AttributeValue(automobile.getBrand()));
		attributesMap.put("model", new AttributeValue(automobile.getModel()));
		attributesMap.put("modelYear", new AttributeValue(String.valueOf(automobile.getModelYear())));
		attributesMap.put("color", new AttributeValue(automobile.getColor()));
		attributesMap.put("basePrice", new AttributeValue(automobile.getBasePrice()));

		dynamoDB.putItem(dynamodbTableName, attributesMap);
	}
}
