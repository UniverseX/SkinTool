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
	echo !drawabledir!|find /i %filterStr%>nul 2>nul && rd /s /q %%f || echo DONT_DELETE
)
endlocal
@rem make new drawable dir
set drawable_dir=\drawable-nodpi-%device_ratio%
set dst_path=%res_path%%drawable_dir%
echo dst_path=%dst_path%
md %dst_path%

@rem ����D9
set extra_load_png=%root%\ThemeSkinD9\extra\home_player_load.png
if "%platform%" == "D9" (
	if not exist "%extra_load_png%" call pathFail.bat extra_load_png "%extra_load_png%"
	@rem copy extra png
	copy "%extra_load_png%" %dst_path%
)

@rem copy skin_config_preview.txt to ini file
copy "%prew_path%" "skin_config_preview.ini"
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
echo all_preview_num=%all_preview_num%

@rem start copy pngs
set /a num_pngs=0
for /r "%src_path%" %%j in (*.png) do (
	set rev=!preview_revmap[%%~nj]!
	if "!rev!" equ "" (
		copy "%%j" "%dst_path%"
		set /a num_pngs+=1 
	)
)
echo ������res_pngͼƬ%num_pngs%��

@rem start copy jpgs
echo platform=%platform%
if "%platform%" == "A7" (
    set /a num_jpgs=0
	for /r "%src_path%" %%j in (*.jpg) do (
		set rev=!preview_revmap[%%~nj]!
		if "!rev!" equ "" (
			copy "%%j" "%dst_path%"
			echo copy_jpg=%%j
			set /a num_jpgs+=1
		)
	)
	echo ������res_jpgͼƬ%num_jpgs%��
)

@rem ����Ԥ��ͼ
set /a num_prevew=0
for /r "%src_path%" %%p in (*.png) do (
	set preview_rev=!preview_revmap[%%~np]!
	if "!preview_rev!" neq "" (
		echo ����Ԥ��ͼ��assetsĿ¼
		copy "%%p" "%assets_path%"
		set /a num_prevew+=1
	)
)
echo ������Ԥ��ͼ%num_prevew%��
if %num_prevew% lss %all_preview_num% call pathFail.bat Ԥ��ͼ�ļ��д� "%PREVIEW_CONFIG_FILE%"

if %all_preview_num% == 0 call pathFail.bat Ԥ��ͼ�ļ��д���������ļ�·���д� "%PREVIEW_CONFIG_FILE%"

endlocal
