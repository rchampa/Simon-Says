package es.rczone.simonsays.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
	
	private List<CellColor> moves;
	
	public Board(){
		moves = new ArrayList<CellColor>();
	}
	
	public void addMove(CellColor cell){
		moves.add(cell);
	}

}
