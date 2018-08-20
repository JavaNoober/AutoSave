package com.noober.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.compile.JavaCompile
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.testfixtures.ProjectBuilder

import java.nio.file.Paths


class SavePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.repositories {
            mavenLocal()
            jcenter()
        }

//        project.dependencies {
//            implementation 'org.aspectj:aspectjrt:1.8.13'
//        }
        final def autoSaveVersion = "3.0.6"
        if (project.plugins.hasPlugin('kotlin-android')) {
            project.dependencies {
                implementation 'org.aspectj:aspectjrt:1.8.13'
                implementation "com.noober:savehelper:${autoSaveVersion}"
                kapt "com.noober:processor:${autoSaveVersion}"
                implementation "com.noober:savehelper-api:${autoSaveVersion}"
            }
        } else {
            project.dependencies {
                implementation 'org.aspectj:aspectjrt:1.8.13'
                implementation "com.noober:savehelper:${autoSaveVersion}"
                annotationProcessor "com.noober:processor:${autoSaveVersion}"
                implementation "com.noober:savehelper-api:${autoSaveVersion}"
            }
        }
        final def log = project.logger
        final def variants
        if (project.plugins.hasPlugin('com.android.application')) {
            variants = project.android.applicationVariants
        } else {
            variants = project.android.libraryVariants
        }

        variants.all { variant ->
            JavaCompile javaCompile = variant.javaCompile
            javaCompile.doLast {
                String[] args = ["-showWeaveInfo",
                                 "-1.7",
                                 "-inpath", javaCompile.destinationDir.toString(),
                                 "-aspectpath", javaCompile.classpath.asPath,
                                 "-d", javaCompile.destinationDir.toString(),
                                 "-classpath", javaCompile.classpath.asPath,
                                 "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
                log.debug "ajc args: " + Arrays.toString(args)

                MessageHandler handler = new MessageHandler(true);
                new Main().run(args, handler);
                for (IMessage message : handler.getMessages(null, true)) {
                    switch (message.getKind()) {
                        case IMessage.ABORT:
                        case IMessage.ERROR:
                        case IMessage.FAIL:
                            log.error message.message, message.thrown
                            break
                        case IMessage.WARNING:
                            log.warn message.message, message.thrown
                            break
                        case IMessage.INFO:
                            log.info message.message, message.thrown
                            break
                        case IMessage.DEBUG:
                            log.debug message.message, message.thrown
                            break
                    }
                }
            }
        }

    }
}