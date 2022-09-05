package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.HintView;
/* loaded from: classes3.dex */
public class HintView extends FrameLayout {
    private AnimatorSet animatorSet;
    private ImageView arrowImageView;
    private int bottomOffset;
    private int currentType;
    private View currentView;
    private float extraTranslationY;
    private Runnable hideRunnable;
    private ImageView imageView;
    private boolean isTopArrow;
    private ChatMessageCell messageCell;
    private String overrideText;
    private final Theme.ResourcesProvider resourcesProvider;
    private long showingDuration;
    private int shownY;
    private TextView textView;
    private float translationY;

    public HintView(Context context, int i) {
        this(context, i, false, null);
    }

    public HintView(Context context, int i, boolean z) {
        this(context, i, z, null);
    }

    public HintView(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
        this(context, i, false, resourcesProvider);
    }

    public HintView(Context context, int i, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.showingDuration = 2000L;
        this.resourcesProvider = resourcesProvider;
        this.currentType = i;
        this.isTopArrow = z;
        CorrectlyMeasuringTextView correctlyMeasuringTextView = new CorrectlyMeasuringTextView(context);
        this.textView = correctlyMeasuringTextView;
        correctlyMeasuringTextView.setTextColor(getThemedColor("chat_gifSaveHintText"));
        this.textView.setTextSize(1, 14.0f);
        this.textView.setMaxLines(2);
        if (i == 7 || i == 8 || i == 9) {
            this.textView.setMaxWidth(AndroidUtilities.dp(310.0f));
        } else if (i == 4) {
            this.textView.setMaxWidth(AndroidUtilities.dp(280.0f));
        } else {
            this.textView.setMaxWidth(AndroidUtilities.dp(250.0f));
        }
        if (this.currentType == 3) {
            this.textView.setGravity(19);
            this.textView.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(5.0f), getThemedColor("chat_gifSaveHintBackground")));
            this.textView.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
            addView(this.textView, LayoutHelper.createFrame(-2, 30.0f, 51, 0.0f, z ? 6.0f : 0.0f, 0.0f, z ? 0.0f : 6.0f));
        } else {
            this.textView.setGravity(51);
            this.textView.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), getThemedColor("chat_gifSaveHintBackground")));
            this.textView.setPadding(AndroidUtilities.dp(this.currentType == 0 ? 54.0f : 8.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
            addView(this.textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, z ? 6.0f : 0.0f, 0.0f, z ? 0.0f : 6.0f));
        }
        if (i == 0) {
            this.textView.setText(LocaleController.getString("AutoplayVideoInfo", R.string.AutoplayVideoInfo));
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            imageView.setImageResource(R.drawable.tooltip_sound);
            this.imageView.setScaleType(ImageView.ScaleType.CENTER);
            this.imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor("chat_gifSaveHintText"), PorterDuff.Mode.MULTIPLY));
            addView(this.imageView, LayoutHelper.createFrame(38, 34.0f, 51, 7.0f, 7.0f, 0.0f, 0.0f));
        }
        ImageView imageView2 = new ImageView(context);
        this.arrowImageView = imageView2;
        imageView2.setImageResource(z ? R.drawable.tooltip_arrow_up : R.drawable.tooltip_arrow);
        this.arrowImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor("chat_gifSaveHintBackground"), PorterDuff.Mode.MULTIPLY));
        addView(this.arrowImageView, LayoutHelper.createFrame(14, 6.0f, (z ? 48 : 80) | 3, 0.0f, 0.0f, 0.0f, 0.0f));
    }

    public void setBackgroundColor(int i, int i2) {
        this.textView.setTextColor(i2);
        this.arrowImageView.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
        TextView textView = this.textView;
        int i3 = this.currentType;
        textView.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp((i3 == 7 || i3 == 8) ? 6.0f : 3.0f), i));
    }

    public void setOverrideText(String str) {
        this.overrideText = str;
        this.textView.setText(str);
        ChatMessageCell chatMessageCell = this.messageCell;
        if (chatMessageCell != null) {
            this.messageCell = null;
            showForMessageCell(chatMessageCell, false);
        }
    }

    public void setExtraTranslationY(float f) {
        this.extraTranslationY = f;
        setTranslationY(f + this.translationY);
    }

    public float getBaseTranslationY() {
        return this.translationY;
    }

    public boolean showForMessageCell(ChatMessageCell chatMessageCell, boolean z) {
        return showForMessageCell(chatMessageCell, null, 0, 0, z);
    }

    public boolean showForMessageCell(ChatMessageCell chatMessageCell, Object obj, int i, int i2, boolean z) {
        int dp;
        int forwardNameCenterX;
        int i3 = this.currentType;
        if (!(i3 == 5 && i2 == this.shownY && this.messageCell == chatMessageCell) && (i3 == 5 || ((i3 != 0 || getTag() == null) && this.messageCell != chatMessageCell))) {
            Runnable runnable = this.hideRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.hideRunnable = null;
            }
            int[] iArr = new int[2];
            chatMessageCell.getLocationInWindow(iArr);
            int i4 = iArr[1];
            ((View) getParent()).getLocationInWindow(iArr);
            int i5 = i4 - iArr[1];
            View view = (View) chatMessageCell.getParent();
            int i6 = this.currentType;
            if (i6 == 0) {
                ImageReceiver photoImage = chatMessageCell.getPhotoImage();
                i5 = (int) (i5 + photoImage.getImageY());
                int imageHeight = (int) photoImage.getImageHeight();
                int i7 = i5 + imageHeight;
                int measuredHeight = view.getMeasuredHeight();
                if (i5 <= getMeasuredHeight() + AndroidUtilities.dp(10.0f) || i7 > measuredHeight + (imageHeight / 4)) {
                    return false;
                }
                forwardNameCenterX = chatMessageCell.getNoSoundIconCenterX();
            } else if (i6 == 5) {
                Integer num = (Integer) obj;
                i5 += i2;
                this.shownY = i2;
                if (num.intValue() == -1) {
                    this.textView.setText(LocaleController.getString("PollSelectOption", R.string.PollSelectOption));
                } else if (chatMessageCell.getMessageObject().isQuiz()) {
                    if (num.intValue() == 0) {
                        this.textView.setText(LocaleController.getString("NoVotesQuiz", R.string.NoVotesQuiz));
                    } else {
                        this.textView.setText(LocaleController.formatPluralString("Answer", num.intValue(), new Object[0]));
                    }
                } else if (num.intValue() == 0) {
                    this.textView.setText(LocaleController.getString("NoVotes", R.string.NoVotes));
                } else {
                    this.textView.setText(LocaleController.formatPluralString("Vote", num.intValue(), new Object[0]));
                }
                measure(View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE));
                forwardNameCenterX = i;
            } else {
                MessageObject messageObject = chatMessageCell.getMessageObject();
                String str = this.overrideText;
                if (str == null) {
                    this.textView.setText(LocaleController.getString("HidAccount", R.string.HidAccount));
                } else {
                    this.textView.setText(str);
                }
                measure(View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE));
                TLRPC$User currentUser = chatMessageCell.getCurrentUser();
                if (currentUser != null && currentUser.id == 0) {
                    dp = (chatMessageCell.getMeasuredHeight() - Math.max(0, chatMessageCell.getBottom() - view.getMeasuredHeight())) - AndroidUtilities.dp(50.0f);
                } else {
                    i5 += AndroidUtilities.dp(22.0f);
                    if (!messageObject.isOutOwner() && chatMessageCell.isDrawNameLayout()) {
                        dp = AndroidUtilities.dp(20.0f);
                    }
                    if (this.isTopArrow && i5 <= getMeasuredHeight() + AndroidUtilities.dp(10.0f)) {
                        return false;
                    }
                    forwardNameCenterX = chatMessageCell.getForwardNameCenterX();
                }
                i5 += dp;
                if (this.isTopArrow) {
                }
                forwardNameCenterX = chatMessageCell.getForwardNameCenterX();
            }
            int measuredWidth = view.getMeasuredWidth();
            if (this.isTopArrow) {
                float f = this.extraTranslationY;
                float dp2 = AndroidUtilities.dp(44.0f);
                this.translationY = dp2;
                setTranslationY(f + dp2);
            } else {
                float f2 = this.extraTranslationY;
                float measuredHeight2 = i5 - getMeasuredHeight();
                this.translationY = measuredHeight2;
                setTranslationY(f2 + measuredHeight2);
            }
            int left = chatMessageCell.getLeft() + forwardNameCenterX;
            int dp3 = AndroidUtilities.dp(19.0f);
            if (this.currentType == 5) {
                int max = Math.max(0, (forwardNameCenterX - (getMeasuredWidth() / 2)) - AndroidUtilities.dp(19.1f));
                setTranslationX(max);
                dp3 += max;
            } else if (left > view.getMeasuredWidth() / 2) {
                int measuredWidth2 = (measuredWidth - getMeasuredWidth()) - AndroidUtilities.dp(38.0f);
                setTranslationX(measuredWidth2);
                dp3 += measuredWidth2;
            } else {
                setTranslationX(0.0f);
            }
            float left2 = ((chatMessageCell.getLeft() + forwardNameCenterX) - dp3) - (this.arrowImageView.getMeasuredWidth() / 2);
            this.arrowImageView.setTranslationX(left2);
            if (left > view.getMeasuredWidth() / 2) {
                if (left2 < AndroidUtilities.dp(10.0f)) {
                    float dp4 = left2 - AndroidUtilities.dp(10.0f);
                    setTranslationX(getTranslationX() + dp4);
                    this.arrowImageView.setTranslationX(left2 - dp4);
                }
            } else if (left2 > getMeasuredWidth() - AndroidUtilities.dp(24.0f)) {
                float measuredWidth3 = (left2 - getMeasuredWidth()) + AndroidUtilities.dp(24.0f);
                setTranslationX(measuredWidth3);
                this.arrowImageView.setTranslationX(left2 - measuredWidth3);
            } else if (left2 < AndroidUtilities.dp(10.0f)) {
                float dp5 = left2 - AndroidUtilities.dp(10.0f);
                setTranslationX(getTranslationX() + dp5);
                this.arrowImageView.setTranslationX(left2 - dp5);
            }
            this.messageCell = chatMessageCell;
            AnimatorSet animatorSet = this.animatorSet;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.animatorSet = null;
            }
            setTag(1);
            setVisibility(0);
            if (z) {
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.animatorSet = animatorSet2;
                animatorSet2.playTogether(ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f, 1.0f));
                this.animatorSet.addListener(new 1());
                this.animatorSet.setDuration(300L);
                this.animatorSet.start();
            } else {
                setAlpha(1.0f);
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 1 extends AnimatorListenerAdapter {
        1() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            HintView.this.animatorSet = null;
            AndroidUtilities.runOnUIThread(HintView.this.hideRunnable = new Runnable() { // from class: org.telegram.ui.Components.HintView$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    HintView.1.this.lambda$onAnimationEnd$0();
                }
            }, HintView.this.currentType == 0 ? 10000L : 2000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0() {
            HintView.this.hide();
        }
    }

    public boolean showForView(View view, boolean z) {
        if (this.currentView == view || getTag() != null) {
            if (getTag() != null) {
                updatePosition(view);
            }
            return false;
        }
        Runnable runnable = this.hideRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.hideRunnable = null;
        }
        updatePosition(view);
        this.currentView = view;
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animatorSet = null;
        }
        setTag(1);
        setVisibility(0);
        if (z) {
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animatorSet = animatorSet2;
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f, 1.0f));
            this.animatorSet.addListener(new 2());
            this.animatorSet.setDuration(300L);
            this.animatorSet.start();
        } else {
            setAlpha(1.0f);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 2 extends AnimatorListenerAdapter {
        2() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            HintView.this.animatorSet = null;
            AndroidUtilities.runOnUIThread(HintView.this.hideRunnable = new Runnable() { // from class: org.telegram.ui.Components.HintView$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    HintView.2.this.lambda$onAnimationEnd$0();
                }
            }, HintView.this.showingDuration);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0() {
            HintView.this.hide();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:55:0x0140, code lost:
        if (r1 < 0) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0161, code lost:
        r11 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x015e, code lost:
        if (r1 >= 0) goto L56;
     */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0176  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x018a  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01a9  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0130  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x014c  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0115  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00b3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updatePosition(View view) {
        int measuredHeight;
        int dp;
        int i;
        int i2;
        int measuredWidth;
        View view2;
        int i3;
        int i4;
        int i5;
        int measuredWidth2;
        int i6;
        measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x, Integer.MIN_VALUE));
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        int dp2 = iArr[1] - AndroidUtilities.dp(4.0f);
        int i7 = this.currentType;
        if (i7 == 4) {
            i = AndroidUtilities.dp(4.0f);
        } else {
            if (i7 == 6) {
                measuredHeight = view.getMeasuredHeight() + getMeasuredHeight();
                dp = AndroidUtilities.dp(10.0f);
            } else if (i7 == 7 || (i7 == 8 && this.isTopArrow)) {
                measuredHeight = view.getMeasuredHeight() + getMeasuredHeight();
                dp = AndroidUtilities.dp(8.0f);
            } else {
                if (i7 == 8) {
                    dp2 -= AndroidUtilities.dp(10.0f);
                }
                i2 = this.currentType;
                int i8 = 0;
                if (i2 == 8 || !this.isTopArrow) {
                    if (i2 != 3) {
                        measuredWidth = iArr[0];
                    } else {
                        measuredWidth = iArr[0] + (view.getMeasuredWidth() / 2);
                    }
                } else if (view instanceof SimpleTextView) {
                    SimpleTextView simpleTextView = (SimpleTextView) view;
                    Drawable rightDrawable = simpleTextView.getRightDrawable();
                    measuredWidth = (iArr[0] + (rightDrawable != null ? rightDrawable.getBounds().centerX() : simpleTextView.getTextWidth() / 2)) - AndroidUtilities.dp(8.0f);
                } else if (view instanceof TextView) {
                    measuredWidth = (iArr[0] + ((TextView) view).getMeasuredWidth()) - AndroidUtilities.dp(16.5f);
                } else {
                    measuredWidth = iArr[0];
                }
                view2 = (View) getParent();
                view2.getLocationInWindow(iArr);
                i3 = measuredWidth - iArr[0];
                int i9 = (dp2 - iArr[1]) - this.bottomOffset;
                int measuredWidth3 = view2.getMeasuredWidth();
                if (!this.isTopArrow && (i6 = this.currentType) != 6 && i6 != 7 && i6 != 8) {
                    float f = this.extraTranslationY;
                    float dp3 = AndroidUtilities.dp(44.0f);
                    this.translationY = dp3;
                    setTranslationY(f + dp3);
                } else {
                    float f2 = this.extraTranslationY;
                    float measuredHeight2 = i9 - getMeasuredHeight();
                    this.translationY = measuredHeight2;
                    setTranslationY(f2 + measuredHeight2);
                }
                if (!(getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
                    i4 = ((ViewGroup.MarginLayoutParams) getLayoutParams()).leftMargin;
                    i5 = ((ViewGroup.MarginLayoutParams) getLayoutParams()).rightMargin;
                } else {
                    i4 = 0;
                    i5 = 0;
                }
                if (this.currentType != 8 && !this.isTopArrow) {
                    i8 = (((measuredWidth3 - i4) - i5) - getMeasuredWidth()) / 2;
                } else if (i3 <= view2.getMeasuredWidth() / 2) {
                    if (this.currentType == 3) {
                        measuredWidth2 = (int) (measuredWidth3 - (getMeasuredWidth() * 1.5f));
                    } else {
                        i8 = (measuredWidth3 - getMeasuredWidth()) - (i5 + i4);
                    }
                } else if (this.currentType == 3) {
                    measuredWidth2 = (i3 - (getMeasuredWidth() / 2)) - this.arrowImageView.getMeasuredWidth();
                }
                setTranslationX(i8);
                float measuredWidth4 = (i3 - (i4 + i8)) - (this.arrowImageView.getMeasuredWidth() / 2);
                if (this.currentType == 7) {
                    measuredWidth4 += AndroidUtilities.dp(2.0f);
                }
                this.arrowImageView.setTranslationX(measuredWidth4);
                if (i3 <= view2.getMeasuredWidth() / 2) {
                    if (measuredWidth4 >= AndroidUtilities.dp(10.0f)) {
                        return;
                    }
                    float dp4 = measuredWidth4 - AndroidUtilities.dp(10.0f);
                    setTranslationX(getTranslationX() + dp4);
                    this.arrowImageView.setTranslationX(measuredWidth4 - dp4);
                    return;
                } else if (measuredWidth4 > getMeasuredWidth() - AndroidUtilities.dp(24.0f)) {
                    float measuredWidth5 = (measuredWidth4 - getMeasuredWidth()) + AndroidUtilities.dp(24.0f);
                    setTranslationX(measuredWidth5);
                    this.arrowImageView.setTranslationX(measuredWidth4 - measuredWidth5);
                    return;
                } else if (measuredWidth4 >= AndroidUtilities.dp(10.0f)) {
                    return;
                } else {
                    float dp5 = measuredWidth4 - AndroidUtilities.dp(10.0f);
                    setTranslationX(getTranslationX() + dp5);
                    this.arrowImageView.setTranslationX(measuredWidth4 - dp5);
                    return;
                }
            }
            i = measuredHeight + dp;
        }
        dp2 += i;
        i2 = this.currentType;
        int i82 = 0;
        if (i2 == 8) {
        }
        if (i2 != 3) {
        }
        view2 = (View) getParent();
        view2.getLocationInWindow(iArr);
        i3 = measuredWidth - iArr[0];
        int i92 = (dp2 - iArr[1]) - this.bottomOffset;
        int measuredWidth32 = view2.getMeasuredWidth();
        if (!this.isTopArrow) {
        }
        float f22 = this.extraTranslationY;
        float measuredHeight22 = i92 - getMeasuredHeight();
        this.translationY = measuredHeight22;
        setTranslationY(f22 + measuredHeight22);
        if (!(getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
        }
        if (this.currentType != 8) {
        }
        if (i3 <= view2.getMeasuredWidth() / 2) {
        }
        setTranslationX(i82);
        float measuredWidth42 = (i3 - (i4 + i82)) - (this.arrowImageView.getMeasuredWidth() / 2);
        if (this.currentType == 7) {
        }
        this.arrowImageView.setTranslationX(measuredWidth42);
        if (i3 <= view2.getMeasuredWidth() / 2) {
        }
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean z) {
        if (getTag() == null) {
            return;
        }
        setTag(null);
        Runnable runnable = this.hideRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.hideRunnable = null;
        }
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animatorSet = null;
        }
        if (z) {
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animatorSet = animatorSet2;
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f));
            this.animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.HintView.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    HintView.this.setVisibility(4);
                    HintView.this.currentView = null;
                    HintView.this.messageCell = null;
                    HintView.this.animatorSet = null;
                }
            });
            this.animatorSet.setDuration(300L);
            this.animatorSet.start();
            return;
        }
        setVisibility(4);
        this.currentView = null;
        this.messageCell = null;
        this.animatorSet = null;
    }

    public void setText(CharSequence charSequence) {
        this.textView.setText(charSequence);
    }

    public ChatMessageCell getMessageCell() {
        return this.messageCell;
    }

    public void setShowingDuration(long j) {
        this.showingDuration = j;
    }

    public void setBottomOffset(int i) {
        this.bottomOffset = i;
    }

    private int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }
}