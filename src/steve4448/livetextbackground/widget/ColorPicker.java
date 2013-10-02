package steve4448.livetextbackground.widget;

import steve4448.livetextbackground.util.Rect;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/*
 * Tidbits of implementation code from:
 * https://github.com/CyanogenMod/android_packages_apps_Settings/blob/527f17f73f76feee76f50d022c7b7629d288e312/src/com/android/settings/notificationlight/ColorPickerView.java
 */
public class ColorPicker extends View {
	public static final int HUE_SELECTOR_RADIUS = 10;
	public static final int LINE_THICKNESS = 3;
	public static final int CONTENT_PADDING = HUE_SELECTOR_RADIUS + (LINE_THICKNESS * 2);
	private int[] hueList;
	private Paint huePaint;
	private Rect hueRect;
	private LinearGradient hueShader;
	
	private Paint satValPaint;
	private Rect satValRect;
	private LinearGradient satValShader;
	
	private Paint selectorPaint;
	
	private boolean draggingSelector = false;
	private Point hueSelectorLoc;
	
	private boolean draggingSelector2 = false;
	private Point valSelectorLoc;
	
	public ColorPicker(Context context) {
		super(context);
		init(context, null, 0);
	}
	
	public ColorPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}
	
	public ColorPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}
	
	public void init(Context context, AttributeSet attrs, int defStyle) {
		hueList = new int[361];
		for(int i = 0; i < hueList.length; i++) {
			hueList[i] = Color.HSVToColor(new float[] {i, 1f, 1f});
		}
		onSizeChanged(getWidth(), getHeight(), 0, 0);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldW, int oldH) {
		super.onSizeChanged(w, h, oldW, oldH);
		hueRect = new Rect(0, 0, 300, 300);
		hueShader = new LinearGradient(0, 0, hueRect.x, hueRect.height, hueList, null, TileMode.CLAMP);
		huePaint = new Paint();
		huePaint.setStyle(Paint.Style.FILL);
		huePaint.setShader(hueShader);
		
		satValRect = new Rect(0, hueRect.y + hueRect.height + HUE_SELECTOR_RADIUS + (LINE_THICKNESS * 2), 300, 30);
		satValRect.x = w / 2 - satValRect.width / 2 - CONTENT_PADDING;
		satValShader = new LinearGradient(0, 0, satValRect.width, satValRect.height, 0xFF000000, 0xFFFFFFFF, TileMode.CLAMP);
		satValPaint = new Paint();
		satValPaint.setStyle(Paint.Style.FILL);
		satValPaint.setShader(satValShader);
		
		valSelectorLoc = new Point(satValRect.x, satValRect.y);
		hueSelectorLoc = new Point(hueRect.x + hueRect.width / 2, hueRect.y + hueRect.height / 2);
		selectorPaint = new Paint();
		selectorPaint.setAntiAlias(true);
		selectorPaint.setStyle(Paint.Style.STROKE);
		selectorPaint.setStrokeWidth(LINE_THICKNESS);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// TODO: Actually measure properly.
		this.setMeasuredDimension(hueRect.width + CONTENT_PADDING * 2, hueRect.height + satValRect.height + CONTENT_PADDING * 2 + (LINE_THICKNESS * 2));
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.translate(CONTENT_PADDING, CONTENT_PADDING);
		canvas.drawRect(hueRect.toAndroidRect(), huePaint);
		canvas.drawRect(satValRect.toAndroidRect(), satValPaint);
		selectorPaint.setColor(Color.BLACK);
		canvas.drawCircle(hueSelectorLoc.x, hueSelectorLoc.y, HUE_SELECTOR_RADIUS, selectorPaint);
		canvas.drawRect(valSelectorLoc.x - 5, valSelectorLoc.y, valSelectorLoc.x + 5, valSelectorLoc.y + 30, selectorPaint);
		selectorPaint.setColor(Color.WHITE);
		canvas.drawCircle(hueSelectorLoc.x, hueSelectorLoc.y, HUE_SELECTOR_RADIUS + LINE_THICKNESS, selectorPaint);
		canvas.drawRect(valSelectorLoc.x - 5 - LINE_THICKNESS, valSelectorLoc.y - LINE_THICKNESS, valSelectorLoc.x + 5 + LINE_THICKNESS, valSelectorLoc.y + 30 + LINE_THICKNESS, selectorPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!isEnabled())
			return false;
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				setPressed(true);
				attemptMove(event);
			break;
			
			case MotionEvent.ACTION_MOVE:
				getParent().requestDisallowInterceptTouchEvent(true);
				attemptMove(event);
			break;
			
			case MotionEvent.ACTION_UP:
				setPressed(false);
			break;
			
			case MotionEvent.ACTION_CANCEL:
				setPressed(false);
			break;
		}
		return true;
	}
	
	public void attemptMove(MotionEvent event) {
		Point p = new Point((int) event.getX(), (int) event.getY());
		p.x -= CONTENT_PADDING;
		p.y -= CONTENT_PADDING;
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			draggingSelector = hueRect.intersects(p);
			draggingSelector2 = satValRect.intersects(p);
		}
		
		if(draggingSelector) {
			hueSelectorLoc.x = p.x;
			hueSelectorLoc.y = p.y;
			
			if(hueSelectorLoc.x < hueRect.x)
				hueSelectorLoc.x = hueRect.x;
			else if(hueSelectorLoc.x > hueRect.x + hueRect.width)
				hueSelectorLoc.x = hueRect.x + hueRect.width;
			
			if(hueSelectorLoc.y < hueRect.y)
				hueSelectorLoc.y = hueRect.y;
			else if(hueSelectorLoc.y > hueRect.y + hueRect.height)
				hueSelectorLoc.y = hueRect.y + hueRect.height;
		} else if(draggingSelector2) {
			valSelectorLoc.x = p.x;
			
			if(valSelectorLoc.x < satValRect.x)
				valSelectorLoc.x = satValRect.x;
			else if(valSelectorLoc.x > satValRect.x + satValRect.width)
				valSelectorLoc.x = satValRect.x + satValRect.width;
		}
		if(draggingSelector || draggingSelector2)
			invalidate();
	}
	
	@Override
	public void setPressed(boolean isPressed) {
		draggingSelector = false;
	}
}
