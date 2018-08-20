//package com.noober.plugin.javassist
//
//import com.android.build.gradle.AppExtension
//import org.gradle.api.Action
//import org.gradle.api.Plugin
//import org.gradle.api.Project
//import org.gradle.api.Task
//
//class MyPlugin implements Plugin<Project> {
//
//    @Override
//    void apply(Project project) {
//        def android = project.getExtensions().findByType(AppExtension);
//        android.registerTransform(new SaveTransform(project));
//    }
//}