package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.messenger.MessagesStorage;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda37 implements DialogInterface.OnClickListener {
    public final /* synthetic */ int[] f$0;
    public final /* synthetic */ MessagesStorage.IntCallback f$1;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda37(int[] iArr, MessagesStorage.IntCallback intCallback) {
        this.f$0 = iArr;
        this.f$1 = intCallback;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createLocationUpdateDialog$94(this.f$0, this.f$1, dialogInterface, i);
    }
}