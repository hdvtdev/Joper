package hdvtdev.Tools.System;

import io.github.cdimascio.dotenv.Dotenv;

public class ENV {
    private static final Dotenv dotenv = Dotenv.load();

    public static String getDiscordToken() {
        return dotenv.get("DISCORD_TOKEN");
    }

    public static String getOpenAIkey() {
        return dotenv.get("OPENAI_KEY");
    }

}
