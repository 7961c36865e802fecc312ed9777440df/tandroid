package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChannelAdminLogActivity$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ChannelAdminLogActivity f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ ChannelAdminLogActivity$$ExternalSyntheticLambda0(ChannelAdminLogActivity channelAdminLogActivity, String str) {
        this.f$0 = channelAdminLogActivity;
        this.f$1 = str;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showOpenUrlAlert$12(this.f$1, dialogInterface, i);
    }
}