#!/bin/bash

#===============================================================================
# 上访人员重点监控信息管理系统 - UI升级脚本 v2.0
# 
# 功能：
#   1. 修复侧边栏乱码问题（使用Unicode字符替代图标字体）
#   2. 全新科技感仪表盘界面
#   3. 引导式（向导式）人员新增/编辑弹窗
#   4. 下滑式人员详情查看弹窗
#   5. 照片管理功能（支持多照片、画廊浏览）
#
# 使用方法：
#   1. 将此脚本放到项目根目录（包含pom.xml的目录）
#   2. 运行: bash install-ui-upgrade-v2.sh
#===============================================================================

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# 打印带颜色的消息
print_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
print_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
print_warning() { echo -e "${YELLOW}[WARNING]${NC} $1"; }
print_error() { echo -e "${RED}[ERROR]${NC} $1"; }

echo ""
echo -e "${CYAN}╔════════════════════════════════════════════════════════════════╗${NC}"
echo -e "${CYAN}║     上访人员监控系统 - UI升级安装程序 v2.0                     ║${NC}"
echo -e "${CYAN}║                                                                ║${NC}"
echo -e "${CYAN}║  • 科技感仪表盘升级                                            ║${NC}"
echo -e "${CYAN}║  • 向导式表单弹窗                                              ║${NC}"
echo -e "${CYAN}║  • 下滑式详情查看                                              ║${NC}"
echo -e "${CYAN}║  • 照片管理系统                                                ║${NC}"
echo -e "${CYAN}╚════════════════════════════════════════════════════════════════╝${NC}"
echo ""

# 检查是否在项目根目录
if [ ! -f "pom.xml" ]; then
    print_error "请在项目根目录（包含pom.xml）运行此脚本"
    exit 1
fi

print_success "检测到项目根目录"

# 创建备份
BACKUP_DIR=".backup/$(date +%Y%m%d_%H%M%S)"
mkdir -p "$BACKUP_DIR"
print_info "创建备份目录: $BACKUP_DIR"

# 备份现有文件
backup_if_exists() {
    if [ -f "$1" ]; then
        mkdir -p "$BACKUP_DIR/$(dirname $1)"
        cp "$1" "$BACKUP_DIR/$1"
        print_info "已备份: $1"
    fi
}

# 创建目录结构
print_info "创建目录结构..."
mkdir -p src/main/resources/css
mkdir -p src/main/java/com/petition/util
mkdir -p src/main/java/com/petition/model
mkdir -p src/main/java/com/petition/dao
mkdir -p src/main/java/com/petition/service
mkdir -p src/main/java/com/petition/controller

# 备份现有文件
backup_if_exists "src/main/resources/css/main.css"
backup_if_exists "src/main/java/com/petition/util/AnimationUtil.java"
backup_if_exists "src/main/java/com/petition/util/DialogUtil.java"

print_info "开始写入升级文件..."

#===============================================================================
# CSS 主题文件
#===============================================================================
cat > src/main/resources/css/main.css << 'CSS_EOF'
/*
 * ============================================================================
 *  上访人员重点监控信息管理系统 - 现代化UI主题 v3.0
 *  设计理念: 赛博朋克 + 数据可视化 + 流畅交互
 * ============================================================================
 */

/* ==================== 全局根样式 ==================== */
.root {
    -fx-background-color: #0a0e1a;
    -fx-font-family: "Microsoft YaHei UI", "PingFang SC", "Segoe UI", sans-serif;
    -fx-font-size: 14px;
    -fx-text-fill: #e2e8f0;
}

/* ==================== 主窗口布局 ==================== */
.main-container {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #0a0e1a, #1a1f35);
}

