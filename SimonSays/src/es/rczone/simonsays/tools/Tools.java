package es.rczone.simonsays.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;
import es.rczone.simonsays.GCMIntentService;

public class Tools {
	
	public static String dateToString(Date date){
		String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return dateString;
	}

	
	public static void askConfirmation(Context context, String title, String message, int iconID, 
					String positiveOption, String negativeOption, final IDialogOperations idialog){
		
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
		 
		// Setting Dialog Title
		alertDialog2.setTitle(title);
		 
		// Setting Dialog Message
		alertDialog2.setMessage(message);
		 
		// Setting Icon to Dialog
		alertDialog2.setIcon(iconID);
		 
		// Setting Positive "Yes" Btn
		alertDialog2.setPositiveButton(positiveOption,
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	idialog.positiveOperation();
		            }
		        });
		// Setting Negative "NO" Btn
		alertDialog2.setNegativeButton(negativeOption,
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		               idialog.negativeOperation();
		                
		            }
		        });
		 
		// Showing Alert Dialog
		alertDialog2.show();
		
		
	}
	
	public static void updateFriendsUI(Context context){
    	Intent intent = new Intent(GCMIntentService.FRIENDS_UPDATER_ACTION);
    	context.sendBroadcast(intent);//This will be received in Friends activity
    }
    public static void updateGamesUI(Context context){
    	Intent intent = new Intent(GCMIntentService.GAMES_UPDATER_ACTION);
    	context.sendBroadcast(intent);//This will be received in Games activity
    }
    
	public static void setEnableEditTextFields(boolean b, EditText... fields){
		
		for(EditText field : fields){
			field.setFocusable(b);
			field.setFocusableInTouchMode(b);
		}
			
	}
    
}
