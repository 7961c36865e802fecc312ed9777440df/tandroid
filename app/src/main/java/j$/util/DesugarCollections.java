package j$.util;

import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Function;
import j$.util.function.Predicate;
import j$.wrappers.C0226q;
import j$.wrappers.C0227s;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
/* loaded from: classes2.dex */
public class DesugarCollections {
    public static final Class a;
    static final Class b = Collections.synchronizedList(new LinkedList()).getClass();
    private static final Field c;
    private static final Field d;
    private static final Constructor e;
    private static final Constructor f;

    /* loaded from: classes2.dex */
    public static class a implements java.util.Map, Serializable, Map {
        private final java.util.Map a;
        final Object b = this;
        private transient Set c;
        private transient Set d;
        private transient Collection e;

        a(java.util.Map map) {
            map.getClass();
            this.a = map;
        }

        private Set a(Set set, Object obj) {
            if (DesugarCollections.f == null) {
                return Collections.synchronizedSet(set);
            }
            try {
                return (Set) DesugarCollections.f.newInstance(set, obj);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new Error("Unable to instantiate a synchronized list.", e);
            }
        }

        @Override // java.util.Map, j$.util.Map
        public void clear() {
            synchronized (this.b) {
                this.a.clear();
            }
        }

        @Override // j$.util.Map
        public Object compute(Object obj, BiFunction biFunction) {
            Object m;
            synchronized (this.b) {
                m = AbstractC0033a.m(this.a, obj, biFunction);
            }
            return m;
        }

        @Override // java.util.Map
        public Object compute(Object obj, java.util.function.BiFunction biFunction) {
            Object m;
            BiFunction a = C0227s.a(biFunction);
            synchronized (this.b) {
                m = AbstractC0033a.m(this.a, obj, a);
            }
            return m;
        }

        @Override // j$.util.Map
        public Object computeIfAbsent(Object obj, Function function) {
            Object n;
            synchronized (this.b) {
                n = AbstractC0033a.n(this.a, obj, function);
            }
            return n;
        }

        @Override // java.util.Map
        public Object computeIfAbsent(Object obj, java.util.function.Function function) {
            Object n;
            Function a = j$.wrappers.M.a(function);
            synchronized (this.b) {
                n = AbstractC0033a.n(this.a, obj, a);
            }
            return n;
        }

        @Override // j$.util.Map
        public Object computeIfPresent(Object obj, BiFunction biFunction) {
            Object o;
            synchronized (this.b) {
                o = AbstractC0033a.o(this.a, obj, biFunction);
            }
            return o;
        }

        @Override // java.util.Map
        public Object computeIfPresent(Object obj, java.util.function.BiFunction biFunction) {
            Object o;
            BiFunction a = C0227s.a(biFunction);
            synchronized (this.b) {
                o = AbstractC0033a.o(this.a, obj, a);
            }
            return o;
        }

        @Override // java.util.Map, j$.util.Map
        public boolean containsKey(Object obj) {
            boolean containsKey;
            synchronized (this.b) {
                containsKey = this.a.containsKey(obj);
            }
            return containsKey;
        }

        @Override // java.util.Map, j$.util.Map
        public boolean containsValue(Object obj) {
            boolean containsValue;
            synchronized (this.b) {
                containsValue = this.a.containsValue(obj);
            }
            return containsValue;
        }

        @Override // java.util.Map, j$.util.Map
        public Set entrySet() {
            Set set;
            synchronized (this.b) {
                if (this.d == null) {
                    this.d = a(this.a.entrySet(), this.b);
                }
                set = this.d;
            }
            return set;
        }

        @Override // java.util.Map, j$.util.Map
        public boolean equals(Object obj) {
            boolean equals;
            if (this == obj) {
                return true;
            }
            synchronized (this.b) {
                equals = this.a.equals(obj);
            }
            return equals;
        }

        @Override // j$.util.Map
        public void forEach(BiConsumer biConsumer) {
            synchronized (this.b) {
                AbstractC0033a.y(this.a, biConsumer);
            }
        }

