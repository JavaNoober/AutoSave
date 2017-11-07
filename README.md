# AutoSave
A framework can automatically generate OnSaveInstanceState code

使用方法介绍 ：http://blog.csdn.net/qq_25412055/article/details/54355935
##how to use:
     
    
    project gradle:
     dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
        ....
    }
    
    app gradle:
    apply plugin: 'android-apt'
    
    repositories {
        maven {
            url 'https://dl.bintray.com/xqandroid/maven/' 
        }
    }
    
    dependencies {
        compile 'com.xiaoqi:xqauto-api:1.1.1'
        apt 'com.xiaoqi:xqauto-processor:1.1.1'
        compile 'com.xiaoqi:xqauto-savehelper:1.1.1'
    }
