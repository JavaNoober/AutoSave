package com.xqand.helper;

import com.xqand.processor.HelperConfig;
import com.xqand.utils.TypeUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;


public class HelperClass {

	private TypeElement encloseElement;
	private Elements elementUtils;
	private ArrayList<HelperSavedValues> elementArrayList;
	private Messager messager;

	public HelperClass(TypeElement encloseElement, Elements elementUtils, Messager messager){
		this.encloseElement = encloseElement;
		this.elementUtils = elementUtils;
		elementArrayList = new ArrayList<>();
		this.messager = messager;
	}

	public void addField(HelperSavedValues savedValues){
		elementArrayList.add(savedValues);
	}

	public JavaFile generateCode(){
		try {
//			ClassName cacheClass =  ClassName.get(getPackageName(), getCacheClassName());
			TypeName cacheClass =  ClassName.get(encloseElement.asType());
			MethodSpec.Builder saveMethodBuilder = MethodSpec.methodBuilder("save")
					.addModifiers(Modifier.PUBLIC)
					.returns(void.class)
					.addParameter(TypeUtil.BUNDLE, "outState")
					.addParameter(TypeUtil.ACTITVITY, "activity")
					.addAnnotation(Override.class)
					.addStatement("$T recoverActivity = ($T)activity",  cacheClass, cacheClass);

			MethodSpec.Builder recoverMethodBuilder = MethodSpec.methodBuilder("recover")
					.addModifiers(Modifier.PUBLIC)
					.returns(void.class)
					.addParameter(TypeUtil.BUNDLE, "savedInstanceState")
					.addParameter(TypeUtil.ACTITVITY, "activity")
					.addAnnotation(Override.class)
					.beginControlFlow("if(savedInstanceState != null)")
					.addStatement("$T recoverActivity = ($T)activity", cacheClass, cacheClass);

			for (HelperSavedValues value : elementArrayList) {
				Name fieldName = value.getSimpleName();
				String fieldType = value.getFieldType().toString();
				String type = getFieldType(fieldType);
				if(!type.equals("unKnow")){
					addMethodStatement(saveMethodBuilder,recoverMethodBuilder, type, fieldName);
				}else {
//					Class c = Class.forName(fieldType);
//					Class[] interfaces = c.getInterfaces();
//					for (Class inter : interfaces) {
//						saveMethodBuilder.addStatement("//$N", inter.getName());
//					}
					if( value.getFieldType() instanceof Serializable){
						saveMethodBuilder.addStatement("outState.putSerializable($S,recoverActivity.$S)", fieldName.toString
								().toUpperCase(),fieldName);
						recoverMethodBuilder.addStatement("recoverActivity.$N = savedInstanceState.getSerializable($S)",
								fieldName, fieldName.toString().toUpperCase());
					}else {
						saveMethodBuilder.addStatement("//$S", fieldType);
					}
				}
			}
			recoverMethodBuilder.endControlFlow();

			MethodSpec saveMethod = saveMethodBuilder.build();
			MethodSpec recoverMethod = recoverMethodBuilder.build();
			String className = encloseElement.getSimpleName().toString() + HelperConfig.HELP_CLASS;
			TypeSpec cacheClassTypeSpec = TypeSpec.classBuilder(className)
					.addModifiers(Modifier.PUBLIC)
					.addSuperinterface(TypeUtil.IHELPER)
					.addMethod(saveMethod)
					.addMethod(recoverMethod)
					.build();
			JavaFile javaFile = JavaFile.builder(getPackageName(), cacheClassTypeSpec).build();
			return javaFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getPackageName() {
		return elementUtils.getPackageOf(encloseElement).getQualifiedName().toString();
	}

	private String getCacheClassName(){
		return encloseElement.getSimpleName().toString() + "_Cache";
	}

	private String upperFirstWord(String str){
		if(str != null){
			char[] ch = str.toCharArray();
			if (ch[0] >= 'a' && ch[0] <= 'z') {
				ch[0] = (char) (ch[0] - 32);
			}
			return new String(ch);
		}else {
			return "";
		}
	}

	private void addSaveMethodStatement(MethodSpec.Builder saveMethodBuilder, String str, Name fieldName){
		saveMethodBuilder.addStatement(String.format("outState.put%s($S,recoverActivity.$N)",upperFirstWord(str)),
				fieldName.toString().toUpperCase(),fieldName);
	}

	private void addRecoverMethodStatement(MethodSpec.Builder recoverMethodBuilder, String str, Name fieldName){
		recoverMethodBuilder.addStatement(String.format("recoverActivity.$N = savedInstanceState.get%s($S)",upperFirstWord(str)),
				fieldName, fieldName.toString().toUpperCase());
	}

	private void addMethodStatement(MethodSpec.Builder saveMethodBuilder,MethodSpec.Builder recoverMethodBuilder, String str, Name fieldName){
		addSaveMethodStatement(saveMethodBuilder,str,fieldName);
		addRecoverMethodStatement(recoverMethodBuilder,str,fieldName);
	}

	private String getFieldType(String fieldType){
		String type;
		if(fieldType.equals("java.lang.String")){
			type = "String";
		}else if(fieldType.equals("int") || fieldType.equals("java.lang.Integer")){
			type = "Int";
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
		}else {
			type = "unKnow";
		}
		return type;
	}

}
