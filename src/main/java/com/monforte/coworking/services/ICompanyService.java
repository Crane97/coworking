package com.monforte.coworking.services;

import com.monforte.coworking.domain.entities.Company;

import java.util.List;

public interface ICompanyService {

    List<Company> getCompanys();

    Company getCompanyById(Integer id);

    Company addCompany(Company company);

    Company updateCompany(Company company);

    void deleteCompany(Integer id);
}
