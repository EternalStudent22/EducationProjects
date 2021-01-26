package Models;

import javafx.beans.property.SimpleStringProperty;

public class Worker {

    SimpleStringProperty fullName = new SimpleStringProperty();
    SimpleStringProperty dateOfBirth  = new SimpleStringProperty();
    SimpleStringProperty sex  = new SimpleStringProperty();
    SimpleStringProperty email = new SimpleStringProperty();
    SimpleStringProperty passSeria= new SimpleStringProperty();
    SimpleStringProperty passNumber = new SimpleStringProperty();
    SimpleStringProperty phone = new SimpleStringProperty();
    String passIssued;
    String address;
    String registrationAddress;
    String index;
    String INN;
    String snils;
    String photoPath;

    String filePath;

    int idx;


    public Worker(String fullName, String dateOfBirth, String sex, String emaile, String passSeria,
                    String passNumber, String phone,String passIssued, String address, String registrationAddress, String index,
                    String INN, String snils, String photoPath, String filePath, int idx) {
        setFullName(fullName);
        setDateOfBirth(dateOfBirth);
        setSex(sex);
        setEmail(emaile);
        setPassSeria(passSeria);
        setPassNumber(passNumber);
        setPhone(phone);
        this.passIssued = passIssued;
        this.address = address;
        this.registrationAddress = registrationAddress;
        this.index=index;
        this.INN=INN;
        this.snils=snils;
        this.photoPath=photoPath;
        this.filePath = filePath;
        this.idx = idx;
    }

    public Worker() {

    }


    //    public Worker( String fullName, String dateOfBirth, String sex, String emaile, String passSeria,
//                  String passNumber) {
//        setID(ID);
//        setFullName(fullName);
//        setDateOfBirth(dateOfBirth);
//        setSex(sex);
//        setEmail(emaile);
//        setPassSeria(passSeria);
//        setPassNumber(passNumber);
//        setPhone(phone);
//    }


    public String getFullName() {
        return fullName.get();
    }

    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public SimpleStringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public String getSex() {
        return sex.get();
    }

    public SimpleStringProperty sexProperty() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex.set(sex);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPassSeria() {
        return passSeria.get();
    }

    public SimpleStringProperty passSeriaProperty() {
        return passSeria;
    }

    public void setPassSeria(String passSeria) {
        this.passSeria.set(passSeria);
    }

    public String getPassNumber() {
        return passNumber.get();
    }

    public SimpleStringProperty passNumberProperty() {
        return passNumber;
    }

    public void setPassNumber(String passNumber) {
        this.passNumber.set(passNumber);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getPassIssued() {
        return passIssued;
    }

    public void setPassIssued(String passIssued) {
        this.passIssued = passIssued;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegistrationAddress() {
        return registrationAddress;
    }

    public void setRegistrationAddress(String registrationAddress) {
        this.registrationAddress = registrationAddress;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getINN() {
        return INN;
    }

    public void setINN(String INN) {
        this.INN = INN;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
}