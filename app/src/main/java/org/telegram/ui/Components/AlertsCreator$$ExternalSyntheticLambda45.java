package org.telegram.ui.Components;

import android.content.DialogInterface;
import androidx.core.util.Consumer;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda45 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ AtomicBoolean f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda45(AtomicBoolean atomicBoolean, Consumer consumer) {
        this.f$0 = atomicBoolean;
        this.f$1 = consumer;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        AlertsCreator.lambda$createWebViewPermissionsRequestDialog$4(this.f$0, this.f$1, dialogInterface);
    }
}