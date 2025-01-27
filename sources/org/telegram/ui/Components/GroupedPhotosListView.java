package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;

/* loaded from: classes3.dex */
public class GroupedPhotosListView extends View implements GestureDetector.OnGestureListener {
    private boolean animateAllLine;
    private boolean animateBackground;
    private int animateToDX;
    private int animateToDXStart;
    private int animateToItem;
    private boolean animateToItemFast;
    private boolean animationsEnabled;
    private Paint backgroundPaint;
    private long currentGroupId;
    private int currentImage;
    private float currentItemProgress;
    private ArrayList currentObjects;
    public ArrayList currentPhotos;
    private GroupedPhotosListViewDelegate delegate;
    private float drawAlpha;
    private int drawDx;
    private GestureDetector gestureDetector;
    private boolean hasPhotos;
    private ValueAnimator hideAnimator;
    private boolean ignoreChanges;
    private ArrayList imagesToDraw;
    private int itemHeight;
    private int itemSpacing;
    private int itemWidth;
    private int itemY;
    private long lastUpdateTime;
    private float moveLineProgress;
    private boolean moving;
    private int nextImage;
    private float nextItemProgress;
    private int nextPhotoScrolling;
    private android.widget.Scroller scroll;
    private boolean scrolling;
    private ValueAnimator showAnimator;
    private boolean stopedScrolling;
    private ArrayList unusedReceivers;

    public interface GroupedPhotosListViewDelegate {
        boolean forceAll();

        long getAvatarsDialogId();

        int getCurrentAccount();

        int getCurrentIndex();

        ArrayList getImagesArr();

        ArrayList getImagesArrLocations();

        List getPageBlockArr();

        Object getParentObject();

        int getSlideshowMessageId();

        void onShowAnimationStart();

        void onStopScrolling();

        void setCurrentIndex(int i);
    }

    public GroupedPhotosListView(Context context, int i) {
        super(context);
        this.backgroundPaint = new Paint();
        this.unusedReceivers = new ArrayList();
        this.imagesToDraw = new ArrayList();
        this.currentPhotos = new ArrayList();
        this.currentObjects = new ArrayList();
        this.currentItemProgress = 1.0f;
        this.nextItemProgress = 0.0f;
        this.animateToItem = -1;
        this.animationsEnabled = true;
        this.nextPhotoScrolling = -1;
        this.animateBackground = true;
        this.gestureDetector = new GestureDetector(context, this);
        this.scroll = new android.widget.Scroller(context);
        this.itemWidth = AndroidUtilities.dp(42.0f);
        this.itemHeight = AndroidUtilities.dp(56.0f);
        this.itemSpacing = AndroidUtilities.dp(1.0f);
        this.itemY = i;
        this.backgroundPaint.setColor(2130706432);
    }

