package de.thedesigncraft.mattibot.constants.methods;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;

public interface ServerCommandMethods {

    static String getCommandName(ServerCommand serverCommand) {

        return serverCommand.getClass().getSimpleName().split("ServerCommand")[0].toLowerCase();

    }

}
