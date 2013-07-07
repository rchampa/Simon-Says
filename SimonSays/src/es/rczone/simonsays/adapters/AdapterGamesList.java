package es.rczone.simonsays.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import es.rczone.simonsays.R;
import es.rczone.simonsays.game.GameItemList;


public class AdapterGamesList extends BaseAdapter{
	
	private final Activity activity;
    private final List<GameItemList> lista;
    private LayoutInflater inflater = null;
    private GameItemList gameItemList;
    
    public AdapterGamesList(Activity activity, List<GameItemList> lista) {
          super(); 
          this.activity = activity;
          this.lista = lista;
          inflater = activity.getLayoutInflater();
          
    }

    public View getView(int position, View v, ViewGroup parent) {
          
    	GameItemViewHolder viewHolder;
    	
        if (v == null) {
              
            v = inflater.inflate(R.layout.item_list_game, null);
 
            viewHolder = new GameItemViewHolder();
            viewHolder.iv_opponent_photo = (ImageView) v.findViewById(R.id.itemlistgame_iv_opponent_picture);
            viewHolder.tv_opponent_name = (TextView) v.findViewById(R.id.itemlistgame_tv_opponent_name);
            viewHolder.tv_date_last_move = (TextView) v.findViewById(R.id.itemlistgame_tv_date_last_move);
         
 
            v.setTag(viewHolder);
            
        } 
        else {
            viewHolder = (GameItemViewHolder) v.getTag();
        }
 
        gameItemList = lista.get(position);
        
        if (gameItemList != null) {
        	
//            viewHolder.iv_opponent_photo.setText(gameItemList.get...);
//            viewHolder.tv_opponent_name.setText(gameItemList.getOpponentName());
//            viewHolder.tv_date_last_move.setText(gameItemList.getDateLastMove());
            
        } 
        
        return v;
    	
    }
    

    @Override
    public int getCount() {
          return lista.size();
    }

    @Override
    public GameItemList getItem(int arg0) {
          return lista.get(arg0);
    }

    @Override
    public long getItemId(int position) {
          return position;
    }
    
    static class GameItemViewHolder {
        ImageView iv_opponent_photo;
        TextView tv_opponent_name;
        TextView tv_date_last_move;
    }

}
