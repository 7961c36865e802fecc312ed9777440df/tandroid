package com.google.android.play.core.integrity;

import android.content.Context;
import com.google.android.play.integrity.internal.ag;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.play:integrity@@1.3.0 */
/* loaded from: classes.dex */
public final class z {
    private static s a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized s a(Context context) {
        s sVar;
        synchronized (z.class) {
            if (a == null) {
                q qVar = new q(null);
                qVar.a(ag.a(context));
                a = qVar.b();
            }
            sVar = a;
        }
        return sVar;
    }
}