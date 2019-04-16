package com.aura.influxdb;


import com.aura.properties.InfluxDBProperties;
import okhttp3.OkHttpClient.Builder;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

public class InfluxDBConnectionFactory implements InitializingBean
{

  private static Logger logger = LoggerFactory.getLogger(InfluxDBConnectionFactory.class);

  private InfluxDB connection;

  private InfluxDBProperties properties;

  public InfluxDBConnectionFactory() {

  }

  public InfluxDBConnectionFactory(final InfluxDBProperties properties)
  {
    this.properties = properties;
  }

  public InfluxDB getConnection() {
    Assert.notNull(this.getProperties(), "InfluxDBProperties are required");
    if (connection == null)
    {
      final Builder client = new Builder()
        .connectTimeout(properties.getConnectTimeout(), TimeUnit.SECONDS)
        .writeTimeout(properties.getWriteTimeout(), TimeUnit.SECONDS)
        .readTimeout(properties.getReadTimeout(), TimeUnit.SECONDS);
      connection = InfluxDBFactory
        .connect(properties.getUrl(), properties.getUsername(), properties.getPassword(), client);
      logger.debug("Using InfluxDB '{}' on '{}'", properties.getDatabase(), properties.getUrl());
      if (properties.isGzip())
      {
        logger.debug("Enabled gzip compression for HTTP requests");
        connection.enableGzip();
      }
    }
    return connection;
  }

  /**
   * Returns the configuration properties.
   *
   * @return Returns the configuration properties
   */
  public InfluxDBProperties getProperties()
  {
    return this.properties;
  }

  /**
   * Sets the configuration properties.
   *
   * @param properties The configuration properties to set
   */
  public void setProperties(final InfluxDBProperties properties)
  {
    this.properties = properties;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(getProperties(), "InfluxDBProperties are required");
  }
}
