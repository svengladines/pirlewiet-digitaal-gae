/**
 *	ExcelImporter.java
 *
 *	Created on 13-okt-2009, 16:54:53
 */
package be.pirlewiet.registrations.web.util;

import static be.pirlewiet.registrations.web.util.Tuple.tuple;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class ExcelImporter {

	private Logger logger = LoggerFactory.getLogger(ExcelImporter.class);

	private static final String EXT2007HEADERS = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	private static final String EXT2003HEADERS = "application/vnd.ms-excel";

	private static final String EXT2003 = "xls";

	private static final String EXT2007 = "xlsx";

	private static final int FIRST_SHEET = 0;

	private static final int FIRST_ROW = 0;

	private static final int FIRST_COL = 0;

	private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

	private String cellDateFormat;

	public ExcelImporter() {

		this.setCellDateFormat(DEFAULT_DATE_FORMAT);
	}

	public Tuple<Boolean, Workbook> validateExcelFile(MultipartFile excelFile)
			throws IOException {

		logger.debug("########################");

		String fileName = excelFile.getOriginalFilename();

		logger.debug("#### validateExcelFile - Receive file --> " + fileName);

		// logger.debug("#### Content-Type : " + excelFile.getContentType());

		if ((StringUtils.endsWithIgnoreCase(fileName, EXT2003))
				|| excelFile.getContentType().contains(EXT2003HEADERS)) {

			logger.debug("#### Type of received file: xls (excel2003)");
			logger.debug("########################");
			return tuple(true,
					(Workbook) new HSSFWorkbook(excelFile.getInputStream()));

		}
		if ((StringUtils.endsWithIgnoreCase(fileName, EXT2007))
				|| excelFile.getContentType().contains(EXT2007HEADERS)) {

			logger.debug("#### Type of received file: xlsx (excel2007)");
			logger.debug("########################");
			return tuple(true, (Workbook) new XSSFWorkbook(excelFile.getInputStream()));
		}

		return tuple(false, null);

	}

	public Tuple<Boolean, Workbook> validateExcelFile(String fileName,
			InputStream inputStream, String contentType) throws IOException {

		logger.debug("########################");

		if ((StringUtils.endsWithIgnoreCase(fileName, EXT2003))
				|| contentType.contains(EXT2003HEADERS)) {

			logger.debug("#### Type of received file: xls (excel2003)");
			logger.debug("########################");
			return tuple(true, (Workbook) new HSSFWorkbook(inputStream));

		}
		if ((StringUtils.endsWithIgnoreCase(fileName, EXT2007))
				|| contentType.contains(EXT2007HEADERS)) {

			logger.debug("#### Type of received file: xlsx (excel2007)");
			logger.debug("########################");
			return tuple(true, (Workbook) new HSSFWorkbook(inputStream));
		}

		return tuple(false, null);

	}

	private boolean columnsOk(Sheet sheet, String... columnNames) {

		Row row = sheet.getRow(FIRST_ROW);

		if (row != null) {

			final int nColumns = row.getLastCellNum();

			logger.debug("#### Column Presents -> number of received headers in a excel file: " + nColumns);

			if (nColumns >= columnNames.length) {

				logger.debug("#### Column Presents -> Valid number of columns in file");

				for (int nr = FIRST_COL; nr < columnNames.length; nr++) {

					String currColumn = row.getCell(nr).getStringCellValue().trim();

					logger.debug("#### Column Presents -> Current Header File: "
							+ currColumn + " = expected header : " + columnNames[nr]);

					if (!columnNames[nr].equalsIgnoreCase(currColumn)) {

						logger.debug("#### Column Presents -> No Match");

						return false;

					}

				}
			} else {

				logger.debug("#### Column Presents -> Invalid number of columns");

				return false;
			}

			logger.debug("########################");

			return true;

		}

		return false;
	}

	private boolean columnsOk(Sheet sheet, int... columnIndexes) {

		Row row = sheet.getRow(FIRST_ROW);

		if (row != null) {

			final int nColumns = row.getLastCellNum();

			for (int index : columnIndexes) {

				if (index < 0) {
					logger.info("column index {} is negative", index);
					return false;
				}

				if (index > nColumns) {
					logger.info("column index {} is bigger than the number of columns: {}", index, nColumns);
					return false;
				}

			}

			logger.debug("columns {} are ok", columnIndexes);
			return true;

		}

		return false;
	}

	private String getCelValue(Cell currentCell) {

		String value = null;

		switch (currentCell.getCellType()) {

		case Cell.CELL_TYPE_BLANK:
			value = "";
			break;

		case Cell.CELL_TYPE_BOOLEAN:
			value = String.valueOf(currentCell.getBooleanCellValue());
			break;

		case Cell.CELL_TYPE_NUMERIC:

			if (DateUtil.isCellDateFormatted(currentCell)) {

				SimpleDateFormat formatter = new SimpleDateFormat(this.getCellDateFormat());

				value = formatter.format(currentCell.getDateCellValue());

			} else {

				value = String.valueOf(currentCell.getNumericCellValue());
			}

			break;

		case Cell.CELL_TYPE_STRING:
			value = currentCell.getStringCellValue();
			break;

		}

		return value;
	}

	public String getCellDateFormat() {
		return cellDateFormat;
	}

	public void setCellDateFormat(String cellDateFormat) {
		this.cellDateFormat = cellDateFormat;
	}

	private List<String[]> getExcelData(Boolean isValid, Workbook workbook, int... columnIndexes) {

		List<String[]> data = new ArrayList<String[]>();

		int nTotalEmpty = 0;

		// check if the given file is a valid excel file
		if (isValid) {

			Sheet sheet = workbook.getSheetAt(FIRST_SHEET);

			final int nRows = sheet.getLastRowNum() + 1;

			logger.debug("#### get ExcelData - number of rows ---> " + nRows);

			// check if the file has data rows (no header row)
			if (nRows >= 1) {

				// check if the currentsheet from the workbook has the right
				// headers or some of the headers given
				if (columnsOk(sheet, columnIndexes)) {

					// Loop all the rows from the current sheet
					for (int r = (FIRST_ROW); r <= nRows; r++) {

						logger.debug("row {}...", r);

						Row currentRow = sheet.getRow(r);

						if (currentRow != null) {
							String[] row = new String[columnIndexes.length];

							int emptyCells = 0;

							// Loop all the columns from the current row
							for (int c : columnIndexes) {

								String value = null;

								logger.debug("row [{}],cell [{}]...", r, c);

								Cell currentCell = currentRow.getCell(c);

								// if the current cell contains data - get it out
								if (currentCell != null) {

									value = getCelValue(currentCell);

									logger.debug("[" + r + ":" + c + "]" + value + "\t");

								}

								row[c] = value;

								// if the data was blank
								if (isEmpty(row[c])) {

									emptyCells++;
								}
							}

							// if the row wasn't blank add it to the list
							if (emptyCells < columnIndexes.length) {

								data.add(row);

								logger.debug("-> Added");

							} else {

								nTotalEmpty++;

								logger.debug("Row " + r + ":  empty");

							}

							logger.debug("\n");
						} else {

							nTotalEmpty++;
						}

					}
				} else {

					throw new RuntimeException("The given column indexes are not consistant with the model");
				}
			} else {

				throw new RuntimeException("No data rows found");
			}

		} else {

			throw new RuntimeException("Not a xls or xlsx file");
		}

		logger.debug("-----------------------------------");
		logger.debug("| valid number of rows found : " + data.size() + "  |");
		logger.debug("| Total of empty rows : " + nTotalEmpty + "         |");
		logger.debug("| Total number of rows : " + (data.size() + nTotalEmpty) + "        |");
		logger.debug("-----------------------------------");

		return data;

	}
	
	protected boolean isEmpty( String s ) {
		return (s == null)  || s.isEmpty();
	}
}