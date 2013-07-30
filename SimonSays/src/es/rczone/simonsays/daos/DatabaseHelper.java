package es.rczone.simonsays.daos;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import es.rczone.simonsays.MemorizeApp;

final class DatabaseHelper extends SQLiteOpenHelper {

	@SuppressWarnings("unused")
	private static final String TAG = DatabaseHelper.class.getSimpleName();
	private static final String DATABASE_NAME = "TapCounter";
	private static final int DATABASE_VERSION = 1;
	
	public DatabaseHelper() {
		super(MemorizeApp.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		final String friends = "CREATE TABLE " + FriendDAO.TABLE + "(" + FriendDAO.NAME + " varchar(20) primary key )"; 
		database.execSQL(friends);
		
		final String games = "CREATE TABLE " + GameDAO.TABLE + "(" +
				GameDAO.ID + " integer primary key, " +
				GameDAO.OP_NAME + " varchar(20), " +
				GameDAO.IS_FINISH + " int, " +
				GameDAO.TURN + " int, " +
				GameDAO.NUM_MOVES + " int)";
		
		database.execSQL(games);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// first iteration. do nothing.
	}

}
