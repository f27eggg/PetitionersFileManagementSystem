# Claude Code 项目配置

## 项目基本信息

**项目名称**：上访人员重点监控信息管理系统
**开发人员**：刘一村
**项目版本**：v1.0.0
**技术栈**：Java 17 + JavaFX 17.0.2 + Maven

---

## 开发规范

### 1. 项目语言

**所有代码、注释、文档均使用中文**

- **类注释**：使用中文JavaDoc注释
- **方法注释**：使用中文描述功能、参数、返回值
- **变量命名**：使用英文驼峰命名，但注释用中文
- **提交信息**：使用中文Git提交信息
- **文档编写**：所有Markdown文档使用中文

**示例**：
```java
/**
 * 上访人员信息实体类
 * 包含个人信息、在京关系人、信访案件、评估结果四大模块
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class Petitioner {
    /**
     * 唯一标识符（UUID）
     */
    private String id;

    /**
     * 获取上访人员的危险等级
     *
     * @return 危险等级枚举值
     */
    public RiskLevel getRiskLevel() {
        return this.riskAssessment.getRiskLevel();
    }
}
```

---

### 2. 开发流程

#### 2.1 按阶段分步开发

**严格遵循TODO.md中定义的开发阶段顺序**：

1. ✅ 阶段一：项目初始化（已完成）
2. ✅ 阶段二：开发环境准备（已完成）
3. ⏭️ 阶段三：数据层开发（当前）
4. ⏭️ 阶段四：业务逻辑层开发
5. ⏭️ 阶段五：UI界面开发
6. ⏭️ 阶段六：UI样式美化
7. ⏭️ 阶段七：功能集成与测试
8. ⏭️ 阶段八：打包与部署

**开发原则**：
- 每个阶段必须完整完成后才能进入下一阶段
- 不能跳过阶段或并行开发多个阶段
- 每个阶段内的任务按顺序逐个完成

#### 2.2 开发步骤（每个阶段）

**Step 1: 查看任务清单**
```bash
# 查看TODO.md，明确当前阶段的任务列表
```

**Step 2: 逐个完成任务**
- 按照TODO.md中的任务顺序开发
- 每完成一个任务立即标记为完成
- 编写代码时遵循项目规范

**Step 3: 代码提交**
- 每完成一个重要功能或子阶段，立即提交到Git
- 提交信息格式：`feat/fix/docs: 简短描述`
- 提交信息必须包含Claude Code标识

**Step 4: 同步更新TODO.md**
- 将已完成的任务标记为 ✅
- 更新进度百分比
- 更新"当前进度总览"表格
- 提交TODO.md的变更到Git

**Step 5: 阶段完成确认**
- 当前阶段所有任务完成后，等待确认
- 确认无误后进入下一阶段

---

### 3. Git提交规范

#### 3.1 提交信息格式

```
<类型>: <简短描述>

<详细说明>（可选）

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>
```

#### 3.2 提交类型

| 类型 | 说明 | 示例 |
|------|------|------|
| `feat` | 新功能 | `feat: 实现上访人员数据模型` |
| `fix` | Bug修复 | `fix: 修复身份证号验证逻辑错误` |
| `docs` | 文档更新 | `docs: 更新TODO.md - 阶段三完成` |
| `style` | 代码格式调整 | `style: 统一代码缩进格式` |
| `refactor` | 重构 | `refactor: 优化JsonDataManager结构` |
| `test` | 测试相关 | `test: 添加PetitionerService单元测试` |
| `chore` | 构建/配置变更 | `chore: 更新pom.xml依赖版本` |

#### 3.3 提交时机

**立即提交的场景**：
1. 完成一个完整的功能模块（如一个Service类）
2. 完成一个子阶段的所有任务
3. 完成一个阶段的所有任务
4. 更新TODO.md进度

**示例提交流程**：
```bash
# 1. 完成Gender枚举类
git add src/main/java/com/petition/model/enums/Gender.java
git commit -m "feat: 创建Gender性别枚举类"

# 2. 完成所有枚举类后
git add src/main/java/com/petition/model/enums/
git commit -m "feat: 完成所有枚举类创建（6个）"

# 3. 同步更新TODO.md
git add TODO.md
git commit -m "docs: 更新TODO.md - 枚举类创建完成"
```

