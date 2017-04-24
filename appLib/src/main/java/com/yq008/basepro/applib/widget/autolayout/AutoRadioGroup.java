package com.yq008.basepro.applib.widget.autolayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.yq008.basepro.applib.util.autolayout.AutoLayoutHelper;
import com.yq008.basepro.util.autolayout.AutoLayoutInfo;

/**
 * Created by Administrator on 2017/4/17.
 */

public class AutoRadioGroup extends RadioGroup {
    private final AutoLayoutHelper mHelper = new AutoLayoutHelper(this);
    public AutoRadioGroup(Context context) {
        super(context);
    }

    public AutoRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (!isInEditMode())
            mHelper.adjustChildren();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new AutoRadioGroup.LayoutParams(getContext(), attrs);
    }


    public static class LayoutParams extends RadioGroup.LayoutParams
            implements AutoLayoutHelper.AutoLayoutParams
    {
        private AutoLayoutInfo mAutoLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs)
        {
            super(c, attrs);
            mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
        }

        @Override
        public AutoLayoutInfo getAutoLayoutInfo()
        {
            return mAutoLayoutInfo;
        }


        public LayoutParams(int width, int height)
        {
            super(width, height);
        }


        public LayoutParams(ViewGroup.LayoutParams source)
        {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source)
        {
            super(source);
        }

    }


}
