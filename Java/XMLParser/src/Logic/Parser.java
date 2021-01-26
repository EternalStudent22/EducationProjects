package Logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import Models.Worker;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Parser {

    public static String getString(Element elem, String tag) {
        return elem.getElementsByTagName(tag)
                .item(0).getChildNodes().item(0).getNodeValue();
    }

    ///массив документов и нодев и чекнуть хешмапы
    public static ArrayList<Worker> getWorkers(String path) throws ParserConfigurationException,
            SAXException, IOException {


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Load the input XML document, parse it and return an instance of the
        // Document class.
        Document document = builder.parse(path);


        ArrayList<Worker> employees = new ArrayList<Worker>();
        NodeList nodeList = document.getDocumentElement().getElementsByTagName("Worked");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element elem = (Element) node;

            String fullName = getString(elem, "Name");
            String dateOfBirth = getString(elem, "Dateofbitrth");
            String sex = getString(elem, "sex");
            String email = getString(elem, "email");
            String passSeria = getString(elem, "passseria");
            String passNumber = getString(elem, "passnumber");
            String phone = getString(elem, "phone");
            String passIssued = getString(elem, "passissued");
            String address = getString(elem, "address");
            String registrationAddress = getString(elem, "registrationAddress");
            String index = getString(elem, "index");
            String INN = getString(elem, "INN");
            String snils = getString(elem, "snils");
            String photoPath = getString(elem, "photopath");


            employees.add(new Worker(fullName, dateOfBirth, sex, email, passSeria,
                    passNumber, phone, passIssued, address, registrationAddress, index,
                    INN, snils, photoPath, path, i));

//                employees.add(new Worker(ID, fullName, dateOfBirth,sex,email,passSeria,
//                        passNumber,phone));
        }
        return employees;

    }

