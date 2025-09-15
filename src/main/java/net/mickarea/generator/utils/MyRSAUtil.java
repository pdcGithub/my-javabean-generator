/******************************************************************************************************

This file "MyRSAUtil.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

/**
 * 这是一个简易的 RSA 算法 加密处理工具。它由 pdc-common-tools 移植过来。至于为什么要移植，因为输出处理上，有些不同
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年9月15日
 */
public final class MyRSAUtil {

	/**
	 * 私有构造函数
	 */
	private MyRSAUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 默认密钥长度
	 */
	public static int KEY_LENGTH = 2048;
	
	/**
	 * 生成密钥对信息
	 * @return 一个简单的密钥对对象，包含公钥 和 私钥
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static KeyPair genKeys() throws Exception {
		//密钥对生成器：生成类型为 RSA 算法所需密钥对
		KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
		//随机种子处理
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(System.nanoTime());
		//生成器初始化
		gen.initialize(KEY_LENGTH, random);
		//生成
		KeyPair keyPair = gen.genKeyPair();
		//返回结果
		return keyPair;
	}
	
	/**
	 * 获取密钥对中的公钥信息字符串（以base64编码方式输出）
	 * @param keypair 密钥对
	 * @return 公钥信息字符串
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static String getPublicKey(KeyPair keypair) throws Exception {
		return byteToBase64(keypair.getPublic().getEncoded());
	}
	
	/**
	 * 获取密钥对中的私钥信息字符串（以base64编码方式输出）
	 * @param keypair 密钥对
	 * @return 私钥信息字符串
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static String getPrivateKey(KeyPair keypair) throws Exception {
		return byteToBase64(keypair.getPrivate().getEncoded());
	}
	
	/**
	 * 将公钥信息字符串（base64编码），转换为公钥信息对象
	 * @param publicBase64String 公钥信息字符串
	 * @return 公钥信息对象
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static PublicKey stringToPublicKey(String publicBase64String) throws Exception {
		byte[] keyBytes = base64Tobyte(publicBase64String);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
	}
	
	/**
	 * 将私钥信息字符串（base64编码），转换为私钥信息对象
	 * @param privateBase64String 私钥信息字符串
	 * @return 私钥信息对象
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static PrivateKey stringToPrivateKey(String privateBase64String) throws Exception {
		byte[] keyBytes = base64Tobyte(privateBase64String);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}
	
	/**
	 * 将字节数组，转换为base64编码的字符串输出。
	 * @param input 字节数组
	 * @return base64编码的字符串
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static String byteToBase64(byte[] input) throws Exception {
		return Base64.getEncoder().encodeToString(input);
	}
	
	/**
	 * 将base64编码的字符串，转换为字节数组输出。
	 * @param base64 base64编码的字符串
	 * @return 字节数组
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static byte[] base64Tobyte(String base64) throws Exception {
		return Base64.getDecoder().decode(base64);
	}
	
	/**
	 * 使用公钥，对传入的字节数组信息进行加密，返回一个加密后的字节数组
	 * @param input 待加密的字节数组
	 * @param publicKey 公钥信息对象
	 * @return 加密后的字节数组
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static byte[] encodeByteArray(byte[] input, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(input);
	}
	
	/**
	 * 使用私钥，对传入的字节数组信息进行解密，返回一个解密后的字节数组
	 * @param input 待解密的字节数组
	 * @param privateKey 私钥信息对象
	 * @return 解密后的字节数组
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static byte[] decodeByteArray(byte[] input, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(input);
	}
	
	/**
	 * 使用公钥对象，对传入的字符串进行加密
	 * @param plaintext 待加密字符串
	 * @param publicKey 公钥信息对象
	 * @return 加密后的字符串
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static String encode(String plaintext, PublicKey publicKey) throws Exception {
		byte[] encodeBytes = encodeByteArray(plaintext.getBytes(), publicKey);
		return byteToBase64(encodeBytes);
	}
	
	/**
	 * 使用公钥字符串信息，对传入的字符串进行加密
	 * @param plaintext 待加密字符串
	 * @param publicBase64String 公钥字符串信息（base64编码）
	 * @return 加密后的字符串
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static String encode(String plaintext, String publicBase64String) throws Exception {
		return encode(plaintext, stringToPublicKey(publicBase64String));
	}
	
	/**
	 * 使用私钥对象，对传入的字符串进行解密
	 * @param ciphertext 待解密字符串
	 * @param privateKey 私钥信息对象
	 * @return 解密后的字符串
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static String decode(String ciphertext, PrivateKey privateKey) throws Exception {
		byte[] decodeBytes = decodeByteArray(base64Tobyte(ciphertext), privateKey);
		return new String(decodeBytes);
	}
	
	/**
	 * 使用私钥字符串信息，对传入的字符串进行解密
	 * @param ciphertext 待解密字符串
	 * @param privateBase64String 私钥字符串信息（base64编码）
	 * @return 解密后的字符串
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static String decode(String ciphertext, String privateBase64String) throws Exception {
		return decode(ciphertext, stringToPrivateKey(privateBase64String));
	}
	
}
