package com.xiaoqi.autosavesample;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by xiaoqi on 2017/1/10.
 */

public class Example implements Parcelable{
	protected Example(Parcel in) {
	}

	public static final Creator<Example> CREATOR = new Creator<Example>() {
		@Override
		public Example createFromParcel(Parcel in) {
			return new Example(in);
		}

		@Override
		public Example[] newArray(int size) {
			return new Example[size];
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
