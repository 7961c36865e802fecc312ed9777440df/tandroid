package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.NotificationsCustomSettingsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ NotificationsCustomSettingsActivity.SearchAdapter f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ ArrayList f$2;

    public /* synthetic */ NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda2(NotificationsCustomSettingsActivity.SearchAdapter searchAdapter, String str, ArrayList arrayList) {
        this.f$0 = searchAdapter;
        this.f$1 = str;
        this.f$2 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processSearch$2(this.f$1, this.f$2);
    }
}