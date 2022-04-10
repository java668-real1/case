package com.janey668.flink.transform;

import com.janey668.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransformTest1_Base {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment evn = StreamExecutionEnvironment.getExecutionEnvironment();
        evn.setParallelism(1);
        DataStream<String> dataStream = evn.readTextFile("D:\\project\\case\\janey668-flink-demo\\src\\main\\resources\\sensor.txt" );
        DataStream<SensorReading> transStream  = dataStream.map(item -> {
            String[] arr = item.split("," );
            return new SensorReading(arr[0], Long.valueOf(arr[1]), Double.valueOf(arr[2]));
        });
        DataStream<Tuple2<String, Long>> tupleStream = dataStream.flatMap((FlatMapFunction<String, Tuple2<String, Long>>) (item, out) -> {
            String[] arr = item.split("," );
            out.collect(new Tuple2<>(arr[0], Long.valueOf(arr[1])));
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));

        DataStream<String> filterStream = dataStream.filter(item -> {
            String[] arr = item.split("," );
            return Double.valueOf(arr[2]).compareTo(new Double(35)) > 0;
        });

        transStream.print();
        tupleStream.print();
        filterStream.print("filterStream");
        evn.execute();
    }

}
