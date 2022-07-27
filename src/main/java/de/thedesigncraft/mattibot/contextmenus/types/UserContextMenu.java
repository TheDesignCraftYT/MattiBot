package de.thedesigncraft.mattibot.contextmenus.types;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;

import java.util.List;

public interface UserContextMenu {

    String version();

    String description();

    String category();

    Emoji commandEmoji();

    List<Permission> requiredPermissions();

    void performUserContextMenu(UserContextInteractionEvent event);

}
