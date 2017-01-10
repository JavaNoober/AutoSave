package com.xqand.utils;

import com.squareup.javapoet.ClassName;

public class TypeUtil {
    public static final ClassName BUNDLE = ClassName.get("android.os", "Bundle");
    public static final ClassName IHELPER = ClassName.get("com.xqand.savehelper", "ISaveInstanceStateHelper");
    public static final ClassName ACTITVITY = ClassName.get("android.app", "Activity");
}
