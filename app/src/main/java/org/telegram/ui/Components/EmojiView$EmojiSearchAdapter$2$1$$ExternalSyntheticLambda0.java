package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.ui.Components.EmojiView;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiView$EmojiSearchAdapter$2$1$$ExternalSyntheticLambda0 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ EmojiView.EmojiSearchAdapter.AnonymousClass2.AnonymousClass1 f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ EmojiView$EmojiSearchAdapter$2$1$$ExternalSyntheticLambda0(EmojiView.EmojiSearchAdapter.AnonymousClass2.AnonymousClass1 anonymousClass1, int i) {
        this.f$0 = anonymousClass1;
        this.f$1 = i;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$onClick$2(this.f$1, dialogInterface);
    }
}