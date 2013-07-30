package es.rczone.simonsays.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
	
	private List<List<Colors>> userHistorial;
	private List<List<Colors>> opponentHistorial;
	private List<Colors> movesUser;
	private List<Colors> movesOpponent;
	private Difficulty difficulty;
	private int counter;
	
	public Board(Difficulty difficulty){
		userHistorial = new ArrayList<List<Colors>>();
		opponentHistorial = new ArrayList<List<Colors>>();
		movesUser = new ArrayList<Colors>();
		this.difficulty = difficulty;
		counter = this.difficulty.getNumMoves();
	}
	
	public void addMove(Colors color){
		movesUser.add(color);
		consumeAmove();
	}
	public void removeLastMove(){
		movesUser.remove(movesUser.size()-1);
	}	
	private void consumeAmove(){
		if(counter==0)
			return;
		
		counter--;
	}
	private boolean isMoveCompleted(){
		return counter==0;
	}
	
	public void setMovesOfOpponent(List<Colors> moves){
		movesOpponent = moves;		
	}
}
