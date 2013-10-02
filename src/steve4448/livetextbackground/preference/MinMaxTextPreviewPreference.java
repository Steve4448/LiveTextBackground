package steve4448.livetextbackground.preference;

import steve4448.livetextbackground.R;
import steve4448.livetextbackground.widget.MinMaxBar;
import steve4448.livetextbackground.widget.listener.OnMinMaxBarChangeListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class MinMaxTextPreviewPreference extends MinMaxPreference {
	
	public MinMaxTextPreviewPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.minmaxtextpreviewpreference);
	}
	
	@Override
	public void onBindDialogView(View view) {
		super.onBindDialogView(view);
		final TextView minTextView = (TextView) view.findViewById(R.id.numMin);
		final TextView maxTextView = (TextView) view.findViewById(R.id.numMax);
		final TextView minPreviewTextView = (TextView) view.findViewById(R.id.minPreview);
		final TextView maxPreviewTextView = (TextView) view.findViewById(R.id.maxPreview);
		final MinMaxBar minMaxBar = (MinMaxBar) view.findViewById(R.id.minMaxBar);
		final OnMinMaxBarChangeListener minMaxBarChangeListener = new OnMinMaxBarChangeListener() {
			@Override
			public void onMinValueChanged(int newMin, boolean userCall) {
				min = newMin;
				minTextView.setText(min + (unitSuffix == null ? "" : unitSuffix));
				minPreviewTextView.setTextSize(minMaxBar.getMinimum());
			}
			
			@Override
			public void onMaxValueChanged(int newMax, boolean userCall) {
				max = newMax;
				maxTextView.setText(max + (unitSuffix == null ? "" : unitSuffix));
				maxPreviewTextView.setTextSize(minMaxBar.getMaximum());
			}
		};
		minMaxBar.setOnMinMaxBarChangeListener(minMaxBarChangeListener);
		minPreviewTextView.setTextSize(minMaxBar.getMinimum());
		maxPreviewTextView.setTextSize(minMaxBar.getMaximum());
		float tmpScreenDenisty = getContext().getResources().getDisplayMetrics().density;
		minPreviewTextView.setHeight((int) (tmpScreenDenisty * minMaxBar.getAbsoluteMaximum() + minMaxBar.getAbsoluteMaximum() * 0.2));
		maxPreviewTextView.setHeight((int) (tmpScreenDenisty * minMaxBar.getAbsoluteMaximum() + minMaxBar.getAbsoluteMaximum() * 0.2));
	}
}
