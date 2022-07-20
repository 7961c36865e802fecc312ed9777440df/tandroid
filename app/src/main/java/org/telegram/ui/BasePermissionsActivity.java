package org.telegram.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.camera.CameraController;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class BasePermissionsActivity extends Activity {
    protected int currentAccount = -1;

    public boolean checkPermissionsResult(int i, String[] strArr, int[] iArr) {
        String str;
        if (iArr == null) {
            iArr = new int[0];
        }
        if (strArr == null) {
            strArr = new String[0];
        }
        boolean z = iArr.length > 0 && iArr[0] == 0;
        if (i == 104) {
            if (z) {
                GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
                if (groupCallActivity != null) {
                    groupCallActivity.enableCamera();
                }
            } else {
                showPermissionErrorAlert(2131558493, LocaleController.getString("VoipNeedCameraPermission", 2131629149));
            }
        } else if (i == 4 || i == 151) {
            if (!z) {
                if (i == 151) {
                    str = LocaleController.getString("PermissionNoStorageAvatar", 2131627482);
                } else {
                    str = LocaleController.getString("PermissionStorageWithHint", 2131627485);
                }
                showPermissionErrorAlert(2131558495, str);
            } else {
                ImageLoader.getInstance().checkMediaPaths();
            }
        } else if (i == 5) {
            if (!z) {
                showPermissionErrorAlert(2131558494, LocaleController.getString("PermissionNoContactsSharing", 2131627476));
                return false;
            }
            ContactsController.getInstance(this.currentAccount).forceImportContacts();
        } else if (i == 3 || i == 150) {
            int min = Math.min(strArr.length, iArr.length);
            boolean z2 = true;
            boolean z3 = true;
            for (int i2 = 0; i2 < min; i2++) {
                if ("android.permission.RECORD_AUDIO".equals(strArr[i2])) {
                    z2 = iArr[i2] == 0;
                } else if ("android.permission.CAMERA".equals(strArr[i2])) {
                    z3 = iArr[i2] == 0;
                }
            }
            if (i == 150 && !(z2 && z3)) {
                showPermissionErrorAlert(2131558493, LocaleController.getString("PermissionNoCameraMicVideo", 2131627474));
            } else if (!z2) {
                showPermissionErrorAlert(2131558497, LocaleController.getString("PermissionNoAudioWithHint", 2131627472));
            } else if (!z3) {
                showPermissionErrorAlert(2131558493, LocaleController.getString("PermissionNoCameraWithHint", 2131627475));
            } else {
                if (SharedConfig.inappCamera) {
                    CameraController.getInstance().initCamera(null);
                }
                return false;
            }
        } else if (i == 18 || i == 19 || i == 20 || i == 22) {
            if (!z) {
                showPermissionErrorAlert(2131558493, LocaleController.getString("PermissionNoCameraWithHint", 2131627475));
            }
        } else if (i == 2) {
            NotificationCenter.getGlobalInstance().postNotificationName(z ? NotificationCenter.locationPermissionGranted : NotificationCenter.locationPermissionDenied, new Object[0]);
        }
        return true;
    }

    public AlertDialog createPermissionErrorAlert(int i, String str) {
        return new AlertDialog.Builder(this).setTopAnimation(i, 72, false, Theme.getColor("dialogTopBackground")).setMessage(AndroidUtilities.replaceTags(str)).setPositiveButton(LocaleController.getString("PermissionOpenSettings", 2131627483), new BasePermissionsActivity$$ExternalSyntheticLambda0(this)).setNegativeButton(LocaleController.getString("ContactsPermissionAlertNotNow", 2131625245), null).create();
    }

    public /* synthetic */ void lambda$createPermissionErrorAlert$0(DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private void showPermissionErrorAlert(int i, String str) {
        createPermissionErrorAlert(i, str).show();
    }
}