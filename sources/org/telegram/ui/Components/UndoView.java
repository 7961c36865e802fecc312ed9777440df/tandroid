package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Keep;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$TL_authorization;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_payments_getPaymentReceipt;
import org.telegram.tgnet.TLRPC$TL_payments_paymentReceipt;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.PaymentFormActivity;
/* loaded from: classes3.dex */
public class UndoView extends FrameLayout {
    public static int ACTION_RINGTONE_ADDED = 83;
    private float additionalTranslationY;
    private BackupImageView avatarImageView;
    Drawable backgroundDrawable;
    private int currentAccount;
    private int currentAction;
    private Runnable currentActionRunnable;
    private Runnable currentCancelRunnable;
    private ArrayList<Long> currentDialogIds;
    private Object currentInfoObject;
    float enterOffset;
    private int enterOffsetMargin;
    private boolean fromTop;
    private int hideAnimationType;
    private CharSequence infoText;
    private TextView infoTextView;
    private boolean isShown;
    private long lastUpdateTime;
    private RLottieImageView leftImageView;
    private BaseFragment parentFragment;
    private int prevSeconds;
    private Paint progressPaint;
    private RectF rect;
    private final Theme.ResourcesProvider resourcesProvider;
    private TextView subinfoTextView;
    private TextPaint textPaint;
    private int textWidth;
    StaticLayout timeLayout;
    StaticLayout timeLayoutOut;
    private long timeLeft;
    private String timeLeftString;
    float timeReplaceProgress;
    private LinearLayout undoButton;
    private ImageView undoImageView;
    private TextView undoTextView;
    private int undoViewHeight;

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$new$1(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$showWithAction$3(View view, MotionEvent motionEvent) {
        return true;
    }

    protected boolean canUndo() {
        return true;
    }

    public void didPressUrl(CharacterStyle characterStyle) {
    }

    protected void onRemoveDialogAction(long j, int i) {
    }

    /* loaded from: classes3.dex */
    public class LinkMovementMethodMy extends LinkMovementMethod {
        public LinkMovementMethodMy() {
        }

        @Override // android.text.method.LinkMovementMethod, android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod, android.text.method.MovementMethod
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
            CharacterStyle[] characterStyleArr;
            try {
                if (motionEvent.getAction() == 0 && ((characterStyleArr = (CharacterStyle[]) spannable.getSpans(textView.getSelectionStart(), textView.getSelectionEnd(), CharacterStyle.class)) == null || characterStyleArr.length == 0)) {
                    return false;
                }
                if (motionEvent.getAction() == 1) {
                    CharacterStyle[] characterStyleArr2 = (CharacterStyle[]) spannable.getSpans(textView.getSelectionStart(), textView.getSelectionEnd(), CharacterStyle.class);
                    if (characterStyleArr2 != null && characterStyleArr2.length > 0) {
                        UndoView.this.didPressUrl(characterStyleArr2[0]);
                    }
                    Selection.removeSelection(spannable);
                    return true;
                }
                return super.onTouchEvent(textView, spannable, motionEvent);
            } catch (Exception e) {
                FileLog.e(e);
                return false;
            }
        }
    }

    public UndoView(Context context) {
        this(context, null, false, null);
    }

    public UndoView(Context context, BaseFragment baseFragment) {
        this(context, baseFragment, false, null);
    }

