/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package woy.woy.woy;

import com.mifmif.common.regex.Generex;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class MyBenchmark {

    @State(Scope.Thread)
    public static class MyState {
        private ClassSearcher classSearcher = new ClassSearcher();
        private Generex generex = new Generex("[a-z]{1,3}");
        private String[] classNames;
        private long[] modDates;

        {
            try {
                BufferedReader namesReader = new BufferedReader(new FileReader(Main.class.getResource("/classNames.txt").getFile()));
                BufferedReader datesReader = new BufferedReader(new FileReader(Main.class.getResource("/modDates.txt").getFile()));

                List<String> list = namesReader.lines().collect(Collectors.toList());
                List<Long> list1 = datesReader.lines().map(Long::parseLong).collect(Collectors.toList());

                classNames = list.toArray(new String[list.size()]);
                final int[] i = {0};
                modDates = new long[list1.size()];
                list1.forEach(l -> modDates[i[0]++] = l);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = {"-Xmx64m", "-Xms64m", "-Xss64m"})
    @Warmup(iterations = 5)
    @BenchmarkMode(Mode.All)
    @Measurement
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void regexSolution(MyState state) {
        state.classSearcher.refresh(state.classNames, state.modDates);
//        state.classSearcher.guess(state.generex.random());
        state.classSearcher.guess("Cla");
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MyBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

}


