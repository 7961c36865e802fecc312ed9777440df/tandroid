package org.telegram.ui.Components;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivityEnterView$$ExternalSyntheticLambda48 implements Runnable {
    public final /* synthetic */ ChatActivityEnterView f$0;
    public final /* synthetic */ CharSequence f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ ChatActivityEnterView$$ExternalSyntheticLambda48(ChatActivityEnterView chatActivityEnterView, CharSequence charSequence, boolean z, int i) {
        this.f$0 = chatActivityEnterView;
        this.f$1 = charSequence;
        this.f$2 = z;
        this.f$3 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$sendMessageInternal$40(this.f$1, this.f$2, this.f$3);
    }
}