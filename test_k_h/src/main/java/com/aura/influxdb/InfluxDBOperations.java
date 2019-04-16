package com.aura.influxdb;

import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public interface InfluxDBOperations<T>
{
  /**
   * Ensures that the configured database exists.
   */
  void createDatabase();

  /**
   * Write a single measurement to the database.
   *
   * @param payload the measurement to write to
   */
  @SuppressWarnings("unchecked")
 // void write(final T... payload);

  /**
   * Write a set of measurements to the database.
   *
   * @param payload the values to write to
   */
  void write(final List<T> payload);

  /**
   * Executes a query against the database.
   *
   * @param sql the sql query to execute
   * @param database enableNull(true)
   * @return a List of time series data matching the sql
   */
  int query(final String sql, String database);

  /**
   * Executes a query against the database.
   *
   * @param sql the sql query to execute
   * @return a List of time series data matching the sql
   */
  List query(final String sql, String database, Object obj);

  /**
   * Executes a query against the database.
   *
   * @param query the query to execute
   * @return a List of time series data matching the query
   */
  QueryResult query(final Query query);

  /**
   * Executes a query against the database.
   *
   * @param query    the query to execute
   * @param timeUnit the time unit to be used for the query
   * @return a List of time series data matching the query
   */
  QueryResult query(final Query query, final TimeUnit timeUnit);

  /**
   * Execute a streaming query against the database.
   *
   * @param query     the query to execute
   * @param chunkSize the number of QueryResults to process in one chunk
   * @param consumer  the consumer to invoke for each received QueryResult
   */
  void query(final Query query, final int chunkSize, final Consumer<QueryResult> consumer);

  /**
   * Ping the database.
   *
   * @return the response of the ping execution
   */
  Pong ping();

  /**
   * Return the version of the connected database.
   *
   * @return the version String, otherwise unknown
   */
  String version();
}
