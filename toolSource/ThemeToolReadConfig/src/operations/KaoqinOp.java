package operations;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import utils.ExcelUtils;

public class KaoqinOp implements Operate {

	public static final int date_index = 3;
	public static final int firt_time_index = 5;
	public static final int second_time_index = 6;
	public static final int jiaban_index = 9;
	
	//dist
	public static final int dst_date_index = 1;
	public static final int dst_time_index = 2;
	public static final int dst_jiaban_index = 3;
	public static final int dst_ok_index = 7;
	
	@Override
	public void operate() {
		String srcPath = "加班记录单-朱晓龙.xlsx";
		String srcPath2 = "朱晓龙.xlsx";
		String dstPath = "加班记录单-朱晓龙_处理好.xls";
		File file = new File(srcPath);
		File file2 = new File(srcPath2);
		System.out.println("file exits: " + file.exists());
		if(!file.exists() || !file2.exists()){
			return;
		}
		
		//title index 0/1/2  ,end index  end7---end0
		List<List<String>> infos = ExcelUtils.read(file.getAbsolutePath());
//		System.out.println(infos);
		
		List<List<String>> infos2 = ExcelUtils.read(file2.getAbsolutePath());
		infos2.remove(0);
//		System.out.println(infos2);
		
		List<Integer> jiaBanRowIndexs = new ArrayList<Integer>();
		for (int i = 0; i < infos2.size(); i++) {
			List<String> rowInfos = infos2.get(i);
//			System.out.println(rowInfos);
			String a = rowInfos.get(jiaban_index);
			if(a != null && !"".equals(a) && Float.parseFloat(a) > 0){
//				System.out.println(rowInfos.get(key_index));
				jiaBanRowIndexs.add(i);
			}
		}
		
		System.out.println(jiaBanRowIndexs);
		
		for (int i = 3; i < infos.size(); i++) {
			List<String> list = infos.get(i);
			if(i-3 < jiaBanRowIndexs.size()){
//			Integer integer = jiaBanRowIndexs.get(i-3);
//			System.out.println(integer);
				if(list.get(0)!= null && !list.get(0).isEmpty()){
					String index0 = list.remove(0);
					System.out.println(index0);
					list.add(0, (int)Float.parseFloat(index0)  + "");
				}
				
				list.remove(dst_date_index);
				list.add(dst_date_index, infos2.get(jiaBanRowIndexs.get(i-3)).get(date_index));
				
				list.remove(dst_time_index);
				String fistTime = infos2.get(jiaBanRowIndexs.get(i-3)).get(firt_time_index);
				String secondTime = infos2.get(jiaBanRowIndexs.get(i-3)).get(second_time_index);
				fistTime = makeJiaBanTimeStr(fistTime);
				secondTime = makeJiaBanTimeStr(secondTime);
				list.add(dst_time_index, fistTime +" - " + secondTime);
				
				list.remove(dst_jiaban_index);
				String jiaBanTime = infos2.get(jiaBanRowIndexs.get(i-3)).get(jiaban_index);
				list.add(dst_jiaban_index, jiaBanTime);
				
				list.remove(dst_ok_index);
				float jiaban = Float.parseFloat(jiaBanTime);
				if(jiaban >= 1.5f){
					list.add(dst_ok_index, "√");
				}else{
					list.add(dst_ok_index, "");
				}
			}
		}
//		System.out.println(infos);
		// 写表
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet");
		for (int j = 0; j < infos.size(); j++) {
			HSSFRow row = sheet.createRow(j);
			// row.setRowStyle(style);
			List<String> list = infos.get(j);
			for (int k = 0; k < list.size(); k++) {
				HSSFCell cell = row.createCell(k);
				cell.setCellValue(list.get(k));
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

	private String makeJiaBanTimeStr(String orig){
		if(orig.length() > 4){
			return orig.substring(0, 5);
		}
		return orig;
	}
}
