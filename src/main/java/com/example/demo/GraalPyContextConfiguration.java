package com.example.demo;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.python.embedding.GraalPyResources;
import org.graalvm.python.embedding.VirtualFileSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

@Configuration
public class GraalPyContextConfiguration {
    private static final String LANGUAGE_ID = "python";

    /*
     * Make GraalPy context available for injection as a Spring bean.
     */
    @Bean(destroyMethod = "close")
    public GraalPyContext graalPyContext() throws IOException {
        var vfs = VirtualFileSystem.newBuilder()
                .resourceLoadingClass(GraalPyContextConfiguration.class)
                .build();
        var tmpdir = Files.createTempDirectory("graalpy-vfs");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try (Stream<Path> paths = Files.walk(tmpdir)) {
                paths.sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                // ignore
                            }
                        });
            } catch (IOException e) {
                // ignore
            }
        }));
        GraalPyResources.extractVirtualFileSystemResources(vfs, tmpdir);
        return new GraalPyContext(GraalPyResources.contextBuilder(tmpdir)
                .option("python.WarnExperimentalFeatures", "false")
                .allowAllAccess(true)
                .build());
    }

    public record GraalPyContext(Context context) {
        public Value eval(String source) {
            return context.eval(LANGUAGE_ID, source);
        }

        public <T> T getFunction(String name, Class<T> targetType) {
            return context.getBindings(LANGUAGE_ID).getMember(name).as(targetType);
        }

        public void close() {
            context.close(true);
        }
    }
}
