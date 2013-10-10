package steve4448.livetextbackground.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
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
		Rect[] r = getRectsBasedOffMode(currentMode, new Rect(0, 0, image.getWidth(), image.getHeight()), dstRect);
		srcRect = r[0];
		dstRect = r[1];
		invalidate();
	}
	
	public ImageMode getImageMode() {
		return currentMode;
	}
	
	@SuppressLint("DefaultLocale")
    public static Rect[] getRectsBasedOffMode(String mode, Rect imageRect, Rect drawIntoRect) {
		return getRectsBasedOffMode(ImageMode.valueOf(mode.toUpperCase()), imageRect, drawIntoRect);
	}
	
	public static Rect[] getRectsBasedOffMode(ImageMode mode, Rect srcRect, Rect dstRect) {
		boolean doCenter = false;
		switch(mode) {
			case CENTER:
				doCenter = true;
			break;
			case FILL:
				srcRect.set(0, 0, dstRect.width(), srcRect.height());
				doCenter = true;
			break;
			case FIT:
				srcRect.set(0, 0, srcRect.width(), dstRect.height());
				doCenter = true;
			break;
			case STRECH:
				srcRect.set(0, 0, srcRect.width(), srcRect.height());
			break;
			case TILE:
				//TODO: this
			break;
		}
		if(doCenter)
			dstRect.set(dstRect.width() / 2 - srcRect.width() / 2, dstRect.height() / 2 - srcRect.height() / 2, dstRect.width() / 2 + srcRect.width() / 2, dstRect.height() / 2 + srcRect.height() / 2);
		return new Rect[] {srcRect, dstRect};
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
