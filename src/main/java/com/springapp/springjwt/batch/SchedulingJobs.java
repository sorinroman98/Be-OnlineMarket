package com.springapp.springjwt.batch;

import com.springapp.springjwt.utility.VerificationTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class SchedulingJobs {


    private final transient VerificationTokenProvider verificationToken;

    @Autowired
    public SchedulingJobs(VerificationTokenProvider verificationToken) {
        this.verificationToken = verificationToken;
    }

    @Scheduled(cron="0 0 12 * * ?")
    public void deleteUnconfirmedUserAccount(){
        verificationToken.deleteUnconfirmedAccountWithExpiredToken();

    }
}
