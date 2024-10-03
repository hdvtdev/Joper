package hdvtdev;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class EventListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getAuthor().isBot())
            return;

        if (event.getMessage().getMentions().isMentioned(event.getJDA().getSelfUser())) {
            User user = event.getAuthor();
            event.getChannel().sendMessage(user.getAsMention() + " пошел нахуй ").queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        switch (event.getName()) {
            case "clear-ram" -> {
                event.replyEmbeds(new EmbedBuilder().setTitle("Очистка...").setColor(Color.decode("#1a2426")).build()).setEphemeral(true).queue();
                System.gc();
            }
        }



    }




}
