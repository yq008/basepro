package com.yq008.basepro.applib;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yq008.basepro.applib.util.StatusBarUtil;
import com.yq008.basepro.applib.widget.autolayout.AutoConstraintLayout;
import com.yq008.basepro.applib.widget.autolayout.AutoFrameLayout;
import com.yq008.basepro.applib.widget.autolayout.AutoLinearLayout;
import com.yq008.basepro.applib.widget.autolayout.AutoRelativeLayout;
import com.yq008.basepro.applib.widget.TitleBar;
import com.yq008.basepro.applib.widget.dialog.MyDialog;
import com.yq008.basepro.http.extra.AbHttpActivity;
import com.yq008.basepro.util.AppActivityManager;
import com.yq008.basepro.widget.Toast;
import com.yq008.basepro.widget.dialog.BaseDialog;
import com.yq008.basepro.widget.dialog.DialogClickListener;


/**
 * 描述：继承BaseHttpActivity 可实现网络请求和加载网络图片
 *
 * @author Xiay .
 */
public abstract class AppActivity extends AbHttpActivity {
    protected int layoutId;
    public TitleBar titleBar;
    public AppActivity activity;
    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    private static final String LAYOUT_CONSTRAINTLAYOUT = "android.support.constraint.ConstraintLayout";


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_CONSTRAINTLAYOUT)) {
            view = new AutoConstraintLayout(context, attrs);
        }
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }

    protected void onCreate(Bundle paramBundle) {
        activity = this;
        super.onCreate(paramBundle);
        int statusBarColor = getStatusBarColor();
        initPageView();
        if (statusBarColor != 0) {
            StatusBarUtil.setColor(this, getResources().getColor(statusBarColor), 0);
            if (titleBar != null)
                titleBar.setBackgroundColor(getResources().getColor(statusBarColor));
        }
    }

    protected void initPageView() {
        if (layoutId != -1) {//如果是-1,说明是通过Binding方式设置的布局
            layoutId = setContentView();
            View view;
            if (layoutId != 0) {
                super.setContentView(layoutId);
                //  view = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            } else {
                view = setNewContentView();
                if (view != null)
                    super.setContentView(view);
            }
            // scaleView(view);
        }
        setPageTitle(setTitle(), setTitleTextColor());
    }


    /**
     * 为头部是 ImageView 的界面设置状态栏透明(使用默认透明度)
     *
     * @param needOffsetView 需要向下偏移的 View
     */
    public void setTranslucentForImageView(View needOffsetView) {
        StatusBarUtil.setTranslucentForImageView(this, 0, needOffsetView);
    }

    /**
     * 设置状态栏颜色
     */
    public abstract int getStatusBarColor();

    public abstract int setContentView();

    /**
     * 设置New出来的View
     */
    public View setNewContentView() {
        return null;
    }

    /**
     * 设置标题
     */
    public abstract String setTitle();

    /**
     * 设置标题文字颜色
     */
    public int setTitleTextColor() {
        return getResources().getColor(R.color.white);
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void setPageTitle(String title) {
        setPageTitle(title, -1);
    }

    protected void setPageTitle(String title, int textColor) {
        titleBar = findView(R.id.title_bar);
        if (titleBar != null) {
            if (title != null) {
                titleBar.setTitle(title);
            }
            if (textColor != -1) {
                titleBar.setTitleColor(textColor);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 固定文字大小，可选
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 退出应用时弹出提示对话框 可选
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isAddBackButton()) {
                ((MyDialog) getDialog()).showExit(new DialogClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppActivityManager.getInstance().AppExit(AppActivity.this);
                    }

                });
            } else {
                closeActivity();
            }

            return true;
        }
        return false;
    }



    //========================================= 以下实现代码为固定写法  =========================================
    @Override
    public BaseDialog getDialog() {
        return initDialog();
    }

    private BaseDialog initDialog() {
        if (dialog == null) {
            synchronized (AppActivity.this) {
                dialog = new MyDialog(this);
                dialog.setCanceledOnTouchOutside(false);
            }
        }
        return dialog;
    }

    public MyDialog getMyDialog() {
        return (MyDialog) initDialog();
    }




    /**
     * 跳转activity
     */
    public void openActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /**
     * 跳转activity
     */
    public void openActivity(Class clazz) {
        openActivity(new Intent(this, clazz));
    }

    /**
     * 跳转activity
     *
     * @param clazz
     * @param parmas 传值格式  key,value
     */

    public void openActivity(Class clazz, Object... parmas) {
        if (parmas != null) {
            Intent i = new Intent(this, clazz);
            int parmasLenth = parmas.length;
            if (parmasLenth % 2 != 0) {
                Toast.show("参数格式为key,value");
            } else {
                parmasLenth = parmasLenth / 2;
                for (int j = 0; j < parmasLenth; j++) {
                    Object parmas1 = parmas[j + j];
                    Object parmas2 = parmas[j + j + 1];
                    if (parmas1 instanceof String) {
                        String key = (String) parmas1;
                        if (parmas2 instanceof String)
                            i.putExtra(key, (String) parmas2);
                        else if (parmas2 instanceof Integer)
                            i.putExtra(key, (int) parmas2);
                        else if (parmas2 instanceof Boolean)
                            i.putExtra(key, (boolean) parmas2);
                        else if (parmas2 instanceof Parcelable)
                            i.putExtra(key, (Parcelable) parmas2);

                    } else {
                        Toast.show("参数key类型不对");
                        return;
                    }
                }
            }
            openActivity(i);
        }
    }

    /**
     * 跳转activity
     */
    public void openActivitys(Intent[] intents) {
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        startActivities(intents);
    }

    public void addBackButton(int imageResId) {
        if (titleBar != null) {
            titleBar.setLeftImageResource(imageResId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeActivity();
                }
            });
        }

    }

    /**
     * 跳转activity
     */
    public void openActivitys(Class... classes) {
        Intent[] intents = new Intent[classes.length];
        for (int i = 0; i < classes.length; i++) {
            Intent intent = new Intent(this, classes[i]);
            intents[i] = intent;
        }
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        startActivities(intents);
    }

    /**
     * 跳转activity
     */
    public void openActivityForResult(Class<?> cls, int requestCode) {
        openActivityForResult(new Intent(this, cls), requestCode);
    }

    /**
     * 跳转activity
     */
    public void openActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void closeActivity() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    /**
     * 替换Fragment
     *
     * @param viewId
     * @param fragment
     * @param isAddToBack
     */
    public void replace(int viewId, Fragment fragment, boolean isAddToBack) {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(viewId, fragment);
        if (isAddToBack)
            mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commitAllowingStateLoss();

    }

    /**
     * 替换Fragment
     *
     * @param viewId
     * @param fragment
     */
    public void replace(int viewId, Fragment fragment) {
        replace(viewId, fragment, false);
    }
    public abstract boolean isAddBackButton();
}
