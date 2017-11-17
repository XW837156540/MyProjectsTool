package com.xu.baselib.Tool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.functions.Action1;


/**
 * Created by scorpio on 2016/11/3.
 */

public class Tools {

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    /**
     * 获取时间戳
     * */
    public static long getUNIX() {
        return new Date().getTime();
    }
    public static String currentVersion(Context context, String appPackage) {
        String ver = null;
        try {
            appPackage = context.getPackageName();
            ver = context.getPackageManager().getPackageInfo(appPackage, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ver;
    }

    /**
     * 获取软件版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取手机的IMEI
     *
     * @param context
     * @return
     */
    static String deviceId = "";

    public static String getIMEI(final Context context) {

        if (!Tools.checkoutPermissions(context, new String[]{Manifest.permission.READ_PHONE_STATE})) {
            RxPermissions.getInstance(context).request(Manifest.permission.READ_PHONE_STATE).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    if (aBoolean) {
                        deviceId = ((TelephonyManager) context
                                .getSystemService(Context.TELEPHONY_SERVICE))
                                .getDeviceId();
                    }
                }
            });
        } else {
            deviceId = ((TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE))
                    .getDeviceId();
        }
        return deviceId;
    }

    /**
     * 判断null和""的情况
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str != null) {
            if (str.length() > 0 && !"null".equals(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 整形转字符串
     */
    public static String int2string(int s) {
        try {
            return Integer.toString(s);
        } catch (Exception e) {
            // TODO: handle exception
            return "";
        }
    }

    /**
     * 字符串转整形
     */
    public static int string2int(String s, int defvalue) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            // TODO: handle exception
            return defvalue;
        }
    }

    /**
     * 双精度转字符串
     */
    public static String double2string(double s) {
        try {
            return Double.toString(s);
        } catch (Exception e) {
            // TODO: handle exception
            return "";
        }
    }

    /**
     * 字符串 转双精度
     */
    public static double string2double(String s, double defvalue) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            // TODO: handle exception
            return defvalue;
        }
    }

    /**
     * object转整形
     */
    public static int obj2int(Object s, int defvalue) {
        try {
            return Integer.parseInt(s.toString());
        } catch (Exception e) {
            // TODO: handle exception
            return defvalue;
        }
    }

    /**
     * object转双精度
     */
    public static double obj2double(Object s, double defvalue) {
        try {
            return Double.parseDouble(s.toString());
        } catch (Exception e) {
            // TODO: handle exception
            return defvalue;
        }
    }

    /**
     * object转string
     */
    public static String obj2string(Object s, String defvalue) {
        try {
            return s.toString();
        } catch (Exception e) {
            // TODO: handle exception
            return defvalue;
        }
    }

    /**
     * 数字保留小数点后两位
     */
    public static String valueFormat(Object price) {
        try {
            double dou = string2double(price.toString(), 0);
            if (dou == 0) {
                return "0.00";
            }
            DecimalFormat df2 = new DecimalFormat("##0.00");// 保留2位
            return df2.format(dou);
        } catch (Exception e) {
            // TODO: handle exception
            return "0.00";
        }

    }

    /**
     * 数字保留小数点后两位
     *
     * @param rule
     */
    public static String valueFormat(Object price, String rule) {
        String format = "##0.00";
        if (!Tools.isEmpty(rule)) {
            format = rule;
        }
        try {
            double dou = string2double(price.toString(), 0);
            if (dou == 0) {
                return "0.00";
            }
            DecimalFormat df2 = new DecimalFormat(format);// 保留2位
            return df2.format(dou);
        } catch (Exception e) {
            // TODO: handle exception
            return "0.00";
        }

    }

    /**
     * 不保留小数点
     */
    public static String noPointFormat(Object price) {
        try {
            double dou = string2double(price.toString(), 0);
            if (dou == 0) {
                return "0";
            }
            DecimalFormat df2 = new DecimalFormat("##0");// 保留2位
            return df2.format(dou);
        } catch (Exception e) {
            // TODO: handle exception
            return "0";
        }

    }

    /**
     * 把字符串转为字符数组/
     *
     * @param str
     * @return
     */

    public static String[] StrToArray(String str) {
        String[] str_array = new String[str.length()];
        for (int cnt = 0; cnt < str.length(); cnt++) {
            if (cnt < str.length() - 1) {
                str_array[cnt] = str.substring(cnt, cnt + 1);
            } else {
                str_array[cnt] = str.substring(cnt);
            }

        }
        return str_array;
    }

    /**
     * Base64加密
     *
     * @param xml
     * @return
     */
    @SuppressLint("NewApi")
    public static String encodeBase64(String xml) {
        return Base64.encodeToString(xml.getBytes(), Base64.DEFAULT);
    }

    /**
     * Base64解密
     *
     * @param rexml
     * @return
     */
    @SuppressLint("NewApi")
    public static String decodeBase64(String rexml) {
        byte b[] = Base64.decode(rexml, Base64.DEFAULT);
        String reString = new String(b);
        return reString;
    }

    /**
     * 明文转url编码
     */
    public static String encode(String strRes) {
        return URLEncoder.encode(strRes);
    }

    /**
     * 将url编码转明文
     */
    public static String decode(String strRes) {
        return URLDecoder.decode(strRes);
    }

    /**
     * 验证是否为邮箱
     */
    public static boolean isEmail(String str) {
        Pattern pattern = Pattern
                .compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为手机号
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 验证是否为固定电话
     */
    public static boolean isPhone(String str) {
        Pattern pattern = Pattern.compile("^(\\d{4}|\\d{3}|\\d{2}|\\d{0})-?\\d{7,8}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 验证是否为邮编
     */
    public static boolean isPostCode(String str) {
        Pattern pattern = Pattern.compile("^[1-9]\\d{5}$");
        return pattern.matcher(str).matches();
    }


    /**
     * 设置cookie
     *
     * @param context
     * @param url
     * @param t
     */
    public static void setCookie(Context context, String url, String t) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, t);
        CookieSyncManager.getInstance().sync();

    }

    /**
     * 判断是否是中国移动
     * 唯一地识别移动客户所属的国家，我国为460；MNC为网络id，由2位数字组成，
     * 用于识别移动客户所归属的移动网络，中国移动为00，中国联通为01,中国电信为03；MSIN为移动客户识别码，采用等长11位数字构成。
     *
     * @param context
     * @return
     */
    public static boolean isChianMM(Context context) {
        boolean isChina = false;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId();
        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                isChina = true;
            }
        } else {
            isChina = false;
        }
        return isChina;
    }

    /**
     * 检测网络是否有效
     *
     * @param context
     * @return ture 有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取当前网络状态
     *
     * @return 1:无线网  2：手机网络   3：无网络连接
     */
    public static int getNetState(Context context) {
        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED == mobileState) {
            // 手机网络连接成功
            return 2;
        } else if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED != mobileState) {
            // 手机没有任何的网络
            return 3;
        } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
            // 无线网络连接成功
            return 1;
        }
        return 3;
    }

    /**
     * 匹配用户名：只能为数字、字母或者汉字,4-20
     */
    public static boolean CheckUsername(String str) {
//			^[0-9a-zA-Z\u4e00-\u9fa5]{4,20}$

//			^[a-zA-Z|\\d|\\u0391-\\uFFE5]*$
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z\u4e00-\u9fa5]{4,20}$");
        return pattern.matcher(str).matches();
    }


    /**
     * 匹用户名长度
     */
    public static boolean CheckUsernameLength(String str) {

        if (str.length() < 4 || str.length() > 20) {

            return false;
        } else {
            return true;
        }
    }

    public static String getNowTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long date = new Date().getTime();
        return sdf.format(date);
    }

    /**
     * 获取当前日期
     *
     * @param isTime true：返回yyyy-MM-dd HH:mm:ss格式，      false：返回yyyy-MM-dd格式
     */
    public static String getDate(boolean isTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    /**
     * 获取当前日期
     */
    public static String GetDate() {
        Date dt = new Date();
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        String s = matter1.format(dt);
        return s;
    }

    /**
     * 获取时间 年月 周
     *
     * @return
     */
    public static String getWeekStr(int day) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK) + day);
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "周" + mWay;
    }

    /**
     * 把分钟换算为天,小时,分钟的方法
     * 4天23小时42分钟以前
     *
     * @param time
     * @return
     */
    public static String getDetailTime(int time) {
        int day = time / 1440;
        int hour = (time % 1440) / 60;
        int min = (time % 1440) % 60;
        String detailTime;
        if (day == 0 && hour == 0 && min == 0) {
            detailTime = "刚刚";
            return detailTime;
        }
        if (day == 0 && hour == 0) {
            detailTime = min + "分钟前";
            return detailTime;
        } else if (day == 0) {
            detailTime = hour + "小时前";
            return detailTime;
        } else if (day == 1) {
            detailTime = "昨天" + hour + ":" + min;
            return detailTime;
        } else if (day == 2) {
            detailTime = "前天" + hour + "小时" + min + "分钟前";
            return detailTime;
        } else if (day > 2) {
            detailTime = day + " " + hour + "-" + min;
            return detailTime;
        }
        return null;
    }

    /**
     * 计算两个时间相差的分钟数
     * time1-需要计算的时间，time2-当前时间
     *
     * @return
     */
    public static String getTimeMinuteDiffer(String time1, String time2) {
        int time = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//时间形式

        try {
            long date = (format.parse(time1)).getTime();
            long now = (format.parse(time2)).getTime();

            time = (int) ((now - date) / (1000 * 60));
            time = Math.abs(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int day = time / 1440;
        int hour = (time % 1440) / 60;
        int min = (time % 1440) % 60;
        String detailTime;
        String[] sellerTime = time1.split(" ");
        String passTime = "未知时间";
        if (sellerTime.length >= 2) {
            passTime = sellerTime[1];
        }
        if (day == 0 && hour == 0 && min == 0) {
            detailTime = "刚刚";
            return detailTime;
        }
        if (day == 0 && hour == 0) {
            detailTime = min + "分钟前";
            return detailTime;
        }
        if (day == 0) {
            detailTime = hour + "小时前";
            return detailTime;
        }
        if (day == 1) {
            detailTime = "昨天 " + passTime.substring(0, 5);
            return detailTime;
        }
        if (day == 2) {
            detailTime = "前天 " + passTime.substring(0, 5);
            return detailTime;
        }

        if (day > 2) {
            detailTime = time1.substring(0, 16);
            return detailTime;
        }
        return null;
    }

    /**
     * 根据参数计算毫秒数
     *
     * @param d 天数
     * @param h 小时
     * @param m 分钟
     * @param s 秒
     */
    public static long getMillis(int d, int h, int m, int s) {
        return d * 86400000l + h * 3600000l + m * 60000l + s * 1000l;
    }

    /**
     * 获取距离显示规范
     *
     * @return
     */
    public static String getDistanceNorm(double distance) {
        String disStr = "";
        distance = distance / 10;
        if (distance > 1) {
            if (distance > 500) {
                disStr = ">500KM";
            } else {
                disStr = valueFormat(distance, "##0.0") + "KM";
            }
        } else {
            if (distance * 10 < 1 || distance == 0) {
                //小于100M
                disStr = "<100M";
            } else {
                disStr = (int) (distance * 1000) + "M";
            }
        }
        return disStr;
    }

    /**
     * 发送短信
     *
     * @param context
     * @param to
     * @param content
     */
    public static void sendSms(Context context, String to, String content) {
        Uri uri = Uri.parse("smsto:" + to);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", content);
        context.startActivity(it);
    }

    /**
     * 打电话
     *
     * @param context
     * @param phone
     */
    public static void tel(Context context, String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent it = new Intent(Intent.ACTION_CALL, uri);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(it);
    }

    /**
     * 获取手机状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    // 获取ActionBar的高度
    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))// 如果资源是存在的、有效的
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    // 获取ActionBar的宽度
    public static int getActionBarWidth(Context context) {
        TypedValue tv = new TypedValue();
        int actionBarWidth = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))// 如果资源是存在的、有效的
        {
            actionBarWidth = context.getResources().getDisplayMetrics().widthPixels;
        }
        return actionBarWidth;
    }

    /**
     * 根据包名 和类名 打开应用程序
     *
     * @param pkg
     * @param cls
     */
    public static void startapp(Context context, String pkg, String cls) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName componentName = new ComponentName(pkg, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(componentName);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            intent = context.getPackageManager().getLaunchIntentForPackage(pkg);
            try {
                context.startActivity(intent);
            } catch (Exception e1) {
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }


    /**
     * 权限 6.0 检测权限
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean checkoutPermissions(final Context context, String[] permissions) {
        // 进行权限请求
        boolean result = true;
        int count = 0;
        for (int i = 0; i < permissions.length; i++) {
            final String permission = permissions[i];
            if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                count = count + 1;
            }
        }
        if (count == permissions.length) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }
    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredHeight(View view) {
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }

    /**
     * 测量控件的尺寸
     *
     * @param view
     */
    public static void calcViewMeasure(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }

    /**
     * 将图片路径和图片尺寸进行md5加密并作为图片名称
     */
    public static String imgCacheName(String imgUrl, int imgWidth, int imgHeight) {
        String url = MD5.getMD5(imgUrl + imgWidth + "_" + imgHeight)
                + imgUrl.substring(imgUrl.lastIndexOf(".")); // 将图片路径和图片尺寸进行md5加密并作为图片名称
        return url;
    }

    /**
     * 返回app通用的存储路径
     *
     * @return
     */
    public static String appSavePath(Context context) {
        String path = null;
        if (isSdOk()) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName() + "/";
        }
        return path;
    }

    public static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 判断sd卡是否存在
     */

    public static boolean isSdOk() {
        boolean sdCardExist = Environment.MEDIA_MOUNTED
                .equals(Environment.getExternalStorageState());
        return sdCardExist;
    }

    /**
     * @param view
     * @param zoom          true 缩小  透明    false 扩大，不透明
     * @param Duration      动画开始到结束的时间
     * @param alphaValue    变化后的透明值（百分比）
     * @param zoomX         变化后宽度（百分比）
     * @param zoomY         变化后高度（百分比）
     * @param displacementX 变化后相对于宽度，偏移X（百分比）
     * @param displacementY 变化后相对于高度，偏移Y（百分比）
     */
    public static void animationUser(View view, boolean zoom, int Duration, float alphaValue, float zoomX, float zoomY, float displacementX, float displacementY, Boolean rotat, float rotatstartAngle, float rotatAngle) {
        AnimationSet animationSet = new AnimationSet(true);
        if (zoom) {
            //透明
            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, alphaValue);
            alphaAnimation.setDuration(Duration);
            animationSet.addAnimation(alphaAnimation);
            //宽高
            ScaleAnimation scaleAnimation = new ScaleAnimation(1f, zoomX, 1f, zoomY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(Duration);
            animationSet.addAnimation(scaleAnimation);
            //位移
            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, displacementX, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, displacementY);
            translateAnimation.setDuration(Duration);
            animationSet.addAnimation(translateAnimation);
            //旋转
            if (rotat) {
                RotateAnimation rotateAnimation = new RotateAnimation(rotatstartAngle, rotatAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(Duration);
                animationSet.addAnimation(rotateAnimation);
            }
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(alphaValue, 1f);
            alphaAnimation.setDuration(Duration);
            animationSet.addAnimation(alphaAnimation);
            ScaleAnimation scaleAnimation = new ScaleAnimation(zoomX, 1f, zoomY, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(Duration);
            animationSet.addAnimation(scaleAnimation);
            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, displacementX, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, displacementY, Animation.RELATIVE_TO_SELF, 0f);
            translateAnimation.setDuration(Duration);
            animationSet.addAnimation(translateAnimation);
            //旋转
            if (rotat) {
                RotateAnimation rotateAnimation = new RotateAnimation(rotatstartAngle, rotatAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(Duration);
                animationSet.addAnimation(rotateAnimation);
            }
        }
        animationSet.setFillAfter(true);
        view.startAnimation(animationSet);
    }

    public static void getFilesNames(File directory) {
        File f = new File("/storage/emulated/0/oa");
        Logs.Debug("fileName=============1");
        if (f != null && f.exists() && f.isDirectory()) {
            for (File item : f.listFiles()) {
                Logs.Debug("fileName=============" + item.getName() + "====" + item.getAbsolutePath() + "===");
            }
        }
    }

    public static void animationUser(View view, int Duration, int alphaValueStart, int alphaValueEnd) {
        AnimationSet animationSet = new AnimationSet(true);
        //透明
        AlphaAnimation alphaAnimation = new AlphaAnimation(alphaValueStart, alphaValueEnd);
        alphaAnimation.setDuration(Duration);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setFillAfter(true);
        view.startAnimation(animationSet);
    }
    public static Bitmap activityShot(Activity activity) {
         /*获取windows中最顶层的view*/
        try {


            View view = activity.getWindow().getDecorView().getRootView();

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


        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }


    /**
     * 清楚应用所以的缓存数据
     * @param context
     */
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
    /**
     * 判断是否在Wifi网络状态下
     * @param
     * @return
     */
    public boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

}
