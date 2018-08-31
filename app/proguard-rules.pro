# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes Signature
-keepattributes EnclosingMethod


-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod
-repackageclasses ''
-allowaccessmodification
-printmapping map.txt

-optimizationpasses 7
-dontskipnonpubliclibraryclassmembers



-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-dontoptimize
-dontpreverify

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
native <methods>;
}

-keepclassmembers public class * extends android.view.View {
void set*(***);
*** get*();
}





-keepclassmembers class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
public static <fields>;
}
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,Annotation,EnclosingMethod,MethodParameters

-keep class **.R$* {
*;
}

-dontwarn android.support.**


-keep public class java.awt.**{ *; }
-dontwarn java.awt.**
-dontwarn junit.**
-dontwarn javax.microedition.khronos.opengles.GL10.**
-keep interface *
-keep class javax.jmdns.**{*;}
-keep public class java.time.**{ *; }
-keep public class com.alibaba.fastjson.**{ *;}
-keep class com.iflytek.**{*;}

-keep interface android.support.v7.**{*;}
-keep class android.support.**{*;}
-keep class * extends android.support.**{*;}
-keep class * extends android.app.*{*;}
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.os.IInterface
-keep public class * extends android.app.FragmentActivity
-keep public class * extends android.app.BaseActivity
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.appwidget.AppWidgetProvider
-keep public class * extends android.webkit.*{*;}
-keep public class * extends android.widget.*{*;}
-keep public class * extends android.view.View{*;}
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keepattributes Signature
-keep class * implements android.os.Parcelable{*;}
-keepclassmembers class * extends java.lang.Enum {
public static **[] values();
public static ** valueOf(java.lang.String);
**[] $VALUES;
}
-ignorewarning

-keepclasseswithmembernames class * {
    native <methods>;
}

-keep class * implements java.io.Serializable{*;}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.FragmentActivity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * {
public <init>(org.json.JSONObject);
}
-keep class org.json.**{*;}

#EventBus
-keep class de.greenrobot.event.**{*;}
-dontwarn de.greenrobot.event.**
-keepclassmembers class ** {
public void onEvent*(**);
}

#fastJson
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.alibaba.fastjson.**