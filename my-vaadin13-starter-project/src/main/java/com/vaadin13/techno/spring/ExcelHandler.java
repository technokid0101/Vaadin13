package com.vaadin13.techno.spring;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.DAO.Customer;

public class ExcelHandler {
	private static XSSFWorkbook workbook;
	private static XSSFSheet spreadsheet;
	private static XSSFCellStyle cellstyleColumn;
	private static XSSFFont font, font1;
	private static XSSFCellStyle cellstyleRows;

	public static File exportExcel(List<Customer> customers) {
		File file = null;
		try {

			Comparator<Customer> comparator = new Comparator<Customer>() {
				@Override
				public int compare(Customer o1, Customer o2) {
					return o1.getFirstName().compareTo(o2.getFirstName());
				}
			};
			customers.sort(comparator);
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet(" Customer Data ");
			cellstyleColumn = workbook.createCellStyle();
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 18);
			font.setFontName("TIMES NEW ROMAN");
			font.setBold(true);
			font.setUnderline((byte) 1);
			font.setColor(IndexedColors.WHITE.getIndex());
			cellstyleColumn.setFont(font);
			cellstyleColumn.setFillBackgroundColor(IndexedColors.DARK_BLUE.getIndex());
			cellstyleColumn.setFillPattern(FillPatternType.LESS_DOTS);
			cellstyleColumn.setBorderBottom(BorderStyle.THICK);
			cellstyleColumn.setBorderTop(BorderStyle.THICK);
			cellstyleColumn.setBorderLeft(BorderStyle.THICK);
			cellstyleColumn.setBorderRight(BorderStyle.THICK);

			Row headerRow = spreadsheet.createRow(0);
			String[] strColumns = { "First Name", "Last Name", "Birth Date", "Status", "Email" };
			for (int i = 0; i < strColumns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(strColumns[i]);
				cell.setCellStyle(cellstyleColumn);
			}

			cellstyleRows = workbook.createCellStyle();
			font1 = workbook.createFont();
			font1.setFontHeightInPoints((short) 18);
			font1.setFontName("TIMES NEW ROMAN");
			cellstyleRows.setFont(font1);
			cellstyleRows.setBorderBottom(BorderStyle.THICK);
			cellstyleRows.setBorderTop(BorderStyle.THICK);
			cellstyleRows.setBorderLeft(BorderStyle.THICK);
			cellstyleRows.setBorderRight(BorderStyle.THICK);

			int rowNum = 1;

			for (Customer customer : customers) {
				Row row = spreadsheet.createRow(rowNum++);
				Cell cell1 = row.createCell(0);
				cell1.setCellStyle(cellstyleRows);
				cell1.setCellValue(customer.getFirstName());

				Cell cell2 = row.createCell(1);
				cell2.setCellStyle(cellstyleRows);
				cell2.setCellValue(customer.getLastName());

				Cell cell3 = row.createCell(2);
				cell3.setCellStyle(cellstyleRows);
				cell3.setCellValue(customer.getBirthDate().toString());

				Cell cell5 = row.createCell(3);
				cell5.setCellStyle(cellstyleRows);
				cell5.setCellValue(customer.getStatus().toString());

				Cell cell6 = row.createCell(4);
				cell6.setCellStyle(cellstyleRows);
				cell6.setCellValue(customer.getEmail());
			}

//			 Resize all columns to fit the content size
			for (int i = 0; i < strColumns.length; i++) {
				spreadsheet.autoSizeColumn(i);
			}
			String strFileName;
			String strCurrentTime = new SimpleDateFormat("hh_mm_ss").format(new Timestamp(System.currentTimeMillis()));
			strFileName = "CusomerReport_" + Utilities.getCurrentDate() + "_" + strCurrentTime + ".xlsx";
			file = new File(System.getProperty("user.home") + "/Documents/" + strFileName);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			System.out.println("Caught an Exception " + e.getMessage());
			e.printStackTrace();
		}
		return file;
	}
}
