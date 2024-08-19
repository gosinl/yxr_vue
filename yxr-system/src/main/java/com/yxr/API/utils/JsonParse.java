/**
 * 对企业微信发送给企业后台的消息加解密示例代码.
 *
 * @copyright Copyright (c) 1998-2020 Tencent Inc.
 */

// ------------------------------------------------------------------------

package com.yxr.API.utils;

/**
 * 针对 org.json.JSONObject,
 * 要编译打包架包json
 * 官方源码下载地址 : https://github.com/stleary/JSON-java, jar包下载地址 : https://mvnrepository.com/artifact/org.json/json
 */

/**
 * JsonParse class
 *
 * 提供提取消息格式中的密文及生成回复消息格式的接口.
 */
class JsonParse {

	/**
	 * 生成json消息
	 * @param encrypt 加密后的消息密文
	 * @param signature 安全签名
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @return 生成的json字符串
	 */
	public static String generate(String encrypt, String signature, String timestamp, String nonce) {

		String format = "{\"echostr\":\"%1$s\",\"msgsignature\":\"%2$s\",\"timestamp\":\"%3$s\",\"nonce\":\"%4$s\"}";
		return String.format(format, encrypt, signature, timestamp, nonce);

	}

	public static String generate(String encrypt, String signature, String timestamp, String nonce, String corpid) {

		String format = "{\"echostr\":\"%1$s\",\"msgSignature\":\"%2$s\",\"timestamp\":\"%3$s\",\"nonce\":\"%4$s\",\"corpid\":\"%5$s\"}";
		return String.format(format, encrypt, signature, timestamp, nonce, corpid);

	}


}
