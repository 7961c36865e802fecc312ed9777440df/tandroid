package org.telegram.ui.Adapters;
/* loaded from: classes3.dex */
public final /* synthetic */ class StickersAdapter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ StickersAdapter f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ StickersAdapter$$ExternalSyntheticLambda0(StickersAdapter stickersAdapter, String str) {
        this.f$0 = stickersAdapter;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$searchEmojiByKeyword$1(this.f$1);
    }
}