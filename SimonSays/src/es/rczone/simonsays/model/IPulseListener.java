package es.rczone.simonsays.model;

public interface IPulseListener {
	public void onPulse(long milisecs_passed);
	public void onFinish();
}
