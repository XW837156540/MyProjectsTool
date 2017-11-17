package com.xu.baselib.http;

public interface HttpCallBackListener<T> {
	void onBack(HttpResult<T> httpResult);
}
