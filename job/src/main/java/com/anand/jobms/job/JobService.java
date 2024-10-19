package com.anand.jobms.job;


import com.anand.jobms.job.response.JobResponse;


public interface JobService {
    JobResponse findAll();

    JobResponse findJobByid(Long id);

    JobResponse createJob(Job job);

    JobResponse updateJobById(Long id, Job job);

    JobResponse deleteJobById(Long id);
}
