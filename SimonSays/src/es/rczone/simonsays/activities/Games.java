package es.rczone.simonsays.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.fragments.FragmentGamesList;
import es.rczone.simonsays.activities.fragments.listeners.ListListener;
import es.rczone.simonsays.controllers.GamesController;
import es.rczone.simonsays.daos.GameDAO;
import es.rczone.simonsays.model.Game;
import es.rczone.simonsays.model.GameStates;

public class Games extends FragmentActivity implements Handler.Callback, ListListener<Game>{

	private FragmentGamesList frg_rdy_games_list;
	private FragmentGamesList frg_waiting_games_list;
	private GamesController controller;
	private List<Game> games_rdy_list;
	private List<Game> games_waiting_list;
	private boolean listsUpdated;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_activity_games);
 
        frg_rdy_games_list =(FragmentGamesList)getSupportFragmentManager().findFragmentById(R.id.frg_game_ready_list);
        frg_rdy_games_list.setListener(this);
        frg_waiting_games_list = (FragmentGamesList)getSupportFragmentManager().findFragmentById(R.id.frg_game_waiting_list);
        frg_waiting_games_list.setListener(this);
        
        games_rdy_list = new ArrayList<Game>();
        games_waiting_list = new ArrayList<Game>();
        
        controller = new GamesController(games_rdy_list, games_waiting_list);
		
		controller.addOutboxHandler(new Handler(this));
		
		listsUpdated = false;
         
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(games_rdy_list!=null && games_waiting_list!=null && !listsUpdated){
			controller.handleMessage(GamesController.MESSAGE_GET_READY_LIST);
			controller.handleMessage(GamesController.MESSAGE_GET_WAITING_LIST);
			listsUpdated = true;
		}
		
	}
	
	
	@Override
	protected void onPause(){
		super.onPause();
		listsUpdated=false;
	}
	

	@Override
	public void onItemClicked(Game item) {

		switch(item.getState()){
		
		case PENDING:
			break;
		
		case IN_PROGRESS:
			Intent intent = new Intent(this, Board.class);
        	intent.putExtra(Board.GAME_ID, item.getID());
        	startActivity(intent);
			break;
		case REFUSED:
			break;
		case WAITING_FOR_MOVE:
			break;
		case WAITING_FOR_RESPONSE:
			break;
		case FINISHED:
			break;
		default:
			break;
		
		}
		
	}

	@Override
	public void onItemLongClicked(Game item) {
		// xxx
		item.setState(GameStates.WAITING_FOR_MOVE);
		new GameDAO().update(item);
		
	}

	@Override
	public boolean handleMessage(Message message) {
		
		switch(message.what) {
			case GamesController.MESSAGE_TO_VIEW_READY_MODEL_UPDATED:
				this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						frg_rdy_games_list.refreshList(games_rdy_list);
					}
				});
				return true;
			
			case GamesController.MESSAGE_TO_VIEW_WAITING_MODEL_UPDATED:
				this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						frg_waiting_games_list.refreshList(games_waiting_list);
					}
				});
				return true;
		}
		return false;
	}

}
