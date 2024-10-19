package com.anand.company.companyms;

import com.anand.company.companyms.response.CompanyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // custom Response
   // CompanyResponse companyResponse;

//    public CompanyController() {
//    }
    // Start to write Controller

    //GetAll Company
    @GetMapping
    public ResponseEntity<CompanyResponse> getAllCompanies() {
        CompanyResponse companies = companyService.findAll();


        if (companies.getData() != null) {
            companies.setMsg("Successfully retrieved all companies");
            return ResponseEntity.ok(companies);
        } else {
            companies.setMsg("No companies found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(companies);
        }

    }

    // Get Company By Id
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id)
    {
        CompanyResponse company = companyService.findCompanyById(id);



        if (company.getData() != null) {
            company.setMsg("Successfully retrieved company with id " + id);
            return ResponseEntity.ok(company);
        } else {
            company.setMsg("No company exist with id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(company);
        }
    }

    // Create company
    @PostMapping()
    public ResponseEntity<CompanyResponse> createCompany(@RequestBody Company comp)
    {
        CompanyResponse savedCompany= companyService.createCompany( comp);


        if(savedCompany.getData() != null)
        {
            savedCompany.setMsg("sucessfully created company ");
            return ResponseEntity.ok(savedCompany);
        }
        else
        {
            savedCompany.setMsg("Failed to create company: ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(savedCompany);
        }
    }

   // Update company by id
    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateComapyById(@PathVariable Long id, @RequestBody Company comp)
    {
        CompanyResponse company = companyService.updateComanyById(id, comp);


        if(company.getData() != null)
        {
         company.setMsg("Sucessfully Updated company which id "+ id);
         return ResponseEntity.ok(company);
        }
        else
        {
            company.setMsg("Not Updated company");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(company);
        }

    }


    // Delete company by id
    @DeleteMapping("/{id}")
    public ResponseEntity<CompanyResponse> deleteCompanyById(@PathVariable Long id)
    {
        CompanyResponse comp = companyService.deleteCompanyById(id);

       if(comp.getData() != null)
       {
           comp.setMsg("Copmpany deleted Successfully");
           return  ResponseEntity.ok(comp);
       }
       else {
           comp.setMsg("Copmpany  does not exist with id "+ id);
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(comp);
       }
    }

}
