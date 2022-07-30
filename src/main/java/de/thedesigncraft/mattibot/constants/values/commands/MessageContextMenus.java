package de.thedesigncraft.mattibot.constants.values.commands;

import de.thedesigncraft.mattibot.contextmenus.types.MessageContextMenu;
import de.thedesigncraft.mattibot.functions.report.ReportMessageContextMenu;

import java.util.ArrayList;
import java.util.List;

public interface MessageContextMenus {

    static List<MessageContextMenu> messageContextMenus() {

        List<MessageContextMenu> returnList = new ArrayList<>();

        // Add UserContextMenus
        returnList.add(new ReportMessageContextMenu());

        return returnList;

    }

}
