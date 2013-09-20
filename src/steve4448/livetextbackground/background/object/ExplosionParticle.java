package steve4448.livetextbackground.background.object;

public class ExplosionParticle {
	public float x;
	public float y;
	public float velocityX;
	public float velocityY;
	public int size;
	public int color;
	
	public ExplosionParticle(float x, float y, int size, int color) {
		this.x = x;
		this.y = y;
		velocityX = (float)(Math.random() * 3 - Math.random() * 3);
		velocityY = (float)(Math.random() * 3 - Math.random() * 3);
		this.size = size;
		this.color = color;
	}
}
