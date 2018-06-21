package com.recover.autosavesample;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.noober.api.NeedSave;
import com.noober.savehelper.SaveHelper;

/**
 * Created by xiaoqi on 18/6/21
 */
public class CustomView extends View {

    @NeedSave
    int a;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Log.e("CustomView", "onSaveInstanceState");
        return super.onSaveInstanceState();
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {

        SaveHelper.save(this, container);
        a = 2;

        Bundle bundle = new Bundle();
        bundle.putInt("A", a);
        container.put(1, bundle);
        super.dispatchSaveInstanceState(container);
        Log.e("CustomView", "dispatchSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        Log.e("CustomView", "onRestoreInstanceState");
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
        SaveHelper.recover(this, container);
        Bundle bundle = (Bundle) container.get(1);
        a = bundle.getInt("A");
        Log.e("CustomView", "dispatchRestoreInstanceState");
    }
}
