package nju.androidchat.client.component;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.StyleableRes;

import java.util.UUID;

import nju.androidchat.client.R;

public class ItemTextReceive extends LinearLayout {


    @StyleableRes
    int index0 = 0;

    private TextView textView;
    private Context context;
    private UUID messageId;
    private OnRecallMessageRequested onRecallMessageRequested;


//    public ItemTextReceive(Context context, String text, UUID messageId) {
//        super(context);
//        this.context = context;
//        inflate(context, R.layout.item_text_receive, this);
//        this.textView = findViewById(R.id.chat_item_content_text);
//        this.messageId = messageId;
//        setText(text);
//    }

    /**
     * 修改了原本传入的String类型text，改为CharSequence
     * @param context
     * @param text
     * @param messageId
     */
    public ItemTextReceive(Context context, CharSequence text, UUID messageId) {
        super(context);
        this.context = context;
        inflate(context, R.layout.item_text_receive, this);
        this.textView = findViewById(R.id.chat_item_content_text);
        this.messageId = messageId;
        setText(text);
    }

    public void init(Context context) {

    }

    public String getText() {
        return textView.getText().toString();
    }

//    public void setText(String text) {
//        textView.setText(text);
//    }

    /**
     * 修改了原本传入的String类型text，改为CharSequence
     * @param text
     */
    public void setText(CharSequence text) {
        textView.setText(text);
    }

}
