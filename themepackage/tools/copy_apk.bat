@rem platfrom is D9 or S5
set platform=%1%
@echo off

set DIRNAME=%~dp0
set ROOT=%DIRNAME%..
set CONFIG_FILE=%ROOT%\skin_config_%platform%.txt
set ini_config_file=%DIRNAME%skin_config_%platform%.ini

if not exist "%ini_config_file%" ( copy "%CONFIG_FILE%" "%ini_config_file%" ) else ( echo ini_config_prepare_success )
echo ---------------

echo DIRNAME=%DIRNAME%

@rem read files config
for /f "tokens=1,2 delims==" %%x in (tools\skin_config_%platform%.ini) do (
 set map[%%x]=%%y
)
set SKIN=%map[skinname]%

echo 皮肤包名称=%SKIN%

set apk_path=ThemeSkin%platform%\app\build\outputs\apk\app-release-unsigned.apk

echo apk_path=%apk_path%

if not exist "%apk_path%" call pathFail.bat apk_path %apk_path%

set skin_path=skins\%SKIN%

echo 皮肤路径=%skin_path%

copy "%apk_path%" "%skin_path%"

echo 打包完成

pause & exit


