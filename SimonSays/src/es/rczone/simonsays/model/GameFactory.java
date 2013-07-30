package es.rczone.simonsays.model;

public class GameFactory {
	
	public Game createNewGame(int id, Friend opponent){
		return new Game(id, GameStates.WAITING_FOR_RESPONSE, opponent, null, new Board(new Difficulty(4)),false);
	}
	
	public Game createNewGameFromRequest(int id, Friend opponent){
		return new Game(id, GameStates.PENDING, opponent, null, new Board(new Difficulty(4)),false);
	}
	
	//FIXME XXX for testing purposes...
	public Game loadGame(int id,  Friend opponent){
		return new Game(id, GameStates.WAITING_FOR_RESPONSE, opponent, null, new Board(new Difficulty(4)),false);
	}

}
