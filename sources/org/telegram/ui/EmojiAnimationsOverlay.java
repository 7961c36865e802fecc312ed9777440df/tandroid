package org.telegram.ui;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.EmojiData;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.Reactions.AnimatedEmojiEffect;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickerSetBulletinLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Stories.StoryReactionWidgetView;

/* loaded from: classes4.dex */
public class EmojiAnimationsOverlay implements NotificationCenter.NotificationCenterDelegate {
    private static final HashSet excludeEmojiFromPack;
    private static final HashSet supportedEmoji = new HashSet();
    private boolean attached;
    ChatActivity chatActivity;
    FrameLayout contentLayout;
    int currentAccount;
    long dialogId;
    Runnable hintRunnable;
    String lastTappedEmoji;
    RecyclerListView listView;
    private HashMap preloaded;
    Runnable sentInteractionsRunnable;
    TLRPC.TL_messages_stickerSet set;
    long threadMsgId;
    private final int ANIMATION_JSON_VERSION = 1;
    private final String INTERACTIONS_STICKER_PACK = "EmojiAnimations";
    boolean inited = false;
    HashMap emojiInteractionsStickersMap = new HashMap();
    HashMap lastAnimationIndex = new HashMap();
    Random random = new Random();
    int lastTappedMsgId = -1;
    long lastTappedTime = 0;
    ArrayList timeIntervals = new ArrayList();
    ArrayList animationIndexes = new ArrayList();
    final ArrayList drawingObjects = new ArrayList();

    public static class DrawingObject {
        TLRPC.Document document;
        public long documentId;
        private String fileName;
        public AnimatedEmojiEffect genericEffect;
        ImageReceiver imageReceiver;
        public boolean isMessageEffect;
        boolean isOut;
        public boolean isPremiumSticker;
        public boolean isReaction;
        public float lastH;
        public float lastW;
        public float lastX;
        public float lastY;
        int messageId;
        public float randomOffsetX;
        public float randomOffsetY;
        float removeProgress;
        boolean removing;
        public boolean viewFound;
        boolean wasPlayed;

        DrawingObject() {
            ImageReceiver imageReceiver = new ImageReceiver();
            this.imageReceiver = imageReceiver;
            imageReceiver.setAllowLoadingOnAttachedOnly(true);
            this.imageReceiver.setAllowDrawWhileCacheGenerating(true);
        }

        public float getProgress() {
            ImageLocation mediaLocation = this.imageReceiver.getMediaLocation();
            if (mediaLocation == null) {
                mediaLocation = this.imageReceiver.getImageLocation();
            }
            if (mediaLocation == null) {
                mediaLocation = this.imageReceiver.getThumbLocation();
            }
            if (mediaLocation == null) {
                return -1.0f;
            }
            if (this.fileName == null) {
                TLRPC.Document document = mediaLocation.document;
                this.fileName = document != null ? FileLoader.getAttachFileName(document, "tgs") : FileLoader.getAttachFileName(mediaLocation.location, "tgs");
            }
            if (this.fileName == null) {
                return -1.0f;
            }
            Float fileProgress = ImageLoader.getInstance().getFileProgress(this.fileName);
            if (fileProgress == null) {
                fileProgress = Float.valueOf(1.0f);
            }
            return (fileProgress.floatValue() * 0.55f) + 0.15f + (fileProgress.floatValue() * 0.3f);
        }
    }

    static {
        HashSet hashSet = new HashSet();
        excludeEmojiFromPack = hashSet;
        hashSet.add("0⃣");
        hashSet.add("1⃣");
        hashSet.add("2⃣");
        hashSet.add("3⃣");
        hashSet.add("4⃣");
        hashSet.add("5⃣");
        hashSet.add("6⃣");
        hashSet.add("7⃣");
        hashSet.add("8⃣");
        hashSet.add("9⃣");
    }

    public EmojiAnimationsOverlay(FrameLayout frameLayout, int i) {
        this.contentLayout = frameLayout;
        this.currentAccount = i;
    }

    public EmojiAnimationsOverlay(ChatActivity chatActivity, FrameLayout frameLayout, RecyclerListView recyclerListView, int i, long j, long j2) {
        this.chatActivity = chatActivity;
        this.contentLayout = frameLayout;
        this.listView = recyclerListView;
        this.currentAccount = i;
        this.dialogId = j;
        this.threadMsgId = j2;
    }

