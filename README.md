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
            url 'https://xqand.bintray.com/maven/' 
        }
    }
    
    dependencies {
        compile 'com.xqand:xqauto-api:1.1.3'
        apt 'com.xqand:xqauto-processor:1.1.3'
        compile 'com.xqand:xqauto-savehelper:1.1.3'
    }
