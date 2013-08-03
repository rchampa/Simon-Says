package es.rczone.simonsays.model;

import es.rczone.simonsays.model.Game.GameStates;

public class GameFactory {
	
	private final int NUM_MOVES = 4;
	
	public Game createNewGame(int id, Friend opponent){
		return new Game(id, GameStates.WAITING_FOR_RESPONSE, opponent, 0,0, NUM_MOVES,false);
	}
	
	public Game createNewGameFromRequest(int id, Friend opponent){
		return new Game(id, GameStates.PENDING, opponent, 0,0, NUM_MOVES,false);
	}
	
	//FIXME XXX for testing purposes...
	public Game loadGame(int id,  Friend opponent){
		return new Game(id, GameStates.WAITING_FOR_RESPONSE, opponent, 0,0, NUM_MOVES,false);
	}

}
