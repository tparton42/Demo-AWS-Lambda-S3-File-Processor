package com.inrhythm;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inrhythm.dto.AutomobileDTO;
import com.inrhythm.dto.AutomobilesDTO;
import com.inrhythm.lambda.layers.AbstractLambdaInputStreamWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This handler should write MULTIPLE Automobiles to a DynamoDb table
 *
 * Inspired by tutorial at Baeldung.com see: https://www.baeldung.com/aws-lambda-dynamodb-java
 */
public class SaveAutomobilesHandler extends AbstractLambdaInputStreamWrapper<InputStream, Boolean> {
	private LambdaLogger logger;
	private AmazonDynamoDB dynamoDB;
	private static ObjectMapper objectMapper = new ObjectMapper();
	private Properties appProperties;


	private String dynamodbTableName = "Automobiles";


	@Override
	public Boolean handleRequest(InputStream inputStream, Context context) {
		this.logger = context.getLogger();
		init();

		objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		// Use Jackson to convert the input stream to AutomobilesDTO
		AutomobilesDTO automobilesDTO = new AutomobilesDTO();

		try {
			automobilesDTO = objectMapper.readValue(inputStream, AutomobilesDTO.class);
		} catch (IOException e) {
			logger.log("Exception: " + e);
			logger.log("Failed to convert input stream to Automobiles DTO");
			return false;
		}

		if (automobilesDTO == null || automobilesDTO.getAutomobiles() == null) {
			// Do not pass go! Do not collect $200. Game over man!
			logger.log("No records present.");
			return false;
		}

		try {
			logger.log("Attempting to persist an Automobile.");

			// Check to see if we have records
			if (automobilesDTO.getAutomobiles().isEmpty()) {
				logger.log("No automobile records found in JSON dataset.");
				return false;
			}

			persistAutomobiles(automobilesDTO);

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

	private void persistAutomobiles(AutomobilesDTO automobilesDTO) throws ConditionalCheckFailedException {

		for (AutomobileDTO vehicle : automobilesDTO.getAutomobiles()) {
			Map<String, AttributeValue> attributesMap = new HashMap<>();

			attributesMap.put("id", new AttributeValue(String.valueOf(vehicle.getId())));
			attributesMap.put("brand", new AttributeValue(vehicle.getBrand()));
			attributesMap.put("model", new AttributeValue(vehicle.getModel()));
			attributesMap.put("modelYear", new AttributeValue(String.valueOf(vehicle.getModelYear())));
			attributesMap.put("color", new AttributeValue(vehicle.getColor()));
			attributesMap.put("basePrice", new AttributeValue(vehicle.getBasePrice()));

			dynamoDB.putItem(this.dynamodbTableName, attributesMap);
		}
	}
}
