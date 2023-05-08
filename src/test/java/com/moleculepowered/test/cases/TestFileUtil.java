package com.moleculepowered.test.cases;

import com.moleculepowered.api.util.FileUtil;
import com.moleculepowered.test.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;

public class TestFileUtil extends AbstractTest {

    private final String RESOURCE_PATH = "temp-resource.txt";
    private final File TEMP_FILE = new File(plugin.getDataFolder(), "temp.txt");

    @Test
    @DisplayName("Ensure that we can retrieve resources from our internal resource folder")
    public void testResourceGetting() {
        InputStream input = FileUtil.getResource(RESOURCE_PATH);
        Assertions.assertNotNull(input);
    }

    @Test
    @DisplayName("Ensure that we can save resources from our internal resource folder")
    public void testResourceSaving() {
        InputStream input = FileUtil.getResource(RESOURCE_PATH);

        FileUtil.saveResource(input, TEMP_FILE);
        Assertions.assertTrue(TEMP_FILE.delete());

        FileUtil.saveResource(RESOURCE_PATH, TEMP_FILE);
        Assertions.assertTrue(TEMP_FILE.delete());
    }

    @Test
    @DisplayName("Ensure that we can get file names correctly")
    public void testFilenameGetting() {
        Assertions.assertEquals(FileUtil.getFileName(TEMP_FILE), TEMP_FILE.getName());
        Assertions.assertEquals(FileUtil.getFileName("hello/there/world/temp.txt"), TEMP_FILE.getName());
        Assertions.assertEquals(FileUtil.getFileName(Paths.get("hello/there/world/temp.txt")), TEMP_FILE.getName());
    }
}
