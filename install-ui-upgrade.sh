#!/bin/bash
# ============================================================================
#  上访人员管理系统 - UI现代化升级一键安装脚本
#  
#  使用方法:
#    1. 将此脚本放到项目根目录 (PetitionersFileManagementSystem/)
#    2. 执行: bash install-ui-upgrade.sh
#    3. 脚本会自动更新文件并提交到Git
# ============================================================================

set -e

echo ""
echo "╔══════════════════════════════════════════════════════════════╗"
echo "║     🎨 上访人员管理系统 - UI现代化升级安装程序              ║"
echo "╚══════════════════════════════════════════════════════════════╝"
echo ""

# 检查是否在项目根目录
if [ ! -f "pom.xml" ]; then
    echo "❌ 错误: 请在项目根目录运行此脚本"
    echo "   当前目录: $(pwd)"
    exit 1
fi

echo "📍 项目目录: $(pwd)"
echo ""

# 创建备份
echo "📦 步骤 1/5: 备份原有文件..."
mkdir -p .backup/css .backup/util
[ -f "src/main/resources/css/main.css" ] && cp src/main/resources/css/main.css .backup/css/ 2>/dev/null || true
[ -d "src/main/java/com/petition/util" ] && cp src/main/java/com/petition/util/*.java .backup/util/ 2>/dev/null || true
echo "   ✅ 备份完成 -> .backup/"

# 确保目录存在
echo ""
echo "📁 步骤 2/5: 创建目录结构..."
mkdir -p src/main/resources/css
mkdir -p src/main/java/com/petition/util
echo "   ✅ 目录准备完成"

# 写入CSS主题文件
echo ""
echo "🎨 步骤 3/5: 写入样式文件..."

cat > src/main/resources/css/main.css << 'CSSEOF'
/*
 * ============================================================================
 *  上访人员重点监控信息管理系统 - 现代化UI主题 v2.0
 *  设计理念: 科技感玻璃态 + 渐变光效 + 流畅动画
 * ============================================================================
 */

/* 全局根样式 - 配色系统 */
.root {
    -fx-background-color: #0f172a;
    -fx-font-family: "Microsoft YaHei UI", "PingFang SC", "Segoe UI", sans-serif;
    -fx-font-size: 14px;
    -fx-text-fill: #f8fafc;
}

/* 主窗口布局 */
.main-container {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #0f172a, #1e293b);
}

