package operations;

import java.awt.Color;
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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utils.ExcelPair;
import utils.ExcelUtils;

public class HuanYuOpFormat implements Operate {

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
		List<List<ExcelPair>> infos = ExcelUtils.readForObject(file.getAbsolutePath());
		int size = infos.size();
		List<ExcelPair> first = infos.remove(0);//title
		// 排序
		// 这里将map.entrySet()转换成list
		// 然后通过比较器来实现排序
		Collections.sort(infos, new Comparator<List<ExcelPair>>() {

			@Override
			public int compare(List<ExcelPair> o1,
					List<ExcelPair> o2) {
				if(o2.size() < 8 || o1.size() < 8){
					return -1;
				}
				Object key1 = o1.get(key_colum).value;
				String key1Str = makeKey(key1);
				Object key2 = o2.get(key_colum).value;
				String key2Str = makeKey(key2);
//				System.out.println("key1="+key1 + ", key2="+key2);
				if(key2Str.contains(keyExclude) || !key2Str.contains(keyKey)){
//					System.out.println("contains 18.0");
					return -1;
				}
				if(key1Str.contains(keyExclude)|| !key1Str.contains(keyKey)){
					return 1;
				}
				return key1Str.compareTo(key2Str);
			}
				
		});
		
		infos.add(0, first);
//		ExcelUtils.printlnList(infos);
		
		// 分类
		for (int i = 0; i < size; i++) {// row
			List<ExcelPair> list = infos.get(i);
			if (list.size() > 8) {
				String key = String.valueOf(list.get(key_colum).value);
				int indexOf = key.indexOf(keyKey);
				if (indexOf > 0 && key.length() > indexOf + key_lengh) {
					key = key.substring(indexOf, indexOf + key_lengh);
					// System.out.println("key="+ key + ", index_18="+
					// indexOf);
//					 System.out.println("key="+ key);
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

//		System.out.println(kindsBykey);

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
				List<ExcelPair> list = infos.get(row);
				System.out.println("row="+row);
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
		Workbook wb;
		boolean isExcel2003 = ExcelUtils.isExcel2003(dstPath);
		if(isExcel2003){
			wb = new HSSFWorkbook();
		}else{
			wb = new XSSFWorkbook();
		}
		Sheet sheet = wb.createSheet("王梦瑶爱朱晓龙");
		for (int j = 0; j < size; j++) {
			Row row = sheet.createRow(j);
			Integer color = rowWithColor.get(j);
			CellStyle style = wb.createCellStyle();

			if (color != null) {
				if (color == GREEN) {
					style.setFillPattern(CellStyle.SOLID_FOREGROUND);
					if(isExcel2003){
						style.setFillForegroundColor(HSSFColor.GREEN.index);
					}else{
						XSSFColor xssfColor = new XSSFColor(Color.GREEN);
						style.setFillForegroundColor(xssfColor.getIndexed());
					}
				} else if (color == ORANGE) {
					style.setFillPattern(CellStyle.SOLID_FOREGROUND);
					if(isExcel2003){
						style.setFillForegroundColor(HSSFColor.ORANGE.index);
					}else{
						XSSFColor xssfColor = new XSSFColor(Color.ORANGE);
						style.setFillForegroundColor(xssfColor.getIndexed());
					}
					
				} else if (color == YELLOW) {
					style.setFillPattern(CellStyle.SOLID_FOREGROUND);
					if(isExcel2003){
						style.setFillForegroundColor(HSSFColor.YELLOW.index);
					}else{
						XSSFColor xssfColor = new XSSFColor(Color.YELLOW);
						style.setFillForegroundColor(xssfColor.getIndexed());
					}
				}
			} else {
				System.out.println("没有打颜色的行： " + j);
			}
			// row.setRowStyle(style);
			List<ExcelPair> list = infos.get(j);
			for (int k = 0; k < list.size(); k++) {
				ExcelPair excelPair = list.get(k);
				Cell cell = row.createCell(k, excelPair.cellType);
				ExcelUtils.setCellValue(excelPair.value, cell);
//				cell.setCellValue(excelPair.value);
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

	public String readValue(List<ExcelPair> list, int index) {
		String value = String.valueOf(list.get(index).value);
		System.out.println("value="+ value);
		return value == null || value.isEmpty() || value.equals("-")? "0" : value;//left_value_index
	}

	public String makeKey(Object key) {
		String keyStr = String.valueOf(key);
		int indexOf = keyStr.indexOf(keyKey);
		if (indexOf > 0 && keyStr.length() > indexOf + key_lengh) {
			keyStr = keyStr.substring(indexOf, indexOf + key_lengh);
		}
//		System.out.println("key="+keyStr);
		return keyStr;
	}

}
