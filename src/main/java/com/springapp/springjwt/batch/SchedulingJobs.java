package com.springapp.springjwt.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class SchedulingJobs {
    //private static final Logger logger = LoggerFactory.getLogger(InternshipProjectApplication.class);

    //private final transient VerificationTokenService verificationToken;

    @Scheduled(cron="0 0 12 * * ?")
    public void deleteUnconfirmedUserAccount(){
     //   verificationToken.deleteUnconfirmedAccountWithExpiredToken();
      //  logger.info("just a test info");

    }
}
