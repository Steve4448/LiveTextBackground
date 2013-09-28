package steve4448.livetextbackground.background.object;

import android.graphics.RectF;

public class TextObject {
	public String text;
	public RectF dimen;
	public float velocityX;
	public float velocityY;
	public int size;
	public int color;
	
	/**
	 * Used as a stub to hold the applicable variables.
	 **/
	public TextObject(String text, RectF dimen, int size, int color) {
		this.text = text;
		this.dimen = dimen;
		this.velocityX = (float)((Math.random() * 3) - (Math.random() * 3));
		this.velocityY = (float)(Math.random() * 6);
		this.size = size;
		this.color = color;
	}
}
