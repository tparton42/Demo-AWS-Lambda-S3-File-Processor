package com.inrhythm;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inrhythm.dto.aws.*;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Hello world!
 */
public class EventHandler implements RequestHandler<InputStream, Boolean> {
	private ObjectMapper objectMapper = new ObjectMapper();

	private SaveAutomobilesHandler saveAutomobilesHandler = new SaveAutomobilesHandler();

	// Define our AWS S3 Client, use default AWS Credentials
	private static final AmazonS3 s3Client = AmazonS3Client.builder()
         .withCredentials(new DefaultAWSCredentialsProviderChain())
			.build();

	@Override
	public Boolean handleRequest(InputStream input, Context context) {
		final LambdaLogger logger = context.getLogger();

		// Create a empty instance of S3EventNotification before converting the input stream to it.
		AwsSnsWrapper notification = new AwsSnsWrapper();

		// convert the input stream to an object we can work with
		try {
			notification = objectMapper.readValue(input, AwsSnsWrapper.class);
		} catch (Exception ex) {
			logger.log("An exception occurred while attempting to convert input stream to SNS Wrapper.");
			logger.log(ex.getMessage());
			return false;
		}


		// Check to see if we have empty/no records
		if (notification.getRecords().isEmpty()) {
			logger.log("No records found in SNS packet.");
			return false;
		}

		for (AwsSnsRecord notificationRecord : notification.getRecords()) {
			if (notificationRecord == null) {
				logger.log("Encountered a null record in the SNS Packet, or did not de-serialize correctly.");
				return false;
			}

			if (notificationRecord.getS3() == null) {
				logger.log("Inside a SNS Record but unable to retrieve the S3Wrapper");
				return false;
			}

			if (notificationRecord.getS3().getBucket() == null) {
				logger.log("S3 Bucket is NULL for " + notificationRecord.getEventSource());
				return false;
			}

			if (notificationRecord.getS3().getBucket().getName() == null) {
				logger.log("S3 Bucket Name is NULL for " + notificationRecord.getEventSource());
				return false;
			}

			// Get our the bucket name and object key
			String bucketName = notificationRecord.getS3().getBucket().getName();
			String objectKey = notificationRecord.getS3().getObject().getKey();

			logger.log("Processing file, " + bucketName + "/" + objectKey);


			// Get the S3 Object
			S3Object s3Object = s3Client.getObject(bucketName, objectKey);
			S3ObjectInputStream inputStream = s3Object.getObjectContent();

			logger.log("Processing file, " + s3Object.getBucketName() + "/" + s3Object.getKey());

			if (!saveAutomobilesHandler.handleRequest(inputStream, context)) {
				logger.log("Failed to process S3 Automobile File: " + objectKey);
				return false;
			}

		}

		return true;
	}
}
