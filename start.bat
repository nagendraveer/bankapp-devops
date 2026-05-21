@echo off
echo Starting BankApp port-forward with auto-reconnect...
echo Open http://localhost:8081 in your browser
echo Press Ctrl+C to stop
echo.

:loop
echo Connecting to bankapp service...
kubectl port-forward svc/bankapp-service 8081:80
echo Connection lost. Reconnecting in 5 seconds...
timeout /t 5 /nobreak >nul
goto loop
