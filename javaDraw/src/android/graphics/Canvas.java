package android.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.ImageObserver;

public class Canvas {
	Graphics2D graphics2d;

	public Canvas(Bitmap bitmap) {
		graphics2d = bitmap.image.createGraphics();
	}
	
	public Canvas(Graphics2D graphics2d) {
		this.graphics2d = graphics2d;
	}

	public void setColor(int color) {
		graphics2d.setColor(new Color(color));
	}

	public void drawBitmap(Bitmap bitmap, double x,double y, Paint paint) {
		graphics2d.drawImage(bitmap.image, (int)x, (int)y, new ImageObserver(){

			public boolean imageUpdate(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4,
					int paramInt5) {
				// TODO Auto-generated method stub
				return true;
			}
			
		});
	}
	
    public void drawBitmap( Bitmap bitmap,  Rect src,  Rect dst,
             Paint paint) {
    	if(paint.isAntiAlias()){
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	}
    	else{
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    	}
        graphics2d.drawImage(bitmap.image, dst.left, dst.top, dst.right, dst.bottom,src.left,src.top, src.right, src.bottom,   null);
    }
    
    public void drawBitmap( int[] colors, int offset, int stride, float x, float y,
            int width, int height, boolean hasAlpha,  Paint paint) {
        
    }
//    
//    public void drawBitmap( Bitmap bitmap,  Matrix matrix,  Paint paint) {
//        super.drawBitmap(bitmap, matrix, paint);
//    }
//    
    
    
    public void drawColor( int color) {
    	Color c = new Color((color>>16)&0xff, (color>>8)&0xff, (color)&0xff, (color>>24)&0xff);
        graphics2d.setColor(c);
        graphics2d.fillRect(0, 0, 1080, 1080);
    }

 /**
     * Draw the specified circle using the specified paint. If radius is <= 0, then nothing will be
     * drawn. The circle will be filled or framed based on the Style in the paint.
     *
     * @param cx The x-coordinate of the center of the cirle to be drawn
     * @param cy The y-coordinate of the center of the cirle to be drawn
     * @param radius The radius of the cirle to be drawn
     * @param paint The paint used to draw the circle
     */
    public void drawCircle(float cx, float cy, float radius,  Paint paint) {
    	graphics2d.setColor(paint.mColor);
    	if(paint.isAntiAlias()){
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	}
    	else{
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    	}
    	
    	if(paint.mStyle == Paint.Style.FILL){
    		graphics2d.fillOval((int)(cx-radius), (int)(cy-radius), (int)(radius*2), (int)radius*2);
    	}
    	else if(paint.mStyle == Paint.Style.STROKE){
    		graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
    		graphics2d.drawOval((int)(cx-radius), (int)(cy-radius), (int)(radius*2), (int)radius*2);
    	}
    	else if(paint.mStyle == Paint.Style.FILL_AND_STROKE){
    		graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
    		graphics2d.fillOval((int)(cx-radius), (int)(cy-radius), (int)(radius*2), (int)(radius*2));
    		graphics2d.drawOval((int)(cx-radius), (int)(cy-radius), (int)(radius*2), (int)(radius*2));
    	}
        
    	
    }

  /**
     * Draw a line segment with the specified start and stop x,y coordinates, using the specified
     * paint.
     * <p>
     * Note that since a line is always "framed", the Style is ignored in the paint.
     * </p>
     * <p>
     * Degenerate lines (length is 0) will not be drawn.
     * </p>
     *
     * @param startX The x-coordinate of the start point of the line
     * @param startY The y-coordinate of the start point of the line
     * @param paint The paint used to draw the line
     */
    public void drawLine(float startX, float startY, float stopX, float stopY,
             Paint paint) {
    	if(paint.isAntiAlias()){
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	}
    	else{
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    	}
    	graphics2d.setColor(paint.mColor);
    	graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
        graphics2d.drawLine((int)startX, (int)startY, (int)stopX, (int)stopY);
    }

