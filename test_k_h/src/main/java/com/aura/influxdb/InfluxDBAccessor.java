package com.aura.influxdb;


import org.influxdb.InfluxDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class InfluxDBAccessor implements InitializingBean {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private InfluxDBConnectionFactory connectionFactory;

  /**
   * Returns the connection factory.
   *
   * @return Returns the connection factory
   */
  public InfluxDBConnectionFactory getConnectionFactory() {
//    if (connectionFactory != null){
//      connectionFactory = SpringContextAware.getBean("influxDBConnectionFactory");
//    }
    return connectionFactory;
  }

  /**
   * Sets the connection factory.
   *
   * @param connectionFactory The connection factory to set
   */
  public void setConnectionFactory(final InfluxDBConnectionFactory connectionFactory) {
    this.connectionFactory = connectionFactory;
  }

  public String getDatabase()
  {
    return getConnectionFactory().getProperties().getDatabase();
  }

  public String getRetentionPolicy() {
    return getConnectionFactory().getProperties().getRetentionPolicy();
  }

  public InfluxDB getConnection() {

    return getConnectionFactory().getConnection();
  }

  @Override
  public void afterPropertiesSet()
  {
    Assert.notNull(getConnectionFactory(), "InfluxDBConnectionFactory is required");
  }
}
