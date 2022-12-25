package org.telegram.messenger.video;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.view.Surface;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.CharacterCompat;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
/* loaded from: classes.dex */
public class MediaCodecVideoConvertor {
    private static final int MEDIACODEC_TIMEOUT_DEFAULT = 2500;
    private static final int MEDIACODEC_TIMEOUT_INCREASED = 22000;
    private static final int PROCESSOR_TYPE_INTEL = 2;
    private static final int PROCESSOR_TYPE_MTK = 3;
    private static final int PROCESSOR_TYPE_OTHER = 0;
    private static final int PROCESSOR_TYPE_QCOM = 1;
    private static final int PROCESSOR_TYPE_SEC = 4;
    private static final int PROCESSOR_TYPE_TI = 5;
    private MediaController.VideoConvertorListener callback;
    private long endPresentationTime;
    private MediaExtractor extractor;
    private MP4Builder mediaMuxer;

    public boolean convertVideo(String str, File file, int i, boolean z, int i2, int i3, int i4, int i5, int i6, int i7, int i8, long j, long j2, long j3, boolean z2, long j4, MediaController.SavedFilterState savedFilterState, String str2, ArrayList<VideoEditedInfo.MediaEntity> arrayList, boolean z3, MediaController.CropState cropState, boolean z4, MediaController.VideoConvertorListener videoConvertorListener) {
        this.callback = videoConvertorListener;
        return convertVideoInternal(str, file, i, z, i2, i3, i4, i5, i6, i7, i8, j, j2, j3, j4, z2, false, savedFilterState, str2, arrayList, z3, cropState, z4);
    }

