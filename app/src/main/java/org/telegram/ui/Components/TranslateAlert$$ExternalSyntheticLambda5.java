package org.telegram.ui.Components;

import org.telegram.ui.Components.TranslateAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class TranslateAlert$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ TranslateAlert.OnTranslationFail f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ TranslateAlert$$ExternalSyntheticLambda5(TranslateAlert.OnTranslationFail onTranslationFail, boolean z) {
        this.f$0 = onTranslationFail;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.run(this.f$1);
    }
}