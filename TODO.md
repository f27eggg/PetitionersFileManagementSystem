# 上访人员重点监控信息管理系统 - 开发任务清单

## 文档说明

本文档记录系统从设计到开发的所有待办任务，按开发阶段组织。

**状态标识**：
- ✅ 已完成
- 🔄 进行中
- ⏸️ 暂停
- ⏭️ 待开始
- ❌ 已取消

---

## 阶段一：项目初始化（已完成 ✅）

### 1.1 环境与版本控制
- ✅ 初始化Git仓库
- ✅ 创建.gitignore文件
- ⏭️ 配置Git用户信息（待执行）
- ⏭️ 首次Git提交

### 1.2 项目结构搭建
- ✅ 创建docs/文档目录
- ✅ 创建assets/资源目录
- ✅ 创建data/数据目录

### 1.3 设计文档编写
- ✅ README.md（项目说明）
- ✅ 数据结构设计.md（32个字段详细说明）
- ✅ 功能需求文档.md（5大核心功能）
- ✅ 技术选型方案.md（Java 17 + JavaFX方案）
- ✅ UI设计方案.md（科技感深色主题设计）
- ✅ 项目目录结构规划.md（完整目录树）
- ✅ TODO.md（本文档）

---

## 阶段二：开发环境准备（已完成 ✅）

### 2.1 JDK安装与配置
- ✅ 下载并安装JDK 17.0.12
- ✅ 配置JAVA_HOME环境变量
- ✅ 验证`java -version`和`javac -version`

### 2.2 Maven配置
- ✅ 下载安装Maven 3.6.3
- ✅ 配置MAVEN_HOME环境变量
- ✅ 配置Maven国内镜像（阿里云）
- ✅ 验证`mvn -version`

### 2.3 IDE配置
- ✅ 安装IntelliJ IDEA
- ✅ 配置IDEA的JDK 17
- ✅ 安装JavaFX插件
- ✅ 下载Scene Builder并集成到IDEA

### 2.4 创建Maven项目
- ✅ 创建pom.xml配置文件
- ✅ 配置Maven依赖（JavaFX, Jackson, POI, Logback, Batik）
- ✅ 创建标准Maven目录结构（src/main/java, src/main/resources等）
- ✅ 执行`mvn clean compile`验证配置

---

## 阶段三：数据层开发（待开始 ⏭️）

### 3.1 数据模型实现
- ✅ 创建枚举类
  - ✅ Gender.java（性别）
  - ✅ Education.java（学历）
  - ✅ MaritalStatus.java（婚姻状态）
  - ✅ RiskLevel.java（危险等级）
  - ✅ EntryMethod.java（进京方式）
  - ✅ TransportMethod.java（在京通行方式）

- ✅ 创建实体类
  - ✅ PersonalInfo.java（个人信息，17字段）
  - ✅ BeijingContact.java（在京关系人，6字段）
  - ✅ PetitionCase.java（信访案件，7字段）
  - ✅ RiskAssessment.java（评估结果，1字段）
  - ✅ Petitioner.java（主实体，聚合以上四个模块）

### 3.2 数据访问层实现（已完成 ✅）
- ✅ JsonDataManager.java（JSON文件读写）
  - ✅ 实现loadAll()方法（加载所有数据）
  - ✅ 实现saveAll()方法（保存所有数据）
  - ✅ 实现findById()方法（按ID查询）
  - ✅ 实现save()方法（保存单条）
  - ✅ 实现delete()方法（删除单条）
  - ✅ 实现batchDelete()方法（批量删除）
  - ✅ 实现count()方法（统计总数）
  - ✅ 实现clear()方法（清空数据）
  - ✅ 实现refresh()方法（刷新缓存）

- ✅ ConfigManager.java（配置管理）
  - ✅ 实现loadConfig()方法
  - ✅ 实现saveConfig()方法
  - ✅ 实现get/getString/getInt/getBoolean()方法
  - ✅ 实现set()方法
  - ✅ 实现resetToDefault()方法

