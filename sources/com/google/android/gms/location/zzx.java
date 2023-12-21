package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.WorkSource;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.internal.location.zzd;
import org.telegram.messenger.R;
import org.telegram.tgnet.ConnectionsManager;
/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
/* loaded from: classes.dex */
public final class zzx implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        WorkSource workSource = new WorkSource();
        String str = null;
        zzd zzdVar = null;
        long j = Long.MAX_VALUE;
        long j2 = Long.MAX_VALUE;
        long j3 = 3600000;
        long j4 = 600000;
        long j5 = 0;
        long j6 = -1;
        int i = R.styleable.AppCompatTheme_textAppearanceLargePopupMenu;
        int i2 = ConnectionsManager.DEFAULT_DATACENTER_ID;
        float f = 0.0f;
        boolean z = false;
        int i3 = 0;
        int i4 = 0;
        boolean z2 = false;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 1:
                    i = SafeParcelReader.readInt(parcel, readHeader);
                    break;
                case 2:
                    j3 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 3:
                    j4 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 4:
                default:
                    SafeParcelReader.skipUnknownField(parcel, readHeader);
                    break;
                case 5:
                    j = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 6:
                    i2 = SafeParcelReader.readInt(parcel, readHeader);
                    break;
                case 7:
                    f = SafeParcelReader.readFloat(parcel, readHeader);
                    break;
                case 8:
                    j5 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 9:
                    z = SafeParcelReader.readBoolean(parcel, readHeader);
                    break;
                case 10:
                    j2 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 11:
                    j6 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 12:
                    i3 = SafeParcelReader.readInt(parcel, readHeader);
                    break;
                case 13:
                    i4 = SafeParcelReader.readInt(parcel, readHeader);
                    break;
                case 14:
                    str = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 15:
                    z2 = SafeParcelReader.readBoolean(parcel, readHeader);
                    break;
                case 16:
                    workSource = (WorkSource) SafeParcelReader.createParcelable(parcel, readHeader, WorkSource.CREATOR);
                    break;
                case 17:
                    zzdVar = (zzd) SafeParcelReader.createParcelable(parcel, readHeader, zzd.CREATOR);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new LocationRequest(i, j3, j4, j5, j, j2, i2, f, z, j6, i3, i4, str, z2, workSource, zzdVar);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i) {
        return new LocationRequest[i];
    }
}
