package com.recover.autosavesample;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaoqi on 2018/2/9
 */

public class Model implements Parcelable {

	String name;


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
	}

	public Model() {
	}

	protected Model(Parcel in) {
		this.name = in.readString();
	}

	public static final Parcelable.Creator<Model> CREATOR = new Parcelable.Creator<Model>() {
		@Override
		public Model createFromParcel(Parcel source) {
			return new Model(source);
		}

		@Override
		public Model[] newArray(int size) {
			return new Model[size];
		}
	};
}
