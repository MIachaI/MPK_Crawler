package save.excel;

import businfov2.BusStop;
import businfov2.CertificationMethod;
import businfov2.VehicleType;
import businfov2.timetable.Timetable;
import businfov2.timetable.TimetableNumberComparator;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static org.apache.poi.hssf.record.ExtendedFormatRecord.CENTER;

public abstract class ExcelSaver {
    public static void save(String path, ArrayList<BusStop> selectedStops, CertificationMethod method) throws IOException {
        ArrayList<Timetable> selectedBuses = new ArrayList<>();
        ArrayList<Timetable> selectedTrams = new ArrayList<>();
        for (BusStop stop : selectedStops) {
            selectedBuses.addAll(stop.getTimetables(VehicleType.BUS, method));
            selectedTrams.addAll(stop.getTimetables(VehicleType.TRAM, method));
        }
        selectedBuses.sort(new TimetableNumberComparator());
        selectedTrams.sort(new TimetableNumberComparator());

        int tramWeekdaySum=0, tramWeekendAvgSum=0;
        int busWeekdaySum=0, busWeekendAvgSum=0;

        FileOutputStream fileOut;
        try (Workbook output = new HSSFWorkbook()) {
            Sheet result = output.createSheet("wynik");

            for (int row = 0; row < 500; row++) {
                result.createRow(row);
                for (int column = 0; column < 20; column++) {
                    result.getRow(row).createCell(column);
                }
            }

            int row = 1;
            // set width of certain columns
            result.setColumnWidth(2, 11*500);
            result.setColumnWidth(3, 11*300);
            result.setColumnWidth(4, 11*300);
            result.setColumnWidth(8, 11*250);
            result.setColumnWidth(9, 11*450);
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
            titleRowStyle.setVerticalAlignment(CENTER);
            titleRowStyle.setAlignment(HorizontalAlignment.CENTER);

            titleRowStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            titleRowStyle.setBorderBottom(CellStyle.BORDER_THIN);
            titleRowStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());

            titleRowStyle.setWrapText(true);
            titleRowStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);

            CellStyle lineCellStyle = output.createCellStyle();
            lineCellStyle.setAlignment(HorizontalAlignment.CENTER);

            if(!selectedTrams.isEmpty()) {
                result.addMergedRegion(new CellRangeAddress(row, row + 1, 1, 1));
                result.addMergedRegion(new CellRangeAddress(row, row + 1, 2, 2));
                result.addMergedRegion(new CellRangeAddress(row, row + 1, 3, 3));
                result.addMergedRegion(new CellRangeAddress(row, row + 1, 4, 4));
                result.addMergedRegion(new CellRangeAddress(row, row + 1, 5, 8));


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

                for (Timetable tram : selectedTrams) {
                    row++;

                    result.getRow(row).getCell(1).setCellValue(tram.lineNumber);
                    result.getRow(row).getCell(1).setCellStyle(lineCellStyle);
                    result.getRow(row).getCell(2).setCellValue(tram.busStopName);
                    result.getRow(row).getCell(3).setCellValue(tram.vehicleType.toString());
                    result.getRow(row).getCell(5).setCellValue(tram.getDeparturesAmount(Timetable.Day.WEEKDAY));
                    result.getRow(row).getCell(6).setCellValue(tram.getDeparturesAmount(Timetable.Day.SATURDAY));
                    result.getRow(row).getCell(7).setCellValue(tram.getDeparturesAmount(Timetable.Day.SUNDAY));
                    result.getRow(row).getCell(8).setCellValue(tram.getWeekendAverage());

                    tramWeekdaySum += tram.getDeparturesAmount(Timetable.Day.WEEKDAY);
                    tramWeekendAvgSum += tram.getWeekendAverage();
                    result.getRow(row).getCell(10).setCellValue(
                            !tram.getWarnings().isEmpty() ? tram.getWarnings().get(0) : ""
                    );

                    // write summary (sum of courses for weekday and weekend avgs)
                    row++;
                    result.addMergedRegion(new CellRangeAddress(row, row, 7, 8));
                    result.getRow(row).getCell(7).setCellValue("Total");
                    result.getRow(row).getCell(7).setCellStyle(lineCellStyle);
                    result.getRow(++row).getCell(7).setCellValue("Weekday");
                    result.getRow(row).getCell(8).setCellValue("Weekend");
                    result.getRow(++row).getCell(7).setCellValue(tramWeekdaySum);
                    result.getRow(row).getCell(8).setCellValue(tramWeekendAvgSum);
                    row += 4;
                }
            }

