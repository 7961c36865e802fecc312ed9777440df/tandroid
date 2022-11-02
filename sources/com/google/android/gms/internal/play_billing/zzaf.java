package com.google.android.gms.internal.play_billing;

import java.util.Arrays;
import javax.annotation.CheckForNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes.dex */
public final class zzaf extends zzx {
    static final zzx zza = new zzaf(null, new Object[0], 0);
    final transient Object[] zzb;
    @CheckForNull
    private final transient Object zzc;
    private final transient int zzd;

    private zzaf(@CheckForNull Object obj, Object[] objArr, int i) {
        this.zzc = obj;
        this.zzb = objArr;
        this.zzd = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r15v0 */
    /* JADX WARN: Type inference failed for: r5v11 */
    /* JADX WARN: Type inference failed for: r5v13, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r5v4, types: [int[]] */
    public static zzaf zzf(int i, Object[] objArr, zzw zzwVar) {
        short[] sArr;
        Object[] objArr2;
        byte[] bArr;
        int i2 = i;
        Object[] objArr3 = objArr;
        if (i2 == 0) {
            return (zzaf) zza;
        }
        Object obj = null;
        if (i2 == 1) {
            Object obj2 = objArr3[0];
            obj2.getClass();
            Object obj3 = objArr3[1];
            obj3.getClass();
            zzp.zza(obj2, obj3);
            return new zzaf(null, objArr3, 1);
        }
        zzm.zzb(i2, objArr3.length >> 1, "index");
        char c = 2;
        int max = Math.max(i2, 2);
        int i3 = 1073741824;
        if (max < 751619276) {
            int highestOneBit = Integer.highestOneBit(max - 1);
            i3 = highestOneBit + highestOneBit;
            while (true) {
                double d = i3;
                Double.isNaN(d);
                if (d * 0.7d >= max) {
                    break;
                }
                i3 += i3;
            }
        } else if (max >= 1073741824) {
            throw new IllegalArgumentException("collection too large");
        }
        if (i2 == 1) {
            Object obj4 = objArr3[0];
            obj4.getClass();
            Object obj5 = objArr3[1];
            obj5.getClass();
            zzp.zza(obj4, obj5);
        } else {
            int i4 = i3 - 1;
            char c2 = 65535;
            if (i3 <= 128) {
                byte[] bArr2 = new byte[i3];
                Arrays.fill(bArr2, (byte) -1);
                int i5 = 0;
                for (int i6 = 0; i6 < i2; i6++) {
                    int i7 = i6 + i6;
                    int i8 = i5 + i5;
                    Object obj6 = objArr3[i7];
                    obj6.getClass();
                    Object obj7 = objArr3[i7 ^ 1];
                    obj7.getClass();
                    zzp.zza(obj6, obj7);
                    int zza2 = zzq.zza(obj6.hashCode());
                    while (true) {
                        int i9 = zza2 & i4;
                        int i10 = bArr2[i9] & 255;
                        if (i10 != 255) {
                            if (obj6.equals(objArr3[i10])) {
                                int i11 = i10 ^ 1;
                                Object obj8 = objArr3[i11];
                                obj8.getClass();
                                zzv zzvVar = new zzv(obj6, obj7, obj8);
                                objArr3[i11] = obj7;
                                obj = zzvVar;
                                break;
                            }
                            zza2 = i9 + 1;
                        } else {
                            bArr2[i9] = (byte) i8;
                            if (i5 < i6) {
                                objArr3[i8] = obj6;
                                objArr3[i8 ^ 1] = obj7;
                            }
                            i5++;
                        }
                    }
                }
                if (i5 == i2) {
                    bArr = bArr2;
                    c = 2;
                    obj = bArr;
                } else {
                    sArr = new Object[]{bArr2, Integer.valueOf(i5), obj};
                }
            } else if (i3 <= 32768) {
                sArr = new short[i3];
                Arrays.fill(sArr, (short) -1);
                int i12 = 0;
                for (int i13 = 0; i13 < i2; i13++) {
                    int i14 = i13 + i13;
                    int i15 = i12 + i12;
                    Object obj9 = objArr3[i14];
                    obj9.getClass();
                    Object obj10 = objArr3[i14 ^ 1];
                    obj10.getClass();
                    zzp.zza(obj9, obj10);
                    int zza3 = zzq.zza(obj9.hashCode());
                    while (true) {
                        int i16 = zza3 & i4;
                        char c3 = (char) sArr[i16];
                        if (c3 != 65535) {
                            if (obj9.equals(objArr3[c3])) {
                                int i17 = c3 ^ 1;
                                Object obj11 = objArr3[i17];
                                obj11.getClass();
                                zzv zzvVar2 = new zzv(obj9, obj10, obj11);
                                objArr3[i17] = obj10;
                                obj = zzvVar2;
                                break;
                            }
                            zza3 = i16 + 1;
                        } else {
                            sArr[i16] = (short) i15;
                            if (i12 < i13) {
                                objArr3[i15] = obj9;
                                objArr3[i15 ^ 1] = obj10;
                            }
                            i12++;
                        }
                    }
                }
                if (i12 != i2) {
                    c = 2;
                    objArr2 = new Object[]{sArr, Integer.valueOf(i12), obj};
                    obj = objArr2;
                }
            } else {
                sArr = new int[i3];
                Arrays.fill((int[]) sArr, -1);
                int i18 = 0;
                int i19 = 0;
                while (i18 < i2) {
                    int i20 = i18 + i18;
                    int i21 = i19 + i19;
                    Object obj12 = objArr3[i20];
                    obj12.getClass();
                    Object obj13 = objArr3[i20 ^ 1];
                    obj13.getClass();
                    zzp.zza(obj12, obj13);
                    int zza4 = zzq.zza(obj12.hashCode());
                    while (true) {
                        int i22 = zza4 & i4;
                        ?? r15 = sArr[i22];
                        if (r15 != c2) {
                            if (obj12.equals(objArr3[r15])) {
                                int i23 = r15 ^ 1;
                                Object obj14 = objArr3[i23];
                                obj14.getClass();
                                zzv zzvVar3 = new zzv(obj12, obj13, obj14);
                                objArr3[i23] = obj13;
                                obj = zzvVar3;
                                break;
                            }
                            zza4 = i22 + 1;
                            c2 = 65535;
                        } else {
                            sArr[i22] = i21;
                            if (i19 < i18) {
                                objArr3[i21] = obj12;
                                objArr3[i21 ^ 1] = obj13;
                            }
                            i19++;
                        }
                    }
                    i18++;
                    c2 = 65535;
                }
                if (i19 != i2) {
                    c = 2;
                    objArr2 = new Object[]{sArr, Integer.valueOf(i19), obj};
                    obj = objArr2;
                }
            }
            bArr = sArr;
            c = 2;
            obj = bArr;
        }
        boolean z = obj instanceof Object[];
        Object obj15 = obj;
        if (z) {
            Object[] objArr4 = (Object[]) obj;
            zzwVar.zzc = (zzv) objArr4[c];
            Object obj16 = objArr4[0];
            int intValue = ((Integer) objArr4[1]).intValue();
            objArr3 = Arrays.copyOf(objArr3, intValue + intValue);
            obj15 = obj16;
            i2 = intValue;
        }
        return new zzaf(obj15, objArr3, i2);
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x009e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x009f A[RETURN] */
    @Override // com.google.android.gms.internal.play_billing.zzx, java.util.Map, j$.util.Map
    @CheckForNull
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object get(@CheckForNull Object obj) {
        Object obj2;
        Object obj3 = this.zzc;
        Object[] objArr = this.zzb;
        int i = this.zzd;
        if (obj != null) {
            if (i == 1) {
                Object obj4 = objArr[0];
                obj4.getClass();
                if (obj4.equals(obj)) {
                    obj2 = objArr[1];
                    obj2.getClass();
                }
            } else if (obj3 != null) {
                if (obj3 instanceof byte[]) {
                    byte[] bArr = (byte[]) obj3;
                    int length = bArr.length - 1;
                    int zza2 = zzq.zza(obj.hashCode());
                    while (true) {
                        int i2 = zza2 & length;
                        int i3 = bArr[i2] & 255;
                        if (i3 == 255) {
                            break;
                        } else if (obj.equals(objArr[i3])) {
                            obj2 = objArr[i3 ^ 1];
                            break;
                        } else {
                            zza2 = i2 + 1;
                        }
                    }
                } else if (obj3 instanceof short[]) {
                    short[] sArr = (short[]) obj3;
                    int length2 = sArr.length - 1;
                    int zza3 = zzq.zza(obj.hashCode());
                    while (true) {
                        int i4 = zza3 & length2;
                        char c = (char) sArr[i4];
                        if (c == 65535) {
                            break;
                        } else if (obj.equals(objArr[c])) {
                            obj2 = objArr[c ^ 1];
                            break;
                        } else {
                            zza3 = i4 + 1;
                        }
                    }
                } else {
                    int[] iArr = (int[]) obj3;
                    int length3 = iArr.length - 1;
                    int zza4 = zzq.zza(obj.hashCode());
                    while (true) {
                        int i5 = zza4 & length3;
                        int i6 = iArr[i5];
                        if (i6 == -1) {
                            break;
                        } else if (obj.equals(objArr[i6])) {
                            obj2 = objArr[i6 ^ 1];
                            break;
                        } else {
                            zza4 = i5 + 1;
                        }
                    }
                }
            }
            if (obj2 == null) {
                return obj2;
            }
            return null;
        }
        obj2 = null;
        if (obj2 == null) {
        }
    }

    @Override // java.util.Map, j$.util.Map
    public final int size() {
        return this.zzd;
    }

    @Override // com.google.android.gms.internal.play_billing.zzx
    final zzr zza() {
        return new zzae(this.zzb, 1, this.zzd);
    }

    @Override // com.google.android.gms.internal.play_billing.zzx
    final zzy zzc() {
        return new zzac(this, this.zzb, 0, this.zzd);
    }

    @Override // com.google.android.gms.internal.play_billing.zzx
    final zzy zzd() {
        return new zzad(this, new zzae(this.zzb, 0, this.zzd));
    }
}