/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.client.tests.utils

import io.ktor.client.engine.*
import kotlin.jvm.*

/**
 * Helper interface to test client.
 */
public expect abstract class ClientLoader(timeoutSeconds: Int = 60) {
    /**
     * Perform test against all clients from dependencies.
     */
    public fun clientTests(
        skipEngines: List<String> = emptyList(),
        block: suspend TestClientBuilder<HttpClientEngineConfig>.() -> Unit
    )

    /**
     * Print coroutines in debug mode.
     */
    public fun dumpCoroutines()
}
