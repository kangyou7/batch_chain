package com.glyde.mall.batch.job.sample.entity;

import java.io.Serializable;

public class PeopleDto implements Serializable {

	private static final long serialVersionUID = -7685905244214117531L;

	private int id;
	private String firstName;
	private String lastName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
