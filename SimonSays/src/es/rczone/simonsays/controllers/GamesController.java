package es.rczone.simonsays.controllers;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.HandlerThread;
import es.rczone.simonsays.daos.GameDAO;
import es.rczone.simonsays.model.Game;


public class GamesController extends Controller{
	
	@SuppressWarnings("unused")
	private static final String TAG = GamesController.class.getSimpleName();
	private HandlerThread workerThread;
	private Handler workerHandler;
	
	
	public static final int MESSAGE_GET_READY_LIST = 11;
	public static final int MESSAGE_GET_WAITING_LIST = 12;
	public static final int MESSAGE_TO_VIEW_READY_MODEL_UPDATED = 21;
	public static final int MESSAGE_TO_VIEW_WAITING_MODEL_UPDATED = 22;

	
	private List<Game> model_ready_items;
	private List<Game> model_waiting_items;
	
	public GamesController(List<Game> model_ready_items, List<Game> model_waiting_items){
		this.model_ready_items = model_ready_items;
		this.model_waiting_items = model_waiting_items;
		
		//workerthread has created just for get a looper instance
		workerThread = new HandlerThread("Worker Thread");
		workerThread.start();
		workerHandler = new Handler(workerThread.getLooper());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		workerThread.getLooper().quit();
	}

	/**
	 * This method receive message from view(activity) and notifies(with a boolean value) whether the message was received (handle)
	 */
	@Override
	public boolean handleMessage(int what, Object data) {
		switch(what) {
			case MESSAGE_GET_READY_LIST:
				getReadyList();
				return true;
			
			case MESSAGE_GET_WAITING_LIST:
				getWaitingList();
				return true;
				
			
		}
		return false;
	}
	

	private void getReadyList() {
		workerHandler.post(new Runnable() {
			@Override
			public void run() {
				//Make the changes persistence 
				GameDAO dao = new GameDAO();
				
				ArrayList<Game> games = dao.getAllReadyGames();
				synchronized (model_ready_items) {
					//remove old elements
					model_ready_items.clear();
					
					//re-populate the model with updated values
					for (Game game : games){
						model_ready_items.add(game);
					}
					
					//Send asynchronous message to the view
					notifyOutboxHandlers(MESSAGE_TO_VIEW_READY_MODEL_UPDATED, 0, 0, null);
				}
			}
		});
		
	}
	
	private void getWaitingList() {
		workerHandler.post(new Runnable() {
			@Override
			public void run() {
				//Make the changes persistence 
				GameDAO dao = new GameDAO();
				
				ArrayList<Game> games = dao.getAllWaitingGames();
				synchronized (model_waiting_items) {
					//remove old elements
					model_waiting_items.clear();
					
					//re-populate the model with updated values
					for (Game game : games){
						model_waiting_items.add(game);
					}
					
					//Send asynchronous message to the view
					notifyOutboxHandlers(MESSAGE_TO_VIEW_WAITING_MODEL_UPDATED, 0, 0, null);
				}
			}
		});
		
	}

}
