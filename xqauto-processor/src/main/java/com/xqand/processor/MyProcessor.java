package com.xqand.processor;


import com.google.auto.service.AutoService;
import com.xqand.helper.HelperClass;
import com.xqand.helper.HelperSavedValues;
import com.processor.NeedSave;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor{

	private Types typeUtils;
	private Elements elementUtils;
	private Filer filer;
	private Messager messager;


	//存储添加了注解的Activity
	private Map<String, HelperClass> mHelperClassMap = new HashMap<>();

	@Override
	public synchronized void init(ProcessingEnvironment processingEnvironment) {
		super.init(processingEnvironment);
		typeUtils = processingEnvironment.getTypeUtils();
		elementUtils = processingEnvironment.getElementUtils();
		filer = processingEnvironment.getFiler();
		messager = processingEnvironment.getMessager();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> set = new LinkedHashSet<>();
		set.add(NeedSave.class.getCanonicalName());
		return set;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
		for (Element annotatedElement: roundEnvironment.getElementsAnnotatedWith(NeedSave.class)) {
			getHelperClass(annotatedElement);
		}

		for(HelperClass helperClass : mHelperClassMap.values()){
			try {
				if(helperClass.generateCode() != null){
					helperClass.generateCode().writeTo(filer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private HelperClass getHelperClass(Element element) {
		TypeElement encloseElement = (TypeElement) element.getEnclosingElement();
		String fullClassName = encloseElement.getQualifiedName().toString();
		HelperClass annotatedClass = mHelperClassMap.get(fullClassName);
		if (annotatedClass == null) {
			annotatedClass = new HelperClass(encloseElement, elementUtils,messager);
			mHelperClassMap.put(fullClassName, annotatedClass);
		}
		HelperSavedValues values = new HelperSavedValues(element);
		annotatedClass.addField(values);
		return annotatedClass;
	}

	private void error(String msg, Object... args) {
		messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
	}

	private void info(String msg, Object... args) {
		messager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
	}
}
