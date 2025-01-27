package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Property;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;

/* loaded from: classes3.dex */
public class PipRoundVideoView implements NotificationCenter.NotificationCenterDelegate {
    private static PipRoundVideoView instance;
    private AspectRatioFrameLayout aspectRatioFrameLayout;
    private Bitmap bitmap;
    private int currentAccount;
    private DecelerateInterpolator decelerateInterpolator;
    private AnimatorSet hideShowAnimation;
    private ImageView imageView;
    private Runnable onCloseRunnable;
    private Activity parentActivity;
    private SharedPreferences preferences;
    private RectF rect = new RectF();
    private TextureView textureView;
    private int videoHeight;
    private int videoWidth;
    private WindowManager.LayoutParams windowLayoutParams;
    private WindowManager windowManager;
    private FrameLayout windowView;

    public class PipFrameLayout extends FrameLayout {
        public PipFrameLayout(Context context) {
            super(context);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x017b  */
    /* JADX WARN: Removed duplicated region for block: B:46:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void animateToBoundsMaybe() {
        ArrayList arrayList;
        boolean z;
        ObjectAnimator ofInt;
        int i;
        int sideCoord = getSideCoord(true, 0, 0.0f, this.videoWidth);
        int sideCoord2 = getSideCoord(true, 1, 0.0f, this.videoWidth);
        int sideCoord3 = getSideCoord(false, 0, 0.0f, this.videoHeight);
        int sideCoord4 = getSideCoord(false, 1, 0.0f, this.videoHeight);
        SharedPreferences.Editor edit = this.preferences.edit();
        int dp = AndroidUtilities.dp(20.0f);
        if (Math.abs(sideCoord - this.windowLayoutParams.x) <= dp || ((i = this.windowLayoutParams.x) < 0 && i > (-this.videoWidth) / 4)) {
            ArrayList arrayList2 = new ArrayList();
            edit.putInt("sidex", 0);
            if (this.windowView.getAlpha() != 1.0f) {
                arrayList2.add(ObjectAnimator.ofFloat(this.windowView, (Property<FrameLayout, Float>) View.ALPHA, 1.0f));
            }
            arrayList2.add(ObjectAnimator.ofInt(this, "x", sideCoord));
            arrayList = arrayList2;
        } else {
            if (Math.abs(sideCoord2 - i) > dp) {
                int i2 = this.windowLayoutParams.x;
                int i3 = AndroidUtilities.displaySize.x;
                int i4 = this.videoWidth;
                if (i2 <= i3 - i4 || i2 >= i3 - ((i4 / 4) * 3)) {
                    if (this.windowView.getAlpha() != 1.0f) {
                        arrayList = new ArrayList();
                        arrayList.add(this.windowLayoutParams.x < 0 ? ObjectAnimator.ofInt(this, "x", -this.videoWidth) : ObjectAnimator.ofInt(this, "x", AndroidUtilities.displaySize.x));
                        z = true;
                        if (!z) {
                            if (Math.abs(sideCoord3 - this.windowLayoutParams.y) <= dp || this.windowLayoutParams.y <= ActionBar.getCurrentActionBarHeight()) {
                                if (arrayList == null) {
                                    arrayList = new ArrayList();
                                }
                                edit.putInt("sidey", 0);
                                ofInt = ObjectAnimator.ofInt(this, "y", sideCoord3);
                            } else if (Math.abs(sideCoord4 - this.windowLayoutParams.y) <= dp) {
                                if (arrayList == null) {
                                    arrayList = new ArrayList();
                                }
                                edit.putInt("sidey", 1);
                                ofInt = ObjectAnimator.ofInt(this, "y", sideCoord4);
                            } else {
                                edit.putFloat("py", (this.windowLayoutParams.y - sideCoord3) / (sideCoord4 - sideCoord3));
                                edit.putInt("sidey", 2);
                                edit.commit();
                            }
                            arrayList.add(ofInt);
                            edit.commit();
                        }
                        if (arrayList == null) {
                            if (this.decelerateInterpolator == null) {
                                this.decelerateInterpolator = new DecelerateInterpolator();
                            }
                            AnimatorSet animatorSet = new AnimatorSet();
                            animatorSet.setInterpolator(this.decelerateInterpolator);
                            animatorSet.setDuration(150L);
                            if (z) {
                                arrayList.add(ObjectAnimator.ofFloat(this.windowView, (Property<FrameLayout, Float>) View.ALPHA, 0.0f));
                                animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.PipRoundVideoView.7
                                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                    public void onAnimationEnd(Animator animator) {
                                        PipRoundVideoView.this.close(false);
                                        if (PipRoundVideoView.this.onCloseRunnable != null) {
                                            PipRoundVideoView.this.onCloseRunnable.run();
                                        }
                                    }
                                });
                            }
                            animatorSet.playTogether(arrayList);
                            animatorSet.start();
                            return;
                        }
                        return;
                    }
                    edit.putFloat("px", (this.windowLayoutParams.x - sideCoord) / (sideCoord2 - sideCoord));
                    edit.putInt("sidex", 2);
                    arrayList = null;
                }
            }
            arrayList = new ArrayList();
            edit.putInt("sidex", 1);
            if (this.windowView.getAlpha() != 1.0f) {
                arrayList.add(ObjectAnimator.ofFloat(this.windowView, (Property<FrameLayout, Float>) View.ALPHA, 1.0f));
            }
            arrayList.add(ObjectAnimator.ofInt(this, "x", sideCoord2));
        }
        z = false;
        if (!z) {
        }
        if (arrayList == null) {
        }
    }

    public static PipRoundVideoView getInstance() {
        return instance;
    }

    private static int getSideCoord(boolean z, int i, float f, int i2) {
        int i3;
        android.graphics.Point point = AndroidUtilities.displaySize;
        if (z) {
            i3 = point.x;
        } else {
            i3 = point.y - i2;
            i2 = ActionBar.getCurrentActionBarHeight();
        }
        int dp = i == 0 ? AndroidUtilities.dp(10.0f) : i == 1 ? (i3 - i2) - AndroidUtilities.dp(10.0f) : Math.round((r0 - AndroidUtilities.dp(20.0f)) * f) + AndroidUtilities.dp(10.0f);
        return !z ? dp + ActionBar.getCurrentActionBarHeight() : dp;
    }

    private void runShowHideAnimation(final boolean z) {
        AnimatorSet animatorSet = this.hideShowAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.hideShowAnimation = animatorSet2;
        animatorSet2.playTogether(ObjectAnimator.ofFloat(this.windowView, (Property<FrameLayout, Float>) View.ALPHA, z ? 1.0f : 0.0f), ObjectAnimator.ofFloat(this.windowView, (Property<FrameLayout, Float>) View.SCALE_X, z ? 1.0f : 0.8f), ObjectAnimator.ofFloat(this.windowView, (Property<FrameLayout, Float>) View.SCALE_Y, z ? 1.0f : 0.8f));
        this.hideShowAnimation.setDuration(150L);
        if (this.decelerateInterpolator == null) {
            this.decelerateInterpolator = new DecelerateInterpolator();
        }
        this.hideShowAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.PipRoundVideoView.6
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (animator.equals(PipRoundVideoView.this.hideShowAnimation)) {
                    PipRoundVideoView.this.hideShowAnimation = null;
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (animator.equals(PipRoundVideoView.this.hideShowAnimation)) {
                    if (!z) {
                        PipRoundVideoView.this.close(false);
                    }
                    PipRoundVideoView.this.hideShowAnimation = null;
                }
            }
        });
        this.hideShowAnimation.setInterpolator(this.decelerateInterpolator);
        this.hideShowAnimation.start();
    }

    public void close(boolean z) {
        if (!z) {
            if (this.bitmap != null) {
                this.imageView.setImageDrawable(null);
                this.bitmap.recycle();
                this.bitmap = null;
            }
            try {
                this.windowManager.removeView(this.windowView);
            } catch (Exception unused) {
            }
            if (instance == this) {
                instance = null;
            }
            this.parentActivity = null;
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
            return;
        }
        TextureView textureView = this.textureView;
        if (textureView == null || textureView.getParent() == null) {
            return;
        }
        if (this.textureView.getWidth() > 0 && this.textureView.getHeight() > 0) {
            this.bitmap = Bitmaps.createBitmap(this.textureView.getWidth(), this.textureView.getHeight(), Bitmap.Config.ARGB_8888);
        }
        try {
            this.textureView.getBitmap(this.bitmap);
        } catch (Throwable unused2) {
            this.bitmap = null;
        }
        this.imageView.setImageBitmap(this.bitmap);
        try {
            this.aspectRatioFrameLayout.removeView(this.textureView);
        } catch (Exception unused3) {
        }
        this.imageView.setVisibility(0);
        runShowHideAnimation(false);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        AspectRatioFrameLayout aspectRatioFrameLayout;
        if (i != NotificationCenter.messagePlayingProgressDidChanged || (aspectRatioFrameLayout = this.aspectRatioFrameLayout) == null) {
            return;
        }
        aspectRatioFrameLayout.invalidate();
    }

    public TextureView getTextureView() {
        return this.textureView;
    }

    public void onConfigurationChanged() {
        int i = this.preferences.getInt("sidex", 1);
        int i2 = this.preferences.getInt("sidey", 0);
        float f = this.preferences.getFloat("px", 0.0f);
        float f2 = this.preferences.getFloat("py", 0.0f);
        this.windowLayoutParams.x = getSideCoord(true, i, f, this.videoWidth);
        this.windowLayoutParams.y = getSideCoord(false, i2, f2, this.videoHeight);
        this.windowManager.updateViewLayout(this.windowView, this.windowLayoutParams);
    }

    public void show(Activity activity, Runnable runnable) {
        if (activity == null) {
            return;
        }
        instance = this;
        this.onCloseRunnable = runnable;
        PipFrameLayout pipFrameLayout = new PipFrameLayout(activity) { // from class: org.telegram.ui.Components.PipRoundVideoView.1
            private boolean dragging;
            private boolean startDragging;
            private float startX;
            private float startY;

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                Drawable drawable = Theme.chat_roundVideoShadow;
                if (drawable != null) {
                    drawable.setAlpha((int) (getAlpha() * 255.0f));
                    Theme.chat_roundVideoShadow.setBounds(AndroidUtilities.dp(1.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(125.0f), AndroidUtilities.dp(125.0f));
                    Theme.chat_roundVideoShadow.draw(canvas);
                    Theme.chat_docBackPaint.setColor(Theme.getColor(Theme.key_chat_inBubble));
                    Theme.chat_docBackPaint.setAlpha((int) (getAlpha() * 255.0f));
                    canvas.drawCircle(AndroidUtilities.dp(63.0f), AndroidUtilities.dp(63.0f), AndroidUtilities.dp(59.5f), Theme.chat_docBackPaint);
                }
            }

            @Override // android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    this.startX = motionEvent.getRawX();
                    this.startY = motionEvent.getRawY();
                    this.startDragging = true;
                }
                return true;
            }

            /* JADX WARN: Removed duplicated region for block: B:25:0x00bb  */
            /* JADX WARN: Removed duplicated region for block: B:28:0x010b  */
            /* JADX WARN: Removed duplicated region for block: B:31:0x011e  */
            /* JADX WARN: Removed duplicated region for block: B:34:0x0127  */
            /* JADX WARN: Removed duplicated region for block: B:37:0x00ca  */
            @Override // android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public boolean onTouchEvent(MotionEvent motionEvent) {
                MessageObject playingMessageObject;
                WindowManager.LayoutParams layoutParams;
                float f;
                WindowManager.LayoutParams layoutParams2;
                int i = 0;
                if (!this.startDragging && !this.dragging) {
                    return false;
                }
                float rawX = motionEvent.getRawX();
                float rawY = motionEvent.getRawY();
                if (motionEvent.getAction() == 2) {
                    float f2 = rawX - this.startX;
                    float f3 = rawY - this.startY;
                    if (this.startDragging) {
                        if (Math.abs(f2) >= AndroidUtilities.getPixelsInCM(0.3f, true) || Math.abs(f3) >= AndroidUtilities.getPixelsInCM(0.3f, false)) {
                            this.dragging = true;
                            this.startDragging = false;
                        }
                    } else if (this.dragging) {
                        PipRoundVideoView.this.windowLayoutParams.x = (int) (r6.x + f2);
                        PipRoundVideoView.this.windowLayoutParams.y = (int) (r10.y + f3);
                        int i2 = PipRoundVideoView.this.videoWidth / 2;
                        int i3 = -i2;
                        if (PipRoundVideoView.this.windowLayoutParams.x < i3) {
                            layoutParams = PipRoundVideoView.this.windowLayoutParams;
                        } else {
                            if (PipRoundVideoView.this.windowLayoutParams.x > (AndroidUtilities.displaySize.x - PipRoundVideoView.this.windowLayoutParams.width) + i2) {
                                layoutParams = PipRoundVideoView.this.windowLayoutParams;
                                i3 = (AndroidUtilities.displaySize.x - PipRoundVideoView.this.windowLayoutParams.width) + i2;
                            }
                            f = 1.0f;
                            if (PipRoundVideoView.this.windowLayoutParams.x >= 0) {
                                f = 1.0f + ((PipRoundVideoView.this.windowLayoutParams.x / i2) * 0.5f);
                            } else if (PipRoundVideoView.this.windowLayoutParams.x > AndroidUtilities.displaySize.x - PipRoundVideoView.this.windowLayoutParams.width) {
                                f = 1.0f - ((((PipRoundVideoView.this.windowLayoutParams.x - AndroidUtilities.displaySize.x) + PipRoundVideoView.this.windowLayoutParams.width) / i2) * 0.5f);
                            }
                            if (PipRoundVideoView.this.windowView.getAlpha() != f) {
                                PipRoundVideoView.this.windowView.setAlpha(f);
                            }
                            if (PipRoundVideoView.this.windowLayoutParams.y >= 0) {
                                layoutParams2 = PipRoundVideoView.this.windowLayoutParams;
                            } else {
                                if (PipRoundVideoView.this.windowLayoutParams.y > AndroidUtilities.displaySize.y - PipRoundVideoView.this.windowLayoutParams.height) {
                                    layoutParams2 = PipRoundVideoView.this.windowLayoutParams;
                                    i = AndroidUtilities.displaySize.y - PipRoundVideoView.this.windowLayoutParams.height;
                                }
                                PipRoundVideoView.this.windowManager.updateViewLayout(PipRoundVideoView.this.windowView, PipRoundVideoView.this.windowLayoutParams);
                                this.startX = rawX;
                                this.startY = rawY;
                            }
                            layoutParams2.y = i;
                            PipRoundVideoView.this.windowManager.updateViewLayout(PipRoundVideoView.this.windowView, PipRoundVideoView.this.windowLayoutParams);
                            this.startX = rawX;
                            this.startY = rawY;
                        }
                        layoutParams.x = i3;
                        f = 1.0f;
                        if (PipRoundVideoView.this.windowLayoutParams.x >= 0) {
                        }
                        if (PipRoundVideoView.this.windowView.getAlpha() != f) {
                        }
                        if (PipRoundVideoView.this.windowLayoutParams.y >= 0) {
                        }
                        layoutParams2.y = i;
                        PipRoundVideoView.this.windowManager.updateViewLayout(PipRoundVideoView.this.windowView, PipRoundVideoView.this.windowLayoutParams);
                        this.startX = rawX;
                        this.startY = rawY;
                    }
                } else if (motionEvent.getAction() == 1) {
                    if (this.startDragging && !this.dragging && (playingMessageObject = MediaController.getInstance().getPlayingMessageObject()) != null) {
                        if (MediaController.getInstance().isMessagePaused()) {
                            MediaController.getInstance().playMessage(playingMessageObject);
                        } else {
                            MediaController.getInstance().lambda$startAudioAgain$7(playingMessageObject);
                        }
                    }
                    this.dragging = false;
                    this.startDragging = false;
                    PipRoundVideoView.this.animateToBoundsMaybe();
                }
                return true;
            }

