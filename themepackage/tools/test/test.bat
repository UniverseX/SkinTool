@echo off
::copy ..\skin_config_D9.txt skin_config_D9.ini
set /a index=0
for /f "tokens=1,2 delims==" %%i in (skin_config_D9.ini) do (
 set /a index+=1
 set map[%%i]=%%j
)
echo index=%index%

echo %map[player_sphere_album_color_c]%

if "aaa" neq "" echo not_equal

if "aaa" equ "aaa" echo equal