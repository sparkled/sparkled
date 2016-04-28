package net.chrisparton.sparkled.renderer.converter;

import net.chrisparton.sparkled.renderer.data.Led;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class LedDataConverterTest {

    @Test
    public void can_generate_lowest_16_bit_led_data() throws Exception {
        Led led = new Led(0, 0, 0);
        byte[] ledBytes = LedDataConverter.generate16BitLedData(led);
        assertThat(ledBytes[0] & 255, is(0b00000000));
        assertThat(ledBytes[1] & 255, is(0b00000000));
    }

    @Test
    public void can_generate_highest_16_bit_led_data() throws Exception {
        Led led = new Led(255, 255, 255);
        byte[] ledBytes = LedDataConverter.generate16BitLedData(led);
        assertThat(ledBytes[0] & 255, is(0b11111111));
        assertThat(ledBytes[1] & 255, is(0b11111111));
    }

    @Test
    public void can_generate_mid_range_16_bit_led_data() throws Exception {
        Led led = new Led(127, 127, 127);
        byte[] ledBytes = LedDataConverter.generate16BitLedData(led);
        assertThat(ledBytes[0] & 255, is(0b01111011));
        assertThat(ledBytes[1] & 255, is(0b11101111));
    }
}