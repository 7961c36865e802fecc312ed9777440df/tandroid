package org.telegram.ui;

import org.telegram.ui.Components.PasscodeView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ExternalActionActivity$$ExternalSyntheticLambda10 implements PasscodeView.PasscodeViewDelegate {
    public final /* synthetic */ ExternalActionActivity f$0;

    public /* synthetic */ ExternalActionActivity$$ExternalSyntheticLambda10(ExternalActionActivity externalActionActivity) {
        this.f$0 = externalActionActivity;
    }

    @Override // org.telegram.ui.Components.PasscodeView.PasscodeViewDelegate
    public final void didAcceptedPassword() {
        this.f$0.lambda$showPasscodeActivity$2();
    }
}