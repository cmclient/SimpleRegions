package pl.kuezese.region.config;

import org.bukkit.plugin.java.*;
import com.google.common.base.*;
import com.google.common.io.*;

import java.io.*;

public class Config {

    private File file;
    public String data;

    public void load(JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "regions.json");
        if (!this.file.exists()) {
            plugin.saveResource("regions.json", false);
        }
        try {
            FileInputStream stream = new FileInputStream(this.file);
            this.data = this.load(new InputStreamReader(stream, Charsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String load(Reader reader) throws IOException {
        try (BufferedReader input = new BufferedReader(reader)) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
            return builder.toString();
        }
    }

    public void save() {
        try {
            Files.createParentDirs(this.file);
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(this.file), Charsets.UTF_8)) {
                writer.write(this.data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
