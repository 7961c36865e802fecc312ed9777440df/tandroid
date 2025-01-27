package org.telegram.tgnet.tl;

import java.util.ArrayList;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.MessagesStorage$$ExternalSyntheticLambda42;
import org.telegram.tgnet.InputSerializedData;
import org.telegram.tgnet.OutputSerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1;
import org.telegram.tgnet.TLRPC$TL_channels_adminLogResults$$ExternalSyntheticLambda1;
import org.telegram.tgnet.TLRPC$TL_messageReactions$$ExternalSyntheticLambda0;
import org.telegram.tgnet.TLRPC$TL_updatePrivacy$$ExternalSyntheticLambda0;
import org.telegram.tgnet.Vector;
import org.telegram.tgnet.Vector$$ExternalSyntheticLambda3;
import org.telegram.tgnet.tl.TL_stats;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet;

/* loaded from: classes3.dex */
public class TL_stories {

    public static class Boost extends TLObject {
        public static final long NO_USER_ID = -1;
        public static int constructor = 706514033;
        public int date;
        public int expires;
        public int flags;
        public boolean gift;
        public boolean giveaway;
        public int giveaway_msg_id;
        public String id;
        public int multiplier;
        public long stars;
        public boolean unclaimed;
        public String used_gift_slug;
        public long user_id = -1;

