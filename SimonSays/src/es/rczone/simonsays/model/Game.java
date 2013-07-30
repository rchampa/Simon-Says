package es.rczone.simonsays.model;

public class Game extends SimpleObservable<Friend> {
	
	private int id;
	private boolean finished;
	private Friend opponent;
	private ScoreBoard scoreBoard;
	private Board board;
	
	
	public Game(int id, Friend opponent, Difficulty difficulty){
		this.id = id;
		this.finished = false;
		this.opponent = opponent;
		this.board = new Board(difficulty);
		this.scoreBoard = new ScoreBoard();
	}
	
	public int getID(){
		return id;
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
