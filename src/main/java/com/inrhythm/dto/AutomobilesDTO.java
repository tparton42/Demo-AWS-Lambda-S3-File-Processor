package com.inrhythm.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

/***
 * Array of Automobiles
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutomobilesDTO implements Serializable {

	@JsonProperty("automobiles")
	@JsonAlias("autos")
	private ArrayList<AutomobileDTO> automobiles;

	public AutomobilesDTO() {
		// Default constructor for serialization
	}

	public AutomobilesDTO(ArrayList<AutomobileDTO> automobiles) {
		this.automobiles = automobiles;
	}

	public ArrayList<AutomobileDTO> getAutomobiles() {
		return automobiles;
	}

	public void setAutomobiles(ArrayList<AutomobileDTO> automobiles) {
		this.automobiles = automobiles;
	}
}
