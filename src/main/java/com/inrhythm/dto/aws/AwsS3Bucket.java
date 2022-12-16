package com.inrhythm.dto.aws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AwsS3Bucket {

	private String name;
	private String arn;

	private OwnerIdentity ownerIdentity;

	public AwsS3Bucket(String name, String arn, OwnerIdentity ownerIdentity) {
		this.name = name;
		this.arn = arn;
		this.ownerIdentity = ownerIdentity;
	}

	public AwsS3Bucket() {
		// For Serialization
	}

	public String getName() {
		return name;
	}

	public String getArn() {
		return arn;
	}

	public OwnerIdentity getOwnerIdentity() {
		return ownerIdentity;
	}



	// Sub-class representing S3 Owner
	public class OwnerIdentity {
		private String principalId;

		public OwnerIdentity() {
			// For Jackson Serialization
		}

		public OwnerIdentity(String principalId) {
			this.principalId = principalId;
		}

		public String getPrincipalId() {
			return principalId;
		}
	}

}
