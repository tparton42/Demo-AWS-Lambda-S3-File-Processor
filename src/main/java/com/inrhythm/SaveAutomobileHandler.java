package com.inrhythm;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.inrhythm.dto.AutomobileDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * This handler should write a SINGLE Automobile to a DynamoDb table
 *
 * Inspired by tutorial at Baeldung.com see: https://www.baeldung.com/aws-lambda-dynamodb-java
 */
public class SaveAutomobileHandler implements RequestHandler<AutomobileDTO, Boolean> {
	private AmazonDynamoDB dynamoDB;

	private LambdaLogger logger;
	private static String DYNAMODB_TABLE_NAME = "Automobiles";
	private static Regions REGION = Regions.US_EAST_2;


	@Override
	public Boolean handleRequest(AutomobileDTO automobile, Context context) {
		this.logger = context.getLogger();

		if (automobile == null) {
			// No record found! Abort!
			logger.log("Empty/Null automobile record was passed in.");
			return false;
		}

		this.initDynamoDbClient();
		try {
			logger.log("Attempting to persist an Automobile.");
			persistData(automobile);
		} catch (ConditionalCheckFailedException e) {
			logger.log("Failed to persist an Automobile");
			return false;
		}

		logger.log("Successfully persisted an Automobile.");
		return true;
	}

	private void initDynamoDbClient() {
		this.dynamoDB = AmazonDynamoDBClientBuilder.standard()
				.withRegion(REGION)
				.build();
	}

	private void persistData(AutomobileDTO automobile) throws ConditionalCheckFailedException {
		Map<String, AttributeValue> attributesMap = new HashMap<>();

		attributesMap.put("id", new AttributeValue(String.valueOf(automobile.getId())));
		attributesMap.put("brand", new AttributeValue(automobile.getBrand()));
		attributesMap.put("model", new AttributeValue(automobile.getModel()));
		attributesMap.put("modelYear", new AttributeValue(String.valueOf(automobile.getModelYear())));
		attributesMap.put("color", new AttributeValue(automobile.getColor()));
		attributesMap.put("basePrice", new AttributeValue(automobile.getBasePrice()));

		dynamoDB.putItem(DYNAMODB_TABLE_NAME, attributesMap);
	}
}
