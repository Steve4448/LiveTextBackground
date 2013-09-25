package steve4448.livetextbackground.activity;

import steve4448.livetextbackground.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferencesActivity extends PreferenceActivity {
	public static final String PREFERENCE_NAME = "livetextbackground_settings";
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(PREFERENCE_NAME);
		addPreferencesFromResource(R.xml.livetextbackground_settings);
	}
}