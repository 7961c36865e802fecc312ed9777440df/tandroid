package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
class N1 extends f {
    protected final y2 h;
    protected final j$.util.function.r i;
    protected final j$.util.function.b j;

    N1(N1 n1, j$.util.t tVar) {
        super(n1, tVar);
        this.h = n1.h;
        this.i = n1.i;
        this.j = n1.j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public N1(y2 y2Var, j$.util.t tVar, j$.util.function.r rVar, j$.util.function.b bVar) {
        super(y2Var, tVar);
        this.h = y2Var;
        this.i = rVar;
        this.j = bVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public Object a() {
        s1 s1Var = (s1) this.i.apply(this.h.q0(this.b));
        this.h.u0(s1Var, this.b);
        return s1Var.a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public f f(j$.util.t tVar) {
        return new N1(this, tVar);
    }

    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter
    public void onCompletion(CountedCompleter countedCompleter) {
        if (!d()) {
            g((A1) this.j.apply((A1) ((N1) this.d).b(), (A1) ((N1) this.e).b()));
        }
        this.b = null;
        this.e = null;
        this.d = null;
    }
}
