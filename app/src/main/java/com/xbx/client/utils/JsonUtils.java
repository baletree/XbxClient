package com.xbx.client.utils;

import com.alibaba.fastjson.JSON;

/**
 * json管理
 * 
 * @author Administrator
 * 
 */
public class JsonUtils {
	public static <T> T object(String json, Class<T> classOfT) {
		return JSON.parseObject(json, classOfT);
	}
	public static <T> String toJson(Class<T> param) {
		return JSON.toJSONString(param);
	}
}
