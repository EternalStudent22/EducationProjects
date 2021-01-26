package Models;

import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class PathCount {
    SimpleStringProperty path = new SimpleStringProperty();
    ArrayList<Worker> workers;

    public PathCount(String path, ArrayList<Worker> workers) {
        setPath(path);
        this.workers = workers;
    }

    public String getPath() {
        return path.get();
    }

    public SimpleStringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(ArrayList<Worker> workers) {
        this.workers = workers;
    }
}
