@echo off
set DIR=%~dp0
set JAVA_EXE=java
"%JAVA_EXE%" -Dorg.gradle.appname=%DIR% -classpath "%DIR%gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
