package de.thedesigncraft.mattibot.functions.youtube;

import de.thedesigncraft.mattibot.MattiBot;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class YoutubeMessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromGuild())
            return;

        // Set Channel
        if (event.getMessage().getMessageReference() != null && event.getMessage().getReferencedMessage() != null && event.getMessage().getReferencedMessage().getAuthor().equals(MattiBot.jda.getSelfUser()) && event.getMessage().getReferencedMessage().getEmbeds().get(0).getTitle().equals(YoutubeEmbeds.setChannel().getTitle()) && event.getMessage().getReferencedMessage().getEmbeds().get(0).getDescription().startsWith(YoutubeEmbeds.setChannel().getDescription())) {

            if (event.getMessage().getReferencedMessage().getActionRows().get(0).getButtons().get(0).getId().replace("youtube.goToMainPage&id=", "").equals(event.getAuthor().getId())) {

                if (!event.getMessage().getMentions().getChannels().isEmpty()) {

                    try {

                        TextChannel textChannel = (TextChannel) event.getMessage().getMentions().getChannels().get(0);

                        setChannel(event.getGuild(), textChannel, event.getMessage());

                    } catch (ClassCastException e) {

                        event.getMessage().getReferencedMessage().editMessageEmbeds(new EmbedBuilder(YoutubeEmbeds.setChannel()).appendDescription("\n\n```❌ Du musst einen Textkanal angeben.```").build()).setActionRows(ActionRow.of(YoutubeActionRows.goToMainPage(event.getAuthor()))).queue();

                        event.getMessage().delete().queue();

                    }

                } else if (!event.getMessage().getContentDisplay().equals("") && !event.getMessage().getContentDisplay().equals(" ")) {

                    try {

                        TextChannel textChannel = event.getGuild().getTextChannelById(event.getMessage().getContentDisplay().replace(" ", ""));

                        setChannel(event.getGuild(), textChannel, event.getMessage());

                    } catch (NumberFormatException e) {

                        event.getMessage().getReferencedMessage().editMessageEmbeds(new EmbedBuilder(YoutubeEmbeds.setChannel()).appendDescription("\n\n```❌ Du musst die Id eines Textkanals angeben.```").build()).setActionRows(ActionRow.of(YoutubeActionRows.goToMainPage(event.getAuthor()))).queue();

                        event.getMessage().delete().queue();

                    }

                }

            } else {

                event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Nur " + event.getGuild().getMemberById(event.getMessage().getReferencedMessage().getActionRows().get(0).getButtons().get(0).getId().replace("youtube.goToMainPage&id=", "")).getAsMention() + " hat Zugriff auf diese Nachricht.", true)).queue(message -> {

                    event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                    message.delete().queueAfter(10, TimeUnit.SECONDS);

                });

            }

        }

        // Set Message
        if (event.getMessage().getMessageReference() != null && event.getMessage().getReferencedMessage() != null && event.getMessage().getReferencedMessage().getAuthor().equals(MattiBot.jda.getSelfUser()) && event.getMessage().getReferencedMessage().getEmbeds().get(0).getTitle().equals(YoutubeEmbeds.setMessage().getTitle()) && event.getMessage().getReferencedMessage().getEmbeds().get(0).getDescription().startsWith(YoutubeEmbeds.setMessage().getDescription())) {

            if (event.getMessage().getReferencedMessage().getActionRows().get(0).getButtons().get(0).getId().replace("youtube.goToMainPage&id=", "").equals(event.getAuthor().getId())) {

                setMessage(event.getGuild(), event.getMessage());

            } else {

                event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Nur " + event.getGuild().getMemberById(event.getMessage().getReferencedMessage().getActionRows().get(0).getButtons().get(0).getId().replace("youtube.goToMainPage&id=", "")).getAsMention() + " hat Zugriff auf diese Nachricht.", true)).queue(message -> {

                    event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                    message.delete().queueAfter(10, TimeUnit.SECONDS);

                });

            }

        }

    }

    private void setMessage(Guild guild, Message message) {

        if (!YoutubeEmbeds.mainPage(guild).getFields().get(1).getValue().equals("```" + message.getContentDisplay() + "```")) {

            LiteSQL.onUpdate("UPDATE youtube SET message = '" + message.getContentDisplay() + "' WHERE guildid = " + guild.getIdLong());

            message.getReferencedMessage().editMessageEmbeds(new EmbedBuilder(YoutubeEmbeds.setChannel()).appendDescription("\n\n```✅ Der angegebene Wert wurde erfolgreich als Nachricht festgelegt.```").build()).setActionRows(ActionRow.of(YoutubeActionRows.goToMainPage(message.getAuthor()))).queue();

            message.delete().queue();

        } else {

            message.getReferencedMessage().editMessageEmbeds(new EmbedBuilder(YoutubeEmbeds.setMessage()).appendDescription("\n\n```❌ Der angegebene Wert ist schon als Nachricht festgelegt.```").build()).setActionRows(ActionRow.of(YoutubeActionRows.goToMainPage(message.getAuthor()))).queue();

            message.delete().queue();

        }

    }

    private void setChannel(Guild guild, TextChannel textChannel, Message message) {

        if (YoutubeEmbeds.mainPage(guild).getFields().get(0).getValue().equals("```Kein Kanal festgelegt.```") || !YoutubeEmbeds.mainPage(guild).getFields().get(0).getValue().equals(textChannel.getAsMention())) {

            LiteSQL.onUpdate("UPDATE youtube SET channel = " + textChannel.getIdLong() + " WHERE guildid = " + guild.getIdLong());

            message.getReferencedMessage().editMessageEmbeds(new EmbedBuilder(YoutubeEmbeds.setChannel()).appendDescription("\n\n```✅ '#" + textChannel.getName() + "' als Benachrichtigungs-Kanal festgelegt.```").build()).setActionRows(ActionRow.of(YoutubeActionRows.goToMainPage(message.getAuthor()))).queue();

            message.delete().queue();

        } else if (YoutubeEmbeds.mainPage(guild).getFields().get(0).getValue().equals(textChannel.getAsMention())) {

            message.getReferencedMessage().editMessageEmbeds(new EmbedBuilder(YoutubeEmbeds.setChannel()).appendDescription("\n\n```❌ '#" + textChannel.getName() + "' ist schon als Benachrichtigungs-Kanal festgelegt.```").build()).setActionRows(ActionRow.of(YoutubeActionRows.goToMainPage(message.getAuthor()))).queue();

            message.delete().queue();

        }

    }

}
