package org.telegram.messenger;

import android.app.ActivityManager;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseArray;
import android.webkit.WebView;
import androidx.core.content.pm.ShortcutManagerCompat;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLRPC$TL_help_appUpdate;
import org.telegram.tgnet.TLRPC$help_AppUpdate;
/* loaded from: classes.dex */
public class SharedConfig {
    public static final int PASSCODE_TYPE_PASSWORD = 1;
    public static final int PASSCODE_TYPE_PIN = 0;
    public static final int PERFORMANCE_CLASS_AVERAGE = 1;
    public static final int PERFORMANCE_CLASS_HIGH = 2;
    public static final int PERFORMANCE_CLASS_LOW = 0;
    public static final int SAVE_TO_GALLERY_FLAG_CHANNELS = 4;
    public static final int SAVE_TO_GALLERY_FLAG_GROUP = 2;
    public static final int SAVE_TO_GALLERY_FLAG_PEER = 1;
    public static boolean allowBigEmoji = false;
    public static boolean allowScreenCapture = false;
    private static Boolean animationsEnabled = null;
    public static boolean appLocked = false;
    public static boolean archiveHidden = false;
    public static int autoLockIn = 3600;
    public static boolean autoplayGifs = false;
    public static boolean autoplayVideo = false;
    public static int badPasscodeTries = 0;
    public static int bubbleRadius = 0;
    public static boolean chatBlur = false;
    public static boolean chatBubbles = false;
    private static int chatSwipeAction = 0;
    private static boolean configLoaded = false;
    public static ProxyInfo currentProxy = null;
    public static boolean customTabs = false;
    public static int dayNightThemeSwitchHintCount = 0;
    public static boolean debugWebView = false;
    private static int devicePerformanceClass = 0;
    public static boolean directShare = false;
    public static String directShareHash = null;
    public static boolean disableVoiceAudioEffects = false;
    public static int distanceSystemType = 0;
    public static boolean dontAskManageStorage = false;
    public static boolean drawDialogIcons = false;
    public static int emojiInteractionsHintCount = 0;
    public static int fastScrollHintCount = 0;
    public static int fontSize = 0;
    public static boolean forceDisableTabletMode = false;
    public static boolean forceRtmpStream = false;
    public static boolean forwardingOptionsHintShown = false;
    public static boolean hasCameraCache = false;
    public static boolean hasEmailLogin = false;
    public static boolean inappCamera = false;
    public static boolean isWaitingForPasscodeEnter = false;
    public static int ivFontSize = 0;
    public static int keepMedia = 2;
    public static int lastKeepMediaCheckTime = 0;
    private static int lastLocalId = -210000;
    public static int lastLogsCheckTime = 0;
    public static int lastPauseTime = 0;
    public static long lastUpdateCheckTime = 0;
    public static String lastUpdateVersion = null;
    public static long lastUptimeMillis = 0;
    public static int lockRecordAudioVideoHint = 0;
    public static boolean loopStickers = false;
    public static int mapPreviewType = 2;
    public static int mediaColumnsCount = 0;
    public static int messageSeenHintCount = 0;
    public static boolean noSoundHintShowed = false;
    public static boolean noStatusBar = false;
    public static boolean noiseSupression = false;
    public static String passcodeHash = "";
    public static long passcodeRetryInMs = 0;
    public static int passcodeType = 0;
    public static int passportConfigHash = 0;
    private static String passportConfigJson = "";
    private static HashMap<String, String> passportConfigMap = null;
    public static boolean pauseMusicOnRecord = false;
    public static TLRPC$TL_help_appUpdate pendingAppUpdate = null;
    public static int pendingAppUpdateBuildVersion = 0;
    public static boolean playOrderReversed = false;
    public static ArrayList<ProxyInfo> proxyList = null;
    private static boolean proxyListLoaded = false;
    public static byte[] pushAuthKey = null;
    public static byte[] pushAuthKeyId = null;
    public static boolean pushStatSent = false;
    public static String pushString = "";
    public static long pushStringGetTimeEnd = 0;
    public static long pushStringGetTimeStart = 0;
    public static String pushStringStatus = "";
    public static int pushType = 2;
    public static boolean raiseToSpeak = false;
    public static boolean recordViaSco = false;
    public static int repeatMode = 0;
    public static boolean roundCamera16to9 = false;
    public static boolean saveIncomingPhotos = false;
    public static boolean saveStreamMedia = false;
    public static int saveToGalleryFlags = 0;
    public static int scheduledOrNoSoundHintShows = 0;
    public static int searchMessagesAsListHintShows = 0;
    public static boolean searchMessagesAsListUsed = false;
    public static boolean showNotificationsForAllAccounts = false;
    public static boolean shuffleMusic = false;
    public static boolean smoothKeyboard = false;
    public static boolean sortContactsByName = false;
    public static boolean sortFilesByName = false;
    public static boolean stickersReorderingHintUsed = false;
    public static String storageCacheDir = null;
    public static boolean streamAllVideo = false;
    public static boolean streamMedia = false;
    public static boolean streamMkv = false;
    public static boolean suggestAnimatedEmoji = false;
    public static int suggestStickers = 0;
    public static int textSelectionHintShows = 0;
    public static boolean useFingerprint = true;
    public static boolean useSystemEmoji;
    public static boolean useThreeLinesLayout;
    public static byte[] passcodeSalt = new byte[0];
    private static final Object sync = new Object();
    private static final Object localIdSync = new Object();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface PasscodeType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface PerformanceClass {
    }

    static {
        chatBubbles = Build.VERSION.SDK_INT >= 30;
        autoplayGifs = true;
        autoplayVideo = true;
        raiseToSpeak = false;
        recordViaSco = true;
        customTabs = true;
        directShare = true;
        inappCamera = true;
        roundCamera16to9 = true;
        noSoundHintShowed = false;
        streamMedia = true;
        streamAllVideo = false;
        streamMkv = false;
        saveStreamMedia = true;
        smoothKeyboard = true;
        pauseMusicOnRecord = true;
        chatBlur = true;
        noStatusBar = true;
        showNotificationsForAllAccounts = true;
        fontSize = 16;
        bubbleRadius = 17;
        ivFontSize = 16;
        mediaColumnsCount = 3;
        fastScrollHintCount = 3;
        loadConfig();
        proxyList = new ArrayList<>();
    }

    /* loaded from: classes.dex */
    public static class ProxyInfo {
        public String address;
        public boolean available;
        public long availableCheckTime;
        public boolean checking;
        public String password;
        public long ping;
        public int port;
        public long proxyCheckPingId;
        public String secret;
        public String username;

