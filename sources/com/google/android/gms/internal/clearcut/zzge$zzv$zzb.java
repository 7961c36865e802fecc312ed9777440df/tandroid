package com.google.android.gms.internal.clearcut;
/* loaded from: classes.dex */
public enum zzge$zzv$zzb implements zzcj {
    zzbhk(0),
    zzbhl(1),
    zzbhm(2),
    zzbhn(3),
    zzbho(4);
    
    private final int value;

    static {
        new zzck<zzge$zzv$zzb>() { // from class: com.google.android.gms.internal.clearcut.zzgr
            @Override // com.google.android.gms.internal.clearcut.zzck
            public final /* synthetic */ zzge$zzv$zzb zzb(int i) {
                return zzge$zzv$zzb.zzbc(i);
            }
        };
    }

    zzge$zzv$zzb(int i) {
        this.value = i;
    }

    public static zzge$zzv$zzb zzbc(int i) {
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i != 4) {
                            return null;
                        }
                        return zzbho;
                    }
                    return zzbhn;
                }
                return zzbhm;
            }
            return zzbhl;
        }
        return zzbhk;
    }

    @Override // com.google.android.gms.internal.clearcut.zzcj
    public final int zzc() {
        return this.value;
    }
}
