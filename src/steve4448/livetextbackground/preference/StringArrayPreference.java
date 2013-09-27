package steve4448.livetextbackground.preference;

import steve4448.livetextbackground.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class StringArrayPreference extends DialogPreference {
	private ScrollView scrollView;
	private LinearLayout alterableLayout;
	private Button addNewEntryButton;
	
	public StringArrayPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.stringarraypreference);
	}
	
	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		if(restorePersistedValue) {
			//Should already be persisted, don't bother...
		} else {
			String[] vals = getSharedPreferences().getString(getKey(), (String)defaultValue).split("\\|");
			SharedPreferences.Editor ed = getSharedPreferences().edit();
			{
				ed.putInt(getKey(), vals.length);
				for(int i = 0; i < vals.length; i++) {
					ed.putString(getKey() + i, vals[i]);
				}
			}
			ed.commit();
		}
	}
	
	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
	    return a.getString(index);
	}
	
	private void loadDefaultValues() {
		boolean exists = getSharedPreferences().contains(getKey());
		if(exists) {
			if(scrollView != null) {
				int amt = getSharedPreferences().getInt(getKey(), -1);
				for(int i = 0; i < amt; i++) {
					addNewEntry(getSharedPreferences().getString(getKey() + i, null));
				}
			}
		}
	}
	
	@Override
	public void onBindDialogView(final View view) {
		scrollView = (ScrollView)view.findViewById(R.id.scrollView);
		alterableLayout = (LinearLayout)view.findViewById(R.id.mainLayout);
		addNewEntryButton = (Button)view.findViewById(R.id.newEntryButton);
		addNewEntryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addNewEntry();
			}
		});
		loadDefaultValues();
		super.onBindDialogView(view);
	}
	
	public void addNewEntry() {
		addNewEntry(null);
	}
	
	public void addNewEntry(String initText) {
		RelativeLayout wrapper = new RelativeLayout(getContext());
		EditText newTextEdit = new EditText(getContext());
		newTextEdit.setInputType(EditorInfo.TYPE_TEXT_FLAG_CAP_WORDS);
		if(initText != null)
			newTextEdit.setText(initText);
		Button newButton = new Button(getContext(), null, android.R.attr.buttonStyleSmall);
		newButton.setText(R.string.label_settings_text_array_entry_remove);
		// newButton.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL | Gravity.TOP);
		
		RelativeLayout.LayoutParams textViewLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		textViewLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		textViewLayout.addRule(RelativeLayout.LEFT_OF, newButton.getId());
		wrapper.addView(newTextEdit, textViewLayout);
		
		RelativeLayout.LayoutParams buttonViewLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		buttonViewLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		buttonViewLayout.addRule(RelativeLayout.ALIGN_TOP, newTextEdit.getId());
		buttonViewLayout.addRule(RelativeLayout.ALIGN_BOTTOM, newTextEdit.getId());
		wrapper.addView(newButton, buttonViewLayout);
		alterableLayout.addView(wrapper, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		
		//TODO: Is there a better way to make it so the "New Entry" button can be kept at the bottom without re-adding?
		alterableLayout.removeView(addNewEntryButton);
		alterableLayout.addView(addNewEntryButton);
		newButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final View viewParent = (View)v.getParent();
				final int beforeRemovalX = scrollView.getScrollX();
				final int beforeRemovalY = scrollView.getScrollY();
				scrollView.post(new Runnable() {
					@Override
					public void run() {
						// TODO: Still improperly scrolls, think of a better method.
						scrollView.scrollTo(beforeRemovalX, beforeRemovalY - viewParent.getHeight());
					}
				});
				alterableLayout.removeView(viewParent);
			}
		});
		
		scrollView.post(new Runnable() {
			@Override
			public void run() {
				scrollView.fullScroll(View.FOCUS_DOWN);
			}
		});
	}
	
	@Override
	public void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if(positiveResult && hasKey()) {
			String[] s = new String[alterableLayout.getChildCount() - 1];
			for(int i = 0; i < s.length; i++) {
				s[i] = ((EditText)((RelativeLayout)alterableLayout.getChildAt(i)).getChildAt(0)).getText().toString();
			}
			//Would use putStringSet, but that exists in API >= 11, going for API 8.
			//Could also just use persistString with a separator, but this seems much more dynamic in the long-run with seemingly little to no potential to fail.
			SharedPreferences.Editor ed = getSharedPreferences().edit();
			{
				ed.putInt(getKey(), s.length);
				for(int i = 0; i < s.length; i++) {
					ed.putString(getKey() + i, s[i]);
				}
			}
			ed.commit();
		}
		scrollView = null;
	}
	
}
