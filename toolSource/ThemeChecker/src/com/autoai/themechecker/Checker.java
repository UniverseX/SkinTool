package com.autoai.themechecker;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.autoai.themechecker.util.Log;

import javax.swing.JFrame;

/**
 *
 */
public class Checker {
	private static String standardSkinFiles_S5 = "./default_theme_files_S5.txt";
	private static String standardSkinFiles_D9 = "./default_theme_files_D9.txt";
	private static String ignoreCheckFiles = "./ignore_check.txt";
	private static String parentPath = "./";
	private static String dstCheckPath = parentPath;
	private static DataModel dataModel_S5;
	private static DataModel dataModel_D9;

	public static void main(String[] args) {
		loadDstPath();

		if(!checkPath(standardSkinFiles_S5)
				|| !checkPath(ignoreCheckFiles)
				|| !checkPath(dstCheckPath) || !checkPath(standardSkinFiles_D9)){
			return;
		}

		//１准备数据
		dataModel_S5 = new DataModel(ignoreCheckFiles, standardSkinFiles_S5, dstCheckPath);
		dataModel_S5.prepareStandData();

		dataModel_D9 = new DataModel(ignoreCheckFiles, standardSkinFiles_D9, dstCheckPath);
		dataModel_D9.prepareStandData();

		//2.展示数据
		showUi();
	}

	private static void loadDstPath() {
		String dstPropterParentPath = parentPath;
		String dstPropterPath = dstCheckPath;
		boolean findDst = false;
		File file2 = new File(dstPropterParentPath);
		if(file2.isDirectory()){
			File[] listFiles = file2.listFiles();
			if(listFiles != null){
				for(File f: listFiles){
					if(f.getName().endsWith(".properties")){
						dstPropterPath = f.getAbsolutePath();
						findDst = true;
						break;
					}
				}
			}
		}
		if(!findDst){
			return;
		}
		Log.println(dstPropterPath);
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(dstPropterPath));
			String path = (String) properties.get("dst_path");
			Log.println("dst_path from property: " + path);
			if(path != null) {
				File file = new File(path);
				if (file.exists()) {
					dstCheckPath = path;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static boolean checkPath(String path) {
		File file1 = new File(path);
		if(!file1.exists()){
			showNoData(path);
			return false;
		}
		return true;
	}


	public static void showUi(){
		final JFrame f = new JFrame("主题名称检查");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setMinimumSize(new Dimension(800, 400));

		final JF_demo1 jf_demo1 = new JF_demo1();
		//must before initPanel
		jf_demo1.setOnClickListenerS5(new JF_demo1.OnClickListener() {
			@Override
			public void onClick(String dstPath) {
				f.remove(jf_demo1);
				dataModel_S5.doCompare(dstPath);
				jf_demo1.showResult(dataModel_S5.getStandardDiffArray(), dataModel_S5.getDstDiffArray());

				f.add("Center", jf_demo1);
				f.setSize(f.getPreferredSize());
				f.setVisible(true);
				f.pack();
			}
		});
		jf_demo1.setOnClickListenerD9(new JF_demo1.OnClickListener() {
			@Override
			public void onClick(String dstPath) {
				f.remove(jf_demo1);
				dataModel_D9.doCompare(dstPath);
				jf_demo1.showResult(dataModel_D9.getStandardDiffArray(), dataModel_D9.getDstDiffArray());

				f.add("Center", jf_demo1);
				f.setSize(f.getPreferredSize());
				f.setVisible(true);
				f.pack();
			}
		});
		jf_demo1.initPanel();

		f.add("Center", jf_demo1);
		f.setSize(f.getPreferredSize());
		f.setVisible(true);
		f.pack();
	}

	public static void showNoData(String path){
		JF_demo1.initNoData("目标路径不存在\r\npath=" + path);
	}

}
