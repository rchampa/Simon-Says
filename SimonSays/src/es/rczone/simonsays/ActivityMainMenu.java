package es.rczone.simonsays;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class ActivityMainMenu extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
	}

}