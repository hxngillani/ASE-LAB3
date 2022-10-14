package org.example;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.example.MainApp.latch;

public class MyProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(MyProcessor.class);

    public void process(Exchange exchange) throws Exception {
        Map<String, Integer> top20 = ((MyAggregationStrategy) exchange.getIn()
                .getHeader("myAggregation"))
                .getTop20Artists();

        top20.forEach((k, v) -> LOG.info("{}, {}", k, v));
        latch.countDown();
    }
}
