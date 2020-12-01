@echo off & setlocal enabledelayedexpansion
cd d:\test
for /f "tokens=1* delims=." %%i in (1.txt) do (
set /a n+=2
call :loop "%%i %%j" !n!
)

:loop
set s=%1
for /f "tokens=%2 delims=. " %%a in (2.txt) do echo;%s:~1,-1%%%a
goto :eof