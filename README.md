# ZenithProxy Chat Control

Lets whitelisted players send commands through whispers or public chat.

## Warning

Letting in-game players send commands is inherently insecure and unstable. It relies on the server's authentication and chat formatting.

A better option is: [ZenithProxyMod](https://github.com/rfresh2/ZenithProxyMod#web-api-commands) + [ZenithProxyWebAPI](https://github.com/rfresh2/ZenithProxyWebAPI)

The mod adds an in-game command to send commands through an authenticated web API. Example:`/api command myProxy pearlLoader load myFriend`

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

ZenithProxy commands return responses formatted for discord embeds.

But in-game whispers cannot be formatted with multiple lines or long text.

So the output of chat control commands is limited to only the embed's title.

ZenithProxyMod + ZenithProxyWebAPI has full command response formatting if you want a better experience.
