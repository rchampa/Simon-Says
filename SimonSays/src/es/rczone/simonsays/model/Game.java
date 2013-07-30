package es.rczone.simonsays.model;

public class Game extends SimpleObservable<Friend> {
	
	private int id;
	private boolean finished;
	private Friend opponent;
	private ScoreBoard scoreBoard;
	private Board board;
	private boolean isMyTurn;
	
	
	public Game(int id, Friend opponent, Difficulty difficulty, boolean isMyTurn){
		this.id = id;
		this.finished = false;
		this.opponent = opponent;
		this.board = new Board(difficulty);
		this.scoreBoard = new ScoreBoard();
		this.isMyTurn = isMyTurn;
	}
	
	
	public Game(int id, boolean finished, Friend opponent,
			ScoreBoard scoreBoard, Board board, boolean isMyTurn) {
		super();
		this.id = id;
		this.finished = finished;
		this.opponent = opponent;
		this.scoreBoard = scoreBoard;
		this.board = board;
		this.isMyTurn = isMyTurn;
	}


	public int getID(){
		return id;
	}
	
	public boolean isFinished(){
		return finished;
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
