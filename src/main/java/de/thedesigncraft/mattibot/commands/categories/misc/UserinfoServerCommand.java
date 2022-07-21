package de.thedesigncraft.mattibot.commands.categories.misc;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.values.commands.Categories;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserinfoServerCommand implements ServerCommand {
    @Override
    public String version() {
        return Versions.versions().get("v100a1");
    }

    @Override
    public String description() {
        return "Zeigt alle Informationen über einen Nutzer an.";
    }

    @Override
    public String category() {
        return Categories.categories().get("misc");
    }

    @Override
    public List<OptionData> options() {
        List<OptionData> returnList = new ArrayList<>();

        returnList.add(new OptionData(OptionType.USER, "user", "Der Nutzer, dessen Informationen angezeigt werden sollen."));

        return returnList;
    }

    @Override
    public boolean slashCommand() {
        return true;
    }

    @Override
    public Emoji commandEmoji() {
        return Emoji.fromUnicode("U+1F464");
    }

    @Override
    public List<Permission> requiredPermissions() {
        return null;
    }

    @Override
    public void performGuildMessageCommand(MessageReceivedEvent event) {

        try {

            String arg = event.getMessage().getContentDisplay().replace("userinfo", "").split(" ")[1];

            if (!arg.equals("") && !arg.equals(" ")) {

                if (!event.getMessage().getMentions().getMembers().isEmpty()) {

                    event.getMessage().replyEmbeds(performCommand(event.getMessage().getMentions().getMembers().get(0))).mentionRepliedUser(false).queue();

                } else {

                    try {

                        event.getMessage().replyEmbeds(performCommand(event.getGuild().getMemberById(event.getMessage().getContentDisplay().replace("userinfo", "").split(" ")[1]))).mentionRepliedUser(false).queue();

                    } catch (NumberFormatException e) {

                        event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Das ist keine NutzerId.", true)).mentionRepliedUser(false).queue(message -> {

                            event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                            message.delete().queueAfter(10, TimeUnit.SECONDS);

                        });

                    }

                }

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Du musst einen Nutzer angeben. Du kannst entweder die Id des Nutzers schreiben, oder den Nutzer @pingen.", true)).mentionRepliedUser(false).queue(message -> {

                event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                message.delete().queueAfter(10, TimeUnit.SECONDS);

            });

        }

    }

    @Override
    public void performSlashCommand(SlashCommandInteractionEvent event) {

        if (event.getOption("user") != null) {

            event.replyEmbeds(performCommand(event.getGuild().getMemberById(event.getOption("user").getAsUser().getIdLong()))).queue();

        } else {

            event.replyEmbeds(EmbedTemplates.issueEmbed("Du musst einen Nutzer angeben.", false)).setEphemeral(true).queue();

        }

    }

    private MessageEmbed performCommand(Member member) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.copyFrom(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Userinfo", "Userinfo von **" + member.getUser().getAsTag() + "**"));

        embedBuilder.setThumbnail(member.getEffectiveAvatarUrl());
        embedBuilder.setAuthor(member.getUser().getAsTag(), member.getUser().getEffectiveAvatarUrl(), member.getUser().getEffectiveAvatarUrl());

        embedBuilder.addField("Name:", member.getUser().getName(), false);
        embedBuilder.addField("Diskriminator:", "#" + member.getUser().getDiscriminator(), false);
        embedBuilder.addField("Standard Avatar:", "```Der Avatar kann hier leider nicht dargestellt werden.\nEr wird oben links über dem Titel angezeigt.```", false);

        if (member.getUser().isBot()) {

            embedBuilder.addField("Bot:", "☑", false);

        } else {

            embedBuilder.addField("Bot:", "❎", false);

        }

        if (member.getNickname() != null) {

            embedBuilder.addField("Nickname:", member.getNickname(), false);

        } else {

            embedBuilder.addField("Nickname:", "```---```", false);

        }

        embedBuilder.addField("Server Avatar:", "```Der Server Avatar kann hier leider nicht dargestellt werden.\nEr wird oben rechts unter dem Titel angezeigt.```", false);

        long timeJoined = Date.from(member.getTimeJoined().toInstant()).getTime() / 1000L;

        embedBuilder.addField("Beitrittszeit:", "<t:" + timeJoined + ":R>", false);

        long timeCreated = Date.from(member.getTimeCreated().toInstant()).getTime() / 1000L;

        embedBuilder.addField("Erstellungszeit:", "<t:" + timeCreated + ":R>", false);

        return embedBuilder.build();

    }

}
