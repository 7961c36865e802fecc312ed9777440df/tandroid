package org.telegram.ui.Adapters;

import android.location.Location;
/* loaded from: classes3.dex */
public final /* synthetic */ class BaseLocationAdapter$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ BaseLocationAdapter f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ Location f$2;

    public /* synthetic */ BaseLocationAdapter$$ExternalSyntheticLambda1(BaseLocationAdapter baseLocationAdapter, String str, Location location) {
        this.f$0 = baseLocationAdapter;
        this.f$1 = str;
        this.f$2 = location;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$searchDelayed$0(this.f$1, this.f$2);
    }
}