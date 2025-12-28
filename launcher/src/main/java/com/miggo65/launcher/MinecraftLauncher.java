package com.miggo65.launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Main launcher application for Minecraft No Verification Client.
 * Provides a GUI to select Minecraft version and launch without authentication.
 */
public class MinecraftLauncher extends JFrame {
    private static final String APP_TITLE = "Minecraft No Verification Launcher";
    private static final String[] SUPPORTED_VERSIONS = {
        "1.20.4",
        "1.20.2",
        "1.20.1",
        "1.19.4",
        "1.19.2"
    };
    
    private JComboBox<String> versionComboBox;
    private JTextField usernameField;
    private JButton launchButton;
    private JTextArea logArea;
    private JProgressBar progressBar;
    
    private MinecraftVersionManager versionManager;
    private GameLauncher gameLauncher;
    
    public MinecraftLauncher() {
        super(APP_TITLE);
        this.versionManager = new MinecraftVersionManager();
        this.gameLauncher = new GameLauncher();
        
        initializeUI();
    }
    
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Top panel - Selection
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel - Log output
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel - Progress and launch
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Apply styling
        applyDarkTheme();
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel(APP_TITLE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Version selection
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Minecraft Version:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        versionComboBox = new JComboBox<>(SUPPORTED_VERSIONS);
        panel.add(versionComboBox, gbc);
        
        // Username field
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        usernameField = new JTextField("Player");
        panel.add(usernameField, gbc);
        
        return panel;
    }
    
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Log Output"));
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(logArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        
        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("Ready");
        panel.add(progressBar, BorderLayout.CENTER);
        
        // Launch button
        launchButton = new JButton("Install & Launch Minecraft");
        launchButton.setFont(new Font("Arial", Font.BOLD, 14));
        launchButton.setPreferredSize(new Dimension(200, 40));
        launchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchMinecraft();
            }
        });
        panel.add(launchButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private void applyDarkTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel
        }
        
        Color bgColor = new Color(43, 43, 43);
        Color fgColor = Color.WHITE;
        
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.GREEN);
    }
    
    private void launchMinecraft() {
        String version = (String) versionComboBox.getSelectedItem();
        String username = usernameField.getText().trim();
        
        if (username.isEmpty()) {
            showError("Please enter a username!");
            return;
        }
        
        launchButton.setEnabled(false);
        
        // Run installation and launch in a separate thread
        new Thread(() -> {
            try {
                log("Starting Minecraft Launcher...");
                log("Selected Version: " + version);
                log("Username: " + username);
                log("");
                
                // Step 1: Check/Install Minecraft version
                updateProgress(10, "Checking Minecraft installation...");
                boolean versionInstalled = versionManager.checkVersion(version);
                
                if (!versionInstalled) {
                    updateProgress(20, "Downloading Minecraft " + version + "...");
                    log("Downloading Minecraft " + version + "...");
                    versionManager.downloadVersion(version, this::log);
                    updateProgress(40, "Minecraft " + version + " downloaded");
                    log("Minecraft " + version + " downloaded successfully");
                }
                
                // Step 2: Install Fabric loader
                updateProgress(50, "Checking Fabric Loader...");
                log("Checking Fabric Loader for version " + version + "...");
                boolean fabricInstalled = versionManager.checkFabricLoader(version);
                
                if (!fabricInstalled) {
                    updateProgress(60, "Installing Fabric Loader...");
                    log("Installing Fabric Loader...");
                    versionManager.installFabricLoader(version, this::log);
                    updateProgress(70, "Fabric Loader installed");
                    log("Fabric Loader installed successfully");
                }
                
                // Step 3: Install no-verification mod
                updateProgress(80, "Installing No-Verification mod...");
                log("Installing No-Verification mod...");
                versionManager.installNoVerificationMod(version, this::log);
                log("No-Verification mod installed");
                
                // Step 4: Launch the game
                updateProgress(90, "Launching Minecraft...");
                log("");
                log("=================================");
                log("Launching Minecraft " + version + "...");
                log("Username: " + username);
                log("Mode: Offline (No Authentication)");
                log("=================================");
                log("");
                
                gameLauncher.launchGame(version, username, this::log);
                
                updateProgress(100, "Minecraft launched successfully!");
                log("Minecraft launched successfully!");
                log("You can now close this launcher or keep it open for logs.");
                
            } catch (Exception e) {
                log("ERROR: " + e.getMessage());
                e.printStackTrace();
                showError("Failed to launch Minecraft: " + e.getMessage());
                updateProgress(0, "Error occurred");
            } finally {
                SwingUtilities.invokeLater(() -> launchButton.setEnabled(true));
            }
        }).start();
    }
    
    private void updateProgress(int value, String message) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(value);
            progressBar.setString(message);
        });
    }
    
    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
    
    private void showError(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MinecraftLauncher launcher = new MinecraftLauncher();
            launcher.setVisible(true);
        });
    }
}
