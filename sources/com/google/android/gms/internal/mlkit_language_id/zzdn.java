package com.google.android.gms.internal.mlkit_language_id;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: classes.dex */
public abstract class zzdn implements Serializable, Iterable {
    public static final zzdn zza = new zzdx(zzeq.zzb);
    private static final zzdt zzb;
    private static final Comparator zzd;
    private int zzc = 0;

    /* JADX WARN: Multi-variable type inference failed */
    static {
        zzdm zzdmVar = null;
        zzb = zzdl.zza() ? new zzdw(zzdmVar) : new zzdr(zzdmVar);
        zzd = new zzdp();
    }

    zzdn() {
    }

    public static zzdn zza(String str) {
        return new zzdx(str.getBytes(zzeq.zza));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int zzb(byte b) {
        return b & 255;
    }

    static int zzb(int i, int i2, int i3) {
        int i4 = i2 - i;
        if ((i | i2 | i4 | (i3 - i2)) >= 0) {
            return i4;
        }
        if (i < 0) {
            StringBuilder sb = new StringBuilder(32);
            sb.append("Beginning index: ");
            sb.append(i);
            sb.append(" < 0");
            throw new IndexOutOfBoundsException(sb.toString());
        }
        if (i2 < i) {
            StringBuilder sb2 = new StringBuilder(66);
            sb2.append("Beginning index larger than ending index: ");
            sb2.append(i);
            sb2.append(", ");
            sb2.append(i2);
            throw new IndexOutOfBoundsException(sb2.toString());
        }
        StringBuilder sb3 = new StringBuilder(37);
        sb3.append("End index: ");
        sb3.append(i2);
        sb3.append(" >= ");
        sb3.append(i3);
        throw new IndexOutOfBoundsException(sb3.toString());
    }

    public abstract boolean equals(Object obj);

    public final int hashCode() {
        int i = this.zzc;
        if (i == 0) {
            int zza2 = zza();
            i = zza(zza2, 0, zza2);
            if (i == 0) {
                i = 1;
            }
            this.zzc = i;
        }
        return i;
    }

    @Override // java.lang.Iterable
    public /* synthetic */ Iterator iterator() {
        return new zzdm(this);
    }

    public final String toString() {
        return String.format(Locale.ROOT, "<ByteString@%s size=%d contents=\"%s\">", Integer.toHexString(System.identityHashCode(this)), Integer.valueOf(zza()), zza() <= 50 ? zzhd.zza(this) : String.valueOf(zzhd.zza(zza(0, 47))).concat("..."));
    }

    public abstract byte zza(int i);

    public abstract int zza();

    protected abstract int zza(int i, int i2, int i3);

    public abstract zzdn zza(int i, int i2);

    protected abstract String zza(Charset charset);

    abstract void zza(zzdk zzdkVar);

    abstract byte zzb(int i);

    public final String zzb() {
        return zza() == 0 ? "" : zza(zzeq.zza);
    }

    public abstract boolean zzc();

    protected final int zzd() {
        return this.zzc;
    }
}
