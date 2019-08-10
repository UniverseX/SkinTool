@echo off
set platform=%1%

set DIRNAME=%~dp0
set ROOT=%DIRNAME%..
set CONFIG_FILE=%ROOT%\skin_config_%platform%.txt

if not exist "%CONFIG_FILE%" call platformFail.bat CONFIG_FILE %CONFIG_FILE%

copy "%CONFIG_FILE%" "skin_config_D9.ini"

@rem read files config
set /a index=0
for /f "tokens=1,2 delims==" %%x in (skin_config_D9.ini) do (
 set /a index+=1
 set map[%%x]=%%y
 set revmap[%%~ny]=%%x
)

set THEME_PATH=%map[themepath]%

if not exist "%THEME_PATH%" call pathFail.bat themepath %THEME_PATH%

@rem copy pngs
set SRC=%THEME_PATH%
set DST=%ROOT%\ThemeSkin%platform%\app\src\main\res\drawable-land-mdpi\
set EXTRA_LOAD_PNG=%ROOT%\ThemeSkin%platform%\extra\home_player_load.png

@rem del old pngs
for /r "%DST%" %%f in (*.png) do (
 del "%%f"
)

@rem ????D9
if "%platform%" == "D9" (
@rem copy extra png
copy "%EXTRA_LOAD_PNG%" "%DST%"
)

if not exist "%SRC%" call pathFail.bat themepath "%SRC%"
if not exist "%DST%" call pathFail.bat dst_path "%DST%"

setlocal
set /a num_pngs=0
for /r "%SRC%" %%j in (*.png) do (
	copy "%%j" "%DST%"
	set /a num_pngs+=1
)
echo 共复制图片=%num_pngs%个
endlocal

@Rem map lenth --> echo index=%index%

@rem test map echo %revmap[icon_preview]%

@rem delete ignore files
set /a num_prevew=0
setlocal enabledelayedexpansion  
for /r "%DST%" %%f in (*.png) do (
	rem echo %%f
	set rev=!revmap[%%~nf]!
    echo dst=%%~nf, rev=!rev!
	if "!rev!" neq "" (
		del "%%f"
		echo ------delete------="%%f"
		set /a num_prevew+=1
	)
)
echo 删除预览文件=%num_prevew%个
endlocal

@rem config colors and dimens
call config_colors_dimens.bat %platform%


