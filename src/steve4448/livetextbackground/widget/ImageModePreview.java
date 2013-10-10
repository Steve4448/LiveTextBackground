package steve4448.livetextbackground.widget;

import steve4448.livetextbackground.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

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
	private Rect srcRect;
	private Rect dstRect;
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
		srcRect = new Rect(0, 0, 0, 0);
		dstRect = new Rect(0, 0, 0, 0);
		currentMode = ImageMode.CENTER;
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setImageMode(currentMode);
	}
	
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
		try {
	        image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(imageLocation));
			setImageMode(currentMode);
        } catch(Exception e) {
	        e.printStackTrace();
        }
		invalidate();
	}
	
	public String getImageLocation() {
		return imageLocation;
	}
	
	public void setImageMode(String mode) {
		setImageMode(ImageMode.valueOf(mode.toUpperCase(getResources().getConfiguration().locale)));
	}
	
	public void setImageMode(ImageMode mode) {
        srcRect = new Rect(0, 0, image.getWidth(), image.getHeight());
		dstRect.set(0, 0, getWidth(), getHeight());
		currentMode = mode;
		boolean doCenter = false;
		switch(currentMode) {
			case CENTER:
				srcRect.set(0, 0, image.getWidth(), image.getHeight());
				doCenter = true;
			break;
			case FILL:
				srcRect.set(0, 0, dstRect.width(), image.getHeight());
				doCenter = true;
			break;
			case FIT:
				srcRect.set(0, 0, image.getWidth(), dstRect.height());
				doCenter = true;
			break;
			case STRECH:
				srcRect.set(0, 0, image.getWidth(), image.getHeight());
			break;
			case TILE:
				//TODO: this
				Toast.makeText(getContext(), R.string.placeholder_not_yet_implemented, Toast.LENGTH_SHORT).show();
			break;
		}
		if(doCenter)
			dstRect.set(dstRect.width() / 2 - srcRect.width() / 2, dstRect.height() / 2 - srcRect.height() / 2, dstRect.width() / 2 + srcRect.width() / 2, dstRect.height() / 2 + srcRect.height() / 2);
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
				canvas.drawBitmap(image, srcRect, dstRect, painter);
		}
	}
}
