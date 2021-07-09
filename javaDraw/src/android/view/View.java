package android.view;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

public class View extends JComponent{
	private Context mContext;
	private int mMinWidth;
	private int mMinHeight;
	private int mPaddingLeft;
	private int mPaddingRight;
	private int mPaddingTop;
	private int mPaddingBottom;
	private int mScrollX,mScrollY;
	
	
	public View(Context context){
		this.mContext = context;
		this.initView();
	}
	
	private void initView(){
		 addMouseListener(new MouseListener() {
				
			 public void mousePressed(MouseEvent e){
			        System.out.println("你已经压下鼠标按钮");
			        MotionEvent event = new MotionEvent();
			        event.setAction(MotionEvent.ACTION_DOWN);
			        event.addXY(e.getX(),e.getY());
			        onTouchEvent(event);
			        
//			        repaint();
			    }
			    public void mouseReleased(MouseEvent e){
			    	 MotionEvent event = new MotionEvent();
				        event.setAction(MotionEvent.ACTION_UP);
				        event.addXY(e.getX(),e.getY());
				        onTouchEvent(event);
			        
//			        repaint();
			    }
			    public void mouseEntered(MouseEvent e){
			        
//			        repaint();
			    }
			    public void mouseExited(MouseEvent e){
			        
//			        repaint();
			    }
			    public void mouseClicked(MouseEvent e){
			        
//			        repaint();
			    }
		});
	}

	public boolean onTouchEvent(MotionEvent event) {
	    final float x = event.getX();
	    final float y = event.getY();
//	    final int viewFlags = mViewFlags;
//	    final int action = event.getAction();
		return false;
		
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		return false;
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		return false;
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
		
	}
	
	protected int getSuggestedMinimumWidth() {
		return 0;
	}
	
	protected int getSuggestedMinimumHeight() {
		return 0;
	}
	
	public Context getContext(){
		return this.mContext;
	}
	
    public void setMinimumWidth(int minWidth) {
        mMinWidth = minWidth;
        

    }
    
    public void setMinimumHeight(int minHeight){
    	mMinHeight = minHeight;
    }
    
    public int getPaddingLeft(){
    	return this.mPaddingLeft;
    }
    
    public int getPaddingRight(){
    	return this.mPaddingRight;
    }
    
    public int getPaddingTop(){
    	return this.mPaddingTop;
    }
    
    public int getPaddingBottom(){
    	return this.mPaddingBottom;
    }
    
    public final int getMeasuredWidth(){
    	return getWidth();
    }
    
    public final int getMeasureHeight(){
    	return getHeight();
    }
    
    public void setPadding(int padding_left,int padding_top, int padding_right, int padding_bottom){
    	this.mPaddingLeft = padding_left;
    	this.mPaddingTop = padding_top;
    	this.mPaddingRight = padding_right;
    	this.mPaddingBottom = padding_bottom;
    }
    
    public void scrollTo(int scrollX,int scrollY){
    	this.mScrollX = scrollX;
    	this.mScrollY = scrollY;
    }
    
    /**
     * Set the horizontal scrolled position of your view. This will cause a call to
     * {@link #onScrollChanged(int, int, int, int)} and the view will be
     * invalidated.
     * @param value the x position to scroll to
     */
    public void setScrollX(int value) {
        scrollTo(value, mScrollY);
    }

    /**
     * Set the vertical scrolled position of your view. This will cause a call to
     * {@link #onScrollChanged(int, int, int, int)} and the view will be
     * invalidated.
     * @param value the y position to scroll to
     */
    public void setScrollY(int value) {
        scrollTo(mScrollX, value);
    }

    /**
     * Return the scrolled left position of this view. This is the left edge of
     * the displayed part of your view. You do not need to draw any pixels
     * farther left, since those are outside of the frame of your view on
     * screen.
     *
     * @return The left edge of the displayed part of your view, in pixels.
     */
    public final int getScrollX() {
        return mScrollX;
    }

    /**
     * Return the scrolled top position of this view. This is the top edge of
     * the displayed part of your view. You do not need to draw any pixels above
     * it, since those are outside of the frame of your view on screen.
     *
     * @return The top edge of the displayed part of your view, in pixels.
     */
    public final int getScrollY() {
        return mScrollY;
    }
    
    public void setFocusable(boolean b) {
		
	}
    
	public void setFocusableInTouchMode(boolean b) {
		
	}
	
	public void invalidate(){
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D graphics2d = (Graphics2D)g;
		graphics2d.translate(-mScrollX, -mScrollY);
		Canvas canvas = new Canvas(graphics2d);
		onDraw(canvas);
	}
	
	protected void onDraw(Canvas canvas)
	{
		
	}
	
	protected void onFocusChanged(boolean gainFocus,  int direction,
             Rect previouslyFocusedRect) {
		
	}
	
