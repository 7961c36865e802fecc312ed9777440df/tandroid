package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzn extends zzeo implements zzgb {
    private static final zzy$zzn zzf;
    private static volatile zzgj zzg;
    private int zzc;
    private int zzd;
    private int zze;

    public enum zza implements zzet {
        zza(0),
        zzb(1),
        zzc(2);

        private static final zzes zzd = new zzad();
        private final int zze;

        zza(int i) {
            this.zze = i;
        }

        public static zzev zzb() {
            return zzac.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zza.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zze + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zze;
        }
    }

    public static final class zzb extends zzeo.zzb implements zzgb {
        private zzb() {
            super(zzy$zzn.zzf);
        }

        /* synthetic */ zzb(zzx zzxVar) {
            this();
        }
    }

    static {
        zzy$zzn zzy_zzn = new zzy$zzn();
        zzf = zzy_zzn;
        zzeo.zza(zzy$zzn.class, zzy_zzn);
    }

    private zzy$zzn() {
    }

    /* JADX WARN: Type inference failed for: r3v14, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzn();
            case 2:
                return new zzb(zzxVar);
            case 3:
                return zzeo.zza(zzf, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001င\u0000\u0002ဌ\u0001", new Object[]{"zzc", "zzd", "zze", zza.zzb()});
            case 4:
                return zzf;
            case 5:
                zzgj zzgjVar = zzg;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzn.class) {
                        try {
                            zzgj zzgjVar3 = zzg;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzf);
                                zzg = zzaVar;
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
