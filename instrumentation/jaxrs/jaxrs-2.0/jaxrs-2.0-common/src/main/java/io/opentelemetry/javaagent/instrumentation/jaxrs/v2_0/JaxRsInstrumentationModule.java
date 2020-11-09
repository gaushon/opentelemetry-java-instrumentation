/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.javaagent.instrumentation.jaxrs.v2_0;

import static io.opentelemetry.javaagent.tooling.ClassLoaderMatcher.hasClassesNamed;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;

import com.google.auto.service.AutoService;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.javaagent.tooling.InstrumentationModule;
import io.opentelemetry.javaagent.tooling.TypeInstrumentation;
import java.util.List;
import java.util.Map;
import net.bytebuddy.matcher.ElementMatcher;

@AutoService(InstrumentationModule.class)
public class JaxRsInstrumentationModule extends InstrumentationModule {
  public JaxRsInstrumentationModule() {
    super("jax-rs", "jaxrs");
  }

  // require jax-rs 2
  @Override
  public ElementMatcher.Junction<ClassLoader> classLoaderMatcher() {
    return hasClassesNamed("javax.ws.rs.container.AsyncResponse");
  }

  @Override
  public String[] helperClassNames() {
    return new String[] {
      "io.opentelemetry.javaagent.tooling.ClassHierarchyIterable",
      "io.opentelemetry.javaagent.tooling.ClassHierarchyIterable$ClassIterator",
      packageName + ".JaxRsAnnotationsTracer",
      packageName + ".CompletionStageFinishCallback",
      packageName + ".RequestContextHelper"
    };
  }

  @Override
  public List<TypeInstrumentation> typeInstrumentations() {
    return asList(
        new ContainerRequestFilterInstrumentation(),
        new DefaultRequestContextInstrumentation(),
        new JaxRsAnnotationsInstrumentation(),
        new JaxRsAsyncResponseInstrumentation());
  }

  @Override
  public Map<String, String> contextStore() {
    return singletonMap("javax.ws.rs.container.AsyncResponse", Span.class.getName());
  }
}
