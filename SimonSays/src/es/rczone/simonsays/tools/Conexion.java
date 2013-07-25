package es.rczone.simonsays.tools;

public interface Conexion {

	boolean duringConnection(String... params);
	boolean validateDataBeforeConnection(String... params);
	void afterGoodConnection();
	void afterErrorConnection();
}
