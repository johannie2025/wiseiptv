Run ./gradlew :tv:assembleDebug \
  ./gradlew :tv:assembleDebug \
    --no-daemon \
    --stacktrace \
    --info \
    2>&1 | tee build-tv-debug.log
  echo "════════════ ERREURS DE COMPILATION ════════════"
  grep -E "^(e:|error:|FAILED|Exception|Caused by:|> Task|BUILD)" build-tv-debug.log \
    | grep -v "^> Task :.*UP-TO-DATE" \
    | grep -v "^> Task :.*SKIPPED" \
    | head -100 || echo "Aucune erreur détectée"
  echo "═════════════════════════════════════════════════"
  grep -q "BUILD SUCCESSFUL" build-tv-debug.log || exit 1
  shell: /usr/bin/bash -e {0}
  env:
    KEYSTORE_BASE64: 
    JAVA_HOME: /opt/hostedtoolcache/Java_Temurin-Hotspot_jdk/17.0.19-10/x64
    JAVA_HOME_17_X64: /opt/hostedtoolcache/Java_Temurin-Hotspot_jdk/17.0.19-10/x64
Initialized native services in: /home/runner/.gradle/native
Initialized jansi services in: /home/runner/.gradle/native

Welcome to Gradle 8.6!

Here are the highlights of this release:
 - Configurable encryption key for configuration cache
 - Build init improvements
 - Build authoring improvements

For more details see https://docs.gradle.org/8.6/release-notes.html

Received JVM installation metadata from '/usr/lib/jvm/temurin-17-jdk-amd64': {JAVA_HOME=/usr/lib/jvm/temurin-17-jdk-amd64, JAVA_VERSION=17.0.19, JAVA_VENDOR=Eclipse Adoptium, RUNTIME_NAME=OpenJDK Runtime Environment, RUNTIME_VERSION=17.0.19+10, VM_NAME=OpenJDK 64-Bit Server VM, VM_VERSION=17.0.19+10, VM_VENDOR=Eclipse Adoptium, OS_ARCH=amd64}
Checking if the launcher JVM can be re-used for build. To be re-used, the launcher JVM needs to match the parameters required for the build process: --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.prefs/java.util.prefs=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED -Xmx2048m -Dfile.encoding=UTF-8 -Duser.country -Duser.language=en -Duser.variant
To honour the JVM settings for this build a single-use Daemon process will be forked. For more on this, please refer to https://docs.gradle.org/8.6/userguide/gradle_daemon.html#sec:disabling_the_daemon in the Gradle documentation.
Starting process 'Gradle build daemon'. Working directory: /home/runner/.gradle/daemon/8.6 Command: /usr/lib/jvm/temurin-17-jdk-amd64/bin/java --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.prefs/java.util.prefs=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED -Xmx2048m -Dfile.encoding=UTF-8 -Duser.country -Duser.language=en -Duser.variant -cp /home/runner/.gradle/wrapper/dists/gradle-8.6-bin/afr5mpiioh2wthjmwnkmdsd5w/gradle-8.6/lib/gradle-launcher-8.6.jar -javaagent:/home/runner/.gradle/wrapper/dists/gradle-8.6-bin/afr5mpiioh2wthjmwnkmdsd5w/gradle-8.6/lib/agents/gradle-instrumentation-agent-8.6.jar org.gradle.launcher.daemon.bootstrap.GradleDaemon 8.6
Successfully started process 'Gradle build daemon'
An attempt to start the daemon took 0.957 secs.
The client will now receive all logging from the daemon (pid: 2309). The daemon log file: /home/runner/.gradle/daemon/8.6/daemon-2309.out.log
Closing daemon's stdin at end of input.
The daemon will no longer process any standard input.
Daemon will be stopped at the end of the build 
Using 4 worker leases.
Received JVM installation metadata from '/usr/lib/jvm/temurin-17-jdk-amd64': {JAVA_HOME=/usr/lib/jvm/temurin-17-jdk-amd64, JAVA_VERSION=17.0.19, JAVA_VENDOR=Eclipse Adoptium, RUNTIME_NAME=OpenJDK Runtime Environment, RUNTIME_VERSION=17.0.19+10, VM_NAME=OpenJDK 64-Bit Server VM, VM_VERSION=17.0.19+10, VM_VENDOR=Eclipse Adoptium, OS_ARCH=amd64}
Watching the file system is configured to be enabled if available
Now considering [/home/runner/work/wiseiptv/wiseiptv] as hierarchies to watch
File system watching is active
Starting Build
Settings evaluated using settings file '/home/runner/work/wiseiptv/wiseiptv/settings.gradle'.
Projects loaded. Root project using build file '/home/runner/work/wiseiptv/wiseiptv/build.gradle'.
Included projects: [root project 'WiseIPTV', project ':core', project ':mobile', project ':tv']

