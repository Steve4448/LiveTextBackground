package steve4448.livetextbackground.preference;

import steve4448.livetextbackground.R;
import steve4448.livetextbackground.widget.ImageModePreview;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ImageModePreviewPreference extends DialogPreference {
	public String imageLocation;
	public String imageMode = null;
	
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
		final ImageModePreview iMP = (ImageModePreview) view.findViewById(R.id.imageModePreview);
		final Spinner modeSpinner = (Spinner) view.findViewById(R.id.imageModeSpinner);
		imageMode = getPersistedString(null);
		
		if(imageLocation != null)
			iMP.setImageLocation(imageLocation);
		if(imageMode != null) {
			iMP.setImageMode(imageMode);
			@SuppressWarnings("unchecked")
			ArrayAdapter<String> d = ((ArrayAdapter<String>) modeSpinner.getAdapter());
			modeSpinner.setSelection(d.getPosition(imageMode));
		}
		modeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				imageMode = modeSpinner.getSelectedItem().toString();
				iMP.setImageMode(imageMode);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	}
	
	@Override
	public void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if(positiveResult) {
			persistString(imageMode);
		}
	}
}
