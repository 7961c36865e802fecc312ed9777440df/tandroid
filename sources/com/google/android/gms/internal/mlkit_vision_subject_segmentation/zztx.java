package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

/* loaded from: classes.dex */
public final class zztx {
    private static zztx zza;

    private zztx() {
    }

    public static synchronized zztx zza() {
        zztx zztxVar;
        synchronized (zztx.class) {
            try {
                if (zza == null) {
                    zza = new zztx();
                }
                zztxVar = zza;
            } catch (Throwable th) {
                throw th;
            }
        }
        return zztxVar;
    }
}
