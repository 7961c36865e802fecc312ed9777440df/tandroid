package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.Pair;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.telegram.messenger.NotificationBadge;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ResultCallback;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$TL_account_getChatThemes;
import org.telegram.tgnet.TLRPC$TL_account_themes;
import org.telegram.tgnet.TLRPC$TL_account_themesNotModified;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messageActionSetChatWallPaper;
import org.telegram.tgnet.TLRPC$TL_messages_setChatTheme;
import org.telegram.tgnet.TLRPC$TL_messages_setChatWallPaper;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_theme;
import org.telegram.tgnet.TLRPC$TL_updateNewMessage;
import org.telegram.tgnet.TLRPC$TL_updatePeerWallpaper;
import org.telegram.tgnet.TLRPC$TL_wallPaper;
import org.telegram.tgnet.TLRPC$TL_wallPaperNoFile;
import org.telegram.tgnet.TLRPC$TL_wallPaperSettings;
import org.telegram.tgnet.TLRPC$Theme;
import org.telegram.tgnet.TLRPC$Updates;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$WallPaper;
import org.telegram.tgnet.TLRPC$WallPaperSettings;
import org.telegram.ui.ActionBar.EmojiThemes;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatBackgroundDrawable;
/* loaded from: classes.dex */
public class ChatThemeController extends BaseController {
    public static volatile DispatchQueue chatThemeQueue = new DispatchQueue("chatThemeQueue");
    private static final ChatThemeController[] instances = new ChatThemeController[4];
    private List<EmojiThemes> allChatThemes;
    private final LongSparseArray<String> dialogEmoticonsMap;
    private volatile long lastReloadTimeMs;
    private final long reloadTimeoutMs;
    private final HashMap<Long, Bitmap> themeIdWallpaperThumbMap;
    private volatile long themesHash;

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$clearWallpaper$9(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public void clearWallpaperImages() {
    }

    private ChatThemeController(int i) {
        super(i);
        this.reloadTimeoutMs = 7200000L;
        this.themeIdWallpaperThumbMap = new HashMap<>();
        this.dialogEmoticonsMap = new LongSparseArray<>();
        init();
    }

    private void init() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        this.themesHash = 0L;
        this.lastReloadTimeMs = 0L;
        try {
            this.themesHash = sharedPreferences.getLong("hash", 0L);
            this.lastReloadTimeMs = sharedPreferences.getLong("lastReload", 0L);
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.allChatThemes = getAllChatThemesFromPrefs();
        preloadSticker("❌");
        if (this.allChatThemes.isEmpty()) {
            return;
        }
        for (EmojiThemes emojiThemes : this.allChatThemes) {
            preloadSticker(emojiThemes.getEmoticon());
        }
    }

    private void preloadSticker(String str) {
        new ImageReceiver().setImage(ImageLocation.getForDocument(MediaDataController.getInstance(UserConfig.selectedAccount).getEmojiAnimatedSticker(str)), "50_50", null, null, null, 0);
        Emoji.preloadEmoji(str);
    }

    public void requestAllChatThemes(final ResultCallback<List<EmojiThemes>> resultCallback, final boolean z) {
        if (this.themesHash == 0 || this.lastReloadTimeMs == 0) {
            init();
        }
        boolean z2 = System.currentTimeMillis() - this.lastReloadTimeMs > 7200000;
        List<EmojiThemes> list = this.allChatThemes;
        if (list == null || list.isEmpty() || z2) {
            TLRPC$TL_account_getChatThemes tLRPC$TL_account_getChatThemes = new TLRPC$TL_account_getChatThemes();
            tLRPC$TL_account_getChatThemes.hash = this.themesHash;
            ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tLRPC$TL_account_getChatThemes, new RequestDelegate() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda9
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    ChatThemeController.this.lambda$requestAllChatThemes$3(resultCallback, z, tLObject, tLRPC$TL_error);
                }
            });
        }
        List<EmojiThemes> list2 = this.allChatThemes;
        if (list2 == null || list2.isEmpty()) {
            return;
        }
        ArrayList<EmojiThemes> arrayList = new ArrayList(this.allChatThemes);
        if (z && !arrayList.get(0).showAsDefaultStub) {
            arrayList.add(0, EmojiThemes.createChatThemesDefault(this.currentAccount));
        }
        for (EmojiThemes emojiThemes : arrayList) {
            emojiThemes.initColors();
        }
        resultCallback.onComplete(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestAllChatThemes$3(final ResultCallback resultCallback, final boolean z, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        chatThemeQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeController.this.lambda$requestAllChatThemes$2(tLObject, resultCallback, tLRPC$TL_error, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:15:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$requestAllChatThemes$2(TLObject tLObject, final ResultCallback resultCallback, final TLRPC$TL_error tLRPC$TL_error, boolean z) {
        final List<EmojiThemes> list;
        boolean z2;
        if (tLObject instanceof TLRPC$TL_account_themes) {
            TLRPC$TL_account_themes tLRPC$TL_account_themes = (TLRPC$TL_account_themes) tLObject;
            this.themesHash = tLRPC$TL_account_themes.hash;
            this.lastReloadTimeMs = System.currentTimeMillis();
            SharedPreferences.Editor edit = getSharedPreferences().edit();
            edit.clear();
            edit.putLong("hash", this.themesHash);
            edit.putLong("lastReload", this.lastReloadTimeMs);
            edit.putInt(NotificationBadge.NewHtcHomeBadger.COUNT, tLRPC$TL_account_themes.themes.size());
            list = new ArrayList<>(tLRPC$TL_account_themes.themes.size());
            for (int i = 0; i < tLRPC$TL_account_themes.themes.size(); i++) {
                TLRPC$TL_theme tLRPC$TL_theme = tLRPC$TL_account_themes.themes.get(i);
                Emoji.preloadEmoji(tLRPC$TL_theme.emoticon);
                SerializedData serializedData = new SerializedData(tLRPC$TL_theme.getObjectSize());
                tLRPC$TL_theme.serializeToStream(serializedData);
                edit.putString("theme_" + i, Utilities.bytesToHex(serializedData.toByteArray()));
                EmojiThemes emojiThemes = new EmojiThemes(this.currentAccount, tLRPC$TL_theme, false);
                emojiThemes.preloadWallpaper();
                list.add(emojiThemes);
            }
            edit.apply();
        } else if (tLObject instanceof TLRPC$TL_account_themesNotModified) {
            list = getAllChatThemesFromPrefs();
        } else {
            list = null;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    ResultCallback.this.onError(tLRPC$TL_error);
                }
            });
            z2 = true;
            if (z2) {
                if (z && !list.get(0).showAsDefaultStub) {
                    list.add(0, EmojiThemes.createChatThemesDefault(this.currentAccount));
                }
                for (EmojiThemes emojiThemes2 : list) {
                    emojiThemes2.initColors();
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatThemeController.this.lambda$requestAllChatThemes$1(list, resultCallback);
                    }
                });
                return;
            }
            return;
        }
        z2 = false;
        if (z2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestAllChatThemes$1(List list, ResultCallback resultCallback) {
        this.allChatThemes = new ArrayList(list);
        resultCallback.onComplete(list);
    }

    private SharedPreferences getSharedPreferences() {
        Context context = ApplicationLoader.applicationContext;
        return context.getSharedPreferences("chatthemeconfig_" + this.currentAccount, 0);
    }

    private SharedPreferences getEmojiSharedPreferences() {
        return ApplicationLoader.applicationContext.getSharedPreferences("chatthemeconfig_emoji", 0);
    }

    private List<EmojiThemes> getAllChatThemesFromPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        int i = sharedPreferences.getInt(NotificationBadge.NewHtcHomeBadger.COUNT, 0);
        ArrayList arrayList = new ArrayList(i);
        for (int i2 = 0; i2 < i; i2++) {
            SerializedData serializedData = new SerializedData(Utilities.hexToBytes(sharedPreferences.getString("theme_" + i2, "")));
            try {
                TLRPC$TL_theme TLdeserialize = TLRPC$Theme.TLdeserialize(serializedData, serializedData.readInt32(true), true);
                if (TLdeserialize != null) {
                    arrayList.add(new EmojiThemes(this.currentAccount, TLdeserialize, false));
                }
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        return arrayList;
    }

    public void requestChatTheme(final String str, final ResultCallback<EmojiThemes> resultCallback) {
        if (TextUtils.isEmpty(str)) {
            resultCallback.onComplete(null);
        } else {
            requestAllChatThemes(new ResultCallback<List<EmojiThemes>>() { // from class: org.telegram.messenger.ChatThemeController.1
                public /* bridge */ /* synthetic */ void onError(Throwable th) {
                    ResultCallback.-CC.$default$onError(this, th);
                }

                @Override // org.telegram.tgnet.ResultCallback
                public void onComplete(List<EmojiThemes> list) {
                    for (EmojiThemes emojiThemes : list) {
                        if (str.equals(emojiThemes.getEmoticon())) {
                            emojiThemes.initColors();
                            resultCallback.onComplete(emojiThemes);
                            return;
                        }
                    }
                }

                @Override // org.telegram.tgnet.ResultCallback
                public void onError(TLRPC$TL_error tLRPC$TL_error) {
                    resultCallback.onComplete(null);
                }
            }, false);
        }
    }

    public static ChatThemeController getInstance(int i) {
        ChatThemeController[] chatThemeControllerArr = instances;
        ChatThemeController chatThemeController = chatThemeControllerArr[i];
        if (chatThemeController == null) {
            synchronized (ChatThemeController.class) {
                chatThemeController = chatThemeControllerArr[i];
                if (chatThemeController == null) {
                    chatThemeController = new ChatThemeController(i);
                    chatThemeControllerArr[i] = chatThemeController;
                }
            }
        }
        return chatThemeController;
    }

    public static boolean equals(TLRPC$WallPaper tLRPC$WallPaper, TLRPC$WallPaper tLRPC$WallPaper2) {
        if (tLRPC$WallPaper == null && tLRPC$WallPaper2 == null) {
            return true;
        }
        if (tLRPC$WallPaper == null || tLRPC$WallPaper2 == null) {
            return false;
        }
        String str = tLRPC$WallPaper.uploadingImage;
        if (str != null) {
            return TextUtils.equals(tLRPC$WallPaper2.uploadingImage, str);
        }
        return tLRPC$WallPaper.id == tLRPC$WallPaper2.id && TextUtils.equals(ChatBackgroundDrawable.hash(tLRPC$WallPaper.settings), ChatBackgroundDrawable.hash(tLRPC$WallPaper2.settings));
    }

    public void setDialogTheme(long j, String str, boolean z) {
        if (TextUtils.equals(this.dialogEmoticonsMap.get(j), str)) {
            return;
        }
        if (str == null) {
            this.dialogEmoticonsMap.delete(j);
        } else {
            this.dialogEmoticonsMap.put(j, str);
        }
        if (j >= 0) {
            TLRPC$UserFull userFull = getMessagesController().getUserFull(j);
            if (userFull != null) {
                userFull.theme_emoticon = str;
                getMessagesStorage().updateUserInfo(userFull, true);
            }
        } else {
            TLRPC$ChatFull chatFull = getMessagesController().getChatFull(-j);
            if (chatFull != null) {
                chatFull.theme_emoticon = str;
                getMessagesStorage().updateChatInfo(chatFull, true);
            }
        }
        SharedPreferences.Editor edit = getEmojiSharedPreferences().edit();
        edit.putString("chatTheme_" + this.currentAccount + "_" + j, str).apply();
        if (z) {
            TLRPC$TL_messages_setChatTheme tLRPC$TL_messages_setChatTheme = new TLRPC$TL_messages_setChatTheme();
            if (str == null) {
                str = "";
            }
            tLRPC$TL_messages_setChatTheme.emoticon = str;
            tLRPC$TL_messages_setChatTheme.peer = getMessagesController().getInputPeer(j);
            getConnectionsManager().sendRequest(tLRPC$TL_messages_setChatTheme, null);
        }
    }

    public EmojiThemes getDialogTheme(long j) {
        String str = this.dialogEmoticonsMap.get(j);
        if (str == null) {
            SharedPreferences emojiSharedPreferences = getEmojiSharedPreferences();
            str = emojiSharedPreferences.getString("chatTheme_" + this.currentAccount + "_" + j, null);
            this.dialogEmoticonsMap.put(j, str);
        }
        if (str != null) {
            for (EmojiThemes emojiThemes : this.allChatThemes) {
                if (str.equals(emojiThemes.getEmoticon())) {
                    return emojiThemes;
                }
            }
        }
        return null;
    }

    public void saveChatWallpaper(long j, TLRPC$WallPaper tLRPC$WallPaper) {
        if (j < 0) {
            return;
        }
        if (tLRPC$WallPaper != null) {
            SerializedData serializedData = new SerializedData(tLRPC$WallPaper.getObjectSize());
            tLRPC$WallPaper.serializeToStream(serializedData);
            String bytesToHex = Utilities.bytesToHex(serializedData.toByteArray());
            SharedPreferences.Editor edit = getEmojiSharedPreferences().edit();
            edit.putString("chatWallpaper_" + this.currentAccount + "_" + j, bytesToHex).apply();
            return;
        }
        SharedPreferences.Editor edit2 = getEmojiSharedPreferences().edit();
        edit2.remove("chatWallpaper_" + this.currentAccount + "_" + j).apply();
    }

    public TLRPC$WallPaper getDialogWallpaper(long j) {
        if (j < 0) {
            return null;
        }
        TLRPC$UserFull userFull = getMessagesController().getUserFull(j);
        if (userFull != null) {
            return userFull.wallpaper;
        }
        SharedPreferences emojiSharedPreferences = getEmojiSharedPreferences();
        String string = emojiSharedPreferences.getString("chatWallpaper_" + this.currentAccount + "_" + j, null);
        if (string != null) {
            SerializedData serializedData = new SerializedData(Utilities.hexToBytes(string));
            try {
                return TLRPC$WallPaper.TLdeserialize(serializedData, serializedData.readInt32(true), true);
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        return null;
    }

    public void preloadAllWallpaperImages(boolean z) {
        for (EmojiThemes emojiThemes : this.allChatThemes) {
            TLRPC$TL_theme tlTheme = emojiThemes.getTlTheme(z ? 1 : 0);
            if (tlTheme != null && !getPatternFile(tlTheme.id).exists()) {
                emojiThemes.loadWallpaper(z ? 1 : 0, null);
            }
        }
    }

    public void preloadAllWallpaperThumbs(boolean z) {
        for (EmojiThemes emojiThemes : this.allChatThemes) {
            TLRPC$TL_theme tlTheme = emojiThemes.getTlTheme(z ? 1 : 0);
            if (tlTheme != null) {
                if (!this.themeIdWallpaperThumbMap.containsKey(Long.valueOf(tlTheme.id))) {
                    emojiThemes.loadWallpaperThumb(z ? 1 : 0, new ResultCallback() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda11
                        @Override // org.telegram.tgnet.ResultCallback
                        public final void onComplete(Object obj) {
                            ChatThemeController.this.lambda$preloadAllWallpaperThumbs$4((Pair) obj);
                        }

                        @Override // org.telegram.tgnet.ResultCallback
                        public /* synthetic */ void onError(TLRPC$TL_error tLRPC$TL_error) {
                            ResultCallback.-CC.$default$onError(this, tLRPC$TL_error);
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$preloadAllWallpaperThumbs$4(Pair pair) {
        if (pair != null) {
            this.themeIdWallpaperThumbMap.put((Long) pair.first, (Bitmap) pair.second);
        }
    }

    public void clearWallpaperThumbImages() {
        this.themeIdWallpaperThumbMap.clear();
    }

    public void getWallpaperBitmap(long j, final ResultCallback<Bitmap> resultCallback) {
        if (this.themesHash == 0) {
            resultCallback.onComplete(null);
            return;
        }
        final File patternFile = getPatternFile(j);
        chatThemeQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeController.lambda$getWallpaperBitmap$6(patternFile, resultCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getWallpaperBitmap$6(File file, final ResultCallback resultCallback) {
        final Bitmap bitmap = null;
        try {
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (resultCallback != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    ResultCallback.this.onComplete(bitmap);
                }
            });
        }
    }

    private File getPatternFile(long j) {
        return new File(ApplicationLoader.getFilesDirFixed(), String.format(Locale.US, "%d_%d.jpg", Long.valueOf(j), Long.valueOf(this.themesHash)));
    }

    public void saveWallpaperBitmap(final Bitmap bitmap, long j) {
        final File patternFile = getPatternFile(j);
        chatThemeQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeController.lambda$saveWallpaperBitmap$7(patternFile, bitmap);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$saveWallpaperBitmap$7(File file, Bitmap bitmap) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 87, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public Bitmap getWallpaperThumbBitmap(long j) {
        return this.themeIdWallpaperThumbMap.get(Long.valueOf(j));
    }

    public void clearCache() {
        this.themesHash = 0L;
        this.lastReloadTimeMs = 0L;
        getSharedPreferences().edit().clear().apply();
    }

    public void processUpdate(TLRPC$TL_updatePeerWallpaper tLRPC$TL_updatePeerWallpaper) {
        final TLRPC$UserFull userFull;
        if (!(tLRPC$TL_updatePeerWallpaper.peer instanceof TLRPC$TL_peerUser) || (userFull = getMessagesController().getUserFull(tLRPC$TL_updatePeerWallpaper.peer.user_id)) == null || wallpaperEquals(userFull.wallpaper, tLRPC$TL_updatePeerWallpaper.wallpaper)) {
            return;
        }
        final long j = userFull.id;
        userFull.wallpaper_overridden = tLRPC$TL_updatePeerWallpaper.wallpaper_overridden;
        userFull.wallpaper = tLRPC$TL_updatePeerWallpaper.wallpaper;
        userFull.flags |= ConnectionsManager.FileTypePhoto;
        getMessagesStorage().updateUserInfo(userFull, false);
        saveChatWallpaper(j, null);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeController.this.lambda$processUpdate$8(j, userFull);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processUpdate$8(long j, TLRPC$UserFull tLRPC$UserFull) {
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.userInfoDidLoad, Long.valueOf(j), tLRPC$UserFull);
    }

    public static boolean wallpaperEquals(TLRPC$WallPaper tLRPC$WallPaper, TLRPC$WallPaper tLRPC$WallPaper2) {
        if ((tLRPC$WallPaper == null || (tLRPC$WallPaper instanceof TLRPC$TL_wallPaperNoFile)) && (tLRPC$WallPaper2 == null || (tLRPC$WallPaper2 instanceof TLRPC$TL_wallPaperNoFile))) {
            return true;
        }
        return (tLRPC$WallPaper instanceof TLRPC$TL_wallPaper) && (tLRPC$WallPaper2 instanceof TLRPC$TL_wallPaper) && tLRPC$WallPaper.id == tLRPC$WallPaper2.id;
    }

    public void clearWallpaper(long j, boolean z) {
        clearWallpaper(j, z, false);
    }

    public void clearWallpaper(long j, boolean z, boolean z2) {
        TLRPC$TL_messages_setChatWallPaper tLRPC$TL_messages_setChatWallPaper = new TLRPC$TL_messages_setChatWallPaper();
        if (j >= 0) {
            tLRPC$TL_messages_setChatWallPaper.peer = MessagesController.getInputPeer(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j)));
            tLRPC$TL_messages_setChatWallPaper.revert = z2;
            if (!z2) {
                TLRPC$UserFull userFull = getMessagesController().getUserFull(j);
                if (userFull != null) {
                    userFull.wallpaper = null;
                    userFull.flags &= -16777217;
                    getMessagesStorage().updateUserInfo(userFull, false);
                }
                saveChatWallpaper(j, null);
                if (z) {
                    NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.userInfoDidLoad, Long.valueOf(j), userFull);
                }
            }
        } else {
            tLRPC$TL_messages_setChatWallPaper.peer = MessagesController.getInputPeer(MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j)));
        }
        getConnectionsManager().sendRequest(tLRPC$TL_messages_setChatWallPaper, ChatThemeController$$ExternalSyntheticLambda10.INSTANCE);
    }

    public int setWallpaperToUser(final long j, final String str, Theme.OverrideWallpaperInfo overrideWallpaperInfo, MessageObject messageObject, final Runnable runnable) {
        String str2;
        TLRPC$TL_messages_setChatWallPaper tLRPC$TL_messages_setChatWallPaper = new TLRPC$TL_messages_setChatWallPaper();
        if (j > 0) {
            tLRPC$TL_messages_setChatWallPaper.peer = MessagesController.getInputPeer(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j)));
        } else {
            tLRPC$TL_messages_setChatWallPaper.peer = MessagesController.getInputPeer(MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j)));
        }
        tLRPC$TL_messages_setChatWallPaper.for_both = overrideWallpaperInfo.forBoth;
        final boolean z = false;
        if (messageObject != null && (messageObject.messageOwner.action instanceof TLRPC$TL_messageActionSetChatWallPaper)) {
            tLRPC$TL_messages_setChatWallPaper.flags |= 2;
            tLRPC$TL_messages_setChatWallPaper.id = messageObject.getId();
            TLRPC$UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(j);
            if (userFull != null) {
                TLRPC$TL_messageActionSetChatWallPaper tLRPC$TL_messageActionSetChatWallPaper = (TLRPC$TL_messageActionSetChatWallPaper) messageObject.messageOwner.action;
                TLRPC$TL_wallPaper tLRPC$TL_wallPaper = new TLRPC$TL_wallPaper();
                TLRPC$WallPaper tLRPC$WallPaper = tLRPC$TL_messageActionSetChatWallPaper.wallpaper;
                tLRPC$TL_wallPaper.id = tLRPC$WallPaper.id;
                tLRPC$TL_wallPaper.document = tLRPC$WallPaper.document;
                TLRPC$TL_wallPaperSettings tLRPC$TL_wallPaperSettings = new TLRPC$TL_wallPaperSettings();
                tLRPC$TL_wallPaper.settings = tLRPC$TL_wallPaperSettings;
                tLRPC$TL_wallPaperSettings.intensity = (int) (overrideWallpaperInfo.intensity * 100.0f);
                tLRPC$TL_wallPaperSettings.motion = overrideWallpaperInfo.isMotion;
                tLRPC$TL_wallPaperSettings.blur = overrideWallpaperInfo.isBlurred;
                tLRPC$TL_wallPaperSettings.background_color = overrideWallpaperInfo.color;
                tLRPC$TL_wallPaperSettings.second_background_color = overrideWallpaperInfo.gradientColor1;
                tLRPC$TL_wallPaperSettings.third_background_color = overrideWallpaperInfo.gradientColor2;
                tLRPC$TL_wallPaperSettings.fourth_background_color = overrideWallpaperInfo.gradientColor3;
                tLRPC$TL_wallPaperSettings.rotation = overrideWallpaperInfo.rotation;
                tLRPC$TL_wallPaper.uploadingImage = str;
                TLRPC$WallPaper tLRPC$WallPaper2 = userFull.wallpaper;
                if (tLRPC$WallPaper2 != null && (str2 = tLRPC$WallPaper2.uploadingImage) != null && str2.equals(str)) {
                    tLRPC$TL_wallPaper.stripedThumb = userFull.wallpaper.stripedThumb;
                }
                TLRPC$WallPaperSettings tLRPC$WallPaperSettings = tLRPC$TL_wallPaper.settings;
                int i = tLRPC$WallPaperSettings.flags | 1;
                tLRPC$WallPaperSettings.flags = i;
                int i2 = i | 8;
                tLRPC$WallPaperSettings.flags = i2;
                int i3 = i2 | 16;
                tLRPC$WallPaperSettings.flags = i3;
                int i4 = i3 | 32;
                tLRPC$WallPaperSettings.flags = i4;
                tLRPC$WallPaperSettings.flags = i4 | 64;
                TLRPC$TL_wallPaper tLRPC$TL_wallPaper2 = new TLRPC$TL_wallPaper();
                userFull.wallpaper = tLRPC$TL_wallPaper2;
                TLRPC$WallPaper tLRPC$WallPaper3 = tLRPC$TL_messageActionSetChatWallPaper.wallpaper;
                tLRPC$TL_wallPaper2.pattern = tLRPC$WallPaper3.pattern;
                tLRPC$TL_wallPaper2.id = tLRPC$WallPaper3.id;
                tLRPC$TL_wallPaper2.document = tLRPC$WallPaper3.document;
                int i5 = tLRPC$WallPaper3.flags;
                tLRPC$TL_wallPaper2.flags = i5;
                tLRPC$TL_wallPaper2.creator = tLRPC$WallPaper3.creator;
                tLRPC$TL_wallPaper2.dark = tLRPC$WallPaper3.dark;
                tLRPC$TL_wallPaper2.isDefault = tLRPC$WallPaper3.isDefault;
                tLRPC$TL_wallPaper2.slug = tLRPC$WallPaper3.slug;
                tLRPC$TL_wallPaper2.access_hash = tLRPC$WallPaper3.access_hash;
                tLRPC$TL_wallPaper2.stripedThumb = tLRPC$WallPaper3.stripedThumb;
                tLRPC$TL_wallPaper2.settings = tLRPC$TL_wallPaper.settings;
                tLRPC$TL_wallPaper2.flags = i5 | 4;
                userFull.flags |= ConnectionsManager.FileTypePhoto;
                getMessagesStorage().updateUserInfo(userFull, false);
                NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.userInfoDidLoad, Long.valueOf(j), userFull);
                if (runnable != null) {
                    runnable.run();
                }
            }
        } else {
            tLRPC$TL_messages_setChatWallPaper.flags |= 1;
            tLRPC$TL_messages_setChatWallPaper.wallpaper = MessagesController.getInputWallpaper(overrideWallpaperInfo);
            z = true;
        }
        tLRPC$TL_messages_setChatWallPaper.flags |= 4;
        tLRPC$TL_messages_setChatWallPaper.settings = MessagesController.getWallpaperSetting(overrideWallpaperInfo);
        return ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_setChatWallPaper, new RequestDelegate() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda8
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                ChatThemeController.this.lambda$setWallpaperToUser$11(j, z, str, runnable, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setWallpaperToUser$11(final long j, final boolean z, final String str, final Runnable runnable, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeController.this.lambda$setWallpaperToUser$10(tLObject, j, z, str, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setWallpaperToUser$10(TLObject tLObject, long j, boolean z, String str, Runnable runnable) {
        String str2;
        if (tLObject instanceof TLRPC$Updates) {
            TLRPC$Updates tLRPC$Updates = (TLRPC$Updates) tLObject;
            TLRPC$UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(j);
            if (userFull != null) {
                int i = 0;
                while (true) {
                    if (i >= tLRPC$Updates.updates.size()) {
                        break;
                    }
                    if (tLRPC$Updates.updates.get(i) instanceof TLRPC$TL_updateNewMessage) {
                        TLRPC$MessageAction tLRPC$MessageAction = ((TLRPC$TL_updateNewMessage) tLRPC$Updates.updates.get(i)).message.action;
                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatWallPaper) {
                            if (z) {
                                TLRPC$TL_messageActionSetChatWallPaper tLRPC$TL_messageActionSetChatWallPaper = (TLRPC$TL_messageActionSetChatWallPaper) tLRPC$MessageAction;
                                tLRPC$TL_messageActionSetChatWallPaper.wallpaper.uploadingImage = str;
                                TLRPC$WallPaper tLRPC$WallPaper = userFull.wallpaper;
                                if (tLRPC$WallPaper != null && (str2 = tLRPC$WallPaper.uploadingImage) != null && str2.equals(str)) {
                                    tLRPC$TL_messageActionSetChatWallPaper.wallpaper.stripedThumb = userFull.wallpaper.stripedThumb;
                                }
                                TLRPC$WallPaper tLRPC$WallPaper2 = tLRPC$TL_messageActionSetChatWallPaper.wallpaper;
                                userFull.wallpaper = tLRPC$WallPaper2;
                                userFull.flags |= ConnectionsManager.FileTypePhoto;
                                saveChatWallpaper(j, tLRPC$WallPaper2);
                                getMessagesStorage().updateUserInfo(userFull, false);
                                NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.userInfoDidLoad, Long.valueOf(j), userFull);
                            }
                        }
                    }
                    i++;
                }
            }
            MessagesController.getInstance(this.currentAccount).processUpdateArray(tLRPC$Updates.updates, tLRPC$Updates.users, tLRPC$Updates.chats, false, tLRPC$Updates.date);
            if (runnable != null) {
                runnable.run();
            }
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.wallpaperSettedToUser, new Object[0]);
        }
    }
}