- ✅ BackupManager.java（数据备份）
  - ✅ 实现backup()方法
  - ✅ 实现restore()方法
  - ✅ 实现restoreLatest()方法
  - ✅ 实现listBackups()方法
  - ✅ 实现startAutoBackup()方法（定时备份）
  - ✅ 实现stopAutoBackup()方法
  - ✅ 实现cleanupOldBackups()方法

### 3.3 单元测试（已完成 ✅）
- ✅ 编写JsonDataManager测试用例（12个测试）
- ✅ 编写ConfigManager测试用例（9个测试）
- ✅ 编写BackupManager测试用例（12个测试）
- ✅ 测试数据序列化/反序列化
- ✅ 测试枚举类序列化
- ✅ 测试LocalDateTime序列化
- ✅ 所有测试通过（33/33）

---

## 阶段四：业务逻辑层开发（已完成 ✅）

### 4.1 核心业务服务（已完成 ✅）
- ✅ PetitionerService.java（人员管理服务）
  - ✅ getAllPetitioners()
  - ✅ getPetitionerById()
  - ✅ addPetitioner()（含身份证号重复检查）
  - ✅ updatePetitioner()
  - ✅ deletePetitioner()
  - ✅ batchDelete()
  - ✅ getTotalCount()
  - ✅ 数据验证功能

- ✅ QueryService.java（查询服务）
  - ✅ quickSearch()（快速搜索）
  - ✅ advancedQuery()（高级查询）
  - ✅ filterByRiskLevel()
  - ✅ filterByVisitCount()
  - ✅ getHighRiskPetitioners()
  - ✅ filterByNativePlace()
  - ✅ QueryCriteria查询条件类

- ✅ StatisticsService.java（统计服务）
  - ✅ getTotalCount()
  - ✅ getRiskLevelDistribution()（危险等级分布）
  - ✅ getVisitCountDistribution()（上访次数分布）
  - ✅ getNativePlaceDistribution()（籍贯分布）
  - ✅ getEntryMethodDistribution()（进京方式分布）
  - ✅ getEducationDistribution()（学历分布）
  - ✅ getGenderDistribution()（性别分布）
  - ✅ getMaritalStatusDistribution()（婚姻状态分布）
  - ✅ getHighRiskCount()（高危人员数量）
  - ✅ getStatisticsSummary()（统计摘要）

- ✅ 单元测试（38个测试用例）
  - ✅ PetitionerServiceTest（14个测试）
  - ✅ QueryServiceTest（14个测试）
  - ✅ StatisticsServiceTest（10个测试）

### 4.2 导入导出服务（已完成 ✅）
- ✅ ImportService.java（导入服务）
  - ✅ importFromExcel()（从Excel导入数据）
  - ✅ parseRowData()（解析Excel行数据，16列映射）
  - ✅ validateImportData()（数据验证）
  - ✅ isDuplicate()（重复检查）
  - ✅ ImportResult内部类（导入结果统计）
  - ✅ ImportError内部类（错误信息封装）

- ✅ ExportService.java（导出服务）
  - ✅ exportToExcel()（导出为Excel格式）
  - ✅ exportToCsv()（导出为CSV格式）
  - ✅ exportSelected()（按ID列表导出）
  - ✅ createExcelHeader()（创建Excel表头）
  - ✅ fillExcelRow()（填充Excel数据行）
  - ✅ csvEscape()（CSV特殊字符转义）

- ✅ 单元测试（28个测试用例）
  - ✅ ImportServiceTest（12个测试）
  - ✅ ExportServiceTest（16个测试）
  - ✅ 所有测试通过（99/99）

### 4.3 工具类开发（已完成 ✅）
- ✅ ValidationUtil.java（验证工具）
  - ✅ isEmpty/isNotEmpty() - 空值验证
  - ✅ isValidIdCard() - 身份证号格式验证
  - ✅ isValidPhone() - 手机号验证
  - ✅ isValidEmail() - 邮箱验证
  - ✅ isLengthInRange() - 长度范围验证
  - ✅ isNumberInRange() - 数值范围验证
  - ✅ isNumeric/isAlpha/isAlphanumeric() - 字符类型验证
  - ✅ matches() - 正则表达式匹配

