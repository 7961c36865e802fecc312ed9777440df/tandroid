package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.messenger.MessagesController;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda118 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ MessagesController.SponsoredMessagesInfo f$3;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda118(MessagesController messagesController, ArrayList arrayList, long j, MessagesController.SponsoredMessagesInfo sponsoredMessagesInfo) {
        this.f$0 = messagesController;
        this.f$1 = arrayList;
        this.f$2 = j;
        this.f$3 = sponsoredMessagesInfo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$getSponsoredMessages$338(this.f$1, this.f$2, this.f$3);
    }
}