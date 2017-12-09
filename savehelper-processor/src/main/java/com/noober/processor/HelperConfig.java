package com.noober.processor;

/**
 * Created by xiaoqi on 2017/1/10.
 */

public class HelperConfig {

	public final static String HELP_CLASS = "_SaveStateHelper";

	public static String getFieldType(String fieldType){
		String type;
		if(fieldType.equals("java.lang.String")){
			type = "String";
		}else if(fieldType.equals("int") || fieldType.equals("java.lang.Integer")){
			type = "Int";
		}else if(fieldType.equals("int[]")){
			type = "IntArray";
		}else if(fieldType.equals("short") || fieldType.equals("java.lang.Short")){
			type = "Short";
		}else if(fieldType.equals("double") || fieldType.equals("java.lang.Double")){
			type = "Double";
		}else if(fieldType.equals("float") || fieldType.equals("java.lang.Float")){
			type = "Float";
		}else if(fieldType.equals("boolean") || fieldType.equals("java.lang.Boolean")){
			type = "Boolean";
		}else if(fieldType.equals("char")){
			type = "Char";
		}else if(fieldType.equals("char[]")){
			type = "CharArray";
		}else if(fieldType.equals("android.os.Bundle")){
			type = "Bundle";
		}else {
			type = "unKnow";
		}
		return type;
	}

}
