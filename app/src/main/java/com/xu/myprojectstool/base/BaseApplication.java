package com.xu.myprojectstool.base;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import org.xutils.x;

/**
 * Created by Administrator on 2017/11/13.
 */
public class BaseApplication extends Application{

    public static BaseApplication appContext;
    public static AppManager mAppManager;

    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init (this);
        appContext = this;
        mAppManager = AppManager.getInstance();

    }

    /**
     * 获得屏幕宽度
     *
     * @return 屏幕宽度
     */
    public static int getWidth() {
        WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获得屏幕高度
     *
     * @return 屏幕高度
     */
    public static int getHeight() {
        WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }
}
