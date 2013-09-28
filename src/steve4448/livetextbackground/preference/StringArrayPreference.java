package steve4448.livetextbackground.preference;

import steve4448.livetextbackground.R;
import steve4448.livetextbackground.util.ViewHelper;
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
	private boolean triedToLoad = false;
	
	public StringArrayPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.stringarraypreference);
	}
	
	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		if(restorePersistedValue) {
			// Should already be persisted, don't bother...
		} else {
			String d = null;
			try {
				d = getSharedPreferences().getString(getKey(), (String)defaultValue);
			} catch(Exception e) {}
			
			saveEntries((d != null ? d : (String)defaultValue).split("\\|"));
		}
	}
	
	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getString(index);
	}
	
	@Override
	public void onBindDialogView(final View view) {
		scrollView = (ScrollView)view.findViewById(R.id.scrollView);
		alterableLayout = (LinearLayout)view.findViewById(R.id.mainLayout);
		
		addNewEntryButton = (Button)view.findViewById(R.id.newEntryButton);
		addNewEntryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				addNewEntry();
				attemptEnableFirst(true);
				scrollView.post(new Runnable() {
					@Override
					public void run() {
						scrollView.fullScroll(View.FOCUS_DOWN);
					}
				});
			}
		});
		loadEntries();
		super.onBindDialogView(view);
	}
	
	public void addNewEntry() {
		addNewEntry(null);
	}
	
	public void addNewEntry(String initText) {
		if(scrollView == null)
			return;
		RelativeLayout wrapper = new RelativeLayout(getContext());
		final EditText newTextEdit = new EditText(getContext());
		newTextEdit.setId(ViewHelper.findUnusedId(wrapper));
		newTextEdit.setInputType(EditorInfo.TYPE_TEXT_FLAG_CAP_WORDS);
		newTextEdit.post(new Runnable() {
			@Override
			public void run() {
				newTextEdit.requestFocus();
			}
		});
		if(initText != null)
			newTextEdit.setText(initText);
		Button newButton = new Button(getContext(), null, android.R.attr.buttonStyleSmall);
		newButton.setId(ViewHelper.findUnusedId(wrapper));
		newButton.setText(R.string.label_settings_text_array_entry_remove);
		
		RelativeLayout.LayoutParams textEditLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		textEditLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		textEditLayout.addRule(RelativeLayout.LEFT_OF, newButton.getId());

		RelativeLayout.LayoutParams buttonViewLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		buttonViewLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		buttonViewLayout.addRule(RelativeLayout.ALIGN_TOP, newTextEdit.getId());
		buttonViewLayout.addRule(RelativeLayout.ALIGN_BOTTOM, newTextEdit.getId());
		
		newTextEdit.setLayoutParams(textEditLayout);
		newButton.setLayoutParams(buttonViewLayout);
		
		wrapper.addView(newTextEdit);
		wrapper.addView(newButton);
		
		alterableLayout.addView(wrapper, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

		addNewEntryButton.bringToFront();
		
		newButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alterableLayout.removeView((View)v.getParent());
				attemptEnableFirst(false);
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
			saveEntries(s);
		}
		scrollView = null;
	}
	
	public void loadEntries() {
		System.out.print("Loading entries...");
		try {
			int amt = getSharedPreferences().getInt(getKey(), -1);
			System.out.print(" " + amt + " found:");
			for(int i = 0; i < amt; i++) {
				System.out.print((i == 0 ? " " : ", ") + getSharedPreferences().getString(getKey() + i, null));
				addNewEntry(getSharedPreferences().getString(getKey() + i, null));
			}
			System.out.println(" Done.");
		} catch(ClassCastException e) {
			System.out.println(" ClassCastException, attempting to read as a string...");
			String[] str = getSharedPreferences().getString(getKey(), null).split("\\|");
			saveEntries(str);
			if(!triedToLoad) {
				triedToLoad = true;
				loadEntries();
			}
			triedToLoad = false;
		}
		attemptEnableFirst(false);
	}
	
	public void saveEntries(String[] entries) {
		System.out.print("Saving entries... " + entries.length + " total:");
		SharedPreferences.Editor ed = getSharedPreferences().edit();
		{
			// Would use putStringSet, but that exists in API >= 11, going for API 8.
			// Could also just use persistString with a separator, but this seems much more dynamic in the long-run with seemingly little to no potential to fail.
			ed.putInt(getKey(), entries.length);
			for(int i = 0; i < entries.length; i++) {
				System.out.print((i == 0 ? " " : ", ") + entries[i]);
				ed.putString(getKey() + i, entries[i]);
			}
		}
		ed.commit();
		System.out.println(" Done.");
	}
	
	public void attemptEnableFirst(boolean enable) {
		if(enable ? alterableLayout.getChildCount() > 2 : alterableLayout.getChildCount() == 2) {
			Button b = ((Button)((RelativeLayout)alterableLayout.getChildAt(0)).getChildAt(1));
			b.setEnabled(enable);
		}
	}
}
