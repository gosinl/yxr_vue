/**
 * 对企业微信发送给企业后台的消息加解密示例代码.
 *
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

/**
 * 针对org.apache.commons.codec.binary.Base64，
 * 需要导入架包commons-codec-1.9（或commons-codec-1.8等其他版本）
 * 官方下载地址：http://commons.apache.org/proper/commons-codec/download_codec.cgi
 */
package com.yxr.API.utils;


import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;
//import java.util.Random;

/**
 * 提供接收和推送给企业微信消息的加解密接口(UTF8编码的字符串).
 * <ol>
 * 	<li>第三方回复加密消息给企业微信</li>
 * 	<li>第三方收到企业微信发送的消息，验证消息的安全性，并对消息进行解密。</li>
 * </ol>
 * 说明：异常java.security.InvalidKeyException:illegal Key Size的解决方案
 * <ol>
 * 	<li>在官方网站下载JCE无限制权限策略文件（JDK7的下载地址：
 *      http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html</li>
 * 	<li>下载后解压，可以看到local_policy.jar和US_export_policy.jar以及readme.txt</li>
 * 	<li>如果安装了JRE，将两个jar文件放到%JRE_HOME%\lib\security目录下覆盖原来的文件</li>
 * 	<li>如果安装了JDK，将两个jar文件放到%JDK_HOME%\jre\lib\security目录下覆盖原来文件</li>
 * </ol>
 */
public class WXBizJsonMsgCrypt {
	static Charset CHARSET = Charset.forName("utf-8");
	Base64 base64 = new Base64();
	byte[] aesKey;
	String token;
	String receiveid;

	/**
	 * 构造函数
	 * @param token 企业微信后台，开发者设置的token
	 * @param encodingAesKey 企业微信后台，开发者设置的EncodingAESKey
	 * @param receiveid, 不同场景含义不同，详见文档
	 *
	 * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
	 */
	public WXBizJsonMsgCrypt(String token, String encodingAesKey, String receiveid) throws  AesException {
		if (encodingAesKey.length() != 43 ) {
			throw new AesException( AesException.IllegalAesKey);
		}

		this.token = token;
		this.receiveid = receiveid;
		aesKey = Base64.decodeBase64(encodingAesKey + "=");
	}

	// 生成4个字节的网络字节序
	byte[] getNetworkBytesOrder(int sourceNumber) {
		byte[] orderBytes = new byte[4];
		orderBytes[3] = (byte) (sourceNumber & 0xFF);
		orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
		orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
		orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
		return orderBytes;
	}

	// 还原4个字节的网络字节序
	int recoverNetworkBytesOrder(byte[] orderBytes) {
		int sourceNumber = 0;
		for (int i = 0; i < 4; i++) {
			sourceNumber <<= 8;
			sourceNumber |= orderBytes[i] & 0xff;
		}
		return sourceNumber;
	}

