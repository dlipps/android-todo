package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;
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
	private ArrayList<String> contacts;

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
		contacts=new ArrayList<String>();
	}
	
	public TodoModel() {
		contacts=new ArrayList<String>();
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
	
	public void addContact(String contact){
		if(!contacts.contains(contact)){
			contacts.add(contact);
		}
	}
	
	public void addContactList(ArrayList<String> contacts){
		this.contacts.clear();
		this.contacts.addAll(contacts);
	}
	
	public void deleteContact(String contact){
		contacts.remove(contact);
	}
	
	public ArrayList<String> getContacts(){
		return contacts;
	}
	
	public TodoModel updateFrom(TodoModel todo) {
		this.setName(todo.getName());
		this.setDescription(todo.getDescription());
		this.setDate(todo.getDate());
		this.setErledigt(todo.getErledigt());
		this.setFavourite(todo.getFavourite());
		contacts=todo.getContacts();
		if(!this.getContacts().containsAll(contacts)){
			for(String s : contacts){
				this.addContact(s);
			}
			
		}
		return this;
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
