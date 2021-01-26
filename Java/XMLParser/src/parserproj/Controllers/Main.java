package parserproj.Controllers;

import Models.PathCount;
import Models.Worker;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        workerScene();
    }

    public void workerScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/workers.fxml"));
        Parent root = loader.load();
        WorkersController workersController = loader.getController();
        primaryStage.setTitle("");
        primaryStage.setScene(new Scene(root));
        workersController.setMain(this);
        workersController.prepare();
        primaryStage.show();
    }

    public void singleWorkerScene(Stage o, Worker worker) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream("../fxml/single_worker.fxml"));
        SingleWorkerController controller = loader.getController();
        Stage stage = new Stage();
        stage.setTitle("");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(o);
        controller.setMain(this);
        controller.setWorker(worker);
        controller.prepare();
        stage.show();
    }

    public void createWorkerScene(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream("../fxml/create_worker.fxml"));
        CreateWorkerController controller = loader.getController();
        Stage stage = new Stage();
        stage.setTitle("");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        controller.setPath(path);
        controller.setMain(this);
        controller.prepare();
        stage.show();
    }

    public void mergeScene(Stage o, ObservableList<PathCount> p) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream("../fxml/merge.fxml"));
        MergeController controller = loader.getController();
        Stage stage = new Stage();
        stage.setTitle("");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(o);
        controller.setMain(this);
        controller.setItems(p);
        controller.prepare();
        stage.show();
    }


    public Stage getStage(Control control)
    {
        return (Stage)control.getScene().getWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public boolean confirmationDialog(String message) {
        return alert(message, "Подтвердите действие", Alert.AlertType.CONFIRMATION).getResult() == ButtonType.OK;
    }

    public void showInformation(String message) {
        alert(message, "Информация", Alert.AlertType.INFORMATION);
    }

    public void showError(String message) {
        alert(message, "Ошибка", Alert.AlertType.ERROR);
    }

    public Alert alert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setContentText(message);
        alert.showAndWait();
        return alert;
    }
}
