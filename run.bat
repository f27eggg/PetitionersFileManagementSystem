@echo off
echo ========================================
echo 上访人员重点监控信息管理系统 v1.0.0
echo 开发人员：刘一村
echo ========================================
echo.

:: 设置项目路径
set PROJECT_DIR=%~dp0
set MAVEN_PATH=D:\Program Files\Apache-maven-3.6.3\apache-maven-3.6.3\bin\mvn

:: 检查启动方式
if "%1"=="jar" goto :RUN_JAR
if "%1"=="mvn" goto :RUN_MVN

:: 默认使用Maven方式启动（开发模式）
:RUN_MVN
echo [启动模式] Maven JavaFX 开发模式
echo.
echo 正在启动应用程序...
echo.

"%MAVEN_PATH%" -f "%PROJECT_DIR%pom.xml" javafx:run

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [错误] 应用程序启动失败！
    echo.
    echo 请检查：
    echo 1. Maven 是否正确安装
    echo 2. pom.xml 配置是否正确
    echo 3. 依赖是否已下载完成
    echo.
    pause
)
goto :EOF

:: 使用JAR包方式启动（生产模式）
:RUN_JAR
echo [启动模式] JAR 包运行模式
echo.

set JAR_FILE=%PROJECT_DIR%target\petitioners-system-1.0.0-shaded.jar

if not exist "%JAR_FILE%" (
    echo [警告] JAR文件不存在，正在编译打包...
    echo.
    "%MAVEN_PATH%" -f "%PROJECT_DIR%pom.xml" clean package -DskipTests
    echo.
)

if exist "%JAR_FILE%" (
    echo 正在启动应用程序...
    echo.

    :: 检查是否设置了JAVAFX_HOME
    if defined JAVAFX_HOME (
        java --module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls,javafx.fxml -jar "%JAR_FILE%"
    ) else (
        :: 尝试直接运行（如果JAR已包含JavaFX依赖）
        java -jar "%JAR_FILE%"
    )

    if %ERRORLEVEL% NEQ 0 (
        echo.
        echo [错误] 应用程序启动失败！
        echo.
        echo 请确保：
        echo 1. 已安装 JDK 17 或更高版本
        echo 2. 已设置 JAVAFX_HOME 环境变量（如需要）
        echo.
        pause
    )
) else (
    echo [错误] JAR文件打包失败！
    pause
)
goto :EOF

:EOF
