package com.monforte.coworking.services;

import com.monforte.coworking.domain.entities.Company;
import com.monforte.coworking.domain.entities.User;
import com.monforte.coworking.exceptions.UserIsAdminException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICompanyService {

    Page<Company> getCompanys(Pageable pageable);

    Page<User> getUsersFromCompany(Pageable pageable, Integer companyId);

    Company getCompanyById(Integer id);

    Company addCompany(Company company);

    Company addCompanyAsAdmin(Company company, User user);

    Company addUserToCompany(Integer companyId, User user);

    Company deleteUserFromCompany(Integer companyId, User user) throws UserIsAdminException;

    Company updateCompany(Company company);

    Company selectNewAdmin(Integer companyId, Integer userId);

    void deleteCompany(Integer id);
}
