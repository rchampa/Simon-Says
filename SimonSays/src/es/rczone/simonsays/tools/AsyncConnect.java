package es.rczone.simonsays.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


/*
 * CLASE ASYNCTASK
 * 
 * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos
 * y obtenemos los datos podria hacerse lo mismo sin usar esto pero si el
 * tiempo de respuesta es demasiado lo que podria ocurrir si la conexion es
 * lenta o el servidor tarda en responder la aplicacion sera inestable.
 * ademas observariamos el mensaje de que la app no responde.
 */
public class AsyncConnect extends AsyncTask<String, String, String> {

	public static final String CONNECTION_ESTABLISHED = "ok";
	public static final String CONNECTION_ERROR = "error";
	private ProgressDialog progressDialog;
	private Context context;
	private Conexion conexion;
	
	public AsyncConnect(Context context){
		this.context = context;
		conexion = (Conexion)context;
	}

	protected void onPreExecute() {
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Conectando con el servidor....");
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	protected String doInBackground(String... params) {
		
		// enviamos y recibimos y analizamos los datos en segundo plano.
		if (conexion.duringConnection(params)) {
			return CONNECTION_ESTABLISHED; // conexión valida
		} else {
			return CONNECTION_ERROR; // conexión invalida
		}

	}

	/*
	 * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
	 * a la sig. activity o mostramos error
	 */
	protected void onPostExecute(String result) {

		progressDialog.dismiss();// ocultamos progess dialog.
		Log.e("onPostExecute=", "" + result);

		if (result.equals(CONNECTION_ESTABLISHED))
			conexion.afterGoodConnection();
		else
			conexion.afterErrorConnection();
	}

}
