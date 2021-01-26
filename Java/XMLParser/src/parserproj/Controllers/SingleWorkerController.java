package parserproj.Controllers;

import Logic.Parser;
import Logic.SaveToFile;
import Models.Worker;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.xml.sax.SAXException;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SingleWorkerController {
    public ImageView ivImage;
    public TextField etName;
    public TextField etPassSeria;
    public TextField etPassNumber;
    public TextField etPassIssued;
    public TextField etPhone;
    public TextField etEmail;
    public TextField etSex;
    public TextField etAddress;
    public TextField etIndex;
    public TextField etRegistrationAddress;
    public TextField etINN;
    public TextField etSnils;
    public Button btnPrint;
    public Button btnloadPhoto;
    public Button btnRedact;
    public DatePicker dpDate;


    Main main;

    Worker worker;

    @FXML
    public void initialize()
    {

    }

    public void prepare()
    {
        etName.setText(worker.getFullName());
        dpDate.setValue(LocalDate.parse(worker.getDateOfBirth(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        etPassSeria.setText(worker.getPassSeria());
        etPassNumber.setText(worker.getPassNumber());
        etPassIssued.setText(worker.getPassIssued());
        etPhone.setText(worker.getPhone());
        etEmail.setText(worker.getEmail());
        etSex.setText(worker.getSex());
        etAddress.setText(worker.getAddress());
        etIndex.setText(worker.getIndex());
        etRegistrationAddress.setText(worker.getRegistrationAddress());
        etINN.setText(worker.getINN());
        etSnils.setText(worker.getSnils());

        File file = new File(worker.getPhotoPath());
        Image image = new Image(file.toURI().toString());
        ivImage.setImage(image);


    }

    public  void onBtnRedactWorker(){
        setNewWorker();
    }
    public  void onBtnAddPhoto(){
        SetNewPhoto();
    }

    public void onBtnPrintClick()
    {
        new MakeReport(worker).start();
    }

    class MakeReport extends Thread
    {
        Worker worker;

        public MakeReport(Worker worker) {
            this.worker = worker;
        }

        @Override
        public void run() {
            String path = null;
            path = SaveToFile.writeUsingIText(worker);
            if (path == null) {
                Platform.runLater(() -> main.showError("Произошла ошибка при формировании отчета"));
            } else {
                String finalPath = path;
                Platform.runLater(() ->
                        {
                            if(main.confirmationDialog("Отчет сохранен по пути: " + finalPath + " Распечатать?"))
                            {
                                try {
                                    Desktop.getDesktop().open(new File(new File(finalPath).getParent()));
                                    PDDocument document = PDDocument.load(new File(finalPath));

                                    PrintService myPrintService = findPrintService("My Windows printer Name");

                                    PrinterJob job = PrinterJob.getPrinterJob();
                                    job.setPageable(new PDFPageable(document));
                                    job.printDialog();
                                } catch (IOException e) {
                                    main.showError("Произошла ошибка при открытии.");
                                }//                                } catch (PrinterException e) {
//                                    main.showError("Произошла ошибка при печати.");
//                                }
                            }
                        }
                );
            }
        }
    }

    private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }

    public  void  SetNewPhoto(){
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(null);
        Image image = new Image(file.toURI().toString());
        ivImage.setImage(image);
        worker.setPhotoPath(file.getPath());
    }

    public void setNewWorker (){

        Parser parser = new Parser();

        worker.setFullName(etName.getText());
        LocalDate date = dpDate.getValue();
        if(date != null)
        {
            worker.setDateOfBirth(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }
        worker.setPassSeria(etPassSeria.getText());
        worker.setPassNumber(etPassNumber.getText());
        worker.setSex(etSex.getText());
        worker.setRegistrationAddress(etRegistrationAddress.getText());
        worker.setAddress(etAddress.getText());
        worker.setIndex(etIndex.getText());
        worker.setSnils(etSnils.getText());
        worker.setINN(etINN.getText());
        worker.setEmail(etEmail.getText());
        worker.setPhone(etPhone.getText());
        worker.setPassIssued(etPassIssued.getText());

        try {
            parser.redactFile(worker);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            main.showError("Ошибка при редактировании");
            return;
        }
        main.getStage(btnloadPhoto).close();
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
