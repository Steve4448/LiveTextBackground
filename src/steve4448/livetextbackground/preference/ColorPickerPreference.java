package steve4448.livetextbackground.preference;

import steve4448.livetextbackground.R;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

public class ColorPickerPreference extends DialogPreference {
	public ColorPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.colorpickerpreference);
	}
	
	@Override
	public void onBindDialogView(View view) {
		super.onBindDialogView(view);
		Toast.makeText(getContext(), R.string.placeholder_not_yet_implemented, Toast.LENGTH_SHORT).show();
	}
}
