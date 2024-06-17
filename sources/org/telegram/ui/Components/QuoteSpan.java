package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineHeightSpan;
import android.text.style.MetricAffectingSpan;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$TL_messageEntityBlockquote;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedTextView;
/* loaded from: classes3.dex */
public class QuoteSpan implements LeadingMarginSpan {
    public static int COLLAPSE_LINES = 3;
    public boolean adaptLineHeight = true;
    private final Paint backgroundPaint;
    private final Path backgroundPath;
    private final float[] backgroundPathRadii;
    public QuoteCollapsedPart collapsedSpan;
    private int color;
    public final boolean edit;
    public int end;
    private ButtonBounce expandBounce;
    private ExpandDrawable expandDrawable;
    private boolean expandPressed;
    private AnimatedFloat expandScale;
    private AnimatedTextView.AnimatedTextDrawable expandText;
    private boolean expandTextCollapsed;
    private int expandTextWidth;
    public boolean first;
    public boolean isCollapsing;
    public boolean last;
    private final Paint linePaint;
    private final Path linePath;
    private final float[] linePathRadii;
    private SpannableString newline;
    private final Drawable quoteDrawable;
    public boolean rtl;
    public boolean singleLine;
    public int start;
    public final QuoteStyleSpan styleSpan;

    /* loaded from: classes3.dex */
    public static class QuoteButtonNewLineSpan extends CharacterStyle {
        @Override // android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
        }
    }

    @Override // android.text.style.LeadingMarginSpan
    public void drawLeadingMargin(Canvas canvas, Paint paint, int i, int i2, int i3, int i4, int i5, CharSequence charSequence, int i6, int i7, boolean z, Layout layout) {
    }

    public QuoteSpan(boolean z, boolean z2, QuoteStyleSpan quoteStyleSpan) {
        Paint paint = new Paint(1);
        this.backgroundPaint = paint;
        this.backgroundPathRadii = new float[8];
        this.backgroundPath = new Path();
        Paint paint2 = new Paint(1);
        this.linePaint = paint2;
        this.linePathRadii = new float[8];
        this.linePath = new Path();
        this.color = -1;
        this.edit = z;
        this.styleSpan = quoteStyleSpan;
        this.isCollapsing = z2;
        this.quoteDrawable = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.mini_quote).mutate();
        paint2.setColor(this.color);
        paint.setColor(ColorUtils.setAlphaComponent(this.color, 30));
    }

    public void setColor(int i) {
        if (this.color != i) {
            Drawable drawable = this.quoteDrawable;
            this.color = i;
            drawable.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN));
            this.linePaint.setColor(i);
            this.backgroundPaint.setColor(ColorUtils.setAlphaComponent(i, 30));
        }
    }

    @Override // android.text.style.LeadingMarginSpan
    public int getLeadingMargin(boolean z) {
        return AndroidUtilities.dp(this.adaptLineHeight ? 8.0f : 10.0f);
    }

    /* loaded from: classes3.dex */
    public static class QuoteStyleSpan extends MetricAffectingSpan implements LineHeightSpan {
        public QuoteSpan span;

        @Override // android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            if (textPaint == null) {
                return;
            }
            textPaint.setTextSize(AndroidUtilities.dp(this.span.edit ? 16.0f : SharedConfig.fontSize - 2));
        }

        @Override // android.text.style.MetricAffectingSpan
        public void updateMeasureState(TextPaint textPaint) {
            textPaint.setTextSize(AndroidUtilities.dp(this.span.edit ? 16.0f : SharedConfig.fontSize - 2));
            textPaint.setTextScaleX(this.span.edit ? 1.1f : 1.0f);
        }

        @Override // android.text.style.LineHeightSpan
        public void chooseHeight(CharSequence charSequence, int i, int i2, int i3, int i4, Paint.FontMetricsInt fontMetricsInt) {
            QuoteSpan quoteSpan = this.span;
            if (quoteSpan.adaptLineHeight) {
                int i5 = quoteSpan.singleLine ? 7 : 2;
                if (i <= quoteSpan.start) {
                    fontMetricsInt.ascent -= AndroidUtilities.dp((quoteSpan.last ? 2 : 0) + i5);
                    fontMetricsInt.top -= AndroidUtilities.dp((this.span.last ? 2 : 0) + i5);
                }
                if (i2 >= this.span.end) {
                    float f = i5;
                    fontMetricsInt.descent += AndroidUtilities.dp(f);
                    fontMetricsInt.bottom += AndroidUtilities.dp(f);
                }
            }
        }
    }

    public static int putQuote(Spannable spannable, int i, int i2, boolean z) {
        if (spannable == null) {
            return -1;
        }
        QuoteSpan[] quoteSpanArr = (QuoteSpan[]) spannable.getSpans(i, i2, QuoteSpan.class);
        if (quoteSpanArr == null || quoteSpanArr.length <= 0) {
            int clamp = Utilities.clamp(i, spannable.length(), 0);
            int clamp2 = Utilities.clamp(i2, spannable.length(), 0);
            QuoteStyleSpan quoteStyleSpan = new QuoteStyleSpan();
            QuoteSpan quoteSpan = new QuoteSpan(false, z, quoteStyleSpan);
            quoteStyleSpan.span = quoteSpan;
            quoteSpan.start = clamp;
            quoteSpan.end = clamp2;
            spannable.setSpan(quoteStyleSpan, clamp, clamp2, 33);
            spannable.setSpan(quoteSpan, clamp, clamp2, 33);
            return clamp2;
        }
        return -1;
    }

    public static int putQuoteToEditable(Editable editable, int i, int i2, boolean z) {
        if (editable == null) {
            return -1;
        }
        int clamp = Utilities.clamp(i, editable.length(), 0);
        int clamp2 = Utilities.clamp(i2, editable.length(), 0);
        if (clamp > 0 && editable.charAt(clamp - 1) != '\n') {
            editable.insert(clamp, "\n");
            clamp++;
            clamp2++;
        }
        int i3 = clamp2 + 1;
        if (clamp2 >= editable.length() || editable.charAt(clamp2) != '\n') {
            editable.insert(clamp2, "\n");
        }
        QuoteStyleSpan quoteStyleSpan = new QuoteStyleSpan();
        QuoteSpan quoteSpan = new QuoteSpan(true, z, quoteStyleSpan);
        quoteStyleSpan.span = quoteSpan;
        quoteSpan.start = clamp;
        quoteSpan.end = clamp2;
        editable.setSpan(quoteSpan, clamp, clamp2, 33);
        editable.setSpan(quoteStyleSpan, clamp, clamp2, 33);
        editable.insert(clamp2, "\ufeff");
        editable.delete(clamp2, i3);
        return i3;
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x0110  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ArrayList<Block> updateQuoteBlocks(View view, Layout layout, ArrayList<Block> arrayList, boolean[] zArr) {
        int lineStart;
        QuoteSpan quoteSpan;
        int i;
        boolean z;
        int i2;
        if (layout == null) {
            if (arrayList != null) {
                arrayList.clear();
            }
            return arrayList;
        }
        CharSequence text = layout.getText();
        if (text == null || !(text instanceof Spannable)) {
            if (arrayList != null) {
                arrayList.clear();
            }
            return arrayList;
        }
        Spannable spannable = (Spannable) text;
        if (arrayList != null) {
            arrayList.clear();
        }
        QuoteSpan[] quoteSpanArr = (QuoteSpan[]) spannable.getSpans(0, spannable.length(), QuoteSpan.class);
        ArrayList<Block> arrayList2 = arrayList;
        for (int i3 = 0; i3 < quoteSpanArr.length; i3++) {
            boolean z2 = quoteSpanArr[i3].last;
            Block block = new Block(view, layout, spannable, quoteSpanArr[i3]);
            QuoteSpan quoteSpan2 = block.span;
            if (quoteSpan2.edit) {
                int i4 = quoteSpan2.start;
                if (i4 != 0 && text.charAt(i4 - 1) != '\n') {
                    spannable.removeSpan(quoteSpanArr[i3]);
                    spannable.removeSpan(quoteSpanArr[i3].styleSpan);
                    if (quoteSpanArr[i3].collapsedSpan != null) {
                        spannable.removeSpan(quoteSpanArr[i3].collapsedSpan);
                    }
                } else {
                    if (block.span.end != text.length() && text.charAt(block.span.end) != '\n') {
                        int i5 = block.span.end;
                        while (i5 <= text.length() && i5 != text.length() && text.charAt(i5) != '\n') {
                            i5++;
                        }
                        spannable.removeSpan(quoteSpanArr[i3]);
                        spannable.removeSpan(quoteSpanArr[i3].styleSpan);
                        spannable.setSpan(quoteSpanArr[i3], block.span.start, i5, 33);
                        spannable.setSpan(quoteSpanArr[i3].styleSpan, block.span.start, i5, 33);
                        block = new Block(view, layout, spannable, quoteSpanArr[i3]);
                    }
                    if (spannable instanceof SpannableStringBuilder) {
                        SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) spannable;
                        int i6 = block.span.end;
                        boolean z3 = i6 + (-1) >= 0 && spannableStringBuilder.charAt(i6 + (-1)) == '\n';
                        if (block.hasButton()) {
                            int i7 = block.span.end;
                            if (i7 - 2 >= 0 && layout.getLineRight(layout.getLineForOffset(i7 - 1)) - AndroidUtilities.dp(12.0f) > block.width - block.buttonWidth()) {
                                z = true;
                                if (z3 != z) {
                                    int i8 = block.span.end;
                                    if (z3) {
                                        i2 = i8 - 1;
                                        spannableStringBuilder.delete(i8 - 1, i8);
                                    } else {
                                        i2 = i8 + 2;
                                        boolean z4 = Selection.getSelectionStart(spannableStringBuilder) == block.span.end && Selection.getSelectionStart(spannableStringBuilder) == Selection.getSelectionEnd(spannableStringBuilder);
                                        QuoteSpan quoteSpan3 = block.span;
                                        spannableStringBuilder.insert(quoteSpan3.end, (CharSequence) quoteSpan3.getNewlineHack());
                                        if (z4) {
                                            int selectionStart = Selection.getSelectionStart(spannableStringBuilder);
                                            int i9 = block.span.end;
                                            if (selectionStart != i9) {
                                                Selection.setSelection(spannableStringBuilder, i9, i9);
                                            }
                                        }
                                    }
                                    block.span.end = Math.min(i2, spannable.length());
                                    spannable.removeSpan(quoteSpanArr[i3]);
                                    spannable.removeSpan(quoteSpanArr[i3].styleSpan);
                                    QuoteSpan quoteSpan4 = quoteSpanArr[i3];
                                    QuoteSpan quoteSpan5 = block.span;
                                    spannable.setSpan(quoteSpan4, quoteSpan5.start, quoteSpan5.end, 33);
                                    QuoteStyleSpan quoteStyleSpan = quoteSpanArr[i3].styleSpan;
                                    QuoteSpan quoteSpan6 = block.span;
                                    spannable.setSpan(quoteStyleSpan, quoteSpan6.start, quoteSpan6.end, 33);
                                    if (zArr != null) {
                                        zArr[0] = true;
                                    }
                                }
                            }
                        }
                        z = false;
                        if (z3 != z) {
                        }
                    }
                    QuoteCollapsedPart quoteCollapsedPart = block.span.collapsedSpan;
                    if (quoteCollapsedPart != null) {
                        spannable.removeSpan(quoteCollapsedPart);
                    }
                    QuoteSpan quoteSpan7 = block.span;
                    if (quoteSpan7.isCollapsing && (lineStart = layout.getLineStart(Math.min(layout.getLineForOffset(quoteSpan7.start) + COLLAPSE_LINES, layout.getLineCount()))) < (i = (quoteSpan = block.span).end)) {
                        if (quoteSpan.collapsedSpan == null) {
                            quoteSpan.collapsedSpan = new QuoteCollapsedPart(block.span);
                        }
                        spannable.setSpan(block.span.collapsedSpan, lineStart, i, 33);
                    }
                }
            }
            if (arrayList2 == null) {
                arrayList2 = new ArrayList<>();
            }
            if (quoteSpanArr[i3].last != z2 && zArr != null) {
                zArr[0] = true;
            }
            arrayList2.add(block);
        }
        return arrayList2;
    }

    public static ArrayList<Block> updateQuoteBlocksSpanned(Layout layout, ArrayList<Block> arrayList) {
        if (layout == null) {
            if (arrayList != null) {
                arrayList.clear();
            }
            return arrayList;
        }
        CharSequence text = layout.getText();
        if (text == null || !(text instanceof Spanned)) {
            if (arrayList != null) {
                arrayList.clear();
            }
            return arrayList;
        }
        Spanned spanned = (Spanned) text;
        if (arrayList != null) {
            arrayList.clear();
        }
        QuoteSpan[] quoteSpanArr = (QuoteSpan[]) spanned.getSpans(0, spanned.length(), QuoteSpan.class);
        for (int i = 0; i < quoteSpanArr.length; i++) {
            boolean z = quoteSpanArr[i].last;
            Block block = new Block(null, layout, spanned, quoteSpanArr[i]);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
            }
            arrayList.add(block);
        }
        return arrayList;
    }

    /* loaded from: classes3.dex */
    public static class Block {
        public final int bottom;
        public RectF collapseButtonBounds;
        public final TextPaint paint;
        public final QuoteSpan span;
        public final int top;
        public final View view;
        public final int width;

        public Block(View view, Layout layout, Spanned spanned, QuoteSpan quoteSpan) {
            int i;
            int i2;
            this.view = view;
            this.span = quoteSpan;
            this.paint = layout.getPaint();
            quoteSpan.start = spanned.getSpanStart(quoteSpan);
            int spanEnd = spanned.getSpanEnd(quoteSpan);
            quoteSpan.end = spanEnd;
            if (spanEnd - 1 >= 0 && spanEnd < spanned.length() && spanned.charAt(quoteSpan.end) != '\n' && spanned.charAt(quoteSpan.end - 1) == '\n') {
                quoteSpan.end--;
            }
            int lineForOffset = layout.getLineForOffset(quoteSpan.start);
            int lineForOffset2 = layout.getLineForOffset(quoteSpan.end);
            quoteSpan.singleLine = lineForOffset2 - lineForOffset < 1;
            quoteSpan.first = lineForOffset <= 0;
            quoteSpan.last = lineForOffset2 + 1 >= layout.getLineCount();
            if (quoteSpan.edit) {
                int lineTop = layout.getLineTop(lineForOffset);
                if (quoteSpan.singleLine) {
                    i = 0;
                } else {
                    i = (quoteSpan.first ? 2 : 0) + 3;
                }
                this.top = lineTop + AndroidUtilities.dp(3 - i);
                int lineBottom = layout.getLineBottom(lineForOffset2);
                if (quoteSpan.singleLine) {
                    i2 = 0;
                } else {
                    i2 = (quoteSpan.last ? 2 : 0) + 3;
                }
                this.bottom = lineBottom - AndroidUtilities.dp(2 - i2);
            } else {
                this.top = layout.getLineTop(lineForOffset) + AndroidUtilities.dp(3 - (quoteSpan.singleLine ? 1 : 2));
                this.bottom = layout.getLineBottom(lineForOffset2) - AndroidUtilities.dp(2 - (quoteSpan.singleLine ? 1 : 2));
            }
            quoteSpan.rtl = false;
            float f = 0.0f;
            while (lineForOffset <= lineForOffset2) {
                f = Math.max(f, layout.getLineRight(lineForOffset));
                if (layout.getLineLeft(lineForOffset) > 0.0f) {
                    quoteSpan.rtl = true;
                }
                lineForOffset++;
            }
            this.width = (int) Math.ceil(f);
            if (!quoteSpan.edit || view == null) {
                return;
            }
            if (quoteSpan.expandScale == null) {
                quoteSpan.expandScale = new AnimatedFloat(view, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
            }
            if (quoteSpan.expandDrawable == null) {
                quoteSpan.expandDrawable = new ExpandDrawable(view);
            }
            if (quoteSpan.expandText == null) {
                quoteSpan.expandText = new AnimatedTextView.AnimatedTextDrawable();
                quoteSpan.expandText.setTextSize(AndroidUtilities.dp(11.0f));
                quoteSpan.expandText.setHacks(true, true, true);
                quoteSpan.expandText.setCallback(view);
                quoteSpan.expandText.setOverrideFullWidth((int) (AndroidUtilities.displaySize.x * 0.3f));
                quoteSpan.expandText.setText(LocaleController.getString(quoteSpan.expandTextCollapsed = false ? R.string.QuoteExpand : R.string.QuoteCollapse), false);
                quoteSpan.expandTextWidth = (int) Math.ceil(Math.max(quoteSpan.expandText.getPaint().measureText(LocaleController.getString(R.string.QuoteExpand)), quoteSpan.expandText.getPaint().measureText(LocaleController.getString(R.string.QuoteCollapse))));
            }
            if (quoteSpan.expandBounce == null) {
                quoteSpan.expandBounce = new ButtonBounce(view);
            }
        }

        public void draw(Canvas canvas, float f, int i, int i2, float f2, TextPaint textPaint) {
            QuoteSpan quoteSpan;
            int i3;
            this.span.setColor(i2);
            int dp = this.span.edit ? i : this.width + AndroidUtilities.dp(32.0f);
            double d = i;
            Double.isNaN(d);
            if (dp >= d * 0.95d) {
                dp = i;
            }
            canvas.save();
            canvas.translate(0.0f, f);
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(0.0f, this.top, dp, this.bottom);
            float[] fArr = this.span.backgroundPathRadii;
            float[] fArr2 = this.span.backgroundPathRadii;
            float[] fArr3 = this.span.backgroundPathRadii;
            this.span.backgroundPathRadii[7] = 0.0f;
            fArr3[6] = 0.0f;
            fArr2[1] = 0.0f;
            fArr[0] = 0.0f;
            float[] fArr4 = this.span.backgroundPathRadii;
            float[] fArr5 = this.span.backgroundPathRadii;
            float[] fArr6 = this.span.backgroundPathRadii;
            float[] fArr7 = this.span.backgroundPathRadii;
            float dp2 = AndroidUtilities.dp(4.0f);
            fArr7[5] = dp2;
            fArr6[4] = dp2;
            fArr5[3] = dp2;
            fArr4[2] = dp2;
            this.span.backgroundPath.rewind();
            this.span.backgroundPath.addRoundRect(rectF, this.span.backgroundPathRadii, Path.Direction.CW);
            canvas.drawPath(this.span.backgroundPath, this.span.backgroundPaint);
            QuoteSpan quoteSpan2 = this.span;
            if (quoteSpan2.edit && this.view != null) {
                if (quoteSpan2.isCollapsing != quoteSpan2.expandTextCollapsed) {
                    AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.span.expandText;
                    QuoteSpan quoteSpan3 = this.span;
                    animatedTextDrawable.setText(LocaleController.getString(quoteSpan3.expandTextCollapsed = quoteSpan3.isCollapsing ? R.string.QuoteExpand : R.string.QuoteCollapse), true);
                }
                int dp3 = (int) (AndroidUtilities.dp(23.66f) + this.span.expandText.getCurrentWidth());
                int dp4 = AndroidUtilities.dp(17.66f);
                int dp5 = AndroidUtilities.dp(3.333f);
                if (this.collapseButtonBounds == null) {
                    this.collapseButtonBounds = new RectF();
                }
                int i4 = this.bottom;
                float f3 = dp - dp5;
                this.collapseButtonBounds.set(i3 - dp3, (i4 - dp5) - dp4, f3, i4 - dp5);
                float scale = this.span.expandScale.set(hasButton()) * this.span.expandBounce.getScale(0.02f);
                if (scale > 0.0f) {
                    canvas.save();
                    canvas.scale(scale, scale, f3, this.bottom - dp5);
                    float f4 = dp4 / 2.0f;
                    canvas.drawRoundRect(this.collapseButtonBounds, f4, f4, this.span.backgroundPaint);
                    RectF rectF2 = this.collapseButtonBounds;
                    this.span.expandText.setBounds((int) (this.collapseButtonBounds.left + AndroidUtilities.dp(6.0f)), (int) rectF2.top, (int) (rectF2.right - AndroidUtilities.dp(17.66f)), (int) this.collapseButtonBounds.bottom);
                    this.span.expandText.setTextColor(i2);
                    this.span.expandText.draw(canvas);
                    float dp6 = AndroidUtilities.dp(14.0f);
                    float f5 = dp6 / 2.0f;
                    this.span.expandDrawable.setBounds((int) ((this.collapseButtonBounds.right - AndroidUtilities.dp(3.33f)) - dp6), (int) ((this.collapseButtonBounds.centerY() - f5) + AndroidUtilities.dp(0.33f)), (int) (this.collapseButtonBounds.right - AndroidUtilities.dp(3.33f)), (int) (this.collapseButtonBounds.centerY() + f5 + AndroidUtilities.dp(0.33f)));
                    this.span.expandDrawable.setColor(i2);
                    this.span.expandDrawable.setState(!this.span.isCollapsing);
                    this.span.expandDrawable.draw(canvas);
                    canvas.restore();
                }
            }
            rectF.set(-AndroidUtilities.dp(3.0f), this.top, 0.0f, this.bottom);
            float[] fArr8 = this.span.linePathRadii;
            float[] fArr9 = this.span.linePathRadii;
            float[] fArr10 = this.span.linePathRadii;
            float[] fArr11 = this.span.linePathRadii;
            float dp7 = AndroidUtilities.dp(4.0f);
            fArr11[7] = dp7;
            fArr10[6] = dp7;
            fArr9[1] = dp7;
            fArr8[0] = dp7;
            float[] fArr12 = this.span.linePathRadii;
            float[] fArr13 = this.span.linePathRadii;
            float[] fArr14 = this.span.linePathRadii;
            this.span.linePathRadii[5] = 0.0f;
            fArr14[4] = 0.0f;
            fArr13[3] = 0.0f;
            fArr12[2] = 0.0f;
            this.span.linePath.rewind();
            this.span.linePath.addRoundRect(rectF, this.span.linePathRadii, Path.Direction.CW);
            canvas.drawPath(this.span.linePath, this.span.linePaint);
            if (!this.span.rtl) {
                int intrinsicHeight = (int) (((this.top + this.bottom) - quoteSpan.quoteDrawable.getIntrinsicHeight()) / 2.0f);
                if (intrinsicHeight > this.top + AndroidUtilities.dp(8.0f)) {
                    intrinsicHeight = this.top + AndroidUtilities.dp(4.0f);
                }
                this.span.quoteDrawable.setBounds((dp - this.span.quoteDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(4.0f), intrinsicHeight, dp - AndroidUtilities.dp(4.0f), this.span.quoteDrawable.getIntrinsicHeight() + intrinsicHeight);
                this.span.quoteDrawable.setAlpha((int) (255.0f * f2));
                this.span.quoteDrawable.draw(canvas);
            }
            canvas.restore();
        }

        public int buttonWidth() {
            return AndroidUtilities.dp(23.66f) + this.span.expandTextWidth + (AndroidUtilities.dp(3.333f) * 2);
        }

        public boolean hasButton() {
            return this.span.edit && ((float) (this.bottom - this.top)) > (this.paint.getTextSize() * 1.3f) * ((float) QuoteSpan.COLLAPSE_LINES);
        }
    }

    public static boolean onTouch(MotionEvent motionEvent, int i, ArrayList<Block> arrayList, Runnable runnable) {
        boolean z;
        if (arrayList == null) {
            return false;
        }
        Iterator<Block> it = arrayList.iterator();
        while (true) {
            while (it.hasNext()) {
                Block next = it.next();
                boolean z2 = next.hasButton() && next.collapseButtonBounds.contains(motionEvent.getX(), motionEvent.getY() - ((float) i));
                if (motionEvent.getAction() == 0) {
                    QuoteSpan quoteSpan = next.span;
                    quoteSpan.expandPressed = z2;
                    ButtonBounce buttonBounce = quoteSpan.expandBounce;
                    if (buttonBounce != null) {
                        buttonBounce.setPressed(z2);
                    }
                } else if (motionEvent.getAction() == 1) {
                    QuoteSpan quoteSpan2 = next.span;
                    if (quoteSpan2.expandPressed && z2) {
                        quoteSpan2.isCollapsing = !quoteSpan2.isCollapsing;
                        if (runnable != null) {
                            runnable.run();
                        }
                        z = true;
                    }
                    QuoteSpan quoteSpan3 = next.span;
                    quoteSpan3.expandPressed = false;
                    ButtonBounce buttonBounce2 = quoteSpan3.expandBounce;
                    if (buttonBounce2 != null) {
                        buttonBounce2.setPressed(false);
                    }
                } else if (motionEvent.getAction() == 3) {
                    QuoteSpan quoteSpan4 = next.span;
                    quoteSpan4.expandPressed = false;
                    ButtonBounce buttonBounce3 = quoteSpan4.expandBounce;
                    if (buttonBounce3 != null) {
                        buttonBounce3.setPressed(false);
                    }
                }
                z = next.span.expandPressed || z;
            }
            return z;
        }
    }

    public static void mergeQuotes(SpannableStringBuilder spannableStringBuilder, ArrayList<TLRPC$MessageEntity> arrayList) {
        boolean z;
        if (arrayList == null || !(spannableStringBuilder instanceof Spanned)) {
            return;
        }
        TreeSet treeSet = new TreeSet();
        HashMap hashMap = new HashMap();
        int i = 0;
        while (true) {
            if (i >= arrayList.size()) {
                break;
            }
            TLRPC$MessageEntity tLRPC$MessageEntity = arrayList.get(i);
            if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length <= spannableStringBuilder.length()) {
                int i2 = tLRPC$MessageEntity.offset;
                int i3 = tLRPC$MessageEntity.length + i2;
                if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBlockquote) {
                    treeSet.add(Integer.valueOf(i2));
                    treeSet.add(Integer.valueOf(i3));
                    hashMap.put(Integer.valueOf(i2), Integer.valueOf((hashMap.containsKey(Integer.valueOf(i2)) ? ((Integer) hashMap.get(Integer.valueOf(i2))).intValue() : 0) | (tLRPC$MessageEntity.collapsed ? 16 : 1)));
                    hashMap.put(Integer.valueOf(i3), Integer.valueOf((hashMap.containsKey(Integer.valueOf(i3)) ? ((Integer) hashMap.get(Integer.valueOf(i3))).intValue() : 0) | 2));
                }
            }
            i++;
        }
        Iterator it = treeSet.iterator();
        int i4 = 0;
        int i5 = 0;
        loop1: while (true) {
            z = false;
            while (it.hasNext()) {
                int intValue = ((Integer) it.next()).intValue();
                int intValue2 = ((Integer) hashMap.get(Integer.valueOf(intValue))).intValue();
                if (i4 != intValue) {
                    int i6 = intValue - 1;
                    int i7 = (i6 < 0 || i6 >= spannableStringBuilder.length() || spannableStringBuilder.charAt(i6) != '\n') ? intValue : intValue - 1;
                    if (i5 > 0) {
                        putQuoteToEditable(spannableStringBuilder, i4, i7, z);
                    }
                    i4 = intValue + 1;
                    if (i4 >= spannableStringBuilder.length() || spannableStringBuilder.charAt(intValue) != '\n') {
                        i4 = intValue;
                    }
                }
                if ((intValue2 & 2) != 0) {
                    i5--;
                }
                if ((intValue2 & 1) != 0 || (intValue2 & 16) != 0) {
                    i5++;
                    z = (intValue2 & 16) != 0 ? true : true;
                }
            }
        }
        if (i4 >= spannableStringBuilder.length() || i5 <= 0) {
            return;
        }
        putQuoteToEditable(spannableStringBuilder, i4, spannableStringBuilder.length(), z);
    }

    public static void normalizeQuotes(Editable editable) {
        boolean z;
        if (editable == null) {
            return;
        }
        TreeSet treeSet = new TreeSet();
        HashMap hashMap = new HashMap();
        QuoteStyleSpan[] quoteStyleSpanArr = (QuoteStyleSpan[]) editable.getSpans(0, editable.length(), QuoteStyleSpan.class);
        int i = 0;
        while (true) {
            if (i >= quoteStyleSpanArr.length) {
                break;
            }
            QuoteStyleSpan quoteStyleSpan = quoteStyleSpanArr[i];
            int spanStart = editable.getSpanStart(quoteStyleSpan);
            int spanEnd = editable.getSpanEnd(quoteStyleSpan);
            treeSet.add(Integer.valueOf(spanStart));
            hashMap.put(Integer.valueOf(spanStart), Integer.valueOf((quoteStyleSpan.span.isCollapsing ? 16 : 1) | (hashMap.containsKey(Integer.valueOf(spanStart)) ? ((Integer) hashMap.get(Integer.valueOf(spanStart))).intValue() : 0)));
            treeSet.add(Integer.valueOf(spanEnd));
            hashMap.put(Integer.valueOf(spanEnd), Integer.valueOf((hashMap.containsKey(Integer.valueOf(spanEnd)) ? ((Integer) hashMap.get(Integer.valueOf(spanEnd))).intValue() : 0) | 2));
            editable.removeSpan(quoteStyleSpan);
            editable.removeSpan(quoteStyleSpan.span);
            i++;
        }
        Iterator it = treeSet.iterator();
        int i2 = 0;
        int i3 = 0;
        loop1: while (true) {
            z = false;
            while (it.hasNext()) {
                int intValue = ((Integer) it.next()).intValue();
                int intValue2 = ((Integer) hashMap.get(Integer.valueOf(intValue))).intValue();
                if (i2 != intValue) {
                    int i4 = intValue - 1;
                    int i5 = (i4 < 0 || i4 >= editable.length() || editable.charAt(i4) != '\n') ? intValue : intValue - 1;
                    if (i3 > 0) {
                        putQuoteToEditable(editable, i2, i5, z);
                    }
                    i2 = intValue + 1;
                    if (i2 >= editable.length() || editable.charAt(intValue) != '\n') {
                        i2 = intValue;
                    }
                }
                if ((intValue2 & 2) != 0) {
                    i3--;
                }
                if ((intValue2 & 1) != 0 || (intValue2 & 16) != 0) {
                    i3++;
                    z = (intValue2 & 16) != 0 ? true : true;
                }
            }
        }
        if (i2 >= editable.length() || i3 <= 0) {
            return;
        }
        putQuoteToEditable(editable, i2, editable.length(), z);
    }

    public SpannableString getNewlineHack() {
        if (this.newline == null) {
            SpannableString spannableString = new SpannableString("\n");
            this.newline = spannableString;
            spannableString.setSpan(new QuoteButtonNewLineSpan(), 0, this.newline.length(), 33);
        }
        return this.newline;
    }

    public static CharSequence stripNewlineHacks(CharSequence charSequence) {
        if (charSequence == null) {
            return null;
        }
        if (charSequence instanceof Spanned) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
            QuoteButtonNewLineSpan[] quoteButtonNewLineSpanArr = (QuoteButtonNewLineSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), QuoteButtonNewLineSpan.class);
            for (int length = quoteButtonNewLineSpanArr.length - 1; length >= 0; length--) {
                QuoteButtonNewLineSpan quoteButtonNewLineSpan = quoteButtonNewLineSpanArr[length];
                int spanStart = spannableStringBuilder.getSpanStart(quoteButtonNewLineSpan);
                int spanEnd = spannableStringBuilder.getSpanEnd(quoteButtonNewLineSpan);
                spannableStringBuilder.removeSpan(quoteButtonNewLineSpan);
                spannableStringBuilder.delete(spanStart, spanEnd);
            }
            return spannableStringBuilder;
        }
        return charSequence;
    }

    /* loaded from: classes3.dex */
    public static class QuoteCollapsedPart extends CharacterStyle {
        private final QuoteSpan span;

        public QuoteCollapsedPart(QuoteSpan quoteSpan) {
            this.span = quoteSpan;
        }

        @Override // android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            textPaint.setColor(Theme.blendOver(Theme.multAlpha(textPaint.getColor(), 0.55f), Theme.multAlpha(this.span.color, 0.4f)));
        }
    }

    /* loaded from: classes3.dex */
    public static class ExpandDrawable extends Drawable {
        private int alpha;
        private final AnimatedFloat animatedState;
        private final Paint paint;
        private final Path path;
        private boolean state;
        private final View view;

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public ExpandDrawable(View view) {
            Paint paint = new Paint(1);
            this.paint = paint;
            Path path = new Path();
            this.path = path;
            this.alpha = 255;
            this.view = view;
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(AndroidUtilities.dp(1.0f));
            this.animatedState = new AnimatedFloat(view, 0L, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
            float dpf2 = AndroidUtilities.dpf2(4.66f);
            float dpf22 = AndroidUtilities.dpf2(2.16f);
            path.rewind();
            path.moveTo(dpf2 / 2.0f, 0.0f);
            float f = (-dpf2) / 2.0f;
            path.lineTo(f, 0.0f);
            float f2 = f + dpf22;
            path.lineTo(f2, -dpf22);
            path.moveTo(f, 0.0f);
            path.lineTo(f2, dpf22);
        }

        public void setColor(int i) {
            this.paint.setColor(i);
            this.paint.setAlpha(this.alpha);
        }

        public void setState(boolean z) {
            if (this.state != z) {
                this.state = z;
                this.view.invalidate();
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            int centerX = getBounds().centerX();
            int centerY = getBounds().centerY();
            float f = this.animatedState.set(this.state);
            float dpf2 = AndroidUtilities.dpf2(2.51f);
            canvas.save();
            canvas.translate(centerX, centerY);
            canvas.save();
            canvas.translate(dpf2, dpf2);
            canvas.rotate(45.0f);
            canvas.scale(AndroidUtilities.lerp(-1.0f, 1.0f, f), 1.0f);
            canvas.drawPath(this.path, this.paint);
            canvas.restore();
            canvas.save();
            float f2 = -dpf2;
            canvas.translate(f2, f2);
            canvas.rotate(225.0f);
            canvas.scale(AndroidUtilities.lerp(-1.0f, 1.0f, f), 1.0f);
            canvas.drawPath(this.path, this.paint);
            canvas.restore();
            canvas.restore();
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            Paint paint = this.paint;
            this.alpha = i;
            paint.setAlpha(i);
        }
    }
}