package j$.wrappers;

import j$.util.function.Consumer;
import java.util.Comparator;
import java.util.Spliterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class m implements j$.util.v {
    final /* synthetic */ Spliterator.OfLong a;

    private /* synthetic */ m(Spliterator.OfLong ofLong) {
        this.a = ofLong;
    }

    public static /* synthetic */ j$.util.v a(Spliterator.OfLong ofLong) {
        if (ofLong == null) {
            return null;
        }
        return ofLong instanceof n ? ((n) ofLong).a : new m(ofLong);
    }

    @Override // j$.util.v, j$.util.u
    public /* synthetic */ boolean b(Consumer consumer) {
        return this.a.tryAdvance(x.a(consumer));
    }

    @Override // j$.util.u
    public /* synthetic */ int characteristics() {
        return this.a.characteristics();
    }

    @Override // j$.util.v
    public /* synthetic */ void d(j$.util.function.q qVar) {
        this.a.forEachRemaining(g0.a(qVar));
    }

    @Override // j$.util.u
    public /* synthetic */ long estimateSize() {
        return this.a.estimateSize();
    }

    @Override // j$.util.v, j$.util.u
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining(x.a(consumer));
    }

    @Override // j$.util.w
    public /* synthetic */ void forEachRemaining(Object obj) {
        this.a.forEachRemaining((Spliterator.OfLong) obj);
    }

    @Override // j$.util.u
    public /* synthetic */ Comparator getComparator() {
        return this.a.getComparator();
    }

    @Override // j$.util.u
    public /* synthetic */ long getExactSizeIfKnown() {
        return this.a.getExactSizeIfKnown();
    }

    @Override // j$.util.u
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return this.a.hasCharacteristics(i);
    }

    @Override // j$.util.v
    public /* synthetic */ boolean i(j$.util.function.q qVar) {
        return this.a.tryAdvance(g0.a(qVar));
    }

    @Override // j$.util.w
    public /* synthetic */ boolean tryAdvance(Object obj) {
        return this.a.tryAdvance((Spliterator.OfLong) obj);
    }

    @Override // j$.util.v, j$.util.w, j$.util.u
    public /* synthetic */ j$.util.u trySplit() {
        return g.a(this.a.trySplit());
    }

    @Override // j$.util.v, j$.util.w, j$.util.u
    public /* synthetic */ j$.util.v trySplit() {
        return a(this.a.trySplit());
    }

    @Override // j$.util.v, j$.util.w, j$.util.u
    public /* synthetic */ j$.util.w trySplit() {
        return o.a(this.a.trySplit());
    }
}