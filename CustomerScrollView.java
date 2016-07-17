package net.oschina.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 可以拖动的ScrollView
 *
 */
public class CustomerScrollView extends ScrollView {

	private static final int size = 4;
	private static final String TAG ="CustomerScrollView" ;
	private View inner;
	private float y;
	private Rect normal = new Rect();



	public CustomerScrollView(Context context) {
		super(context);
	}

	public CustomerScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {

		// 获取scrollView 中的第一个儿子， 实际上scrollview也只能有一个儿子
		if (getChildCount() > 0) {
			inner = getChildAt(0);

			Log.d(TAG, "onFinishInflate: "+inner.getTag());
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (inner == null) {
			return super.onTouchEvent(ev);
		} else {
			commOnTouchEvent(ev);
		}
		return super.onTouchEvent(ev);
	}

	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			y = ev.getY();
			break;
		case MotionEvent.ACTION_UP:
			// 只要前面的矩形Rect 有值，那么这里直接走动画了。
			if (isNeedAnimation()) {
				// Log.v("mlguitar", "will up and animation");
				animation();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			final float preY = y; // 按下的Y
			float nowY = ev.getY(); //现在的Y ，移动的Y
			/**
			 * size=4 表示 拖动的距离为屏幕的高度的1/4
			 */

			//按下的Y - 移动的Y /4  ： 意思是指， 跨度4个像素，那么只认为移动了1像素
			int deltaY = (int) (preY - nowY) / size;
			// 滚动
			// scrollBy(0, deltaY);

			y = nowY;

			// 判断我们是否需要自己处理控件的移动
			if (isNeedMove()) {

				/*
					允许滑动后，在这里先判断早前的rect 矩形是否没有设置

					初次进来，这里一定是empty.

					接着使用rect 矩形，记录住我们的inner的四个角

				 */
				if (normal.isEmpty()) {

					Log.d(TAG, "commOnTouchEvent:y= "+y);
					Log.d(TAG, "commOnTouchEvent:preY =  "+preY);
					Log.d(TAG, "commOnTouchEvent:deltaY= "+deltaY);
					normal.set(inner.getLeft(), inner.getTop(),
							inner.getRight(), inner.getBottom());
					return;
				}

				// 顶部位置 - 移动的距离 这里表示 ： 重新计算新的位置.
				int yy = inner.getTop() - deltaY;

				// 移动布局
				inner.layout(inner.getLeft(), yy, inner.getRight(),
						inner.getBottom() - deltaY);
			}
			break;
		default:
			break;
		}
	}

	public void animation() {



		// 让内部的view执行动画效果
		TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
				normal.top);
		ta.setDuration(200);
		inner.startAnimation(ta);

		inner.layout(normal.left, normal.top, normal.right, normal.bottom);

		normal.setEmpty();
	}



	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	/**
	 * 判断是否需要移动
	 * @return
	 */
	public boolean isNeedMove() {

		/*
			inner.getMeasuredHeight() : scrollview的内部孩子高度，说白了就是scrollView 的内容高度
			getHeight :　 scrollView 的高度。
		 */

		int offset = inner.getMeasuredHeight() - getHeight();

//		800 1200
		// 屏幕左上角的Y坐标
		int scrollY = getScrollY();
		Log.d(TAG, "isNeedMove: scrollY="+scrollY);

		/*

			scrollY == 0 : 当前处于0位置， 默认初始位置
			scrollY == offset :  当前屏幕左上角的Y坐标，正好是差值的位置。表明滑动到底部了。

			因为有这部分的差值在上面的屏幕了。

			那么其实这个if的判断就是 处于 开始顶部或者底部的时候，才允许执行move操作。

			其他情况，不允许走我们的move逻辑。

		 */

		if (scrollY == 0 || scrollY == offset) {
			return true;
		}
		return false;
	}

}