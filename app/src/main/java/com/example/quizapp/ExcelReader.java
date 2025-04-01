package com.example.quizapp;

import android.content.Context;
import android.util.Pair;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ExcelReader {

    public ExcelReader() {

    }
    private Map<String, Pair<Set<String>, ArrayList<Float>>> deepCopyMap(Map<String, Pair<Set<String>, ArrayList<Float>>> original) {
        Map<String, Pair<Set<String>, ArrayList<Float>>> copy = new HashMap<>();
        for (Map.Entry<String, Pair<Set<String>, ArrayList<Float>>> entry : original.entrySet()) {
            Set<String> newSet = new HashSet<>(entry.getValue().first);
            ArrayList<Float> newList = new ArrayList<>(entry.getValue().second);
            copy.put(entry.getKey(), Pair.create(newSet, newList));
        }
        return copy;
    }
    public  Map<String, Pair<Set<String>, ArrayList<Float>>> readExcelFile(Context context , String internalFileName) throws IOException
    {
        ArrayList<Route> entities = new ArrayList<>();
        FileInputStream file = null;
        FileOutputStream fos = null;
        Workbook workbook = null;
        ArrayList<Integer> dest = new ArrayList<>();
        File internalFile1 = null;
        Map<String, Pair<Set<String>, ArrayList<Float>>> sourceStations = new HashMap<>();
        int count1 = 0;

        try {
            File internalFile = new File(context.getFilesDir(), internalFileName);
            file = new FileInputStream(internalFile);
            workbook = WorkbookFactory.create(file); // XSSFWorkbook for .xlsx format

            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Assuming your data is in specific columns (adjust indexes as needed)
                String column1 = getCellStringValue(row.getCell(0));
                String column2 = getCellStringValue(row.getCell(1));
                String column3 = getCellStringValue(row.getCell(2));
                float column4 = getCellNumericValue(row.getCell(3));
                String column5 = getCellStringValue(row.getCell(4));
                float column6 = getCellNumericValue(row.getCell(5));

                String sourceKey = column1 + "~" + column2;
                String str = "";
                int index = sourceKey.indexOf('~');
                String s = sourceKey.substring(0, index);
                Iterator<Row> rowIterator1 = sheet.iterator();
                int count2 = 0;

                if (!sourceStations.containsKey(column1) && column1 != "~") {
                    ArrayList<Float> arr1 = new ArrayList<>();
                    Set<String> s1 = new HashSet<String>();
                    Pair<Set<String>, ArrayList<Float>> p1 = Pair.create(s1, arr1);
                    sourceStations.putIfAbsent(column1, p1);
                    if (column5 != "" && column5 != "" && column5 != "~") {
                        s1.add(column5);
                        arr1.add(column6);
                        Pair<Set<String>, ArrayList<Float>> p2 = Pair.create(s1, arr1);
                        sourceStations.putIfAbsent(column1, p2);
                    }
                }
                while (rowIterator1.hasNext()) {
                    Row row1 = rowIterator1.next();
                    String col1 = getCellStringValue(row1.getCell(0));
                    if (col1.equals(s) && count2 != count1) {
                        String col2 = getCellStringValue(row1.getCell(1));
                        String col5 = getCellStringValue(row1.getCell(4));
                        float col6 = getCellNumericValue(row1.getCell(5));
                        str += col2;
                        if (sourceStations.containsKey(col1) && col5 != null && col5 != "" && col5 != "~") {
                            Set<String> set = Objects.requireNonNull(sourceStations.get(col1)).first;
                            ArrayList<Float> arr = Objects.requireNonNull(sourceStations.get(col1)).second;
                            set.add(col5);
                            arr.add(col6);
                            dest.add(count2);
                        }
                    }
                    count2++;
                }

                sourceKey += str;
                if (sourceStations.containsKey(s)) {
                    Set<String> s1 = Objects.requireNonNull(sourceStations.get(s)).first;
                    ArrayList<Float> arr4 = Objects.requireNonNull(sourceStations.get(s)).second;
                    sourceStations.remove(s);
                    Pair<Set<String>, ArrayList<Float>> p5 = Pair.create(s1, arr4);
                    sourceStations.putIfAbsent(sourceKey, p5);
                }
                // Add source station to sourceStations map
                Route route = new Route(column1, column2, column3, column4, column5, column6);
                entities.add(route);
                count1++;
            }

            Set<String> dest1 = new HashSet<>();
            for (Map.Entry<String, Pair<Set<String>, ArrayList<Float>>> entry : sourceStations.entrySet()) {
                dest1.add(entry.getKey());
            }
            // Create a copy of the sourceStations to iterate over
            Map<String, Pair<Set<String>, ArrayList<Float>>> sourceStationsCopy = deepCopyMap(sourceStations);
            Set<String> dest2 = new HashSet<>();

            for (Map.Entry<String, Pair<Set<String>, ArrayList<Float>>> entry : sourceStationsCopy.entrySet()) {
                if (entry.getValue() != null) {
                    //System.out.println("Station " + entry.getKey());
                    Set<String> destinations = entry.getValue().first;
                    ArrayList<Float> arr7 = entry.getValue().second;
                    if (destinations != null)
                    {
                        for (String destination : destinations) {
                            Iterator<String> it1 = dest1.iterator();
                            Iterator<Float> it2 = arr7.iterator();
                            int i = 0;
                            while (it1.hasNext() && it2.hasNext()) {
                                String next = it1.next();
                                int index = next.indexOf('~');
                                String s1 = next.substring(0, index);
                                if (destination.equals(s1) && i < arr7.size() ) {
                                    dest2.add(next);
                                    i++;
                                }
                            }
                        }
                    }
                    else
                    {
                        // Handle case where entry.getValue() is null
                        // You can log an error, skip processing, or handle it based on your application's logic
                        System.out.println("WARNING: entry.getValue() is null for source vertex ");
                    }
                    String str1 = entry.getKey();
                    Pair<Set<String>, ArrayList<Float>> pair1 = Pair.create(new HashSet<>(dest2), new ArrayList<>(arr7));
                    //System.out.println(str1);
                    //System.out.println(pair1);
                    sourceStations.put(str1, pair1);
                    dest2.clear();
                }
            }
            if (!dest.isEmpty()) {
                for (int rowIndex : dest) {
                    Row rowToDelete = sheet.getRow(rowIndex);
                    if (rowToDelete != null) {
                        sheet.removeRow(rowToDelete);
                    }
                }
            }
            try{
                internalFile1 = new File(context.getFilesDir(), internalFileName);
                fos = new FileOutputStream(internalFile1);
                workbook.write(fos);
            }
            finally{
                if (workbook != null) {
                    workbook.close();
                }
                if (fos != null) {
                    fos.close();
                }
            }
        }
        finally {
            if (workbook != null) {
                workbook.close();
            }
            if (file != null) {
                file.close();
            }
        }
        System.out.println("Final sourceStations map: ");
        /*for (Map.Entry<String, Pair<Set<String>, ArrayList<Float>>> entry : sourceStations.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }*/

        return sourceStations;
    }

    private static String getCellStringValue(Cell cell) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
            }
        }
        return "";
    }

    private static float getCellNumericValue(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (float) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                try {
                    return Float.parseFloat(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }
}

