package org.telegram.ui;

import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.LocationActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LocationActivity$MapOverlayView$$ExternalSyntheticLambda1 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ LocationActivity.MapOverlayView f$0;
    public final /* synthetic */ LocationActivity.VenueLocation f$1;

    public /* synthetic */ LocationActivity$MapOverlayView$$ExternalSyntheticLambda1(LocationActivity.MapOverlayView mapOverlayView, LocationActivity.VenueLocation venueLocation) {
        this.f$0 = mapOverlayView;
        this.f$1 = venueLocation;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$addInfoView$0(this.f$1, z, i);
    }
}