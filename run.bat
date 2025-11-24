@echo off
chcp 65001 > nul
echo ========================================
echo 上访人员重点监控信息管理系统 v1.0.0
echo 济南市公安局历下分局刑侦大队
echo ========================================
echo.
echo 正在启动应用程序...
echo.

java --module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls,javafx.fxml -jar "%~dp0target\petitioners-system-1.0.0-shaded.jar"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [错误] 应用程序启动失败！
    echo.
    echo 请确保：
    echo 1. 已安装 JDK 17 或更高版本
    echo 2. 已设置 JAVAFX_HOME 环境变量指向 JavaFX SDK 路径
    echo 3. 或者使用安装程序版本（无需配置环境变量）
    echo.
    pause
)
