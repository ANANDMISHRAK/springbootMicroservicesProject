package com.anand.company.companyms.impl;

import com.anand.company.companyms.Company;
import com.anand.company.companyms.CompanyRepository;
import com.anand.company.companyms.CompanyService;
import com.anand.company.companyms.response.CompanyResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    public CompanyServiceImpl() {
    }

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }


//    CompanyResponse companyResponse;
//
//    public CompanyServiceImpl(CompanyResponse companyResponse) {
//        this.companyResponse = companyResponse;
//    }

    Long nextId= 1L;

    // Start Implement Services

    @Override
    public CompanyResponse findAll() {
        List<Company> resComp=  companyRepository.findAll();
        CompanyResponse companyResponse = new CompanyResponse();
         companyResponse.setData(resComp);

        return companyResponse;
    }

    @Override
    public CompanyResponse findCompanyById(Long id) {

       Optional<Company> comp = companyRepository.findById(id);
        CompanyResponse companyResponse = new CompanyResponse();
       if(comp.isPresent()) {
           Company copmany = comp.get();
           companyResponse.setData(copmany);
         }
       else {
           companyResponse.setData(null);
       }

        return companyResponse;
    }

    @Override
    public CompanyResponse createCompany(Company comp) {
        CompanyResponse companyResponse = new CompanyResponse();
        try{
            comp.setId(nextId ++);
            companyRepository.save(comp) ;
            companyResponse.setData(comp);
            return companyResponse;

        }
        catch (Exception e)
        {
            companyResponse.setData(null);
            return companyResponse;
        }

    }

    @Override
    public CompanyResponse updateComanyById(Long id, Company comp) {
        Optional<Company> opComany = companyRepository.findById(id);
        CompanyResponse companyResponse = new CompanyResponse();
        if(opComany.isPresent())
        {
            Company dbCom = opComany.get();

            dbCom.setName(comp.getName());
            dbCom.setDescription(comp.getDescription());
            companyRepository.save(dbCom);
             companyResponse.setData(dbCom);
        }
        else
        {
            companyResponse.setData(null);
        }
        return companyResponse;
    }

    @Override
    public CompanyResponse deleteCompanyById(Long id) {
        Optional<Company> opComp = companyRepository.findById(id);
        CompanyResponse companyResponse = new CompanyResponse();
        if(opComp.isPresent())
        {
            Company dbComp = opComp.get();
            companyRepository.deleteById(id);
            companyResponse.setData(dbComp);

        }
        else {
            companyResponse.setData(null);

        }
        return  companyResponse;
    }
}
