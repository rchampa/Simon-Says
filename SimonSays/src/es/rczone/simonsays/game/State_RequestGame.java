package es.rczone.simonsays.game;

public class State_RequestGame implements GameState{

	
	public State_RequestGame(){
		
	}
	
	@Override
	public void makeHisJob(GameData gameData) {
		
		String opponentUserName = gameData.getOpponentUserName();
		
		//Send a message to server
	}



}
