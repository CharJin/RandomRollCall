package controller;

import com.jfoenix.controls.JFXButton;
import func.DataFiller;
import func.FileIO;
import func.InfoTip;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class RollCallController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Label lab_stuid;

    @FXML
    private JFXButton btn_rollCall;

    @FXML
    private JFXButton btn_settings1;

    @FXML
    private Button btn_settings;

    @FXML
    private Label lab_class;

    @FXML
    private Label lab_stuname;

    @FXML
    private StackPane stack_classdata;

    @FXML
    private BorderPane borderpane_dirs;

//    @FXML
//    private MenuItem menuitem_loadData;

    private TreeView<String> treeview_gradeAndClass;
//    @FXML
//    private TreeView<String> treeview_gradeAndClass;

    private Stage stage_settings;

    private Map<Integer, String> map = null;
    private String classDataPath = "";
    private String prefix_id;
    private int stuNum;
    private Timeline timeline = new Timeline();
    private String initialBtnStyle;
    static RollCallType rollCallType = RollCallType.AUTO;
    static StuInfoDialogIs stuInfoDialogIs = StuInfoDialogIs.SELECTED;
    static int cycleCount = 25;
    static double durationTime = 100;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/image/setting.png")));
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);
        btn_settings.setGraphic(imageView);
        this.initialBtnStyle = this.btn_rollCall.getStyle();
        treeview_gradeAndClass = new TreeView<>();
    }

    @FXML
    void loadClassDataToTreeview() {
        if (this.treeview_gradeAndClass.getRoot() != null) {
            return;
        }

//        try {

        File file_dirs = FileIO.openSystemDirectory((Stage) borderPane.getScene().getWindow());
        if (file_dirs != null) {
            TreeItem<String> root = DataFiller.initTreeViewbyDir(file_dirs);
            root.setExpanded(true);
            this.treeview_gradeAndClass.setRoot(root);
            this.treeview_gradeAndClass.refresh();
            //  if user chose a empty directory, the following program stops being executed.
            if (this.treeview_gradeAndClass.getRoot().getChildren().size() == 0) return;
            /*
             * get the last directory name of the file path.
             * */
            String[] dires = file_dirs.getAbsolutePath().split("\\\\");
            String last_dire = dires[dires.length - 1];
//            System.out.println("edsds"+last_dire+"dsds");
            this.treeview_gradeAndClass.getSelectionModel().
                    selectedItemProperty().
                    addListener((observable, oldValue, newValue) -> {
                        if (this.timeline.getStatus() == Timeline.Status.RUNNING)
                            return;
                        this.lab_stuid.setText("0");
                        this.lab_stuname.setText("Stu Name");
                        if (newValue.getChildren().isEmpty()) {
                            StringBuilder path = new StringBuilder();
                            TreeItem<String> item = newValue;
//                            System.out.println(item.getValue()+"/"+path+"/");
                            while (true) {
                                if (item.getValue().equals(last_dire)) {
//                                    if (path.length() != 0)
                                    path.deleteCharAt(path.length() - 1);
                                    break;
                                }
                                path.insert(0, item.getValue() + "/");
                                item = item.getParent();
                            }
                            this.classDataPath = file_dirs.getAbsolutePath().replaceAll("\\\\", "/") + "/" + path.toString() + ".txt";
//                            System.out.println(file_dirs.getAbsolutePath().replaceAll("\\\\", "/") + "/" + path.toString() + ".txt");
                            this.lab_class.setText(newValue.getValue());
//                            this.lab_stuid.setText("0");
                            this.btn_rollCall.setDisable(false);
                        } else {
                            this.lab_class.setText("Here's the class name.");
                            this.lab_stuname.setText("Stu Name");
                            this.btn_rollCall.setDisable(true);
                        }

                    });

//            this.menuitem_loadData.setVisible(false);
            initTreeView();
            this.stack_classdata.getChildren().add(this.treeview_gradeAndClass);
        }
//        } catch (Exception e) {
//
//        }
    }

    private void initTreeView() {
        this.treeview_gradeAndClass.setStyle("-fx-background-color:transparent;");
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("关闭此数据源");
        menuItem.setOnAction(e -> {
            stack_classdata.getChildren().remove(treeview_gradeAndClass);
            treeview_gradeAndClass = new TreeView<>();
            this.lab_class.setText("Here's the class name.");
            this.lab_stuname.setText("Stu Name");
            this.lab_stuid.setText("0");
            this.btn_rollCall.setDisable(true);

        });
        contextMenu.getItems().add(menuItem);
        this.treeview_gradeAndClass.setContextMenu(contextMenu);
    }

    @FXML
    void actionRandomRollCall() {
        if (this.rollCallType == RollCallType.AUTO) {
            this.setBtnStyleAndTimeline(ButtonStatus.WAIT);
            this.btn_settings.setDisable(true);
        } else {
            if (this.btn_rollCall.getText().equals("开始")) {
                this.setBtnStyleAndTimeline(ButtonStatus.STOP);
                cycleCount = Timeline.INDEFINITE;
                this.btn_settings.setDisable(true);
            } else {
                this.timeline.stop();
//                System.out.println(prefix_id + this.lab_stuid.getText());
                this.setBtnStyleAndTimeline(ButtonStatus.START);
                cycleCount = 25;
                this.btn_settings.setDisable(false);
                if (stuInfoDialogIs == StuInfoDialogIs.SELECTED)
                    InfoTip.showDialog("点名结果", "Congratulations!", prefix_id + this.lab_stuid.getText() + " " + map.get(this.stuNum));
                return;
            }
        }
//        System.out.println(this.classDataPath);
        try {
            map = FileIO.getStudentMap(new File(classDataPath));
//        map = FileIO.getStudentMap(new File("C:\\Users\\char_\\Desktop\\16计科1.txt"));
            prefix_id = map.get(-1).toString();

        } catch (NullPointerException e) {
            this.setBtnStyleAndTimeline(ButtonStatus.START);
            InfoTip.showDialog("错误提示", "Unfortunately!", "没有学生信息或内容格式错误.");
            return;
        }
        timeline = new Timeline(new KeyFrame(Duration.millis(durationTime), new RandomRollCallActionEvent(this.cycleCount, map.size() - 1)));
        timeline.setCycleCount(this.cycleCount);
        timeline.play();
    }

    /**
     * According to the type of rollcall to set the style of button and properties of the timeline.
     *
     * @param btn_status
     */
    private void setBtnStyleAndTimeline(ButtonStatus btn_status) {
        if (btn_status == ButtonStatus.START) {
            this.btn_rollCall.setDisable(false);
            this.btn_rollCall.setText("开始");
            this.btn_rollCall.setButtonType(JFXButton.ButtonType.FLAT);
            this.btn_rollCall.setStyle(this.initialBtnStyle);
            this.btn_rollCall.setRipplerFill(Color.valueOf("#b1b1b1"));

        } else if (btn_status == ButtonStatus.WAIT) {
            this.btn_rollCall.setDisable(true);
            this.btn_rollCall.setText("稍等..");
//            this.cycleCount = 25;
        } else {
            /*
             * btn_status == ButtonStatus.STOP
             * */
            this.btn_rollCall.setDisable(false);
            this.btn_rollCall.setText("停止");
            this.btn_rollCall.setButtonType(JFXButton.ButtonType.RAISED);
            this.btn_rollCall.setStyle(this.btn_rollCall.getStyle() + "-fx-background-color: #ea5858;");
            this.btn_rollCall.setRipplerFill(Color.valueOf("#fdd1d1"));
//            this.cycleCount = Timeline.INDEFINITE;

        }
    }

    //    #e14a4a; #ea5858
    private class RandomRollCallActionEvent implements EventHandler<ActionEvent> {
        private int i = 1;
        private int cycleCount;
        private int stuNums;


        public RandomRollCallActionEvent(int cycleCount, int stuNums) {
            this.cycleCount = cycleCount;
            this.stuNums = stuNums;
        }

        @Override
        public void handle(ActionEvent event) {
            stuNum = new Random().nextInt(stuNums) + 1;
            lab_stuid.setText(stuNum < 10 ? ("0" + stuNum) : stuNum + "");
            lab_stuname.setText(map.get(stuNum));
            if (rollCallType == RollCallType.AUTO)
                autoTypeAction();
            else
                manualTypeAction();
        }

        private void autoTypeAction() {
            if (i++ == cycleCount) {
                prefix_id += lab_stuid.getText();
                btn_rollCall.setDisable(false);
                btn_settings.setDisable(false);
                btn_rollCall.setText("开始");
                if (stuInfoDialogIs == StuInfoDialogIs.SELECTED)
                    InfoTip.showDialog("点名结果", "Congratulations!", prefix_id + " " + map.get(stuNum));
//                System.out.println(prefix_id + " " + map.get(stuNum));
            }
        }

        private void manualTypeAction() {

        }
    }

    @FXML
    private void openSettingsView() {
        if (this.stage_settings == null)
            try {
                this.stage_settings = new Stage();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/SettingsView.fxml")));
                stage_settings.setScene(new Scene(root));
//                stage_settings.setAlwaysOnTop(true);
                stage_settings.setResizable(false);
                stage_settings.setTitle(" 随机点名器信息配置");
                stage_settings.getIcons().add(new Image(getClass().getResourceAsStream("/image/config_icon.png")));
                stage_settings.show();
            } catch (IOException e) {
                e.printStackTrace();
//            System.out.println(e.getMessage());
            }
        else {
            stage_settings.show();
            stage_settings.requestFocus();
        }

    }


}
