package com.noober.processor;


import com.noober.api.NeedSave;
import com.noober.helper.HelperClass;
import com.noober.helper.HelperSavedValues;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

//@AutoService(Processor.class)
public class AutoSaveProcessor extends AbstractProcessor{

	private Types typeUtils;
	//可以处理相关Element（包括ExecutableElement, PackageElement, TypeElement, TypeParameterElement, VariableElement）
	private Elements elementUtils;
	//用来创建新源、类或辅助文件的 Filer。
	private Filer filer;

	//返回用来报告错误、警报和其他通知的 Messager。
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

		//获取需要生成代码的类
		for (Element annotatedElement: roundEnvironment.getElementsAnnotatedWith(NeedSave.class)) {
			getHelperClass(annotatedElement);
		}

		//遍历生成java代码
		for(HelperClass helperClass : mHelperClassMap.values()){
			try {

				//获取helperClass，调用其方法直接生成java代码
				JavaFile javaFile = helperClass.generateCode();
				if(javaFile != null){
					javaFile.writeTo(filer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private HelperClass getHelperClass(Element element) {
		TypeElement encloseElement = (TypeElement) element.getEnclosingElement();
		//所在类的完整类名
		String fullClassName = encloseElement.getQualifiedName().toString();
		//通过所在类的类名获取HelperClass类，HelperClass是用于自动生成代码的对象
        HelperClass annotatedClass = mHelperClassMap.get(fullClassName);
		if (annotatedClass == null) {
			annotatedClass = new HelperClass(encloseElement, elementUtils, messager);
			mHelperClassMap.put(fullClassName, annotatedClass);
		}
		//HelperSavedValues是被@NeedSave标记的字段，然后把这些字段添加到对应的map中
		//也就是map的key为需要生成的类，value为生成这个类的方法对象，其中包括了所有的需要保存的元素
		HelperSavedValues values = new HelperSavedValues(element);
		annotatedClass.addField(values);//添加当前类中的 所被注解标记的元素
		return annotatedClass;
	}
}
