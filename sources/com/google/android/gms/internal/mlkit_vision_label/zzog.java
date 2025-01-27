package com.google.android.gms.internal.mlkit_vision_label;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.dynamic.IObjectWrapper;

/* loaded from: classes.dex */
public final class zzog extends zza implements zzoi {
    zzog(IBinder iBinder) {
        super(iBinder, "com.google.mlkit.vision.label.aidls.IImageLabelerCreator");
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzoi
    public final zzof zzd(IObjectWrapper iObjectWrapper, zzol zzolVar) {
        zzof zzofVar;
        Parcel zza = zza();
        zzc.zzb(zza, iObjectWrapper);
        zzc.zza(zza, zzolVar);
        Parcel zzb = zzb(1, zza);
        IBinder readStrongBinder = zzb.readStrongBinder();
        if (readStrongBinder == null) {
            zzofVar = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.mlkit.vision.label.aidls.IImageLabeler");
            zzofVar = queryLocalInterface instanceof zzof ? (zzof) queryLocalInterface : new zzof(readStrongBinder);
        }
        zzb.recycle();
        return zzofVar;
    }
}
