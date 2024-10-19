package com.anand.jobms.job;


import com.anand.jobms.job.response.JobResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/job")
public class JobController {
    @Autowired
    JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }


    // Start to write Controller

    //GetAll Job
    @GetMapping
    public ResponseEntity<JobResponse> getAllJobs() {
        JobResponse jobs = jobService.findAll();


        if (jobs.getData() != null) {
            jobs.setMsg("Successfully retrieved all Jobs");
            return ResponseEntity.ok(jobs);
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jobs);
        }

    }

    // Get Company By Id
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id)
    {
        JobResponse job = jobService.findJobByid(id);



        if (job.getData() != null) {
            job.setMsg("Successfully retrieved job with id " + id);
            return ResponseEntity.ok(job);
        } else {
            job.setMsg("No job exist with id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(job);
        }
    }

    // Create company
    @PostMapping()
    public ResponseEntity<JobResponse> createJob(@RequestBody Job job)
    {
        JobResponse savedJob= jobService.createJob( job);


        if(savedJob.getData() != null)
        {
            savedJob.setMsg("sucessfully created Job ");
            return ResponseEntity.ok(savedJob);
        }
        else
        {
           // savedJob.setMsg("Failed to create Job : ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(savedJob);
        }
    }

   // Update company by id
    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJobById(@PathVariable Long id, @RequestBody Job job)
    {
        JobResponse resJob = jobService.updateJobById(id, job);


        if(resJob.getData() != null)
        {
         resJob.setMsg("Sucessfully Updated job which id "+ id);
         return ResponseEntity.ok(resJob);
        }
        else
        {
            resJob.setMsg("Not Updated Job");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resJob);
        }

    }


    // Delete company by id
    @DeleteMapping("/{id}")
    public ResponseEntity<JobResponse> deleteJobById(@PathVariable Long id)
    {
        JobResponse job = jobService.deleteJobById(id);

       if(job.getData() != null)
       {
           job.setMsg("Job deleted Successfully");
           return  ResponseEntity.ok(job);
       }
       else {
           job.setMsg("Job  does not exist with id "+ id);
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(job);
       }
    }

}