    public UndoView(Context context, BaseFragment baseFragment, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.currentAction = -1;
        this.hideAnimationType = 1;
        this.enterOffsetMargin = AndroidUtilities.dp(8.0f);
        this.timeReplaceProgress = 1.0f;
        this.resourcesProvider = resourcesProvider;
        this.parentFragment = baseFragment;
        this.fromTop = z;
        TextView textView = new TextView(context);
        this.infoTextView = textView;
        textView.setTextSize(1, 15.0f);
        this.infoTextView.setTextColor(getThemedColor("undo_infoColor"));
        this.infoTextView.setLinkTextColor(getThemedColor("undo_cancelColor"));
        this.infoTextView.setMovementMethod(new LinkMovementMethodMy());
        addView(this.infoTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 45.0f, 13.0f, 0.0f, 0.0f));
        TextView textView2 = new TextView(context);
        this.subinfoTextView = textView2;
        textView2.setTextSize(1, 13.0f);
        this.subinfoTextView.setTextColor(getThemedColor("undo_infoColor"));
        this.subinfoTextView.setLinkTextColor(getThemedColor("undo_cancelColor"));
        this.subinfoTextView.setHighlightColor(0);
        this.subinfoTextView.setSingleLine(true);
        this.subinfoTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.subinfoTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        addView(this.subinfoTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 58.0f, 27.0f, 8.0f, 0.0f));
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.leftImageView = rLottieImageView;
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        this.leftImageView.setLayerColor("info1.**", getThemedColor("undo_background") | (-16777216));
        this.leftImageView.setLayerColor("info2.**", getThemedColor("undo_background") | (-16777216));
        this.leftImageView.setLayerColor("luc12.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc11.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc10.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc9.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc8.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc7.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc6.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc5.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc4.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc3.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc2.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc1.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("Oval.**", getThemedColor("undo_infoColor"));
        addView(this.leftImageView, LayoutHelper.createFrame(54, -2.0f, 19, 3.0f, 0.0f, 0.0f, 0.0f));
        BackupImageView backupImageView = new BackupImageView(context);
        this.avatarImageView = backupImageView;
        backupImageView.setRoundRadius(AndroidUtilities.dp(15.0f));
        addView(this.avatarImageView, LayoutHelper.createFrame(30, 30.0f, 19, 15.0f, 0.0f, 0.0f, 0.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        this.undoButton = linearLayout;
        linearLayout.setOrientation(0);
        this.undoButton.setBackground(Theme.createRadSelectorDrawable(getThemedColor("undo_cancelColor") & 587202559, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f)));
        addView(this.undoButton, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 11.0f, 0.0f));
        this.undoButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                UndoView.this.lambda$new$0(view);
            }
        });
        ImageView imageView = new ImageView(context);
        this.undoImageView = imageView;
        imageView.setImageResource(R.drawable.chats_undo);
        this.undoImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor("undo_cancelColor"), PorterDuff.Mode.MULTIPLY));
        this.undoButton.addView(this.undoImageView, LayoutHelper.createLinear(-2, -2, 19, 4, 4, 0, 4));
        TextView textView3 = new TextView(context);
        this.undoTextView = textView3;
        textView3.setTextSize(1, 14.0f);
        this.undoTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.undoTextView.setTextColor(getThemedColor("undo_cancelColor"));
        this.undoTextView.setText(LocaleController.getString("Undo", R.string.Undo));
        this.undoButton.addView(this.undoTextView, LayoutHelper.createLinear(-2, -2, 19, 6, 4, 8, 4));
        this.rect = new RectF(AndroidUtilities.dp(15.0f), AndroidUtilities.dp(15.0f), AndroidUtilities.dp(33.0f), AndroidUtilities.dp(33.0f));
        Paint paint = new Paint(1);
        this.progressPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.progressPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.progressPaint.setStrokeCap(Paint.Cap.ROUND);
        this.progressPaint.setColor(getThemedColor("undo_infoColor"));
        TextPaint textPaint = new TextPaint(1);
        this.textPaint = textPaint;
        textPaint.setTextSize(AndroidUtilities.dp(12.0f));
        this.textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.textPaint.setColor(getThemedColor("undo_infoColor"));
        setWillNotDraw(false);
        this.backgroundDrawable = Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), getThemedColor("undo_background"));
        setOnTouchListener(UndoView$$ExternalSyntheticLambda3.INSTANCE);
        setVisibility(4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        if (!canUndo()) {
            return;
        }
        hide(false, 1);
    }

    public void setColors(int i, int i2) {
        Theme.setDrawableColor(this.backgroundDrawable, i);
        this.infoTextView.setTextColor(i2);
        this.subinfoTextView.setTextColor(i2);
        int i3 = i | (-16777216);
        this.leftImageView.setLayerColor("info1.**", i3);
        this.leftImageView.setLayerColor("info2.**", i3);
    }

    private boolean isTooltipAction() {
        int i = this.currentAction;
        return i == 6 || i == 3 || i == 5 || i == 7 || i == 8 || i == 9 || i == 10 || i == 13 || i == 14 || i == 19 || i == 20 || i == 21 || i == 22 || i == 23 || i == 30 || i == 31 || i == 32 || i == 33 || i == 34 || i == 35 || i == 36 || i == 74 || i == 37 || i == 38 || i == 39 || i == 40 || i == 42 || i == 43 || i == 77 || i == 44 || i == 78 || i == 79 || i == 100 || i == 101 || i == ACTION_RINGTONE_ADDED;
    }

    private boolean hasSubInfo() {
        int i = this.currentAction;
        return i == 11 || i == 24 || i == 6 || i == 3 || i == 5 || i == 13 || i == 14 || i == 74 || (i == 7 && MessagesController.getInstance(this.currentAccount).dialogFilters.isEmpty()) || this.currentAction == ACTION_RINGTONE_ADDED;
    }

    public boolean isMultilineSubInfo() {
        int i = this.currentAction;
        return i == 12 || i == 15 || i == 24 || i == 74 || i == ACTION_RINGTONE_ADDED;
    }

    public void setAdditionalTranslationY(float f) {
        if (this.additionalTranslationY != f) {
            this.additionalTranslationY = f;
            updatePosition();
        }
    }

    public Object getCurrentInfoObject() {
        return this.currentInfoObject;
    }

    public void hide(boolean z, int i) {
        if (getVisibility() != 0 || !this.isShown) {
            return;
        }
        this.currentInfoObject = null;
        this.isShown = false;
        Runnable runnable = this.currentActionRunnable;
        if (runnable != null) {
            if (z) {
                runnable.run();
            }
            this.currentActionRunnable = null;
        }
        Runnable runnable2 = this.currentCancelRunnable;
        if (runnable2 != null) {
            if (!z) {
                runnable2.run();
            }
            this.currentCancelRunnable = null;
        }
        int i2 = this.currentAction;
        if (i2 == 0 || i2 == 1 || i2 == 26 || i2 == 27) {
            for (int i3 = 0; i3 < this.currentDialogIds.size(); i3++) {
                long longValue = this.currentDialogIds.get(i3).longValue();
                MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                int i4 = this.currentAction;
                messagesController.removeDialogAction(longValue, i4 == 0 || i4 == 26, z);
                onRemoveDialogAction(longValue, this.currentAction);
            }
        }
        float f = -1.0f;
        if (i != 0) {
            AnimatorSet animatorSet = new AnimatorSet();
            if (i == 1) {
                Animator[] animatorArr = new Animator[1];
                float[] fArr = new float[1];
                if (!this.fromTop) {
                    f = 1.0f;
                }
                fArr[0] = f * (this.enterOffsetMargin + this.undoViewHeight);
                animatorArr[0] = ObjectAnimator.ofFloat(this, "enterOffset", fArr);
                animatorSet.playTogether(animatorArr);
                animatorSet.setDuration(250L);
            } else {
                animatorSet.playTogether(ObjectAnimator.ofFloat(this, View.SCALE_X, 0.8f), ObjectAnimator.ofFloat(this, View.SCALE_Y, 0.8f), ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f));
                animatorSet.setDuration(180L);
            }
            animatorSet.setInterpolator(new DecelerateInterpolator());
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.UndoView.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    UndoView.this.setVisibility(4);
                    UndoView.this.setScaleX(1.0f);
                    UndoView.this.setScaleY(1.0f);
                    UndoView.this.setAlpha(1.0f);
                }
            });
            animatorSet.start();
            return;
        }
        if (!this.fromTop) {
            f = 1.0f;
        }
        setEnterOffset(f * (this.enterOffsetMargin + this.undoViewHeight));
        setVisibility(4);
    }

    public void showWithAction(long j, int i, Runnable runnable) {
        showWithAction(j, i, (Object) null, (Object) null, runnable, (Runnable) null);
    }

    public void showWithAction(long j, int i, Object obj) {
        showWithAction(j, i, obj, (Object) null, (Runnable) null, (Runnable) null);
    }

    public void showWithAction(long j, int i, Runnable runnable, Runnable runnable2) {
        showWithAction(j, i, (Object) null, (Object) null, runnable, runnable2);
    }

    public void showWithAction(long j, int i, Object obj, Runnable runnable, Runnable runnable2) {
        showWithAction(j, i, obj, (Object) null, runnable, runnable2);
    }

    public void showWithAction(long j, int i, Object obj, Object obj2, Runnable runnable, Runnable runnable2) {
        ArrayList<Long> arrayList = new ArrayList<>();
        arrayList.add(Long.valueOf(j));
        showWithAction(arrayList, i, obj, obj2, runnable, runnable2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:48:0x160d  */
    /* JADX WARN: Removed duplicated region for block: B:503:0x15ed  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x1634  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x1723  */
    /* JADX WARN: Removed duplicated region for block: B:71:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x1679  */
    /* JADX WARN: Type inference failed for: r2v348, types: [java.lang.CharSequence] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void showWithAction(ArrayList<Long> arrayList, int i, Object obj, Object obj2, Runnable runnable, Runnable runnable2) {
        boolean z;
        float f;
        int dp;
        int i2;
        String str;
        CharSequence charSequence;
        int i3;
        String str2;
        CharSequence replaceTags;
        int i4;
        int i5;
        String str3;
        SpannableStringBuilder replaceTags2;
        int i6;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        int i7;
        int i8;
        if (AndroidUtilities.shouldShowClipboardToast() || !((i8 = this.currentAction) == 52 || i8 == 56 || i8 == 57 || i8 == 58 || i8 == 59 || i8 == 60 || i8 == 80 || i8 == 33)) {
            Runnable runnable3 = this.currentActionRunnable;
            if (runnable3 != null) {
                runnable3.run();
            }
            this.isShown = true;
            this.currentActionRunnable = runnable;
            this.currentCancelRunnable = runnable2;
            this.currentDialogIds = arrayList;
            long longValue = arrayList.get(0).longValue();
            this.currentAction = i;
            this.timeLeft = 5000L;
            this.currentInfoObject = obj;
            this.lastUpdateTime = SystemClock.elapsedRealtime();
            this.undoTextView.setText(LocaleController.getString("Undo", R.string.Undo).toUpperCase());
            this.undoImageView.setVisibility(0);
            this.leftImageView.setPadding(0, 0, 0, 0);
            this.infoTextView.setTextSize(1, 15.0f);
            this.avatarImageView.setVisibility(8);
            this.infoTextView.setGravity(51);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.infoTextView.getLayoutParams();
            layoutParams.height = -2;
            layoutParams.topMargin = AndroidUtilities.dp(13.0f);
            layoutParams.bottomMargin = 0;
            this.leftImageView.setScaleType(ImageView.ScaleType.CENTER);
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.leftImageView.getLayoutParams();
            layoutParams2.gravity = 19;
            layoutParams2.bottomMargin = 0;
            layoutParams2.topMargin = 0;
            layoutParams2.leftMargin = AndroidUtilities.dp(3.0f);
            layoutParams2.width = AndroidUtilities.dp(54.0f);
            layoutParams2.height = -2;
            this.infoTextView.setMinHeight(0);
            String str11 = null;
            if ((runnable == null && runnable2 == null) || i == ACTION_RINGTONE_ADDED) {
                setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        UndoView.this.lambda$showWithAction$2(view);
                    }
                });
                setOnTouchListener(null);
            } else {
                setOnClickListener(null);
                setOnTouchListener(UndoView$$ExternalSyntheticLambda4.INSTANCE);
            }
            this.infoTextView.setMovementMethod(null);
            String str12 = "";
            int i9 = 36;
            if (isTooltipAction()) {
                if (i == ACTION_RINGTONE_ADDED) {
                    this.subinfoTextView.setSingleLine(false);
                    replaceTags = LocaleController.getString("SoundAdded", R.string.SoundAdded);
                    ?? replaceSingleTag = AndroidUtilities.replaceSingleTag(LocaleController.getString("SoundAddedSubtitle", R.string.SoundAddedSubtitle), runnable);
                    this.currentActionRunnable = null;
                    i4 = R.raw.sound_download;
                    this.timeLeft = 4000L;
                    str11 = replaceSingleTag;
                } else if (i == 74) {
                    this.subinfoTextView.setSingleLine(false);
                    replaceTags = LocaleController.getString("ReportChatSent", R.string.ReportChatSent);
                    str11 = LocaleController.formatString("ReportSentInfo", R.string.ReportSentInfo, new Object[0]);
                    i4 = R.raw.ic_admin;
                    this.timeLeft = 4000L;
                } else {
                    if (i == 34) {
                        TLRPC$User tLRPC$User = (TLRPC$User) obj;
                        SpannableStringBuilder replaceTags3 = ChatObject.isChannelOrGiga((TLRPC$Chat) obj2) ? AndroidUtilities.replaceTags(LocaleController.formatString("VoipChannelInvitedUser", R.string.VoipChannelInvitedUser, UserObject.getFirstName(tLRPC$User))) : AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupInvitedUser", R.string.VoipGroupInvitedUser, UserObject.getFirstName(tLRPC$User)));
                        AvatarDrawable avatarDrawable = new AvatarDrawable();
                        avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
                        avatarDrawable.setInfo(tLRPC$User);
                        this.avatarImageView.setForUserOrChat(tLRPC$User, avatarDrawable);
                        this.avatarImageView.setVisibility(0);
                        this.timeLeft = 3000L;
                        replaceTags = replaceTags3;
                    } else if (i == 44) {
                        TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) obj2;
                        if (obj instanceof TLRPC$User) {
                            TLRPC$User tLRPC$User2 = (TLRPC$User) obj;
                            replaceTags = ChatObject.isChannelOrGiga(tLRPC$Chat) ? AndroidUtilities.replaceTags(LocaleController.formatString("VoipChannelUserJoined", R.string.VoipChannelUserJoined, UserObject.getFirstName(tLRPC$User2))) : AndroidUtilities.replaceTags(LocaleController.formatString("VoipChatUserJoined", R.string.VoipChatUserJoined, UserObject.getFirstName(tLRPC$User2)));
                        } else {
                            TLRPC$Chat tLRPC$Chat2 = (TLRPC$Chat) obj;
                            replaceTags = ChatObject.isChannelOrGiga(tLRPC$Chat) ? AndroidUtilities.replaceTags(LocaleController.formatString("VoipChannelChatJoined", R.string.VoipChannelChatJoined, tLRPC$Chat2.title)) : AndroidUtilities.replaceTags(LocaleController.formatString("VoipChatChatJoined", R.string.VoipChatChatJoined, tLRPC$Chat2.title));
                        }
                        AvatarDrawable avatarDrawable2 = new AvatarDrawable();
                        avatarDrawable2.setTextSize(AndroidUtilities.dp(12.0f));
                        TLObject tLObject = (TLObject) obj;
                        avatarDrawable2.setInfo(tLObject);
                        this.avatarImageView.setForUserOrChat(tLObject, avatarDrawable2);
                        this.avatarImageView.setVisibility(0);
                        this.timeLeft = 3000L;
                    } else if (i == 37) {
                        AvatarDrawable avatarDrawable3 = new AvatarDrawable();
                        avatarDrawable3.setTextSize(AndroidUtilities.dp(12.0f));
                        if (obj instanceof TLRPC$User) {
                            TLRPC$User tLRPC$User3 = (TLRPC$User) obj;
                            avatarDrawable3.setInfo(tLRPC$User3);
                            this.avatarImageView.setForUserOrChat(tLRPC$User3, avatarDrawable3);
                            str10 = ContactsController.formatName(tLRPC$User3.first_name, tLRPC$User3.last_name);
                        } else {
                            TLRPC$Chat tLRPC$Chat3 = (TLRPC$Chat) obj;
                            avatarDrawable3.setInfo(tLRPC$Chat3);
                            this.avatarImageView.setForUserOrChat(tLRPC$Chat3, avatarDrawable3);
                            str10 = tLRPC$Chat3.title;
                        }
                        replaceTags = ChatObject.isChannelOrGiga((TLRPC$Chat) obj2) ? AndroidUtilities.replaceTags(LocaleController.formatString("VoipChannelUserChanged", R.string.VoipChannelUserChanged, str10)) : AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserChanged", R.string.VoipGroupUserChanged, str10));
                        this.avatarImageView.setVisibility(0);
                        this.timeLeft = 3000L;
                    } else if (i == 33) {
                        replaceTags = LocaleController.getString("VoipGroupCopyInviteLinkCopied", R.string.VoipGroupCopyInviteLinkCopied);
                        i4 = R.raw.voip_invite;
                        this.timeLeft = 3000L;
                    } else if (i == 77) {
                        replaceTags = (CharSequence) obj;
                        i4 = R.raw.payment_success;
                        this.timeLeft = 5000L;
                        if (this.parentFragment != null && (obj2 instanceof TLRPC$Message)) {
                            final TLRPC$Message tLRPC$Message = (TLRPC$Message) obj2;
                            setOnTouchListener(null);
                            this.infoTextView.setMovementMethod(null);
                            setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda2
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    UndoView.this.lambda$showWithAction$6(tLRPC$Message, view);
                                }
                            });
                        }
                    } else if (i == 30) {
                        if (obj instanceof TLRPC$User) {
                            str9 = UserObject.getFirstName((TLRPC$User) obj);
                        } else {
                            str9 = ((TLRPC$Chat) obj).title;
                        }
                        replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserCantNowSpeak", R.string.VoipGroupUserCantNowSpeak, str9));
                        i4 = R.raw.voip_muted;
                        this.timeLeft = 3000L;
                    } else if (i == 35) {
                        if (obj instanceof TLRPC$User) {
                            str8 = UserObject.getFirstName((TLRPC$User) obj);
                        } else {
                            str8 = obj instanceof TLRPC$Chat ? ((TLRPC$Chat) obj).title : str12;
                        }
                        replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserCantNowSpeakForYou", R.string.VoipGroupUserCantNowSpeakForYou, str8));
                        i4 = R.raw.voip_muted;
                        this.timeLeft = 3000L;
                    } else if (i == 31) {
                        if (obj instanceof TLRPC$User) {
                            str7 = UserObject.getFirstName((TLRPC$User) obj);
                        } else {
                            str7 = ((TLRPC$Chat) obj).title;
                        }
                        replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserCanNowSpeak", R.string.VoipGroupUserCanNowSpeak, str7));
                        i4 = R.raw.voip_unmuted;
                        this.timeLeft = 3000L;
                    } else if (i == 38) {
                        replaceTags = obj instanceof TLRPC$Chat ? AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupYouCanNowSpeakIn", R.string.VoipGroupYouCanNowSpeakIn, ((TLRPC$Chat) obj).title)) : AndroidUtilities.replaceTags(LocaleController.getString("VoipGroupYouCanNowSpeak", R.string.VoipGroupYouCanNowSpeak));
                        i4 = R.raw.voip_allow_talk;
                        this.timeLeft = 3000L;
                    } else if (i == 42) {
                        if (ChatObject.isChannelOrGiga((TLRPC$Chat) obj)) {
                            replaceTags = AndroidUtilities.replaceTags(LocaleController.getString("VoipChannelSoundMuted", R.string.VoipChannelSoundMuted));
                        } else {
                            replaceTags = AndroidUtilities.replaceTags(LocaleController.getString("VoipGroupSoundMuted", R.string.VoipGroupSoundMuted));
                        }
                        i4 = R.raw.ic_mute;
                        this.timeLeft = 3000L;
                    } else if (i == 43) {
                        if (ChatObject.isChannelOrGiga((TLRPC$Chat) obj)) {
                            replaceTags = AndroidUtilities.replaceTags(LocaleController.getString("VoipChannelSoundUnmuted", R.string.VoipChannelSoundUnmuted));
                        } else {
                            replaceTags = AndroidUtilities.replaceTags(LocaleController.getString("VoipGroupSoundUnmuted", R.string.VoipGroupSoundUnmuted));
                        }
                        i4 = R.raw.ic_unmute;
                        this.timeLeft = 3000L;
                    } else {
                        int i10 = this.currentAction;
                        if (i10 == 39 || i10 == 100) {
                            if (i10 == 39) {
                                i3 = R.string.VoipGroupAudioRecordStarted;
                                str2 = "VoipGroupAudioRecordStarted";
                            } else {
                                i3 = R.string.VoipGroupVideoRecordStarted;
                                str2 = "VoipGroupVideoRecordStarted";
                            }
                            replaceTags = AndroidUtilities.replaceTags(LocaleController.getString(str2, i3));
                            i4 = R.raw.voip_record_start;
                            this.timeLeft = 3000L;
                        } else if (i10 == 40 || i10 == 101) {
                            if (i10 == 40) {
                                i5 = R.string.VoipGroupAudioRecordSaved;
                                str3 = "VoipGroupAudioRecordSaved";
                            } else {
                                i5 = R.string.VoipGroupVideoRecordSaved;
                                str3 = "VoipGroupVideoRecordSaved";
                            }
                            String string = LocaleController.getString(str3, i5);
                            i4 = R.raw.voip_record_saved;
                            this.timeLeft = 4000L;
                            this.infoTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
                            int indexOf = string.indexOf("**");
                            int lastIndexOf = string.lastIndexOf("**");
                            if (indexOf >= 0 && lastIndexOf >= 0 && indexOf != lastIndexOf) {
                                spannableStringBuilder.replace(lastIndexOf, lastIndexOf + 2, (CharSequence) str12);
                                spannableStringBuilder.replace(indexOf, indexOf + 2, (CharSequence) str12);
                                try {
                                    spannableStringBuilder.setSpan(new URLSpanNoUnderline("tg://openmessage?user_id=" + UserConfig.getInstance(this.currentAccount).getClientUserId()), indexOf, lastIndexOf - 2, 33);
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                            }
                            replaceTags = spannableStringBuilder;
                        } else if (i == 36) {
                            if (obj instanceof TLRPC$User) {
                                str6 = UserObject.getFirstName((TLRPC$User) obj);
                            } else {
                                str6 = ((TLRPC$Chat) obj).title;
                            }
                            replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserCanNowSpeakForYou", R.string.VoipGroupUserCanNowSpeakForYou, str6));
                            i4 = R.raw.voip_unmuted;
                            this.timeLeft = 3000L;
                        } else if (i == 32) {
                            if (obj instanceof TLRPC$User) {
                                str5 = UserObject.getFirstName((TLRPC$User) obj);
                            } else {
                                str5 = ((TLRPC$Chat) obj).title;
                            }
                            replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupRemovedFromGroup", R.string.VoipGroupRemovedFromGroup, str5));
                            i4 = R.raw.voip_group_removed;
                            this.timeLeft = 3000L;
                        } else if (i == 9 || i == 10) {
                            TLRPC$User tLRPC$User4 = (TLRPC$User) obj;
                            replaceTags = i == 9 ? AndroidUtilities.replaceTags(LocaleController.formatString("EditAdminTransferChannelToast", R.string.EditAdminTransferChannelToast, UserObject.getFirstName(tLRPC$User4))) : AndroidUtilities.replaceTags(LocaleController.formatString("EditAdminTransferGroupToast", R.string.EditAdminTransferGroupToast, UserObject.getFirstName(tLRPC$User4)));
                            i4 = R.raw.contact_check;
                        } else if (i == 8) {
                            replaceTags = LocaleController.formatString("NowInContacts", R.string.NowInContacts, UserObject.getFirstName((TLRPC$User) obj));
                            i4 = R.raw.contact_check;
                        } else if (i == 22) {
                            if (!DialogObject.isUserDialog(longValue)) {
                                TLRPC$Chat chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(-longValue));
                                if (!ChatObject.isChannel(chat) || chat.megagroup) {
                                    if (obj == null) {
                                        replaceTags = LocaleController.getString("MainGroupProfilePhotoSetHint", R.string.MainGroupProfilePhotoSetHint);
                                    } else {
                                        replaceTags = LocaleController.getString("MainGroupProfileVideoSetHint", R.string.MainGroupProfileVideoSetHint);
                                    }
                                } else if (obj == null) {
                                    replaceTags = LocaleController.getString("MainChannelProfilePhotoSetHint", R.string.MainChannelProfilePhotoSetHint);
                                } else {
                                    replaceTags = LocaleController.getString("MainChannelProfileVideoSetHint", R.string.MainChannelProfileVideoSetHint);
                                }
                            } else if (obj == null) {
                                replaceTags = LocaleController.getString("MainProfilePhotoSetHint", R.string.MainProfilePhotoSetHint);
                            } else {
                                replaceTags = LocaleController.getString("MainProfileVideoSetHint", R.string.MainProfileVideoSetHint);
                            }
                            i4 = R.raw.contact_check;
                        } else if (i == 23) {
                            replaceTags = LocaleController.getString("ChatWasMovedToMainList", R.string.ChatWasMovedToMainList);
                            i4 = R.raw.contact_check;
                        } else if (i == 6) {
                            replaceTags = LocaleController.getString("ArchiveHidden", R.string.ArchiveHidden);
                            str11 = LocaleController.getString("ArchiveHiddenInfo", R.string.ArchiveHiddenInfo);
                            i4 = R.raw.chats_swipearchive;
                            i9 = 48;
                        } else {
                            if (i10 == 13) {
                                replaceTags = LocaleController.getString("QuizWellDone", R.string.QuizWellDone);
                                str11 = LocaleController.getString("QuizWellDoneInfo", R.string.QuizWellDoneInfo);
                                i4 = R.raw.wallet_congrats;
                            } else if (i10 == 14) {
                                replaceTags = LocaleController.getString("QuizWrongAnswer", R.string.QuizWrongAnswer);
                                str11 = LocaleController.getString("QuizWrongAnswerInfo", R.string.QuizWrongAnswerInfo);
                                i4 = R.raw.wallet_science;
                            } else if (i == 7) {
                                replaceTags = LocaleController.getString("ArchivePinned", R.string.ArchivePinned);
                                if (MessagesController.getInstance(this.currentAccount).dialogFilters.isEmpty()) {
                                    str11 = LocaleController.getString("ArchivePinnedInfo", R.string.ArchivePinnedInfo);
                                }
                                i4 = R.raw.chats_infotip;
                            } else if (i == 20 || i == 21) {
                                MessagesController.DialogFilter dialogFilter = (MessagesController.DialogFilter) obj2;
                                if (longValue != 0) {
                                    if (DialogObject.isEncryptedDialog(longValue)) {
                                        longValue = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(longValue))).user_id;
                                    }
                                    if (DialogObject.isUserDialog(longValue)) {
                                        TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(longValue));
                                        replaceTags2 = i == 20 ? AndroidUtilities.replaceTags(LocaleController.formatString("FilterUserAddedToExisting", R.string.FilterUserAddedToExisting, UserObject.getFirstName(user), dialogFilter.name)) : AndroidUtilities.replaceTags(LocaleController.formatString("FilterUserRemovedFrom", R.string.FilterUserRemovedFrom, UserObject.getFirstName(user), dialogFilter.name));
                                    } else {
                                        TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue));
                                        replaceTags2 = i == 20 ? AndroidUtilities.replaceTags(LocaleController.formatString("FilterChatAddedToExisting", R.string.FilterChatAddedToExisting, chat2.title, dialogFilter.name)) : AndroidUtilities.replaceTags(LocaleController.formatString("FilterChatRemovedFrom", R.string.FilterChatRemovedFrom, chat2.title, dialogFilter.name));
                                    }
                                } else {
                                    replaceTags2 = i == 20 ? AndroidUtilities.replaceTags(LocaleController.formatString("FilterChatsAddedToExisting", R.string.FilterChatsAddedToExisting, LocaleController.formatPluralString("ChatsSelected", ((Integer) obj).intValue(), new Object[0]), dialogFilter.name)) : AndroidUtilities.replaceTags(LocaleController.formatString("FilterChatsRemovedFrom", R.string.FilterChatsRemovedFrom, LocaleController.formatPluralString("ChatsSelected", ((Integer) obj).intValue(), new Object[0]), dialogFilter.name));
                                }
                                SpannableStringBuilder spannableStringBuilder2 = replaceTags2;
                                i4 = i == 20 ? R.raw.folder_in : R.raw.folder_out;
                                replaceTags = spannableStringBuilder2;
                            } else if (i == 19) {
                                replaceTags = this.infoText;
                                i4 = R.raw.chats_infotip;
                            } else if (i == 82) {
                                if (((MediaController.PhotoEntry) obj).isVideo) {
                                    i6 = R.string.AttachMediaVideoDeselected;
                                    str4 = "AttachMediaVideoDeselected";
                                } else {
                                    i6 = R.string.AttachMediaPhotoDeselected;
                                    str4 = "AttachMediaPhotoDeselected";
                                }
                                replaceTags = LocaleController.getString(str4, i6);
                            } else if (i == 78 || i == 79) {
                                int intValue = ((Integer) obj).intValue();
                                if (i == 78) {
                                    replaceTags = LocaleController.formatPluralString("PinnedDialogsCount", intValue, new Object[0]);
                                } else {
                                    replaceTags = LocaleController.formatPluralString("UnpinnedDialogsCount", intValue, new Object[0]);
                                }
                                i4 = this.currentAction == 78 ? R.raw.ic_pin : R.raw.ic_unpin;
                                if (obj2 instanceof Integer) {
                                    this.timeLeft = ((Integer) obj2).intValue();
                                }
                            } else {
                                if (i == 3) {
                                    replaceTags = LocaleController.getString("ChatArchived", R.string.ChatArchived);
                                } else {
                                    replaceTags = LocaleController.getString("ChatsArchived", R.string.ChatsArchived);
                                }
                                if (MessagesController.getInstance(this.currentAccount).dialogFilters.isEmpty()) {
                                    str11 = LocaleController.getString("ChatArchivedInfo", R.string.ChatArchivedInfo);
                                }
                                i4 = R.raw.chats_infotip;
                            }
                            i9 = 44;
                        }
                    }
                    i4 = 0;
                }
                this.infoTextView.setText(replaceTags);
                if (i4 != 0) {
                    this.leftImageView.setAnimation(i4, i9, i9);
                    RLottieDrawable animatedDrawable = this.leftImageView.getAnimatedDrawable();
                    animatedDrawable.setPlayInDirectionOfCustomEndFrame(false);
                    animatedDrawable.setCustomEndFrame(animatedDrawable.getFramesCount());
                    this.leftImageView.setVisibility(0);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                } else {
                    this.leftImageView.setVisibility(8);
                }
                if (str11 != null) {
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                    layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                    ((FrameLayout.LayoutParams) this.subinfoTextView.getLayoutParams()).rightMargin = AndroidUtilities.dp(8.0f);
                    this.subinfoTextView.setText(str11);
                    this.subinfoTextView.setVisibility(0);
                    this.infoTextView.setTextSize(1, 14.0f);
                    this.infoTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                    i7 = 8;
                } else {
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.topMargin = AndroidUtilities.dp(13.0f);
                    layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                    i7 = 8;
                    this.subinfoTextView.setVisibility(8);
                    this.infoTextView.setTextSize(1, 15.0f);
                    this.infoTextView.setTypeface(Typeface.DEFAULT);
                }
                this.undoButton.setVisibility(i7);
            } else {
                int i11 = this.currentAction;
                if (i11 == 45 || i11 == 46 || i11 == 47 || i11 == 51 || i11 == 50 || i11 == 52 || i11 == 53 || i11 == 54 || i11 == 55 || i11 == 56 || i11 == 57 || i11 == 58 || i11 == 59 || i11 == 60 || i11 == 71 || i11 == 70 || i11 == 75 || i11 == 76 || i11 == 41 || i11 == 78 || i11 == 79 || i11 == 61 || i11 == 80) {
                    this.undoImageView.setVisibility(8);
                    this.leftImageView.setVisibility(0);
                    this.infoTextView.setTypeface(Typeface.DEFAULT);
                    long j = -1;
                    int i12 = this.currentAction;
                    if (i12 == 76) {
                        this.infoTextView.setText(LocaleController.getString("BroadcastGroupConvertSuccess", R.string.BroadcastGroupConvertSuccess));
                        this.leftImageView.setAnimation(R.raw.gigagroup_convert, 36, 36);
                        layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                        this.infoTextView.setTextSize(1, 14.0f);
                    } else if (i12 == 75) {
                        this.infoTextView.setText(LocaleController.getString("GigagroupConvertCancelHint", R.string.GigagroupConvertCancelHint));
                        this.leftImageView.setAnimation(R.raw.chats_infotip, 36, 36);
                        layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                        this.infoTextView.setTextSize(1, 14.0f);
                    } else {
                        if (i == 70) {
                            TLRPC$User tLRPC$User5 = (TLRPC$User) obj;
                            int intValue2 = ((Integer) obj2).intValue();
                            this.subinfoTextView.setSingleLine(false);
                            this.infoTextView.setText(LocaleController.formatString("AutoDeleteHintOnText", R.string.AutoDeleteHintOnText, LocaleController.formatTTLString(intValue2)));
                            this.leftImageView.setAnimation(R.raw.fire_on, 36, 36);
                            layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                            this.timeLeft = 4000L;
                            this.leftImageView.setPadding(0, 0, 0, AndroidUtilities.dp(3.0f));
                            z = true;
                        } else {
                            if (i12 == 71) {
                                this.infoTextView.setText(LocaleController.getString("AutoDeleteHintOffText", R.string.AutoDeleteHintOffText));
                                this.leftImageView.setAnimation(R.raw.fire_off, 36, 36);
                                this.infoTextView.setTextSize(1, 14.0f);
                                this.timeLeft = 3000L;
                                this.leftImageView.setPadding(0, 0, 0, AndroidUtilities.dp(4.0f));
                            } else if (i12 == 45) {
                                this.infoTextView.setText(LocaleController.getString("ImportMutualError", R.string.ImportMutualError));
                                this.leftImageView.setAnimation(R.raw.error, 36, 36);
                                layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                                this.infoTextView.setTextSize(1, 14.0f);
                            } else if (i12 == 46) {
                                this.infoTextView.setText(LocaleController.getString("ImportNotAdmin", R.string.ImportNotAdmin));
                                this.leftImageView.setAnimation(R.raw.error, 36, 36);
                                layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                                this.infoTextView.setTextSize(1, 14.0f);
                            } else if (i12 == 47) {
                                this.infoTextView.setText(LocaleController.getString("ImportedInfo", R.string.ImportedInfo));
                                this.leftImageView.setAnimation(R.raw.imported, 36, 36);
                                this.leftImageView.setPadding(0, 0, 0, AndroidUtilities.dp(5.0f));
                                layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                                this.infoTextView.setTextSize(1, 14.0f);
                            } else if (i12 == 51) {
                                this.infoTextView.setText(LocaleController.getString("AudioSpeedNormal", R.string.AudioSpeedNormal));
                                this.leftImageView.setAnimation(R.raw.audio_stop_speed, 36, 36);
                                this.timeLeft = 3000L;
                                this.infoTextView.setTextSize(1, 15.0f);
                            } else if (i12 == 50) {
                                this.infoTextView.setText(LocaleController.getString("AudioSpeedFast", R.string.AudioSpeedFast));
                                this.leftImageView.setAnimation(R.raw.audio_speed, 36, 36);
                                this.timeLeft = 3000L;
                                this.infoTextView.setTextSize(1, 15.0f);
                            } else if (i12 == 52 || i12 == 56 || i12 == 57 || i12 == 58 || i12 == 59 || i12 == 60 || i12 == 80) {
                                if (!AndroidUtilities.shouldShowClipboardToast()) {
                                    return;
                                }
                                int i13 = R.raw.copy;
                                int i14 = this.currentAction;
                                if (i14 == 80) {
                                    this.infoTextView.setText(LocaleController.getString("EmailCopied", R.string.EmailCopied));
                                } else if (i14 == 60) {
                                    this.infoTextView.setText(LocaleController.getString("PhoneCopied", R.string.PhoneCopied));
                                } else if (i14 == 56) {
                                    this.infoTextView.setText(LocaleController.getString("UsernameCopied", R.string.UsernameCopied));
                                } else if (i14 == 57) {
                                    this.infoTextView.setText(LocaleController.getString("HashtagCopied", R.string.HashtagCopied));
                                } else if (i14 == 52) {
                                    this.infoTextView.setText(LocaleController.getString("MessageCopied", R.string.MessageCopied));
                                } else if (i14 == 59) {
                                    i13 = R.raw.voip_invite;
                                    this.infoTextView.setText(LocaleController.getString("LinkCopied", R.string.LinkCopied));
                                } else {
                                    this.infoTextView.setText(LocaleController.getString("TextCopied", R.string.TextCopied));
                                }
                                this.leftImageView.setAnimation(i13, 30, 30);
                                this.timeLeft = 3000L;
                                this.infoTextView.setTextSize(1, 15.0f);
                            } else if (i12 == 54) {
                                this.infoTextView.setText(LocaleController.getString("ChannelNotifyMembersInfoOn", R.string.ChannelNotifyMembersInfoOn));
                                this.leftImageView.setAnimation(R.raw.silent_unmute, 30, 30);
                                this.timeLeft = 3000L;
                                this.infoTextView.setTextSize(1, 15.0f);
                            } else if (i12 == 55) {
                                this.infoTextView.setText(LocaleController.getString("ChannelNotifyMembersInfoOff", R.string.ChannelNotifyMembersInfoOff));
                                this.leftImageView.setAnimation(R.raw.silent_mute, 30, 30);
                                this.timeLeft = 3000L;
                                this.infoTextView.setTextSize(1, 15.0f);
                            } else if (i12 == 41) {
                                if (obj2 == null) {
                                    if (longValue == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("InvLinkToSavedMessages", R.string.InvLinkToSavedMessages)));
                                    } else if (DialogObject.isChatDialog(longValue)) {
                                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToGroup", R.string.InvLinkToGroup, MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue)).title)));
                                    } else {
                                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToUser", R.string.InvLinkToUser, UserObject.getFirstName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(longValue))))));
                                    }
                                } else {
                                    this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToChats", R.string.InvLinkToChats, LocaleController.formatPluralString("Chats", ((Integer) obj2).intValue(), new Object[0]))));
                                }
                                this.leftImageView.setAnimation(R.raw.contact_check, 36, 36);
                                this.timeLeft = 3000L;
                            } else if (i12 == 53) {
                                Integer num = (Integer) obj;
                                if (obj2 == null) {
                                    if (longValue == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                        if (num.intValue() == 1) {
                                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("FwdMessageToSavedMessages", R.string.FwdMessageToSavedMessages)));
                                        } else {
                                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("FwdMessagesToSavedMessages", R.string.FwdMessagesToSavedMessages)));
                                        }
                                        this.leftImageView.setAnimation(R.raw.saved_messages, 30, 30);
                                        this.timeLeft = 3000L;
                                    } else {
                                        if (DialogObject.isChatDialog(longValue)) {
                                            TLRPC$Chat chat3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue));
                                            if (num.intValue() == 1) {
                                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToGroup", R.string.FwdMessageToGroup, chat3.title)));
                                            } else {
                                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToGroup", R.string.FwdMessagesToGroup, chat3.title)));
                                            }
                                        } else {
                                            TLRPC$User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(longValue));
                                            if (num.intValue() == 1) {
                                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToUser", R.string.FwdMessageToUser, UserObject.getFirstName(user2))));
                                            } else {
                                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToUser", R.string.FwdMessagesToUser, UserObject.getFirstName(user2))));
                                            }
                                        }
                                        this.leftImageView.setAnimation(R.raw.forward, 30, 30);
                                    }
                                } else {
                                    int intValue3 = ((Integer) obj2).intValue();
                                    if (num.intValue() == 1) {
                                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToChats", R.string.FwdMessageToChats, LocaleController.formatPluralString("Chats", intValue3, new Object[0]))));
                                    } else {
                                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToChats", R.string.FwdMessagesToChats, LocaleController.formatPluralString("Chats", intValue3, new Object[0]))));
                                    }
                                    this.leftImageView.setAnimation(R.raw.forward, 30, 30);
                                }
                                j = 300;
                                this.timeLeft = 3000L;
                            } else if (i12 == 61) {
                                Integer num2 = (Integer) obj;
                                if (obj2 == null) {
                                    if (longValue == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("BackgroundToSavedMessages", R.string.BackgroundToSavedMessages)));
                                        this.leftImageView.setAnimation(R.raw.saved_messages, 30, 30);
                                    } else {
                                        if (DialogObject.isChatDialog(longValue)) {
                                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("BackgroundToGroup", R.string.BackgroundToGroup, MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue)).title)));
                                        } else {
                                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("BackgroundToUser", R.string.BackgroundToUser, UserObject.getFirstName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(longValue))))));
                                        }
                                        this.leftImageView.setAnimation(R.raw.forward, 30, 30);
                                    }
                                } else {
                                    this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("BackgroundToChats", R.string.BackgroundToChats, LocaleController.formatPluralString("Chats", ((Integer) obj2).intValue(), new Object[0]))));
                                    this.leftImageView.setAnimation(R.raw.forward, 30, 30);
                                }
                                this.timeLeft = 3000L;
                            }
                            z = false;
                        }
                        this.subinfoTextView.setVisibility(8);
                        this.undoTextView.setTextColor(getThemedColor("undo_cancelColor"));
                        this.undoButton.setVisibility(8);
                        layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                        layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                        this.leftImageView.setProgress(0.0f);
                        this.leftImageView.playAnimation();
                        if (j > 0) {
                            this.leftImageView.postDelayed(new Runnable() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda5
                                @Override // java.lang.Runnable
                                public final void run() {
                                    UndoView.this.lambda$showWithAction$7();
                                }
                            }, j);
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append((Object) this.infoTextView.getText());
                        if (this.subinfoTextView.getVisibility() == 0) {
                            str12 = ". " + ((Object) this.subinfoTextView.getText());
                        }
                        sb.append(str12);
                        AndroidUtilities.makeAccessibilityAnnouncement(sb.toString());
                        if (isMultilineSubInfo()) {
                            int measuredWidth = ((ViewGroup) getParent()).getMeasuredWidth();
                            if (measuredWidth == 0) {
                                measuredWidth = AndroidUtilities.displaySize.x;
                            }
                            measureChildWithMargins(this.subinfoTextView, View.MeasureSpec.makeMeasureSpec(measuredWidth - AndroidUtilities.dp(16.0f), 1073741824), 0, View.MeasureSpec.makeMeasureSpec(0, 0), 0);
                            this.undoViewHeight = this.subinfoTextView.getMeasuredHeight() + AndroidUtilities.dp(37.0f);
                        } else if (hasSubInfo()) {
                            this.undoViewHeight = AndroidUtilities.dp(52.0f);
                        } else if (getParent() instanceof ViewGroup) {
                            ViewGroup viewGroup = (ViewGroup) getParent();
                            int measuredWidth2 = (viewGroup.getMeasuredWidth() - viewGroup.getPaddingLeft()) - viewGroup.getPaddingRight();
                            if (measuredWidth2 <= 0) {
                                measuredWidth2 = AndroidUtilities.displaySize.x;
                            }
                            measureChildWithMargins(this.infoTextView, View.MeasureSpec.makeMeasureSpec(measuredWidth2 - AndroidUtilities.dp(16.0f), 1073741824), 0, View.MeasureSpec.makeMeasureSpec(0, 0), 0);
                            int measuredHeight = this.infoTextView.getMeasuredHeight();
                            int i15 = this.currentAction;
                            int dp2 = measuredHeight + AndroidUtilities.dp((i15 == 16 || i15 == 17 || i15 == 18) ? 14.0f : 28.0f);
                            this.undoViewHeight = dp2;
                            int i16 = this.currentAction;
                            if (i16 == 18) {
                                this.undoViewHeight = Math.max(dp2, AndroidUtilities.dp(52.0f));
                            } else if (i16 == 25) {
                                this.undoViewHeight = Math.max(dp2, AndroidUtilities.dp(50.0f));
                            } else if (z) {
                                this.undoViewHeight = dp2 - AndroidUtilities.dp(8.0f);
                            }
                        }
                        if (getVisibility() == 0) {
                            return;
                        }
                        setVisibility(0);
                        setEnterOffset((this.fromTop ? -1.0f : 1.0f) * (this.enterOffsetMargin + this.undoViewHeight));
                        AnimatorSet animatorSet = new AnimatorSet();
                        Animator[] animatorArr = new Animator[1];
                        float[] fArr = new float[2];
                        boolean z2 = this.fromTop;
                        fArr[0] = (z2 ? -1.0f : 1.0f) * (this.enterOffsetMargin + this.undoViewHeight);
                        fArr[1] = z2 ? 1.0f : -1.0f;
                        animatorArr[0] = ObjectAnimator.ofFloat(this, "enterOffset", fArr);
                        animatorSet.playTogether(animatorArr);
                        animatorSet.setInterpolator(new DecelerateInterpolator());
                        animatorSet.setDuration(180L);
                        animatorSet.start();
                        return;
                    }
                    z = true;
                    this.subinfoTextView.setVisibility(8);
                    this.undoTextView.setTextColor(getThemedColor("undo_cancelColor"));
                    this.undoButton.setVisibility(8);
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                    if (j > 0) {
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append((Object) this.infoTextView.getText());
                    if (this.subinfoTextView.getVisibility() == 0) {
                    }
                    sb2.append(str12);
                    AndroidUtilities.makeAccessibilityAnnouncement(sb2.toString());
                    if (isMultilineSubInfo()) {
                    }
                    if (getVisibility() == 0) {
                    }
                } else if (i11 == 24 || i11 == 25) {
                    int intValue4 = ((Integer) obj).intValue();
                    TLRPC$User tLRPC$User6 = (TLRPC$User) obj2;
                    this.undoImageView.setVisibility(8);
                    this.leftImageView.setVisibility(0);
                    if (intValue4 != 0) {
                        this.infoTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                        this.infoTextView.setTextSize(1, 14.0f);
                        this.leftImageView.clearLayerColors();
                        this.leftImageView.setLayerColor("BODY.**", getThemedColor("undo_infoColor"));
                        this.leftImageView.setLayerColor("Wibe Big.**", getThemedColor("undo_infoColor"));
                        this.leftImageView.setLayerColor("Wibe Big 3.**", getThemedColor("undo_infoColor"));
                        this.leftImageView.setLayerColor("Wibe Small.**", getThemedColor("undo_infoColor"));
                        this.infoTextView.setText(LocaleController.getString("ProximityAlertSet", R.string.ProximityAlertSet));
                        this.leftImageView.setAnimation(R.raw.ic_unmute, 28, 28);
                        this.subinfoTextView.setVisibility(0);
                        this.subinfoTextView.setSingleLine(false);
                        this.subinfoTextView.setMaxLines(3);
                        if (tLRPC$User6 != null) {
                            this.subinfoTextView.setText(LocaleController.formatString("ProximityAlertSetInfoUser", R.string.ProximityAlertSetInfoUser, UserObject.getFirstName(tLRPC$User6), LocaleController.formatDistance(intValue4, 2)));
                        } else {
                            this.subinfoTextView.setText(LocaleController.formatString("ProximityAlertSetInfoGroup2", R.string.ProximityAlertSetInfoGroup2, LocaleController.formatDistance(intValue4, 2)));
                        }
                        this.undoButton.setVisibility(8);
                        layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                    } else {
                        this.infoTextView.setTypeface(Typeface.DEFAULT);
                        this.infoTextView.setTextSize(1, 15.0f);
                        this.leftImageView.clearLayerColors();
                        this.leftImageView.setLayerColor("Body Main.**", getThemedColor("undo_infoColor"));
                        this.leftImageView.setLayerColor("Body Top.**", getThemedColor("undo_infoColor"));
                        this.leftImageView.setLayerColor("Line.**", getThemedColor("undo_infoColor"));
                        this.leftImageView.setLayerColor("Curve Big.**", getThemedColor("undo_infoColor"));
                        this.leftImageView.setLayerColor("Curve Small.**", getThemedColor("undo_infoColor"));
                        layoutParams.topMargin = AndroidUtilities.dp(14.0f);
                        this.infoTextView.setText(LocaleController.getString("ProximityAlertCancelled", R.string.ProximityAlertCancelled));
                        this.leftImageView.setAnimation(R.raw.ic_mute, 28, 28);
                        this.subinfoTextView.setVisibility(8);
                        this.undoTextView.setTextColor(getThemedColor("undo_cancelColor"));
                        this.undoButton.setVisibility(0);
                    }
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                } else if (i11 == 11) {
                    this.infoTextView.setText(LocaleController.getString("AuthAnotherClientOk", R.string.AuthAnotherClientOk));
                    this.leftImageView.setAnimation(R.raw.contact_check, 36, 36);
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                    this.subinfoTextView.setText(((TLRPC$TL_authorization) obj).app_name);
                    this.subinfoTextView.setVisibility(0);
                    this.infoTextView.setTextSize(1, 14.0f);
                    this.infoTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                    this.undoTextView.setTextColor(getThemedColor("windowBackgroundWhiteRedText2"));
                    this.undoImageView.setVisibility(8);
                    this.undoButton.setVisibility(0);
                    this.leftImageView.setVisibility(0);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                } else if (i11 == 15) {
                    this.timeLeft = 10000L;
                    this.undoTextView.setText(LocaleController.getString("Open", R.string.Open).toUpperCase());
                    this.infoTextView.setText(LocaleController.getString("FilterAvailableTitle", R.string.FilterAvailableTitle));
                    this.leftImageView.setAnimation(R.raw.filter_new, 36, 36);
                    int ceil = ((int) Math.ceil(this.undoTextView.getPaint().measureText(this.undoTextView.getText().toString()))) + AndroidUtilities.dp(26.0f);
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.rightMargin = ceil;
                    layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                    ((FrameLayout.LayoutParams) this.subinfoTextView.getLayoutParams()).rightMargin = ceil;
                    String string2 = LocaleController.getString("FilterAvailableText", R.string.FilterAvailableText);
                    SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(string2);
                    int indexOf2 = string2.indexOf(42);
                    int lastIndexOf2 = string2.lastIndexOf(42);
                    if (indexOf2 >= 0 && lastIndexOf2 >= 0 && indexOf2 != lastIndexOf2) {
                        spannableStringBuilder3.replace(lastIndexOf2, lastIndexOf2 + 1, (CharSequence) str12);
                        spannableStringBuilder3.replace(indexOf2, indexOf2 + 1, (CharSequence) str12);
                        spannableStringBuilder3.setSpan(new URLSpanNoUnderline("tg://settings/folders"), indexOf2, lastIndexOf2 - 1, 33);
                    }
                    this.subinfoTextView.setText(spannableStringBuilder3);
                    this.subinfoTextView.setVisibility(0);
                    this.subinfoTextView.setSingleLine(false);
                    this.subinfoTextView.setMaxLines(2);
                    this.undoButton.setVisibility(0);
                    this.undoImageView.setVisibility(8);
                    this.leftImageView.setVisibility(0);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                } else if (i11 == 16 || i11 == 17) {
                    this.timeLeft = 4000L;
                    this.infoTextView.setTextSize(1, 14.0f);
                    this.infoTextView.setGravity(16);
                    this.infoTextView.setMinHeight(AndroidUtilities.dp(30.0f));
                    String str13 = (String) obj;
                    if ("🎲".equals(str13)) {
                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("DiceInfo2", R.string.DiceInfo2)));
                        this.leftImageView.setImageResource(R.drawable.dice);
                    } else {
                        if ("🎯".equals(str13)) {
                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("DartInfo", R.string.DartInfo)));
                        } else {
                            String serverString = LocaleController.getServerString("DiceEmojiInfo_" + str13);
                            if (!TextUtils.isEmpty(serverString)) {
                                TextView textView = this.infoTextView;
                                textView.setText(Emoji.replaceEmoji(serverString, textView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                            } else {
                                f = 14.0f;
                                this.infoTextView.setText(Emoji.replaceEmoji(LocaleController.formatString("DiceEmojiInfo", R.string.DiceEmojiInfo, str13), this.infoTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                                this.leftImageView.setImageDrawable(Emoji.getEmojiDrawable(str13));
                                this.leftImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                layoutParams.topMargin = AndroidUtilities.dp(f);
                                layoutParams.bottomMargin = AndroidUtilities.dp(f);
                                layoutParams2.leftMargin = AndroidUtilities.dp(f);
                                layoutParams2.width = AndroidUtilities.dp(26.0f);
                                layoutParams2.height = AndroidUtilities.dp(26.0f);
                            }
                        }
                        f = 14.0f;
                        this.leftImageView.setImageDrawable(Emoji.getEmojiDrawable(str13));
                        this.leftImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        layoutParams.topMargin = AndroidUtilities.dp(f);
                        layoutParams.bottomMargin = AndroidUtilities.dp(f);
                        layoutParams2.leftMargin = AndroidUtilities.dp(f);
                        layoutParams2.width = AndroidUtilities.dp(26.0f);
                        layoutParams2.height = AndroidUtilities.dp(26.0f);
                    }
                    this.undoTextView.setText(LocaleController.getString("SendDice", R.string.SendDice));
                    if (this.currentAction == 16) {
                        dp = ((int) Math.ceil(this.undoTextView.getPaint().measureText(this.undoTextView.getText().toString()))) + AndroidUtilities.dp(26.0f);
                        this.undoTextView.setVisibility(0);
                        this.undoTextView.setTextColor(getThemedColor("undo_cancelColor"));
                        this.undoImageView.setVisibility(8);
                        this.undoButton.setVisibility(0);
                    } else {
                        dp = AndroidUtilities.dp(8.0f);
                        this.undoTextView.setVisibility(8);
                        this.undoButton.setVisibility(8);
                    }
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.rightMargin = dp;
                    layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                    layoutParams.bottomMargin = AndroidUtilities.dp(7.0f);
                    layoutParams.height = -1;
                    this.subinfoTextView.setVisibility(8);
                    this.leftImageView.setVisibility(0);
                } else if (i11 == 18) {
                    this.timeLeft = Math.max(4000, Math.min((charSequence.length() / 50) * 1600, 10000));
                    this.infoTextView.setTextSize(1, 14.0f);
                    this.infoTextView.setGravity(16);
                    this.infoTextView.setText((CharSequence) obj);
                    this.undoTextView.setVisibility(8);
                    this.undoButton.setVisibility(8);
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                    layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                    layoutParams.bottomMargin = AndroidUtilities.dp(7.0f);
                    layoutParams.height = -1;
                    layoutParams2.gravity = 51;
                    int dp3 = AndroidUtilities.dp(8.0f);
                    layoutParams2.bottomMargin = dp3;
                    layoutParams2.topMargin = dp3;
                    this.leftImageView.setVisibility(0);
                    this.leftImageView.setAnimation(R.raw.chats_infotip, 36, 36);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                    this.infoTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                } else if (i11 == 12) {
                    this.infoTextView.setText(LocaleController.getString("ColorThemeChanged", R.string.ColorThemeChanged));
                    this.leftImageView.setImageResource(R.drawable.toast_pallete);
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.rightMargin = AndroidUtilities.dp(48.0f);
                    layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                    ((FrameLayout.LayoutParams) this.subinfoTextView.getLayoutParams()).rightMargin = AndroidUtilities.dp(48.0f);
                    String string3 = LocaleController.getString("ColorThemeChangedInfo", R.string.ColorThemeChangedInfo);
                    SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder(string3);
                    int indexOf3 = string3.indexOf(42);
                    int lastIndexOf3 = string3.lastIndexOf(42);
                    if (indexOf3 >= 0 && lastIndexOf3 >= 0 && indexOf3 != lastIndexOf3) {
                        spannableStringBuilder4.replace(lastIndexOf3, lastIndexOf3 + 1, (CharSequence) str12);
                        spannableStringBuilder4.replace(indexOf3, indexOf3 + 1, (CharSequence) str12);
                        spannableStringBuilder4.setSpan(new URLSpanNoUnderline("tg://settings/themes"), indexOf3, lastIndexOf3 - 1, 33);
                    }
                    this.subinfoTextView.setText(spannableStringBuilder4);
                    this.subinfoTextView.setVisibility(0);
                    this.subinfoTextView.setSingleLine(false);
                    this.subinfoTextView.setMaxLines(2);
                    this.undoTextView.setVisibility(8);
                    this.undoButton.setVisibility(0);
                    this.leftImageView.setVisibility(0);
                } else if (i11 == 2 || i11 == 4) {
                    if (i == 2) {
                        this.infoTextView.setText(LocaleController.getString("ChatArchived", R.string.ChatArchived));
                    } else {
                        this.infoTextView.setText(LocaleController.getString("ChatsArchived", R.string.ChatsArchived));
                    }
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.topMargin = AndroidUtilities.dp(13.0f);
                    layoutParams.rightMargin = 0;
                    this.infoTextView.setTextSize(1, 15.0f);
                    this.undoButton.setVisibility(0);
                    this.infoTextView.setTypeface(Typeface.DEFAULT);
                    this.subinfoTextView.setVisibility(8);
                    this.leftImageView.setVisibility(0);
                    this.leftImageView.setAnimation(R.raw.chats_archived, 36, 36);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                } else if (i == 82) {
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) obj;
                    TextView textView2 = this.infoTextView;
                    if (photoEntry.isVideo) {
                        i2 = R.string.AttachMediaVideoDeselected;
                        str = "AttachMediaVideoDeselected";
                    } else {
                        i2 = R.string.AttachMediaPhotoDeselected;
                        str = "AttachMediaPhotoDeselected";
                    }
                    textView2.setText(LocaleController.getString(str, i2));
                    this.undoButton.setVisibility(0);
                    this.infoTextView.setTextSize(1, 15.0f);
                    this.infoTextView.setTypeface(Typeface.DEFAULT);
                    this.subinfoTextView.setVisibility(8);
                    this.avatarImageView.setVisibility(0);
                    this.avatarImageView.setRoundRadius(AndroidUtilities.dp(2.0f));
                    String str14 = photoEntry.thumbPath;
                    if (str14 != null) {
                        this.avatarImageView.setImage(str14, null, Theme.chat_attachEmptyDrawable);
                    } else if (photoEntry.path != null) {
                        this.avatarImageView.setOrientation(photoEntry.orientation, true);
                        if (photoEntry.isVideo) {
                            this.avatarImageView.setImage("vthumb://" + photoEntry.imageId + ":" + photoEntry.path, null, Theme.chat_attachEmptyDrawable);
                        } else {
                            this.avatarImageView.setImage("thumb://" + photoEntry.imageId + ":" + photoEntry.path, null, Theme.chat_attachEmptyDrawable);
                        }
                    } else {
                        this.avatarImageView.setImageDrawable(Theme.chat_attachEmptyDrawable);
                    }
                } else {
                    layoutParams.leftMargin = AndroidUtilities.dp(45.0f);
                    layoutParams.topMargin = AndroidUtilities.dp(13.0f);
                    layoutParams.rightMargin = 0;
                    this.infoTextView.setTextSize(1, 15.0f);
                    this.undoButton.setVisibility(0);
                    this.infoTextView.setTypeface(Typeface.DEFAULT);
                    this.subinfoTextView.setVisibility(8);
                    this.leftImageView.setVisibility(8);
                    int i17 = this.currentAction;
                    if (i17 == 81 || i17 == 0 || i17 == 26) {
                        this.infoTextView.setText(LocaleController.getString("HistoryClearedUndo", R.string.HistoryClearedUndo));
                    } else if (i17 == 27) {
                        this.infoTextView.setText(LocaleController.getString("ChatsDeletedUndo", R.string.ChatsDeletedUndo));
                    } else if (DialogObject.isChatDialog(longValue)) {
                        TLRPC$Chat chat4 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue));
                        if (ChatObject.isChannel(chat4) && !chat4.megagroup) {
                            this.infoTextView.setText(LocaleController.getString("ChannelDeletedUndo", R.string.ChannelDeletedUndo));
                        } else {
                            this.infoTextView.setText(LocaleController.getString("GroupDeletedUndo", R.string.GroupDeletedUndo));
                        }
                    } else {
                        this.infoTextView.setText(LocaleController.getString("ChatDeletedUndo", R.string.ChatDeletedUndo));
                    }
                    if (this.currentAction != 81) {
                        for (int i18 = 0; i18 < arrayList.size(); i18++) {
                            MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                            long longValue2 = arrayList.get(i18).longValue();
                            int i19 = this.currentAction;
                            messagesController.addDialogAction(longValue2, i19 == 0 || i19 == 26);
                        }
                    }
                }
            }
            z = false;
            StringBuilder sb22 = new StringBuilder();
            sb22.append((Object) this.infoTextView.getText());
            if (this.subinfoTextView.getVisibility() == 0) {
            }
            sb22.append(str12);
            AndroidUtilities.makeAccessibilityAnnouncement(sb22.toString());
            if (isMultilineSubInfo()) {
            }
            if (getVisibility() == 0) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showWithAction$2(View view) {
        hide(false, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showWithAction$6(TLRPC$Message tLRPC$Message, View view) {
        hide(true, 1);
        TLRPC$TL_payments_getPaymentReceipt tLRPC$TL_payments_getPaymentReceipt = new TLRPC$TL_payments_getPaymentReceipt();
        tLRPC$TL_payments_getPaymentReceipt.msg_id = tLRPC$Message.id;
        tLRPC$TL_payments_getPaymentReceipt.peer = this.parentFragment.getMessagesController().getInputPeer(tLRPC$Message.peer_id);
        this.parentFragment.getConnectionsManager().sendRequest(tLRPC$TL_payments_getPaymentReceipt, new RequestDelegate() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda7
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                UndoView.this.lambda$showWithAction$5(tLObject, tLRPC$TL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showWithAction$5(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                UndoView.this.lambda$showWithAction$4(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showWithAction$4(TLObject tLObject) {
        if (tLObject instanceof TLRPC$TL_payments_paymentReceipt) {
            this.parentFragment.presentFragment(new PaymentFormActivity((TLRPC$TL_payments_paymentReceipt) tLObject));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showWithAction$7() {
        this.leftImageView.performHapticFeedback(3, 2);
    }

    public void setEnterOffsetMargin(int i) {
        this.enterOffsetMargin = i;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(this.undoViewHeight, 1073741824));
        this.backgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.additionalTranslationY != 0.0f) {
            canvas.save();
            float measuredHeight = (getMeasuredHeight() - this.enterOffset) + AndroidUtilities.dp(9.0f);
            if (measuredHeight > 0.0f) {
                canvas.clipRect(0.0f, 0.0f, getMeasuredWidth(), measuredHeight);
                super.dispatchDraw(canvas);
            }
            canvas.restore();
            return;
        }
        super.dispatchDraw(canvas);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.additionalTranslationY != 0.0f) {
            canvas.save();
            float measuredHeight = (getMeasuredHeight() - this.enterOffset) + this.enterOffsetMargin + AndroidUtilities.dp(1.0f);
            if (measuredHeight > 0.0f) {
                canvas.clipRect(0.0f, 0.0f, getMeasuredWidth(), measuredHeight);
                super.dispatchDraw(canvas);
            }
            this.backgroundDrawable.draw(canvas);
            canvas.restore();
        } else {
            this.backgroundDrawable.draw(canvas);
        }
        int i = this.currentAction;
        if (i == 1 || i == 0 || i == 27 || i == 26 || i == 81) {
            long j = this.timeLeft;
            int ceil = j > 0 ? (int) Math.ceil(((float) j) / 1000.0f) : 0;
            if (this.prevSeconds != ceil) {
                this.prevSeconds = ceil;
                String format = String.format("%d", Integer.valueOf(Math.max(1, ceil)));
                this.timeLeftString = format;
                StaticLayout staticLayout = this.timeLayout;
                if (staticLayout != null) {
                    this.timeLayoutOut = staticLayout;
                    this.timeReplaceProgress = 0.0f;
                }
                this.textWidth = (int) Math.ceil(this.textPaint.measureText(format));
                this.timeLayout = new StaticLayout(this.timeLeftString, this.textPaint, ConnectionsManager.DEFAULT_DATACENTER_ID, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
            float f = this.timeReplaceProgress;
            if (f < 1.0f) {
                float f2 = f + 0.10666667f;
                this.timeReplaceProgress = f2;
                if (f2 > 1.0f) {
                    this.timeReplaceProgress = 1.0f;
                } else {
                    invalidate();
                }
            }
            int alpha = this.textPaint.getAlpha();
            if (this.timeLayoutOut != null) {
                float f3 = this.timeReplaceProgress;
                if (f3 < 1.0f) {
                    this.textPaint.setAlpha((int) (alpha * (1.0f - f3)));
                    canvas.save();
                    canvas.translate(this.rect.centerX() - (this.textWidth / 2), AndroidUtilities.dp(17.2f) + (AndroidUtilities.dp(10.0f) * this.timeReplaceProgress));
                    this.timeLayoutOut.draw(canvas);
                    this.textPaint.setAlpha(alpha);
                    canvas.restore();
                }
            }
            if (this.timeLayout != null) {
                float f4 = this.timeReplaceProgress;
                if (f4 != 1.0f) {
                    this.textPaint.setAlpha((int) (alpha * f4));
                }
                canvas.save();
                canvas.translate(this.rect.centerX() - (this.textWidth / 2), AndroidUtilities.dp(17.2f) - (AndroidUtilities.dp(10.0f) * (1.0f - this.timeReplaceProgress)));
                this.timeLayout.draw(canvas);
                if (this.timeReplaceProgress != 1.0f) {
                    this.textPaint.setAlpha(alpha);
                }
                canvas.restore();
            }
            canvas.drawArc(this.rect, -90.0f, (((float) this.timeLeft) / 5000.0f) * (-360.0f), false, this.progressPaint);
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j2 = this.timeLeft - (elapsedRealtime - this.lastUpdateTime);
        this.timeLeft = j2;
        this.lastUpdateTime = elapsedRealtime;
        if (j2 <= 0) {
            hide(true, this.hideAnimationType);
        }
        if (this.currentAction != 82) {
            invalidate();
        }
    }

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
        this.infoTextView.invalidate();
        this.leftImageView.invalidate();
    }

    public void setInfoText(CharSequence charSequence) {
        this.infoText = charSequence;
    }

    public void setHideAnimationType(int i) {
        this.hideAnimationType = i;
    }

    @Keep
    public float getEnterOffset() {
        return this.enterOffset;
    }

    @Keep
    public void setEnterOffset(float f) {
        if (this.enterOffset != f) {
            this.enterOffset = f;
            updatePosition();
        }
    }

    private void updatePosition() {
        setTranslationY(((this.enterOffset - this.enterOffsetMargin) + AndroidUtilities.dp(8.0f)) - this.additionalTranslationY);
        invalidate();
    }

    @Override // android.view.View
    public Drawable getBackground() {
        return this.backgroundDrawable;
    }

    private int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }
}