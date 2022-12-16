package com.inrhythm.dto;

import java.io.Serializable;

/***
 *
 *
 */
public class VehicleDetails implements Serializable {

	private int id;
	private String model;
	private Integer modelYear;

	private String color;
	private String basePrice;

	private VehicleDetails() {
		// Default constructor for serialization
	}

	public VehicleDetails(int id, String model, Integer modelYear, String color, String basePrice) {
		this.id = id;
		this.model = model;
		this.modelYear = modelYear;
		this.color = color;
		this.basePrice = basePrice;
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

	/** Setters **/
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
