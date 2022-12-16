package com.inrhythm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/***
 * Store a single automobile by brand/mfg
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutomobileDTO implements Serializable {

	private String brand;
	private int id;
	private String model;
	private Integer modelYear;

	private String color;
	private String basePrice;

	public AutomobileDTO() {
		// Intentionally empty for serialization
	}

	public AutomobileDTO(String brand, int id, String model, Integer modelYear, String color, String basePrice) {
		this.brand = brand;
		this.id = id;
		this.model = model;
		this.modelYear = modelYear;
		this.color = color;
		this.basePrice = basePrice;
	}

	/*** Getters ***/
	public String getBrand() {
		return brand;
	}

	public int getId() {
		return id;
	}

	public String getModel() {
		return model;
	}

	public Integer getModelYear() {
		return modelYear;
	}

	public String getColor() {
		return color;
	}

	public String getBasePrice() {
		return basePrice;
	}

	/*** Setters ***/
	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setModelYear(Integer modelYear) {
		this.modelYear = modelYear;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
	}

}
