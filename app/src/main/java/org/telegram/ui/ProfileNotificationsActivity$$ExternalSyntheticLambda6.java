package org.telegram.ui;

import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileNotificationsActivity$$ExternalSyntheticLambda6 implements AlertsCreator.SoundFrequencyDelegate {
    public final /* synthetic */ ProfileNotificationsActivity f$0;

    public /* synthetic */ ProfileNotificationsActivity$$ExternalSyntheticLambda6(ProfileNotificationsActivity profileNotificationsActivity) {
        this.f$0 = profileNotificationsActivity;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.SoundFrequencyDelegate
    public final void didSelectValues(int i, int i2) {
        this.f$0.lambda$createView$4(i, i2);
    }
}