package kotlin.internal.jdk7;

import kotlin.internal.PlatformImplementations;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: JDK7PlatformImplementations.kt */
/* loaded from: classes.dex */
public class JDK7PlatformImplementations extends PlatformImplementations {

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: JDK7PlatformImplementations.kt */
    /* loaded from: classes.dex */
    public static final class ReflectSdkVersion {
        public static final Integer sdkVersion;

        private ReflectSdkVersion() {
        }

        /* JADX WARN: Removed duplicated region for block: B:9:0x0020  */
        static {
            Integer num;
            Object obj;
            new ReflectSdkVersion();
            Integer num2 = null;
            try {
                obj = Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
            } catch (Throwable unused) {
            }
            if (obj instanceof Integer) {
                num = (Integer) obj;
                if (num != null) {
                    if (num.intValue() > 0) {
                        num2 = num;
                    }
                }
                sdkVersion = num2;
            }
            num = null;
            if (num != null) {
            }
            sdkVersion = num2;
        }
    }

    private final boolean sdkIsNullOrAtLeast(int i) {
        Integer num = ReflectSdkVersion.sdkVersion;
        return num == null || num.intValue() >= i;
    }

    @Override // kotlin.internal.PlatformImplementations
    public void addSuppressed(Throwable cause, Throwable exception) {
        Intrinsics.checkNotNullParameter(cause, "cause");
        Intrinsics.checkNotNullParameter(exception, "exception");
        if (sdkIsNullOrAtLeast(19)) {
            cause.addSuppressed(exception);
        } else {
            super.addSuppressed(cause, exception);
        }
    }
}