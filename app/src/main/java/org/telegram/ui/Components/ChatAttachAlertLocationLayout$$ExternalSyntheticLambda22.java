package org.telegram.ui.Components;

import java.util.ArrayList;
import org.telegram.ui.Adapters.BaseLocationAdapter;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertLocationLayout$$ExternalSyntheticLambda22 implements BaseLocationAdapter.BaseLocationAdapterDelegate {
    public final /* synthetic */ ChatAttachAlertLocationLayout f$0;

    public /* synthetic */ ChatAttachAlertLocationLayout$$ExternalSyntheticLambda22(ChatAttachAlertLocationLayout chatAttachAlertLocationLayout) {
        this.f$0 = chatAttachAlertLocationLayout;
    }

    @Override // org.telegram.ui.Adapters.BaseLocationAdapter.BaseLocationAdapterDelegate
    public final void didLoadSearchResult(ArrayList arrayList) {
        this.f$0.updatePlacesMarkers(arrayList);
    }
}