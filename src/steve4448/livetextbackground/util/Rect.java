package steve4448.livetextbackground.util;

import android.graphics.Point;

public class Rect {
	public int x;
	public int y;
	public int width;
	public int height;
	
	public Rect() {
		this(0, 0, 0);
	}
	
	public Rect(int x, int y, int size) {
		this(x, y, size, size);
	}
	
	public Rect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean intersects(Rect with) {
		return x >= with.x && x <= with.x + with.width && y >= with.y && y <= with.y + with.height;
	}
	
	public boolean intersects(Point with) {
		return with.x >= x && with.x <= x + width && with.y >= y && with.y <= y + height;
	}
	
	public static boolean intersects(Point first, Rect second) {
		return first.x >= second.x && first.x <= second.x + second.width && first.y >= second.y && first.y <= second.y + second.height;
	}
	
	public static boolean intersects(Point first, android.graphics.Rect second) {
		return first.x >= second.left && first.x <= second.right && first.y >= second.top && first.y <= second.bottom;
	}
	
	public static boolean intersects(Rect first, Rect second) {
		return first.x >= second.x && first.x <= second.x + second.width && first.y >= second.y && first.y <= second.y + second.height;
	}
}
