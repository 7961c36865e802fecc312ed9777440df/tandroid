package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.net.Uri;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LetterDrawable;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.TextStyleSpan;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.FilteredSearchView;

/* loaded from: classes4.dex */
public class SharedLinkCell extends FrameLayout {
    private StaticLayout captionLayout;
    private TextPaint captionTextPaint;
    private int captionY;
    private CheckBox2 checkBox;
    private boolean checkingForLongPress;
    private StaticLayout dateLayout;
    private int dateLayoutX;
    private SharedLinkCellDelegate delegate;
    private TextPaint description2TextPaint;
    private int description2Y;
    private StaticLayout descriptionLayout;
    private StaticLayout descriptionLayout2;
    private List descriptionLayout2Spoilers;
    private List descriptionLayoutSpoilers;
    private TextPaint descriptionTextPaint;
    private int descriptionY;
    private boolean drawLinkImageView;
    private StaticLayout fromInfoLayout;
    private AnimatedEmojiSpan.EmojiGroupedSpans fromInfoLayoutEmojis;
    private int fromInfoLayoutY;
    private LetterDrawable letterDrawable;
    private ImageReceiver linkImageView;
    private ArrayList linkLayout;
    private boolean linkPreviewPressed;
    private SparseArray linkSpoilers;
    private int linkY;
    ArrayList links;
    private LinkSpanDrawable.LinkCollector linksCollector;
    private MessageObject message;
    private boolean needDivider;
    private AtomicReference patchedDescriptionLayout;
    private AtomicReference patchedDescriptionLayout2;
    private Path path;
    private CheckForLongPress pendingCheckForLongPress;
    private CheckForTap pendingCheckForTap;
    private int pressCount;
    private LinkSpanDrawable pressedLink;
    private int pressedLinkIndex;
    private Theme.ResourcesProvider resourcesProvider;
    private SpoilerEffect spoilerPressed;
    private int spoilerTypePressed;
    private Stack spoilersPool;
    private StaticLayout titleLayout;
    private TextPaint titleTextPaint;
    private int titleY;
    private int viewType;

    class CheckForLongPress implements Runnable {
        public int currentPressCount;