    public long getLastFrameTimestamp() {
        return this.endPresentationTime;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:(25:(2:496|497)(2:1308|(47:1310|1311|1312|1313|1314|499|(3:501|(1:1302)(1:505)|506)(1:1303)|507|(1:509)|510|511|(3:513|514|(39:516|(1:518)|519|520|521|522|523|524|525|526|528|529|530|531|532|533|534|(8:536|537|538|539|540|541|542|(23:544|545|546|548|549|550|551|552|553|(2:1232|1233)(1:555)|556|(7:1146|1147|(3:1223|1224|(4:1226|1150|(1:1152)|(11:(9:1192|1193|1194|1195|(1:1197)|1198|1199|(2:1201|1202)(2:1205|1206)|1203)(12:1155|1156|1157|1158|(3:1160|1161|1162)(2:1184|1185)|1163|1164|1165|1166|1167|1168|1169)|(1:561)(1:1145)|562|563|564|(2:(6:594|595|(1:1136)(4:598|599|600|601)|(5:1012|1013|1014|(5:1016|1017|1018|(4:1020|(1:1022)(1:1043)|1023|(1:1025)(1:1042))(1:1044)|1026)(5:1049|(2:1051|(1:(16:1054|1055|1056|(4:1104|1105|1106|(3:1108|1109|1110))(1:1058)|1059|1060|1061|1062|(1:1064)|1065|(1:1067)(2:1098|1099)|1068|(3:1075|1076|(9:1080|1081|1082|1083|(1:1085)|1086|1087|1088|1089))|1097|1088|1089))(3:1122|(1:1124)|1089))|1125|(0)|1089)|(3:1028|1029|(2:1031|1032)))(1:603)|604|(1:(11:609|610|(1:612)|613|614|615|616|(1:618)(2:829|(4:993|994|(1:996)|997)(2:831|(3:833|(1:865)(7:836|837|838|839|(3:841|842|(5:844|845|846|847|848))(1:859)|858|848)|849)(3:866|867|(4:869|870|(1:872)(1:987)|(12:874|875|(12:892|893|894|(5:(1:947)(3:899|900|901)|(3:905|(2:907|(2:908|(1:927)(3:910|(2:925|926)(2:916|(2:920|921))|923)))(0)|928)|929|930|(4:934|935|(1:937)|938))(2:948|(14:950|(3:954|(2:960|(2:962|963)(1:970))|971)|976|964|(1:967)|968|969|880|881|(1:883)(1:886)|884|885|(3:826|827|828)(5:621|(7:623|624|625|626|(1:628)(2:632|(23:634|(3:810|811|(1:813))(1:(20:637|(1:639)(1:805)|640|641|(1:804)(3:645|646|647)|648|(4:650|651|652|(6:654|655|656|657|658|(16:660|(3:785|786|787)(4:662|663|664|665)|666|667|668|669|670|(4:672|673|674|(1:678))(1:779)|679|(1:681)(1:772)|682|(1:771)(2:686|(3:688|(1:690)(1:766)|691)(3:767|(1:769)|770))|(1:693)(3:760|(1:764)|765)|(9:703|704|705|(1:707)(1:754)|708|709|710|(4:712|713|714|715)(1:749)|716)(1:695)|696|(3:698|(1:700)|701)(1:702))(13:791|792|670|(0)(0)|679|(0)(0)|682|(1:684)|771|(0)(0)|(0)(0)|696|(0)(0))))(1:803)|799|792|670|(0)(0)|679|(0)(0)|682|(0)|771|(0)(0)|(0)(0)|696|(0)(0))(1:806))|797|798|719|720|721|722|723|(1:745)(1:726)|727|728|729|730|731|732|733|734|735|570|571|(1:573)|575))|629|630)(1:825)|819|629|630)|631))|879|880|881|(0)(0)|884|885|(0)(0)|631)(1:877)|878|879|880|881|(0)(0)|884|885|(0)(0)|631)(4:983|984|985|986))(3:988|989|990))))|619|(0)(0)|631)))|608)|569|570|571|(0)|575)(1:1221)))|1149|1150|(0)|(0)(0))(1:558)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575)(1:1249))(1:1261)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575))(1:1293)|1291|522|523|524|525|526|528|529|530|531|532|533|534|(0)(0)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575)(6:1319|1320|1321|1322|1323|1324))|533|534|(0)(0)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575)|522|523|524|525|526|528|529|530|531|532) */
    /* JADX WARN: Can't wrap try/catch for region: R(12:874|875|(12:892|893|894|(5:(1:947)(3:899|900|901)|(3:905|(2:907|(2:908|(1:927)(3:910|(2:925|926)(2:916|(2:920|921))|923)))(0)|928)|929|930|(4:934|935|(1:937)|938))(2:948|(14:950|(3:954|(2:960|(2:962|963)(1:970))|971)|976|964|(1:967)|968|969|880|881|(1:883)(1:886)|884|885|(3:826|827|828)(5:621|(7:623|624|625|626|(1:628)(2:632|(23:634|(3:810|811|(1:813))(1:(20:637|(1:639)(1:805)|640|641|(1:804)(3:645|646|647)|648|(4:650|651|652|(6:654|655|656|657|658|(16:660|(3:785|786|787)(4:662|663|664|665)|666|667|668|669|670|(4:672|673|674|(1:678))(1:779)|679|(1:681)(1:772)|682|(1:771)(2:686|(3:688|(1:690)(1:766)|691)(3:767|(1:769)|770))|(1:693)(3:760|(1:764)|765)|(9:703|704|705|(1:707)(1:754)|708|709|710|(4:712|713|714|715)(1:749)|716)(1:695)|696|(3:698|(1:700)|701)(1:702))(13:791|792|670|(0)(0)|679|(0)(0)|682|(1:684)|771|(0)(0)|(0)(0)|696|(0)(0))))(1:803)|799|792|670|(0)(0)|679|(0)(0)|682|(0)|771|(0)(0)|(0)(0)|696|(0)(0))(1:806))|797|798|719|720|721|722|723|(1:745)(1:726)|727|728|729|730|731|732|733|734|735|570|571|(1:573)|575))|629|630)(1:825)|819|629|630)|631))|879|880|881|(0)(0)|884|885|(0)(0)|631)(1:877)|878|879|880|881|(0)(0)|884|885|(0)(0)|631) */
    /* JADX WARN: Can't wrap try/catch for region: R(25:(2:496|497)(2:1308|(47:1310|1311|1312|1313|1314|499|(3:501|(1:1302)(1:505)|506)(1:1303)|507|(1:509)|510|511|(3:513|514|(39:516|(1:518)|519|520|521|522|523|524|525|526|528|529|530|531|532|533|534|(8:536|537|538|539|540|541|542|(23:544|545|546|548|549|550|551|552|553|(2:1232|1233)(1:555)|556|(7:1146|1147|(3:1223|1224|(4:1226|1150|(1:1152)|(11:(9:1192|1193|1194|1195|(1:1197)|1198|1199|(2:1201|1202)(2:1205|1206)|1203)(12:1155|1156|1157|1158|(3:1160|1161|1162)(2:1184|1185)|1163|1164|1165|1166|1167|1168|1169)|(1:561)(1:1145)|562|563|564|(2:(6:594|595|(1:1136)(4:598|599|600|601)|(5:1012|1013|1014|(5:1016|1017|1018|(4:1020|(1:1022)(1:1043)|1023|(1:1025)(1:1042))(1:1044)|1026)(5:1049|(2:1051|(1:(16:1054|1055|1056|(4:1104|1105|1106|(3:1108|1109|1110))(1:1058)|1059|1060|1061|1062|(1:1064)|1065|(1:1067)(2:1098|1099)|1068|(3:1075|1076|(9:1080|1081|1082|1083|(1:1085)|1086|1087|1088|1089))|1097|1088|1089))(3:1122|(1:1124)|1089))|1125|(0)|1089)|(3:1028|1029|(2:1031|1032)))(1:603)|604|(1:(11:609|610|(1:612)|613|614|615|616|(1:618)(2:829|(4:993|994|(1:996)|997)(2:831|(3:833|(1:865)(7:836|837|838|839|(3:841|842|(5:844|845|846|847|848))(1:859)|858|848)|849)(3:866|867|(4:869|870|(1:872)(1:987)|(12:874|875|(12:892|893|894|(5:(1:947)(3:899|900|901)|(3:905|(2:907|(2:908|(1:927)(3:910|(2:925|926)(2:916|(2:920|921))|923)))(0)|928)|929|930|(4:934|935|(1:937)|938))(2:948|(14:950|(3:954|(2:960|(2:962|963)(1:970))|971)|976|964|(1:967)|968|969|880|881|(1:883)(1:886)|884|885|(3:826|827|828)(5:621|(7:623|624|625|626|(1:628)(2:632|(23:634|(3:810|811|(1:813))(1:(20:637|(1:639)(1:805)|640|641|(1:804)(3:645|646|647)|648|(4:650|651|652|(6:654|655|656|657|658|(16:660|(3:785|786|787)(4:662|663|664|665)|666|667|668|669|670|(4:672|673|674|(1:678))(1:779)|679|(1:681)(1:772)|682|(1:771)(2:686|(3:688|(1:690)(1:766)|691)(3:767|(1:769)|770))|(1:693)(3:760|(1:764)|765)|(9:703|704|705|(1:707)(1:754)|708|709|710|(4:712|713|714|715)(1:749)|716)(1:695)|696|(3:698|(1:700)|701)(1:702))(13:791|792|670|(0)(0)|679|(0)(0)|682|(1:684)|771|(0)(0)|(0)(0)|696|(0)(0))))(1:803)|799|792|670|(0)(0)|679|(0)(0)|682|(0)|771|(0)(0)|(0)(0)|696|(0)(0))(1:806))|797|798|719|720|721|722|723|(1:745)(1:726)|727|728|729|730|731|732|733|734|735|570|571|(1:573)|575))|629|630)(1:825)|819|629|630)|631))|879|880|881|(0)(0)|884|885|(0)(0)|631)(1:877)|878|879|880|881|(0)(0)|884|885|(0)(0)|631)(4:983|984|985|986))(3:988|989|990))))|619|(0)(0)|631)))|608)|569|570|571|(0)|575)(1:1221)))|1149|1150|(0)|(0)(0))(1:558)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575)(1:1249))(1:1261)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575))(1:1293)|1291|522|523|524|525|526|528|529|530|531|532|533|534|(0)(0)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575)(6:1319|1320|1321|1322|1323|1324))|533|534|(0)(0)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575) */
    /* JADX WARN: Can't wrap try/catch for region: R(35:(2:496|497)(2:1308|(47:1310|1311|1312|1313|1314|499|(3:501|(1:1302)(1:505)|506)(1:1303)|507|(1:509)|510|511|(3:513|514|(39:516|(1:518)|519|520|521|522|523|524|525|526|528|529|530|531|532|533|534|(8:536|537|538|539|540|541|542|(23:544|545|546|548|549|550|551|552|553|(2:1232|1233)(1:555)|556|(7:1146|1147|(3:1223|1224|(4:1226|1150|(1:1152)|(11:(9:1192|1193|1194|1195|(1:1197)|1198|1199|(2:1201|1202)(2:1205|1206)|1203)(12:1155|1156|1157|1158|(3:1160|1161|1162)(2:1184|1185)|1163|1164|1165|1166|1167|1168|1169)|(1:561)(1:1145)|562|563|564|(2:(6:594|595|(1:1136)(4:598|599|600|601)|(5:1012|1013|1014|(5:1016|1017|1018|(4:1020|(1:1022)(1:1043)|1023|(1:1025)(1:1042))(1:1044)|1026)(5:1049|(2:1051|(1:(16:1054|1055|1056|(4:1104|1105|1106|(3:1108|1109|1110))(1:1058)|1059|1060|1061|1062|(1:1064)|1065|(1:1067)(2:1098|1099)|1068|(3:1075|1076|(9:1080|1081|1082|1083|(1:1085)|1086|1087|1088|1089))|1097|1088|1089))(3:1122|(1:1124)|1089))|1125|(0)|1089)|(3:1028|1029|(2:1031|1032)))(1:603)|604|(1:(11:609|610|(1:612)|613|614|615|616|(1:618)(2:829|(4:993|994|(1:996)|997)(2:831|(3:833|(1:865)(7:836|837|838|839|(3:841|842|(5:844|845|846|847|848))(1:859)|858|848)|849)(3:866|867|(4:869|870|(1:872)(1:987)|(12:874|875|(12:892|893|894|(5:(1:947)(3:899|900|901)|(3:905|(2:907|(2:908|(1:927)(3:910|(2:925|926)(2:916|(2:920|921))|923)))(0)|928)|929|930|(4:934|935|(1:937)|938))(2:948|(14:950|(3:954|(2:960|(2:962|963)(1:970))|971)|976|964|(1:967)|968|969|880|881|(1:883)(1:886)|884|885|(3:826|827|828)(5:621|(7:623|624|625|626|(1:628)(2:632|(23:634|(3:810|811|(1:813))(1:(20:637|(1:639)(1:805)|640|641|(1:804)(3:645|646|647)|648|(4:650|651|652|(6:654|655|656|657|658|(16:660|(3:785|786|787)(4:662|663|664|665)|666|667|668|669|670|(4:672|673|674|(1:678))(1:779)|679|(1:681)(1:772)|682|(1:771)(2:686|(3:688|(1:690)(1:766)|691)(3:767|(1:769)|770))|(1:693)(3:760|(1:764)|765)|(9:703|704|705|(1:707)(1:754)|708|709|710|(4:712|713|714|715)(1:749)|716)(1:695)|696|(3:698|(1:700)|701)(1:702))(13:791|792|670|(0)(0)|679|(0)(0)|682|(1:684)|771|(0)(0)|(0)(0)|696|(0)(0))))(1:803)|799|792|670|(0)(0)|679|(0)(0)|682|(0)|771|(0)(0)|(0)(0)|696|(0)(0))(1:806))|797|798|719|720|721|722|723|(1:745)(1:726)|727|728|729|730|731|732|733|734|735|570|571|(1:573)|575))|629|630)(1:825)|819|629|630)|631))|879|880|881|(0)(0)|884|885|(0)(0)|631)(1:877)|878|879|880|881|(0)(0)|884|885|(0)(0)|631)(4:983|984|985|986))(3:988|989|990))))|619|(0)(0)|631)))|608)|569|570|571|(0)|575)(1:1221)))|1149|1150|(0)|(0)(0))(1:558)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575)(1:1249))(1:1261)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575))(1:1293)|1291|522|523|524|525|526|528|529|530|531|532|533|534|(0)(0)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575)(6:1319|1320|1321|1322|1323|1324))|522|523|524|525|526|528|529|530|531|532|533|534|(0)(0)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575) */
    /* JADX WARN: Can't wrap try/catch for region: R(46:472|(14:473|474|475|(3:477|478|479)(2:1355|1356)|480|481|482|(3:484|(1:486)(2:1342|(1:1344)(1:1345))|487)(1:(1:1347)(1:1348))|488|(2:1335|1336)|490|(1:492)(1:1334)|493|494)|(2:496|497)(2:1308|(47:1310|1311|1312|1313|1314|499|(3:501|(1:1302)(1:505)|506)(1:1303)|507|(1:509)|510|511|(3:513|514|(39:516|(1:518)|519|520|521|522|523|524|525|526|528|529|530|531|532|533|534|(8:536|537|538|539|540|541|542|(23:544|545|546|548|549|550|551|552|553|(2:1232|1233)(1:555)|556|(7:1146|1147|(3:1223|1224|(4:1226|1150|(1:1152)|(11:(9:1192|1193|1194|1195|(1:1197)|1198|1199|(2:1201|1202)(2:1205|1206)|1203)(12:1155|1156|1157|1158|(3:1160|1161|1162)(2:1184|1185)|1163|1164|1165|1166|1167|1168|1169)|(1:561)(1:1145)|562|563|564|(2:(6:594|595|(1:1136)(4:598|599|600|601)|(5:1012|1013|1014|(5:1016|1017|1018|(4:1020|(1:1022)(1:1043)|1023|(1:1025)(1:1042))(1:1044)|1026)(5:1049|(2:1051|(1:(16:1054|1055|1056|(4:1104|1105|1106|(3:1108|1109|1110))(1:1058)|1059|1060|1061|1062|(1:1064)|1065|(1:1067)(2:1098|1099)|1068|(3:1075|1076|(9:1080|1081|1082|1083|(1:1085)|1086|1087|1088|1089))|1097|1088|1089))(3:1122|(1:1124)|1089))|1125|(0)|1089)|(3:1028|1029|(2:1031|1032)))(1:603)|604|(1:(11:609|610|(1:612)|613|614|615|616|(1:618)(2:829|(4:993|994|(1:996)|997)(2:831|(3:833|(1:865)(7:836|837|838|839|(3:841|842|(5:844|845|846|847|848))(1:859)|858|848)|849)(3:866|867|(4:869|870|(1:872)(1:987)|(12:874|875|(12:892|893|894|(5:(1:947)(3:899|900|901)|(3:905|(2:907|(2:908|(1:927)(3:910|(2:925|926)(2:916|(2:920|921))|923)))(0)|928)|929|930|(4:934|935|(1:937)|938))(2:948|(14:950|(3:954|(2:960|(2:962|963)(1:970))|971)|976|964|(1:967)|968|969|880|881|(1:883)(1:886)|884|885|(3:826|827|828)(5:621|(7:623|624|625|626|(1:628)(2:632|(23:634|(3:810|811|(1:813))(1:(20:637|(1:639)(1:805)|640|641|(1:804)(3:645|646|647)|648|(4:650|651|652|(6:654|655|656|657|658|(16:660|(3:785|786|787)(4:662|663|664|665)|666|667|668|669|670|(4:672|673|674|(1:678))(1:779)|679|(1:681)(1:772)|682|(1:771)(2:686|(3:688|(1:690)(1:766)|691)(3:767|(1:769)|770))|(1:693)(3:760|(1:764)|765)|(9:703|704|705|(1:707)(1:754)|708|709|710|(4:712|713|714|715)(1:749)|716)(1:695)|696|(3:698|(1:700)|701)(1:702))(13:791|792|670|(0)(0)|679|(0)(0)|682|(1:684)|771|(0)(0)|(0)(0)|696|(0)(0))))(1:803)|799|792|670|(0)(0)|679|(0)(0)|682|(0)|771|(0)(0)|(0)(0)|696|(0)(0))(1:806))|797|798|719|720|721|722|723|(1:745)(1:726)|727|728|729|730|731|732|733|734|735|570|571|(1:573)|575))|629|630)(1:825)|819|629|630)|631))|879|880|881|(0)(0)|884|885|(0)(0)|631)(1:877)|878|879|880|881|(0)(0)|884|885|(0)(0)|631)(4:983|984|985|986))(3:988|989|990))))|619|(0)(0)|631)))|608)|569|570|571|(0)|575)(1:1221)))|1149|1150|(0)|(0)(0))(1:558)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575)(1:1249))(1:1261)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575))(1:1293)|1291|522|523|524|525|526|528|529|530|531|532|533|534|(0)(0)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575)(6:1319|1320|1321|1322|1323|1324))|498|499|(0)(0)|507|(0)|510|511|(0)(0)|1291|522|523|524|525|526|528|529|530|531|532|533|534|(0)(0)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575) */
    /* JADX WARN: Can't wrap try/catch for region: R(59:472|473|474|475|(3:477|478|479)(2:1355|1356)|480|481|482|(3:484|(1:486)(2:1342|(1:1344)(1:1345))|487)(1:(1:1347)(1:1348))|488|(2:1335|1336)|490|(1:492)(1:1334)|493|494|(2:496|497)(2:1308|(47:1310|1311|1312|1313|1314|499|(3:501|(1:1302)(1:505)|506)(1:1303)|507|(1:509)|510|511|(3:513|514|(39:516|(1:518)|519|520|521|522|523|524|525|526|528|529|530|531|532|533|534|(8:536|537|538|539|540|541|542|(23:544|545|546|548|549|550|551|552|553|(2:1232|1233)(1:555)|556|(7:1146|1147|(3:1223|1224|(4:1226|1150|(1:1152)|(11:(9:1192|1193|1194|1195|(1:1197)|1198|1199|(2:1201|1202)(2:1205|1206)|1203)(12:1155|1156|1157|1158|(3:1160|1161|1162)(2:1184|1185)|1163|1164|1165|1166|1167|1168|1169)|(1:561)(1:1145)|562|563|564|(2:(6:594|595|(1:1136)(4:598|599|600|601)|(5:1012|1013|1014|(5:1016|1017|1018|(4:1020|(1:1022)(1:1043)|1023|(1:1025)(1:1042))(1:1044)|1026)(5:1049|(2:1051|(1:(16:1054|1055|1056|(4:1104|1105|1106|(3:1108|1109|1110))(1:1058)|1059|1060|1061|1062|(1:1064)|1065|(1:1067)(2:1098|1099)|1068|(3:1075|1076|(9:1080|1081|1082|1083|(1:1085)|1086|1087|1088|1089))|1097|1088|1089))(3:1122|(1:1124)|1089))|1125|(0)|1089)|(3:1028|1029|(2:1031|1032)))(1:603)|604|(1:(11:609|610|(1:612)|613|614|615|616|(1:618)(2:829|(4:993|994|(1:996)|997)(2:831|(3:833|(1:865)(7:836|837|838|839|(3:841|842|(5:844|845|846|847|848))(1:859)|858|848)|849)(3:866|867|(4:869|870|(1:872)(1:987)|(12:874|875|(12:892|893|894|(5:(1:947)(3:899|900|901)|(3:905|(2:907|(2:908|(1:927)(3:910|(2:925|926)(2:916|(2:920|921))|923)))(0)|928)|929|930|(4:934|935|(1:937)|938))(2:948|(14:950|(3:954|(2:960|(2:962|963)(1:970))|971)|976|964|(1:967)|968|969|880|881|(1:883)(1:886)|884|885|(3:826|827|828)(5:621|(7:623|624|625|626|(1:628)(2:632|(23:634|(3:810|811|(1:813))(1:(20:637|(1:639)(1:805)|640|641|(1:804)(3:645|646|647)|648|(4:650|651|652|(6:654|655|656|657|658|(16:660|(3:785|786|787)(4:662|663|664|665)|666|667|668|669|670|(4:672|673|674|(1:678))(1:779)|679|(1:681)(1:772)|682|(1:771)(2:686|(3:688|(1:690)(1:766)|691)(3:767|(1:769)|770))|(1:693)(3:760|(1:764)|765)|(9:703|704|705|(1:707)(1:754)|708|709|710|(4:712|713|714|715)(1:749)|716)(1:695)|696|(3:698|(1:700)|701)(1:702))(13:791|792|670|(0)(0)|679|(0)(0)|682|(1:684)|771|(0)(0)|(0)(0)|696|(0)(0))))(1:803)|799|792|670|(0)(0)|679|(0)(0)|682|(0)|771|(0)(0)|(0)(0)|696|(0)(0))(1:806))|797|798|719|720|721|722|723|(1:745)(1:726)|727|728|729|730|731|732|733|734|735|570|571|(1:573)|575))|629|630)(1:825)|819|629|630)|631))|879|880|881|(0)(0)|884|885|(0)(0)|631)(1:877)|878|879|880|881|(0)(0)|884|885|(0)(0)|631)(4:983|984|985|986))(3:988|989|990))))|619|(0)(0)|631)))|608)|569|570|571|(0)|575)(1:1221)))|1149|1150|(0)|(0)(0))(1:558)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575)(1:1249))(1:1261)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575))(1:1293)|1291|522|523|524|525|526|528|529|530|531|532|533|534|(0)(0)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575)(6:1319|1320|1321|1322|1323|1324))|498|499|(0)(0)|507|(0)|510|511|(0)(0)|1291|522|523|524|525|526|528|529|530|531|532|533|534|(0)(0)|1250|548|549|550|551|552|553|(0)(0)|556|(0)(0)|559|(0)(0)|562|563|564|(9:(0)|594|595|(0)|1136|(0)(0)|604|(12:(0)|609|610|(0)|613|614|615|616|(0)(0)|619|(0)(0)|631)|608)|569|570|571|(0)|575) */
    /* JADX WARN: Code restructure failed: missing block: B:1004:0x122d, code lost:
        r10 = r87;
        r93 = r11;
        r4 = r54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1005:0x124a, code lost:
        throw new java.lang.RuntimeException("unexpected result from decoder.dequeueOutputBuffer: " + r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:1020:0x12a7, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1021:0x12a8, code lost:
        r10 = r87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1022:0x12ac, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1023:0x12ad, code lost:
        r10 = r87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1053:0x1352, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1054:0x1353, code lost:
        r10 = r87;
        r5 = r88;
        r69 = r9;
        r23 = r14;
        r71 = r55;
        r72 = r92;
        r44 = r94;
        r1 = r0;
        r3 = r21;
        r13 = -5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1055:0x136c, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1056:0x136d, code lost:
        r10 = r87;
        r5 = r88;
        r23 = r14;
        r71 = r55;
        r72 = r92;
        r44 = r94;
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1057:0x1380, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1058:0x1381, code lost:
        r10 = r87;
        r5 = r88;
        r23 = r14;
        r71 = r55;
        r72 = r92;
        r44 = r94;
        r1 = r0;
        r8 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1059:0x1395, code lost:
        r3 = r21;
        r13 = -5;
        r10 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1062:0x13a0, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1063:0x13a1, code lost:
        r10 = r87;
        r5 = r88;
        r14 = r2;
        r71 = r55;
        r72 = r92;
        r44 = r94;
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1064:0x13b3, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1065:0x13b4, code lost:
        r10 = r87;
        r94 = r14;
        r71 = r30;
        r15 = r78;
        r14 = r2;
        r72 = r92;
        r44 = r94;
        r1 = r0;
        r54 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1066:0x13c5, code lost:
        r8 = r14;
        r3 = r21;
        r13 = -5;
        r23 = null;
        r10 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1067:0x13cb, code lost:
        r69 = null;
        r10 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1069:0x13d0, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1070:0x13d1, code lost:
        r10 = r87;
        r71 = r30;
        r15 = r78;
        r72 = r92;
        r44 = r14;
        r1 = r0;
        r54 = r4;
        r14 = r5;
        r3 = r21;
        r8 = null;
        r13 = -5;
        r23 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1071:0x13ea, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1072:0x13eb, code lost:
        r10 = r87;
        r71 = r30;
        r15 = r78;
        r72 = r92;
        r44 = r14;
        r1 = r0;
        r14 = r5;
        r3 = r21;
        r8 = null;
        r13 = -5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1078:0x1415, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1079:0x1416, code lost:
        r10 = r87;
        r71 = r30;
        r15 = r78;
        r72 = r92;
        r44 = r14;
        r1 = r0;
        r3 = r21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x087d, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x087e, code lost:
        r2 = r85;
        r72 = r92;
        r1 = r0;
        r7 = r3;
        r10 = r87;
        r44 = r14;
        r6 = false;
        r13 = -5;
        r15 = r78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x088d, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x088e, code lost:
        r72 = r92;
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01fc, code lost:
        r6 = r7;
        r13 = r8;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1001:0x1227  */
    /* JADX WARN: Removed duplicated region for block: B:1099:0x1484 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:1110:0x14d0 A[Catch: all -> 0x14e0, TRY_LEAVE, TryCatch #108 {all -> 0x14e0, blocks: (B:1108:0x14c7, B:1110:0x14d0), top: B:1289:0x14c7 }] */
    /* JADX WARN: Removed duplicated region for block: B:1124:0x1506  */
    /* JADX WARN: Removed duplicated region for block: B:1131:0x1531 A[Catch: all -> 0x1525, TryCatch #3 {all -> 0x1525, blocks: (B:1126:0x1521, B:1131:0x1531, B:1133:0x1536, B:1135:0x153e, B:1136:0x1541), top: B:1190:0x1521 }] */
    /* JADX WARN: Removed duplicated region for block: B:1133:0x1536 A[Catch: all -> 0x1525, TryCatch #3 {all -> 0x1525, blocks: (B:1126:0x1521, B:1131:0x1531, B:1133:0x1536, B:1135:0x153e, B:1136:0x1541), top: B:1190:0x1521 }] */
    /* JADX WARN: Removed duplicated region for block: B:1135:0x153e A[Catch: all -> 0x1525, TryCatch #3 {all -> 0x1525, blocks: (B:1126:0x1521, B:1131:0x1531, B:1133:0x1536, B:1135:0x153e, B:1136:0x1541), top: B:1190:0x1521 }] */
    /* JADX WARN: Removed duplicated region for block: B:1140:0x154c  */
    /* JADX WARN: Removed duplicated region for block: B:1158:0x15b9  */
    /* JADX WARN: Removed duplicated region for block: B:1167:0x15d7  */
    /* JADX WARN: Removed duplicated region for block: B:1169:0x1606  */
    /* JADX WARN: Removed duplicated region for block: B:1190:0x1521 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1205:0x0a0f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1207:0x0645 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1222:0x1553 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1279:0x15c0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1293:0x11d9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1324:0x0a30 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1354:0x0bd2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1389:0x045c A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1391:0x044d A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1412:0x102a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1414:0x100b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:207:0x043b  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x043d  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x05fe A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:311:0x065a A[Catch: all -> 0x0649, TryCatch #22 {all -> 0x0649, blocks: (B:307:0x0645, B:311:0x065a, B:313:0x065f, B:314:0x0665), top: B:1207:0x0645 }] */
    /* JADX WARN: Removed duplicated region for block: B:313:0x065f A[Catch: all -> 0x0649, TryCatch #22 {all -> 0x0649, blocks: (B:307:0x0645, B:311:0x065a, B:313:0x065f, B:314:0x0665), top: B:1207:0x0645 }] */
    /* JADX WARN: Removed duplicated region for block: B:362:0x0746  */
    /* JADX WARN: Removed duplicated region for block: B:426:0x0863  */
    /* JADX WARN: Removed duplicated region for block: B:439:0x08a3  */
    /* JADX WARN: Removed duplicated region for block: B:442:0x08ad A[Catch: all -> 0x087d, Exception -> 0x088d, TRY_ENTER, TRY_LEAVE, TryCatch #129 {Exception -> 0x088d, all -> 0x087d, blocks: (B:431:0x086e, B:442:0x08ad, B:446:0x08eb, B:432:0x0873), top: B:1362:0x0861 }] */
    /* JADX WARN: Removed duplicated region for block: B:446:0x08eb A[Catch: all -> 0x087d, Exception -> 0x088d, TRY_ENTER, TRY_LEAVE, TryCatch #129 {Exception -> 0x088d, all -> 0x087d, blocks: (B:431:0x086e, B:442:0x08ad, B:446:0x08eb, B:432:0x0873), top: B:1362:0x0861 }] */
    /* JADX WARN: Removed duplicated region for block: B:459:0x091d  */
    /* JADX WARN: Removed duplicated region for block: B:469:0x098b  */
    /* JADX WARN: Removed duplicated region for block: B:497:0x09f0  */
    /* JADX WARN: Removed duplicated region for block: B:509:0x0a2a  */
    /* JADX WARN: Removed duplicated region for block: B:523:0x0a6a  */
    /* JADX WARN: Removed duplicated region for block: B:525:0x0a6d  */
    /* JADX WARN: Removed duplicated region for block: B:575:0x0b23  */
    /* JADX WARN: Removed duplicated region for block: B:584:0x0b50  */
    /* JADX WARN: Removed duplicated region for block: B:587:0x0b5e  */
    /* JADX WARN: Removed duplicated region for block: B:588:0x0b60  */
    /* JADX WARN: Removed duplicated region for block: B:592:0x0b81 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:598:0x0ba1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:696:0x0d46  */
    /* JADX WARN: Removed duplicated region for block: B:714:0x0d86  */
    /* JADX WARN: Removed duplicated region for block: B:717:0x0da7 A[ADDED_TO_REGION, EDGE_INSN: B:717:0x0da7->B:1413:0x0daa ?: BREAK  ] */
    /* JADX WARN: Removed duplicated region for block: B:722:0x0dca  */
    /* JADX WARN: Removed duplicated region for block: B:727:0x0ddb  */
    /* JADX WARN: Removed duplicated region for block: B:729:0x0df3  */
    /* JADX WARN: Removed duplicated region for block: B:861:0x0ffc  */
    /* JADX WARN: Removed duplicated region for block: B:862:0x0ffe  */
    /* JADX WARN: Removed duplicated region for block: B:939:0x1158  */
    /* JADX WARN: Removed duplicated region for block: B:947:0x116d  */
    /* JADX WARN: Removed duplicated region for block: B:950:0x1175  */
    /* JADX WARN: Removed duplicated region for block: B:951:0x1179  */
    /* JADX WARN: Removed duplicated region for block: B:954:0x1180  */
    /* JADX WARN: Removed duplicated region for block: B:969:0x11c1  */
    /* JADX WARN: Removed duplicated region for block: B:970:0x11c4  */
    /* JADX WARN: Removed duplicated region for block: B:994:0x120c  */
    /* JADX WARN: Removed duplicated region for block: B:997:0x1218 A[Catch: all -> 0x124b, Exception -> 0x124d, TryCatch #24 {all -> 0x124b, blocks: (B:940:0x115a, B:942:0x1162, B:958:0x1188, B:960:0x118c, B:977:0x11d9, B:980:0x11e1, B:983:0x11e8, B:989:0x11f5, B:991:0x1200, B:995:0x1212, B:997:0x1218, B:999:0x121c, B:1000:0x1221, B:987:0x11ef, B:974:0x11ce, B:975:0x11d4, B:963:0x11af, B:965:0x11b9, B:1004:0x122d, B:1005:0x124a), top: B:1209:0x115a }] */
    /* JADX WARN: Type inference failed for: r14v49 */
    /* JADX WARN: Type inference failed for: r2v80 */
    /* JADX WARN: Type inference failed for: r2v81 */
    /* JADX WARN: Type inference failed for: r2v83 */
    /* JADX WARN: Type inference failed for: r44v103 */
    /* JADX WARN: Type inference failed for: r44v104 */
    /* JADX WARN: Type inference failed for: r44v166 */
    /* JADX WARN: Type inference failed for: r44v167 */
    /* JADX WARN: Type inference failed for: r44v168 */
    /* JADX WARN: Type inference failed for: r44v169 */
    /* JADX WARN: Type inference failed for: r4v124 */
    /* JADX WARN: Type inference failed for: r4v185 */
    /* JADX WARN: Type inference failed for: r4v186 */
    /* JADX WARN: Type inference failed for: r4v187 */
    /* JADX WARN: Type inference failed for: r4v193 */
    /* JADX WARN: Type inference failed for: r4v194 */
    /* JADX WARN: Type inference failed for: r4v195 */
    /* JADX WARN: Type inference failed for: r4v196 */
    /* JADX WARN: Type inference failed for: r4v197 */
    /* JADX WARN: Type inference failed for: r4v39 */
    /* JADX WARN: Type inference failed for: r4v41 */
    /* JADX WARN: Type inference failed for: r4v46, types: [java.nio.ByteBuffer] */
    /* JADX WARN: Type inference failed for: r4v47 */
    /* JADX WARN: Type inference failed for: r4v48 */
    /* JADX WARN: Type inference failed for: r4v49 */
    /* JADX WARN: Type inference failed for: r4v54 */
    /* JADX WARN: Type inference failed for: r4v55 */
    /* JADX WARN: Type inference failed for: r4v85 */
    /* JADX WARN: Type inference failed for: r4v86 */
    /* JADX WARN: Type inference failed for: r5v41, types: [android.media.MediaExtractor] */
    /* JADX WARN: Type inference failed for: r5v47, types: [org.telegram.messenger.video.MP4Builder] */
    /* JADX WARN: Type inference failed for: r8v34 */
    /* JADX WARN: Type inference failed for: r9v40, types: [org.telegram.messenger.video.InputSurface] */
    /* JADX WARN: Type inference failed for: r9v41 */
    /* JADX WARN: Type inference failed for: r9v53, types: [org.telegram.messenger.video.InputSurface] */
    /* JADX WARN: Type inference failed for: r9v55 */
    /* JADX WARN: Type inference failed for: r9v62, types: [org.telegram.messenger.video.InputSurface] */
    @TargetApi(18)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean convertVideoInternal(String str, File file, int i, boolean z, int i2, int i3, int i4, int i5, int i6, int i7, int i8, long j, long j2, long j3, long j4, boolean z2, boolean z3, MediaController.SavedFilterState savedFilterState, String str2, ArrayList<VideoEditedInfo.MediaEntity> arrayList, boolean z4, MediaController.CropState cropState, boolean z5) {
        int i9;
        int i10;
        int i11;
        long j5;
        long j6;
        Throwable th;
        int i12;
        int i13;
        boolean z6;
        int i14;
        int i15;
        MediaExtractor mediaExtractor;
        MP4Builder mP4Builder;
        int i16;
        int i17;
        int i18;
        boolean z7;
        int findTrack;
        boolean z8;
        boolean z9;
        int i19;
        int i20;
        int i21;
        int i22;
        boolean z10;
        OutputSurface outputSurface;
        MediaCodec mediaCodec;
        boolean z11;
        InputSurface inputSurface;
        AudioRecoder audioRecoder;
        int i23;
        boolean z12;
        int i24;
        int i25;
        int i26;
        long j7;
        Exception exc;
        int i27;
        MediaCodec mediaCodec2;
        MediaCodec mediaCodec3;
        OutputSurface outputSurface2;
        int i28;
        MediaCodec mediaCodec4;
        int i29;
        boolean z13;
        int i30;
        long j8;
        int i31;
        MediaFormat trackFormat;
        long j9;
        long j10;
        int i32;
        long j11;
        int i33;
        long j12;
        int i34;
        long j13;
        int i35;
        long j14;
        String str3;
        MediaController.CropState cropState2;
        int i36;
        int i37;
        MediaFormat createVideoFormat;
        int i38;
        int i39;
        long j15;
        MediaCodec createEncoderByType;
        MediaCodec createDecoderByType;
        long j16;
        int i40;
        OutputSurface outputSurface3;
        int i41;
        MediaCodec mediaCodec5;
        int i42;
        MediaCodec.BufferInfo bufferInfo;
        String str4;
        String str5;
        int i43;
        String str6;
        OutputSurface outputSurface4;
        int i44;
        boolean z14;
        Throwable th2;
        long j17;
        int i45;
        int i46;
        int i47;
        ByteBuffer[] inputBuffers;
        ByteBuffer[] outputBuffers;
        int i48;
        long j18;
        int i49;
        MediaFormat trackFormat2;
        boolean z15;
        ByteBuffer[] byteBufferArr;
        long j19;
        int i50;
        int i51;
        AudioRecoder audioRecoder2;
        int i52;
        int i53;
        ByteBuffer byteBuffer;
        boolean z16;
        AudioRecoder audioRecoder3;
        long j20;
        long j21;
        long j22;
        boolean z17;
        boolean z18;
        int i54;
        InputSurface inputSurface2;
        ByteBuffer[] byteBufferArr2;
        int i55;
        long j23;
        int i56;
        int i57;
        int i58;
        ByteBuffer[] byteBufferArr3;
        int i59;
        int i60;
        MediaCodec.BufferInfo bufferInfo2;
        long j24;
        boolean z19;
        long j25;
        int i61;
        boolean z20;
        int i62;
        int i63;
        MediaController.VideoConvertorListener videoConvertorListener;
        long j26;
        ByteBuffer byteBuffer2;
        boolean z21;
        int i64;
        long j27;
        boolean z22;
        long j28;
        int i65;
        int i66;
        int i67;
        long j29;
        int i68;
        long j30;
        int i69;
        int i70;
        boolean z23;
        int dequeueOutputBuffer;
        int i71;
        int i72;
        long j31;
        int i73;
        long j32;
        int i74;
        int i75;
        String str7;
        int i76;
        String str8;
        ByteBuffer outputBuffer;
        int i77;
        int i78;
        MediaCodec mediaCodec6;
        ByteBuffer byteBuffer3;
        ByteBuffer byteBuffer4;
        int i79;
        MediaController.VideoConvertorListener videoConvertorListener2;
        byte[] bArr;
        MediaCodec mediaCodec7;
        OutputSurface outputSurface5;
        boolean z24;
        String str9;
        int i80;
        int i81;
        MediaCodec mediaCodec8;
        String str10;
        long j33;
        OutputSurface outputSurface6;
        int i82;
        OutputSurface outputSurface7;
        MediaCodec.BufferInfo bufferInfo3;
        boolean z25;
        int i83;
        boolean z26;
        int i84;
        int i85;
        long j34;
        int i86;
        long j35;
        int dequeueOutputBuffer2;
        InputSurface inputSurface3;
        boolean z27;
        long j36;
        boolean z28;
        long j37;
        int i87;
        boolean z29;
        long j38;
        boolean z30;
        MediaCodec.BufferInfo bufferInfo4;
        long j39;
        boolean z31;
        InputSurface inputSurface4;
        int i88;
        int i89;
        int i90;
        int i91;
        MediaExtractor mediaExtractor2;
        MP4Builder mP4Builder2;
        MediaCodecVideoConvertor mediaCodecVideoConvertor;
        int i92;
        Exception exc2;
        int i93;
        int i94;
        int i95;
        int i96;
        int i97;
        int i98;
        OutputSurface outputSurface8;
        int i99;
        MediaCodec mediaCodec9;
        ?? r9;
        int i100;
        ByteBuffer[] byteBufferArr4;
        boolean z32;
        MediaFormat createVideoFormat2;
        MediaCodec createEncoderByType2;
        String str11;
        int i101;
        String str12;
        String str13;
        ByteBuffer[] outputBuffers2;
        boolean z33;
        int i102;
        boolean z34;
        boolean z35;
        int i103;
        long j40;
        long j41;
        int i104;
        boolean z36;
        boolean z37;
        boolean z38;
        int dequeueOutputBuffer3;
        int i105;
        boolean z39;
        boolean z40;
        String str14;
        String str15;
        ByteBuffer outputBuffer2;
        int i106;
        int i107;
        ByteBuffer[] byteBufferArr5;
        ByteBuffer byteBuffer5;
        ByteBuffer byteBuffer6;
        boolean z41;
        MediaController.VideoConvertorListener videoConvertorListener3;
        byte[] bArr2;
        int i108;
        boolean z42;
        ByteBuffer[] byteBufferArr6;
        boolean z43;
        int i109;
        boolean z44;
        ?? r92;
        boolean z45;
        ByteBuffer[] byteBufferArr7;
        ByteBuffer[] byteBufferArr8;
        MediaCodecVideoConvertor mediaCodecVideoConvertor2 = this;
        int i110 = i5;
        int i111 = i7;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            MediaCodec.BufferInfo bufferInfo5 = new MediaCodec.BufferInfo();
            Mp4Movie mp4Movie = new Mp4Movie();
            mp4Movie.setCacheFile(file);
            mp4Movie.setRotation(0);
            mp4Movie.setSize(i4, i110);
            mediaCodecVideoConvertor2.mediaMuxer = new MP4Builder().createMovie(mp4Movie, z);
            float f = ((float) j4) / 1000.0f;
            mediaCodecVideoConvertor2.endPresentationTime = j4 * 1000;
            checkConversionCanceled();
            if (z4) {
                if (j3 >= 0) {
                    i111 = f <= 2000.0f ? 2600000 : f <= 5000.0f ? 2200000 : 1560000;
                } else if (i111 <= 0) {
                    i111 = 921600;
                }
                try {
                    if (i4 % 16 != 0) {
                        try {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("changing width from " + i4 + " to " + (Math.round(i4 / 16.0f) * 16));
                            }
                            i93 = Math.round(i4 / 16.0f) * 16;
                        } catch (Exception e) {
                            mediaCodecVideoConvertor = this;
                            exc2 = e;
                            i92 = i111;
                            i93 = i4;
                            i12 = i110;
                            i95 = -5;
                            mediaCodec9 = null;
                            outputSurface8 = null;
                            byteBufferArr4 = null;
                            try {
                                if (exc2 instanceof IllegalStateException) {
                                }
                                try {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("bitrate: ");
                                    i100 = i92;
                                    try {
                                        sb.append(i100);
                                        sb.append(" framerate: ");
                                        i99 = i6;
                                        try {
                                            sb.append(i99);
                                            sb.append(" size: ");
                                            sb.append(i12);
                                            sb.append("x");
                                            sb.append(i93);
                                            FileLog.e(sb.toString());
                                            FileLog.e(exc2);
                                            i98 = i95;
                                            i97 = i93;
                                            z6 = z32;
                                            r9 = byteBufferArr4;
                                            z12 = true;
                                            if (outputSurface8 != null) {
                                            }
                                            if (r9 != 0) {
                                            }
                                            if (mediaCodec9 != null) {
                                            }
                                            checkConversionCanceled();
                                            j5 = j2;
                                            j6 = j3;
                                            i22 = i100;
                                            i19 = i97;
                                            i21 = i12;
                                            mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                            i14 = i98;
                                            mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                            if (mediaExtractor2 != null) {
                                            }
                                            mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                            if (mP4Builder2 != null) {
                                            }
                                            i17 = i21;
                                            i16 = i19;
                                            i18 = i22;
                                            z7 = z12;
                                        } catch (Throwable th3) {
                                            j5 = j2;
                                            j6 = j3;
                                            th = th3;
                                            i111 = i100;
                                            i13 = i93;
                                            i96 = i99;
                                            z6 = z32;
                                            mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                            i14 = i95;
                                            i15 = i96;
                                            try {
                                                FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                FileLog.e(th);
                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                if (mediaExtractor != null) {
                                                }
                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                if (mP4Builder != null) {
                                                }
                                                i16 = i13;
                                                i17 = i12;
                                                i18 = i111;
                                                z7 = true;
                                                if (z6) {
                                                }
                                            } finally {
                                                MediaExtractor mediaExtractor3 = this.extractor;
                                                if (mediaExtractor3 != null) {
                                                    mediaExtractor3.release();
                                                }
                                                MP4Builder mP4Builder3 = this.mediaMuxer;
                                                if (mP4Builder3 != null) {
                                                    try {
                                                        mP4Builder3.finishMovie();
                                                        this.endPresentationTime = this.mediaMuxer.getLastFrameTimestamp(i14);
                                                    } catch (Throwable th4) {
                                                        FileLog.e(th4);
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Throwable th5) {
                                        th = th5;
                                        i96 = i6;
                                        j5 = j2;
                                        j6 = j3;
                                        th = th;
                                        i111 = i100;
                                        i13 = i93;
                                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                        z6 = z32;
                                        i14 = i95;
                                        i15 = i96;
                                        FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                        FileLog.e(th);
                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                        if (mediaExtractor != null) {
                                        }
                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                        if (mP4Builder != null) {
                                        }
                                        i16 = i13;
                                        i17 = i12;
                                        i18 = i111;
                                        z7 = true;
                                        if (z6) {
                                        }
                                    }
                                } catch (Throwable th6) {
                                    th = th6;
                                    i100 = i92;
                                }
                            } catch (Throwable th7) {
                                i94 = i6;
                                j5 = j2;
                                j6 = j3;
                                th = th7;
                                i111 = i92;
                                i13 = i93;
                                mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                z6 = false;
                                i96 = i94;
                                i14 = i95;
                                i15 = i96;
                                FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                FileLog.e(th);
                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                if (mediaExtractor != null) {
                                }
                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                if (mP4Builder != null) {
                                }
                                i16 = i13;
                                i17 = i12;
                                i18 = i111;
                                z7 = true;
                                if (z6) {
                                }
                            }
                            if (z6) {
                            }
                        } catch (Throwable th8) {
                            mediaCodecVideoConvertor2 = this;
                            j5 = j2;
                            j6 = j3;
                            th = th8;
                            i13 = i4;
                            i12 = i110;
                            z6 = false;
                            i14 = -5;
                            i15 = i6;
                            FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                            FileLog.e(th);
                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                            if (mediaExtractor != null) {
                            }
                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                            if (mP4Builder != null) {
                            }
                            i16 = i13;
                            i17 = i12;
                            i18 = i111;
                            z7 = true;
                            if (z6) {
                            }
                        }
                    } else {
                        i93 = i4;
                    }
                } catch (Exception e2) {
                    mediaCodecVideoConvertor = this;
                    i92 = i111;
                    exc2 = e2;
                } catch (Throwable th9) {
                    mediaCodecVideoConvertor2 = this;
                    j5 = j2;
                    j6 = j3;
                    th = th9;
                    i13 = i4;
                    i12 = i110;
                }
                try {
                    if (i110 % 16 != 0) {
                        try {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("changing height from " + i110 + " to " + (Math.round(i110 / 16.0f) * 16));
                            }
                            i110 = Math.round(i110 / 16.0f) * 16;
                        } catch (Exception e3) {
                            mediaCodecVideoConvertor = this;
                            exc2 = e3;
                            i92 = i111;
                            i12 = i110;
                            i95 = -5;
                            mediaCodec9 = null;
                            outputSurface8 = null;
                            byteBufferArr4 = null;
                            if (exc2 instanceof IllegalStateException) {
                            }
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("bitrate: ");
                            i100 = i92;
                            sb2.append(i100);
                            sb2.append(" framerate: ");
                            i99 = i6;
                            sb2.append(i99);
                            sb2.append(" size: ");
                            sb2.append(i12);
                            sb2.append("x");
                            sb2.append(i93);
                            FileLog.e(sb2.toString());
                            FileLog.e(exc2);
                            i98 = i95;
                            i97 = i93;
                            z6 = z32;
                            r9 = byteBufferArr4;
                            z12 = true;
                            if (outputSurface8 != null) {
                            }
                            if (r9 != 0) {
                            }
                            if (mediaCodec9 != null) {
                            }
                            checkConversionCanceled();
                            j5 = j2;
                            j6 = j3;
                            i22 = i100;
                            i19 = i97;
                            i21 = i12;
                            mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                            i14 = i98;
                            mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                            if (mediaExtractor2 != null) {
                            }
                            mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                            if (mP4Builder2 != null) {
                            }
                            i17 = i21;
                            i16 = i19;
                            i18 = i22;
                            z7 = z12;
                            if (z6) {
                            }
                        } catch (Throwable th10) {
                            mediaCodecVideoConvertor2 = this;
                            j5 = j2;
                            j6 = j3;
                            th = th10;
                            i12 = i110;
                            i13 = i93;
                            z6 = false;
                            i14 = -5;
                            i15 = i6;
                            FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                            FileLog.e(th);
                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                            if (mediaExtractor != null) {
                            }
                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                            if (mP4Builder != null) {
                            }
                            i16 = i13;
                            i17 = i12;
                            i18 = i111;
                            z7 = true;
                            if (z6) {
                            }
                        }
                    }
                    try {
                        try {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("create photo encoder " + i93 + " " + i110 + " duration = " + j4);
                            }
                            createVideoFormat2 = MediaFormat.createVideoFormat(MediaController.VIDEO_MIME_TYPE, i93, i110);
                            createVideoFormat2.setInteger("color-format", 2130708361);
                            createVideoFormat2.setInteger("bitrate", i111);
                            createVideoFormat2.setInteger("frame-rate", 30);
                            createVideoFormat2.setInteger("i-frame-interval", 1);
                            createEncoderByType2 = MediaCodec.createEncoderByType(MediaController.VIDEO_MIME_TYPE);
                        } catch (Exception e4) {
                            mediaCodecVideoConvertor = this;
                            i92 = i111;
                            i12 = i110;
                            exc2 = e4;
                            i95 = -5;
                            mediaCodec9 = null;
                            outputSurface8 = null;
                            byteBufferArr4 = null;
                            if (exc2 instanceof IllegalStateException) {
                            }
                            StringBuilder sb22 = new StringBuilder();
                            sb22.append("bitrate: ");
                            i100 = i92;
                            sb22.append(i100);
                            sb22.append(" framerate: ");
                            i99 = i6;
                            sb22.append(i99);
                            sb22.append(" size: ");
                            sb22.append(i12);
                            sb22.append("x");
                            sb22.append(i93);
                            FileLog.e(sb22.toString());
                            FileLog.e(exc2);
                            i98 = i95;
                            i97 = i93;
                            z6 = z32;
                            r9 = byteBufferArr4;
                            z12 = true;
                            if (outputSurface8 != null) {
                            }
                            if (r9 != 0) {
                            }
                            if (mediaCodec9 != null) {
                            }
                            checkConversionCanceled();
                            j5 = j2;
                            j6 = j3;
                            i22 = i100;
                            i19 = i97;
                            i21 = i12;
                            mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                            i14 = i98;
                            mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                            if (mediaExtractor2 != null) {
                            }
                            mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                            if (mP4Builder2 != null) {
                            }
                            i17 = i21;
                            i16 = i19;
                            i18 = i22;
                            z7 = z12;
                            if (z6) {
                            }
                        }
                    } catch (Throwable th11) {
                        i12 = i110;
                        i97 = i93;
                        mediaCodecVideoConvertor2 = this;
                        i11 = i6;
                        j5 = j2;
                        j6 = j3;
                        th = th11;
                    }
                    try {
                        createEncoderByType2.configure(createVideoFormat2, (Surface) null, (MediaCrypto) null, 1);
                        ?? inputSurface5 = new InputSurface(createEncoderByType2.createInputSurface());
                        try {
                            inputSurface5.makeCurrent();
                            createEncoderByType2.start();
                            float f2 = i6;
                            str11 = "csd-0";
                            i92 = i111;
                            int i112 = i93;
                            int i113 = i110;
                            byteBufferArr4 = inputSurface5;
                            i101 = i110;
                            i97 = i93;
                            str12 = MediaController.VIDEO_MIME_TYPE;
                            str13 = "prepend-sps-pps-to-idr-frames";
                            try {
                                try {
                                    outputSurface8 = new OutputSurface(savedFilterState, str, str2, arrayList, null, i112, i113, i2, i3, i, f2, true);
                                    try {
                                        if (Build.VERSION.SDK_INT < 21) {
                                            try {
                                                outputBuffers2 = createEncoderByType2.getOutputBuffers();
                                            } catch (Exception e5) {
                                                mediaCodecVideoConvertor = this;
                                                exc2 = e5;
                                                mediaCodec9 = createEncoderByType2;
                                                i12 = i101;
                                                i93 = i97;
                                                i95 = -5;
                                            } catch (Throwable th12) {
                                                mediaCodecVideoConvertor2 = this;
                                                i11 = i6;
                                                j5 = j2;
                                                j6 = j3;
                                                th = th12;
                                                i111 = i92;
                                                i12 = i101;
                                                i13 = i97;
                                                z6 = false;
                                                i14 = -5;
                                                i15 = i11;
                                                FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                FileLog.e(th);
                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                if (mediaExtractor != null) {
                                                }
                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                if (mP4Builder != null) {
                                                }
                                                i16 = i13;
                                                i17 = i12;
                                                i18 = i111;
                                                z7 = true;
                                                if (z6) {
                                                }
                                            }
                                        } else {
                                            outputBuffers2 = null;
                                        }
                                        checkConversionCanceled();
                                        z33 = false;
                                        i12 = 0;
                                        i102 = 0;
                                        z34 = true;
                                        z35 = false;
                                        i14 = -5;
                                    } catch (Exception e6) {
                                        e = e6;
                                        mediaCodecVideoConvertor = this;
                                        i12 = i101;
                                        mediaCodec9 = createEncoderByType2;
                                        i93 = i97;
                                        i95 = -5;
                                    }
                                } catch (Throwable th13) {
                                    i12 = i101;
                                    mediaCodecVideoConvertor2 = this;
                                    i11 = i6;
                                    j5 = j2;
                                    j6 = j3;
                                    th = th13;
                                    i111 = i92;
                                }
                            } catch (Exception e7) {
                                e = e7;
                                mediaCodecVideoConvertor = this;
                                i12 = i101;
                                mediaCodec9 = createEncoderByType2;
                                i93 = i97;
                                i95 = -5;
                                outputSurface8 = null;
                                exc2 = e;
                                if (exc2 instanceof IllegalStateException) {
                                }
                                StringBuilder sb222 = new StringBuilder();
                                sb222.append("bitrate: ");
                                i100 = i92;
                                sb222.append(i100);
                                sb222.append(" framerate: ");
                                i99 = i6;
                                sb222.append(i99);
                                sb222.append(" size: ");
                                sb222.append(i12);
                                sb222.append("x");
                                sb222.append(i93);
                                FileLog.e(sb222.toString());
                                FileLog.e(exc2);
                                i98 = i95;
                                i97 = i93;
                                z6 = z32;
                                r9 = byteBufferArr4;
                                z12 = true;
                                if (outputSurface8 != null) {
                                }
                                if (r9 != 0) {
                                }
                                if (mediaCodec9 != null) {
                                }
                                checkConversionCanceled();
                                j5 = j2;
                                j6 = j3;
                                i22 = i100;
                                i19 = i97;
                                i21 = i12;
                                mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                i14 = i98;
                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                if (mediaExtractor2 != null) {
                                }
                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                if (mP4Builder2 != null) {
                                }
                                i17 = i21;
                                i16 = i19;
                                i18 = i22;
                                z7 = z12;
                                if (z6) {
                                }
                            }
                        } catch (Exception e8) {
                            e = e8;
                            mediaCodecVideoConvertor = this;
                            i92 = i111;
                            i12 = i110;
                            byteBufferArr4 = inputSurface5;
                            mediaCodec9 = createEncoderByType2;
                        }
                    } catch (Exception e9) {
                        mediaCodecVideoConvertor = this;
                        i92 = i111;
                        i12 = i110;
                        exc2 = e9;
                        mediaCodec9 = createEncoderByType2;
                        i95 = -5;
                        outputSurface8 = null;
                        byteBufferArr4 = null;
                        if (exc2 instanceof IllegalStateException) {
                        }
                        StringBuilder sb2222 = new StringBuilder();
                        sb2222.append("bitrate: ");
                        i100 = i92;
                        sb2222.append(i100);
                        sb2222.append(" framerate: ");
                        i99 = i6;
                        sb2222.append(i99);
                        sb2222.append(" size: ");
                        sb2222.append(i12);
                        sb2222.append("x");
                        sb2222.append(i93);
                        FileLog.e(sb2222.toString());
                        FileLog.e(exc2);
                        i98 = i95;
                        i97 = i93;
                        z6 = z32;
                        r9 = byteBufferArr4;
                        z12 = true;
                        if (outputSurface8 != null) {
                        }
                        if (r9 != 0) {
                        }
                        if (mediaCodec9 != null) {
                        }
                        checkConversionCanceled();
                        j5 = j2;
                        j6 = j3;
                        i22 = i100;
                        i19 = i97;
                        i21 = i12;
                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                        i14 = i98;
                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                        if (mediaExtractor2 != null) {
                        }
                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                        if (mP4Builder2 != null) {
                        }
                        i17 = i21;
                        i16 = i19;
                        i18 = i22;
                        z7 = z12;
                        if (z6) {
                        }
                    }
                } catch (Exception e10) {
                    mediaCodecVideoConvertor = this;
                    i92 = i111;
                    exc2 = e10;
                } catch (Throwable th14) {
                    mediaCodecVideoConvertor2 = this;
                    j5 = j2;
                    j6 = j3;
                    th = th14;
                    i12 = i110;
                    i13 = i93;
                    z6 = false;
                    i14 = -5;
                    i15 = i6;
                    FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                    FileLog.e(th);
                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                    if (mediaExtractor != null) {
                    }
                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                    if (mP4Builder != null) {
                    }
                    i16 = i13;
                    i17 = i12;
                    i18 = i111;
                    z7 = true;
                    if (z6) {
                    }
                }
                while (!z35) {
                    try {
                        checkConversionCanceled();
                        i95 = i14;
                        z36 = true;
                        z37 = z35;
                        z38 = !z33;
                    } catch (Exception e11) {
                        e = e11;
                        int i114 = i14;
                        i12 = i101;
                        mediaCodecVideoConvertor = this;
                        mediaCodec9 = createEncoderByType2;
                        i95 = i114;
                    } catch (Throwable th15) {
                        i12 = i101;
                        mediaCodecVideoConvertor2 = this;
                        i103 = i6;
                        j5 = j2;
                        j40 = j3;
                        th = th15;
                    }
                    while (true) {
                        if (z38 || z36) {
                            try {
                                checkConversionCanceled();
                                dequeueOutputBuffer3 = createEncoderByType2.dequeueOutputBuffer(bufferInfo5, z3 ? 22000L : 2500L);
                            } catch (Exception e12) {
                                e = e12;
                                mediaCodecVideoConvertor = this;
                            } catch (Throwable th16) {
                                i12 = i101;
                                mediaCodecVideoConvertor2 = this;
                                i103 = i6;
                                j5 = j2;
                                j40 = j3;
                                th = th16;
                                i14 = i95;
                                i111 = i92;
                                i104 = i103;
                                j41 = j40;
                                i13 = i97;
                                i91 = i104;
                                j6 = j41;
                                z6 = false;
                                i15 = i91;
                                FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                FileLog.e(th);
                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                if (mediaExtractor != null) {
                                }
                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                if (mP4Builder != null) {
                                }
                                i16 = i13;
                                i17 = i12;
                                i18 = i111;
                                z7 = true;
                                if (z6) {
                                }
                            }
                            if (dequeueOutputBuffer3 == -1) {
                                z40 = false;
                                mediaCodecVideoConvertor = this;
                                i105 = i12;
                                z39 = z38;
                                str14 = str11;
                            } else {
                                if (dequeueOutputBuffer3 == -3) {
                                    try {
                                        if (Build.VERSION.SDK_INT < 21) {
                                            outputBuffers2 = createEncoderByType2.getOutputBuffers();
                                        }
                                        i105 = i12;
                                        z39 = z38;
                                        z40 = z36;
                                        str14 = str11;
                                        i12 = i101;
                                        str15 = str12;
                                        mediaCodecVideoConvertor = this;
                                        z42 = z34;
                                        byteBufferArr6 = outputBuffers2;
                                        i108 = -1;
                                        if (dequeueOutputBuffer3 == i108) {
                                            i101 = i12;
                                            outputBuffers2 = byteBufferArr6;
                                            z34 = z42;
                                            z36 = z40;
                                            str12 = str15;
                                            str11 = str14;
                                            i12 = i105;
                                            z38 = z39;
                                        } else {
                                            if (z33) {
                                                byteBufferArr8 = byteBufferArr4;
                                            } else {
                                                try {
                                                    try {
                                                        outputSurface8.drawImage();
                                                        z44 = z33;
                                                        r92 = byteBufferArr4;
                                                    } catch (Exception e13) {
                                                        e = e13;
                                                        exc2 = e;
                                                        mediaCodec9 = createEncoderByType2;
                                                        i93 = i97;
                                                        if (exc2 instanceof IllegalStateException) {
                                                        }
                                                        StringBuilder sb22222 = new StringBuilder();
                                                        sb22222.append("bitrate: ");
                                                        i100 = i92;
                                                        sb22222.append(i100);
                                                        sb22222.append(" framerate: ");
                                                        i99 = i6;
                                                        sb22222.append(i99);
                                                        sb22222.append(" size: ");
                                                        sb22222.append(i12);
                                                        sb22222.append("x");
                                                        sb22222.append(i93);
                                                        FileLog.e(sb22222.toString());
                                                        FileLog.e(exc2);
                                                        i98 = i95;
                                                        i97 = i93;
                                                        z6 = z32;
                                                        r9 = byteBufferArr4;
                                                        z12 = true;
                                                        if (outputSurface8 != null) {
                                                        }
                                                        if (r9 != 0) {
                                                        }
                                                        if (mediaCodec9 != null) {
                                                        }
                                                        checkConversionCanceled();
                                                        j5 = j2;
                                                        j6 = j3;
                                                        i22 = i100;
                                                        i19 = i97;
                                                        i21 = i12;
                                                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                        i14 = i98;
                                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                        if (mediaExtractor2 != null) {
                                                        }
                                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                        if (mP4Builder2 != null) {
                                                        }
                                                        i17 = i21;
                                                        i16 = i19;
                                                        i18 = i22;
                                                        z7 = z12;
                                                        if (z6) {
                                                        }
                                                    }
                                                    try {
                                                        r92.setPresentationTime((i102 / 30.0f) * 1000.0f * 1000.0f * 1000.0f);
                                                        r92.swapBuffers();
                                                        i102++;
                                                        if (i102 >= 30.0f * f) {
                                                            createEncoderByType2.signalEndOfInputStream();
                                                            z45 = false;
                                                            z33 = true;
                                                            byteBufferArr7 = r92;
                                                            i101 = i12;
                                                            byteBufferArr4 = byteBufferArr7;
                                                            z36 = z40;
                                                            str12 = str15;
                                                            str11 = str14;
                                                            i12 = i105;
                                                            boolean z46 = z42;
                                                            z38 = z45;
                                                            outputBuffers2 = byteBufferArr6;
                                                            z34 = z46;
                                                        } else {
                                                            z33 = z44;
                                                            byteBufferArr8 = r92;
                                                        }
                                                    } catch (Exception e14) {
                                                        exc2 = e14;
                                                        byteBufferArr4 = r92;
                                                        mediaCodec9 = createEncoderByType2;
                                                        i93 = i97;
                                                        if (exc2 instanceof IllegalStateException) {
                                                        }
                                                        StringBuilder sb222222 = new StringBuilder();
                                                        sb222222.append("bitrate: ");
                                                        i100 = i92;
                                                        sb222222.append(i100);
                                                        sb222222.append(" framerate: ");
                                                        i99 = i6;
                                                        sb222222.append(i99);
                                                        sb222222.append(" size: ");
                                                        sb222222.append(i12);
                                                        sb222222.append("x");
                                                        sb222222.append(i93);
                                                        FileLog.e(sb222222.toString());
                                                        FileLog.e(exc2);
                                                        i98 = i95;
                                                        i97 = i93;
                                                        z6 = z32;
                                                        r9 = byteBufferArr4;
                                                        z12 = true;
                                                        if (outputSurface8 != null) {
                                                        }
                                                        if (r9 != 0) {
                                                        }
                                                        if (mediaCodec9 != null) {
                                                        }
                                                        checkConversionCanceled();
                                                        j5 = j2;
                                                        j6 = j3;
                                                        i22 = i100;
                                                        i19 = i97;
                                                        i21 = i12;
                                                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                        i14 = i98;
                                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                        if (mediaExtractor2 != null) {
                                                        }
                                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                        if (mP4Builder2 != null) {
                                                        }
                                                        i17 = i21;
                                                        i16 = i19;
                                                        i18 = i22;
                                                        z7 = z12;
                                                        if (z6) {
                                                        }
                                                    }
                                                } catch (Throwable th17) {
                                                    th = th17;
                                                    i109 = i6;
                                                    j5 = j2;
                                                    j6 = j3;
                                                    th = th;
                                                    mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                    i111 = i92;
                                                    i13 = i97;
                                                    i94 = i109;
                                                    z6 = false;
                                                    i96 = i94;
                                                    i14 = i95;
                                                    i15 = i96;
                                                    FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                    FileLog.e(th);
                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                    if (mediaExtractor != null) {
                                                    }
                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                    if (mP4Builder != null) {
                                                    }
                                                    i16 = i13;
                                                    i17 = i12;
                                                    i18 = i111;
                                                    z7 = true;
                                                    if (z6) {
                                                    }
                                                }
                                            }
                                            z45 = z39;
                                            byteBufferArr7 = byteBufferArr8;
                                            i101 = i12;
                                            byteBufferArr4 = byteBufferArr7;
                                            z36 = z40;
                                            str12 = str15;
                                            str11 = str14;
                                            i12 = i105;
                                            boolean z462 = z42;
                                            z38 = z45;
                                            outputBuffers2 = byteBufferArr6;
                                            z34 = z462;
                                        }
                                    } catch (Exception e15) {
                                        e = e15;
                                        mediaCodecVideoConvertor = this;
                                    } catch (Throwable th18) {
                                        mediaCodecVideoConvertor2 = this;
                                        i104 = i6;
                                        j5 = j2;
                                        j41 = j3;
                                        th = th18;
                                        i14 = i95;
                                        i111 = i92;
                                        i12 = i101;
                                        i13 = i97;
                                        i91 = i104;
                                        j6 = j41;
                                        z6 = false;
                                        i15 = i91;
                                        FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                        FileLog.e(th);
                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                        if (mediaExtractor != null) {
                                        }
                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                        if (mP4Builder != null) {
                                        }
                                        i16 = i13;
                                        i17 = i12;
                                        i18 = i111;
                                        z7 = true;
                                        if (z6) {
                                        }
                                    }
                                } else if (dequeueOutputBuffer3 == -2) {
                                    MediaFormat outputFormat = createEncoderByType2.getOutputFormat();
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("photo encoder new format " + outputFormat);
                                    }
                                    if (i95 != -5 || outputFormat == null) {
                                        z43 = z36;
                                        str14 = str11;
                                        mediaCodecVideoConvertor = this;
                                    } else {
                                        z43 = z36;
                                        mediaCodecVideoConvertor = this;
                                        try {
                                            i95 = mediaCodecVideoConvertor.mediaMuxer.addTrack(outputFormat, false);
                                            String str16 = str13;
                                            if (outputFormat.containsKey(str16)) {
                                                str13 = str16;
                                                if (outputFormat.getInteger(str16) == 1) {
                                                    str14 = str11;
                                                    i12 = outputFormat.getByteBuffer(str14).limit() + outputFormat.getByteBuffer("csd-1").limit();
                                                }
                                            } else {
                                                str13 = str16;
                                            }
                                            str14 = str11;
                                        } catch (Exception e16) {
                                            e = e16;
                                        } catch (Throwable th19) {
                                            i109 = i6;
                                            j5 = j2;
                                            j6 = j3;
                                            th = th19;
                                            mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                            i111 = i92;
                                            i12 = i101;
                                            i13 = i97;
                                            i94 = i109;
                                            z6 = false;
                                            i96 = i94;
                                            i14 = i95;
                                            i15 = i96;
                                            FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                            FileLog.e(th);
                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                            if (mediaExtractor != null) {
                                            }
                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                            if (mP4Builder != null) {
                                            }
                                            i16 = i13;
                                            i17 = i12;
                                            i18 = i111;
                                            z7 = true;
                                            if (z6) {
                                            }
                                        }
                                    }
                                    z40 = z43;
                                    i105 = i12;
                                    z39 = z38;
                                } else {
                                    boolean z47 = z36;
                                    str14 = str11;
                                    mediaCodecVideoConvertor = this;
                                    try {
                                        if (dequeueOutputBuffer3 < 0) {
                                            throw new RuntimeException("unexpected result from encoder.dequeueOutputBuffer: " + dequeueOutputBuffer3);
                                        }
                                        try {
                                            try {
                                                if (Build.VERSION.SDK_INT < 21) {
                                                    outputBuffer2 = outputBuffers2[dequeueOutputBuffer3];
                                                } else {
                                                    outputBuffer2 = createEncoderByType2.getOutputBuffer(dequeueOutputBuffer3);
                                                }
                                                if (outputBuffer2 == null) {
                                                    throw new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer3 + " was null");
                                                }
                                                try {
                                                    i106 = bufferInfo5.size;
                                                } catch (Exception e17) {
                                                    e = e17;
                                                    i12 = i101;
                                                    exc2 = e;
                                                    mediaCodec9 = createEncoderByType2;
                                                    i93 = i97;
                                                    if (exc2 instanceof IllegalStateException) {
                                                    }
                                                    StringBuilder sb2222222 = new StringBuilder();
                                                    sb2222222.append("bitrate: ");
                                                    i100 = i92;
                                                    sb2222222.append(i100);
                                                    sb2222222.append(" framerate: ");
                                                    i99 = i6;
                                                    sb2222222.append(i99);
                                                    sb2222222.append(" size: ");
                                                    sb2222222.append(i12);
                                                    sb2222222.append("x");
                                                    sb2222222.append(i93);
                                                    FileLog.e(sb2222222.toString());
                                                    FileLog.e(exc2);
                                                    i98 = i95;
                                                    i97 = i93;
                                                    z6 = z32;
                                                    r9 = byteBufferArr4;
                                                    z12 = true;
                                                    if (outputSurface8 != null) {
                                                    }
                                                    if (r9 != 0) {
                                                    }
                                                    if (mediaCodec9 != null) {
                                                    }
                                                    checkConversionCanceled();
                                                    j5 = j2;
                                                    j6 = j3;
                                                    i22 = i100;
                                                    i19 = i97;
                                                    i21 = i12;
                                                    mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                    i14 = i98;
                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                    if (mediaExtractor2 != null) {
                                                    }
                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                    if (mP4Builder2 != null) {
                                                    }
                                                    i17 = i21;
                                                    i16 = i19;
                                                    i18 = i22;
                                                    z7 = z12;
                                                    if (z6) {
                                                    }
                                                }
                                                try {
                                                    if (i106 > 1) {
                                                        try {
                                                            int i115 = bufferInfo5.flags;
                                                            if ((i115 & 2) == 0) {
                                                                if (i12 == 0 || (i115 & 1) == 0) {
                                                                    byteBufferArr5 = outputBuffers2;
                                                                } else {
                                                                    byteBufferArr5 = outputBuffers2;
                                                                    bufferInfo5.offset += i12;
                                                                    bufferInfo5.size = i106 - i12;
                                                                }
                                                                if (z34 && (i115 & 1) != 0) {
                                                                    if (bufferInfo5.size > 100) {
                                                                        outputBuffer2.position(bufferInfo5.offset);
                                                                        byte[] bArr3 = new byte[100];
                                                                        outputBuffer2.get(bArr3);
                                                                        int i116 = 0;
                                                                        int i117 = 0;
                                                                        while (true) {
                                                                            if (i116 >= 96) {
                                                                                break;
                                                                            }
                                                                            if (bArr3[i116] == 0 && bArr3[i116 + 1] == 0 && bArr3[i116 + 2] == 0) {
                                                                                bArr2 = bArr3;
                                                                                if (bArr3[i116 + 3] == 1 && (i117 = i117 + 1) > 1) {
                                                                                    bufferInfo5.offset += i116;
                                                                                    bufferInfo5.size -= i116;
                                                                                    break;
                                                                                }
                                                                            } else {
                                                                                bArr2 = bArr3;
                                                                            }
                                                                            i116++;
                                                                            bArr3 = bArr2;
                                                                        }
                                                                    }
                                                                    z34 = false;
                                                                }
                                                                boolean z48 = z34;
                                                                z39 = z38;
                                                                long writeSampleData = mediaCodecVideoConvertor.mediaMuxer.writeSampleData(i95, outputBuffer2, bufferInfo5, true);
                                                                if (writeSampleData == 0 || (videoConvertorListener3 = mediaCodecVideoConvertor.callback) == null) {
                                                                    z41 = z48;
                                                                } else {
                                                                    z41 = z48;
                                                                    videoConvertorListener3.didWriteData(writeSampleData, (((float) 0) / 1000.0f) / f);
                                                                }
                                                                z34 = z41;
                                                                i107 = i97;
                                                                str15 = str12;
                                                                i105 = i12;
                                                                i12 = i101;
                                                            } else {
                                                                byteBufferArr5 = outputBuffers2;
                                                                z39 = z38;
                                                                if (i95 == -5) {
                                                                    byte[] bArr4 = new byte[i106];
                                                                    outputBuffer2.limit(bufferInfo5.offset + i106);
                                                                    outputBuffer2.position(bufferInfo5.offset);
                                                                    outputBuffer2.get(bArr4);
                                                                    byte b = 1;
                                                                    int i118 = bufferInfo5.size - 1;
                                                                    while (i118 >= 0 && i118 > 3) {
                                                                        if (bArr4[i118] == b && bArr4[i118 - 1] == 0 && bArr4[i118 - 2] == 0) {
                                                                            int i119 = i118 - 3;
                                                                            if (bArr4[i119] == 0) {
                                                                                byteBuffer5 = ByteBuffer.allocate(i119);
                                                                                byteBuffer6 = ByteBuffer.allocate(bufferInfo5.size - i119);
                                                                                i105 = i12;
                                                                                byteBuffer5.put(bArr4, 0, i119).position(0);
                                                                                byteBuffer6.put(bArr4, i119, bufferInfo5.size - i119).position(0);
                                                                                break;
                                                                            }
                                                                        }
                                                                        i118--;
                                                                        i12 = i12;
                                                                        b = 1;
                                                                    }
                                                                    i105 = i12;
                                                                    byteBuffer5 = null;
                                                                    byteBuffer6 = null;
                                                                    i12 = i101;
                                                                    i107 = i97;
                                                                    str15 = str12;
                                                                    try {
                                                                        MediaFormat createVideoFormat3 = MediaFormat.createVideoFormat(str15, i107, i12);
                                                                        if (byteBuffer5 != null && byteBuffer6 != null) {
                                                                            createVideoFormat3.setByteBuffer(str14, byteBuffer5);
                                                                            createVideoFormat3.setByteBuffer("csd-1", byteBuffer6);
                                                                        }
                                                                        i95 = mediaCodecVideoConvertor.mediaMuxer.addTrack(createVideoFormat3, false);
                                                                    } catch (Exception e18) {
                                                                        e = e18;
                                                                        i93 = i107;
                                                                        mediaCodec9 = createEncoderByType2;
                                                                        exc2 = e;
                                                                        if (exc2 instanceof IllegalStateException) {
                                                                        }
                                                                        StringBuilder sb22222222 = new StringBuilder();
                                                                        sb22222222.append("bitrate: ");
                                                                        i100 = i92;
                                                                        sb22222222.append(i100);
                                                                        sb22222222.append(" framerate: ");
                                                                        i99 = i6;
                                                                        sb22222222.append(i99);
                                                                        sb22222222.append(" size: ");
                                                                        sb22222222.append(i12);
                                                                        sb22222222.append("x");
                                                                        sb22222222.append(i93);
                                                                        FileLog.e(sb22222222.toString());
                                                                        FileLog.e(exc2);
                                                                        i98 = i95;
                                                                        i97 = i93;
                                                                        z6 = z32;
                                                                        r9 = byteBufferArr4;
                                                                        z12 = true;
                                                                        if (outputSurface8 != null) {
                                                                        }
                                                                        if (r9 != 0) {
                                                                        }
                                                                        if (mediaCodec9 != null) {
                                                                        }
                                                                        checkConversionCanceled();
                                                                        j5 = j2;
                                                                        j6 = j3;
                                                                        i22 = i100;
                                                                        i19 = i97;
                                                                        i21 = i12;
                                                                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                                        i14 = i98;
                                                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                        if (mediaExtractor2 != null) {
                                                                        }
                                                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                        if (mP4Builder2 != null) {
                                                                        }
                                                                        i17 = i21;
                                                                        i16 = i19;
                                                                        i18 = i22;
                                                                        z7 = z12;
                                                                        if (z6) {
                                                                        }
                                                                    } catch (Throwable th20) {
                                                                        th = th20;
                                                                        i96 = i6;
                                                                        j5 = j2;
                                                                        j6 = j3;
                                                                        i13 = i107;
                                                                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                                        i111 = i92;
                                                                        z6 = false;
                                                                        th = th;
                                                                        i14 = i95;
                                                                        i15 = i96;
                                                                        FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                        FileLog.e(th);
                                                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                        if (mediaExtractor != null) {
                                                                        }
                                                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                        if (mP4Builder != null) {
                                                                        }
                                                                        i16 = i13;
                                                                        i17 = i12;
                                                                        i18 = i111;
                                                                        z7 = true;
                                                                        if (z6) {
                                                                        }
                                                                    }
                                                                } else {
                                                                    i105 = i12;
                                                                }
                                                            }
                                                            boolean z49 = (bufferInfo5.flags & 4) == 0;
                                                            createEncoderByType2.releaseOutputBuffer(dequeueOutputBuffer3, false);
                                                            z40 = z47;
                                                            i97 = i107;
                                                            z37 = z49;
                                                            i108 = -1;
                                                            z42 = z34;
                                                            byteBufferArr6 = byteBufferArr5;
                                                            if (dequeueOutputBuffer3 == i108) {
                                                            }
                                                        } catch (Exception e19) {
                                                            e = e19;
                                                            i12 = i101;
                                                            i107 = i97;
                                                        } catch (Throwable th21) {
                                                            th = th21;
                                                            i12 = i101;
                                                            i107 = i97;
                                                        }
                                                    } else {
                                                        byteBufferArr5 = outputBuffers2;
                                                        i105 = i12;
                                                        z39 = z38;
                                                    }
                                                    if ((bufferInfo5.flags & 4) == 0) {
                                                    }
                                                    createEncoderByType2.releaseOutputBuffer(dequeueOutputBuffer3, false);
                                                    z40 = z47;
                                                    i97 = i107;
                                                    z37 = z49;
                                                    i108 = -1;
                                                    z42 = z34;
                                                    byteBufferArr6 = byteBufferArr5;
                                                    if (dequeueOutputBuffer3 == i108) {
                                                    }
                                                } catch (Exception e20) {
                                                    e = e20;
                                                    i97 = i107;
                                                    exc2 = e;
                                                    mediaCodec9 = createEncoderByType2;
                                                    i93 = i97;
                                                    if (exc2 instanceof IllegalStateException) {
                                                    }
                                                    StringBuilder sb222222222 = new StringBuilder();
                                                    sb222222222.append("bitrate: ");
                                                    i100 = i92;
                                                    sb222222222.append(i100);
                                                    sb222222222.append(" framerate: ");
                                                    i99 = i6;
                                                    sb222222222.append(i99);
                                                    sb222222222.append(" size: ");
                                                    sb222222222.append(i12);
                                                    sb222222222.append("x");
                                                    sb222222222.append(i93);
                                                    FileLog.e(sb222222222.toString());
                                                    FileLog.e(exc2);
                                                    i98 = i95;
                                                    i97 = i93;
                                                    z6 = z32;
                                                    r9 = byteBufferArr4;
                                                    z12 = true;
                                                    if (outputSurface8 != null) {
                                                    }
                                                    if (r9 != 0) {
                                                    }
                                                    if (mediaCodec9 != null) {
                                                    }
                                                    checkConversionCanceled();
                                                    j5 = j2;
                                                    j6 = j3;
                                                    i22 = i100;
                                                    i19 = i97;
                                                    i21 = i12;
                                                    mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                    i14 = i98;
                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                    if (mediaExtractor2 != null) {
                                                    }
                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                    if (mP4Builder2 != null) {
                                                    }
                                                    i17 = i21;
                                                    i16 = i19;
                                                    i18 = i22;
                                                    z7 = z12;
                                                    if (z6) {
                                                    }
                                                } catch (Throwable th22) {
                                                    th = th22;
                                                    i97 = i107;
                                                    i109 = i6;
                                                    j5 = j2;
                                                    j6 = j3;
                                                    th = th;
                                                    mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                    i111 = i92;
                                                    i13 = i97;
                                                    i94 = i109;
                                                    z6 = false;
                                                    i96 = i94;
                                                    i14 = i95;
                                                    i15 = i96;
                                                    FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                    FileLog.e(th);
                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                    if (mediaExtractor != null) {
                                                    }
                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                    if (mP4Builder != null) {
                                                    }
                                                    i16 = i13;
                                                    i17 = i12;
                                                    i18 = i111;
                                                    z7 = true;
                                                    if (z6) {
                                                    }
                                                }
                                                i12 = i101;
                                                i107 = i97;
                                                str15 = str12;
                                            } catch (Exception e21) {
                                                e = e21;
                                                i12 = i101;
                                                mediaCodec9 = createEncoderByType2;
                                                i93 = i97;
                                                exc2 = e;
                                                if (exc2 instanceof IllegalStateException) {
                                                }
                                                StringBuilder sb2222222222 = new StringBuilder();
                                                sb2222222222.append("bitrate: ");
                                                i100 = i92;
                                                sb2222222222.append(i100);
                                                sb2222222222.append(" framerate: ");
                                                i99 = i6;
                                                sb2222222222.append(i99);
                                                sb2222222222.append(" size: ");
                                                sb2222222222.append(i12);
                                                sb2222222222.append("x");
                                                sb2222222222.append(i93);
                                                FileLog.e(sb2222222222.toString());
                                                FileLog.e(exc2);
                                                i98 = i95;
                                                i97 = i93;
                                                z6 = z32;
                                                r9 = byteBufferArr4;
                                                z12 = true;
                                                if (outputSurface8 != null) {
                                                }
                                                if (r9 != 0) {
                                                }
                                                if (mediaCodec9 != null) {
                                                }
                                                checkConversionCanceled();
                                                j5 = j2;
                                                j6 = j3;
                                                i22 = i100;
                                                i19 = i97;
                                                i21 = i12;
                                                mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                i14 = i98;
                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                if (mediaExtractor2 != null) {
                                                }
                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                if (mP4Builder2 != null) {
                                                }
                                                i17 = i21;
                                                i16 = i19;
                                                i18 = i22;
                                                z7 = z12;
                                                if (z6) {
                                                }
                                            }
                                        } catch (Throwable th23) {
                                            th = th23;
                                            i12 = i101;
                                        }
                                    } catch (Exception e22) {
                                        e = e22;
                                        byteBufferArr4 = outputBuffers2;
                                        mediaCodec9 = createEncoderByType2;
                                        i93 = i97;
                                        exc2 = e;
                                        if (exc2 instanceof IllegalStateException) {
                                        }
                                        StringBuilder sb22222222222 = new StringBuilder();
                                        sb22222222222.append("bitrate: ");
                                        i100 = i92;
                                        sb22222222222.append(i100);
                                        sb22222222222.append(" framerate: ");
                                        i99 = i6;
                                        sb22222222222.append(i99);
                                        sb22222222222.append(" size: ");
                                        sb22222222222.append(i12);
                                        sb22222222222.append("x");
                                        sb22222222222.append(i93);
                                        FileLog.e(sb22222222222.toString());
                                        FileLog.e(exc2);
                                        i98 = i95;
                                        i97 = i93;
                                        z6 = z32;
                                        r9 = byteBufferArr4;
                                        z12 = true;
                                        if (outputSurface8 != null) {
                                        }
                                        if (r9 != 0) {
                                        }
                                        if (mediaCodec9 != null) {
                                        }
                                        checkConversionCanceled();
                                        j5 = j2;
                                        j6 = j3;
                                        i22 = i100;
                                        i19 = i97;
                                        i21 = i12;
                                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                        i14 = i98;
                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                        if (mediaExtractor2 != null) {
                                        }
                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                        if (mP4Builder2 != null) {
                                        }
                                        i17 = i21;
                                        i16 = i19;
                                        i18 = i22;
                                        z7 = z12;
                                        if (z6) {
                                        }
                                    }
                                }
                                exc2 = e;
                                mediaCodec9 = createEncoderByType2;
                                i12 = i101;
                                i93 = i97;
                                z32 = ((exc2 instanceof IllegalStateException) || z3) ? false : true;
                                StringBuilder sb222222222222 = new StringBuilder();
                                sb222222222222.append("bitrate: ");
                                i100 = i92;
                                sb222222222222.append(i100);
                                sb222222222222.append(" framerate: ");
                                i99 = i6;
                                sb222222222222.append(i99);
                                sb222222222222.append(" size: ");
                                sb222222222222.append(i12);
                                sb222222222222.append("x");
                                sb222222222222.append(i93);
                                FileLog.e(sb222222222222.toString());
                                FileLog.e(exc2);
                                i98 = i95;
                                i97 = i93;
                                z6 = z32;
                                r9 = byteBufferArr4;
                                z12 = true;
                                if (outputSurface8 != null) {
                                    try {
                                        outputSurface8.release();
                                    } catch (Throwable th24) {
                                        j5 = j2;
                                        j6 = j3;
                                        th = th24;
                                        i111 = i100;
                                        i15 = i99;
                                        i13 = i97;
                                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                        i14 = i98;
                                        FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                        FileLog.e(th);
                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                        if (mediaExtractor != null) {
                                        }
                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                        if (mP4Builder != null) {
                                        }
                                        i16 = i13;
                                        i17 = i12;
                                        i18 = i111;
                                        z7 = true;
                                        if (z6) {
                                        }
                                    }
                                }
                                if (r9 != 0) {
                                    r9.release();
                                }
                                if (mediaCodec9 != null) {
                                    mediaCodec9.stop();
                                    mediaCodec9.release();
                                }
                                checkConversionCanceled();
                                j5 = j2;
                                j6 = j3;
                                i22 = i100;
                                i19 = i97;
                                i21 = i12;
                                mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                i14 = i98;
                            }
                            i12 = i101;
                            str15 = str12;
                            z42 = z34;
                            byteBufferArr6 = outputBuffers2;
                            i108 = -1;
                            if (dequeueOutputBuffer3 == i108) {
                            }
                        }
                    }
                }
                i98 = i14;
                i12 = i101;
                mediaCodecVideoConvertor = this;
                r9 = byteBufferArr4;
                mediaCodec9 = createEncoderByType2;
                i100 = i92;
                z6 = false;
                z12 = false;
                i99 = i6;
                if (outputSurface8 != null) {
                }
                if (r9 != 0) {
                }
                if (mediaCodec9 != null) {
                }
                checkConversionCanceled();
                j5 = j2;
                j6 = j3;
                i22 = i100;
                i19 = i97;
                i21 = i12;
                mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                i14 = i98;
            } else {
                try {
                    MediaExtractor mediaExtractor4 = new MediaExtractor();
                    this.extractor = mediaExtractor4;
                    mediaExtractor4.setDataSource(str);
                    int findTrack2 = MediaController.findTrack(this.extractor, false);
                    if (i111 != -1) {
                        try {
                            findTrack = MediaController.findTrack(this.extractor, true);
                        } catch (Throwable th25) {
                            j5 = j2;
                            j6 = j3;
                            th = th25;
                            i13 = i4;
                            i12 = i110;
                            i11 = i6;
                            z6 = false;
                            mediaCodecVideoConvertor2 = this;
                            i14 = -5;
                            i15 = i11;
                            FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                            FileLog.e(th);
                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                            if (mediaExtractor != null) {
                            }
                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                            if (mP4Builder != null) {
                            }
                            i16 = i13;
                            i17 = i12;
                            i18 = i111;
                            z7 = true;
                            if (z6) {
                            }
                        }
                    } else {
                        findTrack = -1;
                    }
                    if (findTrack2 >= 0) {
                        if (!this.extractor.getTrackFormat(findTrack2).getString("mime").equals(MediaController.VIDEO_MIME_TYPE)) {
                            z8 = z2;
                            z9 = true;
                            if (!z8 || z9) {
                                if (findTrack2 < 0) {
                                    try {
                                        j8 = (1000 / i6) * 1000;
                                        if (i6 < 30) {
                                            try {
                                                i31 = 1000 / (i6 + 5);
                                            } catch (Exception e23) {
                                                i26 = i7;
                                                j5 = j2;
                                                j7 = j3;
                                                exc = e23;
                                                i24 = findTrack2;
                                                i27 = i6;
                                                mediaCodecVideoConvertor2 = this;
                                                mediaCodec2 = null;
                                                i14 = -5;
                                                mediaCodec3 = null;
                                                int i120 = i27;
                                                outputSurface2 = null;
                                                inputSurface = null;
                                                int i121 = i120;
                                                audioRecoder = null;
                                                i28 = i121;
                                                j6 = j7;
                                                try {
                                                    if (exc instanceof IllegalStateException) {
                                                    }
                                                    try {
                                                        StringBuilder sb3 = new StringBuilder();
                                                        sb3.append("bitrate: ");
                                                        sb3.append(i26);
                                                        sb3.append(" framerate: ");
                                                        sb3.append(i28 == 1 ? 1 : 0);
                                                        sb3.append(" size: ");
                                                        i21 = i5;
                                                        try {
                                                            sb3.append(i21);
                                                            sb3.append("x");
                                                            i19 = i4;
                                                            try {
                                                                sb3.append(i19);
                                                                FileLog.e(sb3.toString());
                                                                FileLog.e(exc);
                                                                i22 = i26;
                                                                mediaCodec4 = mediaCodec2;
                                                                i29 = i14;
                                                                z6 = z11;
                                                                z13 = true;
                                                                i30 = i28;
                                                                try {
                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                    if (mediaCodec4 != null) {
                                                                    }
                                                                    z11 = z6;
                                                                    z10 = z13;
                                                                    mediaCodec = mediaCodec3;
                                                                    outputSurface = outputSurface2;
                                                                    i14 = i29;
                                                                    i20 = i30;
                                                                    if (outputSurface != null) {
                                                                    }
                                                                    if (inputSurface != null) {
                                                                    }
                                                                    if (mediaCodec != null) {
                                                                    }
                                                                    if (audioRecoder != null) {
                                                                    }
                                                                    checkConversionCanceled();
                                                                    z12 = z10;
                                                                    z6 = z11;
                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                    if (mediaExtractor2 != null) {
                                                                    }
                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                    if (mP4Builder2 != null) {
                                                                    }
                                                                    i17 = i21;
                                                                    i16 = i19;
                                                                    i18 = i22;
                                                                    z7 = z12;
                                                                } catch (Throwable th26) {
                                                                    th = th26;
                                                                    i12 = i21;
                                                                    i13 = i19;
                                                                    i111 = i22;
                                                                    i14 = i29;
                                                                    i15 = i30;
                                                                    FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                    FileLog.e(th);
                                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                    if (mediaExtractor != null) {
                                                                    }
                                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                    if (mP4Builder != null) {
                                                                    }
                                                                    i16 = i13;
                                                                    i17 = i12;
                                                                    i18 = i111;
                                                                    z7 = true;
                                                                    if (z6) {
                                                                    }
                                                                }
                                                            } catch (Throwable th27) {
                                                                th = th27;
                                                                th = th;
                                                                i111 = i26;
                                                                i12 = i21;
                                                                i13 = i19;
                                                                i23 = i28;
                                                                z6 = z11;
                                                                i15 = i23;
                                                                FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                FileLog.e(th);
                                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                if (mediaExtractor != null) {
                                                                }
                                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                if (mP4Builder != null) {
                                                                }
                                                                i16 = i13;
                                                                i17 = i12;
                                                                i18 = i111;
                                                                z7 = true;
                                                                if (z6) {
                                                                }
                                                            }
                                                        } catch (Throwable th28) {
                                                            th = th28;
                                                            i19 = i4;
                                                        }
                                                    } catch (Throwable th29) {
                                                        th = th29;
                                                        i19 = i4;
                                                        i21 = i5;
                                                    }
                                                } catch (Throwable th30) {
                                                    th = th30;
                                                    i111 = i26;
                                                    i12 = i5;
                                                    i13 = i4;
                                                    i91 = i28;
                                                    j6 = j6;
                                                    z6 = false;
                                                    i15 = i91;
                                                    FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                    FileLog.e(th);
                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                    if (mediaExtractor != null) {
                                                    }
                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                    if (mP4Builder != null) {
                                                    }
                                                    i16 = i13;
                                                    i17 = i12;
                                                    i18 = i111;
                                                    z7 = true;
                                                    if (z6) {
                                                    }
                                                }
                                                if (z6) {
                                                }
                                            } catch (Throwable th31) {
                                                i13 = i4;
                                                i12 = i5;
                                                i111 = i7;
                                                j5 = j2;
                                                j6 = j3;
                                                th = th31;
                                                i11 = i6;
                                                mediaCodecVideoConvertor2 = this;
                                                z6 = false;
                                                i14 = -5;
                                                i15 = i11;
                                                FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                FileLog.e(th);
                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                if (mediaExtractor != null) {
                                                }
                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                if (mP4Builder != null) {
                                                }
                                                i16 = i13;
                                                i17 = i12;
                                                i18 = i111;
                                                z7 = true;
                                                if (z6) {
                                                }
                                            }
                                        } else {
                                            i31 = 1000 / (i6 + 1);
                                        }
                                        long j42 = i31 * 1000;
                                        this.extractor.selectTrack(findTrack2);
                                        trackFormat = this.extractor.getTrackFormat(findTrack2);
                                        if (j3 >= 0) {
                                            i26 = f <= 2000.0f ? 2600000 : f <= 5000.0f ? 2200000 : 1560000;
                                            j10 = j42;
                                            j9 = 0;
                                        } else if (i7 <= 0) {
                                            j9 = j3;
                                            j10 = j42;
                                            i26 = 921600;
                                        } else {
                                            i26 = i7;
                                            j9 = j3;
                                            j10 = j42;
                                        }
                                        if (i8 > 0) {
                                            try {
                                                i26 = Math.min(i8, i26);
                                            } catch (Exception e24) {
                                                j5 = j2;
                                                exc = e24;
                                                i24 = findTrack2;
                                                i27 = i6;
                                                mediaCodecVideoConvertor2 = this;
                                                j7 = j9;
                                                mediaCodec2 = null;
                                                i14 = -5;
                                                mediaCodec3 = null;
                                                int i1202 = i27;
                                                outputSurface2 = null;
                                                inputSurface = null;
                                                int i1212 = i1202;
                                                audioRecoder = null;
                                                i28 = i1212;
                                                j6 = j7;
                                                if (exc instanceof IllegalStateException) {
                                                }
                                                StringBuilder sb32 = new StringBuilder();
                                                sb32.append("bitrate: ");
                                                sb32.append(i26);
                                                sb32.append(" framerate: ");
                                                sb32.append(i28 == 1 ? 1 : 0);
                                                sb32.append(" size: ");
                                                i21 = i5;
                                                sb32.append(i21);
                                                sb32.append("x");
                                                i19 = i4;
                                                sb32.append(i19);
                                                FileLog.e(sb32.toString());
                                                FileLog.e(exc);
                                                i22 = i26;
                                                mediaCodec4 = mediaCodec2;
                                                i29 = i14;
                                                z6 = z11;
                                                z13 = true;
                                                i30 = i28;
                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                if (mediaCodec4 != null) {
                                                }
                                                z11 = z6;
                                                z10 = z13;
                                                mediaCodec = mediaCodec3;
                                                outputSurface = outputSurface2;
                                                i14 = i29;
                                                i20 = i30;
                                                if (outputSurface != null) {
                                                }
                                                if (inputSurface != null) {
                                                }
                                                if (mediaCodec != null) {
                                                }
                                                if (audioRecoder != null) {
                                                }
                                                checkConversionCanceled();
                                                z12 = z10;
                                                z6 = z11;
                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                if (mediaExtractor2 != null) {
                                                }
                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                if (mP4Builder2 != null) {
                                                }
                                                i17 = i21;
                                                i16 = i19;
                                                i18 = i22;
                                                z7 = z12;
                                                if (z6) {
                                                }
                                            } catch (Throwable th32) {
                                                i13 = i4;
                                                j5 = j2;
                                                th = th32;
                                                i111 = i26;
                                                i32 = i6;
                                                mediaCodecVideoConvertor2 = this;
                                                j6 = j9;
                                                z6 = false;
                                                i14 = -5;
                                                i12 = i5;
                                                i15 = i32;
                                                FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                FileLog.e(th);
                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                if (mediaExtractor != null) {
                                                }
                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                if (mP4Builder != null) {
                                                }
                                                i16 = i13;
                                                i17 = i12;
                                                i18 = i111;
                                                z7 = true;
                                                if (z6) {
                                                }
                                            }
                                        }
                                        j11 = j9 >= 0 ? -1L : j9;
                                        try {
                                            try {
                                            } catch (Exception e25) {
                                                j5 = j2;
                                                exc = e25;
                                                i24 = findTrack2;
                                                i33 = i6;
                                                j12 = j11;
                                            }
                                        } catch (Throwable th33) {
                                            i13 = i4;
                                            j5 = j2;
                                            th = th33;
                                            i111 = i26;
                                            i11 = i6;
                                            j6 = j11;
                                            z6 = false;
                                            i12 = i5;
                                            mediaCodecVideoConvertor2 = this;
                                            i14 = -5;
                                            i15 = i11;
                                            FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                            FileLog.e(th);
                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                            if (mediaExtractor != null) {
                                            }
                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                            if (mP4Builder != null) {
                                            }
                                            i16 = i13;
                                            i17 = i12;
                                            i18 = i111;
                                            z7 = true;
                                            if (z6) {
                                            }
                                        }
                                    } catch (Exception e26) {
                                        e = e26;
                                        i24 = findTrack2;
                                        i25 = i6;
                                        mediaCodecVideoConvertor2 = this;
                                        i26 = i7;
                                    } catch (Throwable th34) {
                                        i11 = i6;
                                        mediaCodecVideoConvertor2 = this;
                                        i13 = i4;
                                        i12 = i5;
                                        i111 = i7;
                                        j5 = j2;
                                        j6 = j3;
                                        th = th34;
                                    }
                                    try {
                                        try {
                                            if (j11 >= 0) {
                                                i34 = findTrack;
                                                j13 = j8;
                                                this.extractor.seekTo(j11, 0);
                                                i35 = findTrack2;
                                                str3 = "csd-0";
                                            } else {
                                                i34 = findTrack;
                                                j13 = j8;
                                                if (j > 0) {
                                                    i35 = findTrack2;
                                                    try {
                                                        this.extractor.seekTo(j, 0);
                                                        cropState2 = cropState;
                                                        str3 = "csd-0";
                                                        if (cropState2 == null) {
                                                            if (i == 90 || i == 270) {
                                                                i88 = cropState2.transformHeight;
                                                                i89 = cropState2.transformWidth;
                                                            } else {
                                                                i88 = cropState2.transformWidth;
                                                                i89 = cropState2.transformHeight;
                                                            }
                                                            int i122 = i89;
                                                            i36 = i88;
                                                            i37 = i122;
                                                        } else {
                                                            i36 = i4;
                                                            i37 = i5;
                                                        }
                                                        if (BuildVars.LOGS_ENABLED) {
                                                            FileLog.d("create encoder with w = " + i36 + " h = " + i37);
                                                        }
                                                        createVideoFormat = MediaFormat.createVideoFormat(MediaController.VIDEO_MIME_TYPE, i36, i37);
                                                        createVideoFormat.setInteger("color-format", 2130708361);
                                                        createVideoFormat.setInteger("bitrate", i26);
                                                        createVideoFormat.setInteger("frame-rate", i6);
                                                        createVideoFormat.setInteger("i-frame-interval", 2);
                                                        i38 = Build.VERSION.SDK_INT;
                                                    } catch (Exception e27) {
                                                        j5 = j2;
                                                        exc = e27;
                                                        i33 = i6;
                                                        j12 = j11;
                                                        i24 = i35;
                                                        mediaCodec2 = null;
                                                        mediaCodec3 = null;
                                                        outputSurface2 = null;
                                                        inputSurface = null;
                                                        audioRecoder = null;
                                                        mediaCodecVideoConvertor2 = this;
                                                        i14 = -5;
                                                        i28 = i33;
                                                        j6 = j12;
                                                        if (exc instanceof IllegalStateException) {
                                                        }
                                                        StringBuilder sb322 = new StringBuilder();
                                                        sb322.append("bitrate: ");
                                                        sb322.append(i26);
                                                        sb322.append(" framerate: ");
                                                        sb322.append(i28 == 1 ? 1 : 0);
                                                        sb322.append(" size: ");
                                                        i21 = i5;
                                                        sb322.append(i21);
                                                        sb322.append("x");
                                                        i19 = i4;
                                                        sb322.append(i19);
                                                        FileLog.e(sb322.toString());
                                                        FileLog.e(exc);
                                                        i22 = i26;
                                                        mediaCodec4 = mediaCodec2;
                                                        i29 = i14;
                                                        z6 = z11;
                                                        z13 = true;
                                                        i30 = i28;
                                                        mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                        if (mediaCodec4 != null) {
                                                        }
                                                        z11 = z6;
                                                        z10 = z13;
                                                        mediaCodec = mediaCodec3;
                                                        outputSurface = outputSurface2;
                                                        i14 = i29;
                                                        i20 = i30;
                                                        if (outputSurface != null) {
                                                        }
                                                        if (inputSurface != null) {
                                                        }
                                                        if (mediaCodec != null) {
                                                        }
                                                        if (audioRecoder != null) {
                                                        }
                                                        checkConversionCanceled();
                                                        z12 = z10;
                                                        z6 = z11;
                                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                        if (mediaExtractor2 != null) {
                                                        }
                                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                        if (mP4Builder2 != null) {
                                                        }
                                                        i17 = i21;
                                                        i16 = i19;
                                                        i18 = i22;
                                                        z7 = z12;
                                                        if (z6) {
                                                        }
                                                    }
                                                    if (i38 >= 23) {
                                                        i39 = i37;
                                                        if (Math.min(i37, i36) <= 480) {
                                                            int i123 = i26 <= 921600 ? i26 : 921600;
                                                            try {
                                                                createVideoFormat.setInteger("bitrate", i123);
                                                                i22 = i123;
                                                                createEncoderByType = MediaCodec.createEncoderByType(MediaController.VIDEO_MIME_TYPE);
                                                                createEncoderByType.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
                                                                InputSurface inputSurface6 = new InputSurface(createEncoderByType.createInputSurface());
                                                                inputSurface6.makeCurrent();
                                                                createEncoderByType.start();
                                                                createDecoderByType = MediaCodec.createDecoderByType(trackFormat.getString("mime"));
                                                                float f3 = i6;
                                                                int i124 = i34;
                                                                i41 = i39;
                                                                inputSurface = inputSurface6;
                                                                mediaCodec5 = createEncoderByType;
                                                                i42 = i35;
                                                                bufferInfo = bufferInfo5;
                                                                str4 = MediaController.VIDEO_MIME_TYPE;
                                                                str5 = "prepend-sps-pps-to-idr-frames";
                                                                i43 = i36;
                                                                str6 = str3;
                                                                j15 = j11;
                                                                mediaCodecVideoConvertor2 = this;
                                                                outputSurface4 = outputSurface3;
                                                                outputSurface3 = new OutputSurface(savedFilterState, null, str2, arrayList, cropState, i4, i5, i2, i3, i, f3, false);
                                                            } catch (Exception e28) {
                                                                j5 = j2;
                                                                exc = e28;
                                                                i26 = i123;
                                                                i28 = i6;
                                                                j6 = j11;
                                                                i24 = i35;
                                                                mediaCodec2 = null;
                                                                i14 = -5;
                                                                mediaCodec3 = null;
                                                                outputSurface2 = null;
                                                                inputSurface = null;
                                                                audioRecoder = null;
                                                                mediaCodecVideoConvertor2 = this;
                                                                if (exc instanceof IllegalStateException) {
                                                                }
                                                                StringBuilder sb3222 = new StringBuilder();
                                                                sb3222.append("bitrate: ");
                                                                sb3222.append(i26);
                                                                sb3222.append(" framerate: ");
                                                                sb3222.append(i28 == 1 ? 1 : 0);
                                                                sb3222.append(" size: ");
                                                                i21 = i5;
                                                                sb3222.append(i21);
                                                                sb3222.append("x");
                                                                i19 = i4;
                                                                sb3222.append(i19);
                                                                FileLog.e(sb3222.toString());
                                                                FileLog.e(exc);
                                                                i22 = i26;
                                                                mediaCodec4 = mediaCodec2;
                                                                i29 = i14;
                                                                z6 = z11;
                                                                z13 = true;
                                                                i30 = i28;
                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                if (mediaCodec4 != null) {
                                                                }
                                                                z11 = z6;
                                                                z10 = z13;
                                                                mediaCodec = mediaCodec3;
                                                                outputSurface = outputSurface2;
                                                                i14 = i29;
                                                                i20 = i30;
                                                                if (outputSurface != null) {
                                                                }
                                                                if (inputSurface != null) {
                                                                }
                                                                if (mediaCodec != null) {
                                                                }
                                                                if (audioRecoder != null) {
                                                                }
                                                                checkConversionCanceled();
                                                                z12 = z10;
                                                                z6 = z11;
                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                if (mediaExtractor2 != null) {
                                                                }
                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                if (mP4Builder2 != null) {
                                                                }
                                                                i17 = i21;
                                                                i16 = i19;
                                                                i18 = i22;
                                                                z7 = z12;
                                                                if (z6) {
                                                                }
                                                            } catch (Throwable th35) {
                                                                i12 = i5;
                                                                j5 = j2;
                                                                th = th35;
                                                                i111 = i123;
                                                                i15 = i6;
                                                                j6 = j11;
                                                                z6 = false;
                                                                i14 = -5;
                                                                mediaCodecVideoConvertor2 = this;
                                                                i13 = i4;
                                                                FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                FileLog.e(th);
                                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                if (mediaExtractor != null) {
                                                                }
                                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                if (mP4Builder != null) {
                                                                }
                                                                i16 = i13;
                                                                i17 = i12;
                                                                i18 = i111;
                                                                z7 = true;
                                                                if (z6) {
                                                                }
                                                            }
                                                            if (z5) {
                                                                i13 = i4;
                                                                i44 = i5;
                                                            } else {
                                                                i44 = i5;
                                                                try {
                                                                } catch (Exception e29) {
                                                                    e = e29;
                                                                } catch (Throwable th36) {
                                                                    th2 = th36;
                                                                }
                                                                try {
                                                                } catch (Exception e30) {
                                                                    e = e30;
                                                                    i45 = i6;
                                                                    j5 = j2;
                                                                    j17 = j15;
                                                                    exc = e;
                                                                    outputSurface2 = outputSurface4;
                                                                    i26 = i22;
                                                                    mediaCodec2 = createDecoderByType;
                                                                    i24 = i42;
                                                                    i14 = -5;
                                                                    audioRecoder = null;
                                                                    i46 = i45;
                                                                    mediaCodec3 = mediaCodec5;
                                                                    i28 = i46;
                                                                    j6 = j17;
                                                                    if (exc instanceof IllegalStateException) {
                                                                    }
                                                                    StringBuilder sb32222 = new StringBuilder();
                                                                    sb32222.append("bitrate: ");
                                                                    sb32222.append(i26);
                                                                    sb32222.append(" framerate: ");
                                                                    sb32222.append(i28 == 1 ? 1 : 0);
                                                                    sb32222.append(" size: ");
                                                                    i21 = i5;
                                                                    sb32222.append(i21);
                                                                    sb32222.append("x");
                                                                    i19 = i4;
                                                                    sb32222.append(i19);
                                                                    FileLog.e(sb32222.toString());
                                                                    FileLog.e(exc);
                                                                    i22 = i26;
                                                                    mediaCodec4 = mediaCodec2;
                                                                    i29 = i14;
                                                                    z6 = z11;
                                                                    z13 = true;
                                                                    i30 = i28;
                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                    if (mediaCodec4 != null) {
                                                                    }
                                                                    z11 = z6;
                                                                    z10 = z13;
                                                                    mediaCodec = mediaCodec3;
                                                                    outputSurface = outputSurface2;
                                                                    i14 = i29;
                                                                    i20 = i30;
                                                                    if (outputSurface != null) {
                                                                    }
                                                                    if (inputSurface != null) {
                                                                    }
                                                                    if (mediaCodec != null) {
                                                                    }
                                                                    if (audioRecoder != null) {
                                                                    }
                                                                    checkConversionCanceled();
                                                                    z12 = z10;
                                                                    z6 = z11;
                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                    if (mediaExtractor2 != null) {
                                                                    }
                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                    if (mP4Builder2 != null) {
                                                                    }
                                                                    i17 = i21;
                                                                    i16 = i19;
                                                                    i18 = i22;
                                                                    z7 = z12;
                                                                    if (z6) {
                                                                    }
                                                                } catch (Throwable th37) {
                                                                    th2 = th37;
                                                                    i13 = i4;
                                                                    i15 = i6;
                                                                    j5 = j2;
                                                                    j6 = j15;
                                                                    i12 = i44;
                                                                    i111 = i22;
                                                                    z6 = false;
                                                                    i14 = -5;
                                                                    th = th2;
                                                                    FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                    FileLog.e(th);
                                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                    if (mediaExtractor != null) {
                                                                    }
                                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                    if (mP4Builder != null) {
                                                                    }
                                                                    i16 = i13;
                                                                    i17 = i12;
                                                                    i18 = i111;
                                                                    z7 = true;
                                                                    if (z6) {
                                                                    }
                                                                }
                                                                if (Math.max(i44, i44) / Math.max(i3, i2) < 0.9f) {
                                                                    i13 = i4;
                                                                    z14 = true;
                                                                    try {
                                                                        try {
                                                                            outputSurface4.changeFragmentShader(createFragmentShader(i2, i3, i13, i44, true), createFragmentShader(i2, i3, i13, i44, false));
                                                                            mediaCodec2 = createDecoderByType;
                                                                            mediaCodec2.configure(trackFormat, outputSurface4.getSurface(), (MediaCrypto) null, 0);
                                                                            mediaCodec2.start();
                                                                            if (i38 >= 21) {
                                                                                try {
                                                                                    inputBuffers = mediaCodec2.getInputBuffers();
                                                                                    outputBuffers = mediaCodec5.getOutputBuffers();
                                                                                } catch (Exception e31) {
                                                                                    i47 = i6;
                                                                                    j5 = j2;
                                                                                    j17 = j15;
                                                                                    exc = e31;
                                                                                    audioRecoder = null;
                                                                                    outputSurface2 = outputSurface4;
                                                                                    i26 = i22;
                                                                                    i24 = i42;
                                                                                    i14 = -5;
                                                                                    i46 = i47;
                                                                                    mediaCodec3 = mediaCodec5;
                                                                                    i28 = i46;
                                                                                    j6 = j17;
                                                                                    if (exc instanceof IllegalStateException) {
                                                                                    }
                                                                                    StringBuilder sb322222 = new StringBuilder();
                                                                                    sb322222.append("bitrate: ");
                                                                                    sb322222.append(i26);
                                                                                    sb322222.append(" framerate: ");
                                                                                    sb322222.append(i28 == 1 ? 1 : 0);
                                                                                    sb322222.append(" size: ");
                                                                                    i21 = i5;
                                                                                    sb322222.append(i21);
                                                                                    sb322222.append("x");
                                                                                    i19 = i4;
                                                                                    sb322222.append(i19);
                                                                                    FileLog.e(sb322222.toString());
                                                                                    FileLog.e(exc);
                                                                                    i22 = i26;
                                                                                    mediaCodec4 = mediaCodec2;
                                                                                    i29 = i14;
                                                                                    z6 = z11;
                                                                                    z13 = true;
                                                                                    i30 = i28;
                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                    if (mediaCodec4 != null) {
                                                                                    }
                                                                                    z11 = z6;
                                                                                    z10 = z13;
                                                                                    mediaCodec = mediaCodec3;
                                                                                    outputSurface = outputSurface2;
                                                                                    i14 = i29;
                                                                                    i20 = i30;
                                                                                    if (outputSurface != null) {
                                                                                    }
                                                                                    if (inputSurface != null) {
                                                                                    }
                                                                                    if (mediaCodec != null) {
                                                                                    }
                                                                                    if (audioRecoder != null) {
                                                                                    }
                                                                                    checkConversionCanceled();
                                                                                    z12 = z10;
                                                                                    z6 = z11;
                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                    if (mediaExtractor2 != null) {
                                                                                    }
                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                    if (mP4Builder2 != null) {
                                                                                    }
                                                                                    i17 = i21;
                                                                                    i16 = i19;
                                                                                    i18 = i22;
                                                                                    z7 = z12;
                                                                                    if (z6) {
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                inputBuffers = null;
                                                                                outputBuffers = null;
                                                                            }
                                                                            i48 = i124;
                                                                        } catch (Exception e32) {
                                                                            e = e32;
                                                                            i45 = i6;
                                                                            j5 = j2;
                                                                            j17 = j15;
                                                                            exc = e;
                                                                            outputSurface2 = outputSurface4;
                                                                            i26 = i22;
                                                                            mediaCodec2 = createDecoderByType;
                                                                            i24 = i42;
                                                                            i14 = -5;
                                                                            audioRecoder = null;
                                                                            i46 = i45;
                                                                            mediaCodec3 = mediaCodec5;
                                                                            i28 = i46;
                                                                            j6 = j17;
                                                                            if (exc instanceof IllegalStateException) {
                                                                            }
                                                                            StringBuilder sb3222222 = new StringBuilder();
                                                                            sb3222222.append("bitrate: ");
                                                                            sb3222222.append(i26);
                                                                            sb3222222.append(" framerate: ");
                                                                            sb3222222.append(i28 == 1 ? 1 : 0);
                                                                            sb3222222.append(" size: ");
                                                                            i21 = i5;
                                                                            sb3222222.append(i21);
                                                                            sb3222222.append("x");
                                                                            i19 = i4;
                                                                            sb3222222.append(i19);
                                                                            FileLog.e(sb3222222.toString());
                                                                            FileLog.e(exc);
                                                                            i22 = i26;
                                                                            mediaCodec4 = mediaCodec2;
                                                                            i29 = i14;
                                                                            z6 = z11;
                                                                            z13 = true;
                                                                            i30 = i28;
                                                                            mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                            if (mediaCodec4 != null) {
                                                                            }
                                                                            z11 = z6;
                                                                            z10 = z13;
                                                                            mediaCodec = mediaCodec3;
                                                                            outputSurface = outputSurface2;
                                                                            i14 = i29;
                                                                            i20 = i30;
                                                                            if (outputSurface != null) {
                                                                            }
                                                                            if (inputSurface != null) {
                                                                            }
                                                                            if (mediaCodec != null) {
                                                                            }
                                                                            if (audioRecoder != null) {
                                                                            }
                                                                            checkConversionCanceled();
                                                                            z12 = z10;
                                                                            z6 = z11;
                                                                            mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                            if (mediaExtractor2 != null) {
                                                                            }
                                                                            mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                            if (mP4Builder2 != null) {
                                                                            }
                                                                            i17 = i21;
                                                                            i16 = i19;
                                                                            i18 = i22;
                                                                            z7 = z12;
                                                                            if (z6) {
                                                                            }
                                                                        }
                                                                        if (i48 < 0) {
                                                                            try {
                                                                                trackFormat2 = mediaCodecVideoConvertor2.extractor.getTrackFormat(i48);
                                                                            } catch (Exception e33) {
                                                                                e = e33;
                                                                            } catch (Throwable th38) {
                                                                                th = th38;
                                                                            }
                                                                            if (!trackFormat2.getString("mime").equals(MediaController.AUIDO_MIME_TYPE)) {
                                                                                try {
                                                                                } catch (Exception e34) {
                                                                                    e = e34;
                                                                                    i49 = i6;
                                                                                    j5 = j2;
                                                                                    j17 = j15;
                                                                                    exc = e;
                                                                                    outputSurface2 = outputSurface4;
                                                                                    i26 = i22;
                                                                                    i45 = i49;
                                                                                    i24 = i42;
                                                                                    i14 = -5;
                                                                                    audioRecoder = null;
                                                                                    i46 = i45;
                                                                                    mediaCodec3 = mediaCodec5;
                                                                                    i28 = i46;
                                                                                    j6 = j17;
                                                                                    if (exc instanceof IllegalStateException) {
                                                                                    }
                                                                                    StringBuilder sb32222222 = new StringBuilder();
                                                                                    sb32222222.append("bitrate: ");
                                                                                    sb32222222.append(i26);
                                                                                    sb32222222.append(" framerate: ");
                                                                                    sb32222222.append(i28 == 1 ? 1 : 0);
                                                                                    sb32222222.append(" size: ");
                                                                                    i21 = i5;
                                                                                    sb32222222.append(i21);
                                                                                    sb32222222.append("x");
                                                                                    i19 = i4;
                                                                                    sb32222222.append(i19);
                                                                                    FileLog.e(sb32222222.toString());
                                                                                    FileLog.e(exc);
                                                                                    i22 = i26;
                                                                                    mediaCodec4 = mediaCodec2;
                                                                                    i29 = i14;
                                                                                    z6 = z11;
                                                                                    z13 = true;
                                                                                    i30 = i28;
                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                    if (mediaCodec4 != null) {
                                                                                    }
                                                                                    z11 = z6;
                                                                                    z10 = z13;
                                                                                    mediaCodec = mediaCodec3;
                                                                                    outputSurface = outputSurface2;
                                                                                    i14 = i29;
                                                                                    i20 = i30;
                                                                                    if (outputSurface != null) {
                                                                                    }
                                                                                    if (inputSurface != null) {
                                                                                    }
                                                                                    if (mediaCodec != null) {
                                                                                    }
                                                                                    if (audioRecoder != null) {
                                                                                    }
                                                                                    checkConversionCanceled();
                                                                                    z12 = z10;
                                                                                    z6 = z11;
                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                    if (mediaExtractor2 != null) {
                                                                                    }
                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                    if (mP4Builder2 != null) {
                                                                                    }
                                                                                    i17 = i21;
                                                                                    i16 = i19;
                                                                                    i18 = i22;
                                                                                    z7 = z12;
                                                                                    if (z6) {
                                                                                    }
                                                                                }
                                                                                if (!trackFormat2.getString("mime").equals("audio/mpeg")) {
                                                                                    z15 = false;
                                                                                    if (trackFormat2.getString("mime").equals("audio/unknown")) {
                                                                                        i48 = -1;
                                                                                    }
                                                                                    if (i48 < 0) {
                                                                                        if (z15) {
                                                                                            try {
                                                                                                int addTrack = mediaCodecVideoConvertor2.mediaMuxer.addTrack(trackFormat2, z14);
                                                                                                mediaCodecVideoConvertor2.extractor.selectTrack(i48);
                                                                                                try {
                                                                                                    i52 = trackFormat2.getInteger("max-input-size");
                                                                                                } catch (Exception e35) {
                                                                                                    FileLog.e(e35);
                                                                                                    i52 = 0;
                                                                                                }
                                                                                                if (i52 <= 0) {
                                                                                                    i52 = CharacterCompat.MIN_SUPPLEMENTARY_CODE_POINT;
                                                                                                }
                                                                                                ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i52);
                                                                                                byteBufferArr = outputBuffers;
                                                                                                long j43 = j;
                                                                                                if (j43 > 0) {
                                                                                                    mediaCodecVideoConvertor2.extractor.seekTo(j43, 0);
                                                                                                    i53 = i52;
                                                                                                    byteBuffer = allocateDirect;
                                                                                                } else {
                                                                                                    try {
                                                                                                        i53 = i52;
                                                                                                        byteBuffer = allocateDirect;
                                                                                                        mediaCodecVideoConvertor2.extractor.seekTo(0L, 0);
                                                                                                    } catch (Throwable th39) {
                                                                                                        th = th39;
                                                                                                        i12 = i5;
                                                                                                        i11 = i6;
                                                                                                        j5 = j2;
                                                                                                        j6 = j15;
                                                                                                        th = th;
                                                                                                        i111 = i22;
                                                                                                        z6 = false;
                                                                                                        i14 = -5;
                                                                                                        i15 = i11;
                                                                                                        FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                        FileLog.e(th);
                                                                                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                        if (mediaExtractor != null) {
                                                                                                        }
                                                                                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                        if (mP4Builder != null) {
                                                                                                        }
                                                                                                        i16 = i13;
                                                                                                        i17 = i12;
                                                                                                        i18 = i111;
                                                                                                        z7 = true;
                                                                                                        if (z6) {
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                j18 = j2;
                                                                                                i51 = i53;
                                                                                                i50 = addTrack;
                                                                                                audioRecoder2 = null;
                                                                                                j20 = j43;
                                                                                            } catch (Exception e36) {
                                                                                                e = e36;
                                                                                                i49 = i6;
                                                                                                j5 = j2;
                                                                                                j17 = j15;
                                                                                                exc = e;
                                                                                                outputSurface2 = outputSurface4;
                                                                                                i26 = i22;
                                                                                                i45 = i49;
                                                                                                i24 = i42;
                                                                                                i14 = -5;
                                                                                                audioRecoder = null;
                                                                                                i46 = i45;
                                                                                                mediaCodec3 = mediaCodec5;
                                                                                                i28 = i46;
                                                                                                j6 = j17;
                                                                                                if (exc instanceof IllegalStateException) {
                                                                                                }
                                                                                                StringBuilder sb322222222 = new StringBuilder();
                                                                                                sb322222222.append("bitrate: ");
                                                                                                sb322222222.append(i26);
                                                                                                sb322222222.append(" framerate: ");
                                                                                                sb322222222.append(i28 == 1 ? 1 : 0);
                                                                                                sb322222222.append(" size: ");
                                                                                                i21 = i5;
                                                                                                sb322222222.append(i21);
                                                                                                sb322222222.append("x");
                                                                                                i19 = i4;
                                                                                                sb322222222.append(i19);
                                                                                                FileLog.e(sb322222222.toString());
                                                                                                FileLog.e(exc);
                                                                                                i22 = i26;
                                                                                                mediaCodec4 = mediaCodec2;
                                                                                                i29 = i14;
                                                                                                z6 = z11;
                                                                                                z13 = true;
                                                                                                i30 = i28;
                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                if (mediaCodec4 != null) {
                                                                                                }
                                                                                                z11 = z6;
                                                                                                z10 = z13;
                                                                                                mediaCodec = mediaCodec3;
                                                                                                outputSurface = outputSurface2;
                                                                                                i14 = i29;
                                                                                                i20 = i30;
                                                                                                if (outputSurface != null) {
                                                                                                }
                                                                                                if (inputSurface != null) {
                                                                                                }
                                                                                                if (mediaCodec != null) {
                                                                                                }
                                                                                                if (audioRecoder != null) {
                                                                                                }
                                                                                                checkConversionCanceled();
                                                                                                z12 = z10;
                                                                                                z6 = z11;
                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                if (mediaExtractor2 != null) {
                                                                                                }
                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                if (mP4Builder2 != null) {
                                                                                                }
                                                                                                i17 = i21;
                                                                                                i16 = i19;
                                                                                                i18 = i22;
                                                                                                z7 = z12;
                                                                                                if (z6) {
                                                                                                }
                                                                                            } catch (Throwable th40) {
                                                                                                th = th40;
                                                                                            }
                                                                                        } else {
                                                                                            byteBufferArr = outputBuffers;
                                                                                            long j44 = j;
                                                                                            try {
                                                                                                try {
                                                                                                    MediaExtractor mediaExtractor5 = new MediaExtractor();
                                                                                                    mediaExtractor5.setDataSource(str);
                                                                                                    mediaExtractor5.selectTrack(i48);
                                                                                                    z16 = z15;
                                                                                                    if (j44 > 0) {
                                                                                                        try {
                                                                                                            mediaExtractor5.seekTo(j44, 0);
                                                                                                        } catch (Throwable th41) {
                                                                                                            i11 = i6;
                                                                                                            j5 = j2;
                                                                                                            j6 = j15;
                                                                                                            th = th41;
                                                                                                            i12 = i5;
                                                                                                            i111 = i22;
                                                                                                            z6 = false;
                                                                                                            i14 = -5;
                                                                                                            i15 = i11;
                                                                                                            FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                            FileLog.e(th);
                                                                                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                            if (mediaExtractor != null) {
                                                                                                            }
                                                                                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                            if (mP4Builder != null) {
                                                                                                            }
                                                                                                            i16 = i13;
                                                                                                            i17 = i12;
                                                                                                            i18 = i111;
                                                                                                            z7 = true;
                                                                                                            if (z6) {
                                                                                                            }
                                                                                                        }
                                                                                                    } else {
                                                                                                        mediaExtractor5.seekTo(0L, 0);
                                                                                                    }
                                                                                                    audioRecoder3 = new AudioRecoder(trackFormat2, mediaExtractor5, i48);
                                                                                                    try {
                                                                                                        audioRecoder3.startTime = j44;
                                                                                                        j18 = j2;
                                                                                                    } catch (Exception e37) {
                                                                                                        e = e37;
                                                                                                        j18 = j2;
                                                                                                    }
                                                                                                } catch (Exception e38) {
                                                                                                    e = e38;
                                                                                                    i49 = i6;
                                                                                                    j17 = j15;
                                                                                                    exc = e;
                                                                                                    j5 = j2;
                                                                                                    outputSurface2 = outputSurface4;
                                                                                                    i26 = i22;
                                                                                                    i45 = i49;
                                                                                                    i24 = i42;
                                                                                                    i14 = -5;
                                                                                                    audioRecoder = null;
                                                                                                    i46 = i45;
                                                                                                    mediaCodec3 = mediaCodec5;
                                                                                                    i28 = i46;
                                                                                                    j6 = j17;
                                                                                                    if (exc instanceof IllegalStateException) {
                                                                                                    }
                                                                                                    StringBuilder sb3222222222 = new StringBuilder();
                                                                                                    sb3222222222.append("bitrate: ");
                                                                                                    sb3222222222.append(i26);
                                                                                                    sb3222222222.append(" framerate: ");
                                                                                                    sb3222222222.append(i28 == 1 ? 1 : 0);
                                                                                                    sb3222222222.append(" size: ");
                                                                                                    i21 = i5;
                                                                                                    sb3222222222.append(i21);
                                                                                                    sb3222222222.append("x");
                                                                                                    i19 = i4;
                                                                                                    sb3222222222.append(i19);
                                                                                                    FileLog.e(sb3222222222.toString());
                                                                                                    FileLog.e(exc);
                                                                                                    i22 = i26;
                                                                                                    mediaCodec4 = mediaCodec2;
                                                                                                    i29 = i14;
                                                                                                    z6 = z11;
                                                                                                    z13 = true;
                                                                                                    i30 = i28;
                                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                    if (mediaCodec4 != null) {
                                                                                                    }
                                                                                                    z11 = z6;
                                                                                                    z10 = z13;
                                                                                                    mediaCodec = mediaCodec3;
                                                                                                    outputSurface = outputSurface2;
                                                                                                    i14 = i29;
                                                                                                    i20 = i30;
                                                                                                    if (outputSurface != null) {
                                                                                                    }
                                                                                                    if (inputSurface != null) {
                                                                                                    }
                                                                                                    if (mediaCodec != null) {
                                                                                                    }
                                                                                                    if (audioRecoder != null) {
                                                                                                    }
                                                                                                    checkConversionCanceled();
                                                                                                    z12 = z10;
                                                                                                    z6 = z11;
                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                    if (mediaExtractor2 != null) {
                                                                                                    }
                                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                    if (mP4Builder2 != null) {
                                                                                                    }
                                                                                                    i17 = i21;
                                                                                                    i16 = i19;
                                                                                                    i18 = i22;
                                                                                                    z7 = z12;
                                                                                                    if (z6) {
                                                                                                    }
                                                                                                }
                                                                                            } catch (Throwable th42) {
                                                                                                th = th42;
                                                                                                j18 = j2;
                                                                                                i12 = i5;
                                                                                                i11 = i6;
                                                                                                j6 = j15;
                                                                                                th = th;
                                                                                                j5 = j18;
                                                                                                i111 = i22;
                                                                                                z6 = false;
                                                                                                i14 = -5;
                                                                                                i15 = i11;
                                                                                                FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                FileLog.e(th);
                                                                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                if (mediaExtractor != null) {
                                                                                                }
                                                                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                if (mP4Builder != null) {
                                                                                                }
                                                                                                i16 = i13;
                                                                                                i17 = i12;
                                                                                                i18 = i111;
                                                                                                z7 = true;
                                                                                                if (z6) {
                                                                                                }
                                                                                            }
                                                                                            try {
                                                                                                audioRecoder3.endTime = j18;
                                                                                                i50 = mediaCodecVideoConvertor2.mediaMuxer.addTrack(audioRecoder3.format, true);
                                                                                                z15 = z16;
                                                                                                byteBuffer = null;
                                                                                                audioRecoder2 = audioRecoder3;
                                                                                                i51 = 0;
                                                                                                j20 = j44;
                                                                                            } catch (Exception e39) {
                                                                                                e = e39;
                                                                                                i47 = i6;
                                                                                                j17 = j15;
                                                                                                exc = e;
                                                                                                audioRecoder = audioRecoder3;
                                                                                                j5 = j18;
                                                                                                outputSurface2 = outputSurface4;
                                                                                                i26 = i22;
                                                                                                i24 = i42;
                                                                                                i14 = -5;
                                                                                                i46 = i47;
                                                                                                mediaCodec3 = mediaCodec5;
                                                                                                i28 = i46;
                                                                                                j6 = j17;
                                                                                                if (exc instanceof IllegalStateException) {
                                                                                                }
                                                                                                StringBuilder sb32222222222 = new StringBuilder();
                                                                                                sb32222222222.append("bitrate: ");
                                                                                                sb32222222222.append(i26);
                                                                                                sb32222222222.append(" framerate: ");
                                                                                                sb32222222222.append(i28 == 1 ? 1 : 0);
                                                                                                sb32222222222.append(" size: ");
                                                                                                i21 = i5;
                                                                                                sb32222222222.append(i21);
                                                                                                sb32222222222.append("x");
                                                                                                i19 = i4;
                                                                                                sb32222222222.append(i19);
                                                                                                FileLog.e(sb32222222222.toString());
                                                                                                FileLog.e(exc);
                                                                                                i22 = i26;
                                                                                                mediaCodec4 = mediaCodec2;
                                                                                                i29 = i14;
                                                                                                z6 = z11;
                                                                                                z13 = true;
                                                                                                i30 = i28;
                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                if (mediaCodec4 != null) {
                                                                                                }
                                                                                                z11 = z6;
                                                                                                z10 = z13;
                                                                                                mediaCodec = mediaCodec3;
                                                                                                outputSurface = outputSurface2;
                                                                                                i14 = i29;
                                                                                                i20 = i30;
                                                                                                if (outputSurface != null) {
                                                                                                }
                                                                                                if (inputSurface != null) {
                                                                                                }
                                                                                                if (mediaCodec != null) {
                                                                                                }
                                                                                                if (audioRecoder != null) {
                                                                                                }
                                                                                                checkConversionCanceled();
                                                                                                z12 = z10;
                                                                                                z6 = z11;
                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                if (mediaExtractor2 != null) {
                                                                                                }
                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                if (mP4Builder2 != null) {
                                                                                                }
                                                                                                i17 = i21;
                                                                                                i16 = i19;
                                                                                                i18 = i22;
                                                                                                z7 = z12;
                                                                                                if (z6) {
                                                                                                }
                                                                                            } catch (Throwable th43) {
                                                                                                th = th43;
                                                                                                i12 = i5;
                                                                                                i11 = i6;
                                                                                                j6 = j15;
                                                                                                th = th;
                                                                                                j5 = j18;
                                                                                                i111 = i22;
                                                                                                z6 = false;
                                                                                                i14 = -5;
                                                                                                i15 = i11;
                                                                                                FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                FileLog.e(th);
                                                                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                if (mediaExtractor != null) {
                                                                                                }
                                                                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                if (mP4Builder != null) {
                                                                                                }
                                                                                                i16 = i13;
                                                                                                i17 = i12;
                                                                                                i18 = i111;
                                                                                                z7 = true;
                                                                                                if (z6) {
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        boolean z50 = i48 < 0;
                                                                                        checkConversionCanceled();
                                                                                        j21 = j15;
                                                                                        long j45 = -1;
                                                                                        long j46 = -1;
                                                                                        ByteBuffer byteBuffer7 = byteBuffer;
                                                                                        j22 = -2147483648L;
                                                                                        outputSurface2 = null;
                                                                                        z17 = false;
                                                                                        i29 = -5;
                                                                                        z18 = false;
                                                                                        boolean z51 = true;
                                                                                        long j47 = 0;
                                                                                        long j48 = 0;
                                                                                        boolean z52 = z50;
                                                                                        i54 = 0;
                                                                                        byteBufferArr2 = inputBuffers;
                                                                                        inputSurface2 = j20;
                                                                                        loop4: while (true) {
                                                                                            if (outputSurface2 != null || (!z15 && !z52)) {
                                                                                                try {
                                                                                                    checkConversionCanceled();
                                                                                                    if (!z15 || audioRecoder2 == null) {
                                                                                                        i57 = i54;
                                                                                                    } else {
                                                                                                        i57 = i54;
                                                                                                        try {
                                                                                                            try {
                                                                                                                z52 = audioRecoder2.step(mediaCodecVideoConvertor2.mediaMuxer, i50);
                                                                                                            } catch (Exception e40) {
                                                                                                                i46 = i6;
                                                                                                                j17 = j21;
                                                                                                                exc = e40;
                                                                                                                audioRecoder = audioRecoder2;
                                                                                                                j5 = j18;
                                                                                                                outputSurface2 = outputSurface4;
                                                                                                                i26 = i22;
                                                                                                                i14 = i29;
                                                                                                                i24 = i42;
                                                                                                                mediaCodec3 = mediaCodec5;
                                                                                                                i28 = i46;
                                                                                                                j6 = j17;
                                                                                                                if (exc instanceof IllegalStateException) {
                                                                                                                }
                                                                                                                StringBuilder sb322222222222 = new StringBuilder();
                                                                                                                sb322222222222.append("bitrate: ");
                                                                                                                sb322222222222.append(i26);
                                                                                                                sb322222222222.append(" framerate: ");
                                                                                                                sb322222222222.append(i28 == 1 ? 1 : 0);
                                                                                                                sb322222222222.append(" size: ");
                                                                                                                i21 = i5;
                                                                                                                sb322222222222.append(i21);
                                                                                                                sb322222222222.append("x");
                                                                                                                i19 = i4;
                                                                                                                sb322222222222.append(i19);
                                                                                                                FileLog.e(sb322222222222.toString());
                                                                                                                FileLog.e(exc);
                                                                                                                i22 = i26;
                                                                                                                mediaCodec4 = mediaCodec2;
                                                                                                                i29 = i14;
                                                                                                                z6 = z11;
                                                                                                                z13 = true;
                                                                                                                i30 = i28;
                                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                if (mediaCodec4 != null) {
                                                                                                                }
                                                                                                                z11 = z6;
                                                                                                                z10 = z13;
                                                                                                                mediaCodec = mediaCodec3;
                                                                                                                outputSurface = outputSurface2;
                                                                                                                i14 = i29;
                                                                                                                i20 = i30;
                                                                                                                if (outputSurface != null) {
                                                                                                                }
                                                                                                                if (inputSurface != null) {
                                                                                                                }
                                                                                                                if (mediaCodec != null) {
                                                                                                                }
                                                                                                                if (audioRecoder != null) {
                                                                                                                }
                                                                                                                checkConversionCanceled();
                                                                                                                z12 = z10;
                                                                                                                z6 = z11;
                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                }
                                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                if (mP4Builder2 != null) {
                                                                                                                }
                                                                                                                i17 = i21;
                                                                                                                i16 = i19;
                                                                                                                i18 = i22;
                                                                                                                z7 = z12;
                                                                                                                if (z6) {
                                                                                                                }
                                                                                                            }
                                                                                                        } catch (Throwable th44) {
                                                                                                            i12 = i5;
                                                                                                            i56 = i6;
                                                                                                            j23 = j21;
                                                                                                            th = th44;
                                                                                                            j5 = j18;
                                                                                                            i111 = i22;
                                                                                                            i14 = i29;
                                                                                                            i91 = i56;
                                                                                                            j6 = j23;
                                                                                                            z6 = false;
                                                                                                            i15 = i91;
                                                                                                            FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                            FileLog.e(th);
                                                                                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                            if (mediaExtractor != null) {
                                                                                                            }
                                                                                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                            if (mP4Builder != null) {
                                                                                                            }
                                                                                                            i16 = i13;
                                                                                                            i17 = i12;
                                                                                                            i18 = i111;
                                                                                                            z7 = true;
                                                                                                            if (z6) {
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                    if (z18) {
                                                                                                        i59 = i50;
                                                                                                        audioRecoder = audioRecoder2;
                                                                                                        i60 = i48;
                                                                                                        j5 = j18;
                                                                                                        i24 = i42;
                                                                                                        bufferInfo2 = bufferInfo;
                                                                                                        j24 = 2500;
                                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                                        z19 = z15;
                                                                                                        j25 = inputSurface2;
                                                                                                    } else {
                                                                                                        try {
                                                                                                            int sampleTrackIndex = mediaCodecVideoConvertor2.extractor.getSampleTrackIndex();
                                                                                                            audioRecoder = audioRecoder2;
                                                                                                            int i125 = i42;
                                                                                                            if (sampleTrackIndex == i125) {
                                                                                                                ?? r4 = 2500;
                                                                                                                try {
                                                                                                                    int dequeueInputBuffer = mediaCodec2.dequeueInputBuffer(2500L);
                                                                                                                    if (dequeueInputBuffer >= 0) {
                                                                                                                        if (Build.VERSION.SDK_INT < 21) {
                                                                                                                            byteBuffer2 = byteBufferArr2[dequeueInputBuffer];
                                                                                                                        } else {
                                                                                                                            byteBuffer2 = mediaCodec2.getInputBuffer(dequeueInputBuffer);
                                                                                                                        }
                                                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                                                        int readSampleData = mediaCodecVideoConvertor2.extractor.readSampleData(byteBuffer2, 0);
                                                                                                                        if (readSampleData < 0) {
                                                                                                                            j26 = dequeueInputBuffer;
                                                                                                                            mediaCodec2.queueInputBuffer(j26, 0, 0, 0L, 4);
                                                                                                                            z18 = true;
                                                                                                                            r4 = byteBuffer2;
                                                                                                                        } else {
                                                                                                                            j26 = dequeueInputBuffer;
                                                                                                                            mediaCodec2.queueInputBuffer(j26, 0, readSampleData, mediaCodecVideoConvertor2.extractor.getSampleTime(), 0);
                                                                                                                            mediaCodecVideoConvertor2.extractor.advance();
                                                                                                                            r4 = byteBuffer2;
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                                                    }
                                                                                                                    i59 = i50;
                                                                                                                    i24 = i125;
                                                                                                                    i60 = i48;
                                                                                                                    j5 = j18;
                                                                                                                    bufferInfo2 = bufferInfo;
                                                                                                                    j24 = 2500;
                                                                                                                    z20 = false;
                                                                                                                    z19 = z15;
                                                                                                                    j25 = j;
                                                                                                                    inputSurface2 = r4;
                                                                                                                } catch (Exception e41) {
                                                                                                                    i62 = i6;
                                                                                                                    j17 = j21;
                                                                                                                    exc = e41;
                                                                                                                    i24 = i125;
                                                                                                                    j5 = j18;
                                                                                                                    i58 = i62;
                                                                                                                    outputSurface2 = outputSurface4;
                                                                                                                    i26 = i22;
                                                                                                                    i14 = i29;
                                                                                                                    i46 = i58;
                                                                                                                    mediaCodec3 = mediaCodec5;
                                                                                                                    i28 = i46;
                                                                                                                    j6 = j17;
                                                                                                                    if (exc instanceof IllegalStateException) {
                                                                                                                    }
                                                                                                                    StringBuilder sb3222222222222 = new StringBuilder();
                                                                                                                    sb3222222222222.append("bitrate: ");
                                                                                                                    sb3222222222222.append(i26);
                                                                                                                    sb3222222222222.append(" framerate: ");
                                                                                                                    sb3222222222222.append(i28 == 1 ? 1 : 0);
                                                                                                                    sb3222222222222.append(" size: ");
                                                                                                                    i21 = i5;
                                                                                                                    sb3222222222222.append(i21);
                                                                                                                    sb3222222222222.append("x");
                                                                                                                    i19 = i4;
                                                                                                                    sb3222222222222.append(i19);
                                                                                                                    FileLog.e(sb3222222222222.toString());
                                                                                                                    FileLog.e(exc);
                                                                                                                    i22 = i26;
                                                                                                                    mediaCodec4 = mediaCodec2;
                                                                                                                    i29 = i14;
                                                                                                                    z6 = z11;
                                                                                                                    z13 = true;
                                                                                                                    i30 = i28;
                                                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                    }
                                                                                                                    z11 = z6;
                                                                                                                    z10 = z13;
                                                                                                                    mediaCodec = mediaCodec3;
                                                                                                                    outputSurface = outputSurface2;
                                                                                                                    i14 = i29;
                                                                                                                    i20 = i30;
                                                                                                                    if (outputSurface != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec != null) {
                                                                                                                    }
                                                                                                                    if (audioRecoder != null) {
                                                                                                                    }
                                                                                                                    checkConversionCanceled();
                                                                                                                    z12 = z10;
                                                                                                                    z6 = z11;
                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                    }
                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                    }
                                                                                                                    i17 = i21;
                                                                                                                    i16 = i19;
                                                                                                                    i18 = i22;
                                                                                                                    z7 = z12;
                                                                                                                    if (z6) {
                                                                                                                    }
                                                                                                                }
                                                                                                            } else {
                                                                                                                byteBufferArr3 = byteBufferArr2;
                                                                                                                if (z15) {
                                                                                                                    i61 = -1;
                                                                                                                    if (i48 == -1) {
                                                                                                                        i59 = i50;
                                                                                                                        i24 = i125;
                                                                                                                        i60 = i48;
                                                                                                                        j5 = j18;
                                                                                                                        bufferInfo2 = bufferInfo;
                                                                                                                        j24 = 2500;
                                                                                                                        z19 = z15;
                                                                                                                        j25 = j;
                                                                                                                        if (sampleTrackIndex == i61) {
                                                                                                                            z20 = true;
                                                                                                                            inputSurface2 = inputSurface2;
                                                                                                                        }
                                                                                                                        z20 = false;
                                                                                                                        inputSurface2 = inputSurface2;
                                                                                                                    } else if (sampleTrackIndex == i48) {
                                                                                                                        try {
                                                                                                                            try {
                                                                                                                                int i126 = Build.VERSION.SDK_INT;
                                                                                                                                if (i126 >= 28) {
                                                                                                                                    try {
                                                                                                                                        long sampleSize = mediaCodecVideoConvertor2.extractor.getSampleSize();
                                                                                                                                        i24 = i125;
                                                                                                                                        i60 = i48;
                                                                                                                                        if (sampleSize > i51) {
                                                                                                                                            i51 = (int) (sampleSize + 1024);
                                                                                                                                            try {
                                                                                                                                                byteBuffer7 = ByteBuffer.allocateDirect(i51);
                                                                                                                                            } catch (Exception e42) {
                                                                                                                                                e = e42;
                                                                                                                                                i62 = i6;
                                                                                                                                                j17 = j21;
                                                                                                                                                exc = e;
                                                                                                                                                j5 = j18;
                                                                                                                                                i58 = i62;
                                                                                                                                                outputSurface2 = outputSurface4;
                                                                                                                                                i26 = i22;
                                                                                                                                                i14 = i29;
                                                                                                                                                i46 = i58;
                                                                                                                                                mediaCodec3 = mediaCodec5;
                                                                                                                                                i28 = i46;
                                                                                                                                                j6 = j17;
                                                                                                                                                if (exc instanceof IllegalStateException) {
                                                                                                                                                }
                                                                                                                                                StringBuilder sb32222222222222 = new StringBuilder();
                                                                                                                                                sb32222222222222.append("bitrate: ");
                                                                                                                                                sb32222222222222.append(i26);
                                                                                                                                                sb32222222222222.append(" framerate: ");
                                                                                                                                                sb32222222222222.append(i28 == 1 ? 1 : 0);
                                                                                                                                                sb32222222222222.append(" size: ");
                                                                                                                                                i21 = i5;
                                                                                                                                                sb32222222222222.append(i21);
                                                                                                                                                sb32222222222222.append("x");
                                                                                                                                                i19 = i4;
                                                                                                                                                sb32222222222222.append(i19);
                                                                                                                                                FileLog.e(sb32222222222222.toString());
                                                                                                                                                FileLog.e(exc);
                                                                                                                                                i22 = i26;
                                                                                                                                                mediaCodec4 = mediaCodec2;
                                                                                                                                                i29 = i14;
                                                                                                                                                z6 = z11;
                                                                                                                                                z13 = true;
                                                                                                                                                i30 = i28;
                                                                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                                }
                                                                                                                                                z11 = z6;
                                                                                                                                                z10 = z13;
                                                                                                                                                mediaCodec = mediaCodec3;
                                                                                                                                                outputSurface = outputSurface2;
                                                                                                                                                i14 = i29;
                                                                                                                                                i20 = i30;
                                                                                                                                                if (outputSurface != null) {
                                                                                                                                                }
                                                                                                                                                if (inputSurface != null) {
                                                                                                                                                }
                                                                                                                                                if (mediaCodec != null) {
                                                                                                                                                }
                                                                                                                                                if (audioRecoder != null) {
                                                                                                                                                }
                                                                                                                                                checkConversionCanceled();
                                                                                                                                                z12 = z10;
                                                                                                                                                z6 = z11;
                                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                }
                                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                }
                                                                                                                                                i17 = i21;
                                                                                                                                                i16 = i19;
                                                                                                                                                i18 = i22;
                                                                                                                                                z7 = z12;
                                                                                                                                                if (z6) {
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    } catch (Exception e43) {
                                                                                                                                        e = e43;
                                                                                                                                        i24 = i125;
                                                                                                                                    }
                                                                                                                                } else {
                                                                                                                                    i24 = i125;
                                                                                                                                    i60 = i48;
                                                                                                                                }
                                                                                                                                inputSurface2 = byteBuffer7;
                                                                                                                                try {
                                                                                                                                    bufferInfo2 = bufferInfo;
                                                                                                                                    bufferInfo2.size = mediaCodecVideoConvertor2.extractor.readSampleData(inputSurface2, 0);
                                                                                                                                    if (i126 < 21) {
                                                                                                                                        inputSurface2.position(0);
                                                                                                                                        inputSurface2.limit(bufferInfo2.size);
                                                                                                                                    }
                                                                                                                                    if (bufferInfo2.size >= 0) {
                                                                                                                                        i63 = i51;
                                                                                                                                        bufferInfo2.presentationTimeUs = mediaCodecVideoConvertor2.extractor.getSampleTime();
                                                                                                                                        mediaCodecVideoConvertor2.extractor.advance();
                                                                                                                                    } else {
                                                                                                                                        i63 = i51;
                                                                                                                                        bufferInfo2.size = 0;
                                                                                                                                        z18 = true;
                                                                                                                                    }
                                                                                                                                    if (bufferInfo2.size > 0 && (j18 < 0 || bufferInfo2.presentationTimeUs < j18)) {
                                                                                                                                        bufferInfo2.offset = 0;
                                                                                                                                        bufferInfo2.flags = mediaCodecVideoConvertor2.extractor.getSampleFlags();
                                                                                                                                        long writeSampleData2 = mediaCodecVideoConvertor2.mediaMuxer.writeSampleData(i50, inputSurface2, bufferInfo2, false);
                                                                                                                                        if (writeSampleData2 != 0 && (videoConvertorListener = mediaCodecVideoConvertor2.callback) != null) {
                                                                                                                                            j5 = j18;
                                                                                                                                            try {
                                                                                                                                                long j49 = bufferInfo2.presentationTimeUs;
                                                                                                                                                i59 = i50;
                                                                                                                                                z19 = z15;
                                                                                                                                                j24 = 2500;
                                                                                                                                                j25 = j;
                                                                                                                                                if (j49 - j25 > j47) {
                                                                                                                                                    j47 = j49 - j25;
                                                                                                                                                }
                                                                                                                                                long j50 = j47;
                                                                                                                                                videoConvertorListener.didWriteData(writeSampleData2, (((float) j50) / 1000.0f) / f);
                                                                                                                                                j47 = j50;
                                                                                                                                                i51 = i63;
                                                                                                                                                byteBuffer7 = inputSurface2;
                                                                                                                                                z20 = false;
                                                                                                                                                inputSurface2 = inputSurface2;
                                                                                                                                            } catch (Exception e44) {
                                                                                                                                                e = e44;
                                                                                                                                                i58 = i6;
                                                                                                                                                j17 = j21;
                                                                                                                                                exc = e;
                                                                                                                                                outputSurface2 = outputSurface4;
                                                                                                                                                i26 = i22;
                                                                                                                                                i14 = i29;
                                                                                                                                                i46 = i58;
                                                                                                                                                mediaCodec3 = mediaCodec5;
                                                                                                                                                i28 = i46;
                                                                                                                                                j6 = j17;
                                                                                                                                                if (exc instanceof IllegalStateException) {
                                                                                                                                                }
                                                                                                                                                StringBuilder sb322222222222222 = new StringBuilder();
                                                                                                                                                sb322222222222222.append("bitrate: ");
                                                                                                                                                sb322222222222222.append(i26);
                                                                                                                                                sb322222222222222.append(" framerate: ");
                                                                                                                                                sb322222222222222.append(i28 == 1 ? 1 : 0);
                                                                                                                                                sb322222222222222.append(" size: ");
                                                                                                                                                i21 = i5;
                                                                                                                                                sb322222222222222.append(i21);
                                                                                                                                                sb322222222222222.append("x");
                                                                                                                                                i19 = i4;
                                                                                                                                                sb322222222222222.append(i19);
                                                                                                                                                FileLog.e(sb322222222222222.toString());
                                                                                                                                                FileLog.e(exc);
                                                                                                                                                i22 = i26;
                                                                                                                                                mediaCodec4 = mediaCodec2;
                                                                                                                                                i29 = i14;
                                                                                                                                                z6 = z11;
                                                                                                                                                z13 = true;
                                                                                                                                                i30 = i28;
                                                                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                                }
                                                                                                                                                z11 = z6;
                                                                                                                                                z10 = z13;
                                                                                                                                                mediaCodec = mediaCodec3;
                                                                                                                                                outputSurface = outputSurface2;
                                                                                                                                                i14 = i29;
                                                                                                                                                i20 = i30;
                                                                                                                                                if (outputSurface != null) {
                                                                                                                                                }
                                                                                                                                                if (inputSurface != null) {
                                                                                                                                                }
                                                                                                                                                if (mediaCodec != null) {
                                                                                                                                                }
                                                                                                                                                if (audioRecoder != null) {
                                                                                                                                                }
                                                                                                                                                checkConversionCanceled();
                                                                                                                                                z12 = z10;
                                                                                                                                                z6 = z11;
                                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                }
                                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                }
                                                                                                                                                i17 = i21;
                                                                                                                                                i16 = i19;
                                                                                                                                                i18 = i22;
                                                                                                                                                z7 = z12;
                                                                                                                                                if (z6) {
                                                                                                                                                }
                                                                                                                                            } catch (Throwable th45) {
                                                                                                                                                th = th45;
                                                                                                                                                i13 = i4;
                                                                                                                                                i12 = i5;
                                                                                                                                                i55 = i6;
                                                                                                                                                j23 = j21;
                                                                                                                                                th = th;
                                                                                                                                                i56 = i55;
                                                                                                                                                i111 = i22;
                                                                                                                                                i14 = i29;
                                                                                                                                                i91 = i56;
                                                                                                                                                j6 = j23;
                                                                                                                                                z6 = false;
                                                                                                                                                i15 = i91;
                                                                                                                                                FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                                                                FileLog.e(th);
                                                                                                                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                if (mediaExtractor != null) {
                                                                                                                                                }
                                                                                                                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                if (mP4Builder != null) {
                                                                                                                                                }
                                                                                                                                                i16 = i13;
                                                                                                                                                i17 = i12;
                                                                                                                                                i18 = i111;
                                                                                                                                                z7 = true;
                                                                                                                                                if (z6) {
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    i59 = i50;
                                                                                                                                    j5 = j18;
                                                                                                                                    z19 = z15;
                                                                                                                                    j24 = 2500;
                                                                                                                                    j25 = j;
                                                                                                                                    i51 = i63;
                                                                                                                                    byteBuffer7 = inputSurface2;
                                                                                                                                    z20 = false;
                                                                                                                                    inputSurface2 = inputSurface2;
                                                                                                                                } catch (Exception e45) {
                                                                                                                                    e = e45;
                                                                                                                                    j5 = j18;
                                                                                                                                    i58 = i6;
                                                                                                                                    j17 = j21;
                                                                                                                                    exc = e;
                                                                                                                                    outputSurface2 = outputSurface4;
                                                                                                                                    i26 = i22;
                                                                                                                                    i14 = i29;
                                                                                                                                    i46 = i58;
                                                                                                                                    mediaCodec3 = mediaCodec5;
                                                                                                                                    i28 = i46;
                                                                                                                                    j6 = j17;
                                                                                                                                    if (exc instanceof IllegalStateException) {
                                                                                                                                    }
                                                                                                                                    StringBuilder sb3222222222222222 = new StringBuilder();
                                                                                                                                    sb3222222222222222.append("bitrate: ");
                                                                                                                                    sb3222222222222222.append(i26);
                                                                                                                                    sb3222222222222222.append(" framerate: ");
                                                                                                                                    sb3222222222222222.append(i28 == 1 ? 1 : 0);
                                                                                                                                    sb3222222222222222.append(" size: ");
                                                                                                                                    i21 = i5;
                                                                                                                                    sb3222222222222222.append(i21);
                                                                                                                                    sb3222222222222222.append("x");
                                                                                                                                    i19 = i4;
                                                                                                                                    sb3222222222222222.append(i19);
                                                                                                                                    FileLog.e(sb3222222222222222.toString());
                                                                                                                                    FileLog.e(exc);
                                                                                                                                    i22 = i26;
                                                                                                                                    mediaCodec4 = mediaCodec2;
                                                                                                                                    i29 = i14;
                                                                                                                                    z6 = z11;
                                                                                                                                    z13 = true;
                                                                                                                                    i30 = i28;
                                                                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                                    }
                                                                                                                                    z11 = z6;
                                                                                                                                    z10 = z13;
                                                                                                                                    mediaCodec = mediaCodec3;
                                                                                                                                    outputSurface = outputSurface2;
                                                                                                                                    i14 = i29;
                                                                                                                                    i20 = i30;
                                                                                                                                    if (outputSurface != null) {
                                                                                                                                    }
                                                                                                                                    if (inputSurface != null) {
                                                                                                                                    }
                                                                                                                                    if (mediaCodec != null) {
                                                                                                                                    }
                                                                                                                                    if (audioRecoder != null) {
                                                                                                                                    }
                                                                                                                                    checkConversionCanceled();
                                                                                                                                    z12 = z10;
                                                                                                                                    z6 = z11;
                                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                    }
                                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                    }
                                                                                                                                    i17 = i21;
                                                                                                                                    i16 = i19;
                                                                                                                                    i18 = i22;
                                                                                                                                    z7 = z12;
                                                                                                                                    if (z6) {
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            } catch (Exception e46) {
                                                                                                                                e = e46;
                                                                                                                                i24 = i125;
                                                                                                                            }
                                                                                                                        } catch (Throwable th46) {
                                                                                                                            th = th46;
                                                                                                                            j5 = j18;
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                                i59 = i50;
                                                                                                                i24 = i125;
                                                                                                                i60 = i48;
                                                                                                                j5 = j18;
                                                                                                                bufferInfo2 = bufferInfo;
                                                                                                                j24 = 2500;
                                                                                                                z19 = z15;
                                                                                                                j25 = j;
                                                                                                                i61 = -1;
                                                                                                                if (sampleTrackIndex == i61) {
                                                                                                                }
                                                                                                                z20 = false;
                                                                                                                inputSurface2 = inputSurface2;
                                                                                                            }
                                                                                                            if (z20) {
                                                                                                                try {
                                                                                                                    j26 = mediaCodec2.dequeueInputBuffer(j24);
                                                                                                                    if (j26 >= 0) {
                                                                                                                        mediaCodec2.queueInputBuffer(j26, 0, 0, 0L, 4);
                                                                                                                        z18 = true;
                                                                                                                    }
                                                                                                                } catch (Exception e47) {
                                                                                                                    e = e47;
                                                                                                                    i58 = i6;
                                                                                                                    j17 = j21;
                                                                                                                    exc = e;
                                                                                                                    outputSurface2 = outputSurface4;
                                                                                                                    i26 = i22;
                                                                                                                    i14 = i29;
                                                                                                                    i46 = i58;
                                                                                                                    mediaCodec3 = mediaCodec5;
                                                                                                                    i28 = i46;
                                                                                                                    j6 = j17;
                                                                                                                    if (exc instanceof IllegalStateException) {
                                                                                                                    }
                                                                                                                    StringBuilder sb32222222222222222 = new StringBuilder();
                                                                                                                    sb32222222222222222.append("bitrate: ");
                                                                                                                    sb32222222222222222.append(i26);
                                                                                                                    sb32222222222222222.append(" framerate: ");
                                                                                                                    sb32222222222222222.append(i28 == 1 ? 1 : 0);
                                                                                                                    sb32222222222222222.append(" size: ");
                                                                                                                    i21 = i5;
                                                                                                                    sb32222222222222222.append(i21);
                                                                                                                    sb32222222222222222.append("x");
                                                                                                                    i19 = i4;
                                                                                                                    sb32222222222222222.append(i19);
                                                                                                                    FileLog.e(sb32222222222222222.toString());
                                                                                                                    FileLog.e(exc);
                                                                                                                    i22 = i26;
                                                                                                                    mediaCodec4 = mediaCodec2;
                                                                                                                    i29 = i14;
                                                                                                                    z6 = z11;
                                                                                                                    z13 = true;
                                                                                                                    i30 = i28;
                                                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                    }
                                                                                                                    z11 = z6;
                                                                                                                    z10 = z13;
                                                                                                                    mediaCodec = mediaCodec3;
                                                                                                                    outputSurface = outputSurface2;
                                                                                                                    i14 = i29;
                                                                                                                    i20 = i30;
                                                                                                                    if (outputSurface != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec != null) {
                                                                                                                    }
                                                                                                                    if (audioRecoder != null) {
                                                                                                                    }
                                                                                                                    checkConversionCanceled();
                                                                                                                    z12 = z10;
                                                                                                                    z6 = z11;
                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                    }
                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                    }
                                                                                                                    i17 = i21;
                                                                                                                    i16 = i19;
                                                                                                                    i18 = i22;
                                                                                                                    z7 = z12;
                                                                                                                    if (z6) {
                                                                                                                    }
                                                                                                                } catch (Throwable th47) {
                                                                                                                    th = th47;
                                                                                                                    i13 = i4;
                                                                                                                    i12 = i5;
                                                                                                                    i55 = i6;
                                                                                                                    j23 = j21;
                                                                                                                    th = th;
                                                                                                                    i56 = i55;
                                                                                                                    i111 = i22;
                                                                                                                    i14 = i29;
                                                                                                                    i91 = i56;
                                                                                                                    j6 = j23;
                                                                                                                    z6 = false;
                                                                                                                    i15 = i91;
                                                                                                                    FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                                    FileLog.e(th);
                                                                                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                    if (mediaExtractor != null) {
                                                                                                                    }
                                                                                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                    if (mP4Builder != null) {
                                                                                                                    }
                                                                                                                    i16 = i13;
                                                                                                                    i17 = i12;
                                                                                                                    i18 = i111;
                                                                                                                    z7 = true;
                                                                                                                    if (z6) {
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        } catch (Exception e48) {
                                                                                                            e = e48;
                                                                                                            audioRecoder = audioRecoder2;
                                                                                                            j5 = j18;
                                                                                                            i24 = i42;
                                                                                                        } catch (Throwable th48) {
                                                                                                            th = th48;
                                                                                                            j5 = j18;
                                                                                                        }
                                                                                                    }
                                                                                                    i54 = i57;
                                                                                                    z21 = !z17;
                                                                                                    i64 = i29;
                                                                                                    j27 = j22;
                                                                                                    z22 = true;
                                                                                                    j28 = j21;
                                                                                                } catch (Exception e49) {
                                                                                                    i40 = i6;
                                                                                                    createEncoderByType = mediaCodec5;
                                                                                                    audioRecoder = audioRecoder2;
                                                                                                    j5 = j18;
                                                                                                    outputSurface2 = outputSurface4;
                                                                                                    i24 = i42;
                                                                                                    j16 = j21;
                                                                                                    exc = e49;
                                                                                                    i26 = i22;
                                                                                                    i14 = i29;
                                                                                                } catch (Throwable th49) {
                                                                                                    th = th49;
                                                                                                    i55 = i6;
                                                                                                    j5 = j18;
                                                                                                    i13 = i4;
                                                                                                    i12 = i5;
                                                                                                }
                                                                                                while (true) {
                                                                                                    if (!z21 || z22) {
                                                                                                        try {
                                                                                                            try {
                                                                                                                checkConversionCanceled();
                                                                                                                if (z3) {
                                                                                                                    j24 = 22000;
                                                                                                                }
                                                                                                                z23 = z21;
                                                                                                                createEncoderByType = mediaCodec5;
                                                                                                            } catch (Exception e50) {
                                                                                                                e = e50;
                                                                                                                i66 = i6;
                                                                                                                createEncoderByType = mediaCodec5;
                                                                                                            }
                                                                                                            try {
                                                                                                                dequeueOutputBuffer = createEncoderByType.dequeueOutputBuffer(bufferInfo2, j24);
                                                                                                                i71 = -1;
                                                                                                            } catch (Exception e51) {
                                                                                                                e = e51;
                                                                                                                i66 = i6;
                                                                                                                j26 = j28;
                                                                                                                outputSurface2 = outputSurface4;
                                                                                                                i67 = i66;
                                                                                                                exc = e;
                                                                                                                i14 = i64;
                                                                                                                i70 = i67;
                                                                                                                j16 = j26;
                                                                                                                i26 = i22;
                                                                                                                i40 = i70;
                                                                                                                mediaCodec3 = createEncoderByType;
                                                                                                                i28 = i40;
                                                                                                                j6 = j16;
                                                                                                                if (exc instanceof IllegalStateException) {
                                                                                                                }
                                                                                                                StringBuilder sb322222222222222222 = new StringBuilder();
                                                                                                                sb322222222222222222.append("bitrate: ");
                                                                                                                sb322222222222222222.append(i26);
                                                                                                                sb322222222222222222.append(" framerate: ");
                                                                                                                sb322222222222222222.append(i28 == 1 ? 1 : 0);
                                                                                                                sb322222222222222222.append(" size: ");
                                                                                                                i21 = i5;
                                                                                                                sb322222222222222222.append(i21);
                                                                                                                sb322222222222222222.append("x");
                                                                                                                i19 = i4;
                                                                                                                sb322222222222222222.append(i19);
                                                                                                                FileLog.e(sb322222222222222222.toString());
                                                                                                                FileLog.e(exc);
                                                                                                                i22 = i26;
                                                                                                                mediaCodec4 = mediaCodec2;
                                                                                                                i29 = i14;
                                                                                                                z6 = z11;
                                                                                                                z13 = true;
                                                                                                                i30 = i28;
                                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                if (mediaCodec4 != null) {
                                                                                                                }
                                                                                                                z11 = z6;
                                                                                                                z10 = z13;
                                                                                                                mediaCodec = mediaCodec3;
                                                                                                                outputSurface = outputSurface2;
                                                                                                                i14 = i29;
                                                                                                                i20 = i30;
                                                                                                                if (outputSurface != null) {
                                                                                                                }
                                                                                                                if (inputSurface != null) {
                                                                                                                }
                                                                                                                if (mediaCodec != null) {
                                                                                                                }
                                                                                                                if (audioRecoder != null) {
                                                                                                                }
                                                                                                                checkConversionCanceled();
                                                                                                                z12 = z10;
                                                                                                                z6 = z11;
                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                }
                                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                if (mP4Builder2 != null) {
                                                                                                                }
                                                                                                                i17 = i21;
                                                                                                                i16 = i19;
                                                                                                                i18 = i22;
                                                                                                                z7 = z12;
                                                                                                                if (z6) {
                                                                                                                }
                                                                                                            }
                                                                                                            if (dequeueOutputBuffer == -1) {
                                                                                                                j32 = j28;
                                                                                                                i73 = i51;
                                                                                                                i74 = i54;
                                                                                                                i75 = i41;
                                                                                                                str7 = str4;
                                                                                                                i76 = i43;
                                                                                                                str8 = str6;
                                                                                                                z22 = false;
                                                                                                            } else if (dequeueOutputBuffer == -3) {
                                                                                                                try {
                                                                                                                    i73 = i51;
                                                                                                                    if (Build.VERSION.SDK_INT < 21) {
                                                                                                                        byteBufferArr = createEncoderByType.getOutputBuffers();
                                                                                                                    }
                                                                                                                    j32 = j28;
                                                                                                                    i74 = i54;
                                                                                                                    i75 = i41;
                                                                                                                    str7 = str4;
                                                                                                                    i76 = i43;
                                                                                                                    str8 = str6;
                                                                                                                    i71 = -1;
                                                                                                                } catch (Exception e52) {
                                                                                                                    i72 = i6;
                                                                                                                    exc = e52;
                                                                                                                    j31 = j28;
                                                                                                                    i14 = i64;
                                                                                                                    outputSurface2 = outputSurface4;
                                                                                                                    i70 = i72;
                                                                                                                    j16 = j31;
                                                                                                                    i26 = i22;
                                                                                                                    i40 = i70;
                                                                                                                    mediaCodec3 = createEncoderByType;
                                                                                                                    i28 = i40;
                                                                                                                    j6 = j16;
                                                                                                                    if (exc instanceof IllegalStateException) {
                                                                                                                    }
                                                                                                                    StringBuilder sb3222222222222222222 = new StringBuilder();
                                                                                                                    sb3222222222222222222.append("bitrate: ");
                                                                                                                    sb3222222222222222222.append(i26);
                                                                                                                    sb3222222222222222222.append(" framerate: ");
                                                                                                                    sb3222222222222222222.append(i28 == 1 ? 1 : 0);
                                                                                                                    sb3222222222222222222.append(" size: ");
                                                                                                                    i21 = i5;
                                                                                                                    sb3222222222222222222.append(i21);
                                                                                                                    sb3222222222222222222.append("x");
                                                                                                                    i19 = i4;
                                                                                                                    sb3222222222222222222.append(i19);
                                                                                                                    FileLog.e(sb3222222222222222222.toString());
                                                                                                                    FileLog.e(exc);
                                                                                                                    i22 = i26;
                                                                                                                    mediaCodec4 = mediaCodec2;
                                                                                                                    i29 = i14;
                                                                                                                    z6 = z11;
                                                                                                                    z13 = true;
                                                                                                                    i30 = i28;
                                                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                    }
                                                                                                                    z11 = z6;
                                                                                                                    z10 = z13;
                                                                                                                    mediaCodec = mediaCodec3;
                                                                                                                    outputSurface = outputSurface2;
                                                                                                                    i14 = i29;
                                                                                                                    i20 = i30;
                                                                                                                    if (outputSurface != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec != null) {
                                                                                                                    }
                                                                                                                    if (audioRecoder != null) {
                                                                                                                    }
                                                                                                                    checkConversionCanceled();
                                                                                                                    z12 = z10;
                                                                                                                    z6 = z11;
                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                    }
                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                    }
                                                                                                                    i17 = i21;
                                                                                                                    i16 = i19;
                                                                                                                    i18 = i22;
                                                                                                                    z7 = z12;
                                                                                                                    if (z6) {
                                                                                                                    }
                                                                                                                } catch (Throwable th50) {
                                                                                                                    i13 = i4;
                                                                                                                    i32 = i6;
                                                                                                                    th = th50;
                                                                                                                    j6 = j28;
                                                                                                                    i14 = i64;
                                                                                                                    i111 = i22;
                                                                                                                    z6 = false;
                                                                                                                    i12 = i5;
                                                                                                                    i15 = i32;
                                                                                                                    FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                                    FileLog.e(th);
                                                                                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                    if (mediaExtractor != null) {
                                                                                                                    }
                                                                                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                    if (mP4Builder != null) {
                                                                                                                    }
                                                                                                                    i16 = i13;
                                                                                                                    i17 = i12;
                                                                                                                    i18 = i111;
                                                                                                                    z7 = true;
                                                                                                                    if (z6) {
                                                                                                                    }
                                                                                                                }
                                                                                                            } else {
                                                                                                                i73 = i51;
                                                                                                                if (dequeueOutputBuffer == -2) {
                                                                                                                    MediaFormat outputFormat2 = createEncoderByType.getOutputFormat();
                                                                                                                    if (i64 != -5 || outputFormat2 == null) {
                                                                                                                        z24 = z22;
                                                                                                                        str9 = str5;
                                                                                                                        str8 = str6;
                                                                                                                    } else {
                                                                                                                        z24 = z22;
                                                                                                                        int addTrack2 = mediaCodecVideoConvertor2.mediaMuxer.addTrack(outputFormat2, false);
                                                                                                                        str9 = str5;
                                                                                                                        try {
                                                                                                                            if (outputFormat2.containsKey(str9)) {
                                                                                                                                i80 = addTrack2;
                                                                                                                                if (outputFormat2.getInteger(str9) == 1) {
                                                                                                                                    str8 = str6;
                                                                                                                                    try {
                                                                                                                                        i54 = outputFormat2.getByteBuffer(str8).limit() + outputFormat2.getByteBuffer("csd-1").limit();
                                                                                                                                        i64 = i80;
                                                                                                                                    } catch (Exception e53) {
                                                                                                                                        e = e53;
                                                                                                                                        i72 = i6;
                                                                                                                                        i14 = i80;
                                                                                                                                        exc = e;
                                                                                                                                        j31 = j28;
                                                                                                                                        outputSurface2 = outputSurface4;
                                                                                                                                        i70 = i72;
                                                                                                                                        j16 = j31;
                                                                                                                                        i26 = i22;
                                                                                                                                        i40 = i70;
                                                                                                                                        mediaCodec3 = createEncoderByType;
                                                                                                                                        i28 = i40;
                                                                                                                                        j6 = j16;
                                                                                                                                        if (exc instanceof IllegalStateException) {
                                                                                                                                        }
                                                                                                                                        StringBuilder sb32222222222222222222 = new StringBuilder();
                                                                                                                                        sb32222222222222222222.append("bitrate: ");
                                                                                                                                        sb32222222222222222222.append(i26);
                                                                                                                                        sb32222222222222222222.append(" framerate: ");
                                                                                                                                        sb32222222222222222222.append(i28 == 1 ? 1 : 0);
                                                                                                                                        sb32222222222222222222.append(" size: ");
                                                                                                                                        i21 = i5;
                                                                                                                                        sb32222222222222222222.append(i21);
                                                                                                                                        sb32222222222222222222.append("x");
                                                                                                                                        i19 = i4;
                                                                                                                                        sb32222222222222222222.append(i19);
                                                                                                                                        FileLog.e(sb32222222222222222222.toString());
                                                                                                                                        FileLog.e(exc);
                                                                                                                                        i22 = i26;
                                                                                                                                        mediaCodec4 = mediaCodec2;
                                                                                                                                        i29 = i14;
                                                                                                                                        z6 = z11;
                                                                                                                                        z13 = true;
                                                                                                                                        i30 = i28;
                                                                                                                                        mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                        if (mediaCodec4 != null) {
                                                                                                                                        }
                                                                                                                                        z11 = z6;
                                                                                                                                        z10 = z13;
                                                                                                                                        mediaCodec = mediaCodec3;
                                                                                                                                        outputSurface = outputSurface2;
                                                                                                                                        i14 = i29;
                                                                                                                                        i20 = i30;
                                                                                                                                        if (outputSurface != null) {
                                                                                                                                        }
                                                                                                                                        if (inputSurface != null) {
                                                                                                                                        }
                                                                                                                                        if (mediaCodec != null) {
                                                                                                                                        }
                                                                                                                                        if (audioRecoder != null) {
                                                                                                                                        }
                                                                                                                                        checkConversionCanceled();
                                                                                                                                        z12 = z10;
                                                                                                                                        z6 = z11;
                                                                                                                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                        if (mediaExtractor2 != null) {
                                                                                                                                        }
                                                                                                                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                        if (mP4Builder2 != null) {
                                                                                                                                        }
                                                                                                                                        i17 = i21;
                                                                                                                                        i16 = i19;
                                                                                                                                        i18 = i22;
                                                                                                                                        z7 = z12;
                                                                                                                                        if (z6) {
                                                                                                                                        }
                                                                                                                                    } catch (Throwable th51) {
                                                                                                                                        th = th51;
                                                                                                                                        i13 = i4;
                                                                                                                                        i32 = i6;
                                                                                                                                        i14 = i80;
                                                                                                                                        th = th;
                                                                                                                                        j6 = j28;
                                                                                                                                        i111 = i22;
                                                                                                                                        z6 = false;
                                                                                                                                        i12 = i5;
                                                                                                                                        i15 = i32;
                                                                                                                                        FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                                                        FileLog.e(th);
                                                                                                                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                        if (mediaExtractor != null) {
                                                                                                                                        }
                                                                                                                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                        if (mP4Builder != null) {
                                                                                                                                        }
                                                                                                                                        i16 = i13;
                                                                                                                                        i17 = i12;
                                                                                                                                        i18 = i111;
                                                                                                                                        z7 = true;
                                                                                                                                        if (z6) {
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            } else {
                                                                                                                                i80 = addTrack2;
                                                                                                                            }
                                                                                                                            str8 = str6;
                                                                                                                            i64 = i80;
                                                                                                                        } catch (Exception e54) {
                                                                                                                            e = e54;
                                                                                                                            i80 = addTrack2;
                                                                                                                        } catch (Throwable th52) {
                                                                                                                            th = th52;
                                                                                                                            i80 = addTrack2;
                                                                                                                        }
                                                                                                                    }
                                                                                                                    j32 = j28;
                                                                                                                    str5 = str9;
                                                                                                                    i75 = i41;
                                                                                                                    i76 = i43;
                                                                                                                    i71 = -1;
                                                                                                                    z22 = z24;
                                                                                                                    i74 = i54;
                                                                                                                    str7 = str4;
                                                                                                                } else {
                                                                                                                    boolean z53 = z22;
                                                                                                                    String str17 = str5;
                                                                                                                    str8 = str6;
                                                                                                                    try {
                                                                                                                    } catch (Exception e55) {
                                                                                                                        exc = e55;
                                                                                                                        inputSurface = inputSurface2;
                                                                                                                        str10 = str17;
                                                                                                                        mediaCodec8 = outputSurface4;
                                                                                                                        i81 = j26;
                                                                                                                    }
                                                                                                                    if (dequeueOutputBuffer < 0) {
                                                                                                                        throw new RuntimeException("unexpected result from encoder.dequeueOutputBuffer: " + dequeueOutputBuffer);
                                                                                                                    }
                                                                                                                    str5 = str17;
                                                                                                                    if (Build.VERSION.SDK_INT < 21) {
                                                                                                                        outputBuffer = byteBufferArr[dequeueOutputBuffer];
                                                                                                                    } else {
                                                                                                                        outputBuffer = createEncoderByType.getOutputBuffer(dequeueOutputBuffer);
                                                                                                                    }
                                                                                                                    if (outputBuffer == null) {
                                                                                                                        throw new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer + " was null");
                                                                                                                    }
                                                                                                                    int i127 = bufferInfo2.size;
                                                                                                                    outputSurface2 = outputSurface4;
                                                                                                                    if (i127 > 1) {
                                                                                                                        try {
                                                                                                                            i77 = bufferInfo2.flags;
                                                                                                                        } catch (Exception e56) {
                                                                                                                            e = e56;
                                                                                                                            j26 = j28;
                                                                                                                        } catch (Throwable th53) {
                                                                                                                            th = th53;
                                                                                                                            j26 = j28;
                                                                                                                            i13 = i4;
                                                                                                                            i12 = i5;
                                                                                                                            i68 = i6;
                                                                                                                            j29 = j26;
                                                                                                                            th = th;
                                                                                                                            i14 = i64;
                                                                                                                            i69 = i68;
                                                                                                                            j30 = j29;
                                                                                                                            i111 = i22;
                                                                                                                            i91 = i69;
                                                                                                                            j6 = j30;
                                                                                                                            z6 = false;
                                                                                                                            i15 = i91;
                                                                                                                            FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                                            FileLog.e(th);
                                                                                                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                            if (mediaExtractor != null) {
                                                                                                                            }
                                                                                                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                            if (mP4Builder != null) {
                                                                                                                            }
                                                                                                                            i16 = i13;
                                                                                                                            i17 = i12;
                                                                                                                            i18 = i111;
                                                                                                                            z7 = true;
                                                                                                                            if (z6) {
                                                                                                                            }
                                                                                                                        }
                                                                                                                        if ((i77 & 2) == 0) {
                                                                                                                            if (i54 == 0 || (i77 & 1) == 0) {
                                                                                                                                j26 = j28;
                                                                                                                            } else {
                                                                                                                                j26 = j28;
                                                                                                                                try {
                                                                                                                                    bufferInfo2.offset += i54;
                                                                                                                                    bufferInfo2.size = i127 - i54;
                                                                                                                                } catch (Exception e57) {
                                                                                                                                    i28 = i6;
                                                                                                                                    exc = e57;
                                                                                                                                    mediaCodec3 = createEncoderByType;
                                                                                                                                    i14 = i64;
                                                                                                                                    i26 = i22;
                                                                                                                                    j6 = j26;
                                                                                                                                }
                                                                                                                            }
                                                                                                                            if (z51 && (i77 & 1) != 0) {
                                                                                                                                if (bufferInfo2.size > 100) {
                                                                                                                                    outputBuffer.position(bufferInfo2.offset);
                                                                                                                                    byte[] bArr5 = new byte[100];
                                                                                                                                    outputBuffer.get(bArr5);
                                                                                                                                    int i128 = 0;
                                                                                                                                    int i129 = 0;
                                                                                                                                    while (true) {
                                                                                                                                        if (i128 >= 96) {
                                                                                                                                            break;
                                                                                                                                        }
                                                                                                                                        if (bArr5[i128] == 0 && bArr5[i128 + 1] == 0 && bArr5[i128 + 2] == 0) {
                                                                                                                                            bArr = bArr5;
                                                                                                                                            if (bArr5[i128 + 3] == 1 && (i129 = i129 + 1) > 1) {
                                                                                                                                                bufferInfo2.offset += i128;
                                                                                                                                                bufferInfo2.size -= i128;
                                                                                                                                                break;
                                                                                                                                            }
                                                                                                                                        } else {
                                                                                                                                            bArr = bArr5;
                                                                                                                                        }
                                                                                                                                        i128++;
                                                                                                                                        bArr5 = bArr;
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                z51 = false;
                                                                                                                            }
                                                                                                                            try {
                                                                                                                                long writeSampleData3 = mediaCodecVideoConvertor2.mediaMuxer.writeSampleData(i64, outputBuffer, bufferInfo2, true);
                                                                                                                                if (writeSampleData3 != 0 && (videoConvertorListener2 = mediaCodecVideoConvertor2.callback) != null) {
                                                                                                                                    i78 = i54;
                                                                                                                                    mediaCodec6 = mediaCodec2;
                                                                                                                                    long j51 = bufferInfo2.presentationTimeUs;
                                                                                                                                    if (j51 - j25 > j47) {
                                                                                                                                        j47 = j51 - j25;
                                                                                                                                    }
                                                                                                                                    long j52 = j47;
                                                                                                                                    videoConvertorListener2.didWriteData(writeSampleData3, (((float) j52) / 1000.0f) / f);
                                                                                                                                    j47 = j52;
                                                                                                                                }
                                                                                                                            } catch (Exception e58) {
                                                                                                                                e = e58;
                                                                                                                                i67 = i6;
                                                                                                                                exc = e;
                                                                                                                                i14 = i64;
                                                                                                                                i70 = i67;
                                                                                                                                j16 = j26;
                                                                                                                                i26 = i22;
                                                                                                                                i40 = i70;
                                                                                                                                mediaCodec3 = createEncoderByType;
                                                                                                                                i28 = i40;
                                                                                                                                j6 = j16;
                                                                                                                                if (exc instanceof IllegalStateException) {
                                                                                                                                }
                                                                                                                                StringBuilder sb322222222222222222222 = new StringBuilder();
                                                                                                                                sb322222222222222222222.append("bitrate: ");
                                                                                                                                sb322222222222222222222.append(i26);
                                                                                                                                sb322222222222222222222.append(" framerate: ");
                                                                                                                                sb322222222222222222222.append(i28 == 1 ? 1 : 0);
                                                                                                                                sb322222222222222222222.append(" size: ");
                                                                                                                                i21 = i5;
                                                                                                                                sb322222222222222222222.append(i21);
                                                                                                                                sb322222222222222222222.append("x");
                                                                                                                                i19 = i4;
                                                                                                                                sb322222222222222222222.append(i19);
                                                                                                                                FileLog.e(sb322222222222222222222.toString());
                                                                                                                                FileLog.e(exc);
                                                                                                                                i22 = i26;
                                                                                                                                mediaCodec4 = mediaCodec2;
                                                                                                                                i29 = i14;
                                                                                                                                z6 = z11;
                                                                                                                                z13 = true;
                                                                                                                                i30 = i28;
                                                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                }
                                                                                                                                z11 = z6;
                                                                                                                                z10 = z13;
                                                                                                                                mediaCodec = mediaCodec3;
                                                                                                                                outputSurface = outputSurface2;
                                                                                                                                i14 = i29;
                                                                                                                                i20 = i30;
                                                                                                                                if (outputSurface != null) {
                                                                                                                                }
                                                                                                                                if (inputSurface != null) {
                                                                                                                                }
                                                                                                                                if (mediaCodec != null) {
                                                                                                                                }
                                                                                                                                if (audioRecoder != null) {
                                                                                                                                }
                                                                                                                                checkConversionCanceled();
                                                                                                                                z12 = z10;
                                                                                                                                z6 = z11;
                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                }
                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                }
                                                                                                                                i17 = i21;
                                                                                                                                i16 = i19;
                                                                                                                                i18 = i22;
                                                                                                                                z7 = z12;
                                                                                                                                if (z6) {
                                                                                                                                }
                                                                                                                            }
                                                                                                                        } else {
                                                                                                                            j26 = j28;
                                                                                                                            i78 = i54;
                                                                                                                            mediaCodec6 = mediaCodec2;
                                                                                                                            if (i64 == -5) {
                                                                                                                                byte[] bArr6 = new byte[i127];
                                                                                                                                outputBuffer.limit(bufferInfo2.offset + i127);
                                                                                                                                outputBuffer.position(bufferInfo2.offset);
                                                                                                                                outputBuffer.get(bArr6);
                                                                                                                                byte b2 = 1;
                                                                                                                                int i130 = bufferInfo2.size - 1;
                                                                                                                                while (i130 >= 0 && i130 > 3) {
                                                                                                                                    if (bArr6[i130] == b2 && bArr6[i130 - 1] == 0 && bArr6[i130 - 2] == 0) {
                                                                                                                                        int i131 = i130 - 3;
                                                                                                                                        if (bArr6[i131] == 0) {
                                                                                                                                            byteBuffer3 = ByteBuffer.allocate(i131);
                                                                                                                                            byteBuffer4 = ByteBuffer.allocate(bufferInfo2.size - i131);
                                                                                                                                            byteBuffer3.put(bArr6, 0, i131).position(0);
                                                                                                                                            byteBuffer4.put(bArr6, i131, bufferInfo2.size - i131).position(0);
                                                                                                                                            break;
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    i130--;
                                                                                                                                    b2 = 1;
                                                                                                                                }
                                                                                                                                byteBuffer3 = null;
                                                                                                                                byteBuffer4 = null;
                                                                                                                                i79 = i41;
                                                                                                                                str7 = str4;
                                                                                                                                i76 = i43;
                                                                                                                                MediaFormat createVideoFormat4 = MediaFormat.createVideoFormat(str7, i76, i79);
                                                                                                                                if (byteBuffer3 != null && byteBuffer4 != null) {
                                                                                                                                    createVideoFormat4.setByteBuffer(str8, byteBuffer3);
                                                                                                                                    createVideoFormat4.setByteBuffer("csd-1", byteBuffer4);
                                                                                                                                }
                                                                                                                                i64 = mediaCodecVideoConvertor2.mediaMuxer.addTrack(createVideoFormat4, false);
                                                                                                                                mediaCodec7 = mediaCodec6;
                                                                                                                                ?? r2 = (bufferInfo2.flags & 4) != 0 ? 1 : 0;
                                                                                                                                createEncoderByType.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                                                                z22 = z53;
                                                                                                                                i74 = i78;
                                                                                                                                outputSurface5 = r2;
                                                                                                                                i71 = -1;
                                                                                                                                inputSurface2 = i79;
                                                                                                                                mediaCodec7 = mediaCodec7;
                                                                                                                                j26 = j26;
                                                                                                                                if (dequeueOutputBuffer == i71) {
                                                                                                                                    i43 = i76;
                                                                                                                                    i41 = inputSurface2;
                                                                                                                                    str6 = str8;
                                                                                                                                    str4 = str7;
                                                                                                                                    j28 = j26;
                                                                                                                                    j24 = 2500;
                                                                                                                                    i54 = i74;
                                                                                                                                    i51 = i73;
                                                                                                                                    z21 = z23;
                                                                                                                                    mediaCodec5 = createEncoderByType;
                                                                                                                                    OutputSurface outputSurface9 = outputSurface2;
                                                                                                                                    outputSurface2 = outputSurface5;
                                                                                                                                    mediaCodec2 = mediaCodec7;
                                                                                                                                    outputSurface4 = outputSurface9;
                                                                                                                                } else {
                                                                                                                                    if (z17) {
                                                                                                                                        i43 = i76;
                                                                                                                                    } else {
                                                                                                                                        i43 = i76;
                                                                                                                                        try {
                                                                                                                                            dequeueOutputBuffer2 = mediaCodec7.dequeueOutputBuffer(bufferInfo2, 2500L);
                                                                                                                                        } catch (Exception e59) {
                                                                                                                                            e = e59;
                                                                                                                                            i85 = i6;
                                                                                                                                            i83 = i64;
                                                                                                                                            j35 = j26;
                                                                                                                                        } catch (Throwable th54) {
                                                                                                                                            th = th54;
                                                                                                                                            i84 = i6;
                                                                                                                                            i83 = i64;
                                                                                                                                        }
                                                                                                                                        if (dequeueOutputBuffer2 == -1) {
                                                                                                                                            i41 = inputSurface2;
                                                                                                                                            str6 = str8;
                                                                                                                                            str4 = str7;
                                                                                                                                            outputSurface7 = outputSurface5;
                                                                                                                                            bufferInfo3 = bufferInfo2;
                                                                                                                                            z25 = z22;
                                                                                                                                            i83 = i64;
                                                                                                                                            inputSurface2 = inputSurface;
                                                                                                                                            z26 = false;
                                                                                                                                            j26 = j26;
                                                                                                                                        } else if (dequeueOutputBuffer2 != -3) {
                                                                                                                                            if (dequeueOutputBuffer2 != -2) {
                                                                                                                                                if (dequeueOutputBuffer2 < 0) {
                                                                                                                                                    break loop4;
                                                                                                                                                }
                                                                                                                                                boolean z54 = bufferInfo2.size != 0;
                                                                                                                                                long j53 = bufferInfo2.presentationTimeUs;
                                                                                                                                                if (j5 <= 0 || j53 < j5) {
                                                                                                                                                    i41 = inputSurface2;
                                                                                                                                                    z27 = z54;
                                                                                                                                                    j36 = 0;
                                                                                                                                                } else {
                                                                                                                                                    i41 = inputSurface2;
                                                                                                                                                    bufferInfo2.flags |= 4;
                                                                                                                                                    z27 = false;
                                                                                                                                                    z17 = true;
                                                                                                                                                    j36 = 0;
                                                                                                                                                    z18 = true;
                                                                                                                                                }
                                                                                                                                                if (j26 >= j36) {
                                                                                                                                                    z28 = z27;
                                                                                                                                                    try {
                                                                                                                                                        if ((bufferInfo2.flags & 4) != 0) {
                                                                                                                                                            z25 = z22;
                                                                                                                                                            i65 = i6;
                                                                                                                                                            try {
                                                                                                                                                                try {
                                                                                                                                                                    str6 = str8;
                                                                                                                                                                    str4 = str7;
                                                                                                                                                                    if (Math.abs(j26 - j25) > MediaController.VIDEO_BITRATE_480 / i65) {
                                                                                                                                                                        if (j25 > 0) {
                                                                                                                                                                            try {
                                                                                                                                                                                mediaCodecVideoConvertor2.extractor.seekTo(j25, 0);
                                                                                                                                                                                outputSurface7 = outputSurface5;
                                                                                                                                                                                j37 = j27;
                                                                                                                                                                            } catch (Throwable th55) {
                                                                                                                                                                                th = th55;
                                                                                                                                                                                i13 = i4;
                                                                                                                                                                                i12 = i5;
                                                                                                                                                                                i68 = i65;
                                                                                                                                                                                j29 = j26;
                                                                                                                                                                                th = th;
                                                                                                                                                                                i14 = i64;
                                                                                                                                                                                i69 = i68;
                                                                                                                                                                                j30 = j29;
                                                                                                                                                                                i111 = i22;
                                                                                                                                                                                i91 = i69;
                                                                                                                                                                                j6 = j30;
                                                                                                                                                                                z6 = false;
                                                                                                                                                                                i15 = i91;
                                                                                                                                                                                FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                                                                                                FileLog.e(th);
                                                                                                                                                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                                if (mediaExtractor != null) {
                                                                                                                                                                                }
                                                                                                                                                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                                if (mP4Builder != null) {
                                                                                                                                                                                }
                                                                                                                                                                                i16 = i13;
                                                                                                                                                                                i17 = i12;
                                                                                                                                                                                i18 = i111;
                                                                                                                                                                                z7 = true;
                                                                                                                                                                                if (z6) {
                                                                                                                                                                                }
                                                                                                                                                                            }
                                                                                                                                                                        } else {
                                                                                                                                                                            outputSurface7 = outputSurface5;
                                                                                                                                                                            mediaCodecVideoConvertor2.extractor.seekTo(0L, 0);
                                                                                                                                                                            j37 = j27;
                                                                                                                                                                        }
                                                                                                                                                                        j48 = j37 + j13;
                                                                                                                                                                        try {
                                                                                                                                                                            bufferInfo2.flags &= -5;
                                                                                                                                                                            mediaCodec7.flush();
                                                                                                                                                                            j5 = j26;
                                                                                                                                                                            z29 = false;
                                                                                                                                                                            z17 = false;
                                                                                                                                                                            j38 = 0;
                                                                                                                                                                            z18 = false;
                                                                                                                                                                            z30 = true;
                                                                                                                                                                            j26 = -1;
                                                                                                                                                                            i84 = i65;
                                                                                                                                                                            if (j45 > j38) {
                                                                                                                                                                                i83 = i64;
                                                                                                                                                                                try {
                                                                                                                                                                                    try {
                                                                                                                                                                                        if (bufferInfo2.presentationTimeUs - j45 < j10 && (bufferInfo2.flags & 4) == 0) {
                                                                                                                                                                                            z29 = false;
                                                                                                                                                                                        }
                                                                                                                                                                                    } catch (Throwable th56) {
                                                                                                                                                                                        th = th56;
                                                                                                                                                                                        i13 = i4;
                                                                                                                                                                                        i12 = i5;
                                                                                                                                                                                        i14 = i83;
                                                                                                                                                                                        th = th;
                                                                                                                                                                                        i69 = i84;
                                                                                                                                                                                        j30 = j26;
                                                                                                                                                                                        i111 = i22;
                                                                                                                                                                                        i91 = i69;
                                                                                                                                                                                        j6 = j30;
                                                                                                                                                                                        z6 = false;
                                                                                                                                                                                        i15 = i91;
                                                                                                                                                                                        FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                                                                                                        FileLog.e(th);
                                                                                                                                                                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                                        if (mediaExtractor != null) {
                                                                                                                                                                                        }
                                                                                                                                                                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                                        if (mP4Builder != null) {
                                                                                                                                                                                        }
                                                                                                                                                                                        i16 = i13;
                                                                                                                                                                                        i17 = i12;
                                                                                                                                                                                        i18 = i111;
                                                                                                                                                                                        z7 = true;
                                                                                                                                                                                        if (z6) {
                                                                                                                                                                                        }
                                                                                                                                                                                    }
                                                                                                                                                                                } catch (Exception e60) {
                                                                                                                                                                                    e = e60;
                                                                                                                                                                                    i86 = i84;
                                                                                                                                                                                    j34 = j26;
                                                                                                                                                                                    i14 = i83;
                                                                                                                                                                                    exc = e;
                                                                                                                                                                                    i82 = i86;
                                                                                                                                                                                    outputSurface6 = mediaCodec7;
                                                                                                                                                                                    j33 = j34;
                                                                                                                                                                                    mediaCodec2 = outputSurface6;
                                                                                                                                                                                    i70 = i82;
                                                                                                                                                                                    j16 = j33;
                                                                                                                                                                                    i26 = i22;
                                                                                                                                                                                    i40 = i70;
                                                                                                                                                                                    mediaCodec3 = createEncoderByType;
                                                                                                                                                                                    i28 = i40;
                                                                                                                                                                                    j6 = j16;
                                                                                                                                                                                    if (exc instanceof IllegalStateException) {
                                                                                                                                                                                    }
                                                                                                                                                                                    StringBuilder sb3222222222222222222222 = new StringBuilder();
                                                                                                                                                                                    sb3222222222222222222222.append("bitrate: ");
                                                                                                                                                                                    sb3222222222222222222222.append(i26);
                                                                                                                                                                                    sb3222222222222222222222.append(" framerate: ");
                                                                                                                                                                                    sb3222222222222222222222.append(i28 == 1 ? 1 : 0);
                                                                                                                                                                                    sb3222222222222222222222.append(" size: ");
                                                                                                                                                                                    i21 = i5;
                                                                                                                                                                                    sb3222222222222222222222.append(i21);
                                                                                                                                                                                    sb3222222222222222222222.append("x");
                                                                                                                                                                                    i19 = i4;
                                                                                                                                                                                    sb3222222222222222222222.append(i19);
                                                                                                                                                                                    FileLog.e(sb3222222222222222222222.toString());
                                                                                                                                                                                    FileLog.e(exc);
                                                                                                                                                                                    i22 = i26;
                                                                                                                                                                                    mediaCodec4 = mediaCodec2;
                                                                                                                                                                                    i29 = i14;
                                                                                                                                                                                    z6 = z11;
                                                                                                                                                                                    z13 = true;
                                                                                                                                                                                    i30 = i28;
                                                                                                                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    z11 = z6;
                                                                                                                                                                                    z10 = z13;
                                                                                                                                                                                    mediaCodec = mediaCodec3;
                                                                                                                                                                                    outputSurface = outputSurface2;
                                                                                                                                                                                    i14 = i29;
                                                                                                                                                                                    i20 = i30;
                                                                                                                                                                                    if (outputSurface != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    if (inputSurface != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    if (mediaCodec != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    if (audioRecoder != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    checkConversionCanceled();
                                                                                                                                                                                    z12 = z10;
                                                                                                                                                                                    z6 = z11;
                                                                                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    i17 = i21;
                                                                                                                                                                                    i16 = i19;
                                                                                                                                                                                    i18 = i22;
                                                                                                                                                                                    z7 = z12;
                                                                                                                                                                                    if (z6) {
                                                                                                                                                                                    }
                                                                                                                                                                                }
                                                                                                                                                                            } else {
                                                                                                                                                                                i83 = i64;
                                                                                                                                                                            }
                                                                                                                                                                            if (j26 >= 0) {
                                                                                                                                                                                bufferInfo4 = bufferInfo2;
                                                                                                                                                                                j39 = j26;
                                                                                                                                                                            } else {
                                                                                                                                                                                bufferInfo4 = bufferInfo2;
                                                                                                                                                                                j39 = j;
                                                                                                                                                                            }
                                                                                                                                                                            if (j39 > 0 || j46 != -1) {
                                                                                                                                                                                bufferInfo3 = bufferInfo4;
                                                                                                                                                                            } else if (j53 < j39) {
                                                                                                                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                                                                                                                    StringBuilder sb4 = new StringBuilder();
                                                                                                                                                                                    sb4.append("drop frame startTime = ");
                                                                                                                                                                                    sb4.append(j39);
                                                                                                                                                                                    sb4.append(" present time = ");
                                                                                                                                                                                    bufferInfo3 = bufferInfo4;
                                                                                                                                                                                    sb4.append(bufferInfo3.presentationTimeUs);
                                                                                                                                                                                    FileLog.d(sb4.toString());
                                                                                                                                                                                } else {
                                                                                                                                                                                    bufferInfo3 = bufferInfo4;
                                                                                                                                                                                }
                                                                                                                                                                                z29 = false;
                                                                                                                                                                            } else {
                                                                                                                                                                                bufferInfo3 = bufferInfo4;
                                                                                                                                                                                long j54 = bufferInfo3.presentationTimeUs;
                                                                                                                                                                                if (j37 != -2147483648L) {
                                                                                                                                                                                    j48 -= j54;
                                                                                                                                                                                }
                                                                                                                                                                                j46 = j54;
                                                                                                                                                                            }
                                                                                                                                                                            if (z30) {
                                                                                                                                                                                j46 = -1;
                                                                                                                                                                            } else {
                                                                                                                                                                                if (j26 == -1 && j48 != 0) {
                                                                                                                                                                                    bufferInfo3.presentationTimeUs += j48;
                                                                                                                                                                                }
                                                                                                                                                                                mediaCodec7.releaseOutputBuffer(dequeueOutputBuffer2, z29);
                                                                                                                                                                            }
                                                                                                                                                                            if (z29) {
                                                                                                                                                                                try {
                                                                                                                                                                                    long j55 = bufferInfo3.presentationTimeUs;
                                                                                                                                                                                    j27 = j26 >= 0 ? Math.max(j37, j55) : j37;
                                                                                                                                                                                    try {
                                                                                                                                                                                        outputSurface2.awaitNewImage();
                                                                                                                                                                                        z31 = false;
                                                                                                                                                                                    } catch (Exception e61) {
                                                                                                                                                                                        FileLog.e(e61);
                                                                                                                                                                                        z31 = true;
                                                                                                                                                                                    }
                                                                                                                                                                                    if (z31) {
                                                                                                                                                                                        inputSurface3 = inputSurface;
                                                                                                                                                                                    } else {
                                                                                                                                                                                        outputSurface2.drawImage();
                                                                                                                                                                                        inputSurface3 = inputSurface;
                                                                                                                                                                                        try {
                                                                                                                                                                                            inputSurface3.setPresentationTime(bufferInfo3.presentationTimeUs * 1000);
                                                                                                                                                                                            inputSurface3.swapBuffers();
                                                                                                                                                                                        } catch (Exception e62) {
                                                                                                                                                                                            i14 = i83;
                                                                                                                                                                                            exc = e62;
                                                                                                                                                                                            inputSurface = inputSurface3;
                                                                                                                                                                                            i82 = i84;
                                                                                                                                                                                            outputSurface6 = mediaCodec7;
                                                                                                                                                                                            j33 = j26;
                                                                                                                                                                                        }
                                                                                                                                                                                    }
                                                                                                                                                                                    j45 = j55;
                                                                                                                                                                                    inputSurface4 = inputSurface3;
                                                                                                                                                                                } catch (Exception e63) {
                                                                                                                                                                                    e = e63;
                                                                                                                                                                                    i85 = i84;
                                                                                                                                                                                    j35 = j26;
                                                                                                                                                                                    i86 = i85;
                                                                                                                                                                                    j34 = j35;
                                                                                                                                                                                    i14 = i83;
                                                                                                                                                                                    exc = e;
                                                                                                                                                                                    i82 = i86;
                                                                                                                                                                                    outputSurface6 = mediaCodec7;
                                                                                                                                                                                    j33 = j34;
                                                                                                                                                                                    mediaCodec2 = outputSurface6;
                                                                                                                                                                                    i70 = i82;
                                                                                                                                                                                    j16 = j33;
                                                                                                                                                                                    i26 = i22;
                                                                                                                                                                                    i40 = i70;
                                                                                                                                                                                    mediaCodec3 = createEncoderByType;
                                                                                                                                                                                    i28 = i40;
                                                                                                                                                                                    j6 = j16;
                                                                                                                                                                                    if (exc instanceof IllegalStateException) {
                                                                                                                                                                                    }
                                                                                                                                                                                    StringBuilder sb32222222222222222222222 = new StringBuilder();
                                                                                                                                                                                    sb32222222222222222222222.append("bitrate: ");
                                                                                                                                                                                    sb32222222222222222222222.append(i26);
                                                                                                                                                                                    sb32222222222222222222222.append(" framerate: ");
                                                                                                                                                                                    sb32222222222222222222222.append(i28 == 1 ? 1 : 0);
                                                                                                                                                                                    sb32222222222222222222222.append(" size: ");
                                                                                                                                                                                    i21 = i5;
                                                                                                                                                                                    sb32222222222222222222222.append(i21);
                                                                                                                                                                                    sb32222222222222222222222.append("x");
                                                                                                                                                                                    i19 = i4;
                                                                                                                                                                                    sb32222222222222222222222.append(i19);
                                                                                                                                                                                    FileLog.e(sb32222222222222222222222.toString());
                                                                                                                                                                                    FileLog.e(exc);
                                                                                                                                                                                    i22 = i26;
                                                                                                                                                                                    mediaCodec4 = mediaCodec2;
                                                                                                                                                                                    i29 = i14;
                                                                                                                                                                                    z6 = z11;
                                                                                                                                                                                    z13 = true;
                                                                                                                                                                                    i30 = i28;
                                                                                                                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    z11 = z6;
                                                                                                                                                                                    z10 = z13;
                                                                                                                                                                                    mediaCodec = mediaCodec3;
                                                                                                                                                                                    outputSurface = outputSurface2;
                                                                                                                                                                                    i14 = i29;
                                                                                                                                                                                    i20 = i30;
                                                                                                                                                                                    if (outputSurface != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    if (inputSurface != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    if (mediaCodec != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    if (audioRecoder != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    checkConversionCanceled();
                                                                                                                                                                                    z12 = z10;
                                                                                                                                                                                    z6 = z11;
                                                                                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    i17 = i21;
                                                                                                                                                                                    i16 = i19;
                                                                                                                                                                                    i18 = i22;
                                                                                                                                                                                    z7 = z12;
                                                                                                                                                                                    if (z6) {
                                                                                                                                                                                    }
                                                                                                                                                                                }
                                                                                                                                                                            } else {
                                                                                                                                                                                inputSurface4 = inputSurface;
                                                                                                                                                                                j27 = j37;
                                                                                                                                                                            }
                                                                                                                                                                            if ((bufferInfo3.flags & 4) != 0) {
                                                                                                                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                                                                                                                    FileLog.d("decoder stream end");
                                                                                                                                                                                }
                                                                                                                                                                                createEncoderByType.signalEndOfInputStream();
                                                                                                                                                                                z26 = false;
                                                                                                                                                                                inputSurface2 = inputSurface4;
                                                                                                                                                                                j26 = j26;
                                                                                                                                                                            } else {
                                                                                                                                                                                z26 = z23;
                                                                                                                                                                                inputSurface2 = inputSurface4;
                                                                                                                                                                                j26 = j26;
                                                                                                                                                                            }
                                                                                                                                                                        } catch (Exception e64) {
                                                                                                                                                                            exc = e64;
                                                                                                                                                                            i14 = i64;
                                                                                                                                                                            mediaCodec2 = mediaCodec7;
                                                                                                                                                                            i26 = i22;
                                                                                                                                                                            j5 = j26;
                                                                                                                                                                            mediaCodec3 = createEncoderByType;
                                                                                                                                                                            j6 = -1;
                                                                                                                                                                            i28 = i65;
                                                                                                                                                                        } catch (Throwable th57) {
                                                                                                                                                                            i13 = i4;
                                                                                                                                                                            i12 = i5;
                                                                                                                                                                            th = th57;
                                                                                                                                                                            i14 = i64;
                                                                                                                                                                            i111 = i22;
                                                                                                                                                                            j5 = j26;
                                                                                                                                                                            z6 = false;
                                                                                                                                                                            j6 = -1;
                                                                                                                                                                            i15 = i65;
                                                                                                                                                                            FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                                                                                            FileLog.e(th);
                                                                                                                                                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                            if (mediaExtractor != null) {
                                                                                                                                                                            }
                                                                                                                                                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                            if (mP4Builder != null) {
                                                                                                                                                                            }
                                                                                                                                                                            i16 = i13;
                                                                                                                                                                            i17 = i12;
                                                                                                                                                                            i18 = i111;
                                                                                                                                                                            z7 = true;
                                                                                                                                                                            if (z6) {
                                                                                                                                                                            }
                                                                                                                                                                        }
                                                                                                                                                                    } else {
                                                                                                                                                                        outputSurface7 = outputSurface5;
                                                                                                                                                                        j37 = j27;
                                                                                                                                                                        i87 = i65;
                                                                                                                                                                        z29 = z28;
                                                                                                                                                                        j38 = 0;
                                                                                                                                                                        z30 = false;
                                                                                                                                                                        i84 = i87;
                                                                                                                                                                        j26 = j26;
                                                                                                                                                                        if (j45 > j38) {
                                                                                                                                                                        }
                                                                                                                                                                        if (j26 >= 0) {
                                                                                                                                                                        }
                                                                                                                                                                        if (j39 > 0) {
                                                                                                                                                                        }
                                                                                                                                                                        bufferInfo3 = bufferInfo4;
                                                                                                                                                                        if (z30) {
                                                                                                                                                                        }
                                                                                                                                                                        if (z29) {
                                                                                                                                                                        }
                                                                                                                                                                        if ((bufferInfo3.flags & 4) != 0) {
                                                                                                                                                                        }
                                                                                                                                                                    }
                                                                                                                                                                } catch (Throwable th58) {
                                                                                                                                                                    th = th58;
                                                                                                                                                                    i13 = i4;
                                                                                                                                                                    i12 = i5;
                                                                                                                                                                    i68 = i65;
                                                                                                                                                                    j29 = j26;
                                                                                                                                                                    th = th;
                                                                                                                                                                    i14 = i64;
                                                                                                                                                                    i69 = i68;
                                                                                                                                                                    j30 = j29;
                                                                                                                                                                    i111 = i22;
                                                                                                                                                                    i91 = i69;
                                                                                                                                                                    j6 = j30;
                                                                                                                                                                    z6 = false;
                                                                                                                                                                    i15 = i91;
                                                                                                                                                                    FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                                                                                    FileLog.e(th);
                                                                                                                                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                    if (mediaExtractor != null) {
                                                                                                                                                                    }
                                                                                                                                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                    if (mP4Builder != null) {
                                                                                                                                                                    }
                                                                                                                                                                    i16 = i13;
                                                                                                                                                                    i17 = i12;
                                                                                                                                                                    i18 = i111;
                                                                                                                                                                    z7 = true;
                                                                                                                                                                    if (z6) {
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                            } catch (Exception e65) {
                                                                                                                                                                e = e65;
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    } catch (Throwable th59) {
                                                                                                                                                        th = th59;
                                                                                                                                                        i65 = i6;
                                                                                                                                                    }
                                                                                                                                                } else {
                                                                                                                                                    z28 = z27;
                                                                                                                                                }
                                                                                                                                                str6 = str8;
                                                                                                                                                str4 = str7;
                                                                                                                                                outputSurface7 = outputSurface5;
                                                                                                                                                z25 = z22;
                                                                                                                                                j37 = j27;
                                                                                                                                                i87 = i6;
                                                                                                                                                z29 = z28;
                                                                                                                                                j38 = 0;
                                                                                                                                                z30 = false;
                                                                                                                                                i84 = i87;
                                                                                                                                                j26 = j26;
                                                                                                                                                if (j45 > j38) {
                                                                                                                                                }
                                                                                                                                                if (j26 >= 0) {
                                                                                                                                                }
                                                                                                                                                if (j39 > 0) {
                                                                                                                                                }
                                                                                                                                                bufferInfo3 = bufferInfo4;
                                                                                                                                                if (z30) {
                                                                                                                                                }
                                                                                                                                                if (z29) {
                                                                                                                                                }
                                                                                                                                                if ((bufferInfo3.flags & 4) != 0) {
                                                                                                                                                }
                                                                                                                                            } else {
                                                                                                                                                try {
                                                                                                                                                    try {
                                                                                                                                                        MediaFormat outputFormat3 = mediaCodec7.getOutputFormat();
                                                                                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                                                                                            FileLog.d("newFormat = " + outputFormat3);
                                                                                                                                                        }
                                                                                                                                                    } catch (Throwable th60) {
                                                                                                                                                        th = th60;
                                                                                                                                                        i13 = i4;
                                                                                                                                                        i12 = i5;
                                                                                                                                                        i68 = i6;
                                                                                                                                                        j29 = j26;
                                                                                                                                                        th = th;
                                                                                                                                                        i14 = i64;
                                                                                                                                                        i69 = i68;
                                                                                                                                                        j30 = j29;
                                                                                                                                                        i111 = i22;
                                                                                                                                                        i91 = i69;
                                                                                                                                                        j6 = j30;
                                                                                                                                                        z6 = false;
                                                                                                                                                        i15 = i91;
                                                                                                                                                        FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                                                                                                        FileLog.e(th);
                                                                                                                                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                        if (mediaExtractor != null) {
                                                                                                                                                        }
                                                                                                                                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                        if (mP4Builder != null) {
                                                                                                                                                        }
                                                                                                                                                        i16 = i13;
                                                                                                                                                        i17 = i12;
                                                                                                                                                        i18 = i111;
                                                                                                                                                        z7 = true;
                                                                                                                                                        if (z6) {
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                } catch (Exception e66) {
                                                                                                                                                    e = e66;
                                                                                                                                                    i65 = i6;
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                            exc = e;
                                                                                                                                            str10 = i65;
                                                                                                                                            mediaCodec8 = mediaCodec7;
                                                                                                                                            i81 = j26;
                                                                                                                                            i14 = i64;
                                                                                                                                            i82 = str10;
                                                                                                                                            outputSurface6 = mediaCodec8;
                                                                                                                                            j33 = i81;
                                                                                                                                            mediaCodec2 = outputSurface6;
                                                                                                                                            i70 = i82;
                                                                                                                                            j16 = j33;
                                                                                                                                            i26 = i22;
                                                                                                                                            i40 = i70;
                                                                                                                                            mediaCodec3 = createEncoderByType;
                                                                                                                                            i28 = i40;
                                                                                                                                            j6 = j16;
                                                                                                                                            z11 = ((exc instanceof IllegalStateException) || z3) ? false : true;
                                                                                                                                            StringBuilder sb322222222222222222222222 = new StringBuilder();
                                                                                                                                            sb322222222222222222222222.append("bitrate: ");
                                                                                                                                            sb322222222222222222222222.append(i26);
                                                                                                                                            sb322222222222222222222222.append(" framerate: ");
                                                                                                                                            sb322222222222222222222222.append(i28 == 1 ? 1 : 0);
                                                                                                                                            sb322222222222222222222222.append(" size: ");
                                                                                                                                            i21 = i5;
                                                                                                                                            sb322222222222222222222222.append(i21);
                                                                                                                                            sb322222222222222222222222.append("x");
                                                                                                                                            i19 = i4;
                                                                                                                                            sb322222222222222222222222.append(i19);
                                                                                                                                            FileLog.e(sb322222222222222222222222.toString());
                                                                                                                                            FileLog.e(exc);
                                                                                                                                            i22 = i26;
                                                                                                                                            mediaCodec4 = mediaCodec2;
                                                                                                                                            i29 = i14;
                                                                                                                                            z6 = z11;
                                                                                                                                            z13 = true;
                                                                                                                                            i30 = i28;
                                                                                                                                            mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                            if (mediaCodec4 != null) {
                                                                                                                                                mediaCodec4.stop();
                                                                                                                                                mediaCodec4.release();
                                                                                                                                            }
                                                                                                                                            z11 = z6;
                                                                                                                                            z10 = z13;
                                                                                                                                            mediaCodec = mediaCodec3;
                                                                                                                                            outputSurface = outputSurface2;
                                                                                                                                            i14 = i29;
                                                                                                                                            i20 = i30;
                                                                                                                                        }
                                                                                                                                        j25 = j;
                                                                                                                                        i54 = i74;
                                                                                                                                        i64 = i83;
                                                                                                                                        i51 = i73;
                                                                                                                                        z22 = z25;
                                                                                                                                        bufferInfo2 = bufferInfo3;
                                                                                                                                        inputSurface = inputSurface2;
                                                                                                                                        mediaCodec5 = createEncoderByType;
                                                                                                                                        mediaCodec2 = mediaCodec7;
                                                                                                                                        outputSurface4 = outputSurface2;
                                                                                                                                        outputSurface2 = outputSurface7;
                                                                                                                                        j28 = j26;
                                                                                                                                        z21 = z26;
                                                                                                                                        j24 = 2500;
                                                                                                                                    }
                                                                                                                                    i41 = inputSurface2;
                                                                                                                                    str6 = str8;
                                                                                                                                    str4 = str7;
                                                                                                                                    outputSurface7 = outputSurface5;
                                                                                                                                    bufferInfo3 = bufferInfo2;
                                                                                                                                    z25 = z22;
                                                                                                                                    i83 = i64;
                                                                                                                                    inputSurface2 = inputSurface;
                                                                                                                                    z26 = z23;
                                                                                                                                    j27 = j27;
                                                                                                                                    j26 = j26;
                                                                                                                                    j25 = j;
                                                                                                                                    i54 = i74;
                                                                                                                                    i64 = i83;
                                                                                                                                    i51 = i73;
                                                                                                                                    z22 = z25;
                                                                                                                                    bufferInfo2 = bufferInfo3;
                                                                                                                                    inputSurface = inputSurface2;
                                                                                                                                    mediaCodec5 = createEncoderByType;
                                                                                                                                    mediaCodec2 = mediaCodec7;
                                                                                                                                    outputSurface4 = outputSurface2;
                                                                                                                                    outputSurface2 = outputSurface7;
                                                                                                                                    j28 = j26;
                                                                                                                                    z21 = z26;
                                                                                                                                    j24 = 2500;
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                        i79 = i41;
                                                                                                                        str7 = str4;
                                                                                                                        i76 = i43;
                                                                                                                        mediaCodec7 = mediaCodec6;
                                                                                                                        if ((bufferInfo2.flags & 4) != 0) {
                                                                                                                        }
                                                                                                                        createEncoderByType.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                                                        z22 = z53;
                                                                                                                        i74 = i78;
                                                                                                                        outputSurface5 = r2;
                                                                                                                        i71 = -1;
                                                                                                                        inputSurface2 = i79;
                                                                                                                        mediaCodec7 = mediaCodec7;
                                                                                                                        j26 = j26;
                                                                                                                        if (dequeueOutputBuffer == i71) {
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        j26 = j28;
                                                                                                                    }
                                                                                                                    i78 = i54;
                                                                                                                    mediaCodec6 = mediaCodec2;
                                                                                                                    i79 = i41;
                                                                                                                    str7 = str4;
                                                                                                                    i76 = i43;
                                                                                                                    mediaCodec7 = mediaCodec6;
                                                                                                                    if ((bufferInfo2.flags & 4) != 0) {
                                                                                                                    }
                                                                                                                    createEncoderByType.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                                                    z22 = z53;
                                                                                                                    i74 = i78;
                                                                                                                    outputSurface5 = r2;
                                                                                                                    i71 = -1;
                                                                                                                    inputSurface2 = i79;
                                                                                                                    mediaCodec7 = mediaCodec7;
                                                                                                                    j26 = j26;
                                                                                                                    if (dequeueOutputBuffer == i71) {
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                            OutputSurface outputSurface10 = outputSurface4;
                                                                                                            mediaCodec7 = mediaCodec2;
                                                                                                            outputSurface5 = outputSurface2;
                                                                                                            outputSurface2 = outputSurface10;
                                                                                                            inputSurface2 = i75;
                                                                                                            j26 = j32;
                                                                                                            if (dequeueOutputBuffer == i71) {
                                                                                                            }
                                                                                                        } catch (Throwable th61) {
                                                                                                            th = th61;
                                                                                                            i65 = i6;
                                                                                                            j26 = j28;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            i13 = i4;
                                                                                            j21 = j28;
                                                                                            i29 = i64;
                                                                                            inputSurface2 = j25;
                                                                                            i50 = i59;
                                                                                            byteBufferArr2 = byteBufferArr3;
                                                                                            z15 = z19;
                                                                                            i48 = i60;
                                                                                            i42 = i24;
                                                                                            j18 = j5;
                                                                                            j22 = j27;
                                                                                            bufferInfo = bufferInfo2;
                                                                                            audioRecoder2 = audioRecoder;
                                                                                        }
                                                                                        i21 = i5;
                                                                                        i30 = i6;
                                                                                        j6 = j21;
                                                                                        i19 = i13;
                                                                                        mediaCodec4 = mediaCodec2;
                                                                                        audioRecoder = audioRecoder2;
                                                                                        j5 = j18;
                                                                                        outputSurface2 = outputSurface4;
                                                                                        i24 = i42;
                                                                                        z6 = false;
                                                                                        z13 = false;
                                                                                        mediaCodec3 = mediaCodec5;
                                                                                        mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                        if (mediaCodec4 != null) {
                                                                                        }
                                                                                        z11 = z6;
                                                                                        z10 = z13;
                                                                                        mediaCodec = mediaCodec3;
                                                                                        outputSurface = outputSurface2;
                                                                                        i14 = i29;
                                                                                        i20 = i30;
                                                                                    } else {
                                                                                        j18 = j2;
                                                                                        byteBufferArr = outputBuffers;
                                                                                        j19 = j;
                                                                                        i50 = -5;
                                                                                        i51 = 0;
                                                                                        audioRecoder2 = null;
                                                                                    }
                                                                                }
                                                                            }
                                                                            z15 = true;
                                                                            if (trackFormat2.getString("mime").equals("audio/unknown")) {
                                                                            }
                                                                            if (i48 < 0) {
                                                                            }
                                                                        } else {
                                                                            j18 = j2;
                                                                            byteBufferArr = outputBuffers;
                                                                            j19 = j;
                                                                            i50 = -5;
                                                                            i51 = 0;
                                                                            audioRecoder2 = null;
                                                                            z15 = true;
                                                                        }
                                                                        byteBuffer = null;
                                                                        j20 = j19;
                                                                        if (i48 < 0) {
                                                                        }
                                                                        checkConversionCanceled();
                                                                        j21 = j15;
                                                                        long j452 = -1;
                                                                        long j462 = -1;
                                                                        ByteBuffer byteBuffer72 = byteBuffer;
                                                                        j22 = -2147483648L;
                                                                        outputSurface2 = null;
                                                                        z17 = false;
                                                                        i29 = -5;
                                                                        z18 = false;
                                                                        boolean z512 = true;
                                                                        long j472 = 0;
                                                                        long j482 = 0;
                                                                        boolean z522 = z50;
                                                                        i54 = 0;
                                                                        byteBufferArr2 = inputBuffers;
                                                                        inputSurface2 = j20;
                                                                        loop4: while (true) {
                                                                            if (outputSurface2 != null) {
                                                                            }
                                                                            checkConversionCanceled();
                                                                            if (z15) {
                                                                            }
                                                                            i57 = i54;
                                                                            if (z18) {
                                                                            }
                                                                            i54 = i57;
                                                                            z21 = !z17;
                                                                            i64 = i29;
                                                                            j27 = j22;
                                                                            z22 = true;
                                                                            j28 = j21;
                                                                            while (true) {
                                                                                if (!z21) {
                                                                                }
                                                                                checkConversionCanceled();
                                                                                if (z3) {
                                                                                }
                                                                                z23 = z21;
                                                                                createEncoderByType = mediaCodec5;
                                                                                dequeueOutputBuffer = createEncoderByType.dequeueOutputBuffer(bufferInfo2, j24);
                                                                                i71 = -1;
                                                                                if (dequeueOutputBuffer == -1) {
                                                                                }
                                                                                OutputSurface outputSurface102 = outputSurface4;
                                                                                mediaCodec7 = mediaCodec2;
                                                                                outputSurface5 = outputSurface2;
                                                                                outputSurface2 = outputSurface102;
                                                                                inputSurface2 = i75;
                                                                                j26 = j32;
                                                                                if (dequeueOutputBuffer == i71) {
                                                                                }
                                                                            }
                                                                            i13 = i4;
                                                                            j21 = j28;
                                                                            i29 = i64;
                                                                            inputSurface2 = j25;
                                                                            i50 = i59;
                                                                            byteBufferArr2 = byteBufferArr3;
                                                                            z15 = z19;
                                                                            i48 = i60;
                                                                            i42 = i24;
                                                                            j18 = j5;
                                                                            j22 = j27;
                                                                            bufferInfo = bufferInfo2;
                                                                            audioRecoder2 = audioRecoder;
                                                                        }
                                                                        i21 = i5;
                                                                        i30 = i6;
                                                                        j6 = j21;
                                                                        i19 = i13;
                                                                        mediaCodec4 = mediaCodec2;
                                                                        audioRecoder = audioRecoder2;
                                                                        j5 = j18;
                                                                        outputSurface2 = outputSurface4;
                                                                        i24 = i42;
                                                                        z6 = false;
                                                                        z13 = false;
                                                                        mediaCodec3 = mediaCodec5;
                                                                        mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                        if (mediaCodec4 != null) {
                                                                        }
                                                                        z11 = z6;
                                                                        z10 = z13;
                                                                        mediaCodec = mediaCodec3;
                                                                        outputSurface = outputSurface2;
                                                                        i14 = i29;
                                                                        i20 = i30;
                                                                    } catch (Throwable th62) {
                                                                        th2 = th62;
                                                                        i15 = i6;
                                                                        j5 = j2;
                                                                        j6 = j15;
                                                                        i12 = i44;
                                                                        i111 = i22;
                                                                        z6 = false;
                                                                        i14 = -5;
                                                                        th = th2;
                                                                        FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                                        FileLog.e(th);
                                                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                        if (mediaExtractor != null) {
                                                                        }
                                                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                        if (mP4Builder != null) {
                                                                        }
                                                                        i16 = i13;
                                                                        i17 = i12;
                                                                        i18 = i111;
                                                                        z7 = true;
                                                                        if (z6) {
                                                                        }
                                                                    }
                                                                } else {
                                                                    i13 = i4;
                                                                }
                                                            }
                                                            z14 = true;
                                                            mediaCodec2 = createDecoderByType;
                                                            mediaCodec2.configure(trackFormat, outputSurface4.getSurface(), (MediaCrypto) null, 0);
                                                            mediaCodec2.start();
                                                            if (i38 >= 21) {
                                                            }
                                                            i48 = i124;
                                                            if (i48 < 0) {
                                                            }
                                                            byteBuffer = null;
                                                            j20 = j19;
                                                            if (i48 < 0) {
                                                            }
                                                            checkConversionCanceled();
                                                            j21 = j15;
                                                            long j4522 = -1;
                                                            long j4622 = -1;
                                                            ByteBuffer byteBuffer722 = byteBuffer;
                                                            j22 = -2147483648L;
                                                            outputSurface2 = null;
                                                            z17 = false;
                                                            i29 = -5;
                                                            z18 = false;
                                                            boolean z5122 = true;
                                                            long j4722 = 0;
                                                            long j4822 = 0;
                                                            boolean z5222 = z50;
                                                            i54 = 0;
                                                            byteBufferArr2 = inputBuffers;
                                                            inputSurface2 = j20;
                                                            loop4: while (true) {
                                                                if (outputSurface2 != null) {
                                                                }
                                                                checkConversionCanceled();
                                                                if (z15) {
                                                                }
                                                                i57 = i54;
                                                                if (z18) {
                                                                }
                                                                i54 = i57;
                                                                z21 = !z17;
                                                                i64 = i29;
                                                                j27 = j22;
                                                                z22 = true;
                                                                j28 = j21;
                                                                while (true) {
                                                                    if (!z21) {
                                                                    }
                                                                    checkConversionCanceled();
                                                                    if (z3) {
                                                                    }
                                                                    z23 = z21;
                                                                    createEncoderByType = mediaCodec5;
                                                                    dequeueOutputBuffer = createEncoderByType.dequeueOutputBuffer(bufferInfo2, j24);
                                                                    i71 = -1;
                                                                    if (dequeueOutputBuffer == -1) {
                                                                    }
                                                                    OutputSurface outputSurface1022 = outputSurface4;
                                                                    mediaCodec7 = mediaCodec2;
                                                                    outputSurface5 = outputSurface2;
                                                                    outputSurface2 = outputSurface1022;
                                                                    inputSurface2 = i75;
                                                                    j26 = j32;
                                                                    if (dequeueOutputBuffer == i71) {
                                                                    }
                                                                }
                                                                i13 = i4;
                                                                j21 = j28;
                                                                i29 = i64;
                                                                inputSurface2 = j25;
                                                                i50 = i59;
                                                                byteBufferArr2 = byteBufferArr3;
                                                                z15 = z19;
                                                                i48 = i60;
                                                                i42 = i24;
                                                                j18 = j5;
                                                                j22 = j27;
                                                                bufferInfo = bufferInfo2;
                                                                audioRecoder2 = audioRecoder;
                                                            }
                                                            i21 = i5;
                                                            i30 = i6;
                                                            j6 = j21;
                                                            i19 = i13;
                                                            mediaCodec4 = mediaCodec2;
                                                            audioRecoder = audioRecoder2;
                                                            j5 = j18;
                                                            outputSurface2 = outputSurface4;
                                                            i24 = i42;
                                                            z6 = false;
                                                            z13 = false;
                                                            mediaCodec3 = mediaCodec5;
                                                            mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                            if (mediaCodec4 != null) {
                                                            }
                                                            z11 = z6;
                                                            z10 = z13;
                                                            mediaCodec = mediaCodec3;
                                                            outputSurface = outputSurface2;
                                                            i14 = i29;
                                                            i20 = i30;
                                                        }
                                                    } else {
                                                        i39 = i37;
                                                    }
                                                    i22 = i26;
                                                    createEncoderByType = MediaCodec.createEncoderByType(MediaController.VIDEO_MIME_TYPE);
                                                    createEncoderByType.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
                                                    InputSurface inputSurface62 = new InputSurface(createEncoderByType.createInputSurface());
                                                    inputSurface62.makeCurrent();
                                                    createEncoderByType.start();
                                                    createDecoderByType = MediaCodec.createDecoderByType(trackFormat.getString("mime"));
                                                    float f32 = i6;
                                                    int i1242 = i34;
                                                    i41 = i39;
                                                    inputSurface = inputSurface62;
                                                    mediaCodec5 = createEncoderByType;
                                                    i42 = i35;
                                                    bufferInfo = bufferInfo5;
                                                    str4 = MediaController.VIDEO_MIME_TYPE;
                                                    str5 = "prepend-sps-pps-to-idr-frames";
                                                    i43 = i36;
                                                    str6 = str3;
                                                    j15 = j11;
                                                    mediaCodecVideoConvertor2 = this;
                                                    outputSurface4 = outputSurface3;
                                                    outputSurface3 = new OutputSurface(savedFilterState, null, str2, arrayList, cropState, i4, i5, i2, i3, i, f32, false);
                                                    if (z5) {
                                                    }
                                                    z14 = true;
                                                    mediaCodec2 = createDecoderByType;
                                                    mediaCodec2.configure(trackFormat, outputSurface4.getSurface(), (MediaCrypto) null, 0);
                                                    mediaCodec2.start();
                                                    if (i38 >= 21) {
                                                    }
                                                    i48 = i1242;
                                                    if (i48 < 0) {
                                                    }
                                                    byteBuffer = null;
                                                    j20 = j19;
                                                    if (i48 < 0) {
                                                    }
                                                    checkConversionCanceled();
                                                    j21 = j15;
                                                    long j45222 = -1;
                                                    long j46222 = -1;
                                                    ByteBuffer byteBuffer7222 = byteBuffer;
                                                    j22 = -2147483648L;
                                                    outputSurface2 = null;
                                                    z17 = false;
                                                    i29 = -5;
                                                    z18 = false;
                                                    boolean z51222 = true;
                                                    long j47222 = 0;
                                                    long j48222 = 0;
                                                    boolean z52222 = z50;
                                                    i54 = 0;
                                                    byteBufferArr2 = inputBuffers;
                                                    inputSurface2 = j20;
                                                    loop4: while (true) {
                                                        if (outputSurface2 != null) {
                                                        }
                                                        checkConversionCanceled();
                                                        if (z15) {
                                                        }
                                                        i57 = i54;
                                                        if (z18) {
                                                        }
                                                        i54 = i57;
                                                        z21 = !z17;
                                                        i64 = i29;
                                                        j27 = j22;
                                                        z22 = true;
                                                        j28 = j21;
                                                        while (true) {
                                                            if (!z21) {
                                                            }
                                                            checkConversionCanceled();
                                                            if (z3) {
                                                            }
                                                            z23 = z21;
                                                            createEncoderByType = mediaCodec5;
                                                            dequeueOutputBuffer = createEncoderByType.dequeueOutputBuffer(bufferInfo2, j24);
                                                            i71 = -1;
                                                            if (dequeueOutputBuffer == -1) {
                                                            }
                                                            OutputSurface outputSurface10222 = outputSurface4;
                                                            mediaCodec7 = mediaCodec2;
                                                            outputSurface5 = outputSurface2;
                                                            outputSurface2 = outputSurface10222;
                                                            inputSurface2 = i75;
                                                            j26 = j32;
                                                            if (dequeueOutputBuffer == i71) {
                                                            }
                                                        }
                                                        i13 = i4;
                                                        j21 = j28;
                                                        i29 = i64;
                                                        inputSurface2 = j25;
                                                        i50 = i59;
                                                        byteBufferArr2 = byteBufferArr3;
                                                        z15 = z19;
                                                        i48 = i60;
                                                        i42 = i24;
                                                        j18 = j5;
                                                        j22 = j27;
                                                        bufferInfo = bufferInfo2;
                                                        audioRecoder2 = audioRecoder;
                                                    }
                                                    i21 = i5;
                                                    i30 = i6;
                                                    j6 = j21;
                                                    i19 = i13;
                                                    mediaCodec4 = mediaCodec2;
                                                    audioRecoder = audioRecoder2;
                                                    j5 = j18;
                                                    outputSurface2 = outputSurface4;
                                                    i24 = i42;
                                                    z6 = false;
                                                    z13 = false;
                                                    mediaCodec3 = mediaCodec5;
                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                    if (mediaCodec4 != null) {
                                                    }
                                                    z11 = z6;
                                                    z10 = z13;
                                                    mediaCodec = mediaCodec3;
                                                    outputSurface = outputSurface2;
                                                    i14 = i29;
                                                    i20 = i30;
                                                } else {
                                                    i35 = findTrack2;
                                                    try {
                                                        str3 = "csd-0";
                                                        try {
                                                            this.extractor.seekTo(0L, 0);
                                                        } catch (Exception e67) {
                                                            e = e67;
                                                            i25 = i6;
                                                            j3 = j11;
                                                            i24 = i35;
                                                            mediaCodecVideoConvertor2 = this;
                                                            j5 = j2;
                                                            j7 = j3;
                                                            exc = e;
                                                            i27 = i25;
                                                            mediaCodec2 = null;
                                                            i14 = -5;
                                                            mediaCodec3 = null;
                                                            int i12022 = i27;
                                                            outputSurface2 = null;
                                                            inputSurface = null;
                                                            int i12122 = i12022;
                                                            audioRecoder = null;
                                                            i28 = i12122;
                                                            j6 = j7;
                                                            if (exc instanceof IllegalStateException) {
                                                            }
                                                            StringBuilder sb3222222222222222222222222 = new StringBuilder();
                                                            sb3222222222222222222222222.append("bitrate: ");
                                                            sb3222222222222222222222222.append(i26);
                                                            sb3222222222222222222222222.append(" framerate: ");
                                                            sb3222222222222222222222222.append(i28 == 1 ? 1 : 0);
                                                            sb3222222222222222222222222.append(" size: ");
                                                            i21 = i5;
                                                            sb3222222222222222222222222.append(i21);
                                                            sb3222222222222222222222222.append("x");
                                                            i19 = i4;
                                                            sb3222222222222222222222222.append(i19);
                                                            FileLog.e(sb3222222222222222222222222.toString());
                                                            FileLog.e(exc);
                                                            i22 = i26;
                                                            mediaCodec4 = mediaCodec2;
                                                            i29 = i14;
                                                            z6 = z11;
                                                            z13 = true;
                                                            i30 = i28;
                                                            mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                            if (mediaCodec4 != null) {
                                                            }
                                                            z11 = z6;
                                                            z10 = z13;
                                                            mediaCodec = mediaCodec3;
                                                            outputSurface = outputSurface2;
                                                            i14 = i29;
                                                            i20 = i30;
                                                            if (outputSurface != null) {
                                                            }
                                                            if (inputSurface != null) {
                                                            }
                                                            if (mediaCodec != null) {
                                                            }
                                                            if (audioRecoder != null) {
                                                            }
                                                            checkConversionCanceled();
                                                            z12 = z10;
                                                            z6 = z11;
                                                            mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                            if (mediaExtractor2 != null) {
                                                            }
                                                            mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                            if (mP4Builder2 != null) {
                                                            }
                                                            i17 = i21;
                                                            i16 = i19;
                                                            i18 = i22;
                                                            z7 = z12;
                                                            if (z6) {
                                                            }
                                                        } catch (Throwable th63) {
                                                            th = th63;
                                                            i32 = i6;
                                                            j14 = j11;
                                                            mediaCodecVideoConvertor2 = this;
                                                            i13 = i4;
                                                            j5 = j2;
                                                            j6 = j14;
                                                            th = th;
                                                            i111 = i26;
                                                            z6 = false;
                                                            i14 = -5;
                                                            i12 = i5;
                                                            i15 = i32;
                                                            FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                            FileLog.e(th);
                                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                            if (mediaExtractor != null) {
                                                            }
                                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                            if (mP4Builder != null) {
                                                            }
                                                            i16 = i13;
                                                            i17 = i12;
                                                            i18 = i111;
                                                            z7 = true;
                                                            if (z6) {
                                                            }
                                                        }
                                                    } catch (Exception e68) {
                                                        e = e68;
                                                        i25 = i6;
                                                        j3 = j11;
                                                        i24 = i35;
                                                        mediaCodecVideoConvertor2 = this;
                                                        j5 = j2;
                                                        j7 = j3;
                                                        exc = e;
                                                        i27 = i25;
                                                        mediaCodec2 = null;
                                                        i14 = -5;
                                                        mediaCodec3 = null;
                                                        int i120222 = i27;
                                                        outputSurface2 = null;
                                                        inputSurface = null;
                                                        int i121222 = i120222;
                                                        audioRecoder = null;
                                                        i28 = i121222;
                                                        j6 = j7;
                                                        if (exc instanceof IllegalStateException) {
                                                        }
                                                        StringBuilder sb32222222222222222222222222 = new StringBuilder();
                                                        sb32222222222222222222222222.append("bitrate: ");
                                                        sb32222222222222222222222222.append(i26);
                                                        sb32222222222222222222222222.append(" framerate: ");
                                                        sb32222222222222222222222222.append(i28 == 1 ? 1 : 0);
                                                        sb32222222222222222222222222.append(" size: ");
                                                        i21 = i5;
                                                        sb32222222222222222222222222.append(i21);
                                                        sb32222222222222222222222222.append("x");
                                                        i19 = i4;
                                                        sb32222222222222222222222222.append(i19);
                                                        FileLog.e(sb32222222222222222222222222.toString());
                                                        FileLog.e(exc);
                                                        i22 = i26;
                                                        mediaCodec4 = mediaCodec2;
                                                        i29 = i14;
                                                        z6 = z11;
                                                        z13 = true;
                                                        i30 = i28;
                                                        mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                        if (mediaCodec4 != null) {
                                                        }
                                                        z11 = z6;
                                                        z10 = z13;
                                                        mediaCodec = mediaCodec3;
                                                        outputSurface = outputSurface2;
                                                        i14 = i29;
                                                        i20 = i30;
                                                        if (outputSurface != null) {
                                                        }
                                                        if (inputSurface != null) {
                                                        }
                                                        if (mediaCodec != null) {
                                                        }
                                                        if (audioRecoder != null) {
                                                        }
                                                        checkConversionCanceled();
                                                        z12 = z10;
                                                        z6 = z11;
                                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                        if (mediaExtractor2 != null) {
                                                        }
                                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                        if (mP4Builder2 != null) {
                                                        }
                                                        i17 = i21;
                                                        i16 = i19;
                                                        i18 = i22;
                                                        z7 = z12;
                                                        if (z6) {
                                                        }
                                                    } catch (Throwable th64) {
                                                        th = th64;
                                                        i32 = i6;
                                                        j14 = j11;
                                                        mediaCodecVideoConvertor2 = this;
                                                        i13 = i4;
                                                        j5 = j2;
                                                        j6 = j14;
                                                        th = th;
                                                        i111 = i26;
                                                        z6 = false;
                                                        i14 = -5;
                                                        i12 = i5;
                                                        i15 = i32;
                                                        FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                                        FileLog.e(th);
                                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                        if (mediaExtractor != null) {
                                                        }
                                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                        if (mP4Builder != null) {
                                                        }
                                                        i16 = i13;
                                                        i17 = i12;
                                                        i18 = i111;
                                                        z7 = true;
                                                        if (z6) {
                                                        }
                                                    }
                                                }
                                            }
                                            outputSurface3 = new OutputSurface(savedFilterState, null, str2, arrayList, cropState, i4, i5, i2, i3, i, f32, false);
                                            if (z5) {
                                            }
                                            z14 = true;
                                            mediaCodec2 = createDecoderByType;
                                            mediaCodec2.configure(trackFormat, outputSurface4.getSurface(), (MediaCrypto) null, 0);
                                            mediaCodec2.start();
                                            if (i38 >= 21) {
                                            }
                                            i48 = i1242;
                                            if (i48 < 0) {
                                            }
                                            byteBuffer = null;
                                            j20 = j19;
                                            if (i48 < 0) {
                                            }
                                            checkConversionCanceled();
                                            j21 = j15;
                                            long j452222 = -1;
                                            long j462222 = -1;
                                            ByteBuffer byteBuffer72222 = byteBuffer;
                                            j22 = -2147483648L;
                                            outputSurface2 = null;
                                            z17 = false;
                                            i29 = -5;
                                            z18 = false;
                                            boolean z512222 = true;
                                            long j472222 = 0;
                                            long j482222 = 0;
                                            boolean z522222 = z50;
                                            i54 = 0;
                                            byteBufferArr2 = inputBuffers;
                                            inputSurface2 = j20;
                                            loop4: while (true) {
                                                if (outputSurface2 != null) {
                                                }
                                                checkConversionCanceled();
                                                if (z15) {
                                                }
                                                i57 = i54;
                                                if (z18) {
                                                }
                                                i54 = i57;
                                                z21 = !z17;
                                                i64 = i29;
                                                j27 = j22;
                                                z22 = true;
                                                j28 = j21;
                                                while (true) {
                                                    if (!z21) {
                                                    }
                                                    checkConversionCanceled();
                                                    if (z3) {
                                                    }
                                                    z23 = z21;
                                                    createEncoderByType = mediaCodec5;
                                                    dequeueOutputBuffer = createEncoderByType.dequeueOutputBuffer(bufferInfo2, j24);
                                                    i71 = -1;
                                                    if (dequeueOutputBuffer == -1) {
                                                    }
                                                    OutputSurface outputSurface102222 = outputSurface4;
                                                    mediaCodec7 = mediaCodec2;
                                                    outputSurface5 = outputSurface2;
                                                    outputSurface2 = outputSurface102222;
                                                    inputSurface2 = i75;
                                                    j26 = j32;
                                                    if (dequeueOutputBuffer == i71) {
                                                    }
                                                }
                                                i13 = i4;
                                                j21 = j28;
                                                i29 = i64;
                                                inputSurface2 = j25;
                                                i50 = i59;
                                                byteBufferArr2 = byteBufferArr3;
                                                z15 = z19;
                                                i48 = i60;
                                                i42 = i24;
                                                j18 = j5;
                                                j22 = j27;
                                                bufferInfo = bufferInfo2;
                                                audioRecoder2 = audioRecoder;
                                            }
                                            i21 = i5;
                                            i30 = i6;
                                            j6 = j21;
                                            i19 = i13;
                                            mediaCodec4 = mediaCodec2;
                                            audioRecoder = audioRecoder2;
                                            j5 = j18;
                                            outputSurface2 = outputSurface4;
                                            i24 = i42;
                                            z6 = false;
                                            z13 = false;
                                            mediaCodec3 = mediaCodec5;
                                            mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                            if (mediaCodec4 != null) {
                                            }
                                            z11 = z6;
                                            z10 = z13;
                                            mediaCodec = mediaCodec3;
                                            outputSurface = outputSurface2;
                                            i14 = i29;
                                            i20 = i30;
                                        } catch (Throwable th65) {
                                            th = th65;
                                            i11 = i6;
                                            i13 = i4;
                                            i12 = i5;
                                            j5 = j2;
                                            j6 = j15;
                                            th = th;
                                            i111 = i22;
                                            z6 = false;
                                            i14 = -5;
                                            i15 = i11;
                                            FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                            FileLog.e(th);
                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                            if (mediaExtractor != null) {
                                            }
                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                            if (mP4Builder != null) {
                                            }
                                            i16 = i13;
                                            i17 = i12;
                                            i18 = i111;
                                            z7 = true;
                                            if (z6) {
                                            }
                                        }
                                        createEncoderByType = MediaCodec.createEncoderByType(MediaController.VIDEO_MIME_TYPE);
                                        createEncoderByType.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
                                        InputSurface inputSurface622 = new InputSurface(createEncoderByType.createInputSurface());
                                        inputSurface622.makeCurrent();
                                        createEncoderByType.start();
                                        createDecoderByType = MediaCodec.createDecoderByType(trackFormat.getString("mime"));
                                        float f322 = i6;
                                        int i12422 = i34;
                                        i41 = i39;
                                        inputSurface = inputSurface622;
                                        mediaCodec5 = createEncoderByType;
                                        i42 = i35;
                                        bufferInfo = bufferInfo5;
                                        str4 = MediaController.VIDEO_MIME_TYPE;
                                        str5 = "prepend-sps-pps-to-idr-frames";
                                        i43 = i36;
                                        str6 = str3;
                                        j15 = j11;
                                        mediaCodecVideoConvertor2 = this;
                                        outputSurface4 = outputSurface3;
                                    } catch (Throwable th66) {
                                        th = th66;
                                        i11 = i6;
                                        j15 = j11;
                                        mediaCodecVideoConvertor2 = this;
                                    }
                                    cropState2 = cropState;
                                    if (cropState2 == null) {
                                    }
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    createVideoFormat = MediaFormat.createVideoFormat(MediaController.VIDEO_MIME_TYPE, i36, i37);
                                    createVideoFormat.setInteger("color-format", 2130708361);
                                    createVideoFormat.setInteger("bitrate", i26);
                                    createVideoFormat.setInteger("frame-rate", i6);
                                    createVideoFormat.setInteger("i-frame-interval", 2);
                                    i38 = Build.VERSION.SDK_INT;
                                    if (i38 >= 23) {
                                    }
                                    i22 = i26;
                                } else {
                                    i19 = i4;
                                    i20 = i6;
                                    i21 = i110;
                                    mediaCodecVideoConvertor2 = this;
                                    i22 = i7;
                                    j5 = j2;
                                    j6 = j3;
                                    z10 = false;
                                    i14 = -5;
                                    outputSurface = null;
                                    mediaCodec = null;
                                    z11 = false;
                                    inputSurface = null;
                                    audioRecoder = null;
                                }
                                if (outputSurface != null) {
                                    try {
                                        outputSurface.release();
                                    } catch (Throwable th67) {
                                        th = th67;
                                        i12 = i21;
                                        i13 = i19;
                                        i111 = i22;
                                        i23 = i20;
                                        z6 = z11;
                                        i15 = i23;
                                        FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                        FileLog.e(th);
                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                        if (mediaExtractor != null) {
                                            mediaExtractor.release();
                                        }
                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                        if (mP4Builder != null) {
                                            try {
                                                mP4Builder.finishMovie();
                                                mediaCodecVideoConvertor2.endPresentationTime = mediaCodecVideoConvertor2.mediaMuxer.getLastFrameTimestamp(i14);
                                            } catch (Throwable th68) {
                                                FileLog.e(th68);
                                            }
                                        }
                                        i16 = i13;
                                        i17 = i12;
                                        i18 = i111;
                                        z7 = true;
                                        if (z6) {
                                        }
                                    }
                                }
                                if (inputSurface != null) {
                                    inputSurface.release();
                                }
                                if (mediaCodec != null) {
                                    mediaCodec.stop();
                                    mediaCodec.release();
                                }
                                if (audioRecoder != null) {
                                    audioRecoder.release();
                                }
                                checkConversionCanceled();
                                z12 = z10;
                                z6 = z11;
                            } else {
                                try {
                                    i90 = i110;
                                    try {
                                        readAndWriteTracks(this.extractor, this.mediaMuxer, bufferInfo5, j, j2, j4, file, i111 != -1);
                                        i19 = i4;
                                        i22 = i7;
                                        j5 = j2;
                                        j6 = j3;
                                        i21 = i90;
                                        z6 = false;
                                        z12 = false;
                                        mediaCodecVideoConvertor2 = this;
                                        i14 = -5;
                                    } catch (Throwable th69) {
                                        th = th69;
                                        i13 = i4;
                                        i11 = i6;
                                        i111 = i7;
                                        j5 = j2;
                                        j6 = j3;
                                        th = th;
                                        i12 = i90;
                                        z6 = false;
                                        mediaCodecVideoConvertor2 = this;
                                        i14 = -5;
                                        i15 = i11;
                                        FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                                        FileLog.e(th);
                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                        if (mediaExtractor != null) {
                                        }
                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                        if (mP4Builder != null) {
                                        }
                                        i16 = i13;
                                        i17 = i12;
                                        i18 = i111;
                                        z7 = true;
                                        if (z6) {
                                        }
                                    }
                                } catch (Throwable th70) {
                                    th = th70;
                                    i90 = i110;
                                }
                            }
                        }
                    }
                    z8 = z2;
                    z9 = false;
                    if (!z8) {
                    }
                    if (findTrack2 < 0) {
                    }
                    if (outputSurface != null) {
                    }
                    if (inputSurface != null) {
                    }
                    if (mediaCodec != null) {
                    }
                    if (audioRecoder != null) {
                    }
                    checkConversionCanceled();
                    z12 = z10;
                    z6 = z11;
                } catch (Throwable th71) {
                    th = th71;
                    i9 = i4;
                    i10 = i110;
                    i11 = i6;
                    mediaCodecVideoConvertor2 = this;
                    i111 = i7;
                    j5 = j2;
                    j6 = j3;
                    th = th;
                    i12 = i10;
                    i13 = i9;
                    z6 = false;
                    i14 = -5;
                    i15 = i11;
                    FileLog.e("bitrate: " + i111 + " framerate: " + i15 + " size: " + i12 + "x" + i13);
                    FileLog.e(th);
                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                    if (mediaExtractor != null) {
                    }
                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                    if (mP4Builder != null) {
                    }
                    i16 = i13;
                    i17 = i12;
                    i18 = i111;
                    z7 = true;
                    if (z6) {
                    }
                }
            }
            mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
            if (mediaExtractor2 != null) {
                mediaExtractor2.release();
            }
            mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
            if (mP4Builder2 != null) {
                try {
                    mP4Builder2.finishMovie();
                    mediaCodecVideoConvertor2.endPresentationTime = mediaCodecVideoConvertor2.mediaMuxer.getLastFrameTimestamp(i14);
                } catch (Throwable th72) {
                    FileLog.e(th72);
                }
            }
            i17 = i21;
            i16 = i19;
            i18 = i22;
            z7 = z12;
        } catch (Throwable th73) {
            th = th73;
            i9 = i4;
            i10 = i110;
            i11 = i6;
        }
        if (z6) {
            return convertVideoInternal(str, file, i, z, i2, i3, i16, i17, i6, i18, i8, j, j5, j6, j4, z2, true, savedFilterState, str2, arrayList, z4, cropState, z5);
        }
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("compression completed time=" + currentTimeMillis2 + " needCompress=" + z2 + " w=" + i16 + " h=" + i17 + " bitrate=" + i18);
        }
        return z7;
    }

    /* JADX WARN: Code restructure failed: missing block: B:73:0x0123, code lost:
        if (r9[r6 + 3] != 1) goto L62;
     */
    /* JADX WARN: Removed duplicated region for block: B:119:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x01d5  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x01ec  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00df  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private long readAndWriteTracks(MediaExtractor mediaExtractor, MP4Builder mP4Builder, MediaCodec.BufferInfo bufferInfo, long j, long j2, long j3, File file, boolean z) throws Exception {
        long j4;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        boolean z2;
        int i10;
        int i11;
        boolean z3;
        long j5;
        byte[] array;
        int i12;
        int i13;
        int i14;
        int findTrack = MediaController.findTrack(mediaExtractor, false);
        if (z) {
            j4 = j3;
            i = MediaController.findTrack(mediaExtractor, true);
        } else {
            j4 = j3;
            i = -1;
        }
        float f = ((float) j4) / 1000.0f;
        if (findTrack >= 0) {
            mediaExtractor.selectTrack(findTrack);
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(findTrack);
            i3 = mP4Builder.addTrack(trackFormat, false);
            try {
                i14 = trackFormat.getInteger("max-input-size");
            } catch (Exception e) {
                FileLog.e(e);
                i14 = 0;
            }
            if (j > 0) {
                mediaExtractor.seekTo(j, 0);
            } else {
                mediaExtractor.seekTo(0L, 0);
            }
            i2 = i14;
        } else {
            i2 = 0;
            i3 = -1;
        }
        if (i >= 0) {
            mediaExtractor.selectTrack(i);
            MediaFormat trackFormat2 = mediaExtractor.getTrackFormat(i);
            if (trackFormat2.getString("mime").equals("audio/unknown")) {
                i4 = -1;
                i = -1;
            } else {
                i4 = mP4Builder.addTrack(trackFormat2, true);
                try {
                    i2 = Math.max(trackFormat2.getInteger("max-input-size"), i2);
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                if (j > 0) {
                    mediaExtractor.seekTo(j, 0);
                } else {
                    mediaExtractor.seekTo(0L, 0);
                }
            }
        } else {
            i4 = -1;
        }
        if (i2 <= 0) {
            i2 = CharacterCompat.MIN_SUPPLEMENTARY_CODE_POINT;
        }
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i2);
        if (i >= 0 || findTrack >= 0) {
            checkConversionCanceled();
            long j6 = 0;
            long j7 = -1;
            boolean z4 = false;
            while (!z4) {
                checkConversionCanceled();
                int i15 = Build.VERSION.SDK_INT;
                if (i15 >= 28) {
                    long sampleSize = mediaExtractor.getSampleSize();
                    i5 = i;
                    if (sampleSize > i2) {
                        int i16 = (int) (sampleSize + 1024);
                        i2 = i16;
                        allocateDirect = ByteBuffer.allocateDirect(i16);
                    }
                } else {
                    i5 = i;
                }
                bufferInfo.size = mediaExtractor.readSampleData(allocateDirect, 0);
                int sampleTrackIndex = mediaExtractor.getSampleTrackIndex();
                if (sampleTrackIndex == findTrack) {
                    i6 = i5;
                    i8 = i3;
                } else {
                    i6 = i5;
                    if (sampleTrackIndex == i6) {
                        i8 = i4;
                    } else {
                        i7 = -1;
                        i8 = -1;
                        if (i8 == i7) {
                            if (i15 < 21) {
                                allocateDirect.position(0);
                                allocateDirect.limit(bufferInfo.size);
                            }
                            if (sampleTrackIndex != i6 && (array = allocateDirect.array()) != null) {
                                int arrayOffset = allocateDirect.arrayOffset();
                                int limit = arrayOffset + allocateDirect.limit();
                                i9 = i4;
                                int i17 = arrayOffset;
                                int i18 = -1;
                                while (true) {
                                    z2 = z4;
                                    int i19 = limit - 4;
                                    if (i17 > i19) {
                                        break;
                                    }
                                    if (array[i17] == 0 && array[i17 + 1] == 0 && array[i17 + 2] == 0) {
                                        i12 = i2;
                                        i13 = i6;
                                    } else {
                                        i12 = i2;
                                        i13 = i6;
                                    }
                                    if (i17 != i19) {
                                        i17++;
                                        z4 = z2;
                                        i6 = i13;
                                        i2 = i12;
                                    }
                                    if (i18 != -1) {
                                        int i20 = (i17 - i18) - (i17 == i19 ? 0 : 4);
                                        array[i18] = (byte) (i20 >> 24);
                                        array[i18 + 1] = (byte) (i20 >> 16);
                                        array[i18 + 2] = (byte) (i20 >> 8);
                                        array[i18 + 3] = (byte) i20;
                                    }
                                    i18 = i17;
                                    i17++;
                                    z4 = z2;
                                    i6 = i13;
                                    i2 = i12;
                                }
                            } else {
                                i9 = i4;
                                z2 = z4;
                            }
                            i10 = i2;
                            i11 = i6;
                            if (bufferInfo.size >= 0) {
                                bufferInfo.presentationTimeUs = mediaExtractor.getSampleTime();
                                z3 = false;
                            } else {
                                bufferInfo.size = 0;
                                z3 = true;
                            }
                            if (bufferInfo.size > 0 && !z3) {
                                if (sampleTrackIndex == findTrack) {
                                    j5 = 0;
                                    if (j > 0 && j7 == -1) {
                                        j7 = bufferInfo.presentationTimeUs;
                                    }
                                } else {
                                    j5 = 0;
                                }
                                if (j2 < j5 || bufferInfo.presentationTimeUs < j2) {
                                    bufferInfo.offset = 0;
                                    bufferInfo.flags = mediaExtractor.getSampleFlags();
                                    long writeSampleData = mP4Builder.writeSampleData(i8, allocateDirect, bufferInfo, false);
                                    if (writeSampleData != 0) {
                                        MediaController.VideoConvertorListener videoConvertorListener = this.callback;
                                        if (videoConvertorListener != null) {
                                            long j8 = bufferInfo.presentationTimeUs;
                                            long j9 = j8 - j7 > j6 ? j8 - j7 : j6;
                                            videoConvertorListener.didWriteData(writeSampleData, (((float) j9) / 1000.0f) / f);
                                            j6 = j9;
                                            if (!z3) {
                                                mediaExtractor.advance();
                                            }
                                        }
                                        if (!z3) {
                                        }
                                    }
                                } else {
                                    z3 = true;
                                }
                            }
                            if (!z3) {
                            }
                        } else {
                            i9 = i4;
                            z2 = z4;
                            i10 = i2;
                            i11 = i6;
                            if (sampleTrackIndex == -1) {
                                z3 = true;
                            } else {
                                mediaExtractor.advance();
                                z3 = false;
                            }
                        }
                        z4 = !z3 ? true : z2;
                        i4 = i9;
                        i = i11;
                        i2 = i10;
                    }
                }
                i7 = -1;
                if (i8 == i7) {
                }
                if (!z3) {
                }
                i4 = i9;
                i = i11;
                i2 = i10;
            }
            int i21 = i;
            if (findTrack >= 0) {
                mediaExtractor.unselectTrack(findTrack);
            }
            if (i21 >= 0) {
                mediaExtractor.unselectTrack(i21);
            }
            return j7;
        }
        return -1L;
    }

    private void checkConversionCanceled() {
        MediaController.VideoConvertorListener videoConvertorListener = this.callback;
        if (videoConvertorListener != null && videoConvertorListener.checkConversionCanceled()) {
            throw new ConversionCanceledException();
        }
    }

    private static String createFragmentShader(int i, int i2, int i3, int i4, boolean z) {
        int clamp = (int) Utilities.clamp((Math.max(i, i2) / Math.max(i4, i3)) * 0.8f, 2.0f, 1.0f);
        FileLog.d("source size " + i + "x" + i2 + "    dest size " + i3 + i4 + "   kernelRadius " + clamp);
        if (z) {
            return "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nconst float kernel = " + clamp + ".0;\nconst float pixelSizeX = 1.0 / " + i + ".0;\nconst float pixelSizeY = 1.0 / " + i2 + ".0;\nuniform samplerExternalOES sTexture;\nvoid main() {\nvec3 accumulation = vec3(0);\nvec3 weightsum = vec3(0);\nfor (float x = -kernel; x <= kernel; x++){\n   for (float y = -kernel; y <= kernel; y++){\n       accumulation += texture2D(sTexture, vTextureCoord + vec2(x * pixelSizeX, y * pixelSizeY)).xyz;\n       weightsum += 1.0;\n   }\n}\ngl_FragColor = vec4(accumulation / weightsum, 1.0);\n}\n";
        }
        return "precision mediump float;\nvarying vec2 vTextureCoord;\nconst float kernel = " + clamp + ".0;\nconst float pixelSizeX = 1.0 / " + i2 + ".0;\nconst float pixelSizeY = 1.0 / " + i + ".0;\nuniform sampler2D sTexture;\nvoid main() {\nvec3 accumulation = vec3(0);\nvec3 weightsum = vec3(0);\nfor (float x = -kernel; x <= kernel; x++){\n   for (float y = -kernel; y <= kernel; y++){\n       accumulation += texture2D(sTexture, vTextureCoord + vec2(x * pixelSizeX, y * pixelSizeY)).xyz;\n       weightsum += 1.0;\n   }\n}\ngl_FragColor = vec4(accumulation / weightsum, 1.0);\n}\n";
    }

    /* loaded from: classes.dex */
    public class ConversionCanceledException extends RuntimeException {
        public ConversionCanceledException() {
            super("canceled conversion");
        }
    }
}
