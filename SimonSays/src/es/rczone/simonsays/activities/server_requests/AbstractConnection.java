package es.rczone.simonsays.activities.server_requests;

import android.content.Context;
import android.support.v4.app.Fragment;
import es.rczone.simonsays.tools.ConnectionListener;

public abstract class AbstractConnection implements ConnectionListener{
	
	protected Context context;

	public AbstractConnection(Context context){
		this.context = context;
	}
	
	public AbstractConnection(Fragment fragment){
		this.context = fragment.getActivity();
	}
	
	public abstract boolean inBackground(String... params);

	public abstract boolean validateDataBeforeConnection(String... params);

	public abstract void afterGoodConnection();

	public abstract void invalidInputData();

	public abstract void afterErrorConnection();
	
}
