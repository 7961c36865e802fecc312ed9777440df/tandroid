package j$.util.concurrent;

import j$.util.Map;
import j$.util.function.BiConsumer;
/* loaded from: classes2.dex */
public interface v extends Map {
    @Override // j$.util.Map
    void forEach(BiConsumer biConsumer);

    @Override // java.util.concurrent.ConcurrentMap, j$.util.concurrent.v, j$.util.Map
    Object getOrDefault(Object obj, Object obj2);
}