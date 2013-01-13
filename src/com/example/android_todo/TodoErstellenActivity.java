package com.example.android_todo;

import java.util.Date;

import com.example.accessor.SQLiteTodoAccessor;
import com.example.helper.TodoContentProvider;
import com.example.model.TodoModel;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class TodoErstellenActivity extends Activity {
	
	private EditText todoName;
	private EditText todoDescription;
	private DatePicker todoFaelligkeitDatum;
	private TimePicker todoZeit;
	private CheckBox todoErledigt;
	private RatingBar todoWichtigkeit;
	private Button todoSpeichern;
	private Button todoLoeschen;
	
	private String name;
	private String beschreibung;
	private Date faelligkeit;
//	private Date time;
	private int erledigt;
	private float wichtigkeit;
	
	
	protected static final String logger = TodoErstellenActivity.class.getName();
	private SQLiteTodoAccessor accessor;
	private TodoModel todoM;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tododetail);
		
		accessor=new SQLiteTodoAccessor();
		accessor.setActivity(this);
		todoM=new TodoModel();
		erledigt=0;
		wichtigkeit=0f;
		name=null;
		faelligkeit=new Date();
		
		todoName=(EditText)findViewById(R.id.nameEditText);
		todoDescription=(EditText)findViewById(R.id.beschreibung);
		todoFaelligkeitDatum=(DatePicker)findViewById(R.id.datumpicker);
		todoZeit=(TimePicker)findViewById(R.id.timePicker);
		todoErledigt=(CheckBox)findViewById(R.id.erledigtCheckBox);
		todoWichtigkeit=(RatingBar)findViewById(R.id.wichtigkeitBar);
		todoSpeichern=(Button)findViewById(R.id.buttonSave);
		todoLoeschen=(Button)findViewById(R.id.buttonDelete);
		
//		time=new Date(todoFaelligkeitDatum.getYear(),todoFaelligkeitDatum.getMonth(),todoFaelligkeitDatum.getDayOfMonth());
		
		todoName.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
					name=v.getText().toString();
					return false;
				}
				return false;
			}
		});
		
		todoDescription.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
					beschreibung=v.getText().toString();
					return false;
				}
				return false;
			}
		});
		
		todoFaelligkeitDatum.init(todoFaelligkeitDatum.getYear(),todoFaelligkeitDatum.getMonth(),todoFaelligkeitDatum.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				faelligkeit.setYear(year);
				faelligkeit.setMonth(monthOfYear);
				faelligkeit.setDate(dayOfMonth);
			}
		});
		
		todoZeit.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				faelligkeit.setHours(hourOfDay);
				faelligkeit.setMinutes(minute);				
			}
		});
		
		todoErledigt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1){
					erledigt=1;
				}
			}
		});
		
		todoWichtigkeit.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				if(fromUser){
					wichtigkeit=rating;
				}
				
			}
		});
		
		todoSpeichern.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(name == null){
				}else if(faelligkeit==null){
				}else{
					Log.i(logger, "Model erstellt!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
					todoM.setName(name);
					todoM.setDescription(beschreibung);
					todoM.setErledigt(erledigt);
					todoM.setFavourite((int) wichtigkeit);
					todoM.setDate(faelligkeit);
					accessor.addTodo(todoM);
				}
			}
		});
		
		
	}

}