---

### 4. TODO.md更新规范

#### 4.1 任务状态标识

- ✅ 已完成
- 🔄 进行中
- ⏸️ 暂停
- ⏭️ 待开始
- ❌ 已取消

#### 4.2 更新内容

**每完成一个任务**：
```markdown
- ✅ 创建Gender.java（性别）  # 从 ⏭️ 改为 ✅
```

**每完成一个子阶段**：
```markdown
### 3.1 数据模型实现（已完成 ✅）  # 更新标题
```

**每完成一个阶段**：
```markdown
## 阶段三：数据层开发（已完成 ✅）  # 更新标题

# 同时更新"当前进度总览"表格
| 阶段三：数据层开发 | ✅ 已完成 | 100% |
```

#### 4.3 进度百分比计算

**阶段完成度** = 已完成任务数 / 总任务数 × 100%

**示例**：
- 阶段三总任务：35个
- 已完成：20个
- 完成度：20/35 × 100% ≈ 57%

---

### 5. 代码规范

#### 5.1 文件组织

**包结构**：
```
com.petition
├── model/              # 数据模型层
│   ├── enums/         # 枚举类
│   └── *.java         # 实体类
├── dao/               # 数据访问层
├── service/           # 业务逻辑层
├── controller/        # 控制器层
├── view/              # 视图相关
├── util/              # 工具类
└── exception/         # 自定义异常
```

#### 5.2 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 类名 | 大驼峰 | `PetitionerService` |
| 方法名 | 小驼峰 | `getPetitionerById()` |
| 变量名 | 小驼峰 | `riskLevel` |
| 常量名 | 全大写下划线 | `MAX_PAGE_SIZE` |
| 包名 | 全小写 | `com.petition.service` |

#### 5.3 注释规范

**类注释**（必需）：
```java
/**
 * 类的简要描述
 * 详细说明（可选）
 *
 * @author 刘一村
 * @version 1.0.0
 */
```

**方法注释**（公共方法必需）：
```java
/**
 * 方法功能描述
 *
 * @param paramName 参数说明
 * @return 返回值说明
 * @throws ExceptionType 异常说明
 */
```

---

### 6. 开发环境信息

**JDK版本**：17.0.12
**Maven版本**：3.6.3
**Maven镜像**：阿里云镜像（已配置）
**IDE**：IntelliJ IDEA
**Scene Builder**：已集成

**Maven路径**：
```
D:\Program Files\Apache-maven-3.6.3\apache-maven-3.6.3\bin\mvn
```

**项目路径**：
```
D:\Program Files\JetBrains\PROJECT_JAVA\PetitionersFileManagementSystem
```

**启动命令**：
```bash
"D:\Program Files\Apache-maven-3.6.3\apache-maven-3.6.3\bin\mvn" -f "d:\Program Files\JetBrains\PROJECT_JAVA\PetitionersFileManagementSystem\pom.xml" javafx:run
```

或在项目目录下：
```bash
cd "d:\Program Files\JetBrains\PROJECT_JAVA\PetitionersFileManagementSystem"
"D:\Program Files\Apache-maven-3.6.3\apache-maven-3.6.3\bin\mvn" javafx:run
```

---

### 7. 当前开发状态

**当前阶段**：阶段三 - 数据层开发
**当前进度**：待开始（0%）
**下一步任务**：创建枚举类（Gender, Education, MaritalStatus等）

**已完成阶段**：
- ✅ 阶段一：项目初始化（100%）
- ✅ 阶段二：开发环境准备（100%）

---

### 8. 特别注意事项

1. **不要跳过阶段**：必须按TODO.md顺序开发
2. **及时提交代码**：每完成一个功能模块立即提交
3. **同步更新文档**：代码提交后立即更新TODO.md
4. **使用中文**：所有注释、文档、提交信息使用中文
5. **遵循设计文档**：参考docs/目录下的设计文档
6. **单元测试**：重要功能必须编写测试用例

---

*配置文件版本：v1.0*
*创建日期：2025年11月22日*
*最后更新：2025年11月22日*
