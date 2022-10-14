package org.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;

public class MyRouteBuilder extends RouteBuilder {

    private static final String BASE_PATH = System.getProperty("user.dir") + "/src/data";

    @Override
    public void configure() {
    from("file:" + BASE_PATH+"?noop=true&idempotent=true")
            .split(body().tokenize("\n"))
            .choice().when(simple("${exchangeProperty.CamelSplitIndex} > 0"))
            .unmarshal().bindy(BindyType.Csv, SongRecord.class)
            .to("seda:aggregate");
    from("seda:aggregate?concurrentConsumers=10")
            .bean(MyAggregationStrategy.class,"setArtistHeader")
            .aggregate(new MyAggregationStrategy()).header("artist")
            .completionPredicate(header("CamelSplitComplete").isEqualTo(true))
            .process(new MyProcessor());
    }
}
