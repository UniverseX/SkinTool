import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import utils.ExcelUtils;
import xuibean.Skin;

public class ReadConfig {
	private static String sPlatform;
	private static String sConfig_path;
	private static String sValues_path;
	private static String sProduct;
	/**
	 * 实际值开始行索引
	 */
	private static final int VALUE_START_ROW_INDEX = 2;
	/**
	 * 实际值开始列索引
	 */
	private static final int VALUE_START_COLUMN_INDEX = 1;

	/**
	 * 变量名称所在索引
	 */
	private static final int NAME_COLUMN_INDEX = 1;

	/**
	 * 变量值所在索引
	 */
	private static final int VALUE_COLUMN_INDEX = 2;

	/**
	 * 仿XUI天气时钟组件 位置索引
	 */
	private static final String XUI_PROPERTIES_START_ROW_INDEX = "config_widget_start_row";
	private static final String XUI_PROPERTIES_END_ROW_INDEX = "config_widget_end_row";
	private static final String XUI_APP_JSON_START_ROW_INDEX = "config_app_list_start_row";
	private static final String XUI_APP_JSON_END_ROW_INDEX = "config_app_list_end_row";

	/**
	 * 仿XUI app列表位置参数
	 */
	private static final int XUI_APP_LIST_START_ROW_INDEX = 15;