    private void fillImages(boolean z, int i) {
        int i2;
        int i3;
        Object obj;
        Object obj2;
        if (!z && !this.imagesToDraw.isEmpty()) {
            this.unusedReceivers.addAll(this.imagesToDraw);
            this.imagesToDraw.clear();
            this.moving = false;
            this.moveLineProgress = 1.0f;
            this.currentItemProgress = 1.0f;
            this.nextItemProgress = 0.0f;
        }
        invalidate();
        if (getMeasuredWidth() == 0 || this.currentPhotos.isEmpty()) {
            return;
        }
        int measuredWidth = getMeasuredWidth();
        int measuredWidth2 = (getMeasuredWidth() / 2) - (this.itemWidth / 2);
        if (z) {
            int size = this.imagesToDraw.size();
            int i4 = 0;
            i2 = Integer.MIN_VALUE;
            i3 = ConnectionsManager.DEFAULT_DATACENTER_ID;
            while (i4 < size) {
                ImageReceiver imageReceiver = (ImageReceiver) this.imagesToDraw.get(i4);
                int param = imageReceiver.getParam();
                int i5 = param - this.currentImage;
                int i6 = this.itemWidth;
                int i7 = (i5 * (this.itemSpacing + i6)) + measuredWidth2 + i;
                if (i7 > measuredWidth || i7 + i6 < 0) {
                    this.unusedReceivers.add(imageReceiver);
                    this.imagesToDraw.remove(i4);
                    size--;
                    i4--;
                }
                i3 = Math.min(i3, param - 1);
                i2 = Math.max(i2, param + 1);
                i4++;
            }
        } else {
            i2 = this.currentImage;
            i3 = i2 - 1;
        }
        if (i2 != Integer.MIN_VALUE) {
            int size2 = this.currentPhotos.size();
            while (i2 < size2) {
                int i8 = ((i2 - this.currentImage) * (this.itemWidth + this.itemSpacing)) + measuredWidth2 + i;
                if (i8 >= measuredWidth) {
                    break;
                }
                ImageLocation imageLocation = (ImageLocation) this.currentPhotos.get(i2);
                ImageReceiver freeReceiver = getFreeReceiver();
                freeReceiver.setImageCoords(i8, this.itemY, this.itemWidth, this.itemHeight);
                if (this.currentObjects.get(0) instanceof MessageObject) {
                    obj2 = this.currentObjects.get(i2);
                } else if (this.currentObjects.get(0) instanceof TLRPC.PageBlock) {
                    obj2 = this.delegate.getParentObject();
                } else {
                    obj2 = "avatar_" + this.delegate.getAvatarsDialogId();
                }
                freeReceiver.setImage(null, null, imageLocation, "80_80", 0L, null, obj2, 1);
                freeReceiver.setParam(i2);
                i2++;
            }
        }
        if (i3 != Integer.MAX_VALUE) {
            while (i3 >= 0) {
                int i9 = i3 - this.currentImage;
                int i10 = this.itemWidth;
                int i11 = (i9 * (this.itemSpacing + i10)) + measuredWidth2 + i + i10;
                if (i11 <= 0) {
                    break;
                }
                ImageLocation imageLocation2 = (ImageLocation) this.currentPhotos.get(i3);
                ImageReceiver freeReceiver2 = getFreeReceiver();
                freeReceiver2.setImageCoords(i11, this.itemY, this.itemWidth, this.itemHeight);
                if (this.currentObjects.get(0) instanceof MessageObject) {
                    obj = this.currentObjects.get(i3);
                } else if (this.currentObjects.get(0) instanceof TLRPC.PageBlock) {
                    obj = this.delegate.getParentObject();
                } else {
                    obj = "avatar_" + this.delegate.getAvatarsDialogId();
                }
                freeReceiver2.setImage(null, null, imageLocation2, "80_80", 0L, null, obj, 1);
                freeReceiver2.setParam(i3);
                i3--;
            }
        }
        ValueAnimator valueAnimator = this.showAnimator;
        if (valueAnimator == null || valueAnimator.isStarted()) {
            return;
        }
        this.showAnimator.start();
    }

    private ImageReceiver getFreeReceiver() {
        ImageReceiver imageReceiver;
        if (this.unusedReceivers.isEmpty()) {
            imageReceiver = new ImageReceiver(this);
            imageReceiver.setAllowLoadingOnAttachedOnly(false);
        } else {
            imageReceiver = (ImageReceiver) this.unusedReceivers.get(0);
            this.unusedReceivers.remove(0);
        }
        this.imagesToDraw.add(imageReceiver);
        imageReceiver.setCurrentAccount(this.delegate.getCurrentAccount());
        return imageReceiver;
    }

    private int getMaxScrollX() {
        return this.currentImage * (this.itemWidth + (this.itemSpacing * 2));
    }

