package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ReplacementSpan;
import android.view.View;
import android.widget.FrameLayout;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class MessageContainsEmojiButton extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private int currentAccount;
    private AnimatedEmojiDrawable emojiDrawable;
    private CharSequence endText;
    private TLRPC$InputStickerSet inputStickerSet;
    private int lastLineHeight;
    private int lastLineMargin;
    private int lastLineTop;
    private CharSequence lastMainTextText;
    private int lastMainTextWidth;
    private CharSequence lastSecondPartText;
    private int lastSecondPartTextWidth;
    private ValueAnimator loadAnimator;
    private float loadT;
    private android.graphics.Rect loadingBoundsFrom;
    private android.graphics.Rect loadingBoundsTo;
    private LoadingDrawable loadingDrawable;
    private CharSequence mainText;
    private StaticLayout mainTextLayout;
    private Theme.ResourcesProvider resourcesProvider;
    private CharSequence secondPartText;
    private StaticLayout secondPartTextLayout;
    private TextPaint textPaint;
    private android.graphics.Rect emojiDrawableBounds = new android.graphics.Rect();
    private boolean loadingDrawableBoundsSet = false;
    private int lastWidth = -1;
    public boolean checkWidth = true;

    /* loaded from: classes3.dex */
    private class BoldAndAccent extends CharacterStyle {
        private BoldAndAccent() {
        }

        @Override // android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            int alpha = textPaint.getAlpha();
            textPaint.setColor(Theme.getColor("windowBackgroundWhiteBlueText", MessageContainsEmojiButton.this.resourcesProvider));
            textPaint.setAlpha(alpha);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:53:0x0107, code lost:
        r4 = null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MessageContainsEmojiButton(int i, Context context, Theme.ResourcesProvider resourcesProvider, ArrayList<TLRPC$InputStickerSet> arrayList, int i2) {
        super(context);
        String string;
        String str;
        TLRPC$Document tLRPC$Document;
        TLRPC$TL_messages_stickerSet stickerSet;
        TLRPC$StickerSet tLRPC$StickerSet;
        ArrayList<TLRPC$Document> arrayList2;
        String formatPluralString;
        this.loadT = 0.0f;
        this.currentAccount = i;
        setBackground(Theme.createRadSelectorDrawable(Theme.getColor("listSelectorSDK21", resourcesProvider), 0, 6));
        TextPaint textPaint = new TextPaint(1);
        this.textPaint = textPaint;
        textPaint.setTextSize(AndroidUtilities.dp(13.0f));
        this.textPaint.setColor(Theme.getColor("actionBarDefaultSubmenuItem", resourcesProvider));
        if (arrayList.size() > 1) {
            if (i2 == 0) {
                formatPluralString = LocaleController.formatPluralString("MessageContainsEmojiPacks", arrayList.size(), new Object[0]);
            } else {
                formatPluralString = LocaleController.formatPluralString("MessageContainsReactionsPacks", arrayList.size(), new Object[0]);
            }
            SpannableStringBuilder replaceTags = AndroidUtilities.replaceTags(formatPluralString);
            this.mainText = replaceTags;
            SpannableStringBuilder spannableStringBuilder = replaceTags;
            TypefaceSpan[] typefaceSpanArr = (TypefaceSpan[]) spannableStringBuilder.getSpans(0, replaceTags.length(), TypefaceSpan.class);
            for (int i3 = 0; typefaceSpanArr != null && i3 < typefaceSpanArr.length; i3++) {
                int spanStart = spannableStringBuilder.getSpanStart(typefaceSpanArr[i3]);
                int spanEnd = spannableStringBuilder.getSpanEnd(typefaceSpanArr[i3]);
                spannableStringBuilder.removeSpan(typefaceSpanArr[i3]);
                spannableStringBuilder.setSpan(new BoldAndAccent(), spanStart, spanEnd, 33);
            }
        } else if (arrayList.size() == 1) {
            if (i2 == 0) {
                string = LocaleController.getString("MessageContainsEmojiPack", R.string.MessageContainsEmojiPack);
            } else {
                string = LocaleController.getString("MessageContainsReactionsPack", R.string.MessageContainsReactionsPack);
            }
            String[] split = string.split("%s");
            if (split.length <= 1) {
                this.mainText = string;
                return;
            }
            TLRPC$InputStickerSet tLRPC$InputStickerSet = arrayList.get(0);
            this.inputStickerSet = tLRPC$InputStickerSet;
            if (tLRPC$InputStickerSet == null || (stickerSet = MediaDataController.getInstance(i).getStickerSet(this.inputStickerSet, false)) == null || (tLRPC$StickerSet = stickerSet.set) == null) {
                str = null;
                tLRPC$Document = null;
            } else {
                str = tLRPC$StickerSet.title;
                int i4 = 0;
                while (true) {
                    ArrayList<TLRPC$Document> arrayList3 = stickerSet.documents;
                    if (arrayList3 == null || i4 >= arrayList3.size()) {
                        break;
                    } else if (stickerSet.documents.get(i4).id == stickerSet.set.thumb_document_id) {
                        tLRPC$Document = stickerSet.documents.get(i4);
                        break;
                    } else {
                        i4++;
                    }
                }
                if (tLRPC$Document == null && (arrayList2 = stickerSet.documents) != null && arrayList2.size() > 0) {
                    tLRPC$Document = stickerSet.documents.get(0);
                }
            }
            if (str != null && tLRPC$Document != null) {
                SpannableString spannableString = new SpannableString(MessageObject.findAnimatedEmojiEmoticon(tLRPC$Document));
                spannableString.setSpan(new AnimatedEmojiSpan(tLRPC$Document, this.textPaint.getFontMetricsInt()) { // from class: org.telegram.ui.Components.MessageContainsEmojiButton.1
                    @Override // org.telegram.ui.Components.AnimatedEmojiSpan, android.text.style.ReplacementSpan
                    public void draw(Canvas canvas, CharSequence charSequence, int i5, int i6, float f, int i7, int i8, int i9, Paint paint) {
                        int i10 = i9 + i7;
                        int i11 = this.measuredSize;
                        MessageContainsEmojiButton.this.emojiDrawableBounds.set((int) f, (i10 - i11) / 2, (int) (f + i11), (i10 + i11) / 2);
                    }
                }, 0, spannableString.length(), 33);
                AnimatedEmojiDrawable make = AnimatedEmojiDrawable.make(i, 0, tLRPC$Document);
                this.emojiDrawable = make;
                make.addView(this);
                SpannableString spannableString2 = new SpannableString(str);
                spannableString2.setSpan(new BoldAndAccent(), 0, spannableString2.length(), 33);
                this.mainText = new SpannableStringBuilder().append((CharSequence) split[0]).append((CharSequence) spannableString).append(' ').append((CharSequence) spannableString2).append((CharSequence) split[1]);
                this.loadT = 1.0f;
                this.inputStickerSet = null;
                return;
            }
            this.mainText = split[0];
            this.endText = split[1];
            LoadingDrawable loadingDrawable = new LoadingDrawable(resourcesProvider);
            this.loadingDrawable = loadingDrawable;
            loadingDrawable.paint.setPathEffect(new CornerPathEffect(AndroidUtilities.dp(4.0f)));
        }
    }

    private int updateLayout(int i, boolean z) {
        StaticLayout staticLayout;
        float f;
        CharSequence charSequence = this.mainText;
        int i2 = 0;
        if (charSequence != this.lastMainTextText || this.lastMainTextWidth != i) {
            if (charSequence != null) {
                CharSequence charSequence2 = this.mainText;
                StaticLayout staticLayout2 = new StaticLayout(charSequence2, 0, charSequence2.length(), this.textPaint, i, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.mainTextLayout = staticLayout2;
                if (this.loadingDrawable != null && this.loadingBoundsTo == null) {
                    int lineCount = staticLayout2.getLineCount() - 1;
                    this.lastLineMargin = ((int) this.mainTextLayout.getPrimaryHorizontal(this.mainText.length())) + AndroidUtilities.dp(2.0f);
                    this.lastLineTop = this.mainTextLayout.getLineTop(lineCount);
                    this.lastLineHeight = r1 - this.lastLineTop;
                    float min = Math.min(AndroidUtilities.dp(100.0f), this.mainTextLayout.getWidth() - this.lastLineMargin);
                    if (this.loadingBoundsFrom == null) {
                        this.loadingBoundsFrom = new android.graphics.Rect();
                    }
                    this.loadingBoundsFrom.set(this.lastLineMargin, this.lastLineTop + AndroidUtilities.dp(1.25f), (int) (this.lastLineMargin + min), r1 + AndroidUtilities.dp(1.25f));
                    this.loadingDrawable.setBounds(this.loadingBoundsFrom);
                    this.loadingDrawableBoundsSet = true;
                }
            } else {
                this.mainTextLayout = null;
                this.loadingDrawableBoundsSet = false;
            }
            this.lastMainTextText = this.mainText;
            this.lastMainTextWidth = i;
        }
        CharSequence charSequence3 = this.secondPartText;
        if (charSequence3 != this.lastSecondPartText || this.lastSecondPartTextWidth != i) {
            if (charSequence3 != null) {
                CharSequence charSequence4 = this.secondPartText;
                this.secondPartTextLayout = new StaticLayout(charSequence4, 0, charSequence4.length(), this.textPaint, i, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            } else {
                this.secondPartTextLayout = null;
            }
            this.lastSecondPartText = this.secondPartText;
            this.lastSecondPartTextWidth = i;
        }
        StaticLayout staticLayout3 = this.mainTextLayout;
        if (staticLayout3 != null) {
            i2 = staticLayout3.getHeight();
        }
        if (this.secondPartTextLayout != null) {
            f = (staticLayout.getHeight() - this.lastLineHeight) * (z ? 1.0f : this.loadT);
        } else {
            f = 0.0f;
        }
        return i2 + ((int) f);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        setPadding(AndroidUtilities.dp(13.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(8.0f));
        int size = View.MeasureSpec.getSize(i);
        if (this.checkWidth && (i3 = this.lastWidth) > 0) {
            size = Math.min(size, i3);
        }
        this.lastWidth = size;
        int paddingLeft = (size - getPaddingLeft()) - getPaddingRight();
        if (paddingLeft < 0) {
            paddingLeft = 0;
        }
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(updateLayout(paddingLeft, false) + getPaddingTop() + getPaddingBottom(), 1073741824));
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        android.graphics.Rect rect;
        super.onDraw(canvas);
        if (this.mainTextLayout != null) {
            canvas.save();
            canvas.translate(getPaddingLeft(), getPaddingTop());
            this.textPaint.setAlpha(255);
            this.mainTextLayout.draw(canvas);
            LoadingDrawable loadingDrawable = this.loadingDrawable;
            if (loadingDrawable != null && this.loadingDrawableBoundsSet) {
                loadingDrawable.setAlpha((int) ((1.0f - this.loadT) * 255.0f));
                android.graphics.Rect rect2 = this.loadingBoundsFrom;
                if (rect2 != null && (rect = this.loadingBoundsTo) != null) {
                    float f = this.loadT;
                    android.graphics.Rect rect3 = AndroidUtilities.rectTmp2;
                    AndroidUtilities.lerp(rect2, rect, f, rect3);
                    this.loadingDrawable.setBounds(rect3);
                }
                this.loadingDrawable.draw(canvas);
                invalidate();
            }
            if (this.secondPartTextLayout != null) {
                canvas.save();
                canvas.translate(0.0f, this.lastLineTop);
                this.textPaint.setAlpha((int) (this.loadT * 255.0f));
                this.secondPartTextLayout.draw(canvas);
                canvas.restore();
            }
            AnimatedEmojiDrawable animatedEmojiDrawable = this.emojiDrawable;
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.setAlpha((int) (this.loadT * 255.0f));
                this.emojiDrawable.setBounds(this.emojiDrawableBounds);
                this.emojiDrawable.draw(canvas);
            }
            canvas.restore();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:49:0x0048, code lost:
        r1 = null;
     */
    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        TLRPC$TL_messages_stickerSet stickerSet;
        String str;
        TLRPC$Document tLRPC$Document;
        ArrayList<TLRPC$Document> arrayList;
        if (i != NotificationCenter.groupStickersDidLoad || this.inputStickerSet == null || (stickerSet = MediaDataController.getInstance(this.currentAccount).getStickerSet(this.inputStickerSet, false)) == null) {
            return;
        }
        TLRPC$StickerSet tLRPC$StickerSet = stickerSet.set;
        if (tLRPC$StickerSet != null) {
            str = tLRPC$StickerSet.title;
            int i3 = 0;
            while (true) {
                ArrayList<TLRPC$Document> arrayList2 = stickerSet.documents;
                if (arrayList2 == null || i3 >= arrayList2.size()) {
                    break;
                } else if (stickerSet.documents.get(i3).id == stickerSet.set.thumb_document_id) {
                    tLRPC$Document = stickerSet.documents.get(i3);
                    break;
                } else {
                    i3++;
                }
            }
            if (tLRPC$Document == null && (arrayList = stickerSet.documents) != null && arrayList.size() > 0) {
                tLRPC$Document = stickerSet.documents.get(0);
            }
        } else {
            str = null;
            tLRPC$Document = null;
        }
        if (str == null || tLRPC$Document == null) {
            return;
        }
        AnimatedEmojiDrawable make = AnimatedEmojiDrawable.make(this.currentAccount, 0, tLRPC$Document);
        this.emojiDrawable = make;
        make.addView(this);
        invalidate();
        SpannableString spannableString = new SpannableString(" ");
        spannableString.setSpan(new ReplacementSpan() { // from class: org.telegram.ui.Components.MessageContainsEmojiButton.2
            @Override // android.text.style.ReplacementSpan
            public void draw(Canvas canvas, CharSequence charSequence, int i4, int i5, float f, int i6, int i7, int i8, Paint paint) {
            }

            @Override // android.text.style.ReplacementSpan
            public int getSize(Paint paint, CharSequence charSequence, int i4, int i5, Paint.FontMetricsInt fontMetricsInt) {
                return MessageContainsEmojiButton.this.lastLineMargin;
            }
        }, 0, 1, 33);
        SpannableString spannableString2 = new SpannableString(MessageObject.findAnimatedEmojiEmoticon(tLRPC$Document));
        spannableString2.setSpan(new AnimatedEmojiSpan(tLRPC$Document, this.textPaint.getFontMetricsInt()) { // from class: org.telegram.ui.Components.MessageContainsEmojiButton.3
            @Override // org.telegram.ui.Components.AnimatedEmojiSpan, android.text.style.ReplacementSpan
            public void draw(Canvas canvas, CharSequence charSequence, int i4, int i5, float f, int i6, int i7, int i8, Paint paint) {
                int i9 = MessageContainsEmojiButton.this.lastLineTop;
                int i10 = i8 + i6;
                int i11 = this.measuredSize;
                MessageContainsEmojiButton.this.emojiDrawableBounds.set((int) f, i9 + ((i10 - i11) / 2), (int) (f + i11), MessageContainsEmojiButton.this.lastLineTop + ((i10 + this.measuredSize) / 2));
            }
        }, 0, spannableString2.length(), 33);
        SpannableString spannableString3 = new SpannableString(str);
        spannableString3.setSpan(new BoldAndAccent(), 0, spannableString3.length(), 33);
        this.secondPartText = new SpannableStringBuilder().append((CharSequence) spannableString).append((CharSequence) spannableString2).append(' ').append((CharSequence) spannableString3).append(this.endText);
        int measuredHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        int updateLayout = updateLayout((this.lastWidth - getPaddingLeft()) - getPaddingRight(), true);
        if (this.loadingBoundsFrom != null && this.secondPartTextLayout != null) {
            if (this.loadingBoundsTo == null) {
                this.loadingBoundsTo = new android.graphics.Rect();
            }
            StaticLayout staticLayout = this.secondPartTextLayout;
            float primaryHorizontal = staticLayout.getPrimaryHorizontal(staticLayout.getLineEnd(0));
            android.graphics.Rect rect = this.loadingBoundsTo;
            android.graphics.Rect rect2 = this.loadingBoundsFrom;
            rect.set(rect2.left, rect2.top, (int) primaryHorizontal, rect2.bottom);
        }
        this.inputStickerSet = null;
        ValueAnimator valueAnimator = this.loadAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        final boolean z = Math.abs(measuredHeight - updateLayout) > AndroidUtilities.dp(3.0f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.loadT, 1.0f);
        this.loadAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.MessageContainsEmojiButton$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                MessageContainsEmojiButton.this.lambda$didReceivedNotification$0(z, valueAnimator2);
            }
        });
        this.loadAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.loadAnimator.setStartDelay(150L);
        this.loadAnimator.setDuration(400L);
        this.loadAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$0(boolean z, ValueAnimator valueAnimator) {
        this.loadT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
        if (z) {
            requestLayout();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AnimatedEmojiDrawable animatedEmojiDrawable = this.emojiDrawable;
        if (animatedEmojiDrawable != null) {
            animatedEmojiDrawable.removeView(this);
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.groupStickersDidLoad);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AnimatedEmojiDrawable animatedEmojiDrawable = this.emojiDrawable;
        if (animatedEmojiDrawable != null) {
            animatedEmojiDrawable.addView(this);
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.groupStickersDidLoad);
    }
}