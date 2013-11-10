package es.rczone.simonsays.model;

public class Game extends SimpleObservable<Friend> {
	
	public enum GameStates {PENDING,WAITING_FOR_RESPONSE,FIRST_MOVE,WAITING_FOR_MOVE, IN_PROGRESS, REFUSED, FINISHED};
	
	private int id;
	private Friend opponent;
	//private ScoreBoard scoreBoard;
	//private Board board;
	private boolean isMyTurn;//This field is @deprecated
	private GameStates state;
	private int numMoves;
	private int userScore;
	private int oppScore;
	private boolean isOppMoveChecked;
	
	
//	public Game(int id, Friend opponent, int numMoves, boolean isMyTurn){
//		this.id = id;
//		this.opponent = opponent;
//		this.numMoves = numMoves;
//		//this.scoreBoard = new ScoreBoard();
//		this.isMyTurn = isMyTurn;
//		state = GameStates.WAITING_FOR_RESPONSE;
//		this.userScore = 0;
//		this.oppScore = 0;
//		isOppMoveChecked = false;
//	}
	
	
	public Game(int id, GameStates state, Friend opponent, int userScore, int oppScore, int numMoves, boolean isMyTurn) {
		super();
		this.id = id;
		this.state = state;
		this.opponent = opponent;
		//this.scoreBoard = scoreBoard;
		this.numMoves = numMoves;
		this.isMyTurn = isMyTurn;
		this.userScore = userScore;
		this.oppScore = oppScore;
		isOppMoveChecked = false;
	}


	public boolean isOppMoveChecked() {
		return isOppMoveChecked;
	}


	public void setOppMoveChecked(boolean isOppMoveChecked) {
		this.isOppMoveChecked = isOppMoveChecked;
	}


	public int getID(){
		return id;
	}
	
	public void setMyTurn(boolean turn){
		this.isMyTurn = turn;
	}
	
	public GameStates getState(){
		return state;
	}
	
	public void setState(GameStates state){
		this.state = state;
	}
	
	public boolean isFinished(){
		return state==GameStates.FINISHED;
	}
	
	public boolean isMyTurn(){
		return isMyTurn;
	}
	
	public String getOpponentName(){
		return opponent.getUserName();
	}
	
	public int getUserScore() {
		return userScore;
	}


	public void setUserScore(int userScore) {
		this.userScore = userScore;
	}

	public void upUserScore() {
		this.oppScore++;
	}

	public int getOppScore() {
		return oppScore;
	}


	public void setOppScore(int oppScore) {
		this.oppScore = oppScore;
	}

	public void upOppScore() {
		this.oppScore++;
	}
	
	
	public void setNumMoves(int numMoves){
		this.numMoves = numMoves;
	}
	
	public int getNumMoves(){
		return this.numMoves;
	}
}