	public static void main(String[] args) {
		String localSrcPath = "";
		/*//test.start
		File file1 = new File(".");
		System.out.println(file1.getAbsolutePath() + ", args = " + Arrays.toString(args));
		String tempPlatform = "XUI_DUI";
		String tempPath = "testfiles/skin_config_" + tempPlatform + ".xls";
		String tempValuePath = "testfiles/values_" + tempPlatform;
		args = new String[]{tempPlatform, tempPath, tempValuePath};
		localSrcPath = "src/";
		//test.end*/
		if (args.length < 3) {
			System.out.println("path args error");
			return;
		}
		sProduct = args[0];
		sPlatform = args[1];
		sConfig_path = args[2];
		sValues_path = args[3];

		File file = new File(sConfig_path);
		System.out.println("配置文件路径：" + file.getAbsolutePath());
		if (!file.exists()) {
			System.out.println("配置文件不存在");
			System.exit(1);
			return;
		}

		String modelXls_path = sValues_path.substring(0, sValues_path.indexOf("ThemeSkin")) + "\\UI标注模板" + "\\skin_config_" + sProduct + ".xls";
		System.out.println("模板文件路径：" + modelXls_path);
		File fileModel = new File(modelXls_path);
		if (!fileModel.exists()) {
			System.out.println("模板文件不存在");
			System.exit(1);
			return;
		}

		List<List<String>> modelList = ExcelUtils.read(fileModel.getAbsolutePath());
		if (modelList == null) {
			System.out.println("模板文件解析失败");
			System.exit(1);
		}

		List<List<String>> infos = ExcelUtils.read(file.getAbsolutePath());
		if (infos == null) {
			System.out.println("配置文件解析失败");
			System.exit(1);
		}
		if (modelList.size() != infos.size()) {
			System.out.println("模板文件和主题配置文件行数不一致");
			System.exit(1);
		}

		if (!Platform.PLATFORM_VUI.equals(sPlatform)) {
			Properties properties = new Properties();
			for (int i = VALUE_START_ROW_INDEX; i < infos.size(); i++) {
				List<String> excelPairs = infos.get(i);
				String name = excelPairs.get(NAME_COLUMN_INDEX);
				String s_v = excelPairs.get(VALUE_COLUMN_INDEX);
				String beizhu = excelPairs.get(VALUE_COLUMN_INDEX + 1);
				if (isEmpty(name) && isEmpty(s_v) && isEmpty(beizhu)) {
					continue;
				}

				if (isEmpty(name) || isEmpty(s_v)) {
					throw new NullPointerException("配置文件中有空值");
				}

				String value = s_v.trim();
				if (!value.startsWith("#")) {
					//去掉.0后缀
					properties.put(name, String.valueOf(Float.valueOf(value).intValue()));
				} else {
					properties.put(name, value);
				}

			}

			WriteConfig.copyColors(properties, sValues_path + "/colors.xml");
			WriteConfig.copyDimens(properties, sValues_path + "/dimens.xml");
		} else {
			//to colors and dimens
			Properties properties = new Properties();
			try {
				properties.loadFromXML(new FileInputStream(localSrcPath + "properties_xml/theme_properties_" + sProduct +".xml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			int widgetStartRow = Integer.parseInt(properties.getProperty(XUI_PROPERTIES_START_ROW_INDEX));
			int widgetEndRow = Integer.parseInt(properties.getProperty(XUI_PROPERTIES_END_ROW_INDEX));
			for (int i = widgetStartRow; i < widgetEndRow; i++) {
				List<String> rowInfos = infos.get(i);
				int columns = rowInfos.size();
				for (int j = 0; j < columns; j++) {
					String key = rowInfos.get(j);
					if (isEmpty(key)) {
						continue;
					}
					if (properties.containsKey(key)) {
						String value = infos.get(i + 1).get(j);
						if (!value.startsWith("#")) {
							//去掉.0后缀
							String valueStr;
							try {
								valueStr = String.valueOf(Float.valueOf(value).intValue());
							} catch (Exception e) {
								System.out.println(key + "的值有问题");
								throw new RuntimeException(e);
							}
							if(key.endsWith("_color")){//如果颜色为0，默认#ffffff
								if (isEmpty(valueStr) || valueStr.endsWith("0")) {
									valueStr = "#ffffff";
								}
								properties.setProperty(key, valueStr);
							}else {
								properties.setProperty(key, valueStr);
							}
						} else {
							properties.setProperty(key, value);
						}
					}
				}
			}
			WriteConfig.copyColors(properties, sValues_path + "/colors.xml");
			WriteConfig.copyDimens(properties, sValues_path + "/dimens.xml");

			//to json
			Skin skin = new Skin();
			List<Skin.Page> pageList = new ArrayList<>();
			skin.setPages(pageList);

			int tmpPage = -1;
			int appStartRow = Integer.parseInt(properties.getProperty(XUI_APP_JSON_START_ROW_INDEX));
			int appEndRow = Integer.parseInt(properties.getProperty(XUI_APP_JSON_END_ROW_INDEX));
			for (int i = appStartRow; i < appEndRow; i++) {
				List<String> rowInfos = infos.get(i);
				String pageStr = rowInfos.get(0);
				if (isEmpty(pageStr)) {
					continue;
				}
				int pageNum = Float.valueOf(pageStr).intValue();
				if (pageNum == 0) {
					continue;
				}
				Skin.Page page;
				if (tmpPage != pageNum) {
					tmpPage = pageNum;
					//create page
					page = new Skin.Page();
					page.setPageIndex(pageNum - 1);
					List<Skin.App> appList = new ArrayList<>();
					page.setApps(appList);
					//add page
					pageList.add(page);
					//create app
					Skin.App app = new Skin.App();
					//set attrs
					app.setIndex(i - appStartRow);
					app.setX(strToInt(rowInfos.get(1)));
					app.setY(strToInt(rowInfos.get(2)));
					app.setIcon_size(strToInt(rowInfos.get(3)));
					app.setLabel_relative_loc(strToInt(rowInfos.get(4)));
					app.setMargin_text_icon(strToInt(rowInfos.get(5)));
					try {
						app.setText_color(checkColor(rowInfos.get(6)));
					} catch (IllegalArgumentException e) {
						System.out.println("主题配置文件：app颜色参数有错，" + e.getMessage());
						System.exit(1);
					}
					app.setIcon_padding(strToInt(rowInfos.get(7)));
					appList.add(app);
				} else {
					page = pageList.get(pageNum - 1);
					List<Skin.App> appList = page.getApps();
					Skin.App app = new Skin.App();
					//set attrs
					app.setIndex(i - appStartRow);
					app.setX(strToInt(rowInfos.get(1)));
					app.setY(strToInt(rowInfos.get(2)));
					app.setIcon_size(strToInt(rowInfos.get(3)));
					app.setLabel_relative_loc(strToInt(rowInfos.get(4)));
					app.setMargin_text_icon(strToInt(rowInfos.get(5)));
					app.setText_color(checkColor(rowInfos.get(6)));
					app.setIcon_padding(strToInt(rowInfos.get(7)));
					appList.add(app);
				}
			}
//			System.out.println(skin);

			String json = new Gson().toJson(skin);
			String assetsPath = sValues_path + "/../../" + "assets";
			File skinJsonFileDir = new File(assetsPath);
			if (!skinJsonFileDir.exists()) {
				boolean skinJsonFileMake = skinJsonFileDir.mkdirs();
				if (!skinJsonFileMake) {
					System.out.println("make dir failed " + skinJsonFileDir.getAbsolutePath());
					System.exit(1);
					return;
				}
			}
			try {
				FileWriter fileWriter = new FileWriter(skinJsonFileDir + "/skin.json", false);
				fileWriter.write(json);
				fileWriter.flush();
				fileWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public static boolean isEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}

	public static int strToInt(String s) {
		return isEmpty(s) ? 0 : Float.valueOf(s).intValue();
	}

	public static String checkColor(String colorString) {
		if (colorString.charAt(0) != '#' || (colorString.length() != 7 && colorString.length() != 9)) {
			if (isEmpty(colorString) || colorString.equals("0.0") || colorString.equals("0")) {
				colorString = "#ffffff";
			} else {
				throw new IllegalArgumentException("Unknown color");
			}
		}
		return colorString;
	}
}
