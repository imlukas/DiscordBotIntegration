package dev.imlukas.commands.ticket.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class ButtonHandler extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Button button = event.getButton();
        Guild guild = event.getGuild();

        if (button.getId().equals("Support")) {


            if (guild.getCategoriesByName("support-tickets", true).isEmpty()) {
                guild.createCategory("support-tickets").complete();
            }

            Category category = guild.getCategoriesByName("support-tickets", true).get(0);
            category.createTextChannel("support-" + event.getUser().getName().replace(" ", "-")).queue();

            event.reply("Created a ticket, mods will help you soon!").setEphemeral(true).queue();
        }
    }
}
