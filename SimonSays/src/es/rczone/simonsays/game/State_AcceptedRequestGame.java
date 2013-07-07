package es.rczone.simonsays.game;

public class State_AcceptedRequestGame implements GameState{

	private int id;
	
	public State_AcceptedRequestGame(int id){
		this.id = id;
	}
	
	@Override
	public void makeHisJob(GameData gameData) {

		gameData.setId(id);
		
	}

}
