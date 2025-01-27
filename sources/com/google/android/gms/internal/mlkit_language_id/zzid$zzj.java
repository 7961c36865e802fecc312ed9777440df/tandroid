package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzid$zzj extends zzeo implements zzgb {
    private static final zzid$zzj zzg;
    private static volatile zzgj zzh;
    private int zzc;
    private int zzd;
    private zzid$zzh zze;
    private zzid$zze zzf;

    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzid$zzj.zzg);
        }

        /* synthetic */ zza(zzic zzicVar) {
            this();
        }
    }

    public enum zzb implements zzet {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3),
        zze(4);

        private static final zzes zzf = new zzin();
        private final int zzg;

        zzb(int i) {
            this.zzg = i;
        }

        public static zzev zzb() {
            return zzio.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzb.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzg + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzg;
        }
    }

    static {
        zzid$zzj zzid_zzj = new zzid$zzj();
        zzg = zzid_zzj;
        zzeo.zza(zzid$zzj.class, zzid_zzj);
    }

    private zzid$zzj() {
    }

    /* JADX WARN: Type inference failed for: r3v16, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzic zzicVar = null;
        switch (zzic.zza[i - 1]) {
            case 1:
                return new zzid$zzj();
            case 2:
                return new zza(zzicVar);
            case 3:
                return zzeo.zza(zzg, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0000\u0000\u0001ဌ\u0000\u0002ဉ\u0001\u0003ဉ\u0002", new Object[]{"zzc", "zzd", zzb.zzb(), "zze", "zzf"});
            case 4:
                return zzg;
            case 5:
                zzgj zzgjVar = zzh;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzid$zzj.class) {
                        try {
                            zzgj zzgjVar3 = zzh;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzg);
                                zzh = zzaVar;
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
