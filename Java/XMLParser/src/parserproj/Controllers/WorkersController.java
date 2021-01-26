package parserproj.Controllers;

//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.input.MouseButton;
//import javafx.scene.input.MouseEvent;
//import org.xml.sax.SAXException;
//
//import javax.xml.Parsers.ParserConfigurationException;
//import java.io.IOException;
//import java.util.ArrayList;

import Logic.Parser;
import Models.PathCount;
import Models.Worker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;


public class WorkersController {

    public ListView<PathCount> lvOpenedFiles;
    public Button btnClose;
    public TableView<Worker> taWorkers;
    public TableColumn<Worker, String> colFullName;
    public TableColumn<Worker, String> colDateOfBirth;
    public TableColumn<Worker, String> colSex;
    public TableColumn<Worker, String> colEmail;
    public TableColumn<Worker, String> colPassSeria;
    public TableColumn<Worker, String> colPassNumber;
    public TableColumn<Worker, String> colPhone;
    public Button btnImport;
    public Button btnGotoPage;
    public TextField tfGotoPage;
    public Button btnPageBack;
    public Button btnPageForward;
    public Label lblPage;
    public Button btnFilter;
    public Button btnDeleteFilter;
    public TextField tfFilterCostMax;
    public Button btnExport;

    ObservableList<PathCount> paths = FXCollections.observableArrayList();

    ArrayList<Worker> w = new ArrayList<>();

    ObservableList<Worker> items;

    int count = 50;

    int page = 1;

    int pageCount = 1;

    Main main;

    @FXML
    private void initialize() {
        lvOpenedFiles.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lvOpenedFiles.setCellFactory(new Callback<ListView<PathCount>, ListCell<PathCount>>() {
            @Override
            public ListCell<PathCount> call(ListView<PathCount> pathCountListView) {
                ListCell<PathCount> cell = new ListCell<PathCount>() {
                    @Override
                    protected void updateItem(PathCount pathCount, boolean b) {
                        super.updateItem(pathCount, b);
                        if(pathCount != null)
                        {
                            setText(pathCount.getPath());
                        }
                        else
                        {
                            setText("");
                        }
                    }
                };
                return cell;
            }
        });
        lvOpenedFiles.setItems(paths);
    }

