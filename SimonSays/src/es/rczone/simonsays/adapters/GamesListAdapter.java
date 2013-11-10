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
import es.rczone.simonsays.model.Game;


public class GamesListAdapter extends BaseAdapter{
	
	@SuppressWarnings("unused")
	private final Activity activity;
    private final List<Game> lista;
    private LayoutInflater inflater = null;
    private Game game;
    private final String USER_SCORE = "Me: ";
    private final String OPP_SCORE = "Opp: ";
    
    public GamesListAdapter(Activity activity, List<Game> lista) {
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
            viewHolder.tv_opponent_userScore = (TextView) v.findViewById(R.id.itemlistgame_tv_userScore);
            viewHolder.tv_opponent_oppScore = (TextView) v.findViewById(R.id.itemlistgame_tv_oppScore);
            viewHolder.tv_date_last_move = (TextView) v.findViewById(R.id.itemlistgame_tv_date_last_move);
         
 
            v.setTag(viewHolder);
            
        } 
        else {
            viewHolder = (GameItemViewHolder) v.getTag();
        }
 
        game = lista.get(position);
        
        //FIXME date should be showned soon
        if (game != null) {
            viewHolder.iv_opponent_photo.setImageResource(R.drawable.icon_game);
            viewHolder.tv_opponent_name.setText(game.getOpponentName());
            viewHolder.tv_opponent_userScore.setText(USER_SCORE+game.getUserScore());
            viewHolder.tv_opponent_oppScore.setText(OPP_SCORE+game.getOppScore());
            viewHolder.tv_date_last_move.setText("");
        } 
        
        return v;
    	
    }
    

    @Override
    public int getCount() {
          return lista.size();
    }

    @Override
    public Game getItem(int arg0) {
          return lista.get(arg0);
    }

    @Override
    public long getItemId(int position) {
          return position;
    }
    
    static class GameItemViewHolder {
        ImageView iv_opponent_photo;
        TextView tv_opponent_name;
        TextView tv_opponent_userScore;
        TextView tv_opponent_oppScore;
        TextView tv_date_last_move;
    }

}
