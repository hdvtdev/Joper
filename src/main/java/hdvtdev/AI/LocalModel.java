package hdvtdev.AI;

import hdvtdev.Tools.Annotations.Experimental;
import hdvtdev.Tools.System.ENV;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Experimental
public class LocalModel {

    @Experimental
    public static String getResponse(String prompt, @Nullable String model) {
        prompt = "(ограничься 2000 символами)" + prompt;
        if (model == null) model = "qwen2.5:3b";
        String textResponse = "no response";
        String url = ENV.getServerURL();

        try {


            String requestBody = String.format("""
    {
        "model": "%s",
        "prompt": "%s",
        "stream": false
    }
    """, model, prompt);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            client.close();

            textResponse = response.body();
            System.out.println("[AI] Response ready.");

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return toHumanFormat(textResponse);
    }

    private static String toHumanFormat(String response) {

        String key = "\"response\":\"";
        int startIndex = response.indexOf(key);
        if (startIndex != -1) {
            startIndex += key.length();
            int endIndex = response.indexOf("\"", startIndex);
            if (endIndex != -1) {
                response = response.substring(startIndex, endIndex);
            }
        }

        String[] parts = response.split("\\\\n");
        StringBuilder responseBuilder = new StringBuilder();
        for (String part : parts) {
            responseBuilder.append(part).append("\n");
        }
        response = responseBuilder.toString();

        return response.replaceAll("\n", " ");
    }

}
