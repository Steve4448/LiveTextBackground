package steve4448.livetextbackground.util;

import android.graphics.PointF;

public class RectF {
	public float x;
	public float y;
	public float width;
	public float height;
	public RectF() {
		this(0, 0, 0);
	}
	
	public RectF(float x, float y, float size) {
		this(x, y, size, size);
	}
	
	public RectF(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean intersects(RectF with) {
		return x >= with.x && x <= with.x + with.width && y >= with.y && y <= with.y + with.height;
	}
	
	public boolean intersects(PointF with) {
		return with.x >= x && with.x <= x + width && with.y >= y && with.y <= y + height;
	}
	
	public static boolean intersects(PointF first, RectF second) {
		return first.x >= second.x && first.x <= second.x + second.width && first.y >= second.y && first.y <= second.y + second.height;
	}
	
	public static boolean intersects(PointF first, android.graphics.RectF second) {
		return first.x >= second.left && first.x <= second.right && first.y >= second.top && first.y <= second.bottom;
	}
	
	public static boolean intersects(RectF first, RectF second) {
		return first.x >= second.x && first.x <= second.x + second.width && first.y >= second.y && first.y <= second.y + second.height;
	}
	
	public android.graphics.RectF toAndroidRect() {
		return new android.graphics.RectF(x, y, x + width, y + height);
	}
	
	public static android.graphics.RectF toAndroidRect(RectF r) {
		return new android.graphics.RectF(r.x, r.y, r.x + r.width, r.y + r.height);
	}
}
