package com.leela.amazon;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class AmazonClientHelper {
    private static AmazonS3 amazonClient;
    private static final String ACCESS_KEY = "";
    private static final String SECRETE_KEY = "";

    public AmazonClientHelper() {

    }

    public static AmazonS3 getAmazonClient() {
        if (amazonClient == null) {
            final BasicAWSCredentials creds = new BasicAWSCredentials(
                    ACCESS_KEY, SECRETE_KEY);
            amazonClient = new AmazonS3Client(creds);
        }
        return amazonClient;
    }
}
