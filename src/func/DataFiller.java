package func;

import javafx.scene.control.TreeItem;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DataFiller {
    /*
     * show class directory by treeview and only show directory containing the file of which suffix is ".txt".
     * */
    public static TreeItem initTreeViewbyDir(File file_dirs) {
        TreeItem<String> root = new TreeItem<>(file_dirs.getName());
        File[] child_files = file_dirs.listFiles();
        for (File childFile : child_files) {
//            System.out.println(childFile.getAbsolutePath());
            if (childFile.isDirectory()) {
//                String[] files = childFile.list();
//                if (files.length == 0)
//                    continue;
//                } else {
//                    int i = 0, j = 0;
//                    for (; i < files.length; i++) {
//                        if (files[i].getName().endsWith(".txt"))
//                            break;
//                        else if (files[i].isDirectory()) {
//                            j++;
//                            continue;
//                        }
//                    }
//                    if (i == files.length || j > 0) continue;
//                }
                root.getChildren().add(initTreeViewbyDir(childFile));
            } else {
//                if (!childFile.getName().endsWith(".txt")) continue;
                root.getChildren().add(new TreeItem<>(FileIO.getFileNameNoSuffix(childFile)));
            }
        }
//        root.setExpanded(true);
        return root;

    }


    /**
     * transform the data of excel into map. And the list,key of which equals
     * -1,stores the header of column.
     *
     * @return
     */
    public static Map<Integer, List<String>> getDataMap(File excelFile) {
        Map<Integer, List<String>> map = null;

        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelFile));
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            map = new HashMap<>();
            int i = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.iterator();
                List<String> list = new ArrayList<>();
                int j = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if ((cell.getColumnIndex() == 3 || cell.getColumnIndex() == 4) && cell.getRowIndex() >= 7) {
                        cell.setCellType(CellType.STRING);
                        list.add(j++, cell.getStringCellValue());
                    }
                }
                if (list.size() != 0) {
//                    System.out.println(i);
//                    System.out.println("--" + list.get(0)` + list.get(1) + list.size() + "--");
                    if (list.get(0).length() < 12) break;
                    map.put(i++, list);
                }
            }
//            System.out.println(map.size());
            workbook.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return map;
    }

/*    public static void main(String[] args) {
//        File file = new File("E:\\char_\\OneDrive\\Projects\\javastudio_3\\项目说明及其数据ReadMe\\Excel样例\\成绩表.xls");
        File file = new File("./src/excelsamples/16计科1.xls");
        Map<Integer, List<String>> map = getDataMap(file);
        for (int i = 0; i < map.size(); i++) {
            List<String> list = map.get(i);
            list.forEach(System.out::println);
        }
    }*/
}