> Configure project :
Evaluating root project 'WiseIPTV' using build file '/home/runner/work/wiseiptv/wiseiptv/build.gradle'.
Transforming gradle-settings-api-8.3.0.jar (com.android.tools.build:gradle-settings-api:8.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming builder-8.3.0.jar (com.android.tools.build:builder:8.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming lint-model-31.3.0.jar (com.android.tools.lint:lint-model:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming gradle-8.3.0.jar (com.android.tools.build:gradle:8.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming manifest-merger-31.3.0.jar (com.android.tools.build:manifest-merger:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming sdk-common-31.3.0.jar (com.android.tools:sdk-common:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming sdklib-31.3.0.jar (com.android.tools:sdklib:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming repository-31.3.0.jar (com.android.tools:repository:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming gradle-api-8.3.0.jar (com.android.tools.build:gradle-api:8.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming builder-test-api-8.3.0.jar (com.android.tools.build:builder-test-api:8.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming ddmlib-31.3.0.jar (com.android.tools.ddms:ddmlib:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming aaptcompiler-8.3.0.jar (com.android.tools.build:aaptcompiler:8.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming bundletool-1.15.6.jar (com.android.tools.build:bundletool:1.15.6) with ExternalDependencyInstrumentingArtifactTransform
Transforming aapt2-proto-8.3.0-10880808.jar (com.android.tools.build:aapt2-proto:8.3.0-10880808) with ExternalDependencyInstrumentingArtifactTransform
Transforming crash-31.3.0.jar (com.android.tools.analytics-library:crash:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming tracker-31.3.0.jar (com.android.tools.analytics-library:tracker:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming shared-31.3.0.jar (com.android.tools.analytics-library:shared:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming lint-typedef-remover-31.3.0.jar (com.android.tools.lint:lint-typedef-remover:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming databinding-compiler-common-8.3.0.jar (androidx.databinding:databinding-compiler-common:8.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming databinding-common-8.3.0.jar (androidx.databinding:databinding-common:8.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming baseLibrary-8.3.0.jar (com.android.databinding:baseLibrary:8.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming layoutlib-api-31.3.0.jar (com.android.tools.layoutlib:layoutlib-api:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming android-device-provider-ddmlib-proto-31.3.0.jar (com.android.tools.utp:android-device-provider-ddmlib-proto:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming android-device-provider-gradle-proto-31.3.0.jar (com.android.tools.utp:android-device-provider-gradle-proto:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming android-test-plugin-host-additional-test-output-proto-31.3.0.jar (com.android.tools.utp:android-test-plugin-host-additional-test-output-proto:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming android-test-plugin-host-coverage-proto-31.3.0.jar (com.android.tools.utp:android-test-plugin-host-coverage-proto:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming android-test-plugin-host-emulator-control-proto-31.3.0.jar (com.android.tools.utp:android-test-plugin-host-emulator-control-proto:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming android-test-plugin-host-logcat-proto-31.3.0.jar (com.android.tools.utp:android-test-plugin-host-logcat-proto:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming android-test-plugin-host-apk-installer-proto-31.3.0.jar (com.android.tools.utp:android-test-plugin-host-apk-installer-proto:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming android-test-plugin-host-retention-proto-31.3.0.jar (com.android.tools.utp:android-test-plugin-host-retention-proto:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming android-test-plugin-result-listener-gradle-proto-31.3.0.jar (com.android.tools.utp:android-test-plugin-result-listener-gradle-proto:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming builder-model-8.3.0.jar (com.android.tools.build:builder-model:8.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming dvlib-31.3.0.jar (com.android.tools:dvlib:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming common-31.3.0.jar (com.android.tools:common:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming jetifier-processor-1.0.0-beta10.jar (com.android.tools.build.jetifier:jetifier-processor:1.0.0-beta10) with ExternalDependencyInstrumentingArtifactTransform
Transforming kotlin-reflect-1.9.20.jar (org.jetbrains.kotlin:kotlin-reflect:1.9.20) with ExternalDependencyInstrumentingArtifactTransform
Transforming jetifier-core-1.0.0-beta10.jar (com.android.tools.build.jetifier:jetifier-core:1.0.0-beta10) with ExternalDependencyInstrumentingArtifactTransform
Transforming kotlin-stdlib-jdk7-1.9.20.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.20) with ExternalDependencyInstrumentingArtifactTransform
Transforming kotlin-stdlib-1.9.20.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.20) with ExternalDependencyInstrumentingArtifactTransform
Transforming kotlin-stdlib-jdk8-1.9.20.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20) with ExternalDependencyInstrumentingArtifactTransform
Transforming transform-api-2.0.0-deprecated-use-gradle-api.jar (com.android.tools.build:transform-api:2.0.0-deprecated-use-gradle-api) with ExternalDependencyInstrumentingArtifactTransform
Transforming httpmime-4.5.6.jar (org.apache.httpcomponents:httpmime:4.5.6) with ExternalDependencyInstrumentingArtifactTransform
Transforming commons-io-2.13.0.jar (commons-io:commons-io:2.13.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming asm-commons-9.6.jar (org.ow2.asm:asm-commons:9.6) with ExternalDependencyInstrumentingArtifactTransform
Transforming asm-analysis-9.6.jar (org.ow2.asm:asm-analysis:9.6) with ExternalDependencyInstrumentingArtifactTransform
Transforming asm-util-9.6.jar (org.ow2.asm:asm-util:9.6) with ExternalDependencyInstrumentingArtifactTransform
Transforming asm-tree-9.6.jar (org.ow2.asm:asm-tree:9.6) with ExternalDependencyInstrumentingArtifactTransform
Transforming asm-9.6.jar (org.ow2.asm:asm:9.6) with ExternalDependencyInstrumentingArtifactTransform
Transforming apkzlib-8.3.0.jar (com.android.tools.build:apkzlib:8.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming bcpkix-jdk15on-1.67.jar (org.bouncycastle:bcpkix-jdk15on:1.67) with ExternalDependencyInstrumentingArtifactTransform
Transforming jaxb-runtime-2.3.2.jar (org.glassfish.jaxb:jaxb-runtime:2.3.2) with ExternalDependencyInstrumentingArtifactTransform
Transforming jopt-simple-4.9.jar (net.sf.jopt-simple:jopt-simple:4.9) with ExternalDependencyInstrumentingArtifactTransform
Transforming javapoet-1.10.0.jar (com.squareup:javapoet:1.10.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming protobuf-java-util-3.22.3.jar (com.google.protobuf:protobuf-java-util:3.22.3) with ExternalDependencyInstrumentingArtifactTransform
Transforming grpc-protobuf-1.57.0.jar (io.grpc:grpc-protobuf:1.57.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming tink-1.7.0.jar (com.google.crypto.tink:tink:1.7.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming protos-31.3.0.jar (com.android.tools.analytics-library:protos:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming proto-google-common-protos-2.17.0.jar (com.google.api.grpc:proto-google-common-protos:2.17.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming protobuf-java-3.22.3.jar (com.google.protobuf:protobuf-java:3.22.3) with ExternalDependencyInstrumentingArtifactTransform
Transforming grpc-netty-1.57.0.jar (io.grpc:grpc-netty:1.57.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming grpc-core-1.57.0.jar (io.grpc:grpc-core:1.57.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming gson-2.10.1.jar (com.google.code.gson:gson:2.10.1) with ExternalDependencyInstrumentingArtifactTransform
Transforming grpc-stub-1.57.0.jar (io.grpc:grpc-stub:1.57.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming core-proto-0.0.9-alpha02.jar (com.google.testing.platform:core-proto:0.0.9-alpha02) with ExternalDependencyInstrumentingArtifactTransform
Transforming kxml2-2.3.0.jar (net.sf.kxml:kxml2:2.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming tensorflow-lite-metadata-0.1.0-rc2.jar (org.tensorflow:tensorflow-lite-metadata:0.1.0-rc2) with ExternalDependencyInstrumentingArtifactTransform
Transforming flatbuffers-java-1.12.0.jar (com.google.flatbuffers:flatbuffers-java:1.12.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming jimfs-1.1.jar (com.google.jimfs:jimfs:1.1) with ExternalDependencyInstrumentingArtifactTransform
Transforming grpc-context-1.57.0.jar (io.grpc:grpc-context:1.57.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming grpc-protobuf-lite-1.57.0.jar (io.grpc:grpc-protobuf-lite:1.57.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming guava-32.0.1-jre.jar (com.google.guava:guava:32.0.1-jre) with ExternalDependencyInstrumentingArtifactTransform
Transforming grpc-api-1.57.0.jar (io.grpc:grpc-api:1.57.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming javax.inject-1.jar (javax.inject:javax.inject:1) with ExternalDependencyInstrumentingArtifactTransform
Transforming dagger-2.28.3.jar (com.google.dagger:dagger:2.28.3) with ExternalDependencyInstrumentingArtifactTransform
Transforming bcprov-jdk15on-1.67.jar (org.bouncycastle:bcprov-jdk15on:1.67) with ExternalDependencyInstrumentingArtifactTransform
Transforming trove4j-1.0.20200330.jar (org.jetbrains.intellij.deps:trove4j:1.0.20200330) with ExternalDependencyInstrumentingArtifactTransform
Transforming commons-compress-1.21.jar (org.apache.commons:commons-compress:1.21) with ExternalDependencyInstrumentingArtifactTransform
Transforming httpclient-4.5.14.jar (org.apache.httpcomponents:httpclient:4.5.14) with ExternalDependencyInstrumentingArtifactTransform
Transforming javax.activation-1.2.0.jar (com.sun.activation:javax.activation:1.2.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming httpcore-4.4.16.jar (org.apache.httpcomponents:httpcore:4.4.16) with ExternalDependencyInstrumentingArtifactTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming signflinger-8.3.0.jar (com.android:signflinger:8.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming zipflinger-8.3.0.jar (com.android:zipflinger:8.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming annotations-31.3.0.jar (com.android.tools:annotations:31.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming jna-platform-5.6.0.jar (net.java.dev.jna:jna-platform:5.6.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming juniversalchardet-1.0.3.jar (com.googlecode.juniversalchardet:juniversalchardet:1.0.3) with ExternalDependencyInstrumentingArtifactTransform
Transforming javax.annotation-api-1.3.2.jar (javax.annotation:javax.annotation-api:1.3.2) with ExternalDependencyInstrumentingArtifactTransform
Transforming stax-ex-1.8.1.jar (org.jvnet.staxex:stax-ex:1.8.1) with ExternalDependencyInstrumentingArtifactTransform
Transforming jakarta.xml.bind-api-2.3.2.jar (jakarta.xml.bind:jakarta.xml.bind-api:2.3.2) with ExternalDependencyInstrumentingArtifactTransform
Transforming txw2-2.3.2.jar (org.glassfish.jaxb:txw2:2.3.2) with ExternalDependencyInstrumentingArtifactTransform
Transforming istack-commons-runtime-3.0.8.jar (com.sun.istack:istack-commons-runtime:3.0.8) with ExternalDependencyInstrumentingArtifactTransform
Transforming FastInfoset-1.2.16.jar (com.sun.xml.fastinfoset:FastInfoset:1.2.16) with ExternalDependencyInstrumentingArtifactTransform
Transforming error_prone_annotations-2.18.0.jar (com.google.errorprone:error_prone_annotations:2.18.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming jakarta.activation-api-1.2.1.jar (jakarta.activation:jakarta.activation-api:1.2.1) with ExternalDependencyInstrumentingArtifactTransform
Transforming auto-value-annotations-1.6.2.jar (com.google.auto.value:auto-value-annotations:1.6.2) with ExternalDependencyInstrumentingArtifactTransform
Transforming jose4j-0.7.0.jar (org.bitbucket.b_c:jose4j:0.7.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming slf4j-api-1.7.30.jar (org.slf4j:slf4j-api:1.7.30) with ExternalDependencyInstrumentingArtifactTransform
Transforming jdom2-2.0.6.jar (org.jdom:jdom2:2.0.6) with ExternalDependencyInstrumentingArtifactTransform
Transforming jsr305-3.0.2.jar (com.google.code.findbugs:jsr305:3.0.2) with ExternalDependencyInstrumentingArtifactTransform
Transforming j2objc-annotations-2.8.jar (com.google.j2objc:j2objc-annotations:2.8) with ExternalDependencyInstrumentingArtifactTransform
Transforming annotations-4.1.1.4.jar (com.google.android:annotations:4.1.1.4) with ExternalDependencyInstrumentingArtifactTransform
Transforming perfmark-api-0.26.0.jar (io.perfmark:perfmark-api:0.26.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming animal-sniffer-annotations-1.23.jar (org.codehaus.mojo:animal-sniffer-annotations:1.23) with ExternalDependencyInstrumentingArtifactTransform
Transforming netty-handler-proxy-4.1.93.Final.jar (io.netty:netty-handler-proxy:4.1.93.Final) with ExternalDependencyInstrumentingArtifactTransform
Transforming netty-codec-http2-4.1.93.Final.jar (io.netty:netty-codec-http2:4.1.93.Final) with ExternalDependencyInstrumentingArtifactTransform
Transforming netty-codec-http-4.1.93.Final.jar (io.netty:netty-codec-http:4.1.93.Final) with ExternalDependencyInstrumentingArtifactTransform
Transforming netty-handler-4.1.93.Final.jar (io.netty:netty-handler:4.1.93.Final) with ExternalDependencyInstrumentingArtifactTransform
Transforming checker-qual-3.33.0.jar (org.checkerframework:checker-qual:3.33.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming netty-transport-native-unix-common-4.1.93.Final.jar (io.netty:netty-transport-native-unix-common:4.1.93.Final) with ExternalDependencyInstrumentingArtifactTransform
Transforming commons-codec-1.11.jar (commons-codec:commons-codec:1.11) with ExternalDependencyInstrumentingArtifactTransform
Transforming apksig-8.3.0.jar (com.android.tools.build:apksig:8.3.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming javawriter-2.5.0.jar (com.squareup:javawriter:2.5.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with ExternalDependencyInstrumentingArtifactTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with ExternalDependencyInstrumentingArtifactTransform
Transforming commons-logging-1.2.jar (commons-logging:commons-logging:1.2) with ExternalDependencyInstrumentingArtifactTransform
Transforming netty-codec-socks-4.1.93.Final.jar (io.netty:netty-codec-socks:4.1.93.Final) with ExternalDependencyInstrumentingArtifactTransform
Transforming jna-5.6.0.jar (net.java.dev.jna:jna:5.6.0) with ExternalDependencyInstrumentingArtifactTransform
Transforming netty-codec-4.1.93.Final.jar (io.netty:netty-codec:4.1.93.Final) with ExternalDependencyInstrumentingArtifactTransform
Transforming netty-transport-4.1.93.Final.jar (io.netty:netty-transport:4.1.93.Final) with ExternalDependencyInstrumentingArtifactTransform
Transforming netty-buffer-4.1.93.Final.jar (io.netty:netty-buffer:4.1.93.Final) with ExternalDependencyInstrumentingArtifactTransform
Transforming netty-resolver-4.1.93.Final.jar (io.netty:netty-resolver:4.1.93.Final) with ExternalDependencyInstrumentingArtifactTransform
Transforming netty-common-4.1.93.Final.jar (io.netty:netty-common:4.1.93.Final) with ExternalDependencyInstrumentingArtifactTransform

> Configure project :core
Evaluating project ':core' using build file '/home/runner/work/wiseiptv/wiseiptv/core/build.gradle'.
Using default execution profile

> Configure project :mobile
Evaluating project ':mobile' using build file '/home/runner/work/wiseiptv/wiseiptv/mobile/build.gradle'.
Using default execution profile

> Configure project :tv
Evaluating project ':tv' using build file '/home/runner/work/wiseiptv/wiseiptv/tv/build.gradle'.
Using default execution profile
All projects evaluated.
Task path ':tv:assembleDebug' matched project ':tv'
Task name matched 'assembleDebug'
Selected primary task 'assembleDebug' from project :tv
Tasks to be executed: [task ':tv:preBuild', task ':tv:preDebugBuild', task ':tv:mergeDebugNativeDebugMetadata', task ':core:preBuild', task ':core:preDebugBuild', task ':core:generateDebugResValues', task ':core:generateDebugResources', task ':core:packageDebugResources', task ':core:parseDebugLocalResources', task ':core:generateDebugRFile', task ':core:javaPreCompileDebug', task ':core:compileDebugJavaWithJavac', task ':core:bundleLibCompileToJarDebug', task ':tv:javaPreCompileDebug', task ':core:compileDebugLibraryResources', task ':core:writeDebugAarMetadata', task ':tv:checkDebugAarMetadata', task ':tv:generateDebugResValues', task ':tv:mapDebugSourceSetPaths', task ':tv:generateDebugResources', task ':tv:mergeDebugResources', task ':tv:packageDebugResources', task ':tv:parseDebugLocalResources', task ':tv:createDebugCompatibleScreenManifests', task ':core:extractDeepLinksDebug', task ':core:processDebugManifest', task ':tv:extractDeepLinksDebug', task ':tv:processDebugMainManifest', task ':tv:processDebugManifest', task ':tv:processDebugManifestForPackage', task ':tv:processDebugResources', task ':tv:compileDebugJavaWithJavac', task ':core:mergeDebugShaders', task ':core:compileDebugShaders', task ':core:generateDebugAssets', task ':core:packageDebugAssets', task ':tv:mergeDebugShaders', task ':tv:compileDebugShaders', task ':tv:generateDebugAssets', task ':tv:mergeDebugAssets', task ':tv:compressDebugAssets', task ':core:bundleLibRuntimeToJarDebug', task ':tv:desugarDebugFileDependencies', task ':tv:dexBuilderDebug', task ':tv:mergeDebugGlobalSynthetics', task ':core:processDebugJavaRes', task ':tv:processDebugJavaRes', task ':tv:mergeDebugJavaResource', task ':tv:checkDebugDuplicateClasses', task ':tv:mergeExtDexDebug', task ':tv:mergeLibDexDebug', task ':tv:mergeProjectDexDebug', task ':core:mergeDebugJniLibFolders', task ':core:mergeDebugNativeLibs', task ':core:copyDebugJniLibsProjectOnly', task ':tv:mergeDebugJniLibFolders', task ':tv:mergeDebugNativeLibs', task ':tv:stripDebugDebugSymbols', task ':tv:validateSigningDebug', task ':tv:writeDebugAppMetadata', task ':tv:writeDebugSigningConfigVersions', task ':tv:packageDebug', task ':tv:createDebugApkListingFileRedirect', task ':tv:assembleDebug']
Tasks that were excluded: []
work action org.gradle.api.internal.artifacts.transform.DefaultTransformUpstreamDependenciesResolver$FinalizeTransformDependenciesFromSelectedArtifacts$CalculateFinalDependencies@1c4bdb3d (Thread[Execution worker,5,main]) started.
Downloading https://dl.google.com/dl/android/maven2/androidx/leanback/leanback/1.0.0/leanback-1.0.0.pom to /home/runner/.gradle/.tmp/gradle_download12997531261650770176bin
Downloading https://dl.google.com/dl/android/maven2/androidx/constraintlayout/constraintlayout/2.0.1/constraintlayout-2.0.1.pom to /home/runner/.gradle/.tmp/gradle_download1452051184250934712bin
Downloading https://dl.google.com/dl/android/maven2/androidx/legacy/legacy-support-core-ui/1.0.0/legacy-support-core-ui-1.0.0.pom to /home/runner/.gradle/.tmp/gradle_download5547935596633071534bin
work action Parameters of DexingWithClasspathTransform (Thread[included builds,5,main]) started.
Downloading https://dl.google.com/dl/android/maven2/androidx/media/media/1.0.0/media-1.0.0.pom to /home/runner/.gradle/.tmp/gradle_download1088116121349626436bin
work action Parameters of DexingOutputSplitTransform (Thread[included builds,5,main]) started.
work action Parameters of DexingOutputSplitTransform (Thread[Execution worker Thread 2,5,main]) started.
Resolve mutations for :tv:preBuild (Thread[Execution worker Thread 2,5,main]) started.
:tv:preBuild (Thread[Execution worker Thread 2,5,main]) started.

> Task :tv:preBuild UP-TO-DATE
Skipping task ':tv:preBuild' as it has no actions.
Resolve mutations for :tv:preDebugBuild (Thread[Execution worker Thread 2,5,main]) started.
:tv:preDebugBuild (Thread[included builds,5,main]) started.

> Task :tv:preDebugBuild UP-TO-DATE
Skipping task ':tv:preDebugBuild' as it has no actions.
Resolve mutations for :tv:mergeDebugNativeDebugMetadata (Thread[Execution worker Thread 2,5,main]) started.
:tv:mergeDebugNativeDebugMetadata (Thread[Execution worker Thread 2,5,main]) started.

> Task :tv:mergeDebugNativeDebugMetadata NO-SOURCE
Skipping task ':tv:mergeDebugNativeDebugMetadata' as it has no source files and no previous output files.
Resolve mutations for :core:preBuild (Thread[Execution worker Thread 2,5,main]) started.
:core:preBuild (Thread[Execution worker Thread 2,5,main]) started.

> Task :core:preBuild UP-TO-DATE
Skipping task ':core:preBuild' as it has no actions.
Resolve mutations for :core:preDebugBuild (Thread[Execution worker Thread 2,5,main]) started.
:core:preDebugBuild (Thread[Execution worker Thread 2,5,main]) started.

> Task :core:preDebugBuild UP-TO-DATE
Skipping task ':core:preDebugBuild' as it has no actions.
Resolve mutations for :core:generateDebugResValues (Thread[Execution worker Thread 2,5,main]) started.
:core:generateDebugResValues (Thread[Execution worker Thread 2,5,main]) started.

> Task :core:generateDebugResValues
Caching disabled for task ':core:generateDebugResValues' because:
  Build cache is disabled
Task ':core:generateDebugResValues' is not up-to-date because:
  No history is available.
Downloading https://dl.google.com/dl/android/maven2/androidx/constraintlayout/constraintlayout-solver/2.0.1/constraintlayout-solver-2.0.1.pom to /home/runner/.gradle/.tmp/gradle_download10443395519925640248bin
Downloading https://dl.google.com/dl/android/maven2/androidx/swiperefreshlayout/swiperefreshlayout/1.0.0/swiperefreshlayout-1.0.0.pom to /home/runner/.gradle/.tmp/gradle_download14494928584052607009bin
Downloading https://dl.google.com/dl/android/maven2/androidx/slidingpanelayout/slidingpanelayout/1.0.0/slidingpanelayout-1.0.0.pom to /home/runner/.gradle/.tmp/gradle_download9876661649854965015bin
Resolve mutations for :core:generateDebugResources (Thread[Execution worker Thread 2,5,main]) started.
:core:generateDebugResources (Thread[Execution worker Thread 2,5,main]) started.

> Task :core:generateDebugResources
Caching disabled for task ':core:generateDebugResources' because:
  Build cache is disabled
Task ':core:generateDebugResources' is not up-to-date because:
  No history is available.
Resolve mutations for :core:packageDebugResources (Thread[Execution worker Thread 2,5,main]) started.
:core:packageDebugResources (Thread[Execution worker Thread 2,5,main]) started.
Downloading https://dl.google.com/dl/android/maven2/androidx/asynclayoutinflater/asynclayoutinflater/1.0.0/asynclayoutinflater-1.0.0.pom to /home/runner/.gradle/.tmp/gradle_download4697502157005120873bin

> Task :core:packageDebugResources
Caching disabled for task ':core:packageDebugResources' because:
  Build cache is disabled
Task ':core:packageDebugResources' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':core:packageDebugResources'.
[MergeResources] Inputs are non-incremental full task action.
work action resolve packageDebugResources (project :core) (Thread[included builds,5,main]) started.
Resolve mutations for :core:parseDebugLocalResources (Thread[included builds,5,main]) started.
:core:parseDebugLocalResources (Thread[included builds,5,main]) started.
Resolve mutations for :core:javaPreCompileDebug (Thread[Execution worker Thread 2,5,main]) started.
:core:javaPreCompileDebug (Thread[Execution worker Thread 2,5,main]) started.
work action Dependencies for project :core {artifactType=android-classes-jar, dexing-component-attributes=ComponentSpecificParameters(minSdkVersion=21, debuggable=true, enableCoreLibraryDesugaring=false, enableGlobalSynthetics=true, enableApiModeling=false, dependenciesClassesAreInstrumented=false, asmTransformComponent=null, useJacocoTransformInstrumentation=false, enableDesugaring=true, needsClasspath=true, useFullClasspath=false, componentIfUsingFullClasspath=null)} (Thread[Execution worker Thread 3,5,main]) started.
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with JetifyTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with JetifyTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with JetifyTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with AarToClassTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with AarToClassTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with JetifyTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with JetifyTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with JetifyTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with JetifyTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with JetifyTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with JetifyTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with JetifyTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with AarToClassTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with AarToClassTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with JetifyTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with JetifyTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with AarToClassTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with AarToClassTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with AarToClassTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with JetifyTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with JetifyTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with AarToClassTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with JetifyTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with AarToClassTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with AarToClassTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarToClassTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with AarToClassTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with JetifyTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with JetifyTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with AarToClassTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with IdentityTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with AarToClassTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with JetifyTransform
Transforming okhttp-4.12.0.jar (com.squareup.okhttp3:okhttp:4.12.0) with JetifyTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with JetifyTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with AarToClassTransform
Transforming okhttp-4.12.0.jar (com.squareup.okhttp3:okhttp:4.12.0) with IdentityTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with AarToClassTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarToClassTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with AarToClassTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarToClassTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarToClassTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with AarToClassTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarToClassTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarToClassTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with JetifyTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with JetifyTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with AarToClassTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with AarToClassTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with JetifyTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with JetifyTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with AarToClassTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with AarToClassTransform
Transforming lifecycle-common-2.7.0.jar (androidx.lifecycle:lifecycle-common:2.7.0) with JetifyTransform
Cached resource https://dl.google.com/dl/android/maven2/androidx/recyclerview/recyclerview/1.3.2/recyclerview-1.3.2.aar is up-to-date (lastModified: Wed Oct 18 17:00:00 UTC 2023).
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarToClassTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with JetifyTransform
Transforming lifecycle-common-2.7.0.jar (androidx.lifecycle:lifecycle-common:2.7.0) with IdentityTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with AarToClassTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with AarToClassTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with JetifyTransform
Cached resource https://dl.google.com/dl/android/maven2/androidx/fragment/fragment/1.5.4/fragment-1.5.4.aar is up-to-date (lastModified: Mon Oct 24 13:00:00 UTC 2022).
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with AarToClassTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarToClassTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarToClassTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarToClassTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarToClassTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarToClassTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with AarToClassTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with AarToClassTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarToClassTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarToClassTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarToClassTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with AarToClassTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarToClassTransform
Transforming resourceinspection-annotation-1.0.1.jar (androidx.resourceinspection:resourceinspection-annotation:1.0.1) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarToClassTransform
Transforming resourceinspection-annotation-1.0.1.jar (androidx.resourceinspection:resourceinspection-annotation:1.0.1) with IdentityTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarToClassTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with JetifyTransform
Transforming collection-1.2.0.jar (androidx.collection:collection:1.2.0) with JetifyTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with AarToClassTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with JetifyTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with AarToClassTransform
Transforming collection-1.2.0.jar (androidx.collection:collection:1.2.0) with IdentityTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with JetifyTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with AarToClassTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with JetifyTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with AarToClassTransform
Cached resource https://dl.google.com/dl/android/maven2/androidx/media/media/1.7.0/media-1.7.0.aar is up-to-date (lastModified: Wed Nov 29 18:00:00 UTC 2023).
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarToClassTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with JetifyTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with JetifyTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with AarToClassTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with JetifyTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with AarToClassTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with AarToClassTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with AarToClassTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with JetifyTransform
Transforming core-common-2.2.0.jar (androidx.arch.core:core-common:2.2.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarToClassTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming core-common-2.2.0.jar (androidx.arch.core:core-common:2.2.0) with IdentityTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarToClassTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with AarToClassTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarToClassTransform
Transforming concurrent-futures-1.1.0.jar (androidx.concurrent:concurrent-futures:1.1.0) with JetifyTransform
Transforming annotation-jvm-1.6.0.jar (androidx.annotation:annotation-jvm:1.6.0) with JetifyTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with JetifyTransform
Transforming okio-jvm-3.6.0.jar (com.squareup.okio:okio-jvm:3.6.0) with JetifyTransform
Transforming concurrent-futures-1.1.0.jar (androidx.concurrent:concurrent-futures:1.1.0) with IdentityTransform
Transforming kotlinx-coroutines-android-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1) with JetifyTransform
Transforming annotation-jvm-1.6.0.jar (androidx.annotation:annotation-jvm:1.6.0) with IdentityTransform
Transforming kotlinx-coroutines-core-jvm-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1) with JetifyTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with AarToClassTransform
Transforming kotlinx-coroutines-android-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1) with IdentityTransform
Transforming okio-jvm-3.6.0.jar (com.squareup.okio:okio-jvm:3.6.0) with IdentityTransform
Transforming kotlin-stdlib-jdk8-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-jdk7-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10) with JetifyTransform
Transforming kotlinx-coroutines-core-jvm-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1) with IdentityTransform
Transforming kotlin-stdlib-jdk8-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10) with IdentityTransform
Transforming kotlin-stdlib-jdk7-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10) with IdentityTransform
Transforming kotlin-stdlib-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.10) with JetifyTransform
Transforming guava-32.1.3-android.jar (com.google.guava:guava:32.1.3-android) with JetifyTransform
Transforming disklrucache-4.16.0.jar (com.github.bumptech.glide:disklrucache:4.16.0) with JetifyTransform
Transforming kotlin-stdlib-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.10) with IdentityTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with IdentityTransform
Transforming kotlin-stdlib-common-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10) with JetifyTransform
Transforming disklrucache-4.16.0.jar (com.github.bumptech.glide:disklrucache:4.16.0) with IdentityTransform
Transforming guava-32.1.3-android.jar (com.google.guava:guava:32.1.3-android) with IdentityTransform
Transforming kotlin-stdlib-common-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10) with IdentityTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with IdentityTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with IdentityTransform

> Task :core:parseDebugLocalResources
Transforming android.jar with PlatformAttrTransform
Transforming android.jar with PlatformAttrTransform
Caching disabled for task ':core:parseDebugLocalResources' because:
  Build cache is disabled
Task ':core:parseDebugLocalResources' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':core:parseDebugLocalResources'.
Transforming android.jar with PlatformAttrTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with IdentityTransform
Resolve mutations for :core:generateDebugRFile (Thread[Execution worker Thread 3,5,main]) started.
:core:generateDebugRFile (Thread[Execution worker Thread 3,5,main]) started.
Resolve mutations for :tv:javaPreCompileDebug (Thread[Execution worker,5,main]) started.
:tv:javaPreCompileDebug (Thread[Execution worker,5,main]) started.
Resolve mutations for :core:compileDebugLibraryResources (Thread[included builds,5,main]) started.
:core:compileDebugLibraryResources (Thread[included builds,5,main]) started.

> Task :tv:javaPreCompileDebug
Caching disabled for task ':tv:javaPreCompileDebug' because:
  Build cache is disabled
Task ':tv:javaPreCompileDebug' is not up-to-date because:
  No history is available.

> Task :core:javaPreCompileDebug
Caching disabled for task ':core:javaPreCompileDebug' because:
  Build cache is disabled
Task ':core:javaPreCompileDebug' is not up-to-date because:
  No history is available.
Resolve mutations for :core:writeDebugAarMetadata (Thread[Execution worker Thread 2,5,main]) started.
:core:writeDebugAarMetadata (Thread[Execution worker Thread 2,5,main]) started.

> Task :core:generateDebugRFile
Transforming android.jar with PlatformAttrTransform
Caching disabled for task ':core:generateDebugRFile' because:
  Build cache is disabled
Task ':core:generateDebugRFile' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':core:generateDebugRFile'.

> Task :core:compileDebugLibraryResources
Caching disabled for task ':core:compileDebugLibraryResources' because:
  Build cache is disabled
Task ':core:compileDebugLibraryResources' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':core:compileDebugLibraryResources'.
Transforming aapt2-8.3.0-10880808-linux.jar (com.android.tools.build:aapt2:8.3.0-10880808) with Aapt2Extractor
work action resolve package-aware-r.txt (project :core) (Thread[included builds,5,main]) started.
work action resolve out (project :core) (Thread[included builds,5,main]) started.
Resolve mutations for :core:compileDebugJavaWithJavac (Thread[included builds,5,main]) started.
:core:compileDebugJavaWithJavac (Thread[included builds,5,main]) started.

> Task :core:writeDebugAarMetadata
Caching disabled for task ':core:writeDebugAarMetadata' because:
  Build cache is disabled
Task ':core:writeDebugAarMetadata' is not up-to-date because:
  No history is available.
work action resolve aar-metadata.properties (project :core) (Thread[Execution worker Thread 2,5,main]) started.

> Task :core:compileDebugJavaWithJavac
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with JetifyTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with AarToClassTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with JetifyTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with AarToClassTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with JetifyTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with AarToClassTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with JetifyTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with AarToClassTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with JetifyTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with AarToClassTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with JetifyTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with AarToClassTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with JetifyTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with AarToClassTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with JetifyTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with AarToClassTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with JetifyTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with IdentityTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with JetifyTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with AarToClassTransform
Transforming okhttp-4.12.0.jar (com.squareup.okhttp3:okhttp:4.12.0) with JetifyTransform
Transforming okhttp-4.12.0.jar (com.squareup.okhttp3:okhttp:4.12.0) with IdentityTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with JetifyTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with AarToClassTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarToClassTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with JetifyTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with AarToClassTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarToClassTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarToClassTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarToClassTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarToClassTransform
Transforming lifecycle-common-2.7.0.jar (androidx.lifecycle:lifecycle-common:2.7.0) with JetifyTransform
Transforming lifecycle-common-2.7.0.jar (androidx.lifecycle:lifecycle-common:2.7.0) with IdentityTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarToClassTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarToClassTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with AarToClassTransform
Transforming activity-1.7.0.aar (androidx.activity:activity:1.7.0) with JetifyTransform
Transforming drawerlayout-1.0.0.aar (androidx.drawerlayout:drawerlayout:1.0.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with JetifyTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with AarToClassTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with AarToClassTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with AarToClassTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarToClassTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarToClassTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with JetifyTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with AarToClassTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarToClassTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with JetifyTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with AarToClassTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with JetifyTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with AarToClassTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with JetifyTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with AarToClassTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with JetifyTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with AarToClassTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with JetifyTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with AarToClassTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with JetifyTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with AarToClassTransform
Transforming collection-1.2.0.jar (androidx.collection:collection:1.2.0) with JetifyTransform
Transforming collection-1.2.0.jar (androidx.collection:collection:1.2.0) with IdentityTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarToClassTransform
Transforming activity-1.7.0.aar (androidx.activity:activity:1.7.0) with AarToClassTransform
Transforming customview-1.0.0.aar (androidx.customview:customview:1.0.0) with JetifyTransform
Transforming drawerlayout-1.0.0.aar (androidx.drawerlayout:drawerlayout:1.0.0) with AarToClassTransform
Transforming core-common-2.2.0.jar (androidx.arch.core:core-common:2.2.0) with JetifyTransform
Transforming core-common-2.2.0.jar (androidx.arch.core:core-common:2.2.0) with IdentityTransform
Transforming annotation-jvm-1.6.0.jar (androidx.annotation:annotation-jvm:1.6.0) with JetifyTransform
Transforming annotation-jvm-1.6.0.jar (androidx.annotation:annotation-jvm:1.6.0) with IdentityTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with JetifyTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with AarToClassTransform
Transforming okio-jvm-3.6.0.jar (com.squareup.okio:okio-jvm:3.6.0) with JetifyTransform
Transforming okio-jvm-3.6.0.jar (com.squareup.okio:okio-jvm:3.6.0) with IdentityTransform
Transforming kotlinx-coroutines-android-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1) with JetifyTransform
Transforming kotlinx-coroutines-android-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1) with IdentityTransform
Transforming kotlinx-coroutines-core-jvm-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1) with JetifyTransform
Transforming kotlinx-coroutines-core-jvm-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1) with IdentityTransform
Transforming kotlin-stdlib-jdk8-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-jdk8-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10) with IdentityTransform
Transforming customview-1.0.0.aar (androidx.customview:customview:1.0.0) with JetifyTransform
Transforming customview-1.0.0.aar (androidx.customview:customview:1.0.0) with AarToClassTransform
Transforming kotlin-stdlib-jdk7-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10) with JetifyTransform
Transforming customview-1.0.0.aar (androidx.customview:customview:1.0.0) with AarToClassTransform
Transforming kotlin-stdlib-jdk7-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10) with IdentityTransform
Transforming kotlin-stdlib-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.10) with IdentityTransform
Transforming kotlin-stdlib-common-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-common-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10) with IdentityTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with JetifyTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with IdentityTransform
Transforming guava-32.1.3-android.jar (com.google.guava:guava:32.1.3-android) with JetifyTransform
Transforming guava-32.1.3-android.jar (com.google.guava:guava:32.1.3-android) with IdentityTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with JetifyTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with IdentityTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with JetifyTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with AarToClassTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with JetifyTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with AarToClassTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with JetifyTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with IdentityTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with JetifyTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with AarToClassTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with JetifyTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with AarToClassTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with JetifyTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with AarToClassTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with JetifyTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with AarToClassTransform
Transforming disklrucache-4.16.0.jar (com.github.bumptech.glide:disklrucache:4.16.0) with JetifyTransform
Transforming disklrucache-4.16.0.jar (com.github.bumptech.glide:disklrucache:4.16.0) with IdentityTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with JetifyTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with IdentityTransform
Transforming room-migration-2.6.1.jar (androidx.room:room-migration:2.6.1) with JetifyTransform
Transforming room-compiler-processing-2.6.1.jar (androidx.room:room-compiler-processing:2.6.1) with JetifyTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with JetifyTransform
Transforming room-compiler-2.6.1.jar (androidx.room:room-compiler:2.6.1) with JetifyTransform
Transforming compiler-4.16.0.jar (com.github.bumptech.glide:compiler:4.16.0) with JetifyTransform
Transforming auto-common-0.11.jar (com.google.auto:auto-common:0.11) with JetifyTransform
Transforming auto-value-annotations-1.6.3.jar (com.google.auto.value:auto-value-annotations:1.6.3) with JetifyTransform
Transforming symbol-processing-api-1.9.0-1.0.13.jar (com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.13) with JetifyTransform
Transforming annotations-12.0.jar (com.intellij:annotations:12.0) with JetifyTransform
Transforming kotlinpoet-javapoet-1.14.2.jar (com.squareup:kotlinpoet-javapoet:1.14.2) with JetifyTransform
Transforming javapoet-1.13.0.jar (com.squareup:javapoet:1.13.0) with JetifyTransform
Transforming kotlinpoet-1.14.2.jar (com.squareup:kotlinpoet:1.14.2) with JetifyTransform
Transforming commons-codec-1.15.jar (commons-codec:commons-codec:1.15) with JetifyTransform
Transforming kotlin-stdlib-jdk8-1.8.22.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.22) with JetifyTransform
Transforming kotlin-reflect-1.8.21.jar (org.jetbrains.kotlin:kotlin-reflect:1.8.21) with JetifyTransform
Transforming kotlin-stdlib-jdk7-1.8.22.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.22) with JetifyTransform
Transforming kotlin-stdlib-1.9.0.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.0) with JetifyTransform
Transforming sqlite-jdbc-3.41.2.2.jar (org.xerial:sqlite-jdbc:3.41.2.2) with JetifyTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with JetifyTransform
Transforming annotation-1.3.0.jar (androidx.annotation:annotation:1.3.0) with JetifyTransform
Transforming guava-31.1-jre.jar (com.google.guava:guava:31.1-jre) with JetifyTransform
Transforming gson-2.9.0.jar (com.google.code.gson:gson:2.9.0) with JetifyTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with JetifyTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with JetifyTransform
Transforming jsr305-3.0.2.jar (com.google.code.findbugs:jsr305:3.0.2) with JetifyTransform
Transforming checker-qual-3.12.0.jar (org.checkerframework:checker-qual:3.12.0) with JetifyTransform
Transforming error_prone_annotations-2.11.0.jar (com.google.errorprone:error_prone_annotations:2.11.0) with JetifyTransform
Transforming j2objc-annotations-1.3.jar (com.google.j2objc:j2objc-annotations:1.3) with JetifyTransform
Transforming kotlin-stdlib-common-1.9.0.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.0) with JetifyTransform
Transforming annotations-13.0.jar (org.jetbrains:annotations:13.0) with JetifyTransform
Transforming core-for-system-modules.jar with JdkImageTransform
Transforming core-for-system-modules.jar with JdkImageTransform
Transforming core-for-system-modules.jar with JdkImageTransform
Transforming core-for-system-modules.jar with JdkImageTransform
Transforming core-for-system-modules.jar with JdkImageTransform
Transforming core-for-system-modules.jar with JdkImageTransform
Caching disabled for task ':core:compileDebugJavaWithJavac' because:
  Build cache is disabled
Task ':core:compileDebugJavaWithJavac' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':core:compileDebugJavaWithJavac'.
Transforming core-for-system-modules.jar with JdkImageTransform
Full recompilation is required because no incremental change information is available. This is usually caused by clean builds or changing compiler arguments.
Compiling with toolchain '/usr/lib/jvm/temurin-17-jdk-amd64'.
file or directory '/home/runner/work/wiseiptv/wiseiptv/core/src/debug/java', not found
Compiling with JDK Java compiler API.
Class dependency analysis for incremental compilation took 0.066 secs.
Created classpath snapshot for incremental compilation in 0.115 secs.
Resolve mutations for :core:bundleLibCompileToJarDebug (Thread[included builds,5,main]) started.
:core:bundleLibCompileToJarDebug (Thread[included builds,5,main]) started.
Resolve mutations for :tv:checkDebugAarMetadata (Thread[Execution worker,5,main]) started.
:tv:checkDebugAarMetadata (Thread[Execution worker Thread 2,5,main]) started.

> Task :core:bundleLibCompileToJarDebug
Caching disabled for task ':core:bundleLibCompileToJarDebug' because:
  Build cache is disabled
Task ':core:bundleLibCompileToJarDebug' is not up-to-date because:
  No history is available.
work action resolve classes.jar (project :core) (Thread[Execution worker Thread 3,5,main]) started.
Resolve mutations for :tv:generateDebugResValues (Thread[Execution worker Thread 3,5,main]) started.
:tv:generateDebugResValues (Thread[Execution worker Thread 3,5,main]) started.

> Task :tv:generateDebugResValues
Caching disabled for task ':tv:generateDebugResValues' because:
  Build cache is disabled
Task ':tv:generateDebugResValues' is not up-to-date because:
  No history is available.
Resolve mutations for :tv:mapDebugSourceSetPaths (Thread[Execution worker,5,main]) started.
:tv:mapDebugSourceSetPaths (Thread[Execution worker,5,main]) started.

> Task :tv:checkDebugAarMetadata
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with JetifyTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with JetifyTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with JetifyTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with JetifyTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with ExtractAarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with JetifyTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with ExtractAarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with JetifyTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with ExtractAarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with JetifyTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with ExtractAarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with JetifyTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with ExtractAarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with JetifyTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with ExtractAarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with JetifyTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with ExtractAarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with JetifyTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with ExtractAarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with JetifyTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with JetifyTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with ExtractAarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with JetifyTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with ExtractAarTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with JetifyTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with ExtractAarTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with ExtractAarTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with JetifyTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with JetifyTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with JetifyTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with JetifyTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with ExtractAarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with JetifyTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with ExtractAarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with JetifyTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with ExtractAarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with JetifyTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with ExtractAarTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with JetifyTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with ExtractAarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with JetifyTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with JetifyTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with ExtractAarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with JetifyTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with ExtractAarTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with JetifyTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with ExtractAarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with JetifyTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with ExtractAarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with JetifyTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with ExtractAarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with JetifyTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with ExtractAarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with JetifyTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with ExtractAarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with JetifyTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with ExtractAarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with JetifyTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with JetifyTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with ExtractAarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with AarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with AarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with AarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with AarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with AarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with AarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with AarTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with AarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with AarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with AarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with AarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with JetifyTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with AarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with AarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with AarTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with AarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with AarTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with AarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with AarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with JetifyTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with ExtractAarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with AarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with JetifyTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with ExtractAarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with AarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with AarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarTransform
Downloading https://dl.google.com/dl/android/maven2/androidx/constraintlayout/constraintlayout/2.0.1/constraintlayout-2.0.1.aar to /home/runner/.gradle/.tmp/gradle_download3477127418928670923bin
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with AarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with AarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with AarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with AarTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with AarTransform
Downloading https://dl.google.com/dl/android/maven2/androidx/leanback/leanback/1.0.0/leanback-1.0.0.aar to /home/runner/.gradle/.tmp/gradle_download1485940645864914976bin
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with AarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with AarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarTransform
Downloading https://dl.google.com/dl/android/maven2/androidx/legacy/legacy-support-core-ui/1.0.0/legacy-support-core-ui-1.0.0.aar to /home/runner/.gradle/.tmp/gradle_download9483840463852215650bin
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with AarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with AarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarTransform
Caching disabled for JetifyTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.legacy/legacy-support-core-ui/1.0.0/61a264f996046e059f889914050fae1e75d3b702/legacy-support-core-ui-1.0.0.aar because:
  Build cache is disabled
Downloading https://dl.google.com/dl/android/maven2/androidx/slidingpanelayout/slidingpanelayout/1.0.0/slidingpanelayout-1.0.0.aar to /home/runner/.gradle/.tmp/gradle_download16733011730920106554bin
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with ExtractAarTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with JetifyTransform
Caching disabled for JetifyTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.slidingpanelayout/slidingpanelayout/1.0.0/37eba9ccbf09b75cc4aa78a5e182d5b8ba79ad6a/slidingpanelayout-1.0.0.aar because:
  Build cache is disabled
Caching disabled for ExtractAarTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.legacy/legacy-support-core-ui/1.0.0/61a264f996046e059f889914050fae1e75d3b702/legacy-support-core-ui-1.0.0.aar because:
  Build cache is disabled
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with JetifyTransform
Caching disabled for JetifyTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.constraintlayout/constraintlayout/2.0.1/fab633b1873e5c1fbe49d919fab85ed1f8cf47ca/constraintlayout-2.0.1.aar because:
  Build cache is disabled
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with ExtractAarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with ExtractAarTransform
Caching disabled for ExtractAarTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.slidingpanelayout/slidingpanelayout/1.0.0/37eba9ccbf09b75cc4aa78a5e182d5b8ba79ad6a/slidingpanelayout-1.0.0.aar because:
  Build cache is disabled
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with AarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with AarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/f898a674e9cfed3d540fe9e2a8b40e57/transformed/slidingpanelayout-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/a97fd6e266fa9af37bff1418f096f875/transformed/legacy-support-core-ui-1.0.0 because:
  Build cache is disabled
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Caching disabled for ExtractAarTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.constraintlayout/constraintlayout/2.0.1/fab633b1873e5c1fbe49d919fab85ed1f8cf47ca/constraintlayout-2.0.1.aar because:
  Build cache is disabled
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with AarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/9346ab6aafc40c73d868f8cb25ad6eda/transformed/constraintlayout-2.0.1 because:
  Build cache is disabled
Downloading https://dl.google.com/dl/android/maven2/androidx/swiperefreshlayout/swiperefreshlayout/1.0.0/swiperefreshlayout-1.0.0.aar to /home/runner/.gradle/.tmp/gradle_download15610015919523533060bin
Downloading https://dl.google.com/dl/android/maven2/androidx/asynclayoutinflater/asynclayoutinflater/1.0.0/asynclayoutinflater-1.0.0.aar to /home/runner/.gradle/.tmp/gradle_download3932075154396063981bin
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with JetifyTransform
Caching disabled for JetifyTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.asynclayoutinflater/asynclayoutinflater/1.0.0/5ffa788d19a6863799f25cb50d4fdfb0ec649037/asynclayoutinflater-1.0.0.aar because:
  Build cache is disabled
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Caching disabled for JetifyTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.swiperefreshlayout/swiperefreshlayout/1.0.0/4fd265b80a2b0fbeb062ab2bc4b1487521507762/swiperefreshlayout-1.0.0.aar because:
  Build cache is disabled
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with ExtractAarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with AarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with ExtractAarTransform
Caching disabled for ExtractAarTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.asynclayoutinflater/asynclayoutinflater/1.0.0/5ffa788d19a6863799f25cb50d4fdfb0ec649037/asynclayoutinflater-1.0.0.aar because:
  Build cache is disabled
Caching disabled for ExtractAarTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.swiperefreshlayout/swiperefreshlayout/1.0.0/4fd265b80a2b0fbeb062ab2bc4b1487521507762/swiperefreshlayout-1.0.0.aar because:
  Build cache is disabled
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with AarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with AarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/35cf7e22f9d1f043d812a7c3bcc02ec9/transformed/asynclayoutinflater-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/f846b076b9a99945a2694c3a76bf6d97/transformed/swiperefreshlayout-1.0.0 because:
  Build cache is disabled
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with JetifyTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with AarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with AarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with AarTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with AarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with AarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with AarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with AarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with AarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with JetifyTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with JetifyTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with JetifyTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with ExtractAarTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with AarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with ExtractAarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with ExtractAarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with AarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with AarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with AarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with JetifyTransform
Caching disabled for JetifyTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.leanback/leanback/1.0.0/65025d699df3d9f98e830de9c16fb29cdf348759/leanback-1.0.0.aar because:
  Build cache is disabled
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with ExtractAarTransform
Caching disabled for ExtractAarTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.leanback/leanback/1.0.0/65025d699df3d9f98e830de9c16fb29cdf348759/leanback-1.0.0.aar because:
  Build cache is disabled
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with AarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0 because:
  Build cache is disabled
Caching disabled for task ':tv:checkDebugAarMetadata' because:
  Build cache is disabled
Task ':tv:checkDebugAarMetadata' is not up-to-date because:
  No history is available.

> Task :tv:mapDebugSourceSetPaths
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with JetifyTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with ExtractAarTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with AarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with JetifyTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with ExtractAarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with AarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with JetifyTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with AarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with JetifyTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with AarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with JetifyTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with AarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with JetifyTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with ExtractAarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with AarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with JetifyTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with ExtractAarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with AarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with JetifyTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with ExtractAarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with AarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with JetifyTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with ExtractAarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with AarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with JetifyTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with ExtractAarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with AarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with JetifyTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with ExtractAarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with AarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with JetifyTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with ExtractAarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with AarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with JetifyTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with ExtractAarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with AarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with JetifyTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with ExtractAarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with AarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0 because:
  Build cache is disabled
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with JetifyTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with ExtractAarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with AarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with JetifyTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with ExtractAarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with AarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with JetifyTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with ExtractAarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with AarTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with JetifyTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with ExtractAarTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with AarTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with JetifyTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with ExtractAarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/9346ab6aafc40c73d868f8cb25ad6eda/transformed/constraintlayout-2.0.1 because:
  Build cache is disabled
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with AarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with JetifyTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with ExtractAarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with AarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with JetifyTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with AarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with JetifyTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with ExtractAarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with AarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with JetifyTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with ExtractAarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with AarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with JetifyTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with ExtractAarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with AarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with JetifyTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with ExtractAarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with AarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with JetifyTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with ExtractAarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with AarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with JetifyTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with ExtractAarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with AarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with JetifyTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/a97fd6e266fa9af37bff1418f096f875/transformed/legacy-support-core-ui-1.0.0 because:
  Build cache is disabled
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with ExtractAarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with AarTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with JetifyTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with ExtractAarTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with AarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with AarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with AarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with AarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with ExtractAarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with AarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with JetifyTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with ExtractAarTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with JetifyTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with ExtractAarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with AarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with JetifyTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with ExtractAarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with JetifyTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with ExtractAarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with AarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with JetifyTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with ExtractAarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with AarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with JetifyTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with ExtractAarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with AarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with JetifyTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with ExtractAarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with AarTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with JetifyTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with ExtractAarTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with AarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with JetifyTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with ExtractAarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with AarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with JetifyTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with ExtractAarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with AarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with JetifyTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with ExtractAarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with AarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with JetifyTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with ExtractAarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with AarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with JetifyTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with ExtractAarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with AarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with JetifyTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with ExtractAarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with JetifyTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with ExtractAarTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with AarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with JetifyTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with ExtractAarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with AarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with JetifyTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with ExtractAarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with AarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with JetifyTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with ExtractAarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with AarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/f898a674e9cfed3d540fe9e2a8b40e57/transformed/slidingpanelayout-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/f846b076b9a99945a2694c3a76bf6d97/transformed/swiperefreshlayout-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/35cf7e22f9d1f043d812a7c3bcc02ec9/transformed/asynclayoutinflater-1.0.0 because:
  Build cache is disabled
Caching disabled for task ':tv:mapDebugSourceSetPaths' because:
  Build cache is disabled
Task ':tv:mapDebugSourceSetPaths' is not up-to-date because:
  No history is available.
Resolve mutations for :tv:generateDebugResources (Thread[Execution worker Thread 2,5,main]) started.
:tv:generateDebugResources (Thread[included builds,5,main]) started.

> Task :tv:generateDebugResources
Caching disabled for task ':tv:generateDebugResources' because:
  Build cache is disabled
Task ':tv:generateDebugResources' is not up-to-date because:
  No history is available.
Resolve mutations for :tv:mergeDebugResources (Thread[Execution worker Thread 2,5,main]) started.
:tv:mergeDebugResources (Thread[Execution worker Thread 2,5,main]) started.

> Task :tv:mergeDebugResources
Caching disabled for task ':tv:mergeDebugResources' because:
  Build cache is disabled
Task ':tv:mergeDebugResources' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':tv:mergeDebugResources'.
[MergeResources] Inputs are non-incremental full task action.
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-hi/values-hi.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-hi_values-hi.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values/values.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values_values.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-pt/values-pt.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-pt_values-pt.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-watch-v21/values-watch-v21.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-watch-v21_values-watch-v21.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-hy/values-hy.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-hy_values-hy.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-hu/values-hu.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-hu_values-hu.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-hr/values-hr.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-hr_values-hr.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-v18/values-v18.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-v18_values-v18.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-v17/values-v17.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-v17_values-v17.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-w360dp-port-v13/values-w360dp-port-v13.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-w360dp-port-v13_values-w360dp-port-v13.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-zh-rHK/values-zh-rHK.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-zh-rHK_values-zh-rHK.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-af/values-af.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-af_values-af.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-v16/values-v16.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-v16_values-v16.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-v19/values-v19.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-v19_values-v19.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-w320dp-land-v13/values-w320dp-land-v13.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-w320dp-land-v13_values-w320dp-land-v13.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-am/values-am.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-am_values-am.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-b+sr+Latn/values-b+sr+Latn.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-b+sr+Latn_values-b+sr+Latn.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-is/values-is.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-is_values-is.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-as/values-as.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-as_values-as.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-v21/values-v21.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-v21_values-v21.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-iw/values-iw.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-iw_values-iw.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ja/values-ja.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ja_values-ja.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-es-rUS/values-es-rUS.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-es-rUS_values-es-rUS.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-v24/values-v24.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-v24_values-v24.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-v26/values-v26.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-v26_values-v26.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-v25/values-v25.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-v25_values-v25.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-pt-rPT/values-pt-rPT.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-pt-rPT_values-pt-rPT.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-sw600dp-v13/values-sw600dp-v13.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-sw600dp-v13_values-sw600dp-v13.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-bn/values-bn.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-bn_values-bn.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ro/values-ro.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ro_values-ro.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-v22/values-v22.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-v22_values-v22.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-be/values-be.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-be_values-be.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-h360dp-land-v13/values-h360dp-land-v13.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-h360dp-land-v13_values-h360dp-land-v13.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-h480dp-land-v13/values-h480dp-land-v13.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-h480dp-land-v13_values-h480dp-land-v13.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ka/values-ka.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ka_values-ka.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ru/values-ru.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ru_values-ru.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-en-rCA/values-en-rCA.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-en-rCA_values-en-rCA.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-si/values-si.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-si_values-si.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-sl/values-sl.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-sl_values-sl.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-bs/values-bs.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-bs_values-bs.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-kk/values-kk.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-kk_values-kk.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-v34/values-v34.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-v34_values-v34.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-sq/values-sq.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-sq_values-sq.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ca/values-ca.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ca_values-ca.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-sw/values-sw.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-sw_values-sw.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-kn/values-kn.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-kn_values-kn.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-km/values-km.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-km_values-km.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-pt-rBR/values-pt-rBR.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-pt-rBR_values-pt-rBR.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ta/values-ta.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ta_values-ta.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-th/values-th.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-th_values-th.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ko/values-ko.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ko_values-ko.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-h720dp-v13/values-h720dp-v13.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-h720dp-v13_values-h720dp-v13.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-tr/values-tr.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-tr_values-tr.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-cs/values-cs.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-cs_values-cs.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-xlarge-v4/values-xlarge-v4.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-xlarge-v4_values-xlarge-v4.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-de/values-de.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-de_values-de.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-lt/values-lt.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-lt_values-lt.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-tl/values-tl.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-tl_values-tl.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ldrtl-v17/values-ldrtl-v17.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ldrtl-v17_values-ldrtl-v17.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ml/values-ml.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ml_values-ml.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-en-rIN/values-en-rIN.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-en-rIN_values-en-rIN.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-uz/values-uz.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-uz_values-uz.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-en-rAU/values-en-rAU.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-en-rAU_values-en-rAU.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ur/values-ur.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ur_values-ur.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-my/values-my.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-my_values-my.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-lv/values-lv.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-lv_values-lv.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-fr-rCA/values-fr-rCA.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-fr-rCA_values-fr-rCA.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-vi/values-vi.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-vi_values-vi.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-el/values-el.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-el_values-el.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-w600dp-land-v13/values-w600dp-land-v13.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-w600dp-land-v13_values-w600dp-land-v13.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-nl/values-nl.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-nl_values-nl.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-uk/values-uk.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-uk_values-uk.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-fr/values-fr.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-fr_values-fr.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-et/values-et.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-et_values-et.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-land/values-land.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-land_values-land.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-b+es+419/values-b+es+419.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-b+es+419_values-b+es+419.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-nb/values-nb.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-nb_values-nb.arsc.flat
Compiling xml file /home/runner/work/wiseiptv/wiseiptv/tv/src/main/res/layout/activity_tv_player.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/layout_activity_tv_player.xml.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-mn/values-mn.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-mn_values-mn.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ne/values-ne.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ne_values-ne.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-fi/values-fi.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-fi_values-fi.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ms/values-ms.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ms_values-ms.arsc.flat
Compiling xml file /home/runner/work/wiseiptv/wiseiptv/tv/src/main/res/layout/item_playlist.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/layout_item_playlist.xml.flat

Resolve mutations for :tv:packageDebugResources (Thread[Execution worker,5,main]) started.
:tv:packageDebugResources (Thread[Execution worker,5,main]) started.

> Task :tv:packageDebugResources
Caching disabled for task ':tv:packageDebugResources' because:
  Build cache is disabled
Task ':tv:packageDebugResources' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':tv:packageDebugResources'.
[MergeResources] Inputs are non-incremental full task action.
Resolve mutations for :tv:parseDebugLocalResources (Thread[Execution worker,5,main]) started.
:tv:parseDebugLocalResources (Thread[Execution worker,5,main]) started.
Resolve mutations for :tv:createDebugCompatibleScreenManifests (Thread[Execution worker Thread 3,5,main]) started.
:tv:createDebugCompatibleScreenManifests (Thread[Execution worker Thread 3,5,main]) started.

> Task :tv:parseDebugLocalResources
Transforming android.jar with PlatformAttrTransform
Transforming android.jar with PlatformAttrTransform
Caching disabled for task ':tv:parseDebugLocalResources' because:
  Build cache is disabled
Task ':tv:parseDebugLocalResources' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':tv:parseDebugLocalResources'.
Transforming android.jar with PlatformAttrTransform

> Task :tv:createDebugCompatibleScreenManifests
Caching disabled for task ':tv:createDebugCompatibleScreenManifests' because:
  Build cache is disabled
Task ':tv:createDebugCompatibleScreenManifests' is not up-to-date because:
  No history is available.
Resolve mutations for :core:extractDeepLinksDebug (Thread[Execution worker Thread 3,5,main]) started.
:core:extractDeepLinksDebug (Thread[Execution worker Thread 3,5,main]) started.

> Task :core:extractDeepLinksDebug
Caching disabled for task ':core:extractDeepLinksDebug' because:
  Build cache is disabled
Task ':core:extractDeepLinksDebug' is not up-to-date because:
  No history is available.
work action resolve navigation.json (project :core) (Thread[Execution worker Thread 3,5,main]) started.
Resolve mutations for :core:processDebugManifest (Thread[Execution worker Thread 3,5,main]) started.
:core:processDebugManifest (Thread[Execution worker Thread 3,5,main]) started.
Resolve mutations for :tv:extractDeepLinksDebug (Thread[Execution worker,5,main]) started.
:tv:extractDeepLinksDebug (Thread[Execution worker,5,main]) started.

> Task :tv:extractDeepLinksDebug
Caching disabled for task ':tv:extractDeepLinksDebug' because:
  Build cache is disabled
Task ':tv:extractDeepLinksDebug' is not up-to-date because:
  No history is available.
Resolve mutations for :core:mergeDebugShaders (Thread[Execution worker,5,main]) started.
:core:mergeDebugShaders (Thread[Execution worker,5,main]) started.

> Task :core:mergeDebugShaders
Caching disabled for task ':core:mergeDebugShaders' because:
  Build cache is disabled
Task ':core:mergeDebugShaders' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':core:mergeDebugShaders'.
Resolve mutations for :core:compileDebugShaders (Thread[included builds,5,main]) started.
:core:compileDebugShaders (Thread[included builds,5,main]) started.

> Task :core:compileDebugShaders NO-SOURCE
Skipping task ':core:compileDebugShaders' as it has no source files and no previous output files.
Resolve mutations for :core:generateDebugAssets (Thread[included builds,5,main]) started.
:core:generateDebugAssets (Thread[included builds,5,main]) started.

> Task :core:generateDebugAssets UP-TO-DATE
Skipping task ':core:generateDebugAssets' as it has no actions.
Resolve mutations for :core:packageDebugAssets (Thread[included builds,5,main]) started.
:core:packageDebugAssets (Thread[included builds,5,main]) started.

> Task :core:packageDebugAssets
Caching disabled for task ':core:packageDebugAssets' because:
  Build cache is disabled
Task ':core:packageDebugAssets' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':core:packageDebugAssets'.
work action resolve out (project :core) (Thread[included builds,5,main]) started.
Resolve mutations for :tv:mergeDebugShaders (Thread[included builds,5,main]) started.
:tv:mergeDebugShaders (Thread[included builds,5,main]) started.

> Task :tv:mergeDebugShaders
Caching disabled for task ':tv:mergeDebugShaders' because:
  Build cache is disabled
Task ':tv:mergeDebugShaders' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':tv:mergeDebugShaders'.
Resolve mutations for :tv:compileDebugShaders (Thread[included builds,5,main]) started.
:tv:compileDebugShaders (Thread[included builds,5,main]) started.

> Task :tv:compileDebugShaders NO-SOURCE
Skipping task ':tv:compileDebugShaders' as it has no source files and no previous output files.
Resolve mutations for :tv:generateDebugAssets (Thread[Execution worker,5,main]) started.
:tv:generateDebugAssets (Thread[Execution worker,5,main]) started.

> Task :tv:generateDebugAssets UP-TO-DATE
Skipping task ':tv:generateDebugAssets' as it has no actions.
Resolve mutations for :tv:mergeDebugAssets (Thread[Execution worker,5,main]) started.
:tv:mergeDebugAssets (Thread[Execution worker,5,main]) started.

> Task :core:processDebugManifest
Caching disabled for task ':core:processDebugManifest' because:
  Build cache is disabled
Task ':core:processDebugManifest' is not up-to-date because:
  No history is available.
Merging main manifest /home/runner/work/wiseiptv/wiseiptv/core/src/main/AndroidManifest.xml

Merging result: SUCCESS
1<?xml version="1.0" encoding="utf-8"?>
2<manifest xmlns:android="http://schemas.android.com/apk/res/android"
3    package="com.wdesign.wiseiptv.core" >
4
5    <uses-sdk android:minSdkVersion="21" />
6
7</manifest>

Merged manifest saved to /home/runner/work/wiseiptv/wiseiptv/core/build/intermediates/merged_manifest/debug/processDebugManifest/AndroidManifest.xml

> Task :tv:mergeDebugAssets
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with JetifyTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with ExtractAarTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with AarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with JetifyTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with ExtractAarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with AarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with JetifyTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with AarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with JetifyTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with AarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with JetifyTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with AarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with JetifyTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with ExtractAarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with AarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with JetifyTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with ExtractAarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with AarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with JetifyTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with ExtractAarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with AarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with JetifyTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with ExtractAarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with AarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with JetifyTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with ExtractAarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with AarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with JetifyTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with ExtractAarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with AarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with JetifyTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with ExtractAarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with AarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with JetifyTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with ExtractAarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with AarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with JetifyTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with ExtractAarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with AarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with JetifyTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with ExtractAarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with AarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with JetifyTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with ExtractAarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with AarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with JetifyTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with ExtractAarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with AarTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with JetifyTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with ExtractAarTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with AarTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with JetifyTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with ExtractAarTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with AarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with JetifyTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with ExtractAarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with AarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with JetifyTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with AarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with JetifyTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with ExtractAarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with AarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with JetifyTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with ExtractAarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with AarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with JetifyTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with ExtractAarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with AarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with JetifyTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with ExtractAarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with AarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with JetifyTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with ExtractAarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with AarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with JetifyTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with ExtractAarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with AarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with JetifyTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with ExtractAarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with AarTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with JetifyTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with ExtractAarTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with AarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with AarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with AarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with AarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with ExtractAarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with AarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with JetifyTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with ExtractAarTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with JetifyTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with ExtractAarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with AarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with JetifyTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with ExtractAarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with JetifyTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with ExtractAarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with AarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with JetifyTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with ExtractAarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with AarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with JetifyTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with ExtractAarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with AarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with JetifyTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with ExtractAarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with AarTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with JetifyTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with ExtractAarTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with AarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with JetifyTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with ExtractAarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with AarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with JetifyTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with ExtractAarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with AarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with JetifyTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with ExtractAarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with AarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with JetifyTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/9346ab6aafc40c73d868f8cb25ad6eda/transformed/constraintlayout-2.0.1 because:
  Build cache is disabled
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with ExtractAarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with AarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with JetifyTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with ExtractAarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with AarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with JetifyTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with ExtractAarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with JetifyTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with ExtractAarTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with AarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with JetifyTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with ExtractAarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with AarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with JetifyTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with ExtractAarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with AarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with JetifyTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with ExtractAarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with AarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/a97fd6e266fa9af37bff1418f096f875/transformed/legacy-support-core-ui-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/f898a674e9cfed3d540fe9e2a8b40e57/transformed/slidingpanelayout-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/f846b076b9a99945a2694c3a76bf6d97/transformed/swiperefreshlayout-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/35cf7e22f9d1f043d812a7c3bcc02ec9/transformed/asynclayoutinflater-1.0.0 because:
  Build cache is disabled
Caching disabled for task ':tv:mergeDebugAssets' because:
  Build cache is disabled
Task ':tv:mergeDebugAssets' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':tv:mergeDebugAssets'.
work action resolve AndroidManifest.xml (project :core) (Thread[Execution worker,5,main]) started.
Resolve mutations for :tv:processDebugMainManifest (Thread[Execution worker,5,main]) started.
:tv:processDebugMainManifest (Thread[Execution worker,5,main]) started.

> Task :tv:processDebugMainManifest
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with JetifyTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with ExtractAarTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with AarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with JetifyTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with ExtractAarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with AarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with JetifyTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with AarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with JetifyTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with AarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with JetifyTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with AarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with JetifyTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with ExtractAarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with AarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with JetifyTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with ExtractAarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with AarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with JetifyTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with ExtractAarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with AarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with JetifyTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with ExtractAarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with AarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with JetifyTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with ExtractAarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with AarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with JetifyTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with ExtractAarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with AarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with JetifyTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with ExtractAarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with AarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with JetifyTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with ExtractAarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with AarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with JetifyTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with ExtractAarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with AarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with JetifyTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with ExtractAarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with AarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with JetifyTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with ExtractAarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with AarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with JetifyTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with ExtractAarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with AarTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with JetifyTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with ExtractAarTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with AarTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with JetifyTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with ExtractAarTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with AarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with JetifyTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with ExtractAarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with AarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with JetifyTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with AarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with JetifyTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with ExtractAarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with AarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with JetifyTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with ExtractAarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with AarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with JetifyTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with ExtractAarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with AarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with JetifyTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with ExtractAarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with AarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with JetifyTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with ExtractAarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with AarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0 because:
  Build cache is disabled
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with JetifyTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with ExtractAarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with AarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with JetifyTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with ExtractAarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with AarTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with JetifyTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with ExtractAarTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with AarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with AarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with AarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with AarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with ExtractAarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with AarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/9346ab6aafc40c73d868f8cb25ad6eda/transformed/constraintlayout-2.0.1 because:
  Build cache is disabled
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with JetifyTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with ExtractAarTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with JetifyTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with ExtractAarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with AarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with JetifyTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with ExtractAarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with JetifyTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with ExtractAarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with AarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with JetifyTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with ExtractAarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with AarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with JetifyTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with ExtractAarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with AarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with JetifyTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with ExtractAarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with AarTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with JetifyTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with ExtractAarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/a97fd6e266fa9af37bff1418f096f875/transformed/legacy-support-core-ui-1.0.0 because:
  Build cache is disabled
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with AarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with JetifyTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with ExtractAarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with AarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with JetifyTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with ExtractAarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with AarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with JetifyTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with ExtractAarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with AarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with JetifyTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with ExtractAarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with AarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with JetifyTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with ExtractAarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with AarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with JetifyTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with ExtractAarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with JetifyTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with ExtractAarTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with AarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with JetifyTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with ExtractAarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with AarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with JetifyTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with ExtractAarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with AarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with JetifyTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with ExtractAarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with AarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/f898a674e9cfed3d540fe9e2a8b40e57/transformed/slidingpanelayout-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/f846b076b9a99945a2694c3a76bf6d97/transformed/swiperefreshlayout-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/35cf7e22f9d1f043d812a7c3bcc02ec9/transformed/asynclayoutinflater-1.0.0 because:
  Build cache is disabled
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with JetifyTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with ExtractAarTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with AarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with JetifyTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with ExtractAarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with AarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with JetifyTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with AarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0 because:
  Build cache is disabled
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with JetifyTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with AarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with JetifyTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with AarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with JetifyTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with ExtractAarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with AarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with JetifyTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with ExtractAarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with AarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with JetifyTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with ExtractAarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with AarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with JetifyTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with ExtractAarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with AarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with JetifyTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with ExtractAarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with AarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with JetifyTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with ExtractAarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with AarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with JetifyTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with ExtractAarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with AarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with JetifyTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with ExtractAarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with AarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with JetifyTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with ExtractAarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with AarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with JetifyTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with ExtractAarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with AarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with JetifyTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with ExtractAarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with AarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with JetifyTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with ExtractAarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with AarTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with JetifyTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with ExtractAarTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with AarTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with JetifyTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with ExtractAarTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with AarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with JetifyTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with ExtractAarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with AarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with JetifyTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with AarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with JetifyTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with ExtractAarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with AarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with JetifyTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with ExtractAarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with AarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with JetifyTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with ExtractAarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with AarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with JetifyTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with ExtractAarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with AarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with JetifyTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with ExtractAarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with AarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with JetifyTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with ExtractAarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with AarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with JetifyTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with ExtractAarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with AarTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with JetifyTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with ExtractAarTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with AarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with AarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with AarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with AarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with ExtractAarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with AarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with JetifyTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with ExtractAarTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with JetifyTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with ExtractAarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with AarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with JetifyTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with ExtractAarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with JetifyTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with ExtractAarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with AarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with JetifyTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with ExtractAarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with AarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with JetifyTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with ExtractAarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with AarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with JetifyTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with ExtractAarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with AarTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with JetifyTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with ExtractAarTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with AarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with JetifyTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with ExtractAarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with AarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with JetifyTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with ExtractAarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with AarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with JetifyTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with ExtractAarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with AarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with JetifyTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with ExtractAarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with AarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with JetifyTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/9346ab6aafc40c73d868f8cb25ad6eda/transformed/constraintlayout-2.0.1 because:
  Build cache is disabled
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with ExtractAarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with AarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with JetifyTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with ExtractAarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with JetifyTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with ExtractAarTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with AarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with JetifyTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with ExtractAarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with AarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with JetifyTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with ExtractAarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with AarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with JetifyTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with ExtractAarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with AarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/a97fd6e266fa9af37bff1418f096f875/transformed/legacy-support-core-ui-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/f898a674e9cfed3d540fe9e2a8b40e57/transformed/slidingpanelayout-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/f846b076b9a99945a2694c3a76bf6d97/transformed/swiperefreshlayout-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/35cf7e22f9d1f043d812a7c3bcc02ec9/transformed/asynclayoutinflater-1.0.0 because:
  Build cache is disabled
Caching disabled for task ':tv:processDebugMainManifest' because:
  Build cache is disabled
Task ':tv:processDebugMainManifest' is not up-to-date because:
  No history is available.
Loading library manifest /home/runner/work/wiseiptv/wiseiptv/core/build/intermediates/merged_manifest/debug/processDebugManifest/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/61c7acaba160e09f048a2c9cb89f1b15/transformed/material-1.12.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/ac6a9a0a462590b1560fbcdb96a13590/transformed/jetified-media3-exoplayer-hls-1.3.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/2b7410d6bf9f8a56ac27d5f6061bced9/transformed/jetified-media3-exoplayer-dash-1.3.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/968f87b459e0b578287c7024cfcb89f8/transformed/jetified-media3-exoplayer-1.3.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/b020fb7eace4270094df3bbcdaafa4ec/transformed/jetified-media3-datasource-okhttp-1.3.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/67b2468d13d796541282e48ff4938947/transformed/jetified-media3-datasource-1.3.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/ad5f03cb29bea91672e7d357dc57d86b/transformed/jetified-media3-extractor-1.3.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/1c4f20bb8a2489253b953565aa5b02d0/transformed/jetified-media3-container-1.3.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/691fc213a470c7a6a6ac0d4c7dc0f6c4/transformed/jetified-media3-decoder-1.3.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/2c90ea12d6bfa2b8df4e73422fd1074f/transformed/jetified-media3-database-1.3.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/4dd98756242a7166f3ddb803590f3420/transformed/jetified-media3-common-1.3.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/1936099ef5178cb47394b7939c7b64a3/transformed/jetified-media3-ui-1.3.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/7160d3436bbc8cc2498e8e79056abb01/transformed/jetified-viewpager2-1.1.0-beta02/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/a3067c42258be651eb2e82056c5bb54c/transformed/recyclerview-1.3.2/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/9346ab6aafc40c73d868f8cb25ad6eda/transformed/constraintlayout-2.0.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/2eac344b0e1437691f12baad17fc0a7c/transformed/jetified-appcompat-resources-1.7.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/2d60e11ceea301e30143e8eefcb8edf5/transformed/appcompat-1.7.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/c25696574b9114c6991abe36517b309c/transformed/room-runtime-2.6.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/08b6a3ee59b64694988f22bc0e28dc2d/transformed/jetified-glide-4.16.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/c577c7e45888442991a94ee77a5e061c/transformed/fragment-1.5.4/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/990618fd15ef5fe58d047488640e6999/transformed/jetified-activity-1.8.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/a97fd6e266fa9af37bff1418f096f875/transformed/legacy-support-core-ui-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/f067a6051337d3b79d3e7d11003e0035/transformed/coordinatorlayout-1.1.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/fea3a8a143804bf764f9f8c903b5b661/transformed/drawerlayout-1.1.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/70a9f73a6e2e4e4b29014b84aed89ab7/transformed/transition-1.5.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/0f822fb90b6e94e69b2d20fe6341aed1/transformed/dynamicanimation-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/99df21d4160d4636659439272a92d0f3/transformed/vectordrawable-animated-1.1.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/d0d1c90f3788dfd4c771ecb64075898e/transformed/vectordrawable-1.1.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/cf9c176383d058759751c1b763cb90d1/transformed/jetified-customview-poolingcontainer-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/d3c36c5e12835637b75fcb2868de8fc9/transformed/jetified-emoji2-views-helper-1.3.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/822467cc6e4d69eef592a63d52fa54ca/transformed/jetified-emoji2-1.3.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/c03f040eff675eca9d7657a650bcf0d1/transformed/jetified-savedstate-1.2.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/e700a3f752763e22f0c7b8dbc23972c3/transformed/jetified-lifecycle-process-2.7.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/989b62fe57ee777f6c79a3bbfb0ba89e/transformed/jetified-lifecycle-livedata-core-ktx-2.7.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/c1fcdfe2fcc8ce6ce710d91217003a6d/transformed/lifecycle-livedata-core-2.7.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/fba572c399e240c8227a56ccc51d957d/transformed/legacy-support-core-utils-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/746352a98c016cd42bbbe9e2c3f7319d/transformed/loader-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/6a893a7e050ef39afc93d4d9c0c6e66a/transformed/lifecycle-viewmodel-2.7.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/3bdd1331e1302a45987add8d17b57e74/transformed/jetified-lifecycle-viewmodel-savedstate-2.7.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/fa2c9c66a81e51fb14d1fac08dcf872f/transformed/jetified-core-ktx-1.13.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/2536992368a918694366e3bdb798e7b6/transformed/media-1.7.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/4dd2eddd016c85e6278219005671ec2a/transformed/viewpager-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/f898a674e9cfed3d540fe9e2a8b40e57/transformed/slidingpanelayout-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/9aca73330bbfa9bf0d5fc7364036a9d8/transformed/customview-1.1.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/f846b076b9a99945a2694c3a76bf6d97/transformed/swiperefreshlayout-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/35cf7e22f9d1f043d812a7c3bcc02ec9/transformed/asynclayoutinflater-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/da6dae1a7818fe5e5cec32306397e7da/transformed/core-1.13.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/aa276b15bb13c78493f7fbde468b24a2/transformed/lifecycle-runtime-2.7.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/4b65a380ba0aa576a411344f88f4d079/transformed/lifecycle-livedata-2.7.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/36fb7616050fba8c389922529db37a72/transformed/cardview-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/187921e138129a252c9b33385d6cf7e5/transformed/cursoradapter-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/1f2d18028e769fb15b228170380dc8bf/transformed/versionedparcelable-1.1.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/576a8f3980635254f2fda4984958cda3/transformed/exifinterface-1.3.6/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/a81262d910730b02fe1fb8eaf60e6a15/transformed/core-runtime-2.2.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/5b5d8cf9e8bd6c9e458a1bb67e99a27f/transformed/sqlite-framework-2.4.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/64728f2d9caac9a1ca48761c30c00108/transformed/sqlite-2.4.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/52c5fd5ed45c093bd5703fdf2caf118e/transformed/jetified-gifdecoder-4.16.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/7a526fa1dabeb6a36adb1a01465d3a15/transformed/jetified-startup-runtime-1.1.1/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/d3fbe586a952ec492822bceff4881c03/transformed/jetified-tracing-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/1ceac3d27c69c2b9b31614e00cd3abbc/transformed/interpolator-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/930d438abcc334a070edf8bd557626b3/transformed/documentfile-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/9beda206dd4b846a110859856608903e/transformed/localbroadcastmanager-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/d3c884fa679a06e3a691e54eed75e668/transformed/print-1.0.0/AndroidManifest.xml
Loading library manifest /home/runner/.gradle/caches/transforms-4/8c6400ddf911810a936ef14484f5be72/transformed/jetified-annotation-experimental-1.4.0/AndroidManifest.xml
Merging main manifest /home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml

Merging library manifest /home/runner/work/wiseiptv/wiseiptv/core/build/intermediates/merged_manifest/debug/processDebugManifest/AndroidManifest.xml
Merging manifest with lower [:core] AndroidManifest.xml:2:1-7:12
uses-sdk defined in both files...
Merging uses-sdk with lower [:core] AndroidManifest.xml:5:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/61c7acaba160e09f048a2c9cb89f1b15/transformed/material-1.12.0/AndroidManifest.xml
Merging manifest with lower [com.google.android.material:material:1.12.0] AndroidManifest.xml:17:1-24:12
uses-sdk defined in both files...
Merging uses-sdk with lower [com.google.android.material:material:1.12.0] AndroidManifest.xml:20:5-44
application defined in both files...
Merging application with lower [com.google.android.material:material:1.12.0] AndroidManifest.xml:22:5-20
Merging library manifest /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.leanback:leanback:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.leanback:leanback:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/ac6a9a0a462590b1560fbcdb96a13590/transformed/jetified-media3-exoplayer-hls-1.3.1/AndroidManifest.xml
Merging manifest with lower [androidx.media3:media3-exoplayer-hls:1.3.1] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.media3:media3-exoplayer-hls:1.3.1] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/2b7410d6bf9f8a56ac27d5f6061bced9/transformed/jetified-media3-exoplayer-dash-1.3.1/AndroidManifest.xml
Merging manifest with lower [androidx.media3:media3-exoplayer-dash:1.3.1] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.media3:media3-exoplayer-dash:1.3.1] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/968f87b459e0b578287c7024cfcb89f8/transformed/jetified-media3-exoplayer-1.3.1/AndroidManifest.xml
Merging manifest with lower [androidx.media3:media3-exoplayer:1.3.1] AndroidManifest.xml:17:1-24:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.media3:media3-exoplayer:1.3.1] AndroidManifest.xml:20:5-44
uses-permission#android.permission.ACCESS_NETWORK_STATE defined in both files...
Merging uses-permission#android.permission.ACCESS_NETWORK_STATE with lower [androidx.media3:media3-exoplayer:1.3.1] AndroidManifest.xml:22:5-79
Merging library manifest /home/runner/.gradle/caches/transforms-4/b020fb7eace4270094df3bbcdaafa4ec/transformed/jetified-media3-datasource-okhttp-1.3.1/AndroidManifest.xml
Merging manifest with lower [androidx.media3:media3-datasource-okhttp:1.3.1] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.media3:media3-datasource-okhttp:1.3.1] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/67b2468d13d796541282e48ff4938947/transformed/jetified-media3-datasource-1.3.1/AndroidManifest.xml
Merging manifest with lower [androidx.media3:media3-datasource:1.3.1] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.media3:media3-datasource:1.3.1] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/ad5f03cb29bea91672e7d357dc57d86b/transformed/jetified-media3-extractor-1.3.1/AndroidManifest.xml
Merging manifest with lower [androidx.media3:media3-extractor:1.3.1] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.media3:media3-extractor:1.3.1] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/1c4f20bb8a2489253b953565aa5b02d0/transformed/jetified-media3-container-1.3.1/AndroidManifest.xml
Merging manifest with lower [androidx.media3:media3-container:1.3.1] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.media3:media3-container:1.3.1] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/691fc213a470c7a6a6ac0d4c7dc0f6c4/transformed/jetified-media3-decoder-1.3.1/AndroidManifest.xml
Merging manifest with lower [androidx.media3:media3-decoder:1.3.1] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.media3:media3-decoder:1.3.1] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/2c90ea12d6bfa2b8df4e73422fd1074f/transformed/jetified-media3-database-1.3.1/AndroidManifest.xml
Merging manifest with lower [androidx.media3:media3-database:1.3.1] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.media3:media3-database:1.3.1] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/4dd98756242a7166f3ddb803590f3420/transformed/jetified-media3-common-1.3.1/AndroidManifest.xml
Merging manifest with lower [androidx.media3:media3-common:1.3.1] AndroidManifest.xml:17:1-24:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.media3:media3-common:1.3.1] AndroidManifest.xml:20:5-44
uses-permission#android.permission.ACCESS_NETWORK_STATE defined in both files...
Merging uses-permission#android.permission.ACCESS_NETWORK_STATE with lower [androidx.media3:media3-common:1.3.1] AndroidManifest.xml:22:5-79
Merging library manifest /home/runner/.gradle/caches/transforms-4/1936099ef5178cb47394b7939c7b64a3/transformed/jetified-media3-ui-1.3.1/AndroidManifest.xml
Merging manifest with lower [androidx.media3:media3-ui:1.3.1] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.media3:media3-ui:1.3.1] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/7160d3436bbc8cc2498e8e79056abb01/transformed/jetified-viewpager2-1.1.0-beta02/AndroidManifest.xml
Merging manifest with lower [androidx.viewpager2:viewpager2:1.1.0-beta02] AndroidManifest.xml:2:1-7:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.viewpager2:viewpager2:1.1.0-beta02] AndroidManifest.xml:5:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/a3067c42258be651eb2e82056c5bb54c/transformed/recyclerview-1.3.2/AndroidManifest.xml
Merging manifest with lower [androidx.recyclerview:recyclerview:1.3.2] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.recyclerview:recyclerview:1.3.2] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/9346ab6aafc40c73d868f8cb25ad6eda/transformed/constraintlayout-2.0.1/AndroidManifest.xml
Merging manifest with lower [androidx.constraintlayout:constraintlayout:2.0.1] AndroidManifest.xml:2:1-11:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.constraintlayout:constraintlayout:2.0.1] AndroidManifest.xml:5:5-7:41
application defined in both files...
Merging application with lower [androidx.constraintlayout:constraintlayout:2.0.1] AndroidManifest.xml:9:5-20
Merging library manifest /home/runner/.gradle/caches/transforms-4/2eac344b0e1437691f12baad17fc0a7c/transformed/jetified-appcompat-resources-1.7.0/AndroidManifest.xml
Merging manifest with lower [androidx.appcompat:appcompat-resources:1.7.0] AndroidManifest.xml:2:1-7:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.appcompat:appcompat-resources:1.7.0] AndroidManifest.xml:5:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/2d60e11ceea301e30143e8eefcb8edf5/transformed/appcompat-1.7.0/AndroidManifest.xml
Merging manifest with lower [androidx.appcompat:appcompat:1.7.0] AndroidManifest.xml:2:1-7:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.appcompat:appcompat:1.7.0] AndroidManifest.xml:5:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/c25696574b9114c6991abe36517b309c/transformed/room-runtime-2.6.1/AndroidManifest.xml
Merging manifest with lower [androidx.room:room-runtime:2.6.1] AndroidManifest.xml:17:1-31:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.room:room-runtime:2.6.1] AndroidManifest.xml:21:5-44
application defined in both files...
Merging application with lower [androidx.room:room-runtime:2.6.1] AndroidManifest.xml:23:5-29:19
Adopted [service: null]
Merging library manifest /home/runner/.gradle/caches/transforms-4/08b6a3ee59b64694988f22bc0e28dc2d/transformed/jetified-glide-4.16.0/AndroidManifest.xml
Merging manifest with lower [com.github.bumptech.glide:glide:4.16.0] AndroidManifest.xml:2:1-9:12
uses-sdk defined in both files...
Merging uses-sdk with lower [com.github.bumptech.glide:glide:4.16.0] AndroidManifest.xml:5:5-7:41
Merging library manifest /home/runner/.gradle/caches/transforms-4/c577c7e45888442991a94ee77a5e061c/transformed/fragment-1.5.4/AndroidManifest.xml
Merging manifest with lower [androidx.fragment:fragment:1.5.4] AndroidManifest.xml:17:1-24:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.fragment:fragment:1.5.4] AndroidManifest.xml:20:5-22:41
Merging library manifest /home/runner/.gradle/caches/transforms-4/990618fd15ef5fe58d047488640e6999/transformed/jetified-activity-1.8.0/AndroidManifest.xml
Merging manifest with lower [androidx.activity:activity:1.8.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.activity:activity:1.8.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/a97fd6e266fa9af37bff1418f096f875/transformed/legacy-support-core-ui-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.legacy:legacy-support-core-ui:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.legacy:legacy-support-core-ui:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/f067a6051337d3b79d3e7d11003e0035/transformed/coordinatorlayout-1.1.0/AndroidManifest.xml
Merging manifest with lower [androidx.coordinatorlayout:coordinatorlayout:1.1.0] AndroidManifest.xml:17:1-24:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.coordinatorlayout:coordinatorlayout:1.1.0] AndroidManifest.xml:20:5-22:41
Merging library manifest /home/runner/.gradle/caches/transforms-4/fea3a8a143804bf764f9f8c903b5b661/transformed/drawerlayout-1.1.1/AndroidManifest.xml
Merging manifest with lower [androidx.drawerlayout:drawerlayout:1.1.1] AndroidManifest.xml:17:1-24:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.drawerlayout:drawerlayout:1.1.1] AndroidManifest.xml:20:5-22:41
Merging library manifest /home/runner/.gradle/caches/transforms-4/70a9f73a6e2e4e4b29014b84aed89ab7/transformed/transition-1.5.0/AndroidManifest.xml
Merging manifest with lower [androidx.transition:transition:1.5.0] AndroidManifest.xml:2:1-7:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.transition:transition:1.5.0] AndroidManifest.xml:5:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/0f822fb90b6e94e69b2d20fe6341aed1/transformed/dynamicanimation-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.dynamicanimation:dynamicanimation:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.dynamicanimation:dynamicanimation:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/99df21d4160d4636659439272a92d0f3/transformed/vectordrawable-animated-1.1.0/AndroidManifest.xml
Merging manifest with lower [androidx.vectordrawable:vectordrawable-animated:1.1.0] AndroidManifest.xml:17:1-24:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.vectordrawable:vectordrawable-animated:1.1.0] AndroidManifest.xml:20:5-22:41
Merging library manifest /home/runner/.gradle/caches/transforms-4/d0d1c90f3788dfd4c771ecb64075898e/transformed/vectordrawable-1.1.0/AndroidManifest.xml
Merging manifest with lower [androidx.vectordrawable:vectordrawable:1.1.0] AndroidManifest.xml:17:1-24:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.vectordrawable:vectordrawable:1.1.0] AndroidManifest.xml:20:5-22:41
Merging library manifest /home/runner/.gradle/caches/transforms-4/cf9c176383d058759751c1b763cb90d1/transformed/jetified-customview-poolingcontainer-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.customview:customview-poolingcontainer:1.0.0] AndroidManifest.xml:17:1-23:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.customview:customview-poolingcontainer:1.0.0] AndroidManifest.xml:20:5-21:38
Merging library manifest /home/runner/.gradle/caches/transforms-4/d3c36c5e12835637b75fcb2868de8fc9/transformed/jetified-emoji2-views-helper-1.3.0/AndroidManifest.xml
Merging manifest with lower [androidx.emoji2:emoji2-views-helper:1.3.0] AndroidManifest.xml:2:1-7:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.emoji2:emoji2-views-helper:1.3.0] AndroidManifest.xml:5:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/822467cc6e4d69eef592a63d52fa54ca/transformed/jetified-emoji2-1.3.0/AndroidManifest.xml
Merging manifest with lower [androidx.emoji2:emoji2:1.3.0] AndroidManifest.xml:17:1-35:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.emoji2:emoji2:1.3.0] AndroidManifest.xml:21:5-44
application defined in both files...
Merging application with lower [androidx.emoji2:emoji2:1.3.0] AndroidManifest.xml:23:5-33:19
Adopted [provider: null]
Merging library manifest /home/runner/.gradle/caches/transforms-4/c03f040eff675eca9d7657a650bcf0d1/transformed/jetified-savedstate-1.2.1/AndroidManifest.xml
Merging manifest with lower [androidx.savedstate:savedstate:1.2.1] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.savedstate:savedstate:1.2.1] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/e700a3f752763e22f0c7b8dbc23972c3/transformed/jetified-lifecycle-process-2.7.0/AndroidManifest.xml
Merging manifest with lower [androidx.lifecycle:lifecycle-process:2.7.0] AndroidManifest.xml:17:1-35:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.lifecycle:lifecycle-process:2.7.0] AndroidManifest.xml:21:5-44
application defined in both files...
Merging application with lower [androidx.lifecycle:lifecycle-process:2.7.0] AndroidManifest.xml:23:5-33:19
provider#androidx.startup.InitializationProvider defined in both files...
Merging provider#androidx.startup.InitializationProvider with lower [androidx.lifecycle:lifecycle-process:2.7.0] AndroidManifest.xml:24:9-32:20
Adopted [meta-data: null]
Merging library manifest /home/runner/.gradle/caches/transforms-4/989b62fe57ee777f6c79a3bbfb0ba89e/transformed/jetified-lifecycle-livedata-core-ktx-2.7.0/AndroidManifest.xml
Merging manifest with lower [androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0] AndroidManifest.xml:2:1-7:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0] AndroidManifest.xml:5:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/c1fcdfe2fcc8ce6ce710d91217003a6d/transformed/lifecycle-livedata-core-2.7.0/AndroidManifest.xml
Merging manifest with lower [androidx.lifecycle:lifecycle-livedata-core:2.7.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.lifecycle:lifecycle-livedata-core:2.7.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/fba572c399e240c8227a56ccc51d957d/transformed/legacy-support-core-utils-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.legacy:legacy-support-core-utils:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.legacy:legacy-support-core-utils:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/746352a98c016cd42bbbe9e2c3f7319d/transformed/loader-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.loader:loader:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.loader:loader:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/6a893a7e050ef39afc93d4d9c0c6e66a/transformed/lifecycle-viewmodel-2.7.0/AndroidManifest.xml
Merging manifest with lower [androidx.lifecycle:lifecycle-viewmodel:2.7.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.lifecycle:lifecycle-viewmodel:2.7.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/3bdd1331e1302a45987add8d17b57e74/transformed/jetified-lifecycle-viewmodel-savedstate-2.7.0/AndroidManifest.xml
Merging manifest with lower [androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/fa2c9c66a81e51fb14d1fac08dcf872f/transformed/jetified-core-ktx-1.13.0/AndroidManifest.xml
Merging manifest with lower [androidx.core:core-ktx:1.13.0] AndroidManifest.xml:2:1-7:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.core:core-ktx:1.13.0] AndroidManifest.xml:5:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/2536992368a918694366e3bdb798e7b6/transformed/media-1.7.0/AndroidManifest.xml
Merging manifest with lower [androidx.media:media:1.7.0] AndroidManifest.xml:2:1-7:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.media:media:1.7.0] AndroidManifest.xml:5:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/4dd2eddd016c85e6278219005671ec2a/transformed/viewpager-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.viewpager:viewpager:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.viewpager:viewpager:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/f898a674e9cfed3d540fe9e2a8b40e57/transformed/slidingpanelayout-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.slidingpanelayout:slidingpanelayout:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.slidingpanelayout:slidingpanelayout:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/9aca73330bbfa9bf0d5fc7364036a9d8/transformed/customview-1.1.0/AndroidManifest.xml
Merging manifest with lower [androidx.customview:customview:1.1.0] AndroidManifest.xml:17:1-24:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.customview:customview:1.1.0] AndroidManifest.xml:20:5-22:41
Merging library manifest /home/runner/.gradle/caches/transforms-4/f846b076b9a99945a2694c3a76bf6d97/transformed/swiperefreshlayout-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.swiperefreshlayout:swiperefreshlayout:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.swiperefreshlayout:swiperefreshlayout:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/35cf7e22f9d1f043d812a7c3bcc02ec9/transformed/asynclayoutinflater-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.asynclayoutinflater:asynclayoutinflater:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.asynclayoutinflater:asynclayoutinflater:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/da6dae1a7818fe5e5cec32306397e7da/transformed/core-1.13.0/AndroidManifest.xml
Merging manifest with lower [androidx.core:core:1.13.0] AndroidManifest.xml:17:1-30:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.core:core:1.13.0] AndroidManifest.xml:20:5-44
Adopted [permission: null]
Adopted [uses-permission: null]
application defined in both files...
Merging application with lower [androidx.core:core:1.13.0] AndroidManifest.xml:28:5-89
Merging library manifest /home/runner/.gradle/caches/transforms-4/aa276b15bb13c78493f7fbde468b24a2/transformed/lifecycle-runtime-2.7.0/AndroidManifest.xml
Merging manifest with lower [androidx.lifecycle:lifecycle-runtime:2.7.0] AndroidManifest.xml:2:1-7:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.lifecycle:lifecycle-runtime:2.7.0] AndroidManifest.xml:5:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/4b65a380ba0aa576a411344f88f4d079/transformed/lifecycle-livedata-2.7.0/AndroidManifest.xml
Merging manifest with lower [androidx.lifecycle:lifecycle-livedata:2.7.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.lifecycle:lifecycle-livedata:2.7.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/36fb7616050fba8c389922529db37a72/transformed/cardview-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.cardview:cardview:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.cardview:cardview:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/187921e138129a252c9b33385d6cf7e5/transformed/cursoradapter-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.cursoradapter:cursoradapter:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.cursoradapter:cursoradapter:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml
Merging manifest with lower [androidx.profileinstaller:profileinstaller:1.3.1] AndroidManifest.xml:17:1-55:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.profileinstaller:profileinstaller:1.3.1] AndroidManifest.xml:21:5-44
application defined in both files...
Merging application with lower [androidx.profileinstaller:profileinstaller:1.3.1] AndroidManifest.xml:23:5-53:19
provider#androidx.startup.InitializationProvider defined in both files...
Merging provider#androidx.startup.InitializationProvider with lower [androidx.profileinstaller:profileinstaller:1.3.1] AndroidManifest.xml:24:9-32:20
Adopted [meta-data: null]
Adopted [receiver: null]
Merging library manifest /home/runner/.gradle/caches/transforms-4/1f2d18028e769fb15b228170380dc8bf/transformed/versionedparcelable-1.1.1/AndroidManifest.xml
Merging manifest with lower [androidx.versionedparcelable:versionedparcelable:1.1.1] AndroidManifest.xml:17:1-27:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.versionedparcelable:versionedparcelable:1.1.1] AndroidManifest.xml:20:5-22:41
application defined in both files...
Merging application with lower [androidx.versionedparcelable:versionedparcelable:1.1.1] AndroidManifest.xml:24:5-25:19
Merging library manifest /home/runner/.gradle/caches/transforms-4/576a8f3980635254f2fda4984958cda3/transformed/exifinterface-1.3.6/AndroidManifest.xml
Merging manifest with lower [androidx.exifinterface:exifinterface:1.3.6] AndroidManifest.xml:17:1-24:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.exifinterface:exifinterface:1.3.6] AndroidManifest.xml:20:5-22:41
Merging library manifest /home/runner/.gradle/caches/transforms-4/a81262d910730b02fe1fb8eaf60e6a15/transformed/core-runtime-2.2.0/AndroidManifest.xml
Merging manifest with lower [androidx.arch.core:core-runtime:2.2.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.arch.core:core-runtime:2.2.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/5b5d8cf9e8bd6c9e458a1bb67e99a27f/transformed/sqlite-framework-2.4.0/AndroidManifest.xml
Merging manifest with lower [androidx.sqlite:sqlite-framework:2.4.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.sqlite:sqlite-framework:2.4.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/64728f2d9caac9a1ca48761c30c00108/transformed/sqlite-2.4.0/AndroidManifest.xml
Merging manifest with lower [androidx.sqlite:sqlite:2.4.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.sqlite:sqlite:2.4.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/52c5fd5ed45c093bd5703fdf2caf118e/transformed/jetified-gifdecoder-4.16.0/AndroidManifest.xml
Merging manifest with lower [com.github.bumptech.glide:gifdecoder:4.16.0] AndroidManifest.xml:2:1-9:12
uses-sdk defined in both files...
Merging uses-sdk with lower [com.github.bumptech.glide:gifdecoder:4.16.0] AndroidManifest.xml:5:5-7:41
Merging library manifest /home/runner/.gradle/caches/transforms-4/7a526fa1dabeb6a36adb1a01465d3a15/transformed/jetified-startup-runtime-1.1.1/AndroidManifest.xml
Merging manifest with lower [androidx.startup:startup-runtime:1.1.1] AndroidManifest.xml:17:1-33:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.startup:startup-runtime:1.1.1] AndroidManifest.xml:21:5-23:41
application defined in both files...
Merging application with lower [androidx.startup:startup-runtime:1.1.1] AndroidManifest.xml:25:5-31:19
provider#androidx.startup.InitializationProvider defined in both files...
Merging provider#androidx.startup.InitializationProvider with lower [androidx.startup:startup-runtime:1.1.1] AndroidManifest.xml:26:9-30:34
Merging library manifest /home/runner/.gradle/caches/transforms-4/d3fbe586a952ec492822bceff4881c03/transformed/jetified-tracing-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.tracing:tracing:1.0.0] AndroidManifest.xml:17:1-24:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.tracing:tracing:1.0.0] AndroidManifest.xml:20:5-22:41
Merging library manifest /home/runner/.gradle/caches/transforms-4/1ceac3d27c69c2b9b31614e00cd3abbc/transformed/interpolator-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.interpolator:interpolator:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.interpolator:interpolator:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/930d438abcc334a070edf8bd557626b3/transformed/documentfile-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.documentfile:documentfile:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.documentfile:documentfile:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/9beda206dd4b846a110859856608903e/transformed/localbroadcastmanager-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.localbroadcastmanager:localbroadcastmanager:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.localbroadcastmanager:localbroadcastmanager:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/d3c884fa679a06e3a691e54eed75e668/transformed/print-1.0.0/AndroidManifest.xml
Merging manifest with lower [androidx.print:print:1.0.0] AndroidManifest.xml:17:1-22:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.print:print:1.0.0] AndroidManifest.xml:20:5-44
Merging library manifest /home/runner/.gradle/caches/transforms-4/8c6400ddf911810a936ef14484f5be72/transformed/jetified-annotation-experimental-1.4.0/AndroidManifest.xml
Merging manifest with lower [androidx.annotation:annotation-experimental:1.4.0] AndroidManifest.xml:2:1-7:12
uses-sdk defined in both files...
Merging uses-sdk with lower [androidx.annotation:annotation-experimental:1.4.0] AndroidManifest.xml:5:5-44
Merging result: SUCCESS
1<?xml version="1.0" encoding="utf-8"?>
2<manifest xmlns:android="http://schemas.android.com/apk/res/android"
3    package="com.wdesign.wiseiptv.tv.debug"
4    android:versionCode="1"
5    android:versionName="2.0.0" >
6
7    <uses-sdk
8        android:minSdkVersion="21"
9        android:targetSdkVersion="34" />
10
11    <uses-permission android:name="android.permission.INTERNET" />
11-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:3:5-66
11-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:3:22-64
12    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
12-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:4:5-78
12-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:4:22-76
13    <uses-permission android:name="android.permission.WAKE_LOCK" />
13-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:5:5-67
13-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:5:22-65
14    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
14-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:6:5-80
14-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:6:22-78
15
16    <uses-feature
16-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:7:5-85
17        android:name="android.software.leanback"
17-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:7:19-59
18        android:required="true" />
18-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:7:60-83
19    <uses-feature
19-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:8:5-89
20        android:name="android.hardware.touchscreen"
20-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:8:19-62
21        android:required="false" />
21-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:8:63-87
22
23    <permission
23-->[androidx.core:core:1.13.0] /home/runner/.gradle/caches/transforms-4/da6dae1a7818fe5e5cec32306397e7da/transformed/core-1.13.0/AndroidManifest.xml:22:5-24:47
24        android:name="com.wdesign.wiseiptv.tv.debug.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION"
24-->[androidx.core:core:1.13.0] /home/runner/.gradle/caches/transforms-4/da6dae1a7818fe5e5cec32306397e7da/transformed/core-1.13.0/AndroidManifest.xml:23:9-81
25        android:protectionLevel="signature" />
25-->[androidx.core:core:1.13.0] /home/runner/.gradle/caches/transforms-4/da6dae1a7818fe5e5cec32306397e7da/transformed/core-1.13.0/AndroidManifest.xml:24:9-44
26
27    <uses-permission android:name="com.wdesign.wiseiptv.tv.debug.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION" />
27-->[androidx.core:core:1.13.0] /home/runner/.gradle/caches/transforms-4/da6dae1a7818fe5e5cec32306397e7da/transformed/core-1.13.0/AndroidManifest.xml:26:5-97
27-->[androidx.core:core:1.13.0] /home/runner/.gradle/caches/transforms-4/da6dae1a7818fe5e5cec32306397e7da/transformed/core-1.13.0/AndroidManifest.xml:26:22-94
28
29    <application
29-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:9:5-34:19
30        android:name="com.wdesign.wiseiptv.tv.WiseIptvTvApp"
30-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:10:9-38
31        android:allowBackup="false"
31-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:11:9-36
32        android:appComponentFactory="androidx.core.app.CoreComponentFactory"
32-->[androidx.core:core:1.13.0] /home/runner/.gradle/caches/transforms-4/da6dae1a7818fe5e5cec32306397e7da/transformed/core-1.13.0/AndroidManifest.xml:28:18-86
33        android:banner="@drawable/ic_channel_placeholder"
33-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:12:9-58
34        android:debuggable="true"
35        android:extractNativeLibs="true"
36        android:icon="@drawable/ic_channel_placeholder"
36-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:13:9-56
37        android:label="@string/app_name"
37-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:14:9-41
38        android:theme="@style/Theme.WiseIPTV.TV"
38-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:15:9-49
39        android:usesCleartextTraffic="true" >
39-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:16:9-44
40        <activity
40-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:17:9-24:20
41            android:name="com.wdesign.wiseiptv.tv.ui.TvMainActivity"
41-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:17:19-52
42            android:configChanges="orientation|keyboardHidden|screenSize"
42-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:19:13-74
43            android:exported="true"
43-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:18:13-36
44            android:launchMode="singleTask" >
44-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:18:37-68
45            <intent-filter>
45-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:20:13-23:29
46                <action android:name="android.intent.action.MAIN" />
46-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:21:17-68
46-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:21:25-66
47
48                <category android:name="android.intent.category.LEANBACK_LAUNCHER" />
48-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:22:17-85
48-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:22:27-83
49            </intent-filter>
50        </activity>
51        <activity
51-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:25:9-28:66
52            android:name="com.wdesign.wiseiptv.tv.ui.TvPlayerActivity"
52-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:25:19-54
53            android:configChanges="orientation|keyboardHidden|screenSize"
53-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:27:13-74
54            android:exported="false"
54-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:26:13-37
55            android:theme="@style/Theme.WiseIPTV.TV.Fullscreen" />
55-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:28:13-64
56
57        <receiver
57-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:29:9-33:20
58            android:name="com.wdesign.wiseiptv.tv.receiver.BootReceiver"
58-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:29:19-56
59            android:exported="true" >
59-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:29:57-80
60            <intent-filter>
60-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:30:13-32:29
61                <action android:name="android.intent.action.BOOT_COMPLETED" />
61-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:31:17-78
61-->/home/runner/work/wiseiptv/wiseiptv/tv/src/main/AndroidManifest.xml:31:25-76
62            </intent-filter>
63        </receiver>
64
65        <service
65-->[androidx.room:room-runtime:2.6.1] /home/runner/.gradle/caches/transforms-4/c25696574b9114c6991abe36517b309c/transformed/room-runtime-2.6.1/AndroidManifest.xml:24:9-28:63
66            android:name="androidx.room.MultiInstanceInvalidationService"
66-->[androidx.room:room-runtime:2.6.1] /home/runner/.gradle/caches/transforms-4/c25696574b9114c6991abe36517b309c/transformed/room-runtime-2.6.1/AndroidManifest.xml:25:13-74
67            android:directBootAware="true"
67-->[androidx.room:room-runtime:2.6.1] /home/runner/.gradle/caches/transforms-4/c25696574b9114c6991abe36517b309c/transformed/room-runtime-2.6.1/AndroidManifest.xml:26:13-43
68            android:exported="false" />
68-->[androidx.room:room-runtime:2.6.1] /home/runner/.gradle/caches/transforms-4/c25696574b9114c6991abe36517b309c/transformed/room-runtime-2.6.1/AndroidManifest.xml:27:13-37
69
70        <provider
70-->[androidx.emoji2:emoji2:1.3.0] /home/runner/.gradle/caches/transforms-4/822467cc6e4d69eef592a63d52fa54ca/transformed/jetified-emoji2-1.3.0/AndroidManifest.xml:24:9-32:20
71            android:name="androidx.startup.InitializationProvider"
71-->[androidx.emoji2:emoji2:1.3.0] /home/runner/.gradle/caches/transforms-4/822467cc6e4d69eef592a63d52fa54ca/transformed/jetified-emoji2-1.3.0/AndroidManifest.xml:25:13-67
72            android:authorities="com.wdesign.wiseiptv.tv.debug.androidx-startup"
72-->[androidx.emoji2:emoji2:1.3.0] /home/runner/.gradle/caches/transforms-4/822467cc6e4d69eef592a63d52fa54ca/transformed/jetified-emoji2-1.3.0/AndroidManifest.xml:26:13-68
73            android:exported="false" >
73-->[androidx.emoji2:emoji2:1.3.0] /home/runner/.gradle/caches/transforms-4/822467cc6e4d69eef592a63d52fa54ca/transformed/jetified-emoji2-1.3.0/AndroidManifest.xml:27:13-37
74            <meta-data
74-->[androidx.emoji2:emoji2:1.3.0] /home/runner/.gradle/caches/transforms-4/822467cc6e4d69eef592a63d52fa54ca/transformed/jetified-emoji2-1.3.0/AndroidManifest.xml:29:13-31:52
75                android:name="androidx.emoji2.text.EmojiCompatInitializer"
75-->[androidx.emoji2:emoji2:1.3.0] /home/runner/.gradle/caches/transforms-4/822467cc6e4d69eef592a63d52fa54ca/transformed/jetified-emoji2-1.3.0/AndroidManifest.xml:30:17-75
76                android:value="androidx.startup" />
76-->[androidx.emoji2:emoji2:1.3.0] /home/runner/.gradle/caches/transforms-4/822467cc6e4d69eef592a63d52fa54ca/transformed/jetified-emoji2-1.3.0/AndroidManifest.xml:31:17-49
77            <meta-data
77-->[androidx.lifecycle:lifecycle-process:2.7.0] /home/runner/.gradle/caches/transforms-4/e700a3f752763e22f0c7b8dbc23972c3/transformed/jetified-lifecycle-process-2.7.0/AndroidManifest.xml:29:13-31:52
78                android:name="androidx.lifecycle.ProcessLifecycleInitializer"
78-->[androidx.lifecycle:lifecycle-process:2.7.0] /home/runner/.gradle/caches/transforms-4/e700a3f752763e22f0c7b8dbc23972c3/transformed/jetified-lifecycle-process-2.7.0/AndroidManifest.xml:30:17-78
79                android:value="androidx.startup" />
79-->[androidx.lifecycle:lifecycle-process:2.7.0] /home/runner/.gradle/caches/transforms-4/e700a3f752763e22f0c7b8dbc23972c3/transformed/jetified-lifecycle-process-2.7.0/AndroidManifest.xml:31:17-49
80            <meta-data
80-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:29:13-31:52
81                android:name="androidx.profileinstaller.ProfileInstallerInitializer"
81-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:30:17-85
82                android:value="androidx.startup" />
82-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:31:17-49
83        </provider>
84
85        <receiver
85-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:34:9-52:20
86            android:name="androidx.profileinstaller.ProfileInstallReceiver"
86-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:35:13-76
87            android:directBootAware="false"
87-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:36:13-44
88            android:enabled="true"
88-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:37:13-35
89            android:exported="true"
89-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:38:13-36
90            android:permission="android.permission.DUMP" >
90-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:39:13-57
91            <intent-filter>
91-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:40:13-42:29
92                <action android:name="androidx.profileinstaller.action.INSTALL_PROFILE" />
92-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:41:17-91
92-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:41:25-88
93            </intent-filter>
94            <intent-filter>
94-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:43:13-45:29
95                <action android:name="androidx.profileinstaller.action.SKIP_FILE" />
95-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:44:17-85
95-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:44:25-82
96            </intent-filter>
97            <intent-filter>
97-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:46:13-48:29
98                <action android:name="androidx.profileinstaller.action.SAVE_PROFILE" />
98-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:47:17-88
98-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:47:25-85
99            </intent-filter>
100            <intent-filter>
100-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:49:13-51:29
101                <action android:name="androidx.profileinstaller.action.BENCHMARK_OPERATION" />
101-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:50:17-95
101-->[androidx.profileinstaller:profileinstaller:1.3.1] /home/runner/.gradle/caches/transforms-4/1f8c96679d3e4584c6b8a7b28d114773/transformed/jetified-profileinstaller-1.3.1/AndroidManifest.xml:50:25-92
102            </intent-filter>
103        </receiver>
104    </application>
105
106</manifest>

