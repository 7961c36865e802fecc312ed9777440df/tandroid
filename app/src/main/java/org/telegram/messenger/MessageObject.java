package org.telegram.messenger;

import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Base64;
import android.view.View;
import androidx.collection.LongSparseArray;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.ringtone.RingtoneDataStore;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$ChannelAdminLogEventAction;
import org.telegram.tgnet.TLRPC$ChannelLocation;
import org.telegram.tgnet.TLRPC$ChannelParticipant;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatInvite;
import org.telegram.tgnet.TLRPC$DecryptedMessageAction;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$ExportedChatInvite;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$KeyboardButton;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$MessageFwdHeader;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$MessageReplies;
import org.telegram.tgnet.TLRPC$Page;
import org.telegram.tgnet.TLRPC$PageBlock;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$PollResults;
import org.telegram.tgnet.TLRPC$SecureValueType;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEvent;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeAbout;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeLinkedChat;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeLocation;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangePhoto;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeStickerSet;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeTheme;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeTitle;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeUsername;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionDefaultBannedRights;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionDeleteMessage;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionDiscardGroupCall;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionEditMessage;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionExportedInviteDelete;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionExportedInviteEdit;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionExportedInviteRevoke;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantInvite;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantJoin;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantLeave;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantMute;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantToggleBan;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantUnmute;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantVolume;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionSendMessage;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionStartGroupCall;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionStopPoll;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleGroupCallSetting;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleInvites;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleNoForwards;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionTogglePreHistoryHidden;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleSignatures;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleSlowMode;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionUpdatePinned;
import org.telegram.tgnet.TLRPC$TL_channelLocation;
import org.telegram.tgnet.TLRPC$TL_channelLocationEmpty;
import org.telegram.tgnet.TLRPC$TL_channelParticipant;
import org.telegram.tgnet.TLRPC$TL_channelParticipantAdmin;
import org.telegram.tgnet.TLRPC$TL_channelParticipantCreator;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_chatBannedRights;
import org.telegram.tgnet.TLRPC$TL_chatInviteExported;
import org.telegram.tgnet.TLRPC$TL_chatInvitePublicJoinRequests;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionScreenshotMessages;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionSetMessageTTL;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAnimated;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_documentAttributeHasStickers;
import org.telegram.tgnet.TLRPC$TL_documentAttributeImageSize;
import org.telegram.tgnet.TLRPC$TL_documentAttributeSticker;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_documentEmpty;
import org.telegram.tgnet.TLRPC$TL_documentEncrypted;
import org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC$TL_game;
import org.telegram.tgnet.TLRPC$TL_inputMessageEntityMentionName;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetEmpty;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetShortName;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonBuy;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonRow;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageActionBotAllowed;
import org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate;
import org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom;
import org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser;
import org.telegram.tgnet.TLRPC$TL_messageActionChatCreate;
import org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto;
import org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser;
import org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto;
import org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle;
import org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink;
import org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest;
import org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo;
import org.telegram.tgnet.TLRPC$TL_messageActionContactSignUp;
import org.telegram.tgnet.TLRPC$TL_messageActionCreatedBroadcastList;
import org.telegram.tgnet.TLRPC$TL_messageActionCustomAction;
import org.telegram.tgnet.TLRPC$TL_messageActionEmpty;
import org.telegram.tgnet.TLRPC$TL_messageActionGameScore;
import org.telegram.tgnet.TLRPC$TL_messageActionGeoProximityReached;
import org.telegram.tgnet.TLRPC$TL_messageActionGroupCall;
import org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled;
import org.telegram.tgnet.TLRPC$TL_messageActionHistoryClear;
import org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall;
import org.telegram.tgnet.TLRPC$TL_messageActionLoginUnknownLocation;
import org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent;
import org.telegram.tgnet.TLRPC$TL_messageActionPhoneCall;
import org.telegram.tgnet.TLRPC$TL_messageActionPinMessage;
import org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken;
import org.telegram.tgnet.TLRPC$TL_messageActionSecureValuesSent;
import org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme;
import org.telegram.tgnet.TLRPC$TL_messageActionSetMessagesTTL;
import org.telegram.tgnet.TLRPC$TL_messageActionTTLChange;
import org.telegram.tgnet.TLRPC$TL_messageActionUserJoined;
import org.telegram.tgnet.TLRPC$TL_messageActionUserUpdatedPhoto;
import org.telegram.tgnet.TLRPC$TL_messageActionWebViewDataSent;
import org.telegram.tgnet.TLRPC$TL_messageEmpty;
import org.telegram.tgnet.TLRPC$TL_messageEncryptedAction;
import org.telegram.tgnet.TLRPC$TL_messageEntityBankCard;
import org.telegram.tgnet.TLRPC$TL_messageEntityBlockquote;
import org.telegram.tgnet.TLRPC$TL_messageEntityBold;
import org.telegram.tgnet.TLRPC$TL_messageEntityBotCommand;
import org.telegram.tgnet.TLRPC$TL_messageEntityCashtag;
import org.telegram.tgnet.TLRPC$TL_messageEntityCode;
import org.telegram.tgnet.TLRPC$TL_messageEntityEmail;
import org.telegram.tgnet.TLRPC$TL_messageEntityHashtag;
import org.telegram.tgnet.TLRPC$TL_messageEntityItalic;
import org.telegram.tgnet.TLRPC$TL_messageEntityMention;
import org.telegram.tgnet.TLRPC$TL_messageEntityMentionName;
import org.telegram.tgnet.TLRPC$TL_messageEntityPhone;
import org.telegram.tgnet.TLRPC$TL_messageEntityPre;
import org.telegram.tgnet.TLRPC$TL_messageEntitySpoiler;
import org.telegram.tgnet.TLRPC$TL_messageEntityStrike;
import org.telegram.tgnet.TLRPC$TL_messageEntityTextUrl;
import org.telegram.tgnet.TLRPC$TL_messageEntityUnderline;
import org.telegram.tgnet.TLRPC$TL_messageEntityUrl;
import org.telegram.tgnet.TLRPC$TL_messageForwarded_old;
import org.telegram.tgnet.TLRPC$TL_messageForwarded_old2;
import org.telegram.tgnet.TLRPC$TL_messageMediaContact;
import org.telegram.tgnet.TLRPC$TL_messageMediaDice;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument_layer68;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument_layer74;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument_old;
import org.telegram.tgnet.TLRPC$TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC$TL_messageMediaGame;
import org.telegram.tgnet.TLRPC$TL_messageMediaGeo;
import org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive;
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto_layer68;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto_layer74;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto_old;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC$TL_messageMediaUnsupported;
import org.telegram.tgnet.TLRPC$TL_messageMediaVenue;
import org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC$TL_messagePeerReaction;
import org.telegram.tgnet.TLRPC$TL_messageReactions;
import org.telegram.tgnet.TLRPC$TL_messageReplyHeader;
import org.telegram.tgnet.TLRPC$TL_messageService;
import org.telegram.tgnet.TLRPC$TL_message_old;
import org.telegram.tgnet.TLRPC$TL_message_old2;
import org.telegram.tgnet.TLRPC$TL_message_old3;
import org.telegram.tgnet.TLRPC$TL_message_old4;
import org.telegram.tgnet.TLRPC$TL_message_secret;
import org.telegram.tgnet.TLRPC$TL_pageBlockCollage;
import org.telegram.tgnet.TLRPC$TL_pageBlockPhoto;
import org.telegram.tgnet.TLRPC$TL_pageBlockSlideshow;
import org.telegram.tgnet.TLRPC$TL_pageBlockVideo;
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_peerChat;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_phoneCallDiscardReasonBusy;
import org.telegram.tgnet.TLRPC$TL_phoneCallDiscardReasonMissed;
import org.telegram.tgnet.TLRPC$TL_photo;
import org.telegram.tgnet.TLRPC$TL_photoCachedSize;
import org.telegram.tgnet.TLRPC$TL_photoEmpty;
import org.telegram.tgnet.TLRPC$TL_photoSizeEmpty;
import org.telegram.tgnet.TLRPC$TL_photoStrippedSize;
import org.telegram.tgnet.TLRPC$TL_pollAnswer;
import org.telegram.tgnet.TLRPC$TL_pollAnswerVoters;
import org.telegram.tgnet.TLRPC$TL_reactionCount;
import org.telegram.tgnet.TLRPC$TL_replyInlineMarkup;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeAddress;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeBankStatement;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeDriverLicense;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeEmail;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeIdentityCard;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeInternalPassport;
import org.telegram.tgnet.TLRPC$TL_secureValueTypePassport;
import org.telegram.tgnet.TLRPC$TL_secureValueTypePassportRegistration;
import org.telegram.tgnet.TLRPC$TL_secureValueTypePersonalDetails;
import org.telegram.tgnet.TLRPC$TL_secureValueTypePhone;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeRentalAgreement;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeTemporaryRegistration;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeUtilityBill;
import org.telegram.tgnet.TLRPC$TL_webPage;
import org.telegram.tgnet.TLRPC$TL_webPageAttributeTheme;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.tgnet.TLRPC$WebDocument;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.TextStyleSpan;
import org.telegram.ui.Components.TranscribeButton;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanBotCommand;
import org.telegram.ui.Components.URLSpanBrowser;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.URLSpanNoUnderlineBold;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
/* loaded from: classes.dex */
public class MessageObject {
    private static final int LINES_PER_BLOCK = 10;
    public static final int MESSAGE_SEND_STATE_EDITING = 3;
    public static final int MESSAGE_SEND_STATE_SENDING = 1;
    public static final int MESSAGE_SEND_STATE_SEND_ERROR = 2;
    public static final int MESSAGE_SEND_STATE_SENT = 0;
    public static final int POSITION_FLAG_BOTTOM = 8;
    public static final int POSITION_FLAG_LEFT = 1;
    public static final int POSITION_FLAG_RIGHT = 2;
    public static final int POSITION_FLAG_TOP = 4;
    public static final int TYPE_ANIMATED_STICKER = 15;
    public static final int TYPE_GEO = 4;
    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_POLL = 17;
    public static final int TYPE_ROUND_VIDEO = 5;
    public static final int TYPE_STICKER = 13;
    public static final int TYPE_VIDEO = 3;
    static final String[] excludeWords = {" vs. ", " vs ", " versus ", " ft. ", " ft ", " featuring ", " feat. ", " feat ", " presents ", " pres. ", " pres ", " and ", " & ", " . "};
    public static Pattern instagramUrlPattern;
    public static Pattern urlPattern;
    public static Pattern videoTimeUrlPattern;
    public boolean animateComments;
    public boolean attachPathExists;
    public int audioPlayerDuration;
    public float audioProgress;
    public int audioProgressMs;
    public int audioProgressSec;
    public StringBuilder botButtonsLayout;
    public String botStartParam;
    public float bufferedProgress;
    public Boolean cachedIsSupergroup;
    public boolean cancelEditing;
    public CharSequence caption;
    public ArrayList<TLRPC$TL_pollAnswer> checkedVotes;
    public int contentType;
    public int currentAccount;
    public TLRPC$TL_channelAdminLogEvent currentEvent;
    public Drawable customAvatarDrawable;
    public String customName;
    public String customReplyName;
    public String dateKey;
    public boolean deleted;
    public CharSequence editingMessage;
    public ArrayList<TLRPC$MessageEntity> editingMessageEntities;
    public boolean editingMessageSearchWebPage;
    public TLRPC$Document emojiAnimatedSticker;
    public String emojiAnimatedStickerColor;
    private int emojiOnlyCount;
    public long eventId;
    public boolean forcePlayEffect;
    public float forceSeekTo;
    public boolean forceUpdate;
    private float generatedWithDensity;
    private int generatedWithMinSize;
    public float gifState;
    public boolean hadAnimationNotReadyLoading;
    public boolean hasRtl;
    public boolean hideSendersName;
    public ArrayList<String> highlightedWords;
    public boolean isDateObject;
    public boolean isDownloadingFile;
    public boolean isReactionPush;
    public boolean isRestrictedMessage;
    private int isRoundVideoCached;
    public boolean isSpoilersRevealed;
    public int lastLineWidth;
    private boolean layoutCreated;
    public int linesCount;
    public CharSequence linkDescription;
    public long loadedFileSize;
    public boolean loadingCancelled;
    public boolean localChannel;
    public boolean localEdit;
    public long localGroupId;
    public String localName;
    public long localSentGroupId;
    public boolean localSupergroup;
    public int localType;
    public String localUserName;
    public boolean mediaExists;
    public ImageLocation mediaSmallThumb;
    public ImageLocation mediaThumb;
    public TLRPC$Message messageOwner;
    public CharSequence messageText;
    public String messageTrimmedToHighlight;
    public String monthKey;
    public int parentWidth;
    public SvgHelper.SvgDrawable pathThumb;
    public ArrayList<TLRPC$PhotoSize> photoThumbs;
    public ArrayList<TLRPC$PhotoSize> photoThumbs2;
    public TLObject photoThumbsObject;
    public TLObject photoThumbsObject2;
    public long pollLastCheckTime;
    public boolean pollVisibleOnScreen;
    public boolean preview;
    public String previousAttachPath;
    public TLRPC$MessageMedia previousMedia;
    public String previousMessage;
    public ArrayList<TLRPC$MessageEntity> previousMessageEntities;
    public boolean putInDownloadsStore;
    public boolean reactionsChanged;
    public long reactionsLastCheckTime;
    public MessageObject replyMessageObject;
    public boolean resendAsIs;
    public boolean scheduled;
    public SendAnimationData sendAnimationData;
    public TLRPC$Peer sendAsPeer;
    public boolean shouldRemoveVideoEditedInfo;
    public int sponsoredChannelPost;
    public TLRPC$ChatInvite sponsoredChatInvite;
    public String sponsoredChatInviteHash;
    public byte[] sponsoredId;
    public int stableId;
    public BitmapDrawable strippedThumb;
    public int textHeight;
    public ArrayList<TextLayoutBlock> textLayoutBlocks;
    public int textWidth;
    public float textXOffset;
    public int type;
    public boolean useCustomPhoto;
    public CharSequence vCardData;
    public VideoEditedInfo videoEditedInfo;
    public AtomicReference<WeakReference<View>> viewRef;
    public boolean viewsReloaded;
    public int wantedBotKeyboardWidth;
    public boolean wasJustSent;
    public boolean wasUnread;

    /* loaded from: classes.dex */
    public static class SendAnimationData {
        public float currentScale;
        public float currentX;
        public float currentY;
        public float height;
        public float timeAlpha;
        public float width;
        public float x;
        public float y;
    }

    public void checkForScam() {
    }

    public static boolean hasUnreadReactions(TLRPC$Message tLRPC$Message) {
        if (tLRPC$Message == null) {
            return false;
        }
        return hasUnreadReactions(tLRPC$Message.reactions);
    }

    public static boolean hasUnreadReactions(TLRPC$TL_messageReactions tLRPC$TL_messageReactions) {
        if (tLRPC$TL_messageReactions == null) {
            return false;
        }
        for (int i = 0; i < tLRPC$TL_messageReactions.recent_reactions.size(); i++) {
            if (tLRPC$TL_messageReactions.recent_reactions.get(i).unread) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPremiumSticker(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null && tLRPC$Document.thumbs != null) {
            for (int i = 0; i < tLRPC$Document.video_thumbs.size(); i++) {
                if ("f".equals(tLRPC$Document.video_thumbs.get(i).type)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getEmojiOnlyCount() {
        return this.emojiOnlyCount;
    }

    public boolean shouldDrawReactionsInLayout() {
        return getDialogId() < 0;
    }

    public TLRPC$TL_messagePeerReaction getRandomUnreadReaction() {
        ArrayList<TLRPC$TL_messagePeerReaction> arrayList;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions = this.messageOwner.reactions;
        if (tLRPC$TL_messageReactions == null || (arrayList = tLRPC$TL_messageReactions.recent_reactions) == null || arrayList.isEmpty()) {
            return null;
        }
        return this.messageOwner.reactions.recent_reactions.get(0);
    }

    public void markReactionsAsRead() {
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions = this.messageOwner.reactions;
        if (tLRPC$TL_messageReactions == null || tLRPC$TL_messageReactions.recent_reactions == null) {
            return;
        }
        boolean z = false;
        for (int i = 0; i < this.messageOwner.reactions.recent_reactions.size(); i++) {
            if (this.messageOwner.reactions.recent_reactions.get(i).unread) {
                this.messageOwner.reactions.recent_reactions.get(i).unread = false;
                z = true;
            }
        }
        if (!z) {
            return;
        }
        MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
        TLRPC$Message tLRPC$Message = this.messageOwner;
        messagesStorage.markMessageReactionsAsRead(tLRPC$Message.dialog_id, tLRPC$Message.id, true);
    }

    public boolean isPremiumSticker() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        if (tLRPC$MessageMedia == null || !tLRPC$MessageMedia.nopremium) {
            return isPremiumSticker(getDocument());
        }
        return false;
    }

    public TLRPC$VideoSize getPremiumStickerAnimation() {
        return getPremiumStickerAnimation(getDocument());
    }

    public static TLRPC$VideoSize getPremiumStickerAnimation(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null && tLRPC$Document.thumbs != null) {
            for (int i = 0; i < tLRPC$Document.video_thumbs.size(); i++) {
                if ("f".equals(tLRPC$Document.video_thumbs.get(i).type)) {
                    return tLRPC$Document.video_thumbs.get(i);
                }
            }
        }
        return null;
    }

    /* loaded from: classes.dex */
    public static class VCardData {
        private String company;
        private ArrayList<String> emails = new ArrayList<>();
        private ArrayList<String> phones = new ArrayList<>();

        public static CharSequence parse(String str) {
            byte[] decodeQuotedPrintable;
            try {
                BufferedReader bufferedReader = new BufferedReader(new StringReader(str));
                boolean z = false;
                VCardData vCardData = null;
                String str2 = null;
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        if (!readLine.startsWith("PHOTO")) {
                            if (readLine.indexOf(58) >= 0) {
                                if (readLine.startsWith("BEGIN:VCARD")) {
                                    vCardData = new VCardData();
                                } else if (readLine.startsWith("END:VCARD") && vCardData != null) {
                                    z = true;
                                }
                            }
                            if (str2 != null) {
                                readLine = str2 + readLine;
                                str2 = null;
                            }
                            if (readLine.contains("=QUOTED-PRINTABLE") && readLine.endsWith("=")) {
                                str2 = readLine.substring(0, readLine.length() - 1);
                            } else {
                                int indexOf = readLine.indexOf(":");
                                int i = 2;
                                String[] strArr = indexOf >= 0 ? new String[]{readLine.substring(0, indexOf), readLine.substring(indexOf + 1).trim()} : new String[]{readLine.trim()};
                                if (strArr.length >= 2 && vCardData != null) {
                                    if (strArr[0].startsWith("ORG")) {
                                        String[] split = strArr[0].split(";");
                                        int length = split.length;
                                        int i2 = 0;
                                        String str3 = null;
                                        String str4 = null;
                                        while (i2 < length) {
                                            String[] split2 = split[i2].split("=");
                                            if (split2.length == i) {
                                                if (split2[0].equals("CHARSET")) {
                                                    str4 = split2[1];
                                                } else if (split2[0].equals("ENCODING")) {
                                                    str3 = split2[1];
                                                }
                                            }
                                            i2++;
                                            i = 2;
                                        }
                                        vCardData.company = strArr[1];
                                        if (str3 != null && str3.equalsIgnoreCase("QUOTED-PRINTABLE") && (decodeQuotedPrintable = AndroidUtilities.decodeQuotedPrintable(AndroidUtilities.getStringBytes(vCardData.company))) != null && decodeQuotedPrintable.length != 0) {
                                            vCardData.company = new String(decodeQuotedPrintable, str4);
                                        }
                                        vCardData.company = vCardData.company.replace(';', ' ');
                                    } else if (strArr[0].startsWith("TEL")) {
                                        if (strArr[1].length() > 0) {
                                            vCardData.phones.add(strArr[1]);
                                        }
                                    } else if (strArr[0].startsWith("EMAIL")) {
                                        String str5 = strArr[1];
                                        if (str5.length() > 0) {
                                            vCardData.emails.add(str5);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        try {
                            break;
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                }
                bufferedReader.close();
                if (!z) {
                    return null;
                }
                StringBuilder sb = new StringBuilder();
                for (int i3 = 0; i3 < vCardData.phones.size(); i3++) {
                    if (sb.length() > 0) {
                        sb.append('\n');
                    }
                    String str6 = vCardData.phones.get(i3);
                    if (!str6.contains("#") && !str6.contains("*")) {
                        sb.append(PhoneFormat.getInstance().format(str6));
                    }
                    sb.append(str6);
                }
                for (int i4 = 0; i4 < vCardData.emails.size(); i4++) {
                    if (sb.length() > 0) {
                        sb.append('\n');
                    }
                    sb.append(PhoneFormat.getInstance().format(vCardData.emails.get(i4)));
                }
                if (!TextUtils.isEmpty(vCardData.company)) {
                    if (sb.length() > 0) {
                        sb.append('\n');
                    }
                    sb.append(vCardData.company);
                }
                return sb;
            } catch (Throwable unused) {
                return null;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class TextLayoutBlock {
        public static final int FLAG_NOT_RTL = 2;
        public static final int FLAG_RTL = 1;
        public int charactersEnd;
        public int charactersOffset;
        public byte directionFlags;
        public int height;
        public int heightByOffset;
        public StaticLayout textLayout;
        public float textYOffset;
        public AtomicReference<Layout> spoilersPatchedTextLayout = new AtomicReference<>();
        public List<SpoilerEffect> spoilers = new ArrayList();

        public boolean isRtl() {
            byte b = this.directionFlags;
            return (b & 1) != 0 && (b & 2) == 0;
        }
    }

    /* loaded from: classes.dex */
    public static class GroupedMessagePosition {
        public float aspectRatio;
        public boolean edge;
        public int flags;
        public boolean last;
        public float left;
        public int leftSpanOffset;
        public byte maxX;
        public byte maxY;
        public byte minX;
        public byte minY;
        public float ph;
        public int pw;
        public float[] siblingHeights;
        public int spanSize;
        public float top;

        public void set(int i, int i2, int i3, int i4, int i5, float f, int i6) {
            this.minX = (byte) i;
            this.maxX = (byte) i2;
            this.minY = (byte) i3;
            this.maxY = (byte) i4;
            this.pw = i5;
            this.spanSize = i5;
            this.ph = f;
            this.flags = (byte) i6;
        }
    }

    /* loaded from: classes.dex */
    public static class GroupedMessages {
        public long groupId;
        public boolean hasCaption;
        public boolean hasSibling;
        public boolean isDocuments;
        public ArrayList<MessageObject> messages = new ArrayList<>();
        public ArrayList<GroupedMessagePosition> posArray = new ArrayList<>();
        public HashMap<MessageObject, GroupedMessagePosition> positions = new HashMap<>();
        private int maxSizeWidth = 800;
        public final TransitionParams transitionParams = new TransitionParams();

        /* loaded from: classes.dex */
        public static class MessageGroupedLayoutAttempt {
            public float[] heights;
            public int[] lineCounts;

            public MessageGroupedLayoutAttempt(int i, int i2, float f, float f2) {
                this.lineCounts = new int[]{i, i2};
                this.heights = new float[]{f, f2};
            }

            public MessageGroupedLayoutAttempt(int i, int i2, int i3, float f, float f2, float f3) {
                this.lineCounts = new int[]{i, i2, i3};
                this.heights = new float[]{f, f2, f3};
            }

            public MessageGroupedLayoutAttempt(int i, int i2, int i3, int i4, float f, float f2, float f3, float f4) {
                this.lineCounts = new int[]{i, i2, i3, i4};
                this.heights = new float[]{f, f2, f3, f4};
            }
        }

        private float multiHeight(float[] fArr, int i, int i2) {
            float f = 0.0f;
            while (i < i2) {
                f += fArr[i];
                i++;
            }
            return this.maxSizeWidth / f;
        }

        /* JADX WARN: Code restructure failed: missing block: B:25:0x006a, code lost:
            if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaInvoice) == false) goto L27;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void calculate() {
            int i;
            int i2;
            int i3;
            float f;
            int i4;
            int i5;
            float f2;
            int i6;
            this.posArray.clear();
            this.positions.clear();
            this.maxSizeWidth = 800;
            int size = this.messages.size();
            if (size <= 1) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            this.hasSibling = false;
            this.hasCaption = false;
            int i7 = 0;
            boolean z = false;
            float f3 = 1.0f;
            boolean z2 = false;
            boolean z3 = false;
            while (i7 < size) {
                MessageObject messageObject = this.messages.get(i7);
                if (i7 == 0) {
                    z3 = messageObject.isOutOwner();
                    if (!z3) {
                        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
                        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from;
                        if (tLRPC$MessageFwdHeader == null || tLRPC$MessageFwdHeader.saved_from_peer == null) {
                            if (tLRPC$Message.from_id instanceof TLRPC$TL_peerUser) {
                                TLRPC$Peer tLRPC$Peer = tLRPC$Message.peer_id;
                                if (tLRPC$Peer.channel_id == 0 && tLRPC$Peer.chat_id == 0) {
                                    TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
                                    if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame)) {
                                    }
                                }
                            }
                        }
                        z = true;
                        if (!messageObject.isMusic() || messageObject.isDocument()) {
                            this.isDocuments = true;
                        }
                    }
                    z = false;
                    if (!messageObject.isMusic()) {
                    }
                    this.isDocuments = true;
                }
                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                GroupedMessagePosition groupedMessagePosition = new GroupedMessagePosition();
                groupedMessagePosition.last = i7 == size + (-1);
                float f4 = closestPhotoSizeWithSize == null ? 1.0f : closestPhotoSizeWithSize.w / closestPhotoSizeWithSize.h;
                groupedMessagePosition.aspectRatio = f4;
                if (f4 > 1.2f) {
                    sb.append("w");
                } else if (f4 < 0.8f) {
                    sb.append("n");
                } else {
                    sb.append("q");
                }
                float f5 = groupedMessagePosition.aspectRatio;
                f3 += f5;
                if (f5 > 2.0f) {
                    z2 = true;
                }
                this.positions.put(messageObject, groupedMessagePosition);
                this.posArray.add(groupedMessagePosition);
                if (messageObject.caption != null) {
                    this.hasCaption = true;
                }
                i7++;
            }
            if (this.isDocuments) {
                for (int i8 = 0; i8 < size; i8++) {
                    GroupedMessagePosition groupedMessagePosition2 = this.posArray.get(i8);
                    int i9 = groupedMessagePosition2.flags | 3;
                    groupedMessagePosition2.flags = i9;
                    if (i8 == 0) {
                        groupedMessagePosition2.flags = i9 | 4;
                    } else if (i8 == size - 1) {
                        groupedMessagePosition2.flags = i9 | 8;
                        groupedMessagePosition2.last = true;
                    }
                    groupedMessagePosition2.edge = true;
                    groupedMessagePosition2.aspectRatio = 1.0f;
                    groupedMessagePosition2.minX = (byte) 0;
                    groupedMessagePosition2.maxX = (byte) 0;
                    byte b = (byte) i8;
                    groupedMessagePosition2.minY = b;
                    groupedMessagePosition2.maxY = b;
                    groupedMessagePosition2.spanSize = 1000;
                    groupedMessagePosition2.pw = this.maxSizeWidth;
                    groupedMessagePosition2.ph = 100.0f;
                }
                return;
            }
            if (z) {
                this.maxSizeWidth -= 50;
                i = 250;
            } else {
                i = 200;
            }
            int dp = AndroidUtilities.dp(120.0f);
            Point point = AndroidUtilities.displaySize;
            int dp2 = (int) (AndroidUtilities.dp(120.0f) / (Math.min(point.x, point.y) / this.maxSizeWidth));
            Point point2 = AndroidUtilities.displaySize;
            int i10 = this.maxSizeWidth;
            int dp3 = (int) (AndroidUtilities.dp(40.0f) / (Math.min(point2.x, point2.y) / i10));
            float f6 = i10 / 814.0f;
            float f7 = f3 / size;
            float dp4 = AndroidUtilities.dp(100.0f) / 814.0f;
            if (z2 || !(size == 2 || size == 3 || size == 4)) {
                int size2 = this.posArray.size();
                float[] fArr = new float[size2];
                for (int i11 = 0; i11 < size; i11++) {
                    if (f7 > 1.1f) {
                        fArr[i11] = Math.max(1.0f, this.posArray.get(i11).aspectRatio);
                    } else {
                        fArr[i11] = Math.min(1.0f, this.posArray.get(i11).aspectRatio);
                    }
                    fArr[i11] = Math.max(0.66667f, Math.min(1.7f, fArr[i11]));
                }
                ArrayList arrayList = new ArrayList();
                for (int i12 = 1; i12 < size2; i12++) {
                    int i13 = size2 - i12;
                    if (i12 <= 3 && i13 <= 3) {
                        arrayList.add(new MessageGroupedLayoutAttempt(i12, i13, multiHeight(fArr, 0, i12), multiHeight(fArr, i12, size2)));
                    }
                }
                for (int i14 = 1; i14 < size2 - 1; i14++) {
                    int i15 = 1;
                    while (true) {
                        int i16 = size2 - i14;
                        if (i15 < i16) {
                            int i17 = i16 - i15;
                            if (i14 <= 3) {
                                if (i15 <= (f7 < 0.85f ? 4 : 3) && i17 <= 3) {
                                    int i18 = i14 + i15;
                                    arrayList.add(new MessageGroupedLayoutAttempt(i14, i15, i17, multiHeight(fArr, 0, i14), multiHeight(fArr, i14, i18), multiHeight(fArr, i18, size2)));
                                }
                            }
                            i15++;
                        }
                    }
                }
                for (int i19 = 1; i19 < size2 - 2; i19++) {
                    int i20 = 1;
                    while (true) {
                        int i21 = size2 - i19;
                        if (i20 < i21) {
                            int i22 = 1;
                            while (true) {
                                int i23 = i21 - i20;
                                if (i22 < i23) {
                                    int i24 = i23 - i22;
                                    if (i19 > 3 || i20 > 3 || i22 > 3 || i24 > 3) {
                                        i4 = i21;
                                    } else {
                                        int i25 = i19 + i20;
                                        i4 = i21;
                                        int i26 = i25 + i22;
                                        arrayList.add(new MessageGroupedLayoutAttempt(i19, i20, i22, i24, multiHeight(fArr, 0, i19), multiHeight(fArr, i19, i25), multiHeight(fArr, i25, i26), multiHeight(fArr, i26, size2)));
                                    }
                                    i22++;
                                    i21 = i4;
                                }
                            }
                            i20++;
                        }
                    }
                }
                float f8 = (this.maxSizeWidth / 3) * 4;
                int i27 = 0;
                MessageGroupedLayoutAttempt messageGroupedLayoutAttempt = null;
                float f9 = 0.0f;
                while (i27 < arrayList.size()) {
                    MessageGroupedLayoutAttempt messageGroupedLayoutAttempt2 = (MessageGroupedLayoutAttempt) arrayList.get(i27);
                    float f10 = Float.MAX_VALUE;
                    int i28 = 0;
                    float f11 = 0.0f;
                    while (true) {
                        float[] fArr2 = messageGroupedLayoutAttempt2.heights;
                        if (i28 >= fArr2.length) {
                            break;
                        }
                        f11 += fArr2[i28];
                        if (fArr2[i28] < f10) {
                            f10 = fArr2[i28];
                        }
                        i28++;
                    }
                    float abs = Math.abs(f11 - f8);
                    int[] iArr = messageGroupedLayoutAttempt2.lineCounts;
                    float f12 = f8;
                    ArrayList arrayList2 = arrayList;
                    if (iArr.length > 1) {
                        if (iArr[0] <= iArr[1]) {
                            if (iArr.length > 2 && iArr[1] > iArr[2]) {
                                f = 1.2f;
                                abs *= f;
                            } else if (iArr.length <= 3 || iArr[2] <= iArr[3]) {
                            }
                        }
                        f = 1.2f;
                        abs *= f;
                    }
                    if (f10 < dp2) {
                        abs *= 1.5f;
                    }
                    if (messageGroupedLayoutAttempt == null || abs < f9) {
                        f9 = abs;
                        messageGroupedLayoutAttempt = messageGroupedLayoutAttempt2;
                    }
                    i27++;
                    arrayList = arrayList2;
                    f8 = f12;
                }
                if (messageGroupedLayoutAttempt == null) {
                    return;
                }
                int i29 = 0;
                int i30 = 0;
                int i31 = 0;
                while (true) {
                    int[] iArr2 = messageGroupedLayoutAttempt.lineCounts;
                    if (i29 >= iArr2.length) {
                        break;
                    }
                    int i32 = iArr2[i29];
                    float f13 = messageGroupedLayoutAttempt.heights[i29];
                    int i33 = i32 - 1;
                    int i34 = this.maxSizeWidth;
                    GroupedMessagePosition groupedMessagePosition3 = null;
                    int i35 = i31;
                    int i36 = 0;
                    int i37 = Math.max(i30 == 1 ? 1 : 0, i33);
                    while (i36 < i32) {
                        float[] fArr3 = fArr;
                        int i38 = (int) (fArr[i35] * f13);
                        i34 -= i38;
                        int i39 = i37;
                        GroupedMessagePosition groupedMessagePosition4 = this.posArray.get(i35);
                        int i40 = i32;
                        int i41 = i29 == 0 ? 4 : 0;
                        if (i29 == messageGroupedLayoutAttempt.lineCounts.length - 1) {
                            i41 |= 8;
                        }
                        if (i36 == 0) {
                            i41 |= 1;
                            if (z3) {
                                groupedMessagePosition3 = groupedMessagePosition4;
                            }
                        }
                        if (i36 == i33) {
                            i41 |= 2;
                            if (!z3) {
                                i3 = i41;
                                groupedMessagePosition3 = groupedMessagePosition4;
                                groupedMessagePosition4.set(i36, i36, i29, i29, i38, Math.max(dp4, f13 / 814.0f), i3);
                                i35++;
                                i36++;
                                i37 = i39;
                                fArr = fArr3;
                                i32 = i40;
                            }
                        }
                        i3 = i41;
                        groupedMessagePosition4.set(i36, i36, i29, i29, i38, Math.max(dp4, f13 / 814.0f), i3);
                        i35++;
                        i36++;
                        i37 = i39;
                        fArr = fArr3;
                        i32 = i40;
                    }
                    boolean z4 = i37 == 1 ? 1 : 0;
                    groupedMessagePosition3.pw += i34;
                    groupedMessagePosition3.spanSize += i34;
                    i29++;
                    i31 = i35;
                    fArr = fArr;
                    i30 = i37;
                }
                i2 = i30;
            } else if (size == 2) {
                GroupedMessagePosition groupedMessagePosition5 = this.posArray.get(0);
                GroupedMessagePosition groupedMessagePosition6 = this.posArray.get(1);
                String sb2 = sb.toString();
                if (sb2.equals("ww")) {
                    i6 = dp2;
                    double d = f6;
                    Double.isNaN(d);
                    if (f7 > d * 1.4d) {
                        float f14 = groupedMessagePosition5.aspectRatio;
                        float f15 = groupedMessagePosition6.aspectRatio;
                        if (f14 - f15 < 0.2d) {
                            int i42 = this.maxSizeWidth;
                            float round = Math.round(Math.min(i42 / f14, Math.min(i42 / f15, 407.0f))) / 814.0f;
                            groupedMessagePosition5.set(0, 0, 0, 0, this.maxSizeWidth, round, 7);
                            groupedMessagePosition6.set(0, 0, 1, 1, this.maxSizeWidth, round, 11);
                            i5 = 0;
                            i2 = i5;
                        }
                    }
                } else {
                    i6 = dp2;
                }
                if (sb2.equals("ww") || sb2.equals("qq")) {
                    int i43 = this.maxSizeWidth / 2;
                    float f16 = i43;
                    float round2 = Math.round(Math.min(f16 / groupedMessagePosition5.aspectRatio, Math.min(f16 / groupedMessagePosition6.aspectRatio, 814.0f))) / 814.0f;
                    groupedMessagePosition5.set(0, 0, 0, 0, i43, round2, 13);
                    groupedMessagePosition6.set(1, 1, 0, 0, i43, round2, 14);
                    i5 = 1;
                    i2 = i5;
                } else {
                    int i44 = this.maxSizeWidth;
                    float f17 = groupedMessagePosition5.aspectRatio;
                    int max = (int) Math.max(i44 * 0.4f, Math.round((i44 / f17) / ((1.0f / f17) + (1.0f / groupedMessagePosition6.aspectRatio))));
                    int i45 = this.maxSizeWidth - max;
                    int i46 = i6;
                    if (i45 < i46) {
                        max -= i46 - i45;
                        i45 = i46;
                    }
                    float min = Math.min(814.0f, Math.round(Math.min(i45 / groupedMessagePosition5.aspectRatio, max / groupedMessagePosition6.aspectRatio))) / 814.0f;
                    groupedMessagePosition5.set(0, 0, 0, 0, i45, min, 13);
                    groupedMessagePosition6.set(1, 1, 0, 0, max, min, 14);
                    i5 = 1;
                    i2 = i5;
                }
            } else if (size == 3) {
                GroupedMessagePosition groupedMessagePosition7 = this.posArray.get(0);
                GroupedMessagePosition groupedMessagePosition8 = this.posArray.get(1);
                GroupedMessagePosition groupedMessagePosition9 = this.posArray.get(2);
                if (sb.charAt(0) == 'n') {
                    float f18 = groupedMessagePosition8.aspectRatio;
                    float min2 = Math.min(407.0f, Math.round((this.maxSizeWidth * f18) / (groupedMessagePosition9.aspectRatio + f18)));
                    int max2 = (int) Math.max(dp2, Math.min(this.maxSizeWidth * 0.5f, Math.round(Math.min(groupedMessagePosition9.aspectRatio * min2, groupedMessagePosition8.aspectRatio * f2))));
                    int round3 = Math.round(Math.min((groupedMessagePosition7.aspectRatio * 814.0f) + dp3, this.maxSizeWidth - max2));
                    groupedMessagePosition7.set(0, 0, 0, 1, round3, 1.0f, 13);
                    float f19 = (814.0f - min2) / 814.0f;
                    groupedMessagePosition8.set(1, 1, 0, 0, max2, f19, 6);
                    float f20 = min2 / 814.0f;
                    groupedMessagePosition9.set(0, 1, 1, 1, max2, f20, 10);
                    int i47 = this.maxSizeWidth;
                    groupedMessagePosition9.spanSize = i47;
                    groupedMessagePosition7.siblingHeights = new float[]{f20, f19};
                    if (z3) {
                        groupedMessagePosition7.spanSize = i47 - max2;
                    } else {
                        groupedMessagePosition8.spanSize = i47 - round3;
                        groupedMessagePosition9.leftSpanOffset = round3;
                    }
                    this.hasSibling = true;
                } else {
                    float round4 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition7.aspectRatio, 537.24005f)) / 814.0f;
                    groupedMessagePosition7.set(0, 1, 0, 0, this.maxSizeWidth, round4, 7);
                    int i48 = this.maxSizeWidth / 2;
                    float f21 = i48;
                    float min3 = Math.min(814.0f - round4, Math.round(Math.min(f21 / groupedMessagePosition8.aspectRatio, f21 / groupedMessagePosition9.aspectRatio))) / 814.0f;
                    if (min3 < dp4) {
                        min3 = dp4;
                    }
                    float f22 = min3;
                    groupedMessagePosition8.set(0, 0, 1, 1, i48, f22, 9);
                    groupedMessagePosition9.set(1, 1, 1, 1, i48, f22, 10);
                }
                i2 = 1;
            } else {
                GroupedMessagePosition groupedMessagePosition10 = this.posArray.get(0);
                GroupedMessagePosition groupedMessagePosition11 = this.posArray.get(1);
                GroupedMessagePosition groupedMessagePosition12 = this.posArray.get(2);
                GroupedMessagePosition groupedMessagePosition13 = this.posArray.get(3);
                if (sb.charAt(0) == 'w') {
                    float round5 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition10.aspectRatio, 537.24005f)) / 814.0f;
                    groupedMessagePosition10.set(0, 2, 0, 0, this.maxSizeWidth, round5, 7);
                    float round6 = Math.round(this.maxSizeWidth / ((groupedMessagePosition11.aspectRatio + groupedMessagePosition12.aspectRatio) + groupedMessagePosition13.aspectRatio));
                    float f23 = dp2;
                    int max3 = (int) Math.max(f23, Math.min(this.maxSizeWidth * 0.4f, groupedMessagePosition11.aspectRatio * round6));
                    int max4 = (int) Math.max(Math.max(f23, this.maxSizeWidth * 0.33f), groupedMessagePosition13.aspectRatio * round6);
                    int i49 = (this.maxSizeWidth - max3) - max4;
                    if (i49 < AndroidUtilities.dp(58.0f)) {
                        int dp5 = AndroidUtilities.dp(58.0f) - i49;
                        i49 = AndroidUtilities.dp(58.0f);
                        int i50 = dp5 / 2;
                        max3 -= i50;
                        max4 -= dp5 - i50;
                    }
                    int i51 = max3;
                    float min4 = Math.min(814.0f - round5, round6) / 814.0f;
                    if (min4 < dp4) {
                        min4 = dp4;
                    }
                    float f24 = min4;
                    groupedMessagePosition11.set(0, 0, 1, 1, i51, f24, 9);
                    groupedMessagePosition12.set(1, 1, 1, 1, i49, f24, 8);
                    groupedMessagePosition13.set(2, 2, 1, 1, max4, f24, 10);
                    i5 = 2;
                    i2 = i5;
                } else {
                    int max5 = Math.max(dp2, Math.round(814.0f / (((1.0f / groupedMessagePosition11.aspectRatio) + (1.0f / groupedMessagePosition12.aspectRatio)) + (1.0f / groupedMessagePosition13.aspectRatio))));
                    float f25 = dp;
                    float f26 = max5;
                    float min5 = Math.min(0.33f, Math.max(f25, f26 / groupedMessagePosition11.aspectRatio) / 814.0f);
                    float min6 = Math.min(0.33f, Math.max(f25, f26 / groupedMessagePosition12.aspectRatio) / 814.0f);
                    float f27 = (1.0f - min5) - min6;
                    int round7 = Math.round(Math.min((814.0f * groupedMessagePosition10.aspectRatio) + dp3, this.maxSizeWidth - max5));
                    groupedMessagePosition10.set(0, 0, 0, 2, round7, min5 + min6 + f27, 13);
                    groupedMessagePosition11.set(1, 1, 0, 0, max5, min5, 6);
                    groupedMessagePosition12.set(0, 1, 1, 1, max5, min6, 2);
                    groupedMessagePosition12.spanSize = this.maxSizeWidth;
                    groupedMessagePosition13.set(0, 1, 2, 2, max5, f27, 10);
                    int i52 = this.maxSizeWidth;
                    groupedMessagePosition13.spanSize = i52;
                    if (z3) {
                        groupedMessagePosition10.spanSize = i52 - max5;
                    } else {
                        groupedMessagePosition11.spanSize = i52 - round7;
                        groupedMessagePosition12.leftSpanOffset = round7;
                        groupedMessagePosition13.leftSpanOffset = round7;
                    }
                    groupedMessagePosition10.siblingHeights = new float[]{min5, min6, f27};
                    this.hasSibling = true;
                    i5 = 1;
                    i2 = i5;
                }
            }
            for (int i53 = 0; i53 < size; i53++) {
                GroupedMessagePosition groupedMessagePosition14 = this.posArray.get(i53);
                if (z3) {
                    if (groupedMessagePosition14.minX == 0) {
                        groupedMessagePosition14.spanSize += i;
                    }
                    if ((groupedMessagePosition14.flags & 2) != 0) {
                        groupedMessagePosition14.edge = true;
                    }
                } else {
                    if (groupedMessagePosition14.maxX == i2 || (groupedMessagePosition14.flags & 2) != 0) {
                        groupedMessagePosition14.spanSize += i;
                    }
                    if ((groupedMessagePosition14.flags & 1) != 0) {
                        groupedMessagePosition14.edge = true;
                    }
                }
                MessageObject messageObject2 = this.messages.get(i53);
                if (!z3 && messageObject2.needDrawAvatarInternal()) {
                    if (groupedMessagePosition14.edge) {
                        int i54 = groupedMessagePosition14.spanSize;
                        if (i54 != 1000) {
                            groupedMessagePosition14.spanSize = i54 + 108;
                        }
                        groupedMessagePosition14.pw += 108;
                    } else {
                        if ((groupedMessagePosition14.flags & 2) != 0) {
                            int i55 = groupedMessagePosition14.spanSize;
                            if (i55 != 1000) {
                                groupedMessagePosition14.spanSize = i55 - 108;
                            } else {
                                int i56 = groupedMessagePosition14.leftSpanOffset;
                                if (i56 != 0) {
                                    groupedMessagePosition14.leftSpanOffset = i56 + 108;
                                }
                            }
                        }
                    }
                }
            }
        }

        public MessageObject findPrimaryMessageObject() {
            return findMessageWithFlags(5);
        }

        public MessageObject findMessageWithFlags(int i) {
            if (!this.messages.isEmpty() && this.positions.isEmpty()) {
                calculate();
            }
            for (int i2 = 0; i2 < this.messages.size(); i2++) {
                MessageObject messageObject = this.messages.get(i2);
                GroupedMessagePosition groupedMessagePosition = this.positions.get(messageObject);
                if (groupedMessagePosition != null && (groupedMessagePosition.flags & i) == i) {
                    return messageObject;
                }
            }
            return null;
        }

        /* loaded from: classes.dex */
        public static class TransitionParams {
            public boolean backgroundChangeBounds;
            public int bottom;
            public float captionEnterProgress = 1.0f;
            public ChatMessageCell cell;
            public boolean drawBackgroundForDeletedItems;
            public boolean drawCaptionLayout;
            public boolean isNewGroup;
            public int left;
            public float offsetBottom;
            public float offsetLeft;
            public float offsetRight;
            public float offsetTop;
            public boolean pinnedBotton;
            public boolean pinnedTop;
            public int right;
            public int top;

            public void reset() {
                this.captionEnterProgress = 1.0f;
                this.offsetBottom = 0.0f;
                this.offsetTop = 0.0f;
                this.offsetRight = 0.0f;
                this.offsetLeft = 0.0f;
                this.backgroundChangeBounds = false;
            }
        }
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, String str, String str2, String str3, boolean z, boolean z2, boolean z3, boolean z4) {
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.viewRef = new AtomicReference<>(null);
        this.localType = z ? 2 : 1;
        this.currentAccount = i;
        this.localName = str2;
        this.localUserName = str3;
        this.messageText = str;
        this.messageOwner = tLRPC$Message;
        this.localChannel = z2;
        this.localSupergroup = z3;
        this.localEdit = z4;
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, AbstractMap<Long, TLRPC$User> abstractMap, boolean z, boolean z2) {
        this(i, tLRPC$Message, abstractMap, (AbstractMap<Long, TLRPC$Chat>) null, z, z2);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, LongSparseArray<TLRPC$User> longSparseArray, boolean z, boolean z2) {
        this(i, tLRPC$Message, longSparseArray, (LongSparseArray<TLRPC$Chat>) null, z, z2);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, boolean z, boolean z2) {
        this(i, tLRPC$Message, null, null, null, null, null, z, z2, 0L);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, MessageObject messageObject, boolean z, boolean z2) {
        this(i, tLRPC$Message, messageObject, null, null, null, null, z, z2, 0L);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, boolean z, boolean z2) {
        this(i, tLRPC$Message, abstractMap, abstractMap2, z, z2, 0L);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, LongSparseArray<TLRPC$User> longSparseArray, LongSparseArray<TLRPC$Chat> longSparseArray2, boolean z, boolean z2) {
        this(i, tLRPC$Message, null, null, null, longSparseArray, longSparseArray2, z, z2, 0L);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, boolean z, boolean z2, long j) {
        this(i, tLRPC$Message, null, abstractMap, abstractMap2, null, null, z, z2, j);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, MessageObject messageObject, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, LongSparseArray<TLRPC$User> longSparseArray, LongSparseArray<TLRPC$Chat> longSparseArray2, boolean z, boolean z2, long j) {
        TLRPC$User tLRPC$User;
        LongSparseArray<TLRPC$Chat> longSparseArray3;
        AbstractMap<Long, TLRPC$Chat> abstractMap3;
        TextPaint textPaint;
        int i2;
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.viewRef = new AtomicReference<>(null);
        Theme.createCommonMessageResources();
        this.currentAccount = i;
        this.messageOwner = tLRPC$Message;
        this.replyMessageObject = messageObject;
        this.eventId = j;
        this.wasUnread = !tLRPC$Message.out && tLRPC$Message.unread;
        TLRPC$Message tLRPC$Message2 = tLRPC$Message.replyMessage;
        if (tLRPC$Message2 != null) {
            this.replyMessageObject = new MessageObject(i, tLRPC$Message2, null, abstractMap, abstractMap2, longSparseArray, longSparseArray2, false, z2, j);
        }
        TLRPC$Peer tLRPC$Peer = tLRPC$Message.from_id;
        if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
            tLRPC$User = getUser(abstractMap, longSparseArray, tLRPC$Peer.user_id);
            abstractMap3 = abstractMap2;
            longSparseArray3 = longSparseArray2;
        } else {
            abstractMap3 = abstractMap2;
            longSparseArray3 = longSparseArray2;
            tLRPC$User = null;
        }
        updateMessageText(abstractMap, abstractMap3, longSparseArray, longSparseArray3);
        setType();
        measureInlineBotButtons();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(this.messageOwner.date * 1000);
        int i3 = gregorianCalendar.get(6);
        int i4 = gregorianCalendar.get(1);
        int i5 = gregorianCalendar.get(2);
        this.dateKey = String.format("%d_%02d_%02d", Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i3));
        this.monthKey = String.format("%d_%02d", Integer.valueOf(i4), Integer.valueOf(i5));
        createMessageSendInfo();
        generateCaption();
        if (z) {
            if (this.messageOwner.media instanceof TLRPC$TL_messageMediaGame) {
                textPaint = Theme.chat_msgGameTextPaint;
            } else {
                textPaint = Theme.chat_msgTextPaint;
            }
            int[] iArr = allowsBigEmoji() ? new int[1] : null;
            this.messageText = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, iArr, this.contentType == 0, this.viewRef);
            checkEmojiOnly(iArr);
            this.emojiAnimatedSticker = null;
            if (this.emojiOnlyCount == 1) {
                TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
                if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) && !(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) && tLRPC$Message.entities.isEmpty()) {
                    TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$Message.media;
                    if (((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaEmpty) || tLRPC$MessageMedia2 == null) && this.messageOwner.grouped_id == 0) {
                        String str = this.messageText;
                        int indexOf = TextUtils.indexOf(str, "🏻");
                        if (indexOf >= 0) {
                            this.emojiAnimatedStickerColor = "_c1";
                            str = str.subSequence(0, indexOf);
                        } else {
                            indexOf = TextUtils.indexOf(str, "🏼");
                            if (indexOf >= 0) {
                                this.emojiAnimatedStickerColor = "_c2";
                                str = str.subSequence(0, indexOf);
                            } else {
                                indexOf = TextUtils.indexOf(str, "🏽");
                                if (indexOf >= 0) {
                                    this.emojiAnimatedStickerColor = "_c3";
                                    str = str.subSequence(0, indexOf);
                                } else {
                                    indexOf = TextUtils.indexOf(str, "🏾");
                                    if (indexOf >= 0) {
                                        this.emojiAnimatedStickerColor = "_c4";
                                        str = str.subSequence(0, indexOf);
                                    } else {
                                        indexOf = TextUtils.indexOf(str, "🏿");
                                        if (indexOf >= 0) {
                                            this.emojiAnimatedStickerColor = "_c5";
                                            str = str.subSequence(0, indexOf);
                                        } else {
                                            this.emojiAnimatedStickerColor = "";
                                        }
                                    }
                                }
                            }
                        }
                        if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor) && (i2 = indexOf + 2) < this.messageText.length()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(str.toString());
                            CharSequence charSequence = this.messageText;
                            sb.append(charSequence.subSequence(i2, charSequence.length()).toString());
                            str = sb.toString();
                        }
                        if (TextUtils.isEmpty(this.emojiAnimatedStickerColor) || EmojiData.emojiColoredMap.contains(str.toString())) {
                            this.emojiAnimatedSticker = MediaDataController.getInstance(this.currentAccount).getEmojiAnimatedSticker(str);
                        }
                    }
                }
            }
            if (this.emojiAnimatedSticker == null) {
                generateLayout(tLRPC$User);
            } else {
                this.type = 1000;
                if (isSticker()) {
                    this.type = 13;
                } else if (isAnimatedSticker()) {
                    this.type = 15;
                }
            }
            createPathThumb();
        }
        this.layoutCreated = z;
        generateThumbs(false);
        if (z2) {
            checkMediaExistance();
        }
    }

    private void createPathThumb() {
        TLRPC$Document document = getDocument();
        if (document == null) {
            return;
        }
        this.pathThumb = DocumentObject.getSvgThumb(document, "chat_serviceBackground", 1.0f);
    }

    public void createStrippedThumb() {
        if (this.photoThumbs == null || SharedConfig.getDevicePerformanceClass() != 2) {
            return;
        }
        try {
            int size = this.photoThumbs.size();
            for (int i = 0; i < size; i++) {
                TLRPC$PhotoSize tLRPC$PhotoSize = this.photoThumbs.get(i);
                if (tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) {
                    this.strippedThumb = new BitmapDrawable(ImageLoader.getStrippedPhotoBitmap(tLRPC$PhotoSize.bytes, "b"));
                    return;
                }
            }
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    private void createDateArray(int i, TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent, ArrayList<MessageObject> arrayList, HashMap<String, ArrayList<MessageObject>> hashMap, boolean z) {
        if (hashMap.get(this.dateKey) == null) {
            hashMap.put(this.dateKey, new ArrayList<>());
            TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
            tLRPC$TL_message.message = LocaleController.formatDateChat(tLRPC$TL_channelAdminLogEvent.date);
            tLRPC$TL_message.id = 0;
            tLRPC$TL_message.date = tLRPC$TL_channelAdminLogEvent.date;
            MessageObject messageObject = new MessageObject(i, tLRPC$TL_message, false, false);
            messageObject.type = 10;
            messageObject.contentType = 1;
            messageObject.isDateObject = true;
            if (z) {
                arrayList.add(0, messageObject);
            } else {
                arrayList.add(messageObject);
            }
        }
    }

    private void checkEmojiOnly(int[] iArr) {
        TextPaint textPaint;
        int i;
        if (iArr != null) {
            if (iArr[0] < 1 || iArr[0] > 3) {
                return;
            }
            int i2 = iArr[0];
            if (i2 == 1) {
                textPaint = Theme.chat_msgTextPaintOneEmoji;
                i = AndroidUtilities.dp(32.0f);
                this.emojiOnlyCount = 1;
            } else if (i2 == 2) {
                textPaint = Theme.chat_msgTextPaintTwoEmoji;
                int dp = AndroidUtilities.dp(28.0f);
                this.emojiOnlyCount = 2;
                i = dp;
            } else {
                textPaint = Theme.chat_msgTextPaintThreeEmoji;
                i = AndroidUtilities.dp(24.0f);
                this.emojiOnlyCount = 3;
            }
            CharSequence charSequence = this.messageText;
            Emoji.EmojiSpan[] emojiSpanArr = (Emoji.EmojiSpan[]) ((Spannable) charSequence).getSpans(0, charSequence.length(), Emoji.EmojiSpan.class);
            if (emojiSpanArr == null || emojiSpanArr.length <= 0) {
                return;
            }
            for (Emoji.EmojiSpan emojiSpan : emojiSpanArr) {
                emojiSpan.replaceFontMetrics(textPaint.getFontMetricsInt(), i);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:432:0x0bde, code lost:
        if (r9.id == r11.id) goto L434;
     */
    /* JADX WARN: Removed duplicated region for block: B:184:0x04e4  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x04f4 A[LOOP:0: B:169:0x04b1->B:190:0x04f4, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:447:0x0c11  */
    /* JADX WARN: Removed duplicated region for block: B:448:0x0c21  */
    /* JADX WARN: Removed duplicated region for block: B:451:0x0c36  */
    /* JADX WARN: Removed duplicated region for block: B:733:0x1483  */
    /* JADX WARN: Removed duplicated region for block: B:736:0x14d1  */
    /* JADX WARN: Removed duplicated region for block: B:738:0x14d4  */
    /* JADX WARN: Removed duplicated region for block: B:748:0x1546  */
    /* JADX WARN: Removed duplicated region for block: B:751:0x154d  */
    /* JADX WARN: Removed duplicated region for block: B:775:0x050c A[EDGE_INSN: B:775:0x050c->B:192:0x050c ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:776:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MessageObject(int i, TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent, ArrayList<MessageObject> arrayList, HashMap<String, ArrayList<MessageObject>> hashMap, TLRPC$Chat tLRPC$Chat, int[] iArr, boolean z) {
        TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent2;
        String str;
        TLRPC$Chat tLRPC$Chat2;
        TLRPC$TL_message tLRPC$TL_message;
        ArrayList<MessageObject> arrayList2;
        int[] iArr2;
        TextPaint textPaint;
        TLRPC$ChannelParticipant tLRPC$ChannelParticipant;
        TLRPC$ChannelParticipant tLRPC$ChannelParticipant2;
        TLRPC$User tLRPC$User;
        StringBuilder sb;
        String str2;
        String str3;
        int i2;
        String str4;
        TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent3;
        String str5;
        char c;
        String str6;
        TLObject tLObject;
        TLObject tLObject2;
        TLObject tLObject3;
        char c2;
        String str7;
        String str8;
        boolean z2;
        TLRPC$Document tLRPC$Document;
        TLRPC$Peer tLRPC$Peer;
        String str9;
        int i3;
        String str10;
        int i4;
        String str11;
        int i5;
        String str12;
        int i6;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        String str13;
        TLObject tLObject4;
        String str14;
        StringBuilder sb2;
        boolean z3;
        char c3;
        int i7;
        String str15;
        int i8;
        boolean z4;
        TLObject tLObject5;
        MessageObject messageObject = this;
        messageObject.type = 1000;
        messageObject.forceSeekTo = -1.0f;
        messageObject.viewRef = new AtomicReference<>(null);
        messageObject.currentEvent = tLRPC$TL_channelAdminLogEvent;
        messageObject.currentAccount = i;
        TLRPC$User user = tLRPC$TL_channelAdminLogEvent.user_id > 0 ? MessagesController.getInstance(i).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEvent.user_id)) : null;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(tLRPC$TL_channelAdminLogEvent.date * 1000);
        int i9 = gregorianCalendar.get(6);
        int i10 = gregorianCalendar.get(1);
        int i11 = gregorianCalendar.get(2);
        messageObject.dateKey = String.format("%d_%02d_%02d", Integer.valueOf(i10), Integer.valueOf(i11), Integer.valueOf(i9));
        messageObject.monthKey = String.format("%d_%02d", Integer.valueOf(i10), Integer.valueOf(i11));
        TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
        tLRPC$TL_peerChannel.channel_id = tLRPC$Chat.id;
        TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction = tLRPC$TL_channelAdminLogEvent.action;
        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeTitle) {
            String str16 = ((TLRPC$TL_channelAdminLogEventActionChangeTitle) tLRPC$ChannelAdminLogEventAction).new_value;
            if (tLRPC$Chat.megagroup) {
                messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogEditedGroupTitle", 2131625692, str16), "un1", user);
            } else {
                messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogEditedChannelTitle", 2131625687, str16), "un1", user);
            }
        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangePhoto) {
            TLRPC$TL_channelAdminLogEventActionChangePhoto tLRPC$TL_channelAdminLogEventActionChangePhoto = (TLRPC$TL_channelAdminLogEventActionChangePhoto) tLRPC$ChannelAdminLogEventAction;
            TLRPC$TL_messageService tLRPC$TL_messageService = new TLRPC$TL_messageService();
            messageObject.messageOwner = tLRPC$TL_messageService;
            if (tLRPC$TL_channelAdminLogEventActionChangePhoto.new_photo instanceof TLRPC$TL_photoEmpty) {
                tLRPC$TL_messageService.action = new TLRPC$TL_messageActionChatDeletePhoto();
                if (tLRPC$Chat.megagroup) {
                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedWGroupPhoto", 2131625750), "un1", user);
                } else {
                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedChannelPhoto", 2131625744), "un1", user);
                }
            } else {
                tLRPC$TL_messageService.action = new TLRPC$TL_messageActionChatEditPhoto();
                messageObject.messageOwner.action.photo = tLRPC$TL_channelAdminLogEventActionChangePhoto.new_photo;
                if (tLRPC$Chat.megagroup) {
                    if (isVideoAvatar()) {
                        messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedGroupVideo", 2131625693), "un1", user);
                    } else {
                        messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedGroupPhoto", 2131625690), "un1", user);
                    }
                } else if (isVideoAvatar()) {
                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedChannelVideo", 2131625688), "un1", user);
                } else {
                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedChannelPhoto", 2131625685), "un1", user);
                }
            }
        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantJoin) {
            if (tLRPC$Chat.megagroup) {
                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogGroupJoined", 2131625715), "un1", user);
            } else {
                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogChannelJoined", 2131625678), "un1", user);
            }
        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantLeave) {
            TLRPC$TL_messageService tLRPC$TL_messageService2 = new TLRPC$TL_messageService();
            messageObject.messageOwner = tLRPC$TL_messageService2;
            tLRPC$TL_messageService2.action = new TLRPC$TL_messageActionChatDeleteUser();
            messageObject.messageOwner.action.user_id = tLRPC$TL_channelAdminLogEvent.user_id;
            if (tLRPC$Chat.megagroup) {
                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogLeftGroup", 2131625720), "un1", user);
            } else {
                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogLeftChannel", 2131625719), "un1", user);
            }
        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantInvite) {
            TLRPC$TL_messageService tLRPC$TL_messageService3 = new TLRPC$TL_messageService();
            messageObject.messageOwner = tLRPC$TL_messageService3;
            tLRPC$TL_messageService3.action = new TLRPC$TL_messageActionChatAddUser();
            long peerId = getPeerId(((TLRPC$TL_channelAdminLogEventActionParticipantInvite) tLRPC$ChannelAdminLogEventAction).participant.peer);
            if (peerId > 0) {
                tLObject5 = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId));
            } else {
                tLObject5 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(-peerId));
            }
            TLRPC$Peer tLRPC$Peer2 = messageObject.messageOwner.from_id;
            if ((tLRPC$Peer2 instanceof TLRPC$TL_peerUser) && peerId == tLRPC$Peer2.user_id) {
                if (tLRPC$Chat.megagroup) {
                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogGroupJoined", 2131625715), "un1", user);
                } else {
                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogChannelJoined", 2131625678), "un1", user);
                }
            } else {
                CharSequence replaceWithLink = replaceWithLink(LocaleController.getString("EventLogAdded", 2131625668), "un2", tLObject5);
                messageObject.messageText = replaceWithLink;
                messageObject.messageText = replaceWithLink(replaceWithLink, "un1", user);
            }
        } else {
            if ((tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin) || ((tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) && (((TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction).prev_participant instanceof TLRPC$TL_channelParticipantAdmin) && (((TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction).new_participant instanceof TLRPC$TL_channelParticipant))) {
                tLRPC$Chat2 = tLRPC$Chat;
                tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                str = "";
                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin) {
                    TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin tLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin = (TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin) tLRPC$ChannelAdminLogEventAction;
                    tLRPC$ChannelParticipant2 = tLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin.prev_participant;
                    tLRPC$ChannelParticipant = tLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin.new_participant;
                } else {
                    TLRPC$TL_channelAdminLogEventActionParticipantToggleBan tLRPC$TL_channelAdminLogEventActionParticipantToggleBan = (TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction;
                    tLRPC$ChannelParticipant2 = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan.prev_participant;
                    tLRPC$ChannelParticipant = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan.new_participant;
                }
                messageObject.messageOwner = new TLRPC$TL_message();
                long peerId2 = getPeerId(tLRPC$ChannelParticipant2.peer);
                if (peerId2 > 0) {
                    tLRPC$User = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId2));
                } else {
                    tLRPC$User = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(-peerId2));
                }
                if (!(tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantCreator) && (tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantCreator)) {
                    String string = LocaleController.getString("EventLogChangedOwnership", 2131625676);
                    sb = new StringBuilder(String.format(string, messageObject.getUserName(tLRPC$User, messageObject.messageOwner.entities, string.indexOf("%1$s"))));
                } else {
                    TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights = tLRPC$ChannelParticipant2.admin_rights;
                    TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2 = tLRPC$ChannelParticipant.admin_rights;
                    tLRPC$TL_chatAdminRights = tLRPC$TL_chatAdminRights == null ? new TLRPC$TL_chatAdminRights() : tLRPC$TL_chatAdminRights;
                    tLRPC$TL_chatAdminRights2 = tLRPC$TL_chatAdminRights2 == null ? new TLRPC$TL_chatAdminRights() : tLRPC$TL_chatAdminRights2;
                    if (tLRPC$TL_chatAdminRights2.other) {
                        str2 = LocaleController.getString("EventLogPromotedNoRights", 2131625737);
                    } else {
                        str2 = LocaleController.getString("EventLogPromoted", 2131625728);
                    }
                    StringBuilder sb3 = new StringBuilder(String.format(str2, messageObject.getUserName(tLRPC$User, messageObject.messageOwner.entities, str2.indexOf("%1$s"))));
                    sb3.append("\n");
                    if (!TextUtils.equals(tLRPC$ChannelParticipant2.rank, tLRPC$ChannelParticipant.rank)) {
                        if (TextUtils.isEmpty(tLRPC$ChannelParticipant.rank)) {
                            sb3.append('\n');
                            sb3.append('-');
                            sb3.append(' ');
                            sb3.append(LocaleController.getString("EventLogPromotedRemovedTitle", 2131625740));
                        } else {
                            sb3.append('\n');
                            sb3.append('+');
                            sb3.append(' ');
                            sb3.append(LocaleController.formatString("EventLogPromotedTitle", 2131625742, tLRPC$ChannelParticipant.rank));
                        }
                    }
                    if (tLRPC$TL_chatAdminRights.change_info != tLRPC$TL_chatAdminRights2.change_info) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.change_info ? '+' : '-');
                        sb3.append(' ');
                        if (tLRPC$Chat2.megagroup) {
                            i2 = 2131625733;
                            str3 = "EventLogPromotedChangeGroupInfo";
                        } else {
                            i2 = 2131625732;
                            str3 = "EventLogPromotedChangeChannelInfo";
                        }
                        sb3.append(LocaleController.getString(str3, i2));
                    }
                    if (!tLRPC$Chat2.megagroup) {
                        if (tLRPC$TL_chatAdminRights.post_messages != tLRPC$TL_chatAdminRights2.post_messages) {
                            sb3.append('\n');
                            sb3.append(tLRPC$TL_chatAdminRights2.post_messages ? '+' : '-');
                            sb3.append(' ');
                            sb3.append(LocaleController.getString("EventLogPromotedPostMessages", 2131625739));
                        }
                        if (tLRPC$TL_chatAdminRights.edit_messages != tLRPC$TL_chatAdminRights2.edit_messages) {
                            sb3.append('\n');
                            sb3.append(tLRPC$TL_chatAdminRights2.edit_messages ? '+' : '-');
                            sb3.append(' ');
                            sb3.append(LocaleController.getString("EventLogPromotedEditMessages", 2131625735));
                        }
                    }
                    if (tLRPC$TL_chatAdminRights.delete_messages != tLRPC$TL_chatAdminRights2.delete_messages) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.delete_messages ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedDeleteMessages", 2131625734));
                    }
                    if (tLRPC$TL_chatAdminRights.add_admins != tLRPC$TL_chatAdminRights2.add_admins) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.add_admins ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedAddAdmins", 2131625729));
                    }
                    if (tLRPC$TL_chatAdminRights.anonymous != tLRPC$TL_chatAdminRights2.anonymous) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.anonymous ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedSendAnonymously", 2131625741));
                    }
                    if (tLRPC$Chat2.megagroup) {
                        if (tLRPC$TL_chatAdminRights.ban_users != tLRPC$TL_chatAdminRights2.ban_users) {
                            sb3.append('\n');
                            sb3.append(tLRPC$TL_chatAdminRights2.ban_users ? '+' : '-');
                            sb3.append(' ');
                            sb3.append(LocaleController.getString("EventLogPromotedBanUsers", 2131625731));
                        }
                        if (tLRPC$TL_chatAdminRights.manage_call != tLRPC$TL_chatAdminRights2.manage_call) {
                            sb3.append('\n');
                            sb3.append(tLRPC$TL_chatAdminRights2.manage_call ? '+' : '-');
                            sb3.append(' ');
                            sb3.append(LocaleController.getString("EventLogPromotedManageCall", 2131625736));
                        }
                    }
                    if (tLRPC$TL_chatAdminRights.invite_users != tLRPC$TL_chatAdminRights2.invite_users) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.invite_users ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedAddUsers", 2131625730));
                    }
                    if (tLRPC$Chat2.megagroup && tLRPC$TL_chatAdminRights.pin_messages != tLRPC$TL_chatAdminRights2.pin_messages) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.pin_messages ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedPinMessages", 2131625738));
                    }
                    sb = sb3;
                }
                messageObject.messageText = sb.toString();
            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDefaultBannedRights) {
                TLRPC$TL_channelAdminLogEventActionDefaultBannedRights tLRPC$TL_channelAdminLogEventActionDefaultBannedRights = (TLRPC$TL_channelAdminLogEventActionDefaultBannedRights) tLRPC$ChannelAdminLogEventAction;
                messageObject.messageOwner = new TLRPC$TL_message();
                TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights = tLRPC$TL_channelAdminLogEventActionDefaultBannedRights.prev_banned_rights;
                TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2 = tLRPC$TL_channelAdminLogEventActionDefaultBannedRights.new_banned_rights;
                StringBuilder sb4 = new StringBuilder(LocaleController.getString("EventLogDefaultPermissions", 2131625681));
                tLRPC$TL_chatBannedRights = tLRPC$TL_chatBannedRights == null ? new TLRPC$TL_chatBannedRights() : tLRPC$TL_chatBannedRights;
                tLRPC$TL_chatBannedRights2 = tLRPC$TL_chatBannedRights2 == null ? new TLRPC$TL_chatBannedRights() : tLRPC$TL_chatBannedRights2;
                if (tLRPC$TL_chatBannedRights.send_messages != tLRPC$TL_chatBannedRights2.send_messages) {
                    sb4.append('\n');
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.send_messages ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedSendMessages", 2131625757));
                    z4 = true;
                } else {
                    z4 = false;
                }
                if (tLRPC$TL_chatBannedRights.send_stickers != tLRPC$TL_chatBannedRights2.send_stickers || tLRPC$TL_chatBannedRights.send_inline != tLRPC$TL_chatBannedRights2.send_inline || tLRPC$TL_chatBannedRights.send_gifs != tLRPC$TL_chatBannedRights2.send_gifs || tLRPC$TL_chatBannedRights.send_games != tLRPC$TL_chatBannedRights2.send_games) {
                    if (!z4) {
                        sb4.append('\n');
                        z4 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.send_stickers ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedSendStickers", 2131625759));
                }
                if (tLRPC$TL_chatBannedRights.send_media != tLRPC$TL_chatBannedRights2.send_media) {
                    if (!z4) {
                        sb4.append('\n');
                        z4 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.send_media ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedSendMedia", 2131625756));
                }
                if (tLRPC$TL_chatBannedRights.send_polls != tLRPC$TL_chatBannedRights2.send_polls) {
                    if (!z4) {
                        sb4.append('\n');
                        z4 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.send_polls ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedSendPolls", 2131625758));
                }
                if (tLRPC$TL_chatBannedRights.embed_links != tLRPC$TL_chatBannedRights2.embed_links) {
                    if (!z4) {
                        sb4.append('\n');
                        z4 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.embed_links ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedSendEmbed", 2131625755));
                }
                if (tLRPC$TL_chatBannedRights.change_info != tLRPC$TL_chatBannedRights2.change_info) {
                    if (!z4) {
                        sb4.append('\n');
                        z4 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.change_info ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedChangeInfo", 2131625751));
                }
                if (tLRPC$TL_chatBannedRights.invite_users != tLRPC$TL_chatBannedRights2.invite_users) {
                    if (!z4) {
                        sb4.append('\n');
                        z4 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.invite_users ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedInviteUsers", 2131625752));
                }
                if (tLRPC$TL_chatBannedRights.pin_messages != tLRPC$TL_chatBannedRights2.pin_messages) {
                    if (!z4) {
                        sb4.append('\n');
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.pin_messages ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedPinMessages", 2131625753));
                }
                messageObject.messageText = sb4.toString();
            } else {
                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) {
                    TLRPC$TL_channelAdminLogEventActionParticipantToggleBan tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2 = (TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction;
                    messageObject.messageOwner = new TLRPC$TL_message();
                    long peerId3 = getPeerId(tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.prev_participant.peer);
                    if (peerId3 > 0) {
                        messageObject = this;
                        tLObject4 = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId3));
                        str13 = "Minutes";
                    } else {
                        messageObject = this;
                        str13 = "Minutes";
                        tLObject4 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(-peerId3));
                    }
                    TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights3 = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.prev_participant.banned_rights;
                    TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights4 = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.new_participant.banned_rights;
                    if (tLRPC$Chat.megagroup && (tLRPC$TL_chatBannedRights4 == null || !tLRPC$TL_chatBannedRights4.view_messages || (tLRPC$TL_chatBannedRights3 != null && tLRPC$TL_chatBannedRights4.until_date != tLRPC$TL_chatBannedRights3.until_date))) {
                        if (tLRPC$TL_chatBannedRights4 != null && !AndroidUtilities.isBannedForever(tLRPC$TL_chatBannedRights4)) {
                            sb2 = new StringBuilder();
                            int i12 = tLRPC$TL_chatBannedRights4.until_date - tLRPC$TL_channelAdminLogEvent.date;
                            int i13 = ((i12 / 60) / 60) / 24;
                            int i14 = i12 - (((i13 * 60) * 60) * 24);
                            int i15 = (i14 / 60) / 60;
                            int i16 = (i14 - ((i15 * 60) * 60)) / 60;
                            str4 = "";
                            int i17 = 0;
                            int i18 = 0;
                            for (int i19 = 3; i17 < i19; i19 = 3) {
                                if (i17 == 0) {
                                    if (i13 != 0) {
                                        str15 = LocaleController.formatPluralString("Days", i13, new Object[0]);
                                        i8 = i18 + 1;
                                        i7 = i8;
                                        if (str15 != null) {
                                            if (sb2.length() > 0) {
                                                sb2.append(", ");
                                            }
                                            sb2.append(str15);
                                        }
                                        if (i7 == 2) {
                                            break;
                                        }
                                        i17++;
                                        i18 = i7;
                                    }
                                    i7 = i18;
                                    str15 = null;
                                    if (str15 != null) {
                                    }
                                    if (i7 == 2) {
                                    }
                                } else {
                                    if (i17 == 1) {
                                        if (i15 != 0) {
                                            i8 = i18 + 1;
                                            str15 = LocaleController.formatPluralString("Hours", i15, new Object[0]);
                                            i7 = i8;
                                            if (str15 != null) {
                                            }
                                            if (i7 == 2) {
                                            }
                                        }
                                    } else if (i16 != 0) {
                                        str15 = LocaleController.formatPluralString(str13, i16, new Object[0]);
                                        i8 = i18 + 1;
                                        i7 = i8;
                                        if (str15 != null) {
                                        }
                                        if (i7 == 2) {
                                        }
                                    }
                                    i7 = i18;
                                    str15 = null;
                                    if (str15 != null) {
                                    }
                                    if (i7 == 2) {
                                    }
                                }
                            }
                        } else {
                            str4 = "";
                            sb2 = new StringBuilder(LocaleController.getString("UserRestrictionsUntilForever", 2131628840));
                        }
                        String string2 = LocaleController.getString("EventLogRestrictedUntil", 2131625760);
                        StringBuilder sb5 = new StringBuilder(String.format(string2, messageObject.getUserName(tLObject4, messageObject.messageOwner.entities, string2.indexOf("%1$s")), sb2.toString()));
                        tLRPC$TL_chatBannedRights3 = tLRPC$TL_chatBannedRights3 == null ? new TLRPC$TL_chatBannedRights() : tLRPC$TL_chatBannedRights3;
                        tLRPC$TL_chatBannedRights4 = tLRPC$TL_chatBannedRights4 == null ? new TLRPC$TL_chatBannedRights() : tLRPC$TL_chatBannedRights4;
                        if (tLRPC$TL_chatBannedRights3.view_messages != tLRPC$TL_chatBannedRights4.view_messages) {
                            sb5.append('\n');
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.view_messages ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedReadMessages", 2131625754));
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        if (tLRPC$TL_chatBannedRights3.send_messages != tLRPC$TL_chatBannedRights4.send_messages) {
                            if (!z3) {
                                sb5.append('\n');
                                z3 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.send_messages ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendMessages", 2131625757));
                        }
                        if (tLRPC$TL_chatBannedRights3.send_stickers != tLRPC$TL_chatBannedRights4.send_stickers || tLRPC$TL_chatBannedRights3.send_inline != tLRPC$TL_chatBannedRights4.send_inline || tLRPC$TL_chatBannedRights3.send_gifs != tLRPC$TL_chatBannedRights4.send_gifs || tLRPC$TL_chatBannedRights3.send_games != tLRPC$TL_chatBannedRights4.send_games) {
                            if (!z3) {
                                sb5.append('\n');
                                z3 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.send_stickers ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendStickers", 2131625759));
                        }
                        if (tLRPC$TL_chatBannedRights3.send_media != tLRPC$TL_chatBannedRights4.send_media) {
                            if (!z3) {
                                sb5.append('\n');
                                z3 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.send_media ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendMedia", 2131625756));
                        }
                        if (tLRPC$TL_chatBannedRights3.send_polls != tLRPC$TL_chatBannedRights4.send_polls) {
                            if (!z3) {
                                sb5.append('\n');
                                z3 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.send_polls ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendPolls", 2131625758));
                        }
                        if (tLRPC$TL_chatBannedRights3.embed_links != tLRPC$TL_chatBannedRights4.embed_links) {
                            if (!z3) {
                                sb5.append('\n');
                                z3 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.embed_links ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendEmbed", 2131625755));
                        }
                        if (tLRPC$TL_chatBannedRights3.change_info != tLRPC$TL_chatBannedRights4.change_info) {
                            if (!z3) {
                                sb5.append('\n');
                                z3 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.change_info ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedChangeInfo", 2131625751));
                        }
                        if (tLRPC$TL_chatBannedRights3.invite_users != tLRPC$TL_chatBannedRights4.invite_users) {
                            if (!z3) {
                                sb5.append('\n');
                                z3 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.invite_users ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedInviteUsers", 2131625752));
                        }
                        if (tLRPC$TL_chatBannedRights3.pin_messages != tLRPC$TL_chatBannedRights4.pin_messages) {
                            if (!z3) {
                                c3 = '\n';
                                sb5.append('\n');
                            } else {
                                c3 = '\n';
                            }
                            sb5.append(c3);
                            sb5.append(!tLRPC$TL_chatBannedRights4.pin_messages ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedPinMessages", 2131625753));
                        }
                        messageObject.messageText = sb5.toString();
                    } else {
                        str4 = "";
                        if (tLRPC$TL_chatBannedRights4 != null && (tLRPC$TL_chatBannedRights3 == null || tLRPC$TL_chatBannedRights4.view_messages)) {
                            str14 = LocaleController.getString("EventLogChannelRestricted", 2131625679);
                        } else {
                            str14 = LocaleController.getString("EventLogChannelUnrestricted", 2131625680);
                        }
                        messageObject.messageText = String.format(str14, messageObject.getUserName(tLObject4, messageObject.messageOwner.entities, str14.indexOf("%1$s")));
                    }
                } else {
                    str4 = "";
                    if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionUpdatePinned) {
                        TLRPC$TL_channelAdminLogEventActionUpdatePinned tLRPC$TL_channelAdminLogEventActionUpdatePinned = (TLRPC$TL_channelAdminLogEventActionUpdatePinned) tLRPC$ChannelAdminLogEventAction;
                        tLRPC$TL_message = tLRPC$TL_channelAdminLogEventActionUpdatePinned.message;
                        if (user != null && user.id == 136817688 && (tLRPC$MessageFwdHeader = tLRPC$TL_message.fwd_from) != null && (tLRPC$MessageFwdHeader.from_id instanceof TLRPC$TL_peerChannel)) {
                            TLRPC$Chat chat = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(tLRPC$TL_channelAdminLogEventActionUpdatePinned.message.fwd_from.from_id.channel_id));
                            TLRPC$Message tLRPC$Message = tLRPC$TL_channelAdminLogEventActionUpdatePinned.message;
                            if ((tLRPC$Message instanceof TLRPC$TL_messageEmpty) || !tLRPC$Message.pinned) {
                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogUnpinnedMessages", 2131625775), "un1", chat);
                            } else {
                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogPinnedMessages", 2131625724), "un1", chat);
                            }
                        } else if ((tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) || !tLRPC$TL_message.pinned) {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogUnpinnedMessages", 2131625775), "un1", user);
                        } else {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogPinnedMessages", 2131625724), "un1", user);
                        }
                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionStopPoll) {
                        tLRPC$TL_message = ((TLRPC$TL_channelAdminLogEventActionStopPoll) tLRPC$ChannelAdminLogEventAction).message;
                        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$TL_message.media;
                        if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) && ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll.quiz) {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogStopQuiz", 2131625766), "un1", user);
                        } else {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogStopPoll", 2131625765), "un1", user);
                        }
                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleSignatures) {
                        if (((TLRPC$TL_channelAdminLogEventActionToggleSignatures) tLRPC$ChannelAdminLogEventAction).new_value) {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogToggledSignaturesOn", 2131625772), "un1", user);
                        } else {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogToggledSignaturesOff", 2131625771), "un1", user);
                        }
                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleInvites) {
                        if (((TLRPC$TL_channelAdminLogEventActionToggleInvites) tLRPC$ChannelAdminLogEventAction).new_value) {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesOn", 2131625770), "un1", user);
                        } else {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesOff", 2131625769), "un1", user);
                        }
                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                        tLRPC$TL_message = ((TLRPC$TL_channelAdminLogEventActionDeleteMessage) tLRPC$ChannelAdminLogEventAction).message;
                        messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogDeletedMessages", 2131625682), "un1", user);
                    } else {
                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeLinkedChat) {
                            long j = ((TLRPC$TL_channelAdminLogEventActionChangeLinkedChat) tLRPC$ChannelAdminLogEventAction).new_value;
                            long j2 = ((TLRPC$TL_channelAdminLogEventActionChangeLinkedChat) tLRPC$ChannelAdminLogEventAction).prev_value;
                            tLRPC$Chat2 = tLRPC$Chat;
                            if (tLRPC$Chat2.megagroup) {
                                if (j == 0) {
                                    TLRPC$Chat chat2 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(j2));
                                    CharSequence replaceWithLink2 = replaceWithLink(LocaleController.getString("EventLogRemovedLinkedChannel", 2131625746), "un1", user);
                                    messageObject.messageText = replaceWithLink2;
                                    messageObject.messageText = replaceWithLink(replaceWithLink2, "un2", chat2);
                                } else {
                                    TLRPC$Chat chat3 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(j));
                                    CharSequence replaceWithLink3 = replaceWithLink(LocaleController.getString("EventLogChangedLinkedChannel", 2131625673), "un1", user);
                                    messageObject.messageText = replaceWithLink3;
                                    messageObject.messageText = replaceWithLink(replaceWithLink3, "un2", chat3);
                                }
                            } else if (j == 0) {
                                TLRPC$Chat chat4 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(j2));
                                CharSequence replaceWithLink4 = replaceWithLink(LocaleController.getString("EventLogRemovedLinkedGroup", 2131625747), "un1", user);
                                messageObject.messageText = replaceWithLink4;
                                messageObject.messageText = replaceWithLink(replaceWithLink4, "un2", chat4);
                            } else {
                                TLRPC$Chat chat5 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(j));
                                CharSequence replaceWithLink5 = replaceWithLink(LocaleController.getString("EventLogChangedLinkedGroup", 2131625674), "un1", user);
                                messageObject.messageText = replaceWithLink5;
                                messageObject.messageText = replaceWithLink(replaceWithLink5, "un2", chat5);
                            }
                        } else {
                            tLRPC$Chat2 = tLRPC$Chat;
                            if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionTogglePreHistoryHidden) {
                                if (((TLRPC$TL_channelAdminLogEventActionTogglePreHistoryHidden) tLRPC$ChannelAdminLogEventAction).new_value) {
                                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesHistoryOff", 2131625767), "un1", user);
                                } else {
                                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesHistoryOn", 2131625768), "un1", user);
                                }
                            } else {
                                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeAbout) {
                                    if (tLRPC$Chat2.megagroup) {
                                        i6 = 2131625689;
                                        str12 = "EventLogEditedGroupDescription";
                                    } else {
                                        i6 = 2131625684;
                                        str12 = "EventLogEditedChannelDescription";
                                    }
                                    messageObject.messageText = replaceWithLink(LocaleController.getString(str12, i6), "un1", user);
                                    tLRPC$TL_message = new TLRPC$TL_message();
                                    tLRPC$TL_message.out = false;
                                    tLRPC$TL_message.unread = false;
                                    TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                                    tLRPC$TL_message.from_id = tLRPC$TL_peerUser;
                                    tLRPC$TL_channelAdminLogEvent3 = tLRPC$TL_channelAdminLogEvent;
                                    tLRPC$TL_peerUser.user_id = tLRPC$TL_channelAdminLogEvent3.user_id;
                                    tLRPC$TL_message.peer_id = tLRPC$TL_peerChannel;
                                    tLRPC$TL_message.date = tLRPC$TL_channelAdminLogEvent3.date;
                                    TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction2 = tLRPC$TL_channelAdminLogEvent3.action;
                                    tLRPC$TL_message.message = ((TLRPC$TL_channelAdminLogEventActionChangeAbout) tLRPC$ChannelAdminLogEventAction2).new_value;
                                    if (!TextUtils.isEmpty(((TLRPC$TL_channelAdminLogEventActionChangeAbout) tLRPC$ChannelAdminLogEventAction2).prev_value)) {
                                        TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage = new TLRPC$TL_messageMediaWebPage();
                                        tLRPC$TL_message.media = tLRPC$TL_messageMediaWebPage;
                                        tLRPC$TL_messageMediaWebPage.webpage = new TLRPC$TL_webPage();
                                        TLRPC$WebPage tLRPC$WebPage = tLRPC$TL_message.media.webpage;
                                        tLRPC$WebPage.flags = 10;
                                        str5 = str4;
                                        tLRPC$WebPage.display_url = str5;
                                        tLRPC$WebPage.url = str5;
                                        tLRPC$WebPage.site_name = LocaleController.getString("EventLogPreviousGroupDescription", 2131625725);
                                        tLRPC$TL_message.media.webpage.description = ((TLRPC$TL_channelAdminLogEventActionChangeAbout) tLRPC$TL_channelAdminLogEvent3.action).prev_value;
                                    } else {
                                        str5 = str4;
                                        tLRPC$TL_message.media = new TLRPC$TL_messageMediaEmpty();
                                    }
                                } else {
                                    tLRPC$TL_channelAdminLogEvent3 = tLRPC$TL_channelAdminLogEvent;
                                    str5 = str4;
                                    if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeTheme) {
                                        if (tLRPC$Chat2.megagroup) {
                                            i5 = 2131625691;
                                            str11 = "EventLogEditedGroupTheme";
                                        } else {
                                            i5 = 2131625686;
                                            str11 = "EventLogEditedChannelTheme";
                                        }
                                        messageObject.messageText = replaceWithLink(LocaleController.getString(str11, i5), "un1", user);
                                        tLRPC$TL_message = new TLRPC$TL_message();
                                        tLRPC$TL_message.out = false;
                                        tLRPC$TL_message.unread = false;
                                        TLRPC$TL_peerUser tLRPC$TL_peerUser2 = new TLRPC$TL_peerUser();
                                        tLRPC$TL_message.from_id = tLRPC$TL_peerUser2;
                                        tLRPC$TL_peerUser2.user_id = tLRPC$TL_channelAdminLogEvent3.user_id;
                                        tLRPC$TL_message.peer_id = tLRPC$TL_peerChannel;
                                        tLRPC$TL_message.date = tLRPC$TL_channelAdminLogEvent3.date;
                                        TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction3 = tLRPC$TL_channelAdminLogEvent3.action;
                                        tLRPC$TL_message.message = ((TLRPC$TL_channelAdminLogEventActionChangeTheme) tLRPC$ChannelAdminLogEventAction3).new_value;
                                        if (!TextUtils.isEmpty(((TLRPC$TL_channelAdminLogEventActionChangeTheme) tLRPC$ChannelAdminLogEventAction3).prev_value)) {
                                            TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage2 = new TLRPC$TL_messageMediaWebPage();
                                            tLRPC$TL_message.media = tLRPC$TL_messageMediaWebPage2;
                                            tLRPC$TL_messageMediaWebPage2.webpage = new TLRPC$TL_webPage();
                                            TLRPC$WebPage tLRPC$WebPage2 = tLRPC$TL_message.media.webpage;
                                            tLRPC$WebPage2.flags = 10;
                                            tLRPC$WebPage2.display_url = str5;
                                            tLRPC$WebPage2.url = str5;
                                            tLRPC$WebPage2.site_name = LocaleController.getString("EventLogPreviousGroupTheme", 2131625726);
                                            tLRPC$TL_message.media.webpage.description = ((TLRPC$TL_channelAdminLogEventActionChangeTheme) tLRPC$TL_channelAdminLogEvent3.action).prev_value;
                                        } else {
                                            tLRPC$TL_message.media = new TLRPC$TL_messageMediaEmpty();
                                        }
                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeUsername) {
                                        String str17 = ((TLRPC$TL_channelAdminLogEventActionChangeUsername) tLRPC$ChannelAdminLogEventAction).new_value;
                                        if (!TextUtils.isEmpty(str17)) {
                                            if (tLRPC$Chat2.megagroup) {
                                                i4 = 2131625672;
                                                str10 = "EventLogChangedGroupLink";
                                            } else {
                                                i4 = 2131625671;
                                                str10 = "EventLogChangedChannelLink";
                                            }
                                            messageObject.messageText = replaceWithLink(LocaleController.getString(str10, i4), "un1", user);
                                        } else {
                                            if (tLRPC$Chat2.megagroup) {
                                                i3 = 2131625745;
                                                str9 = "EventLogRemovedGroupLink";
                                            } else {
                                                i3 = 2131625743;
                                                str9 = "EventLogRemovedChannelLink";
                                            }
                                            messageObject.messageText = replaceWithLink(LocaleController.getString(str9, i3), "un1", user);
                                        }
                                        TLRPC$TL_message tLRPC$TL_message2 = new TLRPC$TL_message();
                                        tLRPC$TL_message2.out = false;
                                        tLRPC$TL_message2.unread = false;
                                        TLRPC$TL_peerUser tLRPC$TL_peerUser3 = new TLRPC$TL_peerUser();
                                        tLRPC$TL_message2.from_id = tLRPC$TL_peerUser3;
                                        tLRPC$TL_peerUser3.user_id = tLRPC$TL_channelAdminLogEvent3.user_id;
                                        tLRPC$TL_message2.peer_id = tLRPC$TL_peerChannel;
                                        tLRPC$TL_message2.date = tLRPC$TL_channelAdminLogEvent3.date;
                                        if (!TextUtils.isEmpty(str17)) {
                                            tLRPC$TL_message2.message = "https://" + MessagesController.getInstance(messageObject.currentAccount).linkPrefix + "/" + str17;
                                        } else {
                                            tLRPC$TL_message2.message = str5;
                                        }
                                        TLRPC$TL_messageEntityUrl tLRPC$TL_messageEntityUrl = new TLRPC$TL_messageEntityUrl();
                                        tLRPC$TL_messageEntityUrl.offset = 0;
                                        tLRPC$TL_messageEntityUrl.length = tLRPC$TL_message2.message.length();
                                        tLRPC$TL_message2.entities.add(tLRPC$TL_messageEntityUrl);
                                        if (!TextUtils.isEmpty(((TLRPC$TL_channelAdminLogEventActionChangeUsername) tLRPC$TL_channelAdminLogEvent3.action).prev_value)) {
                                            TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage3 = new TLRPC$TL_messageMediaWebPage();
                                            tLRPC$TL_message2.media = tLRPC$TL_messageMediaWebPage3;
                                            tLRPC$TL_messageMediaWebPage3.webpage = new TLRPC$TL_webPage();
                                            TLRPC$WebPage tLRPC$WebPage3 = tLRPC$TL_message2.media.webpage;
                                            tLRPC$WebPage3.flags = 10;
                                            tLRPC$WebPage3.display_url = str5;
                                            tLRPC$WebPage3.url = str5;
                                            tLRPC$WebPage3.site_name = LocaleController.getString("EventLogPreviousLink", 2131625727);
                                            tLRPC$TL_message2.media.webpage.description = "https://" + MessagesController.getInstance(messageObject.currentAccount).linkPrefix + "/" + ((TLRPC$TL_channelAdminLogEventActionChangeUsername) tLRPC$TL_channelAdminLogEvent3.action).prev_value;
                                        } else {
                                            tLRPC$TL_message2.media = new TLRPC$TL_messageMediaEmpty();
                                        }
                                        tLRPC$TL_message = tLRPC$TL_message2;
                                    } else {
                                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionEditMessage) {
                                            tLRPC$TL_message = new TLRPC$TL_message();
                                            tLRPC$TL_message.out = false;
                                            tLRPC$TL_message.unread = false;
                                            tLRPC$TL_message.peer_id = tLRPC$TL_peerChannel;
                                            tLRPC$TL_message.date = tLRPC$TL_channelAdminLogEvent3.date;
                                            TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction4 = tLRPC$TL_channelAdminLogEvent3.action;
                                            TLRPC$Message tLRPC$Message2 = ((TLRPC$TL_channelAdminLogEventActionEditMessage) tLRPC$ChannelAdminLogEventAction4).new_message;
                                            TLRPC$Message tLRPC$Message3 = ((TLRPC$TL_channelAdminLogEventActionEditMessage) tLRPC$ChannelAdminLogEventAction4).prev_message;
                                            if (tLRPC$Message2 != null && (tLRPC$Peer = tLRPC$Message2.from_id) != null) {
                                                tLRPC$TL_message.from_id = tLRPC$Peer;
                                            } else {
                                                TLRPC$TL_peerUser tLRPC$TL_peerUser4 = new TLRPC$TL_peerUser();
                                                tLRPC$TL_message.from_id = tLRPC$TL_peerUser4;
                                                tLRPC$TL_peerUser4.user_id = tLRPC$TL_channelAdminLogEvent3.user_id;
                                            }
                                            TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$Message2.media;
                                            if (tLRPC$MessageMedia2 != null && !(tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaEmpty) && !(tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaWebPage)) {
                                                boolean z5 = !TextUtils.equals(tLRPC$Message2.message, tLRPC$Message3.message);
                                                if (tLRPC$Message2.media.getClass() == tLRPC$Message3.media.getClass()) {
                                                    TLRPC$MessageMedia tLRPC$MessageMedia3 = tLRPC$Message2.media;
                                                    TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia3.photo;
                                                    if (tLRPC$Photo == null || (r11 = tLRPC$Message3.media.photo) == null) {
                                                        str8 = str5;
                                                    } else {
                                                        str8 = str5;
                                                    }
                                                    TLRPC$Document tLRPC$Document2 = tLRPC$MessageMedia3.document;
                                                    if (tLRPC$Document2 == null || (tLRPC$Document = tLRPC$Message3.media.document) == null || tLRPC$Document2.id == tLRPC$Document.id) {
                                                        z2 = false;
                                                        if (!z2 && z5) {
                                                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedMediaCaption", 2131625695), "un1", user);
                                                        } else if (!z5) {
                                                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedCaption", 2131625683), "un1", user);
                                                        } else {
                                                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedMedia", 2131625694), "un1", user);
                                                        }
                                                        TLRPC$MessageMedia tLRPC$MessageMedia4 = tLRPC$Message2.media;
                                                        tLRPC$TL_message.media = tLRPC$MessageMedia4;
                                                        if (z5) {
                                                            tLRPC$MessageMedia4.webpage = new TLRPC$TL_webPage();
                                                            tLRPC$TL_message.media.webpage.site_name = LocaleController.getString("EventLogOriginalCaption", 2131625721);
                                                            if (TextUtils.isEmpty(tLRPC$Message3.message)) {
                                                                tLRPC$TL_message.media.webpage.description = LocaleController.getString("EventLogOriginalCaptionEmpty", 2131625722);
                                                            } else {
                                                                tLRPC$TL_message.media.webpage.description = tLRPC$Message3.message;
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    str8 = str5;
                                                }
                                                z2 = true;
                                                if (!z2) {
                                                }
                                                if (!z5) {
                                                }
                                                TLRPC$MessageMedia tLRPC$MessageMedia42 = tLRPC$Message2.media;
                                                tLRPC$TL_message.media = tLRPC$MessageMedia42;
                                                if (z5) {
                                                }
                                            } else {
                                                str8 = str5;
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedMessages", 2131625696), "un1", user);
                                                if (tLRPC$Message2.action instanceof TLRPC$TL_messageActionGroupCall) {
                                                    tLRPC$Message2.media = new TLRPC$TL_messageMediaEmpty();
                                                    tLRPC$TL_message = tLRPC$Message2;
                                                } else {
                                                    tLRPC$TL_message.message = tLRPC$Message2.message;
                                                    TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage4 = new TLRPC$TL_messageMediaWebPage();
                                                    tLRPC$TL_message.media = tLRPC$TL_messageMediaWebPage4;
                                                    tLRPC$TL_messageMediaWebPage4.webpage = new TLRPC$TL_webPage();
                                                    tLRPC$TL_message.media.webpage.site_name = LocaleController.getString("EventLogOriginalMessages", 2131625723);
                                                    if (TextUtils.isEmpty(tLRPC$Message3.message)) {
                                                        tLRPC$TL_message.media.webpage.description = LocaleController.getString("EventLogOriginalCaptionEmpty", 2131625722);
                                                    } else {
                                                        tLRPC$TL_message.media.webpage.description = tLRPC$Message3.message;
                                                    }
                                                }
                                            }
                                            tLRPC$TL_message.reply_markup = tLRPC$Message2.reply_markup;
                                            TLRPC$WebPage tLRPC$WebPage4 = tLRPC$TL_message.media.webpage;
                                            if (tLRPC$WebPage4 != null) {
                                                tLRPC$WebPage4.flags = 10;
                                                str = str8;
                                                tLRPC$WebPage4.display_url = str;
                                                tLRPC$WebPage4.url = str;
                                            } else {
                                                str = str8;
                                            }
                                        } else {
                                            str = str5;
                                            if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeStickerSet) {
                                                TLRPC$InputStickerSet tLRPC$InputStickerSet = ((TLRPC$TL_channelAdminLogEventActionChangeStickerSet) tLRPC$ChannelAdminLogEventAction).new_stickerset;
                                                TLRPC$InputStickerSet tLRPC$InputStickerSet2 = ((TLRPC$TL_channelAdminLogEventActionChangeStickerSet) tLRPC$ChannelAdminLogEventAction).new_stickerset;
                                                if (tLRPC$InputStickerSet == null || (tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty)) {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedStickersSet", 2131625749), "un1", user);
                                                } else {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogChangedStickersSet", 2131625677), "un1", user);
                                                }
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeLocation) {
                                                TLRPC$ChannelLocation tLRPC$ChannelLocation = ((TLRPC$TL_channelAdminLogEventActionChangeLocation) tLRPC$ChannelAdminLogEventAction).new_value;
                                                if (tLRPC$ChannelLocation instanceof TLRPC$TL_channelLocationEmpty) {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedLocation", 2131625748), "un1", user);
                                                } else {
                                                    messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogChangedLocation", 2131625675, ((TLRPC$TL_channelLocation) tLRPC$ChannelLocation).address), "un1", user);
                                                }
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleSlowMode) {
                                                int i20 = ((TLRPC$TL_channelAdminLogEventActionToggleSlowMode) tLRPC$ChannelAdminLogEventAction).new_value;
                                                if (i20 == 0) {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogToggledSlowmodeOff", 2131625773), "un1", user);
                                                } else {
                                                    if (i20 < 60) {
                                                        c2 = 0;
                                                        str7 = LocaleController.formatPluralString("Seconds", i20, new Object[0]);
                                                    } else {
                                                        c2 = 0;
                                                        if (i20 < 3600) {
                                                            str7 = LocaleController.formatPluralString("Minutes", i20 / 60, new Object[0]);
                                                        } else {
                                                            str7 = LocaleController.formatPluralString("Hours", (i20 / 60) / 60, new Object[0]);
                                                        }
                                                    }
                                                    Object[] objArr = new Object[1];
                                                    objArr[c2] = str7;
                                                    messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogToggledSlowmodeOn", 2131625774, objArr), "un1", user);
                                                }
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionStartGroupCall) {
                                                if (ChatObject.isChannel(tLRPC$Chat) && (!tLRPC$Chat2.megagroup || tLRPC$Chat2.gigagroup)) {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogStartedLiveStream", 2131625763), "un1", user);
                                                } else {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogStartedVoiceChat", 2131625764), "un1", user);
                                                }
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDiscardGroupCall) {
                                                if (ChatObject.isChannel(tLRPC$Chat) && (!tLRPC$Chat2.megagroup || tLRPC$Chat2.gigagroup)) {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEndedLiveStream", 2131625701), "un1", user);
                                                } else {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEndedVoiceChat", 2131625702), "un1", user);
                                                }
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantMute) {
                                                long peerId4 = getPeerId(((TLRPC$TL_channelAdminLogEventActionParticipantMute) tLRPC$ChannelAdminLogEventAction).participant.peer);
                                                if (peerId4 > 0) {
                                                    tLObject3 = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId4));
                                                } else {
                                                    tLObject3 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(-peerId4));
                                                }
                                                CharSequence replaceWithLink6 = replaceWithLink(LocaleController.getString("EventLogVoiceChatMuted", 2131625777), "un1", user);
                                                messageObject.messageText = replaceWithLink6;
                                                messageObject.messageText = replaceWithLink(replaceWithLink6, "un2", tLObject3);
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantUnmute) {
                                                long peerId5 = getPeerId(((TLRPC$TL_channelAdminLogEventActionParticipantUnmute) tLRPC$ChannelAdminLogEventAction).participant.peer);
                                                if (peerId5 > 0) {
                                                    tLObject2 = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId5));
                                                } else {
                                                    tLObject2 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(-peerId5));
                                                }
                                                CharSequence replaceWithLink7 = replaceWithLink(LocaleController.getString("EventLogVoiceChatUnmuted", 2131625779), "un1", user);
                                                messageObject.messageText = replaceWithLink7;
                                                messageObject.messageText = replaceWithLink(replaceWithLink7, "un2", tLObject2);
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleGroupCallSetting) {
                                                if (((TLRPC$TL_channelAdminLogEventActionToggleGroupCallSetting) tLRPC$ChannelAdminLogEventAction).join_muted) {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogVoiceChatNotAllowedToSpeak", 2131625778), "un1", user);
                                                } else {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogVoiceChatAllowedToSpeak", 2131625776), "un1", user);
                                                }
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite) {
                                                TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite tLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite = (TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite) tLRPC$ChannelAdminLogEventAction;
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("ActionInviteUser", 2131624184), "un1", user);
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleNoForwards) {
                                                TLRPC$TL_channelAdminLogEventActionToggleNoForwards tLRPC$TL_channelAdminLogEventActionToggleNoForwards = (TLRPC$TL_channelAdminLogEventActionToggleNoForwards) tLRPC$ChannelAdminLogEventAction;
                                                boolean z6 = ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat2.megagroup;
                                                if (tLRPC$TL_channelAdminLogEventActionToggleNoForwards.new_value) {
                                                    if (z6) {
                                                        messageObject.messageText = replaceWithLink(LocaleController.getString("ActionForwardsRestrictedChannel", 2131624172), "un1", user);
                                                    } else {
                                                        messageObject.messageText = replaceWithLink(LocaleController.getString("ActionForwardsRestrictedGroup", 2131624173), "un1", user);
                                                    }
                                                } else if (z6) {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("ActionForwardsEnabledChannel", 2131624170), "un1", user);
                                                } else {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("ActionForwardsEnabledGroup", 2131624171), "un1", user);
                                                }
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionExportedInviteDelete) {
                                                CharSequence replaceWithLink8 = replaceWithLink(LocaleController.formatString("ActionDeletedInviteLinkClickable", 2131624167, new Object[0]), "un1", user);
                                                messageObject.messageText = replaceWithLink8;
                                                messageObject.messageText = replaceWithLink(replaceWithLink8, "un2", ((TLRPC$TL_channelAdminLogEventActionExportedInviteDelete) tLRPC$ChannelAdminLogEventAction).invite);
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionExportedInviteRevoke) {
                                                TLRPC$TL_channelAdminLogEventActionExportedInviteRevoke tLRPC$TL_channelAdminLogEventActionExportedInviteRevoke = (TLRPC$TL_channelAdminLogEventActionExportedInviteRevoke) tLRPC$ChannelAdminLogEventAction;
                                                CharSequence replaceWithLink9 = replaceWithLink(LocaleController.formatString("ActionRevokedInviteLinkClickable", 2131624209, tLRPC$TL_channelAdminLogEventActionExportedInviteRevoke.invite.link), "un1", user);
                                                messageObject.messageText = replaceWithLink9;
                                                messageObject.messageText = replaceWithLink(replaceWithLink9, "un2", tLRPC$TL_channelAdminLogEventActionExportedInviteRevoke.invite);
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionExportedInviteEdit) {
                                                TLRPC$TL_channelAdminLogEventActionExportedInviteEdit tLRPC$TL_channelAdminLogEventActionExportedInviteEdit = (TLRPC$TL_channelAdminLogEventActionExportedInviteEdit) tLRPC$ChannelAdminLogEventAction;
                                                String str18 = tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.prev_invite.link;
                                                if (str18 != null && str18.equals(tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.new_invite.link)) {
                                                    messageObject.messageText = replaceWithLink(LocaleController.formatString("ActionEditedInviteLinkToSameClickable", 2131624169, new Object[0]), "un1", user);
                                                } else {
                                                    messageObject.messageText = replaceWithLink(LocaleController.formatString("ActionEditedInviteLinkClickable", 2131624168, new Object[0]), "un1", user);
                                                }
                                                CharSequence replaceWithLink10 = replaceWithLink(messageObject.messageText, "un2", tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.prev_invite);
                                                messageObject.messageText = replaceWithLink10;
                                                messageObject.messageText = replaceWithLink(replaceWithLink10, "un3", tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.new_invite);
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantVolume) {
                                                TLRPC$TL_channelAdminLogEventActionParticipantVolume tLRPC$TL_channelAdminLogEventActionParticipantVolume = (TLRPC$TL_channelAdminLogEventActionParticipantVolume) tLRPC$ChannelAdminLogEventAction;
                                                long peerId6 = getPeerId(tLRPC$TL_channelAdminLogEventActionParticipantVolume.participant.peer);
                                                if (peerId6 > 0) {
                                                    tLObject = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId6));
                                                } else {
                                                    tLObject = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(-peerId6));
                                                }
                                                double participantVolume = ChatObject.getParticipantVolume(tLRPC$TL_channelAdminLogEventActionParticipantVolume.participant);
                                                Double.isNaN(participantVolume);
                                                double d = participantVolume / 100.0d;
                                                Object[] objArr2 = new Object[1];
                                                objArr2[0] = Integer.valueOf((int) (d > 0.0d ? Math.max(d, 1.0d) : 0.0d));
                                                CharSequence replaceWithLink11 = replaceWithLink(LocaleController.formatString("ActionVolumeChanged", 2131624225, objArr2), "un1", user);
                                                messageObject.messageText = replaceWithLink11;
                                                messageObject.messageText = replaceWithLink(replaceWithLink11, "un2", tLObject);
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL) {
                                                TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL = (TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL) tLRPC$ChannelAdminLogEventAction;
                                                if (!tLRPC$Chat2.megagroup) {
                                                    int i21 = tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL.new_value;
                                                    if (i21 != 0) {
                                                        messageObject.messageText = LocaleController.formatString("ActionTTLChannelChanged", 2131624211, LocaleController.formatTTLString(i21));
                                                    } else {
                                                        messageObject.messageText = LocaleController.getString("ActionTTLChannelDisabled", 2131624212);
                                                    }
                                                } else {
                                                    int i22 = tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL.new_value;
                                                    if (i22 == 0) {
                                                        messageObject.messageText = replaceWithLink(LocaleController.getString("ActionTTLDisabled", 2131624213), "un1", user);
                                                    } else {
                                                        if (i22 > 86400) {
                                                            c = 0;
                                                            str6 = LocaleController.formatPluralString("Days", i22 / 86400, new Object[0]);
                                                        } else {
                                                            c = 0;
                                                            if (i22 >= 3600) {
                                                                str6 = LocaleController.formatPluralString("Hours", i22 / 3600, new Object[0]);
                                                            } else if (i22 >= 60) {
                                                                str6 = LocaleController.formatPluralString("Minutes", i22 / 60, new Object[0]);
                                                            } else {
                                                                str6 = LocaleController.formatPluralString("Seconds", i22, new Object[0]);
                                                            }
                                                        }
                                                        Object[] objArr3 = new Object[1];
                                                        objArr3[c] = str6;
                                                        messageObject.messageText = replaceWithLink(LocaleController.formatString("ActionTTLChanged", 2131624210, objArr3), "un1", user);
                                                    }
                                                }
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest) {
                                                TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest = (TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest) tLRPC$ChannelAdminLogEventAction;
                                                TLRPC$ExportedChatInvite tLRPC$ExportedChatInvite = tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite;
                                                if (((tLRPC$ExportedChatInvite instanceof TLRPC$TL_chatInviteExported) && "https://t.me/+PublicChat".equals(((TLRPC$TL_chatInviteExported) tLRPC$ExportedChatInvite).link)) || (tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite instanceof TLRPC$TL_chatInvitePublicJoinRequests)) {
                                                    CharSequence replaceWithLink12 = replaceWithLink(LocaleController.getString("JoinedViaRequestApproved", 2131626333), "un1", user);
                                                    messageObject.messageText = replaceWithLink12;
                                                    messageObject.messageText = replaceWithLink(replaceWithLink12, "un2", MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.approved_by)));
                                                } else {
                                                    CharSequence replaceWithLink13 = replaceWithLink(LocaleController.getString("JoinedViaInviteLinkApproved", 2131626332), "un1", user);
                                                    messageObject.messageText = replaceWithLink13;
                                                    CharSequence replaceWithLink14 = replaceWithLink(replaceWithLink13, "un2", tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite);
                                                    messageObject.messageText = replaceWithLink14;
                                                    messageObject.messageText = replaceWithLink(replaceWithLink14, "un3", MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.approved_by)));
                                                }
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionSendMessage) {
                                                tLRPC$TL_message = ((TLRPC$TL_channelAdminLogEventActionSendMessage) tLRPC$ChannelAdminLogEventAction).message;
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogSendMessages", 2131625762), "un1", user);
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions) {
                                                tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                                messageObject.messageText = replaceWithLink(LocaleController.formatString("ActionReactionsChanged", 2131624207, TextUtils.join(", ", ((TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions) tLRPC$ChannelAdminLogEventAction).prev_value), TextUtils.join(", ", ((TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions) tLRPC$TL_channelAdminLogEvent2.action).new_value)), "un1", user);
                                            } else {
                                                tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                                messageObject.messageText = "unsupported " + tLRPC$TL_channelAdminLogEvent2.action;
                                            }
                                            tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                        }
                                        tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                        if (messageObject.messageOwner == null) {
                                            messageObject.messageOwner = new TLRPC$TL_messageService();
                                        }
                                        messageObject.messageOwner.message = messageObject.messageText.toString();
                                        messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                                        TLRPC$Message tLRPC$Message4 = messageObject.messageOwner;
                                        tLRPC$Message4.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                        tLRPC$Message4.date = tLRPC$TL_channelAdminLogEvent2.date;
                                        int i23 = iArr[0];
                                        iArr[0] = i23 + 1;
                                        tLRPC$Message4.id = i23;
                                        messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                        tLRPC$Message4.out = false;
                                        tLRPC$Message4.peer_id = new TLRPC$TL_peerChannel();
                                        TLRPC$Message tLRPC$Message5 = messageObject.messageOwner;
                                        tLRPC$Message5.peer_id.channel_id = tLRPC$Chat2.id;
                                        tLRPC$Message5.unread = false;
                                        MediaController mediaController = MediaController.getInstance();
                                        tLRPC$TL_message = tLRPC$TL_message instanceof TLRPC$TL_messageEmpty ? null : tLRPC$TL_message;
                                        if (tLRPC$TL_message != null) {
                                            tLRPC$TL_message.out = false;
                                            int i24 = iArr[0];
                                            iArr[0] = i24 + 1;
                                            tLRPC$TL_message.id = i24;
                                            int i25 = tLRPC$TL_message.flags & (-9);
                                            tLRPC$TL_message.flags = i25;
                                            iArr2 = null;
                                            tLRPC$TL_message.reply_to = null;
                                            tLRPC$TL_message.flags = i25 & (-32769);
                                            MessageObject messageObject2 = new MessageObject(messageObject.currentAccount, (TLRPC$Message) tLRPC$TL_message, (AbstractMap<Long, TLRPC$User>) null, (AbstractMap<Long, TLRPC$Chat>) null, true, true, messageObject.eventId);
                                            if (messageObject2.contentType >= 0) {
                                                if (mediaController.isPlayingMessage(messageObject2)) {
                                                    MessageObject playingMessageObject = mediaController.getPlayingMessageObject();
                                                    messageObject2.audioProgress = playingMessageObject.audioProgress;
                                                    messageObject2.audioProgressSec = playingMessageObject.audioProgressSec;
                                                }
                                                createDateArray(messageObject.currentAccount, tLRPC$TL_channelAdminLogEvent, arrayList, hashMap, z);
                                                if (z) {
                                                    arrayList2 = arrayList;
                                                    arrayList2.add(0, messageObject2);
                                                } else {
                                                    arrayList2 = arrayList;
                                                    arrayList2.add(arrayList.size() - 1, messageObject2);
                                                }
                                            } else {
                                                arrayList2 = arrayList;
                                                messageObject.contentType = -1;
                                            }
                                        } else {
                                            arrayList2 = arrayList;
                                            iArr2 = null;
                                        }
                                        if (messageObject.contentType < 0) {
                                            return;
                                        }
                                        createDateArray(messageObject.currentAccount, tLRPC$TL_channelAdminLogEvent, arrayList, hashMap, z);
                                        if (z) {
                                            arrayList2.add(0, messageObject);
                                        } else {
                                            arrayList2.add(arrayList.size() - 1, messageObject);
                                        }
                                        if (messageObject.messageText == null) {
                                            messageObject.messageText = str;
                                        }
                                        setType();
                                        measureInlineBotButtons();
                                        generateCaption();
                                        if (messageObject.messageOwner.media instanceof TLRPC$TL_messageMediaGame) {
                                            textPaint = Theme.chat_msgGameTextPaint;
                                        } else {
                                            textPaint = Theme.chat_msgTextPaint;
                                        }
                                        iArr2 = allowsBigEmoji() ? new int[1] : iArr2;
                                        messageObject.messageText = Emoji.replaceEmoji(messageObject.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, iArr2, messageObject.contentType == 0, messageObject.viewRef);
                                        messageObject.checkEmojiOnly(iArr2);
                                        if (mediaController.isPlayingMessage(messageObject)) {
                                            MessageObject playingMessageObject2 = mediaController.getPlayingMessageObject();
                                            messageObject.audioProgress = playingMessageObject2.audioProgress;
                                            messageObject.audioProgressSec = playingMessageObject2.audioProgressSec;
                                        }
                                        messageObject.generateLayout(user);
                                        messageObject.layoutCreated = true;
                                        messageObject.generateThumbs(false);
                                        checkMediaExistance();
                                        return;
                                    }
                                }
                                String str19 = str5;
                                tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent3;
                                str = str19;
                                if (messageObject.messageOwner == null) {
                                }
                                messageObject.messageOwner.message = messageObject.messageText.toString();
                                messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                                TLRPC$Message tLRPC$Message42 = messageObject.messageOwner;
                                tLRPC$Message42.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                tLRPC$Message42.date = tLRPC$TL_channelAdminLogEvent2.date;
                                int i232 = iArr[0];
                                iArr[0] = i232 + 1;
                                tLRPC$Message42.id = i232;
                                messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                tLRPC$Message42.out = false;
                                tLRPC$Message42.peer_id = new TLRPC$TL_peerChannel();
                                TLRPC$Message tLRPC$Message52 = messageObject.messageOwner;
                                tLRPC$Message52.peer_id.channel_id = tLRPC$Chat2.id;
                                tLRPC$Message52.unread = false;
                                MediaController mediaController2 = MediaController.getInstance();
                                if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                                }
                                if (tLRPC$TL_message != null) {
                                }
                                if (messageObject.contentType < 0) {
                                }
                            }
                        }
                        tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                        str = str4;
                    }
                    tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                    tLRPC$Chat2 = tLRPC$Chat;
                    str = str4;
                    if (messageObject.messageOwner == null) {
                    }
                    messageObject.messageOwner.message = messageObject.messageText.toString();
                    messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                    TLRPC$Message tLRPC$Message422 = messageObject.messageOwner;
                    tLRPC$Message422.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                    tLRPC$Message422.date = tLRPC$TL_channelAdminLogEvent2.date;
                    int i2322 = iArr[0];
                    iArr[0] = i2322 + 1;
                    tLRPC$Message422.id = i2322;
                    messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                    tLRPC$Message422.out = false;
                    tLRPC$Message422.peer_id = new TLRPC$TL_peerChannel();
                    TLRPC$Message tLRPC$Message522 = messageObject.messageOwner;
                    tLRPC$Message522.peer_id.channel_id = tLRPC$Chat2.id;
                    tLRPC$Message522.unread = false;
                    MediaController mediaController22 = MediaController.getInstance();
                    if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                    }
                    if (tLRPC$TL_message != null) {
                    }
                    if (messageObject.contentType < 0) {
                    }
                }
                tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                tLRPC$Chat2 = tLRPC$Chat;
                str = str4;
            }
            tLRPC$TL_message = null;
            if (messageObject.messageOwner == null) {
            }
            messageObject.messageOwner.message = messageObject.messageText.toString();
            messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
            TLRPC$Message tLRPC$Message4222 = messageObject.messageOwner;
            tLRPC$Message4222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
            tLRPC$Message4222.date = tLRPC$TL_channelAdminLogEvent2.date;
            int i23222 = iArr[0];
            iArr[0] = i23222 + 1;
            tLRPC$Message4222.id = i23222;
            messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
            tLRPC$Message4222.out = false;
            tLRPC$Message4222.peer_id = new TLRPC$TL_peerChannel();
            TLRPC$Message tLRPC$Message5222 = messageObject.messageOwner;
            tLRPC$Message5222.peer_id.channel_id = tLRPC$Chat2.id;
            tLRPC$Message5222.unread = false;
            MediaController mediaController222 = MediaController.getInstance();
            if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
            }
            if (tLRPC$TL_message != null) {
            }
            if (messageObject.contentType < 0) {
            }
        }
        tLRPC$Chat2 = tLRPC$Chat;
        tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
        str = "";
        tLRPC$TL_message = null;
        if (messageObject.messageOwner == null) {
        }
        messageObject.messageOwner.message = messageObject.messageText.toString();
        messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
        TLRPC$Message tLRPC$Message42222 = messageObject.messageOwner;
        tLRPC$Message42222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
        tLRPC$Message42222.date = tLRPC$TL_channelAdminLogEvent2.date;
        int i232222 = iArr[0];
        iArr[0] = i232222 + 1;
        tLRPC$Message42222.id = i232222;
        messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
        tLRPC$Message42222.out = false;
        tLRPC$Message42222.peer_id = new TLRPC$TL_peerChannel();
        TLRPC$Message tLRPC$Message52222 = messageObject.messageOwner;
        tLRPC$Message52222.peer_id.channel_id = tLRPC$Chat2.id;
        tLRPC$Message52222.unread = false;
        MediaController mediaController2222 = MediaController.getInstance();
        if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
        }
        if (tLRPC$TL_message != null) {
        }
        if (messageObject.contentType < 0) {
        }
    }

    private String getUserName(TLObject tLObject, ArrayList<TLRPC$MessageEntity> arrayList, int i) {
        String str;
        String str2;
        long j;
        long j2;
        String str3;
        String str4;
        if (tLObject == null) {
            str = null;
            j = 0;
            str2 = "";
        } else {
            if (tLObject instanceof TLRPC$User) {
                TLRPC$User tLRPC$User = (TLRPC$User) tLObject;
                if (tLRPC$User.deleted) {
                    str4 = LocaleController.getString("HiddenName", 2131626131);
                } else {
                    str4 = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                }
                str2 = str4;
                str3 = tLRPC$User.username;
                j2 = tLRPC$User.id;
            } else {
                TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) tLObject;
                str2 = tLRPC$Chat.title;
                str3 = tLRPC$Chat.username;
                j2 = -tLRPC$Chat.id;
            }
            str = str3;
            j = j2;
        }
        if (i >= 0) {
            TLRPC$TL_messageEntityMentionName tLRPC$TL_messageEntityMentionName = new TLRPC$TL_messageEntityMentionName();
            tLRPC$TL_messageEntityMentionName.user_id = j;
            tLRPC$TL_messageEntityMentionName.offset = i;
            tLRPC$TL_messageEntityMentionName.length = str2.length();
            arrayList.add(tLRPC$TL_messageEntityMentionName);
        }
        if (!TextUtils.isEmpty(str)) {
            if (i >= 0) {
                TLRPC$TL_messageEntityMentionName tLRPC$TL_messageEntityMentionName2 = new TLRPC$TL_messageEntityMentionName();
                tLRPC$TL_messageEntityMentionName2.user_id = j;
                tLRPC$TL_messageEntityMentionName2.offset = i + str2.length() + 2;
                tLRPC$TL_messageEntityMentionName2.length = str.length() + 1;
                arrayList.add(tLRPC$TL_messageEntityMentionName2);
            }
            return String.format("%1$s (@%2$s)", str2, str);
        }
        return str2;
    }

    public void applyNewText() {
        applyNewText(this.messageOwner.message);
    }

    public void applyNewText(CharSequence charSequence) {
        TextPaint textPaint;
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        int[] iArr = null;
        TLRPC$User user = isFromUser() ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id)) : null;
        this.messageText = charSequence;
        if (this.messageOwner.media instanceof TLRPC$TL_messageMediaGame) {
            textPaint = Theme.chat_msgGameTextPaint;
        } else {
            textPaint = Theme.chat_msgTextPaint;
        }
        if (allowsBigEmoji()) {
            iArr = new int[1];
        }
        this.messageText = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, iArr, this.contentType == 0, this.viewRef);
        checkEmojiOnly(iArr);
        generateLayout(user);
    }

    private boolean allowsBigEmoji() {
        TLRPC$Peer tLRPC$Peer;
        if (!SharedConfig.allowBigEmoji) {
            return false;
        }
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null || (tLRPC$Peer = tLRPC$Message.peer_id) == null || (tLRPC$Peer.channel_id == 0 && tLRPC$Peer.chat_id == 0)) {
            return true;
        }
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        TLRPC$Peer tLRPC$Peer2 = this.messageOwner.peer_id;
        long j = tLRPC$Peer2.channel_id;
        if (j == 0) {
            j = tLRPC$Peer2.chat_id;
        }
        TLRPC$Chat chat = messagesController.getChat(Long.valueOf(j));
        return (chat != null && chat.gigagroup) || !ChatObject.isActionBanned(chat, 8) || ChatObject.hasAdminRights(chat);
    }

    public void generateGameMessageText(TLRPC$User tLRPC$User) {
        TLRPC$MessageMedia tLRPC$MessageMedia;
        TLRPC$TL_game tLRPC$TL_game;
        if (tLRPC$User == null && isFromUser()) {
            tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
        }
        TLRPC$TL_game tLRPC$TL_game2 = null;
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null && (tLRPC$MessageMedia = messageObject.messageOwner.media) != null && (tLRPC$TL_game = tLRPC$MessageMedia.game) != null) {
            tLRPC$TL_game2 = tLRPC$TL_game;
        }
        if (tLRPC$TL_game2 == null) {
            if (tLRPC$User == null || tLRPC$User.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                this.messageText = replaceWithLink(LocaleController.formatString("ActionUserScored", 2131624220, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0])), "un1", tLRPC$User);
                return;
            } else {
                this.messageText = LocaleController.formatString("ActionYouScored", 2131624234, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0]));
                return;
            }
        }
        if (tLRPC$User == null || tLRPC$User.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            this.messageText = replaceWithLink(LocaleController.formatString("ActionUserScoredInGame", 2131624221, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0])), "un1", tLRPC$User);
        } else {
            this.messageText = LocaleController.formatString("ActionYouScoredInGame", 2131624235, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0]));
        }
        this.messageText = replaceWithLink(this.messageText, "un2", tLRPC$TL_game2);
    }

    public boolean hasValidReplyMessageObject() {
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null) {
            TLRPC$Message tLRPC$Message = messageObject.messageOwner;
            if (!(tLRPC$Message instanceof TLRPC$TL_messageEmpty) && !(tLRPC$Message.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                return true;
            }
        }
        return false;
    }

    public void generatePaymentSentMessageText(TLRPC$User tLRPC$User) {
        String str;
        if (tLRPC$User == null) {
            tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getDialogId()));
        }
        String firstName = tLRPC$User != null ? UserObject.getFirstName(tLRPC$User) : "";
        try {
            LocaleController localeController = LocaleController.getInstance();
            TLRPC$MessageAction tLRPC$MessageAction = this.messageOwner.action;
            str = localeController.formatCurrencyString(tLRPC$MessageAction.total_amount, tLRPC$MessageAction.currency);
        } catch (Exception e) {
            FileLog.e(e);
            str = "<error>";
        }
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null) {
            TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) {
                if (this.messageOwner.action.recurring_init) {
                    this.messageText = LocaleController.formatString(2131627426, str, firstName, tLRPC$MessageMedia.title);
                    return;
                } else {
                    this.messageText = LocaleController.formatString("PaymentSuccessfullyPaid", 2131627423, str, firstName, tLRPC$MessageMedia.title);
                    return;
                }
            }
        }
        if (this.messageOwner.action.recurring_init) {
            this.messageText = LocaleController.formatString(2131627425, str, firstName);
        } else {
            this.messageText = LocaleController.formatString("PaymentSuccessfullyPaidNoItem", 2131627424, str, firstName);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:137:0x02c5  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x006a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generatePinMessageText(TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat) {
        TLRPC$Chat tLRPC$Chat2;
        TLRPC$Chat tLRPC$Chat3;
        MessageObject messageObject;
        if (tLRPC$User == null && tLRPC$Chat == null) {
            tLRPC$Chat3 = isFromUser() ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id)) : tLRPC$User;
            if (tLRPC$Chat3 == null) {
                TLRPC$Peer tLRPC$Peer = this.messageOwner.peer_id;
                if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
                    tLRPC$Chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.peer_id.channel_id));
                } else if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
                    tLRPC$Chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.peer_id.chat_id));
                }
                messageObject = this.replyMessageObject;
                if (messageObject != null) {
                    TLRPC$Message tLRPC$Message = messageObject.messageOwner;
                    if (!(tLRPC$Message instanceof TLRPC$TL_messageEmpty) && !(tLRPC$Message.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                        if (messageObject.isMusic()) {
                            String string = LocaleController.getString("ActionPinnedMusic", 2131624197);
                            if (tLRPC$Chat3 == null) {
                                tLRPC$Chat3 = tLRPC$Chat2;
                            }
                            this.messageText = replaceWithLink(string, "un1", tLRPC$Chat3);
                            return;
                        } else if (this.replyMessageObject.isVideo()) {
                            String string2 = LocaleController.getString("ActionPinnedVideo", 2131624205);
                            if (tLRPC$Chat3 == null) {
                                tLRPC$Chat3 = tLRPC$Chat2;
                            }
                            this.messageText = replaceWithLink(string2, "un1", tLRPC$Chat3);
                            return;
                        } else if (this.replyMessageObject.isGif()) {
                            String string3 = LocaleController.getString("ActionPinnedGif", 2131624196);
                            if (tLRPC$Chat3 == null) {
                                tLRPC$Chat3 = tLRPC$Chat2;
                            }
                            this.messageText = replaceWithLink(string3, "un1", tLRPC$Chat3);
                            return;
                        } else if (this.replyMessageObject.isVoice()) {
                            String string4 = LocaleController.getString("ActionPinnedVoice", 2131624206);
                            if (tLRPC$Chat3 == null) {
                                tLRPC$Chat3 = tLRPC$Chat2;
                            }
                            this.messageText = replaceWithLink(string4, "un1", tLRPC$Chat3);
                            return;
                        } else if (this.replyMessageObject.isRoundVideo()) {
                            String string5 = LocaleController.getString("ActionPinnedRound", 2131624202);
                            if (tLRPC$Chat3 == null) {
                                tLRPC$Chat3 = tLRPC$Chat2;
                            }
                            this.messageText = replaceWithLink(string5, "un1", tLRPC$Chat3);
                            return;
                        } else if ((this.replyMessageObject.isSticker() || this.replyMessageObject.isAnimatedSticker()) && !this.replyMessageObject.isAnimatedEmoji()) {
                            String string6 = LocaleController.getString("ActionPinnedSticker", 2131624203);
                            if (tLRPC$Chat3 == null) {
                                tLRPC$Chat3 = tLRPC$Chat2;
                            }
                            this.messageText = replaceWithLink(string6, "un1", tLRPC$Chat3);
                            return;
                        } else {
                            MessageObject messageObject2 = this.replyMessageObject;
                            TLRPC$MessageMedia tLRPC$MessageMedia = messageObject2.messageOwner.media;
                            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                                String string7 = LocaleController.getString("ActionPinnedFile", 2131624192);
                                if (tLRPC$Chat3 == null) {
                                    tLRPC$Chat3 = tLRPC$Chat2;
                                }
                                this.messageText = replaceWithLink(string7, "un1", tLRPC$Chat3);
                                return;
                            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeo) {
                                String string8 = LocaleController.getString("ActionPinnedGeo", 2131624194);
                                if (tLRPC$Chat3 == null) {
                                    tLRPC$Chat3 = tLRPC$Chat2;
                                }
                                this.messageText = replaceWithLink(string8, "un1", tLRPC$Chat3);
                                return;
                            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeoLive) {
                                String string9 = LocaleController.getString("ActionPinnedGeoLive", 2131624195);
                                if (tLRPC$Chat3 == null) {
                                    tLRPC$Chat3 = tLRPC$Chat2;
                                }
                                this.messageText = replaceWithLink(string9, "un1", tLRPC$Chat3);
                                return;
                            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaContact) {
                                String string10 = LocaleController.getString("ActionPinnedContact", 2131624191);
                                if (tLRPC$Chat3 == null) {
                                    tLRPC$Chat3 = tLRPC$Chat2;
                                }
                                this.messageText = replaceWithLink(string10, "un1", tLRPC$Chat3);
                                return;
                            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                                if (((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll.quiz) {
                                    String string11 = LocaleController.getString("ActionPinnedQuiz", 2131624201);
                                    if (tLRPC$Chat3 == null) {
                                        tLRPC$Chat3 = tLRPC$Chat2;
                                    }
                                    this.messageText = replaceWithLink(string11, "un1", tLRPC$Chat3);
                                    return;
                                }
                                String string12 = LocaleController.getString("ActionPinnedPoll", 2131624200);
                                if (tLRPC$Chat3 == null) {
                                    tLRPC$Chat3 = tLRPC$Chat2;
                                }
                                this.messageText = replaceWithLink(string12, "un1", tLRPC$Chat3);
                                return;
                            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
                                String string13 = LocaleController.getString("ActionPinnedPhoto", 2131624199);
                                if (tLRPC$Chat3 == null) {
                                    tLRPC$Chat3 = tLRPC$Chat2;
                                }
                                this.messageText = replaceWithLink(string13, "un1", tLRPC$Chat3);
                                return;
                            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                                String formatString = LocaleController.formatString("ActionPinnedGame", 2131624193, "🎮 " + this.replyMessageObject.messageOwner.media.game.title);
                                if (tLRPC$Chat3 == null) {
                                    tLRPC$Chat3 = tLRPC$Chat2;
                                }
                                CharSequence replaceWithLink = replaceWithLink(formatString, "un1", tLRPC$Chat3);
                                this.messageText = replaceWithLink;
                                this.messageText = Emoji.replaceEmoji(replaceWithLink, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, this.contentType == 0, this.viewRef);
                                return;
                            } else {
                                CharSequence charSequence = messageObject2.messageText;
                                if (charSequence != null && charSequence.length() > 0) {
                                    CharSequence charSequence2 = this.replyMessageObject.messageText;
                                    if (charSequence2.length() > 20) {
                                        charSequence2 = ((Object) charSequence2.subSequence(0, 20)) + "...";
                                    }
                                    CharSequence replaceEmoji = Emoji.replaceEmoji(charSequence2, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, this.contentType == 0, this.viewRef);
                                    MediaDataController.addTextStyleRuns(this.replyMessageObject, (Spannable) replaceEmoji);
                                    SpannableStringBuilder formatSpannable = AndroidUtilities.formatSpannable(LocaleController.getString("ActionPinnedText", 2131624204), replaceEmoji);
                                    if (tLRPC$Chat3 == null) {
                                        tLRPC$Chat3 = tLRPC$Chat2;
                                    }
                                    this.messageText = replaceWithLink(formatSpannable, "un1", tLRPC$Chat3);
                                    return;
                                }
                                String string14 = LocaleController.getString("ActionPinnedNoText", 2131624198);
                                if (tLRPC$Chat3 == null) {
                                    tLRPC$Chat3 = tLRPC$Chat2;
                                }
                                this.messageText = replaceWithLink(string14, "un1", tLRPC$Chat3);
                                return;
                            }
                        }
                    }
                }
                String string15 = LocaleController.getString("ActionPinnedNoText", 2131624198);
                if (tLRPC$Chat3 == null) {
                    tLRPC$Chat3 = tLRPC$Chat2;
                }
                this.messageText = replaceWithLink(string15, "un1", tLRPC$Chat3);
            }
        } else {
            tLRPC$Chat3 = tLRPC$User;
        }
        tLRPC$Chat2 = tLRPC$Chat;
        messageObject = this.replyMessageObject;
        if (messageObject != null) {
        }
        String string152 = LocaleController.getString("ActionPinnedNoText", 2131624198);
        if (tLRPC$Chat3 == null) {
        }
        this.messageText = replaceWithLink(string152, "un1", tLRPC$Chat3);
    }

    public static void updateReactions(TLRPC$Message tLRPC$Message, TLRPC$TL_messageReactions tLRPC$TL_messageReactions) {
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions2;
        if (tLRPC$Message == null || tLRPC$TL_messageReactions == null) {
            return;
        }
        if (tLRPC$TL_messageReactions.min && (tLRPC$TL_messageReactions2 = tLRPC$Message.reactions) != null) {
            int size = tLRPC$TL_messageReactions2.results.size();
            int i = 0;
            int i2 = 0;
            while (true) {
                if (i2 >= size) {
                    break;
                }
                TLRPC$TL_reactionCount tLRPC$TL_reactionCount = tLRPC$Message.reactions.results.get(i2);
                if (tLRPC$TL_reactionCount.chosen) {
                    int size2 = tLRPC$TL_messageReactions.results.size();
                    while (true) {
                        if (i >= size2) {
                            break;
                        }
                        TLRPC$TL_reactionCount tLRPC$TL_reactionCount2 = tLRPC$TL_messageReactions.results.get(i);
                        if (tLRPC$TL_reactionCount.reaction.equals(tLRPC$TL_reactionCount2.reaction)) {
                            tLRPC$TL_reactionCount2.chosen = true;
                            break;
                        }
                        i++;
                    }
                } else {
                    i2++;
                }
            }
        }
        tLRPC$Message.reactions = tLRPC$TL_messageReactions;
        tLRPC$Message.flags |= 1048576;
    }

    public boolean hasReactions() {
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions = this.messageOwner.reactions;
        return tLRPC$TL_messageReactions != null && !tLRPC$TL_messageReactions.results.isEmpty();
    }

    public static void updatePollResults(TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll, TLRPC$PollResults tLRPC$PollResults) {
        byte[] bArr;
        ArrayList arrayList;
        ArrayList<TLRPC$TL_pollAnswerVoters> arrayList2;
        if (tLRPC$TL_messageMediaPoll == null || tLRPC$PollResults == null) {
            return;
        }
        if ((tLRPC$PollResults.flags & 2) != 0) {
            if (!tLRPC$PollResults.min || (arrayList2 = tLRPC$TL_messageMediaPoll.results.results) == null) {
                arrayList = null;
                bArr = null;
            } else {
                int size = arrayList2.size();
                arrayList = null;
                bArr = null;
                for (int i = 0; i < size; i++) {
                    TLRPC$TL_pollAnswerVoters tLRPC$TL_pollAnswerVoters = tLRPC$TL_messageMediaPoll.results.results.get(i);
                    if (tLRPC$TL_pollAnswerVoters.chosen) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(tLRPC$TL_pollAnswerVoters.option);
                    }
                    if (tLRPC$TL_pollAnswerVoters.correct) {
                        bArr = tLRPC$TL_pollAnswerVoters.option;
                    }
                }
            }
            TLRPC$PollResults tLRPC$PollResults2 = tLRPC$TL_messageMediaPoll.results;
            ArrayList<TLRPC$TL_pollAnswerVoters> arrayList3 = tLRPC$PollResults.results;
            tLRPC$PollResults2.results = arrayList3;
            if (arrayList != null || bArr != null) {
                int size2 = arrayList3.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    TLRPC$TL_pollAnswerVoters tLRPC$TL_pollAnswerVoters2 = tLRPC$TL_messageMediaPoll.results.results.get(i2);
                    if (arrayList != null) {
                        int size3 = arrayList.size();
                        int i3 = 0;
                        while (true) {
                            if (i3 >= size3) {
                                break;
                            } else if (Arrays.equals(tLRPC$TL_pollAnswerVoters2.option, (byte[]) arrayList.get(i3))) {
                                tLRPC$TL_pollAnswerVoters2.chosen = true;
                                arrayList.remove(i3);
                                break;
                            } else {
                                i3++;
                            }
                        }
                        if (arrayList.isEmpty()) {
                            arrayList = null;
                        }
                    }
                    if (bArr != null && Arrays.equals(tLRPC$TL_pollAnswerVoters2.option, bArr)) {
                        tLRPC$TL_pollAnswerVoters2.correct = true;
                        bArr = null;
                    }
                    if (arrayList == null && bArr == null) {
                        break;
                    }
                }
            }
            tLRPC$TL_messageMediaPoll.results.flags |= 2;
        }
        if ((tLRPC$PollResults.flags & 4) != 0) {
            TLRPC$PollResults tLRPC$PollResults3 = tLRPC$TL_messageMediaPoll.results;
            tLRPC$PollResults3.total_voters = tLRPC$PollResults.total_voters;
            tLRPC$PollResults3.flags |= 4;
        }
        if ((tLRPC$PollResults.flags & 8) != 0) {
            TLRPC$PollResults tLRPC$PollResults4 = tLRPC$TL_messageMediaPoll.results;
            tLRPC$PollResults4.recent_voters = tLRPC$PollResults.recent_voters;
            tLRPC$PollResults4.flags |= 8;
        }
        if ((tLRPC$PollResults.flags & 16) == 0) {
            return;
        }
        TLRPC$PollResults tLRPC$PollResults5 = tLRPC$TL_messageMediaPoll.results;
        tLRPC$PollResults5.solution = tLRPC$PollResults.solution;
        tLRPC$PollResults5.solution_entities = tLRPC$PollResults.solution_entities;
        tLRPC$PollResults5.flags |= 16;
    }

    public boolean isPollClosed() {
        if (this.type != 17) {
            return false;
        }
        return ((TLRPC$TL_messageMediaPoll) this.messageOwner.media).poll.closed;
    }

    public boolean isQuiz() {
        if (this.type != 17) {
            return false;
        }
        return ((TLRPC$TL_messageMediaPoll) this.messageOwner.media).poll.quiz;
    }

    public boolean isPublicPoll() {
        if (this.type != 17) {
            return false;
        }
        return ((TLRPC$TL_messageMediaPoll) this.messageOwner.media).poll.public_voters;
    }

    public boolean isPoll() {
        return this.type == 17;
    }

    public boolean canUnvote() {
        TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll;
        TLRPC$PollResults tLRPC$PollResults;
        if (this.type == 17 && (tLRPC$PollResults = (tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) this.messageOwner.media).results) != null && !tLRPC$PollResults.results.isEmpty() && !tLRPC$TL_messageMediaPoll.poll.quiz) {
            int size = tLRPC$TL_messageMediaPoll.results.results.size();
            for (int i = 0; i < size; i++) {
                if (tLRPC$TL_messageMediaPoll.results.results.get(i).chosen) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isVoted() {
        TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll;
        TLRPC$PollResults tLRPC$PollResults;
        if (this.type == 17 && (tLRPC$PollResults = (tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) this.messageOwner.media).results) != null && !tLRPC$PollResults.results.isEmpty()) {
            int size = tLRPC$TL_messageMediaPoll.results.results.size();
            for (int i = 0; i < size; i++) {
                if (tLRPC$TL_messageMediaPoll.results.results.get(i).chosen) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSponsored() {
        return this.sponsoredId != null;
    }

    public long getPollId() {
        if (this.type != 17) {
            return 0L;
        }
        return ((TLRPC$TL_messageMediaPoll) this.messageOwner.media).poll.id;
    }

    private TLRPC$Photo getPhotoWithId(TLRPC$WebPage tLRPC$WebPage, long j) {
        if (tLRPC$WebPage != null && tLRPC$WebPage.cached_page != null) {
            TLRPC$Photo tLRPC$Photo = tLRPC$WebPage.photo;
            if (tLRPC$Photo != null && tLRPC$Photo.id == j) {
                return tLRPC$Photo;
            }
            for (int i = 0; i < tLRPC$WebPage.cached_page.photos.size(); i++) {
                TLRPC$Photo tLRPC$Photo2 = tLRPC$WebPage.cached_page.photos.get(i);
                if (tLRPC$Photo2.id == j) {
                    return tLRPC$Photo2;
                }
            }
        }
        return null;
    }

    private TLRPC$Document getDocumentWithId(TLRPC$WebPage tLRPC$WebPage, long j) {
        if (tLRPC$WebPage != null && tLRPC$WebPage.cached_page != null) {
            TLRPC$Document tLRPC$Document = tLRPC$WebPage.document;
            if (tLRPC$Document != null && tLRPC$Document.id == j) {
                return tLRPC$Document;
            }
            for (int i = 0; i < tLRPC$WebPage.cached_page.documents.size(); i++) {
                TLRPC$Document tLRPC$Document2 = tLRPC$WebPage.cached_page.documents.get(i);
                if (tLRPC$Document2.id == j) {
                    return tLRPC$Document2;
                }
            }
        }
        return null;
    }

    public boolean isSupergroup() {
        if (this.localSupergroup) {
            return true;
        }
        Boolean bool = this.cachedIsSupergroup;
        if (bool != null) {
            return bool.booleanValue();
        }
        TLRPC$Peer tLRPC$Peer = this.messageOwner.peer_id;
        if (tLRPC$Peer != null) {
            long j = tLRPC$Peer.channel_id;
            if (j != 0) {
                TLRPC$Chat chat = getChat(null, null, j);
                if (chat == null) {
                    return false;
                }
                Boolean valueOf = Boolean.valueOf(chat.megagroup);
                this.cachedIsSupergroup = valueOf;
                return valueOf.booleanValue();
            }
        }
        this.cachedIsSupergroup = Boolean.FALSE;
        return false;
    }

    private MessageObject getMessageObjectForBlock(TLRPC$WebPage tLRPC$WebPage, TLRPC$PageBlock tLRPC$PageBlock) {
        TLRPC$TL_message tLRPC$TL_message;
        if (tLRPC$PageBlock instanceof TLRPC$TL_pageBlockPhoto) {
            TLRPC$Photo photoWithId = getPhotoWithId(tLRPC$WebPage, ((TLRPC$TL_pageBlockPhoto) tLRPC$PageBlock).photo_id);
            if (photoWithId == tLRPC$WebPage.photo) {
                return this;
            }
            tLRPC$TL_message = new TLRPC$TL_message();
            TLRPC$TL_messageMediaPhoto tLRPC$TL_messageMediaPhoto = new TLRPC$TL_messageMediaPhoto();
            tLRPC$TL_message.media = tLRPC$TL_messageMediaPhoto;
            tLRPC$TL_messageMediaPhoto.photo = photoWithId;
        } else if (tLRPC$PageBlock instanceof TLRPC$TL_pageBlockVideo) {
            TLRPC$TL_pageBlockVideo tLRPC$TL_pageBlockVideo = (TLRPC$TL_pageBlockVideo) tLRPC$PageBlock;
            if (getDocumentWithId(tLRPC$WebPage, tLRPC$TL_pageBlockVideo.video_id) == tLRPC$WebPage.document) {
                return this;
            }
            TLRPC$TL_message tLRPC$TL_message2 = new TLRPC$TL_message();
            TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument = new TLRPC$TL_messageMediaDocument();
            tLRPC$TL_message2.media = tLRPC$TL_messageMediaDocument;
            tLRPC$TL_messageMediaDocument.document = getDocumentWithId(tLRPC$WebPage, tLRPC$TL_pageBlockVideo.video_id);
            tLRPC$TL_message = tLRPC$TL_message2;
        } else {
            tLRPC$TL_message = null;
        }
        tLRPC$TL_message.message = "";
        tLRPC$TL_message.realId = getId();
        tLRPC$TL_message.id = Utilities.random.nextInt();
        TLRPC$Message tLRPC$Message = this.messageOwner;
        tLRPC$TL_message.date = tLRPC$Message.date;
        tLRPC$TL_message.peer_id = tLRPC$Message.peer_id;
        tLRPC$TL_message.out = tLRPC$Message.out;
        tLRPC$TL_message.from_id = tLRPC$Message.from_id;
        return new MessageObject(this.currentAccount, tLRPC$TL_message, false, true);
    }

    public ArrayList<MessageObject> getWebPagePhotos(ArrayList<MessageObject> arrayList, ArrayList<TLRPC$PageBlock> arrayList2) {
        TLRPC$WebPage tLRPC$WebPage;
        TLRPC$Page tLRPC$Page;
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        if (tLRPC$MessageMedia == null || (tLRPC$WebPage = tLRPC$MessageMedia.webpage) == null || (tLRPC$Page = tLRPC$WebPage.cached_page) == null) {
            return arrayList;
        }
        if (arrayList2 == null) {
            arrayList2 = tLRPC$Page.blocks;
        }
        for (int i = 0; i < arrayList2.size(); i++) {
            TLRPC$PageBlock tLRPC$PageBlock = arrayList2.get(i);
            if (tLRPC$PageBlock instanceof TLRPC$TL_pageBlockSlideshow) {
                TLRPC$TL_pageBlockSlideshow tLRPC$TL_pageBlockSlideshow = (TLRPC$TL_pageBlockSlideshow) tLRPC$PageBlock;
                for (int i2 = 0; i2 < tLRPC$TL_pageBlockSlideshow.items.size(); i2++) {
                    arrayList.add(getMessageObjectForBlock(tLRPC$WebPage, tLRPC$TL_pageBlockSlideshow.items.get(i2)));
                }
            } else if (tLRPC$PageBlock instanceof TLRPC$TL_pageBlockCollage) {
                TLRPC$TL_pageBlockCollage tLRPC$TL_pageBlockCollage = (TLRPC$TL_pageBlockCollage) tLRPC$PageBlock;
                for (int i3 = 0; i3 < tLRPC$TL_pageBlockCollage.items.size(); i3++) {
                    arrayList.add(getMessageObjectForBlock(tLRPC$WebPage, tLRPC$TL_pageBlockCollage.items.get(i3)));
                }
            }
        }
        return arrayList;
    }

    public void createMessageSendInfo() {
        HashMap<String, String> hashMap;
        String str;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message.message != null) {
            if ((tLRPC$Message.id >= 0 && !isEditing()) || (hashMap = this.messageOwner.params) == null) {
                return;
            }
            String str2 = hashMap.get("ve");
            if (str2 != null && (isVideo() || isNewGif() || isRoundVideo())) {
                VideoEditedInfo videoEditedInfo = new VideoEditedInfo();
                this.videoEditedInfo = videoEditedInfo;
                if (!videoEditedInfo.parseString(str2)) {
                    this.videoEditedInfo = null;
                } else {
                    this.videoEditedInfo.roundVideo = isRoundVideo();
                }
            }
            TLRPC$Message tLRPC$Message2 = this.messageOwner;
            if (tLRPC$Message2.send_state != 3 || (str = tLRPC$Message2.params.get("prevMedia")) == null) {
                return;
            }
            SerializedData serializedData = new SerializedData(Base64.decode(str, 0));
            this.previousMedia = TLRPC$MessageMedia.TLdeserialize(serializedData, serializedData.readInt32(false), false);
            this.previousMessage = serializedData.readString(false);
            this.previousAttachPath = serializedData.readString(false);
            int readInt32 = serializedData.readInt32(false);
            this.previousMessageEntities = new ArrayList<>(readInt32);
            for (int i = 0; i < readInt32; i++) {
                this.previousMessageEntities.add(TLRPC$MessageEntity.TLdeserialize(serializedData, serializedData.readInt32(false), false));
            }
            serializedData.cleanup();
        }
    }

    public void measureInlineBotButtons() {
        int i;
        CharSequence replaceEmoji;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        if (this.isRestrictedMessage) {
            return;
        }
        this.wantedBotKeyboardWidth = 0;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if ((tLRPC$Message.reply_markup instanceof TLRPC$TL_replyInlineMarkup) || ((tLRPC$TL_messageReactions = tLRPC$Message.reactions) != null && !tLRPC$TL_messageReactions.results.isEmpty())) {
            Theme.createCommonMessageResources();
            StringBuilder sb = this.botButtonsLayout;
            if (sb == null) {
                this.botButtonsLayout = new StringBuilder();
            } else {
                sb.setLength(0);
            }
        }
        TLRPC$Message tLRPC$Message2 = this.messageOwner;
        if (tLRPC$Message2.reply_markup instanceof TLRPC$TL_replyInlineMarkup) {
            for (int i2 = 0; i2 < this.messageOwner.reply_markup.rows.size(); i2++) {
                TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow = this.messageOwner.reply_markup.rows.get(i2);
                int size = tLRPC$TL_keyboardButtonRow.buttons.size();
                int i3 = 0;
                for (int i4 = 0; i4 < size; i4++) {
                    TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow.buttons.get(i4);
                    StringBuilder sb2 = this.botButtonsLayout;
                    sb2.append(i2);
                    sb2.append(i4);
                    if ((tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonBuy) && (this.messageOwner.media.flags & 4) != 0) {
                        replaceEmoji = LocaleController.getString("PaymentReceipt", 2131627407);
                    } else {
                        String str = tLRPC$KeyboardButton.text;
                        if (str == null) {
                            str = "";
                        }
                        replaceEmoji = Emoji.replaceEmoji(str, Theme.chat_msgBotButtonPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false, this.contentType == 0, this.viewRef);
                    }
                    StaticLayout staticLayout = new StaticLayout(replaceEmoji, Theme.chat_msgBotButtonPaint, AndroidUtilities.dp(2000.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    if (staticLayout.getLineCount() > 0) {
                        float lineWidth = staticLayout.getLineWidth(0);
                        float lineLeft = staticLayout.getLineLeft(0);
                        if (lineLeft < lineWidth) {
                            lineWidth -= lineLeft;
                        }
                        i3 = Math.max(i3, ((int) Math.ceil(lineWidth)) + AndroidUtilities.dp(4.0f));
                    }
                }
                this.wantedBotKeyboardWidth = Math.max(this.wantedBotKeyboardWidth, ((i3 + AndroidUtilities.dp(12.0f)) * size) + (AndroidUtilities.dp(5.0f) * (size - 1)));
            }
            return;
        }
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions2 = tLRPC$Message2.reactions;
        if (tLRPC$TL_messageReactions2 == null) {
            return;
        }
        int size2 = tLRPC$TL_messageReactions2.results.size();
        for (int i5 = 0; i5 < size2; i5++) {
            TLRPC$TL_reactionCount tLRPC$TL_reactionCount = this.messageOwner.reactions.results.get(i5);
            StringBuilder sb3 = this.botButtonsLayout;
            sb3.append(0);
            sb3.append(i5);
            StaticLayout staticLayout2 = new StaticLayout(Emoji.replaceEmoji(String.format("%d %s", Integer.valueOf(tLRPC$TL_reactionCount.count), tLRPC$TL_reactionCount.reaction), Theme.chat_msgBotButtonPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false, this.contentType == 0, this.viewRef), Theme.chat_msgBotButtonPaint, AndroidUtilities.dp(2000.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (staticLayout2.getLineCount() > 0) {
                float lineWidth2 = staticLayout2.getLineWidth(0);
                float lineLeft2 = staticLayout2.getLineLeft(0);
                if (lineLeft2 < lineWidth2) {
                    lineWidth2 -= lineLeft2;
                }
                i = Math.max(0, ((int) Math.ceil(lineWidth2)) + AndroidUtilities.dp(4.0f));
            } else {
                i = 0;
            }
            this.wantedBotKeyboardWidth = Math.max(this.wantedBotKeyboardWidth, ((i + AndroidUtilities.dp(12.0f)) * size2) + (AndroidUtilities.dp(5.0f) * (size2 - 1)));
        }
    }

    public boolean isVideoAvatar() {
        TLRPC$Photo tLRPC$Photo;
        TLRPC$MessageAction tLRPC$MessageAction = this.messageOwner.action;
        return (tLRPC$MessageAction == null || (tLRPC$Photo = tLRPC$MessageAction.photo) == null || tLRPC$Photo.video_sizes.isEmpty()) ? false : true;
    }

    public boolean isFcmMessage() {
        return this.localType != 0;
    }

    private TLRPC$User getUser(AbstractMap<Long, TLRPC$User> abstractMap, LongSparseArray<TLRPC$User> longSparseArray, long j) {
        TLRPC$User tLRPC$User;
        if (abstractMap != null) {
            tLRPC$User = abstractMap.get(Long.valueOf(j));
        } else {
            tLRPC$User = longSparseArray != null ? longSparseArray.get(j) : null;
        }
        return tLRPC$User == null ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j)) : tLRPC$User;
    }

    private TLRPC$Chat getChat(AbstractMap<Long, TLRPC$Chat> abstractMap, LongSparseArray<TLRPC$Chat> longSparseArray, long j) {
        TLRPC$Chat tLRPC$Chat;
        if (abstractMap != null) {
            tLRPC$Chat = abstractMap.get(Long.valueOf(j));
        } else {
            tLRPC$Chat = longSparseArray != null ? longSparseArray.get(j) : null;
        }
        return tLRPC$Chat == null ? MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j)) : tLRPC$Chat;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x002a  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0556  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x0576  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x05d9  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x05f0  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x063c  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0649  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x0717  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x0751  */
    /* JADX WARN: Removed duplicated region for block: B:489:0x0d0f  */
    /* JADX WARN: Removed duplicated region for block: B:586:0x0f35  */
    /* JADX WARN: Removed duplicated region for block: B:606:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateMessageText(AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, LongSparseArray<TLRPC$User> longSparseArray, LongSparseArray<TLRPC$Chat> longSparseArray2) {
        TLRPC$User tLRPC$User;
        TLRPC$User tLRPC$User2;
        TLRPC$Message tLRPC$Message;
        String str;
        String str2;
        String str3;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Chat tLRPC$Chat2;
        TLRPC$Chat tLRPC$Chat3;
        TLRPC$Chat tLRPC$Chat4;
        TLRPC$Chat tLRPC$Chat5;
        TLObject tLObject;
        TLObject tLObject2;
        String str4;
        TLRPC$Peer tLRPC$Peer = this.messageOwner.from_id;
        if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
            tLRPC$User2 = getUser(abstractMap, longSparseArray, tLRPC$Peer.user_id);
        } else if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
            tLRPC$User = getChat(abstractMap2, longSparseArray2, tLRPC$Peer.channel_id);
            tLRPC$User2 = null;
            TLRPC$User tLRPC$User3 = tLRPC$User2 == null ? tLRPC$User2 : tLRPC$User;
            tLRPC$Message = this.messageOwner;
            if (!(tLRPC$Message instanceof TLRPC$TL_messageService)) {
                TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
                if (tLRPC$MessageAction != null) {
                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGroupCallScheduled) {
                        TLRPC$TL_messageActionGroupCallScheduled tLRPC$TL_messageActionGroupCallScheduled = (TLRPC$TL_messageActionGroupCallScheduled) tLRPC$MessageAction;
                        if ((tLRPC$Message.peer_id instanceof TLRPC$TL_peerChat) || isSupergroup()) {
                            this.messageText = LocaleController.formatString("ActionGroupCallScheduled", 2131624180, LocaleController.formatStartsTime(tLRPC$TL_messageActionGroupCallScheduled.schedule_date, 3, false));
                        } else {
                            this.messageText = LocaleController.formatString("ActionChannelCallScheduled", 2131624158, LocaleController.formatStartsTime(tLRPC$TL_messageActionGroupCallScheduled.schedule_date, 3, false));
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGroupCall) {
                        int i = tLRPC$MessageAction.duration;
                        if (i != 0) {
                            int i2 = i / 86400;
                            if (i2 > 0) {
                                str4 = LocaleController.formatPluralString("Days", i2, new Object[0]);
                            } else {
                                int i3 = i / 3600;
                                if (i3 > 0) {
                                    str4 = LocaleController.formatPluralString("Hours", i3, new Object[0]);
                                } else {
                                    int i4 = i / 60;
                                    if (i4 > 0) {
                                        str4 = LocaleController.formatPluralString("Minutes", i4, new Object[0]);
                                    } else {
                                        str4 = LocaleController.formatPluralString("Seconds", i, new Object[0]);
                                    }
                                }
                            }
                            if ((this.messageOwner.peer_id instanceof TLRPC$TL_peerChat) || isSupergroup()) {
                                if (isOut()) {
                                    this.messageText = LocaleController.formatString("ActionGroupCallEndedByYou", 2131624176, str4);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.formatString("ActionGroupCallEndedBy", 2131624175, str4), "un1", tLRPC$User3);
                                }
                            } else {
                                this.messageText = LocaleController.formatString("ActionChannelCallEnded", 2131624156, str4);
                            }
                        } else if ((tLRPC$Message.peer_id instanceof TLRPC$TL_peerChat) || isSupergroup()) {
                            if (isOut()) {
                                this.messageText = LocaleController.getString("ActionGroupCallStartedByYou", 2131624182);
                            } else {
                                this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallStarted", 2131624181), "un1", tLRPC$User3);
                            }
                        } else {
                            this.messageText = LocaleController.getString("ActionChannelCallJustStarted", 2131624157);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionInviteToGroupCall) {
                        long j = tLRPC$MessageAction.user_id;
                        if (j == 0 && tLRPC$MessageAction.users.size() == 1) {
                            j = this.messageOwner.action.users.get(0).longValue();
                        }
                        if (j != 0) {
                            TLRPC$User user = getUser(abstractMap, longSparseArray, j);
                            if (isOut()) {
                                this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallYouInvited", 2131624183), "un2", user);
                            } else if (j == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallInvitedYou", 2131624178), "un1", tLRPC$User3);
                            } else {
                                CharSequence replaceWithLink = replaceWithLink(LocaleController.getString("ActionGroupCallInvited", 2131624177), "un2", user);
                                this.messageText = replaceWithLink;
                                this.messageText = replaceWithLink(replaceWithLink, "un1", tLRPC$User3);
                            }
                        } else if (isOut()) {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallYouInvited", 2131624183), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                        } else {
                            CharSequence replaceWithLink2 = replaceWithLink(LocaleController.getString("ActionGroupCallInvited", 2131624177), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                            this.messageText = replaceWithLink2;
                            this.messageText = replaceWithLink(replaceWithLink2, "un1", tLRPC$User3);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGeoProximityReached) {
                        TLRPC$TL_messageActionGeoProximityReached tLRPC$TL_messageActionGeoProximityReached = (TLRPC$TL_messageActionGeoProximityReached) tLRPC$MessageAction;
                        long peerId = getPeerId(tLRPC$TL_messageActionGeoProximityReached.from_id);
                        if (peerId > 0) {
                            tLObject = getUser(abstractMap, longSparseArray, peerId);
                        } else {
                            tLObject = getChat(abstractMap2, longSparseArray2, -peerId);
                        }
                        long peerId2 = getPeerId(tLRPC$TL_messageActionGeoProximityReached.to_id);
                        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                        if (peerId2 == clientUserId) {
                            this.messageText = replaceWithLink(LocaleController.formatString("ActionUserWithinRadius", 2131624223, LocaleController.formatDistance(tLRPC$TL_messageActionGeoProximityReached.distance, 2)), "un1", tLObject);
                        } else {
                            if (peerId2 > 0) {
                                tLObject2 = getUser(abstractMap, longSparseArray, peerId2);
                            } else {
                                tLObject2 = getChat(abstractMap2, longSparseArray2, -peerId2);
                            }
                            if (peerId == clientUserId) {
                                this.messageText = replaceWithLink(LocaleController.formatString("ActionUserWithinYouRadius", 2131624224, LocaleController.formatDistance(tLRPC$TL_messageActionGeoProximityReached.distance, 2)), "un1", tLObject2);
                            } else {
                                CharSequence replaceWithLink3 = replaceWithLink(LocaleController.formatString("ActionUserWithinOtherRadius", 2131624222, LocaleController.formatDistance(tLRPC$TL_messageActionGeoProximityReached.distance, 2)), "un2", tLObject2);
                                this.messageText = replaceWithLink3;
                                this.messageText = replaceWithLink(replaceWithLink3, "un1", tLObject);
                            }
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionCustomAction) {
                        this.messageText = tLRPC$MessageAction.message;
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatCreate) {
                        if (isOut()) {
                            this.messageText = LocaleController.getString("ActionYouCreateGroup", 2131624230);
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionCreateGroup", 2131624164), "un1", tLRPC$User3);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatDeleteUser) {
                        if (isFromUser()) {
                            TLRPC$Message tLRPC$Message2 = this.messageOwner;
                            if (tLRPC$Message2.action.user_id == tLRPC$Message2.from_id.user_id) {
                                if (isOut()) {
                                    this.messageText = LocaleController.getString("ActionYouLeftUser", 2131624232);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionLeftUser", 2131624188), "un1", tLRPC$User3);
                                }
                            }
                        }
                        TLRPC$User user2 = getUser(abstractMap, longSparseArray, this.messageOwner.action.user_id);
                        if (isOut()) {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionYouKickUser", 2131624231), "un2", user2);
                        } else if (this.messageOwner.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionKickUserYou", 2131624187), "un1", tLRPC$User3);
                        } else {
                            CharSequence replaceWithLink4 = replaceWithLink(LocaleController.getString("ActionKickUser", 2131624186), "un2", user2);
                            this.messageText = replaceWithLink4;
                            this.messageText = replaceWithLink(replaceWithLink4, "un1", tLRPC$User3);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatAddUser) {
                        long j2 = tLRPC$MessageAction.user_id;
                        if (j2 == 0 && tLRPC$MessageAction.users.size() == 1) {
                            j2 = this.messageOwner.action.users.get(0).longValue();
                        }
                        if (j2 != 0) {
                            TLRPC$User user3 = getUser(abstractMap, longSparseArray, j2);
                            long j3 = this.messageOwner.peer_id.channel_id;
                            TLRPC$Chat chat = j3 != 0 ? getChat(abstractMap2, longSparseArray2, j3) : null;
                            TLRPC$Peer tLRPC$Peer2 = this.messageOwner.from_id;
                            if (tLRPC$Peer2 != null && j2 == tLRPC$Peer2.user_id) {
                                if (ChatObject.isChannel(chat) && !chat.megagroup) {
                                    this.messageText = LocaleController.getString("ChannelJoined", 2131624911);
                                } else if (this.messageOwner.peer_id.channel_id != 0) {
                                    if (j2 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                        this.messageText = LocaleController.getString("ChannelMegaJoined", 2131624916);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserSelfMega", 2131624134), "un1", tLRPC$User3);
                                    }
                                } else if (isOut()) {
                                    this.messageText = LocaleController.getString("ActionAddUserSelfYou", 2131624135);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserSelf", 2131624133), "un1", tLRPC$User3);
                                }
                            } else if (isOut()) {
                                this.messageText = replaceWithLink(LocaleController.getString("ActionYouAddUser", 2131624226), "un2", user3);
                            } else if (j2 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                if (this.messageOwner.peer_id.channel_id != 0) {
                                    if (chat != null && chat.megagroup) {
                                        this.messageText = replaceWithLink(LocaleController.getString("MegaAddedBy", 2131626579), "un1", tLRPC$User3);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("ChannelAddedBy", 2131624873), "un1", tLRPC$User3);
                                    }
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserYou", 2131624136), "un1", tLRPC$User3);
                                }
                            } else {
                                CharSequence replaceWithLink5 = replaceWithLink(LocaleController.getString("ActionAddUser", 2131624132), "un2", user3);
                                this.messageText = replaceWithLink5;
                                this.messageText = replaceWithLink(replaceWithLink5, "un1", tLRPC$User3);
                            }
                        } else if (isOut()) {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionYouAddUser", 2131624226), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                        } else {
                            CharSequence replaceWithLink6 = replaceWithLink(LocaleController.getString("ActionAddUser", 2131624132), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                            this.messageText = replaceWithLink6;
                            this.messageText = replaceWithLink(replaceWithLink6, "un1", tLRPC$User3);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatJoinedByLink) {
                        if (isOut()) {
                            this.messageText = LocaleController.getString("ActionInviteYou", 2131624185);
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionInviteUser", 2131624184), "un1", tLRPC$User3);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditPhoto) {
                        TLRPC$Peer tLRPC$Peer3 = tLRPC$Message.peer_id;
                        if (tLRPC$Peer3 != null) {
                            long j4 = tLRPC$Peer3.channel_id;
                            if (j4 != 0) {
                                tLRPC$Chat5 = getChat(abstractMap2, longSparseArray2, j4);
                                if (!ChatObject.isChannel(tLRPC$Chat5) && !tLRPC$Chat5.megagroup) {
                                    if (isVideoAvatar()) {
                                        this.messageText = LocaleController.getString("ActionChannelChangedVideo", 2131624161);
                                    } else {
                                        this.messageText = LocaleController.getString("ActionChannelChangedPhoto", 2131624159);
                                    }
                                } else if (!isOut()) {
                                    if (isVideoAvatar()) {
                                        this.messageText = LocaleController.getString("ActionYouChangedVideo", 2131624229);
                                    } else {
                                        this.messageText = LocaleController.getString("ActionYouChangedPhoto", 2131624227);
                                    }
                                } else if (isVideoAvatar()) {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionChangedVideo", 2131624155), "un1", tLRPC$User3);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionChangedPhoto", 2131624153), "un1", tLRPC$User3);
                                }
                            }
                        }
                        tLRPC$Chat5 = null;
                        if (!ChatObject.isChannel(tLRPC$Chat5)) {
                        }
                        if (!isOut()) {
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditTitle) {
                        TLRPC$Peer tLRPC$Peer4 = tLRPC$Message.peer_id;
                        if (tLRPC$Peer4 != null) {
                            long j5 = tLRPC$Peer4.channel_id;
                            if (j5 != 0) {
                                tLRPC$Chat4 = getChat(abstractMap2, longSparseArray2, j5);
                                if (!ChatObject.isChannel(tLRPC$Chat4) && !tLRPC$Chat4.megagroup) {
                                    this.messageText = LocaleController.getString("ActionChannelChangedTitle", 2131624160).replace("un2", this.messageOwner.action.title);
                                } else if (!isOut()) {
                                    this.messageText = LocaleController.getString("ActionYouChangedTitle", 2131624228).replace("un2", this.messageOwner.action.title);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionChangedTitle", 2131624154).replace("un2", this.messageOwner.action.title), "un1", tLRPC$User3);
                                }
                            }
                        }
                        tLRPC$Chat4 = null;
                        if (!ChatObject.isChannel(tLRPC$Chat4)) {
                        }
                        if (!isOut()) {
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatDeletePhoto) {
                        TLRPC$Peer tLRPC$Peer5 = tLRPC$Message.peer_id;
                        if (tLRPC$Peer5 != null) {
                            long j6 = tLRPC$Peer5.channel_id;
                            if (j6 != 0) {
                                tLRPC$Chat3 = getChat(abstractMap2, longSparseArray2, j6);
                                if (!ChatObject.isChannel(tLRPC$Chat3) && !tLRPC$Chat3.megagroup) {
                                    this.messageText = LocaleController.getString("ActionChannelRemovedPhoto", 2131624162);
                                } else if (!isOut()) {
                                    this.messageText = LocaleController.getString("ActionYouRemovedPhoto", 2131624233);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionRemovedPhoto", 2131624208), "un1", tLRPC$User3);
                                }
                            }
                        }
                        tLRPC$Chat3 = null;
                        if (!ChatObject.isChannel(tLRPC$Chat3)) {
                        }
                        if (!isOut()) {
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTTLChange) {
                        if (tLRPC$MessageAction.ttl != 0) {
                            if (isOut()) {
                                this.messageText = LocaleController.formatString("MessageLifetimeChangedOutgoing", 2131626626, LocaleController.formatTTLString(this.messageOwner.action.ttl));
                            } else {
                                this.messageText = LocaleController.formatString("MessageLifetimeChanged", 2131626625, UserObject.getFirstName(tLRPC$User2), LocaleController.formatTTLString(this.messageOwner.action.ttl));
                            }
                        } else if (isOut()) {
                            this.messageText = LocaleController.getString("MessageLifetimeYouRemoved", 2131626630);
                        } else {
                            this.messageText = LocaleController.formatString("MessageLifetimeRemoved", 2131626628, UserObject.getFirstName(tLRPC$User2));
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetMessagesTTL) {
                        TLRPC$TL_messageActionSetMessagesTTL tLRPC$TL_messageActionSetMessagesTTL = (TLRPC$TL_messageActionSetMessagesTTL) tLRPC$MessageAction;
                        TLRPC$Peer tLRPC$Peer6 = tLRPC$Message.peer_id;
                        if (tLRPC$Peer6 != null) {
                            long j7 = tLRPC$Peer6.channel_id;
                            if (j7 != 0) {
                                tLRPC$Chat2 = getChat(abstractMap2, longSparseArray2, j7);
                                if (tLRPC$Chat2 == null && !tLRPC$Chat2.megagroup) {
                                    int i5 = tLRPC$TL_messageActionSetMessagesTTL.period;
                                    if (i5 != 0) {
                                        this.messageText = LocaleController.formatString("ActionTTLChannelChanged", 2131624211, LocaleController.formatTTLString(i5));
                                    } else {
                                        this.messageText = LocaleController.getString("ActionTTLChannelDisabled", 2131624212);
                                    }
                                } else if (tLRPC$TL_messageActionSetMessagesTTL.period == 0) {
                                    if (isOut()) {
                                        this.messageText = LocaleController.formatString("ActionTTLYouChanged", 2131624214, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period));
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.formatString("ActionTTLChanged", 2131624210, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period)), "un1", tLRPC$User3);
                                    }
                                } else if (isOut()) {
                                    this.messageText = LocaleController.getString("ActionTTLYouDisabled", 2131624215);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionTTLDisabled", 2131624213), "un1", tLRPC$User3);
                                }
                            }
                        }
                        tLRPC$Chat2 = null;
                        if (tLRPC$Chat2 == null) {
                        }
                        if (tLRPC$TL_messageActionSetMessagesTTL.period == 0) {
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                        long j8 = tLRPC$Message.date * 1000;
                        String formatString = (LocaleController.getInstance().formatterDay == null || LocaleController.getInstance().formatterYear == null) ? "" + this.messageOwner.date : LocaleController.formatString("formatDateAtTime", 2131629349, LocaleController.getInstance().formatterYear.format(j8), LocaleController.getInstance().formatterDay.format(j8));
                        TLRPC$User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                        if (currentUser == null) {
                            currentUser = getUser(abstractMap, longSparseArray, this.messageOwner.peer_id.user_id);
                        }
                        String firstName = currentUser != null ? UserObject.getFirstName(currentUser) : "";
                        TLRPC$MessageAction tLRPC$MessageAction2 = this.messageOwner.action;
                        this.messageText = LocaleController.formatString("NotificationUnrecognizedDevice", 2131627015, firstName, formatString, tLRPC$MessageAction2.title, tLRPC$MessageAction2.address);
                    } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                        this.messageText = LocaleController.formatString("NotificationContactJoined", 2131626944, UserObject.getUserName(tLRPC$User2));
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserUpdatedPhoto) {
                        this.messageText = LocaleController.formatString("NotificationContactNewPhoto", 2131626945, UserObject.getUserName(tLRPC$User2));
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageEncryptedAction) {
                        TLRPC$DecryptedMessageAction tLRPC$DecryptedMessageAction = tLRPC$MessageAction.encryptedAction;
                        if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionScreenshotMessages) {
                            if (isOut()) {
                                this.messageText = LocaleController.formatString("ActionTakeScreenshootYou", 2131624217, new Object[0]);
                            } else {
                                this.messageText = replaceWithLink(LocaleController.getString("ActionTakeScreenshoot", 2131624216), "un1", tLRPC$User3);
                            }
                        } else if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionSetMessageTTL) {
                            TLRPC$TL_decryptedMessageActionSetMessageTTL tLRPC$TL_decryptedMessageActionSetMessageTTL = (TLRPC$TL_decryptedMessageActionSetMessageTTL) tLRPC$DecryptedMessageAction;
                            if (tLRPC$TL_decryptedMessageActionSetMessageTTL.ttl_seconds != 0) {
                                if (isOut()) {
                                    this.messageText = LocaleController.formatString("MessageLifetimeChangedOutgoing", 2131626626, LocaleController.formatTTLString(tLRPC$TL_decryptedMessageActionSetMessageTTL.ttl_seconds));
                                } else {
                                    this.messageText = LocaleController.formatString("MessageLifetimeChanged", 2131626625, UserObject.getFirstName(tLRPC$User2), LocaleController.formatTTLString(tLRPC$TL_decryptedMessageActionSetMessageTTL.ttl_seconds));
                                }
                            } else if (isOut()) {
                                this.messageText = LocaleController.getString("MessageLifetimeYouRemoved", 2131626630);
                            } else {
                                this.messageText = LocaleController.formatString("MessageLifetimeRemoved", 2131626628, UserObject.getFirstName(tLRPC$User2));
                            }
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionScreenshotTaken) {
                        if (isOut()) {
                            this.messageText = LocaleController.formatString("ActionTakeScreenshootYou", 2131624217, new Object[0]);
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionTakeScreenshoot", 2131624216), "un1", tLRPC$User3);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionCreatedBroadcastList) {
                        this.messageText = LocaleController.formatString("YouCreatedBroadcastList", 2131629268, new Object[0]);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChannelCreate) {
                        TLRPC$Peer tLRPC$Peer7 = tLRPC$Message.peer_id;
                        if (tLRPC$Peer7 != null) {
                            long j9 = tLRPC$Peer7.channel_id;
                            if (j9 != 0) {
                                tLRPC$Chat = getChat(abstractMap2, longSparseArray2, j9);
                                if (!ChatObject.isChannel(tLRPC$Chat) && tLRPC$Chat.megagroup) {
                                    this.messageText = LocaleController.getString("ActionCreateMega", 2131624165);
                                } else {
                                    this.messageText = LocaleController.getString("ActionCreateChannel", 2131624163);
                                }
                            }
                        }
                        tLRPC$Chat = null;
                        if (!ChatObject.isChannel(tLRPC$Chat)) {
                        }
                        this.messageText = LocaleController.getString("ActionCreateChannel", 2131624163);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatMigrateTo) {
                        this.messageText = LocaleController.getString("ActionMigrateFromGroup", 2131624189);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChannelMigrateFrom) {
                        this.messageText = LocaleController.getString("ActionMigrateFromGroup", 2131624189);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPinMessage) {
                        generatePinMessageText(tLRPC$User2, tLRPC$User2 == null ? getChat(abstractMap2, longSparseArray2, tLRPC$Message.peer_id.channel_id) : null);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionHistoryClear) {
                        this.messageText = LocaleController.getString("HistoryCleared", 2131626145);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGameScore) {
                        generateGameMessageText(tLRPC$User2);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) {
                        TLRPC$TL_messageActionPhoneCall tLRPC$TL_messageActionPhoneCall = (TLRPC$TL_messageActionPhoneCall) tLRPC$MessageAction;
                        boolean z = tLRPC$TL_messageActionPhoneCall.reason instanceof TLRPC$TL_phoneCallDiscardReasonMissed;
                        if (!isFromUser() || this.messageOwner.from_id.user_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            if (z) {
                                if (tLRPC$TL_messageActionPhoneCall.video) {
                                    this.messageText = LocaleController.getString("CallMessageVideoIncomingMissed", 2131624794);
                                } else {
                                    this.messageText = LocaleController.getString("CallMessageIncomingMissed", 2131624788);
                                }
                            } else if (tLRPC$TL_messageActionPhoneCall.reason instanceof TLRPC$TL_phoneCallDiscardReasonBusy) {
                                if (tLRPC$TL_messageActionPhoneCall.video) {
                                    this.messageText = LocaleController.getString("CallMessageVideoIncomingDeclined", 2131624793);
                                } else {
                                    this.messageText = LocaleController.getString("CallMessageIncomingDeclined", 2131624787);
                                }
                            } else if (tLRPC$TL_messageActionPhoneCall.video) {
                                this.messageText = LocaleController.getString("CallMessageVideoIncoming", 2131624792);
                            } else {
                                this.messageText = LocaleController.getString("CallMessageIncoming", 2131624786);
                            }
                        } else if (z) {
                            if (tLRPC$TL_messageActionPhoneCall.video) {
                                this.messageText = LocaleController.getString("CallMessageVideoOutgoingMissed", 2131624796);
                            } else {
                                this.messageText = LocaleController.getString("CallMessageOutgoingMissed", 2131624790);
                            }
                        } else if (tLRPC$TL_messageActionPhoneCall.video) {
                            this.messageText = LocaleController.getString("CallMessageVideoOutgoing", 2131624795);
                        } else {
                            this.messageText = LocaleController.getString("CallMessageOutgoing", 2131624789);
                        }
                        int i6 = tLRPC$TL_messageActionPhoneCall.duration;
                        if (i6 > 0) {
                            String formatCallDuration = LocaleController.formatCallDuration(i6);
                            String formatString2 = LocaleController.formatString("CallMessageWithDuration", 2131624797, this.messageText, formatCallDuration);
                            this.messageText = formatString2;
                            String charSequence = formatString2.toString();
                            int indexOf = charSequence.indexOf(formatCallDuration);
                            if (indexOf != -1) {
                                SpannableString spannableString = new SpannableString(this.messageText);
                                int length = formatCallDuration.length() + indexOf;
                                if (indexOf > 0 && charSequence.charAt(indexOf - 1) == '(') {
                                    indexOf--;
                                }
                                if (length < charSequence.length() && charSequence.charAt(length) == ')') {
                                    length++;
                                }
                                spannableString.setSpan(new TypefaceSpan(Typeface.DEFAULT), indexOf, length, 0);
                                this.messageText = spannableString;
                            }
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPaymentSent) {
                        generatePaymentSentMessageText(getUser(abstractMap, longSparseArray, getDialogId()));
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionBotAllowed) {
                        String str5 = ((TLRPC$TL_messageActionBotAllowed) tLRPC$MessageAction).domain;
                        String string = LocaleController.getString("ActionBotAllowed", 2131624137);
                        int indexOf2 = string.indexOf("%1$s");
                        SpannableString spannableString2 = new SpannableString(String.format(string, str5));
                        if (indexOf2 >= 0) {
                            spannableString2.setSpan(new URLSpanNoUnderlineBold("http://" + str5), indexOf2, str5.length() + indexOf2, 33);
                        }
                        this.messageText = spannableString2;
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSecureValuesSent) {
                        TLRPC$TL_messageActionSecureValuesSent tLRPC$TL_messageActionSecureValuesSent = (TLRPC$TL_messageActionSecureValuesSent) tLRPC$MessageAction;
                        StringBuilder sb = new StringBuilder();
                        int size = tLRPC$TL_messageActionSecureValuesSent.types.size();
                        for (int i7 = 0; i7 < size; i7++) {
                            TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_messageActionSecureValuesSent.types.get(i7);
                            if (sb.length() > 0) {
                                sb.append(", ");
                            }
                            if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone) {
                                sb.append(LocaleController.getString("ActionBotDocumentPhone", 2131624147));
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail) {
                                sb.append(LocaleController.getString("ActionBotDocumentEmail", 2131624141));
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress) {
                                sb.append(LocaleController.getString("ActionBotDocumentAddress", 2131624138));
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                                sb.append(LocaleController.getString("ActionBotDocumentIdentity", 2131624142));
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) {
                                sb.append(LocaleController.getString("ActionBotDocumentPassport", 2131624145));
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense) {
                                sb.append(LocaleController.getString("ActionBotDocumentDriverLicence", 2131624140));
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard) {
                                sb.append(LocaleController.getString("ActionBotDocumentIdentityCard", 2131624143));
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                                sb.append(LocaleController.getString("ActionBotDocumentUtilityBill", 2131624150));
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeBankStatement) {
                                sb.append(LocaleController.getString("ActionBotDocumentBankStatement", 2131624139));
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                                sb.append(LocaleController.getString("ActionBotDocumentRentalAgreement", 2131624148));
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport) {
                                sb.append(LocaleController.getString("ActionBotDocumentInternalPassport", 2131624144));
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                                sb.append(LocaleController.getString("ActionBotDocumentPassportRegistration", 2131624146));
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                                sb.append(LocaleController.getString("ActionBotDocumentTemporaryRegistration", 2131624149));
                            }
                        }
                        TLRPC$Peer tLRPC$Peer8 = this.messageOwner.peer_id;
                        this.messageText = LocaleController.formatString("ActionBotDocuments", 2131624151, UserObject.getFirstName(tLRPC$Peer8 != null ? getUser(abstractMap, longSparseArray, tLRPC$Peer8.user_id) : null), sb.toString());
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionWebViewDataSent) {
                        this.messageText = LocaleController.formatString("ActionBotWebViewData", 2131624152, ((TLRPC$TL_messageActionWebViewDataSent) tLRPC$MessageAction).text);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatTheme) {
                        String str6 = ((TLRPC$TL_messageActionSetChatTheme) tLRPC$MessageAction).emoticon;
                        String firstName2 = UserObject.getFirstName(tLRPC$User2);
                        boolean isUserSelf = UserObject.isUserSelf(tLRPC$User2);
                        if (TextUtils.isEmpty(str6)) {
                            if (isUserSelf) {
                                str3 = LocaleController.formatString("ChatThemeDisabledYou", 2131625037, new Object[0]);
                            } else {
                                str3 = LocaleController.formatString("ChatThemeDisabled", 2131625036, firstName2, str6);
                            }
                            this.messageText = str3;
                        } else {
                            if (isUserSelf) {
                                str2 = LocaleController.formatString("ChatThemeChangedYou", 2131625034, str6);
                            } else {
                                str2 = LocaleController.formatString("ChatThemeChangedTo", 2131625033, firstName2, str6);
                            }
                            this.messageText = str2;
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatJoinedByRequest) {
                        if (UserObject.isUserSelf(tLRPC$User2)) {
                            if (ChatObject.isChannelAndNotMegaGroup(this.messageOwner.peer_id.channel_id, this.currentAccount)) {
                                str = LocaleController.getString("RequestToJoinChannelApproved", 2131627969);
                            } else {
                                str = LocaleController.getString("RequestToJoinGroupApproved", 2131627973);
                            }
                            this.messageText = str;
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("UserAcceptedToGroupAction", 2131628798), "un1", tLRPC$User3);
                        }
                    }
                }
            } else {
                this.isRestrictedMessage = false;
                String restrictionReason = MessagesController.getRestrictionReason(tLRPC$Message.restriction_reason);
                if (!TextUtils.isEmpty(restrictionReason)) {
                    this.messageText = restrictionReason;
                    this.isRestrictedMessage = true;
                } else if (!isMediaEmpty()) {
                    TLRPC$Message tLRPC$Message3 = this.messageOwner;
                    TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message3.media;
                    if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDice) {
                        this.messageText = getDiceEmoji();
                    } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                        if (((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll.quiz) {
                            this.messageText = LocaleController.getString("QuizPoll", 2131627823);
                        } else {
                            this.messageText = LocaleController.getString("Poll", 2131627577);
                        }
                    } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
                        if (tLRPC$MessageMedia.ttl_seconds != 0 && !(tLRPC$Message3 instanceof TLRPC$TL_message_secret)) {
                            this.messageText = LocaleController.getString("AttachDestructingPhoto", 2131624479);
                        } else {
                            this.messageText = LocaleController.getString("AttachPhoto", 2131624502);
                        }
                    } else if (isVideo() || ((this.messageOwner.media instanceof TLRPC$TL_messageMediaDocument) && (getDocument() instanceof TLRPC$TL_documentEmpty) && this.messageOwner.media.ttl_seconds != 0)) {
                        TLRPC$Message tLRPC$Message4 = this.messageOwner;
                        if (tLRPC$Message4.media.ttl_seconds != 0 && !(tLRPC$Message4 instanceof TLRPC$TL_message_secret)) {
                            this.messageText = LocaleController.getString("AttachDestructingVideo", 2131624480);
                        } else {
                            this.messageText = LocaleController.getString("AttachVideo", 2131624508);
                        }
                    } else if (isVoice()) {
                        this.messageText = LocaleController.getString("AttachAudio", 2131624476);
                    } else if (isRoundVideo()) {
                        this.messageText = LocaleController.getString("AttachRound", 2131624504);
                    } else {
                        TLRPC$Message tLRPC$Message5 = this.messageOwner;
                        TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$Message5.media;
                        if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGeo) || (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaVenue)) {
                            this.messageText = LocaleController.getString("AttachLocation", 2131624492);
                        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGeoLive) {
                            this.messageText = LocaleController.getString("AttachLiveLocation", 2131624488);
                        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaContact) {
                            this.messageText = LocaleController.getString("AttachContact", 2131624478);
                            if (!TextUtils.isEmpty(this.messageOwner.media.vcard)) {
                                this.vCardData = VCardData.parse(this.messageOwner.media.vcard);
                            }
                        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGame) {
                            this.messageText = tLRPC$Message5.message;
                        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaInvoice) {
                            this.messageText = tLRPC$MessageMedia2.description;
                        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaUnsupported) {
                            this.messageText = LocaleController.getString("UnsupportedMedia", 2131628754);
                        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument) {
                            if (isSticker() || isAnimatedStickerDocument(getDocument(), true)) {
                                String stickerChar = getStickerChar();
                                if (stickerChar == null || stickerChar.length() <= 0) {
                                    this.messageText = LocaleController.getString("AttachSticker", 2131624505);
                                } else {
                                    this.messageText = String.format("%s %s", stickerChar, LocaleController.getString("AttachSticker", 2131624505));
                                }
                            } else if (isMusic()) {
                                this.messageText = LocaleController.getString("AttachMusic", 2131624501);
                            } else if (isGif()) {
                                this.messageText = LocaleController.getString("AttachGif", 2131624483);
                            } else {
                                String documentFileName = FileLoader.getDocumentFileName(getDocument());
                                if (!TextUtils.isEmpty(documentFileName)) {
                                    this.messageText = documentFileName;
                                } else {
                                    this.messageText = LocaleController.getString("AttachDocument", 2131624481);
                                }
                            }
                        }
                    }
                } else {
                    String str7 = this.messageOwner.message;
                    if (str7 != null) {
                        try {
                            if (str7.length() > 200) {
                                this.messageText = AndroidUtilities.BAD_CHARS_MESSAGE_LONG_PATTERN.matcher(this.messageOwner.message).replaceAll("\u200c");
                            } else {
                                this.messageText = AndroidUtilities.BAD_CHARS_MESSAGE_PATTERN.matcher(this.messageOwner.message).replaceAll("\u200c");
                            }
                        } catch (Throwable unused) {
                            this.messageText = this.messageOwner.message;
                        }
                    } else {
                        this.messageText = str7;
                    }
                }
            }
            if (this.messageText == null) {
                return;
            }
            this.messageText = "";
            return;
        } else {
            tLRPC$User2 = null;
        }
        tLRPC$User = null;
        if (tLRPC$User2 == null) {
        }
        tLRPC$Message = this.messageOwner;
        if (!(tLRPC$Message instanceof TLRPC$TL_messageService)) {
        }
        if (this.messageText == null) {
        }
    }

    public void setType() {
        int i = this.type;
        this.type = 1000;
        this.isRoundVideoCached = 0;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if ((tLRPC$Message instanceof TLRPC$TL_message) || (tLRPC$Message instanceof TLRPC$TL_messageForwarded_old2)) {
            if (this.isRestrictedMessage) {
                this.type = 0;
            } else if (this.emojiAnimatedSticker != null) {
                if (isSticker()) {
                    this.type = 13;
                } else {
                    this.type = 15;
                }
            } else if (isMediaEmpty()) {
                this.type = 0;
                if (TextUtils.isEmpty(this.messageText) && this.eventId == 0) {
                    this.messageText = "Empty message";
                }
            } else {
                TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
                if (tLRPC$MessageMedia.ttl_seconds != 0 && ((tLRPC$MessageMedia.photo instanceof TLRPC$TL_photoEmpty) || (getDocument() instanceof TLRPC$TL_documentEmpty))) {
                    this.contentType = 1;
                    this.type = 10;
                } else {
                    TLRPC$MessageMedia tLRPC$MessageMedia2 = this.messageOwner.media;
                    if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDice) {
                        this.type = 15;
                        if (tLRPC$MessageMedia2.document == null) {
                            tLRPC$MessageMedia2.document = new TLRPC$TL_document();
                            TLRPC$Document tLRPC$Document = this.messageOwner.media.document;
                            tLRPC$Document.file_reference = new byte[0];
                            tLRPC$Document.mime_type = "application/x-tgsdice";
                            tLRPC$Document.dc_id = Integer.MIN_VALUE;
                            tLRPC$Document.id = -2147483648L;
                            TLRPC$TL_documentAttributeImageSize tLRPC$TL_documentAttributeImageSize = new TLRPC$TL_documentAttributeImageSize();
                            tLRPC$TL_documentAttributeImageSize.w = 512;
                            tLRPC$TL_documentAttributeImageSize.h = 512;
                            this.messageOwner.media.document.attributes.add(tLRPC$TL_documentAttributeImageSize);
                        }
                    } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto) {
                        this.type = 1;
                    } else if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGeo) || (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaVenue) || (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGeoLive)) {
                        this.type = 4;
                    } else if (isRoundVideo()) {
                        this.type = 5;
                    } else if (isVideo()) {
                        this.type = 3;
                    } else if (isVoice()) {
                        this.type = 2;
                    } else if (isMusic()) {
                        this.type = 14;
                    } else {
                        TLRPC$MessageMedia tLRPC$MessageMedia3 = this.messageOwner.media;
                        if (tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaContact) {
                            this.type = 12;
                        } else if (tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaPoll) {
                            this.type = 17;
                            this.checkedVotes = new ArrayList<>();
                        } else if (tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaUnsupported) {
                            this.type = 0;
                        } else if (tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaDocument) {
                            TLRPC$Document document = getDocument();
                            if (document != null && document.mime_type != null) {
                                if (isGifDocument(document, hasValidGroupId())) {
                                    this.type = 8;
                                } else if (isSticker()) {
                                    this.type = 13;
                                } else if (isAnimatedSticker()) {
                                    this.type = 15;
                                } else {
                                    this.type = 9;
                                }
                            } else {
                                this.type = 9;
                            }
                        } else if (tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaGame) {
                            this.type = 0;
                        } else if (tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaInvoice) {
                            this.type = 0;
                        }
                    }
                }
            }
        } else if (tLRPC$Message instanceof TLRPC$TL_messageService) {
            TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
            if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                this.type = 0;
            } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditPhoto) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserUpdatedPhoto)) {
                this.contentType = 1;
                this.type = 11;
            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageEncryptedAction) {
                TLRPC$DecryptedMessageAction tLRPC$DecryptedMessageAction = tLRPC$MessageAction.encryptedAction;
                if ((tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionScreenshotMessages) || (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionSetMessageTTL)) {
                    this.contentType = 1;
                    this.type = 10;
                } else {
                    this.contentType = -1;
                    this.type = -1;
                }
            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionHistoryClear) {
                this.contentType = -1;
                this.type = -1;
            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) {
                this.type = 16;
            } else {
                this.contentType = 1;
                this.type = 10;
            }
        }
        if (i == 1000 || i == this.type) {
            return;
        }
        updateMessageText(MessagesController.getInstance(this.currentAccount).getUsers(), MessagesController.getInstance(this.currentAccount).getChats(), null, null);
        generateThumbs(false);
    }

    public boolean checkLayout() {
        CharSequence charSequence;
        TextPaint textPaint;
        if (this.type == 0 && this.messageOwner.peer_id != null && (charSequence = this.messageText) != null && charSequence.length() != 0) {
            if (this.layoutCreated) {
                if (Math.abs(this.generatedWithMinSize - (AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() : AndroidUtilities.displaySize.x)) > AndroidUtilities.dp(52.0f) || this.generatedWithDensity != AndroidUtilities.density) {
                    this.layoutCreated = false;
                }
            }
            if (!this.layoutCreated) {
                this.layoutCreated = true;
                int[] iArr = null;
                TLRPC$User user = isFromUser() ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id)) : null;
                if (this.messageOwner.media instanceof TLRPC$TL_messageMediaGame) {
                    textPaint = Theme.chat_msgGameTextPaint;
                } else {
                    textPaint = Theme.chat_msgTextPaint;
                }
                if (allowsBigEmoji()) {
                    iArr = new int[1];
                }
                this.messageText = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, iArr, this.contentType == 0, this.viewRef);
                checkEmojiOnly(iArr);
                generateLayout(user);
                return true;
            }
        }
        return false;
    }

    public void resetLayout() {
        this.layoutCreated = false;
    }

    public String getMimeType() {
        TLRPC$Document document = getDocument();
        if (document != null) {
            return document.mime_type;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice)) {
            return tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto ? "image/jpeg" : (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) || tLRPC$MessageMedia.webpage.photo == null) ? "" : "image/jpeg";
        }
        TLRPC$WebDocument tLRPC$WebDocument = ((TLRPC$TL_messageMediaInvoice) tLRPC$MessageMedia).photo;
        return tLRPC$WebDocument != null ? tLRPC$WebDocument.mime_type : "";
    }

    public boolean canPreviewDocument() {
        return canPreviewDocument(getDocument());
    }

    public static boolean isAnimatedStickerDocument(TLRPC$Document tLRPC$Document) {
        return tLRPC$Document != null && tLRPC$Document.mime_type.equals("video/webm");
    }

    public static boolean isGifDocument(WebFile webFile) {
        return webFile != null && (webFile.mime_type.equals("image/gif") || isNewGifDocument(webFile));
    }

    public static boolean isGifDocument(TLRPC$Document tLRPC$Document) {
        return isGifDocument(tLRPC$Document, false);
    }

    public static boolean isGifDocument(TLRPC$Document tLRPC$Document, boolean z) {
        String str;
        return (tLRPC$Document == null || (str = tLRPC$Document.mime_type) == null || ((!str.equals("image/gif") || z) && !isNewGifDocument(tLRPC$Document))) ? false : true;
    }

    public static boolean isDocumentHasThumb(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null && !tLRPC$Document.thumbs.isEmpty()) {
            int size = tLRPC$Document.thumbs.size();
            for (int i = 0; i < size; i++) {
                TLRPC$PhotoSize tLRPC$PhotoSize = tLRPC$Document.thumbs.get(i);
                if (tLRPC$PhotoSize != null && !(tLRPC$PhotoSize instanceof TLRPC$TL_photoSizeEmpty) && !(tLRPC$PhotoSize.location instanceof TLRPC$TL_fileLocationUnavailable)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canPreviewDocument(TLRPC$Document tLRPC$Document) {
        String str;
        if (tLRPC$Document != null && (str = tLRPC$Document.mime_type) != null) {
            String lowerCase = str.toLowerCase();
            if ((isDocumentHasThumb(tLRPC$Document) && (lowerCase.equals("image/png") || lowerCase.equals("image/jpg") || lowerCase.equals("image/jpeg"))) || (Build.VERSION.SDK_INT >= 26 && lowerCase.equals("image/heic"))) {
                for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                    TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                    if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeImageSize) {
                        TLRPC$TL_documentAttributeImageSize tLRPC$TL_documentAttributeImageSize = (TLRPC$TL_documentAttributeImageSize) tLRPC$DocumentAttribute;
                        return tLRPC$TL_documentAttributeImageSize.w < 6000 && tLRPC$TL_documentAttributeImageSize.h < 6000;
                    }
                }
            } else if (BuildVars.DEBUG_PRIVATE_VERSION) {
                String documentFileName = FileLoader.getDocumentFileName(tLRPC$Document);
                if ((documentFileName.startsWith("tg_secret_sticker") && documentFileName.endsWith("json")) || documentFileName.endsWith(".svg")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isRoundVideoDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null && "video/mp4".equals(tLRPC$Document.mime_type)) {
            boolean z = false;
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < tLRPC$Document.attributes.size(); i3++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i3);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                    i = tLRPC$DocumentAttribute.w;
                    i2 = tLRPC$DocumentAttribute.h;
                    z = tLRPC$DocumentAttribute.round_message;
                }
            }
            if (z && i <= 1280 && i2 <= 1280) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNewGifDocument(WebFile webFile) {
        if (webFile != null && "video/mp4".equals(webFile.mime_type)) {
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < webFile.attributes.size(); i3++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = webFile.attributes.get(i3);
                if (!(tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAnimated) && (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo)) {
                    i = tLRPC$DocumentAttribute.w;
                    i2 = tLRPC$DocumentAttribute.h;
                }
            }
            if (i <= 1280 && i2 <= 1280) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNewGifDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null && "video/mp4".equals(tLRPC$Document.mime_type)) {
            boolean z = false;
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < tLRPC$Document.attributes.size(); i3++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i3);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAnimated) {
                    z = true;
                } else if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                    i = tLRPC$DocumentAttribute.w;
                    i2 = tLRPC$DocumentAttribute.h;
                }
            }
            if (z && i <= 1280 && i2 <= 1280) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSystemSignUp(MessageObject messageObject) {
        if (messageObject != null) {
            TLRPC$Message tLRPC$Message = messageObject.messageOwner;
            if ((tLRPC$Message instanceof TLRPC$TL_messageService) && (((TLRPC$TL_messageService) tLRPC$Message).action instanceof TLRPC$TL_messageActionContactSignUp)) {
                return true;
            }
        }
        return false;
    }

    public void generateThumbs(boolean z) {
        ArrayList<TLRPC$PhotoSize> arrayList;
        ArrayList<TLRPC$PhotoSize> arrayList2;
        ArrayList<TLRPC$PhotoSize> arrayList3;
        ArrayList<TLRPC$PhotoSize> arrayList4;
        ArrayList<TLRPC$PhotoSize> arrayList5;
        ArrayList<TLRPC$PhotoSize> arrayList6;
        ArrayList<TLRPC$PhotoSize> arrayList7;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message instanceof TLRPC$TL_messageService) {
            TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
            if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditPhoto)) {
                return;
            }
            TLRPC$Photo tLRPC$Photo = tLRPC$MessageAction.photo;
            if (!z) {
                this.photoThumbs = new ArrayList<>(tLRPC$Photo.sizes);
            } else {
                ArrayList<TLRPC$PhotoSize> arrayList8 = this.photoThumbs;
                if (arrayList8 != null && !arrayList8.isEmpty()) {
                    for (int i = 0; i < this.photoThumbs.size(); i++) {
                        TLRPC$PhotoSize tLRPC$PhotoSize = this.photoThumbs.get(i);
                        int i2 = 0;
                        while (true) {
                            if (i2 < tLRPC$Photo.sizes.size()) {
                                TLRPC$PhotoSize tLRPC$PhotoSize2 = tLRPC$Photo.sizes.get(i2);
                                if (!(tLRPC$PhotoSize2 instanceof TLRPC$TL_photoSizeEmpty) && tLRPC$PhotoSize2.type.equals(tLRPC$PhotoSize.type)) {
                                    tLRPC$PhotoSize.location = tLRPC$PhotoSize2.location;
                                    break;
                                }
                                i2++;
                            }
                        }
                    }
                }
            }
            if (tLRPC$Photo.dc_id != 0 && (arrayList7 = this.photoThumbs) != null) {
                int size = arrayList7.size();
                for (int i3 = 0; i3 < size; i3++) {
                    TLRPC$FileLocation tLRPC$FileLocation = this.photoThumbs.get(i3).location;
                    if (tLRPC$FileLocation != null) {
                        tLRPC$FileLocation.dc_id = tLRPC$Photo.dc_id;
                        tLRPC$FileLocation.file_reference = tLRPC$Photo.file_reference;
                    }
                }
            }
            this.photoThumbsObject = this.messageOwner.action.photo;
        } else if (this.emojiAnimatedSticker != null) {
            if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor) || !isDocumentHasThumb(this.emojiAnimatedSticker)) {
                return;
            }
            if (!z || (arrayList6 = this.photoThumbs) == null) {
                ArrayList<TLRPC$PhotoSize> arrayList9 = new ArrayList<>();
                this.photoThumbs = arrayList9;
                arrayList9.addAll(this.emojiAnimatedSticker.thumbs);
            } else if (!arrayList6.isEmpty()) {
                updatePhotoSizeLocations(this.photoThumbs, this.emojiAnimatedSticker.thumbs);
            }
            this.photoThumbsObject = this.emojiAnimatedSticker;
        } else {
            TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
            if (tLRPC$MessageMedia == null || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaEmpty)) {
                return;
            }
            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
                TLRPC$Photo tLRPC$Photo2 = tLRPC$MessageMedia.photo;
                if (!z || ((arrayList5 = this.photoThumbs) != null && arrayList5.size() != tLRPC$Photo2.sizes.size())) {
                    this.photoThumbs = new ArrayList<>(tLRPC$Photo2.sizes);
                } else {
                    ArrayList<TLRPC$PhotoSize> arrayList10 = this.photoThumbs;
                    if (arrayList10 != null && !arrayList10.isEmpty()) {
                        for (int i4 = 0; i4 < this.photoThumbs.size(); i4++) {
                            TLRPC$PhotoSize tLRPC$PhotoSize3 = this.photoThumbs.get(i4);
                            if (tLRPC$PhotoSize3 != null) {
                                int i5 = 0;
                                while (true) {
                                    if (i5 >= tLRPC$Photo2.sizes.size()) {
                                        break;
                                    }
                                    TLRPC$PhotoSize tLRPC$PhotoSize4 = tLRPC$Photo2.sizes.get(i5);
                                    if (tLRPC$PhotoSize4 != null && !(tLRPC$PhotoSize4 instanceof TLRPC$TL_photoSizeEmpty)) {
                                        if (tLRPC$PhotoSize4.type.equals(tLRPC$PhotoSize3.type)) {
                                            tLRPC$PhotoSize3.location = tLRPC$PhotoSize4.location;
                                            break;
                                        } else if ("s".equals(tLRPC$PhotoSize3.type) && (tLRPC$PhotoSize4 instanceof TLRPC$TL_photoStrippedSize)) {
                                            this.photoThumbs.set(i4, tLRPC$PhotoSize4);
                                            break;
                                        }
                                    }
                                    i5++;
                                }
                            }
                        }
                    }
                }
                this.photoThumbsObject = this.messageOwner.media.photo;
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                TLRPC$Document document = getDocument();
                if (!isDocumentHasThumb(document)) {
                    return;
                }
                if (!z || (arrayList4 = this.photoThumbs) == null) {
                    ArrayList<TLRPC$PhotoSize> arrayList11 = new ArrayList<>();
                    this.photoThumbs = arrayList11;
                    arrayList11.addAll(document.thumbs);
                } else if (!arrayList4.isEmpty()) {
                    updatePhotoSizeLocations(this.photoThumbs, document.thumbs);
                }
                this.photoThumbsObject = document;
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.game.document;
                if (tLRPC$Document != null && isDocumentHasThumb(tLRPC$Document)) {
                    if (!z) {
                        ArrayList<TLRPC$PhotoSize> arrayList12 = new ArrayList<>();
                        this.photoThumbs = arrayList12;
                        arrayList12.addAll(tLRPC$Document.thumbs);
                    } else {
                        ArrayList<TLRPC$PhotoSize> arrayList13 = this.photoThumbs;
                        if (arrayList13 != null && !arrayList13.isEmpty()) {
                            updatePhotoSizeLocations(this.photoThumbs, tLRPC$Document.thumbs);
                        }
                    }
                    this.photoThumbsObject = tLRPC$Document;
                }
                TLRPC$Photo tLRPC$Photo3 = this.messageOwner.media.game.photo;
                if (tLRPC$Photo3 != null) {
                    if (!z || (arrayList3 = this.photoThumbs2) == null) {
                        this.photoThumbs2 = new ArrayList<>(tLRPC$Photo3.sizes);
                    } else if (!arrayList3.isEmpty()) {
                        updatePhotoSizeLocations(this.photoThumbs2, tLRPC$Photo3.sizes);
                    }
                    this.photoThumbsObject2 = tLRPC$Photo3;
                }
                if (this.photoThumbs != null || (arrayList2 = this.photoThumbs2) == null) {
                    return;
                }
                this.photoThumbs = arrayList2;
                this.photoThumbs2 = null;
                this.photoThumbsObject = this.photoThumbsObject2;
                this.photoThumbsObject2 = null;
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
                TLRPC$WebPage tLRPC$WebPage = tLRPC$MessageMedia.webpage;
                TLRPC$Photo tLRPC$Photo4 = tLRPC$WebPage.photo;
                TLRPC$Document tLRPC$Document2 = tLRPC$WebPage.document;
                if (tLRPC$Photo4 != null) {
                    if (!z || (arrayList = this.photoThumbs) == null) {
                        this.photoThumbs = new ArrayList<>(tLRPC$Photo4.sizes);
                    } else if (!arrayList.isEmpty()) {
                        updatePhotoSizeLocations(this.photoThumbs, tLRPC$Photo4.sizes);
                    }
                    this.photoThumbsObject = tLRPC$Photo4;
                } else if (tLRPC$Document2 != null && isDocumentHasThumb(tLRPC$Document2)) {
                    if (!z) {
                        ArrayList<TLRPC$PhotoSize> arrayList14 = new ArrayList<>();
                        this.photoThumbs = arrayList14;
                        arrayList14.addAll(tLRPC$Document2.thumbs);
                    } else {
                        ArrayList<TLRPC$PhotoSize> arrayList15 = this.photoThumbs;
                        if (arrayList15 != null && !arrayList15.isEmpty()) {
                            updatePhotoSizeLocations(this.photoThumbs, tLRPC$Document2.thumbs);
                        }
                    }
                    this.photoThumbsObject = tLRPC$Document2;
                }
            }
        }
    }

    private static void updatePhotoSizeLocations(ArrayList<TLRPC$PhotoSize> arrayList, ArrayList<TLRPC$PhotoSize> arrayList2) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TLRPC$PhotoSize tLRPC$PhotoSize = arrayList.get(i);
            if (tLRPC$PhotoSize != null) {
                int size2 = arrayList2.size();
                int i2 = 0;
                while (true) {
                    if (i2 < size2) {
                        TLRPC$PhotoSize tLRPC$PhotoSize2 = arrayList2.get(i2);
                        if (!(tLRPC$PhotoSize2 instanceof TLRPC$TL_photoSizeEmpty) && !(tLRPC$PhotoSize2 instanceof TLRPC$TL_photoCachedSize) && tLRPC$PhotoSize2 != null && tLRPC$PhotoSize2.type.equals(tLRPC$PhotoSize.type)) {
                            tLRPC$PhotoSize.location = tLRPC$PhotoSize2.location;
                            break;
                        }
                        i2++;
                    }
                }
            }
        }
    }

    public CharSequence replaceWithLink(CharSequence charSequence, String str, ArrayList<Long> arrayList, AbstractMap<Long, TLRPC$User> abstractMap, LongSparseArray<TLRPC$User> longSparseArray) {
        if (TextUtils.indexOf(charSequence, str) >= 0) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("");
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC$User tLRPC$User = null;
                if (abstractMap != null) {
                    tLRPC$User = abstractMap.get(arrayList.get(i));
                } else if (longSparseArray != null) {
                    tLRPC$User = longSparseArray.get(arrayList.get(i).longValue());
                }
                if (tLRPC$User == null) {
                    tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(arrayList.get(i));
                }
                if (tLRPC$User != null) {
                    String userName = UserObject.getUserName(tLRPC$User);
                    int length = spannableStringBuilder.length();
                    if (spannableStringBuilder.length() != 0) {
                        spannableStringBuilder.append((CharSequence) ", ");
                    }
                    spannableStringBuilder.append((CharSequence) userName);
                    spannableStringBuilder.setSpan(new URLSpanNoUnderlineBold("" + tLRPC$User.id), length, userName.length() + length, 33);
                }
            }
            return TextUtils.replace(charSequence, new String[]{str}, new CharSequence[]{spannableStringBuilder});
        }
        return charSequence;
    }

    public static CharSequence replaceWithLink(CharSequence charSequence, String str, TLObject tLObject) {
        String str2;
        String str3;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$User tLRPC$User;
        int indexOf = TextUtils.indexOf(charSequence, str);
        if (indexOf >= 0) {
            TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported = null;
            if (tLObject instanceof TLRPC$User) {
                str3 = UserObject.getUserName((TLRPC$User) tLObject);
                str2 = "" + tLRPC$User.id;
            } else if (tLObject instanceof TLRPC$Chat) {
                str3 = ((TLRPC$Chat) tLObject).title;
                str2 = "" + (-tLRPC$Chat.id);
            } else if (tLObject instanceof TLRPC$TL_game) {
                str3 = ((TLRPC$TL_game) tLObject).title;
                str2 = "game";
            } else if (tLObject instanceof TLRPC$TL_chatInviteExported) {
                tLRPC$TL_chatInviteExported = (TLRPC$TL_chatInviteExported) tLObject;
                str3 = tLRPC$TL_chatInviteExported.link;
                str2 = "invite";
            } else {
                str2 = "0";
                str3 = "";
            }
            String replace = str3.replace('\n', ' ');
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(TextUtils.replace(charSequence, new String[]{str}, new String[]{replace}));
            URLSpanNoUnderlineBold uRLSpanNoUnderlineBold = new URLSpanNoUnderlineBold("" + str2);
            uRLSpanNoUnderlineBold.setObject(tLRPC$TL_chatInviteExported);
            spannableStringBuilder.setSpan(uRLSpanNoUnderlineBold, indexOf, replace.length() + indexOf, 33);
            return spannableStringBuilder;
        }
        return charSequence;
    }

    public String getExtension() {
        String fileName = getFileName();
        int lastIndexOf = fileName.lastIndexOf(46);
        String substring = lastIndexOf != -1 ? fileName.substring(lastIndexOf + 1) : null;
        if (substring == null || substring.length() == 0) {
            substring = getDocument().mime_type;
        }
        if (substring == null) {
            substring = "";
        }
        return substring.toUpperCase();
    }

    public String getFileName() {
        return getFileName(this.messageOwner);
    }

    public static String getFileName(TLRPC$Message tLRPC$Message) {
        TLRPC$PhotoSize closestPhotoSizeWithSize;
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
            return FileLoader.getAttachFileName(getDocument(tLRPC$Message));
        }
        if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto)) {
            return tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage ? FileLoader.getAttachFileName(tLRPC$MessageMedia.webpage.document) : "";
        }
        ArrayList<TLRPC$PhotoSize> arrayList = tLRPC$MessageMedia.photo.sizes;
        return (arrayList.size() <= 0 || (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize())) == null) ? "" : FileLoader.getAttachFileName(closestPhotoSizeWithSize);
    }

    public int getMediaType() {
        if (isVideo()) {
            return 2;
        }
        if (isVoice()) {
            return 1;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
            return 3;
        }
        return tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto ? 0 : 4;
    }

    private static boolean containsUrls(CharSequence charSequence) {
        if (charSequence != null && charSequence.length() >= 2 && charSequence.length() <= 20480) {
            int length = charSequence.length();
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            char c = 0;
            while (i < length) {
                char charAt = charSequence.charAt(i);
                if (charAt >= '0' && charAt <= '9') {
                    i2++;
                    if (i2 >= 6) {
                        return true;
                    }
                    i3 = 0;
                    i4 = 0;
                } else if (charAt == ' ' || i2 <= 0) {
                    i2 = 0;
                }
                if ((charAt != '@' && charAt != '#' && charAt != '/' && charAt != '$') || i != 0) {
                    if (i != 0) {
                        int i5 = i - 1;
                        if (charSequence.charAt(i5) != ' ') {
                            if (charSequence.charAt(i5) == '\n') {
                            }
                        }
                    }
                    if (charAt == ':') {
                        if (i3 == 0) {
                            i3 = 1;
                            i++;
                            c = charAt;
                        }
                        i3 = 0;
                        i++;
                        c = charAt;
                    } else {
                        if (charAt != '/') {
                            if (charAt == '.') {
                                if (i4 == 0 && c != ' ') {
                                    i4++;
                                }
                            } else if (charAt != ' ' && c == '.' && i4 == 1) {
                                return true;
                            }
                            i4 = 0;
                        } else if (i3 == 2) {
                            return true;
                        } else {
                            if (i3 == 1) {
                                i3++;
                            }
                            i3 = 0;
                        }
                        i++;
                        c = charAt;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x008e  */
    /* JADX WARN: Removed duplicated region for block: B:53:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateLinkDescription() {
        int i;
        int i2;
        if (this.linkDescription != null) {
            return;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            TLRPC$WebPage tLRPC$WebPage = tLRPC$MessageMedia.webpage;
            if ((tLRPC$WebPage instanceof TLRPC$TL_webPage) && tLRPC$WebPage.description != null) {
                this.linkDescription = Spannable.Factory.getInstance().newSpannable(this.messageOwner.media.webpage.description);
                String str = this.messageOwner.media.webpage.site_name;
                if (str != null) {
                    str = str.toLowerCase();
                }
                if ("instagram".equals(str)) {
                    i2 = 1;
                } else {
                    i2 = "twitter".equals(str) ? 2 : 0;
                }
                i = i2;
                if (!TextUtils.isEmpty(this.linkDescription)) {
                    return;
                }
                if (containsUrls(this.linkDescription)) {
                    try {
                        AndroidUtilities.addLinks((Spannable) this.linkDescription, 1);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                CharSequence replaceEmoji = Emoji.replaceEmoji(this.linkDescription, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, this.contentType == 0, this.viewRef);
                this.linkDescription = replaceEmoji;
                if (i == 0) {
                    return;
                }
                if (!(replaceEmoji instanceof Spannable)) {
                    this.linkDescription = new SpannableStringBuilder(this.linkDescription);
                }
                addUrlsByPattern(isOutOwner(), this.linkDescription, false, i, 0, false);
                return;
            }
        }
        if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) && tLRPC$MessageMedia.game.description != null) {
            this.linkDescription = Spannable.Factory.getInstance().newSpannable(this.messageOwner.media.game.description);
        } else if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) && tLRPC$MessageMedia.description != null) {
            this.linkDescription = Spannable.Factory.getInstance().newSpannable(this.messageOwner.media.description);
        }
        i = 0;
        if (!TextUtils.isEmpty(this.linkDescription)) {
        }
    }

    public CharSequence getVoiceTranscription() {
        String str;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null || (str = tLRPC$Message.voiceTranscription) == null) {
            return null;
        }
        if (TextUtils.isEmpty(str)) {
            SpannableString spannableString = new SpannableString(LocaleController.getString("NoWordsRecognized", 2131626880));
            spannableString.setSpan(new AnonymousClass1(), 0, spannableString.length(), 33);
            return spannableString;
        }
        String str2 = this.messageOwner.voiceTranscription;
        if (TextUtils.isEmpty(str2)) {
            return str2;
        }
        return Emoji.replaceEmoji(str2, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, this.contentType == 0, this.viewRef);
    }

    /* renamed from: org.telegram.messenger.MessageObject$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends CharacterStyle {
        AnonymousClass1() {
            MessageObject.this = r1;
        }

        @Override // android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            textPaint.setTextSize(textPaint.getTextSize() * 0.8f);
            textPaint.setColor(Theme.chat_timePaint.getColor());
        }
    }

    public float measureVoiceTranscriptionHeight() {
        StaticLayout staticLayout;
        CharSequence voiceTranscription = getVoiceTranscription();
        if (voiceTranscription == null) {
            return 0.0f;
        }
        int dp = AndroidUtilities.displaySize.x - AndroidUtilities.dp(needDrawAvatar() ? 147.0f : 95.0f);
        if (Build.VERSION.SDK_INT >= 24) {
            staticLayout = StaticLayout.Builder.obtain(voiceTranscription, 0, voiceTranscription.length(), Theme.chat_msgTextPaint, dp).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
        } else {
            staticLayout = new StaticLayout(voiceTranscription, Theme.chat_msgTextPaint, dp, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        return staticLayout.getHeight();
    }

    public boolean isVoiceTranscriptionOpen() {
        TLRPC$Message tLRPC$Message;
        return isVoice() && (tLRPC$Message = this.messageOwner) != null && tLRPC$Message.voiceTranscriptionOpen && tLRPC$Message.voiceTranscription != null && (tLRPC$Message.voiceTranscriptionFinal || TranscribeButton.isTranscribing(this)) && UserConfig.getInstance(this.currentAccount).isPremium();
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x0083, code lost:
        if (r9.messageOwner.send_state == 0) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0089, code lost:
        if (r9.messageOwner.id >= 0) goto L41;
     */
    /* JADX WARN: Removed duplicated region for block: B:43:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00cd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateCaption() {
        if (this.caption != null || isRoundVideo() || isMediaEmpty()) {
            return;
        }
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if ((tLRPC$Message.media instanceof TLRPC$TL_messageMediaGame) || TextUtils.isEmpty(tLRPC$Message.message)) {
            return;
        }
        boolean z = true;
        this.caption = Emoji.replaceEmoji(this.messageOwner.message, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, this.contentType == 0, this.viewRef);
        TLRPC$Message tLRPC$Message2 = this.messageOwner;
        if (!(tLRPC$Message2.send_state != 0 ? false : !tLRPC$Message2.entities.isEmpty())) {
            if (this.eventId == 0) {
                TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
                if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto_old)) {
                    if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto_layer68)) {
                        if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto_layer74)) {
                            if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument_old)) {
                                if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument_layer68)) {
                                    if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument_layer74)) {
                                        if (isOut()) {
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (z) {
                if (containsUrls(this.caption)) {
                    try {
                        AndroidUtilities.addLinks((Spannable) this.caption, 5);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                addUrlsByPattern(isOutOwner(), this.caption, true, 0, 0, true);
            }
            addEntitiesToText(this.caption, z);
            if (!isVideo()) {
                addUrlsByPattern(isOutOwner(), this.caption, true, 3, getDuration(), false);
                return;
            } else if (!isMusic() && !isVoice()) {
                return;
            } else {
                addUrlsByPattern(isOutOwner(), this.caption, true, 4, getDuration(), false);
                return;
            }
        }
        z = false;
        if (z) {
        }
        addEntitiesToText(this.caption, z);
        if (!isVideo()) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:82:0x01bb A[Catch: Exception -> 0x0214, TryCatch #0 {Exception -> 0x0214, blocks: (B:9:0x0011, B:11:0x0015, B:12:0x001d, B:13:0x0024, B:15:0x0028, B:16:0x0030, B:17:0x0037, B:19:0x003b, B:20:0x0043, B:21:0x0049, B:22:0x004c, B:24:0x0052, B:28:0x0061, B:32:0x0071, B:33:0x0073, B:42:0x0086, B:45:0x008c, B:46:0x00ae, B:49:0x00d4, B:50:0x00f5, B:51:0x0116, B:54:0x011e, B:58:0x012d, B:60:0x0136, B:62:0x0147, B:65:0x0186, B:70:0x0199, B:75:0x01a8, B:77:0x01b2, B:80:0x01b6, B:82:0x01bb, B:87:0x01c7, B:88:0x01de, B:89:0x01f4, B:92:0x01fa, B:94:0x0204, B:96:0x0207, B:97:0x020d), top: B:102:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01c5  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01f8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void addUrlsByPattern(boolean z, CharSequence charSequence, boolean z2, int i, int i2, boolean z3) {
        Matcher matcher;
        URLSpanNoUnderline uRLSpanNoUnderline;
        ClickableSpan[] clickableSpanArr;
        String str;
        URLSpan[] uRLSpanArr;
        int i3;
        if (charSequence == null) {
            return;
        }
        int i4 = 1;
        try {
            if (i == 3 || i == 4) {
                if (videoTimeUrlPattern == null) {
                    videoTimeUrlPattern = Pattern.compile("\\b(?:(\\d{1,2}):)?(\\d{1,3}):([0-5][0-9])\\b([^\\n]*)");
                }
                matcher = videoTimeUrlPattern.matcher(charSequence);
            } else if (i == 1) {
                if (instagramUrlPattern == null) {
                    instagramUrlPattern = Pattern.compile("(^|\\s|\\()@[a-zA-Z\\d_.]{1,32}|(^|\\s|\\()#[\\w.]+");
                }
                matcher = instagramUrlPattern.matcher(charSequence);
            } else {
                if (urlPattern == null) {
                    urlPattern = Pattern.compile("(^|\\s)/[a-zA-Z@\\d_]{1,255}|(^|\\s|\\()@[a-zA-Z\\d_]{1,32}|(^|\\s|\\()#[^0-9][\\w.]+|(^|\\s)\\$[A-Z]{3,8}([ ,.]|$)");
                }
                matcher = urlPattern.matcher(charSequence);
            }
            Spannable spannable = (Spannable) charSequence;
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                if (i != 3 && i != 4) {
                    char charAt = charSequence.charAt(start);
                    if (i != 0) {
                        if (charAt != '@' && charAt != '#') {
                            start++;
                        }
                        charAt = charSequence.charAt(start);
                        if (charAt != '@' && charAt != '#') {
                        }
                    } else if (charAt != '@' && charAt != '#' && charAt != '/' && charAt != '$') {
                        start++;
                    }
                    if (i == i4) {
                        if (charAt == '@') {
                            uRLSpanNoUnderline = new URLSpanNoUnderline("https://instagram.com/" + charSequence.subSequence(start + 1, end).toString());
                        } else {
                            uRLSpanNoUnderline = new URLSpanNoUnderline("https://www.instagram.com/explore/tags/" + charSequence.subSequence(start + 1, end).toString());
                        }
                    } else if (i == 2) {
                        if (charAt == '@') {
                            uRLSpanNoUnderline = new URLSpanNoUnderline("https://twitter.com/" + charSequence.subSequence(start + 1, end).toString());
                        } else {
                            uRLSpanNoUnderline = new URLSpanNoUnderline("https://twitter.com/hashtag/" + charSequence.subSequence(start + 1, end).toString());
                        }
                    } else if (charSequence.charAt(start) != '/') {
                        uRLSpanNoUnderline = new URLSpanNoUnderline(charSequence.subSequence(start, end).toString());
                    } else if (z2) {
                        uRLSpanNoUnderline = new URLSpanBotCommand(charSequence.subSequence(start, end).toString(), z ? 1 : 0);
                    } else {
                        uRLSpanNoUnderline = null;
                        if (uRLSpanNoUnderline != null) {
                            if (z3 && (clickableSpanArr = (ClickableSpan[]) spannable.getSpans(start, end, ClickableSpan.class)) != null && clickableSpanArr.length > 0) {
                                spannable.removeSpan(clickableSpanArr[0]);
                            }
                            spannable.setSpan(uRLSpanNoUnderline, start, end, 0);
                        }
                        i4 = 1;
                    }
                    if (uRLSpanNoUnderline != null) {
                    }
                    i4 = 1;
                }
                matcher.groupCount();
                int start2 = matcher.start(i4);
                int end2 = matcher.end(i4);
                int start3 = matcher.start(2);
                int end3 = matcher.end(2);
                int start4 = matcher.start(3);
                int end4 = matcher.end(3);
                int start5 = matcher.start(4);
                int end5 = matcher.end(4);
                int intValue = Utilities.parseInt(charSequence.subSequence(start3, end3)).intValue();
                int intValue2 = Utilities.parseInt(charSequence.subSequence(start4, end4)).intValue();
                int intValue3 = (start2 < 0 || end2 < 0) ? -1 : Utilities.parseInt(charSequence.subSequence(start2, end2)).intValue();
                if (start5 >= 0 && end5 >= 0) {
                    str = charSequence.subSequence(start5, end5).toString();
                    if (start5 < 0 || end5 >= 0) {
                        end = end4;
                    }
                    uRLSpanArr = (URLSpan[]) spannable.getSpans(start, end, URLSpan.class);
                    if (uRLSpanArr != null || uRLSpanArr.length <= 0) {
                        i3 = intValue2 + (intValue * 60);
                        if (intValue3 > 0) {
                            i3 += intValue3 * 60 * 60;
                        }
                        if (i3 <= i2) {
                            if (i == 3) {
                                uRLSpanNoUnderline = new URLSpanNoUnderline("video?" + i3);
                            } else {
                                uRLSpanNoUnderline = new URLSpanNoUnderline("audio?" + i3);
                            }
                            uRLSpanNoUnderline.label = str;
                            if (uRLSpanNoUnderline != null) {
                            }
                        }
                    }
                    i4 = 1;
                }
                str = null;
                if (start5 < 0) {
                }
                end = end4;
                uRLSpanArr = (URLSpan[]) spannable.getSpans(start, end, URLSpan.class);
                if (uRLSpanArr != null) {
                }
                i3 = intValue2 + (intValue * 60);
                if (intValue3 > 0) {
                }
                if (i3 <= i2) {
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static int[] getWebDocumentWidthAndHeight(TLRPC$WebDocument tLRPC$WebDocument) {
        if (tLRPC$WebDocument == null) {
            return null;
        }
        int size = tLRPC$WebDocument.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$WebDocument.attributes.get(i);
            if (!(tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeImageSize) && !(tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo)) {
            }
            return new int[]{tLRPC$DocumentAttribute.w, tLRPC$DocumentAttribute.h};
        }
        return null;
    }

    public static int getWebDocumentDuration(TLRPC$WebDocument tLRPC$WebDocument) {
        if (tLRPC$WebDocument == null) {
            return 0;
        }
        int size = tLRPC$WebDocument.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$WebDocument.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                return tLRPC$DocumentAttribute.duration;
            }
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                return tLRPC$DocumentAttribute.duration;
            }
        }
        return 0;
    }

    public static int[] getInlineResultWidthAndHeight(TLRPC$BotInlineResult tLRPC$BotInlineResult) {
        int[] webDocumentWidthAndHeight = getWebDocumentWidthAndHeight(tLRPC$BotInlineResult.content);
        if (webDocumentWidthAndHeight == null) {
            int[] webDocumentWidthAndHeight2 = getWebDocumentWidthAndHeight(tLRPC$BotInlineResult.thumb);
            return webDocumentWidthAndHeight2 == null ? new int[]{0, 0} : webDocumentWidthAndHeight2;
        }
        return webDocumentWidthAndHeight;
    }

    public static int getInlineResultDuration(TLRPC$BotInlineResult tLRPC$BotInlineResult) {
        int webDocumentDuration = getWebDocumentDuration(tLRPC$BotInlineResult.content);
        return webDocumentDuration == 0 ? getWebDocumentDuration(tLRPC$BotInlineResult.thumb) : webDocumentDuration;
    }

    public boolean hasValidGroupId() {
        ArrayList<TLRPC$PhotoSize> arrayList;
        return getGroupId() != 0 && (((arrayList = this.photoThumbs) != null && !arrayList.isEmpty()) || isMusic() || isDocument());
    }

    public long getGroupIdForUse() {
        long j = this.localSentGroupId;
        return j != 0 ? j : this.messageOwner.grouped_id;
    }

    public long getGroupId() {
        long j = this.localGroupId;
        return j != 0 ? j : getGroupIdForUse();
    }

    public static void addLinks(boolean z, CharSequence charSequence) {
        addLinks(z, charSequence, true, false);
    }

    public static void addLinks(boolean z, CharSequence charSequence, boolean z2, boolean z3) {
        addLinks(z, charSequence, z2, z3, false);
    }

    public static void addLinks(boolean z, CharSequence charSequence, boolean z2, boolean z3, boolean z4) {
        if (!(charSequence instanceof Spannable) || !containsUrls(charSequence)) {
            return;
        }
        if (charSequence.length() < 1000) {
            try {
                AndroidUtilities.addLinks((Spannable) charSequence, 5, z4);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else {
            try {
                AndroidUtilities.addLinks((Spannable) charSequence, 1, z4);
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        addUrlsByPattern(z, charSequence, z2, 0, 0, z3);
    }

    public void resetPlayingProgress() {
        this.audioProgress = 0.0f;
        this.audioProgressSec = 0;
        this.bufferedProgress = 0.0f;
    }

    private boolean addEntitiesToText(CharSequence charSequence, boolean z) {
        return addEntitiesToText(charSequence, false, z);
    }

    public boolean addEntitiesToText(CharSequence charSequence, boolean z, boolean z2) {
        if (charSequence == null) {
            return false;
        }
        if (this.isRestrictedMessage) {
            ArrayList arrayList = new ArrayList();
            TLRPC$TL_messageEntityItalic tLRPC$TL_messageEntityItalic = new TLRPC$TL_messageEntityItalic();
            tLRPC$TL_messageEntityItalic.offset = 0;
            tLRPC$TL_messageEntityItalic.length = charSequence.length();
            arrayList.add(tLRPC$TL_messageEntityItalic);
            return addEntitiesToText(charSequence, arrayList, isOutOwner(), true, z, z2);
        }
        return addEntitiesToText(charSequence, this.messageOwner.entities, isOutOwner(), true, z, z2);
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x0171  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0220  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x03df  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x03f3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0225 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean addEntitiesToText(CharSequence charSequence, ArrayList<TLRPC$MessageEntity> arrayList, boolean z, boolean z2, boolean z3, boolean z4) {
        String str;
        String str2;
        int i;
        boolean z5;
        int i2;
        int size;
        int i3;
        if (!(charSequence instanceof Spannable)) {
            return false;
        }
        Spannable spannable = (Spannable) charSequence;
        URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(0, charSequence.length(), URLSpan.class);
        boolean z6 = uRLSpanArr != null && uRLSpanArr.length > 0;
        if (arrayList.isEmpty()) {
            return z6;
        }
        byte b = z3 ? (byte) 2 : z ? (byte) 1 : (byte) 0;
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList(arrayList);
        Collections.sort(arrayList3, MessageObject$$ExternalSyntheticLambda1.INSTANCE);
        int size2 = arrayList3.size();
        int i4 = 0;
        while (true) {
            str = null;
            if (i4 >= size2) {
                break;
            }
            TLRPC$MessageEntity tLRPC$MessageEntity = (TLRPC$MessageEntity) arrayList3.get(i4);
            if (tLRPC$MessageEntity.length > 0 && (i2 = tLRPC$MessageEntity.offset) >= 0 && i2 < charSequence.length()) {
                if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length > charSequence.length()) {
                    tLRPC$MessageEntity.length = charSequence.length() - tLRPC$MessageEntity.offset;
                }
                if ((!z4 || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBold) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityItalic) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityStrike) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUnderline) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBlockquote) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCode) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPre) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMentionName) || (tLRPC$MessageEntity instanceof TLRPC$TL_inputMessageEntityMentionName) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntitySpoiler)) && uRLSpanArr != null && uRLSpanArr.length > 0) {
                    for (int i5 = 0; i5 < uRLSpanArr.length; i5++) {
                        if (uRLSpanArr[i5] != null) {
                            int spanStart = spannable.getSpanStart(uRLSpanArr[i5]);
                            int spanEnd = spannable.getSpanEnd(uRLSpanArr[i5]);
                            int i6 = tLRPC$MessageEntity.offset;
                            if ((i6 <= spanStart && tLRPC$MessageEntity.length + i6 >= spanStart) || (i6 <= spanEnd && i6 + tLRPC$MessageEntity.length >= spanEnd)) {
                                spannable.removeSpan(uRLSpanArr[i5]);
                                uRLSpanArr[i5] = null;
                            }
                        }
                    }
                }
                TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
                int i7 = tLRPC$MessageEntity.offset;
                textStyleRun.start = i7;
                textStyleRun.end = i7 + tLRPC$MessageEntity.length;
                if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntitySpoiler) {
                    textStyleRun.flags = 256;
                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityStrike) {
                    textStyleRun.flags = 8;
                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUnderline) {
                    textStyleRun.flags = 16;
                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBlockquote) {
                    textStyleRun.flags = 32;
                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBold) {
                    textStyleRun.flags = 1;
                } else {
                    if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityItalic) {
                        textStyleRun.flags = 2;
                    } else if ((tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCode) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPre)) {
                        textStyleRun.flags = 4;
                    } else {
                        if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMentionName) {
                            if (z2) {
                                textStyleRun.flags = 64;
                                textStyleRun.urlEntity = tLRPC$MessageEntity;
                            }
                        } else if (tLRPC$MessageEntity instanceof TLRPC$TL_inputMessageEntityMentionName) {
                            if (z2) {
                                textStyleRun.flags = 64;
                                textStyleRun.urlEntity = tLRPC$MessageEntity;
                            }
                        } else if ((!z4 || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl)) && (((!(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUrl) && !(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl)) || !Browser.isPassportUrl(tLRPC$MessageEntity.url)) && (!(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMention) || z2))) {
                            textStyleRun.flags = 128;
                            textStyleRun.urlEntity = tLRPC$MessageEntity;
                        }
                        i4++;
                    }
                    size = arrayList2.size();
                    i3 = 0;
                    while (i3 < size) {
                        TextStyleSpan.TextStyleRun textStyleRun2 = (TextStyleSpan.TextStyleRun) arrayList2.get(i3);
                        if ((textStyleRun2.flags & 256) == 0 || textStyleRun.start < textStyleRun2.start || textStyleRun.end > textStyleRun2.end) {
                            int i8 = textStyleRun.start;
                            int i9 = textStyleRun2.start;
                            if (i8 > i9) {
                                int i10 = textStyleRun2.end;
                                if (i8 < i10) {
                                    if (textStyleRun.end < i10) {
                                        TextStyleSpan.TextStyleRun textStyleRun3 = new TextStyleSpan.TextStyleRun(textStyleRun);
                                        textStyleRun3.merge(textStyleRun2);
                                        int i11 = i3 + 1;
                                        arrayList2.add(i11, textStyleRun3);
                                        TextStyleSpan.TextStyleRun textStyleRun4 = new TextStyleSpan.TextStyleRun(textStyleRun2);
                                        textStyleRun4.start = textStyleRun.end;
                                        i3 = i11 + 1;
                                        size = size + 1 + 1;
                                        arrayList2.add(i3, textStyleRun4);
                                    } else {
                                        TextStyleSpan.TextStyleRun textStyleRun5 = new TextStyleSpan.TextStyleRun(textStyleRun);
                                        textStyleRun5.merge(textStyleRun2);
                                        textStyleRun5.end = textStyleRun2.end;
                                        i3++;
                                        size++;
                                        arrayList2.add(i3, textStyleRun5);
                                    }
                                    int i12 = textStyleRun.start;
                                    textStyleRun.start = textStyleRun2.end;
                                    textStyleRun2.end = i12;
                                }
                            } else {
                                int i13 = textStyleRun.end;
                                if (i9 < i13) {
                                    int i14 = textStyleRun2.end;
                                    if (i13 == i14) {
                                        textStyleRun2.merge(textStyleRun);
                                    } else if (i13 < i14) {
                                        TextStyleSpan.TextStyleRun textStyleRun6 = new TextStyleSpan.TextStyleRun(textStyleRun2);
                                        textStyleRun6.merge(textStyleRun);
                                        textStyleRun6.end = textStyleRun.end;
                                        i3++;
                                        size++;
                                        arrayList2.add(i3, textStyleRun6);
                                        textStyleRun2.start = textStyleRun.end;
                                    } else {
                                        TextStyleSpan.TextStyleRun textStyleRun7 = new TextStyleSpan.TextStyleRun(textStyleRun);
                                        textStyleRun7.start = textStyleRun2.end;
                                        i3++;
                                        size++;
                                        arrayList2.add(i3, textStyleRun7);
                                        textStyleRun2.merge(textStyleRun);
                                    }
                                    textStyleRun.end = i9;
                                }
                            }
                        }
                        i3++;
                        size = size;
                    }
                    if (textStyleRun.start >= textStyleRun.end) {
                        arrayList2.add(textStyleRun);
                    }
                    i4++;
                }
                size = arrayList2.size();
                i3 = 0;
                while (i3 < size) {
                }
                if (textStyleRun.start >= textStyleRun.end) {
                }
                i4++;
            }
            i4++;
        }
        int size3 = arrayList2.size();
        boolean z7 = z6;
        int i15 = 0;
        while (i15 < size3) {
            TextStyleSpan.TextStyleRun textStyleRun8 = (TextStyleSpan.TextStyleRun) arrayList2.get(i15);
            TLRPC$MessageEntity tLRPC$MessageEntity2 = textStyleRun8.urlEntity;
            if (tLRPC$MessageEntity2 != null) {
                int i16 = tLRPC$MessageEntity2.offset;
                str2 = TextUtils.substring(charSequence, i16, tLRPC$MessageEntity2.length + i16);
            } else {
                str2 = str;
            }
            TLRPC$MessageEntity tLRPC$MessageEntity3 = textStyleRun8.urlEntity;
            if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityBotCommand) {
                spannable.setSpan(new URLSpanBotCommand(str2, b, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
            } else {
                if ((tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityHashtag) || (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityMention) || (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityCashtag)) {
                    i = 33;
                    spannable.setSpan(new URLSpanNoUnderline(str2, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityEmail) {
                    spannable.setSpan(new URLSpanReplacement("mailto:" + str2, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                } else {
                    if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityUrl) {
                        if (!str2.toLowerCase().contains("://")) {
                            spannable.setSpan(new URLSpanBrowser("http://" + str2, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                        } else {
                            spannable.setSpan(new URLSpanBrowser(str2, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                        }
                    } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityBankCard) {
                        spannable.setSpan(new URLSpanNoUnderline("card:" + str2, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                    } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityPhone) {
                        String stripExceptNumbers = PhoneFormat.stripExceptNumbers(str2);
                        if (str2.startsWith("+")) {
                            stripExceptNumbers = "+" + stripExceptNumbers;
                        }
                        spannable.setSpan(new URLSpanBrowser("tel:" + stripExceptNumbers, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                    } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityTextUrl) {
                        spannable.setSpan(new URLSpanReplacement(textStyleRun8.urlEntity.url, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                    } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityMentionName) {
                        spannable.setSpan(new URLSpanUserMention("" + ((TLRPC$TL_messageEntityMentionName) textStyleRun8.urlEntity).user_id, b, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                    } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_inputMessageEntityMentionName) {
                        spannable.setSpan(new URLSpanUserMention("" + ((TLRPC$TL_inputMessageEntityMentionName) textStyleRun8.urlEntity).user_id.user_id, b, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                    } else if ((textStyleRun8.flags & 4) != 0) {
                        i = 33;
                        spannable.setSpan(new URLSpanMono(spannable, textStyleRun8.start, textStyleRun8.end, b, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                    } else {
                        i = 33;
                        spannable.setSpan(new TextStyleSpan(textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                        z5 = true;
                        if (!z5 && (textStyleRun8.flags & 256) != 0) {
                            spannable.setSpan(new TextStyleSpan(textStyleRun8), textStyleRun8.start, textStyleRun8.end, i);
                        }
                        i15++;
                        str = null;
                    }
                    z5 = false;
                    z7 = true;
                    i = 33;
                    if (!z5) {
                        spannable.setSpan(new TextStyleSpan(textStyleRun8), textStyleRun8.start, textStyleRun8.end, i);
                    }
                    i15++;
                    str = null;
                }
                z5 = false;
                if (!z5) {
                }
                i15++;
                str = null;
            }
            i = 33;
            z5 = false;
            if (!z5) {
            }
            i15++;
            str = null;
        }
        return z7;
    }

    public static /* synthetic */ int lambda$addEntitiesToText$0(TLRPC$MessageEntity tLRPC$MessageEntity, TLRPC$MessageEntity tLRPC$MessageEntity2) {
        int i = tLRPC$MessageEntity.offset;
        int i2 = tLRPC$MessageEntity2.offset;
        if (i > i2) {
            return 1;
        }
        return i < i2 ? -1 : 0;
    }

    public boolean needDrawShareButton() {
        int i;
        String str;
        if (!this.preview && !this.scheduled && this.eventId == 0) {
            TLRPC$Message tLRPC$Message = this.messageOwner;
            if (tLRPC$Message.noforwards) {
                return false;
            }
            if (tLRPC$Message.fwd_from != null && !isOutOwner() && this.messageOwner.fwd_from.saved_from_peer != null && getDialogId() == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                return true;
            }
            int i2 = this.type;
            if (i2 != 13 && i2 != 15) {
                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
                if (tLRPC$MessageFwdHeader != null && (tLRPC$MessageFwdHeader.from_id instanceof TLRPC$TL_peerChannel) && !isOutOwner()) {
                    return true;
                }
                if (isFromUser()) {
                    TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
                    if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaEmpty) || tLRPC$MessageMedia == null || ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) && !(tLRPC$MessageMedia.webpage instanceof TLRPC$TL_webPage))) {
                        return false;
                    }
                    TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
                    if (user != null && user.bot) {
                        return true;
                    }
                    if (!isOut()) {
                        TLRPC$Message tLRPC$Message2 = this.messageOwner;
                        TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$Message2.media;
                        if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGame) || (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaInvoice)) {
                            return true;
                        }
                        TLRPC$Peer tLRPC$Peer = tLRPC$Message2.peer_id;
                        TLRPC$Chat tLRPC$Chat = null;
                        if (tLRPC$Peer != null) {
                            long j = tLRPC$Peer.channel_id;
                            if (j != 0) {
                                tLRPC$Chat = getChat(null, null, j);
                            }
                        }
                        if (!ChatObject.isChannel(tLRPC$Chat) || !tLRPC$Chat.megagroup || (str = tLRPC$Chat.username) == null || str.length() <= 0) {
                            return false;
                        }
                        TLRPC$MessageMedia tLRPC$MessageMedia3 = this.messageOwner.media;
                        return !(tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaContact) && !(tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaGeo);
                    }
                } else {
                    TLRPC$Message tLRPC$Message3 = this.messageOwner;
                    if ((!(tLRPC$Message3.from_id instanceof TLRPC$TL_peerChannel) && !tLRPC$Message3.post) || isSupergroup()) {
                        return false;
                    }
                    TLRPC$Message tLRPC$Message4 = this.messageOwner;
                    if (tLRPC$Message4.peer_id.channel_id != 0 && ((tLRPC$Message4.via_bot_id == 0 && tLRPC$Message4.reply_to == null) || ((i = this.type) != 13 && i != 15))) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public boolean isYouTubeVideo() {
        TLRPC$WebPage tLRPC$WebPage;
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) && (tLRPC$WebPage = tLRPC$MessageMedia.webpage) != null && !TextUtils.isEmpty(tLRPC$WebPage.embed_url) && "YouTube".equals(this.messageOwner.media.webpage.site_name);
    }

    public int getMaxMessageTextWidth() {
        TLRPC$WebPage tLRPC$WebPage;
        if (AndroidUtilities.isTablet() && this.eventId != 0) {
            this.generatedWithMinSize = AndroidUtilities.dp(530.0f);
        } else {
            this.generatedWithMinSize = AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() : getParentWidth();
        }
        this.generatedWithDensity = AndroidUtilities.density;
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        int i = 0;
        if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) && (tLRPC$WebPage = tLRPC$MessageMedia.webpage) != null && "telegram_background".equals(tLRPC$WebPage.type)) {
            try {
                Uri parse = Uri.parse(this.messageOwner.media.webpage.url);
                String lastPathSegment = parse.getLastPathSegment();
                if (parse.getQueryParameter("bg_color") != null) {
                    i = AndroidUtilities.dp(220.0f);
                } else if (lastPathSegment.length() == 6 || (lastPathSegment.length() == 13 && lastPathSegment.charAt(6) == '-')) {
                    i = AndroidUtilities.dp(200.0f);
                }
            } catch (Exception unused) {
            }
        } else if (isAndroidTheme()) {
            i = AndroidUtilities.dp(200.0f);
        }
        if (i == 0) {
            int dp = this.generatedWithMinSize - AndroidUtilities.dp((!needDrawAvatarInternal() || isOutOwner() || this.messageOwner.isThreadMessage) ? 80.0f : 132.0f);
            if (needDrawShareButton() && !isOutOwner()) {
                dp -= AndroidUtilities.dp(10.0f);
            }
            int i2 = dp;
            return this.messageOwner.media instanceof TLRPC$TL_messageMediaGame ? i2 - AndroidUtilities.dp(10.0f) : i2;
        }
        return i;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(22:76|(1:78)(1:79)|80|(3:82|(1:(1:(2:86|(1:88))(1:89))(1:90))|91)(5:92|(1:94)(12:95|237|96|(5:105|106|226|107|108)(5:99|100|228|101|102)|216|109|110|(3:112|224|113)|114|115|(3:117|222|118)|121)|210|242|211)|122|(1:124)|125|220|126|127|(2:233|131)|218|138|141|(1:143)|144|(1:146)|147|(6:149|(14:235|151|152|239|155|158|(1:160)(1:161)|162|(1:164)(1:165)|(4:231|169|170|(4:172|175|245|176))|174|175|245|176)|244|177|(1:(1:180))(2:(1:182)|183)|184)(3:185|(5:187|(1:189)|190|(1:192)(1:193)|194)(1:195)|196)|197|243|211) */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x0308, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x0309, code lost:
        r8 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x031b, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x031c, code lost:
        org.telegram.messenger.FileLog.e(r0);
        r0 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0077, code lost:
        if ((r0.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaUnsupported) == false) goto L40;
     */
    /* JADX WARN: Removed duplicated region for block: B:143:0x032a  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x032f  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0340  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0407  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00fd  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0106 A[Catch: Exception -> 0x0487, TryCatch #7 {Exception -> 0x0487, blocks: (B:65:0x0100, B:67:0x0106, B:68:0x0124), top: B:230:0x0100 }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0124 A[Catch: Exception -> 0x0487, TRY_LEAVE, TryCatch #7 {Exception -> 0x0487, blocks: (B:65:0x0100, B:67:0x0106, B:68:0x0124), top: B:230:0x0100 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0145  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0147  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0159  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateLayout(TLRPC$User tLRPC$User) {
        TLRPC$Message tLRPC$Message;
        boolean z;
        TextPaint textPaint;
        int i;
        StaticLayout staticLayout;
        int lineCount;
        int ceil;
        int i2;
        int i3;
        StaticLayout staticLayout2;
        boolean z2;
        TextPaint textPaint2;
        int i4;
        int i5;
        int i6;
        StaticLayout staticLayout3;
        int i7;
        TextLayoutBlock textLayoutBlock;
        int ceil2;
        int i8;
        int i9;
        int i10;
        float f;
        float f2;
        StaticLayout staticLayout4;
        float f3;
        float f4;
        float f5;
        int i11;
        int i12;
        boolean z3;
        int i13;
        Exception e;
        int i14;
        int i15;
        MessageObject messageObject;
        if (this.type != 0 || this.messageOwner.peer_id == null || TextUtils.isEmpty(this.messageText)) {
            return;
        }
        generateLinkDescription();
        this.textLayoutBlocks = new ArrayList<>();
        int i16 = 0;
        this.textWidth = 0;
        int i17 = 1;
        try {
            if (!(this.messageOwner.send_state != 0 ? false : !tLRPC$Message.entities.isEmpty())) {
                if (this.eventId == 0) {
                    TLRPC$Message tLRPC$Message2 = this.messageOwner;
                    if (!(tLRPC$Message2 instanceof TLRPC$TL_message_old) && !(tLRPC$Message2 instanceof TLRPC$TL_message_old2) && !(tLRPC$Message2 instanceof TLRPC$TL_message_old3) && !(tLRPC$Message2 instanceof TLRPC$TL_message_old4) && !(tLRPC$Message2 instanceof TLRPC$TL_messageForwarded_old) && !(tLRPC$Message2 instanceof TLRPC$TL_messageForwarded_old2) && !(tLRPC$Message2 instanceof TLRPC$TL_message_secret) && !(tLRPC$Message2.media instanceof TLRPC$TL_messageMediaInvoice) && (!isOut() || this.messageOwner.send_state == 0)) {
                        TLRPC$Message tLRPC$Message3 = this.messageOwner;
                        if (tLRPC$Message3.id >= 0) {
                        }
                    }
                }
                z = true;
                if (z) {
                    addLinks(isOutOwner(), this.messageText, true, true);
                }
                if (!isYouTubeVideo() || ((messageObject = this.replyMessageObject) != null && messageObject.isYouTubeVideo())) {
                    addUrlsByPattern(isOutOwner(), this.messageText, false, 3, Integer.MAX_VALUE, false);
                } else {
                    MessageObject messageObject2 = this.replyMessageObject;
                    if (messageObject2 != null) {
                        if (messageObject2.isVideo()) {
                            addUrlsByPattern(isOutOwner(), this.messageText, false, 3, this.replyMessageObject.getDuration(), false);
                        } else if (this.replyMessageObject.isMusic() || this.replyMessageObject.isVoice()) {
                            addUrlsByPattern(isOutOwner(), this.messageText, false, 4, this.replyMessageObject.getDuration(), false);
                        }
                    }
                }
                boolean addEntitiesToText = addEntitiesToText(this.messageText, z);
                int maxMessageTextWidth = getMaxMessageTextWidth();
                if (!(this.messageOwner.media instanceof TLRPC$TL_messageMediaGame)) {
                    textPaint = Theme.chat_msgGameTextPaint;
                } else {
                    textPaint = Theme.chat_msgTextPaint;
                }
                TextPaint textPaint3 = textPaint;
                i = Build.VERSION.SDK_INT;
                int i18 = 24;
                if (i < 24) {
                    CharSequence charSequence = this.messageText;
                    staticLayout = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textPaint3, maxMessageTextWidth).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
                } else {
                    staticLayout = new StaticLayout(this.messageText, textPaint3, maxMessageTextWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                }
                this.textHeight = staticLayout.getHeight();
                this.linesCount = staticLayout.getLineCount();
                ceil = i < 24 ? 1 : (int) Math.ceil(lineCount / 10.0f);
                float f6 = 0.0f;
                int i19 = 0;
                float f7 = 0.0f;
                for (i2 = 0; i2 < ceil; i2 = i5 + 1) {
                    int i20 = Build.VERSION.SDK_INT;
                    if (i20 >= i18) {
                        i3 = this.linesCount;
                    } else {
                        i3 = Math.min(10, this.linesCount - i19);
                    }
                    int i21 = i3;
                    TextLayoutBlock textLayoutBlock2 = new TextLayoutBlock();
                    if (ceil == i17) {
                        textLayoutBlock2.textLayout = staticLayout;
                        textLayoutBlock2.textYOffset = f6;
                        textLayoutBlock2.charactersOffset = i16;
                        textLayoutBlock2.charactersEnd = staticLayout.getText().length();
                        int i22 = this.emojiOnlyCount;
                        if (i22 != 0) {
                            if (i22 == i17) {
                                this.textHeight -= AndroidUtilities.dp(5.3f);
                                textLayoutBlock2.textYOffset -= AndroidUtilities.dp(5.3f);
                            } else if (i22 == 2) {
                                this.textHeight -= AndroidUtilities.dp(4.5f);
                                textLayoutBlock2.textYOffset -= AndroidUtilities.dp(4.5f);
                            } else if (i22 == 3) {
                                this.textHeight -= AndroidUtilities.dp(4.2f);
                                textLayoutBlock2.textYOffset -= AndroidUtilities.dp(4.2f);
                            }
                        }
                        textLayoutBlock2.height = this.textHeight;
                        textLayoutBlock = textLayoutBlock2;
                        i7 = i21;
                        i6 = i19;
                        i5 = i2;
                        i4 = ceil;
                        staticLayout3 = staticLayout;
                        textPaint2 = textPaint3;
                    } else {
                        int lineStart = staticLayout.getLineStart(i19);
                        int lineEnd = staticLayout.getLineEnd((i19 + i21) - 1);
                        if (lineEnd < lineStart) {
                            z2 = addEntitiesToText;
                            i13 = i19;
                            i5 = i2;
                            i4 = ceil;
                            staticLayout2 = staticLayout;
                            textPaint2 = textPaint3;
                        } else {
                            textLayoutBlock2.charactersOffset = lineStart;
                            textLayoutBlock2.charactersEnd = lineEnd;
                            try {
                                SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(this.messageText.subSequence(lineStart, lineEnd));
                                if (addEntitiesToText && i20 >= i18) {
                                    StaticLayout.Builder obtain = StaticLayout.Builder.obtain(valueOf, i16, valueOf.length(), textPaint3, AndroidUtilities.dp(2.0f) + maxMessageTextWidth);
                                    i17 = 1;
                                    try {
                                        textLayoutBlock2.textLayout = obtain.setBreakStrategy(1).setHyphenationFrequency(i16).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
                                        textLayoutBlock = textLayoutBlock2;
                                        i7 = i21;
                                        i6 = i19;
                                        i15 = i2;
                                        i14 = ceil;
                                        staticLayout3 = staticLayout;
                                        textPaint2 = textPaint3;
                                    } catch (Exception e2) {
                                        e = e2;
                                        z2 = addEntitiesToText;
                                        i13 = i19;
                                        i5 = i2;
                                        i4 = ceil;
                                        staticLayout2 = staticLayout;
                                        textPaint2 = textPaint3;
                                        FileLog.e(e);
                                        i19 = i13;
                                        ceil = i4;
                                        textPaint3 = textPaint2;
                                        addEntitiesToText = z2;
                                        staticLayout = staticLayout2;
                                        i16 = 0;
                                        f6 = 0.0f;
                                        i18 = 24;
                                    }
                                } else {
                                    int length = valueOf.length();
                                    textLayoutBlock = textLayoutBlock2;
                                    i7 = i21;
                                    i15 = i2;
                                    i13 = i19;
                                    i14 = ceil;
                                    staticLayout2 = staticLayout;
                                    textPaint2 = textPaint3;
                                    try {
                                        textLayoutBlock.textLayout = new StaticLayout(valueOf, 0, length, textPaint3, maxMessageTextWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                                        i6 = i13;
                                        staticLayout3 = staticLayout2;
                                    } catch (Exception e3) {
                                        e = e3;
                                        z2 = addEntitiesToText;
                                        i5 = i15;
                                        i4 = i14;
                                        i17 = 1;
                                        FileLog.e(e);
                                        i19 = i13;
                                        ceil = i4;
                                        textPaint3 = textPaint2;
                                        addEntitiesToText = z2;
                                        staticLayout = staticLayout2;
                                        i16 = 0;
                                        f6 = 0.0f;
                                        i18 = 24;
                                    }
                                }
                                try {
                                    float lineTop = staticLayout3.getLineTop(i6);
                                    textLayoutBlock.textYOffset = lineTop;
                                    i5 = i15;
                                    if (i5 != 0) {
                                        try {
                                            textLayoutBlock.height = (int) (lineTop - f7);
                                        } catch (Exception e4) {
                                            e = e4;
                                            z2 = addEntitiesToText;
                                            staticLayout2 = staticLayout3;
                                            i13 = i6;
                                            i4 = i14;
                                            i17 = 1;
                                            FileLog.e(e);
                                            i19 = i13;
                                            ceil = i4;
                                            textPaint3 = textPaint2;
                                            addEntitiesToText = z2;
                                            staticLayout = staticLayout2;
                                            i16 = 0;
                                            f6 = 0.0f;
                                            i18 = 24;
                                        }
                                    }
                                    int i23 = textLayoutBlock.height;
                                    StaticLayout staticLayout5 = textLayoutBlock.textLayout;
                                    textLayoutBlock.height = Math.max(i23, staticLayout5.getLineBottom(staticLayout5.getLineCount() - 1));
                                    float f8 = textLayoutBlock.textYOffset;
                                    i4 = i14;
                                    if (i5 == i4 - 1) {
                                        i7 = Math.max(i7, textLayoutBlock.textLayout.getLineCount());
                                        try {
                                            this.textHeight = Math.max(this.textHeight, (int) (textLayoutBlock.textYOffset + textLayoutBlock.textLayout.getHeight()));
                                        } catch (Exception e5) {
                                            FileLog.e(e5);
                                        }
                                    }
                                    f7 = f8;
                                } catch (Exception e6) {
                                    e = e6;
                                    z2 = addEntitiesToText;
                                    staticLayout2 = staticLayout3;
                                    i13 = i6;
                                    i5 = i15;
                                    i4 = i14;
                                    i17 = 1;
                                    FileLog.e(e);
                                    i19 = i13;
                                    ceil = i4;
                                    textPaint3 = textPaint2;
                                    addEntitiesToText = z2;
                                    staticLayout = staticLayout2;
                                    i16 = 0;
                                    f6 = 0.0f;
                                    i18 = 24;
                                }
                            } catch (Exception e7) {
                                e = e7;
                                z2 = addEntitiesToText;
                                i13 = i19;
                                i5 = i2;
                                i4 = ceil;
                                staticLayout2 = staticLayout;
                                textPaint2 = textPaint3;
                                i17 = 1;
                            }
                        }
                        i19 = i13;
                        ceil = i4;
                        textPaint3 = textPaint2;
                        addEntitiesToText = z2;
                        staticLayout = staticLayout2;
                        i16 = 0;
                        f6 = 0.0f;
                        i18 = 24;
                    }
                    textLayoutBlock.spoilers.clear();
                    if (!this.isSpoilersRevealed) {
                        SpoilerEffect.addSpoilers(null, textLayoutBlock.textLayout, null, textLayoutBlock.spoilers);
                    }
                    this.textLayoutBlocks.add(textLayoutBlock);
                    float f9 = textLayoutBlock.textLayout.getLineLeft(i7 - 1);
                    float f10 = 0.0f;
                    if (i5 == 0 && f9 >= 0.0f) {
                        try {
                            this.textXOffset = f9;
                        } catch (Exception e8) {
                            Exception e9 = e8;
                            if (i5 == 0) {
                                this.textXOffset = f10;
                            }
                            FileLog.e(e9);
                            f9 = 0.0f;
                            float f11 = textLayoutBlock.textLayout.getLineWidth(i7 - 1);
                            ceil2 = (int) Math.ceil(f11);
                            if (ceil2 > maxMessageTextWidth + 80) {
                            }
                            i8 = i4 - 1;
                            if (i5 == i8) {
                            }
                            float f12 = ceil2;
                            int ceil3 = (int) Math.ceil(Math.max(f10, f9) + f12);
                            if (i7 <= 1) {
                            }
                            i19 = i9 + i10;
                            ceil = i4;
                            textPaint3 = textPaint2;
                            addEntitiesToText = z2;
                            staticLayout = staticLayout2;
                            i16 = 0;
                            f6 = 0.0f;
                            i18 = 24;
                        }
                    }
                    float f112 = textLayoutBlock.textLayout.getLineWidth(i7 - 1);
                    ceil2 = (int) Math.ceil(f112);
                    if (ceil2 > maxMessageTextWidth + 80) {
                        ceil2 = maxMessageTextWidth;
                    }
                    i8 = i4 - 1;
                    if (i5 == i8) {
                        this.lastLineWidth = ceil2;
                    }
                    float f122 = ceil2;
                    int ceil32 = (int) Math.ceil(Math.max(f10, f9) + f122);
                    if (i7 <= 1) {
                        int i24 = ceil2;
                        z2 = addEntitiesToText;
                        int i25 = ceil32;
                        int i26 = 0;
                        float f13 = 0.0f;
                        float f14 = 0.0f;
                        boolean z4 = false;
                        while (i26 < i7) {
                            try {
                                f = textLayoutBlock.textLayout.getLineWidth(i26);
                            } catch (Exception e10) {
                                FileLog.e(e10);
                                f = 0.0f;
                            }
                            try {
                                f2 = textLayoutBlock.textLayout.getLineLeft(i26);
                            } catch (Exception e11) {
                                FileLog.e(e11);
                                f2 = 0.0f;
                            }
                            float f15 = f2;
                            if (f > maxMessageTextWidth + 20) {
                                staticLayout4 = staticLayout3;
                                f3 = 0.0f;
                                f4 = maxMessageTextWidth;
                                f5 = 0.0f;
                            } else {
                                staticLayout4 = staticLayout3;
                                f4 = f;
                                f5 = f15;
                                f3 = 0.0f;
                            }
                            if (f5 > f3) {
                                i12 = i7;
                                this.textXOffset = Math.min(this.textXOffset, f5);
                                i11 = i6;
                                textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                this.hasRtl = true;
                            } else {
                                i12 = i7;
                                i11 = i6;
                                textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                            }
                            if (!z4 && f5 == 0.0f) {
                                try {
                                } catch (Exception unused) {
                                    z4 = true;
                                }
                                if (textLayoutBlock.textLayout.getParagraphDirection(i26) == 1) {
                                    z3 = true;
                                    z4 = z3;
                                    f14 = Math.max(f14, f4);
                                    float f16 = f5 + f4;
                                    f13 = Math.max(f13, f16);
                                    i24 = Math.max(i24, (int) Math.ceil(f4));
                                    i25 = Math.max(i25, (int) Math.ceil(f16));
                                    i26++;
                                    i7 = i12;
                                    i6 = i11;
                                    staticLayout3 = staticLayout4;
                                }
                            }
                            z3 = z4;
                            z4 = z3;
                            f14 = Math.max(f14, f4);
                            float f162 = f5 + f4;
                            f13 = Math.max(f13, f162);
                            i24 = Math.max(i24, (int) Math.ceil(f4));
                            i25 = Math.max(i25, (int) Math.ceil(f162));
                            i26++;
                            i7 = i12;
                            i6 = i11;
                            staticLayout3 = staticLayout4;
                        }
                        i10 = i7;
                        staticLayout2 = staticLayout3;
                        i9 = i6;
                        if (!z4) {
                            if (i5 == i8) {
                                this.lastLineWidth = i24;
                            }
                            f13 = f14;
                        } else if (i5 == i8) {
                            this.lastLineWidth = ceil32;
                        }
                        this.textWidth = Math.max(this.textWidth, (int) Math.ceil(f13));
                        i17 = 1;
                    } else {
                        z2 = addEntitiesToText;
                        i10 = i7;
                        staticLayout2 = staticLayout3;
                        i9 = i6;
                        if (f9 > 0.0f) {
                            float min = Math.min(this.textXOffset, f9);
                            this.textXOffset = min;
                            if (min == 0.0f) {
                                ceil2 = (int) (f122 + f9);
                            }
                            i17 = 1;
                            this.hasRtl = i4 != 1;
                            textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                        } else {
                            i17 = 1;
                            textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                        }
                        this.textWidth = Math.max(this.textWidth, Math.min(maxMessageTextWidth, ceil2));
                    }
                    i19 = i9 + i10;
                    ceil = i4;
                    textPaint3 = textPaint2;
                    addEntitiesToText = z2;
                    staticLayout = staticLayout2;
                    i16 = 0;
                    f6 = 0.0f;
                    i18 = 24;
                }
                return;
            }
            i = Build.VERSION.SDK_INT;
            int i182 = 24;
            if (i < 24) {
            }
            this.textHeight = staticLayout.getHeight();
            this.linesCount = staticLayout.getLineCount();
            if (i < 24) {
            }
            float f62 = 0.0f;
            int i192 = 0;
            float f72 = 0.0f;
            while (i2 < ceil) {
            }
            return;
        } catch (Exception e12) {
            FileLog.e(e12);
            return;
        }
        z = false;
        if (z) {
        }
        if (!isYouTubeVideo()) {
        }
        addUrlsByPattern(isOutOwner(), this.messageText, false, 3, Integer.MAX_VALUE, false);
        boolean addEntitiesToText2 = addEntitiesToText(this.messageText, z);
        int maxMessageTextWidth2 = getMaxMessageTextWidth();
        if (!(this.messageOwner.media instanceof TLRPC$TL_messageMediaGame)) {
        }
        TextPaint textPaint32 = textPaint;
    }

    public boolean isOut() {
        return this.messageOwner.out;
    }

    public boolean isOutOwner() {
        TLRPC$Peer tLRPC$Peer;
        if (this.preview) {
            return true;
        }
        TLRPC$Peer tLRPC$Peer2 = this.messageOwner.peer_id;
        TLRPC$Chat tLRPC$Chat = null;
        if (tLRPC$Peer2 != null) {
            long j = tLRPC$Peer2.channel_id;
            if (j != 0) {
                tLRPC$Chat = getChat(null, null, j);
            }
        }
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message.out) {
            TLRPC$Peer tLRPC$Peer3 = tLRPC$Message.from_id;
            if ((tLRPC$Peer3 instanceof TLRPC$TL_peerUser) || ((tLRPC$Peer3 instanceof TLRPC$TL_peerChannel) && (!ChatObject.isChannel(tLRPC$Chat) || tLRPC$Chat.megagroup))) {
                TLRPC$Message tLRPC$Message2 = this.messageOwner;
                if (!tLRPC$Message2.post) {
                    if (tLRPC$Message2.fwd_from == null) {
                        return true;
                    }
                    long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                    if (getDialogId() == clientUserId) {
                        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
                        TLRPC$Peer tLRPC$Peer4 = tLRPC$MessageFwdHeader.from_id;
                        if ((tLRPC$Peer4 instanceof TLRPC$TL_peerUser) && tLRPC$Peer4.user_id == clientUserId && ((tLRPC$Peer = tLRPC$MessageFwdHeader.saved_from_peer) == null || tLRPC$Peer.user_id == clientUserId)) {
                            return true;
                        }
                        TLRPC$Peer tLRPC$Peer5 = tLRPC$MessageFwdHeader.saved_from_peer;
                        return tLRPC$Peer5 != null && tLRPC$Peer5.user_id == clientUserId && (tLRPC$Peer4 == null || tLRPC$Peer4.user_id == clientUserId);
                    }
                    TLRPC$Peer tLRPC$Peer6 = this.messageOwner.fwd_from.saved_from_peer;
                    return tLRPC$Peer6 == null || tLRPC$Peer6.user_id == clientUserId;
                }
            }
        }
        return false;
    }

    public boolean needDrawAvatar() {
        if (this.customAvatarDrawable != null) {
            return true;
        }
        if (isSponsored() && isFromChat()) {
            return true;
        }
        if (!isSponsored()) {
            if (isFromUser() || isFromGroup() || this.eventId != 0) {
                return true;
            }
            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
            if (tLRPC$MessageFwdHeader != null && tLRPC$MessageFwdHeader.saved_from_peer != null) {
                return true;
            }
        }
        return false;
    }

    public boolean needDrawAvatarInternal() {
        if (this.customAvatarDrawable != null) {
            return true;
        }
        if (!isSponsored()) {
            if ((isFromChat() && isFromUser()) || isFromGroup() || this.eventId != 0) {
                return true;
            }
            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
            if (tLRPC$MessageFwdHeader != null && tLRPC$MessageFwdHeader.saved_from_peer != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isFromChat() {
        TLRPC$Peer tLRPC$Peer;
        if (getDialogId() == UserConfig.getInstance(this.currentAccount).clientUserId) {
            return true;
        }
        TLRPC$Peer tLRPC$Peer2 = this.messageOwner.peer_id;
        TLRPC$Chat tLRPC$Chat = null;
        if (tLRPC$Peer2 != null) {
            long j = tLRPC$Peer2.channel_id;
            if (j != 0) {
                tLRPC$Chat = getChat(null, null, j);
            }
        }
        if ((ChatObject.isChannel(tLRPC$Chat) && tLRPC$Chat.megagroup) || ((tLRPC$Peer = this.messageOwner.peer_id) != null && tLRPC$Peer.chat_id != 0)) {
            return true;
        }
        return (tLRPC$Peer == null || tLRPC$Peer.channel_id == 0 || tLRPC$Chat == null || !tLRPC$Chat.megagroup) ? false : true;
    }

    public static long getFromChatId(TLRPC$Message tLRPC$Message) {
        return getPeerId(tLRPC$Message.from_id);
    }

    public static long getPeerId(TLRPC$Peer tLRPC$Peer) {
        long j;
        if (tLRPC$Peer == null) {
            return 0L;
        }
        if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
            j = tLRPC$Peer.chat_id;
        } else if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
            j = tLRPC$Peer.channel_id;
        } else {
            return tLRPC$Peer.user_id;
        }
        return -j;
    }

    public long getFromChatId() {
        return getFromChatId(this.messageOwner);
    }

    public long getChatId() {
        TLRPC$Peer tLRPC$Peer = this.messageOwner.peer_id;
        if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
            return tLRPC$Peer.chat_id;
        }
        if (!(tLRPC$Peer instanceof TLRPC$TL_peerChannel)) {
            return 0L;
        }
        return tLRPC$Peer.channel_id;
    }

    public boolean isFromUser() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return (tLRPC$Message.from_id instanceof TLRPC$TL_peerUser) && !tLRPC$Message.post;
    }

    public boolean isFromGroup() {
        TLRPC$Peer tLRPC$Peer = this.messageOwner.peer_id;
        TLRPC$Chat tLRPC$Chat = null;
        if (tLRPC$Peer != null) {
            long j = tLRPC$Peer.channel_id;
            if (j != 0) {
                tLRPC$Chat = getChat(null, null, j);
            }
        }
        return (this.messageOwner.from_id instanceof TLRPC$TL_peerChannel) && ChatObject.isChannel(tLRPC$Chat) && tLRPC$Chat.megagroup;
    }

    public boolean isForwardedChannelPost() {
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        TLRPC$Peer tLRPC$Peer = tLRPC$Message.from_id;
        if ((tLRPC$Peer instanceof TLRPC$TL_peerChannel) && (tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) != null && tLRPC$MessageFwdHeader.channel_post != 0) {
            TLRPC$Peer tLRPC$Peer2 = tLRPC$MessageFwdHeader.saved_from_peer;
            if ((tLRPC$Peer2 instanceof TLRPC$TL_peerChannel) && tLRPC$Peer.channel_id == tLRPC$Peer2.channel_id) {
                return true;
            }
        }
        return false;
    }

    public boolean isUnread() {
        return this.messageOwner.unread;
    }

    public boolean isContentUnread() {
        return this.messageOwner.media_unread;
    }

    public void setIsRead() {
        this.messageOwner.unread = false;
    }

    public int getUnradFlags() {
        return getUnreadFlags(this.messageOwner);
    }

    public static int getUnreadFlags(TLRPC$Message tLRPC$Message) {
        int i = !tLRPC$Message.unread ? 1 : 0;
        return !tLRPC$Message.media_unread ? i | 2 : i;
    }

    public void setContentIsRead() {
        this.messageOwner.media_unread = false;
    }

    public int getId() {
        return this.messageOwner.id;
    }

    public int getRealId() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        int i = tLRPC$Message.realId;
        return i != 0 ? i : tLRPC$Message.id;
    }

    public static long getMessageSize(TLRPC$Message tLRPC$Message) {
        TLRPC$Document tLRPC$Document;
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            tLRPC$Document = tLRPC$MessageMedia.webpage.document;
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
            tLRPC$Document = tLRPC$MessageMedia.game.document;
        } else {
            tLRPC$Document = tLRPC$MessageMedia != null ? tLRPC$MessageMedia.document : null;
        }
        if (tLRPC$Document != null) {
            return tLRPC$Document.size;
        }
        return 0L;
    }

    public long getSize() {
        return getMessageSize(this.messageOwner);
    }

    public static void fixMessagePeer(ArrayList<TLRPC$Message> arrayList, long j) {
        if (arrayList == null || arrayList.isEmpty() || j == 0) {
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            TLRPC$Message tLRPC$Message = arrayList.get(i);
            if (tLRPC$Message instanceof TLRPC$TL_messageEmpty) {
                TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
                tLRPC$Message.peer_id = tLRPC$TL_peerChannel;
                tLRPC$TL_peerChannel.channel_id = j;
            }
        }
    }

    public long getChannelId() {
        return getChannelId(this.messageOwner);
    }

    public static long getChannelId(TLRPC$Message tLRPC$Message) {
        TLRPC$Peer tLRPC$Peer = tLRPC$Message.peer_id;
        if (tLRPC$Peer != null) {
            return tLRPC$Peer.channel_id;
        }
        return 0L;
    }

    public static boolean shouldEncryptPhotoOrVideo(TLRPC$Message tLRPC$Message) {
        int i;
        if (tLRPC$Message instanceof TLRPC$TL_message_secret) {
            return ((tLRPC$Message.media instanceof TLRPC$TL_messageMediaPhoto) || isVideoMessage(tLRPC$Message)) && (i = tLRPC$Message.ttl) > 0 && i <= 60;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        return ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument)) && tLRPC$MessageMedia.ttl_seconds != 0;
    }

    public boolean shouldEncryptPhotoOrVideo() {
        return shouldEncryptPhotoOrVideo(this.messageOwner);
    }

    public static boolean isSecretPhotoOrVideo(TLRPC$Message tLRPC$Message) {
        int i;
        if (tLRPC$Message instanceof TLRPC$TL_message_secret) {
            return ((tLRPC$Message.media instanceof TLRPC$TL_messageMediaPhoto) || isRoundVideoMessage(tLRPC$Message) || isVideoMessage(tLRPC$Message)) && (i = tLRPC$Message.ttl) > 0 && i <= 60;
        } else if (!(tLRPC$Message instanceof TLRPC$TL_message)) {
            return false;
        } else {
            TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
            return ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument)) && tLRPC$MessageMedia.ttl_seconds != 0;
        }
    }

    public static boolean isSecretMedia(TLRPC$Message tLRPC$Message) {
        if (tLRPC$Message instanceof TLRPC$TL_message_secret) {
            return ((tLRPC$Message.media instanceof TLRPC$TL_messageMediaPhoto) || isRoundVideoMessage(tLRPC$Message) || isVideoMessage(tLRPC$Message)) && tLRPC$Message.media.ttl_seconds != 0;
        } else if (!(tLRPC$Message instanceof TLRPC$TL_message)) {
            return false;
        } else {
            TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
            return ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument)) && tLRPC$MessageMedia.ttl_seconds != 0;
        }
    }

    public boolean needDrawBluredPreview() {
        TLRPC$MessageMedia tLRPC$MessageMedia;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (!(tLRPC$Message instanceof TLRPC$TL_message_secret)) {
            return (tLRPC$Message instanceof TLRPC$TL_message) && (tLRPC$MessageMedia = tLRPC$Message.media) != null && tLRPC$MessageMedia.ttl_seconds != 0 && ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument));
        }
        int max = Math.max(tLRPC$Message.ttl, tLRPC$Message.media.ttl_seconds);
        return max > 0 && ((((this.messageOwner.media instanceof TLRPC$TL_messageMediaPhoto) || isVideo() || isGif()) && max <= 60) || isRoundVideo());
    }

    public boolean isSecretMedia() {
        TLRPC$MessageMedia tLRPC$MessageMedia;
        int i;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message instanceof TLRPC$TL_message_secret ? (((tLRPC$Message.media instanceof TLRPC$TL_messageMediaPhoto) || isGif()) && (i = this.messageOwner.ttl) > 0 && i <= 60) || isVoice() || isRoundVideo() || isVideo() : (tLRPC$Message instanceof TLRPC$TL_message) && (tLRPC$MessageMedia = tLRPC$Message.media) != null && tLRPC$MessageMedia.ttl_seconds != 0 && ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument));
    }

    public static void setUnreadFlags(TLRPC$Message tLRPC$Message, int i) {
        boolean z = false;
        tLRPC$Message.unread = (i & 1) == 0;
        if ((i & 2) == 0) {
            z = true;
        }
        tLRPC$Message.media_unread = z;
    }

    public static boolean isUnread(TLRPC$Message tLRPC$Message) {
        return tLRPC$Message.unread;
    }

    public static boolean isContentUnread(TLRPC$Message tLRPC$Message) {
        return tLRPC$Message.media_unread;
    }

    public boolean isSavedFromMegagroup() {
        TLRPC$Peer tLRPC$Peer;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
        if (tLRPC$MessageFwdHeader == null || (tLRPC$Peer = tLRPC$MessageFwdHeader.saved_from_peer) == null || tLRPC$Peer.channel_id == 0) {
            return false;
        }
        return ChatObject.isMegagroup(MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.saved_from_peer.channel_id)));
    }

    public static boolean isOut(TLRPC$Message tLRPC$Message) {
        return tLRPC$Message.out;
    }

    public long getDialogId() {
        return getDialogId(this.messageOwner);
    }

    public boolean canStreamVideo() {
        TLRPC$Document document = getDocument();
        if (document != null && !(document instanceof TLRPC$TL_documentEncrypted)) {
            if (SharedConfig.streamAllVideo) {
                return true;
            }
            for (int i = 0; i < document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                    return tLRPC$DocumentAttribute.supports_streaming;
                }
            }
            if (SharedConfig.streamMkv && "video/x-matroska".equals(document.mime_type)) {
                return true;
            }
        }
        return false;
    }

    public static long getDialogId(TLRPC$Message tLRPC$Message) {
        TLRPC$Peer tLRPC$Peer;
        if (tLRPC$Message.dialog_id == 0 && (tLRPC$Peer = tLRPC$Message.peer_id) != null) {
            long j = tLRPC$Peer.chat_id;
            if (j != 0) {
                tLRPC$Message.dialog_id = -j;
            } else {
                long j2 = tLRPC$Peer.channel_id;
                if (j2 != 0) {
                    tLRPC$Message.dialog_id = -j2;
                } else if (tLRPC$Message.from_id == null || isOut(tLRPC$Message)) {
                    tLRPC$Message.dialog_id = tLRPC$Message.peer_id.user_id;
                } else {
                    tLRPC$Message.dialog_id = tLRPC$Message.from_id.user_id;
                }
            }
        }
        return tLRPC$Message.dialog_id;
    }

    public boolean isSending() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message.send_state == 1 && tLRPC$Message.id < 0;
    }

    public boolean isEditing() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message.send_state == 3 && tLRPC$Message.id > 0;
    }

    public boolean isEditingMedia() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto ? tLRPC$MessageMedia.photo.id == 0 : (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) && tLRPC$MessageMedia.document.dc_id == 0;
    }

    public boolean isSendError() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return (tLRPC$Message.send_state == 2 && tLRPC$Message.id < 0) || (this.scheduled && tLRPC$Message.id > 0 && tLRPC$Message.date < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + (-60));
    }

    public boolean isSent() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message.send_state == 0 || tLRPC$Message.id > 0;
    }

    public int getSecretTimeLeft() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        int i = tLRPC$Message.ttl;
        int i2 = tLRPC$Message.destroyTime;
        return i2 != 0 ? Math.max(0, i2 - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) : i;
    }

    public String getSecretTimeString() {
        if (!isSecretMedia()) {
            return null;
        }
        int secretTimeLeft = getSecretTimeLeft();
        if (secretTimeLeft < 60) {
            return secretTimeLeft + "s";
        }
        return (secretTimeLeft / 60) + "m";
    }

    public String getDocumentName() {
        return FileLoader.getDocumentFileName(getDocument());
    }

    public static boolean isWebM(TLRPC$Document tLRPC$Document) {
        return tLRPC$Document != null && "video/webm".equals(tLRPC$Document.mime_type);
    }

    public static boolean isVideoSticker(TLRPC$Document tLRPC$Document) {
        return tLRPC$Document != null && isVideoStickerDocument(tLRPC$Document);
    }

    public boolean isVideoSticker() {
        return getDocument() != null && isVideoStickerDocument(getDocument());
    }

    public static boolean isStickerDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                if (tLRPC$Document.attributes.get(i) instanceof TLRPC$TL_documentAttributeSticker) {
                    return "image/webp".equals(tLRPC$Document.mime_type) || "video/webm".equals(tLRPC$Document.mime_type);
                }
            }
        }
        return false;
    }

    public static boolean isVideoStickerDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                if (tLRPC$Document.attributes.get(i) instanceof TLRPC$TL_documentAttributeSticker) {
                    return "video/webm".equals(tLRPC$Document.mime_type);
                }
            }
        }
        return false;
    }

    public static boolean isStickerHasSet(TLRPC$Document tLRPC$Document) {
        TLRPC$InputStickerSet tLRPC$InputStickerSet;
        if (tLRPC$Document != null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) && (tLRPC$InputStickerSet = tLRPC$DocumentAttribute.stickerset) != null && !(tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAnimatedStickerDocument(TLRPC$Document tLRPC$Document, boolean z) {
        if (tLRPC$Document != null && (("application/x-tgsticker".equals(tLRPC$Document.mime_type) && !tLRPC$Document.thumbs.isEmpty()) || "application/x-tgsdice".equals(tLRPC$Document.mime_type))) {
            if (z) {
                return true;
            }
            int size = tLRPC$Document.attributes.size();
            for (int i = 0; i < size; i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                    return tLRPC$DocumentAttribute.stickerset instanceof TLRPC$TL_inputStickerSetShortName;
                }
            }
        }
        return false;
    }

    public static boolean canAutoplayAnimatedSticker(TLRPC$Document tLRPC$Document) {
        return (isAnimatedStickerDocument(tLRPC$Document, true) || isVideoStickerDocument(tLRPC$Document)) && SharedConfig.getDevicePerformanceClass() != 0;
    }

    public static boolean isMaskDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) && tLRPC$DocumentAttribute.mask) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isVoiceDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                    return tLRPC$DocumentAttribute.voice;
                }
            }
        }
        return false;
    }

    public static boolean isVoiceWebDocument(WebFile webFile) {
        return webFile != null && webFile.mime_type.equals("audio/ogg");
    }

    public static boolean isImageWebDocument(WebFile webFile) {
        return webFile != null && !isGifDocument(webFile) && webFile.mime_type.startsWith("image/");
    }

    public static boolean isVideoWebDocument(WebFile webFile) {
        return webFile != null && webFile.mime_type.startsWith("video/");
    }

    public static boolean isMusicDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                    return !tLRPC$DocumentAttribute.voice;
                }
            }
            if (!TextUtils.isEmpty(tLRPC$Document.mime_type)) {
                String lowerCase = tLRPC$Document.mime_type.toLowerCase();
                if (lowerCase.equals("audio/flac") || lowerCase.equals("audio/ogg") || lowerCase.equals("audio/opus") || lowerCase.equals("audio/x-opus+ogg") || (lowerCase.equals("application/octet-stream") && FileLoader.getDocumentFileName(tLRPC$Document).endsWith(".opus"))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static TLRPC$VideoSize getDocumentVideoThumb(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null || tLRPC$Document.video_thumbs.isEmpty()) {
            return null;
        }
        return tLRPC$Document.video_thumbs.get(0);
    }

    public static boolean isVideoDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null) {
            return false;
        }
        boolean z = false;
        int i = 0;
        int i2 = 0;
        boolean z2 = false;
        for (int i3 = 0; i3 < tLRPC$Document.attributes.size(); i3++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i3);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                if (tLRPC$DocumentAttribute.round_message) {
                    return false;
                }
                i = tLRPC$DocumentAttribute.w;
                i2 = tLRPC$DocumentAttribute.h;
                z2 = true;
            } else if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAnimated) {
                z = true;
            }
        }
        if (z && (i > 1280 || i2 > 1280)) {
            z = false;
        }
        if (SharedConfig.streamMkv && !z2 && "video/x-matroska".equals(tLRPC$Document.mime_type)) {
            z2 = true;
        }
        return z2 && !z;
    }

    public TLRPC$Document getDocument() {
        TLRPC$Document tLRPC$Document = this.emojiAnimatedSticker;
        return tLRPC$Document != null ? tLRPC$Document : getDocument(this.messageOwner);
    }

    public static TLRPC$Document getDocument(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            return tLRPC$MessageMedia.webpage.document;
        }
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
            return tLRPC$MessageMedia.game.document;
        }
        if (tLRPC$MessageMedia == null) {
            return null;
        }
        return tLRPC$MessageMedia.document;
    }

    public static TLRPC$Photo getPhoto(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            return tLRPC$MessageMedia.webpage.photo;
        }
        if (tLRPC$MessageMedia == null) {
            return null;
        }
        return tLRPC$MessageMedia.photo;
    }

    public static boolean isStickerMessage(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        return tLRPC$MessageMedia != null && isStickerDocument(tLRPC$MessageMedia.document);
    }

    public static boolean isAnimatedStickerMessage(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia;
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(tLRPC$Message.dialog_id);
        if ((!isEncryptedDialog || tLRPC$Message.stickerVerified == 1) && (tLRPC$MessageMedia = tLRPC$Message.media) != null) {
            return isAnimatedStickerDocument(tLRPC$MessageMedia.document, !isEncryptedDialog || tLRPC$Message.out);
        }
        return false;
    }

    public static boolean isLocationMessage(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        return (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeo) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeoLive) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaVenue);
    }

    public static boolean isMaskMessage(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        return tLRPC$MessageMedia != null && isMaskDocument(tLRPC$MessageMedia.document);
    }

    public static boolean isMusicMessage(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            return isMusicDocument(tLRPC$MessageMedia.webpage.document);
        }
        return tLRPC$MessageMedia != null && isMusicDocument(tLRPC$MessageMedia.document);
    }

    public static boolean isGifMessage(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            return isGifDocument(tLRPC$MessageMedia.webpage.document);
        }
        if (tLRPC$MessageMedia != null) {
            if (isGifDocument(tLRPC$MessageMedia.document, tLRPC$Message.grouped_id != 0)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRoundVideoMessage(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            return isRoundVideoDocument(tLRPC$MessageMedia.webpage.document);
        }
        return tLRPC$MessageMedia != null && isRoundVideoDocument(tLRPC$MessageMedia.document);
    }

    public static boolean isPhoto(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            TLRPC$WebPage tLRPC$WebPage = tLRPC$MessageMedia.webpage;
            return (tLRPC$WebPage.photo instanceof TLRPC$TL_photo) && !(tLRPC$WebPage.document instanceof TLRPC$TL_document);
        }
        return tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto;
    }

    public static boolean isVoiceMessage(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            return isVoiceDocument(tLRPC$MessageMedia.webpage.document);
        }
        return tLRPC$MessageMedia != null && isVoiceDocument(tLRPC$MessageMedia.document);
    }

    public static boolean isNewGifMessage(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            return isNewGifDocument(tLRPC$MessageMedia.webpage.document);
        }
        return tLRPC$MessageMedia != null && isNewGifDocument(tLRPC$MessageMedia.document);
    }

    public static boolean isLiveLocationMessage(TLRPC$Message tLRPC$Message) {
        return tLRPC$Message.media instanceof TLRPC$TL_messageMediaGeoLive;
    }

    public static boolean isVideoMessage(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia == null || !isVideoSticker(tLRPC$MessageMedia.document)) {
            TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$Message.media;
            if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaWebPage) {
                return isVideoDocument(tLRPC$MessageMedia2.webpage.document);
            }
            return tLRPC$MessageMedia2 != null && isVideoDocument(tLRPC$MessageMedia2.document);
        }
        return false;
    }

    public static boolean isGameMessage(TLRPC$Message tLRPC$Message) {
        return tLRPC$Message.media instanceof TLRPC$TL_messageMediaGame;
    }

    public static boolean isInvoiceMessage(TLRPC$Message tLRPC$Message) {
        return tLRPC$Message.media instanceof TLRPC$TL_messageMediaInvoice;
    }

    public static TLRPC$InputStickerSet getInputStickerSet(TLRPC$Message tLRPC$Message) {
        TLRPC$Document document = getDocument(tLRPC$Message);
        if (document != null) {
            return getInputStickerSet(document);
        }
        return null;
    }

    public static TLRPC$InputStickerSet getInputStickerSet(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null) {
            return null;
        }
        int size = tLRPC$Document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                TLRPC$InputStickerSet tLRPC$InputStickerSet = tLRPC$DocumentAttribute.stickerset;
                if (!(tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty)) {
                    return tLRPC$InputStickerSet;
                }
                return null;
            }
        }
        return null;
    }

    public static long getStickerSetId(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null) {
            return -1L;
        }
        for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                TLRPC$InputStickerSet tLRPC$InputStickerSet = tLRPC$DocumentAttribute.stickerset;
                if (!(tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty)) {
                    return tLRPC$InputStickerSet.id;
                }
                return -1L;
            }
        }
        return -1L;
    }

    public static String getStickerSetName(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null) {
            return null;
        }
        for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                TLRPC$InputStickerSet tLRPC$InputStickerSet = tLRPC$DocumentAttribute.stickerset;
                if (!(tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty)) {
                    return tLRPC$InputStickerSet.short_name;
                }
                return null;
            }
        }
        return null;
    }

    public String getStickerChar() {
        TLRPC$Document document = getDocument();
        if (document != null) {
            Iterator<TLRPC$DocumentAttribute> it = document.attributes.iterator();
            while (it.hasNext()) {
                TLRPC$DocumentAttribute next = it.next();
                if (next instanceof TLRPC$TL_documentAttributeSticker) {
                    return next.alt;
                }
            }
            return null;
        }
        return null;
    }

    public int getApproximateHeight() {
        int i;
        int i2;
        int i3;
        TLRPC$PhotoSize closestPhotoSizeWithSize;
        int i4;
        int i5 = this.type;
        int i6 = 0;
        if (i5 == 0) {
            int i7 = this.textHeight;
            TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
            if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) && (tLRPC$MessageMedia.webpage instanceof TLRPC$TL_webPage)) {
                i6 = AndroidUtilities.dp(100.0f);
            }
            int i8 = i7 + i6;
            return isReply() ? i8 + AndroidUtilities.dp(42.0f) : i8;
        } else if (i5 == 2) {
            return AndroidUtilities.dp(72.0f);
        } else {
            if (i5 == 12) {
                return AndroidUtilities.dp(71.0f);
            }
            if (i5 == 9) {
                return AndroidUtilities.dp(100.0f);
            }
            if (i5 == 4) {
                return AndroidUtilities.dp(114.0f);
            }
            if (i5 == 14) {
                return AndroidUtilities.dp(82.0f);
            }
            if (i5 == 10) {
                return AndroidUtilities.dp(30.0f);
            }
            if (i5 == 11) {
                return AndroidUtilities.dp(50.0f);
            }
            if (i5 == 5) {
                return AndroidUtilities.roundMessageSize;
            }
            if (i5 == 13 || i5 == 15) {
                float f = AndroidUtilities.displaySize.y * 0.4f;
                if (AndroidUtilities.isTablet()) {
                    i = AndroidUtilities.getMinTabletSide();
                } else {
                    i = AndroidUtilities.displaySize.x;
                }
                float f2 = i * 0.5f;
                TLRPC$Document document = getDocument();
                int size = document.attributes.size();
                int i9 = 0;
                while (true) {
                    if (i9 >= size) {
                        i2 = 0;
                        break;
                    }
                    TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i9);
                    if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeImageSize) {
                        i6 = tLRPC$DocumentAttribute.w;
                        i2 = tLRPC$DocumentAttribute.h;
                        break;
                    }
                    i9++;
                }
                if (i6 == 0) {
                    i2 = (int) f;
                    i6 = AndroidUtilities.dp(100.0f) + i2;
                }
                float f3 = i2;
                if (f3 > f) {
                    i6 = (int) (i6 * (f / f3));
                    i2 = (int) f;
                }
                float f4 = i6;
                if (f4 > f2) {
                    i2 = (int) (i2 * (f2 / f4));
                }
                return i2 + AndroidUtilities.dp(14.0f);
            }
            if (AndroidUtilities.isTablet()) {
                i3 = AndroidUtilities.getMinTabletSide();
            } else {
                Point point = AndroidUtilities.displaySize;
                i3 = Math.min(point.x, point.y);
            }
            int i10 = (int) (i3 * 0.7f);
            int dp = AndroidUtilities.dp(100.0f) + i10;
            if (i10 > AndroidUtilities.getPhotoSize()) {
                i10 = AndroidUtilities.getPhotoSize();
            }
            if (dp > AndroidUtilities.getPhotoSize()) {
                dp = AndroidUtilities.getPhotoSize();
            }
            if (FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize()) != null) {
                int i11 = (int) (closestPhotoSizeWithSize.h / (closestPhotoSizeWithSize.w / i10));
                if (i11 == 0) {
                    i11 = AndroidUtilities.dp(100.0f);
                }
                if (i11 <= dp) {
                    dp = i11 < AndroidUtilities.dp(120.0f) ? AndroidUtilities.dp(120.0f) : i11;
                }
                if (needDrawBluredPreview()) {
                    if (AndroidUtilities.isTablet()) {
                        i4 = AndroidUtilities.getMinTabletSide();
                    } else {
                        Point point2 = AndroidUtilities.displaySize;
                        i4 = Math.min(point2.x, point2.y);
                    }
                    dp = (int) (i4 * 0.5f);
                }
            }
            return dp + AndroidUtilities.dp(14.0f);
        }
    }

    private int getParentWidth() {
        int i;
        return (!this.preview || (i = this.parentWidth) <= 0) ? AndroidUtilities.displaySize.x : i;
    }

    public String getStickerEmoji() {
        TLRPC$Document document = getDocument();
        if (document == null) {
            return null;
        }
        for (int i = 0; i < document.attributes.size(); i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                String str = tLRPC$DocumentAttribute.alt;
                if (str != null && str.length() > 0) {
                    return tLRPC$DocumentAttribute.alt;
                }
                return null;
            }
        }
        return null;
    }

    public boolean isVideoCall() {
        TLRPC$MessageAction tLRPC$MessageAction = this.messageOwner.action;
        return (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) && tLRPC$MessageAction.video;
    }

    public boolean isAnimatedEmoji() {
        return this.emojiAnimatedSticker != null;
    }

    public boolean isDice() {
        return this.messageOwner.media instanceof TLRPC$TL_messageMediaDice;
    }

    public String getDiceEmoji() {
        if (!isDice()) {
            return null;
        }
        TLRPC$TL_messageMediaDice tLRPC$TL_messageMediaDice = (TLRPC$TL_messageMediaDice) this.messageOwner.media;
        return TextUtils.isEmpty(tLRPC$TL_messageMediaDice.emoticon) ? "🎲" : tLRPC$TL_messageMediaDice.emoticon.replace("️", "");
    }

    public int getDiceValue() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDice) {
            return ((TLRPC$TL_messageMediaDice) tLRPC$MessageMedia).value;
        }
        return -1;
    }

    public boolean isSticker() {
        int i = this.type;
        return i != 1000 ? i == 13 : isStickerDocument(getDocument()) || isVideoSticker(getDocument());
    }

    public boolean isAnimatedSticker() {
        int i = this.type;
        boolean z = false;
        if (i != 1000) {
            return i == 15;
        }
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(getDialogId());
        if (isEncryptedDialog && this.messageOwner.stickerVerified != 1) {
            return false;
        }
        TLRPC$Document document = getDocument();
        if (this.emojiAnimatedSticker != null || !isEncryptedDialog || isOut()) {
            z = true;
        }
        return isAnimatedStickerDocument(document, z);
    }

    public boolean isAnyKindOfSticker() {
        int i = this.type;
        return i == 13 || i == 15;
    }

    public boolean shouldDrawWithoutBackground() {
        int i = this.type;
        return i == 13 || i == 15 || i == 5;
    }

    public boolean isLocation() {
        return isLocationMessage(this.messageOwner);
    }

    public boolean isMask() {
        return isMaskMessage(this.messageOwner);
    }

    public boolean isMusic() {
        return isMusicMessage(this.messageOwner) && !isVideo();
    }

    public boolean isDocument() {
        return getDocument() != null && !isVideo() && !isMusic() && !isVoice() && !isAnyKindOfSticker();
    }

    public boolean isVoice() {
        return isVoiceMessage(this.messageOwner);
    }

    public boolean isVideo() {
        return isVideoMessage(this.messageOwner);
    }

    public boolean isPhoto() {
        return isPhoto(this.messageOwner);
    }

    public boolean isLiveLocation() {
        return isLiveLocationMessage(this.messageOwner);
    }

    public boolean isExpiredLiveLocation(int i) {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message.date + tLRPC$Message.media.period <= i;
    }

    public boolean isGame() {
        return isGameMessage(this.messageOwner);
    }

    public boolean isInvoice() {
        return isInvoiceMessage(this.messageOwner);
    }

    public boolean isRoundVideo() {
        if (this.isRoundVideoCached == 0) {
            this.isRoundVideoCached = (this.type == 5 || isRoundVideoMessage(this.messageOwner)) ? 1 : 2;
        }
        return this.isRoundVideoCached == 1;
    }

    public boolean shouldAnimateSending() {
        return isSending() && (this.type == 5 || isVoice() || ((isAnyKindOfSticker() && this.sendAnimationData != null) || !(this.messageText == null || this.sendAnimationData == null)));
    }

    public boolean hasAttachedStickers() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
            TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia.photo;
            return tLRPC$Photo != null && tLRPC$Photo.has_stickers;
        } else if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument)) {
            return false;
        } else {
            return isDocumentHasAttachedStickers(tLRPC$MessageMedia.document);
        }
    }

    public static boolean isDocumentHasAttachedStickers(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                if (tLRPC$Document.attributes.get(i) instanceof TLRPC$TL_documentAttributeHasStickers) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGif() {
        return isGifMessage(this.messageOwner);
    }

    public boolean isWebpageDocument() {
        TLRPC$Document tLRPC$Document;
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) && (tLRPC$Document = tLRPC$MessageMedia.webpage.document) != null && !isGifDocument(tLRPC$Document);
    }

    public boolean isWebpage() {
        return this.messageOwner.media instanceof TLRPC$TL_messageMediaWebPage;
    }

    public boolean isNewGif() {
        return this.messageOwner.media != null && isNewGifDocument(getDocument());
    }

    public boolean isAndroidTheme() {
        TLRPC$WebPage tLRPC$WebPage;
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        if (tLRPC$MessageMedia != null && (tLRPC$WebPage = tLRPC$MessageMedia.webpage) != null && !tLRPC$WebPage.attributes.isEmpty()) {
            int size = this.messageOwner.media.webpage.attributes.size();
            for (int i = 0; i < size; i++) {
                TLRPC$TL_webPageAttributeTheme tLRPC$TL_webPageAttributeTheme = this.messageOwner.media.webpage.attributes.get(i);
                ArrayList<TLRPC$Document> arrayList = tLRPC$TL_webPageAttributeTheme.documents;
                int size2 = arrayList.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    if ("application/x-tgtheme-android".equals(arrayList.get(i2).mime_type)) {
                        return true;
                    }
                }
                if (tLRPC$TL_webPageAttributeTheme.settings != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getMusicTitle() {
        return getMusicTitle(true);
    }

    public String getMusicTitle(boolean z) {
        TLRPC$Document document = getDocument();
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                    if (tLRPC$DocumentAttribute.voice) {
                        if (z) {
                            return LocaleController.formatDateAudio(this.messageOwner.date, true);
                        }
                        return null;
                    }
                    String str = tLRPC$DocumentAttribute.title;
                    if (str != null && str.length() != 0) {
                        return str;
                    }
                    String documentFileName = FileLoader.getDocumentFileName(document);
                    return (!TextUtils.isEmpty(documentFileName) || !z) ? documentFileName : LocaleController.getString("AudioUnknownTitle", 2131624516);
                } else if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) && tLRPC$DocumentAttribute.round_message) {
                    return LocaleController.formatDateAudio(this.messageOwner.date, true);
                }
            }
            String documentFileName2 = FileLoader.getDocumentFileName(document);
            if (!TextUtils.isEmpty(documentFileName2)) {
                return documentFileName2;
            }
        }
        return LocaleController.getString("AudioUnknownTitle", 2131624516);
    }

    public int getDuration() {
        TLRPC$Document document = getDocument();
        if (document == null) {
            return 0;
        }
        int i = this.audioPlayerDuration;
        if (i > 0) {
            return i;
        }
        for (int i2 = 0; i2 < document.attributes.size(); i2++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i2);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                return tLRPC$DocumentAttribute.duration;
            }
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                return tLRPC$DocumentAttribute.duration;
            }
        }
        return this.audioPlayerDuration;
    }

    public String getArtworkUrl(boolean z) {
        TLRPC$Document document = getDocument();
        if (document == null || "audio/ogg".equals(document.mime_type)) {
            return null;
        }
        int size = document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                if (tLRPC$DocumentAttribute.voice) {
                    return null;
                }
                String str = tLRPC$DocumentAttribute.performer;
                String str2 = tLRPC$DocumentAttribute.title;
                if (!TextUtils.isEmpty(str)) {
                    int i2 = 0;
                    while (true) {
                        String[] strArr = excludeWords;
                        if (i2 >= strArr.length) {
                            break;
                        }
                        str = str.replace(strArr[i2], " ");
                        i2++;
                    }
                }
                if (TextUtils.isEmpty(str) && TextUtils.isEmpty(str2)) {
                    return null;
                }
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("athumb://itunes.apple.com/search?term=");
                    sb.append(URLEncoder.encode(str + " - " + str2, "UTF-8"));
                    sb.append("&entity=song&limit=4");
                    sb.append(z ? "&s=1" : "");
                    return sb.toString();
                } catch (Exception unused) {
                    continue;
                }
            }
        }
        return null;
    }

    public String getMusicAuthor() {
        return getMusicAuthor(true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x003d, code lost:
        if (r5.round_message != false) goto L11;
     */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0158 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String getMusicAuthor(boolean z) {
        TLRPC$Chat tLRPC$Chat;
        TLRPC$User user;
        String str;
        TLRPC$Document document = getDocument();
        if (document != null) {
            boolean z2 = false;
            for (int i = 0; i < document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                    if (!tLRPC$DocumentAttribute.voice) {
                        String str2 = tLRPC$DocumentAttribute.performer;
                        return (!TextUtils.isEmpty(str2) || !z) ? str2 : LocaleController.getString("AudioUnknownArtist", 2131624515);
                    }
                } else {
                    if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                    }
                    if (!z2) {
                        TLRPC$User tLRPC$User = null;
                        if (!z) {
                            return null;
                        }
                        if (!isOutOwner()) {
                            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
                            if (tLRPC$MessageFwdHeader != null) {
                                TLRPC$Peer tLRPC$Peer = tLRPC$MessageFwdHeader.from_id;
                                if ((tLRPC$Peer instanceof TLRPC$TL_peerUser) && tLRPC$Peer.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                }
                            }
                            TLRPC$Message tLRPC$Message = this.messageOwner;
                            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader2 = tLRPC$Message.fwd_from;
                            if (tLRPC$MessageFwdHeader2 != null && (tLRPC$MessageFwdHeader2.from_id instanceof TLRPC$TL_peerChannel)) {
                                tLRPC$Chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.channel_id));
                            } else if (tLRPC$MessageFwdHeader2 != null && (tLRPC$MessageFwdHeader2.from_id instanceof TLRPC$TL_peerChat)) {
                                tLRPC$Chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.chat_id));
                            } else {
                                if (tLRPC$MessageFwdHeader2 != null && (tLRPC$MessageFwdHeader2.from_id instanceof TLRPC$TL_peerUser)) {
                                    user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.fwd_from.from_id.user_id));
                                } else if (tLRPC$MessageFwdHeader2 != null && (str = tLRPC$MessageFwdHeader2.from_name) != null) {
                                    return str;
                                } else {
                                    TLRPC$Peer tLRPC$Peer2 = tLRPC$Message.from_id;
                                    if (tLRPC$Peer2 instanceof TLRPC$TL_peerChat) {
                                        tLRPC$Chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.from_id.chat_id));
                                    } else if (tLRPC$Peer2 instanceof TLRPC$TL_peerChannel) {
                                        tLRPC$Chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.from_id.channel_id));
                                    } else if (tLRPC$Peer2 == null && tLRPC$Message.peer_id.channel_id != 0) {
                                        tLRPC$Chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.peer_id.channel_id));
                                    } else {
                                        user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
                                    }
                                }
                                TLRPC$User tLRPC$User2 = user;
                                tLRPC$Chat = null;
                                tLRPC$User = tLRPC$User2;
                            }
                            if (tLRPC$User != null) {
                                return UserObject.getUserName(tLRPC$User);
                            }
                            if (tLRPC$Chat != null) {
                                return tLRPC$Chat.title;
                            }
                        }
                        return LocaleController.getString("FromYou", 2131626036);
                    }
                }
                z2 = true;
                if (!z2) {
                }
            }
        }
        return LocaleController.getString("AudioUnknownArtist", 2131624515);
    }

    public TLRPC$InputStickerSet getInputStickerSet() {
        return getInputStickerSet(this.messageOwner);
    }

    public boolean isForwarded() {
        return isForwardedMessage(this.messageOwner);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0020, code lost:
        if (r1.channel_id == r0.channel_id) goto L17;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean needDrawForwarded() {
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if ((tLRPC$Message.flags & 4) != 0 && (tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) != null && !tLRPC$MessageFwdHeader.imported) {
            TLRPC$Peer tLRPC$Peer = tLRPC$MessageFwdHeader.saved_from_peer;
            if (tLRPC$Peer != null) {
                TLRPC$Peer tLRPC$Peer2 = tLRPC$MessageFwdHeader.from_id;
                if (tLRPC$Peer2 instanceof TLRPC$TL_peerChannel) {
                }
            }
            if (UserConfig.getInstance(this.currentAccount).getClientUserId() != getDialogId()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isForwardedMessage(TLRPC$Message tLRPC$Message) {
        return ((tLRPC$Message.flags & 4) == 0 || tLRPC$Message.fwd_from == null) ? false : true;
    }

    public boolean isReply() {
        TLRPC$Message tLRPC$Message;
        TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader;
        MessageObject messageObject = this.replyMessageObject;
        return ((messageObject != null && (messageObject.messageOwner instanceof TLRPC$TL_messageEmpty)) || (tLRPC$TL_messageReplyHeader = (tLRPC$Message = this.messageOwner).reply_to) == null || (tLRPC$TL_messageReplyHeader.reply_to_msg_id == 0 && tLRPC$TL_messageReplyHeader.reply_to_random_id == 0) || (tLRPC$Message.flags & 8) == 0) ? false : true;
    }

    public boolean isMediaEmpty() {
        return isMediaEmpty(this.messageOwner);
    }

    public boolean isMediaEmptyWebpage() {
        return isMediaEmptyWebpage(this.messageOwner);
    }

    public static boolean isMediaEmpty(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia;
        return tLRPC$Message == null || (tLRPC$MessageMedia = tLRPC$Message.media) == null || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaEmpty) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage);
    }

    public static boolean isMediaEmptyWebpage(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia;
        return tLRPC$Message == null || (tLRPC$MessageMedia = tLRPC$Message.media) == null || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaEmpty);
    }

    public boolean hasReplies() {
        TLRPC$MessageReplies tLRPC$MessageReplies = this.messageOwner.replies;
        return tLRPC$MessageReplies != null && tLRPC$MessageReplies.replies > 0;
    }

    public boolean canViewThread() {
        MessageObject messageObject;
        if (this.messageOwner.action != null) {
            return false;
        }
        return hasReplies() || !(((messageObject = this.replyMessageObject) == null || messageObject.messageOwner.replies == null) && getReplyTopMsgId() == 0);
    }

    public boolean isComments() {
        TLRPC$MessageReplies tLRPC$MessageReplies = this.messageOwner.replies;
        return tLRPC$MessageReplies != null && tLRPC$MessageReplies.comments;
    }

    public boolean isLinkedToChat(long j) {
        TLRPC$MessageReplies tLRPC$MessageReplies = this.messageOwner.replies;
        return tLRPC$MessageReplies != null && (j == 0 || tLRPC$MessageReplies.channel_id == j);
    }

    public int getRepliesCount() {
        TLRPC$MessageReplies tLRPC$MessageReplies = this.messageOwner.replies;
        if (tLRPC$MessageReplies != null) {
            return tLRPC$MessageReplies.replies;
        }
        return 0;
    }

    public boolean canEditMessage(TLRPC$Chat tLRPC$Chat) {
        return canEditMessage(this.currentAccount, this.messageOwner, tLRPC$Chat, this.scheduled);
    }

    public boolean canEditMessageScheduleTime(TLRPC$Chat tLRPC$Chat) {
        return canEditMessageScheduleTime(this.currentAccount, this.messageOwner, tLRPC$Chat);
    }

    public boolean canForwardMessage() {
        return !(this.messageOwner instanceof TLRPC$TL_message_secret) && !needDrawBluredPreview() && !isLiveLocation() && this.type != 16 && !isSponsored() && !this.messageOwner.noforwards;
    }

    public boolean canEditMedia() {
        if (isSecretMedia()) {
            return false;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
            return true;
        }
        return (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) && !isVoice() && !isSticker() && !isAnimatedSticker() && !isRoundVideo();
    }

    public boolean canEditMessageAnytime(TLRPC$Chat tLRPC$Chat) {
        return canEditMessageAnytime(this.currentAccount, this.messageOwner, tLRPC$Chat);
    }

    public static boolean canEditMessageAnytime(int i, TLRPC$Message tLRPC$Message, TLRPC$Chat tLRPC$Chat) {
        TLRPC$MessageMedia tLRPC$MessageMedia;
        TLRPC$MessageAction tLRPC$MessageAction;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2;
        if (tLRPC$Message != null && tLRPC$Message.peer_id != null && (((tLRPC$MessageMedia = tLRPC$Message.media) == null || (!isRoundVideoDocument(tLRPC$MessageMedia.document) && !isStickerDocument(tLRPC$Message.media.document) && !isAnimatedStickerDocument(tLRPC$Message.media.document, true))) && (((tLRPC$MessageAction = tLRPC$Message.action) == null || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionEmpty)) && !isForwardedMessage(tLRPC$Message) && tLRPC$Message.via_bot_id == 0 && tLRPC$Message.id >= 0))) {
            TLRPC$Peer tLRPC$Peer = tLRPC$Message.from_id;
            if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
                long j = tLRPC$Peer.user_id;
                if (j == tLRPC$Message.peer_id.user_id && j == UserConfig.getInstance(i).getClientUserId() && !isLiveLocationMessage(tLRPC$Message)) {
                    return true;
                }
            }
            if (tLRPC$Chat == null && tLRPC$Message.peer_id.channel_id != 0 && (tLRPC$Chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(tLRPC$Message.peer_id.channel_id))) == null) {
                return false;
            }
            if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup && (tLRPC$Chat.creator || ((tLRPC$TL_chatAdminRights2 = tLRPC$Chat.admin_rights) != null && tLRPC$TL_chatAdminRights2.edit_messages))) {
                return true;
            }
            if (tLRPC$Message.out && tLRPC$Chat != null && tLRPC$Chat.megagroup && (tLRPC$Chat.creator || (((tLRPC$TL_chatAdminRights = tLRPC$Chat.admin_rights) != null && tLRPC$TL_chatAdminRights.pin_messages) || ((tLRPC$TL_chatBannedRights = tLRPC$Chat.default_banned_rights) != null && !tLRPC$TL_chatBannedRights.pin_messages)))) {
                return true;
            }
        }
        return false;
    }

    public static boolean canEditMessageScheduleTime(int i, TLRPC$Message tLRPC$Message, TLRPC$Chat tLRPC$Chat) {
        if (tLRPC$Chat == null && tLRPC$Message.peer_id.channel_id != 0 && (tLRPC$Chat = MessagesController.getInstance(i).getChat(Long.valueOf(tLRPC$Message.peer_id.channel_id))) == null) {
            return false;
        }
        if (!ChatObject.isChannel(tLRPC$Chat) || tLRPC$Chat.megagroup || tLRPC$Chat.creator) {
            return true;
        }
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights = tLRPC$Chat.admin_rights;
        return tLRPC$TL_chatAdminRights != null && (tLRPC$TL_chatAdminRights.edit_messages || tLRPC$Message.out);
    }

    public static boolean canEditMessage(int i, TLRPC$Message tLRPC$Message, TLRPC$Chat tLRPC$Chat, boolean z) {
        TLRPC$MessageMedia tLRPC$MessageMedia;
        TLRPC$MessageAction tLRPC$MessageAction;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights3;
        if (!z || tLRPC$Message.date >= ConnectionsManager.getInstance(i).getCurrentTime() - 60) {
            if ((tLRPC$Chat == null || ((!tLRPC$Chat.left && !tLRPC$Chat.kicked) || (tLRPC$Chat.megagroup && tLRPC$Chat.has_link))) && tLRPC$Message != null && tLRPC$Message.peer_id != null && (((tLRPC$MessageMedia = tLRPC$Message.media) == null || (!isRoundVideoDocument(tLRPC$MessageMedia.document) && !isStickerDocument(tLRPC$Message.media.document) && !isAnimatedStickerDocument(tLRPC$Message.media.document, true) && !isLocationMessage(tLRPC$Message))) && (((tLRPC$MessageAction = tLRPC$Message.action) == null || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionEmpty)) && !isForwardedMessage(tLRPC$Message) && tLRPC$Message.via_bot_id == 0 && tLRPC$Message.id >= 0))) {
                TLRPC$Peer tLRPC$Peer = tLRPC$Message.from_id;
                if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
                    long j = tLRPC$Peer.user_id;
                    if (j == tLRPC$Message.peer_id.user_id && j == UserConfig.getInstance(i).getClientUserId() && !isLiveLocationMessage(tLRPC$Message) && !(tLRPC$Message.media instanceof TLRPC$TL_messageMediaContact)) {
                        return true;
                    }
                }
                if (tLRPC$Chat == null && tLRPC$Message.peer_id.channel_id != 0 && (tLRPC$Chat = MessagesController.getInstance(i).getChat(Long.valueOf(tLRPC$Message.peer_id.channel_id))) == null) {
                    return false;
                }
                TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$Message.media;
                if (tLRPC$MessageMedia2 != null && !(tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaEmpty) && !(tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto) && !(tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument) && !(tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaWebPage)) {
                    return false;
                }
                if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup && (tLRPC$Chat.creator || ((tLRPC$TL_chatAdminRights3 = tLRPC$Chat.admin_rights) != null && tLRPC$TL_chatAdminRights3.edit_messages))) {
                    return true;
                }
                if (tLRPC$Message.out && tLRPC$Chat != null && tLRPC$Chat.megagroup && (tLRPC$Chat.creator || (((tLRPC$TL_chatAdminRights2 = tLRPC$Chat.admin_rights) != null && tLRPC$TL_chatAdminRights2.pin_messages) || ((tLRPC$TL_chatBannedRights = tLRPC$Chat.default_banned_rights) != null && !tLRPC$TL_chatBannedRights.pin_messages)))) {
                    return true;
                }
                if (!z && Math.abs(tLRPC$Message.date - ConnectionsManager.getInstance(i).getCurrentTime()) > MessagesController.getInstance(i).maxEditTime) {
                    return false;
                }
                if (tLRPC$Message.peer_id.channel_id == 0) {
                    if (!tLRPC$Message.out) {
                        TLRPC$Peer tLRPC$Peer2 = tLRPC$Message.from_id;
                        if (!(tLRPC$Peer2 instanceof TLRPC$TL_peerUser) || tLRPC$Peer2.user_id != UserConfig.getInstance(i).getClientUserId()) {
                            return false;
                        }
                    }
                    TLRPC$MessageMedia tLRPC$MessageMedia3 = tLRPC$Message.media;
                    if (!(tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaPhoto) && (!(tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaDocument) || isStickerMessage(tLRPC$Message) || isAnimatedStickerMessage(tLRPC$Message))) {
                        TLRPC$MessageMedia tLRPC$MessageMedia4 = tLRPC$Message.media;
                        if (!(tLRPC$MessageMedia4 instanceof TLRPC$TL_messageMediaEmpty) && !(tLRPC$MessageMedia4 instanceof TLRPC$TL_messageMediaWebPage) && tLRPC$MessageMedia4 != null) {
                            return false;
                        }
                    }
                    return true;
                } else if ((tLRPC$Chat != null && tLRPC$Chat.megagroup && tLRPC$Message.out) || (tLRPC$Chat != null && !tLRPC$Chat.megagroup && ((tLRPC$Chat.creator || ((tLRPC$TL_chatAdminRights = tLRPC$Chat.admin_rights) != null && (tLRPC$TL_chatAdminRights.edit_messages || (tLRPC$Message.out && tLRPC$TL_chatAdminRights.post_messages)))) && tLRPC$Message.post))) {
                    TLRPC$MessageMedia tLRPC$MessageMedia5 = tLRPC$Message.media;
                    if (!(tLRPC$MessageMedia5 instanceof TLRPC$TL_messageMediaPhoto) && (!(tLRPC$MessageMedia5 instanceof TLRPC$TL_messageMediaDocument) || isStickerMessage(tLRPC$Message) || isAnimatedStickerMessage(tLRPC$Message))) {
                        TLRPC$MessageMedia tLRPC$MessageMedia6 = tLRPC$Message.media;
                        if ((tLRPC$MessageMedia6 instanceof TLRPC$TL_messageMediaEmpty) || (tLRPC$MessageMedia6 instanceof TLRPC$TL_messageMediaWebPage) || tLRPC$MessageMedia6 == null) {
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean canDeleteMessage(boolean z, TLRPC$Chat tLRPC$Chat) {
        return this.eventId == 0 && this.sponsoredId == null && canDeleteMessage(this.currentAccount, z, this.messageOwner, tLRPC$Chat);
    }

    public static boolean canDeleteMessage(int i, boolean z, TLRPC$Message tLRPC$Message, TLRPC$Chat tLRPC$Chat) {
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        if (tLRPC$Message == null) {
            return false;
        }
        if (ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat) && (tLRPC$Message.action instanceof TLRPC$TL_messageActionChatJoinedByRequest)) {
            return false;
        }
        if (tLRPC$Message.id < 0) {
            return true;
        }
        if (tLRPC$Chat == null && tLRPC$Message.peer_id.channel_id != 0) {
            tLRPC$Chat = MessagesController.getInstance(i).getChat(Long.valueOf(tLRPC$Message.peer_id.channel_id));
        }
        if (!ChatObject.isChannel(tLRPC$Chat)) {
            return z || isOut(tLRPC$Message) || !ChatObject.isChannel(tLRPC$Chat);
        } else if (z && !tLRPC$Chat.megagroup) {
            if (!tLRPC$Chat.creator) {
                TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2 = tLRPC$Chat.admin_rights;
                if (tLRPC$TL_chatAdminRights2 == null) {
                    return false;
                }
                if (!tLRPC$TL_chatAdminRights2.delete_messages && !tLRPC$Message.out) {
                    return false;
                }
            }
            return true;
        } else {
            boolean z2 = tLRPC$Message.out;
            if (z2 && (tLRPC$Message instanceof TLRPC$TL_messageService)) {
                return tLRPC$Message.id != 1 && ChatObject.canUserDoAdminAction(tLRPC$Chat, 13);
            }
            if (!z) {
                if (tLRPC$Message.id == 1) {
                    return false;
                }
                if (!tLRPC$Chat.creator && (((tLRPC$TL_chatAdminRights = tLRPC$Chat.admin_rights) == null || (!tLRPC$TL_chatAdminRights.delete_messages && (!z2 || (!tLRPC$Chat.megagroup && !tLRPC$TL_chatAdminRights.post_messages)))) && (!tLRPC$Chat.megagroup || !z2))) {
                    return false;
                }
            }
            return true;
        }
    }

    public String getForwardedName() {
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
        if (tLRPC$MessageFwdHeader != null) {
            TLRPC$Peer tLRPC$Peer = tLRPC$MessageFwdHeader.from_id;
            if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
                TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.channel_id));
                if (chat == null) {
                    return null;
                }
                return chat.title;
            } else if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
                TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.chat_id));
                if (chat2 == null) {
                    return null;
                }
                return chat2.title;
            } else if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.fwd_from.from_id.user_id));
                if (user == null) {
                    return null;
                }
                return UserObject.getUserName(user);
            } else {
                String str = tLRPC$MessageFwdHeader.from_name;
                if (str == null) {
                    return null;
                }
                return str;
            }
        }
        return null;
    }

    public int getReplyMsgId() {
        TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = this.messageOwner.reply_to;
        if (tLRPC$TL_messageReplyHeader != null) {
            return tLRPC$TL_messageReplyHeader.reply_to_msg_id;
        }
        return 0;
    }

    public int getReplyTopMsgId() {
        TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = this.messageOwner.reply_to;
        if (tLRPC$TL_messageReplyHeader != null) {
            return tLRPC$TL_messageReplyHeader.reply_to_top_id;
        }
        return 0;
    }

    public static long getReplyToDialogId(TLRPC$Message tLRPC$Message) {
        TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = tLRPC$Message.reply_to;
        if (tLRPC$TL_messageReplyHeader == null) {
            return 0L;
        }
        TLRPC$Peer tLRPC$Peer = tLRPC$TL_messageReplyHeader.reply_to_peer_id;
        if (tLRPC$Peer != null) {
            return getPeerId(tLRPC$Peer);
        }
        return getDialogId(tLRPC$Message);
    }

    public int getReplyAnyMsgId() {
        TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = this.messageOwner.reply_to;
        if (tLRPC$TL_messageReplyHeader != null) {
            int i = tLRPC$TL_messageReplyHeader.reply_to_top_id;
            return i != 0 ? i : tLRPC$TL_messageReplyHeader.reply_to_msg_id;
        }
        return 0;
    }

    public boolean isPrivateForward() {
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
        return tLRPC$MessageFwdHeader != null && !TextUtils.isEmpty(tLRPC$MessageFwdHeader.from_name);
    }

    public boolean isImportedForward() {
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
        return tLRPC$MessageFwdHeader != null && tLRPC$MessageFwdHeader.imported;
    }

    public long getSenderId() {
        TLRPC$Peer tLRPC$Peer;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from;
        if (tLRPC$MessageFwdHeader != null && (tLRPC$Peer = tLRPC$MessageFwdHeader.saved_from_peer) != null) {
            long j = tLRPC$Peer.user_id;
            if (j != 0) {
                TLRPC$Peer tLRPC$Peer2 = tLRPC$MessageFwdHeader.from_id;
                return tLRPC$Peer2 instanceof TLRPC$TL_peerUser ? tLRPC$Peer2.user_id : j;
            } else if (tLRPC$Peer.channel_id != 0) {
                if (isSavedFromMegagroup()) {
                    TLRPC$Peer tLRPC$Peer3 = this.messageOwner.fwd_from.from_id;
                    if (tLRPC$Peer3 instanceof TLRPC$TL_peerUser) {
                        return tLRPC$Peer3.user_id;
                    }
                }
                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader2 = this.messageOwner.fwd_from;
                TLRPC$Peer tLRPC$Peer4 = tLRPC$MessageFwdHeader2.from_id;
                if (tLRPC$Peer4 instanceof TLRPC$TL_peerChannel) {
                    return -tLRPC$Peer4.channel_id;
                }
                if (tLRPC$Peer4 instanceof TLRPC$TL_peerChat) {
                    return -tLRPC$Peer4.chat_id;
                }
                return -tLRPC$MessageFwdHeader2.saved_from_peer.channel_id;
            } else {
                long j2 = tLRPC$Peer.chat_id;
                if (j2 != 0) {
                    TLRPC$Peer tLRPC$Peer5 = tLRPC$MessageFwdHeader.from_id;
                    if (tLRPC$Peer5 instanceof TLRPC$TL_peerUser) {
                        return tLRPC$Peer5.user_id;
                    }
                    if (tLRPC$Peer5 instanceof TLRPC$TL_peerChannel) {
                        return -tLRPC$Peer5.channel_id;
                    }
                    return tLRPC$Peer5 instanceof TLRPC$TL_peerChat ? -tLRPC$Peer5.chat_id : -j2;
                }
            }
        } else {
            TLRPC$Peer tLRPC$Peer6 = tLRPC$Message.from_id;
            if (tLRPC$Peer6 instanceof TLRPC$TL_peerUser) {
                return tLRPC$Peer6.user_id;
            }
            if (tLRPC$Peer6 instanceof TLRPC$TL_peerChannel) {
                return -tLRPC$Peer6.channel_id;
            }
            if (tLRPC$Peer6 instanceof TLRPC$TL_peerChat) {
                return -tLRPC$Peer6.chat_id;
            }
            if (tLRPC$Message.post) {
                return tLRPC$Message.peer_id.channel_id;
            }
        }
        return 0L;
    }

    public boolean isWallpaper() {
        TLRPC$WebPage tLRPC$WebPage;
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) && (tLRPC$WebPage = tLRPC$MessageMedia.webpage) != null && "telegram_background".equals(tLRPC$WebPage.type);
    }

    public boolean isTheme() {
        TLRPC$WebPage tLRPC$WebPage;
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) && (tLRPC$WebPage = tLRPC$MessageMedia.webpage) != null && "telegram_theme".equals(tLRPC$WebPage.type);
    }

    public int getMediaExistanceFlags() {
        boolean z = this.attachPathExists;
        return this.mediaExists ? (z ? 1 : 0) | 2 : z ? 1 : 0;
    }

    public void applyMediaExistanceFlags(int i) {
        if (i == -1) {
            checkMediaExistance();
            return;
        }
        boolean z = false;
        this.attachPathExists = (i & 1) != 0;
        if ((i & 2) != 0) {
            z = true;
        }
        this.mediaExists = z;
    }

    public void checkMediaExistance() {
        checkMediaExistance(true);
    }

    public void checkMediaExistance(boolean z) {
        TLRPC$Photo tLRPC$Photo;
        int i;
        this.attachPathExists = false;
        this.mediaExists = false;
        if (this.type == 1 && FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize()) != null) {
            File pathToMessage = FileLoader.getInstance(this.currentAccount).getPathToMessage(this.messageOwner, z);
            if (needDrawBluredPreview()) {
                this.mediaExists = new File(pathToMessage.getAbsolutePath() + ".enc").exists();
            }
            if (!this.mediaExists) {
                this.mediaExists = pathToMessage.exists();
            }
        }
        if ((!this.mediaExists && this.type == 8) || (i = this.type) == 3 || i == 9 || i == 2 || i == 14 || i == 5) {
            String str = this.messageOwner.attachPath;
            if (str != null && str.length() > 0) {
                this.attachPathExists = new File(this.messageOwner.attachPath).exists();
            }
            if (!this.attachPathExists) {
                File pathToMessage2 = FileLoader.getInstance(this.currentAccount).getPathToMessage(this.messageOwner, z);
                if (this.type == 3 && needDrawBluredPreview()) {
                    this.mediaExists = new File(pathToMessage2.getAbsolutePath() + ".enc").exists();
                }
                if (!this.mediaExists) {
                    this.mediaExists = pathToMessage2.exists();
                }
            }
        }
        if (!this.mediaExists) {
            TLRPC$Document document = getDocument();
            if (document != null) {
                if (isWallpaper()) {
                    this.mediaExists = FileLoader.getInstance(this.currentAccount).getPathToAttach(document, null, true, z).exists();
                    return;
                } else {
                    this.mediaExists = FileLoader.getInstance(this.currentAccount).getPathToAttach(document, null, false, z).exists();
                    return;
                }
            }
            int i2 = this.type;
            if (i2 == 0) {
                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize());
                if (closestPhotoSizeWithSize == null) {
                    return;
                }
                this.mediaExists = FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, null, true, z).exists();
            } else if (i2 != 11 || (tLRPC$Photo = this.messageOwner.action.photo) == null || tLRPC$Photo.video_sizes.isEmpty()) {
            } else {
                this.mediaExists = FileLoader.getInstance(this.currentAccount).getPathToAttach(tLRPC$Photo.video_sizes.get(0), null, true, z).exists();
            }
        }
    }

    public void setQuery(String str) {
        String str2;
        int indexOf;
        if (TextUtils.isEmpty(str)) {
            this.highlightedWords = null;
            this.messageTrimmedToHighlight = null;
            return;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        String lowerCase = str.trim().toLowerCase();
        String[] split = lowerCase.split("\\P{L}+");
        ArrayList arrayList2 = new ArrayList();
        if (!TextUtils.isEmpty(this.messageOwner.message)) {
            String lowerCase2 = this.messageOwner.message.trim().toLowerCase();
            if (lowerCase2.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                arrayList.add(lowerCase);
                handleFoundWords(arrayList, split);
                return;
            }
            arrayList2.addAll(Arrays.asList(lowerCase2.split("\\P{L}+")));
        }
        if (getDocument() != null) {
            String lowerCase3 = FileLoader.getDocumentFileName(getDocument()).toLowerCase();
            if (lowerCase3.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                arrayList.add(lowerCase);
            }
            arrayList2.addAll(Arrays.asList(lowerCase3.split("\\P{L}+")));
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            TLRPC$WebPage tLRPC$WebPage = tLRPC$MessageMedia.webpage;
            if (tLRPC$WebPage instanceof TLRPC$TL_webPage) {
                String str3 = tLRPC$WebPage.title;
                if (str3 == null) {
                    str3 = tLRPC$WebPage.site_name;
                }
                if (str3 != null) {
                    String lowerCase4 = str3.toLowerCase();
                    if (lowerCase4.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                        arrayList.add(lowerCase);
                    }
                    arrayList2.addAll(Arrays.asList(lowerCase4.split("\\P{L}+")));
                }
            }
        }
        String musicAuthor = getMusicAuthor();
        if (musicAuthor != null) {
            String lowerCase5 = musicAuthor.toLowerCase();
            if (lowerCase5.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                arrayList.add(lowerCase);
            }
            arrayList2.addAll(Arrays.asList(lowerCase5.split("\\P{L}+")));
        }
        for (String str4 : split) {
            if (str4.length() >= 2) {
                for (int i = 0; i < arrayList2.size(); i++) {
                    if (!arrayList.contains(arrayList2.get(i)) && (indexOf = (str2 = (String) arrayList2.get(i)).indexOf(str4.charAt(0))) >= 0) {
                        int max = Math.max(str4.length(), str2.length());
                        if (indexOf != 0) {
                            str2 = str2.substring(indexOf);
                        }
                        int min = Math.min(str4.length(), str2.length());
                        int i2 = 0;
                        for (int i3 = 0; i3 < min && str2.charAt(i3) == str4.charAt(i3); i3++) {
                            i2++;
                        }
                        if (i2 / max >= 0.5d) {
                            arrayList.add((String) arrayList2.get(i));
                        }
                    }
                }
            }
        }
        handleFoundWords(arrayList, split);
    }

    private void handleFoundWords(ArrayList<String> arrayList, String[] strArr) {
        boolean z;
        if (!arrayList.isEmpty()) {
            boolean z2 = false;
            for (int i = 0; i < arrayList.size(); i++) {
                int i2 = 0;
                while (true) {
                    if (i2 >= strArr.length) {
                        break;
                    } else if (arrayList.get(i).contains(strArr[i2])) {
                        z2 = true;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (z2) {
                    break;
                }
            }
            if (z2) {
                int i3 = 0;
                while (i3 < arrayList.size()) {
                    int i4 = 0;
                    while (true) {
                        if (i4 >= strArr.length) {
                            z = false;
                            break;
                        } else if (arrayList.get(i3).contains(strArr[i4])) {
                            z = true;
                            break;
                        } else {
                            i4++;
                        }
                    }
                    if (!z) {
                        arrayList.remove(i3);
                        i3--;
                    }
                    i3++;
                }
                if (arrayList.size() > 0) {
                    Collections.sort(arrayList, MessageObject$$ExternalSyntheticLambda0.INSTANCE);
                    arrayList.clear();
                    arrayList.add(arrayList.get(0));
                }
            }
            this.highlightedWords = arrayList;
            String str = this.messageOwner.message;
            if (str == null) {
                return;
            }
            String trim = str.replace('\n', ' ').replaceAll(" +", " ").trim();
            int length = trim.length();
            int indexOf = trim.toLowerCase().indexOf(arrayList.get(0));
            if (indexOf < 0) {
                indexOf = 0;
            }
            if (length > 200) {
                int max = Math.max(0, indexOf - 100);
                trim = trim.substring(max, Math.min(length, (indexOf - max) + indexOf + 100));
            }
            this.messageTrimmedToHighlight = trim;
        }
    }

    public static /* synthetic */ int lambda$handleFoundWords$1(String str, String str2) {
        return str2.length() - str.length();
    }

    public void createMediaThumbs() {
        if (isVideo()) {
            TLRPC$Document document = getDocument();
            TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 50);
            this.mediaThumb = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 320), document);
            this.mediaSmallThumb = ImageLocation.getForDocument(closestPhotoSizeWithSize, document);
            return;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) || tLRPC$MessageMedia.photo == null || this.photoThumbs.isEmpty()) {
            return;
        }
        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 50);
        this.mediaThumb = ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 320, false, closestPhotoSizeWithSize2, false), this.photoThumbsObject);
        this.mediaSmallThumb = ImageLocation.getForObject(closestPhotoSizeWithSize2, this.photoThumbsObject);
    }

    public boolean hasHighlightedWords() {
        ArrayList<String> arrayList = this.highlightedWords;
        return arrayList != null && !arrayList.isEmpty();
    }

    public boolean equals(MessageObject messageObject) {
        return getId() == messageObject.getId() && getDialogId() == messageObject.getDialogId();
    }

    public boolean isReactionsAvailable() {
        return !isEditing() && !isSponsored() && isSent() && this.messageOwner.action == null;
    }

    public boolean selectReaction(String str, boolean z, boolean z2) {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message.reactions == null) {
            tLRPC$Message.reactions = new TLRPC$TL_messageReactions();
            this.messageOwner.reactions.can_see_list = isFromGroup() || isFromUser();
        }
        TLRPC$TL_reactionCount tLRPC$TL_reactionCount = null;
        TLRPC$TL_reactionCount tLRPC$TL_reactionCount2 = null;
        for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
            if (this.messageOwner.reactions.results.get(i).chosen) {
                tLRPC$TL_reactionCount = this.messageOwner.reactions.results.get(i);
            }
            if (this.messageOwner.reactions.results.get(i).reaction.equals(str)) {
                tLRPC$TL_reactionCount2 = this.messageOwner.reactions.results.get(i);
            }
        }
        if (tLRPC$TL_reactionCount == null || tLRPC$TL_reactionCount != tLRPC$TL_reactionCount2 || !z) {
            if (tLRPC$TL_reactionCount != null && (tLRPC$TL_reactionCount == tLRPC$TL_reactionCount2 || z2)) {
                tLRPC$TL_reactionCount.chosen = false;
                int i2 = tLRPC$TL_reactionCount.count - 1;
                tLRPC$TL_reactionCount.count = i2;
                if (i2 <= 0) {
                    this.messageOwner.reactions.results.remove(tLRPC$TL_reactionCount);
                }
                if (this.messageOwner.reactions.can_see_list) {
                    int i3 = 0;
                    while (i3 < this.messageOwner.reactions.recent_reactions.size()) {
                        if (getPeerId(this.messageOwner.reactions.recent_reactions.get(i3).peer_id) == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            this.messageOwner.reactions.recent_reactions.remove(i3);
                            i3--;
                        }
                        i3++;
                    }
                }
                this.reactionsChanged = true;
                return false;
            }
            if (tLRPC$TL_reactionCount != null) {
                tLRPC$TL_reactionCount.chosen = false;
                int i4 = tLRPC$TL_reactionCount.count - 1;
                tLRPC$TL_reactionCount.count = i4;
                if (i4 <= 0) {
                    this.messageOwner.reactions.results.remove(tLRPC$TL_reactionCount);
                }
                if (this.messageOwner.reactions.can_see_list) {
                    int i5 = 0;
                    while (i5 < this.messageOwner.reactions.recent_reactions.size()) {
                        if (getPeerId(this.messageOwner.reactions.recent_reactions.get(i5).peer_id) == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            this.messageOwner.reactions.recent_reactions.remove(i5);
                            i5--;
                        }
                        i5++;
                    }
                }
            }
            if (tLRPC$TL_reactionCount2 == null) {
                tLRPC$TL_reactionCount2 = new TLRPC$TL_reactionCount();
                tLRPC$TL_reactionCount2.reaction = str;
                this.messageOwner.reactions.results.add(tLRPC$TL_reactionCount2);
            }
            tLRPC$TL_reactionCount2.chosen = true;
            tLRPC$TL_reactionCount2.count++;
            if (this.messageOwner.reactions.can_see_list) {
                TLRPC$TL_messagePeerReaction tLRPC$TL_messagePeerReaction = new TLRPC$TL_messagePeerReaction();
                this.messageOwner.reactions.recent_reactions.add(0, tLRPC$TL_messagePeerReaction);
                TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                tLRPC$TL_messagePeerReaction.peer_id = tLRPC$TL_peerUser;
                tLRPC$TL_peerUser.user_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                tLRPC$TL_messagePeerReaction.reaction = str;
            }
            this.reactionsChanged = true;
            return true;
        }
        return true;
    }

    public boolean probablyRingtone() {
        if (getDocument() != null && RingtoneDataStore.ringtoneSupportedMimeType.contains(getDocument().mime_type) && getDocument().size < MessagesController.getInstance(this.currentAccount).ringtoneSizeMax * 2) {
            for (int i = 0; i < getDocument().attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = getDocument().attributes.get(i);
                if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) && tLRPC$DocumentAttribute.duration < 60) {
                    return true;
                }
            }
        }
        return false;
    }
}