package cn.scu.ikuto.objloader.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LineReader {

    private BufferedReader m_BufferedReader;
    public int m_ReadCount = 0;

    public LineReader(InputStream vInputStream) {
        this.m_BufferedReader = this.getBufferedReaderFrom(vInputStream);
    }

    public String readLine() {
        String line = null;
        try {
            line = this.m_BufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.m_ReadCount++;
        return line;
    }

    private BufferedReader getBufferedReaderFrom(InputStream vInputStream) {
        DataInputStream dataInputStream = new DataInputStream(vInputStream);
        InputStreamReader inputStreamReader = new InputStreamReader(dataInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return bufferedReader;
    }
}

