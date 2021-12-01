package com.monforte.coworking.services;

import com.monforte.coworking.entities.Company;
import com.monforte.coworking.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    public CompanyRepository companyRepository;

    public List<Company> getCompanys() { return companyRepository.findAll(); }

    public Company getCompanyById(Integer id){ return companyRepository.findById(id).get(); }

    public Company addCompany(Company company){ return companyRepository.save(company); }

    public Company updateCompany(Company company){ return companyRepository.save(company); }

    public void deleteCompany(Integer id){ companyRepository.deleteById(id); }
}
