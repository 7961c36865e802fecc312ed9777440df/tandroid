package org.telegram.ui;

import org.telegram.ui.Components.ReactionsContainerLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda166 implements Runnable {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ ReactionsContainerLayout f$4;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda166(ChatActivity chatActivity, int i, int i2, boolean z, ReactionsContainerLayout reactionsContainerLayout) {
        this.f$0 = chatActivity;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = z;
        this.f$4 = reactionsContainerLayout;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$createMenu$182(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}