    public void onBtnMergeClick()
    {
        try {
            main.mergeScene(main.getStage(btnClose), paths);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void prepareTable()//лучше переименуй метод
    {

        colFullName.setCellValueFactory(
                new PropertyValueFactory<>("fullName")
        );

        colDateOfBirth.setCellValueFactory(
                new PropertyValueFactory<>("dateOfBirth")
        );
        colSex.setCellValueFactory(
                new PropertyValueFactory<>("sex")
        );
        colEmail.setCellValueFactory(
                new PropertyValueFactory<>("email")
        );
        colPassSeria.setCellValueFactory(
                new PropertyValueFactory<>("passSeria")
        );
        colPassNumber.setCellValueFactory(
                new PropertyValueFactory<>("passNumber")
        );
        colPhone.setCellValueFactory(
                new PropertyValueFactory<>("phone")
        );


        ContextMenu cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Удалить");
        mi1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Worker worker = taWorkers.getSelectionModel().getSelectedItem();
                if (worker == null) {
                    return;
                }
                if (main.confirmationDialog("Вы уверены?")) {//ошибка почему то
                    try {
                        Parser.deleteWorker(worker);
                        PathCount pathCount = findPathCount(worker.getFilePath());
                        ArrayList<Worker> workers = pathCount.getWorkers();
                        workers.remove(worker);
                        if(workers.size() == 0)
                        {
                            paths.remove(pathCount);
                        }
                        else
                        {
                            reopen(pathCount);
                        }
                        mergePathCount();
                        updateTable();
                    } catch (ParserConfigurationException | SAXException | IOException e) {
                        return;
                    }

                }
            }
        });
        MenuItem mi2 = new MenuItem("Просмотр/Редактировать");
        mi2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Worker worker = taWorkers.getSelectionModel().getSelectedItem();
                if (worker == null) {
                    return;
                }
                try {
                    main.singleWorkerScene(main.getStage(btnClose),worker);
                    switchPage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        cm.getItems().add(mi1);
        cm.getItems().add(mi2);
        taWorkers.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.SECONDARY) {
                    cm.show(taWorkers, t.getScreenX(), t.getScreenY());
                }
            }
        });
    }

    public PathCount findPathCount(String path)
    {
        for(int i = 0; i < paths.size(); i++)
        {
            if(paths.get(i).getPath().equals(path))
            {
                return paths.get(i);
            }
        }
        return null;
    }

    public void prepare() {
        //initData();
        prepareTable();
    }

    public ArrayList<Worker> getPage(int start, int count)
    {
        ArrayList<Worker> result = new ArrayList<>();
        for(int i =0; i < count && start+i < w.size(); i++)
        {
            result.add(w.get(start+i));
        }
        return result;
    }

    public ArrayList<Worker> getWorkers(String path)
    {
        try {
            return Parser.getWorkers(path);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return null;
        }
    }

    private void initData(String path) {
        ArrayList<Worker> workers = getWorkers(path);
        if(workers == null)
        {
            return;
        }
        PathCount pathCount = new PathCount(path, workers);
        paths.add(pathCount);
        w.addAll(workers);
        updateTable();
    }

    public void updateTable()
    {
        page = 1;
        pageCount = (int) Math.ceil((double)w.size()/count);
        switchPage();
    }

    public ArrayList<Worker> workersImport() {
        return null;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void switchPage()
    {
        items = FXCollections.observableArrayList(getPage(page*count-count, count));
        taWorkers.setItems(items);
        lblPage.setText(page + "/" + pageCount);
    }

    public void nextPage()
    {
        if(page<pageCount)
        {
            page++;
            switchPage();
        }
    }

    public void previousPage()
    {
        if(page>0)
        {
            page--;
            switchPage();
        }
    }

    public void mergePathCount()
    {
        w.clear();
        for(PathCount p: paths)
        {
            w.addAll(p.getWorkers());
        }
    }

    public void onBtnCloseClick()
    {
        PathCount pathCount = lvOpenedFiles.getSelectionModel().getSelectedItem();
        close(pathCount);
    }

    private void close(PathCount pathCount) {
        paths.remove(pathCount);
        mergePathCount();
        updateTable();
    }

    public void reopen(PathCount p)
    {
        ArrayList<Worker> workers = getWorkers(p.getPath());
        if(workers == null)
        {
            close(p);
            return;
        }
        p.setWorkers(workers);
        mergePathCount();
        switchPage();
    }

    public void onBtnAddOrderClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        String title = "Выберите файл";
        fileChooser.setTitle(title);
        FileChooser.ExtensionFilter zip = new FileChooser.ExtensionFilter("XML", "*.xml");//фильтр для файлов
        fileChooser.getExtensionFilters().add(zip);
        File file = fileChooser.showOpenDialog(main.primaryStage);
        String path = file.getPath();
        for(int i = 0 ; i < paths.size(); i++)
        {
            if(paths.get(i).getPath().equals(path))
            {
                return;
            }
        }
        initData(path);
    }

    public void onBtnGotoPageClick(ActionEvent actionEvent) {
        int p;
        try
        {
            p = Integer.parseInt(tfGotoPage.getText());
        }
        catch (Exception e)
        {
            return;
        }
        if(p != page && p <= pageCount && p > 0)
        {
            page = p;
            switchPage();
        }
    }

    public void onBtnCreateNewWorker(){
        Worker worker= new Worker();

        PathCount pathCount = lvOpenedFiles.getSelectionModel().getSelectedItem();
        if(pathCount == null)
        {
            return;
        }
        try {
            main.createWorkerScene(pathCount.getPath());
            reopen(pathCount);
            mergePathCount();
            updateTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onBtnPageBackClick(ActionEvent actionEvent) {
        previousPage();
    }

    public void onBtnPageForwardClick(ActionEvent actionEvent) {
        nextPage();
    }

    public void onBtnFilterClick(ActionEvent actionEvent) {
    }

    public void onBtnDeleteFilterClick(ActionEvent actionEvent) {
    }
}