- ✅ IdCardUtil.java（身份证工具）
  - ✅ extractGender() - 提取性别
  - ✅ extractBirthDate() - 提取出生日期
  - ✅ extractAge() - 计算年龄
  - ✅ extractProvinceCode/extractProvinceName() - 提取省份信息
  - ✅ maskIdCard() - 脱敏显示（默认/自定义）
  - ✅ convert15To18() - 15位转18位

- ✅ DateUtil.java（日期工具）
  - ✅ 日期格式化与解析（多种格式）
  - ✅ 日期时间计算（加减天/月/年）
  - ✅ 日期判断（今天/过去/未来/范围）
  - ✅ 月份边界获取

- ✅ FileUtil.java（文件工具）
  - ✅ 文件判断（存在/文件/目录）
  - ✅ 文件操作（创建/删除/复制/移动）
  - ✅ 文件读写（字符串/行列表）
  - ✅ 文件信息（大小/扩展名/名称）
  - ✅ 目录操作（列表/递归）

- ✅ 单元测试（91个测试用例）
  - ✅ ValidationUtilTest（22个测试）
  - ✅ IdCardUtilTest（19个测试）
  - ✅ DateUtilTest（28个测试）
  - ✅ FileUtilTest（22个测试）
  - ✅ 所有测试通过（190/190）

---

## 阶段五：UI界面开发（已完成 ✅）

### 5.1 主窗口与导航（已完成 ✅）
- ✅ 设计main.fxml（主窗口布局）
- ✅ 实现MainController.java
- ✅ 创建侧边导航栏
- ✅ 实现页面路由切换逻辑

### 5.2 仪表盘页面（已完成 ✅）
- ✅ 设计dashboard.fxml
- ✅ 实现DashboardController.java
- ✅ 数据卡片组件开发
- ⏭️ 危险等级分布饼图（阶段六完成）
- ⏭️ 上访次数分布柱状图（阶段六完成）
- ⏭️ 籍贯地区分布柱状图（阶段六完成）

### 5.3 人员列表页面（已完成 ✅）

- ✅ 设计petitioners.fxml
- ✅ 实现PetitionersController.java
- ✅ 表格数据绑定（11列）
- ✅ 快速搜索功能（姓名/身份证/电话）
- ✅ 分页功能（支持10/20/50/100条/页）
- ✅ 多条件筛选（风险等级/性别）
- ✅ 操作按钮（查看/编辑/删除）

### 5.4 详情页面（已完成 ✅）
- ✅ 设计detail.fxml（标签页布局）
- ✅ 实现DetailController.java
- ✅ 个人信息标签页
- ✅ 在京关系人标签页
- ✅ 信访案件标签页
- ✅ 评估结果标签页
- ✅ 集成到PetitionersController（查看按钮）

### 5.5 表单页面（新增/编辑）（已完成 ✅）

- ✅ 设计form.fxml（4个Tab标签页）
- ✅ 实现FormController.java（新增/编辑双模式）
- ✅ 表单字段布局（35+个字段）
- ✅ 实时验证逻辑（姓名/身份证/电话）
- ✅ 错误提示显示（底部错误标签）
- ✅ 保存/取消逻辑（含确认对话框）
- ✅ 集成到PetitionersController

### 5.6 高级查询页面（已完成 ✅）
- ✅ 设计query.fxml
- ✅ 实现QueryController.java
- ✅ 多条件筛选器
- ✅ 查询结果展示

### 5.7 统计图表页面（已完成 ✅）
- ✅ 设计chart.fxml
- ✅ 实现ChartController.java
- ✅ 集成JavaFX Charts组件

### 5.8 设置页面（已完成 ✅）
- ✅ 设计settings.fxml
- ✅ 实现SettingsController.java
- ✅ 配置项UI
- ✅ 数据备份/恢复功能

---

## 阶段六：UI样式美化（待开始 ⏭️）

