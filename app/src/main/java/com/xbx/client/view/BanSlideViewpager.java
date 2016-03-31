package com.xbx.client.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by EricYuan on 2016/3/30.
 */
public class BanSlideViewpager extends ViewPager {

    private boolean scrollble=true;

    public BanSlideViewpager(Context context) {
        super(context);
    }

    public BanSlideViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return true;
        }
        return false;
    }
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }
    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
}
