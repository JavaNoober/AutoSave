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


    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        SaveHelper.save(this, container);
        super.dispatchSaveInstanceState(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
        SaveHelper.recover(this, container);
    }
}
