package com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api

import com.github.tolyakulak.discordspammer.core.data.FakeDiscordApi
import com.github.tolyakulak.discordspammer.core.data.remote.DiscordApi
import com.github.tolyakulak.discordspammer.core.di.TestDiscordModule
import com.github.tolyakulak.discordspammer.core.domain.model.Channel
import com.github.tolyakulak.discordspammer.core.domain.model.Message
import com.github.tolyakulak.discordspammer.core.domain.repository.DiscordRepository
import com.github.tolyakulak.discordspammer.core.domain.util.InvalidInputException
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class DiscordUseCasesTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var discordApi: DiscordApi
    private lateinit var repository: DiscordRepository
    private lateinit var discordUseCases: DiscordUseCases

    @Before
    fun setup() {
        mockWebServer = TestDiscordModule.provideMockWebServer()
        discordApi = TestDiscordModule.provideDiscordApi(mockWebServer)
        repository = TestDiscordModule.provideDiscordRepository(discordApi)
        discordUseCases = TestDiscordModule.provideDiscordUseCases(repository)
    }

    @After
    fun cleanup() {
        mockWebServer.shutdown()
    }

    @Test
    fun `invalid token throws http exception with error code`() = runTest {
        val response = discordUseCases.getMyAccount("48329048302fhjkhe")
        assertThat(response.isFailure).isTrue()

        val exc = response.exceptionOrNull() as? HttpException

        assertThat(exc?.code()).isEqualTo(401)
    }

    @Test
    fun `get me returns user`() = runTest {
        val response = discordUseCases.getMyAccount(FakeDiscordApi.TOKEN)
        assertThat(response.isSuccess).isTrue()
        val user = response.getOrNull()
        assertThat(user).isNotNull()

        assertThat(user?.username).isEqualTo(FakeDiscordApi.MY_USERNAME)
        assertThat(user?.id).isEqualTo(FakeDiscordApi.MY_ID)
    }

    @Test
    fun `get channels returns list of channels`() = runTest {
        val response =
            discordUseCases.getChannelsOfGuild(FakeDiscordApi.TOKEN, FakeDiscordApi.GUILD)
        assertThat(response.isSuccess).isTrue()
        val channels = response.getOrNull()
        assertThat(channels).isNotNull()

        val group = channels?.get(0)
        val channel = channels?.get(1)
        assertThat(group).isEqualTo(
            Channel(
                id = "435346543",
                type = Channel.GUILD_CATEGORY,
                guildId = FakeDiscordApi.GUILD,
                name = "Text group",
                parentId = null
            )
        )


        assertThat(channel).isEqualTo(
            Channel(
                id = FakeDiscordApi.CHANNEL,
                type = Channel.TEXT_CHANNEL,
                guildId = FakeDiscordApi.GUILD,
                name = "Text channel",
                parentId = "435346543"
            )
        )
    }

    @Test
    fun `get my guilds returns list of guilds`() = runTest {
        val response = discordUseCases.getMyGuilds(FakeDiscordApi.TOKEN)
        assertThat(response.isSuccess).isTrue()
        val guild = response.getOrNull()?.get(0)

        assertThat(guild).isNotNull()
        assertThat(guild?.id).isEqualTo(FakeDiscordApi.GUILD)
    }

    @Test
    fun sendMessage() = runTest {
        val result =
            discordUseCases.sendMessage(
                FakeDiscordApi.TOKEN,
                FakeDiscordApi.CHANNEL,
                "Hi",
                null
            )
        assertThat(result.isSuccess).isTrue()

        val message: Message? = result.getOrNull()
        assertThat(message).isNotNull()
    }

    @Test
    fun `send message to non existent channel id throws 404`() = runTest {
        val result =
            discordUseCases.sendMessage(
                FakeDiscordApi.TOKEN,
                "483290431242",
                "Hi",
                null
            )
        assertThat(result.isSuccess).isFalse()

        val exc = result.exceptionOrNull() as? HttpException
        assertThat(exc?.code()).isEqualTo(404)
    }

    @Test
    fun `send message to invalid channel id throws InvalidInputException`() = runTest {
        val result = discordUseCases.sendMessage(
            FakeDiscordApi.TOKEN,
            "483294324fdxfs0431242",
            "Hi",
            null
        )
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidInputException::class.java)
    }

    @Test
    fun `send empty message throws InvalidInputException`() = runTest {
        val result = discordUseCases.sendMessage(
                FakeDiscordApi.TOKEN,
                "48329047549tujfoisudio34hfe",
                "",
                null
        )
        assertThat(result.isFailure).isTrue()
        val exc = result.exceptionOrNull()
        assertThat(exc).isInstanceOf(InvalidInputException::class.java)
        assertThat(exc).hasMessageThat().isNotEmpty()
    }
}