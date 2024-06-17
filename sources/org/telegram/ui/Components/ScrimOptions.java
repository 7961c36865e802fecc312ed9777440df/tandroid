package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
/* loaded from: classes3.dex */
public class ScrimOptions extends Dialog {
    private Bitmap blurBitmap;
    private Paint blurBitmapPaint;
    private BitmapShader blurBitmapShader;
    private Matrix blurMatrix;
    private final FrameLayout containerView;
    public final Context context;
    private boolean dismissing;
    private final android.graphics.Rect insets;
    private ValueAnimator openAnimator;
    private float openProgress;
    private ItemOptions options;
    private FrameLayout optionsContainer;
    private View optionsView;
    public final Theme.ResourcesProvider resourcesProvider;
    private Drawable scrimDrawable;
    private float scrimDrawableSh;
    private float scrimDrawableSw;
    private float scrimDrawableTx1;
    private float scrimDrawableTx2;
    private float scrimDrawableTy1;
    private float scrimDrawableTy2;
    private final FrameLayout windowView;

    public ScrimOptions(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, R.style.TransparentDialog);
        int i = UserConfig.selectedAccount;
        this.insets = new android.graphics.Rect();
        this.scrimDrawableSw = 1.0f;
        this.scrimDrawableSh = 1.0f;
        this.dismissing = false;
        this.context = context;
        this.resourcesProvider = resourcesProvider;
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.Components.ScrimOptions.1
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                if (ScrimOptions.this.openProgress > 0.0f && ScrimOptions.this.blurBitmapPaint != null) {
                    ScrimOptions.this.blurMatrix.reset();
                    float width = getWidth() / ScrimOptions.this.blurBitmap.getWidth();
                    ScrimOptions.this.blurMatrix.postScale(width, width);
                    ScrimOptions.this.blurBitmapShader.setLocalMatrix(ScrimOptions.this.blurMatrix);
                    ScrimOptions.this.blurBitmapPaint.setAlpha((int) (ScrimOptions.this.openProgress * 255.0f));
                    canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), ScrimOptions.this.blurBitmapPaint);
                }
                super.dispatchDraw(canvas);
                if (ScrimOptions.this.scrimDrawable != null) {
                    ScrimOptions.this.scrimDrawable.setAlpha((int) (ScrimOptions.this.openProgress * 255.0f));
                    canvas.save();
                    canvas.translate(ScrimOptions.this.scrimDrawableTx2 + (ScrimOptions.this.scrimDrawableTx1 * ScrimOptions.this.openProgress), ScrimOptions.this.scrimDrawableTy2 + (ScrimOptions.this.scrimDrawableTy1 * ScrimOptions.this.openProgress));
                    float lerp = AndroidUtilities.lerp(AndroidUtilities.lerp(Math.min(ScrimOptions.this.scrimDrawableSw, ScrimOptions.this.scrimDrawableSh), Math.max(ScrimOptions.this.scrimDrawableSw, ScrimOptions.this.scrimDrawableSh), 0.75f), 1.0f, ScrimOptions.this.openProgress);
                    canvas.scale(lerp, lerp, (-ScrimOptions.this.scrimDrawableTx2) + ScrimOptions.this.scrimDrawable.getBounds().left + ((ScrimOptions.this.scrimDrawable.getBounds().width() / 2.0f) * ScrimOptions.this.scrimDrawableSw), (-ScrimOptions.this.scrimDrawableTy2) + ScrimOptions.this.scrimDrawable.getBounds().top + ((ScrimOptions.this.scrimDrawable.getBounds().height() / 2.0f) * ScrimOptions.this.scrimDrawableSh));
                    ScrimOptions.this.scrimDrawable.draw(canvas);
                    canvas.restore();
                }
            }

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getKeyCode() == 4 && keyEvent.getAction() == 1) {
                    ScrimOptions.this.onBackPressed();
                    return true;
                }
                return super.dispatchKeyEventPreIme(keyEvent);
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i2, int i3, int i4, int i5) {
                super.onLayout(z, i2, i3, i4, i5);
                ScrimOptions.this.layout();
            }
        };
        this.windowView = frameLayout;
        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ScrimOptions$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScrimOptions.this.lambda$new$0(view);
            }
        });
        SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context);
        this.containerView = sizeNotifierFrameLayout;
        sizeNotifierFrameLayout.setClipToPadding(false);
        frameLayout.addView(sizeNotifierFrameLayout, LayoutHelper.createFrame(-1, -1, 119));
        if (Build.VERSION.SDK_INT >= 21) {
            frameLayout.setFitsSystemWindows(true);
            frameLayout.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: org.telegram.ui.Components.ScrimOptions.2
                @Override // android.view.View.OnApplyWindowInsetsListener
                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    int i2 = Build.VERSION.SDK_INT;
                    if (i2 < 30) {
                        ScrimOptions.this.insets.set(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
                    } else {
                        Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.displayCutout() | WindowInsetsCompat.Type.systemBars());
                        ScrimOptions.this.insets.set(insets.left, insets.top, insets.right, insets.bottom);
                    }
                    ScrimOptions.this.containerView.setPadding(ScrimOptions.this.insets.left, ScrimOptions.this.insets.top, ScrimOptions.this.insets.right, ScrimOptions.this.insets.bottom);
                    ScrimOptions.this.windowView.requestLayout();
                    if (i2 >= 30) {
                        return WindowInsets.CONSUMED;
                    }
                    return windowInsets.consumeSystemWindowInsets();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        onBackPressed();
    }

    public void setItemOptions(ItemOptions itemOptions) {
        this.options = itemOptions;
        this.optionsView = itemOptions.getLayout();
        FrameLayout frameLayout = new FrameLayout(this.context);
        this.optionsContainer = frameLayout;
        frameLayout.addView(this.optionsView, LayoutHelper.createFrame(-2, -2.0f));
        this.containerView.addView(this.optionsContainer, LayoutHelper.createFrame(-2, -2.0f));
    }

    @Override // android.app.Dialog
    public boolean isShowing() {
        return !this.dismissing;
    }

    @Override // android.app.Dialog
    public void show() {
        if (AndroidUtilities.isSafeToShow(getContext())) {
            super.show();
            prepareBlur(null);
            animateOpenTo(true, null);
        }
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        if (this.dismissing) {
            return;
        }
        this.dismissing = true;
        animateOpenTo(false, new Runnable() { // from class: org.telegram.ui.Components.ScrimOptions$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ScrimOptions.this.lambda$dismiss$2();
            }
        });
        this.windowView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$1() {
        super.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$2() {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ScrimOptions$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ScrimOptions.this.lambda$dismiss$1();
            }
        });
    }

    public void dismissFast() {
        if (this.dismissing) {
            return;
        }
        this.dismissing = true;
        animateOpenTo(false, 2.0f, new Runnable() { // from class: org.telegram.ui.Components.ScrimOptions$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ScrimOptions.this.lambda$dismissFast$4();
            }
        });
        this.windowView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissFast$3() {
        super.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissFast$4() {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ScrimOptions$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ScrimOptions.this.lambda$dismissFast$3();
            }
        });
    }

    private void animateOpenTo(boolean z, Runnable runnable) {
        animateOpenTo(z, 1.0f, runnable);
    }

    private void animateOpenTo(final boolean z, float f, final Runnable runnable) {
        ValueAnimator valueAnimator = this.openAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        float[] fArr = new float[2];
        fArr[0] = this.openProgress;
        fArr[1] = z ? 1.0f : 0.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        this.openAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ScrimOptions$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ScrimOptions.this.lambda$animateOpenTo$5(valueAnimator2);
            }
        });
        this.openAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ScrimOptions.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ScrimOptions.this.openProgress = z ? 1.0f : 0.0f;
                ScrimOptions.this.optionsView.setScaleX(AndroidUtilities.lerp(0.8f, 1.0f, ScrimOptions.this.openProgress));
                ScrimOptions.this.optionsView.setScaleY(AndroidUtilities.lerp(0.8f, 1.0f, ScrimOptions.this.openProgress));
                ScrimOptions.this.optionsView.setAlpha(ScrimOptions.this.openProgress);
                ScrimOptions.this.windowView.invalidate();
                ScrimOptions.this.containerView.invalidate();
                Runnable runnable2 = runnable;
                if (runnable2 != null) {
                    AndroidUtilities.runOnUIThread(runnable2);
                }
            }
        });
        this.openAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.openAnimator.setDuration(350L);
        this.openAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateOpenTo$5(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.openProgress = floatValue;
        this.optionsView.setScaleX(AndroidUtilities.lerp(0.8f, 1.0f, floatValue));
        this.optionsView.setScaleY(AndroidUtilities.lerp(0.8f, 1.0f, this.openProgress));
        this.optionsView.setAlpha(this.openProgress);
        this.windowView.invalidate();
        this.containerView.invalidate();
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.setWindowAnimations(R.style.DialogNoAnimation);
        setContentView(this.windowView, new ViewGroup.LayoutParams(-1, -1));
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = -1;
        attributes.height = -1;
        attributes.gravity = 119;
        attributes.dimAmount = 0.0f;
        int i = attributes.flags & (-3);
        attributes.flags = i;
        attributes.softInputMode = 16;
        int i2 = i | 131072;
        attributes.flags = i2;
        int i3 = Build.VERSION.SDK_INT;
        if (i3 >= 21) {
            attributes.flags = i2 | (-1946091264);
        }
        int i4 = attributes.flags | 1024;
        attributes.flags = i4;
        attributes.flags = i4 | 128;
        if (i3 >= 28) {
            attributes.layoutInDisplayCutoutMode = 1;
        }
        window.setAttributes(attributes);
        this.windowView.setSystemUiVisibility(LiteMode.FLAG_CHAT_BLUR);
        AndroidUtilities.setLightNavigationBar(this.windowView, !Theme.isCurrentThemeDark());
    }

    private void prepareBlur(final View view) {
        if (view != null) {
            view.setVisibility(4);
        }
        AndroidUtilities.makeGlobalBlurBitmap(new Utilities.Callback() { // from class: org.telegram.ui.Components.ScrimOptions$$ExternalSyntheticLambda6
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                ScrimOptions.this.lambda$prepareBlur$6(view, (Bitmap) obj);
            }
        }, 14.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareBlur$6(View view, Bitmap bitmap) {
        if (view != null) {
            view.setVisibility(0);
        }
        this.blurBitmap = bitmap;
        Paint paint = new Paint(1);
        this.blurBitmapPaint = paint;
        Bitmap bitmap2 = this.blurBitmap;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(bitmap2, tileMode, tileMode);
        this.blurBitmapShader = bitmapShader;
        paint.setShader(bitmapShader);
        ColorMatrix colorMatrix = new ColorMatrix();
        AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, Theme.isCurrentThemeDark() ? 0.08f : 0.25f);
        AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, Theme.isCurrentThemeDark() ? -0.02f : -0.07f);
        this.blurBitmapPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        this.blurMatrix = new Matrix();
    }

    public void layout() {
        boolean z;
        Drawable drawable = this.scrimDrawable;
        if (drawable != null) {
            android.graphics.Rect bounds = drawable.getBounds();
            FrameLayout frameLayout = this.optionsContainer;
            if (frameLayout != null) {
                float f = this.scrimDrawableTx2;
                float f2 = bounds.left + f;
                float f3 = bounds.right + f;
                float f4 = this.scrimDrawableTy2;
                float f5 = bounds.top + f4;
                float f6 = bounds.bottom + f4;
                boolean z2 = true;
                if (f3 - frameLayout.getMeasuredWidth() < AndroidUtilities.dp(8.0f)) {
                    this.optionsView.setPivotX(AndroidUtilities.dp(6.0f));
                    this.optionsContainer.setX(Math.min(this.containerView.getWidth() - this.optionsContainer.getWidth(), f2 - AndroidUtilities.dp(10.0f)) - this.containerView.getX());
                    z = false;
                } else {
                    View view = this.optionsView;
                    view.setPivotX(view.getMeasuredWidth() - AndroidUtilities.dp(6.0f));
                    this.optionsContainer.setX(Math.max(AndroidUtilities.dp(8.0f), (AndroidUtilities.dp(4.0f) + f3) - this.optionsContainer.getMeasuredWidth()) - this.containerView.getX());
                    z = true;
                }
                this.scrimDrawableTx1 = z ? ((this.optionsContainer.getX() + this.optionsContainer.getWidth()) - AndroidUtilities.dp(6.0f)) - f3 : (this.optionsContainer.getX() + AndroidUtilities.dp(10.0f)) - f2;
                this.scrimDrawableTy1 = 0.0f;
                if (this.optionsContainer.getMeasuredHeight() + f6 > this.windowView.getMeasuredHeight() - AndroidUtilities.dp(16.0f)) {
                    View view2 = this.optionsView;
                    view2.setPivotY(view2.getMeasuredHeight() - AndroidUtilities.dp(6.0f));
                    this.optionsContainer.setY(((f5 - AndroidUtilities.dp(4.0f)) - this.optionsContainer.getMeasuredHeight()) - this.containerView.getY());
                } else {
                    this.optionsView.setPivotY(AndroidUtilities.dp(6.0f));
                    this.optionsContainer.setY(Math.min((this.windowView.getHeight() - this.optionsContainer.getMeasuredHeight()) - AndroidUtilities.dp(16.0f), f6) - this.containerView.getY());
                    z2 = false;
                }
                this.options.setSwipebackGravity(z, z2);
            }
        }
    }

    public void setScrim(final ChatMessageCell chatMessageCell, CharacterStyle characterStyle, CharSequence charSequence) {
        float f;
        float f2;
        ArrayList<MessageObject.TextLayoutBlock> arrayList;
        float f3;
        RectF rectF;
        StaticLayout staticLayout;
        final Bitmap bitmap;
        StaticLayout staticLayout2;
        MessageObject messageObject;
        boolean z;
        if (chatMessageCell == null) {
            return;
        }
        chatMessageCell.getCurrentMessagesGroup();
        MessageObject messageObject2 = chatMessageCell.getMessageObject();
        if (chatMessageCell.getCaptionLayout() != null) {
            f = chatMessageCell.getCaptionX();
            f2 = chatMessageCell.getCaptionY();
            arrayList = chatMessageCell.getCaptionLayout().textLayoutBlocks;
            f3 = chatMessageCell.getCaptionLayout().textXOffset;
        } else {
            f = 0.0f;
            f2 = 0.0f;
            arrayList = null;
            f3 = 0.0f;
        }
        if (arrayList == null) {
            f = chatMessageCell.getTextX();
            f2 = chatMessageCell.getTextY();
            arrayList = messageObject2.textLayoutBlocks;
            f3 = messageObject2.textXOffset;
        }
        if (arrayList == null) {
            return;
        }
        float f4 = f;
        float f5 = f2;
        int i = 0;
        StaticLayout staticLayout3 = null;
        int i2 = 0;
        int i3 = 0;
        while (i < arrayList.size()) {
            MessageObject.TextLayoutBlock textLayoutBlock = arrayList.get(i);
            StaticLayout staticLayout4 = textLayoutBlock.textLayout;
            if (staticLayout4 != null && (staticLayout4.getText() instanceof Spanned)) {
                messageObject = messageObject2;
                CharacterStyle[] characterStyleArr = (CharacterStyle[]) ((Spanned) staticLayout4.getText()).getSpans(0, staticLayout4.getText().length(), CharacterStyle.class);
                if (characterStyleArr != null) {
                    int i4 = 0;
                    while (true) {
                        if (i4 >= characterStyleArr.length) {
                            z = false;
                            break;
                        } else if (characterStyleArr[i4] == characterStyle) {
                            z = true;
                            break;
                        } else {
                            i4++;
                        }
                    }
                    if (z) {
                        i2 = ((Spanned) staticLayout4.getText()).getSpanStart(characterStyle);
                        i3 = ((Spanned) staticLayout4.getText()).getSpanEnd(characterStyle);
                        f4 += textLayoutBlock.isRtl() ? (int) Math.ceil(f3) : 0;
                        f5 += textLayoutBlock.padTop + textLayoutBlock.textYOffset(arrayList, chatMessageCell.transitionParams);
                        staticLayout3 = staticLayout4;
                    }
                }
            } else {
                messageObject = messageObject2;
            }
            i++;
            messageObject2 = messageObject;
        }
        MessageObject messageObject3 = messageObject2;
        if (staticLayout3 == null) {
            return;
        }
        if (charSequence != null) {
            LinkPath linkPath = new LinkPath(true);
            linkPath.setCurrentLayout(staticLayout3, i2, 0.0f);
            staticLayout3.getSelectionPath(i2, i3, linkPath);
            RectF rectF2 = new RectF();
            linkPath.computeBounds(rectF2, true);
            StaticLayout makeStaticLayout = MessageObject.makeStaticLayout(charSequence, staticLayout3.getPaint(), staticLayout3.getWidth(), 1.0f, 0.0f, true);
            i3 = charSequence.length();
            rectF = rectF2;
            i2 = 0;
            staticLayout = makeStaticLayout;
        } else {
            rectF = null;
            staticLayout = staticLayout3;
        }
        final Paint paint = new Paint(1);
        paint.setColor(Theme.getColor(messageObject3.isOutOwner() ? Theme.key_chat_outBubble : Theme.key_chat_inBubble, this.resourcesProvider));
        paint.setPathEffect(new CornerPathEffect(AndroidUtilities.dp(5.0f)));
        final LinkPath linkPath2 = new LinkPath(true);
        linkPath2.setUseCornerPathImplementation(true);
        linkPath2.setCurrentLayout(staticLayout, i2, 0.0f);
        staticLayout.getSelectionPath(i2, i3, linkPath2);
        linkPath2.closeRects();
        final RectF rectF3 = new RectF();
        linkPath2.computeBounds(rectF3, true);
        int width = (int) (rectF3.width() + LinkPath.getRadius());
        if (!chatMessageCell.drawBackgroundInParent() || width <= 0 || rectF3.height() <= 0.0f) {
            bitmap = null;
        } else {
            Bitmap createBitmap = Bitmap.createBitmap(width, (int) rectF3.height(), Bitmap.Config.ALPHA_8);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint2 = new Paint(1);
            paint2.setColor(-1);
            canvas.drawRect(0.0f, 0.0f, width, rectF3.height(), paint2);
            Paint paint3 = new Paint(1);
            paint3.setColor(-1);
            paint3.setPathEffect(new CornerPathEffect(AndroidUtilities.dp(5.0f)));
            paint3.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.translate(-rectF3.left, -rectF3.top);
            canvas.drawPath(linkPath2, paint3);
            bitmap = createBitmap;
        }
        final Paint paint4 = new Paint(3);
        paint4.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        chatMessageCell.setupTextColors();
        TextPaint textPaint = new TextPaint(1);
        textPaint.setColor(staticLayout.getPaint().getColor());
        textPaint.linkColor = staticLayout.getPaint().linkColor;
        textPaint.setTextSize(staticLayout.getPaint().getTextSize());
        textPaint.setTextAlign(staticLayout.getPaint().getTextAlign());
        textPaint.setTypeface(staticLayout.getPaint().getTypeface());
        if (Build.VERSION.SDK_INT >= 24) {
            staticLayout2 = StaticLayout.Builder.obtain(staticLayout.getText(), 0, staticLayout.getText().length(), textPaint, staticLayout.getWidth()).setLineSpacing(0.0f, 1.0f).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
        } else {
            staticLayout2 = new StaticLayout(staticLayout.getText(), textPaint, staticLayout.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        final int[] iArr = new int[2];
        chatMessageCell.getLocationOnScreen(iArr);
        final int[] iArr2 = {iArr[0] + ((int) f4), iArr[1] + ((int) f5)};
        RectF rectF4 = rectF;
        final StaticLayout staticLayout5 = staticLayout2;
        this.scrimDrawable = new Drawable(this) { // from class: org.telegram.ui.Components.ScrimOptions.4
            private int alpha = 255;

            @Override // android.graphics.drawable.Drawable
            public int getOpacity() {
                return -2;
            }

            @Override // android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
            }

            @Override // android.graphics.drawable.Drawable
            public void draw(Canvas canvas2) {
                if (this.alpha <= 0) {
                    return;
                }
                RectF rectF5 = AndroidUtilities.rectTmp;
                rectF5.set(getBounds());
                rectF5.left -= LinkPath.getRadius() / 2.0f;
                canvas2.saveLayerAlpha(rectF5, this.alpha, 31);
                int[] iArr3 = iArr2;
                canvas2.translate(iArr3[0], iArr3[1]);
                ChatMessageCell chatMessageCell2 = chatMessageCell;
                if (chatMessageCell2 != null && chatMessageCell2.drawBackgroundInParent()) {
                    Theme.MessageDrawable messageDrawable = chatMessageCell.currentBackgroundDrawable;
                    if (messageDrawable != null && messageDrawable.getPaint() != null) {
                        canvas2.save();
                        canvas2.translate(0.0f, -chatMessageCell.currentBackgroundDrawable.getTopY());
                        canvas2.drawPaint(chatMessageCell.currentBackgroundDrawable.getPaint());
                        canvas2.restore();
                    } else {
                        int[] iArr4 = iArr2;
                        canvas2.translate(-iArr4[0], -iArr4[1]);
                        int[] iArr5 = iArr;
                        canvas2.translate(iArr5[0], iArr5[1]);
                        chatMessageCell.drawBackgroundInternal(canvas2, true);
                        int[] iArr6 = iArr;
                        canvas2.translate(-iArr6[0], -iArr6[1]);
                        int[] iArr7 = iArr2;
                        canvas2.translate(iArr7[0], iArr7[1]);
                    }
                    if (bitmap != null) {
                        canvas2.save();
                        Bitmap bitmap2 = bitmap;
                        RectF rectF6 = rectF3;
                        canvas2.drawBitmap(bitmap2, rectF6.left, rectF6.top, paint4);
                        canvas2.restore();
                    }
                } else {
                    canvas2.drawPath(linkPath2, paint);
                }
                canvas2.clipPath(linkPath2);
                canvas2.save();
                staticLayout5.draw(canvas2);
                canvas2.restore();
                canvas2.restore();
            }

            @Override // android.graphics.drawable.Drawable
            public void setAlpha(int i5) {
                this.alpha = i5;
            }
        };
        int radius = (int) (iArr[0] + f4 + rectF3.left + (LinkPath.getRadius() / 2.0f));
        int i5 = (int) (iArr[1] + f5 + rectF3.top);
        this.scrimDrawable.setBounds(radius, i5, ((int) rectF3.width()) + radius, ((int) rectF3.height()) + i5);
        if (charSequence != null) {
            float f6 = radius;
            if (rectF3.width() + f6 > AndroidUtilities.displaySize.x - AndroidUtilities.dp(8.0f)) {
                this.scrimDrawableTx2 -= (f6 + rectF3.width()) - (AndroidUtilities.displaySize.x - AndroidUtilities.dp(8.0f));
            }
            float f7 = i5;
            if (rectF3.height() + f7 > (AndroidUtilities.displaySize.y - AndroidUtilities.navigationBarHeight) - AndroidUtilities.dp(8.0f)) {
                this.scrimDrawableTy2 -= (f7 + rectF3.height()) - ((AndroidUtilities.displaySize.y - AndroidUtilities.navigationBarHeight) - AndroidUtilities.dp(8.0f));
            }
            if (rectF4 != null) {
                this.scrimDrawableSw = rectF4.width() / rectF3.width();
                this.scrimDrawableSh = rectF4.height() / rectF3.height();
            }
        }
    }
}