package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Property;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Keep;
import androidx.collection.LongSparseArray;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import androidx.core.os.BuildCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.ChatListItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SharedPrefsHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.camera.CameraController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$BotInfo;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$BotMenuButton;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$KeyboardButton;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_botMenuButton;
import org.telegram.tgnet.TLRPC$TL_channels_sendAsPeers;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_inputMessageEntityMentionName;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC$TL_keyboardButton;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonBuy;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonCallback;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonGame;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonRequestGeoLocation;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonRequestPhone;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonRequestPoll;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonSimpleWebView;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonSwitchInline;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonUrl;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonUrlAuth;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonUserProfile;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonWebView;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageEntityBold;
import org.telegram.tgnet.TLRPC$TL_messageEntityCode;
import org.telegram.tgnet.TLRPC$TL_messageEntityItalic;
import org.telegram.tgnet.TLRPC$TL_messageEntityMentionName;
import org.telegram.tgnet.TLRPC$TL_messageEntityPre;
import org.telegram.tgnet.TLRPC$TL_messageEntitySpoiler;
import org.telegram.tgnet.TLRPC$TL_messageEntityStrike;
import org.telegram.tgnet.TLRPC$TL_messageEntityTextUrl;
import org.telegram.tgnet.TLRPC$TL_messageEntityUnderline;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_replyKeyboardMarkup;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.BotCommandsMenuView;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SenderSelectPopup;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.TextStyleSpan;
import org.telegram.ui.Components.VideoTimelineView;
import org.telegram.ui.ContentPreviewViewer;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.GroupStickersActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.StickersActivity;
/* loaded from: classes3.dex */
public class ChatActivityEnterView extends BlurredFrameLayout implements NotificationCenter.NotificationCenterDelegate, SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate, StickersAlert.StickersAlertDelegate {
    private AccountInstance accountInstance;
    private AdjustPanLayoutHelper adjustPanLayoutHelper;
    public boolean allowBlur;
    private boolean allowGifs;
    private boolean allowShowTopView;
    private boolean allowStickers;
    protected int animatedTop;
    private int animatingContentType;
    private HashMap<View, Float> animationParamsX;
    private ImageView attachButton;
    private LinearLayout attachLayout;
    private ImageView audioSendButton;
    private TLRPC$TL_document audioToSend;
    private MessageObject audioToSendMessageObject;
    private String audioToSendPath;
    private AnimatorSet audioVideoButtonAnimation;
    private FrameLayout audioVideoButtonContainer;
    Paint backgroundPaint;
    private ImageView botButton;
    private ReplaceableIconDrawable botButtonDrawable;
    private MessageObject botButtonsMessageObject;
    int botCommandLastPosition;
    int botCommandLastTop;
    private BotCommandsMenuView.BotCommandsAdapter botCommandsAdapter;
    private BotCommandsMenuView botCommandsMenuButton;
    public BotCommandsMenuContainer botCommandsMenuContainer;
    private int botCount;
    private BotKeyboardView botKeyboardView;
    private boolean botKeyboardViewVisible;
    private BotMenuButtonType botMenuButtonType;
    private String botMenuWebViewTitle;
    private String botMenuWebViewUrl;
    private MessageObject botMessageObject;
    private TLRPC$TL_replyKeyboardMarkup botReplyMarkup;
    private ChatActivityBotWebViewButton botWebViewButton;
    private BotWebViewMenuContainer botWebViewMenuContainer;
    private boolean calledRecordRunnable;
    private Drawable cameraDrawable;
    private Drawable cameraOutline;
    private boolean canWriteToChannel;
    private ImageView cancelBotButton;
    private NumberTextView captionLimitView;
    private float chatSearchExpandOffset;
    private boolean clearBotButtonsOnKeyboardOpen;
    private boolean closeAnimationInProgress;
    private int codePointCount;
    private float composeShadowAlpha;
    private boolean configAnimationsEnabled;
    private int currentAccount;
    private int currentEmojiIcon;
    private int currentLimit;
    private int currentPopupContentType;
    public ValueAnimator currentTopViewAnimation;
    private ChatActivityEnterViewDelegate delegate;
    private boolean destroyed;
    private long dialog_id;
    private float distCanMove;
    private AnimatorSet doneButtonAnimation;
    private ValueAnimator doneButtonColorAnimator;
    private FrameLayout doneButtonContainer;
    boolean doneButtonEnabled;
    private float doneButtonEnabledProgress;
    private ImageView doneButtonImage;
    private ContextProgressView doneButtonProgress;
    private final Drawable doneCheckDrawable;
    private Paint dotPaint;
    private CharSequence draftMessage;
    private boolean draftSearchWebpage;
    private boolean editingCaption;
    private MessageObject editingMessageObject;
    private ImageView[] emojiButton;
    private AnimatorSet emojiButtonAnimation;
    private int emojiPadding;
    private boolean emojiTabOpen;
    private EmojiView emojiView;
    private boolean emojiViewVisible;
    private ImageView expandStickersButton;
    private Runnable focusRunnable;
    private boolean forceShowSendButton;
    private boolean hasBotCommands;
    private boolean hasRecordVideo;
    private Runnable hideKeyboardRunnable;
    private boolean ignoreTextChange;
    private Drawable inactinveSendButtonDrawable;
    private TLRPC$ChatFull info;
    private int innerTextChange;
    private boolean isInitLineCount;
    private boolean isPaste;
    private boolean isPaused;
    private int keyboardHeight;
    private int keyboardHeightLand;
    private boolean keyboardVisible;
    private int lastSizeChangeValue1;
    private boolean lastSizeChangeValue2;
    private long lastTypingTimeSend;
    private int lineCount;
    private int[] location;
    private Drawable lockShadowDrawable;
    private View.AccessibilityDelegate mediaMessageButtonsDelegate;
    protected EditTextCaption messageEditText;
    boolean messageTransitionIsRunning;
    private TLRPC$WebPage messageWebPage;
    private boolean messageWebPageSearch;
    private Drawable micDrawable;
    private Drawable micOutline;
    private Runnable moveToSendStateRunnable;
    private boolean needShowTopView;
    private int notificationsIndex;
    private ImageView notifyButton;
    private CrossOutDrawable notifySilentDrawable;
    private Runnable onEmojiSearchClosed;
    private Runnable onFinishInitCameraRunnable;
    private Runnable onKeyboardClosed;
    private Runnable openKeyboardRunnable;
    private int originalViewHeight;
    private Paint paint;
    private AnimatorSet panelAnimation;
    private Activity parentActivity;
    private ChatActivity parentFragment;
    private RectF pauseRect;
    private TLRPC$KeyboardButton pendingLocationButton;
    private MessageObject pendingMessageObject;
    private MediaActionDrawable playPauseDrawable;
    private int popupX;
    private int popupY;
    public boolean preventInput;
    private CloseProgressDrawable2 progressDrawable;
    private Runnable recordAudioVideoRunnable;
    private boolean recordAudioVideoRunnableStarted;
    private RecordCircle recordCircle;
    private Property<RecordCircle, Float> recordCircleScale;
    private RLottieImageView recordDeleteImageView;
    private RecordDot recordDot;
    private int recordInterfaceState;
    private boolean recordIsCanceled;
    private FrameLayout recordPanel;
    private AnimatorSet recordPannelAnimation;
    private LinearLayout recordTimeContainer;
    private TimerView recordTimerView;
    private View recordedAudioBackground;
    private FrameLayout recordedAudioPanel;
    private ImageView recordedAudioPlayButton;
    private SeekBarWaveformView recordedAudioSeekBar;
    private TextView recordedAudioTimeTextView;
    private boolean recordingAudioVideo;
    private int recordingGuid;
    private android.graphics.Rect rect;
    private Paint redDotPaint;
    private boolean removeEmojiViewAfterAnimation;
    private MessageObject replyingMessageObject;
    private final Theme.ResourcesProvider resourcesProvider;
    private Property<View, Integer> roundedTranslationYProperty;
    private Runnable runEmojiPanelAnimation;
    private AnimatorSet runningAnimation;
    private AnimatorSet runningAnimation2;
    private AnimatorSet runningAnimationAudio;
    private int runningAnimationType;
    private boolean scheduleButtonHidden;
    private ImageView scheduledButton;
    private AnimatorSet scheduledButtonAnimation;
    private ValueAnimator searchAnimator;
    private float searchToOpenProgress;
    private int searchingType;
    private SeekBarWaveform seekBarWaveform;
    private View sendButton;
    private FrameLayout sendButtonContainer;
    private Drawable sendButtonDrawable;
    private Drawable sendButtonInverseDrawable;
    private boolean sendByEnter;
    private Drawable sendDrawable;
    private ActionBarPopupWindow.ActionBarPopupWindowLayout sendPopupLayout;
    private ActionBarPopupWindow sendPopupWindow;
    private android.graphics.Rect sendRect;
    private SenderSelectPopup senderSelectPopupWindow;
    private SenderSelectView senderSelectView;
    private Runnable setTextFieldRunnable;
    protected boolean shouldAnimateEditTextWithBounds;
    private boolean showKeyboardOnResume;
    private Runnable showTopViewRunnable;
    private boolean silent;
    private SizeNotifierFrameLayout sizeNotifierLayout;
    private SlideTextView slideText;
    private SimpleTextView slowModeButton;
    private int slowModeTimer;
    private boolean smoothKeyboard;
    private float startedDraggingX;
    private AnimatedArrowDrawable stickersArrow;
    private boolean stickersDragging;
    private boolean stickersExpanded;
    private int stickersExpandedHeight;
    private Animator stickersExpansionAnim;
    private float stickersExpansionProgress;
    private boolean stickersTabOpen;
    private FrameLayout textFieldContainer;
    boolean textTransitionIsRunning;
    protected View topLineView;
    protected View topView;
    protected float topViewEnterProgress;
    protected boolean topViewShowed;
    private final ValueAnimator.AnimatorUpdateListener topViewUpdateListener;
    private TrendingStickersAlert trendingStickersAlert;
    private Runnable updateExpandabilityRunnable;
    private Runnable updateSlowModeRunnable;
    private ImageView videoSendButton;
    private VideoTimelineView videoTimelineView;
    private VideoEditedInfo videoToSendMessageObject;
    private boolean waitingForKeyboardOpen;
    private boolean waitingForKeyboardOpenAfterAnimation;
    private PowerManager.WakeLock wakeLock;
    private boolean wasSendTyping;

    /* loaded from: classes3.dex */
    public enum BotMenuButtonType {
        NO_BUTTON,
        COMMANDS,
        WEB_VIEW
    }

    /* loaded from: classes3.dex */
    public interface ChatActivityEnterViewDelegate {

        /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$ChatActivityEnterViewDelegate$-CC */
        /* loaded from: classes3.dex */
        public final /* synthetic */ class CC {
            public static void $default$bottomPanelTranslationYChanged(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate, float f) {
            }

            public static int $default$getContentViewHeight(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return 0;
            }

            public static TLRPC$TL_channels_sendAsPeers $default$getSendAsPeers(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return null;
            }

            public static boolean $default$hasForwardingMessages(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return false;
            }

            public static boolean $default$hasScheduledMessages(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return true;
            }

            public static int $default$measureKeyboardHeight(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return 0;
            }

            public static void $default$onTrendingStickersShowed(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate, boolean z) {
            }

            public static void $default$openScheduledMessages(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
            }

            public static void $default$prepareMessageSending(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
            }

            public static void $default$scrollToSendingMessage(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
            }
        }

        void bottomPanelTranslationYChanged(float f);

        void didPressAttachButton();

        int getContentViewHeight();

        TLRPC$TL_channels_sendAsPeers getSendAsPeers();

        boolean hasForwardingMessages();

        boolean hasScheduledMessages();

        int measureKeyboardHeight();

        void needChangeVideoPreviewState(int i, float f);

        void needSendTyping();

        void needShowMediaBanHint();

        void needStartRecordAudio(int i);

        void needStartRecordVideo(int i, boolean z, int i2);

        void onAttachButtonHidden();

        void onAttachButtonShow();

        void onAudioVideoInterfaceUpdated();

        void onMessageEditEnd(boolean z);

        void onMessageSend(CharSequence charSequence, boolean z, int i);

        void onPreAudioVideoRecord();

        void onSendLongClick();

        void onStickersExpandedChange();

        void onStickersTab(boolean z);

        void onSwitchRecordMode(boolean z);

        void onTextChanged(CharSequence charSequence, boolean z);

        void onTextSelectionChanged(int i, int i2);

        void onTextSpansChanged(CharSequence charSequence);

        void onTrendingStickersShowed(boolean z);

        void onUpdateSlowModeButton(View view, boolean z, CharSequence charSequence);

        void onWindowSizeChanged(int i);

        void openScheduledMessages();

        void prepareMessageSending();

        void scrollToSendingMessage();
    }

    public static /* synthetic */ boolean lambda$new$17(View view, MotionEvent motionEvent) {
        return true;
    }

    public void checkAnimation() {
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    protected void onLineCountChanged(int i, int i2) {
    }

    protected boolean pannelAnimationEnabled() {
        return true;
    }

    /* loaded from: classes3.dex */
    public class SeekBarWaveformView extends View {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SeekBarWaveformView(Context context) {
            super(context);
            ChatActivityEnterView.this = r2;
            r2.seekBarWaveform = new SeekBarWaveform(context);
            r2.seekBarWaveform.setDelegate(new ChatActivityEnterView$SeekBarWaveformView$$ExternalSyntheticLambda0(this));
        }

        public /* synthetic */ void lambda$new$0(float f) {
            if (ChatActivityEnterView.this.audioToSendMessageObject != null) {
                ChatActivityEnterView.this.audioToSendMessageObject.audioProgress = f;
                MediaController.getInstance().seekToProgress(ChatActivityEnterView.this.audioToSendMessageObject, f);
            }
        }

        public void setWaveform(byte[] bArr) {
            ChatActivityEnterView.this.seekBarWaveform.setWaveform(bArr);
            invalidate();
        }

        public void setProgress(float f) {
            ChatActivityEnterView.this.seekBarWaveform.setProgress(f);
            invalidate();
        }

        public boolean isDragging() {
            return ChatActivityEnterView.this.seekBarWaveform.isDragging();
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean onTouch = ChatActivityEnterView.this.seekBarWaveform.onTouch(motionEvent.getAction(), motionEvent.getX(), motionEvent.getY());
            if (onTouch) {
                if (motionEvent.getAction() == 0) {
                    ChatActivityEnterView.this.requestDisallowInterceptTouchEvent(true);
                }
                invalidate();
            }
            return onTouch || super.onTouchEvent(motionEvent);
        }

        @Override // android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            ChatActivityEnterView.this.seekBarWaveform.setSize(i3 - i, i4 - i2);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            ChatActivityEnterView.this.seekBarWaveform.setColors(ChatActivityEnterView.this.getThemedColor("chat_recordedVoiceProgress"), ChatActivityEnterView.this.getThemedColor("chat_recordedVoiceProgressInner"), ChatActivityEnterView.this.getThemedColor("chat_recordedVoiceProgress"));
            ChatActivityEnterView.this.seekBarWaveform.draw(canvas, this);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends View.AccessibilityDelegate {
        AnonymousClass1(ChatActivityEnterView chatActivityEnterView) {
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName("android.widget.ImageButton");
            accessibilityNodeInfo.setClickable(true);
            accessibilityNodeInfo.setLongClickable(true);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
            ChatActivityEnterView.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if ((!ChatActivityEnterView.this.hasBotWebView() || !ChatActivityEnterView.this.botCommandsMenuIsShowing()) && !ChatActivityEnterView.this.destroyed) {
                ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                if (chatActivityEnterView.messageEditText == null || !chatActivityEnterView.waitingForKeyboardOpen || ChatActivityEnterView.this.keyboardVisible || AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow) {
                    return;
                }
                ChatActivityEnterView.this.messageEditText.requestFocus();
                AndroidUtilities.showKeyboard(ChatActivityEnterView.this.messageEditText);
                AndroidUtilities.cancelRunOnUIThread(ChatActivityEnterView.this.openKeyboardRunnable);
                AndroidUtilities.runOnUIThread(ChatActivityEnterView.this.openKeyboardRunnable, 100L);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements Runnable {
        private int lastKnownPage = -1;

        AnonymousClass3() {
            ChatActivityEnterView.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            int currentPage;
            if (ChatActivityEnterView.this.emojiView == null || (currentPage = ChatActivityEnterView.this.emojiView.getCurrentPage()) == this.lastKnownPage) {
                return;
            }
            this.lastKnownPage = currentPage;
            boolean z = ChatActivityEnterView.this.stickersTabOpen;
            int i = 2;
            ChatActivityEnterView.this.stickersTabOpen = currentPage == 1 || currentPage == 2;
            boolean z2 = ChatActivityEnterView.this.emojiTabOpen;
            ChatActivityEnterView.this.emojiTabOpen = currentPage == 0;
            if (ChatActivityEnterView.this.stickersExpanded) {
                if (ChatActivityEnterView.this.searchingType == 0) {
                    if (!ChatActivityEnterView.this.stickersTabOpen) {
                        ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                    }
                } else {
                    ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                    if (currentPage != 0) {
                        i = 1;
                    }
                    chatActivityEnterView.setSearchingTypeInternal(i, true);
                    ChatActivityEnterView.this.checkStickresExpandHeight();
                }
            }
            if (z == ChatActivityEnterView.this.stickersTabOpen && z2 == ChatActivityEnterView.this.emojiTabOpen) {
                return;
            }
            ChatActivityEnterView.this.checkSendButton(true);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends Property<View, Integer> {
        AnonymousClass4(ChatActivityEnterView chatActivityEnterView, Class cls, String str) {
            super(cls, str);
        }

        public Integer get(View view) {
            return Integer.valueOf(Math.round(view.getTranslationY()));
        }

        public void set(View view, Integer num) {
            view.setTranslationY(num.intValue());
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends Property<RecordCircle, Float> {
        AnonymousClass5(ChatActivityEnterView chatActivityEnterView, Class cls, String str) {
            super(cls, str);
        }

        public Float get(RecordCircle recordCircle) {
            return Float.valueOf(recordCircle.getScale());
        }

        public void set(RecordCircle recordCircle, Float f) {
            recordCircle.setScale(f.floatValue());
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 implements Runnable {
        AnonymousClass6() {
            ChatActivityEnterView.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.needStartRecordVideo(0, true, 0);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 implements Runnable {
        AnonymousClass7() {
            ChatActivityEnterView.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ChatActivityEnterView.this.delegate == null || ChatActivityEnterView.this.parentActivity == null) {
                return;
            }
            ChatActivityEnterView.this.delegate.onPreAudioVideoRecord();
            ChatActivityEnterView.this.calledRecordRunnable = true;
            ChatActivityEnterView.this.recordAudioVideoRunnableStarted = false;
            ChatActivityEnterView.this.slideText.setAlpha(1.0f);
            ChatActivityEnterView.this.slideText.setTranslationY(0.0f);
            if (ChatActivityEnterView.this.videoSendButton == null || ChatActivityEnterView.this.videoSendButton.getTag() == null) {
                if (ChatActivityEnterView.this.parentFragment == null || Build.VERSION.SDK_INT < 23 || ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                    ChatActivityEnterView.this.delegate.needStartRecordAudio(1);
                    ChatActivityEnterView.this.startedDraggingX = -1.0f;
                    MediaController.getInstance().startRecording(ChatActivityEnterView.this.currentAccount, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), ChatActivityEnterView.this.recordingGuid);
                    ChatActivityEnterView.this.recordingAudioVideo = true;
                    ChatActivityEnterView.this.updateRecordInterface(0);
                    ChatActivityEnterView.this.recordTimerView.start();
                    ChatActivityEnterView.this.recordDot.enterAnimation = false;
                    ChatActivityEnterView.this.audioVideoButtonContainer.getParent().requestDisallowInterceptTouchEvent(true);
                    ChatActivityEnterView.this.recordCircle.showWaves(true, false);
                    return;
                }
                ChatActivityEnterView.this.parentActivity.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 3);
                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                boolean z = ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.RECORD_AUDIO") == 0;
                boolean z2 = ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.CAMERA") == 0;
                if (!z || !z2) {
                    String[] strArr = new String[(z || z2) ? 1 : 2];
                    if (!z && !z2) {
                        strArr[0] = "android.permission.RECORD_AUDIO";
                        strArr[1] = "android.permission.CAMERA";
                    } else if (!z) {
                        strArr[0] = "android.permission.RECORD_AUDIO";
                    } else {
                        strArr[0] = "android.permission.CAMERA";
                    }
                    ChatActivityEnterView.this.parentActivity.requestPermissions(strArr, 150);
                    return;
                }
            }
            if (!CameraController.getInstance().isCameraInitied()) {
                CameraController.getInstance().initCamera(ChatActivityEnterView.this.onFinishInitCameraRunnable);
            } else {
                ChatActivityEnterView.this.onFinishInitCameraRunnable.run();
            }
            if (ChatActivityEnterView.this.recordingAudioVideo) {
                return;
            }
            ChatActivityEnterView.this.recordingAudioVideo = true;
            ChatActivityEnterView.this.updateRecordInterface(0);
            ChatActivityEnterView.this.recordCircle.showWaves(false, false);
            ChatActivityEnterView.this.recordTimerView.reset();
        }
    }

    /* loaded from: classes3.dex */
    public class RecordDot extends View {
        private float alpha;
        boolean attachedToWindow;
        RLottieDrawable drawable;
        private boolean enterAnimation;
        private boolean isIncr;
        private long lastUpdateTime;
        boolean playing;

        @Override // android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.attachedToWindow = true;
            if (this.playing) {
                this.drawable.start();
            }
            this.drawable.addParentView(this);
        }

        @Override // android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.attachedToWindow = false;
            this.drawable.stop();
            this.drawable.removeParentView(this);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public RecordDot(Context context) {
            super(context);
            ChatActivityEnterView.this = r8;
            RLottieDrawable rLottieDrawable = new RLottieDrawable(2131558418, "2131558418", AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), false, null);
            this.drawable = rLottieDrawable;
            rLottieDrawable.setCurrentParentView(this);
            this.drawable.setInvalidateOnProgressSet(true);
            updateColors();
        }

        public void updateColors() {
            int themedColor = ChatActivityEnterView.this.getThemedColor("chat_recordedVoiceDot");
            int themedColor2 = ChatActivityEnterView.this.getThemedColor("chat_messagePanelBackground");
            ChatActivityEnterView.this.redDotPaint.setColor(themedColor);
            this.drawable.beginApplyLayerColors();
            this.drawable.setLayerColor("Cup Red.**", themedColor);
            this.drawable.setLayerColor("Box.**", themedColor);
            this.drawable.setLayerColor("Line 1.**", themedColor2);
            this.drawable.setLayerColor("Line 2.**", themedColor2);
            this.drawable.setLayerColor("Line 3.**", themedColor2);
            this.drawable.commitApplyLayerColors();
            if (ChatActivityEnterView.this.playPauseDrawable != null) {
                ChatActivityEnterView.this.playPauseDrawable.setColor(ChatActivityEnterView.this.getThemedColor("chat_recordedVoicePlayPause"));
            }
        }

        public void resetAlpha() {
            this.alpha = 1.0f;
            this.lastUpdateTime = System.currentTimeMillis();
            this.isIncr = false;
            this.playing = false;
            this.drawable.stop();
            invalidate();
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            this.drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.playing) {
                this.drawable.setAlpha((int) (this.alpha * 255.0f));
            }
            ChatActivityEnterView.this.redDotPaint.setAlpha((int) (this.alpha * 255.0f));
            long currentTimeMillis = System.currentTimeMillis() - this.lastUpdateTime;
            if (this.enterAnimation) {
                this.alpha = 1.0f;
            } else if (!this.isIncr && !this.playing) {
                float f = this.alpha - (((float) currentTimeMillis) / 600.0f);
                this.alpha = f;
                if (f <= 0.0f) {
                    this.alpha = 0.0f;
                    this.isIncr = true;
                }
            } else {
                float f2 = this.alpha + (((float) currentTimeMillis) / 600.0f);
                this.alpha = f2;
                if (f2 >= 1.0f) {
                    this.alpha = 1.0f;
                    this.isIncr = false;
                }
            }
            this.lastUpdateTime = System.currentTimeMillis();
            if (this.playing) {
                this.drawable.draw(canvas);
            }
            if (!this.playing || !this.drawable.hasBitmap()) {
                canvas.drawCircle(getMeasuredWidth() >> 1, getMeasuredHeight() >> 1, AndroidUtilities.dp(5.0f), ChatActivityEnterView.this.redDotPaint);
            }
            invalidate();
        }

        public void playDeleteAnimation() {
            this.playing = true;
            this.drawable.setProgress(0.0f);
            if (this.attachedToWindow) {
                this.drawable.start();
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 implements Runnable {
        AnonymousClass8() {
            ChatActivityEnterView.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ChatActivityEnterView.this.panelAnimation == null || ChatActivityEnterView.this.panelAnimation.isRunning()) {
                return;
            }
            ChatActivityEnterView.this.panelAnimation.start();
        }
    }

    /* loaded from: classes3.dex */
    public class RecordCircle extends View {
        private float amplitude;
        private float animateAmplitudeDiff;
        private float animateToAmplitude;
        private boolean canceledByGesture;
        public float drawingCircleRadius;
        public float drawingCx;
        public float drawingCy;
        private float exitTransition;
        float idleProgress;
        boolean incIdle;
        private float lastMovingX;
        private float lastMovingY;
        private int lastSize;
        private long lastUpdateTime;
        private float lockAnimatedTranslation;
        private int paintAlpha;
        private boolean pressed;
        private float progressToSeekbarStep3;
        private float progressToSendButton;
        private float scale;
        private boolean sendButtonVisible;
        private boolean showTooltip;
        private long showTooltipStartTime;
        public boolean skipDraw;
        private int slideDelta;
        private float slideToCancelLockProgress;
        private float slideToCancelProgress;
        private float snapAnimationProgress;
        private float startTranslation;
        private float tooltipAlpha;
        private Drawable tooltipBackground;
        private Drawable tooltipBackgroundArrow;
        private StaticLayout tooltipLayout;
        private float tooltipWidth;
        private float touchSlop;
        private float transformToSeekbar;
        private VirtualViewHelper virtualViewHelper;
        public boolean voiceEnterTransitionInProgress;
        BlobDrawable tinyWaveDrawable = new BlobDrawable(11);
        BlobDrawable bigWaveDrawable = new BlobDrawable(12);
        private TextPaint tooltipPaint = new TextPaint(1);
        private float circleRadius = AndroidUtilities.dpf2(41.0f);
        private float circleRadiusAmplitude = AndroidUtilities.dp(30.0f);
        Paint lockBackgroundPaint = new Paint(1);
        Paint lockPaint = new Paint(1);
        Paint lockOutlinePaint = new Paint(1);
        RectF rectF = new RectF();
        Path path = new Path();
        private float wavesEnterAnimation = 0.0f;
        private boolean showWaves = true;
        private Paint p = new Paint(1);
        private String tooltipMessage = LocaleController.getString("SlideUpToLock", 2131628358);

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public RecordCircle(Context context) {
            super(context);
            ChatActivityEnterView.this = r6;
            r6.micDrawable = getResources().getDrawable(2131165538).mutate();
            r6.micDrawable.setColorFilter(new PorterDuffColorFilter(r6.getThemedColor("chat_messagePanelVoicePressed"), PorterDuff.Mode.MULTIPLY));
            r6.cameraDrawable = getResources().getDrawable(2131165546).mutate();
            r6.cameraDrawable.setColorFilter(new PorterDuffColorFilter(r6.getThemedColor("chat_messagePanelVoicePressed"), PorterDuff.Mode.MULTIPLY));
            r6.sendDrawable = getResources().getDrawable(2131165264).mutate();
            r6.sendDrawable.setColorFilter(new PorterDuffColorFilter(r6.getThemedColor("chat_messagePanelVoicePressed"), PorterDuff.Mode.MULTIPLY));
            r6.micOutline = getResources().getDrawable(2131165537).mutate();
            r6.micOutline.setColorFilter(new PorterDuffColorFilter(r6.getThemedColor("chat_messagePanelIcons"), PorterDuff.Mode.MULTIPLY));
            r6.cameraOutline = getResources().getDrawable(2131165545).mutate();
            r6.cameraOutline.setColorFilter(new PorterDuffColorFilter(r6.getThemedColor("chat_messagePanelIcons"), PorterDuff.Mode.MULTIPLY));
            VirtualViewHelper virtualViewHelper = new VirtualViewHelper(this);
            this.virtualViewHelper = virtualViewHelper;
            ViewCompat.setAccessibilityDelegate(this, virtualViewHelper);
            this.tinyWaveDrawable.minRadius = AndroidUtilities.dp(47.0f);
            this.tinyWaveDrawable.maxRadius = AndroidUtilities.dp(55.0f);
            this.tinyWaveDrawable.generateBlob();
            this.bigWaveDrawable.minRadius = AndroidUtilities.dp(47.0f);
            this.bigWaveDrawable.maxRadius = AndroidUtilities.dp(55.0f);
            this.bigWaveDrawable.generateBlob();
            this.lockOutlinePaint.setStyle(Paint.Style.STROKE);
            this.lockOutlinePaint.setStrokeCap(Paint.Cap.ROUND);
            this.lockOutlinePaint.setStrokeWidth(AndroidUtilities.dpf2(1.7f));
            r6.lockShadowDrawable = getResources().getDrawable(2131165589);
            r6.lockShadowDrawable.setColorFilter(new PorterDuffColorFilter(r6.getThemedColor("key_chat_messagePanelVoiceLockShadow"), PorterDuff.Mode.MULTIPLY));
            this.tooltipBackground = Theme.createRoundRectDrawable(AndroidUtilities.dp(5.0f), r6.getThemedColor("chat_gifSaveHintBackground"));
            this.tooltipPaint.setTextSize(AndroidUtilities.dp(14.0f));
            this.tooltipBackgroundArrow = ContextCompat.getDrawable(context, 2131166184);
            float scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
            this.touchSlop = scaledTouchSlop;
            this.touchSlop = scaledTouchSlop * scaledTouchSlop;
            updateColors();
        }

        public void setAmplitude(double d) {
            this.bigWaveDrawable.setValue((float) (Math.min(1800.0d, d) / 1800.0d), true);
            this.tinyWaveDrawable.setValue((float) (Math.min(1800.0d, d) / 1800.0d), false);
            float min = (float) (Math.min(1800.0d, d) / 1800.0d);
            this.animateToAmplitude = min;
            this.animateAmplitudeDiff = (min - this.amplitude) / 375.0f;
            invalidate();
        }

        public float getScale() {
            return this.scale;
        }

        @Keep
        public void setScale(float f) {
            this.scale = f;
            invalidate();
        }

        @Keep
        public void setLockAnimatedTranslation(float f) {
            this.lockAnimatedTranslation = f;
            invalidate();
        }

        @Keep
        public void setSnapAnimationProgress(float f) {
            this.snapAnimationProgress = f;
            invalidate();
        }

        @Keep
        public float getLockAnimatedTranslation() {
            return this.lockAnimatedTranslation;
        }

        public boolean isSendButtonVisible() {
            return this.sendButtonVisible;
        }

        public void setSendButtonInvisible() {
            this.sendButtonVisible = false;
            invalidate();
        }

        public int setLockTranslation(float f) {
            if (f == 10000.0f) {
                this.sendButtonVisible = false;
                this.lockAnimatedTranslation = -1.0f;
                this.startTranslation = -1.0f;
                invalidate();
                this.snapAnimationProgress = 0.0f;
                this.transformToSeekbar = 0.0f;
                this.exitTransition = 0.0f;
                this.scale = 0.0f;
                this.tooltipAlpha = 0.0f;
                this.showTooltip = false;
                this.progressToSendButton = 0.0f;
                this.slideToCancelProgress = 1.0f;
                this.slideToCancelLockProgress = 1.0f;
                this.canceledByGesture = false;
                return 0;
            } else if (this.sendButtonVisible) {
                return 2;
            } else {
                if (this.lockAnimatedTranslation == -1.0f) {
                    this.startTranslation = f;
                }
                this.lockAnimatedTranslation = f;
                invalidate();
                if (this.canceledByGesture || this.slideToCancelProgress < 0.7f || this.startTranslation - this.lockAnimatedTranslation < AndroidUtilities.dp(57.0f)) {
                    return 1;
                }
                this.sendButtonVisible = true;
                return 2;
            }
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (this.sendButtonVisible) {
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                if (motionEvent.getAction() == 0) {
                    boolean contains = ChatActivityEnterView.this.pauseRect.contains(x, y);
                    this.pressed = contains;
                    return contains;
                } else if (this.pressed) {
                    if (motionEvent.getAction() == 1 && ChatActivityEnterView.this.pauseRect.contains(x, y)) {
                        if (ChatActivityEnterView.this.videoSendButton != null && ChatActivityEnterView.this.videoSendButton.getTag() != null) {
                            ChatActivityEnterView.this.delegate.needStartRecordVideo(3, true, 0);
                        } else {
                            MediaController.getInstance().stopRecording(2, true, 0);
                            ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                        }
                        ChatActivityEnterView.this.slideText.setEnabled(false);
                    }
                    return true;
                }
            }
            return false;
        }

        @Override // android.view.View
        @SuppressLint({"DrawAllocation"})
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            int dp = AndroidUtilities.dp(194.0f);
            if (this.lastSize != size) {
                this.lastSize = size;
                StaticLayout staticLayout = new StaticLayout(this.tooltipMessage, this.tooltipPaint, AndroidUtilities.dp(220.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
                this.tooltipLayout = staticLayout;
                int lineCount = staticLayout.getLineCount();
                this.tooltipWidth = 0.0f;
                for (int i3 = 0; i3 < lineCount; i3++) {
                    float lineWidth = this.tooltipLayout.getLineWidth(i3);
                    if (lineWidth > this.tooltipWidth) {
                        this.tooltipWidth = lineWidth;
                    }
                }
            }
            StaticLayout staticLayout2 = this.tooltipLayout;
            if (staticLayout2 != null && staticLayout2.getLineCount() > 1) {
                dp += this.tooltipLayout.getHeight() - this.tooltipLayout.getLineBottom(0);
            }
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(dp, 1073741824));
            float measuredWidth = getMeasuredWidth() * 0.35f;
            if (measuredWidth > AndroidUtilities.dp(140.0f)) {
                measuredWidth = AndroidUtilities.dp(140.0f);
            }
            this.slideDelta = (int) ((-measuredWidth) * (1.0f - this.slideToCancelProgress));
        }

        /* JADX WARN: Removed duplicated region for block: B:159:0x057a  */
        /* JADX WARN: Removed duplicated region for block: B:164:0x05de  */
        /* JADX WARN: Removed duplicated region for block: B:167:0x0657  */
        /* JADX WARN: Removed duplicated region for block: B:170:0x066a  */
        /* JADX WARN: Removed duplicated region for block: B:175:0x067a  */
        /* JADX WARN: Removed duplicated region for block: B:184:0x0693  */
        /* JADX WARN: Removed duplicated region for block: B:189:0x06b1  */
        /* JADX WARN: Removed duplicated region for block: B:194:0x06de  */
        /* JADX WARN: Removed duplicated region for block: B:195:0x0805  */
        /* JADX WARN: Removed duplicated region for block: B:199:0x0830  */
        /* JADX WARN: Removed duplicated region for block: B:200:0x0835  */
        /* JADX WARN: Removed duplicated region for block: B:209:0x0849  */
        /* JADX WARN: Removed duplicated region for block: B:218:0x086d  */
        /* JADX WARN: Removed duplicated region for block: B:223:0x089a  */
        /* JADX WARN: Removed duplicated region for block: B:226:0x0995  */
        /* JADX WARN: Removed duplicated region for block: B:229:0x09a4  */
        /* JADX WARN: Removed duplicated region for block: B:235:0x0a7a  */
        /* JADX WARN: Removed duplicated region for block: B:240:0x0ab6  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            float f;
            float f2;
            float f3;
            float f4;
            float f5;
            float f6;
            float f7;
            Drawable drawable;
            float f8;
            long j;
            float f9;
            Drawable drawable2;
            Drawable drawable3;
            float f10;
            float f11;
            float f12;
            float f13;
            float f14;
            float f15;
            float f16;
            float f17;
            Drawable drawable4;
            float f18;
            Drawable drawable5;
            float dpf2;
            float dpf22;
            float f19;
            float f20;
            float f21;
            Drawable drawable6;
            if (this.skipDraw) {
                return;
            }
            StaticLayout staticLayout = this.tooltipLayout;
            float height = (staticLayout == null || staticLayout.getLineCount() <= 1) ? 0.0f : this.tooltipLayout.getHeight() - this.tooltipLayout.getLineBottom(0);
            int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp2(26.0f);
            int dp = (int) (AndroidUtilities.dp(170.0f) + height);
            this.drawingCx = this.slideDelta + measuredWidth;
            float f22 = dp;
            this.drawingCy = f22;
            float f23 = this.lockAnimatedTranslation;
            if (f23 != 10000.0f) {
                float max = Math.max(0, (int) (this.startTranslation - f23));
                if (max > AndroidUtilities.dp(57.0f)) {
                    max = AndroidUtilities.dp(57.0f);
                }
                f = max;
            } else {
                f = 0.0f;
            }
            float f24 = this.scale;
            float f25 = 1.0f;
            float f26 = f24 <= 0.5f ? f24 / 0.5f : f24 <= 0.75f ? 1.0f - (((f24 - 0.5f) / 0.25f) * 0.1f) : (((f24 - 0.75f) / 0.25f) * 0.1f) + 0.9f;
            long currentTimeMillis = System.currentTimeMillis() - this.lastUpdateTime;
            float f27 = this.animateToAmplitude;
            float f28 = this.amplitude;
            if (f27 != f28) {
                float f29 = this.animateAmplitudeDiff;
                float f30 = f28 + (((float) currentTimeMillis) * f29);
                this.amplitude = f30;
                if (f29 > 0.0f) {
                    if (f30 > f27) {
                        this.amplitude = f27;
                    }
                } else if (f30 < f27) {
                    this.amplitude = f27;
                }
                invalidate();
            }
            if (this.canceledByGesture) {
                f2 = CubicBezierInterpolator.EASE_OUT.getInterpolation(1.0f - this.slideToCancelProgress) * 0.7f;
            } else {
                f2 = (this.slideToCancelProgress * 0.3f) + 0.7f;
            }
            float f31 = (this.circleRadius + (this.circleRadiusAmplitude * this.amplitude)) * f26 * f2;
            this.progressToSeekbarStep3 = 0.0f;
            float f32 = this.transformToSeekbar;
            if (f32 != 0.0f) {
                if (f32 <= 0.38f) {
                    f25 = f32 / 0.38f;
                }
                float max2 = f32 > 0.63f ? 1.0f : Math.max(0.0f, (f32 - 0.38f) / 0.25f);
                this.progressToSeekbarStep3 = Math.max(0.0f, ((this.transformToSeekbar - 0.38f) - 0.25f) / 0.37f);
                CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_BOTH;
                float interpolation = cubicBezierInterpolator.getInterpolation(f25);
                float interpolation2 = cubicBezierInterpolator.getInterpolation(max2);
                this.progressToSeekbarStep3 = cubicBezierInterpolator.getInterpolation(this.progressToSeekbarStep3);
                float measuredHeight = ChatActivityEnterView.this.recordedAudioBackground.getMeasuredHeight() / 2.0f;
                f5 = interpolation2;
                f6 = (((f31 + (AndroidUtilities.dp(16.0f) * interpolation)) - measuredHeight) * (1.0f - interpolation2)) + measuredHeight;
                f3 = interpolation;
                f7 = 1.0f;
                f4 = 0.0f;
            } else {
                float f33 = this.exitTransition;
                if (f33 != 0.0f) {
                    float f34 = f33 > 0.6f ? 1.0f : f33 / 0.6f;
                    if (!ChatActivityEnterView.this.messageTransitionIsRunning) {
                        f33 = Math.max(0.0f, (f33 - 0.6f) / 0.4f);
                    }
                    CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.EASE_BOTH;
                    float interpolation3 = cubicBezierInterpolator2.getInterpolation(f34);
                    float interpolation4 = cubicBezierInterpolator2.getInterpolation(f33);
                    float dp2 = (f31 + (AndroidUtilities.dp(16.0f) * interpolation3)) * (1.0f - interpolation4);
                    if (ChatActivityEnterView.this.configAnimationsEnabled) {
                        float f35 = this.exitTransition;
                        if (f35 > 0.6f) {
                            f7 = Math.max(0.0f, 1.0f - ((f35 - 0.6f) / 0.4f));
                            f4 = interpolation4;
                            f6 = dp2;
                            f3 = interpolation3;
                            f5 = 0.0f;
                        }
                    }
                    f4 = interpolation4;
                    f6 = dp2;
                    f3 = interpolation3;
                    f7 = 1.0f;
                    f5 = 0.0f;
                } else {
                    f6 = f31;
                    f7 = 1.0f;
                    f5 = 0.0f;
                    f4 = 0.0f;
                    f3 = 0.0f;
                }
            }
            if (this.canceledByGesture) {
                float f36 = this.slideToCancelProgress;
                if (f36 > 0.7f) {
                    f7 *= 1.0f - ((f36 - 0.7f) / 0.3f);
                }
            }
            if (this.progressToSeekbarStep3 > 0.0f) {
                ChatActivityEnterView.this.paint.setColor(ColorUtils.blendARGB(ChatActivityEnterView.this.getThemedColor("chat_messagePanelVoiceBackground"), ChatActivityEnterView.this.getThemedColor("chat_recordedVoiceBackground"), this.progressToSeekbarStep3));
            } else {
                ChatActivityEnterView.this.paint.setColor(ChatActivityEnterView.this.getThemedColor("chat_messagePanelVoiceBackground"));
            }
            Drawable drawable7 = null;
            if (!isSendButtonVisible()) {
                drawable = (ChatActivityEnterView.this.videoSendButton == null || ChatActivityEnterView.this.videoSendButton.getTag() == null) ? ChatActivityEnterView.this.micDrawable : ChatActivityEnterView.this.cameraDrawable;
            } else {
                float f37 = this.progressToSendButton;
                if (f37 != 1.0f) {
                    float f38 = f37 + (((float) currentTimeMillis) / 150.0f);
                    this.progressToSendButton = f38;
                    if (f38 > 1.0f) {
                        this.progressToSendButton = 1.0f;
                    }
                    drawable7 = (ChatActivityEnterView.this.videoSendButton == null || ChatActivityEnterView.this.videoSendButton.getTag() == null) ? ChatActivityEnterView.this.micDrawable : ChatActivityEnterView.this.cameraDrawable;
                }
                drawable = ChatActivityEnterView.this.sendDrawable;
            }
            Drawable drawable8 = drawable7;
            Drawable drawable9 = drawable;
            float f39 = height;
            float f40 = f6;
            float f41 = f7;
            ChatActivityEnterView.this.sendRect.set(measuredWidth - (drawable9.getIntrinsicWidth() / 2), dp - (drawable9.getIntrinsicHeight() / 2), measuredWidth + (drawable9.getIntrinsicWidth() / 2), dp + (drawable9.getIntrinsicHeight() / 2));
            drawable9.setBounds(ChatActivityEnterView.this.sendRect);
            if (drawable8 != null) {
                drawable8.setBounds(measuredWidth - (drawable8.getIntrinsicWidth() / 2), dp - (drawable8.getIntrinsicHeight() / 2), (drawable8.getIntrinsicWidth() / 2) + measuredWidth, dp + (drawable8.getIntrinsicHeight() / 2));
            }
            float dp3 = 1.0f - (f / AndroidUtilities.dp(57.0f));
            if (this.incIdle) {
                float f42 = this.idleProgress + 0.01f;
                this.idleProgress = f42;
                if (f42 > 1.0f) {
                    this.incIdle = false;
                    this.idleProgress = 1.0f;
                }
            } else {
                float f43 = this.idleProgress - 0.01f;
                this.idleProgress = f43;
                if (f43 < 0.0f) {
                    this.incIdle = true;
                    this.idleProgress = 0.0f;
                }
            }
            if (ChatActivityEnterView.this.configAnimationsEnabled) {
                this.tinyWaveDrawable.minRadius = AndroidUtilities.dp(47.0f);
                this.tinyWaveDrawable.maxRadius = AndroidUtilities.dp(47.0f) + (AndroidUtilities.dp(15.0f) * BlobDrawable.FORM_SMALL_MAX);
                this.bigWaveDrawable.minRadius = AndroidUtilities.dp(50.0f);
                this.bigWaveDrawable.maxRadius = AndroidUtilities.dp(50.0f) + (AndroidUtilities.dp(12.0f) * BlobDrawable.FORM_BIG_MAX);
                this.bigWaveDrawable.updateAmplitude(currentTimeMillis);
                BlobDrawable blobDrawable = this.bigWaveDrawable;
                blobDrawable.update(blobDrawable.amplitude, 1.01f);
                this.tinyWaveDrawable.updateAmplitude(currentTimeMillis);
                BlobDrawable blobDrawable2 = this.tinyWaveDrawable;
                blobDrawable2.update(blobDrawable2.amplitude, 1.02f);
            }
            this.lastUpdateTime = System.currentTimeMillis();
            float f44 = this.slideToCancelProgress;
            float f45 = f44 > 0.7f ? 1.0f : f44 / 0.7f;
            if (ChatActivityEnterView.this.configAnimationsEnabled && f5 != 1.0f && f4 < 0.4f && f45 > 0.0f && !this.canceledByGesture) {
                if (this.showWaves) {
                    float f46 = this.wavesEnterAnimation;
                    if (f46 != 1.0f) {
                        float f47 = f46 + 0.04f;
                        this.wavesEnterAnimation = f47;
                        if (f47 > 1.0f) {
                            this.wavesEnterAnimation = 1.0f;
                        }
                    }
                }
                if (!this.voiceEnterTransitionInProgress) {
                    float interpolation5 = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.wavesEnterAnimation);
                    canvas.save();
                    float f48 = 1.0f - f3;
                    float f49 = this.scale * f48 * f45 * interpolation5 * (BlobDrawable.SCALE_BIG_MIN + (this.bigWaveDrawable.amplitude * 1.4f));
                    canvas.scale(f49, f49, this.slideDelta + measuredWidth, f22);
                    BlobDrawable blobDrawable3 = this.bigWaveDrawable;
                    blobDrawable3.draw(this.slideDelta + measuredWidth, f22, canvas, blobDrawable3.paint);
                    canvas.restore();
                    float f50 = this.scale * f48 * f45 * interpolation5 * (BlobDrawable.SCALE_SMALL_MIN + (this.tinyWaveDrawable.amplitude * 1.4f));
                    canvas.save();
                    canvas.scale(f50, f50, this.slideDelta + measuredWidth, f22);
                    BlobDrawable blobDrawable4 = this.tinyWaveDrawable;
                    blobDrawable4.draw(this.slideDelta + measuredWidth, f22, canvas, blobDrawable4.paint);
                    canvas.restore();
                }
            }
            if (!this.voiceEnterTransitionInProgress) {
                ChatActivityEnterView.this.paint.setAlpha((int) (this.paintAlpha * f41));
                if (this.scale == 1.0f) {
                    if (this.transformToSeekbar == 0.0f) {
                        drawable6 = drawable8;
                        j = currentTimeMillis;
                        f8 = dp3;
                        f9 = f;
                        f10 = f40;
                        canvas.drawCircle(this.slideDelta + measuredWidth, f22, f10, ChatActivityEnterView.this.paint);
                    } else if (this.progressToSeekbarStep3 <= 0.0f) {
                        drawable6 = drawable8;
                        j = currentTimeMillis;
                        f8 = dp3;
                        f9 = f;
                        f10 = f40;
                        canvas.drawCircle(this.slideDelta + measuredWidth, f22, f10, ChatActivityEnterView.this.paint);
                    } else {
                        float f51 = f22 + f40;
                        float f52 = f22 - f40;
                        int i = this.slideDelta;
                        float f53 = measuredWidth + i + f40;
                        float f54 = (i + measuredWidth) - f40;
                        View view = ChatActivityEnterView.this.recordedAudioBackground;
                        drawable6 = drawable8;
                        j = currentTimeMillis;
                        int i2 = 0;
                        int i3 = 0;
                        for (View view2 = (View) view.getParent(); view2 != getParent(); view2 = (View) view2.getParent()) {
                            i2 += view2.getTop();
                            i3 += view2.getLeft();
                        }
                        int top = (view.getTop() + i2) - getTop();
                        int bottom = (view.getBottom() + i2) - getTop();
                        int right = (view.getRight() + i3) - getLeft();
                        f8 = dp3;
                        int left = (view.getLeft() + i3) - getLeft();
                        f9 = f;
                        float measuredHeight2 = ChatActivityEnterView.this.isInVideoMode() ? 0.0f : view.getMeasuredHeight() / 2.0f;
                        float f55 = top;
                        float f56 = this.progressToSeekbarStep3;
                        float f57 = f55 + ((f52 - f55) * (1.0f - f56));
                        float f58 = bottom;
                        float f59 = f58 + ((f51 - f58) * (1.0f - f56));
                        float f60 = left;
                        float f61 = f60 + ((f54 - f60) * (1.0f - f56));
                        float f62 = right;
                        float f63 = measuredHeight2 + ((f40 - measuredHeight2) * (1.0f - f56));
                        this.rectF.set(f61, f57, f62 + ((f53 - f62) * (1.0f - f56)), f59);
                        canvas.drawRoundRect(this.rectF, f63, f63, ChatActivityEnterView.this.paint);
                        f10 = f40;
                    }
                    canvas.save();
                    canvas.translate(this.slideDelta, 0.0f);
                    drawable3 = drawable6;
                    drawable2 = drawable9;
                    drawIconInternal(canvas, drawable9, drawable3, this.progressToSendButton, (int) ((1.0f - f5) * (1.0f - f4) * 255.0f));
                    canvas.restore();
                    if (!isSendButtonVisible()) {
                        f16 = AndroidUtilities.dp(36.0f);
                        f15 = (((AndroidUtilities.dp(60.0f) + f39) + (AndroidUtilities.dpf2(30.0f) * (1.0f - f26))) - f9) + (AndroidUtilities.dpf2(14.0f) * f8);
                        float f64 = (f16 / 2.0f) + f15;
                        f13 = (f64 - AndroidUtilities.dpf2(8.0f)) + AndroidUtilities.dpf2(2.0f);
                        f14 = (f64 - AndroidUtilities.dpf2(16.0f)) + AndroidUtilities.dpf2(2.0f);
                        float f65 = f8 > 0.4f ? 1.0f : f8 / 0.4f;
                        float f66 = this.snapAnimationProgress;
                        f12 = (((1.0f - f8) * 9.0f) * (1.0f - f66)) - ((f66 * 15.0f) * (1.0f - f65));
                        f11 = f8;
                    } else {
                        f16 = AndroidUtilities.dp(36.0f) + ((int) (AndroidUtilities.dp(14.0f) * f8));
                        f15 = (((AndroidUtilities.dp(60.0f) + f39) + ((int) (AndroidUtilities.dp(30.0f) * (1.0f - f26)))) - ((int) f9)) + (this.idleProgress * f8 * (-AndroidUtilities.dp(8.0f)));
                        float f67 = (f16 / 2.0f) + f15;
                        f13 = (f67 - AndroidUtilities.dpf2(8.0f)) + AndroidUtilities.dpf2(2.0f) + (AndroidUtilities.dpf2(2.0f) * f8);
                        f14 = (f67 - AndroidUtilities.dpf2(16.0f)) + AndroidUtilities.dpf2(2.0f) + (AndroidUtilities.dpf2(2.0f) * f8);
                        this.snapAnimationProgress = 0.0f;
                        f12 = (1.0f - f8) * 9.0f;
                        f11 = 0.0f;
                    }
                    if (!this.showTooltip) {
                        f17 = f15;
                        if (System.currentTimeMillis() - this.showTooltipStartTime > 200) {
                            f21 = 0.0f;
                            if (f8 >= 0.8f || isSendButtonVisible() || this.exitTransition != f21 || this.transformToSeekbar != f21) {
                                this.showTooltip = false;
                            }
                            if (this.showTooltip) {
                                float f68 = this.tooltipAlpha;
                                f18 = f14;
                                if (f68 != 1.0f) {
                                    float f69 = f68 + (((float) j) / 150.0f);
                                    this.tooltipAlpha = f69;
                                    if (f69 >= 1.0f) {
                                        this.tooltipAlpha = 1.0f;
                                        SharedConfig.increaseLockRecordAudioVideoHintShowed();
                                    }
                                }
                            } else {
                                f18 = f14;
                                float f70 = this.tooltipAlpha - (((float) j) / 150.0f);
                                this.tooltipAlpha = f70;
                                if (f70 < 0.0f) {
                                    this.tooltipAlpha = 0.0f;
                                }
                            }
                            int i4 = (int) (this.tooltipAlpha * 255.0f);
                            this.tooltipBackground.setAlpha(i4);
                            this.tooltipBackgroundArrow.setAlpha(i4);
                            this.tooltipPaint.setAlpha(i4);
                            if (this.tooltipLayout != null) {
                                canvas.save();
                                drawable4 = drawable3;
                                this.rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                                canvas.translate((getMeasuredWidth() - this.tooltipWidth) - AndroidUtilities.dp(44.0f), AndroidUtilities.dpf2(16.0f));
                                drawable5 = drawable2;
                                this.tooltipBackground.setBounds(-AndroidUtilities.dp(8.0f), -AndroidUtilities.dp(2.0f), (int) (this.tooltipWidth + AndroidUtilities.dp(36.0f)), (int) (this.tooltipLayout.getHeight() + AndroidUtilities.dpf2(4.0f)));
                                this.tooltipBackground.draw(canvas);
                                this.tooltipLayout.draw(canvas);
                                canvas.restore();
                                canvas.save();
                                canvas.translate(measuredWidth, (AndroidUtilities.dpf2(17.0f) + (this.tooltipLayout.getHeight() / 2.0f)) - (this.idleProgress * AndroidUtilities.dpf2(3.0f)));
                                this.path.reset();
                                this.path.setLastPoint(-AndroidUtilities.dpf2(5.0f), AndroidUtilities.dpf2(4.0f));
                                this.path.lineTo(0.0f, 0.0f);
                                this.path.lineTo(AndroidUtilities.dpf2(5.0f), AndroidUtilities.dpf2(4.0f));
                                this.p.setColor(-1);
                                this.p.setAlpha(i4);
                                this.p.setStyle(Paint.Style.STROKE);
                                this.p.setStrokeCap(Paint.Cap.ROUND);
                                this.p.setStrokeJoin(Paint.Join.ROUND);
                                this.p.setStrokeWidth(AndroidUtilities.dpf2(1.5f));
                                canvas.drawPath(this.path, this.p);
                                canvas.restore();
                                canvas.save();
                                Drawable drawable10 = this.tooltipBackgroundArrow;
                                drawable10.setBounds(measuredWidth - (drawable10.getIntrinsicWidth() / 2), (int) (this.tooltipLayout.getHeight() + AndroidUtilities.dpf2(20.0f)), (this.tooltipBackgroundArrow.getIntrinsicWidth() / 2) + measuredWidth, ((int) (this.tooltipLayout.getHeight() + AndroidUtilities.dpf2(20.0f))) + this.tooltipBackgroundArrow.getIntrinsicHeight());
                                this.tooltipBackgroundArrow.draw(canvas);
                                canvas.restore();
                                canvas.save();
                                canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - ChatActivityEnterView.this.textFieldContainer.getMeasuredHeight());
                                float f71 = this.scale;
                                float f72 = 1.0f - f71 != 0.0f ? 1.0f - f71 : f5 != 0.0f ? f5 : f4 != 0.0f ? f4 : 0.0f;
                                if (this.slideToCancelProgress >= 0.7f || this.canceledByGesture) {
                                    this.showTooltip = false;
                                    f20 = this.slideToCancelLockProgress;
                                    if (f20 != 0.0f) {
                                        float f73 = f20 - 0.12f;
                                        this.slideToCancelLockProgress = f73;
                                        if (f73 < 0.0f) {
                                            this.slideToCancelLockProgress = 0.0f;
                                        }
                                    }
                                } else {
                                    float f74 = this.slideToCancelLockProgress;
                                    if (f74 != 1.0f) {
                                        float f75 = f74 + 0.12f;
                                        this.slideToCancelLockProgress = f75;
                                        if (f75 > 1.0f) {
                                            this.slideToCancelLockProgress = 1.0f;
                                        }
                                    }
                                }
                                dpf2 = AndroidUtilities.dpf2(72.0f);
                                dpf22 = ((dpf2 * f72) - ((AndroidUtilities.dpf2(20.0f) * f3) * (1.0f - f72))) + ((1.0f - this.slideToCancelLockProgress) * dpf2);
                                if (dpf22 <= dpf2) {
                                    dpf2 = dpf22;
                                }
                                canvas.translate(0.0f, dpf2);
                                float f76 = this.scale * (1.0f - f5) * (1.0f - f4) * this.slideToCancelLockProgress;
                                float f77 = measuredWidth;
                                canvas.scale(f76, f76, f77, f13);
                                this.rectF.set(f77 - AndroidUtilities.dpf2(18.0f), f17, AndroidUtilities.dpf2(18.0f) + f77, f17 + f16);
                                ChatActivityEnterView.this.lockShadowDrawable.setBounds((int) (this.rectF.left - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.top - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.right + AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.bottom + AndroidUtilities.dpf2(3.0f)));
                                ChatActivityEnterView.this.lockShadowDrawable.draw(canvas);
                                canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(18.0f), AndroidUtilities.dpf2(18.0f), this.lockBackgroundPaint);
                                ChatActivityEnterView.this.pauseRect.set(this.rectF);
                                float f78 = 1.0f - f11;
                                float f79 = f10;
                                this.rectF.set((f77 - AndroidUtilities.dpf2(6.0f)) - (AndroidUtilities.dpf2(2.0f) * f78), f13 - (AndroidUtilities.dpf2(2.0f) * f78), AndroidUtilities.dp(6.0f) + measuredWidth + (AndroidUtilities.dpf2(2.0f) * f78), f13 + AndroidUtilities.dp(12.0f) + (AndroidUtilities.dpf2(2.0f) * f78));
                                RectF rectF = this.rectF;
                                float f80 = rectF.bottom;
                                float centerX = rectF.centerX();
                                float centerY = this.rectF.centerY();
                                canvas.save();
                                float f81 = 1.0f - f8;
                                canvas.translate(0.0f, AndroidUtilities.dpf2(2.0f) * f81);
                                canvas.rotate(f12, centerX, centerY);
                                canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(3.0f), AndroidUtilities.dpf2(3.0f), this.lockPaint);
                                if (f11 != 1.0f) {
                                    canvas.drawCircle(centerX, centerY, AndroidUtilities.dpf2(2.0f) * f78, this.lockBackgroundPaint);
                                }
                                if (f11 != 1.0f) {
                                    this.rectF.set(0.0f, 0.0f, AndroidUtilities.dpf2(8.0f), AndroidUtilities.dpf2(8.0f));
                                    canvas.save();
                                    canvas.clipRect(0.0f, 0.0f, getMeasuredWidth(), dpf2 + f80 + (AndroidUtilities.dpf2(2.0f) * f81));
                                    canvas.translate(f77 - AndroidUtilities.dpf2(4.0f), ((f18 - ((AndroidUtilities.dpf2(1.5f) * (1.0f - this.idleProgress)) * f8)) - (AndroidUtilities.dpf2(2.0f) * f81)) + (AndroidUtilities.dpf2(12.0f) * f11) + (AndroidUtilities.dpf2(2.0f) * this.snapAnimationProgress));
                                    if (f12 > 0.0f) {
                                        canvas.rotate(f12, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                                    }
                                    canvas.drawLine(AndroidUtilities.dpf2(8.0f), AndroidUtilities.dpf2(4.0f), AndroidUtilities.dpf2(8.0f), (AndroidUtilities.dpf2(4.0f) * f78) + AndroidUtilities.dpf2(6.0f), this.lockOutlinePaint);
                                    canvas.drawArc(this.rectF, 0.0f, -180.0f, false, this.lockOutlinePaint);
                                    canvas.drawLine(0.0f, AndroidUtilities.dpf2(4.0f), 0.0f, (AndroidUtilities.dpf2(4.0f) * this.snapAnimationProgress * f81) + AndroidUtilities.dpf2(4.0f) + (AndroidUtilities.dpf2(4.0f) * this.idleProgress * f8 * (!isSendButtonVisible())), this.lockOutlinePaint);
                                    canvas.restore();
                                }
                                canvas.restore();
                                canvas.restore();
                                if (this.scale != 1.0f) {
                                    f19 = f79;
                                    canvas.drawCircle(measuredWidth + this.slideDelta, f22, f19, ChatActivityEnterView.this.paint);
                                    float f82 = this.canceledByGesture ? 1.0f - this.slideToCancelProgress : 1.0f;
                                    canvas.save();
                                    canvas.translate(this.slideDelta, 0.0f);
                                    drawIconInternal(canvas, drawable5, drawable4, this.progressToSendButton, (int) (f82 * 255.0f));
                                    canvas.restore();
                                } else {
                                    f19 = f79;
                                }
                                this.drawingCircleRadius = f19;
                            }
                            drawable4 = drawable3;
                            drawable5 = drawable2;
                            canvas.save();
                            canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - ChatActivityEnterView.this.textFieldContainer.getMeasuredHeight());
                            float f712 = this.scale;
                            if (1.0f - f712 != 0.0f) {
                            }
                            if (this.slideToCancelProgress >= 0.7f) {
                            }
                            this.showTooltip = false;
                            f20 = this.slideToCancelLockProgress;
                            if (f20 != 0.0f) {
                            }
                            dpf2 = AndroidUtilities.dpf2(72.0f);
                            dpf22 = ((dpf2 * f72) - ((AndroidUtilities.dpf2(20.0f) * f3) * (1.0f - f72))) + ((1.0f - this.slideToCancelLockProgress) * dpf2);
                            if (dpf22 <= dpf2) {
                            }
                            canvas.translate(0.0f, dpf2);
                            float f762 = this.scale * (1.0f - f5) * (1.0f - f4) * this.slideToCancelLockProgress;
                            float f772 = measuredWidth;
                            canvas.scale(f762, f762, f772, f13);
                            this.rectF.set(f772 - AndroidUtilities.dpf2(18.0f), f17, AndroidUtilities.dpf2(18.0f) + f772, f17 + f16);
                            ChatActivityEnterView.this.lockShadowDrawable.setBounds((int) (this.rectF.left - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.top - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.right + AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.bottom + AndroidUtilities.dpf2(3.0f)));
                            ChatActivityEnterView.this.lockShadowDrawable.draw(canvas);
                            canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(18.0f), AndroidUtilities.dpf2(18.0f), this.lockBackgroundPaint);
                            ChatActivityEnterView.this.pauseRect.set(this.rectF);
                            float f782 = 1.0f - f11;
                            float f792 = f10;
                            this.rectF.set((f772 - AndroidUtilities.dpf2(6.0f)) - (AndroidUtilities.dpf2(2.0f) * f782), f13 - (AndroidUtilities.dpf2(2.0f) * f782), AndroidUtilities.dp(6.0f) + measuredWidth + (AndroidUtilities.dpf2(2.0f) * f782), f13 + AndroidUtilities.dp(12.0f) + (AndroidUtilities.dpf2(2.0f) * f782));
                            RectF rectF2 = this.rectF;
                            float f802 = rectF2.bottom;
                            float centerX2 = rectF2.centerX();
                            float centerY2 = this.rectF.centerY();
                            canvas.save();
                            float f812 = 1.0f - f8;
                            canvas.translate(0.0f, AndroidUtilities.dpf2(2.0f) * f812);
                            canvas.rotate(f12, centerX2, centerY2);
                            canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(3.0f), AndroidUtilities.dpf2(3.0f), this.lockPaint);
                            if (f11 != 1.0f) {
                            }
                            if (f11 != 1.0f) {
                            }
                            canvas.restore();
                            canvas.restore();
                            if (this.scale != 1.0f) {
                            }
                            this.drawingCircleRadius = f19;
                        }
                    } else {
                        f17 = f15;
                    }
                    f21 = 0.0f;
                    if (this.tooltipAlpha == 0.0f) {
                        f18 = f14;
                        drawable4 = drawable3;
                        drawable5 = drawable2;
                        canvas.save();
                        canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - ChatActivityEnterView.this.textFieldContainer.getMeasuredHeight());
                        float f7122 = this.scale;
                        if (1.0f - f7122 != 0.0f) {
                        }
                        if (this.slideToCancelProgress >= 0.7f) {
                        }
                        this.showTooltip = false;
                        f20 = this.slideToCancelLockProgress;
                        if (f20 != 0.0f) {
                        }
                        dpf2 = AndroidUtilities.dpf2(72.0f);
                        dpf22 = ((dpf2 * f72) - ((AndroidUtilities.dpf2(20.0f) * f3) * (1.0f - f72))) + ((1.0f - this.slideToCancelLockProgress) * dpf2);
                        if (dpf22 <= dpf2) {
                        }
                        canvas.translate(0.0f, dpf2);
                        float f7622 = this.scale * (1.0f - f5) * (1.0f - f4) * this.slideToCancelLockProgress;
                        float f7722 = measuredWidth;
                        canvas.scale(f7622, f7622, f7722, f13);
                        this.rectF.set(f7722 - AndroidUtilities.dpf2(18.0f), f17, AndroidUtilities.dpf2(18.0f) + f7722, f17 + f16);
                        ChatActivityEnterView.this.lockShadowDrawable.setBounds((int) (this.rectF.left - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.top - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.right + AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.bottom + AndroidUtilities.dpf2(3.0f)));
                        ChatActivityEnterView.this.lockShadowDrawable.draw(canvas);
                        canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(18.0f), AndroidUtilities.dpf2(18.0f), this.lockBackgroundPaint);
                        ChatActivityEnterView.this.pauseRect.set(this.rectF);
                        float f7822 = 1.0f - f11;
                        float f7922 = f10;
                        this.rectF.set((f7722 - AndroidUtilities.dpf2(6.0f)) - (AndroidUtilities.dpf2(2.0f) * f7822), f13 - (AndroidUtilities.dpf2(2.0f) * f7822), AndroidUtilities.dp(6.0f) + measuredWidth + (AndroidUtilities.dpf2(2.0f) * f7822), f13 + AndroidUtilities.dp(12.0f) + (AndroidUtilities.dpf2(2.0f) * f7822));
                        RectF rectF22 = this.rectF;
                        float f8022 = rectF22.bottom;
                        float centerX22 = rectF22.centerX();
                        float centerY22 = this.rectF.centerY();
                        canvas.save();
                        float f8122 = 1.0f - f8;
                        canvas.translate(0.0f, AndroidUtilities.dpf2(2.0f) * f8122);
                        canvas.rotate(f12, centerX22, centerY22);
                        canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(3.0f), AndroidUtilities.dpf2(3.0f), this.lockPaint);
                        if (f11 != 1.0f) {
                        }
                        if (f11 != 1.0f) {
                        }
                        canvas.restore();
                        canvas.restore();
                        if (this.scale != 1.0f) {
                        }
                        this.drawingCircleRadius = f19;
                    }
                    if (f8 >= 0.8f) {
                    }
                    this.showTooltip = false;
                    if (this.showTooltip) {
                    }
                    int i42 = (int) (this.tooltipAlpha * 255.0f);
                    this.tooltipBackground.setAlpha(i42);
                    this.tooltipBackgroundArrow.setAlpha(i42);
                    this.tooltipPaint.setAlpha(i42);
                    if (this.tooltipLayout != null) {
                    }
                    drawable4 = drawable3;
                    drawable5 = drawable2;
                    canvas.save();
                    canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - ChatActivityEnterView.this.textFieldContainer.getMeasuredHeight());
                    float f71222 = this.scale;
                    if (1.0f - f71222 != 0.0f) {
                    }
                    if (this.slideToCancelProgress >= 0.7f) {
                    }
                    this.showTooltip = false;
                    f20 = this.slideToCancelLockProgress;
                    if (f20 != 0.0f) {
                    }
                    dpf2 = AndroidUtilities.dpf2(72.0f);
                    dpf22 = ((dpf2 * f72) - ((AndroidUtilities.dpf2(20.0f) * f3) * (1.0f - f72))) + ((1.0f - this.slideToCancelLockProgress) * dpf2);
                    if (dpf22 <= dpf2) {
                    }
                    canvas.translate(0.0f, dpf2);
                    float f76222 = this.scale * (1.0f - f5) * (1.0f - f4) * this.slideToCancelLockProgress;
                    float f77222 = measuredWidth;
                    canvas.scale(f76222, f76222, f77222, f13);
                    this.rectF.set(f77222 - AndroidUtilities.dpf2(18.0f), f17, AndroidUtilities.dpf2(18.0f) + f77222, f17 + f16);
                    ChatActivityEnterView.this.lockShadowDrawable.setBounds((int) (this.rectF.left - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.top - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.right + AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.bottom + AndroidUtilities.dpf2(3.0f)));
                    ChatActivityEnterView.this.lockShadowDrawable.draw(canvas);
                    canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(18.0f), AndroidUtilities.dpf2(18.0f), this.lockBackgroundPaint);
                    ChatActivityEnterView.this.pauseRect.set(this.rectF);
                    float f78222 = 1.0f - f11;
                    float f79222 = f10;
                    this.rectF.set((f77222 - AndroidUtilities.dpf2(6.0f)) - (AndroidUtilities.dpf2(2.0f) * f78222), f13 - (AndroidUtilities.dpf2(2.0f) * f78222), AndroidUtilities.dp(6.0f) + measuredWidth + (AndroidUtilities.dpf2(2.0f) * f78222), f13 + AndroidUtilities.dp(12.0f) + (AndroidUtilities.dpf2(2.0f) * f78222));
                    RectF rectF222 = this.rectF;
                    float f80222 = rectF222.bottom;
                    float centerX222 = rectF222.centerX();
                    float centerY222 = this.rectF.centerY();
                    canvas.save();
                    float f81222 = 1.0f - f8;
                    canvas.translate(0.0f, AndroidUtilities.dpf2(2.0f) * f81222);
                    canvas.rotate(f12, centerX222, centerY222);
                    canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(3.0f), AndroidUtilities.dpf2(3.0f), this.lockPaint);
                    if (f11 != 1.0f) {
                    }
                    if (f11 != 1.0f) {
                    }
                    canvas.restore();
                    canvas.restore();
                    if (this.scale != 1.0f) {
                    }
                    this.drawingCircleRadius = f19;
                }
            }
            drawable2 = drawable9;
            j = currentTimeMillis;
            f8 = dp3;
            f9 = f;
            f10 = f40;
            drawable3 = drawable8;
            if (!isSendButtonVisible()) {
            }
            if (!this.showTooltip) {
            }
            f21 = 0.0f;
            if (this.tooltipAlpha == 0.0f) {
            }
            if (f8 >= 0.8f) {
            }
            this.showTooltip = false;
            if (this.showTooltip) {
            }
            int i422 = (int) (this.tooltipAlpha * 255.0f);
            this.tooltipBackground.setAlpha(i422);
            this.tooltipBackgroundArrow.setAlpha(i422);
            this.tooltipPaint.setAlpha(i422);
            if (this.tooltipLayout != null) {
            }
            drawable4 = drawable3;
            drawable5 = drawable2;
            canvas.save();
            canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - ChatActivityEnterView.this.textFieldContainer.getMeasuredHeight());
            float f712222 = this.scale;
            if (1.0f - f712222 != 0.0f) {
            }
            if (this.slideToCancelProgress >= 0.7f) {
            }
            this.showTooltip = false;
            f20 = this.slideToCancelLockProgress;
            if (f20 != 0.0f) {
            }
            dpf2 = AndroidUtilities.dpf2(72.0f);
            dpf22 = ((dpf2 * f72) - ((AndroidUtilities.dpf2(20.0f) * f3) * (1.0f - f72))) + ((1.0f - this.slideToCancelLockProgress) * dpf2);
            if (dpf22 <= dpf2) {
            }
            canvas.translate(0.0f, dpf2);
            float f762222 = this.scale * (1.0f - f5) * (1.0f - f4) * this.slideToCancelLockProgress;
            float f772222 = measuredWidth;
            canvas.scale(f762222, f762222, f772222, f13);
            this.rectF.set(f772222 - AndroidUtilities.dpf2(18.0f), f17, AndroidUtilities.dpf2(18.0f) + f772222, f17 + f16);
            ChatActivityEnterView.this.lockShadowDrawable.setBounds((int) (this.rectF.left - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.top - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.right + AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.bottom + AndroidUtilities.dpf2(3.0f)));
            ChatActivityEnterView.this.lockShadowDrawable.draw(canvas);
            canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(18.0f), AndroidUtilities.dpf2(18.0f), this.lockBackgroundPaint);
            ChatActivityEnterView.this.pauseRect.set(this.rectF);
            float f782222 = 1.0f - f11;
            float f792222 = f10;
            this.rectF.set((f772222 - AndroidUtilities.dpf2(6.0f)) - (AndroidUtilities.dpf2(2.0f) * f782222), f13 - (AndroidUtilities.dpf2(2.0f) * f782222), AndroidUtilities.dp(6.0f) + measuredWidth + (AndroidUtilities.dpf2(2.0f) * f782222), f13 + AndroidUtilities.dp(12.0f) + (AndroidUtilities.dpf2(2.0f) * f782222));
            RectF rectF2222 = this.rectF;
            float f802222 = rectF2222.bottom;
            float centerX2222 = rectF2222.centerX();
            float centerY2222 = this.rectF.centerY();
            canvas.save();
            float f812222 = 1.0f - f8;
            canvas.translate(0.0f, AndroidUtilities.dpf2(2.0f) * f812222);
            canvas.rotate(f12, centerX2222, centerY2222);
            canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(3.0f), AndroidUtilities.dpf2(3.0f), this.lockPaint);
            if (f11 != 1.0f) {
            }
            if (f11 != 1.0f) {
            }
            canvas.restore();
            canvas.restore();
            if (this.scale != 1.0f) {
            }
            this.drawingCircleRadius = f19;
        }

        public void drawIcon(Canvas canvas, int i, int i2, float f) {
            Drawable drawable;
            Drawable drawable2 = null;
            if (!isSendButtonVisible()) {
                drawable = (ChatActivityEnterView.this.videoSendButton == null || ChatActivityEnterView.this.videoSendButton.getTag() == null) ? ChatActivityEnterView.this.micDrawable : ChatActivityEnterView.this.cameraDrawable;
            } else {
                if (this.progressToSendButton != 1.0f) {
                    drawable2 = (ChatActivityEnterView.this.videoSendButton == null || ChatActivityEnterView.this.videoSendButton.getTag() == null) ? ChatActivityEnterView.this.micDrawable : ChatActivityEnterView.this.cameraDrawable;
                }
                drawable = ChatActivityEnterView.this.sendDrawable;
            }
            Drawable drawable3 = drawable;
            Drawable drawable4 = drawable2;
            ChatActivityEnterView.this.sendRect.set(i - (drawable3.getIntrinsicWidth() / 2), i2 - (drawable3.getIntrinsicHeight() / 2), (drawable3.getIntrinsicWidth() / 2) + i, (drawable3.getIntrinsicHeight() / 2) + i2);
            drawable3.setBounds(ChatActivityEnterView.this.sendRect);
            if (drawable4 != null) {
                drawable4.setBounds(i - (drawable4.getIntrinsicWidth() / 2), i2 - (drawable4.getIntrinsicHeight() / 2), i + (drawable4.getIntrinsicWidth() / 2), i2 + (drawable4.getIntrinsicHeight() / 2));
            }
            drawIconInternal(canvas, drawable3, drawable4, this.progressToSendButton, (int) (f * 255.0f));
        }

        private void drawIconInternal(Canvas canvas, Drawable drawable, Drawable drawable2, float f, int i) {
            float f2 = 0.0f;
            if (f == 0.0f || f == 1.0f || drawable2 == null) {
                boolean z = this.canceledByGesture;
                if (z && this.slideToCancelProgress == 1.0f) {
                    (ChatActivityEnterView.this.isInVideoMode() ? ChatActivityEnterView.this.videoSendButton : ChatActivityEnterView.this.audioSendButton).setAlpha(1.0f);
                    setVisibility(8);
                    return;
                } else if (!z || this.slideToCancelProgress >= 1.0f) {
                    if (z) {
                        return;
                    }
                    drawable.setAlpha(i);
                    drawable.draw(canvas);
                    return;
                } else {
                    Drawable drawable3 = ChatActivityEnterView.this.isInVideoMode() ? ChatActivityEnterView.this.cameraOutline : ChatActivityEnterView.this.micOutline;
                    drawable3.setBounds(drawable.getBounds());
                    float f3 = this.slideToCancelProgress;
                    if (f3 >= 0.93f) {
                        f2 = ((f3 - 0.93f) / 0.07f) * 255.0f;
                    }
                    int i2 = (int) f2;
                    drawable3.setAlpha(i2);
                    drawable3.draw(canvas);
                    drawable3.setAlpha(255);
                    drawable.setAlpha(255 - i2);
                    drawable.draw(canvas);
                    return;
                }
            }
            canvas.save();
            canvas.scale(f, f, drawable.getBounds().centerX(), drawable.getBounds().centerY());
            float f4 = i;
            drawable.setAlpha((int) (f4 * f));
            drawable.draw(canvas);
            canvas.restore();
            canvas.save();
            float f5 = 1.0f - f;
            canvas.scale(f5, f5, drawable.getBounds().centerX(), drawable.getBounds().centerY());
            drawable2.setAlpha((int) (f4 * f5));
            drawable2.draw(canvas);
            canvas.restore();
        }

        @Override // android.view.View
        protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
            return super.dispatchHoverEvent(motionEvent) || this.virtualViewHelper.dispatchHoverEvent(motionEvent);
        }

        public void setTransformToSeekbar(float f) {
            this.transformToSeekbar = f;
            invalidate();
        }

        public float getTransformToSeekbarProgressStep3() {
            return this.progressToSeekbarStep3;
        }

        @Keep
        public float getExitTransition() {
            return this.exitTransition;
        }

        @Keep
        public void setExitTransition(float f) {
            this.exitTransition = f;
            invalidate();
        }

        public void updateColors() {
            ChatActivityEnterView.this.paint.setColor(ChatActivityEnterView.this.getThemedColor("chat_messagePanelVoiceBackground"));
            this.tinyWaveDrawable.paint.setColor(ColorUtils.setAlphaComponent(ChatActivityEnterView.this.getThemedColor("chat_messagePanelVoiceBackground"), 38));
            this.bigWaveDrawable.paint.setColor(ColorUtils.setAlphaComponent(ChatActivityEnterView.this.getThemedColor("chat_messagePanelVoiceBackground"), 76));
            this.tooltipPaint.setColor(ChatActivityEnterView.this.getThemedColor("chat_gifSaveHintText"));
            this.tooltipBackground = Theme.createRoundRectDrawable(AndroidUtilities.dp(5.0f), ChatActivityEnterView.this.getThemedColor("chat_gifSaveHintBackground"));
            this.tooltipBackgroundArrow.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor("chat_gifSaveHintBackground"), PorterDuff.Mode.MULTIPLY));
            this.lockBackgroundPaint.setColor(ChatActivityEnterView.this.getThemedColor("key_chat_messagePanelVoiceLockBackground"));
            this.lockPaint.setColor(ChatActivityEnterView.this.getThemedColor("key_chat_messagePanelVoiceLock"));
            this.lockOutlinePaint.setColor(ChatActivityEnterView.this.getThemedColor("key_chat_messagePanelVoiceLock"));
            this.paintAlpha = ChatActivityEnterView.this.paint.getAlpha();
        }

        public void showTooltipIfNeed() {
            if (SharedConfig.lockRecordAudioVideoHint < 3) {
                this.showTooltip = true;
                this.showTooltipStartTime = System.currentTimeMillis();
            }
        }

        @Keep
        public float getSlideToCancelProgress() {
            return this.slideToCancelProgress;
        }

        @Keep
        public void setSlideToCancelProgress(float f) {
            this.slideToCancelProgress = f;
            float measuredWidth = getMeasuredWidth() * 0.35f;
            if (measuredWidth > AndroidUtilities.dp(140.0f)) {
                measuredWidth = AndroidUtilities.dp(140.0f);
            }
            this.slideDelta = (int) ((-measuredWidth) * (1.0f - f));
            invalidate();
        }

        public void canceledByGesture() {
            this.canceledByGesture = true;
        }

        public void setMovingCords(float f, float f2) {
            float f3 = this.lastMovingX;
            float f4 = (f - f3) * (f - f3);
            float f5 = this.lastMovingY;
            float f6 = f4 + ((f2 - f5) * (f2 - f5));
            this.lastMovingY = f2;
            this.lastMovingX = f;
            if (!this.showTooltip || this.tooltipAlpha != 0.0f || f6 <= this.touchSlop) {
                return;
            }
            this.showTooltipStartTime = System.currentTimeMillis();
        }

        public void showWaves(boolean z, boolean z2) {
            if (!z2) {
                this.wavesEnterAnimation = z ? 1.0f : 0.5f;
            }
            this.showWaves = z;
        }

        public void drawWaves(Canvas canvas, float f, float f2, float f3) {
            float interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.wavesEnterAnimation);
            float f4 = this.slideToCancelProgress;
            float f5 = f4 > 0.7f ? 1.0f : f4 / 0.7f;
            canvas.save();
            float f6 = this.scale * f5 * interpolation * (BlobDrawable.SCALE_BIG_MIN + (this.bigWaveDrawable.amplitude * 1.4f)) * f3;
            canvas.scale(f6, f6, f, f2);
            BlobDrawable blobDrawable = this.bigWaveDrawable;
            blobDrawable.draw(f, f2, canvas, blobDrawable.paint);
            canvas.restore();
            float f7 = this.scale * f5 * interpolation * (BlobDrawable.SCALE_SMALL_MIN + (this.tinyWaveDrawable.amplitude * 1.4f)) * f3;
            canvas.save();
            canvas.scale(f7, f7, f, f2);
            BlobDrawable blobDrawable2 = this.tinyWaveDrawable;
            blobDrawable2.draw(f, f2, canvas, blobDrawable2.paint);
            canvas.restore();
        }

        /* loaded from: classes3.dex */
        public class VirtualViewHelper extends ExploreByTouchHelper {
            private int[] coords = new int[2];

            @Override // androidx.customview.widget.ExploreByTouchHelper
            protected boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
                return true;
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public VirtualViewHelper(View view) {
                super(view);
                RecordCircle.this = r1;
            }

            @Override // androidx.customview.widget.ExploreByTouchHelper
            protected int getVirtualViewAt(float f, float f2) {
                if (RecordCircle.this.isSendButtonVisible()) {
                    if (ChatActivityEnterView.this.sendRect.contains((int) f, (int) f2)) {
                        return 1;
                    }
                    if (ChatActivityEnterView.this.pauseRect.contains(f, f2)) {
                        return 2;
                    }
                    if (ChatActivityEnterView.this.slideText == null || ChatActivityEnterView.this.slideText.cancelRect == null) {
                        return -1;
                    }
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(ChatActivityEnterView.this.slideText.cancelRect);
                    ChatActivityEnterView.this.slideText.getLocationOnScreen(this.coords);
                    int[] iArr = this.coords;
                    rectF.offset(iArr[0], iArr[1]);
                    ChatActivityEnterView.this.recordCircle.getLocationOnScreen(this.coords);
                    int[] iArr2 = this.coords;
                    rectF.offset(-iArr2[0], -iArr2[1]);
                    return rectF.contains(f, f2) ? 3 : -1;
                }
                return -1;
            }

            @Override // androidx.customview.widget.ExploreByTouchHelper
            protected void getVisibleVirtualViews(List<Integer> list) {
                if (RecordCircle.this.isSendButtonVisible()) {
                    list.add(1);
                    list.add(2);
                    list.add(3);
                }
            }

            @Override // androidx.customview.widget.ExploreByTouchHelper
            protected void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                if (i == 1) {
                    accessibilityNodeInfoCompat.setBoundsInParent(ChatActivityEnterView.this.sendRect);
                    accessibilityNodeInfoCompat.setText(LocaleController.getString("Send", 2131628175));
                } else if (i == 2) {
                    ChatActivityEnterView.this.rect.set((int) ChatActivityEnterView.this.pauseRect.left, (int) ChatActivityEnterView.this.pauseRect.top, (int) ChatActivityEnterView.this.pauseRect.right, (int) ChatActivityEnterView.this.pauseRect.bottom);
                    accessibilityNodeInfoCompat.setBoundsInParent(ChatActivityEnterView.this.rect);
                    accessibilityNodeInfoCompat.setText(LocaleController.getString("Stop", 2131628463));
                } else if (i != 3) {
                } else {
                    if (ChatActivityEnterView.this.slideText != null && ChatActivityEnterView.this.slideText.cancelRect != null) {
                        android.graphics.Rect rect = AndroidUtilities.rectTmp2;
                        rect.set(ChatActivityEnterView.this.slideText.cancelRect);
                        ChatActivityEnterView.this.slideText.getLocationOnScreen(this.coords);
                        int[] iArr = this.coords;
                        rect.offset(iArr[0], iArr[1]);
                        ChatActivityEnterView.this.recordCircle.getLocationOnScreen(this.coords);
                        int[] iArr2 = this.coords;
                        rect.offset(-iArr2[0], -iArr2[1]);
                        accessibilityNodeInfoCompat.setBoundsInParent(rect);
                    }
                    accessibilityNodeInfoCompat.setText(LocaleController.getString("Cancel", 2131624819));
                }
            }
        }
    }

    public ChatActivityEnterView(Activity activity, SizeNotifierFrameLayout sizeNotifierFrameLayout, ChatActivity chatActivity, boolean z) {
        this(activity, sizeNotifierFrameLayout, chatActivity, z, null);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public ChatActivityEnterView(Activity activity, SizeNotifierFrameLayout sizeNotifierFrameLayout, ChatActivity chatActivity, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(activity, chatActivity == null ? null : chatActivity.contentView);
        String str;
        String str2;
        int i;
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate;
        int i2 = UserConfig.selectedAccount;
        this.currentAccount = i2;
        this.accountInstance = AccountInstance.getInstance(i2);
        this.lineCount = 1;
        this.currentLimit = -1;
        this.botMenuButtonType = BotMenuButtonType.NO_BUTTON;
        this.animationParamsX = new HashMap<>();
        this.mediaMessageButtonsDelegate = new AnonymousClass1(this);
        int i3 = 2;
        this.emojiButton = new ImageView[2];
        this.currentPopupContentType = -1;
        this.currentEmojiIcon = -1;
        this.isPaused = true;
        this.startedDraggingX = -1.0f;
        this.distCanMove = AndroidUtilities.dp(80.0f);
        this.location = new int[2];
        this.messageWebPageSearch = true;
        this.animatingContentType = -1;
        this.doneButtonEnabledProgress = 1.0f;
        this.doneButtonEnabled = true;
        this.openKeyboardRunnable = new AnonymousClass2();
        this.updateExpandabilityRunnable = new AnonymousClass3();
        this.roundedTranslationYProperty = new AnonymousClass4(this, Integer.class, "translationY");
        this.recordCircleScale = new AnonymousClass5(this, Float.class, "scale");
        this.redDotPaint = new Paint(1);
        this.onFinishInitCameraRunnable = new AnonymousClass6();
        this.recordAudioVideoRunnable = new AnonymousClass7();
        this.paint = new Paint(1);
        this.pauseRect = new RectF();
        this.sendRect = new android.graphics.Rect();
        this.rect = new android.graphics.Rect();
        this.runEmojiPanelAnimation = new AnonymousClass8();
        this.allowBlur = true;
        this.backgroundPaint = new Paint();
        this.composeShadowAlpha = 1.0f;
        this.topViewUpdateListener = new ChatActivityEnterView$$ExternalSyntheticLambda1(this);
        this.botCommandLastPosition = -1;
        this.resourcesProvider = resourcesProvider;
        this.backgroundColor = getThemedColor("chat_messagePanelBackground");
        boolean z2 = false;
        this.drawBlur = false;
        this.smoothKeyboard = z && SharedConfig.smoothKeyboard && !AndroidUtilities.isInMultiwindow && (chatActivity == null || !chatActivity.isInBubbleMode());
        Paint paint = new Paint(1);
        this.dotPaint = paint;
        paint.setColor(getThemedColor("chat_emojiPanelNewTrending"));
        setFocusable(true);
        setFocusableInTouchMode(true);
        setWillNotDraw(false);
        setClipChildren(false);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStarted);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStartError);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStopped);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.audioDidSent);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.audioRouteChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messageReceivedByServer);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.sendingMessagesChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.audioRecordTooShort);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateBotMenuButton);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        this.parentActivity = activity;
        this.parentFragment = chatActivity;
        if (chatActivity != null) {
            this.recordingGuid = chatActivity.getClassGuid();
        }
        this.sizeNotifierLayout = sizeNotifierFrameLayout;
        sizeNotifierFrameLayout.setDelegate(this);
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        this.sendByEnter = globalMainSettings.getBoolean("send_by_enter", false);
        this.configAnimationsEnabled = globalMainSettings.getBoolean("view_animations", true);
        AnonymousClass9 anonymousClass9 = new AnonymousClass9(activity);
        this.textFieldContainer = anonymousClass9;
        anonymousClass9.setClipChildren(false);
        this.textFieldContainer.setClipToPadding(false);
        this.textFieldContainer.setPadding(0, AndroidUtilities.dp(1.0f), 0, 0);
        addView(this.textFieldContainer, LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 1.0f, 0.0f, 0.0f));
        AnonymousClass10 anonymousClass10 = new AnonymousClass10(activity);
        anonymousClass10.setClipChildren(false);
        this.textFieldContainer.addView(anonymousClass10, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 48.0f, 0.0f));
        int i4 = 0;
        while (i4 < i3) {
            this.emojiButton[i4] = new AnonymousClass11(activity);
            this.emojiButton[i4].setColorFilter(new PorterDuffColorFilter(getThemedColor("chat_messagePanelIcons"), PorterDuff.Mode.MULTIPLY));
            this.emojiButton[i4].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            if (Build.VERSION.SDK_INT >= 21) {
                this.emojiButton[i4].setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor("listSelectorSDK21")));
            }
            anonymousClass10.addView(this.emojiButton[i4], LayoutHelper.createFrame(48, 48.0f, 83, 3.0f, 0.0f, 0.0f, 0.0f));
            this.emojiButton[i4].setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda22(this));
            this.emojiButton[i4].setContentDescription(LocaleController.getString("AccDescrEmojiButton", 2131623982));
            if (i4 == 1) {
                this.emojiButton[i4].setVisibility(4);
                this.emojiButton[i4].setAlpha(0.0f);
                this.emojiButton[i4].setScaleX(0.1f);
                this.emojiButton[i4].setScaleY(0.1f);
            }
            i4++;
            i3 = 2;
            z2 = false;
        }
        setEmojiButtonImage(z2, z2);
        NumberTextView numberTextView = new NumberTextView(activity);
        this.captionLimitView = numberTextView;
        numberTextView.setVisibility(8);
        this.captionLimitView.setTextSize(15);
        this.captionLimitView.setTextColor(getThemedColor("windowBackgroundWhiteGrayText"));
        this.captionLimitView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.captionLimitView.setCenterAlign(true);
        addView(this.captionLimitView, LayoutHelper.createFrame(48, 20.0f, 85, 3.0f, 0.0f, 0.0f, 48.0f));
        AnonymousClass12 anonymousClass12 = new AnonymousClass12(activity, resourcesProvider, resourcesProvider, chatActivity, activity);
        this.messageEditText = anonymousClass12;
        anonymousClass12.setDelegate(new ChatActivityEnterView$$ExternalSyntheticLambda58(this));
        this.messageEditText.setIncludeFontPadding(false);
        this.messageEditText.setWindowView(this.parentActivity.getWindow().getDecorView());
        ChatActivity chatActivity2 = this.parentFragment;
        TLRPC$EncryptedChat currentEncryptedChat = chatActivity2 != null ? chatActivity2.getCurrentEncryptedChat() : null;
        this.messageEditText.setAllowTextEntitiesIntersection(supportsSendingNewEntities());
        updateFieldHint(false);
        this.messageEditText.setImeOptions(currentEncryptedChat != null ? 285212672 : 268435456);
        EditTextCaption editTextCaption = this.messageEditText;
        editTextCaption.setInputType(editTextCaption.getInputType() | 16384 | 131072);
        this.messageEditText.setSingleLine(false);
        this.messageEditText.setMaxLines(6);
        this.messageEditText.setTextSize(1, 18.0f);
        this.messageEditText.setGravity(80);
        this.messageEditText.setPadding(0, AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(12.0f));
        this.messageEditText.setBackgroundDrawable(null);
        this.messageEditText.setTextColor(getThemedColor("chat_messagePanelText"));
        this.messageEditText.setLinkTextColor(getThemedColor("chat_messageLinkOut"));
        this.messageEditText.setHighlightColor(getThemedColor("chat_inTextSelectionHighlight"));
        this.messageEditText.setHintColor(getThemedColor("chat_messagePanelHint"));
        this.messageEditText.setHintTextColor(getThemedColor("chat_messagePanelHint"));
        this.messageEditText.setCursorColor(getThemedColor("chat_messagePanelCursor"));
        this.messageEditText.setHandlesColor(getThemedColor("chat_TextSelectionCursor"));
        anonymousClass10.addView(this.messageEditText, LayoutHelper.createFrame(-1, -2.0f, 80, 52.0f, 0.0f, z ? 50.0f : 2.0f, 0.0f));
        this.messageEditText.setOnKeyListener(new AnonymousClass13());
        this.messageEditText.setOnEditorActionListener(new AnonymousClass14());
        this.messageEditText.addTextChangedListener(new AnonymousClass15());
        if (z) {
            if (this.parentFragment != null) {
                Drawable mutate = activity.getResources().getDrawable(2131165530).mutate();
                Drawable mutate2 = activity.getResources().getDrawable(2131165531).mutate();
                mutate.setColorFilter(new PorterDuffColorFilter(getThemedColor("chat_messagePanelIcons"), PorterDuff.Mode.MULTIPLY));
                mutate2.setColorFilter(new PorterDuffColorFilter(getThemedColor("chat_recordedVoiceDot"), PorterDuff.Mode.MULTIPLY));
                CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, mutate2);
                ImageView imageView = new ImageView(activity);
                this.scheduledButton = imageView;
                imageView.setImageDrawable(combinedDrawable);
                this.scheduledButton.setVisibility(8);
                this.scheduledButton.setContentDescription(LocaleController.getString("ScheduledMessages", 2131628084));
                this.scheduledButton.setScaleType(ImageView.ScaleType.CENTER);
                if (Build.VERSION.SDK_INT >= 21) {
                    str = "listSelectorSDK21";
                    this.scheduledButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(str)));
                } else {
                    str = "listSelectorSDK21";
                }
                anonymousClass10.addView(this.scheduledButton, LayoutHelper.createFrame(48, 48, 85));
                this.scheduledButton.setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda25(this));
            } else {
                str = "listSelectorSDK21";
            }
            LinearLayout linearLayout = new LinearLayout(activity);
            this.attachLayout = linearLayout;
            linearLayout.setOrientation(0);
            this.attachLayout.setEnabled(false);
            this.attachLayout.setPivotX(AndroidUtilities.dp(48.0f));
            this.attachLayout.setClipChildren(false);
            anonymousClass10.addView(this.attachLayout, LayoutHelper.createFrame(-2, 48, 85));
            BotCommandsMenuView botCommandsMenuView = new BotCommandsMenuView(getContext());
            this.botCommandsMenuButton = botCommandsMenuView;
            botCommandsMenuView.setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda16(this));
            anonymousClass10.addView(this.botCommandsMenuButton, LayoutHelper.createFrame(-2, 32.0f, 83, 10.0f, 8.0f, 10.0f, 8.0f));
            AndroidUtilities.updateViewVisibilityAnimated(this.botCommandsMenuButton, false, 1.0f, false);
            this.botCommandsMenuButton.setExpanded(true, false);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
            AnonymousClass16 anonymousClass16 = new AnonymousClass16(activity);
            this.botCommandsMenuContainer = anonymousClass16;
            anonymousClass16.listView.setLayoutManager(linearLayoutManager);
            RecyclerListView recyclerListView = this.botCommandsMenuContainer.listView;
            BotCommandsMenuView.BotCommandsAdapter botCommandsAdapter = new BotCommandsMenuView.BotCommandsAdapter();
            this.botCommandsAdapter = botCommandsAdapter;
            recyclerListView.setAdapter(botCommandsAdapter);
            this.botCommandsMenuContainer.listView.setOnItemClickListener(new AnonymousClass17(resourcesProvider, chatActivity));
            this.botCommandsMenuContainer.listView.setOnItemLongClickListener(new AnonymousClass18());
            this.botCommandsMenuContainer.setClipToPadding(false);
            this.sizeNotifierLayout.addView(this.botCommandsMenuContainer, 14, LayoutHelper.createFrame(-1, -1, 80));
            this.botCommandsMenuContainer.setVisibility(8);
            AnonymousClass19 anonymousClass19 = new AnonymousClass19(activity, this);
            this.botWebViewMenuContainer = anonymousClass19;
            this.sizeNotifierLayout.addView(anonymousClass19, 15, LayoutHelper.createFrame(-1, -1, 80));
            this.botWebViewMenuContainer.setVisibility(8);
            this.botWebViewMenuContainer.setOnDismissGlobalListener(new ChatActivityEnterView$$ExternalSyntheticLambda34(this));
            ImageView imageView2 = new ImageView(activity);
            this.botButton = imageView2;
            ReplaceableIconDrawable replaceableIconDrawable = new ReplaceableIconDrawable(activity);
            this.botButtonDrawable = replaceableIconDrawable;
            imageView2.setImageDrawable(replaceableIconDrawable);
            this.botButtonDrawable.setColorFilter(new PorterDuffColorFilter(getThemedColor("chat_messagePanelIcons"), PorterDuff.Mode.MULTIPLY));
            this.botButtonDrawable.setIcon(2131165529, false);
            this.botButton.setScaleType(ImageView.ScaleType.CENTER);
            int i5 = Build.VERSION.SDK_INT;
            if (i5 >= 21) {
                this.botButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(str)));
            }
            this.botButton.setVisibility(8);
            AndroidUtilities.updateViewVisibilityAnimated(this.botButton, false, 0.1f, false);
            this.attachLayout.addView(this.botButton, LayoutHelper.createLinear(48, 48));
            this.botButton.setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda18(this));
            this.notifyButton = new ImageView(activity);
            CrossOutDrawable crossOutDrawable = new CrossOutDrawable(activity, 2131165540, "chat_messagePanelIcons");
            this.notifySilentDrawable = crossOutDrawable;
            this.notifyButton.setImageDrawable(crossOutDrawable);
            this.notifySilentDrawable.setCrossOut(this.silent, false);
            ImageView imageView3 = this.notifyButton;
            if (this.silent) {
                i = 2131623973;
                str2 = "AccDescrChanSilentOn";
            } else {
                i = 2131623972;
                str2 = "AccDescrChanSilentOff";
            }
            imageView3.setContentDescription(LocaleController.getString(str2, i));
            this.notifyButton.setColorFilter(new PorterDuffColorFilter(getThemedColor("chat_messagePanelIcons"), PorterDuff.Mode.MULTIPLY));
            this.notifyButton.setScaleType(ImageView.ScaleType.CENTER);
            if (i5 >= 21) {
                this.notifyButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(str)));
            }
            this.notifyButton.setVisibility((!this.canWriteToChannel || ((chatActivityEnterViewDelegate = this.delegate) != null && chatActivityEnterViewDelegate.hasScheduledMessages())) ? 8 : 0);
            this.attachLayout.addView(this.notifyButton, LayoutHelper.createLinear(48, 48));
            this.notifyButton.setOnClickListener(new AnonymousClass20(activity, chatActivity));
            ImageView imageView4 = new ImageView(activity);
            this.attachButton = imageView4;
            imageView4.setColorFilter(new PorterDuffColorFilter(getThemedColor("chat_messagePanelIcons"), PorterDuff.Mode.MULTIPLY));
            this.attachButton.setImageResource(2131165527);
            this.attachButton.setScaleType(ImageView.ScaleType.CENTER);
            if (i5 >= 21) {
                this.attachButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(str)));
            }
            this.attachLayout.addView(this.attachButton, LayoutHelper.createLinear(48, 48));
            this.attachButton.setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda13(this));
            this.attachButton.setContentDescription(LocaleController.getString("AccDescrAttachButton", 2131623960));
        } else {
            str = "listSelectorSDK21";
        }
        SenderSelectView senderSelectView = new SenderSelectView(getContext());
        this.senderSelectView = senderSelectView;
        senderSelectView.setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda26(this, activity));
        this.senderSelectView.setVisibility(8);
        anonymousClass10.addView(this.senderSelectView, LayoutHelper.createFrame(32, 32.0f, 83, 10.0f, 8.0f, 10.0f, 8.0f));
        AnonymousClass26 anonymousClass26 = new AnonymousClass26(activity);
        this.recordedAudioPanel = anonymousClass26;
        anonymousClass26.setVisibility(this.audioToSend == null ? 8 : 0);
        this.recordedAudioPanel.setFocusable(true);
        this.recordedAudioPanel.setFocusableInTouchMode(true);
        this.recordedAudioPanel.setClickable(true);
        anonymousClass10.addView(this.recordedAudioPanel, LayoutHelper.createFrame(-1, 48, 80));
        RLottieImageView rLottieImageView = new RLottieImageView(activity);
        this.recordDeleteImageView = rLottieImageView;
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        this.recordDeleteImageView.setAnimation(2131558419, 28, 28);
        this.recordDeleteImageView.getAnimatedDrawable().setInvalidateOnProgressSet(true);
        updateRecordedDeleteIconColors();
        this.recordDeleteImageView.setContentDescription(LocaleController.getString("Delete", 2131625368));
        int i6 = Build.VERSION.SDK_INT;
        if (i6 >= 21) {
            this.recordDeleteImageView.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(str)));
        }
        this.recordedAudioPanel.addView(this.recordDeleteImageView, LayoutHelper.createFrame(48, 48.0f));
        this.recordDeleteImageView.setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda12(this));
        VideoTimelineView videoTimelineView = new VideoTimelineView(activity);
        this.videoTimelineView = videoTimelineView;
        videoTimelineView.setColor(getThemedColor("chat_messagePanelVideoFrame"));
        this.videoTimelineView.setRoundFrames(true);
        this.videoTimelineView.setDelegate(new AnonymousClass27());
        this.recordedAudioPanel.addView(this.videoTimelineView, LayoutHelper.createFrame(-1, -1.0f, 19, 56.0f, 0.0f, 8.0f, 0.0f));
        VideoTimelineView.TimeHintView timeHintView = new VideoTimelineView.TimeHintView(activity);
        this.videoTimelineView.setTimeHintView(timeHintView);
        this.sizeNotifierLayout.addView(timeHintView, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 52.0f));
        View view = new View(activity);
        this.recordedAudioBackground = view;
        view.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), getThemedColor("chat_recordedVoiceBackground")));
        this.recordedAudioPanel.addView(this.recordedAudioBackground, LayoutHelper.createFrame(-1, 36.0f, 19, 48.0f, 0.0f, 0.0f, 0.0f));
        this.recordedAudioSeekBar = new SeekBarWaveformView(activity);
        LinearLayout linearLayout2 = new LinearLayout(activity);
        linearLayout2.setOrientation(0);
        this.recordedAudioPanel.addView(linearLayout2, LayoutHelper.createFrame(-1, 32.0f, 19, 92.0f, 0.0f, 13.0f, 0.0f));
        this.playPauseDrawable = new MediaActionDrawable();
        this.recordedAudioPlayButton = new ImageView(activity);
        Matrix matrix = new Matrix();
        matrix.postScale(0.8f, 0.8f, AndroidUtilities.dpf2(24.0f), AndroidUtilities.dpf2(24.0f));
        this.recordedAudioPlayButton.setImageMatrix(matrix);
        this.recordedAudioPlayButton.setImageDrawable(this.playPauseDrawable);
        this.recordedAudioPlayButton.setScaleType(ImageView.ScaleType.MATRIX);
        this.recordedAudioPlayButton.setContentDescription(LocaleController.getString("AccActionPlay", 2131623955));
        this.recordedAudioPanel.addView(this.recordedAudioPlayButton, LayoutHelper.createFrame(48, 48.0f, 83, 48.0f, 0.0f, 13.0f, 0.0f));
        this.recordedAudioPlayButton.setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda14(this));
        TextView textView = new TextView(activity);
        this.recordedAudioTimeTextView = textView;
        textView.setTextColor(getThemedColor("chat_messagePanelVoiceDuration"));
        this.recordedAudioTimeTextView.setTextSize(1, 13.0f);
        linearLayout2.addView(this.recordedAudioSeekBar, LayoutHelper.createLinear(0, 32, 1.0f, 16, 0, 0, 4, 0));
        linearLayout2.addView(this.recordedAudioTimeTextView, LayoutHelper.createLinear(-2, -2, 0.0f, 16));
        FrameLayout frameLayout = new FrameLayout(activity);
        this.recordPanel = frameLayout;
        frameLayout.setClipChildren(false);
        this.recordPanel.setVisibility(8);
        anonymousClass10.addView(this.recordPanel, LayoutHelper.createFrame(-1, 48.0f));
        this.recordPanel.setOnTouchListener(ChatActivityEnterView$$ExternalSyntheticLambda30.INSTANCE);
        SlideTextView slideTextView = new SlideTextView(activity);
        this.slideText = slideTextView;
        this.recordPanel.addView(slideTextView, LayoutHelper.createFrame(-1, -1.0f, 0, 45.0f, 0.0f, 0.0f, 0.0f));
        LinearLayout linearLayout3 = new LinearLayout(activity);
        this.recordTimeContainer = linearLayout3;
        linearLayout3.setOrientation(0);
        this.recordTimeContainer.setPadding(AndroidUtilities.dp(13.0f), 0, 0, 0);
        this.recordTimeContainer.setFocusable(false);
        this.recordPanel.addView(this.recordTimeContainer, LayoutHelper.createFrame(-1, -1, 16));
        RecordDot recordDot = new RecordDot(activity);
        this.recordDot = recordDot;
        this.recordTimeContainer.addView(recordDot, LayoutHelper.createLinear(28, 28, 16, 0, 0, 0, 0));
        TimerView timerView = new TimerView(activity);
        this.recordTimerView = timerView;
        this.recordTimeContainer.addView(timerView, LayoutHelper.createLinear(-1, -1, 16, 6, 0, 0, 0));
        AnonymousClass28 anonymousClass28 = new AnonymousClass28(activity);
        this.sendButtonContainer = anonymousClass28;
        anonymousClass28.setClipChildren(false);
        this.sendButtonContainer.setClipToPadding(false);
        this.textFieldContainer.addView(this.sendButtonContainer, LayoutHelper.createFrame(48, 48, 85));
        FrameLayout frameLayout2 = new FrameLayout(activity);
        this.audioVideoButtonContainer = frameLayout2;
        frameLayout2.setSoundEffectsEnabled(false);
        this.sendButtonContainer.addView(this.audioVideoButtonContainer, LayoutHelper.createFrame(48, 48.0f));
        this.audioVideoButtonContainer.setFocusable(true);
        this.audioVideoButtonContainer.setImportantForAccessibility(1);
        this.audioVideoButtonContainer.setOnTouchListener(new ChatActivityEnterView$$ExternalSyntheticLambda29(this, resourcesProvider));
        ImageView imageView5 = new ImageView(activity);
        this.audioSendButton = imageView5;
        imageView5.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.audioSendButton.setColorFilter(new PorterDuffColorFilter(getThemedColor("chat_messagePanelIcons"), PorterDuff.Mode.MULTIPLY));
        this.audioSendButton.setImageResource(2131165537);
        this.audioSendButton.setPadding(0, 0, AndroidUtilities.dp(4.0f), 0);
        this.audioSendButton.setContentDescription(LocaleController.getString("AccDescrVoiceMessage", 2131624107));
        this.audioSendButton.setFocusable(true);
        this.audioSendButton.setImportantForAccessibility(1);
        this.audioSendButton.setAccessibilityDelegate(this.mediaMessageButtonsDelegate);
        this.audioVideoButtonContainer.addView(this.audioSendButton, LayoutHelper.createFrame(48, 48.0f));
        if (z) {
            ImageView imageView6 = new ImageView(activity);
            this.videoSendButton = imageView6;
            imageView6.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            this.videoSendButton.setColorFilter(new PorterDuffColorFilter(getThemedColor("chat_messagePanelIcons"), PorterDuff.Mode.MULTIPLY));
            this.videoSendButton.setImageResource(2131165545);
            this.videoSendButton.setPadding(0, 0, AndroidUtilities.dp(4.0f), 0);
            this.videoSendButton.setContentDescription(LocaleController.getString("AccDescrVideoMessage", 2131624105));
            this.videoSendButton.setFocusable(true);
            this.videoSendButton.setImportantForAccessibility(1);
            this.videoSendButton.setAccessibilityDelegate(this.mediaMessageButtonsDelegate);
            this.audioVideoButtonContainer.addView(this.videoSendButton, LayoutHelper.createFrame(48, 48.0f));
        }
        RecordCircle recordCircle = new RecordCircle(activity);
        this.recordCircle = recordCircle;
        recordCircle.setVisibility(8);
        this.sizeNotifierLayout.addView(this.recordCircle, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 0.0f));
        ImageView imageView7 = new ImageView(activity);
        this.cancelBotButton = imageView7;
        imageView7.setVisibility(4);
        this.cancelBotButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ImageView imageView8 = this.cancelBotButton;
        AnonymousClass29 anonymousClass29 = new AnonymousClass29(this);
        this.progressDrawable = anonymousClass29;
        imageView8.setImageDrawable(anonymousClass29);
        this.cancelBotButton.setContentDescription(LocaleController.getString("Cancel", 2131624819));
        this.cancelBotButton.setSoundEffectsEnabled(false);
        this.cancelBotButton.setScaleX(0.1f);
        this.cancelBotButton.setScaleY(0.1f);
        this.cancelBotButton.setAlpha(0.0f);
        if (i6 >= 21) {
            this.cancelBotButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(str)));
        }
        this.sendButtonContainer.addView(this.cancelBotButton, LayoutHelper.createFrame(48, 48.0f));
        this.cancelBotButton.setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda24(this));
        if (isInScheduleMode()) {
            this.sendButtonDrawable = activity.getResources().getDrawable(2131165542).mutate();
            this.sendButtonInverseDrawable = activity.getResources().getDrawable(2131165542).mutate();
            this.inactinveSendButtonDrawable = activity.getResources().getDrawable(2131165542).mutate();
        } else {
            this.sendButtonDrawable = activity.getResources().getDrawable(2131165498).mutate();
            this.sendButtonInverseDrawable = activity.getResources().getDrawable(2131165498).mutate();
            this.inactinveSendButtonDrawable = activity.getResources().getDrawable(2131165498).mutate();
        }
        AnonymousClass30 anonymousClass30 = new AnonymousClass30(activity);
        this.sendButton = anonymousClass30;
        anonymousClass30.setVisibility(4);
        int themedColor = getThemedColor("chat_messagePanelSend");
        this.sendButton.setContentDescription(LocaleController.getString("Send", 2131628175));
        this.sendButton.setSoundEffectsEnabled(false);
        this.sendButton.setScaleX(0.1f);
        this.sendButton.setScaleY(0.1f);
        this.sendButton.setAlpha(0.0f);
        if (i6 >= 21) {
            this.sendButton.setBackgroundDrawable(Theme.createSelectorDrawable(Color.argb(24, Color.red(themedColor), Color.green(themedColor), Color.blue(themedColor)), 1));
        }
        this.sendButtonContainer.addView(this.sendButton, LayoutHelper.createFrame(48, 48.0f));
        this.sendButton.setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda23(this));
        this.sendButton.setOnLongClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda27(this));
        SimpleTextView simpleTextView = new SimpleTextView(activity);
        this.slowModeButton = simpleTextView;
        simpleTextView.setTextSize(18);
        this.slowModeButton.setVisibility(4);
        this.slowModeButton.setSoundEffectsEnabled(false);
        this.slowModeButton.setScaleX(0.1f);
        this.slowModeButton.setScaleY(0.1f);
        this.slowModeButton.setAlpha(0.0f);
        this.slowModeButton.setPadding(0, 0, AndroidUtilities.dp(13.0f), 0);
        this.slowModeButton.setGravity(21);
        this.slowModeButton.setTextColor(getThemedColor("chat_messagePanelIcons"));
        this.sendButtonContainer.addView(this.slowModeButton, LayoutHelper.createFrame(64, 48, 53));
        this.slowModeButton.setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda20(this));
        this.slowModeButton.setOnLongClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda28(this));
        AnonymousClass31 anonymousClass31 = new AnonymousClass31(this, activity);
        this.expandStickersButton = anonymousClass31;
        anonymousClass31.setScaleType(ImageView.ScaleType.CENTER);
        ImageView imageView9 = this.expandStickersButton;
        AnimatedArrowDrawable animatedArrowDrawable = new AnimatedArrowDrawable(getThemedColor("chat_messagePanelIcons"), false);
        this.stickersArrow = animatedArrowDrawable;
        imageView9.setImageDrawable(animatedArrowDrawable);
        this.expandStickersButton.setVisibility(8);
        this.expandStickersButton.setScaleX(0.1f);
        this.expandStickersButton.setScaleY(0.1f);
        this.expandStickersButton.setAlpha(0.0f);
        if (i6 >= 21) {
            this.expandStickersButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(str)));
        }
        this.sendButtonContainer.addView(this.expandStickersButton, LayoutHelper.createFrame(48, 48.0f));
        this.expandStickersButton.setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda19(this));
        this.expandStickersButton.setContentDescription(LocaleController.getString("AccDescrExpandPanel", 2131623983));
        FrameLayout frameLayout3 = new FrameLayout(activity);
        this.doneButtonContainer = frameLayout3;
        frameLayout3.setVisibility(8);
        this.textFieldContainer.addView(this.doneButtonContainer, LayoutHelper.createFrame(48, 48, 85));
        this.doneButtonContainer.setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda15(this));
        ShapeDrawable createCircleDrawable = Theme.createCircleDrawable(AndroidUtilities.dp(16.0f), getThemedColor("chat_messagePanelSend"));
        Drawable mutate3 = activity.getResources().getDrawable(2131165533).mutate();
        this.doneCheckDrawable = mutate3;
        mutate3.setColorFilter(new PorterDuffColorFilter(getThemedColor("chat_messagePanelVoicePressed"), PorterDuff.Mode.MULTIPLY));
        CombinedDrawable combinedDrawable2 = new CombinedDrawable(createCircleDrawable, mutate3, 0, AndroidUtilities.dp(1.0f));
        combinedDrawable2.setCustomSize(AndroidUtilities.dp(32.0f), AndroidUtilities.dp(32.0f));
        ImageView imageView10 = new ImageView(activity);
        this.doneButtonImage = imageView10;
        imageView10.setScaleType(ImageView.ScaleType.CENTER);
        this.doneButtonImage.setImageDrawable(combinedDrawable2);
        this.doneButtonImage.setContentDescription(LocaleController.getString("Done", 2131625525));
        this.doneButtonContainer.addView(this.doneButtonImage, LayoutHelper.createFrame(48, 48.0f));
        ContextProgressView contextProgressView = new ContextProgressView(activity, 0);
        this.doneButtonProgress = contextProgressView;
        contextProgressView.setVisibility(4);
        this.doneButtonContainer.addView(this.doneButtonProgress, LayoutHelper.createFrame(-1, -1.0f));
        SharedPreferences globalEmojiSettings = MessagesController.getGlobalEmojiSettings();
        this.keyboardHeight = globalEmojiSettings.getInt("kbd_height", AndroidUtilities.dp(200.0f));
        this.keyboardHeightLand = globalEmojiSettings.getInt("kbd_height_land3", AndroidUtilities.dp(200.0f));
        setRecordVideoButtonVisible(false, false);
        checkSendButton(false);
        checkChannelRights();
        ChatActivityBotWebViewButton chatActivityBotWebViewButton = new ChatActivityBotWebViewButton(activity);
        this.botWebViewButton = chatActivityBotWebViewButton;
        chatActivityBotWebViewButton.setVisibility(8);
        this.botWebViewButton.setBotMenuButton(this.botCommandsMenuButton);
        anonymousClass10.addView(this.botWebViewButton, LayoutHelper.createFrame(-1, -1, 80));
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass9(Context context) {
            super(context);
            ChatActivityEnterView.this = r1;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (ChatActivityEnterView.this.botWebViewButton.getVisibility() == 0) {
                return ChatActivityEnterView.this.botWebViewButton.dispatchTouchEvent(motionEvent);
            }
            return super.dispatchTouchEvent(motionEvent);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass10(Context context) {
            super(context);
            ChatActivityEnterView.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (ChatActivityEnterView.this.scheduledButton != null) {
                int measuredWidth = (getMeasuredWidth() - AndroidUtilities.dp((ChatActivityEnterView.this.botButton == null || ChatActivityEnterView.this.botButton.getVisibility() != 0) ? 48.0f : 96.0f)) - AndroidUtilities.dp(48.0f);
                ChatActivityEnterView.this.scheduledButton.layout(measuredWidth, ChatActivityEnterView.this.scheduledButton.getTop(), ChatActivityEnterView.this.scheduledButton.getMeasuredWidth() + measuredWidth, ChatActivityEnterView.this.scheduledButton.getBottom());
            }
            if (!ChatActivityEnterView.this.animationParamsX.isEmpty()) {
                for (int i5 = 0; i5 < getChildCount(); i5++) {
                    View childAt = getChildAt(i5);
                    Float f = (Float) ChatActivityEnterView.this.animationParamsX.get(childAt);
                    if (f != null) {
                        childAt.setTranslationX(f.floatValue() - childAt.getLeft());
                        childAt.animate().translationX(0.0f).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                    }
                }
                ChatActivityEnterView.this.animationParamsX.clear();
            }
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (view == ChatActivityEnterView.this.messageEditText) {
                canvas.save();
                canvas.clipRect(0, ((-getTop()) - ChatActivityEnterView.this.textFieldContainer.getTop()) - ChatActivityEnterView.this.getTop(), getMeasuredWidth(), getMeasuredHeight() - AndroidUtilities.dp(6.0f));
                boolean drawChild = super.drawChild(canvas, view, j);
                canvas.restore();
                return drawChild;
            }
            return super.drawChild(canvas, view, j);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$11 */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 extends ImageView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass11(Context context) {
            super(context);
            ChatActivityEnterView.this = r1;
        }

        @Override // android.widget.ImageView, android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (getTag() == null || ChatActivityEnterView.this.attachLayout == null || ChatActivityEnterView.this.emojiViewVisible || MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).getUnreadStickerSets().isEmpty() || ChatActivityEnterView.this.dotPaint == null) {
                return;
            }
            canvas.drawCircle((getWidth() / 2) + AndroidUtilities.dp(9.0f), (getHeight() / 2) - AndroidUtilities.dp(8.0f), AndroidUtilities.dp(5.0f), ChatActivityEnterView.this.dotPaint);
        }
    }

    public /* synthetic */ void lambda$new$1(View view) {
        AdjustPanLayoutHelper adjustPanLayoutHelper = this.adjustPanLayoutHelper;
        if (adjustPanLayoutHelper == null || !adjustPanLayoutHelper.animationInProgress()) {
            if (hasBotWebView() && botCommandsMenuIsShowing()) {
                BotWebViewMenuContainer botWebViewMenuContainer = this.botWebViewMenuContainer;
                view.getClass();
                botWebViewMenuContainer.dismiss(new ChatActivityEnterView$$ExternalSyntheticLambda33(view));
                return;
            }
            boolean z = true;
            if (!isPopupShowing() || this.currentPopupContentType != 0) {
                showPopup(1, 0);
                EmojiView emojiView = this.emojiView;
                if (this.messageEditText.length() <= 0) {
                    z = false;
                }
                emojiView.onOpen(z);
                return;
            }
            if (this.searchingType != 0) {
                setSearchingTypeInternal(0, true);
                EmojiView emojiView2 = this.emojiView;
                if (emojiView2 != null) {
                    emojiView2.closeSearch(false);
                }
                this.messageEditText.requestFocus();
            }
            if (this.stickersExpanded) {
                setStickersExpanded(false, true, false);
                this.waitingForKeyboardOpenAfterAnimation = true;
                AndroidUtilities.runOnUIThread(new ChatActivityEnterView$$ExternalSyntheticLambda37(this), 200L);
                return;
            }
            openKeyboardInternal();
        }
    }

    public /* synthetic */ void lambda$new$0() {
        this.waitingForKeyboardOpenAfterAnimation = false;
        openKeyboardInternal();
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$12 */
    /* loaded from: classes3.dex */
    public class AnonymousClass12 extends EditTextCaption {
        final /* synthetic */ Activity val$context;
        final /* synthetic */ ChatActivity val$fragment;
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass12(Context context, Theme.ResourcesProvider resourcesProvider, Theme.ResourcesProvider resourcesProvider2, ChatActivity chatActivity, Activity activity) {
            super(context, resourcesProvider);
            ChatActivityEnterView.this = r1;
            this.val$resourcesProvider = resourcesProvider2;
            this.val$fragment = chatActivity;
            this.val$context = activity;
        }

        /* renamed from: send */
        public void lambda$onCreateInputConnection$0(InputContentInfoCompat inputContentInfoCompat, boolean z, int i) {
            if (inputContentInfoCompat.getDescription().hasMimeType("image/gif")) {
                SendMessagesHelper.prepareSendingDocument(ChatActivityEnterView.this.accountInstance, null, null, inputContentInfoCompat.getContentUri(), null, "image/gif", ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), inputContentInfoCompat, null, z, 0);
            } else {
                SendMessagesHelper.prepareSendingPhoto(ChatActivityEnterView.this.accountInstance, null, inputContentInfoCompat.getContentUri(), ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), null, null, null, inputContentInfoCompat, 0, null, z, 0);
            }
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onMessageSend(null, true, i);
            }
        }

        @Override // android.widget.TextView, android.view.View
        public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
            InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
            if (onCreateInputConnection == null) {
                return null;
            }
            try {
                EditorInfoCompat.setContentMimeTypes(editorInfo, new String[]{"image/gif", "image/*", "image/jpg", "image/png", "image/webp"});
                return InputConnectionCompat.createWrapper(onCreateInputConnection, editorInfo, new ChatActivityEnterView$12$$ExternalSyntheticLambda0(this, this.val$resourcesProvider));
            } catch (Throwable th) {
                FileLog.e(th);
                return onCreateInputConnection;
            }
        }

        public /* synthetic */ boolean lambda$onCreateInputConnection$1(Theme.ResourcesProvider resourcesProvider, InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle) {
            if (BuildCompat.isAtLeastNMR1() && (i & 1) != 0) {
                try {
                    inputContentInfoCompat.requestPermission();
                } catch (Exception unused) {
                    return false;
                }
            }
            if (inputContentInfoCompat.getDescription().hasMimeType("image/gif") || SendMessagesHelper.shouldSendWebPAsSticker(null, inputContentInfoCompat.getContentUri())) {
                if (ChatActivityEnterView.this.isInScheduleMode()) {
                    AlertsCreator.createScheduleDatePickerDialog(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.parentFragment.getDialogId(), new ChatActivityEnterView$12$$ExternalSyntheticLambda4(this, inputContentInfoCompat), resourcesProvider);
                } else {
                    lambda$onCreateInputConnection$0(inputContentInfoCompat, true, 0);
                }
            } else {
                editPhoto(inputContentInfoCompat.getContentUri(), inputContentInfoCompat.getDescription().getMimeType(0));
            }
            return true;
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (!ChatActivityEnterView.this.stickersDragging && ChatActivityEnterView.this.stickersExpansionAnim == null) {
                if (ChatActivityEnterView.this.isPopupShowing() && motionEvent.getAction() == 0) {
                    if (ChatActivityEnterView.this.searchingType != 0) {
                        ChatActivityEnterView.this.setSearchingTypeInternal(0, false);
                        ChatActivityEnterView.this.emojiView.closeSearch(false);
                        requestFocus();
                    }
                    ChatActivityEnterView.this.showPopup(AndroidUtilities.usingHardwareInput ? 0 : 2, 0);
                    if (!ChatActivityEnterView.this.stickersExpanded) {
                        ChatActivityEnterView.this.openKeyboardInternal();
                    } else {
                        ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                        ChatActivityEnterView.this.waitingForKeyboardOpenAfterAnimation = true;
                        AndroidUtilities.runOnUIThread(new ChatActivityEnterView$12$$ExternalSyntheticLambda1(this), 200L);
                    }
                    return true;
                }
                try {
                    return super.onTouchEvent(motionEvent);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            return false;
        }

        public /* synthetic */ void lambda$onTouchEvent$2() {
            ChatActivityEnterView.this.waitingForKeyboardOpenAfterAnimation = false;
            ChatActivityEnterView.this.openKeyboardInternal();
        }

        @Override // android.view.View
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            if (ChatActivityEnterView.this.preventInput) {
                return false;
            }
            return super.dispatchKeyEvent(keyEvent);
        }

        @Override // org.telegram.ui.Components.EditTextEffects, android.widget.TextView
        public void onSelectionChanged(int i, int i2) {
            super.onSelectionChanged(i, i2);
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onTextSelectionChanged(i, i2);
            }
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor
        protected void extendActionMode(ActionMode actionMode, Menu menu) {
            if (ChatActivityEnterView.this.parentFragment != null) {
                ChatActivityEnterView.this.parentFragment.extendActionMode(menu);
            }
        }

        @Override // android.view.View
        public boolean requestRectangleOnScreen(android.graphics.Rect rect) {
            rect.bottom += AndroidUtilities.dp(1000.0f);
            return super.requestRectangleOnScreen(rect);
        }

        @Override // org.telegram.ui.Components.EditTextCaption, org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
        public void onMeasure(int i, int i2) {
            ChatActivityEnterView.this.isInitLineCount = getMeasuredWidth() == 0 && getMeasuredHeight() == 0;
            super.onMeasure(i, i2);
            if (ChatActivityEnterView.this.isInitLineCount) {
                ChatActivityEnterView.this.lineCount = getLineCount();
            }
            ChatActivityEnterView.this.isInitLineCount = false;
        }

        @Override // android.widget.TextView
        public boolean onTextContextMenuItem(int i) {
            if (i == 16908322) {
                ChatActivityEnterView.this.isPaste = true;
            }
            ClipData primaryClip = ((ClipboardManager) getContext().getSystemService("clipboard")).getPrimaryClip();
            if (primaryClip != null && primaryClip.getItemCount() == 1 && primaryClip.getDescription().hasMimeType("image/*")) {
                editPhoto(primaryClip.getItemAt(0).getUri(), primaryClip.getDescription().getMimeType(0));
            }
            return super.onTextContextMenuItem(i);
        }

        private void editPhoto(Uri uri, String str) {
            ChatActivity chatActivity = this.val$fragment;
            Utilities.globalQueue.postRunnable(new ChatActivityEnterView$12$$ExternalSyntheticLambda2(this, this.val$context, uri, AndroidUtilities.generatePicturePath(chatActivity != null && chatActivity.isSecretChat(), MimeTypeMap.getSingleton().getExtensionFromMimeType(str))));
        }

        public /* synthetic */ void lambda$editPhoto$4(Activity activity, Uri uri, File file) {
            try {
                InputStream openInputStream = activity.getContentResolver().openInputStream(uri);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = openInputStream.read(bArr);
                    if (read > 0) {
                        fileOutputStream.write(bArr, 0, read);
                        fileOutputStream.flush();
                    } else {
                        openInputStream.close();
                        fileOutputStream.close();
                        MediaController.PhotoEntry photoEntry = new MediaController.PhotoEntry(0, -1, 0L, file.getAbsolutePath(), 0, false, 0, 0, 0L);
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(photoEntry);
                        AndroidUtilities.runOnUIThread(new ChatActivityEnterView$12$$ExternalSyntheticLambda3(this, arrayList, file));
                        return;
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        /* renamed from: openPhotoViewerForEdit */
        public void lambda$editPhoto$3(ArrayList<Object> arrayList, File file) {
            MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) arrayList.get(0);
            if (!ChatActivityEnterView.this.keyboardVisible) {
                PhotoViewer.getInstance().setParentActivity(ChatActivityEnterView.this.parentActivity, this.val$resourcesProvider);
                PhotoViewer.getInstance().openPhotoForSelect(arrayList, 0, 2, false, new AnonymousClass2(photoEntry, file), ChatActivityEnterView.this.parentFragment);
                return;
            }
            AndroidUtilities.hideKeyboard(ChatActivityEnterView.this.messageEditText);
            AndroidUtilities.runOnUIThread(new AnonymousClass1(arrayList, file), 100L);
        }

        /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$12$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 implements Runnable {
            final /* synthetic */ ArrayList val$entries;
            final /* synthetic */ File val$sourceFile;

            AnonymousClass1(ArrayList arrayList, File file) {
                AnonymousClass12.this = r1;
                this.val$entries = arrayList;
                this.val$sourceFile = file;
            }

            @Override // java.lang.Runnable
            public void run() {
                AnonymousClass12.this.lambda$editPhoto$3(this.val$entries, this.val$sourceFile);
            }
        }

        /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$12$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 extends PhotoViewer.EmptyPhotoViewerProvider {
            boolean sending;
            final /* synthetic */ MediaController.PhotoEntry val$photoEntry;
            final /* synthetic */ File val$sourceFile;

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public boolean canCaptureMorePhotos() {
                return false;
            }

            AnonymousClass2(MediaController.PhotoEntry photoEntry, File file) {
                AnonymousClass12.this = r1;
                this.val$photoEntry = photoEntry;
                this.val$sourceFile = file;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public void sendButtonPressed(int i, VideoEditedInfo videoEditedInfo, boolean z, int i2, boolean z2) {
                String str;
                ArrayList arrayList = new ArrayList();
                SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
                MediaController.PhotoEntry photoEntry = this.val$photoEntry;
                boolean z3 = photoEntry.isVideo;
                if (!z3 && (str = photoEntry.imagePath) != null) {
                    sendingMediaInfo.path = str;
                } else {
                    String str2 = photoEntry.path;
                    if (str2 != null) {
                        sendingMediaInfo.path = str2;
                    }
                }
                sendingMediaInfo.thumbPath = photoEntry.thumbPath;
                sendingMediaInfo.isVideo = z3;
                CharSequence charSequence = photoEntry.caption;
                sendingMediaInfo.caption = charSequence != null ? charSequence.toString() : null;
                MediaController.PhotoEntry photoEntry2 = this.val$photoEntry;
                sendingMediaInfo.entities = photoEntry2.entities;
                sendingMediaInfo.masks = photoEntry2.stickers;
                sendingMediaInfo.ttl = photoEntry2.ttl;
                sendingMediaInfo.videoEditedInfo = videoEditedInfo;
                sendingMediaInfo.canDeleteAfter = true;
                arrayList.add(sendingMediaInfo);
                this.val$photoEntry.reset();
                this.sending = true;
                SendMessagesHelper.prepareSendingMedia(ChatActivityEnterView.this.accountInstance, arrayList, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), null, false, false, ChatActivityEnterView.this.editingMessageObject, z, i2);
                if (ChatActivityEnterView.this.delegate != null) {
                    ChatActivityEnterView.this.delegate.onMessageSend(null, true, i2);
                }
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public void willHidePhotoViewer() {
                if (!this.sending) {
                    try {
                        this.val$sourceFile.delete();
                    } catch (Throwable unused) {
                    }
                }
            }
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor
        protected Theme.ResourcesProvider getResourcesProvider() {
            return this.val$resourcesProvider;
        }
    }

    public /* synthetic */ void lambda$new$2() {
        this.messageEditText.invalidateEffects();
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.onTextSpansChanged(this.messageEditText.getText());
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$13 */
    /* loaded from: classes3.dex */
    public class AnonymousClass13 implements View.OnKeyListener {
        boolean ctrlPressed = false;

        AnonymousClass13() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.view.View.OnKeyListener
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            boolean z = false;
            if (i == 4 && !ChatActivityEnterView.this.keyboardVisible && ChatActivityEnterView.this.isPopupShowing() && keyEvent.getAction() == 1) {
                if (!ContentPreviewViewer.hasInstance() || !ContentPreviewViewer.getInstance().isVisible()) {
                    if (ChatActivityEnterView.this.currentPopupContentType == 1 && ChatActivityEnterView.this.botButtonsMessageObject != null) {
                        return false;
                    }
                    if (keyEvent.getAction() == 1) {
                        if (ChatActivityEnterView.this.currentPopupContentType == 1 && ChatActivityEnterView.this.botButtonsMessageObject != null) {
                            SharedPreferences.Editor edit = MessagesController.getMainSettings(ChatActivityEnterView.this.currentAccount).edit();
                            edit.putInt("hidekeyboard_" + ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.botButtonsMessageObject.getId()).commit();
                        }
                        if (ChatActivityEnterView.this.searchingType != 0) {
                            ChatActivityEnterView.this.setSearchingTypeInternal(0, true);
                            if (ChatActivityEnterView.this.emojiView != null) {
                                ChatActivityEnterView.this.emojiView.closeSearch(true);
                            }
                            ChatActivityEnterView.this.messageEditText.requestFocus();
                        } else if (!ChatActivityEnterView.this.stickersExpanded) {
                            if (ChatActivityEnterView.this.stickersExpansionAnim == null) {
                                if (ChatActivityEnterView.this.botButtonsMessageObject == null || ChatActivityEnterView.this.currentPopupContentType == 1 || !TextUtils.isEmpty(ChatActivityEnterView.this.messageEditText.getText())) {
                                    ChatActivityEnterView.this.showPopup(0, 0);
                                } else {
                                    ChatActivityEnterView.this.showPopup(1, 1);
                                }
                            }
                        } else {
                            ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                        }
                    }
                    return true;
                }
                ContentPreviewViewer.getInstance().closeWithMenu();
                return true;
            } else if (i == 66 && ((this.ctrlPressed || ChatActivityEnterView.this.sendByEnter) && keyEvent.getAction() == 0 && ChatActivityEnterView.this.editingMessageObject == null)) {
                ChatActivityEnterView.this.sendMessage();
                return true;
            } else if (i != 113 && i != 114) {
                return false;
            } else {
                if (keyEvent.getAction() == 0) {
                    z = true;
                }
                this.ctrlPressed = z;
                return true;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$14 */
    /* loaded from: classes3.dex */
    public class AnonymousClass14 implements TextView.OnEditorActionListener {
        boolean ctrlPressed = false;

        AnonymousClass14() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.widget.TextView.OnEditorActionListener
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 4) {
                ChatActivityEnterView.this.sendMessage();
                return true;
            } else if (keyEvent == null || i != 0) {
                return false;
            } else {
                if ((!this.ctrlPressed && !ChatActivityEnterView.this.sendByEnter) || keyEvent.getAction() != 0 || ChatActivityEnterView.this.editingMessageObject != null) {
                    return false;
                }
                ChatActivityEnterView.this.sendMessage();
                return true;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$15 */
    /* loaded from: classes3.dex */
    public class AnonymousClass15 implements TextWatcher {
        private boolean ignorePrevTextChange;
        private boolean nextChangeIsSend;
        private CharSequence prevText;
        private boolean processChange;

        AnonymousClass15() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!this.ignorePrevTextChange && ChatActivityEnterView.this.recordingAudioVideo) {
                this.prevText = charSequence.toString();
            }
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (this.ignorePrevTextChange) {
                return;
            }
            if (ChatActivityEnterView.this.lineCount != ChatActivityEnterView.this.messageEditText.getLineCount()) {
                if (!ChatActivityEnterView.this.isInitLineCount && ChatActivityEnterView.this.messageEditText.getMeasuredWidth() > 0) {
                    ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                    chatActivityEnterView.onLineCountChanged(chatActivityEnterView.lineCount, ChatActivityEnterView.this.messageEditText.getLineCount());
                }
                ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
                chatActivityEnterView2.lineCount = chatActivityEnterView2.messageEditText.getLineCount();
            }
            if (ChatActivityEnterView.this.innerTextChange == 1) {
                return;
            }
            if (ChatActivityEnterView.this.sendByEnter && !ChatActivityEnterView.this.isPaste && ChatActivityEnterView.this.editingMessageObject == null && i3 > i2 && charSequence.length() > 0 && charSequence.length() == i + i3 && charSequence.charAt(charSequence.length() - 1) == '\n') {
                this.nextChangeIsSend = true;
            }
            boolean z = false;
            ChatActivityEnterView.this.isPaste = false;
            ChatActivityEnterView.this.checkSendButton(true);
            CharSequence trimmedString = AndroidUtilities.getTrimmedString(charSequence.toString());
            if (ChatActivityEnterView.this.delegate != null && !ChatActivityEnterView.this.ignoreTextChange) {
                int i4 = i3 + 1;
                if (i2 > i4 || i3 - i2 > 2 || TextUtils.isEmpty(charSequence)) {
                    ChatActivityEnterView.this.messageWebPageSearch = true;
                }
                ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = ChatActivityEnterView.this.delegate;
                if (i2 > i4 || i3 - i2 > 2) {
                    z = true;
                }
                chatActivityEnterViewDelegate.onTextChanged(charSequence, z);
            }
            if (ChatActivityEnterView.this.innerTextChange != 2 && i3 - i2 > 1) {
                this.processChange = true;
            }
            if (ChatActivityEnterView.this.editingMessageObject != null || ChatActivityEnterView.this.canWriteToChannel || trimmedString.length() == 0 || ChatActivityEnterView.this.lastTypingTimeSend >= System.currentTimeMillis() - 5000 || ChatActivityEnterView.this.ignoreTextChange) {
                return;
            }
            ChatActivityEnterView.this.lastTypingTimeSend = System.currentTimeMillis();
            if (ChatActivityEnterView.this.delegate == null) {
                return;
            }
            ChatActivityEnterView.this.delegate.needSendTyping();
        }

        /* JADX WARN: Removed duplicated region for block: B:43:0x015b  */
        /* JADX WARN: Removed duplicated region for block: B:56:0x01ab  */
        @Override // android.text.TextWatcher
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void afterTextChanged(Editable editable) {
            boolean z;
            ChatActivityEnterView chatActivityEnterView;
            BotCommandsMenuContainer botCommandsMenuContainer;
            int i;
            if (this.ignorePrevTextChange) {
                return;
            }
            if (this.prevText == null) {
                if (ChatActivityEnterView.this.innerTextChange == 0) {
                    if (this.nextChangeIsSend) {
                        ChatActivityEnterView.this.sendMessage();
                        this.nextChangeIsSend = false;
                    }
                    if (this.processChange) {
                        for (ImageSpan imageSpan : (ImageSpan[]) editable.getSpans(0, editable.length(), ImageSpan.class)) {
                            editable.removeSpan(imageSpan);
                        }
                        Emoji.replaceEmoji(editable, ChatActivityEnterView.this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                        this.processChange = false;
                    }
                }
                ChatActivityEnterView.this.codePointCount = Character.codePointCount(editable, 0, editable.length());
                float f = 0.0f;
                if (ChatActivityEnterView.this.currentLimit <= 0 || (i = ChatActivityEnterView.this.currentLimit - ChatActivityEnterView.this.codePointCount) > 100) {
                    ChatActivityEnterView.this.captionLimitView.animate().alpha(0.0f).scaleX(0.5f).scaleY(0.5f).setDuration(100L).setListener(new AnonymousClass1());
                } else {
                    if (i < -9999) {
                        i = -9999;
                    }
                    ChatActivityEnterView.this.captionLimitView.setNumber(i, ChatActivityEnterView.this.captionLimitView.getVisibility() == 0);
                    if (ChatActivityEnterView.this.captionLimitView.getVisibility() != 0) {
                        ChatActivityEnterView.this.captionLimitView.setVisibility(0);
                        ChatActivityEnterView.this.captionLimitView.setAlpha(0.0f);
                        ChatActivityEnterView.this.captionLimitView.setScaleX(0.5f);
                        ChatActivityEnterView.this.captionLimitView.setScaleY(0.5f);
                    }
                    ChatActivityEnterView.this.captionLimitView.animate().setListener(null).cancel();
                    ChatActivityEnterView.this.captionLimitView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(100L).start();
                    if (i < 0) {
                        ChatActivityEnterView.this.captionLimitView.setTextColor(ChatActivityEnterView.this.getThemedColor("windowBackgroundWhiteRedText"));
                        z = false;
                        chatActivityEnterView = ChatActivityEnterView.this;
                        if (chatActivityEnterView.doneButtonEnabled != z) {
                            chatActivityEnterView.doneButtonEnabled = z;
                            if (chatActivityEnterView.doneButtonColorAnimator != null) {
                                ChatActivityEnterView.this.doneButtonColorAnimator.cancel();
                            }
                            ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
                            float[] fArr = new float[2];
                            boolean z2 = chatActivityEnterView2.doneButtonEnabled;
                            fArr[0] = z2 ? 0.0f : 1.0f;
                            if (z2) {
                                f = 1.0f;
                            }
                            fArr[1] = f;
                            chatActivityEnterView2.doneButtonColorAnimator = ValueAnimator.ofFloat(fArr);
                            ChatActivityEnterView.this.doneButtonColorAnimator.addUpdateListener(new ChatActivityEnterView$15$$ExternalSyntheticLambda0(this));
                            ChatActivityEnterView.this.doneButtonColorAnimator.setDuration(150L).start();
                        }
                        botCommandsMenuContainer = ChatActivityEnterView.this.botCommandsMenuContainer;
                        if (botCommandsMenuContainer != null) {
                            botCommandsMenuContainer.dismiss();
                        }
                        ChatActivityEnterView.this.checkBotMenu();
                        return;
                    }
                    ChatActivityEnterView.this.captionLimitView.setTextColor(ChatActivityEnterView.this.getThemedColor("windowBackgroundWhiteGrayText"));
                }
                z = true;
                chatActivityEnterView = ChatActivityEnterView.this;
                if (chatActivityEnterView.doneButtonEnabled != z) {
                }
                botCommandsMenuContainer = ChatActivityEnterView.this.botCommandsMenuContainer;
                if (botCommandsMenuContainer != null) {
                }
                ChatActivityEnterView.this.checkBotMenu();
                return;
            }
            this.ignorePrevTextChange = true;
            editable.replace(0, editable.length(), this.prevText);
            this.prevText = null;
            this.ignorePrevTextChange = false;
        }

        /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$15$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends AnimatorListenerAdapter {
            AnonymousClass1() {
                AnonymousClass15.this = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ChatActivityEnterView.this.captionLimitView.setVisibility(8);
            }
        }

        public /* synthetic */ void lambda$afterTextChanged$0(ValueAnimator valueAnimator) {
            int themedColor = ChatActivityEnterView.this.getThemedColor("chat_messagePanelVoicePressed");
            int alpha = Color.alpha(themedColor);
            ChatActivityEnterView.this.doneButtonEnabledProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            ChatActivityEnterView.this.doneCheckDrawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.setAlphaComponent(themedColor, (int) (alpha * ((ChatActivityEnterView.this.doneButtonEnabledProgress * 0.42f) + 0.58f))), PorterDuff.Mode.MULTIPLY));
            ChatActivityEnterView.this.doneButtonImage.invalidate();
        }
    }

    public /* synthetic */ void lambda$new$3(View view) {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.openScheduledMessages();
        }
    }

    public /* synthetic */ void lambda$new$4(View view) {
        boolean z = !this.botCommandsMenuButton.isOpened();
        this.botCommandsMenuButton.setOpened(z);
        try {
            performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        if (!hasBotWebView()) {
            if (z) {
                this.botCommandsMenuContainer.show();
            } else {
                this.botCommandsMenuContainer.dismiss();
            }
        } else if (z) {
            if (this.emojiViewVisible || this.botKeyboardViewVisible) {
                AndroidUtilities.runOnUIThread(new ChatActivityEnterView$$ExternalSyntheticLambda42(this), 275L);
                hidePopup(false);
                return;
            }
            openWebViewMenu();
        } else {
            this.botWebViewMenuContainer.dismiss();
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$16 */
    /* loaded from: classes3.dex */
    public class AnonymousClass16 extends BotCommandsMenuContainer {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass16(Context context) {
            super(context);
            ChatActivityEnterView.this = r1;
        }

        @Override // org.telegram.ui.Components.BotCommandsMenuContainer
        public void onDismiss() {
            super.onDismiss();
            ChatActivityEnterView.this.botCommandsMenuButton.setOpened(false);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$17 */
    /* loaded from: classes3.dex */
    public class AnonymousClass17 implements RecyclerListView.OnItemClickListener {
        final /* synthetic */ ChatActivity val$fragment;
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;

        AnonymousClass17(Theme.ResourcesProvider resourcesProvider, ChatActivity chatActivity) {
            ChatActivityEnterView.this = r1;
            this.val$resourcesProvider = resourcesProvider;
            this.val$fragment = chatActivity;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
        public void onItemClick(View view, int i) {
            if (view instanceof BotCommandsMenuView.BotCommandView) {
                String command = ((BotCommandsMenuView.BotCommandView) view).getCommand();
                if (TextUtils.isEmpty(command)) {
                    return;
                }
                if (ChatActivityEnterView.this.isInScheduleMode()) {
                    AlertsCreator.createScheduleDatePickerDialog(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.dialog_id, new ChatActivityEnterView$17$$ExternalSyntheticLambda0(this, command), this.val$resourcesProvider);
                    return;
                }
                ChatActivity chatActivity = this.val$fragment;
                if (chatActivity != null && chatActivity.checkSlowMode(view)) {
                    return;
                }
                SendMessagesHelper.getInstance(ChatActivityEnterView.this.currentAccount).sendMessage(command, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), null, false, null, null, null, true, 0, null);
                ChatActivityEnterView.this.setFieldText("");
                ChatActivityEnterView.this.botCommandsMenuContainer.dismiss();
            }
        }

        public /* synthetic */ void lambda$onItemClick$0(String str, boolean z, int i) {
            SendMessagesHelper.getInstance(ChatActivityEnterView.this.currentAccount).sendMessage(str, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), null, false, null, null, null, z, i, null);
            ChatActivityEnterView.this.setFieldText("");
            ChatActivityEnterView.this.botCommandsMenuContainer.dismiss();
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$18 */
    /* loaded from: classes3.dex */
    public class AnonymousClass18 implements RecyclerListView.OnItemLongClickListener {
        AnonymousClass18() {
            ChatActivityEnterView.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
        public boolean onItemClick(View view, int i) {
            if (view instanceof BotCommandsMenuView.BotCommandView) {
                String command = ((BotCommandsMenuView.BotCommandView) view).getCommand();
                ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                chatActivityEnterView.setFieldText(command + " ");
                ChatActivityEnterView.this.botCommandsMenuContainer.dismiss();
                return true;
            }
            return false;
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$19 */
    /* loaded from: classes3.dex */
    public class AnonymousClass19 extends BotWebViewMenuContainer {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass19(Context context, ChatActivityEnterView chatActivityEnterView) {
            super(context, chatActivityEnterView);
            ChatActivityEnterView.this = r1;
        }

        @Override // org.telegram.ui.Components.BotWebViewMenuContainer
        public void onDismiss() {
            super.onDismiss();
            ChatActivityEnterView.this.botCommandsMenuButton.setOpened(false);
        }
    }

    public /* synthetic */ void lambda$new$5() {
        if (this.botButtonsMessageObject == null || !TextUtils.isEmpty(this.messageEditText.getText()) || this.botWebViewMenuContainer.hasSavedText()) {
            return;
        }
        showPopup(1, 1);
    }

    public /* synthetic */ void lambda$new$6(View view) {
        if (hasBotWebView() && botCommandsMenuIsShowing()) {
            BotWebViewMenuContainer botWebViewMenuContainer = this.botWebViewMenuContainer;
            view.getClass();
            botWebViewMenuContainer.dismiss(new ChatActivityEnterView$$ExternalSyntheticLambda33(view));
            return;
        }
        if (this.searchingType != 0) {
            setSearchingTypeInternal(0, false);
            this.emojiView.closeSearch(false);
            this.messageEditText.requestFocus();
        }
        if (this.botReplyMarkup != null) {
            if (!isPopupShowing() || this.currentPopupContentType != 1) {
                showPopup(1, 1);
            }
        } else if (this.hasBotCommands) {
            setFieldText("/");
            this.messageEditText.requestFocus();
            openKeyboard();
        }
        if (!this.stickersExpanded) {
            return;
        }
        setStickersExpanded(false, false, false);
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$20 */
    /* loaded from: classes3.dex */
    public class AnonymousClass20 implements View.OnClickListener {
        final /* synthetic */ Activity val$context;
        final /* synthetic */ ChatActivity val$fragment;
        private Toast visibleToast;

        AnonymousClass20(Activity activity, ChatActivity chatActivity) {
            ChatActivityEnterView.this = r1;
            this.val$context = activity;
            this.val$fragment = chatActivity;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String str;
            int i;
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            chatActivityEnterView.silent = !chatActivityEnterView.silent;
            if (ChatActivityEnterView.this.notifySilentDrawable == null) {
                ChatActivityEnterView.this.notifySilentDrawable = new CrossOutDrawable(this.val$context, 2131165540, "chat_messagePanelIcons");
            }
            ChatActivityEnterView.this.notifySilentDrawable.setCrossOut(ChatActivityEnterView.this.silent, true);
            ChatActivityEnterView.this.notifyButton.setImageDrawable(ChatActivityEnterView.this.notifySilentDrawable);
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(ChatActivityEnterView.this.currentAccount).edit();
            edit.putBoolean("silent_" + ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.silent).commit();
            NotificationsController.getInstance(ChatActivityEnterView.this.currentAccount).updateServerNotificationsSettings(ChatActivityEnterView.this.dialog_id);
            try {
                Toast toast = this.visibleToast;
                if (toast != null) {
                    toast.cancel();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            this.val$fragment.getUndoView().showWithAction(0L, !ChatActivityEnterView.this.silent ? 54 : 55, (Runnable) null);
            ImageView imageView = ChatActivityEnterView.this.notifyButton;
            if (ChatActivityEnterView.this.silent) {
                i = 2131623973;
                str = "AccDescrChanSilentOn";
            } else {
                i = 2131623972;
                str = "AccDescrChanSilentOff";
            }
            imageView.setContentDescription(LocaleController.getString(str, i));
            ChatActivityEnterView.this.updateFieldHint(true);
        }
    }

    public /* synthetic */ void lambda$new$7(View view) {
        AdjustPanLayoutHelper adjustPanLayoutHelper = this.adjustPanLayoutHelper;
        if (adjustPanLayoutHelper == null || !adjustPanLayoutHelper.animationInProgress()) {
            this.delegate.didPressAttachButton();
        }
    }

    public /* synthetic */ void lambda$new$14(Activity activity, View view) {
        int i;
        int i2;
        if (getTranslationY() != 0.0f) {
            this.onEmojiSearchClosed = new ChatActivityEnterView$$ExternalSyntheticLambda40(this);
            hidePopup(true, true);
            return;
        }
        if (this.delegate.measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
            int contentViewHeight = this.delegate.getContentViewHeight();
            int measureKeyboardHeight = this.delegate.measureKeyboardHeight();
            if (measureKeyboardHeight <= AndroidUtilities.dp(20.0f)) {
                contentViewHeight += measureKeyboardHeight;
            }
            if (this.emojiViewVisible) {
                contentViewHeight -= getEmojiPadding();
            }
            if (contentViewHeight < AndroidUtilities.dp(200.0f)) {
                this.onKeyboardClosed = new ChatActivityEnterView$$ExternalSyntheticLambda41(this);
                closeKeyboard();
                return;
            }
        }
        if (this.delegate.getSendAsPeers() == null) {
            return;
        }
        try {
            view.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        SenderSelectPopup senderSelectPopup = this.senderSelectPopupWindow;
        int i3 = 0;
        if (senderSelectPopup != null) {
            senderSelectPopup.setPauseNotifications(false);
            this.senderSelectPopupWindow.startDismissAnimation(new SpringAnimation[0]);
            return;
        }
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        TLRPC$ChatFull chatFull = messagesController.getChatFull(-this.dialog_id);
        if (chatFull == null) {
            return;
        }
        ActionBarLayout parentLayout = this.parentFragment.getParentLayout();
        AnonymousClass21 anonymousClass21 = new AnonymousClass21(activity, this.parentFragment, messagesController, chatFull, this.delegate.getSendAsPeers(), new ChatActivityEnterView$$ExternalSyntheticLambda59(this, chatFull, messagesController), parentLayout);
        this.senderSelectPopupWindow = anonymousClass21;
        anonymousClass21.setPauseNotifications(true);
        this.senderSelectPopupWindow.setDismissAnimationDuration(220);
        this.senderSelectPopupWindow.setOutsideTouchable(true);
        this.senderSelectPopupWindow.setClippingEnabled(true);
        this.senderSelectPopupWindow.setFocusable(true);
        this.senderSelectPopupWindow.getContentView().measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        this.senderSelectPopupWindow.setInputMethodMode(2);
        this.senderSelectPopupWindow.setSoftInputMode(0);
        this.senderSelectPopupWindow.getContentView().setFocusableInTouchMode(true);
        this.senderSelectPopupWindow.setAnimationEnabled(false);
        int i4 = -AndroidUtilities.dp(4.0f);
        int[] iArr = new int[2];
        if (AndroidUtilities.isTablet()) {
            this.parentFragment.getFragmentView().getLocationInWindow(iArr);
            i = iArr[0] + i4;
        } else {
            i = i4;
        }
        int contentViewHeight2 = this.delegate.getContentViewHeight();
        int measuredHeight = this.senderSelectPopupWindow.getContentView().getMeasuredHeight();
        int measureKeyboardHeight2 = this.delegate.measureKeyboardHeight();
        if (measureKeyboardHeight2 <= AndroidUtilities.dp(20.0f)) {
            contentViewHeight2 += measureKeyboardHeight2;
        }
        if (this.emojiViewVisible) {
            contentViewHeight2 -= getEmojiPadding();
        }
        int dp = AndroidUtilities.dp(1.0f);
        if (measuredHeight < (((i4 * 2) + contentViewHeight2) - (this.parentFragment.isInBubbleMode() ? 0 : AndroidUtilities.statusBarHeight)) - this.senderSelectPopupWindow.headerText.getMeasuredHeight()) {
            getLocationInWindow(iArr);
            i2 = ((iArr[1] - measuredHeight) - i4) - AndroidUtilities.dp(2.0f);
            parentLayout.addView(this.senderSelectPopupWindow.dimView, new FrameLayout.LayoutParams(-1, i4 + i2 + measuredHeight + dp + AndroidUtilities.dp(2.0f)));
        } else {
            if (!this.parentFragment.isInBubbleMode()) {
                i3 = AndroidUtilities.statusBarHeight;
            }
            int dp2 = AndroidUtilities.dp(14.0f);
            this.senderSelectPopupWindow.recyclerContainer.getLayoutParams().height = ((contentViewHeight2 - i3) - dp2) - getHeightWithTopView();
            parentLayout.addView(this.senderSelectPopupWindow.dimView, new FrameLayout.LayoutParams(-1, dp2 + i3 + this.senderSelectPopupWindow.recyclerContainer.getLayoutParams().height + dp));
            i2 = i3;
        }
        this.senderSelectPopupWindow.startShowAnimation();
        SenderSelectPopup senderSelectPopup2 = this.senderSelectPopupWindow;
        this.popupX = i;
        this.popupY = i2;
        senderSelectPopup2.showAtLocation(view, 51, i, i2);
        this.senderSelectView.setProgress(1.0f);
    }

    public /* synthetic */ void lambda$new$8() {
        this.senderSelectView.callOnClick();
    }

    public /* synthetic */ void lambda$new$9() {
        this.senderSelectView.callOnClick();
    }

    public /* synthetic */ void lambda$new$13(TLRPC$ChatFull tLRPC$ChatFull, MessagesController messagesController, RecyclerView recyclerView, SenderSelectPopup.SenderView senderView, TLRPC$Peer tLRPC$Peer) {
        TLRPC$User user;
        if (this.senderSelectPopupWindow == null) {
            return;
        }
        if (tLRPC$ChatFull != null) {
            tLRPC$ChatFull.default_send_as = tLRPC$Peer;
            updateSendAsButton();
        }
        MessagesController messagesController2 = this.parentFragment.getMessagesController();
        long j = this.dialog_id;
        long j2 = tLRPC$Peer.user_id;
        long j3 = 0;
        if (j2 == 0) {
            j2 = -tLRPC$Peer.channel_id;
        }
        messagesController2.setDefaultSendAs(j, j2);
        int[] iArr = new int[2];
        boolean isSelected = senderView.avatar.isSelected();
        senderView.avatar.getLocationInWindow(iArr);
        senderView.avatar.setSelected(true, true);
        SimpleAvatarView simpleAvatarView = new SimpleAvatarView(getContext());
        long j4 = tLRPC$Peer.channel_id;
        if (j4 != 0) {
            TLRPC$Chat chat = messagesController.getChat(Long.valueOf(j4));
            if (chat != null) {
                simpleAvatarView.setAvatar(chat);
            }
        } else {
            long j5 = tLRPC$Peer.user_id;
            if (j5 != 0 && (user = messagesController.getUser(Long.valueOf(j5))) != null) {
                simpleAvatarView.setAvatar(user);
            }
        }
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View childAt = recyclerView.getChildAt(i);
            if ((childAt instanceof SenderSelectPopup.SenderView) && childAt != senderView) {
                ((SenderSelectPopup.SenderView) childAt).avatar.setSelected(false, true);
            }
        }
        ChatActivityEnterView$$ExternalSyntheticLambda49 chatActivityEnterView$$ExternalSyntheticLambda49 = new ChatActivityEnterView$$ExternalSyntheticLambda49(this, simpleAvatarView, iArr, senderView);
        if (!isSelected) {
            j3 = 200;
        }
        AndroidUtilities.runOnUIThread(chatActivityEnterView$$ExternalSyntheticLambda49, j3);
    }

    public /* synthetic */ void lambda$new$12(SimpleAvatarView simpleAvatarView, int[] iArr, SenderSelectPopup.SenderView senderView) {
        if (this.senderSelectPopupWindow == null) {
            return;
        }
        Dialog dialog = new Dialog(getContext(), 2131689511);
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.addView(simpleAvatarView, LayoutHelper.createFrame(40, 40, 3));
        dialog.setContentView(frameLayout);
        dialog.getWindow().setLayout(-1, -1);
        int i = Build.VERSION.SDK_INT;
        if (i >= 21) {
            dialog.getWindow().clearFlags(1024);
            dialog.getWindow().clearFlags(67108864);
            dialog.getWindow().clearFlags(134217728);
            dialog.getWindow().addFlags(Integer.MIN_VALUE);
            dialog.getWindow().addFlags(512);
            dialog.getWindow().addFlags(131072);
            dialog.getWindow().getAttributes().windowAnimations = 0;
            dialog.getWindow().getDecorView().setSystemUiVisibility(1792);
            dialog.getWindow().setStatusBarColor(0);
            dialog.getWindow().setNavigationBarColor(0);
            AndroidUtilities.setLightStatusBar(dialog.getWindow(), Theme.getColor("actionBarDefault", null, true) == -1);
            if (i >= 26) {
                AndroidUtilities.setLightNavigationBar(dialog.getWindow(), AndroidUtilities.computePerceivedBrightness(Theme.getColor("windowBackgroundGray", null, true)) >= 0.721f);
            }
        }
        if (i >= 23) {
            this.popupX += getRootWindowInsets().getSystemWindowInsetLeft();
        }
        this.senderSelectView.getLocationInWindow(this.location);
        int[] iArr2 = this.location;
        float f = iArr2[0];
        float f2 = iArr2[1];
        float dp = AndroidUtilities.dp(5.0f);
        float dp2 = iArr[0] + this.popupX + dp + AndroidUtilities.dp(4.0f) + 0.0f;
        float f3 = iArr[1] + this.popupY + dp + 0.0f;
        simpleAvatarView.setTranslationX(dp2);
        simpleAvatarView.setTranslationY(f3);
        float dp3 = this.senderSelectView.getLayoutParams().width / AndroidUtilities.dp(40.0f);
        simpleAvatarView.setPivotX(0.0f);
        simpleAvatarView.setPivotY(0.0f);
        simpleAvatarView.setScaleX(0.75f);
        simpleAvatarView.setScaleY(0.75f);
        simpleAvatarView.getViewTreeObserver().addOnDrawListener(new AnonymousClass22(this, simpleAvatarView, senderView));
        dialog.show();
        this.senderSelectView.setScaleX(1.0f);
        this.senderSelectView.setScaleY(1.0f);
        this.senderSelectView.setAlpha(1.0f);
        SenderSelectPopup senderSelectPopup = this.senderSelectPopupWindow;
        SenderSelectView senderSelectView = this.senderSelectView;
        DynamicAnimation.ViewProperty viewProperty = DynamicAnimation.SCALE_X;
        SenderSelectView senderSelectView2 = this.senderSelectView;
        DynamicAnimation.ViewProperty viewProperty2 = DynamicAnimation.SCALE_Y;
        senderSelectPopup.startDismissAnimation(new SpringAnimation(senderSelectView, viewProperty).setSpring(new SpringForce(0.5f).setStiffness(750.0f).setDampingRatio(1.0f)), new SpringAnimation(senderSelectView2, viewProperty2).setSpring(new SpringForce(0.5f).setStiffness(750.0f).setDampingRatio(1.0f)), new SpringAnimation(this.senderSelectView, DynamicAnimation.ALPHA).setSpring(new SpringForce(0.0f).setStiffness(750.0f).setDampingRatio(1.0f)).addEndListener(new ChatActivityEnterView$$ExternalSyntheticLambda32(this, dialog, simpleAvatarView, f, f2)), new SpringAnimation(simpleAvatarView, DynamicAnimation.TRANSLATION_X).setStartValue(MathUtils.clamp(dp2, f - AndroidUtilities.dp(6.0f), dp2)).setSpring(new SpringForce(f).setStiffness(700.0f).setDampingRatio(0.75f)).setMinValue(f - AndroidUtilities.dp(6.0f)), new SpringAnimation(simpleAvatarView, DynamicAnimation.TRANSLATION_Y).setStartValue(MathUtils.clamp(f3, f3, AndroidUtilities.dp(6.0f) + f2)).setSpring(new SpringForce(f2).setStiffness(700.0f).setDampingRatio(0.75f)).setMaxValue(AndroidUtilities.dp(6.0f) + f2).addUpdateListener(new AnonymousClass24(this, f2, simpleAvatarView)).addEndListener(new ChatActivityEnterView$$ExternalSyntheticLambda31(this, dialog, simpleAvatarView, f, f2)), new SpringAnimation(simpleAvatarView, viewProperty).setSpring(new SpringForce(dp3).setStiffness(1000.0f).setDampingRatio(1.0f)), new SpringAnimation(simpleAvatarView, viewProperty2).setSpring(new SpringForce(dp3).setStiffness(1000.0f).setDampingRatio(1.0f)));
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$22 */
    /* loaded from: classes3.dex */
    public class AnonymousClass22 implements ViewTreeObserver.OnDrawListener {
        final /* synthetic */ SimpleAvatarView val$avatar;
        final /* synthetic */ SenderSelectPopup.SenderView val$senderView;

        AnonymousClass22(ChatActivityEnterView chatActivityEnterView, SimpleAvatarView simpleAvatarView, SenderSelectPopup.SenderView senderView) {
            this.val$avatar = simpleAvatarView;
            this.val$senderView = senderView;
        }

        @Override // android.view.ViewTreeObserver.OnDrawListener
        public void onDraw() {
            SimpleAvatarView simpleAvatarView = this.val$avatar;
            simpleAvatarView.post(new ChatActivityEnterView$22$$ExternalSyntheticLambda0(this, simpleAvatarView, this.val$senderView));
        }

        public /* synthetic */ void lambda$onDraw$0(SimpleAvatarView simpleAvatarView, SenderSelectPopup.SenderView senderView) {
            simpleAvatarView.getViewTreeObserver().removeOnDrawListener(this);
            senderView.avatar.setHideAvatar(true);
        }
    }

    public /* synthetic */ void lambda$new$10(Dialog dialog, SimpleAvatarView simpleAvatarView, float f, float f2, DynamicAnimation dynamicAnimation, boolean z, float f3, float f4) {
        if (dialog.isShowing()) {
            simpleAvatarView.setTranslationX(f);
            simpleAvatarView.setTranslationY(f2);
            this.senderSelectView.setProgress(0.0f, false);
            this.senderSelectView.setScaleX(1.0f);
            this.senderSelectView.setScaleY(1.0f);
            this.senderSelectView.setAlpha(1.0f);
            this.senderSelectView.getViewTreeObserver().addOnPreDrawListener(new AnonymousClass23(dialog));
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$23 */
    /* loaded from: classes3.dex */
    public class AnonymousClass23 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ Dialog val$d;

        AnonymousClass23(Dialog dialog) {
            ChatActivityEnterView.this = r1;
            this.val$d = dialog;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            ChatActivityEnterView.this.senderSelectView.getViewTreeObserver().removeOnPreDrawListener(this);
            SenderSelectView senderSelectView = ChatActivityEnterView.this.senderSelectView;
            Dialog dialog = this.val$d;
            dialog.getClass();
            senderSelectView.postDelayed(new ChatActivityEnterView$23$$ExternalSyntheticLambda0(dialog), 100L);
            return true;
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$24 */
    /* loaded from: classes3.dex */
    public class AnonymousClass24 implements DynamicAnimation.OnAnimationUpdateListener {
        boolean performedHapticFeedback = false;
        final /* synthetic */ SimpleAvatarView val$avatar;
        final /* synthetic */ float val$endY;

        AnonymousClass24(ChatActivityEnterView chatActivityEnterView, float f, SimpleAvatarView simpleAvatarView) {
            this.val$endY = f;
            this.val$avatar = simpleAvatarView;
        }

        @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
            if (this.performedHapticFeedback || f < this.val$endY) {
                return;
            }
            this.performedHapticFeedback = true;
            try {
                this.val$avatar.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
        }
    }

    public /* synthetic */ void lambda$new$11(Dialog dialog, SimpleAvatarView simpleAvatarView, float f, float f2, DynamicAnimation dynamicAnimation, boolean z, float f3, float f4) {
        if (dialog.isShowing()) {
            simpleAvatarView.setTranslationX(f);
            simpleAvatarView.setTranslationY(f2);
            this.senderSelectView.setProgress(0.0f, false);
            this.senderSelectView.setScaleX(1.0f);
            this.senderSelectView.setScaleY(1.0f);
            this.senderSelectView.setAlpha(1.0f);
            this.senderSelectView.getViewTreeObserver().addOnPreDrawListener(new AnonymousClass25(dialog));
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$25 */
    /* loaded from: classes3.dex */
    public class AnonymousClass25 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ Dialog val$d;

        AnonymousClass25(Dialog dialog) {
            ChatActivityEnterView.this = r1;
            this.val$d = dialog;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            ChatActivityEnterView.this.senderSelectView.getViewTreeObserver().removeOnPreDrawListener(this);
            SenderSelectView senderSelectView = ChatActivityEnterView.this.senderSelectView;
            Dialog dialog = this.val$d;
            dialog.getClass();
            senderSelectView.postDelayed(new ChatActivityEnterView$23$$ExternalSyntheticLambda0(dialog), 100L);
            return true;
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$21 */
    /* loaded from: classes3.dex */
    public class AnonymousClass21 extends SenderSelectPopup {
        final /* synthetic */ ViewGroup val$fl;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass21(Context context, ChatActivity chatActivity, MessagesController messagesController, TLRPC$ChatFull tLRPC$ChatFull, TLRPC$TL_channels_sendAsPeers tLRPC$TL_channels_sendAsPeers, SenderSelectPopup.OnSelectCallback onSelectCallback, ViewGroup viewGroup) {
            super(context, chatActivity, messagesController, tLRPC$ChatFull, tLRPC$TL_channels_sendAsPeers, onSelectCallback);
            ChatActivityEnterView.this = r8;
            this.val$fl = viewGroup;
        }

        @Override // org.telegram.ui.Components.SenderSelectPopup, org.telegram.ui.ActionBar.ActionBarPopupWindow, android.widget.PopupWindow
        public void dismiss() {
            if (ChatActivityEnterView.this.senderSelectPopupWindow == this) {
                ChatActivityEnterView.this.senderSelectPopupWindow = null;
                if (!this.runningCustomSprings) {
                    startDismissAnimation(new SpringAnimation[0]);
                    ChatActivityEnterView.this.senderSelectView.setProgress(0.0f, true, true);
                    return;
                }
                for (SpringAnimation springAnimation : this.springAnimations) {
                    springAnimation.cancel();
                }
                this.springAnimations.clear();
                super.dismiss();
                return;
            }
            this.val$fl.removeView(this.dimView);
            super.dismiss();
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$26 */
    /* loaded from: classes3.dex */
    public class AnonymousClass26 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass26(Context context) {
            super(context);
            ChatActivityEnterView.this = r1;
        }

        @Override // android.view.View
        public void setVisibility(int i) {
            super.setVisibility(i);
            ChatActivityEnterView.this.updateSendAsButton();
        }
    }

    public /* synthetic */ void lambda$new$15(View view) {
        AnimatorSet animatorSet = this.runningAnimationAudio;
        if (animatorSet == null || !animatorSet.isRunning()) {
            if (this.videoToSendMessageObject != null) {
                CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
                this.delegate.needStartRecordVideo(2, true, 0);
            } else {
                MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                if (playingMessageObject != null && playingMessageObject == this.audioToSendMessageObject) {
                    MediaController.getInstance().cleanupPlayer(true, true);
                }
            }
            if (this.audioToSendPath != null) {
                new File(this.audioToSendPath).delete();
            }
            hideRecordedAudioPanel(false);
            checkSendButton(true);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$27 */
    /* loaded from: classes3.dex */
    public class AnonymousClass27 implements VideoTimelineView.VideoTimelineViewDelegate {
        AnonymousClass27() {
            ChatActivityEnterView.this = r1;
        }

        @Override // org.telegram.ui.Components.VideoTimelineView.VideoTimelineViewDelegate
        public void onLeftProgressChanged(float f) {
            if (ChatActivityEnterView.this.videoToSendMessageObject == null) {
                return;
            }
            ChatActivityEnterView.this.videoToSendMessageObject.startTime = ((float) ChatActivityEnterView.this.videoToSendMessageObject.estimatedDuration) * f;
            ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(2, f);
        }

        @Override // org.telegram.ui.Components.VideoTimelineView.VideoTimelineViewDelegate
        public void onRightProgressChanged(float f) {
            if (ChatActivityEnterView.this.videoToSendMessageObject == null) {
                return;
            }
            ChatActivityEnterView.this.videoToSendMessageObject.endTime = ((float) ChatActivityEnterView.this.videoToSendMessageObject.estimatedDuration) * f;
            ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(2, f);
        }

        @Override // org.telegram.ui.Components.VideoTimelineView.VideoTimelineViewDelegate
        public void didStartDragging() {
            ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(1, 0.0f);
        }

        @Override // org.telegram.ui.Components.VideoTimelineView.VideoTimelineViewDelegate
        public void didStopDragging() {
            ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(0, 0.0f);
        }
    }

    public /* synthetic */ void lambda$new$16(View view) {
        if (this.audioToSend == null) {
            return;
        }
        if (MediaController.getInstance().isPlayingMessage(this.audioToSendMessageObject) && !MediaController.getInstance().isMessagePaused()) {
            MediaController.getInstance().lambda$startAudioAgain$7(this.audioToSendMessageObject);
            this.playPauseDrawable.setIcon(0, true);
            this.recordedAudioPlayButton.setContentDescription(LocaleController.getString("AccActionPlay", 2131623955));
            return;
        }
        this.playPauseDrawable.setIcon(1, true);
        MediaController.getInstance().playMessage(this.audioToSendMessageObject);
        this.recordedAudioPlayButton.setContentDescription(LocaleController.getString("AccActionPause", 2131623954));
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$28 */
    /* loaded from: classes3.dex */
    public class AnonymousClass28 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass28(Context context) {
            super(context);
            ChatActivityEnterView.this = r1;
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (view != ChatActivityEnterView.this.sendButton || !ChatActivityEnterView.this.textTransitionIsRunning) {
                return super.drawChild(canvas, view, j);
            }
            return true;
        }
    }

    public /* synthetic */ boolean lambda$new$24(Theme.ResourcesProvider resourcesProvider, View view, MotionEvent motionEvent) {
        TLRPC$Chat currentChat;
        int i = 3;
        boolean z = false;
        if (motionEvent.getAction() == 0) {
            if (this.recordCircle.isSendButtonVisible()) {
                boolean z2 = this.hasRecordVideo;
                if (!z2 || this.calledRecordRunnable) {
                    this.startedDraggingX = -1.0f;
                    if (z2 && this.videoSendButton.getTag() != null) {
                        this.delegate.needStartRecordVideo(1, true, 0);
                    } else {
                        if (this.recordingAudioVideo && isInScheduleMode()) {
                            AlertsCreator.createScheduleDatePickerDialog(this.parentActivity, this.parentFragment.getDialogId(), ChatActivityEnterView$$ExternalSyntheticLambda55.INSTANCE, ChatActivityEnterView$$ExternalSyntheticLambda51.INSTANCE, resourcesProvider);
                        }
                        MediaController mediaController = MediaController.getInstance();
                        if (!isInScheduleMode()) {
                            i = 1;
                        }
                        mediaController.stopRecording(i, true, 0);
                        this.delegate.needStartRecordAudio(0);
                    }
                    this.recordingAudioVideo = false;
                    this.messageTransitionIsRunning = false;
                    ChatActivityEnterView$$ExternalSyntheticLambda36 chatActivityEnterView$$ExternalSyntheticLambda36 = new ChatActivityEnterView$$ExternalSyntheticLambda36(this);
                    this.moveToSendStateRunnable = chatActivityEnterView$$ExternalSyntheticLambda36;
                    AndroidUtilities.runOnUIThread(chatActivityEnterView$$ExternalSyntheticLambda36, 200L);
                }
                return false;
            }
            ChatActivity chatActivity = this.parentFragment;
            if (chatActivity != null && (currentChat = chatActivity.getCurrentChat()) != null && !ChatObject.canSendMedia(currentChat)) {
                this.delegate.needShowMediaBanHint();
                return false;
            }
            if (this.hasRecordVideo) {
                this.calledRecordRunnable = false;
                this.recordAudioVideoRunnableStarted = true;
                AndroidUtilities.runOnUIThread(this.recordAudioVideoRunnable, 150L);
            } else {
                this.recordAudioVideoRunnable.run();
            }
            return true;
        }
        float f = 1.0f;
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            if (motionEvent.getAction() == 3 && this.recordingAudioVideo) {
                if (this.recordCircle.slideToCancelProgress >= 0.7f) {
                    this.recordCircle.sendButtonVisible = true;
                    startLockTransition();
                } else {
                    if (this.hasRecordVideo && this.videoSendButton.getTag() != null) {
                        CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
                        this.delegate.needStartRecordVideo(2, true, 0);
                    } else {
                        this.delegate.needStartRecordAudio(0);
                        MediaController.getInstance().stopRecording(0, false, 0);
                    }
                    this.recordingAudioVideo = false;
                    updateRecordInterface(5);
                }
                return false;
            } else if (this.recordCircle.isSendButtonVisible() || this.recordedAudioPanel.getVisibility() == 0) {
                if (this.recordAudioVideoRunnableStarted) {
                    AndroidUtilities.cancelRunOnUIThread(this.recordAudioVideoRunnable);
                }
                return false;
            } else {
                if ((((motionEvent.getX() + this.audioVideoButtonContainer.getX()) - this.startedDraggingX) / this.distCanMove) + 1.0f < 0.45d) {
                    if (this.hasRecordVideo && this.videoSendButton.getTag() != null) {
                        CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
                        this.delegate.needStartRecordVideo(2, true, 0);
                    } else {
                        this.delegate.needStartRecordAudio(0);
                        MediaController.getInstance().stopRecording(0, false, 0);
                    }
                    this.recordingAudioVideo = false;
                    updateRecordInterface(5);
                } else if (this.recordAudioVideoRunnableStarted) {
                    AndroidUtilities.cancelRunOnUIThread(this.recordAudioVideoRunnable);
                    this.delegate.onSwitchRecordMode(this.videoSendButton.getTag() == null);
                    if (this.videoSendButton.getTag() == null) {
                        z = true;
                    }
                    setRecordVideoButtonVisible(z, true);
                    performHapticFeedback(3);
                    sendAccessibilityEvent(1);
                } else {
                    boolean z3 = this.hasRecordVideo;
                    if (!z3 || this.calledRecordRunnable) {
                        this.startedDraggingX = -1.0f;
                        if (z3 && this.videoSendButton.getTag() != null) {
                            CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
                            this.delegate.needStartRecordVideo(1, true, 0);
                        } else {
                            if (this.recordingAudioVideo && isInScheduleMode()) {
                                AlertsCreator.createScheduleDatePickerDialog(this.parentActivity, this.parentFragment.getDialogId(), ChatActivityEnterView$$ExternalSyntheticLambda56.INSTANCE, ChatActivityEnterView$$ExternalSyntheticLambda50.INSTANCE, resourcesProvider);
                            }
                            this.delegate.needStartRecordAudio(0);
                            MediaController mediaController2 = MediaController.getInstance();
                            if (!isInScheduleMode()) {
                                i = 1;
                            }
                            mediaController2.stopRecording(i, true, 0);
                        }
                        this.recordingAudioVideo = false;
                        this.messageTransitionIsRunning = false;
                        ChatActivityEnterView$$ExternalSyntheticLambda43 chatActivityEnterView$$ExternalSyntheticLambda43 = new ChatActivityEnterView$$ExternalSyntheticLambda43(this);
                        this.moveToSendStateRunnable = chatActivityEnterView$$ExternalSyntheticLambda43;
                        AndroidUtilities.runOnUIThread(chatActivityEnterView$$ExternalSyntheticLambda43, 500L);
                    }
                }
                return true;
            }
        } else if (motionEvent.getAction() == 2 && this.recordingAudioVideo) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (this.recordCircle.isSendButtonVisible()) {
                return false;
            }
            if (this.recordCircle.setLockTranslation(y) == 2) {
                startLockTransition();
                return false;
            }
            this.recordCircle.setMovingCords(x, y);
            if (this.startedDraggingX == -1.0f) {
                this.startedDraggingX = x;
                double measuredWidth = this.sizeNotifierLayout.getMeasuredWidth();
                Double.isNaN(measuredWidth);
                float f2 = (float) (measuredWidth * 0.35d);
                this.distCanMove = f2;
                if (f2 > AndroidUtilities.dp(140.0f)) {
                    this.distCanMove = AndroidUtilities.dp(140.0f);
                }
            }
            float x2 = x + this.audioVideoButtonContainer.getX();
            float f3 = this.startedDraggingX;
            float f4 = ((x2 - f3) / this.distCanMove) + 1.0f;
            if (f3 != -1.0f) {
                if (f4 <= 1.0f) {
                    f = f4 < 0.0f ? 0.0f : f4;
                }
                this.slideText.setSlideX(f);
                this.recordCircle.setSlideToCancelProgress(f);
                f4 = f;
            }
            if (f4 == 0.0f) {
                if (this.hasRecordVideo && this.videoSendButton.getTag() != null) {
                    CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
                    this.delegate.needStartRecordVideo(2, true, 0);
                } else {
                    this.delegate.needStartRecordAudio(0);
                    MediaController.getInstance().stopRecording(0, false, 0);
                }
                this.recordingAudioVideo = false;
                updateRecordInterface(5);
            }
            return true;
        } else {
            view.onTouchEvent(motionEvent);
            return true;
        }
    }

    public static /* synthetic */ void lambda$new$18(boolean z, int i) {
        MediaController.getInstance().stopRecording(1, z, i);
    }

    public static /* synthetic */ void lambda$new$19() {
        MediaController.getInstance().stopRecording(0, false, 0);
    }

    public /* synthetic */ void lambda$new$20() {
        this.moveToSendStateRunnable = null;
        updateRecordInterface(1);
    }

    public static /* synthetic */ void lambda$new$21(boolean z, int i) {
        MediaController.getInstance().stopRecording(1, z, i);
    }

    public static /* synthetic */ void lambda$new$22() {
        MediaController.getInstance().stopRecording(0, false, 0);
    }

    public /* synthetic */ void lambda$new$23() {
        this.moveToSendStateRunnable = null;
        updateRecordInterface(1);
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$29 */
    /* loaded from: classes3.dex */
    public class AnonymousClass29 extends CloseProgressDrawable2 {
        AnonymousClass29(ChatActivityEnterView chatActivityEnterView) {
        }

        @Override // org.telegram.ui.Components.CloseProgressDrawable2
        protected int getCurrentColor() {
            return Theme.getColor("chat_messagePanelCancelInlineBot");
        }
    }

    public /* synthetic */ void lambda$new$25(View view) {
        String obj = this.messageEditText.getText().toString();
        int indexOf = obj.indexOf(32);
        if (indexOf == -1 || indexOf == obj.length() - 1) {
            setFieldText("");
        } else {
            setFieldText(obj.substring(0, indexOf + 1));
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$30 */
    /* loaded from: classes3.dex */
    public class AnonymousClass30 extends View {
        private float animationDuration;
        private float animationProgress;
        private int drawableColor;
        private long lastAnimationTime;
        private int prevColorType;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass30(Context context) {
            super(context);
            ChatActivityEnterView.this = r1;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            int i;
            int dp;
            float f;
            float dp2;
            int measuredWidth = (getMeasuredWidth() - ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicWidth()) / 2;
            int measuredHeight = (getMeasuredHeight() - ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicHeight()) / 2;
            if (ChatActivityEnterView.this.isInScheduleMode()) {
                measuredHeight -= AndroidUtilities.dp(1.0f);
            } else {
                measuredWidth += AndroidUtilities.dp(2.0f);
            }
            int i2 = 1;
            boolean z = ChatActivityEnterView.this.sendPopupWindow != null && ChatActivityEnterView.this.sendPopupWindow.isShowing();
            if (z) {
                i = ChatActivityEnterView.this.getThemedColor("chat_messagePanelVoicePressed");
            } else {
                i = ChatActivityEnterView.this.getThemedColor("chat_messagePanelSend");
                i2 = 2;
            }
            if (i != this.drawableColor) {
                this.lastAnimationTime = SystemClock.elapsedRealtime();
                int i3 = this.prevColorType;
                if (i3 != 0 && i3 != i2) {
                    this.animationProgress = 0.0f;
                    if (z) {
                        this.animationDuration = 200.0f;
                    } else {
                        this.animationDuration = 120.0f;
                    }
                } else {
                    this.animationProgress = 1.0f;
                }
                this.prevColorType = i2;
                this.drawableColor = i;
                ChatActivityEnterView.this.sendButtonDrawable.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor("chat_messagePanelSend"), PorterDuff.Mode.MULTIPLY));
                int themedColor = ChatActivityEnterView.this.getThemedColor("chat_messagePanelIcons");
                ChatActivityEnterView.this.inactinveSendButtonDrawable.setColorFilter(new PorterDuffColorFilter(Color.argb(180, Color.red(themedColor), Color.green(themedColor), Color.blue(themedColor)), PorterDuff.Mode.MULTIPLY));
                ChatActivityEnterView.this.sendButtonInverseDrawable.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor("chat_messagePanelVoicePressed"), PorterDuff.Mode.MULTIPLY));
            }
            if (this.animationProgress < 1.0f) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                float f2 = this.animationProgress + (((float) (elapsedRealtime - this.lastAnimationTime)) / this.animationDuration);
                this.animationProgress = f2;
                if (f2 > 1.0f) {
                    this.animationProgress = 1.0f;
                }
                this.lastAnimationTime = elapsedRealtime;
                invalidate();
            }
            if (!z) {
                if (ChatActivityEnterView.this.slowModeTimer != Integer.MAX_VALUE || ChatActivityEnterView.this.isInScheduleMode()) {
                    ChatActivityEnterView.this.sendButtonDrawable.setBounds(measuredWidth, measuredHeight, ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicWidth() + measuredWidth, ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicHeight() + measuredHeight);
                    ChatActivityEnterView.this.sendButtonDrawable.draw(canvas);
                } else {
                    ChatActivityEnterView.this.inactinveSendButtonDrawable.setBounds(measuredWidth, measuredHeight, ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicWidth() + measuredWidth, ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicHeight() + measuredHeight);
                    ChatActivityEnterView.this.inactinveSendButtonDrawable.draw(canvas);
                }
            }
            if (z || this.animationProgress != 1.0f) {
                Theme.dialogs_onlineCirclePaint.setColor(ChatActivityEnterView.this.getThemedColor("chat_messagePanelSend"));
                int dp3 = AndroidUtilities.dp(20.0f);
                if (z) {
                    ChatActivityEnterView.this.sendButtonInverseDrawable.setAlpha(255);
                    float f3 = this.animationProgress;
                    if (f3 <= 0.25f) {
                        f = dp3;
                        dp2 = AndroidUtilities.dp(2.0f) * CubicBezierInterpolator.EASE_IN.getInterpolation(f3 / 0.25f);
                    } else {
                        float f4 = f3 - 0.25f;
                        if (f4 <= 0.5f) {
                            f = dp3;
                            dp2 = AndroidUtilities.dp(2.0f) - (AndroidUtilities.dp(3.0f) * CubicBezierInterpolator.EASE_IN.getInterpolation(f4 / 0.5f));
                        } else {
                            dp = (int) (dp3 + (-AndroidUtilities.dp(1.0f)) + (AndroidUtilities.dp(1.0f) * CubicBezierInterpolator.EASE_IN.getInterpolation((f4 - 0.5f) / 0.25f)));
                            dp3 = dp;
                        }
                    }
                    dp = (int) (f + dp2);
                    dp3 = dp;
                } else {
                    int i4 = (int) ((1.0f - this.animationProgress) * 255.0f);
                    Theme.dialogs_onlineCirclePaint.setAlpha(i4);
                    ChatActivityEnterView.this.sendButtonInverseDrawable.setAlpha(i4);
                }
                canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, dp3, Theme.dialogs_onlineCirclePaint);
                ChatActivityEnterView.this.sendButtonInverseDrawable.setBounds(measuredWidth, measuredHeight, ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicWidth() + measuredWidth, ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicHeight() + measuredHeight);
                ChatActivityEnterView.this.sendButtonInverseDrawable.draw(canvas);
            }
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (getAlpha() <= 0.0f) {
                return false;
            }
            return super.onTouchEvent(motionEvent);
        }
    }

    public /* synthetic */ void lambda$new$26(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow == null || !actionBarPopupWindow.isShowing()) {
            AnimatorSet animatorSet = this.runningAnimationAudio;
            if ((animatorSet != null && animatorSet.isRunning()) || this.moveToSendStateRunnable != null) {
                return;
            }
            sendMessage();
        }
    }

    public /* synthetic */ void lambda$new$27(View view) {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            SimpleTextView simpleTextView = this.slowModeButton;
            chatActivityEnterViewDelegate.onUpdateSlowModeButton(simpleTextView, true, simpleTextView.getText());
        }
    }

    public /* synthetic */ boolean lambda$new$28(View view) {
        if (this.messageEditText.length() == 0) {
            return false;
        }
        return onSendLongClick(view);
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$31 */
    /* loaded from: classes3.dex */
    public class AnonymousClass31 extends ImageView {
        AnonymousClass31(ChatActivityEnterView chatActivityEnterView, Context context) {
            super(context);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (getAlpha() <= 0.0f) {
                return false;
            }
            return super.onTouchEvent(motionEvent);
        }
    }

    public /* synthetic */ void lambda$new$29(View view) {
        EmojiView emojiView;
        if (this.expandStickersButton.getVisibility() == 0 && this.expandStickersButton.getAlpha() == 1.0f && !this.waitingForKeyboardOpen) {
            if (this.keyboardVisible && this.messageEditText.isFocused()) {
                return;
            }
            if (this.stickersExpanded) {
                if (this.searchingType != 0) {
                    setSearchingTypeInternal(0, true);
                    this.emojiView.closeSearch(true);
                    this.emojiView.hideSearchKeyboard();
                    if (this.emojiTabOpen) {
                        checkSendButton(true);
                    }
                } else if (!this.stickersDragging && (emojiView = this.emojiView) != null) {
                    emojiView.showSearchField(false);
                }
            } else if (!this.stickersDragging) {
                this.emojiView.showSearchField(true);
            }
            if (this.stickersDragging) {
                return;
            }
            setStickersExpanded(!this.stickersExpanded, true, false);
        }
    }

    public /* synthetic */ void lambda$new$30(View view) {
        doneEditingMessage();
    }

    public void openWebViewMenu() {
        ChatActivityEnterView$$ExternalSyntheticLambda35 chatActivityEnterView$$ExternalSyntheticLambda35 = new ChatActivityEnterView$$ExternalSyntheticLambda35(this);
        if (SharedPrefsHelper.isWebViewConfirmShown(this.currentAccount, this.dialog_id)) {
            chatActivityEnterView$$ExternalSyntheticLambda35.run();
        } else {
            new AlertDialog.Builder(this.parentFragment.getParentActivity()).setTitle(LocaleController.getString(2131624720)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString(2131624719, UserObject.getUserName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialog_id)))))).setPositiveButton(LocaleController.getString(2131627075), new ChatActivityEnterView$$ExternalSyntheticLambda8(this, chatActivityEnterView$$ExternalSyntheticLambda35)).setNegativeButton(LocaleController.getString(2131624819), null).setOnDismissListener(new ChatActivityEnterView$$ExternalSyntheticLambda11(this)).show();
        }
    }

    public /* synthetic */ void lambda$openWebViewMenu$31() {
        AndroidUtilities.hideKeyboard(this);
        if (AndroidUtilities.isTablet()) {
            BotWebViewSheet botWebViewSheet = new BotWebViewSheet(getContext(), this.parentFragment.getResourceProvider());
            botWebViewSheet.setParentActivity(this.parentActivity);
            int i = this.currentAccount;
            long j = this.dialog_id;
            botWebViewSheet.requestWebView(i, j, j, this.botMenuWebViewTitle, this.botMenuWebViewUrl, 2, 0, false);
            botWebViewSheet.show();
            this.botCommandsMenuButton.setOpened(false);
            return;
        }
        this.botWebViewMenuContainer.show(this.currentAccount, this.dialog_id, this.botMenuWebViewUrl);
    }

    public /* synthetic */ void lambda$openWebViewMenu$32(Runnable runnable, DialogInterface dialogInterface, int i) {
        runnable.run();
        SharedPrefsHelper.setWebViewConfirmShown(this.currentAccount, this.dialog_id, true);
    }

    public /* synthetic */ void lambda$openWebViewMenu$33(DialogInterface dialogInterface) {
        if (!SharedPrefsHelper.isWebViewConfirmShown(this.currentAccount, this.dialog_id)) {
            this.botCommandsMenuButton.setOpened(false);
        }
    }

    public void setBotWebViewButtonOffsetX(float f) {
        for (ImageView imageView : this.emojiButton) {
            imageView.setTranslationX(f);
        }
        this.messageEditText.setTranslationX(f);
        this.attachButton.setTranslationX(f);
        this.audioSendButton.setTranslationX(f);
        this.videoSendButton.setTranslationX(f);
        ImageView imageView2 = this.botButton;
        if (imageView2 != null) {
            imageView2.setTranslationX(f);
        }
    }

    public void setComposeShadowAlpha(float f) {
        this.composeShadowAlpha = f;
        invalidate();
    }

    public ChatActivityBotWebViewButton getBotWebViewButton() {
        return this.botWebViewButton;
    }

    public ChatActivity getParentFragment() {
        return this.parentFragment;
    }

    public void checkBotMenu() {
        BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
        if (botCommandsMenuView != null) {
            boolean z = botCommandsMenuView.expanded;
            botCommandsMenuView.setExpanded(TextUtils.isEmpty(this.messageEditText.getText()) && !this.keyboardVisible && !this.waitingForKeyboardOpen && !isPopupShowing(), true);
            if (z == this.botCommandsMenuButton.expanded) {
                return;
            }
            beginDelayedTransition();
        }
    }

    public void forceSmoothKeyboard(boolean z) {
        ChatActivity chatActivity;
        this.smoothKeyboard = z && SharedConfig.smoothKeyboard && !AndroidUtilities.isInMultiwindow && ((chatActivity = this.parentFragment) == null || !chatActivity.isInBubbleMode());
    }

    private void startLockTransition() {
        AnimatorSet animatorSet = new AnimatorSet();
        performHapticFeedback(3, 2);
        RecordCircle recordCircle = this.recordCircle;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(recordCircle, "lockAnimatedTranslation", recordCircle.startTranslation);
        ofFloat.setStartDelay(100L);
        ofFloat.setDuration(350L);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.recordCircle, "snapAnimationProgress", 1.0f);
        ofFloat2.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        ofFloat2.setDuration(250L);
        SharedConfig.removeLockRecordAudioVideoHint();
        animatorSet.playTogether(ofFloat2, ofFloat, ObjectAnimator.ofFloat(this.recordCircle, "slideToCancelProgress", 1.0f).setDuration(200L), ObjectAnimator.ofFloat(this.slideText, "cancelToProgress", 1.0f));
        animatorSet.start();
    }

    public int getBackgroundTop() {
        int top = getTop();
        View view = this.topView;
        return (view == null || view.getVisibility() != 0) ? top : top + this.topView.getLayoutParams().height;
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        boolean z = view == this.topView || view == this.textFieldContainer;
        if (z) {
            canvas.save();
            if (view == this.textFieldContainer) {
                int dp = (int) (this.animatedTop + AndroidUtilities.dp(2.0f) + this.chatSearchExpandOffset);
                View view2 = this.topView;
                if (view2 != null && view2.getVisibility() == 0) {
                    dp += this.topView.getHeight();
                }
                canvas.clipRect(0, dp, getMeasuredWidth(), getMeasuredHeight());
            } else {
                canvas.clipRect(0, this.animatedTop, getMeasuredWidth(), this.animatedTop + view.getLayoutParams().height + AndroidUtilities.dp(2.0f));
            }
        }
        boolean drawChild = super.drawChild(canvas, view, j);
        if (z) {
            canvas.restore();
        }
        return drawChild;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int intrinsicHeight = (int) (this.animatedTop + (Theme.chat_composeShadowDrawable.getIntrinsicHeight() * (1.0f - this.composeShadowAlpha)));
        View view = this.topView;
        if (view != null && view.getVisibility() == 0) {
            intrinsicHeight = (int) (intrinsicHeight + ((1.0f - this.topViewEnterProgress) * this.topView.getLayoutParams().height));
        }
        int intrinsicHeight2 = Theme.chat_composeShadowDrawable.getIntrinsicHeight() + intrinsicHeight;
        Theme.chat_composeShadowDrawable.setAlpha((int) (this.composeShadowAlpha * 255.0f));
        Theme.chat_composeShadowDrawable.setBounds(0, intrinsicHeight, getMeasuredWidth(), intrinsicHeight2);
        Theme.chat_composeShadowDrawable.draw(canvas);
        int i = (int) (intrinsicHeight2 + this.chatSearchExpandOffset);
        if (this.allowBlur) {
            this.backgroundPaint.setColor(getThemedColor("chat_messagePanelBackground"));
            if (SharedConfig.chatBlurEnabled() && this.sizeNotifierLayout != null) {
                android.graphics.Rect rect = AndroidUtilities.rectTmp2;
                rect.set(0, i, getWidth(), getHeight());
                this.sizeNotifierLayout.drawBlurRect(canvas, getTop(), rect, this.backgroundPaint, false);
                return;
            }
            canvas.drawRect(0.0f, i, getWidth(), getHeight(), this.backgroundPaint);
            return;
        }
        canvas.drawRect(0.0f, i, getWidth(), getHeight(), getThemedPaint("paintChatComposeBackground"));
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:5|(1:10)(1:9)|11|(8:13|(1:18)(1:17)|(1:25)(1:24)|26|(3:28|(1:30)(1:31)|32)|(1:34)|35|(1:37))|38|(4:40|(1:45)(1:44)|46|(5:48|50|54|51|52))|49|50|54|51|52) */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onSendLongClick(View view) {
        int i;
        if (isInScheduleMode()) {
            return false;
        }
        ChatActivity chatActivity = this.parentFragment;
        boolean z = chatActivity != null && UserObject.isUserSelf(chatActivity.getCurrentUser());
        if (this.sendPopupLayout == null) {
            ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this.parentActivity, this.resourcesProvider);
            this.sendPopupLayout = actionBarPopupWindowLayout;
            actionBarPopupWindowLayout.setAnimationEnabled(false);
            this.sendPopupLayout.setOnTouchListener(new AnonymousClass32());
            this.sendPopupLayout.setDispatchKeyEventListener(new ChatActivityEnterView$$ExternalSyntheticLambda52(this));
            this.sendPopupLayout.setShownFromBottom(false);
            ChatActivity chatActivity2 = this.parentFragment;
            boolean z2 = chatActivity2 != null && chatActivity2.canScheduleMessage();
            boolean z3 = !z && (this.slowModeTimer <= 0 || isInScheduleMode());
            if (z2) {
                ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(getContext(), true, !z3, this.resourcesProvider);
                if (z) {
                    actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("SetReminder", 2131628248), 2131165662);
                } else {
                    actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("ScheduleMessage", 2131628082), 2131165662);
                }
                actionBarMenuSubItem.setMinimumWidth(AndroidUtilities.dp(196.0f));
                actionBarMenuSubItem.setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda17(this));
                this.sendPopupLayout.addView((View) actionBarMenuSubItem, LayoutHelper.createLinear(-1, 48));
            }
            if (z3) {
                ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(getContext(), !z2, true, this.resourcesProvider);
                actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString("SendWithoutSound", 2131628212), 2131165539);
                actionBarMenuSubItem2.setMinimumWidth(AndroidUtilities.dp(196.0f));
                actionBarMenuSubItem2.setOnClickListener(new ChatActivityEnterView$$ExternalSyntheticLambda21(this));
                this.sendPopupLayout.addView((View) actionBarMenuSubItem2, LayoutHelper.createLinear(-1, 48));
            }
            this.sendPopupLayout.setupRadialSelectors(getThemedColor("dialogButtonSelector"));
            AnonymousClass33 anonymousClass33 = new AnonymousClass33(this.sendPopupLayout, -2, -2);
            this.sendPopupWindow = anonymousClass33;
            anonymousClass33.setAnimationEnabled(false);
            this.sendPopupWindow.setAnimationStyle(2131689481);
            this.sendPopupWindow.setOutsideTouchable(true);
            this.sendPopupWindow.setClippingEnabled(true);
            this.sendPopupWindow.setInputMethodMode(2);
            this.sendPopupWindow.setSoftInputMode(0);
            this.sendPopupWindow.getContentView().setFocusableInTouchMode(true);
            SharedConfig.removeScheduledOrNoSoundHint();
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
            if (chatActivityEnterViewDelegate != null) {
                chatActivityEnterViewDelegate.onSendLongClick();
            }
        }
        this.sendPopupLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        this.sendPopupWindow.setFocusable(true);
        view.getLocationInWindow(this.location);
        if (this.keyboardVisible) {
            int measuredHeight = getMeasuredHeight();
            View view2 = this.topView;
            if (measuredHeight > AndroidUtilities.dp((view2 == null || view2.getVisibility() != 0) ? 58.0f : 106.0f)) {
                i = this.location[1] + view.getMeasuredHeight();
                this.sendPopupWindow.showAtLocation(view, 51, ((this.location[0] + view.getMeasuredWidth()) - this.sendPopupLayout.getMeasuredWidth()) + AndroidUtilities.dp(8.0f), i);
                this.sendPopupWindow.dimBehind();
                this.sendButton.invalidate();
                view.performHapticFeedback(3, 2);
                return false;
            }
        }
        i = (this.location[1] - this.sendPopupLayout.getMeasuredHeight()) - AndroidUtilities.dp(2.0f);
        this.sendPopupWindow.showAtLocation(view, 51, ((this.location[0] + view.getMeasuredWidth()) - this.sendPopupLayout.getMeasuredWidth()) + AndroidUtilities.dp(8.0f), i);
        this.sendPopupWindow.dimBehind();
        this.sendButton.invalidate();
        view.performHapticFeedback(3, 2);
        return false;
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$32 */
    /* loaded from: classes3.dex */
    public class AnonymousClass32 implements View.OnTouchListener {
        private android.graphics.Rect popupRect = new android.graphics.Rect();

        AnonymousClass32() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() != 0 || ChatActivityEnterView.this.sendPopupWindow == null || !ChatActivityEnterView.this.sendPopupWindow.isShowing()) {
                return false;
            }
            view.getHitRect(this.popupRect);
            if (this.popupRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                return false;
            }
            ChatActivityEnterView.this.sendPopupWindow.dismiss();
            return false;
        }
    }

    public /* synthetic */ void lambda$onSendLongClick$34(KeyEvent keyEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (keyEvent.getKeyCode() != 4 || keyEvent.getRepeatCount() != 0 || (actionBarPopupWindow = this.sendPopupWindow) == null || !actionBarPopupWindow.isShowing()) {
            return;
        }
        this.sendPopupWindow.dismiss();
    }

    public /* synthetic */ void lambda$onSendLongClick$35(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        AlertsCreator.createScheduleDatePickerDialog(this.parentActivity, this.parentFragment.getDialogId(), new ChatActivityEnterView$$ExternalSyntheticLambda53(this), this.resourcesProvider);
    }

    public /* synthetic */ void lambda$onSendLongClick$36(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        sendMessageInternal(false, 0);
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$33 */
    /* loaded from: classes3.dex */
    public class AnonymousClass33 extends ActionBarPopupWindow {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass33(View view, int i, int i2) {
            super(view, i, i2);
            ChatActivityEnterView.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow, android.widget.PopupWindow
        public void dismiss() {
            super.dismiss();
            ChatActivityEnterView.this.sendButton.invalidate();
        }
    }

    public boolean isSendButtonVisible() {
        return this.sendButton.getVisibility() == 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0075  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x009f  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00e0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setRecordVideoButtonVisible(boolean z, boolean z2) {
        boolean z3;
        ImageView imageView = this.videoSendButton;
        if (imageView == null) {
            return;
        }
        imageView.setTag(z ? 1 : null);
        AnimatorSet animatorSet = this.audioVideoButtonAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.audioVideoButtonAnimation = null;
        }
        float f = 0.0f;
        float f2 = 0.1f;
        if (z2) {
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            if (DialogObject.isChatDialog(this.dialog_id)) {
                TLRPC$Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
                if (ChatObject.isChannel(chat) && !chat.megagroup) {
                    z3 = true;
                    globalMainSettings.edit().putBoolean(!z3 ? "currentModeVideoChannel" : "currentModeVideo", z).commit();
                    AnimatorSet animatorSet2 = new AnimatorSet();
                    this.audioVideoButtonAnimation = animatorSet2;
                    Animator[] animatorArr = new Animator[6];
                    ImageView imageView2 = this.videoSendButton;
                    Property property = View.SCALE_X;
                    float[] fArr = new float[1];
                    fArr[0] = !z ? 1.0f : 0.1f;
                    animatorArr[0] = ObjectAnimator.ofFloat(imageView2, property, fArr);
                    ImageView imageView3 = this.videoSendButton;
                    Property property2 = View.SCALE_Y;
                    float[] fArr2 = new float[1];
                    fArr2[0] = !z ? 1.0f : 0.1f;
                    animatorArr[1] = ObjectAnimator.ofFloat(imageView3, property2, fArr2);
                    ImageView imageView4 = this.videoSendButton;
                    Property property3 = View.ALPHA;
                    float[] fArr3 = new float[1];
                    fArr3[0] = !z ? 1.0f : 0.0f;
                    animatorArr[2] = ObjectAnimator.ofFloat(imageView4, property3, fArr3);
                    ImageView imageView5 = this.audioSendButton;
                    Property property4 = View.SCALE_X;
                    float[] fArr4 = new float[1];
                    fArr4[0] = !z ? 0.1f : 1.0f;
                    animatorArr[3] = ObjectAnimator.ofFloat(imageView5, property4, fArr4);
                    ImageView imageView6 = this.audioSendButton;
                    Property property5 = View.SCALE_Y;
                    float[] fArr5 = new float[1];
                    if (!z) {
                        f2 = 1.0f;
                    }
                    fArr5[0] = f2;
                    animatorArr[4] = ObjectAnimator.ofFloat(imageView6, property5, fArr5);
                    ImageView imageView7 = this.audioSendButton;
                    Property property6 = View.ALPHA;
                    float[] fArr6 = new float[1];
                    if (!z) {
                        f = 1.0f;
                    }
                    fArr6[0] = f;
                    animatorArr[5] = ObjectAnimator.ofFloat(imageView7, property6, fArr6);
                    animatorSet2.playTogether(animatorArr);
                    this.audioVideoButtonAnimation.addListener(new AnonymousClass34());
                    this.audioVideoButtonAnimation.setInterpolator(new DecelerateInterpolator());
                    this.audioVideoButtonAnimation.setDuration(150L);
                    this.audioVideoButtonAnimation.start();
                    return;
                }
            }
            z3 = false;
            globalMainSettings.edit().putBoolean(!z3 ? "currentModeVideoChannel" : "currentModeVideo", z).commit();
            AnimatorSet animatorSet22 = new AnimatorSet();
            this.audioVideoButtonAnimation = animatorSet22;
            Animator[] animatorArr2 = new Animator[6];
            ImageView imageView22 = this.videoSendButton;
            Property property7 = View.SCALE_X;
            float[] fArr7 = new float[1];
            fArr7[0] = !z ? 1.0f : 0.1f;
            animatorArr2[0] = ObjectAnimator.ofFloat(imageView22, property7, fArr7);
            ImageView imageView32 = this.videoSendButton;
            Property property22 = View.SCALE_Y;
            float[] fArr22 = new float[1];
            fArr22[0] = !z ? 1.0f : 0.1f;
            animatorArr2[1] = ObjectAnimator.ofFloat(imageView32, property22, fArr22);
            ImageView imageView42 = this.videoSendButton;
            Property property32 = View.ALPHA;
            float[] fArr32 = new float[1];
            fArr32[0] = !z ? 1.0f : 0.0f;
            animatorArr2[2] = ObjectAnimator.ofFloat(imageView42, property32, fArr32);
            ImageView imageView52 = this.audioSendButton;
            Property property42 = View.SCALE_X;
            float[] fArr42 = new float[1];
            fArr42[0] = !z ? 0.1f : 1.0f;
            animatorArr2[3] = ObjectAnimator.ofFloat(imageView52, property42, fArr42);
            ImageView imageView62 = this.audioSendButton;
            Property property52 = View.SCALE_Y;
            float[] fArr52 = new float[1];
            if (!z) {
            }
            fArr52[0] = f2;
            animatorArr2[4] = ObjectAnimator.ofFloat(imageView62, property52, fArr52);
            ImageView imageView72 = this.audioSendButton;
            Property property62 = View.ALPHA;
            float[] fArr62 = new float[1];
            if (!z) {
            }
            fArr62[0] = f;
            animatorArr2[5] = ObjectAnimator.ofFloat(imageView72, property62, fArr62);
            animatorSet22.playTogether(animatorArr2);
            this.audioVideoButtonAnimation.addListener(new AnonymousClass34());
            this.audioVideoButtonAnimation.setInterpolator(new DecelerateInterpolator());
            this.audioVideoButtonAnimation.setDuration(150L);
            this.audioVideoButtonAnimation.start();
            return;
        }
        this.videoSendButton.setScaleX(z ? 1.0f : 0.1f);
        this.videoSendButton.setScaleY(z ? 1.0f : 0.1f);
        this.videoSendButton.setAlpha(z ? 1.0f : 0.0f);
        this.audioSendButton.setScaleX(z ? 0.1f : 1.0f);
        ImageView imageView8 = this.audioSendButton;
        if (!z) {
            f2 = 1.0f;
        }
        imageView8.setScaleY(f2);
        ImageView imageView9 = this.audioSendButton;
        if (!z) {
            f = 1.0f;
        }
        imageView9.setAlpha(f);
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$34 */
    /* loaded from: classes3.dex */
    public class AnonymousClass34 extends AnimatorListenerAdapter {
        AnonymousClass34() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.audioVideoButtonAnimation)) {
                ChatActivityEnterView.this.audioVideoButtonAnimation = null;
            }
            (ChatActivityEnterView.this.videoSendButton.getTag() == null ? ChatActivityEnterView.this.audioSendButton : ChatActivityEnterView.this.videoSendButton).sendAccessibilityEvent(8);
        }
    }

    public boolean isRecordingAudioVideo() {
        AnimatorSet animatorSet;
        return this.recordingAudioVideo || ((animatorSet = this.runningAnimationAudio) != null && animatorSet.isRunning());
    }

    public boolean isRecordLocked() {
        return this.recordingAudioVideo && this.recordCircle.isSendButtonVisible();
    }

    public void cancelRecordingAudioVideo() {
        ImageView imageView;
        if (this.hasRecordVideo && (imageView = this.videoSendButton) != null && imageView.getTag() != null) {
            CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
            this.delegate.needStartRecordVideo(5, true, 0);
        } else {
            this.delegate.needStartRecordAudio(0);
            MediaController.getInstance().stopRecording(0, false, 0);
        }
        this.recordingAudioVideo = false;
        updateRecordInterface(2);
    }

    public void showContextProgress(boolean z) {
        CloseProgressDrawable2 closeProgressDrawable2 = this.progressDrawable;
        if (closeProgressDrawable2 == null) {
            return;
        }
        if (z) {
            closeProgressDrawable2.startAnimation();
        } else {
            closeProgressDrawable2.stopAnimation();
        }
    }

    public void setCaption(String str) {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            editTextCaption.setCaption(str);
            checkSendButton(true);
        }
    }

    public void setSlowModeTimer(int i) {
        this.slowModeTimer = i;
        updateSlowModeText();
    }

    public CharSequence getSlowModeTimer() {
        if (this.slowModeTimer > 0) {
            return this.slowModeButton.getText();
        }
        return null;
    }

    public void updateSlowModeText() {
        int i;
        boolean isUploadingMessageIdDialog;
        int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        AndroidUtilities.cancelRunOnUIThread(this.updateSlowModeRunnable);
        this.updateSlowModeRunnable = null;
        TLRPC$ChatFull tLRPC$ChatFull = this.info;
        int i2 = 2147483646;
        if (tLRPC$ChatFull != null && tLRPC$ChatFull.slowmode_seconds != 0 && tLRPC$ChatFull.slowmode_next_send_date <= currentTime && ((isUploadingMessageIdDialog = SendMessagesHelper.getInstance(this.currentAccount).isUploadingMessageIdDialog(this.dialog_id)) || SendMessagesHelper.getInstance(this.currentAccount).isSendingMessageIdDialog(this.dialog_id))) {
            if (!ChatObject.hasAdminRights(this.accountInstance.getMessagesController().getChat(Long.valueOf(this.info.id)))) {
                i = this.info.slowmode_seconds;
                if (isUploadingMessageIdDialog) {
                    i2 = Integer.MAX_VALUE;
                }
                this.slowModeTimer = i2;
            }
            i = 0;
        } else {
            int i3 = this.slowModeTimer;
            if (i3 >= 2147483646) {
                if (this.info != null) {
                    this.accountInstance.getMessagesController().loadFullChat(this.info.id, 0, true);
                }
                i = 0;
            } else {
                i = i3 - currentTime;
            }
        }
        if (this.slowModeTimer != 0 && i > 0) {
            this.slowModeButton.setText(AndroidUtilities.formatDurationNoHours(Math.max(1, i), false));
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
            if (chatActivityEnterViewDelegate != null) {
                SimpleTextView simpleTextView = this.slowModeButton;
                chatActivityEnterViewDelegate.onUpdateSlowModeButton(simpleTextView, false, simpleTextView.getText());
            }
            ChatActivityEnterView$$ExternalSyntheticLambda38 chatActivityEnterView$$ExternalSyntheticLambda38 = new ChatActivityEnterView$$ExternalSyntheticLambda38(this);
            this.updateSlowModeRunnable = chatActivityEnterView$$ExternalSyntheticLambda38;
            AndroidUtilities.runOnUIThread(chatActivityEnterView$$ExternalSyntheticLambda38, 100L);
        } else {
            this.slowModeTimer = 0;
        }
        if (!isInScheduleMode()) {
            checkSendButton(true);
        }
    }

    public void addTopView(View view, View view2, int i) {
        if (view == null) {
            return;
        }
        this.topLineView = view2;
        view2.setVisibility(8);
        this.topLineView.setAlpha(0.0f);
        addView(this.topLineView, LayoutHelper.createFrame(-1, 1.0f, 51, 0.0f, i + 1, 0.0f, 0.0f));
        this.topView = view;
        view.setVisibility(8);
        this.topViewEnterProgress = 0.0f;
        float f = i;
        this.topView.setTranslationY(f);
        addView(this.topView, 0, LayoutHelper.createFrame(-1, f, 51, 0.0f, 2.0f, 0.0f, 0.0f));
        this.needShowTopView = false;
    }

    public void setForceShowSendButton(boolean z, boolean z2) {
        this.forceShowSendButton = z;
        checkSendButton(z2);
    }

    public void setAllowStickersAndGifs(boolean z, boolean z2) {
        setAllowStickersAndGifs(z, z2, false);
    }

    public void setAllowStickersAndGifs(boolean z, boolean z2, boolean z3) {
        if ((this.allowStickers != z || this.allowGifs != z2) && this.emojiView != null) {
            if (!SharedConfig.smoothKeyboard) {
                if (this.emojiViewVisible) {
                    hidePopup(false);
                }
                this.sizeNotifierLayout.removeView(this.emojiView);
                this.emojiView = null;
            } else if (this.emojiViewVisible && !z3) {
                this.removeEmojiViewAfterAnimation = true;
                hidePopup(false);
            } else {
                if (z3) {
                    openKeyboardInternal();
                }
                this.sizeNotifierLayout.removeView(this.emojiView);
                this.emojiView = null;
            }
        }
        this.allowStickers = z;
        this.allowGifs = z2;
        setEmojiButtonImage(false, !this.isPaused);
    }

    public void addEmojiToRecent(String str) {
        createEmojiView();
        this.emojiView.addEmojiToRecent(str);
    }

    public void setOpenGifsTabFirst() {
        createEmojiView();
        MediaDataController.getInstance(this.currentAccount).loadRecents(0, true, true, false);
        this.emojiView.switchToGifRecent();
    }

    public /* synthetic */ void lambda$new$37(ValueAnimator valueAnimator) {
        MentionsContainerView mentionsContainerView;
        if (this.topView != null) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.topViewEnterProgress = floatValue;
            View view = this.topView;
            float f = 1.0f - floatValue;
            view.setTranslationY(this.animatedTop + (view.getLayoutParams().height * f));
            this.topLineView.setAlpha(floatValue);
            this.topLineView.setTranslationY(this.animatedTop);
            ChatActivity chatActivity = this.parentFragment;
            if (chatActivity == null || (mentionsContainerView = chatActivity.mentionContainer) == null) {
                return;
            }
            mentionsContainerView.setTranslationY(f * this.topView.getLayoutParams().height);
        }
    }

    public void showTopView(boolean z, boolean z2) {
        showTopView(z, z2, false);
    }

    private void showTopView(boolean z, boolean z2, boolean z3) {
        if (this.topView == null || this.topViewShowed || getVisibility() != 0) {
            if (this.recordedAudioPanel.getVisibility() == 0) {
                return;
            }
            if (this.forceShowSendButton && !z2) {
                return;
            }
            openKeyboard();
            return;
        }
        boolean z4 = this.recordedAudioPanel.getVisibility() != 0 && (!this.forceShowSendButton || z2) && (this.botReplyMarkup == null || this.editingMessageObject != null);
        if (!z3 && z && z4 && !this.keyboardVisible && !isPopupShowing()) {
            openKeyboard();
            Runnable runnable = this.showTopViewRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
            }
            ChatActivityEnterView$$ExternalSyntheticLambda39 chatActivityEnterView$$ExternalSyntheticLambda39 = new ChatActivityEnterView$$ExternalSyntheticLambda39(this);
            this.showTopViewRunnable = chatActivityEnterView$$ExternalSyntheticLambda39;
            AndroidUtilities.runOnUIThread(chatActivityEnterView$$ExternalSyntheticLambda39, 200L);
            return;
        }
        this.needShowTopView = true;
        this.topViewShowed = true;
        if (!this.allowShowTopView) {
            return;
        }
        this.topView.setVisibility(0);
        this.topLineView.setVisibility(0);
        ValueAnimator valueAnimator = this.currentTopViewAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.currentTopViewAnimation = null;
        }
        resizeForTopView(true);
        if (z) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.topViewEnterProgress, 1.0f);
            this.currentTopViewAnimation = ofFloat;
            ofFloat.addUpdateListener(this.topViewUpdateListener);
            this.currentTopViewAnimation.addListener(new AnonymousClass35());
            this.currentTopViewAnimation.setDuration(270L);
            this.currentTopViewAnimation.setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR);
            this.currentTopViewAnimation.start();
            this.notificationsIndex = NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(this.notificationsIndex, null);
        } else {
            this.topViewEnterProgress = 1.0f;
            this.topView.setTranslationY(0.0f);
            this.topLineView.setAlpha(1.0f);
        }
        if (!z4) {
            return;
        }
        this.messageEditText.requestFocus();
        openKeyboard();
    }

    public /* synthetic */ void lambda$showTopView$38() {
        showTopView(true, false, true);
        this.showTopViewRunnable = null;
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$35 */
    /* loaded from: classes3.dex */
    public class AnonymousClass35 extends AnimatorListenerAdapter {
        AnonymousClass35() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ValueAnimator valueAnimator = ChatActivityEnterView.this.currentTopViewAnimation;
            if (valueAnimator != null && valueAnimator.equals(animator)) {
                ChatActivityEnterView.this.currentTopViewAnimation = null;
            }
            NotificationCenter.getInstance(ChatActivityEnterView.this.currentAccount).onAnimationFinish(ChatActivityEnterView.this.notificationsIndex);
            if (ChatActivityEnterView.this.parentFragment == null || ChatActivityEnterView.this.parentFragment.mentionContainer == null) {
                return;
            }
            ChatActivityEnterView.this.parentFragment.mentionContainer.setTranslationY(0.0f);
        }
    }

    public void onEditTimeExpired() {
        this.doneButtonContainer.setVisibility(8);
    }

    public void showEditDoneProgress(boolean z, boolean z2) {
        AnimatorSet animatorSet = this.doneButtonAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (z2) {
            this.doneButtonAnimation = new AnimatorSet();
            if (z) {
                this.doneButtonProgress.setVisibility(0);
                this.doneButtonContainer.setEnabled(false);
                this.doneButtonAnimation.playTogether(ObjectAnimator.ofFloat(this.doneButtonImage, View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.doneButtonImage, View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.doneButtonImage, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.doneButtonProgress, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.doneButtonProgress, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.doneButtonProgress, View.ALPHA, 1.0f));
            } else {
                this.doneButtonImage.setVisibility(0);
                this.doneButtonContainer.setEnabled(true);
                this.doneButtonAnimation.playTogether(ObjectAnimator.ofFloat(this.doneButtonProgress, View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.doneButtonProgress, View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.doneButtonProgress, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.doneButtonImage, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.doneButtonImage, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.doneButtonImage, View.ALPHA, 1.0f));
            }
            this.doneButtonAnimation.addListener(new AnonymousClass36(z));
            this.doneButtonAnimation.setDuration(150L);
            this.doneButtonAnimation.start();
        } else if (z) {
            this.doneButtonImage.setScaleX(0.1f);
            this.doneButtonImage.setScaleY(0.1f);
            this.doneButtonImage.setAlpha(0.0f);
            this.doneButtonProgress.setScaleX(1.0f);
            this.doneButtonProgress.setScaleY(1.0f);
            this.doneButtonProgress.setAlpha(1.0f);
            this.doneButtonImage.setVisibility(4);
            this.doneButtonProgress.setVisibility(0);
            this.doneButtonContainer.setEnabled(false);
        } else {
            this.doneButtonProgress.setScaleX(0.1f);
            this.doneButtonProgress.setScaleY(0.1f);
            this.doneButtonProgress.setAlpha(0.0f);
            this.doneButtonImage.setScaleX(1.0f);
            this.doneButtonImage.setScaleY(1.0f);
            this.doneButtonImage.setAlpha(1.0f);
            this.doneButtonImage.setVisibility(0);
            this.doneButtonProgress.setVisibility(4);
            this.doneButtonContainer.setEnabled(true);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$36 */
    /* loaded from: classes3.dex */
    public class AnonymousClass36 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$show;

        AnonymousClass36(boolean z) {
            ChatActivityEnterView.this = r1;
            this.val$show = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ChatActivityEnterView.this.doneButtonAnimation == null || !ChatActivityEnterView.this.doneButtonAnimation.equals(animator)) {
                return;
            }
            if (!this.val$show) {
                ChatActivityEnterView.this.doneButtonProgress.setVisibility(4);
            } else {
                ChatActivityEnterView.this.doneButtonImage.setVisibility(4);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (ChatActivityEnterView.this.doneButtonAnimation == null || !ChatActivityEnterView.this.doneButtonAnimation.equals(animator)) {
                return;
            }
            ChatActivityEnterView.this.doneButtonAnimation = null;
        }
    }

    public void hideTopView(boolean z) {
        if (this.topView == null || !this.topViewShowed) {
            return;
        }
        Runnable runnable = this.showTopViewRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        this.topViewShowed = false;
        this.needShowTopView = false;
        if (!this.allowShowTopView) {
            return;
        }
        ValueAnimator valueAnimator = this.currentTopViewAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.currentTopViewAnimation = null;
        }
        if (z) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.topViewEnterProgress, 0.0f);
            this.currentTopViewAnimation = ofFloat;
            ofFloat.addUpdateListener(this.topViewUpdateListener);
            this.currentTopViewAnimation.addListener(new AnonymousClass37());
            this.currentTopViewAnimation.setDuration(250L);
            this.currentTopViewAnimation.setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR);
            this.currentTopViewAnimation.start();
            return;
        }
        this.topViewEnterProgress = 0.0f;
        this.topView.setVisibility(8);
        this.topLineView.setVisibility(8);
        this.topLineView.setAlpha(0.0f);
        resizeForTopView(false);
        View view = this.topView;
        view.setTranslationY(view.getLayoutParams().height);
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$37 */
    /* loaded from: classes3.dex */
    public class AnonymousClass37 extends AnimatorListenerAdapter {
        AnonymousClass37() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ValueAnimator valueAnimator = ChatActivityEnterView.this.currentTopViewAnimation;
            if (valueAnimator != null && valueAnimator.equals(animator)) {
                ChatActivityEnterView.this.topView.setVisibility(8);
                ChatActivityEnterView.this.topLineView.setVisibility(8);
                ChatActivityEnterView.this.resizeForTopView(false);
                ChatActivityEnterView.this.currentTopViewAnimation = null;
            }
            if (ChatActivityEnterView.this.parentFragment == null || ChatActivityEnterView.this.parentFragment.mentionContainer == null) {
                return;
            }
            ChatActivityEnterView.this.parentFragment.mentionContainer.setTranslationY(0.0f);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            ValueAnimator valueAnimator = ChatActivityEnterView.this.currentTopViewAnimation;
            if (valueAnimator == null || !valueAnimator.equals(animator)) {
                return;
            }
            ChatActivityEnterView.this.currentTopViewAnimation = null;
        }
    }

    public boolean isTopViewVisible() {
        View view = this.topView;
        return view != null && view.getVisibility() == 0;
    }

    public void onAdjustPanTransitionUpdate(float f, float f2, boolean z) {
        this.botWebViewMenuContainer.setTranslationY(f);
    }

    public void onAdjustPanTransitionEnd() {
        this.botWebViewMenuContainer.onPanTransitionEnd();
        Runnable runnable = this.onKeyboardClosed;
        if (runnable != null) {
            runnable.run();
            this.onKeyboardClosed = null;
        }
    }

    public void onAdjustPanTransitionStart(boolean z, int i) {
        Runnable runnable;
        this.botWebViewMenuContainer.onPanTransitionStart(z, i);
        if (z && (runnable = this.showTopViewRunnable) != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.showTopViewRunnable.run();
        }
        Runnable runnable2 = this.setTextFieldRunnable;
        if (runnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            this.setTextFieldRunnable.run();
        }
        if (!z || !this.messageEditText.hasFocus() || !hasBotWebView() || !botCommandsMenuIsShowing()) {
            return;
        }
        this.botWebViewMenuContainer.dismiss();
    }

    private void onWindowSizeChanged() {
        int height = this.sizeNotifierLayout.getHeight();
        if (!this.keyboardVisible) {
            height -= this.emojiPadding;
        }
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.onWindowSizeChanged(height);
        }
        if (this.topView != null) {
            if (height < AndroidUtilities.dp(72.0f) + ActionBar.getCurrentActionBarHeight()) {
                if (!this.allowShowTopView) {
                    return;
                }
                this.allowShowTopView = false;
                if (!this.needShowTopView) {
                    return;
                }
                this.topView.setVisibility(8);
                this.topLineView.setVisibility(8);
                this.topLineView.setAlpha(0.0f);
                resizeForTopView(false);
                this.topViewEnterProgress = 0.0f;
                View view = this.topView;
                view.setTranslationY(view.getLayoutParams().height);
            } else if (this.allowShowTopView) {
            } else {
                this.allowShowTopView = true;
                if (!this.needShowTopView) {
                    return;
                }
                this.topView.setVisibility(0);
                this.topLineView.setVisibility(0);
                this.topLineView.setAlpha(1.0f);
                resizeForTopView(true);
                this.topViewEnterProgress = 1.0f;
                this.topView.setTranslationY(0.0f);
            }
        }
    }

    public void resizeForTopView(boolean z) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.textFieldContainer.getLayoutParams();
        layoutParams.topMargin = AndroidUtilities.dp(2.0f) + (z ? this.topView.getLayoutParams().height : 0);
        this.textFieldContainer.setLayoutParams(layoutParams);
        setMinimumHeight(AndroidUtilities.dp(51.0f) + (z ? this.topView.getLayoutParams().height : 0));
        if (this.stickersExpanded) {
            if (this.searchingType == 0) {
                setStickersExpanded(false, true, false);
            } else {
                checkStickresExpandHeight();
            }
        }
    }

    public void onDestroy() {
        this.destroyed = true;
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStarted);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStartError);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStopped);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.audioDidSent);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.audioRouteChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messageReceivedByServer);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.sendingMessagesChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.audioRecordTooShort);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateBotMenuButton);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.onDestroy();
        }
        Runnable runnable = this.updateSlowModeRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.updateSlowModeRunnable = null;
        }
        PowerManager.WakeLock wakeLock = this.wakeLock;
        if (wakeLock != null) {
            try {
                wakeLock.release();
                this.wakeLock = null;
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierLayout;
        if (sizeNotifierFrameLayout != null) {
            sizeNotifierFrameLayout.setDelegate(null);
        }
        SenderSelectPopup senderSelectPopup = this.senderSelectPopupWindow;
        if (senderSelectPopup != null) {
            senderSelectPopup.setPauseNotifications(false);
            this.senderSelectPopupWindow.dismiss();
        }
    }

    public void checkChannelRights() {
        TLRPC$Chat currentChat;
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || (currentChat = chatActivity.getCurrentChat()) == null) {
            return;
        }
        this.audioVideoButtonContainer.setAlpha(ChatObject.canSendMedia(currentChat) ? 1.0f : 0.5f);
        EmojiView emojiView = this.emojiView;
        if (emojiView == null) {
            return;
        }
        emojiView.setStickersBanned(!ChatObject.canSendStickers(currentChat), currentChat.id);
    }

    public void onBeginHide() {
        Runnable runnable = this.focusRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.focusRunnable = null;
        }
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        SenderSelectPopup senderSelectPopup = this.senderSelectPopupWindow;
        if (senderSelectPopup != null) {
            senderSelectPopup.setPauseNotifications(false);
            this.senderSelectPopupWindow.dismiss();
        }
    }

    public void onPause() {
        this.isPaused = true;
        SenderSelectPopup senderSelectPopup = this.senderSelectPopupWindow;
        if (senderSelectPopup != null) {
            senderSelectPopup.setPauseNotifications(false);
            this.senderSelectPopupWindow.dismiss();
        }
        if (this.keyboardVisible) {
            this.showKeyboardOnResume = true;
        }
        ChatActivityEnterView$$ExternalSyntheticLambda45 chatActivityEnterView$$ExternalSyntheticLambda45 = new ChatActivityEnterView$$ExternalSyntheticLambda45(this);
        this.hideKeyboardRunnable = chatActivityEnterView$$ExternalSyntheticLambda45;
        AndroidUtilities.runOnUIThread(chatActivityEnterView$$ExternalSyntheticLambda45, 500L);
    }

    public /* synthetic */ void lambda$onPause$39() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || chatActivity.isLastFragment()) {
            closeKeyboard();
        }
        this.hideKeyboardRunnable = null;
    }

    public void onResume() {
        this.isPaused = false;
        Runnable runnable = this.hideKeyboardRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.hideKeyboardRunnable = null;
        }
        if (!hasBotWebView() || !botCommandsMenuIsShowing()) {
            getVisibility();
            if (!this.showKeyboardOnResume || !this.parentFragment.isLastFragment()) {
                return;
            }
            this.showKeyboardOnResume = false;
            if (this.searchingType == 0) {
                this.messageEditText.requestFocus();
            }
            AndroidUtilities.showKeyboard(this.messageEditText);
            if (AndroidUtilities.usingHardwareInput || this.keyboardVisible || AndroidUtilities.isInMultiwindow) {
                return;
            }
            this.waitingForKeyboardOpen = true;
            AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
            AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100L);
        }
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        this.messageEditText.setEnabled(i == 0);
    }

    public void setDialogId(long j, int i) {
        this.dialog_id = j;
        int i2 = this.currentAccount;
        if (i2 != i) {
            NotificationCenter.getInstance(i2).onAnimationFinish(this.notificationsIndex);
            NotificationCenter notificationCenter = NotificationCenter.getInstance(this.currentAccount);
            int i3 = NotificationCenter.recordStarted;
            notificationCenter.removeObserver(this, i3);
            NotificationCenter notificationCenter2 = NotificationCenter.getInstance(this.currentAccount);
            int i4 = NotificationCenter.recordStartError;
            notificationCenter2.removeObserver(this, i4);
            NotificationCenter notificationCenter3 = NotificationCenter.getInstance(this.currentAccount);
            int i5 = NotificationCenter.recordStopped;
            notificationCenter3.removeObserver(this, i5);
            NotificationCenter notificationCenter4 = NotificationCenter.getInstance(this.currentAccount);
            int i6 = NotificationCenter.recordProgressChanged;
            notificationCenter4.removeObserver(this, i6);
            NotificationCenter notificationCenter5 = NotificationCenter.getInstance(this.currentAccount);
            int i7 = NotificationCenter.closeChats;
            notificationCenter5.removeObserver(this, i7);
            NotificationCenter notificationCenter6 = NotificationCenter.getInstance(this.currentAccount);
            int i8 = NotificationCenter.audioDidSent;
            notificationCenter6.removeObserver(this, i8);
            NotificationCenter notificationCenter7 = NotificationCenter.getInstance(this.currentAccount);
            int i9 = NotificationCenter.audioRouteChanged;
            notificationCenter7.removeObserver(this, i9);
            NotificationCenter notificationCenter8 = NotificationCenter.getInstance(this.currentAccount);
            int i10 = NotificationCenter.messagePlayingDidReset;
            notificationCenter8.removeObserver(this, i10);
            NotificationCenter notificationCenter9 = NotificationCenter.getInstance(this.currentAccount);
            int i11 = NotificationCenter.messagePlayingProgressDidChanged;
            notificationCenter9.removeObserver(this, i11);
            NotificationCenter notificationCenter10 = NotificationCenter.getInstance(this.currentAccount);
            int i12 = NotificationCenter.featuredStickersDidLoad;
            notificationCenter10.removeObserver(this, i12);
            NotificationCenter notificationCenter11 = NotificationCenter.getInstance(this.currentAccount);
            int i13 = NotificationCenter.messageReceivedByServer;
            notificationCenter11.removeObserver(this, i13);
            NotificationCenter notificationCenter12 = NotificationCenter.getInstance(this.currentAccount);
            int i14 = NotificationCenter.sendingMessagesChanged;
            notificationCenter12.removeObserver(this, i14);
            this.currentAccount = i;
            this.accountInstance = AccountInstance.getInstance(i);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i3);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i4);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i5);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i6);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i7);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i8);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i9);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i10);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i11);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i12);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i13);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i14);
        }
        updateScheduleButton(false);
        checkRoundVideo();
        updateFieldHint(false);
        updateSendAsButton(false);
    }

    public void setChatInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        this.info = tLRPC$ChatFull;
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.setChatInfo(tLRPC$ChatFull);
        }
        setSlowModeTimer(tLRPC$ChatFull.slowmode_next_send_date);
    }

    public void checkRoundVideo() {
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        if (this.hasRecordVideo) {
            return;
        }
        if (this.attachLayout == null || Build.VERSION.SDK_INT < 18) {
            this.hasRecordVideo = false;
            setRecordVideoButtonVisible(false, false);
            return;
        }
        boolean z = true;
        this.hasRecordVideo = true;
        if (DialogObject.isChatDialog(this.dialog_id)) {
            TLRPC$Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
            if (!ChatObject.isChannel(chat) || chat.megagroup) {
                z = false;
            }
            if (z && !chat.creator && ((tLRPC$TL_chatAdminRights = chat.admin_rights) == null || !tLRPC$TL_chatAdminRights.post_messages)) {
                this.hasRecordVideo = false;
            }
        } else {
            z = false;
        }
        if (!SharedConfig.inappCamera) {
            this.hasRecordVideo = false;
        }
        if (this.hasRecordVideo) {
            if (SharedConfig.hasCameraCache) {
                CameraController.getInstance().initCamera(null);
            }
            setRecordVideoButtonVisible(MessagesController.getGlobalMainSettings().getBoolean(z ? "currentModeVideoChannel" : "currentModeVideo", z), false);
            return;
        }
        setRecordVideoButtonVisible(false, false);
    }

    public boolean isInVideoMode() {
        ImageView imageView = this.videoSendButton;
        return (imageView == null || imageView.getTag() == null) ? false : true;
    }

    public boolean hasRecordVideo() {
        return this.hasRecordVideo;
    }

    public MessageObject getReplyingMessageObject() {
        return this.replyingMessageObject;
    }

    public void updateFieldHint(boolean z) {
        boolean z2;
        MessageObject messageObject;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup2;
        MessageObject messageObject2 = this.replyingMessageObject;
        if (messageObject2 != null && (tLRPC$ReplyMarkup2 = messageObject2.messageOwner.reply_markup) != null && !TextUtils.isEmpty(tLRPC$ReplyMarkup2.placeholder)) {
            this.messageEditText.setHintText(this.replyingMessageObject.messageOwner.reply_markup.placeholder, z);
        } else if (this.editingMessageObject != null) {
            this.messageEditText.setHintText(this.editingCaption ? LocaleController.getString("Caption", 2131624843) : LocaleController.getString("TypeMessage", 2131628711));
        } else if (this.botKeyboardViewVisible && (messageObject = this.botButtonsMessageObject) != null && (tLRPC$ReplyMarkup = messageObject.messageOwner.reply_markup) != null && !TextUtils.isEmpty(tLRPC$ReplyMarkup.placeholder)) {
            this.messageEditText.setHintText(this.botButtonsMessageObject.messageOwner.reply_markup.placeholder, z);
        } else {
            boolean z3 = false;
            if (DialogObject.isChatDialog(this.dialog_id)) {
                TLRPC$Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
                TLRPC$ChatFull chatFull = this.accountInstance.getMessagesController().getChatFull(-this.dialog_id);
                z2 = ChatObject.isChannel(chat) && !chat.megagroup;
                if (ChatObject.getSendAsPeerId(chat, chatFull) == chat.id) {
                    z3 = true;
                }
            } else {
                z2 = false;
            }
            if (z3) {
                this.messageEditText.setHintText(LocaleController.getString("SendAnonymously", 2131628176));
                return;
            }
            ChatActivity chatActivity = this.parentFragment;
            if (chatActivity != null && chatActivity.isThreadChat()) {
                if (this.parentFragment.isReplyChatComment()) {
                    this.messageEditText.setHintText(LocaleController.getString("Comment", 2131625192));
                } else {
                    this.messageEditText.setHintText(LocaleController.getString("Reply", 2131627927));
                }
            } else if (z2) {
                if (this.silent) {
                    this.messageEditText.setHintText(LocaleController.getString("ChannelSilentBroadcast", 2131624967), z);
                } else {
                    this.messageEditText.setHintText(LocaleController.getString("ChannelBroadcast", 2131624886), z);
                }
            } else {
                this.messageEditText.setHintText(LocaleController.getString("TypeMessage", 2131628711));
            }
        }
    }

    public void setReplyingMessageObject(MessageObject messageObject) {
        MessageObject messageObject2;
        if (messageObject != null) {
            if (this.botMessageObject == null && (messageObject2 = this.botButtonsMessageObject) != this.replyingMessageObject) {
                this.botMessageObject = messageObject2;
            }
            this.replyingMessageObject = messageObject;
            setButtons(messageObject, true);
        } else if (this.replyingMessageObject == this.botButtonsMessageObject) {
            this.replyingMessageObject = null;
            setButtons(this.botMessageObject, false);
            this.botMessageObject = null;
        } else {
            this.replyingMessageObject = null;
        }
        MediaController.getInstance().setReplyingMessage(messageObject, getThreadMessage());
        updateFieldHint(false);
    }

    public void setWebPage(TLRPC$WebPage tLRPC$WebPage, boolean z) {
        this.messageWebPage = tLRPC$WebPage;
        this.messageWebPageSearch = z;
    }

    public boolean isMessageWebPageSearchEnabled() {
        return this.messageWebPageSearch;
    }

    private void hideRecordedAudioPanel(boolean z) {
        AnimatorSet animatorSet;
        AnimatorSet animatorSet2 = this.recordPannelAnimation;
        if (animatorSet2 == null || !animatorSet2.isRunning()) {
            this.audioToSendPath = null;
            this.audioToSend = null;
            this.audioToSendMessageObject = null;
            this.videoToSendMessageObject = null;
            this.videoTimelineView.destroy();
            if (this.videoSendButton != null && isInVideoMode()) {
                this.videoSendButton.setVisibility(0);
            } else {
                ImageView imageView = this.audioSendButton;
                if (imageView != null) {
                    imageView.setVisibility(0);
                }
            }
            if (z) {
                this.attachButton.setAlpha(0.0f);
                this.emojiButton[0].setAlpha(0.0f);
                this.emojiButton[1].setAlpha(0.0f);
                this.attachButton.setScaleX(0.0f);
                this.emojiButton[0].setScaleX(0.0f);
                this.emojiButton[1].setScaleX(0.0f);
                this.attachButton.setScaleY(0.0f);
                this.emojiButton[0].setScaleY(0.0f);
                this.emojiButton[1].setScaleY(0.0f);
                AnimatorSet animatorSet3 = new AnimatorSet();
                this.recordPannelAnimation = animatorSet3;
                animatorSet3.playTogether(ObjectAnimator.ofFloat(this.emojiButton[0], View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.recordedAudioPanel, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.attachButton, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.attachButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.attachButton, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.messageEditText, View.TRANSLATION_X, 0.0f));
                BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
                if (botCommandsMenuView != null) {
                    botCommandsMenuView.setAlpha(0.0f);
                    this.botCommandsMenuButton.setScaleY(0.0f);
                    this.botCommandsMenuButton.setScaleX(0.0f);
                    this.recordPannelAnimation.playTogether(ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_Y, 1.0f));
                }
                this.recordPannelAnimation.setDuration(150L);
                this.recordPannelAnimation.addListener(new AnonymousClass38());
            } else {
                this.recordDeleteImageView.playAnimation();
                AnimatorSet animatorSet4 = new AnimatorSet();
                if (isInVideoMode()) {
                    animatorSet4.playTogether(ObjectAnimator.ofFloat(this.videoTimelineView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.videoTimelineView, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.messageEditText, View.TRANSLATION_X, 0.0f));
                } else {
                    this.messageEditText.setAlpha(1.0f);
                    this.messageEditText.setTranslationX(0.0f);
                    animatorSet4.playTogether(ObjectAnimator.ofFloat(this.recordedAudioSeekBar, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordedAudioPlayButton, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordedAudioBackground, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordedAudioTimeTextView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordedAudioSeekBar, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.recordedAudioPlayButton, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.recordedAudioBackground, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.recordedAudioTimeTextView, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)));
                }
                animatorSet4.setDuration(200L);
                ImageView imageView2 = this.attachButton;
                if (imageView2 != null) {
                    imageView2.setAlpha(0.0f);
                    this.attachButton.setScaleX(0.0f);
                    this.attachButton.setScaleY(0.0f);
                    AnimatorSet animatorSet5 = new AnimatorSet();
                    animatorSet5.playTogether(ObjectAnimator.ofFloat(this.attachButton, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.attachButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.attachButton, View.SCALE_Y, 1.0f));
                    animatorSet5.setDuration(150L);
                    animatorSet = animatorSet5;
                } else {
                    animatorSet = null;
                }
                this.emojiButton[0].setAlpha(0.0f);
                this.emojiButton[1].setAlpha(0.0f);
                this.emojiButton[0].setScaleX(0.0f);
                this.emojiButton[1].setScaleX(0.0f);
                this.emojiButton[0].setScaleY(0.0f);
                this.emojiButton[1].setScaleY(0.0f);
                AnimatorSet animatorSet6 = new AnimatorSet();
                animatorSet6.playTogether(ObjectAnimator.ofFloat(this.recordDeleteImageView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_Y, 1.0f));
                BotCommandsMenuView botCommandsMenuView2 = this.botCommandsMenuButton;
                if (botCommandsMenuView2 != null) {
                    botCommandsMenuView2.setAlpha(0.0f);
                    this.botCommandsMenuButton.setScaleY(0.0f);
                    this.botCommandsMenuButton.setScaleX(0.0f);
                    animatorSet6.playTogether(ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_Y, 1.0f));
                }
                animatorSet6.setDuration(150L);
                animatorSet6.setStartDelay(600L);
                AnimatorSet animatorSet7 = new AnimatorSet();
                this.recordPannelAnimation = animatorSet7;
                if (animatorSet != null) {
                    animatorSet7.playTogether(animatorSet4, animatorSet, animatorSet6);
                } else {
                    animatorSet7.playTogether(animatorSet4, animatorSet6);
                }
                this.recordPannelAnimation.addListener(new AnonymousClass39());
            }
            this.recordPannelAnimation.start();
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$38 */
    /* loaded from: classes3.dex */
    public class AnonymousClass38 extends AnimatorListenerAdapter {
        AnonymousClass38() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ChatActivityEnterView.this.recordedAudioPanel.setVisibility(8);
            ChatActivityEnterView.this.messageEditText.requestFocus();
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$39 */
    /* loaded from: classes3.dex */
    public class AnonymousClass39 extends AnimatorListenerAdapter {
        AnonymousClass39() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ChatActivityEnterView.this.recordedAudioSeekBar.setAlpha(1.0f);
            ChatActivityEnterView.this.recordedAudioSeekBar.setTranslationX(0.0f);
            ChatActivityEnterView.this.recordedAudioPlayButton.setAlpha(1.0f);
            ChatActivityEnterView.this.recordedAudioPlayButton.setTranslationX(0.0f);
            ChatActivityEnterView.this.recordedAudioBackground.setAlpha(1.0f);
            ChatActivityEnterView.this.recordedAudioBackground.setTranslationX(0.0f);
            ChatActivityEnterView.this.recordedAudioTimeTextView.setAlpha(1.0f);
            ChatActivityEnterView.this.recordedAudioTimeTextView.setTranslationX(0.0f);
            ChatActivityEnterView.this.videoTimelineView.setAlpha(1.0f);
            ChatActivityEnterView.this.videoTimelineView.setTranslationX(0.0f);
            ChatActivityEnterView.this.messageEditText.setAlpha(1.0f);
            ChatActivityEnterView.this.messageEditText.setTranslationX(0.0f);
            ChatActivityEnterView.this.messageEditText.requestFocus();
            ChatActivityEnterView.this.recordedAudioPanel.setVisibility(8);
        }
    }

    public void sendMessage() {
        if (isInScheduleMode()) {
            AlertsCreator.createScheduleDatePickerDialog(this.parentActivity, this.parentFragment.getDialogId(), new ChatActivityEnterView$$ExternalSyntheticLambda53(this), this.resourcesProvider);
        } else {
            sendMessageInternal(true, 0);
        }
    }

    public void sendMessageInternal(boolean z, int i) {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate;
        TLRPC$Chat currentChat;
        EmojiView emojiView;
        if (this.slowModeTimer == Integer.MAX_VALUE && !isInScheduleMode()) {
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = this.delegate;
            if (chatActivityEnterViewDelegate2 == null) {
                return;
            }
            chatActivityEnterViewDelegate2.scrollToSendingMessage();
            return;
        }
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null) {
            TLRPC$Chat currentChat2 = chatActivity.getCurrentChat();
            if (this.parentFragment.getCurrentUser() != null || ((ChatObject.isChannel(currentChat2) && currentChat2.megagroup) || !ChatObject.isChannel(currentChat2))) {
                SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                edit.putBoolean("silent_" + this.dialog_id, !z).commit();
            }
        }
        if (this.stickersExpanded) {
            setStickersExpanded(false, true, false);
            if (this.searchingType != 0 && (emojiView = this.emojiView) != null) {
                emojiView.closeSearch(false);
                this.emojiView.hideSearchKeyboard();
            }
        }
        if (this.videoToSendMessageObject != null) {
            this.delegate.needStartRecordVideo(4, z, i);
            hideRecordedAudioPanel(true);
            checkSendButton(true);
        } else if (this.audioToSend != null) {
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null && playingMessageObject == this.audioToSendMessageObject) {
                MediaController.getInstance().cleanupPlayer(true, true);
            }
            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(this.audioToSend, null, this.audioToSendPath, this.dialog_id, this.replyingMessageObject, getThreadMessage(), null, null, null, null, z, i, 0, null, null);
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate3 = this.delegate;
            if (chatActivityEnterViewDelegate3 != null) {
                chatActivityEnterViewDelegate3.onMessageSend(null, z, i);
            }
            hideRecordedAudioPanel(true);
            checkSendButton(true);
        } else {
            Editable text = this.messageEditText.getText();
            ChatActivity chatActivity2 = this.parentFragment;
            if (chatActivity2 != null && (currentChat = chatActivity2.getCurrentChat()) != null && currentChat.slowmode_enabled && !ChatObject.hasAdminRights(currentChat)) {
                if (text.length() > this.accountInstance.getMessagesController().maxMessageLength) {
                    AlertsCreator.showSimpleAlert(this.parentFragment, LocaleController.getString("Slowmode", 2131628360), LocaleController.getString("SlowmodeSendErrorTooLong", 2131628369), this.resourcesProvider);
                    return;
                } else if (this.forceShowSendButton && text.length() > 0) {
                    AlertsCreator.showSimpleAlert(this.parentFragment, LocaleController.getString("Slowmode", 2131628360), LocaleController.getString("SlowmodeSendError", 2131628368), this.resourcesProvider);
                    return;
                }
            }
            if (processSendingText(text, z, i)) {
                if (this.delegate.hasForwardingMessages() || ((i != 0 && !isInScheduleMode()) || isInScheduleMode())) {
                    this.messageEditText.setText("");
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate4 = this.delegate;
                    if (chatActivityEnterViewDelegate4 != null) {
                        chatActivityEnterViewDelegate4.onMessageSend(text, z, i);
                    }
                } else {
                    this.messageTransitionIsRunning = false;
                    ChatActivityEnterView$$ExternalSyntheticLambda48 chatActivityEnterView$$ExternalSyntheticLambda48 = new ChatActivityEnterView$$ExternalSyntheticLambda48(this, text, z, i);
                    this.moveToSendStateRunnable = chatActivityEnterView$$ExternalSyntheticLambda48;
                    AndroidUtilities.runOnUIThread(chatActivityEnterView$$ExternalSyntheticLambda48, 200L);
                }
                this.lastTypingTimeSend = 0L;
            } else if (!this.forceShowSendButton || (chatActivityEnterViewDelegate = this.delegate) == null) {
            } else {
                chatActivityEnterViewDelegate.onMessageSend(null, z, i);
            }
        }
    }

    public /* synthetic */ void lambda$sendMessageInternal$40(CharSequence charSequence, boolean z, int i) {
        this.moveToSendStateRunnable = null;
        hideTopView(true);
        this.messageEditText.setText("");
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.onMessageSend(charSequence, z, i);
        }
    }

    public void doneEditingMessage() {
        if (this.editingMessageObject == null) {
            return;
        }
        if (this.currentLimit - this.codePointCount < 0) {
            AndroidUtilities.shakeView(this.captionLimitView, 2.0f, 0);
            Vibrator vibrator = (Vibrator) this.captionLimitView.getContext().getSystemService("vibrator");
            if (vibrator == null) {
                return;
            }
            vibrator.vibrate(200L);
            return;
        }
        if (this.searchingType != 0) {
            setSearchingTypeInternal(0, true);
            this.emojiView.closeSearch(false);
            if (this.stickersExpanded) {
                setStickersExpanded(false, true, false);
                this.waitingForKeyboardOpenAfterAnimation = true;
                AndroidUtilities.runOnUIThread(new ChatActivityEnterView$$ExternalSyntheticLambda44(this), 200L);
            }
        }
        CharSequence[] charSequenceArr = {AndroidUtilities.getTrimmedString(this.messageEditText.getText())};
        ArrayList<TLRPC$MessageEntity> entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr, supportsSendingNewEntities());
        if (!TextUtils.equals(charSequenceArr[0], this.editingMessageObject.messageText) || ((entities != null && !entities.isEmpty()) || (((entities == null || entities.isEmpty()) && !this.editingMessageObject.messageOwner.entities.isEmpty()) || (this.editingMessageObject.messageOwner.media instanceof TLRPC$TL_messageMediaWebPage)))) {
            MessageObject messageObject = this.editingMessageObject;
            messageObject.editingMessage = charSequenceArr[0];
            messageObject.editingMessageEntities = entities;
            messageObject.editingMessageSearchWebPage = this.messageWebPageSearch;
            SendMessagesHelper.getInstance(this.currentAccount).editMessage(this.editingMessageObject, null, null, null, null, null, false, null);
        }
        setEditingMessageObject(null, false);
    }

    public /* synthetic */ void lambda$doneEditingMessage$41() {
        this.waitingForKeyboardOpenAfterAnimation = false;
        openKeyboardInternal();
    }

    public boolean processSendingText(CharSequence charSequence, boolean z, int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        ChatActivity chatActivity;
        CharSequence trimmedString = AndroidUtilities.getTrimmedString(charSequence);
        boolean supportsSendingNewEntities = supportsSendingNewEntities();
        int i6 = this.accountInstance.getMessagesController().maxMessageLength;
        char c = 0;
        if (trimmedString.length() != 0) {
            if (this.delegate != null && (chatActivity = this.parentFragment) != null) {
                if ((i != 0) == chatActivity.isInScheduleMode()) {
                    this.delegate.prepareMessageSending();
                }
            }
            int i7 = 0;
            while (true) {
                int i8 = i7 + i6;
                if (trimmedString.length() > i8) {
                    int i9 = i8 - 1;
                    i4 = -1;
                    i3 = -1;
                    i2 = -1;
                    for (int i10 = 0; i9 > i7 && i10 < 300; i10++) {
                        char charAt = trimmedString.charAt(i9);
                        char charAt2 = i9 > 0 ? trimmedString.charAt(i9 - 1) : ' ';
                        if (charAt == '\n' && charAt2 == '\n') {
                            i5 = i9;
                            break;
                        }
                        if (charAt == '\n') {
                            i2 = i9;
                        } else if (i4 < 0 && Character.isWhitespace(charAt) && charAt2 == '.') {
                            i4 = i9;
                        } else if (i3 < 0 && Character.isWhitespace(charAt)) {
                            i3 = i9;
                        }
                        i9--;
                    }
                    i5 = -1;
                } else {
                    i5 = -1;
                    i4 = -1;
                    i3 = -1;
                    i2 = -1;
                }
                int min = i5 > 0 ? i5 : i2 > 0 ? i2 : i4 > 0 ? i4 : i3 > 0 ? i3 : Math.min(i8, trimmedString.length());
                CharSequence[] charSequenceArr = new CharSequence[1];
                charSequenceArr[c] = AndroidUtilities.getTrimmedString(trimmedString.subSequence(i7, min));
                ArrayList<TLRPC$MessageEntity> entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr, supportsSendingNewEntities);
                MessageObject.SendAnimationData sendAnimationData = null;
                if (!this.delegate.hasForwardingMessages()) {
                    sendAnimationData = new MessageObject.SendAnimationData();
                    float dp = AndroidUtilities.dp(22.0f);
                    sendAnimationData.height = dp;
                    sendAnimationData.width = dp;
                    this.messageEditText.getLocationInWindow(this.location);
                    sendAnimationData.x = this.location[c] + AndroidUtilities.dp(11.0f);
                    sendAnimationData.y = this.location[1] + AndroidUtilities.dp(19.0f);
                }
                int i11 = min;
                SendMessagesHelper.getInstance(this.currentAccount).sendMessage(charSequenceArr[c].toString(), this.dialog_id, this.replyingMessageObject, getThreadMessage(), this.messageWebPage, this.messageWebPageSearch, entities, null, null, z, i, sendAnimationData);
                i7 = i11 + 1;
                if (i11 == trimmedString.length()) {
                    return true;
                }
                c = 0;
            }
        } else {
            return false;
        }
    }

    private boolean supportsSendingNewEntities() {
        ChatActivity chatActivity = this.parentFragment;
        TLRPC$EncryptedChat currentEncryptedChat = chatActivity != null ? chatActivity.getCurrentEncryptedChat() : null;
        return currentEncryptedChat == null || AndroidUtilities.getPeerLayerVersion(currentEncryptedChat.layer) >= 101;
    }

    public void checkSendButton(boolean z) {
        int i;
        int i2;
        if (this.editingMessageObject != null || this.recordingAudioVideo) {
            return;
        }
        boolean z2 = this.isPaused ? false : z;
        CharSequence trimmedString = AndroidUtilities.getTrimmedString(this.messageEditText.getText());
        int i3 = this.slowModeTimer;
        if (i3 > 0 && i3 != Integer.MAX_VALUE && !isInScheduleMode()) {
            if (this.slowModeButton.getVisibility() == 0) {
                return;
            }
            if (z2) {
                if (this.runningAnimationType == 5) {
                    return;
                }
                AnimatorSet animatorSet = this.runningAnimation;
                if (animatorSet != null) {
                    animatorSet.cancel();
                    this.runningAnimation = null;
                }
                AnimatorSet animatorSet2 = this.runningAnimation2;
                if (animatorSet2 != null) {
                    animatorSet2.cancel();
                    this.runningAnimation2 = null;
                }
                if (this.attachLayout != null) {
                    this.runningAnimation2 = new AnimatorSet();
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(ObjectAnimator.ofFloat(this.attachLayout, View.ALPHA, 0.0f));
                    arrayList.add(ObjectAnimator.ofFloat(this.attachLayout, View.SCALE_X, 0.0f));
                    this.scheduleButtonHidden = false;
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
                    boolean z3 = chatActivityEnterViewDelegate != null && chatActivityEnterViewDelegate.hasScheduledMessages();
                    ImageView imageView = this.scheduledButton;
                    if (imageView != null) {
                        imageView.setScaleY(1.0f);
                        if (z3) {
                            this.scheduledButton.setVisibility(0);
                            this.scheduledButton.setTag(1);
                            this.scheduledButton.setPivotX(AndroidUtilities.dp(48.0f));
                            ImageView imageView2 = this.scheduledButton;
                            Property property = View.TRANSLATION_X;
                            float[] fArr = new float[1];
                            ImageView imageView3 = this.botButton;
                            fArr[0] = AndroidUtilities.dp((imageView3 == null || imageView3.getVisibility() != 0) ? 48.0f : 96.0f);
                            arrayList.add(ObjectAnimator.ofFloat(imageView2, property, fArr));
                            arrayList.add(ObjectAnimator.ofFloat(this.scheduledButton, View.ALPHA, 1.0f));
                            arrayList.add(ObjectAnimator.ofFloat(this.scheduledButton, View.SCALE_X, 1.0f));
                        } else {
                            ImageView imageView4 = this.scheduledButton;
                            ImageView imageView5 = this.botButton;
                            imageView4.setTranslationX(AndroidUtilities.dp((imageView5 == null || imageView5.getVisibility() != 0) ? 48.0f : 96.0f));
                            this.scheduledButton.setAlpha(1.0f);
                            this.scheduledButton.setScaleX(1.0f);
                        }
                    }
                    this.runningAnimation2.playTogether(arrayList);
                    this.runningAnimation2.setDuration(100L);
                    this.runningAnimation2.addListener(new AnonymousClass40());
                    this.runningAnimation2.start();
                    updateFieldRight(0);
                    if (this.delegate != null && getVisibility() == 0) {
                        this.delegate.onAttachButtonHidden();
                    }
                }
                this.runningAnimationType = 5;
                this.runningAnimation = new AnimatorSet();
                ArrayList arrayList2 = new ArrayList();
                if (this.audioVideoButtonContainer.getVisibility() == 0) {
                    arrayList2.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_X, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_Y, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 0.0f));
                }
                if (this.expandStickersButton.getVisibility() == 0) {
                    arrayList2.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_X, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_Y, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.ALPHA, 0.0f));
                }
                if (this.sendButton.getVisibility() == 0) {
                    arrayList2.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_X, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_Y, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.sendButton, View.ALPHA, 0.0f));
                }
                if (this.cancelBotButton.getVisibility() == 0) {
                    arrayList2.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_X, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_Y, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.ALPHA, 0.0f));
                }
                arrayList2.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_X, 1.0f));
                arrayList2.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_Y, 1.0f));
                arrayList2.add(ObjectAnimator.ofFloat(this.slowModeButton, View.ALPHA, 1.0f));
                setSlowModeButtonVisible(true);
                this.runningAnimation.playTogether(arrayList2);
                this.runningAnimation.setDuration(150L);
                this.runningAnimation.addListener(new AnonymousClass41());
                this.runningAnimation.start();
                return;
            }
            this.slowModeButton.setScaleX(1.0f);
            this.slowModeButton.setScaleY(1.0f);
            this.slowModeButton.setAlpha(1.0f);
            setSlowModeButtonVisible(true);
            this.audioVideoButtonContainer.setScaleX(0.1f);
            this.audioVideoButtonContainer.setScaleY(0.1f);
            this.audioVideoButtonContainer.setAlpha(0.0f);
            this.audioVideoButtonContainer.setVisibility(8);
            this.sendButton.setScaleX(0.1f);
            this.sendButton.setScaleY(0.1f);
            this.sendButton.setAlpha(0.0f);
            this.sendButton.setVisibility(8);
            this.cancelBotButton.setScaleX(0.1f);
            this.cancelBotButton.setScaleY(0.1f);
            this.cancelBotButton.setAlpha(0.0f);
            this.cancelBotButton.setVisibility(8);
            if (this.expandStickersButton.getVisibility() == 0) {
                this.expandStickersButton.setScaleX(0.1f);
                this.expandStickersButton.setScaleY(0.1f);
                this.expandStickersButton.setAlpha(0.0f);
                this.expandStickersButton.setVisibility(8);
            }
            LinearLayout linearLayout = this.attachLayout;
            if (linearLayout != null) {
                linearLayout.setVisibility(8);
                if (this.delegate != null && getVisibility() == 0) {
                    this.delegate.onAttachButtonHidden();
                }
                updateFieldRight(0);
            }
            this.scheduleButtonHidden = false;
            if (this.scheduledButton == null) {
                return;
            }
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = this.delegate;
            if (chatActivityEnterViewDelegate2 != null && chatActivityEnterViewDelegate2.hasScheduledMessages()) {
                this.scheduledButton.setVisibility(0);
                this.scheduledButton.setTag(1);
            }
            ImageView imageView6 = this.scheduledButton;
            ImageView imageView7 = this.botButton;
            imageView6.setTranslationX(AndroidUtilities.dp((imageView7 == null || imageView7.getVisibility() != 0) ? 48.0f : 96.0f));
            this.scheduledButton.setAlpha(1.0f);
            this.scheduledButton.setScaleX(1.0f);
            this.scheduledButton.setScaleY(1.0f);
        } else if (trimmedString.length() > 0 || this.forceShowSendButton || this.audioToSend != null || this.videoToSendMessageObject != null || (this.slowModeTimer == Integer.MAX_VALUE && !isInScheduleMode())) {
            String caption = this.messageEditText.getCaption();
            boolean z4 = caption != null && (this.sendButton.getVisibility() == 0 || this.expandStickersButton.getVisibility() == 0);
            boolean z5 = caption == null && (this.cancelBotButton.getVisibility() == 0 || this.expandStickersButton.getVisibility() == 0);
            if (this.slowModeTimer == Integer.MAX_VALUE && !isInScheduleMode()) {
                i = getThemedColor("chat_messagePanelIcons");
            } else {
                i = getThemedColor("chat_messagePanelSend");
            }
            Theme.setSelectorDrawableColor(this.sendButton.getBackground(), Color.argb(24, Color.red(i), Color.green(i), Color.blue(i)), true);
            if (this.audioVideoButtonContainer.getVisibility() != 0 && this.slowModeButton.getVisibility() != 0 && !z4 && !z5) {
                return;
            }
            if (z2) {
                if (this.runningAnimationType == 1 && this.messageEditText.getCaption() == null) {
                    return;
                }
                if (this.runningAnimationType == 3 && caption != null) {
                    return;
                }
                AnimatorSet animatorSet3 = this.runningAnimation;
                if (animatorSet3 != null) {
                    animatorSet3.cancel();
                    this.runningAnimation = null;
                }
                AnimatorSet animatorSet4 = this.runningAnimation2;
                if (animatorSet4 != null) {
                    animatorSet4.cancel();
                    this.runningAnimation2 = null;
                }
                if (this.attachLayout != null) {
                    this.runningAnimation2 = new AnimatorSet();
                    ArrayList arrayList3 = new ArrayList();
                    arrayList3.add(ObjectAnimator.ofFloat(this.attachLayout, View.ALPHA, 0.0f));
                    arrayList3.add(ObjectAnimator.ofFloat(this.attachLayout, View.SCALE_X, 0.0f));
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate3 = this.delegate;
                    boolean z6 = chatActivityEnterViewDelegate3 != null && chatActivityEnterViewDelegate3.hasScheduledMessages();
                    this.scheduleButtonHidden = true;
                    ImageView imageView8 = this.scheduledButton;
                    if (imageView8 != null) {
                        imageView8.setScaleY(1.0f);
                        if (z6) {
                            this.scheduledButton.setTag(null);
                            arrayList3.add(ObjectAnimator.ofFloat(this.scheduledButton, View.ALPHA, 0.0f));
                            arrayList3.add(ObjectAnimator.ofFloat(this.scheduledButton, View.SCALE_X, 0.0f));
                            ImageView imageView9 = this.scheduledButton;
                            Property property2 = View.TRANSLATION_X;
                            float[] fArr2 = new float[1];
                            ImageView imageView10 = this.botButton;
                            fArr2[0] = AndroidUtilities.dp((imageView10 == null || imageView10.getVisibility() == 8) ? 48.0f : 96.0f);
                            arrayList3.add(ObjectAnimator.ofFloat(imageView9, property2, fArr2));
                        } else {
                            this.scheduledButton.setAlpha(0.0f);
                            this.scheduledButton.setScaleX(0.0f);
                            ImageView imageView11 = this.scheduledButton;
                            ImageView imageView12 = this.botButton;
                            imageView11.setTranslationX(AndroidUtilities.dp((imageView12 == null || imageView12.getVisibility() == 8) ? 48.0f : 96.0f));
                        }
                    }
                    this.runningAnimation2.playTogether(arrayList3);
                    this.runningAnimation2.setDuration(100L);
                    this.runningAnimation2.addListener(new AnonymousClass42(z6));
                    this.runningAnimation2.start();
                    updateFieldRight(0);
                    if (this.delegate != null && getVisibility() == 0) {
                        this.delegate.onAttachButtonHidden();
                    }
                }
                this.runningAnimation = new AnimatorSet();
                ArrayList arrayList4 = new ArrayList();
                if (this.audioVideoButtonContainer.getVisibility() == 0) {
                    arrayList4.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_X, 0.1f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_Y, 0.1f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 0.0f));
                }
                if (this.expandStickersButton.getVisibility() == 0) {
                    arrayList4.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_X, 0.1f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_Y, 0.1f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.ALPHA, 0.0f));
                }
                if (this.slowModeButton.getVisibility() == 0) {
                    arrayList4.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_X, 0.1f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_Y, 0.1f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.slowModeButton, View.ALPHA, 0.0f));
                }
                if (z4) {
                    arrayList4.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_X, 0.1f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_Y, 0.1f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.sendButton, View.ALPHA, 0.0f));
                } else if (z5) {
                    arrayList4.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_X, 0.1f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_Y, 0.1f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.ALPHA, 0.0f));
                }
                if (caption != null) {
                    this.runningAnimationType = 3;
                    arrayList4.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_X, 1.0f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_Y, 1.0f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.ALPHA, 1.0f));
                    this.cancelBotButton.setVisibility(0);
                } else {
                    this.runningAnimationType = 1;
                    arrayList4.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_X, 1.0f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_Y, 1.0f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.sendButton, View.ALPHA, 1.0f));
                    this.sendButton.setVisibility(0);
                }
                this.runningAnimation.playTogether(arrayList4);
                this.runningAnimation.setDuration(150L);
                this.runningAnimation.addListener(new AnonymousClass43(caption));
                this.runningAnimation.start();
                return;
            }
            this.audioVideoButtonContainer.setScaleX(0.1f);
            this.audioVideoButtonContainer.setScaleY(0.1f);
            this.audioVideoButtonContainer.setAlpha(0.0f);
            this.audioVideoButtonContainer.setVisibility(8);
            if (this.slowModeButton.getVisibility() == 0) {
                this.slowModeButton.setScaleX(0.1f);
                this.slowModeButton.setScaleY(0.1f);
                this.slowModeButton.setAlpha(0.0f);
                setSlowModeButtonVisible(false);
            }
            if (caption != null) {
                this.sendButton.setScaleX(0.1f);
                this.sendButton.setScaleY(0.1f);
                this.sendButton.setAlpha(0.0f);
                this.sendButton.setVisibility(8);
                this.cancelBotButton.setScaleX(1.0f);
                this.cancelBotButton.setScaleY(1.0f);
                this.cancelBotButton.setAlpha(1.0f);
                this.cancelBotButton.setVisibility(0);
            } else {
                this.cancelBotButton.setScaleX(0.1f);
                this.cancelBotButton.setScaleY(0.1f);
                this.cancelBotButton.setAlpha(0.0f);
                this.sendButton.setVisibility(0);
                this.sendButton.setScaleX(1.0f);
                this.sendButton.setScaleY(1.0f);
                this.sendButton.setAlpha(1.0f);
                this.cancelBotButton.setVisibility(8);
            }
            if (this.expandStickersButton.getVisibility() == 0) {
                this.expandStickersButton.setScaleX(0.1f);
                this.expandStickersButton.setScaleY(0.1f);
                this.expandStickersButton.setAlpha(0.0f);
                i2 = 8;
                this.expandStickersButton.setVisibility(8);
            } else {
                i2 = 8;
            }
            LinearLayout linearLayout2 = this.attachLayout;
            if (linearLayout2 != null) {
                linearLayout2.setVisibility(i2);
                if (this.delegate != null && getVisibility() == 0) {
                    this.delegate.onAttachButtonHidden();
                }
                updateFieldRight(0);
            }
            this.scheduleButtonHidden = true;
            if (this.scheduledButton == null) {
                return;
            }
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate4 = this.delegate;
            if (chatActivityEnterViewDelegate4 != null && chatActivityEnterViewDelegate4.hasScheduledMessages()) {
                this.scheduledButton.setVisibility(8);
                this.scheduledButton.setTag(null);
            }
            this.scheduledButton.setAlpha(0.0f);
            this.scheduledButton.setScaleX(0.0f);
            this.scheduledButton.setScaleY(1.0f);
            ImageView imageView13 = this.scheduledButton;
            ImageView imageView14 = this.botButton;
            imageView13.setTranslationX(AndroidUtilities.dp((imageView14 == null || imageView14.getVisibility() == 8) ? 48.0f : 96.0f));
        } else if (this.emojiView != null && this.emojiViewVisible && ((this.stickersTabOpen || (this.emojiTabOpen && this.searchingType == 2)) && !AndroidUtilities.isInMultiwindow)) {
            if (z2) {
                if (this.runningAnimationType == 4) {
                    return;
                }
                AnimatorSet animatorSet5 = this.runningAnimation;
                if (animatorSet5 != null) {
                    animatorSet5.cancel();
                    this.runningAnimation = null;
                }
                AnimatorSet animatorSet6 = this.runningAnimation2;
                if (animatorSet6 != null) {
                    animatorSet6.cancel();
                    this.runningAnimation2 = null;
                }
                LinearLayout linearLayout3 = this.attachLayout;
                if (linearLayout3 != null && this.recordInterfaceState == 0) {
                    linearLayout3.setVisibility(0);
                    this.runningAnimation2 = new AnimatorSet();
                    ArrayList arrayList5 = new ArrayList();
                    arrayList5.add(ObjectAnimator.ofFloat(this.attachLayout, View.ALPHA, 1.0f));
                    arrayList5.add(ObjectAnimator.ofFloat(this.attachLayout, View.SCALE_X, 1.0f));
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate5 = this.delegate;
                    boolean z7 = chatActivityEnterViewDelegate5 != null && chatActivityEnterViewDelegate5.hasScheduledMessages();
                    this.scheduleButtonHidden = false;
                    ImageView imageView15 = this.scheduledButton;
                    if (imageView15 != null) {
                        imageView15.setScaleY(1.0f);
                        if (z7) {
                            this.scheduledButton.setVisibility(0);
                            this.scheduledButton.setTag(1);
                            this.scheduledButton.setPivotX(AndroidUtilities.dp(48.0f));
                            arrayList5.add(ObjectAnimator.ofFloat(this.scheduledButton, View.ALPHA, 1.0f));
                            arrayList5.add(ObjectAnimator.ofFloat(this.scheduledButton, View.SCALE_X, 1.0f));
                            arrayList5.add(ObjectAnimator.ofFloat(this.scheduledButton, View.TRANSLATION_X, 0.0f));
                        } else {
                            this.scheduledButton.setAlpha(1.0f);
                            this.scheduledButton.setScaleX(1.0f);
                            this.scheduledButton.setTranslationX(0.0f);
                        }
                    }
                    this.runningAnimation2.playTogether(arrayList5);
                    this.runningAnimation2.setDuration(100L);
                    this.runningAnimation2.addListener(new AnonymousClass44());
                    this.runningAnimation2.start();
                    updateFieldRight(1);
                    if (getVisibility() == 0) {
                        this.delegate.onAttachButtonShow();
                    }
                }
                this.expandStickersButton.setVisibility(0);
                this.runningAnimation = new AnimatorSet();
                this.runningAnimationType = 4;
                ArrayList arrayList6 = new ArrayList();
                arrayList6.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_X, 1.0f));
                arrayList6.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_Y, 1.0f));
                arrayList6.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.ALPHA, 1.0f));
                if (this.cancelBotButton.getVisibility() == 0) {
                    arrayList6.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_X, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_Y, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.ALPHA, 0.0f));
                } else if (this.audioVideoButtonContainer.getVisibility() == 0) {
                    arrayList6.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_X, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_Y, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 0.0f));
                } else if (this.slowModeButton.getVisibility() == 0) {
                    arrayList6.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_X, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_Y, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.slowModeButton, View.ALPHA, 0.0f));
                } else {
                    arrayList6.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_X, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_Y, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.sendButton, View.ALPHA, 0.0f));
                }
                this.runningAnimation.playTogether(arrayList6);
                this.runningAnimation.setDuration(250L);
                this.runningAnimation.addListener(new AnonymousClass45());
                this.runningAnimation.start();
                return;
            }
            this.slowModeButton.setScaleX(0.1f);
            this.slowModeButton.setScaleY(0.1f);
            this.slowModeButton.setAlpha(0.0f);
            setSlowModeButtonVisible(false);
            this.sendButton.setScaleX(0.1f);
            this.sendButton.setScaleY(0.1f);
            this.sendButton.setAlpha(0.0f);
            this.sendButton.setVisibility(8);
            this.cancelBotButton.setScaleX(0.1f);
            this.cancelBotButton.setScaleY(0.1f);
            this.cancelBotButton.setAlpha(0.0f);
            this.cancelBotButton.setVisibility(8);
            this.audioVideoButtonContainer.setScaleX(0.1f);
            this.audioVideoButtonContainer.setScaleY(0.1f);
            this.audioVideoButtonContainer.setAlpha(0.0f);
            this.audioVideoButtonContainer.setVisibility(8);
            this.expandStickersButton.setScaleX(1.0f);
            this.expandStickersButton.setScaleY(1.0f);
            this.expandStickersButton.setAlpha(1.0f);
            this.expandStickersButton.setVisibility(0);
            if (this.attachLayout != null) {
                if (getVisibility() == 0) {
                    this.delegate.onAttachButtonShow();
                }
                this.attachLayout.setVisibility(0);
                updateFieldRight(1);
            }
            this.scheduleButtonHidden = false;
            if (this.scheduledButton == null) {
                return;
            }
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate6 = this.delegate;
            if (chatActivityEnterViewDelegate6 != null && chatActivityEnterViewDelegate6.hasScheduledMessages()) {
                this.scheduledButton.setVisibility(0);
                this.scheduledButton.setTag(1);
            }
            this.scheduledButton.setAlpha(1.0f);
            this.scheduledButton.setScaleX(1.0f);
            this.scheduledButton.setScaleY(1.0f);
            this.scheduledButton.setTranslationX(0.0f);
        } else if (this.sendButton.getVisibility() != 0 && this.cancelBotButton.getVisibility() != 0 && this.expandStickersButton.getVisibility() != 0 && this.slowModeButton.getVisibility() != 0) {
        } else {
            if (z2) {
                if (this.runningAnimationType == 2) {
                    return;
                }
                AnimatorSet animatorSet7 = this.runningAnimation;
                if (animatorSet7 != null) {
                    animatorSet7.cancel();
                    this.runningAnimation = null;
                }
                AnimatorSet animatorSet8 = this.runningAnimation2;
                if (animatorSet8 != null) {
                    animatorSet8.cancel();
                    this.runningAnimation2 = null;
                }
                LinearLayout linearLayout4 = this.attachLayout;
                if (linearLayout4 != null) {
                    if (linearLayout4.getVisibility() != 0) {
                        this.attachLayout.setVisibility(0);
                        this.attachLayout.setAlpha(0.0f);
                        this.attachLayout.setScaleX(0.0f);
                    }
                    this.runningAnimation2 = new AnimatorSet();
                    ArrayList arrayList7 = new ArrayList();
                    arrayList7.add(ObjectAnimator.ofFloat(this.attachLayout, View.ALPHA, 1.0f));
                    arrayList7.add(ObjectAnimator.ofFloat(this.attachLayout, View.SCALE_X, 1.0f));
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate7 = this.delegate;
                    boolean z8 = chatActivityEnterViewDelegate7 != null && chatActivityEnterViewDelegate7.hasScheduledMessages();
                    this.scheduleButtonHidden = false;
                    ImageView imageView16 = this.scheduledButton;
                    if (imageView16 != null) {
                        if (z8) {
                            imageView16.setVisibility(0);
                            this.scheduledButton.setTag(1);
                            this.scheduledButton.setPivotX(AndroidUtilities.dp(48.0f));
                            arrayList7.add(ObjectAnimator.ofFloat(this.scheduledButton, View.ALPHA, 1.0f));
                            arrayList7.add(ObjectAnimator.ofFloat(this.scheduledButton, View.SCALE_X, 1.0f));
                            arrayList7.add(ObjectAnimator.ofFloat(this.scheduledButton, View.TRANSLATION_X, 0.0f));
                        } else {
                            imageView16.setAlpha(1.0f);
                            this.scheduledButton.setScaleX(1.0f);
                            this.scheduledButton.setScaleY(1.0f);
                            this.scheduledButton.setTranslationX(0.0f);
                        }
                    }
                    this.runningAnimation2.playTogether(arrayList7);
                    this.runningAnimation2.setDuration(100L);
                    this.runningAnimation2.addListener(new AnonymousClass46());
                    this.runningAnimation2.start();
                    updateFieldRight(1);
                    if (getVisibility() == 0) {
                        this.delegate.onAttachButtonShow();
                    }
                }
                this.audioVideoButtonContainer.setVisibility(0);
                this.runningAnimation = new AnimatorSet();
                this.runningAnimationType = 2;
                ArrayList arrayList8 = new ArrayList();
                arrayList8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_X, 1.0f));
                arrayList8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_Y, 1.0f));
                arrayList8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 1.0f));
                if (this.cancelBotButton.getVisibility() == 0) {
                    arrayList8.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_X, 0.1f));
                    arrayList8.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_Y, 0.1f));
                    arrayList8.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.ALPHA, 0.0f));
                } else if (this.expandStickersButton.getVisibility() == 0) {
                    arrayList8.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_X, 0.1f));
                    arrayList8.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_Y, 0.1f));
                    arrayList8.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.ALPHA, 0.0f));
                } else if (this.slowModeButton.getVisibility() == 0) {
                    arrayList8.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_X, 0.1f));
                    arrayList8.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_Y, 0.1f));
                    arrayList8.add(ObjectAnimator.ofFloat(this.slowModeButton, View.ALPHA, 0.0f));
                } else {
                    arrayList8.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_X, 0.1f));
                    arrayList8.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_Y, 0.1f));
                    arrayList8.add(ObjectAnimator.ofFloat(this.sendButton, View.ALPHA, 0.0f));
                }
                this.runningAnimation.playTogether(arrayList8);
                this.runningAnimation.setDuration(150L);
                this.runningAnimation.addListener(new AnonymousClass47());
                this.runningAnimation.start();
                return;
            }
            this.slowModeButton.setScaleX(0.1f);
            this.slowModeButton.setScaleY(0.1f);
            this.slowModeButton.setAlpha(0.0f);
            setSlowModeButtonVisible(false);
            this.sendButton.setScaleX(0.1f);
            this.sendButton.setScaleY(0.1f);
            this.sendButton.setAlpha(0.0f);
            this.sendButton.setVisibility(8);
            this.cancelBotButton.setScaleX(0.1f);
            this.cancelBotButton.setScaleY(0.1f);
            this.cancelBotButton.setAlpha(0.0f);
            this.cancelBotButton.setVisibility(8);
            this.expandStickersButton.setScaleX(0.1f);
            this.expandStickersButton.setScaleY(0.1f);
            this.expandStickersButton.setAlpha(0.0f);
            this.expandStickersButton.setVisibility(8);
            this.audioVideoButtonContainer.setScaleX(1.0f);
            this.audioVideoButtonContainer.setScaleY(1.0f);
            this.audioVideoButtonContainer.setAlpha(1.0f);
            this.audioVideoButtonContainer.setVisibility(0);
            if (this.attachLayout != null) {
                if (getVisibility() == 0) {
                    this.delegate.onAttachButtonShow();
                }
                this.attachLayout.setAlpha(1.0f);
                this.attachLayout.setScaleX(1.0f);
                this.attachLayout.setVisibility(0);
                updateFieldRight(1);
            }
            this.scheduleButtonHidden = false;
            if (this.scheduledButton == null) {
                return;
            }
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate8 = this.delegate;
            if (chatActivityEnterViewDelegate8 != null && chatActivityEnterViewDelegate8.hasScheduledMessages()) {
                this.scheduledButton.setVisibility(0);
                this.scheduledButton.setTag(1);
            }
            this.scheduledButton.setAlpha(1.0f);
            this.scheduledButton.setScaleX(1.0f);
            this.scheduledButton.setScaleY(1.0f);
            this.scheduledButton.setTranslationX(0.0f);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$40 */
    /* loaded from: classes3.dex */
    public class AnonymousClass40 extends AnimatorListenerAdapter {
        AnonymousClass40() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                ChatActivityEnterView.this.attachLayout.setVisibility(8);
                ChatActivityEnterView.this.runningAnimation2 = null;
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                ChatActivityEnterView.this.runningAnimation2 = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$41 */
    /* loaded from: classes3.dex */
    public class AnonymousClass41 extends AnimatorListenerAdapter {
        AnonymousClass41() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                ChatActivityEnterView.this.sendButton.setVisibility(8);
                ChatActivityEnterView.this.cancelBotButton.setVisibility(8);
                ChatActivityEnterView.this.audioVideoButtonContainer.setVisibility(8);
                ChatActivityEnterView.this.expandStickersButton.setVisibility(8);
                ChatActivityEnterView.this.runningAnimation = null;
                ChatActivityEnterView.this.runningAnimationType = 0;
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                ChatActivityEnterView.this.runningAnimation = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$42 */
    /* loaded from: classes3.dex */
    public class AnonymousClass42 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$hasScheduled;

        AnonymousClass42(boolean z) {
            ChatActivityEnterView.this = r1;
            this.val$hasScheduled = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                ChatActivityEnterView.this.attachLayout.setVisibility(8);
                if (this.val$hasScheduled) {
                    ChatActivityEnterView.this.scheduledButton.setVisibility(8);
                }
                ChatActivityEnterView.this.runningAnimation2 = null;
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                ChatActivityEnterView.this.runningAnimation2 = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$43 */
    /* loaded from: classes3.dex */
    public class AnonymousClass43 extends AnimatorListenerAdapter {
        final /* synthetic */ String val$caption;

        AnonymousClass43(String str) {
            ChatActivityEnterView.this = r1;
            this.val$caption = str;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                if (this.val$caption != null) {
                    ChatActivityEnterView.this.cancelBotButton.setVisibility(0);
                    ChatActivityEnterView.this.sendButton.setVisibility(8);
                } else {
                    ChatActivityEnterView.this.sendButton.setVisibility(0);
                    ChatActivityEnterView.this.cancelBotButton.setVisibility(8);
                }
                ChatActivityEnterView.this.audioVideoButtonContainer.setVisibility(8);
                ChatActivityEnterView.this.expandStickersButton.setVisibility(8);
                ChatActivityEnterView.this.setSlowModeButtonVisible(false);
                ChatActivityEnterView.this.runningAnimation = null;
                ChatActivityEnterView.this.runningAnimationType = 0;
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                ChatActivityEnterView.this.runningAnimation = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$44 */
    /* loaded from: classes3.dex */
    public class AnonymousClass44 extends AnimatorListenerAdapter {
        AnonymousClass44() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                ChatActivityEnterView.this.runningAnimation2 = null;
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                ChatActivityEnterView.this.runningAnimation2 = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$45 */
    /* loaded from: classes3.dex */
    public class AnonymousClass45 extends AnimatorListenerAdapter {
        AnonymousClass45() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                ChatActivityEnterView.this.sendButton.setVisibility(8);
                ChatActivityEnterView.this.cancelBotButton.setVisibility(8);
                ChatActivityEnterView.this.setSlowModeButtonVisible(false);
                ChatActivityEnterView.this.audioVideoButtonContainer.setVisibility(8);
                ChatActivityEnterView.this.expandStickersButton.setVisibility(0);
                ChatActivityEnterView.this.runningAnimation = null;
                ChatActivityEnterView.this.runningAnimationType = 0;
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                ChatActivityEnterView.this.runningAnimation = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$46 */
    /* loaded from: classes3.dex */
    public class AnonymousClass46 extends AnimatorListenerAdapter {
        AnonymousClass46() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                ChatActivityEnterView.this.runningAnimation2 = null;
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                ChatActivityEnterView.this.runningAnimation2 = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$47 */
    /* loaded from: classes3.dex */
    public class AnonymousClass47 extends AnimatorListenerAdapter {
        AnonymousClass47() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                ChatActivityEnterView.this.setSlowModeButtonVisible(false);
                ChatActivityEnterView.this.runningAnimation = null;
                ChatActivityEnterView.this.runningAnimationType = 0;
                if (ChatActivityEnterView.this.audioVideoButtonContainer == null) {
                    return;
                }
                ChatActivityEnterView.this.audioVideoButtonContainer.setVisibility(0);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                ChatActivityEnterView.this.runningAnimation = null;
            }
        }
    }

    public void setSlowModeButtonVisible(boolean z) {
        this.slowModeButton.setVisibility(z ? 0 : 8);
        int dp = z ? AndroidUtilities.dp(16.0f) : 0;
        if (this.messageEditText.getPaddingRight() != dp) {
            this.messageEditText.setPadding(0, AndroidUtilities.dp(11.0f), dp, AndroidUtilities.dp(12.0f));
        }
    }

    private void updateFieldRight(int i) {
        ImageView imageView;
        ImageView imageView2;
        ImageView imageView3;
        LinearLayout linearLayout;
        ImageView imageView4;
        ImageView imageView5;
        ImageView imageView6;
        LinearLayout linearLayout2;
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null || this.editingMessageObject != null) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) editTextCaption.getLayoutParams();
        int i2 = layoutParams.rightMargin;
        if (i == 1) {
            ImageView imageView7 = this.botButton;
            if (imageView7 != null && imageView7.getVisibility() == 0 && (imageView6 = this.scheduledButton) != null && imageView6.getVisibility() == 0 && (linearLayout2 = this.attachLayout) != null && linearLayout2.getVisibility() == 0) {
                layoutParams.rightMargin = AndroidUtilities.dp(146.0f);
            } else {
                ImageView imageView8 = this.botButton;
                if ((imageView8 != null && imageView8.getVisibility() == 0) || (((imageView4 = this.notifyButton) != null && imageView4.getVisibility() == 0) || ((imageView5 = this.scheduledButton) != null && imageView5.getTag() != null))) {
                    layoutParams.rightMargin = AndroidUtilities.dp(98.0f);
                } else {
                    layoutParams.rightMargin = AndroidUtilities.dp(50.0f);
                }
            }
        } else if (i == 2) {
            if (i2 != AndroidUtilities.dp(2.0f)) {
                ImageView imageView9 = this.botButton;
                if (imageView9 != null && imageView9.getVisibility() == 0 && (imageView3 = this.scheduledButton) != null && imageView3.getVisibility() == 0 && (linearLayout = this.attachLayout) != null && linearLayout.getVisibility() == 0) {
                    layoutParams.rightMargin = AndroidUtilities.dp(146.0f);
                } else {
                    ImageView imageView10 = this.botButton;
                    if ((imageView10 != null && imageView10.getVisibility() == 0) || (((imageView = this.notifyButton) != null && imageView.getVisibility() == 0) || ((imageView2 = this.scheduledButton) != null && imageView2.getTag() != null))) {
                        layoutParams.rightMargin = AndroidUtilities.dp(98.0f);
                    } else {
                        layoutParams.rightMargin = AndroidUtilities.dp(50.0f);
                    }
                }
            }
        } else {
            ImageView imageView11 = this.scheduledButton;
            if (imageView11 != null && imageView11.getTag() != null) {
                layoutParams.rightMargin = AndroidUtilities.dp(50.0f);
            } else {
                layoutParams.rightMargin = AndroidUtilities.dp(2.0f);
            }
        }
        if (i2 == layoutParams.rightMargin) {
            return;
        }
        this.messageEditText.setLayoutParams(layoutParams);
    }

    public void startMessageTransition() {
        Runnable runnable = this.moveToSendStateRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.messageTransitionIsRunning = true;
            this.moveToSendStateRunnable.run();
            this.moveToSendStateRunnable = null;
        }
    }

    public boolean canShowMessageTransition() {
        return this.moveToSendStateRunnable != null;
    }

    /* JADX WARN: Removed duplicated region for block: B:62:0x02f5 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x02f6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateRecordInterface(int i) {
        Exception e;
        boolean z;
        long j;
        float f;
        float f2;
        float f3;
        ViewGroup viewGroup;
        ViewGroup.LayoutParams layoutParams;
        Runnable runnable = this.moveToSendStateRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.moveToSendStateRunnable = null;
        }
        this.recordCircle.voiceEnterTransitionInProgress = false;
        if (this.recordingAudioVideo) {
            if (this.recordInterfaceState == 1) {
                return;
            }
            this.recordInterfaceState = 1;
            EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                emojiView.setEnabled(false);
            }
            AnimatorSet animatorSet = this.emojiButtonAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            try {
                if (this.wakeLock == null) {
                    PowerManager.WakeLock newWakeLock = ((PowerManager) ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(536870918, "telegram:audio_record_lock");
                    this.wakeLock = newWakeLock;
                    newWakeLock.acquire();
                }
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            AndroidUtilities.lockOrientation(this.parentActivity);
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
            if (chatActivityEnterViewDelegate != null) {
                chatActivityEnterViewDelegate.needStartRecordAudio(0);
            }
            AnimatorSet animatorSet2 = this.runningAnimationAudio;
            if (animatorSet2 != null) {
                animatorSet2.cancel();
            }
            AnimatorSet animatorSet3 = this.recordPannelAnimation;
            if (animatorSet3 != null) {
                animatorSet3.cancel();
            }
            this.recordPanel.setVisibility(0);
            this.recordCircle.setVisibility(0);
            this.recordCircle.setAmplitude(0.0d);
            this.recordDot.resetAlpha();
            this.runningAnimationAudio = new AnimatorSet();
            this.recordDot.setScaleX(0.0f);
            this.recordDot.setScaleY(0.0f);
            this.recordDot.enterAnimation = true;
            this.recordTimerView.setTranslationX(AndroidUtilities.dp(20.0f));
            this.recordTimerView.setAlpha(0.0f);
            this.slideText.setTranslationX(AndroidUtilities.dp(20.0f));
            this.slideText.setAlpha(0.0f);
            this.slideText.setCancelToProgress(0.0f);
            this.slideText.setSlideX(1.0f);
            this.recordCircle.setLockTranslation(10000.0f);
            this.slideText.setEnabled(true);
            this.recordIsCanceled = false;
            AnimatorSet animatorSet4 = new AnimatorSet();
            animatorSet4.playTogether(ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordDot, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.recordDot, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.recordTimerView, View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.recordTimerView, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.slideText, View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.slideText, View.ALPHA, 1.0f));
            ImageView imageView = this.audioSendButton;
            if (imageView != null) {
                animatorSet4.playTogether(ObjectAnimator.ofFloat(imageView, View.ALPHA, 0.0f));
            }
            ImageView imageView2 = this.videoSendButton;
            if (imageView2 != null) {
                animatorSet4.playTogether(ObjectAnimator.ofFloat(imageView2, View.ALPHA, 0.0f));
            }
            BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
            if (botCommandsMenuView != null) {
                animatorSet4.playTogether(ObjectAnimator.ofFloat(botCommandsMenuView, View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.ALPHA, 0.0f));
            }
            AnimatorSet animatorSet5 = new AnimatorSet();
            animatorSet5.playTogether(ObjectAnimator.ofFloat(this.messageEditText, View.TRANSLATION_X, AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordedAudioPanel, View.ALPHA, 1.0f));
            ImageView imageView3 = this.scheduledButton;
            if (imageView3 != null) {
                animatorSet5.playTogether(ObjectAnimator.ofFloat(imageView3, View.TRANSLATION_X, AndroidUtilities.dp(30.0f)), ObjectAnimator.ofFloat(this.scheduledButton, View.ALPHA, 0.0f));
            }
            LinearLayout linearLayout = this.attachLayout;
            if (linearLayout != null) {
                animatorSet5.playTogether(ObjectAnimator.ofFloat(linearLayout, View.TRANSLATION_X, AndroidUtilities.dp(30.0f)), ObjectAnimator.ofFloat(this.attachLayout, View.ALPHA, 0.0f));
            }
            this.runningAnimationAudio.playTogether(animatorSet4.setDuration(150L), animatorSet5.setDuration(150L), ObjectAnimator.ofFloat(this.recordCircle, this.recordCircleScale, 1.0f).setDuration(300L));
            this.runningAnimationAudio.addListener(new AnonymousClass48());
            this.runningAnimationAudio.setInterpolator(new DecelerateInterpolator());
            this.runningAnimationAudio.start();
            this.recordTimerView.start();
        } else if (this.recordIsCanceled && i == 3) {
            return;
        } else {
            PowerManager.WakeLock wakeLock = this.wakeLock;
            if (wakeLock != null) {
                try {
                    wakeLock.release();
                } catch (Exception e3) {
                    e = e3;
                }
                try {
                    this.wakeLock = null;
                } catch (Exception e4) {
                    e = e4;
                    FileLog.e(e);
                    AndroidUtilities.unlockOrientation(this.parentActivity);
                    this.wasSendTyping = false;
                    if (this.recordInterfaceState != 0) {
                    }
                }
            }
            AndroidUtilities.unlockOrientation(this.parentActivity);
            this.wasSendTyping = false;
            if (this.recordInterfaceState != 0) {
                return;
            }
            this.accountInstance.getMessagesController().sendTyping(this.dialog_id, getThreadMessageId(), 2, 0);
            this.recordInterfaceState = 0;
            EmojiView emojiView2 = this.emojiView;
            if (emojiView2 != null) {
                emojiView2.setEnabled(true);
            }
            AnimatorSet animatorSet6 = this.runningAnimationAudio;
            if (animatorSet6 != null) {
                z = animatorSet6.isRunning();
                ImageView imageView4 = this.videoSendButton;
                if (imageView4 != null) {
                    imageView4.setScaleX(1.0f);
                    this.videoSendButton.setScaleY(1.0f);
                }
                ImageView imageView5 = this.audioSendButton;
                if (imageView5 != null) {
                    imageView5.setScaleX(1.0f);
                    this.audioSendButton.setScaleY(1.0f);
                }
                this.runningAnimationAudio.removeAllListeners();
                this.runningAnimationAudio.cancel();
            } else {
                z = false;
            }
            AnimatorSet animatorSet7 = this.recordPannelAnimation;
            if (animatorSet7 != null) {
                animatorSet7.cancel();
            }
            this.messageEditText.setVisibility(0);
            this.runningAnimationAudio = new AnimatorSet();
            if (z || i == 4) {
                if (this.videoSendButton != null && isInVideoMode()) {
                    this.videoSendButton.setVisibility(0);
                } else {
                    ImageView imageView6 = this.audioSendButton;
                    if (imageView6 != null) {
                        imageView6.setVisibility(0);
                    }
                }
                this.runningAnimationAudio.playTogether(ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.recordDot, View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.recordDot, View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.recordCircle, this.recordCircleScale, 0.0f), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.recordTimerView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordCircle, this.recordCircleScale, 0.0f), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.messageEditText, View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.recordCircle, "slideToCancelProgress", 1.0f));
                BotCommandsMenuView botCommandsMenuView2 = this.botCommandsMenuButton;
                if (botCommandsMenuView2 != null) {
                    this.runningAnimationAudio.playTogether(ObjectAnimator.ofFloat(botCommandsMenuView2, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.ALPHA, 1.0f));
                }
                ImageView imageView7 = this.audioSendButton;
                if (imageView7 != null) {
                    imageView7.setScaleX(1.0f);
                    this.audioSendButton.setScaleY(1.0f);
                    AnimatorSet animatorSet8 = this.runningAnimationAudio;
                    Animator[] animatorArr = new Animator[1];
                    ImageView imageView8 = this.audioSendButton;
                    Property property = View.ALPHA;
                    float[] fArr = new float[1];
                    fArr[0] = isInVideoMode() ? 0.0f : 1.0f;
                    animatorArr[0] = ObjectAnimator.ofFloat(imageView8, property, fArr);
                    animatorSet8.playTogether(animatorArr);
                }
                ImageView imageView9 = this.videoSendButton;
                if (imageView9 != null) {
                    imageView9.setScaleX(1.0f);
                    this.videoSendButton.setScaleY(1.0f);
                    AnimatorSet animatorSet9 = this.runningAnimationAudio;
                    Animator[] animatorArr2 = new Animator[1];
                    ImageView imageView10 = this.videoSendButton;
                    Property property2 = View.ALPHA;
                    float[] fArr2 = new float[1];
                    fArr2[0] = isInVideoMode() ? 1.0f : 0.0f;
                    animatorArr2[0] = ObjectAnimator.ofFloat(imageView10, property2, fArr2);
                    animatorSet9.playTogether(animatorArr2);
                }
                ImageView imageView11 = this.scheduledButton;
                if (imageView11 != null) {
                    this.runningAnimationAudio.playTogether(ObjectAnimator.ofFloat(imageView11, View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.scheduledButton, View.ALPHA, 1.0f));
                }
                LinearLayout linearLayout2 = this.attachLayout;
                if (linearLayout2 != null) {
                    this.runningAnimationAudio.playTogether(ObjectAnimator.ofFloat(linearLayout2, View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.attachLayout, View.ALPHA, 1.0f));
                }
                this.recordIsCanceled = true;
                this.runningAnimationAudio.setDuration(150L);
            } else if (i == 3) {
                this.slideText.setEnabled(false);
                if (isInVideoMode()) {
                    this.recordedAudioBackground.setVisibility(8);
                    this.recordedAudioTimeTextView.setVisibility(8);
                    this.recordedAudioPlayButton.setVisibility(8);
                    this.recordedAudioSeekBar.setVisibility(8);
                    this.recordedAudioPanel.setAlpha(1.0f);
                    this.recordedAudioPanel.setVisibility(0);
                    this.recordDeleteImageView.setProgress(0.0f);
                    this.recordDeleteImageView.stopAnimation();
                } else {
                    this.videoTimelineView.setVisibility(8);
                    this.recordedAudioBackground.setVisibility(0);
                    this.recordedAudioTimeTextView.setVisibility(0);
                    this.recordedAudioPlayButton.setVisibility(0);
                    this.recordedAudioSeekBar.setVisibility(0);
                    this.recordedAudioPanel.setAlpha(1.0f);
                    this.recordedAudioBackground.setAlpha(0.0f);
                    this.recordedAudioTimeTextView.setAlpha(0.0f);
                    this.recordedAudioPlayButton.setAlpha(0.0f);
                    this.recordedAudioSeekBar.setAlpha(0.0f);
                    this.recordedAudioPanel.setVisibility(0);
                }
                this.recordDeleteImageView.setAlpha(0.0f);
                this.recordDeleteImageView.setScaleX(0.0f);
                this.recordDeleteImageView.setScaleY(0.0f);
                this.recordDeleteImageView.setProgress(0.0f);
                this.recordDeleteImageView.stopAnimation();
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.addUpdateListener(new ChatActivityEnterView$$ExternalSyntheticLambda4(this));
                if (!isInVideoMode()) {
                    viewGroup = (ViewGroup) this.recordedAudioPanel.getParent();
                    layoutParams = this.recordedAudioPanel.getLayoutParams();
                    viewGroup.removeView(this.recordedAudioPanel);
                    FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(viewGroup.getMeasuredWidth(), AndroidUtilities.dp(48.0f));
                    layoutParams2.gravity = 80;
                    this.sizeNotifierLayout.addView(this.recordedAudioPanel, layoutParams2);
                    this.videoTimelineView.setVisibility(8);
                } else {
                    this.videoTimelineView.setVisibility(0);
                    layoutParams = null;
                    viewGroup = null;
                }
                this.recordDeleteImageView.setAlpha(0.0f);
                this.recordDeleteImageView.setScaleX(0.0f);
                this.recordDeleteImageView.setScaleY(0.0f);
                AnimatorSet animatorSet10 = new AnimatorSet();
                animatorSet10.playTogether(ObjectAnimator.ofFloat(this.recordDot, View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.recordDot, View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.recordTimerView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordTimerView, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.slideText, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 0.0f));
                ImageView imageView12 = this.videoSendButton;
                if (imageView12 != null) {
                    Animator[] animatorArr3 = new Animator[3];
                    Property property3 = View.ALPHA;
                    float[] fArr3 = new float[1];
                    fArr3[0] = isInVideoMode() ? 1.0f : 0.0f;
                    animatorArr3[0] = ObjectAnimator.ofFloat(imageView12, property3, fArr3);
                    animatorArr3[1] = ObjectAnimator.ofFloat(this.videoSendButton, View.SCALE_X, 1.0f);
                    animatorArr3[2] = ObjectAnimator.ofFloat(this.videoSendButton, View.SCALE_Y, 1.0f);
                    animatorSet10.playTogether(animatorArr3);
                }
                ImageView imageView13 = this.audioSendButton;
                if (imageView13 != null) {
                    Animator[] animatorArr4 = new Animator[3];
                    Property property4 = View.ALPHA;
                    float[] fArr4 = new float[1];
                    fArr4[0] = isInVideoMode() ? 0.0f : 1.0f;
                    animatorArr4[0] = ObjectAnimator.ofFloat(imageView13, property4, fArr4);
                    animatorArr4[1] = ObjectAnimator.ofFloat(this.audioSendButton, View.SCALE_X, 1.0f);
                    animatorArr4[2] = ObjectAnimator.ofFloat(this.audioSendButton, View.SCALE_Y, 1.0f);
                    animatorSet10.playTogether(animatorArr4);
                }
                BotCommandsMenuView botCommandsMenuView3 = this.botCommandsMenuButton;
                if (botCommandsMenuView3 != null) {
                    animatorSet10.playTogether(ObjectAnimator.ofFloat(botCommandsMenuView3, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_Y, 0.0f));
                }
                animatorSet10.addListener(new AnonymousClass49());
                animatorSet10.setDuration(150L);
                animatorSet10.setStartDelay(150L);
                AnimatorSet animatorSet11 = new AnimatorSet();
                if (isInVideoMode()) {
                    this.recordedAudioTimeTextView.setAlpha(0.0f);
                    this.videoTimelineView.setAlpha(0.0f);
                    animatorSet11.playTogether(ObjectAnimator.ofFloat(this.recordedAudioTimeTextView, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.videoTimelineView, View.ALPHA, 1.0f));
                    animatorSet11.setDuration(150L);
                    animatorSet11.setStartDelay(430L);
                }
                ofFloat.setDuration(isInVideoMode() ? 490L : 580L);
                this.runningAnimationAudio.playTogether(animatorSet10, ofFloat, animatorSet11);
                this.runningAnimationAudio.addListener(new AnonymousClass50(viewGroup, layoutParams));
            } else if (i == 2 || i == 5) {
                if (this.videoSendButton != null && isInVideoMode()) {
                    this.videoSendButton.setVisibility(0);
                } else {
                    ImageView imageView14 = this.audioSendButton;
                    if (imageView14 != null) {
                        imageView14.setVisibility(0);
                    }
                }
                this.recordIsCanceled = true;
                AnimatorSet animatorSet12 = new AnimatorSet();
                animatorSet12.playTogether(ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.recordDot, View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.recordDot, View.SCALE_X, 0.0f));
                BotCommandsMenuView botCommandsMenuView4 = this.botCommandsMenuButton;
                if (botCommandsMenuView4 != null) {
                    animatorSet12.playTogether(ObjectAnimator.ofFloat(botCommandsMenuView4, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.ALPHA, 1.0f));
                }
                AnimatorSet animatorSet13 = new AnimatorSet();
                animatorSet13.playTogether(ObjectAnimator.ofFloat(this.recordTimerView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordTimerView, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.slideText, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.slideText, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)));
                if (i != 5) {
                    this.audioVideoButtonContainer.setScaleX(0.0f);
                    this.audioVideoButtonContainer.setScaleY(0.0f);
                    ImageView imageView15 = this.attachButton;
                    if (imageView15 != null && imageView15.getVisibility() == 0) {
                        this.attachButton.setScaleX(0.0f);
                        this.attachButton.setScaleY(0.0f);
                    }
                    ImageView imageView16 = this.botButton;
                    if (imageView16 != null && imageView16.getVisibility() == 0) {
                        this.botButton.setScaleX(0.0f);
                        this.botButton.setScaleY(0.0f);
                    }
                    animatorSet12.playTogether(ObjectAnimator.ofFloat(this.recordCircle, "slideToCancelProgress", 1.0f), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 1.0f));
                    LinearLayout linearLayout3 = this.attachLayout;
                    if (linearLayout3 != null) {
                        animatorSet12.playTogether(ObjectAnimator.ofFloat(linearLayout3, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.attachLayout, View.TRANSLATION_X, 0.0f));
                    }
                    ImageView imageView17 = this.attachButton;
                    if (imageView17 != null) {
                        f2 = 1.0f;
                        animatorSet12.playTogether(ObjectAnimator.ofFloat(imageView17, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.attachButton, View.SCALE_Y, 1.0f));
                    } else {
                        f2 = 1.0f;
                    }
                    ImageView imageView18 = this.botButton;
                    if (imageView18 != null) {
                        animatorSet12.playTogether(ObjectAnimator.ofFloat(imageView18, View.SCALE_X, f2), ObjectAnimator.ofFloat(this.botButton, View.SCALE_Y, f2));
                    }
                    ImageView imageView19 = this.videoSendButton;
                    if (imageView19 != null) {
                        Animator[] animatorArr5 = new Animator[1];
                        Property property5 = View.ALPHA;
                        float[] fArr5 = new float[1];
                        fArr5[0] = isInVideoMode() ? 1.0f : 0.0f;
                        animatorArr5[0] = ObjectAnimator.ofFloat(imageView19, property5, fArr5);
                        animatorSet12.playTogether(animatorArr5);
                        animatorSet12.playTogether(ObjectAnimator.ofFloat(this.videoSendButton, View.SCALE_X, 1.0f));
                        animatorSet12.playTogether(ObjectAnimator.ofFloat(this.videoSendButton, View.SCALE_Y, 1.0f));
                    }
                    ImageView imageView20 = this.audioSendButton;
                    if (imageView20 != null) {
                        Animator[] animatorArr6 = new Animator[1];
                        Property property6 = View.ALPHA;
                        float[] fArr6 = new float[1];
                        fArr6[0] = isInVideoMode() ? 0.0f : 1.0f;
                        animatorArr6[0] = ObjectAnimator.ofFloat(imageView20, property6, fArr6);
                        animatorSet12.playTogether(animatorArr6);
                        f3 = 1.0f;
                        animatorSet12.playTogether(ObjectAnimator.ofFloat(this.audioSendButton, View.SCALE_X, 1.0f));
                        animatorSet12.playTogether(ObjectAnimator.ofFloat(this.audioSendButton, View.SCALE_Y, 1.0f));
                    } else {
                        f3 = 1.0f;
                    }
                    ImageView imageView21 = this.scheduledButton;
                    if (imageView21 != null) {
                        animatorSet12.playTogether(ObjectAnimator.ofFloat(imageView21, View.ALPHA, f3), ObjectAnimator.ofFloat(this.scheduledButton, View.TRANSLATION_X, 0.0f));
                    }
                    j = 150;
                } else {
                    AnimatorSet animatorSet14 = new AnimatorSet();
                    animatorSet14.playTogether(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 1.0f));
                    LinearLayout linearLayout4 = this.attachLayout;
                    if (linearLayout4 != null) {
                        f = 1.0f;
                        animatorSet14.playTogether(ObjectAnimator.ofFloat(linearLayout4, View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.attachLayout, View.ALPHA, 1.0f));
                    } else {
                        f = 1.0f;
                    }
                    ImageView imageView22 = this.scheduledButton;
                    if (imageView22 != null) {
                        animatorSet14.playTogether(ObjectAnimator.ofFloat(imageView22, View.ALPHA, f), ObjectAnimator.ofFloat(this.scheduledButton, View.TRANSLATION_X, 0.0f));
                    }
                    j = 150;
                    animatorSet14.setDuration(150L);
                    animatorSet14.setStartDelay(110L);
                    animatorSet14.addListener(new AnonymousClass51());
                    this.runningAnimationAudio.playTogether(animatorSet14);
                }
                animatorSet12.setDuration(j);
                animatorSet12.setStartDelay(700L);
                animatorSet13.setDuration(200L);
                animatorSet13.setStartDelay(200L);
                this.messageEditText.setTranslationX(0.0f);
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 1.0f);
                ofFloat2.setStartDelay(300L);
                ofFloat2.setDuration(200L);
                AnimatorSet animatorSet15 = this.runningAnimationAudio;
                RecordCircle recordCircle = this.recordCircle;
                animatorSet15.playTogether(animatorSet12, animatorSet13, ofFloat2, ObjectAnimator.ofFloat(recordCircle, "lockAnimatedTranslation", recordCircle.startTranslation).setDuration(200L));
                if (i == 5) {
                    this.recordCircle.canceledByGesture();
                    ObjectAnimator duration = ObjectAnimator.ofFloat(this.recordCircle, "slideToCancelProgress", 1.0f).setDuration(200L);
                    duration.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
                    this.runningAnimationAudio.playTogether(duration);
                } else {
                    ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.recordCircle, "exitTransition", 1.0f);
                    ofFloat3.setDuration(360L);
                    ofFloat3.setStartDelay(490L);
                    this.runningAnimationAudio.playTogether(ofFloat3);
                }
                this.recordDot.playDeleteAnimation();
            } else {
                if (this.videoSendButton != null && isInVideoMode()) {
                    this.videoSendButton.setVisibility(0);
                } else {
                    ImageView imageView23 = this.audioSendButton;
                    if (imageView23 != null) {
                        imageView23.setVisibility(0);
                    }
                }
                AnimatorSet animatorSet16 = new AnimatorSet();
                animatorSet16.playTogether(ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[0], View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.recordDot, View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.recordDot, View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 1.0f));
                BotCommandsMenuView botCommandsMenuView5 = this.botCommandsMenuButton;
                if (botCommandsMenuView5 != null) {
                    animatorSet16.playTogether(ObjectAnimator.ofFloat(botCommandsMenuView5, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.ALPHA, 1.0f));
                }
                ImageView imageView24 = this.audioSendButton;
                if (imageView24 != null) {
                    imageView24.setScaleX(1.0f);
                    this.audioSendButton.setScaleY(1.0f);
                    Animator[] animatorArr7 = new Animator[1];
                    ImageView imageView25 = this.audioSendButton;
                    Property property7 = View.ALPHA;
                    float[] fArr7 = new float[1];
                    fArr7[0] = isInVideoMode() ? 0.0f : 1.0f;
                    animatorArr7[0] = ObjectAnimator.ofFloat(imageView25, property7, fArr7);
                    animatorSet16.playTogether(animatorArr7);
                }
                ImageView imageView26 = this.videoSendButton;
                if (imageView26 != null) {
                    imageView26.setScaleX(1.0f);
                    this.videoSendButton.setScaleY(1.0f);
                    Animator[] animatorArr8 = new Animator[1];
                    ImageView imageView27 = this.videoSendButton;
                    Property property8 = View.ALPHA;
                    float[] fArr8 = new float[1];
                    fArr8[0] = isInVideoMode() ? 1.0f : 0.0f;
                    animatorArr8[0] = ObjectAnimator.ofFloat(imageView27, property8, fArr8);
                    animatorSet16.playTogether(animatorArr8);
                }
                LinearLayout linearLayout5 = this.attachLayout;
                if (linearLayout5 != null) {
                    linearLayout5.setTranslationX(0.0f);
                    animatorSet16.playTogether(ObjectAnimator.ofFloat(this.attachLayout, View.ALPHA, 1.0f));
                }
                ImageView imageView28 = this.scheduledButton;
                if (imageView28 != null) {
                    imageView28.setTranslationX(0.0f);
                    animatorSet16.playTogether(ObjectAnimator.ofFloat(this.scheduledButton, View.ALPHA, 1.0f));
                }
                animatorSet16.setDuration(150L);
                animatorSet16.setStartDelay(200L);
                AnimatorSet animatorSet17 = new AnimatorSet();
                animatorSet17.playTogether(ObjectAnimator.ofFloat(this.recordTimerView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordTimerView, View.TRANSLATION_X, AndroidUtilities.dp(40.0f)), ObjectAnimator.ofFloat(this.slideText, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.slideText, View.TRANSLATION_X, AndroidUtilities.dp(40.0f)));
                animatorSet17.setDuration(150L);
                ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this.recordCircle, "exitTransition", 1.0f);
                ofFloat4.setDuration(this.messageTransitionIsRunning ? 220L : 360L);
                this.messageEditText.setTranslationX(0.0f);
                ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 1.0f);
                ofFloat5.setStartDelay(150L);
                ofFloat5.setDuration(200L);
                this.runningAnimationAudio.playTogether(animatorSet16, animatorSet17, ofFloat5, ofFloat4);
            }
            this.runningAnimationAudio.addListener(new AnonymousClass52(i));
            this.runningAnimationAudio.start();
            this.recordTimerView.stop();
        }
        this.delegate.onAudioVideoInterfaceUpdated();
        updateSendAsButton();
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$48 */
    /* loaded from: classes3.dex */
    public class AnonymousClass48 extends AnimatorListenerAdapter {
        AnonymousClass48() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimationAudio)) {
                ChatActivityEnterView.this.runningAnimationAudio = null;
            }
            ChatActivityEnterView.this.slideText.setAlpha(1.0f);
            ChatActivityEnterView.this.slideText.setTranslationX(0.0f);
            ChatActivityEnterView.this.recordCircle.showTooltipIfNeed();
            ChatActivityEnterView.this.messageEditText.setAlpha(0.0f);
        }
    }

    public /* synthetic */ void lambda$updateRecordInterface$42(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        if (!isInVideoMode()) {
            this.recordCircle.setTransformToSeekbar(floatValue);
            this.seekBarWaveform.setWaveScaling(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioSeekBar.invalidate();
            this.recordedAudioTimeTextView.setAlpha(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioPlayButton.setAlpha(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioPlayButton.setScaleX(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioPlayButton.setScaleY(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioSeekBar.setAlpha(this.recordCircle.getTransformToSeekbarProgressStep3());
            return;
        }
        this.recordCircle.setExitTransition(floatValue);
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$49 */
    /* loaded from: classes3.dex */
    public class AnonymousClass49 extends AnimatorListenerAdapter {
        AnonymousClass49() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ChatActivityEnterView.this.videoSendButton != null) {
                ChatActivityEnterView.this.videoSendButton.setScaleX(1.0f);
                ChatActivityEnterView.this.videoSendButton.setScaleY(1.0f);
            }
            if (ChatActivityEnterView.this.audioSendButton != null) {
                ChatActivityEnterView.this.audioSendButton.setScaleX(1.0f);
                ChatActivityEnterView.this.audioSendButton.setScaleY(1.0f);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$50 */
    /* loaded from: classes3.dex */
    public class AnonymousClass50 extends AnimatorListenerAdapter {
        final /* synthetic */ ViewGroup.LayoutParams val$finalOldLayoutParams;
        final /* synthetic */ ViewGroup val$finalParent;

        AnonymousClass50(ViewGroup viewGroup, ViewGroup.LayoutParams layoutParams) {
            ChatActivityEnterView.this = r1;
            this.val$finalParent = viewGroup;
            this.val$finalOldLayoutParams = layoutParams;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.val$finalParent != null) {
                ChatActivityEnterView.this.sizeNotifierLayout.removeView(ChatActivityEnterView.this.recordedAudioPanel);
                this.val$finalParent.addView(ChatActivityEnterView.this.recordedAudioPanel, this.val$finalOldLayoutParams);
            }
            ChatActivityEnterView.this.recordedAudioPanel.setAlpha(1.0f);
            ChatActivityEnterView.this.recordedAudioBackground.setAlpha(1.0f);
            ChatActivityEnterView.this.recordedAudioTimeTextView.setAlpha(1.0f);
            ChatActivityEnterView.this.recordedAudioPlayButton.setAlpha(1.0f);
            ChatActivityEnterView.this.recordedAudioPlayButton.setScaleY(1.0f);
            ChatActivityEnterView.this.recordedAudioPlayButton.setScaleX(1.0f);
            ChatActivityEnterView.this.recordedAudioSeekBar.setAlpha(1.0f);
            for (int i = 0; i < 2; i++) {
                ChatActivityEnterView.this.emojiButton[i].setScaleY(0.0f);
                ChatActivityEnterView.this.emojiButton[i].setScaleX(0.0f);
                ChatActivityEnterView.this.emojiButton[i].setAlpha(0.0f);
            }
            if (ChatActivityEnterView.this.botCommandsMenuButton != null) {
                ChatActivityEnterView.this.botCommandsMenuButton.setAlpha(0.0f);
                ChatActivityEnterView.this.botCommandsMenuButton.setScaleX(0.0f);
                ChatActivityEnterView.this.botCommandsMenuButton.setScaleY(0.0f);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$51 */
    /* loaded from: classes3.dex */
    public class AnonymousClass51 extends AnimatorListenerAdapter {
        AnonymousClass51() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            float f = 0.0f;
            if (ChatActivityEnterView.this.audioSendButton != null) {
                ChatActivityEnterView.this.audioSendButton.setAlpha(ChatActivityEnterView.this.isInVideoMode() ? 0.0f : 1.0f);
            }
            if (ChatActivityEnterView.this.videoSendButton != null) {
                ImageView imageView = ChatActivityEnterView.this.videoSendButton;
                if (ChatActivityEnterView.this.isInVideoMode()) {
                    f = 1.0f;
                }
                imageView.setAlpha(f);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$52 */
    /* loaded from: classes3.dex */
    public class AnonymousClass52 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$recordState;

        AnonymousClass52(int i) {
            ChatActivityEnterView.this = r1;
            this.val$recordState = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.runningAnimationAudio)) {
                ChatActivityEnterView.this.recordPanel.setVisibility(8);
                ChatActivityEnterView.this.recordCircle.setVisibility(8);
                ChatActivityEnterView.this.recordCircle.setSendButtonInvisible();
                ChatActivityEnterView.this.runningAnimationAudio = null;
                if (this.val$recordState != 3) {
                    ChatActivityEnterView.this.messageEditText.requestFocus();
                }
                ChatActivityEnterView.this.recordedAudioBackground.setAlpha(1.0f);
                if (ChatActivityEnterView.this.attachLayout != null) {
                    ChatActivityEnterView.this.attachLayout.setTranslationX(0.0f);
                }
                ChatActivityEnterView.this.slideText.setCancelToProgress(0.0f);
                ChatActivityEnterView.this.delegate.onAudioVideoInterfaceUpdated();
                ChatActivityEnterView.this.updateSendAsButton();
            }
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.recordingAudioVideo) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public void setDelegate(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
        this.delegate = chatActivityEnterViewDelegate;
    }

    public void setCommand(MessageObject messageObject, String str, boolean z, boolean z2) {
        String str2;
        if (str == null || getVisibility() != 0) {
            return;
        }
        TLRPC$User tLRPC$User = null;
        if (z) {
            String obj = this.messageEditText.getText().toString();
            if (messageObject != null && DialogObject.isChatDialog(this.dialog_id)) {
                tLRPC$User = this.accountInstance.getMessagesController().getUser(Long.valueOf(messageObject.messageOwner.from_id.user_id));
            }
            if ((this.botCount != 1 || z2) && tLRPC$User != null && tLRPC$User.bot && !str.contains("@")) {
                str2 = String.format(Locale.US, "%s@%s", str, tLRPC$User.username) + " " + obj.replaceFirst("^/[a-zA-Z@\\d_]{1,255}(\\s|$)", "");
            } else {
                str2 = str + " " + obj.replaceFirst("^/[a-zA-Z@\\d_]{1,255}(\\s|$)", "");
            }
            this.ignoreTextChange = true;
            this.messageEditText.setText(str2);
            EditTextCaption editTextCaption = this.messageEditText;
            editTextCaption.setSelection(editTextCaption.getText().length());
            this.ignoreTextChange = false;
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
            if (chatActivityEnterViewDelegate != null) {
                chatActivityEnterViewDelegate.onTextChanged(this.messageEditText.getText(), true);
            }
            if (this.keyboardVisible || this.currentPopupContentType != -1) {
                return;
            }
            openKeyboard();
        } else if (this.slowModeTimer > 0 && !isInScheduleMode()) {
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = this.delegate;
            if (chatActivityEnterViewDelegate2 == null) {
                return;
            }
            SimpleTextView simpleTextView = this.slowModeButton;
            chatActivityEnterViewDelegate2.onUpdateSlowModeButton(simpleTextView, true, simpleTextView.getText());
        } else {
            if (messageObject != null && DialogObject.isChatDialog(this.dialog_id)) {
                tLRPC$User = this.accountInstance.getMessagesController().getUser(Long.valueOf(messageObject.messageOwner.from_id.user_id));
            }
            if ((this.botCount != 1 || z2) && tLRPC$User != null && tLRPC$User.bot && !str.contains("@")) {
                SendMessagesHelper.getInstance(this.currentAccount).sendMessage(String.format(Locale.US, "%s@%s", str, tLRPC$User.username), this.dialog_id, this.replyingMessageObject, getThreadMessage(), null, false, null, null, null, true, 0, null);
            } else {
                SendMessagesHelper.getInstance(this.currentAccount).sendMessage(str, this.dialog_id, this.replyingMessageObject, getThreadMessage(), null, false, null, null, null, true, 0, null);
            }
        }
    }

    public void setEditingMessageObject(MessageObject messageObject, boolean z) {
        MessageObject messageObject2;
        boolean z2;
        CharSequence charSequence;
        ArrayList<TLRPC$MessageEntity> arrayList;
        if (this.audioToSend == null && this.videoToSendMessageObject == null && (messageObject2 = this.editingMessageObject) != messageObject) {
            int i = 1;
            boolean z3 = messageObject2 != null;
            this.editingMessageObject = messageObject;
            this.editingCaption = z;
            if (messageObject != null) {
                AnimatorSet animatorSet = this.doneButtonAnimation;
                if (animatorSet != null) {
                    animatorSet.cancel();
                    this.doneButtonAnimation = null;
                }
                this.doneButtonContainer.setVisibility(0);
                this.doneButtonImage.setScaleX(0.1f);
                this.doneButtonImage.setScaleY(0.1f);
                this.doneButtonImage.setAlpha(0.0f);
                this.doneButtonImage.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                if (z) {
                    this.currentLimit = this.accountInstance.getMessagesController().maxCaptionLength;
                    charSequence = this.editingMessageObject.caption;
                } else {
                    this.currentLimit = this.accountInstance.getMessagesController().maxMessageLength;
                    charSequence = this.editingMessageObject.messageText;
                }
                String str = "";
                String str2 = str;
                if (charSequence != null) {
                    ArrayList<TLRPC$MessageEntity> arrayList2 = this.editingMessageObject.messageOwner.entities;
                    MediaDataController.sortEntities(arrayList2);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
                    Object[] spans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), Object.class);
                    if (spans != null && spans.length > 0) {
                        for (Object obj : spans) {
                            spannableStringBuilder.removeSpan(obj);
                        }
                    }
                    if (arrayList2 != null) {
                        int i2 = 0;
                        while (i2 < arrayList2.size()) {
                            try {
                                TLRPC$MessageEntity tLRPC$MessageEntity = arrayList2.get(i2);
                                if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length > spannableStringBuilder.length()) {
                                    arrayList = arrayList2;
                                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_inputMessageEntityMentionName) {
                                    if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length < spannableStringBuilder.length() && spannableStringBuilder.charAt(tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length) == ' ') {
                                        tLRPC$MessageEntity.length += i;
                                    }
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(str);
                                    arrayList = arrayList2;
                                    sb.append(((TLRPC$TL_inputMessageEntityMentionName) tLRPC$MessageEntity).user_id.user_id);
                                    URLSpanUserMention uRLSpanUserMention = new URLSpanUserMention(sb.toString(), 3);
                                    int i3 = tLRPC$MessageEntity.offset;
                                    spannableStringBuilder.setSpan(uRLSpanUserMention, i3, tLRPC$MessageEntity.length + i3, 33);
                                } else {
                                    arrayList = arrayList2;
                                    if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMentionName) {
                                        if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length < spannableStringBuilder.length() && spannableStringBuilder.charAt(tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length) == ' ') {
                                            tLRPC$MessageEntity.length += i;
                                        }
                                        URLSpanUserMention uRLSpanUserMention2 = new URLSpanUserMention(str + ((TLRPC$TL_messageEntityMentionName) tLRPC$MessageEntity).user_id, 3);
                                        int i4 = tLRPC$MessageEntity.offset;
                                        spannableStringBuilder.setSpan(uRLSpanUserMention2, i4, tLRPC$MessageEntity.length + i4, 33);
                                    } else {
                                        if (!(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCode) && !(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPre)) {
                                            if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBold) {
                                                TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
                                                textStyleRun.flags |= 1;
                                                TextStyleSpan textStyleSpan = new TextStyleSpan(textStyleRun);
                                                int i5 = tLRPC$MessageEntity.offset;
                                                MediaDataController.addStyleToText(textStyleSpan, i5, tLRPC$MessageEntity.length + i5, spannableStringBuilder, true);
                                            } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityItalic) {
                                                TextStyleSpan.TextStyleRun textStyleRun2 = new TextStyleSpan.TextStyleRun();
                                                textStyleRun2.flags |= 2;
                                                TextStyleSpan textStyleSpan2 = new TextStyleSpan(textStyleRun2);
                                                int i6 = tLRPC$MessageEntity.offset;
                                                MediaDataController.addStyleToText(textStyleSpan2, i6, tLRPC$MessageEntity.length + i6, spannableStringBuilder, true);
                                            } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityStrike) {
                                                TextStyleSpan.TextStyleRun textStyleRun3 = new TextStyleSpan.TextStyleRun();
                                                textStyleRun3.flags |= 8;
                                                TextStyleSpan textStyleSpan3 = new TextStyleSpan(textStyleRun3);
                                                int i7 = tLRPC$MessageEntity.offset;
                                                MediaDataController.addStyleToText(textStyleSpan3, i7, tLRPC$MessageEntity.length + i7, spannableStringBuilder, true);
                                            } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUnderline) {
                                                TextStyleSpan.TextStyleRun textStyleRun4 = new TextStyleSpan.TextStyleRun();
                                                textStyleRun4.flags |= 16;
                                                TextStyleSpan textStyleSpan4 = new TextStyleSpan(textStyleRun4);
                                                int i8 = tLRPC$MessageEntity.offset;
                                                MediaDataController.addStyleToText(textStyleSpan4, i8, tLRPC$MessageEntity.length + i8, spannableStringBuilder, true);
                                            } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl) {
                                                URLSpanReplacement uRLSpanReplacement = new URLSpanReplacement(tLRPC$MessageEntity.url);
                                                int i9 = tLRPC$MessageEntity.offset;
                                                spannableStringBuilder.setSpan(uRLSpanReplacement, i9, tLRPC$MessageEntity.length + i9, 33);
                                            } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntitySpoiler) {
                                                TextStyleSpan.TextStyleRun textStyleRun5 = new TextStyleSpan.TextStyleRun();
                                                textStyleRun5.flags |= 256;
                                                TextStyleSpan textStyleSpan5 = new TextStyleSpan(textStyleRun5);
                                                int i10 = tLRPC$MessageEntity.offset;
                                                MediaDataController.addStyleToText(textStyleSpan5, i10, tLRPC$MessageEntity.length + i10, spannableStringBuilder, true);
                                            }
                                        }
                                        TextStyleSpan.TextStyleRun textStyleRun6 = new TextStyleSpan.TextStyleRun();
                                        textStyleRun6.flags |= 4;
                                        TextStyleSpan textStyleSpan6 = new TextStyleSpan(textStyleRun6);
                                        int i11 = tLRPC$MessageEntity.offset;
                                        MediaDataController.addStyleToText(textStyleSpan6, i11, tLRPC$MessageEntity.length + i11, spannableStringBuilder, true);
                                    }
                                }
                                i2++;
                                arrayList2 = arrayList;
                                i = 1;
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        }
                    }
                    str2 = Emoji.replaceEmoji(new SpannableStringBuilder(spannableStringBuilder), this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                }
                if (this.draftMessage == null && !z3) {
                    this.draftMessage = this.messageEditText.length() > 0 ? this.messageEditText.getText() : null;
                    this.draftSearchWebpage = this.messageWebPageSearch;
                }
                this.messageWebPageSearch = this.editingMessageObject.messageOwner.media instanceof TLRPC$TL_messageMediaWebPage;
                if (!this.keyboardVisible) {
                    ChatActivityEnterView$$ExternalSyntheticLambda47 chatActivityEnterView$$ExternalSyntheticLambda47 = new ChatActivityEnterView$$ExternalSyntheticLambda47(this, str2);
                    this.setTextFieldRunnable = chatActivityEnterView$$ExternalSyntheticLambda47;
                    AndroidUtilities.runOnUIThread(chatActivityEnterView$$ExternalSyntheticLambda47, 200L);
                } else {
                    Runnable runnable = this.setTextFieldRunnable;
                    if (runnable != null) {
                        AndroidUtilities.cancelRunOnUIThread(runnable);
                        this.setTextFieldRunnable = null;
                    }
                    setFieldText(str2);
                }
                this.messageEditText.requestFocus();
                openKeyboard();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.messageEditText.getLayoutParams();
                layoutParams.rightMargin = AndroidUtilities.dp(4.0f);
                this.messageEditText.setLayoutParams(layoutParams);
                this.sendButton.setVisibility(8);
                setSlowModeButtonVisible(false);
                this.cancelBotButton.setVisibility(8);
                this.audioVideoButtonContainer.setVisibility(8);
                this.attachLayout.setVisibility(8);
                this.sendButtonContainer.setVisibility(8);
                ImageView imageView = this.scheduledButton;
                if (imageView != null) {
                    imageView.setVisibility(8);
                }
                z2 = true;
            } else {
                Runnable runnable2 = this.setTextFieldRunnable;
                if (runnable2 != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable2);
                    this.setTextFieldRunnable = null;
                }
                this.doneButtonContainer.setVisibility(8);
                this.currentLimit = -1;
                this.delegate.onMessageEditEnd(false);
                this.sendButtonContainer.setVisibility(0);
                this.cancelBotButton.setScaleX(0.1f);
                this.cancelBotButton.setScaleY(0.1f);
                this.cancelBotButton.setAlpha(0.0f);
                this.cancelBotButton.setVisibility(8);
                if (this.slowModeTimer > 0 && !isInScheduleMode()) {
                    if (this.slowModeTimer == Integer.MAX_VALUE) {
                        this.sendButton.setScaleX(1.0f);
                        this.sendButton.setScaleY(1.0f);
                        this.sendButton.setAlpha(1.0f);
                        this.sendButton.setVisibility(0);
                        this.slowModeButton.setScaleX(0.1f);
                        this.slowModeButton.setScaleY(0.1f);
                        this.slowModeButton.setAlpha(0.0f);
                        setSlowModeButtonVisible(false);
                    } else {
                        this.sendButton.setScaleX(0.1f);
                        this.sendButton.setScaleY(0.1f);
                        this.sendButton.setAlpha(0.0f);
                        this.sendButton.setVisibility(8);
                        this.slowModeButton.setScaleX(1.0f);
                        this.slowModeButton.setScaleY(1.0f);
                        this.slowModeButton.setAlpha(1.0f);
                        setSlowModeButtonVisible(true);
                    }
                    this.attachLayout.setScaleX(0.01f);
                    this.attachLayout.setAlpha(0.0f);
                    this.attachLayout.setVisibility(8);
                    this.audioVideoButtonContainer.setScaleX(0.1f);
                    this.audioVideoButtonContainer.setScaleY(0.1f);
                    this.audioVideoButtonContainer.setAlpha(0.0f);
                    this.audioVideoButtonContainer.setVisibility(8);
                } else {
                    this.sendButton.setScaleX(0.1f);
                    this.sendButton.setScaleY(0.1f);
                    this.sendButton.setAlpha(0.0f);
                    this.sendButton.setVisibility(8);
                    this.slowModeButton.setScaleX(0.1f);
                    this.slowModeButton.setScaleY(0.1f);
                    this.slowModeButton.setAlpha(0.0f);
                    setSlowModeButtonVisible(false);
                    this.attachLayout.setScaleX(1.0f);
                    this.attachLayout.setAlpha(1.0f);
                    this.attachLayout.setVisibility(0);
                    this.audioVideoButtonContainer.setScaleX(1.0f);
                    this.audioVideoButtonContainer.setScaleY(1.0f);
                    this.audioVideoButtonContainer.setAlpha(1.0f);
                    this.audioVideoButtonContainer.setVisibility(0);
                }
                if (this.scheduledButton.getTag() != null) {
                    this.scheduledButton.setScaleX(1.0f);
                    this.scheduledButton.setScaleY(1.0f);
                    this.scheduledButton.setAlpha(1.0f);
                    this.scheduledButton.setVisibility(0);
                }
                this.messageEditText.setText(this.draftMessage);
                this.draftMessage = null;
                this.messageWebPageSearch = this.draftSearchWebpage;
                EditTextCaption editTextCaption = this.messageEditText;
                editTextCaption.setSelection(editTextCaption.length());
                if (getVisibility() == 0) {
                    this.delegate.onAttachButtonShow();
                }
                z2 = true;
                updateFieldRight(1);
            }
            updateFieldHint(z2);
            updateSendAsButton(z2);
        }
    }

    public /* synthetic */ void lambda$setEditingMessageObject$43(CharSequence charSequence) {
        setFieldText(charSequence);
        this.setTextFieldRunnable = null;
    }

    public ImageView getAttachButton() {
        return this.attachButton;
    }

    public View getSendButton() {
        return this.sendButton.getVisibility() == 0 ? this.sendButton : this.audioVideoButtonContainer;
    }

    public View getAudioVideoButtonContainer() {
        return this.audioVideoButtonContainer;
    }

    public View getEmojiButton() {
        return this.emojiButton[0];
    }

    public EmojiView getEmojiView() {
        return this.emojiView;
    }

    public TrendingStickersAlert getTrendingStickersAlert() {
        return this.trendingStickersAlert;
    }

    public void updateColors() {
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = this.sendPopupLayout;
        if (actionBarPopupWindowLayout != null) {
            int childCount = actionBarPopupWindowLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.sendPopupLayout.getChildAt(i);
                if (childAt instanceof ActionBarMenuSubItem) {
                    ActionBarMenuSubItem actionBarMenuSubItem = (ActionBarMenuSubItem) childAt;
                    actionBarMenuSubItem.setColors(getThemedColor("actionBarDefaultSubmenuItem"), getThemedColor("actionBarDefaultSubmenuItemIcon"));
                    actionBarMenuSubItem.setSelectorColor(getThemedColor("dialogButtonSelector"));
                }
            }
            this.sendPopupLayout.setBackgroundColor(getThemedColor("actionBarDefaultSubmenuBackground"));
            ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
            if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
                this.sendPopupLayout.invalidate();
            }
        }
        updateRecordedDeleteIconColors();
        this.recordCircle.updateColors();
        this.recordDot.updateColors();
        this.slideText.updateColors();
        this.recordTimerView.updateColors();
        this.videoTimelineView.updateColors();
        NumberTextView numberTextView = this.captionLimitView;
        if (numberTextView != null && this.messageEditText != null) {
            if (this.codePointCount - this.currentLimit < 0) {
                numberTextView.setTextColor(getThemedColor("windowBackgroundWhiteRedText"));
            } else {
                numberTextView.setTextColor(getThemedColor("windowBackgroundWhiteGrayText"));
            }
        }
        int themedColor = getThemedColor("chat_messagePanelVoicePressed");
        this.doneCheckDrawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.setAlphaComponent(themedColor, (int) (Color.alpha(themedColor) * ((this.doneButtonEnabledProgress * 0.42f) + 0.58f))), PorterDuff.Mode.MULTIPLY));
        BotCommandsMenuContainer botCommandsMenuContainer = this.botCommandsMenuContainer;
        if (botCommandsMenuContainer != null) {
            botCommandsMenuContainer.updateColors();
        }
        BotKeyboardView botKeyboardView = this.botKeyboardView;
        if (botKeyboardView != null) {
            botKeyboardView.updateColors();
        }
        for (int i2 = 0; i2 < 2; i2++) {
            this.emojiButton[i2].setColorFilter(new PorterDuffColorFilter(getThemedColor("chat_messagePanelIcons"), PorterDuff.Mode.MULTIPLY));
            if (Build.VERSION.SDK_INT >= 21) {
                this.emojiButton[i2].setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor("listSelectorSDK21")));
            }
        }
    }

    private void updateRecordedDeleteIconColors() {
        int themedColor = getThemedColor("chat_recordedVoiceDot");
        int themedColor2 = getThemedColor("chat_messagePanelBackground");
        int themedColor3 = getThemedColor("chat_messagePanelVoiceDelete");
        this.recordDeleteImageView.setLayerColor("Cup Red.**", themedColor);
        this.recordDeleteImageView.setLayerColor("Box Red.**", themedColor);
        this.recordDeleteImageView.setLayerColor("Cup Grey.**", themedColor3);
        this.recordDeleteImageView.setLayerColor("Box Grey.**", themedColor3);
        this.recordDeleteImageView.setLayerColor("Line 1.**", themedColor2);
        this.recordDeleteImageView.setLayerColor("Line 2.**", themedColor2);
        this.recordDeleteImageView.setLayerColor("Line 3.**", themedColor2);
    }

    public void setFieldText(CharSequence charSequence) {
        setFieldText(charSequence, true);
    }

    public void setFieldText(CharSequence charSequence, boolean z) {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate;
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null) {
            return;
        }
        this.ignoreTextChange = z;
        editTextCaption.setText(charSequence);
        EditTextCaption editTextCaption2 = this.messageEditText;
        editTextCaption2.setSelection(editTextCaption2.getText().length());
        this.ignoreTextChange = false;
        if (!z || (chatActivityEnterViewDelegate = this.delegate) == null) {
            return;
        }
        chatActivityEnterViewDelegate.onTextChanged(this.messageEditText.getText(), true);
    }

    public void setSelection(int i) {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null) {
            return;
        }
        editTextCaption.setSelection(i, editTextCaption.length());
    }

    public int getCursorPosition() {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null) {
            return 0;
        }
        return editTextCaption.getSelectionStart();
    }

    public int getSelectionLength() {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null) {
            return 0;
        }
        try {
            return editTextCaption.getSelectionEnd() - this.messageEditText.getSelectionStart();
        } catch (Exception e) {
            FileLog.e(e);
            return 0;
        }
    }

    public void replaceWithText(int i, int i2, CharSequence charSequence, boolean z) {
        try {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.messageEditText.getText());
            spannableStringBuilder.replace(i, i2 + i, charSequence);
            if (z) {
                Emoji.replaceEmoji(spannableStringBuilder, this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            }
            this.messageEditText.setText(spannableStringBuilder);
            this.messageEditText.setSelection(i + charSequence.length());
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void setFieldFocused() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.parentActivity.getSystemService("accessibility");
        if (this.messageEditText == null || accessibilityManager.isTouchExplorationEnabled()) {
            return;
        }
        try {
            this.messageEditText.requestFocus();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void setFieldFocused(boolean z) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.parentActivity.getSystemService("accessibility");
        if (this.messageEditText == null || accessibilityManager.isTouchExplorationEnabled()) {
            return;
        }
        if (z) {
            if (this.searchingType != 0 || this.messageEditText.isFocused()) {
                return;
            }
            ChatActivityEnterView$$ExternalSyntheticLambda46 chatActivityEnterView$$ExternalSyntheticLambda46 = new ChatActivityEnterView$$ExternalSyntheticLambda46(this);
            this.focusRunnable = chatActivityEnterView$$ExternalSyntheticLambda46;
            AndroidUtilities.runOnUIThread(chatActivityEnterView$$ExternalSyntheticLambda46, 600L);
            return;
        }
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null || !editTextCaption.isFocused()) {
            return;
        }
        if (this.keyboardVisible && !this.isPaused) {
            return;
        }
        this.messageEditText.clearFocus();
    }

    public /* synthetic */ void lambda$setFieldFocused$44() {
        EditTextCaption editTextCaption;
        ActionBarLayout layersActionBarLayout;
        this.focusRunnable = null;
        boolean z = true;
        if (AndroidUtilities.isTablet()) {
            Activity activity = this.parentActivity;
            if ((activity instanceof LaunchActivity) && (layersActionBarLayout = ((LaunchActivity) activity).getLayersActionBarLayout()) != null && layersActionBarLayout.getVisibility() == 0) {
                z = false;
            }
        }
        if (this.isPaused || !z || (editTextCaption = this.messageEditText) == null) {
            return;
        }
        try {
            editTextCaption.requestFocus();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public boolean hasText() {
        EditTextCaption editTextCaption = this.messageEditText;
        return editTextCaption != null && editTextCaption.length() > 0;
    }

    public EditTextCaption getEditField() {
        return this.messageEditText;
    }

    public CharSequence getDraftMessage() {
        if (this.editingMessageObject != null) {
            if (!TextUtils.isEmpty(this.draftMessage)) {
                return this.draftMessage;
            }
            return null;
        } else if (!hasText()) {
            return null;
        } else {
            return this.messageEditText.getText();
        }
    }

    public CharSequence getFieldText() {
        if (hasText()) {
            return this.messageEditText.getText();
        }
        return null;
    }

    public void updateScheduleButton(boolean z) {
        boolean z2;
        ImageView imageView;
        ImageView imageView2;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        int i = 0;
        if (DialogObject.isChatDialog(this.dialog_id)) {
            TLRPC$Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
            SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
            this.silent = notificationsSettings.getBoolean("silent_" + this.dialog_id, false);
            z2 = ChatObject.isChannel(chat) && (chat.creator || ((tLRPC$TL_chatAdminRights = chat.admin_rights) != null && tLRPC$TL_chatAdminRights.post_messages)) && !chat.megagroup;
            this.canWriteToChannel = z2;
            if (this.notifyButton != null) {
                if (this.notifySilentDrawable == null) {
                    this.notifySilentDrawable = new CrossOutDrawable(getContext(), 2131165540, "chat_messagePanelIcons");
                }
                this.notifySilentDrawable.setCrossOut(this.silent, false);
                this.notifyButton.setImageDrawable(this.notifySilentDrawable);
            } else {
                z2 = false;
            }
            LinearLayout linearLayout = this.attachLayout;
            if (linearLayout != null) {
                updateFieldRight(linearLayout.getVisibility() == 0 ? 1 : 0);
            }
        } else {
            z2 = false;
        }
        boolean z3 = this.delegate != null && !isInScheduleMode() && this.delegate.hasScheduledMessages();
        boolean z4 = z3 && !this.scheduleButtonHidden && !this.recordingAudioVideo;
        ImageView imageView3 = this.scheduledButton;
        float f = 96.0f;
        if (imageView3 != null) {
            if ((imageView3.getTag() != null && z4) || (this.scheduledButton.getTag() == null && !z4)) {
                if (this.notifyButton == null) {
                    return;
                }
                if (z3 || !z2 || this.scheduledButton.getVisibility() == 0) {
                    i = 8;
                }
                if (i == this.notifyButton.getVisibility()) {
                    return;
                }
                this.notifyButton.setVisibility(i);
                LinearLayout linearLayout2 = this.attachLayout;
                if (linearLayout2 == null) {
                    return;
                }
                ImageView imageView4 = this.botButton;
                if ((imageView4 == null || imageView4.getVisibility() == 8) && ((imageView2 = this.notifyButton) == null || imageView2.getVisibility() == 8)) {
                    f = 48.0f;
                }
                linearLayout2.setPivotX(AndroidUtilities.dp(f));
                return;
            }
            this.scheduledButton.setTag(z4 ? 1 : null);
        }
        AnimatorSet animatorSet = this.scheduledButtonAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.scheduledButtonAnimation = null;
        }
        float f2 = 0.0f;
        float f3 = 0.1f;
        if (!z || z2) {
            ImageView imageView5 = this.scheduledButton;
            if (imageView5 != null) {
                imageView5.setVisibility(z4 ? 0 : 8);
                ImageView imageView6 = this.scheduledButton;
                if (z4) {
                    f2 = 1.0f;
                }
                imageView6.setAlpha(f2);
                this.scheduledButton.setScaleX(z4 ? 1.0f : 0.1f);
                ImageView imageView7 = this.scheduledButton;
                if (z4) {
                    f3 = 1.0f;
                }
                imageView7.setScaleY(f3);
                ImageView imageView8 = this.notifyButton;
                if (imageView8 != null) {
                    if (!z2 || this.scheduledButton.getVisibility() == 0) {
                        i = 8;
                    }
                    imageView8.setVisibility(i);
                }
            }
        } else {
            ImageView imageView9 = this.scheduledButton;
            if (imageView9 != null) {
                if (z4) {
                    imageView9.setVisibility(0);
                }
                this.scheduledButton.setPivotX(AndroidUtilities.dp(24.0f));
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.scheduledButtonAnimation = animatorSet2;
                Animator[] animatorArr = new Animator[3];
                ImageView imageView10 = this.scheduledButton;
                Property property = View.ALPHA;
                float[] fArr = new float[1];
                if (z4) {
                    f2 = 1.0f;
                }
                fArr[0] = f2;
                animatorArr[0] = ObjectAnimator.ofFloat(imageView10, property, fArr);
                ImageView imageView11 = this.scheduledButton;
                Property property2 = View.SCALE_X;
                float[] fArr2 = new float[1];
                fArr2[0] = z4 ? 1.0f : 0.1f;
                animatorArr[1] = ObjectAnimator.ofFloat(imageView11, property2, fArr2);
                ImageView imageView12 = this.scheduledButton;
                Property property3 = View.SCALE_Y;
                float[] fArr3 = new float[1];
                if (z4) {
                    f3 = 1.0f;
                }
                fArr3[0] = f3;
                animatorArr[2] = ObjectAnimator.ofFloat(imageView12, property3, fArr3);
                animatorSet2.playTogether(animatorArr);
                this.scheduledButtonAnimation.setDuration(180L);
                this.scheduledButtonAnimation.addListener(new AnonymousClass53(z4));
                this.scheduledButtonAnimation.start();
            }
        }
        LinearLayout linearLayout3 = this.attachLayout;
        if (linearLayout3 != null) {
            ImageView imageView13 = this.botButton;
            if ((imageView13 == null || imageView13.getVisibility() == 8) && ((imageView = this.notifyButton) == null || imageView.getVisibility() == 8)) {
                f = 48.0f;
            }
            linearLayout3.setPivotX(AndroidUtilities.dp(f));
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$53 */
    /* loaded from: classes3.dex */
    public class AnonymousClass53 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$visible;

        AnonymousClass53(boolean z) {
            ChatActivityEnterView.this = r1;
            this.val$visible = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ChatActivityEnterView.this.scheduledButtonAnimation = null;
            if (!this.val$visible) {
                ChatActivityEnterView.this.scheduledButton.setVisibility(8);
            }
        }
    }

    public void updateSendAsButton() {
        updateSendAsButton(true);
    }

    public void updateSendAsButton(boolean z) {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || this.delegate == null) {
            return;
        }
        TLRPC$ChatFull chatFull = chatActivity.getMessagesController().getChatFull(-this.dialog_id);
        TLRPC$Peer tLRPC$Peer = chatFull != null ? chatFull.default_send_as : null;
        if (tLRPC$Peer == null && this.delegate.getSendAsPeers() != null && !this.delegate.getSendAsPeers().peers.isEmpty()) {
            tLRPC$Peer = this.delegate.getSendAsPeers().peers.get(0);
        }
        if (tLRPC$Peer != null) {
            if (tLRPC$Peer.channel_id != 0) {
                TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(tLRPC$Peer.channel_id));
                if (chat != null) {
                    this.senderSelectView.setAvatar(chat);
                }
            } else {
                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$Peer.user_id));
                if (user != null) {
                    this.senderSelectView.setAvatar(user);
                }
            }
        }
        boolean z2 = this.senderSelectView.getVisibility() == 0;
        boolean z3 = tLRPC$Peer != null && (this.delegate.getSendAsPeers() == null || this.delegate.getSendAsPeers().peers.size() > 1) && !isEditingMessage() && !isRecordingAudioVideo() && this.recordedAudioPanel.getVisibility() != 0;
        int dp = AndroidUtilities.dp(2.0f);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.senderSelectView.getLayoutParams();
        float f = 0.0f;
        float f2 = z3 ? 0.0f : 1.0f;
        float f3 = z3 ? ((-this.senderSelectView.getLayoutParams().width) - marginLayoutParams.leftMargin) - dp : 0.0f;
        float f4 = z3 ? 1.0f : 0.0f;
        float f5 = z3 ? 0.0f : ((-this.senderSelectView.getLayoutParams().width) - marginLayoutParams.leftMargin) - dp;
        if (z2 == z3) {
            return;
        }
        ValueAnimator valueAnimator = (ValueAnimator) this.senderSelectView.getTag();
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.senderSelectView.setTag(null);
        }
        if (this.parentFragment.getOtherSameChatsDiff() == 0 && this.parentFragment.fragmentOpened && z) {
            ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(150L);
            this.senderSelectView.setTranslationX(f3);
            this.messageEditText.setTranslationX(this.senderSelectView.getTranslationX());
            float f6 = f3;
            duration.addUpdateListener(new ChatActivityEnterView$$ExternalSyntheticLambda5(this, f2, f4, f6, f5));
            duration.addListener(new AnonymousClass54(z3, f2, f6, f4, f5));
            duration.start();
            this.senderSelectView.setTag(duration);
            return;
        }
        this.senderSelectView.setVisibility(z3 ? 0 : 8);
        this.senderSelectView.setTranslationX(f5);
        if (z3) {
            f = f5;
        }
        for (ImageView imageView : this.emojiButton) {
            imageView.setTranslationX(f);
        }
        this.messageEditText.setTranslationX(f);
        this.senderSelectView.setAlpha(f4);
        this.senderSelectView.setTag(null);
    }

    public /* synthetic */ void lambda$updateSendAsButton$45(float f, float f2, float f3, float f4, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.senderSelectView.setAlpha(f + ((f2 - f) * floatValue));
        this.senderSelectView.setTranslationX(f3 + ((f4 - f3) * floatValue));
        for (ImageView imageView : this.emojiButton) {
            imageView.setTranslationX(this.senderSelectView.getTranslationX());
        }
        this.messageEditText.setTranslationX(this.senderSelectView.getTranslationX());
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$54 */
    /* loaded from: classes3.dex */
    public class AnonymousClass54 extends AnimatorListenerAdapter {
        final /* synthetic */ float val$endAlpha;
        final /* synthetic */ float val$endX;
        final /* synthetic */ boolean val$isVisible;
        final /* synthetic */ float val$startAlpha;
        final /* synthetic */ float val$startX;

        AnonymousClass54(boolean z, float f, float f2, float f3, float f4) {
            ChatActivityEnterView.this = r1;
            this.val$isVisible = z;
            this.val$startAlpha = f;
            this.val$startX = f2;
            this.val$endAlpha = f3;
            this.val$endX = f4;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (this.val$isVisible) {
                ChatActivityEnterView.this.senderSelectView.setVisibility(0);
            }
            ChatActivityEnterView.this.senderSelectView.setAlpha(this.val$startAlpha);
            ChatActivityEnterView.this.senderSelectView.setTranslationX(this.val$startX);
            for (ImageView imageView : ChatActivityEnterView.this.emojiButton) {
                imageView.setTranslationX(ChatActivityEnterView.this.senderSelectView.getTranslationX());
            }
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            chatActivityEnterView.messageEditText.setTranslationX(chatActivityEnterView.senderSelectView.getTranslationX());
            if (ChatActivityEnterView.this.botCommandsMenuButton.getTag() == null) {
                ChatActivityEnterView.this.animationParamsX.clear();
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (!this.val$isVisible) {
                ChatActivityEnterView.this.senderSelectView.setVisibility(8);
                for (ImageView imageView : ChatActivityEnterView.this.emojiButton) {
                    imageView.setTranslationX(0.0f);
                }
                ChatActivityEnterView.this.messageEditText.setTranslationX(0.0f);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (this.val$isVisible) {
                ChatActivityEnterView.this.senderSelectView.setVisibility(0);
            } else {
                ChatActivityEnterView.this.senderSelectView.setVisibility(8);
            }
            ChatActivityEnterView.this.senderSelectView.setAlpha(this.val$endAlpha);
            ChatActivityEnterView.this.senderSelectView.setTranslationX(this.val$endX);
            for (ImageView imageView : ChatActivityEnterView.this.emojiButton) {
                imageView.setTranslationX(ChatActivityEnterView.this.senderSelectView.getTranslationX());
            }
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            chatActivityEnterView.messageEditText.setTranslationX(chatActivityEnterView.senderSelectView.getTranslationX());
            ChatActivityEnterView.this.requestLayout();
        }
    }

    public boolean onBotWebViewBackPressed() {
        BotWebViewMenuContainer botWebViewMenuContainer = this.botWebViewMenuContainer;
        return botWebViewMenuContainer != null && botWebViewMenuContainer.onBackPressed();
    }

    public boolean hasBotWebView() {
        return this.botMenuButtonType == BotMenuButtonType.WEB_VIEW;
    }

    private void updateBotButton(boolean z) {
        ImageView imageView;
        if (this.botButton == null) {
            return;
        }
        if (!this.parentFragment.openAnimationEnded) {
            z = false;
        }
        boolean hasBotWebView = hasBotWebView();
        boolean z2 = this.botMenuButtonType != BotMenuButtonType.NO_BUTTON && this.dialog_id > 0;
        boolean z3 = this.botButton.getVisibility() == 0;
        if (hasBotWebView || this.hasBotCommands || this.botReplyMarkup != null) {
            if (this.botReplyMarkup != null) {
                if (isPopupShowing() && this.currentPopupContentType == 1) {
                    if (this.botButton.getVisibility() != 8) {
                        this.botButton.setVisibility(8);
                    }
                } else {
                    if (this.botButton.getVisibility() != 0) {
                        this.botButton.setVisibility(0);
                    }
                    this.botButtonDrawable.setIcon(2131165529, true);
                    this.botButton.setContentDescription(LocaleController.getString("AccDescrBotKeyboard", 2131623963));
                }
            } else if (!z2) {
                this.botButtonDrawable.setIcon(2131165528, true);
                this.botButton.setContentDescription(LocaleController.getString("AccDescrBotCommands", 2131623962));
                this.botButton.setVisibility(0);
            } else {
                this.botButton.setVisibility(8);
            }
        } else {
            this.botButton.setVisibility(8);
        }
        BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
        boolean z4 = botCommandsMenuView.isWebView;
        botCommandsMenuView.setWebView(this.botMenuButtonType == BotMenuButtonType.WEB_VIEW);
        boolean menuText = this.botCommandsMenuButton.setMenuText(this.botMenuButtonType == BotMenuButtonType.COMMANDS ? LocaleController.getString(2131624754) : this.botMenuWebViewTitle);
        AndroidUtilities.updateViewVisibilityAnimated(this.botCommandsMenuButton, z2, 0.5f, z);
        if ((((this.botButton.getVisibility() == 0) == z3 && !menuText && z4 == this.botCommandsMenuButton.isWebView) ? false : true) && z) {
            beginDelayedTransition();
            boolean z5 = this.botButton.getVisibility() == 0;
            if (z5 != z3) {
                this.botButton.setVisibility(0);
                if (z5) {
                    this.botButton.setAlpha(0.0f);
                    this.botButton.setScaleX(0.1f);
                    this.botButton.setScaleY(0.1f);
                } else if (!z5) {
                    this.botButton.setAlpha(1.0f);
                    this.botButton.setScaleX(1.0f);
                    this.botButton.setScaleY(1.0f);
                }
                AndroidUtilities.updateViewVisibilityAnimated(this.botButton, z5, 0.1f, true);
            }
        }
        updateFieldRight(2);
        LinearLayout linearLayout = this.attachLayout;
        ImageView imageView2 = this.botButton;
        linearLayout.setPivotX(AndroidUtilities.dp(((imageView2 == null || imageView2.getVisibility() == 8) && ((imageView = this.notifyButton) == null || imageView.getVisibility() == 8)) ? 48.0f : 96.0f));
    }

    public boolean isRtlText() {
        try {
            return this.messageEditText.getLayout().getParagraphDirection(0) == -1;
        } catch (Throwable unused) {
            return false;
        }
    }

    public void updateBotWebView(boolean z) {
        this.botCommandsMenuButton.setWebView(hasBotWebView());
        updateBotButton(z);
    }

    public void setBotsCount(int i, boolean z, boolean z2) {
        this.botCount = i;
        if (this.hasBotCommands != z) {
            this.hasBotCommands = z;
            updateBotButton(z2);
        }
    }

    public void setButtons(MessageObject messageObject) {
        setButtons(messageObject, true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x00a0, code lost:
        if (r7.getInt("answered_" + r5.dialog_id, 0) == r6.getId()) goto L38;
     */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x006a  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00bf  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setButtons(MessageObject messageObject, boolean z) {
        TLRPC$TL_replyKeyboardMarkup tLRPC$TL_replyKeyboardMarkup;
        MessageObject messageObject2 = this.replyingMessageObject;
        if (messageObject2 != null && messageObject2 == this.botButtonsMessageObject && messageObject2 != messageObject) {
            this.botMessageObject = messageObject;
        } else if (this.botButton == null) {
        } else {
            MessageObject messageObject3 = this.botButtonsMessageObject;
            if (messageObject3 != null && messageObject3 == messageObject) {
                return;
            }
            if (messageObject3 == null && messageObject == null) {
                return;
            }
            boolean z2 = false;
            if (this.botKeyboardView == null) {
                AnonymousClass55 anonymousClass55 = new AnonymousClass55(this.parentActivity, this.resourcesProvider);
                this.botKeyboardView = anonymousClass55;
                anonymousClass55.setVisibility(8);
                this.botKeyboardViewVisible = false;
                this.botKeyboardView.setDelegate(new ChatActivityEnterView$$ExternalSyntheticLambda57(this));
                SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierLayout;
                sizeNotifierFrameLayout.addView(this.botKeyboardView, sizeNotifierFrameLayout.getChildCount() - 1);
            }
            this.botButtonsMessageObject = messageObject;
            if (messageObject != null) {
                TLRPC$ReplyMarkup tLRPC$ReplyMarkup = messageObject.messageOwner.reply_markup;
                if (tLRPC$ReplyMarkup instanceof TLRPC$TL_replyKeyboardMarkup) {
                    tLRPC$TL_replyKeyboardMarkup = (TLRPC$TL_replyKeyboardMarkup) tLRPC$ReplyMarkup;
                    this.botReplyMarkup = tLRPC$TL_replyKeyboardMarkup;
                    BotKeyboardView botKeyboardView = this.botKeyboardView;
                    android.graphics.Point point = AndroidUtilities.displaySize;
                    botKeyboardView.setPanelHeight(point.x <= point.y ? this.keyboardHeightLand : this.keyboardHeight);
                    if (this.botReplyMarkup == null) {
                        SharedPreferences mainSettings = MessagesController.getMainSettings(this.currentAccount);
                        if (this.botButtonsMessageObject != this.replyingMessageObject && this.botReplyMarkup.single_use) {
                        }
                        z2 = true;
                        if (z2 && this.messageEditText.length() == 0 && !isPopupShowing()) {
                            showPopup(1, 1);
                        }
                        this.botKeyboardView.setButtons(this.botReplyMarkup);
                    } else if (isPopupShowing() && this.currentPopupContentType == 1) {
                        if (z) {
                            this.clearBotButtonsOnKeyboardOpen = true;
                            openKeyboardInternal();
                        } else {
                            showPopup(0, 1);
                        }
                    }
                    updateBotButton(true);
                }
            }
            tLRPC$TL_replyKeyboardMarkup = null;
            this.botReplyMarkup = tLRPC$TL_replyKeyboardMarkup;
            BotKeyboardView botKeyboardView2 = this.botKeyboardView;
            android.graphics.Point point2 = AndroidUtilities.displaySize;
            botKeyboardView2.setPanelHeight(point2.x <= point2.y ? this.keyboardHeightLand : this.keyboardHeight);
            if (this.botReplyMarkup == null) {
            }
            updateBotButton(true);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$55 */
    /* loaded from: classes3.dex */
    public class AnonymousClass55 extends BotKeyboardView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass55(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
            ChatActivityEnterView.this = r1;
        }

        @Override // android.view.View
        public void setTranslationY(float f) {
            super.setTranslationY(f);
            if (ChatActivityEnterView.this.panelAnimation == null || ChatActivityEnterView.this.animatingContentType != 1) {
                return;
            }
            ChatActivityEnterView.this.delegate.bottomPanelTranslationYChanged(f);
        }
    }

    public /* synthetic */ void lambda$setButtons$46(TLRPC$KeyboardButton tLRPC$KeyboardButton) {
        MessageObject messageObject = this.replyingMessageObject;
        if (messageObject == null) {
            messageObject = DialogObject.isChatDialog(this.dialog_id) ? this.botButtonsMessageObject : null;
        }
        MessageObject messageObject2 = this.replyingMessageObject;
        if (messageObject2 == null) {
            messageObject2 = this.botButtonsMessageObject;
        }
        boolean didPressedBotButton = didPressedBotButton(tLRPC$KeyboardButton, messageObject, messageObject2);
        if (this.replyingMessageObject != null) {
            openKeyboardInternal();
            setButtons(this.botMessageObject, false);
        } else {
            MessageObject messageObject3 = this.botButtonsMessageObject;
            if (messageObject3 != null && messageObject3.messageOwner.reply_markup.single_use) {
                if (didPressedBotButton) {
                    openKeyboardInternal();
                } else {
                    showPopup(0, 0);
                }
                SharedPreferences.Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
                edit.putInt("answered_" + this.dialog_id, this.botButtonsMessageObject.getId()).commit();
            }
        }
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.onMessageSend(null, true, 0);
        }
    }

    public boolean didPressedBotButton(TLRPC$KeyboardButton tLRPC$KeyboardButton, MessageObject messageObject, MessageObject messageObject2) {
        if (tLRPC$KeyboardButton == null || messageObject2 == null) {
            return false;
        }
        if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButton) {
            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(tLRPC$KeyboardButton.text, this.dialog_id, messageObject, getThreadMessage(), null, false, null, null, null, true, 0, null);
        } else if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonUrl) {
            AlertsCreator.showOpenUrlAlert(this.parentFragment, tLRPC$KeyboardButton.url, false, true, this.resourcesProvider);
        } else if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonRequestPhone) {
            this.parentFragment.shareMyContact(2, messageObject2);
        } else {
            Boolean bool = null;
            if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonRequestPoll) {
                ChatActivity chatActivity = this.parentFragment;
                if ((tLRPC$KeyboardButton.flags & 1) != 0) {
                    bool = Boolean.valueOf(tLRPC$KeyboardButton.quiz);
                }
                chatActivity.openPollCreate(bool);
                return false;
            } else if ((tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonWebView) || (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonSimpleWebView)) {
                TLRPC$Message tLRPC$Message = messageObject2.messageOwner;
                long j = tLRPC$Message.via_bot_id;
                if (j == 0) {
                    j = tLRPC$Message.from_id.user_id;
                }
                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j));
                AnonymousClass56 anonymousClass56 = new AnonymousClass56(messageObject2, j, tLRPC$KeyboardButton, messageObject);
                if (SharedPrefsHelper.isWebViewConfirmShown(this.currentAccount, j)) {
                    anonymousClass56.run();
                } else {
                    new AlertDialog.Builder(this.parentFragment.getParentActivity()).setTitle(LocaleController.getString(2131624720)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BotOpenPageMessage", 2131624719, UserObject.getUserName(user)))).setPositiveButton(LocaleController.getString(2131627075), new ChatActivityEnterView$$ExternalSyntheticLambda9(this, anonymousClass56, j)).setNegativeButton(LocaleController.getString(2131624819), null).show();
                }
            } else if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonRequestGeoLocation) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.parentActivity);
                builder.setTitle(LocaleController.getString("ShareYouLocationTitle", 2131628290));
                builder.setMessage(LocaleController.getString("ShareYouLocationInfo", 2131628288));
                builder.setPositiveButton(LocaleController.getString("OK", 2131627075), new ChatActivityEnterView$$ExternalSyntheticLambda10(this, messageObject2, tLRPC$KeyboardButton));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                this.parentFragment.showDialog(builder.create());
            } else if ((tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) || (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonGame) || (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonBuy) || (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonUrlAuth)) {
                SendMessagesHelper.getInstance(this.currentAccount).sendCallback(true, messageObject2, tLRPC$KeyboardButton, this.parentFragment);
            } else if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonSwitchInline) {
                if (this.parentFragment.processSwitchButton((TLRPC$TL_keyboardButtonSwitchInline) tLRPC$KeyboardButton)) {
                    return true;
                }
                if (tLRPC$KeyboardButton.same_peer) {
                    TLRPC$Message tLRPC$Message2 = messageObject2.messageOwner;
                    long j2 = tLRPC$Message2.from_id.user_id;
                    long j3 = tLRPC$Message2.via_bot_id;
                    if (j3 != 0) {
                        j2 = j3;
                    }
                    TLRPC$User user2 = this.accountInstance.getMessagesController().getUser(Long.valueOf(j2));
                    if (user2 == null) {
                        return true;
                    }
                    setFieldText("@" + user2.username + " " + tLRPC$KeyboardButton.query);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("onlySelect", true);
                    bundle.putInt("dialogsType", 1);
                    DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                    dialogsActivity.setDelegate(new ChatActivityEnterView$$ExternalSyntheticLambda60(this, messageObject2, tLRPC$KeyboardButton));
                    this.parentFragment.presentFragment(dialogsActivity);
                }
            } else if ((tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonUserProfile) && MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$KeyboardButton.user_id)) != null) {
                Bundle bundle2 = new Bundle();
                bundle2.putLong("user_id", tLRPC$KeyboardButton.user_id);
                this.parentFragment.presentFragment(new ProfileActivity(bundle2));
            }
        }
        return true;
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$56 */
    /* loaded from: classes3.dex */
    public class AnonymousClass56 implements Runnable {
        final /* synthetic */ long val$botId;
        final /* synthetic */ TLRPC$KeyboardButton val$button;
        final /* synthetic */ MessageObject val$messageObject;
        final /* synthetic */ MessageObject val$replyMessageObject;

        AnonymousClass56(MessageObject messageObject, long j, TLRPC$KeyboardButton tLRPC$KeyboardButton, MessageObject messageObject2) {
            ChatActivityEnterView.this = r1;
            this.val$messageObject = messageObject;
            this.val$botId = j;
            this.val$button = tLRPC$KeyboardButton;
            this.val$replyMessageObject = messageObject2;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ChatActivityEnterView.this.sizeNotifierLayout.measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
                AndroidUtilities.hideKeyboard(ChatActivityEnterView.this);
                AndroidUtilities.runOnUIThread(this, 150L);
                return;
            }
            BotWebViewSheet botWebViewSheet = new BotWebViewSheet(ChatActivityEnterView.this.getContext(), ChatActivityEnterView.this.resourcesProvider);
            botWebViewSheet.setParentActivity(ChatActivityEnterView.this.parentActivity);
            int i = ChatActivityEnterView.this.currentAccount;
            long j = this.val$messageObject.messageOwner.dialog_id;
            long j2 = this.val$botId;
            TLRPC$KeyboardButton tLRPC$KeyboardButton = this.val$button;
            String str = tLRPC$KeyboardButton.text;
            String str2 = tLRPC$KeyboardButton.url;
            boolean z = tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonSimpleWebView;
            MessageObject messageObject = this.val$replyMessageObject;
            botWebViewSheet.requestWebView(i, j, j2, str, str2, z ? 1 : 0, messageObject != null ? messageObject.messageOwner.id : 0, false);
            botWebViewSheet.show();
        }
    }

    public /* synthetic */ void lambda$didPressedBotButton$47(Runnable runnable, long j, DialogInterface dialogInterface, int i) {
        runnable.run();
        SharedPrefsHelper.setWebViewConfirmShown(this.currentAccount, j, true);
    }

    public /* synthetic */ void lambda$didPressedBotButton$48(MessageObject messageObject, TLRPC$KeyboardButton tLRPC$KeyboardButton, DialogInterface dialogInterface, int i) {
        if (Build.VERSION.SDK_INT >= 23 && this.parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            this.parentActivity.requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
            this.pendingMessageObject = messageObject;
            this.pendingLocationButton = tLRPC$KeyboardButton;
            return;
        }
        SendMessagesHelper.getInstance(this.currentAccount).sendCurrentLocation(messageObject, tLRPC$KeyboardButton);
    }

    public /* synthetic */ void lambda$didPressedBotButton$49(MessageObject messageObject, TLRPC$KeyboardButton tLRPC$KeyboardButton, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
        long j = tLRPC$Message.from_id.user_id;
        long j2 = tLRPC$Message.via_bot_id;
        if (j2 != 0) {
            j = j2;
        }
        TLRPC$User user = this.accountInstance.getMessagesController().getUser(Long.valueOf(j));
        if (user == null) {
            dialogsActivity.finishFragment();
            return;
        }
        long longValue = ((Long) arrayList.get(0)).longValue();
        MediaDataController mediaDataController = MediaDataController.getInstance(this.currentAccount);
        mediaDataController.saveDraft(longValue, 0, "@" + user.username + " " + tLRPC$KeyboardButton.query, null, null, true);
        if (longValue != this.dialog_id) {
            if (!DialogObject.isEncryptedDialog(longValue)) {
                Bundle bundle = new Bundle();
                if (DialogObject.isUserDialog(longValue)) {
                    bundle.putLong("user_id", longValue);
                } else {
                    bundle.putLong("chat_id", -longValue);
                }
                if (!this.accountInstance.getMessagesController().checkCanOpenChat(bundle, dialogsActivity)) {
                    return;
                }
                if (this.parentFragment.presentFragment(new ChatActivity(bundle), true)) {
                    if (AndroidUtilities.isTablet()) {
                        return;
                    }
                    this.parentFragment.removeSelfFromStack();
                    return;
                }
                dialogsActivity.finishFragment();
                return;
            }
            dialogsActivity.finishFragment();
            return;
        }
        dialogsActivity.finishFragment();
    }

    public boolean isPopupView(View view) {
        return view == this.botKeyboardView || view == this.emojiView;
    }

    public boolean isRecordCircle(View view) {
        return view == this.recordCircle;
    }

    public SizeNotifierFrameLayout getSizeNotifierLayout() {
        return this.sizeNotifierLayout;
    }

    private void createEmojiView() {
        if (this.emojiView != null) {
            return;
        }
        AnonymousClass57 anonymousClass57 = new AnonymousClass57(this.allowStickers, this.allowGifs, getContext(), true, this.info, this.sizeNotifierLayout, this.resourcesProvider);
        this.emojiView = anonymousClass57;
        anonymousClass57.setVisibility(8);
        this.emojiView.setShowing(false);
        this.emojiView.setDelegate(new AnonymousClass58());
        this.emojiView.setDragListener(new AnonymousClass59());
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierLayout;
        sizeNotifierFrameLayout.addView(this.emojiView, sizeNotifierFrameLayout.getChildCount() - 5);
        checkChannelRights();
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$57 */
    /* loaded from: classes3.dex */
    public class AnonymousClass57 extends EmojiView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass57(boolean z, boolean z2, Context context, boolean z3, TLRPC$ChatFull tLRPC$ChatFull, ViewGroup viewGroup, Theme.ResourcesProvider resourcesProvider) {
            super(z, z2, context, z3, tLRPC$ChatFull, viewGroup, resourcesProvider);
            ChatActivityEnterView.this = r10;
        }

        @Override // org.telegram.ui.Components.EmojiView, android.view.View
        public void setTranslationY(float f) {
            super.setTranslationY(f);
            if (ChatActivityEnterView.this.panelAnimation == null || ChatActivityEnterView.this.animatingContentType != 0) {
                return;
            }
            ChatActivityEnterView.this.delegate.bottomPanelTranslationYChanged(f);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$58 */
    /* loaded from: classes3.dex */
    public class AnonymousClass58 implements EmojiView.EmojiViewDelegate {
        AnonymousClass58() {
            ChatActivityEnterView.this = r1;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean onBackspace() {
            if (ChatActivityEnterView.this.messageEditText.length() == 0) {
                return false;
            }
            ChatActivityEnterView.this.messageEditText.dispatchKeyEvent(new KeyEvent(0, 67));
            return true;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onEmojiSelected(String str) {
            int selectionEnd = ChatActivityEnterView.this.messageEditText.getSelectionEnd();
            if (selectionEnd < 0) {
                selectionEnd = 0;
            }
            try {
                try {
                    ChatActivityEnterView.this.innerTextChange = 2;
                    CharSequence replaceEmoji = Emoji.replaceEmoji(str, ChatActivityEnterView.this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    EditTextCaption editTextCaption = ChatActivityEnterView.this.messageEditText;
                    editTextCaption.setText(editTextCaption.getText().insert(selectionEnd, replaceEmoji));
                    int length = selectionEnd + replaceEmoji.length();
                    ChatActivityEnterView.this.messageEditText.setSelection(length, length);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            } finally {
                ChatActivityEnterView.this.innerTextChange = 0;
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onStickerSelected(View view, TLRPC$Document tLRPC$Document, String str, Object obj, MessageObject.SendAnimationData sendAnimationData, boolean z, int i) {
            if (ChatActivityEnterView.this.trendingStickersAlert != null) {
                ChatActivityEnterView.this.trendingStickersAlert.dismiss();
                ChatActivityEnterView.this.trendingStickersAlert = null;
            }
            if (ChatActivityEnterView.this.slowModeTimer <= 0 || isInScheduleMode()) {
                if (ChatActivityEnterView.this.stickersExpanded) {
                    if (ChatActivityEnterView.this.searchingType != 0) {
                        ChatActivityEnterView.this.setSearchingTypeInternal(0, true);
                        ChatActivityEnterView.this.emojiView.closeSearch(true, MessageObject.getStickerSetId(tLRPC$Document));
                        ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                    }
                    ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                }
                ChatActivityEnterView.this.lambda$onStickerSelected$50(tLRPC$Document, str, obj, sendAnimationData, false, z, i);
                if (!DialogObject.isEncryptedDialog(ChatActivityEnterView.this.dialog_id) || !MessageObject.isGifDocument(tLRPC$Document)) {
                    return;
                }
                ChatActivityEnterView.this.accountInstance.getMessagesController().saveGif(obj, tLRPC$Document);
            } else if (ChatActivityEnterView.this.delegate == null) {
            } else {
                ChatActivityEnterView.this.delegate.onUpdateSlowModeButton(view != null ? view : ChatActivityEnterView.this.slowModeButton, true, ChatActivityEnterView.this.slowModeButton.getText());
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onStickersSettingsClick() {
            if (ChatActivityEnterView.this.parentFragment != null) {
                ChatActivityEnterView.this.parentFragment.presentFragment(new StickersActivity(0));
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        /* renamed from: onGifSelected */
        public void lambda$onGifSelected$0(View view, Object obj, String str, Object obj2, boolean z, int i) {
            if (!isInScheduleMode() || i != 0) {
                if (ChatActivityEnterView.this.slowModeTimer <= 0 || isInScheduleMode()) {
                    if (ChatActivityEnterView.this.stickersExpanded) {
                        if (ChatActivityEnterView.this.searchingType != 0) {
                            ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                        }
                        ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                    }
                    if (obj instanceof TLRPC$Document) {
                        TLRPC$Document tLRPC$Document = (TLRPC$Document) obj;
                        SendMessagesHelper.getInstance(ChatActivityEnterView.this.currentAccount).sendSticker(tLRPC$Document, str, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), obj2, null, z, i);
                        MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).addRecentGif(tLRPC$Document, (int) (System.currentTimeMillis() / 1000), true);
                        if (DialogObject.isEncryptedDialog(ChatActivityEnterView.this.dialog_id)) {
                            ChatActivityEnterView.this.accountInstance.getMessagesController().saveGif(obj2, tLRPC$Document);
                        }
                    } else if (obj instanceof TLRPC$BotInlineResult) {
                        TLRPC$BotInlineResult tLRPC$BotInlineResult = (TLRPC$BotInlineResult) obj;
                        if (tLRPC$BotInlineResult.document != null) {
                            MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).addRecentGif(tLRPC$BotInlineResult.document, (int) (System.currentTimeMillis() / 1000), false);
                            if (DialogObject.isEncryptedDialog(ChatActivityEnterView.this.dialog_id)) {
                                ChatActivityEnterView.this.accountInstance.getMessagesController().saveGif(obj2, tLRPC$BotInlineResult.document);
                            }
                        }
                        TLRPC$User tLRPC$User = (TLRPC$User) obj2;
                        HashMap hashMap = new HashMap();
                        hashMap.put("id", tLRPC$BotInlineResult.id);
                        hashMap.put("query_id", "" + tLRPC$BotInlineResult.query_id);
                        hashMap.put("force_gif", "1");
                        SendMessagesHelper.prepareSendingBotContextResult(ChatActivityEnterView.this.accountInstance, tLRPC$BotInlineResult, hashMap, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), z, i);
                        if (ChatActivityEnterView.this.searchingType != 0) {
                            ChatActivityEnterView.this.setSearchingTypeInternal(0, true);
                            ChatActivityEnterView.this.emojiView.closeSearch(true);
                            ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                        }
                    }
                    if (ChatActivityEnterView.this.delegate == null) {
                        return;
                    }
                    ChatActivityEnterView.this.delegate.onMessageSend(null, z, i);
                    return;
                } else if (ChatActivityEnterView.this.delegate == null) {
                    return;
                } else {
                    ChatActivityEnterView.this.delegate.onUpdateSlowModeButton(view != null ? view : ChatActivityEnterView.this.slowModeButton, true, ChatActivityEnterView.this.slowModeButton.getText());
                    return;
                }
            }
            AlertsCreator.createScheduleDatePickerDialog(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.parentFragment.getDialogId(), new ChatActivityEnterView$58$$ExternalSyntheticLambda1(this, view, obj, str, obj2), ChatActivityEnterView.this.resourcesProvider);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onTabOpened(int i) {
            ChatActivityEnterView.this.delegate.onStickersTab(i == 3);
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            chatActivityEnterView.post(chatActivityEnterView.updateExpandabilityRunnable);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onClearEmojiRecent() {
            if (ChatActivityEnterView.this.parentFragment == null || ChatActivityEnterView.this.parentActivity == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.resourcesProvider);
            builder.setTitle(LocaleController.getString("AppName", 2131624375));
            builder.setMessage(LocaleController.getString("ClearRecentEmoji", 2131625149));
            builder.setPositiveButton(LocaleController.getString("ClearButton", 2131625132).toUpperCase(), new ChatActivityEnterView$58$$ExternalSyntheticLambda0(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
            ChatActivityEnterView.this.parentFragment.showDialog(builder.create());
        }

        public /* synthetic */ void lambda$onClearEmojiRecent$1(DialogInterface dialogInterface, int i) {
            ChatActivityEnterView.this.emojiView.clearRecentEmoji();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onShowStickerSet(TLRPC$StickerSet tLRPC$StickerSet, TLRPC$InputStickerSet tLRPC$InputStickerSet) {
            if (ChatActivityEnterView.this.trendingStickersAlert == null || ChatActivityEnterView.this.trendingStickersAlert.isDismissed()) {
                if (ChatActivityEnterView.this.parentFragment == null || ChatActivityEnterView.this.parentActivity == null) {
                    return;
                }
                if (tLRPC$StickerSet != null) {
                    tLRPC$InputStickerSet = new TLRPC$TL_inputStickerSetID();
                    tLRPC$InputStickerSet.access_hash = tLRPC$StickerSet.access_hash;
                    tLRPC$InputStickerSet.id = tLRPC$StickerSet.id;
                }
                ChatActivity chatActivity = ChatActivityEnterView.this.parentFragment;
                Activity activity = ChatActivityEnterView.this.parentActivity;
                ChatActivity chatActivity2 = ChatActivityEnterView.this.parentFragment;
                ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                chatActivity.showDialog(new StickersAlert(activity, chatActivity2, tLRPC$InputStickerSet, null, chatActivityEnterView, chatActivityEnterView.resourcesProvider));
                return;
            }
            ChatActivityEnterView.this.trendingStickersAlert.getLayout().showStickerSet(tLRPC$StickerSet, tLRPC$InputStickerSet);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onStickerSetAdd(TLRPC$StickerSetCovered tLRPC$StickerSetCovered) {
            MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).toggleStickerSet(ChatActivityEnterView.this.parentActivity, tLRPC$StickerSetCovered, 2, ChatActivityEnterView.this.parentFragment, false, false);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onStickerSetRemove(TLRPC$StickerSetCovered tLRPC$StickerSetCovered) {
            MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).toggleStickerSet(ChatActivityEnterView.this.parentActivity, tLRPC$StickerSetCovered, 0, ChatActivityEnterView.this.parentFragment, false, false);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onStickersGroupClick(long j) {
            if (ChatActivityEnterView.this.parentFragment != null) {
                if (AndroidUtilities.isTablet()) {
                    ChatActivityEnterView.this.hidePopup(false);
                }
                GroupStickersActivity groupStickersActivity = new GroupStickersActivity(j);
                groupStickersActivity.setInfo(ChatActivityEnterView.this.info);
                ChatActivityEnterView.this.parentFragment.presentFragment(groupStickersActivity);
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onSearchOpenClose(int i) {
            ChatActivityEnterView.this.setSearchingTypeInternal(i, true);
            if (i != 0) {
                ChatActivityEnterView.this.setStickersExpanded(true, true, false);
            }
            if (!ChatActivityEnterView.this.emojiTabOpen || ChatActivityEnterView.this.searchingType != 2) {
                return;
            }
            ChatActivityEnterView.this.checkStickresExpandHeight();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean isSearchOpened() {
            return ChatActivityEnterView.this.searchingType != 0;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean isExpanded() {
            return ChatActivityEnterView.this.stickersExpanded;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean canSchedule() {
            return ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.parentFragment.canScheduleMessage();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean isInScheduleMode() {
            return ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.parentFragment.isInScheduleMode();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public long getDialogId() {
            return ChatActivityEnterView.this.dialog_id;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public int getThreadId() {
            return ChatActivityEnterView.this.getThreadMessageId();
        }

        /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$58$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends TrendingStickersAlert {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, BaseFragment baseFragment, TrendingStickersLayout trendingStickersLayout, Theme.ResourcesProvider resourcesProvider) {
                super(context, baseFragment, trendingStickersLayout, resourcesProvider);
                AnonymousClass58.this = r1;
            }

            @Override // org.telegram.ui.Components.TrendingStickersAlert, org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface
            public void dismiss() {
                super.dismiss();
                if (ChatActivityEnterView.this.trendingStickersAlert == this) {
                    ChatActivityEnterView.this.trendingStickersAlert = null;
                }
                if (ChatActivityEnterView.this.delegate != null) {
                    ChatActivityEnterView.this.delegate.onTrendingStickersShowed(false);
                }
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void showTrendingStickersAlert(TrendingStickersLayout trendingStickersLayout) {
            if (ChatActivityEnterView.this.parentActivity == null || ChatActivityEnterView.this.parentFragment == null) {
                return;
            }
            ChatActivityEnterView.this.trendingStickersAlert = new AnonymousClass1(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.parentFragment, trendingStickersLayout, ChatActivityEnterView.this.resourcesProvider);
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onTrendingStickersShowed(true);
            }
            ChatActivityEnterView.this.trendingStickersAlert.show();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void invalidateEnterView() {
            ChatActivityEnterView.this.invalidate();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public float getProgressToSearchOpened() {
            return ChatActivityEnterView.this.searchToOpenProgress;
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$59 */
    /* loaded from: classes3.dex */
    public class AnonymousClass59 implements EmojiView.DragListener {
        int initialOffset;
        boolean wasExpanded;

        AnonymousClass59() {
            ChatActivityEnterView.this = r1;
        }

        @Override // org.telegram.ui.Components.EmojiView.DragListener
        public void onDragStart() {
            if (!allowDragging()) {
                return;
            }
            if (ChatActivityEnterView.this.stickersExpansionAnim != null) {
                ChatActivityEnterView.this.stickersExpansionAnim.cancel();
            }
            ChatActivityEnterView.this.stickersDragging = true;
            this.wasExpanded = ChatActivityEnterView.this.stickersExpanded;
            ChatActivityEnterView.this.stickersExpanded = true;
            int i = 0;
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.stopAllHeavyOperations, 1);
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            int height = chatActivityEnterView.sizeNotifierLayout.getHeight();
            if (Build.VERSION.SDK_INT >= 21) {
                i = AndroidUtilities.statusBarHeight;
            }
            chatActivityEnterView.stickersExpandedHeight = (((height - i) - ActionBar.getCurrentActionBarHeight()) - ChatActivityEnterView.this.getHeight()) + Theme.chat_composeShadowDrawable.getIntrinsicHeight();
            if (ChatActivityEnterView.this.searchingType == 2) {
                ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
                int i2 = chatActivityEnterView2.stickersExpandedHeight;
                int dp = AndroidUtilities.dp(120.0f);
                android.graphics.Point point = AndroidUtilities.displaySize;
                chatActivityEnterView2.stickersExpandedHeight = Math.min(i2, dp + (point.x > point.y ? ChatActivityEnterView.this.keyboardHeightLand : ChatActivityEnterView.this.keyboardHeight));
            }
            ChatActivityEnterView.this.emojiView.getLayoutParams().height = ChatActivityEnterView.this.stickersExpandedHeight;
            ChatActivityEnterView.this.emojiView.setLayerType(2, null);
            ChatActivityEnterView.this.sizeNotifierLayout.requestLayout();
            ChatActivityEnterView.this.sizeNotifierLayout.setForeground(new ScrimDrawable());
            this.initialOffset = (int) ChatActivityEnterView.this.getTranslationY();
            if (ChatActivityEnterView.this.delegate == null) {
                return;
            }
            ChatActivityEnterView.this.delegate.onStickersExpandedChange();
        }

        @Override // org.telegram.ui.Components.EmojiView.DragListener
        public void onDragEnd(float f) {
            if (!allowDragging()) {
                return;
            }
            ChatActivityEnterView.this.stickersDragging = false;
            if ((this.wasExpanded && f >= AndroidUtilities.dp(200.0f)) || ((!this.wasExpanded && f <= AndroidUtilities.dp(-200.0f)) || ((this.wasExpanded && ChatActivityEnterView.this.stickersExpansionProgress <= 0.6f) || (!this.wasExpanded && ChatActivityEnterView.this.stickersExpansionProgress >= 0.4f)))) {
                ChatActivityEnterView.this.setStickersExpanded(!this.wasExpanded, true, true);
            } else {
                ChatActivityEnterView.this.setStickersExpanded(this.wasExpanded, true, true);
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.DragListener
        public void onDragCancel() {
            if (!ChatActivityEnterView.this.stickersTabOpen) {
                return;
            }
            ChatActivityEnterView.this.stickersDragging = false;
            ChatActivityEnterView.this.setStickersExpanded(this.wasExpanded, true, false);
        }

        @Override // org.telegram.ui.Components.EmojiView.DragListener
        public void onDrag(int i) {
            int i2;
            ChatActivityEnterView chatActivityEnterView;
            if (!allowDragging()) {
                return;
            }
            android.graphics.Point point = AndroidUtilities.displaySize;
            float max = Math.max(Math.min(i + this.initialOffset, 0), -(ChatActivityEnterView.this.stickersExpandedHeight - (point.x > point.y ? ChatActivityEnterView.this.keyboardHeightLand : ChatActivityEnterView.this.keyboardHeight)));
            ChatActivityEnterView.this.emojiView.setTranslationY(max);
            ChatActivityEnterView.this.setTranslationY(max);
            ChatActivityEnterView.this.stickersExpansionProgress = max / (-(chatActivityEnterView.stickersExpandedHeight - i2));
            ChatActivityEnterView.this.sizeNotifierLayout.invalidate();
        }

        private boolean allowDragging() {
            return ChatActivityEnterView.this.stickersTabOpen && (ChatActivityEnterView.this.stickersExpanded || ChatActivityEnterView.this.messageEditText.length() <= 0) && ChatActivityEnterView.this.emojiView.areThereAnyStickers() && !ChatActivityEnterView.this.waitingForKeyboardOpen;
        }
    }

    @Override // org.telegram.ui.Components.StickersAlert.StickersAlertDelegate
    /* renamed from: onStickerSelected */
    public void lambda$onStickerSelected$50(TLRPC$Document tLRPC$Document, String str, Object obj, MessageObject.SendAnimationData sendAnimationData, boolean z, boolean z2, int i) {
        if (isInScheduleMode() && i == 0) {
            AlertsCreator.createScheduleDatePickerDialog(this.parentActivity, this.parentFragment.getDialogId(), new ChatActivityEnterView$$ExternalSyntheticLambda54(this, tLRPC$Document, str, obj, sendAnimationData, z), this.resourcesProvider);
        } else if (this.slowModeTimer > 0 && !isInScheduleMode()) {
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
            if (chatActivityEnterViewDelegate == null) {
                return;
            }
            SimpleTextView simpleTextView = this.slowModeButton;
            chatActivityEnterViewDelegate.onUpdateSlowModeButton(simpleTextView, true, simpleTextView.getText());
        } else {
            if (this.searchingType != 0) {
                setSearchingTypeInternal(0, true);
                this.emojiView.closeSearch(true);
                this.emojiView.hideSearchKeyboard();
            }
            setStickersExpanded(false, true, false);
            SendMessagesHelper.getInstance(this.currentAccount).sendSticker(tLRPC$Document, str, this.dialog_id, this.replyingMessageObject, getThreadMessage(), obj, sendAnimationData, z2, i);
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = this.delegate;
            if (chatActivityEnterViewDelegate2 != null) {
                chatActivityEnterViewDelegate2.onMessageSend(null, true, i);
            }
            if (z) {
                setFieldText("");
            }
            MediaDataController.getInstance(this.currentAccount).addRecentSticker(0, obj, tLRPC$Document, (int) (System.currentTimeMillis() / 1000), false);
        }
    }

    @Override // org.telegram.ui.Components.StickersAlert.StickersAlertDelegate
    public boolean canSchedule() {
        ChatActivity chatActivity = this.parentFragment;
        return chatActivity != null && chatActivity.canScheduleMessage();
    }

    @Override // org.telegram.ui.Components.StickersAlert.StickersAlertDelegate
    public boolean isInScheduleMode() {
        ChatActivity chatActivity = this.parentFragment;
        return chatActivity != null && chatActivity.isInScheduleMode();
    }

    public void addStickerToRecent(TLRPC$Document tLRPC$Document) {
        createEmojiView();
        this.emojiView.addRecentSticker(tLRPC$Document);
    }

    public void showEmojiView() {
        showPopup(1, 0);
    }

    public void showPopup(int i, int i2) {
        showPopup(i, i2, true);
    }

    private void showPopup(int i, int i2, boolean z) {
        BotKeyboardView botKeyboardView;
        EmojiView emojiView;
        View view;
        int i3;
        SizeNotifierFrameLayout sizeNotifierFrameLayout;
        if (i == 2) {
            return;
        }
        if (i == 1) {
            if (i2 == 0 && this.emojiView == null) {
                if (this.parentActivity == null) {
                    return;
                }
                createEmojiView();
            }
            if (i2 == 0) {
                if (this.emojiView.getParent() == null) {
                    this.sizeNotifierLayout.addView(this.emojiView, sizeNotifierFrameLayout.getChildCount() - 5);
                }
                if (this.emojiViewVisible) {
                    this.emojiView.getVisibility();
                }
                this.emojiView.setVisibility(0);
                this.emojiViewVisible = true;
                BotKeyboardView botKeyboardView2 = this.botKeyboardView;
                if (botKeyboardView2 == null || botKeyboardView2.getVisibility() == 8) {
                    i3 = 0;
                } else {
                    this.botKeyboardView.setVisibility(8);
                    this.botKeyboardViewVisible = false;
                    i3 = this.botKeyboardView.getMeasuredHeight();
                }
                this.emojiView.setShowing(true);
                view = this.emojiView;
                this.animatingContentType = 0;
            } else if (i2 == 1) {
                if (this.botKeyboardViewVisible) {
                    this.botKeyboardView.getVisibility();
                }
                this.botKeyboardViewVisible = true;
                EmojiView emojiView2 = this.emojiView;
                if (emojiView2 == null || emojiView2.getVisibility() == 8) {
                    i3 = 0;
                } else {
                    this.sizeNotifierLayout.removeView(this.emojiView);
                    this.emojiView.setVisibility(8);
                    this.emojiView.setShowing(false);
                    this.emojiViewVisible = false;
                    i3 = this.emojiView.getMeasuredHeight();
                }
                this.botKeyboardView.setVisibility(0);
                view = this.botKeyboardView;
                this.animatingContentType = 1;
            } else {
                i3 = 0;
                view = null;
            }
            this.currentPopupContentType = i2;
            if (this.keyboardHeight <= 0) {
                this.keyboardHeight = MessagesController.getGlobalEmojiSettings().getInt("kbd_height", AndroidUtilities.dp(200.0f));
            }
            if (this.keyboardHeightLand <= 0) {
                this.keyboardHeightLand = MessagesController.getGlobalEmojiSettings().getInt("kbd_height_land3", AndroidUtilities.dp(200.0f));
            }
            android.graphics.Point point = AndroidUtilities.displaySize;
            int i4 = point.x > point.y ? this.keyboardHeightLand : this.keyboardHeight;
            if (i2 == 1) {
                i4 = Math.min(this.botKeyboardView.getKeyboardHeight(), i4);
            }
            BotKeyboardView botKeyboardView3 = this.botKeyboardView;
            if (botKeyboardView3 != null) {
                botKeyboardView3.setPanelHeight(i4);
            }
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
            layoutParams.height = i4;
            view.setLayoutParams(layoutParams);
            if (!AndroidUtilities.isInMultiwindow) {
                AndroidUtilities.hideKeyboard(this.messageEditText);
            }
            SizeNotifierFrameLayout sizeNotifierFrameLayout2 = this.sizeNotifierLayout;
            if (sizeNotifierFrameLayout2 != null) {
                this.emojiPadding = i4;
                sizeNotifierFrameLayout2.requestLayout();
                setEmojiButtonImage(true, true);
                updateBotButton(true);
                onWindowSizeChanged();
                if (this.smoothKeyboard && !this.keyboardVisible && i4 != i3 && z) {
                    this.panelAnimation = new AnimatorSet();
                    float f = i4 - i3;
                    view.setTranslationY(f);
                    this.panelAnimation.playTogether(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, f, 0.0f));
                    this.panelAnimation.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                    this.panelAnimation.setDuration(250L);
                    this.panelAnimation.addListener(new AnonymousClass60());
                    AndroidUtilities.runOnUIThread(this.runEmojiPanelAnimation, 50L);
                    this.notificationsIndex = NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(this.notificationsIndex, null);
                    requestLayout();
                }
            }
        } else {
            if (this.emojiButton != null) {
                setEmojiButtonImage(false, true);
            }
            this.currentPopupContentType = -1;
            EmojiView emojiView3 = this.emojiView;
            if (emojiView3 != null) {
                if (i != 2 || AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow) {
                    if (this.smoothKeyboard && !this.keyboardVisible && !this.stickersExpanded) {
                        this.emojiViewVisible = true;
                        this.animatingContentType = 0;
                        emojiView3.setShowing(false);
                        AnimatorSet animatorSet = new AnimatorSet();
                        this.panelAnimation = animatorSet;
                        animatorSet.playTogether(ObjectAnimator.ofFloat(this.emojiView, View.TRANSLATION_Y, emojiView.getMeasuredHeight()));
                        this.panelAnimation.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                        this.panelAnimation.setDuration(250L);
                        this.panelAnimation.addListener(new AnonymousClass61(i));
                        this.notificationsIndex = NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(this.notificationsIndex, null);
                        AndroidUtilities.runOnUIThread(this.runEmojiPanelAnimation, 50L);
                        requestLayout();
                    } else {
                        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
                        if (chatActivityEnterViewDelegate != null) {
                            chatActivityEnterViewDelegate.bottomPanelTranslationYChanged(0.0f);
                        }
                        this.emojiPadding = 0;
                        this.sizeNotifierLayout.removeView(this.emojiView);
                        this.emojiView.setVisibility(8);
                        this.emojiView.setShowing(false);
                    }
                } else {
                    this.removeEmojiViewAfterAnimation = false;
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = this.delegate;
                    if (chatActivityEnterViewDelegate2 != null) {
                        chatActivityEnterViewDelegate2.bottomPanelTranslationYChanged(0.0f);
                    }
                    this.sizeNotifierLayout.removeView(this.emojiView);
                    this.emojiView = null;
                }
                this.emojiViewVisible = false;
            }
            BotKeyboardView botKeyboardView4 = this.botKeyboardView;
            if (botKeyboardView4 != null && botKeyboardView4.getVisibility() == 0) {
                if (i != 2 || AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow) {
                    if (this.smoothKeyboard && !this.keyboardVisible) {
                        if (this.botKeyboardViewVisible) {
                            this.animatingContentType = 1;
                        }
                        AnimatorSet animatorSet2 = new AnimatorSet();
                        this.panelAnimation = animatorSet2;
                        animatorSet2.playTogether(ObjectAnimator.ofFloat(this.botKeyboardView, View.TRANSLATION_Y, botKeyboardView.getMeasuredHeight()));
                        this.panelAnimation.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                        this.panelAnimation.setDuration(250L);
                        this.panelAnimation.addListener(new AnonymousClass62(i));
                        this.notificationsIndex = NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(this.notificationsIndex, null);
                        AndroidUtilities.runOnUIThread(this.runEmojiPanelAnimation, 50L);
                        requestLayout();
                    } else if (!this.waitingForKeyboardOpen) {
                        this.botKeyboardView.setVisibility(8);
                    }
                }
                this.botKeyboardViewVisible = false;
            }
            SizeNotifierFrameLayout sizeNotifierFrameLayout3 = this.sizeNotifierLayout;
            if (sizeNotifierFrameLayout3 != null && !SharedConfig.smoothKeyboard && i == 0) {
                this.emojiPadding = 0;
                sizeNotifierFrameLayout3.requestLayout();
                onWindowSizeChanged();
            }
            updateBotButton(true);
        }
        if (this.stickersTabOpen || this.emojiTabOpen) {
            checkSendButton(true);
        }
        if (this.stickersExpanded && i != 1) {
            setStickersExpanded(false, false, false);
        }
        updateFieldHint(false);
        checkBotMenu();
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$60 */
    /* loaded from: classes3.dex */
    public class AnonymousClass60 extends AnimatorListenerAdapter {
        AnonymousClass60() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ChatActivityEnterView.this.panelAnimation = null;
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.bottomPanelTranslationYChanged(0.0f);
            }
            NotificationCenter.getInstance(ChatActivityEnterView.this.currentAccount).onAnimationFinish(ChatActivityEnterView.this.notificationsIndex);
            ChatActivityEnterView.this.requestLayout();
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$61 */
    /* loaded from: classes3.dex */
    public class AnonymousClass61 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$show;

        AnonymousClass61(int i) {
            ChatActivityEnterView.this = r1;
            this.val$show = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.val$show == 0) {
                ChatActivityEnterView.this.emojiPadding = 0;
            }
            ChatActivityEnterView.this.panelAnimation = null;
            if (ChatActivityEnterView.this.emojiView != null) {
                ChatActivityEnterView.this.emojiView.setTranslationY(0.0f);
                ChatActivityEnterView.this.emojiView.setVisibility(8);
                ChatActivityEnterView.this.sizeNotifierLayout.removeView(ChatActivityEnterView.this.emojiView);
                if (ChatActivityEnterView.this.removeEmojiViewAfterAnimation) {
                    ChatActivityEnterView.this.removeEmojiViewAfterAnimation = false;
                    ChatActivityEnterView.this.emojiView = null;
                }
            }
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.bottomPanelTranslationYChanged(0.0f);
            }
            NotificationCenter.getInstance(ChatActivityEnterView.this.currentAccount).onAnimationFinish(ChatActivityEnterView.this.notificationsIndex);
            ChatActivityEnterView.this.requestLayout();
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$62 */
    /* loaded from: classes3.dex */
    public class AnonymousClass62 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$show;

        AnonymousClass62(int i) {
            ChatActivityEnterView.this = r1;
            this.val$show = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.val$show == 0) {
                ChatActivityEnterView.this.emojiPadding = 0;
            }
            ChatActivityEnterView.this.panelAnimation = null;
            ChatActivityEnterView.this.botKeyboardView.setTranslationY(0.0f);
            ChatActivityEnterView.this.botKeyboardView.setVisibility(8);
            NotificationCenter.getInstance(ChatActivityEnterView.this.currentAccount).onAnimationFinish(ChatActivityEnterView.this.notificationsIndex);
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.bottomPanelTranslationYChanged(0.0f);
            }
            ChatActivityEnterView.this.requestLayout();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v1 */
    /* JADX WARN: Type inference failed for: r12v15 */
    /* JADX WARN: Type inference failed for: r12v2 */
    /* JADX WARN: Type inference failed for: r12v28 */
    /* JADX WARN: Type inference failed for: r12v32 */
    private void setEmojiButtonImage(boolean z, boolean z2) {
        int i;
        int i2;
        FrameLayout frameLayout;
        ?? r12 = z2;
        if (this.recordInterfaceState == 1 || ((frameLayout = this.recordedAudioPanel) != null && frameLayout.getVisibility() == 0)) {
            this.emojiButton[0].setScaleX(0.0f);
            this.emojiButton[0].setScaleY(0.0f);
            this.emojiButton[0].setAlpha(0.0f);
            this.emojiButton[1].setScaleX(0.0f);
            this.emojiButton[1].setScaleY(0.0f);
            this.emojiButton[1].setAlpha(0.0f);
            r12 = 0;
        }
        if (r12 != 0 && this.currentEmojiIcon == -1) {
            r12 = 0;
        }
        if (!z || this.currentPopupContentType != 0) {
            EmojiView emojiView = this.emojiView;
            if (emojiView == null) {
                i2 = MessagesController.getGlobalEmojiSettings().getInt("selected_page", 0);
            } else {
                i2 = emojiView.getCurrentPage();
            }
            i = (i2 == 0 || (!this.allowStickers && !this.allowGifs)) ? 1 : i2 == 1 ? 2 : 3;
        } else {
            i = 0;
        }
        if (this.currentEmojiIcon == i) {
            return;
        }
        AnimatorSet animatorSet = this.emojiButtonAnimation;
        Integer num = null;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.emojiButtonAnimation = null;
        }
        if (i == 0) {
            this.emojiButton[r12].setImageResource(2131165536);
        } else if (i == 1) {
            this.emojiButton[r12].setImageResource(2131165543);
        } else if (i == 2) {
            this.emojiButton[r12].setImageResource(2131165544);
        } else if (i == 3) {
            this.emojiButton[r12].setImageResource(2131165535);
        }
        ImageView[] imageViewArr = this.emojiButton;
        char c = r12 == true ? 1 : 0;
        char c2 = r12 == true ? 1 : 0;
        char c3 = r12 == true ? 1 : 0;
        ImageView imageView = imageViewArr[c];
        if (i == 2) {
            num = 1;
        }
        imageView.setTag(num);
        this.currentEmojiIcon = i;
        if (r12 != 0) {
            this.emojiButton[1].setVisibility(0);
            this.emojiButton[1].setAlpha(0.0f);
            this.emojiButton[1].setScaleX(0.1f);
            this.emojiButton[1].setScaleY(0.1f);
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.emojiButtonAnimation = animatorSet2;
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.emojiButton[0], View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.emojiButton[1], View.ALPHA, 1.0f));
            this.emojiButtonAnimation.addListener(new AnonymousClass63());
            this.emojiButtonAnimation.setDuration(150L);
            this.emojiButtonAnimation.start();
        }
        onEmojiIconChanged(i);
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$63 */
    /* loaded from: classes3.dex */
    public class AnonymousClass63 extends AnimatorListenerAdapter {
        AnonymousClass63() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(ChatActivityEnterView.this.emojiButtonAnimation)) {
                ChatActivityEnterView.this.emojiButtonAnimation = null;
                ImageView imageView = ChatActivityEnterView.this.emojiButton[1];
                ChatActivityEnterView.this.emojiButton[1] = ChatActivityEnterView.this.emojiButton[0];
                ChatActivityEnterView.this.emojiButton[0] = imageView;
                ChatActivityEnterView.this.emojiButton[1].setVisibility(4);
                ChatActivityEnterView.this.emojiButton[1].setAlpha(0.0f);
                ChatActivityEnterView.this.emojiButton[1].setScaleX(0.1f);
                ChatActivityEnterView.this.emojiButton[1].setScaleY(0.1f);
            }
        }
    }

    protected void onEmojiIconChanged(int i) {
        if (i == 3 && this.emojiView == null) {
            MediaDataController.getInstance(this.currentAccount).loadRecents(0, true, true, false);
            ArrayList<String> arrayList = MessagesController.getInstance(this.currentAccount).gifSearchEmojies;
            int min = Math.min(10, arrayList.size());
            for (int i2 = 0; i2 < min; i2++) {
                Emoji.preloadEmoji(arrayList.get(i2));
            }
        }
    }

    public boolean hidePopup(boolean z) {
        return hidePopup(z, false);
    }

    public boolean hidePopup(boolean z, boolean z2) {
        if (isPopupShowing()) {
            if (this.currentPopupContentType == 1 && z && this.botButtonsMessageObject != null) {
                return false;
            }
            if ((z && this.searchingType != 0) || z2) {
                setSearchingTypeInternal(0, true);
                EmojiView emojiView = this.emojiView;
                if (emojiView != null) {
                    emojiView.closeSearch(true);
                }
                this.messageEditText.requestFocus();
                setStickersExpanded(false, true, false);
                if (this.emojiTabOpen) {
                    checkSendButton(true);
                }
            } else {
                if (this.searchingType != 0) {
                    setSearchingTypeInternal(0, false);
                    this.emojiView.closeSearch(false);
                    this.messageEditText.requestFocus();
                }
                showPopup(0, 0);
            }
            return true;
        }
        return false;
    }

    public void setSearchingTypeInternal(int i, boolean z) {
        boolean z2 = i != 0;
        if (z2 != (this.searchingType != 0)) {
            ValueAnimator valueAnimator = this.searchAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.searchAnimator.cancel();
            }
            float f = 1.0f;
            if (!z) {
                if (!z2) {
                    f = 0.0f;
                }
                this.searchToOpenProgress = f;
                EmojiView emojiView = this.emojiView;
                if (emojiView != null) {
                    emojiView.searchProgressChanged();
                }
            } else {
                float[] fArr = new float[2];
                fArr[0] = this.searchToOpenProgress;
                if (!z2) {
                    f = 0.0f;
                }
                fArr[1] = f;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
                this.searchAnimator = ofFloat;
                ofFloat.addUpdateListener(new ChatActivityEnterView$$ExternalSyntheticLambda3(this));
                this.searchAnimator.addListener(new AnonymousClass64(z2));
                this.searchAnimator.setDuration(220L);
                this.searchAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                this.searchAnimator.start();
            }
        }
        this.searchingType = i;
    }

    public /* synthetic */ void lambda$setSearchingTypeInternal$51(ValueAnimator valueAnimator) {
        this.searchToOpenProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.searchProgressChanged();
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$64 */
    /* loaded from: classes3.dex */
    public class AnonymousClass64 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$showSearchingNew;

        AnonymousClass64(boolean z) {
            ChatActivityEnterView.this = r1;
            this.val$showSearchingNew = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ChatActivityEnterView.this.searchToOpenProgress = this.val$showSearchingNew ? 1.0f : 0.0f;
            if (ChatActivityEnterView.this.emojiView != null) {
                ChatActivityEnterView.this.emojiView.searchProgressChanged();
            }
        }
    }

    public void openKeyboardInternal() {
        ChatActivity chatActivity;
        if (!hasBotWebView() || !botCommandsMenuIsShowing()) {
            showPopup((AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow || ((chatActivity = this.parentFragment) != null && chatActivity.isInBubbleMode()) || this.isPaused) ? 0 : 2, 0);
            this.messageEditText.requestFocus();
            AndroidUtilities.showKeyboard(this.messageEditText);
            if (this.isPaused) {
                this.showKeyboardOnResume = true;
            } else if (AndroidUtilities.usingHardwareInput || this.keyboardVisible || AndroidUtilities.isInMultiwindow) {
            } else {
                ChatActivity chatActivity2 = this.parentFragment;
                if (chatActivity2 != null && chatActivity2.isInBubbleMode()) {
                    return;
                }
                this.waitingForKeyboardOpen = true;
                EmojiView emojiView = this.emojiView;
                if (emojiView != null) {
                    emojiView.onTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 3, 0.0f, 0.0f, 0));
                }
                AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
                AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100L);
            }
        }
    }

    public boolean isEditingMessage() {
        return this.editingMessageObject != null;
    }

    public MessageObject getEditingMessageObject() {
        return this.editingMessageObject;
    }

    public boolean isEditingCaption() {
        return this.editingCaption;
    }

    public boolean hasAudioToSend() {
        return (this.audioToSendMessageObject == null && this.videoToSendMessageObject == null) ? false : true;
    }

    public void openKeyboard() {
        if ((!hasBotWebView() || !botCommandsMenuIsShowing()) && !AndroidUtilities.showKeyboard(this.messageEditText)) {
            this.messageEditText.clearFocus();
            this.messageEditText.requestFocus();
        }
    }

    public void closeKeyboard() {
        AndroidUtilities.hideKeyboard(this.messageEditText);
    }

    public boolean isPopupShowing() {
        return this.emojiViewVisible || this.botKeyboardViewVisible;
    }

    public boolean isKeyboardVisible() {
        return this.keyboardVisible;
    }

    public void addRecentGif(TLRPC$Document tLRPC$Document) {
        MediaDataController.getInstance(this.currentAccount).addRecentGif(tLRPC$Document, (int) (System.currentTimeMillis() / 1000), true);
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.addRecentGif(tLRPC$Document);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i != i3 && this.stickersExpanded) {
            setSearchingTypeInternal(0, false);
            this.emojiView.closeSearch(false);
            setStickersExpanded(false, false, false);
        }
        this.videoTimelineView.clearFrames();
    }

    public boolean isStickersExpanded() {
        return this.stickersExpanded;
    }

    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate
    public void onSizeChanged(int i, boolean z) {
        boolean z2;
        MessageObject messageObject;
        View view;
        boolean z3 = true;
        if (this.searchingType != 0) {
            this.lastSizeChangeValue1 = i;
            this.lastSizeChangeValue2 = z;
            if (i <= 0) {
                z3 = false;
            }
            this.keyboardVisible = z3;
            checkBotMenu();
            return;
        }
        if (i > AndroidUtilities.dp(50.0f) && this.keyboardVisible && !AndroidUtilities.isInMultiwindow) {
            if (z) {
                this.keyboardHeightLand = i;
                MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height_land3", this.keyboardHeightLand).commit();
            } else {
                this.keyboardHeight = i;
                MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height", this.keyboardHeight).commit();
            }
        }
        if (this.keyboardVisible && this.emojiViewVisible && this.emojiView == null) {
            this.emojiViewVisible = false;
        }
        if (isPopupShowing()) {
            int i2 = z ? this.keyboardHeightLand : this.keyboardHeight;
            if (this.currentPopupContentType == 1 && !this.botKeyboardView.isFullSize()) {
                i2 = Math.min(this.botKeyboardView.getKeyboardHeight(), i2);
            }
            int i3 = this.currentPopupContentType;
            if (i3 == 0) {
                view = this.emojiView;
            } else {
                view = i3 == 1 ? this.botKeyboardView : null;
            }
            BotKeyboardView botKeyboardView = this.botKeyboardView;
            if (botKeyboardView != null) {
                botKeyboardView.setPanelHeight(i2);
            }
            if (view != null) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                if (!this.closeAnimationInProgress) {
                    int i4 = layoutParams.width;
                    int i5 = AndroidUtilities.displaySize.x;
                    if ((i4 != i5 || layoutParams.height != i2) && !this.stickersExpanded) {
                        layoutParams.width = i5;
                        layoutParams.height = i2;
                        view.setLayoutParams(layoutParams);
                        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierLayout;
                        if (sizeNotifierFrameLayout != null) {
                            int i6 = this.emojiPadding;
                            this.emojiPadding = layoutParams.height;
                            sizeNotifierFrameLayout.requestLayout();
                            onWindowSizeChanged();
                            if (this.smoothKeyboard && !this.keyboardVisible && i6 != this.emojiPadding && pannelAnimationEnabled()) {
                                AnimatorSet animatorSet = new AnimatorSet();
                                this.panelAnimation = animatorSet;
                                animatorSet.playTogether(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, this.emojiPadding - i6, 0.0f));
                                this.panelAnimation.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                                this.panelAnimation.setDuration(250L);
                                this.panelAnimation.addListener(new AnonymousClass65());
                                AndroidUtilities.runOnUIThread(this.runEmojiPanelAnimation, 50L);
                                this.notificationsIndex = NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(this.notificationsIndex, null);
                                requestLayout();
                            }
                        }
                    }
                }
            }
        }
        if (this.lastSizeChangeValue1 == i && this.lastSizeChangeValue2 == z) {
            onWindowSizeChanged();
            return;
        }
        this.lastSizeChangeValue1 = i;
        this.lastSizeChangeValue2 = z;
        boolean z4 = this.keyboardVisible;
        this.keyboardVisible = i > 0;
        checkBotMenu();
        if (this.keyboardVisible && isPopupShowing() && this.stickersExpansionAnim == null) {
            showPopup(0, this.currentPopupContentType);
        } else if (!this.keyboardVisible && !isPopupShowing() && (messageObject = this.botButtonsMessageObject) != null && this.replyingMessageObject != messageObject && ((!hasBotWebView() || !botCommandsMenuIsShowing()) && TextUtils.isEmpty(this.messageEditText.getText()))) {
            if (this.sizeNotifierLayout.adjustPanLayoutHelper.animationInProgress()) {
                this.sizeNotifierLayout.adjustPanLayoutHelper.stopTransition();
            } else {
                this.sizeNotifierLayout.adjustPanLayoutHelper.ignoreOnce();
            }
            showPopup(1, 1, false);
        }
        if (this.emojiPadding != 0 && !(z2 = this.keyboardVisible) && z2 != z4 && !isPopupShowing()) {
            this.emojiPadding = 0;
            this.sizeNotifierLayout.requestLayout();
        }
        if (this.keyboardVisible && this.waitingForKeyboardOpen) {
            this.waitingForKeyboardOpen = false;
            if (this.clearBotButtonsOnKeyboardOpen) {
                this.clearBotButtonsOnKeyboardOpen = false;
                this.botKeyboardView.setButtons(this.botReplyMarkup);
            }
            AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
        }
        onWindowSizeChanged();
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$65 */
    /* loaded from: classes3.dex */
    class AnonymousClass65 extends AnimatorListenerAdapter {
        AnonymousClass65() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ChatActivityEnterView.this.panelAnimation = null;
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.bottomPanelTranslationYChanged(0.0f);
            }
            ChatActivityEnterView.this.requestLayout();
            NotificationCenter.getInstance(ChatActivityEnterView.this.currentAccount).onAnimationFinish(ChatActivityEnterView.this.notificationsIndex);
        }
    }

    public int getEmojiPadding() {
        return this.emojiPadding;
    }

    public int getVisibleEmojiPadding() {
        if (this.emojiViewVisible) {
            return this.emojiPadding;
        }
        return 0;
    }

    public MessageObject getThreadMessage() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null) {
            return chatActivity.getThreadMessage();
        }
        return null;
    }

    public int getThreadMessageId() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || chatActivity.getThreadMessage() == null) {
            return 0;
        }
        return this.parentFragment.getThreadMessage().getId();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        TLRPC$ChatFull tLRPC$ChatFull;
        TLRPC$Chat chat;
        int i3;
        if (i == NotificationCenter.emojiLoaded) {
            EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                emojiView.invalidateViews();
            }
            BotKeyboardView botKeyboardView = this.botKeyboardView;
            if (botKeyboardView != null) {
                botKeyboardView.invalidateViews();
            }
            EditTextCaption editTextCaption = this.messageEditText;
            if (editTextCaption == null) {
                return;
            }
            editTextCaption.postInvalidate();
            return;
        }
        int i4 = 0;
        if (i == NotificationCenter.recordProgressChanged) {
            if (((Integer) objArr[0]).intValue() != this.recordingGuid) {
                return;
            }
            if (this.recordInterfaceState != 0 && !this.wasSendTyping && !isInScheduleMode()) {
                this.wasSendTyping = true;
                MessagesController messagesController = this.accountInstance.getMessagesController();
                long j = this.dialog_id;
                int threadMessageId = getThreadMessageId();
                ImageView imageView = this.videoSendButton;
                messagesController.sendTyping(j, threadMessageId, (imageView == null || imageView.getTag() == null) ? 1 : 7, 0);
            }
            RecordCircle recordCircle = this.recordCircle;
            if (recordCircle == null) {
                return;
            }
            recordCircle.setAmplitude(((Double) objArr[1]).doubleValue());
        } else if (i == NotificationCenter.closeChats) {
            EditTextCaption editTextCaption2 = this.messageEditText;
            if (editTextCaption2 == null || !editTextCaption2.isFocused()) {
                return;
            }
            AndroidUtilities.hideKeyboard(this.messageEditText);
        } else {
            int i5 = 4;
            if (i == NotificationCenter.recordStartError || i == NotificationCenter.recordStopped) {
                if (((Integer) objArr[0]).intValue() != this.recordingGuid) {
                    return;
                }
                if (this.recordingAudioVideo) {
                    this.recordingAudioVideo = false;
                    if (i == NotificationCenter.recordStopped) {
                        Integer num = (Integer) objArr[1];
                        if (num.intValue() != 4) {
                            if (isInVideoMode() && num.intValue() == 5) {
                                i5 = 1;
                            } else if (num.intValue() == 0) {
                                i5 = 5;
                            } else {
                                i5 = num.intValue() == 6 ? 2 : 3;
                            }
                        }
                        if (i5 != 3) {
                            updateRecordInterface(i5);
                        }
                    } else {
                        updateRecordInterface(2);
                    }
                }
                if (i != NotificationCenter.recordStopped) {
                    return;
                }
                Integer num2 = (Integer) objArr[1];
                return;
            }
            Integer num3 = null;
            if (i == NotificationCenter.recordStarted) {
                if (((Integer) objArr[0]).intValue() != this.recordingGuid) {
                    return;
                }
                boolean booleanValue = ((Boolean) objArr[1]).booleanValue();
                ImageView imageView2 = this.videoSendButton;
                if (imageView2 != null) {
                    if (!booleanValue) {
                        num3 = 1;
                    }
                    imageView2.setTag(num3);
                    int i6 = 8;
                    this.videoSendButton.setVisibility(booleanValue ? 8 : 0);
                    ImageView imageView3 = this.videoSendButton;
                    if (booleanValue) {
                        i6 = 0;
                    }
                    imageView3.setVisibility(i6);
                }
                if (!this.recordingAudioVideo) {
                    this.recordingAudioVideo = true;
                    updateRecordInterface(0);
                } else {
                    this.recordCircle.showWaves(true, true);
                }
                this.recordTimerView.start();
                this.recordDot.enterAnimation = false;
            } else if (i == NotificationCenter.audioDidSent) {
                if (((Integer) objArr[0]).intValue() != this.recordingGuid) {
                    return;
                }
                Object obj = objArr[1];
                if (obj instanceof VideoEditedInfo) {
                    this.videoToSendMessageObject = (VideoEditedInfo) obj;
                    String str = (String) objArr[2];
                    this.audioToSendPath = str;
                    this.videoTimelineView.setVideoPath(str);
                    this.videoTimelineView.setKeyframes((ArrayList) objArr[3]);
                    this.videoTimelineView.setVisibility(0);
                    this.videoTimelineView.setMinProgressDiff(1000.0f / ((float) this.videoToSendMessageObject.estimatedDuration));
                    updateRecordInterface(3);
                    checkSendButton(false);
                    return;
                }
                TLRPC$TL_document tLRPC$TL_document = (TLRPC$TL_document) objArr[1];
                this.audioToSend = tLRPC$TL_document;
                this.audioToSendPath = (String) objArr[2];
                if (tLRPC$TL_document != null) {
                    if (this.recordedAudioPanel == null) {
                        return;
                    }
                    TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
                    tLRPC$TL_message.out = true;
                    tLRPC$TL_message.id = 0;
                    tLRPC$TL_message.peer_id = new TLRPC$TL_peerUser();
                    TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                    tLRPC$TL_message.from_id = tLRPC$TL_peerUser;
                    TLRPC$Peer tLRPC$Peer = tLRPC$TL_message.peer_id;
                    long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                    tLRPC$TL_peerUser.user_id = clientUserId;
                    tLRPC$Peer.user_id = clientUserId;
                    tLRPC$TL_message.date = (int) (System.currentTimeMillis() / 1000);
                    tLRPC$TL_message.message = "";
                    tLRPC$TL_message.attachPath = this.audioToSendPath;
                    TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument = new TLRPC$TL_messageMediaDocument();
                    tLRPC$TL_message.media = tLRPC$TL_messageMediaDocument;
                    tLRPC$TL_messageMediaDocument.flags |= 3;
                    tLRPC$TL_messageMediaDocument.document = this.audioToSend;
                    tLRPC$TL_message.flags |= 768;
                    this.audioToSendMessageObject = new MessageObject(UserConfig.selectedAccount, tLRPC$TL_message, false, true);
                    this.recordedAudioPanel.setAlpha(1.0f);
                    this.recordedAudioPanel.setVisibility(0);
                    this.recordDeleteImageView.setVisibility(0);
                    this.recordDeleteImageView.setAlpha(0.0f);
                    this.recordDeleteImageView.setScaleY(0.0f);
                    this.recordDeleteImageView.setScaleX(0.0f);
                    int i7 = 0;
                    while (true) {
                        if (i7 >= this.audioToSend.attributes.size()) {
                            i3 = 0;
                            break;
                        }
                        TLRPC$DocumentAttribute tLRPC$DocumentAttribute = this.audioToSend.attributes.get(i7);
                        if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                            i3 = tLRPC$DocumentAttribute.duration;
                            break;
                        }
                        i7++;
                    }
                    int i8 = 0;
                    while (true) {
                        if (i8 >= this.audioToSend.attributes.size()) {
                            break;
                        }
                        TLRPC$DocumentAttribute tLRPC$DocumentAttribute2 = this.audioToSend.attributes.get(i8);
                        if (tLRPC$DocumentAttribute2 instanceof TLRPC$TL_documentAttributeAudio) {
                            byte[] bArr = tLRPC$DocumentAttribute2.waveform;
                            if (bArr == null || bArr.length == 0) {
                                tLRPC$DocumentAttribute2.waveform = MediaController.getInstance().getWaveform(this.audioToSendPath);
                            }
                            this.recordedAudioSeekBar.setWaveform(tLRPC$DocumentAttribute2.waveform);
                        } else {
                            i8++;
                        }
                    }
                    this.recordedAudioTimeTextView.setText(AndroidUtilities.formatShortDuration(i3));
                    checkSendButton(false);
                    updateRecordInterface(3);
                    return;
                }
                ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
                if (chatActivityEnterViewDelegate == null) {
                    return;
                }
                chatActivityEnterViewDelegate.onMessageSend(null, true, 0);
            } else if (i == NotificationCenter.audioRouteChanged) {
                if (this.parentActivity == null) {
                    return;
                }
                boolean booleanValue2 = ((Boolean) objArr[0]).booleanValue();
                Activity activity = this.parentActivity;
                if (!booleanValue2) {
                    i4 = Integer.MIN_VALUE;
                }
                activity.setVolumeControlStream(i4);
            } else if (i == NotificationCenter.messagePlayingDidReset) {
                if (this.audioToSendMessageObject == null || MediaController.getInstance().isPlayingMessage(this.audioToSendMessageObject)) {
                    return;
                }
                this.playPauseDrawable.setIcon(0, true);
                this.recordedAudioPlayButton.setContentDescription(LocaleController.getString("AccActionPlay", 2131623955));
                this.recordedAudioSeekBar.setProgress(0.0f);
            } else if (i == NotificationCenter.messagePlayingProgressDidChanged) {
                Integer num4 = (Integer) objArr[0];
                if (this.audioToSendMessageObject == null || !MediaController.getInstance().isPlayingMessage(this.audioToSendMessageObject)) {
                    return;
                }
                MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                MessageObject messageObject = this.audioToSendMessageObject;
                messageObject.audioProgress = playingMessageObject.audioProgress;
                messageObject.audioProgressSec = playingMessageObject.audioProgressSec;
                if (this.recordedAudioSeekBar.isDragging()) {
                    return;
                }
                this.recordedAudioSeekBar.setProgress(this.audioToSendMessageObject.audioProgress);
            } else if (i == NotificationCenter.featuredStickersDidLoad) {
                if (this.emojiButton == null) {
                    return;
                }
                while (true) {
                    ImageView[] imageViewArr = this.emojiButton;
                    if (i4 >= imageViewArr.length) {
                        return;
                    }
                    imageViewArr[i4].invalidate();
                    i4++;
                }
            } else if (i == NotificationCenter.messageReceivedByServer) {
                if (((Boolean) objArr[6]).booleanValue() || ((Long) objArr[3]).longValue() != this.dialog_id || (tLRPC$ChatFull = this.info) == null || tLRPC$ChatFull.slowmode_seconds == 0 || (chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(this.info.id))) == null || ChatObject.hasAdminRights(chat)) {
                    return;
                }
                TLRPC$ChatFull tLRPC$ChatFull2 = this.info;
                int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                TLRPC$ChatFull tLRPC$ChatFull3 = this.info;
                tLRPC$ChatFull2.slowmode_next_send_date = currentTime + tLRPC$ChatFull3.slowmode_seconds;
                tLRPC$ChatFull3.flags |= 262144;
                setSlowModeTimer(tLRPC$ChatFull3.slowmode_next_send_date);
            } else if (i == NotificationCenter.sendingMessagesChanged) {
                if (this.info == null) {
                    return;
                }
                updateSlowModeText();
            } else if (i == NotificationCenter.audioRecordTooShort) {
                updateRecordInterface(4);
            } else if (i != NotificationCenter.updateBotMenuButton) {
            } else {
                long longValue = ((Long) objArr[0]).longValue();
                TLRPC$BotMenuButton tLRPC$BotMenuButton = (TLRPC$BotMenuButton) objArr[1];
                if (longValue != this.dialog_id) {
                    return;
                }
                if (tLRPC$BotMenuButton instanceof TLRPC$TL_botMenuButton) {
                    TLRPC$TL_botMenuButton tLRPC$TL_botMenuButton = (TLRPC$TL_botMenuButton) tLRPC$BotMenuButton;
                    this.botMenuWebViewTitle = tLRPC$TL_botMenuButton.text;
                    this.botMenuWebViewUrl = tLRPC$TL_botMenuButton.url;
                    this.botMenuButtonType = BotMenuButtonType.WEB_VIEW;
                } else if (this.hasBotCommands) {
                    this.botMenuButtonType = BotMenuButtonType.COMMANDS;
                } else {
                    this.botMenuButtonType = BotMenuButtonType.NO_BUTTON;
                }
                updateBotButton(false);
            }
        }
    }

    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        if (i != 2 || this.pendingLocationButton == null) {
            return;
        }
        if (iArr.length > 0 && iArr[0] == 0) {
            SendMessagesHelper.getInstance(this.currentAccount).sendCurrentLocation(this.pendingMessageObject, this.pendingLocationButton);
        }
        this.pendingLocationButton = null;
        this.pendingMessageObject = null;
    }

    public void checkStickresExpandHeight() {
        if (this.emojiView == null) {
            return;
        }
        android.graphics.Point point = AndroidUtilities.displaySize;
        int i = point.x > point.y ? this.keyboardHeightLand : this.keyboardHeight;
        int currentActionBarHeight = (((this.originalViewHeight - (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)) - ActionBar.getCurrentActionBarHeight()) - getHeight()) + Theme.chat_composeShadowDrawable.getIntrinsicHeight();
        if (this.searchingType == 2) {
            currentActionBarHeight = Math.min(currentActionBarHeight, AndroidUtilities.dp(120.0f) + i);
        }
        int i2 = this.emojiView.getLayoutParams().height;
        if (i2 == currentActionBarHeight) {
            return;
        }
        Animator animator = this.stickersExpansionAnim;
        if (animator != null) {
            animator.cancel();
            this.stickersExpansionAnim = null;
        }
        this.stickersExpandedHeight = currentActionBarHeight;
        if (i2 > currentActionBarHeight) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofInt(this, (Property<ChatActivityEnterView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i)), ObjectAnimator.ofInt(this.emojiView, (Property<EmojiView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i)));
            ((ObjectAnimator) animatorSet.getChildAnimations().get(0)).addUpdateListener(new ChatActivityEnterView$$ExternalSyntheticLambda2(this));
            animatorSet.setDuration(300L);
            animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
            animatorSet.addListener(new AnonymousClass66());
            this.stickersExpansionAnim = animatorSet;
            this.emojiView.setLayerType(2, null);
            animatorSet.start();
            return;
        }
        this.emojiView.getLayoutParams().height = this.stickersExpandedHeight;
        this.sizeNotifierLayout.requestLayout();
        int selectionStart = this.messageEditText.getSelectionStart();
        int selectionEnd = this.messageEditText.getSelectionEnd();
        EditTextCaption editTextCaption = this.messageEditText;
        editTextCaption.setText(editTextCaption.getText());
        this.messageEditText.setSelection(selectionStart, selectionEnd);
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(ObjectAnimator.ofInt(this, (Property<ChatActivityEnterView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i)), ObjectAnimator.ofInt(this.emojiView, (Property<EmojiView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i)));
        ((ObjectAnimator) animatorSet2.getChildAnimations().get(0)).addUpdateListener(new ChatActivityEnterView$$ExternalSyntheticLambda0(this));
        animatorSet2.setDuration(300L);
        animatorSet2.setInterpolator(CubicBezierInterpolator.DEFAULT);
        animatorSet2.addListener(new AnonymousClass67());
        this.stickersExpansionAnim = animatorSet2;
        this.emojiView.setLayerType(2, null);
        animatorSet2.start();
    }

    public /* synthetic */ void lambda$checkStickresExpandHeight$52(ValueAnimator valueAnimator) {
        this.sizeNotifierLayout.invalidate();
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$66 */
    /* loaded from: classes3.dex */
    public class AnonymousClass66 extends AnimatorListenerAdapter {
        AnonymousClass66() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ChatActivityEnterView.this.stickersExpansionAnim = null;
            if (ChatActivityEnterView.this.emojiView != null) {
                ChatActivityEnterView.this.emojiView.getLayoutParams().height = ChatActivityEnterView.this.stickersExpandedHeight;
                ChatActivityEnterView.this.emojiView.setLayerType(0, null);
            }
        }
    }

    public /* synthetic */ void lambda$checkStickresExpandHeight$53(ValueAnimator valueAnimator) {
        this.sizeNotifierLayout.invalidate();
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$67 */
    /* loaded from: classes3.dex */
    public class AnonymousClass67 extends AnimatorListenerAdapter {
        AnonymousClass67() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ChatActivityEnterView.this.stickersExpansionAnim = null;
            ChatActivityEnterView.this.emojiView.setLayerType(0, null);
        }
    }

    public void setStickersExpanded(boolean z, boolean z2, boolean z3) {
        AdjustPanLayoutHelper adjustPanLayoutHelper = this.adjustPanLayoutHelper;
        if ((adjustPanLayoutHelper == null || !adjustPanLayoutHelper.animationInProgress()) && !this.waitingForKeyboardOpenAfterAnimation && this.emojiView != null) {
            if (!z3 && this.stickersExpanded == z) {
                return;
            }
            this.stickersExpanded = z;
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
            if (chatActivityEnterViewDelegate != null) {
                chatActivityEnterViewDelegate.onStickersExpandedChange();
            }
            android.graphics.Point point = AndroidUtilities.displaySize;
            int i = point.x > point.y ? this.keyboardHeightLand : this.keyboardHeight;
            Animator animator = this.stickersExpansionAnim;
            if (animator != null) {
                animator.cancel();
                this.stickersExpansionAnim = null;
            }
            if (this.stickersExpanded) {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.stopAllHeavyOperations, 1);
                int height = this.sizeNotifierLayout.getHeight();
                this.originalViewHeight = height;
                int currentActionBarHeight = (((height - (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)) - ActionBar.getCurrentActionBarHeight()) - getHeight()) + Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                this.stickersExpandedHeight = currentActionBarHeight;
                if (this.searchingType == 2) {
                    this.stickersExpandedHeight = Math.min(currentActionBarHeight, AndroidUtilities.dp(120.0f) + i);
                }
                this.emojiView.getLayoutParams().height = this.stickersExpandedHeight;
                this.sizeNotifierLayout.requestLayout();
                this.sizeNotifierLayout.setForeground(new ScrimDrawable());
                int selectionStart = this.messageEditText.getSelectionStart();
                int selectionEnd = this.messageEditText.getSelectionEnd();
                EditTextCaption editTextCaption = this.messageEditText;
                editTextCaption.setText(editTextCaption.getText());
                this.messageEditText.setSelection(selectionStart, selectionEnd);
                if (z2) {
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(ObjectAnimator.ofInt(this, (Property<ChatActivityEnterView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i)), ObjectAnimator.ofInt(this.emojiView, (Property<EmojiView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i)), ObjectAnimator.ofFloat(this.stickersArrow, "animationProgress", 1.0f));
                    animatorSet.setDuration(300L);
                    animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                    ((ObjectAnimator) animatorSet.getChildAnimations().get(0)).addUpdateListener(new ChatActivityEnterView$$ExternalSyntheticLambda6(this, i));
                    animatorSet.addListener(new AnonymousClass68());
                    this.stickersExpansionAnim = animatorSet;
                    this.emojiView.setLayerType(2, null);
                    this.notificationsIndex = NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(this.notificationsIndex, null);
                    this.stickersExpansionProgress = 0.0f;
                    this.sizeNotifierLayout.invalidate();
                    animatorSet.start();
                } else {
                    this.stickersExpansionProgress = 1.0f;
                    setTranslationY(-(this.stickersExpandedHeight - i));
                    this.emojiView.setTranslationY(-(this.stickersExpandedHeight - i));
                    this.stickersArrow.setAnimationProgress(1.0f);
                }
            } else {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.startAllHeavyOperations, 1);
                if (z2) {
                    this.closeAnimationInProgress = true;
                    AnimatorSet animatorSet2 = new AnimatorSet();
                    animatorSet2.playTogether(ObjectAnimator.ofInt(this, (Property<ChatActivityEnterView, Integer>) this.roundedTranslationYProperty, 0), ObjectAnimator.ofInt(this.emojiView, (Property<EmojiView, Integer>) this.roundedTranslationYProperty, 0), ObjectAnimator.ofFloat(this.stickersArrow, "animationProgress", 0.0f));
                    animatorSet2.setDuration(300L);
                    animatorSet2.setInterpolator(CubicBezierInterpolator.DEFAULT);
                    ((ObjectAnimator) animatorSet2.getChildAnimations().get(0)).addUpdateListener(new ChatActivityEnterView$$ExternalSyntheticLambda7(this, i));
                    animatorSet2.addListener(new AnonymousClass69(i));
                    this.stickersExpansionProgress = 1.0f;
                    this.sizeNotifierLayout.invalidate();
                    this.stickersExpansionAnim = animatorSet2;
                    this.emojiView.setLayerType(2, null);
                    this.notificationsIndex = NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(this.notificationsIndex, null);
                    animatorSet2.start();
                } else {
                    this.stickersExpansionProgress = 0.0f;
                    setTranslationY(0.0f);
                    this.emojiView.setTranslationY(0.0f);
                    this.emojiView.getLayoutParams().height = i;
                    this.sizeNotifierLayout.requestLayout();
                    this.sizeNotifierLayout.setForeground(null);
                    this.sizeNotifierLayout.setWillNotDraw(false);
                    this.stickersArrow.setAnimationProgress(0.0f);
                }
            }
            if (z) {
                this.expandStickersButton.setContentDescription(LocaleController.getString("AccDescrCollapsePanel", 2131623978));
            } else {
                this.expandStickersButton.setContentDescription(LocaleController.getString("AccDescrExpandPanel", 2131623983));
            }
        }
    }

    public /* synthetic */ void lambda$setStickersExpanded$54(int i, ValueAnimator valueAnimator) {
        this.stickersExpansionProgress = Math.abs(getTranslationY() / (-(this.stickersExpandedHeight - i)));
        this.sizeNotifierLayout.invalidate();
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$68 */
    /* loaded from: classes3.dex */
    public class AnonymousClass68 extends AnimatorListenerAdapter {
        AnonymousClass68() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ChatActivityEnterView.this.stickersExpansionAnim = null;
            ChatActivityEnterView.this.emojiView.setLayerType(0, null);
            NotificationCenter.getInstance(ChatActivityEnterView.this.currentAccount).onAnimationFinish(ChatActivityEnterView.this.notificationsIndex);
        }
    }

    public /* synthetic */ void lambda$setStickersExpanded$55(int i, ValueAnimator valueAnimator) {
        this.stickersExpansionProgress = getTranslationY() / (-(this.stickersExpandedHeight - i));
        this.sizeNotifierLayout.invalidate();
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterView$69 */
    /* loaded from: classes3.dex */
    public class AnonymousClass69 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$origHeight;

        AnonymousClass69(int i) {
            ChatActivityEnterView.this = r1;
            this.val$origHeight = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ChatActivityEnterView.this.closeAnimationInProgress = false;
            ChatActivityEnterView.this.stickersExpansionAnim = null;
            if (ChatActivityEnterView.this.emojiView != null) {
                ChatActivityEnterView.this.emojiView.getLayoutParams().height = this.val$origHeight;
                ChatActivityEnterView.this.emojiView.setLayerType(0, null);
            }
            if (ChatActivityEnterView.this.sizeNotifierLayout != null) {
                ChatActivityEnterView.this.sizeNotifierLayout.requestLayout();
                ChatActivityEnterView.this.sizeNotifierLayout.setForeground(null);
                ChatActivityEnterView.this.sizeNotifierLayout.setWillNotDraw(false);
            }
            if (ChatActivityEnterView.this.keyboardVisible && ChatActivityEnterView.this.isPopupShowing()) {
                ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                chatActivityEnterView.showPopup(0, chatActivityEnterView.currentPopupContentType);
            }
            if (ChatActivityEnterView.this.onEmojiSearchClosed != null) {
                ChatActivityEnterView.this.onEmojiSearchClosed.run();
                ChatActivityEnterView.this.onEmojiSearchClosed = null;
            }
            NotificationCenter.getInstance(ChatActivityEnterView.this.currentAccount).onAnimationFinish(ChatActivityEnterView.this.notificationsIndex);
        }
    }

    public boolean swipeToBackEnabled() {
        FrameLayout frameLayout;
        if (this.recordingAudioVideo) {
            return false;
        }
        if (this.videoSendButton != null && isInVideoMode() && (frameLayout = this.recordedAudioPanel) != null && frameLayout.getVisibility() == 0) {
            return false;
        }
        return !hasBotWebView() || !this.botCommandsMenuButton.isOpened();
    }

    public int getHeightWithTopView() {
        int measuredHeight = getMeasuredHeight();
        View view = this.topView;
        return (view == null || view.getVisibility() != 0) ? measuredHeight : (int) (measuredHeight - ((1.0f - this.topViewEnterProgress) * this.topView.getLayoutParams().height));
    }

    public void setAdjustPanLayoutHelper(AdjustPanLayoutHelper adjustPanLayoutHelper) {
        this.adjustPanLayoutHelper = adjustPanLayoutHelper;
    }

    public AdjustPanLayoutHelper getAdjustPanLayoutHelper() {
        return this.adjustPanLayoutHelper;
    }

    public boolean panelAnimationInProgress() {
        return this.panelAnimation != null;
    }

    public float getTopViewTranslation() {
        View view = this.topView;
        if (view == null || view.getVisibility() == 8) {
            return 0.0f;
        }
        return this.topView.getTranslationY();
    }

    public int getAnimatedTop() {
        return this.animatedTop;
    }

    /* loaded from: classes3.dex */
    public class ScrimDrawable extends Drawable {
        private Paint paint;

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public ScrimDrawable() {
            ChatActivityEnterView.this = r2;
            Paint paint = new Paint();
            this.paint = paint;
            paint.setColor(0);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            if (ChatActivityEnterView.this.emojiView == null) {
                return;
            }
            this.paint.setAlpha(Math.round(ChatActivityEnterView.this.stickersExpansionProgress * 102.0f));
            canvas.drawRect(0.0f, 0.0f, ChatActivityEnterView.this.getWidth(), (ChatActivityEnterView.this.emojiView.getY() - ChatActivityEnterView.this.getHeight()) + Theme.chat_composeShadowDrawable.getIntrinsicHeight(), this.paint);
        }
    }

    /* loaded from: classes3.dex */
    public class SlideTextView extends View {
        TextPaint bluePaint;
        float cancelAlpha;
        int cancelCharOffset;
        StaticLayout cancelLayout;
        String cancelString;
        float cancelToProgress;
        float cancelWidth;
        TextPaint grayPaint;
        private int lastSize;
        long lastUpdateTime;
        boolean moveForward;
        private boolean pressed;
        Drawable selectableBackground;
        float slideProgress;
        float slideToAlpha;
        String slideToCancelString;
        float slideToCancelWidth;
        StaticLayout slideToLayout;
        boolean smallSize;
        Paint arrowPaint = new Paint(1);
        float xOffset = 0.0f;
        Path arrowPath = new Path();
        public android.graphics.Rect cancelRect = new android.graphics.Rect();

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                setPressed(false);
            }
            if (this.cancelToProgress == 0.0f || !isEnabled()) {
                return false;
            }
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            if (motionEvent.getAction() == 0) {
                boolean contains = this.cancelRect.contains(x, y);
                this.pressed = contains;
                if (contains) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        this.selectableBackground.setHotspot(x, y);
                    }
                    setPressed(true);
                }
                return this.pressed;
            }
            boolean z = this.pressed;
            if (!z) {
                return z;
            }
            if (motionEvent.getAction() == 2 && !this.cancelRect.contains(x, y)) {
                setPressed(false);
                return false;
            }
            if (motionEvent.getAction() == 1 && this.cancelRect.contains(x, y)) {
                onCancelButtonPressed();
            }
            return true;
        }

        public void onCancelButtonPressed() {
            if (!ChatActivityEnterView.this.hasRecordVideo || ChatActivityEnterView.this.videoSendButton.getTag() == null) {
                ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                MediaController.getInstance().stopRecording(0, false, 0);
            } else {
                CameraController.getInstance().cancelOnInitRunnable(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                ChatActivityEnterView.this.delegate.needStartRecordVideo(5, true, 0);
            }
            ChatActivityEnterView.this.recordingAudioVideo = false;
            ChatActivityEnterView.this.updateRecordInterface(2);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SlideTextView(Context context) {
            super(context);
            ChatActivityEnterView.this = r5;
            this.smallSize = AndroidUtilities.displaySize.x <= AndroidUtilities.dp(320.0f);
            TextPaint textPaint = new TextPaint(1);
            this.grayPaint = textPaint;
            textPaint.setTextSize(AndroidUtilities.dp(this.smallSize ? 13.0f : 15.0f));
            TextPaint textPaint2 = new TextPaint(1);
            this.bluePaint = textPaint2;
            textPaint2.setTextSize(AndroidUtilities.dp(15.0f));
            this.bluePaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.arrowPaint.setColor(r5.getThemedColor("chat_messagePanelIcons"));
            this.arrowPaint.setStyle(Paint.Style.STROKE);
            this.arrowPaint.setStrokeWidth(AndroidUtilities.dpf2(this.smallSize ? 1.0f : 1.6f));
            this.arrowPaint.setStrokeCap(Paint.Cap.ROUND);
            this.arrowPaint.setStrokeJoin(Paint.Join.ROUND);
            this.slideToCancelString = LocaleController.getString("SlideToCancel", 2131628357);
            this.slideToCancelString = this.slideToCancelString.charAt(0) + this.slideToCancelString.substring(1).toLowerCase();
            String upperCase = LocaleController.getString("Cancel", 2131624819).toUpperCase();
            this.cancelString = upperCase;
            this.cancelCharOffset = this.slideToCancelString.indexOf(upperCase);
            updateColors();
        }

        public void updateColors() {
            this.grayPaint.setColor(ChatActivityEnterView.this.getThemedColor("chat_recordTime"));
            this.bluePaint.setColor(ChatActivityEnterView.this.getThemedColor("chat_recordVoiceCancel"));
            this.slideToAlpha = this.grayPaint.getAlpha();
            this.cancelAlpha = this.bluePaint.getAlpha();
            Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(60.0f), 0, ColorUtils.setAlphaComponent(ChatActivityEnterView.this.getThemedColor("chat_recordVoiceCancel"), 26));
            this.selectableBackground = createSimpleSelectorCircleDrawable;
            createSimpleSelectorCircleDrawable.setCallback(this);
        }

        @Override // android.view.View
        protected void drawableStateChanged() {
            super.drawableStateChanged();
            this.selectableBackground.setState(getDrawableState());
        }

        @Override // android.view.View
        public boolean verifyDrawable(Drawable drawable) {
            return this.selectableBackground == drawable || super.verifyDrawable(drawable);
        }

        @Override // android.view.View
        public void jumpDrawablesToCurrentState() {
            super.jumpDrawablesToCurrentState();
            Drawable drawable = this.selectableBackground;
            if (drawable != null) {
                drawable.jumpToCurrentState();
            }
        }

        @Override // android.view.View
        @SuppressLint({"DrawAllocation"})
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            int measuredHeight = getMeasuredHeight() + (getMeasuredWidth() << 16);
            if (this.lastSize != measuredHeight) {
                this.lastSize = measuredHeight;
                this.slideToCancelWidth = this.grayPaint.measureText(this.slideToCancelString);
                this.cancelWidth = this.bluePaint.measureText(this.cancelString);
                this.lastUpdateTime = System.currentTimeMillis();
                int measuredHeight2 = getMeasuredHeight() >> 1;
                this.arrowPath.reset();
                if (this.smallSize) {
                    float f = measuredHeight2;
                    this.arrowPath.setLastPoint(AndroidUtilities.dpf2(2.5f), f - AndroidUtilities.dpf2(3.12f));
                    this.arrowPath.lineTo(0.0f, f);
                    this.arrowPath.lineTo(AndroidUtilities.dpf2(2.5f), f + AndroidUtilities.dpf2(3.12f));
                } else {
                    float f2 = measuredHeight2;
                    this.arrowPath.setLastPoint(AndroidUtilities.dpf2(4.0f), f2 - AndroidUtilities.dpf2(5.0f));
                    this.arrowPath.lineTo(0.0f, f2);
                    this.arrowPath.lineTo(AndroidUtilities.dpf2(4.0f), f2 + AndroidUtilities.dpf2(5.0f));
                }
                this.slideToLayout = new StaticLayout(this.slideToCancelString, this.grayPaint, (int) this.slideToCancelWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.cancelLayout = new StaticLayout(this.cancelString, this.bluePaint, (int) this.cancelWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            StaticLayout staticLayout;
            if (this.slideToLayout == null || (staticLayout = this.cancelLayout) == null) {
                return;
            }
            int width = staticLayout.getWidth() + AndroidUtilities.dp(16.0f);
            this.grayPaint.setColor(ChatActivityEnterView.this.getThemedColor("chat_recordTime"));
            this.grayPaint.setAlpha((int) (this.slideToAlpha * (1.0f - this.cancelToProgress) * this.slideProgress));
            this.bluePaint.setAlpha((int) (this.cancelAlpha * this.cancelToProgress));
            this.arrowPaint.setColor(this.grayPaint.getColor());
            boolean z = true;
            if (this.smallSize) {
                this.xOffset = AndroidUtilities.dp(16.0f);
            } else {
                long currentTimeMillis = System.currentTimeMillis() - this.lastUpdateTime;
                this.lastUpdateTime = System.currentTimeMillis();
                if (this.cancelToProgress == 0.0f && this.slideProgress > 0.8f) {
                    if (this.moveForward) {
                        float dp = this.xOffset + ((AndroidUtilities.dp(3.0f) / 250.0f) * ((float) currentTimeMillis));
                        this.xOffset = dp;
                        if (dp > AndroidUtilities.dp(6.0f)) {
                            this.xOffset = AndroidUtilities.dp(6.0f);
                            this.moveForward = false;
                        }
                    } else {
                        float dp2 = this.xOffset - ((AndroidUtilities.dp(3.0f) / 250.0f) * ((float) currentTimeMillis));
                        this.xOffset = dp2;
                        if (dp2 < (-AndroidUtilities.dp(6.0f))) {
                            this.xOffset = -AndroidUtilities.dp(6.0f);
                            this.moveForward = true;
                        }
                    }
                }
            }
            if (this.cancelCharOffset < 0) {
                z = false;
            }
            int measuredWidth = ((int) ((getMeasuredWidth() - this.slideToCancelWidth) / 2.0f)) + AndroidUtilities.dp(5.0f);
            int measuredWidth2 = (int) ((getMeasuredWidth() - this.cancelWidth) / 2.0f);
            float primaryHorizontal = z ? this.slideToLayout.getPrimaryHorizontal(this.cancelCharOffset) : 0.0f;
            float f = z ? (measuredWidth + primaryHorizontal) - measuredWidth2 : 0.0f;
            float f2 = this.xOffset;
            float f3 = this.cancelToProgress;
            float dp3 = ((measuredWidth + ((f2 * (1.0f - f3)) * this.slideProgress)) - (f * f3)) + AndroidUtilities.dp(16.0f);
            float dp4 = z ? 0.0f : this.cancelToProgress * AndroidUtilities.dp(12.0f);
            if (this.cancelToProgress != 1.0f) {
                int i = (int) (((-getMeasuredWidth()) / 4) * (1.0f - this.slideProgress));
                canvas.save();
                canvas.clipRect(ChatActivityEnterView.this.recordTimerView.getLeftProperty() + AndroidUtilities.dp(4.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                canvas.save();
                int i2 = (int) dp3;
                canvas.translate((i2 - AndroidUtilities.dp(this.smallSize ? 7.0f : 10.0f)) + i, dp4);
                canvas.drawPath(this.arrowPath, this.arrowPaint);
                canvas.restore();
                canvas.save();
                canvas.translate(i2 + i, ((getMeasuredHeight() - this.slideToLayout.getHeight()) / 2.0f) + dp4);
                this.slideToLayout.draw(canvas);
                canvas.restore();
                canvas.restore();
            }
            float measuredHeight = (getMeasuredHeight() - this.cancelLayout.getHeight()) / 2.0f;
            if (!z) {
                measuredHeight -= AndroidUtilities.dp(12.0f) - dp4;
            }
            float f4 = z ? dp3 + primaryHorizontal : measuredWidth2;
            this.cancelRect.set((int) f4, (int) measuredHeight, (int) (this.cancelLayout.getWidth() + f4), (int) (this.cancelLayout.getHeight() + measuredHeight));
            this.cancelRect.inset(-AndroidUtilities.dp(16.0f), -AndroidUtilities.dp(16.0f));
            if (this.cancelToProgress > 0.0f) {
                this.selectableBackground.setBounds((getMeasuredWidth() / 2) - width, (getMeasuredHeight() / 2) - width, (getMeasuredWidth() / 2) + width, (getMeasuredHeight() / 2) + width);
                this.selectableBackground.draw(canvas);
                canvas.save();
                canvas.translate(f4, measuredHeight);
                this.cancelLayout.draw(canvas);
                canvas.restore();
            } else {
                setPressed(false);
            }
            if (this.cancelToProgress == 1.0f) {
                return;
            }
            invalidate();
        }

        @Keep
        public void setCancelToProgress(float f) {
            this.cancelToProgress = f;
        }

        @Keep
        public float getSlideToCancelWidth() {
            return this.slideToCancelWidth;
        }

        public void setSlideX(float f) {
            this.slideProgress = f;
        }
    }

    /* loaded from: classes3.dex */
    public class TimerView extends View {
        StaticLayout inLayout;
        boolean isRunning;
        long lastSendTypingTime;
        float left;
        String oldString;
        StaticLayout outLayout;
        float replaceTransition;
        long startTime;
        long stopTime;
        boolean stoppedInternal;
        final TextPaint textPaint;
        SpannableStringBuilder replaceIn = new SpannableStringBuilder();
        SpannableStringBuilder replaceOut = new SpannableStringBuilder();
        SpannableStringBuilder replaceStable = new SpannableStringBuilder();
        final float replaceDistance = AndroidUtilities.dp(15.0f);

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TimerView(Context context) {
            super(context);
            ChatActivityEnterView.this = r2;
            TextPaint textPaint = new TextPaint(1);
            this.textPaint = textPaint;
            textPaint.setTextSize(AndroidUtilities.dp(15.0f));
            textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            updateColors();
        }

        public void start() {
            this.isRunning = true;
            long currentTimeMillis = System.currentTimeMillis();
            this.startTime = currentTimeMillis;
            this.lastSendTypingTime = currentTimeMillis;
            invalidate();
        }

        public void stop() {
            if (this.isRunning) {
                this.isRunning = false;
                if (this.startTime > 0) {
                    this.stopTime = System.currentTimeMillis();
                }
                invalidate();
            }
            this.lastSendTypingTime = 0L;
        }

        @Override // android.view.View
        @SuppressLint({"DrawAllocation"})
        protected void onDraw(Canvas canvas) {
            String str;
            SpannableStringBuilder spannableStringBuilder;
            String str2;
            long currentTimeMillis = System.currentTimeMillis();
            long j = this.isRunning ? currentTimeMillis - this.startTime : this.stopTime - this.startTime;
            long j2 = j / 1000;
            int i = ((int) (j % 1000)) / 10;
            if (ChatActivityEnterView.this.videoSendButton != null && ChatActivityEnterView.this.videoSendButton.getTag() != null && j >= 59500 && !this.stoppedInternal) {
                ChatActivityEnterView.this.startedDraggingX = -1.0f;
                ChatActivityEnterView.this.delegate.needStartRecordVideo(3, true, 0);
                this.stoppedInternal = true;
            }
            if (this.isRunning && currentTimeMillis > this.lastSendTypingTime + 5000) {
                this.lastSendTypingTime = currentTimeMillis;
                MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).sendTyping(ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.getThreadMessageId(), (ChatActivityEnterView.this.videoSendButton == null || ChatActivityEnterView.this.videoSendButton.getTag() == null) ? 1 : 7, 0);
            }
            long j3 = j2 / 60;
            if (j3 >= 60) {
                str = String.format(Locale.US, "%01d:%02d:%02d,%d", Long.valueOf(j3 / 60), Long.valueOf(j3 % 60), Long.valueOf(j2 % 60), Integer.valueOf(i / 10));
            } else {
                str = String.format(Locale.US, "%01d:%02d,%d", Long.valueOf(j3), Long.valueOf(j2 % 60), Integer.valueOf(i / 10));
            }
            if (str.length() >= 3 && (str2 = this.oldString) != null && str2.length() >= 3 && str.length() == this.oldString.length() && str.charAt(str.length() - 3) != this.oldString.charAt(str.length() - 3)) {
                int length = str.length();
                this.replaceIn.clear();
                this.replaceOut.clear();
                this.replaceStable.clear();
                this.replaceIn.append((CharSequence) str);
                this.replaceOut.append((CharSequence) this.oldString);
                this.replaceStable.append((CharSequence) str);
                int i2 = -1;
                int i3 = -1;
                int i4 = 0;
                int i5 = 0;
                for (int i6 = 0; i6 < length - 1; i6++) {
                    if (this.oldString.charAt(i6) != str.charAt(i6)) {
                        if (i5 == 0) {
                            i3 = i6;
                        }
                        i5++;
                        if (i4 != 0) {
                            EmptyStubSpan emptyStubSpan = new EmptyStubSpan();
                            if (i6 == length - 2) {
                                i4++;
                            }
                            int i7 = i4 + i2;
                            this.replaceIn.setSpan(emptyStubSpan, i2, i7, 33);
                            this.replaceOut.setSpan(emptyStubSpan, i2, i7, 33);
                            i4 = 0;
                        }
                    } else {
                        if (i4 == 0) {
                            i2 = i6;
                        }
                        i4++;
                        if (i5 != 0) {
                            this.replaceStable.setSpan(new EmptyStubSpan(), i3, i5 + i3, 33);
                            i5 = 0;
                        }
                    }
                }
                if (i4 != 0) {
                    EmptyStubSpan emptyStubSpan2 = new EmptyStubSpan();
                    int i8 = i4 + i2 + 1;
                    this.replaceIn.setSpan(emptyStubSpan2, i2, i8, 33);
                    this.replaceOut.setSpan(emptyStubSpan2, i2, i8, 33);
                }
                if (i5 != 0) {
                    this.replaceStable.setSpan(new EmptyStubSpan(), i3, i5 + i3, 33);
                }
                this.inLayout = new StaticLayout(this.replaceIn, this.textPaint, getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.outLayout = new StaticLayout(this.replaceOut, this.textPaint, getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.replaceTransition = 1.0f;
            } else {
                if (this.replaceStable == null) {
                    this.replaceStable = new SpannableStringBuilder(str);
                }
                if (this.replaceStable.length() == 0 || this.replaceStable.length() != str.length()) {
                    this.replaceStable.clear();
                    this.replaceStable.append((CharSequence) str);
                } else {
                    this.replaceStable.replace(spannableStringBuilder.length() - 1, this.replaceStable.length(), (CharSequence) str, (str.length() - 1) - (str.length() - this.replaceStable.length()), str.length());
                }
            }
            float f = this.replaceTransition;
            if (f != 0.0f) {
                float f2 = f - 0.15f;
                this.replaceTransition = f2;
                if (f2 < 0.0f) {
                    this.replaceTransition = 0.0f;
                }
            }
            float measuredHeight = getMeasuredHeight() / 2;
            if (this.replaceTransition == 0.0f) {
                this.replaceStable.clearSpans();
                StaticLayout staticLayout = new StaticLayout(this.replaceStable, this.textPaint, getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                canvas.save();
                canvas.translate(0.0f, measuredHeight - (staticLayout.getHeight() / 2.0f));
                staticLayout.draw(canvas);
                canvas.restore();
                this.left = staticLayout.getLineWidth(0) + 0.0f;
            } else {
                if (this.inLayout != null) {
                    canvas.save();
                    this.textPaint.setAlpha((int) ((1.0f - this.replaceTransition) * 255.0f));
                    canvas.translate(0.0f, (measuredHeight - (this.inLayout.getHeight() / 2.0f)) - (this.replaceDistance * this.replaceTransition));
                    this.inLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.outLayout != null) {
                    canvas.save();
                    this.textPaint.setAlpha((int) (this.replaceTransition * 255.0f));
                    canvas.translate(0.0f, (measuredHeight - (this.outLayout.getHeight() / 2.0f)) + (this.replaceDistance * (1.0f - this.replaceTransition)));
                    this.outLayout.draw(canvas);
                    canvas.restore();
                }
                canvas.save();
                this.textPaint.setAlpha(255);
                StaticLayout staticLayout2 = new StaticLayout(this.replaceStable, this.textPaint, getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                canvas.translate(0.0f, measuredHeight - (staticLayout2.getHeight() / 2.0f));
                staticLayout2.draw(canvas);
                canvas.restore();
                this.left = staticLayout2.getLineWidth(0) + 0.0f;
            }
            this.oldString = str;
            if (this.isRunning || this.replaceTransition != 0.0f) {
                invalidate();
            }
        }

        public void updateColors() {
            this.textPaint.setColor(ChatActivityEnterView.this.getThemedColor("chat_recordTime"));
        }

        public float getLeftProperty() {
            return this.left;
        }

        public void reset() {
            this.isRunning = false;
            this.startTime = 0L;
            this.stopTime = 0L;
            this.stoppedInternal = false;
        }
    }

    public RecordCircle getRecordCicle() {
        return this.recordCircle;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        LinearLayoutManager linearLayoutManager;
        int findFirstVisibleItemPosition;
        View findViewByPosition;
        BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
        if (botCommandsMenuView != null && botCommandsMenuView.getTag() != null) {
            this.botCommandsMenuButton.measure(i, i2);
            int i4 = 0;
            while (true) {
                ImageView[] imageViewArr = this.emojiButton;
                if (i4 >= imageViewArr.length) {
                    break;
                }
                ((ViewGroup.MarginLayoutParams) imageViewArr[i4].getLayoutParams()).leftMargin = AndroidUtilities.dp(10.0f) + this.botCommandsMenuButton.getMeasuredWidth();
                i4++;
            }
            ((ViewGroup.MarginLayoutParams) this.messageEditText.getLayoutParams()).leftMargin = AndroidUtilities.dp(57.0f) + this.botCommandsMenuButton.getMeasuredWidth();
        } else {
            SenderSelectView senderSelectView = this.senderSelectView;
            if (senderSelectView != null && senderSelectView.getVisibility() == 0) {
                int i5 = this.senderSelectView.getLayoutParams().width;
                this.senderSelectView.measure(View.MeasureSpec.makeMeasureSpec(i5, 1073741824), View.MeasureSpec.makeMeasureSpec(this.senderSelectView.getLayoutParams().height, 1073741824));
                int i6 = 0;
                while (true) {
                    ImageView[] imageViewArr2 = this.emojiButton;
                    if (i6 >= imageViewArr2.length) {
                        break;
                    }
                    ((ViewGroup.MarginLayoutParams) imageViewArr2[i6].getLayoutParams()).leftMargin = AndroidUtilities.dp(16.0f) + i5;
                    i6++;
                }
                ((ViewGroup.MarginLayoutParams) this.messageEditText.getLayoutParams()).leftMargin = AndroidUtilities.dp(63.0f) + i5;
            } else {
                int i7 = 0;
                while (true) {
                    ImageView[] imageViewArr3 = this.emojiButton;
                    if (i7 >= imageViewArr3.length) {
                        break;
                    }
                    ((ViewGroup.MarginLayoutParams) imageViewArr3[i7].getLayoutParams()).leftMargin = AndroidUtilities.dp(3.0f);
                    i7++;
                }
                ((ViewGroup.MarginLayoutParams) this.messageEditText.getLayoutParams()).leftMargin = AndroidUtilities.dp(50.0f);
            }
        }
        if (this.botCommandsMenuContainer != null) {
            if (this.botCommandsAdapter.getItemCount() > 4) {
                i3 = Math.max(0, this.sizeNotifierLayout.getMeasuredHeight() - AndroidUtilities.dp(162.8f));
            } else {
                i3 = Math.max(0, this.sizeNotifierLayout.getMeasuredHeight() - AndroidUtilities.dp((Math.max(1, Math.min(4, this.botCommandsAdapter.getItemCount())) * 36) + 8));
            }
            if (this.botCommandsMenuContainer.listView.getPaddingTop() != i3) {
                this.botCommandsMenuContainer.listView.setTopGlowOffset(i3);
                if (this.botCommandLastPosition == -1 && this.botCommandsMenuContainer.getVisibility() == 0 && this.botCommandsMenuContainer.listView.getLayoutManager() != null && (findFirstVisibleItemPosition = (linearLayoutManager = (LinearLayoutManager) this.botCommandsMenuContainer.listView.getLayoutManager()).findFirstVisibleItemPosition()) >= 0 && (findViewByPosition = linearLayoutManager.findViewByPosition(findFirstVisibleItemPosition)) != null) {
                    this.botCommandLastPosition = findFirstVisibleItemPosition;
                    this.botCommandLastTop = findViewByPosition.getTop() - this.botCommandsMenuContainer.listView.getPaddingTop();
                }
                this.botCommandsMenuContainer.listView.setPadding(0, i3, 0, AndroidUtilities.dp(8.0f));
            }
        }
        super.onMeasure(i, i2);
        ChatActivityBotWebViewButton chatActivityBotWebViewButton = this.botWebViewButton;
        if (chatActivityBotWebViewButton != null) {
            BotCommandsMenuView botCommandsMenuView2 = this.botCommandsMenuButton;
            if (botCommandsMenuView2 != null) {
                chatActivityBotWebViewButton.setMeasuredButtonWidth(botCommandsMenuView2.getMeasuredWidth());
            }
            this.botWebViewButton.getLayoutParams().height = getMeasuredHeight() - AndroidUtilities.dp(2.0f);
            measureChild(this.botWebViewButton, i, i2);
        }
        BotWebViewMenuContainer botWebViewMenuContainer = this.botWebViewMenuContainer;
        if (botWebViewMenuContainer != null) {
            ((ViewGroup.MarginLayoutParams) botWebViewMenuContainer.getLayoutParams()).bottomMargin = this.messageEditText.getMeasuredHeight();
            measureChild(this.botWebViewMenuContainer, i, i2);
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.botCommandLastPosition != -1) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) this.botCommandsMenuContainer.listView.getLayoutManager();
            if (linearLayoutManager != null) {
                linearLayoutManager.scrollToPositionWithOffset(this.botCommandLastPosition, this.botCommandLastTop);
            }
            this.botCommandLastPosition = -1;
        }
    }

    private void beginDelayedTransition() {
        HashMap<View, Float> hashMap = this.animationParamsX;
        ImageView[] imageViewArr = this.emojiButton;
        hashMap.put(imageViewArr[0], Float.valueOf(imageViewArr[0].getX()));
        HashMap<View, Float> hashMap2 = this.animationParamsX;
        ImageView[] imageViewArr2 = this.emojiButton;
        hashMap2.put(imageViewArr2[1], Float.valueOf(imageViewArr2[1].getX()));
        HashMap<View, Float> hashMap3 = this.animationParamsX;
        EditTextCaption editTextCaption = this.messageEditText;
        hashMap3.put(editTextCaption, Float.valueOf(editTextCaption.getX()));
    }

    public void setBotInfo(LongSparseArray<TLRPC$BotInfo> longSparseArray) {
        if (longSparseArray.size() == 1 && longSparseArray.valueAt(0).user_id == this.dialog_id) {
            TLRPC$BotInfo valueAt = longSparseArray.valueAt(0);
            TLRPC$BotMenuButton tLRPC$BotMenuButton = valueAt.menu_button;
            if (tLRPC$BotMenuButton instanceof TLRPC$TL_botMenuButton) {
                TLRPC$TL_botMenuButton tLRPC$TL_botMenuButton = (TLRPC$TL_botMenuButton) tLRPC$BotMenuButton;
                this.botMenuWebViewTitle = tLRPC$TL_botMenuButton.text;
                this.botMenuWebViewUrl = tLRPC$TL_botMenuButton.url;
                this.botMenuButtonType = BotMenuButtonType.WEB_VIEW;
            } else if (!valueAt.commands.isEmpty()) {
                this.botMenuButtonType = BotMenuButtonType.COMMANDS;
            } else {
                this.botMenuButtonType = BotMenuButtonType.NO_BUTTON;
            }
        } else {
            this.botMenuButtonType = BotMenuButtonType.NO_BUTTON;
        }
        BotCommandsMenuView.BotCommandsAdapter botCommandsAdapter = this.botCommandsAdapter;
        if (botCommandsAdapter != null) {
            botCommandsAdapter.setBotInfo(longSparseArray);
        }
    }

    public boolean botCommandsMenuIsShowing() {
        BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
        return botCommandsMenuView != null && botCommandsMenuView.isOpened();
    }

    public void hideBotCommands() {
        this.botCommandsMenuButton.setOpened(false);
        if (hasBotWebView()) {
            this.botWebViewMenuContainer.dismiss();
        } else {
            this.botCommandsMenuContainer.dismiss();
        }
    }

    public void setTextTransitionIsRunning(boolean z) {
        this.textTransitionIsRunning = z;
        this.sendButtonContainer.invalidate();
    }

    public float getTopViewHeight() {
        View view = this.topView;
        if (view == null || view.getVisibility() != 0) {
            return 0.0f;
        }
        return this.topView.getLayoutParams().height;
    }

    public void runEmojiPanelAnimation() {
        AndroidUtilities.cancelRunOnUIThread(this.runEmojiPanelAnimation);
        this.runEmojiPanelAnimation.run();
    }

    public Drawable getStickersArrowDrawable() {
        return this.stickersArrow;
    }

    @Override // org.telegram.ui.Components.BlurredFrameLayout, android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        EmojiView emojiView = this.emojiView;
        if (emojiView == null || emojiView.getVisibility() != 0 || this.emojiView.getStickersExpandOffset() == 0.0f) {
            super.dispatchDraw(canvas);
            return;
        }
        canvas.save();
        canvas.clipRect(0, AndroidUtilities.dp(2.0f), getMeasuredWidth(), getMeasuredHeight());
        canvas.translate(0.0f, -this.emojiView.getStickersExpandOffset());
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    public int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }

    private Paint getThemedPaint(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Paint paint = resourcesProvider != null ? resourcesProvider.getPaint(str) : null;
        return paint != null ? paint : Theme.getThemePaint(str);
    }

    public void setChatSearchExpandOffset(float f) {
        this.chatSearchExpandOffset = f;
        invalidate();
    }
}