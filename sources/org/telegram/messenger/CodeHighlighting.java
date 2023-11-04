package org.telegram.messenger;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.TextStyleSpan;
/* loaded from: classes.dex */
public class CodeHighlighting {
    public static final int MATCH_COMMENT = 6;
    public static final int MATCH_CONSTANT = 3;
    public static final int MATCH_FUNCTION = 7;
    public static final int MATCH_KEYWORD = 1;
    public static final int MATCH_NONE = 0;
    public static final int MATCH_NUMBER = 5;
    public static final int MATCH_OPERATOR = 2;
    public static final int MATCH_STRING = 4;
    private static HashMap<String, TokenPattern[]> compiledPatterns;
    private static final HashMap<String, Highlighting> processedHighlighting = new HashMap<>();

    public static int getTextSizeDecrement(int i) {
        if (i > 120) {
            return 5;
        }
        return i > 50 ? 3 : 2;
    }

    /* loaded from: classes.dex */
    public static class Span extends CharacterStyle {
        public final String code;
        public final int currentType;
        public final float decrementSize;
        public final String lng;
        public final boolean smallerSize;
        public final TextStyleSpan.TextStyleRun style;

        public Span(boolean z, int i, TextStyleSpan.TextStyleRun textStyleRun, String str, String str2) {
            this.smallerSize = z;
            this.lng = str;
            this.code = str2;
            this.decrementSize = CodeHighlighting.getTextSizeDecrement(str2 == null ? 0 : str2.length());
            this.currentType = i;
            this.style = textStyleRun;
        }

