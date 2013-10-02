package steve4448.livetextbackground.widget;

import steve4448.livetextbackground.R;
import steve4448.livetextbackground.widget.listener.OnMinMaxBarChangeListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MinMaxBar extends View {
	public static final int BAR_COLOR = Color.argb(100, 60, 60, 60);
	public static final int THUMB_COLOR = Color.argb(100, 0, 166, 255);
	public static final int THUMB_COLOR_PRESSED = Color.argb(200, 0, 166, 255);
	public static final int THUMB_OUTER_RING_COLOR = Color.argb(200, 0, 80, 255);
	public static final int THUMB_INNER_COLOR = Color.argb(200, 255, 255, 255);
	public static final int THUMB_INNER_COLOR_PRESSED = Color.argb(255, 200, 200, 200);
	public static final int THUMB_INNER_RING_COLOR = Color.argb(200, 55, 55, 55);
	
	private Paint paintFilled;
	private Paint paintStroked;
	
	private OnMinMaxBarChangeListener onMinMaxBarChangeListener;
	
	private float absoluteMinimum = 0, actualMinimum = 0;
	private float absoluteMaximum = 100, actualMaximum = 0;
	
	public float maxCircleRadius = 24;
	private RectF barRect;
	private PointF thumbMinPoint;
	private PointF thumbMaxPoint;
	
	private boolean singleThumbMode = false;
	
	private boolean draggingMinXThumb = false;
	private boolean draggingMaxXThumb = false;
	
	public MinMaxBar(Context context) {
		super(context);
		init(context, null, 0);
	}
	
	public MinMaxBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}
	
	public MinMaxBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}
	
	public void init(Context context, AttributeSet attrs, int defStyle) {
		paintFilled = new Paint();
		paintFilled.setStyle(Paint.Style.FILL);
		paintFilled.setAntiAlias(true);
		
		paintStroked = new Paint();
		paintStroked.setStyle(Paint.Style.STROKE);
		paintStroked.setAntiAlias(true);
		if(attrs != null) {
			TypedArray extraAttrs = context.obtainStyledAttributes(attrs, R.styleable.MinMaxBar);
			singleThumbMode = extraAttrs.getBoolean(R.styleable.MinMaxBar_singleThumbMode, singleThumbMode);
			extraAttrs.recycle();
		}
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		barRect = new RectF(maxCircleRadius, h / 2 - h / 6, w - maxCircleRadius, h / 2 + h / 6);
		maxCircleRadius = h / 3;
		thumbMinPoint = new PointF(barRect.left, barRect.top + barRect.height() / 2);
		thumbMaxPoint = new PointF(barRect.left, barRect.top + barRect.height() / 2);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		thumbMinPoint.x = actualMinimum == 0 ? 0 : (((actualMinimum - absoluteMinimum) / (absoluteMaximum - absoluteMinimum)) * (barRect.right - barRect.left)) + barRect.left;
		thumbMaxPoint.x = actualMaximum == 0 ? 0 : (((actualMaximum - absoluteMinimum) / (absoluteMaximum - absoluteMinimum)) * (barRect.right - barRect.left)) + barRect.left;
		paintFilled.setColor(BAR_COLOR);
		canvas.drawRect(barRect, paintFilled);
		
		// Thumb Min:
		{
			if(isEnabled()) {
				paintFilled.setColor(draggingMinXThumb ? THUMB_COLOR_PRESSED : THUMB_COLOR);
				canvas.drawCircle(thumbMinPoint.x, thumbMinPoint.y, maxCircleRadius, paintFilled);
			}
			paintFilled.setColor(draggingMinXThumb ? THUMB_INNER_COLOR_PRESSED : isEnabled() ? THUMB_INNER_COLOR : THUMB_INNER_COLOR_PRESSED);
			canvas.drawCircle(thumbMinPoint.x, thumbMinPoint.y, maxCircleRadius / 2, paintFilled);
			
			if(isEnabled()) {
				paintStroked.setColor(THUMB_OUTER_RING_COLOR);
				canvas.drawCircle(thumbMinPoint.x, thumbMinPoint.y, maxCircleRadius, paintStroked);
			}
			
			paintStroked.setColor(THUMB_INNER_RING_COLOR);
			canvas.drawCircle(thumbMinPoint.x, thumbMinPoint.y, maxCircleRadius / 2, paintStroked);
		}
		
		// Thumb Max:
		if(!singleThumbMode) {
			if(isEnabled()) {
				paintFilled.setColor(draggingMaxXThumb ? THUMB_COLOR_PRESSED : THUMB_COLOR);
				canvas.drawCircle(thumbMaxPoint.x, thumbMaxPoint.y, maxCircleRadius, paintFilled);
			}
			
			paintFilled.setColor(draggingMaxXThumb ? THUMB_INNER_COLOR_PRESSED : isEnabled() ? THUMB_INNER_COLOR : THUMB_INNER_COLOR_PRESSED);
			canvas.drawCircle(thumbMaxPoint.x, thumbMaxPoint.y, maxCircleRadius / 2, paintFilled);
			
			if(isEnabled()) {
				paintStroked.setColor(THUMB_OUTER_RING_COLOR);
				canvas.drawCircle(thumbMaxPoint.x, thumbMaxPoint.y, maxCircleRadius, paintStroked);
			}
			
			paintStroked.setColor(THUMB_INNER_RING_COLOR);
			canvas.drawCircle(thumbMaxPoint.x, thumbMaxPoint.y, maxCircleRadius / 2, paintStroked);
		}
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
		PointF locPoint = null;
		RectF thumbMinXRect = null, thumbMaxXRect = null;
		if(!draggingMinXThumb && !draggingMaxXThumb) {
			locPoint = new PointF(event.getX(), event.getY());
			thumbMinXRect = new RectF(thumbMinPoint.x - maxCircleRadius, thumbMinPoint.y - maxCircleRadius, thumbMinPoint.x + maxCircleRadius, thumbMinPoint.y + maxCircleRadius);
			thumbMaxXRect = new RectF(thumbMaxPoint.x - maxCircleRadius, thumbMaxPoint.y - maxCircleRadius, thumbMaxPoint.x + maxCircleRadius, thumbMaxPoint.y + maxCircleRadius);
		}
		if(draggingMinXThumb || (locPoint != null && steve4448.livetextbackground.util.RectF.intersects(locPoint, thumbMinXRect))) {
			draggingMinXThumb = true;
			draggingMaxXThumb = false;
			float newLocationMinXThumb = event.getX();
			
			if(newLocationMinXThumb < barRect.left)
				newLocationMinXThumb = barRect.left;
			else if(newLocationMinXThumb > barRect.right)
				newLocationMinXThumb = barRect.right;
			
			if(!singleThumbMode && newLocationMinXThumb > thumbMaxPoint.x) {
				newLocationMinXThumb = thumbMaxPoint.x;
				draggingMinXThumb = false;
				draggingMaxXThumb = true;
			}
			
			actualMinimum = absoluteMinimum + ((newLocationMinXThumb - barRect.left) / (barRect.right - barRect.left)) * (absoluteMaximum - absoluteMinimum);
			if(onMinMaxBarChangeListener != null)
				onMinMaxBarChangeListener.onMinValueChanged((int) actualMinimum, true);
		} else if(draggingMaxXThumb || (locPoint != null && steve4448.livetextbackground.util.RectF.intersects(locPoint, thumbMaxXRect) && !singleThumbMode)) {
			draggingMinXThumb = false;
			draggingMaxXThumb = true;
			float newLocationMaxXThumb = event.getX();
			
			if(newLocationMaxXThumb < barRect.left)
				newLocationMaxXThumb = barRect.left;
			else if(newLocationMaxXThumb > barRect.right)
				newLocationMaxXThumb = barRect.right;
			
			if(newLocationMaxXThumb < thumbMinPoint.x) {
				newLocationMaxXThumb = thumbMinPoint.x;
				draggingMinXThumb = true;
				draggingMaxXThumb = false;
			}
			actualMaximum = absoluteMinimum + ((newLocationMaxXThumb - barRect.left) / (barRect.right - barRect.left)) * (absoluteMaximum - absoluteMinimum);
			if(onMinMaxBarChangeListener != null)
				onMinMaxBarChangeListener.onMaxValueChanged((int) actualMaximum, true);
		}
		invalidate();
	}
	
	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
		if(!pressed) {
			draggingMinXThumb = false;
			draggingMaxXThumb = false;
			invalidate();
		}
	}
	
	public void setMinMaxAbsolutes(float min, float max) {
		absoluteMinimum = min;
		absoluteMaximum = max;
		if(actualMinimum < min)
			actualMinimum = min;
		if(actualMinimum > max)
			actualMinimum = max;
		if(actualMaximum < min)
			actualMaximum = min;
		if(actualMaximum > max)
			actualMaximum = max;
		invalidate();
	}
	
	public void setMinimum(float minimum) {
		actualMinimum = minimum;
		if(actualMinimum < absoluteMinimum)
			actualMinimum = absoluteMinimum;
		else if(actualMinimum > absoluteMaximum)
			actualMinimum = absoluteMaximum;
		if(onMinMaxBarChangeListener != null)
			onMinMaxBarChangeListener.onMinValueChanged((int) actualMinimum, false);
		invalidate();
	}
	
	public float getMinimum() {
		return actualMinimum;
	}
	
	public float getAbsoluteMinimum() {
		return absoluteMinimum;
	}
	
	public void setMaximum(float maximum) {
		actualMaximum = maximum;
		if(actualMaximum < absoluteMinimum)
			actualMaximum = absoluteMinimum;
		else if(actualMaximum > absoluteMaximum)
			actualMaximum = absoluteMaximum;
		if(onMinMaxBarChangeListener != null)
			onMinMaxBarChangeListener.onMaxValueChanged((int) actualMaximum, false);
		invalidate();
	}
	
	public float getMaximum() {
		return actualMaximum;
	}
	
	public float getAbsoluteMaximum() {
		return absoluteMaximum;
	}
	
	public void setSingleThumbMode(boolean singleThumbMode) {
		this.singleThumbMode = singleThumbMode;
		invalidate();
	}
	
	public boolean isSingleThumbMode() {
		return singleThumbMode;
	}
	
	public void setOnMinMaxBarChangeListener(OnMinMaxBarChangeListener onMinMaxBarChangeListener) {
		this.onMinMaxBarChangeListener = onMinMaxBarChangeListener;
	}
}