### 6.1 CSS样式开发
- ⏭️ main.css（基础样式）
- ⏭️ dark-theme.css（深色主题变量定义）
- ⏭️ components.css（组件样式）
- ⏭️ animations.css（动画效果）

### 6.2 图标与素材
- ⏭️ 设计/下载应用图标（app-icon.ico）
- ⏭️ 准备菜单图标（SVG格式）
- ⏭️ 准备系统Logo
- ⏭️ 准备启动画面

### 6.3 自定义组件
- ⏭️ RiskBadge.java（危险等级标签组件）
- ⏭️ SearchBox.java（搜索框组件）
- ⏭️ DataCard.java（数据卡片组件）

---

## 阶段七：功能集成与测试（待开始 ⏭️）

### 7.1 功能集成
- ⏭️ 集成各页面到主窗口
- ⏭️ 实现页面间数据传递
- ⏭️ 实现全局状态管理
- ⏭️ 实现消息提示系统（Toast）

### 7.2 功能测试
- ⏭️ 测试CRUD功能（增删改查）
- ⏭️ 测试搜索功能
- ⏭️ 测试统计图表
- ⏭️ 测试导入导出
- ⏭️ 测试数据验证
- ⏭️ 测试异常处理

### 7.3 性能优化
- ⏭️ 优化大数据量加载速度
- ⏭️ 优化图表渲染性能
- ⏭️ 优化界面响应速度

### 7.4 Bug修复
- ⏭️ 记录并修复发现的Bug
- ⏭️ 回归测试

---

## 阶段八：打包与部署（待开始 ⏭️）

### 8.1 打包准备
- ⏭️ 配置Maven打包插件
- ⏭️ 配置应用元数据（版本号、供应商等）
- ⏭️ 准备JRE运行时环境

### 8.2 jpackage打包
- ⏭️ 编写打包脚本（scripts/package.bat）
- ⏭️ 生成Windows exe安装程序
- ⏭️ 生成免安装绿色版
- ⏭️ 测试安装程序

### 8.3 最终交付
- ⏭️ 打包源代码（压缩包）
- ⏭️ 编写用户使用手册
- ⏭️ 准备演示数据
- ⏭️ 准备演示视频/PPT

---

## 阶段九：后续迭代（未来规划）

### 9.1 功能增强
- ⏭️ 用户权限管理
- ⏭️ 操作日志记录
- ⏭️ 数据变更历史
- ⏭️ 高级数据分析

### 9.2 技术升级
- ⏭️ 迁移到SQLite数据库（支持更大数据量）
- ⏭️ 实现数据加密存储
- ⏭️ 支持网络同步

---

## 当前进度总览

| 阶段 | 状态 | 完成度 |
|------|------|--------|
| 阶段一：项目初始化 | ✅ 已完成 | 100% |
| 阶段二：开发环境准备 | ✅ 已完成 | 100% |
| 阶段三：数据层开发 | ✅ 已完成 | 100% |
| 阶段四：业务逻辑层开发 | ✅ 已完成 | 100% |
| 阶段五：UI界面开发 | ✅ 已完成 | 100% |
| 阶段六：UI样式美化 | ⏭️ 待开始 | 0% |
| 阶段七：功能集成与测试 | ⏭️ 待开始 | 0% |
| 阶段八：打包与部署 | ⏭️ 待开始 | 0% |

**总体进度**：阶段一至阶段五已完成，下一步进入阶段六UI样式美化

---

## 下一步行动计划

**当前阶段**：阶段五 - UI界面开发（已完成 ✅）

**已完成子阶段**：
1. ✅ 主窗口与导航（5.1）
2. ✅ 仪表盘页面（5.2）
3. ✅ 人员列表页面（5.3）
4. ✅ 详情页面（5.4）
5. ✅ 表单页面（5.5）
6. ✅ 高级查询页面（5.6）
7. ✅ 统计图表页面（5.7）
8. ✅ 设置页面（5.8）

**下一阶段**：阶段六 - UI样式美化

---

*文档最后更新：2025年11月23日*
*当前负责人：开发团队*
