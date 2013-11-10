package es.rczone.simonsays.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.fragments.listeners.AddFriendListener;
import es.rczone.simonsays.activities.server_requests.AddFriendRequest;
import es.rczone.simonsays.model.Friend;
import es.rczone.simonsays.tools.AsyncConnect;

public class FragmentAddFriend extends Fragment {

	private String nameNewFriend;
	private AddFriendListener listener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		final View view = inflater.inflate(R.layout.fragment_add_friend, container, false);
		
		final View button = view.findViewById(R.id.addfriends_button_add);
	    button.setOnClickListener(
	        new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	nameNewFriend = ((EditText)view.findViewById(R.id.addfriends_et_name)).getText().toString();
	            	new AsyncConnect(new AddFriendRequest(FragmentAddFriend.this),nameNewFriend).execute();
	            }
	        }
	    );
		return view;
	}

//	@Override
//	public void onActivityCreated(Bundle state) {
//		super.onActivityCreated(state);
//	}
	
	public void setListener(AddFriendListener listener){
		this.listener = listener;
	}
	
	public void addFriend(Friend newFriend){
		this.listener.onFriendshipAdded(newFriend);
	}
	


}