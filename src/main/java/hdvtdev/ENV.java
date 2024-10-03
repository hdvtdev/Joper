package hdvtdev;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;

public class ENV {
    private static final Dotenv dotenv = Dotenv.load();

    public static String getDiscordToken() {
        return dotenv.get("DISCORD_TOKEN");
    }

}
