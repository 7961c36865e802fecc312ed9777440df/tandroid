package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatEditActivity$$ExternalSyntheticLambda30 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ ChatEditActivity f$0;

    public /* synthetic */ ChatEditActivity$$ExternalSyntheticLambda30(ChatEditActivity chatEditActivity) {
        this.f$0 = chatEditActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$31();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}