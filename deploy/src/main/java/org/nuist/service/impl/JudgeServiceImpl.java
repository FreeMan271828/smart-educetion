package org.nuist.service.impl;

import org.nuist.dto.response.JudgeResultDTO;
import org.nuist.service.JudgeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class JudgeServiceImpl implements JudgeService {

    @Override
    public JudgeResultDTO judge(String lang, String code, String input, String expectedOutput) {
        try {
            // 将源码写入临时目录的对应源代码文件
            Path work = Files.createTempDirectory("submission");
            Files.writeString(work.resolve(sourceFilename(lang)), code);

            JudgeResultDTO result = new JudgeResultDTO();

            // 执行编译
            ProcessBuilder compilePb = compileProcessBuilder(lang);
            if (compilePb != null) {    // 无需编译的语言就跳过这一步
                compilePb.directory(work.toFile());
                Process compile = compilePb.start();
                boolean compiled = compile.waitFor(10, TimeUnit.SECONDS);
                // 检查编译结果

                if (!compiled || compile.exitValue() != 0) {
                    result.setStatus("COMPILATION_ERROR");
                    result.setStderr(compile.getErrorStream().toString());
                    return result;
                }
            }

            // 运行脚本
            long startTime = System.currentTimeMillis();
            ProcessBuilder runPb = runProcessBuilder(lang, work);
            runPb.directory(work.toFile());
            Process run = runPb.start();
            if (input != null) {    // 将测试样例input内容输入程序
                try (OutputStream os = run.getOutputStream()) {
                    os.write(input.getBytes());
                }
            }
            boolean finished = run.waitFor(5, TimeUnit.SECONDS);
            long elapsedTime = System.currentTimeMillis() - startTime;
            result.setTimeMs(elapsedTime);
            if (!finished) {
                run.destroyForcibly();
                result.setStatus("TIME_LIMIT_EXCEEDED");
            } else if (run.exitValue() != 0) {
                result.setStatus("RUNTIME_ERROR");
            } else {
                // 运行成功，比对结果
                String stdout = new String(run.getInputStream().readAllBytes());
                String stderr = new String(run.getErrorStream().readAllBytes());
                stdout = stdout.trim();
                result.setStdout(stdout);
                result.setStderr(stderr);

                if (expectedOutput.equals(stdout)) {
                    result.setStatus("ACCEPTED");
                } else {
                    result.setStatus("WRONG_ANSWER");
                }
            }
            return result;

        } catch (IOException | InterruptedException exception) {
            JudgeResultDTO result = new JudgeResultDTO();
            result.setStatus("SYSTEM_ERROR");
            result.setStderr(exception.getMessage());
            return result;
        }

    }

    private String sourceFilename(String lang) {
        return switch (lang) {
            case "java" -> "Main.java";
            case "python" -> "main.py";
            case "cpp" -> "main.cpp";
            default -> "main.txt";
        };
    }

    private ProcessBuilder compileProcessBuilder(String lang) {
        return switch (lang) {
            case "java" -> new ProcessBuilder("javac", "Main.java");
            case "python" -> null;    // python无需编译
            case "cpp" -> new ProcessBuilder("g++", "main.cpp", "-O2", "-std=c++17", "-o", "main");
            default -> throw new IllegalArgumentException("unsupported language " + lang);
        };
    }

    private ProcessBuilder runProcessBuilder(String lang, Path work) {
        return switch (lang) {
            case "java" -> new ProcessBuilder("java", "-cp", ".", "Main");
            case "cpp" -> new ProcessBuilder(work.resolve("main").toAbsolutePath().toString());
            case "python" -> new ProcessBuilder("python3", "main.py");
            default -> throw new IllegalArgumentException("unsupported language");
        };
    }
}
