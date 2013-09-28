package steve4448.livetextbackground.util;

import android.view.View;

public class ViewHelper {
	private static int last = 0;
	public static int findUnusedId(View v) {
		while(v.findViewById(last++) != null);
			return last;
	}
}
