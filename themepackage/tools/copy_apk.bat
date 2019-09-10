@echo off

@rem 打包后的skin名称
set /p skin_name="请输入打包后的名称："

set apk_path=%1%

echo apk_path=%apk_path%

if not exist "%apk_path%" call tools\pathFail.bat 没有找到生成的apk %apk_path%

set skin_path=%2%

echo 皮肤路径=%skin_path%%skin_name%

copy "%apk_path%" "%skin_path%%skin_name%"

echo 打包完成

pause & exit


