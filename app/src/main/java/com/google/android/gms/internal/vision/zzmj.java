package com.google.android.gms.internal.vision;

import com.google.android.exoplayer2.extractor.ts.PsExtractor;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes3.dex */
final class zzmj extends zzme {
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0063, code lost:
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00be, code lost:
        return -1;
     */
    @Override // com.google.android.gms.internal.vision.zzme
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    final int zza(int i, byte[] bArr, int i2, int i3) {
        int i4;
        if ((i2 | i3 | (bArr.length - i3)) < 0) {
            throw new ArrayIndexOutOfBoundsException(String.format("Array length=%d, index=%d, limit=%d", Integer.valueOf(bArr.length), Integer.valueOf(i2), Integer.valueOf(i3)));
        }
        long j = i2;
        int i5 = (int) (i3 - j);
        if (i5 < 16) {
            i4 = 0;
        } else {
            long j2 = j;
            i4 = 0;
            while (true) {
                if (i4 < i5) {
                    long j3 = j2 + 1;
                    if (zzma.zza(bArr, j2) < 0) {
                        break;
                    }
                    i4++;
                    j2 = j3;
                } else {
                    i4 = i5;
                    break;
                }
            }
        }
        int i6 = i5 - i4;
        long j4 = j + i4;
        while (true) {
            byte b = 0;
            while (true) {
                if (i6 <= 0) {
                    break;
                }
                long j5 = j4 + 1;
                b = zzma.zza(bArr, j4);
                if (b < 0) {
                    j4 = j5;
                    break;
                }
                i6--;
                j4 = j5;
            }
            if (i6 != 0) {
                int i7 = i6 - 1;
                if (b < -32) {
                    if (i7 == 0) {
                        return b;
                    }
                    i6 = i7 - 1;
                    if (b < -62) {
                        break;
                    }
                    long j6 = j4 + 1;
                    if (zzma.zza(bArr, j4) > -65) {
                        break;
                    }
                    j4 = j6;
                } else if (b < -16) {
                    if (i7 < 2) {
                        return zza(bArr, b, j4, i7);
                    }
                    i6 = i7 - 2;
                    long j7 = j4 + 1;
                    byte zza = zzma.zza(bArr, j4);
                    if (zza > -65 || ((b == -32 && zza < -96) || (b == -19 && zza >= -96))) {
                        break;
                    }
                    j4 = j7 + 1;
                    if (zzma.zza(bArr, j7) > -65) {
                        break;
                    }
                } else if (i7 < 3) {
                    return zza(bArr, b, j4, i7);
                } else {
                    i6 = i7 - 3;
                    long j8 = j4 + 1;
                    byte zza2 = zzma.zza(bArr, j4);
                    if (zza2 > -65 || (((b << 28) + (zza2 + 112)) >> 30) != 0) {
                        break;
                    }
                    long j9 = j8 + 1;
                    if (zzma.zza(bArr, j8) > -65) {
                        break;
                    }
                    long j10 = j9 + 1;
                    if (zzma.zza(bArr, j9) > -65) {
                        break;
                    }
                    j4 = j10;
                }
            } else {
                return 0;
            }
        }
        return -1;
    }

    @Override // com.google.android.gms.internal.vision.zzme
    public final String zzb(byte[] bArr, int i, int i2) throws zzjk {
        boolean zzd;
        boolean zzd2;
        boolean zze;
        boolean zzf;
        boolean zzd3;
        if ((i | i2 | ((bArr.length - i) - i2)) < 0) {
            throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", Integer.valueOf(bArr.length), Integer.valueOf(i), Integer.valueOf(i2)));
        }
        int i3 = i + i2;
        char[] cArr = new char[i2];
        int i4 = 0;
        while (i < i3) {
            byte zza = zzma.zza(bArr, i);
            zzd3 = zzmf.zzd(zza);
            if (!zzd3) {
                break;
            }
            i++;
            zzmf.zzb(zza, cArr, i4);
            i4++;
        }
        int i5 = i4;
        while (i < i3) {
            int i6 = i + 1;
            byte zza2 = zzma.zza(bArr, i);
            zzd = zzmf.zzd(zza2);
            if (!zzd) {
                zze = zzmf.zze(zza2);
                if (!zze) {
                    zzf = zzmf.zzf(zza2);
                    if (zzf) {
                        if (i6 >= i3 - 1) {
                            throw zzjk.zzh();
                        }
                        int i7 = i6 + 1;
                        zzmf.zzb(zza2, zzma.zza(bArr, i6), zzma.zza(bArr, i7), cArr, i5);
                        i = i7 + 1;
                        i5++;
                    } else if (i6 >= i3 - 2) {
                        throw zzjk.zzh();
                    } else {
                        int i8 = i6 + 1;
                        int i9 = i8 + 1;
                        zzmf.zzb(zza2, zzma.zza(bArr, i6), zzma.zza(bArr, i8), zzma.zza(bArr, i9), cArr, i5);
                        i = i9 + 1;
                        i5 = i5 + 1 + 1;
                    }
                } else if (i6 >= i3) {
                    throw zzjk.zzh();
                } else {
                    zzmf.zzb(zza2, zzma.zza(bArr, i6), cArr, i5);
                    i = i6 + 1;
                    i5++;
                }
            } else {
                int i10 = i5 + 1;
                zzmf.zzb(zza2, cArr, i5);
                while (i6 < i3) {
                    byte zza3 = zzma.zza(bArr, i6);
                    zzd2 = zzmf.zzd(zza3);
                    if (!zzd2) {
                        break;
                    }
                    i6++;
                    zzmf.zzb(zza3, cArr, i10);
                    i10++;
                }
                i = i6;
                i5 = i10;
            }
        }
        return new String(cArr, 0, i5);
    }

