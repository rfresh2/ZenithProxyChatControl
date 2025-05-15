package dev.zenith.chat.control.module;

import com.github.rfresh2.EventConsumer;
import com.zenith.Proxy;
import com.zenith.command.api.CommandContext;
import com.zenith.command.api.CommandSource;
import com.zenith.discord.Embed;
import com.zenith.event.chat.PublicChatEvent;
import com.zenith.event.chat.WhisperChatEvent;
import com.zenith.module.api.Module;
import com.zenith.util.ChatUtil;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntry;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;

import java.util.List;

import static com.github.rfresh2.EventConsumer.of;
import static com.zenith.Globals.*;
import static dev.zenith.chat.control.ChatControlPlugin.PLUGIN_CONFIG;

public class ChatControlModule extends Module {
    @Override
    public boolean enabledSetting() {
        return PLUGIN_CONFIG.enabled;
    }

    @Override
    public List<EventConsumer<?>> registerEvents() {
        return List.of(
            of(WhisperChatEvent.class, this::onWhisper),
            of(PublicChatEvent.class, this::onPublicChat)
        );
    }

    private void onWhisper(WhisperChatEvent event) {
        if (!PLUGIN_CONFIG.whispers) return;
        if (event.outgoing()) return;
        String messageContents = event.message().substring(event.message().indexOf(':') + 1).trim();
        var sender = event.sender();
        executeCommand(sender, messageContents);
    }

    private void onPublicChat(PublicChatEvent event) {
        if (!PLUGIN_CONFIG.publicChat) return;
        if (event.sender().getProfileId().equals(CACHE.getProfileCache().getProfile().getId())) return;
        String messageContents = event.message();
        if (event.isDefaultMessageSchema()) {
            messageContents = event.extractMessageDefaultSchema();
        }
        executeCommand(event.sender(), messageContents);
    }

    private void executeCommand(PlayerListEntry sender, String messageContents) {
        if (!messageContents.startsWith(PLUGIN_CONFIG.prefix)) return;
        messageContents = messageContents.substring(PLUGIN_CONFIG.prefix.length());
        if (PLUGIN_CONFIG.whitelist && !PLAYER_LISTS.getWhitelist().contains(sender.getProfileId())) {
            info("Ignoring non-whitelisted command from: " + sender.getName());
            return;
        }
        discordAndIngameNotification(Embed.builder()
            .title("Command Received")
            .addField("Sender", "[" + sender.getName() + "](https://namemc.com/profile/" + sender.getProfileId() + ")")
            .addField("Command", messageContents)
            .thumbnail(Proxy.getInstance().getPlayerBodyURL(sender.getProfileId()).toString())
        );
        var commandContext = CommandContext.create(messageContents, ChatControlCommandSource.INSTANCE);
        commandContext.getData().put("ChatControlSender", sender);
        COMMAND.execute(commandContext);
        String response;
        if (commandContext.getEmbed().isTitlePresent()) {
            response = ChatUtil.sanitizeChatMessage(commandContext.getEmbed().title());
        } else {
            response = "Command executed";
        }
        discordAndIngameNotification(commandContext.getEmbed());
        sendClientPacketAsync(new ServerboundChatCommandPacket("msg " + sender.getName() + " " + response));
    }

    public static class ChatControlCommandSource implements CommandSource {
        public static final ChatControlCommandSource INSTANCE = new ChatControlCommandSource();
        @Override
        public String name() {
            return "Chat Control";
        }

        @Override
        public boolean validateAccountOwner(final CommandContext ctx) {
            return false;
        }

        @Override
        public void logEmbed(final CommandContext ctx, final Embed embed) {
            if (!embed.isTitlePresent()) return;
            var senderCtx = ctx.getData().get("ChatControlSender");
            if (senderCtx == null) return;
            if (!(senderCtx instanceof PlayerListEntry sender)) return;
            Proxy.getInstance().getClient().sendAsync(new ServerboundChatCommandPacket("msg " + sender.getName() + " " + ChatUtil.sanitizeChatMessage(embed.title())));
        }
    }
}
