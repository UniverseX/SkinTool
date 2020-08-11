package com.autoai.themechecker;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;

/**
 *
 */
public class Checker {
	private static String default_names = "./default_theme_files.txt";
	private static String ignoreCheckFiles = "./ignore_check.txt";
	private static String parentPath = "./";
	private static String dstCheckPath = null;
	private static DataModel dataModel;

	public static void main(String[] args) {
		if (args != null) {
			default_names = args[0];
			dstCheckPath = args[1];
		}
		System.out.println("default_names_file: " + default_names);
		System.out.println("dstCheckPath: " + dstCheckPath);

		if (!checkPath(default_names)
				|| !checkPath(dstCheckPath)
		) {
			return;
		}

		//１准备数据
		if (args != null && "A7".equals(args[0])) {
			dataModel = new DataModelJpg(ignoreCheckFiles, default_names, dstCheckPath);
		} else {
			dataModel = new DataModelPng(ignoreCheckFiles, default_names, dstCheckPath);
		}
		dataModel.prepareStandData();
		//2.展示数据
		showUi();
	}

	private static boolean checkPath(String path) {
		File file1 = new File(path);
		if (!file1.exists()) {
			showNoData(path);
			return false;
		}
		return true;
	}


	public static void showUi() {
		final JFrame f = new JFrame("主题名称检查");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setMinimumSize(new Dimension(800, 400));

		final JF_demo1 jf_demo1 = new JF_demo1();
		//must before initPanel
		jf_demo1.setOnClickListenerS5(new JF_demo1.OnClickListener() {
			@Override
			public void onClick(String dstPath) {
				f.remove(jf_demo1);
				dataModel.doCompare(dstPath);
				jf_demo1.showResult(dataModel.getStandardDiffArray(), dataModel.getDstDiffArray());

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

		if (dstCheckPath != null) {
			jf_demo1.getDstPathEditPanel().setText(dstCheckPath);
			jf_demo1.getOnClickListener().onClick(dstCheckPath);
		}
	}

	public static void showNoData(String path) {
		JF_demo1.initNoData("目标路径不存在\r\npath=" + path);
	}

}
