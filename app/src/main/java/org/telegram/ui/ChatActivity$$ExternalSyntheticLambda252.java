package org.telegram.ui;

import android.text.style.URLSpan;
import android.view.View;
import org.telegram.ui.Components.TranslateAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda252 implements TranslateAlert.OnLinkPress {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ View f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda252(ChatActivity chatActivity, View view) {
        this.f$0 = chatActivity;
        this.f$1 = view;
    }

    @Override // org.telegram.ui.Components.TranslateAlert.OnLinkPress
    public final boolean run(URLSpan uRLSpan) {
        boolean lambda$createMenu$173;
        lambda$createMenu$173 = this.f$0.lambda$createMenu$173(this.f$1, uRLSpan);
        return lambda$createMenu$173;
    }
}