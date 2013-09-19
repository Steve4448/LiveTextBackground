package steve4448.livetextbackground.background.object;

public class TextObject {
	public String text;
	public double x;
	public double y;
	public double velocityX;
	public double velocityY;
	public int size;
	public int color;
	
	/**
	 * Used as a stub to hold the applicable variables.
	 **/
	public TextObject(String text, double x, double y, int size, int color) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.velocityX = (Math.random() * 3) - (Math.random() * 3);
		this.velocityY = (Math.random() * 2);
		this.size = size;
		this.color = color;
	}
}
