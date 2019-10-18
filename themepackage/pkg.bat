@echo off
set bat_root=%~dp0
@rem �������
set platform=%1%
@rem �����skin�ļ����Ŀ¼
set skin_result_dir=%bat_root%skins

@rem skin��Դ���·��
set skin_root_dir=%bat_root%ThemeSkin%platform%
@rem skin��Դ��drawableĿ¼
set skin_drawables_path=%skin_root_dir%\app\src\main\res\drawable-land-mdpi
@rem skin��Դ��valuesĿ¼
set skin_values_path=%skin_root_dir%\app\src\main\res\values
@rem skin��Դ��assetsĿ¼
set skin_assets_path=%skin_root_dir%\app\src\main\assets
@rem skin���������apk·��
set apk_path=ThemeSkin%platform%\app\build\outputs\apk\app-release-unsigned.apk

@rem Ԥ�������ļ�
set skin_config_preview=%bat_root%skin_config_preview.txt

@rem ����ͼƬ��·��
set /p skin_pngs_src_path="����������ͼƬ��·����"
@rem set skin_pngs_src_path=E:\launcher_xui\�����\XUN-�����-����01

if not exist "%skin_pngs_src_path%" (
echo "�����ڸ�·��"
pause & exit
)
@rem ���������ļ�·��
set /p skin_config_file="�����������ļ�.xls·����"

if not exist "%skin_config_file%" (
echo "skin_config�����ļ�������"
pause & exit
)

@rem ����ͼƬ
cd tools
echo ��ʼ����ͼƬ
call copy_pngs.bat %platform% "%skin_pngs_src_path%" "%skin_drawables_path%" "%skin_config_preview%" "%skin_assets_path%"
echo ͼƬ�������
echo --------------------
echo ��ʼ������ɫ�ͳߴ�
call config_colors_dimens.bat %platform% %skin_config_file% %skin_values_path%
if %errorlevel% == 0 (
	echo ��ɫ�ͳߴ��������
) else (
	echo ��ɫ�ͳߴ�����ʧ�ܣ����������ļ�
	pause & exit
)
echo --------------------

cd ..

@rem start build skin
cd ThemeSkin%platform%

%platform%_gradlew.bat assembleRelease