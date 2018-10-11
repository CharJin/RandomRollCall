package func;

import com.sun.javafx.tk.FileChooserType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import tray.notification.NotificationType;

import java.io.*;
import java.util.*;

public class FileIO {
    public static File openSystemFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开Excel文件(可多选)");
        ExtensionFilter extensionFilter = new ExtensionFilter("EXCEL", "*.xls");
        fileChooser.getExtensionFilters().addAll(extensionFilter);
        File file = fileChooser.showOpenDialog(stage);
        return file;
    }

    public static List<File> openMultiSystemFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Excel");
        ExtensionFilter extensionFilter = new ExtensionFilter("EXCEL", "*.xls");
        fileChooser.getExtensionFilters().addAll(extensionFilter);
        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        return files;
    }

    public static File openSystemDirectory(Stage stage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("获取班级信息文件夹");
        File fileDir = directoryChooser.showDialog(stage);
        return fileDir;
    }

    public static File saveFile(Stage stage, String initialFileName, File initialDirectory) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(initialFileName);
        if (initialDirectory != null)
            fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.setTitle("保存学生信息");
        ExtensionFilter extensionFilter = new ExtensionFilter("TXT", "*.txt");
        fileChooser.getExtensionFilters().addAll(extensionFilter);
        File file = fileChooser.showSaveDialog(stage);
        return file;
    }

    /**
     * export single file
     *
     * @param stage
     * @param excelFile
     */
    public static void exportClassRollBook(Stage stage, File excelFile) {
        Map<Integer, List<String>> classMap = DataFiller.getDataMap(excelFile);
        File classFile = saveFile(stage, getFileNameNoSuffix(excelFile), null);
        if (classFile == null) return;
        try {
            FileWriter writer = new FileWriter(classFile);
            BufferedWriter buffer = new BufferedWriter(writer);
            for (int i = 0; i < classMap.size(); i++) {
                List<String> stu_list = classMap.get(i);
                buffer.write(stu_list.get(0) + " " + stu_list.get(1));
                buffer.newLine();
            }
            buffer.close();
            writer.close();
            InfoTip.showTrayNotification("信息提示", "学生名册导出成功!", NotificationType.SUCCESS);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * export multiple files (List<File>)
     *
     * @param stage
     * @param excelFiles
     */
    public static void exportClassRollBook(Stage stage, List<File> excelFiles) {
        if (excelFiles == null) return;
        Iterator<File> iterator = excelFiles.iterator();
        File initialDirectory = null;   // record the initial directory path
        boolean isSucceed = true;
        while (iterator.hasNext()) {
            File excelFile = iterator.next();
            Map<Integer, List<String>> classMap = DataFiller.getDataMap(excelFile);
            File classFile = saveFile(stage, getFileNameNoSuffix(excelFile), initialDirectory);
            if (classFile == null) {
                isSucceed = false;
                continue;
            }
            initialDirectory = classFile.getParentFile();
            try {
                FileWriter writer = new FileWriter(classFile);
                BufferedWriter buffer = new BufferedWriter(writer);
                for (int i = 0; i < classMap.size(); i++) {
                    List<String> stu_list = classMap.get(i);
                    buffer.write(stu_list.get(0) + " " + stu_list.get(1));
                    buffer.newLine();
                }
                buffer.close();
                writer.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }
        if (isSucceed)
            InfoTip.showTrayNotification("信息提示", "学生名册导出成功!", NotificationType.SUCCESS);
    }

    /**
     * get student info from the ".txt" file then put it into a map.
     *
     * @param txtFile
     * @return
     */
    public static Map<Integer, String> getStudentMap(File txtFile) {
        Map<Integer, String> classMap = new HashMap<>();
        try {
            FileReader reader = new FileReader(txtFile);
            BufferedReader buffer = new BufferedReader(reader);
            String stuInfo, prefix_id = null;
            int i = 1;
            while ((stuInfo = buffer.readLine()) != null) {
                String[] arr_stuInfo = stuInfo.split(" ");
                if (prefix_id == null) prefix_id = arr_stuInfo[0].substring(0, arr_stuInfo[0].length() - 2);
                classMap.put(i++, arr_stuInfo[1]);
//                System.out.println(arr_stuInfo[0]);
            }
//            System.out.println(prefix_id);
            classMap.put(-1, prefix_id);
            buffer.close();
            reader.close();
//            System.out.println(classMap.size());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return classMap;

    }

/*
    public static Map<String, String> getStudentMap(File txtFile) {
        Map<String, String> classMap = new HashMap<>();
        try {
            FileReader reader = new FileReader(txtFile);
            BufferedReader buffer = new BufferedReader(reader);
            String stuInfo;
            while ((stuInfo = buffer.readLine()) != null) {
                String[] arr_stuInfo = stuInfo.split(" ");
                classMap.put(arr_stuInfo[0], arr_stuInfo[1]);
//                System.out.println(arr_stuInfo[0]);
            }
            classMap.put("-1", classMap.keySet().toArray()[0].toString());
            buffer.close();
            reader.close();
//            System.out.println(classMap.size());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return classMap;

    }
*/

    /**
     * remove the suffix name of the file
     *
     * @param file
     * @return
     */
    public static String getFileNameNoSuffix(File file) {
//        String fileName = file.getName();
//        return fileName.substring(0, fileName.lastIndexOf("."));
        return file.getName().replaceAll("[.][^.]+$", "");
    }

/*    public static void main(String[] args) {
//        File file = new File("./src/excelsamples/16计科1.xls");
//        exportClassRollBook(new Stage(),file);

    }*/
}

