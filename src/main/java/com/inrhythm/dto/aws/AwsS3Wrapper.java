package com.inrhythm.dto.aws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AwsS3Wrapper {
	private String s3SchemaVersion;
	private String configurationId;

	private AwsS3Bucket bucket;

	private AwsS3Object object;

	public AwsS3Wrapper() {
		// For Jackson Serialization
	}

	public AwsS3Wrapper(String s3SchemaVersion, String configurationId, AwsS3Bucket bucket, AwsS3Object object) {
		this.s3SchemaVersion = s3SchemaVersion;
		this.configurationId = configurationId;
		this.bucket = bucket;
		this.object = object;
	}

	public String getS3SchemaVersion() {
		return s3SchemaVersion;
	}

	public String getConfigurationId() {
		return configurationId;
	}

	public AwsS3Bucket getBucket() {
		return bucket;
	}

	public AwsS3Object getObject() {
		return object;
	}
}