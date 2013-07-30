package es.rczone.simonsays;

import android.app.Application;
import android.content.Context;
import android.util.Log;


public class MemorizeApp extends Application {
	
	private static final String TAG = MemorizeApp.class.getSimpleName();
	private static MemorizeApp INSTANCE;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "MemorizeApp.onCreate was called");
		INSTANCE = this;
	}
	
	public static Context getContext() {
		return INSTANCE.getApplicationContext();
	}
}