        @Override // android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            if (this.smallerSize) {
                textPaint.setTextSize(AndroidUtilities.dp(SharedConfig.fontSize - this.decrementSize));
            }
            int i = this.currentType;
            if (i == 2) {
                textPaint.setColor(-1);
            } else if (i == 1) {
                textPaint.setColor(Theme.getColor(Theme.key_chat_messageTextOut));
            } else {
                textPaint.setColor(Theme.getColor(Theme.key_chat_messageTextIn));
            }
            TextStyleSpan.TextStyleRun textStyleRun = this.style;
            if (textStyleRun != null) {
                textStyleRun.applyStyle(textPaint);
                return;
            }
            textPaint.setTypeface(Typeface.MONOSPACE);
            textPaint.setUnderlineText(false);
        }
    }

    /* loaded from: classes.dex */
    public static class ColorSpan extends CharacterStyle {
        public int group;

        public ColorSpan(int i) {
            this.group = i;
        }

        public int getColorKey() {
            switch (this.group) {
                case 1:
                    return Theme.key_code_keyword;
                case 2:
                    return Theme.key_code_operator;
                case 3:
                    return Theme.key_code_constant;
                case 4:
                    return Theme.key_code_string;
                case 5:
                    return Theme.key_code_number;
                case 6:
                    return Theme.key_code_comment;
                case 7:
                    return Theme.key_code_function;
                default:
                    return -1;
            }
        }

        @Override // android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            textPaint.setColor(Theme.getColor(getColorKey()));
        }
    }

    /* loaded from: classes.dex */
    public static class LockedSpannableString extends SpannableString {
        private boolean ready;

        public LockedSpannableString(CharSequence charSequence) {
            super(charSequence);
        }

        public void unlock() {
            this.ready = true;
        }

        @Override // android.text.SpannableString, android.text.Spanned
        public <T> T[] getSpans(int i, int i2, Class<T> cls) {
            return !this.ready ? (T[]) ((Object[]) Array.newInstance((Class<?>) cls, 0)) : (T[]) super.getSpans(i, i2, cls);
        }

        @Override // android.text.SpannableString, android.text.Spanned
        public int nextSpanTransition(int i, int i2, Class cls) {
            return !this.ready ? i2 : super.nextSpanTransition(i, i2, cls);
        }

        @Override // android.text.SpannableString, android.text.Spanned
        public int getSpanStart(Object obj) {
            if (this.ready) {
                return super.getSpanStart(obj);
            }
            return -1;
        }

        @Override // android.text.SpannableString, android.text.Spanned
        public int getSpanEnd(Object obj) {
            if (this.ready) {
                return super.getSpanEnd(obj);
            }
            return -1;
        }

        @Override // android.text.SpannableString, android.text.Spanned
        public int getSpanFlags(Object obj) {
            if (this.ready) {
                return super.getSpanFlags(obj);
            }
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Highlighting {
        String language;
        SpannableString result;
        String text;

        private Highlighting() {
        }
    }

    public static SpannableString getHighlighted(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return new SpannableString(str);
        }
        String str3 = str2 + "`" + str;
        HashMap<String, Highlighting> hashMap = processedHighlighting;
        Highlighting highlighting = hashMap.get(str3);
        if (highlighting == null) {
            highlighting = new Highlighting();
            highlighting.text = str;
            highlighting.language = str2;
            LockedSpannableString lockedSpannableString = new LockedSpannableString(str);
            highlighting.result = lockedSpannableString;
            highlight(lockedSpannableString, 0, lockedSpannableString.length(), str2, 0, null, true);
            Iterator<String> it = hashMap.keySet().iterator();
            while (it.hasNext() && processedHighlighting.size() > 8) {
                it.next();
                it.remove();
            }
            processedHighlighting.put(str3, highlighting);
        }
        return highlighting.result;
    }

    public static void highlight(final Spannable spannable, final int i, final int i2, final String str, int i3, TextStyleSpan.TextStyleRun textStyleRun, boolean z) {
        if (spannable == null) {
            return;
        }
        Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.CodeHighlighting$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CodeHighlighting.lambda$highlight$2(spannable, i, i2, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$highlight$2(final Spannable spannable, int i, int i2, String str) {
        if (compiledPatterns == null) {
            parse();
        }
        long currentTimeMillis = System.currentTimeMillis();
        StringToken[][] stringTokenArr = new StringToken[1];
        try {
            String charSequence = spannable.subSequence(i, i2).toString();
            HashMap<String, TokenPattern[]> hashMap = compiledPatterns;
            stringTokenArr[0] = tokenize(charSequence, hashMap == null ? null : hashMap.get(str)).toArray();
        } catch (Exception e) {
            FileLog.e(e);
        }
        FileLog.d("[CodeHighlighter] tokenize took " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        long currentTimeMillis2 = System.currentTimeMillis();
        final ArrayList arrayList = new ArrayList();
        colorize(spannable, i, i2, stringTokenArr[0], -1, arrayList);
        FileLog.d("[CodeHighlighter] colorize took " + (System.currentTimeMillis() - currentTimeMillis2) + "ms");
        if (arrayList.isEmpty()) {
            return;
        }
        if (spannable instanceof LockedSpannableString) {
            long currentTimeMillis3 = System.currentTimeMillis();
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                CachedToSpan cachedToSpan = (CachedToSpan) arrayList.get(i3);
                spannable.setSpan(new ColorSpan(cachedToSpan.group), cachedToSpan.start, cachedToSpan.end, 33);
            }
            FileLog.d("[CodeHighlighter] applying " + arrayList.size() + " colorize spans took " + (System.currentTimeMillis() - currentTimeMillis3) + "ms in another thread");
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.CodeHighlighting$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CodeHighlighting.lambda$highlight$0(spannable);
                }
            });
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.CodeHighlighting$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                CodeHighlighting.lambda$highlight$1(arrayList, spannable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$highlight$0(Spannable spannable) {
        ((LockedSpannableString) spannable).unlock();
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.emojiLoaded, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$highlight$1(ArrayList arrayList, Spannable spannable) {
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < arrayList.size(); i++) {
            CachedToSpan cachedToSpan = (CachedToSpan) arrayList.get(i);
            spannable.setSpan(new ColorSpan(cachedToSpan.group), cachedToSpan.start, cachedToSpan.end, 33);
        }
        FileLog.d("[CodeHighlighter] applying " + arrayList.size() + " colorize spans took " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.emojiLoaded, new Object[0]);
    }

    private static void colorize(Spannable spannable, int i, int i2, StringToken[] stringTokenArr, int i3, ArrayList<CachedToSpan> arrayList) {
        int length;
        if (stringTokenArr == null) {
            return;
        }
        for (int i4 = 0; i4 < stringTokenArr.length && i < i2; i4++) {
            StringToken stringToken = stringTokenArr[i4];
            if (stringToken != null) {
                if (stringToken.string != null) {
                    int i5 = stringToken.group;
                    if (i3 != -1) {
                        i5 = i3;
                    }
                    if (i5 == -1) {
                        length = stringToken.length();
                        i += length;
                    } else {
                        arrayList.add(new CachedToSpan(i5, i, stringToken.length() + i));
                    }
                } else if (stringToken.inside != null) {
                    colorize(spannable, i, i + stringToken.length(), stringToken.inside.toArray(), stringToken.group, arrayList);
                }
                length = stringToken.length();
                i += length;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CachedToSpan {
        public int end;
        public int group;
        public int start;

        public CachedToSpan(int i, int i2, int i3) {
            this.group = i;
            this.start = i2;
            this.end = i3;
        }
    }

    private static LinkedList tokenize(String str, TokenPattern[] tokenPatternArr) {
        return tokenize(str, tokenPatternArr, null);
    }

    private static LinkedList tokenize(String str, TokenPattern[] tokenPatternArr, TokenPattern tokenPattern) {
        LinkedList linkedList = new LinkedList();
        linkedList.addAfter(linkedList.head, new StringToken(str));
        matchGrammar(str, linkedList, flatRest(tokenPatternArr), linkedList.head, 0, null, tokenPattern);
        return linkedList;
    }

    private static TokenPattern[] flatRest(TokenPattern[] tokenPatternArr) {
        HashMap<String, TokenPattern[]> hashMap;
        TokenPattern[] tokenPatternArr2;
        ArrayList arrayList = null;
        if (tokenPatternArr == null) {
            return null;
        }
        for (int i = 0; i < tokenPatternArr.length; i++) {
            if (tokenPatternArr[i].pattern != null && "REST".equals(tokenPatternArr[i].pattern.patternSource)) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    Collections.addAll(arrayList, tokenPatternArr);
                }
                arrayList.remove(tokenPatternArr[i]);
                if (!TextUtils.isEmpty(tokenPatternArr[i].insideLanguage) && (hashMap = compiledPatterns) != null && (tokenPatternArr2 = hashMap.get(tokenPatternArr[i].insideLanguage)) != null) {
                    Collections.addAll(arrayList, tokenPatternArr2);
                }
            }
        }
        return arrayList != null ? (TokenPattern[]) arrayList.toArray(new TokenPattern[0]) : tokenPatternArr;
    }

    /* JADX WARN: Removed duplicated region for block: B:65:0x00df  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x013a  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0145  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void matchGrammar(String str, LinkedList linkedList, TokenPattern[] tokenPatternArr, Node node, int i, RematchOptions rematchOptions, TokenPattern tokenPattern) {
        int i2;
        int i3;
        Match matchPattern;
        int i4;
        String substring;
        String substring2;
        int length;
        TokenPattern[] tokenPatternArr2;
        StringToken stringToken;
        int i5;
        String str2 = str;
        TokenPattern[] tokenPatternArr3 = tokenPatternArr;
        if (tokenPatternArr3 == null) {
            return;
        }
        int length2 = tokenPatternArr3.length;
        int i6 = 0;
        while (i6 < length2) {
            TokenPattern tokenPattern2 = tokenPatternArr3[i6];
            if (tokenPattern2 == tokenPattern) {
                return;
            }
            if (rematchOptions != null && rematchOptions.cause == tokenPattern2) {
                return;
            }
            Node node2 = node.next;
            int i7 = i;
            while (node2 != linkedList.tail) {
                if (rematchOptions != null && i7 >= rematchOptions.reach) {
                    return;
                }
                if (linkedList.length > str.length()) {
                    FileLog.e("[CodeHighlighter] Something went terribly wrong, ABORT, ABORT!");
                    return;
                }
                StringToken stringToken2 = node2.value;
                String str3 = stringToken2.string;
                if (str3 != null && !stringToken2.token) {
                    if (tokenPattern2.greedy) {
                        matchPattern = matchPattern(tokenPattern2, i7, str2);
                        if (matchPattern != null && matchPattern.index < str.length()) {
                            int i8 = matchPattern.index;
                            int i9 = matchPattern.length + i8;
                            int length3 = node2.value.length();
                            while (true) {
                                i7 += length3;
                                if (i8 < i7) {
                                    break;
                                }
                                node2 = node2.next;
                                length3 = node2.value.length();
                            }
                            i7 -= node2.value.length();
                            StringToken stringToken3 = node2.value;
                            if (stringToken3.string != null && !stringToken3.token) {
                                int i10 = i7;
                                int i11 = 1;
                                for (Node node3 = node2; node3 != linkedList.tail && (i10 < i9 || !node3.value.token); node3 = node3.next) {
                                    i11++;
                                    i10 += node3.value.length();
                                }
                                str3 = str2.substring(i7, i10);
                                matchPattern.index -= i7;
                                i4 = i11 - 1;
                                i3 = 0;
                                int i12 = matchPattern.index;
                                substring = str3.substring(i3, i12);
                                substring2 = str3.substring(i12 + matchPattern.length);
                                length = str3.length() + i7;
                                if (rematchOptions != null && length > rematchOptions.reach) {
                                    rematchOptions.reach = length;
                                }
                                Node node4 = node2.prev;
                                if (substring.length() > 0) {
                                    node4 = linkedList.addAfter(node4, new StringToken(substring));
                                    i7 += substring.length();
                                }
                                int i13 = i7;
                                linkedList.removeRange(node4, i4);
                                tokenPatternArr2 = tokenPattern2.insideTokenPatterns;
                                if (tokenPatternArr2 == null) {
                                    stringToken = new StringToken(tokenPattern2.group, tokenize(matchPattern.string, tokenPatternArr2), matchPattern.length);
                                    i2 = length2;
                                } else {
                                    String str4 = tokenPattern2.insideLanguage;
                                    if (str4 != null) {
                                        i2 = length2;
                                        stringToken = new StringToken(tokenPattern2.group, tokenize(matchPattern.string, compiledPatterns.get(str4), tokenPattern2), matchPattern.length);
                                    } else {
                                        i2 = length2;
                                        stringToken = new StringToken(tokenPattern2.group, matchPattern.string);
                                    }
                                }
                                Node addAfter = linkedList.addAfter(node4, stringToken);
                                if (substring2.length() > 0) {
                                    linkedList.addAfter(addAfter, new StringToken(substring2));
                                }
                                if (i4 > 1) {
                                    RematchOptions rematchOptions2 = new RematchOptions();
                                    rematchOptions2.cause = tokenPattern2;
                                    rematchOptions2.reach = length;
                                    matchGrammar(str, linkedList, tokenPatternArr, addAfter.prev, i13, rematchOptions2, tokenPattern);
                                    if (rematchOptions != null && (i5 = rematchOptions2.reach) > rematchOptions.reach) {
                                        rematchOptions.reach = i5;
                                    }
                                }
                                node2 = addAfter;
                                i7 = i13;
                            }
                        }
                        i6++;
                        str2 = str;
                        tokenPatternArr3 = tokenPatternArr;
                        length2 = length2;
                    } else {
                        i3 = 0;
                        matchPattern = matchPattern(tokenPattern2, 0, str3);
                        if (matchPattern != null) {
                            i4 = 1;
                            int i122 = matchPattern.index;
                            substring = str3.substring(i3, i122);
                            substring2 = str3.substring(i122 + matchPattern.length);
                            length = str3.length() + i7;
                            if (rematchOptions != null) {
                                rematchOptions.reach = length;
                            }
                            Node node42 = node2.prev;
                            if (substring.length() > 0) {
                            }
                            int i132 = i7;
                            linkedList.removeRange(node42, i4);
                            tokenPatternArr2 = tokenPattern2.insideTokenPatterns;
                            if (tokenPatternArr2 == null) {
                            }
                            Node addAfter2 = linkedList.addAfter(node42, stringToken);
                            if (substring2.length() > 0) {
                            }
                            if (i4 > 1) {
                            }
                            node2 = addAfter2;
                            i7 = i132;
                        }
                    }
                    i7 += node2.value.length();
                    node2 = node2.next;
                    str2 = str;
                    length2 = i2;
                }
                i2 = length2;
                i7 += node2.value.length();
                node2 = node2.next;
                str2 = str;
                length2 = i2;
            }
            i6++;
            str2 = str;
            tokenPatternArr3 = tokenPatternArr;
            length2 = length2;
        }
    }

    private static Match matchPattern(TokenPattern tokenPattern, int i, String str) {
        Matcher matcher = tokenPattern.pattern.getPattern().matcher(str);
        matcher.region(i, str.length());
        if (matcher.find()) {
            Match match = new Match();
            match.index = matcher.start();
            if (tokenPattern.lookbehind && matcher.groupCount() >= 1) {
                match.index += matcher.end(1) - matcher.start(1);
            }
            int end = matcher.end();
            int i2 = match.index;
            int i3 = end - i2;
            match.length = i3;
            match.string = str.substring(i2, i3 + i2);
            return match;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class RematchOptions {
        TokenPattern cause;
        int reach;

        private RematchOptions() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Match {
        int index;
        int length;
        String string;

        private Match() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LinkedList {
        public Node tail;
        public int length = 0;
        public Node head = new Node();

        public LinkedList() {
            Node node = new Node();
            this.tail = node;
            Node node2 = this.head;
            node2.next = node;
            node.prev = node2;
        }

        public Node addAfter(Node node, StringToken stringToken) {
            Node node2 = node.next;
            Node node3 = new Node();
            node3.value = stringToken;
            node3.prev = node;
            node3.next = node2;
            node.next = node3;
            node2.prev = node3;
            this.length++;
            return node3;
        }

        public void removeRange(Node node, int i) {
            Node node2 = node.next;
            int i2 = 0;
            while (i2 < i && node2 != this.tail) {
                node2 = node2.next;
                i2++;
            }
            node.next = node2;
            node2.prev = node;
            this.length -= i2;
        }

        public StringToken[] toArray() {
            StringToken[] stringTokenArr = new StringToken[this.length];
            Node node = this.head.next;
            for (int i = 0; i < this.length && node != this.tail; i++) {
                stringTokenArr[i] = node.value;
                node = node.next;
            }
            return stringTokenArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Node {
        public Node next;
        public Node prev;
        public StringToken value;

        private Node() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class StringToken {
        final int group;
        final LinkedList inside;
        final int insideLength;
        final String string;
        final boolean token;

        public StringToken(int i, String str) {
            this.token = true;
            this.group = i;
            this.string = str;
            this.inside = null;
            this.insideLength = 0;
        }

        public StringToken(int i, LinkedList linkedList, int i2) {
            this.token = true;
            this.group = i;
            this.string = null;
            this.inside = linkedList;
            this.insideLength = i2;
        }

        public StringToken(String str) {
            this.token = false;
            this.group = -1;
            this.string = str;
            this.inside = null;
            this.insideLength = 0;
        }

        public int length() {
            String str = this.string;
            if (str != null) {
                return str.length();
            }
            return this.insideLength;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:74:0x0139 A[Catch: Exception -> 0x0135, TryCatch #7 {Exception -> 0x0135, blocks: (B:70:0x0131, B:74:0x0139, B:76:0x013e), top: B:83:0x0131 }] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x013e A[Catch: Exception -> 0x0135, TRY_LEAVE, TryCatch #7 {Exception -> 0x0135, blocks: (B:70:0x0131, B:74:0x0139, B:76:0x013e), top: B:83:0x0131 }] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0131 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void parse() {
        Throwable th;
        InputStream inputStream;
        BufferedInputStream bufferedInputStream;
        long currentTimeMillis;
        GZIPInputStream gZIPInputStream;
        GZIPInputStream gZIPInputStream2 = null;
        try {
            try {
                currentTimeMillis = System.currentTimeMillis();
                inputStream = ApplicationLoader.applicationContext.getAssets().open("codelng.gzip");
                try {
                    gZIPInputStream = new GZIPInputStream(inputStream, 65536);
                    try {
                        bufferedInputStream = new BufferedInputStream(gZIPInputStream, 65536);
                    } catch (Exception e) {
                        e = e;
                        bufferedInputStream = null;
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedInputStream = null;
                    }
                } catch (Exception e2) {
                    e = e2;
                    bufferedInputStream = null;
                } catch (Throwable th3) {
                    th = th3;
                    bufferedInputStream = null;
                }
            } catch (Exception e3) {
                FileLog.e(e3);
                return;
            }
        } catch (Exception e4) {
            e = e4;
            inputStream = null;
            bufferedInputStream = null;
        } catch (Throwable th4) {
            th = th4;
            inputStream = null;
            bufferedInputStream = null;
        }
        try {
            StreamReader streamReader = new StreamReader(bufferedInputStream);
            HashMap hashMap = new HashMap();
            int readUint8 = streamReader.readUint8();
            for (int i = 0; i < readUint8; i++) {
                int readUint82 = streamReader.readUint8();
                int readUint83 = streamReader.readUint8();
                String[] strArr = new String[readUint83];
                for (int i2 = 0; i2 < readUint83; i2++) {
                    strArr[i2] = streamReader.readString();
                }
                hashMap.put(Integer.valueOf(readUint82), strArr);
            }
            int readUint16 = streamReader.readUint16();
            ParsedPattern[] parsedPatternArr = new ParsedPattern[readUint16];
            for (int i3 = 0; i3 < readUint16; i3++) {
                parsedPatternArr[i3] = new ParsedPattern();
                int readUint84 = streamReader.readUint8();
                parsedPatternArr[i3].multiline = (readUint84 & 1) != 0;
                parsedPatternArr[i3].caseInsensitive = (readUint84 & 2) != 0;
                parsedPatternArr[i3].pattern = streamReader.readString();
            }
            if (compiledPatterns == null) {
                compiledPatterns = new HashMap<>();
            }
            for (int i4 = 0; i4 < readUint8; i4++) {
                int readUint85 = streamReader.readUint8();
                TokenPattern[] readTokens = readTokens(streamReader, parsedPatternArr, hashMap);
                String[] strArr2 = (String[]) hashMap.get(Integer.valueOf(readUint85));
                int length = strArr2.length;
                int i5 = 0;
                while (i5 < length) {
                    compiledPatterns.put(strArr2[i5], readTokens);
                    i5++;
                    streamReader = streamReader;
                }
            }
            FileLog.d("[CodeHighlighter] Successfully read " + readUint8 + " languages, " + readUint16 + " patterns in " + (System.currentTimeMillis() - currentTimeMillis) + "ms from codelng.gzip");
            gZIPInputStream.close();
            bufferedInputStream.close();
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e5) {
            e = e5;
            gZIPInputStream2 = gZIPInputStream;
            try {
                FileLog.e(e);
                if (gZIPInputStream2 != null) {
                    gZIPInputStream2.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Throwable th5) {
                th = th5;
                if (gZIPInputStream2 != null) {
                    try {
                        gZIPInputStream2.close();
                    } catch (Exception e6) {
                        FileLog.e(e6);
                        throw th;
                    }
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th;
            }
        } catch (Throwable th6) {
            th = th6;
            gZIPInputStream2 = gZIPInputStream;
            if (gZIPInputStream2 != null) {
            }
            if (bufferedInputStream != null) {
            }
            if (inputStream != null) {
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ParsedPattern {
        private CachedPattern cachedPattern;
        boolean caseInsensitive;
        boolean multiline;
        String pattern;

        private ParsedPattern() {
        }

        public int flags() {
            return (this.multiline ? 8 : 0) | (this.caseInsensitive ? 2 : 0);
        }

        public CachedPattern getCachedPattern() {
            if (this.cachedPattern == null) {
                this.cachedPattern = new CachedPattern(this.pattern, flags());
            }
            return this.cachedPattern;
        }
    }

    private static TokenPattern[] readTokens(StreamReader streamReader, ParsedPattern[] parsedPatternArr, HashMap<Integer, String[]> hashMap) throws IOException {
        int readUint8 = streamReader.readUint8();
        TokenPattern[] tokenPatternArr = new TokenPattern[readUint8];
        for (int i = 0; i < readUint8; i++) {
            int readUint82 = streamReader.readUint8();
            int i2 = readUint82 & 3;
            int i3 = (readUint82 >> 2) & 7;
            boolean z = (readUint82 & 32) != 0;
            boolean z2 = (readUint82 & 64) != 0;
            int readUint16 = streamReader.readUint16();
            if (i2 == 0) {
                tokenPatternArr[i] = new TokenPattern(i3, parsedPatternArr[readUint16].getCachedPattern());
            } else if (i2 == 1) {
                if (i3 == 0) {
                    tokenPatternArr[i] = new TokenPattern(parsedPatternArr[readUint16].getCachedPattern(), readTokens(streamReader, parsedPatternArr, hashMap));
                } else {
                    tokenPatternArr[i] = new TokenPattern(i3, parsedPatternArr[readUint16].getCachedPattern(), readTokens(streamReader, parsedPatternArr, hashMap));
                }
            } else if (i2 == 2) {
                tokenPatternArr[i] = new TokenPattern(parsedPatternArr[readUint16].getCachedPattern(), hashMap.get(Integer.valueOf(streamReader.readUint8()))[0]);
            }
            if (z) {
                tokenPatternArr[i].greedy = true;
            }
            if (z2) {
                tokenPatternArr[i].lookbehind = true;
            }
        }
        return tokenPatternArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class StreamReader {
        private final InputStream is;

        public StreamReader(InputStream inputStream) {
            this.is = inputStream;
        }

        public int readUint8() throws IOException {
            return this.is.read() & 255;
        }

        public int readUint16() throws IOException {
            return (this.is.read() & 255) | ((this.is.read() & 255) << 8);
        }

        public String readString() throws IOException {
            int read = this.is.read();
            if (read >= 254) {
                read = this.is.read() | (this.is.read() << 8) | (this.is.read() << 16);
            }
            byte[] bArr = new byte[read];
            for (int i = 0; i < read; i++) {
                bArr[i] = (byte) this.is.read();
            }
            return new String(bArr, StandardCharsets.US_ASCII);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TokenPattern {
        public boolean greedy;
        public int group;
        public String insideLanguage;
        public TokenPattern[] insideTokenPatterns;
        public boolean lookbehind;
        public final CachedPattern pattern;

        public TokenPattern(int i, CachedPattern cachedPattern) {
            this.group = -1;
            this.pattern = cachedPattern;
            this.group = i;
        }

        public TokenPattern(CachedPattern cachedPattern, TokenPattern... tokenPatternArr) {
            this.group = -1;
            this.pattern = cachedPattern;
            this.insideTokenPatterns = tokenPatternArr;
        }

        public TokenPattern(CachedPattern cachedPattern, String str) {
            this.group = -1;
            this.pattern = cachedPattern;
            this.insideLanguage = str;
        }

        public TokenPattern(int i, CachedPattern cachedPattern, TokenPattern... tokenPatternArr) {
            this.group = -1;
            this.group = i;
            this.pattern = cachedPattern;
            this.insideTokenPatterns = tokenPatternArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CachedPattern {
        private Pattern pattern;
        private String patternSource;
        private int patternSourceFlags;

        public CachedPattern(String str, int i) {
            this.patternSource = str;
            this.patternSourceFlags = i;
        }

        public Pattern getPattern() {
            if (this.pattern == null) {
                this.pattern = Pattern.compile(this.patternSource, this.patternSourceFlags);
            }
            return this.pattern;
        }
    }
}
