package com.imfan.j.a91fan.session.viewholder;

import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.netease.nim.uikit.session.emoji.MoonUtil;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;

import java.util.Map;

/**
 * Created by huangjun on 2015/11/25.
 * Tip类型消息ViewHolder
 */
public class MsgViewHolderTip extends MsgViewHolderBase {

    protected TextView notificationTextView;

    @Override
    protected int getContentResId() {
        return com.netease.nim.uikit.R.layout.nim_message_item_notification;
    }

    @Override
    protected void inflateContentView() {
        notificationTextView = (TextView) view.findViewById(com.netease.nim.uikit.R.id.message_item_notification_label);
    }

    @Override
    protected void bindContentView() {
        String text = "未知通知提醒";
        if(TextUtils.isEmpty(message.getContent())) {
            Map<String, Object> content = message.getRemoteExtension();
            if (content != null && !content.isEmpty()) {
                text = (String) content.get("content");
            }
        } else {
            text = message.getContent();
        }

        handleTextNotification(text);
    }

    private void handleTextNotification(String text) {
        MoonUtil.identifyFaceExpressionAndATags(context, notificationTextView, text, ImageSpan.ALIGN_BOTTOM);
        notificationTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}
