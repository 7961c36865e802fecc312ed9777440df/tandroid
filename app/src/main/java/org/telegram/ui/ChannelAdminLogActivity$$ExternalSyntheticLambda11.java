package org.telegram.ui;

import androidx.collection.LongSparseArray;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventsFilter;
import org.telegram.ui.Components.AdminLogFilterAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChannelAdminLogActivity$$ExternalSyntheticLambda11 implements AdminLogFilterAlert.AdminLogFilterAlertDelegate {
    public final /* synthetic */ ChannelAdminLogActivity f$0;

    public /* synthetic */ ChannelAdminLogActivity$$ExternalSyntheticLambda11(ChannelAdminLogActivity channelAdminLogActivity) {
        this.f$0 = channelAdminLogActivity;
    }

    @Override // org.telegram.ui.Components.AdminLogFilterAlert.AdminLogFilterAlertDelegate
    public final void didSelectRights(TLRPC$TL_channelAdminLogEventsFilter tLRPC$TL_channelAdminLogEventsFilter, LongSparseArray longSparseArray) {
        this.f$0.lambda$createView$4(tLRPC$TL_channelAdminLogEventsFilter, longSparseArray);
    }
}