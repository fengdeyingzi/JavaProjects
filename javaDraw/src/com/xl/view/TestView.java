package com.xl.view;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class TestView extends View{

	public TestView(Context context) {
		super(context);
		initView();
	}
	
	private void initView(){
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(getContext().getResources().getAssets().open("icon.png"));
			canvas.drawBitmap(bitmap, 20, 20, null);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		super.onDraw(canvas);
		
	}

}
