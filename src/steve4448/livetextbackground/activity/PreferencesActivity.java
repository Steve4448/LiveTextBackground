package steve4448.livetextbackground.activity;

import steve4448.livetextbackground.R;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.widget.Toast;

public class PreferencesActivity extends PreferenceActivity {
	public static final String PREFERENCE_NAME = "livetextbackground_settings";
	public static final int PHOTO_PICKER_REQUEST_CODE = 1;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(PREFERENCE_NAME);
		addPreferencesFromResource(R.xml.livetextbackground_settings);
		getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE).registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
				System.out.println("Should reload settings now...");
			}
		});
		getPreferenceManager().findPreference("settings_background_image").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				try {
					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK); 
					photoPickerIntent.setType("image/*");
			        startActivityForResult(Intent.createChooser(photoPickerIntent, "Select An Image"), PHOTO_PICKER_REQUEST_CODE);
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
		if(requestCode == PHOTO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
			try {
				Uri obtainedData = (Uri)data.getData();
				String[] projection = { MediaStore.Images.Media.DATA };
		        Cursor cursor = managedQuery(obtainedData, projection, null, null, null);
		        cursor.moveToFirst();
		        getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE).edit().putString("settings_background_image", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))).commit();
			} catch(Exception e) {
				e.printStackTrace();
				Toast.makeText(getBaseContext(), R.string.toast_could_not_load_image_picker_data, Toast.LENGTH_LONG).show();
			}
		}
	}
}