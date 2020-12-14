/*
 * Copyright 2014-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.client.tests

import io.ktor.client.features.websocket.*
import io.ktor.client.tests.utils.*
import io.ktor.http.cio.websocket.*
import kotlin.test.*
import kotlin.test.Test

private const val TEST_SIZE: Int = 100

class WebSocketJvmTest : ClientLoader(100000) {

    @OptIn(ExperimentalWebSocketExtensionApi::class)
    @Test
    fun testWebSocketDeflateBinary() = clientTests(ENGINES_WITHOUT_WS_EXTENSIONS) {
        config {
            WebSockets {
                extensions {
                    install(WebSocketDeflateExtension)
                }
            }
        }

        test { client ->
            client.webSocket("$TEST_WEBSOCKET_SERVER/websockets/echo") {
                repeat(TEST_SIZE) { size ->
                    val data = generateRandomByteArray(size, size * 10 + 1)
                    send(Frame.Binary(fin = true, data))

                    val actual = incoming.receive()
                    assertTrue(actual is Frame.Binary)
                    assertTrue { data.contentEquals(actual.data) }
                }
            }
        }
    }
}
