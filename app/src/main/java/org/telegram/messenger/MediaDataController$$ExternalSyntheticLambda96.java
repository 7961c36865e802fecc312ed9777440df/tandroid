package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$TL_contacts_topPeers;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda96 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ TLRPC$TL_contacts_topPeers f$1;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda96(MediaDataController mediaDataController, TLRPC$TL_contacts_topPeers tLRPC$TL_contacts_topPeers) {
        this.f$0 = mediaDataController;
        this.f$1 = tLRPC$TL_contacts_topPeers;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadHints$107(this.f$1);
    }
}