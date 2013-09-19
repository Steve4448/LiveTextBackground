package steve4448.livetextbackground.background;

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
		private TextObject[] textObj;
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
			textObj = new TextObject[100];
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
				if((int)(Math.random() * 25) == 1)
					for(int i = 0; i < textObj.length; i++)
						if(textObj[i] == null) {
							textObj[i] = new TextObject("Testing", (Math.random() * getDesiredMinimumWidth()), 0, (int)(8 + Math.random() * 12), 0x00000055 + (int)(Math.random() * 0xFFFFFF55));
							break;
						}
				canvas.drawColor(Color.DKGRAY);
				for(int i = 0; i < textObj.length; i++) {
					if(textObj[i] == null)
						continue;
					//Text Object Logic
					textObj[i].y += (textObj[i].velocityY+=0.1);
					
					//Text Object Drawing
					paint.setColor(textObj[i].color);
					paint.setTextSize(textObj[i].size);
					canvas.drawText(textObj[i].text, (int)textObj[i].x, (int)textObj[i].y, paint);
					if(textObj[i].y > getDesiredMinimumHeight() + (textObj[i].size * 2))
						textObj[i] = null;
						
				}
			} finally {
				holder.unlockCanvasAndPost(canvas);
			}
			logicHandler.postDelayed(logicRunnable, DESIRED_FPS);
		}
	}
}