//    public static Worker getSelectWorker(int number) throws ParserConfigurationException,
//            SAXException, IOException {
//
//        Worker worker = new Worker();
//        NodeList nodeList = document.getDocumentElement().getChildNodes();
//
//        for (int i = number - 1; i < nodeList.getLength(); i++) {
//            Node node = nodeList.item(i);
//
//            if (node.getNodeType() == Node.ELEMENT_NODE) {
//                Element elem = (Element) node;
//
//                if (Integer.parseInt(node.getAttributes().getNamedItem("ID").getNodeValue()) == number) {
//                    // Get the value of the ID attribute.
//                    worker.setID(node.getAttributes().getNamedItem("ID").getNodeValue());
//                    worker.setFullName(elem.getElementsByTagName("Name")
//                            .item(0).getChildNodes().item(0).getNodeValue());
//                    worker.setDateOfBirth(elem.getElementsByTagName("Dateofbitrth").item(0)
//                            .getChildNodes().item(0).getNodeValue());
//                    worker.setSex(elem.getElementsByTagName("sex")
//                            .item(0).getChildNodes().item(0).getNodeValue());
//                    worker.setEmail(elem.getElementsByTagName("email")
//                            .item(0).getChildNodes().item(0).getNodeValue());
//                    worker.setPassSeria(elem.getElementsByTagName("passseria")
//                            .item(0).getChildNodes().item(0).getNodeValue());
//                    worker.setPassNumber(elem.getElementsByTagName("passnumber")
//                            .item(0).getChildNodes().item(0).getNodeValue());
//                    worker.setPhone(elem.getElementsByTagName("phone")
//                            .item(0).getChildNodes().item(0).getNodeValue());
//                    worker.setPassIssued(elem.getElementsByTagName("passissued")
//                            .item(0).getChildNodes().item(0).getNodeValue());
//                    worker.setAddress(elem.getElementsByTagName("address")
//                            .item(0).getChildNodes().item(0).getNodeValue());
//                    worker.setRegistrationAddress(elem.getElementsByTagName("registrationAddress")
//                            .item(0).getChildNodes().item(0).getNodeValue());
//                    worker.setIndex(elem.getElementsByTagName("index")
//                            .item(0).getChildNodes().item(0).getNodeValue());
//                    worker.setINN(elem.getElementsByTagName("INN")
//                            .item(0).getChildNodes().item(0).getNodeValue());
//                    worker.setSnils(elem.getElementsByTagName("snils")
//                            .item(0).getChildNodes().item(0).getNodeValue());
//                    worker.setPhotoPath(elem.getElementsByTagName("photopath")
//                            .item(0).getChildNodes().item(0).getNodeValue());
//                    return worker;
//                }
//            }
//        }
//        return null;
//    }


    public static void deleteWorker(Worker worker) throws ParserConfigurationException,
            SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(worker.getFilePath());
        NodeList nodeList = document.getElementsByTagName("Worked");
        Element el = (Element) nodeList.item(worker.getIdx());
        el.getParentNode().removeChild(el);
        document.getDocumentElement().normalize();
        saveChanges(worker, document);
    }

    public static void redactFile(Worker worker) throws ParserConfigurationException,
            SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(worker.getFilePath());
        Node node = document.getDocumentElement().getElementsByTagName("Worked").item(worker.getIdx());
        NodeList childNodeList = node.getChildNodes();

        for (int j = 0; j < childNodeList.getLength(); j++) {
            Node childNode = childNodeList.item(j);

            if ("Name".equals(childNode.getNodeName())) {
                childNode.setTextContent(worker.getFullName());
            }

            if ("Dateofbitrth".equals(childNode.getNodeName())) {
                childNode.setTextContent(worker.getDateOfBirth());
            }

            if ("sex".equals(childNode.getNodeName())) {
                childNode.setTextContent(worker.getSex());
            }
            if ("email".equals(childNode.getNodeName())) {
                childNode.setTextContent(worker.getEmail());
            }
            if ("passseria".equals(childNode.getNodeName())) {
                childNode.setTextContent(worker.getPassSeria());
            }
            if ("passnumber".equals(childNode.getNodeName())) {
                childNode.setTextContent(worker.getPassNumber());
            }
            if ("phone".equals(childNode.getNodeName())) {
                childNode.setTextContent(worker.getPhone());
            }
            if ("passissued".equals(childNode.getNodeName())) {
                childNode.setTextContent(worker.getPassIssued());
            }
            if ("address".equals(childNode.getNodeName())) {
                childNode.setTextContent(worker.getAddress());
            }
            if ("registrationAddress".equals(childNode.getNodeName())) {
                childNode.setTextContent(worker.getRegistrationAddress());
            }
            if ("index".equals(childNode.getNodeName())) {
                childNode.setTextContent(worker.getIndex());
            }
            if ("INN".equals(childNode.getNodeName())) {
                childNode.setTextContent(worker.getINN());
            }
            if ("snils".equals(childNode.getNodeName())) {
                childNode.setTextContent(worker.getSnils());
            }
            if ("photopath".equals(childNode.getNodeName())) {
                childNode.setTextContent(worker.getPhotoPath());
            }
        }

        saveChanges(worker, document);
    }

    private static void saveChanges(Worker worker, Document document) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(worker.getFilePath()));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    public static void addToFile(Worker worker) throws ParserConfigurationException,
            SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(worker.getFilePath());
        Element root = document.getDocumentElement();

        Element newWorker = createWorkerElement(worker, document);

        root.appendChild(newWorker);
        saveChanges(worker, document);
    }

    private static Element createWorkerElement(Worker worker, Document document) {

//        String[] arFamilia = {"Filatov","Raskolnikov","Ivanov","Ibragimov","Nabokov","Petrov","Grishin","Novikov","Roizman","Putin","Mixeev","Fursov","Shulmin",
//        "Kirkorov","Latypov","Borisov","Shobolov","Antonov","Stepanov","Izotov"};
//        String[] arName={"Anna","Ruslan","Aleksandr","Sveta","Dima","Vladimir","Igor","Kostya","Aleksey","Aduard","Mixail","Sofiya","Kirill","Yakov","Boris","Petr",
//        "Matvey","Elena","Amir","Jamal","Pavel"};
//        String[] arOtchestvo = {"Aleksee","Pavlo","Petro","Dmitrie","Rostislvo","Kirilo","Antono","Konstantino","Egoro","Vladimiro",
//        "Stepano","Romano","Andree","Mirono","Artemo"};
//        String sex="";
//        String FIO;
//
//        Integer day = 1+ (int)(Math.random()*31);
//        Integer mouth = 1 +(int)(Math.random()*12);
//        Integer year= 1960 + (int)(Math.random()*50);
//
//        Integer emailPref=1960+(int)(Math.random()*50);
//
//        Integer pasSeria = 1000+(int) (Math.random()*9999);
//
//        Integer passNumber=111111+(int)(Math.random()*999999);
//
//        String[] nameCity={"Moscow","Odintsovo","Novgorod","Kerch"};
//
//        String[] nameStreat ={"Arbar","Marshala Jukova","Lunacharskogo","Selki"};
//
//        Integer numberHome= 1+ (int)(Math.random()*150);
//
//        Integer numKvart= 1 +(int)(Math.random()*200);
//
//        Integer index = 111111 +(int)(Math.random()*999999);
//
//        Integer INN= 111111111 + (int)(Math.random()*999999999);
//
//        Integer snils= 111111111+ (int)(Math.random()*999999999);
//
//        Integer phone = 905000000 + (int)(Math.random()*999999999);
//
//        String name = arName[0+(int)(Math.random()*19)];
//
//        String otchestvo = arOtchestvo[0+(int)(Math.random()*14)];
//
//        if (name.equals("Anna")||name.equals("Sveta")||name.equals("Sofiya")||name.equals("Elena")){
//            sex = "female";
//            otchestvo = otchestvo + "vna";
//        }
//        else{
//            sex="male";
//            otchestvo = otchestvo + "vich";
//        }
//
//
//
//        if(sex.equals("female")){
//            FIO=arFamilia[0+(int)(Math.random()*19)]+"a" +" "+ name +" "+ otchestvo;
//        }else {
//            FIO=arFamilia[0+(int)(Math.random()*19)] +" "+ name +" "+ otchestvo;
//        }
//
//        String dataOfBirt=(day.toString().length()==1?"0"+day.toString():day.toString())+"."+(mouth.toString().length()==1?"0"+mouth.toString():mouth.toString())+"."+year.toString();
//        String email = name+emailPref.toString()+"@yandex.ru";
//        String passSeria = pasSeria.toString();
//        String pasNumber = passNumber.toString();
//        String Phone = "8"+phone.toString();
//        String Sity= nameCity[0+(int)(Math.random()*3)];
//        String PassIssued= "TP MVD to city " + Sity;
//        String Adress = Sity + " yl."+ nameStreat[0+(int)(Math.random()*3)]+" d."+ numberHome.toString()+" kv."+numKvart;
//        String AdressReg=Sity+" yl."+ nameStreat[0+(int)(Math.random()*3)]+" d."+ numberHome.toString()+" kv."+numKvart;
//        String Index= index.toString();
//        String INNN=INN.toString();
//        String Snils=snils.toString();
//        String path="img.jpg";
//
//
//        Element newWorker = document.createElement("Worked");
//        appendWorkerElement("Name", FIO, document, newWorker);//TODO
//        appendWorkerElement("Dateofbitrth",dataOfBirt,document,newWorker);
//        appendWorkerElement("sex",sex,document,newWorker);
//        appendWorkerElement("email",email,document,newWorker);
//        appendWorkerElement("passseria",passSeria,document,newWorker);
//        appendWorkerElement("passnumber",pasNumber,document,newWorker);
//        appendWorkerElement("phone",Phone,document,newWorker);
//        appendWorkerElement("passissued",PassIssued,document,newWorker);
//        appendWorkerElement("address",Adress,document,newWorker);
//        appendWorkerElement("registrationAddress",AdressReg,document,newWorker);
//        appendWorkerElement("index",Index,document,newWorker);
//        appendWorkerElement("INN",INNN,document,newWorker);
//        appendWorkerElement("snils",Snils,document,newWorker);
//        appendWorkerElement("photopath",path,document,newWorker);



        Element newWorker = document.createElement("Worked");
        appendWorkerElement("Name", worker.getFullName(), document, newWorker);//TODO
        appendWorkerElement("Dateofbitrth",worker.getDateOfBirth(),document,newWorker);
        appendWorkerElement("sex",worker.getSex(),document,newWorker);
        appendWorkerElement("email",worker.getEmail(),document,newWorker);
        appendWorkerElement("passseria",worker.getPassSeria(),document,newWorker);
        appendWorkerElement("passnumber",worker.getPassNumber(),document,newWorker);
        appendWorkerElement("phone",worker.getPhone(),document,newWorker);
        appendWorkerElement("passissued",worker.getPassIssued(),document,newWorker);
        appendWorkerElement("address",worker.getAddress(),document,newWorker);
        appendWorkerElement("registrationAddress",worker.getRegistrationAddress(),document,newWorker);
        appendWorkerElement("index",worker.getIndex(),document,newWorker);
        appendWorkerElement("INN",worker.getINN(),document,newWorker);
        appendWorkerElement("snils",worker.getSnils(),document,newWorker);
        appendWorkerElement("photopath",worker.getPhotoPath(),document,newWorker);
        return newWorker;
    }

    private static void appendWorkerElement(String tag, String value, Document document, Element newWorker) {
        Element name = document.createElement(tag);
        name.appendChild(document.createTextNode(value));
        newWorker.appendChild(name);
    }

    public static void createFile(ArrayList<Worker> workers, String path) throws TransformerException, ParserConfigurationException {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("Workers");
            document.appendChild(root);

//            for(int i=0;i<5000;i++){
//                Element newWorker = createWorkerElement(null, document);
//                root.appendChild(newWorker);
//            }

            for (Worker worker : workers) {
                Element newWorker = createWorkerElement(worker, document);
                root.appendChild(newWorker);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(path));

            transformer.transform(domSource, streamResult);
    }
}