package j$.util.stream;

import j$.util.function.BiFunction;
import j$.util.function.BinaryOperator;
import j$.util.function.Function;
import java.util.Set;
/* loaded from: classes2.dex */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda38 implements BinaryOperator {
    public static final /* synthetic */ Collectors$$ExternalSyntheticLambda38 INSTANCE = new Collectors$$ExternalSyntheticLambda38();

    private /* synthetic */ Collectors$$ExternalSyntheticLambda38() {
    }

    @Override // j$.util.function.BiFunction
    public /* synthetic */ BiFunction andThen(Function function) {
        return function.getClass();
    }

    @Override // j$.util.function.BiFunction
    public final Object apply(Object obj, Object obj2) {
        return ((Set) obj).addAll((Set) obj2);
    }
}
