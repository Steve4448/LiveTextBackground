package steve4448.livetextbackground.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;

public class ImageModePreview extends View {
	private String imageLocation;
	private Bitmap image;
	private Paint painter;
	
	public ImageModePreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}
	
	public ImageModePreview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}
	
	public void init(Context context, AttributeSet attrs, int defStyle) {
		painter = new Paint();
	}
	
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
		try {
	        image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(imageLocation));
        } catch(Exception e) {
	        e.printStackTrace();
        }
		invalidate();
	}
	
	public String getImageLocation() {
		return imageLocation;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(image == null)
				;//Todo: Draw an image of which indicates the image failed to load?
		else
			canvas.drawBitmap(image, 0, 0, painter);
	}
}
