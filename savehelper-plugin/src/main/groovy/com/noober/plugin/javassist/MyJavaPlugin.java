//package com.noober.plugin;
//
//import com.android.build.gradle.AppExtension;
//
//import org.gradle.api.Action;
//import org.gradle.api.Plugin;
//import org.gradle.api.Project;
//import org.gradle.api.Task;
//import org.gradle.api.logging.Logger;
//
///**
// * Created by xiaoqi on 2018/8/8
// */
//public class MyJavaPlugin implements Plugin<Project> {
//    @Override
//    public void apply(Project project) {
//        project.evaluationDependsOn("clean");
//        AppExtension android = project.getExtensions().findByType(AppExtension.class);
//        android.registerTransform(new SaveTransform(project));
//    }
//}
