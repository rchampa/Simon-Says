package es.rczone.simonsays.activities.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.fragments.listeners.ListListener;
import es.rczone.simonsays.adapters.FriendsListAdapter;
import es.rczone.simonsays.model.Friend;

public class FragmentFriendsList extends Fragment {


	private ListView lstListado;
	private List<Friend> friends;
	
	private FriendsListAdapter adapter;
	private ListListener<Friend> listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_friends_list, container,false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);

		friends = new ArrayList<Friend>();
		
		adapter = new FriendsListAdapter(this.getActivity(), friends);
		lstListado = (ListView) getView().findViewById(R.id.list_friends);
		lstListado.setAdapter(adapter);
		
		lstListado.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				
				listener.onItemClicked(friends.get(position));
			}
		});
		
		lstListado.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
				listener.onItemLongClicked(friends.get(position));
				return true;
			}
			
		});
		
		
		
	}
	
	public void setListener(ListListener<Friend> listener){
		this.listener = listener;
	}
	
	
	public void refreshList(){
		adapter.notifyDataSetChanged();
	}
	
	public void refreshList(List<Friend> friends){
		
		this.friends.clear();
		
		for(Friend f : friends){
			this.friends.add(f);
		}
		adapter.notifyDataSetChanged();
	}

}