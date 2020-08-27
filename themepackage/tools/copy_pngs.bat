@echo off

set platform=%1%

set src_path=%2%

set res_path=%3%

set prew_path=%4%

set assets_path=%5%

set device_ratio=%6%

set dirname=%~dp0

set root=%dirname%..

echo platform="%platform%"

@rem delete old ini files
for /r "%dirname%" %%i in (*.ini) do (
	if exist "%%i" (
		del /q "%%i"
	)
)

@rem del old pngs
setlocal enabledelayedexpansion
set filterStr="drawable-"
for /f %%f in ('dir %res_path% /a:d/s/b') do (
	set drawabledir="%%~nxf"
	echo drawabledir=!drawabledir!
	echo !drawabledir!|find /i %filterStr%>nul && rd /s /q %%f || echo DONT_DELETE
)
endlocal

@rem make new drawable dir
set drawable_dir=\drawable-nodpi-%device_ratio%
set dst_path=%res_path%%drawable_dir%
echo dst_path=%dst_path%
md %dst_path%

@rem 适配D9
set extra_load_png=%root%\ThemeSkinD9\extra\home_player_load.png
if "%platform%" == "D9" (
	if not exist "%extra_load_png%" call pathFail.bat extra_load_png "%extra_load_png%"
	@rem copy extra png
	copy "%extra_load_png%" %dst_path% >nul
)

echo 拷贝预览图到assets目录
@rem copy skin_config_preview.txt to ini file
copy "%prew_path%" "skin_config_preview.ini" >nul
@rem read preview_config
set /a all_preview_num=0
setlocal enabledelayedexpansion
for /f "tokens=1,2 delims==" %%x in (skin_config_preview.ini) do (
	set key_tmp=%%x
	set key=!key_tmp:~0,-9!
	if "!key!" equ "%platform%" (
		set preview_map[%%x]=%%y
		set preview_revmap[%%~ny]=%%x 
		echo prew_png=%%~ny
		set /a all_preview_num+=1
	)
)
@rem 拷贝预览图
echo all_preview_num=%all_preview_num%
set /a num_prevew=0
for /r "%src_path%" %%p in (*.png) do (
	set preview_rev=!preview_revmap[%%~np]!
	if "!preview_rev!" neq "" (
		copy "%%p" "%assets_path%" >nul
		set /a num_prevew+=1
	)
)
for /r "%src_path%" %%p in (*.gif) do (
	set preview_rev=!preview_revmap[%%~np]!
	if "!preview_rev!" neq "" (
		copy "%%p" "%assets_path%" >nul
		set /a num_prevew+=1
	)
)
echo 共拷贝预览图%num_prevew%个
if %num_prevew% lss %all_preview_num% call pathFail.bat 预览图文件有错 "%PREVIEW_CONFIG_FILE%"

@rem start copy pngs
set /a num_pngs=0
for /r "%src_path%" %%j in (*.png) do (
	set rev=!preview_revmap[%%~nj]!
	if "!rev!" equ "" (
		copy "%%j" "%dst_path%" >nul
		set /a num_pngs+=1 
	)
)
echo 共复制res_png图片%num_pngs%个

@rem start copy jpgs
echo platform=%platform%
if "%platform%" == "A7" (
    set /a num_jpgs=0
	for /r "%src_path%" %%j in (*.jpg) do (
		set rev=!preview_revmap[%%~nj]!
		if "!rev!" equ "" (
			copy "%%j" "%dst_path%" >nul
			echo copy_jpg=%%j
			set /a num_jpgs+=1
		)
	)
	echo 共复制res_jpg图片%num_jpgs%个
)



if %all_preview_num% == 0 call pathFail.bat 预览图文件有错或者配置文件路径有错 "%PREVIEW_CONFIG_FILE%"

endlocal
