package es.rczone.simonsays.daos;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import es.rczone.simonsays.model.Friend;
import es.rczone.simonsays.model.Friend.FriendStates;


public class FriendDAO {
	
	protected static final String TABLE = "friends";
	protected static final String NAME = "name";
	protected static final String STATE = "state";
	
	
	
	public ArrayList<Friend> getAllFriends() {
		
		ArrayList<Friend> list = new ArrayList<Friend>();
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
		Cursor cursor = db.query(TABLE, null, null, null, null, null, null);

		if(cursor.moveToFirst()){
			
			do{
				String name = cursor.getString(cursor.getColumnIndex(NAME));
				int istate = cursor.getInt(cursor.getColumnIndex(STATE));
				FriendStates state = FriendStates.values()[istate];
				Friend valueObject = new Friend(name, state);
				
				list.add(valueObject);
			}while(cursor.moveToNext());
		}
		
		cursor.close();
		db.close();
		return list;
	}
	
	public Friend get(String name) {
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
		Cursor cursor = db.query(TABLE, null, NAME+"=?", new String[] {name}, null, null, null);
		Friend valueObject = null;
		if (cursor.moveToFirst()) {
			int state = cursor.getInt(cursor.getColumnIndex(STATE));
			valueObject = new Friend(name, FriendStates.values()[state]);
		}
		
		cursor.close();
		db.close();
		return valueObject;
	}
	
	public long insert(Friend friend) {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NAME, friend.getUserName());
		values.put(STATE, friend.getState().ordinal());
		long num = db.insert(TABLE, null, values);
		db.close();
		return num;
	}
	
	public int update(Friend friend) {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NAME, friend.getUserName());
		values.put(STATE, friend.getState().ordinal());
		int num = db.update(TABLE, values, NAME + "=?", new String[]{friend.getUserName()});
		
		db.close();
		return num;
	}
	
	public void delete(String name) {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		db.delete(TABLE, NAME+"=?", new String[]{name});
		db.close();
	}
	
	public void delete(Friend friend) {
		delete(friend.getUserName());
	}
	
	public void deleteAll() {
		SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
		db.delete(TABLE, null, null);
		db.close();
	}

}
