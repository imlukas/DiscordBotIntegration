package me.imlukas.slashcommands;

import me.imlukas.slashcommands.annotations.Option;
import me.imlukas.slashcommands.annotations.SlashCommand;
import me.imlukas.slashcommands.commands.member.BanSlashCommand;
import me.imlukas.slashcommands.commands.member.UnbanSlashCommand;
import me.imlukas.slashcommands.commands.server.RolesCommand;
import me.imlukas.slashcommands.commands.server.ServerCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlashCommandManager {

    private final List<ISlashCommand> commands = new ArrayList<>();
    private Map<String, Object[]> commandMap;
    public void init(Guild guild, SlashCommandContext ctx) {
        commandMap = new HashMap<>();
        commands.add(new BanSlashCommand());
        commands.add(new UnbanSlashCommand());
        commands.add(new RolesCommand());
        commands.add(new ServerCommand());

        List<CommandData> commands = new ArrayList<>();

        for (ISlashCommand command : getCommands()) {
            String commandName = command.getName();

            Class<?> clazz = command.getClass();

            Method[] methods = clazz.getMethods();

            for (Method method : methods) {

                Parameter[] parameters = method.getParameters();
                Object[] args = new Object[parameters.length];


                for (int index = 0; index < parameters.length; index++) {
                    Parameter parameter = parameters[index];
                    if (parameter.isAnnotationPresent(Option.class)) {
                        args[index] = parameter.getAnnotation(Option.class);
                    } else if (parameter.getType() == SlashCommandContext.class) {
                        args[index] = ctx;
                    }
                }
                commandMap.put(commandName, args);
            }
            SlashCommandData commandData = Commands.slash(commandName, command.getDescription());

            if (commandMap.get(commandName) != null) {
                for (Object arg : commandMap.get(commandName)) {
                    if (!(arg instanceof Option option)) {
                        continue;
                    }
                    commandData.addOption(option.type(), option.name(), option.description(), option.required());
                }
            }
            commands.add(commandData.setDefaultPermissions(command.getPermission()));

        }
        updateCommands(guild, commands);
    }

    public void updateCommands(Guild guild, List<CommandData> commands) {
        guild.updateCommands()
                .addCommands(commands)
                .queue();
    }


    public List<ISlashCommand> getCommands() {
        return commands;
    }

    @Nullable
    public ISlashCommand getCommand(String search) {

        String searchLower = search.toLowerCase();

        for (ISlashCommand cmd : this.commands) {
            if (cmd.getName().equalsIgnoreCase(searchLower)) {
                return cmd;
            }
        }

        return null;

    }

    public void run(ISlashCommand instance, Method method, SlashCommandContext ctx, Map<String, Object> options) {
        Object[] args = commandMap.get(instance.getName());
        try {
            method.invoke(instance, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handle(SlashCommandInteractionEvent event) {

        ISlashCommand command = getCommand(event.getName());

        if (command == null)
            return;


        SlashCommandContext ctx = new SlashCommandContext(event);
        Map<String, Object> options = new HashMap<>();

        event.getOptions().forEach(option -> options.put(option.getName(), OptionTypeWrapper.fromType(option.getType()).get(option)));

        Class<?> clazz = command.getClass();

        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(SlashCommand.class)) {
                run(command, method, ctx, options);
                break;
            }
        }

        event.reply("Command executed!").queue();

    }


}
