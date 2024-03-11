package dev.imlukas.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AutoDisconnectListener extends ListenerAdapter {

    private final Map<Long, ScheduledFuture<?>> scheduledFutures = new HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Guild guild = event.getGuild();

        AudioChannelUnion joinedChannel = event.getChannelJoined();

        if (joinedChannel != null) {
            System.out.println("Connected: " + event.getEntity().getEffectiveName());
            ScheduledFuture<?> scheduledFuture = scheduledFutures.get(guild.getIdLong());

            if (scheduledFuture == null) {
                return;
            }

            System.out.println("Cancelling disconnect");
            scheduledFuture.cancel(true);
            scheduledFutures.remove(guild.getIdLong());
            return;
        }

        AudioChannelUnion audioChannel = event.getChannelLeft();

        if (audioChannel == null) {
            return;
        }

        System.out.println("Disconnected: " + event.getEntity().getEffectiveName());
        List<Member> members = audioChannel.getMembers();

        if (members.size() > 1) {
            return;
        }

        System.out.println("Scheduling disconnect");
        ScheduledFuture<?> scheduledFuture = scheduler.schedule(() -> {
            guild.getAudioManager().closeAudioConnection();
        }, 5, TimeUnit.SECONDS);

        scheduledFutures.put(guild.getIdLong(), scheduledFuture);
    }
}