Merged manifest saved to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_manifest/debug/processDebugMainManifest/AndroidManifest.xml
Resolve mutations for :tv:processDebugManifest (Thread[Execution worker,5,main]) started.
:tv:processDebugManifest (Thread[Execution worker,5,main]) started.

> Task :tv:mergeDebugResources
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-hdpi-v4/values-hdpi-v4.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-hdpi-v4_values-hdpi-v4.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-or/values-or.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-or_values-or.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-es/values-es.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-es_values-es.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-pa/values-pa.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-pa_values-pa.arsc.flat
Compiling xml file /home/runner/work/wiseiptv/wiseiptv/tv/src/main/res/layout/dialog_add_playlist.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/layout_dialog_add_playlist.xml.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-w400dp-port-v13/values-w400dp-port-v13.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-w400dp-port-v13_values-w400dp-port-v13.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-night-v8/values-night-v8.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-night-v8_values-night-v8.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-zh-rCN/values-zh-rCN.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-zh-rCN_values-zh-rCN.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-large-v4/values-large-v4.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-large-v4_values-large-v4.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-small-v4/values-small-v4.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-small-v4_values-small-v4.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-gu/values-gu.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-gu_values-gu.arsc.flat
Compiling xml file /home/runner/work/wiseiptv/wiseiptv/tv/src/main/res/drawable/ic_channel_placeholder.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/drawable_ic_channel_placeholder.xml.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-watch-v20/values-watch-v20.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-watch-v20_values-watch-v20.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-h320dp-port-v13/values-h320dp-port-v13.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-h320dp-port-v13_values-h320dp-port-v13.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ldltr-v21/values-ldltr-v21.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ldltr-v21_values-ldltr-v21.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-in/values-in.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-in_values-in.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-it/values-it.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-it_values-it.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ar/values-ar.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ar_values-ar.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-v23/values-v23.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-v23_values-v23.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-az/values-az.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-az_values-az.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-zu/values-zu.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-zu_values-zu.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-v28/values-v28.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-v28_values-v28.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-bg/values-bg.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-bg_values-bg.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-v31/values-v31.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-v31_values-v31.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-sk/values-sk.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-sk_values-sk.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-sr/values-sr.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-sr_values-sr.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-sv/values-sv.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-sv_values-sv.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-ky/values-ky.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-ky_values-ky.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-te/values-te.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-te_values-te.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-da/values-da.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-da_values-da.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-lo/values-lo.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-lo_values-lo.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-h550dp-port-v13/values-h550dp-port-v13.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-h550dp-port-v13_values-h550dp-port-v13.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-mk/values-mk.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-mk_values-mk.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-mr/values-mr.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-mr_values-mr.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-zh-rTW/values-zh-rTW.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-zh-rTW_values-zh-rTW.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-eu/values-eu.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-eu_values-eu.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-port/values-port.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-port_values-port.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-fa/values-fa.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-fa_values-fa.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-en-rXC/values-en-rXC.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-en-rXC_values-en-rXC.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-en-rGB/values-en-rGB.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-en-rGB_values-en-rGB.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-gl/values-gl.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-gl_values-gl.arsc.flat
Compiling XML table /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/debug/mergeDebugResources/merged.dir/values-pl/values-pl.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/values-pl_values-pl.arsc.flat
Compiling xml file /home/runner/work/wiseiptv/wiseiptv/tv/src/main/res/layout/activity_tv_main.xml to /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/merged_res/debug/mergeDebugResources/layout_activity_tv_main.xml.flat

