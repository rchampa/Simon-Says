package es.rczone.simonsays.game;

import es.rczone.simonsays.model.Board;
import es.rczone.simonsays.model.Difficulty;

public class GameData {
	
	private int id;
	private String opponentUserName;
	private Board board;
	private Difficulty difficulty;
	
	public GameData(Board board, Difficulty difficulty) {
		this.board = board;
		this.difficulty = difficulty;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOpponentUserName() {
		return opponentUserName;
	}

	public void setOpponentUserName(String opponentUserName) {
		this.opponentUserName = opponentUserName;
	}


	
}
