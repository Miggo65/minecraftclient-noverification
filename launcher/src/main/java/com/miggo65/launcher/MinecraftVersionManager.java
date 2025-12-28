package com.miggo65.launcher;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Manages Minecraft version installation and Fabric loader setup.
 */
public class MinecraftVersionManager {
    private static final String MINECRAFT_DIR = System.getProperty("user.home") + 
        File.separator + ".minecraft";
    private static final String VERSIONS_DIR = MINECRAFT_DIR + File.separator + "versions";
    private static final String MODS_DIR = MINECRAFT_DIR + File.separator + "mods";
    
    private static final String FABRIC_META_URL = "https://meta.fabricmc.net/v2";
    private static final String MINECRAFT_META_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    
    public MinecraftVersionManager() {
        // Ensure directories exist
        new File(MINECRAFT_DIR).mkdirs();
        new File(VERSIONS_DIR).mkdirs();
        new File(MODS_DIR).mkdirs();
    }
    
    /**
     * Check if a Minecraft version is installed.
     */
    public boolean checkVersion(String version) {
        File versionDir = new File(VERSIONS_DIR, version);
        File versionJson = new File(versionDir, version + ".json");
        File versionJar = new File(versionDir, version + ".jar");
        
        return versionDir.exists() && versionJson.exists() && versionJar.exists();
    }
    
    /**
     * Download and install a Minecraft version.
     */
    public void downloadVersion(String version, Consumer<String> logger) throws Exception {
        logger.accept("Fetching version manifest from Mojang...");
        
        // Get version manifest
        String manifestJson = downloadString(MINECRAFT_META_URL);
        JsonObject manifest = JsonParser.parseString(manifestJson).getAsJsonObject();
        
        // Find the version
        String versionUrl = null;
        for (var versionElement : manifest.getAsJsonArray("versions")) {
            JsonObject versionObj = versionElement.getAsJsonObject();
            if (versionObj.get("id").getAsString().equals(version)) {
                versionUrl = versionObj.get("url").getAsString();
                break;
            }
        }
        
        if (versionUrl == null) {
            throw new Exception("Version " + version + " not found in Mojang manifest");
        }
        
        logger.accept("Found version " + version + " in manifest");
        logger.accept("Downloading version metadata...");
        
        // Download version metadata
        String versionJson = downloadString(versionUrl);
        JsonObject versionData = JsonParser.parseString(versionJson).getAsJsonObject();
        
        // Create version directory
        File versionDir = new File(VERSIONS_DIR, version);
        versionDir.mkdirs();
        
        // Save version JSON
        File versionJsonFile = new File(versionDir, version + ".json");
        Files.write(versionJsonFile.toPath(), versionJson.getBytes());
        logger.accept("Version metadata saved");
        
        // Download client jar
        if (versionData.has("downloads")) {
            JsonObject downloads = versionData.getAsJsonObject("downloads");
            if (downloads.has("client")) {
                String clientUrl = downloads.getAsJsonObject("client").get("url").getAsString();
                logger.accept("Downloading Minecraft client jar...");
                
                File versionJar = new File(versionDir, version + ".jar");
                downloadFile(clientUrl, versionJar, logger);
                logger.accept("Minecraft client jar downloaded");
            }
        }
        
        logger.accept("Version " + version + " installed successfully");
    }
    
    /**
     * Check if Fabric loader is installed for a version.
     */
    public boolean checkFabricLoader(String mcVersion) {
        // Check for Fabric profile
        String fabricVersion = "fabric-loader-0.15.3-" + mcVersion;
        File fabricDir = new File(VERSIONS_DIR, fabricVersion);
        File fabricJson = new File(fabricDir, fabricVersion + ".json");
        
        return fabricDir.exists() && fabricJson.exists();
    }
    
    /**
     * Install Fabric loader for a Minecraft version.
     */
    public void installFabricLoader(String mcVersion, Consumer<String> logger) throws Exception {
        logger.accept("Fetching Fabric loader information...");
        
        // Get latest Fabric loader version
        String loaderUrl = FABRIC_META_URL + "/versions/loader/" + mcVersion;
        String loaderJson = downloadString(loaderUrl);
        JsonObject loaderData = JsonParser.parseString(loaderJson).getAsJsonObject();
        
        if (!loaderData.has("loader")) {
            throw new Exception("Fabric loader not available for version " + mcVersion);
        }
        
        String loaderVersion = loaderData.getAsJsonObject("loader").get("version").getAsString();
        logger.accept("Found Fabric loader version: " + loaderVersion);
        
        // Download Fabric profile
        String profileUrl = FABRIC_META_URL + "/versions/loader/" + mcVersion + "/" + 
            loaderVersion + "/profile/json";
        logger.accept("Downloading Fabric profile...");
        String profileJson = downloadString(profileUrl);
        
        // Create Fabric version directory
        String fabricVersion = "fabric-loader-" + loaderVersion + "-" + mcVersion;
        File fabricDir = new File(VERSIONS_DIR, fabricVersion);
        fabricDir.mkdirs();
        
        // Save Fabric profile
        File fabricJsonFile = new File(fabricDir, fabricVersion + ".json");
        Files.write(fabricJsonFile.toPath(), profileJson.getBytes());
        
        logger.accept("Fabric loader installed successfully");
    }
    
    /**
     * Install the no-verification mod.
     */
    public void installNoVerificationMod(String version, Consumer<String> logger) throws Exception {
        logger.accept("Installing no-verification mod...");
        
        // For now, we'll create a simple marker file
        // In a real implementation, you would copy the built mod jar
        File modsDir = new File(MODS_DIR);
        modsDir.mkdirs();
        
        // Check if mod jar exists in build directory
        File buildModJar = new File("build/libs/noverification-client-1.0.0.jar");
        if (buildModJar.exists()) {
            File targetModJar = new File(modsDir, "noverification-client-1.0.0.jar");
            Files.copy(buildModJar.toPath(), targetModJar.toPath(), 
                StandardCopyOption.REPLACE_EXISTING);
            logger.accept("No-verification mod installed from build");
        } else {
            logger.accept("Warning: Mod jar not found in build directory");
            logger.accept("The mod will need to be built and installed manually");
        }
    }
    
    /**
     * Download a string from a URL.
     */
    private String downloadString(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            conn.disconnect();
        }
    }
    
    /**
     * Download a file from a URL.
     */
    private void downloadFile(String urlString, File destination, Consumer<String> logger) 
            throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(30000);
        
        long fileSize = conn.getContentLengthLong();
        
        try (InputStream in = conn.getInputStream();
             FileOutputStream out = new FileOutputStream(destination)) {
            
            byte[] buffer = new byte[8192];
            long downloaded = 0;
            int bytesRead;
            int lastPercent = 0;
            
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                downloaded += bytesRead;
                
                if (fileSize > 0) {
                    int percent = (int) ((downloaded * 100) / fileSize);
                    if (percent > lastPercent && percent % 10 == 0) {
                        logger.accept("  Download progress: " + percent + "%");
                        lastPercent = percent;
                    }
                }
            }
        } finally {
            conn.disconnect();
        }
    }
}