	// 随机生成16位字符串
	String getRandomStr() {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 16; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 对明文进行加密.
	 *
	 * @param text 需要加密的明文
	 * @return 加密后base64编码的字符串
	 * @throws AesException aes加密失败
	 */
	String encrypt(String randomStr, String text) throws  AesException {
		ByteGroup byteCollector = new ByteGroup();
		byte[] randomStrBytes = randomStr.getBytes(CHARSET);
		byte[] textBytes = text.getBytes(CHARSET);
		byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);
		byte[] receiveidBytes = receiveid.getBytes(CHARSET);

		// randomStr + networkBytesOrder + text + receiveid
		byteCollector.addBytes(randomStrBytes);
		byteCollector.addBytes(networkBytesOrder);
		byteCollector.addBytes(textBytes);
		byteCollector.addBytes(receiveidBytes);
		// ... + pad: 使用自定义的填充方式对明文进行补位填充
		byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
		byteCollector.addBytes(padBytes);

		// 获得最终的字节流, 未加密
		byte[] unencrypted = byteCollector.toBytes();

		try {
			// 设置加密模式为AES的CBC模式
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

			// 加密
			byte[] encrypted = cipher.doFinal(unencrypted);
			// 使用BASE64对加密后的字符串进行编码
			String base64Encrypted = base64.encodeToString(encrypted);
			return base64Encrypted;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.EncryptAESError);
		}
	}

	/**
	 * 对密文进行解密.
	 *
	 * @param text 需要解密的密文
	 * @return 解密得到的明文
	 * @throws AesException aes解密失败
	 */
	String decrypt(String text) throws AesException {
		byte[] original;
		try {
			// 设置解密模式为AES的CBC模式
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
			cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);
			// 使用BASE64对密文进行解码
			byte[] encrypted = Base64.decodeBase64(text);
			// 解密
			original = cipher.doFinal(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.DecryptAESError);
		}

		String jsonContent, from_receiveid;
		try {
			// 去除补位字符
			byte[] bytes = PKCS7Encoder.decode(original);

			// 分离16位随机字符串,网络字节序和receiveid
			byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

			int jsonLength = recoverNetworkBytesOrder(networkOrder);

			jsonContent = new String(Arrays.copyOfRange(bytes, 20, 20 + jsonLength), CHARSET);
			from_receiveid = new String(Arrays.copyOfRange(bytes, 20 + jsonLength, bytes.length),
					CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.IllegalBuffer);
		}

		// receiveid不相同的情况
		if (!from_receiveid.equals(receiveid)) {
			throw new AesException(AesException.ValidateCorpidError);
		}
		return jsonContent;

	}

	/**
	 * 将企业微信回复用户的消息加密打包.
	 * <ol>
	 * 	<li>对要发送的消息进行AES-CBC加密</li>
	 * 	<li>生成安全签名</li>
	 * 	<li>将消息密文和安全签名打包成json格式</li>
	 * </ol>
	 *
	 * @param replyMsg 企业微信待回复用户的消息，json格式的字符串
	 * @param timeStamp 时间戳，可以自己生成，也可以用URL参数的timestamp
	 * @param nonce 随机串，可以自己生成，也可以用URL参数的nonce
	 *
	 * @return 加密后的可以直接回复用户的密文，包括msg_signature, timestamp, nonce, encrypt的json格式的字符串
	 * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
	 */
	public String EncryptMsg(String replyMsg, String timeStamp, String nonce) throws AesException {
		// 加密
		String encrypt = encrypt(getRandomStr(), replyMsg);

		// 生成安全签名
		if (timeStamp == "") {
			timeStamp = Long.toString(System.currentTimeMillis());
		}

		String signature = SHA1.getSHA1(token, timeStamp, nonce, encrypt.replace("\r\n",""));

		// 生成发送的json
		String result = JsonParse.generate(encrypt, signature, timeStamp, nonce);
		return result;
	}

	public String EncryptMsg(String replyMsg, String timeStamp, String nonce, String corpid)  {
		try {
			// 加密
			String encrypt = encrypt(getRandomStr(), replyMsg);

			// 生成安全签名
			if (timeStamp == "") {
				timeStamp = Long.toString(System.currentTimeMillis());
			}

			String signature = SHA1.getSHA1(token, timeStamp, nonce, encrypt.replace("\r\n",""));

			// 生成发送的json
			String result = JsonParse.generate(encrypt, signature, timeStamp, nonce, corpid);
			return result;
		}catch (AesException e){
			return "";
		}

	}
	// 加密获得对象
	public JSONObject EncryptMsgObj(String replyMsg, String timeStamp, String nonce, String corpid)  {
		try {
			// 加密
			String encrypt = encrypt(getRandomStr(), replyMsg);

			// 生成安全签名
			if (timeStamp == "") {
				timeStamp = Long.toString(System.currentTimeMillis());
			}

			String signature = SHA1.getSHA1(token, timeStamp, nonce, encrypt.replace("\r\n",""));

			// 生成发送的json
			String result = JsonParse.generate(encrypt, signature, timeStamp, nonce, corpid);
			return JSONObject.parseObject(result);
		}catch (AesException e){
			return null;
		}

	}
	/**
	 * 检验消息的真实性，并且获取解密后的明文.
	 * <ol>
	 * 	<li>利用收到的密文生成安全签名，进行签名验证</li>
	 * 	<li>若验证通过，则提取json中的加密消息</li>
	 * 	<li>对消息进行解密</li>
	 * </ol>
	 *
	 * @param msgSignature 签名串，对应URL参数的msg_signature
	 * @param timeStamp 时间戳，对应URL参数的timestamp
	 * @param nonce 随机串，对应URL参数的nonce
	 * @param postData 密文，对应POST请求的数据
	 *
	 * @return 解密后的原文
	 * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
	 */
	public String DecryptMsg(String msgSignature, String timeStamp, String nonce, String postData)
			throws AesException {

		// 验证安全签名
		String signature = SHA1.getSHA1(token, timeStamp, nonce, postData);

		// 和URL中的签名比较是否相等
		if (!signature.equals(msgSignature)) {
			throw new AesException(AesException.ValidateSignatureError);
		}

		// 解密
		String result = decrypt(postData);
		return result;
	}

	/**
	 * 验证URL
	 * @param msgSignature 签名串，对应URL参数的msg_signature
	 * @param timeStamp 时间戳，对应URL参数的timestamp
	 * @param nonce 随机串，对应URL参数的nonce
	 * @param echoStr 随机串，对应URL参数的echostr
	 *
	 * @return 解密之后的echostr
	 * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
	 */
	public String VerifyURL(String msgSignature, String timeStamp, String nonce, String echoStr)
			throws AesException {
		String signature = SHA1.getSHA1(token, timeStamp, nonce, echoStr);

		if (!signature.equals(msgSignature)) {
			throw new AesException(AesException.ValidateSignatureError);
		}

		String result = decrypt(echoStr);
		return result;
	}

