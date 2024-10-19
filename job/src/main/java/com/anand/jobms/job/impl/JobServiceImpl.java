package com.anand.jobms.job.impl;


import com.anand.jobms.job.ApiData.Company;
import com.anand.jobms.job.Job;
import com.anand.jobms.job.JobRepository;
import com.anand.jobms.job.JobService;
import com.anand.jobms.job.dto.JobWithCompanyDto;
import com.anand.jobms.job.response.JobResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    JobRepository jobRepository;
    @Autowired
    RestTemplate restTemplate;

    public JobServiceImpl() {
    }

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }


//    CompanyResponse companyResponse;
//
//    public CompanyServiceImpl(CompanyResponse companyResponse) {
//        this.companyResponse = companyResponse;
//    }

    Long nextId= 1L;

    // Start Implement Services

    @Override
    @CircuitBreaker(name = "companyBreaker" , fallbackMethod="companyBreakerFallback")
    public JobResponse findAll() {
        JobResponse jobResponse = new JobResponse();
        List<Job> resJobs=  jobRepository.findAll();
       // RestTemplate restTemp = new RestTemplate();
        List<JobWithCompanyDto> listJobWithCompany = new ArrayList<>();
        // find company crossponsding Job
        for( Job j : resJobs)
        {
            try{
                ResponseEntity<JobResponse> restApiResponse = restTemplate.getForEntity("http://companyms:8081/company/"+j.getCompanyId() , JobResponse.class) ;
                if(restApiResponse.getStatusCode() == HttpStatus.OK)
                {
                    JobResponse restData = restApiResponse.getBody();
                    Object compdata = restData.getData();
                    ObjectMapper objectMapper = new ObjectMapper();
                    if(compdata != null)
                    {
                        Company  comp = objectMapper.convertValue(compdata, Company.class);
                        JobWithCompanyDto jwcd = new JobWithCompanyDto();
                        jwcd.setJob(j);
                        jwcd.setCompany(comp);
                        listJobWithCompany.add(jwcd);

                    }
                }
                else {
                    jobResponse.setData(null);
                    jobResponse.setMsg(" Not able to Featch Company with id" + j.getCompanyId() + restApiResponse.getStatusCode());
                    return jobResponse;
                }
            }
            catch (Exception e) {
                jobResponse.setData(null);
                jobResponse.setMsg("Error: " + e.getMessage());
                return jobResponse;
            }

        }

        jobResponse.setData(listJobWithCompany);

        return jobResponse;


    }
    public JobResponse companyBreakerFallback(Throwable e)
    {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setData("Company server is down so Retry after sometime");
        jobResponse.setMsg("Retry after 1 mint " + e.getMessage());
        return jobResponse ;
    }

    @Override
    public JobResponse findJobByid(Long id) {

       Optional<Job> job = jobRepository.findById(id);
        JobResponse jobResponse = new JobResponse();
        JobWithCompanyDto jWithComdto = new JobWithCompanyDto();
        RestTemplate restTemp = new RestTemplate();
        Long comId = 1L;
       if(job.isPresent()) {
           Job dbJob = job.get();
           comId= dbJob.getCompanyId();
           jWithComdto.setJob(dbJob);

         }
       else {
           jWithComdto.setJob(null);
       }
      // if job find then Croseponding Company find
        if(jWithComdto.getJob() != null)
        {
            try{
              ResponseEntity<JobResponse> restApiResponse = restTemp.getForEntity("http://companyms:8081/company/"+comId , JobResponse.class) ;
              if(restApiResponse.getStatusCode() == HttpStatus.OK)
              {
                  JobResponse restData = restApiResponse.getBody();
                  Object compdata = restData.getData();
                  ObjectMapper objectMapper = new ObjectMapper();
                  if(compdata != null)
                  {
                    Company  comp = objectMapper.convertValue(compdata, Company.class);
                    jWithComdto.setCompany(comp);
                    jobResponse.setData(jWithComdto);
                  }
              }
              else {
                  jobResponse.setData(null);
                  jobResponse.setMsg(" Not able to Featch Company with id" + comId + restApiResponse.getStatusCode());
                  return jobResponse;
              }
            }
            catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    // Handle 404 Not Found
                    String responseBody = e.getResponseBodyAsString();
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        JobResponse errorResponse = objectMapper.readValue(responseBody, JobResponse.class);
                        // System.out.println("Error Message: " + errorResponse.getMsg());
                        return errorResponse;
                    } catch (Exception ex) {
                        System.out.println("Error parsing error response: " + ex.getMessage());
                    }
                } else {
                    jobResponse.setData(null);
                    jobResponse.setMsg("HTTP Error: " + e.getStatusCode()+ "Error Message: " + e.getMessage());
                    return jobResponse;

                }
            } catch (Exception e) {
                jobResponse.setData(null);
                jobResponse.setMsg("Error: " + e.getMessage());
                return jobResponse;
            }
        }
        return jobResponse;
    }

    @Override
    public JobResponse createJob(Job job) {
        JobResponse jobResponse = new JobResponse();
        RestTemplate restTemp = new RestTemplate();

        try {

            ResponseEntity<JobResponse> restApiresponse = restTemp.getForEntity("http://companyms:8081/company/" + job.getCompanyId(), JobResponse.class);

            if (restApiresponse.getStatusCode() == HttpStatus.OK) {
                // Get data in response job
                JobResponse restData = restApiresponse.getBody();

                // Access field from Api Response
                Object compData = restData.getData();
                // convert into Company Type
                ObjectMapper objectMapper = new ObjectMapper();
                Company comp;
                if (compData != null) {
                    // convert LinkedHashMap to Company
                    comp = objectMapper.convertValue(compData, Company.class);

                    try {
                        job.setId(nextId++);
                        jobRepository.save(job);
                        JobWithCompanyDto jWithComdto = new JobWithCompanyDto();
                        jWithComdto.setJob(job);
                        jWithComdto.setCompany(comp);
                        jobResponse.setData(jWithComdto);
                        return jobResponse;

                    } catch (Exception e) {
                        jobResponse.setData(null);
                        jobResponse.setMsg("Failed to create Job :" + e.getLocalizedMessage());
                        return jobResponse;
                    }

                } else {
                    jobResponse.setData(null);
                    jobResponse.setMsg("Company does not Exit with id" + job.getCompanyId());
                    return jobResponse;
                }
            } else {
                jobResponse.setData(null);
                jobResponse.setMsg(" Not able to Featch Company with id" + job.getCompanyId() + restApiresponse.getStatusCode());
                return jobResponse;
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // Handle 404 Not Found
                String responseBody = e.getResponseBodyAsString();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JobResponse errorResponse = objectMapper.readValue(responseBody, JobResponse.class);
                    // System.out.println("Error Message: " + errorResponse.getMsg());
                    return errorResponse;
                } catch (Exception ex) {
                    System.out.println("Error parsing error response: " + ex.getMessage());
                }
            } else {
                jobResponse.setData(null);
                jobResponse.setMsg("HTTP Error: " + e.getStatusCode()+ "Error Message: " + e.getMessage());
                return jobResponse;

            }
        } catch (Exception e) {
            jobResponse.setData(null);
            jobResponse.setMsg("Error: " + e.getMessage());
            return jobResponse;
        }
    return jobResponse;
    }

    @Override
    public JobResponse updateJobById(Long id, Job job) {
        Optional<Job> opJob = jobRepository.findById(id);
        JobResponse jobResponse = new JobResponse();
        if(opJob.isPresent())
        {
            Job dbJob = opJob.get();

            dbJob.setTitle(job.getTitle());
            dbJob.setDescription(job.getDescription());
            dbJob.setLocation(job.getLocation());
            dbJob.setMaxSalary(job.getMaxSalary());
            dbJob.setMinSalary(job.getMinSalary());

            jobRepository.save(dbJob);
             jobResponse.setData(dbJob);
        }
        else
        {
            jobResponse.setData(null);
        }
        return jobResponse;
    }

    @Override
    public JobResponse deleteJobById(Long id) {
        Optional<Job> opJob = jobRepository.findById(id);
        JobResponse jobResponse = new JobResponse();
        if(opJob.isPresent())
        {
            Job dbJob = opJob.get();
            jobRepository.deleteById(id);
            jobResponse.setData(dbJob);

        }
        else {
            jobResponse.setData(null);

        }
        return  jobResponse;
    }
}
