package com.xu.baselib.Tool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/17.
 */
public class UITools {

    private static Toast strShorttoast; // 短，字符串提示
    private static Toast strLongtoast; // 长，字符串提示
    public static String cookieUrl = ".9ji.com";


     /**
     * 短 字符串提示
     */
    public static void toastShowShort(Context context, String mes) {
        if (Tools.isEmpty(mes)){
            return;
        }
        if (strShorttoast != null) {
            strShorttoast.setText(mes);
            strShorttoast.setDuration(Toast.LENGTH_SHORT);
        } else {
            strShorttoast = Toast.makeText(context, mes, Toast.LENGTH_SHORT);
        }
        strShorttoast.show();
    }

    /**
     * 长 字符串提示
     */
    public static void toastShowLong(Context context, String mes) {
        if (Tools.isEmpty(mes)){
            return;
        }
        if (strLongtoast != null) {
            strLongtoast.setText(mes);
            strLongtoast.setDuration(Toast.LENGTH_SHORT);
        } else {
            strLongtoast = Toast.makeText(context, mes, Toast.LENGTH_SHORT);
        }

        strLongtoast.show();
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */

    public static Bitmap readBitMap(Context context, int resId) {

        BitmapFactory.Options opt = new BitmapFactory.Options();

        opt.inPreferredConfig = Bitmap.Config.RGB_565;

        opt.inPurgeable = true;

        opt.inInputShareable = true;

        //  获取资源图片

        InputStream is = context.getResources().openRawResource(resId);

        return BitmapFactory.decodeStream(is, null, opt);

    }

    public static Bitmap activityShot(Activity activity) {
         /*获取windows中最顶层的view*/
        View view = activity.getWindow().getDecorView();

        //允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        //获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        WindowManager windowManager = activity.getWindowManager();

        //获取屏幕宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        //去掉状态栏
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeight, width,
                height - statusBarHeight);

        //销毁缓存信息
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }
    /*
    图片变字节流

     */

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /*
    得到一个  "yyyy-MM-dd HH:mm:ss" 格式的字符串
        format:"yyyy-MM-dd HH:mm:ss"
     */
    public static String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static String getTimestamp(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date();
        String timestamp = formatter.format(date);
        return timestamp;
    }

    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }
}