        public static Boost TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            Boost tL_boost = i != 706514033 ? i != 1262359766 ? null : new TL_boost() : new TL_boost_layer186();
            if (tL_boost == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in Boost", Integer.valueOf(i)));
            }
            if (tL_boost != null) {
                tL_boost.readParams(inputSerializedData, z);
            }
            return tL_boost;
        }
    }

    public static class MediaArea extends TLObject {
        public MediaAreaCoordinates coordinates;
        public boolean dark;
        public int flags;
        public boolean flipped;
        public TLRPC.Reaction reaction;

        public static MediaArea TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            MediaArea tL_mediaAreaWeather2;
            switch (i) {
                case TL_mediaAreaWeather2.constructor /* -2057362882 */:
                    tL_mediaAreaWeather2 = new TL_mediaAreaWeather2();
                    break;
                case TL_inputMediaAreaVenue.constructor /* -1300094593 */:
                    tL_mediaAreaWeather2 = new TL_inputMediaAreaVenue();
                    break;
                case TL_mediaAreaVenue.constructor /* -1098720356 */:
                    tL_mediaAreaWeather2 = new TL_mediaAreaVenue();
                    break;
                case TL_mediaAreaGeoPoint.constructor /* -891992787 */:
                    tL_mediaAreaWeather2 = new TL_mediaAreaGeoPoint();
                    break;
                case TL_mediaAreaGeoPoint_layer181.constructor /* -544523486 */:
                    tL_mediaAreaWeather2 = new TL_mediaAreaGeoPoint_layer181();
                    break;
                case TL_mediaAreaSuggestedReaction.constructor /* 340088945 */:
                    tL_mediaAreaWeather2 = new TL_mediaAreaSuggestedReaction();
                    break;
                case TL_inputMediaAreaChannelPost.constructor /* 577893055 */:
                    tL_mediaAreaWeather2 = new TL_inputMediaAreaChannelPost();
                    break;
                case TL_mediaAreaUrl.constructor /* 926421125 */:
                    tL_mediaAreaWeather2 = new TL_mediaAreaUrl();
                    break;
                case TL_mediaAreaWeatherOld.constructor /* 1132918857 */:
                    tL_mediaAreaWeather2 = new TL_mediaAreaWeatherOld();
                    break;
                case TL_mediaAreaWeather.constructor /* 1235637404 */:
                    tL_mediaAreaWeather2 = new TL_mediaAreaWeather();
                    break;
                case TL_mediaAreaStarGift.constructor /* 1468491885 */:
                    tL_mediaAreaWeather2 = new TL_mediaAreaStarGift();
                    break;
                case TL_mediaAreaChannelPost.constructor /* 1996756655 */:
                    tL_mediaAreaWeather2 = new TL_mediaAreaChannelPost();
                    break;
                default:
                    tL_mediaAreaWeather2 = null;
                    break;
            }
            if (tL_mediaAreaWeather2 == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in MediaArea", Integer.valueOf(i)));
            }
            if (tL_mediaAreaWeather2 != null) {
                tL_mediaAreaWeather2.readParams(inputSerializedData, z);
            }
            return tL_mediaAreaWeather2;
        }
    }

    public static class MediaAreaCoordinates extends TLObject {
        public int flags;
        public double h;
        public double radius;
        public double rotation;
        public double w;
        public double x;
        public double y;

        public static MediaAreaCoordinates TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            MediaAreaCoordinates tL_mediaAreaCoordinates_layer181 = i != -808853502 ? i != 64088654 ? null : new TL_mediaAreaCoordinates_layer181() : new TL_mediaAreaCoordinates();
            if (tL_mediaAreaCoordinates_layer181 == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in MediaAreaCoordinates", Integer.valueOf(i)));
            }
            if (tL_mediaAreaCoordinates_layer181 != null) {
                tL_mediaAreaCoordinates_layer181.readParams(inputSerializedData, z);
            }
            return tL_mediaAreaCoordinates_layer181;
        }
    }

    public static abstract class PeerStories extends TLObject {
        public boolean checkedExpired;
        public int flags;
        public int max_read_id;
        public TLRPC.Peer peer;
        public ArrayList<StoryItem> stories = new ArrayList<>();

        public static PeerStories TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            PeerStories tL_peerStories = i != -2045664768 ? i != -1707742823 ? null : new TL_peerStories() : new TL_peerStories_layer162();
            if (tL_peerStories == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in PeerStories", Integer.valueOf(i)));
            }
            if (tL_peerStories != null) {
                tL_peerStories.readParams(inputSerializedData, z);
            }
            return tL_peerStories;
        }
    }

    public static class PrepaidGiveaway extends TLObject {
        public int boosts;
        public int date;
        public long id;
        public int quantity;

        public static PrepaidGiveaway TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            PrepaidGiveaway tL_prepaidGiveaway = i != -1700956192 ? i != -1303143084 ? null : new TL_prepaidGiveaway() : new TL_prepaidStarsGiveaway();
            if (tL_prepaidGiveaway == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in PrepaidGiveaway", Integer.valueOf(i)));
            }
            if (tL_prepaidGiveaway != null) {
                tL_prepaidGiveaway.readParams(inputSerializedData, z);
            }
            return tL_prepaidGiveaway;
        }
    }

    public static class StoryFwdHeader extends TLObject {
        public int flags;
        public TLRPC.Peer from;
        public String from_name;
        public boolean modified;
        public int story_id;

        public static StoryFwdHeader TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            TL_storyFwdHeader tL_storyFwdHeader = i != -1205411504 ? null : new TL_storyFwdHeader();
            if (tL_storyFwdHeader == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in StoryFwdHeader", Integer.valueOf(i)));
            }
            if (tL_storyFwdHeader != null) {
                tL_storyFwdHeader.readParams(inputSerializedData, z);
            }
            return tL_storyFwdHeader;
        }
    }

    public static abstract class StoryItem extends TLObject {
        public String attachPath;
        public String caption;
        public boolean close_friends;
        public boolean contacts;
        public int date;
        public String detectedLng;
        public long dialogId;
        public boolean edited;
        public int expire_date;
        public int fileReference;
        public String firstFramePath;
        public int flags;
        public TLRPC.Peer from_id;
        public StoryFwdHeader fwd_from;
        public int id;
        public boolean isPublic;
        public boolean justUploaded;
        public long lastUpdateTime;
        public TLRPC.MessageMedia media;
        public int messageId;
        public int messageType;
        public boolean min;
        public boolean noforwards;
        public boolean out;
        public StoryPrivacyBottomSheet.StoryPrivacy parsedPrivacy;
        public boolean pinned;
        public boolean selected_contacts;
        public TLRPC.Reaction sent_reaction;
        public boolean translated;
        public String translatedLng;
        public TLRPC.TL_textWithEntities translatedText;
        public StoryViews views;
        public ArrayList<TLRPC.MessageEntity> entities = new ArrayList<>();
        public ArrayList<MediaArea> media_areas = new ArrayList<>();
        public ArrayList<TLRPC.PrivacyRule> privacy = new ArrayList<>();

        public static StoryItem TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            StoryItem tL_storyItem_layer174;
            switch (i) {
                case TL_storyItem_layer174.constructor /* -1352440415 */:
                    tL_storyItem_layer174 = new TL_storyItem_layer174();
                    break;
                case TL_storyItemSkipped.constructor /* -5388013 */:
                    tL_storyItem_layer174 = new TL_storyItemSkipped();
                    break;
                case TL_storyItem_layer166.constructor /* 1153718222 */:
                    tL_storyItem_layer174 = new TL_storyItem_layer166();
                    break;
                case TL_storyItemDeleted.constructor /* 1374088783 */:
                    tL_storyItem_layer174 = new TL_storyItemDeleted();
                    break;
                case TL_storyItem_layer160.constructor /* 1445635639 */:
                    tL_storyItem_layer174 = new TL_storyItem_layer160();
                    break;
                case TL_storyItem.constructor /* 2041735716 */:
                    tL_storyItem_layer174 = new TL_storyItem();
                    break;
                default:
                    tL_storyItem_layer174 = null;
                    break;
            }
            if (tL_storyItem_layer174 == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in StoryItem", Integer.valueOf(i)));
            }
            if (tL_storyItem_layer174 != null) {
                tL_storyItem_layer174.readParams(inputSerializedData, z);
            }
            return tL_storyItem_layer174;
        }
    }

    public static class StoryReaction extends TLObject {
        public TLRPC.Message message;
        public TLRPC.Peer peer_id;
        public StoryItem story;

        public static StoryReaction TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            StoryReaction tL_storyReaction = i != -1146411453 ? i != -808644845 ? i != 1620104917 ? null : new TL_storyReaction() : new TL_storyReactionPublicRepost() : new TL_storyReactionPublicForward();
            if (tL_storyReaction == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in StoryReaction", Integer.valueOf(i)));
            }
            if (tL_storyReaction != null) {
                tL_storyReaction.readParams(inputSerializedData, z);
            }
            return tL_storyReaction;
        }
    }

    public static class StoryView extends TLObject {
        public boolean blocked;
        public boolean blocked_my_stories_from;
        public int date;
        public int flags;
        public TLRPC.Message message;
        public TLRPC.Peer peer_id;
        public TLRPC.Reaction reaction;
        public StoryItem story;
        public long user_id;

        public static StoryView TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            StoryView tL_storyViewPublicRepost = i != -1870436597 ? i != -1329730875 ? i != -1116418231 ? null : new TL_storyViewPublicRepost() : new TL_storyView() : new TL_storyViewPublicForward();
            if (tL_storyViewPublicRepost == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in StoryView", Integer.valueOf(i)));
            }
            if (tL_storyViewPublicRepost != null) {
                tL_storyViewPublicRepost.readParams(inputSerializedData, z);
            }
            return tL_storyViewPublicRepost;
        }
    }

    public static abstract class StoryViews extends TLObject {
        public int flags;
        public int forwards_count;
        public boolean has_viewers;
        public int reactions_count;
        public int views_count;
        public ArrayList<Long> recent_viewers = new ArrayList<>();
        public ArrayList<TLRPC.ReactionCount> reactions = new ArrayList<>();

        public static StoryViews TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            StoryViews tL_storyViews_layer160 = i != -1923523370 ? i != -968094825 ? i != -748199729 ? null : new TL_storyViews_layer160() : new TL_storyViews_layer161() : new TL_storyViews();
            if (tL_storyViews_layer160 == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in StoryViews", Integer.valueOf(i)));
            }
            if (tL_storyViews_layer160 != null) {
                tL_storyViews_layer160.readParams(inputSerializedData, z);
            }
            return tL_storyViews_layer160;
        }
    }

    public static class StoryViewsList extends TLObject {
        public int count;
        public int flags;
        public int forwards_count;
        public int reactions_count;
        public int views_count;
        public ArrayList<StoryView> views = new ArrayList<>();
        public ArrayList<TLRPC.Chat> chats = new ArrayList<>();
        public ArrayList<TLRPC.User> users = new ArrayList<>();
        public String next_offset = "";

        public static StoryViewsList TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            StoryViewsList tL_storyViewsList = i != 1189722604 ? i != 1507299269 ? null : new TL_storyViewsList() : new TL_storyViewsList_layer167();
            if (tL_storyViewsList == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in StoryViewsList", Integer.valueOf(i)));
            }
            if (tL_storyViewsList != null) {
                tL_storyViewsList.readParams(inputSerializedData, z);
            }
            return tL_storyViewsList;
        }
    }

    public static class TL_boost extends Boost {
        public static final int constructor = 1262359766;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.gift = (readInt32 & 2) != 0;
            this.giveaway = (readInt32 & 4) != 0;
            this.unclaimed = (readInt32 & 8) != 0;
            this.id = inputSerializedData.readString(z);
            if ((this.flags & 1) != 0) {
                this.user_id = inputSerializedData.readInt64(z);
            }
            if ((this.flags & 4) != 0) {
                this.giveaway_msg_id = inputSerializedData.readInt32(z);
            }
            this.date = inputSerializedData.readInt32(z);
            this.expires = inputSerializedData.readInt32(z);
            if ((this.flags & 16) != 0) {
                this.used_gift_slug = inputSerializedData.readString(z);
            }
            if ((this.flags & 32) != 0) {
                this.multiplier = inputSerializedData.readInt32(z);
            }
            if ((this.flags & 64) != 0) {
                this.stars = inputSerializedData.readInt64(z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.gift ? this.flags | 2 : this.flags & (-3);
            this.flags = i;
            int i2 = this.giveaway ? i | 4 : i & (-5);
            this.flags = i2;
            int i3 = this.unclaimed ? i2 | 8 : i2 & (-9);
            this.flags = i3;
            outputSerializedData.writeInt32(i3);
            outputSerializedData.writeString(this.id);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeInt64(this.user_id);
            }
            if ((this.flags & 4) != 0) {
                outputSerializedData.writeInt32(this.giveaway_msg_id);
            }
            outputSerializedData.writeInt32(this.date);
            outputSerializedData.writeInt32(this.expires);
            if ((this.flags & 16) != 0) {
                outputSerializedData.writeString(this.used_gift_slug);
            }
            if ((this.flags & 32) != 0) {
                outputSerializedData.writeInt32(this.multiplier);
            }
            if ((this.flags & 64) != 0) {
                outputSerializedData.writeInt64(this.stars);
            }
        }
    }

    public static class TL_boost_layer186 extends TL_boost {
        public static final int constructor = 706514033;

        @Override // org.telegram.tgnet.tl.TL_stories.TL_boost, org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.gift = (readInt32 & 2) != 0;
            this.giveaway = (readInt32 & 4) != 0;
            this.unclaimed = (readInt32 & 8) != 0;
            this.id = inputSerializedData.readString(z);
            if ((this.flags & 1) != 0) {
                this.user_id = inputSerializedData.readInt64(z);
            }
            if ((this.flags & 4) != 0) {
                this.giveaway_msg_id = inputSerializedData.readInt32(z);
            }
            this.date = inputSerializedData.readInt32(z);
            this.expires = inputSerializedData.readInt32(z);
            if ((this.flags & 16) != 0) {
                this.used_gift_slug = inputSerializedData.readString(z);
            }
            if ((this.flags & 32) != 0) {
                this.multiplier = inputSerializedData.readInt32(z);
            }
        }

        @Override // org.telegram.tgnet.tl.TL_stories.TL_boost, org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.gift ? this.flags | 2 : this.flags & (-3);
            this.flags = i;
            int i2 = this.giveaway ? i | 4 : i & (-5);
            this.flags = i2;
            int i3 = this.unclaimed ? i2 | 8 : i2 & (-9);
            this.flags = i3;
            outputSerializedData.writeInt32(i3);
            outputSerializedData.writeString(this.id);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeInt64(this.user_id);
            }
            if ((this.flags & 4) != 0) {
                outputSerializedData.writeInt32(this.giveaway_msg_id);
            }
            outputSerializedData.writeInt32(this.date);
            outputSerializedData.writeInt32(this.expires);
            if ((this.flags & 16) != 0) {
                outputSerializedData.writeString(this.used_gift_slug);
            }
            if ((this.flags & 32) != 0) {
                outputSerializedData.writeInt32(this.multiplier);
            }
        }
    }

    public static class TL_exportedStoryLink extends TLObject {
        public static final int constructor = 1070138683;
        public String link;

        public static TL_exportedStoryLink TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (1070138683 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_exportedStoryLink", Integer.valueOf(i)));
                }
                return null;
            }
            TL_exportedStoryLink tL_exportedStoryLink = new TL_exportedStoryLink();
            tL_exportedStoryLink.readParams(inputSerializedData, z);
            return tL_exportedStoryLink;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.link = inputSerializedData.readString(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeString(this.link);
        }
    }

    public static class TL_foundStories extends TLObject {
        public static final int constructor = -488736969;
        public int count;
        public int flags;
        public String next_offset;
        public ArrayList<TL_foundStory> stories = new ArrayList<>();
        public ArrayList<TLRPC.Chat> chats = new ArrayList<>();
        public ArrayList<TLRPC.User> users = new ArrayList<>();

        public static TL_foundStories TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-488736969 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_foundStories", Integer.valueOf(i)));
                }
                return null;
            }
            TL_foundStories tL_foundStories = new TL_foundStories();
            tL_foundStories.readParams(inputSerializedData, z);
            return tL_foundStories;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.count = inputSerializedData.readInt32(z);
            this.stories = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_stories$TL_foundStories$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.Vector.TLDeserializer
                public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                    return TL_stories.TL_foundStory.TLdeserialize(inputSerializedData2, i, z2);
                }
            }, z);
            if ((this.flags & 1) != 0) {
                this.next_offset = inputSerializedData.readString(z);
            }
            this.chats = Vector.deserialize(inputSerializedData, new TLRPC$TL_channels_adminLogResults$$ExternalSyntheticLambda1(), z);
            this.users = Vector.deserialize(inputSerializedData, new TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1(), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeInt32(this.count);
            Vector.serialize(outputSerializedData, this.stories);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.next_offset);
            }
            Vector.serialize(outputSerializedData, this.chats);
            Vector.serialize(outputSerializedData, this.users);
        }
    }

    public static class TL_foundStory extends TLObject {
        public static final int constructor = -394605632;
        public TLRPC.Peer peer;
        public StoryItem storyItem;

        public static TL_foundStory TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-394605632 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_foundStory", Integer.valueOf(i)));
                }
                return null;
            }
            TL_foundStory tL_foundStory = new TL_foundStory();
            tL_foundStory.readParams(inputSerializedData, z);
            return tL_foundStory;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.peer = TLRPC.Peer.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.storyItem = StoryItem.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            this.storyItem.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_geoPointAddress extends TLObject {
        public static final int constructor = -565420653;
        public String city;
        public String country_iso2;
        public int flags;
        public String state;
        public String street;

        public static TL_geoPointAddress TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-565420653 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_geoPointAddress", Integer.valueOf(i)));
                }
                return null;
            }
            TL_geoPointAddress tL_geoPointAddress = new TL_geoPointAddress();
            tL_geoPointAddress.readParams(inputSerializedData, z);
            return tL_geoPointAddress;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.country_iso2 = inputSerializedData.readString(z);
            if ((this.flags & 1) != 0) {
                this.state = inputSerializedData.readString(z);
            }
            if ((this.flags & 2) != 0) {
                this.city = inputSerializedData.readString(z);
            }
            if ((this.flags & 4) != 0) {
                this.street = inputSerializedData.readString(z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeString(this.country_iso2);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.state);
            }
            if ((this.flags & 2) != 0) {
                outputSerializedData.writeString(this.city);
            }
            if ((this.flags & 4) != 0) {
                outputSerializedData.writeString(this.street);
            }
        }

        public String toString() {
            String str;
            String str2;
            StringBuilder sb = new StringBuilder();
            sb.append("geo{country=");
            sb.append(this.country_iso2);
            sb.append(", ");
            String str3 = "";
            if (this.state != null) {
                str = "state=" + this.state + ", ";
            } else {
                str = "";
            }
            sb.append(str);
            if (this.city != null) {
                str2 = "city=" + this.city + ", ";
            } else {
                str2 = "";
            }
            sb.append(str2);
            if (this.street != null) {
                str3 = "street=" + this.street;
            }
            sb.append(str3);
            sb.append("}");
            return sb.toString();
        }
    }

    public static class TL_getStoryReactionsList extends TLObject {
        public static final int constructor = -1179482081;
        public int flags;
        public boolean forwards_first;
        public int id;
        public int limit;
        public String offset;
        public TLRPC.InputPeer peer;
        public TLRPC.Reaction reaction;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_storyReactionsList.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.forwards_first ? this.flags | 4 : this.flags & (-5);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.id);
            if ((this.flags & 1) != 0) {
                this.reaction.serializeToStream(outputSerializedData);
            }
            if ((this.flags & 2) != 0) {
                outputSerializedData.writeString(this.offset);
            }
            outputSerializedData.writeInt32(this.limit);
        }
    }

    public static class TL_inputMediaAreaChannelPost extends MediaArea {
        public static final int constructor = 577893055;
        public TLRPC.InputChannel channel;
        public int msg_id;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.coordinates = MediaAreaCoordinates.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.channel = TLRPC.InputChannel.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.msg_id = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.coordinates.serializeToStream(outputSerializedData);
            this.channel.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.msg_id);
        }
    }

    public static class TL_inputMediaAreaVenue extends MediaArea {
        public static final int constructor = -1300094593;
        public long query_id;
        public String result_id;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.coordinates = MediaAreaCoordinates.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.query_id = inputSerializedData.readInt64(z);
            this.result_id = inputSerializedData.readString(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.coordinates.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt64(this.query_id);
            outputSerializedData.writeString(this.result_id);
        }
    }

    public static class TL_mediaAreaChannelPost extends MediaArea {
        public static final int constructor = 1996756655;
        public long channel_id;
        public int msg_id;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.coordinates = MediaAreaCoordinates.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.channel_id = inputSerializedData.readInt64(z);
            this.msg_id = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.coordinates.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt64(this.channel_id);
            outputSerializedData.writeInt32(this.msg_id);
        }
    }

    public static class TL_mediaAreaCoordinates extends MediaAreaCoordinates {
        public static final int constructor = -808853502;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.x = inputSerializedData.readDouble(z);
            this.y = inputSerializedData.readDouble(z);
            this.w = inputSerializedData.readDouble(z);
            this.h = inputSerializedData.readDouble(z);
            this.rotation = inputSerializedData.readDouble(z);
            if ((this.flags & 1) != 0) {
                this.radius = inputSerializedData.readDouble(z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeDouble(this.x);
            outputSerializedData.writeDouble(this.y);
            outputSerializedData.writeDouble(this.w);
            outputSerializedData.writeDouble(this.h);
            outputSerializedData.writeDouble(this.rotation);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeDouble(this.radius);
            }
        }
    }

    public static class TL_mediaAreaCoordinates_layer181 extends MediaAreaCoordinates {
        public static final int constructor = 64088654;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.x = inputSerializedData.readDouble(z);
            this.y = inputSerializedData.readDouble(z);
            this.w = inputSerializedData.readDouble(z);
            this.h = inputSerializedData.readDouble(z);
            this.rotation = inputSerializedData.readDouble(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeDouble(this.x);
            outputSerializedData.writeDouble(this.y);
            outputSerializedData.writeDouble(this.w);
            outputSerializedData.writeDouble(this.h);
            outputSerializedData.writeDouble(this.rotation);
        }
    }

    public static class TL_mediaAreaGeoPoint extends MediaArea {
        public static final int constructor = -891992787;
        public TL_geoPointAddress address;
        public TLRPC.GeoPoint geo;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.coordinates = MediaAreaCoordinates.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.geo = TLRPC.GeoPoint.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            if ((this.flags & 1) != 0) {
                this.address = TL_geoPointAddress.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            this.coordinates.serializeToStream(outputSerializedData);
            this.geo.serializeToStream(outputSerializedData);
            if ((this.flags & 1) != 0) {
                this.address.serializeToStream(outputSerializedData);
            }
        }
    }

    public static class TL_mediaAreaGeoPoint_layer181 extends TL_mediaAreaGeoPoint {
        public static final int constructor = -544523486;

        @Override // org.telegram.tgnet.tl.TL_stories.TL_mediaAreaGeoPoint, org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.coordinates = MediaAreaCoordinates.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.geo = TLRPC.GeoPoint.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.tl.TL_stories.TL_mediaAreaGeoPoint, org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.coordinates.serializeToStream(outputSerializedData);
            this.geo.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_mediaAreaStarGift extends MediaArea {
        public static final int constructor = 1468491885;
        public String slug;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.coordinates = MediaAreaCoordinates.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.slug = inputSerializedData.readString(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.coordinates.serializeToStream(outputSerializedData);
            outputSerializedData.writeString(this.slug);
        }
    }

    public static class TL_mediaAreaSuggestedReaction extends MediaArea {
        public static final int constructor = 340088945;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.dark = (readInt32 & 1) != 0;
            this.flipped = (readInt32 & 2) != 0;
            this.coordinates = MediaAreaCoordinates.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.reaction = TLRPC.Reaction.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.dark ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            int i2 = this.flipped ? i | 2 : i & (-3);
            this.flags = i2;
            outputSerializedData.writeInt32(i2);
            this.coordinates.serializeToStream(outputSerializedData);
            this.reaction.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_mediaAreaUrl extends MediaArea {
        public static final int constructor = 926421125;
        public String url;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.coordinates = MediaAreaCoordinates.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.url = inputSerializedData.readString(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.coordinates.serializeToStream(outputSerializedData);
            outputSerializedData.writeString(this.url);
        }
    }

    public static class TL_mediaAreaVenue extends MediaArea {
        public static final int constructor = -1098720356;
        public String address;
        public TLRPC.GeoPoint geo;
        public String provider;
        public String title;
        public String venue_id;
        public String venue_type;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.coordinates = MediaAreaCoordinates.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.geo = TLRPC.GeoPoint.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.title = inputSerializedData.readString(z);
            this.address = inputSerializedData.readString(z);
            this.provider = inputSerializedData.readString(z);
            this.venue_id = inputSerializedData.readString(z);
            this.venue_type = inputSerializedData.readString(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.coordinates.serializeToStream(outputSerializedData);
            this.geo.serializeToStream(outputSerializedData);
            outputSerializedData.writeString(this.title);
            outputSerializedData.writeString(this.address);
            outputSerializedData.writeString(this.provider);
            outputSerializedData.writeString(this.venue_id);
            outputSerializedData.writeString(this.venue_type);
        }
    }

    public static class TL_mediaAreaWeather extends MediaArea {
        public static final int constructor = 1235637404;
        public int color;
        public String emoji;
        public double temperature_c;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.coordinates = MediaAreaCoordinates.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.emoji = inputSerializedData.readString(z);
            this.temperature_c = inputSerializedData.readDouble(z);
            this.color = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.coordinates.serializeToStream(outputSerializedData);
            outputSerializedData.writeString(this.emoji);
            outputSerializedData.writeDouble(this.temperature_c);
            outputSerializedData.writeInt32(this.color);
        }
    }

    public static class TL_mediaAreaWeather2 extends MediaArea {
        public static final int constructor = -2057362882;
        public String emoji;
        public int temperature_c;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.dark = (readInt32 & 1) != 0;
            this.coordinates = MediaAreaCoordinates.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.emoji = inputSerializedData.readString(z);
            this.temperature_c = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.dark ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            this.coordinates.serializeToStream(outputSerializedData);
            outputSerializedData.writeString(this.emoji);
            outputSerializedData.writeInt32(this.temperature_c);
        }
    }

    public static class TL_mediaAreaWeatherOld extends MediaArea {
        public static final int constructor = 1132918857;
        public String emoji;
        public double temperature_c;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.dark = (readInt32 & 1) != 0;
            this.coordinates = MediaAreaCoordinates.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.emoji = inputSerializedData.readString(z);
            this.temperature_c = inputSerializedData.readDouble(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.dark ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            this.coordinates.serializeToStream(outputSerializedData);
            outputSerializedData.writeString(this.emoji);
            outputSerializedData.writeDouble(this.temperature_c);
        }
    }

    public static class TL_myBoost extends TLObject {
        public static int constructor = -1001897636;
        public int cooldown_until_date;
        public int date;
        public int expires;
        public int flags;
        public TLRPC.Peer peer;
        public int slot;

        public static TL_myBoost TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (constructor != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_myBoost", Integer.valueOf(i)));
                }
                return null;
            }
            TL_myBoost tL_myBoost = new TL_myBoost();
            tL_myBoost.readParams(inputSerializedData, z);
            return tL_myBoost;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.slot = inputSerializedData.readInt32(z);
            if ((this.flags & 1) != 0) {
                this.peer = TLRPC.Peer.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
            this.date = inputSerializedData.readInt32(z);
            this.expires = inputSerializedData.readInt32(z);
            if ((this.flags & 2) != 0) {
                this.cooldown_until_date = inputSerializedData.readInt32(z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeInt32(this.slot);
            if ((this.flags & 1) != 0) {
                this.peer.serializeToStream(outputSerializedData);
            }
            outputSerializedData.writeInt32(this.date);
            outputSerializedData.writeInt32(this.expires);
            if ((this.flags & 2) != 0) {
                outputSerializedData.writeInt32(this.cooldown_until_date);
            }
        }
    }

    public static class TL_peerStories extends PeerStories {
        public static final int constructor = -1707742823;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.peer = TLRPC.Peer.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            if ((this.flags & 1) != 0) {
                this.max_read_id = inputSerializedData.readInt32(z);
            }
            this.stories = Vector.deserialize(inputSerializedData, new TL_stories$TL_peerStories$$ExternalSyntheticLambda0(), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            this.peer.serializeToStream(outputSerializedData);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeInt32(this.max_read_id);
            }
            Vector.serialize(outputSerializedData, this.stories);
        }
    }

    public static class TL_peerStories_layer162 extends TL_peerStories {
        public static final int constructor = -2045664768;

        @Override // org.telegram.tgnet.tl.TL_stories.TL_peerStories, org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            long readInt64 = inputSerializedData.readInt64(z);
            TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
            this.peer = tL_peerUser;
            tL_peerUser.user_id = readInt64;
            if ((this.flags & 1) != 0) {
                this.max_read_id = inputSerializedData.readInt32(z);
            }
            this.stories = Vector.deserialize(inputSerializedData, new TL_stories$TL_peerStories$$ExternalSyntheticLambda0(), z);
        }

        @Override // org.telegram.tgnet.tl.TL_stories.TL_peerStories, org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeInt64(this.peer.user_id);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeInt32(this.max_read_id);
            }
            Vector.serialize(outputSerializedData, this.stories);
        }
    }

    public static class TL_premium_applyBoost extends TLObject {
        public static int constructor = 1803396934;
        public int flags;
        public TLRPC.InputPeer peer;
        public ArrayList<Integer> slots = new ArrayList<>();

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_premium_myBoosts.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            if ((this.flags & 1) != 0) {
                Vector.serializeInt(outputSerializedData, this.slots);
            }
            this.peer.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_premium_boostsList extends TLObject {
        public static int constructor = -2030542532;
        public int count;
        public int flags;
        public String next_offset;
        public ArrayList<Boost> boosts = new ArrayList<>();
        public ArrayList<TLRPC.User> users = new ArrayList<>();

        public static TL_premium_boostsList TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (constructor != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_premium_boostsList", Integer.valueOf(i)));
                }
                return null;
            }
            TL_premium_boostsList tL_premium_boostsList = new TL_premium_boostsList();
            tL_premium_boostsList.readParams(inputSerializedData, z);
            return tL_premium_boostsList;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.count = inputSerializedData.readInt32(z);
            this.boosts = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_stories$TL_premium_boostsList$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.Vector.TLDeserializer
                public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                    return TL_stories.Boost.TLdeserialize(inputSerializedData2, i, z2);
                }
            }, z);
            if ((this.flags & 1) != 0) {
                this.next_offset = inputSerializedData.readString(z);
            }
            this.users = Vector.deserialize(inputSerializedData, new TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1(), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeInt32(this.count);
            Vector.serialize(outputSerializedData, this.boosts);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.next_offset);
            }
            Vector.serialize(outputSerializedData, this.users);
        }
    }

    public static class TL_premium_boostsStatus extends TLObject {
        public static int constructor = 1230586490;
        public String boost_url;
        public int boosts;
        public int current_level_boosts;
        public int flags;
        public int gift_boosts;
        public int level;
        public boolean my_boost;
        public int next_level_boosts;
        public TL_stats.TL_statsPercentValue premium_audience;
        public ArrayList<PrepaidGiveaway> prepaid_giveaways = new ArrayList<>();
        public ArrayList<Integer> my_boost_slots = new ArrayList<>();

        public static TL_premium_boostsStatus TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (constructor != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_premium_boostsStatus", Integer.valueOf(i)));
                }
                return null;
            }
            TL_premium_boostsStatus tL_premium_boostsStatus = new TL_premium_boostsStatus();
            tL_premium_boostsStatus.readParams(inputSerializedData, z);
            return tL_premium_boostsStatus;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.my_boost = (readInt32 & 4) != 0;
            this.level = inputSerializedData.readInt32(z);
            this.current_level_boosts = inputSerializedData.readInt32(z);
            this.boosts = inputSerializedData.readInt32(z);
            if ((this.flags & 16) != 0) {
                this.gift_boosts = inputSerializedData.readInt32(z);
            }
            if ((this.flags & 1) != 0) {
                this.next_level_boosts = inputSerializedData.readInt32(z);
            }
            if ((this.flags & 2) != 0) {
                this.premium_audience = TL_stats.TL_statsPercentValue.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
            this.boost_url = inputSerializedData.readString(z);
            if ((this.flags & 8) != 0) {
                this.prepaid_giveaways = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_stories$TL_premium_boostsStatus$$ExternalSyntheticLambda0
                    @Override // org.telegram.tgnet.Vector.TLDeserializer
                    public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                        return TL_stories.PrepaidGiveaway.TLdeserialize(inputSerializedData2, i, z2);
                    }
                }, z);
            }
            if ((this.flags & 4) != 0) {
                this.my_boost_slots = Vector.deserializeInt(inputSerializedData, z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.my_boost ? this.flags | 4 : this.flags & (-5);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            outputSerializedData.writeInt32(this.level);
            outputSerializedData.writeInt32(this.current_level_boosts);
            outputSerializedData.writeInt32(this.boosts);
            if ((this.flags & 16) != 0) {
                outputSerializedData.writeInt32(this.gift_boosts);
            }
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeInt32(this.next_level_boosts);
            }
            if ((this.flags & 2) != 0) {
                this.premium_audience.serializeToStream(outputSerializedData);
            }
            outputSerializedData.writeString(this.boost_url);
            if ((this.flags & 8) != 0) {
                Vector.serialize(outputSerializedData, this.prepaid_giveaways);
            }
            if ((this.flags & 4) != 0) {
                Vector.serializeInt(outputSerializedData, this.my_boost_slots);
            }
        }
    }

    public static class TL_premium_getBoostsList extends TLObject {
        public static int constructor = 1626764896;
        public int flags;
        public boolean gifts;
        public int limit;
        public String offset;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_premium_boostsList.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.gifts ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeString(this.offset);
            outputSerializedData.writeInt32(this.limit);
        }
    }

    public static class TL_premium_getBoostsStatus extends TLObject {
        public static int constructor = 70197089;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_premium_boostsStatus.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_premium_getMyBoosts extends TLObject {
        public static int constructor = 199719754;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_premium_myBoosts.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
        }
    }

    public static class TL_premium_myBoosts extends TLObject {
        public static int constructor = -1696454430;
        public ArrayList<TL_myBoost> my_boosts = new ArrayList<>();
        public ArrayList<TLRPC.Chat> chats = new ArrayList<>();
        public ArrayList<TLRPC.User> users = new ArrayList<>();

        public static TL_premium_myBoosts TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (constructor != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_premium_myBoosts", Integer.valueOf(i)));
                }
                return null;
            }
            TL_premium_myBoosts tL_premium_myBoosts = new TL_premium_myBoosts();
            tL_premium_myBoosts.readParams(inputSerializedData, z);
            return tL_premium_myBoosts;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.my_boosts = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_stories$TL_premium_myBoosts$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.Vector.TLDeserializer
                public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                    return TL_stories.TL_myBoost.TLdeserialize(inputSerializedData2, i, z2);
                }
            }, z);
            this.chats = Vector.deserialize(inputSerializedData, new TLRPC$TL_channels_adminLogResults$$ExternalSyntheticLambda1(), z);
            this.users = Vector.deserialize(inputSerializedData, new TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1(), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            Vector.serialize(outputSerializedData, this.my_boosts);
            Vector.serialize(outputSerializedData, this.chats);
            Vector.serialize(outputSerializedData, this.users);
        }
    }

    public static class TL_prepaidGiveaway extends PrepaidGiveaway {
        public static final int constructor = -1303143084;
        public int months;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.id = inputSerializedData.readInt64(z);
            this.months = inputSerializedData.readInt32(z);
            this.quantity = inputSerializedData.readInt32(z);
            this.date = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt64(this.id);
            outputSerializedData.writeInt32(this.months);
            outputSerializedData.writeInt32(this.quantity);
            outputSerializedData.writeInt32(this.date);
        }
    }

    public static class TL_prepaidStarsGiveaway extends PrepaidGiveaway {
        public static final int constructor = -1700956192;
        public long stars;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.id = inputSerializedData.readInt64(z);
            this.stars = inputSerializedData.readInt64(z);
            this.quantity = inputSerializedData.readInt32(z);
            this.boosts = inputSerializedData.readInt32(z);
            this.date = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt64(this.id);
            outputSerializedData.writeInt64(this.stars);
            outputSerializedData.writeInt32(this.quantity);
            outputSerializedData.writeInt32(this.boosts);
            outputSerializedData.writeInt32(this.date);
        }
    }

    public static class TL_publicForwardStory extends TL_stats.PublicForward {
        public static final int constructor = -302797360;
        public TLRPC.Peer peer;
        public StoryItem story;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.peer = TLRPC.Peer.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.story = StoryItem.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            this.story.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_stats_getStoryStats extends TLObject {
        public static final int constructor = 927985472;
        public boolean dark;
        public int flags;
        public int id;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_stats_storyStats.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.dark ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.id);
        }
    }

    public static class TL_stats_storyStats extends TLObject {
        public static final int constructor = 1355613820;
        public TL_stats.StatsGraph reactions_by_emotion_graph;
        public TL_stats.StatsGraph views_graph;

        public static TL_stats_storyStats TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (1355613820 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_stats_storyStats", Integer.valueOf(i)));
                }
                return null;
            }
            TL_stats_storyStats tL_stats_storyStats = new TL_stats_storyStats();
            tL_stats_storyStats.readParams(inputSerializedData, z);
            return tL_stats_storyStats;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.views_graph = TL_stats.StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.reactions_by_emotion_graph = TL_stats.StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.views_graph.serializeToStream(outputSerializedData);
            this.reactions_by_emotion_graph.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_storiesStealthMode extends TLObject {
        public static final int constructor = 1898850301;
        public int active_until_date;
        public int cooldown_until_date;
        public int flags;

        public static TL_storiesStealthMode TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (1898850301 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_storiesStealthMode", Integer.valueOf(i)));
                }
                return null;
            }
            TL_storiesStealthMode tL_storiesStealthMode = new TL_storiesStealthMode();
            tL_storiesStealthMode.readParams(inputSerializedData, z);
            return tL_storiesStealthMode;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            if ((readInt32 & 1) != 0) {
                this.active_until_date = inputSerializedData.readInt32(z);
            }
            if ((this.flags & 2) != 0) {
                this.cooldown_until_date = inputSerializedData.readInt32(z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeInt32(this.active_until_date);
            }
            if ((this.flags & 2) != 0) {
                outputSerializedData.writeInt32(this.cooldown_until_date);
            }
        }
    }

    public static class TL_stories_activateStealthMode extends TLObject {
        public static final int constructor = 1471926630;
        public int flags;
        public boolean future;
        public boolean past;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TLRPC.Updates.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.past ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            int i2 = this.future ? i | 2 : i & (-3);
            this.flags = i2;
            outputSerializedData.writeInt32(i2);
        }
    }

    public static class TL_stories_allStories extends stories_AllStories {
        public static final int constructor = 1862033025;
        public int count;
        public int flags;
        public boolean has_more;
        public String state;
        public TL_storiesStealthMode stealth_mode;
        public ArrayList<PeerStories> peer_stories = new ArrayList<>();
        public ArrayList<TLRPC.Chat> chats = new ArrayList<>();
        public ArrayList<TLRPC.User> users = new ArrayList<>();

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.has_more = (readInt32 & 1) != 0;
            this.count = inputSerializedData.readInt32(z);
            this.state = inputSerializedData.readString(z);
            this.peer_stories = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_stories$TL_stories_allStories$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.Vector.TLDeserializer
                public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                    return TL_stories.PeerStories.TLdeserialize(inputSerializedData2, i, z2);
                }
            }, z);
            this.chats = Vector.deserialize(inputSerializedData, new TLRPC$TL_channels_adminLogResults$$ExternalSyntheticLambda1(), z);
            this.users = Vector.deserialize(inputSerializedData, new TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1(), z);
            this.stealth_mode = TL_storiesStealthMode.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.has_more ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            outputSerializedData.writeInt32(this.count);
            outputSerializedData.writeString(this.state);
            Vector.serialize(outputSerializedData, this.peer_stories);
            Vector.serialize(outputSerializedData, this.chats);
            Vector.serialize(outputSerializedData, this.users);
            this.stealth_mode.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_stories_allStoriesNotModified extends stories_AllStories {
        public static final int constructor = 291044926;
        public int flags;
        public String state;
        public TL_storiesStealthMode stealth_mode;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.state = inputSerializedData.readString(z);
            this.stealth_mode = TL_storiesStealthMode.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeString(this.state);
            this.stealth_mode.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_stories_canSendStory extends TLObject {
        public static final int constructor = -941629475;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TLRPC.Bool.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_stories_deleteStories extends TLObject {
        public static final int constructor = -1369842849;
        public ArrayList<Integer> id = new ArrayList<>();
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return Vector.TLDeserializeInt(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            Vector.serializeInt(outputSerializedData, this.id);
        }
    }

    public static class TL_stories_editStory extends TLObject {
        public static final int constructor = -1249658298;
        public String caption;
        public int flags;
        public int id;
        public TLRPC.InputMedia media;
        public TLRPC.InputPeer peer;
        public ArrayList<MediaArea> media_areas = new ArrayList<>();
        public ArrayList<TLRPC.MessageEntity> entities = new ArrayList<>();
        public ArrayList<TLRPC.InputPrivacyRule> privacy_rules = new ArrayList<>();

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TLRPC.Updates.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.id);
            if ((this.flags & 1) != 0) {
                this.media.serializeToStream(outputSerializedData);
            }
            if ((this.flags & 8) != 0) {
                Vector.serialize(outputSerializedData, this.media_areas);
            }
            if ((this.flags & 2) != 0) {
                outputSerializedData.writeString(this.caption);
            }
            if ((this.flags & 2) != 0) {
                Vector.serialize(outputSerializedData, this.entities);
            }
            if ((this.flags & 4) != 0) {
                Vector.serialize(outputSerializedData, this.privacy_rules);
            }
        }
    }

    public static class TL_stories_exportStoryLink extends TLObject {
        public static final int constructor = 2072899360;
        public int id;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_exportedStoryLink.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.id);
        }
    }

    public static class TL_stories_getAllReadPeerStories extends TLObject {
        public static final int constructor = -1688541191;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TLRPC.Updates.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
        }
    }

    public static class TL_stories_getAllStories extends TLObject {
        public static final int constructor = -290400731;
        public int flags;
        public boolean include_hidden;
        public boolean next;
        public String state;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return stories_AllStories.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.next ? this.flags | 2 : this.flags & (-3);
            this.flags = i;
            int i2 = this.include_hidden ? i | 4 : i & (-5);
            this.flags = i2;
            outputSerializedData.writeInt32(i2);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.state);
            }
        }
    }

    public static class TL_stories_getChatsToSend extends TLObject {
        public static final int constructor = -1519744160;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TLRPC.messages_Chats.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
        }
    }

    public static class TL_stories_getPeerMaxIDs extends TLObject {
        public static final int constructor = 1398375363;
        public ArrayList<TLRPC.InputPeer> id = new ArrayList<>();

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return Vector.TLDeserializeInt(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            Vector.serialize(outputSerializedData, this.id);
        }
    }

    public static class TL_stories_getPeerStories extends TLObject {
        public static final int constructor = 743103056;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_stories_peerStories.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_stories_getPinnedStories extends TLObject {
        public static final int constructor = 1478600156;
        public int limit;
        public int offset_id;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_stories_stories.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.offset_id);
            outputSerializedData.writeInt32(this.limit);
        }
    }

    public static class TL_stories_getStoriesArchive extends TLObject {
        public static final int constructor = -1271586794;
        public int limit;
        public int offset_id;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_stories_stories.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.offset_id);
            outputSerializedData.writeInt32(this.limit);
        }
    }

    public static class TL_stories_getStoriesByID extends TLObject {
        public static final int constructor = 1467271796;
        public ArrayList<Integer> id = new ArrayList<>();
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_stories_stories.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            Vector.serializeInt(outputSerializedData, this.id);
        }
    }

    public static class TL_stories_getStoriesViews extends TLObject {
        public static final int constructor = 685862088;
        public ArrayList<Integer> id = new ArrayList<>();
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_stories_storyViews.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            Vector.serializeInt(outputSerializedData, this.id);
        }
    }

    public static class TL_stories_getStoryViewsList extends TLObject {
        public static final int constructor = 2127707223;
        public int flags;
        public boolean forwards_first;
        public int id;
        public boolean just_contacts;
        public int limit;
        public String offset;
        public TLRPC.InputPeer peer;
        public String q;
        public boolean reactions_first;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return StoryViewsList.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.just_contacts ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            int i2 = this.reactions_first ? i | 4 : i & (-5);
            this.flags = i2;
            int i3 = this.forwards_first ? i2 | 8 : i2 & (-9);
            this.flags = i3;
            outputSerializedData.writeInt32(i3);
            this.peer.serializeToStream(outputSerializedData);
            if ((this.flags & 2) != 0) {
                outputSerializedData.writeString(this.q);
            }
            outputSerializedData.writeInt32(this.id);
            outputSerializedData.writeString(this.offset);
            outputSerializedData.writeInt32(this.limit);
        }
    }

    public static class TL_stories_incrementStoryViews extends TLObject {
        public static final int constructor = -1308456197;
        public ArrayList<Integer> id = new ArrayList<>();
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TLRPC.Bool.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            Vector.serializeInt(outputSerializedData, this.id);
        }
    }

    public static class TL_stories_peerStories extends TLObject {
        public static final int constructor = -890861720;
        public PeerStories stories;
        public ArrayList<TLRPC.Chat> chats = new ArrayList<>();
        public ArrayList<TLRPC.User> users = new ArrayList<>();

        public static TL_stories_peerStories TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-890861720 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_stories_peerStories", Integer.valueOf(i)));
                }
                return null;
            }
            TL_stories_peerStories tL_stories_peerStories = new TL_stories_peerStories();
            tL_stories_peerStories.readParams(inputSerializedData, z);
            return tL_stories_peerStories;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.stories = PeerStories.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.chats = Vector.deserialize(inputSerializedData, new TLRPC$TL_channels_adminLogResults$$ExternalSyntheticLambda1(), z);
            this.users = Vector.deserialize(inputSerializedData, new TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1(), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.stories.serializeToStream(outputSerializedData);
            Vector.serialize(outputSerializedData, this.chats);
            Vector.serialize(outputSerializedData, this.users);
        }
    }

    public static class TL_stories_readStories extends TLObject {
        public static final int constructor = -1521034552;
        public int max_id;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return Vector.TLDeserializeInt(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.max_id);
        }
    }

    public static class TL_stories_report extends TLObject {
        public static final int constructor = 433646405;
        public ArrayList<Integer> id = new ArrayList<>();
        public String message;
        public byte[] option;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TLRPC.ReportResult.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            Vector.serialize(outputSerializedData, new Vector$$ExternalSyntheticLambda3(outputSerializedData), this.id);
            outputSerializedData.writeByteArray(this.option);
            outputSerializedData.writeString(this.message);
        }
    }

    public static class TL_stories_searchPosts extends TLObject {
        public static final int constructor = -780072697;
        public MediaArea area;
        public int flags;
        public String hashtag;
        public int limit;
        public String offset;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_foundStories.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.hashtag);
            }
            if ((this.flags & 2) != 0) {
                this.area.serializeToStream(outputSerializedData);
            }
            if ((this.flags & 4) != 0) {
                this.peer.serializeToStream(outputSerializedData);
            }
            outputSerializedData.writeString(this.offset);
            outputSerializedData.writeInt32(this.limit);
        }
    }

    public static class TL_stories_sendReaction extends TLObject {
        public static final int constructor = 2144810674;
        public boolean add_to_recent;
        public int flags;
        public TLRPC.InputPeer peer;
        public TLRPC.Reaction reaction;
        public int story_id;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TLRPC.Updates.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.add_to_recent ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.story_id);
            this.reaction.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_stories_sendStory extends TLObject {
        public static final int constructor = -454661813;
        public String caption;
        public int flags;
        public TLRPC.InputPeer fwd_from_id;
        public int fwd_from_story;
        public boolean fwd_modified;
        public TLRPC.InputMedia media;
        public boolean noforwards;
        public TLRPC.InputPeer peer;
        public int period;
        public boolean pinned;
        public long random_id;
        public ArrayList<MediaArea> media_areas = new ArrayList<>();
        public ArrayList<TLRPC.MessageEntity> entities = new ArrayList<>();
        public ArrayList<TLRPC.InputPrivacyRule> privacy_rules = new ArrayList<>();

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TLRPC.Updates.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.pinned ? this.flags | 4 : this.flags & (-5);
            this.flags = i;
            int i2 = this.noforwards ? i | 16 : i & (-17);
            this.flags = i2;
            int i3 = this.fwd_modified ? i2 | 128 : i2 & (-129);
            this.flags = i3;
            outputSerializedData.writeInt32(i3);
            this.peer.serializeToStream(outputSerializedData);
            this.media.serializeToStream(outputSerializedData);
            if ((this.flags & 32) != 0) {
                Vector.serialize(outputSerializedData, this.media_areas);
            }
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.caption);
            }
            if ((this.flags & 2) != 0) {
                Vector.serialize(outputSerializedData, this.entities);
            }
            Vector.serialize(outputSerializedData, this.privacy_rules);
            outputSerializedData.writeInt64(this.random_id);
            if ((this.flags & 8) != 0) {
                outputSerializedData.writeInt32(this.period);
            }
            if ((this.flags & 64) != 0) {
                this.fwd_from_id.serializeToStream(outputSerializedData);
            }
            if ((this.flags & 64) != 0) {
                outputSerializedData.writeInt32(this.fwd_from_story);
            }
        }
    }

    public static class TL_stories_stories extends TLObject {
        public static final int constructor = 1673780490;
        public int count;
        public int flags;
        public ArrayList<StoryItem> stories = new ArrayList<>();
        public ArrayList<Integer> pinned_to_top = new ArrayList<>();
        public ArrayList<TLRPC.Chat> chats = new ArrayList<>();
        public ArrayList<TLRPC.User> users = new ArrayList<>();

        public static TL_stories_stories TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (1673780490 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_stories_stories", Integer.valueOf(i)));
                }
                return null;
            }
            TL_stories_stories tL_stories_stories = new TL_stories_stories();
            tL_stories_stories.readParams(inputSerializedData, z);
            return tL_stories_stories;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.count = inputSerializedData.readInt32(z);
            this.stories = Vector.deserialize(inputSerializedData, new TL_stories$TL_peerStories$$ExternalSyntheticLambda0(), z);
            if ((this.flags & 1) != 0) {
                this.pinned_to_top = Vector.deserializeInt(inputSerializedData, z);
            }
            this.chats = Vector.deserialize(inputSerializedData, new TLRPC$TL_channels_adminLogResults$$ExternalSyntheticLambda1(), z);
            this.users = Vector.deserialize(inputSerializedData, new TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1(), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeInt32(this.count);
            Vector.serialize(outputSerializedData, this.stories);
            if ((this.flags & 1) != 0) {
                Vector.serializeInt(outputSerializedData, this.pinned_to_top);
            }
            Vector.serialize(outputSerializedData, this.chats);
            Vector.serialize(outputSerializedData, this.users);
        }
    }

    public static class TL_stories_storyViews extends TLObject {
        public static final int constructor = -560009955;
        public ArrayList<StoryViews> views = new ArrayList<>();
        public ArrayList<TLRPC.User> users = new ArrayList<>();

        public static TL_stories_storyViews TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-560009955 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_stories_storyViews", Integer.valueOf(i)));
                }
                return null;
            }
            TL_stories_storyViews tL_stories_storyViews = new TL_stories_storyViews();
            tL_stories_storyViews.readParams(inputSerializedData, z);
            return tL_stories_storyViews;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.views = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_stories$TL_stories_storyViews$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.Vector.TLDeserializer
                public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                    return TL_stories.StoryViews.TLdeserialize(inputSerializedData2, i, z2);
                }
            }, z);
            this.users = Vector.deserialize(inputSerializedData, new TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1(), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            Vector.serialize(outputSerializedData, this.views);
            Vector.serialize(outputSerializedData, this.users);
        }
    }

    public static class TL_stories_togglePeerStoriesHidden extends TLObject {
        public static final int constructor = -1123805756;
        public boolean hidden;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TLRPC.Bool.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeBool(this.hidden);
        }
    }

    public static class TL_storyFwdHeader extends StoryFwdHeader {
        public static final int constructor = -1205411504;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.modified = (readInt32 & 8) != 0;
            if ((readInt32 & 1) != 0) {
                this.from = TLRPC.Peer.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
            if ((this.flags & 2) != 0) {
                this.from_name = inputSerializedData.readString(z);
            }
            if ((this.flags & 4) != 0) {
                this.story_id = inputSerializedData.readInt32(z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.modified ? this.flags | 8 : this.flags & (-9);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            if ((this.flags & 1) != 0) {
                this.from.serializeToStream(outputSerializedData);
            }
            if ((this.flags & 2) != 0) {
                outputSerializedData.writeString(this.from_name);
            }
            if ((this.flags & 4) != 0) {
                outputSerializedData.writeInt32(this.story_id);
            }
        }
    }

    public static class TL_storyItem extends StoryItem {
        public static final int constructor = 2041735716;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.pinned = (readInt32 & 32) != 0;
            this.isPublic = (readInt32 & 128) != 0;
            this.close_friends = (readInt32 & 256) != 0;
            this.min = (readInt32 & 512) != 0;
            this.noforwards = (readInt32 & 1024) != 0;
            this.edited = (readInt32 & 2048) != 0;
            this.contacts = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) != 0;
            this.selected_contacts = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
            this.out = (readInt32 & 65536) != 0;
            this.id = inputSerializedData.readInt32(z);
            this.date = inputSerializedData.readInt32(z);
            if ((this.flags & 262144) != 0) {
                this.from_id = TLRPC.Peer.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
            if ((this.flags & 131072) != 0) {
                this.fwd_from = StoryFwdHeader.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
            this.expire_date = inputSerializedData.readInt32(z);
            if ((this.flags & 1) != 0) {
                this.caption = inputSerializedData.readString(z);
            }
            if ((this.flags & 2) != 0) {
                this.entities = Vector.deserialize(inputSerializedData, new MessagesStorage$$ExternalSyntheticLambda42(), z);
            }
            this.media = TLRPC.MessageMedia.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            if ((this.flags & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0) {
                this.media_areas = Vector.deserialize(inputSerializedData, new TL_stories$TL_storyItem$$ExternalSyntheticLambda0(), z);
            }
            if ((this.flags & 4) != 0) {
                this.privacy = Vector.deserialize(inputSerializedData, new TLRPC$TL_updatePrivacy$$ExternalSyntheticLambda0(), z);
            }
            if ((this.flags & 8) != 0) {
                this.views = StoryViews.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
            if ((this.flags & 32768) != 0) {
                this.sent_reaction = TLRPC.Reaction.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.pinned ? this.flags | 32 : this.flags & (-33);
            this.flags = i;
            int i2 = this.isPublic ? i | 128 : i & (-129);
            this.flags = i2;
            int i3 = this.close_friends ? i2 | 256 : i2 & (-257);
            this.flags = i3;
            int i4 = this.min ? i3 | 512 : i3 & (-513);
            this.flags = i4;
            int i5 = this.noforwards ? i4 | 1024 : i4 & (-1025);
            this.flags = i5;
            int i6 = this.edited ? i5 | 2048 : i5 & (-2049);
            this.flags = i6;
            int i7 = this.contacts ? i6 | LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM : i6 & (-4097);
            this.flags = i7;
            int i8 = this.selected_contacts ? i7 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i7 & (-8193);
            this.flags = i8;
            int i9 = this.out ? i8 | 65536 : i8 & (-65537);
            this.flags = i9;
            outputSerializedData.writeInt32(i9);
            outputSerializedData.writeInt32(this.id);
            outputSerializedData.writeInt32(this.date);
            if ((this.flags & 262144) != 0) {
                this.from_id.serializeToStream(outputSerializedData);
            }
            if ((this.flags & 131072) != 0) {
                this.fwd_from.serializeToStream(outputSerializedData);
            }
            outputSerializedData.writeInt32(this.expire_date);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.caption);
            }
            if ((this.flags & 2) != 0) {
                Vector.serialize(outputSerializedData, this.entities);
            }
            this.media.serializeToStream(outputSerializedData);
            if ((this.flags & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0) {
                Vector.serialize(outputSerializedData, this.media_areas);
            }
            if ((this.flags & 4) != 0) {
                Vector.serialize(outputSerializedData, this.privacy);
            }
            if ((this.flags & 8) != 0) {
                this.views.serializeToStream(outputSerializedData);
            }
            if ((this.flags & 32768) != 0) {
                this.sent_reaction.serializeToStream(outputSerializedData);
            }
        }
    }

    public static class TL_storyItemDeleted extends StoryItem {
        public static final int constructor = 1374088783;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.id = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.id);
        }
    }

    public static class TL_storyItemSkipped extends StoryItem {
        public static final int constructor = -5388013;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.close_friends = (readInt32 & 256) != 0;
            this.id = inputSerializedData.readInt32(z);
            this.date = inputSerializedData.readInt32(z);
            this.expire_date = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.close_friends ? this.flags | 256 : this.flags & (-257);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            outputSerializedData.writeInt32(this.id);
            outputSerializedData.writeInt32(this.date);
            outputSerializedData.writeInt32(this.expire_date);
        }
    }

    public static class TL_storyItem_layer160 extends TL_storyItem {
        public static final int constructor = 1445635639;

        @Override // org.telegram.tgnet.tl.TL_stories.TL_storyItem, org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.pinned = (readInt32 & 32) != 0;
            this.isPublic = (readInt32 & 128) != 0;
            this.close_friends = (readInt32 & 256) != 0;
            this.min = (readInt32 & 512) != 0;
            this.noforwards = (readInt32 & 1024) != 0;
            this.edited = (readInt32 & 2048) != 0;
            this.contacts = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) != 0;
            this.selected_contacts = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
            this.id = inputSerializedData.readInt32(z);
            this.date = inputSerializedData.readInt32(z);
            this.expire_date = inputSerializedData.readInt32(z);
            if ((this.flags & 1) != 0) {
                this.caption = inputSerializedData.readString(z);
            }
            if ((this.flags & 2) != 0) {
                this.entities = Vector.deserialize(inputSerializedData, new MessagesStorage$$ExternalSyntheticLambda42(), z);
            }
            this.media = TLRPC.MessageMedia.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            if ((this.flags & 4) != 0) {
                this.privacy = Vector.deserialize(inputSerializedData, new TLRPC$TL_updatePrivacy$$ExternalSyntheticLambda0(), z);
            }
            if ((this.flags & 8) != 0) {
                this.views = StoryViews.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
        }

        @Override // org.telegram.tgnet.tl.TL_stories.TL_storyItem, org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.pinned ? this.flags | 32 : this.flags & (-33);
            this.flags = i;
            int i2 = this.isPublic ? i | 128 : i & (-129);
            this.flags = i2;
            int i3 = this.close_friends ? i2 | 256 : i2 & (-257);
            this.flags = i3;
            int i4 = this.min ? i3 | 512 : i3 & (-513);
            this.flags = i4;
            int i5 = this.noforwards ? i4 | 1024 : i4 & (-1025);
            this.flags = i5;
            int i6 = this.edited ? i5 | 2048 : i5 & (-2049);
            this.flags = i6;
            int i7 = this.contacts ? i6 | LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM : i6 & (-4097);
            this.flags = i7;
            int i8 = this.selected_contacts ? i7 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i7 & (-8193);
            this.flags = i8;
            outputSerializedData.writeInt32(i8);
            outputSerializedData.writeInt32(this.id);
            outputSerializedData.writeInt32(this.date);
            outputSerializedData.writeInt32(this.expire_date);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.caption);
            }
            if ((this.flags & 2) != 0) {
                Vector.serialize(outputSerializedData, this.entities);
            }
            this.media.serializeToStream(outputSerializedData);
            if ((this.flags & 4) != 0) {
                Vector.serialize(outputSerializedData, this.privacy);
            }
            if ((this.flags & 8) != 0) {
                this.views.serializeToStream(outputSerializedData);
            }
        }
    }

    public static class TL_storyItem_layer166 extends TL_storyItem {
        public static final int constructor = 1153718222;

        @Override // org.telegram.tgnet.tl.TL_stories.TL_storyItem, org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.pinned = (readInt32 & 32) != 0;
            this.isPublic = (readInt32 & 128) != 0;
            this.close_friends = (readInt32 & 256) != 0;
            this.min = (readInt32 & 512) != 0;
            this.noforwards = (readInt32 & 1024) != 0;
            this.edited = (readInt32 & 2048) != 0;
            this.contacts = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) != 0;
            this.selected_contacts = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
            this.out = (readInt32 & 65536) != 0;
            this.id = inputSerializedData.readInt32(z);
            this.date = inputSerializedData.readInt32(z);
            this.expire_date = inputSerializedData.readInt32(z);
            if ((this.flags & 1) != 0) {
                this.caption = inputSerializedData.readString(z);
            }
            if ((this.flags & 2) != 0) {
                this.entities = Vector.deserialize(inputSerializedData, new MessagesStorage$$ExternalSyntheticLambda42(), z);
            }
            this.media = TLRPC.MessageMedia.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            if ((this.flags & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0) {
                this.media_areas = Vector.deserialize(inputSerializedData, new TL_stories$TL_storyItem$$ExternalSyntheticLambda0(), z);
            }
            if ((this.flags & 4) != 0) {
                this.privacy = Vector.deserialize(inputSerializedData, new TLRPC$TL_updatePrivacy$$ExternalSyntheticLambda0(), z);
            }
            if ((this.flags & 8) != 0) {
                this.views = StoryViews.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
            if ((this.flags & 32768) != 0) {
                this.sent_reaction = TLRPC.Reaction.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
        }

        @Override // org.telegram.tgnet.tl.TL_stories.TL_storyItem, org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.pinned ? this.flags | 32 : this.flags & (-33);
            this.flags = i;
            int i2 = this.isPublic ? i | 128 : i & (-129);
            this.flags = i2;
            int i3 = this.close_friends ? i2 | 256 : i2 & (-257);
            this.flags = i3;
            int i4 = this.min ? i3 | 512 : i3 & (-513);
            this.flags = i4;
            int i5 = this.noforwards ? i4 | 1024 : i4 & (-1025);
            this.flags = i5;
            int i6 = this.edited ? i5 | 2048 : i5 & (-2049);
            this.flags = i6;
            int i7 = this.contacts ? i6 | LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM : i6 & (-4097);
            this.flags = i7;
            int i8 = this.selected_contacts ? i7 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i7 & (-8193);
            this.flags = i8;
            int i9 = this.out ? i8 | 65536 : i8 & (-65537);
            this.flags = i9;
            outputSerializedData.writeInt32(i9);
            outputSerializedData.writeInt32(this.id);
            outputSerializedData.writeInt32(this.date);
            outputSerializedData.writeInt32(this.expire_date);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.caption);
            }
            if ((this.flags & 2) != 0) {
                Vector.serialize(outputSerializedData, this.entities);
            }
            this.media.serializeToStream(outputSerializedData);
            if ((this.flags & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0) {
                Vector.serialize(outputSerializedData, this.media_areas);
            }
            if ((this.flags & 4) != 0) {
                Vector.serialize(outputSerializedData, this.privacy);
            }
            if ((this.flags & 8) != 0) {
                this.views.serializeToStream(outputSerializedData);
            }
            if ((this.flags & 32768) != 0) {
                this.sent_reaction.serializeToStream(outputSerializedData);
            }
        }
    }

    public static class TL_storyItem_layer174 extends TL_storyItem {
        public static final int constructor = -1352440415;

        @Override // org.telegram.tgnet.tl.TL_stories.TL_storyItem, org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.pinned = (readInt32 & 32) != 0;
            this.isPublic = (readInt32 & 128) != 0;
            this.close_friends = (readInt32 & 256) != 0;
            this.min = (readInt32 & 512) != 0;
            this.noforwards = (readInt32 & 1024) != 0;
            this.edited = (readInt32 & 2048) != 0;
            this.contacts = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) != 0;
            this.selected_contacts = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
            this.out = (readInt32 & 65536) != 0;
            this.id = inputSerializedData.readInt32(z);
            this.date = inputSerializedData.readInt32(z);
            if ((this.flags & 131072) != 0) {
                this.fwd_from = StoryFwdHeader.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
            this.expire_date = inputSerializedData.readInt32(z);
            if ((this.flags & 1) != 0) {
                this.caption = inputSerializedData.readString(z);
            }
            if ((this.flags & 2) != 0) {
                this.entities = Vector.deserialize(inputSerializedData, new MessagesStorage$$ExternalSyntheticLambda42(), z);
            }
            this.media = TLRPC.MessageMedia.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            if ((this.flags & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0) {
                this.media_areas = Vector.deserialize(inputSerializedData, new TL_stories$TL_storyItem$$ExternalSyntheticLambda0(), z);
            }
            if ((this.flags & 4) != 0) {
                this.privacy = Vector.deserialize(inputSerializedData, new TLRPC$TL_updatePrivacy$$ExternalSyntheticLambda0(), z);
            }
            if ((this.flags & 8) != 0) {
                this.views = StoryViews.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
            if ((this.flags & 32768) != 0) {
                this.sent_reaction = TLRPC.Reaction.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
        }

        @Override // org.telegram.tgnet.tl.TL_stories.TL_storyItem, org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.pinned ? this.flags | 32 : this.flags & (-33);
            this.flags = i;
            int i2 = this.isPublic ? i | 128 : i & (-129);
            this.flags = i2;
            int i3 = this.close_friends ? i2 | 256 : i2 & (-257);
            this.flags = i3;
            int i4 = this.min ? i3 | 512 : i3 & (-513);
            this.flags = i4;
            int i5 = this.noforwards ? i4 | 1024 : i4 & (-1025);
            this.flags = i5;
            int i6 = this.edited ? i5 | 2048 : i5 & (-2049);
            this.flags = i6;
            int i7 = this.contacts ? i6 | LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM : i6 & (-4097);
            this.flags = i7;
            int i8 = this.selected_contacts ? i7 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i7 & (-8193);
            this.flags = i8;
            int i9 = this.out ? i8 | 65536 : i8 & (-65537);
            this.flags = i9;
            outputSerializedData.writeInt32(i9);
            outputSerializedData.writeInt32(this.id);
            outputSerializedData.writeInt32(this.date);
            if ((this.flags & 131072) != 0) {
                this.fwd_from.serializeToStream(outputSerializedData);
            }
            outputSerializedData.writeInt32(this.expire_date);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.caption);
            }
            if ((this.flags & 2) != 0) {
                Vector.serialize(outputSerializedData, this.entities);
            }
            this.media.serializeToStream(outputSerializedData);
            if ((this.flags & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0) {
                Vector.serialize(outputSerializedData, this.media_areas);
            }
            if ((this.flags & 4) != 0) {
                Vector.serialize(outputSerializedData, this.privacy);
            }
            if ((this.flags & 8) != 0) {
                this.views.serializeToStream(outputSerializedData);
            }
            if ((this.flags & 32768) != 0) {
                this.sent_reaction.serializeToStream(outputSerializedData);
            }
        }
    }

    public static class TL_storyReaction extends StoryReaction {
        public static final int constructor = 1620104917;
        public int date;
        public TLRPC.Reaction reaction;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.peer_id = TLRPC.Peer.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.date = inputSerializedData.readInt32(z);
            this.reaction = TLRPC.Reaction.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer_id.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.date);
            this.reaction.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_storyReactionPublicForward extends StoryReaction {
        public static final int constructor = -1146411453;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.message = TLRPC.Message.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.message.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_storyReactionPublicRepost extends StoryReaction {
        public static final int constructor = -808644845;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.peer_id = TLRPC.Peer.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            StoryItem TLdeserialize = StoryItem.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.story = TLdeserialize;
            if (TLdeserialize != null) {
                TLdeserialize.dialogId = DialogObject.getPeerDialogId(this.peer_id);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer_id.serializeToStream(outputSerializedData);
            this.story.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_storyReactionsList extends TLObject {
        public static final int constructor = -1436583780;
        public int count;
        public int flags;
        public String next_offset;
        public ArrayList<StoryReaction> reactions = new ArrayList<>();
        public ArrayList<TLRPC.Chat> chats = new ArrayList<>();
        public ArrayList<TLRPC.User> users = new ArrayList<>();

        public static TL_storyReactionsList TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-1436583780 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_storyReactionsList", Integer.valueOf(i)));
                }
                return null;
            }
            TL_storyReactionsList tL_storyReactionsList = new TL_storyReactionsList();
            tL_storyReactionsList.readParams(inputSerializedData, z);
            return tL_storyReactionsList;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.count = inputSerializedData.readInt32(z);
            this.reactions = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_stories$TL_storyReactionsList$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.Vector.TLDeserializer
                public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                    return TL_stories.StoryReaction.TLdeserialize(inputSerializedData2, i, z2);
                }
            }, z);
            this.chats = Vector.deserialize(inputSerializedData, new TLRPC$TL_channels_adminLogResults$$ExternalSyntheticLambda1(), z);
            this.users = Vector.deserialize(inputSerializedData, new TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1(), z);
            if ((this.flags & 1) != 0) {
                this.next_offset = inputSerializedData.readString(z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeInt32(this.count);
            Vector.serialize(outputSerializedData, this.reactions);
            Vector.serialize(outputSerializedData, this.chats);
            Vector.serialize(outputSerializedData, this.users);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.next_offset);
            }
        }
    }

    public static class TL_storyView extends StoryView {
        public static final int constructor = -1329730875;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.blocked = (readInt32 & 1) != 0;
            this.blocked_my_stories_from = (readInt32 & 2) != 0;
            this.user_id = inputSerializedData.readInt64(z);
            this.date = inputSerializedData.readInt32(z);
            if ((this.flags & 4) != 0) {
                this.reaction = TLRPC.Reaction.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.blocked ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            int i2 = this.blocked_my_stories_from ? i | 2 : i & (-3);
            this.flags = i2;
            outputSerializedData.writeInt32(i2);
            outputSerializedData.writeInt64(this.user_id);
            outputSerializedData.writeInt32(this.date);
            if ((this.flags & 4) != 0) {
                this.reaction.serializeToStream(outputSerializedData);
            }
        }
    }

    public static class TL_storyViewPublicForward extends StoryView {
        public static final int constructor = -1870436597;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.blocked = (readInt32 & 1) != 0;
            this.blocked_my_stories_from = (readInt32 & 2) != 0;
            this.message = TLRPC.Message.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.blocked ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            int i2 = this.blocked_my_stories_from ? i | 2 : i & (-3);
            this.flags = i2;
            outputSerializedData.writeInt32(i2);
            this.message.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_storyViewPublicRepost extends StoryView {
        public static final int constructor = -1116418231;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.blocked = (readInt32 & 1) != 0;
            this.blocked_my_stories_from = (readInt32 & 2) != 0;
            this.peer_id = TLRPC.Peer.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.story = StoryItem.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.blocked ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            int i2 = this.blocked_my_stories_from ? i | 2 : i & (-3);
            this.flags = i2;
            outputSerializedData.writeInt32(i2);
            this.peer_id.serializeToStream(outputSerializedData);
            this.story.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_storyViews extends StoryViews {
        public static final int constructor = -1923523370;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.has_viewers = (readInt32 & 2) != 0;
            this.views_count = inputSerializedData.readInt32(z);
            if ((this.flags & 4) != 0) {
                this.forwards_count = inputSerializedData.readInt32(z);
            }
            if ((this.flags & 8) != 0) {
                this.reactions = Vector.deserialize(inputSerializedData, new TLRPC$TL_messageReactions$$ExternalSyntheticLambda0(), z);
            }
            if ((this.flags & 16) != 0) {
                this.reactions_count = inputSerializedData.readInt32(z);
            }
            if ((this.flags & 1) != 0) {
                this.recent_viewers = Vector.deserializeLong(inputSerializedData, z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.has_viewers ? this.flags | 2 : this.flags & (-3);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            outputSerializedData.writeInt32(this.views_count);
            if ((this.flags & 4) != 0) {
                outputSerializedData.writeInt32(this.forwards_count);
            }
            if ((this.flags & 8) != 0) {
                Vector.serialize(outputSerializedData, this.reactions);
            }
            if ((this.flags & 16) != 0) {
                outputSerializedData.writeInt32(this.reactions_count);
            }
            if ((this.flags & 1) != 0) {
                Vector.serializeLong(outputSerializedData, this.recent_viewers);
            }
        }
    }

    public static class TL_storyViewsList extends StoryViewsList {
        public static final int constructor = 1507299269;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.count = inputSerializedData.readInt32(z);
            this.views_count = inputSerializedData.readInt32(z);
            this.forwards_count = inputSerializedData.readInt32(z);
            this.reactions_count = inputSerializedData.readInt32(z);
            this.views = Vector.deserialize(inputSerializedData, new TL_stories$TL_storyViewsList$$ExternalSyntheticLambda0(), z);
            this.chats = Vector.deserialize(inputSerializedData, new TLRPC$TL_channels_adminLogResults$$ExternalSyntheticLambda1(), z);
            this.users = Vector.deserialize(inputSerializedData, new TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1(), z);
            if ((this.flags & 1) != 0) {
                this.next_offset = inputSerializedData.readString(z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeInt32(this.count);
            outputSerializedData.writeInt32(this.views_count);
            outputSerializedData.writeInt32(this.forwards_count);
            outputSerializedData.writeInt32(this.reactions_count);
            Vector.serialize(outputSerializedData, this.views);
            Vector.serialize(outputSerializedData, this.chats);
            Vector.serialize(outputSerializedData, this.users);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.next_offset);
            }
        }
    }

    public static class TL_storyViewsList_layer167 extends StoryViewsList {
        public static final int constructor = 1189722604;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.count = inputSerializedData.readInt32(z);
            this.reactions_count = inputSerializedData.readInt32(z);
            this.views = Vector.deserialize(inputSerializedData, new TL_stories$TL_storyViewsList$$ExternalSyntheticLambda0(), z);
            this.users = Vector.deserialize(inputSerializedData, new TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1(), z);
            if ((this.flags & 1) != 0) {
                this.next_offset = inputSerializedData.readString(z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeInt32(this.count);
            outputSerializedData.writeInt32(this.reactions_count);
            Vector.serialize(outputSerializedData, this.views);
            Vector.serialize(outputSerializedData, this.users);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.next_offset);
            }
        }
    }

    public static class TL_storyViews_layer160 extends StoryViews {
        public static final int constructor = -748199729;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.views_count = inputSerializedData.readInt32(z);
            if ((this.flags & 1) != 0) {
                this.recent_viewers = Vector.deserializeLong(inputSerializedData, z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeInt32(this.views_count);
            if ((this.flags & 1) != 0) {
                Vector.serializeLong(outputSerializedData, this.recent_viewers);
            }
        }
    }

    public static class TL_storyViews_layer161 extends StoryViews {
        public static final int constructor = -968094825;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.views_count = inputSerializedData.readInt32(z);
            this.reactions_count = inputSerializedData.readInt32(z);
            if ((this.flags & 1) != 0) {
                this.recent_viewers = Vector.deserializeLong(inputSerializedData, z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeInt32(this.views_count);
            outputSerializedData.writeInt32(this.reactions_count);
            if ((this.flags & 1) != 0) {
                Vector.serializeLong(outputSerializedData, this.recent_viewers);
            }
        }
    }

    public static class TL_togglePinnedToTop extends TLObject {
        public static final int constructor = 187268763;
        public ArrayList<Integer> id = new ArrayList<>();
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TLRPC.Bool.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            Vector.serializeInt(outputSerializedData, this.id);
        }
    }

    public static class TL_updateReadStories extends TLRPC.Update {
        public static final int constructor = -145845461;
        public int max_id;
        public TLRPC.Peer peer;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.peer = TLRPC.Peer.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.max_id = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.max_id);
        }
    }

    public static class TL_updateStoriesStealthMode extends TLRPC.Update {
        public static final int constructor = 738741697;
        public TL_storiesStealthMode stealth_mode;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.stealth_mode = TL_storiesStealthMode.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.stealth_mode.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_updateStory extends TLRPC.Update {
        public static final int constructor = 1974712216;
        public TLRPC.Peer peer;
        public StoryItem story;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.peer = TLRPC.Peer.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.story = StoryItem.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            this.story.serializeToStream(outputSerializedData);
        }
    }

    public static abstract class stories_AllStories extends TLObject {
        public static stories_AllStories TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            stories_AllStories tL_stories_allStories = i != 291044926 ? i != 1862033025 ? null : new TL_stories_allStories() : new TL_stories_allStoriesNotModified();
            if (tL_stories_allStories == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in stories_AllStories", Integer.valueOf(i)));
            }
            if (tL_stories_allStories != null) {
                tL_stories_allStories.readParams(inputSerializedData, z);
            }
            return tL_stories_allStories;
        }
    }

    public static class togglePinned extends TLObject {
        public static final int constructor = -1703566865;
        public ArrayList<Integer> id = new ArrayList<>();
        public TLRPC.InputPeer peer;
        public boolean pinned;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return Vector.TLDeserializeInt(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            Vector.serializeInt(outputSerializedData, this.id);
            outputSerializedData.writeBool(this.pinned);
        }
    }
}
