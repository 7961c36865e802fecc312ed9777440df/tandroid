package androidx.biometric;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/* loaded from: classes.dex */
abstract class PackageUtils {

    private static class Api23Impl {
        static boolean hasSystemFeatureFingerprint(PackageManager packageManager) {
            return packageManager.hasSystemFeature("android.hardware.fingerprint");
        }
    }

    static boolean hasSystemFeatureFingerprint(Context context) {
        return Build.VERSION.SDK_INT >= 23 && context != null && context.getPackageManager() != null && Api23Impl.hasSystemFeatureFingerprint(context.getPackageManager());
    }
}
