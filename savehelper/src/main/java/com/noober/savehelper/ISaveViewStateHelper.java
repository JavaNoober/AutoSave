package com.noober.savehelper;


import android.os.Parcelable;
import android.util.SparseArray;

public interface ISaveViewStateHelper<T> {

	void save(T clazz, SparseArray<Parcelable> container);
	void recover(T clazz, SparseArray<Parcelable> container);
}
