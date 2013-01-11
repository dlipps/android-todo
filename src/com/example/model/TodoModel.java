package com.example.model;

import java.io.Serializable;
import java.util.Date;


public class TodoModel implements Serializable{
	/**
	 * some static id assignment
	 */
	private static int ID = 0;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7481912314472891511L;

	/**
	 * the fields
	 */
	private long id;
	private String name;
	private String description;
	private Date faelligkeit;
	private boolean erledigt;
	private boolean favourite;

	public TodoModel(long id, String name, String description,
			Date date, boolean erledigt, boolean favourite) {
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
	
	public boolean getErledigt() {
		return this.erledigt;
	}
	
	public boolean getFavourite() {
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
	
	public void setErledigt(boolean erledigt) {
		this.erledigt = erledigt;
	}
	
	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}
	
	public void updateFrom(TodoModel item) {
		this.setName(item.getName());
		this.setDescription(item.getDescription());
		this.setDate(item.getDate());
		this.setErledigt(item.getErledigt());
		this.setFavourite(item.getFavourite());
	}
	
	public String toString() {
		return "{TodoModel " + this.getId() + " " + this.getName() + "}";
	}
}