> Task :tv:processDebugManifest
Caching disabled for task ':tv:processDebugManifest' because:
  Build cache is disabled
Task ':tv:processDebugManifest' is not up-to-date because:
  No history is available.
Resolve mutations for :tv:processDebugManifestForPackage (Thread[Execution worker Thread 2,5,main]) started.
:tv:processDebugManifestForPackage (Thread[Execution worker Thread 2,5,main]) started.
Resolve mutations for :tv:compressDebugAssets (Thread[Execution worker,5,main]) started.
:tv:compressDebugAssets (Thread[Execution worker Thread 3,5,main]) started.

> Task :tv:compressDebugAssets
Caching disabled for task ':tv:compressDebugAssets' because:
  Build cache is disabled
Task ':tv:compressDebugAssets' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':tv:compressDebugAssets'.
Resolve mutations for :core:bundleLibRuntimeToJarDebug (Thread[Execution worker Thread 3,5,main]) started.
:core:bundleLibRuntimeToJarDebug (Thread[Execution worker Thread 3,5,main]) started.
Resolve mutations for :tv:desugarDebugFileDependencies (Thread[Execution worker,5,main]) started.
:tv:desugarDebugFileDependencies (Thread[Execution worker,5,main]) started.

> Task :core:bundleLibRuntimeToJarDebug
Caching disabled for task ':core:bundleLibRuntimeToJarDebug' because:
  Build cache is disabled
Task ':core:bundleLibRuntimeToJarDebug' is not up-to-date because:
  No history is available.
work action resolve classes.jar (project :core) (Thread[Execution worker Thread 3,5,main]) started.
DexingWithClasspathTransform (Thread[Execution worker Thread 3,5,main]) started.

