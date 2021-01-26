package parserproj.Controllers;

import Logic.Parser;
import Models.PathCount;
import Models.Worker;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.print.PrintService;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class MergeController {

    public TableView<PathCount> taPaths;
    public TableColumn<PathCount, String> colPath;
    public TextField etPath;
    public Button btnMerge;
    Main main;

    ObservableList<PathCount> items;

    public void prepare() {
        etPath.setText(System.getProperty("user.dir")+"/output.xml");
        colPath.setCellValueFactory(
                new PropertyValueFactory<>("path")
        );
        taPaths.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        taPaths.setItems(items);
    }

    public void onBtnMergeClick() {
        String path = etPath.getText();
        File file = new File(path);
        if (file.isDirectory()) {
            return;//TODO сообщение об ошибке
        }
        String[] f = file.getName().split(".");
//        if(!Files.probeContentType(Path.of(new URI(path))).equals("xml"))//ой пошло оно нахуй корочк
//        {
//            //TODO не тот тип файла
//            return;
//        }
        ObservableList<PathCount> list = taPaths.getSelectionModel().getSelectedItems();//пиздааааааааааааааааааааааааааааааааааааааааааа
        if (list.isEmpty()) {
            return;
        }
        ArrayList<Worker> workers = new ArrayList<>();
        for (PathCount pathCount : list) {
            workers.addAll(pathCount.getWorkers());
        }
        new MergeThread(workers, path).start();
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setItems(ObservableList<PathCount> items) {
        this.items = items;
    }

    class MergeThread extends Thread {
        ArrayList<Worker> workers;
        String path;

        public MergeThread(ArrayList<Worker> workers, String path) {
            this.workers = workers;
            this.path = path;
        }

        @Override
        public void run() {
            try {
                Parser.createFile(workers, path);
            } catch (TransformerException | ParserConfigurationException e) {
                return;
            }
            Platform.runLater(() -> {
                if (main.confirmationDialog("Файл сохранен по пути: " + path + " Открыть?")) {
                    try {
                        Desktop.getDesktop().open(new File(new File(path).getParent()));
                    } catch (IOException e) {
                        main.showError("Произошла ошибка при открытии.");
                    }
                }
                main.getStage(btnMerge).close();
            });
        }
    }
}
