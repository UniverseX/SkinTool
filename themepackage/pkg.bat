@echo off
set bat_root=%~dp0
@rem 打包分类
set platform=%1%
@rem 打包后skin文件存放目录
set skin_result_dir=%bat_root%skins

@rem skin包源码根路径
set skin_root_dir=%bat_root%ThemeSkin%platform%
@rem skin包源码drawable目录
set skin_drawables_path=%skin_root_dir%\app\src\main\res\drawable-land-mdpi
@rem skin包源码values目录
set skin_values_path=%skin_root_dir%\app\src\main\res\values
@rem skin包源码assets目录
set skin_assets_path=%skin_root_dir%\app\src\main\assets
@rem skin包打包完后的apk路径
set apk_path=ThemeSkin%platform%\app\build\outputs\apk\app-release-unsigned.apk

@rem 预览配置文件
set skin_config_preview=%bat_root%skin_config_preview.txt

@rem 主题图片根路径
set /p skin_pngs_src_path="请输入主题图片根路径："
@rem set skin_pngs_src_path=E:\launcher_xui\主题包\XUN-主题包-节日01

if not exist "%skin_pngs_src_path%" (
echo "不存在该路径"
pause & exit
)
@rem 主题配置文件路径
set /p skin_config_file="请输入配置文件.xls路径："

if not exist "%skin_config_file%" (
echo "skin_config配置文件不存在"
pause & exit
)

@rem 拷贝图片
cd tools
echo 开始拷贝图片
call copy_pngs.bat %platform% "%skin_pngs_src_path%" "%skin_drawables_path%" "%skin_config_preview%" "%skin_assets_path%"
echo 图片拷贝完成
echo --------------------
echo 开始配置颜色和尺寸
call config_colors_dimens.bat %platform% %skin_config_file% %skin_values_path%
if %errorlevel% == 0 (
	echo 颜色和尺寸配置完成
) else (
	echo 颜色和尺寸配置失败，请检查配置文件
	pause & exit
)
echo --------------------

cd ..

@rem start build skin
cd ThemeSkin%platform%

%platform%_gradlew.bat assembleRelease