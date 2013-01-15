package com.example.android_todo;

import java.util.Date;

import com.example.accessor.ITodoAccessor;
import com.example.accessor.IntentTodoAccessor;
import com.example.model.TodoModel;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
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
	private IntentTodoAccessor accessor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tododetail);
		try{
		accessor=new IntentTodoAccessor();
		accessor.setActivity(this);
		
		todoName=(EditText)findViewById(R.id.nameEditText);
		todoDescription=(EditText)findViewById(R.id.beschreibung);
		todoFaelligkeitDatum=(DatePicker)findViewById(R.id.datumpicker);
		todoZeit=(TimePicker)findViewById(R.id.timePicker);
		todoZeit.setIs24HourView(true);
		todoErledigt=(CheckBox)findViewById(R.id.erledigtCheckBox);
		todoWichtigkeit=(CheckBox)findViewById(R.id.checkBoxWichtigkeit);
		todoSpeichern=(Button)findViewById(R.id.buttonSave);
		todoLoeschen=(Button)findViewById(R.id.buttonDelete);
		
		if (accessor.hasTodo()) {
			TodoModel todo=accessor.readTodo();
			todoName.setText(todo.getName());
			todoDescription.setText(accessor.readTodo().getDescription());
//			Log.i(TodoDetailsActivity.class.getName(),"INITIALISIEREN!!! Jahr: " + todo.getDate().getYear()+"Monat: "+todo.getDate().getMonth());
			todoFaelligkeitDatum.init(todo.getDate().getYear()+1900, todo.getDate().getMonth(), todo.getDate().getDate(), null);
			todoZeit.setCurrentHour(todo.getDate().getHours());
			todoZeit.setCurrentMinute(todo.getDate().getMinutes());
			todoErledigt.setChecked(todo.getErledigt()==1);
			todoWichtigkeit.setChecked(todo.getFavourite()==1);
		} else {
			accessor.createTodo();
			todoLoeschen.setEnabled(false);
		}
		
		todoSpeichern.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!todoName.getText().toString().equals("")){
					processTodoSpeichern(accessor,todoName,todoDescription,todoFaelligkeitDatum,todoZeit,todoErledigt,todoWichtigkeit);
				}
				finish();
			}
		});
		
		todoLoeschen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				processTodoLoeschen(accessor);
				
			}
		});
		
		
		}catch (Exception e) {
			String err = "got exception: " + e;
			Log.e(logger, err, e);
//			((DataAccessApplication) getApplication())
//			.reportError(this, err);
		}
		
		
		
	}
	
	protected void processTodoSpeichern(ITodoAccessor accessor, EditText name,
			EditText description, DatePicker faelligkeitDatum, TimePicker zeit, CheckBox erledigt, CheckBox wichtigkeit) {
		accessor.readTodo().setName(name.getText().toString());
		accessor.readTodo().setDescription(description.getText().toString());
//		Log.i(TodoDetailsActivity.class.getName(),"SPEIHCERH!!! Jahr: " + faelligkeitDatum.getYear()+"Monat: "+faelligkeitDatum.getMonth());
		accessor.readTodo().setDate(new Date(faelligkeitDatum.getYear()-1900,faelligkeitDatum.getMonth(),faelligkeitDatum.getDayOfMonth(),zeit.getCurrentHour(),zeit.getCurrentMinute()));
		accessor.readTodo().setErledigt(erledigt.isChecked() ? 1:0);
		accessor.readTodo().setFavourite(wichtigkeit.isChecked() ? 1:0);
		
		accessor.writeTodo();

		finish();
	}

	protected void processTodoLoeschen(ITodoAccessor accessor) {

		accessor.deleteTodo();

		finish();
	}

}