        @Override // java.util.Map
        public void forEach(java.util.function.BiConsumer biConsumer) {
            BiConsumer a = C0226q.a(biConsumer);
            synchronized (this.b) {
                AbstractC0033a.y(this.a, a);
            }
        }

        @Override // java.util.Map, j$.util.Map
        public Object get(Object obj) {
            Object obj2;
            synchronized (this.b) {
                obj2 = this.a.get(obj);
            }
            return obj2;
        }

        @Override // java.util.Map, j$.util.Map
        public Object getOrDefault(Object obj, Object obj2) {
            Object z;
            synchronized (this.b) {
                z = AbstractC0033a.z(this.a, obj, obj2);
            }
            return z;
        }

        @Override // java.util.Map, j$.util.Map
        public int hashCode() {
            int hashCode;
            synchronized (this.b) {
                hashCode = this.a.hashCode();
            }
            return hashCode;
        }

        @Override // java.util.Map, j$.util.Map
        public boolean isEmpty() {
            boolean isEmpty;
            synchronized (this.b) {
                isEmpty = this.a.isEmpty();
            }
            return isEmpty;
        }

        @Override // java.util.Map, j$.util.Map
        public Set keySet() {
            Set set;
            synchronized (this.b) {
                if (this.c == null) {
                    this.c = a(this.a.keySet(), this.b);
                }
                set = this.c;
            }
            return set;
        }

        @Override // j$.util.Map
        public Object merge(Object obj, Object obj2, BiFunction biFunction) {
            Object A;
            synchronized (this.b) {
                A = AbstractC0033a.A(this.a, obj, obj2, biFunction);
            }
            return A;
        }

        @Override // java.util.Map
        public Object merge(Object obj, Object obj2, java.util.function.BiFunction biFunction) {
            Object A;
            BiFunction a = C0227s.a(biFunction);
            synchronized (this.b) {
                A = AbstractC0033a.A(this.a, obj, obj2, a);
            }
            return A;
        }

        @Override // java.util.Map, j$.util.Map
        public Object put(Object obj, Object obj2) {
            Object put;
            synchronized (this.b) {
                put = this.a.put(obj, obj2);
            }
            return put;
        }

        @Override // java.util.Map, j$.util.Map
        public void putAll(java.util.Map map) {
            synchronized (this.b) {
                this.a.putAll(map);
            }
        }

        @Override // java.util.Map, j$.util.Map
        public Object putIfAbsent(Object obj, Object obj2) {
            Object B;
            synchronized (this.b) {
                B = AbstractC0033a.B(this.a, obj, obj2);
            }
            return B;
        }

        @Override // java.util.Map, j$.util.Map
        public Object remove(Object obj) {
            Object remove;
            synchronized (this.b) {
                remove = this.a.remove(obj);
            }
            return remove;
        }

        @Override // java.util.Map, j$.util.Map
        public boolean remove(Object obj, Object obj2) {
            boolean C;
            synchronized (this.b) {
                C = AbstractC0033a.C(this.a, obj, obj2);
            }
            return C;
        }

        @Override // java.util.Map, j$.util.Map
        public Object replace(Object obj, Object obj2) {
            Object D;
            synchronized (this.b) {
                D = AbstractC0033a.D(this.a, obj, obj2);
            }
            return D;
        }

        @Override // java.util.Map, j$.util.Map
        public boolean replace(Object obj, Object obj2, Object obj3) {
            boolean E;
            synchronized (this.b) {
                E = AbstractC0033a.E(this.a, obj, obj2, obj3);
            }
            return E;
        }

        @Override // j$.util.Map
        public void replaceAll(BiFunction biFunction) {
            synchronized (this.b) {
                AbstractC0033a.F(this.a, biFunction);
            }
        }

        @Override // java.util.Map
        public void replaceAll(java.util.function.BiFunction biFunction) {
            BiFunction a = C0227s.a(biFunction);
            synchronized (this.b) {
                AbstractC0033a.F(this.a, a);
            }
        }

