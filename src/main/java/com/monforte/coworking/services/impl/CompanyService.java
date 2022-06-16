package com.monforte.coworking.services.impl;

import com.monforte.coworking.domain.entities.Company;
import com.monforte.coworking.repositories.CompanyRepository;
import com.monforte.coworking.services.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CompanyService implements ICompanyService {

    @Autowired
    public CompanyRepository companyRepository;

    public Page<Company> getCompanys(Pageable pageable) { return (Page<Company>) companyRepository.findAll(pageable); }

    public Company getCompanyById(Integer id) throws NoSuchElementException {

        Optional<Company> comp = companyRepository.findById(id);

        if(comp.isPresent()) {
            return companyRepository.findById(id).get();
        }
        else throw new NoSuchElementException("No company with id: "+id);
    }

    public Company addCompany(Company company){ return companyRepository.save(company); }

    public Company updateCompany(Company company){ return companyRepository.save(company); }

    public void deleteCompany(Integer id){ companyRepository.deleteById(id); }
}
