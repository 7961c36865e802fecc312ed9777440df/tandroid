package com.google.android.gms.internal.icing;
/* compiled from: com.google.firebase:firebase-appindexing@@20.0.0 */
/* loaded from: classes3.dex */
public class zzdm {
    private static final zzcp zzb = zzcp.zza();
    protected volatile zzee zza;
    private volatile zzcf zzc;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzdm)) {
            return false;
        }
        zzdm zzdmVar = (zzdm) obj;
        zzee zzeeVar = this.zza;
        zzee zzeeVar2 = zzdmVar.zza;
        if (zzeeVar != null || zzeeVar2 != null) {
            if (zzeeVar == null || zzeeVar2 == null) {
                if (zzeeVar != null) {
                    zzdmVar.zzc(zzeeVar.zzm());
                    return zzeeVar.equals(zzdmVar.zza);
                }
                zzc(zzeeVar2.zzm());
                return this.zza.equals(zzeeVar2);
            }
            return zzeeVar.equals(zzeeVar2);
        }
        return zzb().equals(zzdmVar.zzb());
    }

    public int hashCode() {
        return 1;
    }

    public final int zza() {
        if (this.zzc != null) {
            return ((zzcd) this.zzc).zza.length;
        }
        if (this.zza == null) {
            return 0;
        }
        return this.zza.zzo();
    }

    public final zzcf zzb() {
        if (this.zzc != null) {
            return this.zzc;
        }
        synchronized (this) {
            if (this.zzc != null) {
                return this.zzc;
            }
            if (this.zza == null) {
                this.zzc = zzcf.zzb;
            } else {
                this.zzc = this.zza.zzg();
            }
            return this.zzc;
        }
    }

    protected final void zzc(zzee zzeeVar) {
        if (this.zza != null) {
            return;
        }
        synchronized (this) {
            if (this.zza != null) {
                return;
            }
            try {
                this.zza = zzeeVar;
                this.zzc = zzcf.zzb;
            } catch (zzdj e) {
                this.zza = zzeeVar;
                this.zzc = zzcf.zzb;
            }
        }
    }
}
