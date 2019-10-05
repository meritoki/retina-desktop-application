/*
 * Copyright 2019 osvaldo.rodriguez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meritoki.retina.application.desktop.model.system;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.meritoki.retina.application.desktop.model.provider.zooniverse.Zooniverse;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Shell {
    static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Shell.class.getName());
    @JsonIgnore
    public static List<String> executeCommand(String command) {
        return Shell.executeCommand(command, 120);
    }
    
    @JsonIgnore
    public static List<String> executeCommand(String command, int timeout) {
        logger.info("executeCommand(" + command + ")");
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c",command)
                .redirectError(new File("error"))
                .redirectOutput(new File("output"));

        Process process;
        int seconds = 120;
        String output = null;
        String error = null;
        List<String> stringList = new ArrayList<>();
        String string;
        try {
            process = processBuilder.start();
            if (!process.waitFor(seconds, TimeUnit.SECONDS)) {
                process.destroy();
                logger.info("executeCommand(...) exitValue="+process.exitValue());
            }
            output = (FileUtils.readFileToString(new File("output"),"UTF8"));
            error = (FileUtils.readFileToString(new File("error"),"UTF8"));
            if(error != null && !error.equals("")){
                string = "error";
                stringList.add(string);
            } else if(output != null && !output.equals("")){
                string = output;
                String[] stringArray = string.split("\n");
                for(String s: stringArray){
                    stringList.add(s);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Zooniverse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Zooniverse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stringList;
    }
}
