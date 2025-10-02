# GraalPy Spring Boot Summarization Demo

This demo embeds [`markitdown`](https://github.com/microsoft/markitdown) and [`transformers`](https://github.com/huggingface/transformers) via [GraalPy](https://www.graalvm.org/python/) in a [Spring Boot](https://spring.io) app.


## Running the application


Install GraalVM 25 and set the value of `JAVA_HOME` accordingly. We recommend using SDKMAN!: `sdk install java 25-graal`. To start the demo, simply run:

```shell script
./gradlew bootRun
```
> **_NOTE:_** Prebuilt binary wheels for this demo are only available for Linux x64 and macOS aarch64. On other platforms, building the application may take some time as numerous Python packages with native extensions may need to be built from source.

## Endpoints

**/hello**: says hi from GraalPy
```shell script
$ curl http://localhost:8080/hello
Hi Sprign Boot from GraalPy
```

**/convert**: converts a file to text using `markitdown`
```shell script
$ curl http://localhost:8080/convert -F "file=@/path/to/test.pdf"
Lorem ipsum...
```

**/summarize**: summarizes text converted from a file using `markitdown` and `transformers`
```shell script
curl http://localhost:8080/convert -F "file=@/path/to/test.pdf"
Summary of lorem ipsum...
```
> **_NOTE:_** The selected model (*HuggingFaceTB/SmolLM2-360M*) is not able to handle a lot of text. If you want to summarize larger amounts of text, consider using a different model.

Example files are available in the [`markitdown` repository](https://github.com/microsoft/markitdown/tree/8a9d8f15936b2068bcb39ccc8d3b317f93784d86/packages/markitdown/tests/test_files).

## GraalVM Native Image

You can compile this demo ahead-of-time into a native image and run it using the following commands:

```shell script
./gradlew nativeCompile
./build/native/nativeCompile/demo
```

> **_NOTE:_** Since this demo uses many Python dependencies, the native image needs ~20GB of memory to compile and is quite large in file size (~1.2GB on macOS).
> Also, don't expect significant startup improvements as `numpy`, `pandas`, and other Python packages still need to be loaded.
> Nonetheless, the native image requires less memory and CPU to run the same workload.
