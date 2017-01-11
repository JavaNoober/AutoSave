package com.xqand.helper;

import com.squareup.javapoet.ParameterizedTypeName;
import com.xqand.processor.HelperConfig;
import com.xqand.utils.TypeUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

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
			TypeName cacheClass =  ClassName.get(encloseElement.asType());
			MethodSpec.Builder saveMethodBuilder;
			MethodSpec.Builder recoverMethodBuilder;
			saveMethodBuilder = MethodSpec.methodBuilder("save")
					.addModifiers(Modifier.PUBLIC)
					.returns(void.class)
					.addParameter(TypeUtil.BUNDLE, "outState")
					.addParameter(cacheClass, "save")
					.addAnnotation(Override.class);

			recoverMethodBuilder = MethodSpec.methodBuilder("recover")
					.addModifiers(Modifier.PUBLIC)
					.returns(void.class)
					.addParameter(TypeUtil.BUNDLE, "savedInstanceState")
					.addParameter(cacheClass, "recover")
					.addAnnotation(Override.class)
					.beginControlFlow("if(savedInstanceState != null)");

			for (HelperSavedValues value : elementArrayList) {
				Name fieldName = value.getSimpleName();
				String fieldType = value.getFieldType().toString();
				String type = HelperConfig.getFieldType(fieldType);
				//only support public field
				if(!value.isPublic()){
					continue;
				}
				if(!type.equals("unKnow")){
					addMethodStatement(saveMethodBuilder,recoverMethodBuilder, type, fieldName);
				}else {
					if(value.isParcelable()){
						saveMethodBuilder.addStatement("outState.putParcelable($S,save.$N)", fieldName.toString().toUpperCase(),fieldName);
						recoverMethodBuilder.addStatement("recover.$N = savedInstanceState.getParcelable($S)",
								fieldName, fieldName.toString().toUpperCase());
					}else {
						saveMethodBuilder.addStatement("outState.putSerializable($S,save.$N)", fieldName.toString
								().toUpperCase(),fieldName);
						recoverMethodBuilder.addStatement("recover.$N = ($T)savedInstanceState.getSerializable($S)",
								fieldName, ClassName.get(value.getFieldType()),fieldName.toString().toUpperCase());
					}
				}
			}
			recoverMethodBuilder.endControlFlow();

			MethodSpec saveMethod = saveMethodBuilder.build();
			MethodSpec recoverMethod = recoverMethodBuilder.build();
			String className = encloseElement.getSimpleName().toString() + HelperConfig.HELP_CLASS;
			TypeSpec cacheClassTypeSpec = TypeSpec.classBuilder(className)
					.addModifiers(Modifier.PUBLIC)
					.addSuperinterface(ParameterizedTypeName.get(TypeUtil.IHELPER, cacheClass))
					.addMethod(saveMethod)
					.addMethod(recoverMethod)
					.build();

			JavaFile javaFile = JavaFile.builder(getPackageName(), cacheClassTypeSpec).build();
			return javaFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
		saveMethodBuilder.addStatement(String.format("outState.put%s($S,save.$N)",upperFirstWord(str)),
				fieldName.toString().toUpperCase(),fieldName);
	}

	private void addRecoverMethodStatement(MethodSpec.Builder recoverMethodBuilder, String str, Name fieldName){
		recoverMethodBuilder.addStatement(String.format("recover.$N = savedInstanceState.get%s($S)",upperFirstWord(str)),
				fieldName, fieldName.toString().toUpperCase());
	}

	private void addMethodStatement(MethodSpec.Builder saveMethodBuilder,MethodSpec.Builder recoverMethodBuilder, String str, Name fieldName){
		addSaveMethodStatement(saveMethodBuilder,str,fieldName);
		addRecoverMethodStatement(recoverMethodBuilder,str,fieldName);
	}


}
