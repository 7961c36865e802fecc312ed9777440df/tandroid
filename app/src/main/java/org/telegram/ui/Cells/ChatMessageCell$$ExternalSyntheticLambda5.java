package org.telegram.ui.Cells;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatMessageCell$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ ChatMessageCell f$0;
    public final /* synthetic */ ChatMessageCell f$1;

    public /* synthetic */ ChatMessageCell$$ExternalSyntheticLambda5(ChatMessageCell chatMessageCell, ChatMessageCell chatMessageCell2) {
        this.f$0 = chatMessageCell;
        this.f$1 = chatMessageCell2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$checkSpoilersMotionEvent$3(this.f$1);
    }
}