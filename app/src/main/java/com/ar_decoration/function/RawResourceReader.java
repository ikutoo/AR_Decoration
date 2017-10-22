package com.ar_decoration.function;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class RawResourceReader {
    public static String readTextFileFromRawResource(final Context Context, final int ResourceId) {
        final InputStream InputStream = Context.getResources().openRawResource(ResourceId);
        final InputStreamReader InputStreamReader = new InputStreamReader(InputStream);
        final BufferedReader BufferedReader = new BufferedReader(InputStreamReader);

        String NextLine;
        final StringBuilder Body = new StringBuilder();

        try {
            while ((NextLine = BufferedReader.readLine()) != null) {
                Body.append(NextLine);
                Body.append('\n');
            }
        } catch (IOException e) {
            return null;
        }

        return Body.toString();
    }
}
