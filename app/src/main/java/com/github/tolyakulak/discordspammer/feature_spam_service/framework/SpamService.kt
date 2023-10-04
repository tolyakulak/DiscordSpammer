package com.github.tolyakulak.discordspammer.feature_spam_service.framework

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.tolyakulak.discordspammer.R
import com.github.tolyakulak.discordspammer.core.domain.model.Message
import com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api.DiscordUseCases
import com.github.tolyakulak.discordspammer.core.framework.MainActivity
import com.github.tolyakulak.discordspammer.core.framework.util.NotificationChannelsId
import com.github.tolyakulak.discordspammer.core.framework.util.ServiceExtras
import com.github.tolyakulak.discordspammer.core.framework.util.ServiceIntents
import com.github.tolyakulak.discordspammer.core.framework.util.ServiceIntents.START
import com.github.tolyakulak.discordspammer.core.framework.util.ServiceIntents.STOP
import com.github.tolyakulak.discordspammer.feature_spam_service.domain.util.ServiceUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Android service for spamming in discord channel
 *
 * [ServiceIntents.START] -> Start spamming process.
 * Must contain extras from [ServiceExtras]
 *
 * [ServiceIntents.STOP] -> Stop spamming process
 *
 */
@AndroidEntryPoint
class SpamService : Service() {
    @Inject
    lateinit var discordUseCases: DiscordUseCases

    private var job: Job? = null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * Starts a coroutine that spams in channel
     * @param token authorization token needed to send messages
     * @param channelId channel id you want send messages to
     * @param amount amount of messages to send
     * @return a [Job] object of this coroutine
     */
    private fun startSpam(token: String, channelId: String, amount: Int) =
        CoroutineScope(Dispatchers.Default).launch {
            if (token == "" || channelId == "" || amount == 0) return@launch

            val notification = NotificationCompat.Builder(
                this@SpamService, NotificationChannelsId.FOREGROUND_SERVICE
            ).setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(this@SpamService.getString(R.string.app_name)).addAction(
                    NotificationCompat.Action.Builder(
                        null,
                        this@SpamService.getString(R.string.cancel),
                        getStopServicePendingIntent()
                    ).build()
                ).setContentIntent(getSpamInfoPendingIntent())
                .setOnlyAlertOnce(true)

            var amountLeft = amount
            var errorsInARow = 0
            while (amountLeft > 0) {
                startForeground(
                    1, notification.setContentText(
                        this@SpamService.getString(R.string.spam_notification)
                            .format((progress.value * 100).roundToInt())
                    ).build()
                )

                val response: Result<Message> = discordUseCases.sendMessage(
                    token = token,
                    channelId = channelId,
                    message = ServiceUtils.getRandomMessage(),
                    replyToId = null
                )

                if (response.isFailure) {
                    when (val exc = response.exceptionOrNull()) {
                        is HttpException -> {
                            when (exc.code()) {
                                404, 401 -> {
                                    _progress.emit(-1f)
                                    return@launch
                                }
                            }
                        }
                    }
                    if (++errorsInARow >= 10) {
                        if (this@SpamService.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                            val exitNotification = NotificationCompat.Builder(
                                this@SpamService, NotificationChannelsId.FOREGROUND_SERVICE
                            ).setSmallIcon(R.mipmap.ic_launcher_foreground)
                                .setContentTitle(this@SpamService.getString(R.string.foreground_exit_error_title))
                                .setContentText(this@SpamService.getString(R.string.foreground_exit_error_content))
                                .setContentIntent(getMainActivityPendingIntent()).build()

                            with(NotificationManagerCompat.from(this@SpamService)) {
                                notify(2, exitNotification)
                            }
                        }
                        _progress.emit(-1f)
                        return@launch
                    }
                } else {
                    --amountLeft
                }

                _progress.emit((amount - amountLeft) / amount.toFloat())
            }
            stopSelf()
        }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            START -> {
                job?.cancel()
                _progress.tryEmit(0f)
                job = startSpam(
                    token = intent.extras?.getString(ServiceExtras.TOKEN) ?: "",
                    channelId = intent.extras?.getString(ServiceExtras.CHANNEL) ?: "",
                    amount = intent.extras?.getInt(ServiceExtras.AMOUNT) ?: 0
                )
            }

            STOP -> {
                job?.cancel()
                _progress.tryEmit(-1f)
                stopSelf()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * Get pending intent for opening Spam Progress Screen
     */
    private fun getSpamInfoPendingIntent() = PendingIntent.getActivity(
        this, 0, Intent(this, MainActivity::class.java).apply {
            this.action = ServiceIntents.OPEN_SPAM_PROGRESS_SCREEN
        }, PendingIntent.FLAG_IMMUTABLE
    )

    /**
     * Get pending intent for opening MainActivity
     */
    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        1,
        Intent(this, MainActivity::class.java),
        PendingIntent.FLAG_IMMUTABLE
    )

    /**
     * Get pending intent to stop service
     */
    private fun getStopServicePendingIntent() = PendingIntent.getService(
        this, 2, Intent(this, SpamService::class.java).apply {
            this.action = STOP
        }, PendingIntent.FLAG_IMMUTABLE
    )

    companion object {
        private var _progress = MutableStateFlow(-1f)

        /**
         * Progress in float from 0f to 1f.
         * -1f if there's no process running
         */
        val progress = _progress.asStateFlow()
    }
}
