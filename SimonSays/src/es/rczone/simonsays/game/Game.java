package es.rczone.simonsays.game;

public class Game{
	
		
	private boolean finished;
	private GameData gameData;
	private GameState state;
	
	
	public Game(GameData gameData){
		this.gameData = gameData;
		finished = false;
		state = null;
	}
	
	public void init(String opponent){
		if(state == null){
			state = new State_Init(opponent);
		}
		else{
			//TODO expception
		}
	}
	
	public void requestGame(){
		if(state instanceof State_Init){
			state = new State_RequestGame();
		}
		else{
			//TODO expception
		}
	}
	
	public void acceptedRequestGame(int id){
		if(state instanceof State_RequestGame){
			state = new State_AcceptedRequestGame(id);
		}
		else{
			//TODO expception
		}
	}
	
	public void refusedRequestGame(String reason){
		if(state instanceof State_RequestGame){
			state = new State_RefuseRequestGame(reason);
		}
		else{
			//TODO expception
		}
	}
	
	public void play(){
		state.makeHisJob(gameData);
	}
	

	public boolean isFinished(){
		return finished;
	}
	public void finish(){
		finished = true;
	}
	
	
}
