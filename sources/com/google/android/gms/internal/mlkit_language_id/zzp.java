package com.google.android.gms.internal.mlkit_language_id;

/* loaded from: classes.dex */
final class zzp extends zzk {
    static final zzk zza = new zzp(new Object[0], 0);
    private final transient Object[] zzb;
    private final transient int zzc;

    zzp(Object[] objArr, int i) {
        this.zzb = objArr;
        this.zzc = i;
    }

    @Override // java.util.List
    public final Object get(int i) {
        zzg.zza(i, this.zzc);
        return this.zzb[i];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzk, com.google.android.gms.internal.mlkit_language_id.zzl
    final int zza(Object[] objArr, int i) {
        System.arraycopy(this.zzb, 0, objArr, 0, this.zzc);
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzl
    final Object[] zzb() {
        return this.zzb;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzl
    final int zzc() {
        return 0;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzl
    final int zzd() {
        return this.zzc;
    }
}
