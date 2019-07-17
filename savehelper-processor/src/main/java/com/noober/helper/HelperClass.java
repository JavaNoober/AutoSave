package com.noober.helper;

import com.noober.processor.HelperConfig;
import com.noober.utils.TypeUtil;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;


public class HelperClass {

    private TypeElement encloseElement;
    private Elements elementUtils;
    private ArrayList<HelperSavedValues> elementArrayList;
    private Messager messager;

    public HelperClass(TypeElement encloseElement, Elements elementUtils, Messager messager) {
        this.encloseElement = encloseElement;
        this.elementUtils = elementUtils;
        elementArrayList = new ArrayList<>();
        this.messager = messager;
    }

    public void addField(HelperSavedValues savedValues) {
        elementArrayList.add(savedValues);
    }

    public JavaFile generateCode() {
        try {
            //当前所在的class
            TypeName cacheClass = ClassName.get(encloseElement.asType());
            MethodSpec.Builder saveMethodBuilder = MethodSpec.methodBuilder("save")//设置方法名
                    .addModifiers(Modifier.PUBLIC)//设置public修饰字段
                    .returns(void.class)//设置方法返回类型
                    .addParameter(TypeUtil.BUNDLE, "outState")//添加save方法的outState参数
                    .addParameter(TypeUtil.PERSISTABLE_BUNDLE, "outPersistentState")//添加save方法的outPersistentState参数
                    .addParameter(cacheClass, "save")//添加save方法的save参数
                    .addAnnotation(Override.class)//添加Override注解
                    .beginControlFlow("if(outState != null)");//开始编写save方法内的内容，以if(outState != null)加上"{"开头


            MethodSpec.Builder recoverMethodBuilder = MethodSpec.methodBuilder("recover")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(TypeUtil.BUNDLE, "savedInstanceState")
                    .addParameter(TypeUtil.PERSISTABLE_BUNDLE, "persistentState")
                    .addParameter(cacheClass, "recover")
                    .addAnnotation(Override.class)
                    .beginControlFlow("if(savedInstanceState != null)");

            int efficientElement = 0;
            ArrayList<HelperSavedValues> persistentArrayList = new ArrayList<>();
            //遍历这个方法内需要保存的所有字段
            for (HelperSavedValues value : elementArrayList) {
                //only support public field
                if (value.isPrivate()) {
                    error("the modifier of the field must not be private, otherwise  it won't work", value.getEncloseElement());
                    continue;
                }
                if (value.isPersistable()) {
                    persistentArrayList.add(value);
                    continue;
                }
                Name fieldName = value.getSimpleName();//字段名称
                TypeMirror typeMirror = value.getFieldType();//字段类型
                String type = HelperConfig.getBundleFieldType(elementUtils, typeMirror);//获取类型的名称
                efficientElement++;
                if (!type.equals(HelperConfig.UNKONW)) {
                    if (type.equals("Serializable") || type.equals("ParcelableArray")) {
                        addMethodStatementForClassCast("outState", "savedInstanceState", saveMethodBuilder, recoverMethodBuilder, value, fieldName, type);
                    } else {
                        addMethodStatement("outState", "savedInstanceState", saveMethodBuilder, recoverMethodBuilder, type, fieldName);
                    }
                } else {
                    error("this field is not support yet", value.getEncloseElement());
                }
            }
            saveMethodBuilder.endControlFlow();//加上"}"结束该方法
            recoverMethodBuilder.endControlFlow();

            if (persistentArrayList.size() > 0) {
                saveMethodBuilder.beginControlFlow("if(outPersistentState != null)");
                recoverMethodBuilder.beginControlFlow("if(persistentState != null)");
                for (HelperSavedValues value : persistentArrayList) {
                    Name fieldName = value.getSimpleName();
                    TypeMirror typeMirror = value.getFieldType();
                    String type = HelperConfig.getPersistableBundleFieldType(elementUtils, typeMirror);
                    efficientElement++;
                    if (!type.equals(HelperConfig.UNKONW)) {
                        if (type.equals("Serializable") || type.equals("ParcelableArray")) {
                            addMethodStatementForClassCast("outPersistentState", "persistentState", saveMethodBuilder, recoverMethodBuilder, value, fieldName, type);
                        } else {
                            addMethodStatement("outPersistentState", "persistentState", saveMethodBuilder, recoverMethodBuilder, type, fieldName);
                        }
                    } else {
                        error("this field is not support yet", value.getEncloseElement());
                    }

                }
                saveMethodBuilder.endControlFlow();
                recoverMethodBuilder.endControlFlow();
            }


            if (efficientElement == 0) {
                return null;
            }

            //创建save方法的MethodSpec对象
            MethodSpec saveMethod = saveMethodBuilder.build();
            //创建recover方法的MethodSpec对象
            MethodSpec recoverMethod = recoverMethodBuilder.build();
            String className = encloseElement.getSimpleName().toString() + HelperConfig.HELP_CLASS;
            TypeSpec cacheClassTypeSpec = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)//增加class的修饰字段
                    .addSuperinterface(ParameterizedTypeName.get(TypeUtil.IHELPER, cacheClass))//class实现了一个范型接口
                    .addMethod(saveMethod)//添加save方法
                    .addMethod(recoverMethod)//添加recover方法
                    .build();//生成TypeSpec
            //通过TypeSpec获取javaFile对象，用于生成代码
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

    private String getCacheClassName() {
        return encloseElement.getSimpleName().toString() + "_Cache";
    }

    private String upperFirstWord(String str) {
        if (str != null) {
            char[] ch = str.toCharArray();
            if (ch[0] >= 'a' && ch[0] <= 'z') {
                ch[0] = (char) (ch[0] - 32);
            }
            return new String(ch);
        } else {
            return "";
        }
    }

    private void addSaveMethodStatement(String putParam, MethodSpec.Builder saveMethodBuilder, String type, Name fieldName) {
        saveMethodBuilder.addStatement(String.format("%s.put%s($S,save.$N)", putParam, upperFirstWord(type)),
                fieldName.toString().toUpperCase(), fieldName);
    }

    private void addRecoverMethodStatement(String getParam, MethodSpec.Builder recoverMethodBuilder, String type, Name fieldName) {
        recoverMethodBuilder.addStatement(String.format("recover.$N = %s.get%s($S)", getParam, upperFirstWord(type)),
                fieldName, fieldName.toString().toUpperCase());
    }

    private void addMethodStatement(String putParam, String getParam, MethodSpec.Builder saveMethodBuilder, MethodSpec.Builder recoverMethodBuilder, String type, Name fieldName) {
        addSaveMethodStatement(putParam, saveMethodBuilder, type, fieldName);
        addRecoverMethodStatement(getParam, recoverMethodBuilder, type, fieldName);
    }

    private void addMethodStatementForClassCast(String putParam, String getParam, MethodSpec.Builder saveMethodBuilder, MethodSpec.Builder recoverMethodBuilder, HelperSavedValues value, Name fieldName, String type) {
        saveMethodBuilder.addStatement(String.format("%s.put%s($S,save.$N)", putParam, type),
                fieldName.toString().toUpperCase(), fieldName);
        recoverMethodBuilder.addStatement(String.format("recover.$N = ($T)%s.get%s($S)", getParam, type),
                fieldName, ClassName.get(value.getFieldType()), fieldName.toString().toUpperCase());
    }

    private void error(String msg, Element element) {
        messager.printMessage(Diagnostic.Kind.ERROR, msg, element);
    }

    private void info(String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

}
