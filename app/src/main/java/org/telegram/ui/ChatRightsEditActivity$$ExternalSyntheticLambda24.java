package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatRightsEditActivity$$ExternalSyntheticLambda24 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ ChatRightsEditActivity f$0;

    public /* synthetic */ ChatRightsEditActivity$$ExternalSyntheticLambda24(ChatRightsEditActivity chatRightsEditActivity) {
        this.f$0 = chatRightsEditActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$26();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}