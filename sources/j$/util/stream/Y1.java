package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class Y1 extends a2 implements y1 {
    @Override // j$.util.stream.A1
    /* renamed from: a */
    public /* synthetic */ void i(Long[] lArr, int i) {
        o1.j(this, lArr, i);
    }

    @Override // j$.util.stream.a2, j$.util.stream.A1
    public z1 b(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.z1
    public Object e() {
        long[] jArr;
        jArr = x2.f;
        return jArr;
    }

    @Override // j$.util.stream.a2, j$.util.stream.A1
    /* renamed from: f */
    public /* synthetic */ y1 r(long j, long j2, j$.util.function.m mVar) {
        return o1.p(this, j, j2, mVar);
    }

    @Override // j$.util.stream.A1
    public /* synthetic */ void forEach(Consumer consumer) {
        o1.m(this, consumer);
    }

    @Override // j$.util.stream.A1
    public j$.util.w spliterator() {
        return j$.util.L.d();
    }

    @Override // j$.util.stream.a2, j$.util.stream.A1
    public /* bridge */ /* synthetic */ A1 b(int i) {
        b(i);
        throw null;
    }

    @Override // j$.util.stream.A1
    public j$.util.u spliterator() {
        return j$.util.L.d();
    }
}