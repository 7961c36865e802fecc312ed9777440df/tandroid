package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC$TL_messages_inactiveChats;
/* loaded from: classes3.dex */
public final /* synthetic */ class TooManyCommunitiesActivity$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ TooManyCommunitiesActivity f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ TLRPC$TL_messages_inactiveChats f$2;

    public /* synthetic */ TooManyCommunitiesActivity$$ExternalSyntheticLambda2(TooManyCommunitiesActivity tooManyCommunitiesActivity, ArrayList arrayList, TLRPC$TL_messages_inactiveChats tLRPC$TL_messages_inactiveChats) {
        this.f$0 = tooManyCommunitiesActivity;
        this.f$1 = arrayList;
        this.f$2 = tLRPC$TL_messages_inactiveChats;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadInactiveChannels$4(this.f$1, this.f$2);
    }
}