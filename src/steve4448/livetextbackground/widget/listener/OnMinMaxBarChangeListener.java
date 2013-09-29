package steve4448.livetextbackground.widget.listener;

public interface OnMinMaxBarChangeListener {
	public abstract void onMinValueChanged(int newMin, boolean userCall);
	
	public abstract void onMaxValueChanged(int newMax, boolean userCall);
}
