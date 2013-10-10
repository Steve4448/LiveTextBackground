package steve4448.livetextbackground.util;

import steve4448.livetextbackground.R;
import steve4448.livetextbackground.widget.ImageModePreview;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.widget.Toast;

public class PreferenceHelper {
	public static final String LIVE_TEXT_BACKGROUND_PATH = "LiveTextBackground";
	public static final String BACKGROUND_FILE = "The Background.png";
	public static final String PREFERENCE_NAME = "livetextbackground_settings";
	public static final int PHOTO_PICKER_REQUEST_CODE = 1;
	private static SharedPreferences actualPrefs;
	private static OnSharedPreferenceChangeListener changeListener;
	private Context context;
	public int width = 0;
	public int height = 0;
	public int textSizeMin;
	public int textSizeMax;
	public String[] availableStrings;
	public boolean collisionEnabled;
	public boolean applyShadow;
	public int desiredFPS;
	public Rect imageRect;
	public Rect backgroundRect;
	public boolean backgroundImageEnabled;
	public Bitmap backgroundImage;
	public String backgroundMode;
	public boolean tried = false;
	
	public PreferenceHelper(final Context context) {
		imageRect = new Rect();
		backgroundRect = new Rect();
		this.context = context;
		if(actualPrefs != null) {
			actualPrefs.unregisterOnSharedPreferenceChangeListener(changeListener);
		} else {
			actualPrefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
			changeListener = new OnSharedPreferenceChangeListener() {
				@Override
				public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
					actualPrefs.unregisterOnSharedPreferenceChangeListener(this);
					System.out.println("Key Changed: " + key);
					loadSettings(key);
					actualPrefs.registerOnSharedPreferenceChangeListener(this);
				}
			};
		}
		loadSettings(null);
		actualPrefs.registerOnSharedPreferenceChangeListener(changeListener);
	}
	
	public boolean loadSettings(String key) {
		try {
			Resources r = context.getResources();
			if(key == null || key == r.getString(R.string.settings_text_size_variance_min))
				try {
					textSizeMin = actualPrefs.getInt(r.getString(R.string.settings_text_size_variance_min), r.getInteger(R.integer.label_settings_text_size_default_min));
				} catch(ClassCastException e) {
					textSizeMin = r.getInteger(R.integer.label_settings_text_size_default_min);
					actualPrefs.edit().putInt(r.getString(R.string.settings_text_size_variance_min), textSizeMin).commit();
				}
			
			if(key == null || key == r.getString(R.string.settings_text_size_variance_max))
				try {
					textSizeMax = actualPrefs.getInt(r.getString(R.string.settings_text_size_variance_max), r.getInteger(R.integer.label_settings_text_size_default_max));
				} catch(ClassCastException e) {
					textSizeMax = r.getInteger(R.integer.label_settings_text_size_default_max);
					actualPrefs.edit().putInt(r.getString(R.string.settings_text_size_variance_max), textSizeMax).commit();
				}
			if(key == null || key.startsWith(r.getString(R.string.settings_text))) {
				String[] defaultStrings = r.getString(R.string.label_settings_text_default).split("\\|");
				try {
					int prefsStrings = actualPrefs.getInt(r.getString(R.string.settings_text), -1);
					if(prefsStrings > 0) {
						availableStrings = new String[prefsStrings];
						for(int i = 0; i < availableStrings.length; i++) {
							availableStrings[i] = actualPrefs.getString(r.getString(R.string.settings_text) + i, null);
						}
					} else {
						throw new Exception("Invalid amount of strings.");
					}
				} catch(Exception e) {
					availableStrings = defaultStrings;
					actualPrefs.edit().putString(r.getString(R.string.settings_text), r.getString(R.string.label_settings_text_default)).commit();
				}
				
				if(availableStrings.length == 0) {
					throw new Exception("Zero available strings at this point..?");
				}
			}
			
			if(key == null || key == r.getString(R.string.settings_collision))
				try {
					collisionEnabled = actualPrefs.getBoolean(r.getString(R.string.settings_collision), r.getBoolean(R.bool.label_settings_collision_default));
				} catch(ClassCastException e) {
					collisionEnabled = r.getBoolean(R.bool.label_settings_collision_default);
					actualPrefs.edit().putBoolean(r.getString(R.string.settings_collision), r.getBoolean(R.bool.label_settings_collision_default)).commit();
				}
			
			if(key == null || key == r.getString(R.string.settings_shadow_layer))
				try {
					applyShadow = actualPrefs.getBoolean(r.getString(R.string.settings_shadow_layer), r.getBoolean(R.bool.label_settings_shadow_layer_default));
				} catch(ClassCastException e) {
					applyShadow = r.getBoolean(R.bool.label_settings_shadow_layer_default);
					actualPrefs.edit().putBoolean(r.getString(R.string.settings_shadow_layer), r.getBoolean(R.bool.label_settings_shadow_layer_default)).commit();
				}
			
			if(key == null || key == r.getString(R.string.settings_desired_fps))
				try {
					desiredFPS = 1000 / actualPrefs.getInt(r.getString(R.string.settings_desired_fps), r.getInteger(R.integer.label_settings_desired_fps_default));
				} catch(ClassCastException e) {
					desiredFPS = 1000 / r.getInteger(R.integer.label_settings_desired_fps_default);
					actualPrefs.edit().putInt(r.getString(R.string.settings_desired_fps), r.getInteger(R.integer.label_settings_desired_fps_default)).commit();
				}
			
			if(key == null || key == r.getString(R.string.settings_enable_background_image)) {
				try {
					backgroundImageEnabled = actualPrefs.getBoolean(r.getString(R.string.settings_enable_background_image), r.getBoolean(R.bool.label_settings_background_enabled));
				} catch(ClassCastException e) {
					backgroundImageEnabled = r.getBoolean(R.bool.label_settings_background_enabled);
					actualPrefs.edit().putBoolean(r.getString(R.string.settings_enable_background_image), r.getBoolean(R.bool.label_settings_background_enabled)).commit();
				}
			}
			if((key == null && backgroundImageEnabled) || (key == r.getString(R.string.settings_background_image) && backgroundImageEnabled)) {
				if(backgroundImage != null)
					backgroundImage.recycle();
				try {
					String uri = actualPrefs.getString(r.getString(R.string.settings_background_image), null);
					if(uri != null) {
						backgroundImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(uri));
					}
				} catch(Exception e) {
					e.printStackTrace();
					backgroundImage = null;
					Toast.makeText(context, R.string.toast_could_not_load_image_picker_data, Toast.LENGTH_SHORT).show();
				}
			}
			if(key == null || key == r.getString(R.string.settings_background_mode)) {
				try {
					backgroundMode = actualPrefs.getString(r.getString(R.string.settings_background_mode), r.getString(R.string.label_settings_background_image_modes_default));
				} catch(ClassCastException e) {
					backgroundMode = r.getString(R.string.label_settings_background_image_modes_default);
					actualPrefs.edit().putString(r.getString(R.string.settings_background_mode), r.getString(R.string.label_settings_background_image_modes_default)).commit();
				}
			}
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			PreferenceManager.setDefaultValues(context, PREFERENCE_NAME, Context.MODE_PRIVATE, R.xml.livetextbackground_settings, true);
			Toast.makeText(context, R.string.toast_error_loading_preferences, Toast.LENGTH_LONG).show();
			if(!tried) {
				tried = true;
				boolean fixed = loadSettings(key);
				if(!fixed)
					Toast.makeText(context, R.string.toast_fatal_error_loading_preferences, Toast.LENGTH_LONG).show();
				return fixed;
			} else
				return false;
		}
	}
	
    public void doResize(int width, int height) {
    	this.width = width;
    	this.height = height;
		if(backgroundImage != null) {
			imageRect.set(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
			backgroundRect.set(0, 0, width, height);
			ImageModePreview.getRectsBasedOffMode(backgroundMode, imageRect, backgroundRect);
		}
	}
}
