package de.thedesigncraft.mattibot.listeners;

import de.thedesigncraft.mattibot.commands.categories.moderation.BanServerCommand;
import de.thedesigncraft.mattibot.commands.categories.moderation.KickServerCommand;
import de.thedesigncraft.mattibot.commands.categories.moderation.TempBanServerCommand;
import de.thedesigncraft.mattibot.commands.categories.moderation.WarnServerCommand;
import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ModerationActionRowListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        User user = event.getUser();

        if(StandardActionRows.proofButton(event, "warn.success", user)) {

            String reason = event.getButton().getId().split("&")[1].replace("reason=", "");

            Member member = event.getGuild().getMemberById(event.getMessage().getEmbeds().get(0).getDescription().replace("<", "III").replace(">", "III").replace("@", "").split("III")[1]);

            event.editMessageEmbeds(WarnServerCommand.performCommand(member, reason, event.getMember())).setActionRows().queue();

        } else if (StandardActionRows.proofButton(event, "kick.success", user)) {

            String reason = event.getButton().getId().split("&")[1].replace("reason=", "");

            Member member = event.getGuild().getMemberById(event.getMessage().getEmbeds().get(0).getDescription().replace("<", "III").replace(">", "III").replace("@", "").split("III")[1]);

            event.editMessageEmbeds(KickServerCommand.performCommand(member, reason, event.getMember())).setActionRows().queue();

        } else if (StandardActionRows.proofButton(event, "ban.success", user)) {

            String reason = event.getButton().getId().split("&")[1].replace("reason=", "");

            Member member = event.getGuild().getMemberById(event.getMessage().getEmbeds().get(0).getDescription().replace("<", "III").replace(">", "III").replace("@", "").split("III")[1]);

            event.editMessageEmbeds(BanServerCommand.performCommand(member, reason, event.getMember())).setActionRows().queue();

        } else if (StandardActionRows.proofButton(event, "tempban.success", user)) {

            Member member = event.getGuild().getMemberById(event.getMessage().getEmbeds().get(0).getDescription().replace("<", "III").replace(">", "III").replace("@", "").split("III")[1]);

            if (event.getMessage().getEmbeds().get(0).getFields().get(0).getName().equals("Grund:")) {

                String reason = event.getMessage().getEmbeds().get(0).getFields().get(0).getValue().replace("```", "");
                String time = event.getMessage().getEmbeds().get(0).getFields().get(1).getValue().replace("```", "");

                event.editMessageEmbeds(TempBanServerCommand.performCommand(member, reason, event.getMember(), time)).setActionRows().queue();

            } else if (event.getMessage().getEmbeds().get(0).getFields().get(0).getName().equals("Entbannung:")) {

                String time = event.getMessage().getEmbeds().get(0).getFields().get(0).getValue().replace("```", "");

                event.editMessageEmbeds(TempBanServerCommand.performCommand(member, "", event.getMember(), time)).setActionRows().queue();

            } else {

                event.editMessageEmbeds(TempBanServerCommand.performCommand(member, "", event.getMember(), "")).setActionRows().queue();

            }
            
        }

    }
}
