package dev.zenith.chat.control.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.zenith.command.api.Command;
import com.zenith.command.api.CommandCategory;
import com.zenith.command.api.CommandContext;
import com.zenith.command.api.CommandUsage;
import com.zenith.discord.Embed;
import dev.zenith.chat.control.module.ChatControlModule;

import static com.zenith.Globals.MODULE;
import static com.zenith.command.brigadier.CustomStringArgumentType.getString;
import static com.zenith.command.brigadier.CustomStringArgumentType.wordWithChars;
import static com.zenith.command.brigadier.ToggleArgumentType.getToggle;
import static com.zenith.command.brigadier.ToggleArgumentType.toggle;
import static dev.zenith.chat.control.ChatControlPlugin.PLUGIN_CONFIG;

public class ChatControlCommand extends Command {
    @Override
    public CommandUsage commandUsage() {
        return CommandUsage.builder()
            .name("chatControl")
            .category(CommandCategory.MODULE)
            .description("""
               Let players send commands through whispers
               """)
            .usageLines(
                "on/off",
                "from <whispers/publicChat> on/off",
                "whitelist on/off",
                "prefix <prefix>"
            )
            .build();
    }

    @Override
    public LiteralArgumentBuilder<CommandContext> register() {
        return command("chatControl").requires(Command::validateAccountOwner)
            .then(argument("toggle", toggle()).executes(c -> {
                PLUGIN_CONFIG.enabled = getToggle(c, "toggle");
                MODULE.get(ChatControlModule.class).syncEnabledFromConfig();
                c.getSource().getEmbed()
                    .title("Chat Control " + toggleStrCaps(PLUGIN_CONFIG.enabled));
            }))
            .then(literal("from")
                .then(literal("whispers").then(argument("w", toggle()).executes(c -> {
                    PLUGIN_CONFIG.whispers = getToggle(c, "w");
                    c.getSource().getEmbed()
                        .title("Whispers " + toggleStrCaps(PLUGIN_CONFIG.whispers));
                })))
                .then(literal("publicChat").then(argument("p", toggle()).executes(c -> {
                    PLUGIN_CONFIG.publicChat = getToggle(c, "p");
                    c.getSource().getEmbed()
                        .title("Public Chat " + toggleStrCaps(PLUGIN_CONFIG.publicChat));
                }))))
            .then(literal("whitelist").then(argument("wl", toggle()).executes(c -> {
                PLUGIN_CONFIG.whitelist = getToggle(c, "wl");
                c.getSource().getEmbed()
                    .title("Whitelist " + toggleStrCaps(PLUGIN_CONFIG.whitelist));
            })))
            .then(literal("prefix").then(argument("prefix", wordWithChars()).executes(c -> {
                PLUGIN_CONFIG.prefix = getString(c, "prefix").trim();
                c.getSource().getEmbed()
                    .title("Prefix Set");
            })));
    }

    @Override
    public void defaultEmbed(Embed embed) {
        embed
            .addField("Chat Control", toggleStr(PLUGIN_CONFIG.enabled))
            .addField("From Whispers", toggleStr(PLUGIN_CONFIG.whispers))
            .addField("From Public Chat", toggleStr(PLUGIN_CONFIG.publicChat))
            .addField("Whitelist", toggleStr(PLUGIN_CONFIG.whitelist))
            .addField("Prefix", PLUGIN_CONFIG.prefix)
            .primaryColor();
    }
}
