package es.rczone.simonsays.model;

public class Game extends SimpleObservable<Friend> {
	
	private int id;
	private Friend opponent;
	private ScoreBoard scoreBoard;
	private Board board;
	private boolean isMyTurn;
	private GameStates state;
	
	
	public Game(int id, Friend opponent, Difficulty difficulty, boolean isMyTurn){
		this.id = id;
		this.opponent = opponent;
		this.board = new Board(difficulty);
		this.scoreBoard = new ScoreBoard();
		this.isMyTurn = isMyTurn;
		state = GameStates.WAITING_FOR_RESPONSE;
	}
	
	
	public Game(int id, GameStates state, Friend opponent, ScoreBoard scoreBoard, Board board, boolean isMyTurn) {
		super();
		this.id = id;
		this.state = state;
		this.opponent = opponent;
		this.scoreBoard = scoreBoard;
		this.board = board;
		this.isMyTurn = isMyTurn;
	}


	public int getID(){
		return id;
	}
	
	public GameStates getState(){
		return state;
	}
	
	public boolean isFinished(){
		return state==GameStates.FINISHED;
	}
	
	public boolean isMyTurn(){
		return isMyTurn;
	}
	
	//FIXME
	public int getNumMoves(){
		return 4;
	}
	
	public String opponentName(){
		return opponent.getUserName();
	}
	
	public void move(Colors color){
		board.addMove(color);
	}
	
	public void removeLastMove(){
		board.removeLastMove();
	}

	
}
