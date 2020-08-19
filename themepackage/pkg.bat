@echo off
set bat_root=%~dp0
@rem �������
set product=%1%
set skin_device_ratio=%2%
set skin_pngs_src_path=%3%
set skin_config_file=%4%
set real_platform=%5%

@rem �����skin�ļ����Ŀ¼
set skin_result_dir=%bat_root%skins

@rem skin��Դ���·��
set skin_root_dir=%bat_root%ThemeSkin%product%
@rem skin��Դ��drawableĿ¼
set skin_drawables_path=%skin_root_dir%\app\src\main\res
@rem skin��Դ��valuesĿ¼
set skin_values_path=%skin_root_dir%\app\src\main\res\values
@rem skin��Դ��assetsĿ¼
set skin_assets_path=%skin_root_dir%\app\src\main\assets
@rem skin���������apk·��
set apk_path=ThemeSkin%product%\app\build\outputs\apk\app-release-unsigned.apk

@rem Ԥ�������ļ�
set skin_config_preview=%bat_root%skin_config_preview.txt

@rem ����ͼƬ��·��
@rem set /p skin_device_ratio="����������ֱ���(��:1024x600��1280x720)��"
set x=%skin_device_ratio:~4,1%
if "%x%" NEQ "x" (
echo "�ֱ���������� ratio=%skin_device_ratio%"
pause & exit
)

@rem ����ͼƬ��·��
@rem set /p skin_pngs_src_path="����������ͼƬ��·����"

if not exist "%skin_pngs_src_path%" (
echo "�����ڸ�·��"
pause & exit
)
@rem ���������ļ�·��
@rem set /p skin_config_file="�����������ļ�.xls·����"

if not exist "%skin_config_file%" (
echo "skin_config�����ļ�������"
pause & exit
)

if "" == "%real_platform%" (
	set real_platform=%product%
)

echo product=%product%
echo real_platform=%real_platform%

cd tools

@rem ������ɫ�ͳߴ�
echo ��ʼ������ɫ�ͳߴ�
call config_colors_dimens.bat %real_platform% %skin_config_file% %skin_values_path%
if %errorlevel% == 0 (
	echo ��ɫ�ͳߴ��������
) else (
	echo ��ɫ�ͳߴ�����ʧ�ܣ����������ļ�
	pause & exit
)
@rem ����ͼƬ
echo --------------------
echo ��ʼ����ͼƬ
call copy_pngs.bat %real_platform% "%skin_pngs_src_path%" %skin_drawables_path% "%skin_config_preview%" "%skin_assets_path%" %skin_device_ratio%
echo ͼƬ�������
echo --------------------

cd ..

@rem start build skin
cd ThemeSkin%product%
echo ��ʼ���
%product%_gradlew.bat assembleRelease