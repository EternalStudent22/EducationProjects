package Logic;


import Models.Worker;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class SaveToFile {

    public static String writeUsingIText(Worker worker) {

        String path = System.getProperty("user.dir") + "/reports/" + "Сотрудник " + worker.getFullName() + ".pdf";
        try {
            BufferedImage bimg = null;
            try {
                bimg = ImageIO.read(new File(worker.getPhotoPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            float width = bimg.getWidth();
            float height = bimg.getHeight();
            width = (width / height) * 250;
            height = 250;
            BaseFont bf = BaseFont.createFont("ARIALUNI.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, new FileOutputStream(new File(path)));
            document.open();
            Paragraph paragraph = new Paragraph("Карточка сотрудника", new Font(bf, 20, Font.BOLD));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            Image image = Image.getInstance(worker.getPhotoPath());
            image.scaleAbsolute(width, height);
            document.add(image);
            document.add(new Paragraph("Имя: " + worker.getFullName()
                    + "\nДата рождения: " + worker.getDateOfBirth()
                    + "\nПол: " + worker.getSex()
                    + "\nПочта " + worker.getEmail()
                    + "\nТелефон: " + worker.getPhone()
                    + "\nСерия паспорта: " + worker.getPassSeria()
                    + "\nНомер паспорта: " + worker.getPassNumber()
                    + "\nПаспорт выдан: " + worker.getPassIssued()
                    + "\nАдрес: " + worker.getAddress()
                    + "\nАдрес регистрации: " + worker.getRegistrationAddress()
                    + "\nПочтовый индекс: " + worker.getIndex()
                    + "\nИНН: " + worker.getINN()
                    + "\nСнилс: " + worker.getSnils(), new Font(bf, 16)));
            document.close();
        } catch (DocumentException | IOException e) {
            return null;
        }
        return path;
    }

//    public static String createWorkerReport(Worker worker)
//    {
//        Workbook workbook = new XSSFWorkbook();
//
//        Sheet sheet = workbook.createSheet("Работник");
//
//        int rowNumber = 0;
//
//        CellStyle headerStyle = getHeaderCellStyle(workbook);// сохраняем в ячейку значения воркера
//        createKeyValueRow(sheet, rowNumber, "ФИО", worker.getFullName(), headerStyle);
//        rowNumber++;
//        createKeyValueRow(sheet, rowNumber, "Дата рождения", worker.getFullName(), headerStyle);
//        rowNumber++;
//        createKeyValueRow(sheet, rowNumber, "Пол", worker.getDateOfBirth(), headerStyle);
//        rowNumber++;
//        createKeyValueRow(sheet, rowNumber, "Почта", worker.getEmail(), headerStyle);
//        rowNumber++;
//        createKeyValueRow(sheet, rowNumber, "Серия паспорта", worker.getPassSeria(), headerStyle);
//        rowNumber++;
//        createKeyValueRow(sheet, rowNumber, "Номер паспорта", worker.getPassNumber(), headerStyle);
//        rowNumber++;
//        createKeyValueRow(sheet, rowNumber, "Телефон", worker.getPhone(), headerStyle);
//        rowNumber++;
//        createKeyValueRow(sheet, rowNumber, "Кем выдан", worker.getPassIssued(), headerStyle);
//        rowNumber++;
//        createKeyValueRow(sheet, rowNumber, "Адресс", worker.getAddress(), headerStyle);
//        rowNumber++;
//        createKeyValueRow(sheet, rowNumber, "Адрес регистрации", worker.getRegistrationAddress(), headerStyle);
//        rowNumber++;
//        createKeyValueRow(sheet, rowNumber, "Почтовый индекс", worker.getIndex(), headerStyle);
//        rowNumber++;
//        createKeyValueRow(sheet, rowNumber, "ИНН", worker.getINN(), headerStyle);
//        rowNumber++;
//        createKeyValueRow(sheet, rowNumber, "СНИЛС", worker.getSnils(), headerStyle);
//        rowNumber++;
//        createKeyValuePhotoRow(workbook, sheet, rowNumber, "Фото", worker.getPhotoPath(), headerStyle);
//
//        for (int i = 0; i < 2; i++) {
//            sheet.autoSizeColumn(i);
//        }
//
//        String path = System.getProperty("user.dir") + "/reports/";
//        String fileName = "Сотрудник " + worker.getFullName() + ".xlsx";
//        try {
//            saveToFile(workbook, path, fileName);
//            return path + fileName;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public static CellStyle getHeaderCellStyle(Workbook workbook) {
//        Font font = workbook.createFont();
//        font.setBold(true);
//        font.setFontHeightInPoints((short) 14);
//        font.setColor(IndexedColors.BLACK.getIndex());
//
//        CellStyle headerCellStyle = workbook.createCellStyle();
//        headerCellStyle.setFont(font);
//
//        return headerCellStyle;
//    }

//    public static void createKeyValueRow(Sheet sheet, int rowNumber, String key, String value, CellStyle headerStyle)
//    {
//        Row row = sheet.createRow(rowNumber);
//        Cell cell = row.createCell(0);
//        cell.setCellValue(key);
//        cell.setCellStyle(headerStyle);
//        cell = row.createCell(1);
//        cell.setCellValue(value);
//
//    }
//
//    public static void createKeyValuePhotoRow(Workbook workbook, Sheet sheet, int rowNumber, String key, String path, CellStyle headerStyle)
//    {
//        Row row = sheet.createRow(rowNumber);
//        Cell cell = row.createCell(0);
//        cell.setCellValue(key);
//        cell.setCellStyle(headerStyle);
//        final FileInputStream stream;
//        try {
//            stream = new FileInputStream(path);
//        } catch (FileNotFoundException e) {
//            return;
//        }
//        byte[] bytes = new byte[0];
//        try {
//            bytes = IOUtils.toByteArray(stream);
//        } catch (IOException e) {
//            return;
//        }
//        //Adds a picture to the workbook
//        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
//        //close the input stream
//        try {
//            stream.close();
//        } catch (IOException e) {
//            return;
//        }
//        //Returns an object that handles instantiating concrete classes
//        CreationHelper helper = workbook.getCreationHelper();
//
//        //Creates the top-level drawing patriarch.
//        Drawing drawing = sheet.createDrawingPatriarch();
//
//        //Create an anchor that is attached to the worksheet
//        ClientAnchor anchor = helper.createClientAnchor();
//        //set top-left corner for the image
//        anchor.setCol1(2);
//        anchor.setRow1(13);
//        //Creates a picture
//        Picture pict = drawing.createPicture(anchor, pictureIdx);
//        //Reset the image to the original size
//        BufferedImage bimg = null;
//        try {
//            bimg = ImageIO.read(new File(path));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        double width          = bimg.getWidth();
//        double height         = bimg.getHeight();
//        width = (height/width);
//        pict.resize(width*3, 9);
//    }
//
//    public static void createHeaderRow(Sheet sheet, int rowNumber, String[] data, CellStyle style) {
//        Row row = sheet.createRow(rowNumber);
//        for (int i = 0; i < data.length; i++) {
//            Cell cell = row.createCell(i);
//            cell.setCellValue(data[i]);
//            cell.setCellStyle(style);
//        }
//    }
//
//    public static void createRow(Sheet sheet, int rowNumber, String[] data) {
//        Row row = sheet.createRow(rowNumber);
//        for (int i = 0; i < data.length; i++) {
//            Cell cell = row.createCell(i);
//            cell.setCellValue(data[i]);
//        }
//    }
//
//    public static void saveToFile(XWPFDocument document, String path, String fileName) throws Exception {
//        File myFile = new File(path);
//        myFile.mkdirs();
//        path = path + fileName;
//        myFile = new File(path);
//        myFile.delete();
//        if (!myFile.createNewFile()) {
//            throw new Exception();
//        }
//        FileOutputStream fileOut;
//        fileOut = new FileOutputStream(myFile);
//        document.write(fileOut);
//        fileOut.close();
//    }
//
//    public static void saveToFile(Workbook workbook, String path, String fileName) throws Exception {
//        File myFile = new File(path);
//        myFile.mkdirs();
//        path = path + fileName;
//        myFile = new File(path);
//        myFile.delete();
//        if (!myFile.createNewFile()) {
//            throw new Exception();
//        }
//        FileOutputStream fileOut;
//        fileOut = new FileOutputStream(myFile);
//        workbook.write(fileOut);
//        fileOut.close();
//    }

}
