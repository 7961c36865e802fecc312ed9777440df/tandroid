package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.Components.ChatAttachAlertLocationLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertLocationLayout$MapOverlayView$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ ChatAttachAlertLocationLayout.MapOverlayView f$0;
    public final /* synthetic */ ChatAttachAlertLocationLayout.VenueLocation f$1;

    public /* synthetic */ ChatAttachAlertLocationLayout$MapOverlayView$$ExternalSyntheticLambda0(ChatAttachAlertLocationLayout.MapOverlayView mapOverlayView, ChatAttachAlertLocationLayout.VenueLocation venueLocation) {
        this.f$0 = mapOverlayView;
        this.f$1 = venueLocation;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$addInfoView$1(this.f$1, view);
    }
}