package j$.util.stream;

import java.util.Arrays;

/* loaded from: classes2.dex */
final class v2 extends r2 {
    private L2 c;

    v2(e2 e2Var) {
        super(e2Var);
    }

    @Override // j$.util.stream.e2
    public final void accept(long j) {
        this.c.accept(j);
    }

    @Override // j$.util.stream.Z1, j$.util.stream.e2
    public final void m() {
        long[] jArr = (long[]) this.c.e();
        Arrays.sort(jArr);
        long length = jArr.length;
        e2 e2Var = this.a;
        e2Var.n(length);
        int i = 0;
        if (this.b) {
            int length2 = jArr.length;
            while (i < length2) {
                long j = jArr[i];
                if (e2Var.q()) {
                    break;
                }
                e2Var.accept(j);
                i++;
            }
        } else {
            int length3 = jArr.length;
            while (i < length3) {
                e2Var.accept(jArr[i]);
                i++;
            }
        }
        e2Var.m();
    }

    @Override // j$.util.stream.Z1, j$.util.stream.e2
    public final void n(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.c = j > 0 ? new L2((int) j) : new L2();
    }
}