/* ==================== 顶部标题栏 ==================== */
.header-bar {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #0d1224ee, #1a1f35ee);
    -fx-padding: 16 28 16 28;
    -fx-border-color: transparent transparent #2d3a5a transparent;
    -fx-border-width: 0 0 1 0;
    -fx-effect: dropshadow(gaussian, #00d4ff20, 20, 0, 0, 4);
}

.header-logo {
    -fx-effect: dropshadow(gaussian, #00d4ff80, 12, 0.6, 0, 0);
}

.header-title {
    -fx-font-size: 22px;
    -fx-font-weight: bold;
    -fx-text-fill: linear-gradient(from 0% 50% to 100% 50%, #ffffff, #00d4ff);
}

.header-subtitle {
    -fx-font-size: 12px;
    -fx-text-fill: #64748b;
    -fx-padding: 2 0 0 0;
}

/* ==================== 侧边导航栏 ==================== */
.sidebar {
    -fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #0d1224f8, #0a0e1af8);
    -fx-padding: 20 0 20 0;
    -fx-pref-width: 260;
    -fx-min-width: 60;
    -fx-border-color: transparent #1e293b transparent transparent;
    -fx-border-width: 0 1 0 0;
}

.sidebar-header {
    -fx-padding: 0 20 20 20;
    -fx-border-color: transparent transparent #1e293b transparent;
    -fx-border-width: 0 0 1 0;
}

.nav-section-title {
    -fx-font-size: 11px;
    -fx-font-weight: bold;
    -fx-text-fill: #475569;
    -fx-padding: 16 20 8 20;
}

/* 导航按钮 - 使用文字图标避免乱码 */
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
    -fx-max-width: 10000;
}

.nav-button:hover {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #00d4ff15, transparent);
    -fx-text-fill: #e2e8f0;
}

.nav-button-active {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #00d4ff25, #00d4ff05);
    -fx-text-fill: #00d4ff;
    -fx-font-weight: bold;
    -fx-border-color: #00d4ff transparent transparent transparent;
    -fx-border-width: 0 0 0 3;
    -fx-border-radius: 0;
    -fx-background-radius: 0 12 12 0;
    -fx-effect: dropshadow(gaussian, #00d4ff30, 16, 0, 0, 0);
}

/* 导航图标容器 */
.nav-icon {
    -fx-min-width: 24;
    -fx-min-height: 24;
    -fx-alignment: CENTER;
    -fx-font-size: 16px;
}

/* ==================== 仪表盘 - 科技感升级 ==================== */
.dashboard-container {
    -fx-background-color: transparent;
    -fx-padding: 0;
}

/* 仪表盘顶部概览条 */
.dashboard-overview-bar {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #0d122480, #1a1f3580);
    -fx-background-radius: 20;
    -fx-border-color: #2d3a5a;
    -fx-border-width: 1;
    -fx-border-radius: 20;
    -fx-padding: 24 32 24 32;
    -fx-effect: dropshadow(gaussian, #00000040, 20, 0, 0, 8);
}

/* 数据指标卡片 - 赛博朋克风格 */
.metric-card {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #0d1224cc, #1a1f35cc);
    -fx-background-radius: 16;
    -fx-border-color: #2d3a5a;
    -fx-border-width: 1;
    -fx-border-radius: 16;
    -fx-padding: 20 24 20 24;
    -fx-pref-width: 200;
    -fx-min-height: 120;
    -fx-cursor: hand;
}

.metric-card:hover {
    -fx-border-color: #00d4ff60;
    -fx-effect: dropshadow(gaussian, #00d4ff30, 20, 0, 0, 0);
    -fx-scale-x: 1.02;
    -fx-scale-y: 1.02;
}

.metric-card-primary {
    -fx-border-color: #00d4ff40;
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #00d4ff10, #0d1224cc);
}

.metric-card-primary:hover {
    -fx-border-color: #00d4ff;
    -fx-effect: dropshadow(gaussian, #00d4ff50, 24, 0, 0, 0);
}

.metric-card-danger {
    -fx-border-color: #ff006640;
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #ff006610, #0d1224cc);
}

.metric-card-danger:hover {
    -fx-border-color: #ff0066;
    -fx-effect: dropshadow(gaussian, #ff006650, 24, 0, 0, 0);
}

.metric-card-warning {
    -fx-border-color: #ffaa0040;
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #ffaa0010, #0d1224cc);
}

.metric-card-warning:hover {
    -fx-border-color: #ffaa00;
    -fx-effect: dropshadow(gaussian, #ffaa0050, 24, 0, 0, 0);
}

.metric-card-success {
    -fx-border-color: #00ff8840;
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #00ff8810, #0d1224cc);
}

.metric-card-success:hover {
    -fx-border-color: #00ff88;
    -fx-effect: dropshadow(gaussian, #00ff8850, 24, 0, 0, 0);
}

.metric-value {
    -fx-font-size: 42px;
    -fx-font-weight: bold;
    -fx-text-fill: #ffffff;
}

.metric-value-primary { -fx-text-fill: #00d4ff; }
.metric-value-danger { -fx-text-fill: #ff0066; }
.metric-value-warning { -fx-text-fill: #ffaa00; }
.metric-value-success { -fx-text-fill: #00ff88; }

.metric-label {
    -fx-font-size: 13px;
    -fx-text-fill: #64748b;
    -fx-padding: 8 0 0 0;
}

.metric-trend {
    -fx-font-size: 12px;
    -fx-padding: 4 8 4 8;
    -fx-background-radius: 12;
}

.metric-trend-up {
    -fx-background-color: #00ff8820;
    -fx-text-fill: #00ff88;
}

.metric-trend-down {
    -fx-background-color: #ff006620;
    -fx-text-fill: #ff0066;
}

/* 数据面板 - 全息投影风格 */
.data-panel {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #0d1224dd, #1a1f35dd);
    -fx-background-radius: 20;
    -fx-border-color: #2d3a5a;
    -fx-border-width: 1;
    -fx-border-radius: 20;
    -fx-padding: 24;
    -fx-effect: dropshadow(gaussian, #00000060, 24, 0, 0, 8);
}

.data-panel-header {
    -fx-padding: 0 0 16 0;
    -fx-border-color: transparent transparent #2d3a5a transparent;
    -fx-border-width: 0 0 1 0;
}

.data-panel-title {
    -fx-font-size: 18px;
    -fx-font-weight: bold;
    -fx-text-fill: #f8fafc;
}

.data-panel-subtitle {
    -fx-font-size: 12px;
    -fx-text-fill: #64748b;
}

/* 实时数据网格线效果 */
.grid-overlay {
    -fx-background-color: transparent;
    -fx-border-color: #00d4ff08;
    -fx-border-width: 1;
}

/* 脉冲动画指示器 */
.pulse-indicator {
    -fx-background-color: #00ff88;
    -fx-background-radius: 50;
    -fx-min-width: 8;
    -fx-min-height: 8;
    -fx-effect: dropshadow(gaussian, #00ff8880, 8, 0.5, 0, 0);
}

.pulse-indicator-warning {
    -fx-background-color: #ffaa00;
    -fx-effect: dropshadow(gaussian, #ffaa0080, 8, 0.5, 0, 0);
}

.pulse-indicator-danger {
    -fx-background-color: #ff0066;
    -fx-effect: dropshadow(gaussian, #ff006680, 8, 0.5, 0, 0);
}

/* 快捷操作卡片 */
.quick-action-card {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1a1f35, #0d1224);
    -fx-background-radius: 16;
    -fx-border-color: #2d3a5a;
    -fx-border-width: 1;
    -fx-border-radius: 16;
    -fx-padding: 20;
    -fx-cursor: hand;
    -fx-alignment: CENTER;
}

.quick-action-card:hover {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #00d4ff15, #1a1f35);
    -fx-border-color: #00d4ff60;
    -fx-effect: dropshadow(gaussian, #00d4ff30, 16, 0, 0, 0);
}

.quick-action-icon {
    -fx-font-size: 32px;
    -fx-text-fill: #00d4ff;
}

.quick-action-label {
    -fx-font-size: 13px;
    -fx-text-fill: #94a3b8;
    -fx-padding: 8 0 0 0;
}

/* ==================== 内容区域 ==================== */
.content-area {
    -fx-background-color: transparent;
    -fx-padding: 28;
}

/* 页面标题 */
.page-title {
    -fx-font-size: 28px;
    -fx-font-weight: bold;
    -fx-text-fill: #f8fafc;
}

.page-subtitle {
    -fx-font-size: 14px;
    -fx-text-fill: #64748b;
    -fx-padding: 4 0 0 0;
}

/* ==================== 通用卡片 ==================== */
.card {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #0d1224cc, #1a1f35cc);
    -fx-background-radius: 16;
    -fx-border-color: #2d3a5a;
    -fx-border-width: 1;
    -fx-border-radius: 16;
    -fx-padding: 20;
    -fx-effect: dropshadow(gaussian, #00000040, 16, 0, 0, 4);
}

.card:hover {
    -fx-border-color: #3d4a6a;
    -fx-effect: dropshadow(gaussian, #00000060, 20, 0, 0, 6);
}

/* ==================== 按钮系统 ==================== */
.btn {
    -fx-padding: 12 24 12 24;
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
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #00d4ff, #0099cc);
    -fx-text-fill: #0a0e1a;
    -fx-border-color: transparent;
}

.btn-primary:hover {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #33ddff, #00bbee);
    -fx-effect: dropshadow(gaussian, #00d4ff60, 16, 0, 0, 0);
}

.btn-secondary {
    -fx-background-color: transparent;
    -fx-text-fill: #94a3b8;
    -fx-border-color: #3d4a6a;
    -fx-border-width: 1.5;
}

.btn-secondary:hover {
    -fx-background-color: #2d3a5a30;
    -fx-text-fill: #e2e8f0;
    -fx-border-color: #4d5a7a;
}

.btn-success {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #00ff88, #00cc6a);
    -fx-text-fill: #0a0e1a;
}

.btn-success:hover {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #33ff99, #00ee7a);
    -fx-effect: dropshadow(gaussian, #00ff8860, 16, 0, 0, 0);
}

.btn-danger {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #ff0066, #cc0052);
    -fx-text-fill: white;
}

.btn-danger:hover {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #ff3388, #ee0066);
    -fx-effect: dropshadow(gaussian, #ff006660, 16, 0, 0, 0);
}

.btn-warning {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #ffaa00, #cc8800);
    -fx-text-fill: #0a0e1a;
}

.btn-ghost {
    -fx-background-color: transparent;
    -fx-text-fill: #94a3b8;
    -fx-border-color: transparent;
}

.btn-ghost:hover {
    -fx-background-color: #2d3a5a40;
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
    -fx-padding: 16 32 16 32;
    -fx-font-size: 16px;
    -fx-background-radius: 12;
}

.btn-sm {
    -fx-padding: 8 16 8 16;
    -fx-font-size: 12px;
    -fx-background-radius: 8;
}

/* ==================== 输入框系统 ==================== */
.text-field, .text-area, .password-field {
    -fx-background-color: #0a0e1a;
    -fx-background-radius: 10;
    -fx-border-color: #2d3a5a;
    -fx-border-width: 1.5;
    -fx-border-radius: 10;
    -fx-padding: 12 16 12 16;
    -fx-text-fill: #f8fafc;
    -fx-prompt-text-fill: #4d5a7a;
    -fx-font-size: 14px;
}

.text-field:hover, .text-area:hover {
    -fx-border-color: #3d4a6a;
    -fx-background-color: #0d1224;
}

.text-field:focused, .text-area:focused {
    -fx-border-color: #00d4ff;
    -fx-background-color: #0d1224;
    -fx-effect: dropshadow(gaussian, #00d4ff40, 8, 0, 0, 0);
}

.text-field-error {
    -fx-border-color: #ff0066;
}

.text-field-error:focused {
    -fx-effect: dropshadow(gaussian, #ff006640, 8, 0, 0, 0);
}

.text-field-success {
    -fx-border-color: #00ff88;
}

/* ==================== 下拉框 ==================== */
.combo-box {
    -fx-background-color: #0a0e1a;
    -fx-background-radius: 10;
    -fx-border-color: #2d3a5a;
    -fx-border-width: 1.5;
    -fx-border-radius: 10;
    -fx-padding: 4;
}

.combo-box:hover {
    -fx-border-color: #3d4a6a;
    -fx-background-color: #0d1224;
}

.combo-box:focused {
    -fx-border-color: #00d4ff;
    -fx-effect: dropshadow(gaussian, #00d4ff40, 8, 0, 0, 0);
}

.combo-box .list-cell {
    -fx-background-color: transparent;
    -fx-text-fill: #f8fafc;
    -fx-padding: 8 12 8 12;
}

.combo-box-popup .list-view {
    -fx-background-color: #0d1224;
    -fx-background-radius: 10;
    -fx-border-color: #2d3a5a;
    -fx-border-radius: 10;
    -fx-effect: dropshadow(gaussian, #00000080, 20, 0, 0, 8);
}

.combo-box-popup .list-cell {
    -fx-background-color: transparent;
    -fx-text-fill: #e2e8f0;
    -fx-padding: 12 16 12 16;
}

.combo-box-popup .list-cell:hover {
    -fx-background-color: #00d4ff20;
}

.combo-box-popup .list-cell:selected {
    -fx-background-color: #00d4ff30;
    -fx-text-fill: #00d4ff;
}

.combo-box .arrow-button {
    -fx-background-color: transparent;
    -fx-padding: 0 12 0 0;
}

.combo-box .arrow {
    -fx-background-color: #64748b;
}

/* ==================== 表格 ==================== */
.table-view {
    -fx-background-color: transparent;
    -fx-border-color: #2d3a5a;
    -fx-border-radius: 16;
    -fx-background-radius: 16;
}

.table-view .column-header-background {
    -fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #1a1f35, #0d1224);
    -fx-background-radius: 16 16 0 0;
}

.table-view .column-header {
    -fx-background-color: transparent;
    -fx-border-color: transparent transparent #2d3a5a transparent;
    -fx-border-width: 0 0 1 0;
    -fx-padding: 16 16 16 16;
}

.table-view .column-header .label {
    -fx-font-size: 13px;
    -fx-font-weight: bold;
    -fx-text-fill: #64748b;
}

.table-view .table-row-cell {
    -fx-background-color: transparent;
    -fx-border-color: transparent transparent #2d3a5a20 transparent;
    -fx-border-width: 0 0 1 0;
    -fx-table-cell-border-color: transparent;
}

.table-view .table-row-cell:odd {
    -fx-background-color: #0a0e1a40;
}

.table-view .table-row-cell:hover {
    -fx-background-color: #00d4ff15;
}

.table-view .table-row-cell:selected {
    -fx-background-color: #00d4ff25;
}

.table-view .table-cell {
    -fx-padding: 14 16 14 16;
    -fx-text-fill: #e2e8f0;
    -fx-font-size: 13px;
    -fx-alignment: CENTER_LEFT;
}

/* 表格中的头像列 */
.table-avatar {
    -fx-background-radius: 50;
    -fx-border-radius: 50;
    -fx-border-color: #00d4ff40;
    -fx-border-width: 2;
    -fx-min-width: 40;
    -fx-min-height: 40;
    -fx-max-width: 40;
    -fx-max-height: 40;
    -fx-effect: dropshadow(gaussian, #00d4ff30, 4, 0, 0, 0);
}

.table-avatar:hover {
    -fx-border-color: #00d4ff;
    -fx-effect: dropshadow(gaussian, #00d4ff60, 8, 0, 0, 0);
    -fx-cursor: hand;
}

/* ==================== 滚动条 ==================== */
.scroll-bar {
    -fx-background-color: transparent;
}

.scroll-bar .track {
    -fx-background-color: #0d122440;
    -fx-background-radius: 4;
}

.scroll-bar .thumb {
    -fx-background-color: #3d4a6a;
    -fx-background-radius: 4;
}

.scroll-bar .thumb:hover {
    -fx-background-color: #4d5a7a;
}

.scroll-bar .thumb:pressed {
    -fx-background-color: #00d4ff;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-background-color: transparent;
    -fx-shape: "";
}

/* ==================== 向导式弹窗 ==================== */
.wizard-dialog {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #0d1224f8, #1a1f35f8);
    -fx-background-radius: 24;
    -fx-border-color: #2d3a5a;
    -fx-border-width: 1;
    -fx-border-radius: 24;
    -fx-effect: dropshadow(gaussian, #000000a0, 48, 0, 0, 16);
}

/* 向导头部 */
.wizard-header {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #00d4ff15, transparent);
    -fx-padding: 28 32 28 32;
    -fx-border-color: transparent transparent #2d3a5a transparent;
    -fx-border-width: 0 0 1 0;
}

.wizard-title {
    -fx-font-size: 24px;
    -fx-font-weight: bold;
    -fx-text-fill: #f8fafc;
}

.wizard-subtitle {
    -fx-font-size: 14px;
    -fx-text-fill: #64748b;
    -fx-padding: 4 0 0 0;
}

/* 步骤指示器 */
.wizard-steps {
    -fx-padding: 24 32 24 32;
    -fx-background-color: #0a0e1a40;
    -fx-spacing: 0;
}

.wizard-step {
    -fx-alignment: CENTER;
    -fx-spacing: 8;
}

.wizard-step-circle {
    -fx-min-width: 36;
    -fx-min-height: 36;
    -fx-max-width: 36;
    -fx-max-height: 36;
    -fx-background-color: #1a1f35;
    -fx-background-radius: 50;
    -fx-border-color: #2d3a5a;
    -fx-border-width: 2;
    -fx-border-radius: 50;
    -fx-alignment: CENTER;
}

.wizard-step-circle-active {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #00d4ff, #0099cc);
    -fx-border-color: #00d4ff;
    -fx-effect: dropshadow(gaussian, #00d4ff60, 12, 0, 0, 0);
}

.wizard-step-circle-completed {
    -fx-background-color: #00ff88;
    -fx-border-color: #00ff88;
}

.wizard-step-number {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: #64748b;
}

.wizard-step-number-active {
    -fx-text-fill: #0a0e1a;
}

.wizard-step-label {
    -fx-font-size: 12px;
    -fx-text-fill: #64748b;
    -fx-padding: 8 0 0 0;
}

.wizard-step-label-active {
    -fx-text-fill: #00d4ff;
    -fx-font-weight: bold;
}

.wizard-step-line {
    -fx-background-color: #2d3a5a;
    -fx-pref-height: 2;
    -fx-pref-width: 60;
}

.wizard-step-line-completed {
    -fx-background-color: #00ff88;
}

/* 向导内容区 */
.wizard-content {
    -fx-padding: 32;
    -fx-spacing: 24;
}

.wizard-section {
    -fx-background-color: #0a0e1a60;
    -fx-background-radius: 16;
    -fx-border-color: #2d3a5a;
    -fx-border-width: 1;
    -fx-border-radius: 16;
    -fx-padding: 24;
}

.wizard-section-title {
    -fx-font-size: 16px;
    -fx-font-weight: bold;
    -fx-text-fill: #00d4ff;
    -fx-padding: 0 0 16 0;
}

.wizard-field-label {
    -fx-font-size: 13px;
    -fx-font-weight: 600;
    -fx-text-fill: #94a3b8;
    -fx-padding: 0 0 8 0;
}

.wizard-field-hint {
    -fx-font-size: 11px;
    -fx-text-fill: #4d5a7a;
    -fx-padding: 4 0 0 0;
}

/* 向导底部 */
.wizard-footer {
    -fx-background-color: linear-gradient(from 50% 0% to 50% 100%, #0d1224, #0a0e1a);
    -fx-padding: 20 32 20 32;
    -fx-border-color: #2d3a5a transparent transparent transparent;
    -fx-border-width: 1 0 0 0;
}

/* ==================== 下滑式查看弹窗 ==================== */
.slide-viewer {
    -fx-background-color: #0a0e1af0;
}

.slide-viewer-header {
    -fx-background-color: linear-gradient(from 50% 100% to 50% 0%, #0a0e1a, transparent);
    -fx-padding: 20 24 20 24;
}

.slide-viewer-close {
    -fx-background-color: #2d3a5a80;
    -fx-background-radius: 50;
    -fx-min-width: 40;
    -fx-min-height: 40;
    -fx-cursor: hand;
}

.slide-viewer-close:hover {
    -fx-background-color: #ff006680;
}

.slide-viewer-content {
    -fx-padding: 0 32 32 32;
}

.slide-viewer-section {
    -fx-background-color: #0d122480;
    -fx-background-radius: 20;
    -fx-border-color: #2d3a5a;
    -fx-border-width: 1;
    -fx-border-radius: 20;
    -fx-padding: 24;
    -fx-spacing: 16;
}

.slide-viewer-section-title {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: #00d4ff;
    -fx-padding: 0 0 12 0;
    -fx-border-color: transparent transparent #2d3a5a transparent;
    -fx-border-width: 0 0 1 0;
}

.slide-viewer-field {
    -fx-spacing: 4;
}

.slide-viewer-field-label {
    -fx-font-size: 12px;
    -fx-text-fill: #64748b;
}

.slide-viewer-field-value {
    -fx-font-size: 14px;
    -fx-text-fill: #e2e8f0;
}

/* 照片画廊 */
.photo-gallery {
    -fx-background-color: #0a0e1a;
    -fx-background-radius: 16;
    -fx-padding: 16;
    -fx-spacing: 12;
}

.photo-gallery-main {
    -fx-background-color: #0d1224;
    -fx-background-radius: 12;
    -fx-border-color: #2d3a5a;
    -fx-border-width: 1;
    -fx-border-radius: 12;
    -fx-min-height: 300;
    -fx-alignment: CENTER;
}

.photo-gallery-main-image {
    -fx-effect: dropshadow(gaussian, #00d4ff20, 16, 0, 0, 0);
}

.photo-gallery-thumbnails {
    -fx-spacing: 8;
    -fx-padding: 8 0 0 0;
}

.photo-thumbnail {
    -fx-background-color: #1a1f35;
    -fx-background-radius: 8;
    -fx-border-color: #2d3a5a;
    -fx-border-width: 2;
    -fx-border-radius: 8;
    -fx-min-width: 60;
    -fx-min-height: 60;
    -fx-max-width: 60;
    -fx-max-height: 60;
    -fx-cursor: hand;
}

.photo-thumbnail:hover {
    -fx-border-color: #00d4ff60;
}

.photo-thumbnail-active {
    -fx-border-color: #00d4ff;
    -fx-effect: dropshadow(gaussian, #00d4ff40, 8, 0, 0, 0);
}

.photo-nav-button {
    -fx-background-color: #0a0e1a80;
    -fx-background-radius: 50;
    -fx-min-width: 48;
    -fx-min-height: 48;
    -fx-text-fill: #e2e8f0;
    -fx-font-size: 20px;
    -fx-cursor: hand;
}

.photo-nav-button:hover {
    -fx-background-color: #00d4ff40;
}

.photo-counter {
    -fx-background-color: #0a0e1acc;
    -fx-background-radius: 20;
    -fx-padding: 8 16 8 16;
    -fx-text-fill: #e2e8f0;
    -fx-font-size: 13px;
}

/* ==================== 风险等级徽章 ==================== */
.risk-badge {
    -fx-padding: 6 14 6 14;
    -fx-background-radius: 20;
    -fx-font-size: 12px;
    -fx-font-weight: bold;
    -fx-alignment: CENTER;
}

.risk-badge-extreme {
    -fx-background-color: #ff006630;
    -fx-text-fill: #ff6699;
    -fx-border-color: #ff006650;
    -fx-border-width: 1;
    -fx-border-radius: 20;
}

.risk-badge-high {
    -fx-background-color: #ff444430;
    -fx-text-fill: #ff7777;
    -fx-border-color: #ff444450;
    -fx-border-width: 1;
    -fx-border-radius: 20;
}

.risk-badge-medium {
    -fx-background-color: #ffaa0030;
    -fx-text-fill: #ffcc44;
    -fx-border-color: #ffaa0050;
    -fx-border-width: 1;
    -fx-border-radius: 20;
}

.risk-badge-low {
    -fx-background-color: #00ff8830;
    -fx-text-fill: #66ffaa;
    -fx-border-color: #00ff8850;
    -fx-border-width: 1;
    -fx-border-radius: 20;
}

/* ==================== 工具提示 ==================== */
.tooltip {
    -fx-background-color: #1a1f35f0;
    -fx-background-radius: 8;
    -fx-border-color: #2d3a5a;
    -fx-border-radius: 8;
    -fx-text-fill: #e2e8f0;
    -fx-font-size: 12px;
    -fx-padding: 10 14 10 14;
    -fx-effect: dropshadow(gaussian, #00000080, 12, 0, 0, 4);
}

/* ==================== 进度条 ==================== */
.progress-bar {
    -fx-pref-height: 6;
    -fx-background-color: #1a1f35;
    -fx-background-radius: 3;
}

.progress-bar .track {
    -fx-background-color: #1a1f35;
    -fx-background-radius: 3;
}

.progress-bar .bar {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #00d4ff, #00ff88);
    -fx-background-radius: 3;
}

/* ==================== 复选框 ==================== */
.check-box {
    -fx-text-fill: #e2e8f0;
    -fx-font-size: 14px;
}

.check-box .box {
    -fx-background-color: #0a0e1a;
    -fx-background-radius: 6;
    -fx-border-color: #3d4a6a;
    -fx-border-width: 2;
    -fx-border-radius: 6;
}

.check-box:hover .box {
    -fx-border-color: #00d4ff;
}

.check-box:selected .box {
    -fx-background-color: #00d4ff;
    -fx-border-color: #00d4ff;
}

.check-box:selected .mark {
    -fx-background-color: #0a0e1a;
}

/* ==================== 分隔线 ==================== */
.separator .line {
    -fx-border-color: #2d3a5a;
    -fx-border-width: 1 0 0 0;
}

/* ==================== 对话框 ==================== */
.dialog-pane {
    -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #0d1224, #1a1f35);
    -fx-background-radius: 20;
    -fx-border-color: #2d3a5a;
    -fx-border-radius: 20;
    -fx-effect: dropshadow(gaussian, #000000a0, 40, 0, 0, 12);
}

.dialog-pane .header-panel {
    -fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #00d4ff15, transparent);
    -fx-background-radius: 20 20 0 0;
    -fx-padding: 24;
    -fx-border-color: transparent transparent #2d3a5a transparent;
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

/* ==================== 搜索栏 ==================== */
.search-bar {
    -fx-background-color: #0d1224;
    -fx-background-radius: 12;
    -fx-padding: 16;
    -fx-spacing: 16;
    -fx-border-color: #2d3a5a;
    -fx-border-radius: 12;
}

/* ==================== 工具类 ==================== */
.text-xs { -fx-font-size: 11px; }
.text-sm { -fx-font-size: 12px; }
.text-base { -fx-font-size: 14px; }
.text-lg { -fx-font-size: 16px; }
.text-xl { -fx-font-size: 18px; }
.text-2xl { -fx-font-size: 22px; }
.text-3xl { -fx-font-size: 28px; }
.text-4xl { -fx-font-size: 36px; }

.font-bold { -fx-font-weight: bold; }
.font-semibold { -fx-font-weight: 600; }

.text-primary { -fx-text-fill: #f8fafc; }
.text-secondary { -fx-text-fill: #94a3b8; }
.text-muted { -fx-text-fill: #4d5a7a; }
.text-accent { -fx-text-fill: #00d4ff; }
.text-success { -fx-text-fill: #00ff88; }
.text-warning { -fx-text-fill: #ffaa00; }
.text-danger { -fx-text-fill: #ff0066; }

.bg-transparent { -fx-background-color: transparent; }
.cursor-pointer { -fx-cursor: hand; }

CSS_EOF
print_success "已创建: src/main/resources/css/main.css"

#===============================================================================
# AnimationUtil.java - 动画工具类
#===============================================================================
cat > src/main/java/com/petition/util/AnimationUtil.java << 'JAVA_EOF'
package com.petition.util;

import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

/**
 * UI动画工具类 v2.0
 * 提供丰富的科技感动画效果
 */
public class AnimationUtil {

    // 时间常量
    public static final Duration INSTANT = Duration.millis(100);
    public static final Duration FAST = Duration.millis(150);
    public static final Duration NORMAL = Duration.millis(250);
    public static final Duration SLOW = Duration.millis(400);
    public static final Duration SLOWER = Duration.millis(600);

    // ==================== 基础动画 ====================

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
    public static void fadeOut(Node node) {
        fadeOut(node, NORMAL, null);
    }

    public static void fadeOut(Node node, Duration duration, Runnable onFinished) {
        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(node.getOpacity());
        ft.setToValue(0);
        ft.setInterpolator(Interpolator.EASE_IN);
        if (onFinished != null) ft.setOnFinished(e -> onFinished.run());
        ft.play();
    }

    // ==================== 缩放动画 ====================

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
        st.setFromX(0.8);
        st.setFromY(0.8);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0);
        ft.setToValue(1);

        pt.getChildren().addAll(st, ft);
        if (onFinished != null) pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    /** 缩放退出 */
    public static void scaleOut(Node node, Duration duration, Runnable onFinished) {
        ParallelTransition pt = new ParallelTransition();
        
        ScaleTransition st = new ScaleTransition(duration, node);
        st.setToX(0.8);
        st.setToY(0.8);
        st.setInterpolator(Interpolator.EASE_IN);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setToValue(0);

        pt.getChildren().addAll(st, ft);
        if (onFinished != null) pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    /** 弹性放大 */
    public static void popIn(Node node) {
        node.setScaleX(0);
        node.setScaleY(0);
        node.setOpacity(1);

        ScaleTransition st = new ScaleTransition(NORMAL, node);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setInterpolator(Interpolator.EASE_OUT);
        st.play();
    }

    /** 弹性反馈 */
    public static void bounce(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(0.95);
        st.setToY(0.95);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    /** 脉冲动画 */
    public static void pulse(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(300), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.05);
        st.setToY(1.05);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.setInterpolator(Interpolator.EASE_BOTH);
        st.play();
    }

    // ==================== 滑动动画 ====================

    /** 从右侧滑入 */
    public static void slideInFromRight(Node node) {
        slideInFromRight(node, NORMAL, null);
    }

    public static void slideInFromRight(Node node, Duration duration, Runnable onFinished) {
        node.setTranslateX(50);
        node.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        
        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setFromX(50);
        tt.setToX(0);
        tt.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0);
        ft.setToValue(1);

        pt.getChildren().addAll(tt, ft);
        if (onFinished != null) pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    /** 从左侧滑入 */
    public static void slideInFromLeft(Node node, Duration duration, Runnable onFinished) {
        node.setTranslateX(-50);
        node.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        
        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setFromX(-50);
        tt.setToX(0);
        tt.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0);
        ft.setToValue(1);

        pt.getChildren().addAll(tt, ft);
        if (onFinished != null) pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    /** 从顶部滑入 */
    public static void slideInFromTop(Node node, Duration duration, Runnable onFinished) {
        node.setTranslateY(-30);
        node.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        
        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setFromY(-30);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0);
        ft.setToValue(1);

        pt.getChildren().addAll(tt, ft);
        if (onFinished != null) pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    /** 从底部滑入 */
    public static void slideInFromBottom(Node node, Duration duration, Runnable onFinished) {
        node.setTranslateY(30);
        node.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        
        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setFromY(30);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0);
        ft.setToValue(1);

        pt.getChildren().addAll(tt, ft);
        if (onFinished != null) pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    /** 向右滑出 */
    public static void slideOutToRight(Node node, Duration duration, Runnable onFinished) {
        ParallelTransition pt = new ParallelTransition();
        
        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setToX(50);
        tt.setInterpolator(Interpolator.EASE_IN);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setToValue(0);

        pt.getChildren().addAll(tt, ft);
        pt.setOnFinished(e -> {
            node.setTranslateX(0);
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    /** 向左滑出 */
    public static void slideOutToLeft(Node node, Duration duration, Runnable onFinished) {
        ParallelTransition pt = new ParallelTransition();
        
        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setToX(-50);
        tt.setInterpolator(Interpolator.EASE_IN);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setToValue(0);

        pt.getChildren().addAll(tt, ft);
        pt.setOnFinished(e -> {
            node.setTranslateX(0);
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    // ==================== 交错动画 ====================

    /** 交错淡入 */
    public static void staggerFadeIn(Node... nodes) {
        staggerFadeIn(50, nodes);
    }

    public static void staggerFadeIn(int delayMs, Node... nodes) {
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            node.setOpacity(0);
            
            FadeTransition ft = new FadeTransition(NORMAL, node);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.setDelay(Duration.millis(i * delayMs));
            ft.play();
        }
    }

    /** 交错滑入(从右) */
    public static void staggerSlideIn(Node... nodes) {
        staggerSlideIn(60, nodes);
    }

    public static void staggerSlideIn(int delayMs, Node... nodes) {
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            node.setTranslateX(30);
            node.setOpacity(0);

            ParallelTransition pt = new ParallelTransition();
            
            TranslateTransition tt = new TranslateTransition(NORMAL, node);
            tt.setFromX(30);
            tt.setToX(0);
            tt.setInterpolator(Interpolator.EASE_OUT);

            FadeTransition ft = new FadeTransition(NORMAL, node);
            ft.setFromValue(0);
            ft.setToValue(1);

            pt.getChildren().addAll(tt, ft);
            pt.setDelay(Duration.millis(i * delayMs));
            pt.play();
        }
    }

    /** 交错缩放进入 */
    public static void staggerScaleIn(int delayMs, Node... nodes) {
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            node.setScaleX(0.8);
            node.setScaleY(0.8);
            node.setOpacity(0);

            ParallelTransition pt = new ParallelTransition();
            
            ScaleTransition st = new ScaleTransition(NORMAL, node);
            st.setFromX(0.8);
            st.setFromY(0.8);
            st.setToX(1.0);
            st.setToY(1.0);
            st.setInterpolator(Interpolator.EASE_OUT);

            FadeTransition ft = new FadeTransition(NORMAL, node);
            ft.setFromValue(0);
            ft.setToValue(1);

            pt.getChildren().addAll(st, ft);
            pt.setDelay(Duration.millis(i * delayMs));
            pt.play();
        }
    }

    // ==================== 特效动画 ====================

    /** 抖动动画(验证失败) */
    public static void shake(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), node);
        tt.setFromX(0);
        tt.setByX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.setOnFinished(e -> node.setTranslateX(0));
        tt.play();
    }

    /** 闪烁动画 */
    public static void flash(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(100), node);
        ft.setFromValue(1);
        ft.setToValue(0.3);
        ft.setCycleCount(4);
        ft.setAutoReverse(true);
        ft.play();
    }

    /** 心跳动画 */
    public static void heartbeat(Node node) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(node.scaleXProperty(), 1.0)),
            new KeyFrame(Duration.ZERO, new KeyValue(node.scaleYProperty(), 1.0)),
            new KeyFrame(Duration.millis(140), new KeyValue(node.scaleXProperty(), 1.1)),
            new KeyFrame(Duration.millis(140), new KeyValue(node.scaleYProperty(), 1.1)),
            new KeyFrame(Duration.millis(280), new KeyValue(node.scaleXProperty(), 1.0)),
            new KeyFrame(Duration.millis(280), new KeyValue(node.scaleYProperty(), 1.0)),
            new KeyFrame(Duration.millis(420), new KeyValue(node.scaleXProperty(), 1.1)),
            new KeyFrame(Duration.millis(420), new KeyValue(node.scaleYProperty(), 1.1)),
            new KeyFrame(Duration.millis(560), new KeyValue(node.scaleXProperty(), 1.0)),
            new KeyFrame(Duration.millis(560), new KeyValue(node.scaleYProperty(), 1.0))
        );
        timeline.play();
    }

    /** 旋转进入 */
    public static void rotateIn(Node node) {
        node.setRotate(-90);
        node.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        
        RotateTransition rt = new RotateTransition(SLOW, node);
        rt.setFromAngle(-90);
        rt.setToAngle(0);
        rt.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(SLOW, node);
        ft.setFromValue(0);
        ft.setToValue(1);

        pt.getChildren().addAll(rt, ft);
        pt.play();
    }

    // ==================== 弹窗动画 ====================

    /** 弹窗打开动画 */
    public static void dialogOpen(Stage stage) {
        if (stage.getScene() == null) return;
        Node root = stage.getScene().getRoot();
        
        root.setScaleX(0.9);
        root.setScaleY(0.9);
        root.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        
        ScaleTransition st = new ScaleTransition(NORMAL, root);
        st.setFromX(0.9);
        st.setFromY(0.9);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(NORMAL, root);
        ft.setFromValue(0);
        ft.setToValue(1);

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
        st.setToX(0.9);
        st.setToY(0.9);
        st.setInterpolator(Interpolator.EASE_IN);

        FadeTransition ft = new FadeTransition(FAST, root);
        ft.setToValue(0);

        pt.getChildren().addAll(st, ft);
        pt.setOnFinished(e -> {
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    /** 向导切换动画 - 下一步 */
    public static void wizardNext(Node currentPane, Node nextPane, Runnable onFinished) {
        nextPane.setTranslateX(100);
        nextPane.setOpacity(0);
        nextPane.setVisible(true);

        ParallelTransition pt = new ParallelTransition();

        // 当前面板滑出
        TranslateTransition ttOut = new TranslateTransition(NORMAL, currentPane);
        ttOut.setToX(-100);
        FadeTransition ftOut = new FadeTransition(NORMAL, currentPane);
        ftOut.setToValue(0);

        // 下一面板滑入
        TranslateTransition ttIn = new TranslateTransition(NORMAL, nextPane);
        ttIn.setFromX(100);
        ttIn.setToX(0);
        FadeTransition ftIn = new FadeTransition(NORMAL, nextPane);
        ftIn.setFromValue(0);
        ftIn.setToValue(1);

        pt.getChildren().addAll(ttOut, ftOut, ttIn, ftIn);
        pt.setOnFinished(e -> {
            currentPane.setVisible(false);
            currentPane.setTranslateX(0);
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    /** 向导切换动画 - 上一步 */
    public static void wizardPrev(Node currentPane, Node prevPane, Runnable onFinished) {
        prevPane.setTranslateX(-100);
        prevPane.setOpacity(0);
        prevPane.setVisible(true);

        ParallelTransition pt = new ParallelTransition();

        // 当前面板滑出
        TranslateTransition ttOut = new TranslateTransition(NORMAL, currentPane);
        ttOut.setToX(100);
        FadeTransition ftOut = new FadeTransition(NORMAL, currentPane);
        ftOut.setToValue(0);

        // 上一面板滑入
        TranslateTransition ttIn = new TranslateTransition(NORMAL, prevPane);
        ttIn.setFromX(-100);
        ttIn.setToX(0);
        FadeTransition ftIn = new FadeTransition(NORMAL, prevPane);
        ftIn.setFromValue(0);
        ftIn.setToValue(1);

        pt.getChildren().addAll(ttOut, ftOut, ttIn, ftIn);
        pt.setOnFinished(e -> {
            currentPane.setVisible(false);
            currentPane.setTranslateX(0);
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    // ==================== 照片切换动画 ====================

    /** 照片淡入切换 */
    public static void crossFade(Node oldNode, Node newNode, Duration duration, Runnable onFinished) {
        newNode.setOpacity(0);
        
        ParallelTransition pt = new ParallelTransition();
        
        FadeTransition ftOut = new FadeTransition(duration, oldNode);
        ftOut.setToValue(0);
        
        FadeTransition ftIn = new FadeTransition(duration, newNode);
        ftIn.setFromValue(0);
        ftIn.setToValue(1);
        
        pt.getChildren().addAll(ftOut, ftIn);
        pt.setOnFinished(e -> {
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    /** 照片缩放切换 */
    public static void zoomTransition(Node oldNode, Node newNode, Runnable onFinished) {
        newNode.setScaleX(1.2);
        newNode.setScaleY(1.2);
        newNode.setOpacity(0);
        
        ParallelTransition pt = new ParallelTransition();
        
        // 旧图缩小淡出
        ScaleTransition stOut = new ScaleTransition(NORMAL, oldNode);
        stOut.setToX(0.8);
        stOut.setToY(0.8);
        FadeTransition ftOut = new FadeTransition(NORMAL, oldNode);
        ftOut.setToValue(0);
        
        // 新图放大淡入
        ScaleTransition stIn = new ScaleTransition(NORMAL, newNode);
        stIn.setFromX(1.2);
        stIn.setFromY(1.2);
        stIn.setToX(1.0);
        stIn.setToY(1.0);
        FadeTransition ftIn = new FadeTransition(NORMAL, newNode);
        ftIn.setFromValue(0);
        ftIn.setToValue(1);
        
        pt.getChildren().addAll(stOut, ftOut, stIn, ftIn);
        pt.setOnFinished(e -> {
            oldNode.setScaleX(1);
            oldNode.setScaleY(1);
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    // ==================== 悬停效果 ====================

    /** 添加悬停缩放效果 */
    public static void addHoverScale(Node node, double scale) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(FAST, node);
            st.setToX(scale);
            st.setToY(scale);
            st.play();
        });
        node.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(FAST, node);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

    /** 添加悬停发光效果 */
    public static void addHoverGlow(Node node) {
        node.setOnMouseEntered(e -> {
            node.setStyle(node.getStyle() + "-fx-effect: dropshadow(gaussian, #00d4ff60, 16, 0, 0, 0);");
        });
        node.setOnMouseExited(e -> {
            node.setStyle(node.getStyle().replace("-fx-effect: dropshadow(gaussian, #00d4ff60, 16, 0, 0, 0);", ""));
        });
    }

    // ==================== 数字动画 ====================

    /** 数字递增动画 */
    public static void animateNumber(javafx.scene.control.Label label, int from, int to, Duration duration) {
        Timeline timeline = new Timeline();
        int frames = 30;
        double step = (to - from) / (double) frames;
        
        for (int i = 0; i <= frames; i++) {
            final int value = (int) (from + step * i);
            KeyFrame kf = new KeyFrame(
                duration.multiply((double) i / frames),
                e -> label.setText(String.valueOf(value))
            );
            timeline.getKeyFrames().add(kf);
        }
        
        // 确保最终值准确
        timeline.getKeyFrames().add(new KeyFrame(duration, e -> label.setText(String.valueOf(to))));
        timeline.play();
    }

    // ==================== 加载动画 ====================

    /** 旋转加载动画 */
    public static RotateTransition createSpinner(Node node) {
        RotateTransition rt = new RotateTransition(Duration.seconds(1), node);
        rt.setFromAngle(0);
        rt.setToAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
        return rt;
    }

    /** 脉冲加载动画 */
    public static ScaleTransition createPulseLoader(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(600), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        return st;
    }
}

JAVA_EOF
print_success "已创建: AnimationUtil.java"

#===============================================================================
# NavIcon.java - 导航图标辅助类（解决乱码问题）
#===============================================================================
cat > src/main/java/com/petition/util/NavIcon.java << 'JAVA_EOF'
package com.petition.util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * 导航图标辅助类
 * 使用Unicode字符和SVG形状替代可能导致乱码的图标字体
 * 
 * 解决侧边栏"系统设置"等菜单项前出现乱码的问题
 */
public class NavIcon {

    // ==================== 导航图标定义 ====================
    
    // 使用通用Unicode符号，这些符号在大多数字体中都有支持
    public static final String DASHBOARD = "▣";      // 仪表盘
    public static final String PERSON = "◉";         // 人员
    public static final String SEARCH = "◎";         // 搜索
    public static final String CHART = "◐";          // 图表/统计
    public static final String DOCUMENT = "▤";       // 文档
    public static final String SETTINGS = "⚙";       // 设置
    public static final String USER = "◈";           // 用户
    public static final String LOGOUT = "◇";         // 退出
    public static final String ADD = "＋";           // 添加
    public static final String EDIT = "✎";           // 编辑
    public static final String DELETE = "✕";         // 删除
    public static final String VIEW = "◉";           // 查看
    public static final String EXPORT = "▲";         // 导出
    public static final String IMPORT = "▼";         // 导入
    public static final String ALERT = "◆";          // 警告
    public static final String INFO = "●";           // 信息
    public static final String SUCCESS = "✓";        // 成功
    public static final String ERROR = "✗";          // 错误
    public static final String PHOTO = "▢";          // 照片
    public static final String CALENDAR = "▦";       // 日历
    public static final String LOCATION = "◎";       // 位置
    public static final String PHONE = "☎";          // 电话
    public static final String MAIL = "✉";           // 邮件
    public static final String LOCK = "▣";           // 锁定
    public static final String UNLOCK = "▢";         // 解锁
    public static final String REFRESH = "↻";        // 刷新
    public static final String FILTER = "▽";         // 筛选
    public static final String SORT = "↕";           // 排序
    public static final String LIST = "≡";           // 列表
    public static final String GRID = "⊞";           // 网格
    public static final String HOME = "⌂";           // 首页
    public static final String BACK = "◀";           // 返回
    public static final String FORWARD = "▶";        // 前进
    public static final String UP = "▲";             // 上
    public static final String DOWN = "▼";           // 下
    public static final String LEFT = "◀";           // 左
    public static final String RIGHT = "▶";          // 右
    public static final String CLOSE = "✕";          // 关闭
    public static final String CHECK = "✓";          // 勾选
    public static final String UNCHECK = "○";        // 未勾选
    public static final String STAR = "★";           // 收藏
    public static final String STAR_EMPTY = "☆";     // 未收藏
    public static final String HEART = "♥";          // 喜欢
    public static final String MENU = "☰";           // 菜单
    public static final String MORE = "⋯";           // 更多
    public static final String EXPAND = "＋";        // 展开
    public static final String COLLAPSE = "－";      // 收起

    // ==================== 导航菜单项图标映射 ====================

    /**
     * 获取导航菜单图标
     * @param menuName 菜单名称
     * @return 对应的图标字符
     */
    public static String getNavIcon(String menuName) {
        return switch (menuName) {
            case "仪表盘", "首页", "概览" -> DASHBOARD;
            case "人员管理", "人员列表", "档案管理" -> PERSON;
            case "高级搜索", "搜索", "查询" -> SEARCH;
            case "数据统计", "统计分析", "报表" -> CHART;
            case "文档管理", "档案", "文件" -> DOCUMENT;
            case "系统设置", "设置", "配置" -> SETTINGS;
            case "用户管理", "账户", "用户" -> USER;
            case "退出登录", "注销", "退出" -> LOGOUT;
            case "新增", "添加", "创建" -> ADD;
            case "编辑", "修改" -> EDIT;
            case "删除", "移除" -> DELETE;
            case "查看", "详情" -> VIEW;
            case "导出", "下载" -> EXPORT;
            case "导入", "上传" -> IMPORT;
            case "预警", "警告", "告警" -> ALERT;
            case "照片", "图片", "相册" -> PHOTO;
            default -> "●"; // 默认圆点
        };
    }

    // ==================== 创建图标组件 ====================

    /**
     * 创建导航图标Label
     */
    public static Label createNavIconLabel(String menuName) {
        Label icon = new Label(getNavIcon(menuName));
        icon.getStyleClass().add("nav-icon");
        icon.setStyle("-fx-font-size: 16px; -fx-min-width: 24px; -fx-alignment: CENTER;");
        return icon;
    }

    /**
     * 创建图标容器（带背景）
     */
    public static StackPane createIconContainer(String iconChar, String bgColor, double size) {
        StackPane container = new StackPane();
        container.setMinSize(size, size);
        container.setMaxSize(size, size);
        container.setStyle(
            "-fx-background-color: " + bgColor + "20; " +
            "-fx-background-radius: " + (size / 2) + "; " +
            "-fx-border-color: " + bgColor + "40; " +
            "-fx-border-radius: " + (size / 2) + "; " +
            "-fx-border-width: 1;"
        );

        Label icon = new Label(iconChar);
        icon.setStyle("-fx-font-size: " + (size * 0.5) + "px; -fx-text-fill: " + bgColor + ";");
        
        container.getChildren().add(icon);
        return container;
    }

    /**
     * 创建彩色圆形图标
     */
    public static StackPane createColoredIcon(String iconChar, Color color, double size) {
        StackPane container = new StackPane();
        container.setMinSize(size, size);
        container.setMaxSize(size, size);

        Circle bg = new Circle(size / 2);
        bg.setFill(color.deriveColor(0, 1, 1, 0.15));
        bg.setStroke(color.deriveColor(0, 1, 1, 0.3));
        bg.setStrokeWidth(1);

        Label icon = new Label(iconChar);
        icon.setTextFill(color);
        icon.setFont(Font.font("System", FontWeight.BOLD, size * 0.5));

        container.getChildren().addAll(bg, icon);
        return container;
    }

    // ==================== SVG图标形状 ====================

    /**
     * 创建仪表盘形状图标
     */
    public static SVGPath createDashboardShape() {
        SVGPath path = new SVGPath();
        path.setContent("M3 3h6v6H3V3zm8 0h6v6h-6V3zm-8 8h6v6H3v-6zm8 2a4 4 0 1 0 8 0 4 4 0 0 0-8 0z");
        path.setFill(Color.web("#00d4ff"));
        return path;
    }

    /**
     * 创建用户形状图标
     */
    public static SVGPath createUserShape() {
        SVGPath path = new SVGPath();
        path.setContent("M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z");
        path.setFill(Color.web("#00d4ff"));
        return path;
    }

    /**
     * 创建设置形状图标
     */
    public static SVGPath createSettingsShape() {
        SVGPath path = new SVGPath();
        path.setContent("M19.14 12.94c.04-.31.06-.63.06-.94 0-.31-.02-.63-.06-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.04.31-.06.63-.06.94s.02.63.06.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z");
        path.setFill(Color.web("#00d4ff"));
        return path;
    }

    // ==================== 状态图标 ====================

    /**
     * 获取风险等级图标
     */
    public static String getRiskIcon(String level) {
        return switch (level) {
            case "极高风险" -> "◆";  // 红色菱形
            case "高风险" -> "▲";    // 三角形
            case "中风险" -> "●";    // 圆形
            case "低风险" -> "○";    // 空心圆
            default -> "○";
        };
    }

    /**
     * 获取状态图标
     */
    public static String getStatusIcon(String status) {
        return switch (status) {
            case "正常", "完成", "通过" -> "✓";
            case "异常", "失败", "拒绝" -> "✗";
            case "警告", "待处理" -> "◆";
            case "进行中", "处理中" -> "◐";
            default -> "●";
        };
    }

    // ==================== 数字徽章 ====================

    /**
     * 创建数字徽章
     */
    public static StackPane createBadge(int count, String color) {
        StackPane badge = new StackPane();
        badge.setMinSize(20, 20);
        badge.setMaxSize(count > 99 ? 28 : 20, 20);

        String text = count > 99 ? "99+" : String.valueOf(count);
        
        badge.setStyle(
            "-fx-background-color: " + color + "; " +
            "-fx-background-radius: 10; " +
            "-fx-padding: 2 6 2 6;"
        );

        Label label = new Label(text);
        label.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: white;");

        badge.getChildren().add(label);
        badge.setVisible(count > 0);

        return badge;
    }

    // ==================== 带徽章的导航项 ====================

    /**
     * 创建带徽章的导航图标
     */
    public static StackPane createNavIconWithBadge(String menuName, int badgeCount) {
        StackPane container = new StackPane();
        container.setMinSize(32, 24);
        container.setMaxSize(32, 24);
        container.setAlignment(Pos.CENTER);

        Label icon = createNavIconLabel(menuName);
        
        if (badgeCount > 0) {
            StackPane badge = createBadge(badgeCount, "#ff0066");
            StackPane.setAlignment(badge, Pos.TOP_RIGHT);
            StackPane.setMargin(badge, new javafx.geometry.Insets(-4, -8, 0, 0));
            container.getChildren().addAll(icon, badge);
        } else {
            container.getChildren().add(icon);
        }

        return container;
    }
}

JAVA_EOF
print_success "已创建: NavIcon.java"

#===============================================================================
# DialogUtil.java - 弹窗工具类
#===============================================================================
cat > src/main/java/com/petition/util/DialogUtil.java << 'JAVA_EOF'
package com.petition.util;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 现代化弹窗工具类 v2.0
 * 提供统一的弹窗管理和丰富的弹窗类型
 */
public class DialogUtil {

    private static final String CSS_PATH = "/css/main.css";

    // ==================== 表单弹窗 ====================

    /**
     * 创建标准表单弹窗
     */
    public static <T> Stage createFormDialog(
            Window owner, String fxmlPath, String title,
            double width, double height, Consumer<T> onController) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = DialogUtil.class.getResource(fxmlPath);
            if (url == null) throw new IOException("找不到FXML文件: " + fxmlPath);
            loader.setLocation(url);
            Parent root = loader.load();

            if (onController != null) {
                T ctrl = loader.getController();
                onController.accept(ctrl);
            }

            Scene scene = new Scene(root, width, height);
            scene.setFill(Color.TRANSPARENT);
            applyTheme(scene);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setMinWidth(width * 0.8);
            stage.setMinHeight(height * 0.8);

            centerOnOwner(stage, owner, width, height);

            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE) closeWithAnimation(stage);
            });

            stage.setOnShown(e -> AnimationUtil.dialogOpen(stage));
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("加载失败", "无法加载表单: " + e.getMessage());
            return null;
        }
    }

    /**
     * 创建自定义内容弹窗
     */
    public static Stage createCustomDialog(Window owner, String title, Node content, 
                                           double width, double height) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dialog-pane");
        root.setCenter(content);

        Scene scene = new Scene(root, width, height);
        scene.setFill(Color.TRANSPARENT);
        applyTheme(scene);

        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setTitle(title);
        stage.setScene(scene);

        centerOnOwner(stage, owner, width, height);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) closeWithAnimation(stage);
        });

        stage.setOnShown(e -> AnimationUtil.dialogOpen(stage));
        return stage;
    }

    // ==================== 提示框 ====================

    /**
     * 信息提示
     */
    public static void showInfoAlert(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, "ℹ " + title, message);
    }

    /**
     * 成功提示
     */
    public static void showSuccessAlert(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, "✓ " + title, message);
    }

    /**
     * 警告提示
     */
    public static void showWarningAlert(String title, String message) {
        showAlert(Alert.AlertType.WARNING, "⚠ " + title, message);
    }

    /**
     * 错误提示
     */
    public static void showErrorAlert(String title, String message) {
        showAlert(Alert.AlertType.ERROR, "✗ " + title, message);
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        applyDarkTheme(alert);
        alert.showAndWait();
    }

    // ==================== 确认框 ====================

    /**
     * 确认对话框
     */
    public static void showConfirmDialog(String title, String message, 
                                         Runnable onConfirm, Runnable onCancel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        applyDarkTheme(alert);
        
        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                if (onConfirm != null) onConfirm.run();
            } else {
                if (onCancel != null) onCancel.run();
            }
        });
    }

    /**
     * 删除确认对话框
     */
    public static void showDeleteConfirmDialog(String itemName, Runnable onConfirm) {
        showConfirmDialog(
            "⚠ 确认删除",
            "确定要删除「" + itemName + "」吗？\n此操作不可恢复！",
            onConfirm,
            null
        );
    }

    /**
     * 带输入框的确认对话框
     */
    public static Optional<String> showInputDialog(String title, String header, 
                                                   String defaultValue, String promptText) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(promptText);
        applyDarkTheme(dialog);
        return dialog.showAndWait();
    }

    // ==================== Toast提示 ====================

    /**
     * 显示Toast消息
     */
    public static void showToast(Window owner, String message, ToastType type) {
        showToast(owner, message, type, Duration.seconds(3));
    }

    public static void showToast(Window owner, String message, ToastType type, Duration duration) {
        Stage toast = new Stage();
        toast.initStyle(StageStyle.TRANSPARENT);
        toast.initOwner(owner);
        toast.setAlwaysOnTop(true);

        String icon = switch (type) {
            case SUCCESS -> "✓";
            case ERROR -> "✗";
            case WARNING -> "⚠";
            case INFO -> "ℹ";
        };

        String color = switch (type) {
            case SUCCESS -> "#00ff88";
            case ERROR -> "#ff0066";
            case WARNING -> "#ffaa00";
            case INFO -> "#00d4ff";
        };

        HBox content = new HBox(12);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(16, 24, 16, 24));
        content.setStyle(
            "-fx-background-color: #1a1f35f0; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: " + color + "40; " +
            "-fx-border-radius: 12; " +
            "-fx-border-width: 1; " +
            "-fx-effect: dropshadow(gaussian, #00000080, 16, 0, 0, 4);"
        );

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: " + color + ";");

        Label msgLabel = new Label(message);
        msgLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #e2e8f0;");

        content.getChildren().addAll(iconLabel, msgLabel);

        Scene scene = new Scene(content);
        scene.setFill(Color.TRANSPARENT);

        toast.setScene(scene);

        // 定位到窗口底部中央
        if (owner != null) {
            toast.setX(owner.getX() + (owner.getWidth() - 300) / 2);
            toast.setY(owner.getY() + owner.getHeight() - 100);
        }

        toast.show();

        // 动画
        content.setOpacity(0);
        content.setTranslateY(20);
        
        AnimationUtil.slideInFromBottom(content, AnimationUtil.NORMAL, null);

        // 自动关闭
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(duration);
        pause.setOnFinished(e -> {
            AnimationUtil.fadeOut(content, AnimationUtil.FAST, toast::close);
        });
        pause.play();
    }

    public enum ToastType {
        SUCCESS, ERROR, WARNING, INFO
    }

    // ==================== 图片查看器 ====================

    /**
     * 显示图片查看器
     */
    public static void showImageViewer(Window owner, List<String> imagePaths, int startIndex) {
        if (imagePaths == null || imagePaths.isEmpty()) return;

        Stage viewer = new Stage();
        viewer.initStyle(StageStyle.DECORATED);
        viewer.initModality(Modality.APPLICATION_MODAL);
        viewer.initOwner(owner);
        viewer.setTitle("照片查看器");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0a0e1a;");

        // 主图显示
        ImageView mainImage = new ImageView();
        mainImage.setPreserveRatio(true);
        mainImage.setFitWidth(700);
        mainImage.setFitHeight(500);

        StackPane imagePane = new StackPane(mainImage);
        imagePane.setStyle("-fx-background-color: #0d1224;");
        imagePane.setPadding(new Insets(20));

        // 导航按钮
        Button prevBtn = new Button("◀");
        prevBtn.getStyleClass().addAll("btn", "btn-ghost");
        prevBtn.setStyle("-fx-font-size: 24px;");

        Button nextBtn = new Button("▶");
        nextBtn.getStyleClass().addAll("btn", "btn-ghost");
        nextBtn.setStyle("-fx-font-size: 24px;");

        // 计数
        Label counter = new Label();
        counter.getStyleClass().addAll("text-secondary");

        final int[] currentIdx = {Math.max(0, Math.min(startIndex, imagePaths.size() - 1))};

        Runnable loadImage = () -> {
            int idx = currentIdx[0];
            try {
                Image img = new Image("file:" + imagePaths.get(idx));
                mainImage.setImage(img);
            } catch (Exception e) {
                mainImage.setImage(null);
            }
            counter.setText((idx + 1) + " / " + imagePaths.size());
            prevBtn.setDisable(idx == 0);
            nextBtn.setDisable(idx == imagePaths.size() - 1);
        };

        prevBtn.setOnAction(e -> {
            if (currentIdx[0] > 0) {
                currentIdx[0]--;
                loadImage.run();
            }
        });

        nextBtn.setOnAction(e -> {
            if (currentIdx[0] < imagePaths.size() - 1) {
                currentIdx[0]++;
                loadImage.run();
            }
        });

        loadImage.run();

        HBox toolbar = new HBox(16);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setPadding(new Insets(16));
        toolbar.setStyle("-fx-background-color: #0d122480;");
        toolbar.getChildren().addAll(prevBtn, counter, nextBtn);

        root.setCenter(imagePane);
        root.setBottom(toolbar);

        Scene scene = new Scene(root, 800, 650);
        applyTheme(scene);

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT -> { if (currentIdx[0] > 0) { currentIdx[0]--; loadImage.run(); } }
                case RIGHT -> { if (currentIdx[0] < imagePaths.size() - 1) { currentIdx[0]++; loadImage.run(); } }
                case ESCAPE -> viewer.close();
            }
        });

        viewer.setScene(scene);
        viewer.show();
    }

    // ==================== 文件选择 ====================

    /**
     * 选择图片文件
     */
    public static List<File> chooseImageFiles(Window owner, boolean multiple) {
        FileChooser fc = new FileChooser();
        fc.setTitle("选择图片");
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("图片文件", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp"),
            new FileChooser.ExtensionFilter("所有文件", "*.*")
        );

        if (multiple) {
            return fc.showOpenMultipleDialog(owner);
        } else {
            File file = fc.showOpenDialog(owner);
            return file != null ? List.of(file) : null;
        }
    }

    /**
     * 选择保存位置
     */
    public static File chooseSaveFile(Window owner, String title, String defaultName, 
                                       String... extensions) {
        FileChooser fc = new FileChooser();
        fc.setTitle(title);
        fc.setInitialFileName(defaultName);
        
        if (extensions.length > 0) {
            fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("支持的格式", extensions)
            );
        }

        return fc.showSaveDialog(owner);
    }

    // ==================== 辅助方法 ====================

    /**
     * 关闭弹窗（带动画）
     */
    public static void closeWithAnimation(Stage stage) {
        AnimationUtil.dialogClose(stage, stage::close);
    }

    /**
     * 应用主题样式
     */
    private static void applyTheme(Scene scene) {
        try {
            URL cssUrl = DialogUtil.class.getResource(CSS_PATH);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
        } catch (Exception e) {
            System.err.println("无法加载CSS: " + e.getMessage());
        }
    }

    /**
     * 应用暗色主题到Alert
     */
    private static void applyDarkTheme(Dialog<?> dialog) {
        DialogPane pane = dialog.getDialogPane();
        pane.setStyle(
            "-fx-background-color: #1a1f35; " +
            "-fx-border-color: #2d3a5a; " +
            "-fx-border-radius: 12; " +
            "-fx-background-radius: 12;"
        );
        
        try {
            URL cssUrl = DialogUtil.class.getResource(CSS_PATH);
            if (cssUrl != null) {
                pane.getStylesheets().add(cssUrl.toExternalForm());
            }
        } catch (Exception ignored) {}
    }

    /**
     * 居中显示在父窗口
     */
    private static void centerOnOwner(Stage stage, Window owner, double width, double height) {
        if (owner != null) {
            stage.setX(owner.getX() + (owner.getWidth() - width) / 2);
            stage.setY(owner.getY() + (owner.getHeight() - height) / 2);
        }
    }
}

JAVA_EOF
print_success "已创建: DialogUtil.java"

#===============================================================================
# WizardDialog.java - 向导式弹窗组件
#===============================================================================
cat > src/main/java/com/petition/util/WizardDialog.java << 'JAVA_EOF'
package com.petition.util;

import javafx.animation.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

/**
 * 向导式弹窗组件
 * 提供引导式的多步骤表单填写体验
 */
public class WizardDialog<T> {

    private Stage stage;
    private BorderPane root;
    private StackPane contentStack;
    private HBox stepsIndicator;
    private Button prevBtn, nextBtn, submitBtn;
    private Label titleLabel, subtitleLabel;

    private List<WizardStep> steps = new ArrayList<>();
    private int currentStepIndex = 0;
    private T dataModel;
    private Consumer<T> onSubmit;
    private Runnable onCancel;

    private static final String CSS_PATH = "/css/main.css";

    /**
     * 向导步骤定义
     */
    public static class WizardStep {
        private String title;
        private String subtitle;
        private String icon;
        private Node content;
        private Runnable onEnter;
        private BooleanProperty valid = new SimpleBooleanProperty(true);

        public WizardStep(String title, String icon) {
            this.title = title;
            this.icon = icon;
        }

        public WizardStep subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public WizardStep content(Node content) {
            this.content = content;
            return this;
        }

        public WizardStep onEnter(Runnable onEnter) {
            this.onEnter = onEnter;
            return this;
        }

        public WizardStep valid(BooleanProperty valid) {
            this.valid = valid;
            return this;
        }

        public String getTitle() { return title; }
        public String getSubtitle() { return subtitle; }
        public String getIcon() { return icon; }
        public Node getContent() { return content; }
        public BooleanProperty validProperty() { return valid; }
        public boolean isValid() { return valid.get(); }
    }

    public WizardDialog(Window owner, String title, T dataModel) {
        this.dataModel = dataModel;
        createStage(owner, title);
    }

    private void createStage(Window owner, String title) {
        stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setTitle(title);
        stage.setMinWidth(700);
        stage.setMinHeight(600);

        root = new BorderPane();
        root.getStyleClass().add("wizard-dialog");

        // 顶部标题区
        VBox header = createHeader(title);
        root.setTop(header);

        // 内容区域
        contentStack = new StackPane();
        contentStack.getStyleClass().add("wizard-content");
        root.setCenter(contentStack);

        // 底部按钮区
        HBox footer = createFooter();
        root.setBottom(footer);

        Scene scene = new Scene(root, 800, 700);
        scene.setFill(Color.TRANSPARENT);
        
        try {
            scene.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
        } catch (Exception e) {
            System.err.println("无法加载CSS: " + e.getMessage());
        }

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                handleCancel();
            }
        });

        stage.setScene(scene);
        stage.setOnShown(e -> AnimationUtil.dialogOpen(stage));
    }

    private VBox createHeader(String title) {
        VBox header = new VBox(8);
        header.getStyleClass().add("wizard-header");

        titleLabel = new Label(title);
        titleLabel.getStyleClass().add("wizard-title");

        subtitleLabel = new Label();
        subtitleLabel.getStyleClass().add("wizard-subtitle");

        header.getChildren().addAll(titleLabel, subtitleLabel);

        // 步骤指示器
        stepsIndicator = new HBox();
        stepsIndicator.getStyleClass().add("wizard-steps");
        stepsIndicator.setAlignment(Pos.CENTER);

        VBox headerContainer = new VBox(0);
        headerContainer.getChildren().addAll(header, stepsIndicator);

        return headerContainer;
    }

    private HBox createFooter() {
        HBox footer = new HBox(12);
        footer.getStyleClass().add("wizard-footer");
        footer.setAlignment(Pos.CENTER_RIGHT);

        Button cancelBtn = new Button("取消");
        cancelBtn.getStyleClass().addAll("btn", "btn-ghost");
        cancelBtn.setOnAction(e -> handleCancel());

        prevBtn = new Button("◀ 上一步");
        prevBtn.getStyleClass().addAll("btn", "btn-secondary");
        prevBtn.setOnAction(e -> goToPreviousStep());

        nextBtn = new Button("下一步 ▶");
        nextBtn.getStyleClass().addAll("btn", "btn-primary");
        nextBtn.setOnAction(e -> goToNextStep());

        submitBtn = new Button("✓ 提交保存");
        submitBtn.getStyleClass().addAll("btn", "btn-success", "btn-lg");
        submitBtn.setOnAction(e -> handleSubmit());
        submitBtn.setVisible(false);
        submitBtn.setManaged(false);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        footer.getChildren().addAll(cancelBtn, spacer, prevBtn, nextBtn, submitBtn);

        return footer;
    }

    public WizardDialog<T> addStep(WizardStep step) {
        steps.add(step);
        return this;
    }

    public WizardDialog<T> onSubmit(Consumer<T> onSubmit) {
        this.onSubmit = onSubmit;
        return this;
    }

    public WizardDialog<T> onCancel(Runnable onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    public void show() {
        if (steps.isEmpty()) {
            throw new IllegalStateException("向导至少需要一个步骤");
        }

        buildStepsIndicator();
        showStep(0, false);
        stage.show();
    }

    private void buildStepsIndicator() {
        stepsIndicator.getChildren().clear();

        for (int i = 0; i < steps.size(); i++) {
            WizardStep step = steps.get(i);
            
            // 步骤圆圈
            VBox stepBox = new VBox(6);
            stepBox.setAlignment(Pos.CENTER);
            stepBox.getStyleClass().add("wizard-step");

            StackPane circle = new StackPane();
            circle.getStyleClass().add("wizard-step-circle");
            circle.setMinSize(40, 40);
            circle.setMaxSize(40, 40);

            Label numLabel = new Label(step.getIcon() != null ? step.getIcon() : String.valueOf(i + 1));
            numLabel.getStyleClass().add("wizard-step-number");
            circle.getChildren().add(numLabel);

            Label textLabel = new Label(step.getTitle());
            textLabel.getStyleClass().add("wizard-step-label");

            stepBox.getChildren().addAll(circle, textLabel);
            stepsIndicator.getChildren().add(stepBox);

            // 连接线
            if (i < steps.size() - 1) {
                Region line = new Region();
                line.getStyleClass().add("wizard-step-line");
                line.setMinWidth(60);
                line.setMaxHeight(2);
                HBox lineBox = new HBox(line);
                lineBox.setAlignment(Pos.CENTER);
                lineBox.setPadding(new Insets(0, 8, 16, 8));
                stepsIndicator.getChildren().add(lineBox);
            }
        }

        updateStepsIndicator();
    }

    private void updateStepsIndicator() {
        int nodeIndex = 0;
        for (int i = 0; i < steps.size(); i++) {
            Node stepNode = stepsIndicator.getChildren().get(nodeIndex);
            if (stepNode instanceof VBox) {
                VBox stepBox = (VBox) stepNode;
                StackPane circle = (StackPane) stepBox.getChildren().get(0);
                Label numLabel = (Label) circle.getChildren().get(0);
                Label textLabel = (Label) stepBox.getChildren().get(1);

                circle.getStyleClass().removeAll("wizard-step-circle-active", "wizard-step-circle-completed");
                numLabel.getStyleClass().removeAll("wizard-step-number-active");
                textLabel.getStyleClass().removeAll("wizard-step-label-active");

                if (i < currentStepIndex) {
                    circle.getStyleClass().add("wizard-step-circle-completed");
                    numLabel.setText("✓");
                } else if (i == currentStepIndex) {
                    circle.getStyleClass().add("wizard-step-circle-active");
                    numLabel.getStyleClass().add("wizard-step-number-active");
                    textLabel.getStyleClass().add("wizard-step-label-active");
                } else {
                    numLabel.setText(steps.get(i).getIcon() != null ? steps.get(i).getIcon() : String.valueOf(i + 1));
                }
            }

            nodeIndex++;
            // 更新连接线
            if (nodeIndex < stepsIndicator.getChildren().size()) {
                Node lineNode = stepsIndicator.getChildren().get(nodeIndex);
                if (lineNode instanceof HBox) {
                    Region line = (Region) ((HBox) lineNode).getChildren().get(0);
                    line.getStyleClass().remove("wizard-step-line-completed");
                    if (i < currentStepIndex) {
                        line.getStyleClass().add("wizard-step-line-completed");
                    }
                }
                nodeIndex++;
            }
        }
    }

    private void showStep(int index, boolean animate) {
        if (index < 0 || index >= steps.size()) return;

        WizardStep step = steps.get(index);
        Node newContent = step.getContent();

        // 更新标题
        subtitleLabel.setText(step.getSubtitle() != null ? step.getSubtitle() : "步骤 " + (index + 1) + " / " + steps.size());

        if (animate && !contentStack.getChildren().isEmpty()) {
            Node oldContent = contentStack.getChildren().get(0);
            contentStack.getChildren().add(newContent);
            
            if (index > currentStepIndex) {
                AnimationUtil.wizardNext(oldContent, newContent, () -> {
                    contentStack.getChildren().remove(oldContent);
                });
            } else {
                AnimationUtil.wizardPrev(oldContent, newContent, () -> {
                    contentStack.getChildren().remove(oldContent);
                });
            }
        } else {
            contentStack.getChildren().setAll(newContent);
            AnimationUtil.fadeIn(newContent);
        }

        currentStepIndex = index;
        updateStepsIndicator();
        updateButtons();

        if (step.onEnter != null) {
            step.onEnter.run();
        }
    }

    private void updateButtons() {
        boolean isFirst = currentStepIndex == 0;
        boolean isLast = currentStepIndex == steps.size() - 1;

        prevBtn.setDisable(isFirst);
        prevBtn.setVisible(!isFirst);
        prevBtn.setManaged(!isFirst);

        nextBtn.setVisible(!isLast);
        nextBtn.setManaged(!isLast);

        submitBtn.setVisible(isLast);
        submitBtn.setManaged(isLast);

        // 绑定验证状态
        WizardStep currentStep = steps.get(currentStepIndex);
        nextBtn.disableProperty().bind(currentStep.validProperty().not());
        submitBtn.disableProperty().bind(currentStep.validProperty().not());
    }

    private void goToNextStep() {
        if (currentStepIndex < steps.size() - 1) {
            WizardStep current = steps.get(currentStepIndex);
            if (current.isValid()) {
                showStep(currentStepIndex + 1, true);
            } else {
                AnimationUtil.shake(contentStack);
            }
        }
    }

    private void goToPreviousStep() {
        if (currentStepIndex > 0) {
            showStep(currentStepIndex - 1, true);
        }
    }

    private void handleSubmit() {
        WizardStep last = steps.get(steps.size() - 1);
        if (!last.isValid()) {
            AnimationUtil.shake(submitBtn);
            return;
        }

        if (onSubmit != null) {
            onSubmit.accept(dataModel);
        }
        close();
    }

    private void handleCancel() {
        if (onCancel != null) {
            onCancel.run();
        }
        close();
    }

    public void close() {
        AnimationUtil.dialogClose(stage, stage::close);
    }

    public T getDataModel() {
        return dataModel;
    }

    public Stage getStage() {
        return stage;
    }

    // ==================== 表单构建辅助方法 ====================

    /**
     * 创建表单区域
     */
    public static VBox createSection(String title, Node... children) {
        VBox section = new VBox(16);
        section.getStyleClass().add("wizard-section");

        if (title != null && !title.isEmpty()) {
            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("wizard-section-title");
            section.getChildren().add(titleLabel);
        }

        section.getChildren().addAll(children);
        return section;
    }

    /**
     * 创建表单字段
     */
    public static VBox createField(String label, Node input) {
        return createField(label, input, null);
    }

    public static VBox createField(String label, Node input, String hint) {
        VBox field = new VBox(6);

        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("wizard-field-label");
        field.getChildren().add(labelNode);

        if (input instanceof TextField) {
            ((TextField) input).getStyleClass().add("text-field");
        } else if (input instanceof ComboBox) {
            ((ComboBox<?>) input).getStyleClass().add("combo-box");
        } else if (input instanceof TextArea) {
            ((TextArea) input).getStyleClass().add("text-area");
        }
        field.getChildren().add(input);

        if (hint != null && !hint.isEmpty()) {
            Label hintLabel = new Label(hint);
            hintLabel.getStyleClass().add("wizard-field-hint");
            field.getChildren().add(hintLabel);
        }

        return field;
    }

    /**
     * 创建必填字段
     */
    public static VBox createRequiredField(String label, TextField input, BooleanProperty valid) {
        VBox field = createField(label + " *", input, null);
        
        input.textProperty().addListener((obs, oldVal, newVal) -> {
            boolean isValid = newVal != null && !newVal.trim().isEmpty();
            valid.set(isValid);
            
            input.getStyleClass().removeAll("text-field-error", "text-field-success");
            if (newVal != null && !newVal.isEmpty()) {
                input.getStyleClass().add(isValid ? "text-field-success" : "text-field-error");
            }
        });

        return field;
    }

    /**
     * 创建两列布局
     */
    public static HBox createTwoColumns(Node left, Node right) {
        HBox row = new HBox(20);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        
        if (left instanceof Region) ((Region) left).setMaxWidth(Double.MAX_VALUE);
        if (right instanceof Region) ((Region) right).setMaxWidth(Double.MAX_VALUE);
        
        row.getChildren().addAll(left, right);
        return row;
    }

    /**
     * 创建三列布局
     */
    public static HBox createThreeColumns(Node col1, Node col2, Node col3) {
        HBox row = new HBox(16);
        HBox.setHgrow(col1, Priority.ALWAYS);
        HBox.setHgrow(col2, Priority.ALWAYS);
        HBox.setHgrow(col3, Priority.ALWAYS);
        
        if (col1 instanceof Region) ((Region) col1).setMaxWidth(Double.MAX_VALUE);
        if (col2 instanceof Region) ((Region) col2).setMaxWidth(Double.MAX_VALUE);
        if (col3 instanceof Region) ((Region) col3).setMaxWidth(Double.MAX_VALUE);
        
        row.getChildren().addAll(col1, col2, col3);
        return row;
    }

    /**
     * 创建风险等级选择器
     */
    public static HBox createRiskLevelSelector(StringProperty selectedLevel) {
        HBox container = new HBox(12);
        container.setAlignment(Pos.CENTER_LEFT);

        String[] levels = {"低风险", "中风险", "高风险", "极高风险"};
        String[] styles = {"low", "medium", "high", "extreme"};
        String[] colors = {"#00ff88", "#ffaa00", "#ff4444", "#ff0066"};

        ToggleGroup group = new ToggleGroup();

        for (int i = 0; i < levels.length; i++) {
            final int index = i;
            ToggleButton btn = new ToggleButton(levels[i]);
            btn.setToggleGroup(group);
            btn.getStyleClass().addAll("risk-card", "risk-card-" + styles[i]);
            btn.setMinWidth(90);

            btn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                btn.getStyleClass().remove("selected");
                if (isSelected) {
                    btn.getStyleClass().add("selected");
                    selectedLevel.set(levels[index]);
                }
            });

            if (selectedLevel.get() != null && selectedLevel.get().equals(levels[i])) {
                btn.setSelected(true);
            }

            container.getChildren().add(btn);
        }

        return container;
    }

    /**
     * 创建照片上传区域
     */
    public static VBox createPhotoUploader(List<String> photoPaths, Runnable onPhotosChanged) {
        VBox container = new VBox(12);
        container.getStyleClass().add("photo-gallery");

        FlowPane photosPane = new FlowPane(12, 12);
        photosPane.setPrefWrapLength(500);

        // 添加已有照片
        for (String path : photoPaths) {
            photosPane.getChildren().add(createPhotoThumbnail(path, () -> {
                photoPaths.remove(path);
                if (onPhotosChanged != null) onPhotosChanged.run();
            }));
        }

        // 添加上传按钮
        Button addBtn = new Button("+ 添加照片");
        addBtn.getStyleClass().addAll("btn", "btn-secondary");
        addBtn.setMinSize(100, 100);
        addBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("选择照片");
            fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("图片文件", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp")
            );
            List<File> files = fc.showOpenMultipleDialog(container.getScene().getWindow());
            if (files != null) {
                for (File file : files) {
                    photoPaths.add(file.getAbsolutePath());
                }
                if (onPhotosChanged != null) onPhotosChanged.run();
            }
        });

        photosPane.getChildren().add(addBtn);
        container.getChildren().add(photosPane);

        return container;
    }

    private static StackPane createPhotoThumbnail(String path, Runnable onRemove) {
        StackPane thumb = new StackPane();
        thumb.getStyleClass().add("photo-thumbnail");
        thumb.setMinSize(100, 100);
        thumb.setMaxSize(100, 100);

        try {
            Image img = new Image("file:" + path, 100, 100, true, true);
            ImageView iv = new ImageView(img);
            iv.setFitWidth(94);
            iv.setFitHeight(94);
            thumb.getChildren().add(iv);
        } catch (Exception e) {
            Label placeholder = new Label("📷");
            placeholder.setStyle("-fx-font-size: 24px; -fx-text-fill: #64748b;");
            thumb.getChildren().add(placeholder);
        }

        // 删除按钮
        Button removeBtn = new Button("×");
        removeBtn.getStyleClass().addAll("btn", "btn-danger", "btn-sm");
        removeBtn.setStyle("-fx-background-radius: 50; -fx-min-width: 24; -fx-min-height: 24; -fx-max-width: 24; -fx-max-height: 24; -fx-padding: 0;");
        removeBtn.setVisible(false);
        StackPane.setAlignment(removeBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(removeBtn, new Insets(4));

        removeBtn.setOnAction(e -> onRemove.run());

        thumb.setOnMouseEntered(e -> removeBtn.setVisible(true));
        thumb.setOnMouseExited(e -> removeBtn.setVisible(false));

        thumb.getChildren().add(removeBtn);
        
        return thumb;
    }
}

JAVA_EOF
print_success "已创建: WizardDialog.java"

#===============================================================================
# SlideViewerDialog.java - 下滑式查看弹窗
#===============================================================================
cat > src/main/java/com/petition/util/SlideViewerDialog.java << 'JAVA_EOF'
package com.petition.util;

import javafx.animation.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 下滑式详情查看弹窗
 * 提供流畅的下滑浏览体验，适合查看详细信息
 */
public class SlideViewerDialog {

    private Stage stage;
    private BorderPane root;
    private ScrollPane scrollPane;
    private VBox contentBox;
    private HBox headerBar;
    private Label titleLabel;

    private List<ViewerSection> sections = new ArrayList<>();
    private Runnable onClose;
    private Consumer<Void> onEdit;

    private static final String CSS_PATH = "/css/main.css";

    /**
     * 查看区块定义
     */
    public static class ViewerSection {
        private String title;
        private String icon;
        private Node content;
        private boolean expanded = true;

        public ViewerSection(String title) {
            this.title = title;
        }

        public ViewerSection icon(String icon) {
            this.icon = icon;
            return this;
        }

        public ViewerSection content(Node content) {
            this.content = content;
            return this;
        }

        public ViewerSection expanded(boolean expanded) {
            this.expanded = expanded;
            return this;
        }

        public String getTitle() { return title; }
        public String getIcon() { return icon; }
        public Node getContent() { return content; }
        public boolean isExpanded() { return expanded; }
    }

    public SlideViewerDialog(Window owner, String title) {
        createStage(owner, title);
    }

    private void createStage(Window owner, String title) {
        stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setTitle(title);
        stage.setMinWidth(700);
        stage.setMinHeight(500);

        root = new BorderPane();
        root.getStyleClass().add("slide-viewer");

        // 顶部悬浮工具栏
        headerBar = createHeaderBar(title);
        root.setTop(headerBar);

        // 滚动内容区
        contentBox = new VBox(24);
        contentBox.getStyleClass().add("slide-viewer-content");
        contentBox.setPadding(new Insets(0, 0, 40, 0));

        scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("bg-transparent");
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 800, 800);
        scene.setFill(Color.web("#0a0e1a"));

        try {
            scene.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
        } catch (Exception e) {
            System.err.println("无法加载CSS: " + e.getMessage());
        }

        // 键盘事件
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                close();
            }
        });

        // 滚动视差效果
        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            double opacity = Math.min(1.0, newVal.doubleValue() * 3);
            headerBar.setStyle("-fx-background-color: rgba(10, 14, 26, " + (0.7 + opacity * 0.25) + ");");
        });

        stage.setScene(scene);
        stage.setOnShown(e -> playOpenAnimation());
    }

    private HBox createHeaderBar(String title) {
        HBox header = new HBox(16);
        header.getStyleClass().add("slide-viewer-header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 24, 16, 24));
        header.setStyle("-fx-background-color: rgba(10, 14, 26, 0.7);");

        // 关闭按钮
        Button closeBtn = new Button("✕");
        closeBtn.getStyleClass().addAll("slide-viewer-close");
        closeBtn.setOnAction(e -> close());

        // 标题
        titleLabel = new Label(title);
        titleLabel.getStyleClass().addAll("text-xl", "font-bold", "text-primary");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 编辑按钮
        Button editBtn = new Button("✎ 编辑");
        editBtn.getStyleClass().addAll("btn", "btn-primary");
        editBtn.setOnAction(e -> {
            if (onEdit != null) {
                onEdit.accept(null);
            }
        });

        header.getChildren().addAll(closeBtn, titleLabel, spacer, editBtn);

        return header;
    }

    public SlideViewerDialog addSection(ViewerSection section) {
        sections.add(section);
        return this;
    }

    public SlideViewerDialog onClose(Runnable onClose) {
        this.onClose = onClose;
        return this;
    }

    public SlideViewerDialog onEdit(Consumer<Void> onEdit) {
        this.onEdit = onEdit;
        return this;
    }

    public void show() {
        buildContent();
        stage.show();
    }

    private void buildContent() {
        contentBox.getChildren().clear();

        for (int i = 0; i < sections.size(); i++) {
            ViewerSection section = sections.get(i);
            Node sectionNode = createSectionNode(section);
            contentBox.getChildren().add(sectionNode);
        }
    }

    private Node createSectionNode(ViewerSection section) {
        VBox sectionBox = new VBox(16);
        sectionBox.getStyleClass().add("slide-viewer-section");

        // 区块标题
        HBox titleBar = new HBox(12);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.getStyleClass().add("slide-viewer-section-title");

        if (section.getIcon() != null) {
            Label iconLabel = new Label(section.getIcon());
            iconLabel.setStyle("-fx-font-size: 18px;");
            titleBar.getChildren().add(iconLabel);
        }

        Label titleLabel = new Label(section.getTitle());
        titleLabel.getStyleClass().addAll("font-bold");
        titleBar.getChildren().add(titleLabel);

        sectionBox.getChildren().add(titleBar);

        if (section.getContent() != null) {
            sectionBox.getChildren().add(section.getContent());
        }

        return sectionBox;
    }

    private void playOpenAnimation() {
        // 内容从底部滑入
        contentBox.setTranslateY(50);
        contentBox.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();

        TranslateTransition tt = new TranslateTransition(Duration.millis(400), contentBox);
        tt.setFromY(50);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(Duration.millis(400), contentBox);
        ft.setFromValue(0);
        ft.setToValue(1);

        pt.getChildren().addAll(tt, ft);
        pt.setDelay(Duration.millis(100));
        pt.play();

        // 各区块交错出现
        int delay = 200;
        for (Node section : contentBox.getChildren()) {
            section.setOpacity(0);
            section.setTranslateY(20);

            ParallelTransition sectionPt = new ParallelTransition();

            TranslateTransition stt = new TranslateTransition(Duration.millis(300), section);
            stt.setFromY(20);
            stt.setToY(0);
            stt.setInterpolator(Interpolator.EASE_OUT);

            FadeTransition sft = new FadeTransition(Duration.millis(300), section);
            sft.setFromValue(0);
            sft.setToValue(1);

            sectionPt.getChildren().addAll(stt, sft);
            sectionPt.setDelay(Duration.millis(delay));
            sectionPt.play();

            delay += 80;
        }
    }

    public void close() {
        // 关闭动画
        ParallelTransition pt = new ParallelTransition();

        TranslateTransition tt = new TranslateTransition(Duration.millis(200), contentBox);
        tt.setToY(30);
        tt.setInterpolator(Interpolator.EASE_IN);

        FadeTransition ft = new FadeTransition(Duration.millis(200), root);
        ft.setToValue(0);

        pt.getChildren().addAll(tt, ft);
        pt.setOnFinished(e -> {
            if (onClose != null) onClose.run();
            stage.close();
        });
        pt.play();
    }

    public Stage getStage() {
        return stage;
    }

    // ==================== 内容构建辅助方法 ====================

    /**
     * 创建字段显示行
     */
    public static HBox createFieldRow(String label, String value) {
        HBox row = new HBox(16);
        row.getStyleClass().add("slide-viewer-field");
        row.setAlignment(Pos.CENTER_LEFT);

        Label labelNode = new Label(label);
        labelNode.getStyleClass().addAll("slide-viewer-field-label");
        labelNode.setMinWidth(100);

        Label valueNode = new Label(value != null ? value : "-");
        valueNode.getStyleClass().addAll("slide-viewer-field-value");
        valueNode.setWrapText(true);
        HBox.setHgrow(valueNode, Priority.ALWAYS);

        row.getChildren().addAll(labelNode, valueNode);
        return row;
    }

    /**
     * 创建两列字段显示
     */
    public static HBox createTwoColumnFields(String label1, String value1, String label2, String value2) {
        HBox row = new HBox(40);

        VBox col1 = new VBox(4);
        Label l1 = new Label(label1);
        l1.getStyleClass().add("slide-viewer-field-label");
        Label v1 = new Label(value1 != null ? value1 : "-");
        v1.getStyleClass().add("slide-viewer-field-value");
        col1.getChildren().addAll(l1, v1);
        HBox.setHgrow(col1, Priority.ALWAYS);

        VBox col2 = new VBox(4);
        Label l2 = new Label(label2);
        l2.getStyleClass().add("slide-viewer-field-label");
        Label v2 = new Label(value2 != null ? value2 : "-");
        v2.getStyleClass().add("slide-viewer-field-value");
        col2.getChildren().addAll(l2, v2);
        HBox.setHgrow(col2, Priority.ALWAYS);

        row.getChildren().addAll(col1, col2);
        return row;
    }

    /**
     * 创建风险等级徽章
     */
    public static Label createRiskBadge(String level) {
        Label badge = new Label(level);
        badge.getStyleClass().add("risk-badge");

        String styleClass;
        switch (level) {
            case "极高风险": styleClass = "risk-badge-extreme"; break;
            case "高风险": styleClass = "risk-badge-high"; break;
            case "中风险": styleClass = "risk-badge-medium"; break;
            default: styleClass = "risk-badge-low"; break;
        }
        badge.getStyleClass().add(styleClass);

        return badge;
    }

    /**
     * 创建照片画廊组件
     */
    public static VBox createPhotoGallery(List<String> photoPaths) {
        VBox gallery = new VBox(16);
        gallery.getStyleClass().add("photo-gallery");

        if (photoPaths == null || photoPaths.isEmpty()) {
            Label noPhoto = new Label("暂无照片");
            noPhoto.getStyleClass().addAll("text-muted");
            noPhoto.setStyle("-fx-padding: 40; -fx-alignment: center;");
            gallery.getChildren().add(noPhoto);
            return gallery;
        }

        // 主图显示区
        StackPane mainImagePane = new StackPane();
        mainImagePane.getStyleClass().add("photo-gallery-main");
        mainImagePane.setMinHeight(350);

        ImageView mainImage = new ImageView();
        mainImage.getStyleClass().add("photo-gallery-main-image");
        mainImage.setFitWidth(500);
        mainImage.setFitHeight(350);
        mainImage.setPreserveRatio(true);

        // 左右导航按钮
        Button prevBtn = new Button("◀");
        prevBtn.getStyleClass().add("photo-nav-button");
        StackPane.setAlignment(prevBtn, Pos.CENTER_LEFT);
        StackPane.setMargin(prevBtn, new Insets(0, 0, 0, 16));

        Button nextBtn = new Button("▶");
        nextBtn.getStyleClass().add("photo-nav-button");
        StackPane.setAlignment(nextBtn, Pos.CENTER_RIGHT);
        StackPane.setMargin(nextBtn, new Insets(0, 16, 0, 0));

        // 计数器
        Label counter = new Label("1 / " + photoPaths.size());
        counter.getStyleClass().add("photo-counter");
        StackPane.setAlignment(counter, Pos.BOTTOM_CENTER);
        StackPane.setMargin(counter, new Insets(0, 0, 16, 0));

        mainImagePane.getChildren().addAll(mainImage, prevBtn, nextBtn, counter);

        // 缩略图区域
        HBox thumbnails = new HBox(12);
        thumbnails.getStyleClass().add("photo-gallery-thumbnails");
        thumbnails.setAlignment(Pos.CENTER);

        IntegerProperty currentIndex = new SimpleIntegerProperty(0);

        // 加载图片
        Runnable loadCurrentImage = () -> {
            int idx = currentIndex.get();
            if (idx >= 0 && idx < photoPaths.size()) {
                try {
                    String path = photoPaths.get(idx);
                    Image img = new Image("file:" + path, 600, 400, true, true);
                    
                    // 切换动画
                    FadeTransition ft = new FadeTransition(Duration.millis(150), mainImage);
                    ft.setFromValue(1);
                    ft.setToValue(0);
                    ft.setOnFinished(e -> {
                        mainImage.setImage(img);
                        FadeTransition ft2 = new FadeTransition(Duration.millis(150), mainImage);
                        ft2.setFromValue(0);
                        ft2.setToValue(1);
                        ft2.play();
                    });
                    ft.play();
                } catch (Exception e) {
                    mainImage.setImage(null);
                }
                counter.setText((idx + 1) + " / " + photoPaths.size());

                // 更新缩略图选中状态
                for (int i = 0; i < thumbnails.getChildren().size(); i++) {
                    Node thumb = thumbnails.getChildren().get(i);
                    thumb.getStyleClass().remove("photo-thumbnail-active");
                    if (i == idx) {
                        thumb.getStyleClass().add("photo-thumbnail-active");
                    }
                }
            }
        };

        // 创建缩略图
        for (int i = 0; i < photoPaths.size(); i++) {
            final int index = i;
            StackPane thumb = new StackPane();
            thumb.getStyleClass().add("photo-thumbnail");
            thumb.setMinSize(70, 70);
            thumb.setMaxSize(70, 70);
            thumb.setCursor(javafx.scene.Cursor.HAND);

            try {
                Image img = new Image("file:" + photoPaths.get(i), 70, 70, true, true);
                ImageView iv = new ImageView(img);
                iv.setFitWidth(66);
                iv.setFitHeight(66);
                thumb.getChildren().add(iv);
            } catch (Exception e) {
                Label placeholder = new Label("📷");
                placeholder.setStyle("-fx-font-size: 20px; -fx-text-fill: #64748b;");
                thumb.getChildren().add(placeholder);
            }

            thumb.setOnMouseClicked(e -> {
                currentIndex.set(index);
                loadCurrentImage.run();
            });

            thumbnails.getChildren().add(thumb);
        }

        // 导航按钮事件
        prevBtn.setOnAction(e -> {
            if (currentIndex.get() > 0) {
                currentIndex.set(currentIndex.get() - 1);
                loadCurrentImage.run();
            }
        });

        nextBtn.setOnAction(e -> {
            if (currentIndex.get() < photoPaths.size() - 1) {
                currentIndex.set(currentIndex.get() + 1);
                loadCurrentImage.run();
            }
        });

        // 初始加载第一张
        loadCurrentImage.run();

        gallery.getChildren().addAll(mainImagePane, thumbnails);

        return gallery;
    }

    /**
     * 创建信息网格
     */
    public static GridPane createInfoGrid(Map<String, String> data) {
        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(16);

        int row = 0;
        int col = 0;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            VBox field = new VBox(4);
            
            Label label = new Label(entry.getKey());
            label.getStyleClass().add("slide-viewer-field-label");
            
            Label value = new Label(entry.getValue() != null ? entry.getValue() : "-");
            value.getStyleClass().add("slide-viewer-field-value");
            
            field.getChildren().addAll(label, value);
            grid.add(field, col, row);

            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }

        return grid;
    }
}

JAVA_EOF
print_success "已创建: SlideViewerDialog.java"

#===============================================================================
# DashboardComponents.java - 仪表盘组件库
#===============================================================================
cat > src/main/java/com/petition/util/DashboardComponents.java << 'JAVA_EOF'
package com.petition.util;

import javafx.animation.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 仪表盘组件库
 * 提供科技感强的数据可视化组件
 */
public class DashboardComponents {

    // ==================== 数据指标卡片 ====================

    /**
     * 创建数据指标卡片
     */
    public static VBox createMetricCard(String title, String value, String trend, String type) {
        VBox card = new VBox(12);
        card.getStyleClass().addAll("metric-card", "metric-card-" + type);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(24));
        card.setMinWidth(200);
        card.setMinHeight(130);

        // 标题行
        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().addAll("metric-label");
        
        // 脉冲指示器
        Circle pulse = new Circle(4);
        pulse.getStyleClass().add("pulse-indicator");
        if ("danger".equals(type) || "warning".equals(type)) {
            pulse.getStyleClass().add("pulse-indicator-" + type);
        }
        startPulseAnimation(pulse);
        
        header.getChildren().addAll(titleLabel, pulse);

        // 数值
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().addAll("metric-value", "metric-value-" + type);

        // 趋势
        HBox trendBox = new HBox(8);
        trendBox.setAlignment(Pos.CENTER_LEFT);
        
        if (trend != null && !trend.isEmpty()) {
            boolean isUp = trend.startsWith("+") || trend.startsWith("↑");
            Label trendLabel = new Label(trend);
            trendLabel.getStyleClass().addAll("metric-trend", isUp ? "metric-trend-up" : "metric-trend-down");
            trendBox.getChildren().add(trendLabel);
        }

        card.getChildren().addAll(header, valueLabel, trendBox);

        // 悬停效果
        AnimationUtil.addHoverScale(card, 1.03);

        return card;
    }

    /**
     * 启动脉冲动画
     */
    private static void startPulseAnimation(Circle circle) {
        ScaleTransition st = new ScaleTransition(Duration.millis(1000), circle);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.5);
        st.setToY(1.5);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        
        FadeTransition ft = new FadeTransition(Duration.millis(1000), circle);
        ft.setFromValue(1.0);
        ft.setToValue(0.5);
        ft.setCycleCount(Animation.INDEFINITE);
        ft.setAutoReverse(true);

        ParallelTransition pt = new ParallelTransition(st, ft);
        pt.play();
    }

    // ==================== 概览统计条 ====================

    /**
     * 创建顶部概览统计条
     */
    public static HBox createOverviewBar(int total, int highRisk, int thisMonth, int pending) {
        HBox bar = new HBox(32);
        bar.getStyleClass().add("dashboard-overview-bar");
        bar.setAlignment(Pos.CENTER);

        bar.getChildren().addAll(
            createMetricCard("总人数", String.valueOf(total), null, "primary"),
            createMetricCard("高风险", String.valueOf(highRisk), highRisk > 10 ? "↑ 需关注" : null, "danger"),
            createMetricCard("本月新增", String.valueOf(thisMonth), "+" + thisMonth + " 本月", "warning"),
            createMetricCard("待处理", String.valueOf(pending), pending > 0 ? "需跟进" : "已完成", "success")
        );

        // 交错动画
        AnimationUtil.staggerScaleIn(60, bar.getChildren().toArray(new Node[0]));

        return bar;
    }

    // ==================== 数据面板 ====================

    /**
     * 创建数据面板
     */
    public static VBox createDataPanel(String title, String subtitle, Node content) {
        VBox panel = new VBox(16);
        panel.getStyleClass().add("data-panel");

        // 头部
        VBox header = new VBox(4);
        header.getStyleClass().add("data-panel-header");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("data-panel-title");

        if (subtitle != null) {
            Label subtitleLabel = new Label(subtitle);
            subtitleLabel.getStyleClass().add("data-panel-subtitle");
            header.getChildren().addAll(titleLabel, subtitleLabel);
        } else {
            header.getChildren().add(titleLabel);
        }

        panel.getChildren().addAll(header, content);

        return panel;
    }

    // ==================== 快捷操作卡片 ====================

    /**
     * 创建快捷操作卡片
     */
    public static VBox createQuickActionCard(String icon, String label, Runnable action) {
        VBox card = new VBox(8);
        card.getStyleClass().add("quick-action-card");
        card.setAlignment(Pos.CENTER);
        card.setMinSize(120, 100);

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("quick-action-icon");

        Label textLabel = new Label(label);
        textLabel.getStyleClass().add("quick-action-label");

        card.getChildren().addAll(iconLabel, textLabel);
        
        card.setOnMouseClicked(e -> {
            AnimationUtil.bounce(card);
            if (action != null) action.run();
        });

        AnimationUtil.addHoverScale(card, 1.05);

        return card;
    }

    /**
     * 创建快捷操作网格
     */
    public static FlowPane createQuickActionsGrid(Map<String, Runnable> actions) {
        FlowPane grid = new FlowPane(16, 16);
        grid.setAlignment(Pos.CENTER);

        // 预定义图标
        String[] icons = {"➕", "🔍", "📊", "📋", "⚙️", "📤", "📥", "🔔"};
        int iconIndex = 0;

        for (Map.Entry<String, Runnable> entry : actions.entrySet()) {
            String icon = iconIndex < icons.length ? icons[iconIndex++] : "▪";
            grid.getChildren().add(createQuickActionCard(icon, entry.getKey(), entry.getValue()));
        }

        return grid;
    }

    // ==================== 实时时钟 ====================

    /**
     * 创建实时时钟组件
     */
    public static VBox createRealtimeClock() {
        VBox clock = new VBox(4);
        clock.setAlignment(Pos.CENTER_RIGHT);

        Label timeLabel = new Label();
        timeLabel.getStyleClass().addAll("text-2xl", "font-bold", "text-accent");

        Label dateLabel = new Label();
        dateLabel.getStyleClass().addAll("text-sm", "text-muted");

        clock.getChildren().addAll(timeLabel, dateLabel);

        // 更新时间
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 EEEE", Locale.CHINESE);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            LocalDateTime now = LocalDateTime.now();
            timeLabel.setText(now.format(timeFormatter));
            dateLabel.setText(now.format(dateFormatter));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // 立即更新一次
        LocalDateTime now = LocalDateTime.now();
        timeLabel.setText(now.format(timeFormatter));
        dateLabel.setText(now.format(dateFormatter));

        return clock;
    }

    // ==================== 风险等级分布 ====================

    /**
     * 创建风险等级分布图
     */
    public static VBox createRiskDistributionChart(int low, int medium, int high, int extreme) {
        VBox container = new VBox(16);

        int total = low + medium + high + extreme;
        if (total == 0) total = 1;

        // 进度条形式
        HBox bars = new HBox(4);
        bars.setMinHeight(24);
        bars.setMaxHeight(24);
        bars.setStyle("-fx-background-radius: 12;");

        Region lowBar = createColorBar("#00ff88", (double) low / total);
        Region mediumBar = createColorBar("#ffaa00", (double) medium / total);
        Region highBar = createColorBar("#ff4444", (double) high / total);
        Region extremeBar = createColorBar("#ff0066", (double) extreme / total);

        bars.getChildren().addAll(lowBar, mediumBar, highBar, extremeBar);

        // 图例
        HBox legend = new HBox(24);
        legend.setAlignment(Pos.CENTER);

        legend.getChildren().addAll(
            createLegendItem("低风险", "#00ff88", low),
            createLegendItem("中风险", "#ffaa00", medium),
            createLegendItem("高风险", "#ff4444", high),
            createLegendItem("极高风险", "#ff0066", extreme)
        );

        container.getChildren().addAll(bars, legend);

        // 动画效果
        animateBar(lowBar, (double) low / total);
        animateBar(mediumBar, (double) medium / total);
        animateBar(highBar, (double) high / total);
        animateBar(extremeBar, (double) extreme / total);

        return container;
    }

    private static Region createColorBar(String color, double ratio) {
        Region bar = new Region();
        bar.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 12;");
        bar.setMinHeight(24);
        bar.setMaxHeight(24);
        HBox.setHgrow(bar, Priority.ALWAYS);
        bar.setMaxWidth(0); // 初始宽度为0，用于动画
        return bar;
    }

    private static void animateBar(Region bar, double targetRatio) {
        // 延迟动画让各条依次出现
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(bar.maxWidthProperty(), 0)),
            new KeyFrame(Duration.millis(800), new KeyValue(bar.maxWidthProperty(), 2000 * targetRatio, Interpolator.EASE_OUT))
        );
        timeline.setDelay(Duration.millis(300));
        timeline.play();
    }

    private static HBox createLegendItem(String label, String color, int count) {
        HBox item = new HBox(8);
        item.setAlignment(Pos.CENTER_LEFT);

        Circle dot = new Circle(6);
        dot.setFill(Color.web(color));
        dot.setEffect(new DropShadow(8, Color.web(color + "80")));

        Label text = new Label(label + " (" + count + ")");
        text.getStyleClass().addAll("text-sm", "text-secondary");

        item.getChildren().addAll(dot, text);
        return item;
    }

    // ==================== 活动时间线 ====================

    /**
     * 创建活动时间线
     */
    public static VBox createActivityTimeline(List<ActivityItem> items) {
        VBox timeline = new VBox(0);

        for (int i = 0; i < items.size(); i++) {
            ActivityItem item = items.get(i);
            boolean isLast = i == items.size() - 1;
            timeline.getChildren().add(createTimelineItem(item, isLast));
        }

        return timeline;
    }

    public static class ActivityItem {
        public String time;
        public String title;
        public String description;
        public String type; // info, success, warning, danger

        public ActivityItem(String time, String title, String description, String type) {
            this.time = time;
            this.title = title;
            this.description = description;
            this.type = type;
        }
    }

    private static HBox createTimelineItem(ActivityItem item, boolean isLast) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(0, 0, 16, 0));

        // 时间线指示器
        VBox indicator = new VBox(0);
        indicator.setAlignment(Pos.TOP_CENTER);
        indicator.setMinWidth(24);

        Circle dot = new Circle(6);
        String color = switch (item.type) {
            case "success" -> "#00ff88";
            case "warning" -> "#ffaa00";
            case "danger" -> "#ff0066";
            default -> "#00d4ff";
        };
        dot.setFill(Color.web(color));
        dot.setEffect(new DropShadow(6, Color.web(color + "60")));

        indicator.getChildren().add(dot);

        if (!isLast) {
            Region line = new Region();
            line.setMinWidth(2);
            line.setMaxWidth(2);
            line.setMinHeight(40);
            line.setStyle("-fx-background-color: #2d3a5a;");
            VBox.setMargin(line, new Insets(8, 0, 0, 0));
            indicator.getChildren().add(line);
        }

        // 内容
        VBox content = new VBox(4);
        HBox.setHgrow(content, Priority.ALWAYS);

        HBox titleRow = new HBox(12);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(item.title);
        titleLabel.getStyleClass().addAll("text-base", "text-primary");

        Label timeLabel = new Label(item.time);
        timeLabel.getStyleClass().addAll("text-xs", "text-muted");

        titleRow.getChildren().addAll(titleLabel, timeLabel);

        Label descLabel = new Label(item.description);
        descLabel.getStyleClass().addAll("text-sm", "text-secondary");
        descLabel.setWrapText(true);

        content.getChildren().addAll(titleRow, descLabel);

        row.getChildren().addAll(indicator, content);
        return row;
    }

    // ==================== 迷你图表 ====================

    /**
     * 创建迷你折线图
     */
    public static StackPane createMiniLineChart(List<Double> data, String color) {
        StackPane container = new StackPane();
        container.setMinSize(120, 40);
        container.setMaxSize(120, 40);

        if (data == null || data.isEmpty()) return container;

        // 归一化数据
        double max = data.stream().mapToDouble(d -> d).max().orElse(1);
        double min = data.stream().mapToDouble(d -> d).min().orElse(0);
        double range = max - min;
        if (range == 0) range = 1;

        Polyline line = new Polyline();
        line.setStroke(Color.web(color));
        line.setStrokeWidth(2);
        line.setFill(Color.TRANSPARENT);

        double width = 120;
        double height = 40;
        double stepX = width / (data.size() - 1);

        for (int i = 0; i < data.size(); i++) {
            double x = i * stepX;
            double y = height - ((data.get(i) - min) / range) * height * 0.8 - height * 0.1;
            line.getPoints().addAll(x, y);
        }

        // 渐变填充
        Polygon fill = new Polygon();
        fill.setFill(Color.web(color + "20"));
        fill.getPoints().addAll(0.0, height);
        for (int i = 0; i < data.size(); i++) {
            double x = i * stepX;
            double y = height - ((data.get(i) - min) / range) * height * 0.8 - height * 0.1;
            fill.getPoints().addAll(x, y);
        }
        fill.getPoints().addAll(width, height);

        container.getChildren().addAll(fill, line);

        return container;
    }

    // ==================== 系统状态指示器 ====================

    /**
     * 创建系统状态指示器
     */
    public static HBox createSystemStatus(boolean online, int activeUsers, String lastSync) {
        HBox status = new HBox(24);
        status.setAlignment(Pos.CENTER_LEFT);

        // 在线状态
        HBox onlineStatus = new HBox(8);
        onlineStatus.setAlignment(Pos.CENTER_LEFT);
        
        Circle statusDot = new Circle(5);
        statusDot.setFill(online ? Color.web("#00ff88") : Color.web("#ff0066"));
        statusDot.setEffect(new DropShadow(6, online ? Color.web("#00ff8860") : Color.web("#ff006660")));
        if (online) startPulseAnimation(statusDot);
        
        Label statusLabel = new Label(online ? "系统在线" : "系统离线");
        statusLabel.getStyleClass().addAll("text-sm", online ? "text-success" : "text-danger");
        
        onlineStatus.getChildren().addAll(statusDot, statusLabel);

        // 活跃用户
        Label usersLabel = new Label("👤 " + activeUsers + " 人在线");
        usersLabel.getStyleClass().addAll("text-sm", "text-secondary");

        // 最后同步
        Label syncLabel = new Label("🔄 " + lastSync);
        syncLabel.getStyleClass().addAll("text-sm", "text-muted");

        status.getChildren().addAll(onlineStatus, usersLabel, syncLabel);

        return status;
    }
}

JAVA_EOF
print_success "已创建: DashboardComponents.java"

#===============================================================================
# Photo.java - 照片实体类
#===============================================================================
cat > src/main/java/com/petition/model/Photo.java << 'JAVA_EOF'
package com.petition.model;

import java.time.LocalDateTime;

/**
 * 照片实体类
 * 用于存储人员的多张照片信息
 */
public class Photo {

    private Integer id;
    private Integer personId;       // 关联人员ID
    private String filePath;        // 照片文件路径
    private String fileName;        // 原始文件名
    private String description;     // 照片描述
    private Boolean isPrimary;      // 是否为主照片（头像）
    private LocalDateTime uploadTime;   // 上传时间
    private Long fileSize;          // 文件大小(字节)
    private String mimeType;        // 文件类型

    public Photo() {
        this.isPrimary = false;
        this.uploadTime = LocalDateTime.now();
    }

    public Photo(Integer personId, String filePath, String fileName) {
        this();
        this.personId = personId;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", personId=" + personId +
                ", fileName='" + fileName + '\'' +
                ", isPrimary=" + isPrimary +
                '}';
    }
}

JAVA_EOF
print_success "已创建: Photo.java"

#===============================================================================
# PhotoDao.java - 照片数据访问层
#===============================================================================
cat > src/main/java/com/petition/dao/PhotoDao.java << 'JAVA_EOF'
package com.petition.dao;

import com.petition.model.Photo;
import com.petition.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 照片数据访问层
 * 提供照片的CRUD操作
 */
public class PhotoDao {

    /**
     * 初始化照片表
     * 在应用启动时调用以确保表存在
     */
    public static void initTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS photos (
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
            )
            """;
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            
            // 创建索引
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_photos_person ON photos(person_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_photos_primary ON photos(person_id, is_primary)");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加照片
     */
    public int add(Photo photo) {
        String sql = """
            INSERT INTO photos (person_id, file_path, file_name, description, 
                               is_primary, upload_time, file_size, mime_type)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, photo.getPersonId());
            pstmt.setString(2, photo.getFilePath());
            pstmt.setString(3, photo.getFileName());
            pstmt.setString(4, photo.getDescription());
            pstmt.setInt(5, photo.getIsPrimary() ? 1 : 0);
            pstmt.setString(6, photo.getUploadTime() != null ? photo.getUploadTime().toString() : null);
            pstmt.setObject(7, photo.getFileSize());
            pstmt.setString(8, photo.getMimeType());
            
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 更新照片信息
     */
    public boolean update(Photo photo) {
        String sql = """
            UPDATE photos SET 
                file_path = ?, file_name = ?, description = ?, 
                is_primary = ?, mime_type = ?
            WHERE id = ?
            """;
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, photo.getFilePath());
            pstmt.setString(2, photo.getFileName());
            pstmt.setString(3, photo.getDescription());
            pstmt.setInt(4, photo.getIsPrimary() ? 1 : 0);
            pstmt.setString(5, photo.getMimeType());
            pstmt.setInt(6, photo.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除照片
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM photos WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除某人的所有照片
     */
    public boolean deleteByPersonId(int personId) {
        String sql = "DELETE FROM photos WHERE person_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取照片详情
     */
    public Photo getById(int id) {
        String sql = "SELECT * FROM photos WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取某人的所有照片
     */
    public List<Photo> getByPersonId(int personId) {
        String sql = "SELECT * FROM photos WHERE person_id = ? ORDER BY is_primary DESC, upload_time DESC";
        List<Photo> photos = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                photos.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return photos;
    }

    /**
     * 获取某人的照片路径列表
     */
    public List<String> getPhotoPathsByPersonId(int personId) {
        String sql = "SELECT file_path FROM photos WHERE person_id = ? ORDER BY is_primary DESC, upload_time DESC";
        List<String> paths = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                paths.add(rs.getString("file_path"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paths;
    }

    /**
     * 获取某人的主照片（头像）
     */
    public Photo getPrimaryPhoto(int personId) {
        String sql = "SELECT * FROM photos WHERE person_id = ? AND is_primary = 1 LIMIT 1";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // 如果没有主照片，返回第一张
        return getFirstPhoto(personId);
    }

    /**
     * 获取第一张照片
     */
    public Photo getFirstPhoto(int personId) {
        String sql = "SELECT * FROM photos WHERE person_id = ? ORDER BY upload_time ASC LIMIT 1";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置主照片
     */
    public boolean setPrimaryPhoto(int personId, int photoId) {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // 先取消该人所有照片的主照片标记
                PreparedStatement pstmt1 = conn.prepareStatement(
                    "UPDATE photos SET is_primary = 0 WHERE person_id = ?");
                pstmt1.setInt(1, personId);
                pstmt1.executeUpdate();
                
                // 设置指定照片为主照片
                PreparedStatement pstmt2 = conn.prepareStatement(
                    "UPDATE photos SET is_primary = 1 WHERE id = ? AND person_id = ?");
                pstmt2.setInt(1, photoId);
                pstmt2.setInt(2, personId);
                int affected = pstmt2.executeUpdate();
                
                conn.commit();
                return affected > 0;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取照片数量
     */
    public int getPhotoCount(int personId) {
        String sql = "SELECT COUNT(*) FROM photos WHERE person_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 批量添加照片
     */
    public boolean addBatch(List<Photo> photos) {
        String sql = """
            INSERT INTO photos (person_id, file_path, file_name, description, 
                               is_primary, upload_time, file_size, mime_type)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            conn.setAutoCommit(false);
            
            for (Photo photo : photos) {
                pstmt.setInt(1, photo.getPersonId());
                pstmt.setString(2, photo.getFilePath());
                pstmt.setString(3, photo.getFileName());
                pstmt.setString(4, photo.getDescription());
                pstmt.setInt(5, photo.getIsPrimary() ? 1 : 0);
                pstmt.setString(6, photo.getUploadTime() != null ? photo.getUploadTime().toString() : null);
                pstmt.setObject(7, photo.getFileSize());
                pstmt.setString(8, photo.getMimeType());
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ResultSet映射到Photo对象
     */
    private Photo mapResultSet(ResultSet rs) throws SQLException {
        Photo photo = new Photo();
        photo.setId(rs.getInt("id"));
        photo.setPersonId(rs.getInt("person_id"));
        photo.setFilePath(rs.getString("file_path"));
        photo.setFileName(rs.getString("file_name"));
        photo.setDescription(rs.getString("description"));
        photo.setIsPrimary(rs.getInt("is_primary") == 1);
        
        String uploadTime = rs.getString("upload_time");
        if (uploadTime != null) {
            photo.setUploadTime(LocalDateTime.parse(uploadTime));
        }
        
        photo.setFileSize(rs.getLong("file_size"));
        photo.setMimeType(rs.getString("mime_type"));
        
        return photo;
    }
}

JAVA_EOF
print_success "已创建: PhotoDao.java"

#===============================================================================
# PhotoService.java - 照片服务层
#===============================================================================
cat > src/main/java/com/petition/service/PhotoService.java << 'JAVA_EOF'
package com.petition.service;

import com.petition.dao.PhotoDao;
import com.petition.model.Photo;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 照片服务层
 * 处理照片的业务逻辑，包括文件存储和管理
 */
public class PhotoService {

    private final PhotoDao photoDao;
    private final String photoStoragePath;

    // 支持的图片格式
    private static final List<String> SUPPORTED_FORMATS = List.of(
        ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"
    );

    // 最大文件大小 10MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public PhotoService() {
        this.photoDao = new PhotoDao();
        // 照片存储目录，可以通过配置文件设置
        this.photoStoragePath = getStoragePath();
        ensureStorageDirectoryExists();
    }

    public PhotoService(String storagePath) {
        this.photoDao = new PhotoDao();
        this.photoStoragePath = storagePath;
        ensureStorageDirectoryExists();
    }

    /**
     * 获取存储路径
     */
    private String getStoragePath() {
        String userHome = System.getProperty("user.home");
        return userHome + File.separator + ".petition-system" + File.separator + "photos";
    }

    /**
     * 确保存储目录存在
     */
    private void ensureStorageDirectoryExists() {
        File dir = new File(photoStoragePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 上传照片
     * @param personId 人员ID
     * @param sourceFile 源文件
     * @param description 照片描述
     * @param isPrimary 是否设为主照片
     * @return 上传后的照片对象
     */
    public Photo uploadPhoto(int personId, File sourceFile, String description, boolean isPrimary) 
            throws IOException {
        
        // 验证文件
        validateFile(sourceFile);

        // 生成新文件名
        String originalName = sourceFile.getName();
        String extension = getFileExtension(originalName);
        String newFileName = generateFileName(personId, extension);

        // 创建人员专属目录
        String personDir = photoStoragePath + File.separator + personId;
        new File(personDir).mkdirs();

        // 复制文件
        Path sourcePath = sourceFile.toPath();
        Path targetPath = Paths.get(personDir, newFileName);
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        // 创建照片记录
        Photo photo = new Photo();
        photo.setPersonId(personId);
        photo.setFilePath(targetPath.toString());
        photo.setFileName(originalName);
        photo.setDescription(description);
        photo.setIsPrimary(isPrimary);
        photo.setUploadTime(LocalDateTime.now());
        photo.setFileSize(Files.size(targetPath));
        photo.setMimeType(getMimeType(extension));

        // 如果设为主照片，先取消其他主照片
        if (isPrimary) {
            clearPrimaryPhoto(personId);
        }

        // 保存到数据库
        int id = photoDao.add(photo);
        photo.setId(id);

        return photo;
    }

    /**
     * 批量上传照片
     */
    public List<Photo> uploadPhotos(int personId, List<File> files, boolean firstAsPrimary) 
            throws IOException {
        
        List<Photo> photos = new ArrayList<>();
        
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            boolean isPrimary = firstAsPrimary && i == 0;
            Photo photo = uploadPhoto(personId, file, null, isPrimary);
            photos.add(photo);
        }
        
        return photos;
    }

    /**
     * 删除照片
     */
    public boolean deletePhoto(int photoId) {
        Photo photo = photoDao.getById(photoId);
        if (photo == null) return false;

        // 删除文件
        try {
            Files.deleteIfExists(Paths.get(photo.getFilePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 删除数据库记录
        return photoDao.delete(photoId);
    }

    /**
     * 删除某人的所有照片
     */
    public boolean deleteAllPhotos(int personId) {
        List<Photo> photos = photoDao.getByPersonId(personId);
        
        // 删除所有文件
        for (Photo photo : photos) {
            try {
                Files.deleteIfExists(Paths.get(photo.getFilePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 删除人员照片目录
        try {
            Path personDir = Paths.get(photoStoragePath, String.valueOf(personId));
            if (Files.exists(personDir)) {
                Files.walk(personDir)
                    .sorted((a, b) -> -a.compareTo(b))
                    .forEach(path -> {
                        try { Files.delete(path); } catch (IOException ignored) {}
                    });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 删除数据库记录
        return photoDao.deleteByPersonId(personId);
    }

    /**
     * 获取照片
     */
    public Photo getPhoto(int photoId) {
        return photoDao.getById(photoId);
    }

    /**
     * 获取某人的所有照片
     */
    public List<Photo> getPhotosByPerson(int personId) {
        return photoDao.getByPersonId(personId);
    }

    /**
     * 获取照片路径列表
     */
    public List<String> getPhotoPathsByPerson(int personId) {
        return photoDao.getPhotoPathsByPersonId(personId);
    }

    /**
     * 获取主照片（头像）
     */
    public Photo getPrimaryPhoto(int personId) {
        return photoDao.getPrimaryPhoto(personId);
    }

    /**
     * 获取主照片路径
     */
    public String getPrimaryPhotoPath(int personId) {
        Photo photo = getPrimaryPhoto(personId);
        return photo != null ? photo.getFilePath() : null;
    }

    /**
     * 设置主照片
     */
    public boolean setPrimaryPhoto(int personId, int photoId) {
        return photoDao.setPrimaryPhoto(personId, photoId);
    }

    /**
     * 清除主照片标记
     */
    private void clearPrimaryPhoto(int personId) {
        List<Photo> photos = photoDao.getByPersonId(personId);
        for (Photo photo : photos) {
            if (photo.getIsPrimary()) {
                photo.setIsPrimary(false);
                photoDao.update(photo);
            }
        }
    }

    /**
     * 获取照片数量
     */
    public int getPhotoCount(int personId) {
        return photoDao.getPhotoCount(personId);
    }

    /**
     * 更新照片描述
     */
    public boolean updateDescription(int photoId, String description) {
        Photo photo = photoDao.getById(photoId);
        if (photo == null) return false;
        
        photo.setDescription(description);
        return photoDao.update(photo);
    }

    /**
     * 验证文件
     */
    private void validateFile(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IOException("文件不存在");
        }

        if (file.length() > MAX_FILE_SIZE) {
            throw new IOException("文件大小超过限制（最大10MB）");
        }

        String extension = getFileExtension(file.getName()).toLowerCase();
        if (!SUPPORTED_FORMATS.contains(extension)) {
            throw new IOException("不支持的文件格式，支持: " + String.join(", ", SUPPORTED_FORMATS));
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot) : "";
    }

    /**
     * 生成新文件名
     */
    private String generateFileName(int personId, String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return "photo_" + personId + "_" + timestamp + "_" + uuid + extension;
    }

    /**
     * 获取MIME类型
     */
    private String getMimeType(String extension) {
        return switch (extension.toLowerCase()) {
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".png" -> "image/png";
            case ".gif" -> "image/gif";
            case ".bmp" -> "image/bmp";
            case ".webp" -> "image/webp";
            default -> "application/octet-stream";
        };
    }

    /**
     * 初始化（创建数据库表）
     */
    public static void init() {
        PhotoDao.initTable();
    }
}

JAVA_EOF
print_success "已创建: PhotoService.java"

#===============================================================================
# PersonManageControllerExample.java - 人员管理控制器示例
#===============================================================================
cat > src/main/java/com/petition/controller/PersonManageControllerExample.java << 'JAVA_EOF'
package com.petition.controller;

import com.petition.dao.PhotoDao;
import com.petition.model.Petitioner;
import com.petition.model.Photo;
import com.petition.service.PetitionerService;
import com.petition.service.PhotoService;
import com.petition.util.*;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * 人员管理控制器示例
 * 展示如何使用向导式弹窗、下滑式查看和照片管理
 */
public class PersonManageControllerExample implements Initializable {

    @FXML private TableView<Petitioner> personTable;
    @FXML private TableColumn<Petitioner, ImageView> colAvatar;
    @FXML private TableColumn<Petitioner, String> colName;
    @FXML private TableColumn<Petitioner, String> colIdCard;
    @FXML private TableColumn<Petitioner, String> colRiskLevel;
    @FXML private TableColumn<Petitioner, String> colPhone;
    @FXML private TableColumn<Petitioner, Void> colActions;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> riskFilter;
    @FXML private Button addButton;

    private PetitionerService petitionerService;
    private PhotoService photoService;
    private ObservableList<Petitioner> personList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        petitionerService = new PetitionerService();
        photoService = new PhotoService();
        personList = FXCollections.observableArrayList();

        // 初始化照片表
        PhotoService.init();

        setupTable();
        setupFilters();
        loadData();
    }

    /**
     * 设置表格
     */
    private void setupTable() {
        // 头像列 - 显示照片缩略图
        colAvatar.setCellFactory(col -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            private final StackPane container = new StackPane(imageView);
            
            {
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
                container.getStyleClass().add("table-avatar");
                container.setOnMouseClicked(e -> {
                    Petitioner p = getTableRow().getItem();
                    if (p != null && e.getClickCount() == 1) {
                        showPhotoViewer(p);
                    }
                });
            }
            
            @Override
            protected void updateItem(ImageView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Petitioner person = getTableRow().getItem();
                    String photoPath = photoService.getPrimaryPhotoPath(person.getId());
                    if (photoPath != null) {
                        try {
                            Image img = new Image("file:" + photoPath, 40, 40, true, true);
                            imageView.setImage(img);
                        } catch (Exception ex) {
                            imageView.setImage(null);
                        }
                    } else {
                        imageView.setImage(null);
                    }
                    setGraphic(container);
                }
            }
        });

        // 其他列
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colIdCard.setCellValueFactory(new PropertyValueFactory<>("idCard"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // 风险等级列 - 带徽章
        colRiskLevel.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label badge = SlideViewerDialog.createRiskBadge(item);
                    setGraphic(badge);
                }
            }
        });
        colRiskLevel.setCellValueFactory(new PropertyValueFactory<>("riskLevel"));

        // 操作列
        colActions.setCellFactory(col -> new TableCell<>() {
            private final HBox buttons = new HBox(8);
            private final Button viewBtn = new Button("查看");
            private final Button editBtn = new Button("编辑");
            private final Button deleteBtn = new Button("删除");
            
            {
                viewBtn.getStyleClass().addAll("btn", "btn-sm", "btn-secondary");
                editBtn.getStyleClass().addAll("btn", "btn-sm", "btn-primary");
                deleteBtn.getStyleClass().addAll("btn", "btn-sm", "btn-danger");
                
                viewBtn.setOnAction(e -> {
                    Petitioner p = getTableRow().getItem();
                    if (p != null) showViewDialog(p);
                });
                
                editBtn.setOnAction(e -> {
                    Petitioner p = getTableRow().getItem();
                    if (p != null) showEditWizard(p);
                });
                
                deleteBtn.setOnAction(e -> {
                    Petitioner p = getTableRow().getItem();
                    if (p != null) confirmDelete(p);
                });
                
                buttons.getChildren().addAll(viewBtn, editBtn, deleteBtn);
                buttons.setAlignment(Pos.CENTER);
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        personTable.setItems(personList);

        // 双击查看详情
        personTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Petitioner selected = personTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showViewDialog(selected);
                }
            }
        });
    }

    /**
     * 设置筛选器
     */
    private void setupFilters() {
        riskFilter.setItems(FXCollections.observableArrayList(
            "全部", "低风险", "中风险", "高风险", "极高风险"
        ));
        riskFilter.setValue("全部");
        riskFilter.setOnAction(e -> filterData());

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterData());

        addButton.setOnAction(e -> showAddWizard());
    }

    /**
     * 加载数据
     */
    private void loadData() {
        // 这里应该从service加载数据
        // personList.setAll(petitionerService.getAll());
    }

    /**
     * 筛选数据
     */
    private void filterData() {
        // 实现筛选逻辑
    }

    /**
     * 显示新增向导
     */
    @FXML
    private void showAddWizard() {
        Petitioner newPerson = new Petitioner();
        List<String> photoPaths = new ArrayList<>();

        // 创建表单字段
        TextField nameField = new TextField();
        TextField idCardField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        ComboBox<String> genderBox = new ComboBox<>(FXCollections.observableArrayList("男", "女"));
        DatePicker birthPicker = new DatePicker();
        TextArea reasonArea = new TextArea();
        reasonArea.setPrefRowCount(3);
        StringProperty riskLevel = new SimpleStringProperty("低风险");

        // 验证属性
        BooleanProperty step1Valid = new SimpleBooleanProperty(false);
        BooleanProperty step2Valid = new SimpleBooleanProperty(true);
        BooleanProperty step3Valid = new SimpleBooleanProperty(true);

        // 步骤1验证
        nameField.textProperty().addListener((obs, o, n) -> 
            step1Valid.set(n != null && !n.trim().isEmpty() && 
                          idCardField.getText() != null && !idCardField.getText().trim().isEmpty()));
        idCardField.textProperty().addListener((obs, o, n) -> 
            step1Valid.set(n != null && !n.trim().isEmpty() && 
                          nameField.getText() != null && !nameField.getText().trim().isEmpty()));

        // 照片上传区域
        VBox photoUploader = createPhotoUploader(photoPaths);

        WizardDialog<Petitioner> wizard = new WizardDialog<>(
            getStage(), "新增人员", newPerson
        );

        // 步骤1: 基本信息
        VBox step1Content = new VBox(20);
        step1Content.getChildren().addAll(
            WizardDialog.createSection("基本信息",
                WizardDialog.createTwoColumns(
                    WizardDialog.createField("姓名 *", nameField),
                    WizardDialog.createField("性别", genderBox)
                ),
                WizardDialog.createTwoColumns(
                    WizardDialog.createField("身份证号 *", idCardField),
                    WizardDialog.createField("出生日期", birthPicker)
                ),
                WizardDialog.createField("联系电话", phoneField)
            )
        );

        // 步骤2: 风险评估
        VBox step2Content = new VBox(20);
        step2Content.getChildren().addAll(
            WizardDialog.createSection("风险评估",
                WizardDialog.createField("风险等级", WizardDialog.createRiskLevelSelector(riskLevel)),
                WizardDialog.createField("上访原因", reasonArea, "请详细描述上访原因及诉求")
            )
        );

        // 步骤3: 照片上传
        VBox step3Content = new VBox(20);
        step3Content.getChildren().addAll(
            WizardDialog.createSection("照片信息",
                WizardDialog.createField("人员照片", photoUploader, "支持上传多张照片，第一张将作为头像显示"),
                WizardDialog.createField("家庭住址", addressField)
            )
        );

        wizard.addStep(new WizardDialog.WizardStep("基本信息", "1")
                .subtitle("填写人员基本信息")
                .content(step1Content)
                .valid(step1Valid))
            .addStep(new WizardDialog.WizardStep("风险评估", "2")
                .subtitle("评估风险等级")
                .content(step2Content)
                .valid(step2Valid))
            .addStep(new WizardDialog.WizardStep("照片信息", "3")
                .subtitle("上传照片及地址")
                .content(step3Content)
                .valid(step3Valid))
            .onSubmit(person -> {
                // 保存人员信息
                person.setName(nameField.getText());
                person.setIdCard(idCardField.getText());
                person.setPhone(phoneField.getText());
                person.setAddress(addressField.getText());
                person.setGender(genderBox.getValue());
                person.setRiskLevel(riskLevel.get());
                if (birthPicker.getValue() != null) {
                    person.setBirthDate(birthPicker.getValue().toString());
                }
                person.setReason(reasonArea.getText());

                // 保存到数据库
                // int personId = petitionerService.add(person);

                // 保存照片
                // for (String path : photoPaths) {
                //     photoService.uploadPhoto(personId, new File(path), null, photoPaths.indexOf(path) == 0);
                // }

                DialogUtil.showSuccessAlert("添加成功", "人员信息已保存");
                loadData();
            })
            .onCancel(() -> {
                // 取消时清理临时照片
            });

        wizard.show();
    }

    /**
     * 显示编辑向导
     */
    private void showEditWizard(Petitioner person) {
        List<String> photoPaths = new ArrayList<>(photoService.getPhotoPathsByPerson(person.getId()));

        TextField nameField = new TextField(person.getName());
        TextField idCardField = new TextField(person.getIdCard());
        TextField phoneField = new TextField(person.getPhone());
        TextField addressField = new TextField(person.getAddress());
        ComboBox<String> genderBox = new ComboBox<>(FXCollections.observableArrayList("男", "女"));
        genderBox.setValue(person.getGender());
        TextArea reasonArea = new TextArea(person.getReason());
        reasonArea.setPrefRowCount(3);
        StringProperty riskLevel = new SimpleStringProperty(person.getRiskLevel());

        BooleanProperty valid = new SimpleBooleanProperty(true);

        VBox photoUploader = createPhotoUploader(photoPaths);

        WizardDialog<Petitioner> wizard = new WizardDialog<>(
            getStage(), "编辑人员 - " + person.getName(), person
        );

        // 步骤1
        VBox step1 = new VBox(20);
        step1.getChildren().addAll(
            WizardDialog.createSection("基本信息",
                WizardDialog.createTwoColumns(
                    WizardDialog.createField("姓名", nameField),
                    WizardDialog.createField("性别", genderBox)
                ),
                WizardDialog.createField("身份证号", idCardField),
                WizardDialog.createField("联系电话", phoneField)
            )
        );

        // 步骤2
        VBox step2 = new VBox(20);
        step2.getChildren().addAll(
            WizardDialog.createSection("风险与原因",
                WizardDialog.createField("风险等级", WizardDialog.createRiskLevelSelector(riskLevel)),
                WizardDialog.createField("上访原因", reasonArea)
            )
        );

        // 步骤3
        VBox step3 = new VBox(20);
        step3.getChildren().addAll(
            WizardDialog.createSection("照片与地址",
                WizardDialog.createField("照片管理", photoUploader),
                WizardDialog.createField("家庭住址", addressField)
            )
        );

        wizard.addStep(new WizardDialog.WizardStep("基本信息", "✎").content(step1).valid(valid))
            .addStep(new WizardDialog.WizardStep("风险评估", "⚡").content(step2).valid(valid))
            .addStep(new WizardDialog.WizardStep("照片地址", "📷").content(step3).valid(valid))
            .onSubmit(p -> {
                p.setName(nameField.getText());
                p.setIdCard(idCardField.getText());
                p.setPhone(phoneField.getText());
                p.setAddress(addressField.getText());
                p.setGender(genderBox.getValue());
                p.setRiskLevel(riskLevel.get());
                p.setReason(reasonArea.getText());

                // petitionerService.update(p);
                // 更新照片...

                DialogUtil.showSuccessAlert("保存成功", "人员信息已更新");
                loadData();
            });

        wizard.show();
    }

    /**
     * 显示查看弹窗（下滑式）
     */
    private void showViewDialog(Petitioner person) {
        List<String> photoPaths = photoService.getPhotoPathsByPerson(person.getId());

        SlideViewerDialog viewer = new SlideViewerDialog(getStage(), person.getName() + " - 详细信息");

        // 照片区域
        VBox photoGallery = SlideViewerDialog.createPhotoGallery(photoPaths);

        // 基本信息
        VBox basicInfo = new VBox(12);
        basicInfo.getChildren().addAll(
            SlideViewerDialog.createTwoColumnFields("姓名", person.getName(), "性别", person.getGender()),
            SlideViewerDialog.createTwoColumnFields("身份证号", person.getIdCard(), "联系电话", person.getPhone()),
            SlideViewerDialog.createFieldRow("家庭住址", person.getAddress())
        );

        // 风险信息
        HBox riskInfo = new HBox(16);
        riskInfo.setAlignment(Pos.CENTER_LEFT);
        riskInfo.getChildren().addAll(
            new Label("风险等级:"),
            SlideViewerDialog.createRiskBadge(person.getRiskLevel())
        );

        VBox riskSection = new VBox(12);
        riskSection.getChildren().addAll(
            riskInfo,
            SlideViewerDialog.createFieldRow("上访原因", person.getReason())
        );

        viewer.addSection(new SlideViewerDialog.ViewerSection("照片")
                .icon("📷")
                .content(photoGallery))
            .addSection(new SlideViewerDialog.ViewerSection("基本信息")
                .icon("👤")
                .content(basicInfo))
            .addSection(new SlideViewerDialog.ViewerSection("风险评估")
                .icon("⚠")
                .content(riskSection))
            .onEdit(v -> {
                viewer.close();
                showEditWizard(person);
            });

        viewer.show();
    }

    /**
     * 显示照片查看器
     */
    private void showPhotoViewer(Petitioner person) {
        List<String> paths = photoService.getPhotoPathsByPerson(person.getId());
        if (!paths.isEmpty()) {
            DialogUtil.showImageViewer(getStage(), paths, 0);
        } else {
            DialogUtil.showInfoAlert("提示", "该人员暂无照片");
        }
    }

    /**
     * 确认删除
     */
    private void confirmDelete(Petitioner person) {
        DialogUtil.showDeleteConfirmDialog(person.getName(), () -> {
            // 删除照片
            photoService.deleteAllPhotos(person.getId());
            // 删除人员
            // petitionerService.delete(person.getId());
            
            personList.remove(person);
            DialogUtil.showToast(getStage(), "删除成功", DialogUtil.ToastType.SUCCESS);
        });
    }

    /**
     * 创建照片上传组件
     */
    private VBox createPhotoUploader(List<String> photoPaths) {
        VBox container = new VBox(12);
        FlowPane photosPane = new FlowPane(12, 12);
        photosPane.setPrefWrapLength(450);

        Runnable refresh = () -> {
            photosPane.getChildren().clear();
            for (String path : photoPaths) {
                photosPane.getChildren().add(createPhotoThumb(path, () -> {
                    photoPaths.remove(path);
                    refresh.run();
                }));
            }
            // 添加按钮
            Button addBtn = new Button("+ 添加");
            addBtn.getStyleClass().addAll("btn", "btn-secondary");
            addBtn.setMinSize(80, 80);
            addBtn.setOnAction(e -> {
                List<File> files = DialogUtil.chooseImageFiles(getStage(), true);
                if (files != null) {
                    for (File f : files) {
                        photoPaths.add(f.getAbsolutePath());
                    }
                    refresh.run();
                }
            });
            photosPane.getChildren().add(addBtn);
        };

        refresh.run();
        container.getChildren().add(photosPane);
        return container;
    }

    private StackPane createPhotoThumb(String path, Runnable onRemove) {
        StackPane thumb = new StackPane();
        thumb.getStyleClass().add("photo-thumbnail");
        thumb.setMinSize(80, 80);
        thumb.setMaxSize(80, 80);

        try {
            Image img = new Image("file:" + path, 80, 80, true, true);
            ImageView iv = new ImageView(img);
            iv.setFitWidth(76);
            iv.setFitHeight(76);
            thumb.getChildren().add(iv);
        } catch (Exception e) {
            thumb.getChildren().add(new Label("📷"));
        }

        Button removeBtn = new Button("×");
        removeBtn.setStyle("-fx-background-color: #ff0066; -fx-text-fill: white; -fx-background-radius: 50; -fx-min-width: 20; -fx-min-height: 20; -fx-max-width: 20; -fx-max-height: 20; -fx-padding: 0; -fx-font-size: 12px;");
        removeBtn.setVisible(false);
        StackPane.setAlignment(removeBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(removeBtn, new Insets(2));
        removeBtn.setOnAction(e -> onRemove.run());

        thumb.setOnMouseEntered(e -> removeBtn.setVisible(true));
        thumb.setOnMouseExited(e -> removeBtn.setVisible(false));
        thumb.getChildren().add(removeBtn);

        return thumb;
    }

    private Stage getStage() {
        return (Stage) personTable.getScene().getWindow();
    }
}

JAVA_EOF
print_success "已创建: PersonManageControllerExample.java"

#===============================================================================
# DashboardControllerExample.java - 仪表盘控制器示例
#===============================================================================
cat > src/main/java/com/petition/controller/DashboardControllerExample.java << 'JAVA_EOF'
package com.petition.controller;

import com.petition.util.AnimationUtil;
import com.petition.util.DashboardComponents;
import com.petition.util.DashboardComponents.ActivityItem;
import com.petition.util.NavIcon;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.*;

/**
 * 仪表盘控制器示例
 * 展示如何使用科技感仪表盘组件
 */
public class DashboardControllerExample implements Initializable {

    @FXML private VBox dashboardContainer;
    @FXML private VBox sidebarNav;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupSidebar();
        buildDashboard();
    }

    /**
     * 设置侧边栏导航（解决乱码问题）
     */
    private void setupSidebar() {
        if (sidebarNav == null) return;

        sidebarNav.getChildren().clear();
        sidebarNav.setSpacing(4);
        sidebarNav.setPadding(new Insets(8, 0, 8, 0));

        // 主要功能
        Label mainLabel = new Label("主要功能");
        mainLabel.getStyleClass().add("nav-section-title");
        sidebarNav.getChildren().add(mainLabel);

        addNavItem(sidebarNav, "仪表盘", true);
        addNavItem(sidebarNav, "人员管理", false);
        addNavItem(sidebarNav, "高级搜索", false);
        addNavItem(sidebarNav, "数据统计", false);

        // 系统管理
        Label systemLabel = new Label("系统管理");
        systemLabel.getStyleClass().add("nav-section-title");
        systemLabel.setPadding(new Insets(16, 0, 0, 0));
        sidebarNav.getChildren().add(systemLabel);

        addNavItem(sidebarNav, "用户管理", false);
        addNavItem(sidebarNav, "系统设置", false);  // 使用NavIcon避免乱码
        addNavItem(sidebarNav, "退出登录", false);
    }

    /**
     * 添加导航项（使用NavIcon解决乱码）
     */
    private void addNavItem(VBox container, String name, boolean active) {
        HBox navItem = new HBox(14);
        navItem.setAlignment(Pos.CENTER_LEFT);
        navItem.setPadding(new Insets(14, 20, 14, 20));
        navItem.getStyleClass().add("nav-button");
        if (active) {
            navItem.getStyleClass().add("nav-button-active");
        }
        navItem.setCursor(javafx.scene.Cursor.HAND);

        // 使用NavIcon获取图标（避免乱码）
        Label icon = NavIcon.createNavIconLabel(name);
        Label text = new Label(name);
        text.getStyleClass().add(active ? "text-accent" : "text-secondary");

        navItem.getChildren().addAll(icon, text);

        // 悬停效果
        navItem.setOnMouseEntered(e -> {
            if (!navItem.getStyleClass().contains("nav-button-active")) {
                navItem.setStyle("-fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #00d4ff15, transparent);");
            }
        });
        navItem.setOnMouseExited(e -> {
            if (!navItem.getStyleClass().contains("nav-button-active")) {
                navItem.setStyle("");
            }
        });

        container.getChildren().add(navItem);
    }

    /**
     * 构建仪表盘
     */
    private void buildDashboard() {
        if (dashboardContainer == null) return;

        dashboardContainer.getChildren().clear();
        dashboardContainer.setSpacing(24);
        dashboardContainer.setPadding(new Insets(0));

        // 1. 顶部欢迎区域和实时时钟
        HBox welcomeBar = createWelcomeBar();
        dashboardContainer.getChildren().add(welcomeBar);

        // 2. 数据概览卡片
        HBox overviewBar = DashboardComponents.createOverviewBar(1234, 56, 23, 12);
        dashboardContainer.getChildren().add(overviewBar);

        // 3. 主内容区（两列布局）
        HBox mainContent = new HBox(24);
        mainContent.setAlignment(Pos.TOP_CENTER);

        // 左侧 - 风险分布和快捷操作
        VBox leftColumn = new VBox(24);
        leftColumn.setMinWidth(400);
        HBox.setHgrow(leftColumn, Priority.ALWAYS);

        // 风险分布面板
        VBox riskDistribution = DashboardComponents.createRiskDistributionChart(456, 234, 89, 23);
        VBox riskPanel = DashboardComponents.createDataPanel("风险等级分布", "实时数据统计", riskDistribution);
        leftColumn.getChildren().add(riskPanel);

        // 快捷操作面板
        Map<String, Runnable> quickActions = new LinkedHashMap<>();
        quickActions.put("新增人员", () -> System.out.println("新增人员"));
        quickActions.put("快速搜索", () -> System.out.println("快速搜索"));
        quickActions.put("数据导出", () -> System.out.println("数据导出"));
        quickActions.put("生成报表", () -> System.out.println("生成报表"));
        
        FlowPane actionsGrid = DashboardComponents.createQuickActionsGrid(quickActions);
        VBox actionsPanel = DashboardComponents.createDataPanel("快捷操作", null, actionsGrid);
        leftColumn.getChildren().add(actionsPanel);

        // 右侧 - 最近活动
        VBox rightColumn = new VBox(24);
        rightColumn.setMinWidth(350);
        HBox.setHgrow(rightColumn, Priority.SOMETIMES);

        // 最近活动时间线
        List<ActivityItem> activities = Arrays.asList(
            new ActivityItem("10:30", "新增人员", "王某某 被添加到系统", "success"),
            new ActivityItem("09:45", "风险升级", "李某某 风险等级升至高风险", "danger"),
            new ActivityItem("09:15", "信息更新", "张某某 联系方式已更新", "info"),
            new ActivityItem("08:30", "处理完成", "赵某某 案件已处理完毕", "success"),
            new ActivityItem("昨天", "系统备份", "数据库自动备份完成", "info")
        );

        VBox timeline = DashboardComponents.createActivityTimeline(activities);
        ScrollPane timelineScroll = new ScrollPane(timeline);
        timelineScroll.setFitToWidth(true);
        timelineScroll.setMaxHeight(350);
        timelineScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        VBox activityPanel = DashboardComponents.createDataPanel("最近活动", "实时更新", timelineScroll);
        rightColumn.getChildren().add(activityPanel);

        // 系统状态
        HBox systemStatus = DashboardComponents.createSystemStatus(true, 3, "刚刚");
        VBox statusPanel = DashboardComponents.createDataPanel("系统状态", null, systemStatus);
        rightColumn.getChildren().add(statusPanel);

        mainContent.getChildren().addAll(leftColumn, rightColumn);
        dashboardContainer.getChildren().add(mainContent);

        // 4. 入场动画
        playEntranceAnimation();
    }

    /**
     * 创建欢迎栏
     */
    private HBox createWelcomeBar() {
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(0, 0, 8, 0));

        VBox welcomeText = new VBox(4);
        Label greeting = new Label("欢迎回来，管理员");
        greeting.getStyleClass().addAll("text-2xl", "font-bold", "text-primary");
        
        Label subtitle = new Label("以下是系统概览数据");
        subtitle.getStyleClass().addAll("text-sm", "text-muted");
        
        welcomeText.getChildren().addAll(greeting, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox clock = DashboardComponents.createRealtimeClock();

        bar.getChildren().addAll(welcomeText, spacer, clock);
        return bar;
    }

    /**
     * 播放入场动画
     */
    private void playEntranceAnimation() {
        if (dashboardContainer == null) return;

        int delay = 0;
        for (Node child : dashboardContainer.getChildren()) {
            child.setOpacity(0);
            child.setTranslateY(20);

            javafx.animation.TranslateTransition tt = new javafx.animation.TranslateTransition(
                javafx.util.Duration.millis(400), child);
            tt.setFromY(20);
            tt.setToY(0);
            tt.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

            javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(
                javafx.util.Duration.millis(400), child);
            ft.setFromValue(0);
            ft.setToValue(1);

            javafx.animation.ParallelTransition pt = new javafx.animation.ParallelTransition(tt, ft);
            pt.setDelay(javafx.util.Duration.millis(delay));
            pt.play();

            delay += 100;
        }
    }

    /**
     * 刷新数据
     */
    @FXML
    private void refreshData() {
        AnimationUtil.pulse(dashboardContainer);
        // 重新加载数据...
    }
}

JAVA_EOF
print_success "已创建: DashboardControllerExample.java"


#===============================================================================
# 创建使用指南
#===============================================================================
print_info "创建使用指南..."

cat > UI-UPGRADE-V2-GUIDE.md << 'GUIDE_EOF'
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
GUIDE_EOF

print_success "已创建: UI-UPGRADE-V2-GUIDE.md"

#===============================================================================
# 编译验证
#===============================================================================
echo ""
print_info "尝试编译验证..."

if command -v mvn &> /dev/null; then
    if mvn compile -q 2>/dev/null; then
        print_success "Maven编译成功!"
    else
        print_warning "编译有警告或错误，请手动检查"
        print_info "运行 'mvn compile' 查看详情"
    fi
else
    print_warning "未检测到Maven，跳过编译验证"
    print_info "请手动运行 'mvn compile' 验证"
fi

#===============================================================================
# Git提交
#===============================================================================
echo ""
print_info "准备Git提交..."

if command -v git &> /dev/null && [ -d ".git" ]; then
    git add -A
    
    # 检查是否有变更
    if git diff --cached --quiet; then
        print_warning "没有检测到变更"
    else
        git commit -m "feat(ui): 升级UI系统 v2.0

- 修复侧边栏图标乱码问题（使用Unicode字符）
- 新增科技感仪表盘组件（数据卡片、风险分布图、活动时间线）
- 新增向导式表单弹窗（WizardDialog）
- 新增下滑式详情查看弹窗（SlideViewerDialog）
- 新增照片管理系统（Photo, PhotoDao, PhotoService）
- 新增丰富的动画效果（AnimationUtil）
- 新增Toast提示功能
- 优化整体视觉效果和交互体验

包含文件：
- css/main.css: 完整的科技感主题样式
- util/AnimationUtil.java: 动画工具类
- util/NavIcon.java: 导航图标辅助类
- util/DialogUtil.java: 弹窗工具类
- util/WizardDialog.java: 向导式弹窗组件
- util/SlideViewerDialog.java: 下滑式查看组件
- util/DashboardComponents.java: 仪表盘组件库
- model/Photo.java: 照片实体类
- dao/PhotoDao.java: 照片数据访问层
- service/PhotoService.java: 照片服务层
- controller/*Example.java: 使用示例"
        
        print_success "Git提交成功!"
        
        read -p "是否推送到远程仓库? (y/n) " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            if git push; then
                print_success "推送成功!"
            else
                print_error "推送失败，请手动推送"
            fi
        fi
    fi
else
    print_warning "未检测到Git仓库，跳过提交"
fi

#===============================================================================
# 完成
#===============================================================================
echo ""
echo -e "${GREEN}╔════════════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║                    升级安装完成!                               ║${NC}"
echo -e "${GREEN}╚════════════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "已安装的文件："
echo -e "  ${CYAN}CSS:${NC}"
echo -e "    • src/main/resources/css/main.css"
echo -e ""
echo -e "  ${CYAN}工具类:${NC}"
echo -e "    • AnimationUtil.java  - 动画效果"
echo -e "    • NavIcon.java        - 导航图标（解决乱码）"
echo -e "    • DialogUtil.java     - 弹窗工具"
echo -e "    • WizardDialog.java   - 向导式弹窗"
echo -e "    • SlideViewerDialog.java - 下滑式查看"
echo -e "    • DashboardComponents.java - 仪表盘组件"
echo -e ""
echo -e "  ${CYAN}照片系统:${NC}"
echo -e "    • Photo.java          - 实体类"
echo -e "    • PhotoDao.java       - 数据访问"
echo -e "    • PhotoService.java   - 服务层"
echo -e ""
echo -e "  ${CYAN}示例代码:${NC}"
echo -e "    • PersonManageControllerExample.java"
echo -e "    • DashboardControllerExample.java"
echo ""
echo -e "  ${CYAN}文档:${NC}"
echo -e "    • UI-UPGRADE-V2-GUIDE.md - 使用指南"
echo ""
echo -e "备份位置: ${YELLOW}$BACKUP_DIR${NC}"
echo ""
echo -e "${YELLOW}下一步:${NC}"
echo -e "  1. 参考示例控制器(*Example.java)集成到现有代码"
echo -e "  2. 在应用启动时调用 PhotoService.init() 初始化照片表"
echo -e "  3. 运行 'mvn compile' 验证编译"
echo -e "  4. 查看 UI-UPGRADE-V2-GUIDE.md 获取详细使用说明"
echo ""