> Task :tv:desugarDebugFileDependencies
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with JetifyTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with ExtractAarTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with AarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with JetifyTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with ExtractAarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with AarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with JetifyTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with AarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with JetifyTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with AarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with JetifyTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with AarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with JetifyTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with ExtractAarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with AarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with JetifyTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with ExtractAarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with AarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with JetifyTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with ExtractAarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with AarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with JetifyTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with ExtractAarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with AarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with JetifyTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with ExtractAarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with AarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with JetifyTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with ExtractAarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with AarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with JetifyTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with ExtractAarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with AarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with JetifyTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with ExtractAarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with AarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with JetifyTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with ExtractAarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with AarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with JetifyTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with ExtractAarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with AarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with JetifyTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with ExtractAarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with AarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with JetifyTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with ExtractAarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with AarTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with JetifyTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with JetifyTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with ExtractAarTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with AarTransform
Transforming okhttp-4.12.0.jar (com.squareup.okhttp3:okhttp:4.12.0) with JetifyTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with JetifyTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with ExtractAarTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with AarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with JetifyTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with ExtractAarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with AarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with JetifyTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with AarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with JetifyTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with ExtractAarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with AarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with JetifyTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with ExtractAarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with AarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with JetifyTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with ExtractAarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with AarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with JetifyTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with ExtractAarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with AarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0 because:
  Build cache is disabled
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with JetifyTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with ExtractAarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with AarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with JetifyTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with ExtractAarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with AarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with JetifyTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with ExtractAarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with AarTransform
Transforming lifecycle-common-2.7.0.jar (androidx.lifecycle:lifecycle-common:2.7.0) with JetifyTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with JetifyTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with ExtractAarTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with AarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with AarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with AarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with AarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with ExtractAarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with AarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with JetifyTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with ExtractAarTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with JetifyTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/9346ab6aafc40c73d868f8cb25ad6eda/transformed/constraintlayout-2.0.1 because:
  Build cache is disabled
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with ExtractAarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with AarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with JetifyTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with ExtractAarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with JetifyTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/a97fd6e266fa9af37bff1418f096f875/transformed/legacy-support-core-ui-1.0.0 because:
  Build cache is disabled
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with ExtractAarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with AarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with JetifyTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with ExtractAarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with AarTransform
Transforming resourceinspection-annotation-1.0.1.jar (androidx.resourceinspection:resourceinspection-annotation:1.0.1) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with JetifyTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with ExtractAarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with AarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with JetifyTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with ExtractAarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with AarTransform
Transforming collection-1.2.0.jar (androidx.collection:collection:1.2.0) with JetifyTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with JetifyTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with ExtractAarTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with AarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with JetifyTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with ExtractAarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with AarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with JetifyTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with ExtractAarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with AarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with JetifyTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with ExtractAarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with AarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with JetifyTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with ExtractAarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with AarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with JetifyTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with ExtractAarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with AarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with JetifyTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with ExtractAarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with AarTransform
Transforming core-common-2.2.0.jar (androidx.arch.core:core-common:2.2.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming concurrent-futures-1.1.0.jar (androidx.concurrent:concurrent-futures:1.1.0) with JetifyTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with JetifyTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with ExtractAarTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with AarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with JetifyTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with ExtractAarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with AarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with JetifyTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with ExtractAarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with AarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with JetifyTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with ExtractAarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with AarTransform
Transforming annotation-jvm-1.6.0.jar (androidx.annotation:annotation-jvm:1.6.0) with JetifyTransform
Transforming okio-jvm-3.6.0.jar (com.squareup.okio:okio-jvm:3.6.0) with JetifyTransform
Transforming kotlinx-coroutines-android-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1) with JetifyTransform
Transforming kotlinx-coroutines-core-jvm-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1) with JetifyTransform
Transforming kotlin-stdlib-jdk8-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-jdk7-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.10) with JetifyTransform
Transforming guava-32.1.3-android.jar (com.google.guava:guava:32.1.3-android) with JetifyTransform
Transforming disklrucache-4.16.0.jar (com.github.bumptech.glide:disklrucache:4.16.0) with JetifyTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with JetifyTransform
Transforming kotlin-stdlib-common-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10) with JetifyTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with JetifyTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with JetifyTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with JetifyTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/f898a674e9cfed3d540fe9e2a8b40e57/transformed/slidingpanelayout-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/35cf7e22f9d1f043d812a7c3bcc02ec9/transformed/asynclayoutinflater-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/f846b076b9a99945a2694c3a76bf6d97/transformed/swiperefreshlayout-1.0.0 because:
  Build cache is disabled
Transforming error_prone_annotations-2.15.0.jar (com.google.errorprone:error_prone_annotations:2.15.0) with JetifyTransform
Downloading https://dl.google.com/dl/android/maven2/androidx/constraintlayout/constraintlayout-solver/2.0.1/constraintlayout-solver-2.0.1.jar to /home/runner/.gradle/.tmp/gradle_download293269383997413320bin
Transforming constraintlayout-solver-2.0.1.jar (androidx.constraintlayout:constraintlayout-solver:2.0.1) with JetifyTransform
Caching disabled for JetifyTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.constraintlayout/constraintlayout-solver/2.0.1/30988fe2d77f3fe3bf7551bb8a8b795fad7e7226/constraintlayout-solver-2.0.1.jar because:
  Build cache is disabled
Caching disabled for task ':tv:desugarDebugFileDependencies' because:
  Build cache is disabled
Task ':tv:desugarDebugFileDependencies' is not up-to-date because:
  No history is available.
Resolve mutations for :core:processDebugJavaRes (Thread[Execution worker,5,main]) started.
:core:processDebugJavaRes (Thread[Execution worker,5,main]) started.

> Task :core:processDebugJavaRes
file or directory '/home/runner/work/wiseiptv/wiseiptv/core/src/main/resources', not found
file or directory '/home/runner/work/wiseiptv/wiseiptv/core/src/debug/resources', not found
file or directory '/home/runner/work/wiseiptv/wiseiptv/core/src/main/resources', not found
file or directory '/home/runner/work/wiseiptv/wiseiptv/core/src/debug/resources', not found
Caching disabled for task ':core:processDebugJavaRes' because:
  Build cache is disabled
Task ':core:processDebugJavaRes' is not up-to-date because:
  No history is available.
file or directory '/home/runner/work/wiseiptv/wiseiptv/core/src/main/resources', not found
file or directory '/home/runner/work/wiseiptv/wiseiptv/core/src/debug/resources', not found
file or directory '/home/runner/work/wiseiptv/wiseiptv/core/src/main/resources', not found
file or directory '/home/runner/work/wiseiptv/wiseiptv/core/src/debug/resources', not found
work action resolve out (project :core) (Thread[Execution worker,5,main]) started.
Resolve mutations for :tv:processDebugJavaRes (Thread[Execution worker,5,main]) started.
:tv:processDebugJavaRes (Thread[Execution worker,5,main]) started.

> Task :tv:processDebugJavaRes NO-SOURCE
file or directory '/home/runner/work/wiseiptv/wiseiptv/tv/src/main/resources', not found
file or directory '/home/runner/work/wiseiptv/wiseiptv/tv/src/debug/resources', not found
Skipping task ':tv:processDebugJavaRes' as it has no source files and no previous output files.
Resolve mutations for :tv:mergeDebugJavaResource (Thread[Execution worker,5,main]) started.
:tv:mergeDebugJavaResource (Thread[Execution worker,5,main]) started.
Resolve mutations for :tv:checkDebugDuplicateClasses (Thread[included builds,5,main]) started.
:tv:checkDebugDuplicateClasses (Thread[included builds,5,main]) started.

> Task :tv:processDebugManifestForPackage
Caching disabled for task ':tv:processDebugManifestForPackage' because:
  Build cache is disabled
Task ':tv:processDebugManifestForPackage' is not up-to-date because:
  No history is available.
Resolve mutations for :tv:processDebugResources (Thread[Execution worker Thread 2,5,main]) started.
:tv:processDebugResources (Thread[Execution worker Thread 2,5,main]) started.

> Task :tv:checkDebugDuplicateClasses
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with JetifyTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with AarToClassTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with JetifyTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with AarToClassTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with JetifyTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with AarToClassTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with EnumerateClassesTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with JetifyTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with AarToClassTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with EnumerateClassesTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with JetifyTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with AarToClassTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with EnumerateClassesTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with JetifyTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with AarToClassTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with EnumerateClassesTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with JetifyTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with AarToClassTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with EnumerateClassesTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with JetifyTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with AarToClassTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with EnumerateClassesTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with JetifyTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with AarToClassTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with EnumerateClassesTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with JetifyTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with AarToClassTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with EnumerateClassesTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with JetifyTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with AarToClassTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with EnumerateClassesTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with JetifyTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with AarToClassTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with EnumerateClassesTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with EnumerateClassesTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with JetifyTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with AarToClassTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with EnumerateClassesTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with JetifyTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with AarToClassTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarToClassTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with EnumerateClassesTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarToClassTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with EnumerateClassesTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with JetifyTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with AarToClassTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with JetifyTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with AarToClassTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with EnumerateClassesTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with JetifyTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with AarToClassTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with EnumerateClassesTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with JetifyTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with IdentityTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with EnumerateClassesTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with JetifyTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with AarToClassTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with EnumerateClassesTransform
Transforming okhttp-4.12.0.jar (com.squareup.okhttp3:okhttp:4.12.0) with JetifyTransform
Transforming okhttp-4.12.0.jar (com.squareup.okhttp3:okhttp:4.12.0) with IdentityTransform
Transforming okhttp-4.12.0.jar (com.squareup.okhttp3:okhttp:4.12.0) with EnumerateClassesTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with JetifyTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with AarToClassTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with EnumerateClassesTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarToClassTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with EnumerateClassesTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarToClassTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with EnumerateClassesTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with JetifyTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with AarToClassTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with EnumerateClassesTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with JetifyTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with AarToClassTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarToClassTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarToClassTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarToClassTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with EnumerateClassesTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarToClassTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with EnumerateClassesTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with JetifyTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with AarToClassTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with JetifyTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with AarToClassTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with JetifyTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with AarToClassTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with EnumerateClassesTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarToClassTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with EnumerateClassesTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarToClassTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with EnumerateClassesTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with JetifyTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with AarToClassTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with EnumerateClassesTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with JetifyTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with AarToClassTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with EnumerateClassesTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with JetifyTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with AarToClassTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with EnumerateClassesTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with JetifyTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with AarToClassTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with EnumerateClassesTransform
Transforming lifecycle-common-2.7.0.jar (androidx.lifecycle:lifecycle-common:2.7.0) with JetifyTransform
Transforming lifecycle-common-2.7.0.jar (androidx.lifecycle:lifecycle-common:2.7.0) with IdentityTransform
Transforming lifecycle-common-2.7.0.jar (androidx.lifecycle:lifecycle-common:2.7.0) with EnumerateClassesTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with JetifyTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with AarToClassTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with EnumerateClassesTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with AarToClassTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with EnumerateClassesTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with AarToClassTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with EnumerateClassesTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarToClassTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarToClassTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarToClassTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with EnumerateClassesTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarToClassTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with EnumerateClassesTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarToClassTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with EnumerateClassesTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarToClassTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with EnumerateClassesTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with AarToClassTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with EnumerateClassesTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with AarToClassTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with EnumerateClassesTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarToClassTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with EnumerateClassesTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarToClassTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with EnumerateClassesTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarToClassTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with EnumerateClassesTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarToClassTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with EnumerateClassesTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with JetifyTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with EnumerateClassesTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with EnumerateClassesTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with EnumerateClassesTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with EnumerateClassesTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with EnumerateClassesTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with EnumerateClassesTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with JetifyTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with AarToClassTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with JetifyTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with EnumerateClassesTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with EnumerateClassesTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with EnumerateClassesTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with EnumerateClassesTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with EnumerateClassesTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with EnumerateClassesTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with EnumerateClassesTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with EnumerateClassesTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with EnumerateClassesTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with EnumerateClassesTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with EnumerateClassesTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with EnumerateClassesTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with EnumerateClassesTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with EnumerateClassesTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with EnumerateClassesTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with JetifyTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with AarToClassTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with EnumerateClassesTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarToClassTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with EnumerateClassesTransform
Caching disabled for AarToClassTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.constraintlayout/constraintlayout/2.0.1/fab633b1873e5c1fbe49d919fab85ed1f8cf47ca/constraintlayout-2.0.1.aar because:
  Build cache is disabled
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarToClassTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with EnumerateClassesTransform
Transforming error_prone_annotations-2.15.0.jar (com.google.errorprone:error_prone_annotations:2.15.0) with JetifyTransform
Transforming error_prone_annotations-2.15.0.jar (com.google.errorprone:error_prone_annotations:2.15.0) with IdentityTransform
Transforming error_prone_annotations-2.15.0.jar (com.google.errorprone:error_prone_annotations:2.15.0) with EnumerateClassesTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with JetifyTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with AarToClassTransform
Transforming resourceinspection-annotation-1.0.1.jar (androidx.resourceinspection:resourceinspection-annotation:1.0.1) with JetifyTransform
Transforming resourceinspection-annotation-1.0.1.jar (androidx.resourceinspection:resourceinspection-annotation:1.0.1) with IdentityTransform
Transforming resourceinspection-annotation-1.0.1.jar (androidx.resourceinspection:resourceinspection-annotation:1.0.1) with EnumerateClassesTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarToClassTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with EnumerateClassesTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarToClassTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with EnumerateClassesTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with JetifyTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with AarToClassTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with EnumerateClassesTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with JetifyTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with AarToClassTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with EnumerateClassesTransform
Transforming collection-1.2.0.jar (androidx.collection:collection:1.2.0) with JetifyTransform
Transforming collection-1.2.0.jar (androidx.collection:collection:1.2.0) with IdentityTransform
Transforming collection-1.2.0.jar (androidx.collection:collection:1.2.0) with EnumerateClassesTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with JetifyTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with AarToClassTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with EnumerateClassesTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with JetifyTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with AarToClassTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with EnumerateClassesTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with JetifyTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with AarToClassTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with EnumerateClassesTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with JetifyTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with AarToClassTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with EnumerateClassesTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with JetifyTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with AarToClassTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with EnumerateClassesTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with JetifyTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with AarToClassTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with EnumerateClassesTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with JetifyTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with AarToClassTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with EnumerateClassesTransform
Transforming core-common-2.2.0.jar (androidx.arch.core:core-common:2.2.0) with JetifyTransform
Transforming core-common-2.2.0.jar (androidx.arch.core:core-common:2.2.0) with IdentityTransform
Transforming core-common-2.2.0.jar (androidx.arch.core:core-common:2.2.0) with EnumerateClassesTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarToClassTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with EnumerateClassesTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarToClassTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with EnumerateClassesTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarToClassTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with EnumerateClassesTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarToClassTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with EnumerateClassesTransform
Transforming concurrent-futures-1.1.0.jar (androidx.concurrent:concurrent-futures:1.1.0) with JetifyTransform
Transforming concurrent-futures-1.1.0.jar (androidx.concurrent:concurrent-futures:1.1.0) with IdentityTransform
Transforming concurrent-futures-1.1.0.jar (androidx.concurrent:concurrent-futures:1.1.0) with EnumerateClassesTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with JetifyTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with AarToClassTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with JetifyTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with AarToClassTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with JetifyTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with AarToClassTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with JetifyTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with AarToClassTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with EnumerateClassesTransform
Transforming annotation-jvm-1.6.0.jar (androidx.annotation:annotation-jvm:1.6.0) with JetifyTransform
Transforming annotation-jvm-1.6.0.jar (androidx.annotation:annotation-jvm:1.6.0) with IdentityTransform
Transforming annotation-jvm-1.6.0.jar (androidx.annotation:annotation-jvm:1.6.0) with EnumerateClassesTransform
Transforming okio-jvm-3.6.0.jar (com.squareup.okio:okio-jvm:3.6.0) with JetifyTransform
Transforming okio-jvm-3.6.0.jar (com.squareup.okio:okio-jvm:3.6.0) with IdentityTransform
Transforming okio-jvm-3.6.0.jar (com.squareup.okio:okio-jvm:3.6.0) with EnumerateClassesTransform
Transforming kotlinx-coroutines-android-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1) with JetifyTransform
Transforming kotlinx-coroutines-android-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1) with IdentityTransform
Transforming kotlinx-coroutines-android-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1) with EnumerateClassesTransform
Transforming kotlinx-coroutines-core-jvm-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1) with JetifyTransform
Transforming kotlinx-coroutines-core-jvm-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1) with IdentityTransform
Transforming kotlinx-coroutines-core-jvm-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1) with EnumerateClassesTransform
Transforming kotlin-stdlib-jdk8-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-jdk8-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10) with IdentityTransform
Transforming kotlin-stdlib-jdk8-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10) with EnumerateClassesTransform
Transforming kotlin-stdlib-jdk7-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-jdk7-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10) with IdentityTransform
Transforming kotlin-stdlib-jdk7-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10) with EnumerateClassesTransform
Transforming kotlin-stdlib-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.10) with IdentityTransform
Transforming kotlin-stdlib-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.10) with EnumerateClassesTransform
Transforming guava-32.1.3-android.jar (com.google.guava:guava:32.1.3-android) with JetifyTransform
Transforming guava-32.1.3-android.jar (com.google.guava:guava:32.1.3-android) with IdentityTransform
Transforming guava-32.1.3-android.jar (com.google.guava:guava:32.1.3-android) with EnumerateClassesTransform
Transforming disklrucache-4.16.0.jar (com.github.bumptech.glide:disklrucache:4.16.0) with JetifyTransform
Transforming disklrucache-4.16.0.jar (com.github.bumptech.glide:disklrucache:4.16.0) with IdentityTransform
Transforming disklrucache-4.16.0.jar (com.github.bumptech.glide:disklrucache:4.16.0) with EnumerateClassesTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with JetifyTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with IdentityTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with EnumerateClassesTransform
Transforming kotlin-stdlib-common-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-common-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10) with IdentityTransform
Transforming kotlin-stdlib-common-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10) with EnumerateClassesTransform
Transforming constraintlayout-solver-2.0.1.jar (androidx.constraintlayout:constraintlayout-solver:2.0.1) with JetifyTransform
Transforming constraintlayout-solver-2.0.1.jar (androidx.constraintlayout:constraintlayout-solver:2.0.1) with IdentityTransform
Transforming constraintlayout-solver-2.0.1.jar (androidx.constraintlayout:constraintlayout-solver:2.0.1) with EnumerateClassesTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with JetifyTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with IdentityTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with EnumerateClassesTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with JetifyTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with IdentityTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with EnumerateClassesTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with JetifyTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with IdentityTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with EnumerateClassesTransform
Caching disabled for AarToClassTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.legacy/legacy-support-core-ui/1.0.0/61a264f996046e059f889914050fae1e75d3b702/legacy-support-core-ui-1.0.0.aar because:
  Build cache is disabled
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with EnumerateClassesTransform
Caching disabled for EnumerateClassesTransform: /home/runner/.gradle/caches/transforms-4/d88407966df0a8618d4d25c523ba9d9d/transformed/legacy-support-core-ui-1.0.0-runtime.jar because:
  Build cache is disabled
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with EnumerateClassesTransform
Caching disabled for EnumerateClassesTransform: /home/runner/.gradle/caches/transforms-4/90ef22543e81f4ccb6c669c80980b80f/transformed/constraintlayout-2.0.1-runtime.jar because:
  Build cache is disabled
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with EnumerateClassesTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with EnumerateClassesTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with EnumerateClassesTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with EnumerateClassesTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with EnumerateClassesTransform
Caching disabled for AarToClassTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.slidingpanelayout/slidingpanelayout/1.0.0/37eba9ccbf09b75cc4aa78a5e182d5b8ba79ad6a/slidingpanelayout-1.0.0.aar because:
  Build cache is disabled
Caching disabled for AarToClassTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.swiperefreshlayout/swiperefreshlayout/1.0.0/4fd265b80a2b0fbeb062ab2bc4b1487521507762/swiperefreshlayout-1.0.0.aar because:
  Build cache is disabled
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with EnumerateClassesTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with EnumerateClassesTransform
Caching disabled for EnumerateClassesTransform: /home/runner/.gradle/caches/transforms-4/debb72ba00d99df688162734bb4f43a4/transformed/slidingpanelayout-1.0.0-runtime.jar because:
  Build cache is disabled
Caching disabled for EnumerateClassesTransform: /home/runner/.gradle/caches/transforms-4/460c86aa89d9e29fa2d63c24ec7f8a82/transformed/swiperefreshlayout-1.0.0-runtime.jar because:
  Build cache is disabled
Caching disabled for AarToClassTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.asynclayoutinflater/asynclayoutinflater/1.0.0/5ffa788d19a6863799f25cb50d4fdfb0ec649037/asynclayoutinflater-1.0.0.aar because:
  Build cache is disabled
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with EnumerateClassesTransform
Caching disabled for EnumerateClassesTransform: /home/runner/.gradle/caches/transforms-4/0aa8cb039e665d777cabd3dae93496f6/transformed/asynclayoutinflater-1.0.0-runtime.jar because:
  Build cache is disabled
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with EnumerateClassesTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with EnumerateClassesTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with EnumerateClassesTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with EnumerateClassesTransform
Caching disabled for AarToClassTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.leanback/leanback/1.0.0/65025d699df3d9f98e830de9c16fb29cdf348759/leanback-1.0.0.aar because:
  Build cache is disabled
Caching disabled for EnumerateClassesTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.constraintlayout/constraintlayout-solver/2.0.1/30988fe2d77f3fe3bf7551bb8a8b795fad7e7226/constraintlayout-solver-2.0.1.jar because:
  Build cache is disabled
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with EnumerateClassesTransform
Caching disabled for EnumerateClassesTransform: /home/runner/.gradle/caches/transforms-4/764d721b97d482dbebc01a5a33219f1a/transformed/leanback-1.0.0-runtime.jar because:
  Build cache is disabled
Caching disabled for task ':tv:checkDebugDuplicateClasses' because:
  Build cache is disabled
Task ':tv:checkDebugDuplicateClasses' is not up-to-date because:
  No history is available.

> Task :tv:mergeDebugJavaResource
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with JetifyTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with ExtractAarTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with AarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with JetifyTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with ExtractAarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with AarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with JetifyTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with AarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with JetifyTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with AarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with JetifyTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with AarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with JetifyTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with ExtractAarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with AarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with JetifyTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with ExtractAarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with AarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with JetifyTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with ExtractAarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with AarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with JetifyTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with ExtractAarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with AarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with JetifyTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with ExtractAarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with AarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with JetifyTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with ExtractAarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with AarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with JetifyTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with ExtractAarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with AarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with JetifyTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with ExtractAarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with AarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with JetifyTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with ExtractAarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with AarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with JetifyTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with ExtractAarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with AarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with JetifyTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with ExtractAarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with AarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with JetifyTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with ExtractAarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with AarTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with JetifyTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with IdentityTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with JetifyTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0 because:
  Build cache is disabled
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with ExtractAarTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with AarTransform
Transforming okhttp-4.12.0.jar (com.squareup.okhttp3:okhttp:4.12.0) with JetifyTransform
Transforming okhttp-4.12.0.jar (com.squareup.okhttp3:okhttp:4.12.0) with IdentityTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with JetifyTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with ExtractAarTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with AarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with JetifyTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with ExtractAarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with AarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with JetifyTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with AarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with JetifyTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with ExtractAarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with AarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with JetifyTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with ExtractAarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with AarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/9346ab6aafc40c73d868f8cb25ad6eda/transformed/constraintlayout-2.0.1 because:
  Build cache is disabled
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with JetifyTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with ExtractAarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with AarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with JetifyTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with ExtractAarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with AarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with JetifyTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with ExtractAarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with AarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with JetifyTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with ExtractAarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with AarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with JetifyTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with ExtractAarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with AarTransform
Transforming lifecycle-common-2.7.0.jar (androidx.lifecycle:lifecycle-common:2.7.0) with JetifyTransform
Transforming lifecycle-common-2.7.0.jar (androidx.lifecycle:lifecycle-common:2.7.0) with IdentityTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with JetifyTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with ExtractAarTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with AarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with AarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with AarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with ExtractAarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/a97fd6e266fa9af37bff1418f096f875/transformed/legacy-support-core-ui-1.0.0 because:
  Build cache is disabled
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with AarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with ExtractAarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with AarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with JetifyTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with ExtractAarTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with JetifyTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with ExtractAarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with AarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with JetifyTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with ExtractAarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with JetifyTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with ExtractAarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with AarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarTransform
Transforming error_prone_annotations-2.15.0.jar (com.google.errorprone:error_prone_annotations:2.15.0) with JetifyTransform
Transforming error_prone_annotations-2.15.0.jar (com.google.errorprone:error_prone_annotations:2.15.0) with IdentityTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with JetifyTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with ExtractAarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with AarTransform
Transforming resourceinspection-annotation-1.0.1.jar (androidx.resourceinspection:resourceinspection-annotation:1.0.1) with JetifyTransform
Transforming resourceinspection-annotation-1.0.1.jar (androidx.resourceinspection:resourceinspection-annotation:1.0.1) with IdentityTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with JetifyTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with ExtractAarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with AarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with JetifyTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with ExtractAarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with AarTransform
Transforming collection-1.2.0.jar (androidx.collection:collection:1.2.0) with JetifyTransform
Transforming collection-1.2.0.jar (androidx.collection:collection:1.2.0) with IdentityTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with JetifyTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with ExtractAarTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with AarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with JetifyTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with ExtractAarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with AarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with JetifyTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with ExtractAarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with AarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with JetifyTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with ExtractAarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with AarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with JetifyTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with ExtractAarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with AarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with JetifyTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with ExtractAarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with AarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with JetifyTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with ExtractAarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with AarTransform
Transforming core-common-2.2.0.jar (androidx.arch.core:core-common:2.2.0) with JetifyTransform
Transforming core-common-2.2.0.jar (androidx.arch.core:core-common:2.2.0) with IdentityTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/f898a674e9cfed3d540fe9e2a8b40e57/transformed/slidingpanelayout-1.0.0 because:
  Build cache is disabled
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/f846b076b9a99945a2694c3a76bf6d97/transformed/swiperefreshlayout-1.0.0 because:
  Build cache is disabled
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarTransform
Transforming concurrent-futures-1.1.0.jar (androidx.concurrent:concurrent-futures:1.1.0) with JetifyTransform
Transforming concurrent-futures-1.1.0.jar (androidx.concurrent:concurrent-futures:1.1.0) with IdentityTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with JetifyTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with ExtractAarTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with AarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with JetifyTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with ExtractAarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with AarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with JetifyTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with ExtractAarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with AarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with JetifyTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with ExtractAarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with AarTransform
Transforming annotation-jvm-1.6.0.jar (androidx.annotation:annotation-jvm:1.6.0) with JetifyTransform
Transforming annotation-jvm-1.6.0.jar (androidx.annotation:annotation-jvm:1.6.0) with IdentityTransform
Transforming okio-jvm-3.6.0.jar (com.squareup.okio:okio-jvm:3.6.0) with JetifyTransform
Transforming okio-jvm-3.6.0.jar (com.squareup.okio:okio-jvm:3.6.0) with IdentityTransform
Transforming kotlinx-coroutines-android-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1) with JetifyTransform
Transforming kotlinx-coroutines-android-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1) with IdentityTransform
Transforming kotlinx-coroutines-core-jvm-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1) with JetifyTransform
Caching disabled for AarTransform: /home/runner/.gradle/caches/transforms-4/35cf7e22f9d1f043d812a7c3bcc02ec9/transformed/asynclayoutinflater-1.0.0 because:
  Build cache is disabled
Transforming kotlinx-coroutines-core-jvm-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1) with IdentityTransform
Transforming kotlin-stdlib-jdk8-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-jdk8-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10) with IdentityTransform
Transforming kotlin-stdlib-jdk7-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-jdk7-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10) with IdentityTransform
Transforming kotlin-stdlib-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.10) with IdentityTransform
Transforming guava-32.1.3-android.jar (com.google.guava:guava:32.1.3-android) with JetifyTransform
Transforming guava-32.1.3-android.jar (com.google.guava:guava:32.1.3-android) with IdentityTransform
Transforming disklrucache-4.16.0.jar (com.github.bumptech.glide:disklrucache:4.16.0) with JetifyTransform
Transforming disklrucache-4.16.0.jar (com.github.bumptech.glide:disklrucache:4.16.0) with IdentityTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with JetifyTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with IdentityTransform
Transforming kotlin-stdlib-common-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-common-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10) with IdentityTransform
Transforming constraintlayout-solver-2.0.1.jar (androidx.constraintlayout:constraintlayout-solver:2.0.1) with JetifyTransform
Transforming constraintlayout-solver-2.0.1.jar (androidx.constraintlayout:constraintlayout-solver:2.0.1) with IdentityTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with JetifyTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with IdentityTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with JetifyTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with IdentityTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with JetifyTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with IdentityTransform
Caching disabled for IdentityTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.constraintlayout/constraintlayout-solver/2.0.1/30988fe2d77f3fe3bf7551bb8a8b795fad7e7226/constraintlayout-solver-2.0.1.jar because:
  Build cache is disabled
Caching disabled for task ':tv:mergeDebugJavaResource' because:
  Build cache is disabled
Task ':tv:mergeDebugJavaResource' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':tv:mergeDebugJavaResource'.
Resolve mutations for :tv:mergeExtDexDebug (Thread[Execution worker,5,main]) started.
:tv:mergeExtDexDebug (Thread[Execution worker,5,main]) started.

> Transform classes.jar (project :core) with DexingWithClasspathTransform
Transforming classes.jar (project :core) with DexingWithClasspathTransform
Caching disabled for DexingWithClasspathTransform: /home/runner/work/wiseiptv/wiseiptv/core/build/intermediates/runtime_library_classes_jar/debug/bundleLibRuntimeToJarDebug/classes.jar because:
  Build cache is disabled
DexingWithClasspathTransform: /home/runner/work/wiseiptv/wiseiptv/core/build/intermediates/runtime_library_classes_jar/debug/bundleLibRuntimeToJarDebug/classes.jar is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental DexingWithClasspathTransform: /home/runner/work/wiseiptv/wiseiptv/core/build/intermediates/runtime_library_classes_jar/debug/bundleLibRuntimeToJarDebug/classes.jar.
Running dexing transform non-incrementally for '/home/runner/work/wiseiptv/wiseiptv/core/build/intermediates/runtime_library_classes_jar/debug/bundleLibRuntimeToJarDebug/classes.jar'
DexingOutputSplitTransform (Thread[Execution worker Thread 3,5,main]) started.

> Transform classes.jar (project :core) with DexingOutputSplitTransform
Transforming classes.jar (project :core) with DexingOutputSplitTransform
Caching disabled for DexingOutputSplitTransform: /home/runner/work/wiseiptv/wiseiptv/core/build/.transforms/df7e8a31370dd770ec6077b8f8625ffb/transformed/classes because:
  Build cache is disabled
DexingOutputSplitTransform (Thread[Execution worker Thread 3,5,main]) started.

> Transform classes.jar (project :core) with DexingOutputSplitTransform
Transforming classes.jar (project :core) with DexingOutputSplitTransform
Caching disabled for DexingOutputSplitTransform: /home/runner/work/wiseiptv/wiseiptv/core/build/.transforms/df7e8a31370dd770ec6077b8f8625ffb/transformed/classes because:
  Build cache is disabled

