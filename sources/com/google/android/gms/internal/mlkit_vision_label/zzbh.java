package com.google.android.gms.internal.mlkit_vision_label;

import java.util.Set;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public abstract class zzbh extends zzaz implements Set {
    private transient zzbe zza;

    @Override // java.util.Collection, java.util.Set
    public final boolean equals(Object obj) {
        if (obj == this || obj == this) {
            return true;
        }
        if (obj instanceof Set) {
            Set set = (Set) obj;
            try {
                if (size() == set.size()) {
                    if (containsAll(set)) {
                        return true;
                    }
                }
            } catch (ClassCastException | NullPointerException unused) {
            }
        }
        return false;
    }

    @Override // java.util.Collection, java.util.Set
    public final int hashCode() {
        return zzcb.zza(this);
    }

    public final zzbe zzf() {
        zzbe zzbeVar = this.zza;
        if (zzbeVar == null) {
            zzbe zzg = zzg();
            this.zza = zzg;
            return zzg;
        }
        return zzbeVar;
    }

    zzbe zzg() {
        return zzbe.zzg(toArray());
    }
}