package org.telegram.messenger;

import java.util.HashMap;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.SendMessagesHelper;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$$ExternalSyntheticLambda41 implements Runnable {
    public final /* synthetic */ SendMessagesHelper f$0;
    public final /* synthetic */ SendMessagesHelper.ImportingStickers f$1;
    public final /* synthetic */ HashMap f$2;
    public final /* synthetic */ String f$3;
    public final /* synthetic */ MessagesStorage.StringCallback f$4;

    public /* synthetic */ SendMessagesHelper$$ExternalSyntheticLambda41(SendMessagesHelper sendMessagesHelper, SendMessagesHelper.ImportingStickers importingStickers, HashMap hashMap, String str, MessagesStorage.StringCallback stringCallback) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = importingStickers;
        this.f$2 = hashMap;
        this.f$3 = str;
        this.f$4 = stringCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$prepareImportStickers$71(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}