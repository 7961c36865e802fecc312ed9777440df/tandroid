package org.telegram.ui;

import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda74 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ PhotoViewer f$0;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda74(PhotoViewer photoViewer) {
        this.f$0 = photoViewer;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.sendPressed(z, i);
    }
}