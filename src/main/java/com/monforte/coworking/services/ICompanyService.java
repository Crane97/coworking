package com.monforte.coworking.services;

import com.monforte.coworking.domain.entities.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICompanyService {

    Page<Company> getCompanys(Pageable pageable);

    Company getCompanyById(Integer id);

    Company addCompany(Company company);

    Company updateCompany(Company company);

    void deleteCompany(Integer id);
}
