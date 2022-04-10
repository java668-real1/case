package com.janey668.flink.transform;

import com.janey668.flink.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransformTest3_Reduce {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment evn = StreamExecutionEnvironment.getExecutionEnvironment();
        evn.setParallelism(1);
        DataStream<String> dataStream = evn.readTextFile("D:\\project\\case\\janey668-flink-demo\\src\\main\\resources\\sensor.txt" );
        DataStream<SensorReading> mapStream = dataStream.map(item -> {
            String[] arr = item.split("," );
            return new SensorReading(arr[0], Long.valueOf(arr[1]), Double.valueOf(arr[2]));
        });

        DataStream<SensorReading> reduceStream = mapStream.keyBy(SensorReading::getId).reduce((oldItem, newItem) -> new SensorReading(newItem.getId(), newItem.getTimestamp(), Math.max(oldItem.getTemperature(), newItem.getTemperature())));
        reduceStream.print();
        evn.execute();
    }

}
