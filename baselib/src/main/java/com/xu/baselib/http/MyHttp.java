package com.xu.baselib.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by Administrator on 2017/11/13.
 */
public final class MyHttp {

    private static Context mContext = null;

    private static RequestQueue mQueue;

    private static MyHttp myHttp;

    private String mApiDomain ;
    
    private IHttpParams requestParams = new HttpBaseParams();

    public static MyHttp getSingle(Context context){

        if (myHttp == null){
            myHttp = new MyHttp(context);
        }

        return myHttp;
    }

    public MyHttp(Context context) {

        mContext = context;

        mQueue = Volley.newRequestQueue(mContext);

        // TODO: 2017/11/13 //在res/value/string中放入域名
//        mApiDomain = context.getString(R.string.app_name);
        mApiDomain = "http://qfg.lvmengren.com/index.php/";
    }


    public void get(String url, Map<String,String> params,Class<?> c,HttpCallBackListener<?> listener){

        request(Request.Method.GET,url,null,params,c,listener);
    }

    public void post(String url, Map<String, String> params, Class<?> c, HttpCallBackListener<?> listener){
        request(Request.Method.POST, url, null, params, c, listener);
    }


    private void request(int method, String url, Map<String, String> headers, Map<String, String> params, Class<?> c, HttpCallBackListener<?> listener) {

        mQueue.add(new JiamengRequest(method,getAbsoluteUrl(url),requestParams.getHeaders(headers),requestParams.getParams(params), c, listener));

    }

    private String getAbsoluteUrl(String relativeUrl){
        if (relativeUrl.startsWith("http:")) {
            return relativeUrl;
        }
        String url = mApiDomain + relativeUrl;
        return url;
    }

}
