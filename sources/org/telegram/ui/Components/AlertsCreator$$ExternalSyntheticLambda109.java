package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes4.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda109 implements RequestDelegate {
    public static final /* synthetic */ AlertsCreator$$ExternalSyntheticLambda109 INSTANCE = new AlertsCreator$$ExternalSyntheticLambda109();

    private /* synthetic */ AlertsCreator$$ExternalSyntheticLambda109() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AlertsCreator.lambda$createReportAlert$103(tLObject, tLRPC$TL_error);
    }
}