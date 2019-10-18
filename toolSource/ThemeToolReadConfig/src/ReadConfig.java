import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import utils.ExcelUtils;
import utils.IOUtil;
import xuibean.Skin;

public class ReadConfig {
	private static String sPlatform;
	private static String sConfig_path;
	private static String sValues_path;
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
	private static final int XUI_APP_PAGE_INDEX = 0;
	private static final int XUI_APP_LIST_X_INDEX = 1;
	private static final int XUI_APP_LIST_Y_INDEX = 2;
	private static final int XUI_VALUE_START_ROW = 3;

	/**
	 * 仿XUI app列表位置参数
	 */
	private static final int XUI_APP_LIST_START_ROW_INDEX = 15;

	public static void main(String[] args){
		/*//test.start
		File file1 = new File(".");
		System.out.println(file1.getAbsolutePath() + ", args = " + Arrays.toString(args));
		String tempPlatform = "XUI";
		String tempPath = "testfiles/skin_config_"+tempPlatform+".xls";
		String tempValuePath = "testfiles/values_"+tempPlatform;
		args = new String[]{tempPlatform, tempPath, tempValuePath};
		//test.end*/
		if (args.length < 3) {
			System.out.println("path args error");
			return;
		}
		sPlatform = args[0];
		sConfig_path = args[1];
		sValues_path = args[2];

		File file = new File(sConfig_path);
		if(!file.exists()){
			System.out.println("配置文件不存在");
			System.exit(1);
			return;
		}

		List<List<String>> infos = ExcelUtils.read(file.getAbsolutePath());
		if(infos == null){
			throw new NullPointerException("配置文件解析失败");
		}

		if(Platform.PLATFORM_D9.equals(sPlatform) || Platform.PLATFORM_S5.equals(sPlatform)) {
			Properties properties = new Properties();
			for (int i = VALUE_START_ROW_INDEX; i < infos.size(); i++) {
				List<String> excelPairs = infos.get(i);
				String name = excelPairs.get(NAME_COLUMN_INDEX);
				String s_v = excelPairs.get(VALUE_COLUMN_INDEX);
				String beizhu = excelPairs.get(VALUE_COLUMN_INDEX + 1);
				if(isEmpty(name) && isEmpty(s_v) && isEmpty(beizhu)){
					continue;
				}

				if(isEmpty(name) || isEmpty(s_v)){
					throw new NullPointerException("配置文件中有空值");
				}

				String value = s_v.trim();
				if (!value.startsWith("#") && value.endsWith(".0")) {
					//properties的value好像只能接受string值
					properties.put(name, String.valueOf(Float.valueOf(value).intValue()));
				} else {
					properties.put(name, value);
				}

			}

			System.out.println(properties);

			System.out.println("test.value=" + properties.getProperty("third_pard_app_icon_corner"));

			WriteConfig.copyColors(properties, sValues_path + "/colors.xml");
			WriteConfig.copyDimens(properties, sValues_path + "/dimens.xml");
		}else if(Platform.PLATFORM_VUI.equals(sPlatform)){
			Properties properties = new Properties();
			Skin skin = new Skin();
			Skin.Page page = new Skin.Page();
			List<Skin.Page> pageList = new ArrayList<>();
			List<Skin.App> appList = new ArrayList<>();
			int appIndex = 0;
			int lastPageIndex = -1;
			for (int i = XUI_VALUE_START_ROW; i < infos.size(); i++) {
				List<String> excelPairs = infos.get(i);
				System.out.println(excelPairs);
				if(i < XUI_APP_LIST_START_ROW_INDEX) {
					String name = excelPairs.get(NAME_COLUMN_INDEX);
					String s_v = excelPairs.get(VALUE_COLUMN_INDEX);
					String beizhu = excelPairs.get(VALUE_COLUMN_INDEX + 1);
					if(isEmpty(name) && isEmpty(s_v) && isEmpty(beizhu)){
						continue;
					}

					if(isEmpty(name) || isEmpty(s_v)){
						throw new NullPointerException("配置文件中有空值, 第 " + (i + 1) +" 行");
					}

					String value = s_v.trim();

					if (!value.startsWith("#") && value.endsWith(".0")) {
						//properties的value好像只能接受string值
						properties.put(name, String.valueOf(Float.valueOf(value).intValue()));
					} else {
						properties.put(name, value);
					}
				}else {
					String screen = excelPairs.get(XUI_APP_PAGE_INDEX);
					if(isEmpty(screen)){
						String x = excelPairs.get(XUI_APP_LIST_X_INDEX);
						String y = excelPairs.get(XUI_APP_LIST_Y_INDEX);
						if(isEmpty(x) && isEmpty(y)) {
							continue;
						}else {
							throw new NullPointerException("配置文件中有空值, 第 " + (i + 1) +" 行");
						}
					}

					String x = excelPairs.get(XUI_APP_LIST_X_INDEX);
					if(isEmpty(x)){
						throw new NullPointerException("配置文件中有空值, 第 " + (i + 1) +" 行");
					}
					String y = excelPairs.get(XUI_APP_LIST_Y_INDEX);
					if(isEmpty(y)){
						throw new NullPointerException("配置文件中有空值, 第 " + (i + 1) +" 行");
					}

					int currPageIndex = Float.valueOf(screen).intValue();
					if(-1 == lastPageIndex){
						lastPageIndex = currPageIndex;
					}
					if(lastPageIndex != currPageIndex){
						//store
						page.setPageIndex(lastPageIndex-1);
						page.setApps(appList);
						pageList.add(page);

						//new
						lastPageIndex = currPageIndex;
						page = new Skin.Page();
						appList = new ArrayList<>();
					}
					System.out.println("PageIndex = " + lastPageIndex);

					Skin.App app = new Skin.App();
					app.setIndex(appIndex++);
					app.setX(Float.valueOf(x).intValue());
					app.setY(Float.valueOf(y).intValue());
					appList.add(app);
				}
			}
			if(page.getApps() == null && !pageList.contains(page)){
				page.setPageIndex(lastPageIndex - 1);
				page.setApps(appList);
				pageList.add(page);
			}
			skin.setPages(pageList);

			System.out.println(properties);

			WriteConfig.copyColors(properties, sValues_path + "/colors.xml");
			WriteConfig.copyDimens(properties, sValues_path + "/dimens.xml");


			String json = new Gson().toJson(skin);
			String assetsPath = sValues_path+"/../../" +"assets";
			File skinJsonFileDir = new File(assetsPath);
			if(!skinJsonFileDir.exists()){
				boolean skinJsonFileMake = skinJsonFileDir.mkdirs();
				if(!skinJsonFileMake){
					System.out.println("make dir failed " + skinJsonFileDir.getAbsolutePath());
					System.exit(1);
					return;
				}
			}
			try {
				FileWriter fileWriter = new FileWriter(skinJsonFileDir+"/skin.json", false);
				fileWriter.write(json);
				fileWriter.flush();
				fileWriter.close();
			}catch (Exception e){
				e.printStackTrace();
			}

		}
	}

	public static boolean isEmpty(String s){
		return s == null || s.trim().isEmpty();
	}
}
