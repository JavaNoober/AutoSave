//package com.noober.plugin
//
//import com.android.build.api.transform.Context
//import com.android.build.api.transform.DirectoryInput
//import com.android.build.api.transform.Format
//import com.android.build.api.transform.JarInput
//import com.android.build.api.transform.QualifiedContent
//import com.android.build.api.transform.Transform
//import com.android.build.api.transform.TransformException
//import com.android.build.api.transform.TransformInput
//import com.android.build.api.transform.TransformInvocation
//import com.android.build.api.transform.TransformOutputProvider
//import com.android.build.gradle.internal.pipeline.TransformManager
//import javassist.ClassPool
//import org.apache.commons.io.FileUtils
//import org.apache.commons.codec.digest.DigestUtils
//import org.gradle.api.Project
//
//public class SaveTransform extends Transform{
//    Project project
//
//    SaveTransform(Project project){
//        this.project = project
//    }
//
//
//    @Override
//    String getName() {
//        return "MyTrans"
//    }
//
//    @Override
//    Set<QualifiedContent.ContentType> getInputTypes() {
//        return TransformManager.CONTENT_CLASS
//    }
//
//    @Override
//    Set<? super QualifiedContent.Scope> getScopes() {
//        return TransformManager.SCOPE_FULL_PROJECT
//    }
//
//    @Override
//    boolean isIncremental() {
//        return false
//    }
//
////    @Override
////    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
//////        super.transform(transformInvocation)
////        transformInvocation.inputs.each { TransformInput input ->
////            input.directoryInputs.each {DirectoryInput directoryInput ->
////                SaveInject.inject(directoryInput.file.absolutePath, "com\\recover\\autosavesample")
////
////                def filePath = transformInvocation.outputProvider.getContentLocation(directoryInput.name,
////                        directoryInput.scopes, Format.DIRECTORY)
////                FileUtil.copy(directoryInput.file, filePath)
////            }
////
////            input.jarInputs.each {JarInput jarInput ->
////                def jarName = jarInput.name
////                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
////                if(jarName.endsWith(".jar")) {
////                    jarName = jarName.substring(0,jarName.length()-4)
////                }
////                //生成输出路径
////                def dest = outputProvider.getContentLocation(jarName+md5Name,
////                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
////                //将输入内容复制到输出
////                FileUtils.copyFile(jarInput.file, dest)
////            }
////        }
////    }
//
//    @Override
//    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
//        ClassPool classPool = ClassPool.getDefault()
//        classPool.appendClassPath(project.android.bootClasspath[0].toString())
//        inputs.each { TransformInput input ->
//            input.directoryInputs.each {DirectoryInput directoryInput ->
//                SaveInject.inject(directoryInput.file.absolutePath, "com/recover/autosavesample", project, classPool)
//
//                def filePath = outputProvider.getContentLocation(directoryInput.name,directoryInput.contentTypes,
//                        directoryInput.scopes, Format.DIRECTORY)
//                FileUtils.copyDirectory(directoryInput.file, filePath)
//            }
//
//            input.jarInputs.each {JarInput jarInput ->
//                def jarName = jarInput.name
//                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
//                if(jarName.endsWith(".jar")) {
//                    jarName = jarName.substring(0,jarName.length()-4)
//                }
//                //生成输出路径
//                def dest = outputProvider.getContentLocation(jarName+md5Name,
//                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
//                //将输入内容复制到输出
//                FileUtils.copyFile(jarInput.file, dest)
//            }
//        }
//    }
//}