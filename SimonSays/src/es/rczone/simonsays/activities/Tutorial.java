package es.rczone.simonsays.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import es.rczone.simonsays.R;

public class Tutorial extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tutorial);
				
	}
}
