package com.janey668.flink.transform;

import com.janey668.flink.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransformTest2_RollingAggregation {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment evn = StreamExecutionEnvironment.getExecutionEnvironment();
        evn.setParallelism(1);
        DataStream<String> dataStream = evn.readTextFile("D:\\project\\case\\janey668-flink-demo\\src\\main\\resources\\sensor.txt" );
        DataStream<SensorReading> mapStream = dataStream.map(item -> {
            String[] arr = item.split("," );
            return new SensorReading(arr[0], Long.valueOf(arr[1]), Double.valueOf(arr[2]));
        });

        DataStream<SensorReading> temperature = mapStream.keyBy(SensorReading::getId).max("temperature" );
        DataStream<SensorReading> maxBy = mapStream.keyBy(SensorReading::getId).maxBy("temperature" );
        temperature.print();
        maxBy.print("maxBy");
        evn.execute();
    }
}

