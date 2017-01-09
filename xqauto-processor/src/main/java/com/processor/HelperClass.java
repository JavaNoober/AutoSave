package com.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;


public class HelperClass {

	TypeElement encloseElement;
	Elements elementUtils;
	ArrayList<HelperSavedValues> elementArrayList;
	Messager messager;

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
			ClassName cacheClass =  ClassName.get(getPackageName(), getCacheClassName());
			MethodSpec.Builder saveMethodBuilder = MethodSpec.methodBuilder("save")
					.addModifiers(Modifier.PUBLIC)
					.returns(void.class)
					.addParameter(TypeUtil.BUNDLE, "outState")
					.addParameter(TypeName.OBJECT, "clazz")
					.addAnnotation(Override.class)
					.addStatement("$T cache = ($T)clazz",  cacheClass, cacheClass);
			for (HelperSavedValues value : elementArrayList) {
				if(value.getFieldType().toString().equals("java.lang.String")){
					saveMethodBuilder.addStatement("outState.putString($S,cache.$N)", value.getSimpleName().toString
							().toUpperCase(),value.getSimpleName());
				}else if(value.getFieldType().toString().equals("int")){
					saveMethodBuilder.addStatement("outState.putInt($S,cache.$N)", value.getSimpleName().toString
							().toUpperCase(),value.getSimpleName());
				}else if(value.getFieldType() instanceof Serializable){
					saveMethodBuilder.addStatement("outState.putSerializable(\"$S\",cache.$S)", value.getSimpleName().toString
							().toUpperCase(),value.getSimpleName());
				}
			}

			MethodSpec.Builder recoverMethodBuilder = MethodSpec.methodBuilder("recover")
					.addModifiers(Modifier.PUBLIC)
					.returns(void.class)
					.addParameter(TypeUtil.BUNDLE, "savedInstanceState")
					.addParameter(TypeUtil.ACTITVITY, "activity")
					.addAnnotation(Override.class)
					.addStatement("$T recoverActivity = ($T)activity",  ClassName.get(encloseElement.asType()), ClassName.get(encloseElement.asType()));
			for (HelperSavedValues value : elementArrayList) {
				if(value.getFieldType().toString().equals("java.lang.String")){
					saveMethodBuilder.addStatement("outState.putString($S,cache.$N)", value.getSimpleName().toString
							().toUpperCase(),value.getSimpleName());
				}else if(value.getFieldType().toString().equals("int")){
					saveMethodBuilder.addStatement("outState.putInt($S,cache.$N)", value.getSimpleName().toString
							().toUpperCase(),value.getSimpleName());
				}else if(value.getFieldType() instanceof Serializable){
					saveMethodBuilder.addStatement("outState.putSerializable(\"$S\",cache.$S)", value.getSimpleName().toString
							().toUpperCase(),value.getSimpleName());
				}
			}

//				if(typeName.equals("java.lang.String")){
//					saveMethodBuilder.addStatement("outState.putString(\"$S\",cache.$S)", fieldName.toUpperCase(),
//							fieldName);
//				}else if(typeName.equals("int")){
//					saveMethodBuilder.addStatement("outState.putInt(\"$S\",cache.$S)", fieldName.toUpperCase(),fieldName);
//				}else if(field.getType() instanceof Serializable){
//					saveMethodBuilder.addStatement("outState.putSerializable(\"$S\",cache.$S)", fieldName.toUpperCase(),fieldName);
//				}

//			Class cacheClass = Class.forName("com.xiaoqi.annotationdemo.MainActivity_Cache");
//			Field[] fields = cacheClass.getFields();
//			for(Field field : fields){
//				String typeName = field.getType().toString();
//				String fieldName = field.getName();
//				if(typeName.equals("java.lang.String")){
//					saveMethodBuilder.addStatement("outState.putString(\"$S\",cache.$S)", fieldName.toUpperCase(),
//							fieldName);
//				}else if(typeName.equals("int")){
//					saveMethodBuilder.addStatement("outState.putInt(\"$S\",cache.$S)", fieldName.toUpperCase(),fieldName);
//				}else if(field.getType() instanceof Serializable){
//					saveMethodBuilder.addStatement("outState.putSerializable(\"$S\",cache.$S)", fieldName.toUpperCase(),fieldName);
//				}
//			}
			MethodSpec saveMethod = saveMethodBuilder.build();
			MethodSpec recoverMethod = recoverMethodBuilder.build();
			String className = encloseElement.getSimpleName().toString() + "_SaveStateHelper2";
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
}
