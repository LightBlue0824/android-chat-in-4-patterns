package nju.androidchat.client.hw1;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.extern.java.Log;
import nju.androidchat.client.ClientMessage;
import nju.androidchat.client.R;
import nju.androidchat.client.Utils;
import nju.androidchat.client.component.ItemTextReceive;
import nju.androidchat.client.component.ItemTextSend;
import nju.androidchat.client.component.OnRecallMessageRequested;

@Log
public class Hw1TalkActivity extends AppCompatActivity implements Hw1Contract.View, TextView.OnEditorActionListener, OnRecallMessageRequested {
    private Hw1Contract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hw1TalkModel hw1TalkModel = new Hw1TalkModel();

        // Create the presenter
        this.presenter = new Hw1TalkPresenter(hw1TalkModel, this, new ArrayList<>());
        hw1TalkModel.setIHw1TalkPresenter(this.presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showMessageList(List<ClientMessage> messages) {
        runOnUiThread(() -> {
                    LinearLayout content = findViewById(R.id.chat_content);

                    // 删除所有已有的ItemText
                    content.removeAllViews();

                    // 增加ItemText
                    for (ClientMessage message : messages) {
                        String text = String.format("%s", message.getMessage());
                        if(isMarkdownImg(text)){
                            new Thread(() -> {
                                String[] t_strs = text.split("!\\[.*\\]");
                                String url = t_strs[t_strs.length-1];
                                url = url.substring(1, url.length()-1);
//                                url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560850457503&di=07283544224fa5ebad3dbe942fc70759&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201803%2F19%2F20180319235316_iinoc.jpg";
                                //只考虑网络图片
                                String img_html = "<img src='" + url + "'>";
                                CharSequence charSequence_t;
                                try {
                                    charSequence_t = Html.fromHtml(img_html, new Html.ImageGetter() {
                                        @Override
                                        public Drawable getDrawable(String source) {
                                            Drawable drawable = getOnlineImg(source);
                                            //对图片进行压缩（此处我采用原图）
                                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                            return drawable;
                                        }
                                    }, null);
                                }catch (Exception e){
                                    //网络异常，url不正确等
                                    charSequence_t = "网络异常或url不正确";
                                }
                                CharSequence charSequence = charSequence_t;
                                runOnUiThread(() -> {
                                    if (message.getSenderUsername().equals(this.presenter.getUsername())) {
                                        content.addView(new ItemTextSend(this, charSequence, message.getMessageId(), this));
                                    } else {
                                        content.addView(new ItemTextReceive(this, charSequence, message.getMessageId()));
                                    }
                                    Utils.scrollListToBottom(this);
                                });
                            }).start();
                        }
                        else{
                            // 如果是自己发的，增加ItemTextSend
                            if (message.getSenderUsername().equals(this.presenter.getUsername())) {
                                content.addView(new ItemTextSend(this, text, message.getMessageId(), this));
                            } else {
                                content.addView(new ItemTextReceive(this, text, message.getMessageId()));
                            }
                        }
//                        // 如果是自己发的，增加ItemTextSend
//                        if (message.getSenderUsername().equals(this.presenter.getUsername())) {
//                            content.addView(new ItemTextSend(this, text, message.getMessageId(), this));
//                        } else {
//                            content.addView(new ItemTextReceive(this, text, message.getMessageId()));
//                        }
                    }

                    Utils.scrollListToBottom(this);
                }
        );
    }

    @Override
    public void setPresenter(Hw1Contract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            return hideKeyboard();
        }
        return super.onTouchEvent(event);
    }

    private boolean hideKeyboard() {
        return Utils.hideKeyboard(this);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (Utils.send(actionId, event)) {
            hideKeyboard();
            // 异步地让Controller处理事件
            sendText();
        }
        return false;
    }

    private void sendText() {
        EditText text = findViewById(R.id.et_content);
        AsyncTask.execute(() -> {
            this.presenter.sendMessage(text.getText().toString());
        });
    }

    public void onBtnSendClicked(View v) {
        hideKeyboard();
        sendText();
    }

    // 当用户长按消息，并选择撤回消息时做什么，MVP-0不实现
    @Override
    public void onRecallMessageRequested(UUID messageId) {

    }

    private boolean isMarkdownImg(String text){
        if(text.matches("!\\[.*\\]\\(.*\\)")){
            return true;
        }
        else{
            return false;
        }
    }

    private Drawable getOnlineImg(String url){
//        String text = null;
        Drawable drawable = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();

            InputStream is = conn.getInputStream();//获得图片的数据流
            drawable = Drawable.createFromStream(is, "");
//            byte[] buffer = new byte[conn.getContentLength()];
//            is.read(buffer);
//            text = new String(buffer);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return text;
        return drawable;
    }

}
