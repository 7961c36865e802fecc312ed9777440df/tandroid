package org.telegram.ui.Components;

import org.telegram.tgnet.TLObject;
import org.telegram.ui.Components.EmojiView;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiView$GifAdapter$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ EmojiView.GifAdapter f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ EmojiView$GifAdapter$$ExternalSyntheticLambda1(EmojiView.GifAdapter gifAdapter, TLObject tLObject) {
        this.f$0 = gifAdapter;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$searchBotUser$0(this.f$1);
    }
}