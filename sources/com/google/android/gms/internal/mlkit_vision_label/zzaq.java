package com.google.android.gms.internal.mlkit_vision_label;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: classes.dex */
abstract class zzaq implements Iterator {
    int zzb;
    int zzc;
    int zzd;
    final /* synthetic */ zzau zze;

    /* synthetic */ zzaq(zzau zzauVar, zzam zzamVar) {
        int i;
        this.zze = zzauVar;
        i = zzauVar.zzf;
        this.zzb = i;
        this.zzc = zzauVar.zze();
        this.zzd = -1;
    }

    private final void zzb() {
        int i;
        i = this.zze.zzf;
        if (i != this.zzb) {
            throw new ConcurrentModificationException();
        }
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.zzc >= 0;
    }

    @Override // java.util.Iterator
    public final Object next() {
        zzb();
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        int i = this.zzc;
        this.zzd = i;
        Object zza = zza(i);
        this.zzc = this.zze.zzf(this.zzc);
        return zza;
    }

    @Override // java.util.Iterator
    public final void remove() {
        zzb();
        zzs.zzd(this.zzd >= 0, "no calls to next() since the last call to remove()");
        this.zzb += 32;
        zzau zzauVar = this.zze;
        zzauVar.remove(zzau.zzg(zzauVar, this.zzd));
        this.zzc--;
        this.zzd = -1;
    }

    abstract Object zza(int i);
}