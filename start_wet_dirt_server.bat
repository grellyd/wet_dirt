@echo off

For %%G IN (wet_dirt_server.jar) DO (goto  :Process)
echo "Please place me in the same folder as the server .jar file."
goto :Exit

:Process
call java -jar wet_dirt_server.jar

:Exit
exit

