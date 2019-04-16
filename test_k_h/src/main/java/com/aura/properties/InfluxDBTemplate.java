package com.aura.properties;


import com.aura.influxdb.InfluxDBAccessor;
import com.aura.influxdb.InfluxDBConnectionFactory;
import com.aura.influxdb.InfluxDBOperations;
import com.aura.influxdb.converter.PointCollectionConverter;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.util.Assert;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class InfluxDBTemplate<T> extends InfluxDBAccessor implements InfluxDBOperations<T> {

    private final static DecimalFormat df = new DecimalFormat("0");
    private PointCollectionConverter<T> converter;

    public InfluxDBTemplate() {

    }

    public InfluxDBTemplate(final InfluxDBConnectionFactory connectionFactory, final PointCollectionConverter<T> converter) {
        setConnectionFactory(connectionFactory);
        setConverter(converter);
    }


    public void setConverter(final PointCollectionConverter<T> converter) {
        this.converter = converter;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.notNull(converter, "PointCollectionConverter is required");
    }

    @Override
    public void createDatabase() {
        final String database = getDatabase();
        getConnection().createDatabase(database);
    }

/*    @Override
    @SuppressWarnings("unchecked")
    public void write(final T... payload) {
        write(Arrays.asList(payload));
    }*/

    @Override
    public void write(final List<T> payload) {
        final String database = getDatabase();
        final String retentionPolicy = getConnectionFactory().getProperties().getRetentionPolicy();
        final BatchPoints ops = BatchPoints.database(database)
                .retentionPolicy(retentionPolicy)
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();
        payload.forEach(t -> converter.convert(t).forEach(ops::point));
        getConnection().write(ops);
    }

    @Override
    public QueryResult query(final Query query) {
        return getConnection().query(query);
    }

    /**
     * 查询数量、最大值、最小值、平均值、中值等单个int值
     * @param sql
     * @param database(可传可NULL)
     * @return
     */
    @Override
    public int query(String sql, String database) {
        Query query = null;
        int value = 0;
        if (database == null) {
            query = new Query(sql, "qdqg");
        } else {
            query = new Query(sql, database);
        }
        QueryResult queryResult = getConnection().query(query);
        System.out.println(queryResult);
        if (queryResult.getResults() != null) {
            QueryResult.Result result = queryResult.getResults().get(0);
            if (result.getSeries() != null) {
                List<List<Object>> values = result.getSeries().get(0).getValues();
                value = Integer.valueOf(df.format(values.get(0).get(1)));
            }
        }
        return value;
    }

    /**
     * 返回查询的集合链表
     * @param sql the query to execute
     * @return
     */
    @Override
    public List query(final String sql,String database,Object object) {
        Query query = new Query(sql, database);
        QueryResult queryResult = getConnection().query(query);
        List arrList = null;
        List<String> columns = null;
        List<List<Object>> values = null;
        Map map = null;
        if (queryResult.getResults() != null) {
            QueryResult.Result result = queryResult.getResults().get(0);
            if (result.getSeries() != null) {
                for (int k = 0; k < result.getSeries().size(); k++) {
                    columns = result.getSeries().get(k).getColumns();
                    values = result.getSeries().get(k).getValues();
                    arrList = new ArrayList();
                    for (int j = 0; j < values.size(); j++) {
                        map = new HashMap();
                        for (int i = 0; i < columns.size(); i++) {
                            map.put(columns.get(i), values.get(j).get(i));
                        }
                        arrList.add(map);
                    }
                }
            }
        }
        return arrList;
    }

    @Override
    public QueryResult query(final Query query, final TimeUnit timeUnit) {
        return getConnection().query(query, timeUnit);
    }

    @Override
    public void query(Query query, int chunkSize, Consumer<QueryResult> consumer) {

    }

  /*@Override
  public void query(Query query, int chunkSize, Consumer<QueryResult> consumer)
  {
    getConnection().query(query, chunkSize, consumer);
  }*/

    @Override
    public Pong ping() {
        return getConnection().ping();
    }

    @Override
    public String version() {
        return getConnection().version();
    }
}
