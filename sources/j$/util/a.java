package j$.util;

import j$.util.Map;
import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Consumer;
import j$.util.function.Function;
import j$.util.function.Predicate;
import j$.util.stream.Stream;
import j$.util.stream.o1;
import j$.util.u;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.ConcurrentMap;
/* loaded from: classes2.dex */
public abstract /* synthetic */ class a {
    public static Object A(java.util.Map map, Object obj, Object obj2, BiFunction biFunction) {
        if (map instanceof Map) {
            return ((Map) map).merge(obj, obj2, biFunction);
        }
        if (map instanceof ConcurrentMap) {
            ConcurrentMap concurrentMap = (ConcurrentMap) map;
            Objects.requireNonNull(biFunction);
            Objects.requireNonNull(obj2);
            while (true) {
                Object obj3 = concurrentMap.get(obj);
                while (obj3 == null) {
                    obj3 = concurrentMap.putIfAbsent(obj, obj2);
                    if (obj3 == null) {
                        return obj2;
                    }
                }
                Object apply = biFunction.apply(obj3, obj2);
                if (apply != null) {
                    if (concurrentMap.replace(obj, obj3, apply)) {
                        return apply;
                    }
                } else if (concurrentMap.remove(obj, obj3)) {
                    return null;
                }
            }
        } else {
            return Map.-CC.$default$merge(map, obj, obj2, biFunction);
        }
    }

    public static /* synthetic */ Object B(java.util.Map map, Object obj, Object obj2) {
        return map instanceof Map ? ((Map) map).putIfAbsent(obj, obj2) : map.get(obj);
    }

    public static /* synthetic */ boolean C(java.util.Map map, Object obj, Object obj2) {
        return map instanceof Map ? ((Map) map).remove(obj, obj2) : Map.-CC.$default$remove(map, obj, obj2);
    }

    public static /* synthetic */ Object D(java.util.Map map, Object obj, Object obj2) {
        return map instanceof Map ? ((Map) map).replace(obj, obj2) : map.get(obj);
    }

    public static /* synthetic */ boolean E(java.util.Map map, Object obj, Object obj2, Object obj3) {
        return map instanceof Map ? ((Map) map).replace(obj, obj2, obj3) : Map.-CC.$default$replace(map, obj, obj2, obj3);
    }

    public static void F(java.util.Map map, BiFunction biFunction) {
        if (map instanceof Map) {
            ((Map) map).replaceAll(biFunction);
        } else if (map instanceof ConcurrentMap) {
            ConcurrentMap concurrentMap = (ConcurrentMap) map;
            Objects.requireNonNull(biFunction);
            j$.util.concurrent.a aVar = new j$.util.concurrent.a(concurrentMap, biFunction);
            if (concurrentMap instanceof j$.util.concurrent.b) {
                ((j$.util.concurrent.b) concurrentMap).forEach(aVar);
            } else {
                j$.lang.d.a(concurrentMap, aVar);
            }
        } else {
            Map.-CC.$default$replaceAll(map, biFunction);
        }
    }

    public static void G(List list, Comparator comparator) {
        if (DesugarCollections.b.isInstance(list)) {
            DesugarCollections.d(list, comparator);
            return;
        }
        Object[] array = list.toArray();
        Arrays.sort(array, comparator);
        ListIterator listIterator = list.listIterator();
        for (Object obj : array) {
            listIterator.next();
            listIterator.set(obj);
        }
    }

    public static Comparator H(Comparator comparator, Comparator comparator2) {
        if (comparator instanceof e) {
            return ((f) ((e) comparator)).thenComparing(comparator2);
        }
        Objects.requireNonNull(comparator2);
        return new c(comparator, comparator2);
    }

    public static void a(Collection collection, Consumer consumer) {
        Objects.requireNonNull(consumer);
        for (Object obj : collection) {
            consumer.accept(obj);
        }
    }

    public static void b(t tVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.f) {
            tVar.e((j$.util.function.f) consumer);
        } else if (N.a) {
            N.a(tVar.getClass(), "{0} calling Spliterator.OfDouble.forEachRemaining((DoubleConsumer) action::accept)");
            throw null;
        } else {
            Objects.requireNonNull(consumer);
            tVar.e(new m(consumer));
        }
    }

    public static void c(u.a aVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.l) {
            aVar.c((j$.util.function.l) consumer);
        } else if (N.a) {
            N.a(aVar.getClass(), "{0} calling Spliterator.OfInt.forEachRemaining((IntConsumer) action::accept)");
            throw null;
        } else {
            Objects.requireNonNull(consumer);
            aVar.c(new o(consumer));
        }
    }

    public static void d(v vVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.q) {
            vVar.d((j$.util.function.q) consumer);
        } else if (N.a) {
            N.a(vVar.getClass(), "{0} calling Spliterator.OfLong.forEachRemaining((LongConsumer) action::accept)");
            throw null;
        } else {
            Objects.requireNonNull(consumer);
            vVar.d(new q(consumer));
        }
    }

    public static long e(u uVar) {
        if ((uVar.characteristics() & 64) == 0) {
            return -1L;
        }
        return uVar.estimateSize();
    }

    public static boolean f(u uVar, int i) {
        return (uVar.characteristics() & i) == i;
    }

    public static Stream g(Collection collection) {
        return o1.y(Collection$-EL.b(collection), true);
    }

    public static boolean h(Collection collection, Predicate predicate) {
        if (DesugarCollections.a.isInstance(collection)) {
            return DesugarCollections.c(collection, predicate);
        }
        Objects.requireNonNull(predicate);
        boolean z = false;
        java.util.Iterator it = collection.iterator();
        while (it.hasNext()) {
            if (predicate.test(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public static Stream i(Collection collection) {
        return o1.y(Collection$-EL.b(collection), false);
    }

    public static boolean j(t tVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.f) {
            return tVar.k((j$.util.function.f) consumer);
        }
        if (N.a) {
            N.a(tVar.getClass(), "{0} calling Spliterator.OfDouble.tryAdvance((DoubleConsumer) action::accept)");
            throw null;
        }
        Objects.requireNonNull(consumer);
        return tVar.k(new m(consumer));
    }

    public static boolean k(u.a aVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.l) {
            return aVar.g((j$.util.function.l) consumer);
        }
        if (N.a) {
            N.a(aVar.getClass(), "{0} calling Spliterator.OfInt.tryAdvance((IntConsumer) action::accept)");
            throw null;
        }
        Objects.requireNonNull(consumer);
        return aVar.g(new o(consumer));
    }

    public static boolean l(v vVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.q) {
            return vVar.i((j$.util.function.q) consumer);
        }
        if (N.a) {
            N.a(vVar.getClass(), "{0} calling Spliterator.OfLong.tryAdvance((LongConsumer) action::accept)");
            throw null;
        }
        Objects.requireNonNull(consumer);
        return vVar.i(new q(consumer));
    }

    public static Object m(java.util.Map map, Object obj, BiFunction biFunction) {
        Object apply;
        if (map instanceof Map) {
            return ((Map) map).compute(obj, biFunction);
        }
        if (map instanceof ConcurrentMap) {
            ConcurrentMap concurrentMap = (ConcurrentMap) map;
            Objects.requireNonNull(biFunction);
            loop0: while (true) {
                Object obj2 = concurrentMap.get(obj);
                while (true) {
                    apply = biFunction.apply(obj, obj2);
                    if (apply != null) {
                        if (obj2 == null) {
                            obj2 = concurrentMap.putIfAbsent(obj, apply);
                            if (obj2 == null) {
                                break loop0;
                            }
                        } else if (concurrentMap.replace(obj, obj2, apply)) {
                            break;
                        }
                    } else {
                        apply = null;
                        if ((obj2 == null && !concurrentMap.containsKey(obj)) || concurrentMap.remove(obj, obj2)) {
                            break;
                        }
                    }
                }
            }
            return apply;
        }
        return Map.-CC.$default$compute(map, obj, biFunction);
    }

    public static Object n(java.util.Map map, Object obj, Function function) {
        Object apply;
        if (map instanceof Map) {
            return ((Map) map).computeIfAbsent(obj, function);
        }
        if (map instanceof ConcurrentMap) {
            ConcurrentMap concurrentMap = (ConcurrentMap) map;
            Objects.requireNonNull(function);
            Object obj2 = concurrentMap.get(obj);
            return (obj2 == null && (apply = function.apply(obj)) != null && (obj2 = concurrentMap.putIfAbsent(obj, apply)) == null) ? apply : obj2;
        }
        return Map.-CC.$default$computeIfAbsent(map, obj, function);
    }

    public static Object o(java.util.Map map, Object obj, BiFunction biFunction) {
        if (map instanceof Map) {
            return ((Map) map).computeIfPresent(obj, biFunction);
        }
        if (map instanceof ConcurrentMap) {
            ConcurrentMap concurrentMap = (ConcurrentMap) map;
            Objects.requireNonNull(biFunction);
            while (true) {
                Object obj2 = concurrentMap.get(obj);
                if (obj2 == null) {
                    return obj2;
                }
                Object apply = biFunction.apply(obj, obj2);
                if (apply != null) {
                    if (concurrentMap.replace(obj, obj2, apply)) {
                        return apply;
                    }
                } else if (concurrentMap.remove(obj, obj2)) {
                    return null;
                }
            }
        } else {
            return Map.-CC.$default$computeIfPresent(map, obj, biFunction);
        }
    }

    public static Optional p(java.util.Optional optional) {
        if (optional == null) {
            return null;
        }
        return optional.isPresent() ? Optional.of(optional.get()) : Optional.empty();
    }

    public static j q(OptionalDouble optionalDouble) {
        if (optionalDouble == null) {
            return null;
        }
        return optionalDouble.isPresent() ? j.d(optionalDouble.getAsDouble()) : j.a();
    }

    public static k r(OptionalInt optionalInt) {
        if (optionalInt == null) {
            return null;
        }
        return optionalInt.isPresent() ? k.d(optionalInt.getAsInt()) : k.a();
    }

    public static l s(OptionalLong optionalLong) {
        if (optionalLong == null) {
            return null;
        }
        return optionalLong.isPresent() ? l.d(optionalLong.getAsLong()) : l.a();
    }

    public static java.util.Optional t(Optional optional) {
        if (optional == null) {
            return null;
        }
        return optional.isPresent() ? java.util.Optional.of(optional.get()) : java.util.Optional.empty();
    }

    public static OptionalDouble u(j jVar) {
        if (jVar == null) {
            return null;
        }
        return jVar.c() ? OptionalDouble.of(jVar.b()) : OptionalDouble.empty();
    }

    public static OptionalInt v(k kVar) {
        if (kVar == null) {
            return null;
        }
        return kVar.c() ? OptionalInt.of(kVar.b()) : OptionalInt.empty();
    }

    public static OptionalLong w(l lVar) {
        if (lVar == null) {
            return null;
        }
        return lVar.c() ? OptionalLong.of(lVar.b()) : OptionalLong.empty();
    }

    public static boolean x(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static /* synthetic */ void y(java.util.Map map, BiConsumer biConsumer) {
        if (map instanceof Map) {
            ((Map) map).forEach(biConsumer);
        } else if (map instanceof ConcurrentMap) {
            j$.lang.d.a((ConcurrentMap) map, biConsumer);
        } else {
            Map.-CC.$default$forEach(map, biConsumer);
        }
    }

    public static Object z(java.util.Map map, Object obj, Object obj2) {
        if (map instanceof Map) {
            return ((Map) map).getOrDefault(obj, obj2);
        }
        if (map instanceof ConcurrentMap) {
            Object obj3 = ((ConcurrentMap) map).get(obj);
            return obj3 != null ? obj3 : obj2;
        }
        Object obj4 = map.get(obj);
        return (obj4 != null || map.containsKey(obj)) ? obj4 : obj2;
    }
}
