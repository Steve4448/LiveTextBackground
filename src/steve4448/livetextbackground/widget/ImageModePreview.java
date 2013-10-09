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
	public static enum ImageMode {
		CENTER,
		FILL,
		FIT,
		STRECH,
		TILE
	};
	
	private String imageLocation;
	private Bitmap image;
	private Paint painter;
	private ImageMode currentMode;
	
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
		currentMode = ImageMode.CENTER;
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
	
	public void setImageMode(ImageMode mode) {
		currentMode = mode;
		invalidate();
	}
	
	public ImageMode getImageMode() {
		return currentMode;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(image == null)
				;//TODO: Draw an image of which indicates the image failed to load?
		else {
			switch(currentMode) {
				case CENTER:
					break;
				case FILL:
					break;
				case FIT:
					break;
				case STRECH:
					break;
				case TILE:
					break;
			}
			canvas.drawBitmap(image, 0, 0, painter);
		}
	}
}
