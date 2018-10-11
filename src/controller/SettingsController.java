package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import func.FileIO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXButton btn_save;

    @FXML
    private JFXToggleButton toggle_rollcallType;

    @FXML
    private JFXComboBox<String> combobox_cycleCount;

    @FXML
    private JFXComboBox<String> combobox_durationTime;

    @FXML
    private JFXCheckBox checkbox_isShowDialog;

    private static boolean isInitial = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] cycleCountValues = new String[]{"10","15", "20", "25", "30", "35", "40", "45", "50"};
        String[] durationTimeValues = new String[]{"70","80", "90", "100", "110", "120", "130", "140", "150"};
        for (int i = 0; i < cycleCountValues.length; i++) {
            this.combobox_cycleCount.getItems().add(cycleCountValues[i]);
            this.combobox_durationTime.getItems().add(durationTimeValues[i]);
        }
        setComboboxCycleCountStatus();
        if (isInitial) {
            // after I click "Save" button, the initial data is from RollCallController.
            if (RollCallController.rollCallType == RollCallType.AUTO) this.toggle_rollcallType.setSelected(true);
            else this.toggle_rollcallType.setSelected(false);
            this.combobox_cycleCount.setValue(RollCallController.cycleCount + "");
            this.combobox_durationTime.setValue((RollCallController.durationTime + "").split("\\.")[0]);
        } else {
            // Opening a form for the first time
            this.combobox_cycleCount.setValue("25");
            this.combobox_durationTime.setValue("100");
        }
    }


    @FXML
    void saveRollCallConfig() {
        RollCallType rollCallType;
        StuInfoDialogIs stuInfoDialogIs;
        if (toggle_rollcallType.isSelected()) rollCallType = RollCallType.AUTO;
        else rollCallType = RollCallType.MANUAL;
        if (checkbox_isShowDialog.isSelected()) stuInfoDialogIs = StuInfoDialogIs.SELECTED;
        else stuInfoDialogIs = StuInfoDialogIs.UNSELECTED;
        int cycleCount = Integer.parseInt(this.combobox_cycleCount.getValue());
        double durationTime = Double.parseDouble(this.combobox_durationTime.getValue());
        assignRCValues(rollCallType, stuInfoDialogIs, cycleCount, durationTime);
        isInitial = true;
        Stage stage = (Stage) this.anchorPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void exportExcelData(ActionEvent event) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        List<File> excelFiles = FileIO.openMultiSystemFile(stage);
        FileIO.exportClassRollBook(stage, excelFiles);
    }

    @FXML
    void toggleStatusChange(ActionEvent event) {
        setComboboxCycleCountStatus();
    }

    private void setComboboxCycleCountStatus() {
        if (this.toggle_rollcallType.isSelected())
            this.combobox_cycleCount.setDisable(false);
        else
            this.combobox_cycleCount.setDisable(true);
    }

    private void assignRCValues(RollCallType rollCallType, StuInfoDialogIs stuInfoDialogIs, int cycleCount, double durationTime) {
        RollCallController.rollCallType = rollCallType;
        RollCallController.stuInfoDialogIs = stuInfoDialogIs;
        RollCallController.cycleCount = cycleCount;
        RollCallController.durationTime = durationTime;
    }
}

