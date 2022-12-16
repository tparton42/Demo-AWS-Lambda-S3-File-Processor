package com.inrhythm.dto.aws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/***
 * For translating inbound stream/packet from AWS SNS.
 * Wrapper containing "Records"
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AwsSnsWrapper {
	@JsonProperty("Records")
	private ArrayList<AwsSnsRecord> records = new ArrayList<>();

	public AwsSnsWrapper() {
	}

	public AwsSnsWrapper(ArrayList<AwsSnsRecord> records) {
		this.records = records;
	}

	public ArrayList<AwsSnsRecord> getRecords() {
		return records;
	}

	public void setRecords(ArrayList<AwsSnsRecord> records) {
		this.records = records;
	}
}
