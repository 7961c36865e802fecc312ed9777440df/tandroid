package com.google.android.gms.internal.mlkit_common;

/* loaded from: classes.dex */
public abstract class zztd {
    private static zztc zza;

    public static synchronized zzss zza(zzsm zzsmVar) {
        zzss zzssVar;
        synchronized (zztd.class) {
            try {
                if (zza == null) {
                    zza = new zztc(null);
                }
                zzssVar = (zzss) zza.get(zzsmVar);
            } catch (Throwable th) {
                throw th;
            }
        }
        return zzssVar;
    }

    public static synchronized zzss zzb(String str) {
        zzss zza2;
        synchronized (zztd.class) {
            zza2 = zza(zzsm.zzd("common").zzd());
        }
        return zza2;
    }
}
