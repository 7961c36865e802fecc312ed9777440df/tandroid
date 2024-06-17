package com.google.android.gms.internal.mlkit_vision_label;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Set;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public abstract class zzag extends zzai implements Serializable {
    private transient Map zza;
    private transient int zzb;

    public zzag(Map map) {
        if (map.isEmpty()) {
            this.zza = map;
            return;
        }
        throw new IllegalArgumentException();
    }

    public static /* synthetic */ int zzd(zzag zzagVar) {
        int i = zzagVar.zzb;
        zzagVar.zzb = i + 1;
        return i;
    }

    public static /* synthetic */ int zze(zzag zzagVar) {
        int i = zzagVar.zzb;
        zzagVar.zzb = i - 1;
        return i;
    }

    public static /* synthetic */ int zzf(zzag zzagVar, int i) {
        int i2 = zzagVar.zzb + i;
        zzagVar.zzb = i2;
        return i2;
    }

    public static /* synthetic */ int zzg(zzag zzagVar, int i) {
        int i2 = zzagVar.zzb - i;
        zzagVar.zzb = i2;
        return i2;
    }

    public static /* synthetic */ Map zzj(zzag zzagVar) {
        return zzagVar.zza;
    }

    public static /* synthetic */ void zzm(zzag zzagVar, Object obj) {
        Object obj2;
        Map map = zzagVar.zza;
        Objects.requireNonNull(map);
        try {
            obj2 = map.remove(obj);
        } catch (ClassCastException | NullPointerException unused) {
            obj2 = null;
        }
        Collection collection = (Collection) obj2;
        if (collection != null) {
            int size = collection.size();
            collection.clear();
            zzagVar.zzb -= size;
        }
    }

    public abstract Collection zza();

    public abstract Collection zzb(Object obj, Collection collection);

    public final Collection zzh(Object obj) {
        Collection collection = (Collection) this.zza.get(obj);
        if (collection == null) {
            collection = zza();
        }
        return zzb(obj, collection);
    }

    public final List zzi(Object obj, List list, zzad zzadVar) {
        if (list instanceof RandomAccess) {
            return new zzab(this, obj, list, zzadVar);
        }
        return new zzaf(this, obj, list, zzadVar);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzai
    final Map zzk() {
        return new zzy(this, this.zza);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzai
    final Set zzl() {
        return new zzaa(this, this.zza);
    }

    public final void zzn() {
        for (Collection collection : this.zza.values()) {
            collection.clear();
        }
        this.zza.clear();
        this.zzb = 0;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbr
    public final boolean zzo(Object obj, Object obj2) {
        Collection collection = (Collection) this.zza.get(obj);
        if (collection == null) {
            Collection zza = zza();
            if (zza.add(obj2)) {
                this.zzb++;
                this.zza.put(obj, zza);
                return true;
            }
            throw new AssertionError("New Collection violated the Collection spec");
        } else if (collection.add(obj2)) {
            this.zzb++;
            return true;
        } else {
            return false;
        }
    }
}