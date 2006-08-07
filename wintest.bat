@echo off
rem "This batchfile runs the Charva test program."
rem "It is intended to be run from the '%CHARVA_HOME%'
rem "directory in a DOS command shell."
rem "It expects to find Terminal.dll in the directory
rem '%CHARVA_HOME%\c\lib', and the charva.jar file in the directory
rem '%CHARVA_HOME%\java\lib'.
rem "Last Modified: 2004/5/25 by Rob Pitman <rob@pitman.co.za>"

rem Check that we are in the right directory to run this script.
if not exist "c\lib\Terminal.dll" goto noDLL
rem if not exist "java\lib\charva.jar" goto noJAR

rem JAVA_HOME must be set to the JDK or JRE installation directory 
rem (for example, C:\jdk1.4 or C:\jre1.4)
rem set JAVA_HOME=C:\j2sdk1.4.2
if not exist %JAVA_HOME% goto noJAVA_HOME

rem Uncomment the next line to log keystrokes and debug key-mappings 
rem (the script file is %HOME%\script.charva).
rem set TEST_OPTS="-Dcharva.script.record=%HOME%/script.charva"

rem Uncomment the following option to test for memory leaks.
rem set TEST_OPTS=%TEST_OPTS% -Xrunhprof:heap=sites

rem Comment out the following option to disable colors.
set TEST_OPTS=%TEST_OPTS% -Dcharva.color=1

rem Uncomment the following line if you want to debug the application
rem using an IDE such as IntelliJ IDEA (I believe that other IDEs such
rem as NetBeans and JBuilder have the same capability).
rem set TEST_OPTS=%TEST_OPTS% -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005


%JAVA_HOME%\bin\java %TEST_OPTS% -cp test/classes;java/dist/lib/charva.jar -Djava.library.path=c\lib tutorial.charva.Tutorial
goto end


:noJAVA_HOME
echo The JAVA_HOME environment variable is not set!
goto end

:noDLL
echo The Terminal.dll library is not available!
goto end

:noJAR
echo The charva.jar file is not available!
goto end

:end