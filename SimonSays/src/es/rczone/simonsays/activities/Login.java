package es.rczone.simonsays.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.server_requests.LoginToServer;
import es.rczone.simonsays.tools.CopyOfAsyncConnect;

public class Login extends Activity{
	
	private CopyOfAsyncConnect connection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
			
		setEnableEditTextFields(false);
		setEnableEditTextFields(true);
	}


	public void onClick(View v) {
		
		
		String name = ((EditText)findViewById(R.id.login_et_name)).getText().toString();
		String pass = ((EditText)findViewById(R.id.login_et_password)).getText().toString();
      
        switch(v.getId()){
            case R.id.login_button_login:
            	// register in game server
            	connection = new CopyOfAsyncConnect(new LoginToServer(this),name, pass);
            	connection.execute();
            	break;
            case R.id.login_button_forget:
            	Toast.makeText(this, "This function is disable yet.", Toast.LENGTH_SHORT).show();
            	break;
 
        }                 
    }
	
	private void setEnableEditTextFields(boolean b){
		
		((EditText)findViewById(R.id.login_et_name)).setFocusable(b);
		((EditText)findViewById(R.id.login_et_name)).setFocusableInTouchMode(b);
		
		((EditText)findViewById(R.id.login_et_password)).setFocusable(b);
		((EditText)findViewById(R.id.login_et_password)).setFocusableInTouchMode(b);

		
	}


}
