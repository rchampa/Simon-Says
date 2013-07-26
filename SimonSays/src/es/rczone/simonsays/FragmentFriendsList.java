package es.rczone.simonsays;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import es.rczone.simonsays.adapters.AdapterFriendsList;
import es.rczone.simonsays.model.Friend;

public class FragmentFriendsList extends Fragment {


	private ListView lstListado;
	private List<Friend> friends;
	private FriendsManager manager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_friends_list, container,false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);

		manager = FriendsManager.getInstance();
		friends = manager.getFriendsList();
		
		lstListado = (ListView) getView().findViewById(R.id.frag_list_friends);
		lstListado.setAdapter(new AdapterFriendsList(this.getActivity(), friends));
		
	}

}