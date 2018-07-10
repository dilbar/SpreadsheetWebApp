package com.dturna.demo;

import java.io.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainController {

    // The Environment object will be used to read parameters from the
    // application.properties configuration file
    @Autowired
    private Environment env;

    /**
     * Show the frontend page containing the form for uploading a file.
     */
    @RequestMapping("/")
    public String frontend() {
        return "frontend.html";
    }

    /**
     * POST /uploadFile -> receive and locally save a file.
     *
     * @param uploadfile The uploaded file as Multipart file parameter in the
     * HTTP request. The RequestParam name must be the same of the attribute
     * "name" in the input tag with type file.
     *
     * @return An http OK status in case of success, an http 4xx status in case
     * of errors.
     */

    private String finalString = "";


    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(
            @RequestParam("uploadfile") MultipartFile uploadfile) throws IOException {

        try {
            // Get the filename and build the local file path
            String filename = uploadfile.getOriginalFilename();
            String directory = env.getProperty("netgloo.paths.uploadedFiles");
            String filepath = Paths.get(directory, filename).toString();

            // Save the file locally
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            stream.write(uploadfile.getBytes());
            stream.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return "bad";
        }


//        BufferedReader br = new BufferedReader(new FileReader("/Users/dturna/Desktop/spreadsheet.csv/"));
//        String line;
//        line = br.readLine();
//        System.out.println(line);
        String csvFile = "C:/Users/Dilbar S. Turna/Desktop/spreadsheet.csv/"; //this is where the file is save to when it is uploaded and it gets read here
        String line = "";
        String cvsSplitBy = ",";
        int i = 0;
        String[] type = new String[13];
        String[] row1;
        Double[] totalInt = new Double[13];
        Double[] rowInt = new Double[13];
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                if (i == 0) {
                    // use comma as separator
                    type = line.split(cvsSplitBy);


                }
                if (i == 1) {
                    row1 = line.split(cvsSplitBy);
                    for (int x = 0; x <= row1.length - 1; x++) {
                        rowInt[x] = Double.parseDouble(row1[x]);
                    }
                    totalInt = rowInt;
                }
                if (i > 1) {
                    row1 = line.split(cvsSplitBy);
                    for (int x = 0; x <= row1.length - 1; x++) {
                        rowInt[x] = Double.parseDouble(row1[x]);
                    }
                    for (int x = 0; x <= rowInt.length - 1; x++) {
                        totalInt[x] = totalInt[x] + rowInt[x];
                    }
                }
                i++;
            }
            for (int z = 0; z < 13; z++ )
            {
                if(z != 6 && z != 7 && z!=8)
                {
                    finalString += (type[z] + ": " + totalInt[z] + "<br>");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalString;

    } // method uploadFile


} // class MainController