> Task :tv:processDebugResources
Transforming aapt2-8.3.0-10880808-linux.jar (com.android.tools.build:aapt2:8.3.0-10880808) with Aapt2Extractor
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with JetifyTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with ExtractAarTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with AarResourcesCompilerTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with JetifyTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with ExtractAarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with AarResourcesCompilerTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with JetifyTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with AarResourcesCompilerTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with JetifyTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with AarResourcesCompilerTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with JetifyTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with AarResourcesCompilerTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with JetifyTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with ExtractAarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with AarResourcesCompilerTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with JetifyTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with ExtractAarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with AarResourcesCompilerTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with JetifyTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with ExtractAarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with AarResourcesCompilerTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with JetifyTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with ExtractAarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with AarResourcesCompilerTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with JetifyTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with ExtractAarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with AarResourcesCompilerTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with JetifyTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with ExtractAarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with AarResourcesCompilerTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with JetifyTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with ExtractAarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with AarResourcesCompilerTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with JetifyTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with ExtractAarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with AarResourcesCompilerTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with JetifyTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with ExtractAarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with AarResourcesCompilerTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarResourcesCompilerTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarResourcesCompilerTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with JetifyTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with ExtractAarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with AarResourcesCompilerTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with JetifyTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with ExtractAarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with AarResourcesCompilerTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with JetifyTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with ExtractAarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with AarResourcesCompilerTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with JetifyTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with ExtractAarTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with AarResourcesCompilerTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with JetifyTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with ExtractAarTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with AarResourcesCompilerTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarResourcesCompilerTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarResourcesCompilerTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with JetifyTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with ExtractAarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with AarResourcesCompilerTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with JetifyTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with AarResourcesCompilerTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarResourcesCompilerTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarResourcesCompilerTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarResourcesCompilerTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarResourcesCompilerTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with JetifyTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with ExtractAarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with AarResourcesCompilerTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with JetifyTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with ExtractAarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with AarResourcesCompilerTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with JetifyTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with ExtractAarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with AarResourcesCompilerTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarResourcesCompilerTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarResourcesCompilerTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with JetifyTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with ExtractAarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with AarResourcesCompilerTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with JetifyTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with ExtractAarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with AarResourcesCompilerTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with JetifyTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with ExtractAarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with AarResourcesCompilerTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with JetifyTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with ExtractAarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with AarResourcesCompilerTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with JetifyTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with ExtractAarTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with AarResourcesCompilerTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with AarResourcesCompilerTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with AarResourcesCompilerTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarResourcesCompilerTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarResourcesCompilerTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarResourcesCompilerTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarResourcesCompilerTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarResourcesCompilerTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarResourcesCompilerTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with AarResourcesCompilerTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with ExtractAarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with AarResourcesCompilerTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarResourcesCompilerTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarResourcesCompilerTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarResourcesCompilerTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarResourcesCompilerTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with JetifyTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with ExtractAarTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with AarResourcesCompilerTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarResourcesCompilerTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarResourcesCompilerTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarResourcesCompilerTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarResourcesCompilerTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarResourcesCompilerTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarResourcesCompilerTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with JetifyTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with ExtractAarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with AarResourcesCompilerTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with JetifyTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with ExtractAarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with AarResourcesCompilerTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarResourcesCompilerTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarResourcesCompilerTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarResourcesCompilerTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarResourcesCompilerTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarResourcesCompilerTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarResourcesCompilerTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarResourcesCompilerTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarResourcesCompilerTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarResourcesCompilerTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarResourcesCompilerTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarResourcesCompilerTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarResourcesCompilerTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarResourcesCompilerTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarResourcesCompilerTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with JetifyTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with ExtractAarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with AarResourcesCompilerTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarResourcesCompilerTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarResourcesCompilerTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with JetifyTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with ExtractAarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with AarResourcesCompilerTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarResourcesCompilerTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarResourcesCompilerTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with JetifyTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with ExtractAarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with AarResourcesCompilerTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with JetifyTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with ExtractAarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with AarResourcesCompilerTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with JetifyTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with ExtractAarTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with AarResourcesCompilerTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with JetifyTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with ExtractAarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with AarResourcesCompilerTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with JetifyTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with ExtractAarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with AarResourcesCompilerTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with JetifyTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with ExtractAarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with AarResourcesCompilerTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with JetifyTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with ExtractAarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with AarResourcesCompilerTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with JetifyTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with ExtractAarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with AarResourcesCompilerTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with JetifyTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with ExtractAarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with AarResourcesCompilerTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarResourcesCompilerTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarResourcesCompilerTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarResourcesCompilerTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarResourcesCompilerTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with JetifyTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with ExtractAarTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with AarResourcesCompilerTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with JetifyTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with ExtractAarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with AarResourcesCompilerTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with JetifyTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with ExtractAarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with AarResourcesCompilerTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with JetifyTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with ExtractAarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with AarResourcesCompilerTransform
Caching disabled for AarResourcesCompilerTransform: /home/runner/.gradle/caches/transforms-4/9346ab6aafc40c73d868f8cb25ad6eda/transformed/constraintlayout-2.0.1 because:
  Build cache is disabled
Caching disabled for AarResourcesCompilerTransform: /home/runner/.gradle/caches/transforms-4/a97fd6e266fa9af37bff1418f096f875/transformed/legacy-support-core-ui-1.0.0 because:
  Build cache is disabled
Caching disabled for AarResourcesCompilerTransform: /home/runner/.gradle/caches/transforms-4/f898a674e9cfed3d540fe9e2a8b40e57/transformed/slidingpanelayout-1.0.0 because:
  Build cache is disabled
Caching disabled for AarResourcesCompilerTransform: /home/runner/.gradle/caches/transforms-4/f846b076b9a99945a2694c3a76bf6d97/transformed/swiperefreshlayout-1.0.0 because:
  Build cache is disabled
Caching disabled for AarResourcesCompilerTransform: /home/runner/.gradle/caches/transforms-4/35cf7e22f9d1f043d812a7c3bcc02ec9/transformed/asynclayoutinflater-1.0.0 because:
  Build cache is disabled
Caching disabled for AarResourcesCompilerTransform: /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0 because:
  Build cache is disabled
Compiling file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/raw/lb_voice_open.ogg to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/raw_lb_voice_open.ogg.flat
Compiling file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/raw/lb_voice_failure.ogg to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/raw_lb_voice_failure.ogg.flat
Compiling file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/raw/lb_voice_no_input.ogg to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/raw_lb_voice_no_input.ogg.flat
Compiling file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/raw/lb_voice_success.ogg to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/raw_lb_voice_success.ogg.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_guidedbuttonactions.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_guidedbuttonactions.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_control_button_secondary.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_control_button_secondary.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_guidance.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_guidance.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_playback_controls.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_playback_controls.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_error_fragment.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_error_fragment.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_headers_fragment.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_headers_fragment.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_media_list_header.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_media_list_header.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_vertical_grid.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_vertical_grid.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_list_row.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_list_row.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_onboarding_fragment.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_onboarding_fragment.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_rows_fragment.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_rows_fragment.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_background_window.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_background_window.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_section_header.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_section_header.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_image_card_view_themed_title.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_image_card_view_themed_title.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_image_card_view_themed_badge_right.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_image_card_view_themed_badge_right.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_action_1_line.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_action_1_line.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_list_row_hovercard.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_list_row_hovercard.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_divider.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_divider.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_speech_orb.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_speech_orb.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_picker_item.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_picker_item.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_row_media_item_action.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_row_media_item_action.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_title_view.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_title_view.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_fullwidth_details_overview.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_fullwidth_details_overview.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_details_fragment.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_details_fragment.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_playback_controls_row.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_playback_controls_row.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_search_fragment.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_search_fragment.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_playback_fragment.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_playback_fragment.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_guidedactions.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_guidedactions.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_picker.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_picker.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_row_media_item.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_row_media_item.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_guidedactions_datepicker_item.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_guidedactions_datepicker_item.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_row_container.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_row_container.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_guidedstep_fragment.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_guidedstep_fragment.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_control_button_primary.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_control_button_primary.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_search_bar.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_search_bar.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_fullwidth_details_overview_logo.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_fullwidth_details_overview_logo.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_guidedstep_background.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_guidedstep_background.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_browse_fragment.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_browse_fragment.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_playback_now_playing_bars.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_playback_now_playing_bars.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_picker_column.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_picker_column.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_video_surface.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_video_surface.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_media_item_number_view_flipper.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_media_item_number_view_flipper.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_row_header.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_row_header.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_playback_transport_controls_row.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_playback_transport_controls_row.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_browse_title.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_browse_title.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_search_orb.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_search_orb.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_control_bar.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_control_bar.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_action_2_lines.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_action_2_lines.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_header.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_header.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_image_card_view_themed_badge_left.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_image_card_view_themed_badge_left.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_picker_separator.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_picker_separator.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_shadow.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_shadow.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_vertical_grid_fragment.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_vertical_grid_fragment.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_playback_transport_controls.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_playback_transport_controls.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_details_description.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_details_description.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_image_card_view.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_image_card_view.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_details_overview.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_details_overview.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_image_card_view_themed_content.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_image_card_view_themed_content.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/video_surface_fragment.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_video_surface_fragment.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/layout/lb_guidedactions_item.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/layout_lb_guidedactions_item.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_title_out.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_title_out.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_shared_element_return_transition.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_shared_element_return_transition.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_browse_entrance_transition.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_browse_entrance_transition.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_details_return_transition.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_details_return_transition.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_guidedstep_activity_enter_bottom.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_guidedstep_activity_enter_bottom.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_shared_element_enter_transition.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_shared_element_enter_transition.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_enter_transition.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_enter_transition.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_title_in.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_title_in.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_browse_headers_out.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_browse_headers_out.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_browse_headers_in.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_browse_headers_in.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_return_transition.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_return_transition.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_vertical_grid_enter_transition.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_vertical_grid_enter_transition.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_browse_return_transition.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_browse_return_transition.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_details_enter_transition.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_details_enter_transition.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_vertical_grid_entrance_transition.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_vertical_grid_entrance_transition.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_guidedstep_activity_enter.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_guidedstep_activity_enter.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_vertical_grid_return_transition.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_vertical_grid_return_transition.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v21/lb_browse_enter_transition.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v21_lb_browse_enter_transition.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v19/lb_browse_headers_out.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v19_lb_browse_headers_out.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/transition-v19/lb_browse_headers_in.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/transition-v19_lb_browse_headers_in.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable-v21/lb_action_bg.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable-v21_lb_action_bg.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable-v21/lb_control_button_secondary.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable-v21_lb_control_button_secondary.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable-v21/lb_card_foreground.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable-v21_lb_card_foreground.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable-v21/lb_selectable_item_rounded_rect.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable-v21_lb_selectable_item_rounded_rect.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable-v21/lb_control_button_primary.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable-v21_lb_control_button_primary.xml.flat
AAPT2 aapt2-8.3.0-10880808-linux Daemon #0: starting
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator-v21/lb_playback_bg_fade_in.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator-v21_lb_playback_bg_fade_in.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator-v21/lb_onboarding_description_enter.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator-v21_lb_onboarding_description_enter.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator-v21/lb_playback_bg_fade_out.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator-v21_lb_playback_bg_fade_out.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator-v21/lb_onboarding_title_enter.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator-v21_lb_onboarding_title_enter.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator-v21/lb_onboarding_logo_enter.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator-v21_lb_onboarding_logo_enter.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator-v21/lb_onboarding_page_indicator_enter.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator-v21_lb_onboarding_page_indicator_enter.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator-v21/lb_playback_description_fade_out.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator-v21_lb_playback_description_fade_out.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator-v21/lb_onboarding_logo_exit.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator-v21_lb_onboarding_logo_exit.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_guidedstep_slide_up.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_guidedstep_slide_up.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_guidedactions_item_pressed.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_guidedactions_item_pressed.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_playback_bg_fade_in.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_playback_bg_fade_in.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_onboarding_description_enter.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_onboarding_description_enter.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_playback_controls_fade_out.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_playback_controls_fade_out.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_playback_rows_fade_in.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_playback_rows_fade_in.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_guidedactions_item_unpressed.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_guidedactions_item_unpressed.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_onboarding_page_indicator_fade_in.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_onboarding_page_indicator_fade_in.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_playback_description_fade_in.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_playback_description_fade_in.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_onboarding_page_indicator_fade_out.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_onboarding_page_indicator_fade_out.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_onboarding_start_button_fade_in.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_onboarding_start_button_fade_in.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_playback_bg_fade_out.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_playback_bg_fade_out.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_onboarding_title_enter.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_onboarding_title_enter.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_playback_controls_fade_in.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_playback_controls_fade_in.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_onboarding_start_button_fade_out.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_onboarding_start_button_fade_out.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_onboarding_logo_enter.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_onboarding_logo_enter.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_playback_rows_fade_out.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_playback_rows_fade_out.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_onboarding_page_indicator_enter.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_onboarding_page_indicator_enter.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_guidedstep_slide_down.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_guidedstep_slide_down.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_playback_description_fade_out.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_playback_description_fade_out.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/animator/lb_onboarding_logo_exit.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/animator_lb_onboarding_logo_exit.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable/lb_control_button_secondary.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable_lb_control_button_secondary.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable/lb_speech_orb.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable_lb_speech_orb.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable/lb_card_foreground.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable_lb_card_foreground.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable/lb_headers_right_fading.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable_lb_headers_right_fading.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable/lb_control_button_primary.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable_lb_control_button_primary.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable/lb_playback_now_playing_bar.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable_lb_playback_now_playing_bar.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable/lb_background.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable_lb_background.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable/lb_search_orb.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable_lb_search_orb.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable/lb_onboarding_start_button_background.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable_lb_onboarding_start_button_background.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/drawable/lb_playback_progress_bar.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/drawable_lb_playback_progress_bar.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/anim/lb_decelerator_2.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/anim_lb_decelerator_2.xml.flat
Compiling xml file /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0/res/anim/lb_decelerator_4.xml to /home/runner/.gradle/caches/transforms-4/10c5e47858c5d2595f080b37e868caa2-9073c321-4eca-45f1-8d61-46544282af7c/transformed/androidx.leanback/anim_lb_decelerator_4.xml.flat
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with JetifyTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with ExtractAarTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with LibrarySymbolTableTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with JetifyTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with ExtractAarTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with LibrarySymbolTableTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with JetifyTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with LibrarySymbolTableTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with JetifyTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with LibrarySymbolTableTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with JetifyTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with ExtractAarTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with LibrarySymbolTableTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with JetifyTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with ExtractAarTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with LibrarySymbolTableTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with JetifyTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with ExtractAarTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with LibrarySymbolTableTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with JetifyTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with ExtractAarTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with LibrarySymbolTableTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with JetifyTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with ExtractAarTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with LibrarySymbolTableTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with JetifyTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with ExtractAarTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with LibrarySymbolTableTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with JetifyTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with ExtractAarTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with LibrarySymbolTableTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with JetifyTransform
Caching disabled for LibrarySymbolTableTransform: /home/runner/.gradle/caches/transforms-4/d8d6331c0146512f98ef009d85f91c1c/transformed/leanback-1.0.0 because:
  Build cache is disabled
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with ExtractAarTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with LibrarySymbolTableTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with JetifyTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with ExtractAarTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with LibrarySymbolTableTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with JetifyTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with ExtractAarTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with LibrarySymbolTableTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with LibrarySymbolTableTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with ExtractAarTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with LibrarySymbolTableTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with JetifyTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with ExtractAarTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with LibrarySymbolTableTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with JetifyTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with ExtractAarTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with LibrarySymbolTableTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with JetifyTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with ExtractAarTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with LibrarySymbolTableTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with JetifyTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with ExtractAarTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with LibrarySymbolTableTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with JetifyTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with ExtractAarTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with LibrarySymbolTableTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with LibrarySymbolTableTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with ExtractAarTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with LibrarySymbolTableTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with JetifyTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with ExtractAarTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with LibrarySymbolTableTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with JetifyTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with LibrarySymbolTableTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with LibrarySymbolTableTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with ExtractAarTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with LibrarySymbolTableTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with LibrarySymbolTableTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with ExtractAarTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with LibrarySymbolTableTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with JetifyTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with ExtractAarTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with LibrarySymbolTableTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with JetifyTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with ExtractAarTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with LibrarySymbolTableTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with JetifyTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with ExtractAarTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with LibrarySymbolTableTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with LibrarySymbolTableTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with ExtractAarTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with LibrarySymbolTableTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with JetifyTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with ExtractAarTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with LibrarySymbolTableTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with JetifyTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with ExtractAarTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with LibrarySymbolTableTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with JetifyTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with ExtractAarTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with LibrarySymbolTableTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with JetifyTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with ExtractAarTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with LibrarySymbolTableTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with JetifyTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with ExtractAarTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with LibrarySymbolTableTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with LibrarySymbolTableTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with LibrarySymbolTableTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with LibrarySymbolTableTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with ExtractAarTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with LibrarySymbolTableTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with LibrarySymbolTableTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with ExtractAarTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with LibrarySymbolTableTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with LibrarySymbolTableTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with LibrarySymbolTableTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with ExtractAarTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with LibrarySymbolTableTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with ExtractAarTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with LibrarySymbolTableTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with LibrarySymbolTableTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with ExtractAarTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with LibrarySymbolTableTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with LibrarySymbolTableTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with ExtractAarTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with LibrarySymbolTableTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with JetifyTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with ExtractAarTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with LibrarySymbolTableTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with LibrarySymbolTableTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with LibrarySymbolTableTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with LibrarySymbolTableTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with LibrarySymbolTableTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with LibrarySymbolTableTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with ExtractAarTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with LibrarySymbolTableTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with JetifyTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with ExtractAarTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with LibrarySymbolTableTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with JetifyTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with ExtractAarTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with LibrarySymbolTableTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with LibrarySymbolTableTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with LibrarySymbolTableTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with LibrarySymbolTableTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with LibrarySymbolTableTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with LibrarySymbolTableTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with LibrarySymbolTableTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with LibrarySymbolTableTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with LibrarySymbolTableTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Caching disabled for LibrarySymbolTableTransform: /home/runner/.gradle/caches/transforms-4/9346ab6aafc40c73d868f8cb25ad6eda/transformed/constraintlayout-2.0.1 because:
  Build cache is disabled
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with LibrarySymbolTableTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with LibrarySymbolTableTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with LibrarySymbolTableTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with LibrarySymbolTableTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with LibrarySymbolTableTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with ExtractAarTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with LibrarySymbolTableTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with JetifyTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with ExtractAarTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with LibrarySymbolTableTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with LibrarySymbolTableTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with ExtractAarTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with LibrarySymbolTableTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with JetifyTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with ExtractAarTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with LibrarySymbolTableTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with LibrarySymbolTableTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with ExtractAarTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with LibrarySymbolTableTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with JetifyTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with ExtractAarTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with LibrarySymbolTableTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with JetifyTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with ExtractAarTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with LibrarySymbolTableTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with JetifyTransform
Caching disabled for LibrarySymbolTableTransform: /home/runner/.gradle/caches/transforms-4/a97fd6e266fa9af37bff1418f096f875/transformed/legacy-support-core-ui-1.0.0 because:
  Build cache is disabled
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with ExtractAarTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with LibrarySymbolTableTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with JetifyTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with ExtractAarTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with LibrarySymbolTableTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with JetifyTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with ExtractAarTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with LibrarySymbolTableTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with JetifyTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with ExtractAarTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with LibrarySymbolTableTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with JetifyTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with ExtractAarTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with LibrarySymbolTableTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with JetifyTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with ExtractAarTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with LibrarySymbolTableTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with JetifyTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with ExtractAarTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with LibrarySymbolTableTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with LibrarySymbolTableTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with LibrarySymbolTableTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with LibrarySymbolTableTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with ExtractAarTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with LibrarySymbolTableTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with JetifyTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with ExtractAarTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with LibrarySymbolTableTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with JetifyTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with ExtractAarTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with LibrarySymbolTableTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with JetifyTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with ExtractAarTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with LibrarySymbolTableTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with JetifyTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with ExtractAarTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with LibrarySymbolTableTransform
Caching disabled for LibrarySymbolTableTransform: /home/runner/.gradle/caches/transforms-4/f898a674e9cfed3d540fe9e2a8b40e57/transformed/slidingpanelayout-1.0.0 because:
  Build cache is disabled
Caching disabled for LibrarySymbolTableTransform: /home/runner/.gradle/caches/transforms-4/f846b076b9a99945a2694c3a76bf6d97/transformed/swiperefreshlayout-1.0.0 because:
  Build cache is disabled
Caching disabled for LibrarySymbolTableTransform: /home/runner/.gradle/caches/transforms-4/35cf7e22f9d1f043d812a7c3bcc02ec9/transformed/asynclayoutinflater-1.0.0 because:
  Build cache is disabled
Caching disabled for task ':tv:processDebugResources' because:
  Build cache is disabled
Task ':tv:processDebugResources' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':tv:processDebugResources'.
Android resource linking failed:
process = AAPT2 aapt2-8.3.0-10880808-linux Daemon #0:
command = /home/runner/.gradle/caches/transforms-4/503c1528a5a4ed83f015114fcba8a7e3/transformed/aapt2-8.3.0-10880808-linux/aapt2 link -I\
        /usr/local/lib/android/sdk/platforms/android-34/android.jar\
        --manifest\
        /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/packaged_manifests/debug/processDebugManifestForPackage/AndroidManifest.xml\
        -o\
        /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/processed_res/debug/processDebugResources/out/resources-debug.ap_\
        -R\
        @/home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/incremental/processDebugResources/resources-list-for-resources-debug.ap_.txt\
        --auto-add-overlay\
        --custom-package\
        com.wdesign.wiseiptv.tv\
        -0\
        apk\
        --output-text-symbols\
        /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/runtime_symbol_list/debug/processDebugResources/R.txt\
        --no-version-vectors\
        --proguard-minimal-keep-rules\
        --no-proguard-location-reference\
        --emit-ids\
        /home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/stable_resource_ids_file/debug/processDebugResources/stableIds.txt

> Task :tv:processDebugResources FAILED

> Task :tv:mergeExtDexDebug
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with JetifyTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with AarToClassTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with JetifyTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with AarToClassTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarToClassTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarToClassTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with JetifyTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with AarToClassTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with JetifyTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with AarToClassTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarToClassTransform
Transforming error_prone_annotations-2.15.0.jar (com.google.errorprone:error_prone_annotations:2.15.0) with JetifyTransform
Transforming error_prone_annotations-2.15.0.jar (com.google.errorprone:error_prone_annotations:2.15.0) with IdentityTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with JetifyTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with AarToClassTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with JetifyTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with AarToClassTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with JetifyTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with AarToClassTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with JetifyTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with AarToClassTransform
Transforming constraintlayout-solver-2.0.1.jar (androidx.constraintlayout:constraintlayout-solver:2.0.1) with JetifyTransform
Transforming constraintlayout-solver-2.0.1.jar (androidx.constraintlayout:constraintlayout-solver:2.0.1) with IdentityTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with JetifyTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with AarToClassTransform
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with DexingWithClasspathTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with JetifyTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with AarToClassTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with JetifyTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with AarToClassTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with JetifyTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with AarToClassTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with JetifyTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with AarToClassTransform
Caching disabled for DexingWithClasspathTransform: /home/runner/.gradle/caches/transforms-4/9228a774d411411a27758a022f2d55a6/transformed/material-1.12.0-runtime.jar because:
  Build cache is disabled
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with JetifyTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with AarToClassTransform
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with DexingWithClasspathTransform
Running dexing transform non-incrementally for '/home/runner/.gradle/caches/transforms-4/9228a774d411411a27758a022f2d55a6/transformed/material-1.12.0-runtime.jar'
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with JetifyTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with AarToClassTransform
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with DexingWithClasspathTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with JetifyTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with AarToClassTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with DexingWithClasspathTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with JetifyTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with AarToClassTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with DexingWithClasspathTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with JetifyTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with AarToClassTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with DexingWithClasspathTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with JetifyTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with AarToClassTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with DexingWithClasspathTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with JetifyTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with AarToClassTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with DexingWithClasspathTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with JetifyTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with AarToClassTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with DexingWithClasspathTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with JetifyTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with AarToClassTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with DexingWithClasspathTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with JetifyTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with AarToClassTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with DexingWithClasspathTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with JetifyTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with AarToClassTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with DexingWithClasspathTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with JetifyTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with AarToClassTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with DexingWithClasspathTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with JetifyTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with AarToClassTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with DexingWithClasspathTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarToClassTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with DexingWithClasspathTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with JetifyTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with AarToClassTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with DexingWithClasspathTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with JetifyTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with AarToClassTransform
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with DexingWithClasspathTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with JetifyTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with AarToClassTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with DexingWithClasspathTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with JetifyTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with AarToClassTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with DexingWithClasspathTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with JetifyTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with IdentityTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with DexingWithClasspathTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with JetifyTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with AarToClassTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with DexingWithClasspathTransform
Transforming okhttp-4.12.0.jar (com.squareup.okhttp3:okhttp:4.12.0) with JetifyTransform
Transforming okhttp-4.12.0.jar (com.squareup.okhttp3:okhttp:4.12.0) with IdentityTransform
Transforming okhttp-4.12.0.jar (com.squareup.okhttp3:okhttp:4.12.0) with DexingWithClasspathTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with JetifyTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with AarToClassTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with DexingWithClasspathTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarToClassTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with DexingWithClasspathTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with JetifyTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with AarToClassTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with DexingWithClasspathTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with JetifyTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with AarToClassTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with DexingWithClasspathTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with JetifyTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with AarToClassTransform
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with DexingWithClasspathTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarToClassTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with DexingWithClasspathTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with JetifyTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with AarToClassTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with DexingWithClasspathTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarToClassTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with DexingWithClasspathTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with JetifyTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with AarToClassTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with DexingWithClasspathTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with JetifyTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with AarToClassTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with DexingWithClasspathTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with JetifyTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with AarToClassTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with DexingWithClasspathTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with JetifyTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with AarToClassTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with DexingWithClasspathTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarToClassTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with DexingWithClasspathTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with JetifyTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with AarToClassTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with DexingWithClasspathTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with JetifyTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with AarToClassTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with DexingWithClasspathTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with JetifyTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with AarToClassTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with DexingWithClasspathTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with JetifyTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with AarToClassTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with DexingWithClasspathTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with JetifyTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with AarToClassTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with DexingWithClasspathTransform
Transforming lifecycle-common-2.7.0.jar (androidx.lifecycle:lifecycle-common:2.7.0) with JetifyTransform
Transforming lifecycle-common-2.7.0.jar (androidx.lifecycle:lifecycle-common:2.7.0) with IdentityTransform
Transforming lifecycle-common-2.7.0.jar (androidx.lifecycle:lifecycle-common:2.7.0) with DexingWithClasspathTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with JetifyTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with AarToClassTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with DexingWithClasspathTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with AarToClassTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with DexingWithClasspathTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with AarToClassTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with DexingWithClasspathTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarToClassTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with DexingWithClasspathTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with JetifyTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with AarToClassTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with DexingWithClasspathTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarToClassTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with DexingWithClasspathTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with JetifyTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with AarToClassTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with DexingWithClasspathTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarToClassTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with DexingWithClasspathTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with AarToClassTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with DexingWithClasspathTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with JetifyTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with AarToClassTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with DexingWithClasspathTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with JetifyTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with AarToClassTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with DexingWithClasspathTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarToClassTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with DexingWithClasspathTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with JetifyTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with AarToClassTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with DexingWithClasspathTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarToClassTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with DexingWithClasspathTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with JetifyTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with AarToClassTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with DexingWithClasspathTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with JetifyTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with AarToClassTransform
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with DexingWithClasspathTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with DexingWithClasspathTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with DexingWithClasspathTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with DexingWithClasspathTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with DexingWithClasspathTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with DexingWithClasspathTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with JetifyTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with AarToClassTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with DexingWithClasspathTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with JetifyTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with AarToClassTransform
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with DexingWithClasspathTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with JetifyTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with AarToClassTransform
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with DexingWithClasspathTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingWithClasspathTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingWithClasspathTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingWithClasspathTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingWithClasspathTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingWithClasspathTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingWithClasspathTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingWithClasspathTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingWithClasspathTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingWithClasspathTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingWithClasspathTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingWithClasspathTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingWithClasspathTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingWithClasspathTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with JetifyTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with AarToClassTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingWithClasspathTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with JetifyTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with AarToClassTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with DexingWithClasspathTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarToClassTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with DexingWithClasspathTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with JetifyTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with AarToClassTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with DexingWithClasspathTransform
Transforming error_prone_annotations-2.15.0.jar (com.google.errorprone:error_prone_annotations:2.15.0) with JetifyTransform
Transforming error_prone_annotations-2.15.0.jar (com.google.errorprone:error_prone_annotations:2.15.0) with IdentityTransform
Transforming error_prone_annotations-2.15.0.jar (com.google.errorprone:error_prone_annotations:2.15.0) with DexingWithClasspathTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with JetifyTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with AarToClassTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with DexingWithClasspathTransform
Transforming resourceinspection-annotation-1.0.1.jar (androidx.resourceinspection:resourceinspection-annotation:1.0.1) with JetifyTransform
Transforming resourceinspection-annotation-1.0.1.jar (androidx.resourceinspection:resourceinspection-annotation:1.0.1) with IdentityTransform
Transforming resourceinspection-annotation-1.0.1.jar (androidx.resourceinspection:resourceinspection-annotation:1.0.1) with DexingWithClasspathTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarToClassTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with DexingWithClasspathTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with JetifyTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with AarToClassTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with DexingWithClasspathTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with JetifyTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with AarToClassTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with DexingWithClasspathTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with JetifyTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with AarToClassTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with DexingWithClasspathTransform
Transforming collection-1.2.0.jar (androidx.collection:collection:1.2.0) with JetifyTransform
Transforming collection-1.2.0.jar (androidx.collection:collection:1.2.0) with IdentityTransform
Transforming collection-1.2.0.jar (androidx.collection:collection:1.2.0) with DexingWithClasspathTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with JetifyTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with AarToClassTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with DexingWithClasspathTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with JetifyTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with AarToClassTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with DexingWithClasspathTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with JetifyTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with AarToClassTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with DexingWithClasspathTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with JetifyTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with AarToClassTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with DexingWithClasspathTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with JetifyTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with AarToClassTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with DexingWithClasspathTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with JetifyTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with AarToClassTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with DexingWithClasspathTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with JetifyTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with AarToClassTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with DexingWithClasspathTransform
Transforming core-common-2.2.0.jar (androidx.arch.core:core-common:2.2.0) with JetifyTransform
Transforming core-common-2.2.0.jar (androidx.arch.core:core-common:2.2.0) with IdentityTransform
Transforming core-common-2.2.0.jar (androidx.arch.core:core-common:2.2.0) with DexingWithClasspathTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarToClassTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with DexingWithClasspathTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarToClassTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with DexingWithClasspathTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarToClassTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with DexingWithClasspathTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with JetifyTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with AarToClassTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with DexingWithClasspathTransform
Transforming concurrent-futures-1.1.0.jar (androidx.concurrent:concurrent-futures:1.1.0) with JetifyTransform
Transforming concurrent-futures-1.1.0.jar (androidx.concurrent:concurrent-futures:1.1.0) with IdentityTransform
Transforming concurrent-futures-1.1.0.jar (androidx.concurrent:concurrent-futures:1.1.0) with DexingWithClasspathTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with JetifyTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with AarToClassTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with DexingWithClasspathTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with JetifyTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with AarToClassTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with DexingWithClasspathTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with JetifyTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with AarToClassTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with DexingWithClasspathTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with JetifyTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with AarToClassTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with DexingWithClasspathTransform
Transforming annotation-jvm-1.6.0.jar (androidx.annotation:annotation-jvm:1.6.0) with JetifyTransform
Transforming annotation-jvm-1.6.0.jar (androidx.annotation:annotation-jvm:1.6.0) with IdentityTransform
Transforming annotation-jvm-1.6.0.jar (androidx.annotation:annotation-jvm:1.6.0) with DexingWithClasspathTransform
Transforming okio-jvm-3.6.0.jar (com.squareup.okio:okio-jvm:3.6.0) with JetifyTransform
Transforming okio-jvm-3.6.0.jar (com.squareup.okio:okio-jvm:3.6.0) with IdentityTransform
Transforming okio-jvm-3.6.0.jar (com.squareup.okio:okio-jvm:3.6.0) with DexingWithClasspathTransform
Transforming kotlinx-coroutines-android-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1) with JetifyTransform
Transforming kotlinx-coroutines-android-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1) with IdentityTransform
Transforming kotlinx-coroutines-android-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1) with DexingWithClasspathTransform
Transforming kotlinx-coroutines-core-jvm-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1) with JetifyTransform
Transforming kotlinx-coroutines-core-jvm-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1) with IdentityTransform
Transforming kotlinx-coroutines-core-jvm-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1) with DexingWithClasspathTransform
Transforming kotlin-stdlib-jdk8-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-jdk8-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10) with IdentityTransform
Transforming kotlin-stdlib-jdk8-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10) with DexingWithClasspathTransform
Transforming kotlin-stdlib-jdk7-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-jdk7-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10) with IdentityTransform
Transforming kotlin-stdlib-jdk7-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10) with DexingWithClasspathTransform
Transforming kotlin-stdlib-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.10) with IdentityTransform
Transforming kotlin-stdlib-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.10) with DexingWithClasspathTransform
Transforming guava-32.1.3-android.jar (com.google.guava:guava:32.1.3-android) with JetifyTransform
Transforming guava-32.1.3-android.jar (com.google.guava:guava:32.1.3-android) with IdentityTransform
Transforming guava-32.1.3-android.jar (com.google.guava:guava:32.1.3-android) with DexingWithClasspathTransform
Transforming disklrucache-4.16.0.jar (com.github.bumptech.glide:disklrucache:4.16.0) with JetifyTransform
Transforming disklrucache-4.16.0.jar (com.github.bumptech.glide:disklrucache:4.16.0) with IdentityTransform
Transforming disklrucache-4.16.0.jar (com.github.bumptech.glide:disklrucache:4.16.0) with DexingWithClasspathTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with JetifyTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with IdentityTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with DexingWithClasspathTransform
Transforming kotlin-stdlib-common-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10) with JetifyTransform
Transforming kotlin-stdlib-common-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10) with IdentityTransform
Transforming kotlin-stdlib-common-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10) with DexingWithClasspathTransform
Transforming constraintlayout-solver-2.0.1.jar (androidx.constraintlayout:constraintlayout-solver:2.0.1) with JetifyTransform
Transforming constraintlayout-solver-2.0.1.jar (androidx.constraintlayout:constraintlayout-solver:2.0.1) with IdentityTransform
Transforming constraintlayout-solver-2.0.1.jar (androidx.constraintlayout:constraintlayout-solver:2.0.1) with DexingWithClasspathTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with JetifyTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with IdentityTransform
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with DexingWithClasspathTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with JetifyTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with IdentityTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with DexingWithClasspathTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with JetifyTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with IdentityTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with DexingWithClasspathTransform
Transforming media3-exoplayer-dash-1.3.1.aar (androidx.media3:media3-exoplayer-dash:1.3.1) with DexingOutputSplitTransform
Transforming media3-exoplayer-1.3.1.aar (androidx.media3:media3-exoplayer:1.3.1) with DexingOutputSplitTransform
Transforming media3-datasource-okhttp-1.3.1.aar (androidx.media3:media3-datasource-okhttp:1.3.1) with DexingOutputSplitTransform
Transforming media3-datasource-1.3.1.aar (androidx.media3:media3-datasource:1.3.1) with DexingOutputSplitTransform
Transforming media3-extractor-1.3.1.aar (androidx.media3:media3-extractor:1.3.1) with DexingOutputSplitTransform
Transforming media3-container-1.3.1.aar (androidx.media3:media3-container:1.3.1) with DexingOutputSplitTransform
Transforming media3-decoder-1.3.1.aar (androidx.media3:media3-decoder:1.3.1) with DexingOutputSplitTransform
Transforming media3-database-1.3.1.aar (androidx.media3:media3-database:1.3.1) with DexingOutputSplitTransform
Transforming media3-common-1.3.1.aar (androidx.media3:media3-common:1.3.1) with DexingOutputSplitTransform
Transforming media3-ui-1.3.1.aar (androidx.media3:media3-ui:1.3.1) with DexingOutputSplitTransform
Transforming viewpager2-1.1.0-beta02.aar (androidx.viewpager2:viewpager2:1.1.0-beta02) with DexingOutputSplitTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with DexingOutputSplitTransform
Transforming recyclerview-1.3.2.aar (androidx.recyclerview:recyclerview:1.3.2) with DexingOutputSplitTransform
Caching disabled for DexingWithClasspathTransform: /home/runner/.gradle/caches/transforms-4/90ef22543e81f4ccb6c669c80980b80f/transformed/constraintlayout-2.0.1-runtime.jar because:
  Build cache is disabled
