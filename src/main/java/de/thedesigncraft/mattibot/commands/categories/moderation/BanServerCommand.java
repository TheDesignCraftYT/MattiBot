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
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
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

public class BanServerCommand implements ServerCommand {
    @Override
    public String version() {
        return Versions.versions().get("v100a1");
    }

    @Override
    public String description() {
        return "Bannt einen bestimmten Nutzer vom Server.";
    }

    @Override
    public String category() {
        return Categories.categories().get("moderation");
    }

    @Override
    public List<OptionData> options() {

        List<OptionData> returnList = new ArrayList<>();

        returnList.add(new OptionData(OptionType.USER, "user", "Der zu bannende Nutzer."));
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

            String[] args = event.getMessage().getContentDisplay().replace("ban", "").split(" ");

            if (args.length >= 2) {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Ban", "Möchtest du **" + member.getUser().getAsMention() + "** wirklich bannen?")).addField("Grund:", args[1], false).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "ban.success&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getAuthor()))).mentionRepliedUser(false).queue();

            } else {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Ban", "Möchtest du **" + member.getUser().getAsMention() + "** wirklich bannen?")).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "ban.success&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getAuthor()))).mentionRepliedUser(false).queue();

            }

        } else if (event.getMessage().getMentions().getRoles().get(0) != null || event.getMessage().getMentions().getChannels().get(0) != null) {

            event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Du musst einen User angeben.", true)).mentionRepliedUser(false).queue(message -> {

                event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                message.delete().queueAfter(10, TimeUnit.SECONDS);

            });

        } else if (event.getGuild().getMemberById(event.getMessage().getContentDisplay().replace("ban", "").split(" ")[1]) != null) {

            Member member = event.getGuild().getMemberById(event.getMessage().getContentDisplay().replace("ban", "").split(" ")[1]);

            String[] args = event.getMessage().getContentDisplay().replace("ban", "").split(" ");

            if (args.length >= 2) {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Ban", "Möchtest du **" + member.getUser().getAsMention() + "** wirklich bannen?")).addField("Grund:", args[1], false).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "ban.success&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getAuthor()))).mentionRepliedUser(false).queue();

            } else {

                event.getMessage().replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Ban", "Möchtest du **" + member.getUser().getAsMention() + "** wirklich bannen?")).build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "ban.success&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getAuthor()))).mentionRepliedUser(false).queue();

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

                event.replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Ban", "Möchtest du **" + event.getOption("user").getAsUser().getAsMention() + "** wirklich bannen?")).addField("Grund:", event.getOption("reason").getAsString(), false).build()).addActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "ban.success&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getUser()))).queue();

            } else {

                event.replyEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Ban", "Möchtest du **" + event.getOption("user").getAsUser().getAsMention() + "** wirklich bannen?")).build()).addActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "ban.success&id=" + event.getMember().getIdLong(), "Bestätigen"), StandardActionRows.cancelButton(event.getUser()))).queue();

            }

        } catch (NullPointerException e) {

            event.replyEmbeds(EmbedTemplates.issueEmbed("Du musst einen Nutzer angeben.", false)).setEphemeral(true).queue();

        }

    }

    public static MessageEmbed performCommand(Member member, String reason, Member staff) {

        String punishment = "<(ban)(" + Date.from(Instant.now()).getTime() / 1000L + ")(" + reason + ")>";

        if (staff.canInteract(member) && !member.isOwner() && !member.equals(member.getGuild().getMemberById(MattiBot.jda.getSelfUser().getIdLong()))) {

            MainMethods.addPunishment(punishment, member.getUser(), member.getGuild());

            ServerCommand banCommand = ServerCommandManager.slashCommandsMap.get("ban");

            EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed(banCommand.commandEmoji().getName() + " Ban", "Du wurdest gebannt."));

            embedBuilder.addField("Server:", "```" + member.getGuild().getName() + "```", true);

            embedBuilder.addField("Gebannt von:", "```" + staff.getUser().getAsTag() + "```", true);

            if (!reason.equals(" ") && !reason.equals(""))
                embedBuilder.addField("Grund:", "```" + reason + "```", false);

            try {

                member.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(embedBuilder.build()).queue());

            } catch (UnsupportedOperationException | ErrorResponseException ignored) {

            }

            member.ban(0, reason).queueAfter(1, TimeUnit.SECONDS);

            EmbedBuilder embedBuilder1 = new EmbedBuilder(EmbedTemplates.standardEmbed(banCommand.commandEmoji().getName() + " Ban", ""));

            embedBuilder1.addField("Gebannt:", "```" + member.getUser().getAsTag() + "```", true);

            if (!reason.equals(" ") && !reason.equals(""))
                embedBuilder1.addField("Grund:", "```" + reason + "```", false);

            return embedBuilder1.build();

        } else if (member.isOwner()) {

            return EmbedTemplates.issueEmbed("Du kannst den Owner eines Servers nicht bannen.", false);

        } else if (member.equals(member.getGuild().getMemberById(MattiBot.jda.getSelfUser().getIdLong()))) {

            return EmbedTemplates.issueEmbed("Der Bot kann sich nicht selber bannen.", false);

        } else {

            return EmbedTemplates.issueEmbed("Du kannst diesen Nutzer nicht bannen.", false);

        }

    }

}
