package steve4448.livetextbackground.background;

import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class LiveTextBackgroundService extends WallpaperService {
	@Override
    public Engine onCreateEngine() {
	    return new LiveTextBackgroundEngine();
    }
	
	class LiveTextBackgroundEngine extends Engine {
		public LiveTextBackgroundEngine() {
			
		}
		
		public void onCreate(SurfaceHolder surface) {
			super.onCreate(surface);
		}
	}
}
