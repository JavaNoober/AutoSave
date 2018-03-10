package com.noober.processor;

import com.noober.helper.HelperSavedValues;
import com.noober.utils.FieldConstant;

import java.lang.reflect.Field;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Created by xiaoqi on 2017/1/10
 */

public class HelperConfig {

	public final static String HELP_CLASS = "_SaveStateHelper";

	private final static String PARCELABLE = "Parcelable";

	private final static String SERIALIZABLE = "Serializable";

	public static String getFieldType(Elements elementUtils, TypeMirror typeMirror) throws Exception {
		String fieldType = typeMirror.toString();
		String type = "unKnow";
		if (fieldType.equals(FieldConstant.STRING)) {
			type = "String";
		} else if (fieldType.equals(FieldConstant.STRING + "[]")) {
			type = "StringArray";
		} else if (fieldType.equals("int") || fieldType.equals(FieldConstant.INTEGER)) {
			type = "Int";
		} else if (fieldType.equals("int[]")) {
			type = "IntArray";
		} else if (fieldType.equals("short") || fieldType.equals(FieldConstant.SHORT)) {
			type = "Short";
		}else if (fieldType.equals("short[]")) {
			type = "ShortArray";
		} else if (fieldType.equals("double") || fieldType.equals(FieldConstant.DOUBLE)) {
			type = "Double";
		} else if (fieldType.equals("double[]")) {
			type = "DoubleArray";
		} else if (fieldType.equals("float") || fieldType.equals(FieldConstant.FLOAT)) {
			type = "Float";
		} else if (fieldType.equals("float[]")) {
			type = "FloatArray";
		} else if (fieldType.equals("boolean") || fieldType.equals(FieldConstant.BOOLEAN)) {
			type = "Boolean";
		} else if (fieldType.equals("boolean[]")) {
			type = "BooleanArray";
		} else if (fieldType.equals("long") || fieldType.equals(FieldConstant.LONG)) {
			type = "Long";
		} else if (fieldType.equals("long[]")) {
			type = "LongArray";
		}else if (fieldType.equals("char")) {
			type = "Char";
		} else if (fieldType.equals("char[]")) {
			type = "CharArray";
		} else if (fieldType.equals("byte[]")) {
			type = "ByteArray";
		} else if (fieldType.equals(FieldConstant.SIZE)) {
			type = "Size";
		} else if (fieldType.equals(FieldConstant.SIZEF)) {
			type = "SizeF";
		} else if (fieldType.equals(FieldConstant.BUNDLE)) {
			type = "Bundle";
		} else {
			Class cla = typeMirror.getClass();
			Field outField = cla.getField("tsym");
			outField.setAccessible(true);

			String outFieldName = outField.get(typeMirror).toString();

			if (outFieldName.equals(FieldConstant.ARRAYLIST)) {
				Field innerField = cla.getField("typarams_field");
				innerField.setAccessible(true);
				List innerFieldList = (List) innerField.get(typeMirror);
				String innerFieldName = "";
				if (innerFieldList.size() > 0) {
					innerFieldName = innerFieldList.get(0).toString();
				}
				if (innerFieldName.equals(FieldConstant.STRING)) {
					type = "StringArrayList";
				} else if (innerFieldName.equals(FieldConstant.CHARSEQUENCE)) {
					type = "CharSequenceArrayList";
				} else if (innerFieldName.equals(FieldConstant.INTEGER)) {
					type = "IntegerArrayList";
				} else {
					TypeElement typeElement = elementUtils.getTypeElement(innerFieldName);
					List<TypeMirror> typeElementInterfaces = (List<TypeMirror>) typeElement.getInterfaces();
					if (typeElementInterfaces.size() > 0) {
						for (TypeMirror tm : typeElementInterfaces) {
							if (tm.toString().equals(FieldConstant.PARCELABLE)) {
								type = "ParcelableArrayList";
								break;
							}else {
								type = SERIALIZABLE;
							}
						}
					}
				}
			} else if (outFieldName.equals(FieldConstant.ARRAY)) {
				Field innerField = cla.getField("elemtype");
				innerField.setAccessible(true);
				String innerFieldName = innerField.get(typeMirror).toString();
				String fieldInterface = getFieldInterface(elementUtils, innerFieldName);
				if (fieldInterface.equals(PARCELABLE)) {
					type = "ParcelableArray";
				}
			} else if (outFieldName.equals(FieldConstant.SPARSEARRAY)) {
				Field innerField = cla.getField("typarams_field");
				innerField.setAccessible(true);
				List innerFieldList = (List) innerField.get(typeMirror);
				String innerFieldName = "";
				if (innerFieldList.size() > 0) {
					innerFieldName = innerFieldList.get(0).toString();
				}
				String fieldInterface = getFieldInterface(elementUtils, innerFieldName);
				if (fieldInterface.equals(PARCELABLE)) {
					type = "SparseParcelableArray";
				}
			} else {
				type = getFieldInterface(elementUtils, outFieldName);
			}
		}
		return type;
	}

	private static String getFieldInterface(Elements elementUtils, String outFieldName) {
		String type = "unKnow";
		TypeElement typeElement = elementUtils.getTypeElement(outFieldName);
		List<TypeMirror> typeElementInterfaces = (List<TypeMirror>) typeElement.getInterfaces();
		if (typeElementInterfaces.size() > 0) {
			for (TypeMirror tm : typeElementInterfaces) {
				if (tm.toString().equals(FieldConstant.PARCELABLE)) {
					type = PARCELABLE;
					break;
				} else if (tm.toString().equals(FieldConstant.SERIALIZABLE)) {
					type = SERIALIZABLE;
					break;
				}
			}
		}
		if(type.equals("unKnow")){
			TypeMirror typeMirror = typeElement.getSuperclass();
			if(typeMirror.toString().startsWith("java.lang.Enum<")){
				type = SERIALIZABLE;
			}else if(!typeMirror.toString().equals(Object.class.getCanonicalName())){
				type = getFieldInterface(elementUtils, typeMirror.toString());
			}
		}
		return type;
	}

}
