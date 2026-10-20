
package com.campusconnect.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class StudyMaterialStorage {

    private static final String STORAGE_FOLDER =
            "src/main/resources/study_materials";

    private StudyMaterialStorage() {
    }

    public static File getStorageDirectory() {
        return new File(
                System.getProperty("user.dir"),
                STORAGE_FOLDER
        ).getAbsoluteFile();
    }

    public static boolean isWebUrl(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        try {
            URI uri = URI.create(value.trim());
            String scheme = uri.getScheme();

            return scheme != null
                    && ("http".equalsIgnoreCase(scheme)
                    || "https".equalsIgnoreCase(scheme))
                    && uri.getHost() != null;

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Copies a selected local file into shared storage.
     * Returns a path relative to study_materials.
     */
    public static String importLocalFile(File selectedFile)
            throws IOException {

        if (selectedFile == null || !selectedFile.isFile()) {
            throw new IOException(
                    "The selected file does not exist."
            );
        }

        File source = selectedFile.getCanonicalFile();
        File storage = getStorageDirectory().getCanonicalFile();

        Files.createDirectories(storage.toPath());

        Path sourcePath = source.toPath();
        Path storagePath = storage.toPath();

        // If the file is already inside shared storage,
        // keep its existing relative path.
        if (sourcePath.startsWith(storagePath)) {
            return storagePath.relativize(sourcePath)
                    .toString()
                    .replace(File.separatorChar, '/');
        }

        String fileName = source.getName();
        File destination = new File(storage, fileName);

        // Avoid overwriting a different file with the same name.
        if (destination.exists()) {
            String baseName = fileName;
            String extension = "";

            int dot = fileName.lastIndexOf('.');

            if (dot > 0) {
                baseName = fileName.substring(0, dot);
                extension = fileName.substring(dot);
            }

            int count = 1;

            do {
                destination = new File(
                        storage,
                        baseName + "_" + count + extension
                );
                count++;
            } while (destination.exists());
        }

        Files.copy(
                sourcePath,
                destination.toPath(),
                StandardCopyOption.COPY_ATTRIBUTES
        );

        return storagePath.relativize(
                destination.getCanonicalFile().toPath()
        ).toString().replace(File.separatorChar, '/');
    }

    /**
     * Resolves an existing local file path.
     * Supports legacy absolute paths and relative paths.
     */
    public static File resolveLocalFile(String savedPath)
            throws IOException {

        if (savedPath == null || savedPath.trim().isEmpty()) {
            throw new IOException("The saved file path is empty.");
        }

        String value = savedPath.trim();

        if (isWebUrl(value)) {
            throw new IOException(
                    "The saved path is a web URL, not a local file."
            );
        }

        File supplied = new File(value);

        // Support old database records containing absolute paths.
        if (supplied.isAbsolute() && supplied.isFile()) {
            return supplied.getCanonicalFile();
        }

        File projectDirectory =
                new File(System.getProperty("user.dir"));

        // First check the saved path relative to the project.
        File projectRelative = new File(projectDirectory, value);

        if (projectRelative.isFile()) {
            return projectRelative.getCanonicalFile();
        }

        // Then check relative to the shared storage folder.
        File storageRelative = new File(
                getStorageDirectory(),
                value.replace("/", File.separator)
        );

        if (storageRelative.isFile()) {
            return storageRelative.getCanonicalFile();
        }

        // Also support old records such as:
        // src/main/resources/study_materials/ADS/file.pdf
        String prefix = STORAGE_FOLDER + "/";

        if (value.replace('\\', '/').startsWith(prefix)) {
            String relative = value.replace('\\', '/')
                    .substring(prefix.length());

            File legacyRelative = new File(
                    getStorageDirectory(),
                    relative.replace("/", File.separator)
            );

            if (legacyRelative.isFile()) {
                return legacyRelative.getCanonicalFile();
            }
        }

        throw new IOException(
                "File not found on this computer.\n"
                        + "Saved path: " + savedPath + "\n"
                        + "Shared folder: "
                        + getStorageDirectory().getAbsolutePath()
        );
    }

    /**
     * Opens a web URL in the browser or a local file
     * using the operating system's default application.
     */
    public static void open(String savedPath) throws Exception {

        if (isWebUrl(savedPath)) {
            if (!Desktop.isDesktopSupported()
                    || !Desktop.getDesktop().isSupported(
                    Desktop.Action.BROWSE)) {
                throw new IOException(
                        "Opening web links is not supported on this system."
                );
            }

            Desktop.getDesktop().browse(
                    URI.create(savedPath.trim())
            );

            return;
        }

        File file = resolveLocalFile(savedPath);

        if (!Desktop.isDesktopSupported()
                || !Desktop.getDesktop().isSupported(
                Desktop.Action.OPEN)) {
            throw new IOException(
                    "Opening local files is not supported on this system."
            );
        }

        Desktop.getDesktop().open(file);
    }
}
