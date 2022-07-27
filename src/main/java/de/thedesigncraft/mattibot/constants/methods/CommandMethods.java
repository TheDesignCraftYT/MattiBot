package de.thedesigncraft.mattibot.constants.methods;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.contextmenus.types.MessageContextMenu;
import de.thedesigncraft.mattibot.contextmenus.types.UserContextMenu;

public interface CommandMethods {

    static String getServerCommandName(ServerCommand serverCommand) {

        return serverCommand.getClass().getSimpleName().split("ServerCommand")[0].toLowerCase();

    }

    static String getUserContextMenuName(UserContextMenu userContextMenu) {

        return userContextMenu.getClass().getSimpleName().split("UserContextMenu")[0].toLowerCase();

    }

    static String getMessageContextMenuName(MessageContextMenu messageContextMenu) {

        return messageContextMenu.getClass().getSimpleName().split("MessageContextMenu")[0].toLowerCase();

    }

}