 /**
     * Draw the specified oval using the specified paint. The oval will be filled or framed based on
     * the Style in the paint.
     */
    public void drawOval(float left, float top, float right, float bottom,  Paint paint) {
    	graphics2d.setColor(paint.mColor);
    	if(paint.isAntiAlias()){
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	}
    	else{
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    	}
    	if(paint.mStyle == Paint.Style.FILL){
    		graphics2d.fillOval((int)left, (int)top, (int)(right-left), (int)(bottom-top));
    	}
    	else if(paint.mStyle == Paint.Style.STROKE){
    		graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
    		graphics2d.drawOval((int)left, (int)top, (int)(right-left), (int)(bottom-top));
    	}
    	else if(paint.mStyle == Paint.Style.FILL_AND_STROKE){
    		graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
    		graphics2d.drawOval((int)left, (int)top, (int)(right-left), (int)(bottom-top));
    		graphics2d.fillOval((int)left, (int)top, (int)(right-left), (int)(bottom-top));
    	}
    	
    
    }

    /**
     * Fill the entire canvas' bitmap (restricted to the current clip) with the specified paint.
     * This is equivalent (but faster) to drawing an infinitely large rectangle with the specified
     * paint.
     *
     * @param paint The paint used to draw onto the canvas
     */
    public void drawPaint( Paint paint) {
        drawColor(paint.getColor());
    }

//    /**
//     * Helper for drawPoints() for drawing a single point.
//     */
//    public void drawPoint(float x, float y, Paint paint) {
//        
//    }



    /**
     * Draw the specified Rect using the specified Paint. The rectangle will be filled or framed
     * based on the Style in the paint.
     *
     * @param r The rectangle to be drawn.
     * @param paint The paint used to draw the rectangle
     */
    public void drawRect( Rect r,  Paint paint) {
    	graphics2d.setColor(paint.mColor);
    	if(paint.isAntiAlias()){
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	}
    	else{
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    	}
        
        if(paint.mStyle == Paint.Style.FILL){
        	graphics2d.fillRect((int)r.left, (int)r.top, (int)r.width(), (int)r.height());
    	}
    	else if(paint.mStyle == Paint.Style.STROKE){
    		graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
    		graphics2d.drawRect((int)r.left, (int)r.top, (int)r.width(), (int)r.height());
    	}
    	else if(paint.mStyle == Paint.Style.FILL_AND_STROKE){
    		graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
    		graphics2d.fillRect((int)r.left, (int)r.top, (int)r.width(), (int)r.height());
    		graphics2d.drawRect((int)r.left, (int)r.top, (int)r.width(), (int)r.height());
    	}
    }

  /**
     * Draw the specified Rect using the specified paint. The rectangle will be filled or framed
     * based on the Style in the paint.
     *
     * @param left The left side of the rectangle to be drawn
     * @param top The top side of the rectangle to be drawn
     * @param right The right side of the rectangle to be drawn
     * @param bottom The bottom side of the rectangle to be drawn
     * @param paint The paint used to draw the rect
     */
    public void drawRect(float left, float top, float right, float bottom,  Paint paint) {
    	graphics2d.setColor(paint.mColor);
    	if(paint.isAntiAlias()){
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	}
    	else{
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    	}
        if(paint.mStyle == Paint.Style.FILL){
        	graphics2d.fillRect((int)left, (int)top, (int)(right-left), (int)(bottom-top));
    	}
    	else if(paint.mStyle == Paint.Style.STROKE){
    		graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
    		graphics2d.drawRect((int)left, (int)top, (int)(right-left), (int)(bottom-top));
    	}
    	else if(paint.mStyle == Paint.Style.FILL_AND_STROKE){
    		graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
    		graphics2d.drawRect((int)left, (int)top, (int)(right-left), (int)(bottom-top));
    		graphics2d.fillRect((int)left, (int)top, (int)(right-left), (int)(bottom-top));
    	}
    }

    /**
     * Fill the entire canvas' bitmap (restricted to the current clip) with the specified RGB color,
     * using srcover porterduff mode.
     *
     * @param r red component (0..255) of the color to draw onto the canvas
     * @param g green component (0..255) of the color to draw onto the canvas
     * @param b blue component (0..255) of the color to draw onto the canvas
     */
    public void drawRGB(int r, int g, int b) {
        drawColor(0xff000000 | (r<<16) | (g<<8) | b);
    }

