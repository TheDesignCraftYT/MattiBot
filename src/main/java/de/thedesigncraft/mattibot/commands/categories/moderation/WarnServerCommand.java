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

public class WarnServerCommand implements ServerCommand {
    @Override
    public String version() {
        return Versions.versions().get("v100a1");
    }

    @Override
    public String description() {
        return "Warnt einen bestimmten Nutzer.";
    }

    @Override
    public String category() {
        return Categories.categories().get("moderation");
    }

    @Override
    public List<OptionData> options() {

        List<OptionData> returnList = new ArrayList<>();

        returnList.add(new OptionData(OptionType.USER, "user", "Der zu warnende Nutzer."));
        returnList.add(new OptionData(OptionType.STRING, "reason", "Der Grund für den Warn."));

        return returnList;
    }

    @Override
    public boolean slashCommand() {
        return true;
    }

    @Override
    public Emoji commandEmoji() {
        return Emoji.fromUnicode("U+26A0");
    }

    @Override
    public List<Permission> requiredPermissions() {

        List<Permission> returnList = new ArrayList<>();

        returnList.add(Permission.KICK_MEMBERS);

        return returnList;
    }

    @Override
    public void performGuildMessageCommand(MessageReceivedEvent event) {

        if (event.getMessage().getMentions().getMembers().get(0) != null) {

            Member member = event.getMessage().getMentions().getMembers().get(0);

            String[] args = event.getMessage().getContentDisplay().replace("warn", "").split(" ");

            if (args.length >= 2) {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Verwarnung", "Möchtest du **" + member.getUser().getAsMention() + "** wirklich verwarnen?")).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "warn.success&reason=" + args[1] + "&id=" + member.getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(member.getUser()))).mentionRepliedUser(false).queue();

            } else {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Verwarnung", "Möchtest du **" + member.getUser().getAsMention() + "** wirklich verwarnen?")).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "warn.success&reason=&id=" + member.getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(member.getUser()))).mentionRepliedUser(false).queue();

            }

        } else if (event.getMessage().getMentions().getRoles().get(0) != null || event.getMessage().getMentions().getChannels().get(0) != null) {

            event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Du musst einen User angeben.", true)).mentionRepliedUser(false).queue(message -> {

                event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                message.delete().queueAfter(10, TimeUnit.SECONDS);

            });

        } else if (event.getGuild().getMemberById(event.getMessage().getContentDisplay().replace("warn", "").split(" ")[1]) != null) {

            Member member = event.getGuild().getMemberById(event.getMessage().getContentDisplay().replace("warn", "").split(" ")[1]);

            String[] args = event.getMessage().getContentDisplay().replace("warn", "").split(" ");

            if (args.length >= 2) {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Verwarnung", "Möchtest du **" + member.getUser().getAsMention() + "** wirklich verwarnen?")).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "warn.success&reason=" + args[1] + "&id=" + member.getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(member.getUser()))).mentionRepliedUser(false).queue();

            } else {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Verwarnung", "Möchtest du **" + member.getUser().getAsMention() + "** wirklich verwarnen?")).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "warn.success&reason=&id=" + member.getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(member.getUser()))).mentionRepliedUser(false).queue();

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

                event.replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Verwarnung", "Möchtest du **" + event.getOption("user").getAsUser().getAsMention() + "** wirklich verwarnen?")).build()).addActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "warn.success&reason=" + event.getOption("reason").getAsString() + "&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getUser()))).queue();

            } else {

                event.replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Verwarnung", "Möchtest du **" + event.getOption("user").getAsUser().getAsMention() + "** wirklich verwarnen?")).build()).addActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "warn.success&reason=&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getUser()))).queue();

            }

        } catch (NullPointerException e) {

            event.replyEmbeds(EmbedTemplates.issueEmbed("Du musst einen Nutzer angeben.", false)).setEphemeral(true).queue();

        }

    }

    public static MessageEmbed performCommand(Member member, String reason, Member staff) {

        String punishment = "<(warn)(" + Date.from(Instant.now()).getTime() / 1000L + ")(" + reason + ")>";

        if (staff.canInteract(member) && !member.isOwner() && !member.equals(member.getGuild().getMemberById(MattiBot.jda.getSelfUser().getIdLong()))) {

            MainMethods.addPunishment(punishment, member);

            ServerCommand warnCommand = ServerCommandManager.commandsMap.get("warn");

            EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed(warnCommand.commandEmoji().getName() + " Verwarnung", "Du wurdest verwarnt."));

            embedBuilder.addField("Server:", "```" + member.getGuild().getName() + "```", true);

            embedBuilder.addField("Verwarnt von:", "```" + staff.getUser().getAsTag() + "```", true);

            if (!reason.equals(" ") && !reason.equals(""))
                embedBuilder.addField("Grund:", "```" + reason + "```", false);

            try {

                member.getUser().openPrivateChannel().queue(privateChannel -> {

                    privateChannel.sendMessageEmbeds(embedBuilder.build()).queue();

                });

            } catch (UnsupportedOperationException ignored) {

            }

            EmbedBuilder embedBuilder1 = new EmbedBuilder(EmbedTemplates.standardEmbed(warnCommand.commandEmoji().getName() + " Verwarnung", ""));

            embedBuilder1.addField("Verwarnt:", "```" + member.getUser().getAsTag() + "```", true);

            if (!reason.equals(" ") && !reason.equals(""))
                embedBuilder1.addField("Grund:", "```" + reason + "```", false);

            return embedBuilder1.build();

        } else if (member.isOwner()) {

            return EmbedTemplates.issueEmbed("Du kannst der Owner eines Servers nicht warnen.", false);

        } else if (member.equals(member.getGuild().getMemberById(MattiBot.jda.getSelfUser().getIdLong()))) {

            return EmbedTemplates.issueEmbed("Der Bot kann sich nicht selber warnen.", false);

        } else {

            return EmbedTemplates.issueEmbed("Du kannst diese Person nicht warnen.", false);

        }

    }

}
