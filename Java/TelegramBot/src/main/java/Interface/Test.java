package Interface;

import DataBase.DataBaseHandler;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class Test {
    public ArrayList<String> questions = new ArrayList<String>();
    public ArrayList<String> answers = new ArrayList<String>();

    public ArrayList<String> savedQuest = new ArrayList<String>();
    public ArrayList<String> savedAnswers = new ArrayList<String>();
    public ArrayList<Integer> savedOrders = new ArrayList<Integer>();
    //public ArrayList<ArrayList>

    public Test() {

    }


    public String colculationTest() {
        DataBaseHandler db = new DataBaseHandler();

        String message = "";
        //хранит наименование параметров
        int age = 0;
        int level = 0;
        int timeUse = 0;
        String recomendation = "";
        boolean vitamin = false;
        boolean test = false;
        String contraidication = "";
        int price;

        for (int i = 0; i < savedAnswers.size(); i++) {
            String valueParam = db.getValueParam(savedOrders.get(i), savedAnswers.get(i));

            if (savedQuest.get(i).equals("Ваш возраст ?")) {
                age = Integer.parseInt(savedAnswers.get(i));
            } else if (savedQuest.get(i).equals("Ваш индекс массы ?")) {
                if (Integer.parseInt(savedAnswers.get(i)) < 18) {
                    recomendation = recomendation + "М";
                } else if (Integer.parseInt(savedAnswers.get(i)) > 18 && Integer.parseInt(savedAnswers.get(i)) < 30) {
                    recomendation = recomendation + "C";
                } else {
                    recomendation = recomendation + "П";
                }

            }

            if (savedOrders.get(i) == 7) {
                level = Integer.parseInt(valueParam);
            } else if (savedOrders.get(i) == 10) {
                timeUse = Integer.parseInt(valueParam);
            }
            if (savedOrders.get(i) == 11) {
                if (valueParam.equals("false")) {
                    vitamin = false;
                    test = false;
                } else {
                    vitamin = true;
                    test = true;
                }
            } else if (savedOrders.get(i) == 12) {
                if (valueParam.equals("false")) {
                    vitamin = false;
                } else {
                    vitamin = true;
                }
            } else if (savedOrders.get(i) == 13) {
                if (valueParam.equals("false")) {
                    test = false;
                } else {
                    test = true;
                }
            }

            if (savedOrders.get(i) == 15) {
                contraidication = contraidication + valueParam;
            }

            if (savedOrders.get(i) == 3) {
                if (!valueParam.equals("null")) {
                    recomendation = recomendation + valueParam;
                }
            } else if (savedOrders.get(i) > 7 && savedOrders.get(i) < 10) {
                recomendation = recomendation + valueParam;
            }

            if (savedOrders.get(i) == 16) {
                price = Integer.parseInt(valueParam);
            }


        }

        recomendation=checkRecomendation(recomendation);

        ArrayList<Product> selectProducts=db.getSelectProduscts(vitamin,test,recomendation,contraidication,level,age,timeUse);
        ArrayList<Integer> selectIndex=new ArrayList<Integer>();

        for(int i=0;i<selectProducts.size();i++){
            if(timeUse-(selectProducts.get(i).size/selectProducts.get(i).normDos)<50){
                selectIndex.add(i);
            }else if (timeUse>95){
                selectIndex.add(i);
            }
        }
        for(int i=0;i<selectIndex.size();i++){
            message=message+selectProducts.get(selectIndex.get(i)).name+" производитель:"+selectProducts.get(selectIndex.get(i)).manufacturer+
                    " размер:"+selectProducts.get(selectIndex.get(i)).size+" цена:"+selectProducts.get(selectIndex.get(i)).price+"\n";
        }

        if(message.equals("")){
            message="Мы не смогли подобрать комлекс БАДов по Ваши нужды \nНачать по новой: /start";
        }else {
            message=message+"\nНачать по новой: /start";
        }


        return message;
    }

    public String checkRecomendation(String recomendation) {
        String selectRec = "";
        int S;
        int P;
        int M;
        int V;

        S = StringUtils.countMatches(recomendation, "С");
        P = StringUtils.countMatches(recomendation, "П");
        M = StringUtils.countMatches(recomendation, "М");
        V = StringUtils.countMatches(recomendation, "В");

        if(S>=P && S>=M && S>=V){
            selectRec="С";
        }else if(P>=S && P>=M && P>=V){
            selectRec="П";
        }else if(M>=P && M>=P && M>=V){
            selectRec="М";
        }else if(V>=S && V>=P & V>=M){
            selectRec="В";
        }
        return selectRec;
    }

    public ArrayList<String> getSavedQuest() {
        return savedQuest;
    }

    public void setSavedQuest(ArrayList<String> savedQuest) {
        this.savedQuest = savedQuest;
    }

    public ArrayList<String> getSavedAnswers() {
        return savedAnswers;
    }

    public void setSavedAnswers(ArrayList<String> savedAnswers) {
        this.savedAnswers = savedAnswers;
    }

    public ArrayList<Integer> getSavedOrders() {
        return savedOrders;
    }

    public void setSavedOrders(ArrayList<Integer> savedOrders) {
        this.savedOrders = savedOrders;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }


}
