package steve4448.livetextbackground.background;

import java.util.ArrayList;
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
		private final Handler logicHandler = new Handler();
		private final Runnable logicRunnable = new Runnable() {
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
			setupLogicHandler(true);
		}
		
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			this.holder = null;
			setupLogicHandler(false);
		}
		
		@Override
		public void onVisibilityChanged(boolean visible) {
			setupLogicHandler(visible);
		}
		
		private void setupLogicHandler(boolean on) {
			if(!handlerBusy && on) {
				logicHandler.post(logicRunnable);
				handlerBusy = true;
			} else if(!on) {
				logicHandler.removeCallbacks(logicRunnable);
				handlerBusy = false;
			}
		}
		
		private void draw() {
			if(holder == null)
				return;
			Canvas canvas = holder.lockCanvas();
			try {
				if((int)(Math.random() * 22) == 1 || textObj.size() < 3)
					textObj.add(new TextObject("Testing", (Math.random() * getDesiredMinimumWidth()), 0, (int)(8 + Math.random() * 12), Color.argb(155 + (int)(Math.random() * 100), (int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255))));
				canvas.drawColor(Color.DKGRAY);
				for(TextObject t : textObj) {
					//Text Object Logic
					t.y += (t.velocityY+=0.1);
					
					//Text Object Drawing
					paint.setColor(t.color);
					paint.setTextSize(t.size);
					int maxXPosition = (int)(getDesiredMinimumWidth() - paint.measureText(t.text));
					if(t.x > maxXPosition) t.x = maxXPosition;
					canvas.drawText(t.text, (int)t.x, (int)t.y, paint);
					if(t.y > getDesiredMinimumHeight() + (t.size * 2))
						textObj.remove(t);
						
				}
			} finally {
				holder.unlockCanvasAndPost(canvas);
			}
			logicHandler.postDelayed(logicRunnable, DESIRED_FPS);
		}
	}
}
