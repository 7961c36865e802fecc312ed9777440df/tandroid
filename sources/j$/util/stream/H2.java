package j$.util.stream;

import j$.util.Optional;
import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
class H2 implements S2 {
    private boolean a;
    private Object b;
    final /* synthetic */ j$.util.function.b c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public H2(j$.util.function.b bVar) {
        this.c = bVar;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(double d) {
        o1.f(this);
        throw null;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(int i) {
        o1.d(this);
        throw null;
    }

    @Override // j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public /* synthetic */ void accept(long j) {
        o1.e(this);
        throw null;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        if (this.a) {
            this.a = false;
        } else {
            obj = this.c.apply(this.b, obj);
        }
        this.b = obj;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Objects.requireNonNull(consumer);
    }

    @Override // j$.util.function.y
    public Object get() {
        return this.a ? Optional.empty() : Optional.of(this.b);
    }

    @Override // j$.util.stream.S2
    public void h(S2 s2) {
        H2 h2 = (H2) s2;
        if (h2.a) {
            return;
        }
        accept(h2.b);
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        this.a = true;
        this.b = null;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ boolean o() {
        return false;
    }
}