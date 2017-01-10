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
				if(value.getFieldType().toString().equals("java.lang.String")){
					saveMethodBuilder.addStatement("outState.putString($S,recoverActivity.$N)", fieldName.toString
							().toUpperCase(),fieldName);
					recoverMethodBuilder.addStatement("recoverActivity.$N = savedInstanceState.getString($S)",fieldName,
							fieldName.toString().toUpperCase());
				}else if(value.getFieldType().toString().equals("int")){
					saveMethodBuilder.addStatement("outState.putInt($S,recoverActivity.$N)", fieldName.toString
							().toUpperCase(),fieldName);
					recoverMethodBuilder.addStatement("recoverActivity.$N = savedInstanceState.getInt($S)",fieldName,
							fieldName.toString().toUpperCase());
				}else if(value.getFieldType() instanceof Serializable){
					saveMethodBuilder.addStatement("outState.putSerializable($S,recoverActivity.$S)", fieldName.toString
							().toUpperCase(),fieldName);
					recoverMethodBuilder.addStatement("recoverActivity.$N = savedInstanceState.getSerializable($S)",
							fieldName, fieldName.toString().toUpperCase());
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
}
