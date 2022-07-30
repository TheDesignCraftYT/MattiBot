package de.thedesigncraft.mattibot.constants.values.commands;

import de.thedesigncraft.mattibot.contextmenus.types.UserContextMenu;
import de.thedesigncraft.mattibot.functions.report.ReportUserContextMenu;

import java.util.ArrayList;
import java.util.List;

public interface UserContextMenus {

    static List<UserContextMenu> userContextMenus() {

        List<UserContextMenu> returnList = new ArrayList<>();

        // Add UserContextMenus
        returnList.add(new ReportUserContextMenu());

        return returnList;

    }

}
