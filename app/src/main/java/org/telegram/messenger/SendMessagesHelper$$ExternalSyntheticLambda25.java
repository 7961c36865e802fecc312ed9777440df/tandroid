package org.telegram.messenger;

import java.io.File;
import org.telegram.messenger.SendMessagesHelper;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$$ExternalSyntheticLambda25 implements Runnable {
    public final /* synthetic */ SendMessagesHelper f$0;
    public final /* synthetic */ File f$1;
    public final /* synthetic */ MessageObject f$2;
    public final /* synthetic */ SendMessagesHelper.DelayedMessage f$3;
    public final /* synthetic */ String f$4;

    public /* synthetic */ SendMessagesHelper$$ExternalSyntheticLambda25(SendMessagesHelper sendMessagesHelper, File file, MessageObject messageObject, SendMessagesHelper.DelayedMessage delayedMessage, String str) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = file;
        this.f$2 = messageObject;
        this.f$3 = delayedMessage;
        this.f$4 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didReceivedNotification$2(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}