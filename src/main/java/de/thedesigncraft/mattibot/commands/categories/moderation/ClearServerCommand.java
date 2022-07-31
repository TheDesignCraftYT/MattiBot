package de.thedesigncraft.mattibot.commands.categories.moderation;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import de.thedesigncraft.mattibot.constants.values.commands.Categories;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ClearServerCommand extends ListenerAdapter implements ServerCommand {
    @Override
    public String version() {
        return Versions.versions().get("v100a2");
    }

    @Override
    public String description() {
        return "Löscht eine bestimmte Anzahl an Nachrichten aus dem Chat.";
    }

    @Override
    public String category() {
        return Categories.categories().get("moderation");
    }

    @Override
    public List<OptionData> options() {

        List<OptionData> returnList = new ArrayList<>();

        returnList.add(new OptionData(OptionType.INTEGER, "amount", "Die Anzahl an Nachrichten, die gelöscht werden sollen."));
        returnList.add(new OptionData(OptionType.BOOLEAN, "deletepinned", "Legt fest, ob auch angepinnte Nachrichten gelöscht werden sollen."));

        return returnList;
    }

    @Override
    public boolean slashCommand() {
        return true;
    }

    @Override
    public Emoji commandEmoji() {
        return Emoji.fromUnicode("U+1F5D1");
    }

    @Override
    public List<Permission> requiredPermissions() {

        List<Permission> returnList = new ArrayList<>();

        returnList.add(Permission.MANAGE_CHANNEL);
        returnList.add(Permission.MESSAGE_MANAGE);

        return returnList;
    }

    @Override
    public void performGuildMessageCommand(MessageReceivedEvent event) {

        String amount;
        boolean deletepinned;

        String[] args = event.getMessage().getContentDisplay().split(" ");

        if (args.length == 2) {

            if (args[1].equals("true") || args[1].equals("false")) {

                amount = "alle";
                deletepinned = Boolean.parseBoolean(args[1]);

                event.getMessage().replyEmbeds(performCommand(amount, deletepinned)).mentionRepliedUser(false).queue();

            } else {

                try {
                    Integer.parseInt(args[1]);

                    amount = args[1];

                    event.getMessage().replyEmbeds(performCommand(amount, false)).mentionRepliedUser(false).setActionRows(ActionRow.of(Button.of(ButtonStyle.DANGER, "clear.success&id=" + event.getAuthor().getIdLong(), "Nachrichten löschen"), StandardActionRows.cancelButton(event.getAuthor()))).queue();

                } catch (NumberFormatException e) {

                    event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Falsche Argumente. Führe `/help` aus, um Hilfe zu bekommen.", true)).mentionRepliedUser(false).queue(message -> {

                        event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                        message.delete().queueAfter(10, TimeUnit.SECONDS);

                    });

                }

            }

        } else if (args.length == 3) {

            if (args[2].equals("true") || args[2].equals("false")) {

                try {

                    Integer.parseInt(args[1]);

                    amount = args[1];
                    deletepinned = Boolean.parseBoolean(args[2]);

                    event.getMessage().replyEmbeds(performCommand(amount, deletepinned)).mentionRepliedUser(false).setActionRows(ActionRow.of(Button.of(ButtonStyle.DANGER, "clear.success&id=" + event.getAuthor().getIdLong(), "Nachrichten löschen"), StandardActionRows.cancelButton(event.getAuthor()))).queue();

                } catch (NumberFormatException e) {

                    event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Falsche Argumente. Führe `/help` aus, um Hilfe zu bekommen.", true)).mentionRepliedUser(false).queue(message -> {

                        event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                        message.delete().queueAfter(10, TimeUnit.SECONDS);

                    });

                }

            } else if (args[1].equals("true") || args[1].equals("false")) {

                try {

                    Integer.parseInt(args[2]);

                    amount = args[2];
                    deletepinned = Boolean.parseBoolean(args[1]);

                    event.getMessage().replyEmbeds(performCommand(amount, deletepinned)).mentionRepliedUser(false).setActionRows(ActionRow.of(Button.of(ButtonStyle.DANGER, "clear.success&id=" + event.getAuthor().getIdLong(), "Nachrichten löschen"), StandardActionRows.cancelButton(event.getAuthor()))).queue();

                } catch (NumberFormatException e) {

                    event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Falsche Argumente. Führe `/help` aus, um Hilfe zu bekommen.", true)).mentionRepliedUser(false).queue(message -> {

                        event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                        message.delete().queueAfter(10, TimeUnit.SECONDS);

                    });

                }

            } else {

                event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Falsche Argumente. Führe `/help` aus, um Hilfe zu bekommen.", true)).mentionRepliedUser(false).queue(message -> {

                    event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                    message.delete().queueAfter(10, TimeUnit.SECONDS);

                });

            }

        } else if (args.length == 1) {

            event.getMessage().replyEmbeds(performCommand("alle", false)).mentionRepliedUser(false).setActionRows(ActionRow.of(Button.of(ButtonStyle.DANGER, "clear.success&id=" + event.getAuthor().getIdLong(), "Nachrichten löschen"), StandardActionRows.cancelButton(event.getAuthor()))).queue();

        } else {

            event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Falsche Argumente. Führe `/help` aus, um Hilfe zu bekommen.", true)).mentionRepliedUser(false).queue(message -> {

                event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                message.delete().queueAfter(10, TimeUnit.SECONDS);

            });

        }

    }

    @Override
    public void performSlashCommand(SlashCommandInteractionEvent event) {

        String amount;
        boolean deletepinned;

        if (event.getOption("amount") != null) {

            amount = event.getOption("amount").getAsString();

        } else {

            amount = "alle";

        }

        if (event.getOption("deletepinned") != null) {

            deletepinned = event.getOption("deletepinned").getAsBoolean();

        } else {

            deletepinned = false;

        }

        event.replyEmbeds(performCommand(amount, deletepinned)).addActionRows(ActionRow.of(Button.of(ButtonStyle.DANGER, "clear.success&id=" + event.getUser().getIdLong(), "Nachrichten löschen"), StandardActionRows.cancelButton(event.getUser()))).queue();

    }

    private MessageEmbed performCommand(String amount, boolean deletepinned) {

        EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Nachrichten löschen", "Möchtest du wirklich **" + amount + "** Nachrichten löschen?"));

        if (deletepinned) {

            return embedBuilder.appendDescription(" (Angepinnte Nachrichten werden **auch** gelöscht)").build();

        } else {

            return embedBuilder.appendDescription(" (Angepinnte Nachrichten werden **nicht** gelöscht)").build();

        }

    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        User user = event.getUser();

        if (StandardActionRows.proofButton(event, "clear.success", user)) {

            String amount = event.getMessage().getEmbeds().get(0).getDescription().replace("**", "III").split("III")[1];
            boolean deletepinned = Boolean.parseBoolean(event.getMessage().getEmbeds().get(0).getDescription().replace("**", "III").split("III")[3].replace("nicht", "false").replace("auch", "true"));

            List<Message> deleteMessages = new ArrayList<>();

            if (amount.equals("alle")) {

                event.getChannel().getIterableHistory().skipTo(event.getMessageIdLong()).forEach(message -> {

                    if (deletepinned) {

                        deleteMessages.add(message);

                    } else if (!message.isPinned()) {

                        deleteMessages.add(message);

                    }

                });

            } else {

                try {

                    event.getChannel().getIterableHistory().skipTo(event.getMessageIdLong()).takeAsync(Integer.parseInt(amount)).get().forEach(message -> {

                        if (deletepinned) {

                            deleteMessages.add(message);

                        } else if (!message.isPinned()) {

                            deleteMessages.add(message);

                        }

                    });

                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }

            }

            event.getChannel().purgeMessages(deleteMessages);

            event.editMessageEmbeds(new EmbedBuilder(event.getMessage().getEmbeds().get(0)).setDescription("Es wurden **" + amount + "** Nachrichten gelöscht.").build()).setActionRows(ActionRow.of(Button.of(ButtonStyle.SUCCESS, "cancel&id=" + user.getIdLong(), "Fertig"))).queue();

        }

    }
}
