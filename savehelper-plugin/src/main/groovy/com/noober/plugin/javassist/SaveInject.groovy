//package com.noober.plugin
//
//import javassist.ClassPool
//import javassist.CtClass
//import javassist.CtConstructor
//import org.gradle.api.Project
//
//class SaveInject {
//    private static String injectStr = "System.out.print(\"sssssssss\"); "
//
//    public static void inject(String path, String packageName, Project project, ClassPool classPool){
//        classPool.appendClassPath(path)
//
////        CtClass fragmentActivityCtClass = classPool.get("android.support.v4.app.FragmentActivity")
////        CtClass v4FragmentCtClass = classPool.get("android.support.v4.app.Fragment")
//        CtClass activityCtClass = classPool.get("android.app.Activity")
//        CtClass fragmentCtClass = classPool.get("android.app.Fragment")
//        CtClass viewCtClass = classPool.get("android.view.View")
//        File dir = new File(path)
//        if(dir.isDirectory()){
//            dir.eachFileRecurse {File file ->
//                String filePath = file.getAbsolutePath()
//                if(filePath.endsWith('.class')
//                && !filePath.contains('R$')
//                && !filePath.contains('R.class')
//                && !filePath.contains('BuildConfig.class')){
//                    project.logger.error(filePath)
//                    int index = filePath.indexOf(packageName)
//                    if(index != -1){
//                        int end = filePath.length() - ".class".length()
//                        String className = filePath.substring(index, end).replace('/', '.')
//                        CtClass ctClass = classPool.getCtClass(className)
//                        if(ctClass.isFrozen()){
//                            ctClass.defrost()
//                        }
//                        ctClass.getMethods()
//                        ctClass.writeFile(path)
//                        ctClass.detach()
//                    }
//                }
//            }
//        }
//    }
//
//}