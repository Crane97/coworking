package com.monforte.coworking.services.impl;

import com.monforte.coworking.domain.entities.Company;
import com.monforte.coworking.domain.entities.User;
import com.monforte.coworking.repositories.CompanyRepository;
import com.monforte.coworking.services.ICompanyService;
import com.monforte.coworking.services.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class CompanyService implements ICompanyService {

    @Autowired
    public CompanyRepository companyRepository;

    @Autowired
    public IUserService userService;

    public Page<Company> getCompanys(Pageable pageable) { return (Page<Company>) companyRepository.findAll(pageable); }

    public Company getCompanyById(Integer id) throws NoSuchElementException {

        Optional<Company> comp = companyRepository.findById(id);

        if(comp.isPresent()) {
            return companyRepository.findById(id).get();
        }
        else throw new NoSuchElementException("No company with id: "+id);
    }

    @Transactional
    public Company addCompany(Company company){

        User admin = userService.getUser(company.getIdAdmin());

        company.getUserList().add(admin);
        Company company1 = companyRepository.save(company);

        admin.setCompany(company1);
        userService.updateUser(admin);

        return company1;
    }

    @Transactional
    public Company addCompanyAsAdmin(Company company, User user){

        company.setIdAdmin(user.getId());
        company.setNameAdmin(user.getName() +" "+ user.getSurname());
        Company company1 = companyRepository.save(company);

        User admin = userService.getUser(company1.getIdAdmin());
        admin.setCompany(company1);
        userService.updateUser(admin);

        return company1;
    }

    public Company addUserToCompany(Integer companyId, User user){

        Company company1 = getCompanyById(companyId);
        User user1 = userService.getUser(user.getId());
        company1.getUserList().add(user1);

        user1.setCompany(company1);
        userService.updateUser(user1);

        return company1;
        
    }

    public Company updateCompany(Company company){ return companyRepository.save(company); }

    public void deleteCompany(Integer id){ companyRepository.deleteById(id); }
}
