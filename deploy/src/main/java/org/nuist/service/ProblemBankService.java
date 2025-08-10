package org.nuist.service;

import org.nuist.bo.ProblemBankBO;
import org.nuist.dto.ProblemBankDTO;
import org.nuist.po.ProblemBankPO;

import java.util.List;
import java.util.Map;

public interface ProblemBankService {


     ProblemBankPO getById(Long id);


     Map<String, Object> saveProblemBank(ProblemBankDTO problemBankDTO);

     Map<String, Object> updateProblemBank(ProblemBankDTO problemBankDTO, Long id);

     Map<String, Object> deleteProblemBank(Long id);

     List<ProblemBankBO> searchProblemBank(String keyword);

     Map<String,Object> pageProblemBank(int pageNum, int pageSize);

     Map<String, Object> batchSaveProblemBank(List<ProblemBankDTO> problemBankDTOList);


}
