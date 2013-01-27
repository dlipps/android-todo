package com.example.android_todo;


import com.example.accessor.ResteasyTodoCRUDAccessor;
import com.example.model.ITodoCRUDAccessor;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

public class TodoRemoteApplication extends Application {
	
	protected static String logger = TodoRemoteApplication.class
			.getSimpleName();
	
	private ResteasyTodoCRUDAccessor resteasyAccessor;
//	private IDataItemCRUDAccessor localAccessor;
	
	public TodoRemoteApplication() {
		Log.i(logger, "<constructor>()");

		// initialise the accessors
//		this.localAccessor = new LocalDataItemCRUDAccessor();

		this.resteasyAccessor = new ResteasyTodoCRUDAccessor(
				getWebappBaseUrl());

		Log.i(logger, "<constructor>(): created accessors.");
	}

	public void reportError(Activity activity, String error) {
		Toast.makeText(activity, error, Toast.LENGTH_LONG).show();
	}
	
	public ITodoCRUDAccessor getDataItemAccessor(int accessorId) {
		ITodoCRUDAccessor accessor=null;

//		switch (accessorId) {
//		case R.integer.localAccessor:
//			accessor = this.localAccessor;
//			break;
//		case R.integer.resteasyFramework:
//			accessor = this.resteasyAccessor;
//			break;
//		case R.integer.urlClass:
//			accessor = this.urlAccessor;
//			break;
//		case R.integer.apacheHttpClient:
//			accessor = this.httpClientAccessor;
//			break;
//		case R.integer.urlConnection:
//			accessor = this.httpURLConnectionAccessor;
//			break;
//		default:
//			Log.e(logger, "got unknown accessor id: " + accessorId
//					+ ". Will use local accessor...");
//			accessor = localAccessor;
//		}
//
//		Log.i(logger, "will provide accessor: " + accessor);

		return accessor;
	}
	
	/**
	 * get the baseUrl of the webapp used as data source and media resource provider
	 * @return
	 */
	public String getWebappBaseUrl() {
		return "http://10.0.2.2:8080/TodoWebapp/";
	}

}
