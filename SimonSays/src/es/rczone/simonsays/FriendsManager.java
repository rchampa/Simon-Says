package es.rczone.simonsays;

import java.util.ArrayList;
import java.util.List;

import es.rczone.simonsays.model.Friend;

public class FriendsManager {
	
	private static FriendsManager INSTANCE = null;

	private ArrayList<Friend> friends;

	private FriendsManager() {
		friends = new ArrayList<Friend>();
	}

	private static void createInstance() {
		if (INSTANCE == null) {
			synchronized (FriendsManager.class) {
				if (INSTANCE == null) {
					INSTANCE = new FriendsManager();
				}
			}
		}
	}
	
	public static FriendsManager getInstance() {
        createInstance();
        return INSTANCE;
    }

	public void addFriend(Friend f) {
		friends.add(f);
	}

	public void removeFriend(Friend f) {
		friends.remove(f);
	}
	
	public List<Friend> getFriendsList(){
		return friends;
	}
	
}
