package steve4448.livetextbackground.activity;

import java.io.File;

import steve4448.livetextbackground.R;
import steve4448.livetextbackground.preference.ImageModePreviewPreference;
import steve4448.livetextbackground.util.PreferenceHelper;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.widget.Toast;

public class PreferencesActivity extends PreferenceActivity {
	private Uri currentBackgroundUri;
	private ImageModePreviewPreference imageModePreviewPreference;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(PreferenceHelper.PREFERENCE_NAME);
		addPreferencesFromResource(R.xml.livetextbackground_settings);
		imageModePreviewPreference = (ImageModePreviewPreference) getPreferenceManager().findPreference(getResources().getString(R.string.settings_background_mode));
		imageModePreviewPreference.imageLocation = getSharedPreferences(PreferenceHelper.PREFERENCE_NAME, MODE_PRIVATE).getString(getResources().getString(R.string.settings_background_image), null);
		getPreferenceManager().findPreference(getResources().getString(R.string.settings_background_image)).setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				try {
					currentBackgroundUri = getBackgroundUri();
					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
					photoPickerIntent.setType("image/*");
					photoPickerIntent.putExtra("crop", "true");
					photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentBackgroundUri);
					photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
					startActivityForResult(Intent.createChooser(photoPickerIntent, getResources().getString(R.string.label_settins_select_an_image)), PreferenceHelper.PHOTO_PICKER_REQUEST_CODE);
				} catch(ActivityNotFoundException e) {
					e.printStackTrace();
					Toast.makeText(getBaseContext(), R.string.toast_no_image_picker_activity_found, Toast.LENGTH_LONG).show();
				}
				return true;
			}
		});
	}
	
	private static Uri getBackgroundUri() {
		return Uri.fromFile(getBackgroundFile());
	}
	
	private static File getBackgroundFile() {
		try {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				System.out.println(Environment.getExternalStorageDirectory().getAbsolutePath());
				File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + PreferenceHelper.LIVE_TEXT_BACKGROUND_PATH);
				System.out.println(directory.getAbsolutePath());
				directory.mkdirs();
				File file = new File(directory, "LiveTextBackground-" + System.currentTimeMillis() + ".png");
				System.out.println(file.getAbsolutePath());
				file.createNewFile();
				return file;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == PreferenceHelper.PHOTO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
			try {
				String tmpUri = currentBackgroundUri.toString();
				getSharedPreferences(PreferenceHelper.PREFERENCE_NAME, MODE_PRIVATE).edit().putString(getResources().getString(R.string.settings_background_image), tmpUri).commit();
				imageModePreviewPreference.imageLocation = tmpUri;
			} catch(Exception e) {
				e.printStackTrace();
				Toast.makeText(getBaseContext(), R.string.toast_could_not_load_image_picker_data, Toast.LENGTH_LONG).show();
			}
		}
	}
}