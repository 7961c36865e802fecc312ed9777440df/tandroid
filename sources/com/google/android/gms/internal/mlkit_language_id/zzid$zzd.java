package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzid$zzd extends zzeo implements zzgb {
    private static final zzid$zzd zzk;
    private static volatile zzgj zzl;
    private int zzc;
    private int zzg;
    private int zzh;
    private int zzj;
    private String zzd = "";
    private String zze = "";
    private String zzf = "";
    private String zzi = "";

    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzid$zzd.zzk);
        }

        /* synthetic */ zza(zzic zzicVar) {
            this();
        }
    }

    static {
        zzid$zzd zzid_zzd = new zzid$zzd();
        zzk = zzid_zzd;
        zzeo.zza(zzid$zzd.class, zzid_zzd);
    }

    private zzid$zzd() {
    }

    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzic zzicVar = null;
        switch (zzic.zza[i - 1]) {
            case 1:
                return new zzid$zzd();
            case 2:
                return new zza(zzicVar);
            case 3:
                return zzeo.zza(zzk, "\u0001\u0007\u0000\u0001\u0001\u0007\u0007\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဈ\u0001\u0003ဈ\u0002\u0004င\u0003\u0005င\u0004\u0006ဈ\u0005\u0007င\u0006", new Object[]{"zzc", "zzd", "zze", "zzf", "zzg", "zzh", "zzi", "zzj"});
            case 4:
                return zzk;
            case 5:
                zzgj zzgjVar = zzl;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzid$zzd.class) {
                        try {
                            zzgj zzgjVar3 = zzl;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzk);
                                zzl = zzaVar;
                                zzgjVar4 = zzaVar;
                            }
                        } finally {
                        }
                    }
                }
                return zzgjVar2;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
