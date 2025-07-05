# Gateway Service with Telegram Bot Integration

This service provides a gateway for the Budget Analyzer application with Telegram bot integration for user interaction.

## Features

- Telegram bot for user interaction
- REST API for sending messages to users
- Mock user implementation (to be replaced with a real user service)

## Setup

### Prerequisites

- Java 11 or higher
- Gradle
- Telegram Bot Token (obtained from BotFather)

### Configuration

Configure the Telegram bot in `application.yml`:

```yaml
telegram:
  bot:
    username: ${TELEGRAM_BOT_USERNAME:budget_analyzer_bot}
    token: ${TELEGRAM_BOT_TOKEN:your_bot_token_here}
```

You can set these values directly in the file or provide them as environment variables:

```bash
export TELEGRAM_BOT_USERNAME=your_bot_username
export TELEGRAM_BOT_TOKEN=your_bot_token
```

### Creating a Telegram Bot

1. Open Telegram and search for `@BotFather`
2. Start a chat with BotFather and send `/newbot`
3. Follow the instructions to create a new bot
4. Once created, BotFather will provide you with a token
5. Use this token in your configuration

## Usage

### Interacting with the Bot

Users can interact with the bot by sending messages to it on Telegram. The bot currently supports:

- `/start` - Welcome message
- Any other message - Echo response

### Sending Messages via API

You can send messages to users via the REST API:

```
POST /api/telegram/send/{chatId}
Content-Type: text/plain

Your message here
```

Where `{chatId}` is the Telegram chat ID of the user.

### Health Check

Check if the Telegram service is running:

```
GET /api/telegram/health
```

## Development

### Mock User

The current implementation uses a mock user. In a production environment, this should be replaced with a real user service that retrieves users from a database.

### Adding Commands

To add new commands to the bot, modify the `processMessage` method in `TelegramBotService.java`.
