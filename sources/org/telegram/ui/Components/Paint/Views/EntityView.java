package org.telegram.ui.Components.Paint.Views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.zxing.common.detector.MathUtils;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.Rect;
/* loaded from: classes4.dex */
public class EntityView extends FrameLayout {
    private static final List<Integer> STICKY_ANGLES = Arrays.asList(-90, 0, 90, 180);
    private float angle;
    private ValueAnimator angleAnimator;
    private boolean announcedDrag;
    private boolean announcedMultitouchDrag;
    private boolean announcedSelection;
    private boolean announcedTrash;
    private ButtonBounce bounce;
    private int currentStickyAngle;
    private final float[] cxy;
    private EntityViewDelegate delegate;
    private ValueAnimator fromStickyAngleAnimator;
    private float fromStickyAnimatedAngle;
    private float fromStickyToAngle;
    private boolean hadMultitouch;
    public boolean hasPanned;
    public boolean hasReleased;
    private boolean hasStickyAngle;
    private boolean hasTransformed;
    private boolean lastIsMultitouch;
    private ViewGroup lastSelectionContainer;
    private final Runnable longPressRunnable;
    private Point position;
    private float previousLocationCX;
    private float previousLocationCY;
    private float previousLocationX;
    private float previousLocationX2;
    private float previousLocationY;
    private float previousLocationY2;
    private boolean recognizedLongPress;
    private float scale;
    private ValueAnimator selectAnimator;
    private float selectT;
    private boolean selecting;
    protected SelectionView selectionView;
    private Runnable setStickyAngleRunnable;
    private Runnable setStickyXRunnable;
    private Runnable setStickyYRunnable;
    private int stickyAngleRunnableValue;
    private float stickyAnimatedAngle;
    private int stickyX;
    private ValueAnimator stickyXAnimator;
    private int stickyXRunnableValue;
    private int stickyY;
    private ValueAnimator stickyYAnimator;
    private int stickyYRunnableValue;
    private ValueAnimator trashAnimator;
    private float trashScale;
    private UUID uuid;
    private final float[] xy;
    private final float[] xy2;

    /* loaded from: classes4.dex */
    public interface EntityViewDelegate {

        /* loaded from: classes4.dex */
        public final /* synthetic */ class -CC {
            public static boolean $default$isEntityDeletable(EntityViewDelegate entityViewDelegate) {
                return true;
            }

            public static void $default$onEntityDragEnd(EntityViewDelegate entityViewDelegate, boolean z) {
            }

            public static void $default$onEntityDragMultitouchEnd(EntityViewDelegate entityViewDelegate) {
            }

            public static void $default$onEntityDragMultitouchStart(EntityViewDelegate entityViewDelegate) {
            }

            public static void $default$onEntityDragStart(EntityViewDelegate entityViewDelegate) {
            }

            public static void $default$onEntityDragTrash(EntityViewDelegate entityViewDelegate, boolean z) {
            }

            public static void $default$onEntityDraggedBottom(EntityViewDelegate entityViewDelegate, boolean z) {
            }

            public static void $default$onEntityDraggedTop(EntityViewDelegate entityViewDelegate, boolean z) {
            }

            public static void $default$onEntityHandleTouched(EntityViewDelegate entityViewDelegate) {
            }
        }

        boolean allowInteraction(EntityView entityView);

        int[] getCenterLocation(EntityView entityView);

        float getCropRotation();

        void getTransformedTouch(float f, float f2, float[] fArr);

        boolean isEntityDeletable();

        void onEntityDragEnd(boolean z);

        void onEntityDragMultitouchEnd();

        void onEntityDragMultitouchStart();

        void onEntityDragStart();

        void onEntityDragTrash(boolean z);

        void onEntityDraggedBottom(boolean z);

        void onEntityDraggedTop(boolean z);

        void onEntityHandleTouched();

        boolean onEntityLongClicked(EntityView entityView);

        boolean onEntitySelected(EntityView entityView);
    }

    protected boolean allowHaptic() {
        return true;
    }

    public boolean allowLongPressOnSelected() {
        return false;
    }

    protected SelectionView createSelectionView() {
        return null;
    }

    protected float getBounceScale() {
        return 0.04f;
    }

    protected float getMaxScale() {
        return 100.0f;
    }

    protected float getMinScale() {
        return 0.0f;
    }

    protected float getStickyPaddingBottom() {
        return 0.0f;
    }

    protected float getStickyPaddingLeft() {
        return 0.0f;
    }

    protected float getStickyPaddingRight() {
        return 0.0f;
    }

    protected float getStickyPaddingTop() {
        return 0.0f;
    }

