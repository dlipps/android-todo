package com.example.model;

import java.io.Serializable;
import java.util.Date;


public class TodoModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1095550755867932688L;

	/**
	 * some static id assignment
	 */
	private static int ID = 0;

	/**
	 * the fields
	 */
	private long id;
	private String name;
	private String description;
	private Date faelligkeit;
	private int erledigt;
	private int favourite;

	public TodoModel(long id, String name, String description,
			Date date, int erledigt, int favourite) {
		this.setId(id == -1 ? ID++ : id);
		this.setName(name);
		this.setDescription(description);
		this.setDate(date);
		this.setErledigt(erledigt);
		this.setFavourite(favourite);
	}
	
	public TodoModel() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}
	
	public Date getDate() {
		return this.faelligkeit;
	}
	
	public int getErledigt() {
		return this.erledigt;
	}
	
	public int getFavourite() {
		return this.favourite;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}	
	
	public void setDate(Date date) {
		this.faelligkeit = date;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setErledigt(int erledigt) {
		this.erledigt = erledigt;
	}
	
	public void setFavourite(int favourite) {
		this.favourite = favourite;
	}
	
	public void updateFrom(TodoModel item) {
		this.setName(item.getName());
		this.setDescription(item.getDescription());
		this.setDate(item.getDate());
		this.setErledigt(item.getErledigt());
		this.setFavourite(item.getFavourite());
	}
	
	public boolean equals(Object other) {

		// we cannot compare getClass() because classes do not coincide in case
		// of delete, where we create an anonymous inner class that extends
		// DataItem
		if (other == null || !(other instanceof TodoModel)) {
			return false;
		} else {
			return ((TodoModel) other).getId() == this.getId();
		}

	}
	
	public String toString() {
		return "{TodoModel " + this.getId() + " " + this.getName() + "}";
	}
}
