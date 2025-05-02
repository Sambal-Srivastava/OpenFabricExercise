# Uncomment this to preserve the line number information for debugging stack traces.
#-keepattributes SourceFile, LineNumberTable

# If you keep the line number information, uncomment this to hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizations code/simplification/arithmetic,!code/simplification/cast,!field/*,!method/inlining/*
-optimizationpasses 5
-allowaccessmodification
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-assumenosideeffects class android.util.Log {
 public static *** d(...);
 public static *** i(...);
 public static *** v(...);
}

-keepattributes Signature
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-dontwarn sun.misc.Unsafe

# NativeMethods
-keepclassmembers class * {
    native <methods>;
}

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.stream.** { *; }



# AndroidComponents
-keepnames public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keepnames public class * extends android.app.Service
-keepnames public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keepnames public class * extends android.app.Fragment

# AndroidXComponents
-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }
-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# CustomViews
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Parceler
-keep class **$$Parcelable { *; }
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Serializable
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}



# Google
-keep public class com.google.** {*;}
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-dontwarn kotlin.reflect.jvm.internal.**
-keep class kotlin.reflect.jvm.internal.** { *; }
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

# Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

# Domain Model classes
-keep class com.app.fabricexercise.domain.model.MessagePayload { <fields>; }

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