    private int getMinScrollX() {
        return (-((this.currentPhotos.size() - this.currentImage) - 1)) * (this.itemWidth + (this.itemSpacing * 2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillList$0(ValueAnimator valueAnimator) {
        this.drawAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillList$1(ValueAnimator valueAnimator) {
        this.drawAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    private void stopScrolling() {
        this.scrolling = false;
        if (!this.scroll.isFinished()) {
            this.scroll.abortAnimation();
        }
        int i = this.nextPhotoScrolling;
        if (i >= 0 && i < this.currentObjects.size()) {
            this.stopedScrolling = true;
            this.animateToItemFast = false;
            int i2 = this.nextPhotoScrolling;
            this.animateToItem = i2;
            this.nextImage = i2;
            this.animateToDX = (this.currentImage - i2) * (this.itemWidth + this.itemSpacing);
            this.animateToDXStart = this.drawDx;
            this.moveLineProgress = 1.0f;
            this.nextPhotoScrolling = -1;
            GroupedPhotosListViewDelegate groupedPhotosListViewDelegate = this.delegate;
            if (groupedPhotosListViewDelegate != null) {
                groupedPhotosListViewDelegate.onStopScrolling();
            }
        }
        invalidate();
    }

    private void updateAfterScroll() {
        int i;
        int i2;
        int i3;
        int i4 = this.drawDx;
        int abs = Math.abs(i4);
        int i5 = this.itemWidth;
        int i6 = this.itemSpacing;
        int i7 = (i5 / 2) + i6;
        int i8 = -1;
        if (abs > i7) {
            if (i4 > 0) {
                i2 = i4 - i7;
                i3 = 1;
            } else {
                i2 = i4 + i7;
                i3 = -1;
            }
            i = i3 + (i2 / (i5 + (i6 * 2)));
        } else {
            i = 0;
        }
        this.nextPhotoScrolling = this.currentImage - i;
        int currentIndex = this.delegate.getCurrentIndex();
        ArrayList imagesArrLocations = this.delegate.getImagesArrLocations();
        ArrayList imagesArr = this.delegate.getImagesArr();
        List pageBlockArr = this.delegate.getPageBlockArr();
        int i9 = this.nextPhotoScrolling;
        if (currentIndex != i9 && i9 >= 0 && i9 < this.currentPhotos.size()) {
            Object obj = this.currentObjects.get(this.nextPhotoScrolling);
            if (imagesArr != null && !imagesArr.isEmpty()) {
                i8 = imagesArr.indexOf((MessageObject) obj);
            } else if (pageBlockArr != null && !pageBlockArr.isEmpty()) {
                i8 = pageBlockArr.indexOf((TLRPC.PageBlock) obj);
            } else if (imagesArrLocations != null && !imagesArrLocations.isEmpty()) {
                i8 = imagesArrLocations.indexOf((ImageLocation) obj);
            }
            if (i8 >= 0) {
                this.ignoreChanges = true;
                this.delegate.setCurrentIndex(i8);
            }
        }
        if (!this.scrolling) {
            this.scrolling = true;
            this.stopedScrolling = false;
        }
        fillImages(true, this.drawDx);
    }

    public void clear() {
        this.currentPhotos.clear();
        this.currentObjects.clear();
        this.imagesToDraw.clear();
    }

    /* JADX WARN: Removed duplicated region for block: B:138:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0265  */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v13, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r3v21 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void fillList() {
        ArrayList arrayList;
        boolean z;
        Object obj;
        TLRPC.PageBlock pageBlock;
        boolean z2;
        int i;
        ?? r3;
        int i2 = 0;
        if (this.ignoreChanges) {
            this.ignoreChanges = false;
            return;
        }
        int currentIndex = this.delegate.getCurrentIndex();
        ArrayList imagesArrLocations = this.delegate.getImagesArrLocations();
        ArrayList imagesArr = this.delegate.getImagesArr();
        List pageBlockArr = this.delegate.getPageBlockArr();
        int slideshowMessageId = this.delegate.getSlideshowMessageId();
        this.delegate.getCurrentAccount();
        this.hasPhotos = false;
        if (imagesArrLocations != null && !imagesArrLocations.isEmpty()) {
            if (currentIndex >= imagesArrLocations.size()) {
                currentIndex = imagesArrLocations.size() - 1;
            }
            ImageLocation imageLocation = (ImageLocation) imagesArrLocations.get(currentIndex);
            int size = imagesArrLocations.size();
            this.hasPhotos = true;
            arrayList = imagesArrLocations;
            i2 = size;
            z = false;
            obj = imageLocation;
        } else if (imagesArr == null || imagesArr.isEmpty()) {
            arrayList = imagesArrLocations;
            if (pageBlockArr == null || pageBlockArr.isEmpty()) {
                z = false;
                i2 = 0;
                obj = null;
            } else {
                TLRPC.PageBlock pageBlock2 = (TLRPC.PageBlock) pageBlockArr.get(currentIndex);
                long j = pageBlock2.groupId;
                if (j != this.currentGroupId) {
                    this.currentGroupId = j;
                    z = true;
                } else {
                    z = false;
                }
                if (this.currentGroupId != 0) {
                    this.hasPhotos = true;
                    int size2 = pageBlockArr.size();
                    int i3 = currentIndex;
                    i2 = 0;
                    TLRPC.PageBlock pageBlock3 = pageBlock2;
                    while (true) {
                        if (i3 >= size2) {
                            pageBlock = pageBlock3;
                            break;
                        }
                        pageBlock = pageBlock3;
                        if (((TLRPC.PageBlock) pageBlockArr.get(i3)).groupId != this.currentGroupId) {
                            break;
                        }
                        i2++;
                        i3++;
                        pageBlock3 = pageBlock;
                    }
                    for (int i4 = currentIndex - 1; i4 >= 0 && ((TLRPC.PageBlock) pageBlockArr.get(i4)).groupId == this.currentGroupId; i4--) {
                        i2++;
                    }
                    obj = pageBlock;
                } else {
                    i2 = 0;
                    obj = pageBlock2;
                }
            }
        } else {
            if (currentIndex >= imagesArr.size()) {
                currentIndex = imagesArr.size() - 1;
            }
            MessageObject messageObject = (MessageObject) imagesArr.get(currentIndex);
            long groupIdForUse = messageObject.getGroupIdForUse();
            if (groupIdForUse != this.currentGroupId) {
                this.currentGroupId = groupIdForUse;
                z = true;
            } else {
                z = false;
            }
            boolean z3 = imagesArr.size() > 1 && this.delegate.forceAll();
            if (this.currentGroupId != 0 || z3) {
                this.hasPhotos = true;
                int min = Math.min(currentIndex + 10, imagesArr.size());
                int i5 = 0;
                for (int i6 = currentIndex; i6 < min; i6++) {
                    MessageObject messageObject2 = (MessageObject) imagesArr.get(i6);
                    if (slideshowMessageId == 0 && !z3 && messageObject2.getGroupIdForUse() != this.currentGroupId) {
                        break;
                    }
                    i5++;
                }
                int max = Math.max(currentIndex - 10, 0);
                int i7 = currentIndex - 1;
                int i8 = i5;
                while (true) {
                    if (i7 < max) {
                        arrayList = imagesArrLocations;
                        break;
                    }
                    MessageObject messageObject3 = (MessageObject) imagesArr.get(i7);
                    if (slideshowMessageId == 0 && !z3) {
                        arrayList = imagesArrLocations;
                        if (messageObject3.getGroupIdForUse() != this.currentGroupId) {
                            break;
                        }
                    } else {
                        arrayList = imagesArrLocations;
                    }
                    i8++;
                    i7--;
                    imagesArrLocations = arrayList;
                }
                i2 = i8;
                obj = messageObject;
            } else {
                arrayList = imagesArrLocations;
                obj = messageObject;
            }
        }
        if (obj == null) {
            return;
        }
        if (this.animationsEnabled) {
            if (this.hasPhotos) {
                ValueAnimator valueAnimator = this.hideAnimator;
                if (valueAnimator != null) {
                    this.hideAnimator = null;
                    valueAnimator.cancel();
                }
                float f = this.drawAlpha;
                if (f < 1.0f && this.showAnimator == null) {
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(f, 1.0f);
                    this.showAnimator = ofFloat;
                    ofFloat.setDuration((long) ((1.0f - this.drawAlpha) * 200.0f));
                    this.showAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.GroupedPhotosListView.2
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (GroupedPhotosListView.this.showAnimator == animator) {
                                GroupedPhotosListView.this.showAnimator = null;
                            }
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationStart(Animator animator) {
                            if (GroupedPhotosListView.this.delegate != null) {
                                GroupedPhotosListView.this.delegate.onShowAnimationStart();
                            }
                        }
                    });
                    this.showAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.GroupedPhotosListView$$ExternalSyntheticLambda1
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                            GroupedPhotosListView.this.lambda$fillList$1(valueAnimator2);
                        }
                    });
                }
            } else {
                ValueAnimator valueAnimator2 = this.showAnimator;
                if (valueAnimator2 != null) {
                    valueAnimator2.cancel();
                    this.showAnimator = null;
                }
                if (this.drawAlpha > 0.0f && this.currentPhotos.size() > 1) {
                    if (this.hideAnimator == null) {
                        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.drawAlpha, 0.0f);
                        this.hideAnimator = ofFloat2;
                        ofFloat2.setDuration((long) (this.drawAlpha * 200.0f));
                        this.hideAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.GroupedPhotosListView.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                if (GroupedPhotosListView.this.hideAnimator == animator) {
                                    GroupedPhotosListView.this.hideAnimator = null;
                                    GroupedPhotosListView.this.fillList();
                                }
                            }
                        });
                        this.hideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.GroupedPhotosListView$$ExternalSyntheticLambda0
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                                GroupedPhotosListView.this.lambda$fillList$0(valueAnimator3);
                            }
                        });
                        this.hideAnimator.start();
                        return;
                    }
                    return;
                }
            }
        }
        if (!z) {
            if (i2 == this.currentPhotos.size() && this.currentObjects.contains(obj)) {
                int indexOf = this.currentObjects.indexOf(obj);
                int i9 = this.currentImage;
                if (i9 != indexOf && indexOf != -1) {
                    boolean z4 = this.animateAllLine;
                    if (!z4 && !this.moving && (indexOf == i9 - 1 || indexOf == i9 + 1)) {
                        this.animateToItemFast = true;
                        z4 = true;
                    }
                    if (z4) {
                        this.animateToItem = indexOf;
                        this.nextImage = indexOf;
                        this.animateToDX = (i9 - indexOf) * (this.itemWidth + this.itemSpacing);
                        this.moving = true;
                        this.animateAllLine = false;
                        this.lastUpdateTime = System.currentTimeMillis();
                        invalidate();
                        z2 = false;
                    } else {
                        fillImages(true, (i9 - indexOf) * (this.itemWidth + this.itemSpacing));
                        this.currentImage = indexOf;
                        z2 = false;
                        this.moving = false;
                    }
                    this.drawDx = z2 ? 1 : 0;
                }
            } else {
                z2 = false;
                z = true;
            }
            if (z) {
                return;
            }
            int size3 = this.currentPhotos.size();
            this.animateAllLine = z2;
            this.currentPhotos.clear();
            this.currentObjects.clear();
            if (arrayList != null && !arrayList.isEmpty()) {
                ArrayList arrayList2 = arrayList;
                this.currentObjects.addAll(arrayList2);
                this.currentPhotos.addAll(arrayList2);
                this.currentImage = currentIndex;
                this.animateToItem = -1;
                this.animateToItemFast = false;
            } else if (imagesArr == null || imagesArr.isEmpty()) {
                int i10 = currentIndex;
                if (pageBlockArr != null && !pageBlockArr.isEmpty() && this.currentGroupId != 0) {
                    int size4 = pageBlockArr.size();
                    for (int i11 = i10; i11 < size4; i11++) {
                        TLRPC.PageBlock pageBlock4 = (TLRPC.PageBlock) pageBlockArr.get(i11);
                        if (pageBlock4.groupId != this.currentGroupId) {
                            break;
                        }
                        this.currentObjects.add(pageBlock4);
                        this.currentPhotos.add(ImageLocation.getForObject(pageBlock4.thumb, pageBlock4.thumbObject));
                    }
                    this.currentImage = 0;
                    this.animateToItem = -1;
                    this.animateToItemFast = false;
                    for (int i12 = i10 - 1; i12 >= 0; i12--) {
                        TLRPC.PageBlock pageBlock5 = (TLRPC.PageBlock) pageBlockArr.get(i12);
                        if (pageBlock5.groupId != this.currentGroupId) {
                            break;
                        }
                        this.currentObjects.add(0, pageBlock5);
                        this.currentPhotos.add(0, ImageLocation.getForObject(pageBlock5.thumb, pageBlock5.thumbObject));
                        this.currentImage++;
                    }
                }
            } else {
                boolean z5 = this.delegate.forceAll() && imagesArr.size() > 1;
                if (this.currentGroupId != 0 || z5 || slideshowMessageId != 0) {
                    int min2 = Math.min(currentIndex + 10, imagesArr.size());
                    int i13 = currentIndex;
                    while (true) {
                        if (i13 >= min2) {
                            i = currentIndex;
                            r3 = 0;
                            break;
                        }
                        MessageObject messageObject4 = (MessageObject) imagesArr.get(i13);
                        if (slideshowMessageId == 0 && !z5) {
                            i = currentIndex;
                            if (messageObject4.getGroupIdForUse() != this.currentGroupId) {
                                r3 = 0;
                                break;
                            }
                        } else {
                            i = currentIndex;
                        }
                        this.currentObjects.add(messageObject4);
                        this.currentPhotos.add(ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(messageObject4.photoThumbs, 56, true), messageObject4.photoThumbsObject));
                        i13++;
                        currentIndex = i;
                    }
                    this.currentImage = r3;
                    this.animateToItem = -1;
                    this.animateToItemFast = r3;
                    int max2 = Math.max(i - 10, (int) r3);
                    for (int i14 = i - 1; i14 >= max2; i14--) {
                        MessageObject messageObject5 = (MessageObject) imagesArr.get(i14);
                        if (slideshowMessageId == 0 && !z5 && messageObject5.getGroupIdForUse() != this.currentGroupId) {
                            break;
                        }
                        this.currentObjects.add(0, messageObject5);
                        this.currentPhotos.add(0, ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(messageObject5.photoThumbs, 56, true), messageObject5.photoThumbsObject));
                        this.currentImage++;
                    }
                }
            }
            if (this.currentPhotos.size() == 1) {
                this.currentPhotos.clear();
                this.currentObjects.clear();
            }
            if (this.currentPhotos.size() != size3) {
                requestLayout();
            }
            fillImages(false, 0);
            return;
        }
        z2 = false;
        if (z) {
        }
    }

    public int getCount() {
        return this.currentPhotos.size();
    }

    public int getIndex() {
        return this.currentImage;
    }

    public boolean hasPhotos() {
        ValueAnimator valueAnimator;
        return this.hasPhotos && this.hideAnimator == null && (this.drawAlpha > 0.0f || !this.animateBackground || ((valueAnimator = this.showAnimator) != null && valueAnimator.isStarted()));
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent motionEvent) {
        if (!this.scroll.isFinished()) {
            this.scroll.abortAnimation();
        }
        this.animateToItem = -1;
        this.animateToItemFast = false;
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x0184  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x024a  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        int i;
        float interpolation;
        int ceil;
        int i2;
        int i3;
        TLRPC.PhotoSize photoSize;
        TLRPC.PhotoSize photoSize2;
        if (this.hasPhotos || !this.imagesToDraw.isEmpty()) {
            float f = this.drawAlpha;
            if (!this.animateBackground) {
                f = this.hasPhotos ? 1.0f : 0.0f;
            }
            this.backgroundPaint.setAlpha((int) (f * 127.0f));
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.backgroundPaint);
            if (this.imagesToDraw.isEmpty()) {
                return;
            }
            int size = this.imagesToDraw.size();
            int i4 = this.drawDx;
            int i5 = (int) (this.itemWidth * 2.0f);
            int dp = AndroidUtilities.dp(8.0f);
            ImageLocation imageLocation = (ImageLocation) this.currentPhotos.get(this.currentImage);
            int min = Math.min(i5, (imageLocation == null || (photoSize2 = imageLocation.photoSize) == null) ? this.itemHeight : Math.max(this.itemWidth, (int) (photoSize2.w * (this.itemHeight / photoSize2.h))));
            float f2 = dp * 2;
            float f3 = this.currentItemProgress;
            int i6 = (int) (f2 * f3);
            int i7 = this.itemWidth + ((int) ((min - r11) * f3)) + i6;
            int i8 = this.nextImage;
            if (i8 < 0 || i8 >= this.currentPhotos.size()) {
                i = this.itemWidth;
            } else {
                ImageLocation imageLocation2 = (ImageLocation) this.currentPhotos.get(this.nextImage);
                i = (imageLocation2 == null || (photoSize = imageLocation2.photoSize) == null) ? this.itemHeight : Math.max(this.itemWidth, (int) (photoSize.w * (this.itemHeight / photoSize.h)));
            }
            int min2 = Math.min(i5, i);
            float f4 = this.nextItemProgress;
            int i9 = (int) (f2 * f4);
            float f5 = i4;
            int i10 = 1;
            int i11 = (int) (f5 + ((((min2 + i9) - r12) / 2) * f4 * (this.nextImage > this.currentImage ? -1 : 1)));
            int i12 = this.itemWidth + ((int) ((min2 - r12) * f4)) + i9;
            int measuredWidth = (getMeasuredWidth() - i7) / 2;
            int i13 = 0;
            while (i13 < size) {
                ImageReceiver imageReceiver = (ImageReceiver) this.imagesToDraw.get(i13);
                int param = imageReceiver.getParam();
                int i14 = this.currentImage;
                if (param == i14) {
                    imageReceiver.setImageX(measuredWidth + i11 + (i6 / 2));
                    imageReceiver.setImageWidth(i7 - i6);
                } else {
                    int i15 = this.nextImage;
                    if (i15 < i14) {
                        if (param < i14) {
                            if (param <= i15) {
                                int param2 = (imageReceiver.getParam() - this.currentImage) + i10;
                                int i16 = this.itemWidth;
                                int i17 = this.itemSpacing;
                                i3 = ((param2 * (i16 + i17)) + measuredWidth) - (i17 + i12);
                                imageReceiver.setImageX(i3 + i11);
                                if (param != this.nextImage) {
                                    imageReceiver.setImageWidth(i12 - i9);
                                    imageReceiver.setImageX((int) (imageReceiver.getImageX() + (i9 / 2)));
                                } else {
                                    imageReceiver.setImageWidth(this.itemWidth);
                                }
                            }
                            i3 = ((imageReceiver.getParam() - this.currentImage) * (this.itemWidth + this.itemSpacing)) + measuredWidth;
                            imageReceiver.setImageX(i3 + i11);
                            if (param != this.nextImage) {
                            }
                        }
                        i3 = measuredWidth + i7 + this.itemSpacing + (((imageReceiver.getParam() - this.currentImage) - i10) * (this.itemWidth + this.itemSpacing));
                        imageReceiver.setImageX(i3 + i11);
                        if (param != this.nextImage) {
                        }
                    } else {
                        if (param >= i14) {
                            if (param > i15) {
                                int i18 = measuredWidth + i7 + this.itemSpacing;
                                int param3 = (imageReceiver.getParam() - this.currentImage) - 2;
                                int i19 = this.itemWidth;
                                int i20 = this.itemSpacing;
                                i3 = i18 + (param3 * (i19 + i20)) + i20 + i12;
                                imageReceiver.setImageX(i3 + i11);
                                if (param != this.nextImage) {
                                }
                            }
                            i3 = measuredWidth + i7 + this.itemSpacing + (((imageReceiver.getParam() - this.currentImage) - i10) * (this.itemWidth + this.itemSpacing));
                            imageReceiver.setImageX(i3 + i11);
                            if (param != this.nextImage) {
                            }
                        }
                        i3 = ((imageReceiver.getParam() - this.currentImage) * (this.itemWidth + this.itemSpacing)) + measuredWidth;
                        imageReceiver.setImageX(i3 + i11);
                        if (param != this.nextImage) {
                        }
                    }
                }
                imageReceiver.setAlpha(this.drawAlpha);
                imageReceiver.setRoundRadius(AndroidUtilities.dp(2.0f));
                imageReceiver.draw(canvas);
                i13++;
                i10 = 1;
            }
            long currentTimeMillis = System.currentTimeMillis();
            long j = currentTimeMillis - this.lastUpdateTime;
            if (j > 17) {
                j = 17;
            }
            this.lastUpdateTime = currentTimeMillis;
            int i21 = this.animateToItem;
            if (i21 >= 0) {
                float f6 = this.moveLineProgress;
                if (f6 > 0.0f) {
                    float f7 = j;
                    float f8 = f6 - (f7 / (this.animateToItemFast ? 100.0f : 200.0f));
                    this.moveLineProgress = f8;
                    if (i21 == this.currentImage) {
                        float f9 = this.currentItemProgress;
                        if (f9 < 1.0f) {
                            float f10 = f9 + (f7 / 200.0f);
                            this.currentItemProgress = f10;
                            if (f10 > 1.0f) {
                                this.currentItemProgress = 1.0f;
                            }
                        }
                        i2 = this.animateToDXStart;
                        interpolation = this.currentItemProgress;
                    } else {
                        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT;
                        interpolation = cubicBezierInterpolator.getInterpolation(1.0f - f8);
                        this.nextItemProgress = interpolation;
                        if (this.stopedScrolling) {
                            float f11 = this.currentItemProgress;
                            if (f11 > 0.0f) {
                                float f12 = f11 - (f7 / 200.0f);
                                this.currentItemProgress = f12;
                                if (f12 < 0.0f) {
                                    this.currentItemProgress = 0.0f;
                                }
                            }
                            i2 = this.animateToDXStart;
                        } else {
                            this.currentItemProgress = cubicBezierInterpolator.getInterpolation(this.moveLineProgress);
                            ceil = (int) Math.ceil(this.nextItemProgress * this.animateToDX);
                            this.drawDx = ceil;
                            if (this.moveLineProgress <= 0.0f) {
                                this.currentImage = this.animateToItem;
                                this.moveLineProgress = 1.0f;
                                this.currentItemProgress = 1.0f;
                                this.nextItemProgress = 0.0f;
                                this.moving = false;
                                this.stopedScrolling = false;
                                this.drawDx = 0;
                                this.animateToItem = -1;
                                this.animateToItemFast = false;
                            }
                        }
                    }
                    ceil = i2 + ((int) Math.ceil(interpolation * (this.animateToDX - i2)));
                    this.drawDx = ceil;
                    if (this.moveLineProgress <= 0.0f) {
                    }
                }
                fillImages(true, this.drawDx);
                invalidate();
            }
            if (this.scrolling) {
                float f13 = this.currentItemProgress;
                if (f13 > 0.0f) {
                    float f14 = f13 - (j / 200.0f);
                    this.currentItemProgress = f14;
                    if (f14 < 0.0f) {
                        this.currentItemProgress = 0.0f;
                    }
                    invalidate();
                }
            }
            if (this.scroll.isFinished()) {
                return;
            }
            if (this.scroll.computeScrollOffset()) {
                this.drawDx = this.scroll.getCurrX();
                updateAfterScroll();
                invalidate();
            }
            if (this.scroll.isFinished()) {
                stopScrolling();
            }
        }
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.scroll.abortAnimation();
        if (this.currentPhotos.size() < 10) {
            return false;
        }
        this.scroll.fling(this.drawDx, 0, Math.round(f), 0, getMinScrollX(), getMaxScrollX(), 0, 0);
        return false;
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        fillImages(false, 0);
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.drawDx = (int) (this.drawDx - f);
        int minScrollX = getMinScrollX();
        int maxScrollX = getMaxScrollX();
        int i = this.drawDx;
        if (i < minScrollX) {
            this.drawDx = minScrollX;
        } else if (i > maxScrollX) {
            this.drawDx = maxScrollX;
        }
        updateAfterScroll();
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        int indexOf;
        int currentIndex = this.delegate.getCurrentIndex();
        ArrayList imagesArrLocations = this.delegate.getImagesArrLocations();
        ArrayList imagesArr = this.delegate.getImagesArr();
        List pageBlockArr = this.delegate.getPageBlockArr();
        stopScrolling();
        int size = this.imagesToDraw.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            }
            ImageReceiver imageReceiver = (ImageReceiver) this.imagesToDraw.get(i);
            if (imageReceiver.isInsideImage(motionEvent.getX(), motionEvent.getY())) {
                int param = imageReceiver.getParam();
                if (param < 0 || param >= this.currentObjects.size()) {
                    return true;
                }
                if (imagesArr != null && !imagesArr.isEmpty()) {
                    indexOf = imagesArr.indexOf((MessageObject) this.currentObjects.get(param));
                    if (currentIndex == indexOf) {
                        return true;
                    }
                } else if (pageBlockArr != null && !pageBlockArr.isEmpty()) {
                    indexOf = pageBlockArr.indexOf((TLRPC.PageBlock) this.currentObjects.get(param));
                    if (currentIndex == indexOf) {
                        return true;
                    }
                } else if (imagesArrLocations != null && !imagesArrLocations.isEmpty()) {
                    indexOf = imagesArrLocations.indexOf((ImageLocation) this.currentObjects.get(param));
                    if (currentIndex == indexOf) {
                        return true;
                    }
                }
                this.moveLineProgress = 1.0f;
                this.animateAllLine = true;
                this.delegate.setCurrentIndex(indexOf);
            } else {
                i++;
            }
        }
        return false;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.currentPhotos.isEmpty() && getAlpha() == 1.0f) {
            r1 = this.gestureDetector.onTouchEvent(motionEvent) || super.onTouchEvent(motionEvent);
            if (this.scrolling && motionEvent.getAction() == 1 && this.scroll.isFinished()) {
                stopScrolling();
            }
        }
        return r1;
    }

    public void reset() {
        this.hasPhotos = false;
        if (this.animationsEnabled) {
            this.drawAlpha = 0.0f;
        }
    }

    public void setAnimateBackground(boolean z) {
        this.animateBackground = z;
    }

    public void setAnimationsEnabled(boolean z) {
        if (this.animationsEnabled != z) {
            this.animationsEnabled = z;
            if (z) {
                return;
            }
            ValueAnimator valueAnimator = this.showAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.showAnimator = null;
            }
            ValueAnimator valueAnimator2 = this.hideAnimator;
            if (valueAnimator2 != null) {
                valueAnimator2.cancel();
                this.hideAnimator = null;
            }
            this.drawAlpha = 0.0f;
            invalidate();
        }
    }

    public void setDelegate(GroupedPhotosListViewDelegate groupedPhotosListViewDelegate) {
        this.delegate = groupedPhotosListViewDelegate;
    }

    public void setMoveProgress(float f) {
        if (this.scrolling || this.animateToItem >= 0) {
            return;
        }
        this.nextImage = f > 0.0f ? this.currentImage - 1 : this.currentImage + 1;
        int i = this.nextImage;
        if (i < 0 || i >= this.currentPhotos.size()) {
            this.currentItemProgress = 1.0f;
        } else {
            this.currentItemProgress = 1.0f - Math.abs(f);
        }
        this.nextItemProgress = 1.0f - this.currentItemProgress;
        this.moving = f != 0.0f;
        invalidate();
        if (this.currentPhotos.isEmpty()) {
            return;
        }
        if (f >= 0.0f || this.currentImage != this.currentPhotos.size() - 1) {
            if (f <= 0.0f || this.currentImage != 0) {
                int i2 = (int) (f * (this.itemWidth + this.itemSpacing));
                this.drawDx = i2;
                fillImages(true, i2);
            }
        }
    }
}