        public ProxyInfo(String str, int i, String str2, String str3, String str4) {
            this.address = str;
            this.port = i;
            this.username = str2;
            this.password = str3;
            this.secret = str4;
            if (str == null) {
                this.address = "";
            }
            if (str3 == null) {
                this.password = "";
            }
            if (str2 == null) {
                this.username = "";
            }
            if (str4 == null) {
                this.secret = "";
            }
        }
    }

    public static void saveConfig() {
        synchronized (sync) {
            try {
                SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("userconfing", 0).edit();
                edit.putBoolean("saveIncomingPhotos", saveIncomingPhotos);
                edit.putString("passcodeHash1", passcodeHash);
                byte[] bArr = passcodeSalt;
                edit.putString("passcodeSalt", bArr.length > 0 ? Base64.encodeToString(bArr, 0) : "");
                edit.putBoolean("appLocked", appLocked);
                edit.putInt("passcodeType", passcodeType);
                edit.putLong("passcodeRetryInMs", passcodeRetryInMs);
                edit.putLong("lastUptimeMillis", lastUptimeMillis);
                edit.putInt("badPasscodeTries", badPasscodeTries);
                edit.putInt("autoLockIn", autoLockIn);
                edit.putInt("lastPauseTime", lastPauseTime);
                edit.putString("lastUpdateVersion2", lastUpdateVersion);
                edit.putBoolean("useFingerprint", useFingerprint);
                edit.putBoolean("allowScreenCapture", allowScreenCapture);
                edit.putString("pushString2", pushString);
                edit.putInt("pushType", pushType);
                edit.putBoolean("pushStatSent", pushStatSent);
                byte[] bArr2 = pushAuthKey;
                edit.putString("pushAuthKey", bArr2 != null ? Base64.encodeToString(bArr2, 0) : "");
                edit.putInt("lastLocalId", lastLocalId);
                edit.putString("passportConfigJson", passportConfigJson);
                edit.putInt("passportConfigHash", passportConfigHash);
                edit.putBoolean("sortContactsByName", sortContactsByName);
                edit.putBoolean("sortFilesByName", sortFilesByName);
                edit.putInt("textSelectionHintShows", textSelectionHintShows);
                edit.putInt("scheduledOrNoSoundHintShows", scheduledOrNoSoundHintShows);
                edit.putBoolean("forwardingOptionsHintShown", forwardingOptionsHintShown);
                edit.putInt("lockRecordAudioVideoHint", lockRecordAudioVideoHint);
                edit.putString("storageCacheDir", !TextUtils.isEmpty(storageCacheDir) ? storageCacheDir : "");
                edit.putBoolean("hasEmailLogin", hasEmailLogin);
                TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate = pendingAppUpdate;
                if (tLRPC$TL_help_appUpdate != null) {
                    try {
                        SerializedData serializedData = new SerializedData(tLRPC$TL_help_appUpdate.getObjectSize());
                        pendingAppUpdate.serializeToStream(serializedData);
                        edit.putString("appUpdate", Base64.encodeToString(serializedData.toByteArray(), 0));
                        edit.putInt("appUpdateBuild", pendingAppUpdateBuildVersion);
                        serializedData.cleanup();
                    } catch (Exception unused) {
                    }
                } else {
                    edit.remove("appUpdate");
                }
                edit.putLong("appUpdateCheckTime", lastUpdateCheckTime);
                edit.apply();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public static int getLastLocalId() {
        int i;
        synchronized (localIdSync) {
            i = lastLocalId;
            lastLocalId = i - 1;
        }
        return i;
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x0232  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0235  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x016a A[Catch: Exception -> 0x0188, all -> 0x03dd, TryCatch #1 {Exception -> 0x0188, blocks: (B:22:0x011b, B:24:0x0123, B:26:0x0133, B:27:0x0147, B:63:0x016a, B:65:0x016e, B:66:0x0170, B:68:0x0174, B:70:0x017a, B:72:0x0180, B:76:0x0164), top: B:21:0x011b, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x016e A[Catch: Exception -> 0x0188, all -> 0x03dd, TryCatch #1 {Exception -> 0x0188, blocks: (B:22:0x011b, B:24:0x0123, B:26:0x0133, B:27:0x0147, B:63:0x016a, B:65:0x016e, B:66:0x0170, B:68:0x0174, B:70:0x017a, B:72:0x0180, B:76:0x0164), top: B:21:0x011b, outer: #3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void loadConfig() {
        SharedPreferences sharedPreferences;
        int i;
        String str;
        String str2;
        synchronized (sync) {
            if (!configLoaded && ApplicationLoader.applicationContext != null) {
                SharedPreferences unused = BackgroundActivityPrefs.prefs = ApplicationLoader.applicationContext.getSharedPreferences("background_activity", 0);
                SharedPreferences sharedPreferences2 = ApplicationLoader.applicationContext.getSharedPreferences("userconfing", 0);
                saveIncomingPhotos = sharedPreferences2.getBoolean("saveIncomingPhotos", false);
                passcodeHash = sharedPreferences2.getString("passcodeHash1", "");
                appLocked = sharedPreferences2.getBoolean("appLocked", false);
                passcodeType = sharedPreferences2.getInt("passcodeType", 0);
                passcodeRetryInMs = sharedPreferences2.getLong("passcodeRetryInMs", 0L);
                lastUptimeMillis = sharedPreferences2.getLong("lastUptimeMillis", 0L);
                badPasscodeTries = sharedPreferences2.getInt("badPasscodeTries", 0);
                autoLockIn = sharedPreferences2.getInt("autoLockIn", 3600);
                lastPauseTime = sharedPreferences2.getInt("lastPauseTime", 0);
                useFingerprint = sharedPreferences2.getBoolean("useFingerprint", true);
                lastUpdateVersion = sharedPreferences2.getString("lastUpdateVersion2", "3.5");
                allowScreenCapture = sharedPreferences2.getBoolean("allowScreenCapture", false);
                lastLocalId = sharedPreferences2.getInt("lastLocalId", -210000);
                pushString = sharedPreferences2.getString("pushString2", "");
                pushType = sharedPreferences2.getInt("pushType", 2);
                pushStatSent = sharedPreferences2.getBoolean("pushStatSent", false);
                passportConfigJson = sharedPreferences2.getString("passportConfigJson", "");
                passportConfigHash = sharedPreferences2.getInt("passportConfigHash", 0);
                storageCacheDir = sharedPreferences2.getString("storageCacheDir", null);
                String string = sharedPreferences2.getString("pushAuthKey", null);
                if (!TextUtils.isEmpty(string)) {
                    pushAuthKey = Base64.decode(string, 0);
                }
                if (passcodeHash.length() > 0 && lastPauseTime == 0) {
                    lastPauseTime = (int) ((SystemClock.elapsedRealtime() / 1000) - 600);
                }
                String string2 = sharedPreferences2.getString("passcodeSalt", "");
                if (string2.length() > 0) {
                    passcodeSalt = Base64.decode(string2, 0);
                } else {
                    passcodeSalt = new byte[0];
                }
                lastUpdateCheckTime = sharedPreferences2.getLong("appUpdateCheckTime", System.currentTimeMillis());
                try {
                    String string3 = sharedPreferences2.getString("appUpdate", null);
                    if (string3 != null) {
                        pendingAppUpdateBuildVersion = sharedPreferences2.getInt("appUpdateBuild", BuildVars.BUILD_VERSION);
                        byte[] decode = Base64.decode(string3, 0);
                        if (decode != null) {
                            SerializedData serializedData = new SerializedData(decode);
                            pendingAppUpdate = (TLRPC$TL_help_appUpdate) TLRPC$help_AppUpdate.TLdeserialize(serializedData, serializedData.readInt32(false), false);
                            serializedData.cleanup();
                        }
                    }
                    if (pendingAppUpdate != null) {
                        try {
                            PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                            i = packageInfo.versionCode;
                            try {
                                str = packageInfo.versionName;
                            } catch (Exception e) {
                                e = e;
                                FileLog.e(e);
                                str = null;
                                if (i == 0) {
                                }
                                if (str == null) {
                                }
                                if (pendingAppUpdateBuildVersion == i) {
                                }
                                pendingAppUpdate = null;
                                AndroidUtilities.runOnUIThread(SharedConfig$$ExternalSyntheticLambda4.INSTANCE);
                                sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                                if (!sharedPreferences.getBoolean("save_gallery", false)) {
                                }
                                saveToGalleryFlags = sharedPreferences.getInt("save_gallery_flags", 0);
                                autoplayGifs = sharedPreferences.getBoolean("autoplay_gif", true);
                                autoplayVideo = sharedPreferences.getBoolean("autoplay_video", true);
                                mapPreviewType = sharedPreferences.getInt("mapPreviewType", 2);
                                raiseToSpeak = sharedPreferences.getBoolean("raise_to_speak", false);
                                recordViaSco = sharedPreferences.getBoolean("record_via_sco", true);
                                customTabs = sharedPreferences.getBoolean("custom_tabs", true);
                                directShare = sharedPreferences.getBoolean("direct_share", true);
                                boolean z = sharedPreferences.getBoolean("shuffleMusic", false);
                                shuffleMusic = z;
                                playOrderReversed = z && sharedPreferences.getBoolean("playOrderReversed", false);
                                inappCamera = sharedPreferences.getBoolean("inappCamera", true);
                                hasCameraCache = sharedPreferences.contains("cameraCache");
                                roundCamera16to9 = true;
                                repeatMode = sharedPreferences.getInt("repeatMode", 0);
                                fontSize = sharedPreferences.getInt("fons_size", !AndroidUtilities.isTablet() ? 18 : 16);
                                bubbleRadius = sharedPreferences.getInt("bubbleRadius", 17);
                                ivFontSize = sharedPreferences.getInt("iv_font_size", fontSize);
                                allowBigEmoji = sharedPreferences.getBoolean("allowBigEmoji", true);
                                useSystemEmoji = sharedPreferences.getBoolean("useSystemEmoji", false);
                                streamMedia = sharedPreferences.getBoolean("streamMedia", true);
                                saveStreamMedia = sharedPreferences.getBoolean("saveStreamMedia", true);
                                smoothKeyboard = sharedPreferences.getBoolean("smoothKeyboard2", true);
                                pauseMusicOnRecord = sharedPreferences.getBoolean("pauseMusicOnRecord", false);
                                chatBlur = sharedPreferences.getBoolean("chatBlur", true);
                                forceDisableTabletMode = sharedPreferences.getBoolean("forceDisableTabletMode", false);
                                streamAllVideo = sharedPreferences.getBoolean("streamAllVideo", BuildVars.DEBUG_VERSION);
                                streamMkv = sharedPreferences.getBoolean("streamMkv", false);
                                suggestStickers = sharedPreferences.getInt("suggestStickers", 0);
                                suggestAnimatedEmoji = sharedPreferences.getBoolean("suggestAnimatedEmoji", true);
                                sortContactsByName = sharedPreferences.getBoolean("sortContactsByName", false);
                                sortFilesByName = sharedPreferences.getBoolean("sortFilesByName", false);
                                noSoundHintShowed = sharedPreferences.getBoolean("noSoundHintShowed", false);
                                directShareHash = sharedPreferences.getString("directShareHash2", null);
                                useThreeLinesLayout = sharedPreferences.getBoolean("useThreeLinesLayout", false);
                                archiveHidden = sharedPreferences.getBoolean("archiveHidden", false);
                                distanceSystemType = sharedPreferences.getInt("distanceSystemType", 0);
                                devicePerformanceClass = sharedPreferences.getInt("devicePerformanceClass", -1);
                                loopStickers = sharedPreferences.getBoolean("loopStickers", true);
                                keepMedia = sharedPreferences.getInt("keep_media", 2);
                                noStatusBar = sharedPreferences.getBoolean("noStatusBar", true);
                                forceRtmpStream = sharedPreferences.getBoolean("forceRtmpStream", false);
                                debugWebView = sharedPreferences.getBoolean("debugWebView", false);
                                lastKeepMediaCheckTime = sharedPreferences.getInt("lastKeepMediaCheckTime", 0);
                                lastLogsCheckTime = sharedPreferences.getInt("lastLogsCheckTime", 0);
                                searchMessagesAsListHintShows = sharedPreferences.getInt("searchMessagesAsListHintShows", 0);
                                searchMessagesAsListUsed = sharedPreferences.getBoolean("searchMessagesAsListUsed", false);
                                stickersReorderingHintUsed = sharedPreferences.getBoolean("stickersReorderingHintUsed", false);
                                textSelectionHintShows = sharedPreferences.getInt("textSelectionHintShows", 0);
                                scheduledOrNoSoundHintShows = sharedPreferences.getInt("scheduledOrNoSoundHintShows", 0);
                                forwardingOptionsHintShown = sharedPreferences.getBoolean("forwardingOptionsHintShown", false);
                                lockRecordAudioVideoHint = sharedPreferences.getInt("lockRecordAudioVideoHint", 0);
                                disableVoiceAudioEffects = sharedPreferences.getBoolean("disableVoiceAudioEffects", false);
                                noiseSupression = sharedPreferences.getBoolean("noiseSupression", false);
                                chatSwipeAction = sharedPreferences.getInt("ChatSwipeAction", -1);
                                messageSeenHintCount = sharedPreferences.getInt("messageSeenCount", 3);
                                emojiInteractionsHintCount = sharedPreferences.getInt("emojiInteractionsHintCount", 3);
                                dayNightThemeSwitchHintCount = sharedPreferences.getInt("dayNightThemeSwitchHintCount", 3);
                                mediaColumnsCount = sharedPreferences.getInt("mediaColumnsCount", 3);
                                fastScrollHintCount = sharedPreferences.getInt("fastScrollHintCount", 3);
                                dontAskManageStorage = sharedPreferences.getBoolean("dontAskManageStorage", false);
                                hasEmailLogin = sharedPreferences.getBoolean("hasEmailLogin", false);
                                showNotificationsForAllAccounts = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0).getBoolean("AllAccounts", true);
                                configLoaded = true;
                                if (Build.VERSION.SDK_INT >= 19) {
                                    WebView.setWebContentsDebuggingEnabled(true);
                                }
                            }
                        } catch (Exception e2) {
                            e = e2;
                            i = 0;
                        }
                        if (i == 0) {
                            i = BuildVars.BUILD_VERSION;
                        }
                        if (str == null) {
                            str = BuildVars.BUILD_VERSION_STRING;
                        }
                        if (pendingAppUpdateBuildVersion == i || (str2 = pendingAppUpdate.version) == null || str.compareTo(str2) >= 0) {
                            pendingAppUpdate = null;
                            AndroidUtilities.runOnUIThread(SharedConfig$$ExternalSyntheticLambda4.INSTANCE);
                        }
                    }
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
                sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                if (!sharedPreferences.getBoolean("save_gallery", false) && BuildVars.NO_SCOPED_STORAGE) {
                    saveToGalleryFlags = 7;
                    sharedPreferences.edit().remove("save_gallery").putInt("save_gallery_flags", saveToGalleryFlags).apply();
                } else {
                    saveToGalleryFlags = sharedPreferences.getInt("save_gallery_flags", 0);
                }
                autoplayGifs = sharedPreferences.getBoolean("autoplay_gif", true);
                autoplayVideo = sharedPreferences.getBoolean("autoplay_video", true);
                mapPreviewType = sharedPreferences.getInt("mapPreviewType", 2);
                raiseToSpeak = sharedPreferences.getBoolean("raise_to_speak", false);
                recordViaSco = sharedPreferences.getBoolean("record_via_sco", true);
                customTabs = sharedPreferences.getBoolean("custom_tabs", true);
                directShare = sharedPreferences.getBoolean("direct_share", true);
                boolean z2 = sharedPreferences.getBoolean("shuffleMusic", false);
                shuffleMusic = z2;
                playOrderReversed = z2 && sharedPreferences.getBoolean("playOrderReversed", false);
                inappCamera = sharedPreferences.getBoolean("inappCamera", true);
                hasCameraCache = sharedPreferences.contains("cameraCache");
                roundCamera16to9 = true;
                repeatMode = sharedPreferences.getInt("repeatMode", 0);
                fontSize = sharedPreferences.getInt("fons_size", !AndroidUtilities.isTablet() ? 18 : 16);
                bubbleRadius = sharedPreferences.getInt("bubbleRadius", 17);
                ivFontSize = sharedPreferences.getInt("iv_font_size", fontSize);
                allowBigEmoji = sharedPreferences.getBoolean("allowBigEmoji", true);
                useSystemEmoji = sharedPreferences.getBoolean("useSystemEmoji", false);
                streamMedia = sharedPreferences.getBoolean("streamMedia", true);
                saveStreamMedia = sharedPreferences.getBoolean("saveStreamMedia", true);
                smoothKeyboard = sharedPreferences.getBoolean("smoothKeyboard2", true);
                pauseMusicOnRecord = sharedPreferences.getBoolean("pauseMusicOnRecord", false);
                chatBlur = sharedPreferences.getBoolean("chatBlur", true);
                forceDisableTabletMode = sharedPreferences.getBoolean("forceDisableTabletMode", false);
                streamAllVideo = sharedPreferences.getBoolean("streamAllVideo", BuildVars.DEBUG_VERSION);
                streamMkv = sharedPreferences.getBoolean("streamMkv", false);
                suggestStickers = sharedPreferences.getInt("suggestStickers", 0);
                suggestAnimatedEmoji = sharedPreferences.getBoolean("suggestAnimatedEmoji", true);
                sortContactsByName = sharedPreferences.getBoolean("sortContactsByName", false);
                sortFilesByName = sharedPreferences.getBoolean("sortFilesByName", false);
                noSoundHintShowed = sharedPreferences.getBoolean("noSoundHintShowed", false);
                directShareHash = sharedPreferences.getString("directShareHash2", null);
                useThreeLinesLayout = sharedPreferences.getBoolean("useThreeLinesLayout", false);
                archiveHidden = sharedPreferences.getBoolean("archiveHidden", false);
                distanceSystemType = sharedPreferences.getInt("distanceSystemType", 0);
                devicePerformanceClass = sharedPreferences.getInt("devicePerformanceClass", -1);
                loopStickers = sharedPreferences.getBoolean("loopStickers", true);
                keepMedia = sharedPreferences.getInt("keep_media", 2);
                noStatusBar = sharedPreferences.getBoolean("noStatusBar", true);
                forceRtmpStream = sharedPreferences.getBoolean("forceRtmpStream", false);
                debugWebView = sharedPreferences.getBoolean("debugWebView", false);
                lastKeepMediaCheckTime = sharedPreferences.getInt("lastKeepMediaCheckTime", 0);
                lastLogsCheckTime = sharedPreferences.getInt("lastLogsCheckTime", 0);
                searchMessagesAsListHintShows = sharedPreferences.getInt("searchMessagesAsListHintShows", 0);
                searchMessagesAsListUsed = sharedPreferences.getBoolean("searchMessagesAsListUsed", false);
                stickersReorderingHintUsed = sharedPreferences.getBoolean("stickersReorderingHintUsed", false);
                textSelectionHintShows = sharedPreferences.getInt("textSelectionHintShows", 0);
                scheduledOrNoSoundHintShows = sharedPreferences.getInt("scheduledOrNoSoundHintShows", 0);
                forwardingOptionsHintShown = sharedPreferences.getBoolean("forwardingOptionsHintShown", false);
                lockRecordAudioVideoHint = sharedPreferences.getInt("lockRecordAudioVideoHint", 0);
                disableVoiceAudioEffects = sharedPreferences.getBoolean("disableVoiceAudioEffects", false);
                noiseSupression = sharedPreferences.getBoolean("noiseSupression", false);
                chatSwipeAction = sharedPreferences.getInt("ChatSwipeAction", -1);
                messageSeenHintCount = sharedPreferences.getInt("messageSeenCount", 3);
                emojiInteractionsHintCount = sharedPreferences.getInt("emojiInteractionsHintCount", 3);
                dayNightThemeSwitchHintCount = sharedPreferences.getInt("dayNightThemeSwitchHintCount", 3);
                mediaColumnsCount = sharedPreferences.getInt("mediaColumnsCount", 3);
                fastScrollHintCount = sharedPreferences.getInt("fastScrollHintCount", 3);
                dontAskManageStorage = sharedPreferences.getBoolean("dontAskManageStorage", false);
                hasEmailLogin = sharedPreferences.getBoolean("hasEmailLogin", false);
                showNotificationsForAllAccounts = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0).getBoolean("AllAccounts", true);
                configLoaded = true;
                try {
                    if (Build.VERSION.SDK_INT >= 19 && debugWebView) {
                        WebView.setWebContentsDebuggingEnabled(true);
                    }
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
            }
        }
    }

    public static void increaseBadPasscodeTries() {
        int i = badPasscodeTries + 1;
        badPasscodeTries = i;
        if (i >= 3) {
            if (i == 3) {
                passcodeRetryInMs = 5000L;
            } else if (i == 4) {
                passcodeRetryInMs = 10000L;
            } else if (i == 5) {
                passcodeRetryInMs = 15000L;
            } else if (i == 6) {
                passcodeRetryInMs = 20000L;
            } else if (i == 7) {
                passcodeRetryInMs = 25000L;
            } else {
                passcodeRetryInMs = 30000L;
            }
            lastUptimeMillis = SystemClock.elapsedRealtime();
        }
        saveConfig();
    }

    public static boolean isPassportConfigLoaded() {
        return passportConfigMap != null;
    }

    public static void setPassportConfig(String str, int i) {
        passportConfigMap = null;
        passportConfigJson = str;
        passportConfigHash = i;
        saveConfig();
        getCountryLangs();
    }

    public static HashMap<String, String> getCountryLangs() {
        if (passportConfigMap == null) {
            passportConfigMap = new HashMap<>();
            try {
                JSONObject jSONObject = new JSONObject(passportConfigJson);
                Iterator<String> keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    passportConfigMap.put(next.toUpperCase(), jSONObject.getString(next).toUpperCase());
                }
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        return passportConfigMap;
    }

    public static boolean isAppUpdateAvailable() {
        int i;
        TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate = pendingAppUpdate;
        if (tLRPC$TL_help_appUpdate == null || tLRPC$TL_help_appUpdate.document == null || !BuildVars.isStandaloneApp()) {
            return false;
        }
        try {
            i = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            FileLog.e(e);
            i = BuildVars.BUILD_VERSION;
        }
        return pendingAppUpdateBuildVersion == i;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0024  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0020  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean setNewAppVersionAvailable(TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate) {
        int i;
        String str;
        String str2;
        PackageInfo packageInfo;
        try {
            packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            i = packageInfo.versionCode;
        } catch (Exception e) {
            e = e;
            i = 0;
        }
        try {
            str = packageInfo.versionName;
        } catch (Exception e2) {
            e = e2;
            FileLog.e(e);
            str = null;
            if (i == 0) {
            }
            if (str == null) {
            }
            str2 = tLRPC$TL_help_appUpdate.version;
            if (str2 != null) {
            }
            return false;
        }
        if (i == 0) {
            i = BuildVars.BUILD_VERSION;
        }
        if (str == null) {
            str = BuildVars.BUILD_VERSION_STRING;
        }
        str2 = tLRPC$TL_help_appUpdate.version;
        if (str2 != null || str.compareTo(str2) >= 0) {
            return false;
        }
        pendingAppUpdate = tLRPC$TL_help_appUpdate;
        pendingAppUpdateBuildVersion = i;
        saveConfig();
        return true;
    }

    public static boolean checkPasscode(String str) {
        if (passcodeSalt.length == 0) {
            boolean equals = Utilities.MD5(str).equals(passcodeHash);
            if (equals) {
                try {
                    passcodeSalt = new byte[16];
                    Utilities.random.nextBytes(passcodeSalt);
                    byte[] bytes = str.getBytes("UTF-8");
                    int length = bytes.length + 32;
                    byte[] bArr = new byte[length];
                    System.arraycopy(passcodeSalt, 0, bArr, 0, 16);
                    System.arraycopy(bytes, 0, bArr, 16, bytes.length);
                    System.arraycopy(passcodeSalt, 0, bArr, bytes.length + 16, 16);
                    passcodeHash = Utilities.bytesToHex(Utilities.computeSHA256(bArr, 0, length));
                    saveConfig();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            return equals;
        }
        try {
            byte[] bytes2 = str.getBytes("UTF-8");
            int length2 = bytes2.length + 32;
            byte[] bArr2 = new byte[length2];
            System.arraycopy(passcodeSalt, 0, bArr2, 0, 16);
            System.arraycopy(bytes2, 0, bArr2, 16, bytes2.length);
            System.arraycopy(passcodeSalt, 0, bArr2, bytes2.length + 16, 16);
            return passcodeHash.equals(Utilities.bytesToHex(Utilities.computeSHA256(bArr2, 0, length2)));
        } catch (Exception e2) {
            FileLog.e(e2);
            return false;
        }
    }

    public static void clearConfig() {
        saveIncomingPhotos = false;
        appLocked = false;
        passcodeType = 0;
        passcodeRetryInMs = 0L;
        lastUptimeMillis = 0L;
        badPasscodeTries = 0;
        passcodeHash = "";
        passcodeSalt = new byte[0];
        autoLockIn = 3600;
        lastPauseTime = 0;
        useFingerprint = true;
        isWaitingForPasscodeEnter = false;
        allowScreenCapture = false;
        lastUpdateVersion = BuildVars.BUILD_VERSION_STRING;
        textSelectionHintShows = 0;
        scheduledOrNoSoundHintShows = 0;
        lockRecordAudioVideoHint = 0;
        forwardingOptionsHintShown = false;
        messageSeenHintCount = 3;
        emojiInteractionsHintCount = 3;
        dayNightThemeSwitchHintCount = 3;
        saveConfig();
    }

    public static void setSuggestStickers(int i) {
        suggestStickers = i;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("suggestStickers", suggestStickers);
        edit.commit();
    }

    public static void setSearchMessagesAsListUsed(boolean z) {
        searchMessagesAsListUsed = z;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("searchMessagesAsListUsed", searchMessagesAsListUsed);
        edit.commit();
    }

    public static void setStickersReorderingHintUsed(boolean z) {
        stickersReorderingHintUsed = z;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("stickersReorderingHintUsed", stickersReorderingHintUsed);
        edit.commit();
    }

    public static void increaseTextSelectionHintShowed() {
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        int i = textSelectionHintShows + 1;
        textSelectionHintShows = i;
        edit.putInt("textSelectionHintShows", i);
        edit.commit();
    }

    public static void removeTextSelectionHint() {
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("textSelectionHintShows", 3);
        edit.commit();
    }

    public static void increaseScheduledOrNoSuoundHintShowed() {
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        int i = scheduledOrNoSoundHintShows + 1;
        scheduledOrNoSoundHintShows = i;
        edit.putInt("scheduledOrNoSoundHintShows", i);
        edit.commit();
    }

    public static void forwardingOptionsHintHintShowed() {
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        forwardingOptionsHintShown = true;
        edit.putBoolean("forwardingOptionsHintShown", true);
        edit.commit();
    }

    public static void removeScheduledOrNoSoundHint() {
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("scheduledOrNoSoundHintShows", 3);
        edit.commit();
    }

    public static void increaseLockRecordAudioVideoHintShowed() {
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        int i = lockRecordAudioVideoHint + 1;
        lockRecordAudioVideoHint = i;
        edit.putInt("lockRecordAudioVideoHint", i);
        edit.commit();
    }

    public static void removeLockRecordAudioVideoHint() {
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("lockRecordAudioVideoHint", 3);
        edit.commit();
    }

    public static void increaseSearchAsListHintShows() {
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        int i = searchMessagesAsListHintShows + 1;
        searchMessagesAsListHintShows = i;
        edit.putInt("searchMessagesAsListHintShows", i);
        edit.commit();
    }

    public static void setKeepMedia(int i) {
        keepMedia = i;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("keep_media", keepMedia);
        edit.commit();
    }

    public static void checkLogsToDelete() {
        if (!BuildVars.LOGS_ENABLED) {
            return;
        }
        final int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (Math.abs(currentTimeMillis - lastLogsCheckTime) < 3600) {
            return;
        }
        lastLogsCheckTime = currentTimeMillis;
        Utilities.cacheClearQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SharedConfig$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SharedConfig.lambda$checkLogsToDelete$0(currentTimeMillis);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkLogsToDelete$0(int i) {
        long j = i - 864000;
        try {
            File externalFilesDir = ApplicationLoader.applicationContext.getExternalFilesDir(null);
            Utilities.clearDir(new File(externalFilesDir.getAbsolutePath() + "/logs").getAbsolutePath(), 0, j, false);
        } catch (Throwable th) {
            FileLog.e(th);
        }
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("lastLogsCheckTime", lastLogsCheckTime);
        edit.commit();
    }

    public static void checkKeepMedia() {
        final int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (Math.abs(currentTimeMillis - lastKeepMediaCheckTime) < 3600) {
            return;
        }
        lastKeepMediaCheckTime = currentTimeMillis;
        final File checkDirectory = FileLoader.checkDirectory(4);
        Utilities.cacheClearQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SharedConfig$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SharedConfig.lambda$checkKeepMedia$1(currentTimeMillis, checkDirectory);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkKeepMedia$1(int i, File file) {
        int i2 = keepMedia;
        if (i2 != 2) {
            long j = i - ((i2 == 0 ? 7 : i2 == 1 ? 30 : 3) * 86400);
            SparseArray<File> createMediaPaths = ImageLoader.getInstance().createMediaPaths();
            for (int i3 = 0; i3 < createMediaPaths.size(); i3++) {
                if (createMediaPaths.keyAt(i3) != 4) {
                    try {
                        Utilities.clearDir(createMediaPaths.valueAt(i3).getAbsolutePath(), 0, j, false);
                    } catch (Throwable th) {
                        FileLog.e(th);
                    }
                }
            }
        }
        File file2 = new File(file, "acache");
        if (file2.exists()) {
            try {
                Utilities.clearDir(file2.getAbsolutePath(), 0, i - 86400, false);
            } catch (Throwable th2) {
                FileLog.e(th2);
            }
        }
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("lastKeepMediaCheckTime", lastKeepMediaCheckTime);
        edit.commit();
    }

    public static void toggleDisableVoiceAudioEffects() {
        disableVoiceAudioEffects = !disableVoiceAudioEffects;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("disableVoiceAudioEffects", disableVoiceAudioEffects);
        edit.commit();
    }

    public static void toggleNoiseSupression() {
        noiseSupression = !noiseSupression;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("noiseSupression", noiseSupression);
        edit.commit();
    }

    public static void toggleForceRTMPStream() {
        forceRtmpStream = !forceRtmpStream;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("forceRtmpStream", forceRtmpStream);
        edit.apply();
    }

    public static void toggleDebugWebView() {
        boolean z = !debugWebView;
        debugWebView = z;
        if (Build.VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(z);
        }
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("debugWebView", debugWebView);
        edit.apply();
    }

    public static void toggleNoStatusBar() {
        noStatusBar = !noStatusBar;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("noStatusBar", noStatusBar);
        edit.commit();
    }

    public static void toggleLoopStickers() {
        loopStickers = !loopStickers;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("loopStickers", loopStickers);
        edit.commit();
    }

    public static void toggleBigEmoji() {
        allowBigEmoji = !allowBigEmoji;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("allowBigEmoji", allowBigEmoji);
        edit.commit();
    }

    public static void toggleSuggestAnimatedEmoji() {
        suggestAnimatedEmoji = !suggestAnimatedEmoji;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("suggestAnimatedEmoji", suggestAnimatedEmoji);
        edit.commit();
    }

    public static void setPlaybackOrderType(int i) {
        if (i == 2) {
            shuffleMusic = true;
            playOrderReversed = false;
        } else if (i == 1) {
            playOrderReversed = true;
            shuffleMusic = false;
        } else {
            playOrderReversed = false;
            shuffleMusic = false;
        }
        MediaController.getInstance().checkIsNextMediaFileDownloaded();
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("shuffleMusic", shuffleMusic);
        edit.putBoolean("playOrderReversed", playOrderReversed);
        edit.commit();
    }

    public static void setRepeatMode(int i) {
        repeatMode = i;
        if (i < 0 || i > 2) {
            repeatMode = 0;
        }
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("repeatMode", repeatMode);
        edit.commit();
    }

    public static void toggleSaveToGalleryFlag(int i) {
        int i2 = saveToGalleryFlags;
        if ((i2 & i) != 0) {
            saveToGalleryFlags = (i ^ (-1)) & i2;
        } else {
            saveToGalleryFlags = i | i2;
        }
        MessagesController.getGlobalMainSettings().edit().putInt("save_gallery_flags", saveToGalleryFlags).apply();
        ImageLoader.getInstance().checkMediaPaths();
        ImageLoader.getInstance().getCacheOutQueue().postRunnable(SharedConfig$$ExternalSyntheticLambda3.INSTANCE);
    }

    public static void toggleAutoplayGifs() {
        autoplayGifs = !autoplayGifs;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("autoplay_gif", autoplayGifs);
        edit.commit();
    }

    public static void setUseThreeLinesLayout(boolean z) {
        useThreeLinesLayout = z;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("useThreeLinesLayout", useThreeLinesLayout);
        edit.commit();
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.dialogsNeedReload, Boolean.TRUE);
    }

    public static void toggleArchiveHidden() {
        archiveHidden = !archiveHidden;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("archiveHidden", archiveHidden);
        edit.commit();
    }

    public static void toggleAutoplayVideo() {
        autoplayVideo = !autoplayVideo;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("autoplay_video", autoplayVideo);
        edit.commit();
    }

    public static boolean isSecretMapPreviewSet() {
        return MessagesController.getGlobalMainSettings().contains("mapPreviewType");
    }

    public static void setSecretMapPreviewType(int i) {
        mapPreviewType = i;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("mapPreviewType", mapPreviewType);
        edit.commit();
    }

    public static void setNoSoundHintShowed(boolean z) {
        if (noSoundHintShowed == z) {
            return;
        }
        noSoundHintShowed = z;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("noSoundHintShowed", noSoundHintShowed);
        edit.commit();
    }

    public static void toogleRaiseToSpeak() {
        raiseToSpeak = !raiseToSpeak;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("raise_to_speak", raiseToSpeak);
        edit.commit();
    }

    public static void toggleRecordViaSco() {
        recordViaSco = !recordViaSco;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("record_via_sco", recordViaSco);
        edit.commit();
    }

    public static void toggleCustomTabs() {
        customTabs = !customTabs;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("custom_tabs", customTabs);
        edit.commit();
    }

    public static void toggleDirectShare() {
        directShare = !directShare;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("direct_share", directShare);
        edit.commit();
        ShortcutManagerCompat.removeAllDynamicShortcuts(ApplicationLoader.applicationContext);
        MediaDataController.getInstance(UserConfig.selectedAccount).buildShortcuts();
    }

    public static void toggleStreamMedia() {
        streamMedia = !streamMedia;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("streamMedia", streamMedia);
        edit.commit();
    }

    public static void toggleSortContactsByName() {
        sortContactsByName = !sortContactsByName;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("sortContactsByName", sortContactsByName);
        edit.commit();
    }

    public static void toggleSortFilesByName() {
        sortFilesByName = !sortFilesByName;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("sortFilesByName", sortFilesByName);
        edit.commit();
    }

    public static void toggleStreamAllVideo() {
        streamAllVideo = !streamAllVideo;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("streamAllVideo", streamAllVideo);
        edit.commit();
    }

    public static void toggleStreamMkv() {
        streamMkv = !streamMkv;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("streamMkv", streamMkv);
        edit.commit();
    }

    public static void toggleSaveStreamMedia() {
        saveStreamMedia = !saveStreamMedia;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("saveStreamMedia", saveStreamMedia);
        edit.commit();
    }

    public static void toggleSmoothKeyboard() {
        smoothKeyboard = !smoothKeyboard;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("smoothKeyboard2", smoothKeyboard);
        edit.commit();
    }

    public static void togglePauseMusicOnRecord() {
        pauseMusicOnRecord = !pauseMusicOnRecord;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("pauseMusicOnRecord", pauseMusicOnRecord);
        edit.commit();
    }

    public static void toggleChatBlur() {
        chatBlur = !chatBlur;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("chatBlur", chatBlur);
        edit.commit();
    }

    public static void toggleForceDisableTabletMode() {
        forceDisableTabletMode = !forceDisableTabletMode;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("forceDisableTabletMode", forceDisableTabletMode);
        edit.commit();
    }

    public static void toggleInappCamera() {
        inappCamera = !inappCamera;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("inappCamera", inappCamera);
        edit.commit();
    }

    public static void toggleRoundCamera16to9() {
        roundCamera16to9 = !roundCamera16to9;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("roundCamera16to9", roundCamera16to9);
        edit.commit();
    }

    public static void setDistanceSystemType(int i) {
        distanceSystemType = i;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("distanceSystemType", distanceSystemType);
        edit.commit();
        LocaleController.resetImperialSystemType();
    }

    public static void loadProxyList() {
        if (proxyListLoaded) {
            return;
        }
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        String string = sharedPreferences.getString("proxy_ip", "");
        String string2 = sharedPreferences.getString("proxy_user", "");
        String string3 = sharedPreferences.getString("proxy_pass", "");
        String string4 = sharedPreferences.getString("proxy_secret", "");
        int i = sharedPreferences.getInt("proxy_port", 1080);
        proxyListLoaded = true;
        proxyList.clear();
        currentProxy = null;
        String string5 = sharedPreferences.getString("proxy_list", null);
        if (!TextUtils.isEmpty(string5)) {
            SerializedData serializedData = new SerializedData(Base64.decode(string5, 0));
            int readInt32 = serializedData.readInt32(false);
            for (int i2 = 0; i2 < readInt32; i2++) {
                ProxyInfo proxyInfo = new ProxyInfo(serializedData.readString(false), serializedData.readInt32(false), serializedData.readString(false), serializedData.readString(false), serializedData.readString(false));
                proxyList.add(proxyInfo);
                if (currentProxy == null && !TextUtils.isEmpty(string) && string.equals(proxyInfo.address) && i == proxyInfo.port && string2.equals(proxyInfo.username) && string3.equals(proxyInfo.password)) {
                    currentProxy = proxyInfo;
                }
            }
            serializedData.cleanup();
        }
        if (currentProxy != null || TextUtils.isEmpty(string)) {
            return;
        }
        ProxyInfo proxyInfo2 = new ProxyInfo(string, i, string2, string3, string4);
        currentProxy = proxyInfo2;
        proxyList.add(0, proxyInfo2);
    }

    public static void saveProxyList() {
        SerializedData serializedData = new SerializedData();
        int size = proxyList.size();
        serializedData.writeInt32(size);
        for (int i = 0; i < size; i++) {
            ProxyInfo proxyInfo = proxyList.get(i);
            String str = proxyInfo.address;
            String str2 = "";
            if (str == null) {
                str = str2;
            }
            serializedData.writeString(str);
            serializedData.writeInt32(proxyInfo.port);
            String str3 = proxyInfo.username;
            if (str3 == null) {
                str3 = str2;
            }
            serializedData.writeString(str3);
            String str4 = proxyInfo.password;
            if (str4 == null) {
                str4 = str2;
            }
            serializedData.writeString(str4);
            String str5 = proxyInfo.secret;
            if (str5 != null) {
                str2 = str5;
            }
            serializedData.writeString(str2);
        }
        ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit().putString("proxy_list", Base64.encodeToString(serializedData.toByteArray(), 2)).commit();
        serializedData.cleanup();
    }

    public static ProxyInfo addProxy(ProxyInfo proxyInfo) {
        loadProxyList();
        int size = proxyList.size();
        for (int i = 0; i < size; i++) {
            ProxyInfo proxyInfo2 = proxyList.get(i);
            if (proxyInfo.address.equals(proxyInfo2.address) && proxyInfo.port == proxyInfo2.port && proxyInfo.username.equals(proxyInfo2.username) && proxyInfo.password.equals(proxyInfo2.password) && proxyInfo.secret.equals(proxyInfo2.secret)) {
                return proxyInfo2;
            }
        }
        proxyList.add(proxyInfo);
        saveProxyList();
        return proxyInfo;
    }

    public static void deleteProxy(ProxyInfo proxyInfo) {
        if (currentProxy == proxyInfo) {
            currentProxy = null;
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            boolean z = globalMainSettings.getBoolean("proxy_enabled", false);
            SharedPreferences.Editor edit = globalMainSettings.edit();
            edit.putString("proxy_ip", "");
            edit.putString("proxy_pass", "");
            edit.putString("proxy_user", "");
            edit.putString("proxy_secret", "");
            edit.putInt("proxy_port", 1080);
            edit.putBoolean("proxy_enabled", false);
            edit.putBoolean("proxy_enabled_calls", false);
            edit.commit();
            if (z) {
                ConnectionsManager.setProxySettings(false, "", 0, "", "", "");
            }
        }
        proxyList.remove(proxyInfo);
        saveProxyList();
    }

    public static void checkSaveToGalleryFiles() {
        Utilities.globalQueue.postRunnable(SharedConfig$$ExternalSyntheticLambda2.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkSaveToGalleryFiles$3() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "Telegram");
            File file2 = new File(file, "Telegram Images");
            file2.mkdir();
            File file3 = new File(file, "Telegram Video");
            file3.mkdir();
            if (saveToGalleryFlags == 0 && BuildVars.NO_SCOPED_STORAGE) {
                if (file2.isDirectory()) {
                    AndroidUtilities.createEmptyFile(new File(file2, ".nomedia"));
                }
                if (!file3.isDirectory()) {
                    return;
                }
                AndroidUtilities.createEmptyFile(new File(file3, ".nomedia"));
                return;
            }
            if (file2.isDirectory()) {
                new File(file2, ".nomedia").delete();
            }
            if (!file3.isDirectory()) {
                return;
            }
            new File(file3, ".nomedia").delete();
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public static int getChatSwipeAction(int i) {
        int i2 = chatSwipeAction;
        if (i2 < 0) {
            return !MessagesController.getInstance(i).dialogFilters.isEmpty() ? 5 : 2;
        } else if (i2 == 5 && MessagesController.getInstance(i).dialogFilters.isEmpty()) {
            return 2;
        } else {
            return chatSwipeAction;
        }
    }

    public static void updateChatListSwipeSetting(int i) {
        chatSwipeAction = i;
        ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit().putInt("ChatSwipeAction", chatSwipeAction).apply();
    }

    public static void updateMessageSeenHintCount(int i) {
        messageSeenHintCount = i;
        ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit().putInt("messageSeenCount", messageSeenHintCount).apply();
    }

    public static void updateEmojiInteractionsHintCount(int i) {
        emojiInteractionsHintCount = i;
        ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit().putInt("emojiInteractionsHintCount", emojiInteractionsHintCount).apply();
    }

    public static void updateDayNightThemeSwitchHintCount(int i) {
        dayNightThemeSwitchHintCount = i;
        ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit().putInt("dayNightThemeSwitchHintCount", dayNightThemeSwitchHintCount).apply();
    }

    public static int getDevicePerformanceClass() {
        if (devicePerformanceClass == -1) {
            int i = Build.VERSION.SDK_INT;
            int i2 = ConnectionsManager.CPU_COUNT;
            int memoryClass = ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).getMemoryClass();
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < i2; i5++) {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(String.format(Locale.ENGLISH, "/sys/devices/system/cpu/cpu%d/cpufreq/cpuinfo_max_freq", Integer.valueOf(i5)), "r");
                    String readLine = randomAccessFile.readLine();
                    if (readLine != null) {
                        i4 += Utilities.parseInt((CharSequence) readLine).intValue() / 1000;
                        i3++;
                    }
                    randomAccessFile.close();
                } catch (Throwable unused) {
                }
            }
            int ceil = i3 == 0 ? -1 : (int) Math.ceil(i4 / i3);
            if (i < 21 || i2 <= 2 || memoryClass <= 100 || ((i2 <= 4 && ceil != -1 && ceil <= 1250) || ((i2 <= 4 && ceil <= 1600 && memoryClass <= 128 && i <= 21) || (i2 <= 4 && ceil <= 1300 && memoryClass <= 128 && i <= 24)))) {
                devicePerformanceClass = 0;
            } else if (i2 < 8 || memoryClass <= 160 || ((ceil != -1 && ceil <= 2050) || (ceil == -1 && i2 == 8 && i <= 23))) {
                devicePerformanceClass = 1;
            } else {
                devicePerformanceClass = 2;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("device performance info selected_class = " + devicePerformanceClass + " (cpu_count = " + i2 + ", freq = " + ceil + ", memoryClass = " + memoryClass + ", android version " + i + ")");
            }
        }
        return devicePerformanceClass;
    }

    public static void setMediaColumnsCount(int i) {
        if (mediaColumnsCount != i) {
            mediaColumnsCount = i;
            ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit().putInt("mediaColumnsCount", mediaColumnsCount).apply();
        }
    }

    public static void setFastScrollHintCount(int i) {
        if (fastScrollHintCount != i) {
            fastScrollHintCount = i;
            ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit().putInt("fastScrollHintCount", fastScrollHintCount).apply();
        }
    }

    public static void setDontAskManageStorage(boolean z) {
        dontAskManageStorage = z;
        ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit().putBoolean("dontAskManageStorage", dontAskManageStorage).apply();
    }

    public static boolean canBlurChat() {
        return getDevicePerformanceClass() == 2;
    }

    public static boolean chatBlurEnabled() {
        return canBlurChat() && chatBlur;
    }

    /* loaded from: classes.dex */
    public static class BackgroundActivityPrefs {
        private static SharedPreferences prefs;

        public static long getLastCheckedBackgroundActivity() {
            return prefs.getLong("last_checked", 0L);
        }

        public static void setLastCheckedBackgroundActivity(long j) {
            prefs.edit().putLong("last_checked", j).apply();
        }
    }

    public static void setAnimationsEnabled(boolean z) {
        animationsEnabled = Boolean.valueOf(z);
    }

    public static boolean animationsEnabled() {
        if (animationsEnabled == null) {
            animationsEnabled = Boolean.valueOf(MessagesController.getGlobalMainSettings().getBoolean("view_animations", true));
        }
        return animationsEnabled.booleanValue();
    }
}