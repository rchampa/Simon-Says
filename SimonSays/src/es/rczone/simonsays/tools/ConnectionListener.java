package es.rczone.simonsays.tools;

import android.content.Context;

public interface ConnectionListener {

	boolean validateDataBeforeConnection(String... params);
	void invalidInputData();
	boolean inBackground(String... params);
	void afterGoodConnection();
	void afterErrorConnection();
	Context getContext();
}
