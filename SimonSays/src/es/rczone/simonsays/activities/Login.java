package es.rczone.simonsays.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.server_requests.LoginRequest;
import es.rczone.simonsays.tools.AsyncConnect2;
import es.rczone.simonsays.tools.Tools;

public class Login extends Activity{
	
	private AsyncConnect2 connection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
			
		EditText name = (EditText)findViewById(R.id.login_et_name);
		EditText pass = (EditText)findViewById(R.id.login_et_password);
		
		//Calling these methods to disable and then enable, produce a focus lost effect.
		Tools.setEnableEditTextFields(false,name,pass);
		Tools.setEnableEditTextFields(true,name,pass);
	}


	public void onClick(View v) {	
		
		String name = ((EditText)findViewById(R.id.login_et_name)).getText().toString();
		String pass = ((EditText)findViewById(R.id.login_et_password)).getText().toString();
      
        switch(v.getId()){
            case R.id.login_button_login:
            	// register in game server
            	connection = new AsyncConnect2(new LoginRequest(this),name, pass);
            	connection.execute();
            	break;
            case R.id.login_button_forget:
            	Toast.makeText(this, "This function is disable yet.", Toast.LENGTH_SHORT).show();
            	break;
 
        }                 
    }
	
}
