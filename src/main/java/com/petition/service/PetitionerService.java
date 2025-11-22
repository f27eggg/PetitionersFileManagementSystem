package com.petition.service;

import com.petition.dao.JsonDataManager;
import com.petition.model.Petitioner;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * 上访人员管理服务
 * 提供上访人员的增删改查等核心业务功能
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class PetitionerService {
    /**
     * 数据管理器
     */
    private final JsonDataManager dataManager;

    /**
     * 默认构造函数
     */
    public PetitionerService() {
        this.dataManager = new JsonDataManager();
    }

    /**
     * 带数据目录的构造函数
     *
     * @param dataDirectory 数据目录路径
     */
    public PetitionerService(String dataDirectory) {
        this.dataManager = new JsonDataManager(dataDirectory);
    }

    /**
     * 带数据管理器的构造函数
     *
     * @param dataManager 数据管理器实例
     */
    public PetitionerService(JsonDataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * 获取所有上访人员
     *
     * @return 上访人员列表
     * @throws IOException 数据读取异常
     */
    public List<Petitioner> getAllPetitioners() throws IOException {
        return dataManager.loadAll();
    }

    /**
     * 根据ID获取上访人员
     *
     * @param id 上访人员ID
     * @return Optional包装的上访人员对象
     */
    public Optional<Petitioner> getPetitionerById(String id) {
        return dataManager.findById(id);
    }

    /**
     * 添加上访人员
     * 自动加载最新数据，添加新人员，然后保存
     *
     * @param petitioner 上访人员对象
     * @throws IOException 数据操作异常
     */
    public void addPetitioner(Petitioner petitioner) throws IOException {
        if (petitioner == null) {
            throw new IllegalArgumentException("上访人员对象不能为null");
        }

        // 验证必填字段
        validatePetitioner(petitioner);

        // 检查身份证号是否已存在
        dataManager.loadAll();
        String idCard = petitioner.getIdCard();
        if (idCard != null) {
            boolean exists = dataManager.loadAll().stream()
                    .anyMatch(p -> idCard.equals(p.getIdCard()));
            if (exists) {
                throw new IllegalArgumentException("身份证号已存在: " + idCard);
            }
        }

        // 保存数据
        dataManager.save(petitioner);
    }

    /**
     * 更新上访人员信息
     *
     * @param petitioner 上访人员对象
     * @throws IOException 数据操作异常
     */
    public void updatePetitioner(Petitioner petitioner) throws IOException {
        if (petitioner == null) {
            throw new IllegalArgumentException("上访人员对象不能为null");
        }

        // 验证必填字段
        validatePetitioner(petitioner);

        // 检查是否存在
        Optional<Petitioner> existing = dataManager.findById(petitioner.getId());
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("上访人员不存在，ID: " + petitioner.getId());
        }

        // 更新修改时间
        petitioner.touch();

        // 保存数据
        dataManager.save(petitioner);
    }

    /**
     * 删除上访人员
     *
     * @param id 上访人员ID
     * @return 是否删除成功
     * @throws IOException 数据操作异常
     */
    public boolean deletePetitioner(String id) throws IOException {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID不能为空");
        }

        return dataManager.delete(id);
    }

    /**
     * 批量删除上访人员
     *
     * @param ids 上访人员ID列表
     * @return 删除的记录数
     * @throws IOException 数据操作异常
     */
    public int batchDelete(List<String> ids) throws IOException {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        return dataManager.batchDelete(ids);
    }

    /**
     * 获取上访人员总数
     *
     * @return 记录总数
     */
    public int getTotalCount() {
        return dataManager.count();
    }

    /**
     * 验证上访人员必填字段
     *
     * @param petitioner 上访人员对象
     */
    private void validatePetitioner(Petitioner petitioner) {
        if (petitioner.getPersonalInfo() == null) {
            throw new IllegalArgumentException("个人信息不能为空");
        }

        String name = petitioner.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("姓名不能为空");
        }

        String idCard = petitioner.getIdCard();
        if (idCard == null || idCard.isBlank()) {
            throw new IllegalArgumentException("身份证号不能为空");
        }

        // 验证身份证号格式（简单验证：15位或18位）
        if (!idCard.matches("^\\d{15}(\\d{2}[0-9Xx])?$")) {
            throw new IllegalArgumentException("身份证号格式不正确: " + idCard);
        }
    }

    /**
     * 刷新数据
     * 从文件重新加载数据
     *
     * @throws IOException 数据读取异常
     */
    public void refresh() throws IOException {
        dataManager.refresh();
    }

    /**
     * 清空所有数据
     *
     * @throws IOException 数据操作异常
     */
    public void clearAll() throws IOException {
        dataManager.clear();
    }
}
