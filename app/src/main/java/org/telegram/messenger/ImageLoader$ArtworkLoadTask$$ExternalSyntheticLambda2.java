package org.telegram.messenger;

import org.telegram.messenger.ImageLoader;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ImageLoader.ArtworkLoadTask f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda2(ImageLoader.ArtworkLoadTask artworkLoadTask, String str) {
        this.f$0 = artworkLoadTask;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onPostExecute$0(this.f$1);
    }
}