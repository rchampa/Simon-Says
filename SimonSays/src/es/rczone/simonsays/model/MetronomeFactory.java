package es.rczone.simonsays.model;

public class MetronomeFactory {
	
	public Metronome level1() throws Exception{
		return new Metronome(8000, 2000);
	}
	
	public Metronome level2() throws Exception{
		return new Metronome(10000, 2000);
	}
	
	public Metronome level3() throws Exception{
		return new Metronome(10000, 1000);
	}


}
