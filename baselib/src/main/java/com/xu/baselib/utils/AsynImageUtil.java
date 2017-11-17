package com.xu.baselib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xu.baselib.Tool.Logs;

public class AsynImageUtil {
    private static Context mContext;

    public static void display(String url, ImageView iv) {
        Glide.with(mContext)
                .load(url)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv);
    }

    public static void display(String url, ImageView iv, int drawable) {
        Glide.with(mContext)
                .load(url)
                .placeholder(drawable)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv);
    }

    public static void display(String url, final ImageView iv, final SimpleTarget<GlideDrawable> target){
        Glide.with(mContext)
                .load(url)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        iv.setImageDrawable(glideDrawable);
                        target.onResourceReady(glideDrawable,glideAnimation);
                    }
                });
    }


    public static void display(int resourceId, ImageView iv) {
        Glide.with(mContext)
                .load(resourceId)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv);
    }


    public static void display(final String url, final ImageView iv,int defalut , ListView listView){
        Glide.with(mContext)
                .load(url)
                .crossFade()
                .placeholder(defalut)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv);
    }

    public static void displayBase64(ImageView iv, String pic,Activity activity) {
        byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int screenWidth=dm.widthPixels;
        if(decodedByte.getWidth()<=screenWidth){
            iv.setImageBitmap(decodedByte);
        }else{
            Bitmap bmp=Bitmap.createScaledBitmap(decodedByte, screenWidth / 5, screenWidth / 5, true);
            iv.setImageBitmap(decodedByte);
        }
        iv.setImageBitmap(decodedByte);
    }

    /**
     * 初始化
     * @param context
     */
    public static void init(Context context) {
        mContext = context ;
    }

    /**
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitamp(Drawable drawable)
    {

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Logs.Debug("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w,h,config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }
}
