package com.google.android.gms.internal.mlkit_vision_common;

/* loaded from: classes.dex */
final class zzu extends zzp {
    static final zzp zza = new zzu(new Object[0], 0);
    final transient Object[] zzb;
    private final transient int zzc;

    zzu(Object[] objArr, int i) {
        this.zzb = objArr;
        this.zzc = i;
    }

    @Override // java.util.List
    public final Object get(int i) {
        zzf.zza(i, this.zzc, "index");
        Object obj = this.zzb[i];
        obj.getClass();
        return obj;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzp, com.google.android.gms.internal.mlkit_vision_common.zzl
    final int zza(Object[] objArr, int i) {
        System.arraycopy(this.zzb, 0, objArr, 0, this.zzc);
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl
    final int zzb() {
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl
    final int zzc() {
        return 0;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl
    final Object[] zze() {
        return this.zzb;
    }
}