package es.rczone.simonsays.model;

public class Friend extends SimpleObservable<Friend> {
	
	public enum FriendStates{
		WAITING_FOR_RESPONSE_FRIENDSHIP,
		ASKED_YOU_FOR_FRIENDSHIP, //friendship
		ACCEPTED, //friendship
		REJECTED, //friendship
		WAITING_FOR_RESPONSE_GAME,
		ASKED_YOU_GAME,
		PLAYING_WITH_YOU, 
	};
	private String userName;
	private FriendStates state;
	
	
	public Friend(String userName, FriendStates state){
		this.userName = userName;
		this.state = state;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
		notifyObservers(this);
	}

	public FriendStates getState() {
		return state;
	}

	public void setState(FriendStates state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Friend other = (Friend) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	
	
}
