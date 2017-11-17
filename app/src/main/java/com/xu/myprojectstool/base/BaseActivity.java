package com.xu.myprojectstool.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Administrator on 2017/11/13.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public BaseActivity mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        BaseApplication.mAppManager.putActivity(mContext.getComponentName().getClassName(), mContext);

        int resId = 0;
        if (0 != (resId = setResouceLayoutId())) {
            setContentView(resId);
        }

        initView();
        initData();
        setListener();
    }

    /**
     * 绑定布局
     *
     * @return 布局id
     */
    protected abstract int setResouceLayoutId();

    /**
     * 找控件
     */
    protected abstract void initView();

    /**
     * 加载数据
     */
    protected abstract void initData();

    /**
     * 设置监听器
     */
    protected abstract void setListener();


    protected <V extends View> V findView(int id) {
        //noinspection unchecked
        return (V) mContext.findViewById(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.mAppManager.removeActivity(mContext.getComponentName().getClassName());
    }
}
