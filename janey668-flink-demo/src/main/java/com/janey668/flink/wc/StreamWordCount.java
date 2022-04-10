package com.janey668.flink.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class StreamWordCount {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
//        environment.setParallelism(1);
//        DataStream<String> dataStream = environment.readTextFile("D:\\project\\case\\janey668-flink-demo\\src\\main\\resources\\hello.txt");
        ParameterTool params = ParameterTool.fromArgs(args);
        String host = params.get("host");
        Integer port = params.getInt("port");
        DataStream<String> dataStream = environment.socketTextStream(host, port);
        DataStream<Tuple2<String, Integer>> resultStream = dataStream.flatMap((FlatMapFunction<String, Tuple2<String, Integer>>) (item, out) -> {
            String[] words = item.split(" ");
            for (String word : words){
                out.collect(new Tuple2(word, 1));
            };
        }).returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(0)
                .sum(1).setParallelism(2);
        resultStream.print().setParallelism(1);
        environment.execute();

    }

}
