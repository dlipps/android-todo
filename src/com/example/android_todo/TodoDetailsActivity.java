package com.example.android_todo;

import java.util.Date;

import com.example.model.TodoModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class TodoDetailsActivity extends Activity {
	
	private EditText todoName;
	private EditText todoDescription;
	private DatePicker todoFaelligkeitDatum;
	private TimePicker todoZeit;
	private CheckBox todoErledigt;
	private CheckBox todoWichtigkeit;
	private Button todoSpeichern;
	private Button todoLoeschen;
	protected static final String logger = TodoDetailsActivity.class.getName();
//	private IntentTodoAccessor accessor;
	private TodoModel todo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tododetail);
		
		try{
//			accessor=new IntentTodoAccessor();
//			accessor.setActivity(this);
			
			todoName=(EditText)findViewById(R.id.nameEditText);
			todoDescription=(EditText)findViewById(R.id.beschreibung);
			todoFaelligkeitDatum=(DatePicker)findViewById(R.id.datumpicker);
			todoZeit=(TimePicker)findViewById(R.id.timePicker);
			todoZeit.setIs24HourView(true);
			todoErledigt=(CheckBox)findViewById(R.id.erledigtCheckBox);
			todoWichtigkeit=(CheckBox)findViewById(R.id.checkBoxWichtigkeit);
			todoSpeichern=(Button)findViewById(R.id.buttonSave);
			todoLoeschen=(Button)findViewById(R.id.buttonDelete);
			
			this.todo = (TodoModel) getIntent().getSerializableExtra(
					TodoListActivity.ARG_TODO_OBJECT);
			
//			if (accessor.hasTodo()) {
			if (this.todo!=null) {
//				TodoModel todo=accessor.readTodo();
				todoName.setText(todo.getName());
				todoDescription.setText(todo.getDescription());
	//			Log.i(TodoDetailsActivity.class.getName(),"INITIALISIEREN!!! Jahr: " + todo.getDate().getYear()+"Monat: "+todo.getDate().getMonth());
				todoFaelligkeitDatum.init(todo.getDate().getYear()+1900, todo.getDate().getMonth(), todo.getDate().getDate(), null);
				todoZeit.setCurrentHour(todo.getDate().getHours());
				todoZeit.setCurrentMinute(todo.getDate().getMinutes());
				todoErledigt.setChecked(todo.getErledigt()==1);
				todoWichtigkeit.setChecked(todo.getFavourite()==1);
			} else {
//				accessor.createTodo();
				this.todo = new TodoModel(-1, "", "", new Date(),0,0);
				todoLoeschen.setEnabled(false);
			}
			
			todoSpeichern.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!todoName.getText().toString().equals("")){
						processTodoSpeichern();
					}
					finish();
				}
			});
			
			todoLoeschen.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					processTodoLoeschen();
					
				}
			});
		
		
		}catch (Exception e) {
			String err = "got exception: " + e;
			Log.e(logger, err, e);
//			((DataAccessApplication) getApplication())
//			.reportError(this, err);
		}
		
		
		
	}
	
	protected void processTodoSpeichern() {
		todo.setName(todoName.getText().toString());
		todo.setDescription(todoDescription.getText().toString());
//		Log.i(TodoDetailsActivity.class.getName(),"SPEIHCERH!!! Jahr: " + faelligkeitDatum.getYear()+"Monat: "+faelligkeitDatum.getMonth());
		todo.setDate(new Date(todoFaelligkeitDatum.getYear()-1900,todoFaelligkeitDatum.getMonth(),todoFaelligkeitDatum.getDayOfMonth(),todoZeit.getCurrentHour(),todoZeit.getCurrentMinute()));
		todo.setErledigt(todoErledigt.isChecked() ? 1:0);
		todo.setFavourite(todoWichtigkeit.isChecked() ? 1:0);
		
		Intent returnIntent = new Intent();

		// set the item on the intent
		returnIntent.putExtra(TodoListActivity.ARG_TODO_OBJECT, this.todo);

		// set the result code
		setResult(TodoListActivity.RESPONSE_TODO_EDITED, returnIntent);

		finish();
	}

	protected void processTodoLoeschen() {

		Intent returnIntent = new Intent();

		// set the item
		returnIntent.putExtra(TodoListActivity.ARG_TODO_OBJECT, this.todo);

		// set the result code
		setResult(TodoListActivity.RESPONSE_TODO_DELETED, returnIntent);

		finish();
	}

}
