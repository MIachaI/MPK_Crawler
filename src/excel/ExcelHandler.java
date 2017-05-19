package excel;

import businfo.busstop.BusInfo;
import businfo.lists.ListContainer;
import businfo.lists.MPKList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.apache.poi.hssf.record.ExtendedFormatRecord.CENTER;

/**
 * Created by umat on 17.05.17.
 */
public class ExcelHandler {
    // TODO reduce code

    public static void saveExcel(ListContainer listContainer, String path) throws IOException {
        System.out.println("rozpoczeto dzialanie metody saveExcel");
        FileOutputStream fileOut;

        try (Workbook output = new HSSFWorkbook()) {
            Sheet result = output.createSheet("wynik");

            for (int row = 0; row < 100; row++) {
                result.createRow(row);
                for (int column = 0; column < 100; column++) {
                    result.getRow(row).createCell(column);
                }
            }

            ArrayList<BusInfo> tramList = listContainer.getOnlyTrams();
            ArrayList<BusInfo> busList = listContainer.getOnlyBuses();

            int row = 0;

            // set style for title row
            CellStyle titleRowStyle = output.createCellStyle();
            Font font = output.createFont();
            font.setColor(HSSFColor.WHITE.index);
            font.setBold(true);
            font.setFontName("Calibri");
            font.setFontHeightInPoints((short) 11);
            titleRowStyle.setFont(font);
            titleRowStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
            titleRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            titleRowStyle.setAlignment(HorizontalAlignment.CENTER);
            titleRowStyle.setVerticalAlignment(CENTER);
            titleRowStyle.setWrapText(true);
            titleRowStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);

            if(!tramList.isEmpty()){ // if there are trams on the list
                result.addMergedRegion(new CellRangeAddress(row, row+1, 1, 1));
                result.addMergedRegion(new CellRangeAddress(row, row+1, 2, 2));
                result.addMergedRegion(new CellRangeAddress(row, row+1, 3, 3));
                result.addMergedRegion(new CellRangeAddress(row, row+1, 4, 4));
                result.addMergedRegion(new CellRangeAddress(row, row+1, 5, 8));


                result.getRow(row).getCell(1).setCellStyle(titleRowStyle);
                result.getRow(row).getCell(2).setCellStyle(titleRowStyle);
                result.getRow(row).getCell(3).setCellStyle(titleRowStyle);
                result.getRow(row).getCell(4).setCellStyle(titleRowStyle);
                result.getRow(row).getCell(5).setCellStyle(titleRowStyle);

                // add values
                result.getRow(row).getCell(1).setCellValue("Line no.");
                result.getRow(row).getCell(2).setCellValue("Stop name");
                result.getRow(row).getCell(3).setCellValue("Light train / Bus");
                result.getRow(row).getCell(4).setCellValue("Distance from bulding");
                result.getRow(row).getCell(5).setCellValue("Number of rides");

                row += 2;
                result.getRow(row).getCell(5).setCellValue("Weekday");
                result.getRow(row).getCell(6).setCellValue("Weekend");
                result.getRow(row).getCell(7).setCellValue("Weekend");
                result.getRow(row).getCell(8).setCellValue("Average");

                int tramWeekdaySum = 0, tramWeekendAvgSum = 0;
                for(BusInfo tramInfo : tramList){
                    row++;
                    result.getRow(row).getCell(1).setCellValue(tramInfo.getLineNumberString());
                    result.getRow(row).getCell(2).setCellValue(tramInfo.getStreetName());
                    result.getRow(row).getCell(3).setCellValue(tramInfo.getVehicleType());
                    result.getRow(row).getCell(5).setCellValue(tramInfo.getWeekdayCourseCount());
                    result.getRow(row).getCell(6).setCellValue(tramInfo.getSaturdayCourseCount());
                    result.getRow(row).getCell(7).setCellValue(tramInfo.getSundayCourseCount());
                    result.getRow(row).getCell(8).setCellValue((tramInfo.getSaturdayCourseCount() + tramInfo.getSundayCourseCount()) / 2);

                    tramWeekdaySum += tramInfo.getWeekdayCourseCount();
                    tramWeekendAvgSum += (tramInfo.getSaturdayCourseCount() + tramInfo.getSundayCourseCount()) / 2;
                    if (!tramInfo.getWarnings().isEmpty()) {
                        for (String warning : tramInfo.getWarnings()) {
                            result.getRow(row).getCell(10).setCellValue(warning);
                        }
                    }
                }
                // write summary (sum of courses for weekday and weekend avgs)
                row++;
                result.addMergedRegion(new CellRangeAddress(row, row, 7, 8));
                result.getRow(row).getCell(7).setCellValue("Total");
                result.getRow(++row).getCell(7).setCellValue("Weekday");
                result.getRow(row).getCell(8).setCellValue("Weekend");
                result.getRow(++row).getCell(7).setCellValue(tramWeekdaySum);
                result.getRow(row).getCell(8).setCellValue(tramWeekendAvgSum);
                row += 4;
            }

            if(!busList.isEmpty()){ // save buses if found any
                result.addMergedRegion(new CellRangeAddress(row, row+1, 1, 1));
                result.addMergedRegion(new CellRangeAddress(row, row+1, 2, 2));
                result.addMergedRegion(new CellRangeAddress(row, row+1, 3, 3));
                result.addMergedRegion(new CellRangeAddress(row, row+1, 4, 4));
                result.addMergedRegion(new CellRangeAddress(row, row+1, 5, 8));

                result.getRow(row).getCell(1).setCellStyle(titleRowStyle);
                result.getRow(row).getCell(2).setCellStyle(titleRowStyle);
                result.getRow(row).getCell(3).setCellStyle(titleRowStyle);
                result.getRow(row).getCell(4).setCellStyle(titleRowStyle);
                result.getRow(row).getCell(5).setCellStyle(titleRowStyle);

                // add values
                result.getRow(row).getCell(1).setCellValue("Line no.");
                result.getRow(row).getCell(2).setCellValue("Stop name");
                result.getRow(row).getCell(3).setCellValue("Light train / Bus");
                result.getRow(row).getCell(4).setCellValue("Distance from bulding");
                result.getRow(row).getCell(5).setCellValue("Number of rides");

                row += 2;
                result.getRow(row).getCell(5).setCellValue("Weekday");
                result.getRow(row).getCell(6).setCellValue("Weekend");
                result.getRow(row).getCell(7).setCellValue("Weekend");
                result.getRow(row).getCell(8).setCellValue("Average");

                int busWeekdaySum = 0, busWeekendAvgSum = 0;
                for(BusInfo busInfo : busList){
                    row++;
                    result.getRow(row).getCell(1).setCellValue(busInfo.getLineNumberString());
                    result.getRow(row).getCell(2).setCellValue(busInfo.getStreetName());
                    result.getRow(row).getCell(3).setCellValue(busInfo.getVehicleType());
                    result.getRow(row).getCell(5).setCellValue(busInfo.getWeekdayCourseCount());
                    result.getRow(row).getCell(6).setCellValue(busInfo.getSaturdayCourseCount());
                    result.getRow(row).getCell(7).setCellValue(busInfo.getSundayCourseCount());
                    result.getRow(row).getCell(8).setCellValue((busInfo.getSaturdayCourseCount() + busInfo.getSundayCourseCount()) / 2);

                    busWeekdaySum += busInfo.getWeekdayCourseCount();
                    busWeekendAvgSum += (busInfo.getSaturdayCourseCount() + busInfo.getSundayCourseCount()) / 2;
                    if (!busInfo.getWarnings().isEmpty()) {
                        for (String warning : busInfo.getWarnings()) {
                            result.getRow(row).getCell(10).setCellValue(warning);
                        }
                    }
                }
                // write summary (sum of courses for weekday and weekend avgs)
                row++;
                result.addMergedRegion(new CellRangeAddress(row, row, 7, 8));
                result.getRow(row).getCell(7).setCellValue("Total");
                result.getRow(++row).getCell(7).setCellValue("Weekday");
                result.getRow(row).getCell(8).setCellValue("Weekend");
                result.getRow(++row).getCell(7).setCellValue(busWeekdaySum);
                result.getRow(row).getCell(8).setCellValue(busWeekendAvgSum);
            }

            fileOut = new FileOutputStream(path + "out.xls");
            output.write(fileOut);
        }
        fileOut.close();
    }
}
