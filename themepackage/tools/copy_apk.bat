@rem platfrom is D9 or S5
set platform=%1%
@echo off

set ROOT=%DIRNAME%..
set CONFIG_FILE=%ROOT%\skin_config_%platform%.txt

copy "%CONFIG_FILE%" "skin_config_D9.ini"

@rem read files config
for /f "tokens=1,2 delims==" %%x in (skin_config_D9.ini) do (
 set map[%%x]=%%y
)
set SKIN=%map[skinname]%

echo Ƥ��������=%SKIN%

set apk_path=ThemeSkin%platform%\app\build\outputs\apk\app-release-unsigned.apk

echo apk_path=%apk_path%

if not exist "%apk_path%" call pathFail.bat apk_path %apk_path%

set skin_path=skins\%SKIN%

echo Ƥ��·��=%skin_path%

copy "%apk_path%" "%skin_path%"

echo ������

pause & exit


