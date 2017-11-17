package com.xu.baselib.http;

import java.util.Map;

/**
 * Created by roy on 2015/8/27.
 */
public interface IHttpParams {
    Map<String, String> getParams(Map<String, String> params);

    Map<String, String> getHeaders(Map<String, String> headers);
}