    public boolean trashCenter() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.recognizedLongPress = true;
        if (this.delegate != null) {
            performHapticFeedback(0);
            this.delegate.onEntityLongClicked(this);
        }
    }

    public EntityView(Context context, Point point) {
        super(context);
        this.bounce = new ButtonBounce(this);
        this.hasPanned = false;
        this.hasReleased = false;
        this.hasTransformed = false;
        this.announcedDrag = false;
        this.announcedMultitouchDrag = false;
        this.announcedSelection = false;
        this.announcedTrash = false;
        this.recognizedLongPress = false;
        this.longPressRunnable = new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.EntityView$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                EntityView.this.lambda$new$0();
            }
        };
        this.hasStickyAngle = true;
        this.currentStickyAngle = 0;
        this.stickyAngleRunnableValue = -1;
        this.stickyX = 0;
        this.stickyY = 0;
        this.setStickyXRunnable = new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.EntityView$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                EntityView.this.updateStickyX();
            }
        };
        this.setStickyYRunnable = new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.EntityView$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                EntityView.this.updateStickyY();
            }
        };
        this.xy = new float[2];
        this.xy2 = new float[2];
        this.cxy = new float[2];
        this.scale = 1.0f;
        this.selecting = false;
        this.trashScale = 1.0f;
        this.uuid = UUID.randomUUID();
        this.position = point;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point point) {
        this.position = point;
        updatePosition();
    }

    public float getScale() {
        return getScaleX();
    }

    public void setScale(float f) {
        this.scale = f;
        setScaleX(f);
        setScaleY(f);
    }

    public void setDelegate(EntityViewDelegate entityViewDelegate) {
        this.delegate = entityViewDelegate;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.delegate.allowInteraction(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onTouchMove(float f, float f2, boolean z, float f3, float f4) {
        EntityViewDelegate entityViewDelegate;
        EntityViewDelegate entityViewDelegate2;
        EntityViewDelegate entityViewDelegate3;
        EntityViewDelegate entityViewDelegate4;
        if (getParent() == null) {
            return false;
        }
        float scaleX = ((View) getParent()).getScaleX();
        float f5 = z ? (f + f3) / 2.0f : f;
        float f6 = z ? (f2 + f4) / 2.0f : f2;
        float f7 = (f5 - this.previousLocationCX) / scaleX;
        float f8 = (f6 - this.previousLocationCY) / scaleX;
        if (((float) Math.hypot(f7, f8)) > (this.hasPanned ? 6.0f : 16.0f) || z) {
            AndroidUtilities.cancelRunOnUIThread(this.longPressRunnable);
            pan(f7, f8);
            if (z) {
                float distance = MathUtils.distance(f, f2, f3, f4);
                float distance2 = MathUtils.distance(this.previousLocationX, this.previousLocationY, this.previousLocationX2, this.previousLocationY2);
                if (distance2 > 0.0f) {
                    scale(distance / distance2);
                }
                rotate((this.angle + ((float) Math.toDegrees(Math.atan2(f2 - f4, f - f3) - Math.atan2(this.previousLocationY - this.previousLocationY2, this.previousLocationX - this.previousLocationX2)))) - this.delegate.getCropRotation());
            }
            this.previousLocationX = f;
            this.previousLocationY = f2;
            this.previousLocationCX = f5;
            this.previousLocationCY = f6;
            if (z) {
                this.previousLocationX2 = f3;
                this.previousLocationY2 = f4;
            }
            this.hasPanned = true;
            if ((getParent() instanceof EntitiesContainerView) && (this.stickyX != 0 || this.stickyY != 0)) {
                ((EntitiesContainerView) getParent()).invalidate();
            }
            if (!this.announcedDrag && (entityViewDelegate4 = this.delegate) != null) {
                this.announcedDrag = true;
                entityViewDelegate4.onEntityDragStart();
            }
            if (!this.announcedMultitouchDrag && z && (entityViewDelegate3 = this.delegate) != null) {
                this.announcedMultitouchDrag = true;
                entityViewDelegate3.onEntityDragMultitouchStart();
            }
            if (this.announcedMultitouchDrag && !z && (entityViewDelegate2 = this.delegate) != null) {
                this.announcedMultitouchDrag = false;
                entityViewDelegate2.onEntityDragMultitouchEnd();
            }
            if (!isSelected() && !this.announcedSelection && (entityViewDelegate = this.delegate) != null) {
                entityViewDelegate.onEntitySelected(this);
                this.announcedSelection = true;
            }
            EntityViewDelegate entityViewDelegate5 = this.delegate;
            if (entityViewDelegate5 != null) {
                entityViewDelegate5.onEntityDraggedTop(this.position.y - ((((float) getHeight()) / 2.0f) * scaleX) < ((float) AndroidUtilities.dp(66.0f)));
                this.delegate.onEntityDraggedBottom(this.position.y + ((((float) getHeight()) / 2.0f) * scaleX) > ((float) (((View) getParent()).getHeight() - AndroidUtilities.dp(114.0f))));
            }
            EntityViewDelegate entityViewDelegate6 = this.delegate;
            updateTrash((entityViewDelegate6 == null || entityViewDelegate6.isEntityDeletable()) && !z && MathUtils.distance(f5, f6, ((float) ((View) getParent()).getWidth()) / 2.0f, (float) (((View) getParent()).getHeight() - AndroidUtilities.dp(76.0f))) < ((float) AndroidUtilities.dp(32.0f)));
            this.bounce.setPressed(false);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTouchUp(boolean z) {
        EntityViewDelegate entityViewDelegate;
        EntityViewDelegate entityViewDelegate2;
        if (this.announcedDrag) {
            this.delegate.onEntityDragEnd(this.announcedTrash);
            this.announcedDrag = false;
        }
        this.announcedMultitouchDrag = false;
        if (!z && !this.recognizedLongPress && !this.hasPanned && !this.hasTransformed && !this.announcedSelection && (entityViewDelegate2 = this.delegate) != null) {
            entityViewDelegate2.onEntitySelected(this);
        }
        if (this.hasPanned && (entityViewDelegate = this.delegate) != null) {
            entityViewDelegate.onEntityDraggedTop(false);
            this.delegate.onEntityDraggedBottom(false);
        }
        AndroidUtilities.cancelRunOnUIThread(this.longPressRunnable);
        this.recognizedLongPress = false;
        this.hasPanned = false;
        this.hasTransformed = false;
        this.hasReleased = true;
        this.announcedSelection = false;
        this.stickyAngleRunnableValue = this.currentStickyAngle;
        Runnable runnable = this.setStickyAngleRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.setStickyAngleRunnable = null;
        }
        this.stickyXRunnableValue = this.stickyX;
        AndroidUtilities.cancelRunOnUIThread(this.setStickyXRunnable);
        this.stickyYRunnableValue = this.stickyY;
        AndroidUtilities.cancelRunOnUIThread(this.setStickyYRunnable);
        if (getParent() instanceof EntitiesContainerView) {
            ((EntitiesContainerView) getParent()).invalidate();
        }
    }

    public final boolean hasTouchDown() {
        return !this.hasReleased;
    }

    public void setStickyX(int i) {
        this.stickyXRunnableValue = i;
        this.stickyX = i;
    }

    public final int getStickyX() {
        return this.stickyX;
    }

    public void setStickyY(int i) {
        this.stickyYRunnableValue = i;
        this.stickyY = i;
    }

    public final int getStickyY() {
        return this.stickyY;
    }

    public boolean hasPanned() {
        return this.hasPanned;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        if (!this.delegate.allowInteraction(this)) {
            return false;
        }
        this.delegate.getTransformedTouch(motionEvent.getRawX(), motionEvent.getRawY(), this.xy);
        boolean z2 = motionEvent.getPointerCount() > 1;
        if (z2) {
            if (Build.VERSION.SDK_INT >= 29) {
                this.delegate.getTransformedTouch(motionEvent.getRawX(1), motionEvent.getRawY(1), this.xy2);
            } else {
                z2 = false;
            }
        }
        if (z2) {
            float[] fArr = this.cxy;
            float[] fArr2 = this.xy;
            float f = fArr2[0];
            float[] fArr3 = this.xy2;
            fArr[0] = (f + fArr3[0]) / 2.0f;
            fArr[1] = (fArr2[1] + fArr3[1]) / 2.0f;
        } else {
            float[] fArr4 = this.cxy;
            float[] fArr5 = this.xy;
            fArr4[0] = fArr5[0];
            fArr4[1] = fArr5[1];
        }
        if (this.lastIsMultitouch != z2) {
            float[] fArr6 = this.xy;
            this.previousLocationX = fArr6[0];
            this.previousLocationY = fArr6[1];
            float[] fArr7 = this.xy2;
            this.previousLocationX2 = fArr7[0];
            this.previousLocationY2 = fArr7[1];
            float[] fArr8 = this.cxy;
            this.previousLocationCX = fArr8[0];
            this.previousLocationCY = fArr8[1];
            SelectionView selectionView = this.selectionView;
            if (selectionView != null) {
                selectionView.hide(z2);
            }
        }
        this.lastIsMultitouch = z2;
        float[] fArr9 = this.cxy;
        float f2 = fArr9[0];
        float f3 = fArr9[1];
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.hadMultitouch = false;
            float[] fArr10 = this.xy;
            this.previousLocationX = fArr10[0];
            this.previousLocationY = fArr10[1];
            this.previousLocationCX = f2;
            this.previousLocationCY = f3;
            this.hasReleased = false;
            if ((getParent() instanceof EntitiesContainerView) && (this.stickyX != 0 || this.stickyY != 0)) {
                ((EntitiesContainerView) getParent()).invalidate();
            }
            this.bounce.setPressed(true);
            AndroidUtilities.cancelRunOnUIThread(this.longPressRunnable);
            if (!z2) {
                AndroidUtilities.runOnUIThread(this.longPressRunnable, ViewConfiguration.getLongPressTimeout());
            }
        } else {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    float[] fArr11 = this.xy;
                    float f4 = fArr11[0];
                    float f5 = fArr11[1];
                    float[] fArr12 = this.xy2;
                    z = onTouchMove(f4, f5, z2, fArr12[0], fArr12[1]);
                } else if (actionMasked != 3) {
                    z = false;
                }
                this.hadMultitouch = z2;
                return !super.onTouchEvent(motionEvent) || z;
            }
            onTouchUp(actionMasked == 3);
            this.bounce.setPressed(false);
            SelectionView selectionView2 = this.selectionView;
            if (selectionView2 != null) {
                selectionView2.hide(false);
            }
        }
        z = true;
        this.hadMultitouch = z2;
        if (super.onTouchEvent(motionEvent)) {
        }
    }

    private void runStickyXAnimator(float... fArr) {
        ValueAnimator valueAnimator = this.stickyXAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator duration = ValueAnimator.ofFloat(fArr).setDuration(150L);
        this.stickyXAnimator = duration;
        duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.stickyXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Paint.Views.EntityView$$ExternalSyntheticLambda4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                EntityView.this.lambda$runStickyXAnimator$1(valueAnimator2);
            }
        });
        this.stickyXAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Paint.Views.EntityView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (animator == EntityView.this.stickyXAnimator) {
                    EntityView.this.stickyXAnimator = null;
                }
            }
        });
        this.stickyXAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runStickyXAnimator$1(ValueAnimator valueAnimator) {
        updatePosition();
    }

    private void runStickyYAnimator(float... fArr) {
        ValueAnimator valueAnimator = this.stickyYAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator duration = ValueAnimator.ofFloat(fArr).setDuration(150L);
        this.stickyYAnimator = duration;
        duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.stickyYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Paint.Views.EntityView$$ExternalSyntheticLambda5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                EntityView.this.lambda$runStickyYAnimator$2(valueAnimator2);
            }
        });
        this.stickyYAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Paint.Views.EntityView.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (animator == EntityView.this.stickyYAnimator) {
                    EntityView.this.stickyYAnimator = null;
                }
            }
        });
        this.stickyYAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runStickyYAnimator$2(ValueAnimator valueAnimator) {
        updatePosition();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStickyX() {
        AndroidUtilities.cancelRunOnUIThread(this.setStickyXRunnable);
        int i = this.stickyX;
        int i2 = this.stickyXRunnableValue;
        if (i == i2) {
            return;
        }
        this.stickyX = i2;
        if (getParent() instanceof EntitiesContainerView) {
            ((EntitiesContainerView) getParent()).invalidate();
        }
        ValueAnimator valueAnimator = this.stickyXAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (this.stickyXRunnableValue == 0) {
            runStickyXAnimator(1.0f, 0.0f);
            return;
        }
        try {
            performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        runStickyXAnimator(0.0f, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStickyY() {
        AndroidUtilities.cancelRunOnUIThread(this.setStickyYRunnable);
        int i = this.stickyY;
        int i2 = this.stickyYRunnableValue;
        if (i == i2) {
            return;
        }
        this.stickyY = i2;
        if (getParent() instanceof EntitiesContainerView) {
            ((EntitiesContainerView) getParent()).invalidate();
        }
        ValueAnimator valueAnimator = this.stickyYAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (this.stickyYRunnableValue == 0) {
            runStickyYAnimator(1.0f, 0.0f);
            return;
        }
        try {
            performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        runStickyYAnimator(0.0f, 1.0f);
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0132, code lost:
        if (java.lang.Math.abs((r9.position.y + (((height() / 2.0f) - getStickyPaddingBottom()) * getScaleY())) - (r10.getMeasuredHeight() - org.telegram.messenger.AndroidUtilities.dp(64.0f))) <= org.telegram.messenger.AndroidUtilities.dp(12.0f)) goto L21;
     */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x013a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void pan(float f, float f2) {
        View view;
        int i;
        Point point = this.position;
        point.x += f;
        point.y += f2;
        if (((View) getParent()) != null) {
            int i2 = 3;
            if (!this.lastIsMultitouch) {
                if (Math.abs(this.position.x - (view.getMeasuredWidth() / 2.0f)) <= AndroidUtilities.dp(12.0f) && this.position.y < view.getMeasuredHeight() - AndroidUtilities.dp(112.0f)) {
                    i = 2;
                } else if (Math.abs((this.position.x - (((width() / 2.0f) + getStickyPaddingLeft()) * getScaleX())) - AndroidUtilities.dp(8.0f)) <= AndroidUtilities.dp(12.0f)) {
                    i = 1;
                } else if (Math.abs((this.position.x + (((width() / 2.0f) - getStickyPaddingRight()) * getScaleX())) - (view.getMeasuredWidth() - AndroidUtilities.dp(8.0f))) <= AndroidUtilities.dp(12.0f)) {
                    i = 3;
                }
                if (this.stickyXRunnableValue != i) {
                    this.stickyXRunnableValue = i;
                    if (i == 0) {
                        updateStickyX();
                    } else {
                        AndroidUtilities.runOnUIThread(this.setStickyXRunnable, 250L);
                    }
                }
                if (!this.lastIsMultitouch) {
                    if (Math.abs(this.position.y - (view.getMeasuredHeight() / 2.0f)) <= AndroidUtilities.dp(12.0f)) {
                        i2 = 2;
                    } else if (Math.abs((this.position.y - (((height() / 2.0f) + getStickyPaddingTop()) * getScaleY())) - AndroidUtilities.dp(64.0f)) <= AndroidUtilities.dp(12.0f)) {
                        i2 = 1;
                    }
                    if (this.stickyYRunnableValue != i2) {
                        this.stickyYRunnableValue = i2;
                        if (i2 == 0) {
                            updateStickyY();
                        } else {
                            AndroidUtilities.runOnUIThread(this.setStickyYRunnable, 250L);
                        }
                    }
                }
                i2 = 0;
                if (this.stickyYRunnableValue != i2) {
                }
            }
            i = 0;
            if (this.stickyXRunnableValue != i) {
            }
            if (!this.lastIsMultitouch) {
            }
            i2 = 0;
            if (this.stickyYRunnableValue != i2) {
            }
        }
        updatePosition();
    }

    private float width() {
        double rotation = getRotation() / 180.0f;
        Double.isNaN(rotation);
        double abs = Math.abs(Math.cos(rotation * 3.141592653589793d));
        double measuredWidth = getMeasuredWidth();
        Double.isNaN(measuredWidth);
        double rotation2 = getRotation() / 180.0f;
        Double.isNaN(rotation2);
        double abs2 = Math.abs(Math.sin(rotation2 * 3.141592653589793d));
        double measuredHeight = getMeasuredHeight();
        Double.isNaN(measuredHeight);
        return (float) ((abs * measuredWidth) + (abs2 * measuredHeight));
    }

    private float height() {
        double rotation = getRotation() / 180.0f;
        Double.isNaN(rotation);
        double abs = Math.abs(Math.cos(rotation * 3.141592653589793d));
        double measuredHeight = getMeasuredHeight();
        Double.isNaN(measuredHeight);
        double rotation2 = getRotation() / 180.0f;
        Double.isNaN(rotation2);
        double abs2 = Math.abs(Math.sin(rotation2 * 3.141592653589793d));
        double measuredWidth = getMeasuredWidth();
        Double.isNaN(measuredWidth);
        return (float) ((abs * measuredHeight) + (abs2 * measuredWidth));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getPositionX() {
        float measuredWidth;
        float f = this.position.x;
        if (getParent() != null) {
            View view = (View) getParent();
            int i = this.stickyX;
            if (i == 1) {
                measuredWidth = AndroidUtilities.dp(8.0f) + (((width() / 2.0f) - getStickyPaddingLeft()) * getScaleX());
            } else if (i == 2) {
                measuredWidth = view.getMeasuredWidth() / 2.0f;
            } else {
                measuredWidth = i == 3 ? (view.getMeasuredWidth() - AndroidUtilities.dp(8.0f)) - (((width() / 2.0f) + getStickyPaddingRight()) * getScaleX()) : f;
            }
            ValueAnimator valueAnimator = this.stickyXAnimator;
            if (valueAnimator != null) {
                return AndroidUtilities.lerp(f, measuredWidth, ((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
            return measuredWidth != 0.0f ? measuredWidth : f;
        }
        return f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getPositionY() {
        float measuredHeight;
        float f = this.position.y;
        if (getParent() != null) {
            View view = (View) getParent();
            int i = this.stickyY;
            if (i == 1) {
                measuredHeight = AndroidUtilities.dp(64.0f) + (((height() / 2.0f) - getStickyPaddingTop()) * getScaleY());
            } else if (i == 2) {
                measuredHeight = view.getMeasuredHeight() / 2.0f;
            } else {
                measuredHeight = i == 3 ? (view.getMeasuredHeight() - AndroidUtilities.dp(64.0f)) - (((height() / 2.0f) + getStickyPaddingBottom()) * getScaleY()) : f;
            }
            ValueAnimator valueAnimator = this.stickyYAnimator;
            if (valueAnimator != null) {
                return AndroidUtilities.lerp(f, measuredHeight, ((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
            return measuredHeight != 0.0f ? measuredHeight : f;
        }
        return f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updatePosition() {
        setX(getPositionX() - (getMeasuredWidth() / 2.0f));
        setY(getPositionY() - (getMeasuredHeight() / 2.0f));
        updateSelectionView();
    }

    public void scale(float f) {
        float f2 = this.scale * f;
        this.scale = f2;
        float clamp = Utilities.clamp(Math.max(f2, 0.1f), getMaxScale(), getMinScale());
        if (allowHaptic() && (clamp >= getMaxScale() || clamp <= getMinScale())) {
            try {
                performHapticFeedback(3, 1);
            } catch (Exception unused) {
            }
        }
        setScaleX(clamp);
        setScaleY(clamp);
    }

    public void rotate(float f) {
        if (this.stickyX != 0) {
            this.stickyXRunnableValue = 0;
            updateStickyX();
        }
        if (this.stickyY != 0) {
            this.stickyYRunnableValue = 0;
            updateStickyY();
        }
        this.angle = f;
        boolean z = this.hasStickyAngle;
        if (!z && !this.lastIsMultitouch) {
            Iterator<Integer> it = STICKY_ANGLES.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                final int intValue = it.next().intValue();
                if (Math.abs(intValue - f) < 4.0f) {
                    if (this.stickyAngleRunnableValue != intValue) {
                        this.stickyAngleRunnableValue = intValue;
                        Runnable runnable = this.setStickyAngleRunnable;
                        if (runnable != null) {
                            AndroidUtilities.cancelRunOnUIThread(runnable);
                        }
                        Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.EntityView$$ExternalSyntheticLambda9
                            @Override // java.lang.Runnable
                            public final void run() {
                                EntityView.this.lambda$rotate$4(intValue);
                            }
                        };
                        this.setStickyAngleRunnable = runnable2;
                        AndroidUtilities.runOnUIThread(runnable2, 250L);
                    }
                }
            }
        } else if (z) {
            if (Math.abs(this.currentStickyAngle - f) >= 12.0f || this.lastIsMultitouch) {
                this.stickyAngleRunnableValue = -1;
                Runnable runnable3 = this.setStickyAngleRunnable;
                if (runnable3 != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable3);
                    this.setStickyAngleRunnable = null;
                }
                ValueAnimator valueAnimator = this.angleAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                ValueAnimator valueAnimator2 = this.fromStickyAngleAnimator;
                if (valueAnimator2 != null) {
                    valueAnimator2.cancel();
                }
                ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(150L);
                this.fromStickyAngleAnimator = duration;
                duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
                this.fromStickyAngleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Paint.Views.EntityView$$ExternalSyntheticLambda2
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                        EntityView.this.lambda$rotate$5(valueAnimator3);
                    }
                });
                this.fromStickyAngleAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Paint.Views.EntityView.4
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (animator == EntityView.this.fromStickyAngleAnimator) {
                            EntityView.this.fromStickyAngleAnimator = null;
                        }
                    }
                });
                this.fromStickyAngleAnimator.start();
                this.hasStickyAngle = false;
            } else if (this.angleAnimator != null) {
                f = this.stickyAnimatedAngle;
            } else {
                f = this.currentStickyAngle;
            }
        }
        ValueAnimator valueAnimator3 = this.fromStickyAngleAnimator;
        if (valueAnimator3 != null) {
            this.fromStickyToAngle = f;
            f = AndroidUtilities.lerpAngle(this.fromStickyAnimatedAngle, f, valueAnimator3.getAnimatedFraction());
        }
        rotateInternal(f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rotate$4(int i) {
        this.currentStickyAngle = i;
        this.hasStickyAngle = true;
        try {
            performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        ValueAnimator valueAnimator = this.angleAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator valueAnimator2 = this.fromStickyAngleAnimator;
        if (valueAnimator2 != null) {
            valueAnimator2.cancel();
        }
        ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(150L);
        this.angleAnimator = duration;
        duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.angleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Paint.Views.EntityView$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                EntityView.this.lambda$rotate$3(valueAnimator3);
            }
        });
        this.angleAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Paint.Views.EntityView.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (animator == EntityView.this.angleAnimator) {
                    EntityView.this.angleAnimator = null;
                    EntityView.this.stickyAnimatedAngle = 0.0f;
                }
            }
        });
        this.angleAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rotate$3(ValueAnimator valueAnimator) {
        float lerpAngle = AndroidUtilities.lerpAngle(this.angle, this.currentStickyAngle, valueAnimator.getAnimatedFraction());
        this.stickyAnimatedAngle = lerpAngle;
        rotateInternal(lerpAngle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rotate$5(ValueAnimator valueAnimator) {
        rotateInternal(AndroidUtilities.lerpAngle(this.currentStickyAngle, this.angle, this.fromStickyAngleAnimator.getAnimatedFraction()));
    }

    private void rotateInternal(float f) {
        setRotation(f);
        if (this.stickyX != 0 || this.stickyY != 0) {
            updatePosition();
        }
        updateSelectionView();
    }

    public Rect getSelectionBounds() {
        return new Rect(0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override // android.view.View
    public boolean isSelected() {
        return this.selecting;
    }

    public void updateSelectionView() {
        SelectionView selectionView = this.selectionView;
        if (selectionView != null) {
            selectionView.updatePosition();
        }
    }

    private void updateSelect(ViewGroup viewGroup, boolean z) {
        if (this.selecting != z) {
            this.selecting = z;
            ValueAnimator valueAnimator = this.selectAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.selectAnimator = null;
            }
            if (this.selectionView == null) {
                if (!z && viewGroup == null) {
                    return;
                }
                SelectionView createSelectionView = createSelectionView();
                this.selectionView = createSelectionView;
                createSelectionView.hide(this.lastIsMultitouch);
                viewGroup.addView(this.selectionView);
                this.selectT = 0.0f;
            }
            this.selectionView.updatePosition();
            float[] fArr = new float[2];
            fArr[0] = this.selectT;
            fArr[1] = z ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.selectAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Paint.Views.EntityView$$ExternalSyntheticLambda3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    EntityView.this.lambda$updateSelect$6(valueAnimator2);
                }
            });
            this.selectAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Paint.Views.EntityView.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (EntityView.this.selecting) {
                        return;
                    }
                    AndroidUtilities.removeFromParent(EntityView.this.selectionView);
                    EntityView.this.selectionView = null;
                }
            });
            this.selectAnimator.setDuration(280L);
            this.selectAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            this.selectAnimator.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSelect$6(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.selectT = floatValue;
        SelectionView selectionView = this.selectionView;
        if (selectionView != null) {
            selectionView.setScaleX(AndroidUtilities.lerp(0.9f, 1.0f, floatValue) * Utilities.clamp(this.trashScale * 1.25f, 1.0f, 0.0f));
            this.selectionView.setScaleY(AndroidUtilities.lerp(0.9f, 1.0f, this.selectT) * Utilities.clamp(this.trashScale * 1.25f, 1.0f, 0.0f));
            this.selectionView.setAlpha(this.selectT * Math.max(0.0f, this.trashScale - 0.8f) * 5.0f);
        }
    }

    public boolean isSelectedProgress() {
        return isSelected() || this.selectT > 0.0f;
    }

    public void select(ViewGroup viewGroup) {
        this.lastSelectionContainer = viewGroup;
        updateSelect(viewGroup, true);
    }

    public void deselect() {
        updateSelect(this.lastSelectionContainer, false);
    }

    public void setSelectionVisibility(boolean z) {
        SelectionView selectionView = this.selectionView;
        if (selectionView == null) {
            return;
        }
        selectionView.setVisibility(z ? 0 : 8);
    }

    /* loaded from: classes4.dex */
    public class SelectionView extends FrameLayout {
        private int currentHandle;
        protected Paint dotPaint;
        protected Paint dotStrokePaint;
        protected Paint paint;
        private final AnimatedFloat showAlpha;
        private boolean shown;

        protected int pointInsideHandle(float f, float f2) {
            return 0;
        }

        public SelectionView(Context context) {
            super(context);
            this.paint = new Paint(1);
            this.dotPaint = new Paint(1);
            this.dotStrokePaint = new Paint(1);
            this.showAlpha = new AnimatedFloat(this, 0L, 250L, CubicBezierInterpolator.EASE_OUT_QUINT);
            this.shown = true;
            setWillNotDraw(false);
            this.paint.setColor(-1);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
            this.paint.setStrokeCap(Paint.Cap.ROUND);
            this.paint.setPathEffect(new DashPathEffect(new float[]{AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f)}, 0.5f));
            this.paint.setShadowLayer(AndroidUtilities.dpf2(0.75f), 0.0f, 0.0f, 1342177280);
            this.dotPaint.setColor(-15033089);
            this.dotStrokePaint.setColor(-1);
            this.dotStrokePaint.setStyle(Paint.Style.STROKE);
            this.dotStrokePaint.setStrokeWidth(AndroidUtilities.dpf2(2.66f));
            this.dotStrokePaint.setShadowLayer(AndroidUtilities.dpf2(0.75f), 0.0f, 0.0f, 1342177280);
        }

        protected void updatePosition() {
            Rect selectionBounds = EntityView.this.getSelectionBounds();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
            layoutParams.leftMargin = (int) selectionBounds.x;
            layoutParams.topMargin = (int) selectionBounds.y;
            layoutParams.width = (int) selectionBounds.width;
            layoutParams.height = (int) selectionBounds.height;
            setLayoutParams(layoutParams);
            setRotation(EntityView.this.getRotation());
        }

        /* JADX WARN: Code restructure failed: missing block: B:25:0x0112, code lost:
            if (r2 != 3) goto L23;
         */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean z;
            double atan2;
            int actionMasked = motionEvent.getActionMasked();
            EntityView.this.delegate.getTransformedTouch(motionEvent.getRawX(), motionEvent.getRawY(), EntityView.this.xy);
            boolean z2 = motionEvent.getPointerCount() > 1 && this.currentHandle == 3;
            if (z2) {
                if (Build.VERSION.SDK_INT >= 29) {
                    EntityView.this.delegate.getTransformedTouch(motionEvent.getRawX(1), motionEvent.getRawY(1), EntityView.this.xy2);
                } else {
                    z2 = false;
                }
            }
            if (z2) {
                EntityView.this.cxy[0] = (EntityView.this.xy[0] + EntityView.this.xy2[0]) / 2.0f;
                EntityView.this.cxy[1] = (EntityView.this.xy[1] + EntityView.this.xy2[1]) / 2.0f;
            } else {
                EntityView.this.cxy[0] = EntityView.this.xy[0];
                EntityView.this.cxy[1] = EntityView.this.xy[1];
            }
            if (EntityView.this.lastIsMultitouch != z2) {
                EntityView entityView = EntityView.this;
                entityView.previousLocationX = entityView.xy[0];
                EntityView entityView2 = EntityView.this;
                entityView2.previousLocationY = entityView2.xy[1];
                EntityView entityView3 = EntityView.this;
                entityView3.previousLocationX2 = entityView3.xy2[0];
                EntityView entityView4 = EntityView.this;
                entityView4.previousLocationY2 = entityView4.xy2[1];
                EntityView entityView5 = EntityView.this;
                entityView5.previousLocationCX = entityView5.cxy[0];
                EntityView entityView6 = EntityView.this;
                entityView6.previousLocationCY = entityView6.cxy[1];
                hide(z2);
            }
            EntityView.this.lastIsMultitouch = z2;
            float f = EntityView.this.cxy[0];
            float f2 = EntityView.this.cxy[1];
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked == 2) {
                        int i = this.currentHandle;
                        if (i == 3) {
                            EntityView entityView7 = EntityView.this;
                            z = entityView7.onTouchMove(entityView7.xy[0], EntityView.this.xy[1], z2, EntityView.this.xy2[0], EntityView.this.xy2[1]);
                        } else if (i != 0) {
                            float f3 = f - EntityView.this.previousLocationX;
                            float f4 = f2 - EntityView.this.previousLocationY;
                            if (EntityView.this.hasTransformed || Math.abs(f3) > AndroidUtilities.dp(2.0f) || Math.abs(f4) > AndroidUtilities.dp(2.0f)) {
                                if (!EntityView.this.hasTransformed && EntityView.this.delegate != null) {
                                    EntityView.this.delegate.onEntityHandleTouched();
                                }
                                EntityView.this.hasTransformed = true;
                                AndroidUtilities.cancelRunOnUIThread(EntityView.this.longPressRunnable);
                                int[] centerLocation = EntityView.this.delegate.getCenterLocation(EntityView.this);
                                float distance = MathUtils.distance(centerLocation[0], centerLocation[1], EntityView.this.previousLocationX, EntityView.this.previousLocationY);
                                float distance2 = MathUtils.distance(centerLocation[0], centerLocation[1], f, f2);
                                float f5 = 0.0f;
                                if (distance > 0.0f) {
                                    EntityView.this.scale(distance2 / distance);
                                }
                                int i2 = this.currentHandle;
                                if (i2 != 1) {
                                    if (i2 == 2) {
                                        atan2 = Math.atan2(f2 - centerLocation[1], f - centerLocation[0]);
                                    }
                                    EntityView.this.rotate(((float) Math.toDegrees(f5)) - EntityView.this.delegate.getCropRotation());
                                    EntityView.this.previousLocationX = f;
                                    EntityView.this.previousLocationY = f2;
                                } else {
                                    atan2 = Math.atan2(centerLocation[1] - f2, centerLocation[0] - f);
                                }
                                f5 = (float) atan2;
                                EntityView.this.rotate(((float) Math.toDegrees(f5)) - EntityView.this.delegate.getCropRotation());
                                EntityView.this.previousLocationX = f;
                                EntityView.this.previousLocationY = f2;
                            }
                            z = true;
                        }
                    }
                    z = false;
                }
                EntityView.this.onTouchUp(actionMasked == 3);
                this.currentHandle = 0;
                hide(false);
                z = true;
            } else {
                EntityView.this.hadMultitouch = false;
                int pointInsideHandle = pointInsideHandle(motionEvent.getX(), motionEvent.getY());
                if (pointInsideHandle != 0) {
                    this.currentHandle = pointInsideHandle;
                    EntityView entityView8 = EntityView.this;
                    entityView8.previousLocationX = entityView8.xy[0];
                    EntityView entityView9 = EntityView.this;
                    entityView9.previousLocationY = entityView9.xy[1];
                    EntityView.this.previousLocationCX = f;
                    EntityView.this.previousLocationCY = f2;
                    EntityView.this.hasReleased = false;
                    if (getParent() instanceof EntitiesContainerView) {
                        ((EntitiesContainerView) getParent()).invalidate();
                    }
                    if (pointInsideHandle == 3 && EntityView.this.allowLongPressOnSelected()) {
                        AndroidUtilities.runOnUIThread(EntityView.this.longPressRunnable, ViewConfiguration.getLongPressTimeout());
                    }
                    z = true;
                }
                z = false;
            }
            EntityView.this.hadMultitouch = z2;
            return super.onTouchEvent(motionEvent) || z;
        }

        public void hide(boolean z) {
            this.shown = !z;
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public float getShowAlpha() {
            return this.showAlpha.set(this.shown);
        }
    }

    private void updateTrash(boolean z) {
        if (this.announcedTrash != z) {
            ValueAnimator valueAnimator = this.trashAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.trashAnimator = null;
            }
            float[] fArr = new float[2];
            fArr[0] = this.trashScale;
            fArr[1] = z ? 0.5f : 1.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.trashAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Paint.Views.EntityView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    EntityView.this.lambda$updateTrash$7(valueAnimator2);
                }
            });
            this.trashAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            this.trashAnimator.setDuration(280L);
            this.trashAnimator.start();
            this.announcedTrash = z;
            EntityViewDelegate entityViewDelegate = this.delegate;
            if (entityViewDelegate != null) {
                entityViewDelegate.onEntityDragTrash(z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTrash$7(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.trashScale = floatValue;
        setAlpha(floatValue);
        SelectionView selectionView = this.selectionView;
        if (selectionView != null) {
            selectionView.setScaleX(AndroidUtilities.lerp(0.9f, 1.0f, this.selectT) * Utilities.clamp(this.trashScale * 1.25f, 1.0f, 0.0f));
            this.selectionView.setScaleY(AndroidUtilities.lerp(0.9f, 1.0f, this.selectT) * Utilities.clamp(this.trashScale * 1.25f, 1.0f, 0.0f));
            this.selectionView.setAlpha(this.selectT * Math.max(0.0f, this.trashScale - 0.8f) * 5.0f);
        }
        invalidate();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        float scale = this.bounce.getScale(getBounceScale());
        canvas.save();
        canvas.scale(scale, scale, getWidth() / 2.0f, getHeight() / 2.0f);
        if (getParent() instanceof View) {
            View view = (View) getParent();
            if (trashCenter()) {
                float f = this.trashScale;
                canvas.scale(f, f, getWidth() / 2.0f, getHeight() / 2.0f);
            } else {
                float width = (view.getWidth() / 2.0f) - getX();
                float height = (view.getHeight() - AndroidUtilities.dp(76.0f)) - getY();
                float f2 = this.trashScale;
                canvas.scale(f2, f2, width, height);
            }
        }
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    public boolean hadMultitouch() {
        return this.hadMultitouch;
    }
}
