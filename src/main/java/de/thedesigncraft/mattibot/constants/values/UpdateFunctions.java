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

        return returnList;

    }

}
