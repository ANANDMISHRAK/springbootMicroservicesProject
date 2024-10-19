package com.anand.company.companyms;

import com.anand.company.companyms.response.CompanyResponse;




public interface CompanyService {
    CompanyResponse findAll();

    CompanyResponse findCompanyById(Long id);

    CompanyResponse createCompany(Company comp);

    CompanyResponse updateComanyById(Long id, Company comp);

    CompanyResponse deleteCompanyById(Long id);
}
