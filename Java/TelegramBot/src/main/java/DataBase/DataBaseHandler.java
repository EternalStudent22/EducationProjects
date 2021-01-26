package DataBase;

import Interface.Product;
import Interface.Test;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.checkerframework.checker.units.qual.A;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataBaseHandler {


    final String tableQuest = "quest";
    final String cellNameQuest = "questions";
    final String cellPar = "parameter";
    final String cellNum = "order_num";
    final String tableAnswer = "answers";
    final String cellNameAnsw = "answer";
    final String cellFlag = "flag_quest";
    final String tableRul = "questresult";
    final String cellRulPar = "if_Par";
    final String cellRulVal = "if_Value";
    final String cellRulNextQuest = "nextQuest";
    final String cellRulOrder = "num_quest";
    final String cellRulValPar = "value";
    final String cellNoAsk = "noAsk";
    final String tableSaveAns = "answ";
    final String cellSaveAns = "answer";
    final String cellSaveQuest = "question";
    final String cellSaveOrder = "order_quest";
    final String cellSaveUser = "chat_id";
    final String tableProducts = "products";
    final String tableUsers = "users";
    final String cellChatId = "chat_id";


    Connection dbConnection;
    private String dbHost = "localhost";
    private String dbPort = "3306";
    private String dbLogin = "root";
    private String dbPassword = "root";
    private String dbName = "es1";

    public DataBaseHandler() {
    }

    public Connection getDbConnection() {
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            String connectionString = "jdbc:mysql://" + this.dbHost + ":" + this.dbPort + "/" + this.dbName + "?useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false";
            Class.forName("com.mysql.jdbc.Driver");
            this.dbConnection = DriverManager.getConnection(connectionString, this.dbLogin, this.dbPassword);
        } catch (SQLException var3) {
            var3.printStackTrace();
        } catch (ClassNotFoundException var4) {
            var4.printStackTrace();
        }

        return this.dbConnection;
    }

    public ArrayList<String> getListQuest() {

        //SELECT `questions` FROM `quest` ORDER BY `order_num` ASC

        ArrayList<String> quest = new ArrayList<String>();
        String insert = "SELECT " + cellNameQuest + " FROM " + tableQuest + " ORDER BY " + cellNum + " ASC";
        try {
            PreparedStatement command = getDbConnection().prepareStatement(insert);
            ResultSet result = command.executeQuery();
            while (result.next()) {
                quest.add(result.getString(1));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return quest;
    }

    public ArrayList<String> getListAnswer(String quest) {
        ArrayList<String> answers = new ArrayList<String>();
        String insert = "SELECT " + cellNameAnsw + " FROM " + tableAnswer + " WHERE " + cellFlag + " =?";

        try {
            PreparedStatement command = getDbConnection().prepareStatement(insert);
            command.setString(1, quest);
            ResultSet result = command.executeQuery();
            while (result.next()) {
                answers.add(result.getString(1));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return answers;
    }

    public int checkOrderNext(int anchor, String answer) {
        //SELECT `nextQuest` FROM `questresult` WHERE `nextQuest`> 7 AND `if_Value`='нет' LIMIT 1
        int orderNext = 0;
        String insert = "SELECT " + cellRulNextQuest + " FROM " + tableRul + " WHERE " + cellRulNextQuest + " >? AND " +
                cellRulVal + "=? LIMIT 1";
        try {
            PreparedStatement command = getDbConnection().prepareStatement(insert);
            command.setInt(1, anchor);
            command.setString(2, answer);
            ResultSet result = command.executeQuery();
            while (result.next()) {
                orderNext = result.getInt(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return orderNext;
    }

    public void setAnswers(String answer, String quest, int order, String chatId) {

        String checkAnsw = "SELECT COUNT(*) " + cellSaveQuest + " FROM " + tableSaveAns + " WHERE " + cellSaveOrder + "=?" +
                " AND " + cellSaveQuest + "= ?" + " AND " + cellSaveUser + "= ?";
        int count = 0;
        String insertUpdate = "UPDATE " + tableSaveAns + " SET " + cellSaveAns + "= ?" + " WHERE " + cellSaveOrder + "= ?" +
                " AND " + cellSaveUser + "= ?";
        String insert = "INSERT " + tableSaveAns + "(" + cellSaveAns + "," + cellSaveQuest + "," + cellSaveOrder
                + "," + cellSaveUser + ") VALUES (?,?,?,?)";
        try {
            PreparedStatement commandCheck = getDbConnection().prepareStatement(checkAnsw);
            commandCheck.setInt(1, order);
            commandCheck.setString(2, quest);
            commandCheck.setString(3, chatId);
            ResultSet resultSet = commandCheck.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            if (count > 0) {
                PreparedStatement commandUpdate = getDbConnection().prepareStatement(insertUpdate);
                commandUpdate.setString(1, answer);
                commandUpdate.setInt(2, order);
                commandUpdate.setString(3, chatId);
                commandUpdate.executeUpdate();
            } else {
                PreparedStatement command = getDbConnection().prepareStatement(insert);
                command.setString(1, answer);
                command.setString(2, quest);
                command.setInt(3, order);
                command.setString(4, chatId);
                command.executeUpdate();
            }

        } catch (SQLException throwables) {

            throwables.printStackTrace();
        }

    }

    public void setUser(String chatId) {
        String insert = "INSERT " + tableUsers + "(" + cellChatId + ") VALUES (?)";

        try {

            PreparedStatement command = getDbConnection().prepareStatement(insert);
            command.setString(1, chatId);
            command.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean checkChat(String chatId) {

        boolean checker = false;
        String insert = "SELECT " + cellChatId + " FROM " + tableUsers + " WHERE " + cellChatId + "=?";

        try {
            PreparedStatement command = getDbConnection().prepareStatement(insert);
            command.setString(1, chatId);
            ResultSet resultSet = command.executeQuery();
            checker = resultSet.next();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return checker;
    }

    public void getParam(ArrayList<String> quests, ArrayList<String> answers,
                         ArrayList<Integer> orders, String chat_id) {

        String insert = "SELECT " + cellSaveQuest + "," + cellSaveAns + "," + cellSaveOrder + " FROM " + tableSaveAns +
                " WHERE " + cellSaveUser + "=?";

        try {
            PreparedStatement command = getDbConnection().prepareStatement(insert);
            command.setString(1, chat_id);
            ResultSet resultSet = command.executeQuery();
            while (resultSet.next()) {
                quests.add(resultSet.getString(1));
                answers.add(resultSet.getString(2));
                orders.add(resultSet.getInt(3));
            }


        }catch (SQLException eror){
            eror.printStackTrace();
        }
    }

    public void deleteSaveAnsw(String chat_id){

        String insert="DELETE FROM "+tableSaveAns+" WHERE "+tableSaveAns+"."+cellSaveUser+"=?";

        try{
            PreparedStatement command=getDbConnection().prepareStatement(insert);
            command.setString(1,chat_id);
            command.executeUpdate();
        }catch (SQLException error){
            error.printStackTrace();
        }
    }

    public String getValueParam(int order,String answer){

        String valueParam="";
        String insert = "SELECT "+cellRulValPar+" FROM "+ tableRul+" WHERE "+ cellRulOrder+"=?"+
                " AND "+cellRulVal+"=?";

        try {
            PreparedStatement command = getDbConnection().prepareStatement(insert);
            command.setInt(1, order);
            command.setString(2,answer);
            ResultSet resultSet = command.executeQuery();
            while (resultSet.next()) {
                valueParam=resultSet.getString(1);

            }


        }catch (SQLException eror){
            eror.printStackTrace();
        }

        return  valueParam;
    }

    public  ArrayList<Product> getSelectProduscts(boolean vit,boolean test,String rec,String contr,int level,int age,int timeUse){

        ArrayList<Product> productsList = new ArrayList<Product>();

        if(contr.equals("")){
            contr=" ";
        }

        String insert = "SELECT name,price,manufacturer,size,normal_dosage,priority FROM" +
                " (SELECT * FROM (SELECT * FROM products WHERE test=? OR test=0) m WHERE vitamin=? OR vitamin=0) s " +
                "WHERE recomendation LIKE '%"+rec+"%' AND contraindications NOT LIKE '%"+contr+"%' AND levelskills <=? " +
                "AND minage_of_use<=? AND maxage_of_use>? AND rec_time_of_use<=? ORDER BY priority ASC";

        try {
            PreparedStatement command = getDbConnection().prepareStatement(insert);
            command.setBoolean(1, vit);
            command.setBoolean(2,test);
//            command.setString(3,rec);
//            command.setString(4,contr);
            command.setInt(3,level);
            command.setInt(4,age);
            command.setInt(5,age);
            command.setInt(6,timeUse);

            ResultSet resultSet = command.executeQuery();
            while (resultSet.next()) {
                Product product=new Product();
                product.name=resultSet.getString(1);
                product.price=resultSet.getInt(2);
                product.manufacturer=resultSet.getString(3);
                product.size=resultSet.getInt(4);
                product.normDos=resultSet.getInt(5);
                product.priority=resultSet.getInt(6);

                productsList.add(product);

            }


        }catch (SQLException eror){
            eror.printStackTrace();
        }

        return productsList;
    }

}
