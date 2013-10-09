package steve4448.livetextbackground.preference;

import steve4448.livetextbackground.R;
import steve4448.livetextbackground.widget.ImageModePreview;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

public class ImageModePreviewPreference extends DialogPreference {
	public String imageLocation;
	
	public ImageModePreviewPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.imagemodepreviewpreference);
		TypedArray extraAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.ImageModePreviewPreference, 0, 0);
		if(extraAttrs.hasValue(R.styleable.ImageModePreviewPreference_imageLocation))
			imageLocation = extraAttrs.getString(R.styleable.ImageModePreviewPreference_imageLocation);
		
		extraAttrs.recycle();
	}
	
	@Override
	public void onBindDialogView(View view) {
		super.onBindDialogView(view);
		ImageModePreview imP = (ImageModePreview)view.findViewById(R.id.imageModePreview);
		if(imageLocation != null)
			imP.setImageLocation(imageLocation);
		Toast.makeText(getContext(), R.string.placeholder_not_yet_implemented, Toast.LENGTH_SHORT).show();
	}
}
