package operations;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

import utils.ExcelUtils;

public class HuanYuOp implements Operate {

	/**
	 * 绿色正确
	 * 
	 * 黄色对不上
	 * 
	 * 橘黄色右边有金额
	 */
	private static int key_colum = 8;
	private static int key_lengh = 6;
	private static int left_value_index = 9;
	private static int right_value_index = 10;
	private static final String keyKey = "18";
	private static final String keyExclude = "18.0";//排序过滤字
	private static final int GREEN = 0;
	private static final int YELLOW = 1;
	private static final int ORANGE = 2;

	private Map<String, List<Integer>> kindsBykey = new HashMap<>();
	private Map<Integer, Integer> rowWithColor = new HashMap<>();

	public void operate() {
		String srcPath = "10月营销中心预收款.xls";
		String dstPath = "10月营销中心预收款_处理好.xls";
		File file = new File(srcPath);
		System.out.println("file exits: " + file.exists());
		if(!file.exists()){
			return;
		}
		List<List<String>> infos = ExcelUtils.read(file.getAbsolutePath());
		int size = infos.size();
		List<String> first = infos.remove(0);//title
		// 排序
		// 这里将map.entrySet()转换成list
		// 然后通过比较器来实现排序
		Collections.sort(infos, new Comparator<List<String>>() {

			// 升序排序
			@Override
			public int compare(List<String> o1, List<String> o2) {
				if(o2.size() < 8 || o1.size() < 8){
					return -1;
				}
				String key1 = o1.get(key_colum);
				key1 = makeKey(key1);
				String key2 = o2.get(key_colum);
				key2 = makeKey(key2);
//				System.out.println("key1="+key1 + ", key2="+key2);
				if(key2.contains(keyExclude)){
//					System.out.println("contains 18.0");
					return -1;
				}
				if(key1.contains(keyExclude)){
					return 1;
				}
				return key1.compareTo(key2);
			}
		});
		
		infos.add(0, first);
		// 分类
		for (int i = 0; i < size; i++) {// row
			List<String> list = infos.get(i);
			if (list.size() > 8) {
				String key = list.get(key_colum);
				int indexOf = key.indexOf(keyKey);
				if (indexOf > 0 && key.length() > indexOf + key_lengh) {
					key = key.substring(indexOf, indexOf + key_lengh);
					// System.out.println("key="+ key + ", index_18="+
					// indexOf);
					 System.out.println("key="+ key);
					if (!kindsBykey.containsKey(key)) {
						List<Integer> rowIndexList = new ArrayList<Integer>();
						rowIndexList.add(i);
						kindsBykey.put(key, rowIndexList);
					} else {
						List<Integer> rowIndexList = kindsBykey.get(key);
						if (!rowIndexList.contains(i)) {
							rowIndexList.add(i);
						}
					}
				}
			}
		}

		System.out.println(kindsBykey);

		// 计算
		Iterator<Entry<String, List<Integer>>> iterator = kindsBykey.entrySet()
				.iterator();

		while (iterator.hasNext()) {
			Entry<String, List<Integer>> entry = (Entry<String, List<Integer>>) iterator
					.next();
			List<Integer> value = entry.getValue();
			float leftSum = 0;
			float rightSum = 0;
			for (Integer row : value) {
				List<String> list = infos.get(row);
				if (list.size() > 10) {
					leftSum += Float.valueOf(readValue(list, left_value_index));
					rightSum += Float.valueOf(readValue(list, right_value_index));
				}
			}

			if (leftSum == 0) {
				// System.out.println("key=" + entry.getKey() + ", 左边和为0打棕色");
				for (Integer integer : value) {
					rowWithColor.put(integer, ORANGE);
				}
			} else if (leftSum == rightSum) {
				// System.out.println("key=" + entry.getKey() + ", 左边右边相等打绿色");
				for (Integer integer : value) {
					rowWithColor.put(integer, GREEN);
				}
			} else {
				// System.out.println("key=" + entry.getKey() + ", 左边右边不相等打黄色");
				for (Integer integer : value) {
					rowWithColor.put(integer, YELLOW);
				}
			}
		}

		// 写表
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("王梦瑶爱朱晓龙");
		for (int j = 0; j < size; j++) {
			HSSFRow row = sheet.createRow(j);
			Integer color = rowWithColor.get(j);
			CellStyle style = wb.createCellStyle();

			if (color != null) {
				if (color == GREEN) {
					style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					style.setFillForegroundColor(HSSFColor.GREEN.index);
					// style.setFillForegroundColor(HSSFColor.GREEN.index);
				} else if (color == ORANGE) {
					style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					style.setFillForegroundColor(HSSFColor.ORANGE.index);
					// style.setFillForegroundColor(HSSFColor.ORANGE.index);
				} else if (color == YELLOW) {
					style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					style.setFillForegroundColor(HSSFColor.YELLOW.index);
					// style.setFillForegroundColor(HSSFColor.YELLOW.index);
				}
			} else {
				System.out.println("没有打颜色的行： " + j);
			}
			// row.setRowStyle(style);
			List<String> list = infos.get(j);
			for (int k = 0; k < list.size(); k++) {
				HSSFCell cell = row.createCell(k);
				cell.setCellValue(list.get(k));
				cell.setCellStyle(style);
			}
		}

		try {
			FileOutputStream fout = new FileOutputStream(dstPath);
			wb.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String readValue(List<String> list, int index) {
		String value = list.get(index);
		System.out.println("value="+ value);
		return value == null || value.isEmpty() || value.equals("-")? "0" : value;//left_value_index
	}

	public String makeKey(String key) {
		int indexOf = key.indexOf(keyKey);
		if (indexOf > 0 && key.length() > indexOf + key_lengh) {
			key = key.substring(indexOf, indexOf + key_lengh);
		}
		return key;
	}

}
