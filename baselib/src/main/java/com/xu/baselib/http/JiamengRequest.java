package com.xu.baselib.http;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.xu.baselib.Tool.Logs;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A request for retrieving a {@link HttpResult} response body at a given URL, allowing for an
 * optional {@link HttpResult} to be passed in as part of the request body.
 */
public class JiamengRequest extends Request<HttpResult> implements ErrorListener {
    private HttpResult result;
    private Class<?> t;


    /**
     * Default charset for JSON request.
     */
    protected static final String PROTOCOL_CHARSET = "utf-8";
    private final HttpCallBackListener mListener;
    private Map<String, String> parameters;
    private Map<String, String> headers;

    /**
     * Creates a new request.
     *
     * @param method     the HTTP method to use
     * @param url        URL to fetch the JSON from
     * @param headers    A {@link Map} to post with the request. Null is allowed and
     *                   indicates no headers will be posted along with request.
     * @param parameters A {@link Map} to post with the request. Null is allowed and
     *                   indicates no parameters will be posted along with request.
     * @param listener   Listener to receive the JSON response
     * @param c          A {@link HttpResult} data type.
     */
    public JiamengRequest(int method, String url, Map<String, String> headers, Map<String, String> parameters, Class<?> c, HttpCallBackListener<?> listener) {
        super(method, url, null);
        if (headers == null) {
            this.headers = new HashMap<>();
        } else {
            this.headers = headers;
        }
        mListener = listener;
        this.t = c;
        result = new HttpResult();
        this.parameters = parameters;
        Log.i("http", url + "\n headers " + headers + "\n parameters " + parameters);
    }

    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     *
     * @see # JiamengRequest(int, String, Map, Map, Listener, Class)
     */
    public JiamengRequest(String url, Map<String, String> headers, Map<String, String> parameters, Class<?> t, HttpCallBackListener<?> listener) {
        this(parameters == null ? Method.GET : Method.POST, url, headers, parameters, t, listener);
    }

    public JiamengRequest(String url, Map<String, String> parameters, Class<?> t, HttpCallBackListener<?> listener) {
        this(parameters == null ? Method.GET : Method.POST, url, null, parameters, t, listener);
    }

    @Override
    protected Response<HttpResult> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            result.text = jsonString;
            Log.i("http", getUrl() + "\n result = " + jsonString);
            result = JSON.parseObject(jsonString, HttpResult.class);
            if (result.data != null) {
                result.text = result.data.toString();
            }
            Log.i("http", " errmsg = " + result.msg);
            if (result.errcode == HttpResult.API_OK) {
                if (result.data instanceof JSONObject) {
                    result.data = JSON.parseObject(result.text, t);
                } else if (result.data instanceof JSONArray) {
                    result.data = JSON.parseArray(result.text, t);
                }
            }
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(HttpResult response) {
        if (mListener != null) {
            try {
                mListener.onBack(response);
            } catch (Exception e) {
                Logs.Debug("===error :====="+ e.getMessage());
            }
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        try {
            Log.w("http", getUrl() + " \n" + error.getMessage(), error.fillInStackTrace());
        } catch (Exception err) {
            err.printStackTrace();
        }
        if (error instanceof NetworkError) {
            result.errcode = HttpResult.NETWORK_ERROR;
            result.msg = "连接网络出错。";
        } else if (error instanceof ParseError) {
            result.errcode = HttpResult.PARSE_ERROR;
            result.msg = "数据解析出错。";
        } else if (error instanceof AuthFailureError) {
            result.errcode = HttpResult.AUTHFAILURE_ERROR;
            result.msg = "认证失败。";
        } else if (error instanceof ServerError) {
            result.errcode = HttpResult.SERVER_ERROR;
            result.msg = "服务器错误。";
        } else if (error instanceof TimeoutError) {
            result.errcode = HttpResult.TIMEOUT_ERROR;
            result.msg = "请求超时。";
        } else {
            result.errcode = HttpResult.UnknownException;
            result.msg = "其他未知异常。";
        }
        Log.i("http", "........errcode = " + result.errcode + "...errmsg = " + result.msg + "........");
        if (mListener != null) {
            try {
                mListener.onBack(result);
            } catch (Exception e) {
                Logs.Debug("====error :====="+ e.getMessage());
            }
        }
    }


    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {

        return parameters;
    }

    @Override
    public void onErrorResponse(VolleyError arg0) {
        // no thing, see deliverError.
    }


}
