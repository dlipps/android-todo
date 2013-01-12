package com.example.android_todo;

import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class LoginActivity extends Activity {
	
	private Button loginButton;
	private EditText emailEdit;
	private EditText passwordEdit;
	private String email=null;
	private String password=null;
	private AlertDialog.Builder ad;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		loginButton = (Button)findViewById(R.id.einloggenButton);
		emailEdit =(EditText)findViewById(R.id.emailadresseEdit);
		passwordEdit =(EditText)findViewById(R.id.passwordnumerischEdit);
		
		
		emailEdit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
					String text=v.getText().toString();
					if(validateEmail(text)){
						email = text;
						updateLoginButtonState();
					}else{
						
					}
					return false;
				}
				return false;
			}
		});
		
		passwordEdit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
					String text=v.getText().toString();
					if(text.length()==6){
						password = text;
						updateLoginButtonState();
					}else{
						
					}
					
					return false;
				}
				return false;
			}
		});
        
        loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TableLayout.class.getName(),"onClick(): " + v);
				startActivity(new Intent(LoginActivity.this,TodoAllActivity.class));	
			}
		});
        
//        context = LoginActivity.this;
//		String title = "";
//		String message = "Es ist keine gültige Email-Adresse, geben Sie eine gültige Email-Adresse ein!";
//		String button1String = "OK";
//		
//		ad = new AlertDialog.Builder(context);
//		ad.setTitle(title); 
//		ad.setMessage(message);
//		ad.setPositiveButton(button1String, new OnClickListener() {
//			public void onClick(DialogInterface dialog, int arg1) {
//
//			}
//		});
//
//		ad.setCancelable(true);
//		ad.setOnCancelListener(new OnCancelListener() {
//			public void onCancel(DialogInterface dialog) {
//			}
//		});
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
