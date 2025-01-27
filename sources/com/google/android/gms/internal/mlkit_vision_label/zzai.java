package com.google.android.gms.internal.mlkit_vision_label;

import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
abstract class zzai implements zzbr {
    private transient Set zza;
    private transient Map zzb;

    zzai() {
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzbr) {
            return zzp().equals(((zzbr) obj).zzp());
        }
        return false;
    }

    public final int hashCode() {
        return zzp().hashCode();
    }

    public final String toString() {
        return ((zzy) zzp()).zza.toString();
    }

    abstract Map zzk();

    abstract Set zzl();

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbr
    public final Map zzp() {
        Map map = this.zzb;
        if (map != null) {
            return map;
        }
        Map zzk = zzk();
        this.zzb = zzk;
        return zzk;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbr
    public final Set zzq() {
        Set set = this.zza;
        if (set != null) {
            return set;
        }
        Set zzl = zzl();
        this.zza = zzl;
        return zzl;
    }
}
