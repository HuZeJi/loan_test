package com.victor_jimenez.test.service;

import com.victor_jimenez.test.model.LoanTermDTO;
import com.victor_jimenez.test.repository.LoanTermRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class LoanTermSvcImpl implements LoanTermSvc {

    private final LoanTermRepository loanTermRepository;

    public LoanTermSvcImpl(LoanTermRepository loanTermRepository) {
        this.loanTermRepository = loanTermRepository;
    }

    @Override
    public List<LoanTermDTO> list() {
        return loanTermRepository.findAll().stream().map(LoanTermDTO::toDTO).toList();
    }
}
