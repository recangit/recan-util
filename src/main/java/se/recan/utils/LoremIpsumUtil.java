package se.recan.utils;

import java.util.Random;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/*
 Copyright (c) 2012, Robert Tomsick <rtomsick@unc.edu>
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: 

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution. 

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies, 
 either expressed or implied, of the University of North Carolina.

 */
/**
 * Lorem ipsum text generator. Will produce an arbitrary number of
 * somewhat-reasonable-looking lorem ipsum lines. Not terribly fast. Somewhat
 * hack-ish. Then again, if you wanted pristine output and code, you probably
 * wouldn't want lorem ipsum.
 *
 * @author Robert Tomsick (rtomsick@unc.edu)
 * @version 1.0
 */
public class LoremIpsumUtil {

    private static final Logger LOGGER = Logger.getLogger("Logger");

    private static final String[] TEXT = {
        "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
        "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    };

    private final String LINES[];
    private String str = null;
    private final Random RANDOM;

    /**
     * Construct a new lorem ipsum corpus consisting of the given number of
     * sentences.
     *
     * @param sentences sentences to construct
     */
    private LoremIpsumUtil(int sentences) {
        this.RANDOM = new Random();
        this.LINES = new String[sentences];
        for (int i = 0; i < sentences; i++) {
            this.LINES[i] = strFry(TEXT[RANDOM.nextInt(TEXT.length)]);
        }
    }

    public static final String randomCorpus(int sentences) {
        return new LoremIpsumUtil(sentences).randomCorpus();
    }

    public static final String randomCorpusWords(int sentences) {
        String[] words = new LoremIpsumUtil(1).randomCorpus().split(" ");

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sentences; i++) {
            builder.append(words[i]);
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    public static String[] randomCorpusLines(int sentences) {

        return new LoremIpsumUtil(sentences).LINES;
    }

    private String randomCorpus() {
        if (this.str == null) {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < this.LINES.length; i++) {
                b.append(this.LINES[i]);
                if (i + 1 < this.LINES.length) {
                    b.append(" ");
                }
            }
            this.str = b.toString();
        }
        return this.str;
    }

    /**
     * Randomly replace some words of the given line with randomly-selected
     * words from the static lipsum text.
     *
     * @param line
     * @return
     */
    private String strFry(String line) {
        final int REPLACE_RATE = 30;
        final int DROP_RATE = 5;

        StringBuilder builder = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(line);

        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            if (RANDOM.nextInt(100) < REPLACE_RATE) {
                String newWord = randomLipsumWord();
                if (!word.toLowerCase().equals(word)) { /* need to change case of replacement */

                    newWord = newWord.substring(0, 1).toUpperCase()
                            + newWord.substring(1);
                }

                /* match ending punctuation if necessary */
                word = stripTrailingPunc(newWord);
            }

            /* we'll only drop lower-case words -- lazy hack to avoid
             * killing the first word in our sentence.
             */
            if (RANDOM.nextInt(100) > DROP_RATE
                    || !word.toLowerCase().equals(word)) {
                builder.append(word);

                if (tokenizer.hasMoreTokens()) {
                    builder.append(" ");
                } else {
                    if (word.charAt(word.length() - 1) != '.') {
                        builder.append('.');
                    }
                }
            }
        }

        return builder.toString();
    }

    private String stripTrailingPunc(String word) {
        if (word.charAt(word.length() - 1) == '.'
                || word.charAt(word.length() - 1) == ',') {
            return word.substring(0, word.length() - 1);
        }
        return word;
    }

    private String randomLipsumWord() {

        final String line = TEXT[RANDOM.nextInt(TEXT.length)];

        StringTokenizer tokenizer = new StringTokenizer(line);

        for (int chosenToken = RANDOM.nextInt(tokenizer.countTokens());
                chosenToken > 0;
                chosenToken--) {
            tokenizer.nextToken();
        }

        return stripTrailingPunc(tokenizer.nextToken().toLowerCase());
    }
}
