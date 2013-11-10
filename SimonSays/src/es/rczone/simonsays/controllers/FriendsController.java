package es.rczone.simonsays.controllers;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.HandlerThread;
import es.rczone.simonsays.daos.FriendDAO;
import es.rczone.simonsays.model.Friend;


public class FriendsController extends Controller{
	
	@SuppressWarnings("unused")
	private static final String TAG = FriendsController.class.getSimpleName();
	private HandlerThread workerThread;
	private Handler workerHandler;
	
	
	public static final int MESSAGE_GET_FRIENDS_LIST = 11;
	public static final int MESSAGE_ADD_FRIEND = 12;
	public static final int MESSAGE_DELETE_FRIEND = 13;
	public static final int MESSAGE_TO_VIEW_MODEL_UPDATED = 21;

	
	private List<Friend> model;
	
	public FriendsController(List<Friend> model){
		this.model = model;
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
			case MESSAGE_GET_FRIENDS_LIST:
				getFriendsList();
				return true;
			
			case MESSAGE_ADD_FRIEND:
				addFriend((Friend)data);
				getFriendsList();
				return true;
				
			case MESSAGE_DELETE_FRIEND:
				deleteCounter((Integer)data);
				getFriendsList();
				return true;
			
		}
		return false;
	}

	private void addFriend(final Friend friend) {
		workerHandler.post(new Runnable() {
			@Override
			public void run() {
				synchronized (friend) {
					FriendDAO dao = new FriendDAO();
					dao.insert(friend);
				}
			}
		});
		
	}

	private void deleteCounter(Integer data) {
		// TODO Auto-generated method stub
		
	}

	private void getFriendsList() {
		workerHandler.post(new Runnable() {
			@Override
			public void run() {
				//Make the changes persistence 
				FriendDAO dao = new FriendDAO();
				
				ArrayList<Friend> friends = dao.getAllFriends();
				synchronized (model) {
					//remove old elements
					model.clear();
					
					//re-populate the model with updated values
					for (Friend friend : friends){
						model.add(friend);
					}
					
					//Send asynchronous message to the view
					notifyOutboxHandlers(MESSAGE_TO_VIEW_MODEL_UPDATED, 0, 0, null);
				}
			}
		});
		
	}

}
