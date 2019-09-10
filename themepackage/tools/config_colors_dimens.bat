@echo off
set platform=%1
set CONFIG_PATH=%2%
set VALUE_PATH=%3%

echo CONFIG_PATH=%CONFIG_PATH%
echo VALUE_PATH=%VALUE_PATH%

@rem java env 
SET JAVA_HOME=../jdk
SET CLASSPATH=%JAVA_HOME%/lib/tools.jar;%JAVA_HOME%/lib/dt.jar
SET Path=%JAVA_HOME%/jre/bin;%JAVA_HOME%/lib;%CLASSPATH%;

@rem param
set ROOT=%DIRNAME%..

@rem clear tmp.properties
if exist tmp.properties (
 del /q tmp.properties
)

if "%platform%" equ "" call platformFail.bat CONFIG_PATH "%CONFIG_PATH%"

java -jar skin_config_reader.jar %platform% "%CONFIG_PATH%" "%VALUE_PATH%"