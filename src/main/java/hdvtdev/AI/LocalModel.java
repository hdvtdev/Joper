package hdvtdev.AI;

import hdvtdev.Tools.Annotations.Experimental;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Experimental
public class LocalModel {

    @Experimental
    public static String getResponse(String msg, Object Experimental) {
        HttpURLConnection conn = null;
        String s = "";
        try {
            URL url = new URL("http://localhost:5000/api/response");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = msg.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim()).append("\n");
                }
            }

            s = response.toString().trim();
        } catch (Exception e) {
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return s;
    }

    public static void launchModel() {
        System.out.println("[INFO] Launching model...");
        ProcessBuilder processBuilder = new ProcessBuilder("python", "LocalModelAPI.py");
        try {processBuilder.start().waitFor();} catch (Exception e) {System.err.println(e.getMessage());}
    }

}