        @Override // java.util.Map, j$.util.Map
        public int size() {
            int size;
            synchronized (this.b) {
                size = this.a.size();
            }
            return size;
        }

        public String toString() {
            String obj;
            synchronized (this.b) {
                obj = this.a.toString();
            }
            return obj;
        }

        @Override // java.util.Map, j$.util.Map
        public Collection values() {
            Collection collection;
            Throwable e;
            Collection collection2;
            synchronized (this.b) {
                try {
                    if (this.e == null) {
                        Collection values = this.a.values();
                        Object obj = this.b;
                        if (DesugarCollections.e == null) {
                            collection2 = Collections.synchronizedCollection(values);
                        } else {
                            try {
                                collection2 = (Collection) DesugarCollections.e.newInstance(values, obj);
                            } catch (IllegalAccessException e2) {
                                e = e2;
                                throw new Error("Unable to instantiate a synchronized list.", e);
                            } catch (InstantiationException e3) {
                                e = e3;
                                throw new Error("Unable to instantiate a synchronized list.", e);
                            } catch (InvocationTargetException e4) {
                                e = e4;
                                throw new Error("Unable to instantiate a synchronized list.", e);
                            }
                        }
                        this.e = collection2;
                    }
                    collection = this.e;
                } finally {
                }
            }
            return collection;
        }
    }

    static {
        Field field;
        Field field2;
        Constructor<?> constructor;
        Class<?> cls = Collections.synchronizedCollection(new ArrayList()).getClass();
        a = cls;
        Constructor<?> constructor2 = null;
        try {
            field = cls.getDeclaredField("mutex");
        } catch (NoSuchFieldException unused) {
            field = null;
        }
        c = field;
        if (field != null) {
            field.setAccessible(true);
        }
        try {
            field2 = cls.getDeclaredField("c");
        } catch (NoSuchFieldException unused2) {
            field2 = null;
        }
        d = field2;
        if (field2 != null) {
            field2.setAccessible(true);
        }
        try {
            constructor = Collections.synchronizedSet(new HashSet()).getClass().getDeclaredConstructor(Set.class, Object.class);
        } catch (NoSuchMethodException unused3) {
            constructor = null;
        }
        f = constructor;
        if (constructor != null) {
            constructor.setAccessible(true);
        }
        try {
            constructor2 = cls.getDeclaredConstructor(Collection.class, Object.class);
        } catch (NoSuchMethodException unused4) {
        }
        e = constructor2;
        if (constructor2 != null) {
            constructor2.setAccessible(true);
        }
    }

    public static boolean c(Collection collection, Predicate predicate) {
        boolean removeIf;
        Field field = c;
        if (field == null) {
            try {
                return Collection$EL.removeIf((Collection) d.get(collection), predicate);
            } catch (IllegalAccessException e2) {
                throw new Error("Runtime illegal access in synchronized collection removeIf fall-back.", e2);
            }
        }
        try {
            synchronized (field.get(collection)) {
                removeIf = Collection$EL.removeIf((Collection) d.get(collection), predicate);
            }
            return removeIf;
        } catch (IllegalAccessException e3) {
            throw new Error("Runtime illegal access in synchronized collection removeIf.", e3);
        }
    }

    public static void d(List list, Comparator comparator) {
        Field field = c;
        if (field == null) {
            try {
                AbstractC0033a.G((List) d.get(list), comparator);
                return;
            } catch (IllegalAccessException e2) {
                throw new Error("Runtime illegal access in synchronized collection sort fall-back.", e2);
            }
        }
        try {
            synchronized (field.get(list)) {
                AbstractC0033a.G((List) d.get(list), comparator);
            }
        } catch (IllegalAccessException e3) {
            throw new Error("Runtime illegal access in synchronized list sort.", e3);
        }
    }

    public static <K, V> java.util.Map<K, V> synchronizedMap(java.util.Map<K, V> map) {
        return new a(map);
    }
}