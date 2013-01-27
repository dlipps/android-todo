package com.example.android_todo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.accessor.ResteasyTodoCRUDAccessor;
import com.example.accessor.SQLiteTodoCRUDAccessor;
import com.example.helper.TodoComparator;
import com.example.model.ITodoCRUDAccessor;
import com.example.model.TodoModel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class TodoListActivity extends Activity {
	
	private ListView listview;
	private ITodoCRUDAccessor accessor;
	public static final String ARG_TODO_OBJECT = "todoObject";
	public static final int RESPONSE_TODO_EDITED = 1;
	public static final int RESPONSE_TODO_DELETED = 2;
	public static final int RESPONSE_NOCHANGE = -1;
	public static final int REQUEST_TODO_DETAILS = 2;
	public static final int REQUEST_TODO_CREATION = 1;
	public static final int CONNECTION = 1;
	
	protected static final String logger = TodoListActivity.class.getName();
	private List<TodoModel> todolist;
	private ArrayAdapter<TodoModel> adapter;
	private Activity activity;

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
			activity=this;
			
			if(getIntent().getExtras() != null){
				Log.i(logger,"Accessor REST ");
				accessor = new ResteasyTodoCRUDAccessor("http://10.0.2.2:8080/TodoWebapp/");
			}else{
				Toast.makeText(this, "Der Server ist nicht erreichbar", Toast.LENGTH_LONG).show();
				accessor = new SQLiteTodoCRUDAccessor(this);
			}

			// determine the accessor which shall be used
//			accessor = new ResteasyTodoCRUDAccessor("http://10.0.2.2:8080/TodoWebapp/todo");
//			accessor = new SQLiteTodoCRUDAccessor(this);
			
			this.todolist=new ArrayList<TodoModel>();
			this.adapter = new ArrayAdapter<TodoModel>(this,
					R.layout.item_in_listview, todolist) {

				// we override getView and manually create the views for each
				// list element
				@Override
				public View getView(final int position, View itemView,
						ViewGroup parent) {
					Log.i(logger,
							"getView() has been invoked for item: "
									+ todolist.get(position) + ", listItemView is: "
									+ itemView);
					View layout = (ViewGroup)getLayoutInflater().inflate(
							R.layout.item_in_listview, null);
					
					if(todolist.get(position).getDate().getTime()<new Date().getTime()){
						Log.i(logger,"Farbe ändern");
						layout.setBackgroundColor(Color.RED);
					}
					
					TextView todoName =(TextView)layout.findViewById(R.id.itemName);
					todoName.setText(todolist.get(position).getName());
					TextView todoFaelligkeit =(TextView)layout.findViewById(R.id.todoall_itemFaelligkeit);
					todoFaelligkeit.setText(todolist.get(position).getDate().toString());
					CheckBox todoErledigt=(CheckBox)layout.findViewById(R.id.todoall_itemErledightcheckBox);
					todoErledigt.setChecked(todolist.get(position).getErledigt()==1);
					todoErledigt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							todolist.get(position).setErledigt(isChecked ? 1:0);
							accessor.updateTodo(todolist.get(position));
							adapter.notifyDataSetChanged();	
						}
					});
					
					CheckBox todoWichtig=(CheckBox)layout.findViewById(R.id.todoall_itemWichtigcheckBox);
					todoWichtig.setChecked(todolist.get(position).getFavourite()==1);
					todoWichtig.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							todolist.get(position).setFavourite(isChecked ? 1:0);
							accessor.updateTodo(todolist.get(position));
							adapter.notifyDataSetChanged();						
						}
					});
					return layout;
					
				}
			};
			// the adapter is set to display changes immediately
			this.adapter.setNotifyOnChange(true);

			// set the adapter on the list view
			listview.setAdapter(this.adapter);
			Log.i(logger, "will use accessor: " + accessor);
			// if we have an ActivityDataAccessor, we pass ourselves


			// set the title of the activity given the accessor class

			// obtain the adapter from the accessor, passing it the id of the
			// item layout to be used

			

			adapter.sort(new TodoComparator(TodoComparator.SORT_TYPE_ERLEDIGT));

			

			// set the adapter on the list view
			listview.setAdapter(adapter);
			
			

			// set a listener that reacts to the selection of an element
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterView,
						View itemView, int itemPosition, long itemId) {
					
					TodoModel todo =  todolist.get(itemPosition);
					
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
						if(arg2==0){
							adapter.sort(new TodoComparator(TodoComparator.SORT_TYPE_ERLEDIGT));
						}else if(arg2==1){
							adapter.sort(new TodoComparator(TodoComparator.SORT_TYPE_DATUM_WICHTIGKEIT));
						}else if(arg2==2){
							adapter.sort(new TodoComparator(TodoComparator.SORT_TYPE_WICHTIGKEIT_DATUM));
						}					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
					adapter.sort(new TodoComparator(TodoComparator.SORT_TYPE_ERLEDIGT));					
				}
				
			});
			
			// set the listview as scrollable
			listview.setScrollBarStyle(ListView.SCROLLBARS_INSIDE_OVERLAY);	
			
			new AsyncTask<Void, Void, List<TodoModel>>() {
				@Override
				protected List<TodoModel> doInBackground(Void... todos) {
//					return accessor.getTodos();
					if(accessor instanceof SQLiteTodoCRUDAccessor){
						return accessor.getTodos();
					}else{
						SQLiteTodoCRUDAccessor	sqaccessor = new SQLiteTodoCRUDAccessor(activity);
						List<TodoModel> todorest=accessor.getTodos();
						List<TodoModel> todosql=sqaccessor.getTodos();
						
						if(todorest.isEmpty()){
							
							for(TodoModel todo: todosql){
								accessor.addTodo(todo);
							}
							sqaccessor.finalise();
							return todosql;
							
						}else{

							for(TodoModel todo: todosql){
								sqaccessor.deleteTodo(todo.getId());
							}
							for(TodoModel todo: todorest){
								sqaccessor.addTodo(todo);
							}
							
							sqaccessor.finalise();
							return todorest;
						}
						
					}
				}

				@Override
				protected void onPostExecute(List<TodoModel> todos) {
					todolist.addAll(todos);
					adapter.notifyDataSetChanged();
				}
			}.execute();
			
		} catch (Exception e) {
			String err = "got exception: " + e;
			Log.e(logger, err, e);
//			((DataAccessApplication) getApplication())
//			.reportError(this, err);
		}					
	}
	
	protected void processTodoNeuRequest() {
		Log.i(logger, "processNewItemRequest()");
		Intent intent = new Intent(TodoListActivity.this,
				TodoDetailsActivity.class);
		// we only specify the accessor class
//		intent.putExtra(DataAccessActivity.ARG_ACCESSOR_CLASS,
//				IntentDataItemAccessorImpl.class.getName());

		// start the details activity with the intent
		startActivityForResult(intent, REQUEST_TODO_CREATION);
	}

	protected void processTodoSelection(TodoModel todo) {
		Log.i(logger, "processItemSelection(): " + todo);
		// create an intent for opening the details view
		Intent intent = new Intent(TodoListActivity.this,
				TodoDetailsActivity.class);
		// pass the item to the intent
		intent.putExtra(ARG_TODO_OBJECT, todo);
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
				
		if (requestCode == REQUEST_TODO_CREATION
			&& resultCode == RESPONSE_TODO_EDITED) {
		Log.i(logger, "onActivityResult(): adding the created item");

					/**
					 * all accessor calls are executed asynchronously
					 */
			new AsyncTask<TodoModel, Void, TodoModel>() {
				@Override
				protected TodoModel doInBackground(TodoModel... todos) {
					return TodoListActivity.this.accessor.addTodo(todos[0]);
				}
	
				@Override
				protected void onPostExecute(TodoModel todo) {
					if (todo != null) {
						adapter.add(todo);
					}
				}
			}.execute(todo);
			
		}

		// check which request we had
		if (requestCode == REQUEST_TODO_DETAILS) {
			if (resultCode == RESPONSE_TODO_EDITED) {
				Log.i(logger, "onActivityResult(): updating the edited item");
				new AsyncTask<TodoModel, Void, TodoModel>() {
					@Override
					protected TodoModel doInBackground(TodoModel... todos) {
						return TodoListActivity.this.accessor
								.updateTodo(todos[0]);
					}

					@Override
					protected void onPostExecute(TodoModel todo) {
						if (todo != null) {
							// read out the item from the list and update it
							todolist.get(todolist.indexOf(todo)).updateFrom(
									todo);
//							todolist.get(adapter.getPosition(todo)).updateFrom(
//									todo);
							// notify the adapter that the item has been changed
							adapter.notifyDataSetChanged();
						}
					}
				}.execute(todo);
			} else if (resultCode == RESPONSE_TODO_DELETED) {
				new AsyncTask<TodoModel, Void, TodoModel>() {
					@Override
					protected TodoModel doInBackground(TodoModel... todos) {
						if (TodoListActivity.this.accessor.deleteTodo(todos[0]
								.getId())) {
							return todos[0];
						} else {
							Log.e(logger, "the item" + todos[0]
									+ " could not be deleted by the accessor!");
							return null;
						}
					}

					@Override
					protected void onPostExecute(TodoModel todo) {
						if (todo != null) {
							adapter.remove(todo);
						}
					}
				}.execute(todo);
			}
		}
	}
	
	@Override
	public void onDestroy() {
		Log.i(logger,"onDestroy(): will signal finalisation to the accessors");
		if(accessor instanceof SQLiteTodoCRUDAccessor){
			((SQLiteTodoCRUDAccessor) this.accessor).finalise();
		}
		super.onStop();
	}

}

