package org.telegram.messenger;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.FilePathDatabase;
import org.telegram.messenger.utils.ImmutableByteArrayOutputStream;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$InputFileLocation;
import org.telegram.tgnet.TLRPC$InputWebFileLocation;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_documentEncrypted;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_fileHash;
import org.telegram.tgnet.TLRPC$TL_fileLocationToBeDeprecated;
import org.telegram.tgnet.TLRPC$TL_inputDocumentFileLocation;
import org.telegram.tgnet.TLRPC$TL_inputPeerPhotoFileLocation;
import org.telegram.tgnet.TLRPC$TL_inputPhotoFileLocation;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetThumb;
import org.telegram.tgnet.TLRPC$TL_secureFile;
import org.telegram.tgnet.TLRPC$TL_theme;
import org.telegram.tgnet.TLRPC$TL_upload_cdnFile;
import org.telegram.tgnet.TLRPC$TL_upload_cdnFileReuploadNeeded;
import org.telegram.tgnet.TLRPC$TL_upload_file;
import org.telegram.tgnet.TLRPC$TL_upload_fileCdnRedirect;
import org.telegram.tgnet.TLRPC$TL_upload_getCdnFile;
import org.telegram.tgnet.TLRPC$TL_upload_getCdnFileHashes;
import org.telegram.tgnet.TLRPC$TL_upload_getFile;
import org.telegram.tgnet.TLRPC$TL_upload_getWebFile;
import org.telegram.tgnet.TLRPC$TL_upload_reuploadCdnFile;
import org.telegram.tgnet.TLRPC$TL_upload_webFile;
import org.telegram.tgnet.TLRPC$Vector;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.tgnet.tl.TL_stories$TL_storyItem;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.Storage.CacheModel;
/* loaded from: classes.dex */
public class FileLoadOperation {
    private static final int FINISH_CODE_DEFAULT = 0;
    private static final int FINISH_CODE_FILE_ALREADY_EXIST = 1;
    public static ImmutableByteArrayOutputStream filesQueueByteBuffer = null;
    private static int globalRequestPointer = 0;
    private static final int preloadMaxBytes = 2097152;
    private static final int stateCanceled = 4;
    private static final int stateDownloading = 1;
    private static final int stateFailed = 2;
    private static final int stateFinished = 3;
    private static final int stateIdle = 0;
    private final boolean FULL_LOGS;
    private boolean allowDisordererFileSave;
    private int bigFileSizeFrom;
    private long bytesCountPadding;
    private File cacheFileFinal;
    private File cacheFileGzipTemp;
    private File cacheFileParts;
    private File cacheFilePreload;
    private File cacheFileTemp;
    private File cacheIvTemp;
    private byte[] cdnCheckBytes;
    private int cdnChunkCheckSize;
    private int cdnDatacenterId;
    private HashMap<Long, TLRPC$TL_fileHash> cdnHashes;
    private byte[] cdnIv;
    private byte[] cdnKey;
    private byte[] cdnToken;
    public int currentAccount;
    private int currentDownloadChunkSize;
    private int currentMaxDownloadRequests;
    private int currentType;
    private int datacenterId;
    private ArrayList<RequestInfo> delayedRequestInfos;
    private FileLoadOperationDelegate delegate;
    private int downloadChunkSize;
    private int downloadChunkSizeAnimation;
    private int downloadChunkSizeBig;
    private long downloadedBytes;
    private boolean encryptFile;
    private byte[] encryptIv;
    private byte[] encryptKey;
    private String ext;
    private FilePathDatabase.FileMeta fileMetadata;
    private String fileName;
    private RandomAccessFile fileOutputStream;
    private RandomAccessFile filePartsStream;
    private RandomAccessFile fileReadStream;
    private Runnable fileWriteRunnable;
    private RandomAccessFile fiv;
    private boolean forceSmallChunk;
    private long foundMoovSize;
    private int initialDatacenterId;
    private boolean isCdn;
    private boolean isForceRequest;
    private boolean isPreloadVideoOperation;
    public boolean isStory;
    private boolean isStream;
    private byte[] iv;
    private byte[] key;
    protected long lastProgressUpdateTime;
    protected TLRPC$InputFileLocation location;
    private int maxCdnParts;
    private int maxDownloadRequests;
    private int maxDownloadRequestsAnimation;
    private int maxDownloadRequestsBig;
    private int moovFound;
    private long nextAtomOffset;
    private boolean nextPartWasPreloaded;
    private long nextPreloadDownloadOffset;
    private ArrayList<Range> notCheckedCdnRanges;
    private ArrayList<Range> notLoadedBytesRanges;
    private volatile ArrayList<Range> notLoadedBytesRangesCopy;
    private ArrayList<Range> notRequestedBytesRanges;
    public Object parentObject;
    public FilePathDatabase.PathData pathSaveData;
    private volatile boolean paused;
    public boolean preFinished;
    private boolean preloadFinished;
    private long preloadNotRequestedBytesCount;
    private int preloadPrefixSize;
    private RandomAccessFile preloadStream;
    private int preloadStreamFileOffset;
    private byte[] preloadTempBuffer;
    private int preloadTempBufferCount;
    private HashMap<Long, PreloadRange> preloadedBytesRanges;
    private int priority;
    private FileLoaderPriorityQueue priorityQueue;
    private RequestInfo priorityRequestInfo;
    private int renameRetryCount;
    private ArrayList<RequestInfo> requestInfos;
    private long requestedBytesCount;
    private HashMap<Long, Integer> requestedPreloadedBytesRanges;
    private boolean requestingCdnOffsets;
    protected boolean requestingReference;
    private int requestsCount;
    private boolean reuploadingCdn;
    private long startTime;
    private boolean started;
    private volatile int state;
    private String storeFileName;
    private File storePath;
    FileLoadOperationStream stream;
    private ArrayList<FileLoadOperationStream> streamListeners;
    long streamOffset;
    boolean streamPriority;
    private long streamPriorityStartOffset;
    private long streamStartOffset;
    private boolean supportsPreloading;
    private File tempPath;
    public long totalBytesCount;
    private int totalPreloadedBytes;
    long totalTime;
    private boolean ungzip;
    private WebFile webFile;
    private TLRPC$InputWebFileLocation webLocation;
    public static volatile DispatchQueue filesQueue = new DispatchQueue("writeFileQueue");
    private static final Object lockObject = new Object();

    /* loaded from: classes.dex */
    public interface FileLoadOperationDelegate {
        void didChangedLoadProgress(FileLoadOperation fileLoadOperation, long j, long j2);

        void didFailedLoadingFile(FileLoadOperation fileLoadOperation, int i);

        void didFinishLoadingFile(FileLoadOperation fileLoadOperation, File file);

        void didPreFinishLoading(FileLoadOperation fileLoadOperation, File file);

        boolean hasAnotherRefOnFile(String str);

        boolean isLocallyCreatedFile(String str);

        void saveFilePath(FilePathDatabase.PathData pathData, File file);
    }

