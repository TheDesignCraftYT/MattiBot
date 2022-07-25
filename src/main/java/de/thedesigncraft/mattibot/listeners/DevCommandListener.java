package de.thedesigncraft.mattibot.listeners;

import de.thedesigncraft.mattibot.MattiBot;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.constants.values.MainValues;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class DevCommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromType(ChannelType.PRIVATE))
            return;

        if (event.getChannel().asPrivateChannel().getUser().equals(MainValues.owner)) {

            if (event.getMessage().getContentDisplay().startsWith("dev.")) {

                executeCommand(event, event.getMessage().getContentDisplay().replaceFirst("dev.", ""));

            }

        }

    }

    private void executeCommand(MessageReceivedEvent event, String command) {

        if (command.startsWith("calculate")) {

            calculate(event);

        } else if (command.startsWith("rebootDatabase")) {

            rebootDatabase(event);

        } else if (command.startsWith("sendNewUpdate")) {

            sendNewUpdate(event);

        } else {

            event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Das ist kein registrierter DevCommand.", false)).queue();

        }

    }

    private void sendNewUpdate(MessageReceivedEvent event) {

        try {

            String arg = event.getMessage().getContentDisplay().split(" ")[1];

            String version = Versions.versions().get(arg);

            NewUpdateListener.sendNewUpdate(version, false);

        } catch (ArrayIndexOutOfBoundsException e) {

            NewUpdateListener.sendNewUpdate(Versions.currentVersion(), false);

        }

    }

    private void rebootDatabase(MessageReceivedEvent event) {

        StringBuilder stringBuilder = new StringBuilder();

        MattiBot.jda.getGuilds().forEach(guild -> stringBuilder.append("➤ " + guild.getName() + "\n"));

        Message reply = event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed("Reboot Database", "Hier siehst du den Log des Vorgangs.")).addField("Servers", stringBuilder.toString(), false).addField("Log", "```Noch keine Lognachrichten vorhanden...```", false).build()).complete();

        MattiBot.jda.getGuilds().forEach(guild -> guild.getTextChannels().get(0).sendMessageEmbeds(EmbedTemplates.issueEmbed("Ein unerwarteter Fehler im Bot ist aufgetreten und der Bot wird diesen Server automatisch verlassen.\n\nDu kannst ihn mit folgendem Link neu einladen:\n\n> https://discord.com/api/oauth2/authorize?client_id=" + MattiBot.jda.getSelfUser().getIdLong() + "&permissions=8&scope=bot%20applications.commands\n\nWir entschuldigen uns für diesen schwerwiegenden Fehler.\n~ Das " + MattiBot.jda.getSelfUser().getName() + " Team", false)).queue());

        StringBuilder stringBuilder1 = new StringBuilder();

        stringBuilder1.append("Sent message to every server.\n");
        reply.editMessageEmbeds(new EmbedBuilder(reply.getEmbeds().get(0)).clearFields().addField("Servers", stringBuilder.toString(), false).addField("Log", "```" + stringBuilder1 + "```", false).build()).queue();

        MattiBot.jda.getGuilds().forEach(guild -> guild.leave().queue());
        stringBuilder1.append("Left every server.\n");
        reply.editMessageEmbeds(new EmbedBuilder(reply.getEmbeds().get(0)).clearFields().addField("Servers", stringBuilder.toString(), false).addField("Log", "```" + stringBuilder1 + "```", false).build()).queue();

        LiteSQL.disconnect();
        stringBuilder1.append("Database Connection offline.\n");
        reply.editMessageEmbeds(new EmbedBuilder(reply.getEmbeds().get(0)).clearFields().addField("Servers", stringBuilder.toString(), false).addField("Log", "```" + stringBuilder1 + "```", false).build()).queue();

        File file = new File("database.db");
        if (file.exists()) {

            file.delete();
            stringBuilder1.append("Old Database deleted.\n");
            reply.editMessageEmbeds(new EmbedBuilder(reply.getEmbeds().get(0)).clearFields().addField("Servers", stringBuilder.toString(), false).addField("Log", "```" + stringBuilder1 + "```", false).build()).queue();

        }

        LiteSQL.connect();
        stringBuilder1.append("Database Connection online.\n");
        reply.editMessageEmbeds(new EmbedBuilder(reply.getEmbeds().get(0)).clearFields().addField("Servers", stringBuilder.toString(), false).addField("Log", "```" + stringBuilder1 + "```", false).build()).queue();

    }

    private void calculate(MessageReceivedEvent event) {

        String rechnung = event.getMessage().getContentDisplay().replaceFirst("dev.calculate ", "");

        event.getMessage().replyEmbeds(EmbedTemplates.standardEmbed("Rechnung", "Summe für:\n```" + rechnung + "```\nSumme:\n```" + MainMethods.sumOf(rechnung) + "```")).mentionRepliedUser(false).queue();

    }

}
