package com.example.helper;

import java.util.Comparator;

import com.example.model.TodoModel;

public class TodoComparator implements Comparator<TodoModel> {
	
	public static int SORT_TYPE_ERLEDIGT=1;
	public static int SORT_TYPE_DATUM_WICHTIGKEIT=2;
	public static int SORT_TYPE_WICHTIGKEIT_DATUM=3;
	
	private int sort;
	
	public TodoComparator(int sort){
		this.sort=sort;
	}

	@Override
	public int compare(TodoModel todo1, TodoModel todo2) {
		
		if(sort==SORT_TYPE_ERLEDIGT){
			if(todo1.getErledigt()==0 && todo2.getErledigt()==1){
				return -1;
			}
			if(todo1.getErledigt()==1 && todo2.getErledigt()==0){
				return 1;
			}
			return 0;
			
		}
		
		if(sort==SORT_TYPE_DATUM_WICHTIGKEIT){
			if(todo1.getDate().getTime()<todo2.getDate().getTime()){
				return -1;
			}
			if(todo1.getDate().getTime()>todo2.getDate().getTime()){
				return 1;
			}
			if(todo1.getDate().getTime()==todo2.getDate().getTime()){
				
				if(todo1.getFavourite()==1 && todo2.getFavourite()==0){
					return -1;
				}
				if(todo1.getFavourite()==0 && todo2.getFavourite()==1){
					return 1;
				}
				return 0;
			}
			return 0;
		}
		
		if(sort==SORT_TYPE_WICHTIGKEIT_DATUM){
			if(todo1.getFavourite()==1 && todo2.getFavourite()==0){
				return -1;
			}
			if(todo1.getFavourite()==0 && todo2.getFavourite()==1){
				return 1;
			}
			if(todo1.getFavourite()==0 && todo2.getFavourite()==0 || todo1.getFavourite()==1 && todo2.getFavourite()==1){
				
				if(todo1.getDate().getTime()<todo2.getDate().getTime()){
					return -1;
				}
				if(todo1.getDate().getTime()>todo2.getDate().getTime()){
					return 1;
				}
				return 0;
			}
			return 0;
			
		}
		return 0;
	}

}
