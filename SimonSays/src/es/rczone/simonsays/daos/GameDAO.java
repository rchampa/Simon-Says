package es.rczone.simonsays.daos;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import es.rczone.simonsays.model.Board;
import es.rczone.simonsays.model.Difficulty;
import es.rczone.simonsays.model.Friend;
import es.rczone.simonsays.model.Game;
import es.rczone.simonsays.model.GameStates;

public class GameDAO {

	protected static final String TABLE = "games";
	protected static final String ID = "id";
	protected static final String OP_NAME = "opponent";
	protected static final String STATE = "state";
	protected static final String NUM_MOVES = "num_moves";
	protected static final String TURN = "turn";
	
	
	public ArrayList<Game> getAllGames() {
		
		ArrayList<Game> list = new ArrayList<Game>();
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, null, null, null, null, null);

		if(cursor.moveToFirst()){
			Game valueObject;
			
			do{
				int id = cursor.getInt(cursor.getColumnIndex(ID));
				String opp_name = cursor.getString(cursor.getColumnIndex(OP_NAME));
				Friend friend = new FriendDAO().get(opp_name);
				int state_ord = cursor.getInt(cursor.getColumnIndex(STATE));
				GameStates state = GameStates.values()[state_ord];
				int num_moves = cursor.getInt(cursor.getColumnIndex(NUM_MOVES));
				Difficulty dif = new Difficulty(num_moves);
				boolean myTurn  = cursor.getInt(cursor.getColumnIndex(TURN)) > 0;
				
				valueObject = new Game(id, state, friend, null, new Board(dif), myTurn);
				
				list.add(valueObject);
			}while(cursor.moveToNext());
		}
		
		cursor.close();
		db.close();
		return list;
	}
	
	public ArrayList<Game> getAllReadyGames() {
		
		ArrayList<Game> list = new ArrayList<Game>();
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, STATE+"=?", new String[] {Integer.toString(GameStates.IN_PROGRESS.ordinal())}, null, null, null);
		if(cursor.moveToFirst()){
			Game valueObject;
			
			do{
				int id = cursor.getInt(cursor.getColumnIndex(ID));
				String opp_name = cursor.getString(cursor.getColumnIndex(OP_NAME));
				Friend friend = new FriendDAO().get(opp_name);
				int state_ord = cursor.getInt(cursor.getColumnIndex(STATE));
				GameStates state = GameStates.values()[state_ord];
				int num_moves = cursor.getInt(cursor.getColumnIndex(NUM_MOVES));
				Difficulty dif = new Difficulty(num_moves);
				boolean myTurn  = cursor.getInt(cursor.getColumnIndex(TURN)) > 0;
				
				valueObject = new Game(id, state, friend, null, new Board(dif), myTurn);
				
				list.add(valueObject);
			}while(cursor.moveToNext());
		}
		
		cursor.close();
		db.close();
		return list;
	}
	
	public ArrayList<Game> getAllWaitingGames() {
		
		ArrayList<Game> list = new ArrayList<Game>();
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, STATE+"=? or " + STATE+"=?", new String[] {Integer.toString(GameStates.WAITING_FOR_RESPONSE.ordinal()),
																						Integer.toString(GameStates.WAITING_FOR_MOVE.ordinal())}, null, null, null);

		if(cursor.moveToFirst()){
			Game valueObject;
			
			do{
				int id = cursor.getInt(cursor.getColumnIndex(ID));
				String opp_name = cursor.getString(cursor.getColumnIndex(OP_NAME));
				Friend friend = new FriendDAO().get(opp_name);
				int state_ord = cursor.getInt(cursor.getColumnIndex(STATE));
				GameStates state = GameStates.values()[state_ord];
				int num_moves = cursor.getInt(cursor.getColumnIndex(NUM_MOVES));
				Difficulty dif = new Difficulty(num_moves);
				boolean myTurn  = cursor.getInt(cursor.getColumnIndex(TURN)) > 0;
				
				valueObject = new Game(id, state, friend, null, new Board(dif), myTurn);
				
				list.add(valueObject);
			}while(cursor.moveToNext());
		}
		
		cursor.close();
		db.close();
		return list;
	}
	
	public Game get(int id) {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, ID+"=?", new String[] {Integer.toString(id)}, null, null, null);
		Game valueObject = null;
		if (cursor.moveToFirst()) {
			String opp_name = cursor.getString(cursor.getColumnIndex(OP_NAME));
			Friend friend = new FriendDAO().get(opp_name);
			int state_ord = cursor.getInt(cursor.getColumnIndex(STATE));
			GameStates state = GameStates.values()[state_ord];
			int num_moves = cursor.getInt(cursor.getColumnIndex(NUM_MOVES));
			Difficulty dif = new Difficulty(num_moves);
			boolean myTurn  = cursor.getInt(cursor.getColumnIndex(TURN)) > 0;
			
			valueObject = new Game(id, state, friend, null, new Board(dif), myTurn);
		}
		
		cursor.close();
		db.close();
		return valueObject;
	}
	
	public long insert(Game game) {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(ID, game.getID());
		values.put(OP_NAME, game.opponentName());
		values.put(STATE, game.getState().ordinal());
		values.put(NUM_MOVES, game.getNumMoves());
		values.put(TURN, game.isMyTurn());
		
		long num = db.insert(TABLE, null, values);
		db.close();
		return num;
	}
	
	
	//FIXME
	public int update(Game game) {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		ContentValues values = new ContentValues();
		//values.put(ID, );
		int num = db.update(TABLE, values, ID + "=?", new String[]{Integer.toString(game.getID())});
		
		db.close();
		return num;
	}
	
	public void delete(int id) {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		db.delete(TABLE, ID+"=?", new String[]{Integer.toString(id)});
		db.close();
	}
	
	public void delete(Game game) {
		delete(game.getID());
	}
	
	public void deleteAll() {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		db.delete(TABLE, null, null);
		db.close();
	}
	
	
}

