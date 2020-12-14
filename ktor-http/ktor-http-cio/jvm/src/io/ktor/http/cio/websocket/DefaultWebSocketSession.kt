/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.http.cio.websocket

import io.ktor.util.*
import kotlinx.coroutines.*

/**
 * Create [DefaultWebSocketSession] from session.
 */
@OptIn(WebSocketInternalAPI::class)
public actual fun DefaultWebSocketSession(
    session: WebSocketSession,
    pingInterval: Long,
    timeoutMillis: Long
): DefaultWebSocketSession = DefaultWebSocketSessionImpl(
    session, pingInterval, timeoutMillis
)

/**
 * Default websocket session with ping-pong and timeout processing and built-in [closeReason] population
 */
public actual interface DefaultWebSocketSession : WebSocketSession {
    /**
     * Ping interval or `-1L` to disable pinger. Please note that pongs will be handled despite of this setting.
     */
    public var pingIntervalMillis: Long
    /**
     * A timeout to wait for pong reply to ping otherwise the session will be terminated immediately.
     * It doesn't have any effect if [pingIntervalMillis] is `-1` (pinger is disabled).
     */
    public var timeoutMillis: Long
    /**
     * A close reason for this session. It could be `null` if a session is terminated with no close reason
     * (for example due to connection failure).
     */
    public actual val closeReason: Deferred<CloseReason?>

    /**
     * Start WebSocket conversation.
     *
     * @param negotiatedExtensions specify negotiated extensions list to use in current session.
     */
    @InternalAPI
    @OptIn(ExperimentalWebSocketExtensionApi::class)
    public actual fun start(negotiatedExtensions: List<WebSocketExtension<*>>)
}
