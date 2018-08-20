//package com.noober.plugin.javassist
//
//import javassist.ClassPool
//import javassist.CtClass
//import javassist.CtConstructor
//import javassist.CtMethod
//import org.gradle.api.Project
//
//class SaveInject {
//    //初始化类池
//    private final static ClassPool pool = ClassPool.getDefault();
//    private final static String javaClassPackage = "intermediates/classes/debug/"
//    private final static String kotlinClassPackage = "tmp/kotlin-classes/debug/"
//
//
//    public static void inject(String path,Project project) {
//        //将当前路径加入类池,不然找不到这个类
//        pool.appendClassPath(path)
//        //project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类
//        pool.appendClassPath(project.android.bootClasspath[0].toString())
//        //引入android.os.Bundle包，因为onCreate方法参数有Bundle
//        pool.importPackage("android.os.Bundle")
//
//        CtClass fragmentActivityCtClass
//        CtClass v4FragmentCtClass
//
//        try {
//            fragmentActivityCtClass = pool.get("android.support.v4.app.FragmentActivity")
//            v4FragmentCtClass = pool.get("android.support.v4.app.Fragment")
//        } catch (Throwable t) {
//            // It's ok
//        }
//
//        CtClass activityCtClass = pool.get("android.app.Activity")
//        CtClass fragmentCtClass = pool.get("android.app.Fragment")
//        CtClass viewCtClass = pool.get("android.view.View")
//
//        File dir = new File(path)
//        if (dir.isDirectory()) {
//            //遍历文件夹
//            dir.eachFileRecurse { File file ->
//                String filePath = file.absolutePath
//                println("filePath = " + filePath)
//                if(filePath.endsWith('.class')
//                && !filePath.contains('R$')
//                && !filePath.contains('R.class')
//                && !filePath.contains('BuildConfig.class')){
//                    String classPath
//                    if(filePath.contains(javaClassPackage)){
//                        classPath= filePath.substring(filePath.indexOf(javaClassPackage) + javaClassPackage.length(), filePath.length() - ".class".length()).replace("/", ".")
//                    }else if(filePath.contains(kotlinClassPackage)){
//                        classPath = filePath.substring(filePath.indexOf(kotlinClassPackage) + kotlinClassPackage.length(), filePath.length() - ".class".length()).replace("/", ".")
//                    }
//
//                    println("classPath = " + classPath)
//                    //获取MainActivity.class
//                    CtClass ctClass = pool.getCtClass(classPath)
//                    println("ctClass = " + ctClass)
//                    //解冻
//                    if (ctClass.isFrozen())
//                        ctClass.defrost()
//
//                    //获取到OnCreate方法
//                    if(ctClass.subclassOf(activityCtClass) || ctClass.subclassOf(fragmentCtClass) || ctClass.subclassOf(v4FragmentCtClass)){
//                        CtMethod ctMethod = ctClass.getDeclaredMethod("onCreate")
//
//                        println("方法名 = " + ctMethod)
//
//                        String insetBeforeStr = """ android.widget.Toast.makeText(this,"我是被插入的Toast代码~!!",android.widget.Toast.LENGTH_SHORT).show();
//                                                """
//                        //在方法开头插入代码
//                        ctMethod.insertBefore(insetBeforeStr)
//                        ctClass.writeFile(path)
//                    }
//
//                    ctClass.detach()//释放
//                }
//            }
//        }
//
//    }
//
//}