@echo off
set bat_root=%~dp0
@rem 打包分类
set product=%1%
set skin_device_ratio=%2%
set skin_pngs_src_path=%3%
set skin_config_file=%4%
set real_platform=%5%

@rem 打包后skin文件存放目录
set skin_result_dir=%bat_root%skins

@rem skin包源码根路径
set skin_root_dir=%bat_root%ThemeSkin%product%
@rem skin包源码drawable目录
set skin_drawables_path=%skin_root_dir%\app\src\main\res
@rem skin包源码values目录
set skin_values_path=%skin_root_dir%\app\src\main\res\values
@rem skin包源码assets目录
set skin_assets_path=%skin_root_dir%\app\src\main\assets
@rem skin包打包完后的apk路径
set apk_path=ThemeSkin%product%\app\build\outputs\apk\app-release-unsigned.apk

@rem 预览配置文件
set skin_config_preview=%bat_root%skin_config_preview.txt

@rem 主题图片根路径
@rem set /p skin_device_ratio="请输入主题分辨率(例:1024x600或1280x720)："
set x=%skin_device_ratio:~4,1%
if "%x%" NEQ "x" (
echo "分辨率输入错误 ratio=%skin_device_ratio%"
pause & exit
)

@rem 主题图片根路径
@rem set /p skin_pngs_src_path="请输入主题图片根路径："

if not exist "%skin_pngs_src_path%" (
echo "不存在该路径"
pause & exit
)
@rem 主题配置文件路径
@rem set /p skin_config_file="请输入配置文件.xls路径："

if not exist "%skin_config_file%" (
echo "skin_config配置文件不存在"
pause & exit
)

if "" == "%real_platform%" (
	set real_platform=%product%
)

echo product=%product%
echo real_platform=%real_platform%

cd tools

@rem 配置颜色和尺寸
echo 开始配置颜色和尺寸
call config_colors_dimens.bat %real_platform% %skin_config_file% %skin_values_path%
if %errorlevel% == 0 (
	echo 颜色和尺寸配置完成
) else (
	echo 颜色和尺寸配置失败，请检查配置文件
	pause & exit
)
@rem 拷贝图片
echo --------------------
echo 开始拷贝图片
call copy_pngs.bat %real_platform% "%skin_pngs_src_path%" %skin_drawables_path% "%skin_config_preview%" "%skin_assets_path%" %skin_device_ratio%
echo 图片拷贝完成
echo --------------------

cd ..

@rem start build skin
cd ThemeSkin%product%
echo 开始打包
%product%_gradlew.bat assembleRelease