            if(!selectedBuses.isEmpty()) { // save buses if found any
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

                for(Timetable bus : selectedBuses){
                    row++;
                    result.getRow(row).getCell(1).setCellValue(bus.lineNumber);
                    result.getRow(row).getCell(1).setCellStyle(lineCellStyle);
                    result.getRow(row).getCell(2).setCellValue(bus.busStopName);
                    result.getRow(row).getCell(3).setCellValue(bus.vehicleType.toString());
                    result.getRow(row).getCell(5).setCellValue(bus.getDeparturesAmount(Timetable.Day.WEEKDAY));
                    result.getRow(row).getCell(6).setCellValue(bus.getDeparturesAmount(Timetable.Day.SATURDAY));
                    result.getRow(row).getCell(7).setCellValue(bus.getDeparturesAmount(Timetable.Day.SUNDAY));
                    result.getRow(row).getCell(8).setCellValue(bus.getWeekendAverage());

                    busWeekdaySum += bus.getDeparturesAmount(Timetable.Day.WEEKDAY);
                    busWeekendAvgSum += bus.getWeekendAverage();

                    result.getRow(row).getCell(10).setCellValue(
                            !bus.getWarnings().isEmpty() ? bus.getWarnings().get(0) : ""
                    );
                }
                // write summary (sum of courses for weekday and weekend avgs)
                row+=2;
                result.addMergedRegion(new CellRangeAddress(row, row, 7, 8));
                result.getRow(row).getCell(7).setCellValue("Total");
                result.getRow(row).getCell(7).setCellStyle(lineCellStyle);
                result.getRow(row+1).getCell(7).setCellValue("Weekday");
                result.getRow(row+1).getCell(8).setCellValue("Weekend");
                result.getRow(row+2).getCell(7).setCellValue(busWeekdaySum);
                result.getRow(row+2).getCell(8).setCellValue(busWeekendAvgSum);
                row+=4;
            }

            if(!selectedBuses.isEmpty() || !selectedTrams.isEmpty()){
                result.addMergedRegion(new CellRangeAddress(row+1,row+1,4,7));
                result.addMergedRegion(new CellRangeAddress(row+2,row+2,4,7));
                result.addMergedRegion(new CellRangeAddress(row+3,row+3,4,7));

                result.getRow(row).getCell(8).setCellValue("Weekdays");
                result.getRow(row).getCell(9).setCellValue("Weekend (average)");
                row++;

                result.getRow(row).getCell(4).setCellValue("Daily number of rides by light trains");
                result.getRow(row).getCell(4).setCellStyle(lineCellStyle);
                result.getRow(row).getCell(8).setCellValue(tramWeekdaySum);
                result.getRow(row).getCell(8).setCellStyle(lineCellStyle);
                result.getRow(row).getCell(9).setCellValue(tramWeekendAvgSum);
                result.getRow(row).getCell(9).setCellStyle(lineCellStyle);
                row++;

                result.getRow(row).getCell(4).setCellValue("Daily number of rides by buses");
                result.getRow(row).getCell(4).setCellStyle(lineCellStyle);
                result.getRow(row).getCell(8).setCellValue(busWeekdaySum);
                result.getRow(row).getCell(8).setCellStyle(lineCellStyle);
                result.getRow(row).getCell(9).setCellValue(busWeekendAvgSum);
                result.getRow(row).getCell(9).setCellStyle(lineCellStyle);
                row++;

                result.getRow(row).getCell(4).setCellValue("Daily number of rides - summary");
                result.getRow(row).getCell(4).setCellStyle(lineCellStyle);
                result.getRow(row).getCell(8).setCellValue(tramWeekdaySum+busWeekdaySum);
                result.getRow(row).getCell(9).setCellValue(tramWeekendAvgSum+busWeekendAvgSum);
            }

            fileOut = new FileOutputStream(path + ".xls");
            output.write(fileOut);
        } // try
        fileOut.close();
    }
}
