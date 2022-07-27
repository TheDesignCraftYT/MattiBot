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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TempBanServerCommand implements ServerCommand {
    @Override
    public String version() {
        return Versions.versions().get("v100a1");
    }

    @Override
    public String description() {
        return "Bannt einen bestimmten Nutzer für eine bestimmte Zeit vom Server.";
    }

    @Override
    public String category() {
        return Categories.categories().get("moderation");
    }

    @Override
    public List<OptionData> options() {

        List<OptionData> returnList = new ArrayList<>();

        returnList.add(new OptionData(OptionType.USER, "user", "Der zu bannende Nutzer."));
        returnList.add(new OptionData(OptionType.STRING, "time", "Die Länge des Bans. (Erlaubte Zeitangaben: d, h, m, s)"));
        returnList.add(new OptionData(OptionType.STRING, "reason", "Der Grund für den Ban."));

        return returnList;
    }

    @Override
    public boolean slashCommand() {
        return true;
    }

    @Override
    public Emoji commandEmoji() {
        return Emoji.fromUnicode("U+1F6AB");
    }

    @Override
    public List<Permission> requiredPermissions() {

        List<Permission> returnList = new ArrayList<>();

        returnList.add(Permission.BAN_MEMBERS);

        return returnList;
    }

    @Override
    public void performGuildMessageCommand(MessageReceivedEvent event) {

        if (event.getMessage().getMentions().getMembers().get(0) != null) {

            Member member = event.getMessage().getMentions().getMembers().get(0);

            String[] args = event.getMessage().getContentDisplay().replace("tempban", "").split(" ");

            List<String> timeStrings = new ArrayList<>(Arrays.stream(args).toList());

            List<String> finalTimeStrings = new ArrayList<>();

            timeStrings.forEach(s -> {

                if (s.endsWith("d") || s.endsWith("h") || s.endsWith("m") || s.endsWith("s"))
                    finalTimeStrings.add(s);

            });

            StringBuilder stringBuilder = new StringBuilder();

            finalTimeStrings.forEach(s -> stringBuilder.append(s).append(" "));

            if (args.length >= 3) {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " TempBan", "Möchtest du **" + member.getUser().getAsMention() + "** wirklich bannen?")).addField("Grund:", "```" + args[args.length - 1] + "```", false).addField("Entbannung:", "```" + stringBuilder + "```", false).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "tempban.success&id=" + member.getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(member.getUser()))).mentionRepliedUser(false).queue();

            } else if (args.length >= 2) {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " TempBan", "Möchtest du **" + member.getUser().getAsMention() + "** wirklich bannen?")).addField("Entbannung:", "```" + stringBuilder + "```", false).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "tempban.success&id=" + member.getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(member.getUser()))).mentionRepliedUser(false).queue();

            } else {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " TempBan", "Möchtest du **" + member.getUser().getAsMention() + "** wirklich bannen?")).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "tempban.success&id=" + member.getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(member.getUser()))).mentionRepliedUser(false).queue();

            }

        } else if (event.getMessage().getMentions().getRoles().get(0) != null || event.getMessage().getMentions().getChannels().get(0) != null) {

            event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Du musst einen User angeben.", true)).mentionRepliedUser(false).queue(message -> {

                event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                message.delete().queueAfter(10, TimeUnit.SECONDS);

            });

        } else if (event.getGuild().getMemberById(event.getMessage().getContentDisplay().replace("tempban", "").split(" ")[1]) != null) {

            Member member = event.getGuild().getMemberById(event.getMessage().getContentDisplay().replace("tempban", "").split(" ")[1]);

            String[] args = event.getMessage().getContentDisplay().replace("tempban", "").split(" ");

            List<String> timeStrings = new ArrayList<>(Arrays.stream(args).toList());

            List<String> finalTimeStrings = new ArrayList<>();

            timeStrings.forEach(s -> {

                if (s.endsWith("d") || s.endsWith("h") || s.endsWith("m") || s.endsWith("s"))
                    finalTimeStrings.add(s);

            });

            StringBuilder stringBuilder = new StringBuilder();

            finalTimeStrings.forEach(s -> stringBuilder.append(s).append(" "));

            if (args.length >= 3) {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " TempBan", "Möchtest du **" + member.getUser().getAsMention() + "** wirklich bannen?")).addField("Grund:", "```" + args[args.length - 1] + "```", false).addField("Entbannung:", "```" + stringBuilder + "```", false).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "tempban.success&id=" + member.getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(member.getUser()))).mentionRepliedUser(false).queue();

            } else if (args.length >= 2) {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " TempBan", "Möchtest du **" + member.getUser().getAsMention() + "** wirklich bannen?")).addField("Entbannung:", "```" + stringBuilder + "```", false).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "tempban.success&id=" + member.getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(member.getUser()))).mentionRepliedUser(false).queue();

            } else {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " TempBan", "Möchtest du **" + member.getUser().getAsMention() + "** wirklich bannen?")).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "tempban.success&id=" + member.getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(member.getUser()))).mentionRepliedUser(false).queue();

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

            if (event.getOption("time") != null && event.getOption("reason") != null) {

                event.replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " TempBan", "Möchtest du **" + event.getOption("user").getAsUser().getAsMention() + "** wirklich bannen?")).addField("Grund:", "```" + event.getOption("reason").getAsString() + "```", false).addField("Entbannung:", "```" + event.getOption("time").getAsString() + "```", false).build()).addActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "tempban.success&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getUser()))).queue();

            } else if (event.getOption("time") != null) {

                event.replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " TempBan", "Möchtest du **" + event.getOption("user").getAsUser().getAsMention() + "** wirklich bannen?")).addField("Entbannung:", "```" + event.getOption("time").getAsString() + "```", false).build()).addActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "tempban.success&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getUser()))).queue();

            } else {

                event.replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " TempBan", "Möchtest du **" + event.getOption("user").getAsUser().getAsMention() + "** wirklich bannen?")).build()).addActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "tempban.success&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getUser()))).queue();

            }

        } catch (NullPointerException e) {

            event.replyEmbeds(EmbedTemplates.issueEmbed("Du musst einen Nutzer angeben.", false)).setEphemeral(true).queue();

        }

    }

    public static MessageEmbed performCommand(Member member, String reason, Member staff, String time) {

        List<String> splitted = Arrays.stream(time.split(" ")).toList();

        List<Integer> timeList = new ArrayList<>();

        try {

            splitted.forEach(s -> {

                if (s.endsWith("d")) {

                    int days = Integer.parseInt(s.replace("d", "")) * 24 * 60 * 60;

                    try {
                        timeList.set(0, timeList.get(0) + days);
                    } catch (IndexOutOfBoundsException e) {
                        timeList.add(days);
                    }


                } else if (s.endsWith("h")) {

                    int hours = Integer.parseInt(s.replace("h", "")) * 60 * 60;

                    try {
                        timeList.set(0, timeList.get(0) + hours);
                    } catch (IndexOutOfBoundsException e) {
                        timeList.add(hours);
                    }

                } else if (s.endsWith("m")) {

                    int minutes = Integer.parseInt(s.replace("m", "")) * 60;

                    try {
                        timeList.set(0, timeList.get(0) + minutes);
                    } catch (IndexOutOfBoundsException e) {
                        timeList.add(minutes);
                    }

                } else if (s.endsWith("s")) {

                    int seconds = Integer.parseInt(s.replace("s", ""));

                    try {
                        timeList.set(0, timeList.get(0) + seconds);
                    } catch (IndexOutOfBoundsException e) {
                        timeList.add(seconds);
                    }

                }

            });

            long endTimestamp = Date.from(Instant.now().plusSeconds(timeList.get(0))).getTime() / 1000L;

            String punishment = "<(tempban)(" + Date.from(Instant.now()).getTime() / 1000L + ")(" + endTimestamp + ")(" + reason + ")>";

            if (staff.canInteract(member) && !member.isOwner() && !member.equals(member.getGuild().getMemberById(MattiBot.jda.getSelfUser().getIdLong()))) {

                MainMethods.addPunishment(punishment, member.getUser(), member.getGuild());

                ServerCommand tempbanCommand = ServerCommandManager.slashCommandsMap.get("tempban");

                EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed(tempbanCommand.commandEmoji().getName() + " TempBan", "Du wurdest gebannt."));

                embedBuilder.addField("Server:", "```" + member.getGuild().getName() + "```", true);

                embedBuilder.addField("Gebannt von:", "```" + staff.getUser().getAsTag() + "```", true);

                if (!reason.equals(" ") && !reason.equals(""))
                    embedBuilder.addField("Grund:", "```" + reason + "```", false);

                embedBuilder.addField("Entbannung:", "<t:" + endTimestamp + ":R>", false);

                try {

                    member.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(embedBuilder.build()).queue());

                } catch (UnsupportedOperationException | ErrorResponseException ignored) {

                }

                try {

                    member.ban(0, reason).queueAfter(1, TimeUnit.SECONDS);

                } catch (HierarchyException e) {

                    return EmbedTemplates.issueEmbed("Der Bot kann keinen Nutzer bannen, der eine höhere Rolle als er selbst hat.", false);

                }

                EmbedBuilder embedBuilder1 = new EmbedBuilder(EmbedTemplates.standardEmbed(tempbanCommand.commandEmoji().getName() + " TempBan", ""));

                embedBuilder1.addField("Gebannt:", "```" + member.getUser().getAsTag() + "```", false);

                if (!reason.equals(" ") && !reason.equals(""))
                    embedBuilder1.addField("Grund:", "```" + reason + "```", false);

                embedBuilder1.addField("Entbannung:", "<t:" + endTimestamp + ":R>", false);

                return embedBuilder1.build();

            } else if (member.isOwner()) {

                return EmbedTemplates.issueEmbed("Du kannst den Owner eines Servers nicht bannen.", false);

            } else if (member.equals(member.getGuild().getMemberById(MattiBot.jda.getSelfUser().getIdLong()))) {

                return EmbedTemplates.issueEmbed("Der Bot kann sich nicht selber bannen.", false);

            } else {

                return EmbedTemplates.issueEmbed("Du kannst diesen Nutzer nicht bannen.", false);

            }

        } catch (NumberFormatException e) {

            return EmbedTemplates.issueEmbed("Du musst eine richtige Zeit angeben.\n\nHier ein Beispiel:\n```3d 5h 8m 30s```", false);

        }

    }

    public static void checkTempBans() {

        MattiBot.jda.getGuilds().forEach(guild -> guild.retrieveBanList().queue(bans -> {

            List<User> bannedUsers = new ArrayList<>();

            bans.forEach(ban -> bannedUsers.add(ban.getUser()));

            bannedUsers.forEach(user -> {

                List<String[]> tempbans = MainMethods.getPunishments(user, guild, "tempban");
                List<String[]> userBans = MainMethods.getPunishments(user, guild, "ban");

                List<Long> endTimestamps = new ArrayList<>();

                assert tempbans != null;
                tempbans.forEach(s -> {

                    long endTimestamp = Long.parseLong(s[2]);

                    long latestBanTimestamp = Long.parseLong(userBans.get(userBans.size()-1)[1]);

                    if (new Date(latestBanTimestamp * 1000L).before(new Date(endTimestamp * 1000L))) {

                        endTimestamps.add(endTimestamp);

                    }

                });

                List<Boolean> ifTime = new ArrayList<>();

                if (endTimestamps.isEmpty()) {

                    return;

                }


                endTimestamps.forEach(aLong -> {

                    if (Date.from(Instant.now()).before(new Date(aLong * 1000L))) {

                        ifTime.add(false);

                    }

                });

                if (ifTime.isEmpty()) {

                    guild.unban(user).queue();

                    user.openPrivateChannel().queue(privateChannel ->
                            privateChannel.getIterableHistory()
                                    .takeAsync(50)
                                    .thenApply(list1 -> list1.stream().filter(message ->
                                            message.getAuthor().equals(MattiBot.jda.getSelfUser()) &&
                                                    !message.getEmbeds().isEmpty() &&
                                                    message.getEmbeds().get(0).getTitle().endsWith("TempBan") &&
                                                    !message.getEmbeds().get(0).getDescription().endsWith("```")
                                    ).collect(Collectors.toList())).join().forEach(message ->

                                            message.getEmbeds().get(0).getFields().forEach(field -> {

                                                if (field.getName().equals("Entbannung:")) {

                                                    if (endTimestamps.contains(Long.parseLong(field.getValue().replace("<t:", "").replace(":R>", "")))) {

                                                        message.editMessageEmbeds(new EmbedBuilder(message.getEmbeds().get(0)).appendDescription("\n\n```✅ Du wurdest automatisch wieder entbannt.```").build()).queue();

                                                    }

                                                }

                                            })
                                    )
                    );

                }

            });

        }));

    }

}
