package com.lx.file.convert.master.utils;

import java.io.File;

public class PathMaster {

//	/**
//	 * ��������ļ�·��
//	 * @param oiClass
//	 * @return oiClass ����·��
//	 */
//	public static String getPath(Class<?> oiClass) {
//		String path = oiClass.getResource("").getPath();
//		return new File(path).getAbsolutePath();
//	}
//	
	/**
	 * ���Object������·��
	 * @param object
	 * @return object ����·��
	 */
	public static String getPath(Object object) {
		String path = object.getClass().getResource("").getPath();
		return new File(path).getAbsolutePath();
	}
	
	/**
	 * ���Object�����ð�
	 * @param object
	 * @return object ���ڰ�·��
	 */
	public static String getPackagePath(Object object) {
		Package p = object.getClass().getPackage();
		return p != null ? p.getName().replaceAll("\\.", "/") : "";
	}
	
	/**
	 * ���Class����·��
	 */
	public static String getClassRootPath() {
		try {
			String path = PathMaster.class.getClassLoader().getResource("").toURI().getPath();
			return new File(path).getAbsolutePath();
		}
		catch (Exception e) {
			String path = PathMaster.class.getClassLoader().getResource("").getPath();
			return new File(path).getAbsolutePath();
		}
	}
	
	/**
	 * ���web��·��
	 */
	public static String getWebRootPath() {
		try {
			String path = PathMaster.class.getResource("/").toURI().getPath();
			return new File(path).getParentFile().getParentFile().getCanonicalPath();
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * �ж��Ƿ��Ǿ��·��
	 */
	public static boolean isAbsolutelyPath(String path) {
		return path.startsWith("/") || path.indexOf(":") == 1;
	}
}
