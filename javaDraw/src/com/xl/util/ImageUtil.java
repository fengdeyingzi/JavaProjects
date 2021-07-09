package com.xl.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;

public class ImageUtil {
	
	//将图片压缩为指定宽高
	public static void zoomBitmap(Bitmap bitmap, int width,int height, String filename){
		System.out.println(""+width+" "+height+" "+filename+" "+bitmap.image.getType());
		Bitmap temp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//		temp.image = new BufferedImage(width, height, bitmap.image.getType());
		Canvas canvas = new Canvas(temp);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Rect src = new Rect(0,0, bitmap.getWidth(), bitmap.getHeight());
		Rect dst = new Rect(0,0, width, height);
		canvas.drawBitmap(bitmap, src,dst, paint);
		canvas.dispose();
		try {
			temp.compress(CompressFormat.PNG, 100, new FileOutputStream(new File(filename)));
//			ImageIO.write(temp.image,"png", new File("D:\\2.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//将图片按最大宽度压缩
	public static void zoomBitmapWidth(Bitmap bitmap, int width, String filename){
		int height = width * bitmap.getHeight()/bitmap.getWidth();
		if(width>=bitmap.getWidth()){
			width = bitmap.getWidth();
			height = bitmap.getHeight();
		}
		else{
			height = width * bitmap.getHeight()/bitmap.getWidth();
		}
		zoomBitmap(bitmap, width, height, filename);
	}
	
	//将图片按最大高度压缩
	public static void zoomBitmapHeight(Bitmap bitmap, int height, String filename){
		int width = height * bitmap.getWidth()/bitmap.getHeight();
		if(height>=bitmap.getHeight()){
			width = bitmap.getWidth();
			height = bitmap.getHeight();
		}
		else{
			width = height * bitmap.getWidth()/bitmap.getHeight();
		}
		zoomBitmap(bitmap, width, height, filename);
	}
	

}
