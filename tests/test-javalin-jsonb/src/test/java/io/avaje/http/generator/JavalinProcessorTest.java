package io.avaje.http.generator;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.avaje.http.generator.javalin.JavalinProcessor;
import io.avaje.jsonb.generator.Processor;

class JavalinProcessorTest {

  @AfterEach
  void deleteGeneratedFiles() throws IOException {

    Paths.get("openapi.json").toAbsolutePath().toFile().delete();
    Paths.get("io.avaje.jsonb.Jsonb$GeneratedComponent").toAbsolutePath().toFile().delete();

    Files.walk(Paths.get("org").toAbsolutePath())
        .sorted(Comparator.reverseOrder())
        .map(Path::toFile)
        .forEach(File::delete);
  }

  @Test
  public void runAnnoationProcessor() throws Exception {
    final var source = Paths.get("src").toAbsolutePath().toString();

    final var files = getSourceFiles(source);

    final var compiler = ToolProvider.getSystemJavaCompiler();

    final var task =
        compiler.getTask(
            new PrintWriter(System.out), null, null, List.of("--release=11"), null, files);
    task.setProcessors(List.of(new JavalinProcessor()));

    assertThat(task.call()).isTrue();
  }

  @Test
  public void runAnnoationProcessorJsonB() throws Exception {
    final var source = Paths.get("src").toAbsolutePath().toString();

    final var files = getSourceFiles(source);

    final var compiler = ToolProvider.getSystemJavaCompiler();

    final var task =
        compiler.getTask(
            new PrintWriter(System.out), null, null, List.of("--release=11"), null, files);
    task.setProcessors(List.of(new JavalinProcessor(false), new Processor()));

    assertThat(task.call()).isTrue();
  }

  @Test
  public void testOpenAPIGeneration() throws Exception {
    final var source = Paths.get("src").toAbsolutePath().toString();
    // OpenAPIController
    final var files = getSourceFiles(source);

    Iterable<JavaFileObject> openAPIController = null;
    for (final var file : files) {
      if (file.isNameCompatible("OpenAPIController", Kind.SOURCE))
        openAPIController = List.of(file);
    }
    final var compiler = ToolProvider.getSystemJavaCompiler();

    final var task =
        compiler.getTask(
            new PrintWriter(System.out),
            null,
            null,
            List.of("--release=11"),
            null,
            openAPIController);
    task.setProcessors(List.of(new JavalinProcessor(false), new Processor()));

    assertThat(task.call()).isTrue();

    final var mapper = new ObjectMapper();
    final var expectedOpenApiJson =
        mapper.readTree(new File("src/test/java/io/avaje/http/generator/openapi.json"));
    final var generatedOpenApi = mapper.readTree(new File("openapi.json"));

    assert expectedOpenApiJson.equals(generatedOpenApi);
  }

  private Iterable<JavaFileObject> getSourceFiles(String source) throws Exception {
    final var compiler = ToolProvider.getSystemJavaCompiler();
    final var files = compiler.getStandardFileManager(null, null, null);

    files.setLocation(StandardLocation.SOURCE_PATH, List.of(new File(source)));

    final Set<Kind> fileKinds = Collections.singleton(Kind.SOURCE);
    return files.list(StandardLocation.SOURCE_PATH, "", fileKinds, true);
  }
}
