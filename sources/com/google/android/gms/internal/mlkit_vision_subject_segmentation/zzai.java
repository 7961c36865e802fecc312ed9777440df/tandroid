package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
final class zzai extends AbstractSet {
    final /* synthetic */ zzal zza;

    zzai(zzal zzalVar) {
        this.zza = zzalVar;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final void clear() {
        this.zza.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        return this.zza.containsKey(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator iterator() {
        zzal zzalVar = this.zza;
        Map zzj = zzalVar.zzj();
        return zzj != null ? zzj.keySet().iterator() : new zzac(zzalVar);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean remove(Object obj) {
        Object zzs;
        Object obj2;
        Map zzj = this.zza.zzj();
        if (zzj != null) {
            return zzj.keySet().remove(obj);
        }
        zzs = this.zza.zzs(obj);
        obj2 = zzal.zzd;
        return zzs != obj2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.zza.size();
    }
}
