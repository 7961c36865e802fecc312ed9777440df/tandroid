package org.telegram.messenger.voip;

import java.util.ArrayList;
import org.telegram.messenger.ChatObject;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda64 implements ChatObject.Call.OnParticipantsLoad {
    public final /* synthetic */ VoIPService f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int[] f$2;

    public /* synthetic */ VoIPService$$ExternalSyntheticLambda64(VoIPService voIPService, long j, int[] iArr) {
        this.f$0 = voIPService;
        this.f$1 = j;
        this.f$2 = iArr;
    }

    @Override // org.telegram.messenger.ChatObject.Call.OnParticipantsLoad
    public final void onLoad(ArrayList arrayList) {
        this.f$0.lambda$createGroupInstance$39(this.f$1, this.f$2, arrayList);
    }
}