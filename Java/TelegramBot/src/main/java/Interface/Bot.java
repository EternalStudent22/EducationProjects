package Interface;

import DataBase.DataBaseHandler;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    DataBaseHandler db=new DataBaseHandler();
    Test test = new Test();
    int anchor =0;

    Bot() {
        test.setQuestions(db.getListQuest());
    }


    public static  void inicilizeBot(){
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new Bot());
        } catch (TelegramApiException var3) {
            var3.printStackTrace();
        }


    }

    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();


        if(update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            test.answers.add(callbackQuery.getData());
            db.setAnswers(callbackQuery.getData(),callbackQuery.getMessage().getText(),anchor,
                    callbackQuery.getMessage().getChatId().toString());
            try {
                if(anchor==7 || anchor==11 || anchor==14){
                    anchor=db.checkOrderNext(anchor,callbackQuery.getData())-1;
                }
                if(anchor==16){
                    db.getParam(test.savedQuest,test.savedAnswers,test.savedOrders,
                            callbackQuery.getMessage().getChatId().toString());
                   //
                    execute(sendMsg(callbackQuery,test.colculationTest()));
                    //вывести результат опроса и обнулить все.
                    db.deleteSaveAnsw(callbackQuery.getMessage().getChatId().toString());
                    test.questions.clear();
                    test.answers.clear();
                }
                execute(sendMsgInlineBut(callbackQuery,test.getQuestions().get(anchor),
                        db.getListAnswer(test.getQuestions().get(anchor))));

                anchor++;
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        if (message != null && message.hasText()) {
            if(message.getText().equals("/start")){
                db.deleteSaveAnsw(message.getChatId().toString());
                try {
                    if(db.checkChat(message.getChatId().toString())==false){
                        db.setUser(message.getChatId().toString());
                    }
                    anchor=0;
//                    execute(sendMsgInlineBut(message,test.getQuestions().get(anchor),
//                            db.getListAnswer(test.getQuestions().get(anchor))));
                    execute(sendMsg(message,test.getQuestions().get(anchor)));

                    anchor++;
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }
            else if(message.getText() !=""){
                try {
                    test.answers.add(message.getText());
                    if(anchor==6){
                        double mass = Integer.parseInt(test.answers.get(4));
                        double growth = Integer.parseInt(test.answers.get(3));
                        growth= (growth/100)*(growth/100);
                        int bodyMassIndex=(int)(mass/growth);
                        test.answers.add(String.valueOf(bodyMassIndex));
                        db.setAnswers(test.getAnswers().get(anchor-2),test.getQuestions().get(anchor-2),anchor-1,
                                message.getChatId().toString());
                        db.setAnswers(String.valueOf(bodyMassIndex),test.getQuestions().get(anchor-1),anchor,
                            message.getChatId().toString());
                    }else{
                           db.setAnswers(message.getText(),test.getQuestions().get(anchor-1),anchor,
                            message.getChatId().toString());
                    }

                    execute(sendMsgInlineBut(message,test.getQuestions().get(anchor),
                            db.getListAnswer(test.getQuestions().get(anchor))));
                    if(anchor==4){
                        anchor=anchor+2;
                        return;
                    }

                    anchor++;
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public SendMessage sendMsg(CallbackQuery callbackQuery, String text) {  // sendMsg генерит текстовые сообщения
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(callbackQuery.getMessage().getChatId().toString());
        return sendMessage;
    }

    public SendMessage sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(message.getChatId().toString());
        return sendMessage;
    }

    public SendMessage sendMsgInlineBut(Message message, String text, ArrayList<String> text_but) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(this.getInlineMessageButtons(text_but));
        return sendMessage;
    }

    public SendMessage sendMsgInlineBut(CallbackQuery callbackQuery, String text, ArrayList<String> text_but) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(callbackQuery.getMessage().getChatId());
        sendMessage.setReplyToMessageId(callbackQuery.getMessage().getMessageId());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(this.getInlineMessageButtons(text_but));
        return sendMessage;
    }

    public InlineKeyboardMarkup getInlineMessageButtons(ArrayList<String> text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList();

        for(int i = 0; i < text.size(); i++) {
            InlineKeyboardButton button = (new InlineKeyboardButton()).setText(text.get(i));
            button.setCallbackData(text.get(i));
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList();
            keyboardButtonsRow.add(button);
            rowList.add(keyboardButtonsRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public String getBotUsername() {
        return "ShopBot";
    }

    public String getBotToken() {
        return "1338019139:AAFREop2lnN6Ufn2oqAy1FDDanH-LpfX_1E";
    }
}

