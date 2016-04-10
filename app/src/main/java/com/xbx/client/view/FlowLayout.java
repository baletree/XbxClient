package com.xbx.client.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目:AndroidHigherExcercise
 * 作者：Hi-Templar
 * 创建时间：2015/12/25 14:03
 * 描述：自定义流式布局
 */
public class FlowLayout extends ViewGroup {

    /**
     * 当new控件是，传一个参数
     *
     * @param context
     */
    public FlowLayout(Context context) {
        this(context, null);
    }

    /**
     * 布局文件书写控件，但没有自定义属性
     *
     * @param context
     * @param attrs
     */
    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 布局文件书写控件，但使用自定义属性
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 存储所有的view（分行存储）
     */
    private List<List<View>> mAllViews=new ArrayList<>();

    /**
     * 每一行高度
     */
    private List<Integer> mLineHeight=new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();
        //当前viewGroup的宽度
        int width=getWidth();

        int lineWidth=0;
        int lineHeight=0;

        List<View> lineViews=new ArrayList<>();

        int cCount=getChildCount();
        for (int i = 0; i <cCount; i++) {
            View child=getChildAt(i);
            MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();
            int childWidth=child.getMeasuredWidth();
            int childHeight=child.getMeasuredHeight();

            //如果需要换行
            if (childWidth+lineWidth+lp.rightMargin+lp.leftMargin>width-getPaddingLeft()-getPaddingRight()){
                //记录lineheight
                mLineHeight.add(lineHeight);
                //记录当前行的view
                mAllViews.add(lineViews);
                //重置行宽 行高
                lineWidth = 0;
                lineHeight=childHeight+lp.topMargin+lp.bottomMargin;
                //重置view集合
                lineViews=new ArrayList<>();
            }
            lineWidth=childWidth+lineWidth+lp.rightMargin+lp.leftMargin;
            lineHeight=Math.max(lineHeight,childHeight+lp.bottomMargin+lp.topMargin);

            lineViews.add(child);
        }//for end
        //处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        //设置子view的位置
        int left=getPaddingLeft();
        int top=getPaddingTop();
        int lineNum=mAllViews.size();
        for (int i = 0; i < lineNum; i++) {
            //当前行所有views
            lineViews=mAllViews.get(i);

            lineHeight=mLineHeight.get(i);

            for (int j = 0; j <lineViews.size() ; j++) {
                View child=lineViews.get(j);
                //判断child的状态
                if (child.getVisibility()==View.GONE) continue;
                MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();

                int lc=left+lp.leftMargin;
                int tc=top+lp.topMargin;
                int rc=lc+child.getMeasuredWidth();
                int bc=tc+child.getMeasuredHeight();

                //为子view布局
                child.layout(lc,tc,rc,bc);

                left+=child.getMeasuredWidth()+lp.rightMargin+lp.leftMargin;
            }

            left=getPaddingLeft();
            top+=lineHeight;
        }
    }

    /**
     * @param widthMeasureSpec  包含测量模式和测量值
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getSize(heightMeasureSpec);

        //wrap_content
        int width = 0;
        int height = 0;
        //记录每一行的宽高
        int lineHeight = 0;
        int lineWidth = 0;

        //得到内部元素个数
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            //测量子view的宽高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到layoutparams
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //子view占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            //子view占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //换行
            if (lineWidth + childWidth > sizeWidth-getPaddingLeft()-getPaddingRight()) {
                //对比得到最大宽度
                width = Math.max(width, lineWidth);
                //重置linewidth
                lineWidth = childWidth;
                //记录行高
                height += lineHeight;
                lineHeight = childHeight;
            }else{//未换行
                //叠加行宽
                lineWidth+=childWidth;
                //对比获得当前行最大行高
                lineHeight=Math.max(lineHeight,childHeight);
            }
            //到达最后一个控件
            if (i==cCount-1){
                width=Math.max(lineWidth,width);
                height+=lineHeight;
            }
        }

        //wrap_content
        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY?sizeWidth:width+getPaddingRight()+getPaddingLeft(),
                modeHeight== MeasureSpec.EXACTLY?sizeHeight:height+getPaddingBottom()+getPaddingTop());
        Log.v("Tag","height:"+height);


//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * 与当前viewgroup对应的layoutparams
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
