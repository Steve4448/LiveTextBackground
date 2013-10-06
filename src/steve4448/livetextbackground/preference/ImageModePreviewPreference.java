package steve4448.livetextbackground.preference;

import steve4448.livetextbackground.R;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class ImageModePreviewPreference extends DialogPreference {
	public ImageModePreviewPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.imagemodepreviewpreference);
	}
}
