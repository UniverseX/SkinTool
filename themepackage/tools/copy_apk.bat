@echo off

@rem ������skin����
set /p skin_name="��������������ƣ�"

set apk_path=%1%

echo apk_path=%apk_path%

if not exist "%apk_path%" call tools\pathFail.bat û���ҵ����ɵ�apk %apk_path%

set skin_path=%2%

echo Ƥ��·��=%skin_path%%skin_name%

copy "%apk_path%" "%skin_path%%skin_name%"

echo ������

pause & exit


