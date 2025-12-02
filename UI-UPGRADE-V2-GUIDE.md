# UI升级 v2.0 使用指南

## 升级内容

### 1. 修复侧边栏乱码
使用 `NavIcon` 类替代原有的图标字体，解决"系统设置"等菜单项前的乱码问题。

```java
// 使用方式
Label icon = NavIcon.createNavIconLabel("系统设置");
// 或者直接获取图标字符
String iconChar = NavIcon.getNavIcon("仪表盘");  // 返回 "▣"
```

### 2. 科技感仪表盘
使用 `DashboardComponents` 类创建科技感组件：

```java
// 数据指标卡片
VBox card = DashboardComponents.createMetricCard("总人数", "1234", "+12%", "primary");

// 概览统计条
HBox overview = DashboardComponents.createOverviewBar(1234, 56, 23, 12);

// 风险分布图
VBox riskChart = DashboardComponents.createRiskDistributionChart(456, 234, 89, 23);

// 活动时间线
List<ActivityItem> items = Arrays.asList(
    new ActivityItem("10:30", "新增人员", "王某某被添加", "success"),
    new ActivityItem("09:15", "风险升级", "李某某升至高风险", "danger")
);
VBox timeline = DashboardComponents.createActivityTimeline(items);

// 实时时钟
VBox clock = DashboardComponents.createRealtimeClock();
```

### 3. 向导式表单弹窗
使用 `WizardDialog` 创建引导式多步骤表单：

```java
WizardDialog<Person> wizard = new WizardDialog<>(stage, "新增人员", new Person());

// 添加步骤
wizard.addStep(new WizardDialog.WizardStep("基本信息", "1")
        .subtitle("填写基本信息")
        .content(step1Content)
        .valid(step1ValidProperty))
    .addStep(new WizardDialog.WizardStep("风险评估", "2")
        .content(step2Content))
    .addStep(new WizardDialog.WizardStep("照片上传", "3")
        .content(step3Content))
    .onSubmit(person -> {
        // 保存逻辑
    });

wizard.show();

// 表单辅助方法
VBox section = WizardDialog.createSection("基本信息", field1, field2);
VBox field = WizardDialog.createField("姓名", textField, "请输入真实姓名");
HBox row = WizardDialog.createTwoColumns(leftField, rightField);
HBox risk = WizardDialog.createRiskLevelSelector(riskProperty);
```

### 4. 下滑式详情查看
使用 `SlideViewerDialog` 创建流畅的详情浏览体验：

```java
SlideViewerDialog viewer = new SlideViewerDialog(stage, "张三 - 详细信息");

// 添加区块
viewer.addSection(new ViewerSection("照片")
        .icon("📷")
        .content(SlideViewerDialog.createPhotoGallery(photoPaths)))
    .addSection(new ViewerSection("基本信息")
        .icon("👤")
        .content(basicInfoBox))
    .onEdit(v -> {
        viewer.close();
        showEditDialog();
    });

viewer.show();

// 内容辅助方法
HBox row = SlideViewerDialog.createFieldRow("姓名", "张三");
HBox twoCol = SlideViewerDialog.createTwoColumnFields("姓名", "张三", "性别", "男");
Label badge = SlideViewerDialog.createRiskBadge("高风险");
VBox gallery = SlideViewerDialog.createPhotoGallery(photoPaths);
```

### 5. 照片管理系统

#### 数据模型
```java
Photo photo = new Photo();
photo.setPersonId(personId);
photo.setFilePath("/path/to/photo.jpg");
photo.setFileName("photo.jpg");
photo.setIsPrimary(true);
```

#### 服务层使用
```java
PhotoService photoService = new PhotoService();

// 初始化（创建数据库表）
PhotoService.init();

// 上传照片
Photo photo = photoService.uploadPhoto(personId, file, "描述", true);

// 批量上传
List<Photo> photos = photoService.uploadPhotos(personId, files, true);

// 获取照片
List<Photo> photos = photoService.getPhotosByPerson(personId);
String primaryPath = photoService.getPrimaryPhotoPath(personId);

// 删除照片
photoService.deletePhoto(photoId);
photoService.deleteAllPhotos(personId);
```

### 6. 动画效果

```java
// 淡入/淡出
AnimationUtil.fadeIn(node);
AnimationUtil.fadeOut(node, Duration.millis(300), () -> {});

// 滑动
AnimationUtil.slideInFromRight(node);
AnimationUtil.slideInFromBottom(node, Duration.millis(400), null);

// 缩放
AnimationUtil.scaleIn(node);
AnimationUtil.popIn(node);
AnimationUtil.bounce(node);
AnimationUtil.pulse(node);

// 特效
AnimationUtil.shake(node);  // 抖动（验证失败）
AnimationUtil.flash(node);  // 闪烁
AnimationUtil.heartbeat(node);

// 交错动画
AnimationUtil.staggerFadeIn(node1, node2, node3);
AnimationUtil.staggerSlideIn(60, nodes);  // 60ms延迟

// 向导切换
AnimationUtil.wizardNext(currentPane, nextPane, null);
AnimationUtil.wizardPrev(currentPane, prevPane, null);

// 弹窗动画
AnimationUtil.dialogOpen(stage);
AnimationUtil.dialogClose(stage, () -> stage.close());
```

### 7. Toast提示

```java
DialogUtil.showToast(stage, "操作成功", DialogUtil.ToastType.SUCCESS);
DialogUtil.showToast(stage, "发生错误", DialogUtil.ToastType.ERROR);
DialogUtil.showToast(stage, "请注意", DialogUtil.ToastType.WARNING);
```

## CSS样式类

### 卡片样式
- `.metric-card` - 数据指标卡片
- `.metric-card-primary` / `.metric-card-danger` / `.metric-card-warning` / `.metric-card-success`
- `.data-panel` - 数据面板
- `.quick-action-card` - 快捷操作卡片

### 向导弹窗
- `.wizard-dialog` - 向导容器
- `.wizard-header` - 向导头部
- `.wizard-steps` - 步骤指示器
- `.wizard-step-circle-active` - 当前步骤
- `.wizard-content` - 内容区
- `.wizard-section` - 表单区块

### 下滑查看
- `.slide-viewer` - 查看器容器
- `.slide-viewer-section` - 内容区块
- `.photo-gallery` - 照片画廊
- `.photo-thumbnail` - 缩略图

### 风险徽章
- `.risk-badge-low` - 低风险（绿色）
- `.risk-badge-medium` - 中风险（黄色）
- `.risk-badge-high` - 高风险（橙红）
- `.risk-badge-extreme` - 极高风险（红色）

## 数据库变更

照片表结构：
```sql
CREATE TABLE photos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    person_id INTEGER NOT NULL,
    file_path TEXT NOT NULL,
    file_name TEXT,
    description TEXT,
    is_primary INTEGER DEFAULT 0,
    upload_time TEXT,
    file_size INTEGER,
    mime_type TEXT,
    FOREIGN KEY (person_id) REFERENCES petitioner(id) ON DELETE CASCADE
);
```

在应用启动时调用 `PhotoService.init()` 自动创建表。

## 注意事项

1. 确保在应用启动时调用 `PhotoService.init()` 初始化照片表
2. 照片默认存储在 `~/.petition-system/photos/` 目录
3. 删除人员时记得调用 `photoService.deleteAllPhotos(personId)`
4. `*Example.java` 文件是示例代码，展示如何集成使用这些组件
