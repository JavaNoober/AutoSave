
## JavaPoet常用类
类名 | 作用
---|---
TypeSpec | 用于生成类、接口、枚举对象的类
MethodSpec | 用于生成方法对象的类
ParameterSpec | 用于生成参数对象的类
AnnotationSpec | 用于生成注解对象的类
FieldSpec | 用于配置生成成员变量的类
ClassName | 通过包名和类名生成的对象，在JavaPoet中相当于为其指定Class
ParameterizedTypeName | 通过MainClass和IncludeClass生成包含泛型的Class
JavaFile | 控制生成的Java文件的输出的类

## JavaPoet的常用方法
### 设置修饰关键字
    addModifiers(Modifier... modifiers)

Modifier是一个枚举对象，枚举值为修饰关键字Public、Protected、Private、Static、Final等等。
所有在JavaPoet创建的对象都必须设置修饰符(包括方法、类、接口、枚举、参数、变量)。
### 设置注解对象
    addAnnotation（AnnotationSpec annotationSpec）
    addAnnotation（ClassName annotation）
    addAnnotation(Class<?> annotation)
    
该方法即为类或方法或参数设置注解。
### 设置注释
    addJavadoc（CodeBlock block）
    addJavadoc(String format, Object... args)
    
在编写类、方法、成员变量时，可以通过addJavadoc来设置注释，可以直接传入String对象，或者传入CodeBlock（代码块）。

### JavaPoet生成类、接口、枚举对象

在JavaPoet中生成类、接口、枚举，必须得通过TypeSpec生成，而classBuilder、interfaceBuilder、enumBuilder便是创建其关键的方法：

    创建类：
    TypeSpec.classBuilder("类名“) 
    TypeSpec.classBuilder(ClassName className)
    
    创建接口：
    TypeSpec.interfaceBuilder("接口名称")
    TypeSpec.interfaceBuilder(ClassName className)
    
    创建枚举：
    TypeSpec.enumBuilder("枚举名称")
    TypeSpec.enumBuilder(ClassName className)
    
### 继承、实现接口

    继承类：
    .superclass(ClassName className)
    
    实现接口
    .addSuperinterface(ClassName className)
    
### 继承存在泛型的父类
    ParameterizedTypeName get(ClassName rawType, TypeName... typeArguments)
    
### 添加方法
    addMethod(MethodSpec methodSpec)
通过配置MethodSpec对象，使用addMethod方法将其添加进TypeSpec中。

### 添加枚举
    addEnumConstan(String enumValue)
通过addEnumConstan方法添加枚举值，参数为枚举值名称。

### 生成成员变量

    builder(TypeName type, String name, Modifier... modifiers)
    
通过FieldSpec的build方法生成，传入TypeName(Class)、name（名称）、Modifier（修饰符），就可以生成一个基本的成员变量。通过addField增加即可。

### 创建对象

    initializer(String format, Object... args)

例如： 

    public Activity mActivity = new Activity;
实现方法：  

    ClassName activity = ClassName.get("android.app", "Activity");
    FieldSpec spec = FieldSpec.builder(activity, "mActivity")
                    .addModifiers(Modifier.PUBLIC)
                    .initializer("new $T", activity)
                    .build();

### 生成方法

#### 构造方法

    MethodSpec.constructorBuilder()

#### 普通方法

    MethodSpec.constructorBuilder()
    
#### 方法参数

    addParameter(ParameterSpec parameterSpec)

#### 返回值

    returns(TypeName returnType)

#### 抛出异常

    .addException(TypeName name)

#### 方法内容

    addCode()
    addStatement()
    
addStatement()方法时，你只需要专注于该段代码的内容，至于结尾的分号和换行它都会帮你做好。
而addCode（）添加的方法体内容就是一段无格式的代码片，需要开发者自己添加其格式。

#### 生成方法参数

    addParameter(ParameterSpec parameterSpec)
    
##### ParameterSpec
    
    ParameterSpec.builder(TypeName type, String name, Modifier... modifiers)


### 常用通配符

#### $T
$T 在JavaPoet代指的是TypeName，该模板主要将Class抽象出来，用传入的TypeName指向的Class来代替。

例如：

    Bundle bundle = new Bundle();

    ClassName bundle = ClassName.get("android.os", "Bundle");
    addStatement("$T bundle = new $T()",bundle)
    
#### $N

$N在JavaPoet中代指的是一个名称，例如调用的方法名称，变量名称，这一类存在意思的名称。

例如：

    data.toString();
    
    addStatement("data.$N()",toString)
    
#### $S

$S在JavaPoet中就和String.format中%s一样,字符串的模板,将指定的字符串替换到$S的地方，需要注意的是替换后的内容，默认自带了双引号，如果不需要双引号包裹，需要使用$L.

例如：

    return name
    
    .addStatement("return $S", “name”)
    
### 生成注解

#### 初始化AnnotationSpec

    AnnotationSpec.builder(ClassName type)
    
#### 设置属性

    addMember(String name, String format, Object... args)
    
使用addMember可以设置注解的属性值，name对应的就是属性名称，format的内容即属性体，同样方法体的格式化在这里也是适用的。

### 生成代码

    JavaFile.builder(String packageName, TypeSpec typeSpec)

JavaFile通过向build方法传入PackageName（Java文件的包名）、TypeSpec（生成的内容）生成。

#### 打印结果

    javaFile.writeTo(System.out)

生成的内容会输出到控制台中
    
#### 生成文件

    javaFile.writeTo(File file)
    
生成的内容会以java文件的方式，存放到你传入File文件的位置

