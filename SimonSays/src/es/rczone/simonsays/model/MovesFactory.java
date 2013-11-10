package es.rczone.simonsays.model;

public class MovesFactory {
	
	public Move createMove(int gameID, String move){
		return new Move(gameID, move);
	}

}
