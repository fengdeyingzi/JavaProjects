package com.xl.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;

import javax.swing.JComponent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;



public class DrawView extends JComponent{

	int x,y;
	Color cir_color;
	BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_4BYTE_ABGR);
	
	
   public DrawView() {
	   image.setRGB(20, 20, 0x303030);
	   image.setRGB(20, 21, 0xf03030);
	   image.setRGB(20, 22, 0xf03030);
	   image.setRGB(20, 23, 0xf03030);
		// TODO Auto-generated constructor stub
	   this.x= 100;
	   this.y=100;
	   this.cir_color= new Color(255, 0, 0);
	   addMouseListener(new MouseListener() {
		
		 public void mousePressed(MouseEvent e){
		        System.out.println("你已经压下鼠标按钮");
		        if(getLineSize(x, y, e.getX(), e.getY())<100){
		        	cir_color= Color.BLACK;
		        }
		        
		        repaint();
		    }
		    public void mouseReleased(MouseEvent e){
		        System.out.println("你已经放开鼠标按钮");
if(getLineSize(x, y, e.getX(), e.getY())<100){
		        	cir_color= Color.RED;
		        }
		        
		        repaint();
		    }
		    public void mouseEntered(MouseEvent e){
		        System.out.println("鼠标光标进入按钮");
		        repaint();
		    }
		    public void mouseExited(MouseEvent e){
		        System.out.println("鼠标光标离开按钮");
		        repaint();
		    }
		    public void mouseClicked(MouseEvent e){
		        System.out.println("你已经按下按钮");
		        repaint();
		    }
	});
	}
   
   //判断两点距离
 //求两点之间距离 可用于计算圆与圆的碰撞(当两个圆的圆心距离大于它们的半径只和，那么碰撞成功)
 	public static double getLineSize(double x,double y,double x2,double y2)
 	{

 		return Math.sqrt( (x2-x)*(x2-x)+ (y2-y)*(y2-y));
 	}
 	
 	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D graphics2d= (Graphics2D) g;
		Paint paint= new Paint() {
			
			public int getTransparency() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
					Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		//graphics2d.setPaint(paint);
		
		//画点
//		graphics2d.drawLine(50, 1, 50, 1);
		
		//画线
//		graphics2d.drawLine(50, 50, 60, 100);
		
		//画矩形
//		graphics2d.fillRect(50, 50, 30, 30);
		
		//画空心矩形
//		graphics2d.drawRect(0, 0, 20, 20);
		
		//
		graphics2d.drawRoundRect(40, 40, 30, 30, 20, 20);
		graphics2d.setColor(cir_color);
		//画圆
//		graphics2d.fillOval(x, y, 20, 20);
//		//画椭圆
//		graphics2d.fillOval(120, 80, 80, 29);
		
		//画圆弧
		/*
		画圆弧有两个方法：
		drawArc(int x,int y,int width,int height,int startAngle, int arcAngle)：画椭圆一部分的圆弧线。椭圆的中心是它的外接矩形的中心，其中参数是外接矩形的左上角坐标(x,y)，宽是width，高是heigh。参数startAngle的单位是 “度”，起始角度0度是指3点钟方位.参数startAngle和arcAngle表示从startAngle角度开始，逆时针方向画arcAngle度的弧，约定，正值度数是逆时针方向，负值度数是顺时针方向，例如-90度是6点钟方位。
		fillArc(int x,int y,int width, int height, int startAngle, int arcAngle)：用setColor()方法设定的颜色,画着色椭圆的一部分。
		*/
//		graphics2d.fillArc(160, 100, 80, 80, 0, 180);
		
		//画文字
		Font font =  new Font("", Font.PLAIN, 20); // 创建字体对象)
		android.graphics.Paint p = new android.graphics.Paint();
		graphics2d.setFont(font);
//		graphics2d.drawString("this is draw test", 0, 20);
//		Bitmap bitmap = BitmapFactory.decodeFile("E:\\Picture\\动图大师\\icon.png");
		Canvas canvas = new Canvas(graphics2d);
		Rect src = new Rect(0,0,300,30);
		Rect dst = new Rect(0,0,144,144);
		Typeface typeface = Typeface.createFromFile(new File("D:\\AndroidStudioProjects\\flutterbyrhyme\\fonts\\qlYouYuan.ttf"));
		
		typeface = Typeface.createFromName("宋体");
		p.setTypeface(typeface);
		p.setTextSize(33);
		p.setColor(0xff30d030);
//		canvas.drawText("测试影子haha", 30, 300, p);
//		canvas.drawCircle(30, 30, 30, p);
		Bitmap bitmap2 = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
		for(int ix = 0;ix<bitmap2.getWidth();ix++){
			for(int iy=0;iy<bitmap2.getHeight();iy++){
				bitmap2.setPixel(ix, iy, 0xff5090f0);
				bitmap2.image.setRGB(255, 30, 30);
//				System.out.println(""+ix+" "+iy);
			}
		}
//		canvas.drawBitmap(bitmap2, 300, 300, p);
		
		graphics2d.drawImage(image, 0,0,null);
		
//		canvas.drawBitmap(bitmap,0,0,p);
		//画图片
		//graphics2d.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer)
		/*
		6. 画多边形
		多边形是用多条线段首尾连接而成的封闭平面图。多边形线段端点的x坐标和y坐标分别存储在两个数组中，画多边形就是按给定的坐标点顺序用直线段将它们连起来。以下是画多边形常用的两个方法：
		drawPolygon(int xpoints[],int yPoints[],int nPoints)：画一个多边形
		fillPolygon(int xPoints[],int yPoints[],int nPoints)：用方法setColor()设定的颜色着色多边形。其中数组xPoints[]存储x坐标点，yPoints[]存储y坐标点，nPoints是坐标点个数。

		注意，上述方法并不自动闭合多边形，要画一个闭合的多边形，给出的坐标点的最后一点必须与第一点相同.以下代码实现填充一个三角形和画一个八边形。
		*/
		
		
	}
}
