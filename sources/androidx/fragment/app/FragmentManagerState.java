package androidx.fragment.app;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;

/* loaded from: classes.dex */
final class FragmentManagerState implements Parcelable {
    public static final Parcelable.Creator<FragmentManagerState> CREATOR = new Parcelable.Creator() { // from class: androidx.fragment.app.FragmentManagerState.1
        @Override // android.os.Parcelable.Creator
        public FragmentManagerState createFromParcel(Parcel parcel) {
            return new FragmentManagerState(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public FragmentManagerState[] newArray(int i) {
            return new FragmentManagerState[i];
        }
    };
    ArrayList mActive;
    ArrayList mAdded;
    BackStackState[] mBackStack;
    int mBackStackIndex;
    ArrayList mLaunchedFragments;
    String mPrimaryNavActiveWho;
    ArrayList mResultKeys;
    ArrayList mResults;

    public FragmentManagerState() {
        this.mPrimaryNavActiveWho = null;
        this.mResultKeys = new ArrayList();
        this.mResults = new ArrayList();
    }

    public FragmentManagerState(Parcel parcel) {
        this.mPrimaryNavActiveWho = null;
        this.mResultKeys = new ArrayList();
        this.mResults = new ArrayList();
        this.mActive = parcel.createTypedArrayList(FragmentState.CREATOR);
        this.mAdded = parcel.createStringArrayList();
        this.mBackStack = (BackStackState[]) parcel.createTypedArray(BackStackState.CREATOR);
        this.mBackStackIndex = parcel.readInt();
        this.mPrimaryNavActiveWho = parcel.readString();
        this.mResultKeys = parcel.createStringArrayList();
        this.mResults = parcel.createTypedArrayList(Bundle.CREATOR);
        this.mLaunchedFragments = parcel.createTypedArrayList(FragmentManager.LaunchedFragmentInfo.CREATOR);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.mActive);
        parcel.writeStringList(this.mAdded);
        parcel.writeTypedArray(this.mBackStack, i);
        parcel.writeInt(this.mBackStackIndex);
        parcel.writeString(this.mPrimaryNavActiveWho);
        parcel.writeStringList(this.mResultKeys);
        parcel.writeTypedList(this.mResults);
        parcel.writeTypedList(this.mLaunchedFragments);
    }
}
