package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
final class zzq extends zzbe {
    final /* synthetic */ zzw zza;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzq(zzw zzwVar, Map map) {
        super(map);
        this.zza = zzwVar;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final void clear() {
        zzaz.zza(iterator());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean containsAll(Collection collection) {
        return this.zzb.keySet().containsAll(collection);
    }

    @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
    public final boolean equals(Object obj) {
        return this == obj || this.zzb.keySet().equals(obj);
    }

    @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
    public final int hashCode() {
        return this.zzb.keySet().hashCode();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzbe, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator iterator() {
        return new zzp(this, this.zzb.entrySet().iterator());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean remove(Object obj) {
        int i;
        Collection collection = (Collection) this.zzb.remove(obj);
        if (collection == null) {
            return false;
        }
        int size = collection.size();
        collection.clear();
        zzw zzwVar = this.zza;
        i = zzwVar.zzb;
        zzwVar.zzb = i - size;
        return size > 0;
    }
}
