package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

    /** 错误信息 */
    private static String errorInfo;

    public static String getErrorInfo() {
        return errorInfo;
    }


    private static boolean validateExcel(String filePath) {
        /** 检查文件名是否为空或者是否是Excel格式的文件 */
        if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
            errorInfo = "文件名不是excel格式";
            return false;
        }
        /** 检查文件是否存在 */
        File file = new File(filePath);
        if (filePath == null || file == null || !file.exists()) {
            errorInfo = "文件不存在";
            return false;
        }
        return true;
    }

    public static List<List<ExcelPair>> readForObject(String filePath) {
        List<List<ExcelPair>> dataLst = new ArrayList<>();
        InputStream is = null;
        try {
            /** 验证文件是否合法 */
            if (!validateExcel(filePath)) {
                System.out.println(errorInfo);
                return null;
            }
            /** 判断文件的类型，是2003还是2007 */
            boolean isExcel2003 = isExcel2003(filePath);
            /** 调用本类提供的根据流读取的方法 */
            File file = new File(filePath);
            is = new FileInputStream(file);
            dataLst = readBookForObject(is, isExcel2003);
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        /** 返回最后读取的结果 */
        return dataLst;
    }

    private static List<List<ExcelPair>> readBookForObject(InputStream inputStream, boolean isExcel2003) {
        List<List<ExcelPair>> dataLst = null;
        try {
            /** 根据版本选择创建Workbook的方式 */
            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(inputStream);
            } else {
                wb = new XSSFWorkbook(inputStream);

            }
            dataLst = readExcelForObject(wb);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return dataLst;
    }

    private static List<List<ExcelPair>> readExcelForObject(Workbook wb) {
        List<List<ExcelPair>> dataLst = new ArrayList<>();

        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        /** 得到第一个shell */
        Sheet sheet = wb.getSheetAt(0);
        /** 得到Excel的行数 */
        int totalRows = sheet.getPhysicalNumberOfRows();
        /** 得到Excel的列数 */
        int totalCells = 0;
        if (totalRows >= 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        /** 循环Excel的行 不取第一行 */
        for (int r = 0; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            List<ExcelPair> rowLst = new ArrayList<>();
            /** 循环Excel的列 */
            for (int c = 0; c <= totalCells; c++) {
                Cell cell = row.getCell(c);
                Object cellValue = null;
                if (null != cell) {
                    // 以下是判断数据的类型
                    cellValue = getCellValue(cell);
                    rowLst.add(new ExcelPair(cellValue, cell.getCellType(), cell.getCellStyle()));
                }else {
                	rowLst.add(new ExcelPair("", Cell.CELL_TYPE_BLANK, wb.createCellStyle()));
				}
            }

            if (rowLst != null && rowLst.size() > 0) {
                /** 保存第r行的第c列 */
                dataLst.add(rowLst);
            }

        }
        return dataLst;
    }

    public static List<List<String>> read(String filePath) {
        List<List<String>> dataLst = new ArrayList<List<String>>();
        InputStream is = null;
        try {
            /** 验证文件是否合法 */
            if (!validateExcel(filePath)) {
                System.out.println(errorInfo);
                return null;
            }
            /** 判断文件的类型，是2003还是2007 */
            boolean isExcel2003 = isExcel2003(filePath);
            
            /** 调用本类提供的根据流读取的方法 */
            File file = new File(filePath);
            is = new FileInputStream(file);
            dataLst = readBook(is, isExcel2003);
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        /** 返回最后读取的结果 */
        return dataLst;
    }


	public static boolean isExcel2003(String filePath) {
		boolean isExcel2003 = true;
		if (filePath.endsWith(".xlsx")) {
		    isExcel2003 = false;
		}
		return isExcel2003;
	}


    public static List<List<String>> readBook(InputStream inputStream, boolean isExcel2003) {
        List<List<String>> dataLst = null;
        try {
            /** 根据版本选择创建Workbook的方式 */
            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(inputStream);
            } else {
                wb = new XSSFWorkbook(inputStream);

            }
            dataLst = readExcel(wb);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return dataLst;
    }


    private static List<List<String>> readExcel(Workbook wb) {
        List<List<String>> dataLst = new ArrayList<List<String>>();

        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        /** 得到第一个shell */
        Sheet sheet = wb.getSheetAt(0);
        /** 得到Excel的行数 */
        int totalRows = sheet.getPhysicalNumberOfRows();
        /** 得到Excel的列数 */
        int totalCells = 0;
        if (totalRows >= 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        /** 循环Excel的行 不取第一行 */
        for (int r = 0; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            List<String> rowLst = new ArrayList<String>();
            /** 循环Excel的列 */
            for (int c = 0; c <= totalCells; c++) {
                Cell cell = row.getCell(c);
                String cellValue = "";
                if (null != cell) {
                    // 以下是判断数据的类型
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                Date date = cell.getDateCellValue();
                                if (date != null) {
                                    cellValue = new SimpleDateFormat("yyyy-MM-dd")
                                            .format(date);
                                } else {
                                    cellValue = "";
                                }
                            } else {
                                cellValue = cell.getNumericCellValue() + "";
                            }
                            break;
                        case HSSFCell.CELL_TYPE_STRING: // 字符串
                            cellValue = cell.getStringCellValue();
                            break;
                        case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                            cellValue = cell.getBooleanCellValue() + "";
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA: // 公式
                            try {
                                CellValue value;
                                value = evaluator.evaluate(cell);
                                switch (value.getCellType()) {              //判断公式类型
                                    case Cell.CELL_TYPE_BOOLEAN:
                                        cellValue  = value.getBooleanValue() + "";
                                        break;
                                    case Cell.CELL_TYPE_NUMERIC:
                                        // 处理日期
                                        if (DateUtil.isCellDateFormatted(cell)) {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = cell.getDateCellValue();
                                            cellValue = format.format(date);
                                        } else {
                                            cellValue  = value.getNumberValue() + "";
                                        }
                                        break;
                                    case Cell.CELL_TYPE_STRING:
                                        cellValue  = value.getStringValue();
                                        break;
                                    case Cell.CELL_TYPE_BLANK:
                                        cellValue = "";
                                        break;
                                    case Cell.CELL_TYPE_ERROR:
                                        cellValue = "";
                                        break;
                                    case Cell.CELL_TYPE_FORMULA:
                                        cellValue = "";
                                        break;
                                }
                            } catch (Exception e) {
                                cellValue = cell.getStringCellValue().toString();
                                cell.getCellFormula();
                            }
                            break;
                        case HSSFCell.CELL_TYPE_BLANK: // 空值
                            cellValue = "";
                            break;
                        case HSSFCell.CELL_TYPE_ERROR: // 故障
                            cellValue = "非法字符";
                            break;
                        default:
                            cellValue = "未知类型";
                            break;
                    }
                }

//                if (StringUtil.isNotEmpty(cellValue) ) {
                rowLst.add(cellValue);
//                }

            }

            if (rowLst != null && rowLst.size() > 0) {
                /** 保存第r行的第c列 */
                dataLst.add(rowLst);
            }

        }
        return dataLst;
    }

    public static Object getCellValue(Cell cell) {
        Object cellValue;
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    cellValue = cell.getDateCellValue();
                } else {
                    cellValue = cell.getNumericCellValue();
                }
                break;
            case HSSFCell.CELL_TYPE_STRING: // 字符串
                cellValue = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                cellValue = cell.getBooleanCellValue();
                break;
            case HSSFCell.CELL_TYPE_FORMULA: // 公式
                cellValue = cell.getCellFormula();
                break;
            case HSSFCell.CELL_TYPE_BLANK: // 空值
                cellValue = "";
                break;
            case HSSFCell.CELL_TYPE_ERROR: // 故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        if(cellValue == null){
            cellValue="";
        }
        return cellValue;
    }

    public static void setCellValue(Object value, Cell cell) {
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    cell.setCellValue((Date) value);
                } else {
                    cell.setCellValue((Double) value);
                }
                break;
            case HSSFCell.CELL_TYPE_STRING: // 字符串
                cell.setCellValue((String) value);
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                cell.setCellValue((Boolean) value);
                break;
            case HSSFCell.CELL_TYPE_FORMULA: // 公式
                cell.setCellFormula((String) value);
                break;
            case HSSFCell.CELL_TYPE_BLANK: // 空值
                cell.setCellValue((String) value);
                break;
            case HSSFCell.CELL_TYPE_ERROR: // 故障
                cell.setCellValue((String) value);
                break;
            default:
                System.out.println("设值错误");
                break;
        }
    }
    
    public static <E> void printlnList(List<E> list){
    	for (E e : list) {
    		System.out.println(e);
		}
    }
}