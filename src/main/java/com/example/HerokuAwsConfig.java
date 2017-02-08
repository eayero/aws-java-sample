package com.example;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.heroku.api.HerokuAPI;

import java.util.Map;

/**
 * @author Joe Kutner on 2/8/17.
 *         Twitter: @codefinger
 */
public class HerokuAwsConfig implements AWSCredentialsProvider {

  private AWSCredentials credentials;

  private String appName;

  private String herokuApiKey;

  private String bucket;

  private String region;

  public HerokuAwsConfig(String appName, String herokuApiKey) {
    this.appName = appName;
    this.herokuApiKey = herokuApiKey;
    refresh();
  }

  public String getBucket() {
    return bucket;
  }

  public String getRegion() {
    return region;
  }

  public AWSCredentials getCredentials() {
    return this.credentials;
  }

  public void refresh() {
    HerokuAPI heroku = new HerokuAPI(herokuApiKey);
    Map<String,String> config = heroku.listConfig(appName);

    String awsAccessKeyId = config.get("AWS_ACCESS_KEY_ID");
    String awsSecretKey = config.get("AWS_SECRET_KEY");

    this.credentials = new HerokuConfigAWSCredentials(awsAccessKeyId, awsSecretKey);
    this.bucket = config.get("AWS_BUCKET");
    this.region = config.get("AWS_REGION");
  }

  public static class HerokuConfigAWSCredentials implements AWSCredentials {

    private String awsAccessKeyId;

    private String awsSecretKey;

    public HerokuConfigAWSCredentials(String awsAccessKeyId, String awsSecretKey) {
      this.awsAccessKeyId = awsAccessKeyId;
      this.awsSecretKey = awsSecretKey;
    }

    public String getAWSAccessKeyId() {
      return this.awsAccessKeyId;
    }

    public String getAWSSecretKey() {
      return this.awsSecretKey;
    }
  }
}
