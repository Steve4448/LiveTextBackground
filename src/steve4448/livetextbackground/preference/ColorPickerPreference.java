package steve4448.livetextbackground.preference;

import steve4448.livetextbackground.R;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class ColorPickerPreference extends DialogPreference {
	public ColorPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.colorpickerpreference);
	}
}