    private void clearSendingInfo() {
        this.lastTappedMsgId = 0;
        this.lastTappedEmoji = null;
        this.lastTappedTime = 0L;
        this.timeIntervals.clear();
        this.animationIndexes.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void findViewAndShowAnimation(int i, int i2) {
        ChatMessageCell chatMessageCell;
        ChatActivity chatActivity;
        if (this.attached) {
            int i3 = 0;
            while (true) {
                if (i3 >= this.listView.getChildCount()) {
                    chatMessageCell = null;
                    break;
                }
                View childAt = this.listView.getChildAt(i3);
                if (childAt instanceof ChatMessageCell) {
                    chatMessageCell = (ChatMessageCell) childAt;
                    String stickerEmoji = chatMessageCell.getMessageObject().getStickerEmoji();
                    if (stickerEmoji == null) {
                        stickerEmoji = chatMessageCell.getMessageObject().messageOwner.message;
                    }
                    if (chatMessageCell.getPhotoImage().hasNotThumb() && stickerEmoji != null && chatMessageCell.getMessageObject().getId() == i) {
                        break;
                    }
                }
                i3++;
            }
            if (chatMessageCell == null || (chatActivity = this.chatActivity) == null) {
                return;
            }
            chatActivity.restartSticker(chatMessageCell);
            if (!EmojiData.hasEmojiSupportVibration(chatMessageCell.getMessageObject().getStickerEmoji()) && !chatMessageCell.getMessageObject().isPremiumSticker() && !chatMessageCell.getMessageObject().isAnimatedAnimatedEmoji()) {
                try {
                    chatMessageCell.performHapticFeedback(3);
                } catch (Exception unused) {
                }
            }
            showAnimationForCell(chatMessageCell, i2, false, true);
        }
    }

    public static int getFilterWidth() {
        float min;
        float f;
        if (AndroidUtilities.isTablet()) {
            min = AndroidUtilities.getMinTabletSide();
            f = 0.4f;
        } else {
            Point point = AndroidUtilities.displaySize;
            min = Math.min(point.x, point.y);
            f = 0.5f;
        }
        return (int) ((((int) (min * f)) * 2.0f) / AndroidUtilities.density);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createDrawingObject$1(final MessageObject messageObject, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.EmojiAnimationsOverlay$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                EmojiAnimationsOverlay.this.lambda$createDrawingObject$0(tLObject, messageObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createDrawingObject$2() {
        sendCurrentTaps();
        this.sentInteractionsRunnable = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showStickerSetBulletin$3(MessageObject messageObject) {
        Activity parentActivity = this.chatActivity.getParentActivity();
        ChatActivity chatActivity = this.chatActivity;
        TLRPC.InputStickerSet inputStickerSet = messageObject.getInputStickerSet();
        ChatActivity chatActivity2 = this.chatActivity;
        StickersAlert stickersAlert = new StickersAlert(parentActivity, chatActivity, inputStickerSet, null, chatActivity2.chatActivityEnterView, chatActivity2.getResourceProvider(), false);
        stickersAlert.setCalcMandatoryInsets(this.chatActivity.isKeyboardVisible());
        this.chatActivity.showDialog(stickersAlert);
    }

    private void preloadAnimation(TLRPC.Document document) {
        if (document == null) {
            return;
        }
        HashMap hashMap = this.preloaded;
        if (hashMap == null || !hashMap.containsKey(Long.valueOf(document.id))) {
            if (this.preloaded == null) {
                this.preloaded = new HashMap();
            }
            this.preloaded.put(Long.valueOf(document.id), Boolean.TRUE);
            MediaDataController.getInstance(this.currentAccount).preloadImage(ImageLocation.getForDocument(document), 2);
        }
    }

    private void sendCurrentTaps() {
        if (this.lastTappedMsgId == 0) {
            return;
        }
        TLRPC.TL_sendMessageEmojiInteraction tL_sendMessageEmojiInteraction = new TLRPC.TL_sendMessageEmojiInteraction();
        tL_sendMessageEmojiInteraction.msg_id = this.lastTappedMsgId;
        tL_sendMessageEmojiInteraction.emoticon = this.lastTappedEmoji;
        tL_sendMessageEmojiInteraction.interaction = new TLRPC.TL_dataJSON();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("v", 1);
            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i < this.timeIntervals.size(); i++) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("i", ((Integer) this.animationIndexes.get(i)).intValue() + 1);
                jSONObject2.put("t", ((Long) this.timeIntervals.get(i)).longValue() / 1000.0f);
                jSONArray.put(i, jSONObject2);
            }
            jSONObject.put("a", jSONArray);
            tL_sendMessageEmojiInteraction.interaction.data = jSONObject.toString();
            TLRPC.TL_messages_setTyping tL_messages_setTyping = new TLRPC.TL_messages_setTyping();
            long j = this.threadMsgId;
            if (j != 0) {
                tL_messages_setTyping.top_msg_id = (int) j;
                tL_messages_setTyping.flags |= 1;
            }
            tL_messages_setTyping.action = tL_sendMessageEmojiInteraction;
            tL_messages_setTyping.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.dialogId);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_setTyping, null);
            clearSendingInfo();
        } catch (JSONException e) {
            clearSendingInfo();
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: showStickerSetBulletin, reason: merged with bridge method [inline-methods] */
    public void lambda$createDrawingObject$0(TLRPC.TL_messages_stickerSet tL_messages_stickerSet, final MessageObject messageObject) {
        if (this.chatActivity == null || MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked() || this.chatActivity.getParentActivity() == null) {
            return;
        }
        StickerSetBulletinLayout stickerSetBulletinLayout = new StickerSetBulletinLayout(this.contentLayout.getContext(), null, -1, messageObject.getDocument(), this.chatActivity.getResourceProvider());
        stickerSetBulletinLayout.titleTextView.setText(tL_messages_stickerSet.set.title);
        stickerSetBulletinLayout.subtitleTextView.setText(LocaleController.getString(R.string.PremiumStickerTooltip));
        Bulletin.UndoButton undoButton = new Bulletin.UndoButton(this.chatActivity.getParentActivity(), true, this.chatActivity.getResourceProvider());
        stickerSetBulletinLayout.setButton(undoButton);
        undoButton.setUndoAction(new Runnable() { // from class: org.telegram.ui.EmojiAnimationsOverlay$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                EmojiAnimationsOverlay.this.lambda$showStickerSetBulletin$3(messageObject);
            }
        });
        undoButton.setText(LocaleController.getString(R.string.ViewAction));
        Bulletin make = Bulletin.make(this.chatActivity, stickerSetBulletinLayout, 2750);
        make.hash = messageObject.getId();
        make.show();
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x002d, code lost:
    
        if (r8.charAt(r5) <= 57343) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0047, code lost:
    
        if (r8.charAt(r5) != 9794) goto L24;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v4, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r8v8, types: [java.lang.CharSequence] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String unwrapEmoji(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        int i = 0;
        String str2 = str;
        while (i < length) {
            if (i < length - 1) {
                if (str2.charAt(i) == 55356) {
                    int i2 = i + 1;
                    if (str2.charAt(i2) >= 57339) {
                    }
                }
                if (str2.charAt(i) == 8205) {
                    int i3 = i + 1;
                    if (str2.charAt(i3) != 9792) {
                    }
                    length -= 2;
                    str2 = TextUtils.concat(str2.subSequence(0, i), str2.subSequence(i + 2, str2.length()));
                    i--;
                    i++;
                    str2 = str2;
                }
            }
            if (str2.charAt(i) == 65039) {
                length--;
                str2 = TextUtils.concat(str2.subSequence(0, i), str2.subSequence(i + 1, str2.length()));
                i--;
                i++;
                str2 = str2;
            } else {
                i++;
                str2 = str2;
            }
        }
        return str2.toString();
    }

    public void cancelAllAnimations() {
        for (int i = 0; i < this.drawingObjects.size(); i++) {
            ((DrawingObject) this.drawingObjects.get(i)).removing = true;
        }
    }

    public void cancelHintRunnable() {
        Runnable runnable = this.hintRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        this.hintRunnable = null;
    }

    public boolean checkPosition(ChatMessageCell chatMessageCell, float f, int i) {
        float y = chatMessageCell.getY() + chatMessageCell.getPhotoImage().getCenterY();
        return y > f && y < ((float) i);
    }

    public void checkStickerPack() {
        if (this.inited) {
            return;
        }
        TLRPC.TL_messages_stickerSet stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByName("EmojiAnimations");
        this.set = stickerSetByName;
        if (stickerSetByName == null) {
            this.set = MediaDataController.getInstance(this.currentAccount).getStickerSetByEmojiOrName("EmojiAnimations");
        }
        if (this.set == null) {
            MediaDataController.getInstance(this.currentAccount).loadStickersByEmojiOrName("EmojiAnimations", false, true);
        }
        if (this.set != null) {
            HashMap hashMap = new HashMap();
            for (int i = 0; i < this.set.documents.size(); i++) {
                hashMap.put(Long.valueOf(this.set.documents.get(i).id), this.set.documents.get(i));
            }
            for (int i2 = 0; i2 < this.set.packs.size(); i2++) {
                TLRPC.TL_stickerPack tL_stickerPack = this.set.packs.get(i2);
                if (!excludeEmojiFromPack.contains(tL_stickerPack.emoticon) && tL_stickerPack.documents.size() > 0) {
                    supportedEmoji.add(tL_stickerPack.emoticon);
                    ArrayList arrayList = new ArrayList();
                    this.emojiInteractionsStickersMap.put(tL_stickerPack.emoticon, arrayList);
                    for (int i3 = 0; i3 < tL_stickerPack.documents.size(); i3++) {
                        arrayList.add((TLRPC.Document) hashMap.get(tL_stickerPack.documents.get(i3)));
                    }
                    if (tL_stickerPack.emoticon.equals("❤")) {
                        String[] strArr = {"🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎"};
                        for (int i4 = 0; i4 < 8; i4++) {
                            String str = strArr[i4];
                            supportedEmoji.add(str);
                            this.emojiInteractionsStickersMap.put(str, arrayList);
                        }
                    }
                }
            }
            this.inited = true;
        }
    }

    public void clear() {
        for (int i = 0; i < this.drawingObjects.size(); i++) {
            ((DrawingObject) this.drawingObjects.get(i)).imageReceiver.onDetachedFromWindow();
            if (((DrawingObject) this.drawingObjects.get(i)).genericEffect != null) {
                ((DrawingObject) this.drawingObjects.get(i)).genericEffect.removeView(this.contentLayout);
            }
        }
        this.drawingObjects.clear();
    }

    /* JADX WARN: Code restructure failed: missing block: B:193:0x01f0, code lost:
    
        if (r5 <= (r11.size() - 1)) goto L130;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean createDrawingObject(String str, int i, TLRPC.Document document, final MessageObject messageObject, int i2, final boolean z, boolean z2, float f, float f2, boolean z3) {
        final MessageObject messageObject2;
        int abs;
        TLRPC.Document document2;
        TLRPC.VideoSize premiumStickerAnimation;
        ArrayList arrayList;
        long valueOf;
        Runnable runnable;
        int i3 = i;
        int i4 = i2;
        boolean z4 = false;
        boolean z5 = messageObject != null && messageObject.isPremiumSticker();
        boolean z6 = (messageObject == null || messageObject.getEffect() == null) ? false : true;
        if (z6 || z5 || supportedEmoji.contains(str)) {
            ArrayList arrayList2 = (ArrayList) this.emojiInteractionsStickersMap.get(str);
            if (z6 || ((arrayList2 != null && !arrayList2.isEmpty()) || z5)) {
                int i5 = 0;
                int i6 = 0;
                int i7 = 0;
                while (i5 < this.drawingObjects.size()) {
                    if (((DrawingObject) this.drawingObjects.get(i5)).messageId == i3) {
                        i6++;
                        if (!z6 && (((DrawingObject) this.drawingObjects.get(i5)).imageReceiver.getLottieAnimation() == null || ((DrawingObject) this.drawingObjects.get(i5)).imageReceiver.getLottieAnimation().isGeneratingCache())) {
                            return z4;
                        }
                    }
                    if (((DrawingObject) this.drawingObjects.get(i5)).document != null && document != null && ((DrawingObject) this.drawingObjects.get(i5)).document.id == document.id) {
                        i7++;
                    }
                    i5++;
                    i3 = i;
                    z4 = false;
                }
                if (z && z5 && i6 > 0) {
                    if (Bulletin.getVisibleBulletin() != null && Bulletin.getVisibleBulletin().hash == messageObject.getId()) {
                        return false;
                    }
                    TLRPC.InputStickerSet inputStickerSet = messageObject.getInputStickerSet();
                    TLRPC.TL_messages_stickerSet stickerSetByName = inputStickerSet.short_name != null ? MediaDataController.getInstance(this.currentAccount).getStickerSetByName(inputStickerSet.short_name) : null;
                    if (stickerSetByName == null) {
                        stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetById(inputStickerSet.id);
                    }
                    if (stickerSetByName != null) {
                        lambda$createDrawingObject$0(stickerSetByName, messageObject);
                        return false;
                    }
                    TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet = new TLRPC.TL_messages_getStickerSet();
                    tL_messages_getStickerSet.stickerset = inputStickerSet;
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getStickerSet, new RequestDelegate() { // from class: org.telegram.ui.EmojiAnimationsOverlay$$ExternalSyntheticLambda0
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            EmojiAnimationsOverlay.this.lambda$createDrawingObject$1(messageObject, tLObject, tL_error);
                        }
                    });
                    return false;
                }
                if (i6 >= 4) {
                    return false;
                }
                if (z6) {
                    TLRPC.TL_availableEffect effect = messageObject.getEffect();
                    TLRPC.messages_AvailableEffects availableEffects = MessagesController.getInstance(this.currentAccount).getAvailableEffects();
                    if (availableEffects == null) {
                        return false;
                    }
                    long j = effect.effect_animation_id;
                    if (j == 0) {
                        j = effect.effect_sticker_id;
                    }
                    int i8 = 0;
                    while (true) {
                        if (i8 >= availableEffects.documents.size()) {
                            document2 = null;
                            break;
                        }
                        document2 = availableEffects.documents.get(i8);
                        if (document2 != null && document2.id == j) {
                            break;
                        }
                        i8++;
                    }
                    if (document2 == null) {
                        return false;
                    }
                    if (effect.effect_sticker_id != 0) {
                        TLRPC.VideoSize premiumStickerAnimation2 = MessageObject.getPremiumStickerAnimation(document2);
                        premiumStickerAnimation = premiumStickerAnimation2;
                        z5 = premiumStickerAnimation2 != null;
                        messageObject2 = messageObject;
                    } else {
                        messageObject2 = messageObject;
                        premiumStickerAnimation = null;
                    }
                } else if (z5) {
                    document2 = messageObject.getDocument();
                    premiumStickerAnimation = messageObject.getPremiumStickerAnimation();
                    messageObject2 = messageObject;
                } else {
                    messageObject2 = messageObject;
                    if (messageObject2 == null || !messageObject.isAnimatedAnimatedEmoji()) {
                        if (i4 >= 0) {
                        }
                        abs = Math.abs(this.random.nextInt()) % arrayList2.size();
                    } else {
                        if (i4 < 0 || i4 > arrayList2.size() - 1) {
                            ArrayList arrayList3 = new ArrayList();
                            for (int i9 = 0; i9 < arrayList2.size(); i9++) {
                                TLRPC.Document document3 = (TLRPC.Document) arrayList2.get(i9);
                                if (document3 != null) {
                                    HashMap hashMap = this.preloaded;
                                    Boolean bool = hashMap != null ? (Boolean) hashMap.get(Long.valueOf(document3.id)) : null;
                                    if (bool != null && bool.booleanValue()) {
                                        arrayList3.add(Integer.valueOf(i9));
                                    }
                                }
                            }
                            if (!arrayList3.isEmpty()) {
                                abs = ((Integer) arrayList3.get(Math.abs(this.random.nextInt()) % arrayList3.size())).intValue();
                            }
                            abs = Math.abs(this.random.nextInt()) % arrayList2.size();
                        }
                        abs = i4;
                    }
                    document2 = (TLRPC.Document) arrayList2.get(abs);
                    i4 = abs;
                    premiumStickerAnimation = null;
                }
                if (document2 == null && premiumStickerAnimation == null) {
                    return false;
                }
                final DrawingObject drawingObject = new DrawingObject();
                drawingObject.isPremiumSticker = z5;
                drawingObject.isMessageEffect = z6;
                if (!z6) {
                    drawingObject.randomOffsetX = (f / 4.0f) * ((this.random.nextInt() % 101) / 100.0f);
                    drawingObject.randomOffsetY = (f2 / 4.0f) * ((this.random.nextInt() % 101) / 100.0f);
                }
                drawingObject.messageId = i;
                drawingObject.document = document2;
                drawingObject.isOut = z3;
                drawingObject.imageReceiver.setAllowStartAnimation(true);
                drawingObject.imageReceiver.setAllowLottieVibration(z);
                if (premiumStickerAnimation == null) {
                    int filterWidth = getFilterWidth();
                    Integer num = (Integer) this.lastAnimationIndex.get(Long.valueOf(document2.id));
                    int intValue = (num == null ? 0 : num.intValue()) + 1;
                    this.lastAnimationIndex.put(Long.valueOf(document2.id), Integer.valueOf(intValue));
                    ImageLocation forDocument = ImageLocation.getForDocument(document2);
                    drawingObject.imageReceiver.setUniqKeyPrefix(intValue + "_" + drawingObject.messageId + "_");
                    drawingObject.imageReceiver.setImage(forDocument, filterWidth + "_" + filterWidth + "_pcache", null, "tgs", this.set, 1);
                    drawingObject.imageReceiver.setDelegate(new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.EmojiAnimationsOverlay.3
                        @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                        public void didSetImage(ImageReceiver imageReceiver, boolean z7, boolean z8, boolean z9) {
                            if (drawingObject.imageReceiver.getLottieAnimation() != null) {
                                drawingObject.imageReceiver.getLottieAnimation().setCurrentFrame(0, false, true);
                            }
                        }

                        @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                        public /* synthetic */ void didSetImageBitmap(int i10, String str2, Drawable drawable) {
                            ImageReceiver.ImageReceiverDelegate.-CC.$default$didSetImageBitmap(this, i10, str2, drawable);
                        }

                        @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                        public void onAnimationReady(ImageReceiver imageReceiver) {
                            MessageObject messageObject3;
                            if (!z || (messageObject3 = messageObject2) == null || !messageObject3.isAnimatedAnimatedEmoji() || imageReceiver.getLottieAnimation() == null || imageReceiver.getLottieAnimation().hasVibrationPattern()) {
                                return;
                            }
                            try {
                                EmojiAnimationsOverlay.this.contentLayout.performHapticFeedback(3, 1);
                            } catch (Exception unused) {
                            }
                        }
                    });
                    if (drawingObject.imageReceiver.getLottieAnimation() != null) {
                        drawingObject.imageReceiver.getLottieAnimation().setCurrentFrame(0, false, true);
                    }
                } else {
                    int filterWidth2 = getFilterWidth();
                    if (i7 > 0) {
                        Integer num2 = (Integer) this.lastAnimationIndex.get(Long.valueOf(document2.id));
                        int intValue2 = num2 == null ? 0 : num2.intValue();
                        this.lastAnimationIndex.put(Long.valueOf(document2.id), Integer.valueOf((intValue2 + 1) % 4));
                        drawingObject.imageReceiver.setUniqKeyPrefix(intValue2 + "_" + drawingObject.messageId + "_");
                    }
                    drawingObject.document = document2;
                    drawingObject.imageReceiver.setImage(ImageLocation.getForDocument(premiumStickerAnimation, document2), filterWidth2 + "_" + filterWidth2, null, "tgs", this.set, 1);
                }
                drawingObject.imageReceiver.setLayerNum(ConnectionsManager.DEFAULT_DATACENTER_ID);
                drawingObject.imageReceiver.setAutoRepeat(0);
                if (drawingObject.imageReceiver.getLottieAnimation() != null) {
                    if (drawingObject.isPremiumSticker) {
                        drawingObject.imageReceiver.getLottieAnimation().setCurrentFrame(0, false, true);
                    }
                    drawingObject.imageReceiver.getLottieAnimation().start();
                }
                this.drawingObjects.add(drawingObject);
                drawingObject.imageReceiver.onAttachedToWindow();
                drawingObject.imageReceiver.setParentView(this.contentLayout);
                this.contentLayout.invalidate();
                if (z && !z5 && UserConfig.getInstance(this.currentAccount).clientUserId != this.dialogId) {
                    int i10 = this.lastTappedMsgId;
                    if (i10 != 0 && i10 != i && (runnable = this.sentInteractionsRunnable) != null) {
                        AndroidUtilities.cancelRunOnUIThread(runnable);
                        this.sentInteractionsRunnable.run();
                    }
                    this.lastTappedMsgId = i;
                    this.lastTappedEmoji = str;
                    if (this.lastTappedTime == 0) {
                        this.lastTappedTime = System.currentTimeMillis();
                        this.timeIntervals.clear();
                        this.animationIndexes.clear();
                        arrayList = this.timeIntervals;
                        valueOf = 0L;
                    } else {
                        arrayList = this.timeIntervals;
                        valueOf = Long.valueOf(System.currentTimeMillis() - this.lastTappedTime);
                    }
                    arrayList.add(valueOf);
                    this.animationIndexes.add(Integer.valueOf(i4));
                    Runnable runnable2 = this.sentInteractionsRunnable;
                    if (runnable2 != null) {
                        AndroidUtilities.cancelRunOnUIThread(runnable2);
                        this.sentInteractionsRunnable = null;
                    }
                    Runnable runnable3 = new Runnable() { // from class: org.telegram.ui.EmojiAnimationsOverlay$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            EmojiAnimationsOverlay.this.lambda$createDrawingObject$2();
                        }
                    };
                    this.sentInteractionsRunnable = runnable3;
                    AndroidUtilities.runOnUIThread(runnable3, 500L);
                }
                if (!z2) {
                    return true;
                }
                MessagesController.getInstance(this.currentAccount).sendTyping(this.dialogId, this.threadMsgId, 11, str, 0);
                return true;
            }
        }
        return false;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        Integer printingStringType;
        if (i == NotificationCenter.diceStickersDidLoad) {
            if ("EmojiAnimations".equals((String) objArr[0])) {
                checkStickerPack();
                return;
            }
            return;
        }
        if (i != NotificationCenter.onEmojiInteractionsReceived) {
            if (i == NotificationCenter.updateInterfaces && (printingStringType = MessagesController.getInstance(this.currentAccount).getPrintingStringType(this.dialogId, this.threadMsgId)) != null && printingStringType.intValue() == 5) {
                cancelHintRunnable();
                return;
            }
            return;
        }
        if (this.chatActivity == null) {
            return;
        }
        long longValue = ((Long) objArr[0]).longValue();
        TLRPC.TL_sendMessageEmojiInteraction tL_sendMessageEmojiInteraction = (TLRPC.TL_sendMessageEmojiInteraction) objArr[1];
        if (longValue == this.dialogId && supportedEmoji.contains(tL_sendMessageEmojiInteraction.emoticon)) {
            final int i3 = tL_sendMessageEmojiInteraction.msg_id;
            if (tL_sendMessageEmojiInteraction.interaction.data != null) {
                try {
                    JSONArray jSONArray = new JSONObject(tL_sendMessageEmojiInteraction.interaction.data).getJSONArray("a");
                    for (int i4 = 0; i4 < jSONArray.length(); i4++) {
                        JSONObject jSONObject = jSONArray.getJSONObject(i4);
                        final int optInt = jSONObject.optInt("i", 1) - 1;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.EmojiAnimationsOverlay.1
                            @Override // java.lang.Runnable
                            public void run() {
                                EmojiAnimationsOverlay.this.findViewAndShowAnimation(i3, optInt);
                            }
                        }, (long) (jSONObject.optDouble("t", 0.0d) * 1000.0d));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:63:0x01c8, code lost:
    
        if (r4.isOut == false) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0220, code lost:
    
        r4.imageReceiver.draw(r17);
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0203, code lost:
    
        r17.save();
        r17.scale(-1.0f, 1.0f, r4.imageReceiver.getCenterX(), r4.imageReceiver.getCenterY());
        r4.imageReceiver.draw(r17);
        r17.restore();
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0201, code lost:
    
        if (r4.isOut == false) goto L89;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        ImageReceiver imageReceiver;
        float f;
        float f2;
        MessageObject messageObject;
        ImageReceiver imageReceiver2;
        float imageWidth;
        if (this.drawingObjects.isEmpty()) {
            return;
        }
        boolean z = false;
        int i = 0;
        while (i < this.drawingObjects.size()) {
            DrawingObject drawingObject = (DrawingObject) this.drawingObjects.get(i);
            if (this.chatActivity != null) {
                drawingObject.viewFound = z;
                int i2 = 0;
                while (true) {
                    if (i2 >= this.listView.getChildCount()) {
                        f2 = 0.0f;
                        break;
                    }
                    View childAt = this.listView.getChildAt(i2);
                    if (childAt instanceof ChatMessageCell) {
                        ChatMessageCell chatMessageCell = (ChatMessageCell) childAt;
                        messageObject = chatMessageCell.getMessageObject();
                        imageReceiver2 = chatMessageCell.getPhotoImage();
                    } else if (childAt instanceof ChatActionCell) {
                        ChatActionCell chatActionCell = (ChatActionCell) childAt;
                        messageObject = chatActionCell.getMessageObject();
                        imageReceiver2 = chatActionCell.getPhotoImage();
                    } else {
                        messageObject = null;
                        imageReceiver2 = null;
                    }
                    if (messageObject == null || messageObject.getId() != drawingObject.messageId) {
                        i2++;
                    } else {
                        drawingObject.viewFound = true;
                        float x = this.listView.getX() + childAt.getX();
                        float y = this.listView.getY() + childAt.getY();
                        f2 = childAt.getY();
                        drawingObject.lastW = imageReceiver2.getImageWidth();
                        drawingObject.lastH = imageReceiver2.getImageHeight();
                        if (drawingObject.isMessageEffect && (childAt instanceof ChatMessageCell)) {
                            ChatMessageCell chatMessageCell2 = (ChatMessageCell) childAt;
                            float filterWidth = (getFilterWidth() * AndroidUtilities.density) / 1.3f;
                            float f3 = filterWidth / 3.0f;
                            drawingObject.lastW = f3;
                            drawingObject.lastH = f3;
                            float f4 = filterWidth / 2.0f;
                            drawingObject.lastX = Utilities.clamp((x + chatMessageCell2.getTimeX()) - f4, AndroidUtilities.displaySize.x - filterWidth, 0.0f);
                            imageWidth = (y + chatMessageCell2.getTimeY()) - f4;
                        } else if (drawingObject.isPremiumSticker) {
                            drawingObject.lastX = x + imageReceiver2.getImageX();
                            imageWidth = y + imageReceiver2.getImageY();
                        } else {
                            float imageX = x + imageReceiver2.getImageX();
                            float imageY = y + imageReceiver2.getImageY();
                            float dp = imageX + (drawingObject.isOut ? ((-imageReceiver2.getImageWidth()) * 2.0f) + AndroidUtilities.dp(24.0f) : -AndroidUtilities.dp(24.0f));
                            imageWidth = imageY - imageReceiver2.getImageWidth();
                            drawingObject.lastX = dp;
                        }
                        drawingObject.lastY = imageWidth;
                    }
                }
                if (!drawingObject.viewFound || drawingObject.lastH + f2 < this.chatActivity.getChatListViewPadding() || f2 > this.listView.getMeasuredHeight() - this.chatActivity.blurredViewBottomOffset) {
                    drawingObject.removing = true;
                }
                if (drawingObject.isPremiumSticker) {
                    float f5 = drawingObject.lastH / 2.0f;
                    boolean z2 = ((float) this.listView.getMeasuredHeight()) - f2 <= f5;
                    boolean z3 = (f2 - this.chatActivity.getChatListViewPadding()) + f5 <= 0.0f;
                    if (z2 || z3) {
                        drawingObject.removing = true;
                    }
                }
                if (drawingObject.removing) {
                    float f6 = drawingObject.removeProgress;
                    if (f6 != 1.0f) {
                        float clamp = Utilities.clamp(f6 + 0.10666667f, 1.0f, 0.0f);
                        drawingObject.removeProgress = clamp;
                        drawingObject.imageReceiver.setAlpha(1.0f - clamp);
                        this.chatActivity.contentView.invalidate();
                    }
                }
            } else {
                layoutObject(drawingObject);
            }
            boolean z4 = !drawingObject.wasPlayed && drawingObject.removing;
            if (!z4) {
                if (!drawingObject.isPremiumSticker || drawingObject.isMessageEffect) {
                    AnimatedEmojiEffect animatedEmojiEffect = drawingObject.genericEffect;
                    if (animatedEmojiEffect != null) {
                        float f7 = drawingObject.lastX + drawingObject.randomOffsetX;
                        float f8 = drawingObject.lastY + drawingObject.randomOffsetY;
                        float f9 = drawingObject.lastW * 3.0f;
                        animatedEmojiEffect.setBounds((int) f7, (int) f8, (int) (f7 + f9), (int) (f8 + f9));
                        drawingObject.genericEffect.draw(canvas);
                    } else {
                        ImageReceiver imageReceiver3 = drawingObject.imageReceiver;
                        float f10 = drawingObject.lastX + drawingObject.randomOffsetX;
                        float f11 = drawingObject.lastY + drawingObject.randomOffsetY;
                        float f12 = drawingObject.lastW * 3.0f;
                        imageReceiver3.setImageCoords(f10, f11, f12, f12);
                    }
                } else {
                    float f13 = drawingObject.lastH;
                    float f14 = 1.49926f * f13;
                    float f15 = 0.0546875f * f14;
                    float f16 = ((drawingObject.lastY + (f13 / 2.0f)) - (f14 / 2.0f)) - (0.00279f * f14);
                    if (drawingObject.isOut) {
                        imageReceiver = drawingObject.imageReceiver;
                        f = ((drawingObject.lastX + drawingObject.lastW) - f14) + f15;
                    } else {
                        imageReceiver = drawingObject.imageReceiver;
                        f = drawingObject.lastX - f15;
                    }
                    imageReceiver.setImageCoords(f, f16, f14, f14);
                }
            }
            AnimatedEmojiEffect animatedEmojiEffect2 = drawingObject.genericEffect;
            boolean isDone = animatedEmojiEffect2 != null ? animatedEmojiEffect2.isDone() : drawingObject.wasPlayed && drawingObject.imageReceiver.getLottieAnimation() != null && drawingObject.imageReceiver.getLottieAnimation().getCurrentFrame() >= drawingObject.imageReceiver.getLottieAnimation().getFramesCount() + (-2);
            if (drawingObject.removeProgress == 1.0f || isDone || z4) {
                DrawingObject drawingObject2 = (DrawingObject) this.drawingObjects.remove(i);
                if (drawingObject.isPremiumSticker && drawingObject.imageReceiver.getLottieAnimation() != null) {
                    drawingObject2.imageReceiver.getLottieAnimation().setCurrentFrame(0, true, true);
                }
                drawingObject2.imageReceiver.onDetachedFromWindow();
                AnimatedEmojiEffect animatedEmojiEffect3 = drawingObject2.genericEffect;
                if (animatedEmojiEffect3 != null) {
                    animatedEmojiEffect3.removeView(this.contentLayout);
                }
                i--;
            } else if (drawingObject.imageReceiver.getLottieAnimation() != null && drawingObject.imageReceiver.getLottieAnimation().isRunning()) {
                drawingObject.wasPlayed = true;
            } else if (drawingObject.imageReceiver.getLottieAnimation() != null && !drawingObject.imageReceiver.getLottieAnimation().isRunning()) {
                drawingObject.imageReceiver.getLottieAnimation().setCurrentFrame(0, true);
                drawingObject.imageReceiver.getLottieAnimation().start();
            }
            i++;
            z = false;
        }
        if (this.drawingObjects.isEmpty()) {
            onAllEffectsEnd();
        }
        this.contentLayout.invalidate();
    }

    public float getProgress() {
        if (this.drawingObjects.isEmpty()) {
            return -1.0f;
        }
        return ((DrawingObject) this.drawingObjects.get(r0.size() - 1)).getProgress();
    }

    public boolean isIdle() {
        return this.drawingObjects.isEmpty();
    }

    protected void layoutObject(DrawingObject drawingObject) {
    }

    public void onAllEffectsEnd() {
    }

    public void onAttachedToWindow() {
        this.attached = true;
        checkStickerPack();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.diceStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.onEmojiInteractionsReceived);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        for (int i = 0; i < this.drawingObjects.size(); i++) {
            ((DrawingObject) this.drawingObjects.get(i)).imageReceiver.onAttachedToWindow();
            if (((DrawingObject) this.drawingObjects.get(i)).genericEffect != null) {
                ((DrawingObject) this.drawingObjects.get(i)).genericEffect.setView(this.contentLayout);
            }
        }
    }

    public void onDetachedFromWindow() {
        this.attached = false;
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.diceStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.onEmojiInteractionsReceived);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        for (int i = 0; i < this.drawingObjects.size(); i++) {
            ((DrawingObject) this.drawingObjects.get(i)).imageReceiver.onDetachedFromWindow();
            if (((DrawingObject) this.drawingObjects.get(i)).genericEffect != null) {
                ((DrawingObject) this.drawingObjects.get(i)).genericEffect.removeView(this.contentLayout);
            }
        }
        this.drawingObjects.clear();
    }

    public void onScrolled(int i) {
        for (int i2 = 0; i2 < this.drawingObjects.size(); i2++) {
            if (!((DrawingObject) this.drawingObjects.get(i2)).viewFound) {
                ((DrawingObject) this.drawingObjects.get(i2)).lastY -= i;
            }
        }
    }

    public boolean onTapItem(ChatMessageCell chatMessageCell, ChatActivity chatActivity, boolean z) {
        if (chatActivity.isSecretChat() || chatMessageCell.getMessageObject() == null || chatMessageCell.getMessageObject().getId() < 0) {
            return false;
        }
        if (!chatMessageCell.getMessageObject().isPremiumSticker() && chatActivity.currentUser == null) {
            return false;
        }
        boolean showAnimationForCell = showAnimationForCell(chatMessageCell, -1, z, false);
        if (z && showAnimationForCell && !EmojiData.hasEmojiSupportVibration(chatMessageCell.getMessageObject().getStickerEmoji()) && !chatMessageCell.getMessageObject().isPremiumSticker() && !chatMessageCell.getMessageObject().isAnimatedAnimatedEmoji()) {
            try {
                chatMessageCell.performHapticFeedback(3);
            } catch (Exception unused) {
            }
        }
        if (chatMessageCell.getMessageObject().isPremiumSticker() || chatMessageCell.getEffect() != null || (!z && chatMessageCell.getMessageObject().isAnimatedEmojiStickerSingle())) {
            chatMessageCell.getMessageObject().forcePlayEffect = false;
            chatMessageCell.getMessageObject().messageOwner.premiumEffectWasPlayed = true;
            chatActivity.getMessagesStorage().updateMessageCustomParams(this.dialogId, chatMessageCell.getMessageObject().messageOwner);
            return showAnimationForCell;
        }
        Integer printingStringType = MessagesController.getInstance(this.currentAccount).getPrintingStringType(this.dialogId, this.threadMsgId);
        if ((printingStringType == null || printingStringType.intValue() != 5) && this.hintRunnable == null && showAnimationForCell && ((Bulletin.getVisibleBulletin() == null || !Bulletin.getVisibleBulletin().isShowing()) && SharedConfig.emojiInteractionsHintCount > 0 && UserConfig.getInstance(this.currentAccount).getClientUserId() != chatActivity.currentUser.id)) {
            SharedConfig.updateEmojiInteractionsHintCount(SharedConfig.emojiInteractionsHintCount - 1);
            StickerSetBulletinLayout stickerSetBulletinLayout = new StickerSetBulletinLayout(chatActivity.getParentActivity(), null, -1, chatMessageCell.getMessageObject().isAnimatedAnimatedEmoji() ? chatMessageCell.getMessageObject().getDocument() : MediaDataController.getInstance(this.currentAccount).getEmojiAnimatedSticker(chatMessageCell.getMessageObject().getStickerEmoji()), chatActivity.getResourceProvider());
            stickerSetBulletinLayout.subtitleTextView.setVisibility(8);
            stickerSetBulletinLayout.titleTextView.setText(Emoji.replaceEmoji(AndroidUtilities.replaceTags(LocaleController.formatString("EmojiInteractionTapHint", R.string.EmojiInteractionTapHint, chatActivity.currentUser.first_name)), stickerSetBulletinLayout.titleTextView.getPaint().getFontMetricsInt(), false));
            stickerSetBulletinLayout.titleTextView.setTypeface(null);
            stickerSetBulletinLayout.titleTextView.setMaxLines(3);
            stickerSetBulletinLayout.titleTextView.setSingleLine(false);
            final Bulletin make = Bulletin.make(chatActivity, stickerSetBulletinLayout, 2750);
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.EmojiAnimationsOverlay.2
                @Override // java.lang.Runnable
                public void run() {
                    make.show();
                    EmojiAnimationsOverlay.this.hintRunnable = null;
                }
            };
            this.hintRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable, 1500L);
        }
        return showAnimationForCell;
    }

    public void preload(ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        ArrayList arrayList;
        String str = visibleReaction.emojicon;
        if (str == null) {
            str = MessageObject.findAnimatedEmojiEmoticon(AnimatedEmojiDrawable.findDocument(this.currentAccount, visibleReaction.documentId));
        }
        if (str == null || (arrayList = (ArrayList) this.emojiInteractionsStickersMap.get(str)) == null || arrayList.isEmpty()) {
            return;
        }
        int min = Math.min(1, arrayList.size());
        for (int i = 0; i < min; i++) {
            preloadAnimation((TLRPC.Document) arrayList.get(i));
        }
    }

    public void preloadAnimation(ChatMessageCell chatMessageCell) {
        ArrayList arrayList;
        MessageObject messageObject = chatMessageCell.getMessageObject();
        if (messageObject.isPremiumSticker()) {
            return;
        }
        String stickerEmoji = messageObject.getStickerEmoji();
        if (stickerEmoji == null) {
            stickerEmoji = messageObject.messageOwner.message;
        }
        String unwrapEmoji = unwrapEmoji(stickerEmoji);
        if (!supportedEmoji.contains(unwrapEmoji) || (arrayList = (ArrayList) this.emojiInteractionsStickersMap.get(unwrapEmoji)) == null || arrayList.isEmpty()) {
            return;
        }
        int min = Math.min(1, arrayList.size());
        for (int i = 0; i < min; i++) {
            preloadAnimation((TLRPC.Document) arrayList.get(i));
        }
    }

    public void setAccount(int i) {
        this.currentAccount = i;
    }

    public boolean showAnimationForActionCell(ChatActionCell chatActionCell, TLRPC.Document document, TLRPC.VideoSize videoSize) {
        if (this.drawingObjects.size() > 12 || !chatActionCell.getPhotoImage().hasNotThumb()) {
            return false;
        }
        float imageHeight = chatActionCell.getPhotoImage().getImageHeight();
        float imageWidth = chatActionCell.getPhotoImage().getImageWidth();
        if (imageHeight <= 0.0f || imageWidth <= 0.0f) {
            return false;
        }
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < this.drawingObjects.size(); i3++) {
            if (((DrawingObject) this.drawingObjects.get(i3)).messageId == chatActionCell.getMessageObject().getId()) {
                i++;
                if (((DrawingObject) this.drawingObjects.get(i3)).imageReceiver.getLottieAnimation() == null || ((DrawingObject) this.drawingObjects.get(i3)).imageReceiver.getLottieAnimation().isGeneratingCache()) {
                    return false;
                }
            }
            if (((DrawingObject) this.drawingObjects.get(i3)).document != null && document != null && ((DrawingObject) this.drawingObjects.get(i3)).document.id == document.id) {
                i2++;
            }
        }
        if (i >= 4) {
            return false;
        }
        DrawingObject drawingObject = new DrawingObject();
        drawingObject.isPremiumSticker = true;
        if (!drawingObject.isMessageEffect) {
            drawingObject.randomOffsetX = (imageWidth / 4.0f) * ((this.random.nextInt() % 101) / 100.0f);
            drawingObject.randomOffsetY = (imageHeight / 4.0f) * ((this.random.nextInt() % 101) / 100.0f);
        }
        drawingObject.messageId = chatActionCell.getMessageObject().getId();
        drawingObject.isOut = true;
        drawingObject.imageReceiver.setAllowStartAnimation(true);
        int filterWidth = getFilterWidth();
        if (i2 > 0) {
            Integer num = (Integer) this.lastAnimationIndex.get(Long.valueOf(document.id));
            int intValue = num == null ? 0 : num.intValue();
            this.lastAnimationIndex.put(Long.valueOf(document.id), Integer.valueOf((intValue + 1) % 4));
            drawingObject.imageReceiver.setUniqKeyPrefix(intValue + "_" + drawingObject.messageId + "_");
        }
        drawingObject.document = document;
        drawingObject.imageReceiver.setImage(ImageLocation.getForDocument(videoSize, document), filterWidth + "_" + filterWidth, null, "tgs", this.set, 1);
        drawingObject.imageReceiver.setLayerNum(ConnectionsManager.DEFAULT_DATACENTER_ID);
        drawingObject.imageReceiver.setAutoRepeat(0);
        if (drawingObject.imageReceiver.getLottieAnimation() != null) {
            if (drawingObject.isPremiumSticker) {
                drawingObject.imageReceiver.getLottieAnimation().setCurrentFrame(0, false, true);
            }
            drawingObject.imageReceiver.getLottieAnimation().start();
        }
        this.drawingObjects.add(drawingObject);
        if (this.attached) {
            drawingObject.imageReceiver.onAttachedToWindow();
            drawingObject.imageReceiver.setParentView(this.contentLayout);
        }
        this.contentLayout.invalidate();
        return true;
    }

    public boolean showAnimationForCell(ChatMessageCell chatMessageCell, int i, boolean z, boolean z2) {
        if (chatMessageCell == null || this.drawingObjects.size() > 12) {
            return false;
        }
        MessageObject messageObject = chatMessageCell.getMessageObject();
        if (chatMessageCell.getEffect() == null && !chatMessageCell.getPhotoImage().hasNotThumb()) {
            return false;
        }
        String stickerEmoji = messageObject.getStickerEmoji();
        if (stickerEmoji == null) {
            stickerEmoji = messageObject.messageOwner.message;
        }
        if (chatMessageCell.getEffect() == null && stickerEmoji == null) {
            return false;
        }
        float imageHeight = chatMessageCell.getPhotoImage().getImageHeight();
        float imageWidth = chatMessageCell.getPhotoImage().getImageWidth();
        if (chatMessageCell.getEffect() != null || (imageHeight > 0.0f && imageWidth > 0.0f)) {
            return createDrawingObject(unwrapEmoji(stickerEmoji), chatMessageCell.getMessageObject().getId(), chatMessageCell.getMessageObject().getDocument(), messageObject, i, z, z2, imageWidth, imageHeight, chatMessageCell.getMessageObject().isOutOwner());
        }
        return false;
    }

    public boolean showAnimationForWidget(StoryReactionWidgetView storyReactionWidgetView) {
        float f;
        float f2;
        boolean z;
        int i;
        float f3;
        boolean z2;
        float f4;
        float f5;
        if (this.drawingObjects.size() > 12) {
            return false;
        }
        ReactionsLayoutInBubble.VisibleReaction fromTL = ReactionsLayoutInBubble.VisibleReaction.fromTL(storyReactionWidgetView.mediaArea.reaction);
        String str = fromTL.emojicon;
        if (str == null) {
            str = MessageObject.findAnimatedEmojiEmoticon(AnimatedEmojiDrawable.findDocument(this.currentAccount, fromTL.documentId));
        }
        float measuredHeight = storyReactionWidgetView.getMeasuredHeight();
        float measuredWidth = storyReactionWidgetView.getMeasuredWidth();
        View view = (View) storyReactionWidgetView.getParent();
        if (measuredWidth > view.getWidth() * 0.5f) {
            f2 = view.getWidth() * 0.4f;
            f = f2;
        } else {
            f = measuredHeight;
            f2 = measuredWidth;
        }
        String unwrapEmoji = unwrapEmoji(str);
        int hashCode = storyReactionWidgetView.hashCode();
        boolean z3 = storyReactionWidgetView.getTranslationX() > ((float) this.contentLayout.getMeasuredWidth()) / 2.0f;
        if (fromTL.emojicon != null) {
            z = z3;
            z2 = true;
            i = hashCode;
            float f6 = f2;
            if (createDrawingObject(unwrapEmoji, hashCode, null, null, -1, false, false, f2, f, z)) {
                if (!this.drawingObjects.isEmpty()) {
                    ArrayList arrayList = this.drawingObjects;
                    DrawingObject drawingObject = (DrawingObject) arrayList.get(arrayList.size() - 1);
                    drawingObject.isReaction = true;
                    drawingObject.lastH = f;
                    drawingObject.lastW = f6;
                    drawingObject.lastX = storyReactionWidgetView.getTranslationX() - (drawingObject.lastW / 2.0f);
                    float translationY = storyReactionWidgetView.getTranslationY();
                    float f7 = drawingObject.lastW;
                    drawingObject.lastY = translationY - (1.5f * f7);
                    if (drawingObject.isOut) {
                        f4 = drawingObject.lastX;
                        f5 = (-f7) * 1.8f;
                    } else {
                        f4 = drawingObject.lastX;
                        f5 = (-f7) * 0.2f;
                    }
                    drawingObject.lastX = f4 + f5;
                }
                return true;
            }
            f3 = f6;
        } else {
            z = z3;
            i = hashCode;
            f3 = f2;
            z2 = true;
        }
        if (fromTL.documentId == 0 || storyReactionWidgetView.getAnimatedEmojiDrawable() == null) {
            return false;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.drawingObjects.size(); i3++) {
            if (((DrawingObject) this.drawingObjects.get(i3)).documentId == fromTL.documentId) {
                i2++;
            }
        }
        if (i2 >= 4) {
            return false;
        }
        DrawingObject drawingObject2 = new DrawingObject();
        drawingObject2.genericEffect = AnimatedEmojiEffect.createFrom(storyReactionWidgetView.getAnimatedEmojiDrawable(), z2, z2);
        if (!drawingObject2.isMessageEffect) {
            drawingObject2.randomOffsetX = (f3 / 4.0f) * ((this.random.nextInt() % 101) / 100.0f);
            drawingObject2.randomOffsetY = (f / 4.0f) * ((this.random.nextInt() % 101) / 100.0f);
        }
        drawingObject2.messageId = i;
        drawingObject2.document = null;
        drawingObject2.documentId = fromTL.documentId;
        drawingObject2.isOut = z;
        drawingObject2.isReaction = z2;
        drawingObject2.lastH = f;
        drawingObject2.lastW = f3;
        drawingObject2.lastX = storyReactionWidgetView.getTranslationX() - (drawingObject2.lastW / 2.0f);
        float translationY2 = storyReactionWidgetView.getTranslationY();
        float f8 = drawingObject2.lastW;
        drawingObject2.lastY = translationY2 - (1.5f * f8);
        drawingObject2.lastX += (-f8) * 1.8f;
        if (this.attached) {
            drawingObject2.genericEffect.setView(this.contentLayout);
        }
        this.drawingObjects.add(drawingObject2);
        return z2;
    }

    public boolean supports(String str) {
        return this.emojiInteractionsStickersMap.containsKey(unwrapEmoji(str));
    }
}
