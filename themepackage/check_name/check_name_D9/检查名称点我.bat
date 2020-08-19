@echo off
set bat_root=%~dp0

SET JAVA_HOME=%~dp0..\..\jdk
SET Classpath=%JAVA_HOME%\lib\tools.jar;%JAVA_HOME%\lib\dt.jar;
SET Path=%JAVA_HOME%\jre\bin;

set dist_path=%1%
set default_names_file=%bat_root%default_theme_files.txt

java -jar %bat_root%check_name.jar %default_names_file% %dist_path%