@echo off

echo 1 - Generate Encrypted/Decrypted Password
echo 2 - Daisy folder Clean up:Delete files from Report folder, Screenshot folder, ProgramData, json2excel and Logs folder

:menuCase
SET /P choice=Type 1 or 2 then press ENTER:
if /I "%choice%"=="1" goto pwdCase
if /I "%choice%"=="2" (goto cleanCase) else (goto menuCase)

:pwdCase
java -cp .\lib\daisy-core-browser-base-command-0.0.1.jar com.persistent.daisy.base.Encryptor
goto end

:cleanCase
SET /P env=Enter environment:
if /I "%env%"=="" (goto defaultEnv) else (goto durationCase)

:defaultEnv
set env=Default
goto durationCase

:durationCase
SET /P days=Enter duration for which you want to retain files (days):
if /I "%days%"=="" (goto durationCase) else (goto valCase)

:valCase
SET /P choice=Are you sure you wish to delete file older than %days% days from %env% environment? (Y/N):
if /I "%choice%"=="N" goto end
if /I "%choice%"=="Y" (goto deleteCase) else (goto valCase)

:deleteCase
java -cp .\lib\daisy-core-browser-base-command-0.0.1.jar com.persistent.daisy.base.DaisyClean %days% %env%
echo clean complete
goto end

:end
pause