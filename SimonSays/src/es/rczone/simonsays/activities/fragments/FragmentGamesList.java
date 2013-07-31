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
import android.widget.ListView;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.fragments.listeners.ListListener;
import es.rczone.simonsays.adapters.GamesListAdapter;
import es.rczone.simonsays.model.Game;

public class FragmentGamesList  extends Fragment {

	private ListView lstListado;
	private List<Game> games;
	
	private GamesListAdapter adapter;
	private ListListener<Game> listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_game_list, container,false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);

		games = new ArrayList<Game>();
		
		adapter = new GamesListAdapter(this.getActivity(), games);
		lstListado = (ListView) getView().findViewById(R.id.list_games);
		lstListado.setAdapter(adapter);
		
		lstListado.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				
				listener.onItemClicked(games.get(position));
			}
		});
		
		
		
	}
	
	public void setListener(ListListener<Game> listener){
		this.listener = listener;
	}
	
	
	public void refreshList(){
		adapter.notifyDataSetChanged();
	}
	
	public void refreshList(List<Game> games){
		
		synchronized (this.games) {
			this.games.clear();
			
			for(Game g : games){
				this.games.add(g);
			}
			adapter.notifyDataSetChanged();
		}
		
	}
}
