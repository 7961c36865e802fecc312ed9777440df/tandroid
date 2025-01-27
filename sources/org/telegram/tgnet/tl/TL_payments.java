package org.telegram.tgnet.tl;

import java.util.ArrayList;
import org.telegram.tgnet.InputSerializedData;
import org.telegram.tgnet.OutputSerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1;
import org.telegram.tgnet.Vector;
import org.telegram.tgnet.tl.TL_payments;
import org.telegram.tgnet.tl.TL_stars;

/* loaded from: classes3.dex */
public class TL_payments {

    public static class connectStarRefBot extends TLObject {
        public static final int constructor = 2127901834;
        public TLRPC.InputUser bot;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return connectedStarRefBots.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            this.bot.serializeToStream(outputSerializedData);
        }
    }

    public static class connectedBotStarRef extends TLObject {
        public static final int constructor = 429997937;
        public long bot_id;
        public int commission_permille;
        public int date;
        public int duration_months;
        public int flags;
        public long participants;
        public long revenue;
        public boolean revoked;
        public String url;

        public static connectedBotStarRef TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (429997937 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_payments.connectedBotStarRef", Integer.valueOf(i)));
                }
                return null;
            }
            connectedBotStarRef connectedbotstarref = new connectedBotStarRef();
            connectedbotstarref.readParams(inputSerializedData, z);
            return connectedbotstarref;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.revoked = (readInt32 & 2) != 0;
            this.url = inputSerializedData.readString(z);
            this.date = inputSerializedData.readInt32(z);
            this.bot_id = inputSerializedData.readInt64(z);
            this.commission_permille = inputSerializedData.readInt32(z);
            if ((this.flags & 1) != 0) {
                this.duration_months = inputSerializedData.readInt32(z);
            }
            this.participants = inputSerializedData.readInt64(z);
            this.revenue = inputSerializedData.readInt64(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.revoked ? this.flags | 2 : this.flags & (-3);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            outputSerializedData.writeString(this.url);
            outputSerializedData.writeInt32(this.date);
            outputSerializedData.writeInt64(this.bot_id);
            outputSerializedData.writeInt32(this.commission_permille);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeInt32(this.duration_months);
            }
            outputSerializedData.writeInt64(this.participants);
            outputSerializedData.writeInt64(this.revenue);
        }
    }

    public static class connectedStarRefBots extends TLObject {
        public static final int constructor = -1730811363;
        public int count;
        public ArrayList<connectedBotStarRef> connected_bots = new ArrayList<>();
        public ArrayList<TLRPC.User> users = new ArrayList<>();

        public static connectedStarRefBots TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-1730811363 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_payments.connectedStarRefBots", Integer.valueOf(i)));
                }
                return null;
            }
            connectedStarRefBots connectedstarrefbots = new connectedStarRefBots();
            connectedstarrefbots.readParams(inputSerializedData, z);
            return connectedstarrefbots;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.count = inputSerializedData.readInt32(z);
            this.connected_bots = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_payments$connectedStarRefBots$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.Vector.TLDeserializer
                public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                    return TL_payments.connectedBotStarRef.TLdeserialize(inputSerializedData2, i, z2);
                }
            }, z);
            this.users = Vector.deserialize(inputSerializedData, new TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1(), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.count);
            Vector.serialize(outputSerializedData, this.connected_bots);
            Vector.serialize(outputSerializedData, this.users);
        }
    }

    public static class editConnectedStarRefBot extends TLObject {
        public static final int constructor = -453204829;
        public int flags;
        public String link;
        public TLRPC.InputPeer peer;
        public boolean revoked;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return connectedStarRefBots.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.revoked ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeString(this.link);
        }
    }

    public static class getConnectedStarRefBot extends TLObject {
        public static final int constructor = -1210476304;
        public TLRPC.InputUser bot;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return connectedStarRefBots.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            this.bot.serializeToStream(outputSerializedData);
        }
    }

    public static class getConnectedStarRefBots extends TLObject {
        public static final int constructor = 1483318611;
        public int flags;
        public int limit;
        public int offset_date;
        public String offset_link;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return connectedStarRefBots.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            this.peer.serializeToStream(outputSerializedData);
            if ((this.flags & 4) != 0) {
                outputSerializedData.writeInt32(this.offset_date);
                outputSerializedData.writeString(this.offset_link);
            }
            outputSerializedData.writeInt32(this.limit);
        }
    }

    public static class getSuggestedStarRefBots extends TLObject {
        public static final int constructor = 225134839;
        public int flags;
        public int limit;
        public String offset;
        public boolean order_by_date;
        public boolean order_by_revenue;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return suggestedStarRefBots.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.order_by_revenue ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            int i2 = this.order_by_date ? i | 2 : i & (-3);
            this.flags = i2;
            outputSerializedData.writeInt32(i2);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeString(this.offset);
            outputSerializedData.writeInt32(this.limit);
        }
    }

    public static class starRefProgram extends TLObject {
        public static final int constructor = -586389774;
        public long bot_id;
        public int commission_permille;
        public TL_stars.StarsAmount daily_revenue_per_user = new TL_stars.StarsAmount(0);
        public int duration_months;
        public int end_date;
        public int flags;

        public static starRefProgram TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-586389774 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_payments.starRefProgram", Integer.valueOf(i)));
                }
                return null;
            }
            starRefProgram starrefprogram = new starRefProgram();
            starrefprogram.readParams(inputSerializedData, z);
            return starrefprogram;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.bot_id = inputSerializedData.readInt64(z);
            this.commission_permille = inputSerializedData.readInt32(z);
            if ((this.flags & 1) != 0) {
                this.duration_months = inputSerializedData.readInt32(z);
            }
            if ((this.flags & 2) != 0) {
                this.end_date = inputSerializedData.readInt32(z);
            }
            if ((this.flags & 4) != 0) {
                this.daily_revenue_per_user = TL_stars.StarsAmount.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeInt64(this.bot_id);
            outputSerializedData.writeInt32(this.commission_permille);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeInt32(this.duration_months);
            }
            if ((this.flags & 2) != 0) {
                outputSerializedData.writeInt32(this.end_date);
            }
            if ((this.flags & 4) != 0) {
                this.daily_revenue_per_user.serializeToStream(outputSerializedData);
            }
        }
    }

    public static class suggestedStarRefBots extends TLObject {
        public static final int constructor = -1261053863;
        public int count;
        public int flags;
        public String next_offset;
        public ArrayList<starRefProgram> suggested_bots = new ArrayList<>();
        public ArrayList<TLRPC.User> users = new ArrayList<>();

        public static suggestedStarRefBots TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-1261053863 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_payments.suggestedStarRefBots", Integer.valueOf(i)));
                }
                return null;
            }
            suggestedStarRefBots suggestedstarrefbots = new suggestedStarRefBots();
            suggestedstarrefbots.readParams(inputSerializedData, z);
            return suggestedstarrefbots;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.count = inputSerializedData.readInt32(z);
            this.suggested_bots = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_payments$suggestedStarRefBots$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.Vector.TLDeserializer
                public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                    return TL_payments.starRefProgram.TLdeserialize(inputSerializedData2, i, z2);
                }
            }, z);
            this.users = Vector.deserialize(inputSerializedData, new TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1(), z);
            if ((this.flags & 1) != 0) {
                this.next_offset = inputSerializedData.readString(z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.count);
            Vector.serialize(outputSerializedData, this.suggested_bots);
            Vector.serialize(outputSerializedData, this.users);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.next_offset);
            }
        }
    }
}
