package dev.zenith.chat.control;

import com.zenith.plugin.api.Plugin;
import com.zenith.plugin.api.PluginAPI;
import com.zenith.plugin.api.ZenithProxyPlugin;
import dev.zenith.chat.control.command.ChatControlCommand;
import dev.zenith.chat.control.module.ChatControlModule;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

@Plugin(
    id = "chat-control",
    version = BuildConstants.VERSION,
    description = "Chat Control",
    url = "https://github.com/rfresh2/ZenithProxyChatControl",
    authors = {"rfresh2"},
    mcVersions = {"1.21.0"}
)
public class ChatControlPlugin implements ZenithProxyPlugin {
    public static ChatControlConfig PLUGIN_CONFIG;
    public static ComponentLogger LOG;

    @Override
    public void onLoad(PluginAPI pluginAPI) {
        LOG = pluginAPI.getLogger();
        PLUGIN_CONFIG = pluginAPI.registerConfig("chat-control", ChatControlConfig.class);
        pluginAPI.registerCommand(new ChatControlCommand());
        pluginAPI.registerModule(new ChatControlModule());
    }
}
