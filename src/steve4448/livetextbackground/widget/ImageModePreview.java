package steve4448.livetextbackground.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class ImageModePreview extends View {
	private String imageLocation;
	
	public ImageModePreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}
	
	public ImageModePreview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}
	
	public void init(Context context, AttributeSet attrs, int defStyle) {
		
	}
	
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
	
	public String getImageLocation() {
		return imageLocation;
	}
}
