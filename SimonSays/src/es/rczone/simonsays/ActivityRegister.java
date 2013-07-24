package es.rczone.simonsays;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class ActivityRegister extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
	}


	public void onClick(View v) {
		
		
		setEnableEditTextFields(false);
		setEnableEditTextFields(true);
      
        switch(v.getId()){
            case R.id.button_register:
            	break;
            case R.id.button_forget_pass:
            	break;
 
        }                 
    } 
	
	private void setEnableEditTextFields(boolean b){
		
		((EditText)findViewById(R.id.register_et_name)).setFocusable(b);
		((EditText)findViewById(R.id.register_et_name)).setFocusableInTouchMode(b);
		
		((EditText)findViewById(R.id.register_et_pass)).setFocusable(b);
		((EditText)findViewById(R.id.register_et_pass)).setFocusableInTouchMode(b);
		
		((EditText)findViewById(R.id.register_et_email)).setFocusable(b);
		((EditText)findViewById(R.id.register_et_email)).setFocusableInTouchMode(b);
		
	}

}