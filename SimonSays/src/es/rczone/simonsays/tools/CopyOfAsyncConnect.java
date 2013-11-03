package es.rczone.simonsays.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;


public class CopyOfAsyncConnect extends AsyncTask<Void, String, String> {

	public static final String CONNECTION_ESTABLISHED = "ok";
	public static final String CONNECTION_ERROR = "error";
	//public static final String INVALID_INPUT_DATA = "invalid";
	private ProgressDialog progressDialog;
	private Context context;
	private ConnectionListener conexion;
	private String[] params;
	
	private String message;
	
	public CopyOfAsyncConnect(ConnectionListener connection, String... params){
		if(connection instanceof Context)
			this.context = (Context)connection;
		else if(connection instanceof Fragment){
			this.context = ((Fragment)connection).getActivity();
		}
		conexion = connection;
		this.params = params;
	}
		
	
	@Override
	protected void onPreExecute() {
		
		if(!conexion.validateDataBeforeConnection(this.params)){
			conexion.invalidInputData();
			this.cancel(true);
		}
		else{
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Connecting....");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
	}

	@Override
	protected String doInBackground(Void... p) {
		
		// enviamos y recibimos y analizamos los datos en segundo plano.
		if (conexion.inBackground(this.params)) {
			return CONNECTION_ESTABLISHED; // conexión valida
		} else {
			return CONNECTION_ERROR; // conexión invalida
		}

	}

	/*
	 * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
	 * a un estado de datos de entrada inválidos, conexión establecida o error de conexión
	 */
	@Override
	protected void onPostExecute(String result) {

		progressDialog.dismiss();// ocultamos progess dialog.
		
		Log.e("onPostExecute=", "" + result);

			
		if (result.equals(CONNECTION_ESTABLISHED))
			conexion.afterGoodConnection();
		else
			conexion.afterErrorConnection();
	}
	

}
