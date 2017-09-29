/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package rip.anticheat.anticheat.util.formatting;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.anticheat.anticheat.util.misc.ReflectionUtil;

public class ActionMessageUtil {
    private List<AMText> Text = new ArrayList<AMText>();
    private static Method CONVERT_TO_IBASE = null;
    private static Class<?> IBASE = null;

    public AMText addText(String string) {
        AMText aMText = new AMText(string);
        this.Text.add(aMText);
        return aMText;
    }

    public String getFormattedMessage() {
        String string = "[\"\",";
        for (AMText aMText : this.Text) {
            string = String.valueOf(String.valueOf(string)) + aMText.getFormattedMessage() + ",";
        }
        string = string.substring(0, string.length() - 1);
        string = String.valueOf(String.valueOf(string)) + "]";
        return string;
    }

    public void sendToPlayer(Player player) {
        try {
            Object object = ActionMessageUtil.convertToIBase(this.getFormattedMessage());
            Object obj = ReflectionUtil.getNMSClass("PacketPlayOutChat").getConstructor(IBASE, Byte.TYPE).newInstance(object, Byte.valueOf(1));
            ReflectionUtil.sendPacket(player, obj);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void sendToPlayer(Player player, String string) {
    }

    private static Object convertToIBase(String string) {
        if (CONVERT_TO_IBASE == null) {
            try {
                Method method;
                IBASE = ReflectionUtil.getNMSClass("IChatBaseComponent");
                Class class_ = null;
                try {
                    class_ = ReflectionUtil.getNMSClass("IChatBaseComponent.ChatSerializer");
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (class_ == null) {
                    try {
                        class_ = ReflectionUtil.getNMSClass("ChatSerializer");
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                if ((method = ReflectionUtil.getMethod(class_, "a", new Class[]{String.class})) != null) {
                    CONVERT_TO_IBASE = method;
                    return ActionMessageUtil.convertToIBase(string);
                }
                throw new UnsupportedOperationException();
            }
            catch (Exception exception) {
                System.out.println("Unable to find ChatSerializer#a are you running a custom build?");
                exception.printStackTrace();
            }
        } else {
            try {
                return CONVERT_TO_IBASE.invoke(null, string);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public class AMText {
        private String Message;
        private Map<String, Map.Entry<String, String>> Modifiers;

        public AMText(String string) {
            this.Message = "";
            this.Modifiers = new HashMap<String, Map.Entry<String, String>>();
            this.Message = string;
        }

        public String getMessage() {
            return this.Message;
        }

        public String getFormattedMessage() {
            String string = "{\"text\":\"" + this.Message + "\"";
            for (String string2 : this.Modifiers.keySet()) {
                Map.Entry<String, String> entry = this.Modifiers.get(string2);
                string = String.valueOf(String.valueOf(string)) + ",\"" + string2 + "\":{\"action\":\"" + entry.getKey() + "\",\"value\":" + entry.getValue() + "}";
            }
            string = String.valueOf(String.valueOf(string)) + "}";
            return string;
        }

        public /* varargs */ AMText addHoverText(String ... arrstring) {
            Object object;
            String string;
            String string2 = "hoverEvent";
            String string3 = "show_text";
            if (arrstring.length == 1) {
                string = "{\"text\":\"" + arrstring[0] + "\"}";
            } else {
                string = "{\"text\":\"\",\"extra\":[";
                String[] arrstring2 = arrstring;
                int n = arrstring2.length;
                int n2 = 0;
                while (n2 < n) {
                    object = arrstring2[n2];
                    string = String.valueOf(String.valueOf(string)) + "{\"text\":\"" + (String)object + "\"},";
                    ++n2;
                }
                string = string.substring(0, string.length() - 1);
                string = String.valueOf(String.valueOf(string)) + "]}";
            }
            object = new AbstractMap.SimpleEntry<String, String>(string3, string);
            this.Modifiers.put(string2, (Map.Entry<String, String>)object);
            return this;
        }

        public AMText addHoverItem(ItemStack itemStack) {
            return this;
        }

        public AMText setClickEvent(ClickableType clickableType, String string) {
            String string2 = "clickEvent";
            String string3 = clickableType.Action;
            AbstractMap.SimpleEntry<String, String> simpleEntry = new AbstractMap.SimpleEntry<String, String>(string3, "\"" + string + "\"");
            this.Modifiers.put(string2, simpleEntry);
            return this;
        }
    }

    public static enum ClickableType {
        RunCommand("RunCommand", 0, "run_command"),
        SuggestCommand("SuggestCommand", 1, "suggest_command"),
        OpenURL("OpenURL", 2, "open_url");
        
        public String Action;

        private ClickableType(String string2, int n2, String string3, int n3, String string4) {
            this.Action = string3;
        }
    }

}