    /**
     * Draw the specified round-rect using the specified paint. The roundrect will be filled or
     * framed based on the Style in the paint.
     *
     * @param rect The rectangular bounds of the roundRect to be drawn
     * @param rx The x-radius of the oval used to round the corners
     * @param ry The y-radius of the oval used to round the corners
     * @param paint The paint used to draw the roundRect
     */
    public void drawRoundRect( RectF rect, float rx, float ry,  Paint paint) {
    	graphics2d.setColor(paint.mColor);
    	if(paint.isAntiAlias()){
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	}
    	else{
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    	}
    	 if(paint.mStyle == Paint.Style.FILL){
    		 graphics2d.fillRoundRect((int)rect.left, (int)rect.top, (int)rect.width(), (int)rect.height(), (int)rx, (int)rx);
     	}
     	else if(paint.mStyle == Paint.Style.STROKE){
     		graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
     		graphics2d.drawRoundRect((int)rect.left, (int)rect.top, (int)rect.width(), (int)rect.height(), (int)rx, (int)rx);
     	}
     	else if(paint.mStyle == Paint.Style.FILL_AND_STROKE){
     		graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
     		graphics2d.fillRoundRect((int)rect.left, (int)rect.top, (int)rect.width(), (int)rect.height(), (int)rx, (int)rx);
     		graphics2d.drawRoundRect((int)rect.left, (int)rect.top, (int)rect.width(), (int)rect.height(), (int)rx, (int)rx);
     	}
        
    }

 /**
     * Draw the specified round-rect using the specified paint. The roundrect will be filled or
     * framed based on the Style in the paint.
     *
     * @param rx The x-radius of the oval used to round the corners
     * @param ry The y-radius of the oval used to round the corners
     * @param paint The paint used to draw the roundRect
     */
    public void drawRoundRect(float left, float top, float right, float bottom, float rx, float ry,
             Paint paint) {
    	graphics2d.setColor(paint.mColor);
    	if(paint.isAntiAlias()){
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	}
    	else{
    		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    	}
        if(paint.mStyle == Paint.Style.FILL){
        	graphics2d.fillRoundRect((int)left, (int)top, (int)(right-left), (int)(bottom-top), (int)rx, (int)rx);
    	}
    	else if(paint.mStyle == Paint.Style.STROKE){
    		graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
    		graphics2d.drawRoundRect((int)left, (int)top, (int)(right-left), (int)(bottom-top), (int)rx, (int)rx);
    	}
    	else if(paint.mStyle == Paint.Style.FILL_AND_STROKE){
    		graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
    		graphics2d.fillRoundRect((int)left, (int)top, (int)(right-left), (int)(bottom-top), (int)rx, (int)rx);
    		graphics2d.drawRoundRect((int)left, (int)top, (int)(right-left), (int)(bottom-top), (int)rx, (int)rx);
    	}
    }

   /**
     * Draw the text, with origin at (x,y), using the specified paint. The origin is interpreted
     * based on the Align setting in the paint.
     *
     * @param text The text to be drawn
     * @param x The x-coordinate of the origin of the text being drawn
     * @param y The y-coordinate of the baseline of the text being drawn
     * @param paint The paint used for the text (e.g. color, size, style)
     */
    public void drawText( String text, float x, float y,  Paint paint) {
    	if(paint.isAntiAlias()){
    		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    	}
    	else{
    		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    	}
    	graphics2d.setColor(paint.mColor);
    	graphics2d.setFont(paint.mFont);
        graphics2d.drawString(text, x, y);
    }

public void dispose() {
	graphics2d.dispose();
	
}

public void drawPoint(int x, int y, Paint paint) {
	if(paint.isAntiAlias()){
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	else{
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
//    if(paint.mStyle == Paint.Style.FILL){
//    	graphics2d.fillRect((int)left, (int)top, (int)(right-left), (int)(bottom-top));
//	}
//	else if(paint.mStyle == Paint.Style.STROKE){
//		graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
//		graphics2d.drawRect((int)left, (int)top, (int)(right-left), (int)(bottom-top));
//	}
//	else if(paint.mStyle == Paint.Style.FILL_AND_STROKE){
//		graphics2d.setStroke(new BasicStroke(paint.mStrokeWidth));
//		graphics2d.drawRect((int)left, (int)top, (int)(right-left), (int)(bottom-top));
//		graphics2d.fillRect((int)left, (int)top, (int)(right-left), (int)(bottom-top));
//	}
	graphics2d.setColor(paint.mColor);
	graphics2d.drawLine(x, y, x, y);
	
}




    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
