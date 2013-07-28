package es.rczone.simonsays.customviews;

import es.rczone.simonsays.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class CustomImageView extends ImageView{

	protected final Bitmap bitmap;
	protected Drawable onFocusDrawable;
	
	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);		 		
		bitmap = ((BitmapDrawable)this.getDrawable()).getBitmap(); 
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		float iX = event.getX();
		float iY = event.getY();

		switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				//Makes sure that X and Y are not less than 0, and no more than the height and width of the image.
				if (iX >= 0 & iY >= 0 & iX < bitmap.getWidth() & iY < bitmap.getHeight()) {
					if (bitmap.getPixel((int) iX, (int) iY) != 0) {
						Log.i("Custom","wiiiii");
						setImageResource(R.drawable.rosco_yellow_shining);
					}
				}
				return true;
			
			case MotionEvent.ACTION_UP:
				setImageResource(R.drawable.rosco_yellow);
				return true;
		}
		
		return false;

	}
}
