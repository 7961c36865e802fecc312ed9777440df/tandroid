package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda51 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ long f$4;
    public final /* synthetic */ String f$5;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda51(MessagesStorage messagesStorage, int i, long j, boolean z, long j2, String str) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = j;
        this.f$3 = z;
        this.f$4 = j2;
        this.f$5 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateMessageVoiceTranscription$77(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}