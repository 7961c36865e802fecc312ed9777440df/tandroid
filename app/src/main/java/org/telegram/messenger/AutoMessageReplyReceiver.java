package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.core.app.RemoteInput;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
import org.telegram.tgnet.TLRPC$WebPage;
/* loaded from: classes.dex */
public class AutoMessageReplyReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        ApplicationLoader.postInitApplication();
        Bundle resultsFromIntent = RemoteInput.getResultsFromIntent(intent);
        if (resultsFromIntent == null) {
            return;
        }
        CharSequence charSequence = resultsFromIntent.getCharSequence(NotificationsController.EXTRA_VOICE_REPLY);
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        long longExtra = intent.getLongExtra("dialog_id", 0L);
        int intExtra = intent.getIntExtra("max_id", 0);
        int intExtra2 = intent.getIntExtra("currentAccount", 0);
        if (longExtra == 0 || intExtra == 0 || !UserConfig.isValidAccount(intExtra2)) {
            return;
        }
        SendMessagesHelper.getInstance(intExtra2).sendMessage(charSequence.toString(), longExtra, (MessageObject) null, (MessageObject) null, (TLRPC$WebPage) null, true, (ArrayList<TLRPC$MessageEntity>) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, true, 0, (MessageObject.SendAnimationData) null, false);
        MessagesController.getInstance(intExtra2).markDialogAsRead(longExtra, intExtra, intExtra, 0, false, 0, 0, true, 0);
    }
}