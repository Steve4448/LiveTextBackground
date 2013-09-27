package steve4448.livetextbackground.preference;

import steve4448.livetextbackground.R;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class StringArrayPreference extends DialogPreference {
	public StringArrayPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.stringarraypreference);
	}
	
	@Override
	public void onBindDialogView(final View view) {
		final ScrollView scrollView = (ScrollView)view.findViewById(R.id.scrollView);
		final LinearLayout alterableLayout = (LinearLayout)view.findViewById(R.id.mainLayout);
		final RelativeLayout firstLayoutTemplate = (RelativeLayout)view.findViewById(R.id.wrapper);
		final TextView firstTextEntryTemplate = (TextView)view.findViewById(R.id.textEntry1);
		final Button firstRemoveEntryTemplate = (Button)view.findViewById(R.id.removeEntry1);
		final Button addNewEntryButton = (Button)view.findViewById(R.id.newEntryButton);
		firstRemoveEntryTemplate.setVisibility(View.INVISIBLE);
		addNewEntryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RelativeLayout wrapper = new RelativeLayout(getContext());
				EditText newTextEdit = new EditText(getContext());
				newTextEdit.setInputType(EditorInfo.TYPE_TEXT_FLAG_CAP_WORDS);
				Button newButton = new Button(getContext(), null, android.R.attr.buttonStyleSmall);
				newButton.setText(R.string.label_settings_text_array_entry_remove);
				//newButton.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL | Gravity.TOP);
				RelativeLayout.LayoutParams textViewLayout = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				textViewLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				textViewLayout.addRule(RelativeLayout.LEFT_OF, newButton.getId());
				wrapper.addView(newTextEdit, textViewLayout);
				RelativeLayout.LayoutParams buttonViewLayout = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				buttonViewLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				buttonViewLayout.addRule(RelativeLayout.ALIGN_TOP, newTextEdit.getId());
				buttonViewLayout.addRule(RelativeLayout.ALIGN_BOTTOM, newTextEdit.getId());
				wrapper.addView(newButton, buttonViewLayout);
				alterableLayout.addView(wrapper, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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
								//TODO: Still improperly scrolls, think of a better method.
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
		});
	}
}
