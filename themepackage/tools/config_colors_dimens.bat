@echo off

set product=%1%
set platform=%2%
set CONFIG_PATH=%3%
set VALUE_PATH=%4%
set assets_path=%5%

@rem java env 
SET JAVA_HOME=../jdk
SET CLASSPATH=%JAVA_HOME%/lib/tools.jar;%JAVA_HOME%/lib/dt.jar
SET path=%path%;%JAVA_HOME%/jre/bin;%JAVA_HOME%/lib;%CLASSPATH%;

@rem param
set ROOT=%DIRNAME%..

@rem clear tmp.properties
if exist tmp.properties (
 del /q tmp.properties
)

@rem clear assets
rd /s /q %assets_path%
md %assets_path%

if "%platform%" == "" call platformFail.bat CONFIG_PATH "%CONFIG_PATH%"

if "%product%" == "XUI" (
	java -jar XUI_skin_config_reader.jar %platform% "%CONFIG_PATH%" "%VALUE_PATH%"
) else (
    java -jar skin_config_reader.jar %product% %platform% "%CONFIG_PATH%" "%VALUE_PATH%"
)