package com.google.android.play.integrity.internal;

import android.os.IBinder;
import android.os.IInterface;
import java.util.List;
/* compiled from: com.google.android.play:integrity@@1.3.0 */
/* loaded from: classes.dex */
final class aa extends t {
    final /* synthetic */ IBinder a;
    final /* synthetic */ ad b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public aa(ad adVar, IBinder iBinder) {
        this.b = adVar;
        this.a = iBinder;
    }

    @Override // com.google.android.play.integrity.internal.t
    public final void b() {
        z zVar;
        List<Runnable> list;
        List list2;
        zVar = this.b.a.j;
        this.b.a.o = (IInterface) zVar.a(this.a);
        ae.r(this.b.a);
        this.b.a.h = false;
        list = this.b.a.e;
        for (Runnable runnable : list) {
            runnable.run();
        }
        list2 = this.b.a.e;
        list2.clear();
    }
}