package org.telegram.messenger;

import org.telegram.messenger.ImageLoader;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageLoader$HttpImageTask$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ ImageLoader.HttpImageTask f$0;
    public final /* synthetic */ Boolean f$1;

    public /* synthetic */ ImageLoader$HttpImageTask$$ExternalSyntheticLambda6(ImageLoader.HttpImageTask httpImageTask, Boolean bool) {
        this.f$0 = httpImageTask;
        this.f$1 = bool;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onPostExecute$4(this.f$1);
    }
}