    @Override // com.google.android.gms.internal.vision.zzme
    public final int zza(CharSequence charSequence, byte[] bArr, int i, int i2) {
        char c;
        long j;
        long j2;
        long j3;
        int i3;
        char charAt;
        long j4 = i;
        long j5 = i2 + j4;
        int length = charSequence.length();
        if (length > i2 || bArr.length - i2 < i) {
            char charAt2 = charSequence.charAt(length - 1);
            StringBuilder sb = new StringBuilder(37);
            sb.append("Failed writing ");
            sb.append(charAt2);
            sb.append(" at index ");
            sb.append(i + i2);
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        }
        int i4 = 0;
        while (true) {
            c = 128;
            j = 1;
            if (i4 >= length || (charAt = charSequence.charAt(i4)) >= 128) {
                break;
            }
            zzma.zza(bArr, j4, (byte) charAt);
            i4++;
            j4 = 1 + j4;
        }
        if (i4 == length) {
            return (int) j4;
        }
        while (i4 < length) {
            char charAt3 = charSequence.charAt(i4);
            if (charAt3 < c && j4 < j5) {
                long j6 = j4 + j;
                zzma.zza(bArr, j4, (byte) charAt3);
                j3 = j;
                j2 = j6;
            } else if (charAt3 < 2048 && j4 <= j5 - 2) {
                long j7 = j4 + j;
                zzma.zza(bArr, j4, (byte) ((charAt3 >>> 6) | 960));
                zzma.zza(bArr, j7, (byte) ((charAt3 & '?') | 128));
                j2 = j7 + j;
                j3 = j;
            } else if ((charAt3 < 55296 || 57343 < charAt3) && j4 <= j5 - 3) {
                long j8 = j4 + j;
                zzma.zza(bArr, j4, (byte) ((charAt3 >>> '\f') | 480));
                long j9 = j8 + j;
                zzma.zza(bArr, j8, (byte) (((charAt3 >>> 6) & 63) | 128));
                zzma.zza(bArr, j9, (byte) ((charAt3 & '?') | 128));
                j2 = j9 + 1;
                j3 = 1;
            } else if (j4 <= j5 - 4) {
                int i5 = i4 + 1;
                if (i5 != length) {
                    char charAt4 = charSequence.charAt(i5);
                    if (!Character.isSurrogatePair(charAt3, charAt4)) {
                        i4 = i5;
                    } else {
                        int codePoint = Character.toCodePoint(charAt3, charAt4);
                        long j10 = j4 + 1;
                        zzma.zza(bArr, j4, (byte) ((codePoint >>> 18) | PsExtractor.VIDEO_STREAM_MASK));
                        long j11 = j10 + 1;
                        zzma.zza(bArr, j10, (byte) (((codePoint >>> 12) & 63) | 128));
                        long j12 = j11 + 1;
                        zzma.zza(bArr, j11, (byte) (((codePoint >>> 6) & 63) | 128));
                        j3 = 1;
                        j2 = j12 + 1;
                        zzma.zza(bArr, j12, (byte) ((codePoint & 63) | 128));
                        i4 = i5;
                    }
                }
                throw new zzmg(i4 - 1, length);
            } else if (55296 > charAt3 || charAt3 > 57343 || ((i3 = i4 + 1) != length && Character.isSurrogatePair(charAt3, charSequence.charAt(i3)))) {
                StringBuilder sb2 = new StringBuilder(46);
                sb2.append("Failed writing ");
                sb2.append(charAt3);
                sb2.append(" at index ");
                sb2.append(j4);
                throw new ArrayIndexOutOfBoundsException(sb2.toString());
            } else {
                throw new zzmg(i4, length);
            }
            i4++;
            c = 128;
            long j13 = j3;
            j4 = j2;
            j = j13;
        }
        return (int) j4;
    }

    private static int zza(byte[] bArr, int i, long j, int i2) {
        int zzb;
        int zzb2;
        int zzb3;
        switch (i2) {
            case 0:
                zzb = zzmd.zzb(i);
                return zzb;
            case 1:
                zzb2 = zzmd.zzb(i, zzma.zza(bArr, j));
                return zzb2;
            case 2:
                zzb3 = zzmd.zzb(i, zzma.zza(bArr, j), zzma.zza(bArr, j + 1));
                return zzb3;
            default:
                throw new AssertionError();
        }
    }
}
