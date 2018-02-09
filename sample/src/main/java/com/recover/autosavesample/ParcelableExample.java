package com.recover.autosavesample;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaoqi on 2017/1/10.
 */

public class ParcelableExample implements Parcelable{
	protected ParcelableExample(Parcel in) {
	}

	public static final Creator<ParcelableExample> CREATOR = new Creator<ParcelableExample>() {
		@Override
		public ParcelableExample createFromParcel(Parcel in) {
			return new ParcelableExample(in);
		}

		@Override
		public ParcelableExample[] newArray(int size) {
			return new ParcelableExample[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}
}
