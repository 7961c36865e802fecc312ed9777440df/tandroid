package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessageStatisticActivity$$ExternalSyntheticLambda8 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ MessageStatisticActivity f$0;

    public /* synthetic */ MessageStatisticActivity$$ExternalSyntheticLambda8(MessageStatisticActivity messageStatisticActivity) {
        this.f$0 = messageStatisticActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$9();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}