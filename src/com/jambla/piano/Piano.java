package com.jambla.piano;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class Piano extends LinearLayout {
	public static interface OnItemSelectedListener{
		public void onSelectedChanged(int index);
	}
	/*
	 * 已激活组件索引
	 */
	private int activeIndex;
	/*
	 * 所有子组件位置
	 */
	private List<PianoLocation> mPL ;
	/*
	 * 父组件是否垂直布局
	 */
	private boolean isVertical;
	/*
	 * 子布局ID
	 */
//	private int mPianoID;
	/*
	 * 子布局个数
	 */
//	private int mPianoCount;
	/*
	 * 子布局宽度
	 */
	private int mWidth;
	/*
	 * 子布局高度
	 */
	private int mHeight;
	/*
	 * 子布局间距
	 */
	private int mMargin;
	/*
	 * 子布局隐藏大小
	 */
	private int mHidden;
	/*
	 * 子布局动画时间
	 */
	private int mDuration;
	
	private OnItemSelectedListener onItemSelectedListener; 
	
	public void setOnItemSelectedListener(OnItemSelectedListener l){
		this.onItemSelectedListener = l;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}
	public Piano(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public Piano(Context context, AttributeSet attrs) {
		super(context, attrs);
		isVertical = getOrientation()==LinearLayout.VERTICAL?true:false;
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.piano);
//		mPianoID = array.getResourceId(R.styleable.piano_piano, 0);
		mWidth = (int) array.getDimension(R.styleable.piano_width, 0);
		mHeight = (int) array.getDimension(R.styleable.piano_height, 0);
		mMargin = (int) array.getDimension(R.styleable.piano_margin, 0);
		mHidden = (int) array.getDimension(R.styleable.piano_hidden, 0);
		mDuration = array.getInteger(R.styleable.piano_duration, 0);
		activeIndex = -1;
		array.recycle();
		
	}

	public Piano(Context context) {
		super(context);
	}
	
	/*
	 * 添加子组件
	 */
	public void addPiano(View piano){
		LayoutParams layoutParams = new LayoutParams(mWidth, mHeight);
		if(isVertical){
			layoutParams.setMargins(-mHidden, mMargin, mMargin, mMargin);
		}else{
			layoutParams.setMargins(mMargin, mMargin, mMargin, -mHidden);
		}
		piano.setLayoutParams(layoutParams);
		addView(piano);
	}
	
	
	/*
	 * 确定所有子组件位置
	 */
	private void getAllPianoLoc(){
		if(mPL!=null){
			return ;
		}
		mPL = new ArrayList<PianoLocation>();
		int count = getChildCount();
		for(int i = 0;i<count;i++){
			View piano = getChildAt(i);
			mPL.add(new PianoLocation(piano.getLeft(), piano.getTop(), piano.getRight(), piano.getBottom()));
		}
	}
	
	/*
	 * 1. 获取所有子组件的位置信息
	 * 2. 根据触摸位置确定是否触摸到子组件上，若没有则直接返回
	 * 3. 查看触摸子组件是否已经处于激活状态，若是则直接返回
	 * 4. 调用组件恢复动画使上一次激活组件回到初始状态
	 * 5. 确定要激活组件
	 * 6. 调用组件激活动画 
	 * 7. 返回激活组件索引
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		getAllPianoLoc();
		int touch = (int) (isVertical?event.getY():event.getX());
		int touchIndex = getIndexOfPiano(touch);
		if(touchIndex!=-1&&touchIndex!=activeIndex){
				recoverAnimation(activeIndex);
				activeIndex = touchIndex;
				activeAnimation(activeIndex);
				if(onItemSelectedListener!=null){
					onItemSelectedListener.onSelectedChanged(activeIndex);
				}
		}
		return true;
	}
	
	/*
	 * 组件恢复动画
	 */
	private void recoverAnimation(int index){
		View child = getChildAt(index);
		if(child!=null){
		TranslateAnimation animation = null;
		if(isVertical){
			animation = new TranslateAnimation(mHidden, 0, 0, 0);
		}else{
			animation = new TranslateAnimation(0, 0, -mHidden, 0);
		}
		animation.setDuration(mDuration);
		animation.setFillAfter(true);
		
		child.startAnimation(animation);
		}
	}
	/*
	 * 組件激活动画
	 */
	private void activeAnimation(int index){
		View child = getChildAt(index);
		TranslateAnimation animation = null;
		if(isVertical){
			animation = new TranslateAnimation(0, mHidden, 0, 0);
		}else{
			animation = new TranslateAnimation(0, 0, 0, mHidden);
		}
		animation.setDuration(mDuration);
		animation.setFillAfter(true);
		child.startAnimation(animation);
	}
	
	/*
	 * 获取触摸组件索引
	 */
	private int getIndexOfPiano(int touch){
		for(int i = 0;i<mPL.size();i++){
			PianoLocation pl = mPL.get(i);
			if(isVertical){
				if(touch>=pl.getTop()&&touch<=pl.getBottom()){
					return i;
				}
			}else{
				if(touch>=pl.getLeft()&&touch<=pl.getRight()){
					return i;
				}
			}
		}
		return -1;
	}
	
	/*
	 * 子组件位置确定
	 */
	private class PianoLocation{
		private int left;
		private int top;
		private int right;
		private int bottom;
		public int getLeft() {
			return left;
		}
		public int getTop() {
			return top;
		}
		public int getRight() {
			return right;
		}
		public int getBottom() {
			return bottom;
		}
		public PianoLocation(int left, int top, int right, int bottom) {
			super();
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}
		
		
	}
	
	

}
