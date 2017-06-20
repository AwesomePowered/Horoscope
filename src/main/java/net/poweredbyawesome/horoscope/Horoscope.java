package net.poweredbyawesome.horoscope;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public final class Horoscope extends JavaPlugin {

    public String endpoint = "http://widgets.fabulously40.com/horoscope.json?sign={sign}";
    private JsonParser jp = new JsonParser();
    String[] horoscopes = {"Aries","Taurus","Gemini","Cancer","Leo","Virgo","Libra","Scorpio","Sagittarius","Capricorn","Aquarius","Pisces"};


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a sign: ");
        while (scanner.hasNextLine()) {
            String sign = scanner.nextLine();
            if (sign.equalsIgnoreCase("quit")) {
                break;
            }
            System.out.println(new Horoscope().getHorsoscope(sign));
            System.out.println("Enter a sign: ");
        }
    }

    public void onEnable() {
        saveDefaultConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender.hasPermission("horoscope.horoscope") && args.length >=1) {
            if (!isValidSign(args[0])) {
                sender.sendMessage(ChatColor.RED + "Invalid Sign");
            }
            for (String string : getConfig().getStringList("message")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', string.replace("%HOROSCOPE%", getHorsoscope(args[0])).replace("%PLAYER%", sender.getName()).replace("%SIGN%", args[0])));
            }
        }
        return false;
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