package com.petition.util;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FileUtil单元测试
 *
 * @author 刘一村
 * @version 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileUtilTest {
    private static final String TEST_DIR = "test_file_util";
    private static final String TEST_FILE = TEST_DIR + "/test.txt";
    private static final String TEST_FILE2 = TEST_DIR + "/test2.txt";
    private static final String TEST_SUBDIR = TEST_DIR + "/subdir";

    @BeforeEach
    void setUp() throws IOException {
        // 清理并创建测试目录
        cleanupTestDir();
        FileUtil.createDirectory(TEST_DIR);
    }

    @AfterEach
    void tearDown() {
        // 清理测试目录
        cleanupTestDir();
    }

    private void cleanupTestDir() {
        Path testDir = Paths.get(TEST_DIR);
        if (Files.exists(testDir)) {
            try {
                Files.walk(testDir)
                     .sorted((a, b) -> b.compareTo(a))
                     .forEach(path -> {
                         try {
                             Files.deleteIfExists(path);
                         } catch (IOException e) {
                             // 忽略
                         }
                     });
            } catch (IOException e) {
                // 忽略
            }
        }
    }

    @Test
    @Order(1)
    @DisplayName("测试exists")
    void testExists() {
        assertTrue(FileUtil.exists(TEST_DIR));
        assertFalse(FileUtil.exists(TEST_FILE));
        assertFalse(FileUtil.exists(null));
        assertFalse(FileUtil.exists(""));
    }

    @Test
    @Order(2)
    @DisplayName("测试isFile")
    void testIsFile() {
        FileUtil.writeStringToFile(TEST_FILE, "test", false);
        assertTrue(FileUtil.isFile(TEST_FILE));
        assertFalse(FileUtil.isFile(TEST_DIR));
        assertFalse(FileUtil.isFile("nonexistent"));
    }

    @Test
    @Order(3)
    @DisplayName("测试isDirectory")
    void testIsDirectory() {
        assertTrue(FileUtil.isDirectory(TEST_DIR));
        assertFalse(FileUtil.isDirectory("nonexistent"));
    }

    @Test
    @Order(4)
    @DisplayName("测试createDirectory")
    void testCreateDirectory() {
        assertTrue(FileUtil.createDirectory(TEST_SUBDIR));
        assertTrue(FileUtil.exists(TEST_SUBDIR));
        assertTrue(FileUtil.isDirectory(TEST_SUBDIR));
    }

    @Test
    @Order(5)
    @DisplayName("测试deleteFile")
    void testDeleteFile() {
        FileUtil.writeStringToFile(TEST_FILE, "test", false);
        assertTrue(FileUtil.exists(TEST_FILE));

        assertTrue(FileUtil.deleteFile(TEST_FILE));
        assertFalse(FileUtil.exists(TEST_FILE));
    }

    @Test
    @Order(6)
    @DisplayName("测试deleteDirectory")
    void testDeleteDirectory() {
        FileUtil.createDirectory(TEST_SUBDIR);
        FileUtil.writeStringToFile(TEST_SUBDIR + "/file.txt", "test", false);

        assertTrue(FileUtil.deleteDirectory(TEST_SUBDIR));
        assertFalse(FileUtil.exists(TEST_SUBDIR));
    }

    @Test
    @Order(7)
    @DisplayName("测试copyFile")
    void testCopyFile() {
        FileUtil.writeStringToFile(TEST_FILE, "test content", false);

        assertTrue(FileUtil.copyFile(TEST_FILE, TEST_FILE2, false));
        assertTrue(FileUtil.exists(TEST_FILE2));
        assertEquals("test content", FileUtil.readFileToString(TEST_FILE2));
    }

    @Test
    @Order(8)
    @DisplayName("测试copyFile-替换")
    void testCopyFileReplace() {
        FileUtil.writeStringToFile(TEST_FILE, "old content", false);
        FileUtil.writeStringToFile(TEST_FILE2, "new content", false);

        assertTrue(FileUtil.copyFile(TEST_FILE, TEST_FILE2, true));
        assertEquals("old content", FileUtil.readFileToString(TEST_FILE2));
    }

    @Test
    @Order(9)
    @DisplayName("测试moveFile")
    void testMoveFile() {
        FileUtil.writeStringToFile(TEST_FILE, "test content", false);

        assertTrue(FileUtil.moveFile(TEST_FILE, TEST_FILE2, false));
        assertFalse(FileUtil.exists(TEST_FILE));
        assertTrue(FileUtil.exists(TEST_FILE2));
        assertEquals("test content", FileUtil.readFileToString(TEST_FILE2));
    }

    @Test
    @Order(10)
    @DisplayName("测试readFileToString")
    void testReadFileToString() {
        String content = "Hello, World!\n中文测试";
        FileUtil.writeStringToFile(TEST_FILE, content, false);

        String read = FileUtil.readFileToString(TEST_FILE);
        assertEquals(content, read);
    }

    @Test
    @Order(11)
    @DisplayName("测试readFileLines")
    void testReadFileLines() {
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileUtil.writeLinesToFile(TEST_FILE, lines, false);

        List<String> read = FileUtil.readFileLines(TEST_FILE);
        assertEquals(lines, read);
    }

    @Test
    @Order(12)
    @DisplayName("测试writeStringToFile-覆盖")
    void testWriteStringToFileOverwrite() {
        FileUtil.writeStringToFile(TEST_FILE, "old content", false);
        FileUtil.writeStringToFile(TEST_FILE, "new content", false);

        assertEquals("new content", FileUtil.readFileToString(TEST_FILE));
    }

    @Test
    @Order(13)
    @DisplayName("测试writeStringToFile-追加")
    void testWriteStringToFileAppend() {
        FileUtil.writeStringToFile(TEST_FILE, "line1\n", false);
        FileUtil.writeStringToFile(TEST_FILE, "line2\n", true);

        assertEquals("line1\nline2\n", FileUtil.readFileToString(TEST_FILE));
    }

    @Test
    @Order(14)
    @DisplayName("测试writeLinesToFile")
    void testWriteLinesToFile() {
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        assertTrue(FileUtil.writeLinesToFile(TEST_FILE, lines, false));

        List<String> read = FileUtil.readFileLines(TEST_FILE);
        assertEquals(lines, read);
    }

    @Test
    @Order(15)
    @DisplayName("测试getFileSize")
    void testGetFileSize() {
        String content = "Hello, World!";
        FileUtil.writeStringToFile(TEST_FILE, content, false);

        long size = FileUtil.getFileSize(TEST_FILE);
        assertTrue(size > 0);
    }

    @Test
    @Order(16)
    @DisplayName("测试getFileExtension")
    void testGetFileExtension() {
        assertEquals("txt", FileUtil.getFileExtension("test.txt"));
        assertEquals("java", FileUtil.getFileExtension("Main.java"));
        assertEquals("gz", FileUtil.getFileExtension("archive.tar.gz")); // 只提取最后一个扩展名
        assertNull(FileUtil.getFileExtension("noextension"));
        assertNull(FileUtil.getFileExtension(".hidden"));
        assertNull(FileUtil.getFileExtension(null));
    }

    @Test
    @Order(17)
    @DisplayName("测试getFileName")
    void testGetFileName() {
        assertEquals("test.txt", FileUtil.getFileName("path/to/test.txt"));
        assertEquals("test.txt", FileUtil.getFileName("test.txt"));
        assertEquals("test.txt", FileUtil.getFileName("C:\\path\\to\\test.txt"));
    }

    @Test
    @Order(18)
    @DisplayName("测试getFileNameWithoutExtension")
    void testGetFileNameWithoutExtension() {
        assertEquals("test", FileUtil.getFileNameWithoutExtension("test.txt"));
        assertEquals("test", FileUtil.getFileNameWithoutExtension("path/to/test.txt"));
        assertEquals("archive.tar", FileUtil.getFileNameWithoutExtension("archive.tar.gz"));
    }

    @Test
    @Order(19)
    @DisplayName("测试listFiles-非递归")
    void testListFilesNonRecursive() {
        FileUtil.writeStringToFile(TEST_DIR + "/file1.txt", "content", false);
        FileUtil.writeStringToFile(TEST_DIR + "/file2.txt", "content", false);
        FileUtil.createDirectory(TEST_SUBDIR);
        FileUtil.writeStringToFile(TEST_SUBDIR + "/file3.txt", "content", false);

        List<String> files = FileUtil.listFiles(TEST_DIR, false);
        assertNotNull(files);
        assertEquals(2, files.size()); // 只有file1.txt和file2.txt
    }

    @Test
    @Order(20)
    @DisplayName("测试listFiles-递归")
    void testListFilesRecursive() {
        FileUtil.writeStringToFile(TEST_DIR + "/file1.txt", "content", false);
        FileUtil.writeStringToFile(TEST_DIR + "/file2.txt", "content", false);
        FileUtil.createDirectory(TEST_SUBDIR);
        FileUtil.writeStringToFile(TEST_SUBDIR + "/file3.txt", "content", false);

        List<String> files = FileUtil.listFiles(TEST_DIR, true);
        assertNotNull(files);
        assertEquals(3, files.size()); // file1.txt、file2.txt和subdir/file3.txt
    }

    @Test
    @Order(21)
    @DisplayName("测试listDirectories")
    void testListDirectories() {
        FileUtil.createDirectory(TEST_DIR + "/dir1");
        FileUtil.createDirectory(TEST_DIR + "/dir2");
        FileUtil.writeStringToFile(TEST_DIR + "/file.txt", "content", false);

        List<String> dirs = FileUtil.listDirectories(TEST_DIR, false);
        assertNotNull(dirs);
        assertEquals(2, dirs.size());
    }

    @Test
    @Order(22)
    @DisplayName("测试formatFileSize")
    void testFormatFileSize() {
        assertEquals("0 B", FileUtil.formatFileSize(-1));
        assertEquals("100 B", FileUtil.formatFileSize(100));
        assertEquals("1.00 KB", FileUtil.formatFileSize(1024));
        assertEquals("1.50 KB", FileUtil.formatFileSize(1536));
        assertEquals("1.00 MB", FileUtil.formatFileSize(1024 * 1024));
        assertEquals("1.50 MB", FileUtil.formatFileSize(1024 * 1024 + 512 * 1024));
        assertEquals("1.00 GB", FileUtil.formatFileSize(1024L * 1024 * 1024));
    }
}
