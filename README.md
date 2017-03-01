# AutoSave
A framework can automatically generate OnSaveInstanceState code
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
        compile 'com.xiaoqi:xqauto-api:1.1.0'
        apt 'com.xiaoqi:xqauto-processor:1.1.0'
        compile 'com.xiaoqi:xqauto-savehelper:1.1.0'
    }