	protected void setMeasuredDimension(int width,int height){
		setPreferredSize(new Dimension(width, height));
	}
	
//	public int getWidth(){
//		return super.getWidth();
//	}
//	
    public interface OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        void onClick(View v);
    }
    
    public static class MeasureSpec {
        private static final int MODE_SHIFT = 30;
        private static final int MODE_MASK  = 0x3 << MODE_SHIFT;

       
        public @interface MeasureSpecMode {}

        /**
         * Measure specification mode: The parent has not imposed any constraint
         * on the child. It can be whatever size it wants.
         */
        public static final int UNSPECIFIED = 0 << MODE_SHIFT;

        /**
         * Measure specification mode: The parent has determined an exact size
         * for the child. The child is going to be given those bounds regardless
         * of how big it wants to be.
         */
        public static final int EXACTLY     = 1 << MODE_SHIFT;

        /**
         * Measure specification mode: The child can be as large as it wants up
         * to the specified size.
         */
        public static final int AT_MOST     = 2 << MODE_SHIFT;

        /**
         * Creates a measure specification based on the supplied size and mode.
         *
         * The mode must always be one of the following:
         * <ul>
         *  <li>{@link android.view.View.MeasureSpec#UNSPECIFIED}</li>
         *  <li>{@link android.view.View.MeasureSpec#EXACTLY}</li>
         *  <li>{@link android.view.View.MeasureSpec#AT_MOST}</li>
         * </ul>
         *
         * <p><strong>Note:</strong> On API level 17 and lower, makeMeasureSpec's
         * implementation was such that the order of arguments did not matter
         * and overflow in either value could impact the resulting MeasureSpec.
         * {@link android.widget.RelativeLayout} was affected by this bug.
         * Apps targeting API levels greater than 17 will get the fixed, more strict
         * behavior.</p>
         *
         * @param size the size of the measure specification
         * @param mode the mode of the measure specification
         * @return the measure specification based on size and mode
         */
//        public static int makeMeasureSpec(@IntRange(from = 0, to = (1 << MeasureSpec.MODE_SHIFT) - 1) int size,
//                                          @MeasureSpecMode int mode) {
//            if (sUseBrokenMakeMeasureSpec) {
//                return size + mode;
//            } else {
//                return (size & ~MODE_MASK) | (mode & MODE_MASK);
//            }
//        }

        /**
         * Like {@link #makeMeasureSpec(int, int)}, but any spec with a mode of UNSPECIFIED
         * will automatically get a size of 0. Older apps expect this.
         *
         * @hide internal use only for compatibility with system widgets and older apps
         */
//        public static int makeSafeMeasureSpec(int size, int mode) {
//            if (sUseZeroUnspecifiedMeasureSpec && mode == UNSPECIFIED) {
//                return 0;
//            }
//            return makeMeasureSpec(size, mode);
//        }

        /**
         * Extracts the mode from the supplied measure specification.
         *
         * @param measureSpec the measure specification to extract the mode from
         * @return {@link android.view.View.MeasureSpec#UNSPECIFIED},
         *         {@link android.view.View.MeasureSpec#AT_MOST} or
         *         {@link android.view.View.MeasureSpec#EXACTLY}
         */
        @MeasureSpecMode
        public static int getMode(int measureSpec) {
            //noinspection ResourceType
            return (measureSpec & MODE_MASK);
        }

        /**
         * Extracts the size from the supplied measure specification.
         *
         * @param measureSpec the measure specification to extract the size from
         * @return the size in pixels defined in the supplied measure specification
         */
        public static int getSize(int measureSpec) {
            return (measureSpec & ~MODE_MASK);
        }

//        static int adjust(int measureSpec, int delta) {
//            final int mode = getMode(measureSpec);
//            int size = getSize(measureSpec);
//            if (mode == UNSPECIFIED) {
//                // No need to adjust size for UNSPECIFIED mode.
//                return makeMeasureSpec(size, UNSPECIFIED);
//            }
//            size += delta;
//            if (size < 0) {
//                Log.e(VIEW_LOG_TAG, "MeasureSpec.adjust: new size would be negative! (" + size +
//                        ") spec: " + toString(measureSpec) + " delta: " + delta);
//                size = 0;
//            }
//            return makeMeasureSpec(size, mode);
//        }

        /**
         * Returns a String representation of the specified measure
         * specification.
         *
         * @param measureSpec the measure specification to convert to a String
         * @return a String with the following format: "MeasureSpec: MODE SIZE"
         */
        public static String toString(int measureSpec) {
            int mode = getMode(measureSpec);
            int size = getSize(measureSpec);

            StringBuilder sb = new StringBuilder("MeasureSpec: ");

            if (mode == UNSPECIFIED)
                sb.append("UNSPECIFIED ");
            else if (mode == EXACTLY)
                sb.append("EXACTLY ");
            else if (mode == AT_MOST)
                sb.append("AT_MOST ");
            else
                sb.append(mode).append(" ");

            sb.append(size);
            return sb.toString();
        }
    }
}
