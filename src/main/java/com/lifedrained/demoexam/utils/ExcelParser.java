package com.lifedrained.demoexam.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.eval.PercentEval;
import org.apache.poi.ss.formula.ptg.PercentPtg;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ExcelParser {
    //cпец символы для парсинга в CSV
    private static final String PAGE_SEPARATOR = "#%";
    public static String DELIMITER = ";";
    public static String LINE_SEPARATOR = "\n";

    //ассинхронный метод парсинга в CSV
    public static void parseExcelToCSV(File file, BiConsumer<List<String>, Throwable> whenComplete) {

        new Thread(() -> {
            try (FileInputStream fis = FileUtils.openInputStream(file)) {
                //Создание принтера и рабочей книги для парсинга xlsx файлов
                CSVPrinter printer = getCSVPrinter();
                Workbook workbook = WorkbookFactory.create(fis);
                //Проход по каждой ячейке
                workbook.sheetIterator().forEachRemaining(sheet -> {
                    sheet.rowIterator().next();
                    sheet.rowIterator().forEachRemaining(row -> {

                        List<String> record = new ArrayList<>();
                        row.cellIterator().forEachRemaining(cell -> {

                            String cellValue = null;
                            //Обработка значений ячейки
                            switch (cell.getCellType()) {
                                case STRING -> cellValue = cell.getStringCellValue().trim();
                                case NUMERIC -> {

                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        cellValue = DateUtils.parseLocalTemporal(
                                                DateUtil.getJavaDate(cell.getNumericCellValue()));
                                    }else if (cell.getCellStyle().getDataFormatString().contains("%")) {
                                        double value = cell.getNumericCellValue()*100;
                                        BigDecimal bd = new BigDecimal(value);
                                        bd = bd.setScale(2, RoundingMode.HALF_UP);
                                        cellValue = bd.toPlainString()+"%";
                                    }else {
                                        cellValue = String.valueOf(cell.getNumericCellValue());
                                    }

                                }
                                case BOOLEAN -> cellValue = String.valueOf(cell.getBooleanCellValue());
                                case FORMULA -> {
                                    cellValue = cell.getCellFormula();
                                }
                            }

                            record.add(cellValue);

                        });
                        try {
                            printer.printRecord(record);
                        } catch (IOException e) {
                            whenComplete.accept(null, e);
                            throw new RuntimeException(e);
                        }
                    });

                });

                whenComplete.accept( parsePrinter(printer), null);
            } catch (IOException e) {
                whenComplete.accept( null, e);
                throw new RuntimeException(e);
            }

        }).start();

    }

    //Метод для создания CSV принтера(записывает данные в CSV)
    private static CSVPrinter getCSVPrinter() throws IOException {
        StringWriter sw = new StringWriter();

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setDelimiter(DELIMITER)
                .setIgnoreEmptyLines(true)
                .setRecordSeparator(LINE_SEPARATOR)
                .setEscape(CSVFormat.EXCEL.getEscapeCharacter())
                .get();

        return new CSVPrinter(sw, csvFormat);
    }

    //парсинг принтера в коллекцию
    private static List<String> parsePrinter(CSVPrinter printer) {
        List<String> entries = new ArrayList<>();

        StringWriter sw = (StringWriter) printer.getOut();

        entries.addAll(Arrays.stream(sw.toString().split(PAGE_SEPARATOR)).toList());

        entries.forEach(System.out::println);
        return entries;
    }
}
