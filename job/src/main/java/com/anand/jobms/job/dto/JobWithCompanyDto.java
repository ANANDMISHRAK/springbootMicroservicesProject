package com.anand.jobms.job.dto;

import com.anand.jobms.job.ApiData.Company;
import com.anand.jobms.job.Job;

public class JobWithCompanyDto {

    private Job job;
    private Company company;

    public JobWithCompanyDto() {
    }

    public JobWithCompanyDto(Job job, Company company) {
        this.job = job;
        this.company = company;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
