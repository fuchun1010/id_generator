package com.sankuai.inf.leaf.server.controller;

import com.sankuai.inf.leaf.common.Result;
import com.sankuai.inf.leaf.common.Status;
import com.sankuai.inf.leaf.server.SegmentService;
import com.sankuai.inf.leaf.server.SnowflakeService;
import com.sankuai.inf.leaf.server.exception.LeafServerException;
import com.sankuai.inf.leaf.server.exception.NoKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LeafController {


  @RequestMapping(value = "/api/segment/get/{key}")
  public String getSegmentID(@PathVariable("key") String key) {
    return get(key, segmentService.getId(key));
  }

  @RequestMapping(value = "/api/snowflake/get/{key}")
  public ResponseEntity<Map<String, Long>> getSnowflakeID(@PathVariable("key") String key) {
    long id = Long.parseLong(get(key, snowflakeService.getId(key)));
    Map<String, Long> body = new HashMap<>(capacity);
    body.putIfAbsent("id", id);
    return ResponseEntity.ok(body);
  }
  
  private String get(@PathVariable("key") String key, Result id) {
    Result result;
    if (key == null || key.isEmpty()) {
      throw new NoKeyException();
    }

    result = id;
    if (result.getStatus().equals(Status.EXCEPTION)) {
      throw new LeafServerException(result.toString());
    }
    return String.valueOf(result.getId());
  }

  private Logger logger = LoggerFactory.getLogger(LeafController.class);

  private final int capacity = 16;

  @Autowired
  SegmentService segmentService;
  @Autowired
  SnowflakeService snowflakeService;
}
