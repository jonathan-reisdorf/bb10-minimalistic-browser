-keep class org.xwalk.core.** {
  *;
}

-dontwarn org.chromium.**

-keep class org.chromium.** {
  *;
}

-keepattributes **

-keep class com.squareup.okhttp3.** {
  *;
}

-keepclassmembers class org.crossnode.bb10beol.NavigationJsInterface {
  public *;
}

-keepclassmembers class org.crossnode.bb10beol.TabsJsInterface {
  public *;
}