            @Override // android.view.ViewGroup, android.view.ViewParent
            public void requestDisallowInterceptTouchEvent(boolean z) {
                super.requestDisallowInterceptTouchEvent(z);
            }
        };
        this.windowView = pipFrameLayout;
        pipFrameLayout.setWillNotDraw(false);
        this.videoWidth = AndroidUtilities.dp(126.0f);
        this.videoHeight = AndroidUtilities.dp(126.0f);
        if (Build.VERSION.SDK_INT >= 21) {
            AspectRatioFrameLayout aspectRatioFrameLayout = new AspectRatioFrameLayout(activity) { // from class: org.telegram.ui.Components.PipRoundVideoView.2
                @Override // android.view.ViewGroup
                protected boolean drawChild(Canvas canvas, View view, long j) {
                    MessageObject playingMessageObject;
                    boolean drawChild = super.drawChild(canvas, view, j);
                    if (view == PipRoundVideoView.this.textureView && (playingMessageObject = MediaController.getInstance().getPlayingMessageObject()) != null) {
                        PipRoundVideoView.this.rect.set(AndroidUtilities.dpf2(1.5f), AndroidUtilities.dpf2(1.5f), getMeasuredWidth() - AndroidUtilities.dpf2(1.5f), getMeasuredHeight() - AndroidUtilities.dpf2(1.5f));
                        canvas.drawArc(PipRoundVideoView.this.rect, -90.0f, playingMessageObject.audioProgress * 360.0f, false, Theme.chat_radialProgressPaint);
                    }
                    return drawChild;
                }
            };
            this.aspectRatioFrameLayout = aspectRatioFrameLayout;
            aspectRatioFrameLayout.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.Components.PipRoundVideoView.3
                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(120.0f), AndroidUtilities.dp(120.0f));
                }
            });
            this.aspectRatioFrameLayout.setClipToOutline(true);
        } else {
            final Paint paint = new Paint(1);
            paint.setColor(-16777216);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            AspectRatioFrameLayout aspectRatioFrameLayout2 = new AspectRatioFrameLayout(activity) { // from class: org.telegram.ui.Components.PipRoundVideoView.4
                private Path aspectPath = new Path();

                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    super.dispatchDraw(canvas);
                    canvas.drawPath(this.aspectPath, paint);
                }

                @Override // android.view.ViewGroup
                protected boolean drawChild(Canvas canvas, View view, long j) {
                    boolean z;
                    MessageObject playingMessageObject;
                    try {
                        z = super.drawChild(canvas, view, j);
                    } catch (Throwable unused) {
                        z = false;
                    }
                    if (view == PipRoundVideoView.this.textureView && (playingMessageObject = MediaController.getInstance().getPlayingMessageObject()) != null) {
                        PipRoundVideoView.this.rect.set(AndroidUtilities.dpf2(1.5f), AndroidUtilities.dpf2(1.5f), getMeasuredWidth() - AndroidUtilities.dpf2(1.5f), getMeasuredHeight() - AndroidUtilities.dpf2(1.5f));
                        canvas.drawArc(PipRoundVideoView.this.rect, -90.0f, playingMessageObject.audioProgress * 360.0f, false, Theme.chat_radialProgressPaint);
                    }
                    return z;
                }

                @Override // android.view.View
                protected void onSizeChanged(int i, int i2, int i3, int i4) {
                    super.onSizeChanged(i, i2, i3, i4);
                    this.aspectPath.reset();
                    float f = i / 2;
                    this.aspectPath.addCircle(f, i2 / 2, f, Path.Direction.CW);
                    this.aspectPath.toggleInverseFillType();
                }
            };
            this.aspectRatioFrameLayout = aspectRatioFrameLayout2;
            aspectRatioFrameLayout2.setLayerType(2, null);
        }
        this.aspectRatioFrameLayout.setAspectRatio(1.0f, 0);
        this.windowView.addView(this.aspectRatioFrameLayout, LayoutHelper.createFrame(120, 120.0f, 51, 3.0f, 3.0f, 0.0f, 0.0f));
        this.windowView.setAlpha(1.0f);
        this.windowView.setScaleX(0.8f);
        this.windowView.setScaleY(0.8f);
        this.textureView = new TextureView(activity);
        float dpf2 = (AndroidUtilities.dpf2(120.0f) + AndroidUtilities.dpf2(2.0f)) / AndroidUtilities.dpf2(120.0f);
        this.textureView.setScaleX(dpf2);
        this.textureView.setScaleY(dpf2);
        this.aspectRatioFrameLayout.addView(this.textureView, LayoutHelper.createFrame(-1, -1.0f));
        ImageView imageView = new ImageView(activity);
        this.imageView = imageView;
        this.aspectRatioFrameLayout.addView(imageView, LayoutHelper.createFrame(-1, -1.0f));
        this.imageView.setVisibility(4);
        this.windowManager = (WindowManager) activity.getSystemService("window");
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("pipconfig", 0);
        this.preferences = sharedPreferences;
        int i = sharedPreferences.getInt("sidex", 1);
        int i2 = this.preferences.getInt("sidey", 0);
        float f = this.preferences.getFloat("px", 0.0f);
        float f2 = this.preferences.getFloat("py", 0.0f);
        try {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            this.windowLayoutParams = layoutParams;
            int i3 = this.videoWidth;
            layoutParams.width = i3;
            layoutParams.height = this.videoHeight;
            layoutParams.x = getSideCoord(true, i, f, i3);
            this.windowLayoutParams.y = getSideCoord(false, i2, f2, this.videoHeight);
            WindowManager.LayoutParams layoutParams2 = this.windowLayoutParams;
            layoutParams2.format = -3;
            layoutParams2.gravity = 51;
            layoutParams2.type = 99;
            layoutParams2.flags = 16777736;
            AndroidUtilities.setPreferredMaxRefreshRate(this.windowManager, this.windowView, layoutParams2);
            this.windowManager.addView(this.windowView, this.windowLayoutParams);
            this.parentActivity = activity;
            int i4 = UserConfig.selectedAccount;
            this.currentAccount = i4;
            NotificationCenter.getInstance(i4).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
            runShowHideAnimation(true);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void showTemporary(boolean z) {
        AnimatorSet animatorSet = this.hideShowAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.hideShowAnimation = animatorSet2;
        animatorSet2.playTogether(ObjectAnimator.ofFloat(this.windowView, (Property<FrameLayout, Float>) View.ALPHA, z ? 1.0f : 0.0f), ObjectAnimator.ofFloat(this.windowView, (Property<FrameLayout, Float>) View.SCALE_X, z ? 1.0f : 0.8f), ObjectAnimator.ofFloat(this.windowView, (Property<FrameLayout, Float>) View.SCALE_Y, z ? 1.0f : 0.8f));
        this.hideShowAnimation.setDuration(150L);
        if (this.decelerateInterpolator == null) {
            this.decelerateInterpolator = new DecelerateInterpolator();
        }
        this.hideShowAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.PipRoundVideoView.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (animator.equals(PipRoundVideoView.this.hideShowAnimation)) {
                    PipRoundVideoView.this.hideShowAnimation = null;
                }
            }
        });
        this.hideShowAnimation.setInterpolator(this.decelerateInterpolator);
        this.hideShowAnimation.start();
    }
}
