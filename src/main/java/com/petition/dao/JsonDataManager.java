package com.petition.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.petition.model.Petitioner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JSON数据管理器
 * 负责上访人员数据的JSON文件读写操作
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class JsonDataManager {
    /**
     * 数据文件存储路径
     */
    private static final String DATA_DIR = "data";

    /**
     * 数据文件名
     */
    private static final String DATA_FILE = "petitioners.json";

    /**
     * Jackson ObjectMapper实例
     */
    private final ObjectMapper objectMapper;

    /**
     * 数据文件完整路径
     */
    private final Path dataFilePath;

    /**
     * 内存中的数据缓存
     */
    private List<Petitioner> dataCache;

    /**
     * 默认构造函数
     * 初始化ObjectMapper并配置JSON序列化选项
     */
    public JsonDataManager() {
        this.objectMapper = new ObjectMapper();
        // 注册JavaTimeModule以支持LocalDateTime序列化
        this.objectMapper.registerModule(new JavaTimeModule());
        // 禁用将日期写为时间戳
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 启用美化输出
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // 初始化数据文件路径
        this.dataFilePath = Paths.get(DATA_DIR, DATA_FILE);
        this.dataCache = new ArrayList<>();

        // 确保数据目录存在
        ensureDataDirectoryExists();
    }

    /**
     * 构造函数（支持自定义数据目录）
     *
     * @param dataDirectory 数据目录路径
     */
    public JsonDataManager(String dataDirectory) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.dataFilePath = Paths.get(dataDirectory, DATA_FILE);
        this.dataCache = new ArrayList<>();

        ensureDataDirectoryExists();
    }

    /**
     * 确保数据目录存在
     * 如果目录不存在则创建
     */
    private void ensureDataDirectoryExists() {
        try {
            Path directory = dataFilePath.getParent();
            if (directory != null && !Files.exists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException e) {
            throw new RuntimeException("无法创建数据目录: " + e.getMessage(), e);
        }
    }

    /**
     * 加载所有上访人员数据
     * 从JSON文件读取数据并更新内存缓存
     *
     * @return 上访人员列表
     * @throws IOException 文件读取异常
     */
    public List<Petitioner> loadAll() throws IOException {
        File dataFile = dataFilePath.toFile();

        // 如果文件不存在，返回空列表
        if (!dataFile.exists()) {
            dataCache = new ArrayList<>();
            return dataCache;
        }

        // 读取JSON文件
        Petitioner[] petitioners = objectMapper.readValue(dataFile, Petitioner[].class);
        dataCache = new ArrayList<>(List.of(petitioners));

        return new ArrayList<>(dataCache);
    }

    /**
     * 保存所有上访人员数据
     * 将内存缓存中的数据写入JSON文件
     *
     * @param petitioners 上访人员列表
     * @throws IOException 文件写入异常
     */
    public void saveAll(List<Petitioner> petitioners) throws IOException {
        // 更新内存缓存
        this.dataCache = new ArrayList<>(petitioners);

        // 写入JSON文件
        objectMapper.writeValue(dataFilePath.toFile(), petitioners);
    }

    /**
     * 根据ID查找上访人员
     *
     * @param id 上访人员ID
     * @return Optional包装的上访人员对象，如果未找到则为空
     */
    public Optional<Petitioner> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }

        return dataCache.stream()
                .filter(p -> id.equals(p.getId()))
                .findFirst();
    }

    /**
     * 保存单个上访人员
     * 如果ID已存在则更新，否则新增
     *
     * @param petitioner 上访人员对象
     * @throws IOException 文件写入异常
     */
    public void save(Petitioner petitioner) throws IOException {
        if (petitioner == null) {
            throw new IllegalArgumentException("上访人员对象不能为null");
        }

        // 查找是否已存在
        Optional<Petitioner> existing = findById(petitioner.getId());

        if (existing.isPresent()) {
            // 更新现有记录
            int index = dataCache.indexOf(existing.get());
            dataCache.set(index, petitioner);
        } else {
            // 新增记录
            dataCache.add(petitioner);
        }

        // 保存到文件
        saveAll(dataCache);
    }

    /**
     * 删除指定ID的上访人员
     *
     * @param id 上访人员ID
     * @return 是否删除成功
     * @throws IOException 文件写入异常
     */
    public boolean delete(String id) throws IOException {
        if (id == null || id.isBlank()) {
            return false;
        }

        boolean removed = dataCache.removeIf(p -> id.equals(p.getId()));

        if (removed) {
            // 保存到文件
            saveAll(dataCache);
        }

        return removed;
    }

    /**
     * 批量删除上访人员
     *
     * @param ids 上访人员ID列表
     * @return 删除的记录数
     * @throws IOException 文件写入异常
     */
    public int batchDelete(List<String> ids) throws IOException {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (String id : ids) {
            boolean removed = dataCache.removeIf(p -> id.equals(p.getId()));
            if (removed) {
                count++;
            }
        }

        if (count > 0) {
            // 保存到文件
            saveAll(dataCache);
        }

        return count;
    }

    /**
     * 获取数据总数
     *
     * @return 记录总数
     */
    public int count() {
        return dataCache.size();
    }

    /**
     * 清空所有数据
     *
     * @throws IOException 文件写入异常
     */
    public void clear() throws IOException {
        dataCache.clear();
        saveAll(dataCache);
    }

    /**
     * 获取数据文件路径
     *
     * @return 数据文件路径
     */
    public Path getDataFilePath() {
        return dataFilePath;
    }

    /**
     * 刷新内存缓存
     * 从文件重新加载数据
     *
     * @throws IOException 文件读取异常
     */
    public void refresh() throws IOException {
        loadAll();
    }
}
