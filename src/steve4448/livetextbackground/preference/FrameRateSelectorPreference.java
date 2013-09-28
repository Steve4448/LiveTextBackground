package steve4448.livetextbackground.preference;

import steve4448.livetextbackground.R;
import steve4448.livetextbackground.widget.MinMaxBar;
import steve4448.livetextbackground.widget.listener.OnMinMaxBarChangeListener;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class FrameRateSelectorPreference extends DialogPreference {
	private int min = 0;
	
	public FrameRateSelectorPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.frameratepreference);
	}
	
	@Override
	public void onBindDialogView(View view) {
		final TextView minTextView = (TextView)view.findViewById(R.id.minFPS);
		final MinMaxBar minMaxBar = (MinMaxBar)view.findViewById(R.id.fpsBar);
		final OnMinMaxBarChangeListener minMaxBarChangeListener = new OnMinMaxBarChangeListener() {
			@Override
			public void onMinValueChanged(int newMin, boolean userCall) {
				min = newMin;
				minTextView.setText(min + " FPS");
			}

			@Override
            public void onMaxValueChanged(int newMax, boolean userCall) {}
		};
		minMaxBar.setOnMinMaxBarChangeListener(minMaxBarChangeListener);
		minMaxBar.setMinimum(getPersistedInt((int)minMaxBar.getMinimum()));
		super.onBindDialogView(view);
	}
	
	@Override
	public void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if(positiveResult)
			persistInt(min);
	}
}
