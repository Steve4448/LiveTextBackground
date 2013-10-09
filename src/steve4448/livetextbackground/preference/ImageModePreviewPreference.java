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
import android.widget.Spinner;
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
		final ImageModePreview iMP = (ImageModePreview)view.findViewById(R.id.imageModePreview);
		if(imageLocation != null)
			iMP.setImageLocation(imageLocation);
		final Spinner modeSpinner = (Spinner)view.findViewById(R.id.spinner1);
		modeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if(modeSpinner.getSelectedItem().equals("Center")) {
					iMP.setImageMode(ImageModePreview.ImageMode.CENTER);
				} else if(modeSpinner.getSelectedItem().equals("Fill")) {
					iMP.setImageMode(ImageModePreview.ImageMode.FILL);
				} else if(modeSpinner.getSelectedItem().equals("Fit")) {
					iMP.setImageMode(ImageModePreview.ImageMode.FIT);
				} else if(modeSpinner.getSelectedItem().equals("Strech")) {
					iMP.setImageMode(ImageModePreview.ImageMode.STRECH);
				} else if(modeSpinner.getSelectedItem().equals("Tile")) {
					iMP.setImageMode(ImageModePreview.ImageMode.TILE);
				} else {
					System.out.println("Selected unhandled mode: " + modeSpinner.getSelectedItem().toString() + ".");
				}
            }

			@Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
		});
		Toast.makeText(getContext(), R.string.placeholder_not_yet_implemented, Toast.LENGTH_SHORT).show();
	}
}
