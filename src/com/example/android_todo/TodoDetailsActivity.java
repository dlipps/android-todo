package com.example.android_todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.example.model.TodoModel;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TodoDetailsActivity extends Activity {
	
	private static final int CHOOSE_CONTACT = 1;
	
	private EditText todoName;
	private EditText todoDescription;
	private DatePicker todoFaelligkeitDatum;
	private TimePicker todoZeit;
	private CheckBox todoErledigt;
	private CheckBox todoWichtigkeit;
	private Button todoSpeichern;
	private Button todoLoeschen;
	protected static final String logger = TodoDetailsActivity.class.getName();
	private TodoModel todo;
	private ListView listview;
	private Button addContact;
	private ArrayAdapter<Uri> adapter;
	private ArrayList<Uri> contacts;
	ContentResolver cr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tododetail);
		
		try{
			
			listview=(ListView)findViewById(R.id.listViewContacts);
			contacts = new ArrayList<Uri>();
			cr=getContentResolver();
			todoName=(EditText)findViewById(R.id.nameEditText);
			todoDescription=(EditText)findViewById(R.id.beschreibung);
			todoFaelligkeitDatum=(DatePicker)findViewById(R.id.datumpicker);
			todoZeit=(TimePicker)findViewById(R.id.timePicker);
			todoZeit.setIs24HourView(true);
			todoErledigt=(CheckBox)findViewById(R.id.erledigtCheckBox);
			todoWichtigkeit=(CheckBox)findViewById(R.id.checkBoxWichtigkeit);
			todoSpeichern=(Button)findViewById(R.id.buttonSave);
			todoLoeschen=(Button)findViewById(R.id.buttonDelete);
			addContact=(Button)findViewById(R.id.buttonAddContact);
			
			this.todo = (TodoModel) getIntent().getSerializableExtra(
					TodoListActivity.ARG_TODO_OBJECT);
			

			if (this.todo!=null) {

				todoName.setText(todo.getName());
				todoDescription.setText(todo.getDescription());
				todoFaelligkeitDatum.init(todo.getDate().getYear()+1900, todo.getDate().getMonth(), todo.getDate().getDate(), null);
				todoZeit.setCurrentHour(todo.getDate().getHours());
				todoZeit.setCurrentMinute(todo.getDate().getMinutes());
				todoErledigt.setChecked(todo.getErledigt()==1);
				todoWichtigkeit.setChecked(todo.getFavourite()==1);
				
			} else {
				
				this.todo = new TodoModel(-1, "", "", new Date(),0,0);
				todoLoeschen.setEnabled(false);
			}
			
			for(String s : todo.getContacts()){
				contacts.add(Uri.parse(s));
			}
//			contacts.addAll(todo.getContacts());
			
			adapter=new ArrayAdapter<Uri>(this, R.id.listViewContacts,contacts){
				
					@Override
					public View getView(final int position, View itemView,
							ViewGroup parent) {
						View layout = (ViewGroup)getLayoutInflater().inflate(
								R.layout.item_in_contactlist, null);
//						Log.i(logger, "Adapter: "+contacts.toString());
						Cursor cursor = cr.query(contacts.get(position), new String[] { ContactsContract.Data.DISPLAY_NAME }, null, null, null);
						cursor.moveToPosition(position);
						TextView contactName =(TextView)layout.findViewById(R.id.textViewContact);
						contactName.setText(cursor.getString(0));
						
						Button contactDelete=(Button)layout.findViewById(R.id.buttonDeleteContact);
						contactDelete.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								contacts.remove(contacts.get(position));
								adapter.notifyDataSetChanged();
								
							}
						});
						
						return layout;		
					}
				
			};
			
			
			this.adapter.setNotifyOnChange(true);
			listview.setAdapter(this.adapter);
			listview.setScrollBarStyle(ListView.SCROLLBARS_INSIDE_OVERLAY);
			
			addContact.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_PICK);
					intent.setData(ContactsContract.Contacts.CONTENT_URI);
					startActivityForResult(intent,TodoDetailsActivity.this.CHOOSE_CONTACT);
					
				}
			});

			
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
			
			listview.setScrollBarStyle(ListView.SCROLLBARS_INSIDE_OVERLAY);	
		
		
		}catch (Exception e) {
			String err = "got exception: " + e;
			Log.e(logger, err, e);
		}
		
		
		
	}
	
	protected void processTodoSpeichern() {
		todo.setName(todoName.getText().toString());
		todo.setDescription(todoDescription.getText().toString());
		todo.setDate(new Date(todoFaelligkeitDatum.getYear()-1900,todoFaelligkeitDatum.getMonth(),todoFaelligkeitDatum.getDayOfMonth(),todoZeit.getCurrentHour(),todoZeit.getCurrentMinute()));
		todo.setErledigt(todoErledigt.isChecked() ? 1:0);
		todo.setFavourite(todoWichtigkeit.isChecked() ? 1:0);
		for(Uri uri:contacts){
			todo.addContact(uri.toString());
		}
		Intent returnIntent = new Intent();
		returnIntent.putExtra(TodoListActivity.ARG_TODO_OBJECT, this.todo);
		setResult(TodoListActivity.RESPONSE_TODO_EDITED, returnIntent);
		Log.i(logger,"TodoDetailsActivity: "+todo.getContacts().toString());
		finish();
	}

	protected void processTodoLoeschen() {

		Intent returnIntent = new Intent();
		returnIntent.putExtra(TodoListActivity.ARG_TODO_OBJECT, this.todo);
		setResult(TodoListActivity.RESPONSE_TODO_DELETED, returnIntent);

		finish();
	}
	
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		ContentResolver cr = getContentResolver();
		Uri dataUri = Uri.withAppendedPath(ContactsContract.Contacts.lookupContact(cr, data.getData()), ContactsContract.Contacts.Data.CONTENT_DIRECTORY);
		if(!contacts.contains(dataUri)){
			contacts.add(dataUri);
			adapter.notifyDataSetChanged();
		}
	}

}
