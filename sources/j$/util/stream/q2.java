package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class q2 extends s2 implements l3 {
    private final long[] h;

    q2(q2 q2Var, j$.util.t tVar, long j, long j2) {
        super(q2Var, tVar, j, j2, q2Var.h.length);
        this.h = q2Var.h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public q2(j$.util.t tVar, y2 y2Var, long[] jArr) {
        super(tVar, y2Var, jArr.length);
        this.h = jArr;
    }

    @Override // j$.util.stream.s2, j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public void accept(long j) {
        int i = this.f;
        if (i >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        long[] jArr = this.h;
        this.f = i + 1;
        jArr[i] = j;
    }

    @Override // j$.util.stream.s2
    s2 b(j$.util.t tVar, long j, long j2) {
        return new q2(this, tVar, j, j2);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: c */
    public /* synthetic */ void accept(Long l) {
        o1.c(this, l);
    }

    @Override // j$.util.function.q
    public j$.util.function.q f(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        return new j$.util.function.p(this, qVar);
    }
}
