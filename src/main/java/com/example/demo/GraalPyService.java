package com.example.demo;

import com.example.demo.GraalPyContextConfiguration.GraalPyContext;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.stereotype.Service;

@Service
@ImportRuntimeHints(GraalPyService.GraalPyServiceRuntimeHints.class)
public class GraalPyService {
    private final HelloFunction helloFunction;
    private final ConvertFunction convertFunction;
    private final SummarizeFunction summarizeFunction;

    GraalPyService(GraalPyContext graalPyContext) {
        graalPyContext.eval("""
                import os
                from markitdown import MarkItDown, StreamInfo
                from transformers import AutoModelForCausalLM, AutoTokenizer
                
                
                checkpoint = "HuggingFaceTB/SmolLM2-360M"
                tokenizer = AutoTokenizer.from_pretrained(checkpoint)
                model = AutoModelForCausalLM.from_pretrained(checkpoint)
                
                
                def hello(name):
                    return f"Hi {name} from GraalPy"
                
                
                def convert(filename, filepath):
                    md = MarkItDown(enable_plugins=False)
                    info = StreamInfo(filename=filename, extension=os.path.splitext(filename)[1])
                    return md.convert(filepath, stream_info=info).text_content.replace("-\\n", "").replace("\\n", " ")
                
                
                def summarize(text):
                    input_text = text + "\\n\\nIn summary, tl;dr in one sentence the above says the following:\\n"
                    inputs = tokenizer.encode(input_text, return_tensors="pt")
                    output = model.generate(inputs)
                    generated_text = tokenizer.decode(output[0], skip_special_tokens=True)
                    result = generated_text[len(input_text):]
                    idx = result.find('.')
                    return result[:idx+1] if idx != -1 else result
                """);
        helloFunction = graalPyContext.getFunction("hello", HelloFunction.class);
        convertFunction = graalPyContext.getFunction("convert", ConvertFunction.class);
        summarizeFunction = graalPyContext.getFunction("summarize", SummarizeFunction.class);
    }

    @FunctionalInterface
    interface HelloFunction {
        String apply(String name);
    }

    @FunctionalInterface
    interface ConvertFunction {
        String apply(String filename, String filepath);
    }

    @FunctionalInterface
    interface SummarizeFunction {
        String apply(String text);
    }

    public String hello(String name) {
        return helloFunction.apply(name);
    }

    public String convert(String filename, String filepath) {
        return convertFunction.apply(filename, filepath);
    }

    public String summarize(String text) {
        return summarizeFunction.apply(text);
    }

    static class GraalPyServiceRuntimeHints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.proxies().registerJdkProxy(HelloFunction.class);
            hints.proxies().registerJdkProxy(ConvertFunction.class);
            hints.proxies().registerJdkProxy(SummarizeFunction.class);
        }
    }
}
