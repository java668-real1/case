package com.janey668.flink.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;

public class WordCount {

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();
        DataSet<String> dataSet = environment.readTextFile("D:\\project\\case\\janey668-flink-demo\\src\\main\\resources\\hello.txt");
//######flatMap算子的lambda表达式###########
//返回值是Tuple,注意，泛型的指定，FlatMapFunction也要指定的
        DataSet<Tuple2<String, Integer>> wordsandOnes = dataSet.flatMap((FlatMapFunction<String, Tuple2<String, Integer>>) (line, out)
                -> {
            for (String word : line.split(" ")) {
                //  if (word.trim() != null)   //过滤无效值
                Tuple2 t = new Tuple2(word, 1);
                out.collect(t);
            }
        }).returns(Types.TUPLE(Types.STRING, Types.INT))
                .groupBy(0)    // 按照第一个位置的word分组
                .sum(1);
        wordsandOnes.print();
    }
}
