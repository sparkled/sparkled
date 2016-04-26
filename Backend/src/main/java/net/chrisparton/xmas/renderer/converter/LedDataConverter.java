package net.chrisparton.xmas.renderer.converter;

import net.chrisparton.xmas.renderer.data.Led;

import java.nio.ByteBuffer;

public class LedDataConverter {

    private LedDataConverter() {
    }

    /**
     * @param led The LED to convert into a 2-bit colour.
     * @return a short containing 5 R bits, 6 G bits and 5 B bits (RRRRRGGGGGGBBBBB).
     */
    public static byte[] generate16BitLedData(Led led) {
        short ledData = 0;
        short rData = (byte) Math.round(led.getR() / (ConverterConstants.MAX_BYTE_VALUE / 31.0));
        short gData = (byte) Math.round(led.getG() / (ConverterConstants.MAX_BYTE_VALUE / 63.0));
        short bData = (byte) Math.round(led.getB() / (ConverterConstants.MAX_BYTE_VALUE / 31.0));

        ledData |= rData << 11;
        ledData |= gData << 5;
        ledData |= bData;

        return ByteBuffer.allocate(2).putShort(ledData).array();
    }
}
