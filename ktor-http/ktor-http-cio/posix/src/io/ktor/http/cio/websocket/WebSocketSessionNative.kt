/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.http.cio.websocket

import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

/**
 * Represents a web socket session between two peers
 */
public actual interface WebSocketSession : CoroutineScope {
    /**
     * Incoming frames channel
     */
    public actual val incoming: ReceiveChannel<Frame>

    /**
     * Outgoing frames channel. It could have limited capacity so sending too much frames may lead to suspension at
     * corresponding send invocations. It also may suspend if a peer doesn't read frames for some reason.
     */
    public actual val outgoing: SendChannel<Frame>

    /**
     * Enqueue frame, may suspend if outgoing queue is full. May throw an exception if outgoing channel is already
     * closed so it is impossible to transfer any message. Frames that were sent after close frame could be silently
     * ignored. Please note that close frame could be sent automatically in reply to a peer close frame unless it is
     * raw websocket session.
     */
    @Suppress("ACTUAL_WITHOUT_EXPECT")
    public suspend fun send(frame: Frame) {
        outgoing.send(frame)
    }

    /**
     * Flush all outstanding messages and suspend until all earlier sent messages will be written. Could be called
     * at any time even after close. May return immediately if the connection is already terminated.
     * However it may also fail with an exception (or cancellation) at any point due to session failure.
     * Please note that [flush] doesn't guarantee that frames were actually delivered.
     */
    public actual suspend fun flush()

    /**
     * Initiate connection termination immediately. Termination may complete asynchronously.
     */
    @Deprecated(
        "Use cancel() instead.",
        ReplaceWith("cancel()", "kotlinx.coroutines.cancel")
    )
    public actual fun terminate()

    /**
     * Specifies frame size limit. Connection will be closed if violated
     */
    public actual var maxFrameSize: Long

    /**
     * Negotiated WebSocket extensions.
     */
    @ExperimentalWebSocketExtensionApi
    public actual val extensions: List<WebSocketExtension<*>>
}
