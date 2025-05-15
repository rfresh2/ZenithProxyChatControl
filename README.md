# ZenithProxy Chat Control

Lets whitelisted players send commands through whispers or public chat.

## Warning

Letting in-game players send commands is inherently insecure and unstable. 

You are relying on the server's authentication, text formatting, and not being muted

A better option is: [ZenithProxyMod](https://github.com/rfresh2/ZenithProxyMod#web-api-commands) + [ZenithProxyWebAPI](https://github.com/rfresh2/ZenithProxyWebAPI)

The mod adds an in-game command to send commands through the web API 

Mod command example:`/api command myProxy pearlLoader load myFriend`

## Usage

### Commands

* `chatControl on/off` -> default: ON
* `chatControl from <whispers/publicChat> on/off` -> default: whispers=ON publicChat=OFF
* `chatControl whitelist on/off` -> only lets whitelisted players send commands, default: ON

### Chat Control Commands

Prefix chats/whispers with `!` to send commands

<img src="https://i.imgur.com/2rCu4Vt.png" alt="Public Chat Command Example">

<img src="https://i.imgur.com/84SKv5s.png" alt="Whisper Command Example">

### Chat Control Output

ZenithProxy commands return too much text for whispers

So their output is limited to only the response's title

Use ZenithProxyMod + ZenithProxyWebAPI for full command output and formatting
