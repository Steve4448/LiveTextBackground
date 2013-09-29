package steve4448.livetextbackground.preference;

import steve4448.livetextbackground.R;
import steve4448.livetextbackground.widget.MinMaxBar;
import steve4448.livetextbackground.widget.listener.OnMinMaxBarChangeListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class MinMaxPreference extends DialogPreference {
	public String keyMin = null, keyMax = null, unitSuffix = null;
	public int min = 0, max = 0;
	public int absMin = 0, absMax = 100;
	
	public MinMaxPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.minmaxpreference);
		TypedArray extraAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.MinMaxPreference, 0, 0);
		if(extraAttrs.hasValue(R.styleable.MinMaxPreference_minKey))
			keyMin = extraAttrs.getString(R.styleable.MinMaxPreference_minKey);
		if(extraAttrs.hasValue(R.styleable.MinMaxPreference_maxKey))
			keyMax = extraAttrs.getString(R.styleable.MinMaxPreference_maxKey);
		if(extraAttrs.hasValue(R.styleable.MinMaxPreference_unitSuffix))
			unitSuffix = extraAttrs.getString(R.styleable.MinMaxPreference_unitSuffix);
		
		min = extraAttrs.getInteger(R.styleable.MinMaxPreference_minDefault, absMin);
		max = extraAttrs.getInteger(R.styleable.MinMaxPreference_maxDefault, absMax);
		absMin = extraAttrs.getInteger(R.styleable.MinMaxPreference_min, absMin);
		absMax = extraAttrs.getInteger(R.styleable.MinMaxPreference_max, absMax);
		extraAttrs.recycle();
	}
	
	@Override
	public void onBindDialogView(View view) {
		super.onBindDialogView(view);
		final TextView minTextView = (TextView)view.findViewById(R.id.numMin);
		final TextView maxTextView = (TextView)view.findViewById(R.id.numMax);
		final MinMaxBar minMaxBar = (MinMaxBar)view.findViewById(R.id.minMaxBar);
		if(hasKey()) {
			min = getPersistedInt(min);
		}
		min = keyMin != null ? getSharedPreferences().getInt(keyMin, min) : min;
		max = keyMax != null ? getSharedPreferences().getInt(keyMax, max) : max;
		if(keyMax == null) {
			minMaxBar.setSingleThumbMode(true);
			maxTextView.setVisibility(View.INVISIBLE);
		}
		final OnMinMaxBarChangeListener minMaxBarChangeListener = new OnMinMaxBarChangeListener() {
			@Override
			public void onMinValueChanged(int newMin, boolean userCall) {
				min = newMin;
				minTextView.setText(min + (unitSuffix == null ? "" : unitSuffix));
			}
			
			@Override
			public void onMaxValueChanged(int newMax, boolean userCall) {
				max = newMax;
				maxTextView.setText(max + (unitSuffix == null ? "" : unitSuffix));
			}
		};
		minMaxBar.setOnMinMaxBarChangeListener(minMaxBarChangeListener);
		minMaxBar.setMinMaxAbsolutes(absMin, absMax);
		minMaxBar.setMinimum(min);
		minMaxBar.setMaximum(max);
	}
	
	@Override
	public void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if(positiveResult) {
			if(hasKey()) {
				persistInt(min);
			}
			if(keyMin != null || keyMax != null) {
				SharedPreferences.Editor ed = getSharedPreferences().edit();
				{
					if(keyMin != null)
						ed.putInt(keyMin, min);
					if(keyMax != null)
						ed.putInt(keyMax, max);
				}
				ed.commit();
			}
		}
	}
}
