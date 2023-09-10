package org.telegram.ui.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$BotInfo;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$ChatParticipant;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_botCommand;
import org.telegram.tgnet.TLRPC$TL_botInlineMessageMediaAuto;
import org.telegram.tgnet.TLRPC$TL_channelFull;
import org.telegram.tgnet.TLRPC$TL_channelParticipantsMentions;
import org.telegram.tgnet.TLRPC$TL_channels_channelParticipants;
import org.telegram.tgnet.TLRPC$TL_channels_getParticipants;
import org.telegram.tgnet.TLRPC$TL_chatBannedRights;
import org.telegram.tgnet.TLRPC$TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC$TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeSticker;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inlineBotSwitchPM;
import org.telegram.tgnet.TLRPC$TL_inlineBotWebView;
import org.telegram.tgnet.TLRPC$TL_inputGeoPoint;
import org.telegram.tgnet.TLRPC$TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC$TL_messages_botResults;
import org.telegram.tgnet.TLRPC$TL_messages_getInlineBotResults;
import org.telegram.tgnet.TLRPC$TL_messages_getStickers;
import org.telegram.tgnet.TLRPC$TL_messages_stickers;
import org.telegram.tgnet.TLRPC$TL_photo;
import org.telegram.tgnet.TLRPC$TL_photoSize;
import org.telegram.tgnet.TLRPC$TL_photoSizeProgressive;
import org.telegram.tgnet.TLRPC$TL_topPeer;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.MentionsAdapter;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Cells.BotSwitchCell;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.MentionCell;
import org.telegram.ui.Cells.StickerCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class MentionsAdapter extends RecyclerListView.SelectionAdapter implements NotificationCenter.NotificationCenterDelegate {
    private LongSparseArray<TLRPC$BotInfo> botInfo;
    private int botsCount;
    private Runnable cancelDelayRunnable;
    private int channelLastReqId;
    private int channelReqId;
    private TLRPC$Chat chat;
    private boolean contextMedia;
    private int contextQueryReqid;
    private Runnable contextQueryRunnable;
    private int contextUsernameReqid;
    private MentionsAdapterDelegate delegate;
    private long dialog_id;
    private TLRPC$User foundContextBot;
    private TLRPC$ChatFull info;
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
    private ArrayList<TLRPC$BotInlineResult> searchResultBotContext;
    private TLRPC$TL_inlineBotSwitchPM searchResultBotContextSwitch;
    private TLRPC$TL_inlineBotWebView searchResultBotWebViewSwitch;
    private ArrayList<String> searchResultCommands;
    private ArrayList<String> searchResultCommandsHelp;
    private ArrayList<TLRPC$User> searchResultCommandsUsers;
    private ArrayList<String> searchResultHashtags;
    private ArrayList<MediaDataController.KeywordResult> searchResultSuggestions;
    private ArrayList<TLObject> searchResultUsernames;
    private LongSparseArray<TLObject> searchResultUsernamesMap;
    private String searchingContextQuery;
    private String searchingContextUsername;
    private ArrayList<StickerResult> stickers;
    private HashMap<String, TLRPC$Document> stickersMap;
    private int threadMessageId;
    private TLRPC$User user;
    private boolean visibleByStickersSearch;
    private boolean allowStickers = true;
    private boolean allowBots = true;
    private boolean allowChats = true;
    private int currentAccount = UserConfig.selectedAccount;
    private boolean needUsernames = true;
    private boolean needBotContext = true;
    private boolean inlineMediaEnabled = true;
    private boolean searchInDailogs = false;
    private ArrayList<String> stickersToLoad = new ArrayList<>();
    private SendMessagesHelper.LocationProvider locationProvider = new SendMessagesHelper.LocationProvider(new SendMessagesHelper.LocationProvider.LocationProviderDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter.1
        @Override // org.telegram.messenger.SendMessagesHelper.LocationProvider.LocationProviderDelegate
        public void onLocationAcquired(Location location) {
            if (MentionsAdapter.this.foundContextBot == null || !MentionsAdapter.this.foundContextBot.bot_inline_geo) {
                return;
            }
            MentionsAdapter.this.lastKnownLocation = location;
            MentionsAdapter mentionsAdapter = MentionsAdapter.this;
            mentionsAdapter.searchForContextBotResults(true, mentionsAdapter.foundContextBot, MentionsAdapter.this.searchingContextQuery, "");
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

    /* loaded from: classes3.dex */
    public interface MentionsAdapterDelegate {
        void needChangePanelVisibility(boolean z);

        void onContextClick(TLRPC$BotInlineResult tLRPC$BotInlineResult);

        void onContextSearch(boolean z);

        void onItemCountUpdate(int i, int i2);
    }

    static /* synthetic */ int access$1704(MentionsAdapter mentionsAdapter) {
        int i = mentionsAdapter.channelLastReqId + 1;
        mentionsAdapter.channelLastReqId = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class StickerResult {
        public Object parent;
        public TLRPC$Document sticker;

        public StickerResult(TLRPC$Document tLRPC$Document, Object obj) {
            this.sticker = tLRPC$Document;
            this.parent = obj;
        }
    }

    public MentionsAdapter(Context context, boolean z, long j, int i, MentionsAdapterDelegate mentionsAdapterDelegate, Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
        this.mContext = context;
        this.delegate = mentionsAdapterDelegate;
        this.isDarkTheme = z;
        this.dialog_id = j;
        this.threadMessageId = i;
        SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(true);
        this.searchAdapterHelper = searchAdapterHelper;
        searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter.3
            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public /* synthetic */ boolean canApplySearchResults(int i2) {
                return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$canApplySearchResults(this, i2);
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
                return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeCallParticipants(this);
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public /* synthetic */ LongSparseArray getExcludeUsers() {
                return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeUsers(this);
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public void onDataSetChanged(int i2) {
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
        if (z) {
            return;
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoadFailed);
    }

    public TLRPC$User getFoundContextBot() {
        return this.foundContextBot;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        ArrayList<StickerResult> arrayList;
        if ((i != NotificationCenter.fileLoaded && i != NotificationCenter.fileLoadFailed) || (arrayList = this.stickers) == null || arrayList.isEmpty() || this.stickersToLoad.isEmpty() || !this.visibleByStickersSearch) {
            return;
        }
        this.stickersToLoad.remove((String) objArr[0]);
        if (this.stickersToLoad.isEmpty()) {
            this.delegate.needChangePanelVisibility(getItemCountInternal() > 0);
        }
    }

    private void addStickerToResult(TLRPC$Document tLRPC$Document, Object obj) {
        if (tLRPC$Document == null) {
            return;
        }
        String str = tLRPC$Document.dc_id + "_" + tLRPC$Document.id;
        HashMap<String, TLRPC$Document> hashMap = this.stickersMap;
        if (hashMap == null || !hashMap.containsKey(str)) {
            if (UserConfig.getInstance(this.currentAccount).isPremium() || !MessageObject.isPremiumSticker(tLRPC$Document)) {
                if (this.stickers == null) {
                    this.stickers = new ArrayList<>();
                    this.stickersMap = new HashMap<>();
                }
                this.stickers.add(new StickerResult(tLRPC$Document, obj));
                this.stickersMap.put(str, tLRPC$Document);
                EmojiView.ChooseStickerActionTracker chooseStickerActionTracker = this.mentionsStickersActionTracker;
                if (chooseStickerActionTracker != null) {
                    chooseStickerActionTracker.checkVisibility();
                }
            }
        }
    }

    private void addStickersToResult(ArrayList<TLRPC$Document> arrayList, Object obj) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TLRPC$Document tLRPC$Document = arrayList.get(i);
            String str = tLRPC$Document.dc_id + "_" + tLRPC$Document.id;
            HashMap<String, TLRPC$Document> hashMap = this.stickersMap;
            if ((hashMap == null || !hashMap.containsKey(str)) && (UserConfig.getInstance(this.currentAccount).isPremium() || !MessageObject.isPremiumSticker(tLRPC$Document))) {
                int size2 = tLRPC$Document.attributes.size();
                int i2 = 0;
                while (true) {
                    if (i2 >= size2) {
                        break;
                    }
                    TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i2);
                    if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                        obj = tLRPC$DocumentAttribute.stickerset;
                        break;
                    }
                    i2++;
                }
                if (this.stickers == null) {
                    this.stickers = new ArrayList<>();
                    this.stickersMap = new HashMap<>();
                }
                this.stickers.add(new StickerResult(tLRPC$Document, obj));
                this.stickersMap.put(str, tLRPC$Document);
            }
        }
    }

    private boolean checkStickerFilesExistAndDownload() {
        if (this.stickers == null) {
            return false;
        }
        this.stickersToLoad.clear();
        int min = Math.min(6, this.stickers.size());
        for (int i = 0; i < min; i++) {
            StickerResult stickerResult = this.stickers.get(i);
            TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(stickerResult.sticker.thumbs, 90);
            if (((closestPhotoSizeWithSize instanceof TLRPC$TL_photoSize) || (closestPhotoSizeWithSize instanceof TLRPC$TL_photoSizeProgressive)) && !FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, "webp", true).exists()) {
                this.stickersToLoad.add(FileLoader.getAttachFileName(closestPhotoSizeWithSize, "webp"));
                FileLoader.getInstance(this.currentAccount).loadFile(ImageLocation.getForDocument(closestPhotoSizeWithSize, stickerResult.sticker), stickerResult.parent, "webp", 1, 1);
            }
        }
        return this.stickersToLoad.isEmpty();
    }

    private boolean isValidSticker(TLRPC$Document tLRPC$Document, String str) {
        int size = tLRPC$Document.attributes.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            }
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                String str2 = tLRPC$DocumentAttribute.alt;
                if (str2 == null || !str2.contains(str)) {
                    break;
                }
                return true;
            }
            i++;
        }
        return false;
    }

    private void searchServerStickers(final String str, String str2) {
        TLRPC$TL_messages_getStickers tLRPC$TL_messages_getStickers = new TLRPC$TL_messages_getStickers();
        tLRPC$TL_messages_getStickers.emoticon = str2;
        tLRPC$TL_messages_getStickers.hash = 0L;
        this.lastReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getStickers, new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda7
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                MentionsAdapter.this.lambda$searchServerStickers$1(str, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchServerStickers$1(final String str, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                MentionsAdapter.this.lambda$searchServerStickers$0(str, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchServerStickers$0(String str, TLObject tLObject) {
        ArrayList<StickerResult> arrayList;
        this.lastReqId = 0;
        if (str.equals(this.lastSticker) && (tLObject instanceof TLRPC$TL_messages_stickers)) {
            TLRPC$TL_messages_stickers tLRPC$TL_messages_stickers = (TLRPC$TL_messages_stickers) tLObject;
            ArrayList<StickerResult> arrayList2 = this.stickers;
            int size = arrayList2 != null ? arrayList2.size() : 0;
            ArrayList<TLRPC$Document> arrayList3 = tLRPC$TL_messages_stickers.stickers;
            addStickersToResult(arrayList3, "sticker_search_" + str);
            ArrayList<StickerResult> arrayList4 = this.stickers;
            int size2 = arrayList4 != null ? arrayList4.size() : 0;
            if (!this.visibleByStickersSearch && (arrayList = this.stickers) != null && !arrayList.isEmpty()) {
                checkStickerFilesExistAndDownload();
                this.delegate.needChangePanelVisibility(getItemCountInternal() > 0);
                this.visibleByStickersSearch = true;
            }
            if (size != size2) {
                notifyDataSetChanged();
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void notifyDataSetChanged() {
        MentionsAdapterDelegate mentionsAdapterDelegate;
        int i = this.lastItemCount;
        if (i == -1 || this.lastData == null) {
            MentionsAdapterDelegate mentionsAdapterDelegate2 = this.delegate;
            if (mentionsAdapterDelegate2 != null) {
                mentionsAdapterDelegate2.onItemCountUpdate(0, getItemCount());
            }
            super.notifyDataSetChanged();
            this.lastData = new Object[getItemCount()];
            while (true) {
                Object[] objArr = this.lastData;
                if (r1 >= objArr.length) {
                    return;
                }
                objArr[r1] = getItem(r1);
                r1++;
            }
        } else {
            int itemCount = getItemCount();
            boolean z = i != itemCount;
            int min = Math.min(i, itemCount);
            Object[] objArr2 = new Object[itemCount];
            for (int i2 = 0; i2 < itemCount; i2++) {
                objArr2[i2] = getItem(i2);
            }
            while (r1 < min) {
                if (r1 >= 0) {
                    Object[] objArr3 = this.lastData;
                    r1 = (r1 < objArr3.length && r1 < itemCount && itemsEqual(objArr3[r1], objArr2[r1])) ? r1 + 1 : 0;
                }
                notifyItemChanged(r1);
                z = true;
            }
            notifyItemRangeRemoved(min, i - min);
            notifyItemRangeInserted(min, itemCount - min);
            if (z && (mentionsAdapterDelegate = this.delegate) != null) {
                mentionsAdapterDelegate.onItemCountUpdate(i, itemCount);
            }
            this.lastData = objArr2;
        }
    }

    private boolean itemsEqual(Object obj, Object obj2) {
        MediaDataController.KeywordResult keywordResult;
        String str;
        String str2;
        if (obj == obj2) {
            return true;
        }
        if ((obj instanceof StickerResult) && (obj2 instanceof StickerResult) && ((StickerResult) obj).sticker == ((StickerResult) obj2).sticker) {
            return true;
        }
        if ((obj instanceof TLRPC$User) && (obj2 instanceof TLRPC$User) && ((TLRPC$User) obj).id == ((TLRPC$User) obj2).id) {
            return true;
        }
        if ((obj instanceof TLRPC$Chat) && (obj2 instanceof TLRPC$Chat) && ((TLRPC$Chat) obj).id == ((TLRPC$Chat) obj2).id) {
            return true;
        }
        if ((obj instanceof String) && (obj2 instanceof String) && obj.equals(obj2)) {
            return true;
        }
        if ((obj instanceof MediaDataController.KeywordResult) && (obj2 instanceof MediaDataController.KeywordResult) && (str = (keywordResult = (MediaDataController.KeywordResult) obj).keyword) != null) {
            MediaDataController.KeywordResult keywordResult2 = (MediaDataController.KeywordResult) obj2;
            return str.equals(keywordResult2.keyword) && (str2 = keywordResult.emoji) != null && str2.equals(keywordResult2.emoji);
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
        if (this.isDarkTheme) {
            return;
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoadFailed);
    }

    public void setParentFragment(ChatActivity chatActivity) {
        this.parentFragment = chatActivity;
    }

    public void setChatInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        ChatActivity chatActivity;
        TLRPC$Chat currentChat;
        this.currentAccount = UserConfig.selectedAccount;
        this.info = tLRPC$ChatFull;
        if (!this.inlineMediaEnabled && this.foundContextBot != null && (chatActivity = this.parentFragment) != null && (currentChat = chatActivity.getCurrentChat()) != null) {
            boolean canSendStickers = ChatObject.canSendStickers(currentChat);
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

    public void setNeedUsernames(boolean z) {
        this.needUsernames = z;
    }

    public void setNeedBotContext(boolean z) {
        this.needBotContext = z;
    }

    public void setBotInfo(LongSparseArray<TLRPC$BotInfo> longSparseArray) {
        this.botInfo = longSparseArray;
    }

    public void setBotsCount(int i) {
        this.botsCount = i;
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

    public TLRPC$TL_inlineBotSwitchPM getBotContextSwitch() {
        return this.searchResultBotContextSwitch;
    }

    public TLRPC$TL_inlineBotWebView getBotWebViewSwitch() {
        return this.searchResultBotWebViewSwitch;
    }

    public long getContextBotId() {
        TLRPC$User tLRPC$User = this.foundContextBot;
        if (tLRPC$User != null) {
            return tLRPC$User.id;
        }
        return 0L;
    }

    public TLRPC$User getContextBotUser() {
        return this.foundContextBot;
    }

    public String getContextBotName() {
        TLRPC$User tLRPC$User = this.foundContextBot;
        return tLRPC$User != null ? tLRPC$User.username : "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processFoundUser(TLRPC$User tLRPC$User) {
        ChatActivity chatActivity;
        TLRPC$Chat currentChat;
        this.contextUsernameReqid = 0;
        this.locationProvider.stop();
        if (tLRPC$User != null && tLRPC$User.bot && tLRPC$User.bot_inline_placeholder != null) {
            this.foundContextBot = tLRPC$User;
            ChatActivity chatActivity2 = this.parentFragment;
            if (chatActivity2 != null && (currentChat = chatActivity2.getCurrentChat()) != null) {
                boolean canSendStickers = ChatObject.canSendStickers(currentChat);
                this.inlineMediaEnabled = canSendStickers;
                if (!canSendStickers) {
                    notifyDataSetChanged();
                    this.delegate.needChangePanelVisibility(true);
                    return;
                }
            }
            if (this.foundContextBot.bot_inline_geo) {
                SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                if (!notificationsSettings.getBoolean("inlinegeo_" + this.foundContextBot.id, false) && (chatActivity = this.parentFragment) != null && chatActivity.getParentActivity() != null) {
                    final TLRPC$User tLRPC$User2 = this.foundContextBot;
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.parentFragment.getParentActivity());
                    builder.setTitle(LocaleController.getString("ShareYouLocationTitle", R.string.ShareYouLocationTitle));
                    builder.setMessage(LocaleController.getString("ShareYouLocationInline", R.string.ShareYouLocationInline));
                    final boolean[] zArr = new boolean[1];
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda1
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            MentionsAdapter.this.lambda$processFoundUser$2(zArr, tLRPC$User2, dialogInterface, i);
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            MentionsAdapter.this.lambda$processFoundUser$3(zArr, dialogInterface, i);
                        }
                    });
                    this.parentFragment.showDialog(builder.create(), new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda2
                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            MentionsAdapter.this.lambda$processFoundUser$4(zArr, dialogInterface);
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processFoundUser$2(boolean[] zArr, TLRPC$User tLRPC$User, DialogInterface dialogInterface, int i) {
        zArr[0] = true;
        if (tLRPC$User != null) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            edit.putBoolean("inlinegeo_" + tLRPC$User.id, true).commit();
            checkLocationPermissionsOrStart();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processFoundUser$3(boolean[] zArr, DialogInterface dialogInterface, int i) {
        zArr[0] = true;
        onLocationUnavailable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processFoundUser$4(boolean[] zArr, DialogInterface dialogInterface) {
        if (zArr[0]) {
            return;
        }
        onLocationUnavailable();
    }

    private void searchForContextBot(String str, String str2) {
        String str3;
        String str4;
        String str5;
        TLRPC$User tLRPC$User = this.foundContextBot;
        if (tLRPC$User == null || (str4 = tLRPC$User.username) == null || !str4.equals(str) || (str5 = this.searchingContextQuery) == null || !str5.equals(str2)) {
            if (this.foundContextBot != null) {
                if (!this.inlineMediaEnabled && str != null && str2 != null) {
                    return;
                }
                this.delegate.needChangePanelVisibility(false);
            }
            Runnable runnable = this.contextQueryRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.contextQueryRunnable = null;
            }
            if (TextUtils.isEmpty(str) || ((str3 = this.searchingContextUsername) != null && !str3.equals(str))) {
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
                if (str == null || str.length() == 0) {
                    return;
                }
            }
            if (str2 == null) {
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
                } else if (str.equals("gif")) {
                    this.searchingContextUsername = "gif";
                    this.delegate.onContextSearch(false);
                }
            }
            MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
            MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
            this.searchingContextQuery = str2;
            4 r0 = new 4(str2, str, messagesController, messagesStorage);
            this.contextQueryRunnable = r0;
            AndroidUtilities.runOnUIThread(r0, 400L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 4 implements Runnable {
        final /* synthetic */ MessagesController val$messagesController;
        final /* synthetic */ MessagesStorage val$messagesStorage;
        final /* synthetic */ String val$query;
        final /* synthetic */ String val$username;

        4(String str, String str2, MessagesController messagesController, MessagesStorage messagesStorage) {
            this.val$query = str;
            this.val$username = str2;
            this.val$messagesController = messagesController;
            this.val$messagesStorage = messagesStorage;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (MentionsAdapter.this.contextQueryRunnable != this) {
                return;
            }
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
            TLObject userOrChat = this.val$messagesController.getUserOrChat(MentionsAdapter.this.searchingContextUsername);
            if (userOrChat instanceof TLRPC$User) {
                MentionsAdapter.this.processFoundUser((TLRPC$User) userOrChat);
                return;
            }
            TLRPC$TL_contacts_resolveUsername tLRPC$TL_contacts_resolveUsername = new TLRPC$TL_contacts_resolveUsername();
            tLRPC$TL_contacts_resolveUsername.username = MentionsAdapter.this.searchingContextUsername;
            MentionsAdapter mentionsAdapter2 = MentionsAdapter.this;
            ConnectionsManager connectionsManager = ConnectionsManager.getInstance(mentionsAdapter2.currentAccount);
            final String str = this.val$username;
            final MessagesController messagesController = this.val$messagesController;
            final MessagesStorage messagesStorage = this.val$messagesStorage;
            mentionsAdapter2.contextUsernameReqid = connectionsManager.sendRequest(tLRPC$TL_contacts_resolveUsername, new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$4$$ExternalSyntheticLambda1
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    MentionsAdapter.4.this.lambda$run$1(str, messagesController, messagesStorage, tLObject, tLRPC$TL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(final String str, final MessagesController messagesController, final MessagesStorage messagesStorage, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MentionsAdapter.4.this.lambda$run$0(str, tLRPC$TL_error, tLObject, messagesController, messagesStorage);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(String str, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, MessagesController messagesController, MessagesStorage messagesStorage) {
            if (MentionsAdapter.this.searchingContextUsername == null || !MentionsAdapter.this.searchingContextUsername.equals(str)) {
                return;
            }
            TLRPC$User tLRPC$User = null;
            if (tLRPC$TL_error == null) {
                TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer = (TLRPC$TL_contacts_resolvedPeer) tLObject;
                if (!tLRPC$TL_contacts_resolvedPeer.users.isEmpty()) {
                    TLRPC$User tLRPC$User2 = tLRPC$TL_contacts_resolvedPeer.users.get(0);
                    messagesController.putUser(tLRPC$User2, false);
                    messagesStorage.putUsersAndChats(tLRPC$TL_contacts_resolvedPeer.users, null, true, true);
                    tLRPC$User = tLRPC$User2;
                }
            }
            MentionsAdapter.this.processFoundUser(tLRPC$User);
            MentionsAdapter.this.contextUsernameReqid = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationUnavailable() {
        TLRPC$User tLRPC$User = this.foundContextBot;
        if (tLRPC$User == null || !tLRPC$User.bot_inline_geo) {
            return;
        }
        Location location = new Location("network");
        this.lastKnownLocation = location;
        location.setLatitude(-1000.0d);
        this.lastKnownLocation.setLongitude(-1000.0d);
        searchForContextBotResults(true, this.foundContextBot, this.searchingContextQuery, "");
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
        TLRPC$User tLRPC$User = this.foundContextBot;
        if (tLRPC$User == null || !tLRPC$User.bot_inline_geo) {
            return;
        }
        this.locationProvider.start();
    }

    public void setSearchingMentions(boolean z) {
        this.isSearchingMentions = z;
    }

    public String getBotCaption() {
        TLRPC$User tLRPC$User = this.foundContextBot;
        if (tLRPC$User != null) {
            return tLRPC$User.bot_inline_placeholder;
        }
        String str = this.searchingContextUsername;
        if (str == null || !str.equals("gif")) {
            return null;
        }
        return LocaleController.getString("SearchGifsTitle", R.string.SearchGifsTitle);
    }

    public void searchForContextBotForNextOffset() {
        String str;
        TLRPC$User tLRPC$User;
        String str2;
        if (this.contextQueryReqid != 0 || (str = this.nextQueryOffset) == null || str.length() == 0 || (tLRPC$User = this.foundContextBot) == null || (str2 = this.searchingContextQuery) == null) {
            return;
        }
        searchForContextBotResults(true, tLRPC$User, str2, this.nextQueryOffset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void searchForContextBotResults(final boolean z, final TLRPC$User tLRPC$User, final String str, final String str2) {
        Location location;
        if (this.contextQueryReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
            this.contextQueryReqid = 0;
        }
        if (!this.inlineMediaEnabled || !this.allowBots) {
            MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
            if (mentionsAdapterDelegate != null) {
                mentionsAdapterDelegate.onContextSearch(false);
            }
        } else if (str == null || tLRPC$User == null) {
            this.searchingContextQuery = null;
        } else if (tLRPC$User.bot_inline_geo && this.lastKnownLocation == null) {
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(this.dialog_id);
            sb.append("_");
            sb.append(str);
            sb.append("_");
            sb.append(str2);
            sb.append("_");
            sb.append(this.dialog_id);
            sb.append("_");
            sb.append(tLRPC$User.id);
            sb.append("_");
            sb.append((!tLRPC$User.bot_inline_geo || this.lastKnownLocation.getLatitude() == -1000.0d) ? "" : Double.valueOf(this.lastKnownLocation.getLatitude() + this.lastKnownLocation.getLongitude()));
            final String sb2 = sb.toString();
            final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
            RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda8
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    MentionsAdapter.this.lambda$searchForContextBotResults$6(str, z, tLRPC$User, str2, messagesStorage, sb2, tLObject, tLRPC$TL_error);
                }
            };
            if (z) {
                messagesStorage.getBotCache(sb2, requestDelegate);
                return;
            }
            TLRPC$TL_messages_getInlineBotResults tLRPC$TL_messages_getInlineBotResults = new TLRPC$TL_messages_getInlineBotResults();
            tLRPC$TL_messages_getInlineBotResults.bot = MessagesController.getInstance(this.currentAccount).getInputUser(tLRPC$User);
            tLRPC$TL_messages_getInlineBotResults.query = str;
            tLRPC$TL_messages_getInlineBotResults.offset = str2;
            if (tLRPC$User.bot_inline_geo && (location = this.lastKnownLocation) != null && location.getLatitude() != -1000.0d) {
                tLRPC$TL_messages_getInlineBotResults.flags |= 1;
                TLRPC$TL_inputGeoPoint tLRPC$TL_inputGeoPoint = new TLRPC$TL_inputGeoPoint();
                tLRPC$TL_messages_getInlineBotResults.geo_point = tLRPC$TL_inputGeoPoint;
                tLRPC$TL_inputGeoPoint.lat = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLatitude());
                tLRPC$TL_messages_getInlineBotResults.geo_point._long = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLongitude());
            }
            if (DialogObject.isEncryptedDialog(this.dialog_id)) {
                tLRPC$TL_messages_getInlineBotResults.peer = new TLRPC$TL_inputPeerEmpty();
            } else {
                tLRPC$TL_messages_getInlineBotResults.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.dialog_id);
            }
            this.contextQueryReqid = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getInlineBotResults, requestDelegate, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchForContextBotResults$6(final String str, final boolean z, final TLRPC$User tLRPC$User, final String str2, final MessagesStorage messagesStorage, final String str3, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                MentionsAdapter.this.lambda$searchForContextBotResults$5(str, z, tLObject, tLRPC$User, str2, messagesStorage, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchForContextBotResults$5(String str, boolean z, TLObject tLObject, TLRPC$User tLRPC$User, String str2, MessagesStorage messagesStorage, String str3) {
        boolean z2;
        if (str.equals(this.searchingContextQuery)) {
            int i = 0;
            this.contextQueryReqid = 0;
            if (z && tLObject == null) {
                searchForContextBotResults(false, tLRPC$User, str, str2);
            } else {
                MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
                if (mentionsAdapterDelegate != null) {
                    mentionsAdapterDelegate.onContextSearch(false);
                }
            }
            if (tLObject instanceof TLRPC$TL_messages_botResults) {
                TLRPC$TL_messages_botResults tLRPC$TL_messages_botResults = (TLRPC$TL_messages_botResults) tLObject;
                if (!z && tLRPC$TL_messages_botResults.cache_time != 0) {
                    messagesStorage.saveBotCache(str3, tLRPC$TL_messages_botResults);
                }
                this.nextQueryOffset = tLRPC$TL_messages_botResults.next_offset;
                if (this.searchResultBotContextSwitch == null) {
                    this.searchResultBotContextSwitch = tLRPC$TL_messages_botResults.switch_pm;
                }
                this.searchResultBotWebViewSwitch = tLRPC$TL_messages_botResults.switch_webview;
                int i2 = 0;
                while (i2 < tLRPC$TL_messages_botResults.results.size()) {
                    TLRPC$BotInlineResult tLRPC$BotInlineResult = tLRPC$TL_messages_botResults.results.get(i2);
                    if (!(tLRPC$BotInlineResult.document instanceof TLRPC$TL_document) && !(tLRPC$BotInlineResult.photo instanceof TLRPC$TL_photo) && !"game".equals(tLRPC$BotInlineResult.type) && tLRPC$BotInlineResult.content == null && (tLRPC$BotInlineResult.send_message instanceof TLRPC$TL_botInlineMessageMediaAuto)) {
                        tLRPC$TL_messages_botResults.results.remove(i2);
                        i2--;
                    }
                    tLRPC$BotInlineResult.query_id = tLRPC$TL_messages_botResults.query_id;
                    i2++;
                }
                if (this.searchResultBotContext == null || str2.length() == 0) {
                    this.searchResultBotContext = tLRPC$TL_messages_botResults.results;
                    this.contextMedia = tLRPC$TL_messages_botResults.gallery;
                    z2 = false;
                } else {
                    this.searchResultBotContext.addAll(tLRPC$TL_messages_botResults.results);
                    if (tLRPC$TL_messages_botResults.results.isEmpty()) {
                        this.nextQueryOffset = "";
                    }
                    z2 = true;
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
                this.delegate.needChangePanelVisibility((this.searchResultBotContext.isEmpty() && this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) ? false : true);
                if (z2) {
                    i = (this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) ? 1 : 1;
                    notifyItemChanged(((this.searchResultBotContext.size() - tLRPC$TL_messages_botResults.results.size()) + i) - 1);
                    notifyItemRangeInserted((this.searchResultBotContext.size() - tLRPC$TL_messages_botResults.results.size()) + i, tLRPC$TL_messages_botResults.results.size());
                    return;
                }
                notifyDataSetChanged();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:219:0x0381, code lost:
        if (r26.info != null) goto L443;
     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x0383, code lost:
        if (r5 == 0) goto L443;
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x0385, code lost:
        r26.lastText = r6;
        r26.lastPosition = r28;
        r26.messages = r29;
        r26.delegate.needChangePanelVisibility(false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x0391, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x05fa, code lost:
        if (r19.toLowerCase().startsWith(r6) == false) goto L215;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x060d, code lost:
        if (r15.toLowerCase().startsWith(r6) != false) goto L212;
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x061d, code lost:
        if (r18.toLowerCase().startsWith(r6) != false) goto L212;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:255:0x0416  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x0421  */
    /* JADX WARN: Removed duplicated region for block: B:354:0x05f0  */
    /* JADX WARN: Removed duplicated region for block: B:357:0x05fd  */
    /* JADX WARN: Removed duplicated region for block: B:360:0x0605  */
    /* JADX WARN: Removed duplicated region for block: B:364:0x0615  */
    /* JADX WARN: Removed duplicated region for block: B:367:0x0621  */
    /* JADX WARN: Removed duplicated region for block: B:510:0x063c A[SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r12v21 */
    /* JADX WARN: Type inference failed for: r12v22 */
    /* JADX WARN: Type inference failed for: r26v0, types: [org.telegram.ui.Adapters.MentionsAdapter] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void searchUsernameOrHashtag(CharSequence charSequence, int i, ArrayList<MessageObject> arrayList, boolean z, boolean z2) {
        String str;
        int i2;
        char c;
        int i3;
        String str2;
        String str3;
        int i4;
        boolean z3;
        char c2;
        int i5;
        int i6;
        String str4;
        String str5;
        ArrayList arrayList2;
        boolean z4;
        int i7;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Chat chat;
        TLRPC$ChatFull tLRPC$ChatFull;
        TLRPC$User user;
        LongSparseArray longSparseArray;
        String str6;
        TLRPC$Chat tLRPC$Chat2;
        long j;
        String str7;
        String str8;
        TLRPC$User tLRPC$User;
        int i8;
        int i9;
        char c3;
        int i10;
        CharSequence concat;
        boolean z5 = z;
        String str9 = "";
        String charSequence2 = charSequence == null ? "" : charSequence.toString();
        TLRPC$Chat tLRPC$Chat3 = this.chat;
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null) {
            tLRPC$Chat3 = chatActivity.getCurrentChat();
            this.parentFragment.getCurrentUser();
        }
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
        if (TextUtils.isEmpty(charSequence2) || charSequence2.length() > MessagesController.getInstance(this.currentAccount).maxMessageLength) {
            searchForContextBot(null, null);
            this.delegate.needChangePanelVisibility(false);
            this.lastText = null;
            clearStickers();
            return;
        }
        int i11 = charSequence2.length() > 0 ? i - 1 : i;
        this.lastText = null;
        this.lastUsernameOnly = z5;
        this.lastForSearch = z2;
        StringBuilder sb = new StringBuilder();
        boolean z6 = !z5 && charSequence2.length() > 0 && charSequence2.length() <= 14;
        if (z6) {
            String str10 = charSequence2;
            int length = charSequence2.length();
            int i12 = 0;
            while (i12 < length) {
                char charAt = str10.charAt(i12);
                String str11 = str9;
                int i13 = length - 1;
                if (i12 < i13) {
                    i9 = i11;
                    c3 = str10.charAt(i12 + 1);
                } else {
                    i9 = i11;
                    c3 = 0;
                }
                if (i12 >= i13 || charAt != 55356 || c3 < 57339 || c3 > 57343) {
                    concat = str10;
                    if (charAt == 65039) {
                        i10 = 1;
                        concat = TextUtils.concat(str10.subSequence(0, i12), str10.subSequence(i12 + 1, str10.length()));
                        length--;
                        i12--;
                        i12 += i10;
                        str9 = str11;
                        i11 = i9;
                        str10 = concat;
                    }
                } else {
                    CharSequence[] charSequenceArr = {str10.subSequence(0, i12), str10.subSequence(i12 + 2, str10.length())};
                    length -= 2;
                    i12--;
                    concat = TextUtils.concat(charSequenceArr);
                }
                i10 = 1;
                i12 += i10;
                str9 = str11;
                i11 = i9;
                str10 = concat;
            }
            str = str9;
            i2 = i11;
            this.lastSticker = str10.toString().trim();
            str9 = charSequence2;
        } else {
            str = "";
            i2 = i11;
        }
        boolean z7 = z6 && (Emoji.isValidEmoji(str9) || Emoji.isValidEmoji(this.lastSticker));
        if (z7 && (charSequence instanceof Spanned)) {
            AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), AnimatedEmojiSpan.class);
            z7 = animatedEmojiSpanArr == null || animatedEmojiSpanArr.length == 0;
        }
        if (this.allowStickers && z7 && (tLRPC$Chat3 == null || ChatObject.canSendStickers(tLRPC$Chat3))) {
            this.stickersToLoad.clear();
            int i14 = SharedConfig.suggestStickers;
            if (i14 == 2 || !z7) {
                if (this.visibleByStickersSearch && i14 == 2) {
                    this.visibleByStickersSearch = false;
                    this.delegate.needChangePanelVisibility(false);
                    notifyDataSetChanged();
                    return;
                }
                return;
            }
            this.stickers = null;
            this.stickersMap = null;
            if (this.lastReqId != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.lastReqId, true);
                i8 = 0;
                this.lastReqId = 0;
            } else {
                i8 = 0;
            }
            boolean z8 = MessagesController.getInstance(this.currentAccount).suggestStickersApiOnly;
            if (!z8) {
                final ArrayList<TLRPC$Document> recentStickersNoCopy = MediaDataController.getInstance(this.currentAccount).getRecentStickersNoCopy(i8);
                final ArrayList<TLRPC$Document> recentStickersNoCopy2 = MediaDataController.getInstance(this.currentAccount).getRecentStickersNoCopy(2);
                int min = Math.min(20, recentStickersNoCopy.size());
                int i15 = 0;
                for (int i16 = 0; i16 < min; i16++) {
                    TLRPC$Document tLRPC$Document = recentStickersNoCopy.get(i16);
                    if (isValidSticker(tLRPC$Document, this.lastSticker)) {
                        addStickerToResult(tLRPC$Document, "recent");
                        int i17 = i15 + 1;
                        if (i17 >= 5) {
                            break;
                        }
                        i15 = i17;
                    }
                }
                int size = recentStickersNoCopy2.size();
                for (int i18 = 0; i18 < size; i18++) {
                    TLRPC$Document tLRPC$Document2 = recentStickersNoCopy2.get(i18);
                    if (isValidSticker(tLRPC$Document2, this.lastSticker)) {
                        addStickerToResult(tLRPC$Document2, "fav");
                    }
                }
                HashMap<String, ArrayList<TLRPC$Document>> allStickers = MediaDataController.getInstance(this.currentAccount).getAllStickers();
                ArrayList<TLRPC$Document> arrayList3 = allStickers != null ? allStickers.get(this.lastSticker) : null;
                if (arrayList3 != null && !arrayList3.isEmpty()) {
                    addStickersToResult(arrayList3, null);
                }
                ArrayList<StickerResult> arrayList4 = this.stickers;
                if (arrayList4 != null) {
                    Collections.sort(arrayList4, new Comparator<StickerResult>(this) { // from class: org.telegram.ui.Adapters.MentionsAdapter.5
                        private int getIndex(StickerResult stickerResult) {
                            for (int i19 = 0; i19 < recentStickersNoCopy2.size(); i19++) {
                                if (((TLRPC$Document) recentStickersNoCopy2.get(i19)).id == stickerResult.sticker.id) {
                                    return i19 + 2000000;
                                }
                            }
                            for (int i20 = 0; i20 < Math.min(20, recentStickersNoCopy.size()); i20++) {
                                if (((TLRPC$Document) recentStickersNoCopy.get(i20)).id == stickerResult.sticker.id) {
                                    return (recentStickersNoCopy.size() - i20) + MediaController.VIDEO_BITRATE_480;
                                }
                            }
                            return -1;
                        }

                        @Override // java.util.Comparator
                        public int compare(StickerResult stickerResult, StickerResult stickerResult2) {
                            boolean isAnimatedStickerDocument = MessageObject.isAnimatedStickerDocument(stickerResult.sticker, true);
                            if (isAnimatedStickerDocument != MessageObject.isAnimatedStickerDocument(stickerResult2.sticker, true)) {
                                return isAnimatedStickerDocument ? -1 : 1;
                            }
                            int index = getIndex(stickerResult);
                            int index2 = getIndex(stickerResult2);
                            if (index > index2) {
                                return -1;
                            }
                            return index < index2 ? 1 : 0;
                        }
                    });
                }
            }
            if (SharedConfig.suggestStickers == 0 || z8) {
                searchServerStickers(this.lastSticker, str9);
            }
            ArrayList<StickerResult> arrayList5 = this.stickers;
            if (arrayList5 != null && !arrayList5.isEmpty()) {
                if (SharedConfig.suggestStickers == 0 && this.stickers.size() < 5) {
                    this.delegate.needChangePanelVisibility(false);
                    this.visibleByStickersSearch = false;
                } else {
                    checkStickerFilesExistAndDownload();
                    this.delegate.needChangePanelVisibility(this.stickersToLoad.isEmpty());
                    this.visibleByStickersSearch = true;
                }
                notifyDataSetChanged();
            } else if (this.visibleByStickersSearch) {
                this.delegate.needChangePanelVisibility(false);
                this.visibleByStickersSearch = false;
                c = 4;
            }
            c = 4;
        } else {
            if (!z5 && this.needBotContext && charSequence2.charAt(0) == '@') {
                int indexOf = charSequence2.indexOf(32);
                int length2 = charSequence2.length();
                if (indexOf > 0) {
                    String substring = charSequence2.substring(1, indexOf);
                    str3 = charSequence2.substring(indexOf + 1);
                    str2 = substring;
                    i3 = 1;
                } else if (charSequence2.charAt(length2 - 1) == 't' && charSequence2.charAt(length2 - 2) == 'o' && charSequence2.charAt(length2 - 3) == 'b') {
                    i3 = 1;
                    str2 = charSequence2.substring(1);
                    str3 = str;
                } else {
                    i3 = 1;
                    searchForContextBot(null, null);
                    str2 = null;
                    str3 = null;
                }
                if (str2 != null && str2.length() >= i3) {
                    for (int i19 = 1; i19 < str2.length(); i19++) {
                        char charAt2 = str2.charAt(i19);
                        if ((charAt2 >= '0' && charAt2 <= '9') || ((charAt2 >= 'a' && charAt2 <= 'z') || ((charAt2 >= 'A' && charAt2 <= 'Z') || charAt2 == '_'))) {
                        }
                    }
                    searchForContextBot(str2, str3);
                }
                str2 = str;
                searchForContextBot(str2, str3);
            } else {
                searchForContextBot(null, null);
            }
            c = 65535;
        }
        if (this.foundContextBot != null) {
            return;
        }
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        if (!z5) {
            i4 = i2;
            while (i4 >= 0) {
                if (i4 < charSequence2.length()) {
                    char charAt3 = charSequence2.charAt(i4);
                    if (i4 != 0) {
                        int i20 = i4 - 1;
                        if (charSequence2.charAt(i20) != ' ' && charSequence2.charAt(i20) != '\n' && charAt3 != ':') {
                            i6 = 0;
                            sb.insert(i6, charAt3);
                        }
                    }
                    if (charAt3 == '@') {
                        boolean z9 = this.searchInDailogs;
                        if (z9 || this.needUsernames || (this.needBotContext && i4 == 0)) {
                            this.resultStartPosition = i4;
                            this.resultLength = sb.length() + 1;
                            c = 0;
                            c2 = 65535;
                            z3 = false;
                            break;
                        }
                    } else if (charAt3 == '#') {
                        if (this.searchAdapterHelper.loadRecentHashtags()) {
                            this.resultStartPosition = i4;
                            this.resultLength = sb.length() + 1;
                            sb.insert(0, charAt3);
                            c = 1;
                        } else {
                            this.lastText = charSequence2;
                            this.lastPosition = i;
                            this.messages = arrayList;
                            return;
                        }
                    } else if (i4 == 0 && this.botInfo != null && charAt3 == '/') {
                        this.resultStartPosition = i4;
                        this.resultLength = sb.length() + 1;
                        c = 2;
                    } else if (charAt3 == ':' && sb.length() > 0) {
                        if (" !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\n".indexOf(sb.charAt(0)) >= 0) {
                            i5 = 1;
                            if (sb.length() > 1) {
                            }
                        } else {
                            i5 = 1;
                        }
                        this.resultStartPosition = i4;
                        this.resultLength = sb.length() + i5;
                        c = 3;
                    }
                    i6 = 0;
                    sb.insert(i6, charAt3);
                }
                i4--;
            }
            z3 = false;
            c2 = 65535;
            i4 = -1;
            if (c != c2) {
                this.contextMedia = z3;
                this.searchResultBotContext = null;
                this.delegate.needChangePanelVisibility(z3);
                return;
            } else if (c != 0) {
                if (c == 1) {
                    ArrayList<String> arrayList6 = new ArrayList<>();
                    String lowerCase = sb.toString().toLowerCase();
                    ArrayList<SearchAdapterHelper.HashtagObject> hashtags = this.searchAdapterHelper.getHashtags();
                    for (int i21 = 0; i21 < hashtags.size(); i21++) {
                        SearchAdapterHelper.HashtagObject hashtagObject = hashtags.get(i21);
                        if (hashtagObject != null && (str5 = hashtagObject.hashtag) != null && str5.startsWith(lowerCase)) {
                            arrayList6.add(hashtagObject.hashtag);
                        }
                    }
                    this.searchResultHashtags = arrayList6;
                    this.stickers = null;
                    this.searchResultUsernames = null;
                    this.searchResultUsernamesMap = null;
                    this.searchResultCommands = null;
                    this.searchResultCommandsHelp = null;
                    this.searchResultCommandsUsers = null;
                    this.searchResultSuggestions = null;
                    this.contextMedia = false;
                    this.searchResultBotContext = null;
                    notifyDataSetChanged();
                    this.delegate.needChangePanelVisibility(!this.searchResultHashtags.isEmpty());
                    return;
                } else if (c != 2) {
                    if (c == 3) {
                        String[] currentKeyboardLanguage = AndroidUtilities.getCurrentKeyboardLanguage();
                        if (!Arrays.equals(currentKeyboardLanguage, this.lastSearchKeyboardLanguage)) {
                            MediaDataController.getInstance(this.currentAccount).fetchNewEmojiKeywords(currentKeyboardLanguage);
                        }
                        this.lastSearchKeyboardLanguage = currentKeyboardLanguage;
                        MediaDataController.getInstance(this.currentAccount).getEmojiSuggestions(this.lastSearchKeyboardLanguage, sb.toString(), false, new MediaDataController.KeywordResultCallback() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda6
                            @Override // org.telegram.messenger.MediaDataController.KeywordResultCallback
                            public final void run(ArrayList arrayList7, String str12) {
                                MentionsAdapter.this.lambda$searchUsernameOrHashtag$8(arrayList7, str12);
                            }
                        }, true);
                        return;
                    } else if (c == 4) {
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
                    ArrayList<String> arrayList7 = new ArrayList<>();
                    ArrayList<String> arrayList8 = new ArrayList<>();
                    ArrayList<TLRPC$User> arrayList9 = new ArrayList<>();
                    String lowerCase2 = sb.toString().toLowerCase();
                    for (int i22 = 0; i22 < this.botInfo.size(); i22++) {
                        TLRPC$BotInfo valueAt = this.botInfo.valueAt(i22);
                        for (int i23 = 0; i23 < valueAt.commands.size(); i23++) {
                            TLRPC$TL_botCommand tLRPC$TL_botCommand = valueAt.commands.get(i23);
                            if (tLRPC$TL_botCommand != null && (str4 = tLRPC$TL_botCommand.command) != null && str4.startsWith(lowerCase2)) {
                                arrayList7.add("/" + tLRPC$TL_botCommand.command);
                                arrayList8.add(tLRPC$TL_botCommand.description);
                                arrayList9.add(messagesController.getUser(Long.valueOf(valueAt.user_id)));
                            }
                        }
                    }
                    this.searchResultHashtags = null;
                    this.stickers = null;
                    this.searchResultUsernames = null;
                    this.searchResultUsernamesMap = null;
                    this.searchResultSuggestions = null;
                    this.searchResultCommands = arrayList7;
                    this.searchResultCommandsHelp = arrayList8;
                    this.searchResultCommandsUsers = arrayList9;
                    this.contextMedia = false;
                    this.searchResultBotContext = null;
                    notifyDataSetChanged();
                    this.delegate.needChangePanelVisibility(!arrayList7.isEmpty());
                    return;
                }
            } else {
                this.contextMedia = z3;
                this.searchResultBotContext = null;
                ArrayList arrayList10 = new ArrayList();
                if (arrayList != null) {
                    for (int i24 = 0; i24 < Math.min(100, arrayList.size()); i24++) {
                        long fromChatId = arrayList.get(i24).getFromChatId();
                        if (fromChatId > 0 && !arrayList10.contains(Long.valueOf(fromChatId))) {
                            arrayList10.add(Long.valueOf(fromChatId));
                        }
                    }
                }
                String lowerCase3 = sb.toString().toLowerCase();
                boolean z10 = lowerCase3.indexOf(32) >= 0;
                final ArrayList arrayList11 = new ArrayList();
                LongSparseArray longSparseArray2 = new LongSparseArray();
                final LongSparseArray longSparseArray3 = new LongSparseArray();
                ArrayList<TLRPC$TL_topPeer> arrayList12 = MediaDataController.getInstance(this.currentAccount).inlineBots;
                if (!z5 && this.needBotContext && i4 == 0 && !arrayList12.isEmpty()) {
                    int i25 = 0;
                    int i26 = 0;
                    while (i25 < arrayList12.size()) {
                        TLRPC$User user2 = messagesController.getUser(Long.valueOf(arrayList12.get(i25).peer.user_id));
                        if (user2 != null) {
                            String publicUsername = UserObject.getPublicUsername(user2);
                            if (TextUtils.isEmpty(publicUsername) || !(lowerCase3.length() == 0 || publicUsername.toLowerCase().startsWith(lowerCase3))) {
                                arrayList2 = arrayList10;
                                z4 = z10;
                            } else {
                                arrayList11.add(user2);
                                arrayList2 = arrayList10;
                                z4 = z10;
                                longSparseArray2.put(user2.id, user2);
                                longSparseArray3.put(user2.id, user2);
                                i26++;
                            }
                            if (i26 == 5) {
                                break;
                            }
                        } else {
                            arrayList2 = arrayList10;
                            z4 = z10;
                        }
                        i25++;
                        z10 = z4;
                        arrayList10 = arrayList2;
                    }
                }
                arrayList2 = arrayList10;
                z4 = z10;
                ChatActivity chatActivity2 = this.parentFragment;
                if (chatActivity2 != null) {
                    tLRPC$Chat3 = chatActivity2.getCurrentChat();
                    i7 = this.parentFragment.getThreadId();
                } else {
                    TLRPC$ChatFull tLRPC$ChatFull2 = this.info;
                    if (tLRPC$ChatFull2 != null) {
                        tLRPC$Chat3 = messagesController.getChat(Long.valueOf(tLRPC$ChatFull2.id));
                    }
                    i7 = 0;
                }
                TLRPC$User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                if (tLRPC$Chat3 != null && (tLRPC$ChatFull = this.info) != null && tLRPC$ChatFull.participants != null && (!ChatObject.isChannel(tLRPC$Chat3) || tLRPC$Chat3.megagroup)) {
                    int i27 = -2;
                    int i28 = -2;
                    while (i28 < this.info.participants.participants.size()) {
                        if (i28 != i27) {
                            LongSparseArray longSparseArray4 = longSparseArray2;
                            if (i28 == -1) {
                                if (z2) {
                                    if (lowerCase3.length() == 0) {
                                        arrayList11.add(tLRPC$Chat3);
                                    } else {
                                        String str12 = tLRPC$Chat3.title;
                                        String publicUsername2 = ChatObject.getPublicUsername(tLRPC$Chat3);
                                        j = -tLRPC$Chat3.id;
                                        tLRPC$Chat2 = tLRPC$Chat3;
                                        str6 = str12;
                                        longSparseArray = longSparseArray4;
                                        str7 = publicUsername2;
                                        str8 = null;
                                        if (TextUtils.isEmpty(str7)) {
                                        }
                                        if (!TextUtils.isEmpty(str6)) {
                                        }
                                        if (!TextUtils.isEmpty(str8)) {
                                        }
                                        if (!z4) {
                                        }
                                    }
                                }
                                tLRPC$User = currentUser;
                                longSparseArray = longSparseArray4;
                            } else {
                                TLRPC$ChatParticipant tLRPC$ChatParticipant = this.info.participants.participants.get(i28);
                                if ((currentUser == null || tLRPC$ChatParticipant.user_id != currentUser.id) && (user = messagesController.getUser(Long.valueOf(tLRPC$ChatParticipant.user_id))) != null && !UserObject.isUserSelf(user)) {
                                    longSparseArray = longSparseArray4;
                                    if (longSparseArray.indexOfKey(user.id) < 0) {
                                        if (lowerCase3.length() == 0 && !user.deleted) {
                                            arrayList11.add(user);
                                        } else {
                                            String str13 = user.first_name;
                                            String str14 = user.last_name;
                                            String publicUsername3 = UserObject.getPublicUsername(user);
                                            str6 = str13;
                                            tLRPC$Chat2 = user;
                                            j = user.id;
                                            str7 = publicUsername3;
                                            str8 = str14;
                                            if (TextUtils.isEmpty(str7)) {
                                            }
                                            if (!TextUtils.isEmpty(str6)) {
                                            }
                                            if (!TextUtils.isEmpty(str8)) {
                                            }
                                            if (!z4) {
                                            }
                                        }
                                    }
                                    tLRPC$User = currentUser;
                                }
                                tLRPC$User = currentUser;
                                longSparseArray = longSparseArray4;
                            }
                            i28++;
                            z5 = z;
                            longSparseArray2 = longSparseArray;
                            currentUser = tLRPC$User;
                            i27 = -2;
                        } else if (currentUser == null || !z5) {
                            tLRPC$User = currentUser;
                            longSparseArray = longSparseArray2;
                            i28++;
                            z5 = z;
                            longSparseArray2 = longSparseArray;
                            currentUser = tLRPC$User;
                            i27 = -2;
                        } else {
                            String str15 = currentUser.first_name;
                            String str16 = currentUser.last_name;
                            String publicUsername4 = UserObject.getPublicUsername(currentUser);
                            j = currentUser.id;
                            tLRPC$Chat2 = currentUser;
                            str6 = str15;
                            longSparseArray = longSparseArray2;
                            str7 = publicUsername4;
                            str8 = str16;
                            if (TextUtils.isEmpty(str7)) {
                                tLRPC$User = currentUser;
                            } else {
                                tLRPC$User = currentUser;
                            }
                            if (!TextUtils.isEmpty(str6)) {
                            }
                            if (!TextUtils.isEmpty(str8)) {
                            }
                            if (!z4) {
                                if (ContactsController.formatName(str6, str8).toLowerCase().startsWith(lowerCase3)) {
                                }
                                arrayList11.add(tLRPC$Chat2);
                                longSparseArray3.put(j, tLRPC$Chat2);
                            }
                            i28++;
                            z5 = z;
                            longSparseArray2 = longSparseArray;
                            currentUser = tLRPC$User;
                            i27 = -2;
                        }
                    }
                }
                LongSparseArray longSparseArray5 = longSparseArray2;
                if (this.searchInDailogs) {
                    ArrayList<TLRPC$Dialog> allDialogs = MessagesController.getInstance(this.currentAccount).getAllDialogs();
                    int i29 = 0;
                    while (i29 < allDialogs.size()) {
                        if (allDialogs.get(i29).id > 0) {
                            TLRPC$User user3 = messagesController.getUser(Long.valueOf(allDialogs.get(i29).id));
                            if (user3 != null && !UserObject.isUserSelf(user3) && longSparseArray5.indexOfKey(user3.id) < 0) {
                                if (lowerCase3.length() == 0 && !user3.deleted) {
                                    arrayList11.add(user3);
                                } else {
                                    String str17 = user3.first_name;
                                    String str18 = user3.last_name;
                                    String publicUsername5 = UserObject.getPublicUsername(user3);
                                    tLRPC$Chat = tLRPC$Chat3;
                                    long j2 = user3.id;
                                    if ((!TextUtils.isEmpty(publicUsername5) && publicUsername5.toLowerCase().startsWith(lowerCase3)) || ((!TextUtils.isEmpty(str17) && str17.toLowerCase().startsWith(lowerCase3)) || ((!TextUtils.isEmpty(str18) && str18.toLowerCase().startsWith(lowerCase3)) || (z4 && ContactsController.formatName(str17, str18).toLowerCase().startsWith(lowerCase3))))) {
                                        arrayList11.add(user3);
                                        longSparseArray3.put(j2, user3);
                                    }
                                }
                            }
                            tLRPC$Chat = tLRPC$Chat3;
                        } else {
                            tLRPC$Chat = tLRPC$Chat3;
                            if (!TextUtils.isEmpty(lowerCase3) && (chat = messagesController.getChat(Long.valueOf(-allDialogs.get(i29).id))) != null && chat.username != null && longSparseArray5.indexOfKey(chat.id) < 0) {
                                if (lowerCase3.length() == 0) {
                                    arrayList11.add(chat);
                                } else {
                                    String str19 = chat.title;
                                    String str20 = chat.username;
                                    long j3 = chat.id;
                                    if ((!TextUtils.isEmpty(str20) && str20.toLowerCase().startsWith(lowerCase3)) || (!TextUtils.isEmpty(str19) && str19.toLowerCase().startsWith(lowerCase3))) {
                                        arrayList11.add(chat);
                                        longSparseArray3.put(j3, chat);
                                    }
                                }
                            }
                        }
                        i29++;
                        tLRPC$Chat3 = tLRPC$Chat;
                    }
                }
                TLRPC$Chat tLRPC$Chat4 = tLRPC$Chat3;
                final ArrayList arrayList13 = arrayList2;
                Collections.sort(arrayList11, new Comparator<TLObject>(this) { // from class: org.telegram.ui.Adapters.MentionsAdapter.6
                    private long getId(TLObject tLObject) {
                        if (tLObject instanceof TLRPC$User) {
                            return ((TLRPC$User) tLObject).id;
                        }
                        return -((TLRPC$Chat) tLObject).id;
                    }

                    @Override // java.util.Comparator
                    public int compare(TLObject tLObject, TLObject tLObject2) {
                        long id = getId(tLObject);
                        long id2 = getId(tLObject2);
                        if (longSparseArray3.indexOfKey(id) < 0 || longSparseArray3.indexOfKey(id2) < 0) {
                            if (longSparseArray3.indexOfKey(id) >= 0) {
                                return -1;
                            }
                            if (longSparseArray3.indexOfKey(id2) >= 0) {
                                return 1;
                            }
                            int indexOf2 = arrayList13.indexOf(Long.valueOf(id));
                            int indexOf3 = arrayList13.indexOf(Long.valueOf(id2));
                            if (indexOf2 != -1 && indexOf3 != -1) {
                                if (indexOf2 < indexOf3) {
                                    return -1;
                                }
                                return indexOf2 == indexOf3 ? 0 : 1;
                            } else if (indexOf2 == -1 || indexOf3 != -1) {
                                return (indexOf2 != -1 || indexOf3 == -1) ? 0 : 1;
                            } else {
                                return -1;
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
                if (((tLRPC$Chat4 != null && tLRPC$Chat4.megagroup) || this.searchInDailogs) && lowerCase3.length() > 0) {
                    if (arrayList11.size() < 5) {
                        Runnable runnable3 = new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                MentionsAdapter.this.lambda$searchUsernameOrHashtag$7(arrayList11, longSparseArray3);
                            }
                        };
                        this.cancelDelayRunnable = runnable3;
                        AndroidUtilities.runOnUIThread(runnable3, 1000L);
                    } else {
                        showUsersResult(arrayList11, longSparseArray3, true);
                    }
                    7 r10 = new 7(tLRPC$Chat4, lowerCase3, i7, arrayList11, longSparseArray3, messagesController);
                    this.searchGlobalRunnable = r10;
                    AndroidUtilities.runOnUIThread(r10, 200L);
                    return;
                }
                showUsersResult(arrayList11, longSparseArray3, true);
                return;
            }
        }
        sb.append(charSequence2.substring(1));
        this.resultStartPosition = 0;
        this.resultLength = sb.length();
        c = 0;
        c2 = 65535;
        i4 = -1;
        z3 = false;
        if (c != c2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchUsernameOrHashtag$7(ArrayList arrayList, LongSparseArray longSparseArray) {
        this.cancelDelayRunnable = null;
        showUsersResult(arrayList, longSparseArray, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 7 implements Runnable {
        final /* synthetic */ TLRPC$Chat val$chat;
        final /* synthetic */ MessagesController val$messagesController;
        final /* synthetic */ LongSparseArray val$newMap;
        final /* synthetic */ ArrayList val$newResult;
        final /* synthetic */ int val$threadId;
        final /* synthetic */ String val$usernameString;

        7(TLRPC$Chat tLRPC$Chat, String str, int i, ArrayList arrayList, LongSparseArray longSparseArray, MessagesController messagesController) {
            this.val$chat = tLRPC$Chat;
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
            TLRPC$TL_channels_getParticipants tLRPC$TL_channels_getParticipants = new TLRPC$TL_channels_getParticipants();
            tLRPC$TL_channels_getParticipants.channel = MessagesController.getInputChannel(this.val$chat);
            tLRPC$TL_channels_getParticipants.limit = 20;
            tLRPC$TL_channels_getParticipants.offset = 0;
            TLRPC$TL_channelParticipantsMentions tLRPC$TL_channelParticipantsMentions = new TLRPC$TL_channelParticipantsMentions();
            int i = tLRPC$TL_channelParticipantsMentions.flags | 1;
            tLRPC$TL_channelParticipantsMentions.flags = i;
            tLRPC$TL_channelParticipantsMentions.q = this.val$usernameString;
            int i2 = this.val$threadId;
            if (i2 != 0) {
                tLRPC$TL_channelParticipantsMentions.flags = i | 2;
                tLRPC$TL_channelParticipantsMentions.top_msg_id = i2;
            }
            tLRPC$TL_channels_getParticipants.filter = tLRPC$TL_channelParticipantsMentions;
            final int access$1704 = MentionsAdapter.access$1704(MentionsAdapter.this);
            MentionsAdapter mentionsAdapter = MentionsAdapter.this;
            ConnectionsManager connectionsManager = ConnectionsManager.getInstance(mentionsAdapter.currentAccount);
            final ArrayList arrayList = this.val$newResult;
            final LongSparseArray longSparseArray = this.val$newMap;
            final MessagesController messagesController = this.val$messagesController;
            mentionsAdapter.channelReqId = connectionsManager.sendRequest(tLRPC$TL_channels_getParticipants, new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$7$$ExternalSyntheticLambda1
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    MentionsAdapter.7.this.lambda$run$1(access$1704, arrayList, longSparseArray, messagesController, tLObject, tLRPC$TL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(final int i, final ArrayList arrayList, final LongSparseArray longSparseArray, final MessagesController messagesController, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MentionsAdapter.7.this.lambda$run$0(i, arrayList, longSparseArray, tLRPC$TL_error, tLObject, messagesController);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(int i, ArrayList arrayList, LongSparseArray longSparseArray, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, MessagesController messagesController) {
            if (MentionsAdapter.this.channelReqId != 0 && i == MentionsAdapter.this.channelLastReqId && MentionsAdapter.this.searchResultUsernamesMap != null && MentionsAdapter.this.searchResultUsernames != null) {
                MentionsAdapter.this.showUsersResult(arrayList, longSparseArray, false);
                if (tLRPC$TL_error == null) {
                    TLRPC$TL_channels_channelParticipants tLRPC$TL_channels_channelParticipants = (TLRPC$TL_channels_channelParticipants) tLObject;
                    messagesController.putUsers(tLRPC$TL_channels_channelParticipants.users, false);
                    messagesController.putChats(tLRPC$TL_channels_channelParticipants.chats, false);
                    MentionsAdapter.this.searchResultUsernames.isEmpty();
                    if (!tLRPC$TL_channels_channelParticipants.participants.isEmpty()) {
                        long clientUserId = UserConfig.getInstance(MentionsAdapter.this.currentAccount).getClientUserId();
                        for (int i2 = 0; i2 < tLRPC$TL_channels_channelParticipants.participants.size(); i2++) {
                            long peerId = MessageObject.getPeerId(tLRPC$TL_channels_channelParticipants.participants.get(i2).peer);
                            if (MentionsAdapter.this.searchResultUsernamesMap.indexOfKey(peerId) < 0 && ((peerId != 0 || MentionsAdapter.this.searchResultUsernamesMap.indexOfKey(clientUserId) < 0) && (MentionsAdapter.this.isSearchingMentions || (peerId != clientUserId && peerId != 0)))) {
                                if (peerId >= 0) {
                                    TLRPC$User user = messagesController.getUser(Long.valueOf(peerId));
                                    if (user == null) {
                                        return;
                                    }
                                    MentionsAdapter.this.searchResultUsernames.add(user);
                                } else {
                                    TLRPC$Chat chat = messagesController.getChat(Long.valueOf(-peerId));
                                    if (chat == null) {
                                        return;
                                    }
                                    MentionsAdapter.this.searchResultUsernames.add(chat);
                                }
                            }
                        }
                    }
                }
                MentionsAdapter.this.notifyDataSetChanged();
                MentionsAdapter.this.delegate.needChangePanelVisibility(!MentionsAdapter.this.searchResultUsernames.isEmpty());
            }
            MentionsAdapter.this.channelReqId = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchUsernameOrHashtag$8(ArrayList arrayList, String str) {
        this.searchResultSuggestions = arrayList;
        this.searchResultHashtags = null;
        this.stickers = null;
        this.searchResultUsernames = null;
        this.searchResultUsernamesMap = null;
        this.searchResultCommands = null;
        this.searchResultCommandsHelp = null;
        this.searchResultCommandsUsers = null;
        notifyDataSetChanged();
        MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
        ArrayList<MediaDataController.KeywordResult> arrayList2 = this.searchResultSuggestions;
        mentionsAdapterDelegate.needChangePanelVisibility((arrayList2 == null || arrayList2.isEmpty()) ? false : true);
    }

    public void setIsReversed(boolean z) {
        if (this.isReversed != z) {
            this.isReversed = z;
            int lastItemCount = getLastItemCount();
            if (lastItemCount > 0) {
                notifyItemChanged(0);
            }
            if (lastItemCount > 1) {
                notifyItemChanged(lastItemCount - 1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showUsersResult(ArrayList<TLObject> arrayList, LongSparseArray<TLObject> longSparseArray, boolean z) {
        this.searchResultUsernames = arrayList;
        if ((!this.allowBots || !this.allowChats) && arrayList != null) {
            Iterator<TLObject> it = arrayList.iterator();
            while (it.hasNext()) {
                TLObject next = it.next();
                if ((next instanceof TLRPC$Chat) && !this.allowChats) {
                    it.remove();
                } else if (next instanceof TLRPC$User) {
                    TLRPC$User tLRPC$User = (TLRPC$User) next;
                    if (tLRPC$User.bot || UserObject.isService(tLRPC$User.id)) {
                        it.remove();
                    }
                }
            }
        }
        this.searchResultUsernamesMap = longSparseArray;
        Runnable runnable = this.cancelDelayRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.cancelDelayRunnable = null;
        }
        this.searchResultBotContext = null;
        this.stickers = null;
        if (z) {
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

    public ArrayList<TLRPC$BotInlineResult> getSearchResultBotContext() {
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
            ArrayList<TLRPC$BotInlineResult> arrayList2 = this.searchResultBotContext;
            if (arrayList2 != null) {
                int size = arrayList2.size();
                if (this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) {
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
            if (arrayList6 != null) {
                return arrayList6.size();
            }
            return 0;
        }
        return 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (this.stickers != null) {
            return 4;
        }
        if (this.foundContextBot == null || this.inlineMediaEnabled) {
            if (this.searchResultBotContext != null) {
                if (i == 0) {
                    return (this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) ? 1 : 2;
                }
                return 1;
            }
            return 0;
        }
        return 3;
    }

    public void addHashtagsFromMessage(CharSequence charSequence) {
        this.searchAdapterHelper.addHashtagsFromMessage(charSequence);
    }

    public int getItemPosition(int i) {
        return this.searchResultBotContext != null ? (this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) ? i : i - 1 : i;
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
            if (i < 0 || i >= arrayList.size()) {
                return null;
            }
            return this.stickers.get(i).sticker;
        }
        ArrayList<TLRPC$BotInlineResult> arrayList2 = this.searchResultBotContext;
        if (arrayList2 != null) {
            TLRPC$TL_inlineBotWebView tLRPC$TL_inlineBotWebView = this.searchResultBotWebViewSwitch;
            if (tLRPC$TL_inlineBotWebView == null) {
                TLRPC$TL_inlineBotSwitchPM tLRPC$TL_inlineBotSwitchPM = this.searchResultBotContextSwitch;
                if (tLRPC$TL_inlineBotSwitchPM != null) {
                    if (i == 0) {
                        return tLRPC$TL_inlineBotSwitchPM;
                    }
                }
                if (i >= 0 || i >= arrayList2.size()) {
                    return null;
                }
                return this.searchResultBotContext.get(i);
            } else if (i == 0) {
                return tLRPC$TL_inlineBotWebView;
            }
            i--;
            if (i >= 0) {
            }
            return null;
        }
        ArrayList<TLObject> arrayList3 = this.searchResultUsernames;
        if (arrayList3 != null) {
            if (i < 0 || i >= arrayList3.size()) {
                return null;
            }
            return this.searchResultUsernames.get(i);
        }
        ArrayList<String> arrayList4 = this.searchResultHashtags;
        if (arrayList4 != null) {
            if (i < 0 || i >= arrayList4.size()) {
                return null;
            }
            return this.searchResultHashtags.get(i);
        }
        ArrayList<MediaDataController.KeywordResult> arrayList5 = this.searchResultSuggestions;
        if (arrayList5 != null) {
            if (i < 0 || i >= arrayList5.size()) {
                return null;
            }
            return this.searchResultSuggestions.get(i);
        }
        ArrayList<String> arrayList6 = this.searchResultCommands;
        if (arrayList6 == null || i < 0 || i >= arrayList6.size()) {
            return null;
        }
        ArrayList<TLRPC$User> arrayList7 = this.searchResultCommandsUsers;
        if (arrayList7 != null && (this.botsCount != 1 || (this.info instanceof TLRPC$TL_channelFull))) {
            if (arrayList7.get(i) != null) {
                Object[] objArr = new Object[2];
                objArr[0] = this.searchResultCommands.get(i);
                objArr[1] = this.searchResultCommandsUsers.get(i) != null ? this.searchResultCommandsUsers.get(i).username : "";
                return String.format("%s@%s", objArr);
            }
            return String.format("%s", this.searchResultCommands.get(i));
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
        return (this.foundContextBot == null || this.inlineMediaEnabled) ? false : true;
    }

    public boolean isMediaLayout() {
        return this.contextMedia || this.stickers != null;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
    public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
        return (this.foundContextBot == null || this.inlineMediaEnabled) && this.stickers == null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateViewHolder$9(ContextLinkCell contextLinkCell) {
        this.delegate.onContextClick(contextLinkCell.getResult());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ContextLinkCell contextLinkCell;
        if (i == 0) {
            MentionCell mentionCell = new MentionCell(this.mContext, this.resourcesProvider);
            mentionCell.setIsDarkTheme(this.isDarkTheme);
            contextLinkCell = mentionCell;
        } else if (i == 1) {
            ContextLinkCell contextLinkCell2 = new ContextLinkCell(this.mContext);
            contextLinkCell2.setDelegate(new ContextLinkCell.ContextLinkCellDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda9
                @Override // org.telegram.ui.Cells.ContextLinkCell.ContextLinkCellDelegate
                public final void didPressedImage(ContextLinkCell contextLinkCell3) {
                    MentionsAdapter.this.lambda$onCreateViewHolder$9(contextLinkCell3);
                }
            });
            contextLinkCell = contextLinkCell2;
        } else if (i == 2) {
            contextLinkCell = new BotSwitchCell(this.mContext);
        } else if (i == 3) {
            TextView textView = new TextView(this.mContext);
            textView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
            textView.setTextSize(1, 14.0f);
            textView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText2));
            contextLinkCell = textView;
        } else {
            contextLinkCell = new StickerCell(this.mContext, this.resourcesProvider);
        }
        return new RecyclerListView.Holder(contextLinkCell);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        int itemViewType = viewHolder.getItemViewType();
        if (itemViewType == 4) {
            StickerCell stickerCell = (StickerCell) viewHolder.itemView;
            StickerResult stickerResult = this.stickers.get(i);
            stickerCell.setSticker(stickerResult.sticker, stickerResult.parent);
            stickerCell.setClearsInputField(true);
        } else if (itemViewType == 3) {
            TextView textView = (TextView) viewHolder.itemView;
            TLRPC$Chat currentChat = this.parentFragment.getCurrentChat();
            if (currentChat != null) {
                if (!ChatObject.hasAdminRights(currentChat) && (tLRPC$TL_chatBannedRights = currentChat.default_banned_rights) != null && tLRPC$TL_chatBannedRights.send_inline) {
                    textView.setText(LocaleController.getString("GlobalAttachInlineRestricted", R.string.GlobalAttachInlineRestricted));
                } else if (AndroidUtilities.isBannedForever(currentChat.banned_rights)) {
                    textView.setText(LocaleController.getString("AttachInlineRestrictedForever", R.string.AttachInlineRestrictedForever));
                } else {
                    textView.setText(LocaleController.formatString("AttachInlineRestricted", R.string.AttachInlineRestricted, LocaleController.formatDateForBan(currentChat.banned_rights.until_date)));
                }
            }
        } else if (this.searchResultBotContext != null) {
            boolean z = (this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) ? false : true;
            if (viewHolder.getItemViewType() != 2) {
                if (z) {
                    i--;
                }
                ((ContextLinkCell) viewHolder.itemView).setLink(this.searchResultBotContext.get(i), this.foundContextBot, this.contextMedia, i != this.searchResultBotContext.size() - 1, z && i == 0, "gif".equals(this.searchingContextUsername));
            } else if (z) {
                BotSwitchCell botSwitchCell = (BotSwitchCell) viewHolder.itemView;
                TLRPC$TL_inlineBotSwitchPM tLRPC$TL_inlineBotSwitchPM = this.searchResultBotContextSwitch;
                botSwitchCell.setText(tLRPC$TL_inlineBotSwitchPM != null ? tLRPC$TL_inlineBotSwitchPM.text : this.searchResultBotWebViewSwitch.text);
            }
        } else {
            MentionCell mentionCell = (MentionCell) viewHolder.itemView;
            ArrayList<TLObject> arrayList = this.searchResultUsernames;
            if (arrayList != null) {
                TLObject tLObject = arrayList.get(i);
                if (tLObject instanceof TLRPC$User) {
                    mentionCell.setUser((TLRPC$User) tLObject);
                } else if (tLObject instanceof TLRPC$Chat) {
                    mentionCell.setChat((TLRPC$Chat) tLObject);
                }
            } else {
                ArrayList<String> arrayList2 = this.searchResultHashtags;
                if (arrayList2 != null) {
                    mentionCell.setText(arrayList2.get(i));
                } else {
                    ArrayList<MediaDataController.KeywordResult> arrayList3 = this.searchResultSuggestions;
                    if (arrayList3 != null) {
                        mentionCell.setEmojiSuggestion(arrayList3.get(i));
                    } else {
                        ArrayList<String> arrayList4 = this.searchResultCommands;
                        if (arrayList4 != null) {
                            String str = arrayList4.get(i);
                            String str2 = this.searchResultCommandsHelp.get(i);
                            ArrayList<TLRPC$User> arrayList5 = this.searchResultCommandsUsers;
                            mentionCell.setBotCommand(str, str2, arrayList5 != null ? arrayList5.get(i) : null);
                        }
                    }
                }
            }
            mentionCell.setDivider(false);
        }
    }

    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        TLRPC$User tLRPC$User;
        if (i == 2 && (tLRPC$User = this.foundContextBot) != null && tLRPC$User.bot_inline_geo) {
            if (iArr.length > 0 && iArr[0] == 0) {
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

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    public void setDialogId(long j) {
        this.dialog_id = j;
    }

    public void setUserOrChat(TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat) {
        this.user = tLRPC$User;
        this.chat = tLRPC$Chat;
    }

    public void setSearchInDailogs(boolean z) {
        this.searchInDailogs = z;
    }

    public void setAllowStickers(boolean z) {
        this.allowStickers = z;
    }

    public void setAllowBots(boolean z) {
        this.allowBots = z;
    }

    public void setAllowChats(boolean z) {
        this.allowChats = z;
    }
}
