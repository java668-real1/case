package com.janey668.flink.source;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FileSource {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment evn = StreamExecutionEnvironment.getExecutionEnvironment();
        evn.setParallelism(1);
        DataStream<String> dataStream = evn.readTextFile("D:\\project\\case\\janey668-flink-demo\\src\\main\\resources\\sensor.txt" );
        dataStream.print();
        evn.execute();
    }

}
