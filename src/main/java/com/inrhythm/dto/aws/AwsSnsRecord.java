package com.inrhythm.dto.aws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

/***
 * Represents a SNS Record in the AWS SNS Wrapper
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AwsSnsRecord {

	private String eventVersion;
	private String eventSource;
	private String awsRegion;

	// TODO: Write a converter for java.time.LocalDatetTime
	private String eventTime;

	private AwsS3Wrapper s3;

	public AwsSnsRecord() {
		// For Serialization
	}

	public AwsSnsRecord(String eventVersion, String eventSource, String awsRegion, String eventTime, AwsS3Wrapper s3) {
		this.eventVersion = eventVersion;
		this.eventSource = eventSource;
		this.awsRegion = awsRegion;
		this.eventTime = eventTime;
		this.s3 = s3;
	}

	public String getEventVersion() {
		return eventVersion;
	}

	public String getEventSource() {
		return eventSource;
	}

	public String getAwsRegion() {
		return awsRegion;
	}

	public String getEventTime() {
		return eventTime;
	}

	public AwsS3Wrapper getS3() {
		return s3;
	}
}
