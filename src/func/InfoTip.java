package func;

import javafx.scene.control.Alert;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class InfoTip {
    public static void showDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    public static void showTrayNotification(String title, String message, NotificationType notificationType) {

        TrayNotification tray = new TrayNotification(title, message, notificationType);
//		tray.setTitle(title);
//		tray.setMessage(message);
//		tray.setNotificationType(NotificationType.SUCCESS);
        tray.setAnimationType(AnimationType.SLIDE);
        tray.showAndDismiss(Duration.seconds(2.5));
        tray.showAndWait();
    }
}
