package com.github.tolyakulak.discordspammer.core.data

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

object FakeDiscordApi : Dispatcher() {
    const val TOKEN = "token"
    const val GUILD = "10000002"
    const val MY_ID = "111111111111111111"
    const val CHANNEL = "302132134"
    const val MY_USERNAME = "username32342"

    override fun dispatch(request: RecordedRequest): MockResponse {
        if (request.headers["Authorization"] != TOKEN) {
            return MockResponse()
                .setResponseCode(401)
                .setBody("""{"message": "401: Unauthorized", "code": 0}""")
        }

        return when (request.path) {
            "/users/@me" -> {
                MockResponse()
                    .setResponseCode(200)
                    .setBody(
                        """{"id": "$MY_ID", "username": "$MY_USERNAME", "avatar": "28ae058479f2e2988e862174802cdb21",
        |"discriminator": "0", "public_flags": 256, "flags": 288, "banner": "8a7304ad846a2988ec4e09628889d7e9", "accent_color": null,
        |"global_name": "nickname", "avatar_decoration_data": null, "banner_color": null, "mfa_enabled": false, "locale": "uk", "premium_type": 2, 
        |"email": "email@example.com", "verified": true, "phone": "+380970000000", "nsfw_allowed": true, "premium_usage_flags": 4,
        |"linked_users": [], "purchased_flags": 2, "bio": "my bio lmao","authenticator_types": []}""".trimMargin()
                    )
            }

            "/users/@me/guilds" -> {
                MockResponse()
                    .setResponseCode(200)
                    .setBody(
                        """[{"id":"$GUILD","name":"Name_1","icon":"a_c70f4d704a9842729788a07619s0be8b","owner":false,
                            |"permissions":"533235222251008","features":["CHANNEL_ICON_EMOJIS_GENERATED","COMMUNITY","PREVIEW_ENABLED",
                            |"ENABLED_DISCOVERABLE_BEFORE","THREADS_ENABLED","ANIMATED_ICON","NEWS","WELCOME_SCREEN_ENABLED","THREE_DAY_THREAD_ARCHIVE",
                            |"DISCOVERABLE","NEW_THREAD_PERMISSIONS","TEXT_IN_VOICE_ENABLED","SOUNDBOARD","INVITE_SPLASH"]},{"id":"1002",
                            |"name":"Name_2","icon":"ddcd5683105b11666ec8930c175a35db","owner":false,"permissions":"562949953421311",
                            |"features":[]}]""".trimMargin()
                    )
            }

            "/guilds/$GUILD/channels" -> {
                MockResponse()
                    .setResponseCode(200)
                    .setBody(
                        """[{"id": "435346543", "type": 4, "flags": 0, "guild_id": "$GUILD", "name": "Text group", "parent_id": null,
        |"position": 2, "permission_overwrites": [] }, { "id": "$CHANNEL", "type": 0, "last_message_id": "435435","flags": 0,
        |"last_pin_timestamp": "2023-06-25T18:29:05+00:00","guild_id": "$GUILD","name": "Text channel","parent_id": "435346543","rate_limit_per_user": 0,
        |"topic": null,"position": 5,"permission_overwrites": [],"nsfw": true}]""".trimMargin()
                    )
            }
            "/channels/$CHANNEL/messages" -> MockResponse()
                .setResponseCode(200)
                .setBody("""{
    "id": "4554345345",
    "type": 0,
    "content": "Sent with Postman",
    "channel_id": "839069741397966861",
    "author": {
        "id": "54354325",
        "username": "54353245",
        "avatar": "5345342534",
        "discriminator": "0",
        "public_flags": 256,
        "flags": 256,
        "banner": "543534253425",
        "accent_color": null,
        "global_name": "54353245324",
        "avatar_decoration_data": null,
        "banner_color": null
    },
    "attachments": [],
    "embeds": [],
    "mentions": [],
    "mention_roles": [],
    "pinned": false,
    "mention_everyone": false,
    "tts": false,
    "timestamp": "2023-09-28T08:43:07.940000+00:00",
    "edited_timestamp": null,
    "flags": 0,
    "components": [],
    "referenced_message": null
}""")

            else -> MockResponse().setResponseCode(404)
        }
    }
}