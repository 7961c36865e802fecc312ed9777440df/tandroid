package org.telegram.ui.Adapters;

import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.Adapters.MentionsAdapter;
/* loaded from: classes3.dex */
public final /* synthetic */ class MentionsAdapter$4$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ MentionsAdapter.AnonymousClass4 f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ TLRPC$TL_error f$2;
    public final /* synthetic */ TLObject f$3;
    public final /* synthetic */ MessagesController f$4;
    public final /* synthetic */ MessagesStorage f$5;

    public /* synthetic */ MentionsAdapter$4$$ExternalSyntheticLambda0(MentionsAdapter.AnonymousClass4 anonymousClass4, String str, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, MessagesController messagesController, MessagesStorage messagesStorage) {
        this.f$0 = anonymousClass4;
        this.f$1 = str;
        this.f$2 = tLRPC$TL_error;
        this.f$3 = tLObject;
        this.f$4 = messagesController;
        this.f$5 = messagesStorage;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$run$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}