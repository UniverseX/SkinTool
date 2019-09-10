@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Gradle startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%
set GRADLE_CLASSPATH_HOME=%APP_HOME%..\gradle3.3
@rem set GRADLE_CLASSPATH_HOME=%APP_HOME%..\gradle
set JAVA_HOME=%APP_HOME%..\jdk
set SDK_HOME=%APP_HOME%..\sdk
set PATH=%JAVA_HOME%\jre\bin;%JAVA_HOME%\lib;%CLASSPATH%;%GRADLE_HOME%\bin;

echo DIRNAME=%DIRNAME%
echo APP_HOME=%APP_HOME%
echo APP_BASE_NAME=%APP_BASE_NAME%


@rem Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%\bin\java.exe

if exist "%JAVA_EXE%" goto init

echo JAVA_EXE1=%JAVA_EXE%

if not exist "%JAVA_EXE%" SET JAVA_EXE=%JAVA_HOME%\jre\bin\java.exe

echo JAVA_EXE2=%JAVA_EXE%

if exist "%JAVA_EXE%" goto init

echo JAVA_EXE3=%JAVA_EXE%

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

echo OS=%OS%

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%GRADLE_CLASSPATH_HOME%\lib\gradle-launcher-3.3.jar
@rem set CLASSPATH=%GRADLE_CLASSPATH_HOME%\wrapper\gradle-wrapper.jar

if not exist "%CLASSPATH%" echo NOT_EXIST_CLASSPATH=%CLASSPATH%

@rem Execute Gradle
@rem "%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %CMD_LINE_ARGS%
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" org.gradle.launcher.GradleMain %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable GRADLE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%GRADLE_EXIT_CONSOLE%" (
@rem cd ..
exit 1
)
pause && exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

set bat_name=%~n0
set platform=%bat_name:~0,-8%
echo platform=%platform%
cd ..

@rem skin包打包完后的apk路径
set apk_path=ThemeSkin%platform%\app\build\outputs\apk\app-release-unsigned.apk
set skin_result_dir=skins\

@rem 拷贝apk文件到skins目录
call tools\copy_apk.bat %apk_path% %skin_result_dir%

:omega
