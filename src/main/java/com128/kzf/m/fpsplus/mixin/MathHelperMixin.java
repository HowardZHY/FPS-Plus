package com128.kzf.m.fpsplus.mixin;

import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MathHelper.class)
public class MathHelperMixin {
    private static final float PI = 3.1415927f;
    private static final float TWO_PI = PI * 2;
    private static final float HALF_PI = PI / 2;
    private static final float ONE_AND_HALF_PI = PI + HALF_PI;
    private static final double DTWO_PI = Math.PI * 2;

    private static final int SIN_BITS = 14;
    private static final int SIN_MASK = ~(-1 << SIN_BITS);
    private static final int SIN_COUNT = SIN_MASK + 1;
    private static final float[] SIN_TABLE = new float[SIN_COUNT];

    private static final float radFull = TWO_PI;
    private static final float degFull = 360;
    private static final float radToIndex = SIN_COUNT / radFull;
    private static final float degToIndex = SIN_COUNT / degFull;

    private static final float RAD_TO_DEG = 180 / PI;
    private static final double DRAD_TO_DEG = 180 / Math.PI;
    private static final float DEG_TO_RAD = PI / 180;
    private static final double DDEG_TO_RAD = Math.PI / 180;

    static {
        for(int i = 0; i < SIN_COUNT; i++) {
            SIN_TABLE[i] = (float) Math.sin((i + 0.5f) / SIN_COUNT * radFull);
        }

        for(int i = 0; i < 360; i += 90) {
            SIN_TABLE[(int) (i * degToIndex) & SIN_MASK] = (float) Math.sin(i * DEG_TO_RAD);
        }
    }
    /**
     * @author abandenz
     * @reason HowardZHY
     */
    @Overwrite
    public static final float sin(float radians) {
        return SIN_TABLE[(int) (radians * radToIndex) & SIN_MASK];
    }
    /**
     * @author abandenz
     * @reason HowardZHY
     */
    @Overwrite
    public static final float cos(float radians) {
        return SIN_TABLE[(int) ((radians + PI / 2) * radToIndex) & SIN_MASK];
    }
}