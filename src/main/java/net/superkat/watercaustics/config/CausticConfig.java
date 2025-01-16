package net.superkat.watercaustics.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class CausticConfig extends MidnightConfig {

    public static final String RENDERING = "rendering";
    public static final String EXTRAS = "extras";

    @Entry(category = RENDERING) public static boolean fancyRendering = true;
    @Entry(category = RENDERING) public static boolean renderBeneathGlass = true;
    @Entry(category = RENDERING) public static int glassFalloffDistance = 12;
    @Entry(category = RENDERING) public static boolean colorOverride = false;
    @Entry(category = RENDERING, isColor = true) public static String causticColor = "#ffffff";
    @Entry(category = EXTRAS) public static boolean modEnabled = true;

}
