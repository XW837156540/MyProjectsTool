package com.xu.baselib.http;

/**
 * HTTP callback result
 *
 * @version 1.0
 */
public class HttpResult<T> {

    public T data;
    public int errcode;
    public String msg;
    public String text;
    public String extdata;

    public static final int INVALID = -2;
    public static final int HTTP_NONE = -1;
    public static final int API_OK = 200;
    public static final int HTTP_MOVED_PERMANENTLY = 301;
    public static final int HTTP_FOUND = 302;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;


    public static final int NETWORK_ERROR = 10007;
    public static final int PARSE_ERROR = 10008;
    public static final int AUTHFAILURE_ERROR = 10001;
    public static final int SERVER_ERROR = 10003;
    public static final int TIMEOUT_ERROR = 30010;

    public static final int Exception = 10011;
    public static final int ClientProtocolException = 10012;
    public static final int UnknownHostException = 10013;


    public static final int UnknownException = 1014;
    public static final int SC_NO_CONTENT = 1015;

    @Override
    public String toString() {
        return "HttpResult{" +
                "data=" + data +
                ", errcode=" + errcode +
                ", msg='" + msg + '\'' +
                ", text='" + text + '\'' +
                ", extdata='" + extdata + '\'' +
                '}';
    }
}
