package j$.wrappers;

import j$.util.Optional;
import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Consumer;
import j$.util.function.Function;
import j$.util.function.Predicate;
import j$.util.function.ToIntFunction;
import j$.util.stream.IntStream;
import j$.util.stream.Stream;
import j$.util.stream.e1;
import java.util.Comparator;
import java.util.Iterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class $r8$wrapper$java$util$stream$Stream$-V-WRP implements Stream {
    final /* synthetic */ java.util.stream.Stream a;

    private /* synthetic */ $r8$wrapper$java$util$stream$Stream$-V-WRP(java.util.stream.Stream stream) {
        this.a = stream;
    }

    public static /* synthetic */ Stream convert(java.util.stream.Stream stream) {
        if (stream == null) {
            return null;
        }
        return stream instanceof P0 ? ((P0) stream).a : new $r8$wrapper$java$util$stream$Stream$-V-WRP(stream);
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Object B(Object obj, BiFunction biFunction, j$.util.function.b bVar) {
        return this.a.reduce(obj, t.a(biFunction), v.a(bVar));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ j$.util.stream.U E(Function function) {
        return L0.n0(this.a.flatMapToDouble(N.a(function)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream T(Predicate predicate) {
        return convert(this.a.filter(y0.a(predicate)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream V(Consumer consumer) {
        return convert(this.a.peek(x.a(consumer)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ boolean W(Predicate predicate) {
        return this.a.allMatch(y0.a(predicate));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ e1 X(Function function) {
        return N0.n0(this.a.flatMapToLong(N.a(function)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ boolean a(Predicate predicate) {
        return this.a.anyMatch(y0.a(predicate));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Object b0(J0 j0) {
        return this.a.collect(j0 == null ? null : j0.a);
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ IntStream c(Function function) {
        return $r8$wrapper$java$util$stream$IntStream$-V-WRP.convert(this.a.flatMapToInt(N.a(function)));
    }

    @Override // j$.util.stream.Stream, j$.util.stream.g, java.lang.AutoCloseable
    public /* synthetic */ void close() {
        this.a.close();
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ long count() {
        return this.a.count();
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ boolean d0(Predicate predicate) {
        return this.a.noneMatch(y0.a(predicate));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream distinct() {
        return convert(this.a.distinct());
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ void e(Consumer consumer) {
        this.a.forEachOrdered(x.a(consumer));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Optional findAny() {
        return j$.util.a.p(this.a.findAny());
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Optional findFirst() {
        return j$.util.a.p(this.a.findFirst());
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ void forEach(Consumer consumer) {
        this.a.forEach(x.a(consumer));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ e1 g0(j$.util.function.A a) {
        return N0.n0(this.a.mapToLong(G0.a(a)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Object i(j$.util.function.y yVar, BiConsumer biConsumer, BiConsumer biConsumer2) {
        return this.a.collect(A0.a(yVar), r.a(biConsumer), r.a(biConsumer2));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // j$.util.stream.g
    public /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ j$.util.stream.U j0(j$.util.function.z zVar) {
        return L0.n0(this.a.mapToDouble(C0.a(zVar)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Object[] l(j$.util.function.m mVar) {
        return this.a.toArray(U.a(mVar));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream limit(long j) {
        return convert(this.a.limit(j));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ IntStream m(ToIntFunction toIntFunction) {
        return $r8$wrapper$java$util$stream$IntStream$-V-WRP.convert(this.a.mapToInt(E0.a(toIntFunction)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Object m0(Object obj, j$.util.function.b bVar) {
        return this.a.reduce(obj, v.a(bVar));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Optional max(Comparator comparator) {
        return j$.util.a.p(this.a.max(comparator));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Optional min(Comparator comparator) {
        return j$.util.a.p(this.a.min(comparator));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream n(Function function) {
        return convert(this.a.map(N.a(function)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream o(Function function) {
        return convert(this.a.flatMap(N.a(function)));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g onClose(Runnable runnable) {
        return H0.n0(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.g, j$.util.stream.IntStream
    public /* synthetic */ j$.util.stream.g parallel() {
        return H0.n0(this.a.parallel());
    }

    @Override // j$.util.stream.g, j$.util.stream.IntStream
    public /* synthetic */ j$.util.stream.g sequential() {
        return H0.n0(this.a.sequential());
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream skip(long j) {
        return convert(this.a.skip(j));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream sorted() {
        return convert(this.a.sorted());
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream sorted(Comparator comparator) {
        return convert(this.a.sorted(comparator));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.u spliterator() {
        return g.a(this.a.spliterator());
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Optional t(j$.util.function.b bVar) {
        return j$.util.a.p(this.a.reduce(v.a(bVar)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Object[] toArray() {
        return this.a.toArray();
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g unordered() {
        return H0.n0(this.a.unordered());
    }
}