//	public static void main(String[] args) throws Exception {
////		String msgsignature = "7e4585ee3c88bd164448c603528e2c6f82c8c59b";
////		String timestamp = "1668586575885";
////		String nonce = "9298972193";
////		String sReqData = "WNMTvXLOB1hbF8IiFKp2tUMMQ7RgizgMzZEVy27hNeMq95f3/ng8mxVM/JSA+ac4reW1QvqQZBSin9l1VwDoM3t8J0JxQeYOZqC4pycxzYTsZCxKYX/IB6ek9bl7m/vcR8EAaWzWSMEZFK1Z7LuJcr6FA77R+Y3a+/7g9dzKJHcKhPeBo4qeksh87fhEpWMaWvjYx8MpH/ZBSP/b/lCLGyZkaQDEzIc6LXn/S1lPy7uzZQ1GPYopi/ARlj6BvhwM3YK4eCGmXcmod4VU14hNoqapsXVzYUW/5wEkloJF/5+WJfKe/WiroFa2mJr0a4+gx5E6bZVpGFNA4rtBWxGFX1vM178NkV9ZZYjhcdRPyh/qCBQitcZ8y6jBFKD0n1aCh1YXVU8RW4CmOeIF203MNC5VqMa5lTHVtyollDQh2jPxaERKVlpEXkJFtmARHDX8JSJLRP7JIMN6aLDc5xJ0fiiUZREu4odJ0ASgGjj0FIHZYI7OCmgTTNuJ3e9j5MDHQxjLIZ48uhl8PmsA3VgnboCRxdLatK4HEy2W+axGX3Y=";
////		String sReqData = "";
////		String sReqData = "";
////		String sReqData = "";
//
//		String sToken = "6wwQg6tu";
//		String sCorpID = "wxc69883266d2d5dd5";
//		String sEncodingAESKey = "uet7kdaDr7LPkJyFGJTJuBS5bov5qswBlgFge0wQuNY";
//		WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(sToken, sEncodingAESKey, sCorpID);
//		try {
//
////			 String decrypt = wxcpt.DecryptMsg( msgsignature ,timestamp , nonce , sReqData);
//			String decrypt = wxcpt.DecryptMsg( msgsignature ,timestamp , nonce , sReqData);
//			System.out.println(decrypt);
//
//		} catch (Exception e) {
//			// TODO
//			// 解密失败，失败原因请查看异常
//			e.printStackTrace();
//		}
//	}


//	public static void main(String[] args) throws Exception {
////token    corpid     encodingAesKey 需要由我方提供
//		String sToken = "6wwQg6tu";
//		String sCorpID = "wxc69883266d2d5dd5";
//		String sEncodingAESKey = "uet7kdaDr7LPkJyFGJTJuBS5bov5qswBlgFge0wQuNY";
//		WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(sToken, sEncodingAESKey, sCorpID);
////sRespData 需要替换成需要传的业务数据
//		String sRespData = "{\"unionid\":\"610145687\" }";
//		try {
////RandomUtil.randomNumbers(10)采用hutool 的随机生成十位数
////Long.toString(System.currentTimeMillis()) 生成时间戳
////sRespData  将下方参数说明以json形式生成
//			String sEncryptMsg = wxcpt.EncryptMsg(sRespData,
//					Long.toString(System.currentTimeMillis()), RandomUtil.randomNumbers(10));
//			System.out.println( sEncryptMsg);
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//
//	}


}
