package edu.bsu.polaris.solver.common.persistence;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractTxtSolutionImporter<Solution_> {

    private static final String DEFAULT_INPUT_FILE_SUFFIX = "txt";

    protected final static Logger logger = LoggerFactory.getLogger(AbstractTxtSolutionImporter.class);

    public String getInputFileSuffix() {
        return DEFAULT_INPUT_FILE_SUFFIX;
    }

    public abstract TxtInputBuilder<Solution_> createTxtInputBuilder();

    public Solution_ readSolution(File inputFile) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"))) {
            TxtInputBuilder<Solution_> txtInputBuilder = createTxtInputBuilder();
            txtInputBuilder.setInputFile(inputFile);
            txtInputBuilder.setBufferedReader(reader);
            try {
                Solution_ solution = txtInputBuilder.readSolution();
                logger.info("Imported: {}", inputFile);
                return solution;
            } catch (IllegalArgumentException | IllegalStateException e) {
                throw new IllegalArgumentException("Exception in inputFile (" + inputFile + ")", e);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read the file (" + inputFile.getName() + ").", e);
        }
    }



    public static abstract class TxtInputBuilder<Solution_> {

        protected File inputFile;
        protected BufferedReader bufferedReader;

        public void setInputFile(File inputFile) {
            this.inputFile = inputFile;
        }

        public void setBufferedReader(BufferedReader bufferedReader) {
            this.bufferedReader = bufferedReader;
        }

        public abstract Solution_ readSolution() throws IOException;

        // ************************************************************************
        // Helper methods
        // ************************************************************************

        public String getInputId() {
            return FilenameUtils.getBaseName(inputFile.getPath());
        }



        public void readConstantLine(String constantRegex) throws IOException {
            readConstantLine(bufferedReader, constantRegex);
        }

        public void readConstantLine(BufferedReader subBufferedReader, String constantRegex) throws IOException {
            String line = subBufferedReader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("File ends before a line is expected to be a constant regex ("
                        + constantRegex + ").");
            }
            String value = line.trim();
            if (!value.matches(constantRegex)) {
                throw new IllegalArgumentException("Read line (" + line + ") is expected to be a constant regex ("
                        + constantRegex + ").");
            }
        }


        public int readIntegerValue() throws IOException {
            return readIntegerValue("");
        }

        public int readIntegerValue(String prefixRegex) throws IOException {
            return readIntegerValue(prefixRegex, "");
        }

        public int readIntegerValue(String prefixRegex, String suffixRegex) throws IOException {
            String line = bufferedReader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("File ends before a line is expected to contain an integer value ("
                        + prefixRegex + "<value>" + suffixRegex + ").");
            }
            String value = removePrefixSuffixFromLine(line, prefixRegex, suffixRegex);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Read line (" + line + ") is expected to contain an integer value ("
                        + value + ").", e);
            }
        }

        public String readStringValue() throws IOException {
            return readStringValue("");
        }

        public String readStringValue(String prefixRegex) throws IOException {
            return readStringValue(prefixRegex, "");
        }

        public String readStringValue(String prefixRegex, String suffixRegex) throws IOException {
            String line = bufferedReader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("File ends before a line is expected to contain an string value ("
                        + prefixRegex + "<value>" + suffixRegex + ").");
            }
            return removePrefixSuffixFromLine(line, prefixRegex, suffixRegex);
        }

        public String removePrefixSuffixFromLine(String line, String prefixRegex, String suffixRegex) {
            String value = line.trim();
            if (!value.matches("^" + prefixRegex + ".*")) {
                throw new IllegalArgumentException("Read line (" + line + ") is expected to start with prefixRegex ("
                        + prefixRegex + ").");
            }
            value = value.replaceAll("^" + prefixRegex + "(.*)", "$1");
            if (!value.matches(".*" + suffixRegex + "$")) {
                throw new IllegalArgumentException("Read line (" + line + ") is expected to end with suffixRegex ("
                        + suffixRegex + ").");
            }
            value = value.replaceAll("(.*)" + suffixRegex + "$", "$1");
            value = value.trim();
            return value;
        }

        // ************************************************************************
        // Split methods
        // ************************************************************************




        public String[] splitBySpacesOrTabs(String line) {
            return splitBySpacesOrTabs(line, null);
        }

        public String[] splitBySpacesOrTabs(String line, Integer numberOfTokens) {
            return splitBy(line, "[\\ \\t]+", "spaces or tabs", numberOfTokens, false, false);
        }





        public String[] splitBy(String line, String delimiterRegex, String delimiterName,
                Integer numberOfTokens, boolean trim, boolean removeQuotes) {
            return splitBy(line, delimiterRegex, delimiterName, numberOfTokens, numberOfTokens, trim, removeQuotes);
        }

        public String[] splitBy(String line, String delimiterRegex, String delimiterName,
                Integer minimumNumberOfTokens, Integer maximumNumberOfTokens, boolean trim, boolean removeQuotes) {
            String[] lineTokens = line.split(delimiterRegex);
            if (removeQuotes) {
                List<String> lineTokenList = new ArrayList<>(lineTokens.length);
                for (int i = 0; i < lineTokens.length; i++) {
                    String token = lineTokens[i];
                    while ((trim ? token.trim() : token).startsWith("\"") && !(trim ? token.trim() : token).endsWith("\"")) {
                        i++;
                        if (i >= lineTokens.length) {
                            throw new IllegalArgumentException("The line (" + line
                                    + ") has an invalid use of quotes (\").");
                        }
                        String delimiter;
                        switch (delimiterRegex) {
                            case "\\ ":
                                delimiter = " ";
                                break;
                            case "\\,":
                                delimiter = ",";
                                break;
                            default:
                                throw new IllegalArgumentException("Not supported delimiterRegex (" + delimiterRegex + ")");
                        }
                        token += delimiter + lineTokens[i];
                    }
                    if (trim) {
                        token = token.trim();
                    }
                    if (token.startsWith("\"") && token.endsWith("\"")) {
                        token = token.substring(1, token.length() - 1);
                        token = token.replaceAll("\"\"", "\"");
                    }
                    lineTokenList.add(token);
                }
                lineTokens = lineTokenList.toArray(new String[0]);
            }
            if (minimumNumberOfTokens != null && lineTokens.length < minimumNumberOfTokens) {
                throw new IllegalArgumentException("Read line (" + line + ") has " + lineTokens.length
                        + " tokens but is expected to contain at least " + minimumNumberOfTokens
                        + " tokens separated by " + delimiterName + ".");
            }
            if (maximumNumberOfTokens != null && lineTokens.length > maximumNumberOfTokens) {
                throw new IllegalArgumentException("Read line (" + line + ") has " + lineTokens.length
                        + " tokens but is expected to contain at most " + maximumNumberOfTokens
                        + " tokens separated by " + delimiterName + ".");
            }
            if (trim) {
                for (int i = 0; i < lineTokens.length; i++) {
                    lineTokens[i] = lineTokens[i].trim();
                }
            }
            return lineTokens;
        }

    }

}
