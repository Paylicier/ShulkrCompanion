package fr.paylicier.shulkr;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import fr.paylicier.shulkr.Shulkr;

public class Utils {

    public static List<String> getLogs() {
        List<String> logs = new ArrayList<>();
        File logFolder = new File("logs");
        if (logFolder.exists()) {
            for (File file : Objects.requireNonNull(logFolder.listFiles())) {
                logs.add(file.getName());
            }
        }
        return logs;
    }

    public static String getLogContent(String log) {
        File logFile = new File("logs/" + log);
        if (logFile.exists()) {

            if (logFile.getName().endsWith(".gz")) {
                try {
                    return gunzipIt(logFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(logFile));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    reader.close();
                    return content.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public static String uploadLogs(String logs) throws IOException {

        String url = Shulkr.getPlugin(Shulkr.class).getConfig().getString("instance-url");
        url = url != null ? url : "https://shulkr.notri1.fr/api";

        HttpURLConnection connection = (HttpURLConnection) new URL(url+"/1/log").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);
        connection.getOutputStream().write(("content=" + logs).getBytes());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();

        return content.toString();
    }

    private static String gunzipIt(String path) throws IOException {

        StringBuilder content = new StringBuilder();

        byte[] buffer = new byte[1024];
        GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(path));
        int read;
        while ((read = gzis.read(buffer)) > 0) {
            content.append(new String(buffer, 0, read));
        }
        gzis.close();
        return content.toString();
    }
}
