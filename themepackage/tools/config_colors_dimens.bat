@echo off

set product=%1%
set platform=%2%
set CONFIG_PATH=%3%
set VALUE_PATH=%4%

@rem java env 
SET JAVA_HOME=../jdk
SET CLASSPATH=%JAVA_HOME%/lib/tools.jar;%JAVA_HOME%/lib/dt.jar
@rem SET path=%path%;%JAVA_HOME%/jre/bin;%JAVA_HOME%/lib;%CLASSPATH%;

@rem param
set ROOT=%DIRNAME%..

@rem clear tmp.properties
if exist tmp.properties (
 del /q tmp.properties
)

if "%platform%" == "" call platformFail.bat CONFIG_PATH "%CONFIG_PATH%"

set product_=""
if "%product%" == "XUI" (
	set product_=%product%_
)
java -jar %product_%skin_config_reader.jar %product% %platform% "%CONFIG_PATH%" "%VALUE_PATH%"