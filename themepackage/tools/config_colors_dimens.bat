@echo off
set platform=%1

@rem java env 
SET JAVA_HOME=../jdk
SET CLASSPATH=%JAVA_HOME%/lib/tools.jar;%JAVA_HOME%/lib/dt.jar
SET Path=%JAVA_HOME%/jre/bin;%JAVA_HOME%/lib;%CLASSPATH%;

@rem param
set ROOT=%DIRNAME%..
set CONFIG_PATH=%ROOT%\skin_config_%platform%.txt
set VALUE_PATH=%ROOT%\ThemeSkin%platform%\app\src\main\res\values

if exist tmp.properties (
 echo clear tmp.properties
 del tmp.properties
)

if "%platform%" equ "" call platformFail.bat CONFIG_PATH "%CONFIG_PATH%"

java -jar config.jar %platform% "%CONFIG_PATH%" "%VALUE_PATH%"