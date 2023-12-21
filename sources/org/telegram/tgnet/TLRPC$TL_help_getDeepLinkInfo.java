package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_help_getDeepLinkInfo extends TLObject {
    public String path;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$help_DeepLinkInfo.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1072547679);
        abstractSerializedData.writeString(this.path);
    }
}
