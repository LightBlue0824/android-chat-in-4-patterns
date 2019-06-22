package nju.androidchat.client.hw1;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.text.Html;
import android.util.Log;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nju.androidchat.client.ClientMessage;

@AllArgsConstructor
public class Hw1TalkPresenter implements Hw1Contract.Presenter {

    private Hw1Contract.Model hw1TalkModel;
    private Hw1Contract.View iHw1TalkView;

    @Getter
    private List<ClientMessage> clientMessages;

    @Override
    public void sendMessage(String content) {
        ClientMessage clientMessage = hw1TalkModel.sendInformation(content);
        refreshMessageList(clientMessage);
    }

    @Override
    public void receiveMessage(ClientMessage clientMessage) {
        refreshMessageList(clientMessage);
    }

    @Override
    public String getUsername() {
        return hw1TalkModel.getUsername();
    }

    private void refreshMessageList(ClientMessage clientMessage) {
        clientMessages.add(clientMessage);
        iHw1TalkView.showMessageList(clientMessages);
    }

    //撤回消息，Mvp0暂不实现
    @Override
    public void recallMessage(int index0) {

    }

    @Override
    public void start() {

    }
}
