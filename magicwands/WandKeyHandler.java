package magicwands;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

public class WandKeyHandler {
	public static final KeyBinding help = new KeyBinding("Wand_Help", Keyboard.KEY_LCONTROL, "key.categories.item.special");
	public static final KeyBinding key_1 = new KeyBinding("Wand_Key_1", Keyboard.KEY_X, "key.categories.item.special");
	public static final KeyBinding key_2 = new KeyBinding("Wand_Key_2", Keyboard.KEY_C, "key.categories.item.special");
	public static final KeyBinding key_3 = new KeyBinding("Wand_Key_3", Keyboard.KEY_V, "key.categories.item.special");

	public WandKeyHandler() {
		for(KeyBinding key:new KeyBinding[] { key_1, key_2, key_3, help }){
            ClientRegistry.registerKeyBinding(key);
        }
	}

	@SubscribeEvent
	public void keyDown(InputEvent.KeyInputEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof WandItem) {
            if (help.getIsKeyPressed()) {
                printHelp(player, (WandItem) player.getCurrentEquippedItem().getItem());
                return;
            }
            int keys = (key_1.getIsKeyPressed()?100:0) + (key_2.getIsKeyPressed() ? 10 : 0) + (key_3.getIsKeyPressed() ? 1 : 0);
            MagicWands.channel.sendToServer(new WandKeyPacket(player.getEntityId(), keys).getPacket(Side.SERVER));
        }
	}

	// control - help
	public void printHelp(EntityPlayer player, WandItem wand) {
		if (wand instanceof BuildWand) {
			addChatMessage(player, "=== " + StatCollector.translateToLocal(wand.reinforced ? "item.rbuwand.name" : "item.buwand.name") + " ===");
			addChatMessage(player, Keyboard.getKeyName(key_1.getKeyCode()) + " - " + StatCollector.translateToLocal("help.build.key1"));
			addChatMessage(player, Keyboard.getKeyName(key_2.getKeyCode()) + " - " + StatCollector.translateToLocal("help.build.key2"));
			addChatMessage(player, Keyboard.getKeyName(key_3.getKeyCode()) + " - " + StatCollector.translateToLocal("help.build.key3"));
			if (wand.reinforced) {
				addChatMessage(player, Keyboard.getKeyName(key_1.getKeyCode()) + "+" + Keyboard.getKeyName(key_2.getKeyCode()) + " - " + StatCollector.translateToLocal("help.rbuild.key1.2"));
				addChatMessage(player, Keyboard.getKeyName(key_1.getKeyCode()) + "+" + Keyboard.getKeyName(key_2.getKeyCode()) + "+" + Keyboard.getKeyName(key_3.getKeyCode()) + " - "
                        + StatCollector.translateToLocal("help.rbuild.key1.2.3"));
			}
			addChatMessage(player, Keyboard.getKeyName(key_1.getKeyCode()) + "+" + Keyboard.getKeyName(key_3.getKeyCode()) + " - " + StatCollector.translateToLocal("help.build.key1.3"));
			if (wand.reinforced) {
				addChatMessage(player, Keyboard.getKeyName(key_2.getKeyCode()) + "+" + Keyboard.getKeyName(key_3.getKeyCode()) + " - " + StatCollector.translateToLocal("help.rbuild.key2.3"));
			}
		} else if (wand instanceof BreakWand) {
			addChatMessage(player, "=== " + StatCollector.translateToLocal(wand.reinforced ? "item.rbrwand.name" : "item.brwand.name") + " ===");
			addChatMessage(player, Keyboard.getKeyName(key_1.getKeyCode()) + " - " + StatCollector.translateToLocal("help.break.key1"));
			addChatMessage(player, Keyboard.getKeyName(key_2.getKeyCode()) + " - " + StatCollector.translateToLocal("help.break.key2"));
			addChatMessage(player, Keyboard.getKeyName(key_3.getKeyCode()) + " - " + StatCollector.translateToLocal("help.break.key3"));
		} else if (wand instanceof MineWand) {
			addChatMessage(player, "=== " + StatCollector.translateToLocal(wand.reinforced ? "item.rmiwand.name" : "item.miwand.name") + " ===");
			addChatMessage(player, Keyboard.getKeyName(key_1.getKeyCode()) + " - " + StatCollector.translateToLocal("help.mine.key1"));
			addChatMessage(player, Keyboard.getKeyName(key_2.getKeyCode()) + " - " + StatCollector.translateToLocal("help.mine.key2"));
			addChatMessage(player, Keyboard.getKeyName(key_3.getKeyCode()) + " - " + StatCollector.translateToLocal("help.mine.key3"));
			addChatMessage(player, Keyboard.getKeyName(key_1.getKeyCode()) + "+" + Keyboard.getKeyName(key_2.getKeyCode()) + " - " + StatCollector.translateToLocal("help.mine.key1.2"));
		}
	}

    public static void addChatMessage(EntityPlayer player, String message){
        player.addChatComponentMessage(new ChatComponentText(message));
    }
}
