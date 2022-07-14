package org.telegram.ui.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.MentionsAdapter;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Cells.BotSwitchCell;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.MentionCell;
import org.telegram.ui.Cells.StickerCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes4.dex */
public class MentionsAdapter extends RecyclerListView.SelectionAdapter implements NotificationCenter.NotificationCenterDelegate {
    private static final String punctuationsChars = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\n";
    private LongSparseArray<TLRPC.BotInfo> botInfo;
    private int botsCount;
    private Runnable cancelDelayRunnable;
    private int channelLastReqId;
    private int channelReqId;
    private boolean contextMedia;
    private int contextQueryReqid;
    private Runnable contextQueryRunnable;
    private int contextUsernameReqid;
    private boolean delayLocalResults;
    private MentionsAdapterDelegate delegate;
    private long dialog_id;
    private TLRPC.User foundContextBot;
    private TLRPC.ChatFull info;
    private boolean isDarkTheme;
    private boolean isSearchingMentions;
    private Object[] lastData;
    private boolean lastForSearch;
    private Location lastKnownLocation;
    private int lastPosition;
    private int lastReqId;
    private String[] lastSearchKeyboardLanguage;
    private String lastSticker;
    private String lastText;
    private boolean lastUsernameOnly;
    private Context mContext;
    private EmojiView.ChooseStickerActionTracker mentionsStickersActionTracker;
    private ArrayList<MessageObject> messages;
    private String nextQueryOffset;
    private boolean noUserName;
    private ChatActivity parentFragment;
    private final Theme.ResourcesProvider resourcesProvider;
    private int resultLength;
    private int resultStartPosition;
    private SearchAdapterHelper searchAdapterHelper;
    private Runnable searchGlobalRunnable;
    private ArrayList<TLRPC.BotInlineResult> searchResultBotContext;
    private TLRPC.TL_inlineBotSwitchPM searchResultBotContextSwitch;
    private ArrayList<String> searchResultCommands;
    private ArrayList<String> searchResultCommandsHelp;
    private ArrayList<TLRPC.User> searchResultCommandsUsers;
    private ArrayList<String> searchResultHashtags;
    private ArrayList<MediaDataController.KeywordResult> searchResultSuggestions;
    private ArrayList<TLObject> searchResultUsernames;
    private LongSparseArray<TLObject> searchResultUsernamesMap;
    private String searchingContextQuery;
    private String searchingContextUsername;
    private ArrayList<StickerResult> stickers;
    private HashMap<String, TLRPC.Document> stickersMap;
    private int threadMessageId;
    private boolean visibleByStickersSearch;
    private final boolean USE_DIVIDERS = false;
    private int currentAccount = UserConfig.selectedAccount;
    private boolean needUsernames = true;
    private boolean needBotContext = true;
    private boolean inlineMediaEnabled = true;
    private ArrayList<String> stickersToLoad = new ArrayList<>();
    private SendMessagesHelper.LocationProvider locationProvider = new SendMessagesHelper.LocationProvider(new SendMessagesHelper.LocationProvider.LocationProviderDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter.1
        @Override // org.telegram.messenger.SendMessagesHelper.LocationProvider.LocationProviderDelegate
        public void onLocationAcquired(Location location) {
            if (MentionsAdapter.this.foundContextBot != null && MentionsAdapter.this.foundContextBot.bot_inline_geo) {
                MentionsAdapter.this.lastKnownLocation = location;
                MentionsAdapter mentionsAdapter = MentionsAdapter.this;
                mentionsAdapter.searchForContextBotResults(true, mentionsAdapter.foundContextBot, MentionsAdapter.this.searchingContextQuery, "");
            }
        }

        @Override // org.telegram.messenger.SendMessagesHelper.LocationProvider.LocationProviderDelegate
        public void onUnableLocationAcquire() {
            MentionsAdapter.this.onLocationUnavailable();
        }
    }) { // from class: org.telegram.ui.Adapters.MentionsAdapter.2
        @Override // org.telegram.messenger.SendMessagesHelper.LocationProvider
        public void stop() {
            super.stop();
            MentionsAdapter.this.lastKnownLocation = null;
        }
    };
    private boolean isReversed = false;
    private int lastItemCount = -1;

    /* loaded from: classes4.dex */
    public interface MentionsAdapterDelegate {
        void needChangePanelVisibility(boolean z);

        void onContextClick(TLRPC.BotInlineResult botInlineResult);

        void onContextSearch(boolean z);

        void onItemCountUpdate(int i, int i2);
    }

    static /* synthetic */ int access$1704(MentionsAdapter x0) {
        int i = x0.channelLastReqId + 1;
        x0.channelLastReqId = i;
        return i;
    }

    /* loaded from: classes4.dex */
    public static class StickerResult {
        public Object parent;
        public TLRPC.Document sticker;

        public StickerResult(TLRPC.Document s, Object p) {
            this.sticker = s;
            this.parent = p;
        }
    }

    public MentionsAdapter(Context context, boolean darkTheme, long did, int threadMessageId, MentionsAdapterDelegate mentionsAdapterDelegate, Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
        this.mContext = context;
        this.delegate = mentionsAdapterDelegate;
        this.isDarkTheme = darkTheme;
        this.dialog_id = did;
        this.threadMessageId = threadMessageId;
        SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(true);
        this.searchAdapterHelper = searchAdapterHelper;
        searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter.3
            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public /* synthetic */ boolean canApplySearchResults(int i) {
                return SearchAdapterHelper.SearchAdapterHelperDelegate.CC.$default$canApplySearchResults(this, i);
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
                return SearchAdapterHelper.SearchAdapterHelperDelegate.CC.$default$getExcludeCallParticipants(this);
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public /* synthetic */ LongSparseArray getExcludeUsers() {
                return SearchAdapterHelper.SearchAdapterHelperDelegate.CC.$default$getExcludeUsers(this);
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public void onDataSetChanged(int searchId) {
                MentionsAdapter.this.notifyDataSetChanged();
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public void onSetHashtags(ArrayList<SearchAdapterHelper.HashtagObject> arrayList, HashMap<String, SearchAdapterHelper.HashtagObject> hashMap) {
                if (MentionsAdapter.this.lastText != null) {
                    MentionsAdapter mentionsAdapter = MentionsAdapter.this;
                    mentionsAdapter.searchUsernameOrHashtag(mentionsAdapter.lastText, MentionsAdapter.this.lastPosition, MentionsAdapter.this.messages, MentionsAdapter.this.lastUsernameOnly, MentionsAdapter.this.lastForSearch);
                }
            }
        });
        if (!darkTheme) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoadFailed);
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int id, int account, Object... args) {
        ArrayList<StickerResult> arrayList;
        if ((id == NotificationCenter.fileLoaded || id == NotificationCenter.fileLoadFailed) && (arrayList = this.stickers) != null && !arrayList.isEmpty() && !this.stickersToLoad.isEmpty() && this.visibleByStickersSearch) {
            boolean z = false;
            String fileName = (String) args[0];
            this.stickersToLoad.remove(fileName);
            if (this.stickersToLoad.isEmpty()) {
                MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
                if (getItemCountInternal() > 0) {
                    z = true;
                }
                mentionsAdapterDelegate.needChangePanelVisibility(z);
            }
        }
    }

    private void addStickerToResult(TLRPC.Document document, Object parent) {
        if (document == null) {
            return;
        }
        String key = document.dc_id + "_" + document.id;
        HashMap<String, TLRPC.Document> hashMap = this.stickersMap;
        if (hashMap != null && hashMap.containsKey(key)) {
            return;
        }
        if (!UserConfig.getInstance(this.currentAccount).isPremium() && MessageObject.isPremiumSticker(document)) {
            return;
        }
        if (this.stickers == null) {
            this.stickers = new ArrayList<>();
            this.stickersMap = new HashMap<>();
        }
        this.stickers.add(new StickerResult(document, parent));
        this.stickersMap.put(key, document);
        EmojiView.ChooseStickerActionTracker chooseStickerActionTracker = this.mentionsStickersActionTracker;
        if (chooseStickerActionTracker != null) {
            chooseStickerActionTracker.checkVisibility();
        }
    }

    private void addStickersToResult(ArrayList<TLRPC.Document> documents, Object parent) {
        if (documents == null || documents.isEmpty()) {
            return;
        }
        int size = documents.size();
        for (int a = 0; a < size; a++) {
            TLRPC.Document document = documents.get(a);
            String key = document.dc_id + "_" + document.id;
            HashMap<String, TLRPC.Document> hashMap = this.stickersMap;
            if ((hashMap == null || !hashMap.containsKey(key)) && (UserConfig.getInstance(this.currentAccount).isPremium() || !MessageObject.isPremiumSticker(document))) {
                int b = 0;
                int size2 = document.attributes.size();
                while (true) {
                    if (b >= size2) {
                        break;
                    }
                    TLRPC.DocumentAttribute attribute = document.attributes.get(b);
                    if (!(attribute instanceof TLRPC.TL_documentAttributeSticker)) {
                        b++;
                    } else {
                        parent = attribute.stickerset;
                        break;
                    }
                }
                if (this.stickers == null) {
                    this.stickers = new ArrayList<>();
                    this.stickersMap = new HashMap<>();
                }
                this.stickers.add(new StickerResult(document, parent));
                this.stickersMap.put(key, document);
            }
        }
    }

    private boolean checkStickerFilesExistAndDownload() {
        if (this.stickers == null) {
            return false;
        }
        this.stickersToLoad.clear();
        int size = Math.min(6, this.stickers.size());
        for (int a = 0; a < size; a++) {
            StickerResult result = this.stickers.get(a);
            TLRPC.PhotoSize thumb = FileLoader.getClosestPhotoSizeWithSize(result.sticker.thumbs, 90);
            if ((thumb instanceof TLRPC.TL_photoSize) || (thumb instanceof TLRPC.TL_photoSizeProgressive)) {
                File f = FileLoader.getInstance(this.currentAccount).getPathToAttach(thumb, "webp", true);
                if (!f.exists()) {
                    this.stickersToLoad.add(FileLoader.getAttachFileName(thumb, "webp"));
                    FileLoader.getInstance(this.currentAccount).loadFile(ImageLocation.getForDocument(thumb, result.sticker), result.parent, "webp", 1, 1);
                }
            }
        }
        return this.stickersToLoad.isEmpty();
    }

    private boolean isValidSticker(TLRPC.Document document, String emoji) {
        int size2 = document.attributes.size();
        for (int b = 0; b < size2; b++) {
            TLRPC.DocumentAttribute attribute = document.attributes.get(b);
            if (attribute instanceof TLRPC.TL_documentAttributeSticker) {
                if (attribute.alt != null && attribute.alt.contains(emoji)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private void searchServerStickers(final String emoji, String originalEmoji) {
        TLRPC.TL_messages_getStickers req = new TLRPC.TL_messages_getStickers();
        req.emoticon = originalEmoji;
        req.hash = 0L;
        this.lastReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda7
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MentionsAdapter.this.m1485x5b1741cc(emoji, tLObject, tL_error);
            }
        });
    }

    /* renamed from: lambda$searchServerStickers$1$org-telegram-ui-Adapters-MentionsAdapter */
    public /* synthetic */ void m1485x5b1741cc(final String emoji, final TLObject response, TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                MentionsAdapter.this.m1484x53b20cad(emoji, response);
            }
        });
    }

    /* renamed from: lambda$searchServerStickers$0$org-telegram-ui-Adapters-MentionsAdapter */
    public /* synthetic */ void m1484x53b20cad(String emoji, TLObject response) {
        ArrayList<StickerResult> arrayList;
        boolean z = false;
        this.lastReqId = 0;
        if (!emoji.equals(this.lastSticker) || !(response instanceof TLRPC.TL_messages_stickers)) {
            return;
        }
        this.delayLocalResults = false;
        TLRPC.TL_messages_stickers res = (TLRPC.TL_messages_stickers) response;
        ArrayList<StickerResult> arrayList2 = this.stickers;
        int oldCount = arrayList2 != null ? arrayList2.size() : 0;
        ArrayList<TLRPC.Document> arrayList3 = res.stickers;
        addStickersToResult(arrayList3, "sticker_search_" + emoji);
        ArrayList<StickerResult> arrayList4 = this.stickers;
        int newCount = arrayList4 != null ? arrayList4.size() : 0;
        if (!this.visibleByStickersSearch && (arrayList = this.stickers) != null && !arrayList.isEmpty()) {
            checkStickerFilesExistAndDownload();
            MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
            if (getItemCountInternal() > 0) {
                z = true;
            }
            mentionsAdapterDelegate.needChangePanelVisibility(z);
            this.visibleByStickersSearch = true;
        }
        if (oldCount != newCount) {
            notifyDataSetChanged();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void notifyDataSetChanged() {
        MentionsAdapterDelegate mentionsAdapterDelegate;
        boolean hadChanges = false;
        if (this.lastItemCount == -1 || this.lastData == null) {
            MentionsAdapterDelegate mentionsAdapterDelegate2 = this.delegate;
            if (mentionsAdapterDelegate2 != null) {
                mentionsAdapterDelegate2.onItemCountUpdate(0, getItemCount());
            }
            super.notifyDataSetChanged();
            this.lastData = new Object[getItemCount()];
            int i = 0;
            while (true) {
                Object[] objArr = this.lastData;
                if (i < objArr.length) {
                    objArr[i] = getItem(i);
                    i++;
                } else {
                    return;
                }
            }
        } else {
            int oldCount = this.lastItemCount;
            int newCount = getItemCount();
            if (oldCount != newCount) {
                hadChanges = true;
            }
            int min = Math.min(oldCount, newCount);
            Object[] newData = new Object[newCount];
            for (int i2 = 0; i2 < newCount; i2++) {
                newData[i2] = getItem(i2);
            }
            for (int i3 = 0; i3 < min; i3++) {
                if (i3 >= 0) {
                    Object[] objArr2 = this.lastData;
                    if (i3 < objArr2.length && i3 < newData.length && itemsEqual(objArr2[i3], newData[i3])) {
                    }
                }
                notifyItemChanged(i3);
                hadChanges = true;
            }
            int i4 = oldCount - min;
            notifyItemRangeRemoved(min, i4);
            notifyItemRangeInserted(min, newCount - min);
            if (hadChanges && (mentionsAdapterDelegate = this.delegate) != null) {
                mentionsAdapterDelegate.onItemCountUpdate(oldCount, newCount);
            }
            this.lastData = newData;
        }
    }

    private boolean itemsEqual(Object a, Object b) {
        if (a == b) {
            return true;
        }
        if ((a instanceof StickerResult) && (b instanceof StickerResult) && ((StickerResult) a).sticker == ((StickerResult) b).sticker) {
            return true;
        }
        if ((a instanceof TLRPC.User) && (b instanceof TLRPC.User) && ((TLRPC.User) a).id == ((TLRPC.User) b).id) {
            return true;
        }
        if ((a instanceof TLRPC.Chat) && (b instanceof TLRPC.Chat) && ((TLRPC.Chat) a).id == ((TLRPC.Chat) b).id) {
            return true;
        }
        if ((a instanceof String) && (b instanceof String) && a.equals(b)) {
            return true;
        }
        if ((a instanceof MediaDataController.KeywordResult) && (b instanceof MediaDataController.KeywordResult) && ((MediaDataController.KeywordResult) a).keyword != null && ((MediaDataController.KeywordResult) a).keyword.equals(((MediaDataController.KeywordResult) b).keyword) && ((MediaDataController.KeywordResult) a).emoji != null && ((MediaDataController.KeywordResult) a).emoji.equals(((MediaDataController.KeywordResult) b).emoji)) {
            return true;
        }
        return false;
    }

    private void clearStickers() {
        this.lastSticker = null;
        this.stickers = null;
        this.stickersMap = null;
        notifyDataSetChanged();
        if (this.lastReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.lastReqId, true);
            this.lastReqId = 0;
        }
        EmojiView.ChooseStickerActionTracker chooseStickerActionTracker = this.mentionsStickersActionTracker;
        if (chooseStickerActionTracker != null) {
            chooseStickerActionTracker.checkVisibility();
        }
    }

    public void onDestroy() {
        SendMessagesHelper.LocationProvider locationProvider = this.locationProvider;
        if (locationProvider != null) {
            locationProvider.stop();
        }
        Runnable runnable = this.contextQueryRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.contextQueryRunnable = null;
        }
        if (this.contextUsernameReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextUsernameReqid, true);
            this.contextUsernameReqid = 0;
        }
        if (this.contextQueryReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
            this.contextQueryReqid = 0;
        }
        this.foundContextBot = null;
        this.inlineMediaEnabled = true;
        this.searchingContextUsername = null;
        this.searchingContextQuery = null;
        this.noUserName = false;
        if (!this.isDarkTheme) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoadFailed);
        }
    }

    public void setParentFragment(ChatActivity fragment) {
        this.parentFragment = fragment;
    }

    public void setChatInfo(TLRPC.ChatFull chatInfo) {
        ChatActivity chatActivity;
        TLRPC.Chat chat;
        this.currentAccount = UserConfig.selectedAccount;
        this.info = chatInfo;
        if (!this.inlineMediaEnabled && this.foundContextBot != null && (chatActivity = this.parentFragment) != null && (chat = chatActivity.getCurrentChat()) != null) {
            boolean canSendStickers = ChatObject.canSendStickers(chat);
            this.inlineMediaEnabled = canSendStickers;
            if (canSendStickers) {
                this.searchResultUsernames = null;
                notifyDataSetChanged();
                this.delegate.needChangePanelVisibility(false);
                processFoundUser(this.foundContextBot);
            }
        }
        String str = this.lastText;
        if (str != null) {
            searchUsernameOrHashtag(str, this.lastPosition, this.messages, this.lastUsernameOnly, this.lastForSearch);
        }
    }

    public void setNeedUsernames(boolean value) {
        this.needUsernames = value;
    }

    public void setNeedBotContext(boolean value) {
        this.needBotContext = value;
    }

    public void setBotInfo(LongSparseArray<TLRPC.BotInfo> info) {
        this.botInfo = info;
    }

    public void setBotsCount(int count) {
        this.botsCount = count;
    }

    public void clearRecentHashtags() {
        this.searchAdapterHelper.clearRecentHashtags();
        this.searchResultHashtags.clear();
        notifyDataSetChanged();
        MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
        if (mentionsAdapterDelegate != null) {
            mentionsAdapterDelegate.needChangePanelVisibility(false);
        }
    }

    public TLRPC.TL_inlineBotSwitchPM getBotContextSwitch() {
        return this.searchResultBotContextSwitch;
    }

    public long getContextBotId() {
        TLRPC.User user = this.foundContextBot;
        if (user != null) {
            return user.id;
        }
        return 0L;
    }

    public TLRPC.User getContextBotUser() {
        return this.foundContextBot;
    }

    public String getContextBotName() {
        TLRPC.User user = this.foundContextBot;
        return user != null ? user.username : "";
    }

    public void processFoundUser(TLRPC.User user) {
        ChatActivity chatActivity;
        TLRPC.Chat chat;
        this.contextUsernameReqid = 0;
        this.locationProvider.stop();
        if (user != null && user.bot && user.bot_inline_placeholder != null) {
            this.foundContextBot = user;
            ChatActivity chatActivity2 = this.parentFragment;
            if (chatActivity2 != null && (chat = chatActivity2.getCurrentChat()) != null) {
                boolean canSendStickers = ChatObject.canSendStickers(chat);
                this.inlineMediaEnabled = canSendStickers;
                if (!canSendStickers) {
                    notifyDataSetChanged();
                    this.delegate.needChangePanelVisibility(true);
                    return;
                }
            }
            if (this.foundContextBot.bot_inline_geo) {
                SharedPreferences preferences = MessagesController.getNotificationsSettings(this.currentAccount);
                boolean allowGeo = preferences.getBoolean("inlinegeo_" + this.foundContextBot.id, false);
                if (!allowGeo && (chatActivity = this.parentFragment) != null && chatActivity.getParentActivity() != null) {
                    final TLRPC.User foundContextBotFinal = this.foundContextBot;
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.parentFragment.getParentActivity());
                    builder.setTitle(LocaleController.getString("ShareYouLocationTitle", R.string.ShareYouLocationTitle));
                    builder.setMessage(LocaleController.getString("ShareYouLocationInline", R.string.ShareYouLocationInline));
                    final boolean[] buttonClicked = new boolean[1];
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda1
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            MentionsAdapter.this.m1479xde31628e(buttonClicked, foundContextBotFinal, dialogInterface, i);
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            MentionsAdapter.this.m1480xe59697ad(buttonClicked, dialogInterface, i);
                        }
                    });
                    this.parentFragment.showDialog(builder.create(), new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda2
                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            MentionsAdapter.this.m1481xecfbcccc(buttonClicked, dialogInterface);
                        }
                    });
                } else {
                    checkLocationPermissionsOrStart();
                }
            }
        } else {
            this.foundContextBot = null;
            this.inlineMediaEnabled = true;
        }
        if (this.foundContextBot == null) {
            this.noUserName = true;
            return;
        }
        MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
        if (mentionsAdapterDelegate != null) {
            mentionsAdapterDelegate.onContextSearch(true);
        }
        searchForContextBotResults(true, this.foundContextBot, this.searchingContextQuery, "");
    }

    /* renamed from: lambda$processFoundUser$2$org-telegram-ui-Adapters-MentionsAdapter */
    public /* synthetic */ void m1479xde31628e(boolean[] buttonClicked, TLRPC.User foundContextBotFinal, DialogInterface dialogInterface, int i) {
        buttonClicked[0] = true;
        if (foundContextBotFinal != null) {
            SharedPreferences preferences1 = MessagesController.getNotificationsSettings(this.currentAccount);
            SharedPreferences.Editor edit = preferences1.edit();
            edit.putBoolean("inlinegeo_" + foundContextBotFinal.id, true).commit();
            checkLocationPermissionsOrStart();
        }
    }

    /* renamed from: lambda$processFoundUser$3$org-telegram-ui-Adapters-MentionsAdapter */
    public /* synthetic */ void m1480xe59697ad(boolean[] buttonClicked, DialogInterface dialog, int which) {
        buttonClicked[0] = true;
        onLocationUnavailable();
    }

    /* renamed from: lambda$processFoundUser$4$org-telegram-ui-Adapters-MentionsAdapter */
    public /* synthetic */ void m1481xecfbcccc(boolean[] buttonClicked, DialogInterface dialog) {
        if (!buttonClicked[0]) {
            onLocationUnavailable();
        }
    }

    private void searchForContextBot(String username, String query) {
        String str;
        String str2;
        TLRPC.User user = this.foundContextBot;
        if (user != null && user.username != null && this.foundContextBot.username.equals(username) && (str2 = this.searchingContextQuery) != null && str2.equals(query)) {
            return;
        }
        if (this.foundContextBot != null) {
            if (!this.inlineMediaEnabled && username != null && query != null) {
                return;
            }
            this.delegate.needChangePanelVisibility(false);
        }
        Runnable runnable = this.contextQueryRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.contextQueryRunnable = null;
        }
        if (TextUtils.isEmpty(username) || ((str = this.searchingContextUsername) != null && !str.equals(username))) {
            if (this.contextUsernameReqid != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextUsernameReqid, true);
                this.contextUsernameReqid = 0;
            }
            if (this.contextQueryReqid != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
                this.contextQueryReqid = 0;
            }
            this.foundContextBot = null;
            this.inlineMediaEnabled = true;
            this.searchingContextUsername = null;
            this.searchingContextQuery = null;
            this.locationProvider.stop();
            this.noUserName = false;
            MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
            if (mentionsAdapterDelegate != null) {
                mentionsAdapterDelegate.onContextSearch(false);
            }
            if (username == null || username.length() == 0) {
                return;
            }
        }
        if (query == null) {
            if (this.contextQueryReqid != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
                this.contextQueryReqid = 0;
            }
            this.searchingContextQuery = null;
            MentionsAdapterDelegate mentionsAdapterDelegate2 = this.delegate;
            if (mentionsAdapterDelegate2 != null) {
                mentionsAdapterDelegate2.onContextSearch(false);
                return;
            }
            return;
        }
        MentionsAdapterDelegate mentionsAdapterDelegate3 = this.delegate;
        if (mentionsAdapterDelegate3 != null) {
            if (this.foundContextBot != null) {
                mentionsAdapterDelegate3.onContextSearch(true);
            } else if (username.equals("gif")) {
                this.searchingContextUsername = "gif";
                this.delegate.onContextSearch(false);
            }
        }
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
        this.searchingContextQuery = query;
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(query, username, messagesController, messagesStorage);
        this.contextQueryRunnable = anonymousClass4;
        AndroidUtilities.runOnUIThread(anonymousClass4, 400L);
    }

    /* renamed from: org.telegram.ui.Adapters.MentionsAdapter$4 */
    /* loaded from: classes4.dex */
    public class AnonymousClass4 implements Runnable {
        final /* synthetic */ MessagesController val$messagesController;
        final /* synthetic */ MessagesStorage val$messagesStorage;
        final /* synthetic */ String val$query;
        final /* synthetic */ String val$username;

        AnonymousClass4(String str, String str2, MessagesController messagesController, MessagesStorage messagesStorage) {
            MentionsAdapter.this = this$0;
            this.val$query = str;
            this.val$username = str2;
            this.val$messagesController = messagesController;
            this.val$messagesStorage = messagesStorage;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (MentionsAdapter.this.contextQueryRunnable == this) {
                MentionsAdapter.this.contextQueryRunnable = null;
                if (MentionsAdapter.this.foundContextBot != null || MentionsAdapter.this.noUserName) {
                    if (MentionsAdapter.this.noUserName) {
                        return;
                    }
                    MentionsAdapter mentionsAdapter = MentionsAdapter.this;
                    mentionsAdapter.searchForContextBotResults(true, mentionsAdapter.foundContextBot, this.val$query, "");
                    return;
                }
                MentionsAdapter.this.searchingContextUsername = this.val$username;
                TLObject object = this.val$messagesController.getUserOrChat(MentionsAdapter.this.searchingContextUsername);
                if (object instanceof TLRPC.User) {
                    MentionsAdapter.this.processFoundUser((TLRPC.User) object);
                    return;
                }
                TLRPC.TL_contacts_resolveUsername req = new TLRPC.TL_contacts_resolveUsername();
                req.username = MentionsAdapter.this.searchingContextUsername;
                MentionsAdapter mentionsAdapter2 = MentionsAdapter.this;
                ConnectionsManager connectionsManager = ConnectionsManager.getInstance(mentionsAdapter2.currentAccount);
                final String str = this.val$username;
                final MessagesController messagesController = this.val$messagesController;
                final MessagesStorage messagesStorage = this.val$messagesStorage;
                mentionsAdapter2.contextUsernameReqid = connectionsManager.sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$4$$ExternalSyntheticLambda1
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        MentionsAdapter.AnonymousClass4.this.m1489lambda$run$1$orgtelegramuiAdaptersMentionsAdapter$4(str, messagesController, messagesStorage, tLObject, tL_error);
                    }
                });
            }
        }

        /* renamed from: lambda$run$1$org-telegram-ui-Adapters-MentionsAdapter$4 */
        public /* synthetic */ void m1489lambda$run$1$orgtelegramuiAdaptersMentionsAdapter$4(final String username, final MessagesController messagesController, final MessagesStorage messagesStorage, final TLObject response, final TLRPC.TL_error error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MentionsAdapter.AnonymousClass4.this.m1488lambda$run$0$orgtelegramuiAdaptersMentionsAdapter$4(username, error, response, messagesController, messagesStorage);
                }
            });
        }

        /* renamed from: lambda$run$0$org-telegram-ui-Adapters-MentionsAdapter$4 */
        public /* synthetic */ void m1488lambda$run$0$orgtelegramuiAdaptersMentionsAdapter$4(String username, TLRPC.TL_error error, TLObject response, MessagesController messagesController, MessagesStorage messagesStorage) {
            if (MentionsAdapter.this.searchingContextUsername == null || !MentionsAdapter.this.searchingContextUsername.equals(username)) {
                return;
            }
            TLRPC.User user = null;
            if (error == null) {
                TLRPC.TL_contacts_resolvedPeer res = (TLRPC.TL_contacts_resolvedPeer) response;
                if (!res.users.isEmpty()) {
                    user = res.users.get(0);
                    messagesController.putUser(user, false);
                    messagesStorage.putUsersAndChats(res.users, null, true, true);
                }
            }
            MentionsAdapter.this.processFoundUser(user);
            MentionsAdapter.this.contextUsernameReqid = 0;
        }
    }

    public void onLocationUnavailable() {
        TLRPC.User user = this.foundContextBot;
        if (user != null && user.bot_inline_geo) {
            Location location = new Location("network");
            this.lastKnownLocation = location;
            location.setLatitude(-1000.0d);
            this.lastKnownLocation.setLongitude(-1000.0d);
            searchForContextBotResults(true, this.foundContextBot, this.searchingContextQuery, "");
        }
    }

    private void checkLocationPermissionsOrStart() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || chatActivity.getParentActivity() == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23 && this.parentFragment.getParentActivity().checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            this.parentFragment.getParentActivity().requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
            return;
        }
        TLRPC.User user = this.foundContextBot;
        if (user != null && user.bot_inline_geo) {
            this.locationProvider.start();
        }
    }

    public void setSearchingMentions(boolean value) {
        this.isSearchingMentions = value;
    }

    public String getBotCaption() {
        TLRPC.User user = this.foundContextBot;
        if (user != null) {
            return user.bot_inline_placeholder;
        }
        String str = this.searchingContextUsername;
        if (str != null && str.equals("gif")) {
            return "Search GIFs";
        }
        return null;
    }

    public void searchForContextBotForNextOffset() {
        String str;
        TLRPC.User user;
        String str2;
        if (this.contextQueryReqid != 0 || (str = this.nextQueryOffset) == null || str.length() == 0 || (user = this.foundContextBot) == null || (str2 = this.searchingContextQuery) == null) {
            return;
        }
        searchForContextBotResults(true, user, str2, this.nextQueryOffset);
    }

    public void searchForContextBotResults(final boolean cache, final TLRPC.User user, final String query, final String offset) {
        Location location;
        if (this.contextQueryReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
            this.contextQueryReqid = 0;
        }
        if (!this.inlineMediaEnabled) {
            MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
            if (mentionsAdapterDelegate != null) {
                mentionsAdapterDelegate.onContextSearch(false);
            }
        } else if (query == null || user == null) {
            this.searchingContextQuery = null;
        } else if (user.bot_inline_geo && this.lastKnownLocation == null) {
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(this.dialog_id);
            sb.append("_");
            sb.append(query);
            sb.append("_");
            sb.append(offset);
            sb.append("_");
            sb.append(this.dialog_id);
            sb.append("_");
            sb.append(user.id);
            sb.append("_");
            sb.append((!user.bot_inline_geo || this.lastKnownLocation.getLatitude() == -1000.0d) ? "" : Double.valueOf(this.lastKnownLocation.getLatitude() + this.lastKnownLocation.getLongitude()));
            final String key = sb.toString();
            final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
            RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda8
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MentionsAdapter.this.m1483xcd6fb36b(query, cache, user, offset, messagesStorage, key, tLObject, tL_error);
                }
            };
            if (cache) {
                messagesStorage.getBotCache(key, requestDelegate);
                return;
            }
            TLRPC.TL_messages_getInlineBotResults req = new TLRPC.TL_messages_getInlineBotResults();
            req.bot = MessagesController.getInstance(this.currentAccount).getInputUser(user);
            req.query = query;
            req.offset = offset;
            if (user.bot_inline_geo && (location = this.lastKnownLocation) != null && location.getLatitude() != -1000.0d) {
                req.flags |= 1;
                req.geo_point = new TLRPC.TL_inputGeoPoint();
                req.geo_point.lat = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLatitude());
                req.geo_point._long = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLongitude());
            }
            if (!DialogObject.isEncryptedDialog(this.dialog_id)) {
                req.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.dialog_id);
            } else {
                req.peer = new TLRPC.TL_inputPeerEmpty();
            }
            this.contextQueryReqid = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, requestDelegate, 2);
        }
    }

    /* renamed from: lambda$searchForContextBotResults$6$org-telegram-ui-Adapters-MentionsAdapter */
    public /* synthetic */ void m1483xcd6fb36b(final String query, final boolean cache, final TLRPC.User user, final String offset, final MessagesStorage messagesStorage, final String key, final TLObject response, TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                MentionsAdapter.this.m1482xc60a7e4c(query, cache, response, user, offset, messagesStorage, key);
            }
        });
    }

    /* renamed from: lambda$searchForContextBotResults$5$org-telegram-ui-Adapters-MentionsAdapter */
    public /* synthetic */ void m1482xc60a7e4c(String query, boolean cache, TLObject response, TLRPC.User user, String offset, MessagesStorage messagesStorage, String key) {
        if (!query.equals(this.searchingContextQuery)) {
            return;
        }
        boolean z = false;
        this.contextQueryReqid = 0;
        if (cache && response == null) {
            searchForContextBotResults(false, user, query, offset);
        } else {
            MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
            if (mentionsAdapterDelegate != null) {
                mentionsAdapterDelegate.onContextSearch(false);
            }
        }
        if (response instanceof TLRPC.TL_messages_botResults) {
            TLRPC.TL_messages_botResults res = (TLRPC.TL_messages_botResults) response;
            if (!cache && res.cache_time != 0) {
                messagesStorage.saveBotCache(key, res);
            }
            this.nextQueryOffset = res.next_offset;
            if (this.searchResultBotContextSwitch == null) {
                this.searchResultBotContextSwitch = res.switch_pm;
            }
            int a = 0;
            while (a < res.results.size()) {
                TLRPC.BotInlineResult result = res.results.get(a);
                if (!(result.document instanceof TLRPC.TL_document) && !(result.photo instanceof TLRPC.TL_photo) && !"game".equals(result.type) && result.content == null && (result.send_message instanceof TLRPC.TL_botInlineMessageMediaAuto)) {
                    res.results.remove(a);
                    a--;
                }
                result.query_id = res.query_id;
                a++;
            }
            boolean added = false;
            if (this.searchResultBotContext == null || offset.length() == 0) {
                this.searchResultBotContext = res.results;
                this.contextMedia = res.gallery;
            } else {
                added = true;
                this.searchResultBotContext.addAll(res.results);
                if (res.results.isEmpty()) {
                    this.nextQueryOffset = "";
                }
            }
            Runnable runnable = this.cancelDelayRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.cancelDelayRunnable = null;
            }
            this.searchResultHashtags = null;
            this.stickers = null;
            this.searchResultUsernames = null;
            this.searchResultUsernamesMap = null;
            this.searchResultCommands = null;
            this.searchResultSuggestions = null;
            this.searchResultCommandsHelp = null;
            this.searchResultCommandsUsers = null;
            if (added) {
                boolean hasTop = this.searchResultBotContextSwitch != null;
                notifyItemChanged(((this.searchResultBotContext.size() - res.results.size()) + (hasTop ? 1 : 0)) - 1);
                notifyItemRangeInserted((this.searchResultBotContext.size() - res.results.size()) + (hasTop ? 1 : 0), res.results.size());
            } else {
                notifyDataSetChanged();
            }
            MentionsAdapterDelegate mentionsAdapterDelegate2 = this.delegate;
            if (!this.searchResultBotContext.isEmpty() || this.searchResultBotContextSwitch != null) {
                z = true;
            }
            mentionsAdapterDelegate2.needChangePanelVisibility(z);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:198:0x0383, code lost:
        if (r33.info != null) goto L202;
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x0385, code lost:
        if (r2 == 0) goto L202;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0387, code lost:
        r33.lastText = r34;
        r33.lastPosition = r35;
        r33.messages = r36;
        r33.delegate.needChangePanelVisibility(false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0393, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x0394, code lost:
        r1 = r2;
        r33.resultStartPosition = r2;
        r33.resultLength = r15.length() + 1;
        r5 = 0;
        r21 = r1;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r33v0, types: [org.telegram.ui.Adapters.MentionsAdapter] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void searchUsernameOrHashtag(String text, int position, ArrayList<MessageObject> messageObjects, boolean usernameOnly, boolean forSearch) {
        String str;
        int foundType;
        String originalEmoji;
        int foundType2;
        int foundType3;
        int foundType4;
        String originalEmoji2;
        int searchPostion;
        int threadId;
        TLRPC.Chat chat;
        LongSparseArray<TLRPC.User> newResultsHashMap;
        ArrayList<TLRPC.TL_topPeer> inlineBots;
        MessagesController messagesController;
        TLRPC.ChatFull chatFull;
        int searchPostion2;
        LongSparseArray<TLRPC.User> newResultsHashMap2;
        ArrayList<TLRPC.TL_topPeer> inlineBots2;
        MessagesController messagesController2;
        long id;
        TLObject object;
        String lastName;
        String firstName;
        String username;
        StringBuilder result;
        int foundType5;
        ChatActivity chatActivity;
        boolean z;
        int foundType6;
        int foundType7;
        boolean z2 = usernameOnly;
        Runnable runnable = this.cancelDelayRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.cancelDelayRunnable = null;
        }
        if (this.channelReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.channelReqId, true);
            this.channelReqId = 0;
        }
        Runnable runnable2 = this.searchGlobalRunnable;
        if (runnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            this.searchGlobalRunnable = null;
        }
        if (!TextUtils.isEmpty(text)) {
            if (text.length() <= MessagesController.getInstance(this.currentAccount).maxMessageLength) {
                int searchPostion3 = text.length() <= 0 ? position : position - 1;
                this.lastText = null;
                this.lastUsernameOnly = z2;
                this.lastForSearch = forSearch;
                StringBuilder result2 = new StringBuilder();
                int foundType8 = -1;
                boolean searchEmoji = !z2 && text != null && text.length() > 0 && text.length() <= 14;
                if (!searchEmoji) {
                    foundType = 65535;
                    originalEmoji = "";
                } else {
                    CharSequence emoji = text;
                    int length = emoji.length();
                    int a = 0;
                    while (a < length) {
                        char ch = emoji.charAt(a);
                        char nch = a < length + (-1) ? emoji.charAt(a + 1) : (char) 0;
                        if (a >= length - 1 || ch != 55356 || nch < 57339 || nch > 57343) {
                            foundType7 = foundType8;
                            if (ch == 65039) {
                                CharSequence[] charSequenceArr = {emoji.subSequence(0, a), emoji.subSequence(a + 1, emoji.length())};
                                length--;
                                a--;
                                emoji = TextUtils.concat(charSequenceArr);
                            }
                        } else {
                            foundType7 = foundType8;
                            CharSequence[] charSequenceArr2 = {emoji.subSequence(0, a), emoji.subSequence(a + 2, emoji.length())};
                            length -= 2;
                            a--;
                            emoji = TextUtils.concat(charSequenceArr2);
                        }
                        a++;
                        foundType8 = foundType7;
                    }
                    foundType = foundType8;
                    this.lastSticker = emoji.toString().trim();
                    originalEmoji = text;
                }
                boolean isValidEmoji = searchEmoji && (Emoji.isValidEmoji(originalEmoji) || Emoji.isValidEmoji(this.lastSticker));
                if (isValidEmoji && (chatActivity = this.parentFragment) != null && (chatActivity.getCurrentChat() == null || ChatObject.canSendStickers(this.parentFragment.getCurrentChat()))) {
                    this.stickersToLoad.clear();
                    if (SharedConfig.suggestStickers != 2 && isValidEmoji) {
                        this.stickers = null;
                        this.stickersMap = null;
                        int foundType9 = 4;
                        if (this.lastReqId != 0) {
                            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.lastReqId, true);
                            z = false;
                            this.lastReqId = 0;
                        } else {
                            z = false;
                        }
                        boolean serverStickersOnly = MessagesController.getInstance(this.currentAccount).suggestStickersApiOnly;
                        this.delayLocalResults = z;
                        if (serverStickersOnly) {
                            foundType6 = 4;
                        } else {
                            MediaDataController mediaDataController = MediaDataController.getInstance(this.currentAccount);
                            int i = z ? 1 : 0;
                            int i2 = z ? 1 : 0;
                            final ArrayList<TLRPC.Document> recentStickers = mediaDataController.getRecentStickersNoCopy(i);
                            final ArrayList<TLRPC.Document> favsStickers = MediaDataController.getInstance(this.currentAccount).getRecentStickersNoCopy(2);
                            int recentsAdded = 0;
                            int size = Math.min(20, recentStickers.size());
                            int a2 = 0;
                            while (true) {
                                if (a2 >= size) {
                                    foundType6 = foundType9;
                                    break;
                                }
                                int size2 = size;
                                TLRPC.Document document = recentStickers.get(a2);
                                foundType6 = foundType9;
                                if (isValidSticker(document, this.lastSticker)) {
                                    addStickerToResult(document, "recent");
                                    recentsAdded++;
                                    if (recentsAdded >= 5) {
                                        break;
                                    }
                                }
                                a2++;
                                foundType9 = foundType6;
                                size = size2;
                            }
                            int a3 = 0;
                            int size3 = favsStickers.size();
                            while (a3 < size3) {
                                TLRPC.Document document2 = favsStickers.get(a3);
                                int recentsAdded2 = recentsAdded;
                                if (isValidSticker(document2, this.lastSticker)) {
                                    addStickerToResult(document2, "fav");
                                }
                                a3++;
                                recentsAdded = recentsAdded2;
                            }
                            int a4 = this.currentAccount;
                            HashMap<String, ArrayList<TLRPC.Document>> allStickers = MediaDataController.getInstance(a4).getAllStickers();
                            ArrayList<TLRPC.Document> newStickers = allStickers != null ? allStickers.get(this.lastSticker) : null;
                            if (newStickers != null && !newStickers.isEmpty()) {
                                addStickersToResult(newStickers, null);
                            }
                            ArrayList<StickerResult> arrayList = this.stickers;
                            if (arrayList != null) {
                                Collections.sort(arrayList, new Comparator<StickerResult>() { // from class: org.telegram.ui.Adapters.MentionsAdapter.5
                                    private int getIndex(StickerResult result3) {
                                        for (int a5 = 0; a5 < favsStickers.size(); a5++) {
                                            if (((TLRPC.Document) favsStickers.get(a5)).id == result3.sticker.id) {
                                                return 2000000 + a5;
                                            }
                                        }
                                        for (int a6 = 0; a6 < Math.min(20, recentStickers.size()); a6++) {
                                            if (((TLRPC.Document) recentStickers.get(a6)).id == result3.sticker.id) {
                                                return (recentStickers.size() - a6) + MediaController.VIDEO_BITRATE_480;
                                            }
                                        }
                                        return -1;
                                    }

                                    public int compare(StickerResult lhs, StickerResult rhs) {
                                        boolean isAnimated1 = MessageObject.isAnimatedStickerDocument(lhs.sticker, true);
                                        boolean isAnimated2 = MessageObject.isAnimatedStickerDocument(rhs.sticker, true);
                                        if (isAnimated1 != isAnimated2) {
                                            return isAnimated1 ? -1 : 1;
                                        }
                                        int idx1 = getIndex(lhs);
                                        int idx2 = getIndex(rhs);
                                        if (idx1 > idx2) {
                                            return -1;
                                        }
                                        return idx1 < idx2 ? 1 : 0;
                                    }
                                });
                            }
                        }
                        if (SharedConfig.suggestStickers == 0 || serverStickersOnly) {
                            searchServerStickers(this.lastSticker, originalEmoji);
                        }
                        ArrayList<StickerResult> arrayList2 = this.stickers;
                        if (arrayList2 != null && !arrayList2.isEmpty()) {
                            if (SharedConfig.suggestStickers == 0 && this.stickers.size() < 5) {
                                this.delayLocalResults = true;
                                this.delegate.needChangePanelVisibility(false);
                                this.visibleByStickersSearch = false;
                            } else {
                                checkStickerFilesExistAndDownload();
                                boolean show = this.stickersToLoad.isEmpty();
                                this.delegate.needChangePanelVisibility(show);
                                this.visibleByStickersSearch = true;
                            }
                            notifyDataSetChanged();
                        } else if (this.visibleByStickersSearch) {
                            this.delegate.needChangePanelVisibility(false);
                            this.visibleByStickersSearch = false;
                        }
                        foundType2 = foundType6;
                    } else if (this.visibleByStickersSearch && SharedConfig.suggestStickers == 2) {
                        this.visibleByStickersSearch = false;
                        this.delegate.needChangePanelVisibility(false);
                        notifyDataSetChanged();
                        return;
                    } else {
                        return;
                    }
                } else {
                    if (z2 || !this.needBotContext || text.charAt(0) != '@') {
                        searchForContextBot(null, null);
                    } else {
                        int index = text.indexOf(32);
                        int len = text.length();
                        String username2 = null;
                        String query = null;
                        if (index > 0) {
                            username2 = text.substring(1, index);
                            query = text.substring(index + 1);
                        } else if (text.charAt(len - 1) != 't' || text.charAt(len - 2) != 'o' || text.charAt(len - 3) != 'b') {
                            searchForContextBot(null, null);
                        } else {
                            username2 = text.substring(1);
                            query = "";
                        }
                        if (username2 != null && username2.length() >= 1) {
                            int a5 = 1;
                            while (true) {
                                if (a5 >= username2.length()) {
                                    break;
                                }
                                char ch2 = username2.charAt(a5);
                                if ((ch2 >= '0' && ch2 <= '9') || ((ch2 >= 'a' && ch2 <= 'z') || ((ch2 >= 'A' && ch2 <= 'Z') || ch2 == '_'))) {
                                    a5++;
                                } else {
                                    username2 = "";
                                    break;
                                }
                            }
                        } else {
                            username2 = "";
                        }
                        searchForContextBot(username2, query);
                    }
                    foundType2 = foundType;
                }
                if (this.foundContextBot != null) {
                    return;
                }
                MessagesController messagesController3 = MessagesController.getInstance(this.currentAccount);
                if (z2) {
                    result2.append(text.substring(1));
                    this.resultStartPosition = 0;
                    this.resultLength = result2.length();
                    foundType4 = 0;
                    foundType3 = -1;
                } else {
                    int a6 = searchPostion3;
                    while (true) {
                        if (a6 < 0) {
                            foundType4 = foundType2;
                            foundType3 = -1;
                            break;
                        }
                        if (a6 >= text.length()) {
                            foundType5 = foundType2;
                        } else {
                            char ch3 = text.charAt(a6);
                            if (a6 != 0 && text.charAt(a6 - 1) != ' ' && text.charAt(a6 - 1) != '\n' && ch3 != ':') {
                                foundType5 = foundType2;
                            } else if (ch3 == '@') {
                                if (this.needUsernames || (this.needBotContext && a6 == 0)) {
                                    break;
                                }
                                foundType5 = foundType2;
                            } else if (ch3 == '#') {
                                if (this.searchAdapterHelper.loadRecentHashtags()) {
                                    this.resultStartPosition = a6;
                                    this.resultLength = result2.length() + 1;
                                    result2.insert(0, ch3);
                                    foundType4 = 1;
                                    foundType3 = -1;
                                } else {
                                    this.lastText = text;
                                    this.lastPosition = position;
                                    this.messages = messageObjects;
                                    return;
                                }
                            } else if (a6 == 0 && this.botInfo != null && ch3 == '/') {
                                this.resultStartPosition = a6;
                                this.resultLength = result2.length() + 1;
                                foundType4 = 2;
                                foundType3 = -1;
                                break;
                            } else if (ch3 != ':' || result2.length() <= 0) {
                                foundType5 = foundType2;
                            } else {
                                boolean isNextPunctiationChar = punctuationsChars.indexOf(result2.charAt(0)) >= 0;
                                if (!isNextPunctiationChar) {
                                    break;
                                }
                                foundType5 = foundType2;
                                if (result2.length() > 1) {
                                    break;
                                }
                            }
                            result2.insert(0, ch3);
                        }
                        a6--;
                        foundType2 = foundType5;
                    }
                    this.resultStartPosition = a6;
                    this.resultLength = result2.length() + 1;
                    foundType4 = 3;
                    foundType3 = -1;
                }
                if (foundType4 == -1) {
                    this.delegate.needChangePanelVisibility(false);
                    return;
                } else if (foundType4 != 0) {
                    int foundType10 = foundType4;
                    if (foundType10 == 1) {
                        ArrayList<String> newResult = new ArrayList<>();
                        String hashtagString = result2.toString().toLowerCase();
                        ArrayList<SearchAdapterHelper.HashtagObject> hashtags = this.searchAdapterHelper.getHashtags();
                        for (int a7 = 0; a7 < hashtags.size(); a7++) {
                            SearchAdapterHelper.HashtagObject hashtagObject = hashtags.get(a7);
                            if (hashtagObject != null && hashtagObject.hashtag != null && hashtagObject.hashtag.startsWith(hashtagString)) {
                                newResult.add(hashtagObject.hashtag);
                            }
                        }
                        this.searchResultHashtags = newResult;
                        this.stickers = null;
                        this.searchResultUsernames = null;
                        this.searchResultUsernamesMap = null;
                        this.searchResultCommands = null;
                        this.searchResultCommandsHelp = null;
                        this.searchResultCommandsUsers = null;
                        this.searchResultSuggestions = null;
                        notifyDataSetChanged();
                        this.delegate.needChangePanelVisibility(!this.searchResultHashtags.isEmpty());
                        return;
                    } else if (foundType10 != 2) {
                        if (foundType10 == 3) {
                            String[] newLanguage = AndroidUtilities.getCurrentKeyboardLanguage();
                            if (!Arrays.equals(newLanguage, this.lastSearchKeyboardLanguage)) {
                                MediaDataController.getInstance(this.currentAccount).fetchNewEmojiKeywords(newLanguage);
                            }
                            this.lastSearchKeyboardLanguage = newLanguage;
                            MediaDataController.getInstance(this.currentAccount).getEmojiSuggestions(this.lastSearchKeyboardLanguage, result2.toString(), false, new MediaDataController.KeywordResultCallback() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda6
                                @Override // org.telegram.messenger.MediaDataController.KeywordResultCallback
                                public final void run(ArrayList arrayList3, String str2) {
                                    MentionsAdapter.this.m1487xf1b585d5(arrayList3, str2);
                                }
                            });
                            return;
                        } else if (foundType10 == 4) {
                            this.searchResultHashtags = null;
                            this.searchResultUsernames = null;
                            this.searchResultUsernamesMap = null;
                            this.searchResultSuggestions = null;
                            this.searchResultCommands = null;
                            this.searchResultCommandsHelp = null;
                            this.searchResultCommandsUsers = null;
                            return;
                        } else {
                            return;
                        }
                    } else {
                        ArrayList<String> newResult2 = new ArrayList<>();
                        ArrayList<String> newResultHelp = new ArrayList<>();
                        ArrayList<TLRPC.User> newResultUsers = new ArrayList<>();
                        String command = result2.toString().toLowerCase();
                        for (int b = 0; b < this.botInfo.size(); b++) {
                            TLRPC.BotInfo info = this.botInfo.valueAt(b);
                            for (int a8 = 0; a8 < info.commands.size(); a8++) {
                                TLRPC.TL_botCommand botCommand = info.commands.get(a8);
                                if (botCommand != null && botCommand.command != null && botCommand.command.startsWith(command)) {
                                    newResult2.add("/" + botCommand.command);
                                    newResultHelp.add(botCommand.description);
                                    newResultUsers.add(messagesController3.getUser(Long.valueOf(info.user_id)));
                                }
                            }
                        }
                        this.searchResultHashtags = null;
                        this.stickers = null;
                        this.searchResultUsernames = null;
                        this.searchResultUsernamesMap = null;
                        this.searchResultSuggestions = null;
                        this.searchResultCommands = newResult2;
                        this.searchResultCommandsHelp = newResultHelp;
                        this.searchResultCommandsUsers = newResultUsers;
                        notifyDataSetChanged();
                        this.delegate.needChangePanelVisibility(!newResult2.isEmpty());
                        return;
                    }
                } else {
                    final ArrayList<Long> users = new ArrayList<>();
                    for (int a9 = 0; a9 < Math.min(100, messageObjects.size()); a9++) {
                        long from_id = messageObjects.get(a9).getFromChatId();
                        if (from_id > 0 && !users.contains(Long.valueOf(from_id))) {
                            users.add(Long.valueOf(from_id));
                        }
                    }
                    String usernameString = result2.toString().toLowerCase();
                    boolean hasSpace = usernameString.indexOf(32) >= 0;
                    final ArrayList<TLObject> newResult3 = new ArrayList<>();
                    LongSparseArray<TLRPC.User> newResultsHashMap3 = new LongSparseArray<>();
                    final LongSparseArray<TLObject> newMap = new LongSparseArray<>();
                    int foundType11 = this.currentAccount;
                    ArrayList<TLRPC.TL_topPeer> inlineBots3 = MediaDataController.getInstance(foundType11).inlineBots;
                    if (!z2) {
                        originalEmoji2 = originalEmoji;
                        if (!this.needBotContext || foundType3 != 0 || inlineBots3.isEmpty()) {
                            searchPostion = searchPostion3;
                        } else {
                            int count = 0;
                            int a10 = 0;
                            while (true) {
                                if (a10 >= inlineBots3.size()) {
                                    searchPostion = searchPostion3;
                                    break;
                                }
                                TLRPC.User user = messagesController3.getUser(Long.valueOf(inlineBots3.get(a10).peer.user_id));
                                if (user == null) {
                                    searchPostion = searchPostion3;
                                    result = result2;
                                } else {
                                    if (TextUtils.isEmpty(user.username)) {
                                        searchPostion = searchPostion3;
                                        result = result2;
                                    } else if (usernameString.length() == 0 || user.username.toLowerCase().startsWith(usernameString)) {
                                        newResult3.add(user);
                                        searchPostion = searchPostion3;
                                        result = result2;
                                        newResultsHashMap3.put(user.id, user);
                                        newMap.put(user.id, user);
                                        count++;
                                    } else {
                                        searchPostion = searchPostion3;
                                        result = result2;
                                    }
                                    if (count == 5) {
                                        break;
                                    }
                                }
                                a10++;
                                searchPostion3 = searchPostion;
                                result2 = result;
                            }
                        }
                    } else {
                        originalEmoji2 = originalEmoji;
                        searchPostion = searchPostion3;
                    }
                    ChatActivity chatActivity2 = this.parentFragment;
                    if (chatActivity2 != null) {
                        TLRPC.Chat chat2 = chatActivity2.getCurrentChat();
                        threadId = this.parentFragment.getThreadId();
                        chat = chat2;
                    } else {
                        TLRPC.ChatFull chatFull2 = this.info;
                        if (chatFull2 != null) {
                            threadId = 0;
                            chat = messagesController3.getChat(Long.valueOf(chatFull2.id));
                        } else {
                            threadId = 0;
                            chat = null;
                        }
                    }
                    if (chat == null || (chatFull = this.info) == null || chatFull.participants == null) {
                        newResultsHashMap = newResultsHashMap3;
                        inlineBots = inlineBots3;
                        messagesController = messagesController3;
                    } else if (!ChatObject.isChannel(chat) || chat.megagroup) {
                        int a11 = forSearch ? -1 : 0;
                        while (a11 < this.info.participants.participants.size()) {
                            if (a11 == -1) {
                                if (usernameString.length() == 0) {
                                    newResult3.add(chat);
                                    newResultsHashMap2 = newResultsHashMap3;
                                    inlineBots2 = inlineBots3;
                                    messagesController2 = messagesController3;
                                    searchPostion2 = searchPostion;
                                    a11++;
                                    z2 = usernameOnly;
                                    messagesController3 = messagesController2;
                                    inlineBots3 = inlineBots2;
                                    newResultsHashMap3 = newResultsHashMap2;
                                    searchPostion = searchPostion2;
                                } else {
                                    String firstName2 = chat.title;
                                    String username3 = chat.username;
                                    id = -chat.id;
                                    searchPostion2 = searchPostion;
                                    object = chat;
                                    inlineBots2 = inlineBots3;
                                    firstName = firstName2;
                                    messagesController2 = messagesController3;
                                    lastName = null;
                                    newResultsHashMap2 = newResultsHashMap3;
                                    username = username3;
                                    if ((!TextUtils.isEmpty(username) && username.toLowerCase().startsWith(usernameString)) || ((!TextUtils.isEmpty(firstName) && firstName.toLowerCase().startsWith(usernameString)) || ((!TextUtils.isEmpty(lastName) && lastName.toLowerCase().startsWith(usernameString)) || (hasSpace && ContactsController.formatName(firstName, lastName).toLowerCase().startsWith(usernameString))))) {
                                        newResult3.add(object);
                                        newMap.put(id, object);
                                    }
                                    a11++;
                                    z2 = usernameOnly;
                                    messagesController3 = messagesController2;
                                    inlineBots3 = inlineBots2;
                                    newResultsHashMap3 = newResultsHashMap2;
                                    searchPostion = searchPostion2;
                                }
                            } else {
                                TLRPC.ChatParticipant chatParticipant = this.info.participants.participants.get(a11);
                                TLRPC.User user2 = messagesController3.getUser(Long.valueOf(chatParticipant.user_id));
                                if (user2 == null) {
                                    newResultsHashMap2 = newResultsHashMap3;
                                    inlineBots2 = inlineBots3;
                                    messagesController2 = messagesController3;
                                    searchPostion2 = searchPostion;
                                } else if (z2 || !UserObject.isUserSelf(user2)) {
                                    inlineBots2 = inlineBots3;
                                    messagesController2 = messagesController3;
                                    if (newResultsHashMap3.indexOfKey(user2.id) >= 0) {
                                        newResultsHashMap2 = newResultsHashMap3;
                                        searchPostion2 = searchPostion;
                                    } else if (usernameString.length() == 0 && !user2.deleted) {
                                        newResult3.add(user2);
                                        newResultsHashMap2 = newResultsHashMap3;
                                        searchPostion2 = searchPostion;
                                    } else {
                                        String firstName3 = user2.first_name;
                                        String lastName2 = user2.last_name;
                                        newResultsHashMap2 = newResultsHashMap3;
                                        username = user2.username;
                                        id = user2.id;
                                        firstName = firstName3;
                                        lastName = lastName2;
                                        searchPostion2 = searchPostion;
                                        object = user2;
                                        if (!TextUtils.isEmpty(username)) {
                                            newResult3.add(object);
                                            newMap.put(id, object);
                                        }
                                        newResult3.add(object);
                                        newMap.put(id, object);
                                    }
                                } else {
                                    newResultsHashMap2 = newResultsHashMap3;
                                    inlineBots2 = inlineBots3;
                                    messagesController2 = messagesController3;
                                    searchPostion2 = searchPostion;
                                }
                                a11++;
                                z2 = usernameOnly;
                                messagesController3 = messagesController2;
                                inlineBots3 = inlineBots2;
                                newResultsHashMap3 = newResultsHashMap2;
                                searchPostion = searchPostion2;
                            }
                        }
                        newResultsHashMap = newResultsHashMap3;
                        inlineBots = inlineBots3;
                        messagesController = messagesController3;
                    } else {
                        newResultsHashMap = newResultsHashMap3;
                        inlineBots = inlineBots3;
                        messagesController = messagesController3;
                    }
                    Collections.sort(newResult3, new Comparator<TLObject>() { // from class: org.telegram.ui.Adapters.MentionsAdapter.6
                        private long getId(TLObject object2) {
                            if (object2 instanceof TLRPC.User) {
                                return ((TLRPC.User) object2).id;
                            }
                            return -((TLRPC.Chat) object2).id;
                        }

                        public int compare(TLObject lhs, TLObject rhs) {
                            long id1 = getId(lhs);
                            long id2 = getId(rhs);
                            if (newMap.indexOfKey(id1) < 0 || newMap.indexOfKey(id2) < 0) {
                                if (newMap.indexOfKey(id1) >= 0) {
                                    return -1;
                                }
                                if (newMap.indexOfKey(id2) >= 0) {
                                    return 1;
                                }
                                int lhsNum = users.indexOf(Long.valueOf(id1));
                                int rhsNum = users.indexOf(Long.valueOf(id2));
                                if (lhsNum != -1 && rhsNum != -1) {
                                    if (lhsNum < rhsNum) {
                                        return -1;
                                    }
                                    return lhsNum == rhsNum ? 0 : 1;
                                } else if (lhsNum != -1 && rhsNum == -1) {
                                    return -1;
                                } else {
                                    return (lhsNum != -1 || rhsNum == -1) ? 0 : 1;
                                }
                            }
                            return 0;
                        }
                    });
                    this.searchResultHashtags = null;
                    this.stickers = null;
                    this.searchResultCommands = null;
                    this.searchResultCommandsHelp = null;
                    this.searchResultCommandsUsers = null;
                    this.searchResultSuggestions = null;
                    if (chat == null || !chat.megagroup || usernameString.length() <= 0) {
                        showUsersResult(newResult3, newMap, true);
                        return;
                    }
                    if (newResult3.size() < 5) {
                        Runnable runnable3 = new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                MentionsAdapter.this.m1486xea5050b6(newResult3, newMap);
                            }
                        };
                        this.cancelDelayRunnable = runnable3;
                        AndroidUtilities.runOnUIThread(runnable3, 1000L);
                    } else {
                        showUsersResult(newResult3, newMap, true);
                    }
                    AnonymousClass7 anonymousClass7 = new AnonymousClass7(chat, usernameString, threadId, newResult3, newMap, messagesController);
                    this.searchGlobalRunnable = anonymousClass7;
                    AndroidUtilities.runOnUIThread(anonymousClass7, 200L);
                    return;
                }
            }
            str = null;
        } else {
            str = null;
        }
        searchForContextBot(str, str);
        this.delegate.needChangePanelVisibility(false);
        this.lastText = str;
        clearStickers();
    }

    /* renamed from: lambda$searchUsernameOrHashtag$7$org-telegram-ui-Adapters-MentionsAdapter */
    public /* synthetic */ void m1486xea5050b6(ArrayList newResult, LongSparseArray newMap) {
        this.cancelDelayRunnable = null;
        showUsersResult(newResult, newMap, true);
    }

    /* renamed from: org.telegram.ui.Adapters.MentionsAdapter$7 */
    /* loaded from: classes4.dex */
    public class AnonymousClass7 implements Runnable {
        final /* synthetic */ TLRPC.Chat val$chat;
        final /* synthetic */ MessagesController val$messagesController;
        final /* synthetic */ LongSparseArray val$newMap;
        final /* synthetic */ ArrayList val$newResult;
        final /* synthetic */ int val$threadId;
        final /* synthetic */ String val$usernameString;

        AnonymousClass7(TLRPC.Chat chat, String str, int i, ArrayList arrayList, LongSparseArray longSparseArray, MessagesController messagesController) {
            MentionsAdapter.this = this$0;
            this.val$chat = chat;
            this.val$usernameString = str;
            this.val$threadId = i;
            this.val$newResult = arrayList;
            this.val$newMap = longSparseArray;
            this.val$messagesController = messagesController;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (MentionsAdapter.this.searchGlobalRunnable != this) {
                return;
            }
            TLRPC.TL_channels_getParticipants req = new TLRPC.TL_channels_getParticipants();
            req.channel = MessagesController.getInputChannel(this.val$chat);
            req.limit = 20;
            req.offset = 0;
            TLRPC.TL_channelParticipantsMentions channelParticipantsMentions = new TLRPC.TL_channelParticipantsMentions();
            channelParticipantsMentions.flags |= 1;
            channelParticipantsMentions.q = this.val$usernameString;
            if (this.val$threadId != 0) {
                channelParticipantsMentions.flags |= 2;
                channelParticipantsMentions.top_msg_id = this.val$threadId;
            }
            req.filter = channelParticipantsMentions;
            final int currentReqId = MentionsAdapter.access$1704(MentionsAdapter.this);
            MentionsAdapter mentionsAdapter = MentionsAdapter.this;
            ConnectionsManager connectionsManager = ConnectionsManager.getInstance(mentionsAdapter.currentAccount);
            final ArrayList arrayList = this.val$newResult;
            final LongSparseArray longSparseArray = this.val$newMap;
            final MessagesController messagesController = this.val$messagesController;
            mentionsAdapter.channelReqId = connectionsManager.sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$7$$ExternalSyntheticLambda1
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MentionsAdapter.AnonymousClass7.this.m1491lambda$run$1$orgtelegramuiAdaptersMentionsAdapter$7(currentReqId, arrayList, longSparseArray, messagesController, tLObject, tL_error);
                }
            });
        }

        /* renamed from: lambda$run$1$org-telegram-ui-Adapters-MentionsAdapter$7 */
        public /* synthetic */ void m1491lambda$run$1$orgtelegramuiAdaptersMentionsAdapter$7(final int currentReqId, final ArrayList newResult, final LongSparseArray newMap, final MessagesController messagesController, final TLObject response, final TLRPC.TL_error error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MentionsAdapter.AnonymousClass7.this.m1490lambda$run$0$orgtelegramuiAdaptersMentionsAdapter$7(currentReqId, newResult, newMap, error, response, messagesController);
                }
            });
        }

        /* renamed from: lambda$run$0$org-telegram-ui-Adapters-MentionsAdapter$7 */
        public /* synthetic */ void m1490lambda$run$0$orgtelegramuiAdaptersMentionsAdapter$7(int currentReqId, ArrayList newResult, LongSparseArray newMap, TLRPC.TL_error error, TLObject response, MessagesController messagesController) {
            if (MentionsAdapter.this.channelReqId != 0 && currentReqId == MentionsAdapter.this.channelLastReqId && MentionsAdapter.this.searchResultUsernamesMap != null && MentionsAdapter.this.searchResultUsernames != null) {
                MentionsAdapter.this.showUsersResult(newResult, newMap, false);
                if (error == null) {
                    TLRPC.TL_channels_channelParticipants res = (TLRPC.TL_channels_channelParticipants) response;
                    messagesController.putUsers(res.users, false);
                    messagesController.putChats(res.chats, false);
                    boolean z = !MentionsAdapter.this.searchResultUsernames.isEmpty();
                    if (!res.participants.isEmpty()) {
                        long currentUserId = UserConfig.getInstance(MentionsAdapter.this.currentAccount).getClientUserId();
                        for (int a = 0; a < res.participants.size(); a++) {
                            TLRPC.ChannelParticipant participant = res.participants.get(a);
                            long peerId = MessageObject.getPeerId(participant.peer);
                            if (MentionsAdapter.this.searchResultUsernamesMap.indexOfKey(peerId) < 0 && (MentionsAdapter.this.isSearchingMentions || peerId != currentUserId)) {
                                if (peerId >= 0) {
                                    TLRPC.User user = messagesController.getUser(Long.valueOf(peerId));
                                    if (user != null) {
                                        MentionsAdapter.this.searchResultUsernames.add(user);
                                    } else {
                                        return;
                                    }
                                } else {
                                    TLRPC.Chat chat = messagesController.getChat(Long.valueOf(-peerId));
                                    if (chat != null) {
                                        MentionsAdapter.this.searchResultUsernames.add(chat);
                                    } else {
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
                MentionsAdapter.this.notifyDataSetChanged();
                MentionsAdapter.this.delegate.needChangePanelVisibility(!MentionsAdapter.this.searchResultUsernames.isEmpty());
                MentionsAdapter.this.channelReqId = 0;
            }
            MentionsAdapter.this.channelReqId = 0;
        }
    }

    /* renamed from: lambda$searchUsernameOrHashtag$8$org-telegram-ui-Adapters-MentionsAdapter */
    public /* synthetic */ void m1487xf1b585d5(ArrayList param, String alias) {
        this.searchResultSuggestions = param;
        this.searchResultHashtags = null;
        this.stickers = null;
        this.searchResultUsernames = null;
        this.searchResultUsernamesMap = null;
        this.searchResultCommands = null;
        this.searchResultCommandsHelp = null;
        this.searchResultCommandsUsers = null;
        notifyDataSetChanged();
        MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
        ArrayList<MediaDataController.KeywordResult> arrayList = this.searchResultSuggestions;
        mentionsAdapterDelegate.needChangePanelVisibility(arrayList != null && !arrayList.isEmpty());
    }

    public void setIsReversed(boolean isReversed) {
        if (this.isReversed != isReversed) {
            this.isReversed = isReversed;
            int itemCount = getLastItemCount();
            if (itemCount > 0) {
                notifyItemChanged(0);
            }
            if (itemCount > 1) {
                notifyItemChanged(itemCount - 1);
            }
        }
    }

    public void showUsersResult(ArrayList<TLObject> newResult, LongSparseArray<TLObject> newMap, boolean notify) {
        this.searchResultUsernames = newResult;
        this.searchResultUsernamesMap = newMap;
        Runnable runnable = this.cancelDelayRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.cancelDelayRunnable = null;
        }
        this.searchResultBotContext = null;
        this.stickers = null;
        if (notify) {
            notifyDataSetChanged();
            this.delegate.needChangePanelVisibility(!this.searchResultUsernames.isEmpty());
        }
    }

    public int getResultStartPosition() {
        return this.resultStartPosition;
    }

    public int getResultLength() {
        return this.resultLength;
    }

    public ArrayList<TLRPC.BotInlineResult> getSearchResultBotContext() {
        return this.searchResultBotContext;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        int itemCountInternal = getItemCountInternal();
        this.lastItemCount = itemCountInternal;
        return itemCountInternal;
    }

    public int getLastItemCount() {
        return this.lastItemCount;
    }

    public int getItemCountInternal() {
        int i = 1;
        if (this.foundContextBot == null || this.inlineMediaEnabled) {
            ArrayList<StickerResult> arrayList = this.stickers;
            if (arrayList != null) {
                return arrayList.size();
            }
            ArrayList<TLRPC.BotInlineResult> arrayList2 = this.searchResultBotContext;
            if (arrayList2 != null) {
                int size = arrayList2.size();
                if (this.searchResultBotContextSwitch == null) {
                    i = 0;
                }
                return size + i;
            }
            ArrayList<TLObject> arrayList3 = this.searchResultUsernames;
            if (arrayList3 != null) {
                return arrayList3.size();
            }
            ArrayList<String> arrayList4 = this.searchResultHashtags;
            if (arrayList4 != null) {
                return arrayList4.size();
            }
            ArrayList<String> arrayList5 = this.searchResultCommands;
            if (arrayList5 != null) {
                return arrayList5.size();
            }
            ArrayList<MediaDataController.KeywordResult> arrayList6 = this.searchResultSuggestions;
            if (arrayList6 == null) {
                return 0;
            }
            return arrayList6.size();
        }
        return 1;
    }

    public void clear(boolean safe) {
        if (safe && (this.channelReqId != 0 || this.contextQueryReqid != 0 || this.contextUsernameReqid != 0 || this.lastReqId != 0)) {
            return;
        }
        this.foundContextBot = null;
        ArrayList<StickerResult> arrayList = this.stickers;
        if (arrayList != null) {
            arrayList.clear();
        }
        ArrayList<TLRPC.BotInlineResult> arrayList2 = this.searchResultBotContext;
        if (arrayList2 != null) {
            arrayList2.clear();
        }
        this.searchResultBotContextSwitch = null;
        ArrayList<TLObject> arrayList3 = this.searchResultUsernames;
        if (arrayList3 != null) {
            arrayList3.clear();
        }
        ArrayList<String> arrayList4 = this.searchResultHashtags;
        if (arrayList4 != null) {
            arrayList4.clear();
        }
        ArrayList<String> arrayList5 = this.searchResultCommands;
        if (arrayList5 != null) {
            arrayList5.clear();
        }
        ArrayList<MediaDataController.KeywordResult> arrayList6 = this.searchResultSuggestions;
        if (arrayList6 != null) {
            arrayList6.clear();
        }
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        if (this.stickers != null) {
            return 4;
        }
        if (this.foundContextBot != null && !this.inlineMediaEnabled) {
            return 3;
        }
        if (this.searchResultBotContext != null) {
            if (position == 0 && this.searchResultBotContextSwitch != null) {
                return 2;
            }
            return 1;
        }
        return 0;
    }

    public void addHashtagsFromMessage(CharSequence message) {
        this.searchAdapterHelper.addHashtagsFromMessage(message);
    }

    public int getItemPosition(int i) {
        if (this.searchResultBotContext != null && this.searchResultBotContextSwitch != null) {
            return i - 1;
        }
        return i;
    }

    public Object getItemParent(int i) {
        ArrayList<StickerResult> arrayList = this.stickers;
        if (arrayList == null || i < 0 || i >= arrayList.size()) {
            return null;
        }
        return this.stickers.get(i).parent;
    }

    public Object getItem(int i) {
        ArrayList<StickerResult> arrayList = this.stickers;
        if (arrayList != null) {
            if (i >= 0 && i < arrayList.size()) {
                return this.stickers.get(i).sticker;
            }
            return null;
        }
        ArrayList<TLRPC.BotInlineResult> arrayList2 = this.searchResultBotContext;
        if (arrayList2 != null) {
            TLRPC.TL_inlineBotSwitchPM tL_inlineBotSwitchPM = this.searchResultBotContextSwitch;
            if (tL_inlineBotSwitchPM != null) {
                if (i == 0) {
                    return tL_inlineBotSwitchPM;
                }
                i--;
            }
            if (i >= 0 && i < arrayList2.size()) {
                return this.searchResultBotContext.get(i);
            }
            return null;
        }
        ArrayList<TLObject> arrayList3 = this.searchResultUsernames;
        if (arrayList3 != null) {
            if (i >= 0 && i < arrayList3.size()) {
                return this.searchResultUsernames.get(i);
            }
            return null;
        }
        ArrayList<String> arrayList4 = this.searchResultHashtags;
        if (arrayList4 != null) {
            if (i >= 0 && i < arrayList4.size()) {
                return this.searchResultHashtags.get(i);
            }
            return null;
        }
        ArrayList<MediaDataController.KeywordResult> arrayList5 = this.searchResultSuggestions;
        if (arrayList5 != null) {
            if (i >= 0 && i < arrayList5.size()) {
                return this.searchResultSuggestions.get(i);
            }
            return null;
        }
        ArrayList<String> arrayList6 = this.searchResultCommands;
        if (arrayList6 == null || i < 0 || i >= arrayList6.size()) {
            return null;
        }
        ArrayList<TLRPC.User> arrayList7 = this.searchResultCommandsUsers;
        if (arrayList7 != null && (this.botsCount != 1 || (this.info instanceof TLRPC.TL_channelFull))) {
            if (arrayList7.get(i) == null) {
                return String.format("%s", this.searchResultCommands.get(i));
            }
            Object[] objArr = new Object[2];
            objArr[0] = this.searchResultCommands.get(i);
            objArr[1] = this.searchResultCommandsUsers.get(i) != null ? this.searchResultCommandsUsers.get(i).username : "";
            return String.format("%s@%s", objArr);
        }
        return this.searchResultCommands.get(i);
    }

    public boolean isLongClickEnabled() {
        return (this.searchResultHashtags == null && this.searchResultCommands == null) ? false : true;
    }

    public boolean isBotCommands() {
        return this.searchResultCommands != null;
    }

    public boolean isStickers() {
        return this.stickers != null;
    }

    public boolean isBotContext() {
        return this.searchResultBotContext != null;
    }

    public boolean isBannedInline() {
        return this.foundContextBot != null && !this.inlineMediaEnabled;
    }

    public boolean isMediaLayout() {
        return this.contextMedia || this.stickers != null;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
    public boolean isEnabled(RecyclerView.ViewHolder holder) {
        return (this.foundContextBot == null || this.inlineMediaEnabled) && this.stickers == null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = new MentionCell(this.mContext, this.resourcesProvider);
                ((MentionCell) view).setIsDarkTheme(this.isDarkTheme);
                break;
            case 1:
                view = new ContextLinkCell(this.mContext);
                ((ContextLinkCell) view).setDelegate(new ContextLinkCell.ContextLinkCellDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda9
                    @Override // org.telegram.ui.Cells.ContextLinkCell.ContextLinkCellDelegate
                    public final void didPressedImage(ContextLinkCell contextLinkCell) {
                        MentionsAdapter.this.m1478xddefa6d9(contextLinkCell);
                    }
                });
                break;
            case 2:
                view = new BotSwitchCell(this.mContext);
                break;
            case 3:
                TextView textView = new TextView(this.mContext);
                textView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                textView.setTextSize(1, 14.0f);
                textView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText2));
                view = textView;
                break;
            default:
                view = new StickerCell(this.mContext);
                break;
        }
        return new RecyclerListView.Holder(view);
    }

    /* renamed from: lambda$onCreateViewHolder$9$org-telegram-ui-Adapters-MentionsAdapter */
    public /* synthetic */ void m1478xddefa6d9(ContextLinkCell cell) {
        this.delegate.onContextClick(cell.getResult());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = holder.getItemViewType();
        if (type == 4) {
            StickerCell stickerCell = (StickerCell) holder.itemView;
            StickerResult result = this.stickers.get(position);
            stickerCell.setSticker(result.sticker, result.parent);
            stickerCell.setClearsInputField(true);
        } else if (type == 3) {
            TextView textView = (TextView) holder.itemView;
            TLRPC.Chat chat = this.parentFragment.getCurrentChat();
            if (chat != null) {
                if (!ChatObject.hasAdminRights(chat) && chat.default_banned_rights != null && chat.default_banned_rights.send_inline) {
                    textView.setText(LocaleController.getString("GlobalAttachInlineRestricted", R.string.GlobalAttachInlineRestricted));
                } else if (AndroidUtilities.isBannedForever(chat.banned_rights)) {
                    textView.setText(LocaleController.getString("AttachInlineRestrictedForever", R.string.AttachInlineRestrictedForever));
                } else {
                    textView.setText(LocaleController.formatString("AttachInlineRestricted", R.string.AttachInlineRestricted, LocaleController.formatDateForBan(chat.banned_rights.until_date)));
                }
            }
        } else if (this.searchResultBotContext != null) {
            boolean hasTop = this.searchResultBotContextSwitch != null;
            if (holder.getItemViewType() == 2) {
                if (hasTop) {
                    ((BotSwitchCell) holder.itemView).setText(this.searchResultBotContextSwitch.text);
                    return;
                }
                return;
            }
            if (hasTop) {
                position--;
            }
            ((ContextLinkCell) holder.itemView).setLink(this.searchResultBotContext.get(position), this.foundContextBot, this.contextMedia, position != this.searchResultBotContext.size() - 1, hasTop && position == 0, "gif".equals(this.searchingContextUsername));
        } else {
            ArrayList<TLObject> arrayList = this.searchResultUsernames;
            if (arrayList != null) {
                TLObject object = arrayList.get(position);
                if (object instanceof TLRPC.User) {
                    ((MentionCell) holder.itemView).setUser((TLRPC.User) object);
                } else if (object instanceof TLRPC.Chat) {
                    ((MentionCell) holder.itemView).setChat((TLRPC.Chat) object);
                }
            } else if (this.searchResultHashtags != null) {
                ((MentionCell) holder.itemView).setText(this.searchResultHashtags.get(position));
            } else if (this.searchResultSuggestions != null) {
                ((MentionCell) holder.itemView).setEmojiSuggestion(this.searchResultSuggestions.get(position));
            } else if (this.searchResultCommands != null) {
                MentionCell mentionCell = (MentionCell) holder.itemView;
                String str = this.searchResultCommands.get(position);
                String str2 = this.searchResultCommandsHelp.get(position);
                ArrayList<TLRPC.User> arrayList2 = this.searchResultCommandsUsers;
                mentionCell.setBotCommand(str, str2, arrayList2 != null ? arrayList2.get(position) : null);
            }
            ((MentionCell) holder.itemView).setDivider(false);
        }
    }

    public void onRequestPermissionsResultFragment(int requestCode, String[] permissions, int[] grantResults) {
        TLRPC.User user;
        if (requestCode == 2 && (user = this.foundContextBot) != null && user.bot_inline_geo) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                this.locationProvider.start();
            } else {
                onLocationUnavailable();
            }
        }
    }

    public void doSomeStickersAction() {
        if (isStickers()) {
            if (this.mentionsStickersActionTracker == null) {
                EmojiView.ChooseStickerActionTracker chooseStickerActionTracker = new EmojiView.ChooseStickerActionTracker(this.currentAccount, this.dialog_id, this.threadMessageId) { // from class: org.telegram.ui.Adapters.MentionsAdapter.8
                    @Override // org.telegram.ui.Components.EmojiView.ChooseStickerActionTracker
                    public boolean isShown() {
                        return MentionsAdapter.this.isStickers();
                    }
                };
                this.mentionsStickersActionTracker = chooseStickerActionTracker;
                chooseStickerActionTracker.checkVisibility();
            }
            this.mentionsStickersActionTracker.doSomeAction();
        }
    }

    private int getThemedColor(String key) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(key) : null;
        return color != null ? color.intValue() : Theme.getColor(key);
    }
}
