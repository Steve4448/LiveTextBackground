package steve4448.livetextbackground.background.object;

public class TextObject {
	public String text;
	public float x;
	public float y;
	public int width;
	public int height;
	public double velocityX;
	public double velocityY;
	public int size;
	public int color;
	
	/**
	 * Used as a stub to hold the applicable variables.
	 **/
	public TextObject(String text, float x, float y, int width, int height, int size, int color) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.velocityX = (Math.random() * 3) - (Math.random() * 3);
		this.velocityY = (Math.random() * 6);
		this.size = size;
		this.color = color;
	}
}
