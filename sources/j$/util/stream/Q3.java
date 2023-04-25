package j$.util.stream;

import java.util.Arrays;
/* loaded from: classes2.dex */
final class Q3 extends E3 {
    private long[] c;
    private int d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Q3(m3 m3Var) {
        super(m3Var);
    }

    @Override // j$.util.stream.l3, j$.util.function.q
    public void accept(long j) {
        long[] jArr = this.c;
        int i = this.d;
        this.d = i + 1;
        jArr[i] = j;
    }

    @Override // j$.util.stream.h3, j$.util.stream.m3
    public void m() {
        int i = 0;
        Arrays.sort(this.c, 0, this.d);
        this.a.n(this.d);
        if (this.b) {
            while (i < this.d && !this.a.o()) {
                this.a.accept(this.c[i]);
                i++;
            }
        } else {
            while (i < this.d) {
                this.a.accept(this.c[i]);
                i++;
            }
        }
        this.a.m();
        this.c = null;
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.c = new long[(int) j];
    }
}