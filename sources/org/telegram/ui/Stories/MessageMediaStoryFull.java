package org.telegram.ui.Stories;

import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.TLRPC$StoryItem;
import org.telegram.tgnet.TLRPC$TL_messageMediaStory;
/* loaded from: classes4.dex */
public class MessageMediaStoryFull extends TLRPC$TL_messageMediaStory {
    public static int constructor = -946147811;

    @Override // org.telegram.tgnet.TLRPC$TL_messageMediaStory, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.user_id = abstractSerializedData.readInt64(z);
        this.id = abstractSerializedData.readInt32(z);
        this.storyItem = TLRPC$StoryItem.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.via_mention = abstractSerializedData.readBool(z);
        this.peer = MessagesController.getInstance(UserConfig.selectedAccount).getPeer(this.user_id);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_messageMediaStory, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt64(this.user_id);
        abstractSerializedData.writeInt32(this.id);
        this.storyItem.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeBool(this.via_mention);
    }
}
