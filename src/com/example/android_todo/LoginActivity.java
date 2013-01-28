package com.example.android_todo;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.example.helper.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class LoginActivity extends Activity {
	
	protected static final String logger = LoginActivity.class.getName();
	
	private static int LOKAL_ARBEITEN=0;
	private static int LOGIN_FALSCH=-1;
	private static int LOGIN_RICHTIG=1;
	private static int KEINE_VERBINDUNG=0;
	
	private Button loginButton;
	private EditText emailEdit;
	private EditText passwordEdit;
	private TextView passwordFalsch;
	private TextView emailFalsch;
	private TextView loginFalsch;
	private CheckBox lokalArb;
	private String email=null;
	private String password=null;
//	private AlertDialog.Builder ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		loginButton = (Button)findViewById(R.id.einloggenButton);
		emailEdit =(EditText)findViewById(R.id.emailadresseEdit);
		passwordEdit =(EditText)findViewById(R.id.passwordnumerischEdit);
		passwordFalsch=(TextView)findViewById(R.id.passwordFalsch);
		emailFalsch=(TextView)findViewById(R.id.emailFalsch);
		loginFalsch=(TextView)findViewById(R.id.loginFalsch);
		lokalArb=(CheckBox)findViewById(R.id.checkBoxLogin);
		
		emailEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				loginFalsch.setVisibility(View.INVISIBLE);
				emailFalsch.setVisibility(View.INVISIBLE);
			}
		});
		
		emailEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				emailFalsch.setVisibility(View.INVISIBLE);
				loginFalsch.setVisibility(View.INVISIBLE);
				
			}
		});

		emailEdit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
					String text=v.getText().toString();
					if(validateEmail(text)){
						email = text;
						updateLoginButtonState();
					}else{
//						ad.show();
						emailFalsch.setVisibility(View.VISIBLE);
					}
					return false;
				}
				return false;
			}
		});
		
		passwordEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				loginFalsch.setVisibility(View.INVISIBLE);
				passwordFalsch.setVisibility(View.INVISIBLE);
			}
		});
		
		passwordEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				passwordFalsch.setVisibility(View.INVISIBLE);
				loginFalsch.setVisibility(View.INVISIBLE);
				
			}
		});
		
		passwordEdit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					String text=v.getText().toString();
					if(text.length()==6){
						password = text;
						updateLoginButtonState();
					}else{
						passwordFalsch.setVisibility(View.VISIBLE);
					}
					
					return false;
				}
				return false;
			}
		});
        
        loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				new AsyncTask<Void, Void, Integer>() {
					@Override
					protected Integer doInBackground(Void... todos) {
						if(lokalArb.isChecked()){
							return LOKAL_ARBEITEN;
						}
						try {
							
							URL url = new URL("http://10.0.2.2:8080/TodoWebapp/");
							  HttpURLConnection con = (HttpURLConnection) url
							    .openConnection();
							  con.setConnectTimeout(100);
							  if(con.getResponseCode()==200){
								  Login lg = new Login();
								  List<String> loginlist=new ArrayList<String>();
								  loginlist.add(0, email);
								  loginlist.add(1, password);
								  if(lg.login(loginlist)){
									  return LOGIN_RICHTIG;
								  }else{
									  return LOGIN_FALSCH;
								  }
							  }else{
								  return KEINE_VERBINDUNG;
							  }
							
							  
							  } catch (Exception e) {
								 
								  Log.i(logger,"Exeption: "+e.toString());
								  return KEINE_VERBINDUNG;
							  }						
					}

					@Override
					protected void onPostExecute(Integer connection) {
						Intent intent = new Intent(LoginActivity.this,
						TodoListActivity.class);
						
						if(connection==LOGIN_RICHTIG){
							intent.putExtra("connection",TodoListActivity.CONNECTION);
							startActivity(intent);
						}else if(connection==LOKAL_ARBEITEN || connection==KEINE_VERBINDUNG){
							startActivity(intent);							
						}else if(connection==LOGIN_FALSCH){
							loginFalsch.setVisibility(View.VISIBLE);
						}

					}
				}.execute();
				
//				startActivity(new Intent(LoginActivity.this,TodoListActivity.class));	
			}
		});
        
//		String title = "Falsche Email-Eingabe";
//		String message = "Es ist keine gültige Email-Adresse, geben Sie eine gültige Email-Adresse ein!";
//		String button1String = "OK";
//
//        ad = new AlertDialog.Builder(this);
//		ad.setMessage(message)
//		       .setTitle(title)
//		       .setPositiveButton(button1String, new DialogInterface.OnClickListener()
//		       {
//		
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
////					emailEdit.setText("");
//					dialog.dismiss();					
//				}
//		    	   
//		       }).create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
	
	private void updateLoginButtonState() {
		loginButton.setEnabled(email != null && password !=null);
	}
	
	private boolean validateEmail(String email) {
	    Pattern pattern = Patterns.EMAIL_ADDRESS;
	    return pattern.matcher(email).matches();
	}

}
