# UI升级包 v2.0

## 升级内容总览

| 问题 | 解决方案 |
|------|----------|
| 侧边栏乱码 | `NavIcon.java` - 使用Unicode字符替代图标字体 |
| 仪表盘不够科技感 | `DashboardComponents.java` - 数据卡片、风险分布、时间线等组件 |
| 新增弹窗要做成引导式 | `WizardDialog.java` - 多步骤向导式表单弹窗 |
| 查看界面要做成下滑式 | `SlideViewerDialog.java` - 下滑浏览详情弹窗 |
| 新增照片数据项 | `Photo.java` + `PhotoDao.java` + `PhotoService.java` |

## 安装方法

```bash
# 1. 进入项目目录
cd PetitionersFileManagementSystem

# 2. 运行安装脚本
bash install-ui-upgrade-v2.sh
```

## 文件清单 (共11个文件，约6200行代码)

### CSS样式
- `main.css` - 完整科技感主题 (~1200行)

### 工具类 (src/main/java/com/petition/util/)
- `AnimationUtil.java` - 动画效果库 (50+种动画)
- `NavIcon.java` - 导航图标辅助类 (解决乱码)
- `DialogUtil.java` - 弹窗工具类
- `WizardDialog.java` - 向导式弹窗组件
- `SlideViewerDialog.java` - 下滑式查看组件
- `DashboardComponents.java` - 仪表盘组件库

### 照片系统
- `Photo.java` - 照片实体类
- `PhotoDao.java` - 照片数据访问层
- `PhotoService.java` - 照片服务层

### 示例代码
- `PersonManageControllerExample.java` - 展示如何使用向导弹窗和照片
- `DashboardControllerExample.java` - 展示如何构建科技感仪表盘

## 核心功能亮点

### 1️⃣ 向导式新增人员
```
步骤1: 基本信息 → 步骤2: 风险评估 → 步骤3: 照片上传
```
- 动态步骤指示器
- 表单验证
- 流畅切换动画

### 2️⃣ 下滑式查看详情
- 照片画廊（支持多张照片翻动）
- 分区展示信息
- 一键跳转编辑

### 3️⃣ 科技感仪表盘
- 脉冲动画指示器
- 数据卡片带悬停效果
- 风险分布可视化
- 实时时钟
- 活动时间线

### 4️⃣ 照片管理
- 支持多张照片
- 设置主照片（头像）
- 画廊浏览模式
- 文件自动管理