Running dexing transform non-incrementally for '/home/runner/.gradle/caches/transforms-4/90ef22543e81f4ccb6c669c80980b80f/transformed/constraintlayout-2.0.1-runtime.jar'
Transforming media3-exoplayer-hls-1.3.1.aar (androidx.media3:media3-exoplayer-hls:1.3.1) with DexingOutputSplitTransform
Transforming appcompat-resources-1.7.0.aar (androidx.appcompat:appcompat-resources:1.7.0) with DexingOutputSplitTransform
Transforming appcompat-1.7.0.aar (androidx.appcompat:appcompat:1.7.0) with DexingOutputSplitTransform
Transforming room-common-2.6.1.jar (androidx.room:room-common:2.6.1) with DexingOutputSplitTransform
Transforming room-runtime-2.6.1.aar (androidx.room:room-runtime:2.6.1) with DexingOutputSplitTransform
Transforming okhttp-4.12.0.jar (com.squareup.okhttp3:okhttp:4.12.0) with DexingOutputSplitTransform
Transforming glide-4.16.0.aar (com.github.bumptech.glide:glide:4.16.0) with DexingOutputSplitTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with DexingOutputSplitTransform
Transforming fragment-1.5.4.aar (androidx.fragment:fragment:1.5.4) with DexingOutputSplitTransform
Transforming activity-1.8.0.aar (androidx.activity:activity:1.8.0) with DexingOutputSplitTransform
Caching disabled for DexingWithClasspathTransform: /home/runner/.gradle/caches/transforms-4/d88407966df0a8618d4d25c523ba9d9d/transformed/legacy-support-core-ui-1.0.0-runtime.jar because:
  Build cache is disabled
Running dexing transform non-incrementally for '/home/runner/.gradle/caches/transforms-4/d88407966df0a8618d4d25c523ba9d9d/transformed/legacy-support-core-ui-1.0.0-runtime.jar'
Transforming legacy-support-core-ui-1.0.0.aar (androidx.legacy:legacy-support-core-ui:1.0.0) with DexingOutputSplitTransform
Caching disabled for DexingOutputSplitTransform: /home/runner/.gradle/caches/transforms-4/dfaebd21ce2ae14e2511de46107ba9d9/transformed/legacy-support-core-ui-1.0.0-runtime because:
  Build cache is disabled
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with DexingOutputSplitTransform
Transforming coordinatorlayout-1.1.0.aar (androidx.coordinatorlayout:coordinatorlayout:1.1.0) with DexingOutputSplitTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with DexingOutputSplitTransform
Transforming drawerlayout-1.1.1.aar (androidx.drawerlayout:drawerlayout:1.1.1) with DexingOutputSplitTransform
Transforming transition-1.5.0.aar (androidx.transition:transition:1.5.0) with DexingOutputSplitTransform
Transforming dynamicanimation-1.0.0.aar (androidx.dynamicanimation:dynamicanimation:1.0.0) with DexingOutputSplitTransform
Transforming vectordrawable-animated-1.1.0.aar (androidx.vectordrawable:vectordrawable-animated:1.1.0) with DexingOutputSplitTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with DexingOutputSplitTransform
Transforming vectordrawable-1.1.0.aar (androidx.vectordrawable:vectordrawable:1.1.0) with DexingOutputSplitTransform
Transforming customview-poolingcontainer-1.0.0.aar (androidx.customview:customview-poolingcontainer:1.0.0) with DexingOutputSplitTransform
Transforming emoji2-views-helper-1.3.0.aar (androidx.emoji2:emoji2-views-helper:1.3.0) with DexingOutputSplitTransform
Transforming emoji2-1.3.0.aar (androidx.emoji2:emoji2:1.3.0) with DexingOutputSplitTransform
Transforming savedstate-1.2.1.aar (androidx.savedstate:savedstate:1.2.1) with DexingOutputSplitTransform
Transforming lifecycle-common-2.7.0.jar (androidx.lifecycle:lifecycle-common:2.7.0) with DexingOutputSplitTransform
Transforming lifecycle-process-2.7.0.aar (androidx.lifecycle:lifecycle-process:2.7.0) with DexingOutputSplitTransform
Transforming lifecycle-livedata-core-ktx-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0) with DexingOutputSplitTransform
Transforming lifecycle-livedata-core-2.7.0.aar (androidx.lifecycle:lifecycle-livedata-core:2.7.0) with DexingOutputSplitTransform
Transforming legacy-support-core-utils-1.0.0.aar (androidx.legacy:legacy-support-core-utils:1.0.0) with DexingOutputSplitTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with DexingOutputSplitTransform
Transforming loader-1.0.0.aar (androidx.loader:loader:1.0.0) with DexingOutputSplitTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with DexingOutputSplitTransform
Transforming lifecycle-viewmodel-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel:2.7.0) with DexingOutputSplitTransform
Transforming lifecycle-viewmodel-savedstate-2.7.0.aar (androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0) with DexingOutputSplitTransform
Transforming core-ktx-1.13.0.aar (androidx.core:core-ktx:1.13.0) with DexingOutputSplitTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with DexingOutputSplitTransform
Transforming media-1.7.0.aar (androidx.media:media:1.7.0) with DexingOutputSplitTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with DexingOutputSplitTransform
Transforming viewpager-1.0.0.aar (androidx.viewpager:viewpager:1.0.0) with DexingOutputSplitTransform
Caching disabled for DexingWithClasspathTransform: /home/runner/.gradle/caches/transforms-4/debb72ba00d99df688162734bb4f43a4/transformed/slidingpanelayout-1.0.0-runtime.jar because:
  Build cache is disabled
Running dexing transform non-incrementally for '/home/runner/.gradle/caches/transforms-4/debb72ba00d99df688162734bb4f43a4/transformed/slidingpanelayout-1.0.0-runtime.jar'
Transforming slidingpanelayout-1.0.0.aar (androidx.slidingpanelayout:slidingpanelayout:1.0.0) with DexingOutputSplitTransform
Caching disabled for DexingOutputSplitTransform: /home/runner/.gradle/caches/transforms-4/3a0e3f123823fe7625647a5df3e8b0f4/transformed/slidingpanelayout-1.0.0-runtime because:
  Build cache is disabled
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with DexingOutputSplitTransform
Transforming customview-1.1.0.aar (androidx.customview:customview:1.1.0) with DexingOutputSplitTransform
Caching disabled for DexingWithClasspathTransform: /home/runner/.gradle/caches/transforms-4/460c86aa89d9e29fa2d63c24ec7f8a82/transformed/swiperefreshlayout-1.0.0-runtime.jar because:
  Build cache is disabled
Running dexing transform non-incrementally for '/home/runner/.gradle/caches/transforms-4/460c86aa89d9e29fa2d63c24ec7f8a82/transformed/swiperefreshlayout-1.0.0-runtime.jar'
Transforming swiperefreshlayout-1.0.0.aar (androidx.swiperefreshlayout:swiperefreshlayout:1.0.0) with DexingOutputSplitTransform
Caching disabled for DexingOutputSplitTransform: /home/runner/.gradle/caches/transforms-4/952bed646c8ada121b4680f1612c5a70/transformed/swiperefreshlayout-1.0.0-runtime because:
  Build cache is disabled
Caching disabled for DexingWithClasspathTransform: /home/runner/.gradle/caches/transforms-4/0aa8cb039e665d777cabd3dae93496f6/transformed/asynclayoutinflater-1.0.0-runtime.jar because:
  Build cache is disabled
Running dexing transform non-incrementally for '/home/runner/.gradle/caches/transforms-4/0aa8cb039e665d777cabd3dae93496f6/transformed/asynclayoutinflater-1.0.0-runtime.jar'
Caching disabled for DexingWithClasspathTransform: /home/runner/.gradle/caches/transforms-4/764d721b97d482dbebc01a5a33219f1a/transformed/leanback-1.0.0-runtime.jar because:
  Build cache is disabled
Running dexing transform non-incrementally for '/home/runner/.gradle/caches/transforms-4/764d721b97d482dbebc01a5a33219f1a/transformed/leanback-1.0.0-runtime.jar'
Transforming asynclayoutinflater-1.0.0.aar (androidx.asynclayoutinflater:asynclayoutinflater:1.0.0) with DexingOutputSplitTransform
Caching disabled for DexingOutputSplitTransform: /home/runner/.gradle/caches/transforms-4/fdaba6162f9a038ca677ebe1a0187bda/transformed/asynclayoutinflater-1.0.0-runtime because:
  Build cache is disabled
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingOutputSplitTransform
Transforming core-1.13.0.aar (androidx.core:core:1.13.0) with DexingOutputSplitTransform
Transforming lifecycle-runtime-2.7.0.aar (androidx.lifecycle:lifecycle-runtime:2.7.0) with DexingOutputSplitTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with DexingOutputSplitTransform
Transforming lifecycle-livedata-2.7.0.aar (androidx.lifecycle:lifecycle-livedata:2.7.0) with DexingOutputSplitTransform
Transforming error_prone_annotations-2.15.0.jar (com.google.errorprone:error_prone_annotations:2.15.0) with DexingOutputSplitTransform
Transforming cardview-1.0.0.aar (androidx.cardview:cardview:1.0.0) with DexingOutputSplitTransform
Transforming resourceinspection-annotation-1.0.1.jar (androidx.resourceinspection:resourceinspection-annotation:1.0.1) with DexingOutputSplitTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with DexingOutputSplitTransform
Transforming cursoradapter-1.0.0.aar (androidx.cursoradapter:cursoradapter:1.0.0) with DexingOutputSplitTransform
Transforming profileinstaller-1.3.1.aar (androidx.profileinstaller:profileinstaller:1.3.1) with DexingOutputSplitTransform
Transforming versionedparcelable-1.1.1.aar (androidx.versionedparcelable:versionedparcelable:1.1.1) with DexingOutputSplitTransform
Transforming collection-1.2.0.jar (androidx.collection:collection:1.2.0) with DexingOutputSplitTransform
Transforming exifinterface-1.3.6.aar (androidx.exifinterface:exifinterface:1.3.6) with DexingOutputSplitTransform
Transforming core-runtime-2.2.0.aar (androidx.arch.core:core-runtime:2.2.0) with DexingOutputSplitTransform
Transforming sqlite-framework-2.4.0.aar (androidx.sqlite:sqlite-framework:2.4.0) with DexingOutputSplitTransform
Transforming sqlite-2.4.0.aar (androidx.sqlite:sqlite:2.4.0) with DexingOutputSplitTransform
Transforming gifdecoder-4.16.0.aar (com.github.bumptech.glide:gifdecoder:4.16.0) with DexingOutputSplitTransform
Transforming startup-runtime-1.1.1.aar (androidx.startup:startup-runtime:1.1.1) with DexingOutputSplitTransform
Transforming tracing-1.0.0.aar (androidx.tracing:tracing:1.0.0) with DexingOutputSplitTransform
Transforming core-common-2.2.0.jar (androidx.arch.core:core-common:2.2.0) with DexingOutputSplitTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with DexingOutputSplitTransform
Transforming interpolator-1.0.0.aar (androidx.interpolator:interpolator:1.0.0) with DexingOutputSplitTransform
Transforming concurrent-futures-1.1.0.jar (androidx.concurrent:concurrent-futures:1.1.0) with DexingOutputSplitTransform
Transforming documentfile-1.0.0.aar (androidx.documentfile:documentfile:1.0.0) with DexingOutputSplitTransform
Transforming localbroadcastmanager-1.0.0.aar (androidx.localbroadcastmanager:localbroadcastmanager:1.0.0) with DexingOutputSplitTransform
Transforming print-1.0.0.aar (androidx.print:print:1.0.0) with DexingOutputSplitTransform
Transforming annotation-experimental-1.4.0.aar (androidx.annotation:annotation-experimental:1.4.0) with DexingOutputSplitTransform
Transforming annotation-jvm-1.6.0.jar (androidx.annotation:annotation-jvm:1.6.0) with DexingOutputSplitTransform
Transforming okio-jvm-3.6.0.jar (com.squareup.okio:okio-jvm:3.6.0) with DexingOutputSplitTransform
Transforming kotlinx-coroutines-android-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1) with DexingOutputSplitTransform
Transforming kotlinx-coroutines-core-jvm-1.7.1.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1) with DexingOutputSplitTransform
Transforming kotlin-stdlib-jdk8-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10) with DexingOutputSplitTransform
Transforming kotlin-stdlib-jdk7-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10) with DexingOutputSplitTransform
Transforming kotlin-stdlib-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib:1.9.10) with DexingOutputSplitTransform
Transforming guava-32.1.3-android.jar (com.google.guava:guava:32.1.3-android) with DexingOutputSplitTransform
Transforming disklrucache-4.16.0.jar (com.github.bumptech.glide:disklrucache:4.16.0) with DexingOutputSplitTransform
Transforming annotations-4.16.0.jar (com.github.bumptech.glide:annotations:4.16.0) with DexingOutputSplitTransform
Transforming kotlin-stdlib-common-1.9.10.jar (org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10) with DexingOutputSplitTransform
Caching disabled for DexingWithClasspathTransform: /home/runner/.gradle/caches/modules-2/files-2.1/androidx.constraintlayout/constraintlayout-solver/2.0.1/30988fe2d77f3fe3bf7551bb8a8b795fad7e7226/constraintlayout-solver-2.0.1.jar because:
  Build cache is disabled
Running dexing transform non-incrementally for '/home/runner/.gradle/caches/modules-2/files-2.1/androidx.constraintlayout/constraintlayout-solver/2.0.1/30988fe2d77f3fe3bf7551bb8a8b795fad7e7226/constraintlayout-solver-2.0.1.jar'
Transforming constraintlayout-2.0.1.aar (androidx.constraintlayout:constraintlayout:2.0.1) with DexingOutputSplitTransform
Caching disabled for DexingOutputSplitTransform: /home/runner/.gradle/caches/transforms-4/b2efed2cf1c2d99ec8a4046a1d617f0a/transformed/constraintlayout-2.0.1-runtime because:
  Build cache is disabled
Transforming listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar (com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava) with DexingOutputSplitTransform
Transforming annotations-23.0.0.jar (org.jetbrains:annotations:23.0.0) with DexingOutputSplitTransform
Transforming failureaccess-1.0.1.jar (com.google.guava:failureaccess:1.0.1) with DexingOutputSplitTransform
Transforming constraintlayout-solver-2.0.1.jar (androidx.constraintlayout:constraintlayout-solver:2.0.1) with DexingOutputSplitTransform
Caching disabled for DexingOutputSplitTransform: /home/runner/.gradle/caches/transforms-4/4702c5c7a21370e6a9b705d26562cdd0/transformed/constraintlayout-solver-2.0.1 because:
  Build cache is disabled
INFO: D8: Stripped invalid locals information from 1 method.
INFO: /home/runner/.gradle/caches/transforms-4/764d721b97d482dbebc01a5a33219f1a/transformed/leanback-1.0.0-runtime.jar: D8: Methods with invalid locals information:
  void androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter.onLayoutOverviewFrame(androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter$ViewHolder, int, boolean)
  Information in locals-table is invalid with respect to the stack map table. Local refers to non-present stack map type for register: 7 with constraint INT.
INFO: D8: Some warnings are typically a sign of using an outdated Java toolchain. To fix, recompile the source with an updated toolchain.
Transforming leanback-1.0.0.aar (androidx.leanback:leanback:1.0.0) with DexingOutputSplitTransform
Caching disabled for DexingOutputSplitTransform: /home/runner/.gradle/caches/transforms-4/90305d6ede6efdf5a50ac79c0eba0e7b/transformed/leanback-1.0.0-runtime because:
  Build cache is disabled
Transforming material-1.12.0.aar (com.google.android.material:material:1.12.0) with DexingOutputSplitTransform
Caching disabled for DexingOutputSplitTransform: /home/runner/.gradle/caches/transforms-4/41df0c4de5c744cb6b762d58db56b6f6/transformed/material-1.12.0-runtime because:
  Build cache is disabled
Caching disabled for task ':tv:mergeExtDexDebug' because:
  Build cache is disabled
Task ':tv:mergeExtDexDebug' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':tv:mergeExtDexDebug'.
Merging to '/home/runner/work/wiseiptv/wiseiptv/tv/build/intermediates/dex/debug/mergeExtDexDebug' with D8 from all or a subset of dex files in /home/runner/.gradle/caches/transforms-4/41df0c4de5c744cb6b762d58db56b6f6/transformed/material-1.12.0-runtime/material-1.12.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/90305d6ede6efdf5a50ac79c0eba0e7b/transformed/leanback-1.0.0-runtime/leanback-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/26a2df71aadaebc5bb405b0e8fbe0c01/transformed/jetified-media3-exoplayer-hls-1.3.1-runtime/jetified-media3-exoplayer-hls-1.3.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/a36859de480acc6745b4303776b3970e/transformed/jetified-media3-exoplayer-dash-1.3.1-runtime/jetified-media3-exoplayer-dash-1.3.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/f2ff64e8108bd07a3a4996137a4af45a/transformed/jetified-media3-exoplayer-1.3.1-runtime/jetified-media3-exoplayer-1.3.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/d8be9394e1608b762ba2bd8914701324/transformed/jetified-media3-datasource-okhttp-1.3.1-runtime/jetified-media3-datasource-okhttp-1.3.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/c316b3be310f517d52b25e1f870651ea/transformed/jetified-media3-datasource-1.3.1-runtime/jetified-media3-datasource-1.3.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/bb216f7bbc7c5138a94af5f2a7e5b6ad/transformed/jetified-media3-extractor-1.3.1-runtime/jetified-media3-extractor-1.3.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/b69f9a285fbfafc953ad34c8d6b49e44/transformed/jetified-media3-container-1.3.1-runtime/jetified-media3-container-1.3.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/213dcd3753dd4bce7d850e6d9dc6c7e9/transformed/jetified-media3-decoder-1.3.1-runtime/jetified-media3-decoder-1.3.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/58c03afb9d982fbd1de74ce729f9baea/transformed/jetified-media3-database-1.3.1-runtime/jetified-media3-database-1.3.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/60c524c3c7e20b4104a4a36d800e3ebf/transformed/jetified-media3-common-1.3.1-runtime/jetified-media3-common-1.3.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/fd695f2dde56b66c059ec07d16faa5d8/transformed/jetified-media3-ui-1.3.1-runtime/jetified-media3-ui-1.3.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/d38bcc3bd5414e3974fe304054299782/transformed/jetified-viewpager2-1.1.0-beta02-runtime/jetified-viewpager2-1.1.0-beta02-runtime_dex, /home/runner/.gradle/caches/transforms-4/c4f2d9cbe4f65364764d9039d75036e1/transformed/recyclerview-1.3.2-runtime/recyclerview-1.3.2-runtime_dex, /home/runner/.gradle/caches/transforms-4/b2efed2cf1c2d99ec8a4046a1d617f0a/transformed/constraintlayout-2.0.1-runtime/constraintlayout-2.0.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/2e4e74bd8327e819dbf866fd3e87176d/transformed/jetified-appcompat-resources-1.7.0-runtime/jetified-appcompat-resources-1.7.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/bf284ff9277b658c03daa181fc419523/transformed/appcompat-1.7.0-runtime/appcompat-1.7.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/a129d192d065d88343b1d61f55c7f789/transformed/room-common-2.6.1/room-common-2.6.1_dex, /home/runner/.gradle/caches/transforms-4/151f2a94fbd268763d31d7a48e4d5925/transformed/room-runtime-2.6.1-runtime/room-runtime-2.6.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/817afcacb6b42505962af1500f0634b0/transformed/jetified-okhttp-4.12.0/jetified-okhttp-4.12.0_dex, /home/runner/.gradle/caches/transforms-4/a9b2f4a71ba1434cbc14986c90c13d67/transformed/jetified-glide-4.16.0-runtime/jetified-glide-4.16.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/471f437c3886fa21b9c85496e50761b0/transformed/fragment-1.5.4-runtime/fragment-1.5.4-runtime_dex, /home/runner/.gradle/caches/transforms-4/c9e1b6d97340f7ddb811b0cd2ca75e3c/transformed/jetified-activity-1.8.0-runtime/jetified-activity-1.8.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/dfaebd21ce2ae14e2511de46107ba9d9/transformed/legacy-support-core-ui-1.0.0-runtime/legacy-support-core-ui-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/ff95daa0a304c27630b2daf6aac4a0f1/transformed/coordinatorlayout-1.1.0-runtime/coordinatorlayout-1.1.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/8874401f37920fbf84eaa76a23c28e5e/transformed/drawerlayout-1.1.1-runtime/drawerlayout-1.1.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/b0620d2c064faf990170c58280f98dd5/transformed/transition-1.5.0-runtime/transition-1.5.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/11854c71ab2aadcb098365f595cf457b/transformed/dynamicanimation-1.0.0-runtime/dynamicanimation-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/65261899e80858f1ce09246c7ffb76b2/transformed/vectordrawable-animated-1.1.0-runtime/vectordrawable-animated-1.1.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/17c345bf52a22adf0cf6222f9084727c/transformed/vectordrawable-1.1.0-runtime/vectordrawable-1.1.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/3f01c057ff5cccf1461a54fc930b55f5/transformed/jetified-customview-poolingcontainer-1.0.0-runtime/jetified-customview-poolingcontainer-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/42136520b65b024a20902db7d3e4cc37/transformed/jetified-emoji2-views-helper-1.3.0-runtime/jetified-emoji2-views-helper-1.3.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/c92c3fb2d652ce30d3c85f532e1a6f02/transformed/jetified-emoji2-1.3.0-runtime/jetified-emoji2-1.3.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/3c3ce40e0e8ad66425a46ea49a02d614/transformed/jetified-savedstate-1.2.1-runtime/jetified-savedstate-1.2.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/d67cd0dc1cdc4d07f657744a68f5e781/transformed/lifecycle-common-2.7.0/lifecycle-common-2.7.0_dex, /home/runner/.gradle/caches/transforms-4/632babbe5f56a4b1af197a436febb3ac/transformed/jetified-lifecycle-process-2.7.0-runtime/jetified-lifecycle-process-2.7.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/1df2906d2a273b72f693f16a076dfdb8/transformed/jetified-lifecycle-livedata-core-ktx-2.7.0-runtime/jetified-lifecycle-livedata-core-ktx-2.7.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/64373421b7c0c9c6ccc43bb3feafa52d/transformed/lifecycle-livedata-core-2.7.0-runtime/lifecycle-livedata-core-2.7.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/811146c6b4eff7e9a20d716590becfc0/transformed/legacy-support-core-utils-1.0.0-runtime/legacy-support-core-utils-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/d8943334a071feed96005b990ab5b1ad/transformed/loader-1.0.0-runtime/loader-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/87d2fbdb07bbd4ef9d659418a1af7624/transformed/lifecycle-viewmodel-2.7.0-runtime/lifecycle-viewmodel-2.7.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/a907f079131aa71f5270653e1f832a1e/transformed/jetified-lifecycle-viewmodel-savedstate-2.7.0-runtime/jetified-lifecycle-viewmodel-savedstate-2.7.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/de55ca09673af6cbcb4e020f3f255c07/transformed/jetified-core-ktx-1.13.0-runtime/jetified-core-ktx-1.13.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/610fb8a1dd03d28d5a60ed9918ac2a04/transformed/media-1.7.0-runtime/media-1.7.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/d54aee6dcf7bc1758ea3127e1f9af069/transformed/viewpager-1.0.0-runtime/viewpager-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/3a0e3f123823fe7625647a5df3e8b0f4/transformed/slidingpanelayout-1.0.0-runtime/slidingpanelayout-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/281675e1a4cd57a58fba41b50891c84b/transformed/customview-1.1.0-runtime/customview-1.1.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/952bed646c8ada121b4680f1612c5a70/transformed/swiperefreshlayout-1.0.0-runtime/swiperefreshlayout-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/fdaba6162f9a038ca677ebe1a0187bda/transformed/asynclayoutinflater-1.0.0-runtime/asynclayoutinflater-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/ee166b4a59933a05d1ed635fb065631e/transformed/core-1.13.0-runtime/core-1.13.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/4e2476f146ed0d35cf418775e2ce0cc0/transformed/lifecycle-runtime-2.7.0-runtime/lifecycle-runtime-2.7.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/fe5e01d64adba2334df17d691c1fb51c/transformed/lifecycle-livedata-2.7.0-runtime/lifecycle-livedata-2.7.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/b7b12e7f242f9bcd98caf172e8959827/transformed/jetified-error_prone_annotations-2.15.0/jetified-error_prone_annotations-2.15.0_dex, /home/runner/.gradle/caches/transforms-4/7292786d56e17d2dae4593340293c7f5/transformed/cardview-1.0.0-runtime/cardview-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/8db1422420c4c4bf36c5fe3bef0c065b/transformed/jetified-resourceinspection-annotation-1.0.1/jetified-resourceinspection-annotation-1.0.1_dex, /home/runner/.gradle/caches/transforms-4/e0a643b506e9ad1153c87ef700c399ce/transformed/cursoradapter-1.0.0-runtime/cursoradapter-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/b9f718efceb940cb588841cba347deb1/transformed/jetified-profileinstaller-1.3.1-runtime/jetified-profileinstaller-1.3.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/2bb0857a88df80c7c62217e7874f4e93/transformed/versionedparcelable-1.1.1-runtime/versionedparcelable-1.1.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/f1148a249e2c5e6f6bd55ee4b26fbe34/transformed/collection-1.2.0/collection-1.2.0_dex, /home/runner/.gradle/caches/transforms-4/0542bf89b9a78c5c1c77ea943d5358d0/transformed/exifinterface-1.3.6-runtime/exifinterface-1.3.6-runtime_dex, /home/runner/.gradle/caches/transforms-4/2a3759a9997708119cbf394c0a9053bb/transformed/core-runtime-2.2.0-runtime/core-runtime-2.2.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/12cb3ab6a5a9025151ef1bd99b630a1a/transformed/sqlite-framework-2.4.0-runtime/sqlite-framework-2.4.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/372e368f1a7f964f67ebc42c6c0e148a/transformed/sqlite-2.4.0-runtime/sqlite-2.4.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/64a3c21e8ea683a00b652eb6c15ff6b8/transformed/jetified-gifdecoder-4.16.0-runtime/jetified-gifdecoder-4.16.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/64d8d151f09e47381db92d72852a1ef2/transformed/jetified-startup-runtime-1.1.1-runtime/jetified-startup-runtime-1.1.1-runtime_dex, /home/runner/.gradle/caches/transforms-4/e0e8c625fc485b4ab95041099c4ff6d9/transformed/jetified-tracing-1.0.0-runtime/jetified-tracing-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/13e104627d3228894ecf51dec7ce87b4/transformed/core-common-2.2.0/core-common-2.2.0_dex, /home/runner/.gradle/caches/transforms-4/4c866a31f2df6cb6181a6fccaae1e054/transformed/interpolator-1.0.0-runtime/interpolator-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/2ab1e0cf678c1a30d861ad2bb93e2fa5/transformed/jetified-concurrent-futures-1.1.0/jetified-concurrent-futures-1.1.0_dex, /home/runner/.gradle/caches/transforms-4/884315d6899cf384f140a823e64b433b/transformed/documentfile-1.0.0-runtime/documentfile-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/c6f34acffb4bf266ddce8320bb31cc21/transformed/localbroadcastmanager-1.0.0-runtime/localbroadcastmanager-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/ccb0ac02b4770368eb5d16ce5612d1d6/transformed/print-1.0.0-runtime/print-1.0.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/3ad79a9b4e4b8a6bdb1b5ff7400fbb36/transformed/jetified-annotation-experimental-1.4.0-runtime/jetified-annotation-experimental-1.4.0-runtime_dex, /home/runner/.gradle/caches/transforms-4/2d866ce8431660b7930e20bf2e14e8e0/transformed/jetified-annotation-jvm-1.6.0/jetified-annotation-jvm-1.6.0_dex, /home/runner/.gradle/caches/transforms-4/46efb7bdd0b0cec978fa87c735fb3f18/transformed/jetified-okio-jvm-3.6.0/jetified-okio-jvm-3.6.0_dex, /home/runner/.gradle/caches/transforms-4/754a30ca4e83fb095f7a095a19228e69/transformed/jetified-kotlinx-coroutines-android-1.7.1/jetified-kotlinx-coroutines-android-1.7.1_dex, /home/runner/.gradle/caches/transforms-4/ae849ee37332f5657054d40c7d8018df/transformed/jetified-kotlinx-coroutines-core-jvm-1.7.1/jetified-kotlinx-coroutines-core-jvm-1.7.1_dex, /home/runner/.gradle/caches/transforms-4/f0c7a988b7616a1a9089657c37390900/transformed/jetified-kotlin-stdlib-1.9.10/jetified-kotlin-stdlib-1.9.10_dex, /home/runner/.gradle/caches/transforms-4/05958df91425ff32f2621e5f19fc9fb2/transformed/jetified-guava-32.1.3-android/jetified-guava-32.1.3-android_dex, /home/runner/.gradle/caches/transforms-4/d1a7e766a9af4404e5e6249448003525/transformed/jetified-disklrucache-4.16.0/jetified-disklrucache-4.16.0_dex, /home/runner/.gradle/caches/transforms-4/18f6d76a21a6d1e6497bd9a010a97309/transformed/jetified-annotations-4.16.0/jetified-annotations-4.16.0_dex, /home/runner/.gradle/caches/transforms-4/4702c5c7a21370e6a9b705d26562cdd0/transformed/constraintlayout-solver-2.0.1/constraintlayout-solver-2.0.1_dex, /home/runner/.gradle/caches/transforms-4/6fb28202bc697c69d2b577426e70b945/transformed/jetified-annotations-23.0.0/jetified-annotations-23.0.0_dex, /home/runner/.gradle/caches/transforms-4/178c2fca21ae86c8a2a2c659537b85b6/transformed/jetified-failureaccess-1.0.1/jetified-failureaccess-1.0.1_dex, and from all global synthetics files in 
AAPT2 aapt2-8.3.0-10880808-linux Daemon #0: shutdown

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':tv:processDebugResources'.
> A failure occurred while executing com.android.build.gradle.internal.res.LinkApplicationAndroidResourcesTask$TaskAction
   > Android resource linking failed
     ERROR: /home/runner/work/wiseiptv/wiseiptv/tv/src/main/res/layout/dialog_add_playlist.xml:6: AAPT: error: '#null' is incompatible with attribute background (attr) reference|color.
         

