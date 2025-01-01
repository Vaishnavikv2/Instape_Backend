package com.instape.app.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelGenerator {

	public static <T> void generateExcel(List<T> data, List<String> columnOrder, String filePath)
			throws IOException, IllegalAccessException {
		// Create a new workbook and a sheet
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Data");

		// Create a header row based on the column order
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < columnOrder.size(); i++) {
			Cell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(columnOrder.get(i));
		}

		// Iterate through the data list and populate the rows
		int rowIdx = 1;
		for (T item : data) {
			Row row = sheet.createRow(rowIdx++);

			// Use reflection to get fields and set cell values based on the column order
			for (int colIdx = 0; colIdx < columnOrder.size(); colIdx++) {
				String columnName = columnOrder.get(colIdx);

				// Get the field value using reflection
				Field field = getFieldByName(item.getClass(), columnName);
				if (field != null) {
					field.setAccessible(true);
					Object value = field.get(item);

					// Set cell value based on field value
					Cell cell = row.createCell(colIdx);
					setCellValue(cell, value);
				}
			}
		}

		// Write the workbook to the given file path
		try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
			workbook.write(fileOut);
		}
		workbook.close();
	}

	private static Field getFieldByName(Class<?> clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void setCellValue(Cell cell, Object value) {
		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);
		} else if (value instanceof Double) {
			cell.setCellValue((Double) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof String) {
			cell.setCellValue((String) value);
		} else {
			cell.setCellValue(value.toString());
		}
	}
}
