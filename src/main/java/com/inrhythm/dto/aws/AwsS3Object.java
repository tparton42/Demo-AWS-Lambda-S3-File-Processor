package com.inrhythm.dto.aws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AwsS3Object {
	private String key;
	private Integer size;
	private String eTag;
	private String sequencer;

	public AwsS3Object() {
		// For serialization
	}

	public AwsS3Object(String key, Integer size, String eTag, String sequencer) {
		this.key = key;
		this.size = size;
		this.eTag = eTag;
		this.sequencer = sequencer;
	}


	public String getKey() {
		return key;
	}

	public Integer getSize() {
		return size;
	}

	public String geteTag() {
		return eTag;
	}

	public String getSequencer() {
		return sequencer;
	}
}
