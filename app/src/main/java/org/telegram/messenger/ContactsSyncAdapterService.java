package org.telegram.messenger;

import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
/* loaded from: classes4.dex */
public class ContactsSyncAdapterService extends Service {
    private static SyncAdapterImpl sSyncAdapter = null;

    /* loaded from: classes4.dex */
    public static class SyncAdapterImpl extends AbstractThreadedSyncAdapter {
        private Context mContext;

        public SyncAdapterImpl(Context context) {
            super(context, true);
            this.mContext = context;
        }

        @Override // android.content.AbstractThreadedSyncAdapter
        public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
            try {
                ContactsSyncAdapterService.performSync(this.mContext, account, extras, authority, provider, syncResult);
            } catch (OperationCanceledException e) {
                FileLog.e(e);
            }
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return getSyncAdapter().getSyncAdapterBinder();
    }

    private SyncAdapterImpl getSyncAdapter() {
        if (sSyncAdapter == null) {
            sSyncAdapter = new SyncAdapterImpl(this);
        }
        return sSyncAdapter;
    }

    public static void performSync(Context context, Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) throws OperationCanceledException {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("performSync: " + account.toString());
        }
    }
}
