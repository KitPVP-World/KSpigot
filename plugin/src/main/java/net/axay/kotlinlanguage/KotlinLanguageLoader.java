package net.axay.kotlinlanguage;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

/**
 * Loading kotlin on paper
 */
public class KotlinLanguageLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        var resolver = new MavenLibraryResolver();
        kotlinx("serialization-json-jvm", "1.5.1", resolver);
        kotlinx("coroutines-jdk8", "1.7.1", resolver);
        kotlinx("coroutines-core-jvm", "1.7.1", resolver);
        kotlin("stdlib-jdk8", "1.8.22", resolver);
        resolver.addRepository(new RemoteRepository.Builder("mavencentral", "default", "https://repo.maven.apache.org/maven2/").build());

        classpathBuilder.addLibrary(resolver);
    }

    private void kotlin(String module, String version, MavenLibraryResolver resolver) {
        String coords = "org.jetbrains.kotlin:kotlin-" + module + ":" + version;

        resolver.addDependency(new Dependency(new DefaultArtifact(coords), null));
    }

    private void kotlinx(String module, String version, MavenLibraryResolver resolver) {
        String coords = "org.jetbrains.kotlinx:kotlinx-" + module + ":" + version;

        resolver.addDependency(new Dependency(new DefaultArtifact(coords), null));
    }
}
