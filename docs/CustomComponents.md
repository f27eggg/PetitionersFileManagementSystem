# 自定义组件使用说明

本文档介绍如何使用系统提供的自定义JavaFX组件。

---

## 1. RiskBadge（危险等级标签组件）

### 功能
显示危险等级的彩色徽章，支持四种风险等级（低危、中危、高危、极危）。

### 使用方法

```java
import com.petition.view.component.RiskBadge;
import com.petition.model.enums.RiskLevel;

// 方式1：创建后设置风险等级
RiskBadge badge1 = new RiskBadge();
badge1.setRiskLevel(RiskLevel.HIGH);

// 方式2：直接传入风险等级
RiskBadge badge2 = new RiskBadge(RiskLevel.MEDIUM);

// 设置大尺寸样式
badge1.setLarge();

// 添加到布局
VBox container = new VBox(badge1, badge2);
```

### 样式类
- `.risk-badge-low` - 低危（绿色）
- `.risk-badge-medium` - 中危（橙色）
- `.risk-badge-high` - 高危（深橙色）
- `.risk-badge-extreme` - 极危（红色）
- `.risk-badge-large` - 大尺寸徽章

---

## 2. SearchBox（搜索框组件）

### 功能
带搜索按钮和清空按钮的输入框组件，支持回车搜索。

### 使用方法

```java
import com.petition.view.component.SearchBox;

// 创建搜索框
SearchBox searchBox = new SearchBox();

// 设置提示文本
searchBox.setPromptText("请输入姓名、身份证或电话...");

// 设置搜索事件处理器
searchBox.setOnSearch(keyword -> {
    System.out.println("搜索关键词：" + keyword);
    // 执行搜索逻辑
});

// 获取当前搜索文本
String text = searchBox.getText();

// 清空搜索框
searchBox.clear();

// 添加到布局
HBox header = new HBox(searchBox);
```

### 样式类
- `.search-box` - 搜索框容器
- `.search-field` - 输入框
- `.primary-button` - 搜索按钮
- `.secondary-button` - 清空按钮

---

## 3. DataCard（数据卡片组件）

### 功能
显示统计数据的卡片组件，适用于仪表盘和统计页面。

### 使用方法

```java
import com.petition.view.component.DataCard;

// 方式1：创建空卡片
DataCard card1 = new DataCard();
card1.setTitle("总人数");
card1.setValue(100);
card1.setDescription("共有记录数");

// 方式2：创建带标题和值的卡片
DataCard card2 = new DataCard("高危人员", "25");

// 方式3：创建带完整信息的卡片
DataCard card3 = new DataCard("中危人员", "40", "需重点关注");

// 设置卡片类型（颜色）
card1.setType("danger");   // 红色边框
card2.setType("warning");  // 橙色边框
card3.setType("success");  // 绿色边框

// 动态更新值
card1.setValue(120);
card1.setValue("120");

// 添加到布局
HBox cards = new HBox(10, card1, card2, card3);
```

### 样式类
- `.stat-card` - 基础卡片样式
- `.stat-card-danger` - 危险类型（红色边框）
- `.stat-card-warning` - 警告类型（橙色边框）
- `.stat-card-success` - 成功类型（绿色边框）
- `.stat-card-title` - 卡片标题
- `.stat-card-value` - 卡片数值
- `.stat-desc` - 卡片描述

---

## 在FXML中使用

### 引入自定义组件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import com.petition.view.component.RiskBadge?>
<?import com.petition.view.component.SearchBox?>
<?import com.petition.view.component.DataCard?>
<?import javafx.scene.layout.*?>

<VBox xmlns:fx="http://javafx.com/fxml">
    <!-- 使用RiskBadge -->
    <RiskBadge fx:id="riskBadge"/>

    <!-- 使用SearchBox -->
    <SearchBox fx:id="searchBox" promptText="搜索..."/>

    <!-- 使用DataCard -->
    <DataCard fx:id="dataCard" title="总计" value="0"/>
</VBox>
```

### 在Controller中操作

```java
@FXML
private RiskBadge riskBadge;

@FXML
private SearchBox searchBox;

@FXML
private DataCard dataCard;

@Override
public void initialize(URL location, ResourceBundle resources) {
    // 设置风险等级
    riskBadge.setRiskLevel(RiskLevel.HIGH);

    // 设置搜索事件
    searchBox.setOnSearch(this::handleSearch);

    // 更新卡片数据
    dataCard.setValue(100);
    dataCard.setType("success");
}

private void handleSearch(String keyword) {
    // 处理搜索
}
```

---

## 完整示例

### 仪表盘页面示例

```java
import com.petition.view.component.DataCard;
import javafx.scene.layout.HBox;

public class DashboardExample {
    public void createDashboard() {
        // 创建统计卡片
        DataCard totalCard = new DataCard("总人数", "156", "全部记录");
        DataCard highRiskCard = new DataCard("高危人员", "28");
        DataCard mediumRiskCard = new DataCard("中危人员", "45");
        DataCard lowRiskCard = new DataCard("低危人员", "83");

        // 设置卡片类型
        highRiskCard.setType("danger");
        mediumRiskCard.setType("warning");
        lowRiskCard.setType("success");

        // 添加到布局
        HBox statsContainer = new HBox(20);
        statsContainer.getChildren().addAll(
            totalCard, highRiskCard, mediumRiskCard, lowRiskCard
        );
    }
}
```

---

## 注意事项

1. **CSS样式**：确保在场景中加载了 `main.css` 样式表
2. **模块导出**：如果使用Java模块系统，需要在 `module-info.java` 中导出组件包
3. **属性绑定**：组件支持JavaFX属性绑定，可以与数据模型双向绑定
4. **事件处理**：SearchBox使用Consumer函数式接口，支持Lambda表达式

---

*文档版本：v1.0*
*最后更新：2025年11月24日*
