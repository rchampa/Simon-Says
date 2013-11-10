package es.rczone.simonsays.model;

import java.util.Date;

public class Move {

	private int id;
	private int game_id;
	private String move;
	private Date move_created_at;
	
	
	public Move(int id, int game_id, String move, Date move_created_at) {
		super();
		this.id = id;
		this.game_id = game_id;
		this.move = move;
		this.move_created_at = move_created_at;
	}
	
	public Move(int game_id, String move) {
		this.game_id = game_id;
		this.move = move;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGame_id() {
		return game_id;
	}
	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}
	public String getMove() {
		return move;
	}
	public void setMove(String move) {
		this.move = move;
	}
	public Date getMove_created_at() {
		return move_created_at;
	}
	public void setMove_created_at(Date move_created_at) {
		this.move_created_at = move_created_at;
	}
	
	
	
}
