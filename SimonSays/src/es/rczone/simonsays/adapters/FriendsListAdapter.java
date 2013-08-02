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
import es.rczone.simonsays.model.Friend;

public class FriendsListAdapter extends BaseAdapter{
	
	@SuppressWarnings("unused")
	private final Activity activity;
    private final List<Friend> list;
    private LayoutInflater inflater = null;
    private Friend friendItemList;
    
    public FriendsListAdapter(Activity activity, List<Friend> list) {
          super(); 
          this.activity = activity;
          this.list = list;
          inflater = activity.getLayoutInflater();
          
    }

    @Override
    public int getCount() {
          return list.size();
    }

    @Override
    public Friend getItem(int arg0) {
          return list.get(arg0);
    }

    @Override
    public long getItemId(int position) {
          return position;
    }
    
    

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		FriendItemViewHolder viewHolder;
    	
        if (v == null) {
              
            v = inflater.inflate(R.layout.item_list_friend, null);
 
            viewHolder = new FriendItemViewHolder();
            viewHolder.iv_opponent_photo = (ImageView) v.findViewById(R.id.itemlistfriend_iv_opponent_picture);
            viewHolder.tv_opponent_name = (TextView) v.findViewById(R.id.itemlistfriend_tv_opponent_name);
            viewHolder.tv_opponent_state= (TextView) v.findViewById(R.id.itemlistfriend_tv_opponent_state);

            v.setTag(viewHolder);
            
        } 
        else {
            viewHolder = (FriendItemViewHolder) v.getTag();
        }
 
        friendItemList = list.get(position);
        
        if (friendItemList != null) {
        	
            viewHolder.iv_opponent_photo.setImageResource(R.drawable.icon_hombre);
            viewHolder.tv_opponent_name.setText(friendItemList.getUserName());
            switch(friendItemList.getState()){
			case ACCEPTED:
				viewHolder.tv_opponent_state.setText("Accepted");
				break;
			case REJECTED:
				viewHolder.tv_opponent_state.setText("Rejected");
				break;
			case WAITING_FOR_RESPONSE:
				viewHolder.tv_opponent_state.setText("Waiting for response");
				break;
			case ASKED_YOU:
				viewHolder.tv_opponent_state.setText("He asked you for friend");
				break;
			default:
				break;
            
            }
            	

        } 

        return v;
	}

	static class FriendItemViewHolder {
        ImageView iv_opponent_photo;
        TextView tv_opponent_name;
        TextView tv_opponent_state;
    }

}