/* 顶部标题栏 */
.header-bar {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #1e293bee, #0f172aee);
    -fx-padding: 12 24 12 24;
    -fx-border-color: transparent transparent #334155 transparent;
    -fx-border-width: 0 0 1 0;
    -fx-effect: dropshadow(gaussian, #00000060, 10, 0, 0, 2);
}

.header-logo {
    -fx-effect: dropshadow(gaussian, #3b82f680, 8, 0.5, 0, 0);
}

.header-title {
    -fx-font-size: 20px;
    -fx-font-weight: bold;
    -fx-text-fill: #f8fafc;
}

.header-subtitle {
    -fx-font-size: 12px;
    -fx-text-fill: #94a3b8;
}

/* 侧边导航栏 */
.sidebar {
    -fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #1e293bf0, #0f172af0);
    -fx-padding: 16 0 16 0;
    -fx-pref-width: 240;
    -fx-min-width: 60;
    -fx-border-color: transparent #334155 transparent transparent;
    -fx-border-width: 0 1 0 0;
}

/* 导航按钮 */
.nav-button {
    -fx-background-color: transparent;
    -fx-background-radius: 12;
    -fx-padding: 14 20 14 20;
    -fx-cursor: hand;
    -fx-text-fill: #94a3b8;
    -fx-font-size: 14px;
    -fx-alignment: CENTER_LEFT;
    -fx-graphic-text-gap: 14;
    -fx-pref-height: 48;
}

.nav-button:hover {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #3b82f620, transparent);
    -fx-text-fill: #e2e8f0;
}

.nav-button-active {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #3b82f630, #3b82f610);
    -fx-text-fill: #3b82f6;
    -fx-font-weight: bold;
    -fx-border-color: #3b82f6 transparent transparent transparent;
    -fx-border-width: 0 0 0 3;
    -fx-effect: dropshadow(gaussian, #3b82f640, 12, 0, 0, 0);
}

/* 内容区域 */
.content-area {
    -fx-background-color: transparent;
    -fx-padding: 24;
}

/* 页面标题 */
.page-title {
    -fx-font-size: 28px;
    -fx-font-weight: bold;
    -fx-text-fill: #f8fafc;
}

.page-subtitle {
    -fx-font-size: 14px;
    -fx-text-fill: #94a3b8;
    -fx-padding: 4 0 0 0;
}

/* 卡片组件 */
.card {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1e293bcc, #0f172acc);
    -fx-background-radius: 16;
    -fx-border-color: #334155;
    -fx-border-width: 1;
    -fx-border-radius: 16;
    -fx-padding: 20;
    -fx-effect: dropshadow(gaussian, #00000040, 16, 0, 0, 4);
}

.card:hover {
    -fx-border-color: #475569;
    -fx-effect: dropshadow(gaussian, #00000060, 20, 0, 0, 6);
}

/* 统计卡片 */
.stat-card {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1e293bdd, #0f172add);
    -fx-background-radius: 16;
    -fx-border-color: #334155;
    -fx-border-width: 1;
    -fx-border-radius: 16;
    -fx-padding: 24;
    -fx-pref-width: 220;
    -fx-min-height: 140;
    -fx-effect: dropshadow(gaussian, #00000040, 12, 0, 0, 4);
    -fx-cursor: hand;
}

.stat-card:hover {
    -fx-scale-x: 1.02;
    -fx-scale-y: 1.02;
}

.stat-card-primary {
    -fx-border-color: #3b82f640;
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1e3a5fdd, #0f172add);
}

.stat-card-primary:hover {
    -fx-border-color: #3b82f680;
    -fx-effect: dropshadow(gaussian, #3b82f640, 20, 0, 0, 0);
}

.stat-card-danger {
    -fx-border-color: #ef444440;
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #3f1d1ddd, #0f172add);
}

.stat-card-danger:hover {
    -fx-border-color: #ef444480;
    -fx-effect: dropshadow(gaussian, #ef444440, 20, 0, 0, 0);
}

.stat-card-warning {
    -fx-border-color: #f59e0b40;
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #3f2d0fdd, #0f172add);
}

.stat-card-warning:hover {
    -fx-border-color: #f59e0b80;
    -fx-effect: dropshadow(gaussian, #f59e0b40, 20, 0, 0, 0);
}

.stat-card-success {
    -fx-border-color: #10b98140;
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #0f2f24dd, #0f172add);
}

.stat-card-success:hover {
    -fx-border-color: #10b98180;
    -fx-effect: dropshadow(gaussian, #10b98140, 20, 0, 0, 0);
}

.stat-value {
    -fx-font-size: 36px;
    -fx-font-weight: bold;
    -fx-text-fill: #f8fafc;
}

.stat-label {
    -fx-font-size: 13px;
    -fx-text-fill: #94a3b8;
    -fx-padding: 4 0 0 0;
}

/* 按钮系统 */
.btn {
    -fx-padding: 10 20 10 20;
    -fx-background-radius: 10;
    -fx-border-radius: 10;
    -fx-font-size: 14px;
    -fx-font-weight: 600;
    -fx-cursor: hand;
    -fx-effect: dropshadow(gaussian, #00000020, 4, 0, 0, 2);
}

.btn:hover {
    -fx-effect: dropshadow(gaussian, #00000040, 8, 0, 0, 4);
}

.btn:pressed {
    -fx-scale-x: 0.97;
    -fx-scale-y: 0.97;
}

.btn-primary {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #3b82f6, #2563eb);
    -fx-text-fill: white;
    -fx-border-color: transparent;
}

.btn-primary:hover {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #60a5fa, #3b82f6);
    -fx-effect: dropshadow(gaussian, #3b82f660, 12, 0, 0, 0);
}

.btn-secondary {
    -fx-background-color: transparent;
    -fx-text-fill: #94a3b8;
    -fx-border-color: #475569;
    -fx-border-width: 1.5;
}

.btn-secondary:hover {
    -fx-background-color: #33415520;
    -fx-text-fill: #e2e8f0;
    -fx-border-color: #64748b;
}

.btn-success {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #10b981, #059669);
    -fx-text-fill: white;
}

.btn-success:hover {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #34d399, #10b981);
    -fx-effect: dropshadow(gaussian, #10b98160, 12, 0, 0, 0);
}

.btn-danger {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #ef4444, #dc2626);
    -fx-text-fill: white;
}

.btn-danger:hover {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #f87171, #ef4444);
    -fx-effect: dropshadow(gaussian, #ef444460, 12, 0, 0, 0);
}

.btn-warning {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #f59e0b, #d97706);
    -fx-text-fill: white;
}

.btn-ghost {
    -fx-background-color: transparent;
    -fx-text-fill: #94a3b8;
    -fx-border-color: transparent;
}

.btn-ghost:hover {
    -fx-background-color: #33415530;
    -fx-text-fill: #e2e8f0;
}

.btn-icon {
    -fx-padding: 10;
    -fx-background-radius: 10;
    -fx-min-width: 40;
    -fx-max-width: 40;
    -fx-min-height: 40;
    -fx-max-height: 40;
}

.btn-lg {
    -fx-padding: 14 28 14 28;
    -fx-font-size: 16px;
    -fx-background-radius: 12;
}

.btn-sm {
    -fx-padding: 6 12 6 12;
    -fx-font-size: 12px;
    -fx-background-radius: 8;
}

/* 输入框系统 */
.text-field, .text-area, .password-field {
    -fx-background-color: #0f172a;
    -fx-background-radius: 10;
    -fx-border-color: #334155;
    -fx-border-width: 1.5;
    -fx-border-radius: 10;
    -fx-padding: 12 16 12 16;
    -fx-text-fill: #f8fafc;
    -fx-prompt-text-fill: #64748b;
    -fx-font-size: 14px;
}

.text-field:hover, .text-area:hover {
    -fx-border-color: #475569;
    -fx-background-color: #1e293b;
}

.text-field:focused, .text-area:focused {
    -fx-border-color: #3b82f6;
    -fx-background-color: #1e293b;
    -fx-effect: dropshadow(gaussian, #3b82f640, 8, 0, 0, 0);
}

.text-field-error {
    -fx-border-color: #ef4444;
}

.text-field-error:focused {
    -fx-effect: dropshadow(gaussian, #ef444440, 8, 0, 0, 0);
}

/* 下拉框 */
.combo-box {
    -fx-background-color: #0f172a;
    -fx-background-radius: 10;
    -fx-border-color: #334155;
    -fx-border-width: 1.5;
    -fx-border-radius: 10;
    -fx-padding: 4;
}

.combo-box:hover {
    -fx-border-color: #475569;
    -fx-background-color: #1e293b;
}

.combo-box:focused {
    -fx-border-color: #3b82f6;
    -fx-effect: dropshadow(gaussian, #3b82f640, 8, 0, 0, 0);
}

.combo-box .list-cell {
    -fx-background-color: transparent;
    -fx-text-fill: #f8fafc;
    -fx-padding: 8 12 8 12;
}

.combo-box-popup .list-view {
    -fx-background-color: #1e293b;
    -fx-background-radius: 10;
    -fx-border-color: #334155;
    -fx-border-radius: 10;
    -fx-effect: dropshadow(gaussian, #00000060, 16, 0, 0, 8);
}

.combo-box-popup .list-cell {
    -fx-background-color: transparent;
    -fx-text-fill: #e2e8f0;
    -fx-padding: 10 16 10 16;
}

.combo-box-popup .list-cell:hover {
    -fx-background-color: #33415580;
}

.combo-box-popup .list-cell:selected {
    -fx-background-color: #3b82f630;
    -fx-text-fill: #3b82f6;
}

.combo-box .arrow-button {
    -fx-background-color: transparent;
    -fx-padding: 0 12 0 0;
}

.combo-box .arrow {
    -fx-background-color: #64748b;
}

/* 表格 */
.table-view {
    -fx-background-color: transparent;
    -fx-border-color: #334155;
    -fx-border-radius: 12;
    -fx-background-radius: 12;
}

.table-view .column-header-background {
    -fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #1e293b, #0f172a);
    -fx-background-radius: 12 12 0 0;
}

.table-view .column-header {
    -fx-background-color: transparent;
    -fx-border-color: transparent transparent #334155 transparent;
    -fx-border-width: 0 0 1 0;
    -fx-padding: 14 16 14 16;
}

.table-view .column-header .label {
    -fx-font-size: 13px;
    -fx-font-weight: bold;
    -fx-text-fill: #94a3b8;
}

.table-view .table-row-cell {
    -fx-background-color: transparent;
    -fx-border-color: transparent transparent #33415540 transparent;
    -fx-border-width: 0 0 1 0;
    -fx-table-cell-border-color: transparent;
}

.table-view .table-row-cell:odd {
    -fx-background-color: #0f172a40;
}

.table-view .table-row-cell:hover {
    -fx-background-color: #33415560;
}

.table-view .table-row-cell:selected {
    -fx-background-color: #3b82f630;
}

.table-view .table-cell {
    -fx-padding: 12 16 12 16;
    -fx-text-fill: #e2e8f0;
    -fx-font-size: 13px;
    -fx-alignment: CENTER_LEFT;
}

/* 滚动条 */
.scroll-bar {
    -fx-background-color: transparent;
}

.scroll-bar .track {
    -fx-background-color: #1e293b40;
    -fx-background-radius: 4;
}

.scroll-bar .thumb {
    -fx-background-color: #475569;
    -fx-background-radius: 4;
}

.scroll-bar .thumb:hover {
    -fx-background-color: #64748b;
}

.scroll-bar .thumb:pressed {
    -fx-background-color: #3b82f6;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-background-color: transparent;
    -fx-shape: "";
}

/* 标签页 */
.tab-pane {
    -fx-background-color: transparent;
}

.tab-pane .tab-header-background {
    -fx-background-color: transparent;
}

.tab-pane .headers-region {
    -fx-background-color: #1e293b;
    -fx-background-radius: 12;
    -fx-padding: 4;
}

.tab-pane .tab {
    -fx-background-color: transparent;
    -fx-background-radius: 10;
    -fx-padding: 10 20 10 20;
    -fx-cursor: hand;
}

.tab-pane .tab .tab-label {
    -fx-text-fill: #94a3b8;
    -fx-font-size: 14px;
}

.tab-pane .tab:hover {
    -fx-background-color: #33415540;
}

.tab-pane .tab:hover .tab-label {
    -fx-text-fill: #e2e8f0;
}

.tab-pane .tab:selected {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #3b82f6, #2563eb);
    -fx-effect: dropshadow(gaussian, #3b82f640, 8, 0, 0, 0);
}

.tab-pane .tab:selected .tab-label {
    -fx-text-fill: white;
    -fx-font-weight: bold;
}

.tab-pane .tab-content-area {
    -fx-background-color: transparent;
    -fx-padding: 20 0 0 0;
}

/* 对话框 */
.dialog-pane {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1e293b, #0f172a);
    -fx-background-radius: 16;
    -fx-border-color: #334155;
    -fx-border-radius: 16;
    -fx-effect: dropshadow(gaussian, #00000080, 32, 0, 0, 8);
}

.dialog-pane .header-panel {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #1e293b, #0f172a);
    -fx-background-radius: 16 16 0 0;
    -fx-padding: 24;
    -fx-border-color: transparent transparent #334155 transparent;
}

.dialog-pane .header-panel .label {
    -fx-font-size: 18px;
    -fx-font-weight: bold;
    -fx-text-fill: #f8fafc;
}

.dialog-pane .content {
    -fx-padding: 24;
}

.dialog-pane .button-bar {
    -fx-padding: 16 24 24 24;
}

/* 表单样式 */
.form-dialog {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1e293bf0, #0f172af0);
}

.form-header {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #3b82f620, transparent);
    -fx-padding: 24 28 24 28;
    -fx-border-color: transparent transparent #334155 transparent;
    -fx-border-width: 0 0 1 0;
}

.form-header-title {
    -fx-font-size: 22px;
    -fx-font-weight: bold;
    -fx-text-fill: #f8fafc;
}

.form-header-subtitle {
    -fx-font-size: 13px;
    -fx-text-fill: #94a3b8;
}

.form-section {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1e293b80, #0f172a80);
    -fx-background-radius: 16;
    -fx-border-color: #334155;
    -fx-border-width: 1;
    -fx-border-radius: 16;
    -fx-padding: 24;
}

.form-section-title {
    -fx-font-size: 16px;
    -fx-font-weight: bold;
    -fx-text-fill: #3b82f6;
}

.form-label {
    -fx-font-size: 13px;
    -fx-font-weight: 600;
    -fx-text-fill: #94a3b8;
}

.form-footer {
    -fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #0f172a, #0f172af0);
    -fx-padding: 16 28 16 28;
    -fx-border-color: #334155 transparent transparent transparent;
    -fx-border-width: 1 0 0 0;
}

/* 风险等级徽章 */
.risk-badge {
    -fx-padding: 4 12 4 12;
    -fx-background-radius: 20;
    -fx-font-size: 12px;
    -fx-font-weight: bold;
    -fx-alignment: CENTER;
}

.risk-badge-extreme {
    -fx-background-color: #dc262630;
    -fx-text-fill: #fca5a5;
    -fx-border-color: #dc262650;
    -fx-border-width: 1;
    -fx-border-radius: 20;
}

.risk-badge-high {
    -fx-background-color: #ef444430;
    -fx-text-fill: #fca5a5;
    -fx-border-color: #ef444450;
    -fx-border-width: 1;
    -fx-border-radius: 20;
}

.risk-badge-medium {
    -fx-background-color: #f59e0b30;
    -fx-text-fill: #fcd34d;
    -fx-border-color: #f59e0b50;
    -fx-border-width: 1;
    -fx-border-radius: 20;
}

.risk-badge-low {
    -fx-background-color: #10b98130;
    -fx-text-fill: #6ee7b7;
    -fx-border-color: #10b98150;
    -fx-border-width: 1;
    -fx-border-radius: 20;
}

/* 风险卡片选择器 */
.risk-card {
    -fx-background-color: #1e293b;
    -fx-background-radius: 12;
    -fx-border-width: 2;
    -fx-border-radius: 12;
    -fx-padding: 16 24 16 24;
    -fx-cursor: hand;
    -fx-min-width: 100;
}

.risk-card:hover {
    -fx-scale-x: 1.05;
    -fx-scale-y: 1.05;
}

.risk-card-low {
    -fx-border-color: #10b98150;
    -fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #0f2f2480, #1e293b);
}

.risk-card-low.selected {
    -fx-border-color: #10b981;
    -fx-effect: dropshadow(gaussian, #10b98160, 16, 0, 0, 0);
}

.risk-card-medium {
    -fx-border-color: #f59e0b50;
    -fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #3f2d0f80, #1e293b);
}

.risk-card-medium.selected {
    -fx-border-color: #f59e0b;
    -fx-effect: dropshadow(gaussian, #f59e0b60, 16, 0, 0, 0);
}

.risk-card-high {
    -fx-border-color: #ef444450;
    -fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #3f1d1d80, #1e293b);
}

.risk-card-high.selected {
    -fx-border-color: #ef4444;
    -fx-effect: dropshadow(gaussian, #ef444460, 16, 0, 0, 0);
}

.risk-card-extreme {
    -fx-border-color: #dc262650;
    -fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #4a111180, #1e293b);
}

.risk-card-extreme.selected {
    -fx-border-color: #dc2626;
    -fx-effect: dropshadow(gaussian, #dc262660, 16, 0, 0, 0);
}

/* Tooltip */
.tooltip {
    -fx-background-color: #1e293bf0;
    -fx-background-radius: 8;
    -fx-border-color: #334155;
    -fx-border-radius: 8;
    -fx-text-fill: #e2e8f0;
    -fx-font-size: 12px;
    -fx-padding: 8 12 8 12;
    -fx-effect: dropshadow(gaussian, #00000060, 8, 0, 0, 4);
}

/* 进度条 */
.progress-bar {
    -fx-pref-height: 8;
    -fx-background-color: #1e293b;
    -fx-background-radius: 4;
}

.progress-bar .track {
    -fx-background-color: #1e293b;
    -fx-background-radius: 4;
}

.progress-bar .bar {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #3b82f6, #8b5cf6);
    -fx-background-radius: 4;
}

/* 复选框 */
.check-box {
    -fx-text-fill: #e2e8f0;
    -fx-font-size: 14px;
}

.check-box .box {
    -fx-background-color: #0f172a;
    -fx-background-radius: 6;
    -fx-border-color: #475569;
    -fx-border-width: 2;
    -fx-border-radius: 6;
}

.check-box:hover .box {
    -fx-border-color: #3b82f6;
}

.check-box:selected .box {
    -fx-background-color: #3b82f6;
    -fx-border-color: #3b82f6;
}

.check-box:selected .mark {
    -fx-background-color: white;
}

/* Spinner */
.spinner {
    -fx-background-color: #0f172a;
    -fx-background-radius: 10;
    -fx-border-color: #334155;
    -fx-border-width: 1.5;
    -fx-border-radius: 10;
}

.spinner:hover {
    -fx-border-color: #475569;
}

.spinner:focused {
    -fx-border-color: #3b82f6;
}

.spinner .text-field {
    -fx-background-color: transparent;
    -fx-border-color: transparent;
    -fx-padding: 8 12 8 12;
    -fx-text-fill: #f8fafc;
    -fx-font-size: 14px;
    -fx-font-weight: 600;
}

.spinner .increment-arrow-button, .spinner .decrement-arrow-button {
    -fx-background-color: #1e293b;
    -fx-background-radius: 0 8 8 0;
    -fx-padding: 8;
    -fx-cursor: hand;
}

.spinner .increment-arrow-button:hover, .spinner .decrement-arrow-button:hover {
    -fx-background-color: #334155;
}

.spinner .increment-arrow, .spinner .decrement-arrow {
    -fx-background-color: #94a3b8;
}

/* 分隔线 */
.separator .line {
    -fx-border-color: #334155;
    -fx-border-width: 1 0 0 0;
}

/* 搜索栏 */
.search-bar {
    -fx-background-color: #1e293b;
    -fx-background-radius: 12;
    -fx-padding: 16;
    -fx-spacing: 16;
    -fx-border-color: #334155;
    -fx-border-radius: 12;
}

/* 分页 */
.pagination-bar {
    -fx-background-color: transparent;
    -fx-padding: 16 0 0 0;
    -fx-alignment: CENTER;
    -fx-spacing: 8;
}

/* 图表 */
.chart {
    -fx-background-color: transparent;
}

.chart-plot-background {
    -fx-background-color: transparent;
}

.chart-vertical-grid-lines, .chart-horizontal-grid-lines {
    -fx-stroke: #33415540;
}

.chart-legend {
    -fx-background-color: transparent;
}

.chart-legend-item {
    -fx-text-fill: #94a3b8;
}

.axis {
    -fx-tick-label-fill: #94a3b8;
}

.axis-label {
    -fx-text-fill: #94a3b8;
}

/* 工具类 */
.text-xs { -fx-font-size: 11px; }
.text-sm { -fx-font-size: 12px; }
.text-base { -fx-font-size: 14px; }
.text-lg { -fx-font-size: 16px; }
.text-xl { -fx-font-size: 18px; }
.text-2xl { -fx-font-size: 22px; }
.text-3xl { -fx-font-size: 28px; }

.font-bold { -fx-font-weight: bold; }
.font-semibold { -fx-font-weight: 600; }

.text-primary { -fx-text-fill: #f8fafc; }
.text-secondary { -fx-text-fill: #cbd5e1; }
.text-muted { -fx-text-fill: #64748b; }
.text-accent { -fx-text-fill: #3b82f6; }
.text-success { -fx-text-fill: #10b981; }
.text-warning { -fx-text-fill: #f59e0b; }
.text-danger { -fx-text-fill: #ef4444; }
CSSEOF

echo "   ✅ main.css 写入完成"

# 写入AnimationUtil.java
echo ""
echo "⚡ 步骤 4/5: 写入Java工具类..."

cat > src/main/java/com/petition/util/AnimationUtil.java << 'JAVAEOF'
package com.petition.util;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * UI动画工具类
 * 提供丰富的动画效果用于增强用户体验
 */
public class AnimationUtil {

    public static final Duration FAST = Duration.millis(150);
    public static final Duration NORMAL = Duration.millis(250);
    public static final Duration SLOW = Duration.millis(400);

    /** 淡入动画 */
    public static void fadeIn(Node node) {
        fadeIn(node, NORMAL, null);
    }

    public static void fadeIn(Node node, Duration duration, Runnable onFinished) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setInterpolator(Interpolator.EASE_OUT);
        if (onFinished != null) ft.setOnFinished(e -> onFinished.run());
        ft.play();
    }

    /** 淡出动画 */
    public static void fadeOut(Node node, Duration duration, Runnable onFinished) {
        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(node.getOpacity());
        ft.setToValue(0);
        ft.setInterpolator(Interpolator.EASE_IN);
        if (onFinished != null) ft.setOnFinished(e -> onFinished.run());
        ft.play();
    }

    /** 缩放进入 */
    public static void scaleIn(Node node) {
        scaleIn(node, NORMAL, null);
    }

    public static void scaleIn(Node node, Duration duration, Runnable onFinished) {
        node.setScaleX(0.8);
        node.setScaleY(0.8);
        node.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        ScaleTransition st = new ScaleTransition(duration, node);
        st.setFromX(0.8); st.setFromY(0.8);
        st.setToX(1.0); st.setToY(1.0);
        st.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0); ft.setToValue(1);

        pt.getChildren().addAll(st, ft);
        if (onFinished != null) pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    /** 弹性反馈 */
    public static void bounce(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
        st.setFromX(1.0); st.setFromY(1.0);
        st.setToX(0.95); st.setToY(0.95);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    /** 抖动动画(验证失败) */
    public static void shake(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), node);
        tt.setFromX(0); tt.setByX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.setOnFinished(e -> node.setTranslateX(0));
        tt.play();
    }

    /** 从右侧滑入 */
    public static void slideInFromRight(Node node) {
        node.setTranslateX(50);
        node.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        TranslateTransition tt = new TranslateTransition(NORMAL, node);
        tt.setFromX(50); tt.setToX(0);

        FadeTransition ft = new FadeTransition(NORMAL, node);
        ft.setFromValue(0); ft.setToValue(1);

        pt.getChildren().addAll(tt, ft);
        pt.play();
    }

    /** 交错淡入 */
    public static void staggerFadeIn(Node... nodes) {
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            node.setOpacity(0);
            FadeTransition ft = new FadeTransition(NORMAL, node);
            ft.setFromValue(0); ft.setToValue(1);
            ft.setDelay(Duration.millis(i * 50));
            ft.play();
        }
    }

    /** 弹窗打开动画 */
    public static void dialogOpen(Stage stage) {
        if (stage.getScene() == null) return;
        Node root = stage.getScene().getRoot();
        root.setScaleX(0.9); root.setScaleY(0.9);
        root.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        ScaleTransition st = new ScaleTransition(NORMAL, root);
        st.setFromX(0.9); st.setFromY(0.9);
        st.setToX(1.0); st.setToY(1.0);

        FadeTransition ft = new FadeTransition(NORMAL, root);
        ft.setFromValue(0); ft.setToValue(1);

        pt.getChildren().addAll(st, ft);
        pt.setDelay(Duration.millis(50));
        pt.play();
    }

    /** 弹窗关闭动画 */
    public static void dialogClose(Stage stage, Runnable onFinished) {
        if (stage.getScene() == null) {
            if (onFinished != null) onFinished.run();
            return;
        }
        Node root = stage.getScene().getRoot();

        ParallelTransition pt = new ParallelTransition();
        ScaleTransition st = new ScaleTransition(FAST, root);
        st.setToX(0.9); st.setToY(0.9);

        FadeTransition ft = new FadeTransition(FAST, root);
        ft.setToValue(0);

        pt.getChildren().addAll(st, ft);
        pt.setOnFinished(e -> { if (onFinished != null) onFinished.run(); });
        pt.play();
    }

    /** 添加悬停缩放 */
    public static void addHoverScale(Node node, double scale) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(FAST, node);
            st.setToX(scale); st.setToY(scale);
            st.play();
        });
        node.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(FAST, node);
            st.setToX(1.0); st.setToY(1.0);
            st.play();
        });
    }
}
JAVAEOF

echo "   ✅ AnimationUtil.java 写入完成"

cat > src/main/java/com/petition/util/DialogUtil.java << 'JAVA2EOF'
package com.petition.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

/**
 * 现代化弹窗工具类
 */
public class DialogUtil {

    private static final String MAIN_CSS = "/css/main.css";

    /** 创建表单弹窗 */
    public static <T> Stage createFormDialog(
            Window owner, String fxmlPath, String title,
            double width, double height, Consumer<T> onController) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = DialogUtil.class.getResource(fxmlPath);
            if (url == null) throw new IOException("找不到: " + fxmlPath);
            loader.setLocation(url);
            Parent root = loader.load();

            if (onController != null) {
                T ctrl = loader.getController();
                onController.accept(ctrl);
            }

            Scene scene = new Scene(root, width, height);
            scene.setFill(Color.TRANSPARENT);
            
            URL css = DialogUtil.class.getResource(MAIN_CSS);
            if (css != null) scene.getStylesheets().add(css.toExternalForm());

            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setMinWidth(width * 0.8);
            stage.setMinHeight(height * 0.8);

            if (owner != null) {
                stage.setX(owner.getX() + (owner.getWidth() - width) / 2);
                stage.setY(owner.getY() + (owner.getHeight() - height) / 2);
            }

            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE) closeWithAnimation(stage);
            });

            stage.setOnShown(e -> AnimationUtil.dialogOpen(stage));
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("加载失败", e.getMessage());
            return null;
        }
    }

    /** 关闭弹窗(带动画) */
    public static void closeWithAnimation(Stage stage) {
        AnimationUtil.dialogClose(stage, stage::close);
    }

    /** 确认对话框 */
    public static void showConfirmDialog(String title, String msg, Runnable onConfirm, Runnable onCancel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        applyDarkTheme(alert);
        alert.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK && onConfirm != null) onConfirm.run();
            else if (onCancel != null) onCancel.run();
        });
    }

    /** 信息提示 */
    public static void showInfoAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        applyDarkTheme(alert);
        alert.showAndWait();
    }

    /** 成功提示 */
    public static void showSuccessAlert(String title, String msg) {
        showInfoAlert("✅ " + title, msg);
    }

    /** 错误提示 */
    public static void showErrorAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("❌ " + title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        applyDarkTheme(alert);
        alert.showAndWait();
    }

    /** 警告提示 */
    public static void showWarningAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("⚠️ " + title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        applyDarkTheme(alert);
        alert.showAndWait();
    }

    /** 删除确认 */
    public static void showDeleteConfirmDialog(String name, Runnable onConfirm) {
        showConfirmDialog("⚠️ 确认删除", 
            "确定要删除「" + name + "」吗？\n此操作不可恢复！", 
            onConfirm, null);
    }

    private static void applyDarkTheme(Alert alert) {
        DialogPane pane = alert.getDialogPane();
        pane.setStyle("-fx-background-color: #1e293b; -fx-border-color: #334155;");
        try {
            URL css = DialogUtil.class.getResource(MAIN_CSS);
            if (css != null) pane.getStylesheets().add(css.toExternalForm());
        } catch (Exception ignored) {}
    }
}
JAVA2EOF

echo "   ✅ DialogUtil.java 写入完成"

# Git提交
echo ""
echo "🚀 步骤 5/5: Git提交..."

git add -A

git commit -m "feat: UI全面现代化升级 - 科技感玻璃态设计

🎨 视觉设计升级:
- 全新深空灰主题(#0f172a, #1e293b)
- 玻璃态卡片效果(半透明+边框发光)
- 科技蓝渐变配色(#3b82f6 → #2563eb)
- 统一的配色变量系统

✨ 新增组件样式:
- 现代化按钮(渐变、发光、点击反馈)
- 玻璃态输入框(聚焦发光效果)
- 风险等级徽章(彩色圆角标签)
- 统计卡片(渐变背景+悬停效果)
- 标签页、表格、下拉框等全面升级

🎬 动画效果:
- 新增AnimationUtil动画工具类
- 淡入淡出、缩放、滑动动画
- 交错列表动画(stagger效果)
- 悬停缩放效果
- 弹窗打开/关闭动画
- 表单验证抖动效果

💬 弹窗系统:
- 新增DialogUtil弹窗工具类
- 统一的表单弹窗创建方法
- 暗色主题确认/提示对话框

📁 文件变更:
- 重写 css/main.css
- 新增 util/AnimationUtil.java
- 新增 util/DialogUtil.java

🤖 Generated with Claude

Co-Authored-By: Claude <noreply@anthropic.com>"

echo "   ✅ Git提交完成"

# 推送
echo ""
echo "📤 推送到远程仓库..."
git push origin master

echo ""
echo "╔══════════════════════════════════════════════════════════════╗"
echo "║     ✅ UI现代化升级完成！                                   ║"
echo "╚══════════════════════════════════════════════════════════════╝"
echo ""
echo "📋 更新内容:"
echo "   • src/main/resources/css/main.css (主题样式)"
echo "   • src/main/java/com/petition/util/AnimationUtil.java"
echo "   • src/main/java/com/petition/util/DialogUtil.java"
echo ""
echo "🔨 建议执行: mvn compile 验证编译"
echo ""
