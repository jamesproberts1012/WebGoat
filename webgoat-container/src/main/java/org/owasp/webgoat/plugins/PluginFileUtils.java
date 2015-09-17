package org.owasp.webgoat.plugins;


import com.google.common.base.Preconditions;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

public class PluginFileUtils {

    public static boolean fileEndsWith(Path p, String s) {
        return p.getFileName().toString().endsWith(s);
    }

    public static boolean fileEndsWith(Path p, String... suffixes) {
        for (String suffix : suffixes) {
            if (fileEndsWith(p, suffix)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasParentDirectoryWithName(Path p, String s) {
        if (p == null || p.getParent() == null || p.getParent().equals(p.getRoot())) {
            return false;
        }
        if (p.getParent().getFileName().toString().equals(s)) {
            return true;
        }
        return hasParentDirectoryWithName(p.getParent(), s);
    }

    public static Path createDirsIfNotExists(Path p) throws IOException {
        if (Files.notExists(p)) {
            Files.createDirectories(p);
        }
        return p;
    }

    public static void replaceInFiles(String replace, String with, Collection<File> files) throws IOException {
        Preconditions.checkNotNull(replace);
        Preconditions.checkNotNull(with);
        Preconditions.checkNotNull(files);

        for (File file : files) {
            replaceInFile(replace, with, file);
        }
    }

    public static void replaceInFile(String replace, String with, File file) throws IOException {
        Preconditions.checkNotNull(replace);
        Preconditions.checkNotNull(with);
        Preconditions.checkNotNull(file);

        String fileAsString = "";
        try (FileInputStream fis = new FileInputStream(file);) {
            fileAsString = IOUtils.toString(fis, StandardCharsets.UTF_8.name());
            fileAsString = fileAsString.replaceAll(replace, with);
        }
        Files.write(file.toPath(), fileAsString.getBytes());
    }
}
