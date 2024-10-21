package hdvtdev.Discord;

import hdvtdev.AI.LocalModel;
import hdvtdev.Discord.Commands.EmbedSchedule;
import net.dv8tion.jda.api.EmbedBuilder;
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
            System.out.println("11212");
            String msg = event.getMessage().getContentRaw();
            String botMention = "<@" + event.getJDA().getSelfUser().getId() + ">";
            msg = msg.replace(botMention, "").trim();
            String response = LocalModel.getResponse(msg, null);
            event.getMessage().reply(response).queue();
        }
    }



    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        switch (event.getName()) {
            case "clear-ram" -> {
                event.replyEmbeds(new EmbedBuilder().setTitle("Очистка...").setColor(Color.decode("#1a2426")).build()).setEphemeral(true).queue();
                System.gc();
            }
            case "schedule" -> schedule(event);
        }



    }

    private static void schedule(SlashCommandInteractionEvent event) {
        event.replyEmbeds(new EmbedSchedule().scheduleEmbedBuilder(event.getOption("group").getAsString(), event.getOption("dayofweek").getAsString(), event.getOption("week").getAsString())).queue();
    }



}
