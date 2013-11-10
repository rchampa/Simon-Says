package es.rczone.simonsays.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;


public class AsyncConnect extends AsyncTask<String, String, String> {

	public static final String CONNECTION_ESTABLISHED = "ok";
	public static final String CONNECTION_ERROR = "error";
	public static final String INVALID_INPUT_DATA = "invalid";
	private ProgressDialog progressDialog;
	private Context context;
	private ConnectionListener conexion;
	
	private String message;
	
	public AsyncConnect(Context context){
		this.context = context;
		conexion = (ConnectionListener)context;
	}
	
	public AsyncConnect(Fragment context){
		this.context = context.getActivity();
		conexion = (ConnectionListener)context;
	}

	
	
	@Override
	protected void onPreExecute() {
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Connecting....");
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		
		if(!conexion.validateDataBeforeConnection(params))
			return INVALID_INPUT_DATA;
		
		// enviamos y recibimos y analizamos los datos en segundo plano.
		if (conexion.inBackground(params)) {
			return CONNECTION_ESTABLISHED; // conexi�n valida
		} else {
			return CONNECTION_ERROR; // conexi�n invalida
		}

	}

	/*
	 * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
	 * a un estado de datos de entrada inv�lidos, conexi�n establecida o error de conexi�n
	 */
	@Override
	protected void onPostExecute(String result) {

		progressDialog.dismiss();// ocultamos progess dialog.
		
		Log.e("onPostExecute=", "" + result);

			
		if (result.equals(CONNECTION_ESTABLISHED))
			conexion.afterGoodConnection();
		else if (result.equals(INVALID_INPUT_DATA))
			conexion.invalidInputData();
		else
			conexion.afterErrorConnection();
	}
	
	public void attachMessage(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}

}
