package es.rczone.simonsays.model;

public class Difficulty {
	
	private int numMoves;
	private int counter;
	
	public Difficulty(int numMoves){
		this.numMoves = numMoves;
		this.counter = numMoves;
	}

	public int getNumMoves(){
		return numMoves;
	}
	
	public void consumeAmove(){
		
		if(counter==0)
			return;
		
		counter--;
	}
	
	public boolean isMoveCompleted(){
		return counter==0;
	}
	
}
