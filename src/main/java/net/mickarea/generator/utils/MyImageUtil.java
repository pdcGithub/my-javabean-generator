/******************************************************************************************************

This file "MyImageUtil.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.utils;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;

/**
 * 这是一个我自己的图片处理工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年5月13日
 */
public class MyImageUtil {

	/**
	 * 根据设置的最大宽度和最大高度，对图片对象进行缩放。
	 * @param oriImg 待处理的图像对象
	 * @param maxWidth 缩放后的最大宽度
	 * @param maxHeight 缩放后的最大高度
	 * @return BufferedImage 类型的图像对象
	 * @throws Exception 如果执行出错，返回异常
	 */
	public static BufferedImage getScaledImage(BufferedImage oriImg, int maxWidth, int maxHeight) throws Exception {
		BufferedImage resultImg = null;
		//先判断参数有效性
		if(oriImg==null || maxWidth<=0 || maxHeight <=0) {
			throw new IllegalArgumentException(
					String.format("The parameters passed to the method are incorrect, (oriImg=%s, maxWidth=%s, maxHeight=%s)", oriImg, maxWidth, maxHeight));
		}
		//获取图片的宽度和高度
		int oriWidth = oriImg.getWidth();
		int oriHeight = oriImg.getHeight();
		//新的 宽度 和 高度
		int newWidth = 0;
		int newHeight = 0;
		//根据最大宽度 和 最大高度 判断是否需要缩放
		if(oriWidth<=maxWidth && oriHeight<=maxHeight) {
			//不缩放，直接返回原对象
			resultImg =  oriImg;
		}else {
			//根据最大宽度缩放，计算出可能的高度为多少
			newWidth = maxWidth;
			newHeight = oriHeight*newWidth/oriWidth;
			//如果计算结果大于最大高度，则重新按高度缩放
			if(newHeight>maxHeight) {
				newHeight = maxHeight;
				newWidth = newHeight*oriWidth/oriHeight;
			}
			//根据计算出的高度和宽度，缩放图片
			Image newImg = oriImg.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			//创建一个新的 BufferedImage 对象用于返回结果
			resultImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR);
			//构建2D图形处理对象，将缩放的图像绘制到新的 BufferedImage 对象
			Graphics g = resultImg.getGraphics();
			g.drawImage(newImg, 0, 0, null);
		}
		//返回结果
		return resultImg;
	}
	
	/**
	 * 根据设置的源文件路径、最大宽度、最大高度，对图片对象进行缩放。并将缩放结果保存到新路径。
	 * @param oriPath 源图片路径
	 * @param tarPath 生成图片路径
	 * @param maxWidth 最大宽度
	 * @param maxHeight 最大高度
	 * @throws Exception 如果执行出错，返回异常
	 */
	public static void getScaledImage(String oriPath, String tarPath, int maxWidth, int maxHeight) throws Exception {
		//先判断参数有效性
		if(MyStrUtil.isEmptyString(oriPath) || MyStrUtil.isEmptyString(tarPath) || maxWidth<=0 || maxHeight <=0) {
			throw new IllegalArgumentException(
					String.format("The parameters passed to the method are incorrect, (oriPath=%s, tarPath=%s, maxWidth=%s, maxHeight=%s)", 
							oriPath, tarPath, maxWidth, maxHeight));
		}
		//开始处理
		File oriFile = new File(oriPath);
		if(oriFile.exists() && oriFile.isFile()) {
			int lastIndex = oriFile.getName().lastIndexOf(".");
			if(lastIndex==-1) {
				throw new Exception(String.format("The source image file is missing a file extension, fileName=%s", oriFile.getName()));
			}
			String suffix = oriFile.getName().substring(lastIndex+1);
			if(MyStrUtil.isEmptyString(suffix)) {
				throw new Exception(String.format("The source image file extension is abnormal, suffix=%s", suffix));
			}
			BufferedImage oriImg = ImageIO.read(oriFile);
			BufferedImage tarImg = MyImageUtil.getScaledImage(oriImg, maxWidth, maxHeight);
			//保存到目标路径
			boolean result = ImageIO.write(tarImg, suffix, new File(tarPath));
			if(!result) {
				throw new Exception("Image saving failed, please check.");
			}
		}else {
			throw new FileNotFoundException(String.format("The image path is incorrect, unable to find a valid file, path: %s",oriPath));
		}
	}
	
}
