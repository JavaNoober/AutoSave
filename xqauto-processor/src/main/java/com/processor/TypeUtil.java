package com.processor;

import com.squareup.javapoet.ClassName;

public class TypeUtil {
    public static final ClassName BUNDLE = ClassName.get("android.os.Bundle", "Bundle");
    public static final ClassName IHELPER = ClassName.get("com.example", "ISaveInstanceStateHelper");
    public static final ClassName ACTITVITY = ClassName.get("android.app", "Activity");
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
}
