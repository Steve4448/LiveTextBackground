package steve4448.livetextbackground.background;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import steve4448.livetextbackground.R;
import steve4448.livetextbackground.activity.PreferencesActivity;
import steve4448.livetextbackground.background.object.ExplosionParticle;
import steve4448.livetextbackground.background.object.ExplosionParticleGroup;
import steve4448.livetextbackground.background.object.TextObject;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class LiveTextBackgroundService extends WallpaperService {
	
	@Override
	public Engine onCreateEngine() {
		return new LiveTextBackgroundEngine();
	}
	
	private class LiveTextBackgroundEngine extends Engine {
		private Paint paintText;
		private CopyOnWriteArrayList<TextObject> textObj = new CopyOnWriteArrayList<TextObject>();
		private CopyOnWriteArrayList<ExplosionParticleGroup> textExplObj = new CopyOnWriteArrayList<ExplosionParticleGroup>();
		private Timer logicTimer;
		private TimerTask logicTimerTask;
		private final Handler paintHandler = new Handler();
		private final Runnable paintRunnable = new Runnable() {
			@Override
			public void run() {
				draw();
			}
		};
		private boolean visible = false;
		
		private String[] availableStrings;
		private boolean collisionEnabled;
		private int desiredFPS;
		private int textSizeMin, textSizeMax;
		private boolean tried = false;
		private boolean hasShadow = false;
		private boolean applyShadow = false;
		
		private LiveTextBackgroundEngine() {
			paintText = new Paint();
			paintText.setAntiAlias(true);
			paintText.setTypeface(Typeface.SANS_SERIF);
			paintText.setStyle(Paint.Style.FILL);
		}
		
		@Override
		public void onCreate(SurfaceHolder holder) {
			super.onCreate(holder);
		}
		
		@Override
		public void onDestroy() {
			super.onDestroy();
			setupLogicHandler(false);
		}
		
		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
		}
		
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			this.visible = false;
			setupLogicHandler(false);
		}
		
		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			setupLogicHandler(true);
		}
		
		@Override
		public void onVisibilityChanged(boolean visible) {
			this.visible = visible;
			setupLogicHandler(visible);
		}
		
		private boolean loadSettings() {
			try {
				SharedPreferences prefs = getSharedPreferences(PreferencesActivity.PREFERENCE_NAME, MODE_PRIVATE);
				textSizeMin = prefs.getInt("settings_text_size_variance_min", getResources().getInteger(R.integer.label_settings_text_size_default_min));
				textSizeMax = prefs.getInt("settings_text_size_variance_max", getResources().getInteger(R.integer.label_settings_text_size_default_max));

				String[] defaultStrings = getResources().getString(R.string.label_settings_text_default).split("\\|");
				try {
					int prefsStrings = prefs.getInt("settings_text", -1);
					if(prefsStrings > 0) {
						availableStrings = new String[prefsStrings];
						for(int i = 0; i < availableStrings.length; i++) {
							availableStrings[i] = prefs.getString("settings_text" + i, null);
						}
					} else {
						throw new Exception("Invalid amount of strings.");
					}
				} catch(Exception e) {
					prefs.edit().putString("settings_text", getResources().getString(R.string.label_settings_text_default)).commit();
					availableStrings = defaultStrings;
				}
				
				if(availableStrings.length == 0) {
					throw new Exception("Zero available strings at this point..?");
				}
				
				collisionEnabled = prefs.getBoolean("settings_collision", getResources().getBoolean(R.bool.label_settings_collision_default));
				applyShadow = prefs.getBoolean("settings_shadow_layer", getResources().getBoolean(R.bool.label_settings_shadow_layer_default));
				
				try {
					desiredFPS = 1000 / prefs.getInt("settings_desired_fps", getResources().getInteger(R.integer.label_settings_desired_fps_default));
				} catch(Exception e) {
					e.printStackTrace();
					Toast.makeText(getBaseContext(), R.string.toast_error_parsing_desired_fps, Toast.LENGTH_LONG).show();
					prefs.edit().putInt("settings_desired_fps", getResources().getInteger(R.integer.label_settings_desired_fps_default)).commit();
					desiredFPS = 1000 / getResources().getInteger(R.integer.label_settings_desired_fps_default);
				}
				return true;
			} catch(Exception e) {
				e.printStackTrace();
				PreferenceManager.setDefaultValues(getBaseContext(), PreferencesActivity.PREFERENCE_NAME, MODE_PRIVATE, R.xml.livetextbackground_settings, true);
				Toast.makeText(getBaseContext(), R.string.toast_error_loading_preferences, Toast.LENGTH_LONG).show();
				if(!tried) {
					tried = true;
					boolean fixed = loadSettings();
					return (tried = fixed);
				} else
					return false;
			}
		}
		
		private void setupLogicHandler(boolean on) {
			if(on) {
				if(logicTimer != null)
					setupLogicHandler(false);
				
				if(!loadSettings()) {
					Toast.makeText(getBaseContext(), R.string.toast_fatal_error_loading_preferences, Toast.LENGTH_LONG).show();
					return;
				}
				
				logicTimer = new Timer();
				logicTimer.schedule(logicTimerTask = new TimerTask() {
					@Override
					public void run() {
						logic();
					}
				}, desiredFPS, desiredFPS);
				paintHandler.post(paintRunnable);
			} else {
				if(logicTimer != null) {
					if(logicTimerTask != null) {
						logicTimerTask.cancel();
						logicTimerTask = null;
					}
					logicTimer.cancel();
					logicTimer.purge();
					logicTimer = null;
				}
				paintHandler.removeCallbacks(paintRunnable);
			}
		}
		
		private void logic() {
			// long startTime = System.currentTimeMillis();
			if((int)(Math.random() * 10) == 1) {
				String text = availableStrings[(int)(Math.random() * availableStrings.length)];
				int size = (int)(textSizeMin + (Math.random() * (textSizeMax - textSizeMin)));
				int col = Color.argb(155 + (int)(Math.random() * 100), (int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
				Rect bounds = new Rect();
				paintText.setColor(col);
				paintText.setTextSize(size);
				paintText.getTextBounds(text, 0, text.length(), bounds);
				float x = (float)(Math.random() * getDesiredMinimumWidth());
				TextObject newObj = new TextObject(paintText, text, new RectF(x, 0, x + bounds.width(), bounds.height()), size, col);
				textObj.add(newObj);
				bounds = null;
			}
			for(TextObject t : textObj) {
				// Text Object Logic
				if(t.velocityX > 0) {
					t.velocityX -= 0.01;
					if(t.velocityX < 0)
						t.velocityX = 0;
				}
				if(t.velocityX < 0) {
					t.velocityX += 0.01;
					if(t.velocityX > 0)
						t.velocityX = 0;
				}
				t.velocityY += 0.01;
				t.dimen.offset(t.velocityX, t.velocityY);
				if(t.dimen.top > getDesiredMinimumHeight() || t.dimen.left > getDesiredMinimumWidth() || t.dimen.right < 0) {
					textObj.remove(t);
					continue;
				}
				if(collisionEnabled) {
					for(TextObject t2 : textObj) {
						if(t2 == t)
							continue;
						if(t.dimen.intersect(t2.dimen)) {
							textExplObj.add(new ExplosionParticleGroup(t.dimen.left, t.dimen.top, t.dimen.width(), t.dimen.height(), t.color));
							textExplObj.add(new ExplosionParticleGroup(t.dimen.left, t.dimen.top, t.dimen.width(), t.dimen.height(), t2.color));
							textObj.remove(t);
							textObj.remove(t2);
							break;
						}
					}
				}
			}
			for(ExplosionParticleGroup p : textExplObj) {
				for(int i = 0; i < p.arr.length; i++) {
					ExplosionParticle p2 = p.arr[i];
					if(p2.velocityX > 0) {
						p2.velocityX -= 0.01;
						if(p2.velocityX < 0)
							p2.velocityX = 0;
					}
					if(p2.velocityX < 0) {
						p2.velocityX += 0.01;
						if(p2.velocityX > 0)
							p2.velocityX = 0;
					}
					
					p2.x += p2.velocityX;
					p2.y += p2.velocityY += 0.01;
				}
				int leftoverAlpha = Color.alpha(p.color) - (int)(Math.random() * 12);
				if(leftoverAlpha < 0)
					leftoverAlpha = 0;
				p.color = Color.argb(leftoverAlpha, Color.red(p.color), Color.green(p.color), Color.blue(p.color));
				if(Color.alpha(p.color) <= 0)
					textExplObj.remove(p);
			}
			paintHandler.removeCallbacks(paintRunnable);
			if(visible)
				paintHandler.post(paintRunnable);
			// System.out.println("Finished logic in " + (System.currentTimeMillis() - startTime) + "ms.");
		}
		
		private void draw() {
			//long startTime = System.currentTimeMillis();
			final SurfaceHolder holder = getSurfaceHolder();
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				if(canvas != null) {
					canvas.drawColor(Color.DKGRAY);
					for(ExplosionParticleGroup p : textExplObj) {
						p.paint.setColor(p.color);
						for(int i = 0; i < p.arr.length; i++) {
							ExplosionParticle p2 = p.arr[i];
							canvas.drawRect(p2.x, p2.y, p2.x + p2.size, p2.y + p2.size, p.paint);
						}
						if(applyShadow)
							p.paint.setShadowLayer(1, 2, 2, Color.argb(Color.alpha(p.color), 0, 0, 0));
					}
					for(TextObject t : textObj) {
						if(t.cachedText != null)
							canvas.drawBitmap(t.cachedText, t.dimen.left, t.dimen.top, paintText);
					}
					
					if(!hasShadow && applyShadow) {
						paintText.setShadowLayer(1, 2, 2, Color.BLACK);
						hasShadow = true;
					} else if(hasShadow && !applyShadow) {
						paintText.setShadowLayer(0, 0, 0, Color.BLACK);
						hasShadow = false;
					}
				}
			} finally {
				if(canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}
			//System.out.println("Finished painting in " + (System.currentTimeMillis() - startTime) + "ms."); //Seems to take about 16-20ms on my device (LGP960).
		}
	}
}
