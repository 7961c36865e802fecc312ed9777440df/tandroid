package com.google.android.gms.internal.mlkit_vision_label_common;

import java.util.Arrays;
import java.util.Objects;
/* compiled from: com.google.mlkit:image-labeling-common@@18.1.0 */
/* loaded from: classes.dex */
public final class zzd {
    private final String zza;
    private final zzc zzb;
    private zzc zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzd(String str, zza zzaVar) {
        zzc zzcVar = new zzc(null);
        this.zzb = zzcVar;
        this.zzc = zzcVar;
        Objects.requireNonNull(str);
        this.zza = str;
    }

    private final zzd zzd(String str, Object obj) {
        zzb zzbVar = new zzb(null);
        this.zzc.zzc = zzbVar;
        this.zzc = zzbVar;
        zzbVar.zzb = obj;
        zzbVar.zza = str;
        return this;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append(this.zza);
        sb.append('{');
        zzc zzcVar = this.zzb.zzc;
        String str = "";
        while (zzcVar != null) {
            Object obj = zzcVar.zzb;
            sb.append(str);
            String str2 = zzcVar.zza;
            if (str2 != null) {
                sb.append(str2);
                sb.append('=');
            }
            if (obj == null || !obj.getClass().isArray()) {
                sb.append(obj);
            } else {
                String deepToString = Arrays.deepToString(new Object[]{obj});
                sb.append((CharSequence) deepToString, 1, deepToString.length() - 1);
            }
            zzcVar = zzcVar.zzc;
            str = ", ";
        }
        sb.append('}');
        return sb.toString();
    }

    public final zzd zza(String str, float f) {
        zzd("confidence", String.valueOf(f));
        return this;
    }

    public final zzd zzb(String str, int i) {
        zzd("index", String.valueOf(i));
        return this;
    }

    public final zzd zzc(String str, Object obj) {
        zzc zzcVar = new zzc(null);
        this.zzc.zzc = zzcVar;
        this.zzc = zzcVar;
        zzcVar.zzb = obj;
        zzcVar.zza = str;
        return this;
    }
}
