package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nuist.dto.request.JudgeSubmissionDTO;
import org.nuist.dto.response.JudgeResultDTO;
import org.nuist.service.JudgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/online-judge")
@Tag(name = "online-judge", description = "编译、判题相关接口")
@RequiredArgsConstructor
public class OnlineJudgeController {

    private final JudgeService judgeService;

    @PostMapping("/submit")
    public ResponseEntity<JudgeResultDTO> submitJudgement(@RequestBody JudgeSubmissionDTO dto) {
        return ResponseEntity.ok(judgeService.judge(
                dto.getLanguage(), dto.getCode(), dto.getInput(), dto.getExpectedOutput()
        ));
    }
}
