package es.rczone.simonsays.activities.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import es.rczone.simonsays.R;
import es.rczone.simonsays.adapters.FriendsListAdapter;
import es.rczone.simonsays.controllers.FriendsController;
import es.rczone.simonsays.model.Friend;

public class FragmentFriendsList extends Fragment implements Handler.Callback {


	private ListView lstListado;
	private List<Friend> friends;
	private FriendsController controller;
	private FriendsListAdapter adapter;
	private ControllerListener<FriendsController> listener;
	private ItemClickedListener<Friend> itemClickedListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_friends_list, container,false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);

		friends = new ArrayList<Friend>();
		controller = new FriendsController(friends);
		controller.addOutboxHandler(new Handler(this));
		
		adapter = new FriendsListAdapter(this.getActivity(), friends);
		lstListado = (ListView) getView().findViewById(R.id.list_friends);
		lstListado.setAdapter(adapter);
		
		lstListado.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				
				itemClickedListener.onItemClicked(friends.get(position));
			}
		});
		
		controller.handleMessage(FriendsController.MESSAGE_GET_FRIENDS_LIST);
		
		if(listener!=null)
			listener.onControllerCreated(controller);
	}
	
	public void setListener(ControllerListener<FriendsController> listener){
		this.listener = listener;
	}
	public void setListener(ItemClickedListener<Friend> itemClickedListener){
		this.itemClickedListener = itemClickedListener;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		controller.dispose();
	}

	@Override
	public boolean handleMessage(Message message) {
		
		switch(message.what) {
		case FriendsController.MESSAGE_TO_VIEW_MODEL_UPDATED:
			this.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					adapter.notifyDataSetChanged();
				}
			});
			return true;
		}
		return false;
	}

}