package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_reactionNotificationsFromAll extends TLRPC$ReactionNotificationsFrom {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1268654752);
    }
}