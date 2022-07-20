package org.telegram.ui.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
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
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$BotInfo;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
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
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Cells.BotSwitchCell;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.MentionCell;
import org.telegram.ui.Cells.StickerCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class MentionsAdapter extends RecyclerListView.SelectionAdapter implements NotificationCenter.NotificationCenterDelegate {
    private LongSparseArray<TLRPC$BotInfo> botInfo;
    private int botsCount;
    private Runnable cancelDelayRunnable;
    private int channelLastReqId;
    private int channelReqId;
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
    private boolean visibleByStickersSearch;
    private int currentAccount = UserConfig.selectedAccount;
    private boolean needUsernames = true;
    private boolean needBotContext = true;
    private boolean inlineMediaEnabled = true;
    private ArrayList<String> stickersToLoad = new ArrayList<>();
    private SendMessagesHelper.LocationProvider locationProvider = new AnonymousClass2(new AnonymousClass1());
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

    /* loaded from: classes3.dex */
    public static class StickerResult {
        public Object parent;
        public TLRPC$Document sticker;

        public StickerResult(TLRPC$Document tLRPC$Document, Object obj) {
            this.sticker = tLRPC$Document;
            this.parent = obj;
        }
    }

    /* renamed from: org.telegram.ui.Adapters.MentionsAdapter$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements SendMessagesHelper.LocationProvider.LocationProviderDelegate {
        AnonymousClass1() {
            MentionsAdapter.this = r1;
        }

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
    }

    /* renamed from: org.telegram.ui.Adapters.MentionsAdapter$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends SendMessagesHelper.LocationProvider {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(SendMessagesHelper.LocationProvider.LocationProviderDelegate locationProviderDelegate) {
            super(locationProviderDelegate);
            MentionsAdapter.this = r1;
        }

        @Override // org.telegram.messenger.SendMessagesHelper.LocationProvider
        public void stop() {
            super.stop();
            MentionsAdapter.this.lastKnownLocation = null;
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
        searchAdapterHelper.setDelegate(new AnonymousClass3());
        if (!z) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoadFailed);
        }
    }

    /* renamed from: org.telegram.ui.Adapters.MentionsAdapter$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements SearchAdapterHelper.SearchAdapterHelperDelegate {
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

        AnonymousClass3() {
            MentionsAdapter.this = r1;
        }

        @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
        public void onDataSetChanged(int i) {
            MentionsAdapter.this.notifyDataSetChanged();
        }

        @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
        public void onSetHashtags(ArrayList<SearchAdapterHelper.HashtagObject> arrayList, HashMap<String, SearchAdapterHelper.HashtagObject> hashMap) {
            if (MentionsAdapter.this.lastText != null) {
                MentionsAdapter mentionsAdapter = MentionsAdapter.this;
                mentionsAdapter.searchUsernameOrHashtag(mentionsAdapter.lastText, MentionsAdapter.this.lastPosition, MentionsAdapter.this.messages, MentionsAdapter.this.lastUsernameOnly, MentionsAdapter.this.lastForSearch);
            }
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        ArrayList<StickerResult> arrayList;
        if ((i == NotificationCenter.fileLoaded || i == NotificationCenter.fileLoadFailed) && (arrayList = this.stickers) != null && !arrayList.isEmpty() && !this.stickersToLoad.isEmpty() && this.visibleByStickersSearch) {
            boolean z = false;
            this.stickersToLoad.remove((String) objArr[0]);
            if (!this.stickersToLoad.isEmpty()) {
                return;
            }
            MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
            if (getItemCountInternal() > 0) {
                z = true;
            }
            mentionsAdapterDelegate.needChangePanelVisibility(z);
        }
    }

    private void addStickerToResult(TLRPC$Document tLRPC$Document, Object obj) {
        if (tLRPC$Document == null) {
            return;
        }
        String str = tLRPC$Document.dc_id + "_" + tLRPC$Document.id;
        HashMap<String, TLRPC$Document> hashMap = this.stickersMap;
        if (hashMap != null && hashMap.containsKey(str)) {
            return;
        }
        if (!UserConfig.getInstance(this.currentAccount).isPremium() && MessageObject.isPremiumSticker(tLRPC$Document)) {
            return;
        }
        if (this.stickers == null) {
            this.stickers = new ArrayList<>();
            this.stickersMap = new HashMap<>();
        }
        this.stickers.add(new StickerResult(tLRPC$Document, obj));
        this.stickersMap.put(str, tLRPC$Document);
        EmojiView.ChooseStickerActionTracker chooseStickerActionTracker = this.mentionsStickersActionTracker;
        if (chooseStickerActionTracker == null) {
            return;
        }
        chooseStickerActionTracker.checkVisibility();
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
                if (str2 != null && str2.contains(str)) {
                    return true;
                }
                break;
            }
            i++;
        }
        return false;
    }

    private void searchServerStickers(String str, String str2) {
        TLRPC$TL_messages_getStickers tLRPC$TL_messages_getStickers = new TLRPC$TL_messages_getStickers();
        tLRPC$TL_messages_getStickers.emoticon = str2;
        tLRPC$TL_messages_getStickers.hash = 0L;
        this.lastReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getStickers, new MentionsAdapter$$ExternalSyntheticLambda7(this, str));
    }

    public /* synthetic */ void lambda$searchServerStickers$1(String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MentionsAdapter$$ExternalSyntheticLambda3(this, str, tLObject));
    }

    public /* synthetic */ void lambda$searchServerStickers$0(String str, TLObject tLObject) {
        ArrayList<StickerResult> arrayList;
        boolean z = false;
        this.lastReqId = 0;
        if (!str.equals(this.lastSticker) || !(tLObject instanceof TLRPC$TL_messages_stickers)) {
            return;
        }
        TLRPC$TL_messages_stickers tLRPC$TL_messages_stickers = (TLRPC$TL_messages_stickers) tLObject;
        ArrayList<StickerResult> arrayList2 = this.stickers;
        int size = arrayList2 != null ? arrayList2.size() : 0;
        ArrayList<TLRPC$Document> arrayList3 = tLRPC$TL_messages_stickers.stickers;
        addStickersToResult(arrayList3, "sticker_search_" + str);
        ArrayList<StickerResult> arrayList4 = this.stickers;
        int size2 = arrayList4 != null ? arrayList4.size() : 0;
        if (!this.visibleByStickersSearch && (arrayList = this.stickers) != null && !arrayList.isEmpty()) {
            checkStickerFilesExistAndDownload();
            MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
            if (getItemCountInternal() > 0) {
                z = true;
            }
            mentionsAdapterDelegate.needChangePanelVisibility(z);
            this.visibleByStickersSearch = true;
        }
        if (size == size2) {
            return;
        }
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void notifyDataSetChanged() {
        MentionsAdapterDelegate mentionsAdapterDelegate;
        int i = this.lastItemCount;
        int i2 = 0;
        if (i == -1 || this.lastData == null) {
            MentionsAdapterDelegate mentionsAdapterDelegate2 = this.delegate;
            if (mentionsAdapterDelegate2 != null) {
                mentionsAdapterDelegate2.onItemCountUpdate(0, getItemCount());
            }
            super.notifyDataSetChanged();
            this.lastData = new Object[getItemCount()];
            while (true) {
                Object[] objArr = this.lastData;
                if (i2 >= objArr.length) {
                    return;
                }
                objArr[i2] = getItem(i2);
                i2++;
            }
        } else {
            int itemCount = getItemCount();
            boolean z = i != itemCount;
            int min = Math.min(i, itemCount);
            Object[] objArr2 = new Object[itemCount];
            for (int i3 = 0; i3 < itemCount; i3++) {
                objArr2[i3] = getItem(i3);
            }
            while (i2 < min) {
                if (i2 >= 0) {
                    Object[] objArr3 = this.lastData;
                    if (i2 < objArr3.length && i2 < itemCount && itemsEqual(objArr3[i2], objArr2[i2])) {
                        i2++;
                    }
                }
                notifyItemChanged(i2);
                z = true;
                i2++;
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
        if (!(obj instanceof MediaDataController.KeywordResult) || !(obj2 instanceof MediaDataController.KeywordResult) || (str = (keywordResult = (MediaDataController.KeywordResult) obj).keyword) == null) {
            return false;
        }
        MediaDataController.KeywordResult keywordResult2 = (MediaDataController.KeywordResult) obj2;
        return str.equals(keywordResult2.keyword) && (str2 = keywordResult.emoji) != null && str2.equals(keywordResult2.emoji);
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
                    TLRPC$User tLRPC$User2 = this.foundContextBot;
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.parentFragment.getParentActivity());
                    builder.setTitle(LocaleController.getString("ShareYouLocationTitle", 2131628290));
                    builder.setMessage(LocaleController.getString("ShareYouLocationInline", 2131628289));
                    boolean[] zArr = new boolean[1];
                    builder.setPositiveButton(LocaleController.getString("OK", 2131627075), new MentionsAdapter$$ExternalSyntheticLambda1(this, zArr, tLRPC$User2));
                    builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), new MentionsAdapter$$ExternalSyntheticLambda0(this, zArr));
                    this.parentFragment.showDialog(builder.create(), new MentionsAdapter$$ExternalSyntheticLambda2(this, zArr));
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

    public /* synthetic */ void lambda$processFoundUser$2(boolean[] zArr, TLRPC$User tLRPC$User, DialogInterface dialogInterface, int i) {
        zArr[0] = true;
        if (tLRPC$User != null) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            edit.putBoolean("inlinegeo_" + tLRPC$User.id, true).commit();
            checkLocationPermissionsOrStart();
        }
    }

    public /* synthetic */ void lambda$processFoundUser$3(boolean[] zArr, DialogInterface dialogInterface, int i) {
        zArr[0] = true;
        onLocationUnavailable();
    }

    public /* synthetic */ void lambda$processFoundUser$4(boolean[] zArr, DialogInterface dialogInterface) {
        if (!zArr[0]) {
            onLocationUnavailable();
        }
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
                if (mentionsAdapterDelegate2 == null) {
                    return;
                }
                mentionsAdapterDelegate2.onContextSearch(false);
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
            AnonymousClass4 anonymousClass4 = new AnonymousClass4(str2, str, messagesController, messagesStorage);
            this.contextQueryRunnable = anonymousClass4;
            AndroidUtilities.runOnUIThread(anonymousClass4, 400L);
        }
    }

    /* renamed from: org.telegram.ui.Adapters.MentionsAdapter$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 implements Runnable {
        final /* synthetic */ MessagesController val$messagesController;
        final /* synthetic */ MessagesStorage val$messagesStorage;
        final /* synthetic */ String val$query;
        final /* synthetic */ String val$username;

        AnonymousClass4(String str, String str2, MessagesController messagesController, MessagesStorage messagesStorage) {
            MentionsAdapter.this = r1;
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
            mentionsAdapter2.contextUsernameReqid = ConnectionsManager.getInstance(mentionsAdapter2.currentAccount).sendRequest(tLRPC$TL_contacts_resolveUsername, new MentionsAdapter$4$$ExternalSyntheticLambda1(this, this.val$username, this.val$messagesController, this.val$messagesStorage));
        }

        public /* synthetic */ void lambda$run$1(String str, MessagesController messagesController, MessagesStorage messagesStorage, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new MentionsAdapter$4$$ExternalSyntheticLambda0(this, str, tLRPC$TL_error, tLObject, messagesController, messagesStorage));
        }

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
        if (str != null && str.equals("gif")) {
            return "Search GIFs";
        }
        return null;
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

    public void searchForContextBotResults(boolean z, TLRPC$User tLRPC$User, String str, String str2) {
        Location location;
        if (this.contextQueryReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
            this.contextQueryReqid = 0;
        }
        if (!this.inlineMediaEnabled) {
            MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
            if (mentionsAdapterDelegate == null) {
                return;
            }
            mentionsAdapterDelegate.onContextSearch(false);
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
            String sb2 = sb.toString();
            MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
            MentionsAdapter$$ExternalSyntheticLambda8 mentionsAdapter$$ExternalSyntheticLambda8 = new MentionsAdapter$$ExternalSyntheticLambda8(this, str, z, tLRPC$User, str2, messagesStorage, sb2);
            if (z) {
                messagesStorage.getBotCache(sb2, mentionsAdapter$$ExternalSyntheticLambda8);
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
            this.contextQueryReqid = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getInlineBotResults, mentionsAdapter$$ExternalSyntheticLambda8, 2);
        }
    }

    public /* synthetic */ void lambda$searchForContextBotResults$6(String str, boolean z, TLRPC$User tLRPC$User, String str2, MessagesStorage messagesStorage, String str3, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MentionsAdapter$$ExternalSyntheticLambda4(this, str, z, tLObject, tLRPC$User, str2, messagesStorage, str3));
    }

    public /* synthetic */ void lambda$searchForContextBotResults$5(String str, boolean z, TLObject tLObject, TLRPC$User tLRPC$User, String str2, MessagesStorage messagesStorage, String str3) {
        boolean z2;
        if (!str.equals(this.searchingContextQuery)) {
            return;
        }
        boolean z3 = false;
        this.contextQueryReqid = 0;
        if (z && tLObject == null) {
            searchForContextBotResults(false, tLRPC$User, str, str2);
        } else {
            MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
            if (mentionsAdapterDelegate != null) {
                mentionsAdapterDelegate.onContextSearch(false);
            }
        }
        if (!(tLObject instanceof TLRPC$TL_messages_botResults)) {
            return;
        }
        TLRPC$TL_messages_botResults tLRPC$TL_messages_botResults = (TLRPC$TL_messages_botResults) tLObject;
        if (!z && tLRPC$TL_messages_botResults.cache_time != 0) {
            messagesStorage.saveBotCache(str3, tLRPC$TL_messages_botResults);
        }
        this.nextQueryOffset = tLRPC$TL_messages_botResults.next_offset;
        if (this.searchResultBotContextSwitch == null) {
            this.searchResultBotContextSwitch = tLRPC$TL_messages_botResults.switch_pm;
        }
        int i = 0;
        while (i < tLRPC$TL_messages_botResults.results.size()) {
            TLRPC$BotInlineResult tLRPC$BotInlineResult = tLRPC$TL_messages_botResults.results.get(i);
            if (!(tLRPC$BotInlineResult.document instanceof TLRPC$TL_document) && !(tLRPC$BotInlineResult.photo instanceof TLRPC$TL_photo) && !"game".equals(tLRPC$BotInlineResult.type) && tLRPC$BotInlineResult.content == null && (tLRPC$BotInlineResult.send_message instanceof TLRPC$TL_botInlineMessageMediaAuto)) {
                tLRPC$TL_messages_botResults.results.remove(i);
                i--;
            }
            tLRPC$BotInlineResult.query_id = tLRPC$TL_messages_botResults.query_id;
            i++;
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
        if (z2) {
            int i2 = this.searchResultBotContextSwitch != null ? 1 : 0;
            notifyItemChanged(((this.searchResultBotContext.size() - tLRPC$TL_messages_botResults.results.size()) + i2) - 1);
            notifyItemRangeInserted((this.searchResultBotContext.size() - tLRPC$TL_messages_botResults.results.size()) + i2, tLRPC$TL_messages_botResults.results.size());
        } else {
            notifyDataSetChanged();
        }
        MentionsAdapterDelegate mentionsAdapterDelegate2 = this.delegate;
        if (!this.searchResultBotContext.isEmpty() || this.searchResultBotContextSwitch != null) {
            z3 = true;
        }
        mentionsAdapterDelegate2.needChangePanelVisibility(z3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:229:0x03d6  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x03dc  */
    /* JADX WARN: Type inference failed for: r20v0, types: [org.telegram.ui.Adapters.MentionsAdapter] */
    /* JADX WARN: Type inference failed for: r6v59 */
    /* JADX WARN: Type inference failed for: r6v60 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void searchUsernameOrHashtag(String str, int i, ArrayList<MessageObject> arrayList, boolean z, boolean z2) {
        String str2;
        char c;
        boolean z3;
        char c2;
        String str3;
        String str4;
        int i2;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$ChatFull tLRPC$ChatFull;
        LongSparseArray longSparseArray;
        String str5;
        String str6;
        String str7;
        TLRPC$User tLRPC$User;
        long j;
        int i3;
        int i4;
        String str8;
        String str9;
        int i5;
        ChatActivity chatActivity;
        int i6;
        int i7;
        CharSequence charSequence;
        boolean z4 = z;
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
        if (TextUtils.isEmpty(str) || str.length() > MessagesController.getInstance(this.currentAccount).maxMessageLength) {
            searchForContextBot(null, null);
            this.delegate.needChangePanelVisibility(false);
            this.lastText = null;
            clearStickers();
            return;
        }
        int i8 = str.length() > 0 ? i - 1 : i;
        this.lastText = null;
        this.lastUsernameOnly = z4;
        this.lastForSearch = z2;
        StringBuilder sb = new StringBuilder();
        boolean z5 = !z4 && str.length() > 0 && str.length() <= 14;
        String str10 = "";
        if (z5) {
            int length = str.length();
            String str11 = str;
            int i9 = 0;
            while (i9 < length) {
                char charAt = str11.charAt(i9);
                int i10 = length - 1;
                char charAt2 = i9 < i10 ? str11.charAt(i9 + 1) : (char) 0;
                if (i9 >= i10 || charAt != 55356 || charAt2 < 57339 || charAt2 > 57343) {
                    charSequence = str11;
                    if (charAt == 65039) {
                        i7 = 1;
                        charSequence = TextUtils.concat(str11.subSequence(0, i9), str11.subSequence(i9 + 1, str11.length()));
                        length--;
                        i9--;
                        i9 += i7;
                        str11 = charSequence;
                    }
                } else {
                    CharSequence[] charSequenceArr = {str11.subSequence(0, i9), str11.subSequence(i9 + 2, str11.length())};
                    length -= 2;
                    i9--;
                    charSequence = TextUtils.concat(charSequenceArr);
                }
                i7 = 1;
                i9 += i7;
                str11 = charSequence;
            }
            this.lastSticker = str11.toString().trim();
            str2 = str;
        } else {
            str2 = str10;
        }
        boolean z6 = z5 && (Emoji.isValidEmoji(str2) || Emoji.isValidEmoji(this.lastSticker));
        if (z6 && (chatActivity = this.parentFragment) != null && (chatActivity.getCurrentChat() == null || ChatObject.canSendStickers(this.parentFragment.getCurrentChat()))) {
            this.stickersToLoad.clear();
            int i11 = SharedConfig.suggestStickers;
            if (i11 == 2 || !z6) {
                if (!this.visibleByStickersSearch || i11 != 2) {
                    return;
                }
                this.visibleByStickersSearch = false;
                this.delegate.needChangePanelVisibility(false);
                notifyDataSetChanged();
                return;
            }
            this.stickers = null;
            this.stickersMap = null;
            if (this.lastReqId != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.lastReqId, true);
                i6 = 0;
                this.lastReqId = 0;
            } else {
                i6 = 0;
            }
            boolean z7 = MessagesController.getInstance(this.currentAccount).suggestStickersApiOnly;
            if (!z7) {
                ArrayList<TLRPC$Document> recentStickersNoCopy = MediaDataController.getInstance(this.currentAccount).getRecentStickersNoCopy(i6);
                ArrayList<TLRPC$Document> recentStickersNoCopy2 = MediaDataController.getInstance(this.currentAccount).getRecentStickersNoCopy(2);
                int min = Math.min(20, recentStickersNoCopy.size());
                int i12 = 0;
                for (int i13 = 0; i13 < min; i13++) {
                    TLRPC$Document tLRPC$Document = recentStickersNoCopy.get(i13);
                    if (isValidSticker(tLRPC$Document, this.lastSticker)) {
                        addStickerToResult(tLRPC$Document, "recent");
                        int i14 = i12 + 1;
                        if (i14 >= 5) {
                            break;
                        }
                        i12 = i14;
                    }
                }
                int size = recentStickersNoCopy2.size();
                for (int i15 = 0; i15 < size; i15++) {
                    TLRPC$Document tLRPC$Document2 = recentStickersNoCopy2.get(i15);
                    if (isValidSticker(tLRPC$Document2, this.lastSticker)) {
                        addStickerToResult(tLRPC$Document2, "fav");
                    }
                }
                HashMap<String, ArrayList<TLRPC$Document>> allStickers = MediaDataController.getInstance(this.currentAccount).getAllStickers();
                ArrayList<TLRPC$Document> arrayList2 = allStickers != null ? allStickers.get(this.lastSticker) : null;
                if (arrayList2 != null && !arrayList2.isEmpty()) {
                    addStickersToResult(arrayList2, null);
                }
                ArrayList<StickerResult> arrayList3 = this.stickers;
                if (arrayList3 != null) {
                    Collections.sort(arrayList3, new AnonymousClass5(this, recentStickersNoCopy2, recentStickersNoCopy));
                }
            }
            if (SharedConfig.suggestStickers == 0 || z7) {
                searchServerStickers(this.lastSticker, str2);
            }
            ArrayList<StickerResult> arrayList4 = this.stickers;
            if (arrayList4 != null && !arrayList4.isEmpty()) {
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
            if (!z4 && this.needBotContext && str.charAt(0) == '@') {
                int indexOf = str.indexOf(32);
                int length2 = str.length();
                if (indexOf > 0) {
                    String substring = str.substring(1, indexOf);
                    str8 = str.substring(indexOf + 1);
                    str9 = substring;
                    i5 = 1;
                } else if (str.charAt(length2 - 1) == 't' && str.charAt(length2 - 2) == 'o' && str.charAt(length2 - 3) == 'b') {
                    i5 = 1;
                    str9 = str.substring(1);
                    str8 = str10;
                } else {
                    i5 = 1;
                    searchForContextBot(null, null);
                    str9 = null;
                    str8 = null;
                }
                if (str9 != null && str9.length() >= i5) {
                    int i16 = 1;
                    while (true) {
                        if (i16 >= str9.length()) {
                            str10 = str9;
                            break;
                        }
                        char charAt3 = str9.charAt(i16);
                        if ((charAt3 < '0' || charAt3 > '9') && ((charAt3 < 'a' || charAt3 > 'z') && ((charAt3 < 'A' || charAt3 > 'Z') && charAt3 != '_'))) {
                            break;
                        }
                        i16++;
                    }
                }
                searchForContextBot(str10, str8);
            } else {
                searchForContextBot(null, null);
            }
            c = 65535;
        }
        if (this.foundContextBot != null) {
            return;
        }
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        if (!z4) {
            while (i8 >= 0) {
                if (i8 < str.length()) {
                    char charAt4 = str.charAt(i8);
                    if (i8 != 0) {
                        int i17 = i8 - 1;
                        if (str.charAt(i17) != ' ' && str.charAt(i17) != '\n' && charAt4 != ':') {
                            i3 = 0;
                            sb.insert(i3, charAt4);
                        }
                    }
                    if (charAt4 != '@') {
                        if (charAt4 == '#') {
                            if (this.searchAdapterHelper.loadRecentHashtags()) {
                                this.resultStartPosition = i8;
                                this.resultLength = sb.length() + 1;
                                sb.insert(0, charAt4);
                                c2 = 65535;
                                i8 = -1;
                                c = 1;
                            } else {
                                this.lastText = str;
                                this.lastPosition = i;
                                this.messages = arrayList;
                                return;
                            }
                        } else if (i8 == 0 && this.botInfo != null && charAt4 == '/') {
                            this.resultStartPosition = i8;
                            this.resultLength = sb.length() + 1;
                            c2 = 65535;
                            i8 = -1;
                            c = 2;
                        } else if (charAt4 == ':' && sb.length() > 0) {
                            if (" !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\n".indexOf(sb.charAt(0)) >= 0) {
                                i4 = 1;
                                if (sb.length() > 1) {
                                }
                            } else {
                                i4 = 1;
                            }
                            this.resultStartPosition = i8;
                            this.resultLength = sb.length() + i4;
                            c2 = 65535;
                            i8 = -1;
                            c = 3;
                        }
                        z3 = false;
                        break;
                    } else if (this.needUsernames || (this.needBotContext && i8 == 0)) {
                        if (this.info == null && i8 != 0) {
                            this.lastText = str;
                            this.lastPosition = i;
                            this.messages = arrayList;
                            this.delegate.needChangePanelVisibility(false);
                            return;
                        }
                        this.resultStartPosition = i8;
                        this.resultLength = sb.length() + 1;
                        c2 = 65535;
                    }
                    i3 = 0;
                    sb.insert(i3, charAt4);
                }
                i8--;
            }
            z3 = false;
            c2 = 65535;
            i8 = -1;
            if (c != c2) {
                this.delegate.needChangePanelVisibility(z3);
                return;
            } else if (c != 0) {
                if (c == 1) {
                    ArrayList<String> arrayList5 = new ArrayList<>();
                    String lowerCase = sb.toString().toLowerCase();
                    ArrayList<SearchAdapterHelper.HashtagObject> hashtags = this.searchAdapterHelper.getHashtags();
                    for (int i18 = 0; i18 < hashtags.size(); i18++) {
                        SearchAdapterHelper.HashtagObject hashtagObject = hashtags.get(i18);
                        if (hashtagObject != null && (str4 = hashtagObject.hashtag) != null && str4.startsWith(lowerCase)) {
                            arrayList5.add(hashtagObject.hashtag);
                        }
                    }
                    this.searchResultHashtags = arrayList5;
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
                } else if (c != 2) {
                    if (c == 3) {
                        String[] currentKeyboardLanguage = AndroidUtilities.getCurrentKeyboardLanguage();
                        if (!Arrays.equals(currentKeyboardLanguage, this.lastSearchKeyboardLanguage)) {
                            MediaDataController.getInstance(this.currentAccount).fetchNewEmojiKeywords(currentKeyboardLanguage);
                        }
                        this.lastSearchKeyboardLanguage = currentKeyboardLanguage;
                        MediaDataController.getInstance(this.currentAccount).getEmojiSuggestions(this.lastSearchKeyboardLanguage, sb.toString(), false, new MentionsAdapter$$ExternalSyntheticLambda6(this));
                        return;
                    } else if (c != 4) {
                        return;
                    } else {
                        this.searchResultHashtags = null;
                        this.searchResultUsernames = null;
                        this.searchResultUsernamesMap = null;
                        this.searchResultSuggestions = null;
                        this.searchResultCommands = null;
                        this.searchResultCommandsHelp = null;
                        this.searchResultCommandsUsers = null;
                        return;
                    }
                } else {
                    ArrayList<String> arrayList6 = new ArrayList<>();
                    ArrayList<String> arrayList7 = new ArrayList<>();
                    ArrayList<TLRPC$User> arrayList8 = new ArrayList<>();
                    String lowerCase2 = sb.toString().toLowerCase();
                    for (int i19 = 0; i19 < this.botInfo.size(); i19++) {
                        TLRPC$BotInfo valueAt = this.botInfo.valueAt(i19);
                        for (int i20 = 0; i20 < valueAt.commands.size(); i20++) {
                            TLRPC$TL_botCommand tLRPC$TL_botCommand = valueAt.commands.get(i20);
                            if (tLRPC$TL_botCommand != null && (str3 = tLRPC$TL_botCommand.command) != null && str3.startsWith(lowerCase2)) {
                                arrayList6.add("/" + tLRPC$TL_botCommand.command);
                                arrayList7.add(tLRPC$TL_botCommand.description);
                                arrayList8.add(messagesController.getUser(Long.valueOf(valueAt.user_id)));
                            }
                        }
                    }
                    this.searchResultHashtags = null;
                    this.stickers = null;
                    this.searchResultUsernames = null;
                    this.searchResultUsernamesMap = null;
                    this.searchResultSuggestions = null;
                    this.searchResultCommands = arrayList6;
                    this.searchResultCommandsHelp = arrayList7;
                    this.searchResultCommandsUsers = arrayList8;
                    notifyDataSetChanged();
                    this.delegate.needChangePanelVisibility(!arrayList6.isEmpty());
                    return;
                }
            } else {
                ArrayList arrayList9 = new ArrayList();
                for (int i21 = 0; i21 < Math.min(100, arrayList.size()); i21++) {
                    long fromChatId = arrayList.get(i21).getFromChatId();
                    if (fromChatId > 0 && !arrayList9.contains(Long.valueOf(fromChatId))) {
                        arrayList9.add(Long.valueOf(fromChatId));
                    }
                }
                String lowerCase3 = sb.toString().toLowerCase();
                boolean z8 = lowerCase3.indexOf(32) >= 0;
                ArrayList arrayList10 = new ArrayList();
                LongSparseArray longSparseArray2 = new LongSparseArray();
                LongSparseArray longSparseArray3 = new LongSparseArray();
                ArrayList<TLRPC$TL_topPeer> arrayList11 = MediaDataController.getInstance(this.currentAccount).inlineBots;
                if (!z4 && this.needBotContext && i8 == 0 && !arrayList11.isEmpty()) {
                    int i22 = 0;
                    for (int i23 = 0; i23 < arrayList11.size(); i23++) {
                        TLRPC$User user = messagesController.getUser(Long.valueOf(arrayList11.get(i23).peer.user_id));
                        if (user != null) {
                            if (!TextUtils.isEmpty(user.username) && (lowerCase3.length() == 0 || user.username.toLowerCase().startsWith(lowerCase3))) {
                                arrayList10.add(user);
                                longSparseArray2.put(user.id, user);
                                longSparseArray3.put(user.id, user);
                                i22++;
                            }
                            if (i22 == 5) {
                                break;
                            }
                        }
                    }
                }
                ChatActivity chatActivity2 = this.parentFragment;
                if (chatActivity2 != null) {
                    tLRPC$Chat = chatActivity2.getCurrentChat();
                    i2 = this.parentFragment.getThreadId();
                } else {
                    TLRPC$ChatFull tLRPC$ChatFull2 = this.info;
                    tLRPC$Chat = tLRPC$ChatFull2 != null ? messagesController.getChat(Long.valueOf(tLRPC$ChatFull2.id)) : null;
                    i2 = 0;
                }
                if (tLRPC$Chat != null && (tLRPC$ChatFull = this.info) != null && tLRPC$ChatFull.participants != null && (!ChatObject.isChannel(tLRPC$Chat) || tLRPC$Chat.megagroup)) {
                    int i24 = z2 ? -1 : 0;
                    while (i24 < this.info.participants.participants.size()) {
                        if (i24 == -1) {
                            if (lowerCase3.length() == 0) {
                                arrayList10.add(tLRPC$Chat);
                                longSparseArray = longSparseArray2;
                            } else {
                                String str12 = tLRPC$Chat.title;
                                str6 = tLRPC$Chat.username;
                                longSparseArray = longSparseArray2;
                                j = -tLRPC$Chat.id;
                                str7 = str12;
                                str5 = null;
                                tLRPC$User = tLRPC$Chat;
                                if ((!TextUtils.isEmpty(str6) && str6.toLowerCase().startsWith(lowerCase3)) || ((!TextUtils.isEmpty(str7) && str7.toLowerCase().startsWith(lowerCase3)) || ((!TextUtils.isEmpty(str5) && str5.toLowerCase().startsWith(lowerCase3)) || (z8 && ContactsController.formatName(str7, str5).toLowerCase().startsWith(lowerCase3))))) {
                                    arrayList10.add(tLRPC$User);
                                    longSparseArray3.put(j, tLRPC$User);
                                }
                            }
                        } else {
                            tLRPC$User = messagesController.getUser(Long.valueOf(this.info.participants.participants.get(i24).user_id));
                            if (tLRPC$User != null && ((z4 || !UserObject.isUserSelf(tLRPC$User)) && longSparseArray2.indexOfKey(tLRPC$User.id) < 0)) {
                                if (lowerCase3.length() == 0 && !tLRPC$User.deleted) {
                                    arrayList10.add(tLRPC$User);
                                } else {
                                    str7 = tLRPC$User.first_name;
                                    String str13 = tLRPC$User.last_name;
                                    String str14 = tLRPC$User.username;
                                    longSparseArray = longSparseArray2;
                                    j = tLRPC$User.id;
                                    str5 = str13;
                                    str6 = str14;
                                    if (!TextUtils.isEmpty(str6)) {
                                        arrayList10.add(tLRPC$User);
                                        longSparseArray3.put(j, tLRPC$User);
                                    }
                                    arrayList10.add(tLRPC$User);
                                    longSparseArray3.put(j, tLRPC$User);
                                }
                            }
                            longSparseArray = longSparseArray2;
                        }
                        i24++;
                        longSparseArray2 = longSparseArray;
                        z4 = z;
                    }
                }
                Collections.sort(arrayList10, new AnonymousClass6(this, longSparseArray3, arrayList9));
                this.searchResultHashtags = null;
                this.stickers = null;
                this.searchResultCommands = null;
                this.searchResultCommandsHelp = null;
                this.searchResultCommandsUsers = null;
                this.searchResultSuggestions = null;
                if (tLRPC$Chat != null && tLRPC$Chat.megagroup && lowerCase3.length() > 0) {
                    if (arrayList10.size() < 5) {
                        MentionsAdapter$$ExternalSyntheticLambda5 mentionsAdapter$$ExternalSyntheticLambda5 = new MentionsAdapter$$ExternalSyntheticLambda5(this, arrayList10, longSparseArray3);
                        this.cancelDelayRunnable = mentionsAdapter$$ExternalSyntheticLambda5;
                        AndroidUtilities.runOnUIThread(mentionsAdapter$$ExternalSyntheticLambda5, 1000L);
                    } else {
                        showUsersResult(arrayList10, longSparseArray3, true);
                    }
                    AnonymousClass7 anonymousClass7 = new AnonymousClass7(tLRPC$Chat, lowerCase3, i2, arrayList10, longSparseArray3, messagesController);
                    this.searchGlobalRunnable = anonymousClass7;
                    AndroidUtilities.runOnUIThread(anonymousClass7, 200L);
                    return;
                }
                showUsersResult(arrayList10, longSparseArray3, true);
                return;
            }
        }
        sb.append(str.substring(1));
        this.resultStartPosition = 0;
        this.resultLength = sb.length();
        c2 = 65535;
        i8 = -1;
        c = 0;
        z3 = false;
        if (c != c2) {
        }
    }

    /* renamed from: org.telegram.ui.Adapters.MentionsAdapter$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 implements Comparator<StickerResult> {
        final /* synthetic */ ArrayList val$favsStickers;
        final /* synthetic */ ArrayList val$recentStickers;

        AnonymousClass5(MentionsAdapter mentionsAdapter, ArrayList arrayList, ArrayList arrayList2) {
            this.val$favsStickers = arrayList;
            this.val$recentStickers = arrayList2;
        }

        private int getIndex(StickerResult stickerResult) {
            for (int i = 0; i < this.val$favsStickers.size(); i++) {
                if (((TLRPC$Document) this.val$favsStickers.get(i)).id == stickerResult.sticker.id) {
                    return i + 2000000;
                }
            }
            for (int i2 = 0; i2 < Math.min(20, this.val$recentStickers.size()); i2++) {
                if (((TLRPC$Document) this.val$recentStickers.get(i2)).id == stickerResult.sticker.id) {
                    return (this.val$recentStickers.size() - i2) + 1000000;
                }
            }
            return -1;
        }

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
    }

    /* renamed from: org.telegram.ui.Adapters.MentionsAdapter$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 implements Comparator<TLObject> {
        final /* synthetic */ LongSparseArray val$newMap;
        final /* synthetic */ ArrayList val$users;

        AnonymousClass6(MentionsAdapter mentionsAdapter, LongSparseArray longSparseArray, ArrayList arrayList) {
            this.val$newMap = longSparseArray;
            this.val$users = arrayList;
        }

        private long getId(TLObject tLObject) {
            if (tLObject instanceof TLRPC$User) {
                return ((TLRPC$User) tLObject).id;
            }
            return -((TLRPC$Chat) tLObject).id;
        }

        public int compare(TLObject tLObject, TLObject tLObject2) {
            long id = getId(tLObject);
            long id2 = getId(tLObject2);
            if (this.val$newMap.indexOfKey(id) < 0 || this.val$newMap.indexOfKey(id2) < 0) {
                if (this.val$newMap.indexOfKey(id) >= 0) {
                    return -1;
                }
                if (this.val$newMap.indexOfKey(id2) >= 0) {
                    return 1;
                }
                int indexOf = this.val$users.indexOf(Long.valueOf(id));
                int indexOf2 = this.val$users.indexOf(Long.valueOf(id2));
                if (indexOf != -1 && indexOf2 != -1) {
                    if (indexOf < indexOf2) {
                        return -1;
                    }
                    return indexOf == indexOf2 ? 0 : 1;
                } else if (indexOf != -1 && indexOf2 == -1) {
                    return -1;
                } else {
                    return (indexOf != -1 || indexOf2 == -1) ? 0 : 1;
                }
            }
            return 0;
        }
    }

    public /* synthetic */ void lambda$searchUsernameOrHashtag$7(ArrayList arrayList, LongSparseArray longSparseArray) {
        this.cancelDelayRunnable = null;
        showUsersResult(arrayList, longSparseArray, true);
    }

    /* renamed from: org.telegram.ui.Adapters.MentionsAdapter$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 implements Runnable {
        final /* synthetic */ TLRPC$Chat val$chat;
        final /* synthetic */ MessagesController val$messagesController;
        final /* synthetic */ LongSparseArray val$newMap;
        final /* synthetic */ ArrayList val$newResult;
        final /* synthetic */ int val$threadId;
        final /* synthetic */ String val$usernameString;

        AnonymousClass7(TLRPC$Chat tLRPC$Chat, String str, int i, ArrayList arrayList, LongSparseArray longSparseArray, MessagesController messagesController) {
            MentionsAdapter.this = r1;
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
            int access$1704 = MentionsAdapter.access$1704(MentionsAdapter.this);
            MentionsAdapter mentionsAdapter = MentionsAdapter.this;
            mentionsAdapter.channelReqId = ConnectionsManager.getInstance(mentionsAdapter.currentAccount).sendRequest(tLRPC$TL_channels_getParticipants, new MentionsAdapter$7$$ExternalSyntheticLambda1(this, access$1704, this.val$newResult, this.val$newMap, this.val$messagesController));
        }

        public /* synthetic */ void lambda$run$1(int i, ArrayList arrayList, LongSparseArray longSparseArray, MessagesController messagesController, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new MentionsAdapter$7$$ExternalSyntheticLambda0(this, i, arrayList, longSparseArray, tLRPC$TL_error, tLObject, messagesController));
        }

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
                            if (MentionsAdapter.this.searchResultUsernamesMap.indexOfKey(peerId) < 0 && (MentionsAdapter.this.isSearchingMentions || peerId != clientUserId)) {
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
        mentionsAdapterDelegate.needChangePanelVisibility(arrayList2 != null && !arrayList2.isEmpty());
    }

    public void setIsReversed(boolean z) {
        if (this.isReversed != z) {
            this.isReversed = z;
            int lastItemCount = getLastItemCount();
            if (lastItemCount > 0) {
                notifyItemChanged(0);
            }
            if (lastItemCount <= 1) {
                return;
            }
            notifyItemChanged(lastItemCount - 1);
        }
    }

    public void showUsersResult(ArrayList<TLObject> arrayList, LongSparseArray<TLObject> longSparseArray, boolean z) {
        this.searchResultUsernames = arrayList;
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

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (this.stickers != null) {
            return 4;
        }
        if (this.foundContextBot != null && !this.inlineMediaEnabled) {
            return 3;
        }
        if (this.searchResultBotContext == null) {
            return 0;
        }
        return (i != 0 || this.searchResultBotContextSwitch == null) ? 1 : 2;
    }

    public void addHashtagsFromMessage(CharSequence charSequence) {
        this.searchAdapterHelper.addHashtagsFromMessage(charSequence);
    }

    public int getItemPosition(int i) {
        return (this.searchResultBotContext == null || this.searchResultBotContextSwitch == null) ? i : i - 1;
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
        ArrayList<TLRPC$BotInlineResult> arrayList2 = this.searchResultBotContext;
        if (arrayList2 != null) {
            TLRPC$TL_inlineBotSwitchPM tLRPC$TL_inlineBotSwitchPM = this.searchResultBotContextSwitch;
            if (tLRPC$TL_inlineBotSwitchPM != null) {
                if (i == 0) {
                    return tLRPC$TL_inlineBotSwitchPM;
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
        ArrayList<TLRPC$User> arrayList7 = this.searchResultCommandsUsers;
        if (arrayList7 != null && (this.botsCount != 1 || (this.info instanceof TLRPC$TL_channelFull))) {
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
    public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
        return (this.foundContextBot == null || this.inlineMediaEnabled) && this.stickers == null;
    }

    public /* synthetic */ void lambda$onCreateViewHolder$9(ContextLinkCell contextLinkCell) {
        this.delegate.onContextClick(contextLinkCell.getResult());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        StickerCell stickerCell;
        if (i == 0) {
            MentionCell mentionCell = new MentionCell(this.mContext, this.resourcesProvider);
            mentionCell.setIsDarkTheme(this.isDarkTheme);
            stickerCell = mentionCell;
        } else if (i == 1) {
            ContextLinkCell contextLinkCell = new ContextLinkCell(this.mContext);
            contextLinkCell.setDelegate(new MentionsAdapter$$ExternalSyntheticLambda9(this));
            stickerCell = contextLinkCell;
        } else if (i == 2) {
            stickerCell = new BotSwitchCell(this.mContext);
        } else if (i == 3) {
            TextView textView = new TextView(this.mContext);
            textView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
            textView.setTextSize(1, 14.0f);
            textView.setTextColor(getThemedColor("windowBackgroundWhiteGrayText2"));
            stickerCell = textView;
        } else {
            stickerCell = new StickerCell(this.mContext);
        }
        return new RecyclerListView.Holder(stickerCell);
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
            if (currentChat == null) {
                return;
            }
            if (!ChatObject.hasAdminRights(currentChat) && (tLRPC$TL_chatBannedRights = currentChat.default_banned_rights) != null && tLRPC$TL_chatBannedRights.send_inline) {
                textView.setText(LocaleController.getString("GlobalAttachInlineRestricted", 2131626076));
            } else if (AndroidUtilities.isBannedForever(currentChat.banned_rights)) {
                textView.setText(LocaleController.getString("AttachInlineRestrictedForever", 2131624487));
            } else {
                textView.setText(LocaleController.formatString("AttachInlineRestricted", 2131624486, LocaleController.formatDateForBan(currentChat.banned_rights.until_date)));
            }
        } else if (this.searchResultBotContext != null) {
            boolean z = this.searchResultBotContextSwitch != null;
            if (viewHolder.getItemViewType() == 2) {
                if (!z) {
                    return;
                }
                ((BotSwitchCell) viewHolder.itemView).setText(this.searchResultBotContextSwitch.text);
                return;
            }
            if (z) {
                i--;
            }
            ((ContextLinkCell) viewHolder.itemView).setLink(this.searchResultBotContext.get(i), this.foundContextBot, this.contextMedia, i != this.searchResultBotContext.size() - 1, z && i == 0, "gif".equals(this.searchingContextUsername));
        } else {
            ArrayList<TLObject> arrayList = this.searchResultUsernames;
            if (arrayList != null) {
                TLObject tLObject = arrayList.get(i);
                if (tLObject instanceof TLRPC$User) {
                    ((MentionCell) viewHolder.itemView).setUser((TLRPC$User) tLObject);
                } else if (tLObject instanceof TLRPC$Chat) {
                    ((MentionCell) viewHolder.itemView).setChat((TLRPC$Chat) tLObject);
                }
            } else {
                ArrayList<String> arrayList2 = this.searchResultHashtags;
                if (arrayList2 != null) {
                    ((MentionCell) viewHolder.itemView).setText(arrayList2.get(i));
                } else {
                    ArrayList<MediaDataController.KeywordResult> arrayList3 = this.searchResultSuggestions;
                    if (arrayList3 != null) {
                        ((MentionCell) viewHolder.itemView).setEmojiSuggestion(arrayList3.get(i));
                    } else {
                        ArrayList<String> arrayList4 = this.searchResultCommands;
                        if (arrayList4 != null) {
                            MentionCell mentionCell = (MentionCell) viewHolder.itemView;
                            String str = arrayList4.get(i);
                            String str2 = this.searchResultCommandsHelp.get(i);
                            ArrayList<TLRPC$User> arrayList5 = this.searchResultCommandsUsers;
                            mentionCell.setBotCommand(str, str2, arrayList5 != null ? arrayList5.get(i) : null);
                        }
                    }
                }
            }
            ((MentionCell) viewHolder.itemView).setDivider(false);
        }
    }

    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        TLRPC$User tLRPC$User;
        if (i != 2 || (tLRPC$User = this.foundContextBot) == null || !tLRPC$User.bot_inline_geo) {
            return;
        }
        if (iArr.length > 0 && iArr[0] == 0) {
            this.locationProvider.start();
        } else {
            onLocationUnavailable();
        }
    }

    public void doSomeStickersAction() {
        if (isStickers()) {
            if (this.mentionsStickersActionTracker == null) {
                AnonymousClass8 anonymousClass8 = new AnonymousClass8(this.currentAccount, this.dialog_id, this.threadMessageId);
                this.mentionsStickersActionTracker = anonymousClass8;
                anonymousClass8.checkVisibility();
            }
            this.mentionsStickersActionTracker.doSomeAction();
        }
    }

    /* renamed from: org.telegram.ui.Adapters.MentionsAdapter$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends EmojiView.ChooseStickerActionTracker {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass8(int i, long j, int i2) {
            super(i, j, i2);
            MentionsAdapter.this = r1;
        }

        @Override // org.telegram.ui.Components.EmojiView.ChooseStickerActionTracker
        public boolean isShown() {
            return MentionsAdapter.this.isStickers();
        }
    }

    private int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }
}