    public void setStream(final FileLoadOperationStream fileLoadOperationStream, boolean z, long j) {
        this.stream = fileLoadOperationStream;
        this.streamOffset = j;
        this.streamPriority = z;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$setStream$0(fileLoadOperationStream);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setStream$0(FileLoadOperationStream fileLoadOperationStream) {
        if (this.streamListeners == null) {
            this.streamListeners = new ArrayList<>();
        }
        if (fileLoadOperationStream != null && !this.streamListeners.contains(fileLoadOperationStream)) {
            this.streamListeners.add(fileLoadOperationStream);
        }
        if (fileLoadOperationStream == null || this.state == 1 || this.state == 0) {
            return;
        }
        fileLoadOperationStream.newDataAvailable();
    }

    public int getPositionInQueue() {
        return getQueue().getPosition(this);
    }

    public boolean checkPrefixPreloadFinished() {
        int i = this.preloadPrefixSize;
        if (i > 0 && this.downloadedBytes > i) {
            long j = Long.MAX_VALUE;
            ArrayList<Range> arrayList = this.notLoadedBytesRanges;
            if (arrayList == null) {
                return true;
            }
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                try {
                    j = Math.min(j, arrayList.get(i2).start);
                } catch (Throwable th) {
                    FileLog.e(th);
                    return true;
                }
            }
            if (j > this.preloadPrefixSize) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class RequestInfo {
        public int chunkSize;
        public int connectionType;
        private boolean forceSmallChunk;
        private long offset;
        public long requestStartTime;
        private int requestToken;
        private TLRPC$TL_upload_file response;
        private TLRPC$TL_upload_cdnFile responseCdn;
        private TLRPC$TL_upload_webFile responseWeb;

        protected RequestInfo() {
        }
    }

    /* loaded from: classes.dex */
    public static class Range {
        private long end;
        private long start;

        private Range(long j, long j2) {
            this.start = j;
            this.end = j2;
        }

        public String toString() {
            return "Range{start=" + this.start + ", end=" + this.end + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class PreloadRange {
        private long fileOffset;
        private long length;

        private PreloadRange(long j, long j2) {
            this.fileOffset = j;
            this.length = j2;
        }
    }

    private void updateParams() {
        if ((this.preloadPrefixSize > 0 || MessagesController.getInstance(this.currentAccount).getfileExperimentalParams) && !this.forceSmallChunk) {
            this.downloadChunkSizeBig = 524288;
            this.maxDownloadRequests = 8;
            this.maxDownloadRequestsBig = 8;
        } else {
            this.downloadChunkSizeBig = 131072;
            this.maxDownloadRequests = 4;
            this.maxDownloadRequestsBig = 4;
        }
        this.maxCdnParts = (int) (FileLoader.DEFAULT_MAX_FILE_SIZE / this.downloadChunkSizeBig);
    }

    public FileLoadOperation(ImageLocation imageLocation, Object obj, String str, long j) {
        boolean z = false;
        this.FULL_LOGS = false;
        this.downloadChunkSize = LiteMode.FLAG_CHAT_SCALE;
        this.downloadChunkSizeBig = 131072;
        this.cdnChunkCheckSize = 131072;
        this.maxDownloadRequests = 4;
        this.maxDownloadRequestsBig = 4;
        this.bigFileSizeFrom = 10485760;
        this.maxCdnParts = (int) (FileLoader.DEFAULT_MAX_FILE_SIZE / 131072);
        this.downloadChunkSizeAnimation = 131072;
        this.maxDownloadRequestsAnimation = 4;
        this.preloadTempBuffer = new byte[24];
        this.state = 0;
        updateParams();
        this.parentObject = obj;
        this.fileMetadata = FileLoader.getFileMetadataFromParent(this.currentAccount, obj);
        this.isStream = imageLocation.imageType == 2;
        if (imageLocation.isEncrypted()) {
            TLRPC$InputFileLocation tLRPC$InputFileLocation = new TLRPC$InputFileLocation() { // from class: org.telegram.tgnet.TLRPC$TL_inputEncryptedFileLocation
                @Override // org.telegram.tgnet.TLObject
                public void readParams(AbstractSerializedData abstractSerializedData, boolean z2) {
                    this.id = abstractSerializedData.readInt64(z2);
                    this.access_hash = abstractSerializedData.readInt64(z2);
                }

                @Override // org.telegram.tgnet.TLObject
                public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                    abstractSerializedData.writeInt32(-182231723);
                    abstractSerializedData.writeInt64(this.id);
                    abstractSerializedData.writeInt64(this.access_hash);
                }
            };
            this.location = tLRPC$InputFileLocation;
            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated = imageLocation.location;
            long j2 = tLRPC$TL_fileLocationToBeDeprecated.volume_id;
            tLRPC$InputFileLocation.id = j2;
            tLRPC$InputFileLocation.volume_id = j2;
            tLRPC$InputFileLocation.local_id = tLRPC$TL_fileLocationToBeDeprecated.local_id;
            tLRPC$InputFileLocation.access_hash = imageLocation.access_hash;
            byte[] bArr = new byte[32];
            this.iv = bArr;
            System.arraycopy(imageLocation.iv, 0, bArr, 0, bArr.length);
            this.key = imageLocation.key;
        } else if (imageLocation.photoPeer != null) {
            TLRPC$TL_inputPeerPhotoFileLocation tLRPC$TL_inputPeerPhotoFileLocation = new TLRPC$TL_inputPeerPhotoFileLocation();
            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated2 = imageLocation.location;
            long j3 = tLRPC$TL_fileLocationToBeDeprecated2.volume_id;
            tLRPC$TL_inputPeerPhotoFileLocation.id = j3;
            tLRPC$TL_inputPeerPhotoFileLocation.volume_id = j3;
            tLRPC$TL_inputPeerPhotoFileLocation.local_id = tLRPC$TL_fileLocationToBeDeprecated2.local_id;
            tLRPC$TL_inputPeerPhotoFileLocation.photo_id = imageLocation.photoId;
            tLRPC$TL_inputPeerPhotoFileLocation.big = imageLocation.photoPeerType == 0;
            tLRPC$TL_inputPeerPhotoFileLocation.peer = imageLocation.photoPeer;
            this.location = tLRPC$TL_inputPeerPhotoFileLocation;
        } else if (imageLocation.stickerSet != null) {
            TLRPC$TL_inputStickerSetThumb tLRPC$TL_inputStickerSetThumb = new TLRPC$TL_inputStickerSetThumb();
            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated3 = imageLocation.location;
            long j4 = tLRPC$TL_fileLocationToBeDeprecated3.volume_id;
            tLRPC$TL_inputStickerSetThumb.id = j4;
            tLRPC$TL_inputStickerSetThumb.volume_id = j4;
            tLRPC$TL_inputStickerSetThumb.local_id = tLRPC$TL_fileLocationToBeDeprecated3.local_id;
            tLRPC$TL_inputStickerSetThumb.thumb_version = imageLocation.thumbVersion;
            tLRPC$TL_inputStickerSetThumb.stickerset = imageLocation.stickerSet;
            this.location = tLRPC$TL_inputStickerSetThumb;
        } else if (imageLocation.thumbSize != null) {
            if (imageLocation.photoId != 0) {
                TLRPC$TL_inputPhotoFileLocation tLRPC$TL_inputPhotoFileLocation = new TLRPC$TL_inputPhotoFileLocation();
                this.location = tLRPC$TL_inputPhotoFileLocation;
                tLRPC$TL_inputPhotoFileLocation.id = imageLocation.photoId;
                TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated4 = imageLocation.location;
                tLRPC$TL_inputPhotoFileLocation.volume_id = tLRPC$TL_fileLocationToBeDeprecated4.volume_id;
                tLRPC$TL_inputPhotoFileLocation.local_id = tLRPC$TL_fileLocationToBeDeprecated4.local_id;
                tLRPC$TL_inputPhotoFileLocation.access_hash = imageLocation.access_hash;
                tLRPC$TL_inputPhotoFileLocation.file_reference = imageLocation.file_reference;
                tLRPC$TL_inputPhotoFileLocation.thumb_size = imageLocation.thumbSize;
                if (imageLocation.imageType == 2) {
                    this.allowDisordererFileSave = true;
                }
            } else {
                TLRPC$TL_inputDocumentFileLocation tLRPC$TL_inputDocumentFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                this.location = tLRPC$TL_inputDocumentFileLocation;
                tLRPC$TL_inputDocumentFileLocation.id = imageLocation.documentId;
                TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated5 = imageLocation.location;
                tLRPC$TL_inputDocumentFileLocation.volume_id = tLRPC$TL_fileLocationToBeDeprecated5.volume_id;
                tLRPC$TL_inputDocumentFileLocation.local_id = tLRPC$TL_fileLocationToBeDeprecated5.local_id;
                tLRPC$TL_inputDocumentFileLocation.access_hash = imageLocation.access_hash;
                tLRPC$TL_inputDocumentFileLocation.file_reference = imageLocation.file_reference;
                tLRPC$TL_inputDocumentFileLocation.thumb_size = imageLocation.thumbSize;
            }
            TLRPC$InputFileLocation tLRPC$InputFileLocation2 = this.location;
            if (tLRPC$InputFileLocation2.file_reference == null) {
                tLRPC$InputFileLocation2.file_reference = new byte[0];
            }
        } else {
            TLRPC$InputFileLocation tLRPC$InputFileLocation3 = new TLRPC$InputFileLocation() { // from class: org.telegram.tgnet.TLRPC$TL_inputFileLocation
                @Override // org.telegram.tgnet.TLObject
                public void readParams(AbstractSerializedData abstractSerializedData, boolean z2) {
                    this.volume_id = abstractSerializedData.readInt64(z2);
                    this.local_id = abstractSerializedData.readInt32(z2);
                    this.secret = abstractSerializedData.readInt64(z2);
                    this.file_reference = abstractSerializedData.readByteArray(z2);
                }

                @Override // org.telegram.tgnet.TLObject
                public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                    abstractSerializedData.writeInt32(-539317279);
                    abstractSerializedData.writeInt64(this.volume_id);
                    abstractSerializedData.writeInt32(this.local_id);
                    abstractSerializedData.writeInt64(this.secret);
                    abstractSerializedData.writeByteArray(this.file_reference);
                }
            };
            this.location = tLRPC$InputFileLocation3;
            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated6 = imageLocation.location;
            tLRPC$InputFileLocation3.volume_id = tLRPC$TL_fileLocationToBeDeprecated6.volume_id;
            tLRPC$InputFileLocation3.local_id = tLRPC$TL_fileLocationToBeDeprecated6.local_id;
            tLRPC$InputFileLocation3.secret = imageLocation.access_hash;
            byte[] bArr2 = imageLocation.file_reference;
            tLRPC$InputFileLocation3.file_reference = bArr2;
            if (bArr2 == null) {
                tLRPC$InputFileLocation3.file_reference = new byte[0];
            }
            this.allowDisordererFileSave = true;
        }
        int i = imageLocation.imageType;
        this.ungzip = (i == 1 || i == 3) ? true : true;
        int i2 = imageLocation.dc_id;
        this.datacenterId = i2;
        this.initialDatacenterId = i2;
        this.currentType = ConnectionsManager.FileTypePhoto;
        this.totalBytesCount = j;
        this.ext = str == null ? "jpg" : str;
    }

    public FileLoadOperation(SecureDocument secureDocument) {
        this.FULL_LOGS = false;
        this.downloadChunkSize = LiteMode.FLAG_CHAT_SCALE;
        this.downloadChunkSizeBig = 131072;
        this.cdnChunkCheckSize = 131072;
        this.maxDownloadRequests = 4;
        this.maxDownloadRequestsBig = 4;
        this.bigFileSizeFrom = 10485760;
        this.maxCdnParts = (int) (FileLoader.DEFAULT_MAX_FILE_SIZE / 131072);
        this.downloadChunkSizeAnimation = 131072;
        this.maxDownloadRequestsAnimation = 4;
        this.preloadTempBuffer = new byte[24];
        this.state = 0;
        updateParams();
        TLRPC$InputFileLocation tLRPC$InputFileLocation = new TLRPC$InputFileLocation() { // from class: org.telegram.tgnet.TLRPC$TL_inputSecureFileLocation
            @Override // org.telegram.tgnet.TLObject
            public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
                this.id = abstractSerializedData.readInt64(z);
                this.access_hash = abstractSerializedData.readInt64(z);
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                abstractSerializedData.writeInt32(-876089816);
                abstractSerializedData.writeInt64(this.id);
                abstractSerializedData.writeInt64(this.access_hash);
            }
        };
        this.location = tLRPC$InputFileLocation;
        TLRPC$TL_secureFile tLRPC$TL_secureFile = secureDocument.secureFile;
        tLRPC$InputFileLocation.id = tLRPC$TL_secureFile.id;
        tLRPC$InputFileLocation.access_hash = tLRPC$TL_secureFile.access_hash;
        this.datacenterId = tLRPC$TL_secureFile.dc_id;
        this.totalBytesCount = tLRPC$TL_secureFile.size;
        this.allowDisordererFileSave = true;
        this.currentType = ConnectionsManager.FileTypeFile;
        this.ext = ".jpg";
    }

    public FileLoadOperation(int i, WebFile webFile) {
        this.FULL_LOGS = false;
        this.downloadChunkSize = LiteMode.FLAG_CHAT_SCALE;
        this.downloadChunkSizeBig = 131072;
        this.cdnChunkCheckSize = 131072;
        this.maxDownloadRequests = 4;
        this.maxDownloadRequestsBig = 4;
        this.bigFileSizeFrom = 10485760;
        this.maxCdnParts = (int) (FileLoader.DEFAULT_MAX_FILE_SIZE / 131072);
        this.downloadChunkSizeAnimation = 131072;
        this.maxDownloadRequestsAnimation = 4;
        this.preloadTempBuffer = new byte[24];
        this.state = 0;
        updateParams();
        this.currentAccount = i;
        this.webFile = webFile;
        this.webLocation = webFile.location;
        this.totalBytesCount = webFile.size;
        int i2 = MessagesController.getInstance(i).webFileDatacenterId;
        this.datacenterId = i2;
        this.initialDatacenterId = i2;
        String mimeTypePart = FileLoader.getMimeTypePart(webFile.mime_type);
        if (webFile.mime_type.startsWith("image/")) {
            this.currentType = ConnectionsManager.FileTypePhoto;
        } else if (webFile.mime_type.equals("audio/ogg")) {
            this.currentType = ConnectionsManager.FileTypeAudio;
        } else if (webFile.mime_type.startsWith("video/")) {
            this.currentType = ConnectionsManager.FileTypeVideo;
        } else {
            this.currentType = ConnectionsManager.FileTypeFile;
        }
        this.allowDisordererFileSave = true;
        this.ext = ImageLoader.getHttpUrlExtension(webFile.url, mimeTypePart);
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x010f A[Catch: Exception -> 0x0136, TryCatch #0 {Exception -> 0x0136, blocks: (B:3:0x0032, B:6:0x0042, B:18:0x00b4, B:20:0x00be, B:25:0x00cc, B:27:0x00d6, B:29:0x00e0, B:30:0x00e8, B:32:0x00f0, B:35:0x00fa, B:37:0x0105, B:39:0x010f, B:44:0x0125, B:46:0x012d, B:40:0x0114, B:42:0x011c, B:43:0x0121, B:36:0x0103, B:7:0x0068, B:9:0x006c, B:11:0x0083, B:12:0x0087, B:14:0x0098, B:16:0x00a2, B:17:0x00b1), top: B:51:0x0032 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0114 A[Catch: Exception -> 0x0136, TryCatch #0 {Exception -> 0x0136, blocks: (B:3:0x0032, B:6:0x0042, B:18:0x00b4, B:20:0x00be, B:25:0x00cc, B:27:0x00d6, B:29:0x00e0, B:30:0x00e8, B:32:0x00f0, B:35:0x00fa, B:37:0x0105, B:39:0x010f, B:44:0x0125, B:46:0x012d, B:40:0x0114, B:42:0x011c, B:43:0x0121, B:36:0x0103, B:7:0x0068, B:9:0x006c, B:11:0x0083, B:12:0x0087, B:14:0x0098, B:16:0x00a2, B:17:0x00b1), top: B:51:0x0032 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x012d A[Catch: Exception -> 0x0136, TRY_LEAVE, TryCatch #0 {Exception -> 0x0136, blocks: (B:3:0x0032, B:6:0x0042, B:18:0x00b4, B:20:0x00be, B:25:0x00cc, B:27:0x00d6, B:29:0x00e0, B:30:0x00e8, B:32:0x00f0, B:35:0x00fa, B:37:0x0105, B:39:0x010f, B:44:0x0125, B:46:0x012d, B:40:0x0114, B:42:0x011c, B:43:0x0121, B:36:0x0103, B:7:0x0068, B:9:0x006c, B:11:0x0083, B:12:0x0087, B:14:0x0098, B:16:0x00a2, B:17:0x00b1), top: B:51:0x0032 }] */
    /* JADX WARN: Removed duplicated region for block: B:55:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public FileLoadOperation(TLRPC$Document tLRPC$Document, Object obj) {
        boolean z;
        long j;
        String documentFileName;
        int lastIndexOf;
        this.FULL_LOGS = false;
        this.downloadChunkSize = LiteMode.FLAG_CHAT_SCALE;
        this.downloadChunkSizeBig = 131072;
        this.cdnChunkCheckSize = 131072;
        this.maxDownloadRequests = 4;
        this.maxDownloadRequestsBig = 4;
        this.bigFileSizeFrom = 10485760;
        this.maxCdnParts = (int) (FileLoader.DEFAULT_MAX_FILE_SIZE / 131072);
        this.downloadChunkSizeAnimation = 131072;
        this.maxDownloadRequestsAnimation = 4;
        this.preloadTempBuffer = new byte[24];
        this.state = 0;
        updateParams();
        try {
            this.parentObject = obj;
            this.fileMetadata = FileLoader.getFileMetadataFromParent(this.currentAccount, obj);
            if (tLRPC$Document instanceof TLRPC$TL_documentEncrypted) {
                TLRPC$InputFileLocation tLRPC$InputFileLocation = new TLRPC$InputFileLocation() { // from class: org.telegram.tgnet.TLRPC$TL_inputEncryptedFileLocation
                    @Override // org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData, boolean z2) {
                        this.id = abstractSerializedData.readInt64(z2);
                        this.access_hash = abstractSerializedData.readInt64(z2);
                    }

                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                        abstractSerializedData.writeInt32(-182231723);
                        abstractSerializedData.writeInt64(this.id);
                        abstractSerializedData.writeInt64(this.access_hash);
                    }
                };
                this.location = tLRPC$InputFileLocation;
                tLRPC$InputFileLocation.id = tLRPC$Document.id;
                tLRPC$InputFileLocation.access_hash = tLRPC$Document.access_hash;
                int i = tLRPC$Document.dc_id;
                this.datacenterId = i;
                this.initialDatacenterId = i;
                byte[] bArr = new byte[32];
                this.iv = bArr;
                System.arraycopy(tLRPC$Document.iv, 0, bArr, 0, bArr.length);
                this.key = tLRPC$Document.key;
            } else if (tLRPC$Document instanceof TLRPC$TL_document) {
                TLRPC$TL_inputDocumentFileLocation tLRPC$TL_inputDocumentFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                this.location = tLRPC$TL_inputDocumentFileLocation;
                tLRPC$TL_inputDocumentFileLocation.id = tLRPC$Document.id;
                tLRPC$TL_inputDocumentFileLocation.access_hash = tLRPC$Document.access_hash;
                byte[] bArr2 = tLRPC$Document.file_reference;
                tLRPC$TL_inputDocumentFileLocation.file_reference = bArr2;
                tLRPC$TL_inputDocumentFileLocation.thumb_size = "";
                if (bArr2 == null) {
                    tLRPC$TL_inputDocumentFileLocation.file_reference = new byte[0];
                }
                int i2 = tLRPC$Document.dc_id;
                this.datacenterId = i2;
                this.initialDatacenterId = i2;
                this.allowDisordererFileSave = true;
                int size = tLRPC$Document.attributes.size();
                int i3 = 0;
                while (true) {
                    if (i3 >= size) {
                        break;
                    } else if (tLRPC$Document.attributes.get(i3) instanceof TLRPC$TL_documentAttributeVideo) {
                        this.supportsPreloading = true;
                        this.preloadPrefixSize = tLRPC$Document.attributes.get(i3).preload_prefix_size;
                        break;
                    } else {
                        i3++;
                    }
                }
            }
            if (!"application/x-tgsticker".equals(tLRPC$Document.mime_type) && !"application/x-tgwallpattern".equals(tLRPC$Document.mime_type)) {
                z = false;
                this.ungzip = z;
                j = tLRPC$Document.size;
                this.totalBytesCount = j;
                if (this.key != null && j % 16 != 0) {
                    long j2 = 16 - (j % 16);
                    this.bytesCountPadding = j2;
                    this.totalBytesCount = j + j2;
                }
                documentFileName = FileLoader.getDocumentFileName(tLRPC$Document);
                this.ext = documentFileName;
                if (documentFileName != null && (lastIndexOf = documentFileName.lastIndexOf(46)) != -1) {
                    this.ext = this.ext.substring(lastIndexOf);
                    if (!"audio/ogg".equals(tLRPC$Document.mime_type)) {
                        this.currentType = ConnectionsManager.FileTypeAudio;
                    } else if (FileLoader.isVideoMimeType(tLRPC$Document.mime_type)) {
                        this.currentType = ConnectionsManager.FileTypeVideo;
                    } else {
                        this.currentType = ConnectionsManager.FileTypeFile;
                    }
                    if (this.ext.length() > 1) {
                        this.ext = FileLoader.getExtensionByMimeType(tLRPC$Document.mime_type);
                        return;
                    }
                    return;
                }
                this.ext = "";
                if (!"audio/ogg".equals(tLRPC$Document.mime_type)) {
                }
                if (this.ext.length() > 1) {
                }
            }
            z = true;
            this.ungzip = z;
            j = tLRPC$Document.size;
            this.totalBytesCount = j;
            if (this.key != null) {
                long j22 = 16 - (j % 16);
                this.bytesCountPadding = j22;
                this.totalBytesCount = j + j22;
            }
            documentFileName = FileLoader.getDocumentFileName(tLRPC$Document);
            this.ext = documentFileName;
            if (documentFileName != null) {
                this.ext = this.ext.substring(lastIndexOf);
                if (!"audio/ogg".equals(tLRPC$Document.mime_type)) {
                }
                if (this.ext.length() > 1) {
                }
            }
            this.ext = "";
            if (!"audio/ogg".equals(tLRPC$Document.mime_type)) {
            }
            if (this.ext.length() > 1) {
            }
        } catch (Exception e) {
            FileLog.e(e);
            onFail(true, 0);
        }
    }

    public void setEncryptFile(boolean z) {
        this.encryptFile = z;
        if (z) {
            this.allowDisordererFileSave = false;
        }
    }

    public int getDatacenterId() {
        return this.initialDatacenterId;
    }

    public void setForceRequest(boolean z) {
        this.isForceRequest = z;
    }

    public boolean isForceRequest() {
        return this.isForceRequest;
    }

    public void setPriority(int i) {
        this.priority = i;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPaths(int i, String str, FileLoaderPriorityQueue fileLoaderPriorityQueue, File file, File file2, String str2) {
        this.storePath = file;
        this.tempPath = file2;
        this.currentAccount = i;
        this.fileName = str;
        this.storeFileName = str2;
        this.priorityQueue = fileLoaderPriorityQueue;
    }

    public FileLoaderPriorityQueue getQueue() {
        return this.priorityQueue;
    }

    public boolean wasStarted() {
        return this.started && !this.paused;
    }

    public int getCurrentType() {
        return this.currentType;
    }

    private void removePart(ArrayList<Range> arrayList, long j, long j2) {
        boolean z;
        if (arrayList == null || j2 < j) {
            return;
        }
        int size = arrayList.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            Range range = arrayList.get(i2);
            if (j == range.end) {
                range.end = j2;
            } else if (j2 == range.start) {
                range.start = j;
            }
            z = true;
        }
        z = false;
        Collections.sort(arrayList, FileLoadOperation$$ExternalSyntheticLambda17.INSTANCE);
        while (i < arrayList.size() - 1) {
            Range range2 = arrayList.get(i);
            int i3 = i + 1;
            Range range3 = arrayList.get(i3);
            if (range2.end == range3.start) {
                range2.end = range3.end;
                arrayList.remove(i3);
                i--;
            }
            i++;
        }
        if (z) {
            return;
        }
        arrayList.add(new Range(j, j2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$removePart$1(Range range, Range range2) {
        if (range.start > range2.start) {
            return 1;
        }
        return range.start < range2.start ? -1 : 0;
    }

    private void addPart(ArrayList<Range> arrayList, long j, long j2, boolean z) {
        if (arrayList == null || j2 < j) {
            return;
        }
        int size = arrayList.size();
        boolean z2 = false;
        for (int i = 0; i < size; i++) {
            Range range = arrayList.get(i);
            if (j <= range.start) {
                if (j2 < range.end) {
                    if (j2 > range.start) {
                        range.start = j2;
                    }
                } else {
                    arrayList.remove(i);
                }
                z2 = true;
                break;
            }
            if (j2 >= range.end) {
                if (j < range.end) {
                    range.end = j;
                }
            } else {
                arrayList.add(0, new Range(range.start, j));
                range.start = j2;
            }
            z2 = true;
            break;
        }
        if (z) {
            if (z2) {
                final ArrayList arrayList2 = new ArrayList(arrayList);
                if (this.fileWriteRunnable != null) {
                    filesQueue.cancelRunnable(this.fileWriteRunnable);
                }
                DispatchQueue dispatchQueue = filesQueue;
                Runnable runnable = new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileLoadOperation.this.lambda$addPart$2(arrayList2);
                    }
                };
                this.fileWriteRunnable = runnable;
                dispatchQueue.postRunnable(runnable);
                notifyStreamListeners();
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.e(this.cacheFileFinal + " downloaded duplicate file part " + j + " - " + j2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addPart$2(ArrayList arrayList) {
        long currentTimeMillis = System.currentTimeMillis();
        try {
        } catch (Exception e) {
            FileLog.e((Throwable) e, false);
            if (AndroidUtilities.isENOSPC(e)) {
                LaunchActivity.checkFreeDiscSpaceStatic(1);
            } else if (AndroidUtilities.isEROFS(e)) {
                SharedConfig.checkSdCard(this.cacheFileFinal);
            }
        }
        if (this.filePartsStream == null) {
            return;
        }
        int size = arrayList.size();
        int i = (size * 16) + 4;
        ImmutableByteArrayOutputStream immutableByteArrayOutputStream = filesQueueByteBuffer;
        if (immutableByteArrayOutputStream == null) {
            filesQueueByteBuffer = new ImmutableByteArrayOutputStream(i);
        } else {
            immutableByteArrayOutputStream.reset();
        }
        filesQueueByteBuffer.writeInt(size);
        for (int i2 = 0; i2 < size; i2++) {
            Range range = (Range) arrayList.get(i2);
            filesQueueByteBuffer.writeLong(range.start);
            filesQueueByteBuffer.writeLong(range.end);
        }
        synchronized (this) {
            RandomAccessFile randomAccessFile = this.filePartsStream;
            if (randomAccessFile == null) {
                return;
            }
            randomAccessFile.seek(0L);
            this.filePartsStream.write(filesQueueByteBuffer.buf, 0, i);
            this.totalTime += System.currentTimeMillis() - currentTimeMillis;
        }
    }

    private void notifyStreamListeners() {
        ArrayList<FileLoadOperationStream> arrayList = this.streamListeners;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                this.streamListeners.get(i).newDataAvailable();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public File getCacheFileFinal() {
        return this.cacheFileFinal;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public File getCurrentFile() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final File[] fileArr = new File[1];
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$getCurrentFile$3(fileArr, countDownLatch);
            }
        });
        try {
            countDownLatch.await();
        } catch (Exception e) {
            FileLog.e(e);
        }
        return fileArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCurrentFile$3(File[] fileArr, CountDownLatch countDownLatch) {
        if (this.state == 3 && !this.preloadFinished) {
            fileArr[0] = this.cacheFileFinal;
        } else {
            fileArr[0] = this.cacheFileTemp;
        }
        countDownLatch.countDown();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public File getCurrentFileFast() {
        if (this.state == 3 && !this.preloadFinished) {
            return this.cacheFileFinal;
        }
        return this.cacheFileTemp;
    }

    private long getDownloadedLengthFromOffsetInternal(ArrayList<Range> arrayList, long j, long j2) {
        long j3;
        if (arrayList == null || this.state == 3 || arrayList.isEmpty()) {
            if (this.state == 3) {
                return j2;
            }
            long j4 = this.downloadedBytes;
            if (j4 == 0) {
                return 0L;
            }
            return Math.min(j2, Math.max(j4 - j, 0L));
        }
        int size = arrayList.size();
        Range range = null;
        int i = 0;
        while (true) {
            if (i >= size) {
                j3 = j2;
                break;
            }
            Range range2 = arrayList.get(i);
            if (j <= range2.start && (range == null || range2.start < range.start)) {
                range = range2;
            }
            if (range2.start <= j && range2.end > j) {
                j3 = 0;
                break;
            }
            i++;
        }
        if (j3 == 0) {
            return 0L;
        }
        if (range != null) {
            return Math.min(j2, range.start - j);
        }
        return Math.min(j2, Math.max(this.totalBytesCount - j, 0L));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getDownloadedLengthFromOffset(float f) {
        ArrayList<Range> arrayList = this.notLoadedBytesRangesCopy;
        long j = this.totalBytesCount;
        if (j == 0 || arrayList == null) {
            return 0.0f;
        }
        return f + (((float) getDownloadedLengthFromOffsetInternal(arrayList, (int) (((float) j) * f), j)) / ((float) this.totalBytesCount));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long[] getDownloadedLengthFromOffset(final long j, final long j2) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final long[] jArr = new long[2];
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$getDownloadedLengthFromOffset$4(jArr, j, j2, countDownLatch);
            }
        });
        try {
            countDownLatch.await();
        } catch (Exception unused) {
        }
        return jArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getDownloadedLengthFromOffset$4(long[] jArr, long j, long j2, CountDownLatch countDownLatch) {
        try {
            jArr[0] = getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, j, j2);
        } catch (Throwable th) {
            FileLog.e(th);
            jArr[0] = 0;
        }
        if (this.state == 3) {
            jArr[1] = 1;
        }
        countDownLatch.countDown();
    }

    public String getFileName() {
        return this.fileName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeStreamListener(final FileLoadOperationStream fileLoadOperationStream) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$removeStreamListener$5(fileLoadOperationStream);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeStreamListener$5(FileLoadOperationStream fileLoadOperationStream) {
        ArrayList<FileLoadOperationStream> arrayList = this.streamListeners;
        if (arrayList == null) {
            return;
        }
        arrayList.remove(fileLoadOperationStream);
    }

    private void copyNotLoadedRanges() {
        if (this.notLoadedBytesRanges == null) {
            return;
        }
        this.notLoadedBytesRangesCopy = new ArrayList<>(this.notLoadedBytesRanges);
    }

    public void pause() {
        if (this.state != 1) {
            return;
        }
        this.paused = true;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$pause$6();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pause$6() {
        if (this.isStory) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("debug_loading:" + this.cacheFileFinal.getName() + " pause operation, clear requests");
            }
            clearOperaion(null, false);
            return;
        }
        for (int i = 0; i < this.requestInfos.size(); i++) {
            ConnectionsManager.getInstance(this.currentAccount).failNotRunningRequest(this.requestInfos.get(i).requestToken);
        }
    }

    public boolean start() {
        return start(this.stream, this.streamOffset, this.streamPriority);
    }

    /* JADX WARN: Code restructure failed: missing block: B:128:0x0416, code lost:
        if (r10 != r28.cacheFileFinal.length()) goto L70;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:114:0x03b4  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x03db  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x03fc  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0472  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x0679  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x06a4  */
    /* JADX WARN: Removed duplicated region for block: B:271:0x0726  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x072c  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x0756  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x07b4  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x07df  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x080b  */
    /* JADX WARN: Removed duplicated region for block: B:308:0x0854  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x08c2  */
    /* JADX WARN: Removed duplicated region for block: B:342:0x08e7 A[Catch: Exception -> 0x08ed, TRY_LEAVE, TryCatch #2 {Exception -> 0x08ed, blocks: (B:340:0x08d6, B:342:0x08e7), top: B:382:0x08d6 }] */
    /* JADX WARN: Removed duplicated region for block: B:355:0x0919  */
    /* JADX WARN: Removed duplicated region for block: B:357:0x091d  */
    /* JADX WARN: Removed duplicated region for block: B:358:0x092c  */
    /* JADX WARN: Removed duplicated region for block: B:384:0x0684 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r1v40 */
    /* JADX WARN: Type inference failed for: r1v41, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r1v45 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean start(FileLoadOperationStream fileLoadOperationStream, final long j, final boolean z) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        Object obj;
        boolean exists;
        boolean z2;
        int i;
        int i2;
        boolean z3;
        boolean[] zArr;
        String str8;
        boolean[] zArr2;
        String str9;
        ArrayList<Range> arrayList;
        ?? r1;
        boolean z4;
        long j2;
        long j3;
        RandomAccessFile randomAccessFile;
        this.startTime = System.currentTimeMillis();
        updateParams();
        boolean z5 = this.parentObject instanceof TL_stories$TL_storyItem;
        this.isStory = z5;
        if (this.currentDownloadChunkSize == 0) {
            if (this.forceSmallChunk) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("debug_loading: restart with small chunk");
                }
                this.currentDownloadChunkSize = LiteMode.FLAG_CHAT_SCALE;
                this.currentMaxDownloadRequests = 4;
            } else if (z5) {
                this.currentDownloadChunkSize = this.downloadChunkSizeBig;
                this.currentMaxDownloadRequests = this.maxDownloadRequestsBig;
            } else if (this.isStream) {
                this.currentDownloadChunkSize = this.downloadChunkSizeAnimation;
                this.currentMaxDownloadRequests = this.maxDownloadRequestsAnimation;
            } else {
                boolean z6 = this.totalBytesCount >= ((long) this.bigFileSizeFrom);
                this.currentDownloadChunkSize = z6 ? this.downloadChunkSizeBig : this.downloadChunkSize;
                this.currentMaxDownloadRequests = z6 ? this.maxDownloadRequestsBig : this.maxDownloadRequests;
            }
        }
        boolean z7 = this.state != 0;
        boolean z8 = this.paused;
        this.paused = false;
        if (fileLoadOperationStream != null) {
            final boolean z9 = z7;
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoadOperation.this.lambda$start$7(z, j, z9);
                }
            });
        } else if (z7) {
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoadOperation.this.lambda$start$8();
                }
            });
        }
        if (z7) {
            return z8;
        }
        if (this.location == null && this.webLocation == null) {
            onFail(true, 0);
            return false;
        }
        int i3 = this.currentDownloadChunkSize;
        this.streamStartOffset = (j / i3) * i3;
        if (this.allowDisordererFileSave) {
            long j4 = this.totalBytesCount;
            if (j4 > 0 && j4 > i3) {
                this.notLoadedBytesRanges = new ArrayList<>();
                this.notRequestedBytesRanges = new ArrayList<>();
            }
        }
        if (this.webLocation != null) {
            String MD5 = Utilities.MD5(this.webFile.url);
            if (this.encryptFile) {
                str6 = MD5 + ".temp.enc";
                str2 = MD5 + "." + this.ext + ".enc";
                if (this.key != null) {
                    str7 = MD5 + "_64.iv.enc";
                }
                str7 = null;
            } else {
                String str10 = MD5 + ".temp";
                str2 = MD5 + "." + this.ext;
                if (this.key != null) {
                    str7 = MD5 + "_64.iv";
                    str6 = str10;
                } else {
                    str6 = str10;
                    str7 = null;
                }
            }
            this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
            this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
            this.state = 1;
            if (!(this.parentObject instanceof TLRPC$TL_theme)) {
                this.cacheFileFinal = new File(ApplicationLoader.getFilesDirFixed(), "remote" + ((TLRPC$TL_theme) obj).id + ".attheme");
            } else if (!this.encryptFile) {
                this.cacheFileFinal = new File(this.storePath, this.storeFileName);
            } else {
                this.cacheFileFinal = new File(this.storePath, str2);
            }
            exists = this.cacheFileFinal.exists();
            if (exists) {
                if (!(this.parentObject instanceof TLRPC$TL_theme)) {
                    long j5 = this.totalBytesCount;
                    if (j5 != 0) {
                        if (!this.ungzip) {
                        }
                    }
                }
                if (!this.delegate.isLocallyCreatedFile(this.cacheFileFinal.toString())) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("debug_loading: delete existing file cause file size mismatch " + this.cacheFileFinal.getName() + " totalSize=" + this.totalBytesCount + " existingFileSize=" + this.cacheFileFinal.length());
                    }
                    if (!this.delegate.hasAnotherRefOnFile(this.cacheFileFinal.toString())) {
                        this.cacheFileFinal.delete();
                    }
                    exists = false;
                }
            }
            if (exists) {
                this.cacheFileTemp = new File(this.tempPath, str6);
                if (this.ungzip) {
                    this.cacheFileGzipTemp = new File(this.tempPath, str6 + ".gz");
                }
                String str11 = "rws";
                if (this.encryptFile) {
                    File file = new File(FileLoader.getInternalCacheDir(), str2 + ".key");
                    try {
                        RandomAccessFile randomAccessFile2 = new RandomAccessFile(file, "rws");
                        long length = file.length();
                        byte[] bArr = new byte[32];
                        this.encryptKey = bArr;
                        this.encryptIv = new byte[16];
                        if (length > 0 && length % 48 == 0) {
                            randomAccessFile2.read(bArr, 0, 32);
                            randomAccessFile2.read(this.encryptIv, 0, 16);
                            z3 = false;
                        } else {
                            Utilities.random.nextBytes(bArr);
                            Utilities.random.nextBytes(this.encryptIv);
                            randomAccessFile2.write(this.encryptKey);
                            randomAccessFile2.write(this.encryptIv);
                            z3 = true;
                        }
                        try {
                            try {
                                randomAccessFile2.getChannel().close();
                            } catch (Exception e) {
                                e = e;
                                if (AndroidUtilities.isENOSPC(e)) {
                                    LaunchActivity.checkFreeDiscSpaceStatic(1);
                                    FileLog.e((Throwable) e, false);
                                } else if (AndroidUtilities.isEROFS(e)) {
                                    SharedConfig.checkSdCard(this.cacheFileFinal);
                                    FileLog.e((Throwable) e, false);
                                } else {
                                    FileLog.e(e);
                                }
                                i2 = 1;
                                zArr = new boolean[i2];
                                zArr[0] = false;
                                long j6 = 8;
                                if (this.supportsPreloading) {
                                }
                                str8 = "rws";
                                zArr2 = zArr;
                                if (str4 == null) {
                                }
                                if (this.fileMetadata != null) {
                                }
                                if (!this.cacheFileTemp.exists()) {
                                }
                                arrayList = this.notLoadedBytesRanges;
                                if (arrayList != null) {
                                }
                                if (BuildVars.LOGS_ENABLED) {
                                }
                                if (str3 != null) {
                                }
                                if (!this.isPreloadVideoOperation) {
                                    copyNotLoadedRanges();
                                }
                                updateProgress();
                                RandomAccessFile randomAccessFile3 = new RandomAccessFile(this.cacheFileTemp, str9);
                                this.fileOutputStream = randomAccessFile3;
                                j2 = this.downloadedBytes;
                                if (j2 != 0) {
                                }
                                r1 = 0;
                                z4 = true;
                                if (this.fileOutputStream != null) {
                                }
                            }
                        } catch (Exception e2) {
                            FileLog.e(e2);
                        }
                        randomAccessFile2.close();
                    } catch (Exception e3) {
                        e = e3;
                        z3 = false;
                    }
                    i2 = 1;
                } else {
                    i2 = 1;
                    z3 = false;
                }
                zArr = new boolean[i2];
                zArr[0] = false;
                long j62 = 8;
                if (this.supportsPreloading || str5 == null) {
                    str8 = "rws";
                    zArr2 = zArr;
                } else {
                    this.cacheFilePreload = new File(this.tempPath, str5);
                    try {
                        RandomAccessFile randomAccessFile4 = new RandomAccessFile(this.cacheFilePreload, "rws");
                        this.preloadStream = randomAccessFile4;
                        long length2 = randomAccessFile4.length();
                        this.preloadStreamFileOffset = 1;
                        long j7 = 1;
                        if (length2 - 0 > 1) {
                            zArr[0] = this.preloadStream.readByte() != 0;
                            while (j7 < length2) {
                                if (length2 - j7 < j62) {
                                    break;
                                }
                                long readLong = this.preloadStream.readLong();
                                long j8 = j7 + j62;
                                if (length2 - j8 < j62 || readLong < 0) {
                                    break;
                                }
                                boolean[] zArr3 = zArr;
                                try {
                                    if (readLong <= this.totalBytesCount) {
                                        long readLong2 = this.preloadStream.readLong();
                                        long j9 = j8 + j62;
                                        if (length2 - j9 >= readLong2 && readLong2 <= this.currentDownloadChunkSize) {
                                            PreloadRange preloadRange = new PreloadRange(j9, readLong2);
                                            long j10 = j9 + readLong2;
                                            this.preloadStream.seek(j10);
                                            if (length2 - j10 >= 24) {
                                                str8 = str11;
                                                try {
                                                    long readLong3 = this.preloadStream.readLong();
                                                    this.foundMoovSize = readLong3;
                                                    if (readLong3 != 0) {
                                                        zArr2 = zArr3;
                                                        try {
                                                            this.moovFound = this.nextPreloadDownloadOffset > this.totalBytesCount / 2 ? 2 : 1;
                                                            this.preloadNotRequestedBytesCount = readLong3;
                                                        } catch (Exception e4) {
                                                            e = e4;
                                                            FileLog.e((Throwable) e, false);
                                                            if (!this.isPreloadVideoOperation) {
                                                                this.cacheFilePreload = null;
                                                                try {
                                                                    randomAccessFile = this.preloadStream;
                                                                    if (randomAccessFile != null) {
                                                                    }
                                                                } catch (Exception e5) {
                                                                    FileLog.e(e5);
                                                                }
                                                            }
                                                            if (str4 == null) {
                                                            }
                                                            if (this.fileMetadata != null) {
                                                            }
                                                            if (!this.cacheFileTemp.exists()) {
                                                            }
                                                            arrayList = this.notLoadedBytesRanges;
                                                            if (arrayList != null) {
                                                            }
                                                            if (BuildVars.LOGS_ENABLED) {
                                                            }
                                                            if (str3 != null) {
                                                            }
                                                            if (!this.isPreloadVideoOperation) {
                                                            }
                                                            updateProgress();
                                                            RandomAccessFile randomAccessFile32 = new RandomAccessFile(this.cacheFileTemp, str9);
                                                            this.fileOutputStream = randomAccessFile32;
                                                            j2 = this.downloadedBytes;
                                                            if (j2 != 0) {
                                                            }
                                                            r1 = 0;
                                                            z4 = true;
                                                            if (this.fileOutputStream != null) {
                                                            }
                                                        }
                                                    } else {
                                                        zArr2 = zArr3;
                                                    }
                                                    this.nextPreloadDownloadOffset = this.preloadStream.readLong();
                                                    this.nextAtomOffset = this.preloadStream.readLong();
                                                    long j11 = j10 + 24;
                                                    if (this.preloadedBytesRanges == null) {
                                                        this.preloadedBytesRanges = new HashMap<>();
                                                    }
                                                    if (this.requestedPreloadedBytesRanges == null) {
                                                        this.requestedPreloadedBytesRanges = new HashMap<>();
                                                    }
                                                    this.preloadedBytesRanges.put(Long.valueOf(readLong), preloadRange);
                                                    this.requestedPreloadedBytesRanges.put(Long.valueOf(readLong), 1);
                                                    this.totalPreloadedBytes = (int) (this.totalPreloadedBytes + readLong2);
                                                    this.preloadStreamFileOffset = (int) (this.preloadStreamFileOffset + readLong2 + 36);
                                                    zArr = zArr2;
                                                    j62 = 8;
                                                    j7 = j11;
                                                    str11 = str8;
                                                } catch (Exception e6) {
                                                    e = e6;
                                                    zArr2 = zArr3;
                                                    FileLog.e((Throwable) e, false);
                                                    if (!this.isPreloadVideoOperation) {
                                                    }
                                                    if (str4 == null) {
                                                    }
                                                    if (this.fileMetadata != null) {
                                                    }
                                                    if (!this.cacheFileTemp.exists()) {
                                                    }
                                                    arrayList = this.notLoadedBytesRanges;
                                                    if (arrayList != null) {
                                                    }
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    if (str3 != null) {
                                                    }
                                                    if (!this.isPreloadVideoOperation) {
                                                    }
                                                    updateProgress();
                                                    RandomAccessFile randomAccessFile322 = new RandomAccessFile(this.cacheFileTemp, str9);
                                                    this.fileOutputStream = randomAccessFile322;
                                                    j2 = this.downloadedBytes;
                                                    if (j2 != 0) {
                                                    }
                                                    r1 = 0;
                                                    z4 = true;
                                                    if (this.fileOutputStream != null) {
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    str8 = str11;
                                    zArr2 = zArr3;
                                    break;
                                } catch (Exception e7) {
                                    e = e7;
                                    str8 = str11;
                                }
                            }
                        }
                        str8 = str11;
                        zArr2 = zArr;
                        this.preloadStream.seek(this.preloadStreamFileOffset);
                    } catch (Exception e8) {
                        e = e8;
                        str8 = str11;
                        zArr2 = zArr;
                    }
                    if (!this.isPreloadVideoOperation && this.preloadedBytesRanges == null) {
                        this.cacheFilePreload = null;
                        randomAccessFile = this.preloadStream;
                        if (randomAccessFile != null) {
                            try {
                                randomAccessFile.getChannel().close();
                            } catch (Exception e9) {
                                FileLog.e(e9);
                            }
                            this.preloadStream.close();
                            this.preloadStream = null;
                        }
                    }
                }
                if (str4 == null) {
                    this.cacheFileParts = new File(this.tempPath, str4);
                    if (!this.cacheFileTemp.exists()) {
                        this.cacheFileParts.delete();
                    }
                    try {
                        str9 = str8;
                        try {
                            RandomAccessFile randomAccessFile5 = new RandomAccessFile(this.cacheFileParts, str9);
                            this.filePartsStream = randomAccessFile5;
                            long length3 = randomAccessFile5.length();
                            if (length3 % 8 == 4) {
                                int readInt = this.filePartsStream.readInt();
                                if (readInt <= (length3 - 4) / 2) {
                                    for (int i4 = 0; i4 < readInt; i4++) {
                                        long readLong4 = this.filePartsStream.readLong();
                                        long readLong5 = this.filePartsStream.readLong();
                                        this.notLoadedBytesRanges.add(new Range(readLong4, readLong5));
                                        this.notRequestedBytesRanges.add(new Range(readLong4, readLong5));
                                    }
                                }
                            }
                        } catch (Exception e10) {
                            e = e10;
                            FileLog.e(e, !AndroidUtilities.isFilNotFoundException(e));
                            if (this.fileMetadata != null) {
                            }
                            if (!this.cacheFileTemp.exists()) {
                            }
                            arrayList = this.notLoadedBytesRanges;
                            if (arrayList != null) {
                            }
                            if (BuildVars.LOGS_ENABLED) {
                            }
                            if (str3 != null) {
                            }
                            if (!this.isPreloadVideoOperation) {
                            }
                            updateProgress();
                            RandomAccessFile randomAccessFile3222 = new RandomAccessFile(this.cacheFileTemp, str9);
                            this.fileOutputStream = randomAccessFile3222;
                            j2 = this.downloadedBytes;
                            if (j2 != 0) {
                            }
                            r1 = 0;
                            z4 = true;
                            if (this.fileOutputStream != null) {
                            }
                        }
                    } catch (Exception e11) {
                        e = e11;
                        str9 = str8;
                    }
                } else {
                    str9 = str8;
                }
                if (this.fileMetadata != null) {
                    FileLoader.getInstance(this.currentAccount).getFileDatabase().saveFileDialogId(this.cacheFileParts, this.fileMetadata);
                    FileLoader.getInstance(this.currentAccount).getFileDatabase().saveFileDialogId(this.cacheFileTemp, this.fileMetadata);
                }
                if (!this.cacheFileTemp.exists()) {
                    ArrayList<Range> arrayList2 = this.notLoadedBytesRanges;
                    if (arrayList2 != null && arrayList2.isEmpty()) {
                        this.notLoadedBytesRanges.add(new Range(0L, this.totalBytesCount));
                        this.notRequestedBytesRanges.add(new Range(0L, this.totalBytesCount));
                    }
                } else if (z3) {
                    this.cacheFileTemp.delete();
                } else {
                    long length4 = this.cacheFileTemp.length();
                    if (str3 != null && length4 % this.currentDownloadChunkSize != 0) {
                        this.requestedBytesCount = 0L;
                    } else {
                        long floorDiv = floorDiv(this.cacheFileTemp.length(), this.currentDownloadChunkSize) * this.currentDownloadChunkSize;
                        this.downloadedBytes = floorDiv;
                        this.requestedBytesCount = floorDiv;
                    }
                    ArrayList<Range> arrayList3 = this.notLoadedBytesRanges;
                    if (arrayList3 != null && arrayList3.isEmpty()) {
                        this.notLoadedBytesRanges.add(new Range(this.downloadedBytes, this.totalBytesCount));
                        this.notRequestedBytesRanges.add(new Range(this.downloadedBytes, this.totalBytesCount));
                    }
                }
                arrayList = this.notLoadedBytesRanges;
                if (arrayList != null) {
                    this.downloadedBytes = this.totalBytesCount;
                    int size = arrayList.size();
                    for (int i5 = 0; i5 < size; i5++) {
                        Range range = this.notLoadedBytesRanges.get(i5);
                        this.downloadedBytes -= range.end - range.start;
                    }
                    this.requestedBytesCount = this.downloadedBytes;
                }
                if (BuildVars.LOGS_ENABLED) {
                    if (this.isPreloadVideoOperation) {
                        FileLog.d("start preloading file to temp = " + this.cacheFileTemp);
                    } else {
                        FileLog.d("start loading file to temp = " + this.cacheFileTemp + " final = " + this.cacheFileFinal + " priority" + this.priority);
                    }
                }
                if (str3 != null) {
                    this.cacheIvTemp = new File(this.tempPath, str3);
                    try {
                        this.fiv = new RandomAccessFile(this.cacheIvTemp, str9);
                        if (this.downloadedBytes != 0 && !z3) {
                            long length5 = this.cacheIvTemp.length();
                            if (length5 > 0 && length5 % 64 == 0) {
                                this.fiv.read(this.iv, 0, 64);
                            } else {
                                j3 = 0;
                                try {
                                    this.downloadedBytes = 0L;
                                    this.requestedBytesCount = 0L;
                                } catch (Exception e12) {
                                    e = e12;
                                    this.downloadedBytes = j3;
                                    this.requestedBytesCount = j3;
                                    if (AndroidUtilities.isENOSPC(e)) {
                                        LaunchActivity.checkFreeDiscSpaceStatic(1);
                                        FileLog.e((Throwable) e, false);
                                    } else if (AndroidUtilities.isEROFS(e)) {
                                        SharedConfig.checkSdCard(this.cacheFileFinal);
                                        FileLog.e((Throwable) e, false);
                                    } else {
                                        FileLog.e(e);
                                    }
                                    if (!this.isPreloadVideoOperation) {
                                    }
                                    updateProgress();
                                    RandomAccessFile randomAccessFile32222 = new RandomAccessFile(this.cacheFileTemp, str9);
                                    this.fileOutputStream = randomAccessFile32222;
                                    j2 = this.downloadedBytes;
                                    if (j2 != 0) {
                                    }
                                    r1 = 0;
                                    z4 = true;
                                    if (this.fileOutputStream != null) {
                                    }
                                }
                            }
                        }
                    } catch (Exception e13) {
                        e = e13;
                        j3 = 0;
                    }
                }
                if (!this.isPreloadVideoOperation && this.downloadedBytes != 0 && this.totalBytesCount > 0) {
                    copyNotLoadedRanges();
                }
                updateProgress();
                try {
                    RandomAccessFile randomAccessFile322222 = new RandomAccessFile(this.cacheFileTemp, str9);
                    this.fileOutputStream = randomAccessFile322222;
                    j2 = this.downloadedBytes;
                    if (j2 != 0) {
                        randomAccessFile322222.seek(j2);
                    }
                    r1 = 0;
                    z4 = true;
                } catch (Exception e14) {
                    r1 = 0;
                    FileLog.e((Throwable) e14, false);
                    if (AndroidUtilities.isENOSPC(e14)) {
                        LaunchActivity.checkFreeDiscSpaceStatic(1);
                        onFail(true, -1);
                        return false;
                    }
                    z4 = true;
                    if (AndroidUtilities.isEROFS(e14)) {
                        SharedConfig.checkSdCard(this.cacheFileFinal);
                        FileLog.e((Throwable) e14, false);
                        onFail(true, -1);
                        return false;
                    }
                }
                if (this.fileOutputStream != null) {
                    onFail(z4, r1);
                    return r1;
                }
                this.started = z4;
                final boolean[] zArr4 = zArr2;
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda16
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileLoadOperation.this.lambda$start$9(zArr4);
                    }
                });
            } else {
                boolean z10 = false;
                this.started = true;
                try {
                    onFinishLoadingFile(false, 1, false);
                    try {
                        FilePathDatabase.PathData pathData = this.pathSaveData;
                        if (pathData != null) {
                            this.delegate.saveFilePath(pathData, this.cacheFileFinal);
                        }
                    } catch (Exception e15) {
                        e = e15;
                        z10 = false;
                        FileLog.e(e, z10);
                        if (AndroidUtilities.isENOSPC(e)) {
                            z2 = true;
                            LaunchActivity.checkFreeDiscSpaceStatic(1);
                            i = -1;
                            onFail(true, -1);
                        } else {
                            z2 = true;
                            i = -1;
                        }
                        if (AndroidUtilities.isEROFS(e)) {
                            SharedConfig.checkSdCard(this.cacheFileFinal);
                            onFail(z2, i);
                            return false;
                        }
                        onFail(z2, 0);
                        return z2;
                    }
                } catch (Exception e16) {
                    e = e16;
                }
            }
            return true;
        }
        TLRPC$InputFileLocation tLRPC$InputFileLocation = this.location;
        long j12 = tLRPC$InputFileLocation.volume_id;
        if (j12 != 0 && tLRPC$InputFileLocation.local_id != 0) {
            int i6 = this.datacenterId;
            if (i6 == Integer.MIN_VALUE || j12 == -2147483648L || i6 == 0) {
                onFail(true, 0);
                return false;
            } else if (this.encryptFile) {
                str6 = this.location.volume_id + "_" + this.location.local_id + ".temp.enc";
                str2 = this.location.volume_id + "_" + this.location.local_id + "." + this.ext + ".enc";
                if (this.key != null) {
                    str7 = this.location.volume_id + "_" + this.location.local_id + "_64.iv.enc";
                }
                str3 = null;
                str4 = null;
                str5 = null;
                this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
                this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
                this.state = 1;
                if (!(this.parentObject instanceof TLRPC$TL_theme)) {
                }
                exists = this.cacheFileFinal.exists();
                if (exists) {
                }
                if (exists) {
                }
                return true;
            } else {
                str = this.location.volume_id + "_" + this.location.local_id + ".temp";
                str2 = this.location.volume_id + "_" + this.location.local_id + "." + this.ext;
                str3 = this.key != null ? this.location.volume_id + "_" + this.location.local_id + "_64.iv" : null;
                str4 = this.notLoadedBytesRanges != null ? this.location.volume_id + "_" + this.location.local_id + "_64.pt" : null;
                str5 = this.location.volume_id + "_" + this.location.local_id + "_64.preload";
                str6 = str;
                this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
                this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
                this.state = 1;
                if (!(this.parentObject instanceof TLRPC$TL_theme)) {
                }
                exists = this.cacheFileFinal.exists();
                if (exists) {
                }
                if (exists) {
                }
                return true;
            }
        } else if (this.datacenterId == 0 || tLRPC$InputFileLocation.id == 0) {
            onFail(true, 0);
            return false;
        } else if (this.encryptFile) {
            str6 = this.datacenterId + "_" + this.location.id + ".temp.enc";
            str2 = this.datacenterId + "_" + this.location.id + this.ext + ".enc";
            if (this.key != null) {
                str7 = this.datacenterId + "_" + this.location.id + "_64.iv.enc";
            }
            str3 = null;
            str4 = null;
            str5 = null;
            this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
            this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
            this.state = 1;
            if (!(this.parentObject instanceof TLRPC$TL_theme)) {
            }
            exists = this.cacheFileFinal.exists();
            if (exists) {
            }
            if (exists) {
            }
            return true;
        } else {
            str = this.datacenterId + "_" + this.location.id + ".temp";
            str2 = this.datacenterId + "_" + this.location.id + this.ext;
            str3 = this.key != null ? this.datacenterId + "_" + this.location.id + "_64.iv" : null;
            str4 = this.notLoadedBytesRanges != null ? this.datacenterId + "_" + this.location.id + "_64.pt" : null;
            str5 = this.datacenterId + "_" + this.location.id + "_64.preload";
            str6 = str;
            this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
            this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
            this.state = 1;
            if (!(this.parentObject instanceof TLRPC$TL_theme)) {
            }
            exists = this.cacheFileFinal.exists();
            if (exists) {
            }
            if (exists) {
            }
            return true;
        }
        str3 = str7;
        str4 = null;
        str5 = null;
        this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
        this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
        this.state = 1;
        if (!(this.parentObject instanceof TLRPC$TL_theme)) {
        }
        exists = this.cacheFileFinal.exists();
        if (exists) {
        }
        if (exists) {
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$7(boolean z, long j, boolean z2) {
        if (this.streamListeners == null) {
            this.streamListeners = new ArrayList<>();
        }
        if (z) {
            int i = this.currentDownloadChunkSize;
            long j2 = (j / i) * i;
            RequestInfo requestInfo = this.priorityRequestInfo;
            if (requestInfo != null && requestInfo.offset != j2) {
                this.requestInfos.remove(this.priorityRequestInfo);
                this.requestedBytesCount -= this.currentDownloadChunkSize;
                removePart(this.notRequestedBytesRanges, this.priorityRequestInfo.offset, this.currentDownloadChunkSize + this.priorityRequestInfo.offset);
                if (this.priorityRequestInfo.requestToken != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.priorityRequestInfo.requestToken, true);
                    this.requestsCount--;
                }
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("frame get cancel request at offset " + this.priorityRequestInfo.offset);
                }
                this.priorityRequestInfo = null;
            }
            if (this.priorityRequestInfo == null) {
                this.streamPriorityStartOffset = j2;
            }
        } else {
            int i2 = this.currentDownloadChunkSize;
            this.streamStartOffset = (j / i2) * i2;
        }
        if (z2) {
            if (this.preloadedBytesRanges != null && getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, this.streamStartOffset, 1L) == 0 && this.preloadedBytesRanges.get(Long.valueOf(this.streamStartOffset)) != null) {
                this.nextPartWasPreloaded = true;
            }
            startDownloadRequest(-1);
            this.nextPartWasPreloaded = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$8() {
        startDownloadRequest(-1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$9(boolean[] zArr) {
        boolean z = this.isPreloadVideoOperation && zArr[0];
        int i = this.preloadPrefixSize;
        boolean z2 = i > 0 && this.downloadedBytes >= ((long) i) && canFinishPreload();
        long j = this.totalBytesCount;
        if (j != 0 && (z || this.downloadedBytes == j || z2)) {
            try {
                onFinishLoadingFile(false, 1, true);
                return;
            } catch (Exception unused) {
                onFail(true, 0);
                return;
            }
        }
        startDownloadRequest(-1);
    }

    public void updateProgress() {
        FileLoadOperationDelegate fileLoadOperationDelegate = this.delegate;
        if (fileLoadOperationDelegate != null) {
            long j = this.downloadedBytes;
            long j2 = this.totalBytesCount;
            if (j == j2 || j2 <= 0) {
                return;
            }
            fileLoadOperationDelegate.didChangedLoadProgress(this, j, j2);
        }
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void setIsPreloadVideoOperation(final boolean z) {
        boolean z2 = this.isPreloadVideoOperation;
        if (z2 != z) {
            if (!z || this.totalBytesCount > 2097152) {
                if (!z && z2) {
                    if (this.state == 3) {
                        this.isPreloadVideoOperation = z;
                        this.state = 0;
                        this.preloadFinished = false;
                        start();
                        return;
                    } else if (this.state == 1) {
                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda9
                            @Override // java.lang.Runnable
                            public final void run() {
                                FileLoadOperation.this.lambda$setIsPreloadVideoOperation$10(z);
                            }
                        });
                        return;
                    } else {
                        this.isPreloadVideoOperation = z;
                        return;
                    }
                }
                this.isPreloadVideoOperation = z;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setIsPreloadVideoOperation$10(boolean z) {
        this.requestedBytesCount = 0L;
        clearOperaion(null, true);
        this.isPreloadVideoOperation = z;
        startDownloadRequest(-1);
    }

    public boolean isPreloadVideoOperation() {
        return this.isPreloadVideoOperation;
    }

    public boolean isPreloadFinished() {
        return this.preloadFinished;
    }

    public void cancel() {
        cancel(false);
    }

    private void cancel(final boolean z) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$cancel$11(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancel$11(boolean z) {
        if (this.state != 3 && this.state != 2) {
            cancelRequests();
            onFail(false, 1);
        }
        if (z) {
            File file = this.cacheFileFinal;
            if (file != null) {
                try {
                    if (!file.delete()) {
                        this.cacheFileFinal.deleteOnExit();
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            File file2 = this.cacheFileTemp;
            if (file2 != null) {
                try {
                    if (!file2.delete()) {
                        this.cacheFileTemp.deleteOnExit();
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
            File file3 = this.cacheFileParts;
            if (file3 != null) {
                try {
                    if (!file3.delete()) {
                        this.cacheFileParts.deleteOnExit();
                    }
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
            }
            File file4 = this.cacheIvTemp;
            if (file4 != null) {
                try {
                    if (!file4.delete()) {
                        this.cacheIvTemp.deleteOnExit();
                    }
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
            }
            File file5 = this.cacheFilePreload;
            if (file5 != null) {
                try {
                    if (file5.delete()) {
                        return;
                    }
                    this.cacheFilePreload.deleteOnExit();
                } catch (Exception e5) {
                    FileLog.e(e5);
                }
            }
        }
    }

    private void cancelRequests() {
        if (this.requestInfos != null) {
            int[] iArr = new int[2];
            int i = 0;
            for (int i2 = 0; i2 < this.requestInfos.size(); i2++) {
                RequestInfo requestInfo = this.requestInfos.get(i2);
                if (requestInfo.requestToken != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(requestInfo.requestToken, true);
                    char c = requestInfo.connectionType == 2 ? (char) 0 : (char) 1;
                    iArr[c] = iArr[c] + requestInfo.chunkSize;
                }
            }
            while (i < 2) {
                int i3 = i == 0 ? 2 : ConnectionsManager.ConnectionTypeDownload2;
                if (iArr[i] > 1048576) {
                    ConnectionsManager.getInstance(this.currentAccount).discardConnection(this.isCdn ? this.cdnDatacenterId : this.datacenterId, i3);
                }
                i++;
            }
        }
    }

    private void cleanup() {
        try {
            RandomAccessFile randomAccessFile = this.fileOutputStream;
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.getChannel().close();
                } catch (Exception e) {
                    FileLog.e(e);
                }
                this.fileOutputStream.close();
                this.fileOutputStream = null;
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        try {
            RandomAccessFile randomAccessFile2 = this.preloadStream;
            if (randomAccessFile2 != null) {
                try {
                    randomAccessFile2.getChannel().close();
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
                this.preloadStream.close();
                this.preloadStream = null;
            }
        } catch (Exception e4) {
            FileLog.e(e4);
        }
        try {
            RandomAccessFile randomAccessFile3 = this.fileReadStream;
            if (randomAccessFile3 != null) {
                try {
                    randomAccessFile3.getChannel().close();
                } catch (Exception e5) {
                    FileLog.e(e5);
                }
                this.fileReadStream.close();
                this.fileReadStream = null;
            }
        } catch (Exception e6) {
            FileLog.e(e6);
        }
        try {
            if (this.filePartsStream != null) {
                synchronized (this) {
                    try {
                        this.filePartsStream.getChannel().close();
                    } catch (Exception e7) {
                        FileLog.e(e7);
                    }
                    this.filePartsStream.close();
                    this.filePartsStream = null;
                }
            }
        } catch (Exception e8) {
            FileLog.e(e8);
        }
        try {
            RandomAccessFile randomAccessFile4 = this.fiv;
            if (randomAccessFile4 != null) {
                randomAccessFile4.close();
                this.fiv = null;
            }
        } catch (Exception e9) {
            FileLog.e(e9);
        }
        if (this.delayedRequestInfos != null) {
            for (int i = 0; i < this.delayedRequestInfos.size(); i++) {
                RequestInfo requestInfo = this.delayedRequestInfos.get(i);
                if (requestInfo.response != null) {
                    requestInfo.response.disableFree = false;
                    requestInfo.response.freeResources();
                } else if (requestInfo.responseWeb != null) {
                    requestInfo.responseWeb.disableFree = false;
                    requestInfo.responseWeb.freeResources();
                } else if (requestInfo.responseCdn != null) {
                    requestInfo.responseCdn.disableFree = false;
                    requestInfo.responseCdn.freeResources();
                }
            }
            this.delayedRequestInfos.clear();
        }
    }

    private void onFinishLoadingFile(final boolean z, int i, boolean z2) {
        if (this.state != 1) {
            return;
        }
        this.state = 3;
        notifyStreamListeners();
        cleanup();
        if (this.isPreloadVideoOperation || z2) {
            this.preloadFinished = true;
            if (BuildVars.DEBUG_VERSION) {
                if (i == 1) {
                    FileLog.d("file already exist " + this.cacheFileTemp);
                } else {
                    FileLog.d("finished preloading file to " + this.cacheFileTemp + " loaded " + this.downloadedBytes + " of " + this.totalBytesCount + " prefSize=" + this.preloadPrefixSize);
                }
            }
            if (this.fileMetadata != null) {
                if (this.cacheFileTemp != null) {
                    FileLoader.getInstance(this.currentAccount).getFileDatabase().removeFiles(Collections.singletonList(new CacheModel.FileInfo(this.cacheFileTemp)));
                }
                if (this.cacheFileParts != null) {
                    FileLoader.getInstance(this.currentAccount).getFileDatabase().removeFiles(Collections.singletonList(new CacheModel.FileInfo(this.cacheFileParts)));
                }
            }
            this.delegate.didPreFinishLoading(this, this.cacheFileFinal);
            this.delegate.didFinishLoadingFile(this, this.cacheFileFinal);
            return;
        }
        final File file = this.cacheIvTemp;
        final File file2 = this.cacheFileParts;
        final File file3 = this.cacheFilePreload;
        final File file4 = this.cacheFileTemp;
        filesQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$onFinishLoadingFile$15(file, file2, file3, file4, z);
            }
        });
        this.cacheIvTemp = null;
        this.cacheFileParts = null;
        this.cacheFilePreload = null;
        this.delegate.didPreFinishLoading(this, this.cacheFileFinal);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:26:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x017c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$onFinishLoadingFile$15(File file, File file2, File file3, File file4, final boolean z) {
        File file5;
        int lastIndexOf;
        String str;
        if (file != null) {
            file.delete();
        }
        if (file2 != null) {
            file2.delete();
        }
        if (file3 != null) {
            file3.delete();
        }
        if (file4 != null) {
            boolean z2 = false;
            if (this.ungzip) {
                try {
                    GZIPInputStream gZIPInputStream = new GZIPInputStream(new FileInputStream(file4));
                    FileLoader.copyFile(gZIPInputStream, this.cacheFileGzipTemp, preloadMaxBytes);
                    gZIPInputStream.close();
                    file4.delete();
                    file5 = this.cacheFileGzipTemp;
                    try {
                        this.ungzip = false;
                    } catch (ZipException unused) {
                        file4 = file5;
                        this.ungzip = false;
                        if (!this.ungzip) {
                        }
                    } catch (Throwable th) {
                        th = th;
                        FileLog.e(th, !AndroidUtilities.isFilNotFoundException(th));
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.e("unable to ungzip temp = " + file4 + " to final = " + this.cacheFileFinal);
                        }
                        file4 = file5;
                        if (!this.ungzip) {
                        }
                    }
                } catch (ZipException unused2) {
                } catch (Throwable th2) {
                    th = th2;
                    file5 = file4;
                }
                file4 = file5;
            }
            if (!this.ungzip) {
                if (this.parentObject instanceof TLRPC$TL_theme) {
                    try {
                        z2 = AndroidUtilities.copyFile(file4, this.cacheFileFinal);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                } else {
                    try {
                        if (this.pathSaveData != null) {
                            synchronized (lockObject) {
                                this.cacheFileFinal = new File(this.storePath, this.storeFileName);
                                int i = 1;
                                while (this.cacheFileFinal.exists()) {
                                    if (this.storeFileName.lastIndexOf(46) > 0) {
                                        str = this.storeFileName.substring(0, lastIndexOf) + " (" + i + ")" + this.storeFileName.substring(lastIndexOf);
                                    } else {
                                        str = this.storeFileName + " (" + i + ")";
                                    }
                                    this.cacheFileFinal = new File(this.storePath, str);
                                    i++;
                                }
                            }
                        }
                        z2 = file4.renameTo(this.cacheFileFinal);
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                }
                if (!z2 && this.renameRetryCount == 3) {
                    try {
                        z2 = AndroidUtilities.copyFile(file4, this.cacheFileFinal);
                        if (z2) {
                            this.cacheFileFinal.delete();
                        }
                    } catch (Throwable th3) {
                        FileLog.e(th3);
                    }
                }
                if (!z2) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.e("unable to rename temp = " + file4 + " to final = " + this.cacheFileFinal + " retry = " + this.renameRetryCount);
                    }
                    int i2 = this.renameRetryCount + 1;
                    this.renameRetryCount = i2;
                    if (i2 < 3) {
                        this.state = 1;
                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda11
                            @Override // java.lang.Runnable
                            public final void run() {
                                FileLoadOperation.this.lambda$onFinishLoadingFile$12(z);
                            }
                        }, 200L);
                        return;
                    }
                    this.cacheFileFinal = file4;
                } else if (this.pathSaveData != null && this.cacheFileFinal.exists()) {
                    this.delegate.saveFilePath(this.pathSaveData, this.cacheFileFinal);
                }
            } else {
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileLoadOperation.this.lambda$onFinishLoadingFile$13();
                    }
                });
                return;
            }
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$onFinishLoadingFile$14(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishLoadingFile$12(boolean z) {
        try {
            onFinishLoadingFile(z, 0, false);
        } catch (Exception unused) {
            onFail(false, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishLoadingFile$13() {
        onFail(false, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishLoadingFile$14(boolean z) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("finished downloading file to " + this.cacheFileFinal + " time = " + (System.currentTimeMillis() - this.startTime) + " dc = " + this.datacenterId + " size = " + AndroidUtilities.formatFileSize(this.totalBytesCount));
        }
        if (z) {
            int i = this.currentType;
            if (i == 50331648) {
                StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 3, 1);
            } else if (i == 33554432) {
                StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 2, 1);
            } else if (i == 16777216) {
                StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 4, 1);
            } else if (i == 67108864) {
                String str = this.ext;
                if (str != null && (str.toLowerCase().endsWith("mp3") || this.ext.toLowerCase().endsWith("m4a"))) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 7, 1);
                } else {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 5, 1);
                }
            }
        }
        this.delegate.didFinishLoadingFile(this, this.cacheFileFinal);
    }

    private void delayRequestInfo(RequestInfo requestInfo) {
        this.delayedRequestInfos.add(requestInfo);
        if (requestInfo.response != null) {
            requestInfo.response.disableFree = true;
        } else if (requestInfo.responseWeb != null) {
            requestInfo.responseWeb.disableFree = true;
        } else if (requestInfo.responseCdn != null) {
            requestInfo.responseCdn.disableFree = true;
        }
    }

    private long findNextPreloadDownloadOffset(long j, long j2, NativeByteBuffer nativeByteBuffer) {
        long j3;
        int limit = nativeByteBuffer.limit();
        long j4 = j;
        do {
            if (j4 >= j2 - (this.preloadTempBuffer != null ? 16 : 0)) {
                j3 = j2 + limit;
                if (j4 < j3) {
                    if (j4 >= j3 - 16) {
                        long j5 = j3 - j4;
                        if (j5 > 2147483647L) {
                            throw new RuntimeException("!!!");
                        }
                        this.preloadTempBufferCount = (int) j5;
                        nativeByteBuffer.position(nativeByteBuffer.limit() - this.preloadTempBufferCount);
                        nativeByteBuffer.readBytes(this.preloadTempBuffer, 0, this.preloadTempBufferCount, false);
                        return j3;
                    }
                    if (this.preloadTempBufferCount != 0) {
                        nativeByteBuffer.position(0);
                        byte[] bArr = this.preloadTempBuffer;
                        int i = this.preloadTempBufferCount;
                        nativeByteBuffer.readBytes(bArr, i, 16 - i, false);
                        this.preloadTempBufferCount = 0;
                    } else {
                        long j6 = j4 - j2;
                        if (j6 > 2147483647L) {
                            throw new RuntimeException("!!!");
                        }
                        nativeByteBuffer.position((int) j6);
                        nativeByteBuffer.readBytes(this.preloadTempBuffer, 0, 16, false);
                    }
                    byte[] bArr2 = this.preloadTempBuffer;
                    int i2 = ((bArr2[0] & 255) << 24) + ((bArr2[1] & 255) << 16) + ((bArr2[2] & 255) << 8) + (bArr2[3] & 255);
                    if (i2 == 0) {
                        return 0L;
                    }
                    if (i2 == 1) {
                        i2 = ((bArr2[12] & 255) << 24) + ((bArr2[13] & 255) << 16) + ((bArr2[14] & 255) << 8) + (bArr2[15] & 255);
                    }
                    if (bArr2[4] == 109 && bArr2[5] == 111 && bArr2[6] == 111 && bArr2[7] == 118) {
                        return -i2;
                    }
                    j4 += i2;
                }
            }
            return 0L;
        } while (j4 < j3);
        return j4;
    }

    private void requestFileOffsets(long j) {
        if (this.requestingCdnOffsets) {
            return;
        }
        this.requestingCdnOffsets = true;
        TLRPC$TL_upload_getCdnFileHashes tLRPC$TL_upload_getCdnFileHashes = new TLRPC$TL_upload_getCdnFileHashes();
        tLRPC$TL_upload_getCdnFileHashes.file_token = this.cdnToken;
        tLRPC$TL_upload_getCdnFileHashes.offset = j;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_upload_getCdnFileHashes, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda18
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                FileLoadOperation.this.lambda$requestFileOffsets$16(tLObject, tLRPC$TL_error);
            }
        }, null, null, 0, this.datacenterId, 1, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestFileOffsets$16(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error != null) {
            onFail(false, 0);
            return;
        }
        this.requestingCdnOffsets = false;
        TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
        if (!tLRPC$Vector.objects.isEmpty()) {
            if (this.cdnHashes == null) {
                this.cdnHashes = new HashMap<>();
            }
            for (int i = 0; i < tLRPC$Vector.objects.size(); i++) {
                TLRPC$TL_fileHash tLRPC$TL_fileHash = (TLRPC$TL_fileHash) tLRPC$Vector.objects.get(i);
                this.cdnHashes.put(Long.valueOf(tLRPC$TL_fileHash.offset), tLRPC$TL_fileHash);
            }
        }
        for (int i2 = 0; i2 < this.delayedRequestInfos.size(); i2++) {
            RequestInfo requestInfo = this.delayedRequestInfos.get(i2);
            if (this.notLoadedBytesRanges != null || this.downloadedBytes == requestInfo.offset) {
                this.delayedRequestInfos.remove(i2);
                if (processRequestResult(requestInfo, null)) {
                    return;
                }
                if (requestInfo.response != null) {
                    requestInfo.response.disableFree = false;
                    requestInfo.response.freeResources();
                    return;
                } else if (requestInfo.responseWeb != null) {
                    requestInfo.responseWeb.disableFree = false;
                    requestInfo.responseWeb.freeResources();
                    return;
                } else if (requestInfo.responseCdn != null) {
                    requestInfo.responseCdn.disableFree = false;
                    requestInfo.responseCdn.freeResources();
                    return;
                } else {
                    return;
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x0276  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x0535 A[Catch: Exception -> 0x05e8, TryCatch #1 {Exception -> 0x05e8, blocks: (B:12:0x0054, B:14:0x0058, B:16:0x0062, B:18:0x0066, B:20:0x006c, B:30:0x0091, B:33:0x0099, B:35:0x00a1, B:37:0x00b1, B:40:0x00bf, B:42:0x00c6, B:44:0x00de, B:46:0x0114, B:48:0x0118, B:50:0x013c, B:51:0x0165, B:53:0x0169, B:54:0x0170, B:56:0x019b, B:58:0x01ae, B:60:0x01c3, B:62:0x01d7, B:64:0x01e4, B:66:0x01e9, B:68:0x020a, B:70:0x020e, B:72:0x0214, B:74:0x021a, B:80:0x0226, B:203:0x0561, B:205:0x0569, B:207:0x0575, B:210:0x0580, B:211:0x0583, B:213:0x058f, B:215:0x0595, B:216:0x05a4, B:218:0x05aa, B:219:0x05b9, B:221:0x05bf, B:223:0x05cf, B:224:0x05d6, B:226:0x05db, B:81:0x0232, B:83:0x0236, B:61:0x01ce, B:63:0x01dc, B:84:0x0240, B:88:0x0252, B:90:0x0256, B:92:0x025b, B:94:0x0261, B:99:0x026d, B:116:0x0295, B:118:0x029b, B:120:0x02b4, B:122:0x02ba, B:127:0x02ce, B:128:0x02e4, B:129:0x02e5, B:130:0x02e9, B:132:0x02ed, B:133:0x031f, B:135:0x0323, B:137:0x0330, B:138:0x0367, B:140:0x038e, B:142:0x03a0, B:144:0x03b0, B:150:0x03c0, B:152:0x03d9, B:154:0x03e0, B:156:0x03e6, B:165:0x03fa, B:167:0x040a, B:168:0x041c, B:173:0x042d, B:174:0x0434, B:175:0x0435, B:177:0x0442, B:179:0x0480, B:181:0x048f, B:183:0x0493, B:185:0x0497, B:186:0x04e8, B:188:0x04ee, B:189:0x050c, B:191:0x0516, B:193:0x0531, B:195:0x0535, B:196:0x0541, B:198:0x0549, B:200:0x054e, B:147:0x03ba, B:102:0x0278, B:106:0x0280, B:227:0x05e2, B:22:0x0074, B:24:0x007a, B:25:0x0081, B:27:0x0087), top: B:289:0x0054 }] */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0569 A[Catch: Exception -> 0x05e8, TryCatch #1 {Exception -> 0x05e8, blocks: (B:12:0x0054, B:14:0x0058, B:16:0x0062, B:18:0x0066, B:20:0x006c, B:30:0x0091, B:33:0x0099, B:35:0x00a1, B:37:0x00b1, B:40:0x00bf, B:42:0x00c6, B:44:0x00de, B:46:0x0114, B:48:0x0118, B:50:0x013c, B:51:0x0165, B:53:0x0169, B:54:0x0170, B:56:0x019b, B:58:0x01ae, B:60:0x01c3, B:62:0x01d7, B:64:0x01e4, B:66:0x01e9, B:68:0x020a, B:70:0x020e, B:72:0x0214, B:74:0x021a, B:80:0x0226, B:203:0x0561, B:205:0x0569, B:207:0x0575, B:210:0x0580, B:211:0x0583, B:213:0x058f, B:215:0x0595, B:216:0x05a4, B:218:0x05aa, B:219:0x05b9, B:221:0x05bf, B:223:0x05cf, B:224:0x05d6, B:226:0x05db, B:81:0x0232, B:83:0x0236, B:61:0x01ce, B:63:0x01dc, B:84:0x0240, B:88:0x0252, B:90:0x0256, B:92:0x025b, B:94:0x0261, B:99:0x026d, B:116:0x0295, B:118:0x029b, B:120:0x02b4, B:122:0x02ba, B:127:0x02ce, B:128:0x02e4, B:129:0x02e5, B:130:0x02e9, B:132:0x02ed, B:133:0x031f, B:135:0x0323, B:137:0x0330, B:138:0x0367, B:140:0x038e, B:142:0x03a0, B:144:0x03b0, B:150:0x03c0, B:152:0x03d9, B:154:0x03e0, B:156:0x03e6, B:165:0x03fa, B:167:0x040a, B:168:0x041c, B:173:0x042d, B:174:0x0434, B:175:0x0435, B:177:0x0442, B:179:0x0480, B:181:0x048f, B:183:0x0493, B:185:0x0497, B:186:0x04e8, B:188:0x04ee, B:189:0x050c, B:191:0x0516, B:193:0x0531, B:195:0x0535, B:196:0x0541, B:198:0x0549, B:200:0x054e, B:147:0x03ba, B:102:0x0278, B:106:0x0280, B:227:0x05e2, B:22:0x0074, B:24:0x007a, B:25:0x0081, B:27:0x0087), top: B:289:0x0054 }] */
    /* JADX WARN: Removed duplicated region for block: B:223:0x05cf A[Catch: Exception -> 0x05e8, TryCatch #1 {Exception -> 0x05e8, blocks: (B:12:0x0054, B:14:0x0058, B:16:0x0062, B:18:0x0066, B:20:0x006c, B:30:0x0091, B:33:0x0099, B:35:0x00a1, B:37:0x00b1, B:40:0x00bf, B:42:0x00c6, B:44:0x00de, B:46:0x0114, B:48:0x0118, B:50:0x013c, B:51:0x0165, B:53:0x0169, B:54:0x0170, B:56:0x019b, B:58:0x01ae, B:60:0x01c3, B:62:0x01d7, B:64:0x01e4, B:66:0x01e9, B:68:0x020a, B:70:0x020e, B:72:0x0214, B:74:0x021a, B:80:0x0226, B:203:0x0561, B:205:0x0569, B:207:0x0575, B:210:0x0580, B:211:0x0583, B:213:0x058f, B:215:0x0595, B:216:0x05a4, B:218:0x05aa, B:219:0x05b9, B:221:0x05bf, B:223:0x05cf, B:224:0x05d6, B:226:0x05db, B:81:0x0232, B:83:0x0236, B:61:0x01ce, B:63:0x01dc, B:84:0x0240, B:88:0x0252, B:90:0x0256, B:92:0x025b, B:94:0x0261, B:99:0x026d, B:116:0x0295, B:118:0x029b, B:120:0x02b4, B:122:0x02ba, B:127:0x02ce, B:128:0x02e4, B:129:0x02e5, B:130:0x02e9, B:132:0x02ed, B:133:0x031f, B:135:0x0323, B:137:0x0330, B:138:0x0367, B:140:0x038e, B:142:0x03a0, B:144:0x03b0, B:150:0x03c0, B:152:0x03d9, B:154:0x03e0, B:156:0x03e6, B:165:0x03fa, B:167:0x040a, B:168:0x041c, B:173:0x042d, B:174:0x0434, B:175:0x0435, B:177:0x0442, B:179:0x0480, B:181:0x048f, B:183:0x0493, B:185:0x0497, B:186:0x04e8, B:188:0x04ee, B:189:0x050c, B:191:0x0516, B:193:0x0531, B:195:0x0535, B:196:0x0541, B:198:0x0549, B:200:0x054e, B:147:0x03ba, B:102:0x0278, B:106:0x0280, B:227:0x05e2, B:22:0x0074, B:24:0x007a, B:25:0x0081, B:27:0x0087), top: B:289:0x0054 }] */
    /* JADX WARN: Removed duplicated region for block: B:224:0x05d6 A[Catch: Exception -> 0x05e8, TryCatch #1 {Exception -> 0x05e8, blocks: (B:12:0x0054, B:14:0x0058, B:16:0x0062, B:18:0x0066, B:20:0x006c, B:30:0x0091, B:33:0x0099, B:35:0x00a1, B:37:0x00b1, B:40:0x00bf, B:42:0x00c6, B:44:0x00de, B:46:0x0114, B:48:0x0118, B:50:0x013c, B:51:0x0165, B:53:0x0169, B:54:0x0170, B:56:0x019b, B:58:0x01ae, B:60:0x01c3, B:62:0x01d7, B:64:0x01e4, B:66:0x01e9, B:68:0x020a, B:70:0x020e, B:72:0x0214, B:74:0x021a, B:80:0x0226, B:203:0x0561, B:205:0x0569, B:207:0x0575, B:210:0x0580, B:211:0x0583, B:213:0x058f, B:215:0x0595, B:216:0x05a4, B:218:0x05aa, B:219:0x05b9, B:221:0x05bf, B:223:0x05cf, B:224:0x05d6, B:226:0x05db, B:81:0x0232, B:83:0x0236, B:61:0x01ce, B:63:0x01dc, B:84:0x0240, B:88:0x0252, B:90:0x0256, B:92:0x025b, B:94:0x0261, B:99:0x026d, B:116:0x0295, B:118:0x029b, B:120:0x02b4, B:122:0x02ba, B:127:0x02ce, B:128:0x02e4, B:129:0x02e5, B:130:0x02e9, B:132:0x02ed, B:133:0x031f, B:135:0x0323, B:137:0x0330, B:138:0x0367, B:140:0x038e, B:142:0x03a0, B:144:0x03b0, B:150:0x03c0, B:152:0x03d9, B:154:0x03e0, B:156:0x03e6, B:165:0x03fa, B:167:0x040a, B:168:0x041c, B:173:0x042d, B:174:0x0434, B:175:0x0435, B:177:0x0442, B:179:0x0480, B:181:0x048f, B:183:0x0493, B:185:0x0497, B:186:0x04e8, B:188:0x04ee, B:189:0x050c, B:191:0x0516, B:193:0x0531, B:195:0x0535, B:196:0x0541, B:198:0x0549, B:200:0x054e, B:147:0x03ba, B:102:0x0278, B:106:0x0280, B:227:0x05e2, B:22:0x0074, B:24:0x007a, B:25:0x0081, B:27:0x0087), top: B:289:0x0054 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00a1 A[Catch: Exception -> 0x05e8, TryCatch #1 {Exception -> 0x05e8, blocks: (B:12:0x0054, B:14:0x0058, B:16:0x0062, B:18:0x0066, B:20:0x006c, B:30:0x0091, B:33:0x0099, B:35:0x00a1, B:37:0x00b1, B:40:0x00bf, B:42:0x00c6, B:44:0x00de, B:46:0x0114, B:48:0x0118, B:50:0x013c, B:51:0x0165, B:53:0x0169, B:54:0x0170, B:56:0x019b, B:58:0x01ae, B:60:0x01c3, B:62:0x01d7, B:64:0x01e4, B:66:0x01e9, B:68:0x020a, B:70:0x020e, B:72:0x0214, B:74:0x021a, B:80:0x0226, B:203:0x0561, B:205:0x0569, B:207:0x0575, B:210:0x0580, B:211:0x0583, B:213:0x058f, B:215:0x0595, B:216:0x05a4, B:218:0x05aa, B:219:0x05b9, B:221:0x05bf, B:223:0x05cf, B:224:0x05d6, B:226:0x05db, B:81:0x0232, B:83:0x0236, B:61:0x01ce, B:63:0x01dc, B:84:0x0240, B:88:0x0252, B:90:0x0256, B:92:0x025b, B:94:0x0261, B:99:0x026d, B:116:0x0295, B:118:0x029b, B:120:0x02b4, B:122:0x02ba, B:127:0x02ce, B:128:0x02e4, B:129:0x02e5, B:130:0x02e9, B:132:0x02ed, B:133:0x031f, B:135:0x0323, B:137:0x0330, B:138:0x0367, B:140:0x038e, B:142:0x03a0, B:144:0x03b0, B:150:0x03c0, B:152:0x03d9, B:154:0x03e0, B:156:0x03e6, B:165:0x03fa, B:167:0x040a, B:168:0x041c, B:173:0x042d, B:174:0x0434, B:175:0x0435, B:177:0x0442, B:179:0x0480, B:181:0x048f, B:183:0x0493, B:185:0x0497, B:186:0x04e8, B:188:0x04ee, B:189:0x050c, B:191:0x0516, B:193:0x0531, B:195:0x0535, B:196:0x0541, B:198:0x0549, B:200:0x054e, B:147:0x03ba, B:102:0x0278, B:106:0x0280, B:227:0x05e2, B:22:0x0074, B:24:0x007a, B:25:0x0081, B:27:0x0087), top: B:289:0x0054 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00de A[Catch: Exception -> 0x05e8, TryCatch #1 {Exception -> 0x05e8, blocks: (B:12:0x0054, B:14:0x0058, B:16:0x0062, B:18:0x0066, B:20:0x006c, B:30:0x0091, B:33:0x0099, B:35:0x00a1, B:37:0x00b1, B:40:0x00bf, B:42:0x00c6, B:44:0x00de, B:46:0x0114, B:48:0x0118, B:50:0x013c, B:51:0x0165, B:53:0x0169, B:54:0x0170, B:56:0x019b, B:58:0x01ae, B:60:0x01c3, B:62:0x01d7, B:64:0x01e4, B:66:0x01e9, B:68:0x020a, B:70:0x020e, B:72:0x0214, B:74:0x021a, B:80:0x0226, B:203:0x0561, B:205:0x0569, B:207:0x0575, B:210:0x0580, B:211:0x0583, B:213:0x058f, B:215:0x0595, B:216:0x05a4, B:218:0x05aa, B:219:0x05b9, B:221:0x05bf, B:223:0x05cf, B:224:0x05d6, B:226:0x05db, B:81:0x0232, B:83:0x0236, B:61:0x01ce, B:63:0x01dc, B:84:0x0240, B:88:0x0252, B:90:0x0256, B:92:0x025b, B:94:0x0261, B:99:0x026d, B:116:0x0295, B:118:0x029b, B:120:0x02b4, B:122:0x02ba, B:127:0x02ce, B:128:0x02e4, B:129:0x02e5, B:130:0x02e9, B:132:0x02ed, B:133:0x031f, B:135:0x0323, B:137:0x0330, B:138:0x0367, B:140:0x038e, B:142:0x03a0, B:144:0x03b0, B:150:0x03c0, B:152:0x03d9, B:154:0x03e0, B:156:0x03e6, B:165:0x03fa, B:167:0x040a, B:168:0x041c, B:173:0x042d, B:174:0x0434, B:175:0x0435, B:177:0x0442, B:179:0x0480, B:181:0x048f, B:183:0x0493, B:185:0x0497, B:186:0x04e8, B:188:0x04ee, B:189:0x050c, B:191:0x0516, B:193:0x0531, B:195:0x0535, B:196:0x0541, B:198:0x0549, B:200:0x054e, B:147:0x03ba, B:102:0x0278, B:106:0x0280, B:227:0x05e2, B:22:0x0074, B:24:0x007a, B:25:0x0081, B:27:0x0087), top: B:289:0x0054 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0112  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0118 A[Catch: Exception -> 0x05e8, TryCatch #1 {Exception -> 0x05e8, blocks: (B:12:0x0054, B:14:0x0058, B:16:0x0062, B:18:0x0066, B:20:0x006c, B:30:0x0091, B:33:0x0099, B:35:0x00a1, B:37:0x00b1, B:40:0x00bf, B:42:0x00c6, B:44:0x00de, B:46:0x0114, B:48:0x0118, B:50:0x013c, B:51:0x0165, B:53:0x0169, B:54:0x0170, B:56:0x019b, B:58:0x01ae, B:60:0x01c3, B:62:0x01d7, B:64:0x01e4, B:66:0x01e9, B:68:0x020a, B:70:0x020e, B:72:0x0214, B:74:0x021a, B:80:0x0226, B:203:0x0561, B:205:0x0569, B:207:0x0575, B:210:0x0580, B:211:0x0583, B:213:0x058f, B:215:0x0595, B:216:0x05a4, B:218:0x05aa, B:219:0x05b9, B:221:0x05bf, B:223:0x05cf, B:224:0x05d6, B:226:0x05db, B:81:0x0232, B:83:0x0236, B:61:0x01ce, B:63:0x01dc, B:84:0x0240, B:88:0x0252, B:90:0x0256, B:92:0x025b, B:94:0x0261, B:99:0x026d, B:116:0x0295, B:118:0x029b, B:120:0x02b4, B:122:0x02ba, B:127:0x02ce, B:128:0x02e4, B:129:0x02e5, B:130:0x02e9, B:132:0x02ed, B:133:0x031f, B:135:0x0323, B:137:0x0330, B:138:0x0367, B:140:0x038e, B:142:0x03a0, B:144:0x03b0, B:150:0x03c0, B:152:0x03d9, B:154:0x03e0, B:156:0x03e6, B:165:0x03fa, B:167:0x040a, B:168:0x041c, B:173:0x042d, B:174:0x0434, B:175:0x0435, B:177:0x0442, B:179:0x0480, B:181:0x048f, B:183:0x0493, B:185:0x0497, B:186:0x04e8, B:188:0x04ee, B:189:0x050c, B:191:0x0516, B:193:0x0531, B:195:0x0535, B:196:0x0541, B:198:0x0549, B:200:0x054e, B:147:0x03ba, B:102:0x0278, B:106:0x0280, B:227:0x05e2, B:22:0x0074, B:24:0x007a, B:25:0x0081, B:27:0x0087), top: B:289:0x0054 }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0226 A[Catch: Exception -> 0x05e8, TryCatch #1 {Exception -> 0x05e8, blocks: (B:12:0x0054, B:14:0x0058, B:16:0x0062, B:18:0x0066, B:20:0x006c, B:30:0x0091, B:33:0x0099, B:35:0x00a1, B:37:0x00b1, B:40:0x00bf, B:42:0x00c6, B:44:0x00de, B:46:0x0114, B:48:0x0118, B:50:0x013c, B:51:0x0165, B:53:0x0169, B:54:0x0170, B:56:0x019b, B:58:0x01ae, B:60:0x01c3, B:62:0x01d7, B:64:0x01e4, B:66:0x01e9, B:68:0x020a, B:70:0x020e, B:72:0x0214, B:74:0x021a, B:80:0x0226, B:203:0x0561, B:205:0x0569, B:207:0x0575, B:210:0x0580, B:211:0x0583, B:213:0x058f, B:215:0x0595, B:216:0x05a4, B:218:0x05aa, B:219:0x05b9, B:221:0x05bf, B:223:0x05cf, B:224:0x05d6, B:226:0x05db, B:81:0x0232, B:83:0x0236, B:61:0x01ce, B:63:0x01dc, B:84:0x0240, B:88:0x0252, B:90:0x0256, B:92:0x025b, B:94:0x0261, B:99:0x026d, B:116:0x0295, B:118:0x029b, B:120:0x02b4, B:122:0x02ba, B:127:0x02ce, B:128:0x02e4, B:129:0x02e5, B:130:0x02e9, B:132:0x02ed, B:133:0x031f, B:135:0x0323, B:137:0x0330, B:138:0x0367, B:140:0x038e, B:142:0x03a0, B:144:0x03b0, B:150:0x03c0, B:152:0x03d9, B:154:0x03e0, B:156:0x03e6, B:165:0x03fa, B:167:0x040a, B:168:0x041c, B:173:0x042d, B:174:0x0434, B:175:0x0435, B:177:0x0442, B:179:0x0480, B:181:0x048f, B:183:0x0493, B:185:0x0497, B:186:0x04e8, B:188:0x04ee, B:189:0x050c, B:191:0x0516, B:193:0x0531, B:195:0x0535, B:196:0x0541, B:198:0x0549, B:200:0x054e, B:147:0x03ba, B:102:0x0278, B:106:0x0280, B:227:0x05e2, B:22:0x0074, B:24:0x007a, B:25:0x0081, B:27:0x0087), top: B:289:0x0054 }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0232 A[Catch: Exception -> 0x05e8, TryCatch #1 {Exception -> 0x05e8, blocks: (B:12:0x0054, B:14:0x0058, B:16:0x0062, B:18:0x0066, B:20:0x006c, B:30:0x0091, B:33:0x0099, B:35:0x00a1, B:37:0x00b1, B:40:0x00bf, B:42:0x00c6, B:44:0x00de, B:46:0x0114, B:48:0x0118, B:50:0x013c, B:51:0x0165, B:53:0x0169, B:54:0x0170, B:56:0x019b, B:58:0x01ae, B:60:0x01c3, B:62:0x01d7, B:64:0x01e4, B:66:0x01e9, B:68:0x020a, B:70:0x020e, B:72:0x0214, B:74:0x021a, B:80:0x0226, B:203:0x0561, B:205:0x0569, B:207:0x0575, B:210:0x0580, B:211:0x0583, B:213:0x058f, B:215:0x0595, B:216:0x05a4, B:218:0x05aa, B:219:0x05b9, B:221:0x05bf, B:223:0x05cf, B:224:0x05d6, B:226:0x05db, B:81:0x0232, B:83:0x0236, B:61:0x01ce, B:63:0x01dc, B:84:0x0240, B:88:0x0252, B:90:0x0256, B:92:0x025b, B:94:0x0261, B:99:0x026d, B:116:0x0295, B:118:0x029b, B:120:0x02b4, B:122:0x02ba, B:127:0x02ce, B:128:0x02e4, B:129:0x02e5, B:130:0x02e9, B:132:0x02ed, B:133:0x031f, B:135:0x0323, B:137:0x0330, B:138:0x0367, B:140:0x038e, B:142:0x03a0, B:144:0x03b0, B:150:0x03c0, B:152:0x03d9, B:154:0x03e0, B:156:0x03e6, B:165:0x03fa, B:167:0x040a, B:168:0x041c, B:173:0x042d, B:174:0x0434, B:175:0x0435, B:177:0x0442, B:179:0x0480, B:181:0x048f, B:183:0x0493, B:185:0x0497, B:186:0x04e8, B:188:0x04ee, B:189:0x050c, B:191:0x0516, B:193:0x0531, B:195:0x0535, B:196:0x0541, B:198:0x0549, B:200:0x054e, B:147:0x03ba, B:102:0x0278, B:106:0x0280, B:227:0x05e2, B:22:0x0074, B:24:0x007a, B:25:0x0081, B:27:0x0087), top: B:289:0x0054 }] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0240 A[Catch: Exception -> 0x05e8, TryCatch #1 {Exception -> 0x05e8, blocks: (B:12:0x0054, B:14:0x0058, B:16:0x0062, B:18:0x0066, B:20:0x006c, B:30:0x0091, B:33:0x0099, B:35:0x00a1, B:37:0x00b1, B:40:0x00bf, B:42:0x00c6, B:44:0x00de, B:46:0x0114, B:48:0x0118, B:50:0x013c, B:51:0x0165, B:53:0x0169, B:54:0x0170, B:56:0x019b, B:58:0x01ae, B:60:0x01c3, B:62:0x01d7, B:64:0x01e4, B:66:0x01e9, B:68:0x020a, B:70:0x020e, B:72:0x0214, B:74:0x021a, B:80:0x0226, B:203:0x0561, B:205:0x0569, B:207:0x0575, B:210:0x0580, B:211:0x0583, B:213:0x058f, B:215:0x0595, B:216:0x05a4, B:218:0x05aa, B:219:0x05b9, B:221:0x05bf, B:223:0x05cf, B:224:0x05d6, B:226:0x05db, B:81:0x0232, B:83:0x0236, B:61:0x01ce, B:63:0x01dc, B:84:0x0240, B:88:0x0252, B:90:0x0256, B:92:0x025b, B:94:0x0261, B:99:0x026d, B:116:0x0295, B:118:0x029b, B:120:0x02b4, B:122:0x02ba, B:127:0x02ce, B:128:0x02e4, B:129:0x02e5, B:130:0x02e9, B:132:0x02ed, B:133:0x031f, B:135:0x0323, B:137:0x0330, B:138:0x0367, B:140:0x038e, B:142:0x03a0, B:144:0x03b0, B:150:0x03c0, B:152:0x03d9, B:154:0x03e0, B:156:0x03e6, B:165:0x03fa, B:167:0x040a, B:168:0x041c, B:173:0x042d, B:174:0x0434, B:175:0x0435, B:177:0x0442, B:179:0x0480, B:181:0x048f, B:183:0x0493, B:185:0x0497, B:186:0x04e8, B:188:0x04ee, B:189:0x050c, B:191:0x0516, B:193:0x0531, B:195:0x0535, B:196:0x0541, B:198:0x0549, B:200:0x054e, B:147:0x03ba, B:102:0x0278, B:106:0x0280, B:227:0x05e2, B:22:0x0074, B:24:0x007a, B:25:0x0081, B:27:0x0087), top: B:289:0x0054 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected boolean processRequestResult(RequestInfo requestInfo, TLRPC$TL_error tLRPC$TL_error) {
        NativeByteBuffer nativeByteBuffer;
        NativeByteBuffer nativeByteBuffer2;
        String str;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        RandomAccessFile randomAccessFile;
        boolean z5;
        boolean z6;
        long j;
        boolean z7;
        int i;
        int i2;
        long j2;
        Integer num;
        boolean z8 = false;
        if (this.state != 1) {
            if (BuildVars.DEBUG_VERSION && this.state == 3) {
                FileLog.e(new FileLog.IgnoreSentException("trying to write to finished file " + this.fileName + " offset " + requestInfo.offset + " " + this.totalBytesCount));
            }
            return false;
        }
        this.requestInfos.remove(requestInfo);
        if (tLRPC$TL_error == null) {
            try {
                if (this.notLoadedBytesRanges != null || this.downloadedBytes == requestInfo.offset) {
                    if (requestInfo.response != null) {
                        nativeByteBuffer2 = requestInfo.response.bytes;
                    } else if (requestInfo.responseWeb != null) {
                        nativeByteBuffer2 = requestInfo.responseWeb.bytes;
                    } else if (requestInfo.responseCdn != null) {
                        nativeByteBuffer2 = requestInfo.responseCdn.bytes;
                    } else {
                        nativeByteBuffer = null;
                        if (nativeByteBuffer != null && nativeByteBuffer.limit() != 0) {
                            int limit = nativeByteBuffer.limit();
                            if (this.isCdn) {
                                long j3 = requestInfo.offset;
                                int i3 = this.cdnChunkCheckSize;
                                long j4 = i3 * (j3 / i3);
                                HashMap<Long, TLRPC$TL_fileHash> hashMap = this.cdnHashes;
                                if ((hashMap != null ? hashMap.get(Long.valueOf(j4)) : null) == null) {
                                    delayRequestInfo(requestInfo);
                                    requestFileOffsets(j4);
                                    return true;
                                }
                            }
                            if (requestInfo.responseCdn == null) {
                                long j5 = requestInfo.offset / 16;
                                byte[] bArr = this.cdnIv;
                                str = " id = ";
                                bArr[15] = (byte) (j5 & 255);
                                bArr[14] = (byte) ((j5 >> 8) & 255);
                                bArr[13] = (byte) ((j5 >> 16) & 255);
                                bArr[12] = (byte) ((j5 >> 24) & 255);
                                Utilities.aesCtrDecryption(nativeByteBuffer.buffer, this.cdnKey, bArr, 0, nativeByteBuffer.limit());
                            } else {
                                str = " id = ";
                            }
                            if (!this.isPreloadVideoOperation) {
                                this.preloadStream.writeLong(requestInfo.offset);
                                long j6 = limit;
                                this.preloadStream.writeLong(j6);
                                this.preloadStreamFileOffset += 16;
                                this.preloadStream.getChannel().write(nativeByteBuffer.buffer);
                                if (BuildVars.DEBUG_VERSION) {
                                    FileLog.d("save preload file part " + this.cacheFilePreload + " offset " + requestInfo.offset + " size " + limit);
                                }
                                if (this.preloadedBytesRanges == null) {
                                    this.preloadedBytesRanges = new HashMap<>();
                                }
                                this.preloadedBytesRanges.put(Long.valueOf(requestInfo.offset), new PreloadRange(this.preloadStreamFileOffset, j6));
                                this.totalPreloadedBytes += limit;
                                this.preloadStreamFileOffset += limit;
                                if (this.moovFound == 0) {
                                    j2 = 0;
                                    long findNextPreloadDownloadOffset = findNextPreloadDownloadOffset(this.nextAtomOffset, requestInfo.offset, nativeByteBuffer);
                                    if (findNextPreloadDownloadOffset < 0) {
                                        findNextPreloadDownloadOffset *= -1;
                                        long j7 = this.nextPreloadDownloadOffset + this.currentDownloadChunkSize;
                                        this.nextPreloadDownloadOffset = j7;
                                        if (j7 < this.totalBytesCount / 2) {
                                            long j8 = 1048576 + findNextPreloadDownloadOffset;
                                            this.foundMoovSize = j8;
                                            this.preloadNotRequestedBytesCount = j8;
                                            this.moovFound = 1;
                                        } else {
                                            this.foundMoovSize = 2097152L;
                                            this.preloadNotRequestedBytesCount = 2097152L;
                                            this.moovFound = 2;
                                        }
                                        this.nextPreloadDownloadOffset = -1L;
                                    } else {
                                        this.nextPreloadDownloadOffset += this.currentDownloadChunkSize;
                                    }
                                    this.nextAtomOffset = findNextPreloadDownloadOffset;
                                } else {
                                    j2 = 0;
                                }
                                this.preloadStream.writeLong(this.foundMoovSize);
                                this.preloadStream.writeLong(this.nextPreloadDownloadOffset);
                                this.preloadStream.writeLong(this.nextAtomOffset);
                                this.preloadStreamFileOffset += 24;
                                long j9 = this.nextPreloadDownloadOffset;
                                if (j9 != j2 && ((this.moovFound == 0 || this.foundMoovSize >= j2) && this.totalPreloadedBytes <= preloadMaxBytes && j9 < this.totalBytesCount)) {
                                    z5 = false;
                                    if (!z5) {
                                        this.preloadStream.seek(j2);
                                        this.preloadStream.write(1);
                                    } else if (this.moovFound != 0) {
                                        this.foundMoovSize -= this.currentDownloadChunkSize;
                                    }
                                }
                                z5 = true;
                                if (!z5) {
                                }
                            } else {
                                long j10 = limit;
                                long j11 = this.downloadedBytes + j10;
                                this.downloadedBytes = j11;
                                long j12 = this.totalBytesCount;
                                if (j12 > 0) {
                                    if (j11 < j12 && ((i = this.preloadPrefixSize) <= 0 || j11 < i || !canFinishPreload() || !this.requestInfos.isEmpty())) {
                                        z7 = false;
                                        z2 = z7;
                                        if (this.downloadedBytes < this.totalBytesCount) {
                                            z8 = true;
                                        }
                                    }
                                    z7 = true;
                                    z2 = z7;
                                    if (this.downloadedBytes < this.totalBytesCount) {
                                    }
                                } else {
                                    int i4 = this.currentDownloadChunkSize;
                                    if (limit == i4 && ((j12 != j11 && j11 % i4 == 0) || (j12 > 0 && j12 > j11))) {
                                        z = false;
                                        z2 = z;
                                        z8 = false;
                                    }
                                    z = true;
                                    z2 = z;
                                    z8 = false;
                                }
                                boolean z9 = BuildVars.LOGS_ENABLED;
                                byte[] bArr2 = this.key;
                                if (bArr2 != null) {
                                    Utilities.aesIgeEncryption(nativeByteBuffer.buffer, bArr2, this.iv, false, true, 0, nativeByteBuffer.limit());
                                    if (z2 && this.bytesCountPadding != 0) {
                                        long limit2 = nativeByteBuffer.limit() - this.bytesCountPadding;
                                        if (BuildVars.DEBUG_VERSION && limit2 > 2147483647L) {
                                            throw new RuntimeException("Out of limit" + limit2);
                                        }
                                        nativeByteBuffer.limit((int) limit2);
                                    }
                                }
                                if (this.encryptFile) {
                                    long j13 = requestInfo.offset / 16;
                                    byte[] bArr3 = this.encryptIv;
                                    bArr3[15] = (byte) (j13 & 255);
                                    bArr3[14] = (byte) ((j13 >> 8) & 255);
                                    bArr3[13] = (byte) ((j13 >> 16) & 255);
                                    bArr3[12] = (byte) ((j13 >> 24) & 255);
                                    Utilities.aesCtrDecryption(nativeByteBuffer.buffer, this.encryptKey, bArr3, 0, nativeByteBuffer.limit());
                                }
                                if (this.notLoadedBytesRanges != null) {
                                    this.fileOutputStream.seek(requestInfo.offset);
                                    if (BuildVars.DEBUG_VERSION) {
                                        FileLog.d("save file part " + this.fileName + " offset=" + requestInfo.offset + " chunk_size=" + this.currentDownloadChunkSize + " isCdn=" + this.isCdn);
                                    }
                                }
                                this.fileOutputStream.getChannel().write(nativeByteBuffer.buffer);
                                addPart(this.notLoadedBytesRanges, requestInfo.offset, requestInfo.offset + j10, true);
                                boolean z10 = BuildVars.LOGS_ENABLED;
                                if (this.isCdn) {
                                    long j14 = requestInfo.offset / this.cdnChunkCheckSize;
                                    int size = this.notCheckedCdnRanges.size();
                                    int i5 = 0;
                                    while (true) {
                                        if (i5 >= size) {
                                            z6 = true;
                                            break;
                                        }
                                        Range range = this.notCheckedCdnRanges.get(i5);
                                        if (range.start <= j14 && j14 <= range.end) {
                                            z6 = false;
                                            break;
                                        }
                                        i5++;
                                    }
                                    if (!z6) {
                                        int i6 = this.cdnChunkCheckSize;
                                        long j15 = j14 * i6;
                                        long downloadedLengthFromOffsetInternal = getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, j15, i6);
                                        if (downloadedLengthFromOffsetInternal != 0) {
                                            if (downloadedLengthFromOffsetInternal != this.cdnChunkCheckSize) {
                                                long j16 = this.totalBytesCount;
                                                if (j16 > 0) {
                                                    j = j15;
                                                    if (downloadedLengthFromOffsetInternal != j16 - j) {
                                                    }
                                                } else {
                                                    j = j15;
                                                }
                                                if (j16 <= 0 && z2) {
                                                }
                                            } else {
                                                j = j15;
                                            }
                                            TLRPC$TL_fileHash tLRPC$TL_fileHash = this.cdnHashes.get(Long.valueOf(j));
                                            if (this.fileReadStream == null) {
                                                this.cdnCheckBytes = new byte[this.cdnChunkCheckSize];
                                                this.fileReadStream = new RandomAccessFile(this.cacheFileTemp, "r");
                                            }
                                            this.fileReadStream.seek(j);
                                            if (BuildVars.DEBUG_VERSION && downloadedLengthFromOffsetInternal > 2147483647L) {
                                                throw new RuntimeException("!!!");
                                            }
                                            this.fileReadStream.readFully(this.cdnCheckBytes, 0, (int) downloadedLengthFromOffsetInternal);
                                            if (this.encryptFile) {
                                                long j17 = j / 16;
                                                byte[] bArr4 = this.encryptIv;
                                                z3 = z2;
                                                z4 = z8;
                                                bArr4[15] = (byte) (j17 & 255);
                                                bArr4[14] = (byte) ((j17 >> 8) & 255);
                                                bArr4[13] = (byte) ((j17 >> 16) & 255);
                                                bArr4[12] = (byte) ((j17 >> 24) & 255);
                                                Utilities.aesCtrDecryptionByteArray(this.cdnCheckBytes, this.encryptKey, bArr4, 0, downloadedLengthFromOffsetInternal, 0);
                                            } else {
                                                z3 = z2;
                                                z4 = z8;
                                            }
                                            if (!Arrays.equals(Utilities.computeSHA256(this.cdnCheckBytes, 0, downloadedLengthFromOffsetInternal), tLRPC$TL_fileHash.hash)) {
                                                if (BuildVars.LOGS_ENABLED) {
                                                    if (this.location != null) {
                                                        FileLog.e("invalid cdn hash " + this.location + str + this.location.id + " local_id = " + this.location.local_id + " access_hash = " + this.location.access_hash + " volume_id = " + this.location.volume_id + " secret = " + this.location.secret);
                                                    } else {
                                                        String str2 = str;
                                                        if (this.webLocation != null) {
                                                            FileLog.e("invalid cdn hash  " + this.webLocation + str2 + this.fileName);
                                                        }
                                                    }
                                                }
                                                onFail(false, 0);
                                                this.cacheFileTemp.delete();
                                                return false;
                                            }
                                            this.cdnHashes.remove(Long.valueOf(j));
                                            addPart(this.notCheckedCdnRanges, j14, j14 + 1, false);
                                            randomAccessFile = this.fiv;
                                            if (randomAccessFile != null) {
                                                randomAccessFile.seek(0L);
                                                this.fiv.write(this.iv);
                                            }
                                            if (this.totalBytesCount > 0 && this.state == 1) {
                                                copyNotLoadedRanges();
                                                this.delegate.didChangedLoadProgress(this, this.downloadedBytes, this.totalBytesCount);
                                            }
                                            z5 = z3;
                                            z8 = z4;
                                        }
                                    }
                                }
                                z3 = z2;
                                z4 = z8;
                                randomAccessFile = this.fiv;
                                if (randomAccessFile != null) {
                                }
                                if (this.totalBytesCount > 0) {
                                    copyNotLoadedRanges();
                                    this.delegate.didChangedLoadProgress(this, this.downloadedBytes, this.totalBytesCount);
                                }
                                z5 = z3;
                                z8 = z4;
                            }
                            while (i2 < this.delayedRequestInfos.size()) {
                                RequestInfo requestInfo2 = this.delayedRequestInfos.get(i2);
                                i2 = (this.notLoadedBytesRanges == null && this.downloadedBytes != requestInfo2.offset) ? i2 + 1 : 0;
                                this.delayedRequestInfos.remove(i2);
                                if (!processRequestResult(requestInfo2, null)) {
                                    if (requestInfo2.response != null) {
                                        requestInfo2.response.disableFree = false;
                                        requestInfo2.response.freeResources();
                                    } else if (requestInfo2.responseWeb != null) {
                                        requestInfo2.responseWeb.disableFree = false;
                                        requestInfo2.responseWeb.freeResources();
                                    } else if (requestInfo2.responseCdn != null) {
                                        requestInfo2.responseCdn.disableFree = false;
                                        requestInfo2.responseCdn.freeResources();
                                    }
                                }
                                if (z5) {
                                    onFinishLoadingFile(true, 0, z8);
                                } else if (this.state != 4) {
                                    startDownloadRequest(requestInfo.connectionType);
                                }
                            }
                            if (z5) {
                            }
                        }
                        onFinishLoadingFile(true, 0, false);
                        return false;
                    }
                    nativeByteBuffer = nativeByteBuffer2;
                    if (nativeByteBuffer != null) {
                        int limit3 = nativeByteBuffer.limit();
                        if (this.isCdn) {
                        }
                        if (requestInfo.responseCdn == null) {
                        }
                        if (!this.isPreloadVideoOperation) {
                        }
                        while (i2 < this.delayedRequestInfos.size()) {
                        }
                        if (z5) {
                        }
                    }
                    onFinishLoadingFile(true, 0, false);
                    return false;
                }
                delayRequestInfo(requestInfo);
                return false;
            } catch (Exception e) {
                FileLog.e(e, (AndroidUtilities.isFilNotFoundException(e) || AndroidUtilities.isENOSPC(e)) ? false : true);
                if (AndroidUtilities.isENOSPC(e)) {
                    onFail(false, -1);
                } else if (AndroidUtilities.isEROFS(e)) {
                    SharedConfig.checkSdCard(this.cacheFileFinal);
                    onFail(true, -1);
                } else {
                    onFail(false, 0);
                }
            }
        } else if (tLRPC$TL_error.text.contains("LIMIT_INVALID") && !requestInfo.forceSmallChunk) {
            removePart(this.notRequestedBytesRanges, requestInfo.offset, requestInfo.offset + requestInfo.chunkSize);
            if (!this.forceSmallChunk) {
                this.forceSmallChunk = true;
                this.currentDownloadChunkSize = LiteMode.FLAG_CHAT_SCALE;
                this.currentMaxDownloadRequests = 4;
            }
            startDownloadRequest(requestInfo.connectionType);
        } else if (tLRPC$TL_error.text.contains("FILE_MIGRATE_")) {
            Scanner scanner = new Scanner(tLRPC$TL_error.text.replace("FILE_MIGRATE_", ""));
            scanner.useDelimiter("");
            try {
                num = Integer.valueOf(scanner.nextInt());
            } catch (Exception unused) {
                num = null;
            }
            if (num == null) {
                onFail(false, 0);
            } else {
                this.datacenterId = num.intValue();
                this.downloadedBytes = 0L;
                this.requestedBytesCount = 0L;
                startDownloadRequest(requestInfo.connectionType);
            }
        } else if (tLRPC$TL_error.text.contains("OFFSET_INVALID")) {
            if (this.downloadedBytes % this.currentDownloadChunkSize == 0) {
                try {
                    onFinishLoadingFile(true, 0, false);
                } catch (Exception e2) {
                    FileLog.e(e2);
                    onFail(false, 0);
                }
            } else {
                onFail(false, 0);
            }
        } else if (tLRPC$TL_error.text.contains("RETRY_LIMIT")) {
            onFail(false, 2);
        } else {
            if (BuildVars.LOGS_ENABLED) {
                TLRPC$InputFileLocation tLRPC$InputFileLocation = this.location;
                if (tLRPC$InputFileLocation != null) {
                    if (tLRPC$InputFileLocation instanceof TLRPC$TL_inputPeerPhotoFileLocation) {
                        FileLog.e(tLRPC$TL_error.text + " " + this.location + " peer_did = " + DialogObject.getPeerDialogId(((TLRPC$TL_inputPeerPhotoFileLocation) this.location).peer) + " peer_access_hash=" + ((TLRPC$TL_inputPeerPhotoFileLocation) this.location).peer.access_hash + " photo_id=" + ((TLRPC$TL_inputPeerPhotoFileLocation) this.location).photo_id + " big=" + ((TLRPC$TL_inputPeerPhotoFileLocation) this.location).big);
                    } else {
                        FileLog.e(tLRPC$TL_error.text + " " + this.location + " id = " + this.location.id + " local_id = " + this.location.local_id + " access_hash = " + this.location.access_hash + " volume_id = " + this.location.volume_id + " secret = " + this.location.secret);
                    }
                } else if (this.webLocation != null) {
                    FileLog.e(tLRPC$TL_error.text + " " + this.webLocation + " id = " + this.fileName);
                }
            }
            onFail(false, 0);
            return false;
        }
        return false;
    }

    private boolean canFinishPreload() {
        return this.isStory && this.priority < 3;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onFail(boolean z, final int i) {
        cleanup();
        this.state = i == 1 ? 4 : 2;
        if (this.delegate != null && BuildVars.LOGS_ENABLED) {
            long currentTimeMillis = this.startTime != 0 ? System.currentTimeMillis() - this.startTime : 0L;
            if (i == 1) {
                FileLog.d("cancel downloading file to " + this.cacheFileFinal + " time = " + currentTimeMillis + " dc = " + this.datacenterId + " size = " + AndroidUtilities.formatFileSize(this.totalBytesCount));
            } else {
                FileLog.d("failed downloading file to " + this.cacheFileFinal + " reason = " + i + " time = " + currentTimeMillis + " dc = " + this.datacenterId + " size = " + AndroidUtilities.formatFileSize(this.totalBytesCount));
            }
        }
        if (z) {
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoadOperation.this.lambda$onFail$17(i);
                }
            });
            return;
        }
        FileLoadOperationDelegate fileLoadOperationDelegate = this.delegate;
        if (fileLoadOperationDelegate != null) {
            fileLoadOperationDelegate.didFailedLoadingFile(this, i);
        }
        notifyStreamListeners();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFail$17(int i) {
        FileLoadOperationDelegate fileLoadOperationDelegate = this.delegate;
        if (fileLoadOperationDelegate != null) {
            fileLoadOperationDelegate.didFailedLoadingFile(this, i);
        }
        notifyStreamListeners();
    }

    private void clearOperaion(RequestInfo requestInfo, boolean z) {
        int[] iArr = new int[2];
        long j = Long.MAX_VALUE;
        int i = 0;
        while (i < this.requestInfos.size()) {
            RequestInfo requestInfo2 = this.requestInfos.get(i);
            long min = Math.min(requestInfo2.offset, j);
            if (this.isPreloadVideoOperation) {
                this.requestedPreloadedBytesRanges.remove(Long.valueOf(requestInfo2.offset));
            } else {
                removePart(this.notRequestedBytesRanges, requestInfo2.offset, requestInfo2.offset + requestInfo2.chunkSize);
            }
            if (requestInfo != requestInfo2 && requestInfo2.requestToken != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(requestInfo2.requestToken, true);
            }
            i++;
            j = min;
        }
        int i2 = 0;
        while (i2 < 2) {
            int i3 = i2 == 0 ? 2 : ConnectionsManager.ConnectionTypeDownload2;
            if (iArr[i2] > 1048576) {
                ConnectionsManager.getInstance(this.currentAccount).discardConnection(this.isCdn ? this.cdnDatacenterId : this.datacenterId, i3);
            }
            i2++;
        }
        this.requestInfos.clear();
        long j2 = j;
        for (int i4 = 0; i4 < this.delayedRequestInfos.size(); i4++) {
            RequestInfo requestInfo3 = this.delayedRequestInfos.get(i4);
            if (this.isPreloadVideoOperation) {
                this.requestedPreloadedBytesRanges.remove(Long.valueOf(requestInfo3.offset));
            } else {
                removePart(this.notRequestedBytesRanges, requestInfo3.offset, requestInfo3.offset + requestInfo3.chunkSize);
            }
            if (requestInfo3.response != null) {
                requestInfo3.response.disableFree = false;
                requestInfo3.response.freeResources();
            } else if (requestInfo3.responseWeb != null) {
                requestInfo3.responseWeb.disableFree = false;
                requestInfo3.responseWeb.freeResources();
            } else if (requestInfo3.responseCdn != null) {
                requestInfo3.responseCdn.disableFree = false;
                requestInfo3.responseCdn.freeResources();
            }
            j2 = Math.min(requestInfo3.offset, j2);
        }
        this.delayedRequestInfos.clear();
        this.requestsCount = 0;
        if (!z && this.isPreloadVideoOperation) {
            this.requestedBytesCount = this.totalPreloadedBytes;
        } else if (this.notLoadedBytesRanges == null) {
            this.downloadedBytes = j2;
            this.requestedBytesCount = j2;
        }
    }

    private void requestReference(RequestInfo requestInfo) {
        TLRPC$WebPage tLRPC$WebPage;
        if (this.requestingReference) {
            return;
        }
        clearOperaion(null, false);
        this.requestingReference = true;
        Object obj = this.parentObject;
        if (obj instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) obj;
            if (messageObject.getId() < 0 && (tLRPC$WebPage = messageObject.messageOwner.media.webpage) != null) {
                this.parentObject = tLRPC$WebPage;
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("debug_loading: " + this.cacheFileFinal.getName() + " file reference expired ");
        }
        FileRefController.getInstance(this.currentAccount).requestReference(this.parentObject, this.location, this, requestInfo);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00bc  */
    /* JADX WARN: Type inference failed for: r11v0 */
    /* JADX WARN: Type inference failed for: r11v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r11v11 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void startDownloadRequest(int i) {
        int i2;
        int max;
        int i3;
        int i4;
        long j;
        long j2;
        int i5;
        TLRPC$TL_upload_getFile tLRPC$TL_upload_getFile;
        long j3;
        HashMap<Long, PreloadRange> hashMap;
        PreloadRange preloadRange;
        ArrayList<Range> arrayList;
        boolean z;
        if (BuildVars.DEBUG_PRIVATE_VERSION && Utilities.stageQueue != null && Utilities.stageQueue.getHandler() != null && Thread.currentThread() != Utilities.stageQueue.getHandler().getLooper().getThread()) {
            throw new RuntimeException("Wrong thread!!!");
        }
        boolean z2 = BuildVars.LOGS_ENABLED;
        if (!this.paused && !this.reuploadingCdn) {
            int i6 = 1;
            if (this.state == 1 && !this.requestingReference) {
                long j4 = 0;
                if ((this.isStory || this.streamPriorityStartOffset != 0 || this.nextPartWasPreloaded || this.requestInfos.size() + this.delayedRequestInfos.size() < this.currentMaxDownloadRequests) && (!this.isPreloadVideoOperation || (this.requestedBytesCount <= 2097152 && (this.moovFound == 0 || this.requestInfos.size() <= 0)))) {
                    ?? r11 = 0;
                    if (this.isStory) {
                        max = Math.max(0, this.currentMaxDownloadRequests - this.requestInfos.size());
                    } else if (this.streamPriorityStartOffset != 0 || this.nextPartWasPreloaded || ((this.isPreloadVideoOperation && this.moovFound == 0) || this.totalBytesCount <= 0)) {
                        i2 = 1;
                        i3 = 0;
                        while (i3 < i2) {
                            int i7 = 2;
                            if (this.isPreloadVideoOperation) {
                                if (this.moovFound != 0 && this.preloadNotRequestedBytesCount <= j4) {
                                    boolean z3 = BuildVars.LOGS_ENABLED;
                                    return;
                                }
                                long j5 = this.nextPreloadDownloadOffset;
                                if (j5 == -1) {
                                    int i8 = (preloadMaxBytes / this.currentDownloadChunkSize) + 2;
                                    long j6 = j4;
                                    while (i8 != 0) {
                                        if (!this.requestedPreloadedBytesRanges.containsKey(Long.valueOf(j6))) {
                                            j5 = j6;
                                            z = true;
                                            break;
                                        }
                                        int i9 = this.currentDownloadChunkSize;
                                        j6 += i9;
                                        long j7 = this.totalBytesCount;
                                        if (j6 > j7) {
                                            break;
                                        }
                                        if (this.moovFound == i7 && j6 == i9 * 8) {
                                            j6 = ((j7 - 1048576) / i9) * i9;
                                        }
                                        i8--;
                                        i7 = 2;
                                    }
                                    j5 = j6;
                                    z = false;
                                    if (!z && this.requestInfos.isEmpty()) {
                                        onFinishLoadingFile(r11, r11, r11);
                                    }
                                }
                                if (this.requestedPreloadedBytesRanges == null) {
                                    this.requestedPreloadedBytesRanges = new HashMap<>();
                                }
                                this.requestedPreloadedBytesRanges.put(Long.valueOf(j5), Integer.valueOf(i6));
                                if (BuildVars.DEBUG_VERSION) {
                                    FileLog.d("start next preload from " + j5 + " size " + this.totalBytesCount + " for " + this.cacheFilePreload);
                                }
                                this.preloadNotRequestedBytesCount -= this.currentDownloadChunkSize;
                                j2 = j5;
                                i4 = i2;
                            } else {
                                ArrayList<Range> arrayList2 = this.notRequestedBytesRanges;
                                if (arrayList2 != null) {
                                    long j8 = this.streamPriorityStartOffset;
                                    if (j8 == j4) {
                                        j8 = this.streamStartOffset;
                                    }
                                    int size = arrayList2.size();
                                    long j9 = Long.MAX_VALUE;
                                    i4 = i2;
                                    int i10 = 0;
                                    long j10 = Long.MAX_VALUE;
                                    while (true) {
                                        if (i10 >= size) {
                                            j8 = j9;
                                            break;
                                        }
                                        Range range = this.notRequestedBytesRanges.get(i10);
                                        if (j8 != j4) {
                                            if (range.start <= j8 && range.end > j8) {
                                                j10 = Long.MAX_VALUE;
                                                break;
                                            } else if (j8 < range.start && range.start < j9) {
                                                j9 = range.start;
                                            }
                                        }
                                        j10 = Math.min(j10, range.start);
                                        i10++;
                                        j4 = 0;
                                    }
                                    if (j8 != Long.MAX_VALUE) {
                                        j = j8;
                                    } else if (j10 == Long.MAX_VALUE) {
                                        boolean z4 = BuildVars.LOGS_ENABLED;
                                        return;
                                    } else {
                                        j = j10;
                                    }
                                } else {
                                    i4 = i2;
                                    j = this.requestedBytesCount;
                                }
                                j2 = j;
                            }
                            int i11 = this.preloadPrefixSize;
                            if (i11 > 0 && j2 >= i11 && canFinishPreload()) {
                                boolean z5 = BuildVars.LOGS_ENABLED;
                                return;
                            }
                            long j11 = this.totalBytesCount;
                            if (j11 > 0 && j2 > 0 && j2 >= j11) {
                                boolean z6 = BuildVars.LOGS_ENABLED;
                                return;
                            }
                            if (!this.isPreloadVideoOperation && (arrayList = this.notRequestedBytesRanges) != null) {
                                addPart(arrayList, j2, j2 + this.currentDownloadChunkSize, false);
                                boolean z7 = BuildVars.LOGS_ENABLED;
                            }
                            long j12 = this.totalBytesCount;
                            boolean z8 = j12 <= 0 || i3 == i4 + (-1) || (j12 > 0 && ((long) this.currentDownloadChunkSize) + j2 >= j12);
                            if (i == -1) {
                                i5 = this.requestsCount % 2 == 0 ? 2 : ConnectionsManager.ConnectionTypeDownload2;
                            } else {
                                i5 = i;
                            }
                            int i12 = this.isForceRequest ? 32 : 0;
                            if (this.isCdn) {
                                TLRPC$TL_upload_getCdnFile tLRPC$TL_upload_getCdnFile = new TLRPC$TL_upload_getCdnFile();
                                tLRPC$TL_upload_getCdnFile.file_token = this.cdnToken;
                                tLRPC$TL_upload_getCdnFile.offset = j2;
                                tLRPC$TL_upload_getCdnFile.limit = this.currentDownloadChunkSize;
                                i12 |= 1;
                                tLRPC$TL_upload_getFile = tLRPC$TL_upload_getCdnFile;
                            } else if (this.webLocation != null) {
                                TLRPC$TL_upload_getWebFile tLRPC$TL_upload_getWebFile = new TLRPC$TL_upload_getWebFile();
                                tLRPC$TL_upload_getWebFile.location = this.webLocation;
                                tLRPC$TL_upload_getWebFile.offset = (int) j2;
                                tLRPC$TL_upload_getWebFile.limit = this.currentDownloadChunkSize;
                                tLRPC$TL_upload_getFile = tLRPC$TL_upload_getWebFile;
                            } else {
                                TLRPC$TL_upload_getFile tLRPC$TL_upload_getFile2 = new TLRPC$TL_upload_getFile();
                                tLRPC$TL_upload_getFile2.location = this.location;
                                tLRPC$TL_upload_getFile2.offset = j2;
                                tLRPC$TL_upload_getFile2.limit = this.currentDownloadChunkSize;
                                tLRPC$TL_upload_getFile2.cdn_supported = true;
                                tLRPC$TL_upload_getFile = tLRPC$TL_upload_getFile2;
                            }
                            int i13 = i12;
                            final TLRPC$TL_upload_getFile tLRPC$TL_upload_getFile3 = tLRPC$TL_upload_getFile;
                            this.requestedBytesCount += this.currentDownloadChunkSize;
                            final RequestInfo requestInfo = new RequestInfo();
                            this.requestInfos.add(requestInfo);
                            requestInfo.offset = j2;
                            requestInfo.chunkSize = this.currentDownloadChunkSize;
                            requestInfo.forceSmallChunk = this.forceSmallChunk;
                            requestInfo.connectionType = i5;
                            if (!this.isPreloadVideoOperation && this.supportsPreloading && this.preloadStream != null && (hashMap = this.preloadedBytesRanges) != null && (preloadRange = hashMap.get(Long.valueOf(requestInfo.offset))) != null) {
                                requestInfo.response = new TLRPC$TL_upload_file();
                                try {
                                    if (BuildVars.DEBUG_VERSION && preloadRange.length > 2147483647L) {
                                        throw new RuntimeException("cast long to integer");
                                        break;
                                    }
                                    NativeByteBuffer nativeByteBuffer = new NativeByteBuffer((int) preloadRange.length);
                                    this.preloadStream.seek(preloadRange.fileOffset);
                                    this.preloadStream.getChannel().read(nativeByteBuffer.buffer);
                                    try {
                                        nativeByteBuffer.buffer.position(0);
                                        requestInfo.response.bytes = nativeByteBuffer;
                                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda6
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                FileLoadOperation.this.lambda$startDownloadRequest$18(requestInfo);
                                            }
                                        });
                                        j3 = 0;
                                    } catch (Exception unused) {
                                    }
                                } catch (Exception unused2) {
                                }
                                i3++;
                                j4 = j3;
                                i2 = i4;
                                i6 = 1;
                                r11 = 0;
                            }
                            if (this.streamPriorityStartOffset != 0) {
                                if (BuildVars.DEBUG_VERSION) {
                                    FileLog.d("frame get offset = " + this.streamPriorityStartOffset);
                                }
                                j3 = 0;
                                this.streamPriorityStartOffset = 0L;
                                this.priorityRequestInfo = requestInfo;
                            } else {
                                j3 = 0;
                            }
                            TLRPC$InputFileLocation tLRPC$InputFileLocation = this.location;
                            if (!(tLRPC$InputFileLocation instanceof TLRPC$TL_inputPeerPhotoFileLocation) || ((TLRPC$TL_inputPeerPhotoFileLocation) tLRPC$InputFileLocation).photo_id != j3) {
                                requestInfo.forceSmallChunk = this.forceSmallChunk;
                                if (BuildVars.LOGS_ENABLED) {
                                    requestInfo.requestStartTime = System.currentTimeMillis();
                                }
                                final int i14 = this.isCdn ? this.cdnDatacenterId : this.datacenterId;
                                final int i15 = i5;
                                requestInfo.requestToken = ConnectionsManager.getInstance(this.currentAccount).sendRequestSync(tLRPC$TL_upload_getFile3, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda20
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        FileLoadOperation.this.lambda$startDownloadRequest$20(requestInfo, i14, i15, tLRPC$TL_upload_getFile3, tLObject, tLRPC$TL_error);
                                    }
                                }, null, null, i13, i14, i5, z8);
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("debug_loading: " + this.cacheFileFinal.getName() + " dc=" + i14 + " send reqId " + requestInfo.requestToken + " offset=" + requestInfo.offset + " conType=" + i5 + " priority=");
                                }
                                this.requestsCount++;
                            } else {
                                requestReference(requestInfo);
                            }
                            i3++;
                            j4 = j3;
                            i2 = i4;
                            i6 = 1;
                            r11 = 0;
                        }
                        return;
                    } else {
                        max = Math.max(0, this.currentMaxDownloadRequests - this.requestInfos.size());
                    }
                    i2 = max;
                    i3 = 0;
                    while (i3 < i2) {
                    }
                    return;
                }
            }
        }
        boolean z9 = BuildVars.LOGS_ENABLED;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadRequest$18(RequestInfo requestInfo) {
        processRequestResult(requestInfo, null);
        requestInfo.response.freeResources();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadRequest$20(final RequestInfo requestInfo, int i, final int i2, TLObject tLObject, TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error) {
        byte[] bArr;
        if (this.requestInfos.contains(requestInfo)) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("debug_loading: " + this.cacheFileFinal.getName() + " time=" + (System.currentTimeMillis() - requestInfo.requestStartTime) + " dcId=" + i + " cdn=" + this.isCdn + " conType=" + i2 + " reqId" + requestInfo.requestToken);
            }
            if (requestInfo == this.priorityRequestInfo) {
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("frame get request completed " + this.priorityRequestInfo.offset);
                }
                this.priorityRequestInfo = null;
            }
            if (tLRPC$TL_error != null) {
                if (tLRPC$TL_error.code == -2000) {
                    this.requestInfos.remove(requestInfo);
                    this.requestedBytesCount -= requestInfo.chunkSize;
                    removePart(this.notRequestedBytesRanges, requestInfo.offset, requestInfo.offset + requestInfo.chunkSize);
                    return;
                } else if (FileRefController.isFileRefError(tLRPC$TL_error.text)) {
                    requestReference(requestInfo);
                    return;
                } else if ((tLObject instanceof TLRPC$TL_upload_getCdnFile) && tLRPC$TL_error.text.equals("FILE_TOKEN_INVALID")) {
                    this.isCdn = false;
                    clearOperaion(requestInfo, false);
                    startDownloadRequest(i2);
                    return;
                }
            }
            if (tLObject2 instanceof TLRPC$TL_upload_fileCdnRedirect) {
                TLRPC$TL_upload_fileCdnRedirect tLRPC$TL_upload_fileCdnRedirect = (TLRPC$TL_upload_fileCdnRedirect) tLObject2;
                if (!tLRPC$TL_upload_fileCdnRedirect.file_hashes.isEmpty()) {
                    if (this.cdnHashes == null) {
                        this.cdnHashes = new HashMap<>();
                    }
                    for (int i3 = 0; i3 < tLRPC$TL_upload_fileCdnRedirect.file_hashes.size(); i3++) {
                        TLRPC$TL_fileHash tLRPC$TL_fileHash = tLRPC$TL_upload_fileCdnRedirect.file_hashes.get(i3);
                        this.cdnHashes.put(Long.valueOf(tLRPC$TL_fileHash.offset), tLRPC$TL_fileHash);
                    }
                }
                byte[] bArr2 = tLRPC$TL_upload_fileCdnRedirect.encryption_iv;
                if (bArr2 == null || (bArr = tLRPC$TL_upload_fileCdnRedirect.encryption_key) == null || bArr2.length != 16 || bArr.length != 32) {
                    TLRPC$TL_error tLRPC$TL_error2 = new TLRPC$TL_error();
                    tLRPC$TL_error2.text = "bad redirect response";
                    tLRPC$TL_error2.code = 400;
                    processRequestResult(requestInfo, tLRPC$TL_error2);
                    return;
                }
                this.isCdn = true;
                if (this.notCheckedCdnRanges == null) {
                    ArrayList<Range> arrayList = new ArrayList<>();
                    this.notCheckedCdnRanges = arrayList;
                    arrayList.add(new Range(0L, this.maxCdnParts));
                }
                this.cdnDatacenterId = tLRPC$TL_upload_fileCdnRedirect.dc_id;
                this.cdnIv = tLRPC$TL_upload_fileCdnRedirect.encryption_iv;
                this.cdnKey = tLRPC$TL_upload_fileCdnRedirect.encryption_key;
                this.cdnToken = tLRPC$TL_upload_fileCdnRedirect.file_token;
                clearOperaion(requestInfo, false);
                startDownloadRequest(i2);
            } else if (tLObject2 instanceof TLRPC$TL_upload_cdnFileReuploadNeeded) {
                if (this.reuploadingCdn) {
                    return;
                }
                clearOperaion(requestInfo, false);
                this.reuploadingCdn = true;
                TLRPC$TL_upload_reuploadCdnFile tLRPC$TL_upload_reuploadCdnFile = new TLRPC$TL_upload_reuploadCdnFile();
                tLRPC$TL_upload_reuploadCdnFile.file_token = this.cdnToken;
                tLRPC$TL_upload_reuploadCdnFile.request_token = ((TLRPC$TL_upload_cdnFileReuploadNeeded) tLObject2).request_token;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_upload_reuploadCdnFile, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda19
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject3, TLRPC$TL_error tLRPC$TL_error3) {
                        FileLoadOperation.this.lambda$startDownloadRequest$19(i2, requestInfo, tLObject3, tLRPC$TL_error3);
                    }
                }, null, null, 0, this.datacenterId, 1, true);
            } else {
                if (tLObject2 instanceof TLRPC$TL_upload_file) {
                    requestInfo.response = (TLRPC$TL_upload_file) tLObject2;
                } else if (tLObject2 instanceof TLRPC$TL_upload_webFile) {
                    requestInfo.responseWeb = (TLRPC$TL_upload_webFile) tLObject2;
                    if (this.totalBytesCount == 0 && requestInfo.responseWeb.size != 0) {
                        this.totalBytesCount = requestInfo.responseWeb.size;
                    }
                } else {
                    requestInfo.responseCdn = (TLRPC$TL_upload_cdnFile) tLObject2;
                }
                if (tLObject2 != null) {
                    int i4 = this.currentType;
                    if (i4 == 50331648) {
                        StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 3, tLObject2.getObjectSize() + 4);
                    } else if (i4 == 33554432) {
                        StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 2, tLObject2.getObjectSize() + 4);
                    } else if (i4 == 16777216) {
                        StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 4, tLObject2.getObjectSize() + 4);
                    } else if (i4 == 67108864) {
                        String str = this.ext;
                        if (str != null && (str.toLowerCase().endsWith("mp3") || this.ext.toLowerCase().endsWith("m4a"))) {
                            StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 7, tLObject2.getObjectSize() + 4);
                        } else {
                            StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 5, tLObject2.getObjectSize() + 4);
                        }
                    }
                }
                processRequestResult(requestInfo, tLRPC$TL_error);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadRequest$19(int i, RequestInfo requestInfo, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.reuploadingCdn = false;
        if (tLRPC$TL_error == null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            if (!tLRPC$Vector.objects.isEmpty()) {
                if (this.cdnHashes == null) {
                    this.cdnHashes = new HashMap<>();
                }
                for (int i2 = 0; i2 < tLRPC$Vector.objects.size(); i2++) {
                    TLRPC$TL_fileHash tLRPC$TL_fileHash = (TLRPC$TL_fileHash) tLRPC$Vector.objects.get(i2);
                    this.cdnHashes.put(Long.valueOf(tLRPC$TL_fileHash.offset), tLRPC$TL_fileHash);
                }
            }
            startDownloadRequest(i);
        } else if (tLRPC$TL_error.text.equals("FILE_TOKEN_INVALID") || tLRPC$TL_error.text.equals("REQUEST_TOKEN_INVALID")) {
            this.isCdn = false;
            clearOperaion(requestInfo, false);
            startDownloadRequest(i);
        } else {
            onFail(false, 0);
        }
    }

    public void setDelegate(FileLoadOperationDelegate fileLoadOperationDelegate) {
        this.delegate = fileLoadOperationDelegate;
    }

    public static long floorDiv(long j, long j2) {
        long j3 = j / j2;
        return ((j ^ j2) >= 0 || j2 * j3 == j) ? j3 : j3 - 1;
    }

    public boolean isFinished() {
        return this.state == 3;
    }
}
