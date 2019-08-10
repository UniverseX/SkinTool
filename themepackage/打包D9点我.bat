@echo off
set platform=D9
cd tools
@rem start copy pngs and config colors and dimens
call copy_pngs.bat %platform%

cd ..

@rem start build skin
cd ThemeSkin%platform%

gradlew.bat assembleRelease