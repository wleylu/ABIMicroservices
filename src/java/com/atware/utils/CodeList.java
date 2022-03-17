/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.utils;

/**
 *
 * @author Melarga.COULIBALY
 */

public class CodeList
{

    private static String codedMacList = "a32a9d2555e5aa4ecd6dc485ca7851645a752c35fb87cc9f7d52f31b715af9c16c0ab5e69f46d00e" +
"0abc4dd56d97e3953e3b8b128cf7a33c5c52e836fbceda0696cc65284be7266fe51f35dedff384e8" +
"2752e02a544ca4ba3500a3b73c7bf29fcb06ce6c9d4921646270ffb9f4ce8d336650b7005e8de115" +
"d0f75ea9fb90f1dc8b6e53ed18185b88b9b368120dd9af2b2ba9e86774da002a6e5d1705de22c14c" +
"95771988ec141a37110abbaa732f3ca1caeec2d83b07e881520ad000a3ae992c23b7087566bb250c" +
"21eb74c4d4b665d37872ea7fbab590d5b9c90fa1072810af6737c21ae6467bc70f67a8d94935bc76" +
"a687f12958eabb92ac27a6e52a61e400381c8e4301f1de51e1ea0c97f0387f8874c352680e0eca4d" +
"e48d08550657d73dfa505212c5bbe2f906dde084b39e12492c94867b141a07b9ef14969baa7184c8" +
"8bf8907237510874046cbcf846a539bc711c5a543ea32c99f64625768fa84a4c"
;

    public CodeList()
    {
    }

    public static boolean contains(String codedKey)
    {
        MD5 md = new MD5();
        md.update(codedKey);
        String codedMac = md.digest();
        return codedMacList.contains(codedMac);
    }

}
