package com.google.android.gms.internal.mlkit_vision_label;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.dynamic.IObjectWrapper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class zzof extends zza implements IInterface {
    zzof(IBinder iBinder) {
        super(iBinder, "com.google.mlkit.vision.label.aidls.IImageLabeler");
    }

    public final List zzd(IObjectWrapper iObjectWrapper, zzod zzodVar) {
        Parcel zza = zza();
        zzc.zzb(zza, iObjectWrapper);
        zzc.zza(zza, zzodVar);
        Parcel zzb = zzb(3, zza);
        ArrayList createTypedArrayList = zzb.createTypedArrayList(zzoj.CREATOR);
        zzb.recycle();
        return createTypedArrayList;
    }

    public final void zze() {
        zzc(1, zza());
    }

    public final void zzf() {
        zzc(2, zza());
    }
}
