package steve4448.livetextbackground.background;

import java.util.concurrent.CopyOnWriteArrayList;

import steve4448.livetextbackground.background.object.TextObject;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class LiveTextBackgroundService extends WallpaperService {
	public static final int DESIRED_FPS = 1000/30;
	
	@Override
	public Engine onCreateEngine() {
		return new LiveTextBackgroundEngine();
	}
	
	private class LiveTextBackgroundEngine extends Engine {
		private SurfaceHolder holder;
		private Paint paint = new Paint();
		private CopyOnWriteArrayList<TextObject> textObj = new CopyOnWriteArrayList<TextObject>();
		private Thread logicThread;
		private final Handler paintHandler = new Handler();
		private final Runnable paintRunnable = new Runnable() {
			@Override
			public void run() {
				draw();
			}
		};
		private boolean handlerBusy = false;
		
		private LiveTextBackgroundEngine() {
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.FILL);
		}
		
		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			this.holder = holder;
		}
		
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			this.holder = null;
		}
		
		@Override
		public void onVisibilityChanged(boolean visible) {
			setupLogicHandler(visible);
		}
		
		private void setupLogicHandler(boolean on) {
			if(!handlerBusy && on) {
				handlerBusy = true;
				logicThread = new Thread() {
					@Override
					public void run() {
						try {
							while(true) {
								logic();
								Thread.sleep(DESIRED_FPS);
							}
						} catch(InterruptedException e) {
						}
					}
				};
				logicThread.start();
				paintHandler.post(paintRunnable);
			} else if(!on) {
				handlerBusy = false;
				logicThread.interrupt();
				paintHandler.removeCallbacks(paintRunnable);
			}
		}
		
		private void logic() {
			if((int)(Math.random() * 22) == 1 || textObj.size() < 3)
				textObj.add(new TextObject("Testing", (Math.random() * getDesiredMinimumWidth()), 0, (int)(8 + Math.random() * 12), Color.argb(155 + (int)(Math.random() * 100), (int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255))));
			for(TextObject t : textObj) {
				//Text Object Logic
				if(t.velocityX > 0) {
					t.velocityX-=0.01;
					if(t.velocityX < 0)
						t.velocityX = 0;
				}
				if(t.velocityX < 0) {
					t.velocityX+=0.01;
					if(t.velocityX > 0)
						t.velocityX = 0;
				}
				t.x += t.velocityX;
				t.y += (t.velocityY+=0.1);
				if(t.y > getDesiredMinimumHeight() + (t.size * 2) || t.x > getDesiredMinimumWidth() || t.x < 0 - paint.measureText(t.text))
					textObj.remove(t);
			}
		}
		
		private void draw() {
			if(holder == null)
				return;
			Canvas canvas = holder.lockCanvas();
			try {
				canvas.drawColor(Color.DKGRAY);
				for(TextObject t : textObj) {
					paint.setColor(t.color);
					paint.setTextSize(t.size);
					canvas.drawText(t.text, (int)t.x, (int)t.y, paint);
				}
			} finally {
				holder.unlockCanvasAndPost(canvas);
			}
			paintHandler.postDelayed(paintRunnable, DESIRED_FPS);
		}
	}
}
