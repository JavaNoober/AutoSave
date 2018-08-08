package com.noober.plugin

import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
import org.gradle.api.Project

class SaveInject {
    private static ClassPool classPool = ClassPool.getDefault()
    private static String injectStr = "System.out.print(\"sssssssss\"); "

    public static void inject(String path, String packageName, Project project){
        classPool.importPackage("android.util.*")
        classPool.appendClassPath(path)
        File dir = new File(path)
        if(dir.isDirectory()){
            dir.eachFileRecurse {File file ->
                String filePath = file.getAbsolutePath()
                if(filePath.endsWith('.class')
                && !filePath.contains('R$')
                && !filePath.contains('R.class')
                && !filePath.contains('BuildConfig.class')){
                    project.logger.error(filePath)
                    int index = filePath.indexOf(packageName)
                    if(index != -1){
                        int end = filePath.length() - ".class".length()
                        String className = filePath.substring(index, end).replace('/', '.')
                        CtClass ctClass = classPool.getCtClass(className)
                        if(ctClass.isFrozen()){
                            ctClass.defrost()
                        }
                        CtConstructor[] constructor = ctClass.getDeclaredConstructors()

                        if(constructor == null || constructor.length == 0){
                            CtConstructor ctConstructor = new CtConstructor(new CtClass[0], ctClass)
                            ctConstructor.addLocalVariable(injectStr, ctClass)
//                            ctClass.addConstructor(ctConstructor)
                        }else {
                            constructor[0].addLocalVariable(injectStr, ctClass)
                        }
                        ctClass.writeFile(path)
                        ctClass.detach()
                    }
                }
            }
        }
    }

}