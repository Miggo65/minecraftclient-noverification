package com.miggo65.launcher;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;

/**
 * Handles launching the Minecraft game with offline authentication.
 */
public class GameLauncher {
    private static final String MINECRAFT_DIR = System.getProperty("user.home") + 
        File.separator + ".minecraft";
    private static final String VERSIONS_DIR = MINECRAFT_DIR + File.separator + "versions";
    private static final String LIBRARIES_DIR = MINECRAFT_DIR + File.separator + "libraries";
    private static final String ASSETS_DIR = MINECRAFT_DIR + File.separator + "assets";
    
    public GameLauncher() {
        // Ensure directories exist
        new File(LIBRARIES_DIR).mkdirs();
        new File(ASSETS_DIR).mkdirs();
    }
    
    /**
     * Launch the Minecraft game.
     */
    public void launchGame(String version, String username, Consumer<String> logger) throws Exception {
        logger.accept("Preparing to launch Minecraft " + version + "...");
        
        // Try Fabric version first
        String fabricVersion = findFabricVersion(version);
        String launchVersion = fabricVersion != null ? fabricVersion : version;
        
        File versionDir = new File(VERSIONS_DIR, launchVersion);
        File versionJson = new File(versionDir, launchVersion + ".json");
        
        if (!versionJson.exists()) {
            throw new Exception("Version JSON not found: " + versionJson.getAbsolutePath());
        }
        
        logger.accept("Reading version configuration...");
        String jsonContent = new String(Files.readAllBytes(versionJson.toPath()));
        JsonObject versionData = JsonParser.parseString(jsonContent).getAsJsonObject();
        
        // Build classpath
        logger.accept("Building classpath...");
        List<String> classpath = new ArrayList<>();
        
        // Add libraries
        if (versionData.has("libraries")) {
            JsonArray libraries = versionData.getAsJsonArray("libraries");
            for (var libElement : libraries) {
                JsonObject lib = libElement.getAsJsonObject();
                if (shouldIncludeLibrary(lib)) {
                    String libPath = getLibraryPath(lib);
                    if (libPath != null) {
                        classpath.add(libPath);
                    }
                }
            }
        }
        
        // Add client jar
        File clientJar = new File(versionDir, launchVersion + ".jar");
        if (!clientJar.exists()) {
            // Try original version jar
            clientJar = new File(VERSIONS_DIR + File.separator + version, version + ".jar");
        }
        classpath.add(clientJar.getAbsolutePath());
        
        // Get main class
        String mainClass = versionData.get("mainClass").getAsString();
        
        // Build command
        List<String> command = new ArrayList<>();
        command.add(getJavaExecutable());
        
        // JVM arguments
        command.add("-Xmx2G");
        command.add("-Xms512M");
        command.add("-Djava.library.path=" + getNativesPath(launchVersion));
        
        // Classpath
        command.add("-cp");
        command.add(String.join(File.pathSeparator, classpath));
        
        // Main class
        command.add(mainClass);
        
        // Game arguments
        command.add("--username");
        command.add(username);
        command.add("--version");
        command.add(launchVersion);
        command.add("--gameDir");
        command.add(MINECRAFT_DIR);
        command.add("--assetsDir");
        command.add(ASSETS_DIR);
        command.add("--assetIndex");
        String assetIndex = versionData.has("assetIndex") ? 
            versionData.getAsJsonObject("assetIndex").get("id").getAsString() : version;
        command.add(assetIndex);
        command.add("--uuid");
        command.add(generateOfflineUUID(username));
        command.add("--accessToken");
        command.add("0");
        command.add("--userType");
        command.add("legacy");
        command.add("--versionType");
        command.add("release");
        
        logger.accept("Launching game...");
        logger.accept("Command: " + String.join(" ", command));
        
        // Launch the game
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(MINECRAFT_DIR));
        pb.redirectErrorStream(true);
        
        Process process = pb.start();
        
        // Read output in a separate thread
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.accept("[GAME] " + line);
                }
            } catch (IOException e) {
                logger.accept("Error reading game output: " + e.getMessage());
            }
        }).start();
        
        logger.accept("Game process started!");
    }
    
    /**
     * Find the Fabric version for a Minecraft version.
     */
    private String findFabricVersion(String mcVersion) {
        File versionsDir = new File(VERSIONS_DIR);
        File[] files = versionsDir.listFiles();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (name.startsWith("fabric-loader-") && name.endsWith("-" + mcVersion)) {
                    return name;
                }
            }
        }
        return null;
    }
    
    /**
     * Check if a library should be included.
     */
    private boolean shouldIncludeLibrary(JsonObject library) {
        // Check rules
        if (library.has("rules")) {
            JsonArray rules = library.getAsJsonArray("rules");
            boolean allowed = false;
            for (var ruleElement : rules) {
                JsonObject rule = ruleElement.getAsJsonObject();
                String action = rule.get("action").getAsString();
                
                if (action.equals("allow")) {
                    allowed = true;
                }
            }
            return allowed;
        }
        return true;
    }
    
    /**
     * Get the path to a library.
     */
    private String getLibraryPath(JsonObject library) {
        if (!library.has("name")) {
            return null;
        }
        
        String name = library.get("name").getAsString();
        String[] parts = name.split(":");
        if (parts.length < 3) {
            return null;
        }
        
        String group = parts[0].replace('.', File.separatorChar);
        String artifact = parts[1];
        String version = parts[2];
        
        String path = LIBRARIES_DIR + File.separator + group + File.separator + 
                     artifact + File.separator + version + File.separator + 
                     artifact + "-" + version + ".jar";
        
        File libFile = new File(path);
        if (libFile.exists()) {
            return path;
        }
        
        return null;
    }
    
    /**
     * Get the natives path for a version.
     */
    private String getNativesPath(String version) {
        File nativesDir = new File(VERSIONS_DIR + File.separator + version + 
                                   File.separator + "natives");
        if (!nativesDir.exists()) {
            nativesDir.mkdirs();
        }
        return nativesDir.getAbsolutePath();
    }
    
    /**
     * Get the Java executable path.
     */
    private String getJavaExecutable() {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            javaBin += ".exe";
        }
        
        return javaBin;
    }
    
    /**
     * Generate an offline UUID for a username.
     */
    private String generateOfflineUUID(String username) {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes());
        return uuid.toString();
    }
}
