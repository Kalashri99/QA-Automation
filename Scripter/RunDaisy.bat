@echo off
if "%1" EQU "" if "%2" EQU "" goto defaultCase
if "%1" EQU "-requestlic" goto requestlic
if "%1" EQU "-generatelic" goto generatelic

set longString="%1"
set tempStr="xlsx="
if "%1" NEQ "Perf" if "%1" NEQ "QA" if "%1" NEQ "UAT" if %longString% == %tempStr% goto defaultCase
if "%1" NEQ "Perf" if "%1" NEQ "QA" if "%1" NEQ "UAT" goto OtherCase

if "%1" EQU "QA" (
	if "%2" NEQ "" (
		goto QAfile
	)
goto QA
)

if "%1" EQU "ScriptQA" goto ScriptQA
if "%1" EQU "ScriptUAT" goto ScriptUAT

if "%1" EQU "UAT" (
	if "%2" NEQ "" (
		goto UATfile
	)
goto UAT
)

if "%1" EQU "Perf" goto Perf
if "%1" NEQ "" goto filecase 

:filecase
call mvn test -Dconfig="Default" -DmasterFile=%1
goto end

:QAfile
call mvn test -Dconfig="QA" -DmasterFile=%2
goto end

:UATfile
call mvn test -Dconfig="UAT" -DmasterFile=%2
goto end


:requestlic
call java -cp .\lib\daisy-core-browser-base-command-0.0.1.jar com.persistent.daisy.lic.saltencrypt.UtilEncrypt
goto end

:generatelic
@echo %1
::cd .\lib
::set CLASSPATH=mysql-connector-java-8.0.28.jar
::@echo %CLASSPATH%
call java -cp  .\lib\mysql-connector-java-8.0.28.jar;.\lib\daisy-core-browser-base-command-0.0.1.jar com.persistent.daisy.lic.saltencrypt.UtilDecrypt
::mvn exec:java 
::cd..
goto end


call mvn test -Dconfig=%1 -DmasterFile="Master.xlsx"
goto end

:bothCase
call mvn test -Dconfig=%1 -DmasterFile=%2
goto end

:defaultCase
call mvn test -Dconfig="Default" -DmasterFile="Master.xlsx"
goto end

:masterCase
call mvn test -Dconfig="Default" -DmasterFile=%1
goto end

:OtherCase
call mvn test -Dconfig=%1 -DmasterFile="Master.xlsx"
goto end

:QA
::call mvn test -Dconfig="QA" -DmasterFile="Master.xlsx"
call mvn test -Dconfig="QA" -DmasterFile="Master.xlsx"
goto end

:ScriptQA
::call mvn test -Dconfig="QA" -DmasterFile="Master.xlsx"
call mvn test -Dconfig="QA" -DmasterFile=%1
goto end

:ScriptUAT
::call mvn test -Dconfig="UAT" -DmasterFile="Master.xlsx"
call mvn test -Dconfig="UAT" -DmasterFile=%1
goto end

:UAT
::call mvn test -Dconfig="UAT" -DmasterFile="Master.xlsx"
call mvn test -Dconfig="UAT" -DmasterFile="Master.xlsx"
goto end


:Perf
::call mvn test -Dconfig="Perf" -DmasterFile="Master.xlsx"
call mvn test -Dconfig="Perf" -DmasterFile="Master.xlsx"
goto end

:end
set "filePath=%~dp0"
findstr /m "Fail" "%filePath%target\surefire-reports\failedScriptsFile.txt"
if %errorlevel%==0 (
color 07
echo.
echo.
echo.
echo -----------------------------------------------------------------------------------------------
echo !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
echo.
SETLOCAL EnableDelayedExpansion
for /F "tokens=1,2 delims=#" %%a in ('"prompt #$H#$E# & echo on & for %%b in (1) do     rem"') do (
  set "DEL=%%a"
)
call :colorEcho a0 "Please refer execution report to get detailed information about failure"
pause
exit
:colorEcho
<nul set /p ".=%DEL%" > "%~2"
findstr /v /a:%1 /R "^$" "%~2" nul
del "%~2" > nul 2>&1i

echo. !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
echo. -----------------------------------------------------------------------------------------------
echo.
echo.
echo.
echo.
)
pause
