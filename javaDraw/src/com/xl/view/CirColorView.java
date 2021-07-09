package com.xl.view;


import android.view.View;

import com.xl.util.ColorUtil;
import com.xl.util.DisplayUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
//import android.graphics.Canvas;
//import com.xl.game.tool.DisplayUtil;
//import android.graphics.Paint;
//import android.content.res.TypedArray;
//import com.xl.opmrcc.tool.ColorUtil;
//import android.widget.Toast;
//import com.xl.opmrcc.ThemeColor;
import android.view.MotionEvent;
//import android.view.KeyEvent;

/*
圆形colorView

 setAntiAlias: 设置画笔的锯齿效果。 
 setColor: 设置画笔颜色 
 setARGB:  设置画笔的a,r,p,g值。 
 setAlpha:  设置Alpha值 
 setTextSize: 设置字体尺寸。 
 setStyle:  设置画笔风格，空心或者实心。 
 setStrokeWidth: 设置空心的边框宽度。 
 getColor:  得到画笔的颜色 
 getAlpha:  得到画笔的Alpha值。 
 // 设置paint的风格为“空心”  
 // 当然也可以设置为"实心"(Paint.Style.FILL)  
 mPaint.setStyle(Paint.Style.STROKE);  

 // 设置“空心”的外框的宽度  
 mPaint.setStrokeWidth(5);  

*/
public class CirColorView extends View
{
	int color,color_cir;
	int cir_size;
	int padding;
	Paint paint_cir,paint_color;
	Paint paint_accent;
	Paint paint_focus;
	boolean isFocus;
	boolean isDown;
	int default_w,default_h;
	View.OnClickListener listener;
	
	public CirColorView(Context context)
	{
		super(context);
		initView();
	}
	
	private void initView()
	{
		setMinimumWidth(DisplayUtil.dip2px(getContext(),32));
		setMinimumHeight(DisplayUtil.dip2px(getContext(),32));
		this.paint_cir = new Paint();
		paint_accent = new Paint();
		paint_focus = new Paint();
		paint_accent.setAntiAlias(true);
		paint_focus.setAntiAlias(true);
		paint_focus.setColor(ColorUtil.getAlphaColor(0xff60a0f0,128));
		paint_accent.setColor(0xff60a0f0);
		this.paint_cir.setStyle(Paint.Style.STROKE);
		paint_accent.setStyle(Paint.Style.STROKE);
		this.paint_color = new Paint();
		this.paint_cir.setColor(getColorControlNormal());
		this.paint_cir.setAntiAlias(true);
		this.paint_color.setAntiAlias(true);
		this.default_w= DisplayUtil.dip2px(getContext(),32);
		this.default_h=default_w;
		//光标
		this.setFocusable(true);
	 //setFocusInTouchMode()
		this.setFocusableInTouchMode(true);
	}
	
	



	//设置外圆颜色
	public void setCirColor(int color)
	{
		this.color_cir = color;
		this.paint_cir.setColor(color);
		invalidate();
	}
	//设置外圆轮廓大小
	public void setCirSize(int size)
	{
		this.cir_size = size;
		this.paint_cir.setStrokeWidth(size);
		this.paint_accent.setStrokeWidth(size);
		invalidate();
	}
	//设置颜色
	public void setColor(int color)
	{
		this.color = color;
		this.paint_color.setColor(color);
		int color1 = getColorControlNormal();
		int color2 = ColorUtil.getAlphaColor(color,50);
		
		this.paint_cir.setColor( ColorUtil.mixColor(color1,color2));
		invalidate();
	}

	public void setPadding(int padding)
	{
		this.padding = padding;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		
		super.onDraw(canvas);
		System.out.println("视图宽高："+getWidth()+" "+getHeight());
		int width = Math.min(getWidth(),getHeight());
		//圆环
	if(isFocus)
		canvas.drawCircle(((float)getWidth())/2,((float)getHeight())/2, width/2-padding-this.cir_size,paint_focus);
	else
		canvas.drawCircle(((float)getWidth())/2,((float)getHeight())/2,width/2-padding-this.cir_size,paint_cir);
	if(isDown)
		canvas.drawCircle(((float)getWidth())/2,((float)getHeight())/2,width/2-padding-this.cir_size,paint_accent);
	
		canvas.drawCircle(((float)getWidth())/2,((float)getHeight())/2,width/2-this.cir_size*2-padding,paint_color);
	}
	
  


	//获取colorAccept
	int getColorAccent()
	{
//		int defaultColor = 0xFF000000;  
//		int[] attrsArray = {android.support.v7.appcompat.R.attr.colorAccent};  
//		TypedArray typedArray = getContext().obtainStyledAttributes(attrsArray);  
//		int accentColor = typedArray.getColor(0, defaultColor);  
//
//// don't forget the resource recycling  
//		typedArray.recycle();  
//		return accentColor;
		return 0xff60a0f0;
	}

	//获取控件元件预设颜色
	int getColorControlNormal()
	{
		return 0xff909090; //return getColorAttr(android.support.v7.appcompat.R.attr.colorControlNormal);
	}
	
