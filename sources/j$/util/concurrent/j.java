package j$.util.concurrent;

import j$.util.Collection$-EL;
import j$.util.P;
import j$.util.Q;
import j$.util.function.Consumer;
import j$.util.function.I0;
import j$.util.function.L;
import j$.util.function.Predicate;
import j$.util.stream.Q2;
import j$.util.stream.u0;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.IntFunction;
import java.util.stream.Stream;
/* loaded from: classes2.dex */
public final class j extends c implements Set, j$.util.b {
    /* JADX INFO: Access modifiers changed from: package-private */
    public j(ConcurrentHashMap concurrentHashMap) {
        super(concurrentHashMap);
    }

    @Override // j$.util.b
    public final /* synthetic */ boolean a(Predicate predicate) {
        return j$.util.a.l(this, predicate);
    }

    @Override // java.util.Collection, java.util.Set
    public final boolean add(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection, java.util.Set
    public final boolean addAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    @Override // j$.util.concurrent.c, java.util.Collection
    public final boolean contains(Object obj) {
        return this.a.containsKey(obj);
    }

    @Override // java.util.Collection, java.util.Set
    public final boolean equals(Object obj) {
        Set set;
        return (obj instanceof Set) && ((set = (Set) obj) == this || (containsAll(set) && set.containsAll(this)));
    }

    @Override // j$.util.b
    public final void forEach(Consumer consumer) {
        consumer.getClass();
        m[] mVarArr = this.a.a;
        if (mVarArr == null) {
            return;
        }
        q qVar = new q(mVarArr, mVarArr.length, 0, mVarArr.length);
        while (true) {
            m f = qVar.f();
            if (f == null) {
                return;
            }
            consumer.accept(f.b);
        }
    }

    @Override // java.lang.Iterable
    public final /* synthetic */ void forEach(java.util.function.Consumer consumer) {
        forEach(Consumer.VivifiedWrapper.convert(consumer));
    }

    @Override // java.util.Collection, java.util.Set
    public final int hashCode() {
        Iterator it = iterator();
        int i = 0;
        while (((b) it).hasNext()) {
            i += ((i) it).next().hashCode();
        }
        return i;
    }

    @Override // j$.util.concurrent.c, java.util.Collection, java.lang.Iterable
    public final Iterator iterator() {
        ConcurrentHashMap concurrentHashMap = this.a;
        m[] mVarArr = concurrentHashMap.a;
        int length = mVarArr == null ? 0 : mVarArr.length;
        return new i(mVarArr, length, length, concurrentHashMap, 0);
    }

    @Override // java.util.Collection
    public final Stream parallelStream() {
        return Q2.i0(u0.W0(Collection$-EL.b(this), true));
    }

    @Override // java.util.Collection, java.util.Set
    public final boolean remove(Object obj) {
        return this.a.remove(obj) != null;
    }

    @Override // java.util.Collection
    public final /* synthetic */ boolean removeIf(java.util.function.Predicate predicate) {
        return j$.util.a.l(this, I0.a(predicate));
    }

    @Override // java.util.Collection, java.lang.Iterable, java.util.Set, j$.util.b
    public final Q spliterator() {
        ConcurrentHashMap concurrentHashMap = this.a;
        long l = concurrentHashMap.l();
        m[] mVarArr = concurrentHashMap.a;
        int length = mVarArr == null ? 0 : mVarArr.length;
        return new k(mVarArr, length, 0, length, l >= 0 ? l : 0L, 0);
    }

    @Override // java.util.Collection, java.lang.Iterable, java.util.Set
    public final /* synthetic */ Spliterator spliterator() {
        return P.a(spliterator());
    }

    @Override // java.util.Collection, j$.util.b
    public final /* synthetic */ j$.util.stream.Stream stream() {
        return j$.util.a.m(this);
    }

    @Override // java.util.Collection
    public final /* synthetic */ Stream stream() {
        return Q2.i0(j$.util.a.m(this));
    }

    public final Object[] toArray(IntFunction intFunction) {
        return toArray((Object[]) L.a(intFunction).apply(0));
    }
}