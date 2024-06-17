package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_updateStoryID extends TLRPC$Update {
    public int id;
    public long random_id;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.id = abstractSerializedData.readInt32(z);
        this.random_id = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(468923833);
        abstractSerializedData.writeInt32(this.id);
        abstractSerializedData.writeInt64(this.random_id);
    }
}