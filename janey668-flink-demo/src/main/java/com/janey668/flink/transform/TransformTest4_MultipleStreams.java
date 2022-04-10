package com.janey668.flink.transform;

import com.janey668.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

import java.util.Collections;

public class TransformTest4_MultipleStreams {

    public static void main(String[] args) throws Exception {
        // split connect union

        StreamExecutionEnvironment evn = StreamExecutionEnvironment.getExecutionEnvironment();
        evn.setParallelism(1);
        DataStream<String> dataStream = evn.readTextFile("D:\\project\\case\\janey668-flink-demo\\src\\main\\resources\\sensor.txt" );
        DataStream<SensorReading> mapStream = dataStream.map(item -> {
            String[] arr = item.split("," );
            return new SensorReading(arr[0], Long.valueOf(arr[1]), Double.valueOf(arr[2]));
        });

        SplitStream<SensorReading> split = mapStream.split(item -> {
            return (item.getTemperature() > 30) ? Collections.singletonList("high" ) : Collections.singletonList("low" );
        });

        DataStream<SensorReading> high = split.select("high" );
        DataStream<SensorReading> low = split.select("low" );
        DataStream<SensorReading> select = split.select("high", "low" );

        high.print("high");
        low.print("low");
        select.print("all");
        // 2. 合流 connect，将高温流转换成二元组类型，与低温流连接合并之后，输出状态信息
        DataStream<Tuple2<String, Double>> warningStream = high.map(new MapFunction<SensorReading, Tuple2<String, Double>>() {
            @Override
            public Tuple2<String, Double> map(SensorReading value) throws Exception {
                return new Tuple2<>(value.getId(), value.getTemperature());
            }
        });

        ConnectedStreams<Tuple2<String, Double>, SensorReading> connectedStreams = warningStream.connect(low);

        DataStream<Object> resultStream = connectedStreams.map(new CoMapFunction<Tuple2<String, Double>, SensorReading, Object>() {
            @Override
            public Object map1(Tuple2<String, Double> value) throws Exception {
                return new Tuple3<>(value.f0, value.f1, "high temp warning");
            }

            @Override
            public Object map2(SensorReading value) throws Exception {
                return new Tuple2<>(value.getId(), "normal");
            }
        });

        resultStream.print();

        // 3. union联合多条流
//        warningStream.union(lowTempStream);
//        highTempStream.union(lowTempStream, allTempStream);
        evn.execute();
    }
}
