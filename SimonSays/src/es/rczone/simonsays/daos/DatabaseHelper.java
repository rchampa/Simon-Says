package es.rczone.simonsays.daos;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import es.rczone.simonsays.MemorizeApp;

final class DatabaseHelper extends SQLiteOpenHelper {

	@SuppressWarnings("unused")
	private static final String TAG = DatabaseHelper.class.getSimpleName();
	private static final String DATABASE_NAME = "Memorize";
	private static final int DATABASE_VERSION = 1;
	
	public DatabaseHelper() {
		super(MemorizeApp.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		final String friends = "CREATE TABLE " + FriendDAO.TABLE + "(" + 
				FriendDAO.NAME + " varchar(20) primary key, " +
				FriendDAO.STATE + " int not null default 0)";
		database.execSQL(friends);
		
		final String games = "CREATE TABLE " + GameDAO.TABLE + "(" +
				GameDAO.ID + " int primary key, " +
				GameDAO.OP_NAME + " varchar(20) not null, " +
				GameDAO.STATE + " int not null, " +
				GameDAO.TURN + " int not null, " +
				GameDAO.MY_SCORE + " smallint not null default 0, " +
				GameDAO.OPP_SCORE + " smallint not null default 0, " +
				GameDAO.CREATED_AT + " timestamp not null  default CURRENT_TIMESTAMP, "+
				GameDAO.NUM_MOVES + " int not null)";		
		database.execSQL(games);
		
		final String moves = "CREATE TABLE " + MoveDAO.TABLE + "(" +
				MoveDAO.ID + " integer primary key, " +
				MoveDAO.GAME_ID + " int not null, " +
				MoveDAO.MOVE + " varchar(60) not null, " +
				MoveDAO.CREATED_AT + " timestamp not null  default CURRENT_TIMESTAMP)";

		database.execSQL(moves);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// first iteration. do nothing.
	}
	

}
