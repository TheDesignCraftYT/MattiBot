package de.thedesigncraft.mattibot.commands.categories.misc;

import de.thedesigncraft.mattibot.MattiBot;
import de.thedesigncraft.mattibot.commands.types.OneSelectionMenu;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.values.commands.Categories;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CreditsServerCommand implements OneSelectionMenu {
    @Override
    public MessageEmbed mainPage() {
        return EmbedTemplates.standardEmbed(commandEmoji().getName() + " " + settings()[0], "Willkommen bei den Credits!\nHier findest du alle Mitwirkenden dieses Bots aufgelistet.\n\nDu kannst unten einen Mitwirkenden auswählen und dir die Informationen über diesen ansehen.");
    }

    @Override
    public String[] settings() {
        return new String[]{"Credits", "contributor", "Der Mitwirkende, über den du anzeigen lassen möchtest."};
    }

    @Override
    public ConcurrentHashMap<String, String[]> choices() {

        ConcurrentHashMap<String, String[]> returnMap = new ConcurrentHashMap<>();

        returnMap.put("TheDesignCraft", new String[]{"Emil", "810769870521106464", "TheDesignCraftYT"});
        returnMap.put("Blocky.jar", new String[]{"Dominic", "731080543503908895", "BlockyDotJar", "Verbindung zur Twitch API"});

        return returnMap;
    }

    @Override
    public MessageEmbed choiceEmbed(String name, String[] args) {

        EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Credits", "Informationen über den Mitwirkenden `" + name + "`."));

        embedBuilder.addField("Name", args[0] + " (aka. " + name + ")", false);

        embedBuilder.addField("Discord User", "`" + MattiBot.jda.retrieveUserById(args[1]).complete().getAsTag() + " (`" + MattiBot.jda.retrieveUserById(args[1]).complete().getAsMention() + "`)`", false);

        embedBuilder.addField("GitHub", "[" + args[2] + "](https://github.com/" + args[2] + ")", false);

        if (args.length > 3) {

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("```");

            int i = 3;

            int length = args.length - 1;

            while (i <= length) {

                stringBuilder
                        .append("• ")
                        .append(args[i])
                        .append("\n");

                i++;

            }

            stringBuilder.append("```");

            embedBuilder.addField("Mitgewirkt in folgenden Systemen", stringBuilder.toString(), false);

        }

        return embedBuilder.build();
    }

    @Override
    public String version() {
        return Versions.versions().get("v100a9");
    }

    @Override
    public String description() {
        return "Zeigt dir die Credits des Bots an.";
    }

    @Override
    public String category() {
        return Categories.categories().get("misc");
    }

    @Override
    public List<OptionData> options() {
        return OneSelectionMenu.super.options();
    }

    @Override
    public boolean slashCommand() {
        return true;
    }

    @Override
    public Emoji commandEmoji() {
        return Emoji.fromUnicode("U+1F9D1 U+200D U+1F4BB");
    }

    @Override
    public List<Permission> requiredPermissions() {
        return null;
    }

    @Override
    public void performGuildMessageCommand(MessageReceivedEvent event) {
        OneSelectionMenu.super.performGuildMessageCommand(event);
    }

    @Override
    public void performSlashCommand(SlashCommandInteractionEvent event) {
        OneSelectionMenu.super.performSlashCommand(event);
    }
}
