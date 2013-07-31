package es.rczone.simonsays.daos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import es.rczone.simonsays.model.Move;

public class MoveDAO {
	
	protected static final String TABLE = "moves";
	protected static final String ID = "id";
	protected static final String GAME_ID = "game_id";
	protected static final String MOVE = "move";
	protected static final String CREATED_AT = "move_created_at";
	

	public ArrayList<Move> getAllMoves() {
		
		ArrayList<Move> list = new ArrayList<Move>();
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
		Cursor cursor = db.query(TABLE, null, null, null, null, null, null);

		if(cursor.moveToFirst()){
			Move valueObject;
			
			do{
				int id = cursor.getInt(cursor.getColumnIndex(ID));
				int game_id = cursor.getInt(cursor.getColumnIndex(GAME_ID));
				String move = cursor.getString(cursor.getColumnIndex(MOVE));
				String created_at = cursor.getString(cursor.getColumnIndex(CREATED_AT));
				Date date_created_at = Timestamp.valueOf(created_at);
				
				valueObject = new Move(id, game_id, move, date_created_at);
				
				list.add(valueObject);
			}while(cursor.moveToNext());
		}
		
		cursor.close();
		db.close();
		return list;
	}
	
	public Move get(int id) {
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
		Cursor cursor = db.query(TABLE, null, ID+"=?", new String[] {Integer.toString(id)}, null, null, null);
		Move valueObject = null;
		if (cursor.moveToFirst()) {
			int game_id = cursor.getInt(cursor.getColumnIndex(GAME_ID));
			String move = cursor.getString(cursor.getColumnIndex(MOVE));
			String created_at = cursor.getString(cursor.getColumnIndex(CREATED_AT));
			Date date_created_at = Timestamp.valueOf(created_at);
			
			valueObject = new Move(id, game_id, move, date_created_at);
		}
		
		cursor.close();
		db.close();
		return valueObject;
	}
	
	public long insert(Move move) {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(ID, move.getId());
		values.put(GAME_ID, move.getGame_id());
		values.put(MOVE, move.getMove());
		//date is assigned by default Current_timestamp
		
		long num = db.insert(TABLE, null, values);
		db.close();
		return num;
	}
	
	
//	
//	public int update(Move move) {
//		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
//		ContentValues values = new ContentValues();
//		//values.put(ID, );
//		int num = db.update(TABLE, values, ID + "=?", new String[]{Integer.toString(move.getId())});
//		
//		db.close();
//		return num;
//	}
	
	public void delete(int id) {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		db.delete(TABLE, ID+"=?", new String[]{Integer.toString(id)});
		db.close();
	}
	
	public void delete(Move move) {
		delete(move.getId());
	}
	
	public void deleteAll() {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		db.delete(TABLE, null, null);
		db.close();
	}
	
}
