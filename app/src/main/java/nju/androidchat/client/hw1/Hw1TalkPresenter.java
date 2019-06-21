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
//        if(isMarkdownImg(clientMessage.getMessage())){
//            String img_str = getOnlineImg("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560850457503&di=07283544224fa5ebad3dbe942fc70759&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201803%2F19%2F20180319235316_iinoc.jpg");
//            clientMessages.add(new ClientMessage(img_str));
//        }else{
//            clientMessages.add(clientMessage);
//        }
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

    private boolean isMarkdownImg(String text){
        if(text.matches("!\\[.*\\]\\(.*\\)")){
            return true;
        }
        else{
            return false;
        }
    }

    private String getOnlineImg(String url){
        String text = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();

            InputStream is = conn.getInputStream();//获得图片的数据流
            byte[] buffer = new byte[conn.getContentLength()];
            is.read(buffer);
            text = new String(buffer);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
}
