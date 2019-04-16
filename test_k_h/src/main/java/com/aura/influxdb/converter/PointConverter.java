package com.aura.influxdb.converter;

import org.influxdb.dto.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PointConverter implements PointCollectionConverter<Point>
{
  @Override
  public List<Point> convert(final Point source)
  {
    final ArrayList<Point> list = new ArrayList<>(1);
    Collections.addAll(list, source);
    return list;
  }
}
