package operations;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utils.ExcelPair;
import utils.ExcelUtils;
import utils.ExcelUtils;
import utils.Pair;

public class HuanYuOpPaixu implements Operate {

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
		String srcPath = "应收账款.xls";
		String dstPath = "应收账款_处理好.xls";
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

		ExcelUtils.printlnList(infos);
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
			List<ExcelPair> list = infos.get(j);
			for (int k = 0; k < list.size(); k++) {
				ExcelPair excelPair = list.get(k);
				Cell cell = row.createCell(k, excelPair.cellType);
				ExcelUtils.setCellValue(excelPair.value, cell);
				CellStyle cellStyle = wb.createCellStyle();
				cellStyle.cloneStyleFrom(excelPair.cellStyle);
				cell.setCellStyle(cellStyle);
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
