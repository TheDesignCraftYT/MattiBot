package de.thedesigncraft.mattibot.functions.joinroles;

import de.thedesigncraft.mattibot.MattiBot;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class JoinRolesMessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromGuild())
            return;

        // Add Channels
        if (event.getMessage().getMessageReference() != null && event.getMessage().getReferencedMessage() != null && event.getMessage().getReferencedMessage().getAuthor().equals(MattiBot.jda.getSelfUser()) && event.getMessage().getReferencedMessage().getEmbeds().get(0).getTitle().equals(JoinRolesEmbeds.addChannels().getTitle()) && event.getMessage().getReferencedMessage().getEmbeds().get(0).getDescription().startsWith(JoinRolesEmbeds.addChannels().getDescription())) {

            if (event.getMessage().getReferencedMessage().getActionRows().get(0).getButtons().get(0).getId().replace("joinroles.goToMainPage&id=", "").equals(event.getAuthor().getId())) {

                if (!event.getMessage().getMentions().getRoles().isEmpty()) {

                    try {

                        Role role = event.getMessage().getMentions().getRoles().get(0);

                        addRoles(event.getGuild(), role, event.getMessage());

                    } catch (ClassCastException e) {

                        event.getMessage().getReferencedMessage().editMessageEmbeds(new EmbedBuilder(JoinRolesEmbeds.addChannels()).appendDescription("\n\n```❌ Du musst eine Rolle angeben.```").build()).setActionRows(ActionRow.of(JoinRolesActionRows.goToMainPage(event.getAuthor()))).queue();

                        event.getMessage().delete().queue();

                    }

                } else if (!event.getMessage().getContentDisplay().equals("") && !event.getMessage().getContentDisplay().equals(" ")) {

                    try {

                        Role role = event.getGuild().getRoleById(event.getMessage().getContentDisplay().replace(" ", ""));

                        addRoles(event.getGuild(), role, event.getMessage());

                    } catch (NumberFormatException e) {

                        event.getMessage().getReferencedMessage().editMessageEmbeds(new EmbedBuilder(JoinRolesEmbeds.addChannels()).appendDescription("\n\n```❌ Du musst die Id einer Rolle angeben.```").build()).setActionRows(ActionRow.of(JoinRolesActionRows.goToMainPage(event.getAuthor()))).queue();

                        event.getMessage().delete().queue();

                    }

                }

            } else {

                event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Nur " + event.getGuild().getMemberById(event.getMessage().getReferencedMessage().getActionRows().get(0).getButtons().get(0).getId().replace("joinroles.goToMainPage&id=", "")).getAsMention() + " hat Zugriff auf diese Nachricht.", true)).queue(message -> {

                    event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                    message.delete().queueAfter(10, TimeUnit.SECONDS);

                });

            }

        }

        // Remove Channels
        if (event.getMessage().getMessageReference() != null && event.getMessage().getReferencedMessage() != null &&event.getMessage().getReferencedMessage().getAuthor().equals(MattiBot.jda.getSelfUser()) && event.getMessage().getReferencedMessage().getEmbeds().get(0).getTitle().equals(JoinRolesEmbeds.removeChannels().getTitle()) && event.getMessage().getReferencedMessage().getEmbeds().get(0).getDescription().startsWith(JoinRolesEmbeds.removeChannels().getDescription())) {

            if (event.getMessage().getReferencedMessage().getActionRows().get(0).getButtons().get(0).getId().replace("joinroles.goToMainPage&id=", "").equals(event.getAuthor().getId())) {

                if (!event.getMessage().getMentions().getRoles().isEmpty()) {

                    try {

                        Role role = event.getMessage().getMentions().getRoles().get(0);

                        removeRoles(event.getGuild(), role, event.getMessage());

                    } catch (ClassCastException e) {

                        event.getMessage().getReferencedMessage().editMessageEmbeds(new EmbedBuilder(JoinRolesEmbeds.removeChannels()).appendDescription("\n\n```❌ Du musst einen Textkanal angeben.```").build()).setActionRows(ActionRow.of(JoinRolesActionRows.goToMainPage(event.getAuthor()))).queue();

                        event.getMessage().delete().queue();

                    }

                } else if (!event.getMessage().getContentDisplay().equals("") && !event.getMessage().getContentDisplay().equals(" ")) {

                    try {

                        Role role = event.getGuild().getRoleById(event.getMessage().getContentDisplay().replace(" ", ""));

                        removeRoles(event.getGuild(), role, event.getMessage());

                    } catch (NumberFormatException | NullPointerException e) {

                        event.getMessage().getReferencedMessage().editMessageEmbeds(new EmbedBuilder(JoinRolesEmbeds.removeChannels()).appendDescription("\n\n```❌ Du musst die Id einer Rolle angeben.```").build()).setActionRows(ActionRow.of(JoinRolesActionRows.goToMainPage(event.getAuthor()))).queue();

                        event.getMessage().delete().queue();

                    }

                }

            } else {

                event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Nur " + event.getGuild().getMemberById(event.getMessage().getReferencedMessage().getActionRows().get(0).getButtons().get(0).getId().replace("joinroles.goToMainPage&id=", "")).getAsMention() + " hat Zugriff auf diese Nachricht.", true)).queue(message -> {

                    event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                    message.delete().queueAfter(10, TimeUnit.SECONDS);

                });

            }

        }

    }

    private void addRoles(Guild guild, Role role, Message message) {

        if (MainMethods.getJoinRoles(guild).equals(guild.getRoles()) || !MainMethods.getJoinRoles(guild).contains(role)) {

            if (guild.getMemberById(MattiBot.jda.getSelfUser().getIdLong()).getRoles().get(0).getPosition() > role.getPosition()) {

                StringBuilder stringBuilder = new StringBuilder();

                if (MainMethods.getJoinRoleDatas(guild) != null) {

                    MainMethods.getJoinRoleDatas(guild).forEach(role1 -> {

                        if (!role1.getId().equals(role.getId())) {

                            stringBuilder
                                    .append(role1.getIdLong())
                                    .append(",");

                        }

                    });

                }

                stringBuilder.append(role.getIdLong());

                LiteSQL.onUpdate("UPDATE joinroles SET roles = '" + stringBuilder + "' WHERE guildid = " + guild.getIdLong());

                message.getReferencedMessage().editMessageEmbeds(new EmbedBuilder(JoinRolesEmbeds.addChannels()).appendDescription("\n\n```✅ '@" + role.getName() + "' hinzugefügt.```").build()).setActionRows(ActionRow.of(JoinRolesActionRows.goToMainPage(message.getAuthor()))).queue();

                message.delete().queue();

            } else {

                message.getReferencedMessage().editMessageEmbeds(new EmbedBuilder(JoinRolesEmbeds.addChannels()).appendDescription("\n\n```❌ '@" + role.getName() + "' ist in der Rollen-Hierarchie höher gelistet als die höchste Rolle des Bots.```").build()).setActionRows(ActionRow.of(JoinRolesActionRows.goToMainPage(message.getAuthor()))).queue();

                message.delete().queue();

            }

        } else if (MainMethods.getJoinRoles(guild).contains(role)) {

            message.getReferencedMessage().editMessageEmbeds(new EmbedBuilder(JoinRolesEmbeds.addChannels()).appendDescription("\n\n```❌ '@" + role.getName() + "' ist schon eine Beitritts Rolle.```").build()).setActionRows(ActionRow.of(JoinRolesActionRows.goToMainPage(message.getAuthor()))).queue();

            message.delete().queue();

        }

    }

    private void removeRoles(Guild guild, Role role, Message message) {

        if (MainMethods.getJoinRoleDatas(guild) != null && MainMethods.getJoinRoleDatas(guild).contains(role)) {

            StringBuilder stringBuilder = new StringBuilder();

            if (MainMethods.getJoinRoleDatas(guild) != null) {

                MainMethods.getJoinRoleDatas(guild).forEach(role1 -> {

                    if (!role1.getId().equals(role.getId())) {

                        stringBuilder
                                .append(role1.getIdLong())
                                .append(",");

                    }

                });

            }

            LiteSQL.onUpdate("UPDATE joinroles SET roles = '" + stringBuilder + "' WHERE guildid = " + guild.getIdLong());

            message.getReferencedMessage().editMessageEmbeds(new EmbedBuilder(JoinRolesEmbeds.removeChannels()).appendDescription("\n\n```✅ '@" + role.getName() + "' entfernt.```").build()).setActionRows(ActionRow.of(JoinRolesActionRows.goToMainPage(message.getAuthor()))).queue();

            message.delete().queue();

        } else if (!MainMethods.getJoinRoleDatas(guild).contains(role)) {

            message.getReferencedMessage().editMessageEmbeds(new EmbedBuilder(JoinRolesEmbeds.removeChannels()).appendDescription("\n\n```❌ '@" + role.getName() + "' ist keine Beitritts Rolle.```").build()).setActionRows(ActionRow.of(JoinRolesActionRows.goToMainPage(message.getAuthor()))).queue();

            message.delete().queue();

        }

    }

}
