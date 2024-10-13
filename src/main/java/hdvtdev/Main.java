package hdvtdev;

import hdvtdev.AI.LocalModel;
import hdvtdev.Discord.DiscordBotBuilder;

import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) {
        CompletableFuture<Void> discordBotFuture = CompletableFuture.runAsync(DiscordBotBuilder::build);
        CompletableFuture<Void> modelFuture = CompletableFuture.runAsync(LocalModel::launchModel);
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(discordBotFuture, modelFuture);
        combinedFuture.join();
    }
}
