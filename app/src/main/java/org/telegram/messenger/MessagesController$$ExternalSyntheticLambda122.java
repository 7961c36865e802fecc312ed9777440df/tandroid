package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC$updates_ChannelDifference;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda122 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ TLRPC$updates_ChannelDifference f$2;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda122(MessagesController messagesController, ArrayList arrayList, TLRPC$updates_ChannelDifference tLRPC$updates_ChannelDifference) {
        this.f$0 = messagesController;
        this.f$1 = arrayList;
        this.f$2 = tLRPC$updates_ChannelDifference;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$getChannelDifference$269(this.f$1, this.f$2);
    }
}