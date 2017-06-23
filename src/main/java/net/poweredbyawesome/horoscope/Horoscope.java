package net.poweredbyawesome.horoscope;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Horoscope extends JavaPlugin {

    public String endpoint = "http://widgets.fabulously40.com/horoscope.json?sign={sign}";
    private JsonParser jp = new JsonParser();
    public static Horoscope instance;
    String[] horoscopes = {"Aries","Taurus","Gemini","Cancer","Leo","Virgo","Libra","Scorpio","Sagittarius","Capricorn","Aquarius","Pisces"};

    public void onEnable() {
        instance = this;
        getCommand("horoscope").setExecutor(new CommandHoroscope());
        saveDefaultConfig();
    }

    public void sendHoroscope(Player p, String sign) {
        if (!isValidSign(sign)) {
            p.sendMessage(ChatColor.RED + "Invalid Sign");
        }
        for (String string : getConfig().getStringList("message")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', string.replace("%HOROSCOPE%", getHorsoscope(sign)).replace("%PLAYER%", p.getName()).replace("%SIGN%", sign)));
        }
    }

    public String getHorsoscope(String sign) {
        try {
            URL url = new URL(endpoint.replace("{sign}", sign));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            return parseData(connection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Could not find horoscope";
    }

    public String parseData(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        JsonObject jsonObject = (JsonObject) jp.parse(reader);
        return String.valueOf(jsonObject.get("horoscope").getAsJsonObject().get("horoscope"));
    }

    public boolean isValidSign(String sign) {
        for (String s : horoscopes) {
            if (s.equalsIgnoreCase(sign)) {
                return true;
            }
        }
        return false;
    }
}