* Try:
> Run with --debug option to get more log output.
> Run with --scan to get full insights.
> Get more help at https://help.gradle.org.

* Exception is:
org.gradle.api.tasks.TaskExecutionException: Execution failed for task ':tv:processDebugResources'.
	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.lambda$executeIfValid$1(ExecuteActionsTaskExecuter.java:148)
	at org.gradle.internal.Try$Failure.ifSuccessfulOrElse(Try.java:282)
	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.executeIfValid(ExecuteActionsTaskExecuter.java:146)
	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.execute(ExecuteActionsTaskExecuter.java:134)
	at org.gradle.api.internal.tasks.execution.FinalizePropertiesTaskExecuter.execute(FinalizePropertiesTaskExecuter.java:46)
	at org.gradle.api.internal.tasks.execution.ResolveTaskExecutionModeExecuter.execute(ResolveTaskExecutionModeExecuter.java:51)
	at org.gradle.api.internal.tasks.execution.SkipTaskWithNoActionsExecuter.execute(SkipTaskWithNoActionsExecuter.java:57)
	at org.gradle.api.internal.tasks.execution.SkipOnlyIfTaskExecuter.execute(SkipOnlyIfTaskExecuter.java:74)
	at org.gradle.api.internal.tasks.execution.CatchExceptionTaskExecuter.execute(CatchExceptionTaskExecuter.java:36)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.executeTask(EventFiringTaskExecuter.java:77)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.call(EventFiringTaskExecuter.java:55)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.call(EventFiringTaskExecuter.java:52)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:204)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:199)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:66)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:157)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.call(DefaultBuildOperationRunner.java:53)
	at org.gradle.internal.operations.DefaultBuildOperationExecutor.call(DefaultBuildOperationExecutor.java:73)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter.execute(EventFiringTaskExecuter.java:52)
	at org.gradle.execution.plan.LocalTaskNodeExecutor.execute(LocalTaskNodeExecutor.java:42)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$InvokeNodeExecutorsAction.execute(DefaultTaskExecutionGraph.java:331)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$InvokeNodeExecutorsAction.execute(DefaultTaskExecutionGraph.java:318)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$BuildOperationAwareExecutionAction.lambda$execute$0(DefaultTaskExecutionGraph.java:314)
	at org.gradle.internal.operations.CurrentBuildOperationRef.with(CurrentBuildOperationRef.java:80)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$BuildOperationAwareExecutionAction.execute(DefaultTaskExecutionGraph.java:314)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$BuildOperationAwareExecutionAction.execute(DefaultTaskExecutionGraph.java:303)
	at org.gradle.execution.plan.DefaultPlanExecutor$ExecutorWorker.execute(DefaultPlanExecutor.java:463)
	at org.gradle.execution.plan.DefaultPlanExecutor$ExecutorWorker.run(DefaultPlanExecutor.java:380)
	at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)
	at org.gradle.internal.concurrent.AbstractManagedExecutor$1.run(AbstractManagedExecutor.java:47)
Caused by: org.gradle.workers.internal.DefaultWorkerExecutor$WorkExecutionException: A failure occurred while executing com.android.build.gradle.internal.res.LinkApplicationAndroidResourcesTask$TaskAction
	at org.gradle.workers.internal.DefaultWorkerExecutor$WorkItemExecution.waitForCompletion(DefaultWorkerExecutor.java:283)
	at org.gradle.internal.work.DefaultAsyncWorkTracker.lambda$waitForItemsAndGatherFailures$2(DefaultAsyncWorkTracker.java:130)
	at org.gradle.internal.Factories$1.create(Factories.java:31)
	at org.gradle.internal.work.DefaultWorkerLeaseService.withoutLocks(DefaultWorkerLeaseService.java:336)
	at org.gradle.internal.work.DefaultWorkerLeaseService.withoutLocks(DefaultWorkerLeaseService.java:319)
	at org.gradle.internal.work.DefaultWorkerLeaseService.withoutLock(DefaultWorkerLeaseService.java:324)
	at org.gradle.internal.work.DefaultAsyncWorkTracker.waitForItemsAndGatherFailures(DefaultAsyncWorkTracker.java:126)
	at org.gradle.internal.work.DefaultAsyncWorkTracker.waitForItemsAndGatherFailures(DefaultAsyncWorkTracker.java:92)
	at org.gradle.internal.work.DefaultAsyncWorkTracker.waitForAll(DefaultAsyncWorkTracker.java:78)
	at org.gradle.internal.work.DefaultAsyncWorkTracker.waitForCompletion(DefaultAsyncWorkTracker.java:66)
	at org.gradle.api.internal.tasks.execution.TaskExecution$3.run(TaskExecution.java:255)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$1.execute(DefaultBuildOperationRunner.java:29)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$1.execute(DefaultBuildOperationRunner.java:26)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:66)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:157)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.run(DefaultBuildOperationRunner.java:47)
	at org.gradle.internal.operations.DefaultBuildOperationExecutor.run(DefaultBuildOperationExecutor.java:68)
	at org.gradle.api.internal.tasks.execution.TaskExecution.executeAction(TaskExecution.java:232)
	at org.gradle.api.internal.tasks.execution.TaskExecution.executeActions(TaskExecution.java:215)
	at org.gradle.api.internal.tasks.execution.TaskExecution.executeWithPreviousOutputFiles(TaskExecution.java:198)
	at org.gradle.api.internal.tasks.execution.TaskExecution.execute(TaskExecution.java:165)
	at org.gradle.internal.execution.steps.ExecuteStep.executeInternal(ExecuteStep.java:105)
	at org.gradle.internal.execution.steps.ExecuteStep.access$000(ExecuteStep.java:44)
	at org.gradle.internal.execution.steps.ExecuteStep$1.call(ExecuteStep.java:59)
	at org.gradle.internal.execution.steps.ExecuteStep$1.call(ExecuteStep.java:56)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:204)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:199)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:66)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:157)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.call(DefaultBuildOperationRunner.java:53)
	at org.gradle.internal.operations.DefaultBuildOperationExecutor.call(DefaultBuildOperationExecutor.java:73)
	at org.gradle.internal.execution.steps.ExecuteStep.execute(ExecuteStep.java:56)
	at org.gradle.internal.execution.steps.ExecuteStep.execute(ExecuteStep.java:44)
	at org.gradle.internal.execution.steps.CancelExecutionStep.execute(CancelExecutionStep.java:41)
	at org.gradle.internal.execution.steps.TimeoutStep.executeWithoutTimeout(TimeoutStep.java:74)
	at org.gradle.internal.execution.steps.TimeoutStep.execute(TimeoutStep.java:55)
	at org.gradle.internal.execution.steps.PreCreateOutputParentsStep.execute(PreCreateOutputParentsStep.java:50)
	at org.gradle.internal.execution.steps.PreCreateOutputParentsStep.execute(PreCreateOutputParentsStep.java:28)
	at org.gradle.internal.execution.steps.RemovePreviousOutputsStep.execute(RemovePreviousOutputsStep.java:67)
	at org.gradle.internal.execution.steps.RemovePreviousOutputsStep.execute(RemovePreviousOutputsStep.java:37)
	at org.gradle.internal.execution.steps.BroadcastChangingOutputsStep.execute(BroadcastChangingOutputsStep.java:61)
	at org.gradle.internal.execution.steps.BroadcastChangingOutputsStep.execute(BroadcastChangingOutputsStep.java:26)
	at org.gradle.internal.execution.steps.CaptureOutputsAfterExecutionStep.execute(CaptureOutputsAfterExecutionStep.java:64)
	at org.gradle.internal.execution.steps.CaptureOutputsAfterExecutionStep.execute(CaptureOutputsAfterExecutionStep.java:42)
	at org.gradle.internal.execution.steps.ResolveInputChangesStep.execute(ResolveInputChangesStep.java:40)
	at org.gradle.internal.execution.steps.ResolveInputChangesStep.execute(ResolveInputChangesStep.java:29)
	at org.gradle.internal.execution.steps.BuildCacheStep.executeWithoutCache(BuildCacheStep.java:189)
	at org.gradle.internal.execution.steps.BuildCacheStep.lambda$execute$1(BuildCacheStep.java:75)
	at org.gradle.internal.Either$Right.fold(Either.java:175)
	at org.gradle.internal.execution.caching.CachingState.fold(CachingState.java:59)
	at org.gradle.internal.execution.steps.BuildCacheStep.execute(BuildCacheStep.java:73)
	at org.gradle.internal.execution.steps.BuildCacheStep.execute(BuildCacheStep.java:48)
	at org.gradle.internal.execution.steps.StoreExecutionStateStep.execute(StoreExecutionStateStep.java:44)
	at org.gradle.internal.execution.steps.StoreExecutionStateStep.execute(StoreExecutionStateStep.java:33)
	at org.gradle.internal.execution.steps.SkipUpToDateStep.executeBecause(SkipUpToDateStep.java:76)
	at org.gradle.internal.execution.steps.SkipUpToDateStep.lambda$execute$2(SkipUpToDateStep.java:54)
	at org.gradle.internal.execution.steps.SkipUpToDateStep.execute(SkipUpToDateStep.java:54)
	at org.gradle.internal.execution.steps.SkipUpToDateStep.execute(SkipUpToDateStep.java:36)
	at org.gradle.internal.execution.steps.ResolveChangesStep.execute(ResolveChangesStep.java:65)
	at org.gradle.internal.execution.steps.ResolveChangesStep.execute(ResolveChangesStep.java:36)
	at org.gradle.internal.execution.steps.legacy.MarkSnapshottingInputsFinishedStep.execute(MarkSnapshottingInputsFinishedStep.java:37)
	at org.gradle.internal.execution.steps.legacy.MarkSnapshottingInputsFinishedStep.execute(MarkSnapshottingInputsFinishedStep.java:27)
	at org.gradle.internal.execution.steps.ResolveCachingStateStep.execute(ResolveCachingStateStep.java:76)
	at org.gradle.internal.execution.steps.ResolveCachingStateStep.execute(ResolveCachingStateStep.java:37)
	at org.gradle.internal.execution.steps.ValidateStep.execute(ValidateStep.java:106)
	at org.gradle.internal.execution.steps.ValidateStep.execute(ValidateStep.java:55)
	at org.gradle.internal.execution.steps.AbstractCaptureStateBeforeExecutionStep.execute(AbstractCaptureStateBeforeExecutionStep.java:64)
	at org.gradle.internal.execution.steps.AbstractCaptureStateBeforeExecutionStep.execute(AbstractCaptureStateBeforeExecutionStep.java:43)
	at org.gradle.internal.execution.steps.AbstractSkipEmptyWorkStep.executeWithNonEmptySources(AbstractSkipEmptyWorkStep.java:125)
	at org.gradle.internal.execution.steps.AbstractSkipEmptyWorkStep.execute(AbstractSkipEmptyWorkStep.java:56)
	at org.gradle.internal.execution.steps.AbstractSkipEmptyWorkStep.execute(AbstractSkipEmptyWorkStep.java:36)
	at org.gradle.internal.execution.steps.legacy.MarkSnapshottingInputsStartedStep.execute(MarkSnapshottingInputsStartedStep.java:38)
	at org.gradle.internal.execution.steps.LoadPreviousExecutionStateStep.execute(LoadPreviousExecutionStateStep.java:36)
	at org.gradle.internal.execution.steps.LoadPreviousExecutionStateStep.execute(LoadPreviousExecutionStateStep.java:23)
	at org.gradle.internal.execution.steps.HandleStaleOutputsStep.execute(HandleStaleOutputsStep.java:75)
	at org.gradle.internal.execution.steps.HandleStaleOutputsStep.execute(HandleStaleOutputsStep.java:41)
	at org.gradle.internal.execution.steps.AssignMutableWorkspaceStep.lambda$execute$0(AssignMutableWorkspaceStep.java:35)
	at org.gradle.api.internal.tasks.execution.TaskExecution$4.withWorkspace(TaskExecution.java:292)
	at org.gradle.internal.execution.steps.AssignMutableWorkspaceStep.execute(AssignMutableWorkspaceStep.java:31)
	at org.gradle.internal.execution.steps.AssignMutableWorkspaceStep.execute(AssignMutableWorkspaceStep.java:22)
	at org.gradle.internal.execution.steps.ChoosePipelineStep.execute(ChoosePipelineStep.java:40)
	at org.gradle.internal.execution.steps.ChoosePipelineStep.execute(ChoosePipelineStep.java:23)
	at org.gradle.internal.execution.steps.ExecuteWorkBuildOperationFiringStep.lambda$execute$2(ExecuteWorkBuildOperationFiringStep.java:66)
	at org.gradle.internal.execution.steps.ExecuteWorkBuildOperationFiringStep.execute(ExecuteWorkBuildOperationFiringStep.java:66)
	at org.gradle.internal.execution.steps.ExecuteWorkBuildOperationFiringStep.execute(ExecuteWorkBuildOperationFiringStep.java:38)
	at org.gradle.internal.execution.steps.IdentityCacheStep.execute(IdentityCacheStep.java:36)
	at org.gradle.internal.execution.steps.IdentityCacheStep.execute(IdentityCacheStep.java:26)
	at org.gradle.internal.execution.steps.IdentifyStep.execute(IdentifyStep.java:47)
	at org.gradle.internal.execution.steps.IdentifyStep.execute(IdentifyStep.java:34)
	at org.gradle.internal.execution.impl.DefaultExecutionEngine$1.execute(DefaultExecutionEngine.java:61)
	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.executeIfValid(ExecuteActionsTaskExecuter.java:145)
	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.execute(ExecuteActionsTaskExecuter.java:134)
	at org.gradle.api.internal.tasks.execution.FinalizePropertiesTaskExecuter.execute(FinalizePropertiesTaskExecuter.java:46)
	at org.gradle.api.internal.tasks.execution.ResolveTaskExecutionModeExecuter.execute(ResolveTaskExecutionModeExecuter.java:51)
	at org.gradle.api.internal.tasks.execution.SkipTaskWithNoActionsExecuter.execute(SkipTaskWithNoActionsExecuter.java:57)
	at org.gradle.api.internal.tasks.execution.SkipOnlyIfTaskExecuter.execute(SkipOnlyIfTaskExecuter.java:74)
	at org.gradle.api.internal.tasks.execution.CatchExceptionTaskExecuter.execute(CatchExceptionTaskExecuter.java:36)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.executeTask(EventFiringTaskExecuter.java:77)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.call(EventFiringTaskExecuter.java:55)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.call(EventFiringTaskExecuter.java:52)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:204)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:199)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:66)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:157)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.call(DefaultBuildOperationRunner.java:53)
	at org.gradle.internal.operations.DefaultBuildOperationExecutor.call(DefaultBuildOperationExecutor.java:73)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter.execute(EventFiringTaskExecuter.java:52)
	at org.gradle.execution.plan.LocalTaskNodeExecutor.execute(LocalTaskNodeExecutor.java:42)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$InvokeNodeExecutorsAction.execute(DefaultTaskExecutionGraph.java:331)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$InvokeNodeExecutorsAction.execute(DefaultTaskExecutionGraph.java:318)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$BuildOperationAwareExecutionAction.lambda$execute$0(DefaultTaskExecutionGraph.java:314)
	at org.gradle.internal.operations.CurrentBuildOperationRef.with(CurrentBuildOperationRef.java:80)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$BuildOperationAwareExecutionAction.execute(DefaultTaskExecutionGraph.java:314)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$BuildOperationAwareExecutionAction.execute(DefaultTaskExecutionGraph.java:303)
	at org.gradle.execution.plan.DefaultPlanExecutor$ExecutorWorker.execute(DefaultPlanExecutor.java:463)
	at org.gradle.execution.plan.DefaultPlanExecutor$ExecutorWorker.run(DefaultPlanExecutor.java:380)
	at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)
	at org.gradle.internal.concurrent.AbstractManagedExecutor$1.run(AbstractManagedExecutor.java:47)
Caused by: com.android.builder.internal.aapt.v2.Aapt2Exception: Android resource linking failed
ERROR: /home/runner/work/wiseiptv/wiseiptv/tv/src/main/res/layout/dialog_add_playlist.xml:6: AAPT: error: '#null' is incompatible with attribute background (attr) reference|color.
    
	at com.android.builder.internal.aapt.v2.Aapt2Exception$Companion.create(Aapt2Exception.kt:45)
	at com.android.builder.internal.aapt.v2.Aapt2Exception$Companion.create$default(Aapt2Exception.kt:33)
	at com.android.build.gradle.internal.res.Aapt2ErrorUtils.rewriteException(Aapt2ErrorUtils.kt:262)
	at com.android.build.gradle.internal.res.Aapt2ErrorUtils.rewriteLinkException(Aapt2ErrorUtils.kt:133)
	at com.android.build.gradle.internal.res.Aapt2ProcessResourcesRunnableKt.processResources(Aapt2ProcessResourcesRunnable.kt:76)
	at com.android.build.gradle.internal.res.LinkApplicationAndroidResourcesTask$Companion.invokeAaptForSplit(LinkApplicationAndroidResourcesTask.kt:941)
	at com.android.build.gradle.internal.res.LinkApplicationAndroidResourcesTask$Companion.access$invokeAaptForSplit(LinkApplicationAndroidResourcesTask.kt:796)
	at com.android.build.gradle.internal.res.LinkApplicationAndroidResourcesTask$TaskAction.run(LinkApplicationAndroidResourcesTask.kt:432)
	at com.android.build.gradle.internal.profile.ProfileAwareWorkAction.execute(ProfileAwareWorkAction.kt:74)
	at org.gradle.workers.internal.DefaultWorkerServer.execute(DefaultWorkerServer.java:63)
	at org.gradle.workers.internal.NoIsolationWorkerFactory$1$1.create(NoIsolationWorkerFactory.java:66)
	at org.gradle.workers.internal.NoIsolationWorkerFactory$1$1.create(NoIsolationWorkerFactory.java:62)
	at org.gradle.internal.classloader.ClassLoaderUtils.executeInClassloader(ClassLoaderUtils.java:100)
	at org.gradle.workers.internal.NoIsolationWorkerFactory$1.lambda$execute$0(NoIsolationWorkerFactory.java:62)
	at org.gradle.workers.internal.AbstractWorker$1.call(AbstractWorker.java:44)
	at org.gradle.workers.internal.AbstractWorker$1.call(AbstractWorker.java:41)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:204)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:199)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:66)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:157)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.call(DefaultBuildOperationRunner.java:53)
	at org.gradle.internal.operations.DefaultBuildOperationExecutor.call(DefaultBuildOperationExecutor.java:73)
	at org.gradle.workers.internal.AbstractWorker.executeWrappedInBuildOperation(AbstractWorker.java:41)
	at org.gradle.workers.internal.NoIsolationWorkerFactory$1.execute(NoIsolationWorkerFactory.java:59)
	at org.gradle.workers.internal.DefaultWorkerExecutor.lambda$submitWork$0(DefaultWorkerExecutor.java:170)
	at org.gradle.internal.work.DefaultConditionalExecutionQueue$ExecutionRunner.runExecution(DefaultConditionalExecutionQueue.java:187)
	at org.gradle.internal.work.DefaultConditionalExecutionQueue$ExecutionRunner.access$700(DefaultConditionalExecutionQueue.java:120)
	at org.gradle.internal.work.DefaultConditionalExecutionQueue$ExecutionRunner$1.run(DefaultConditionalExecutionQueue.java:162)
	at org.gradle.internal.Factories$1.create(Factories.java:31)
	at org.gradle.internal.work.DefaultWorkerLeaseService.withLocks(DefaultWorkerLeaseService.java:264)
	at org.gradle.internal.work.DefaultWorkerLeaseService.runAsWorkerThread(DefaultWorkerLeaseService.java:128)
	at org.gradle.internal.work.DefaultWorkerLeaseService.runAsWorkerThread(DefaultWorkerLeaseService.java:133)
	at org.gradle.internal.work.DefaultConditionalExecutionQueue$ExecutionRunner.runBatch(DefaultConditionalExecutionQueue.java:157)
	at org.gradle.internal.work.DefaultConditionalExecutionQueue$ExecutionRunner.run(DefaultConditionalExecutionQueue.java:126)
	... 2 more


BUILD FAILED in 38s
37 actionable tasks: 37 executed
════════════ ERREURS DE COMPILATION ════════════
> Task :tv:mergeDebugNativeDebugMetadata NO-SOURCE
> Task :core:generateDebugResValues
> Task :core:generateDebugResources
> Task :core:packageDebugResources
> Task :core:parseDebugLocalResources
> Task :tv:javaPreCompileDebug
> Task :core:javaPreCompileDebug
> Task :core:generateDebugRFile
> Task :core:compileDebugLibraryResources
> Task :core:writeDebugAarMetadata
> Task :core:compileDebugJavaWithJavac
> Task :core:bundleLibCompileToJarDebug
> Task :tv:generateDebugResValues
> Task :tv:checkDebugAarMetadata
> Task :tv:mapDebugSourceSetPaths
> Task :tv:generateDebugResources
> Task :tv:mergeDebugResources
> Task :tv:packageDebugResources
> Task :tv:parseDebugLocalResources
> Task :tv:createDebugCompatibleScreenManifests
> Task :core:extractDeepLinksDebug
> Task :tv:extractDeepLinksDebug
> Task :core:mergeDebugShaders
> Task :core:compileDebugShaders NO-SOURCE
> Task :core:packageDebugAssets
> Task :tv:mergeDebugShaders
> Task :tv:compileDebugShaders NO-SOURCE
> Task :core:processDebugManifest
> Task :tv:mergeDebugAssets
> Task :tv:processDebugMainManifest
> Task :tv:mergeDebugResources
> Task :tv:processDebugManifest
> Task :tv:compressDebugAssets
> Task :core:bundleLibRuntimeToJarDebug
> Task :tv:desugarDebugFileDependencies
> Task :core:processDebugJavaRes
> Task :tv:processDebugJavaRes NO-SOURCE
> Task :tv:processDebugManifestForPackage
> Task :tv:checkDebugDuplicateClasses
> Task :tv:mergeDebugJavaResource
> Task :tv:processDebugResources
> Task :tv:processDebugResources FAILED
> Task :tv:mergeExtDexDebug
Caused by: org.gradle.workers.internal.DefaultWorkerExecutor$WorkExecutionException: A failure occurred while executing com.android.build.gradle.internal.res.LinkApplicationAndroidResourcesTask$TaskAction
Caused by: com.android.builder.internal.aapt.v2.Aapt2Exception: Android resource linking failed
BUILD FAILED in 38s


🔨 Lancement de la compilation Gradle...
Initialized native services in: /home/runner/.gradle/native
Initialized jansi services in: /home/runner/.gradle/native

Welcome to Gradle 8.6!

Here are the highlights of this release:
 - Configurable encryption key for configuration cache
 - Build init improvements
 - Build authoring improvements
Resolve mutations for :mobile:writeDebugSigningConfigVersions (Thread[Execution worker,5,main]) started.
:mobile:writeDebugSigningConfigVersions (Thread[Execution worker,5,main]) started.

> Task :mobile:writeDebugSigningConfigVersions
Caching disabled for task ':mobile:writeDebugSigningConfigVersions' because:
  Build cache is disabled
Task ':mobile:writeDebugSigningConfigVersions' is not up-to-date because:
  No history is available.

> Task :mobile:mergeLibDexDebug
Caching disabled for task ':mobile:mergeLibDexDebug' because:
  Build cache is disabled
Task ':mobile:mergeLibDexDebug' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':mobile:mergeLibDexDebug'.
Merging to '/home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/dex/debug/mergeLibDexDebug/0' with D8 from all or a subset of dex files in /home/runner/work/wiseiptv/wiseiptv/core/build/.transforms/df7e8a31370dd770ec6077b8f8625ffb/transformed/classes/classes_dex, and from all global synthetics files in 

> Task :mobile:mergeProjectDexDebug
Caching disabled for task ':mobile:mergeProjectDexDebug' because:
  Build cache is disabled
Task ':mobile:mergeProjectDexDebug' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':mobile:mergeProjectDexDebug'.
Merging to '/home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/dex/debug/mergeProjectDexDebug/0' with D8 from all or a subset of dex files in /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out/06b6b5c0af573d74a44cb1124b21f25fb59aea8dcb9bcc1329730e0200f8f831_0.jar, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out/06b6b5c0af573d74a44cb1124b21f25fb59aea8dcb9bcc1329730e0200f8f831_1.jar, and from all global synthetics files in 
Merging to '/home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/dex/debug/mergeProjectDexDebug/6' with D8 from all or a subset of dex files in /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, and from all global synthetics files in 
Merging to '/home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/dex/debug/mergeProjectDexDebug/4' with D8 from all or a subset of dex files in /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, and from all global synthetics files in 
Merging to '/home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/dex/debug/mergeProjectDexDebug/8' with D8 from all or a subset of dex files in /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, and from all global synthetics files in 
Merging to '/home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/dex/debug/mergeProjectDexDebug/10' with D8 from all or a subset of dex files in /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, /home/runner/work/wiseiptv/wiseiptv/mobile/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out, and from all global synthetics files in 
Resolve mutations for :mobile:packageDebug (Thread[included builds,5,main]) started.
:mobile:packageDebug (Thread[included builds,5,main]) started.

> Task :mobile:packageDebug
Caching disabled for task ':mobile:packageDebug' because:
  Build cache is disabled
Task ':mobile:packageDebug' is not up-to-date because:
  No history is available.
The input changes require a full rebuild for incremental task ':mobile:packageDebug'.
Resolve mutations for :mobile:createDebugApkListingFileRedirect (Thread[included builds,5,main]) started.
:mobile:createDebugApkListingFileRedirect (Thread[included builds,5,main]) started.

> Task :mobile:createDebugApkListingFileRedirect
Caching disabled for task ':mobile:createDebugApkListingFileRedirect' because:
  Build cache is disabled
Task ':mobile:createDebugApkListingFileRedirect' is not up-to-date because:
  No history is available.
Resolve mutations for :mobile:assembleDebug (Thread[included builds,5,main]) started.
:mobile:assembleDebug (Thread[included builds,5,main]) started.

> Task :mobile:assembleDebug
Skipping task ':mobile:assembleDebug' as it has no actions.
AAPT2 aapt2-8.3.0-10880808-linux Daemon #0: shutdown

BUILD SUCCESSFUL in 22s
50 actionable tasks: 50 executed
/home/runner/work/_temp/4c8bdda1-077f-4b1a-830b-9dc9fbb36dda.sh: line 34: syntax error: unexpected end of file
Error: Process completed with exit code 2.