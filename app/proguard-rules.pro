# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\as_sdk\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-printmapping proguard.map
-overloadaggressively
-dontusemixedcaseclassnames
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-dontpreverify
-verbose
-ignorewarnings

-dontwarn org.apache.log4j.**

-keep class android.support.**{
    *;
}
-keep class de.mindpipe.android.logging.log4j.**{
    *;
}

-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.app.backup.BackupAgentHelper

-keep public class * extends android.preference.Preference

-keep class android.os.** {
    <fields>;
    <methods>;
}


-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keep class ** extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep class ** implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
# Keep - Applications. Keep all application classes, along with their 'main'
# methods.
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  *,* {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#glide mode的配置不混淆
-keep public class * implements com.dingqiqi.qijuproject.manager.GlideModuleManager

-keep class com.dingqiqi.qijuproject.mode.** {
    *;
}
-keep class com.dingqiqi.qijuproject.manager.GlideModuleManager{
    *;
}
#retrofit 不混淆
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-dontwarn rx.*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

