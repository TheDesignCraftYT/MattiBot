package de.thedesigncraft.mattibot.commands.categories.moderation;

import de.thedesigncraft.mattibot.MattiBot;
import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import de.thedesigncraft.mattibot.constants.values.commands.Categories;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import de.thedesigncraft.mattibot.manage.ServerCommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class UnbanServerCommand implements ServerCommand {
    @Override
    public String version() {
        return Versions.versions().get("v100a4");
    }

    @Override
    public String description() {
        return "Entbannt einen bestimmten Nutzer.";
    }

    @Override
    public String category() {
        return Categories.categories().get("moderation");
    }

    @Override
    public List<OptionData> options() {

        List<OptionData> returnList = new ArrayList<>();

        returnList.add(new OptionData(OptionType.STRING, "user", "Der zu entbannende Nutzer."));
        returnList.add(new OptionData(OptionType.STRING, "reason", "Der Grund für den Unban."));

        return returnList;
    }

    @Override
    public boolean slashCommand() {
        return true;
    }

    @Override
    public Emoji commandEmoji() {
        return Emoji.fromUnicode("U+1F4AB");
    }

    @Override
    public List<Permission> requiredPermissions() {

        List<Permission> returnList = new ArrayList<>();

        returnList.add(Permission.BAN_MEMBERS);

        return returnList;
    }

    @Override
    public void performGuildMessageCommand(MessageReceivedEvent event) {

        if (event.getMessage().getMentions().getUsers().get(0) != null) {

            User user = event.getMessage().getMentions().getUsers().get(0);

            String[] args = event.getMessage().getContentDisplay().replace("unban", "").split(" ");

            if (args.length >= 2) {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Unban", "Möchtest du diesen Nutzer wirklich entbannen?")).addField("Grund:", args[1], false).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "unban.success&user=" + user.getIdLong() + "&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getAuthor()))).mentionRepliedUser(false).queue();

            } else {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Unban", "Möchtest du diesen Nutzer wirklich entbannen?")).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "unban.success&user=" + user.getIdLong() + "&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getAuthor()))).mentionRepliedUser(false).queue();

            }

        } else if (event.getMessage().getMentions().getRoles().get(0) != null || event.getMessage().getMentions().getChannels().get(0) != null) {

            event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Du musst einen User angeben.", true)).mentionRepliedUser(false).queue(message -> {

                event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                message.delete().queueAfter(10, TimeUnit.SECONDS);

            });

        } else if (event.getGuild().getMemberById(event.getMessage().getContentDisplay().replace("unban", "").split(" ")[1]) != null) {

            String[] args = event.getMessage().getContentDisplay().replace("ban", "").split(" ");

            if (args.length >= 2) {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Unban", "Möchtest du diesen Nutzer wirklich entbannen?")).addField("Grund:", args[1], false).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "unban.success&user=" + event.getMessage().getContentDisplay().replace("unban", "").split(" ")[1] + "&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getAuthor()))).mentionRepliedUser(false).queue();

            } else {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Unban", "Möchtest du diesen Nutzer wirklich entbannen?")).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "unban.success&user=" + event.getMessage().getContentDisplay().replace("unban", "").split(" ")[1] + "&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getAuthor()))).mentionRepliedUser(false).queue();

            }

        } else {

            event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Du musst die Id eines Users angeben.", true)).mentionRepliedUser(false).queue(message -> {

                event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                message.delete().queueAfter(10, TimeUnit.SECONDS);

            });

        }

    }

    @Override
    public void performSlashCommand(SlashCommandInteractionEvent event) {

        try {

            if (event.getOption("reason") != null) {

                event.replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Unban", "Möchtest du diesen Nutzer wirklich entbannen?")).addField("Grund:", event.getOption("reason").getAsString(), false).build()).addActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "unban.success&user=" + event.getOption("user").getAsString() + "&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getUser()))).queue();

            } else {

                event.replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Unban", "Möchtest du diesen Nutzer wirklich entbannen?")).build()).addActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "unban.success&user=" + event.getOption("user").getAsString() + "&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getUser()))).queue();

            }

        } catch (NullPointerException e) {

            event.replyEmbeds(EmbedTemplates.issueEmbed("Du musst einen Nutzer angeben.", false)).setEphemeral(true).queue();

        }

    }

    public static MessageEmbed performCommand(String memberString, Guild guild, String reason, Member staff) {

        List<String> memberStringType = new ArrayList<>();

        try {
            Long.parseLong(memberString.split("#")[1]);

            memberStringType.add("tag");

        } catch (NumberFormatException | IndexOutOfBoundsException ignored) {
        }

        try {

            if (memberStringType.isEmpty()) {

                MattiBot.jda.retrieveUserById(memberString).queue();

                memberStringType.add("id");

            }

        } catch (NumberFormatException e) {

            return EmbedTemplates.issueEmbed("Das ist kein User.", false);

        }

        List<User> userList = new ArrayList<>();

        if (memberStringType.get(0).equals("tag")) {

            guild.retrieveBanList().queue(bans -> {

                bans.forEach(ban -> {

                    if (ban.getUser().getAsTag().equals(memberString)) {

                        userList.add(ban.getUser());

                    }

                });

            });

        } else if (memberStringType.get(0).equals("id")) {

            guild.retrieveBanList().queue(bans -> {

                bans.forEach(ban -> {

                    if (ban.getUser().getId().equals(memberString)) {

                        userList.add(ban.getUser());

                    }

                });

            });

        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignored) {
        }

        User user = userList.get(0);

        String punishment = "<(unban)(" + Date.from(Instant.now()).getTime() / 1000L + ")(" + reason + ")>";

        MainMethods.addPunishment(punishment, user, guild);

        guild.unban(user).queue();

        user.openPrivateChannel().queue(privateChannel -> {

            List<Message> messages = privateChannel.getIterableHistory()
                    .takeAsync(50)
                    .thenApply(list -> list.stream().filter(message ->
                            message.getAuthor().equals(MattiBot.jda.getSelfUser()) &&
                                    !message.getEmbeds().isEmpty() &&
                                    message.getEmbeds().get(0).getTitle().endsWith("Ban") &&
                                    !message.getEmbeds().get(0).getDescription().endsWith("```")
                    ).collect(Collectors.toList())).join();

            Message message = messages.get(0);

            message.editMessageEmbeds(new EmbedBuilder(message.getEmbeds().get(0)).appendDescription("\n\n```Du wurdest entbannt von '" + staff.getUser().getAsTag() + "'.```").build()).queue();

        });

        ServerCommand unbanCommand = ServerCommandManager.slashCommandsMap.get("unban");

        EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed(unbanCommand.commandEmoji().getName() + " Ban", ""));

        embedBuilder.addField("Entbannt:", "```" + user.getAsTag() + "```", true);

        if (!reason.equals(" ") && !reason.equals(""))
            embedBuilder.addField("Grund:", "```" + reason + "```", false);

        return embedBuilder.build();

    }

}
