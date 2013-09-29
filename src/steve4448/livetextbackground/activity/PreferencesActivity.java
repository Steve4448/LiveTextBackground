package steve4448.livetextbackground.activity;

import steve4448.livetextbackground.R;
import steve4448.livetextbackground.util.PreferenceHelper;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.widget.Toast;

public class PreferencesActivity extends PreferenceActivity {
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(PreferenceHelper.PREFERENCE_NAME);
		addPreferencesFromResource(R.xml.livetextbackground_settings);
		getPreferenceManager().findPreference(getResources().getString(R.string.settings_background_image)).setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				try {
					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK); 
					photoPickerIntent.setType("image/*");
			        startActivityForResult(Intent.createChooser(photoPickerIntent, getResources().getString(R.string.label_settins_select_an_image)), PreferenceHelper.PHOTO_PICKER_REQUEST_CODE);
				} catch(ActivityNotFoundException e) {
					e.printStackTrace();
					Toast.makeText(getBaseContext(), R.string.toast_no_image_picker_activity_found, Toast.LENGTH_LONG).show();
				}
				return true;
			}
		});
	}
	
	@SuppressWarnings("deprecation")
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == PreferenceHelper.PHOTO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
			try {
				Uri obtainedData = (Uri)data.getData();
				String[] projection = { MediaStore.Images.Media.DATA };
		        Cursor cursor = managedQuery(obtainedData, projection, null, null, null);
		        cursor.moveToFirst();
		        getSharedPreferences(PreferenceHelper.PREFERENCE_NAME, MODE_PRIVATE).edit().putString("settings_background_image", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))).commit();
			} catch(Exception e) {
				e.printStackTrace();
				Toast.makeText(getBaseContext(), R.string.toast_could_not_load_image_picker_data, Toast.LENGTH_LONG).show();
			}
		}
	}
}