package steve4448.livetextbackground.preference;

import steve4448.livetextbackground.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class ColorPickerPreference extends DialogPreference {
	public ColorPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ColorPickerPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void actualInit(Context context, AttributeSet attrs) {
		TypedArray extraAttrs = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerPreference);
		extraAttrs.recycle();
	}
}