	/*
	焦点控制
	*/
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) 
	{
		/*
	if(previouslyFocusedRect!=null)
	Toast.makeText(getContext(),"onFocusChanged "+previouslyFocusedRect.left+" "+previouslyFocusedRect.top,0).show();
	else
	{
		//Toast.makeText(getContext(),""+gainFocus+" "+direction,0).show();
	}
	*/
	this.isFocus = gainFocus;
	super.onFocusChanged(gainFocus,direction,previouslyFocusedRect);
	invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		System.out.println("touch "+event.getAction()+" "+event.getX()+" "+event.getY());
	switch(event.getAction())
	{
		case MotionEvent.ACTION_DOWN:
		//requestFocusFromTouch();
		this.isDown=true;
		invalidate();
			break;
		case MotionEvent.ACTION_UP:
		this.isDown=false;
		if(listener!=null)listener.onClick(this);
		invalidate();
			break;
		case MotionEvent.ACTION_CANCEL:
			this.isDown=false;
			
			invalidate();
			//clearFocus();
			break;
	}
	return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode== KeyEvent.KEYCODE_DPAD_CENTER)
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
		{
			if(listener!=null)listener.onClick(this);
			invalidate();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	
	
	
	public void setOnClickListener(View.OnClickListener listener)
	{
		this.listener = listener; 
	}
	
	/** 
	 * 比onDraw先执行 
	 * <p> 
	 * 一个MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求。 
	 * 一个MeasureSpec由大小和模式组成 
	 * 它有三种模式：UNSPECIFIED(未指定),父元素部队自元素施加任何束缚，子元素可以得到任意想要的大小;
	 * EXACTLY(完全)，父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小； 
	 * AT_MOST(至多)，子元素至多达到指定大小的值。
	 * <p> 
	 * 它常用的三个函数： 
	 * 1.static int getMode(int measureSpec):根据提供的测量值(格式)提取模式(上述三个模式之一) 
	 * 2.static int getSize(int measureSpec):根据提供的测量值(格式)提取大小值(这个大小也就是我们通常所说的大小) 
	 * 3.static int makeMeasureSpec(int size,int mode):根据提供的大小值和模式创建一个测量值(格式) 
	 */ 
	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
	super.onMeasure(widthMeasureSpec, heightMeasureSpec); 
	final int minimumWidth = getSuggestedMinimumWidth(); 
	final int minimumHeight = getSuggestedMinimumHeight(); 
	//Log.e("YView", "---minimumWidth = " + minimumWidth + ""); 
	//Log.e("YView", "---minimumHeight = " + minimumHeight + ""); 
	int width = measureWidth(minimumWidth, widthMeasureSpec); 
	int height = measureHeight(minimumHeight, heightMeasureSpec); 
	setMeasuredDimension(width, height); 
	}

	private int measureWidth(int defaultWidth, int measureSpec) {

	int specMode = MeasureSpec.getMode(measureSpec); 
	int specSize = MeasureSpec.getSize(measureSpec); 
	//Log.e("YViewWidth", "---speSize = " + specSize + "");


	switch (specMode) { 
	case MeasureSpec.AT_MOST: //当设置为wrap_content时
		//表示子视图最多只能是specSize中指定的大小。（当设置为wrap_content时，模式为AT_MOST, 表示子view的大小最多是多少，这样子view会根据这个上限来设置自己的尺寸）
		defaultWidth = default_w+ getPaddingLeft() + getPaddingRight();

		//Log.e("YViewWidth", "---speMode = AT_MOST"); 
		break; 
	case MeasureSpec.EXACTLY: //当设置width或height为match_parent时
		//表示父视图希望子视图的大小应该是由specSize的值来决定的，系统默认会按照这个规则来设置子视图的大小，简单的说（当设置width或height为match_parent时，模式为EXACTLY，因为子view会占据剩余容器的空间，所以它大小是确定的） 
		//Log.e("YViewWidth", "---speMode = EXACTLY"); 
		defaultWidth = specSize; 
		break; 
	case MeasureSpec.UNSPECIFIED: //表示开发人员可以将视图按照自己的意愿设置成任意的大小，没有任何限制。这种情况比较少见，不太会用到。 
		//Log.e("YViewWidth", "---speMode = UNSPECIFIED"); 
		defaultWidth = default_w+ getPaddingLeft() + getPaddingRight();//Math.max(defaultWidth, specSize); 
	} 
	return defaultWidth; 
	}


	private int measureHeight(int defaultHeight, int measureSpec) {

	int specMode = MeasureSpec.getMode(measureSpec); 
	int specSize = MeasureSpec.getSize(measureSpec); 
	//Log.e("YViewHeight", "---speSize = " + specSize + "");

	switch (specMode) { 
	case MeasureSpec.AT_MOST: 
		defaultHeight = default_h; // (int) (-mPaint.ascent() + mPaint.descent()) + getPaddingTop() + getPaddingBottom(); 
		//Log.e("YViewHeight", "---speMode = AT_MOST"); 
		break; 
	case MeasureSpec.EXACTLY: 
		defaultHeight = specSize; 
		//Log.e("YViewHeight", "---speSize = EXACTLY"); 
		break; 
	case MeasureSpec.UNSPECIFIED: 
		defaultHeight =  default_w+ getPaddingLeft() + getPaddingRight();//Math.max(defaultHeight, specSize); 
		//Log.e("YViewHeight", "---speSize = UNSPECIFIED"); 
//        1.基准点是baseline 
//        2.ascent：是baseline之上至字符最高处的距离 
//        3.descent：是baseline之下至字符最低处的距离 
//        4.leading：是上一行字符的descent到下一行的ascent之间的距离,也就是相邻行间的空白距离 
//        5.top：是指的是最高字符到baseline的值,即ascent的最大值 
//        6.bottom：是指最低字符到baseline的值,即descent的最大值

		break; 
	} 
	return defaultHeight;


	} 
	
}
