package com128.kzf.m.fpsplus.mixin;

import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MathHelper.class)
public class MathHelperMixin {
    private static final float RAD, DEG, SIN_TO_COS;
    private static final int SIN_BITS, SIN_MASK, SIN_MASK2, SIN_COUNT, SIN_COUNT2;
    private static final float radFull, radToIndex;
    private static final float degFull, degToIndex;
    private static final float[] sinFull, sinHalf;

    static {
        RAD = (float) Math.PI / 180.0f;
        DEG = 180.0f / (float) Math.PI;
        SIN_TO_COS = (float) (Math.PI * 0.5f);

        SIN_BITS = 12;
        SIN_MASK = ~(-1 << SIN_BITS);
        SIN_MASK2 = SIN_MASK >> 1;
        SIN_COUNT = SIN_MASK + 1;
        SIN_COUNT2 = SIN_MASK2 + 1;

        radFull = (float) (Math.PI * 2.0);
        degFull = (float) (360.0);
        radToIndex = SIN_COUNT / radFull;
        degToIndex = SIN_COUNT / degFull;

        sinFull = new float[SIN_COUNT];
        for (int i = 0; i < SIN_COUNT; i++) {
            sinFull[i] = (float) Math.sin((i + Math.min(1, i % (SIN_COUNT / 4)) * 0.5) / SIN_COUNT * radFull);
        }

        sinHalf = new float[SIN_COUNT2];
        for (int i = 0; i < SIN_COUNT2; i++) {
            sinHalf[i] = (float) Math.sin((i + Math.min(1, i % (SIN_COUNT / 4)) * 0.5) / SIN_COUNT * radFull);
        }
    }
    /**
     * @author abandenz
     * @reason HowardZHY
     */
    @Overwrite
    public static final float sin(float rad) {
        int index1 = (int) (rad * radToIndex) & SIN_MASK;
        int index2 = index1 & SIN_MASK2;
        int mul = ((index1 == index2) ? +1 : -1);
        return sinHalf[index2] * mul;
    }
    /**
     * @author abandenz
     * @reason HowardZHY
     */
    @Overwrite
    public static final float cos(float rad) {
        return sin(rad + SIN_TO_COS);
    }
}