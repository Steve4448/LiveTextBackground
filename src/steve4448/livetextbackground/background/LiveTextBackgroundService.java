package steve4448.livetextbackground.background;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class LiveTextBackgroundService extends WallpaperService {
	@Override
    public Engine onCreateEngine() {
	    return new LiveTextBackgroundEngine();
    }
	
	private class LiveTextBackgroundEngine extends Engine {
		private SurfaceHolder holder;
		private Paint paint = new Paint();
		
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
			//setupLogicHandler(true);
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
			System.out.println("Drawing...");
			Canvas canvas = holder.lockCanvas();
			try {
				canvas.drawColor(Color.DKGRAY);
				paint.setColor(Color.WHITE);
				for(int i = 0; i < 3; i++)
					canvas.drawRect(new Rect((int)(Math.random() * getDesiredMinimumHeight()), (int)(Math.random() * getDesiredMinimumHeight()), (int)(Math.random() * getDesiredMinimumHeight()), (int)(Math.random() * getDesiredMinimumHeight())), paint);
				paint.setColor(Color.BLACK);
				paint.setTextSize(50);
				canvas.drawText("LEL TESTING LEL", 0, 200, paint);
			} finally {
				holder.unlockCanvasAndPost(canvas);
			}
			logicHandler.postDelayed(logicRunnable, 1000);
		}
	}
}
