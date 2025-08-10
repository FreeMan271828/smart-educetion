package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.nuist.bo.ProblemBankBO;
import org.nuist.dto.ProblemBankDTO;
import org.nuist.service.ProblemBankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/problemBank")
@Tag(name = "problemBank", description = "题库管理API")
@SecurityRequirement(name = "BearerAuth")
public class ProblemBankController {

    private final ProblemBankService problemBankService;

    public ProblemBankController(ProblemBankService problemBankService) {
        this.problemBankService = problemBankService;
    }



    @GetMapping("/{id}")
    public ProblemBankBO getProblemBankById(@PathVariable Long id){
        return ProblemBankBO.fromProblemBankPO(problemBankService.getById(id));
    }

    @PostMapping("/save")
    public Map<String, Object> saveProblemBank(@RequestBody ProblemBankDTO problemBankDTO){
        return problemBankService.saveProblemBank(problemBankDTO);
    }

    @PostMapping("/batchSave")
    public Map<String, Object> batchSaveProblemBank(@RequestBody List<ProblemBankDTO> problemBankDTOList){
        return problemBankService.batchSaveProblemBank(problemBankDTOList);
    }

    @PostMapping("/update/{id}")
    public Map<String, Object> updateProblemBank(@PathVariable Long id,
            @RequestBody ProblemBankDTO problemBankDTO){
        return problemBankService.updateProblemBank(problemBankDTO, id);
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteProblemBank(@PathVariable Long id){
        return problemBankService.deleteProblemBank(id);
    }

    @GetMapping("/search")
    public List<ProblemBankBO> searchProblemBank(@RequestParam String keyword){
        return problemBankService.searchProblemBank(keyword);
    }

    @Operation(summary = "分页获取题库")
    @GetMapping("/page")
    public Map<String, Object> pageProblemBank(@RequestParam int pageNum,
            @RequestParam int pageSize){
        return problemBankService.pageProblemBank(pageNum, pageSize);
    }
}
