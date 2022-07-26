package de.thedesigncraft.mattibot.constants.values;

import de.thedesigncraft.mattibot.constants.values.commands.Versions;

import java.util.ArrayList;
import java.util.List;

public interface UpdateFunctions {

    static List<String[]> updateFunctions() {

        List<String[]> returnList = new ArrayList<>();

        // returnList.add(new String[]{Versions.versions().get("VERSION_NAME"), "FUNCTION_NAME", "FUNCTION_DESCRIPTION"});

        // v1.0.0-alpha.2
        returnList.add(new String[]{Versions.versions().get("v100a2"), "/tempban", "Einige sehr große Fehler im Command wurden behoben und er funktioniert nun einwandfrei."});

        // v1.0.0-alpha.3
        returnList.add(new String[]{Versions.versions().get("v100a3"), "/ban", "Es wurden Bugs im '/ban'-Command behoben und damit das Layout dessen ein wenig überarbeitet."});
        returnList.add(new String[]{Versions.versions().get("v100a3"), "Generelle Bugs", "Es wurden ein paar generelle Bugs gefixt und Fehler behoben."});
        returnList.add(new String[]{Versions.versions().get("v100a3"), "/warn", "Es wurden Bugs im '/warn'-Command behoben und damit das Layout dessen ein wenig überarbeitet."});
        returnList.add(new String[]{Versions.versions().get("v100a3"), "/kick", "Es wurden Bugs im '/kick'-Command behoben und damit das Layout dessen ein wenig überarbeitet."});

        // v1.0.0-alpha.4
        returnList.add(new String[]{Versions.versions().get("v100a4"), "UpdateNachrichten", "Die Update-Nachrichten wurden überarbeitet und komplett automatisiert."});
        returnList.add(new String[]{Versions.versions().get("v100a4"), "/ban", "Bugs gefixt."});

        // v1.0.0-alpha.5
        returnList.add(new String[]{Versions.versions().get("v100a5"), "/ban | /tempban", "Bugs gefixt."});
        returnList.add(new String[]{Versions.versions().get("v100a5"), "/unban", "Bugs gefixt."});
        returnList.add(new String[]{Versions.versions().get("v100a5"), "/kick", "Bugs gefixt."});
        returnList.add(new String[]{Versions.versions().get("v100a5"), "/tempban", "Bugs gefixt."});

        // v1.0.0-alpha.6
        returnList.add(new String[]{Versions.versions().get("v100a6"), "/warn", "Bugs gefixt."});
        returnList.add(new String[]{Versions.versions().get("v100a6"), "/kick", "Bugs gefixt."});
        returnList.add(new String[]{Versions.versions().get("v100a6"), "/help", "Verarbeitung von ContextMenus hinzugefügt."});
        returnList.add(new String[]{Versions.versions().get("v100a6"), "UpdateNachrichten", "Verarbeitung von ContextMenus hinzugefügt."});

        // v1.0.0-alpha.7
        returnList.add(new String[]{Versions.versions().get("v100a7"), "/help", "ContextMenuCommands werden jetzt bei `/help command:` als Option angezeigt."});

        // v1.0.0-alpha.8
        returnList.add(new String[]{Versions.versions().get("v100a8"), "/reportsystem", "SystemStatus zur Hauptseite hinzugefügt."});
        returnList.add(new String[]{Versions.versions().get("v100a8"), "/reportsystem", "Bugs beim Kanal-Festlegen behoben."});
        returnList.add(new String[]{Versions.versions().get("v100a8"), "/clear", "Für die Nachricht nach dem Löschen einen 'Fertig' Button eingebaut."});
        returnList.add(new String[]{Versions.versions().get("v100a8"), "/joinroles", "Bugs gefixt."});

        // v1.0.0-alpha.9
        returnList.add(new String[]{Versions.versions().get("v100a9"), "USER/report", "Bugs gefixt."});
        returnList.add(new String[]{Versions.versions().get("v100a9"), "MESSAGE/report", "Bugs gefixt."});
        returnList.add(new String[]{Versions.versions().get("v100a9"), "/help", "Bugs gefixt."});

        return returnList;

    }

}
