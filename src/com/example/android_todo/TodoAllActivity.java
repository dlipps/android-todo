package com.example.android_todo;


import com.example.accessor.SQLiteTodoAccessor;
import com.example.helper.TodoComparator;
import com.example.model.TodoModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;

public class TodoAllActivity extends Activity {
	
//	private Button todoErstellenButton;
	private ListView listview;
	private SQLiteTodoAccessor accessor;
	public static final String ARG_TODO_OBJECT = "todoObject";
	public static final int RESPONSE_TODO_EDITED = 1;
	public static final int RESPONSE_TODO_DELETED = 2;
	public static final int RESPONSE_NOCHANGE = -1;
	public static final int REQUEST_TODO_DETAILS = 2;
	public static final int REQUEST_TODO_CREATION = 1;
	
	protected static final String logger = TodoAllActivity.class.getName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemlistview);
						
		try {
			// access the listview
			/*
			 * access the list view for the options to be displayed
			 */
			listview = (ListView) findViewById(R.id.list);

			// determine the accessor which shall be used
			accessor = new SQLiteTodoAccessor();
			accessor.setActivity(this);
			Log.i(logger, "will use accessor: " + accessor);
			// if we have an ActivityDataAccessor, we pass ourselves


			// set the title of the activity given the accessor class

			// obtain the adapter from the accessor, passing it the id of the
			// item layout to be used
			final ListAdapter adapter = accessor
					.getAdapter();
			
			if(adapter instanceof ArrayAdapter){
				((ArrayAdapter<TodoModel>) adapter).sort(new TodoComparator(TodoComparator.SORT_TYPE_ERLEDIGT));
			}
			

			// set the adapter on the list view
			listview.setAdapter(adapter);
			
			

			// set a listener that reacts to the selection of an element
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterView,
						View itemView, int itemPosition, long itemId) {
					Log.i(logger, "Click!!!!!!!!!!!!!");
					
					TodoModel todo = accessor.getTodo(itemPosition,itemId);
					
					processTodoSelection(todo);
				}

			});
			Button todoErstellenButton = (Button)findViewById(R.id.newItemButton);

			todoErstellenButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					processTodoNeuRequest();
					
				}
			});
			
			Spinner spinner = (Spinner) findViewById(R.id.filterSpinner);
			ArrayAdapter<CharSequence> adapterFilter = ArrayAdapter.createFromResource(this,
			        R.array.Filter, android.R.layout.simple_spinner_item);
			adapterFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapterFilter);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					if(adapter instanceof ArrayAdapter){
						if(arg2==0){
							((ArrayAdapter<TodoModel>) adapter).sort(new TodoComparator(TodoComparator.SORT_TYPE_ERLEDIGT));
						}else if(arg2==1){
							((ArrayAdapter<TodoModel>) adapter).sort(new TodoComparator(TodoComparator.SORT_TYPE_DATUM_WICHTIGKEIT));
						}else if(arg2==2){
							((ArrayAdapter<TodoModel>) adapter).sort(new TodoComparator(TodoComparator.SORT_TYPE_WICHTIGKEIT_DATUM));
						}
					}
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
					if(adapter instanceof ArrayAdapter){
						((ArrayAdapter<TodoModel>) adapter).sort(new TodoComparator(TodoComparator.SORT_TYPE_ERLEDIGT));
					}
					
				}
				
			});
			
			// set the listview as scrollable
			listview.setScrollBarStyle(ListView.SCROLLBARS_INSIDE_OVERLAY);		
			
		} catch (Exception e) {
			String err = "got exception: " + e;
			Log.e(logger, err, e);
//			((DataAccessApplication) getApplication())
//			.reportError(this, err);
		}					
	}
	
	protected void processTodoNeuRequest() {
		Log.i(logger, "processNewItemRequest()");
		Intent intent = new Intent(TodoAllActivity.this,
				TodoDetailsActivity.class);
		// we only specify the accessor class
//		intent.putExtra(DataAccessActivity.ARG_ACCESSOR_CLASS,
//				IntentDataItemAccessorImpl.class.getName());

		// start the details activity with the intent
		startActivityForResult(intent, REQUEST_TODO_CREATION);
	}

	protected void processTodoSelection(TodoModel item) {
		Log.i(logger, "processItemSelection(): " + item);
		// create an intent for opening the details view
		Intent intent = new Intent(TodoAllActivity.this,
				TodoDetailsActivity.class);
		// pass the item to the intent
		intent.putExtra(ARG_TODO_OBJECT, item);
		// also specify the accessor class
//		intent.putExtra(DataAccessActivity.ARG_ACCESSOR_CLASS,
//				IntentDataItemAccessorImpl.class.getName());

		// start the details activity with the intent
		startActivityForResult(intent, REQUEST_TODO_DETAILS);
	}

	/**
	 * process the result of the item details subactivity, which may be the
	 * creation, modification or deletion of an item.
	 * 
	 * NOTE that is not necessary to invalidate the listview if changes are
	 * communicated to the adapter using notifyDataSetChanged()
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(logger, "onActivityResult(): " + data);

		TodoModel todo = data != null ? (TodoModel) data
				.getSerializableExtra(ARG_TODO_OBJECT) : null;

		// check which request we had
		if (requestCode == REQUEST_TODO_DETAILS) {
			if (resultCode == RESPONSE_TODO_EDITED) {
				Log.i(logger, "onActivityResult(): updating the edited item");
				this.accessor.updateTodo(todo);
			} else if (resultCode == RESPONSE_TODO_DELETED) {
				this.accessor.deleteTodo(todo);
			}
			// this.listview.invalidate();
		} else if (requestCode == REQUEST_TODO_CREATION
				&& resultCode == RESPONSE_TODO_EDITED) {
			Log.i(logger, "onActivityResult(): adding the created item");
			this.accessor.addTodo(todo);
		}

	}
	
	@Override
	public void onDestroy() {
		Log.i(logger,"onDestroy(): will signal finalisation to the accessors");
		this.accessor.finalise();
		
		super.onStop();
	}

}
