package de.thedesigncraft.mattibot.functions.whitelist;

import de.thedesigncraft.mattibot.MattiBot;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
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

public class WhitelistMessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromGuild())
            return;

        // Add Channels
        if (event.getMessage().getMessageReference() != null && event.getMessage().getReferencedMessage() != null && event.getMessage().getReferencedMessage().getAuthor().equals(MattiBot.jda.getSelfUser()) && event.getMessage().getReferencedMessage().getEmbeds().get(0).getTitle().equals(WhitelistEmbeds.addChannels().getTitle()) && event.getMessage().getReferencedMessage().getEmbeds().get(0).getDescription().startsWith(WhitelistEmbeds.addChannels().getDescription())) {

            if (event.getMessage().getReferencedMessage().getActionRows().get(0).getButtons().get(0).getId().replace("whitelist.goToMainPage&id=", "").equals(event.getAuthor().getId())) {

                if (!event.getMessage().getMentions().getChannels().isEmpty()) {

                    try {

                        TextChannel textChannel = (TextChannel) event.getMessage().getMentions().getChannels().get(0);

                        addChannels(event.getGuild(), textChannel, event.getMessage());

                    } catch (ClassCastException e) {

                        event.getMessage().getReferencedMessage().editMessageEmbeds(new EmbedBuilder(WhitelistEmbeds.addChannels()).appendDescription("\n\n```❌ Du musst einen Textkanal angeben.```").build()).setActionRows(ActionRow.of(WhitelistActionRows.goToMainPage(event.getAuthor()))).queue();

                        event.getMessage().delete().queue();

                    }

                } else if (!event.getMessage().getContentDisplay().equals("") && !event.getMessage().getContentDisplay().equals(" ")) {

                    try {

                        TextChannel textChannel = event.getGuild().getTextChannelById(event.getMessage().getContentDisplay().replace(" ", ""));

                        addChannels(event.getGuild(), textChannel, event.getMessage());

                    } catch (NumberFormatException e) {

                        event.getMessage().getReferencedMessage().editMessageEmbeds(new EmbedBuilder(WhitelistEmbeds.addChannels()).appendDescription("\n\n```❌ Du musst die Id eines Textkanals angeben.```").build()).setActionRows(ActionRow.of(WhitelistActionRows.goToMainPage(event.getAuthor()))).queue();

                        event.getMessage().delete().queue();

                    }

                }

            } else {

                event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Nur " + event.getGuild().getMemberById(event.getMessage().getReferencedMessage().getActionRows().get(0).getButtons().get(0).getId().replace("whitelist.goToMainPage&id=", "")).getAsMention() + " hat Zugriff auf diese Nachricht.", true)).queue(message -> {

                    event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                    message.delete().queueAfter(10, TimeUnit.SECONDS);

                });

            }

        }

        // Remove Channels
        if (event.getMessage().getMessageReference() != null && event.getMessage().getReferencedMessage().getAuthor().equals(MattiBot.jda.getSelfUser()) && event.getMessage().getReferencedMessage().getEmbeds().get(0).getTitle().equals(WhitelistEmbeds.removeChannels().getTitle()) && event.getMessage().getReferencedMessage().getEmbeds().get(0).getDescription().startsWith(WhitelistEmbeds.removeChannels().getDescription())) {

            if (event.getMessage().getReferencedMessage().getActionRows().get(0).getButtons().get(0).getId().replace("whitelist.goToMainPage&id=", "").equals(event.getAuthor().getId())) {

                if (!event.getMessage().getMentions().getChannels().isEmpty()) {

                    try {

                        TextChannel textChannel = (TextChannel) event.getMessage().getMentions().getChannels().get(0);

                        removeChannels(event.getGuild(), textChannel, event.getMessage());

                    } catch (ClassCastException e) {

                        event.getMessage().getReferencedMessage().editMessageEmbeds(new EmbedBuilder(WhitelistEmbeds.removeChannels()).appendDescription("\n\n```❌ Du musst einen Textkanal angeben.```").build()).setActionRows(ActionRow.of(WhitelistActionRows.goToMainPage(event.getAuthor()))).queue();

                        event.getMessage().delete().queue();

                    }

                } else if (!event.getMessage().getContentDisplay().equals("") && !event.getMessage().getContentDisplay().equals(" ")) {

                    try {

                        TextChannel textChannel = event.getGuild().getTextChannelById(event.getMessage().getContentDisplay().replace(" ", ""));

                        removeChannels(event.getGuild(), textChannel, event.getMessage());

                    } catch (NumberFormatException e) {

                        event.getMessage().getReferencedMessage().editMessageEmbeds(new EmbedBuilder(WhitelistEmbeds.removeChannels()).appendDescription("\n\n```❌ Du musst die Id eines Textkanals angeben.```").build()).setActionRows(ActionRow.of(WhitelistActionRows.goToMainPage(event.getAuthor()))).queue();

                        event.getMessage().delete().queue();

                    }

                }

            } else {

                event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Nur " + event.getGuild().getMemberById(event.getMessage().getReferencedMessage().getActionRows().get(0).getButtons().get(0).getId().replace("whitelist.goToMainPage&id=", "")).getAsMention() + " hat Zugriff auf diese Nachricht.", true)).queue(message -> {

                    event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                    message.delete().queueAfter(10, TimeUnit.SECONDS);

                });

            }

        }

    }

    private void addChannels(Guild guild, TextChannel textChannel, Message message) {

        if (MainMethods.getWhitelistedChannels(guild).equals(guild.getTextChannels()) || !MainMethods.getWhitelistedChannels(guild).contains(textChannel)) {

            StringBuilder stringBuilder = new StringBuilder();

            if (MainMethods.getWhitelistedChannelDatas(guild) != null) {

                MainMethods.getWhitelistedChannelDatas(guild).forEach(textChannel1 -> {

                    if (!textChannel1.getId().equals(textChannel.getId())) {

                        stringBuilder
                                .append(textChannel1.getIdLong())
                                .append(",");

                    }

                });

            }

            stringBuilder.append(textChannel.getIdLong());

            LiteSQL.onUpdate("UPDATE whitelistedChannels SET channels = '" + stringBuilder + "' WHERE guildid = " + guild.getIdLong());

            message.getReferencedMessage().editMessageEmbeds(new EmbedBuilder(WhitelistEmbeds.addChannels()).appendDescription("\n\n```✅ '#" + textChannel.getName() + "' hinzugefügt.```").build()).setActionRows(ActionRow.of(WhitelistActionRows.goToMainPage(message.getAuthor()))).queue();

            message.delete().queue();

        } else if (MainMethods.getWhitelistedChannels(guild).contains(textChannel)) {

            message.getReferencedMessage().editMessageEmbeds(new EmbedBuilder(WhitelistEmbeds.addChannels()).appendDescription("\n\n```❌ '#" + textChannel.getName() + "' ist schon gewhitelistet.```").build()).setActionRows(ActionRow.of(WhitelistActionRows.goToMainPage(message.getAuthor()))).queue();

            message.delete().queue();

        }

    }

    private void removeChannels(Guild guild, TextChannel textChannel, Message message) {

        if (MainMethods.getWhitelistedChannelDatas(guild) != null && MainMethods.getWhitelistedChannelDatas(guild).contains(textChannel)) {

            StringBuilder stringBuilder = new StringBuilder();

            if (MainMethods.getWhitelistedChannelDatas(guild) != null) {

                MainMethods.getWhitelistedChannelDatas(guild).forEach(textChannel1 -> {

                    if (!textChannel1.getId().equals(textChannel.getId())) {

                        stringBuilder
                                .append(textChannel1.getIdLong())
                                .append(",");

                    }

                });

            }

            LiteSQL.onUpdate("UPDATE whitelistedChannels SET channels = '" + stringBuilder + "' WHERE guildid = " + guild.getIdLong());

            message.getReferencedMessage().editMessageEmbeds(new EmbedBuilder(WhitelistEmbeds.removeChannels()).appendDescription("\n\n```✅ '#" + textChannel.getName() + "' entfernt.```").build()).setActionRows(ActionRow.of(WhitelistActionRows.goToMainPage(message.getAuthor()))).queue();

            message.delete().queue();

        } else if (!MainMethods.getWhitelistedChannelDatas(guild).contains(textChannel)) {

            message.getReferencedMessage().editMessageEmbeds(new EmbedBuilder(WhitelistEmbeds.removeChannels()).appendDescription("\n\n```❌ '#" + textChannel.getName() + "' ist nicht gewhitelistet.```").build()).setActionRows(ActionRow.of(WhitelistActionRows.goToMainPage(message.getAuthor()))).queue();

            message.delete().queue();

        }

    }

}
