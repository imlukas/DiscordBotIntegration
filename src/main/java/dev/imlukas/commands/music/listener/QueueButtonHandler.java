package dev.imlukas.commands.music.listener;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.imlukas.util.misc.utils.MusicUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class QueueButtonHandler extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        Button button = event.getButton();
        Guild guild = event.getGuild();
    }
}