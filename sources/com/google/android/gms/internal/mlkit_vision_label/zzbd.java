package com.google.android.gms.internal.mlkit_vision_label;

import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public final class zzbd extends zzbe {
    final transient int zza;
    final transient int zzb;
    final /* synthetic */ zzbe zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbd(zzbe zzbeVar, int i, int i2) {
        this.zzc = zzbeVar;
        this.zza = i;
        this.zzb = i2;
    }

    @Override // java.util.List
    public final Object get(int i) {
        zzs.zza(i, this.zzb, "index");
        return this.zzc.get(i + this.zza);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zzb;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbe, java.util.List
    public final /* bridge */ /* synthetic */ List subList(int i, int i2) {
        return subList(i, i2);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzaz
    final int zzb() {
        return this.zzc.zzc() + this.zza + this.zzb;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_label.zzaz
    public final int zzc() {
        return this.zzc.zzc() + this.zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_label.zzaz
    public final Object[] zze() {
        return this.zzc.zze();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbe
    public final zzbe zzf(int i, int i2) {
        zzs.zzc(i, i2, this.zzb);
        zzbe zzbeVar = this.zzc;
        int i3 = this.zza;
        return zzbeVar.subList(i + i3, i2 + i3);
    }
}