        CheckForLongPress() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SharedLinkCell.this.checkingForLongPress && SharedLinkCell.this.getParent() != null && this.currentPressCount == SharedLinkCell.this.pressCount) {
                SharedLinkCell.this.checkingForLongPress = false;
                try {
                    SharedLinkCell.this.performHapticFeedback(0);
                } catch (Exception unused) {
                }
                if (SharedLinkCell.this.pressedLinkIndex >= 0) {
                    SharedLinkCellDelegate sharedLinkCellDelegate = SharedLinkCell.this.delegate;
                    SharedLinkCell sharedLinkCell = SharedLinkCell.this;
                    sharedLinkCellDelegate.onLinkPress(((CharSequence) sharedLinkCell.links.get(sharedLinkCell.pressedLinkIndex)).toString(), true);
                }
                MotionEvent obtain = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
                SharedLinkCell.this.onTouchEvent(obtain);
                obtain.recycle();
            }
        }
    }

    private final class CheckForTap implements Runnable {
        private CheckForTap() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SharedLinkCell.this.pendingCheckForLongPress == null) {
                SharedLinkCell sharedLinkCell = SharedLinkCell.this;
                sharedLinkCell.pendingCheckForLongPress = sharedLinkCell.new CheckForLongPress();
            }
            SharedLinkCell.this.pendingCheckForLongPress.currentPressCount = SharedLinkCell.access$104(SharedLinkCell.this);
            SharedLinkCell sharedLinkCell2 = SharedLinkCell.this;
            sharedLinkCell2.postDelayed(sharedLinkCell2.pendingCheckForLongPress, ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout());
        }
    }

    public interface SharedLinkCellDelegate {
        boolean canPerformActions();

        void needOpenWebView(TLRPC.WebPage webPage, MessageObject messageObject);

        void onLinkPress(String str, boolean z);
    }

    public SharedLinkCell(Context context, int i) {
        this(context, i, null);
    }

    public SharedLinkCell(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.checkingForLongPress = false;
        this.pendingCheckForLongPress = null;
        this.pressCount = 0;
        this.pendingCheckForTap = null;
        this.linksCollector = new LinkSpanDrawable.LinkCollector(this);
        this.links = new ArrayList();
        this.linkLayout = new ArrayList();
        this.linkSpoilers = new SparseArray();
        this.descriptionLayoutSpoilers = new ArrayList();
        this.descriptionLayout2Spoilers = new ArrayList();
        this.spoilersPool = new Stack();
        this.path = new Path();
        this.spoilerTypePressed = -1;
        this.titleY = AndroidUtilities.dp(10.0f);
        this.descriptionY = AndroidUtilities.dp(30.0f);
        this.patchedDescriptionLayout = new AtomicReference();
        this.description2Y = AndroidUtilities.dp(30.0f);
        this.patchedDescriptionLayout2 = new AtomicReference();
        this.captionY = AndroidUtilities.dp(30.0f);
        this.fromInfoLayoutY = AndroidUtilities.dp(30.0f);
        this.resourcesProvider = resourcesProvider;
        this.viewType = i;
        setFocusable(true);
        TextPaint textPaint = new TextPaint(1);
        this.titleTextPaint = textPaint;
        textPaint.setTypeface(AndroidUtilities.bold());
        this.titleTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
        this.descriptionTextPaint = new TextPaint(1);
        this.titleTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
        this.descriptionTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
        setWillNotDraw(false);
        ImageReceiver imageReceiver = new ImageReceiver(this);
        this.linkImageView = imageReceiver;
        imageReceiver.setRoundRadius(AndroidUtilities.dp(4.0f));
        this.letterDrawable = new LetterDrawable(resourcesProvider, 0);
        CheckBox2 checkBox2 = new CheckBox2(context, 21, resourcesProvider);
        this.checkBox = checkBox2;
        checkBox2.setVisibility(4);
        this.checkBox.setColor(-1, Theme.key_windowBackgroundWhite, Theme.key_checkboxCheck);
        this.checkBox.setDrawUnchecked(false);
        this.checkBox.setDrawBackgroundAsArc(2);
        CheckBox2 checkBox22 = this.checkBox;
        boolean z = LocaleController.isRTL;
        addView(checkBox22, LayoutHelper.createFrame(24, 24.0f, (z ? 5 : 3) | 48, z ? 0.0f : 44.0f, 44.0f, z ? 44.0f : 0.0f, 0.0f));
        if (i == 1) {
            TextPaint textPaint2 = new TextPaint(1);
            this.description2TextPaint = textPaint2;
            textPaint2.setTextSize(AndroidUtilities.dp(13.0f));
        }
        TextPaint textPaint3 = new TextPaint(1);
        this.captionTextPaint = textPaint3;
        textPaint3.setTextSize(AndroidUtilities.dp(13.0f));
    }

    static /* synthetic */ int access$104(SharedLinkCell sharedLinkCell) {
        int i = sharedLinkCell.pressCount + 1;
        sharedLinkCell.pressCount = i;
        return i;
    }

    private int getYOffsetForType(int i) {
        return i != 1 ? i != 2 ? this.linkY : this.description2Y : this.descriptionY;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSpoilerRipples$0() {
        this.message.isSpoilersRevealed = true;
        this.linkSpoilers.clear();
        this.descriptionLayoutSpoilers.clear();
        this.descriptionLayout2Spoilers.clear();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSpoilerRipples$1() {
        post(new Runnable() { // from class: org.telegram.ui.Cells.SharedLinkCell$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SharedLinkCell.this.lambda$startSpoilerRipples$0();
            }
        });
    }

    private void startSpoilerRipples(int i, int i2, int i3) {
        int dp = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
        resetPressedLink();
        this.spoilerPressed.setOnRippleEndCallback(new Runnable() { // from class: org.telegram.ui.Cells.SharedLinkCell$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SharedLinkCell.this.lambda$startSpoilerRipples$1();
            }
        });
        int i4 = i - dp;
        float sqrt = (float) Math.sqrt(Math.pow(getWidth(), 2.0d) + Math.pow(getHeight(), 2.0d));
        int i5 = this.spoilerTypePressed;
        if (i5 == 0) {
            float f = 0.0f;
            for (int i6 = 0; i6 < this.linkLayout.size(); i6++) {
                Layout layout = (Layout) this.linkLayout.get(i6);
                f += layout.getLineBottom(layout.getLineCount() - 1);
                Iterator it = ((List) this.linkSpoilers.get(i6)).iterator();
                while (it.hasNext()) {
                    ((SpoilerEffect) it.next()).startRipple(i4, ((i2 - getYOffsetForType(0)) - i3) + f, sqrt);
                }
            }
        } else if (i5 == 1) {
            Iterator it2 = this.descriptionLayoutSpoilers.iterator();
            while (it2.hasNext()) {
                ((SpoilerEffect) it2.next()).startRipple(i4, i2 - getYOffsetForType(1), sqrt);
            }
        } else if (i5 == 2) {
            Iterator it3 = this.descriptionLayout2Spoilers.iterator();
            while (it3.hasNext()) {
                ((SpoilerEffect) it3.next()).startRipple(i4, i2 - getYOffsetForType(2), sqrt);
            }
        }
        for (int i7 = 0; i7 <= 2; i7++) {
            if (i7 != this.spoilerTypePressed) {
                if (i7 == 0) {
                    for (int i8 = 0; i8 < this.linkLayout.size(); i8++) {
                        Layout layout2 = (Layout) this.linkLayout.get(i8);
                        layout2.getLineBottom(layout2.getLineCount() - 1);
                        Iterator it4 = ((List) this.linkSpoilers.get(i8)).iterator();
                        while (it4.hasNext()) {
                            ((SpoilerEffect) it4.next()).startRipple(r1.getBounds().centerX(), r1.getBounds().centerY(), sqrt);
                        }
                    }
                } else if (i7 == 1) {
                    Iterator it5 = this.descriptionLayoutSpoilers.iterator();
                    while (it5.hasNext()) {
                        ((SpoilerEffect) it5.next()).startRipple(r13.getBounds().centerX(), r13.getBounds().centerY(), sqrt);
                    }
                } else if (i7 == 2) {
                    Iterator it6 = this.descriptionLayout2Spoilers.iterator();
                    while (it6.hasNext()) {
                        ((SpoilerEffect) it6.next()).startRipple(r13.getBounds().centerX(), r13.getBounds().centerY(), sqrt);
                    }
                }
            }
        }
        this.spoilerTypePressed = -1;
        this.spoilerPressed = null;
    }

    protected void cancelCheckLongPress() {
        this.checkingForLongPress = false;
        CheckForLongPress checkForLongPress = this.pendingCheckForLongPress;
        if (checkForLongPress != null) {
            removeCallbacks(checkForLongPress);
        }
        CheckForTap checkForTap = this.pendingCheckForTap;
        if (checkForTap != null) {
            removeCallbacks(checkForTap);
        }
    }

    public String getLink(int i) {
        if (i < 0 || i >= this.links.size()) {
            return null;
        }
        return ((CharSequence) this.links.get(i)).toString();
    }

    public ImageReceiver getLinkImageView() {
        return this.linkImageView;
    }

    public MessageObject getMessage() {
        return this.message;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.drawLinkImageView) {
            this.linkImageView.onAttachedToWindow();
        }
        this.fromInfoLayoutEmojis = AnimatedEmojiSpan.update(0, this, this.fromInfoLayoutEmojis, this.fromInfoLayout);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.drawLinkImageView) {
            this.linkImageView.onDetachedFromWindow();
        }
        AnimatedEmojiSpan.release(this, this.fromInfoLayoutEmojis);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float dp;
        float measuredHeight;
        float measuredWidth;
        float measuredHeight2;
        Paint paint;
        if (this.viewType == 1) {
            this.description2TextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3, this.resourcesProvider));
        }
        if (this.dateLayout != null) {
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline) + (LocaleController.isRTL ? 0 : this.dateLayoutX), this.titleY);
            this.dateLayout.draw(canvas);
            canvas.restore();
        }
        if (this.titleLayout != null) {
            canvas.save();
            float dp2 = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
            if (LocaleController.isRTL) {
                dp2 += this.dateLayout == null ? 0.0f : r1.getWidth() + AndroidUtilities.dp(4.0f);
            }
            canvas.translate(dp2, this.titleY);
            this.titleLayout.draw(canvas);
            canvas.restore();
        }
        if (this.captionLayout != null) {
            this.captionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.captionY);
            this.captionLayout.draw(canvas);
            canvas.restore();
        }
        if (this.descriptionLayout != null) {
            this.descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.descriptionY);
            SpoilerEffect.renderWithRipple(this, false, this.descriptionTextPaint.getColor(), -AndroidUtilities.dp(2.0f), this.patchedDescriptionLayout, 0, this.descriptionLayout, this.descriptionLayoutSpoilers, canvas, false);
            canvas.restore();
        }
        if (this.descriptionLayout2 != null) {
            this.descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.description2Y);
            SpoilerEffect.renderWithRipple(this, false, this.descriptionTextPaint.getColor(), -AndroidUtilities.dp(2.0f), this.patchedDescriptionLayout2, 0, this.descriptionLayout2, this.descriptionLayout2Spoilers, canvas, false);
            canvas.restore();
        }
        if (!this.linkLayout.isEmpty()) {
            this.descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteLinkText, this.resourcesProvider));
            int i = 0;
            for (int i2 = 0; i2 < this.linkLayout.size(); i2++) {
                StaticLayout staticLayout = (StaticLayout) this.linkLayout.get(i2);
                List list = (List) this.linkSpoilers.get(i2);
                if (staticLayout.getLineCount() > 0) {
                    canvas.save();
                    canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.linkY + i);
                    this.path.rewind();
                    if (list != null) {
                        Iterator it = list.iterator();
                        while (it.hasNext()) {
                            Rect bounds = ((SpoilerEffect) it.next()).getBounds();
                            this.path.addRect(bounds.left, bounds.top, bounds.right, bounds.bottom, Path.Direction.CW);
                        }
                    }
                    canvas.save();
                    canvas.clipPath(this.path, Region.Op.DIFFERENCE);
                    staticLayout.draw(canvas);
                    canvas.restore();
                    canvas.save();
                    canvas.clipPath(this.path);
                    this.path.rewind();
                    if (list != null && !list.isEmpty()) {
                        ((SpoilerEffect) list.get(0)).getRipplePath(this.path);
                    }
                    canvas.clipPath(this.path);
                    staticLayout.draw(canvas);
                    canvas.restore();
                    if (list != null) {
                        Iterator it2 = list.iterator();
                        while (it2.hasNext()) {
                            ((SpoilerEffect) it2.next()).draw(canvas);
                        }
                    }
                    canvas.restore();
                    i += staticLayout.getLineBottom(staticLayout.getLineCount() - 1);
                }
            }
            if (this.linksCollector.draw(canvas)) {
                invalidate();
            }
        }
        if (this.fromInfoLayout != null) {
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.fromInfoLayoutY);
            this.fromInfoLayout.draw(canvas);
            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.fromInfoLayout, this.fromInfoLayoutEmojis, 0.0f, null, 0.0f, 0.0f, 0.0f, 1.0f);
            canvas.restore();
        }
        this.letterDrawable.draw(canvas);
        if (this.drawLinkImageView) {
            this.linkImageView.draw(canvas);
        }
        if (this.needDivider) {
            if (LocaleController.isRTL) {
                measuredHeight = getMeasuredHeight() - 1;
                measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                measuredHeight2 = getMeasuredHeight() - 1;
                paint = Theme.dividerPaint;
                dp = 0.0f;
            } else {
                dp = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                measuredHeight = getMeasuredHeight() - 1;
                measuredWidth = getMeasuredWidth();
                measuredHeight2 = getMeasuredHeight() - 1;
                paint = Theme.dividerPaint;
            }
            canvas.drawLine(dp, measuredHeight, measuredWidth, measuredHeight2, paint);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        StringBuilder sb = new StringBuilder();
        StaticLayout staticLayout = this.titleLayout;
        if (staticLayout != null) {
            sb.append(staticLayout.getText());
        }
        if (this.descriptionLayout != null) {
            sb.append(", ");
            sb.append(this.descriptionLayout.getText());
        }
        if (this.descriptionLayout2 != null) {
            sb.append(", ");
            sb.append(this.descriptionLayout2.getText());
        }
        accessibilityNodeInfo.setText(sb.toString());
        if (this.checkBox.isChecked()) {
            accessibilityNodeInfo.setChecked(true);
            accessibilityNodeInfo.setCheckable(true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:130:0x02ad  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x033c  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x034d  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0457  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0477  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0548  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x056c  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x05f2  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x068e  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x069c  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x06be  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x0616  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x05ed  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0553  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x03a3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:261:0x0355 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:272:0x0350  */
    /* JADX WARN: Removed duplicated region for block: B:273:0x0341  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x02e8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:286:0x02e3  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01ef A[Catch: Exception -> 0x010f, TryCatch #0 {Exception -> 0x010f, blocks: (B:47:0x00f9, B:49:0x00fd, B:52:0x0102, B:55:0x0108, B:58:0x0112, B:60:0x0145, B:63:0x01ef, B:65:0x01f7, B:67:0x0207, B:69:0x0217, B:70:0x022b, B:71:0x023e, B:73:0x0244, B:82:0x0259, B:87:0x0282, B:90:0x0153, B:91:0x015d, B:92:0x01e9, B:94:0x0164, B:96:0x0168, B:98:0x017c, B:100:0x0182, B:102:0x0190, B:104:0x0197, B:106:0x019f, B:108:0x01a9, B:109:0x01af, B:110:0x01cb, B:112:0x01cf, B:114:0x01dd, B:115:0x0178), top: B:46:0x00f9 }] */
    /* JADX WARN: Type inference failed for: r2v125 */
    /* JADX WARN: Type inference failed for: r2v126 */
    /* JADX WARN: Type inference failed for: r2v99, types: [android.text.Spannable] */
    /* JADX WARN: Type inference failed for: r4v3, types: [java.lang.CharSequence] */
    @Override // android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int i, int i2) {
        String str;
        String str2;
        String str3;
        boolean z;
        MessageObject messageObject;
        SpannableStringBuilder spannableStringBuilder;
        int i3;
        String str4;
        MessageObject messageObject2;
        TLRPC.PhotoSize photoSize;
        StaticLayout staticLayout;
        boolean z2;
        int i4;
        StaticLayout staticLayout2;
        int i5;
        StaticLayout staticLayout3;
        StaticLayout staticLayout4;
        StaticLayout staticLayout5;
        int i6;
        int i7;
        TLRPC.PhotoSize photoSize2;
        CharSequence ellipsize;
        int i8;
        String str5;
        int lastIndexOf;
        ?? r2;
        int i9;
        int i10 = 0;
        this.drawLinkImageView = false;
        String str6 = null;
        this.descriptionLayout = null;
        this.titleLayout = null;
        this.descriptionLayout2 = null;
        this.captionLayout = null;
        this.linkLayout.clear();
        this.links.clear();
        int size = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(AndroidUtilities.leftBaseline)) - AndroidUtilities.dp(8.0f);
        MessageObject messageObject3 = this.message;
        TLRPC.MessageMedia messageMedia = messageObject3.messageOwner.media;
        int i11 = 1;
        if (messageMedia instanceof TLRPC.TL_messageMediaWebPage) {
            TLRPC.WebPage webPage = messageMedia.webpage;
            if (webPage instanceof TLRPC.TL_webPage) {
                if (messageObject3.photoThumbs == null && webPage.photo != null) {
                    messageObject3.generateThumbs(true);
                }
                boolean z3 = (webPage.photo == null || this.message.photoThumbs == null) ? false : true;
                str2 = webPage.title;
                if (str2 == null) {
                    str2 = webPage.site_name;
                }
                str3 = webPage.description;
                str = webPage.url;
                z = z3;
                messageObject = this.message;
                if (messageObject != null || messageObject.messageOwner.entities.isEmpty()) {
                    spannableStringBuilder = null;
                } else {
                    SpannableStringBuilder spannableStringBuilder2 = null;
                    int i12 = 0;
                    while (i12 < this.message.messageOwner.entities.size()) {
                        TLRPC.MessageEntity messageEntity = this.message.messageOwner.entities.get(i12);
                        if (messageEntity.length > 0 && (i8 = messageEntity.offset) >= 0 && i8 < this.message.messageOwner.message.length()) {
                            if (messageEntity.offset + messageEntity.length > this.message.messageOwner.message.length()) {
                                messageEntity.length = this.message.messageOwner.message.length() - messageEntity.offset;
                            }
                            if (i12 == 0 && str != null && ((messageEntity.offset != 0 || messageEntity.length != this.message.messageOwner.message.length()) && (this.message.messageOwner.entities.size() != i11 || str3 == null))) {
                                spannableStringBuilder2 = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                                MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder2);
                            }
                            SpannableStringBuilder spannableStringBuilder3 = spannableStringBuilder2;
                            try {
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                            if (!(messageEntity instanceof TLRPC.TL_messageEntityTextUrl) && !(messageEntity instanceof TLRPC.TL_messageEntityUrl)) {
                                if (!(messageEntity instanceof TLRPC.TL_messageEntityEmail) || (str2 != null && str2.length() != 0)) {
                                    str5 = str6;
                                    if (str5 != null) {
                                        if (AndroidUtilities.charSequenceContains(str5, "://") || str5.toString().toLowerCase().indexOf("http") == 0 || str5.toString().toLowerCase().indexOf("mailto") == 0) {
                                            i9 = 0;
                                        } else {
                                            str5 = "http://" + ((Object) str5);
                                            i9 = 7;
                                        }
                                        SpannableString valueOf = SpannableString.valueOf(str5);
                                        int i13 = messageEntity.offset;
                                        int i14 = messageEntity.length + i13;
                                        Iterator<TLRPC.MessageEntity> it = this.message.messageOwner.entities.iterator();
                                        while (it.hasNext()) {
                                            TLRPC.MessageEntity next = it.next();
                                            int i15 = next.offset;
                                            int i16 = next.length + i15;
                                            if ((next instanceof TLRPC.TL_messageEntitySpoiler) && i13 <= i16 && i14 >= i15) {
                                                TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
                                                textStyleRun.flags |= 256;
                                                valueOf.setSpan(new TextStyleSpan(textStyleRun), Math.max(i13, i15), Math.min(i14, i16) + i9, 33);
                                            }
                                        }
                                        this.links.add(valueOf);
                                    }
                                    spannableStringBuilder2 = spannableStringBuilder3;
                                }
                                StringBuilder sb = new StringBuilder();
                                sb.append("mailto:");
                                String str7 = this.message.messageOwner.message;
                                int i17 = messageEntity.offset;
                                sb.append(str7.substring(i17, messageEntity.length + i17));
                                str5 = sb.toString();
                                String str8 = this.message.messageOwner.message;
                                int i18 = messageEntity.offset;
                                str2 = str8.substring(i18, messageEntity.length + i18);
                                if (messageEntity.offset != 0 || messageEntity.length != this.message.messageOwner.message.length()) {
                                    r2 = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                                    MediaDataController.addTextStyleRuns(this.message, r2);
                                    str3 = r2;
                                }
                                if (str5 != null) {
                                }
                                spannableStringBuilder2 = spannableStringBuilder3;
                            }
                            if (messageEntity instanceof TLRPC.TL_messageEntityUrl) {
                                String str9 = this.message.messageOwner.message;
                                int i19 = messageEntity.offset;
                                str5 = str9.substring(i19, messageEntity.length + i19);
                            } else {
                                str5 = messageEntity.url;
                            }
                            if (str2 == null || str2.length() == 0) {
                                str2 = Uri.parse(str5.toString()).getHost();
                                if (str2 == null) {
                                    str2 = str5.toString();
                                }
                                if (str2 != null && (lastIndexOf = str2.lastIndexOf(46)) >= 0) {
                                    String substring = str2.substring(i10, lastIndexOf);
                                    int lastIndexOf2 = substring.lastIndexOf(46);
                                    if (lastIndexOf2 >= 0) {
                                        substring = substring.substring(lastIndexOf2 + i11);
                                    }
                                    str2 = substring.substring(i10, i11).toUpperCase() + substring.substring(i11);
                                }
                                if (messageEntity.offset != 0 || messageEntity.length != this.message.messageOwner.message.length()) {
                                    r2 = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                                    MediaDataController.addTextStyleRuns(this.message, r2);
                                    str3 = r2;
                                }
                            }
                            if (str5 != null) {
                            }
                            spannableStringBuilder2 = spannableStringBuilder3;
                        }
                        i12++;
                        i10 = 0;
                        str6 = null;
                        i11 = 1;
                    }
                    spannableStringBuilder = spannableStringBuilder2;
                }
                if (str != null && this.links.isEmpty()) {
                    this.links.add(str);
                }
                if (this.viewType != 1) {
                    String stringForMessageListDate = LocaleController.stringForMessageListDate(this.message.messageOwner.date);
                    int ceil = (int) Math.ceil(this.description2TextPaint.measureText(stringForMessageListDate));
                    this.dateLayout = ChatMessageCell.generateStaticLayout(stringForMessageListDate, this.description2TextPaint, ceil, ceil, 0, 1);
                    this.dateLayoutX = (size - ceil) - AndroidUtilities.dp(8.0f);
                    i3 = ceil + AndroidUtilities.dp(12.0f);
                } else {
                    i3 = 0;
                }
                if (str2 != null) {
                    try {
                        ?? highlightText = AndroidUtilities.highlightText(str2, this.message.highlightedWords, (Theme.ResourcesProvider) null);
                        int i20 = size - i3;
                        StaticLayout generateStaticLayout = ChatMessageCell.generateStaticLayout(highlightText != 0 ? highlightText : str2, this.titleTextPaint, i20 - AndroidUtilities.dp(4.0f), i20 - AndroidUtilities.dp(4.0f), 0, 3);
                        this.titleLayout = generateStaticLayout;
                        if (generateStaticLayout.getLineCount() > 0) {
                            int i21 = this.titleY;
                            StaticLayout staticLayout6 = this.titleLayout;
                            this.descriptionY = i21 + staticLayout6.getLineBottom(staticLayout6.getLineCount() - 1) + AndroidUtilities.dp(4.0f);
                        }
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                    this.letterDrawable.setTitle(str2);
                }
                this.description2Y = this.descriptionY;
                StaticLayout staticLayout7 = this.titleLayout;
                int max = Math.max(1, 4 - (staticLayout7 == null ? staticLayout7.getLineCount() : 0));
                if (this.viewType != 1) {
                    spannableStringBuilder = null;
                    str4 = null;
                } else {
                    str4 = str3;
                }
                if (str4 != null) {
                    try {
                        StaticLayout generateStaticLayout2 = ChatMessageCell.generateStaticLayout(str4, this.descriptionTextPaint, size, size, 0, max);
                        this.descriptionLayout = generateStaticLayout2;
                        if (generateStaticLayout2.getLineCount() > 0) {
                            int i22 = this.descriptionY;
                            StaticLayout staticLayout8 = this.descriptionLayout;
                            this.description2Y = i22 + staticLayout8.getLineBottom(staticLayout8.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                        }
                        this.spoilersPool.addAll(this.descriptionLayoutSpoilers);
                        this.descriptionLayoutSpoilers.clear();
                        if (!this.message.isSpoilersRevealed) {
                            SpoilerEffect.addSpoilers(this, this.descriptionLayout, this.spoilersPool, this.descriptionLayoutSpoilers);
                        }
                    } catch (Exception e3) {
                        FileLog.e(e3);
                    }
                }
                if (spannableStringBuilder != null) {
                    try {
                        this.descriptionLayout2 = ChatMessageCell.generateStaticLayout(spannableStringBuilder, this.descriptionTextPaint, size, size, 0, max);
                        if (this.descriptionLayout != null) {
                            this.description2Y += AndroidUtilities.dp(10.0f);
                        }
                        this.spoilersPool.addAll(this.descriptionLayout2Spoilers);
                        this.descriptionLayout2Spoilers.clear();
                        if (!this.message.isSpoilersRevealed) {
                            SpoilerEffect.addSpoilers(this, this.descriptionLayout2, this.spoilersPool, this.descriptionLayout2Spoilers);
                        }
                    } catch (Exception e4) {
                        FileLog.e(e4);
                    }
                }
                messageObject2 = this.message;
                if (messageObject2 != null || TextUtils.isEmpty(messageObject2.messageOwner.message)) {
                    photoSize = null;
                } else {
                    photoSize = null;
                    CharSequence highlightText2 = AndroidUtilities.highlightText(Emoji.replaceEmoji(this.message.messageOwner.message.replace("\n", " ").replaceAll(" +", " ").trim(), Theme.chat_msgTextPaint.getFontMetricsInt(), false), this.message.highlightedWords, (Theme.ResourcesProvider) null);
                    if (highlightText2 != null) {
                        this.captionLayout = new StaticLayout(TextUtils.ellipsize(AndroidUtilities.ellipsizeCenterEnd(highlightText2, this.message.highlightedWords.get(0), size, this.captionTextPaint, NotificationCenter.walletSyncProgressChanged), this.captionTextPaint, size, TextUtils.TruncateAt.END), this.captionTextPaint, size + AndroidUtilities.dp(4.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    }
                }
                staticLayout = this.captionLayout;
                if (staticLayout != null) {
                    int i23 = this.descriptionY;
                    this.captionY = i23;
                    int lineBottom = i23 + staticLayout.getLineBottom(staticLayout.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                    this.descriptionY = lineBottom;
                    this.description2Y = lineBottom;
                }
                if (!this.links.isEmpty()) {
                    for (int i24 = 0; i24 < this.linkSpoilers.size(); i24++) {
                        this.spoilersPool.addAll((Collection) this.linkSpoilers.get(i24));
                    }
                    this.linkSpoilers.clear();
                    int i25 = 0;
                    while (i25 < this.links.size()) {
                        try {
                            ellipsize = TextUtils.ellipsize(AndroidUtilities.replaceNewLines(SpannableStringBuilder.valueOf((CharSequence) this.links.get(i25))), this.descriptionTextPaint, Math.min((int) Math.ceil(this.descriptionTextPaint.measureText(r0, 0, r0.length())), size), TextUtils.TruncateAt.MIDDLE);
                            photoSize2 = photoSize;
                        } catch (Exception e5) {
                            e = e5;
                            photoSize2 = photoSize;
                        }
                        try {
                            StaticLayout staticLayout9 = new StaticLayout(ellipsize, this.descriptionTextPaint, size, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                            this.linkY = this.description2Y;
                            StaticLayout staticLayout10 = this.descriptionLayout2;
                            if (staticLayout10 != null && staticLayout10.getLineCount() != 0) {
                                int i26 = this.linkY;
                                StaticLayout staticLayout11 = this.descriptionLayout2;
                                this.linkY = i26 + staticLayout11.getLineBottom(staticLayout11.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                            }
                            if (!this.message.isSpoilersRevealed) {
                                ArrayList arrayList = new ArrayList();
                                if (ellipsize instanceof Spannable) {
                                    SpoilerEffect.addSpoilers(this, staticLayout9, (Spannable) ellipsize, this.spoilersPool, arrayList);
                                }
                                this.linkSpoilers.put(i25, arrayList);
                            }
                            this.linkLayout.add(staticLayout9);
                        } catch (Exception e6) {
                            e = e6;
                            FileLog.e(e);
                            i25++;
                            photoSize = photoSize2;
                        }
                        i25++;
                        photoSize = photoSize2;
                    }
                }
                TLRPC.PhotoSize photoSize3 = photoSize;
                int dp = AndroidUtilities.dp(52.0f);
                int size2 = !LocaleController.isRTL ? (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(10.0f)) - dp : AndroidUtilities.dp(10.0f);
                this.letterDrawable.setBounds(size2, AndroidUtilities.dp(11.0f), size2 + dp, AndroidUtilities.dp(63.0f));
                if (z) {
                    z2 = true;
                } else {
                    TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, dp, true);
                    TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, 80);
                    if (closestPhotoSizeWithSize2 == closestPhotoSizeWithSize) {
                        closestPhotoSizeWithSize2 = photoSize3;
                    }
                    if (closestPhotoSizeWithSize != null) {
                        closestPhotoSizeWithSize.size = -1;
                    }
                    if (closestPhotoSizeWithSize2 != null) {
                        closestPhotoSizeWithSize2.size = -1;
                    }
                    float f = dp;
                    this.linkImageView.setImageCoords(size2, AndroidUtilities.dp(11.0f), f, f);
                    FileLoader.getAttachFileName(closestPhotoSizeWithSize);
                    Locale locale = Locale.US;
                    this.linkImageView.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize, this.message.photoThumbsObject), String.format(locale, "%d_%d", Integer.valueOf(dp), Integer.valueOf(dp)), ImageLocation.getForObject(closestPhotoSizeWithSize2, this.message.photoThumbsObject), String.format(locale, "%d_%d_b", Integer.valueOf(dp), Integer.valueOf(dp)), 0L, null, this.message, 0);
                    z2 = true;
                    this.drawLinkImageView = true;
                }
                if (this.viewType != z2) {
                    StaticLayout generateStaticLayout3 = ChatMessageCell.generateStaticLayout(FilteredSearchView.createFromInfoString(this.message, z2, 2, this.description2TextPaint), this.description2TextPaint, size, size, 0, max);
                    this.fromInfoLayout = generateStaticLayout3;
                    i4 = 0;
                    this.fromInfoLayoutEmojis = AnimatedEmojiSpan.update(0, this, this.fromInfoLayoutEmojis, generateStaticLayout3);
                } else {
                    i4 = 0;
                }
                staticLayout2 = this.titleLayout;
                if (staticLayout2 != null || staticLayout2.getLineCount() == 0) {
                    i5 = 0;
                } else {
                    StaticLayout staticLayout12 = this.titleLayout;
                    i5 = staticLayout12.getLineBottom(staticLayout12.getLineCount() - 1) + AndroidUtilities.dp(4.0f);
                }
                staticLayout3 = this.captionLayout;
                if (staticLayout3 != null && staticLayout3.getLineCount() != 0) {
                    StaticLayout staticLayout13 = this.captionLayout;
                    i5 += staticLayout13.getLineBottom(staticLayout13.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                }
                staticLayout4 = this.descriptionLayout;
                if (staticLayout4 != null && staticLayout4.getLineCount() != 0) {
                    StaticLayout staticLayout14 = this.descriptionLayout;
                    i5 += staticLayout14.getLineBottom(staticLayout14.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                }
                staticLayout5 = this.descriptionLayout2;
                if (staticLayout5 != null && staticLayout5.getLineCount() != 0) {
                    StaticLayout staticLayout15 = this.descriptionLayout2;
                    i5 += staticLayout15.getLineBottom(staticLayout15.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                    if (this.descriptionLayout != null) {
                        i5 += AndroidUtilities.dp(10.0f);
                    }
                }
                i6 = 0;
                while (i6 < this.linkLayout.size()) {
                    StaticLayout staticLayout16 = (StaticLayout) this.linkLayout.get(i6);
                    if (staticLayout16.getLineCount() > 0) {
                        i7 = 1;
                        i4 += staticLayout16.getLineBottom(staticLayout16.getLineCount() - 1);
                    } else {
                        i7 = 1;
                    }
                    i6 += i7;
                }
                int i27 = i5 + i4;
                if (this.fromInfoLayout != null) {
                    this.fromInfoLayoutY = this.linkY + i4 + AndroidUtilities.dp(5.0f);
                    StaticLayout staticLayout17 = this.fromInfoLayout;
                    i27 += staticLayout17.getLineBottom(staticLayout17.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                }
                this.checkBox.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824));
                setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(76.0f), i27 + AndroidUtilities.dp(17.0f)) + (this.needDivider ? 1 : 0));
            }
        }
        str = null;
        str2 = null;
        str3 = null;
        z = false;
        messageObject = this.message;
        if (messageObject != null) {
        }
        spannableStringBuilder = null;
        if (str != null) {
            this.links.add(str);
        }
        if (this.viewType != 1) {
        }
        if (str2 != null) {
        }
        this.description2Y = this.descriptionY;
        StaticLayout staticLayout72 = this.titleLayout;
        int max2 = Math.max(1, 4 - (staticLayout72 == null ? staticLayout72.getLineCount() : 0));
        if (this.viewType != 1) {
        }
        if (str4 != null) {
        }
        if (spannableStringBuilder != null) {
        }
        messageObject2 = this.message;
        if (messageObject2 != null) {
        }
        photoSize = null;
        staticLayout = this.captionLayout;
        if (staticLayout != null) {
        }
        if (!this.links.isEmpty()) {
        }
        TLRPC.PhotoSize photoSize32 = photoSize;
        int dp2 = AndroidUtilities.dp(52.0f);
        if (!LocaleController.isRTL) {
        }
        this.letterDrawable.setBounds(size2, AndroidUtilities.dp(11.0f), size2 + dp2, AndroidUtilities.dp(63.0f));
        if (z) {
        }
        if (this.viewType != z2) {
        }
        staticLayout2 = this.titleLayout;
        if (staticLayout2 != null) {
        }
        i5 = 0;
        staticLayout3 = this.captionLayout;
        if (staticLayout3 != null) {
            StaticLayout staticLayout132 = this.captionLayout;
            i5 += staticLayout132.getLineBottom(staticLayout132.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
        }
        staticLayout4 = this.descriptionLayout;
        if (staticLayout4 != null) {
            StaticLayout staticLayout142 = this.descriptionLayout;
            i5 += staticLayout142.getLineBottom(staticLayout142.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
        }
        staticLayout5 = this.descriptionLayout2;
        if (staticLayout5 != null) {
            StaticLayout staticLayout152 = this.descriptionLayout2;
            i5 += staticLayout152.getLineBottom(staticLayout152.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
            if (this.descriptionLayout != null) {
            }
        }
        i6 = 0;
        while (i6 < this.linkLayout.size()) {
        }
        int i272 = i5 + i4;
        if (this.fromInfoLayout != null) {
        }
        this.checkBox.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824));
        setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(76.0f), i272 + AndroidUtilities.dp(17.0f)) + (this.needDivider ? 1 : 0));
    }

    /* JADX WARN: Code restructure failed: missing block: B:115:0x0220, code lost:
    
        if (r6 != false) goto L138;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        SharedLinkCellDelegate sharedLinkCellDelegate;
        boolean z2;
        int i;
        int i2;
        int i3;
        String str;
        TLRPC.MessageMedia messageMedia;
        if (this.message != null && !this.linkLayout.isEmpty() && (sharedLinkCellDelegate = this.delegate) != null && sharedLinkCellDelegate.canPerformActions()) {
            if (motionEvent.getAction() == 0 || ((this.linkPreviewPressed || this.spoilerPressed != null) && motionEvent.getAction() == 1)) {
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                int i4 = 0;
                int i5 = 0;
                while (true) {
                    if (i4 >= this.linkLayout.size()) {
                        z = false;
                        z2 = false;
                        break;
                    }
                    StaticLayout staticLayout = (StaticLayout) this.linkLayout.get(i4);
                    if (staticLayout.getLineCount() > 0) {
                        int lineBottom = staticLayout.getLineBottom(staticLayout.getLineCount() - 1);
                        int dp = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
                        float f = x;
                        float f2 = dp;
                        if (f < staticLayout.getLineLeft(0) + f2 || f > staticLayout.getLineWidth(0) + f2 || y < (i3 = this.linkY + i5) || y > i3 + lineBottom) {
                            i5 += lineBottom;
                        } else {
                            TLRPC.WebPage webPage = null;
                            if (motionEvent.getAction() == 0) {
                                this.spoilerPressed = null;
                                if (this.linkSpoilers.get(i4, null) != null) {
                                    Iterator it = ((List) this.linkSpoilers.get(i4)).iterator();
                                    while (true) {
                                        if (!it.hasNext()) {
                                            break;
                                        }
                                        SpoilerEffect spoilerEffect = (SpoilerEffect) it.next();
                                        if (spoilerEffect.getBounds().contains(x - dp, (y - this.linkY) - i5)) {
                                            resetPressedLink();
                                            this.spoilerPressed = spoilerEffect;
                                            this.spoilerTypePressed = 0;
                                            break;
                                        }
                                    }
                                }
                                if (this.spoilerPressed == null && (this.pressedLinkIndex != i4 || this.pressedLink == null || !this.linkPreviewPressed)) {
                                    resetPressedLink();
                                    this.pressedLinkIndex = i4;
                                    LinkSpanDrawable linkSpanDrawable = new LinkSpanDrawable(null, this.resourcesProvider, x - dp, (y - this.linkY) - i5);
                                    this.pressedLink = linkSpanDrawable;
                                    LinkPath obtainNewPath = linkSpanDrawable.obtainNewPath();
                                    this.linkPreviewPressed = true;
                                    this.linksCollector.addLink(this.pressedLink);
                                    startCheckLongPress();
                                    try {
                                        obtainNewPath.setCurrentLayout(staticLayout, 0, f2, this.linkY + i5);
                                        staticLayout.getSelectionPath(0, staticLayout.getText().length(), obtainNewPath);
                                    } catch (Exception e) {
                                        FileLog.e(e);
                                    }
                                }
                            } else if (this.linkPreviewPressed) {
                                try {
                                    if (this.pressedLinkIndex == 0 && (messageMedia = this.message.messageOwner.media) != null) {
                                        webPage = messageMedia.webpage;
                                    }
                                    if (webPage == null || (str = webPage.embed_url) == null || str.length() == 0) {
                                        this.delegate.onLinkPress(((CharSequence) this.links.get(this.pressedLinkIndex)).toString(), false);
                                    } else {
                                        this.delegate.needOpenWebView(webPage, this.message);
                                    }
                                } catch (Exception e2) {
                                    FileLog.e(e2);
                                }
                                resetPressedLink();
                            } else if (this.spoilerPressed != null) {
                                startSpoilerRipples(x, y, i5);
                            } else {
                                z = false;
                                z2 = true;
                            }
                            z = true;
                            z2 = true;
                        }
                    }
                    i4++;
                }
                if (motionEvent.getAction() != 0) {
                    if (motionEvent.getAction() == 1 && this.spoilerPressed != null) {
                        startSpoilerRipples(x, y, 0);
                        z = true;
                        z2 = true;
                        break;
                    }
                } else {
                    int dp2 = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
                    StaticLayout staticLayout2 = this.descriptionLayout;
                    if (staticLayout2 != null && x >= dp2 && x <= staticLayout2.getWidth() + dp2 && y >= (i2 = this.descriptionY) && y <= i2 + this.descriptionLayout.getHeight()) {
                        Iterator it2 = this.descriptionLayoutSpoilers.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            SpoilerEffect spoilerEffect2 = (SpoilerEffect) it2.next();
                            if (spoilerEffect2.getBounds().contains(x - dp2, y - this.descriptionY)) {
                                this.spoilerPressed = spoilerEffect2;
                                this.spoilerTypePressed = 1;
                                z = true;
                                z2 = true;
                                break;
                            }
                        }
                    }
                    StaticLayout staticLayout3 = this.descriptionLayout2;
                    if (staticLayout3 != null && x >= dp2 && x <= staticLayout3.getWidth() + dp2 && y >= (i = this.description2Y) && y <= i + this.descriptionLayout2.getHeight()) {
                        for (SpoilerEffect spoilerEffect3 : this.descriptionLayout2Spoilers) {
                            if (spoilerEffect3.getBounds().contains(x - dp2, y - this.description2Y)) {
                                this.spoilerPressed = spoilerEffect3;
                                this.spoilerTypePressed = 2;
                                z = true;
                                z2 = true;
                                break;
                            }
                        }
                    }
                }
            } else if (motionEvent.getAction() != 3) {
                z = false;
            }
            return z || super.onTouchEvent(motionEvent);
        }
        z = false;
        resetPressedLink();
        if (z) {
            return true;
        }
    }

    protected void resetPressedLink() {
        this.linksCollector.clear(true);
        this.pressedLinkIndex = -1;
        this.pressedLink = null;
        this.linkPreviewPressed = false;
        cancelCheckLongPress();
        invalidate();
    }

    public void setChecked(boolean z, boolean z2) {
        if (this.checkBox.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
        }
        this.checkBox.setChecked(z, z2);
    }

    public void setDelegate(SharedLinkCellDelegate sharedLinkCellDelegate) {
        this.delegate = sharedLinkCellDelegate;
    }

    public void setLink(MessageObject messageObject, boolean z) {
        this.needDivider = z;
        resetPressedLink();
        this.message = messageObject;
        requestLayout();
    }

    protected void startCheckLongPress() {
        if (this.checkingForLongPress) {
            return;
        }
        this.checkingForLongPress = true;
        if (this.pendingCheckForTap == null) {
            this.pendingCheckForTap = new CheckForTap();
        }
        postDelayed(this.pendingCheckForTap, ViewConfiguration.getTapTimeout());
    }
}
