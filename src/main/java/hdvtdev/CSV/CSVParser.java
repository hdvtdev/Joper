package hdvtdev.CSV;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;


public class CSVParser {

    private final long startTime = System.currentTimeMillis();
    private final String csvpath = "/home/hadvart/IdeaProjects/POI/src/main/resources/even.csv";
    private final String xlsxpath = "/home/hadvart/IdeaProjects/POI/src/main/resources/schedule.xlsx";


    public void convertXLSXToCSV(String xlsxFile, String csvFile) {

        try (FileInputStream inputStream = new FileInputStream(xlsxFile);
             Workbook workbook = new XSSFWorkbook(inputStream);
             PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {

                StringBuilder rowString = getStringBuilder(row);

                writer.println(rowString.substring(0, rowString.length() - 1));

            }
            System.out.println("Conversion complete!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static StringBuilder getStringBuilder(Row row) {
        StringBuilder rowString = new StringBuilder();

        for (Cell cell : row) {
            switch (cell.getCellType()) {
                case STRING:
                    rowString.append(cell.getStringCellValue());
                    break;
                case NUMERIC:
                    rowString.append((int)cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    rowString.append(cell.getBooleanCellValue());
                    break;
                default:
                    rowString.append(" ");
            }
            rowString.append(",");
        }
        return rowString;
    }


}
