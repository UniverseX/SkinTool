@echo off
set platform=%1%

set DIRNAME=%~dp0
set ROOT=%DIRNAME%..

@rem clear old version build files
echo 删除旧的ini文件
del /q "%ROOT%\*.ini"

@rem prepare ini_files
set CONFIG_FILE=%ROOT%\skin_config_%platform%.txt
if not exist "%CONFIG_FILE%" call platformFail.bat CONFIG_FILE %CONFIG_FILE%

set ini_config_file=%DIRNAME%skin_config_%platform%.ini

copy "%CONFIG_FILE%" "%ini_config_file%"

@rem echo ini_config_file=%ini_config_file%

@rem read files config
set /a index=0
for /f "tokens=1,2 delims==" %%x in (skin_config_%platform%.ini) do (
 set /a index+=1
 set map[%%x]=%%y
 set revmap[%%~ny]=%%x
)

set THEME_PATH=%map[themepath]%

if not exist "%THEME_PATH%" call pathFail.bat themepath %THEME_PATH%

@rem copy pngs
set SRC=%THEME_PATH%
set DST=%ROOT%\ThemeSkin%platform%\app\src\main\res\drawable-land-mdpi\
set EXTRA_LOAD_PNG=%ROOT%\ThemeSkinD9\extra\home_player_load.png

@rem del old pngs
for /r "%DST%" %%f in (*.png) do (
 del "%%f"
)

@rem 适配D9
if "%platform%" == "D9" (
	if not exist "%EXTRA_LOAD_PNG%" call pathFail.bat EXTRA_LOAD_PNG "%EXTRA_LOAD_PNG%"
	@rem copy extra png
	copy "%EXTRA_LOAD_PNG%" "%DST%"
)

if not exist "%SRC%" call pathFail.bat themepath "%SRC%"
if not exist "%DST%" call pathFail.bat dst_path "%DST%"

setlocal enabledelayedexpansion
set /a num_pngs=0
for /r "%SRC%" %%j in (*.png) do (
	set rev=!revmap[%%~nj]!
	if "!rev!" equ "" (
		copy "%%j" "%DST%"
		set /a num_pngs+=1 
	)
)
echo 共复制res图片%num_pngs%个
endlocal

@Rem map lenth --> echo index=%index%

@rem test map echo %revmap[icon_preview]%

@rem delete ignore files
@rem set /a num_prevew=0
@rem setlocal enabledelayedexpansion
@rem for /r "%DST%" %%f in (*.png) do (
@rem 	rem echo %%f
@rem 	set rev=!revmap[%%~nf]!
@rem     @rem echo dst=%%~nf, rev=!rev!
@rem 	if "!rev!" neq "" (
@rem 		del "%%f"
@rem 		echo ------delete------="%%f"
@rem 		set /a num_prevew+=1
@rem 	)
@rem )
@rem echo res目录删除额外图%num_prevew%个
@rem endlocal

@rem copy previews
set PREVIEW_CONFIG_FILE=%ROOT%\skin_config_preview.txt
if not exist "%PREVIEW_CONFIG_FILE%" call platformFail.bat PREVIEW_CONFIG_FILE "%PREVIEW_CONFIG_FILE%"

echo ---------------------
set preview_config_ini=%DIRNAME%skin_config_preview.ini
@rem copy preview_config
echo 准备预览图配置文件
copy "%PREVIEW_CONFIG_FILE%" "%preview_config_ini%"

@rem read preview_config
setlocal enabledelayedexpansion

set /a all_preview_num=0
for /f "tokens=1,2 delims==" %%x in (skin_config_preview.ini) do (
	set key_tmp=%%x
	set key=!key_tmp:~0,2!
	if "!key!" equ "%platform%" (
		set preview_map[%%x]=%%y
		set preview_revmap[%%~ny]=%%x 
		set /a all_preview_num+=1
	)
)

set ASSERTS_DST=%ROOT%\ThemeSkin%platform%\app\src\main\assets
if not exist "%ASSERTS_DST%" md "%ASSERTS_DST%"

del /q "%ASSERTS_DST%\*.png"

set /a num_prevew=0
for /r "%SRC%" %%p in (*.png) do (
	set preview_rev=!preview_revmap[%%~np]!
	if "!preview_rev!" neq "" (
		echo 拷贝预览图到assets目录
		copy "%%p" "%ASSERTS_DST%"
		set /a num_prevew+=1
	)
)
echo 共拷贝预览图%num_prevew%个
if %num_prevew% lss %all_preview_num% call pathFail.bat 预览图文件有错 "%SRC%"

endlocal

echo ---------------------

@rem config colors and dimens
call config_colors_dimens.bat %platform%


