package com.inrhythm;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Hello world!
 */
public class EventHandler implements RequestHandler<S3Event, Boolean> {
	// Define our AWS S3 Client, use default AWS Credentials
	private static final AmazonS3 s3Client = AmazonS3Client.builder()
         .withCredentials(new DefaultAWSCredentialsProviderChain())
			.build();

	@Override
	public Boolean handleRequest(S3Event input, Context context) {
		final LambdaLogger logger = context.getLogger();

		// Check to see if we have empty/no records
		if (input.getRecords().isEmpty()) {
			logger.log("No records found.");
			return false;
		}

		for (S3EventNotification.S3EventNotificationRecord record : input.getRecords()) {
			// Get our the bucket name and object key
			String bucketName = record.getS3().getBucket().getName();
			String objectKey = record.getS3().getObject().getKey();

			// Get the S3 Object
			S3Object s3Object = s3Client.getObject(bucketName, objectKey);
			S3ObjectInputStream inputStream = s3Object.getObjectContent();

			logger.log("Processing file, " + s3Object.getBucketName() + "/" + s3Object.getKey());

			try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
				reader.lines().forEach(line -> logger.log(line + "\n"));
			} catch (IOException e) {
				logger.log("An error occurred in Lambda: " + e.getMessage());
				return false;
			}
		}

		return true;
	}
}
