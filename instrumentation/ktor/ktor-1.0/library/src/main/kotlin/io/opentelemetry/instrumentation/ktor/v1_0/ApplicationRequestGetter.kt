/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.instrumentation.ktor.v1_0

import io.ktor.request.*
import io.opentelemetry.context.propagation.TextMapGetter

internal object ApplicationRequestGetter : TextMapGetter<ApplicationRequest> {
  override fun keys(carrier: ApplicationRequest): Iterable<String> = carrier.headers.names()

  override fun get(carrier: ApplicationRequest?, name: String): String? = carrier?.headers?.get(name)
}
