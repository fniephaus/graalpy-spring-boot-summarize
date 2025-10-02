plugins {
	java
	id("org.springframework.boot") version "4.0.0-M3"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.graalvm.buildtools.native") version "0.11.0"
    id("org.graalvm.python") version "25.0.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

graalPy {
    packages.set(listOf(
        "--extra-index-url=https://lafo.ssw.uni-linz.ac.at/pub/demowheels/simple/",

        "markitdown[all]==0.1.3",
        "beautifulsoup4==4.13.5",
        "charset_normalizer==3.4.3",
        "magika==0.6.2",
        "markdownify==1.2.0",
        "azure_ai_documentintelligence==1.0.2",
        "azure_identity==1.25.0",
        "lxml==6.0.1",
        "mammoth==1.10.0",
        "olefile==0.47",
        "openpyxl==3.1.5",
        "pandas==2.2.3",
        "pdfminer_six==20250506",
        "python_pptx==1.0.2",
        "speechrecognition==3.14.3",
        "xlrd==2.0.2",
        "youtube_transcript_api==1.0.3",
        "onnxruntime==1.17.1",
        "numpy==2.2.6",
        "cobble==0.1.4",
        "isodate==0.7.2",
        "azure_core==1.35.1",
        "cryptography==46.0.1",
        "msal==1.33.0",
        "msal_extensions==1.3.1",
        "soupsieve==2.8",
        "et_xmlfile==2.0.0",
        "pillow==11.3.0",
        "xlsxwriter==3.2.9",
        "cffi==2.0.0",
        "coloredlogs==15.0.1",
        "flatbuffers==25.2.10",
        "protobuf==6.32.1",
        "pycparser==2.23",
        "humanfriendly==10.0",

        "huggingface==0.0.1",
        "transformers==4.56.1",
        "PyYAML==6.0.2",
        "regex==2025.9.18",
        "tokenizers==0.22.1",
        "safetensors==0.6.2",
        "hf_xet==1.1.10",

        "transformers[torch]==4.56.1",
        "psutil==7.1.0",
        "MarkupSafe==3.0.2",
        "torch==2.7.0",
    ))
}

tasks.bootRun {
    jvmArgs = listOf(
        "--enable-native-access=ALL-UNNAMED",
        "--sun-misc-unsafe-memory-access=allow",
        "-Xss16M",
    )
}

tasks.bootJar {
    launchScript()
}

tasks.withType<Test> {
	useJUnitPlatform()
}
