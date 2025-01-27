package androidx.core.content.pm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutInfoCompatSaver;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.util.Preconditions;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* loaded from: classes.dex */
public abstract class ShortcutManagerCompat {
    private static volatile List sShortcutInfoChangeListeners;
    private static volatile ShortcutInfoCompatSaver sShortcutInfoCompatSaver;

    private static class Api25Impl {
        static String getShortcutInfoWithLowestRank(List list) {
            Iterator it = list.iterator();
            int i = -1;
            String str = null;
            while (it.hasNext()) {
                ShortcutInfo shortcutInfo = (ShortcutInfo) it.next();
                if (shortcutInfo.getRank() > i) {
                    str = shortcutInfo.getId();
                    i = shortcutInfo.getRank();
                }
            }
            return str;
        }
    }

    public static boolean addDynamicShortcuts(Context context, List list) {
        Object systemService;
        boolean addDynamicShortcuts;
        List removeShortcutsExcludedFromSurface = removeShortcutsExcludedFromSurface(list, 1);
        int i = Build.VERSION.SDK_INT;
        if (i <= 29) {
            convertUriIconsToBitmapIcons(context, removeShortcutsExcludedFromSurface);
        }
        if (i >= 25) {
            ArrayList arrayList = new ArrayList();
            Iterator it = removeShortcutsExcludedFromSurface.iterator();
            while (it.hasNext()) {
                arrayList.add(((ShortcutInfoCompat) it.next()).toShortcutInfo());
            }
            systemService = context.getSystemService((Class<Object>) ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0.m());
            addDynamicShortcuts = ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2.m(systemService).addDynamicShortcuts(arrayList);
            if (!addDynamicShortcuts) {
                return false;
            }
        }
        getShortcutInfoSaverInstance(context).addShortcuts(removeShortcutsExcludedFromSurface);
        Iterator it2 = getShortcutInfoListeners(context).iterator();
        if (!it2.hasNext()) {
            return true;
        }
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it2.next());
        throw null;
    }

    static boolean convertUriIconToBitmapIcon(Context context, ShortcutInfoCompat shortcutInfoCompat) {
        Bitmap decodeStream;
        IconCompat iconCompat = shortcutInfoCompat.mIcon;
        if (iconCompat == null) {
            return false;
        }
        int i = iconCompat.mType;
        if (i != 6 && i != 4) {
            return true;
        }
        InputStream uriInputStream = iconCompat.getUriInputStream(context);
        if (uriInputStream == null || (decodeStream = BitmapFactory.decodeStream(uriInputStream)) == null) {
            return false;
        }
        shortcutInfoCompat.mIcon = i == 6 ? IconCompat.createWithAdaptiveBitmap(decodeStream) : IconCompat.createWithBitmap(decodeStream);
        return true;
    }

    static void convertUriIconsToBitmapIcons(Context context, List list) {
        for (ShortcutInfoCompat shortcutInfoCompat : new ArrayList(list)) {
            if (!convertUriIconToBitmapIcon(context, shortcutInfoCompat)) {
                list.remove(shortcutInfoCompat);
            }
        }
    }

    public static List getDynamicShortcuts(Context context) {
        Object systemService;
        List dynamicShortcuts;
        if (Build.VERSION.SDK_INT < 25) {
            try {
                return getShortcutInfoSaverInstance(context).getShortcuts();
            } catch (Exception unused) {
                return new ArrayList();
            }
        }
        systemService = context.getSystemService((Class<Object>) ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0.m());
        dynamicShortcuts = ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2.m(systemService).getDynamicShortcuts();
        ArrayList arrayList = new ArrayList(dynamicShortcuts.size());
        Iterator it = dynamicShortcuts.iterator();
        while (it.hasNext()) {
            arrayList.add(new ShortcutInfoCompat.Builder(context, ShortcutInfoCompat$$ExternalSyntheticApiModelOutline13.m(it.next())).build());
        }
        return arrayList;
    }

    public static int getMaxShortcutCountPerActivity(Context context) {
        Object systemService;
        int maxShortcutCountPerActivity;
        Preconditions.checkNotNull(context);
        if (Build.VERSION.SDK_INT < 25) {
            return 5;
        }
        systemService = context.getSystemService((Class<Object>) ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0.m());
        maxShortcutCountPerActivity = ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2.m(systemService).getMaxShortcutCountPerActivity();
        return maxShortcutCountPerActivity;
    }

    private static String getShortcutInfoCompatWithLowestRank(List list) {
        Iterator it = list.iterator();
        int i = -1;
        String str = null;
        while (it.hasNext()) {
            ShortcutInfoCompat shortcutInfoCompat = (ShortcutInfoCompat) it.next();
            if (shortcutInfoCompat.getRank() > i) {
                str = shortcutInfoCompat.getId();
                i = shortcutInfoCompat.getRank();
            }
        }
        return str;
    }

    private static List getShortcutInfoListeners(Context context) {
        Bundle bundle;
        String string;
        if (sShortcutInfoChangeListeners == null) {
            ArrayList arrayList = new ArrayList();
            if (Build.VERSION.SDK_INT >= 21) {
                PackageManager packageManager = context.getPackageManager();
                Intent intent = new Intent("androidx.core.content.pm.SHORTCUT_LISTENER");
                intent.setPackage(context.getPackageName());
                Iterator<ResolveInfo> it = packageManager.queryIntentActivities(intent, 128).iterator();
                while (it.hasNext()) {
                    ActivityInfo activityInfo = it.next().activityInfo;
                    if (activityInfo != null && (bundle = activityInfo.metaData) != null && (string = bundle.getString("androidx.core.content.pm.shortcut_listener_impl")) != null) {
                        try {
                            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(Class.forName(string, false, ShortcutManagerCompat.class.getClassLoader()).getMethod("getInstance", Context.class).invoke(null, context));
                            arrayList.add(null);
                        } catch (Exception unused) {
                        }
                    }
                }
            }
            if (sShortcutInfoChangeListeners == null) {
                sShortcutInfoChangeListeners = arrayList;
            }
        }
        return sShortcutInfoChangeListeners;
    }

    private static ShortcutInfoCompatSaver getShortcutInfoSaverInstance(Context context) {
        if (sShortcutInfoCompatSaver == null) {
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    sShortcutInfoCompatSaver = (ShortcutInfoCompatSaver) Class.forName("androidx.sharetarget.ShortcutInfoCompatSaverImpl", false, ShortcutManagerCompat.class.getClassLoader()).getMethod("getInstance", Context.class).invoke(null, context);
                } catch (Exception unused) {
                }
            }
            if (sShortcutInfoCompatSaver == null) {
                sShortcutInfoCompatSaver = new ShortcutInfoCompatSaver.NoopImpl();
            }
        }
        return sShortcutInfoCompatSaver;
    }

    public static List getShortcuts(Context context, int i) {
        Object systemService;
        List pinnedShortcuts;
        List dynamicShortcuts;
        List manifestShortcuts;
        Object systemService2;
        List shortcuts;
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 30) {
            systemService2 = context.getSystemService((Class<Object>) ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0.m());
            shortcuts = ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2.m(systemService2).getShortcuts(i);
            return ShortcutInfoCompat.fromShortcuts(context, shortcuts);
        }
        if (i2 < 25) {
            if ((i & 2) != 0) {
                try {
                    return getShortcutInfoSaverInstance(context).getShortcuts();
                } catch (Exception unused) {
                }
            }
            return Collections.emptyList();
        }
        systemService = context.getSystemService((Class<Object>) ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0.m());
        ShortcutManager m = ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2.m(systemService);
        ArrayList arrayList = new ArrayList();
        if ((i & 1) != 0) {
            manifestShortcuts = m.getManifestShortcuts();
            arrayList.addAll(manifestShortcuts);
        }
        if ((i & 2) != 0) {
            dynamicShortcuts = m.getDynamicShortcuts();
            arrayList.addAll(dynamicShortcuts);
        }
        if ((i & 4) != 0) {
            pinnedShortcuts = m.getPinnedShortcuts();
            arrayList.addAll(pinnedShortcuts);
        }
        return ShortcutInfoCompat.fromShortcuts(context, arrayList);
    }

    public static boolean isRequestPinShortcutSupported(Context context) {
        Object systemService;
        boolean isRequestPinShortcutSupported;
        if (Build.VERSION.SDK_INT >= 26) {
            systemService = context.getSystemService((Class<Object>) ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0.m());
            isRequestPinShortcutSupported = ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2.m(systemService).isRequestPinShortcutSupported();
            return isRequestPinShortcutSupported;
        }
        if (ContextCompat.checkSelfPermission(context, "com.android.launcher.permission.INSTALL_SHORTCUT") != 0) {
            return false;
        }
        Iterator<ResolveInfo> it = context.getPackageManager().queryBroadcastReceivers(new Intent("com.android.launcher.action.INSTALL_SHORTCUT"), 0).iterator();
        while (it.hasNext()) {
            String str = it.next().activityInfo.permission;
            if (TextUtils.isEmpty(str) || "com.android.launcher.permission.INSTALL_SHORTCUT".equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean pushDynamicShortcut(Context context, ShortcutInfoCompat shortcutInfoCompat) {
        Object systemService;
        boolean isRateLimitingActive;
        List dynamicShortcuts;
        Object systemService2;
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(shortcutInfoCompat);
        int i = Build.VERSION.SDK_INT;
        if (i <= 32 && shortcutInfoCompat.isExcludedFromSurfaces(1)) {
            Iterator it = getShortcutInfoListeners(context).iterator();
            if (!it.hasNext()) {
                return true;
            }
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
            Collections.singletonList(shortcutInfoCompat);
            throw null;
        }
        int maxShortcutCountPerActivity = getMaxShortcutCountPerActivity(context);
        if (maxShortcutCountPerActivity == 0) {
            return false;
        }
        if (i <= 29) {
            convertUriIconToBitmapIcon(context, shortcutInfoCompat);
        }
        if (i >= 30) {
            systemService2 = context.getSystemService((Class<Object>) ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0.m());
            ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2.m(systemService2).pushDynamicShortcut(shortcutInfoCompat.toShortcutInfo());
        } else if (i >= 25) {
            systemService = context.getSystemService((Class<Object>) ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0.m());
            ShortcutManager m = ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2.m(systemService);
            isRateLimitingActive = m.isRateLimitingActive();
            if (isRateLimitingActive) {
                return false;
            }
            dynamicShortcuts = m.getDynamicShortcuts();
            if (dynamicShortcuts.size() >= maxShortcutCountPerActivity) {
                m.removeDynamicShortcuts(Arrays.asList(Api25Impl.getShortcutInfoWithLowestRank(dynamicShortcuts)));
            }
            m.addDynamicShortcuts(Arrays.asList(shortcutInfoCompat.toShortcutInfo()));
        }
        ShortcutInfoCompatSaver shortcutInfoSaverInstance = getShortcutInfoSaverInstance(context);
        try {
            List shortcuts = shortcutInfoSaverInstance.getShortcuts();
            if (shortcuts.size() >= maxShortcutCountPerActivity) {
                shortcutInfoSaverInstance.removeShortcuts(Arrays.asList(getShortcutInfoCompatWithLowestRank(shortcuts)));
            }
            shortcutInfoSaverInstance.addShortcuts(Arrays.asList(shortcutInfoCompat));
            Iterator it2 = getShortcutInfoListeners(context).iterator();
            if (!it2.hasNext()) {
                reportShortcutUsed(context, shortcutInfoCompat.getId());
                return true;
            }
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it2.next());
            Collections.singletonList(shortcutInfoCompat);
            throw null;
        } catch (Exception unused) {
            Iterator it3 = getShortcutInfoListeners(context).iterator();
            if (!it3.hasNext()) {
                reportShortcutUsed(context, shortcutInfoCompat.getId());
                return false;
            }
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it3.next());
            Collections.singletonList(shortcutInfoCompat);
            throw null;
        } catch (Throwable th) {
            Iterator it4 = getShortcutInfoListeners(context).iterator();
            if (!it4.hasNext()) {
                reportShortcutUsed(context, shortcutInfoCompat.getId());
                throw th;
            }
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it4.next());
            Collections.singletonList(shortcutInfoCompat);
            throw null;
        }
    }

    public static void removeAllDynamicShortcuts(Context context) {
        Object systemService;
        if (Build.VERSION.SDK_INT >= 25) {
            systemService = context.getSystemService((Class<Object>) ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0.m());
            ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2.m(systemService).removeAllDynamicShortcuts();
        }
        getShortcutInfoSaverInstance(context).removeAllShortcuts();
        Iterator it = getShortcutInfoListeners(context).iterator();
        if (it.hasNext()) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
            throw null;
        }
    }

    public static void removeDynamicShortcuts(Context context, List list) {
        Object systemService;
        if (Build.VERSION.SDK_INT >= 25) {
            systemService = context.getSystemService((Class<Object>) ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0.m());
            ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2.m(systemService).removeDynamicShortcuts(list);
        }
        getShortcutInfoSaverInstance(context).removeShortcuts(list);
        Iterator it = getShortcutInfoListeners(context).iterator();
        if (it.hasNext()) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
            throw null;
        }
    }

    private static List removeShortcutsExcludedFromSurface(List list, int i) {
        Objects.requireNonNull(list);
        if (Build.VERSION.SDK_INT > 32) {
            return list;
        }
        ArrayList arrayList = new ArrayList(list);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ShortcutInfoCompat shortcutInfoCompat = (ShortcutInfoCompat) it.next();
            if (shortcutInfoCompat.isExcludedFromSurfaces(i)) {
                arrayList.remove(shortcutInfoCompat);
            }
        }
        return arrayList;
    }

    public static void reportShortcutUsed(Context context, String str) {
        Object systemService;
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(str);
        if (Build.VERSION.SDK_INT >= 25) {
            systemService = context.getSystemService((Class<Object>) ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0.m());
            ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2.m(systemService).reportShortcutUsed(str);
        }
        Iterator it = getShortcutInfoListeners(context).iterator();
        if (it.hasNext()) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
            Collections.singletonList(str);
            throw null;
        }
    }

    public static boolean requestPinShortcut(Context context, ShortcutInfoCompat shortcutInfoCompat, final IntentSender intentSender) {
        Object systemService;
        boolean requestPinShortcut;
        int i = Build.VERSION.SDK_INT;
        if (i <= 32 && shortcutInfoCompat.isExcludedFromSurfaces(1)) {
            return false;
        }
        if (i >= 26) {
            systemService = context.getSystemService((Class<Object>) ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0.m());
            requestPinShortcut = ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2.m(systemService).requestPinShortcut(shortcutInfoCompat.toShortcutInfo(), intentSender);
            return requestPinShortcut;
        }
        if (!isRequestPinShortcutSupported(context)) {
            return false;
        }
        Intent addToIntent = shortcutInfoCompat.addToIntent(new Intent("com.android.launcher.action.INSTALL_SHORTCUT"));
        if (intentSender == null) {
            context.sendBroadcast(addToIntent);
            return true;
        }
        context.sendOrderedBroadcast(addToIntent, null, new BroadcastReceiver() { // from class: androidx.core.content.pm.ShortcutManagerCompat.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                try {
                    intentSender.sendIntent(context2, 0, null, null, null);
                } catch (IntentSender.SendIntentException unused) {
                }
            }
        }, null, -1, null, null);
        return true;
    }

    public static boolean updateShortcuts(Context context, List list) {
        Object systemService;
        boolean updateShortcuts;
        List removeShortcutsExcludedFromSurface = removeShortcutsExcludedFromSurface(list, 1);
        int i = Build.VERSION.SDK_INT;
        if (i <= 29) {
            convertUriIconsToBitmapIcons(context, removeShortcutsExcludedFromSurface);
        }
        if (i >= 25) {
            ArrayList arrayList = new ArrayList();
            Iterator it = removeShortcutsExcludedFromSurface.iterator();
            while (it.hasNext()) {
                arrayList.add(((ShortcutInfoCompat) it.next()).toShortcutInfo());
            }
            systemService = context.getSystemService((Class<Object>) ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0.m());
            updateShortcuts = ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2.m(systemService).updateShortcuts(arrayList);
            if (!updateShortcuts) {
                return false;
            }
        }
        getShortcutInfoSaverInstance(context).addShortcuts(removeShortcutsExcludedFromSurface);
        Iterator it2 = getShortcutInfoListeners(context).iterator();
        if (!it2.hasNext()) {
            return true;
        }
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it2.next());
        throw null;
    }
}
