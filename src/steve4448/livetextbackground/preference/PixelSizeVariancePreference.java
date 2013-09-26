package steve4448.livetextbackground.preference;

import steve4448.livetextbackground.R;
import steve4448.livetextbackground.widget.MinMaxBar;
import steve4448.livetextbackground.widget.listener.OnMinMaxBarChangeListener;
import android.content.Context;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class PixelSizeVariancePreference extends DialogPreference {
	private Context context = null;
	private int min = 0, max = 0;
	private boolean exists = false;
	
	public PixelSizeVariancePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		actualInit(context, attrs);
	}
	
	public PixelSizeVariancePreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		actualInit(context, attrs);
	}
	
	public void actualInit(Context context, AttributeSet attrs) {
		this.context = context;
		setDialogLayoutResource(R.layout.pixelsizevariancepreference);
	}
	
	@Override
	public void onBindDialogView(View view) {
		super.onBindDialogView(view);
		exists = PreferenceManager.getDefaultSharedPreferences(context).contains("minimum");
		if(exists) {
			min = PreferenceManager.getDefaultSharedPreferences(context).getInt("minimum", -1);
			max = PreferenceManager.getDefaultSharedPreferences(context).getInt("maximum", -1);
		}
		final TextView minTextView = (TextView)view.findViewById(R.id.numMin);
		final TextView maxTextView = (TextView)view.findViewById(R.id.numMax);
		final TextView minPreviewTextView = (TextView)view.findViewById(R.id.minPreview);
		final TextView maxPreviewTextView = (TextView)view.findViewById(R.id.maxPreview);
		final MinMaxBar minMaxBar = (MinMaxBar)view.findViewById(R.id.minMaxBar);
		final OnMinMaxBarChangeListener minMaxBarChangeListener = new OnMinMaxBarChangeListener() {
			@Override
			
			public void onMinValueChanged(int newMin, boolean userCall) {
				min = newMin;
				minTextView.setText(min + "px");
				minPreviewTextView.setTextSize(min);
			}
			
			@Override
			public void onMaxValueChanged(int newMax, boolean userCall) {
				max = newMax;
				maxTextView.setText(max + "px");
				maxPreviewTextView.setTextSize(max);
			}
		};
		minMaxBar.setOnMinMaxBarChangeListener(minMaxBarChangeListener);
		if(exists) {
			minMaxBar.setMinimum(min);
			minMaxBar.setMaximum(max);
		} else {
			min = (int)minMaxBar.getMinimum();
			max = (int)minMaxBar.getMaximum();
		}
		minTextView.setText(min + "px");
		maxTextView.setText(max + "px");
		minPreviewTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, min);
		minPreviewTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, max);
		float tmpScreenDenisty = getContext().getResources().getDisplayMetrics().density;
		minPreviewTextView.setHeight((int)(tmpScreenDenisty * minMaxBar.getAbsoluteMaximum() + minMaxBar.getAbsoluteMaximum() * 0.2));
		maxPreviewTextView.setHeight((int)(tmpScreenDenisty * minMaxBar.getAbsoluteMaximum() + minMaxBar.getAbsoluteMaximum() * 0.2));
	}
	
	@Override
	public void onDialogClosed(boolean shouldSave) {
		if(shouldSave)
			PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("minimum", min).putInt("maximum", max).commit();
	}
}
