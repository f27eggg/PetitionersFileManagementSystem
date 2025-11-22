package com.petition.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文件工具类
 * 提供文件和目录操作功能
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class FileUtil {
    /**
     * 私有构造函数，防止实例化
     */
    private FileUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return true表示存在，false表示不存在
     */
    public static boolean exists(String filePath) {
        if (ValidationUtil.isEmpty(filePath)) {
            return false;
        }
        return Files.exists(Paths.get(filePath));
    }

    /**
     * 判断是否为文件
     *
     * @param filePath 文件路径
     * @return true表示是文件，false表示不是文件或不存在
     */
    public static boolean isFile(String filePath) {
        if (ValidationUtil.isEmpty(filePath)) {
            return false;
        }
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isRegularFile(path);
    }

    /**
     * 判断是否为目录
     *
     * @param filePath 文件路径
     * @return true表示是目录，false表示不是目录或不存在
     */
    public static boolean isDirectory(String filePath) {
        if (ValidationUtil.isEmpty(filePath)) {
            return false;
        }
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isDirectory(path);
    }

    /**
     * 创建目录（包括父目录）
     *
     * @param dirPath 目录路径
     * @return true表示创建成功，false表示创建失败
     */
    public static boolean createDirectory(String dirPath) {
        if (ValidationUtil.isEmpty(dirPath)) {
            return false;
        }

        try {
            Path path = Paths.get(dirPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return true表示删除成功，false表示删除失败
     */
    public static boolean deleteFile(String filePath) {
        if (ValidationUtil.isEmpty(filePath)) {
            return false;
        }

        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除目录（递归删除）
     *
     * @param dirPath 目录路径
     * @return true表示删除成功，false表示删除失败
     */
    public static boolean deleteDirectory(String dirPath) {
        if (ValidationUtil.isEmpty(dirPath)) {
            return false;
        }

        try {
            Path path = Paths.get(dirPath);
            if (Files.exists(path)) {
                Files.walk(path)
                     .sorted((a, b) -> b.compareTo(a)) // 逆序，先删除文件再删除目录
                     .forEach(p -> {
                         try {
                             Files.deleteIfExists(p);
                         } catch (IOException e) {
                             // 忽略单个文件删除失败
                         }
                     });
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 复制文件
     *
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @param replace 是否替换已存在的目标文件
     * @return true表示复制成功，false表示复制失败
     */
    public static boolean copyFile(String sourcePath, String targetPath, boolean replace) {
        if (ValidationUtil.isEmpty(sourcePath) || ValidationUtil.isEmpty(targetPath)) {
            return false;
        }

        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);

            // 确保目标目录存在
            Files.createDirectories(target.getParent());

            if (replace) {
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(source, target);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 移动文件
     *
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @param replace 是否替换已存在的目标文件
     * @return true表示移动成功，false表示移动失败
     */
    public static boolean moveFile(String sourcePath, String targetPath, boolean replace) {
        if (ValidationUtil.isEmpty(sourcePath) || ValidationUtil.isEmpty(targetPath)) {
            return false;
        }

        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);

            // 确保目标目录存在
            Files.createDirectories(target.getParent());

            if (replace) {
                Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.move(source, target);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 读取文件内容为字符串
     *
     * @param filePath 文件路径
     * @return 文件内容，null表示读取失败
     */
    public static String readFileToString(String filePath) {
        if (ValidationUtil.isEmpty(filePath)) {
            return null;
        }

        try {
            return Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 读取文件所有行
     *
     * @param filePath 文件路径
     * @return 文件行列表，null表示读取失败
     */
    public static List<String> readFileLines(String filePath) {
        if (ValidationUtil.isEmpty(filePath)) {
            return null;
        }

        try {
            return Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 写入字符串到文件
     *
     * @param filePath 文件路径
     * @param content 文件内容
     * @param append 是否追加（true追加，false覆盖）
     * @return true表示写入成功，false表示写入失败
     */
    public static boolean writeStringToFile(String filePath, String content, boolean append) {
        if (ValidationUtil.isEmpty(filePath) || content == null) {
            return false;
        }

        try {
            Path path = Paths.get(filePath);
            // 确保父目录存在
            Files.createDirectories(path.getParent());

            if (append) {
                Files.writeString(path, content, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else {
                Files.writeString(path, content, StandardCharsets.UTF_8);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 写入行列表到文件
     *
     * @param filePath 文件路径
     * @param lines 行列表
     * @param append 是否追加
     * @return true表示写入成功，false表示写入失败
     */
    public static boolean writeLinesToFile(String filePath, List<String> lines, boolean append) {
        if (ValidationUtil.isEmpty(filePath) || lines == null) {
            return false;
        }

        try {
            Path path = Paths.get(filePath);
            // 确保父目录存在
            Files.createDirectories(path.getParent());

            if (append) {
                Files.write(path, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else {
                Files.write(path, lines, StandardCharsets.UTF_8);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 获取文件大小（字节）
     *
     * @param filePath 文件路径
     * @return 文件大小，-1表示获取失败
     */
    public static long getFileSize(String filePath) {
        if (ValidationUtil.isEmpty(filePath)) {
            return -1;
        }

        try {
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 文件扩展名（不含点），null表示无扩展名
     */
    public static String getFileExtension(String fileName) {
        if (ValidationUtil.isEmpty(fileName)) {
            return null;
        }

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return null;
    }

    /**
     * 获取文件名（不含路径）
     *
     * @param filePath 文件路径
     * @return 文件名，null表示输入为空
     */
    public static String getFileName(String filePath) {
        if (ValidationUtil.isEmpty(filePath)) {
            return null;
        }

        return Paths.get(filePath).getFileName().toString();
    }

    /**
     * 获取文件名（不含路径和扩展名）
     *
     * @param filePath 文件路径
     * @return 文件名，null表示输入为空
     */
    public static String getFileNameWithoutExtension(String filePath) {
        String fileName = getFileName(filePath);
        if (fileName == null) {
            return null;
        }

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }

    /**
     * 列出目录下的所有文件
     *
     * @param dirPath 目录路径
     * @param recursive 是否递归列出子目录中的文件
     * @return 文件路径列表，null表示列出失败
     */
    public static List<String> listFiles(String dirPath, boolean recursive) {
        if (ValidationUtil.isEmpty(dirPath)) {
            return null;
        }

        try {
            Path path = Paths.get(dirPath);
            if (!Files.isDirectory(path)) {
                return null;
            }

            Stream<Path> pathStream;
            if (recursive) {
                pathStream = Files.walk(path)
                                  .filter(Files::isRegularFile);
            } else {
                pathStream = Files.list(path)
                                  .filter(Files::isRegularFile);
            }

            return pathStream.map(Path::toString)
                            .collect(Collectors.toList());

        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 列出目录下的所有子目录
     *
     * @param dirPath 目录路径
     * @param recursive 是否递归列出
     * @return 目录路径列表，null表示列出失败
     */
    public static List<String> listDirectories(String dirPath, boolean recursive) {
        if (ValidationUtil.isEmpty(dirPath)) {
            return null;
        }

        try {
            Path path = Paths.get(dirPath);
            if (!Files.isDirectory(path)) {
                return null;
            }

            Stream<Path> pathStream;
            if (recursive) {
                pathStream = Files.walk(path)
                                  .filter(Files::isDirectory)
                                  .filter(p -> !p.equals(path)); // 排除根目录自身
            } else {
                pathStream = Files.list(path)
                                  .filter(Files::isDirectory);
            }

            return pathStream.map(Path::toString)
                            .collect(Collectors.toList());

        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 格式化文件大小为可读字符串
     *
     * @param size 文件大小（字节）
     * @return 格式化后的字符串（如：1.5 KB, 2.3 MB）
     */
    public static String formatFileSize(long size) {
        if (size < 0) {
            return "0 B";
        }

        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
        }
    }
}
