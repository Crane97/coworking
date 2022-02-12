package com.monforte.coworking.controller;

import com.monforte.coworking.domain.entities.Company;
import com.monforte.coworking.exceptions.ApiErrorException;
import com.monforte.coworking.services.impl.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/company")
public class CompanyController {

    @Autowired
    public CompanyService companyService;

    @GetMapping
    public List<Company> getAllCompanys(){
        return companyService.getCompanys();
    }

    @GetMapping(path = "/{id}")
    public Company getCompanyById(@PathVariable Integer id){
        return companyService.getCompanyById(id);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Company> addCompany(@RequestBody Company company){
        Company company1 = companyService.addCompany(company);
        return new ResponseEntity<>(company1, HttpStatus.CREATED);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<?> updateCompany(@RequestBody Company request, @PathVariable Integer id){
        try {
            Company company = companyService.getCompanyById(id);

            if(company == null){
                throw new ApiErrorException("Company with id: " + id + " not found.");
            }

            if(request.getName() != null) company.setName(request.getName());
            if(request.getWorkers() != null) company.setWorkers(request.getWorkers());
            if(request.getField() != null) company.setField(request.getField());
            if(request.getLogo() != null) company.setLogo(request.getLogo());
            if(request.getHiring() != null) company.setHiring(request.getHiring());
            if(request.getIdAdmin() != null) company.setIdAdmin(request.getIdAdmin());

            Company company1 = companyService.updateCompany(company);
            return new ResponseEntity<>(company1, HttpStatus.CREATED);
        }
        catch(ApiErrorException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Company> deleteCompany(@PathVariable Integer id){
        companyService.deleteCompany(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
