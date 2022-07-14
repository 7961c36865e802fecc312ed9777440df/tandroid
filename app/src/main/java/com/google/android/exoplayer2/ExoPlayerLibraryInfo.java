package com.google.android.exoplayer2;

import java.util.HashSet;
/* loaded from: classes3.dex */
public final class ExoPlayerLibraryInfo {
    public static final boolean ASSERTIONS_ENABLED = true;
    public static final boolean GL_ASSERTIONS_ENABLED = false;
    public static final String TAG = "ExoPlayer";
    public static final boolean TRACE_ENABLED = true;
    public static final String VERSION = "2.11.7";
    public static final int VERSION_INT = 2011007;
    public static final String VERSION_SLASHY = "ExoPlayerLib/2.11.7";
    private static final HashSet<String> registeredModules = new HashSet<>();
    private static String registeredModulesString = "goog.exo.core";

    private ExoPlayerLibraryInfo() {
    }

    public static synchronized String registeredModules() {
        String str;
        synchronized (ExoPlayerLibraryInfo.class) {
            str = registeredModulesString;
        }
        return str;
    }

    public static synchronized void registerModule(String name) {
        synchronized (ExoPlayerLibraryInfo.class) {
            if (registeredModules.add(name)) {
                registeredModulesString += ", " + name;
            }
        }
    }
}
