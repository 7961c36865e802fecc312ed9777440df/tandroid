package com.google.android.exoplayer2.text.ttml;

import android.text.Layout;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.util.ColorParser;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.XmlPullParserUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
/* loaded from: classes.dex */
public final class TtmlDecoder extends SimpleSubtitleDecoder {
    private final XmlPullParserFactory xmlParserFactory;
    private static final Pattern CLOCK_TIME = Pattern.compile("^([0-9][0-9]+):([0-9][0-9]):([0-9][0-9])(?:(\\.[0-9]+)|:([0-9][0-9])(?:\\.([0-9]+))?)?$");
    private static final Pattern OFFSET_TIME = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)(h|m|s|ms|f|t)$");
    private static final Pattern FONT_SIZE = Pattern.compile("^(([0-9]*.)?[0-9]+)(px|em|%)$");
    private static final Pattern PERCENTAGE_COORDINATES = Pattern.compile("^(\\d+\\.?\\d*?)% (\\d+\\.?\\d*?)%$");
    private static final Pattern PIXEL_COORDINATES = Pattern.compile("^(\\d+\\.?\\d*?)px (\\d+\\.?\\d*?)px$");
    private static final Pattern CELL_RESOLUTION = Pattern.compile("^(\\d+) (\\d+)$");
    private static final FrameAndTickRate DEFAULT_FRAME_AND_TICK_RATE = new FrameAndTickRate(30.0f, 1, 1);
    private static final CellResolution DEFAULT_CELL_RESOLUTION = new CellResolution(32, 15);

    public TtmlDecoder() {
        super("TtmlDecoder");
        try {
            XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
            this.xmlParserFactory = newInstance;
            newInstance.setNamespaceAware(true);
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    @Override // com.google.android.exoplayer2.text.SimpleSubtitleDecoder
    protected Subtitle decode(byte[] bArr, int i, boolean z) throws SubtitleDecoderException {
        TtmlSubtitle ttmlSubtitle;
        FrameAndTickRate frameAndTickRate;
        try {
            XmlPullParser newPullParser = this.xmlParserFactory.newPullParser();
            HashMap hashMap = new HashMap();
            HashMap hashMap2 = new HashMap();
            HashMap hashMap3 = new HashMap();
            TtsExtent ttsExtent = null;
            hashMap2.put("", new TtmlRegion(null));
            newPullParser.setInput(new ByteArrayInputStream(bArr, 0, i), null);
            ArrayDeque arrayDeque = new ArrayDeque();
            FrameAndTickRate frameAndTickRate2 = DEFAULT_FRAME_AND_TICK_RATE;
            CellResolution cellResolution = DEFAULT_CELL_RESOLUTION;
            TtmlSubtitle ttmlSubtitle2 = null;
            int i2 = 0;
            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.getEventType()) {
                TtmlNode ttmlNode = (TtmlNode) arrayDeque.peek();
                if (i2 == 0) {
                    String name = newPullParser.getName();
                    if (eventType == 2) {
                        if ("tt".equals(name)) {
                            frameAndTickRate2 = parseFrameAndTickRates(newPullParser);
                            cellResolution = parseCellResolution(newPullParser, DEFAULT_CELL_RESOLUTION);
                            ttsExtent = parseTtsExtent(newPullParser);
                        }
                        TtsExtent ttsExtent2 = ttsExtent;
                        FrameAndTickRate frameAndTickRate3 = frameAndTickRate2;
                        CellResolution cellResolution2 = cellResolution;
                        if (isSupportedTag(name)) {
                            if ("head".equals(name)) {
                                ttmlSubtitle = ttmlSubtitle2;
                                frameAndTickRate = frameAndTickRate3;
                                parseHeader(newPullParser, hashMap, cellResolution2, ttsExtent2, hashMap2, hashMap3);
                            } else {
                                ttmlSubtitle = ttmlSubtitle2;
                                frameAndTickRate = frameAndTickRate3;
                                try {
                                    TtmlNode parseNode = parseNode(newPullParser, ttmlNode, hashMap2, frameAndTickRate);
                                    arrayDeque.push(parseNode);
                                    if (ttmlNode != null) {
                                        ttmlNode.addChild(parseNode);
                                    }
                                } catch (SubtitleDecoderException e) {
                                    Log.w("TtmlDecoder", "Suppressing parser error", e);
                                    i2++;
                                }
                            }
                            frameAndTickRate2 = frameAndTickRate;
                            ttsExtent = ttsExtent2;
                            cellResolution = cellResolution2;
                        } else {
                            Log.i("TtmlDecoder", "Ignoring unsupported tag: " + newPullParser.getName());
                            i2++;
                            frameAndTickRate2 = frameAndTickRate3;
                            ttsExtent = ttsExtent2;
                            cellResolution = cellResolution2;
                        }
                    } else {
                        ttmlSubtitle = ttmlSubtitle2;
                        if (eventType == 4) {
                            ttmlNode.addChild(TtmlNode.buildTextNode(newPullParser.getText()));
                        } else if (eventType == 3) {
                            ttmlSubtitle2 = newPullParser.getName().equals("tt") ? new TtmlSubtitle((TtmlNode) arrayDeque.peek(), hashMap, hashMap2, hashMap3) : ttmlSubtitle;
                            arrayDeque.pop();
                        }
                    }
                    newPullParser.next();
                } else {
                    ttmlSubtitle = ttmlSubtitle2;
                    if (eventType == 2) {
                        i2++;
                    } else if (eventType == 3) {
                        i2--;
                    }
                }
                ttmlSubtitle2 = ttmlSubtitle;
                newPullParser.next();
            }
            return ttmlSubtitle2;
        } catch (IOException e2) {
            throw new IllegalStateException("Unexpected error when reading input.", e2);
        } catch (XmlPullParserException e3) {
            throw new SubtitleDecoderException("Unable to decode source", e3);
        }
    }

    private FrameAndTickRate parseFrameAndTickRates(XmlPullParser xmlPullParser) throws SubtitleDecoderException {
        String attributeValue = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "frameRate");
        int parseInt = attributeValue != null ? Integer.parseInt(attributeValue) : 30;
        float f = 1.0f;
        String attributeValue2 = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "frameRateMultiplier");
        if (attributeValue2 != null) {
            String[] split = Util.split(attributeValue2, " ");
            if (split.length != 2) {
                throw new SubtitleDecoderException("frameRateMultiplier doesn't have 2 parts");
            }
            f = Integer.parseInt(split[0]) / Integer.parseInt(split[1]);
        }
        FrameAndTickRate frameAndTickRate = DEFAULT_FRAME_AND_TICK_RATE;
        int i = frameAndTickRate.subFrameRate;
        String attributeValue3 = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "subFrameRate");
        if (attributeValue3 != null) {
            i = Integer.parseInt(attributeValue3);
        }
        int i2 = frameAndTickRate.tickRate;
        String attributeValue4 = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "tickRate");
        if (attributeValue4 != null) {
            i2 = Integer.parseInt(attributeValue4);
        }
        return new FrameAndTickRate(parseInt * f, i, i2);
    }

    private CellResolution parseCellResolution(XmlPullParser xmlPullParser, CellResolution cellResolution) throws SubtitleDecoderException {
        String attributeValue = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "cellResolution");
        if (attributeValue == null) {
            return cellResolution;
        }
        Matcher matcher = CELL_RESOLUTION.matcher(attributeValue);
        if (!matcher.matches()) {
            Log.w("TtmlDecoder", "Ignoring malformed cell resolution: " + attributeValue);
            return cellResolution;
        }
        try {
            int parseInt = Integer.parseInt(matcher.group(1));
            int parseInt2 = Integer.parseInt(matcher.group(2));
            if (parseInt == 0 || parseInt2 == 0) {
                throw new SubtitleDecoderException("Invalid cell resolution " + parseInt + " " + parseInt2);
            }
            return new CellResolution(parseInt, parseInt2);
        } catch (NumberFormatException unused) {
            Log.w("TtmlDecoder", "Ignoring malformed cell resolution: " + attributeValue);
            return cellResolution;
        }
    }

    private TtsExtent parseTtsExtent(XmlPullParser xmlPullParser) {
        String attributeValue = XmlPullParserUtil.getAttributeValue(xmlPullParser, "extent");
        if (attributeValue == null) {
            return null;
        }
        Matcher matcher = PIXEL_COORDINATES.matcher(attributeValue);
        if (!matcher.matches()) {
            Log.w("TtmlDecoder", "Ignoring non-pixel tts extent: " + attributeValue);
            return null;
        }
        try {
            return new TtsExtent(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        } catch (NumberFormatException unused) {
            Log.w("TtmlDecoder", "Ignoring malformed tts extent: " + attributeValue);
            return null;
        }
    }

    private Map<String, TtmlStyle> parseHeader(XmlPullParser xmlPullParser, Map<String, TtmlStyle> map, CellResolution cellResolution, TtsExtent ttsExtent, Map<String, TtmlRegion> map2, Map<String, String> map3) throws IOException, XmlPullParserException {
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "style")) {
                String attributeValue = XmlPullParserUtil.getAttributeValue(xmlPullParser, "style");
                TtmlStyle parseStyleAttributes = parseStyleAttributes(xmlPullParser, new TtmlStyle());
                if (attributeValue != null) {
                    for (String str : parseStyleIds(attributeValue)) {
                        parseStyleAttributes.chain(map.get(str));
                    }
                }
                if (parseStyleAttributes.getId() != null) {
                    map.put(parseStyleAttributes.getId(), parseStyleAttributes);
                }
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "region")) {
                TtmlRegion parseRegionAttributes = parseRegionAttributes(xmlPullParser, cellResolution, ttsExtent);
                if (parseRegionAttributes != null) {
                    map2.put(parseRegionAttributes.id, parseRegionAttributes);
                }
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "metadata")) {
                parseMetadata(xmlPullParser, map3);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "head"));
        return map;
    }

    private void parseMetadata(XmlPullParser xmlPullParser, Map<String, String> map) throws IOException, XmlPullParserException {
        String attributeValue;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "image") && (attributeValue = XmlPullParserUtil.getAttributeValue(xmlPullParser, "id")) != null) {
                map.put(attributeValue, xmlPullParser.nextText());
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "metadata"));
    }

    private TtmlRegion parseRegionAttributes(XmlPullParser xmlPullParser, CellResolution cellResolution, TtsExtent ttsExtent) {
        float parseFloat;
        float f;
        float parseFloat2;
        float parseFloat3;
        float f2;
        int i;
        String attributeValue = XmlPullParserUtil.getAttributeValue(xmlPullParser, "id");
        if (attributeValue == null) {
            return null;
        }
        String attributeValue2 = XmlPullParserUtil.getAttributeValue(xmlPullParser, "origin");
        if (attributeValue2 != null) {
            Pattern pattern = PERCENTAGE_COORDINATES;
            Matcher matcher = pattern.matcher(attributeValue2);
            Pattern pattern2 = PIXEL_COORDINATES;
            Matcher matcher2 = pattern2.matcher(attributeValue2);
            if (matcher.matches()) {
                try {
                    float parseFloat4 = Float.parseFloat(matcher.group(1)) / 100.0f;
                    parseFloat = Float.parseFloat(matcher.group(2)) / 100.0f;
                    f = parseFloat4;
                } catch (NumberFormatException unused) {
                    Log.w("TtmlDecoder", "Ignoring region with malformed origin: " + attributeValue2);
                    return null;
                }
            } else if (!matcher2.matches()) {
                Log.w("TtmlDecoder", "Ignoring region with unsupported origin: " + attributeValue2);
                return null;
            } else if (ttsExtent == null) {
                Log.w("TtmlDecoder", "Ignoring region with missing tts:extent: " + attributeValue2);
                return null;
            } else {
                try {
                    int parseInt = Integer.parseInt(matcher2.group(1));
                    int parseInt2 = Integer.parseInt(matcher2.group(2));
                    f = parseInt / ttsExtent.width;
                    parseFloat = parseInt2 / ttsExtent.height;
                } catch (NumberFormatException unused2) {
                    Log.w("TtmlDecoder", "Ignoring region with malformed origin: " + attributeValue2);
                    return null;
                }
            }
            String attributeValue3 = XmlPullParserUtil.getAttributeValue(xmlPullParser, "extent");
            if (attributeValue3 != null) {
                Matcher matcher3 = pattern.matcher(attributeValue3);
                Matcher matcher4 = pattern2.matcher(attributeValue3);
                if (matcher3.matches()) {
                    try {
                        parseFloat2 = Float.parseFloat(matcher3.group(1)) / 100.0f;
                        parseFloat3 = Float.parseFloat(matcher3.group(2)) / 100.0f;
                    } catch (NumberFormatException unused3) {
                        Log.w("TtmlDecoder", "Ignoring region with malformed extent: " + attributeValue2);
                        return null;
                    }
                } else if (!matcher4.matches()) {
                    Log.w("TtmlDecoder", "Ignoring region with unsupported extent: " + attributeValue2);
                    return null;
                } else if (ttsExtent == null) {
                    Log.w("TtmlDecoder", "Ignoring region with missing tts:extent: " + attributeValue2);
                    return null;
                } else {
                    try {
                        int parseInt3 = Integer.parseInt(matcher4.group(1));
                        int parseInt4 = Integer.parseInt(matcher4.group(2));
                        float f3 = parseInt3 / ttsExtent.width;
                        parseFloat2 = f3;
                        parseFloat3 = parseInt4 / ttsExtent.height;
                    } catch (NumberFormatException unused4) {
                        Log.w("TtmlDecoder", "Ignoring region with malformed extent: " + attributeValue2);
                        return null;
                    }
                }
                String attributeValue4 = XmlPullParserUtil.getAttributeValue(xmlPullParser, "displayAlign");
                if (attributeValue4 != null) {
                    String lowerInvariant = Util.toLowerInvariant(attributeValue4);
                    lowerInvariant.hashCode();
                    if (lowerInvariant.equals("center")) {
                        f2 = parseFloat + (parseFloat3 / 2.0f);
                        i = 1;
                    } else if (lowerInvariant.equals("after")) {
                        f2 = parseFloat + parseFloat3;
                        i = 2;
                    }
                    return new TtmlRegion(attributeValue, f, f2, 0, i, parseFloat2, parseFloat3, 1, 1.0f / cellResolution.rows);
                }
                f2 = parseFloat;
                i = 0;
                return new TtmlRegion(attributeValue, f, f2, 0, i, parseFloat2, parseFloat3, 1, 1.0f / cellResolution.rows);
            }
            Log.w("TtmlDecoder", "Ignoring region without an extent");
            return null;
        }
        Log.w("TtmlDecoder", "Ignoring region without an origin");
        return null;
    }

    private String[] parseStyleIds(String str) {
        String trim = str.trim();
        return trim.isEmpty() ? new String[0] : Util.split(trim, "\\s+");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0131, code lost:
        if (r3.equals("linethrough") == false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0198, code lost:
        if (r3.equals("start") == false) goto L56;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private TtmlStyle parseStyleAttributes(XmlPullParser xmlPullParser, TtmlStyle ttmlStyle) {
        char c;
        int attributeCount = xmlPullParser.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeValue = xmlPullParser.getAttributeValue(i);
            String attributeName = xmlPullParser.getAttributeName(i);
            attributeName.hashCode();
            char c2 = 4;
            char c3 = 3;
            switch (attributeName.hashCode()) {
                case -1550943582:
                    if (attributeName.equals("fontStyle")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -1224696685:
                    if (attributeName.equals("fontFamily")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case -1065511464:
                    if (attributeName.equals("textAlign")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case -879295043:
                    if (attributeName.equals("textDecoration")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case -734428249:
                    if (attributeName.equals("fontWeight")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 3355:
                    if (attributeName.equals("id")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 94842723:
                    if (attributeName.equals("color")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 365601008:
                    if (attributeName.equals("fontSize")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 1287124693:
                    if (attributeName.equals("backgroundColor")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    ttmlStyle = createIfNull(ttmlStyle).setItalic("italic".equalsIgnoreCase(attributeValue));
                    break;
                case 1:
                    ttmlStyle = createIfNull(ttmlStyle).setFontFamily(attributeValue);
                    break;
                case 2:
                    String lowerInvariant = Util.toLowerInvariant(attributeValue);
                    lowerInvariant.hashCode();
                    switch (lowerInvariant.hashCode()) {
                        case -1364013995:
                            if (lowerInvariant.equals("center")) {
                                c2 = 0;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 100571:
                            if (lowerInvariant.equals("end")) {
                                c2 = 1;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 3317767:
                            if (lowerInvariant.equals("left")) {
                                c2 = 2;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 108511772:
                            if (lowerInvariant.equals("right")) {
                                c2 = 3;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 109757538:
                            break;
                        default:
                            c2 = 65535;
                            break;
                    }
                    switch (c2) {
                        case 0:
                            ttmlStyle = createIfNull(ttmlStyle).setTextAlign(Layout.Alignment.ALIGN_CENTER);
                            continue;
                        case 1:
                            ttmlStyle = createIfNull(ttmlStyle).setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
                            continue;
                        case 2:
                            ttmlStyle = createIfNull(ttmlStyle).setTextAlign(Layout.Alignment.ALIGN_NORMAL);
                            continue;
                        case 3:
                            ttmlStyle = createIfNull(ttmlStyle).setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
                            continue;
                        case 4:
                            ttmlStyle = createIfNull(ttmlStyle).setTextAlign(Layout.Alignment.ALIGN_NORMAL);
                            continue;
                    }
                case 3:
                    String lowerInvariant2 = Util.toLowerInvariant(attributeValue);
                    lowerInvariant2.hashCode();
                    switch (lowerInvariant2.hashCode()) {
                        case -1461280213:
                            if (lowerInvariant2.equals("nounderline")) {
                                c3 = 0;
                                break;
                            }
                            c3 = 65535;
                            break;
                        case -1026963764:
                            if (lowerInvariant2.equals("underline")) {
                                c3 = 1;
                                break;
                            }
                            c3 = 65535;
                            break;
                        case 913457136:
                            if (lowerInvariant2.equals("nolinethrough")) {
                                c3 = 2;
                                break;
                            }
                            c3 = 65535;
                            break;
                        case 1679736913:
                            break;
                        default:
                            c3 = 65535;
                            break;
                    }
                    switch (c3) {
                        case 0:
                            ttmlStyle = createIfNull(ttmlStyle).setUnderline(false);
                            continue;
                        case 1:
                            ttmlStyle = createIfNull(ttmlStyle).setUnderline(true);
                            continue;
                        case 2:
                            ttmlStyle = createIfNull(ttmlStyle).setLinethrough(false);
                            continue;
                        case 3:
                            ttmlStyle = createIfNull(ttmlStyle).setLinethrough(true);
                            continue;
                    }
                case 4:
                    ttmlStyle = createIfNull(ttmlStyle).setBold("bold".equalsIgnoreCase(attributeValue));
                    break;
                case 5:
                    if ("style".equals(xmlPullParser.getName())) {
                        ttmlStyle = createIfNull(ttmlStyle).setId(attributeValue);
                        break;
                    } else {
                        break;
                    }
                case 6:
                    ttmlStyle = createIfNull(ttmlStyle);
                    try {
                        ttmlStyle.setFontColor(ColorParser.parseTtmlColor(attributeValue));
                        break;
                    } catch (IllegalArgumentException unused) {
                        Log.w("TtmlDecoder", "Failed parsing color value: " + attributeValue);
                        break;
                    }
                case 7:
                    try {
                        ttmlStyle = createIfNull(ttmlStyle);
                        parseFontSize(attributeValue, ttmlStyle);
                        break;
                    } catch (SubtitleDecoderException unused2) {
                        Log.w("TtmlDecoder", "Failed parsing fontSize value: " + attributeValue);
                        break;
                    }
                case '\b':
                    ttmlStyle = createIfNull(ttmlStyle);
                    try {
                        ttmlStyle.setBackgroundColor(ColorParser.parseTtmlColor(attributeValue));
                        break;
                    } catch (IllegalArgumentException unused3) {
                        Log.w("TtmlDecoder", "Failed parsing background value: " + attributeValue);
                        break;
                    }
            }
        }
        return ttmlStyle;
    }

    private TtmlStyle createIfNull(TtmlStyle ttmlStyle) {
        return ttmlStyle == null ? new TtmlStyle() : ttmlStyle;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private TtmlNode parseNode(XmlPullParser xmlPullParser, TtmlNode ttmlNode, Map<String, TtmlRegion> map, FrameAndTickRate frameAndTickRate) throws SubtitleDecoderException {
        long j;
        long j2;
        char c;
        int attributeCount = xmlPullParser.getAttributeCount();
        TtmlStyle parseStyleAttributes = parseStyleAttributes(xmlPullParser, null);
        String[] strArr = null;
        String str = null;
        String str2 = "";
        long j3 = -9223372036854775807L;
        long j4 = -9223372036854775807L;
        long j5 = -9223372036854775807L;
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            String attributeValue = xmlPullParser.getAttributeValue(i);
            attributeName.hashCode();
            switch (attributeName.hashCode()) {
                case -934795532:
                    if (attributeName.equals("region")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 99841:
                    if (attributeName.equals("dur")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 100571:
                    if (attributeName.equals("end")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 93616297:
                    if (attributeName.equals("begin")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 109780401:
                    if (attributeName.equals("style")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 1292595405:
                    if (attributeName.equals("backgroundImage")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    if (map.containsKey(attributeValue)) {
                        str2 = attributeValue;
                        continue;
                    }
                case 1:
                    j5 = parseTimeExpression(attributeValue, frameAndTickRate);
                    break;
                case 2:
                    j4 = parseTimeExpression(attributeValue, frameAndTickRate);
                    break;
                case 3:
                    j3 = parseTimeExpression(attributeValue, frameAndTickRate);
                    break;
                case 4:
                    String[] parseStyleIds = parseStyleIds(attributeValue);
                    if (parseStyleIds.length > 0) {
                        strArr = parseStyleIds;
                        break;
                    }
                    break;
                case 5:
                    if (attributeValue.startsWith("#")) {
                        str = attributeValue.substring(1);
                        break;
                    }
                    break;
            }
        }
        if (ttmlNode != null) {
            long j6 = ttmlNode.startTimeUs;
            j = -9223372036854775807L;
            if (j6 != -9223372036854775807L) {
                if (j3 != -9223372036854775807L) {
                    j3 += j6;
                }
                if (j4 != -9223372036854775807L) {
                    j4 += j6;
                }
            }
        } else {
            j = -9223372036854775807L;
        }
        long j7 = j3;
        if (j4 == j) {
            if (j5 != j) {
                j2 = j7 + j5;
            } else if (ttmlNode != null) {
                long j8 = ttmlNode.endTimeUs;
                if (j8 != j) {
                    j2 = j8;
                }
            }
            return TtmlNode.buildNode(xmlPullParser.getName(), j7, j2, parseStyleAttributes, strArr, str2, str);
        }
        j2 = j4;
        return TtmlNode.buildNode(xmlPullParser.getName(), j7, j2, parseStyleAttributes, strArr, str2, str);
    }

    private static boolean isSupportedTag(String str) {
        return str.equals("tt") || str.equals("head") || str.equals("body") || str.equals("div") || str.equals("p") || str.equals("span") || str.equals("br") || str.equals("style") || str.equals("styling") || str.equals("layout") || str.equals("region") || str.equals("metadata") || str.equals("image") || str.equals("data") || str.equals("information");
    }

    private static void parseFontSize(String str, TtmlStyle ttmlStyle) throws SubtitleDecoderException {
        Matcher matcher;
        String[] split = Util.split(str, "\\s+");
        if (split.length == 1) {
            matcher = FONT_SIZE.matcher(str);
        } else if (split.length == 2) {
            matcher = FONT_SIZE.matcher(split[1]);
            Log.w("TtmlDecoder", "Multiple values in fontSize attribute. Picking the second value for vertical font size and ignoring the first.");
        } else {
            throw new SubtitleDecoderException("Invalid number of entries for fontSize: " + split.length + ".");
        }
        if (matcher.matches()) {
            String group = matcher.group(3);
            group.hashCode();
            char c = 65535;
            switch (group.hashCode()) {
                case 37:
                    if (group.equals("%")) {
                        c = 0;
                        break;
                    }
                    break;
                case 3240:
                    if (group.equals("em")) {
                        c = 1;
                        break;
                    }
                    break;
                case 3592:
                    if (group.equals("px")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    ttmlStyle.setFontSizeUnit(3);
                    break;
                case 1:
                    ttmlStyle.setFontSizeUnit(2);
                    break;
                case 2:
                    ttmlStyle.setFontSizeUnit(1);
                    break;
                default:
                    throw new SubtitleDecoderException("Invalid unit for fontSize: '" + group + "'.");
            }
            ttmlStyle.setFontSize(Float.valueOf(matcher.group(1)).floatValue());
            return;
        }
        throw new SubtitleDecoderException("Invalid expression for fontSize: '" + str + "'.");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00b3, code lost:
        if (r13.equals("ms") == false) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static long parseTimeExpression(String str, FrameAndTickRate frameAndTickRate) throws SubtitleDecoderException {
        double d;
        double d2;
        String group;
        Matcher matcher = CLOCK_TIME.matcher(str);
        char c = 4;
        if (matcher.matches()) {
            double parseLong = Long.parseLong(matcher.group(1)) * 3600;
            double parseLong2 = Long.parseLong(matcher.group(2)) * 60;
            Double.isNaN(parseLong);
            Double.isNaN(parseLong2);
            double parseLong3 = Long.parseLong(matcher.group(3));
            Double.isNaN(parseLong3);
            double d3 = parseLong + parseLong2 + parseLong3;
            String group2 = matcher.group(4);
            double d4 = 0.0d;
            double parseDouble = d3 + (group2 != null ? Double.parseDouble(group2) : 0.0d) + (matcher.group(5) != null ? ((float) Long.parseLong(group)) / frameAndTickRate.effectiveFrameRate : 0.0d);
            String group3 = matcher.group(6);
            if (group3 != null) {
                double parseLong4 = Long.parseLong(group3);
                double d5 = frameAndTickRate.subFrameRate;
                Double.isNaN(parseLong4);
                Double.isNaN(d5);
                double d6 = frameAndTickRate.effectiveFrameRate;
                Double.isNaN(d6);
                d4 = (parseLong4 / d5) / d6;
            }
            return (long) ((parseDouble + d4) * 1000000.0d);
        }
        Matcher matcher2 = OFFSET_TIME.matcher(str);
        if (matcher2.matches()) {
            double parseDouble2 = Double.parseDouble(matcher2.group(1));
            String group4 = matcher2.group(2);
            group4.hashCode();
            switch (group4.hashCode()) {
                case 102:
                    if (group4.equals("f")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 104:
                    if (group4.equals("h")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 109:
                    if (group4.equals("m")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 116:
                    if (group4.equals("t")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 3494:
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    d = frameAndTickRate.effectiveFrameRate;
                    Double.isNaN(d);
                    parseDouble2 /= d;
                    return (long) (parseDouble2 * 1000000.0d);
                case 1:
                    d2 = 3600.0d;
                    parseDouble2 *= d2;
                    return (long) (parseDouble2 * 1000000.0d);
                case 2:
                    d2 = 60.0d;
                    parseDouble2 *= d2;
                    return (long) (parseDouble2 * 1000000.0d);
                case 3:
                    d = frameAndTickRate.tickRate;
                    Double.isNaN(d);
                    parseDouble2 /= d;
                    return (long) (parseDouble2 * 1000000.0d);
                case 4:
                    d = 1000.0d;
                    parseDouble2 /= d;
                    return (long) (parseDouble2 * 1000000.0d);
                default:
                    return (long) (parseDouble2 * 1000000.0d);
            }
        }
        throw new SubtitleDecoderException("Malformed time expression: " + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class FrameAndTickRate {
        final float effectiveFrameRate;
        final int subFrameRate;
        final int tickRate;

        FrameAndTickRate(float f, int i, int i2) {
            this.effectiveFrameRate = f;
            this.subFrameRate = i;
            this.tickRate = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class CellResolution {
        final int rows;

        CellResolution(int i, int i2) {
            this.rows = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class TtsExtent {
        final int height;
        final int width;

        TtsExtent(int i, int i2) {
            this.width = i;
            this.height = i2;
        }
    }
}
