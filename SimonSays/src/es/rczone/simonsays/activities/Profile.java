package es.rczone.simonsays.activities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import es.rczone.simonsays.R;
import es.rczone.simonsays.adapters.CropOption;
import es.rczone.simonsays.adapters.CropOptionAdapter;
import es.rczone.simonsays.tools.Density;
import es.rczone.simonsays.tools.GlobalInfo;

public class Profile extends Activity{

	private final int SELECT_IMAGE = 100; 
	private final int CROP_IMAGE = 101;
	
	private int PROPER_SIZE;
	
	private ImageButton avatar;
	private Uri mImageCaptureUri;
	private GlobalInfo info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_profile);
		
		avatar = (ImageButton)findViewById(R.id.profile_avatar);
		info = new GlobalInfo(this);
		
		float scale = getApplicationContext().getResources().getDisplayMetrics().density;
		
		PROPER_SIZE = Density.falseConstuctor(scale).getSizePicProfile();
		
		loadAvatar();
		prepareInfo();
	}
	
	
	private void prepareInfo() {
		((TextView)findViewById(R.id.profile_name)).setText(info.USERNAME);
		((TextView)findViewById(R.id.profile_victories)).setText("Not available yet.");
		((TextView)findViewById(R.id.profile_defeats)).setText("Not available yet.");
		
	}


	private void loadAvatar() {
		FileInputStream fin = null;

	    try {
	        fin = openFileInput(info.PATH_PIC_FILE);
	        if(fin !=null && fin.available() > 0) {
	            Bitmap bmp=BitmapFactory.decodeStream(fin);
	            avatar.setImageBitmap(bmp);
	         } else {
	            //input stream has not much data to convert into  Bitmap
	          }
	    } 
	    catch (FileNotFoundException e) {
	    	//Toast.makeText(this, "Can't load your picture profile", Toast.LENGTH_SHORT).show();
	    } 
	    catch (IOException e) {
			// TODO Auto-generated catch block
		}
	    catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}


	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.profile_avatar:
			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, SELECT_IMAGE);   
			break;
		
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
	    switch(requestCode) { 
			case SELECT_IMAGE:
			    if(resultCode == RESULT_OK){  
			    	mImageCaptureUri = imageReturnedIntent.getData();
			        doCrop();
			    }
			break;
			
			case CROP_IMAGE:
				Bundle extras = imageReturnedIntent.getExtras();
		        if (extras != null) {	        	
		            Bitmap photo = extras.getParcelable("data");
		            avatar.setImageBitmap(photo);
		            try {
			            FileOutputStream fos = openFileOutput(info.PATH_PIC_FILE, Context.MODE_PRIVATE);
			            photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
						fos.close();
					} catch (Exception e) {
						Toast.makeText(this, "There was an error saving this picture", Toast.LENGTH_SHORT).show();
					}
		        }
			break;

		}
	    
	}
	
	
	private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
 
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
 
        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
 
        int size = list.size();
 
        if (size == 0) {
            Toast.makeText(this, "Your manufacter doesn't support cropp.", Toast.LENGTH_SHORT).show();
 
            return;
        } else {
            intent.setData(mImageCaptureUri);
 
            intent.putExtra("outputX", PROPER_SIZE);
            intent.putExtra("outputY", PROPER_SIZE);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
 
            if (size == 1) {
                Intent i        = new Intent(intent);
                ResolveInfo res = list.get(0);
 
                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
 
                startActivityForResult(i, CROP_IMAGE);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
 
                    co.title    = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon     = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);
 
                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
 
                    cropOptions.add(co);
                }
 
                CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);
 
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Crop App");
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                        startActivityForResult( cropOptions.get(item).appIntent, CROP_IMAGE);
                    }
                });
 
                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {
 
                        if (mImageCaptureUri != null ) {
                            getContentResolver().delete(mImageCaptureUri, null, null );
                            mImageCaptureUri = null;
                        }
                    }
                } );
 
                AlertDialog alert = builder.create();
 
                alert.show();
            }
        }
    }
}

