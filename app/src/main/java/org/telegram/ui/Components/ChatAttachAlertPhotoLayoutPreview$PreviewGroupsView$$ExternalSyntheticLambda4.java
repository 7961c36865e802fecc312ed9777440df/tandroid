package org.telegram.ui.Components;

import org.telegram.messenger.MediaController;
import org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView f$0;
    public final /* synthetic */ ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.PreviewGroupCell f$1;
    public final /* synthetic */ MediaController.PhotoEntry f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$$ExternalSyntheticLambda4(ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView previewGroupsView, ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.PreviewGroupCell previewGroupCell, MediaController.PhotoEntry photoEntry, int i) {
        this.f$0 = previewGroupsView;
        this.f$1 = previewGroupCell;
        this.f$2 = photoEntry;
        this.f$3 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onTouchEvent$3(this.f$1, this.f$2, this.f$3);
    }
}