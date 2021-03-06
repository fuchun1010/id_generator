package com.sankuai.inf.leaf.server;

import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.common.PropertyFactory;
import com.sankuai.inf.leaf.common.Result;
import com.sankuai.inf.leaf.common.ZeroIDGen;
import com.sankuai.inf.leaf.server.exception.InitException;
import com.sankuai.inf.leaf.snowflake.SnowflakeIDGenImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Properties;

@Service("SnowflakeService")
public class SnowflakeService {
  private Logger logger = LoggerFactory.getLogger(SnowflakeService.class);
  IDGen idGen;

  public SnowflakeService() throws InitException {
    Properties properties = PropertyFactory.getProperties();
    boolean flag = Boolean.parseBoolean(properties.getProperty(Constants.LEAF_SNOWFLAKE_ENABLE, "true"));
    if (flag) {
      String zkAddress = Optional.ofNullable(properties.getProperty(Constants.LEAF_SNOWFLAKE_ZK_ADDRESS))
          .orElse("127.0.0.1");
      int port = Integer.parseInt(Optional.ofNullable(properties.getProperty(Constants.LEAF_SNOWFLAKE_PORT)).orElse("2181"));
      idGen = new SnowflakeIDGenImpl(zkAddress, port);
      if (idGen.init()) {
        logger.info("Snowflake Service Init Successfully");
      } else {
        throw new InitException("Snowflake Service Init Fail");
      }
    } else {
      idGen = new ZeroIDGen();
      logger.info("Zero ID Gen Service Init Successfully");
    }
  }

  public Result getId(String key) {
    return idGen.get(key);
  }


}
