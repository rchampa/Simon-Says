package es.rczone.simonsays.tools;

public interface ConnectionListener {

	boolean inBackground(String... params);
	boolean validateDataBeforeConnection(String... params);
	void afterGoodConnection();
	void invalidInputData();
	void afterErrorConnection();
}
