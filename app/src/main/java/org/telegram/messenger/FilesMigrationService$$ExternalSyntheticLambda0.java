package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class FilesMigrationService$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ FilesMigrationService f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ FilesMigrationService$$ExternalSyntheticLambda0(FilesMigrationService filesMigrationService, int i) {
        this.f$0 = filesMigrationService;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateProgress$1(this.f$1);
    }
}