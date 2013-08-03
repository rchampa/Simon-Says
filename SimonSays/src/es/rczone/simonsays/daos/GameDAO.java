package es.rczone.simonsays.daos;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import es.rczone.simonsays.model.Friend;
import es.rczone.simonsays.model.Game;
import es.rczone.simonsays.model.Game.GameStates;

public class GameDAO {

	protected static final String TABLE = "games";
	protected static final String ID = "id";
	protected static final String OP_NAME = "opponent";
	protected static final String STATE = "state";
	protected static final String NUM_MOVES = "num_moves";
	protected static final String MY_SCORE = "my_score";
	protected static final String OPP_SCORE = "opp_score";
	protected static final String TURN = "turn";
	protected static final String CREATED_AT = "createdAt";
		
	
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
				int userScore = cursor.getInt(cursor.getColumnIndex(MY_SCORE));
				int oppScore = cursor.getInt(cursor.getColumnIndex(OPP_SCORE));
				//Difficulty dif = new Difficulty(num_moves);
				boolean myTurn  = cursor.getInt(cursor.getColumnIndex(TURN)) > 0;
				
				valueObject = new Game(id, state, friend, userScore, oppScore, num_moves, myTurn);
				
				list.add(valueObject);
			}while(cursor.moveToNext());
		}
		
		cursor.close();
		db.close();
		return list;
	}
	
	public ArrayList<Game> getAllReadyGames() {
		
		ArrayList<Game> list = new ArrayList<Game>();
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
		String p = ""+GameStates.PENDING.ordinal();
		String i = ""+GameStates.IN_PROGRESS.ordinal();
		String f = ""+GameStates.FIRST_MOVE.ordinal();
		Cursor cursor = db.query(TABLE, null, STATE+"=? or "+STATE+"=? or "+STATE+"=?", new String[] {p,i,f}, null, null, null);
		if(cursor.moveToFirst()){
			Game valueObject;
			
			do{
				int id = cursor.getInt(cursor.getColumnIndex(ID));
				String opp_name = cursor.getString(cursor.getColumnIndex(OP_NAME));
				Friend friend = new FriendDAO().get(opp_name);
				int state_ord = cursor.getInt(cursor.getColumnIndex(STATE));
				GameStates state = GameStates.values()[state_ord];
				int num_moves = cursor.getInt(cursor.getColumnIndex(NUM_MOVES));
				int userScore = cursor.getInt(cursor.getColumnIndex(MY_SCORE));
				int oppScore = cursor.getInt(cursor.getColumnIndex(OPP_SCORE));
				//Difficulty dif = new Difficulty(num_moves);
				boolean myTurn  = cursor.getInt(cursor.getColumnIndex(TURN)) > 0;
				
				valueObject = new Game(id, state, friend, userScore, oppScore, num_moves, myTurn);
				
				list.add(valueObject);
			}while(cursor.moveToNext());
		}
		
		cursor.close();
		db.close();
		return list;
	}
	
	public ArrayList<Game> getAllWaitingGames() {
		
		ArrayList<Game> list = new ArrayList<Game>();
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
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
				int userScore = cursor.getInt(cursor.getColumnIndex(MY_SCORE));
				int oppScore = cursor.getInt(cursor.getColumnIndex(OPP_SCORE));
				//Difficulty dif = new Difficulty(num_moves);
				boolean myTurn  = cursor.getInt(cursor.getColumnIndex(TURN)) > 0;
				
				valueObject = new Game(id, state, friend, userScore, oppScore, num_moves, myTurn);
				
				list.add(valueObject);
			}while(cursor.moveToNext());
		}
		
		cursor.close();
		db.close();
		return list;
	}
	
	public Game get(int id) {
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
		Cursor cursor = db.query(TABLE, null, ID+"=?", new String[] {Integer.toString(id)}, null, null, null);
		Game valueObject = null;
		if (cursor.moveToFirst()) {
			String opp_name = cursor.getString(cursor.getColumnIndex(OP_NAME));
			Friend friend = new FriendDAO().get(opp_name);
			int state_ord = cursor.getInt(cursor.getColumnIndex(STATE));
			GameStates state = GameStates.values()[state_ord];
			int num_moves = cursor.getInt(cursor.getColumnIndex(NUM_MOVES));
			int userScore = cursor.getInt(cursor.getColumnIndex(MY_SCORE));
			int oppScore = cursor.getInt(cursor.getColumnIndex(OPP_SCORE));
			boolean myTurn  = cursor.getInt(cursor.getColumnIndex(TURN)) > 0;
			
			valueObject = new Game(id, state, friend, userScore,oppScore, num_moves, myTurn);
		}
		
		cursor.close();
		db.close();
		return valueObject;
	}
	
	public long insert(Game game) {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(ID, game.getID());
		values.put(OP_NAME, game.getOpponentName());
		values.put(STATE, game.getState().ordinal());
		values.put(NUM_MOVES, game.getNumMoves());
		values.put(MY_SCORE, game.getUserScore());
		values.put(OPP_SCORE, game.getOppScore());
		values.put(TURN, game.isMyTurn());
		
		long num = db.insert(TABLE, null, values);
		db.close();
		return num;
	}
	

	public int update(Game game) {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ID, game.getID());
		values.put(OP_NAME, game.getOpponentName());
		values.put(STATE, game.getState().ordinal());
		values.put(NUM_MOVES, game.getNumMoves());
		values.put(MY_SCORE, game.getUserScore());
		values.put(OPP_SCORE, game.getOppScore());
		values.put(TURN, game.isMyTurn());
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

