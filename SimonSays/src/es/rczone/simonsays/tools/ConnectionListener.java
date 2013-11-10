package es.rczone.simonsays.tools;

import android.content.Context;

public interface ConnectionListener {

	boolean inBackground(String... params);
	boolean validateDataBeforeConnection(String... params);
	void afterGoodConnection();
	void invalidInputData();
	void afterErrorConnection();
	Context getContext();
}
