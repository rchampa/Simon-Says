package es.rczone.simonsays.game;

public class State_Init implements GameState{
	
	private String opponentPlayerName;
	
	public State_Init(String opponentPlayerName){
		this.opponentPlayerName = opponentPlayerName;
	}

	@Override
	public void makeHisJob(GameData gameData) {
				
		gameData.setOpponentUserName(opponentPlayerName);
	}


}
